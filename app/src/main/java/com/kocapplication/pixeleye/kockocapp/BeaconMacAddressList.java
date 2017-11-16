package com.kocapplication.pixeleye.kockocapp;

/**
 * Created by Pixeleye_server on 2017-11-14.
 */

// TODO: 2017-11-14 mac adress리스트를 클래스로 만듬

public class BeaconMacAddressList {
    private String[] macAddresses = {
            "[C0:EF:7C:E7:D0:41]"
            ,"[D8:C0:D1:26:78:BE]"
            ,"[DB:55:D5:41:27:98]"
    };

    private static BeaconMacAddressList instance;

    private BeaconMacAddressList() {
    }

    public static BeaconMacAddressList getInstance(){
        if(instance == null) instance = new BeaconMacAddressList();
        return instance;
    }

    public String[] getMacAddresses() {
        return macAddresses;
    }
}
