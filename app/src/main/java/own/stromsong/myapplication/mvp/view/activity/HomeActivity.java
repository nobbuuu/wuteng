package own.stromsong.myapplication.mvp.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.mvp.base.MvpActivity;
import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.mvp.model.WeatherBean;
import own.stromsong.myapplication.mvp.presenter.HomePresenter;
import own.stromsong.myapplication.mvp.presenter.LoginPresenter;
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
    private List<MenuBean.ListResultBean.ListshowBean.Material> list = new ArrayList<>();

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
    public void getMenuList(List<MenuBean.ListResultBean> showMenu) {
        list.clear();
        if (showMenu != null) {
            for (int i = 0; i < showMenu.size(); i++) {
                List<MenuBean.ListResultBean.ListshowBean> menu = showMenu.get(i).getListshow();
                if (menu != null) {
                    for (int i1 = 0; i1 < menu.size(); i1++) {//节目
                        list.addAll(menu.get(i1).getListMaterial());
                    }
                }
            }
        }
        Video2Activity.startVideo2Activity(this, list);
    }
}
