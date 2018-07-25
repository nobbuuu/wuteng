package own.stromsong.myapplication.mvp.presenter;

import android.app.Activity;
import android.content.Context;

import own.stromsong.myapplication.mvp.base.BaseObserver;
import own.stromsong.myapplication.mvp.base.BasePresenter;
import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.mvp.view.interfaces.IMenu;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesTag;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public class MenuPresenter extends BasePresenter<IMenu>{
    private Context context;

    public MenuPresenter(IMenu iMenu) {
        this.context = (Activity) iMenu;
        attach(iMenu);
    }

    /**
     * 获取节目单
     */
    public void getList() {
        BaseObserver<MenuBean> observer = new BaseObserver<MenuBean>(context,mvpView) {
            @Override
            public void onResponseCodeSuccess(MenuBean menuBean) {
                if (menuBean != null) {
                    mvpView.getMenuList(menuBean.getListResult());
                }
            }
        };
        addObserver(mApiStores.showMenu(mHelper.getStringValue(SharedPreferencesTag.TOKEN),String.valueOf(System.currentTimeMillis())),observer);
    }
}
