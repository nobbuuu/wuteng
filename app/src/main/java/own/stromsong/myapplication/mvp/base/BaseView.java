package own.stromsong.myapplication.mvp.base;

/**
 * Created by Administrator on 2017/8/16.
 * view父类activity的共有需求接口类，
 * 每个功能view继承该类减少接口的重复定义。
 */

public interface BaseView {

    //加载中
    void showLoading();

    //加载成功
    void showContent();

    //空数据页面
    void showEmpty();

    //加载失败
    void showError();

    //网络错误
    void showNoNetwork();
}
