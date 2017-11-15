package com.kocapplication.pixeleye.kockocapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.eddystone.Eddystone;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hp on 2017-10-29.
 */

public class background extends Service {

    private static final String TAG = "background";

    private BeaconManager beaconManager;
    private ArrayList<Beacon> myBeacon;
    private ArrayList<Eddystone> myEddystone;
    public int[] flag = new int[]{0, 0, 0, 0, 0};


    public Region region_kbg = new Region("경복궁", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 42117, 6064); // Location1


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("background", "start");
        init();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }

    public background() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }


    public void init() {
        myEddystone = new ArrayList<Eddystone>();
        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startEddystoneScanning();
                beaconManager.setBackgroundScanPeriod(10000, 5000);
                beaconManager.setForegroundScanPeriod(5000, 5000);
            }
        });

        //// TODO: 2017-11-14 eddystone을 백그라운드로 돌림 왜 되는지는 모름
        beaconManager.setEddystoneListener(new BeaconManager.EddystoneListener() {
            @Override
            public void onEddystonesFound(List<Eddystone> list) {
                myEddystone = (ArrayList<Eddystone>) list;
                ArrayList<CustomUrl> urls = new ArrayList<CustomUrl>();
                for (int i = 0; i < myEddystone.size(); i++) {
                    for (int j = 0; j < BeaconMacAddressList.getInstance().getMacAddresses().length; j++) {
                        if (myEddystone.get(i).macAddress.toString().equals(BeaconMacAddressList.getInstance().getMacAddresses()[j])){
                            urls.add(new CustomUrl(myEddystone.get(i).url));
                            break;
                        }
                    }
                }
                if(urls.size() != 0)  showNotification("주변에 비콘이 있습니다.", "비콘 목록을 조회하시겠습니까?", urls);
                beaconManager.disconnect();
//                for (Eddystone eddystone : list) {
//                    if (myEddystone.size() == 0) myEddystone.add(eddystone);
//                    else if (myEddystone.size() > 0) {
//                        for (int i = 0; i < myEddystone.size(); i++) {
//                            if (myEddystone.get(i).macAddress.toString() == BeaconMacAddressList.getInstance().getMacAddresses()[0]) {
//                                showNotification("주변에 비콘이 있습니다.", "비콘 목록을 조회하시겠습니까?", (ArrayList<Eddystone>) list);
//                                Log.i("background", "비콘 인식");
//                            }
//                        }
//                    }
//                }
            }

        });


//        beaconManager.connect(new BeaconManager.ServiceReadyCallback(){
//            public void onServiceReady(){
//                beaconManager.startMonitoring(region_kbg);
//                beaconManager.setBackgroundScanPeriod(5000, 5000);
//                beaconManager.setForegroundScanPeriod(5000, 5000);
//            }
//        });
//        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
//            @Override
//            public void onEnteredRegion(Region region, List<Beacon> list) {
//                for(int i=0;i<list.size();i++){
//                    if(region.getIdentifier().equals((region_kbg.getIdentifier()))){
//                        showNotification("주변에 비콘이 있습니다.", "비콘 목록을 조회하시겠습니까?");
//                        Log.i("background", "비콘 인식");
//                    }
//                }
//            }
//
//            @Override
//            public void onExitedRegion(Region region) {
//
//            }
//        });

    }


    public void showNotification(String title, String message, ArrayList<CustomUrl> urls) { // 팝업 띄우기
        Intent notifyIntent = new Intent(background.this, beaconlist.class);
        notifyIntent.putExtra("urlList", urls);

        PendingIntent intent = PendingIntent.getActivity(
                background.this, 0,
                notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setAutoCancel(true);
        builder.setContentIntent(intent);
        builder.setDefaults(Notification.DEFAULT_ALL);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());


    }


}
