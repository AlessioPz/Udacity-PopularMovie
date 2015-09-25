package com.bussolalabs.popularmovies.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupMenu;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.custom.GetUrlsAsyncTask;
import com.bussolalabs.popularmovies.custom.MoviesAdapter;
import com.bussolalabs.popularmovies.custom.SampleScrollListener;
import com.bussolalabs.popularmovies.custom.SettingsActivity;
import com.bussolalabs.popularmovies.util.CommonConstants;

/**
 * Created by alessio on 23/09/15.
 */
public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, PopupMenu.OnMenuItemClickListener {

    // adapter for the grid view
    public MoviesAdapter mAdapter;
    // page counter for the movie GET
    public int mPage = 1;
    // flag to understand if I already got a new page
    public boolean mNextPage = true;

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_popmovie);

        // preference for sort mode and picture's size
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, true);

        GridView gv = (GridView) findViewById(R.id.grid_view);
        mAdapter = new MoviesAdapter(this);
        gv.setAdapter(mAdapter);
        gv.setOnScrollListener(new SampleScrollListener(this));
        gv.setOnItemClickListener(this);

        // task for the fetch of the actual movies page
        new GetUrlsAsyncTask().execute(this, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);
            return true;
        }

        if (id == R.id.action_sort) {
            PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.action_sort));
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            menuInflater.inflate(R.menu.menu_sort, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // show the detail of the selected movie
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(CommonConstants.MOVIE_POSITION, position);
        intent.putExtra(CommonConstants.MOVIE_POSTER_PATH, mAdapter.poster_paths.get(position));
        intent.putExtra(CommonConstants.MOVIE_ORIG_TITLE, mAdapter.titles.get(position));
        intent.putExtra(CommonConstants.MOVIE_OVERVIEW, mAdapter.overviews.get(position));
        intent.putExtra(CommonConstants.MOVIE_RELEASE_DATE, mAdapter.release_dates.get(position));
        intent.putExtra(CommonConstants.MOVIE_VOTE_AVG, mAdapter.vote_avg.get(position));

        this.startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // whe the user choose a sort mode, I update the preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        switch (item.getItemId()){
            case R.id.sort_popular:
                editor.putString(getString(R.string.key_pref_sort), CommonConstants.THEMOVIEDB_SORT_POPULARITY_DES);
                break;
            case R.id.sort_rated:
                editor.putString(getString(R.string.key_pref_sort), CommonConstants.THEMOVIEDB_SORT_VOTE_AVG_DES);
                break;
        }
        editor.apply();
        editor.commit();

        // sort changed, restart from first page
        mPage = 1;
        mNextPage = true;
        // get first page
        new GetUrlsAsyncTask().execute(this, true);

        return false;
    }
}
