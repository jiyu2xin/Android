package com.xiaoqf.adapter;

import java.util.List;

import com.xiaoqf.beans.myLikeRoomOrderBean;
import com.xiaoqf.common.AsyncImageLoader;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.app.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RoomOrderListAdapter extends ArrayAdapter<myLikeRoomOrderBean> {

	private int itemId;
	private Context _context;
	
	public RoomOrderListAdapter(Context context,int textViewResourceId, List<myLikeRoomOrderBean> objects) {
		super(context,  textViewResourceId, objects);
		_context = context;
		this.itemId = textViewResourceId;
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

	@SuppressLint("ResourceAsColor") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		myLikeRoomOrderBean roomBean = getItem(position);
		LinearLayout listItemLayout = new LinearLayout(getContext());
		String inflater = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
		vi.inflate(itemId, listItemLayout, true);
		
		//TextView time =(TextView)listItemLayout.findViewById(R.id.time);
		//final ImageView image = (ImageView) listItemLayout.findViewById(R.id.logo_image);
		TextView projectInfroTextView =(TextView)listItemLayout.findViewById(R.id.room_item_project_name);

		TextView createTimeValueTextView =(TextView)listItemLayout.findViewById(R.id.room_item_register_text);
		TextView qFreeAmountTextView =(TextView)listItemLayout.findViewById(R.id.room_item_tv_xiaoqa_amount);
		TextView orgionAmountTextView =(TextView)listItemLayout.findViewById(R.id.room_item_tv_orgion_amount);
		TextView qFreeMaxAmountTextView =(TextView)listItemLayout.findViewById(R.id.room_item_tv_maxfree_amount);
		
		ImageView selectroomImageView =(ImageView)listItemLayout.findViewById(R.id.room_item_ic_select_room);
		ImageView towards1ImageView =(ImageView)listItemLayout.findViewById(R.id.room_item_ic_towards1);
		ImageView findImageView =(ImageView)listItemLayout.findViewById(R.id.room_item_ic_room);
		ImageView towards2ImageView =(ImageView)listItemLayout.findViewById(R.id.room_item_ic_towards2);
		ImageView pre_walletImageView =(ImageView)listItemLayout.findViewById(R.id.room_item_ic_pre_wallet);
		ImageView towards3ImageView =(ImageView)listItemLayout.findViewById(R.id.room_item_ic_towards3);
		ImageView weixin_shareImageView =(ImageView)listItemLayout.findViewById(R.id.room_item_ic_weixin_share);
		ImageView towards4ImageView =(ImageView)listItemLayout.findViewById(R.id.room_item_ic_towards4);
		ImageView full_amountImageView =(ImageView)listItemLayout.findViewById(R.id.room_item_ic_full_amount);
		
		TextView select_room_textTextView =(TextView)listItemLayout.findViewById(R.id.room_item_ic_select_room_text);
		TextView find_textTextView =(TextView)listItemLayout.findViewById(R.id.room_item_ic_room_text);
		TextView pre_wallet_textTextView =(TextView)listItemLayout.findViewById(R.id.room_item_ic_pre_wallet_text);
		TextView weixin_share_textTextView =(TextView)listItemLayout.findViewById(R.id.room_item_ic_weixin_share_text);
		TextView full_amount_textTextView =(TextView)listItemLayout.findViewById(R.id.room_item_ic_full_amount_text);
		
		ImageView logoImageView =(ImageView)listItemLayout.findViewById(R.id.room_item_mylikes_iv);
		
		String roominfro = roomBean.getFloorNUm()+"栋"+roomBean.getRoomNum()+"房";
		final String logoUrl = roomBean.getLogoImageUrl();
		if(parent.getChildCount() == position) {
			if (!"".equals(logoUrl)) {
				logoImageView.setBackgroundResource(R.drawable.ic_mylikes_room_default);
				try {
					CommonUtil.loadImageFromUrl(_context, logoImageView, logoUrl);
					
				} catch (Exception e) {
					LogUtil.logError(e);
				}
			}
		}
		//time.setText(roomBean.getTaskCreateTime());
			
		switch(Integer.parseInt(roomBean.getOrderHandleProcess())) {
		case 1:
			//createTimeTextView.setVisibility(View.GONE);
			//createTimeValueTextView.setTextSize(12.0f);
			createTimeValueTextView.setText("支付定金");
			//createTimeValueTextView.setBackgroundResource(R.drawable.room_selector_yellor_shape);
			
			selectroomImageView.setSelected(true);
			select_room_textTextView.setTextColor(0xFFff5a00);
			
			break;
		case 2:
			createTimeValueTextView.setText("支付定金");
			selectroomImageView.setSelected(true);
			towards1ImageView.setSelected(true);
			findImageView.setSelected(true);
			select_room_textTextView.setTextColor(0xFFff5a00);
			find_textTextView.setTextColor(0xFFff5a00);
			break;
		case 3:
			createTimeValueTextView.setText("邀请朋友去砍价");
			selectroomImageView.setSelected(true);
			towards1ImageView.setSelected(true);
			pre_walletImageView.setSelected(true);
			findImageView.setSelected(true);
			towards2ImageView.setSelected(true);
			select_room_textTextView.setTextColor(0xFFff5a00);
			find_textTextView.setTextColor(0xFFff5a00);
			pre_wallet_textTextView.setTextColor(0xFFff5a00);
			
			break;
		case 4:
			createTimeValueTextView.setText("继续Q价");
			selectroomImageView.setSelected(true);
			towards1ImageView.setSelected(true);
			findImageView.setSelected(true);
			towards2ImageView.setSelected(true);
			pre_walletImageView.setSelected(true);
			towards3ImageView.setSelected(true);
			weixin_shareImageView.setSelected(true);
			select_room_textTextView.setTextColor(0xFFff5a00);
			pre_wallet_textTextView.setTextColor(0xFFff5a00);
			weixin_share_textTextView.setTextColor(0xFFff5a00);
			find_textTextView.setTextColor(0xFFff5a00);
			break;
		case 5:
			createTimeValueTextView.setText("签约成功");
			selectroomImageView.setSelected(true);
			towards1ImageView.setSelected(true);
			pre_walletImageView.setSelected(true);
			towards2ImageView.setSelected(true);
			weixin_shareImageView.setSelected(true);
			towards3ImageView.setSelected(true);
			full_amountImageView.setSelected(true);
			findImageView.setSelected(true);
			towards4ImageView.setSelected(true);
			select_room_textTextView.setTextColor(0xffff5a00);
			pre_wallet_textTextView.setTextColor(0xFFff5a00);
			weixin_share_textTextView.setTextColor(0xFFff5a00);
			full_amount_textTextView.setTextColor(0xFFff5a00);
			find_textTextView.setTextColor(0xFFff5a00);
			break;
		}
		projectInfroTextView.setText(roomBean.getProjectName());
		qFreeAmountTextView.setText(roomBean.getOrderMaxFreeAmount()+"元");
		orgionAmountTextView.setText("原价: "+roomBean.getOriginalPrice()+"元");
		orgionAmountTextView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG |Paint.ANTI_ALIAS_FLAG);
		qFreeMaxAmountTextView.setText(roomBean.getOrderUnAmount());
		
		return listItemLayout;
	}

}
