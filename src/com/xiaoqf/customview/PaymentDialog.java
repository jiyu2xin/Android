package com.xiaoqf.customview;

import com.xiaoqf.app.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class PaymentDialog extends Dialog implements DialogInterface {
	private View mDialogView;
	
	private RelativeLayout alipayRl;
	private RelativeLayout weixinRl;
	
	private Effectstype type = null;
	
	private static Context tmpCtx;
	private int mDuration = 700*1;
	private RelativeLayout mRelativeLayout;
	
	private static PaymentDialog instance;

	public PaymentDialog(Context context) {
		super(context);
		init(context);
	}
	
	public PaymentDialog(Context context, int theme) {
		super(context);
		init(context);
	}
	
	public static PaymentDialog getInstance(Context context) {
		if (instance == null || !tmpCtx.equals(context)) {
			synchronized (PaymentDialog.class) {
				instance = new PaymentDialog(context, R.style.paymentdialog);
			}
		}
		tmpCtx = context;
		return instance;
	}

	private void init(Context context) {
		mDialogView = View.inflate(context, R.layout.dialog_pay_select, null);
		mRelativeLayout = (RelativeLayout) mDialogView.findViewById(R.id.activity_bargain_pay_select_dialog_rl);
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
        
        alipayRl = (RelativeLayout) findViewById(R.id.activity_bargain_pay_by_alipay_rl);
        weixinRl = (RelativeLayout) findViewById(R.id.activity_bargain_pay_by_wxpay_rl);
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
    
    public void onAlipayRlClick(View.OnClickListener alipayListener) {
    	alipayRl.setOnClickListener(alipayListener);
    }

    public void onWeixinRlClick(View.OnClickListener weixinListener) {
    	weixinRl.setOnClickListener(weixinListener);
    }
}
