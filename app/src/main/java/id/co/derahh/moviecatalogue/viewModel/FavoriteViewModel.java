package id.co.derahh.moviecatalogue.viewModel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import id.co.derahh.moviecatalogue.database.MovieDao;
import id.co.derahh.moviecatalogue.database.MovieRoomDatabase;
import id.co.derahh.moviecatalogue.model.movie.Movie;


public class FavoriteViewModel extends AndroidViewModel {

    private MovieDao movieDao;
    private LiveData<List<Movie>> favoriteMovies;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);

        MovieRoomDatabase movieDB = MovieRoomDatabase.getDatabase(application);
        movieDao = movieDB.movieDao();
        favoriteMovies = movieDao.getFavMovie();
    }

    public LiveData<List<Movie>> getAllFavoritMovie() {
        return favoriteMovies;
    }

    public void InsertFavorite(Movie movie) {
        new InsertAsyncTask(movieDao).execute(movie);
    }

    public void DeleteFavorite(Movie movie) {
        new DeleteAsyncTask(movieDao).execute(movie);
    }

    private class OperationAsyncTask extends AsyncTask<Movie, Void, Void> {

        MovieDao mAsyncTaskDao;

        OperationAsyncTask(MovieDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Movie... movie) {
            return null;
        }
    }

    private class InsertAsyncTask extends OperationAsyncTask {

        InsertAsyncTask(MovieDao dao) {
            super(dao);
        }

        @Override
        protected Void doInBackground(Movie... movie) {
            mAsyncTaskDao.insert(movie[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends OperationAsyncTask {

        DeleteAsyncTask(MovieDao dao) {
            super(dao);
        }

        @Override
        protected Void doInBackground(Movie... movie) {
            mAsyncTaskDao.delete(movie[0]);
            return null;
        }
    }
}
