package own.stromsong.myapplication.mvp.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.youth.xframe.widget.XLoadingDialog;

import own.stromsong.myapplication.app.MyApplication;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesHelper;

/**
 * Created by Administrator on 2017/11/1 0001.
 * fragment基类(优化懒加载，公用方法等等)
 */

public class BaseFragment extends Fragment {
    public Context mContext;
    protected SharedPreferencesHelper mHelper;
    protected XLoadingDialog mLoading;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = SharedPreferencesHelper.getInstance(MyApplication.getInstance());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void initActionBar() {

    }

    public void showLoading() {
        mLoading = XLoadingDialog.with(mContext).setCanceled(false);
        mLoading.show();
    }

    public void goneLoading() {
        if (mLoading != null) {
            mLoading.dismiss();
        }
    }

    public void showContent() {
        goneLoading();
    }

    public void showEmpty() {
        goneLoading();
    }

    public void showError() {
        goneLoading();
    }

    public void showNoNetwork() {
        goneLoading();
    }

    /**
     * 弹出toast 显示时长short
     *
     * @param pMsg
     */
    protected void showToastMsgShort(String pMsg) {
        ToastUtils.showShort(pMsg);
    }
}
