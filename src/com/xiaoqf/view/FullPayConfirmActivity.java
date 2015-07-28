package com.xiaoqf.view;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiaoqf.common.Consts;
import com.xiaoqf.customview.ContactDialog;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.EventHandler;
import com.xiaoqf.app.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName: FullPlayConfirmActivity
 * @Description: 关于 界面
 * @version: 1.0
 * @author: tongdu
 * @Create: 2015-06-08
 */
public class FullPayConfirmActivity extends BaseActivity {
	// 标题栏
	@ViewInject(R.id.iv_title_left)
	private ImageView backImageView;
	
	@ViewInject(R.id.tv_title)
	private TextView titleTextView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_full_play);
		ViewUtils.inject(this);
		
		titleTextView.setText("付全款");
	}
	
	@OnClick(R.id.iv_title_left)
	public void onClickBack(View v) {
		onBackPressed();
	}
	
	@OnClick(R.id.activity_about_contact_ll)
	public void onContactClick(View v) {
/*//		EventHandler.showToast(this, "联系我们");
		final ContactDialog contactDialog = ContactDialog.getInstance(this);
		
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
		final Dialog dlg = new Dialog(this,R.style.AlertDialogCustom);
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
	@Override
	public void onBackPressed() {
		CommonUtil.openActivity(this, SettingsActivity.class);
		finish();
	}
}
