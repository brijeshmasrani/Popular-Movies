package com.udacity.nanodegree.popularmovies.fragment;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
    ImageView imageFavourite, imageView, imageBack;
    Toolbar toolbar;
    static DetailFragment instance;

    public static DetailFragment getInstance() {
        if (instance == null)
            instance = new DetailFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    private void getData() {
        Result data = getArguments().getParcelable("data");
        if (data != null) {

            String posterPath = CommonUtils.getImageURL(data.getPosterPath());
            Picasso.with(getActivity()).load(posterPath).error(R.drawable.ic_poster_placeholder)
                    .placeholder(R.drawable.ic_poster_placeholder).into(imageView);

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

            if (imageView != null) {
                imageView.setLayoutParams(layoutParams);
            }
            String ratingText = String.valueOf(data.getVoteAverage()) + getString(R.string.rating_outof10);

            txt_name.setText(data.getOriginalTitle());
            txt_description.setText(data.getOverview());
            txt_rating.setText(ratingText);

            String releaseDate = data.getReleaseDate();
            String[] releaseDateTemp = releaseDate.split("-");
            String releaseYear = releaseDateTemp[0];
            txt_year.setText(releaseYear);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpView();
        getData();
    }

    private void setUpView() {

        txt_name = (TextView) getView().findViewById(R.id.detail_name);
        txt_year = (TextView) getView().findViewById(R.id.detail_year);
        txt_rating = (TextView) getView().findViewById(R.id.detail_rating);
        txt_description = (TextView) getView().findViewById(R.id.detail_description);
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        imageView = (ImageView) getView().findViewById(R.id.imgPosterDetail);

        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar();
            getActivity().getActionBar().setHomeButtonEnabled(true);
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imageFavourite = (ImageView) getView().findViewById(R.id.imgFav);

        imageFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageFavourite.getTag() == getString(R.string.tag_selected)) {
                    imageFavourite.setImageResource(R.drawable.ic_fav_border);
                    imageFavourite.setTag(getString(R.string.tag_notselected));
                } else {
                    imageFavourite.setImageResource(R.drawable.ic_fav_filled);
                    imageFavourite.setTag(getString(R.string.tag_selected));
                }
            }
        });

        imageBack = (ImageView) getView().findViewById(R.id.imgBackPress);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        Window window = ((MainActivity) getActivity()).getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setStatusBarColor(getActivity().getColor(R.color.colorPrimaryDark));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }
}
