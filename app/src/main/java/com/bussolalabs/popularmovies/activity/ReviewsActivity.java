package com.bussolalabs.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.async.GetReviewsAsyncTask;
import com.bussolalabs.popularmovies.fragment.ReviewDetailFragment;
import com.bussolalabs.popularmovies.util.CommonContents;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity implements ListView.OnItemClickListener{

    public ArrayAdapter mAdapter;
    String id;

    private boolean mTwoPane;

    @Bind(android.R.id.list) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        ButterKnife.bind(this);

        id = getIntent().getStringExtra("id");

        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, CommonContents.reviews);
        new GetReviewsAsyncTask().execute(this, id);

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        if (findViewById(R.id.item_detail_container_review) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("review", CommonContents.reviews.get(position));
            ReviewDetailFragment fragment = new ReviewDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container_review, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, ReviewDetailActivity.class);
            intent.putExtra("review", CommonContents.reviews.get(position));
            startActivity(intent);
        }
    }
}
