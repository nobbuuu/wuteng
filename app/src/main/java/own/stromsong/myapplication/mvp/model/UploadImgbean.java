package own.stromsong.myapplication.mvp.model;

/**
 * Created by Administrator on 2018/4/13 0013.
 */

public class UploadImgbean {

    /**
     * msg : 上传成功!
     * imgs :
     * code : 200
     * size : 25569
     * name : 123.jpg
     * time :
     * url : http://120.24.234.123:8666/sunnet_ad/userfiles/2018-04/img/44a534f5-2580-424f-8574-dacd93d70ee3.jpg
     */

    private String msg;
    private String imgs;
    private String code;
    private int size;
    private String name;
    private String time;
    private String url;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
