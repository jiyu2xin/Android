package com.xiaoqf.customview;

import com.xiaoqf.common.Consts;
import com.xiaoqf.util.LogUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import android.widget.ListView;

public class CustomOrderListView extends ListView {

	public CustomOrderListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomOrderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomOrderListView(Context context) {
		super(context);
	}

/*	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		
		
		// 记录总高度
		int mTotalHeight = 0;
		// 遍历所有子视图
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);

			// 获取在onMeasure中计算的视图尺寸
			int measureHeight = childView.getMeasuredHeight();
			int measuredWidth = childView.getMeasuredWidth();

			childView.layout(l, mTotalHeight, measuredWidth, mTotalHeight
					+ measureHeight);
			LogUtil.log("measureHeight",measureHeight+ "");
			mTotalHeight += measureHeight;
			
		}

	}*/

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int specSize = MeasureSpec.getSize(heightMeasureSpec);//Integer.MAX_VALUE >> 2
		int expandSpec = MeasureSpec.makeMeasureSpec(Consts.showHightSize,
				MeasureSpec.EXACTLY);// at_most
		setMeasuredDimension(widthMeasureSpec, expandSpec);//WalletRecordActivity.showSizeCounts
		super.onMeasure(widthMeasureSpec, expandSpec);
		LogUtil.log("onMeasure", "onMeasure on!"+specSize);
	}

}
