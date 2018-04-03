package own.stromsong.myapplication.mvp.presenter;

import android.app.Activity;
import android.content.Context;

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
                    mvpView.isSuccess();
                    mHelper.putBooleanValue(SharedPreferencesTag.LOGIN_BOOLEAN, true);
                    mHelper.putStringValue(SharedPreferencesTag.TOKEN, mLoginBean.getToken());
                    mHelper.putStringValue(SharedPreferencesTag.ID,equId);
                }
            }
        };
        addObserver(mApiStores.login(equId, password), mObserver);
    }
}
