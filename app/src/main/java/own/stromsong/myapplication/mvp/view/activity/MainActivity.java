package own.stromsong.myapplication.mvp.view.activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.weavey.loading.lib.LoadingLayout;

import butterknife.BindView;
import butterknife.OnClick;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.mvp.base.MvpActivity;
import own.stromsong.myapplication.mvp.presenter.LoginPresenter;
import own.stromsong.myapplication.mvp.view.interfaces.ILoginAct;

public class MainActivity extends MvpActivity<LoginPresenter> implements ILoginAct {

    @BindView(R.id.et_id)
    EditText mEtId;
    @BindView(R.id.et_pw)
    EditText mEtPw;
    @BindView(R.id.login)
    TextView mLogin;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        setBarVisible(false);
    }

    @OnClick(R.id.login)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                String id = mEtId.getText().toString().trim();
                String password = mEtPw.getText().toString().trim();
                mvpPresenter.login(id, password);
                break;
        }
    }

    @Override
    public void isSuccess() {
        XGPushManager.registerPush(this, mEtId.getText().toString().trim(),new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                Log.d("TPush", "注册成功，设备token为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });

        ActivityUtils.startActivity(HomeActivity.class);
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
