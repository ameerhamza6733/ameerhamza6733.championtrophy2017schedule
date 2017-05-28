package ameerhamza6733.championtrophy2017schedule.adupters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.squareup.picasso.Picasso;

import java.util.List;

import ameerhamza6733.championtrophy2017schedule.MainActivity;
import ameerhamza6733.championtrophy2017schedule.R;
import ameerhamza6733.championtrophy2017schedule.model.schedule;

/**
 * Created by AmeerHamza on 2/7/2017.
 */

public class shaudleAdupter extends RecyclerView.Adapter<shaudleAdupter.myViewHolder> {

    private List<schedule> mDataset;
    private int AD_TYPE=2;

    public shaudleAdupter(List<schedule> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v ;


        if(viewType==AD_TYPE)
        {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ad_layout, parent, false);
           NativeExpressAdView adView = (NativeExpressAdView)v.findViewById(R.id.adView);
           AdRequest adRequest = new AdRequest.Builder().addTestDevice(MainActivity.DEvice_id).build();
      adView.loadAd(adRequest);

        }else {
            v=LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shaudle_list, parent, false);
        }


        return new myViewHolder(v);

    }

    @Override
    public int getItemViewType(int position) {
        if (position==2)
            return AD_TYPE;
        return position;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        try {
            if(holder.getItemViewType()==AD_TYPE)
            {

            }else {
                holder.getmMatchData().setText(mDataset.get(position).getData());
                holder.getMmatchstadum().setText(mDataset.get(position).getStadium());
                holder.getmTeam1().setText(mDataset.get(position).getTeam1());
                holder.getmTeam2().setText(mDataset.get(position).getTeam2());
                Log.d("onBindV",mDataset.get(position).getTeam1ImageUrl());
                Picasso.with(holder.getContext()).load(mDataset.get(position).getTeam1ImageUrl().replaceAll("\\s+","")).placeholder(R.drawable.placeholder_2).into(holder.getmTeam1Logo());
                Picasso.with(holder.getContext()).load(mDataset.get(position).getTeam2ImageUrl().replaceAll("\\s+","")).placeholder(R.drawable.placeholder_2).into(holder.getmTeam2Logo());
                holder.getmMatchDay().setText(String.valueOf(position));
            }
        }catch (Exception e)
        {

        }


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        private final TextView mMatchData;
        private final TextView mmatchstadum;
        private final TextView mTeam1;
        private final TextView mTeam2;
        private final TextView mMatchDay;
        private final ImageView mTeam1Logo;
        private final ImageView mTeam2Logo;
        private Context context;




        public myViewHolder(View itemView) {
            super(itemView);
            mMatchData= (TextView) itemView.findViewById(R.id.textViewData);
            mmatchstadum= (TextView) itemView.findViewById(R.id.textViewStadum);
            mTeam1= (TextView) itemView.findViewById(R.id.textViewTeam1);
            mTeam2= (TextView) itemView.findViewById(R.id.textViewTeam2);
            mMatchDay= (TextView) itemView.findViewById(R.id.textViewMatchNo);
            mTeam1Logo= (ImageView) itemView.findViewById(R.id.imageViewTeam1);
            mTeam2Logo= (ImageView) itemView.findViewById(R.id.imageViewTeam2);
            context=itemView.getContext();
        }

        public TextView getmMatchData() {
            return mMatchData;
        }

        public TextView getMmatchstadum() {
            return mmatchstadum;
        }

        public TextView getmTeam1() {
            return mTeam1;
        }

        public TextView getmTeam2() {
            return mTeam2;
        }

        public TextView getmMatchDay() {
            return mMatchDay;
        }

        public ImageView getmTeam1Logo() {
            return mTeam1Logo;
        }

        public ImageView getmTeam2Logo() {
            return mTeam2Logo;
        }

        public Context getContext() {
            return context;
        }
    }
}
