package com.udacity.nanodegree.popularmovies.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.udacity.nanodegree.popularmovies.R;
import com.udacity.nanodegree.popularmovies.adapter.HomeMovieAdapter;
import com.udacity.nanodegree.popularmovies.utils.AppConstants.SORT_ORDER;
import com.udacity.nanodegree.popularmovies.utils.AppPreferences;
import com.udacity.nanodegree.popularmovies.utils.Logger;
import com.udacity.nanodegree.popularmovies.utils.Notify;
import com.udacity.nanodegree.popularmovies.webservice.AppWs;
import com.udacity.nanodegree.popularmovies.webservice.entity.BaseRequest;
import com.udacity.nanodegree.popularmovies.webservice.entity.BaseResponse;
import com.udacity.nanodegree.popularmovies.webservice.entity.MoviesResponse;

public class HomeActivity extends AppCompatActivity {
    private Context mContext;
    private RecyclerView recyclerView;
    private ImageView tick_popular, tick_topRated;
    private MoviesResponse moviesResponse = new MoviesResponse();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();
    }

    void setupView() {
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.movieList);
        float dimension = getResources().getInteger(R.integer.column_count);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), (int) dimension,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mContext = HomeActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        setTitle("");

    }

    @Override
    protected void onResume() {
        super.onResume();
        String sortOrder = AppPreferences.getString(AppPreferences.HOME_SORT_PREFERENCE, SORT_ORDER.POPULAR.name());

        if (sortOrder.equalsIgnoreCase(SORT_ORDER.POPULAR.name())) {
            showPopularMovies();
        } else if (sortOrder.equalsIgnoreCase(SORT_ORDER.TOP_RATED.name())) {
            showTopRatedMovies();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("moviesResponseObject", moviesResponse);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        moviesResponse = savedInstanceState.getParcelable("moviesResponseObject");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            case R.id.menu_sort:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(R.layout.sort_dialog);

                final AlertDialog alert = builder.create();
                alert.show();

                tick_popular = (ImageView) alert.findViewById(R.id.img_popular_tick);
                tick_topRated = (ImageView) alert.findViewById(R.id.img_toprated_tick);

                String sortOrder = AppPreferences.getString(AppPreferences.HOME_SORT_PREFERENCE, SORT_ORDER.POPULAR.name());
                setTickVisibility(sortOrder);

                RelativeLayout relativeLayoutPopular = (RelativeLayout) alert.findViewById(R.id.rl_popular);
                RelativeLayout relativeLayoutTopRated = (RelativeLayout) alert.findViewById(R.id.rl_toprated);

                if (relativeLayoutPopular != null) {
                    relativeLayoutPopular.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setTickVisibility(SORT_ORDER.POPULAR.name());
                            moviesResponse = null;
                            showPopularMovies();
                            alert.dismiss();
                        }
                    });
                }

                if (relativeLayoutTopRated != null) {
                    relativeLayoutTopRated.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setTickVisibility(SORT_ORDER.TOP_RATED.name());
                            moviesResponse = null;
                            showTopRatedMovies();
                            alert.dismiss();
                        }
                    });
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void setTickVisibility(String sortOrder) {

        if (sortOrder.equalsIgnoreCase(SORT_ORDER.POPULAR.name())) {
            tick_popular.setVisibility(View.VISIBLE);
            tick_topRated.setVisibility(View.INVISIBLE);
        } else if (sortOrder.equalsIgnoreCase(SORT_ORDER.TOP_RATED.name())) {
            tick_popular.setVisibility(View.INVISIBLE);
            tick_topRated.setVisibility(View.VISIBLE);
        }
    }

    void showPopularMovies() {
        if (moviesResponse != null && moviesResponse.getTotalResults() != null) {
            setupAdapter(moviesResponse);
            AppPreferences.putString(AppPreferences.HOME_SORT_PREFERENCE, SORT_ORDER.POPULAR.name());
        } else {
            AppWs.getPopularMovies(mContext, new AppWs.WsListener() {
                @Override
                public void onResponseSuccess(BaseResponse baseResponse) {
                    if (baseResponse instanceof MoviesResponse) {
                        moviesResponse = (MoviesResponse) baseResponse;
                        setupAdapter(moviesResponse);
                        AppPreferences.putString(AppPreferences.HOME_SORT_PREFERENCE, SORT_ORDER.POPULAR.name());
                    }
                }

                @Override
                public void notifyResponseFailed(String message, BaseRequest request) {
                    Notify.toast(getString(R.string.text_tryagain), mContext);
                }
            });
        }
    }

    void showTopRatedMovies() {
        if (moviesResponse != null && moviesResponse.getTotalResults() > 0) {
            setupAdapter(moviesResponse);
            AppPreferences.putString(AppPreferences.HOME_SORT_PREFERENCE, SORT_ORDER.TOP_RATED.name());
        } else {
            AppWs.getTopRatedMovies(mContext, new AppWs.WsListener() {
                @Override
                public void onResponseSuccess(BaseResponse baseResponse) {
                    if (baseResponse instanceof MoviesResponse) {
                        moviesResponse = (MoviesResponse) baseResponse;
                        setupAdapter(moviesResponse);
                        AppPreferences.putString(AppPreferences.HOME_SORT_PREFERENCE, SORT_ORDER.TOP_RATED.name());
                    }
                }

                @Override
                public void notifyResponseFailed(String message, BaseRequest request) {
                    Notify.toast(getString(R.string.text_tryagain), mContext);
                }
            });
        }
    }

    void setupAdapter(MoviesResponse moviesResponse) {
        Logger.d("setupAdapter", "setupAdapter");
        HomeMovieAdapter movieAdapter = new HomeMovieAdapter(moviesResponse, HomeActivity.this);
        recyclerView.setAdapter(movieAdapter);
    }

   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        startActivity(new Intent(this,this.getClass()));
        finish();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        startActivity(new Intent(this,this.getClass()));
        finish();
    }*/
}
