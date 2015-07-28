package com.xiaoqf.view;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiaoqf.util.ImageConvertUtil;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.app.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class UserInfoActivity extends BaseActivity {

	private static final String IMAGE_UNSPECIFIED = "image/*";
	private static final String TAG = "UserInfoActivity";

	private static final int ALBUM_REQUEST_CODE = 1;
	private static final int CAMERA_REQUEST_CODE = 2;
	private static final int CROP_REQUEST_CODE = 4;
	private File picture = null;


	private Uri imageUri = null;
	private View view;
	private PopupWindow pop;
	private TextView openCamera;
	private TextView openPictures;
	
	@ViewInject(R.id.tv_title)
	private TextView titleTextView;

	@ViewInject(R.id.activity_userinfo_item_go)
	private ImageView myimageView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);
		ViewUtils.inject(this);
		init();
	}

	private void init() {
		titleTextView.setText("个人信息");
		picture = new File(Environment.getExternalStorageDirectory()
				+ "/xiaoQfang");
		if (!picture.exists()) {
			picture.mkdir();
		}
		File file = new File(picture, "user.png");
		imageUri = Uri.fromFile(file);
		if(file.exists()) {
			Uri imageUri = Uri.fromFile(file);
			Bitmap bitmap =ImageConvertUtil.decodeUriAsBitmap(this, imageUri);
			myimageView.setImageBitmap(bitmap);
		}
		
		initPopupWindow();
	}

	//初始化popupWindow
	private void initPopupWindow() {
		view = this.getLayoutInflater().inflate(R.layout.activity_pickimage, null);
		pop = new PopupWindow(view,ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT);
		
		openCamera = (TextView) view.findViewById(R.id.popw_camera_btn);
		openCamera.setOnClickListener(onOpenCameraClickListener);
		openPictures =  (TextView) view.findViewById(R.id.popw_pictures_btn);
		openPictures.setOnClickListener(onOpenPicturesClickListener);
		pop.setFocusable(true);

		view.setOnTouchListener(myTouchListener);
	}
	
	//点击响应头像按钮列
	@OnClick(R.id.activity_userinfo_item_change)  
	void onImageIconClick(View v) {
		//View LinearView = view.findViewById(R.id.firm_linearlayout);
		pop.showAtLocation(view,Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	/*
	 * 打开图片进行剪切
	 */
	private void openPicture2Crop() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				IMAGE_UNSPECIFIED);
		startActivityForResult(intent, ALBUM_REQUEST_CODE);
	}

	/*
	 * 打开相机进行剪切工作
	 */
	private void openCamera2Crop() {
		// 打开相机
		File file = new File(picture, "user.png");
		if (file.exists())
			file.delete();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		// LogUtil.log(TAG, Uri.fromFile(file).toString());
		startActivityForResult(intent, CAMERA_REQUEST_CODE);
	}

	/**
	 * 开始裁剪
	 * 
	 * @param uri
	 */
	private void startCrop(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");// 调用Android系统自带的一个图片剪裁页面,
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");// 进行修剪
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 120);
		intent.putExtra("outputY", 120);
		// intent.putExtra("scale",true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
		// intent.putExtra("noFaceDetection", true); // no face detection
		// intent.putExtra("circleCrop", true);
		//intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_REQUEST_CODE);
	}

	/**
	 * 判断sdcard卡是否可用
	 * 
	 * @return 布尔类型 true 可用 false 不可用
	 */
	private boolean isSDCardCanUser() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case ALBUM_REQUEST_CODE:
			Log.i(TAG, "相册，开始裁剪");
			if (data == null) {
				return;
			}
			startCrop(data.getData());
			break;
		case CAMERA_REQUEST_CODE:
			Log.i(TAG, "相机, 开始裁剪");
			File pic = new File(picture, "user.png");
			startCrop(Uri.fromFile(pic));
			break;
		case CROP_REQUEST_CODE:
			Log.i(TAG, "相册裁剪成功");
			if (data == null) {
				//TODO 如果之前以后有设置过显示之前设置的图片 否则显示默认的图片
				return;
			}
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.PNG, 75, stream);// (0-100)压缩文件
				photo = getCircleBitmap(photo, 0);
				myimageView.setImageBitmap(photo);
				save2SDCARD(photo);
			}
			/*
			 if(imageUri!=null) {
				 Bitmap bitmap = decodeUriAsBitmap(imageUri);
				 // (0-100)压缩文件 
				 bitmap = getCircleBitmap(bitmap, 0);
				 myimageView.setImageBitmap(bitmap); save2SDCARD(bitmap); 
			 }*/
			 
			break;
		default:
			break;
		}
	}

	// 转换成圆形图
	public Bitmap getCircleBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xffff0000;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setFilterBitmap(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawOval(rectF, paint);

		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth((float) 4);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public void save2SDCARD(Bitmap bitmap) {

		File file = new File(picture, "user.png");

		try {
			FileOutputStream iStream = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, iStream);
			iStream.close();
			iStream = null;
			file = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.log(TAG, Environment.getExternalStorageDirectory()
					+ ":图片处理异常！");
		}
	}

	//popupWindow
	OnClickListener onOpenCameraClickListener = new View.OnClickListener() {	
		@Override
		public void onClick(View v) {
			pop.dismiss();
			openCamera2Crop();
		}
	};
	
	OnClickListener onOpenPicturesClickListener = new View.OnClickListener() {	
		@Override
		public void onClick(View v) {
			pop.dismiss();
			openPicture2Crop();
		}
	};
	
	OnTouchListener myTouchListener = new View.OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent arg1) {
			LogUtil.log(TAG, view.findViewById(R.id.firm_linearlayout).toString()+v.toString());
			if(pop.isShowing()&&(v!=view.findViewById(R.id.firm_linearlayout)))
				pop.dismiss();
			return false;
		}
	};
	
	@OnClick(R.id.iv_title_left)
	private void onBackClick(View v) {
		super.onBackPressed();
	}
}