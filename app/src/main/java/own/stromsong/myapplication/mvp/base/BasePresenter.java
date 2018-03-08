package own.stromsong.myapplication.mvp.base;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import own.stromsong.myapplication.app.MyApplication;
import own.stromsong.myapplication.retrofit.ApiStores;
import own.stromsong.myapplication.retrofit.RetrofitFactory;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesHelper;

/**
 * Created by Administrator on 2017/8/16.
 * presenter的父类主要用来实现baseView的初始化与销毁工作
 */

public class BasePresenter<T> {
    private final long RETRY_TIMES = 1;//请求失败重试的次数
    public T mvpView;
    protected ApiStores mApiStores;
    private CompositeDisposable mCompositeDisposable;
    protected SharedPreferencesHelper mHelper;

    public void attach(T mvpView) {
        this.mvpView = mvpView;
        mApiStores = RetrofitFactory.retrofit().create(ApiStores.class);
        mHelper = SharedPreferencesHelper.getInstance(MyApplication.getInstance());
    }

    public void detach() {
        mvpView = null;
        onUnobserver();
    }

    public void addObserver(Observable observable, Observer observer) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        observable.retry(RETRY_TIMES)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mCompositeDisposable.add(disposable);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    //RXjava2清除保存的disposable对象
    public void onUnobserver() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }
}
