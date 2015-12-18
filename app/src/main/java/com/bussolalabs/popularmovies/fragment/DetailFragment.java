package com.bussolalabs.popularmovies.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.activity.ReviewsActivity;
import com.bussolalabs.popularmovies.activity.VideosActivity;
import com.bussolalabs.popularmovies.async.GetVideosAsyncTask;
import com.bussolalabs.popularmovies.custom.Movie;
import com.bussolalabs.popularmovies.db.FavoriteDbHelper;
import com.bussolalabs.popularmovies.db.PopularMoviesContract;
import com.bussolalabs.popularmovies.util.CommonConstants;
import com.bussolalabs.popularmovies.util.CommonContents;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by alessio on 17/12/15.
 */
public class DetailFragment extends Fragment implements View.OnClickListener{

    Movie movie;
    Button buttonFav;
    public ImageView imageViewYt;

    public DetailFragment () {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("movie")) {
            movie = getArguments().getParcelable("movie");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detail_content, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.img_poster);
        TextView textViewOver = (TextView) rootView.findViewById(R.id.txt_overview);
        TextView textViewTitle = (TextView)rootView.findViewById(R.id.txt_title);
        TextView textViewRelease = (TextView)rootView.findViewById(R.id.txt_release_date);
        TextView textViewVote = (TextView)rootView.findViewById(R.id.txt_vote_avg);
        imageViewYt = (ImageView) rootView.findViewById(R.id.img_youyube);
        imageViewYt.setOnClickListener(this);
        buttonFav = (Button)rootView.findViewById(R.id.btn_favorites);
        buttonFav.setOnClickListener(this);
        Button buttonRev = (Button)rootView.findViewById(R.id.btn_reviews);
        buttonRev.setOnClickListener(this);

        imageViewYt.setVisibility(View.GONE);
        CommonContents.videos.clear();
        new GetVideosAsyncTask().execute(this, movie.getId());

        Picasso.with(getContext()) //
                .load(Uri.parse(movie.getPosterPath())) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error) //
                .fit() //
                .tag(this) //
                .into(imageView);

        textViewOver.setText(movie.getOverview());
        textViewTitle.setText(movie.getTitle());
        textViewRelease.setText(movie.getReleaseDate());
        textViewVote.setText(movie.getVoteAvg());

        buttonFav.setText(getString(movie.isFavorite() ? R.string.btn_favorites_del : R.string.btn_favorites));

        return rootView;
    }

    public void videoClick() {
        /*
        when the user tap the image into the detail pane, if there is only one video, start YouTube link
        otherwise show the videos list
         */
        if (CommonContents.videos.size() == 0) return;

        if (CommonContents.videos.size() == 1) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(CommonConstants.YOUTUBE_URL_WATCH + CommonContents.videos.get(0).getKey()));
            startActivity(intent);
            return;
        }

        Intent intent = new Intent(getContext(), VideosActivity.class);
        intent.putParcelableArrayListExtra("videos", new ArrayList<>(CommonContents.videos));
        intent.putExtra("title", movie.getTitle());
        this.startActivity(intent);
    }

    public void reviewsClick() {
        /*
        show the reviews list
         */
        Intent intent = new Intent(getContext(), ReviewsActivity.class);
        intent.putExtra("id", movie.getId());
        this.startActivity(intent);
    }

    public void favoriteClick() {
        /*
        mark the movie as favorite, or remove from favorites list
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(movie.isFavorite()?R.string.msg_favorite_del:R.string.msg_favorite_add))
                .setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        PopularMoviesContract.FavoriteEntry.getValues().clear();
                        PopularMoviesContract.FavoriteEntry.getValues().put(PopularMoviesContract.FavoriteEntry.COLUMN_MOVIE_ID, movie.getId());
                        FavoriteDbHelper favoriteDbHelper = new FavoriteDbHelper(getContext());
                        if (movie.isFavorite()) {
                            if (favoriteDbHelper.deleteByMovieId() > 0) {
                                movie.setFavorite(false);
                            }
                        } else {
                            PopularMoviesContract.FavoriteEntry.getValues().put(PopularMoviesContract.FavoriteEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
                            PopularMoviesContract.FavoriteEntry.getValues().put(PopularMoviesContract.FavoriteEntry.COLUMN_OVERVIEW, movie.getOverview());
                            PopularMoviesContract.FavoriteEntry.getValues().put(PopularMoviesContract.FavoriteEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                            PopularMoviesContract.FavoriteEntry.getValues().put(PopularMoviesContract.FavoriteEntry.COLUMN_TITLE, movie.getTitle());
                            PopularMoviesContract.FavoriteEntry.getValues().put(PopularMoviesContract.FavoriteEntry.COLUMN_VOTE_AVG, movie.getVoteAvg());
                            if (favoriteDbHelper.insert() > 0) {
                                movie.setFavorite(true);
                            }
                        }
                        buttonFav.setText(getString(movie.isFavorite() ? R.string.btn_favorites_del : R.string.btn_favorites));
                        /*
                        fire the updated event to the listener
                         */
                        mCallback.onMovieUpdated();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //bye bye
                    }
                });

        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_youyube:
                videoClick();
                break;
            case R.id.btn_favorites:
                favoriteClick();
                break;
            case R.id.btn_reviews:
                reviewsClick();
                break;
        }
    }

    OnMovieUpdatedListener mCallback;

    public interface OnMovieUpdatedListener {
        public void onMovieUpdated();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMovieUpdatedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
