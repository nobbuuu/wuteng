package own.stromsong.myapplication.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import own.stromsong.myapplication.app.MyApplication;
import own.stromsong.myapplication.other.Const;

/**
 * Created by Administrator on 2018/5/16 0016.
 */

public class BitmapUtils {
    public static Drawable transformBitmapToDrawable(Bitmap bitmap) {
        Drawable drawable = null;
        File tempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString(), "temp.jpg");
        if(tempFile.exists()){
            try {

                FileOutputStream out = new FileOutputStream(tempFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
                drawable = BitmapDrawable.createFromPath(tempFile.getAbsolutePath());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (drawable == null){
            Log.e("BitmapToDrawable", "Fail to transform drawable");
        }
        tempFile.delete();
        return drawable;
    }

    public static File bitmapToFile(Bitmap bitmap,String filePath){
        String downPath = MyApplication.getInstance().getExternalCacheDir() + File.separator+ Const.IMAGES+File.separator +"surfaceVideo.png" ;
        File file=new File(downPath);//将要保存图片的路径
        if (file.exists()){
            file.delete();
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            //保留图片30%的质量
            if (bos!=null){
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                bos.flush();
                bos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * @Title: getVideoFrame
     * @Description: 获取视频某帧的图像，但得到的图像并不一定是指定position的图像。
     * @param path     视频的本地路径
     * @return Bitmap 返回的视频图像
     * @throws
     */
    @SuppressLint("NewApi")
    public static Bitmap getVideoFrame(String path, MediaPlayer mediaPlayer) {

        Bitmap bmp = null;
        // android 9及其以上版本可以使用该方法
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path);
            // 这一句是必须的
            String timeString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            // 获取总长度,这一句也是必须的
            long titalTime = Long.parseLong(timeString) * 1000;

            long videoPosition = 0;
            try {
                mediaPlayer.setDataSource(path);
                if (path.startsWith("http")) {
                    mediaPlayer.prepareAsync();
                } else {
                    mediaPlayer.prepare();
                }
                int duration = mediaPlayer.getDuration();
                // 通过这个计算出想截取的画面所在的时间
                videoPosition = titalTime * mediaPlayer.getCurrentPosition() / duration;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (videoPosition > 0) {
                bmp = retriever.getFrameAtTime(videoPosition,
                        MediaMetadataRetriever.OPTION_CLOSEST);
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }
        return bmp;
    }
}
