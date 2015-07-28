package com.xiaoqf.customview;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.xiaoqf.beans.Project;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.app.R;


@SuppressLint("ViewHolder") public class FloorAdapter extends ArrayAdapter<Project> {
	//private LayoutInflater inflater;
	private int itemId;
	private Context _context;
	public FloorAdapter(Context context,int textViewResourceId,List<Project> objects) { 
		super(context, textViewResourceId, objects);
		_context = context;
		this.itemId = textViewResourceId;
	    }	
	

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
    	Project project = getItem(position);
		LinearLayout listItemLayout = new LinearLayout(getContext());
		String inflater = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
		vi.inflate(itemId, listItemLayout, true);
		final ImageView image = (ImageView) listItemLayout.findViewById(R.id.image_estate_logo);


		if((parent.getChildCount() == position)) {
			
			final String logoUrl = project.getImgUrl();//parent.getChildCount() == position
			if (!"".equals(logoUrl)) {
				image.setImageResource(R.drawable.icon_default);
				try {
					/*AsyncImageLoader loader = new AsyncImageLoader(_context);
					loader.setCache2File(true);
					loader.setCachedDir(CommonUtil.getImageSavePath(_context));  //_context.getCacheDir().getAbsolutePath()
					loader.downloadImage(logoUrl, true, new AsyncImageLoader.ImageCallback() {
						@Override
						public void onImageLoaded(Bitmap bitmap, String imageUrl) {
							if (bitmap != null) {
								LogUtil.log("FloorAdapter", imageUrl);
								image.setImageBitmap(bitmap);
								//viewHolder.image.setImageBitmap(bitmap);
							} else {
								// 下载失败，设置默认图片
								image.setImageResource(R.drawable.icon_default);
								LogUtil.log("FloorAdapter", "下载失败!");
							}
						}
	
					});*/
					CommonUtil.loadImageFromUrl(_context, image, logoUrl);
					// new ImageUtil(image).execute(landBean.getLogourl());
				} catch (Exception e) {
					LogUtil.logError(e);
				}		
			}
		}
		return listItemLayout;
	}

}
