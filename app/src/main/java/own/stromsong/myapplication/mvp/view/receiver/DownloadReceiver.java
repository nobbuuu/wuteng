package own.stromsong.myapplication.mvp.view.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import own.stromsong.myapplication.app.MyApplication;
import own.stromsong.myapplication.mvp.model.CompleteBean;
import own.stromsong.myapplication.utils.FileUtil;
import own.stromsong.myapplication.utils.StringUtil;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class DownloadReceiver extends BroadcastReceiver {
    private long mTaskId;
    private DownloadManager downloadManager;
    private String versionName;

    public DownloadReceiver(long mskId, DownloadManager downloadManager, String versionName) {
        this.mTaskId = mskId;
        this.downloadManager = downloadManager;
        this.versionName = versionName;
    }

    public DownloadReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        checkDownloadStatus(intent);
    }

    //检查下载状态
    private void checkDownloadStatus(Intent intent) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
        //创建下载任务
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    Log.e(getClass().getName(), ">>>下载暂停");
                case DownloadManager.STATUS_PENDING:
                    Log.e(getClass().getName(), ">>>下载延迟");

                case DownloadManager.STATUS_RUNNING:
                    Log.e(getClass().getName(), ">>>正在下载");

                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.e(getClass().getName(), ">>>下载完成");
                    String action = intent.getAction();
                    String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + versionName;
                    Log.e(getClass().getName(), "downloadPath=" + downloadPath);
                    if (versionName.contains(".apk")){
                        //下载完成安装APK
                        FileUtil.installApk(MyApplication.getInstance(), new File(downloadPath));
                    }else {
                        if (StringUtil.NoNullOrEmpty(downloadPath)) {
                            CompleteBean bean = new CompleteBean();
                            bean.setEventStr("loadPDF");
                            bean.setPath(downloadPath);
                            EventBus.getDefault().post(bean);
                        }
                    }
                    break;
                case DownloadManager.STATUS_FAILED:
                    Log.e(getClass().getName(), ">>>下载失败");
                    break;
            }
        }
    }
}
