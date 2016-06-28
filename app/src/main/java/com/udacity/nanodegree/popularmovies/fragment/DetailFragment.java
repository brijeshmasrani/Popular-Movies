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
import com.udacity.nanodegree.popularmovies.models.Result;
import com.udacity.nanodegree.popularmovies.realm.Favorites;
import com.udacity.nanodegree.popularmovies.utils.CommonUtils;

import io.realm.Realm;

public class DetailFragment extends Fragment {
    private static final String TAG = "Detail";
    TextView txt_name, txt_year, txt_rating, txt_description;
    ImageView posterImage;
    Toolbar toolbar;
    Realm realm;
    Result result;

    public DetailFragment() {
    }

    public static DetailFragment getInstance(Result result, boolean isFavorite) {
        Bundle args = new Bundle();
        args.putParcelable("data", result);
        args.putBoolean("isFavorite", isFavorite);

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

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setTitle("");
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
        boolean isFavorite = getArguments().getBoolean("isFavorite");
        if (isFavorite){
            menu.findItem(R.id.menu_fav).setChecked(true).setIcon(R.drawable.ic_fav_filled);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_fav:
                final boolean isFav;

                if (item.isChecked()) {
                    item.setIcon(R.drawable.ic_fav_border);
                    item.setChecked(false);
                    isFav = false;
                } else {
                    item.setIcon(R.drawable.ic_fav_filled);
                    item.setChecked(true);
                    isFav = true;
                }

                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Favorites favorites = new Favorites();
                        favorites.setFav(isFav);
                        favorites.setId(result.getId());

                        realm.copyToRealmOrUpdate(favorites);
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getData() {
        result = getArguments().getParcelable("data");

        if (result != null) {

            String posterPath = CommonUtils.getImageURL(result.getPosterPath());
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

            txt_name.setText(result.getOriginalTitle());
            txt_description.setText(result.getOverview());

            String ratingText = String.valueOf(result.getVoteAverage()) + getString(R.string.rating_outof10);
            txt_rating.setText(ratingText);

            String releaseDate = result.getReleaseDate();
            String[] releaseDateTemp = releaseDate.split("-");
            String releaseYear = releaseDateTemp[0];
            txt_year.setText(releaseYear);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(realm != null && !realm.isClosed())
            realm.close();
    }
}
