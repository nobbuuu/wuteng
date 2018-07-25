package own.stromsong.myapplication.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import own.stromsong.myapplication.R;
import own.stromsong.myapplication.listener.NoDoubleClickListener;
import own.stromsong.myapplication.mvp.model.DownloadBean;


/**
 * Created by Administrator on 2017/8/7 0007.
 */
public class DialogUtils {



    public static void setBespreadWindow(Dialog mDialog, Activity context) {
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        mDialog.getWindow().setAttributes(lp);
    }

    public static void show(Activity activity, final Dialog dialog) {

        if ("main".equals(Thread.currentThread().getName())) {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                }
            });
        }
    }


    public static void dismiss(Activity activity, final Dialog dialog) {
        if ("main".equals(Thread.currentThread().getName())) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        }
    }


    public static void getVersionDialog(String webVersionName, final String apkUrl, final Context activity) {
        final Dialog dialog_tip = new Dialog(activity);
        View login = LayoutInflater.from(activity).inflate(R.layout.dialog_versionupdate, null);
        TextView tip_title = (TextView) login.findViewById(R.id.tip_title);
        TextView version_tv = (TextView) login.findViewById(R.id.version_tv);
        TextPaint tp = tip_title.getPaint();
        tp.setFakeBoldText(true);//加粗字体
        tip_title.setText("发现新版本");
        version_tv.setText(webVersionName);
        TextView cancle = (TextView) login.findViewById(R.id.tip_nav);
        TextView sure = (TextView) login.findViewById(R.id.tip_positive);
        dialog_tip.requestWindowFeature(Window.FEATURE_NO_TITLE);//（这句设置没有title）
        dialog_tip.setContentView(login);
        dialog_tip.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_tip.setCancelable(true);
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        WindowManager.LayoutParams p = dialog_tip.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (displayWidth * 0.75); //宽度设置为屏幕的0.75
//        p.height = (int) (displayHeight * 0.45); //高度设置为屏幕的0.45
        dialog_tip.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog_tip.getWindow().setAttributes(p);  //设置生效

      /*  // TODO: 2016/12/23  //去掉蓝线
        int dividerID=activity.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider=dialog_tip.findViewById(dividerID);
        divider.setBackgroundColor(Color.TRANSPARENT);(这句在5.1上会报空指针)*/

        dialog_tip.show();
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog_tip.isShowing()) {
                    dialog_tip.dismiss();
                }
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog_tip.isShowing()) {
                    dialog_tip.dismiss();
                }
                String netTypeName = NetUtil.getNetTypeName(activity);
                Log.e("tag", "netTypeName=" + netTypeName);

                if (netTypeName.equals("无网络")) {
                    ToastUtils.showLong("网络异常");
                    return;
                }
                if (!netTypeName.equals("WIFI")) {
                    downloadTip(activity, apkUrl,webVersionName);
                } else {
                    //下载安装包、
                    EventBus.getDefault().post("downloadApk");
                }
            }
        });
    }

    public static void downloadTip(final Context activity, final String apkUrl,String version) {
        final Dialog dialog_tip = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View login = LayoutInflater.from(activity).inflate(R.layout.dialog_networktip, null);
        TextView tip_title = (TextView) login.findViewById(R.id.tip_title);
        TextView version_tv = (TextView) login.findViewById(R.id.version_tv);
        TextPaint tp = tip_title.getPaint();
        tp.setFakeBoldText(true);//加粗字体
        TextView cancle = (TextView) login.findViewById(R.id.tip_nav);
        TextView sure = (TextView) login.findViewById(R.id.tip_positive);
        dialog_tip.requestWindowFeature(Window.FEATURE_NO_TITLE);//（这句设置没有title）
        dialog_tip.setContentView(login);
        //获取当前Activity所在的窗体
        Window dialogWindow1 = dialog_tip.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow1.setGravity(Gravity.BOTTOM);
        dialog_tip.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_tip.setCancelable(true);
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        WindowManager.LayoutParams p = dialog_tip.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (displayWidth * 0.75); //宽度设置为屏幕的0.75
//        p.height = (int) (displayHeight * 0.45); //高度设置为屏幕的0.45
        dialog_tip.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog_tip.getWindow().setAttributes(p);  //设置生效

      /*  // TODO: 2016/12/23  //去掉蓝线
        int dividerID=activity.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider=dialog_tip.findViewById(dividerID);
        divider.setBackgroundColor(Color.TRANSPARENT);(这句在5.1上会报空指针)*/

        dialog_tip.show();
        //使用流量下载
        cancle.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (dialog_tip.isShowing()) {
                    dialog_tip.dismiss();
                }
                //下载安装包、
                DownloadBean downloadBean = new DownloadBean();
                downloadBean.setApkUrl(apkUrl);
                downloadBean.setVersion(version);
                downloadBean.setEventStr("downloadApk");
                EventBus.getDefault().post(downloadBean);
            }
        });

        sure.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (dialog_tip.isShowing()) {
                    dialog_tip.dismiss();
                }
            }
        });
    }
}
