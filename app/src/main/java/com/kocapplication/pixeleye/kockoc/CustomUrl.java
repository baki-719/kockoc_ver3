package com.kocapplication.pixeleye.kockoc;

import java.io.Serializable;

/**
 * Created by Pixeleye_server on 2017-11-14.
 */

//url을 putExtra로 넘기기위해 직렬화 해야해서 커스텀으로 url class 만듬

public class CustomUrl implements Serializable {

    private String url;

    public CustomUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void add(String url){
        setUrl(url);
    }
}
