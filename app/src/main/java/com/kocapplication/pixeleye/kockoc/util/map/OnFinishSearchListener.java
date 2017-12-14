package com.kocapplication.pixeleye.kockoc.util.map;

import java.util.List;

public interface OnFinishSearchListener {
	public void onSuccess(List<Item> itemList);
	public void onFail();
}
