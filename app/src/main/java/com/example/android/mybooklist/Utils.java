package com.example.android.mybooklist;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by user on 8/26/2017.
 */


public class Utils {

    private static final String LOG_TAG = "Utils";

    public static ArrayList<Book> fetchBookData(String url) {
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(createUrl(url));
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with connecting: ", e);
        }

        ArrayList<Book> books = extractFromJSON(jsonResponse);
        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error with creating URL: ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";


        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the search JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }

        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Book> extractFromJSON(String jsonResponse) {

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");


            if (itemsArray.length() > 0) {

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject item = itemsArray.getJSONObject(i);
                    JSONObject volumeInfo = item.getJSONObject("volumeInfo");


                    String title = volumeInfo.getString("title");
                    Book book = new Book(title);


                    if (volumeInfo.has("authors")) {
                        JSONArray authorsJSON = volumeInfo.getJSONArray("authors");
                        String authors = authorsJSON.join(", ").replace("\"", "");
                        book.setAuthors(authors);
                    }


                    if (volumeInfo.has("imageLinks")) {
                        JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                        String smallThumbnail = imageLinks.getString("smallThumbnail");
                        book.setImageUrl(smallThumbnail);
                    }

                    books.add(book);
                }

                return books;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the search JSON results", e);
        }

        return null;
    }
}
