package com.udacity.nanodegree.popularmovies.webservice;

import android.content.Context;

import com.squareup.okhttp.ResponseBody;
import com.udacity.nanodegree.popularmovies.R;
import com.udacity.nanodegree.popularmovies.utils.Logger;
import com.udacity.nanodegree.popularmovies.utils.NetworkUtils;
import com.udacity.nanodegree.popularmovies.webservice.entity.BaseRequest;
import com.udacity.nanodegree.popularmovies.webservice.entity.BaseResponse;
import com.udacity.nanodegree.popularmovies.webservice.entity.MoviesResponse;
import com.udacity.nanodegree.popularmovies.webservice.retrofit.RestClient;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AppWs {

    private static final String TAG = "AppWs";

    public static void getTopRatedMovies(final Context context, final WsListener listener) {

        Call<MoviesResponse> call = RestClient.get().getTopRatedMovies(context.getString(R.string.text_API_KEY));

        try {

            call.enqueue(new Callback<MoviesResponse>() {

                @Override
                public void onResponse(Response<MoviesResponse> response, Retrofit retrofit) {
                    MoviesResponse baseResponse = response.body();


                    if (response.isSuccess()) {

                        if (listener != null) {

                            listener.onResponseSuccess(baseResponse);
                        }

                    } else if (listener != null) {

                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                        }
                        listener.notifyResponseFailed(null, null);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    retrofitError(t, listener, context);

                    listener.notifyResponseFailed(null, null);
                }

            });

        } catch (Exception e) {


            listener.notifyResponseFailed(null, null);
        }
    }

    public static void getPopularMovies(final Context context, final WsListener listener) {

        Call<MoviesResponse> call = RestClient.get().getPopularMovies(context.getString(R.string.text_API_KEY));

        try {

            call.enqueue(new Callback<MoviesResponse>() {

                @Override
                public void onResponse(Response<MoviesResponse> response, Retrofit retrofit) {
                    MoviesResponse baseResponse = response.body();

                    if (response.isSuccess()) {

                        if (listener != null) {

                            listener.onResponseSuccess(baseResponse);
                        }

                    } else {

                        if (listener != null) {
                            ResponseBody errorBody = response.errorBody();
                            if (errorBody != null) {
                            }
                            listener.notifyResponseFailed(null, null);
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    retrofitError(t, listener, context);

                    listener.notifyResponseFailed(null, null);
                }

            });

        } catch (Exception e) {


            listener.notifyResponseFailed(null, null);
        }
    }

    public static void retrofitError(Throwable t, WsListener listener, Context context) {

        if (listener != null) {
            Logger.e(t);
            listener.notifyResponseFailed(null, null);
            NetworkUtils.checkInternetConnection(context);
        }
    }

    public interface WsListener {

        void onResponseSuccess(BaseResponse baseResponse);

        void notifyResponseFailed(String message, BaseRequest request);
    }

}
