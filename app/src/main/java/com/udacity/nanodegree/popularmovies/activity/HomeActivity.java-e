package com.udacity.nanodegree.popularmovies.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.udacity.nanodegree.popularmovies.R;
import com.udacity.nanodegree.popularmovies.adapter.HomeMovieAdapter;
import com.udacity.nanodegree.popularmovies.utils.AppConstants.SORT_ORDER;
import com.udacity.nanodegree.popularmovies.utils.AppPreferences;
import com.udacity.nanodegree.popularmovies.utils.Logger;
import com.udacity.nanodegree.popularmovies.utils.Notify;
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

        String sortOrder = AppPreferences.getString(AppPreferences.HOME_SORT_PREFERENCE, SORT_ORDER.POPULAR.name());

        if (sortOrder.equalsIgnoreCase(SORT_ORDER.POPULAR.name())) {
            showPopularMovies();
        } else if (sortOrder.equalsIgnoreCase(SORT_ORDER.TOP_RATED.name())) {
            showTopRatedMovies();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort:
                final CharSequence[] items = {SORT_ORDER.TOP_RATED.name(), SORT_ORDER.POPULAR.name()};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Make your selection");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        String selectedItem = (String) items[item];
                        if (selectedItem.equalsIgnoreCase(SORT_ORDER.POPULAR.name())) {
                            showPopularMovies();
                        } else if (selectedItem.equalsIgnoreCase(SORT_ORDER.TOP_RATED.name())) {
                            showTopRatedMovies();
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    void showPopularMovies() {
        AppWs.getPopularMovies(mContext, new AppWs.WsListener() {
            @Override
            public void onResponseSuccess(BaseResponse baseResponse) {
                if (baseResponse instanceof MovieResponse) {
                    MovieResponse movieResponse = (MovieResponse) baseResponse;
                    Logger.e("PopularMovieResponse pages ", "" + movieResponse.getTotalPages());
                    setupAdapter(movieResponse);
                }
            }

            @Override
            public void notifyResponseFailed(String message, BaseRequest request) {
                Notify.toast(getString(R.string.text_tryagain), mContext);
            }
        });
    }

    void showTopRatedMovies() {
        AppWs.getTopRatedMovies(mContext, new AppWs.WsListener() {
            @Override
            public void onResponseSuccess(BaseResponse baseResponse) {
                if (baseResponse instanceof MovieResponse) {
                    MovieResponse movieResponse = (MovieResponse) baseResponse;
                    Logger.e("getTopRatedMovies pages ", "" + movieResponse.getTotalPages());
                    setupAdapter(movieResponse);
                }
            }

            @Override
            public void notifyResponseFailed(String message, BaseRequest request) {
                Notify.toast(getString(R.string.text_tryagain), mContext);
            }
        });
    }


    void setupAdapter(MovieResponse movieResponse) {
        HomeMovieAdapter movieAdapter = new HomeMovieAdapter(movieResponse, HomeActivity.this);
        float dimension = getResources().getInteger(R.integer.column_count);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), (int) dimension,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
    }
}
