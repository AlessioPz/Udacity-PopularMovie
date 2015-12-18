package com.bussolalabs.popularmovies.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bussolalabs.popularmovies.R;
import com.bussolalabs.popularmovies.custom.Review;

/**
 * Created by alessio on 17/12/15.
 */
public class ReviewDetailFragment extends Fragment {

    Review review;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("review")) {
            review = getArguments().getParcelable("review");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        container.removeAllViews();
        View rootView = inflater.inflate(R.layout.detail_review_content, container, false);

        TextView textViewAuthor = (TextView)rootView.findViewById(R.id.txt_author);
        TextView textViewLink = (TextView)rootView.findViewById(R.id.txt_link);
        TextView textViewReview = (TextView)rootView.findViewById(R.id.txt_review);

        textViewAuthor.setText(review.getAuthor());
        textViewLink.setText(review.getLink());
        textViewReview.setText(review.getContent());

        return rootView;
    }
}
