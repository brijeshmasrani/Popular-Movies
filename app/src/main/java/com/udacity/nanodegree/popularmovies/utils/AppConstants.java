package com.udacity.nanodegree.popularmovies.utils;

public class AppConstants {
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE = "w185";

    public enum SORT_ORDER {
        POPULAR ("Popular"),
        TOP_RATED ("Top Rated Movies");

        private final String name;

        SORT_ORDER(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }
}
