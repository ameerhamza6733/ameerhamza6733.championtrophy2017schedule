package ameerhamza6733.championtrophy2017schedule.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ameerhamza6733.championtrophy2017schedule.R;

/**
 * Created by AmeerHamza on 4/23/2017.
 */

public class liveScrFramgment extends Fragment {
    protected static final String LIVE_SCR_API_CONN = "http://ams.mapps.cricbuzz.com/cbzandroid/2.0/currentmatches.json";
    //Good Job body you finly decompile this app and get what you want next time i will use obfuscator
    private static final String TAG = "liveScrFramgment";

    private TextView mTeam1;
    private TextView mTeam2;
    private TextView mCCR;
    private TextView mTeam1Src;
    private TextView mTeam2Src;
    private TextView mOver;
    private TextView mMatchStatus;
    private TextView mPrevOvers;
    private TextView mRRR;
    private TextView mMtachNumber;
    private TextView mSriker1;
    private TextView mSriker2;


    private ImageView mImageViewteaam1, mImageViewteam2;
    private SwipeRefreshLayout mSwipeRefrashLayout;



    private RequestQueue queue;
    private Runnable updateTask;
    private Handler handler;
    private TextView mBowlerName;
    private int matchSrs;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.live_scr_card, container, false);

        matchSrs= getArguments().getInt("matchSrs");
        queue = Volley.newRequestQueue(getActivity());  // this = context
        mTeam1 = (TextView) rootView.findViewById(R.id.live_scr_team1);
        mTeam2 = (TextView) rootView.findViewById(R.id.live_scr_team_2);
        mTeam1Src = (TextView) rootView.findViewById(R.id.live_src_team1_);
        mTeam2Src = (TextView) rootView.findViewById(R.id.live_scr_team2_);
        mMatchStatus = (TextView) rootView.findViewById(R.id.tose_status);
        mPrevOvers = (TextView) rootView.findViewById(R.id.live_src_prevOvers);
        mCCR = (TextView) rootView.findViewById(R.id.crr);
        mRRR = (TextView) rootView.findViewById(R.id.rrr);
        mMatchStatus = (TextView) rootView.findViewById(R.id.live_src_match_status);
        mMtachNumber = (TextView) rootView.findViewById(R.id.live_src_mchDesc);
       mSriker1= (TextView) rootView.findViewById(R.id.striker1);
        mSriker2= (TextView) rootView.findViewById(R.id.stiker2);
        mBowlerName= (TextView) rootView.findViewById(R.id.bowler);
        mImageViewteaam1 = (ImageView) rootView.findViewById(R.id.imageView_team1);
        mImageViewteam2 = (ImageView) rootView.findViewById(R.id.imageView_team2);
        mSwipeRefrashLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.mySwipeRefreshLayoutScro);
        NativeExpressAdView adView = (NativeExpressAdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
         adView.loadAd(adRequest);
        intiVolly();
        mSwipeRefrashLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                intiVolly();

            }
        });
        liveScrFramgment.this.handler = new Handler();

        liveScrFramgment.this.updateTask = new Runnable() {
            @Override
            public void run() {

                // liveScrFramgment.this.mSwipeRefrashLayout.setRefreshing(true);
                liveScrFramgment.this.intiVolly();
                liveScrFramgment.this.handler.postDelayed(this, 10000);
            }
        };

        liveScrFramgment.this.handler.postDelayed(liveScrFramgment.this.updateTask, 10000);

        Picasso.with(getActivity()).load("https://4.bp.blogspot.com/-KIKFm19nzuU/WEVZi9yO8LI/AAAAAAAAAcI/MH442HSAIg4XuhnFBgii9QgYRoT2B9NZQCLcB/s640/IPL-T20-2015-Teams-Owners-captains.jpg").into(mImageViewteaam1);

        Picasso.with(getActivity()).load("https://4.bp.blogspot.com/-KIKFm19nzuU/WEVZi9yO8LI/AAAAAAAAAcI/MH442HSAIg4XuhnFBgii9QgYRoT2B9NZQCLcB/s640/IPL-T20-2015-Teams-Owners-captains.jpg").into(mImageViewteam2);
        intiVolly();
        return rootView;
    }

    private void intiVolly() {
        liveScrFramgment.this.mSwipeRefrashLayout.setRefreshing(true);
        JsonArrayRequest getRequest = new JsonArrayRequest(0, liveScrFramgment.LIVE_SCR_API_CONN, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response

                        Log.d(TAG, "onResponse...");

                        try {

                            if (liveScrFramgment.this.mSwipeRefrashLayout.isRefreshing())
                                liveScrFramgment.this.mSwipeRefrashLayout.setRefreshing(false);
                            JSONObject jsonObject = response.getJSONObject(matchSrs);
                            mTeam2.setText(jsonObject.getJSONObject("team2").getString("sName"));
                            mTeam1.setText(jsonObject.getJSONObject("team1").getString("sName"));
                            mMtachNumber.setText(jsonObject.getString("srs") + ", " + jsonObject.getJSONObject("header").getString("mnum"));
                            mMatchStatus.setText("Match : " + jsonObject.getJSONObject("header").getString("status"));
                            if (jsonObject.getJSONObject("header").getString("mchState").equals("inprogress") || jsonObject.getJSONObject("header").getString("mchState").equals("inningbreak")) {
//
                                mTeam2Src.setText(jsonObject.getJSONObject("miniscore").getString("bowlteamscore") + "(" + jsonObject.getJSONObject("miniscore").getString("bowlteamovers") + ")");


                                mTeam1Src.setText(jsonObject.getJSONObject("miniscore").getString("batteamscore") + "(" + jsonObject.getJSONObject("miniscore").getString("overs") + ")");
                                String s = "CRR : " + jsonObject.getJSONObject("miniscore").getString("crr");
                                mCCR.setText(s);
                                mRRR.setText("RRR : " + jsonObject.getJSONObject("miniscore").getString("rrr"));
                                mSriker1.setText(jsonObject.getJSONObject("miniscore").getJSONObject("striker").getString("fullName")+"*"+"  R : "+
                                        jsonObject.getJSONObject("miniscore").getJSONObject("striker").getString("runs") +"    B : "+ jsonObject.getJSONObject("miniscore").getJSONObject("striker").getString("balls")
                                +"    F : "+jsonObject.getJSONObject("miniscore").getJSONObject("striker").getString("fours") +"    S : "+jsonObject.getJSONObject("miniscore").getJSONObject("striker").getString("sixes"));

                                mSriker2.setText(jsonObject.getJSONObject("miniscore").getJSONObject("nonStriker").getString("fullName")+"  R : "+
                                        jsonObject.getJSONObject("miniscore").getJSONObject("striker").getString("runs") +"    B : "+ jsonObject.getJSONObject("miniscore").getJSONObject("striker").getString("balls")
                                        +"    F : "+jsonObject.getJSONObject("miniscore").getJSONObject("striker").getString("fours") +"    S : "+jsonObject.getJSONObject("miniscore").getJSONObject("striker").getString("sixes"));

//                                mStikerName.setText(jsonObject.getJSONObject("miniscore").getJSONObject("striker").getString("fullName"));
//                                mStikerR.setText(jsonObject.getJSONObject("miniscore").getJSONObject("striker").getString("runs"));
//                                mStikerF.setText(jsonObject.getJSONObject("miniscore").getJSONObject("striker").getString("fours"));
//                                mStikerB.setText(jsonObject.getJSONObject("miniscore").getJSONObject("striker").getString("balls"));
//                                mStikerS.setText(jsonObject.getJSONObject("miniscore").getJSONObject("striker").getString("sixes"));
                               mBowlerName.setText("bowlerName : " + jsonObject.getJSONObject("miniscore").getJSONObject("bowler").getString("fullName") + " overs : " +
                                       jsonObject.getJSONObject("miniscore").getJSONObject("bowler").getString("overs") + " Run : " + jsonObject.getJSONObject("miniscore").getJSONObject("bowler").getString("runs")
                                       + " wicket : " + jsonObject.getJSONObject("miniscore").getJSONObject("bowler").getString("wicket"));
                               mPrevOvers.setText("PreOvers : " + jsonObject.getJSONObject("miniscore").getString("prevOvers"));


                            } else if (jsonObject.getJSONObject("header").getString("mchState").equals("preview")) {


                            } else if (jsonObject.getJSONObject("header").getString("mchState").equals("complete")) {
                                if (!jsonObject.getJSONObject("header").getString("MOM").equals(""))
                                    mCCR.setText("MOM : " + jsonObject.getJSONObject("header").getString("MOM"));
                                //  mMatchStatus.setText("Match : "+jsonObject.getJSONObject("header").getString("status"));


                            }


                            // Log.d(TAG,"team 1 =  "+jsonObject.getJSONObject("team1").getString("sName")+jsonObject.getJSONObject("miniscore").getString("batteamscore") + "("+jsonObject.getJSONObject("miniscore").getString("overs")+")");


                            // Log.d(TAG,"r= "+jsonObject.getJSONObject("header").getString("status"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.getMessage();
                        try {
                            Toast.makeText(getActivity(), "Error in Fatching Data!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTask);
    }
}
