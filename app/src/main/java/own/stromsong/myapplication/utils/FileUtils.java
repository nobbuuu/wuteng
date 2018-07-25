package own.stromsong.myapplication.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public static void deleteListFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
                delete(file.getPath());
        }
    }
    public static void deleteFilelistAndFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
                delete(file.getPath());
        }
        dir.delete();
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName
     *            要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir
     *            要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }


    private static final String path = "F:\\company\\dome\\godly";

    /**
     * 获取附件名称 13位时间戳+5位随机数
     *
     * @return
     */
    public static String createFileName() {
        return System.currentTimeMillis() + "" + (int) (Math.random() * 100000);
    }


    /**
     * 判断文件的扩展名
     *
     * @param suffix
     * @return
     */
    public static Boolean getImageFormat(String suffix) {
        return "TFF".equalsIgnoreCase(suffix) || "TIFF".equalsIgnoreCase(suffix) || "PNG".equalsIgnoreCase(suffix) || "GIF".equalsIgnoreCase(suffix) || "JPG".equalsIgnoreCase(suffix) || "JPEG".equalsIgnoreCase(suffix);
    }

    /**
     * 保存文件到磁盘
     *
     * @param file
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    public static void saveFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    /**
     * 创建目录
     *
     * @param descDirName 目录名,包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createDirectory(String descDirName) {
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        if (descDir.exists()) {
            return false;
        }
        // 创建目录
        if (descDir.mkdirs()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 创建单个文件
     *
     * @param descFileName 文件名，包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createFile(String descFileName) {
        File file = new File(descFileName);
        if (file.exists()) {
            return false;
        }
        if (descFileName.endsWith(File.separator)) {
            return false;
        }
        if (!file.getParentFile().exists()) {
            // 如果文件所在的目录不存在，则创建目录
            if (!file.getParentFile().mkdirs()) {
                return false;
            }
        }

        // 创建文件
        try {
            if (file.createNewFile()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 删除文件
     *
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("删除单个文件-> {}成功！", fileName);
                return true;
            } else {
                Log.e("删除单个文件-> {}失败！", fileName);
                return false;
            }
        } else {
            Log.i("删除单个文件失败-> {}不存在！", fileName);
            return false;
        }
    }


    /**
     * 获取某个文件的所有内容
     *
     * @param x
     * @return
     */
    public static String getFileToString(String x) {
        //模板路径
        String tempPath = "/godly-module/godly-core/src/main/resources/template"; //模板路径
        return tempPath;
    }


    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(String fileName) {
        StringBuffer str = new StringBuffer();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                str.append(tempString + "\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return str.toString();

    }


    /**
     * 根据留得到字符串
     * @param in
     * @return
     */
    public static String readFileByLines(InputStream in) {
        BufferedReader br = null;
        try{
            br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = br.readLine()) != null) {
                // 显示行号
                sb.append(tempString + "\n");
            }
            br.close();
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("tag","e="+e.toString());
        }
        return "";
    }


    /**
     * 写入文件
     *
     * @param fileName 文件路径
     * @param content  文件内容
     */
    public static void writeToFile(String fileName, String content) {
        FileUtils.createFile(fileName);
        try {
            FileWriter fw = new FileWriter(fileName, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);// 往已有的文件上添加字符串
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入文件
     *
     * @param fileName 文件路径
     * @param content  文件内容
     */
    public static boolean writeToFile(String fileName, String content,boolean falg) {
        try {
            FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);// 往已有的文件上添加字符串
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return falg;
    }


}
