package own.stromsong.myapplication.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import own.stromsong.myapplication.app.MyApplication;
import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.utils.FileUtil;
import own.stromsong.myapplication.utils.FileUtils;


/**
 * Created by acer-pc on 2016/4/21.
 */
public class AlarmAlertBroadcastReceiver extends BroadcastReceiver {

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        Log.e("remind", "AlarmAlertBroadcastReceiver——onReceive");
        String action = intent.getAction();
        Log.e("tag", "action=" + action);
        switch (action) {
            case "menuAction"://节目单定时播放
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    MenuBean.ListResultBean menuBean = (MenuBean.ListResultBean) extras.getSerializable("menuBean");
                    if (menuBean != null) {
                        new DeleteTask(menuBean, extras).execute();
                    }
                }
                break;
            case "zimuAction"://字幕定时播放
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    MenuBean.SubtitlesBean zimuBean = (MenuBean.SubtitlesBean) bundle.getSerializable("zimuBean");
                    if (zimuBean != null) {
                        EventBus.getDefault().post(zimuBean);
                    }
                    Intent menuIntent = new Intent(context, MenuAlarmService.class);
                    menuIntent.putExtras(bundle);
                    context.startService(menuIntent);
                }
                break;
            case "rk.android.alarm.action"://定时关机
                EventBus.getDefault().post("turnOff");
                break;
        }
    }

    private class DeleteTask extends AsyncTask<String, String, String> {
        private MenuBean.ListResultBean mBean;
        private Bundle mBundle;

        public DeleteTask(MenuBean.ListResultBean mm, Bundle bundle) {
            mBean = mm;
            mBundle = bundle;
        }

        @Override
        protected String doInBackground(String... strings) {
            String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "wuteng";
            String proImgPath = MyApplication.getInstance().getExternalCacheDir() + "/images";
            String proHtmlPath = MyApplication.getInstance().getExternalCacheDir() + "/html";
            try {
                FileUtils.deleteListFile(new File(downloadPath));
                FileUtils.deleteListFile(new File(proImgPath));
                FileUtils.deleteListFile(new File(proHtmlPath));
            } catch (Exception e) {
                Log.e("tag", "exception=" + e.toString());
            }
            return "deleteComplete";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            EventBus.getDefault().post(mBean);
            Intent menuIntent = new Intent(mContext, MenuAlarmService.class);
            menuIntent.putExtras(mBundle);
            mContext.startService(menuIntent);
        }
    }

}
