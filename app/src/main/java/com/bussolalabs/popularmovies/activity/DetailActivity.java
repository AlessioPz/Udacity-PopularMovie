package com.bussolalabs.popularmovies.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.custom.Movie;
import com.bussolalabs.popularmovies.fragment.DetailFragment;
import com.bussolalabs.popularmovies.util.CommonConstants;
import com.bussolalabs.popularmovies.util.CommonContents;

/**
 * Created by alessio on 23/09/15.
 */public class DetailActivity extends AppCompatActivity
        implements DetailFragment.OnMovieUpdatedListener
{
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            if (CommonContents.videos.size() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.msg_no_share))
                        .setNeutralButton("OK", null);
                builder.show();
                return false;
            }
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String key = CommonContents.videos.get(0).getKey();
            sendIntent.putExtra(Intent.EXTRA_TEXT, getText(R.string.share_content) + " " + Uri.parse(CommonConstants.YOUTUBE_URL_WATCH + key).toString());
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, getText(R.string.share_subject));
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }
}
