package com.xiaoqf.customview;

import com.xiaoqf.util.LogUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/** 
 * 
 * @ClassName: RoomSelectorThumbView
 * @Description: 选房缩略图视图
 * @author tongdu
 * @date 2015-5-19 下午3:47:11
 *
 */
public class RoomSelectorThumbView extends View {
	
	private static final String TAG = "RoomSelectorThumbView";
	private Bitmap bitmap = null;
	private Paint paint = null;

	public RoomSelectorThumbView(Context context, AttributeSet attributeset) {
		super(context, attributeset);
	}

	public void a(Bitmap paramBitmap) {
		this.bitmap = paramBitmap;
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		LogUtil.log(TAG, "onDraw()...");
		if (this.bitmap != null)
			canvas.drawBitmap(this.bitmap, 0.0F, 0.0F, this.paint);
	}
}