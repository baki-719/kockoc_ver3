package com.kocapplication.pixeleye.kockoc.util.map;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.kocapplication.pixeleye.kockoc.util.connect.BasicValue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by pixeleye03 on 2015-10-13.
 */
public class Searcher {
    // http://dna.daum.net/apis/local
    public static final String DAUM_MAPS_LOCAL_KEYWORD_SEARCH_API_FORMAT = "https://dapi.kakao.com/v2/local/search/keyword.json?query=%s&location=%f,%f&radius=%d&page=%d&appkey=%s";
    public static final String DAUM_MAPS_LOCAL_CATEGORY_SEARCH_API_FORMAT = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=%s&location=%f,%f&radius=%d&page=%d&appkey=%s";
    public static final String DAUM_MAPS_KEYWORD_SEARCH_API_FORMAT = "https://dapi.kakao.com/v2/local/search/keyword.json?query=%s&page=%d";
    /**
     * category codes
     * MT1 대형마트
     * CS2 편의점
     * PS3 어린이집, 유치원
     * SC4 학교
     * AC5 학원
     * PK6 주차장
     * OL7 주유소, 충전소
     * SW8 지하철역
     * BK9 은행
     * CT1 문화시설
     * AG2 중개업소
     * PO3 공공기관
     * AT4 관광명소
     * AD5 숙박
     * FD6 음식점
     * CE7 카페
     * HP8 병원
     * PM9 약국
     */

    private static final String TAG ="Searcher";
    private static final String HEADER_NAME_X_APPID = "Authorization";
    private static final String HEADER_NAME_X_PLATFORM = "x-platform";
    private static final String HEADER_VALUE_X_PLATFORM_ANDROID = "android";

    private static final int resultObjectSize = 10;

    OnFinishSearchListener onFinishSearchListener;
    SearchTask searchTask;
    String appId;
    private Handler handler;

    public Searcher(Handler handler) {
        super();
        this.handler = handler;
    }

    private class SearchTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... urls) {
            String url = urls[0];
            Map<String, String> header = new HashMap<String, String>();
            header.put(HEADER_NAME_X_APPID, "KakaoAK "+ BasicValue.getInstance().getDAUM_MAP_API_KEY());
//            header.put(HEADER_NAME_X_PLATFORM, HEADER_VALUE_X_PLATFORM_ANDROID);
            String json = fetchData(url, header);
            ArrayList<Item> itemList = parse(json);
            if (onFinishSearchListener != null) {
                if (itemList.isEmpty()) {
                    onFinishSearchListener.onFail();
                } else {
                    //onSuccess와 handler에 메세지를 함께 보낸다.
                    onFinishSearchListener.onSuccess(itemList);
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("THREAD", itemList);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
            return null;
        }
    }

    public void searchKeyword(Context applicationContext, String query, double latitude, double longitude, int radius, int page, String apikey, OnFinishSearchListener onFinishSearchListener) {
        this.onFinishSearchListener = onFinishSearchListener;

        if (searchTask != null) {
            searchTask.cancel(true);
            searchTask = null;
        }

        if (applicationContext != null) {
            appId = applicationContext.getPackageName();
        }
        String url = buildKeywordSearchApiUrlString(query, latitude, longitude, radius, page, apikey);
        searchTask = new SearchTask();
        searchTask.execute(url);
    }

    public void searchKeyword(Context applicationContext, String query, int page, String apikey, OnFinishSearchListener onFinishSearchListener) {
        this.onFinishSearchListener = onFinishSearchListener;

        if (searchTask != null) {
            searchTask.cancel(true);
            searchTask = null;
        }

        if (applicationContext != null) {
            appId = applicationContext.getPackageName();
        }
        String url = buildKeywordSearchApiUrlString(query, page);
        searchTask = new SearchTask();
        searchTask.execute(url);
    }

    public void searchCategory(Context applicationContext, String categoryCode, double latitude, double longitude, int radius, int page, String apikey, OnFinishSearchListener onFinishSearchListener) {
        this.onFinishSearchListener = onFinishSearchListener;

        if (searchTask != null) {
            searchTask.cancel(true);
            searchTask = null;
        }

        if (applicationContext != null) {
            appId = applicationContext.getPackageName();
        }
        String url = buildCategorySearchApiUrlString(categoryCode, latitude, longitude, radius, page, apikey);
        searchTask = new SearchTask();
        searchTask.execute(url);
    }

    private String buildKeywordSearchApiUrlString(String query, double latitude, double longitude, int radius, int page, String apikey) {
        String encodedQuery = "";
        try {
            encodedQuery = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return String.format(DAUM_MAPS_LOCAL_KEYWORD_SEARCH_API_FORMAT, encodedQuery, latitude, longitude, radius, page, apikey);
    }

    private String buildKeywordSearchApiUrlString(String query, int page) {
        String encodedQuery = "";
        try {
            encodedQuery = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return String.format(DAUM_MAPS_KEYWORD_SEARCH_API_FORMAT, encodedQuery, page);
    }


    private String buildCategorySearchApiUrlString(String categoryCode, double latitude, double longitude, int radius, int page, String apikey) {
        return String.format(DAUM_MAPS_LOCAL_CATEGORY_SEARCH_API_FORMAT, categoryCode, latitude, longitude, radius, page, apikey);
    }

    private String fetchData(String urlString, Map<String, String> header) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(4000 /* milliseconds */);
            conn.setConnectTimeout(7000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            if (header != null) {
                for (String key : header.keySet()) {
                    conn.addRequestProperty(key, header.get(key));
                }
            }
            conn.connect();
            InputStream is = conn.getInputStream();
            @SuppressWarnings("resource")
            Scanner s = new Scanner(is);
            s.useDelimiter("\\A");
            String data = s.hasNext() ? s.next() : "";
            is.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    private ArrayList<Item> parse(String jsonString) {
        ArrayList<Item> itemList = new ArrayList<Item>();
        try {
            JSONObject reader = new JSONObject(jsonString);
            JSONArray objects = reader.getJSONArray("documents");
            final int numberOfItemsInResp = objects.length();

            for (int i = 0; i < numberOfItemsInResp; i++) {

                JSONObject object = objects.getJSONObject(i);
                Item item = new Item();
                item.title = object.getString("place_name");
                Log.i("item_", item.title);
                item.address = object.getString("address_name");
                Log.i("item_", item.address);
                item.newAddress = object.getString("road_address_name");
                Log.i("item_", item.newAddress);
                item.phone = object.getString("phone");
                Log.i("item_", item.phone);
                item.category = object.getString("category_name");
                Log.i("item_", item.category);
                item.latitude = object.getDouble("x");
                Log.i("item_", String.valueOf(item.latitude));
                item.longitude = object.getDouble("y");
                Log.i("item_", String.valueOf(item.longitude));
//                item.distance = object.getDouble("distance");
                item.id = object.getString("id");
                Log.i("item_", item.id);
                item.placeUrl = object.getString("place_url");
                Log.i("item_", item.placeUrl);

                itemList.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("exception", "in");
            return null;
        }
        Log.i("exception", "not_in");
        return itemList;
    }

    public void cancel() {
        if (searchTask != null) {
            searchTask.cancel(true);
            searchTask = null;
        }
    }
}