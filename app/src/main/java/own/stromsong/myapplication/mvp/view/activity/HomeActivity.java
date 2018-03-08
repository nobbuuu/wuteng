package own.stromsong.myapplication.mvp.view.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;

import butterknife.BindView;
import butterknife.OnClick;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.mvp.base.MvpActivity;
import own.stromsong.myapplication.mvp.presenter.LoginPresenter;

public class HomeActivity extends MvpActivity<LoginPresenter> {

    @BindView(R.id.weather_1)
    TextView mWeather1;
    @BindView(R.id.weather_2)
    TextView mWeather2;
    @BindView(R.id.line)
    View mLine;
    @BindView(R.id.weather_3)
    TextView mWeather3;
    @BindView(R.id.weather_4)
    ImageView mWeather4;
    @BindView(R.id.act_ll)
    LinearLayout mActLl;
    @BindView(R.id.about_ll)
    LinearLayout mAboutLl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {
        setBarVisible(false);
    }

    @Override
    protected LoginPresenter createPresenter() {
        return null;
    }
    @OnClick({R.id.act_ll, R.id.about_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.act_ll:
                ActivityUtils.startActivity(ActActivity.class);
                break;
            case R.id.about_ll:
                ActivityUtils.startActivity(AboutActivity.class);
                break;
        }
    }
}
