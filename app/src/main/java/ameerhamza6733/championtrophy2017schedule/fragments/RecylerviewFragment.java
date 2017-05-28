package ameerhamza6733.championtrophy2017schedule.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ameerhamza6733.championtrophy2017schedule.R;
import ameerhamza6733.championtrophy2017schedule.adupters.CustomAdapter;
import ameerhamza6733.championtrophy2017schedule.model.video;

/**
 * Created by AmeerHamza on 1/19/2017.
 */

public class RecylerviewFragment extends Fragment {


    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;

    private LayoutManagerType mCurrentLayoutManagerType;
    private RecyclerView mRecyclerView;
    private CustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] mDataset;
    private ProgressBar mProgressBar;


    private RequestQueue requestQueue;
    private List<String> mUrlList = new ArrayList<>();
    private ArrayList<video> mVideoDataSet = new ArrayList<>();
    private String url;
    private video mVideo;
    private int i = 0;

    private String JsonURL = "https://www.youtube.com/oembed?url=https://www.youtube.com/watch?v=nlOR80C-AwY&format=json";
    public static String postID="div#post-body-5818759705570169637";
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_DATA_SET = "KEY_DATA_SET";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.myRecyclerView);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.myRecylerprogressBar);
//        mSwipeRefreshLayout= (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayoutVideoFrag);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//
//                initDataset();
//                mSwipeRefreshLayout.setRefreshing(true);
//            }
//        });

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        requestQueue = Volley.newRequestQueue(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

//        if (savedInstanceState != null) {
//            mVideoDataSet = savedInstanceState.getParcelableArrayList(KEY_DATA_SET);
//            mAdapter = new CustomAdapter(mVideoDataSet);
//            mRecyclerView.setAdapter(mAdapter);
//            mProgressBar.setVisibility(View.GONE);
//        } else {
//          try {
//              if(isVisible())
//              {
//                  initDataset();
//                  mProgressBar.setVisibility(View.VISIBLE);
//              }
//          }catch (Exception e)
//          {
//              e.printStackTrace();
//          }
//        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);




        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
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
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        // savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        savedInstanceState.putParcelableArrayList(KEY_DATA_SET, mVideoDataSet);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "This is element #" + i;
        }

        // new UrlGraber().execute();
        new UrlGraber().execute();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
           try {
               if(mVideoDataSet.isEmpty() || mVideoDataSet.size()==0)
               {
                   initDataset();
               }
           }catch (Exception e)
           {
               e.printStackTrace();
           }
        }
    }

    private void initVolly() {


        do {


           try {
               if(i<mUrlList.size())
                   url = mUrlList.get(i);
               else
                   break;
               Log.d(TAG,"value of i = "+i);
               FirebaseCrash.log(" RecylerViewFragment :: iside the initVolly "+i);
               if(i==0)
               {
                   JsonURL = JsonURL.replace("https://www.youtube.com/watch?v=nlOR80C-AwY", url);




               }else {
                   JsonURL = JsonURL.replace(mUrlList.get(i-1), url);

               }
           }catch (Exception e)
           {
               FirebaseCrash.log(" RecylerViewFragment :: iside the initVolly before Json object req");
               e.printStackTrace();
           }




            Log.d(TAG, "uroJson=" + JsonURL);
            JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET, JsonURL, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // mVideo = new video(response.getString("title"), response.getString("thumbnail_url"), response.getString("author_name"));
                                //  mVideoDataSet.add(mVideo);

                                mVideo = new video(url);
                                mVideo.setVideoTitle(response.getString("title"));
                                mVideo.setVideoThimailUrl(response.getString("thumbnail_url"));
                                mVideo.setVideoChannalName(response.getString("author_name"));
                                mVideo.setLiveNow(false);
                                mVideoDataSet.add( mVideo);
                                Log.d(TAG, "inside the volly" + mVideo.getVideoTitle());
                               // Log.d(TAG, mVideoDataSet.get(i).getVideoStreemUrl());



                                if (mAdapter != null) {
                                    mAdapter.notifyDataSetChanged();
//                                    if(mSwipeRefreshLayout.isRefreshing())
//                                        mSwipeRefreshLayout.setRefreshing(false);

                                } else {
                                    Log.d(TAG, "setingAdupter");
//                                    if(mSwipeRefreshLayout.isRefreshing())
//                                        mSwipeRefreshLayout.setRefreshing(false);
                                   mAdapter = new CustomAdapter(mVideoDataSet);
                                    mRecyclerView.setAdapter(mAdapter);

                                }
                                i++;
                                if (i != mUrlList.size())
                                    initVolly();


                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           // if(error)
                          i++;
                            initVolly();
                            Log.e("Volley", "Error");
                        }
                    }
            );
            // Adds the JSON object request "obreq" to the request queue
            requestQueue.add(obreq);
        } while (false);


    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    private class UrlGraber extends AsyncTask<Void, Void, Void> {


        private String[] urls;

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Document rssDocument = null;
                rssDocument = Jsoup.connect(getResources().getString(R.string.conn_video)).timeout(6000).ignoreContentType(true).parser(Parser.htmlParser()).get();
                Element eMETA = rssDocument.select(RecylerviewFragment.postID).first();

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
                    Snackbar mSnackbar = Snackbar.make(getView().findViewWithTag(TAG), "No Connection", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    new UrlGraber().execute();


                                }
                            });
                    mSnackbar.show();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else {
                try {
//                    if (mSwipeRefreshLayout.isRefreshing()) {
//                       mUrlList.clear();
//
//                    }

                    mProgressBar.setVisibility(View.GONE);

                    FirebaseCrash.log("RecylerViewFragment :: onPostExcute :: urls size "+urls.length);
                    if(urls.length !=0)
                    {Collections.addAll(mUrlList, urls);
                        System.out.println(mUrlList);
                        initVolly();

                    }else {
                        new UrlGraber().execute();
                    }


                } catch (Exception e) {
                    //posible user kill the app but task is still try to updata UI
                    e.printStackTrace();
                }
            }

        }
    }
//
//    private class GrabLiveStreamURl extends AsyncTask<Void, Void, Void> {
//        private List<Date> mDateList;
//        private HashMap<Date,String> mLiveStramURLs;//make hash app for all videos thoise have live:: title
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            this.mDateList=new ArrayList<>();
//            this.mLiveStramURLs= new HashMap<>();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//                Document rssDocument = null;
//                rssDocument = Jsoup.connect(RSS_CHANNEL_FEDDS_URL).timeout(6000).ignoreContentType(true).parser(Parser.xmlParser()).get();
//                Elements elements = rssDocument.select("entry");
//                for (Element element : elements) {
//                    if (element.select("title").first().text().equals(LIVE_STREAM_VIDEO_TITLE)) {
//                        liveStreamUrl = element.select("link").attr("href").toString();
//                        mLiveStreamVideoName.add(element.select("title").first().text());
//                        mUrlList.add(liveStreamUrl);
////                        Log.d(TAG, liveStreamUrl);
////                        mUrlList.add(liveStreamUrl);
////                        String customFormat = "yyyy-MM-dd HH:mm:ss";
////
////                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
////
////                        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////
////                        String s =element.select("updated").first().text();
////                        Date d = sdf.parse(s);
////                        String formattedTime = output.format(d);
////                        Log.d(TAG,"time and data"+formattedTime+"time in miliseonde"+d.getTime());
////
////                        mDateList.add(d);
////                       String liveStramUrl =  element.select("link").attr("href").toString();
////                        Log.d(TAG,"live stream url "+liveStramUrl);
////                        mLiveStramURLs.put(d,liveStramUrl);
//
//
//                    }
//                }
//                //check the witch video last update based on data and time stemp
////                if(mDateList.size()>1)
////                {
////                    for(int i =0 ; i<mDateList.size() ;)
////                    {
////
////                        Date date1 = mDateList.get(i);
////
////                        if(i++ < mDateList.size())
////                        {
////                            Date date2 = mDateList.get(i);
////                            if(date1.getTime()>date2.getTime())
////                            {
////                                Log.d(TAG,"date1 > data2");
////                            }else {
////                                Log.d(TAG,"date1 < data2" +i);
////                                String element = mLiveStramURLs.get(mDateList.get(1));
////                                mUrlList.add(element);
////                            }
////                        }
////
////                    }
////                }else {
////                   String element = mLiveStramURLs.get(mDateList.get(0));
////                    mUrlList.add(element);
////                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            if (mUrlList.isEmpty()) {
//                try {
//                    Snackbar mSnackbar = Snackbar.make(getView().findViewWithTag(TAG), "No Connection For Videos", Snackbar.LENGTH_INDEFINITE)
//                            .setAction("Try Again", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//
//
//                                    new GrabLiveStreamURl().execute();
//
//
//                                }
//                            });
//                    mSnackbar.show();
//                } catch (Exception e) {
//
//                }
//            } else
//                new UrlGraber().execute();
//        }
//    }
}