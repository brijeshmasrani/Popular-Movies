package com.udacity.nanodegree.popularmovies.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.udacity.nanodegree.popularmovies.R;
import com.udacity.nanodegree.popularmovies.adapter.HomeMovieAdapter;
import com.udacity.nanodegree.popularmovies.utils.Logger;
import com.udacity.nanodegree.popularmovies.ws.AppWs;
import com.udacity.nanodegree.popularmovies.ws.entity.BaseRequest;
import com.udacity.nanodegree.popularmovies.ws.entity.BaseResponse;
import com.udacity.nanodegree.popularmovies.ws.entity.MovieResponse;

public class HomeActivity extends AppCompatActivity {
    Context mContext;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.movieList);
        mContext = HomeActivity.this;

        AppWs.getPopularMovies(mContext, new AppWs.WsListener() {
            @Override
            public void onResponseSuccess(BaseResponse baseResponse) {
                if(baseResponse instanceof MovieResponse){
                    MovieResponse movieResponse = (MovieResponse) baseResponse;
                    Logger.e("PopularMovieResponse pages ",""+ movieResponse.getTotalPages());

                    HomeMovieAdapter movieAdapter = new HomeMovieAdapter(movieResponse,HomeActivity.this);
                    float dimension = getResources().getInteger(R.integer.column_count);
                    GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), (int) dimension,
                            GridLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(movieAdapter);
                }
            }

            @Override
            public void notifyResponseFailed(String message, BaseRequest request) {

            }
        });

        /*AppWs.getTopRatedMovies(mContext, new AppWs.WsListener() {
            @Override
            public void onResponseSuccess(BaseResponse baseResponse) {
                if(baseResponse instanceof MovieResponse){
                    MovieResponse movieResponse = (MovieResponse) baseResponse;

                    Logger.e("MovieResponseBean pages ",""+ movieResponse.getTotalPages());

                }
            }

            @Override
            public void notifyResponseFailed(String message, BaseRequest request) {

            }
        });*/
    }
}
