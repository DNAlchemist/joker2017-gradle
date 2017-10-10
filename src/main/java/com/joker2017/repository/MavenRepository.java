package com.joker2017.repository;

@SuppressWarnings("WeakerAccess")
public class MavenRepository {

    private String url;

    public void url(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
