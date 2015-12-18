package com.bussolalabs.popularmovies.util;

/**
 * Created by alessio on 24/09/15.
 */
public class CommonConstants {

    public final static String THEMOVIEDB_API_KEY = "api_key=[INSERT YOUR API KEY HERE]";
    public final static String THEMOVIEDB_URL_LIST = "http://api.themoviedb.org/3/discover/movie?sort_by=#sort#&page=#page#&";

    public final static String THEMOVIEDB_VIDEOS = "http://api.themoviedb.org/3/movie/#id#/videos?";
    public final static String THEMOVIEDB_REVIEWS = "http://api.themoviedb.org/3/movie/#id#/reviews?";

    public final static String YOUTUBE_URL_WATCH = "https://www.youtube.com/watch?v=";
    public final static String YOUTUBE_THUMBNAIL = "http://img.youtube.com/vi/#key#/0.jpg";

    // list of possible posters size
    public final static String THEMOVIEDB_SIZE_W92 = "w92";
    public final static String THEMOVIEDB_SIZE_W154 = "w154";
    public final static String THEMOVIEDB_SIZE_W185 = "w185";
    public final static String THEMOVIEDB_SIZE_W342 = "w342";
    public final static String THEMOVIEDB_SIZE_W500 = "w500";
    public final static String THEMOVIEDB_SIZE_W780 = "w780";
    public final static String THEMOVIEDB_SIZE_ORIG = "original";

    public final static String MOVIE_URL_PREFIX = "http://image.tmdb.org/t/p/#size#/";
    public final static String MOVIE_POSITION = "position";
    public final static String MOVIE_POSTER_PATH = "poster_path";
    public final static String MOVIE_ORIG_TITLE = "original_title";
    public final static String MOVIE_OVERVIEW = "overview";
    public final static String MOVIE_RELEASE_DATE = "release_date";
    public final static String MOVIE_VOTE_AVG = "vote_average";
    public final static String MOVIE_ID = "id";

    public final static String VIDEO_KEY = "key";
    public final static String VIDEO_NAME = "name";
    public final static String VIDEO_SITE = "site";
    public final static String VIDEO_TYPE = "type";

    public final static String REVIEW_AUTHOR = "author";
    public final static String REVIEW_CONTENT = "content";
    public final static String REVIEW_URL = "url";

    // list of possible sort, only two used by now
    public final static String THEMOVIEDB_SORT_POPULARITY_ASC = "popularity.asc";
    public final static String THEMOVIEDB_SORT_POPULARITY_DES = "popularity.desc";
    public final static String THEMOVIEDB_SORT_REL_DATE_ASC = "release_date.asc";
    public final static String THEMOVIEDB_SORT_REL_DATE_DES = "release_date.desc";
    public final static String THEMOVIEDB_SORT_REVENUE_ASC = "revenue.asc";
    public final static String THEMOVIEDB_SORT_REVENUE_DES = "revenue.desc";
    public final static String THEMOVIEDB_SORT_PRI_REL_ASC = "primary_release_date.asc";
    public final static String THEMOVIEDB_SORT_PRI_REL_DES = "primary_release_date.desc";
    public final static String THEMOVIEDB_SORT_ORIG_TIT_ASC = "original_title.asc";
    public final static String THEMOVIEDB_SORT_ORIG_TIT_DES = "original_title.desc";
    public final static String THEMOVIEDB_SORT_VOTE_AVG_ASC = "vote_average.asc";
    public final static String THEMOVIEDB_SORT_VOTE_AVG_DES = "vote_average.desc";
    public final static String THEMOVIEDB_SORT_VOTE_CNT_ASC = "vote_count.asc";
    public final static String THEMOVIEDB_SORT_VOTE_CNT_DES = "vote_count.desc";

    public final static String FILTER_FAVORITE = "filter_favorite";
}
