package com.xiaoqf.customview;

import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.LogUtil;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

public class MagicTextView extends TextView {

	// 递减/递增 的变量值
	private double mRate;
	// 当前显示的值
	private double mCurValue;
	// 当前变化后最终状态的目标值
	private double mGalValue;
	private double tempValue;
	
	// 控制加减法
	private int rate = 1;
	// 当前变化状态(增/减/不变)
	private boolean refreshing;
	private static final int REFRESH = 1;

	private String beforeStr; // 滚动数字前面的字符串
	private String afterStr; // 滚动数字后面的字符串

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case REFRESH:
				RefreshTextView();
				break;
			}
		};
	};

	public MagicTextView(Context context) {
		super(context);
	}

	public MagicTextView(Context context, AttributeSet set) {
		super(context, set);
	}

	public MagicTextView(Context context, AttributeSet set, int defStyle) {
		super(context, set, defStyle);
	}

	public void setValue(double value,double desValue) {
		mGalValue = desValue;
		mCurValue = value;
		mRate = Math.abs((mGalValue - mCurValue) / 20);
	}

	public void startScroll(String beforeStr, double currentvalue, double desValue,String afterStr) {
		setValue(currentvalue,desValue);
		this.beforeStr = beforeStr == null ? "" : beforeStr;
		this.afterStr = afterStr == null ? "" : afterStr;
		doScroll();
	}

	private String formatValue(long mcurrentValue) {
		String str =null;
		long thousandthValue = mcurrentValue/1000;
		long hundredthValue = mcurrentValue%1000;
		//LogUtil.log("MagicTextView", hundredthValue+"");
		if(thousandthValue>0) {
			if((hundredthValue/100)>0)
				str =thousandthValue+","+hundredthValue;
			else if((hundredthValue/10)>0)
				str =thousandthValue+","+"0"+hundredthValue;
			else if(hundredthValue==0)
				str =thousandthValue+","+"0"+"0"+"0";
			else 
				str =thousandthValue+","+"0"+"0"+hundredthValue;
		}else {
			str = hundredthValue+"";
		}
		return str;
	}
	
	private void RefreshTextView() {
		if(mCurValue*rate<tempValue*rate) {
			mCurValue +=mRate * rate;
			setText(beforeStr
					+ CommonUtil.formatStr(String.valueOf((long)mCurValue), 3) + afterStr); // 格式+设置显示内容
//					+ formatValue((long) mCurValue) + afterStr); // 格式+设置显示内容
			mHandler.sendEmptyMessageDelayed(REFRESH, 50);
			//LogUtil.log("MagicTextView", "refresh1");
		}else {
			mCurValue =mGalValue;
			setText(beforeStr
					+ CommonUtil.formatStr(String.valueOf((long)mCurValue), 3) + afterStr); // 格式+设置显示内容
//					+ formatValue((long) mCurValue) + afterStr); // 格式+设置显示内容
			mCurValue =0;
			//LogUtil.log("MagicTextView", "refresh2");
		}
	}

	private void doScroll() {
		// if ( refreshing)
		// return;

		if (mCurValue > mGalValue) {
			rate = -1; // 如果当前值大于目标值,向下滚动
		} else {
			rate = 1; // 如果当前值小于目标值,向上滚动
		}
		tempValue = mCurValue+20*mRate*rate;
		mHandler.sendEmptyMessage(REFRESH);
	}

}
