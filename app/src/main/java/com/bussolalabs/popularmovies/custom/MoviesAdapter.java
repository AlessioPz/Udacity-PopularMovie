package com.bussolalabs.popularmovies.custom;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bussolalabs.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

/**
 * Created by alessio on 23/09/15.
 */
public class MoviesAdapter extends BaseAdapter {
    private final Context context;
    /*
    adapter for the grid view
    here there are the lists for the information needed
     */
    public List<String> poster_paths = new ArrayList<>();
    public List<String> titles = new ArrayList<>();
    public List<String> overviews = new ArrayList<>();
    public List<String> release_dates = new ArrayList<>();
    public List<String> vote_avg = new ArrayList<>();

    public MoviesAdapter(Context context) {
        this.context = context;
    }


    @Override public int getCount() {
        return poster_paths.size();
    }

    @Override public String getItem(int position) {
        return poster_paths.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I get this code from the Picasso's sample project
        SquaredImageView view = (SquaredImageView) convertView;
        if (view == null) {
            view = new SquaredImageView(context);
            view.setScaleType(CENTER_CROP);
        }

        // Get the image URL for the current position.
        String url = getItem(position);

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(context) //
                .load(url) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error) //
                .fit() //
                .tag(context) //
                .into(view);

        return view;
    }

}
