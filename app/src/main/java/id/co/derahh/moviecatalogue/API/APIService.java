package id.co.derahh.moviecatalogue.API;

import id.co.derahh.moviecatalogue.database.model.movie.MovieResult;
import id.co.derahh.moviecatalogue.database.model.tvShow.TvShowResult;
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

    @GET("search/tv")
    Call<TvShowResult> getSearchTvShow(
            @Query("api_key") String key,
            @Query("language") String language,
            @Query("query") String query
    );

    @GET("discover/movie")
    Call<MovieResult> releaseMovie(
            @Query("api_key") String key,
            @Query("primary_release_date.gte") String date1,
            @Query("primary_release_date.lte") String date2
    );
}
