package com.udacity.nanodegree.popularmovies;

import android.app.Application;

/**
 * Created by brijesh on 4/17/16.
 */
public class MoviesFinder extends Application {
    static MoviesFinder _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
    }

    public static MoviesFinder getInstance() {
        return _instance;
    }
}
