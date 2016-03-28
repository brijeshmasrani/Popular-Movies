package com.udacity.nanodegree.popularmovies.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.nanodegree.popularmovies.R;
import com.udacity.nanodegree.popularmovies.ws.AppWs;
import com.udacity.nanodegree.popularmovies.ws.entity.BaseRequest;
import com.udacity.nanodegree.popularmovies.ws.entity.BaseResponse;
import com.udacity.nanodegree.popularmovies.ws.entity.PopularMovieResponse;
import com.udacity.nanodegree.popularmovies.ws.entity.TopRatedMovieResponse;
import com.udacity.nanodegree.popularmovies.utils.Logger;

public class HomeActivity extends AppCompatActivity {
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = HomeActivity.this;

        AppWs.getPopularMovies(mContext, new AppWs.WsListener() {
            @Override
            public void onResponseSuccess(BaseResponse baseResponse) {
                if(baseResponse instanceof PopularMovieResponse){
                    PopularMovieResponse movieResponse = (PopularMovieResponse) baseResponse;

                    Logger.e("PopularMovieResponse pages ",""+ movieResponse.getTotalPages());

                }
            }

            @Override
            public void notifyResponseFailed(String message, BaseRequest request) {

            }
        });

        AppWs.getTopRatedMovies(mContext, new AppWs.WsListener() {
            @Override
            public void onResponseSuccess(BaseResponse baseResponse) {
                if(baseResponse instanceof TopRatedMovieResponse){
                    TopRatedMovieResponse movieResponse = (TopRatedMovieResponse) baseResponse;

                    Logger.e("TopRatedMovieResponse pages ",""+ movieResponse.getTotalPages());

                }
            }

            @Override
            public void notifyResponseFailed(String message, BaseRequest request) {

            }
        });
    }
}
