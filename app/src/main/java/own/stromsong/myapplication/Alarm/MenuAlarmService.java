package own.stromsong.myapplication.Alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import own.stromsong.myapplication.Alarm.AlarmAlertBroadcastReceiver;
import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.other.Const;
import own.stromsong.myapplication.utils.DateFormatUtil;
import own.stromsong.myapplication.utils.SpUtils;

/**
 * Created by Administrator on 2018/5/25.
 */

public class MenuAlarmService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Map<String,MenuBean.ListResultBean> map = (Map<String, MenuBean.ListResultBean>) intent.getSerializableExtra("menuMap");
        Map<String, MenuBean.SubtitlesBean> zimuMap = (Map<String, MenuBean.SubtitlesBean>) intent.getSerializableExtra("zimuMap");
        if (map!=null&&map.size()>0){
            long millis = System.currentTimeMillis();
            for (Map.Entry<String, MenuBean.ListResultBean> mBean : map.entrySet()){
                MenuBean.ListResultBean value = mBean.getValue();
                long startTime = value.getStartTime();//节目单的开始时间
                if (startTime>millis){
                    schedule(startTime,value,map);
                    break;
                }
            }
        }
        if (zimuMap!=null&&zimuMap.size()>0){
            long millis = System.currentTimeMillis();
            for (Map.Entry<String, MenuBean.SubtitlesBean> mBean:zimuMap.entrySet()){
                MenuBean.SubtitlesBean mBeanValue = mBean.getValue();
                long startDate = mBeanValue.getStartDate();//字幕开始时间
                long endDate = mBeanValue.getEndDate();
                Log.e("time","millis="+millis);
                Log.e("time","startDate="+startDate);
                Log.e("time","endDate="+startDate);
                boolean isSet = false;
                if (startDate!=0&&startDate>millis){
                    schedule1(startDate,mBeanValue,zimuMap);
                    isSet = true;
                }
                if (endDate!=0&&endDate>=millis){
                    schedule1(endDate,mBeanValue,zimuMap);
                }
                if (isSet){
                    break;
                }
            }
        }

        return START_NOT_STICKY;
    }

    @SuppressLint("NewApi")
    public void schedule(long remindTime,MenuBean.ListResultBean value,Map<String,MenuBean.ListResultBean> map) {
        Intent myIntent = new Intent(getApplicationContext(), AlarmAlertBroadcastReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("menuMap", (Serializable) map);
        bundle.putSerializable("menuBean",value);
        myIntent.setAction("menuAction");
        myIntent.putExtras(bundle);
//        Intent intent = new Intent("rk.android.alarm.action");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        // TODO: 2017/12/9 0009
        //19以上手机版本不支持
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, remindTime,0,pendingIntent);
        Log.e("tag","??????????");
        alarmManager.setExact(AlarmManager.RTC,remindTime,pendingIntent);
        String time = DateFormatUtil.getTime(remindTime, "yyyy-MM-dd hh:mm:ss");
        Log.e("tag","定时播放节目单时间="+time);
    }
    @SuppressLint("NewApi")
    public void schedule1(long remindTime,MenuBean.SubtitlesBean value,Map<String, MenuBean.SubtitlesBean> map) {
        Intent myIntent = new Intent(getApplicationContext(), AlarmAlertBroadcastReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("zimuMap", (Serializable) map);
        bundle.putSerializable("zimuBean",value);
        myIntent.setAction("zimuAction");
        myIntent.putExtras(bundle);
//        Intent intent = new Intent("rk.android.alarm.action");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        // TODO: 2017/12/9 0009
        //19以上手机版本不支持
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, remindTime,0,pendingIntent);
        alarmManager.setExact(AlarmManager.RTC,remindTime,pendingIntent);
        String time = DateFormatUtil.getTime(remindTime, "yyyy-MM-dd hh:mm:ss");
        Log.e("tag","定时设置字幕时间="+time);
    }
}
