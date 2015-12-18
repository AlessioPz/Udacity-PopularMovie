package com.bussolalabs.popularmovies.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.activity.ReviewsActivity;
import com.bussolalabs.popularmovies.custom.Review;
import com.bussolalabs.popularmovies.util.CommonConstants;
import com.bussolalabs.popularmovies.util.CommonContents;

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
 * Created by alessio on 14/12/15.
 */
public class GetReviewsAsyncTask extends AsyncTask<Object, Void, String> {
    private final String TAG = "GetReviewsAsyncTask";

    private Context mContext = null;
    private String id = null;

    @Override
    protected String doInBackground(Object... params) {
        mContext = (Context) params[0];
        id = (String) params[1];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response = null;

        try {
            URL url = new URL(CommonConstants.THEMOVIEDB_REVIEWS.replace("#id#", id) + CommonConstants.THEMOVIEDB_API_KEY);
            Log.d(TAG, "URL:" + url);

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

            Log.d(TAG, "onPostExecute - s:" + s + "<<");

            CommonContents.reviews.clear();

            // result is null, is not possible fill the list
            if (s == null) {
                Toast.makeText(mContext, mContext.getString(R.string.msg_connection_error), Toast.LENGTH_LONG).show();
                return;
            }

            // result parse with JSON
            JSONObject jObject = new JSONObject(s);
            JSONArray jsonArray = jObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonReview =  jsonArray.getJSONObject(i);

                Review review = new Review();

                review.setAuthor(jsonReview.getString(CommonConstants.REVIEW_AUTHOR));
                review.setContent(jsonReview.getString(CommonConstants.REVIEW_CONTENT));
                review.setLink(jsonReview.getString(CommonConstants.REVIEW_URL));

                CommonContents.reviews.add(review);

            }
            ((ReviewsActivity)mContext).mAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
