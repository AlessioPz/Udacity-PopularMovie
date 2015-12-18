package com.bussolalabs.popularmovies.util;

import com.bussolalabs.popularmovies.custom.Review;
import com.bussolalabs.popularmovies.custom.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alessio on 17/12/15.
 */
public class CommonContents {

    /*
    some contents are referred from different classes, so I make it as common over the whole app
     */
    public static List<Video> videos = new ArrayList<>();
    public static List<Review> reviews = new ArrayList<>();

    public static boolean movieUpdated = false;
}
