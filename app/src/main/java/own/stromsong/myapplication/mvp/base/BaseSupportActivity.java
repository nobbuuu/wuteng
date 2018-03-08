package own.stromsong.myapplication.mvp.base;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.weavey.loading.lib.LoadingLayout;
import com.youth.xframe.widget.XLoadingDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.app.MyApplication;
import own.stromsong.myapplication.utils.dialog.ApplicationSetting;
import own.stromsong.myapplication.utils.dialog.BtnClickListenerForDialog;
import own.stromsong.myapplication.utils.dialog.NormalDialog;
import own.stromsong.myapplication.utils.dialog.OnBtnClickL;
import own.stromsong.myapplication.utils.sharepreference.SharedPreferencesHelper;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public abstract class BaseSupportActivity extends AppCompatActivity {
    protected SharedPreferencesHelper mHelper;
    protected XLoadingDialog mLoading;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rightText)
    TextView mRightText;
    @BindView(R.id.refresh)
    SmartRefreshLayout mRefresh;
    @BindView(R.id.loadLayout)
    LoadingLayout mLoadLayout;
    @BindView(R.id.leftText)
    TextView mLeftText;
    @BindView(R.id.baselayout_rl)
    RelativeLayout mBaselayoutRl;
    @BindView(R.id.real_ll)
    LinearLayout mRealLl;
    private Unbinder bind;
    private LinearLayout mRootLl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        mHelper = SharedPreferencesHelper.getInstance(MyApplication.getInstance());
        View baseView = LayoutInflater.from(this).inflate(R.layout.base_activity, null);
        mRootLl = baseView.findViewById(R.id.root_ll);
        if (getLayoutId() > 0) {
            mRootLl.removeAllViews();
            mRootLl.addView(LayoutInflater.from(this).inflate(getLayoutId(), null));
        }
        setContentView(baseView);
        bind = ButterKnife.bind(this);
        setRefresh();
        initData();
    }

    protected abstract int getLayoutId();

    protected abstract void initData();

    /**
     * 默认不刷新
     */
    protected void setRefresh() {
        mRefresh.setEnableLoadmore(false);
        mRefresh.setEnableRefresh(false);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setRightText(String rightText) {
        mRightText.setText(rightText);
    }

    public void setLeftText(String leftText) {
        mLeftText.setText(leftText);
    }

    public void setLeftIcon(int resid) {
        mLeftText.setBackgroundResource(resid);
    }

    public void setBarVisible(boolean visible) {
        mBaselayoutRl.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.leftText)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leftText:
                finish();
                break;
        }
    }

    public void showLoading1() {
        mLoading = XLoadingDialog.with(this).setCanceled(false);
        mLoading.show();
    }

    public void goneLoading1() {
        if (mLoading != null) {
            mLoading.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

    public void showLoading() {
        mLoadLayout.setStatus(LoadingLayout.Loading);
    }

    public void showContent() {
        mLoadLayout.setStatus(LoadingLayout.Success);
    }

    public void showEmpty() {
        mLoadLayout.setStatus(LoadingLayout.Empty);
    }

    public void showError() {
        mLoadLayout.setStatus(LoadingLayout.Error);
    }

    public void showNoNetwork() {
        mLoadLayout.setStatus(LoadingLayout.No_Network);
    }

    /**
     * 弹出toast 显示时长short
     *
     * @param pMsg
     */
    protected void showToastMsgShort(String pMsg) {
        ToastUtils.showShort(pMsg);
    }

    /**
     * 自定义对话框
     *
     * @param content
     * @param leftTxt
     * @param rightTxt
     */
    protected void showTwoBtnDialog(String content, String leftTxt, String rightTxt, final BtnClickListenerForDialog btnClickListenerForDialog) {
        final NormalDialog dialog = new NormalDialog(this);
        dialog.isTitleShow(false)//
                .bgColor(Color.WHITE)//ContextCompat.getColor(this, R.color.white)
                .cornerRadius(5)//
                .content(content)//
                .contentTextSize(14f)
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.DKGRAY)//ContextCompat.getColor(this, R.color.color_616161)
                .dividerColor(Color.GRAY)//ContextCompat.getColor(this, R.color.color_f5f5f5)
                .btnTextSize(13f, 13f)//
                .btnTextColor(Color.DKGRAY, Color.DKGRAY)//
                .btnText(leftTxt, rightTxt)
                .btnPressColor(Color.GRAY)//ContextCompat.getColor(this, R.color.color_f5f5f5)
                .widthScale(0.8f)//
                .showAnim(ApplicationSetting.showAnim(0))//弹出出现动画效果可根据需求修改
                .dismissAnim(ApplicationSetting.dissmissAnim(2))//渐变消失动画效果
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        btnClickListenerForDialog.leftClickListener();
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        btnClickListenerForDialog.rightClickListener();
                        dialog.dismiss();
                    }
                });
    }
}
