package com.bussolalabs.popularmovies.async;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.bussolalabs.popularmovies.custom.Video;
import com.bussolalabs.popularmovies.fragment.DetailFragment;
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
public class GetVideosAsyncTask extends AsyncTask<Object, Void, String> {
    private final String TAG = "GetVideosAsyncTask";

    private DetailFragment mFragment = null;
    private String id = null;

    @Override
    protected String doInBackground(Object... params) {
        mFragment = (DetailFragment) params[0];
        id = (String) params[1];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response = null;

        try {
            URL url = new URL(CommonConstants.THEMOVIEDB_VIDEOS.replace("#id#", id) + CommonConstants.THEMOVIEDB_API_KEY);
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

            // result is null, is not possible fill the list
            if (s == null) {
                //Toast.makeText(mContext, mContext.getString(R.string.msg_connection_error), Toast.LENGTH_LONG).show();
                return;
            }

            // result parse with JSON
            JSONObject jObject = new JSONObject(s);
            JSONArray jsonArray = jObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonVideo =  jsonArray.getJSONObject(i);

                Video video = new Video();
                video.setKey(jsonVideo.getString(CommonConstants.VIDEO_KEY));
                video.setName(jsonVideo.getString(CommonConstants.VIDEO_NAME));
                video.setSite(jsonVideo.getString(CommonConstants.VIDEO_SITE));
                video.setType(jsonVideo.getString(CommonConstants.VIDEO_TYPE));

                CommonContents.videos.add(video);

            }

            if (CommonContents.videos.size() > 0) {
                mFragment.imageViewYt.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mFragment.imageViewYt.setImageAlpha(180);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
