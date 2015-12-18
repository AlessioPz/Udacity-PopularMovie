package com.bussolalabs.popularmovies.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.custom.Movie;
import com.bussolalabs.popularmovies.custom.SquaredImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

/**
 * Created by alessio on 23/09/15.
 */
public class MoviesAdapter extends BaseAdapter {
    private final String TAG = "MoviesAdapter";
    private final Context context;
    /*
    adapter for the grid view
    here there are the lists for the information needed
     */
    public List<Movie> movies = new ArrayList<>();

    public MoviesAdapter(Context context) {
        this.context = context;
    }


    @Override public int getCount() {
        return movies.size();
    }

    @Override public Movie getItem(int position) {
        return movies.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RelativeLayout relativeLayout = (RelativeLayout) convertView;
        if (null == relativeLayout) {
            relativeLayout = new RelativeLayout(context);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            relativeLayout.setLayoutParams(layoutParams);
        }

        SquaredImageView imageView = new SquaredImageView(context);
        imageView.setScaleType(CENTER_CROP);

        // Get the image URL for the current position.
        String url = getItem(position).getPosterPath();

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(context) //
                .load(url) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error) //
                .fit() //
                .tag(context) //
                .into(imageView);

        relativeLayout.addView(imageView);

        /*
        Put a "star" upon the banner image if the movie is saved in local favorites database
         */
        if (movies.get(position).isFavorite()) {
            ImageView imageViewStar = new ImageView(context);
            imageViewStar.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
            );
            imageViewStar.setImageResource(R.drawable.star24);
            imageViewStar.setPadding(5, 5, 0, 0);

            relativeLayout.addView(imageViewStar);
        }

        return relativeLayout;
    }

}
