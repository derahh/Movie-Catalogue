package id.co.derahh.moviecatalogue.database;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import id.co.derahh.moviecatalogue.model.movie.Movie;
import id.co.derahh.moviecatalogue.model.tvShow.TvShow;

@Dao
public interface MovieDao {

    @Insert
    void insert(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT * FROM movie_table")
    LiveData<List<Movie>> getFavMovie();

    @Insert
    void insert(TvShow tvShow);

    @Delete
    void delete(TvShow tvShow);

    @Query("SELECT * FROM tv_show_table")
    LiveData<List<TvShow>> getFavTvShow();
}
