package com.bussolalabs.popularmovies.custom;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alessio on 16/12/15.
 */
public class Review implements Parcelable{

    String author;
    String content;
    String link;

    public Review(){}

    protected Review(Parcel in) {
        author = in.readString();
        content = in.readString();
        link = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        String resp = author + "\n\n" + content;
        return resp.length() > 50 ? resp.substring(0, 50) + "\n[continue ...] " : resp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(link);
    }
}
