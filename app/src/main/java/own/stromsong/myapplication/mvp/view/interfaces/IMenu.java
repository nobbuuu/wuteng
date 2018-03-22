package own.stromsong.myapplication.mvp.view.interfaces;

import java.util.List;

import own.stromsong.myapplication.mvp.base.BaseView;
import own.stromsong.myapplication.mvp.model.MenuActBean;
import own.stromsong.myapplication.mvp.model.MenuActUrlBean;
import own.stromsong.myapplication.mvp.model.MenuBean;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public interface IMenu extends BaseView{

    void getMenuList(List<MenuBean.ListResultBean> showMenu);//节目单
}
