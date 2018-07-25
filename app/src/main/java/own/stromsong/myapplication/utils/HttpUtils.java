package own.stromsong.myapplication.utils;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import own.stromsong.myapplication.app.MyApplication;
import own.stromsong.myapplication.mvp.model.CompleteBean;
import own.stromsong.myapplication.mvp.model.DownloadBean;
import own.stromsong.myapplication.mvp.view.receiver.DownloadFileReceiver;
import own.stromsong.myapplication.mvp.view.receiver.DownloadReceiver;
import own.stromsong.myapplication.other.Const;
import own.stromsong.myapplication.weight.DocViewUtils;

/**
 * Created by Administrator on 2017/6/15 0015.
 */
public class HttpUtils {

    public static MultipartBody.Part getRequestBodyPart(String element, File mFile) {
        if (mFile != null) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//ParamKey.TOKEN 自定义参数key常量类，即参数名
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
            builder.addFormDataPart(element, mFile.getName(), imageBody);//imgfile 后台接收图片流的参数名
            MultipartBody.Part part = builder.build().part(0);
            return part;
        } else {
            return null;
        }
    }

    public static RequestBody getBody(String str) {
        return RequestBody.create(null, str);
    }

    /**
     * @param path 图片路径
     * @return
     * @将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @author QQ986945193
     * @Date 2015-01-26
     */
    public static String imageToBase64(String path) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {

            InputStream in = new FileInputStream(path);

            data = new byte[in.available()];

            in.read(data);

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        String encodedString = Base64.encodeToString(data, Base64.DEFAULT);
        return encodedString;

    }

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
           /* int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
            }*/
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
            FileUtil.installApk(MyApplication.getInstance(), target);
            return target;
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
        DownloadBean bean = new DownloadBean();
        bean.setEventStr("fail");
        EventBus.getDefault().post(bean);
        return null;

    }

    //使用系统下载器下载
    public static void downloadAPK(String versionUrl, String versionName) {
        if (!versionUrl.contains(".apk")) {
            return;
        }
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionUrl));
        request.setAllowedOverRoaming(false);//漫游网络是否可以下载

        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(versionUrl));
        request.setMimeType(mimeString);


        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);
        request.setTitle("正在下载");

        //sdcard的目录下的download文件夹，必须设置
        request.setDestinationInExternalPublicDir("/download/", versionName + ".apk");
        //request.setDestinationInExternalFilesDir(),也可以自己制定下载路径

        //将下载请求加入下载队列
        DownloadManager downloadManager = (DownloadManager) MyApplication.getInstance().getSystemService(Context.DOWNLOAD_SERVICE);
        //加入下载队列后会给该任务返回一个long型的id，
        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        long mskId = downloadManager.enqueue(request);
        //注册广播接收者，监听下载状态
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        MyApplication.getInstance().registerReceiver(new DownloadReceiver(mskId, downloadManager, versionName + ".apk"), intentFilter);

    }

    /*
    * 下载到应用公有路径
    * */
    //使用系统下载器下载
    public static void downloadFile(String url, String filePath, String fileName) {
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedOverRoaming(false);//漫游网络是否可以下载

        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);


        //在通知栏中显示，默认就是显示的
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(false);
//        request.setTitle("正在下载");

        //sdcard的目录下的download文件夹，必须设置
        request.setDestinationInExternalPublicDir("/download/", filePath);//公共的文件夹
//        request.setDestinationInExternalFilesDir(MyApplication.getInstance(), Environment.DIRECTORY_DOWNLOADS,filePath);//应用专用的文件夹
//        request.setDestinationInExternalFilesDir("");//也可以自己制定下载路径

        //将下载请求加入下载队列
        DownloadManager downloadManager = (DownloadManager) MyApplication.getInstance().getSystemService(Context.DOWNLOAD_SERVICE);
        //加入下载队列后会给该任务返回一个long型的id，
        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        long mskId = downloadManager.enqueue(request);
        //注册广播接收者，监听下载状态
        MyApplication.getInstance().registerReceiver(new DownloadFileReceiver(mskId, downloadManager, fileName), new IntentFilter(String.valueOf(mskId)));
       /* mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                checkDownloadStatus(downloadManager, mskId, fileName, new DownloadManager.Query());
            }
        };
        mHandler.sendEmptyMessage(11);*/
    }

    private static Handler mHandler;

    //检查下载状态
    private static void checkDownloadStatus(DownloadManager downloadManager, long mTaskId, String fileName, DownloadManager.Query query) {
        query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
        //创建下载任务
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            int bytes_downloaded = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int bytes_total = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            Log.e("downLoadLog", "bytes_downloaded=" + bytes_downloaded);
            Log.e("downLoadLog", "bytes_total=" + bytes_total);
            switch (status) {
                case DownloadManager.STATUS_RUNNING:
                    Log.e("downLoadLog", ">>>正在下载");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.e("downLoadLog", "文件" + fileName + "下载完成");
                    c.close();
                    break;
                case DownloadManager.STATUS_FAILED:
                    Log.e("downLoadLog", ">>>下载失败");
                    break;
            }
        }
        mHandler.sendEmptyMessageDelayed(11,200);
    }

    //下载到应用专用目录下
    public static void downloadFile_programPath(String url, String filePath, String fileName) {
        //创建下载任务
        if (new File(filePath).exists()) {
            Log.e("tag", "文件已存在");
            return;
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedOverRoaming(true);//漫游网络是否可以下载

        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);


        //在通知栏中显示，默认就是显示的
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(false);
//        request.setTitle("正在下载");

        //自定义路径
        request.setDestinationUri(Uri.fromFile(new File(filePath)));
        //将下载请求加入下载队列
        DownloadManager downloadManager = (DownloadManager) MyApplication.getInstance().getSystemService(Context.DOWNLOAD_SERVICE);
        //加入下载队列后会给该任务返回一个long型的id，
        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        long mskId = downloadManager.enqueue(request);
        //注册广播接收者，监听下载状态
        MyApplication.getInstance().registerReceiver(new DownloadFileReceiver(mskId, downloadManager, fileName), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    //下载到应用专用目录下（带多个参数）
    public static void downloadFile_programPath(String url, String filePath, String fileName, Bundle bundle) {
        //创建下载任务
        if (new File(filePath).exists()) {
            Log.e("tag", "文件已存在");
            return;
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedOverRoaming(true);//漫游网络是否可以下载

        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);


        //在通知栏中显示，默认就是显示的
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(false);
//        request.setTitle("正在下载");

        //自定义路径
        request.setDestinationUri(Uri.fromFile(new File(filePath)));
        //将下载请求加入下载队列
        DownloadManager downloadManager = (DownloadManager) MyApplication.getInstance().getSystemService(Context.DOWNLOAD_SERVICE);
        //加入下载队列后会给该任务返回一个long型的id，
        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        long mskId = downloadManager.enqueue(request);
        //注册广播接收者，监听下载状态
        MyApplication.getInstance().registerReceiver(new DownloadFileReceiver(mskId, downloadManager, fileName, bundle), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    //使用系统下载器下载
    public static void downloadPDF(String url, String fileName) {
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedOverRoaming(false);//漫游网络是否可以下载

        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);


        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);
        request.setTitle("正在下载");

        //sdcard的目录下的download文件夹，必须设置
        request.setDestinationInExternalPublicDir("/download/", fileName);
        //request.setDestinationInExternalFilesDir(),也可以自己制定下载路径

        //将下载请求加入下载队列
        DownloadManager downloadManager = (DownloadManager) MyApplication.getInstance().getSystemService(Context.DOWNLOAD_SERVICE);
        //加入下载队列后会给该任务返回一个long型的id，
        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        long mskId = downloadManager.enqueue(request);
        //注册广播接收者，监听下载状态
        MyApplication.getInstance().registerReceiver(new DownloadReceiver(mskId, downloadManager, fileName), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {//如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
