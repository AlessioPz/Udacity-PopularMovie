package com.bussolalabs.popularmovies.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.activity.VideosActivity;
import com.bussolalabs.popularmovies.custom.Video;
import com.bussolalabs.popularmovies.util.CommonConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alessio on 15/12/15.
 */
public class VideosAdapter extends BaseAdapter {

    /*
    adapter for the videos list
     */

    private final Context context;

    public List<Video> videos = new ArrayList<>();

    public VideosAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = ((VideosActivity)context).getLayoutInflater().inflate(R.layout.item_videos, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        TextView title = (TextView) rowView.findViewById(R.id.txt_title);

        Video video = videos.get(position);
        Picasso.with(context)
                .load(CommonConstants.YOUTUBE_THUMBNAIL.replace("#key#", video.getKey()))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView);
        title.setText(video.getName());

        return rowView;
    }
}
