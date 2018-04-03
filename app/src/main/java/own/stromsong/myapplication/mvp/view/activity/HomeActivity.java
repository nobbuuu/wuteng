package own.stromsong.myapplication.mvp.view.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.mvp.base.MvpActivity;
import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.mvp.model.WeatherBean;
import own.stromsong.myapplication.mvp.presenter.HomePresenter;
import own.stromsong.myapplication.mvp.view.interfaces.IHomeAct;

public class HomeActivity extends MvpActivity<HomePresenter> implements IHomeAct {

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
    protected void onResume() {
        super.onResume();
        mvpPresenter.getWeather();
    }

    @Override
    protected void initData() {
        setBarVisible(false);
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

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    @OnClick({R.id.act_ll, R.id.about_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.act_ll:
                mvpPresenter.getList();
                break;
            case R.id.about_ll:
                ActivityUtils.startActivity(AboutActivity.class);
                break;
        }
    }

    @Override
    public void getWeather(WeatherBean.WeatherinfoBean weatherinfo) {
//        mWeather1.setText(weatherinfo.getTemp1());
//        mWeather2.setText(weatherinfo.getWeather());
    }

    @Override
    public void getMenuList(List<MenuBean.ListResultBean> showMenu,List<MenuBean.SubtitlesBean> subtitles) {
        Video2Activity.startVideo2Activity(this, showMenu,subtitles);
    }
}
