package own.stromsong.myapplication.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import own.stromsong.myapplication.mvp.base.BaseObserver;
import own.stromsong.myapplication.mvp.base.BasePresenter;
import own.stromsong.myapplication.mvp.model.LoginBean;
import own.stromsong.myapplication.mvp.view.interfaces.ILoginAct;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesTag;

/**
 * Created by Administrator on 2018/3/7 0007.
 */

public class LoginPresenter extends BasePresenter<ILoginAct> {
    private Context context;

    public LoginPresenter(ILoginAct iLoginAct) {
        this.context = (Activity)iLoginAct;
        attach(iLoginAct);
    }

    /**
     * 登录
     */
    public void login(final String equId, String password) {

        BaseObserver<LoginBean> mObserver = new BaseObserver<LoginBean>(context, mvpView) {
            @Override
            public void onResponseCodeSuccess(LoginBean mLoginBean) {
                if (mLoginBean != null) {
                    mHelper.putBooleanValue(SharedPreferencesTag.LOGIN_BOOLEAN, true);
                    mHelper.putStringValue(SharedPreferencesTag.TOKEN, mLoginBean.getToken());
                    Log.e("tag","token="+mLoginBean.getToken());
                    mHelper.putStringValue(SharedPreferencesTag.ID,equId);
                    mHelper.putStringValue(SharedPreferencesTag.ID1,mLoginBean.getId());
                    mvpView.isSuccess();
                }
            }
        };
        addObserver(mApiStores.login(equId, password), mObserver);
    }
}
