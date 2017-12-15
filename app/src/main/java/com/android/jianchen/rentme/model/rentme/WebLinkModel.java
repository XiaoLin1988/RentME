package com.android.jianchen.rentme.model.rentme;

import java.io.Serializable;

/**
 * Created by emerald on 12/8/2017.
 */
public class WebLinkModel implements Serializable {
    private String title;
    private String content;
    private String thumbnail;
    private String link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
