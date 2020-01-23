package id.co.derahh.moviecatalogue.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;

import id.co.derahh.moviecatalogue.API.APIClient;
import id.co.derahh.moviecatalogue.model.movie.MovieResult;
import id.co.derahh.moviecatalogue.Util.LanguageFormater;
import id.co.derahh.moviecatalogue.model.tvShow.TvShowResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewModel extends ViewModel {

    private static final String TAG = MovieViewModel.class.getSimpleName();
    private static final String API_KEY = "06b9cd349f041c2e51292a90868062fc";
    private MutableLiveData<TvShowResult> dataTvShows = new MutableLiveData<>();
    private MutableLiveData<MovieResult> dataMovies = new MutableLiveData<>();

    private APIClient api = APIClient.getInstance();

    public void setMovie() {
        Log.d(TAG, "Running");

        api.getAPI().getAllMovie(API_KEY, LanguageFormater.checkCurrentLanguage()).enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                Log.d("movieTitle", response.body().getResults().get(0).getTitle());
                dataMovies.postValue(response.body());
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    public LiveData<MovieResult> getMovie() {
        return dataMovies;
    }

    public void setTvShow() {
        Log.d(TAG, "Running");

        api.getAPI().getAllTvShow(API_KEY, LanguageFormater.checkCurrentLanguage()).enqueue(new Callback<TvShowResult>() {
            @Override
            public void onResponse(Call<TvShowResult> call, Response<TvShowResult> response) {
                dataTvShows.postValue(response.body());
            }

            @Override
            public void onFailure(Call<TvShowResult> call, Throwable t) {

            }
        });
    }

    public LiveData<TvShowResult> getTvShow() {
        return dataTvShows;
    }
}
