package ameerhamza6733.championtrophy2017schedule.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
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


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;


import java.util.ArrayList;


import ameerhamza6733.championtrophy2017schedule.R;
import ameerhamza6733.championtrophy2017schedule.adupters.shaudleAdupter;
import ameerhamza6733.championtrophy2017schedule.model.schedule;

/**
 * Demonstrates the use of {@link RecyclerView} with a {@link LinearLayoutManager} and a
 * {@link GridLayoutManager}.
 */
public class shaduleFragment extends Fragment {

    private static final String TAG = "shaduleFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected shaudleAdupter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected ProgressBar mProgressBar;

    public static String PostId="div#post-body-2850778440236054028";

    protected ArrayList<schedule> mTimeTableDetailsDataSet = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shaudle_fragment, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.myScheduleRecyclerView);
        mSwipeRefreshLayout= (SwipeRefreshLayout) rootView.findViewById(R.id.myScheduleSwipeRefreshLayout);
        mProgressBar= (ProgressBar) rootView.findViewById(R.id.myScheduleRecyclerViewProgressBar);


        if(mSwipeRefreshLayout!=null)
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                 initDataset();
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());


        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            Log.d(TAG,"savedInstanceState");
            mProgressBar.setProgress(View.INVISIBLE);
            mTimeTableDetailsDataSet= savedInstanceState.getParcelableArrayList("KEY_DATA_SET_SHADYLE");
            mAdapter = new shaudleAdupter(mTimeTableDetailsDataSet);
            mRecyclerView.setAdapter(mAdapter);


        }else {
            //initDataset();
            Log.d(TAG,"notsavedInstanceState");
            if(mProgressBar!=null)
            mProgressBar.setVisibility(View.VISIBLE);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

      //  mAdapter = new shaudleAdupter(mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
       // mRecyclerView.setAdapter(mAdapter);


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
     try {
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
     }catch (Exception e)
     {
         e.printStackTrace();
     }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            initDataset();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putParcelableArrayList("KEY_DATA_SET_SHADYLE",  mTimeTableDetailsDataSet);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {




        new GrabeTheTimeTable().execute();

    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    private class GrabeTheTimeTable extends AsyncTask<Void, Void, Void> {

        private String [] splitByDot ;

        private String [] SplitByhash;
        private schedule schedule;
        private int matchNo=1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mTimeTableDetailsDataSet= new ArrayList<>();


        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document rssDocument = null;
                rssDocument = Jsoup.connect(getResources().getString(R.string.conn_shaudle)).timeout(6000).ignoreContentType(true).parser(Parser.xmlParser()).get();

                Element eMETA = rssDocument.select(shaduleFragment.PostId).first();

                String s = eMETA.toString();
                s = Jsoup.parse(s).text();
                Log.d(TAG,"'"+ s);
                splitByDot = s.split("!");
                Log.d(TAG,"splitby dot size = "+splitByDot.length);
                for(String splitByHash : splitByDot)
                {
                    SplitByhash = splitByHash.split("#");
                    Log.d(TAG,"size of array = "+SplitByhash.length);
                    mTimeTableDetailsDataSet.add(new schedule(SplitByhash[0],SplitByhash[1],SplitByhash[2],SplitByhash[3],SplitByhash[4],SplitByhash[5]));


                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mTimeTableDetailsDataSet.isEmpty()) {
                try {
                    Snackbar mSnackbar = Snackbar.make(getView().findViewWithTag(TAG), "No Connection For Schedule", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    new GrabeTheTimeTable().execute();


                                }
                            });
                    mSnackbar.show();
                } catch (Exception e) {

                }
            } else
            {


               try {
                   if (mSwipeRefreshLayout.isRefreshing()){
                       mSwipeRefreshLayout.setRefreshing(false);

                       mAdapter.notifyDataSetChanged();
                   }

                  else {
                      // mSwipeRefreshLayout.setRefreshing(false);
                       mProgressBar.setVisibility(View.GONE);
                        mAdapter = new shaudleAdupter(mTimeTableDetailsDataSet);
                       mRecyclerView.setAdapter(mAdapter);
                   }
               }catch (Exception e)
               {
                   e.printStackTrace();

               }
            }

        }
    }
}