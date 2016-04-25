package com.udacity.nanodegree.popularmovies.activity;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.nanodegree.popularmovies.R;
import com.udacity.nanodegree.popularmovies.utils.CommonUtils;
import com.udacity.nanodegree.popularmovies.webservice.entity.Result;

public class DetailActivity extends AppCompatActivity {

    TextView txt_name, txt_year, txt_rating, txt_description;
    ImageView imageFavourite, backButton;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setUpView();

        if (getIntent().hasExtra("data")) {
            Result data = getIntent().getParcelableExtra("data");
            ImageView imageView = (ImageView) findViewById(R.id.imgPosterDetail);
            String posterPath = CommonUtils.getImageURL(data.getPosterPath());
            Picasso.with(getApplicationContext()).load(posterPath).into(imageView);
            txt_name = (TextView) findViewById(R.id.detail_name);

            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int viewWidth = displaymetrics.widthPixels;
            int viewHeight = displaymetrics.heightPixels;

            int orientation = getResources().getConfiguration().orientation;
            LinearLayout.LayoutParams layoutParams;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                        new int[] { android.R.attr.actionBarSize });

                int statusBarHeight = (int) (24 * getResources().getDisplayMetrics().density);
                viewHeight = viewHeight - (int) styledAttributes.getDimension(0, 0) - statusBarHeight;
                layoutParams = new LinearLayout.LayoutParams(viewHeight, viewHeight);
            } else {
                layoutParams = new LinearLayout.LayoutParams(viewWidth / 2, (int) (viewWidth * 1.5f) / 2);
            }

            if (imageView != null) {
                imageView.setLayoutParams(layoutParams);
            }

            txt_name.setText(data.getOriginalTitle());
            txt_description.setText(data.getOverview());
            txt_rating.setText(String.valueOf(data.getVoteAverage()) + getString(R.string.rating_outof10));

            String releaseDate = data.getReleaseDate();
            String[] releaseDateTemp = releaseDate.split("-");
            String releaseYear = releaseDateTemp[0];
            txt_year.setText(releaseYear);
        }

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }

    private void setUpView() {
        txt_name = (TextView) findViewById(R.id.detail_name);
        txt_year = (TextView) findViewById(R.id.detail_year);
        txt_rating = (TextView) findViewById(R.id.detail_rating);
        txt_description = (TextView) findViewById(R.id.detail_description);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageFavourite = (ImageView) findViewById(R.id.imgFav);

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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
