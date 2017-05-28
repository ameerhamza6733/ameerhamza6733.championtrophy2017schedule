package ameerhamza6733.championtrophy2017schedule.activitys;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.listener.VideoControlsVisibilityListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;

import ameerhamza6733.championtrophy2017schedule.R;


public class liveStreamPlayerActvty extends AppCompatActivity implements OnPreparedListener, OnBufferUpdateListener {

  //  ExoVideoView videoView;

    public static final String LIVE_STREM_URL_INTENT_KEY_EXTRA="LIVE_STREM_URL_INTENT_KEY_EXTRA";

    private String StreamURL;
    protected EMVideoView emVideoView;
    private FullScreenListener fullScreenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent =getIntent();
        if(intent!=null)
        StreamURL=intent.getStringExtra(liveStreamPlayerActvty.LIVE_STREM_URL_INTENT_KEY_EXTRA);
        else {
            Toast.makeText(this,"some thing wrong please retart the app",Toast.LENGTH_LONG).show();
            finish();
        }
        setContentView(R.layout.activity_live_stream_player_actvty);
         emVideoView = (EMVideoView)findViewById(R.id.video_view);
        emVideoView.setOnPreparedListener(this);

        //For now we just picked an arbitrary item to play.  More can be found at
        //https://archive.org/details/more_animation
        emVideoView.setVideoURI(Uri.parse(StreamURL));
        emVideoView.setOnBufferUpdateListener(this);

        fullScreenListener = new FullScreenListener();


        if (emVideoView.getVideoControls() != null) {
            emVideoView.getVideoControls().setVisibilityListener(new ControlsVisibilityListener());
        }




        // JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
       // jcVideoPlayerStandard.setUp(TEST_URL
       //         , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子闭眼睛");
      //  jcVideoPlayerStandard.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");


    }




    @Override
    public void onPrepared() {
        emVideoView.start();
        goFullscreen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        emVideoView.release();
        exitFullscreen();
    }

    @Override
    public void onBufferingUpdate(@IntRange(from = 0L, to = 100L) int percent) {

    }

    private void goFullscreen() {
        setUiFlags(true);
    }

    private void exitFullscreen() {
        setUiFlags(false);
    }

    /**
     * Applies the correct flags to the windows decor view to enter
     * or exit fullscreen mode
     *
     * @param fullscreen True if entering fullscreen mode
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setUiFlags(boolean fullscreen) {
        View decorView = getWindow().getDecorView();
        if (decorView != null) {
            decorView.setSystemUiVisibility(fullscreen ? getFullscreenUiFlags() : View.SYSTEM_UI_FLAG_VISIBLE);
            decorView.setOnSystemUiVisibilityChangeListener(fullScreenListener);
        }
    }

    /**
     * Determines the appropriate fullscreen flags based on the
     * systems API version.
     *
     * @return The appropriate decor view flags to enter fullscreen mode when supported
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private int getFullscreenUiFlags() {
        int flags = View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        return flags;
    }

    /**
     * Listens to the system to determine when to show the default controls
     * for the {@link EMVideoView}
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private class FullScreenListener implements View.OnSystemUiVisibilityChangeListener {
        @Override
        public void onSystemUiVisibilityChange(int visibility) {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                emVideoView.showControls();
            }
        }
    }


    private class ControlsVisibilityListener implements VideoControlsVisibilityListener {
        @Override
        public void onControlsShown() {
            // No additional functionality performed
        }

        @Override
        public void onControlsHidden() {
            goFullscreen();
        }
    }
}
