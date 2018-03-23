package own.stromsong.myapplication.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.mvp.view.activity.Video2Activity;

public class MyReceiver extends XGPushBaseReceiver {

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {

    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        String content = xgPushTextMessage.getContent();
        String customContent = xgPushTextMessage.getCustomContent();
        String title = xgPushTextMessage.getTitle();

        ToastUtils.showShort("getTitle--->"+ title +"--getContent--->"+ content +"--getCustomContent--->"+ customContent);

            try {
                JSONObject jsonObject = new JSONObject(customContent);
                int type = jsonObject.getInt("type");
                String result = jsonObject.getString("result");
                switch (type) {
                    case -1://关机
                        break;
                    case 1://播放内容
                        MenuBean menuBean = new Gson().fromJson(result, MenuBean.class);
                        Video2Activity.startVideo2Activity(context,menuBean.getListResult());
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

    }

    /**
     * 关机  行不通
     * @param context
     */
    private void shutDown(Context context){
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
