package com.xiaoqf.customview;

import com.xiaoqf.app.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContactDialog extends Dialog implements DialogInterface   {
	private View mDialogView;
	
	private TextView cancleTv;
	private TextView dialTv;
	
	private Effectstype type = null;
	
	private static Context tmpCtx;
	private int mDuration = 700*1;
	private RelativeLayout mRelativeLayout;
	
	private static ContactDialog instance;

	public ContactDialog(Context context) {
		super(context);
		init(context);
	}
	
	public ContactDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}
	
	public static ContactDialog getInstance(Context context) {
		if (instance == null || !tmpCtx.equals(context)) {
			synchronized (ContactDialog.class) {
				instance = new ContactDialog(context, R.style.contactdialog);
			}
		}
		tmpCtx = context;
		return instance;
	}

	private void init(Context context) {
		mDialogView = View.inflate(context, R.layout.dialog_contact, null);
		mRelativeLayout = (RelativeLayout) mDialogView.findViewById(R.id.activity_about_dialog_rl);
		setContentView(mDialogView);
        
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                if(type==null){
                    type=Effectstype.SlideBottom;
                }
                start(type);  
            }
        });
        
        cancleTv = (TextView) findViewById(R.id.activity_about_contact_cancel_tv);
        dialTv = (TextView) findViewById(R.id.activity_about_contact_dial_tv);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
    private void start(Effectstype type){
        BaseEffects animator = type.getAnimator();
        if(mDuration != -1){
            animator.setDuration(Math.abs(mDuration));
        }
        animator.start(mRelativeLayout);
    }
	
    @Override
    public void dismiss() {
        super.dismiss();
    }
	
	public void onDialTvClick(View.OnClickListener dialListener) {
		dialTv.setOnClickListener(dialListener);
	}
	public void onCancleTvClick(View.OnClickListener cancleListener) {
		cancleTv.setOnClickListener(cancleListener);
	}
}