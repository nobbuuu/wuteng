package own.stromsong.myapplication.mvp.view.activity;


import android.Manifest;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.youth.xframe.widget.XLoadingDialog;

import butterknife.BindView;
import butterknife.OnClick;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.mvp.base.MvpActivity;
import own.stromsong.myapplication.mvp.presenter.LoginOutPresenter;
import own.stromsong.myapplication.mvp.view.interfaces.ILogOutAndUpdata;
import own.stromsong.myapplication.utils.cache.GlideCatchUtil;
import own.stromsong.myapplication.utils.dialog.VersionDialog;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesTag;

public class AboutActivity extends MvpActivity<LoginOutPresenter> implements ILogOutAndUpdata {

    @BindView(R.id.update_ll)
    LinearLayout mUpdateLl;
    @BindView(R.id.clearCache_ll)
    LinearLayout mClearCacheLl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData() {
        setTitle("关于我们");
        setLeftIcon(R.drawable.icon_home);
    }

    @OnClick({ R.id.update_ll, R.id.clearCache_ll, R.id.exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update_ll:
                AndPermission.with(this)
                        .requestCode(100)
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .send();
                break;
            case R.id.clearCache_ll:
                XLoadingDialog dialog = XLoadingDialog.with(this).setMessage("缓存清理中");
                dialog.show();
                if (GlideCatchUtil.getInstance().clearCacheDiskSelf()) {
                    dialog.dismiss();
                    showToastMsgShort("清理完成");
                } else {
                    dialog.dismiss();
                    showToastMsgShort("清理失败");
                }
                break;
            case R.id.exit:
                mvpPresenter.logOut();
                break;
        }
    }

    @Override
    protected LoginOutPresenter createPresenter() {
        return new LoginOutPresenter(this);
    }

    @Override
    public void isSuccess() {//退出登录成功
        ActivityUtils.finishToActivity(MainActivity.class, false);
        mHelper.putBooleanValue(SharedPreferencesTag.LOGIN_BOOLEAN, false);
    }

    @Override
    public void fail() {
        showToastMsgShort("失败");
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(100)
    private void getLocationYes() {
        mvpPresenter.getVersionInfo();
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(100)
    private void getLocationNo() {
        showToastMsgShort("无读写内存卡权限");
    }

    @Override
    public void getDownloadUrl(String downUrl, String versionName) {
        if (versionName.equalsIgnoreCase(AppUtils.getAppVersionName())) {
            showToastMsgShort("已是最新版本");
            return;
        }
        VersionDialog.getVersionDialog(versionName, downUrl, this);
    }

    @Override
    public void showLoading() {
        showLoading1();
    }

    @Override
    public void showContent() {
        goneLoading1();
    }

    @Override
    public void showEmpty() {
        goneLoading1();
    }

    @Override
    public void showError() {
        goneLoading1();
    }

    @Override
    public void showNoNetwork() {
        goneLoading1();
    }
}
