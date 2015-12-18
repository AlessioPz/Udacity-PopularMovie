package com.bussolalabs.popularmovies.custom;

import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.AbsListView;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.activity.MainActivity;
import com.bussolalabs.popularmovies.async.GetMoviesAsyncTask;
import com.bussolalabs.popularmovies.util.CommonConstants;
import com.squareup.picasso.Picasso;

public class SampleScrollListener implements AbsListView.OnScrollListener {
    private final String TAG = "SampleScrollListener";
    private final Context context;

    /*
    custom scroll listener, from Picasso's sample project
    I've added the page manager code into the onScroll method
     */

    public SampleScrollListener(Context context) {
        this.context = context;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        final Picasso picasso = Picasso.with(context);
        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            picasso.resumeTag(context);
        } else {
            picasso.pauseTag(context);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        /*
        Log.d(TAG, "onScroll - firstVisibleItem:" + firstVisibleItem);
        Log.d(TAG, "onScroll - visibleItemCount:" + visibleItemCount);
        Log.d(TAG, "onScroll - totalItemCount:" + totalItemCount);
        */
        if (totalItemCount == 0) return;

        String sort = PreferenceManager.getDefaultSharedPreferences(context).getString(
                context.getString(R.string.key_pref_sort),
                CommonConstants.THEMOVIEDB_SORT_POPULARITY_DES);
        if (CommonConstants.FILTER_FAVORITE.equals(sort)) {
            return;
        }

        // if the last items are showed on screen, I do the GET for the next page ...
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            if (((MainActivity) context).mNextPage) {

                ((MainActivity) context).mPage++;
                // ... then no new page needed
                ((MainActivity) context).mNextPage = false;
                new GetMoviesAsyncTask().execute(context, false);
            }
        } else if (!((MainActivity)context).mNextPage){
            // scrolling inside the list, new page manager available
            ((MainActivity)context).mNextPage = true;
        }
        /*
        Log.d(TAG, "onScroll - ((MainActivity) context).mPage:" + ((MainActivity) context).mPage);
        Log.d(TAG, "onScroll - ((MainActivity) context).mNextPage:" + ((MainActivity) context).mNextPage);
        Log.d(TAG, "onScroll - --------------");
        */
    }
}
