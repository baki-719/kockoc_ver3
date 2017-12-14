package com.kocapplication.pixeleye.kockoc;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.kocapplication.pixeleye.kockoc.model.TourData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Pixeleye_server on 2017-11-15.
 */

//인증키
//%2FPY%2FrC05SWjkibjwVdcdM9oEFIigSw3ePTOzJRtbCB3Li5gdZVTDZkYG5A1U4fah%2BvyfFw%2BGIGkeE3gpg3EaPQ%3D%3D
//타입
//&_type=json

public class BeaconTourAPIThread extends Thread {
    private final static String TAG = "BeaconTourAPIThread";
    private BeaconRepo beaconRepo;
    private Context mContext;
    private android.os.Handler handler;

    private String url;
    private String title;
    private String firstImage;
    private String firstImage2;
    private String overview;

    public BeaconTourAPIThread(Context mContext, String url, android.os.Handler handler) {
        this.mContext = mContext;
        this.url = url.replaceAll("https://goo.gl/","");
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();
        Retrofit client = new Retrofit.Builder().baseUrl("https://goo.gl/").addConverterFactory(GsonConverterFactory.create()).build();
        BeaconRepo.TourApiInterface service = client.create(BeaconRepo.TourApiInterface.class);
        Call<BeaconRepo> call = service.getBeaconRepo(url);
        call.enqueue(new Callback<BeaconRepo>() {
            @Override
            public void onResponse(Call<BeaconRepo> call, Response<BeaconRepo> response) {
                Message msg = Message.obtain();
                if(response.isSuccessful()){
                    beaconRepo = response.body();
                    Log.d(TAG,"response.raw :"+response.raw());
                    if(beaconRepo.getResponse().getHeader().getResultCode().equals("0000")){ // 0000 = 성공
                        Log.d(TAG, "Load data success");
                        TourData tourData = new TourData();
                        BeaconRepo.response.body.items.item item = beaconRepo.getResponse().getBody().getItems().getItem();
                        if(item.getFirstImage() == null) return; // 이미지없는거 제외
                        tourData.setTitle(item.getTitle());
                        tourData.setImg(item.getFirstImage());
                        tourData.setThumbImg(item.getFirstImage2());
                        tourData.setOverview(item.getOverview());

                        Log.d(TAG,tourData.toString());

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("THREAD", tourData);
                        msg.setData(bundle);
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }else{
                    }
                } else {
                    Log.d(TAG, "response fail : "+ response.raw());
                }
            }

            @Override
            public void onFailure(Call<BeaconRepo> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}
