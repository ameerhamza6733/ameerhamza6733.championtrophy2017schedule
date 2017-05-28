package ameerhamza6733.championtrophy2017schedule.fragments;

/**
 * Created by AmeerHamza on 3/5/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ameerhamza6733.championtrophy2017schedule.R;
import ameerhamza6733.championtrophy2017schedule.adupters.favrateAdupter;
import ameerhamza6733.championtrophy2017schedule.model.video;
import ameerhamza6733.championtrophy2017schedule.utils.myPraf;
import ameerhamza6733.championtrophy2017schedule.utils.mySinglton;

public class FavrateVideos  extends Fragment {

    private static final String TAG = "FavrateVideos";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected favrateAdupter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List <video>mDataset;
    protected ProgressBar mPrograssBar;

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


        Log.d(TAG,"onCreateView");
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.myRecyclerView);
        mPrograssBar = (ProgressBar) rootView.findViewById(R.id.myRecylerprogressBar);
        mPrograssBar.setVisibility(View.GONE);

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
        initDataset();
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

//        mAdapter = new favrateAdupter(mDataset);
//        // Set CustomAdapter as the adapter for RecyclerView.
//        mRecyclerView.setAdapter(mAdapter);


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
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {

        }
    }



    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */


    private void initDataset() {

        mDataset = new ArrayList<>();


        try {
            Set<String> mKeysSet=myPraf.getAllPraf(mySinglton.getInStance(getContext()));
            for (String tempKey : mKeysSet)
            {
                mDataset.add(myPraf.getSavedObjectFromPreference(getContext(),tempKey,video.class));

            }
            Log.d(TAG,"mdata set size = "+mDataset.size());


            mAdapter=  new favrateAdupter(mDataset);
            mRecyclerView.setAdapter(mAdapter);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}

