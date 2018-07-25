package own.stromsong.myapplication.mvp.model;

import android.os.Bundle;

/**
 * Created by Administrator on 2017/10/13 0013.
 */
public class CompleteBean {
    private String eventStr;
    private String path;
    private Bundle mBundle;

    public Bundle getBundle() {
        return mBundle;
    }

    public void setBundle(Bundle bundle) {
        mBundle = bundle;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEventStr() {
        return eventStr;
    }

    public void setEventStr(String eventStr) {
        this.eventStr = eventStr;
    }
}
