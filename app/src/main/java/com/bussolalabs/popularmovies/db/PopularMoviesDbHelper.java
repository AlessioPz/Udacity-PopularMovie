package com.bussolalabs.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alessio on 16/12/15.
 */
public class PopularMoviesDbHelper extends SQLiteOpenHelper {

    protected final Context mContext;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PopularMovies.db";

    protected static final String TEXT_TYPE = " TEXT";
    protected static final String INTEGER_TYPE = " INTEGER";
    protected static final String COMMA_SEP = ", ";

    private final static String SQL_CREATE_FAVORITE =
            "CREATE TABLE " + PopularMoviesContract.FavoriteEntry.TABLE_NAME + " (" +
                    PopularMoviesContract.FavoriteEntry._ID + INTEGER_TYPE + " PRIMARY KEY " + COMMA_SEP +
                    PopularMoviesContract.FavoriteEntry._COUNT + INTEGER_TYPE + COMMA_SEP +
                    PopularMoviesContract.FavoriteEntry.COLUMN_MOVIE_ID + TEXT_TYPE  + COMMA_SEP +
                    PopularMoviesContract.FavoriteEntry.COLUMN_POSTER_PATH + TEXT_TYPE + COMMA_SEP +
                    PopularMoviesContract.FavoriteEntry.COLUMN_RELEASE_DATE + TEXT_TYPE + COMMA_SEP +
                    PopularMoviesContract.FavoriteEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    PopularMoviesContract.FavoriteEntry.COLUMN_VOTE_AVG + TEXT_TYPE + COMMA_SEP +
                    PopularMoviesContract.FavoriteEntry.COLUMN_OVERVIEW + TEXT_TYPE +
                    ") ";


    public PopularMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
