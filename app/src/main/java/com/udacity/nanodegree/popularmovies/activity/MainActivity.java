package com.udacity.nanodegree.popularmovies.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.View;

import com.udacity.nanodegree.popularmovies.R;
import com.udacity.nanodegree.popularmovies.fragment.DetailFragment;
import com.udacity.nanodegree.popularmovies.fragment.HomeFragment;
import com.udacity.nanodegree.popularmovies.webservice.entity.Result;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;

        if (savedInstanceState == null) {
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment, homeFragment)
                    .addToBackStack(null)
                    .commit();
        }
        DetailFragment detailFragment = DetailFragment.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            detailFragment.setSharedElementReturnTransition(TransitionInflater.from(
                    mContext).inflateTransition(R.transition.poster_transition));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                detailFragment.setExitTransition(TransitionInflater.from(
                        mContext).inflateTransition(android.R.transition.fade));
            }

            detailFragment.setSharedElementEnterTransition(TransitionInflater.from(
                    mContext).inflateTransition(R.transition.poster_transition));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                detailFragment.setEnterTransition(TransitionInflater.from(
                        mContext).inflateTransition(android.R.transition.fade));
            }
        }

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }
        });

    }

    public void showDetailFragment(Result result, View posterImage) {
        if (fragmentManager == null)
            fragmentManager = getSupportFragmentManager();

        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", result);
        detailFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .add(R.id.fragment, detailFragment)
                .addSharedElement(posterImage, mContext.getString(R.string.transition_string))
                .addToBackStack("")
                .commit();
    }
}
