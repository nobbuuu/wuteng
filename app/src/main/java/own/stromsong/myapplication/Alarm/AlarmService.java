package own.stromsong.myapplication.Alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

import own.stromsong.myapplication.utils.DateFormatUtil;
import own.stromsong.myapplication.utils.StringUtil;

/**
 * Created by acer-pc on 2016/4/21.
 */
public class AlarmService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String offTime = intent.getStringExtra("offTime");
        Log.e("tag","offTime="+offTime);
        if (offTime.contains(":")){
            String[] split = offTime.split(":");
            if (split.length>2){
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,Integer.valueOf(split[0]));
                calendar.set(Calendar.MINUTE,Integer.valueOf(split[1]));
                calendar.set(Calendar.SECOND,Integer.valueOf(split[2]));
                if (StringUtil.NoNullOrEmpty(offTime)){
                    schedule(calendar.getTimeInMillis());
                    calendar.setTimeInMillis(SystemClock.currentThreadTimeMillis());
                }
            }
        }
        return START_NOT_STICKY;
    }

    @SuppressLint("NewApi")
    public void schedule(long remindTime) {
        Intent myIntent = new Intent(getApplicationContext(), AlarmAlertBroadcastReceiver.class);
        myIntent.setAction("rk.android.alarm.action");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        // TODO: 2017/12/9 0009
        //19以上手机版本不支持
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, remindTime,0,pendingIntent);
        alarmManager.setExact(AlarmManager.RTC,remindTime,pendingIntent);
        String time = DateFormatUtil.getTime(remindTime, "yyyy-MM-dd hh:mm:ss");
        Log.e("tag","定时关机时间="+time);

    }

}
