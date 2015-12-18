package com.bussolalabs.popularmovies.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupMenu;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.adapter.MoviesAdapter;
import com.bussolalabs.popularmovies.async.GetMoviesAsyncTask;
import com.bussolalabs.popularmovies.custom.Movie;
import com.bussolalabs.popularmovies.custom.SampleScrollListener;
import com.bussolalabs.popularmovies.custom.SettingsActivity;
import com.bussolalabs.popularmovies.db.FavoriteDbHelper;
import com.bussolalabs.popularmovies.db.PopularMoviesContract;
import com.bussolalabs.popularmovies.fragment.DetailFragment;
import com.bussolalabs.popularmovies.util.CommonConstants;
import com.bussolalabs.popularmovies.util.CommonContents;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by alessio on 23/09/15.
 */
public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener,
        PopupMenu.OnMenuItemClickListener,
        DetailFragment.OnMovieUpdatedListener
{

    // adapter for the grid view
    public MoviesAdapter mAdapter;
    // page counter for the movie GET
    public int mPage = 1;
    // flag to understand if I already got a new page
    public boolean mNextPage = true;

    private final String TAG = "MainActivity";

    @Bind(R.id.grid_view) GridView gridView;

    @BindString(R.string.key_pref_sort) String stringPrefSort;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // preference for sort mode and picture's size
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, true);

        mAdapter = new MoviesAdapter(this);
        gridView.setAdapter(mAdapter);
        gridView.setOnScrollListener(new SampleScrollListener(this));
        gridView.setOnItemClickListener(this);

        if (savedInstanceState != null)
        {
            /*
            after the screen rotation, I get saved list
             */
            mAdapter.movies = (List<Movie>)savedInstanceState.get("MOVIE_KEY");
        } else {
            /*
            no previously action, fill new list
             */
            getMovieList();
        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
        for handset device, single pane
        if the movie data is changed into DetailActivity, refresh list for favorite states
         */
        if (CommonContents.movieUpdated) {
            String sort = PreferenceManager.getDefaultSharedPreferences(this).getString(
                    getString(R.string.key_pref_sort),
                    CommonConstants.THEMOVIEDB_SORT_POPULARITY_DES);
            if (CommonConstants.FILTER_FAVORITE.equals(sort)) {
                // retrieve only favorite movies from local db
                mAdapter.movies = new FavoriteDbHelper(this).retrieveAll();
            } else {
                /*
                update favorite state for the movie updated
                 */
                String id = PopularMoviesContract.FavoriteEntry.getValues().getAsString(PopularMoviesContract.FavoriteEntry.COLUMN_MOVIE_ID);
                for (int i = 0; i < mAdapter.movies.size(); i++) {
                    if (mAdapter.movies.get(i).getId().equals(id)) {
                        mAdapter.movies.get(i).setFavorite(new FavoriteDbHelper(this).isFavorite());
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
            CommonContents.movieUpdated = false;
        }
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
        /*
        select movie from the grid
         */
        Movie movie = mAdapter.movies.get(position);
        if (mTwoPane) {
            /*
            tablet device: get fragment detail
             */
            Bundle arguments = new Bundle();
            arguments.putParcelable("movie", movie);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            /*
            handset device: open activity detail
             */
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("movie", movie);

            this.startActivity(intent);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // whe the user choose a sort mode, I update the preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        switch (item.getItemId()){
            case R.id.sort_popular:
                editor.putString(stringPrefSort, CommonConstants.THEMOVIEDB_SORT_POPULARITY_DES);
                break;
            case R.id.sort_rated:
                editor.putString(stringPrefSort, CommonConstants.THEMOVIEDB_SORT_VOTE_AVG_DES);
                break;
            case R.id.sort_favorite:
                editor.putString(stringPrefSort, CommonConstants.FILTER_FAVORITE);
                break;
        }
        editor.apply();
        editor.commit();

        // sort changed, restart from first page
        mPage = 1;
        mNextPage = true;
        // get first page
        getMovieList();

        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("MOVIE_KEY", (ArrayList<? extends Parcelable>) mAdapter.movies);
    }

    private void getMovieList() {
        /*
        fill the grid as new
         */
        String sort = PreferenceManager.getDefaultSharedPreferences(this).getString(
                getString(R.string.key_pref_sort),
                CommonConstants.THEMOVIEDB_SORT_POPULARITY_DES);
        if (CommonConstants.FILTER_FAVORITE.equals(sort)) {
            // retrieve only favorite movies from local db
            mAdapter.movies = new FavoriteDbHelper(this).retrieveAll();
            mAdapter.notifyDataSetChanged();
        } else {
            // task for the fetch of the actual movies page
            new GetMoviesAsyncTask().execute(this, true);
        }
    }

    @Override
    public void onMovieUpdated() {
        /*
        for tablet device, two pane
        the movie is updated into the detail fragment, refresh grid list
         */
        String sort = PreferenceManager.getDefaultSharedPreferences(this).getString(
                getString(R.string.key_pref_sort),
                CommonConstants.THEMOVIEDB_SORT_POPULARITY_DES);
        if (CommonConstants.FILTER_FAVORITE.equals(sort)) {
            // retrieve only favorite movies from local db
            mAdapter.movies = new FavoriteDbHelper(this).retrieveAll();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, new Fragment())
                    .commit();
        }
        mAdapter.notifyDataSetChanged();
    }


}
