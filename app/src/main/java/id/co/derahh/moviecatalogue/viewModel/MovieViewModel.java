package id.co.derahh.moviecatalogue.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import id.co.derahh.moviecatalogue.API.APIClient;
import id.co.derahh.moviecatalogue.Model.TvShow;
import id.co.derahh.moviecatalogue.Model.movie.Result;
import id.co.derahh.moviecatalogue.Util.LanguageFormater;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewModel extends ViewModel {

    private static final String TAG = MovieViewModel.class.getSimpleName();
    private static final String API_KEY = "06b9cd349f041c2e51292a90868062fc";
    private MutableLiveData<ArrayList<TvShow>> listDataTvShow = new MutableLiveData<>();

    private MutableLiveData<Result> dataMovies = new MutableLiveData<>();

    private APIClient api = APIClient.getInstance();

    public void setMovie() {
        Log.d(TAG, "Running");

        api.getAPI().getAllMovie(API_KEY, LanguageFormater.checkCurrentLanguage()).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.d("movieTitle", response.body().getResults().get(0).getTitle());
                dataMovies.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    public LiveData<Result> getMovie() {
        return dataMovies;
    }

    public void setTvShow() {
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

        String url = "https://api.themoviedb.org/3/tv/airing_today?api_key=" + API_KEY + "&language=" + language;
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
                    listDataTvShow.postValue(list);
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

    public LiveData<ArrayList<TvShow>> getTvShow() {
        return listDataTvShow;
    }
}
