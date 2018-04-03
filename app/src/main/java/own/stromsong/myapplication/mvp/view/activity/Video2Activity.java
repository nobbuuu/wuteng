package own.stromsong.myapplication.mvp.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.mvp.base.BaseSupportActivity;
import own.stromsong.myapplication.mvp.model.MenuBean;

public class Video2Activity extends BaseSupportActivity {

    //        String url = "http://183.60.197.29/15/t/j/i/s/tjissokyfzdxotupwpwxjjoufsbshi/he.yinyuetai.com/09040162194A2B01EE029439394C3FBB.mp4?sc\\u003d0d6ab169ff59aea7\\u0026br\\u003d3173\\u0026vid\\u003d2780294\\u0026aid\\u003d6762\\u0026area\\u003dML\\u0026vst\\u003d0";
    String url = "http://120.24.234.123:8080/sysfile/upload/201711/64aa5b11-60df-4cae-8989-6ec194214eff.mp4";
    @BindView(R.id.root_rl)
    RelativeLayout mRootRl;
    @BindView(R.id.top_tv)
    TextView mTopTv;
    @BindView(R.id.bottom_tv)
    TextView mBottomTv;

    private List<MenuBean.ListResultBean> list;
    private List<MenuBean.SubtitlesBean> subtitles;
    private MediaPlayer mediaPlayer;
    private TimeReceiver timeReceiver;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video2;
    }

    /**
     * 节目单 包括字幕
     *
     * @param context
     * @param list
     */
    public static void startVideo2Activity(Context context, List<MenuBean.ListResultBean> list, List<MenuBean.SubtitlesBean> subtitles) {
        Intent intent = new Intent(context, Video2Activity.class);
        intent.putExtra("list", (Serializable) list);
        intent.putExtra("zimu", (Serializable) subtitles);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        list = (List<MenuBean.ListResultBean>) intent.getSerializableExtra("list");//节目单集合
        subtitles = (List<MenuBean.SubtitlesBean>) getIntent().getSerializableExtra("zimu");
        if (list != null) {
            mRootRl.removeAllViews();
            showsList.clear();
            map.clear();
            for (int i = 0; i < list.size(); i++) {
                MenuBean.ListResultBean bean = list.get(i);
                map.put(bean.getTime(), bean);
            }
            setData();
            mRootRl.postInvalidate();
        }
        if (subtitles != null) {
            mTopTv.setText("");
            mBottomTv.setText("");
            setZimu();
        }
    }

    private List<MenuBean.ListResultBean.ListshowBean> showsList = new ArrayList<>();//节目集合

    private Map map = new HashMap<Long, MenuBean.ListResultBean>();

    @Override

    protected void initLocalData() {
        super.initLocalData();
        setBarVisible(false);
        list = (List<MenuBean.ListResultBean>) getIntent().getSerializableExtra("list");//节目单集合
        subtitles = (List<MenuBean.SubtitlesBean>) getIntent().getSerializableExtra("zimu");
        if (list != null) {
            map.clear();
            for (int i = 0; i < list.size(); i++) {
                MenuBean.ListResultBean bean = list.get(i);
                map.put(bean.getTime(), bean);
            }
            setData();//开始播放节目
        }
        if (subtitles != null) {
            setZimu();
        }

        timeReceiver = new TimeReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(timeReceiver, filter);
    }

    private void setZimu() {
        if (subtitles == null) {
            return;
        }
        for (MenuBean.SubtitlesBean subtitle : subtitles) {
            if (subtitle.getLocation() == 0 && subtitle.getStatus().equalsIgnoreCase("1")) {
                mTopTv.setText(subtitle.getMsgContext());
                mTopTv.setTextColor(Color.parseColor(subtitle.getMsgColor()));
//                mTopTv.setBackgroundColor(Color.parseColor(subtitle.getBgColor()));
                mTopTv.setTextSize(subtitle.getFontSize());
            } else if (subtitle.getLocation() == 1 && subtitle.getStatus().equalsIgnoreCase("1")) {
                mBottomTv.setText(subtitle.getMsgContext());
                mBottomTv.setTextColor(Color.parseColor(subtitle.getMsgColor()));
                mBottomTv.setTextSize(subtitle.getFontSize());
//                mBottomTv.setBackgroundColor(Color.parseColor(subtitle.getBgColor()));
            }
        }
    }

    private void setData() {
        MenuBean.ListResultBean.ListshowBean bean = null;
        try {
            bean = showsList.get(0);//播放第一条,如果没节目会走异常
            playShow(bean.getListMaterial());
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

    /**
     * 播放节目
     *
     * @param materialList
     */
    private void playShow(List<MenuBean.ListResultBean.ListshowBean.Material> materialList) {
        if (materialList != null && materialList.size() != 0) {
//            mRootRl.addView(getImage("http://img05.tooopen.com/images/20150820/tooopen_sy_139205349641.jpg", 350, 200, 200, 150));
//            mRootRl.addView(getVideoPlayer(url, 400, 350, 1150, 250, (float) 0));
//            mRootRl.addView(getWebView("http://www.27270.com/zt/nvsheng/", 500, 500, 180, 420));
//            playSound(this,"http://120.24.234.123:8080/sysfile/upload/201711/e06f6f90-6379-422b-ae71-33297e4e0687.mp3",(float)0.1);
//            mRootRl.addView(getText("这是字幕这是字幕", 1400, 900));
//            Glide.with(this).load("http://pic35.nipic.com/20131115/6704106_153707247000_2.jpg").into(new ViewTarget<RelativeLayout, GlideDrawable>(mRootRl) {
//                                @Override
//                                public void onResourceReady(GlideDrawable resource, GlideAnimation anim) {
//                                    mRootRl.setBackground(resource);
//                                }
//                            });
            for (int i = 0; i < materialList.size(); i++) {
                //1.图片 2.音频 3.网络视频 4.本地视频 5.HTML 6.PPT 7.Word 8.Excel 9.PDF 10.背景
                MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean showsMaterialBean = materialList.get(i).getShowsMaterial();
                MenuBean.ListResultBean.ListshowBean.Material.MaterialBean materialBean = materialList.get(i).getMaterial();
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
                            playSound(this, url, (float) 0.2);
                            break;
                        case 3:
                        case 4:
                            mRootRl.addView(getVideoPlayer(url, wide, hige, x, y, (float) 0));
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
    protected void onDestroy() {
        super.onDestroy();
        stopPlaybackVideo();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        unregisterReceiver(timeReceiver);
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
    private VideoView getVideoPlayer(String url, int width, int height, int x, int y, final float volume) {
        final VideoView mVideoPlayer = new VideoView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        mVideoPlayer.setLayoutParams(layoutParams);
        mVideoPlayer.setKeepScreenOn(true);
        mVideoPlayer.setVideoURI(Uri.parse(url));
        mVideoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(volume, volume);
                mp.setLooping(true);
                mp.start();
            }
        });
        mVideoPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
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
    private void playSound(Context context, String path, final float volume) {
        mediaPlayer = new MediaPlayer();
        try {
            //播放之前要先把音频文件重置
            mediaPlayer.reset();
            //调用方法传进去要播放的音频路径
            mediaPlayer.setDataSource(context, Uri.parse(path));
            //异步准备音频资源
            mediaPlayer.prepareAsync();
            //调用mediaPlayer的监听方法，音频准备完毕会响应此方法
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setVolume(volume, volume);
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
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
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
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        imageView.setLayoutParams(layoutParams);
        Glide.with(this).load(url).error(R.mipmap.ic_launcher).into(imageView);
        return imageView;
    }

    public class TimeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long millis = System.currentTimeMillis();
            if (map.containsKey(millis)) {
                showsList.clear();
                showsList.add((MenuBean.ListResultBean.ListshowBean) map.get(millis));
                setData();
            }
        }
    }
}
