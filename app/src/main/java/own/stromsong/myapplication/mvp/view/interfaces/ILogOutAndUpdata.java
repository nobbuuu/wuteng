package own.stromsong.myapplication.mvp.view.interfaces;

import own.stromsong.myapplication.mvp.base.BaseView;

/**
 * Created by Administrator on 2018/3/8 0008.
 */

public interface ILogOutAndUpdata extends BaseView {
    void isSuccess();

    void fail();

    void getDownloadUrl(String downUrl, String versionName);
}
