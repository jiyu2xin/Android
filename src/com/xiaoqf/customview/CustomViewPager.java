package com.xiaoqf.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

public class CustomViewPager extends ViewPager {

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof HorizontalScrollView) {
			return true;
		}
		return super.canScroll(v, checkV, dx, x, y);
	}
	
  
    @Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchKeyEvent(event);
	}


	public CustomViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
    }  
  
    public CustomViewPager(Context context) {  
        super(context);  
        // TODO Auto-generated constructor stub  
    }  
  
}
