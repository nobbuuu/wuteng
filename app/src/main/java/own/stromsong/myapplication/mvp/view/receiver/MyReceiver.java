package own.stromsong.myapplication.mvp.view.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import own.stromsong.myapplication.Alarm.AlarmService;
import own.stromsong.myapplication.mvp.model.RefreshActList;
import own.stromsong.myapplication.mvp.model.ScreenBitmap;
import own.stromsong.myapplication.mvp.model.VoiceControl;
import own.stromsong.myapplication.mvp.view.activity.MainActivity;
import own.stromsong.myapplication.utils.HttpUtils;
import own.stromsong.myapplication.utils.StringUtil;
import own.stromsong.myapplication.utils.VersionUtils;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesHelper;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesTag;

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

        Log.e("aa", "getTitle--->" + title + "--getContent--->" + content + "--getCustomContent--->" + customContent);

        try {
            JSONObject jsonObject = new JSONObject(customContent);
            int type = jsonObject.getInt("type");
//            String method = jsonObject.getString("method");
            switch (type) {
                case 0:
                case 1://播放内容
                    EventBus.getDefault().post(new RefreshActList());
                    break;
                case 2://截屏
                    EventBus.getDefault().post(new ScreenBitmap());
                    break;
                case 3://重启
                    Intent intent = new Intent("ads.android.setreboot.action");
                    context.sendBroadcast(intent);
                    break;
                case 4://音量控制
                    String volume = jsonObject.getString("volume");
                    EventBus.getDefault().post(new VoiceControl(volume));
                    break;
                case 5:
                    EventBus.getDefault().post("zimuRefresh");
                    break;
                case 6://定时开机
                    String date = jsonObject.getString("date");
                    if (StringUtil.NoNullOrEmpty(date)) {
                        if (date.contains(":")) {
                            String[] split = date.split(":");
                            if (split.length >= 2) {
                                String poweron_hour = split[0];
                                String poweron_min = split[1];
                                Intent i = new Intent("rk.android.turnontime.action");
                                i.putExtra("turnonhour", poweron_hour);
                                i.putExtra("turnonmin", poweron_min);
                                context.sendBroadcast(i);
                                Log.e("tag", "定时开机");
                            }
                        }
                    }

                    break;
                case 7://定时关机
                    String offTime = jsonObject.getString("date");
                    if (StringUtil.NoNullOrEmpty(offTime)) {
                        Intent offIntent = new Intent(context, AlarmService.class);
                        offIntent.putExtra("offTime", offTime);
                        context.startService(offIntent);
                    }
                    break;
                case 10:
                    String showMenuId = jsonObject.getString("showMenuId");
                    if (StringUtil.NoNullOrEmpty(showMenuId)) {
                        RefreshActList refreshActList = new RefreshActList();
                        refreshActList.setEventStr("refreshSingleMenu");
                        refreshActList.setMenuId(showMenuId);
                        EventBus.getDefault().post(refreshActList);
                    }
                    break;
                case -1://重新登录
                    SharedPreferencesHelper.getInstance(context).putBooleanValue(SharedPreferencesTag.LOGIN_BOOLEAN, false);
                    ActivityUtils.finishOtherActivities(MainActivity.class);
                    ActivityUtils.startActivity(MainActivity.class);
                    break;
                case 100:
                    String path = jsonObject.getString("path");
                    String vison = jsonObject.getString("vison");
                    Log.e("tag", "path=" + path);
                    Log.e("tag", "vison=" + vison);
                    if (StringUtil.NoNullOrEmpty(path) && StringUtil.NoNullOrEmpty(vison)) {
                        if (VersionUtils.isNewVersion(vison)) {
                            HttpUtils.downloadAPK(path, vison);
                        }
                    }
                    break;
            }
        } catch (JSONException e) {
            Log.e("aa", "推送JSON解析异常");
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
