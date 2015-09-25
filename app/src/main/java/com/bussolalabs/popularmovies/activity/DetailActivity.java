package com.bussolalabs.popularmovies.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.util.CommonConstants;
import com.squareup.picasso.Picasso;

/**
 * Created by alessio on 23/09/15.
 */public class DetailActivity extends AppCompatActivity {
    private final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String url = getIntent().getStringExtra(CommonConstants.MOVIE_POSTER_PATH);
        Log.d(TAG, "onCreate - url:" + url + "<<");

        Picasso.with(this) //
                .load(Uri.parse(url)) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error) //
                .fit() //
                .tag(this) //
                .into((ImageView) findViewById(R.id.img_poster));

        ((TextView)findViewById(R.id.txt_overview)).setText(getIntent().getStringExtra(CommonConstants.MOVIE_OVERVIEW));
        ((TextView)findViewById(R.id.txt_title)).setText(getIntent().getStringExtra(CommonConstants.MOVIE_ORIG_TITLE));
        ((TextView)findViewById(R.id.txt_release_date)).setText(getIntent().getStringExtra(CommonConstants.MOVIE_RELEASE_DATE));
        ((TextView)findViewById(R.id.txt_vote_avg)).setText(getIntent().getStringExtra(CommonConstants.MOVIE_VOTE_AVG));
    }

}
