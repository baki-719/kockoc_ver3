package com.kocapplication.pixeleye.kockocapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Pixeleye_server on 2017-11-20.
 */

public class BeaconRecyclerAdapter extends RecyclerView.Adapter<BeaconRecyclerAdapter.ItemViewHolder> {
    private Context mContext;
    private ArrayList<BeaconRecyclerItem> mItems;

    public BeaconRecyclerAdapter(Context mContext, ArrayList<BeaconRecyclerItem> mItems) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_beacon_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.textView.setText(mItems.get(position).getName());
        Glide.with(mContext).load(mItems.get(position).getImg()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private ImageView imageView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.beacon_point_name);
            imageView = (ImageView) itemView.findViewById(R.id.beacon_point_image);
        }
    }


}
