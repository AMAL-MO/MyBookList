package com.example.android.mybooklist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 8/25/2017.
 */


public class Book implements Parcelable {

    private String mTitle;
    private String mAuthors;
    private String mImage;


    public Book(String title) {
        mTitle = title;
        mAuthors = "";
        mImage = "";

    }

    public Book(Parcel in) {
        readFromParcel(in);
    }

    public String getTitle() {
        return mTitle;
    }


    public String getImage() {
        return mImage;
    }

    public void setImageUrl(String image) {
        mImage = image;
    }


    public String getAuthors() {
        return mAuthors;
    }

    public void setAuthors(String authors) {
        mAuthors = authors;
    }

    public Boolean hasAuthors() {
        return !mAuthors.equals("");
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Book createFromParcel(Parcel i) {
            return new Book(i);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(mTitle);
        parcel.writeString(mAuthors);
        parcel.writeString(mImage);

    }

    private void readFromParcel(Parcel in) {

        mTitle = in.readString();
        mAuthors = in.readString();
        mImage = in.readString();

    }


}