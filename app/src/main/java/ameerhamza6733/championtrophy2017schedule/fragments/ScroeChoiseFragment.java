package ameerhamza6733.championtrophy2017schedule.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ameerhamza6733.championtrophy2017schedule.R;
import ameerhamza6733.championtrophy2017schedule.activitys.liveFragmentContanerActivty;

/**
 * Created by AmeerHamza on 5/28/2017.
 */

public class ScroeChoiseFragment extends Fragment {
    private static final String TAG = "ScroeChoiseFragment";
    private RequestQueue requestQueue;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.scroe_choise_fragment, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());
        intiVolly();
        return rootView;
    }

    private void intiVolly() {

        JsonArrayRequest getRequest = new JsonArrayRequest(0, liveScrFramgment.LIVE_SCR_API_CONN, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response

                        addRadioButtons(response);
                        // for (int i = 0 ; i<response.length();i++)


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
        requestQueue.add(getRequest);
    }

    public void addRadioButtons(JSONArray jsonArray) {


        RadioGroup rgp = (RadioGroup) rootView.findViewById(R.id.radiogroup);
        RadioGroup.LayoutParams rprms;

        for (int i = 0; i < jsonArray.length(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            try {
                radioButton.setText(jsonArray.getJSONObject(i).getString("srs"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            radioButton.setId(1000+i);

            rprms = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rgp.addView(radioButton, rprms);
        }
        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                Intent intent = new Intent(getActivity(),liveFragmentContanerActivty.class);
                intent.putExtra("srs",checkedId-1000);
                startActivity(intent);
                Log.d(TAG,"radio button id "+checkedId);
            }
        });
    }
}
