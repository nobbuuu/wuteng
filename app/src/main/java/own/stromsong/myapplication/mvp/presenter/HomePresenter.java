package own.stromsong.myapplication.mvp.presenter;

import android.app.Activity;
import android.content.Context;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import own.stromsong.myapplication.mvp.base.BaseObserver;
import own.stromsong.myapplication.mvp.base.BasePresenter;
import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.mvp.model.WeatherBean;
import own.stromsong.myapplication.mvp.view.interfaces.IHomeAct;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesTag;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public class HomePresenter extends BasePresenter<IHomeAct> {

    private Context mContext;

    public HomePresenter(IHomeAct iHomeAct) {
        this.mContext = (Activity)iHomeAct;
        attach(iHomeAct);
    }

    public void getWeather() {
        addObserver(mApiStores.getWeather("http://www.weather.com.cn/data/cityinfo/101280601.html"), new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                mvpView.getWeather(((WeatherBean) o).getWeatherinfo());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
    /**
     * 获取节目单
     */
    public void getList() {
        BaseObserver<MenuBean> observer = new BaseObserver<MenuBean>(mContext,mvpView) {
            @Override
            public void onResponseCodeSuccess(MenuBean menuBean) {
                if (menuBean != null) {
                    mvpView.getMenuList(menuBean.getListResult(),menuBean.getSubtitles());
                }
            }
        };
        addObserver(mApiStores.showMenu(mHelper.getStringValue(SharedPreferencesTag.TOKEN)),observer);
    }
}
