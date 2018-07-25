package own.stromsong.myapplication.utils;

import android.util.Log;

import com.blankj.utilcode.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import own.stromsong.myapplication.app.MyApplication;
import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.other.Const;

/**
 * Created by Administrator on 2017/10/20 0020.
 */
public class StringUtil {
    public static boolean notEmpty(String tempStr) {
        if (tempStr.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回false表示null或者空
     *
     * @param s
     * @return
     */
    public static boolean NoNullOrEmpty(String s) {
        if (s != null && !s.isEmpty()) {
            return true;
        }
        return false;
    }

    public static String checkNull(String tempStr) {
        return tempStr == null ? "" : tempStr;
    }

    public static String checkNull(String tempStr, String defaultStr) {
        return tempStr == null ? defaultStr : tempStr;
    }

    public static String getListStr(List<String> strings) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < strings.size(); i++) {
            if (i != strings.size() - 1) {
                buffer.append(strings.get(i) + ",");
            } else {
                buffer.append(strings.get(i));
            }
        }
        return buffer.toString();
    }
    public static String listParseStrings(List<MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean> strings) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < strings.size(); i++) {
            String file = strings.get(i).getMaterial().getFile();
            if (i != strings.size() - 1) {
                buffer.append(file + ",");
            } else {
                buffer.append(file);
            }
        }
        return buffer.toString();
    }

    /**
     * 验证是否存在
     *
     * @param list
     * @param str
     * @return
     */
    public static boolean vifgetMap(List<String> list, String str) {
        for (String l : list) {
            if (str.equals(l)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取N个list图片列表
     *
     * @param list
     * @return
     */
    public static List<List<String>> getList(List<MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean> list) {
        List<String> l = getMap(list);
        List item = new ArrayList();
        for (String remake : l) {
            List<String> lo = new ArrayList<>();
            for (MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean me : list) {
                if (StringUtil.NoNullOrEmpty(me.getRemarks()) && remake.equals(me.getRemarks())) {
                    lo.add(me.getMaterial().getFile());
                }

            }
            item.add(lo);
        }
        return item;
    }


    /**
     *
     *
     */

    /**
     * 获取N个图集
     *
     * @param list
     * @return
     */
    // map { [ { 'key':'-1',list:[string,string] ,{ 'key':'1',list:[{ShowsMateria},{ShowsMateria}] ] }
    public static List<Map<String, Object>> getMapList(List<MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean> list) {

        List<String> l = getMap(list);
        List<Map<String, Object>> item = new ArrayList<>();
        for (String remake : l) {
            List<MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean> lo = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            for (MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean me : list) {
                if (StringUtil.NoNullOrEmpty(me.getRemarks()) && remake.equals(me.getRemarks())) {
                    lo.add(me);
                }
            }
            map.put("key", remake);
            map.put("list", lo);
            item.add(map);
        }
        return item;
    }


    /**
     * 缓存处理
     *
     * @param list
     * @return
     */
    public static List<ImgDome> getImgDome(List<Map<String, Object>> list) {
        List<ImgDome> item = new ArrayList<>();
        for (Map<String, Object> map : list) {
            if (map.get("key") != null) {
                ImgDome img = new ImgDome();
                img.setKey(String.valueOf(map.get("key")));
                if (map.get("list") != null) {
                    List<MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean> l = (List<MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean>) map.get("list");
                    if (l != null && l.size() > 0) {
                        List<String> fileList = new ArrayList<>();
                        int showType=-2;
                        int playTime = 3;
                        for (MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean u : l) {
                            ImgDome.DomeValu dom = new ImgDome.DomeValu();
                            dom.setH(u.getHige());
                            dom.setW(u.getWide());
                            dom.setX(Integer.valueOf(u.getX()));
                            dom.setY(Integer.valueOf(u.getY()));
                            //设置本地图片路径
                            String downPath = MyApplication.getInstance().getExternalCacheDir()+"/images/"+Math.abs(u.getMaterial().getFile().hashCode())+".jpg";
                            dom.setFile(downPath);
                            fileList.add( "../images/"+Math.abs(u.getMaterial().getFile().hashCode())+".jpg");
                            img.setDomeValu(dom);
                            showType=u.getShowType();
                            if (u.getPlayTime()>0){
                                playTime = u.getPlayTime();
                            }
                        }
                        //index.html
                        InputStream is = MyApplication.getInstance().getClass().getClassLoader().getResourceAsStream("assets/ftl/index.ftl");
                        String content = FileUtils.readFileByLines(is);
                        Map<String, Object> p_map = new HashMap<String, Object>();
                        p_map.put("imgPaths", fileList);
                        Log.e("tag","showType="+showType);
                        p_map.put("showType", showType);
                        Log.e("cnm","pictures_playtime_one="+playTime);
                        p_map.put("times", String.valueOf(1000*playTime));//单张图的停留时长
                        p_map.put("duration", Const.ONETIME_IMG_ANIM);//动画时长
                        p_map.put("ben", 0);
                        String resultContent = FreeMarkers.renderString(content, p_map); //新的字符串
                        String filePath = MyApplication.getInstance().getExternalCacheDir() + "/html/index_" + map.get("key") + ".html"; //缓存文件路径
                        FileUtils.writeToFile(filePath, resultContent);
                        img.setUrl(filePath);
                        item.add(img);
                    }
                }
            }
        }
        return item;
    }


    /**
     * 获取N个分组
     *
     * @param list
     * @return
     */
    public static List<String> getMap(List<MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean> list) {
        List<String> l = new ArrayList<>();
        for (MenuBean.ListResultBean.ListshowBean.Material.ShowsMaterialBean me : list) {
            if (StringUtil.NoNullOrEmpty(me.getRemarks())) {
                if (!vifgetMap(l, me.getRemarks())) {
                    l.add(me.getRemarks());
                }
            }
        }
        return l;
    }
}