package com.xiaoqf.customview;

import android.content.Context;
import android.util.AttributeSet;

import android.widget.ListView;

public class CustomListView extends ListView {


	public CustomListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomListView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		setMeasuredDimension(widthMeasureSpec, expandSpec);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
}
