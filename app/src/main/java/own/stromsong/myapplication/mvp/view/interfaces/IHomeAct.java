package own.stromsong.myapplication.mvp.view.interfaces;

import java.util.List;

import own.stromsong.myapplication.mvp.base.BaseView;
import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.mvp.model.WeatherBean;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public interface IHomeAct extends BaseView{
    void getWeather(WeatherBean.WeatherinfoBean weatherinfo);
    void getMenuList(List<MenuBean.ListResultBean> showMenu);//节目单
}
