package com.udacity.nanodegree.popularmovies.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Favorites extends RealmObject {

    @PrimaryKey
    Integer id;

    boolean isFav;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }
}
