package own.stromsong.myapplication.app;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;

import com.awen.photo.FrescoImageLoader;
import com.blankj.utilcode.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.android.tpush.XGPushConfig;
import com.weavey.loading.lib.LoadingLayout;
import com.youth.xframe.XFrame;

import own.stromsong.myapplication.R;

/**
 * Created by Administrator on 2018/3/7 0007.
 */

public class MyApplication extends Application {
    private static MyApplication mApplication;
    public static MyApplication getInstance() {
        return mApplication;
    }

    public static final String AppPath = Environment.getExternalStorageDirectory().getPath();

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
//                return new BezierRadarHeader(context);
                return new ClassicsHeader(context);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        Utils.init(this);//Utils工具类https://www.jianshu.com/p/72494773aace?utm_campaign=hugo&utm_medium=reader_share&utm_content=note&utm_source=weixin-friends
        initStatusLayout();
        XFrame.init(this);
        XFrame.initXLog()//初始化XLog
                .setTag("sst")//设置全局tag
                .setShowThreadInfo(false)//是否开启线程信息显示，默认true
                .setDebug(true);//是否显示日志，默认true，发布时最好关闭
        XGPushConfig.enableDebug(this,true);

        FrescoImageLoader.init(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

    }


    /**
     * 初始化状态布局
     */
    private void initStatusLayout() {
        LoadingLayout.getConfig()
                .setErrorText("出错啦~请稍后重试！")
                .setEmptyText("抱歉，暂无数据")
                .setNoNetworkText("无网络连接，请检查您的网络···")
//                .setLoadingPageLayout(R.layout.define_loading_page)
//                .setErrorImage(R.mipmap.define_error)
//                .setEmptyImage(R.mipmap.define_empty)
//                .setNoNetworkImage(R.mipmap.define_nonetwork)
//                .setAllTipTextColor(R.color.gray)
                .setAllTipTextSize(14)
                .setReloadButtonText("点我重试哦")
                .setReloadButtonTextSize(14)
                .setReloadButtonTextColor(R.color.color_cccccc)
                .setReloadButtonWidthAndHeight(150, 40);
    }
}
