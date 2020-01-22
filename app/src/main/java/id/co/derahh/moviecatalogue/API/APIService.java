package id.co.derahh.moviecatalogue.API;

import id.co.derahh.moviecatalogue.Model.movie.Result;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("now_playing")
    Call<Result> getAllMovie(
            @Query("api_key") String key,
            @Query("language") String language
    );
}
