package own.stromsong.myapplication.mvp.view.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.weavey.loading.lib.LoadingLayout.OnReloadListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.mvp.base.MvpActivity;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean.ListshowBean;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean.ListshowBean.Material;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean.ListshowBean.Material.MaterialBean;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean;
import own.stromsong.myapplication.mvp.model.MenuBean.SubtitlesBean;
import own.stromsong.myapplication.mvp.model.RefreshActList;
import own.stromsong.myapplication.mvp.presenter.Video2Presenter;
import own.stromsong.myapplication.mvp.view.interfaces.IVideo2Act;
import own.stromsong.myapplication.weight.MyVideoView;

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
    private MyVideoView mVideoPlayer;
    private Timer timer;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(("yyyy-MM-dd-HH:mm:ss"));
    private Date mDate = new Date();
    private Set<SubtitlesBean> zimuSet = new HashSet<>();//字幕集合
    private List<ListshowBean> showsList = new ArrayList<>();//节目集合
    private Map map = new HashMap<String, ListResultBean>();//节目单
    private Map zimu = new HashMap<String, SubtitlesBean>();//字幕

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
            Log.e("aa", "节目单listSize--->" + list.size());
            for (int i = 0; i < list.size(); i++) {
                ListResultBean bean = list.get(i);
                String time = bean.getStartTime() / 1000 + "," + bean.getEndTime() / 1000;

                mDate.setTime(bean.getStartTime());
                String StartTime = simpleDateFormat.format(mDate);
                Log.e("aa", "StartTime--->" + StartTime);

                mDate.setTime(bean.getEndTime());
                String EndTime = simpleDateFormat.format(mDate);
                Log.e("aa", "EndTime--->" + EndTime);

                map.put(time, bean);
            }
        }
        if (subtitles != null) {
            zimu.clear();
            zimuSet.clear();
            Log.e("aa", "字幕listSize--->" + subtitles.size());
            for (int i = 0; i < subtitles.size(); i++) {
                SubtitlesBean bean = subtitles.get(i);
                String time = bean.getStartTime() / 1000 + "," + bean.getEndTime() / 1000;

                mDate.setTime(bean.getStartTime());
                String StartTime = simpleDateFormat.format(mDate);
                Log.e("aa", "StartTime--->" + StartTime);

                mDate.setTime(bean.getEndTime());
                String EndTime = simpleDateFormat.format(mDate);
                Log.e("aa", "EndTime--->" + EndTime);

                zimu.put(time, bean);
            }
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
        getLoadLayout().setOnReloadListener(new OnReloadListener() {
            @Override
            public void onReload(View v) {
                mvpPresenter.getList();
            }
        });
    }

    private void setTimer() {
        timer = new Timer("定时器,立即启动，1秒执行一次");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long millis = System.currentTimeMillis() / 1000;
                //遍历map集合
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    String key = entry.getKey().toString();
                    ListResultBean listResultBean = (ListResultBean) entry.getValue();//一个节目单
                    String[] split = key.split(",");
                    if (millis >= Long.valueOf(split[0]) && millis <= Long.valueOf(split[1])) {
                        showsList.clear();
                        showsList.addAll(listResultBean.getListshow());//添加一个节目单的所有节目
                        map.remove(key);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData();//开始播放节目
                                Log.e("aa", i++ + "----i");
                            }
                        });
                        break;
                    }
                }
                //遍历zimu集合
                Iterator it1 = zimu.entrySet().iterator();
                while (it1.hasNext()) {
                    Map.Entry entry = (Map.Entry) it1.next();
                    String key = entry.getKey().toString();
                    SubtitlesBean mSubtitlesBean = (SubtitlesBean) entry.getValue();
                    String[] split = key.split(",");
                    if (millis >= Long.valueOf(split[0]) && millis <= Long.valueOf(split[1])) {
                        zimuSet.add(mSubtitlesBean);//添加一个节目单的所有节目
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                setZimu();//开始展示字幕
                            }
                        });
                    } else {
                        zimuSet.remove(mSubtitlesBean);
                    }
                }
            }
        }, 0, 1000);
    }

    /**
     * 播放一个节目 handler 播放该节目所有素材
     */
    int i = 0;

    private void setData() {
        ListshowBean bean = null;
        try {
            bean = showsList.get(0);//播放第一条,如果没节目会走异常
            mWait.setVisibility(View.GONE);//有节目gone掉等待view
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if (mVideoPlayer != null && mVideoPlayer.isPlaying()) {
                mVideoPlayer.stopPlayback();
            }
            mRootRl.removeAllViews();
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
        if (zimuSet.size() == 0) {
            return;
        }
        for (SubtitlesBean subtitle : subtitles) {
            if (subtitle.getLocation() == 0 && subtitle.getStatus().equalsIgnoreCase("1")) {
                mTopTv.setText(subtitle.getMsgContext());
                mTopTv.setTextColor(Color.parseColor(subtitle.getMsgColor()));
                mTopTv.setBackgroundColor(Color.parseColor(subtitle.getBgColor()));
                mTopTv.setTextSize(Integer.valueOf(subtitle.getFontSize()));
            } else if (subtitle.getLocation() == 1 && subtitle.getStatus().equalsIgnoreCase("1")) {
                mBottomTv.setText(subtitle.getMsgContext());
                mBottomTv.setTextColor(Color.parseColor(subtitle.getMsgColor()));
                mBottomTv.setTextSize(Integer.valueOf(subtitle.getFontSize()));
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
                    String link = materialBean.getLink();
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
                            mRootRl.addView(getWebView(link, wide, hige, x, y));
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
    private View getVideoPlayer(String url, int width, int height, int x, int y) {
        View view = LayoutInflater.from(this).inflate(R.layout.videoview, null);
        mVideoPlayer = (MyVideoView) view.findViewById(R.id.videoview);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        mVideoPlayer = new MyVideoView(this);
        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        view.setLayoutParams(layoutParams);
        mVideoPlayer.setKeepScreenOn(true);
        mVideoPlayer.setVideoURI(Uri.parse(url));
        mVideoPlayer.setOnInfoListener(new OnInfoListener() {
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    //此接口每次回调完START就回调END,若不加上判断就会出现缓冲图标一闪一闪的卡顿现象
                    if (mp.isPlaying()) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
                return true;
            }
        });
        mVideoPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.start();
                progressBar.setVisibility(View.GONE);
            }
        });
        mVideoPlayer.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                showToastMsgShort("无法播放该视频");
                return true;
            }
        });
        return view;
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
    private View getWebView(String url, int width, int height, int x, int y) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.webview, null);
        WebView webView = (WebView) inflate.findViewById(R.id.webview);
        final ProgressBar progressBar = (ProgressBar) inflate.findViewById(R.id.progress);
        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        inflate.setLayoutParams(layoutParams);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

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
        return inflate;
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
        imageView.setScaleType(ScaleType.FIT_XY);
        Glide.with(this).load(url).error(R.mipmap.ic_launcher).into(imageView);
        return imageView;
    }

    @Override
    public void showLoading() {
        showLoading1();
    }

    @Override
    public void showContent() {
        super.showContent();
        goneLoading1();
    }

    @Override
    public void showEmpty() {
        super.showEmpty();
        goneLoading1();
    }

    @Override
    public void showError() {
        super.showError();
        goneLoading1();
    }

    @Override
    public void showNoNetwork() {
        super.showNoNetwork();
        goneLoading1();
    }
}
