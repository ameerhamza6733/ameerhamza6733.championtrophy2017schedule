package ameerhamza6733.championtrophy2017schedule.activitys;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import ameerhamza6733.championtrophy2017schedule.R;
import ameerhamza6733.championtrophy2017schedule.fragments.liveScrFramgment;

public class liveFragmentContanerActivty extends AppCompatActivity {
   // private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_fragment_contaner_activty);
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-2042383951427959/3086113029");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.");
//        }
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                // Load the next interstitial.
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//
//        });

        Intent i  =getIntent();
        int matchSrs=i.getIntExtra("srs",0);
        Bundle bundle = new Bundle();
        bundle.putInt("matchSrs",matchSrs);
        liveScrFramgment liveScrFramgmentObj = new liveScrFramgment();
        liveScrFramgmentObj.setArguments(bundle);
        setFragment(liveScrFramgmentObj);

    }



    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.liveScro_fragmnet_contaner, fragment);
        fragmentTransaction.commit();
    }
}
