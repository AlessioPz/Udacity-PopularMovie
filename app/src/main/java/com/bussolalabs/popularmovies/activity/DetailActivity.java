package com.bussolalabs.popularmovies.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.custom.Movie;
import com.bussolalabs.popularmovies.fragment.DetailFragment;
import com.bussolalabs.popularmovies.util.CommonContents;

/**
 * Created by alessio on 23/09/15.
 */public class DetailActivity extends AppCompatActivity implements DetailFragment.OnMovieUpdatedListener{
    private final String TAG = "DetailActivity";

    /*
    This activity shows the movie detail for handset device
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // get the movie selected from the grid
        Movie movie = getIntent().getParcelableExtra("movie");

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("movie", movie);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onMovieUpdated() {
        /*
        catch the event fired into the DetailFragment
        the movie data is changed (favorite state)
         */
        CommonContents.movieUpdated = true;
    }

}
