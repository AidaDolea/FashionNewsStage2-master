package com.example.android.fashionnews;

/**
 * Created by Dolea on 28.04.2018.
 */

public class News {

    private String title;
    private String category;
    private String author;
    private String date;
    private String url;

    /**
     * Constructor for News object.
     */

    public News(String title, String category, String author, String date, String url) {
        this.title = title;
        this.category = category;
        this.author = author;
        this.date = date;
        this.url = url;
    }

    /**
     * Returns the news title.
     */
    public String getTitle() {

        return title;
    }

    /**
     * Returns the news category.
     */
    public String getCategory() {

        return category;
    }

    /**
     * Returns the news author name.
     */
    public String getAuthor() {

        return author;
    }

    /**
     * Returns the news date.
     */
    public String getDate() {

        return date;
    }

    /**
     * Returns news website URL.
     */
    public String getUrl() {

        return url;
    }
}
