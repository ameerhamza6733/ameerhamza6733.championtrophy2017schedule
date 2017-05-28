package ameerhamza6733.championtrophy2017schedule;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import com.squareup.picasso.Picasso;

import ameerhamza6733.championtrophy2017schedule.activitys.about;
import ameerhamza6733.championtrophy2017schedule.adupters.myPagerAdupter;
import ameerhamza6733.championtrophy2017schedule.fragments.Live;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String DEvice_id = "38F39A259FEF9100994147C67D350A04";
    private static final String _VIDEO_AD_UNIT_ID = "ca-app-pub-2042383951427959/3050065025";
    public static String appPackageName;
    private static int count = 0;
  //  private RewardedVideoAd mAd;

    private final String mInterstitilaAdId = "ca-app-pub-2042383951427959/5889219428";
    private InterstitialAd mInterstitialAd;
    private String TAG = "MainActivityTAg";
    private String deviceId;
    private int[] iconResIds = {
            R.drawable.cricket_icon,
            R.mipmap.ic_schedule_black_24dp,
            R.mipmap.ic_ondemand_video_black_24dp,
            R.mipmap.ic_favorite_black_24dp,


    };
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private myPagerAdupter myPagerAdupter_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      ///  FirebaseInstanceId.getInstance().getToken();

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        myPagerAdupter_=  new myPagerAdupter(getSupportFragmentManager(),MainActivity.this);

        viewPager.setAdapter(myPagerAdupter_);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(mInterstitilaAdId);
//        mAd = MobileAds.getRewardedVideoAdInstance(this);
//        mAd.setRewardedVideoAdListener(this);

        requestNewInterstitial();
        loadVideoAd();

        appPackageName = getPackageName();
        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        tabLayout.setupWithViewPager(viewPager);
        try {
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                if (i == 0)
                    tabLayout.getTabAt(i).setIcon(R.drawable.cricket_icon);
                else
                    tabLayout.getTabAt(i).setIcon(iconResIds[i]);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View mHaderView =  navigationView.getHeaderView(0);
       ImageView mhaderImageView = (ImageView) mHaderView.findViewById(R.id.imageView);
     //   Picasso.with(this).load("http://iplt20livestream.in/wp-content/uploads/2017/01/12112502_1050494644999737_6375344926592110301_n.jpg").into(mhaderImageView);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();



        String str = this.TAG;
        StringBuilder append = new StringBuilder().append("onResumeInside");
        int i = count;
        count = i + 1;
       Log.d(str," l "+ append.append(i).toString());
        if (count > 0 && this.mInterstitialAd != null) {
            this.mInterstitialAd.show();
            this.mInterstitialAd.setAdListener(new AdListener() {
                public void onAdClosed() {
                    super.onAdClosed();
                    MainActivity.this.requestNewInterstitial();
//                    if (MainActivity.this.tabLayout.getSelectedTabPosition() == 1) {
//                        Live mLive = (Live) MainActivity.this.viewPager.getAdapter().instantiateItem(MainActivity.this.viewPager, 1);
//                        mLive.getmSwipeRefreshLayout().setRefreshing(true);
//                        mLive.initDataset();
//                        Log.d(MainActivity.this.TAG, " myPagerAdupter_.notifyDataSetChanged();");
//                    }
                }
            });
        }

    }



    private void showAdd() {
        try {
            if (mInterstitialAd != null) {
                mInterstitialAd.show();
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        requestNewInterstitial();
                        if (tabLayout.getSelectedTabPosition() == 3) {


                            Live mLive = (Live) viewPager.getAdapter().instantiateItem(viewPager,3);
                            mLive.getmSwipeRefreshLayout().setRefreshing(true);
                            mLive.initDataset();
                            Log.d(TAG," myPagerAdupter_.notifyDataSetChanged();");

                        }
                    }
                });

            }else {
                Log.d(TAG,"mInterstitialAd == null");
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rate_me) {

           rateMe();
//            if(mAd.isLoaded())
//                mAd.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void rateMe() {
        Uri uri = Uri.parse("market://details?id=" + MainActivity.this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_tell_toyour_frined) {
            try {
                int applicationNameId = this.getApplicationInfo().labelRes;
                final String appPackageName = this.getPackageName();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, this.getString(applicationNameId));
                String text = "Install this cool application: ";
                String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
                i.putExtra(Intent.EXTRA_TEXT, text + " " + link);
                startActivity(Intent.createChooser(i, "Share link:"));
            } catch (Exception e) {

            }
        } else if (id == R.id.nav_update_app) {
            Uri uri = Uri.parse("market://details?id=" + MainActivity.this.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            }
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
            }
        }else if(id==R.id.nav_rate_us)
        {
            rateMe();
        }else if(id==R.id.nav_rate_about_us)
        {
            startActivity(new Intent(this,about.class));
        }else  if(id== R.id.nav_send_feedback)
        {
            startActivity(new Intent(this,about.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadVideoAd()
    {
//        if (!mAd.isLoaded()) {
//            mAd.loadAd(_VIDEO_AD_UNIT_ID, new AdRequest.Builder().addTestDevice(DEvice_id).build());
//
//
//        }


    }
//    @Override
//    public void onRewarded(RewardItem reward) {
//        Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " +
//                reward.getAmount(), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRewardedVideoAdLeftApplication() {
//        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
//                Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRewardedVideoAdClosed() {
//        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRewardedVideoAdFailedToLoad(int errorCode) {
//        Toast.makeText(this, "onRewardedVideoAdFailedToLoad"+errorCode, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRewardedVideoAdLoaded() {
//        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRewardedVideoAdOpened() {
//        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRewardedVideoStarted() {
//        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
//    }

}
