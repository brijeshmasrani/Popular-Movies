package com.udacity.nanodegree.popularmovies.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.nanodegree.popularmovies.R;
import com.udacity.nanodegree.popularmovies.activity.MainActivity;
import com.udacity.nanodegree.popularmovies.utils.CommonUtils;
import com.udacity.nanodegree.popularmovies.webservice.entity.Result;

public class DetailFragment extends Fragment {
    TextView txt_name, txt_year, txt_rating, txt_description;
    ImageView posterImage;
    Toolbar toolbar;

    public DetailFragment() {
    }

    public static DetailFragment getInstance(Result result) {
        Bundle args = new Bundle();
        args.putParcelable("data", result);

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpView();
        getData();
    }

    private void setUpView() {
        Context mContext = getActivity();

        txt_name = (TextView) getView().findViewById(R.id.detail_name);
        txt_year = (TextView) getView().findViewById(R.id.detail_year);
        txt_rating = (TextView) getView().findViewById(R.id.detail_rating);
        txt_description = (TextView) getView().findViewById(R.id.detail_description);
        toolbar = (Toolbar) getView().findViewById(R.id.toolbarDetail);
        posterImage = (ImageView) getView().findViewById(R.id.imgPosterDetail);

        ((MainActivity) mContext).setSupportActionBar(toolbar);
        toolbar.setTitle("");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_fav:
                if (item.isChecked()) {
                    item.setIcon(R.drawable.ic_fav_border);
                    item.setChecked(false);
                } else {
                    item.setIcon(R.drawable.ic_fav_filled);
                    item.setChecked(true);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getData() {
        Result data = getArguments().getParcelable("data");
        if (data != null) {

            String posterPath = CommonUtils.getImageURL(data.getPosterPath());
            Picasso.with(getActivity()).load(posterPath).error(R.drawable.ic_poster_placeholder)
                    .placeholder(R.drawable.ic_poster_placeholder).into(posterImage);

            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int viewWidth = displaymetrics.widthPixels;
            int viewHeight = displaymetrics.heightPixels;

            int orientation = getResources().getConfiguration().orientation;
            LinearLayout.LayoutParams layoutParams;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                final TypedArray styledAttributes = getActivity().getTheme().obtainStyledAttributes(
                        new int[]{android.R.attr.actionBarSize});

                int statusBarHeight = (int) (24 * getResources().getDisplayMetrics().density);
                viewHeight = viewHeight - (int) styledAttributes.getDimension(0, 0) - statusBarHeight;
                layoutParams = new LinearLayout.LayoutParams(viewWidth, viewHeight);
            } else {
                layoutParams = new LinearLayout.LayoutParams(viewWidth / 2, (int) (viewWidth * 1.5f) / 2);
            }

            posterImage.setLayoutParams(layoutParams);

            txt_name.setText(data.getOriginalTitle());
            txt_description.setText(data.getOverview());

            String ratingText = String.valueOf(data.getVoteAverage()) + getString(R.string.rating_outof10);
            txt_rating.setText(ratingText);

            String releaseDate = data.getReleaseDate();
            String[] releaseDateTemp = releaseDate.split("-");
            String releaseYear = releaseDateTemp[0];
            txt_year.setText(releaseYear);
        }
    }
}
