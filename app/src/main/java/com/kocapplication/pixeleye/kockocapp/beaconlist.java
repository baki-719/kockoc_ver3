package com.kocapplication.pixeleye.kockocapp;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    private TourData item;

    private ArrayList<CustomUrl> urls = new ArrayList<CustomUrl>();
    private BeaconManager beaconManager;
    public ArrayList<Eddystone> myEddystone;
    private ImageView first_image;
    private ImageView second_image;
    private ImageView third_image;
    private TextView first_text;
    private TextView second_text;
    private TextView third_text;

//    public Eddystone kbg_eddystone = new Eddystone(); // Location4



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beaconlist);
        first_image = (ImageView) findViewById(R.id.first_image);
        second_image = (ImageView) findViewById(R.id.second_image);
        third_image = (ImageView) findViewById(R.id.third_image);
        first_text = (TextView) findViewById(R.id.first_text);
        second_text = (TextView) findViewById(R.id.second_text);
        third_text = (TextView) findViewById(R.id.third_text);
        init_();
    }

    private void init_(){
        urls = (ArrayList<CustomUrl>) getIntent().getSerializableExtra("urlList");
        // TODO: 2017-11-14 url 받아온걸로 통신하여 response 받아온 데이터 핸들링 하여야함 (http통신으로 Retrofit을 쓰는게 좋을듯 함)
        Log.d(TAG, "urlsSize : " + urls.size());
        Handler handler = new BeaconTourDataReceiveHandler();
        Thread thread = new BeaconTourAPIThread(this,urls.get(0).getUrl(), handler);
        thread.start();
    }

    private class BeaconTourDataReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            item = (TourData) msg.getData().getSerializable("THREAD");

            Log.d(TAG, item.getImg());

            first_text.setText(item.getTitle());
            Glide.with(getApplicationContext()).load(item.getImg()).into(first_image);

        }
    }




//
//    private void init(){
//        myEddystone= new ArrayList<Eddystone>();
//        beaconManager = new BeaconManager(getApplicationContext());
//
//        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
//            @Override
//            public void onServiceReady() {
//                beaconManager.startEddystoneScanning();
//                beaconManager.setBackgroundScanPeriod(5000, 5000);
//                beaconManager.setForegroundScanPeriod(5000, 5000);
//            }
//        });
//         beaconManager.setEddystoneListener(new BeaconManager.EddystoneListener() {
//            @Override
//            public void onEddystonesFound(List<Eddystone> list) {
//              //  Eddystone a = list.get(0);
//              //  String Eddystone_URL=a.url;
//                for (Eddystone eddystone: list){
//                    if(myEddystone.size()==0){
//                        myEddystone.add(eddystone);
//                    }
//                    else if(myEddystone.size()>0){
//                        for(int i=0;i<myEddystone.size();i++){
//                            if(myEddystone.get(i).macAddress==eddystone.macAddress) { // 맥어드레스 비교하여 등록되어있으면 url가져온다
//                                continue;
//                            }
//                            else{
//                                myEddystone.add(eddystone);
//                            }//등록되어있는 macAddress 비교해가지고 같으면 패스 안같으면 추가한다.
//                        }
//                    }
//                }
//
//            }
//        });
//
//    }
//
//
//    private class SendData extends AsyncTask<String, Void, String> {
//        ProgressDialog progressDialog;
//        String errorString = null;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//        }
//
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            String serverURL = params[0];
//
//
//            try {
//
//                URL url = new URL(serverURL);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//
//                httpURLConnection.setRequestMethod("GET");
//                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setReadTimeout(5000);
//                httpURLConnection.setConnectTimeout(5000);
//                httpURLConnection.connect();
//
//
//
//                int responseStatusCode = httpURLConnection.getResponseCode();
//                Log.d("PHP", "response code - " + responseStatusCode);
//
//                InputStream inputStream;
//                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
//                    inputStream = httpURLConnection.getInputStream();
//                }
//                else{
//                    inputStream = httpURLConnection.getErrorStream();
//                }
//
//
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                StringBuilder sb = new StringBuilder();
//                String line;
//
//                while((line = bufferedReader.readLine()) != null){
//                    sb.append(line);
//                }
//
//
//                bufferedReader.close();
//
//
//                return sb.toString().trim();
//
//
//            } catch (Exception e) {
//
//                Log.d("PHP", "InsertData: Error ", e);
//                errorString = e.toString();
//
//                return null;
//            }
//
//        }
//    }
//

}
