package com.bussolalabs.popularmovies.custom;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alessio on 11/12/15.
 */
public class Movie implements Parcelable {

    String posterPath;
    String title;
    String overview;
    String releaseDate;
    String voteAvg;
    String id;
    boolean favorite;

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getVoteAvg() {
        return voteAvg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVoteAvg(String voteAvg) {
        this.voteAvg = voteAvg;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Movie() {}

    protected Movie(Parcel in) {
        posterPath = in.readString();
        title = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        voteAvg = in.readString();
        id = in.readString();
        favorite = in.readByte() != 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(voteAvg);
        dest.writeString(id);
        dest.writeByte((byte) (favorite ? 1 : 0));
    }
}
