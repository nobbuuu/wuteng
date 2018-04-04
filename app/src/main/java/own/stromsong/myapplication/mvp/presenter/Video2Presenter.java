package own.stromsong.myapplication.mvp.presenter;

import android.app.Activity;
import android.content.Context;

import own.stromsong.myapplication.mvp.base.BaseObserver;
import own.stromsong.myapplication.mvp.base.BasePresenter;
import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.mvp.view.interfaces.IVideo2Act;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesTag;

/**
 * Created by Administrator on 2018/4/4 0004.
 */

public class Video2Presenter extends BasePresenter<IVideo2Act> {
    private Context mContext;

    public Video2Presenter(IVideo2Act iVideo2Act) {
        this.mContext = (Activity)iVideo2Act;
        attach(iVideo2Act);
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
