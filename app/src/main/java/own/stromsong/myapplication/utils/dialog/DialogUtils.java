package own.stromsong.myapplication.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import own.stromsong.myapplication.R;
import own.stromsong.myapplication.listener.NoDoubleClickListener;

/**
 * Created by Administrator on 2018/6/22.
 */

public class DialogUtils {
    public static Dialog showDeleteDialog(final Context activity, final NoDoubleClickListener mSureClicklistener) {
        final Dialog dialog_tip = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View login = LayoutInflater.from(activity).inflate(R.layout.dialog_permission, null);
        TextView tip_title = (TextView) login.findViewById(R.id.tip_title);
        TextView version_tv = (TextView) login.findViewById(R.id.version_tv);
        version_tv.setText("网络无连接，建议去链接网络");
        TextPaint tp = tip_title.getPaint();
        tp.setFakeBoldText(true);//加粗字体
        TextView cancle = (TextView) login.findViewById(R.id.tip_nav);
        cancle.setText("取消");
        TextView sure = (TextView) login.findViewById(R.id.tip_positive);
        sure.setText("确定");
        dialog_tip.requestWindowFeature(Window.FEATURE_NO_TITLE);//（这句设置没有title）
        dialog_tip.setContentView(login);
      /*  //获取当前Activity所在的窗体
        Window dialogWindow1 = dialog_tip.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow1.setGravity(Gravity.BOTTOM);*/
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
        //取消
        cancle.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (dialog_tip.isShowing()) {
                    dialog_tip.dismiss();
                }
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSureClicklistener.onNoDoubleClick(view);
                dialog_tip.dismiss();
            }
        });
        return dialog_tip;
    }

    public static Dialog getProgressDialog(final Context activity) {
        final Dialog dialog_tip = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View login = LayoutInflater.from(activity).inflate(R.layout.dialog_download_view, null);
        ProgressBar downloadbar = login.findViewById(R.id.downloadbar);
        TextView percent_tv = login.findViewById(R.id.percent_tv);
        dialog_tip.requestWindowFeature(Window.FEATURE_NO_TITLE);//（这句设置没有title）
        dialog_tip.setContentView(login);
      /*  //获取当前Activity所在的窗体
        Window dialogWindow1 = dialog_tip.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow1.setGravity(Gravity.BOTTOM);*/
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
        return dialog_tip;
    }


}
