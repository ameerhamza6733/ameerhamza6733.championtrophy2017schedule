package ameerhamza6733.championtrophy2017schedule.adupters;

/**
 * Created by AmeerHamza on 3/5/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Picasso;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;
import com.thefinestartist.ytpa.utils.YouTubeUrlParser;

import java.util.List;
import java.util.Set;

import ameerhamza6733.championtrophy2017schedule.MainActivity;
import ameerhamza6733.championtrophy2017schedule.R;
import ameerhamza6733.championtrophy2017schedule.model.video;
import ameerhamza6733.championtrophy2017schedule.utils.myPraf;
import ameerhamza6733.championtrophy2017schedule.utils.mySinglton;

public class favrateAdupter extends RecyclerView.Adapter<favrateAdupter.ViewHolder> {
    private static final String TAG = "CustomAdapter";
    protected static List<video> mDataSet;
    private final int AD_TYPE = 1;
    private Set myPrafKeysSet;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public favrateAdupter(List<video> dataSet) {
        mDataSet = dataSet;


    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = null;
        if (viewType == AD_TYPE) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.ad_layout, viewGroup, false);
            NativeExpressAdView adView = (NativeExpressAdView) v.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_layout, viewGroup, false);
        }

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element

        if (myPrafKeysSet == null)
            myPrafKeysSet = myPraf.getAllPraf(mySinglton.getInStance(viewHolder.context));

        try {
            if (viewHolder.getItemViewType() == AD_TYPE) {

                Log.d(TAG, "ads set");

            } else {
//                if (CustomAdapter.mDataSet.get(viewHolder.getAdapterPosition()).isLiveNow()){
//                    viewHolder.getmLiveTextView().setVisibility(View.VISIBLE);
//                }else {
//                    viewHolder.getmLiveTextView().setVisibility(View.INVISIBLE);
//                }
                myPraf.getAllPraf(mySinglton.getInStance(viewHolder.context));

                if (myPrafKeysSet.contains(YouTubeUrlParser.getVideoId(mDataSet.get(viewHolder.getAdapterPosition()).getVideoStreemUrl())))
                    viewHolder.getmFavrateButton().setImageResource(R.mipmap.ic_favorite_black_24dp);
                viewHolder.getTextView().setText(mDataSet.get(position).getVideoTitle());
                Picasso.with(viewHolder.getContext()).load(mDataSet.get(position).getVideoThimailUrl()).placeholder(R.drawable.placeholder_2).into(viewHolder.getmThimail());
                viewHolder.getmImageButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupMenu(viewHolder.getmImageButton(), viewHolder.getAdapterPosition());
                    }
                });
                viewHolder.getmFavrateButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (!myPrafKeysSet.contains(YouTubeUrlParser.getVideoId(mDataSet.get(viewHolder.getAdapterPosition()).getVideoStreemUrl()))) {
                                myPraf.saveObjectToSharedPreference(viewHolder.context, YouTubeUrlParser.getVideoId(mDataSet.get(viewHolder.getAdapterPosition()).getVideoStreemUrl()), mDataSet.get(viewHolder.getAdapterPosition()));
                                viewHolder.getmFavrateButton().setImageResource(R.mipmap.ic_favorite_black_24dp);

                            } else {


                                myPrafKeysSet.remove(YouTubeUrlParser.getVideoId(mDataSet.get(viewHolder.getAdapterPosition()).getVideoStreemUrl()));
                                Log.d(TAG, "my praf size = " + myPraf.getPrafSize(viewHolder.context));
                                myPraf.deletePraf(viewHolder.context, YouTubeUrlParser.getVideoId(mDataSet.get(viewHolder.getAdapterPosition()).getVideoStreemUrl()));
                                viewHolder.getmFavrateButton().setImageResource(R.mipmap.ic_favorite_border_black_24dp);
                                mDataSet.remove(viewHolder.getAdapterPosition());
                                notifyItemRemoved(viewHolder.getAdapterPosition());
                                notifyDataSetChanged();
                                Log.d(TAG, "my praf size = " + myPraf.getPrafSize(viewHolder.context));
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                viewHolder.getmShareButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "Here is the share content body";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, mDataSet.get(viewHolder.getAdapterPosition()).getVideoTitle() + "\n" + mDataSet.get(viewHolder.getAdapterPosition()).getVideoStreemUrl() + "\n" + "Watch more videos and live match on \n https://play.google.com/store/apps/details?id=" + MainActivity.appPackageName);
                        viewHolder.context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    }
                });


            }

        } catch (Exception e)

        {

        }
    }

    private void showPopupMenu(ImageButton imageButton, int position) {
        PopupMenu popup = new PopupMenu(imageButton.getContext(), imageButton);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popupmenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position, imageButton.getContext()));
        popup.show();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1)
            return AD_TYPE;
        return position;
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView mThimail;
        private final Context context;
        //   private final TextView mLiveTextView;
        private final ImageButton mImageButton;
        private final CardView mCardview;
        private final ImageButton mShareButton;
        private final ImageButton mFavrateButton;


        public ViewHolder(View v) {

            super(v);
            //  Define click listener for the ViewHolder's View.
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });


            this.textView = (TextView) v.findViewById(R.id.item_title_live);
            this.mThimail = (ImageView) v.findViewById(R.id.item_thumbnail_live);
//            this.mLiveTextView = (TextView) v.findViewById(R.id.liveTextView);
            this.mImageButton = (ImageButton) v.findViewById(R.id.item_more_button_live);
            this.mCardview = (CardView) v.findViewById(R.id.card_view);
            this.mFavrateButton = (ImageButton) v.findViewById(R.id.imageButtonFavrate);
            this.mShareButton = (ImageButton) v.findViewById(R.id.imageButtonShare);
            this.context = v.getContext();
            try {
                this.mCardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intiYoutube(getAdapterPosition());
                        Log.d(TAG, "Element " + getAdapterPosition() + " clicked." + mDataSet.get(getAdapterPosition()).getVideoStreemUrl());
                    }
                });
            } catch (Exception e) {

            }

        }

        private void intiYoutube(int adapterPosition) {

            Intent intent = new Intent(context, YouTubePlayerActivity.class);
            //  String youtubeVideoId= CustomAdapter.mDataSet.get(getAdapterPosition()).getVideoStreemUrl().replace("http://www.youtube.com/watch?v=", "https://www.youtube.com/watch?v=");//replace http with https
            intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, YouTubeUrlParser.getVideoId(mDataSet.get(getAdapterPosition()).getVideoStreemUrl()));
            intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
            intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
            intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO);
            intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
            intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

//            try {
//                String PACKAGE_NAME = "com.android.chrome";
//
//                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
//                customTabsIntent.intent.setData(Uri.parse(mDataSet.get(getAdapterPosition()).getVideoStreemUrl()));
//
//                PackageManager packageManager = context.getPackageManager();
//                List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(customTabsIntent.intent, PackageManager.MATCH_DEFAULT_ONLY);
//
//                for (ResolveInfo resolveInfo : resolveInfoList) {
//                    String packageName = resolveInfo.activityInfo.packageName;
//                    if (TextUtils.equals(packageName, PACKAGE_NAME))
//                        customTabsIntent.intent.setPackage(PACKAGE_NAME);
//                }
//
//                // customTabsIntent.launchUrl(context, Uri.parse(CustomAdapter.mDataSet.get(getAdapterPosition()).getVideoStreemUrl()));
//                ((Activity) context).startActivityForResult(customTabsIntent.intent, 11);
//            }catch (Exception e)
//            {
//
//            }
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getmThimail() {
            return mThimail;
        }

        public Context getContext() {
            return context;
        }

//        public TextView getmLiveTextView() {
//            return mLiveTextView;
//        }


        public ImageButton getmShareButton() {
            return mShareButton;
        }

        public ImageButton getmFavrateButton() {
            return mFavrateButton;
        }

        public ImageButton getmImageButton() {
            return mImageButton;
        }


    }

    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;
        private Context c;

        private MyMenuItemClickListener(int positon, Context c) {
            this.position = positon;
            this.c = c;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.overflow_share:


                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Here is the share content body";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, mDataSet.get(position).getVideoTitle() + "\n" + mDataSet.get(position).getVideoStreemUrl() + "\n" + "Watch more videos and live match on \n https://play.google.com/store/apps/details?id=" + MainActivity.appPackageName);
                    c.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    return true;


                default:
            }
            return false;
        }
    }
}