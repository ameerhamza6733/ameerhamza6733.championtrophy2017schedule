package ameerhamza6733.championtrophy2017schedule.adupters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.crash.FirebaseCrash;
import com.squareup.picasso.Picasso;
import com.thefinestartist.ytpa.utils.YouTubeUrlParser;

import java.util.List;

import ameerhamza6733.championtrophy2017schedule.MainActivity;
import ameerhamza6733.championtrophy2017schedule.R;
import ameerhamza6733.championtrophy2017schedule.activitys.YoutubePlayer;
import ameerhamza6733.championtrophy2017schedule.model.video;



/**
 * Created by AmeerHamza on 2/9/2017.
 */

public class liveAdupter extends RecyclerView.Adapter<liveAdupter.ViewHolder> {
    private static final String TAG = "CustomAdapterLive";
    public static List<video> mDataSet;
    private final int AD_TYPE = 1;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public liveAdupter(List<video> dataSet) {
        mDataSet = dataSet;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = null;
        if (viewType == AD_TYPE) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.ad_layout_live, viewGroup, false);
            NativeExpressAdView adView = (NativeExpressAdView) v.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
              adView.loadAd(adRequest);

        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.live_cardvie, viewGroup, false);
        }

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element

        try {
            if (viewHolder.getItemViewType() == AD_TYPE) {

                Log.d(TAG, "ads set");

            } else {
                if (liveAdupter.mDataSet.get(viewHolder.getAdapterPosition()).isLiveNow()) {
                    viewHolder.getmLiveTextView().setVisibility(View.VISIBLE);
                } else {
                    viewHolder.getmLiveTextView().setVisibility(View.INVISIBLE);
                }


                if (liveAdupter.mDataSet.get(viewHolder.getAdapterPosition()).getVideoStreemUrl().contains(".m3u8")) {
                   // viewHolder.getmFullScreenPlayerButton().setVisibility(View.VISIBLE);

                } else {
                    viewHolder.getmFullScreenPlayerButton().setVisibility(View.INVISIBLE);
                }
                if (!mDataSet.get(position).getVideoStreemUrl().contains("youtube.com")) {
                    String t = " " + mDataSet.get(position).getVideoTitle();
                    viewHolder.getTextView().setText(t);

                } else {
                    String t = "For slow internet connection :: " + mDataSet.get(position).getVideoTitle();
                    viewHolder.getTextView().setText(t);
                }
                Picasso.with(viewHolder.getContext()).load(mDataSet.get(position).getVideoThimailUrl()).into(viewHolder.getmThimail());
                viewHolder.getmImageButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupMenu(viewHolder.getmImageButton(), viewHolder.getAdapterPosition());
                    }
                });
                viewHolder.getmFullScreenPlayerButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {


                            viewHolder.intiChromeCustumTab(mDataSet.get(viewHolder.getAdapterPosition()).getVideoStreemUrl());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });

            }

        } catch (Exception e)

        {

            e.printStackTrace();
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
        private final TextView mLiveTextView;
        private final ImageButton mImageButton;
        private final CardView mCardview;
        private final Button mFullScreenPlayerButton;


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
            this.mLiveTextView = (TextView) v.findViewById(R.id.liveTextView);
            this.mImageButton = (ImageButton) v.findViewById(R.id.item_more_button_live);
            this.mCardview = (CardView) v.findViewById(R.id.card_view);
            this.context = v.getContext();
            this.mFullScreenPlayerButton = (Button) v.findViewById(R.id.buttonplayVideoInExoplayer);
            try {
                this.mCardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       intiYoutube(getAdapterPosition());
                        //context.startActivity(new Intent(context,webView.class));
                    }
                });
            } catch (Exception e) {

                e.printStackTrace();
            }

        }

        private void intiYoutube(int adapterPosition) {
            Log.d(TAG, "Element " + getAdapterPosition() + " clicked." + liveAdupter.mDataSet.get(getAdapterPosition()).getVideoStreemUrl());


            try {
                if (mDataSet.get(getAdapterPosition()).getVideoStreemUrl().contains("youtube.com")) {

//                    Intent intent = new Intent(context, YouTubePlayerActivity.class);
//                    //  String youtubeVideoId= CustomAdapter.mDataSet.get(getAdapterPosition()).getVideoStreemUrl().replace("http://www.youtube.com/watch?v=", "https://www.youtube.com/watch?v=");//replace http with https
//                    intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, YouTubeUrlParser.getVideoId(mDataSet.get(getAdapterPosition()).getVideoStreemUrl()));
//                    intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
//                    intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
//                    intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO);
//                    intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
//                    intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                   context.startActivity(intent);
                    Intent intent = new Intent(context, YoutubePlayer.class);
                    intent.putExtra("URL", YouTubeUrlParser.getVideoId(mDataSet.get(getAdapterPosition()).getVideoStreemUrl()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    //}

                } else {
                    try {
                        if (liveAdupter.mDataSet.get(getAdapterPosition()).getVideoStreemUrl().contains(".m3u8") || liveAdupter.mDataSet.get(getAdapterPosition()).getVideoStreemUrl().endsWith(".ts")) {
                            playItemUrl(liveAdupter.mDataSet.get(getAdapterPosition()).getVideoStreemUrl());
                        }else if(liveAdupter.mDataSet.get(getAdapterPosition()).getVideoStreemUrl().contains("http://c247"))
                        {
                            intiMyWebView(liveAdupter.mDataSet.get(getAdapterPosition()).getVideoStreemUrl());
                        }
                        else {
                            intiChromeCustumTab(liveAdupter.mDataSet.get(getAdapterPosition()).getVideoStreemUrl());
                        }
                        // new FinestWebView.Builder(context).show(liveAdupter.mDataSet.get(getAdapterPosition()).getVideoStreemUrl());
                        //   String PACKAGE_NAME = "com.android.chrome";


                        // ((Activity) context).startActivityForResult(customTabsIntent.intent, 11);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }


            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        private void intiMyWebView(String streamurl)
        {

          try {
              if(streamurl.startsWith("http://c247.to/live.php?ch="))
              {
                  Toast.makeText(context,"Please wait for 30s to 1 mint once you click on play button",Toast.LENGTH_LONG).show();

                //  Intent intent= new Intent(context,webView.class);
                //  intent.putExtra("STREAM_ID",streamurl.replace("http://c247.to/live.php?ch=",""));
                  //context.startActivity(intent);
              }else {
                  intiChromeCustumTab(streamurl);
              }
          }catch (Exception e)
          {
              e.printStackTrace();
          }
        }
        private void intiChromeCustumTab(String videoStreemUrl) {
            try {
                Toast.makeText(context,"Please wait for 30s to 1 mint once you click on play button",Toast.LENGTH_LONG).show();
                String PACKAGE_NAME = "com.android.chrome";

                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                customTabsIntent.intent.setData(Uri.parse(videoStreemUrl));

                PackageManager packageManager = context.getPackageManager();
                List<ApplicationInfo> resolveInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

                for (ApplicationInfo applicationInfo : resolveInfoList) {
                    String packageName = applicationInfo.packageName;
                    if (TextUtils.equals(packageName, PACKAGE_NAME)) {
                        customTabsIntent.intent.setPackage(PACKAGE_NAME);
                        break;
                    }
                }

                customTabsIntent.launchUrl(context, Uri.parse(liveAdupter.mDataSet.get(getAdapterPosition()).getVideoStreemUrl()));

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        private void playItemUrl(String itemUrl) {
            try {
               if(isMxPlayerInstall())
               {
                   Toast.makeText(context,"Please wait for 30s to 1 mint so we can load stream ",Toast.LENGTH_LONG).show();
                   Bundle itemValues = new Bundle();
                   itemValues.putString("title", "Live stream ");
                   itemValues.putInt("position", 0);
                   itemValues.putBoolean("sticky", false);
                   itemValues.putInt("video_zoom", 0);
                   itemValues.putBoolean("secure_uri", true);

                   Intent itemForward = new Intent("android.intent.action.VIEW");
                   itemForward.putExtras(itemValues);
                   itemForward.setDataAndType(Uri.parse(itemUrl), "video/*");
                   itemForward.setPackage("com.mxtech.videoplayer.ad");
                   context.startActivity(itemForward);
               }else {

                   dowloadMxPlayerFromPlayStore();
               }
            } catch (Exception e) {
                FirebaseCrash.log("unable to start mx player");
                e.printStackTrace();
            }
        }
        private void dowloadMxPlayerFromPlayStore() {
            AlertDialog.Builder installPlayer = new AlertDialog.Builder(context);
            installPlayer.setTitle("Important");
            installPlayer.setMessage("MX Player is required to stream this channel.");
            installPlayer.setPositiveButton("Install", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface itemDialog, int j) {
                    try {
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.mxtech.videoplayer.ad"));
                       context.startActivity(intent);
                    } catch (Exception e) {
                       context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.mxtech.videoplayer.ad")));
                    }
                }
            });
            installPlayer.setNegativeButton("Cancel", null).show();
        }

        public boolean isMxPlayerInstall(){
            try {

                context.getPackageManager().getPackageInfo("com.mxtech.videoplayer.ad",1);
                Log.d(TAG,"user install the mx player");
                return true;
            }catch (Exception e)
            {
                e.printStackTrace();
                Log.d(TAG,"user install the mx player");
                return false;
            }

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

        public TextView getmLiveTextView() {
            return mLiveTextView;
        }

        public ImageButton getmImageButton() {
            return mImageButton;
        }

        public Button getmFullScreenPlayerButton() {
            return mFullScreenPlayerButton;
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
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Watch  live match on \n https://play.google.com/store/apps/details?id=" + MainActivity.appPackageName);
                    c.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    return true;


                default:
            }
            return false;
        }
    }
}
