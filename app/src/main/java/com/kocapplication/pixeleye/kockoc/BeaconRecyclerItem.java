package com.kocapplication.pixeleye.kockoc;

import java.io.Serializable;

/**
 * Created by Pixeleye_server on 2017-11-20.
 */

public class BeaconRecyclerItem implements Serializable {
    private String name;
    private String img;
    private String overview;

    public BeaconRecyclerItem(String name, String img, String overview) {
        this.name = name.replaceAll("\\((.*?)\\)","");
        this.img = img;
        this.overview = overview;
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

    public String getOverview(){return overview;}

    public void setOverview(String overview){
        this.overview = overview;
    }
}
