package own.stromsong.myapplication.mvp.presenter;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.AppUtils;
import com.tencent.android.tpush.XGPushManager;

import own.stromsong.myapplication.mvp.base.BaseObserver;
import own.stromsong.myapplication.mvp.base.BasePresenter;
import own.stromsong.myapplication.mvp.model.Result;
import own.stromsong.myapplication.mvp.model.UpdateAppBean;
import own.stromsong.myapplication.mvp.view.interfaces.ILogOutAndUpdata;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesTag;

/**
 * Created by Administrator on 2018/3/8 0008.
 */

public class LoginOutPresenter extends BasePresenter<ILogOutAndUpdata> {
    private Context context;

    public LoginOutPresenter(ILogOutAndUpdata iLogOutAndUpdata) {
        this.context = (Activity)iLogOutAndUpdata;
        attach(iLogOutAndUpdata);
    }

    public void logOut() {
        BaseObserver<Result> mObserver = new BaseObserver<Result>(context, mvpView) {
            @Override
            public void onResponseCodeSuccess(Result mResult) {
                if (mResult != null && "退出登录".equalsIgnoreCase(mResult.getResult())) {
                    mvpView.isSuccess();
                    mHelper.putBooleanValue(SharedPreferencesTag.LOGIN_BOOLEAN, false);
                    XGPushManager.delAccount(context,mHelper.getStringValue(SharedPreferencesTag.ID));
                } else {
                    mvpView.fail();
                }
            }
        };
        addObserver(mApiStores.logOut(mHelper.getStringValue(SharedPreferencesTag.TOKEN)), mObserver);
    }

    public void getVersionInfo() {
        BaseObserver<UpdateAppBean> mObserver = new BaseObserver<UpdateAppBean>(context, mvpView) {
            @Override
            public void onResponseCodeSuccess(UpdateAppBean mUpdateAppBean) {
                if (mUpdateAppBean != null &&mUpdateAppBean.getAPK()!=null) {
                    UpdateAppBean.APKBean apk = mUpdateAppBean.getAPK();
                    mvpView.getDownloadUrl(apk.getAppFile(),apk.getVersion());
                }
            }
        };
        addObserver(mApiStores.appApk(mHelper.getStringValue(SharedPreferencesTag.TOKEN), AppUtils.getAppVersionName()), mObserver);
    }
}
