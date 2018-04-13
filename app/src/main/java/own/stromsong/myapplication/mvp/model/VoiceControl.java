package own.stromsong.myapplication.mvp.model;

/**
 * Created by Administrator on 2018/4/13 0013.
 */

public class VoiceControl {
    private String volume;

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public VoiceControl(String volume) {
        this.volume = volume;
    }
}
