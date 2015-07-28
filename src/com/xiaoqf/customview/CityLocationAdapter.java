package com.xiaoqf.customview;

import java.util.List;
import com.xiaoqf.app.R;
import com.xiaoqf.common.Consts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CityLocationAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context _context;
	private List<String> cityArry;
	
	public CityLocationAdapter(Context context,List<String> objects) { 
		_context = context;
		cityArry = objects;
	    }	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cityArry.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cityArry.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LinearLayout listItemLayout = new LinearLayout(_context);
		String inflater = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater vi = (LayoutInflater) _context.getSystemService(inflater);
		vi.inflate(R.layout.citylocation_city_tv, listItemLayout, true);
		//final ImageView image = (ImageView) listItemLayout.findViewById(R.id.image_estate_logo);
		TextView tv = (TextView) listItemLayout.findViewById(R.id.activity_city_tv); 
		tv.setText(cityArry.get(position));
		if(position == Consts.City_SelectedPosition) {
			tv.setBackgroundResource(R.drawable.selector_yellor_location);
			tv.setTextColor(0xffff9933);
		}
		return listItemLayout;
		
		/* ViewHolder viewHolder; 
        if (convertView == null) 
        { 
            convertView = inflater.inflate(R.layout.citylocation_city_tv, null); 
            viewHolder = new ViewHolder(); 
            viewHolder.cityName = (TextView) convertView.findViewById(R.id.activity_city_tv); 
            //viewHolder.image = (ImageView) convertView.findViewById(R.id.image); 
            convertView.setTag(viewHolder); 
        } else
        { 
            viewHolder = (ViewHolder) convertView.getTag(); 
        } 
        viewHolder.cityName.setText(cityArry.get(position)); 
       // viewHolder.image.setImageResource(pictures.get(position).getImageId()); 
        return convertView;  */
	}
	class ViewHolder 
	{ 
	    public TextView cityName; 
	   // public ImageView image; 
	} 
}
