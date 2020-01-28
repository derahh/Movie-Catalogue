package id.co.derahh.moviecatalogue.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import id.co.derahh.moviecatalogue.API.APIClient;
import id.co.derahh.moviecatalogue.Util.LanguageFormater;
import id.co.derahh.moviecatalogue.database.model.movie.MovieResult;
import id.co.derahh.moviecatalogue.database.model.tvShow.TvShowResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {

    private static final String TAG = MovieViewModel.class.getSimpleName();
    private static final String API_KEY = "06b9cd349f041c2e51292a90868062fc";
    private MutableLiveData<MovieResult> dataSearchMovies = new MutableLiveData<>();
    private MutableLiveData<TvShowResult> dataSearchTvShows = new MutableLiveData<>();

    private APIClient api = APIClient.getInstance();

    public void searchMovie(String query) {
        Log.d(TAG, "Running");

        api.getAPI().getSearchMovie(API_KEY, LanguageFormater.checkCurrentLanguage(), query).enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                dataSearchMovies.postValue(response.body());
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {

            }
        });
    }

    public LiveData<MovieResult> getSearchMovie() {
        return dataSearchMovies;
    }

    public void searchTvShow(String query) {
        Log.d(TAG, "Running");

        api.getAPI().getSearchTvShow(API_KEY, LanguageFormater.checkCurrentLanguage(), query).enqueue(new Callback<TvShowResult>() {
            @Override
            public void onResponse(Call<TvShowResult> call, Response<TvShowResult> response) {
                dataSearchTvShows.postValue(response.body());
            }

            @Override
            public void onFailure(Call<TvShowResult> call, Throwable t) {

            }
        });
    }

    public LiveData<TvShowResult> getSearchTvShow() {
        return dataSearchTvShows;
    }
}
