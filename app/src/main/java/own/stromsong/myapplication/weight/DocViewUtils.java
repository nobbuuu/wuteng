package own.stromsong.myapplication.weight;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import own.stromsong.myapplication.app.MyApplication;

/**
 * Created by Administrator on 2018/5/30.
 */

public class DocViewUtils {

    private static DocViewUtils mDocViewUtils;
    public static DocViewUtils getInstance(){
        if (null==mDocViewUtils){
            mDocViewUtils = new DocViewUtils();
        }
        return mDocViewUtils;
    }
    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    public String getFileType(String paramString) {
        String str = "";
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.e(getClass().getName(), "i <= -1");
            return str;
        }
        if (paramString.contains("?")){
            int i1 = paramString.indexOf("?");
            if (i1>i){
                str = paramString.substring(i + 1,i1);
            }
        }else {
            str = paramString.substring(i + 1);
        }
        return str;
    }
}
