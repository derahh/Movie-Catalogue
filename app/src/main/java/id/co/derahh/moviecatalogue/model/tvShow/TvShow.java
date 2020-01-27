package id.co.derahh.moviecatalogue.model.tvShow;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import static android.provider.BaseColumns._ID;
import static id.co.derahh.moviecatalogue.database.DatabaseContract.getColumnDouble;
import static id.co.derahh.moviecatalogue.database.DatabaseContract.getColumnInt;
import static id.co.derahh.moviecatalogue.database.DatabaseContract.getColumnString;

@Entity(tableName = "tv_show_table")
public class TvShow implements Parcelable {

    @SerializedName("id")
    @PrimaryKey
    private int id;
    @SerializedName("vote_average")
    private double userScore;
    @SerializedName("name")
    private String title;
    @SerializedName("first_air_date")
    private String year;
    @SerializedName("overview")
    private String description;
    @SerializedName("poster_path")
    private String posterPath;

    private String photo;

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
        return "https://image.tmdb.org/t/p/w342/" + getPosterPath();
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public TvShow() {
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
        dest.writeString(this.posterPath);
        dest.writeString(this.photo);
    }

    protected TvShow(Parcel in) {
        this.id = in.readInt();
        this.userScore = in.readDouble();
        this.title = in.readString();
        this.year = in.readString();
        this.description = in.readString();
        this.posterPath = in.readString();
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
