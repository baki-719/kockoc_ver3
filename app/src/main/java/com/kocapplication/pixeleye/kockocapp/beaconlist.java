package com.kocapplication.pixeleye.kockocapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.eddystone.Eddystone;
import com.kocapplication.pixeleye.kockocapp.model.TourData;

import java.util.ArrayList;

/**
 * Created by hp on 2017-11-07.
 */

public class beaconlist extends AppCompatActivity {

    private static final String TAG = "beaconlist";

    private ArrayList<CustomUrl> urls = new ArrayList<CustomUrl>();
    private ArrayList<TourData> tourData = new ArrayList<TourData>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<BeaconRecyclerItem> mItems = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beaconlist);
        try {
            init_();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void init_() throws InterruptedException {
        recyclerView = (RecyclerView) findViewById(R.id.beacon_recyclerview);
        recyclerView.addOnItemTouchListener(new BeaconRecyclerViewOnItemClickListener(getApplicationContext(), recyclerView, new BeaconRecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), BeaconTourDetailActivity.class);
                intent.putExtra("tourData", tourData.get(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "long click");
            }
        }));
        urls = (ArrayList<CustomUrl>) getIntent().getSerializableExtra("urlList");
        Log.d(TAG, "urlsSize : " + urls.size());
        for (int i = 0; i < urls.size(); i++) getItemsFromTourAPI(i);
        setRecyclerView();
    }

    private void getItemsFromTourAPI(int index) {
        Handler handler = new BeaconTourDataReceiveHandler();
        Thread thread = new BeaconTourAPIThread(this, urls.get(index).getUrl(), handler);
        thread.start();
    }


    private class BeaconTourDataReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setData((TourData) msg.getData().getSerializable("THREAD"));
        }
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new BeaconRecyclerAdapter(getApplicationContext(),mItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setData(TourData item) {
        tourData.add(item);
        mItems.add(new BeaconRecyclerItem(item.getTitle(), item.getImg()));
        adapter.notifyDataSetChanged();
    }


}
