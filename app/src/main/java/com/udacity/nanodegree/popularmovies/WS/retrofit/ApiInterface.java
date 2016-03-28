package com.udacity.nanodegree.popularmovies.ws.retrofit;

import com.udacity.nanodegree.popularmovies.ws.entity.PopularMovieResponse;
import com.udacity.nanodegree.popularmovies.ws.entity.TopRatedMovieResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
/* Retrofit 2.0 */

public interface ApiInterface {
    @Headers("Content-Type: application/json")
    @GET("popular")
    Call<PopularMovieResponse> getPopularMovies(@Query("api_key") String key);

    @Headers("Content-Type: application/json")
    @GET("top_rated")
    Call<TopRatedMovieResponse> getTopRatedMovies(@Query("api_key") String key);
}
