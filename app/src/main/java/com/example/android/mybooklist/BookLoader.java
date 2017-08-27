package com.example.android.mybooklist;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.ArrayList;


public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {

    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return Utils.fetchBookData(mUrl);
    }
}
