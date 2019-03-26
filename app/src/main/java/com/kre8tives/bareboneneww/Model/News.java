package com.kre8tives.bareboneneww.Model;

/**
 * Created by Administrator on 4/11/2017.
 */

public class News {
    public News(String news, String title) {
        this.news = news;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public News() {

    }

    private String news,id,title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public News(String news) {

        this.news = news;
    }
}
