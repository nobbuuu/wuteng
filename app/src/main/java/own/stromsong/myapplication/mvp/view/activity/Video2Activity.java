package own.stromsong.myapplication.mvp.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import butterknife.BindView;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.mvp.base.BaseSupportActivity;
import own.stromsong.myapplication.mvp.model.MenuBean;

public class Video2Activity extends BaseSupportActivity {

    //    String url = "http://183.60.197.29/15/t/j/i/s/tjissokyfzdxotupwpwxjjoufsbshi/he.yinyuetai.com/09040162194A2B01EE029439394C3FBB.mp4?sc\\u003d0d6ab169ff59aea7\\u0026br\\u003d3173\\u0026vid\\u003d2780294\\u0026aid\\u003d6762\\u0026area\\u003dML\\u0026vst\\u003d0";
    String url = "http://120.24.234.123:8080/sysfile/upload/201711/64aa5b11-60df-4cae-8989-6ec194214eff.mp4";
    @BindView(R.id.root_rl)
    RelativeLayout mRootRl;

    private List<MenuBean.ListResultBean.ListshowBean.Material> list;
    private IntentFilter intentFilter;
    private MediaPlayer mediaPlayer;

    private TimeChangeReceiver timeChangeReceiver;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video2;
    }

    public static void startVideo2Activity(Context context, List<MenuBean.ListResultBean.ListshowBean.Material> list) {
        Intent intent = new Intent(context, Video2Activity.class);
        intent.putExtra("list", (Serializable) list);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        mRootRl.removeAllViews();
//        mRootRl.setBackground(null);
//        mRootRl.addView(getVideoPlayer(mStringList, 800, 600, 100, 50, 0));
//        mRootRl.addView(getVideoPlayer(mStringList, 800, 600, 1000, 50, 1));
//        playSound("");
//        mRootRl.postInvalidate();
    }

    @Override
    protected void initLocalData() {
        super.initLocalData();
        setBarVisible(false);
        list = (List<MenuBean.ListResultBean.ListshowBean.Material>) getIntent().getSerializableExtra("list");
        if (list != null && list.size() != 0) {
            mRootRl.addView(getImage("http://img05.tooopen.com/images/20150820/tooopen_sy_139205349641.jpg", 350, 200, 200, 150));
            mRootRl.addView(getVideoPlayer(url, 400, 350, 1150, 250, (float) 0));
            mRootRl.addView(getWebView("http://www.27270.com/zt/nvsheng/", 500, 500, 180, 420));
            playSound(this,"http://120.24.234.123:8080/sysfile/upload/201711/e06f6f90-6379-422b-ae71-33297e4e0687.mp3",(float)0.5);
            mRootRl.addView(getText("这是字幕这是字幕", 1400, 900));
            for (int i = 0; i < list.size(); i++) {
                //1.图片 2.音频 3.网络视频 4.本地视频 5.HTML 6.PPT 7.Word 8.Excel 9.PDF 10.背景
                MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean showsMaterialBean = list.get(i).getShowsMaterial();
                MenuBean.ListResultBean.ListshowBean.Material.MaterialBean materialBean = list.get(i).getMaterial();
                if (materialBean != null || showsMaterialBean != null) {
                    int wide = showsMaterialBean.getWide();
                    int hige = showsMaterialBean.getHige();
                    int x = Integer.valueOf(showsMaterialBean.getX());
                    int y = Integer.valueOf(showsMaterialBean.getY());
                    int type = 10;
                    switch (type) {
                        case 1:
                            mRootRl.addView(getImage(url, wide, hige, x, y));
                            break;
                        case 2:
                        case 3:
                        case 4:
                            mRootRl.addView(getVideoPlayer(url, wide, hige, x, y, (float) 0.5));
                            break;
                        case 5:
                            mRootRl.addView(getWebView("http://www.baidu.com", wide, hige, x, y));
                            break;
                        case 10:
                            Glide.with(this).load("http://pic35.nipic.com/20131115/6704106_153707247000_2.jpg").into(new ViewTarget<RelativeLayout, GlideDrawable>(mRootRl) {
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

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);//每分钟变化

        timeChangeReceiver = new TimeChangeReceiver();
        registerReceiver(timeChangeReceiver, intentFilter);

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
        unregisterReceiver(timeChangeReceiver);
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
        return mVideoPlayer;
    }


    /**
     * 播放音频的方法
     */
    private void playSound(Context context,String path,final float volume) {
        mediaPlayer = new MediaPlayer();
        try {
            //播放之前要先把音频文件重置
            mediaPlayer.reset();
            //调用方法传进去要播放的音频路径
            mediaPlayer.setDataSource(context,Uri.parse(path));
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

    /**
     * 字幕
     *
     * @param text
     * @param x
     * @param y
     * @return
     */
    private TextView getText(String text, int x, int y) {
        TextView textView = new TextView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setTextColor(Color.RED);
        textView.setTextSize(40);
        return textView;
    }

    class TimeChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_TIME_TICK:
                    //每过一分钟 触发
                    Toast.makeText(context, "1 min passed", Toast.LENGTH_SHORT).show();
//                    Video2Activity.startVideo2Activity(context);
                    break;
            }
        }
    }
}
