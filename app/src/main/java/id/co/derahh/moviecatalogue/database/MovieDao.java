package id.co.derahh.moviecatalogue.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import id.co.derahh.moviecatalogue.model.movie.Movie;

@Dao
public interface MovieDao {

    @Insert
    void insert(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT * FROM movie_table")
    LiveData<List<Movie>> getFavMovie();
}
