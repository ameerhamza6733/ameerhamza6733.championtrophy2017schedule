package ameerhamza6733.championtrophy2017schedule.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AmeerHamza on 2/6/2017.
 */

public class schedule implements Parcelable {

    private String data;
    private String team1;
    private String team2;
    private String stadium;
    private String team1ImageUrl;
    private String team2ImageUrl;
    private int matchNo;

    public schedule(String data, String team1, String team2, String stadium, String team1ImageUrl, String team2ImageUrl, int matchNo) {
        this.data = data;
        this.team1 = team1;
        this.team2 = team2;
        this.stadium = stadium;
        this.team1ImageUrl = team1ImageUrl;
        this.team2ImageUrl = team2ImageUrl;
        this.matchNo = matchNo;
    }

    public schedule(String data, String team1, String team2, String stadium, String team1ImageUrl, String team2ImageUrl) {
        this.data = data;
        this.team1 = team1;
        this.team2 = team2;
        this.stadium = stadium;
        this.team1ImageUrl = team1ImageUrl;
        this.team2ImageUrl = team2ImageUrl;
    }


    protected schedule(Parcel in) {
        data = in.readString();
        team1 = in.readString();
        team2 = in.readString();
        stadium = in.readString();
        team1ImageUrl = in.readString();
        team2ImageUrl = in.readString();
        matchNo = in.readInt();
    }

    public static final Creator<schedule> CREATOR = new Creator<schedule>() {
        @Override
        public schedule createFromParcel(Parcel in) {
            return new schedule(in);
        }

        @Override
        public schedule[] newArray(int size) {
            return new schedule[size];
        }
    };

    public String getData() {
        return data;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getStadium() {
        return stadium;
    }

    public String getTeam1ImageUrl() {
        return team1ImageUrl;
    }

    public String getTeam2ImageUrl() {
        return team2ImageUrl;
    }

    public int getMatchNo() {
        return matchNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(data);
        parcel.writeString(team1);
        parcel.writeString(team2);
        parcel.writeString(stadium);
        parcel.writeString(team1ImageUrl);
        parcel.writeString(team2ImageUrl);
        parcel.writeInt(matchNo);
    }
}
