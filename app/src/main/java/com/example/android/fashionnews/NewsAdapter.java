package com.example.android.fashionnews;

import android.content.Context;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dolea on 28.04.2018.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, List<News> news) {
        super(context, 0,news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Find the news at the given position in the list of news
        News currentNews = getItem(position);

        // Find the TextView for the news title
        TextView newsTitleTextView = listItemView.findViewById(R.id.newsTitle);
        // Display the current news title
        newsTitleTextView.setText(currentNews.getTitle());

        // Find the TextView for the news category
        TextView newsCategoryTextView = listItemView.findViewById(R.id.newsCategory);
        // Display the current news category
        newsCategoryTextView.setText(currentNews.getCategory());

        // Find the TextView for news author
        TextView newsAuthorTextView = listItemView.findViewById(R.id.newsAuthor);
        // Display the current news category
        newsAuthorTextView.setText(currentNews.getAuthor());

        // Find the TextView for news date
        TextView currentNewsDate = listItemView.findViewById(R.id.newsDate);
        // Extract, format and display the current date news
        SimpleDateFormat dateFormatJSON = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        SimpleDateFormat myDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

        try {
            Date dateNews = dateFormatJSON.parse(currentNews.getDate());
            String date = myDateFormat.format(dateNews);
            currentNewsDate.setText(date);
        } catch (ParseException e) {
            Log.e("Date Parsing Error", "Error parsing json date:" + e.getMessage());
            e.printStackTrace();
        }

        // Return the news list
        return listItemView;

    }
}
