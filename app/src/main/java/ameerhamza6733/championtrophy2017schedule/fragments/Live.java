package ameerhamza6733.championtrophy2017schedule.fragments;

/**
 * Created by AmeerHamza on 2/9/2017.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.List;

import ameerhamza6733.championtrophy2017schedule.R;
import ameerhamza6733.championtrophy2017schedule.adupters.liveAdupter;
import ameerhamza6733.championtrophy2017schedule.interfase.upDateLiveStreamLinks;
import ameerhamza6733.championtrophy2017schedule.model.video;

public class Live extends Fragment implements upDateLiveStreamLinks {

    private static final String TAG = "LiveFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    public static String postID = "div#post-body-4004551379368820875";
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected liveAdupter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<video> mUrlList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int i = 0;
    private GrabeLiveUrl grabeLiveUrl;
    private FloatingActionButton mFabRefresh;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        grabeLiveUrl = new GrabeLiveUrl();
        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recylerview_frag_live, container, false);
        rootView.setTag(TAG);
        Log.d(TAG, "'" + i);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.myRecyclerViewLive);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.myRecylerprogressBarLive);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.mySwipeRefreshLayoutLive);
        mFabRefresh = (FloatingActionButton) rootView.findViewById(R.id.fabRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {

                    if (!mUrlList.isEmpty())
                        mUrlList.clear();
                    if (mAdapter != null)
                        mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                initDataset();

                initDataset();
            }
        });
        mFabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.d(TAG, "mFabRefresh is staring ... ");
                try {
                    if (!mUrlList.isEmpty()) {
                        mUrlList.clear();
                    }

                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    } else {

                        Toast.makeText(getActivity(), "Error please restart the app", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                initDataset();
                if (!mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(true);

            }
        });

        requestQueue = Volley.newRequestQueue(getActivity());

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        //  mAdapter = new shaudleAdupter(mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        // mRecyclerView.setAdapter(mAdapter);


        initDataset();
        return rootView;
    }


    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        try {
            int scrollPosition = 0;

            // If a layout manager has already been set, get current scroll position.
            if (mRecyclerView.getLayoutManager() != null) {
                scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                        .findFirstCompletelyVisibleItemPosition();
            }

            switch (layoutManagerType) {
                case GRID_LAYOUT_MANAGER:
                    mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                    mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                    break;
                case LINEAR_LAYOUT_MANAGER:
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                    break;
                default:
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
            }

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.scrollToPosition(scrollPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            Log.d(TAG, "'" + i);
//            initDataset();
//            i++;
//        } else {
//        }
//    }


    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    public void initDataset() {


        new GrabeLiveUrl().execute();
    }

    @Override
    public void onUpdateLiveStreamLinks() {

        Log.d(TAG, "onUpdateLiveStreamLinks");

        //  }
    }

    public SwipeRefreshLayout getmSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    private void initVolley(String[] urls) {


        for (String url : urls) {

            if (url.startsWith("https://www.googleapis.com/youtube/v3/")) {
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("items");
                            if (jsonArray.length() > 0) {
                                for (int getItem = 0; getItem < jsonArray.length(); getItem++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(getItem).getJSONObject("id");
                                    String videoID = jsonObject.getString("videoId");


                                    Log.d(TAG, "void id " + videoID);

                                    mUrlList.add(new video(jsonArray.getJSONObject(getItem).getJSONObject("snippet").getString("title"), jsonArray.getJSONObject(getItem).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url"), " ", "https://www.youtube.com/watch?v=" + videoID, true));


                                    if (mAdapter != null)
                                        mAdapter.notifyDataSetChanged();
                                    Log.d(TAG, videoID + jsonArray.length() + jsonArray.getJSONObject(getItem).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url"));

                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });
                requestQueue.add(jsonObjectRequest);
            }
        }
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    private class GrabeLiveUrl extends AsyncTask<Void, Void, Void> {


        private String[] urls;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mUrlList = new ArrayList<>();


        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document rssDocument = null;
                rssDocument = Jsoup.connect(getResources().getString(R.string.conn_live)).timeout(6000).ignoreContentType(true).parser(Parser.xmlParser()).get();

                Element eMETA = rssDocument.select(Live.postID).first();


                String s = eMETA.toString();
                s = Jsoup.parse(s).text();
                Log.d(TAG, s);
                urls = s.split("\\s");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (urls == null && getView() != null) {
                try {
                    Snackbar mSnackbar = Snackbar.make(getView().findViewWithTag(TAG), "No Connection For Live Match", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    new GrabeLiveUrl().execute();


                                }
                            });
                    mSnackbar.show();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else {


                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                mProgressBar.setVisibility(View.GONE);


                try {
                    String videoPhoto = "https://s-media-cache-ak0.pinimg.com/originals/97/91/18/979118582a2da09a69d25fb4c2720993.jpg";
                    int i = 0;

                    if (urls == null)//make sure urls not equals to null
                        Toast.makeText(getActivity(), "some thing wrong please check back latter", Toast.LENGTH_LONG).show();
                    else {
                        for (String url : urls) {

                            Log.d(TAG,"url = "+url);
                            if (!url.startsWith("https://www.googleapis.com/youtube/v3/")) {
                                if (url.contains("ptv")) {

                                    mUrlList.add(new video(" Live stream Link " + i, "https://pbs.twimg.com/profile_images/791772528809275394/FSGzQRzu_400x400.jpg", " ", url, true));
                                } else if (url.contains("star")) {
                                    mUrlList.add(new video(" Live stream Link " + i, "https://pbs.twimg.com/profile_images/637699813094502400/rtWivcyO_400x400.jpg", " ", url, true));
                                } else if (url.contains("ten")) {

                                    mUrlList.add(new video(" Live stream Link " + i, "https://pbs.twimg.com/profile_images/619223335327649792/_OdJqt_l_400x400.png", " ", url, true));
                                } else if(url.contains("sony"))
                                {
                                    mUrlList.add(new video(" Live stream Link " + i, "https://pbs.twimg.com/profile_images/766503919958781952/l6z_ZP-v_400x400.jpg", " ", url, true));

                                }else  if(url.contains("geo"))
                                {
                                    mUrlList.add(new video(" Live stream Link " + i, "https://pbs.twimg.com/profile_images/378800000773589149/631e4b216f76e7a4cf5569e429191d7d_400x400.jpeg", " ", url, true));

                                }else
                                {
                                    mUrlList.add(new video(" Live stream Link " + i, videoPhoto, " ", url, true));
                                }
                                i++;
                            } else {
                                initVolley(urls);
                            }

                        }
                        mAdapter = new liveAdupter(mUrlList);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } catch (Exception e) {
                    FirebaseCrash.log("indise the onPostExcute()");
                    e.printStackTrace();
                }


            }

        }
    }
}