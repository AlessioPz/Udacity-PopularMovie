package com.bussolalabs.popularmovies.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.custom.Review;
import com.bussolalabs.popularmovies.fragment.ReviewDetailFragment;

public class ReviewDetailActivity extends AppCompatActivity {

    /*
    This activity shows the review detail for handset device
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        /*
        get the review selected from the list
         */
        Review review = getIntent().getParcelableExtra("review");

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("review", review);
            ReviewDetailFragment fragment = new ReviewDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container_review, fragment)
                    .commit();
        }
    }
}
