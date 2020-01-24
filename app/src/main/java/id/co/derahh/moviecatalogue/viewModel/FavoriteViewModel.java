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
import id.co.derahh.moviecatalogue.model.tvShow.TvShow;


public class FavoriteViewModel extends AndroidViewModel {

    private MovieDao movieDao;
    private LiveData<List<Movie>> favoriteMovies;
    private LiveData<List<TvShow>> favoriteTvShow;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);

        MovieRoomDatabase movieDB = MovieRoomDatabase.getDatabase(application);
        movieDao = movieDB.movieDao();
        favoriteMovies = movieDao.getFavMovie();
        favoriteTvShow = movieDao.getFavTvShow();
    }

    public LiveData<List<Movie>> getAllFavoritMovie() {
        return favoriteMovies;
    }

    public void InsertFavorite(Movie movie) {
        new InsertMovie(movieDao).execute(movie);
    }

    public void DeleteFavorite(Movie movie) {
        new DeleteMovie(movieDao).execute(movie);
    }

    public LiveData<List<TvShow>> getAllFavoritTvShow() {
        return favoriteTvShow;
    }

    public void InsertFavorite(TvShow tvShow){
        new InsertTvShow(movieDao).execute(tvShow);
    }

    private class OperationMovie extends AsyncTask<Movie, Void, Void> {

        MovieDao mAsyncTaskDao;

        OperationMovie(MovieDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Movie... movie) {
            return null;
        }
    }

    private class InsertMovie extends OperationMovie {

        InsertMovie(MovieDao dao) {
            super(dao);
        }

        @Override
        protected Void doInBackground(Movie... movie) {
            mAsyncTaskDao.insert(movie[0]);
            return null;
        }
    }

    private class DeleteMovie extends OperationMovie {

        DeleteMovie(MovieDao dao) {
            super(dao);
        }

        @Override
        protected Void doInBackground(Movie... movie) {
            mAsyncTaskDao.delete(movie[0]);
            return null;
        }
    }

    private class OperationTvshow extends AsyncTask<TvShow, Void, Void> {

        MovieDao mAsyncTaskDao;

        OperationTvshow(MovieDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(TvShow... tvShow) {
            return null;
        }
    }

    private class InsertTvShow extends OperationTvshow {

        InsertTvShow(MovieDao dao) {
            super(dao);
        }

        @Override
        protected Void doInBackground(TvShow... tvShow) {
            mAsyncTaskDao.insert(tvShow[0]);
            return null;
        }
    }
}
