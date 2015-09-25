package com.bussolalabs.popularmovies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.activity.MainActivity;

/**
 * Created by alessio on 24/09/15.
 */
public class Utility {

    public static String getUrlMovie(Context context) {
        // build the URL with user preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return CommonConstants.MOVIE_URL_PREFIX.replace("#size#",
                sharedPreferences.getString(context.getString(R.string.key_pref_size),
                        CommonConstants.THEMOVIEDB_SIZE_W185)
        );
    }

    public static String getUrlList(Context context) {
        // build the URL with user preferences and actual page
        return CommonConstants.THEMOVIEDB_URL_LIST
                .replace("#sort#",
                        PreferenceManager.getDefaultSharedPreferences(context).getString(
                                context.getString(R.string.key_pref_sort),
                                CommonConstants.THEMOVIEDB_SORT_POPULARITY_DES)
                )
                .replace("#page#", String.valueOf(((MainActivity) context).mPage));
    }
}
