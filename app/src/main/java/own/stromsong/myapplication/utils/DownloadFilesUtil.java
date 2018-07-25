package own.stromsong.myapplication.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.webkit.MimeTypeMap;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import own.stromsong.myapplication.app.MyApplication;

/**
 * Created by Administrator on 2018/7/11.
 */

public abstract class DownloadFilesUtil {

    private DownloadManager mDownloadManager;
    private Handler mHandler = new Handler();
    private int mPercent;

    public DownloadFilesUtil() {
        mDownloadManager = (DownloadManager) MyApplication.getInstance().getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public void downloadFiles(List<String> urls, List<String> paths) {
        List<Long> taskIds = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urls.get(i)));
            request.setAllowedOverRoaming(true);//漫游网络是否可以下载

            //设置文件类型，可以在下载结束后自动打开该文件
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(urls.get(i)));
            request.setMimeType(mimeString);


            //在通知栏中显示，默认就是显示的
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setVisibleInDownloadsUi(false);
//        request.setTitle("正在下载");

            //自定义路径
            request.setDestinationUri(Uri.fromFile(new File(paths.get(i))));
            //将下载请求加入下载队列
            //加入下载队列后会给该任务返回一个long型的id，
            //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
            long mskId = mDownloadManager.enqueue(request);
            taskIds.add(mskId);
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                checkDownloadStatus(taskIds);
                if (mPercent < 100) {
                    mHandler.postDelayed(this, 200);
                } else {
                    mHandler = null;
                }
            }
        });
    }

    //检查下载状态
    private void checkDownloadStatus(List<Long> ids) {
        DownloadManager.Query query = new DownloadManager.Query();
        float currentPercent = 0;
        for (int i = 0; i < ids.size(); i++) {
            query.setFilterById(ids.get(i));//筛选下载任务，传入任务ID，可变参数
            //创建下载任务
            Cursor c = mDownloadManager.query(query);
            if (c.moveToFirst()) {
                int bytes_downloaded = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int bytes_total = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                Log.e("downLoadLog", "bytes_downloaded=" + bytes_downloaded);
                Log.e("downLoadLog", "bytes_total=" + bytes_total);
                if (bytes_downloaded <= bytes_total) {
                    float percent = (float) bytes_downloaded / (float) bytes_total;
                    currentPercent += percent;
                }
            }
        }

        float realPercent = (currentPercent / ids.size()) * 100;
        Log.e("tag", "sumPercent=" + realPercent);
        mPercent = Math.round(realPercent);
        if (mPercent <= 100) {
            onProgress(mPercent);
        }
    }

    public abstract void onProgress(int progress);
}
