package com.kocapplication.pixeleye.kockoc;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kocapplication.pixeleye.kockoc.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockoc.model.TourData;

/**
 * Created by Pixeleye_server on 2017-11-20.
 */

public class BeaconTourDetailActivity extends BaseActivityWithoutNav {
    private static final String TAG = "BeaconTourDetailActivity";
    private View containView;
    private LinearLayout ll_tour_img_container;
    private TextView tour_text;
    private TextView tour_title;
    private TourData data;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        container.setLayoutResource(R.layout.activity_tour_detail);
        containView = container.inflate();

        actionBarTitleSet("", Color.WHITE);

        getComponent(containView);
        getData();
        setData();
    }

    private void getComponent(View containView) {
        tour_text = (TextView) containView.findViewById(R.id.tv_tour_detail_text);
        tour_title = (TextView) containView.findViewById(R.id.tv_tour_detail_title);
        ll_tour_img_container = (LinearLayout) containView.findViewById(R.id.ll_tour_detail_image_container);
    }

    private void getData() {
        data = (TourData) getIntent().getSerializableExtra("tourData");
    }

    private void setData() {
        //이미지
        //ImageView 생성
        ImageView temp = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 10;
        temp.setLayoutParams(params);
        temp.setAdjustViewBounds(true);
        temp.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Glide.with(this).load(data.getImg()).into(temp);
        ll_tour_img_container.addView(temp);

        actionBarTitleSet(data.getTitle(), Color.WHITE);
        tour_title.setText(data.getTitle());
        tour_text.setText(Html.fromHtml(data.getOverview()));
    }

}
