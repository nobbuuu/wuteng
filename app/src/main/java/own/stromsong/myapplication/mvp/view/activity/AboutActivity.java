package own.stromsong.myapplication.mvp.view.activity;


import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ActivityUtils;

import butterknife.BindView;
import butterknife.OnClick;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.mvp.base.BaseSupportActivity;

public class AboutActivity extends BaseSupportActivity {

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

    @OnClick({ R.id.update_ll, R.id.clearCache_ll, R.id.exit,R.id.leftText})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update_ll:
                showToastMsgShort("click");
                break;
            case R.id.clearCache_ll:
                showToastMsgShort("click");
                break;
            case R.id.exit:
                ActivityUtils.finishToActivity(MainActivity.class, false);
                break;
            case R.id.leftText:
                finish();
                break;
        }
    }
}
