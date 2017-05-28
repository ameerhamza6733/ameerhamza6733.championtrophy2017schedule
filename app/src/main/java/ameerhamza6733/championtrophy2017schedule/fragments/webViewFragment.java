package ameerhamza6733.championtrophy2017schedule.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.crash.FirebaseCrash;

import ameerhamza6733.championtrophy2017schedule.R;

/**
 * Created by AmeerHamza on 3/10/2017.
 */

public class webViewFragment extends Fragment {
    WebView mWeb;
    ProgressBar progressBar;
    String site;
    private NativeExpressAdView adView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bt4, container, false);
        this.site = "http://www.cricwaves.com/cricket/widgets/script/scoreWidgets.js%22%3E%3C/script%3E";
        this.mWeb = (WebView) rootView.findViewById(R.id.web1);
        this.progressBar = (ProgressBar) rootView.findViewById(R.id.pro);
        this.swipeRefreshLayout= (SwipeRefreshLayout) rootView.findViewById(R.id.myliveScoreSwipeTorefash);
         adView = (NativeExpressAdView)rootView.findViewById(R.id.web_view_adView);
       
        WebSettings set = this.mWeb.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
        this.mWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webViewFragment.this.mWeb.destroy();
                webViewFragment.this.mWeb.loadUrl(webViewFragment.this.site);
            }
        });
        this.mWeb.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webViewFragment.this.progressBar.setVisibility(View.VISIBLE);
                try {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    webViewFragment.this.adView.loadAd(adRequest);   
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
               webViewFragment.this.progressBar.setVisibility(View.GONE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
           try {
               FirebaseCrash.log("webViewFragment :: trying to disply :: Please Connect your Mobile Data or Wifi Connetion ");
               Toast.makeText(getActivity(), "Please Connect your Mobile Data or Wifi Connetion, " + description, Toast.LENGTH_LONG).show();
           }catch (Exception e)
           {
               e.printStackTrace();

           }
            }
        });
        this.mWeb.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                webViewFragment.this.progressBar.setProgress(newProgress);
            }
        });
       
        return rootView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            if(mWeb!=null)
            {
                this.mWeb.loadUrl(this.site);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}