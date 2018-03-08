package own.stromsong.myapplication.mvp.view.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;

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
                mvpPresenter.login(id,password);
                break;
        }
    }

    @Override
    public void isSuccess(boolean success) {
        if (success) ActivityUtils.startActivity(HomeActivity.class);
    }
}
