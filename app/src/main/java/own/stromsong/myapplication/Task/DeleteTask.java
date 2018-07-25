package own.stromsong.myapplication.Task;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import own.stromsong.myapplication.app.MyApplication;
import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.other.Const;
import own.stromsong.myapplication.utils.FileUtils;
import own.stromsong.myapplication.utils.SpUtils;

public class DeleteTask extends AsyncTask<String,String,String> {
    private String menuId;
    public DeleteTask(String menuId){
        this.menuId=menuId;
    }
        @Override
        protected String doInBackground(String... strings) {
            String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "wuteng";
            String proImgPath = MyApplication.getInstance().getExternalCacheDir() + "/images";
            String proHtmlPath = MyApplication.getInstance().getExternalCacheDir() + "/html";
            String proPdfPath = MyApplication.getInstance().getExternalCacheDir() + "/pdf";
            String proVideoPath = MyApplication.getInstance().getExternalCacheDir() + "/video";
            try {
                File file = new File(downloadPath);
                if (file!=null&&file.exists()){
                    File[] files = file.listFiles();
                    if (files!=null&&files.length>0){
                        for (File mfile: files){
                            if (!mfile.getName().equals(menuId)){
                                FileUtils.deleteFilelistAndFile(mfile);
                            }
                        }
                    }
                }
                FileUtils.deleteListFile(new File(proImgPath));
                FileUtils.deleteListFile(new File(proHtmlPath));
                FileUtils.deleteListFile(new File(proPdfPath));
                FileUtils.deleteListFile(new File(proVideoPath));
            }catch (Exception e){
                Log.e("tag","exception="+e.toString());
            }
            return "deleteComplete";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SpUtils.savaUserInfo(Const.menuId,menuId);
        }
    }