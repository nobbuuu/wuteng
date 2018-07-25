package own.stromsong.myapplication.mvp.view.interfaces;

import java.util.List;

import own.stromsong.myapplication.mvp.base.BaseView;
import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean;
import own.stromsong.myapplication.mvp.model.WeatherBean;

/**
 * Created by Administrator on 2018/4/4 0004.
 */

public interface IVideo2Act extends BaseView {
    void getMenuList(List<ListResultBean> showMenu, List<MenuBean.SubtitlesBean> subtitles);//节目单 字幕
    void refreshCity(String cityName);
    void showWeather(String databean);
    void showCityCode(String xmlStr);
    void showImgHtml(String fileUrl,String xmlStr);
    void showSubtitles(List<MenuBean.SubtitlesBean> subtitles);
    void showMenuBean(MenuBean.ListResultBean menuBean);
    void processOff(String status);
    void screenshotsSuccess();
}
