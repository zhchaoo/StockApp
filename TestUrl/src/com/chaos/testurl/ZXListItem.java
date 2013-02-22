package com.chaos.testurl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

public class ZXListItem extends FrameLayout {

	public ZXListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
    	LayoutInflater factory = LayoutInflater.from(context);
        factory.inflate(R.layout.zx_list_item, this);
	}

}
