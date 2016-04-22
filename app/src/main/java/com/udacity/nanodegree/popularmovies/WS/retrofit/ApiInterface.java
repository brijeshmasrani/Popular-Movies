package com.udacity.nanodegree.popularmovies.ws.retrofit;

import com.udacity.nanodegree.popularmovies.ws.entity.MovieResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
/* Retrofit 2.0 */

public interface ApiInterface {
    @Headers("Content-Type: application/json")
    @GET("popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String key);

    @Headers("Content-Type: application/json")
    @GET("top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String key);
}