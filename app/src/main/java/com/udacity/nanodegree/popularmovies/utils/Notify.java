package com.udacity.nanodegree.popularmovies.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.udacity.nanodegree.popularmovies.R;

public class Notify {

    public static void dialogOK(String message, final Activity activity, final boolean finish) {

        if (TextUtils.isEmpty(message)) {
            return;
        }

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(message);
            builder.setPositiveButton(activity.getString(R.string.text_ok),
                    new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    if (finish) {
                        activity.finish();
                    }
                }
            });
            builder.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void toast(String text, Context Activity) {

        if (TextUtils.isEmpty(text)) {

            return;
        }

        try {

            Toast.makeText(Activity, text, Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            Logger.e(e);
        }
    }
}
