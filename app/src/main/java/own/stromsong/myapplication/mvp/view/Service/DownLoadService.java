package own.stromsong.myapplication.mvp.view.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;

import own.stromsong.myapplication.utils.HttpUtils;
import own.stromsong.myapplication.utils.StringUtil;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class DownLoadService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DownLoadService() {
        super("downLoadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String action = intent.getAction();
        if (action!=null&&action.equals("downLoadApk")){
            String apkUrl = intent.getStringExtra("apkUrl");
            String versionName = intent.getStringExtra("versionName");
            if (StringUtil.NoNullOrEmpty(apkUrl)){
                HttpUtils.downloadAPK(apkUrl,versionName);
            }
        }else {
            String url = intent.getStringExtra("url");
            String fileName = intent.getStringExtra("fileName");
            String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + fileName;
            Log.e("tag","downloadPath="+downloadPath);
            if (!new File(downloadPath).exists()) {
                Log.e("tag","step>>>>>>>>>>>>>>=!new File(downloadPath).exists()");
                if (StringUtil.NoNullOrEmpty(url)) {
                    HttpUtils.downloadPDF(url, fileName);
                }
            }
        }
    }
}
