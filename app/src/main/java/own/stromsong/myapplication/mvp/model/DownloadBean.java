package own.stromsong.myapplication.mvp.model;

/**
 * Created by Administrator on 2017/10/13 0013.
 */
public class DownloadBean {
   private String eventStr;
   private String apkUrl;
   private String version;

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEventStr() {
        return eventStr;
    }

    public void setEventStr(String eventStr) {
        this.eventStr = eventStr;
    }
}
