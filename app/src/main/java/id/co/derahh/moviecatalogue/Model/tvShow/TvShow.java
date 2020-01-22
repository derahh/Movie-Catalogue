package id.co.derahh.moviecatalogue.Model.tvShow;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import id.co.derahh.moviecatalogue.database.DatabaseContract.TvShowColumns;

import static android.provider.BaseColumns._ID;
import static id.co.derahh.moviecatalogue.database.DatabaseContract.getColumnDouble;
import static id.co.derahh.moviecatalogue.database.DatabaseContract.getColumnInt;
import static id.co.derahh.moviecatalogue.database.DatabaseContract.getColumnString;

public class TvShow implements Parcelable {

    @SerializedName("id")
    private int id;
    @SerializedName("vote_average")
    private double userScore;
    @SerializedName("title")
    private String title;
    @SerializedName("first_air_date")
    private String year;
    @SerializedName("overview")
    private String description;
    @SerializedName("poster_path")

    private String photo;

    public TvShow(JSONObject currentMovie) {
        try {
            int id = currentMovie.getInt("id");
            double userScore = currentMovie.getDouble("vote_average");
            String title = currentMovie.getString("name");
            String year = currentMovie.getString("first_air_date");
            String description = currentMovie.getString("overview");
            String posterPath = currentMovie.getString("poster_path");
            String photo = "https://image.tmdb.org/t/p/w342/" + posterPath;

            this.id = id;
            this.userScore = userScore;
            this.title = title;
            this.year = year;
            this.description = description;
            this.photo = photo;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TvShow(Cursor cursor){
        this.id = getColumnInt(cursor, _ID);
        this.userScore = getColumnDouble(cursor, TvShowColumns.userScore);
        this.title = getColumnString(cursor, TvShowColumns.title);
        this.year = getColumnString(cursor, TvShowColumns.year);
        this.description = getColumnString(cursor, TvShowColumns.description);
        this.photo = getColumnString(cursor, TvShowColumns.photo);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getUserScore() {
        return userScore;
    }

    public void setUserScore(double userScore) {
        this.userScore = userScore;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeDouble(this.userScore);
        dest.writeString(this.title);
        dest.writeString(this.year);
        dest.writeString(this.description);
        dest.writeString(this.photo);
    }

    public TvShow(){
    }

    private TvShow(Parcel in) {
        this.id = in.readInt();
        this.userScore = in.readDouble();
        this.title = in.readString();
        this.year = in.readString();
        this.description = in.readString();
        this.photo = in.readString();
    }

    public static final Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel source) {
            return new TvShow(source);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };
}
