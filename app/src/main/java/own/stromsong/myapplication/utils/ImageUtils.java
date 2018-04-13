package own.stromsong.myapplication.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/12/4 0004.
 * 作者 sst
 */

public class ImageUtils {
    /**
     * 将参数名与文件装换成parts对象用于retrofit文件上传。
     *
     * @param element
     * @param files
     * @return
     */
    public static MultipartBody.Part[] getRequestBodyParts(String element, List<File> files) {
        if (files != null) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//ParamKey.TOKEN 自定义参数key常量类，即参数名
            MultipartBody.Part[] parts = new MultipartBody.Part[files.size()];
            for (int i = 0; i < files.size(); i++) {
                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), files.get(i));
                builder.addFormDataPart(element, files.get(i).getName(), imageBody);//imgfile 后台接收图片流的参数名
                parts[i] = builder.build().part(i);
            }
            return parts;
        } else {
            return null;
        }
    }

    /**
     * @param element
     * @param strings
     * @return
     */
    public static MultipartBody.Part[] getRequestBodyParts2(String element, List<String> strings) {
        if (strings != null) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//ParamKey.TOKEN 自定义参数key常量类，即参数名
            MultipartBody.Part[] parts = new MultipartBody.Part[strings.size()];
            for (int i = 0; i < strings.size(); i++) {
                builder.addFormDataPart(element, strings.get(i));//imgfile 后台接收图片流的参数名
                parts[i] = builder.build().part(i);
            }
            return parts;
        } else {
            return null;
        }
    }

    /**
     * 将参数名与文件装换成parts对象用于retrofit文件上传。
     *
     * @param element
     * @param files
     * @return
     */
    public static MultipartBody.Part getRequestBodyParts(String element, File files) {
        if (files != null) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//ParamKey.TOKEN 自定义参数key常量类，即参数名
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), files);
            builder.addFormDataPart(element, files.getName(), imageBody);//imgfile 后台接收图片流的参数名
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
     * 检测字符串的null与empty状态
     *
     * @param s
     * @return
     */
    public static boolean CheckStringEmpty(String s) {
        if (s == null) {
            return false;
        }
        if ("".equals(s)) {
            return false;
        }
        return true;
    }


    /**
     * 简单检测输入内容是手机号
     *
     * @return
     */
    public static boolean checkPhoneMember( String s) {
        if (TextUtils.isEmpty(s)) {
            ToastUtils.showShort("请输入手机号", 1000);
            return false;
        }
        if (s.length() != 11) {
            ToastUtils.showShort("手机号有误", 1000);
            return false;
        }
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(s);
        if (!m.matches()) {
            ToastUtils.showShort("手机号有误", 1000);
            return false;
        }
        return true;
    }
}
