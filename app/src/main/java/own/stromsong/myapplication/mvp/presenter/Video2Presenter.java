package own.stromsong.myapplication.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

import own.stromsong.myapplication.mvp.base.BaseObserver;
import own.stromsong.myapplication.mvp.base.BasePresenter;
import own.stromsong.myapplication.mvp.base.HttpResponse;
import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.mvp.model.UploadImgbean;
import own.stromsong.myapplication.mvp.view.interfaces.IVideo2Act;
import own.stromsong.myapplication.utils.ImageUtils;
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
                if (menuBean != null) {
                    mvpView.getMenuList(menuBean.getListResult(), menuBean.getSubtitles());
                }
            }
        };
        addObserver(mApiStores.showMenu(mHelper.getStringValue(SharedPreferencesTag.TOKEN)), observer);
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

    private void commitImage(final String filePath, final int voice) {
        BaseObserver mObserver = new BaseObserver(mContext, mvpView) {
            @Override
            public void onResponseCodeSuccess(Object o) {
            }

            @Override
            public void onNext(Object o) {
                HttpResponse httpResponse = (HttpResponse) o;
                Log.e("aa", "上传结果---"+httpResponse.getErrorMsg());
            }
        };
        addObserver(mApiStores.updateEquipment(mHelper.getStringValue(SharedPreferencesTag.ID1), voice, filePath), mObserver);
    }
}
