package com.bussolalabs.popularmovies.db;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.bussolalabs.popularmovies.custom.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alessio on 16/12/15.
 */
public class FavoriteDbHelper extends PopularMoviesDbHelper {

    public FavoriteDbHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
    }

    public long insert(){

        ContentValues contentValues = PopularMoviesContract.FavoriteEntry.getValues();
        Uri uri = mContext.getContentResolver().insert(PopularMoviesContract.FavoriteEntry.CONTENT_URI, contentValues);

        long newRowId = ContentUris.parseId(uri);

        return newRowId;
    }

    public List<Movie> retrieveAll () {
        List<Movie> movieList = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(
                PopularMoviesContract.FavoriteEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoriteEntry.COLUMN_MOVIE_ID)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoriteEntry.COLUMN_OVERVIEW)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoriteEntry.COLUMN_POSTER_PATH)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoriteEntry.COLUMN_RELEASE_DATE)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoriteEntry.COLUMN_TITLE)));
                movie.setVoteAvg(cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoriteEntry.COLUMN_VOTE_AVG)));
                movie.setFavorite(true);

                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return movieList;
    }

    public boolean isFavorite() {
        Cursor cursor = mContext.getContentResolver().query(
                PopularMoviesContract.FavoriteEntry.CONTENT_URI,
                null,
                PopularMoviesContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{PopularMoviesContract.FavoriteEntry.getValues().getAsString(PopularMoviesContract.FavoriteEntry.COLUMN_MOVIE_ID)},
                null
        );

        boolean resp = cursor.moveToFirst();
        cursor.close();
        return resp;
    }

    public int deleteByMovieId() {
        int rowsDeleted = mContext.getContentResolver().delete(
                PopularMoviesContract.FavoriteEntry.CONTENT_URI,
                PopularMoviesContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ?",
                new String[] {PopularMoviesContract.FavoriteEntry.getValues().getAsString(PopularMoviesContract.FavoriteEntry.COLUMN_MOVIE_ID)}
        );
        return rowsDeleted;
    }
}
