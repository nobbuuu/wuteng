package own.stromsong.myapplication.mvp.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import java.io.File;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.app.MyApplication;
import own.stromsong.myapplication.listener.NoDoubleClickListener;
import own.stromsong.myapplication.mvp.base.MvpActivity;
import own.stromsong.myapplication.mvp.presenter.LoginPresenter;
import own.stromsong.myapplication.mvp.view.interfaces.ILoginAct;
import own.stromsong.myapplication.utils.DeviceUtils;
import own.stromsong.myapplication.utils.FileUtils;
import own.stromsong.myapplication.utils.NetUtil;
import own.stromsong.myapplication.utils.StringUtil;
import own.stromsong.myapplication.utils.dialog.DialogUtils;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesHelper;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesTag;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isLogin = SharedPreferencesHelper.getInstance(MyApplication.getInstance()).getBooleanValue(SharedPreferencesTag.LOGIN_BOOLEAN);
        Log.e("tag","isLogin="+isLogin);
        if (isLogin){
            String TOKEN = SharedPreferencesHelper.getInstance(MyApplication.getInstance()).getStringValue(SharedPreferencesTag.TOKEN);
            if (StringUtil.NoNullOrEmpty(TOKEN)){
                XGPushManager.registerPush(this, TOKEN,new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        //token在设备卸载重装的时候有可能会变
                        Log.e("TPush", "注册成功，设备token为：" + data);
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.e("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                    }
                });
            }
            ActivityUtils.startActivity(Video2Activity.class);
            finish();
        }else {
            checkNetAction();
        }
    }

    private boolean checkNetAction() {
        if (!NetUtil.haveNet()){
            DialogUtils.showDeleteDialog(this, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    Intent intent =  new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                }
            });
            return false;
        }
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        setBarVisible(false);
    }

    @OnClick({R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                toLogin();
                break;
        }
    }

    private void toLogin() {
        if (checkNetAction()){
            String androidID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            //设备唯一标识，实现账号绑定设备
//                    String password = androidID + Build.SERIAL;
            String password = DeviceUtils.getAdresseMAC(getApplicationContext());
            Log.e("tag","password="+password);
            String id = mEtId.getText().toString().trim();
//                    String password = mEtPw.getText().toString().trim();
            mvpPresenter.login(id, password);
        }
    }

    private boolean isLoginble;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("tag","keycode="+keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            ActivityUtils.finishOtherActivities(MainActivity.class);
            finish();
            System.gc();
            Process.killProcess(Process.myPid());
            return true;
        }
        String s = mEtId.getText().toString();
        if (keyCode==20&&!s.isEmpty()){
            mEtId.setFocusable(false);
            mEtId.setFocusableInTouchMode(false);
            mLogin.setBackgroundResource(R.drawable.rectangle_red_stroke);
            isLoginble = true;
        }

        if (keyCode==19&&isLoginble){

            mEtId.setFocusable(true);
            mEtId.setFocusableInTouchMode(true);
            mEtId.requestFocus();
            mEtId.findFocus();
            mLogin.setBackgroundResource(R.drawable.rectangle_red);
            mEtId.setBackgroundResource(R.drawable.edittext_debg_chose);
            isLoginble = false;
        }

        if (keyCode==23&&isLoginble){
            toLogin();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void isSuccess() {
        String TOKEN = SharedPreferencesHelper.getInstance(MyApplication.getInstance()).getStringValue(SharedPreferencesTag.TOKEN);
        if (StringUtil.NoNullOrEmpty(TOKEN)){
            XGPushManager.registerPush(this, TOKEN,new XGIOperateCallback() {
                @Override
                public void onSuccess(Object data, int flag) {
                    //token在设备卸载重装的时候有可能会变
                    Log.e("TPush", "注册成功，设备token为：" + data);
                }

                @Override
                public void onFail(Object data, int errCode, String msg) {
                    Log.e("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                }
            });
        }
        writeJs();
        ActivityUtils.startActivity(Video2Activity.class);
        finish();
    }

    public void writeJs() {
        File externalCacheDir = this.getExternalCacheDir();
        InputStream min = this.getClass().getClassLoader().getResourceAsStream("assets/js/jquery.min.js");
        FileUtils.writeToFile(externalCacheDir + "/jquery.min.js", FileUtils.readFileByLines(min), true);
        InputStream jis = this.getClass().getClassLoader().getResourceAsStream("assets/css/shutter.css");
        FileUtils.writeToFile(externalCacheDir + "/shutter.css", FileUtils.readFileByLines(jis), true);
        InputStream stu = this.getClass().getClassLoader().getResourceAsStream("assets/js/shutter.js");
        FileUtils.writeToFile(externalCacheDir + "/shutter.js", FileUtils.readFileByLines(stu), true);
        InputStream vel = this.getClass().getClassLoader().getResourceAsStream("assets/js/velocity.js");
        FileUtils.writeToFile(externalCacheDir + "/velocity.js", FileUtils.readFileByLines(vel), true);
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
