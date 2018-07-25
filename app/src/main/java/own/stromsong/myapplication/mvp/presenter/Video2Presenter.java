package own.stromsong.myapplication.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import own.stromsong.myapplication.mvp.base.BaseObserver;
import own.stromsong.myapplication.mvp.base.BasePresenter;
import own.stromsong.myapplication.mvp.base.HttpResponse;
import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.mvp.model.UploadImgbean;
import own.stromsong.myapplication.mvp.model.WeatherBean;
import own.stromsong.myapplication.mvp.model.WeatherValueBean;
import own.stromsong.myapplication.mvp.view.interfaces.IVideo2Act;
import own.stromsong.myapplication.retrofit.ApiStores;
import own.stromsong.myapplication.retrofit.RetrofitFactory;
import own.stromsong.myapplication.utils.ImageUtils;
import own.stromsong.myapplication.utils.StringUtil;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesTag;

/**
 * Created by Administrator on 2018/4/4 0004.
 */

public class Video2Presenter extends BasePresenter<IVideo2Act> {
    private Context mContext;

    public Video2Presenter(IVideo2Act iVideo2Act) {
        this.mContext = (Activity) iVideo2Act;
        attach(iVideo2Act);
    }

    /**
     * 获取节目单
     */
    public void getList() {
        BaseObserver<MenuBean> observer = new BaseObserver<MenuBean>(mContext, mvpView) {
            @Override
            public void onResponseCodeSuccess(MenuBean menuBean) {
                mvpView.getMenuList(menuBean.getListResult(), menuBean.getSubtitles());
                if (menuBean != null&&menuBean.getListResult().size()>0) {
                    mvpView.refreshCity(menuBean.getListResult().get(0).getCity());
                }
            }
        };
        String token = mHelper.getStringValue(SharedPreferencesTag.TOKEN);
        Log.e("tag","token="+token);
        if (StringUtil.NoNullOrEmpty(token)){
            addObserver(mApiStores.showMenu(token,String.valueOf(System.currentTimeMillis())), observer);
        }
    }

    public void getSubtitles(){
        BaseObserver<List<MenuBean.SubtitlesBean>> observer = new BaseObserver<List<MenuBean.SubtitlesBean>>(mContext, mvpView) {
            @Override
            public void onResponseCodeSuccess(List<MenuBean.SubtitlesBean> data) {
                mvpView.showSubtitles(data);
            }
        };
        String token = mHelper.getStringValue(SharedPreferencesTag.TOKEN);
        addObserver(mApiStores.getSubtitlesList(token), observer);
    }

    public void getimingShowMenu(String menuId){
        BaseObserver<MenuBean.ListResultBean> observer = new BaseObserver<MenuBean.ListResultBean>(mContext, mvpView) {
            @Override
            public void onResponseCodeSuccess(MenuBean.ListResultBean data) {
                mvpView.showMenuBean(data);
                String city = data.getCity();
                if (StringUtil.NoNullOrEmpty(city)){
                    mvpView.refreshCity(city);
                }
            }
        };
        String token = mHelper.getStringValue(SharedPreferencesTag.TOKEN);
        Log.e("tag","token="+token);
        addObserver(mApiStores.getimingShowMenu(token,menuId), observer);
    }
    /**
     * 获取城市代码
     */
    public void getCityCode(String cityName) {
        BaseObserver<String> observer = new BaseObserver<String>(mContext, mvpView) {
            @Override
            public void onResponseCodeSuccess(String xmlStr) {
                mvpView.showCityCode(xmlStr);
            }
        };
        addObserver(mApiStores.getCityCode(ApiStores.CITYCODE_SERVICE_URL+cityName), observer);
    }
    public void commitPic(File file, final int voice) {
        Log.e("aa", "开始截屏上传");
        BaseObserver<UploadImgbean> baseObserver = new BaseObserver<UploadImgbean>(mContext, mvpView) {
            @Override
            public void onResponseCodeSuccess(UploadImgbean uploadImgbean) {
                if (uploadImgbean != null && !TextUtils.isEmpty(uploadImgbean.getUrl()))
                    commitImage(uploadImgbean.getUrl(), voice);
            }
        };
        addObserver(mApiStores.uploadImgMethod(ImageUtils.getRequestBodyParts("files", file), ImageUtils.getBody("1"),ImageUtils.getBody("img")), baseObserver);
    }

    /**
     * 获取天气
     */
    public void getWeatherData(String cityName) {
        BaseObserver<WeatherValueBean> observer = new BaseObserver<WeatherValueBean>(mContext, mvpView) {
            @Override
            public void onResponseCodeSuccess(WeatherValueBean mStr) {
                if (StringUtil.NoNullOrEmpty(mStr.getValue())){
                    mvpView.showWeather(mStr.getValue());
                }
            }
        };
        addObserver(mApiStores.getWeather(cityName), observer);
    }


    private void commitImage(final String filePath, final int voice) {
        BaseObserver mObserver = new BaseObserver(mContext, mvpView) {
            @Override
            public void onResponseCodeSuccess(Object o) {

            }

            @Override
            public void onNext(Object o) {
                mvpView.screenshotsSuccess();
            }
        };
        addObserver(mApiStores.updateEquipment(mHelper.getStringValue(SharedPreferencesTag.ID1), voice, filePath), mObserver);
    }
    public void refreshDevice(Map<String,String> map,String status) {
        BaseObserver mObserver = new BaseObserver(mContext, mvpView) {
            @Override
            public void onResponseCodeSuccess(Object o) {
            }

            @Override
            public void onNext(Object o) {
                HttpResponse httpResponse = (HttpResponse) o;
                mvpView.processOff(status);
            }
        };
        String deviceId = mHelper.getStringValue(SharedPreferencesTag.ID1);
        addObserver(mApiStores.updateEquipment(deviceId, map), mObserver);
    }



}
