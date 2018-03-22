package own.stromsong.myapplication.mvp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.yczbj.ycvideoplayerlib.OnVideoBackListener;
import org.yczbj.ycvideoplayerlib.VideoPlayer;
import org.yczbj.ycvideoplayerlib.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.VideoPlayerManager;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.mvp.base.BaseSupportActivity;
import own.stromsong.myapplication.mvp.model.MenuActUrlBean;
import own.stromsong.myapplication.mvp.model.MenuBean;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class VideoActivity extends BaseSupportActivity {

    @BindView(R.id.video_player)
    VideoPlayer mVideoPlayer;
    private List<MenuBean.ListResultBean.ListshowBean.Material.MaterialBean> material;
    private String url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video;
    }

    public static void startVideoActivity(Context context, List<MenuBean.ListResultBean.ListshowBean.Material.MaterialBean> list) {
        Intent intent = new Intent(context,VideoActivity.class);
        intent.putExtra("list", (Serializable) list);
        context.startActivity(intent);
    }

    @Override
    protected void initLocalData() {
        setBarVisible(false);
        material = (List<MenuBean.ListResultBean.ListshowBean.Material.MaterialBean>) getIntent().getSerializableExtra("list");
        if (material != null && material.size() > 0) {
            String file = material.get(0).getFile();
            if (!TextUtils.isEmpty(file)&&(file.endsWith("mp3") || (file.endsWith("mp4")))) {
                url = file;
            }
        }
        //设置播放类型
        // IjkPlayer or MediaPlayer
        mVideoPlayer.setPlayerType(VideoPlayer.TYPE_NATIVE);
        //网络视频地址

        //设置视频地址和请求头部
        mVideoPlayer.setUp(url, null);
        //是否从上一次的位置继续播放
        mVideoPlayer.continueFromLastPosition(true);
        //设置播放速度
        mVideoPlayer.setSpeed(1.0f);
        //创建视频控制器
        VideoPlayerController controller = new VideoPlayerController(this);
        controller.setOnVideoBackListener(new OnVideoBackListener() {
            @Override
            public void onBackClick() {
                mVideoPlayer.releasePlayer();
                onBackPressed();
            }
        });
//        controller.setTitle("Name");
        //设置视频时长
//        controller.setLength(98000);
//        设置5秒不操作后则隐藏头部和底部布局视图
        controller.setHideTime(5000);
        controller.setImage(R.mipmap.ic_launcher);
//        ImageUtil.loadImgByPicasso(this, R.drawable.image_default, R.drawable.image_default, controller.imageView());
        //设置视频控制器
        mVideoPlayer.setController(controller);
        mVideoPlayer.start();
        mVideoPlayer.setVideoComplete(new VideoPlayer.VideoComplete() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                mVideoPlayer.restart();
            }
        });
        /**
         * // 将列表中的每个视频设置为默认16:9的比例
         ViewGroup.LayoutParams params = mVideoPlayer.getLayoutParams();
         // 宽度为屏幕宽度
         params.width = itemView.getResources().getDisplayMetrics().widthPixels;
         // 高度为宽度的9/16
         params.height = (int) (params.width * 9f / 16f);
         mVideoPlayer.setLayoutParams(params);
         */

    }

    @Override
    protected void onStop() {
        super.onStop();
        VideoPlayerManager.instance().releaseVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (VideoPlayerManager.instance().onBackPressed()) return;
        super.onBackPressed();
    }
}
