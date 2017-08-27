package com.example.android.mybooklist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    ListView listView;
    BookAdapter mAdapter;
    EditText editText;
    TextView textNoDataFound;


    public static final String LOG_TAG = "MyBookList";
    public static final int BOOK_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_item);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(mAdapter);

        editText = (EditText) findViewById(R.id.keyword_edit_text);
        textNoDataFound = (TextView) findViewById(R.id.noData);


        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        final Context context = getApplicationContext();
        final int duration = Toast.LENGTH_SHORT;


        Button search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {


                    if (editText.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(context, getString(R.string.empty), duration);
                        toast.show();
                        return;
                    }

                    getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);

                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }

                } else {
                    // No network connection

                    Toast toast = Toast.makeText(context, getString(R.string.no_connection), duration);
                    toast.show();

                    Log.e(LOG_TAG, "No network connection");
                }
            }
        });
    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        String query = editText.getText().toString();
        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error url encoding: ", e);
            query = "";
        }

        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=20";
        return new BookLoader(MainActivity.this, url);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
            textNoDataFound.setVisibility(View.GONE);
            listView.setAdapter(mAdapter);
            listView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        mAdapter.clear();
    }
}
