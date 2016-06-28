package com.udacity.nanodegree.popularmovies.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.udacity.nanodegree.popularmovies.R;
import com.udacity.nanodegree.popularmovies.fragment.DetailFragment;
import com.udacity.nanodegree.popularmovies.fragment.HomeFragment;
import com.udacity.nanodegree.popularmovies.models.Result;
import com.udacity.nanodegree.popularmovies.realm.Favorites;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    private static final String TAG = "MainActivity";
    FragmentManager fragmentManager;
    Context mContext;
    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;

        if (savedInstanceState == null) {
            homeFragment = HomeFragment.newInstance();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment, homeFragment)
                    .commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showHomeButton();
    }

    private void showHomeButton(){
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 0 && getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (fragmentManager == null)
                    fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                return true;
        }
        return false;
    }

    public void showDetailFragment(Result result, View posterImage) {
        Boolean isFavorite = false;
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Favorites> all = realm.where(Favorites.class).equalTo("isFav", true).equalTo("id", result.getId()).findAll();
        if (all.size() == 1){
            isFavorite = true;
        }

        DetailFragment detailFragment = DetailFragment.getInstance(result, isFavorite);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slideTransition = new Slide(Gravity.END);
            slideTransition.setDuration(300);

            detailFragment.setEnterTransition(slideTransition);
            detailFragment.setAllowEnterTransitionOverlap(true);
            detailFragment.setAllowReturnTransitionOverlap(true);
        }

        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        fragmentManager.beginTransaction()
                .add(R.id.fragment, detailFragment)
                .addToBackStack("")
                .addSharedElement(posterImage, getString(R.string.transition_string))
                .commit();
    }

    @Override
    public void onBackStackChanged() {
        showHomeButton();
    }
}
