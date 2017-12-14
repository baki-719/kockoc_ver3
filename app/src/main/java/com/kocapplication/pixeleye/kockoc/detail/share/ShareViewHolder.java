package com.kocapplication.pixeleye.kockoc.detail.share;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockoc.R;

/**
 * Created by Han on 2016-07-08.
 */
public class ShareViewHolder extends RecyclerView.ViewHolder{
    private TextView title;
    private ImageView icon;

    public ShareViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        icon = (ImageView) itemView.findViewById(R.id.icon);
    }

    public TextView getTitle() {
        return title;
    }

    public ImageView getIcon() {
        return icon;
    }
}
