package own.stromsong.myapplication.mvp.presenter;

import android.app.Activity;
import android.content.Context;

import own.stromsong.myapplication.mvp.base.BaseObserver;
import own.stromsong.myapplication.mvp.base.BasePresenter;
import own.stromsong.myapplication.mvp.model.LoginBean;
import own.stromsong.myapplication.mvp.model.UserBean;
import own.stromsong.myapplication.mvp.view.interfaces.ILoginAct;

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
    public void login(String equId, String password) {

        BaseObserver<LoginBean> mObserver = new BaseObserver<LoginBean>(context, mvpView) {
            @Override
            public void onResponseCodeSuccess(LoginBean mLoginBean) {

            }
        };
        addObserver(mApiStores.login(equId, password), mObserver);
    }
}
