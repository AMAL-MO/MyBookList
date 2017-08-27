package com.example.android.mybooklist;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
/**
 * Created by user on 8/25/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    static class ViewHolder {
        ImageView bookCoverImageView;
        TextView titleTextView;
        TextView authorsTextView;
    }

    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.bookCoverImageView =  convertView.findViewById(R.id.Book_cover);
            viewHolder.titleTextView =  convertView.findViewById(R.id.BookTitle);
            viewHolder.authorsTextView = convertView.findViewById(R.id.BookAuthor);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Book book = getItem(position);

        viewHolder.bookCoverImageView.setVisibility(View.INVISIBLE);
        viewHolder.titleTextView.setText(book.getTitle());
        if (book.hasAuthors()) {
            viewHolder.authorsTextView.setText("by " + book.getAuthors());
            viewHolder.authorsTextView.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.authorsTextView.setVisibility(View.GONE);
        }
        new DownloadImageAsyncTask(viewHolder.bookCoverImageView)
                .execute(book.getImage());

        return convertView;
    }

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;

        public DownloadImageAsyncTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url= urls[0];

            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap i) {
            bmImage.setImageBitmap(i);
            bmImage.setVisibility(View.VISIBLE);
        }
    }
}
