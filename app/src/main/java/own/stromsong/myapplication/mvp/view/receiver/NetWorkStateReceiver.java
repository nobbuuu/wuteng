package own.stromsong.myapplication.mvp.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;

import org.greenrobot.eventbus.EventBus;

import own.stromsong.myapplication.mvp.model.NetworkBean;

/**
 * Created by Carson_Ho on 16/10/31.
 */
public class NetWorkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        NetworkBean networkBean = new NetworkBean();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            //获取以太网连接信息
            NetworkInfo ethNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
            if (wifiNetworkInfo.isConnected()||dataNetworkInfo.isConnected()||ethNetworkInfo.isConnected()){
                networkBean.setNetworkStatus("haveNet");
            }else {
                networkBean.setNetworkStatus("noNet");
            }
        //API大于23时使用下面的方式进行网络监听
        }else {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
                networkBean.setNetworkStatus("haveNet");
            }else {
                networkBean.setNetworkStatus("noNet");
            }
        }
        EventBus.getDefault().post(networkBean);
    }
}
