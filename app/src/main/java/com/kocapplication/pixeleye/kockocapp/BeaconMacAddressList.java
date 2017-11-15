package com.kocapplication.pixeleye.kockocapp;

/**
 * Created by Pixeleye_server on 2017-11-14.
 */

// TODO: 2017-11-14 mac adress리스트를 클래스로 만듬
// TODO: 2017-11-14 beacon에 아직 tourAPI request할 url입력하지 않음
// TODO: 2017-11-14 현재는 걍 사무실에 잡히는 아무비콘 5개 입력해놓음

public class BeaconMacAddressList {
    private String[] macAddresses = {
            "[FE:13:2F:DA:80:9B]"
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
