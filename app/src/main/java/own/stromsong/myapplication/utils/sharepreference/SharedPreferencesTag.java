package own.stromsong.myapplication.utils.sharepreference;

/**
 * 当前类注释:当前类用户SharedPreferences进行save的时候 配置key常量
 */
public class SharedPreferencesTag {
    //应用标识
    public static final String TAG = "dlf_store_";

    //用户登陆与否的缓存key
    public static final String LOGIN_BOOLEAN = TAG + "login";
    //搜索历史
    public static final String HISTORY_KEY = TAG + "search_history";
    //用户字段
    public static final String USER_PHONE = TAG + "phone";
    public static final String USER_NICKNAME = TAG + "nick_name";
    public static final String USER_TOKEN = TAG + "token";
    public static final String USER_SCORE = TAG + "score";
    public static final String USER_HEAD_IMG = TAG + "head_img";
    public static final String USER_ID = TAG + "user_id";
    public static final String USER_SIG = TAG + "sig";

    public static final String TECH_ID = TAG + "id";
    public static final String TECH_NO = TAG + "no";

    //微信支付发起位置(是否是提交订单开启的)
    public static final String isConfirmOrder = TAG + "is_confirm_order";

    public static final String baseURL = "http://120.24.234.123:8080";

}
