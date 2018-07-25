package own.stromsong.myapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import own.stromsong.myapplication.app.MyApplication;

/**
 * Created by Administrator on 2017/10/13 0013.
 */
public class NetUtil {

    public static String getNetTypeName(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager
                    .getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return "无网络";
            }
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return "WIFI";
            } else {
                String typeName = activeNetworkInfo.getSubtypeName();
                //Log.i("网络名称:", typeName);
                if (typeName == null || typeName.length() == 0) {
                    return "未知网络";
                } else if (typeName.length() > 3) {
                    return activeNetworkInfo.getSubtypeName().substring(0, 4);
                } else {
                    return activeNetworkInfo.getSubtypeName().substring(0,
                            typeName.length());
                }
            }
        } else {
            return "无网络";
        }
    }

    public static boolean haveNet(){
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            //获取以太网连接信息
            NetworkInfo ethNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
            if (wifiNetworkInfo.isConnected()||dataNetworkInfo.isConnected()||ethNetworkInfo.isConnected()){
                return true;
            }else {
                return false;
            }
            //API大于23时使用下面的方式进行网络监听
        }else {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            boolean haveNet = false;
            for (int i=0; i < networks.length; i++){
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                if (networkInfo.isConnected()){
                    haveNet = true;
                }
            }
            if (haveNet){
                return true;
            }else {
                return false;
            }
        }
    }
}
