package ameerhamza6733.championtrophy2017schedule.activitys;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.crash.FirebaseCrash;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;
import com.thefinestartist.ytpa.utils.YouTubeUrlParser;

import java.util.List;

import ameerhamza6733.championtrophy2017schedule.R;
import ameerhamza6733.championtrophy2017schedule.fragments.fbcommantFragment;

import static ameerhamza6733.championtrophy2017schedule.MainActivity.DEvice_id;

/**
 * Created by AmeerHamza on 3/12/2017.
 */

public class YoutubePlayer extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener,
        YouTubePlayer.PlaybackEventListener,
        YouTubePlayer.PlayerStateChangeListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    public static String NOT_PLAYABLE = "NOT_PLAYABLE";
    private final String mInterstitilaAdId = "ca-app-pub-2042383951427959/9863385426";
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private TextView textView;
    private YouTubePlayer youtubeplayerP;
    private YouTubePlayer YPlayer;
    private String url;
    private FragmentManager fm;
    private FragmentTransaction fragmentTransaction;
    private InterstitialAd mInterstitialAd;
    private int count = 0;
    private String TAG = "YoutubePlayerActivtyTAG";
    private boolean openWebViewFlag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();

        Intent intent = getIntent();
        url = intent.getStringExtra("URL");



            intiYOuTube();



        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(mInterstitilaAdId);
        requestNewInterstitial();


    }

    private void intiChromeTab(String url) {
        try {
            String PACKAGE_NAME = "com.android.chrome";

            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
            customTabsIntent.intent.setData(Uri.parse(url));

            PackageManager packageManager = this.getPackageManager();
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(customTabsIntent.intent, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolveInfo : resolveInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                if (TextUtils.equals(packageName, PACKAGE_NAME))
                    customTabsIntent.intent.setPackage(PACKAGE_NAME);
            }

             customTabsIntent.launchUrl(this, Uri.parse(YouTubeUrlParser.getVideoUrl(url)));
            //this.startActivityForResult(customTabsIntent.intent, 11);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void intiYOuTube() {
        Log.d(TAG,"intiyoutube");
        FirebaseCrash.log("intiYoutube...");
        try {
            youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
            youTubePlayerView.initialize(" AIzaSyC1Ub8owKeoWxwek-mhjVH1xUppwjnzo04 ", this);
        }catch (Exception e)
        {//error in youutbe inti
            FirebaseCrash.log("error in youtube inti");
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize("AIzaSyDG7v02opTGkjFg8iWFAG6ytS3ki0X5a3Y", this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_player);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        YPlayer = player;
 /*
 * Now that this variable YPlayer is global you can access it
 * throughout the activity, and perform all the player actions like
 * play, pause and seeking to a position by code.
 */


        YPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
//Tell the player how to control the change
        player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean arg0) {

                intiFullScreen();

            }
        });

        YPlayer.setPlaybackEventListener(this);
        YPlayer.setPlayerStateChangeListener(this);

        if (!wasRestored) {

            Log.d(TAG, "replaing fragment ...");
            YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            YPlayer.cueVideo(url);
          try {
              fragmentTransaction.replace(R.id.fb_fragment_container, new fbcommantFragment()).commit();

          }catch (Exception e)
          {
              e.printStackTrace();
          }
        }

    }

    private void intiFullScreen() {
       try {
           if(url!=null)
           {
               Intent intent = new Intent(YoutubePlayer.this, YouTubePlayerActivity.class);
               //  String youtubeVideoId= CustomAdapter.mDataSet.get(getAdapterPosition()).getVideoStreemUrl().replace("http://www.youtube.com/watch?v=", "https://www.youtube.com/watch?v=");//replace http with https
               intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, url);

               intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.MINIMAL);
               intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO);
               intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
               intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               YoutubePlayer.this.startActivity(intent);
           }else {
               FirebaseCrash.log("Video id null inside the Youutbe Player Activty id = "+url);
           }
       }catch (Exception e)
       {
           e.printStackTrace();
       }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(DEvice_id)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    "There was an error initializing the YouTubePlayer",
                    errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        count++;

        Log.d(TAG, "onResume" + "counter = " + count);
        if (count > 0) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show();
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        requestNewInterstitial();

                    }
                });

            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           // Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            intiFullScreen();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
           // Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPlaying() {
        Log.d(TAG, "onPlaying");

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

        Log.d(TAG, "onLoaded" + s);
    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {
        Log.d(TAG, "onVideoStarted");
    }

    @Override
    public void onVideoEnded() {


    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

        Log.d(TAG, "onError" + errorReason.toString());
        if (errorReason.toString().equals(NOT_PLAYABLE) && openWebViewFlag) {
            openWebViewFlag = false;

            intiChromeTab(YouTubeUrlParser.getVideoUrl(url));

        }
    }


}