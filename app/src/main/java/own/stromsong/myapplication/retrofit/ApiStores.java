package own.stromsong.myapplication.retrofit;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import own.stromsong.myapplication.mvp.base.HttpResponse;
import own.stromsong.myapplication.mvp.model.LoginBean;
import own.stromsong.myapplication.mvp.model.Result;
import own.stromsong.myapplication.mvp.model.UserBean;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2017/8/16.
 * retrofit2访问的接口类
 */

public interface ApiStores {
    //baseUrl
    String API_SERVER_URL = "http://120.24.234.123:8080/sunnet_ad/app/";

    @FormUrlEncoded
    @POST("login")
//设备登录
    Observable<HttpResponse<LoginBean>> login(@Field("equId") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("logOut")
//退出登录
    Observable<HttpResponse<Result>> logOut(@Field("token") String token);

    @FormUrlEncoded
    @POST("showMenu")
//根据当前设备获取所有节目单
    Observable<HttpResponse<UserBean>> showMenu(@Field("token") String token, @Field("showMenuId") String password);

    @FormUrlEncoded
    @POST("show")
//根据节目单中得节目单ID获取节目单中所有的节目
    Observable<HttpResponse<UserBean>> show(@Field("token") String token, @Field("showMenuId") String password);

    @FormUrlEncoded
    @POST("playShow")
//查询节目素材内容
    Observable<HttpResponse<UserBean>> playShow(@Field("token") String token, @Field("showId") String showId);

    @FormUrlEncoded
    @POST("timing")
//查询设备的定时任务播放的节目
    Observable<HttpResponse<UserBean>> timing(@Field("token") String token, @Field("equId") String equId);

}