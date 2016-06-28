package com.udacity.nanodegree.popularmovies;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MoviesFinder extends Application {
    static MoviesFinder _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;

        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(_instance)
                .name("movies")
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    public static MoviesFinder getInstance() {
        return _instance;
    }
}
