package own.stromsong.myapplication.mvp.view.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import own.stromsong.myapplication.app.MyApplication;
import own.stromsong.myapplication.mvp.model.CompleteBean;
import own.stromsong.myapplication.utils.StringUtil;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class DownloadFileReceiver extends BroadcastReceiver {
    private long mTaskId;
    private DownloadManager downloadManager;
    private String fileName;
    private Bundle mBundle;
    private DownloadManager.Query query;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            checkDownloadStatus();
        }
    };
    public DownloadFileReceiver(long mskId, DownloadManager downloadManager, String fileName){
        this.mTaskId = mskId;
        this.downloadManager = downloadManager;
        this.fileName=fileName;
        query = new DownloadManager.Query();
    }
    public DownloadFileReceiver(long mskId, DownloadManager downloadManager, String fileName, Bundle bundle){
        this.mTaskId = mskId;
        this.downloadManager = downloadManager;
        this.fileName=fileName;
        mBundle = bundle;
        query = new DownloadManager.Query();
    }
    public DownloadFileReceiver(){

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("downLoadLog","action---->onReceive");
        checkDownloadStatus();
    }
    //检查下载状态
    private void checkDownloadStatus() {
        query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
        //创建下载任务
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            int bytes_downloaded = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int bytes_total = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            Log.e("downLoadLog","bytes_downloaded="+bytes_downloaded);
            Log.e("downLoadLog","bytes_total="+bytes_total);
            switch (status) {
                case DownloadManager.STATUS_RUNNING:
                    Log.e(getClass().getName(),">>>正在下载");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    if (fileName.contains(".pdf")){
                        CompleteBean bean = new CompleteBean();
                        bean.setEventStr("loadPDF");
                        bean.setPath(MyApplication.getInstance().getExternalCacheDir()+File.separator+"pdf"+File.separator+fileName);
                        bean.setBundle(mBundle);
                        EventBus.getDefault().post(bean);
                    }
                    Log.e("downLoadLog","文件"+fileName+"下载完成");
                    break;
                case DownloadManager.STATUS_FAILED:
                    Log.e(getClass().getName(),">>>下载失败");
                    break;
            }
            c.close();
        }
        mHandler.sendEmptyMessageDelayed(11,200);
    }
}
