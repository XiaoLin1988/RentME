package com.android.jianchen.rentme.model.rentme;

import java.io.Serializable;

/**
 * Created by emerald on 12/4/2017.
 */
public class IntroModel implements Serializable {
    private String link;
    private int type;  // 0 : image, 1 : youtube 2 : vimeo, 3 : web post

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
