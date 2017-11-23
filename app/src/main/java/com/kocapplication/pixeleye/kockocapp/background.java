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
import android.text.format.Time;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.eddystone.Eddystone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by hp on 2017-10-29.
 */

public class background extends Service {

    private static final String TAG = "background";

    private BeaconManager beaconManager;
    private ArrayList<Eddystone> myEddystone;
    public int[] flag = new int[]{0, 0, 0, 0, 0};


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("background", "start");
        scanTheBeacon();
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


    private void scanTheBeacon() {
        myEddystone = new ArrayList<Eddystone>();
        beaconManager = new BeaconManager(getApplicationContext());

        beaconManagerConnect();
        beaconManager.setEddystoneListener(new BeaconManager.EddystoneListener() {
            @Override
            public void onEddystonesFound(List<Eddystone> list) {
                myEddystone = (ArrayList<Eddystone>) list;
                ArrayList<CustomUrl> urls = new ArrayList<CustomUrl>();
                for (int i = 0; i < myEddystone.size(); i++) {
                    Log.d(TAG, "scaned beacon :" + list.get(i).url);
                    Log.d(TAG, "mac : "+ list.get(i).macAddress);
                    Log.d(TAG, "time : " + Calendar.getInstance().getTime().toString());
                    for (int j = 0; j < BeaconMacAddressList.getInstance().getMacAddresses().length; j++) {
                        if (myEddystone.get(i).macAddress.toString().equals(BeaconMacAddressList.getInstance().getMacAddresses()[j])) {
                            urls.add(new CustomUrl(myEddystone.get(i).url));
                            Log.d(TAG, "detecting count : "+ String.valueOf(urls.size()));
                        }
                    }
                }
                if (urls.size() != 0) showNotification("주변에 비콘이 있습니다.", "비콘 목록을 조회하시겠습니까?", urls);
                else {
                }
            }
        });

    }

    private void beaconManagerConnect(){
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startEddystoneScanning();
                beaconManager.setBackgroundScanPeriod(10000, 2000);
                beaconManager.setForegroundScanPeriod(10000, 2000);
            }
        });
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
        builder.setDefaults(Notification.DEFAULT_LIGHTS);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
        scanTheBeacon();
    }


}
