package own.stromsong.myapplication.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import own.stromsong.myapplication.app.MyApplication;

/**
 * Created by Administrator on 2018/3/1 0001.
 */

public class FileUtils {
    public static final File DIR_IMAGE = getDir("AppSearch");
    public static final File DIR_CACHE = getDir("cache");
    public static final File DIR_APK = getDir("apk");
    public static final File DIR_MP3 = getDir("mp3");
    public static final File DIR_VIDOE = getDir("video");

    public static File downloadFile(String httpurl, File dir, String rename) {
        File target = new File(dir, rename);
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(httpurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
//            conn.setReadTimeout(5000);
//            conn.setConnectTimeout(5000);
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                long contentLength = conn.getContentLength();
                Log.e("tag", "target.length=" + target.length());
                Log.e("tag", "contentLength=" + contentLength);
                byte[] buff = new byte[1024 * 1024 * 100];
                inputStream = conn.getInputStream();
                fos = new FileOutputStream(target);
                int read = -1;
                long download = 0;
                NotificationManager manager = NotifyUtil.getNotificationManager(MyApplication.getInstance());
                while ((read = inputStream.read(buff)) != -1) {
                    fos.write(buff, 0, read);
                    fos.flush();
                    download += read;
                    //计算下载百分比
                    final int progress = (int) (100 * download / contentLength);
                    Log.e("tag", "progress=" + progress);
                    Log.e("tag", "download=" + download);
                       /* runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });*/

                    NotifyUtil.sendAppVersionNotify(MyApplication.getInstance(), manager, progress);
                }
                Log.e("tag", "文件下载成功！");
                installApk(MyApplication.getInstance(), target);
                return target;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                    // fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e("TAG", "文件下载失败！");
//        DownloadBean bean = new DownloadBean();
//        bean.setEventStr("fail");
//        EventBus.getDefault().post(bean);
        return null;

    }

    public static void installApk(Context context, File apk) {
        Uri uri = Uri.fromFile(apk);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /*
    * SD卡根目录
    * */
    public static File getSDcardDir() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File storageDictory = Environment.getExternalStorageDirectory();
            return storageDictory;
        }
        throw new RuntimeException("没有找到内存卡！");
    }

    public static File getAppDir() {
        File dir = new File(getSDcardDir(), "Mocar");//项目的根目录
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }

    /*
    * 获取应用目录下面的指定目录
    * */
    public static File getDir(String dir) {
        File file = new File(getAppDir(), dir);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;

    }
}
