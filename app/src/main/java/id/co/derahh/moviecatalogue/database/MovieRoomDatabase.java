package id.co.derahh.moviecatalogue.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import id.co.derahh.moviecatalogue.model.movie.Movie;
import id.co.derahh.moviecatalogue.model.tvShow.TvShow;

@Database(entities = {Movie.class, TvShow.class}, version = 2)
public abstract class MovieRoomDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    private static volatile MovieRoomDatabase mInstance;

    public static MovieRoomDatabase getDatabase(final Context context) {
        if (mInstance == null) {
            synchronized (MovieRoomDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(context.getApplicationContext(),
                            MovieRoomDatabase.class, "movie_db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return mInstance;
    }
}
