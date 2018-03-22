package own.stromsong.myapplication.mvp.base;

import android.content.Context;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import own.stromsong.myapplication.app.MyApplication;
import own.stromsong.myapplication.mvp.view.activity.MainActivity;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesHelper;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesTag;

/**
 * Created by Administrator on 2017/8/17.
 */

public abstract class BaseObserver<T> implements Observer<HttpResponse<T>> {

    private Context mContext;
    private BaseView mBaseView;
    private Disposable mDisposable;

    /**
     * @param context
     * @param baseView 传入view对象
     */
    public BaseObserver(Context context, BaseView baseView) {
        mContext = context;
        mBaseView = baseView;
        mBaseView.showLoading();
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(HttpResponse<T> tHttpResponse) {
        if (NetworkUtils.isConnected()) {//判断网络
            if (tHttpResponse.isSuccess()) {
                mBaseView.showContent();
                T t = tHttpResponse.getData();
                onResponseCodeSuccess(t);
            } else {
                onResponseCodeError(tHttpResponse.getErrorCode(), tHttpResponse.getErrorMsg());
            }
        } else {
            mBaseView.showNoNetwork();
        }

    }

    @Override
    public void onError(Throwable e) {
        LogUtils.d("ad_", "error:" + e.toString());

        mBaseView.showError();
        if (e.getMessage() == null) {
            ToastUtils.showShort(e + "");
            return;
        }
        ToastUtils.showShort(e.getMessage() + "");
    }

    @Override
    public void onComplete() {
        LogUtils.d("ad_", "onComplete");
    }

    public abstract void onResponseCodeSuccess(T t);

    /**
     * 请求失败统一处理返回码
     *
     * @param code
     * @param message
     */
    void onResponseCodeError(String code, String message) {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(MyApplication.getInstance());
        LogUtils.d("返回码：" + code, "msg信息：" + message);
        ToastUtils.showShort(message);
        //根据统一返回码继续做相应处理
        mBaseView.showError();
        switch (code) {
            case "0001"://token失效
                ActivityUtils.finishToActivity(MainActivity.class, false);
                helper.putBooleanValue(SharedPreferencesTag.LOGIN_BOOLEAN, false);
                break;
        }
    }

}
