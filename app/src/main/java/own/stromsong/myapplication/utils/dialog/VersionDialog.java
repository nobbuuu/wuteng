package own.stromsong.myapplication.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import own.stromsong.myapplication.R;
import own.stromsong.myapplication.listener.NoDoubleClickListener;
import own.stromsong.myapplication.utils.FileUtils;
import own.stromsong.myapplication.utils.NetUtil;

/**
 * Created by Administrator on 2018/3/1 0001.
 */

public class VersionDialog {
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
                    ToastUtils.showShort("您的网络异常");
                    return;
                }
                if (!netTypeName.equals("WIFI")) {
                    downloadTip(activity, apkUrl);
                } else {
                    //下载安装包、
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                            //考虑网络断开连接，实现断点下载
                            //注册广播，获取连接状态；
//                        IntentFilter filter = new IntentFilter();
//                        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//                        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//                        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//                        networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
//                        registerReceiver(networkConnectChangedReceiver, filter);
//                        SharePreferenceUtils.setParam(MainActivity.this,Const.apkUrl_fileName,apkUrl);
                            FileUtils.downloadFile(apkUrl, FileUtils.DIR_APK, "mocar.apk");
                        }
                    };
                    new Thread(runnable).start();
                }
            }
        });
    }

    public static void downloadTip(final Context activity, final String apkUrl) {
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
        dialogWindow1.setGravity(Gravity.NO_GRAVITY);
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
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        //考虑网络断开连接，实现断点下载
                        //注册广播，获取连接状态；
//                        IntentFilter filter = new IntentFilter();
//                        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//                        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//                        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//                        networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
//                        registerReceiver(networkConnectChangedReceiver, filter);
//                        SharePreferenceUtils.setParam(MainActivity.this,Const.apkUrl_fileName,apkUrl);
                        FileUtils.downloadFile(apkUrl, FileUtils.DIR_APK, "mocar.apk");
                    }
                };
                new Thread(runnable).start();
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
