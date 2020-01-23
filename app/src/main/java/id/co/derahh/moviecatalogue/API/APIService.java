package id.co.derahh.moviecatalogue.API;

import id.co.derahh.moviecatalogue.model.movie.MovieResult;
import id.co.derahh.moviecatalogue.model.tvShow.TvShowResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("movie/now_playing")
    Call<MovieResult> getAllMovie(
            @Query("api_key") String key,
            @Query("language") String language
    );

    @GET("tv/airing_today")
    Call<TvShowResult> getAllTvShow(
            @Query("api_key") String key,
            @Query("language") String language
    );

    @GET("search/movie")
    Call<MovieResult> getSearchMovie(
            @Query("api_key") String key,
            @Query("language") String language,
            @Query("query") String query
    );
}
