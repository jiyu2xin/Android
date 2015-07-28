package com.xiaoqf.view;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiaoqf.app.R;
import com.xiaoqf.common.Consts;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName: QCallActivity
 * @Description: 小Q电话 页面
 * @version: 1.0
 * @author: wangbin
 * @Create: 2015-05-07
 */
public class QCallActivity extends BaseActivity {
	
	@ViewInject(R.id.iv_title_left)
	private ImageView backImageView;
	
	@ViewInject(R.id.tv_title)
	private TextView titleTextView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qcall);
		ViewUtils.inject(this);
		init();
	}

	private void init() {
		backImageView.setVisibility(View.INVISIBLE);
		titleTextView.setText("小Q直通车");
		//titleTextView.setTextSize(28);
		//titleTextView.setTextColor(0xFF808080);
	}
	
	@OnClick(R.id.activity_qcall_contact_ll)
	public void onCallButtonClick(View v) {	
		//Intent mcallItent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+"4008867733"));
		/*
			PackageManager pgm = getPackageManager();
			List<ResolveInfo> activitys = pgm.queryIntentActivities(mcallItent, 0);
			boolean isActivityExist = activitys.size()>0;
			if(isActivityExist)
		*/
		
		//startActivityForResult(mcallItent,1);
		/*final ContactDialog contactDialog = ContactDialog.getInstance(this);
		
		contactDialog.onCancleTvClick(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				contactDialog.dismiss();
			}
		});
		
		contactDialog.onDialTvClick(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+ Consts.CLIENTPHONE));  
				startActivity(intent); 

			}
		});
		
		contactDialog.show();*/
		dialog();
	}
	
	@SuppressLint("NewApi") private void dialog() {	
		final Dialog dlg = new Dialog(this,R.style.DialogCustomCenter);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_qcall, null);

		final TextView comfirm = (TextView) layout.findViewById(R.id.activity_qcall_comfirm_text);
		//final ImageView image1 = (ImageView) layout.findViewById(R.id.activity_qcall_cancle_text);
		TextView mCancleTextView = (TextView) layout.findViewById(R.id.activity_qcall_cancle_text);
		
		mCancleTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dlg.dismiss();
			}
		});
		// set a large value put it in bottom
		comfirm.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				dlg.dismiss();
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+ Consts.CLIENTPHONE));  
				startActivity(intent);
			}
		});
		

        Window dialogWindow = dlg.getWindow();  
        //设置位置  
        dialogWindow.setGravity(Gravity.CENTER);  
       //设置dialog的宽高属性  
        dialogWindow.getDecorView().setPadding(20, 0, 20, 0);
		dlg.setContentView(layout);
		
		dialogWindow.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
		dlg.show();
		
	}
	
	@OnClick(R.id.iv_title_left)
	public void onQcallBackClick(View v) {
		super.onBackPressed();
	}

}
