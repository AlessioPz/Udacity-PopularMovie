package com.bussolalabs.popularmovies.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.activity.MainActivity;
import com.bussolalabs.popularmovies.custom.Movie;
import com.bussolalabs.popularmovies.db.FavoriteDbHelper;
import com.bussolalabs.popularmovies.db.PopularMoviesContract;
import com.bussolalabs.popularmovies.util.CommonConstants;
import com.bussolalabs.popularmovies.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by alessio on 24/09/15.
 */
public class GetMoviesAsyncTask extends AsyncTask<Object, Void, String> {

    private Context mContext;
    private boolean reset = true;
    private final String TAG = "GetMoviesAsyncTask";

    @Override
    protected String doInBackground(Object... params) {
        mContext = (Context)params[0];
        reset = (boolean)params[1];

        /*
        HTTP connect in order to get actual page
        if all right, return a JSON object as string
        if there is any problem, return null
         */
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response = null;

        try {
            URL url = new URL(Utility.getUrlList(mContext) + CommonConstants.THEMOVIEDB_API_KEY);
            Log.d(TAG, "doInBackground - url:" + url + "<<");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            response = buffer.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            // in order to manage the pages, the new list is always added
            // it's possible to force the reset, for instance when the sort changed
            if (reset) {
                ((MainActivity) mContext).mAdapter.movies.clear();
            }

            Log.d(TAG, "onPostExecute - s:" + s + "<<");

            // result is null, is not possible fill the list
            if (s == null) {
                Toast.makeText(mContext, mContext.getString(R.string.msg_connection_error), Toast.LENGTH_LONG).show();
                return;
            }

            FavoriteDbHelper dbHelper = new FavoriteDbHelper(mContext);

            // result parse with JSON
            JSONObject jObject = new JSONObject(s);
            JSONArray jsonArray = jObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonMovie =  jsonArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setPosterPath(Utility.getUrlMovie(mContext) + jsonMovie.getString(CommonConstants.MOVIE_POSTER_PATH));
                movie.setTitle(jsonMovie.getString(CommonConstants.MOVIE_ORIG_TITLE));
                movie.setOverview(jsonMovie.getString(CommonConstants.MOVIE_OVERVIEW));
                String relDate = jsonMovie.getString(CommonConstants.MOVIE_RELEASE_DATE);
                movie.setReleaseDate(relDate.length() > 4 ? relDate.substring(0, 4) : "n.d.");
                movie.setVoteAvg(jsonMovie.getString(CommonConstants.MOVIE_VOTE_AVG) + "/10");
                movie.setId(jsonMovie.getString(CommonConstants.MOVIE_ID));

                PopularMoviesContract.FavoriteEntry.getValues().clear();
                PopularMoviesContract.FavoriteEntry.getValues().put(PopularMoviesContract.FavoriteEntry.COLUMN_MOVIE_ID, movie.getId());
                movie.setFavorite(dbHelper.isFavorite());

                ((MainActivity) mContext).mAdapter.movies.add(movie);

            }
            // refresh grid
            ((MainActivity)mContext).mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
