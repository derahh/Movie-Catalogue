package id.co.derahh.moviecatalogue.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import id.co.derahh.moviecatalogue.API.APIClient;
import id.co.derahh.moviecatalogue.Util.LanguageFormater;
import id.co.derahh.moviecatalogue.model.movie.Movie;
import id.co.derahh.moviecatalogue.model.movie.MovieResult;
import id.co.derahh.moviecatalogue.model.tvShow.TvShow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {

    private static final String TAG = MovieViewModel.class.getSimpleName();
    private static final String API_KEY = "06b9cd349f041c2e51292a90868062fc";
    private MutableLiveData<MovieResult> dataSearchMovies = new MutableLiveData<>();
    private MutableLiveData<List<TvShow>> listSearchTvShow = new MutableLiveData<>();

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
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShow> list = new ArrayList<>();

        String currentLanguage = Locale.getDefault().getISO3Language();
        String language = "";
        if (currentLanguage.equalsIgnoreCase("ind")) {
            language = "id-ID";
        } else if (currentLanguage.equalsIgnoreCase("eng")) {
            language = "en-US";
        }

        String url = " https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY + "&language=" + language + "&query=" + query;
        Log.e(TAG, "setTvShow: " + url);

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray movieResults = responseObject.getJSONArray("results");
                    for (int i = 0; i < movieResults.length(); i++) {
                        JSONObject currentMovie = movieResults.getJSONObject(i);
                        TvShow movie = new TvShow(currentMovie);
                        list.add(movie);
                    }
                    listSearchTvShow.postValue(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG, "onFailureTvShow: ", error);
            }
        });
    }

    public LiveData<List<TvShow>> getSearchTvShow() {
        return listSearchTvShow;
    }
}
