package com.bussolalabs.popularmovies.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.adapter.VideosAdapter;
import com.bussolalabs.popularmovies.util.CommonConstants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    /*
    this activity shows the videos list, for every device
     */

    public VideosAdapter mAdapter;

    @Bind(R.id.listViewVideos) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle(getIntent().getExtras().getString("title"));

        mAdapter = new VideosAdapter(this);
        mAdapter.videos = getIntent().getParcelableArrayListExtra("videos");
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*
        video selected from the list, start YouTube app (if installed)
         */
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(CommonConstants.YOUTUBE_URL_WATCH + mAdapter.videos.get(position).getKey()));
        startActivity(intent);
    }
}
