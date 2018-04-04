package own.stromsong.myapplication.mvp.view.receiver;

import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import own.stromsong.myapplication.mvp.model.RefreshActList;

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

        Log.e("aa","getTitle--->"+ title +"--getContent--->"+ content +"--getCustomContent--->"+ customContent);
        ToastUtils.showShort("getTitle--->"+ title +"--getContent--->"+ content +"--getCustomContent--->"+ customContent);

            try {
                JSONObject jsonObject = new JSONObject(customContent);
                int type = jsonObject.getInt("type");
                String method = jsonObject.getString("method");
                switch (type) {
                    case 1://播放内容
                        EventBus.getDefault().post(new RefreshActList());
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
}
