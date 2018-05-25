package com.example.android.fashionnews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dolea on 28.04.2018.
 */

public class Utility {

    private static final String LOG_TAG = Utility.class.getSimpleName();

    private Utility() {
    }

    /**
     * Query the Guardian and return a list of {@link News} objects.
     */

    public static List<News> fetchNewsData(String requestUrl) throws InterruptedException {

        // Create URL object
        URL url = returnUrl(requestUrl);

        // Perform HTTP request to URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "HTTP request error.", e);
        }

        // Extract relevant fields from the JSON response and create a list of news
        List<News> news = extractFeatureFromJson(jsonResponse);

        // Return the list of news
        return news;
    }

    // Return new URL object from the given string URL.
    private static URL returnUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "URL building error.", e);
        }
        return url;
    }

    /*
    * Make an HTTP request to the given URL and return a String as the response.
    */
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        // If the URL is null then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful(return code 200), then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why the makeHttpRequest(URL url) method signature specifies than an IOException could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the whole JSON response from the server.
     */
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

    /**
     * Return list of {@link News} objects that has been built up from parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news
        List<News> newsList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            // Create a JSONObject from the JSON response string
            JSONObject root = new JSONObject(newsJSON);

            // Extract the JSONObject associated with the key called "response",
            JSONObject response = root.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results"
            JSONArray results = response.getJSONArray("results");

            // For each news in the JsonNewsArray create an {@link News} object
            for (int i = 0; i < results.length(); i++) {

                // Get a single news article at position i within the list of news
                JSONObject currentNews = results.getJSONObject(i);

                // Extract the value for the key called "sectionName"
                String newsSection = currentNews.getString("sectionName");

                // Check if we have the news date and than extract the value for the key called "webPublicationDate"
                String newsDate = "Publication Date not available";
                if (currentNews.has("webPublicationDate")) {
                    newsDate = currentNews.getString("webPublicationDate");
                }

                // Extract the value for the key called "webTitle"
                String newsTitle = currentNews.getString("webTitle");

                // Extract the value for the key called "webUrl"
                String newsUrl = currentNews.getString("webUrl");

                // Extract the JSONArray for the key called "tags",
                JSONArray currentAuthor= currentNews.getJSONArray("tags");
                String newsAuthor = "Author: Not available";
                int tag = currentAuthor.length();
                if (tag == 1) {
                    // Create a JSONObject for author
                    JSONObject currentNewsAuthor = currentAuthor.getJSONObject(0);
                    String existingAuthor = currentNewsAuthor.getString("webTitle");
                    newsAuthor = "Author: " + existingAuthor;
                }

                // Create a new News object with the title, category, author, date and url, from the JSON response.
                News newNews = new News(newsTitle, newsSection, newsAuthor, newsDate, newsUrl);

                // Add the new {@link News} to the list of News.
                newsList.add(newNews);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("Utility", "JSON results parsing error.");
        }

        // Return the news list
        return newsList;
    }
}
