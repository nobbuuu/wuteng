package own.stromsong.myapplication.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.Map;

import own.stromsong.myapplication.mvp.base.BaseObserver;
import own.stromsong.myapplication.mvp.base.BasePresenter;
import own.stromsong.myapplication.mvp.base.HttpResponse;
import own.stromsong.myapplication.mvp.model.LoginBean;
import own.stromsong.myapplication.mvp.view.interfaces.ILoginAct;
import own.stromsong.myapplication.mvp.view.interfaces.IShutdownAct;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesTag;

/**
 * Created by Administrator on 2018/3/7 0007.
 */

public class ShutdownPresenter extends BasePresenter<IShutdownAct> {
    private Context mContext;

    public ShutdownPresenter(IShutdownAct iLoginAct) {
        this.mContext = (Activity)iLoginAct;
        attach(iLoginAct);
    }

    public void refreshDevice(Map<String,String> map) {
        BaseObserver mObserver = new BaseObserver(mContext, mvpView) {
            @Override
            public void onResponseCodeSuccess(Object o) {
            }

            @Override
            public void onNext(Object o) {
                mvpView.refreshShutdown();
            }
        };
        String deviceId = mHelper.getStringValue(SharedPreferencesTag.ID1);
        addObserver(mApiStores.updateEquipment(deviceId, map), mObserver);
    }
}
