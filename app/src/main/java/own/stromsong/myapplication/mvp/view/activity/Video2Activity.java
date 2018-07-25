package own.stromsong.myapplication.mvp.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.weavey.loading.lib.LoadingLayout.OnReloadListener;

import org.apache.http.util.EncodingUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import own.stromsong.myapplication.Alarm.MenuAlarmService;
import own.stromsong.myapplication.CustomView.MarqueeView;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.Task.DeleteTask;
import own.stromsong.myapplication.app.MyApplication;
import own.stromsong.myapplication.mvp.base.MvpActivity;
import own.stromsong.myapplication.mvp.model.CompleteBean;
import own.stromsong.myapplication.mvp.model.DownloadBean;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean.ListshowBean;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean.ListshowBean.Material;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean.ListshowBean.Material.MaterialBean;
import own.stromsong.myapplication.mvp.model.MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean;
import own.stromsong.myapplication.mvp.model.MenuBean.SubtitlesBean;
import own.stromsong.myapplication.mvp.model.NetworkBean;
import own.stromsong.myapplication.mvp.model.RefreshActList;
import own.stromsong.myapplication.mvp.model.ScreenBitmap;
import own.stromsong.myapplication.mvp.model.VoiceControl;
import own.stromsong.myapplication.mvp.model.WeatherBean;
import own.stromsong.myapplication.mvp.presenter.Video2Presenter;
import own.stromsong.myapplication.mvp.view.Service.DownLoadService;
import own.stromsong.myapplication.mvp.view.interfaces.IVideo2Act;
import own.stromsong.myapplication.other.Const;
import own.stromsong.myapplication.retrofit.ApiStores;
import own.stromsong.myapplication.utils.DownloadFilesUtil;
import own.stromsong.myapplication.utils.ImgDome;
import own.stromsong.myapplication.utils.NetUtil;
import own.stromsong.myapplication.utils.SpUtils;
import own.stromsong.myapplication.utils.StringUtil;
import own.stromsong.myapplication.utils.XmlUtils;
import own.stromsong.myapplication.utils.dialog.ProgressDialog;
import own.stromsong.myapplication.weight.DocViewUtils;
import own.stromsong.myapplication.weight.WeatherView;

public class Video2Activity extends MvpActivity<Video2Presenter> implements IVideo2Act {

    @BindView(R.id.root_rl)
    RelativeLayout mRootRl;
    @BindView(R.id.wait)
    TextView mWait;
    @BindView(R.id.root_ll_parent)
    RelativeLayout mRootLlParent;
    @BindView(R.id.topZw_lay)
    LinearLayout mTopZwLay;
    @BindView(R.id.botZw_lay)
    LinearLayout mBotZwLay;
    @BindView(R.id.no_network_img)
    ImageView mNoNetworkImg;

    private WeatherView mWeatherLay;
    private List<ListResultBean> list;
    private List<SubtitlesBean> subtitles;
    private MediaPlayer mediaPlayer;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(("yyyy-MM-dd-HH:mm:ss"));
    private Date mDate = new Date();
    private List<ListshowBean> showsList = new ArrayList<>();//节目集合
    private Map map = new HashMap<String, ListResultBean>();//节目单
    private Set<SubtitlesBean> zimuSet = new HashSet<>();//字幕集合
    private Map zimu = new HashMap<String, SubtitlesBean>();//字幕
    private boolean showLoading = true;//默认显示loading
    private Context mContext;

    private MarqueeView mTopMv;
    private MarqueeView mBottomMv;

    private ProgressDialog mProgressDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video2;
    }

    @Override
    public void getMenuList(List<ListResultBean> showMenu, List<SubtitlesBean> subtitle) {
        isReplay = false;
        list = showMenu;//节目单集合
        subtitles = subtitle;//字幕
        if (list != null && list.size() > 0) {
            map.clear();
            showsList.clear();
            Log.e("aa", "节目单listSize--->" + list.size());

            //按时间排序
            Collections.sort(list, new Comparator<ListResultBean>() {
                @Override
                public int compare(ListResultBean lhs, ListResultBean rhs) {
                    return (int) (lhs.getStartTime() - rhs.getStartTime());
                }
            });

            long millis = System.currentTimeMillis();
            for (int i = 0; i < list.size(); i++) {
                ListResultBean bean = list.get(i);
                if (bean != null) {
                    Log.e("aa", "节目listSize--->" + bean.getListshow().size());
                    String time = bean.getStartTime() + "," + bean.getEndTime();
                    mDate.setTime(bean.getStartTime());
                    String StartTime = simpleDateFormat.format(mDate);
                    Log.e("aa", "StartTime--->" + StartTime);
                    mDate.setTime(bean.getEndTime());
                    String EndTime = simpleDateFormat.format(mDate);
                    Log.e("aa", "EndTime--->" + EndTime);
                    map.put(time, bean);
                }
            }

            //遍历map集合
            Iterator it = map.entrySet().iterator();
            ListResultBean listResultBean = null;
            ListResultBean tempBean = null;
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                String key = entry.getKey().toString();
                listResultBean = (ListResultBean) entry.getValue();//一个节目单
                String[] split = key.split(",");
                if (millis >= Long.valueOf(split[0]) && millis <= Long.valueOf(split[1])) {
                    showsList.clear();
                    showsList.addAll(listResultBean.getListshow());//添加一个节目单的所有节目
                    //缓存节目单数据
                    SpUtils.savaUserInfo(Const.playList, new Gson().toJson(listResultBean));
                    //节目单ID
                    menuId = listResultBean.getShowMenu().getId();
                    map.remove(key);
                    setData();//开始播放节目
                    isPlay = true;
                    break;
                } else if (millis > Long.valueOf(split[0])) {
                    //最后一个开始播放时间已过的节目单
                    tempBean = (ListResultBean) entry.getValue();
                }
            }
            //当前时间段没有播放任务的节目单，就播放最后一个开始播放时间已过的节目单
            if (!isPlay && tempBean != null) {
                showsList.clear();
                showsList.addAll(tempBean.getListshow());//添加一个节目单的所有节目
                //缓存节目单数据
                SpUtils.savaUserInfo(Const.playList, new Gson().toJson(tempBean));
                menuId = listResultBean.getShowMenu().getId();
                setData();
                isPlay = true;
            }
        } else {//当天没有要播放的任务，就播放最近的任务
            String menuStr = (String) SpUtils.getParam(Const.playList, "");
            if (!menuStr.isEmpty()) {
                ListResultBean listResultBean = new Gson().fromJson(menuStr, ListResultBean.class);
                if (listResultBean != null) {
                    showsList.clear();
                    showsList.addAll(listResultBean.getListshow());//添加一个节目单的所有节目
                    menuId = listResultBean.getShowMenu().getId();
                    setData();
                }
            }
        }

        //字幕处理
        if (subtitles != null && subtitles.size() > 0) {
            refreshSubtitles(subtitles);
            refreshZimu();
        }

        Intent intent = new Intent(mContext, MenuAlarmService.class);
        intent.putExtra("menuMap", (Serializable) map);
        intent.putExtra("zimuMap", (Serializable) zimu);
        startService(intent);
    }

    private void refreshZimu() {
        Iterator it1 = zimu.entrySet().iterator();
        while (it1.hasNext()) {
            Entry entry = (Entry) it1.next();
            String key = entry.getKey().toString();
            SubtitlesBean mSubtitlesBean = (SubtitlesBean) entry.getValue();
            String[] split = key.split(",");
            long millis = System.currentTimeMillis();
            if (millis >= Long.valueOf(split[0]) && millis <= Long.valueOf(split[1])) {
                if (!zimuSet.contains(mSubtitlesBean)) {
                    zimuSet.add(mSubtitlesBean);//添加一个字幕
                    updateZimu = true;
                }
            } else {
                //移除不在当前时间段要播放的字幕
                if (zimuSet.contains(mSubtitlesBean)) {
                    updateZimu = true;
                    zimuSet.remove(mSubtitlesBean);//去掉一个字幕
                }
            }
        }
        if (updateZimu) {
            updateZimu = !updateZimu;
            setZimu();//开始展示字幕
        }
    }

    @Override
    public void refreshCity(String cityName) {
        if (StringUtil.NoNullOrEmpty(cityName)) {
            //获取天气数据
            mvpPresenter.getWeatherData(cityName);
        }
    }

    private void refreshSubtitles(List<SubtitlesBean> subtitles) {
        zimu.clear();
        zimuSet.clear();
        mTopMv.removeViews();
        mBottomMv.removeViews();
        if (subtitles == null) {
            return;
        }
        SpUtils.savaUserInfo(Const.subTitles, new Gson().toJson(subtitles));
        Log.e("aa", "字幕listSize--->" + subtitles.size());
        //按开始时间大小升序排序
        Collections.sort(subtitles, new Comparator<SubtitlesBean>() {
            @Override
            public int compare(SubtitlesBean lhs, SubtitlesBean rhs) {
                return (int) (lhs.getStartDate() - rhs.getStartDate());
            }
        });

        for (int i = 0; i < subtitles.size(); i++) {
            SubtitlesBean bean = subtitles.get(i);
            String time = bean.getStartDate() + "," + bean.getEndDate();

            mDate.setTime(bean.getStartDate());
            String StartTime = simpleDateFormat.format(mDate);
            Log.e("aa", "StartDate--->" + StartTime);

            mDate.setTime(bean.getEndDate());
            String EndTime = simpleDateFormat.format(mDate);
            Log.e("aa", "EndDate--->" + EndTime);

            zimu.put(time, bean);
        }
    }

    @Override
    public void showWeather(String databean) {
        WeatherBean weatherBean = new Gson().fromJson(databean, WeatherBean.class);
        if (weatherBean != null) {
            List<WeatherBean.QueryBean.ResultsBean.ChannelBean.ItemBean.ForecastBean> forecast = weatherBean.getQuery().getResults().getChannel().getItem().getForecast();
            if (forecast != null && forecast.size() > 0) {
                WeatherBean.QueryBean.ResultsBean.ChannelBean.ItemBean.ForecastBean forecastBean = forecast.get(0);
                mWeatherLay = new WeatherView(mContext);
                mWeatherLay.setData(forecastBean);
                setWeatherLocation();
            }
        }
    }

    @Override
    public void showCityCode(String xmlStr) {
        if (StringUtil.NoNullOrEmpty(xmlStr)) {
            String woeid = XmlUtils.getWoeid(xmlStr);
            if (StringUtil.NoNullOrEmpty(woeid)) {
            }
        }
    }

    @Override
    public void showImgHtml(String fileUrl, String xmlStr) {
        int wide = 0;
        int hide = 0;
        int x = 0;
        int y = 0;
        Log.e("tag", "images=" + images.size());
        for (int j = 0; j < images.size(); j++) {
            ShowsMaterialBean showsMaterialBean = images.get(j);
            if (fileUrl.equals(showsMaterialBean.getMaterial().getFile())) {
                wide = showsMaterialBean.getWide();
                hide = showsMaterialBean.getHige();
                x = Integer.getInteger(showsMaterialBean.getX());
                y = Integer.valueOf(showsMaterialBean.getY());
                break;
            }
        }
        if (haveNet) {
            mRootRl.addView(getWebView(xmlStr, wide, hide, x, y));
        }
    }

    @Override
    public void showSubtitles(List<SubtitlesBean> subtitles) {
        refreshSubtitles(subtitles);
        refreshZimu();
    }

    /*
    * 播放即时发布的节目单
    * */
    @Override
    public void showMenuBean(ListResultBean menuBean) {
        mRootRl.removeAllViews();
        isReplay = false;
        menuId = menuBean.getShowMenu().getId();
        showsList.clear();
        showsList.addAll(menuBean.getListshow());
        SpUtils.savaUserInfo(Const.playList, new Gson().toJson(menuBean));
        setData();
    }

    @Override
    public void processOff(String status) {
        Log.e("tag", "processOff>>>" + status);
        mLoading.dismiss();
        if (status.equals("2")) {
            System.gc();
            Process.killProcess(Process.myPid());
            finish();
        }
        if (status.equals("turnoff")) {
            mContext.sendBroadcast(new Intent("ads.android.setpoweroff.action"));
        }
    }

    @Override
    public void screenshotsSuccess() {
        Log.e("tag", "action>>>>截屏成功");
        mLoading.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshActList mRefreshActList) {
        Log.e("aa", "更新数据");
        String eventStr = mRefreshActList.getEventStr();
        showLoading = true;
        isPlay = false;
        i = 0;
        mHandler.removeMessages(1);
        if (eventStr != null && eventStr.equals("refreshSingleMenu")) {
            //即时发布节目单
            mvpPresenter.getimingShowMenu(mRefreshActList.getMenuId());
        } else {
            mvpPresenter.getList();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(String eventStr) {
        if (eventStr.equals("zimuRefresh")) {
            mvpPresenter.getSubtitles();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTurnOff(String eventStr) {
        if (eventStr.equals("turnOff")) {
            refreshDevice("2", "turnoff");
        }
    }

    private boolean haveNet = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetwork(NetworkBean networkBean) {
        if (networkBean.getNetworkStatus().equals("haveNet")) {
            haveNet = true;
        } else {
            haveNet = false;
        }
        if (haveNet) {
            mNoNetworkImg.setVisibility(View.GONE);
        } else {
            mNoNetworkImg.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScreenBitmap(ScreenBitmap mScreenBitmap) {
        Log.e("aa", "开始截屏操作");
        showLoading = false;
        captureScreen(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVoiceControl(VoiceControl mVoiceControl) {
        showLoading = false;
        if (TextUtils.isEmpty(mVoiceControl.getVolume())) return;
        setVoice(Integer.valueOf(mVoiceControl.getVolume()));
        Log.e("aa", "音量控制");
    }


  /*  //展示PDF
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showPDF(CompleteBean completeBean) {
        showLoading = false;
        String filePath = completeBean.getPath();
        Bundle bundle = completeBean.getBundle();
        if (bundle != null) {
            int wide = bundle.getInt("wide");s
            int height = bundle.getInt("height");
            int x = bundle.getInt("x");
            int y = bundle.getInt("y");
            getPDFViewByRender(filePath, wide, height, x, y);
        }
    }*/

    //播放新的节目单
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void playNewMenu(ListResultBean menuBean) {
        showLoading = false;
        i = 0;
        showsList.clear();
        showsList.addAll(menuBean.getListshow());
        setData();
    }

    //播放新的字幕
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void playNewZimu(SubtitlesBean mBean) {
        zimuSet.add(mBean);//添加一个字幕
        setZimu();
    }

    private String apkUrl = "", version = "";

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mainEvent3(DownloadBean eventStr) {
        apkUrl = eventStr.getApkUrl();
        version = eventStr.getVersion();
        if (eventStr.getEventStr().equals("downloadApk")) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                startService_updateApk(eventStr.getApkUrl(), eventStr.getVersion());
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 666);
            }
        }
    }

    private void startService_updateApk(String apkUrl, String version) {
        Intent intent = new Intent(mContext, DownLoadService.class);
        intent.setAction("downLoadApk");
        intent.putExtra("apkUrl", apkUrl);
        intent.putExtra("versionName", version);
        startService(intent);
    }

    @Override
    protected void initData() {
        mContext = this;
        mProgressDialog = new ProgressDialog(this) {
            @Override
            public void onDownover() {
                playMenu();
            }
        };
        refreshDevice("1", "1");
        haveNet = NetUtil.haveNet();
        setBarVisible(false);
        EventBus.getDefault().register(this);
//        basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator;
        initZimuView();
        creatFile();//本地文件读写权限的授予及文件夹的创建；
        mvpPresenter.getList();
        getLoadLayout().setOnReloadListener(new OnReloadListener() {
            @Override
            public void onReload(View v) {
                mvpPresenter.getList();
            }
        });

//        getLocation();
        //没有网络的时候就播放已缓存的节目单
        if (!haveNet) {
            //获取上次播放的节目单数据
            String playList = (String) SpUtils.getParam(Const.playList, "");
            String subTitles = (String) SpUtils.getParam(Const.subTitles, "");
            if (StringUtil.NoNullOrEmpty(playList)) {
                ListResultBean dataBean = new Gson().fromJson(playList, ListResultBean.class);
                if (dataBean != null) {
                    menuId = dataBean.getShowMenu().getId();
                    showsList.addAll(dataBean.getListshow());
                    setData();
                }
            }
            if (StringUtil.NoNullOrEmpty(subTitles)) {
                Type listType = new TypeToken<LinkedList<SubtitlesBean>>() {
                }.getType();
                LinkedList<SubtitlesBean> sublist = new Gson().fromJson(subTitles, listType);
                if (sublist != null && sublist.size() > 0) {
                    refreshGGWord(sublist);
                }
            }
        }
    }

    private void initZimuView() {
        mTopMv = new MarqueeView(mContext);
        mBottomMv = new MarqueeView(mContext);
        mTopMv.setScrollDirection(MarqueeView.RIGHT_TO_LEFT);
        mBottomMv.setScrollDirection(MarqueeView.RIGHT_TO_LEFT);
    }

    private void refreshGGWord(LinkedList<SubtitlesBean> sublist) {
        refreshSubtitles(sublist);
        refreshZimu();
    }

    private boolean updateZimu;
    private boolean isPlay = false;
    //天气展示的位置
    private String weatherLocation = "1";

    //节目单ID
    private String menuId = "";
    //节目Id
    private String mId = "";

    /**
     * 播放一个节目 handler 播放该节目所有素材
     */
    private int i = 0;
    private boolean isRe = true;
    private boolean isReplay = false;//节目单是否循环播放

    /*
    * 播放节目单
    * */
    private void setData() {
        if (StringUtil.NoNullOrEmpty(menuId)) {
            String oldMenuId = (String) SpUtils.getParam(Const.menuId, "");
            if (!menuId.equals(oldMenuId)) {
                new DeleteTask(menuId).execute();//删除之前缓存的节目单中的所有素材

                //清除上一个节目单的所有缓存的view
                imgViewsMap.clear();
                pdfViewsMap.clear();
                videoViewsMap.clear();
                isReplay = false;
            }
            downLoadFiles();
        }
    }

    private void playMenu() {
        Log.e("tag", "action------>rePlaymenu");
        ListshowBean bean = null;
        try {
            mWait.setVisibility(View.GONE);//有节目gone掉等待view
            if (!isReplay && mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            Log.e("shows", "i=" + i);
            bean = showsList.get(i);//播放第一条,如果没节目会走异常
            mId = bean.getShows().getId();
            playShow(bean.getListMaterial(), mId);
            mRootRl.postInvalidate();
            i++;
            weatherLocation = bean.getShows().getType();
        } catch (Exception e) {
//            showToastMsgShort("没有新节目了");//没有节目 上个节目会重复播放
        } finally {
            if (bean != null) {
                long playTime = bean.getShows().getPlayTime();
                Log.e("tag", "playTime=" + playTime);
                if (playTime > 0) {
                    mHandler.sendEmptyMessageDelayed(1, playTime * 1000);
                }
            } else if (isRe) {//isRe,避免递归造成的栈溢出
                //循环播放节目单
                i = 0;
                isReplay = true;
                playMenu();
            }
        }
    }

    /*
    * 下载整个节目单
    * */
    private void downLoadFiles() {
        String menuStr = (String) SpUtils.getParam(Const.playList, "");
        if (StringUtil.NoNullOrEmpty(menuStr)) {
            ListResultBean menuBean = new Gson().fromJson(menuStr, ListResultBean.class);
            if (menuBean != null) {
                List<ListshowBean> listshow = menuBean.getListshow();
                if (listshow != null && listshow.size() > 0) {
                    List<String> listUrl = new ArrayList<>();
                    List<String> pathList = new ArrayList<>();
                    for (ListshowBean listshowBean : listshow) {
                        List<Material> listMaterial = listshowBean.getListMaterial();
                        if (listMaterial != null) {
                            for (Material material : listMaterial) {
                                ShowsMaterialBean showsMaterial = material.getShowsMaterial();
                                MaterialBean material1 = material.getMaterial();
                                int type = material1.getType();
                                String url = material1.getLink();
                                String link = material1.getFile();
                                if (!StringUtil.NoNullOrEmpty(url)) {
                                    url = link;
                                }
                                String fileType = "";
                                switch (type) {//1.图片 2.音频 3.网络视频 4.本地视频 5.HTML 6.PPT 7.Word 8.Excel 9.PDF 10.背景
                                    case 1:
                                    case 10:
                                        fileType = "images";
                                        break;
                                    case 2:
                                        fileType = "music";
                                        break;
                                    case 3:
                                    case 4:
                                        fileType = "video";
                                        break;
                                    case 5:
                                        fileType = "html";
                                        break;
                                    case 6:
                                    case 7:
                                    case 8:
                                    case 9:
                                        fileType = "pdf";
                                        break;
                                }
                                if (!fileType.isEmpty()) {
                                    String downPath = MyApplication.getInstance().getExternalCacheDir() + File.separator + fileType + File.separator + Math.abs(url.hashCode()) + "." + DocViewUtils.getInstance().getFileType(url);
                                    if (!new File(downPath).exists()) {
                                        pathList.add(downPath);
                                        listUrl.add(url);
                                    }
                                }
                            }
                        }
                    }
                    if (listUrl.size() > 0) {
                        new DownloadFilesUtil() {
                            @Override
                            public void onProgress(int progress) {
                                mProgressDialog.uploadDialog(progress);
                            }
                        }.downloadFiles(listUrl, pathList);
                    } else {
                        playMenu();
                        Log.e("tag", "action--->playMenu");
                    }
                }
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            playMenu();
        }
    };

    private void setZimu() {
        if (zimuSet.size() > 0) {
            for (SubtitlesBean subtitle : zimuSet) {
                if (!subtitle.getStatus().equalsIgnoreCase("1")) return;
                getZimuLayout(subtitle);
            }
        }
        setWeatherLocation();
    }

    private void setWeatherLocation() {
        mTopZwLay.removeAllViews();
        mBotZwLay.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        Log.e("tag", "weatherLocation=" + weatherLocation);
        switch (weatherLocation) {
            case "1"://右上
                if (mWeatherLay != null) {
                    mTopMv.setLayoutParams(params);
                } else {
                    mTopZwLay.setGravity(Gravity.CENTER_HORIZONTAL);
                }
                mTopZwLay.addView(mTopMv);
                if (mWeatherLay != null) {
                    mTopZwLay.addView(mWeatherLay);
                }
                mBotZwLay.setGravity(Gravity.CENTER_HORIZONTAL);
                mBotZwLay.addView(mBottomMv);
                break;
            case "2"://右下
                if (mWeatherLay != null) {
                    mBottomMv.setLayoutParams(params);
                } else {
                    mBotZwLay.setGravity(Gravity.CENTER_HORIZONTAL);
                }
                mBotZwLay.addView(mBottomMv);
                if (mWeatherLay != null) {
                    mBotZwLay.addView(mWeatherLay);
                }
                mTopZwLay.setGravity(Gravity.CENTER_HORIZONTAL);
                mTopZwLay.addView(mTopMv);
                break;
            case "3"://左上
                if (mWeatherLay != null) {
                    mTopZwLay.addView(mWeatherLay);
                    mTopMv.setLayoutParams(params);
                } else {
                    mTopZwLay.setGravity(Gravity.CENTER_HORIZONTAL);
                }
                mTopZwLay.addView(mTopMv);

                mBotZwLay.setGravity(Gravity.CENTER_HORIZONTAL);
                mBotZwLay.addView(mBottomMv);
                break;
            case "4"://左下
                if (mWeatherLay != null) {
                    mBotZwLay.addView(mWeatherLay);
                    mBottomMv.setLayoutParams(params);
                } else {
                    mBotZwLay.setGravity(Gravity.CENTER_HORIZONTAL);
                }
                mBotZwLay.addView(mBottomMv);
                mTopZwLay.setGravity(Gravity.CENTER_HORIZONTAL);
                mTopZwLay.addView(mTopMv);
                break;
            case "-1"://隐藏
                mTopZwLay.setGravity(Gravity.CENTER_HORIZONTAL);
                mBotZwLay.setGravity(Gravity.CENTER_HORIZONTAL);
                mTopZwLay.addView(mTopMv);
                mBotZwLay.addView(mBottomMv);

                break;
        }

        int viewWidth = mTopMv.getViewWidth();
        int viewWidth_bot = mBottomMv.getViewWidth();
        int width1 = mTopMv.getWidth();
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        if (mWeatherLay != null) {
            int width = mWeatherLay.getWidth();
            viewWidth += width;
            viewWidth_bot += width;
        }
        if (viewWidth > widthPixels) {
            mTopMv.startScroll();
        }
        if (viewWidth_bot > widthPixels) {
            mBottomMv.startScroll();
        }
    }

    /**
     * 字幕的View
     *
     * @param subtitle
     * @return
     */
    private void getZimuLayout(SubtitlesBean subtitle) {
        final TextView mTopTv = new TextView(mContext);
        final TextView mBottomTv = new TextView(mContext);
        Log.e("tag", "zimuLocation=" + subtitle.getLocation());
        switch (subtitle.getLocation()) {
            case 0://top
                switch (subtitle.getType()) {
                    case 1://Rss
                        WebView mWebview = new WebView(mContext);
                        setWebViewSettings(mWebview, null);
                        LayoutParams params = (LayoutParams) mTopTv.getLayoutParams();
                        params.height = DensityUtil.dp2px(100);
                        mWebview.setLayoutParams(params);
                        mWebview.loadUrl(subtitle.getRss());
                        mTopMv.addViewInQueue(mWebview);
                        break;
                    case 2://图片
                        String file = subtitle.getFile();
                        Glide.with(this).load(file).into(new ViewTarget<TextView, GlideDrawable>(mTopTv) {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation anim) {
                                ImageView imageView = new ImageView(mContext);
                                LayoutParams params = (LayoutParams) mTopTv.getLayoutParams();
                                params.height = DensityUtil.dp2px(100);
                                imageView.setBackground(resource);
                                imageView.setLayoutParams(params);
                                mTopMv.addViewInQueue(imageView);
                            }
                        });
                        break;
                    case 3://文字
                        mTopTv.setText(subtitle.getMsgContext());
                        mTopTv.setTextColor(Color.parseColor(subtitle.getMsgColor()));
                        mTopTv.setBackgroundColor(Color.parseColor(subtitle.getBgColor()));
                        if (StringUtil.NoNullOrEmpty(subtitle.getFontSize())) {
                            mTopTv.setTextSize(Integer.valueOf(subtitle.getFontSize()));
                        }
                        mTopMv.addViewInQueue(mTopTv);

                        break;
                }
                break;
            case 1://bottom
                switch (subtitle.getType()) {
                    case 1://Rss
                        WebView mWebview = new WebView(mContext);
                        setWebViewSettings(mWebview, null);
                        LayoutParams params = (LayoutParams) mTopTv.getLayoutParams();
                        params.height = DensityUtil.dp2px(100);
                        mWebview.setLayoutParams(params);
                        mWebview.loadUrl(subtitle.getRss());
                        mBottomMv.addViewInQueue(mWebview);
                        break;
                    case 2://图片
                        String file = subtitle.getFile();
                        Glide.with(this).load(file).into(new ViewTarget<TextView, GlideDrawable>(mBottomTv) {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation anim) {
                                ImageView imageView = new ImageView(mContext);
                                LayoutParams params = (LayoutParams) mTopTv.getLayoutParams();
                                params.height = DensityUtil.dp2px(100);
                                imageView.setBackground(resource);
                                imageView.setLayoutParams(params);
                                mBottomMv.addViewInQueue(imageView);
                            }
                        });
                        break;
                    case 3://文字
                        mBottomTv.setTextColor(Color.parseColor(subtitle.getMsgColor()));
                        mBottomTv.setBackgroundColor(Color.parseColor(subtitle.getBgColor()));
                        if (StringUtil.NoNullOrEmpty(subtitle.getFontSize())) {
                            mBottomTv.setTextSize(Integer.valueOf(subtitle.getFontSize()));
                        }
                        mBottomTv.setText(subtitle.getMsgContext());
                        mBottomMv.addViewInQueue(mBottomTv);
                        break;
                }
                break;
        }
    }


    /**
     * 播放节目
     *
     * @param materialList
     */
    private List<ShowsMaterialBean> images = new ArrayList<>();
    private String url = "";
    private Map<String, List<View>> imgViewsMap = new HashMap<>();//图集缓存集合
    private Map<String, PDFView> pdfViewsMap = new HashMap<>();//pdf缓存集合
    private Map<String, View> videoViewsMap = new HashMap<>();//视频缓存集合

    private void playShow(List<Material> materialList, String mId) {
        mRootRl.removeAllViews();
//        mediaParamMap.clear();
        if (materialList != null && materialList.size() != 0) {
            final List<String> pictures = new ArrayList<>();
            for (Material material : materialList) {
                MaterialBean materialBean = material.getMaterial();
                if (materialBean != null) {
                    if (materialBean.getType() == 1 && material.getShowsMaterial().getShowType() != -2) {
                        pictures.add(materialBean.getFile());
                    }
                }
            }

            images.clear();
            List<String> urlList = new ArrayList<>();
            List<String> pathList = new ArrayList<>();
            for (int i = 0; i < materialList.size(); i++) {
                //1.图片 2.音频 3.网络视频 4.本地视频 5.HTML 6.PPT 7.Word 8.Excel 9.PDF 10.背景
                ShowsMaterialBean showsMaterialBean = materialList.get(i).getShowsMaterial();
                MaterialBean materialBean = materialList.get(i).getMaterial();
                if (materialBean != null && showsMaterialBean != null) {
                    int wide = showsMaterialBean.getWide();
                    int hige = showsMaterialBean.getHige();
                    int x = Integer.valueOf(showsMaterialBean.getX());
                    int y = Integer.valueOf(showsMaterialBean.getY());
                    url = materialBean.getLink();
                    String link = materialBean.getFile();
                    if (!StringUtil.NoNullOrEmpty(url)) {
                        url = link;
                    }
                    int type = materialBean.getType();
                    String remarks = showsMaterialBean.getRemarks();
                    int playTime = showsMaterialBean.getPlayTime();
                    switch (type) {
                        case 1:
                            if (StringUtil.NoNullOrEmpty(remarks) && showsMaterialBean.getShowType() != -2) {//图集
                                images.add(showsMaterialBean);
                            } else {
                                ImageView imageView = getImage(url, wide, hige, x, y);
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @SuppressLint("NewApi")
                                    @Override
                                    public void onClick(View v) {
                                        int position = 0;
                                        for (int j = 0; j < pictures.size(); j++) {
                                            if (pictures.get(j).equals(url)) {
                                                position = j;
                                                break;
                                            }
                                        }
                                        if (pictures != null && pictures.size() > 0) {
                                            new PhotoPagerConfig.Builder(Video2Activity.this)
                                                    .setBigImageUrls((ArrayList<String>) pictures)       //大图片url,可以是sd卡res，asset，网络图片.
                                                    .setSavaImage(false)                        //开启保存图片，默认false
                                                    .setPosition(position)                     //默认展示第2张图片
                                                    .setSaveImageLocalPath("pictures")        //这里是你想保存大图片到手机的地址,可在手机图库看到，不传会有默认地址
                                                    .setOpenDownAnimate(true)                 //是否开启下滑关闭activity，默认开启。类似微信的图片浏览，可下滑关闭一样
                                                    .build();
                                        }
                                    }
                                });
                                mRootRl.addView(imageView);
                            }
                            break;
                        case 2:
                            playSound(this, url);
                            break;
                        case 3:
                        case 4:
                            mRootRl. addView(getMPlayer(url, wide, hige, x, y, mId));
//                            mRootRl.addView(getVitamioPlayer(url, wide, hige, x, y, mId));
                            break;

                        case 5://html
                            if (haveNet) {
                                mRootRl.addView(getWebView(url, wide, hige, x, y));
                            }
                            break;
                        case 6://PPT
                        case 7://Word
                        case 8://Excel
                        case 9://PDF
                            String dir = MyApplication.getInstance().getExternalCacheDir() + File.separator + Const.PDF + File.separator + Math.abs(url.hashCode()) + "." + DocViewUtils.getInstance().getFileType(url);
                            if (new File(dir).exists()) {
                                getPDFViewByRender(dir, wide, hige, x, y, playTime);
                            }
                            break;
                        case 10:
                            String downPath = MyApplication.getInstance().getExternalCacheDir() + File.separator + Const.IMAGES + File.separator + Math.abs(url.hashCode()) + "." + DocViewUtils.getInstance().getFileType(url);
                            if (new File(downPath).exists()) {
                                Log.e("tag", "展示本地背景");
                                Drawable drawable = Drawable.createFromPath(downPath);
                                if (drawable != null) {
                                    mRootRl.setBackground(drawable);
                                }
                            } else {
                                Log.e("tag", "展示网络背景");
                                Glide.with(this).load(url).into(new ViewTarget<RelativeLayout, GlideDrawable>(mRootRl) {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource, GlideAnimation anim) {
                                        mRootRl.setBackground(resource);
                                    }
                                });
                            }
                            break;
                    }
                }
            }

            /*
            * 图集处理
            * */
            boolean isExit = true;
            //下载图集到images文件夹下
            for (int j = 0; j < images.size(); j++) {
                String file = images.get(j).getMaterial().getFile();
                String downPath = MyApplication.getInstance().getExternalCacheDir() + File.separator + Const.IMAGES + File.separator + Math.abs(file.hashCode()) + "." + DocViewUtils.getInstance().getFileType(file);
                if (!new File(downPath).exists()) {
                    isExit = false;
                }
            }

            //加载图集
            if (images.size() > 0) {
                Log.e("tag", "isExit=" + isExit);
                if (isExit) {
                    //无网络的时候展示图集
                    List<Map<String, Object>> mapList = StringUtil.getMapList(images);
                    List<ImgDome> list = StringUtil.getImgDome(mapList);
                    Log.e("tag", "isReplay=" + isReplay);
                    if (!isReplay) {
                        List<View> imgViews = new ArrayList<>();
                        for (ImgDome im : list) {
                            int wide = im.getDomeValu().getW();
                            int hige = im.getDomeValu().getH();
                            int x = im.getDomeValu().getX();
                            int y = im.getDomeValu().getY();
                            View bdWebView = getBdWebView(im.getUrl(), wide, hige, x, y);
                            imgViews.add(bdWebView);
                            mRootRl.addView(bdWebView);
                        }
                        if (imgViews.size() > 0) {
                            imgViewsMap.put(mId, imgViews);
                        }
                    } else {

                        Log.e("tag", "imgViewsMap.size()=" + imgViewsMap.size());
                        for (Entry<String, List<View>> mImgViews : imgViewsMap.entrySet()) {
                            String key = mImgViews.getKey();
                            if (key.equals(mId)) {
                                List<View> value = mImgViews.getValue();
                                for (View mView : value) {
                                    mRootRl.addView(mView);
                                }
                                break;
                            }
                        }

                    }


                } else {
                    List<Map<String, Object>> mapList = StringUtil.getMapList(images);
                    for (Map<String, Object> mList : mapList) {
                        List<ShowsMaterialBean> value = (List<ShowsMaterialBean>) mList.get("list");
                        if (value != null && value.size() > 0) {
                            ShowsMaterialBean showsMaterialBean = value.get(0);
                            String listStr = StringUtil.listParseStrings(value);
                            String webUrl = ApiStores.API_SERVER_URL + "getShowHtml";
                            int times = 3;
                            if (showsMaterialBean.getPlayTime() > 0) {
                                times = showsMaterialBean.getPlayTime();
                            }
                            Log.e("cnm", "times=" + times);
                            String param = "imgPaths=" + listStr + "&type=" + showsMaterialBean.getShowType() + "&times=" + String.valueOf(times * 1000);
                            mRootRl.addView(getPostWebView(webUrl, param, showsMaterialBean.getWide(), showsMaterialBean.getHige(), Integer.valueOf(showsMaterialBean.getX()), Integer.valueOf(showsMaterialBean.getY())));
                        }
                    }
                }
            }
        }
    }


    private void creatFile() {
        creatFilePermissionResult();
    }

    private void creatFilePermissionResult() {
        String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "wuteng";
        File file = new File(downloadPath);
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        haveNet = NetUtil.haveNet();
        if (haveNet) {
            mNoNetworkImg.setVisibility(View.GONE);
        } else {
            mNoNetworkImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
//            mediaPlayer.release();
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("tag", "onRestart");
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    protected Video2Presenter createPresenter() {
        return new Video2Presenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlaybackVideo();
        if (mDate != null) {
            mDate = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isRe = false;
        EventBus.getDefault().unregister(this);
    }

    private void refreshDevice(String status, String action) {
        if (StringUtil.NoNullOrEmpty(status)) {
            Map<String, String> map = new HashMap<>();
            map.put("runState", status);//设备离线
            mvpPresenter.refreshDevice(map, action);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (NetUtil.haveNet()) {
                refreshDevice("2", "2");
            } else {
                finish();
                System.gc();
                Process.killProcess(Process.myPid());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void stopPlaybackVideo() {
        try {
            for (int i = 0; i < mRootRl.getChildCount(); i++) {
                View childAt = mRootRl.getChildAt(i);
                if (childAt instanceof WebView) {
                    WebView webView = (WebView) childAt;
                    webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                    webView.clearHistory();
                    ((ViewGroup) webView.getParent()).removeView(webView);
                    webView.destroy();
                    webView = null;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 视频播放控件（surfaceview+mediaplayer）
     */
    private View getMPlayer(String url, int width, int height, int x, int y, String showsId) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_surfaceview, null);
        SurfaceView mPlayerView = view.findViewById(R.id.mPlayerView);
        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        view.setLayoutParams(layoutParams);
        mPlayerView.setKeepScreenOn(true);
        SurfaceHolder surfaceHolder = mPlayerView.getHolder();
        MediaPlayer player = new MediaPlayer();
        String downPath = MyApplication.getInstance().getExternalCacheDir() + File.separator + "video" + File.separator + Math.abs(url.hashCode()) + "." + DocViewUtils.getInstance().getFileType(url);
        if (new File(downPath).exists()) {
            url = downPath;
        }
        String finalUrl = url;
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    player.reset();
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    player.setDataSource(finalUrl);
                    player.setDisplay(holder);//视频显示在SurfaceView上
//                    player.setLooping(true);
                    player.prepare();
                    player.start();
                } catch (IllegalArgumentException e) {
                } catch (SecurityException e) {
                } catch (IllegalStateException e) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (player != null) {
                    try {
                        player.stop();
                        //todo 不释放掉容易导致卡机，严重内存泄漏
                        player.release();
                    } catch (IllegalStateException e) {
                        Log.e("tag", "e=" + e.toString());
                    }
                }
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    player.reset();
                    player.setDataSource(finalUrl);
                    player.setDisplay(surfaceHolder);//视频显示在SurfaceView上
                    player.prepare();
                    player.start();
                } catch (IllegalArgumentException e) {
                } catch (SecurityException e) {
                } catch (IllegalStateException e) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //key由当前节目id和所处位置决定
//        videoViewsMap.put(showsId+String.valueOf(x)+String.valueOf(y),view);
        return view;
    }


    /**
     * 播放音频的方法
     */
    private void playSound(Context context, String path) {
        if (null == mediaPlayer) {
            mediaPlayer = new MediaPlayer();
        }
        try {
            //播放之前要先把音频文件重置
            mediaPlayer.reset();
            //调用方法传进去要播放的音频路径
            String downPath = MyApplication.getInstance().getExternalCacheDir() + File.separator + Const.MUSIC + File.separator + Math.abs(url.hashCode()) + "." + DocViewUtils.getInstance().getFileType(url);
            if (new File(downPath).exists()) {//本地播放
                mediaPlayer.setDataSource(context, Uri.parse(downPath));
                Log.e("tag", "播放本地music");
            } else if (haveNet) {
                mediaPlayer.setDataSource(context, Uri.parse(path));
                Log.e("tag", "播放网络music");
            }
            //异步准备音频资源
            mediaPlayer.prepareAsync();
            //调用mediaPlayer的监听方法，音频准备完毕会响应此方法
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();//开始音频
                }
            });

           /* mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.e("tag","onCompletion");
                    mp.setLooping(true);
                    mp.start();
                }
            });*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开Html文件
     *
     * @param url
     * @param width
     * @param height
     * @param x
     * @param y
     * @return
     */
    private View getWebView(String url, int width, int height, int x, int y) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.webview, null);
        WebView webView = (WebView) inflate.findViewById(R.id.webview);
        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        inflate.setLayoutParams(layoutParams);
        WebSettings webSettings = webView.getSettings();
        //其他细节操作
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
// 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setDomStorageEnabled(true);
        Log.e("tag", "url=" + url);
        webView.clearCache(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //防止链接再浏览器打开
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
        return inflate;
    }

    /**
     * loadDataWithBaseURL方式打开Html文件
     *
     * @param url
     * @param width
     * @param height
     * @param x
     * @param y
     * @return
     */
    private View getDataWebView(String url, int width, int height, int x, int y) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.webview, null);
        WebView webView = (WebView) inflate.findViewById(R.id.webview);
        final ProgressBar progressBar = (ProgressBar) inflate.findViewById(R.id.progress);
        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        inflate.setLayoutParams(layoutParams);
        setWebViewSettings(webView, progressBar);
        WebSettings webSettings = webView.getSettings();
        //其他细节操作
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
// 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        Log.e("tag", "url=" + url);
//        webView.loadData(url, "text/html;charset=utf-8", "utf-8");
        //  加载、并显示HTML代码
        webView.clearCache(true);
        webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
        return inflate;
    }

    /**
     * post方式打开Html文件
     *
     * @param url
     * @param width
     * @param height
     * @param x
     * @param y
     * @return
     */
    private View getPostWebView(String url, String param, int width, int height, int x, int y) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.webview, null);
        WebView webView = (WebView) inflate.findViewById(R.id.webview);
        final ProgressBar progressBar = (ProgressBar) inflate.findViewById(R.id.progress);
        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        inflate.setLayoutParams(layoutParams);
        WebSettings webSettings = webView.getSettings();
        //其他细节操作
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
// 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        setWebViewSettings(webView, progressBar);
        Log.e("tag", "url=" + url);
        webView.clearCache(true);
        webView.postUrl(url, EncodingUtils.getBytes(param, "BASE64"));
        return inflate;
    }

    /**
     * 打开本地Html文件
     *
     * @param width
     * @param height
     * @param x
     * @param y
     * @return
     */
    private View getBdWebView(int width, int height, int x, int y) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.webview, null);
        WebView webView = (WebView) inflate.findViewById(R.id.webview);
        final ProgressBar progressBar = (ProgressBar) inflate.findViewById(R.id.progress);
        LayoutParams layoutParams = new LayoutParams(900, 600);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        inflate.setLayoutParams(layoutParams);
        setWebViewSettings(webView, progressBar);
        String s = this.getExternalCacheDir() + "/index.html";
        webView.loadUrl("file://" + s);
        return inflate;
    }

    /**
     * 打开本地Html文件
     *
     * @param width
     * @param height
     * @param x
     * @param y
     * @return
     */
    private View getBdWebView(String filePath, int width, int height, int x, int y) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.webview, null);
        WebView webView = (WebView) inflate.findViewById(R.id.webview);
        final ProgressBar progressBar = (ProgressBar) inflate.findViewById(R.id.progress);
        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        inflate.setLayoutParams(layoutParams);
        setWebViewSettings(webView, progressBar);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);//开启硬件加速
        WebSettings webSettings = webView.getSettings();
        //其他细节操作
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
// 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webView.clearCache(true);
        webView.loadUrl("file://" + filePath);
        return inflate;
    }

    /*
    *加载本地PDF文件
    * 该方法只适用于Android5.0以上
    * */
    private int pageCount = 0;
    private int curentPage = 0;

    private void getPDFViewByRender(String path, int width, int height, int x, int y, int playTime) {
        File file = new File(path);
        if (file.exists()) {
            PDFView viewPager = new PDFView(mContext, null);
            viewPager.fromFile(file).load();
            new PdfAsynTask(viewPager, path, playTime).execute();
            LayoutParams layoutParams = new LayoutParams(width, height);
            layoutParams.leftMargin = x;
            layoutParams.topMargin = y;
            viewPager.setLayoutParams(layoutParams);
//            pdfViewsMap.put(mId + file.getName(), viewPager);
            mRootRl.addView(viewPager);
           /* if (!isReplay){

            }
            for (Entry<String, PDFView> mView:pdfViewsMap.entrySet()){
                if (mView.getKey().equals(mId+file.getName())){
                    PDFView value = mView.getValue();
                    Log.e("tag","value="+value);
                    mRootRl.addView(value);
                    break;
                }
            }*/
        }
    }

    private Runnable mRunnable;

    public class PdfAsynTask extends AsyncTask<String, Object, Integer> {
        private PDFView mPDFView;
        private String filePath;
        private int playTime = 3;

        public PdfAsynTask(PDFView pdfView, String path, int playTime) {
            this.filePath = path;
            this.mPDFView = pdfView;
            if (playTime > 0) {
                this.playTime = playTime;
            }
            if (mRunnable != null) {
                mHandler.removeCallbacks(mRunnable);
            }
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            Log.e("tag", "pageCount=" + s.intValue());
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    if (curentPage < s.intValue()) {
                        curentPage++;
                    } else {
                        curentPage = 0;
                    }
                    mPDFView.jumpTo(curentPage, true);
                    mHandler.postDelayed(this, 1000 * playTime);
                }
            };
            mHandler.post(mRunnable);
        }

        @Override
        protected Integer doInBackground(String... strings) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    ParcelFileDescriptor descriptor = ParcelFileDescriptor.open(new File(filePath), ParcelFileDescriptor.MODE_READ_ONLY);
                    // This is the PdfRenderer we use to render the PDF.
                    if (descriptor != null) {
                        PdfRenderer pdfRenderer = new PdfRenderer(descriptor);
                        pageCount = pdfRenderer.getPageCount();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SystemClock.sleep(3000);
            return pageCount;
        }
    }

    /**
     * 图片
     *
     * @param url
     * @param width
     * @param height
     * @param x
     * @param y
     * @return
     */
    private ImageView getImage(String url, int width, int height, int x, int y) {
        ImageView imageView = new ImageView(this);
        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ScaleType.FIT_XY);
        String downPath = MyApplication.getInstance().getExternalCacheDir() + "/images/" + Math.abs(url.hashCode()) + "." + DocViewUtils.getInstance().getFileType(url);
        if (new File(downPath).exists()) {
            Log.e("tag", "展示本地单图");
            Bitmap bitmap = BitmapFactory.decodeFile(downPath);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        } else {
            Glide.with(this).load(url).error(R.mipmap.ic_launcher).into(imageView);
            Log.e("tag", "展示网络单图");
        }

        return imageView;
    }

    private WebView setWebViewSettings(WebView mWebView, final ProgressBar progressBar) {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //表示等待证书响应
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }
        });
        //声明WebSettings子类
        WebSettings webSettings = mWebView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        return mWebView;
    }

    @Override
    public void showLoading() {
//        if (!showLoading) return;
        showLoading1();
    }

    @Override
    public void showContent() {
//        if (!showLoading) return;
        super.showContent();
        goneLoading1();
    }

    @Override
    public void showEmpty() {
//        if (!showLoading) return;
        super.showEmpty();
        goneLoading1();
    }

    @Override
    public void showError() {
//        if (!showLoading) return;
//        super.showError();
        goneLoading1();
    }

    @Override
    public void showNoNetwork() {
//        if (!showLoading) return;
        super.showNoNetwork();
        goneLoading1();
    }

    /**
     * 获取整个窗口的截图
     *
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
    private void captureScreen(Activity context) {
        new screenTask(context).execute();
    }

    private class screenTask extends AsyncTask<File, String, File> {
        private Activity mContext;

        public screenTask(Activity context) {
            mContext = context;
        }

        @Override
        protected File doInBackground(File... strings) {
            View cv = mContext.getWindow().getDecorView();
            cv.setDrawingCacheEnabled(true);
            cv.buildDrawingCache();
            final Bitmap bmp = cv.getDrawingCache();
            if (bmp == null) {
                Log.e("aa", "截屏失败");
                return null;
            }
            bmp.setHasAlpha(false);
            bmp.prepareToDraw();
            String downPath = MyApplication.getInstance().getExternalCacheDir() + File.separator + Const.IMAGES + File.separator + "screen.png";
            File file = new File(downPath);//将要保存图片的路径
            if (file.exists()) {
                file.delete();
                Log.e("tag", "Action------>file.delete()");
            }
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bmp.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            mvpPresenter.commitPic(file, getVoice());
        }
    }

    private int getVoice() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);//媒体音量
        return volume;
    }

    private void setVoice(int index) {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, index, AudioManager.FLAG_PLAY_SOUND);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("tag", "Action----->onRequestPermissionsResult");
        if (requestCode == 77) {
            creatFilePermissionResult();
        }
        if (requestCode == 666 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startService_updateApk(apkUrl, version);
        }
    }
}
