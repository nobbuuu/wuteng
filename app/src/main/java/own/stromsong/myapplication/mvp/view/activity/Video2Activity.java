package own.stromsong.myapplication.mvp.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.mvp.base.MvpActivity;
import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean.ListshowBean;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean.ListshowBean.Material;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean.ListshowBean.Material.MaterialBean;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean;
import own.stromsong.myapplication.mvp.model.MenuBean.SubtitlesBean;
import own.stromsong.myapplication.mvp.model.RefreshActList;
import own.stromsong.myapplication.mvp.presenter.Video2Presenter;
import own.stromsong.myapplication.mvp.view.interfaces.IVideo2Act;

public class Video2Activity extends MvpActivity<Video2Presenter> implements IVideo2Act {

    @BindView(R.id.root_rl)
    RelativeLayout mRootRl;
    @BindView(R.id.top_tv)
    TextView mTopTv;
    @BindView(R.id.bottom_tv)
    TextView mBottomTv;
    @BindView(R.id.zimu)
    RelativeLayout mZimu;
    @BindView(R.id.wait)
    TextView mWait;

    private List<ListResultBean> list;
    private List<SubtitlesBean> subtitles;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(("yyyy:MM:dd:HH:mm:ss"));
    private Date mDate = new Date();
    private List<ListshowBean> showsList = new ArrayList<>();//节目集合
    private Map map = new HashMap<String, ListResultBean>();//节目单

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video2;
    }

    @Override
    public void getMenuList(List<ListResultBean> showMenu, List<SubtitlesBean> subtitle) {

        list = showMenu;//节目单集合
        subtitles = subtitle;//字幕

        if (list != null) {
            map.clear();
            showsList.clear();
            for (int i = 0; i < list.size(); i++) {
                ListResultBean bean = list.get(i);
                mDate.setTime(bean.getTime());
                String format = simpleDateFormat.format(mDate);
                Log.e("aa", "format---" + format);
                map.put(format, bean);
            }
        }
        if (subtitles != null) {
            setZimu();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshActList mRefreshActList) {
        Log.e("aa", "更新数据");
        mHandler.removeMessages(1);
        mvpPresenter.getList();
    }

    @Override
    protected void initData() {
        setBarVisible(false);
        EventBus.getDefault().register(this);
        setTimer();//设置定时器
        mvpPresenter.getList();
    }

    private void setTimer() {
        timer = new Timer("定时器,立即启动，1秒执行一次");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long millis = System.currentTimeMillis();
                mDate.setTime(millis);
                String format = simpleDateFormat.format(mDate);
                if (map.containsKey(format)) {
                    showsList.clear();
                    ListResultBean listResultBean = (ListResultBean) map.get(format);//获取个节目单
                    showsList.addAll(listResultBean.getListshow());//添加一个节目单的所有节目
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWait.setVisibility(View.GONE);
                            setData();//开始播放节目
                        }
                    });
                }
            }
        }, 0, 1000);
    }

    /**
     * 播放一个节目 handler 播放该节目所有素材
     */
    private void setData() {
        ListshowBean bean = null;
        try {
            mRootRl.removeAllViews();
            bean = showsList.get(0);//播放第一条,如果没节目会走异常
            playShow(bean.getListMaterial());
            mRootRl.postInvalidate();
        } catch (Exception e) {
            showToastMsgShort("没有新节目了");//没有节目 上个节目会重复播放
        } finally {
            if (bean != null) {
                mHandler.sendEmptyMessageDelayed(1, bean.getShows().getPlayTime() * 60 * 1000);
                showsList.remove(bean);
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setData();
        }
    };

    private void setZimu() {
        if (subtitles == null) {
            return;
        }
        for (SubtitlesBean subtitle : subtitles) {
            if (subtitle.getLocation() == 0 && subtitle.getStatus().equalsIgnoreCase("1")) {
                mTopTv.setText(subtitle.getMsgContext());
                mTopTv.setTextColor(Color.parseColor(subtitle.getMsgColor()));
                mTopTv.setBackgroundColor(Color.parseColor(subtitle.getBgColor()));
                mTopTv.setTextSize(subtitle.getFontSize());
            } else if (subtitle.getLocation() == 1 && subtitle.getStatus().equalsIgnoreCase("1")) {
                mBottomTv.setText(subtitle.getMsgContext());
                mBottomTv.setTextColor(Color.parseColor(subtitle.getMsgColor()));
                mBottomTv.setTextSize(subtitle.getFontSize());
                mBottomTv.setBackgroundColor(Color.parseColor(subtitle.getBgColor()));
            }
        }
    }

    /**
     * 播放节目
     *
     * @param materialList
     */
    private void playShow(List<Material> materialList) {
        if (materialList != null && materialList.size() != 0) {
            for (int i = 0; i < materialList.size(); i++) {
                //1.图片 2.音频 3.网络视频 4.本地视频 5.HTML 6.PPT 7.Word 8.Excel 9.PDF 10.背景
                ShowsMaterialBean showsMaterialBean = materialList.get(i).getShowsMaterial();
                MaterialBean materialBean = materialList.get(i).getMaterial();
                if (materialBean != null && showsMaterialBean != null) {
                    int wide = showsMaterialBean.getWide();
                    int hige = showsMaterialBean.getHige();
                    int x = Integer.valueOf(showsMaterialBean.getX());
                    int y = Integer.valueOf(showsMaterialBean.getY());
                    String url = materialBean.getFile();
                    int type = materialBean.getType();
                    switch (type) {
                        case 1:
                            mRootRl.addView(getImage(url, wide, hige, x, y));
                            break;
                        case 2:
                            playSound(this, url);
                            break;
                        case 3:
                        case 4:
                            mRootRl.addView(getVideoPlayer(url, wide, hige, x, y));
                            break;
                        case 5:
                            mRootRl.addView(getWebView(url, wide, hige, x, y));
                            break;
                        case 10:
                            Glide.with(this).load(url).into(new ViewTarget<RelativeLayout, GlideDrawable>(mRootRl) {
                                @Override
                                public void onResourceReady(GlideDrawable resource, GlideAnimation anim) {
                                    mRootRl.setBackground(resource);
                                }
                            });
                            break;
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < mRootRl.getChildCount(); i++) {
            View childAt = mRootRl.getChildAt(i);
            if (childAt instanceof VideoView) {
                VideoView view = (VideoView) childAt;
                if (!view.isPlaying()) {
                    view.resume();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (int i = 0; i < mRootRl.getChildCount(); i++) {
            View childAt = mRootRl.getChildAt(i);
            if (childAt instanceof VideoView) {
                VideoView view = (VideoView) childAt;
                if (!view.canPause()) {
                    view.pause();
                }
            }
        }
    }

    @Override
    protected Video2Presenter createPresenter() {
        return new Video2Presenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlaybackVideo();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (mDate != null) {
            mDate = null;
        }
        EventBus.getDefault().unregister(this);
    }

    private void stopPlaybackVideo() {
        try {
            for (int i = 0; i < mRootRl.getChildCount(); i++) {
                View childAt = mRootRl.getChildAt(i);
                if (childAt instanceof VideoView) {
                    VideoView view = (VideoView) childAt;
                    view.stopPlayback();
                }
                if (childAt instanceof WebView) {
                    WebView webView = (WebView) childAt;
                    webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                    webView.clearHistory();
                    ((ViewGroup) webView.getParent()).removeView(webView);
                    webView.destroy();
                    webView = null;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 视频播放器
     *
     * @param url
     * @return
     */
    private VideoView getVideoPlayer(String url, int width, int height, int x, int y) {
        final VideoView mVideoPlayer = new VideoView(this);
        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        mVideoPlayer.setLayoutParams(layoutParams);
        mVideoPlayer.setKeepScreenOn(true);
        mVideoPlayer.setVideoURI(Uri.parse(url));
        mVideoPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.start();
            }
        });
        mVideoPlayer.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                showToastMsgShort("无法播放该视频");
                return true;
            }
        });
        return mVideoPlayer;
    }


    /**
     * 播放音频的方法
     */
    private void playSound(Context context, String path) {
        mediaPlayer = new MediaPlayer();
        try {
            //播放之前要先把音频文件重置
            mediaPlayer.reset();
            //调用方法传进去要播放的音频路径
            mediaPlayer.setDataSource(context, Uri.parse(path));
            //异步准备音频资源
            mediaPlayer.prepareAsync();
            //调用mediaPlayer的监听方法，音频准备完毕会响应此方法
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();//开始音频
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开Html文件
     *
     * @param url
     * @param width
     * @param height
     * @param x
     * @param y
     * @return
     */
    private WebView getWebView(String url, int width, int height, int x, int y) {
        WebView webView = new WebView(this);
        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        webView.setLayoutParams(layoutParams);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //表示等待证书响应
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }
        });
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webView.loadUrl(url);
        return webView;
    }

    /**
     * 图片
     *
     * @param url
     * @param width
     * @param height
     * @param x
     * @param y
     * @return
     */
    private ImageView getImage(String url, int width, int height, int x, int y) {
        ImageView imageView = new ImageView(this);
        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        imageView.setLayoutParams(layoutParams);
        Glide.with(this).load(url).error(R.mipmap.ic_launcher).into(imageView);
        return imageView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
