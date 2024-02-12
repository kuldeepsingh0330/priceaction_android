package com.ransankul.priceaction.model;

public class RelatedNews {

    private String newstime;
    private String newsHeading;
    private String newsSubheading;

    public RelatedNews() {
    }

    public RelatedNews(String newstime, String newsHeading, String newsSubheading) {
        this.newstime = newstime;
        this.newsHeading = newsHeading;
        this.newsSubheading = newsSubheading;
    }

    public String getNewstime() {
        return newstime;
    }

    public void setNewstime(String newstime) {
        this.newstime = newstime;
    }

    public String getNewsHeading() {
        return newsHeading;
    }

    public void setNewsHeading(String newsHeading) {
        this.newsHeading = newsHeading;
    }

    public String getNewsSubheading() {
        return newsSubheading;
    }

    public void setNewsSubheading(String newsSubheading) {
        this.newsSubheading = newsSubheading;
    }
}
