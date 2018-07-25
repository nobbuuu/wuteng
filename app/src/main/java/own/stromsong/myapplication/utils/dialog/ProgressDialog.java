package own.stromsong.myapplication.utils.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import own.stromsong.myapplication.R;
import own.stromsong.myapplication.other.Const;
import own.stromsong.myapplication.utils.SpUtils;

/**
 * Created by Administrator on 2018/7/6.
 */

public abstract class ProgressDialog extends RelativeLayout {

    private ProgressBar mDownloadbar;
    private TextView mPercentTv;
    private Dialog mDialog;
    public ProgressDialog(Context context) {
        super(context);
        initView(context);
    }

    public ProgressDialog(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView(Context context){
        View inflate = inflate(context,R.layout.dialog_download_view,this);
        mDownloadbar = inflate.findViewById(R.id.downloadbar);
        mPercentTv = inflate.findViewById(R.id.percent_tv);
        mDialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        mDialog.setContentView(inflate);

    }

    public void uploadDialog(int percent){
        mDownloadbar.setProgress(percent);
        mPercentTv.setText(percent+"%");
        if (!mDialog.isShowing()){
            mDialog.show();
        }
        if (percent==100){
            onDownover();
            mDialog.dismiss();
        }
    }

    public abstract void onDownover();

}
