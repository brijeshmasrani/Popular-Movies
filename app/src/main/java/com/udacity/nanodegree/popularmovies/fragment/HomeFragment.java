package com.udacity.nanodegree.popularmovies.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.udacity.nanodegree.popularmovies.R;
import com.udacity.nanodegree.popularmovies.activity.MainActivity;
import com.udacity.nanodegree.popularmovies.adapter.HomeMovieAdapter;
import com.udacity.nanodegree.popularmovies.utils.AppConstants;
import com.udacity.nanodegree.popularmovies.utils.AppPreferences;
import com.udacity.nanodegree.popularmovies.utils.Logger;
import com.udacity.nanodegree.popularmovies.utils.Notify;
import com.udacity.nanodegree.popularmovies.webservice.AppWs;
import com.udacity.nanodegree.popularmovies.webservice.entity.BaseRequest;
import com.udacity.nanodegree.popularmovies.webservice.entity.BaseResponse;
import com.udacity.nanodegree.popularmovies.webservice.entity.MoviesResponse;

public class HomeFragment extends Fragment {
    private Context mContext;
    private RecyclerView recyclerView;
    private ImageView tick_popular, tick_topRated;
    private MoviesResponse moviesResponse = new MoviesResponse();

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            moviesResponse = savedInstanceState.getParcelable("moviesResponseObject");
        }
        mContext = getActivity();

        setupView();
    }

    private void setupView() {
        //noinspection ConstantConditions
        recyclerView = (RecyclerView) getView().findViewById(R.id.movieList);
        float dimension = getResources().getInteger(R.integer.column_count);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), (int) dimension,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        ((MainActivity) mContext).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        String sortOrder = AppPreferences.getString(AppPreferences.HOME_SORT_PREFERENCE, AppConstants.SORT_ORDER.POPULAR.name());

        if (sortOrder.equalsIgnoreCase(AppConstants.SORT_ORDER.POPULAR.name())) {
            showPopularMovies();
        } else if (sortOrder.equalsIgnoreCase(AppConstants.SORT_ORDER.TOP_RATED.name())) {
            showTopRatedMovies();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("moviesResponseObject", moviesResponse);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(R.layout.sort_dialog);

                final AlertDialog alert = builder.create();
                alert.show();

                tick_popular = (ImageView) alert.findViewById(R.id.img_popular_tick);
                tick_topRated = (ImageView) alert.findViewById(R.id.img_toprated_tick);

                String sortOrder = AppPreferences.getString(AppPreferences.HOME_SORT_PREFERENCE, AppConstants.SORT_ORDER.POPULAR.name());
                setTickVisibility(sortOrder);

                RelativeLayout relativeLayoutPopular = (RelativeLayout) alert.findViewById(R.id.rl_popular);
                RelativeLayout relativeLayoutTopRated = (RelativeLayout) alert.findViewById(R.id.rl_toprated);

                if (relativeLayoutPopular != null) {
                    relativeLayoutPopular.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setTickVisibility(AppConstants.SORT_ORDER.POPULAR.name());
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
                            setTickVisibility(AppConstants.SORT_ORDER.TOP_RATED.name());
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
        if (sortOrder.equalsIgnoreCase(AppConstants.SORT_ORDER.POPULAR.name())) {
            tick_popular.setVisibility(View.VISIBLE);
            tick_topRated.setVisibility(View.INVISIBLE);
        } else if (sortOrder.equalsIgnoreCase(AppConstants.SORT_ORDER.TOP_RATED.name())) {
            tick_popular.setVisibility(View.INVISIBLE);
            tick_topRated.setVisibility(View.VISIBLE);
        }
    }

    void showPopularMovies() {
        if (moviesResponse != null && moviesResponse.getTotalResults() != null) {
            setupAdapter(moviesResponse);
            AppPreferences.putString(AppPreferences.HOME_SORT_PREFERENCE, AppConstants.SORT_ORDER.POPULAR.name());
        } else {
            AppWs.getPopularMovies(mContext, new AppWs.WsListener() {
                @Override
                public void onResponseSuccess(BaseResponse baseResponse) {
                    if (baseResponse instanceof MoviesResponse) {
                        moviesResponse = (MoviesResponse) baseResponse;
                        setupAdapter(moviesResponse);
                        AppPreferences.putString(AppPreferences.HOME_SORT_PREFERENCE, AppConstants.SORT_ORDER.POPULAR.name());
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
        if (moviesResponse != null && moviesResponse.getTotalResults() != null) {
            setupAdapter(moviesResponse);
            AppPreferences.putString(AppPreferences.HOME_SORT_PREFERENCE, AppConstants.SORT_ORDER.TOP_RATED.name());
        } else {
            AppWs.getTopRatedMovies(mContext, new AppWs.WsListener() {
                @Override
                public void onResponseSuccess(BaseResponse baseResponse) {
                    if (baseResponse instanceof MoviesResponse) {
                        moviesResponse = (MoviesResponse) baseResponse;
                        setupAdapter(moviesResponse);
                        AppPreferences.putString(AppPreferences.HOME_SORT_PREFERENCE, AppConstants.SORT_ORDER.TOP_RATED.name());
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
        HomeMovieAdapter movieAdapter = new HomeMovieAdapter(moviesResponse, mContext);
        recyclerView.setAdapter(movieAdapter);
    }
}
