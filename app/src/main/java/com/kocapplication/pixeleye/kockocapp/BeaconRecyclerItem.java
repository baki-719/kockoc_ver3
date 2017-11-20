package com.kocapplication.pixeleye.kockocapp;

import java.io.Serializable;

/**
 * Created by Pixeleye_server on 2017-11-20.
 */

public class BeaconRecyclerItem implements Serializable {
    private String name;
    private String img;

    public BeaconRecyclerItem(String name, String img) {
        this.name = name;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
