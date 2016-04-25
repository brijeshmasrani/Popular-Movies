package com.udacity.nanodegree.popularmovies.utils;

public class CommonUtils {

    public static String getImageURL(String posterPath){
        return AppConstants.IMAGE_BASE_URL + AppConstants.IMAGE_SIZE + posterPath;
    }
}
