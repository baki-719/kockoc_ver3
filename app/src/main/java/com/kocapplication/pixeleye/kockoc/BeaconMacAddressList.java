package com.kocapplication.pixeleye.kockoc;

/**
 * Created by Pixeleye_server on 2017-11-14.
 */

public class BeaconMacAddressList {
    private String[] macAddresses = {
            "[C0:EF:7C:E7:D0:41]"
            ,"[FE:13:2F:DA:80:9B]"
            ,"[DB:55:D5:41:27:98]"
            ,"[F2:67:AB:82:F5:93]"
            ,"[F1:B6:28:BC:4A:F0]"
            ,"[DC:25:6D:6D:96:78]"
            ,"[E2:5F:16:9B:70:F1]"
            ,"[E9:60:DF:D0:F4:FC]"
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
