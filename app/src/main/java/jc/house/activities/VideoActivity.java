package jc.house.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import jc.house.R;

public class VideoActivity extends BaseActivity implements UniversalVideoView.VideoViewCallback {
    public static final String FLAG_VIDEO_URL = "flag_video_url";
    public static final String FLAG_VIDEO_NAME = "flag_video_name";
    private View mVideoLayout;
    private UniversalVideoView mVideoView;
    private UniversalMediaController mVideoController;
    private boolean isFullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_video);
        String videoUrl = this.getIntent().getStringExtra(FLAG_VIDEO_URL);
        String videoName = this.getIntent().getStringExtra(FLAG_VIDEO_NAME);
        setTitleBarTitle(null == videoName ? "视频详情" : videoName);
        mVideoLayout = findViewById(R.id.video_layout);
        mVideoView = (UniversalVideoView)findViewById(R.id.videoView);
        mVideoController = (UniversalMediaController)findViewById(R.id.media_controller);
        mVideoView.setVideoPath(videoUrl);
        mVideoView.setMediaController(mVideoController);
        mVideoView.setVideoViewCallback(this);
        this.isFullscreen = false;
        setVideoSize(isFullscreen);
        mVideoView.start();
    }

    private void setVideoSize(final boolean isFullscreen) {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                int height = isFullscreen ? ViewGroup.LayoutParams.MATCH_PARENT : (int)(width * (9.0f / 16.0f));
                ViewGroup.LayoutParams params = mVideoLayout.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = height;
                mVideoLayout.setLayoutParams(params);
                mVideoView.requestFocus();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mVideoView && mVideoView.isPlaying()) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onScaleChange(boolean isFullscreen) {
        if (isFullscreen) {
            hideTitleBar();
            setVideoSize(true);
        } else {
            showTitleBar();
            setVideoSize(false);
        }
        this.isFullscreen = isFullscreen;
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {

    }
}
