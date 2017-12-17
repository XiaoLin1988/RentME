package com.jianchen.rentme.model.rentme;

import java.io.Serializable;

/**
 * Created by emerald on 12/4/2017.
 */
public class IntroModel implements Serializable {
    private Object data;
    private int type;  // 0 : image, 1 : youtube 2 : vimeo, 3 : web post

    public Object getLink() {
        return data;
    }

    public void setLink(Object data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
