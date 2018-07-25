package own.stromsong.myapplication.mvp.model;

import android.media.MediaPlayer;

/**
 * Created by Administrator on 2018/7/11.
 */

public class MediaParamBean {
    private MediaPlayer mMediaPlayer;
    private String videoUrl;

    public MediaParamBean(MediaPlayer mediaPlayer, String videoUrl) {
        mMediaPlayer = mediaPlayer;
        this.videoUrl = videoUrl;
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        mMediaPlayer = mediaPlayer;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
