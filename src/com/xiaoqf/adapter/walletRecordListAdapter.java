package com.xiaoqf.adapter;

import java.util.List;

import com.xiaoqf.beans.walletRecordBean;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.app.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class walletRecordListAdapter extends ArrayAdapter<walletRecordBean> {

	private int itemId;
	private Context _context;
	private static int recordCurrentMonth =0;
	private static int recordCurrentYear =0;
	private static int recordCurrentPosition =0;
	private String str;
	public walletRecordListAdapter(Context context,int textViewResourceId, List<walletRecordBean> objects,String str) {
		super(context,  textViewResourceId, objects);
		_context = context;
		this.itemId = textViewResourceId;
		this.str =str;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return super.getCount();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
	}

	@SuppressLint("ViewHolder") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LogUtil.log("walletRecordListAdapter", position+"");
		walletRecordBean recordBean = getItem(position);
		String Month = recordBean.getTransactionMonth();
		String year = recordBean.getTransactionYear();
		String myPosition = recordBean.getTransactionCounts();

		LinearLayout listItemLayout = new LinearLayout(getContext());
		String inflater = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
		vi.inflate(itemId, listItemLayout, true);
		
		TextView recordMonth =(TextView)listItemLayout.findViewById(R.id.activity_order_item_month);
		View recordTopLine =(View)listItemLayout.findViewById(R.id.activity_order_item_top_line);
		View recordTopHalfLine =(View)listItemLayout.findViewById(R.id.activity_order_item_top_half_line);
		TextView recordAmount =(TextView)listItemLayout.findViewById(R.id.activity_order_item_free_value);
		TextView recordTime =(TextView)listItemLayout.findViewById(R.id.activity_order_item_create_time);
		View recordBottonLine =(View)listItemLayout.findViewById(R.id.activity_order_item_botton_line);
		View recordBottonSpace =(View)listItemLayout.findViewById(R.id.activity_order_item_botton_space);
		ImageView imageView = (ImageView) listItemLayout.findViewById(R.id.activity_order_item_password_change_go);
		TextView recordAmountText =(TextView)listItemLayout.findViewById(R.id.activity_order_item_amount_text);
		
		if(parent.getChildCount()==position) {	
			if("0".equals(str)) {
				imageView.setImageResource(R.drawable.ic_cash);
				recordAmountText.setText("现金");
			}
			if((Integer.parseInt(Month)==recordCurrentMonth)&&(Integer.parseInt(year)==recordCurrentYear)) {
				LogUtil.log("walletRecordListAdapter",position+"1:"+"month is  current month"+Integer.parseInt(myPosition));
				if(position==recordCurrentPosition) {
					recordMonth.setVisibility(View.VISIBLE);
					recordMonth.setText(Month+"月");
					recordTopLine.setVisibility(View.VISIBLE);
					recordTopHalfLine.setVisibility(View.INVISIBLE);			
				}
				else
					listItemLayout.removeView(recordMonth);
				if(position!=(recordCurrentPosition+Integer.parseInt(myPosition)-1))
					listItemLayout.removeView(recordBottonSpace);
				else {
					recordBottonSpace.setVisibility(View.VISIBLE);
					recordBottonLine.setVisibility(View.VISIBLE);
				}
								
			}else {
				recordMonth.setVisibility(View.VISIBLE);
				recordMonth.setText(Month+"月");
				recordCurrentMonth =Integer.parseInt(Month);
				recordCurrentYear =Integer.parseInt(year);
				recordCurrentPosition = position;
				LogUtil.log("walletRecordListAdapter",position+"2:"+"month is not current month");
			}
			
			String amount =recordBean.getTransactionAmount();
			recordAmount.setText((Double.parseDouble(amount)>=0?"+":"")+amount+"元");
			recordTime.setText(recordBean.getTransactionCreateTime());
		}
			
		return listItemLayout;
	}

}
