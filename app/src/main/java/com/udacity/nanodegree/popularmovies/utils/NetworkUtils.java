package com.udacity.nanodegree.popularmovies.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.udacity.nanodegree.popularmovies.R;

public class NetworkUtils {

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null
                && netInfo.isConnectedOrConnecting()
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected();
    }

    public static boolean checkInternetConnection(Context context) {
        if (!NetworkUtils.isOnline(context)) {
            if (context instanceof Activity) {
                String message = context.getString(R.string.internet_off);
                Notify.dialogOK(message, (Activity) context, false);
            }
            return false;
        }
        return true;
    }
}
