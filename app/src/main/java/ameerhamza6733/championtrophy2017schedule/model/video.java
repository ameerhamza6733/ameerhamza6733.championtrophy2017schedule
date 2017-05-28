package ameerhamza6733.championtrophy2017schedule.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AmeerHamza on 1/19/2017.
 */

public class video implements Parcelable {

    private String VideoTitle;
    private String VideoThimailUrl;
    private String VideoChannalName;
    private String videoStreemUrl;
    private boolean LiveNow;

    public video() {
        this.VideoTitle="";
    }

    public video(String videoStreemUrl) {
        this.videoStreemUrl = videoStreemUrl;
    }

    public video(String videoTitle, String videoThimailUrl, String videoChannalName, String videoStreemUrl, boolean liveNow) {
        VideoTitle = videoTitle;
        VideoThimailUrl = videoThimailUrl;
        VideoChannalName = videoChannalName;
        this.videoStreemUrl = videoStreemUrl;
        LiveNow = liveNow;
    }

    protected video(Parcel in) {
        VideoTitle = in.readString();
        VideoThimailUrl = in.readString();
        VideoChannalName = in.readString();
        videoStreemUrl = in.readString();
    }



    public static final Creator<video> CREATOR = new Creator<video>() {
        @Override
        public video createFromParcel(Parcel in) {
            return new video(in);
        }

        @Override
        public video[] newArray(int size) {
            return new video[size];
        }
    };

    public void setVideoTitle(String videoTitle) {
        VideoTitle = videoTitle;
    }

    public void setVideoThimailUrl(String videoThimailUrl) {
        VideoThimailUrl = videoThimailUrl;
    }

    public void setVideoChannalName(String videoChannalName) {
        VideoChannalName = videoChannalName;
    }
    public String getVideoTitle() {
        return VideoTitle;
    }

    public String getVideoThimailUrl() {
        return VideoThimailUrl;
    }

    public String getVideoStreemUrl() {
        return videoStreemUrl;
    }

    public boolean isLiveNow() {
        return LiveNow;
    }

    public void setLiveNow(boolean liveNow) {
        LiveNow = liveNow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(VideoTitle);
        parcel.writeString(VideoThimailUrl);
        parcel.writeString(VideoChannalName);
        parcel.writeString(videoStreemUrl);
    }
}
