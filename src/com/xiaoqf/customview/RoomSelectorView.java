package com.xiaoqf.customview;

import java.util.ArrayList;

import com.xiaoqf.beans.Floor;
import com.xiaoqf.beans.Room;
import com.xiaoqf.common.Consts;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.view.OnRoomClickListener;
import com.xiaoqf.app.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RoomSelectorView extends View {
	// 房间的状态
	private static final int Room_Space = -2;
	private static final int Room_Type_None = Room.None;
	private static final int Room_Type_Normal = Room.Normal;//无
	private static final int Room_Type_Sold = Room.Sold;
	private static final int Room_Type_Checked = Room.Checked;
	private static final int Room_Type_Prescale = Room.Presale; //预售
	private static final String TAG = "RoomSelectorView";
	
	private Context mContext;
	
	// 房间图示状态
	private Bitmap mBitMapRoomNormal = null;
	private Bitmap mBitMapRoomSold = null;
	private Bitmap mBitMapRoomChecked = null;
	private Bitmap mBitMapRoomNone = null;//无房状态
	private Bitmap mBitMapRoomPrescale = null;//预售
	/** 缩略图画布 */
	private Canvas thumbCanvas = null;

	/** 是否显示缩略图 */
	private static boolean showThumb = false;
	/** 是否可缩放 */
	private static boolean isScalable = false;
	/** 能否移动 */
	private static boolean isMoveable = true;
	/** 是否有左侧楼层索引栏 */
	private static boolean hasFloorIdx = false;

	/** 每个房间的高度 - 57 */
	private int rs_room_current_height = 80;//49
	/** 每个房间的宽度 */
	private int rs_room_current_width = 90;//60
	/** 房间的高宽比 */
	private static final double ratioHW = 0.8d;
//	private final double ratioHW = (double)rs_room_current_height / (double)rs_room_current_width;
	/** 房间宽度与左边距的比例 */
	private final double ratioWL = (hasFloorIdx) ? 2.0d : Double.MAX_VALUE;
	/** 房间之间的间距 */
	private int spacing = this.rs_room_current_width / 4;//6
	
	/** 缩放相关系数 */
	private double scaleFactor = 1.0D;
	/** 触屏操作缩放系数 */
	private double t = -1.0D;
	private double u = 1.0D;

	/** 房间最小高度 */
	private int rs_room_min_height = 0;
	/** 房间最大高度 */
	private int rs_room_max_height = 0;
	/** 房间最小宽度 */
	private int rs_room_min_width = 0;
	/** 房间最大宽度 */
	private int rs_room_max_width = 0;

	/** 房间点击监听器 */
	private OnRoomClickListener mOnRoomClickListener = null;

	private int rs_between_offset = 2;
	private int rs_room_check_size = 50;
	private int rs_room_thumb_size_w = 120;
	private int rs_room_thumb_size_h = 90;
	private int rs_room_rect_line = 2;
	
	/** 选房缩略图 */
	private Bitmap mBitMapThumbView = null;
	private RoomSelectorThumbView mRSThumbView = null;
	private volatile int V = 1500;
	
	/** 左边距 */
	private int marginLeft = 0;
	/** 右边距 */
	private int marginRight = 0;
	/** 上边距 */
	private int marginTop = 0;
	/** 下边距 */
	private int marginBottom = 0;
	
	/** 排数x轴偏移量 */
	private float xOffsetRow = 0.0F;
	/** 排数y轴偏移量 */
	private float yOffsetRow = 0.0F;
	/** 房间距离排数的距离 */
	private int xOffsetInRow = 0;
	/** 可视房间距离顶端的距离 */
	private int yOffsetInRow = 0;
	
	/** 整个view的宽度 */
	private int rs_view_width = 0;
	/** 整个view的高度 */
	private int rs_view_height = 0;

	private boolean first_load_bg = true;
	private int tempX;
	private int tempY;
	
	GestureDetector mGestureDetector =
			new GestureDetector(mContext, new GestureListener(this));

	/** 整栋房间信息 */
	private ArrayList<Floor> building = null;
	/** 整栋房间状态信息： */
	private ArrayList<ArrayList<Integer>> roomStates = null;
	/** 限购数 */
	private int iMaxPay = 0;
	private int totalCountEachRow;
	private int rows;
	
	public RoomSelectorView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoomSelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
	}

	public void init(int row_count, int rows,
			ArrayList<Floor> list_rooms,
			ArrayList<ArrayList<Integer>> list_room_condtions,
			RoomSelectorThumbView rsThumbView, int imaxPay) {
		this.iMaxPay = imaxPay;
		this.mRSThumbView = rsThumbView;
		if (null == rsThumbView) {
			setThumb(this, false);
		}
		
		this.totalCountEachRow = row_count;
		this.rows = rows;
		this.building = list_rooms;
		this.roomStates = list_room_condtions;
		this.mBitMapRoomNormal = getBitmapFromDrawable((BitmapDrawable) this.mContext
				.getResources().getDrawable(R.drawable.room_normal));
		this.mBitMapRoomSold = getBitmapFromDrawable((BitmapDrawable) this.mContext
				.getResources().getDrawable(R.drawable.room_sold));
		this.mBitMapRoomChecked = getBitmapFromDrawable((BitmapDrawable) this.mContext
				.getResources().getDrawable(R.drawable.room_checked));
		this.mBitMapRoomNone = getBitmapFromDrawable((BitmapDrawable) this.mContext
				.getResources().getDrawable(R.drawable.room_none));//无房
		this.mBitMapRoomPrescale = getBitmapFromDrawable((BitmapDrawable) this.mContext
				.getResources().getDrawable(R.drawable.room_sold));//预售
		

		this.rs_room_thumb_size_w = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.rs_room_thumb_size_w);
		this.rs_room_thumb_size_h = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.rs_room_thumb_size_h);
		
		if (row_count > 4) {
			this.rs_room_max_width = this.mContext.getResources()
					.getDimensionPixelSize(R.dimen.room_max_width);
			this.rs_room_min_width = this.mContext.getResources()
					.getDimensionPixelSize(R.dimen.room_min_width);
			this.rs_room_min_height = (int)(this.rs_room_min_width * ratioHW);
			this.rs_room_current_width = this.mContext.getResources()
					.getDimensionPixelSize(R.dimen.room_init_width);
			this.rs_room_check_size = this.mContext.getResources()
					.getDimensionPixelSize(R.dimen.rs_room_check_size);//30dp
			this.rs_between_offset = this.mContext.getResources()
					.getDimensionPixelSize(R.dimen.rs_between_offset);//5dp
		} else {
			this.rs_room_max_width = Consts.devWidth - 2 * spacing;
			this.rs_room_current_width =
				(Consts.devWidth - marginLeft - marginRight 
				- (row_count-4)*spacing) / row_count;
		}
		this.rs_room_max_height = (int)(this.rs_room_max_width * ratioHW);
		this.rs_room_current_height = (int)(this.rs_room_current_width * ratioHW);
		
		invalidate();
	}

	/**
	 * Returns the bitmap used by this drawable to render. May be null.
	 * @param mBitmapDrawable
	 * @return
	 */
	public static Bitmap getBitmapFromDrawable(
			BitmapDrawable mBitmapDrawable) {
		return mBitmapDrawable.getBitmap();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		LogUtil.log(TAG, "onDraw()...");
		canvas.drawColor(getResources().getColor(R.color.gray_room_selector_bg));
		
		// 房间数为空时，直接返回
		if (this.totalCountEachRow == 0 || this.rows == 0) {
			return;
		}
		
		// 当前视图越界时
		if (this.xOffsetRow + this.rs_view_width < 0.0f ||
			this.yOffsetRow + this.rs_view_height < 0.0f) {
			this.xOffsetRow = 0.0f;
			this.yOffsetRow = 0.0f;
			this.xOffsetInRow = 0;
			this.yOffsetInRow = 0;
		}

		Paint thumbPaint = new Paint();
		if (this.rs_room_current_width != 0 && this.rs_room_current_height != 0) {

			// 缩略图.初始化
			this.mBitMapThumbView = Bitmap.createBitmap(this.rs_room_thumb_size_w,
					this.rs_room_thumb_size_h, Bitmap.Config.ARGB_8888);
			this.thumbCanvas = new Canvas();
			this.thumbCanvas.setBitmap(this.mBitMapThumbView);
			this.thumbCanvas.save();
			
			// 选房图.初始化
			Paint selectorPaint = new Paint();
			selectorPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			this.thumbCanvas.drawPaint(selectorPaint);
			 
			double scaleOnX = (this.rs_room_thumb_size_w - 10.0D)
					/ (this.rs_room_current_width * this.totalCountEachRow
						+ this.marginLeft + this.marginRight);

			double scaleOnY = (this.rs_room_thumb_size_h - 10.0D)
					/ (this.rs_room_current_height * this.rows);
			
			if (scaleOnX <= scaleOnY) {
				this.scaleFactor = scaleOnX;
			} else {
				this.scaleFactor = scaleOnY;
			}
			
			// 是否显示缩略图
			if(this.showThumb){
				thumbPaint.setColor(Color.BLACK);
				if(first_load_bg){
					first_load_bg = false;
					tempX = 5 + (int) (this.rs_view_width * this.scaleFactor);
					tempY = 5 + (int) (this.rs_view_height * this.scaleFactor);
				}
				this.thumbCanvas.drawRect(5.0F, 5.0F,  tempX, tempY, thumbPaint);
			}
		}

		// 偏移量的转换
		canvas.translate(this.xOffsetRow, this.yOffsetRow);
		// 计算view的宽度
		this.rs_view_width = this.marginLeft + this.rs_room_current_width * this.totalCountEachRow + this.marginRight;
		// 计算view的高度
		this.rs_view_height = this.rs_room_current_height * this.rows;
		// 计算左边距
		this.marginLeft = (int) Math.round(this.rs_room_current_width / ratioWL);
//		this.marginLeft = (int) Math.round(this.rs_room_current_width / 2.0D);
		thumbPaint.setTextAlign(Paint.Align.CENTER);
		thumbPaint.setAntiAlias(true);
		thumbPaint.setColor(Color.BLACK);
		
		for (int tmpRow = 0; tmpRow < this.roomStates.size(); tmpRow++) {
			ArrayList<Integer> tmpRoomConds = this.roomStates.get(tmpRow);

			for (int tmpCol = 0;
					tmpCol < this.building.get(tmpRow).getFloor().size(); tmpCol++) {				
				Room room = this.building.get(tmpRow).getRoom(tmpCol);
				switch (((Integer) tmpRoomConds.get(tmpCol)).intValue()) { // 2373
				case Room_Space: // 2401 - 走道
					thumbPaint.setColor(Color.TRANSPARENT);
					drawRoomOnBoth(tmpCol, tmpRow, null, canvas, this.thumbCanvas, thumbPaint);
					thumbPaint.setColor(Color.BLACK);
					break;
				case Room_Type_Normal:// 正常可选
					drawRoomOnBoth(tmpCol, tmpRow, this.mBitMapRoomNormal, canvas,
							this.thumbCanvas, thumbPaint);
					break;
				case Room_Type_Sold://
					drawRoomOnBoth(tmpCol, tmpRow, this.mBitMapRoomSold, canvas,
							this.thumbCanvas, thumbPaint);
					break;
				case Room_Type_Checked: // 2500-一已点击的状态
					drawRoomOnBoth(tmpCol, tmpRow, this.mBitMapRoomChecked, canvas,
							this.thumbCanvas, thumbPaint);
					break;
				case Room_Type_None: // 无房
					drawRoomOnBoth(tmpCol, tmpRow, this.mBitMapRoomNone, canvas,
							this.thumbCanvas, thumbPaint);
					break;
				case Room_Type_Prescale: // 无房
					drawRoomOnBoth(tmpCol, tmpRow, this.mBitMapRoomPrescale, canvas,
							this.thumbCanvas, thumbPaint);
					break;
				default:
					break;
				}
			}
			// cond_d - 2538
		}

		// 画左侧排数索引栏 
//		drawFloorIdx(canvas, thumbPaint);
		
		if (this.showThumb) {
			// 画缩略图的黄色框
			thumbPaint.setColor(
					this.mContext.getResources().getColor(R.color.yellow_thumb_border));
			thumbPaint.setStyle(Paint.Style.STROKE);
			thumbPaint.setStrokeWidth(this.rs_room_rect_line);
			this.thumbCanvas.drawRect(
					borderOnThumb((int) Math.abs(this.xOffsetRow), (int) Math.abs(this.yOffsetRow)),
					thumbPaint);
			thumbPaint.setStyle(Paint.Style.FILL);
			// paramCanvas.restore();
			this.thumbCanvas.restore();
		}

		if (this.mRSThumbView != null) {
			this.mRSThumbView.a(mBitMapThumbView);
			this.mRSThumbView.invalidate();
		}
	}

	/**
	 * 绘制左侧楼层索引栏
	 * @param canvas
	 * @param thumbPaint
	 */
	private void drawFloorIdx(Canvas canvas, Paint thumbPaint) {
		float idxTextSize = 0.4F * this.rs_room_current_height;
		int idxTextColor = Color.WHITE;
		for (int tmpRow = 0; tmpRow < this.building.size(); tmpRow++) {
			thumbPaint.setColor(
				this.mContext.getResources().getColor(R.color.black_floor_index_bg));
			Rect rect = new Rect(
				(int) Math.abs(this.xOffsetRow),
				this.marginTop	+ tmpRow * this.rs_room_current_height,
				(int) Math.abs(this.xOffsetRow) + (int) (this.rs_room_current_width / ratioWL),
				this.marginTop + (tmpRow + 1) * this.rs_room_current_height);
			canvas.drawRect(rect, thumbPaint);
			
			String text = this.building.get(tmpRow).getFloorNum();
			drawTextCenterInRect(text, idxTextSize, idxTextColor, canvas, thumbPaint, rect);
		}
	}
	
	/**
	 * 触屏移动事件处理
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 单点触控处理
		if (event.getPointerCount() == 1) {
			if (this.isScalable) {
				this.isScalable = false;
				this.isMoveable = false;
				this.t = -1.0D;
				this.u = 1.0D;
			}else{
				this.isMoveable = true;
			}

			// Toast.makeText(mContext, "单点触控", Toast.LENGTH_SHORT).show();
			while (this.rs_room_current_width < this.rs_room_min_width || this.rs_room_current_height < this.rs_room_min_height) {
				this.rs_room_current_width++;
				this.rs_room_current_height=(int)(this.rs_room_current_width * ratioHW);
				this.marginLeft = (int) Math.round(this.rs_room_current_width / ratioWL);
				this.marginRight = this.marginLeft;
				this.spacing = getSpacing();
				// 滑到最左和最上
				RoomSelectorView.setXOffsetInRow(this, 0);
				RoomSelectorView.setXOffSetRow(this, 0.0F);
				RoomSelectorView.setYOffsetInRow(this, 0);
				RoomSelectorView.setYOffsetRow(this, 0.0F);
				invalidate();
			}
			while ((this.rs_room_current_width > this.rs_room_max_width) || (this.rs_room_current_height > this.rs_room_max_height)) {
				this.rs_room_current_width--;
				this.rs_room_current_height=(int)(this.rs_room_current_width * ratioHW);
				this.marginLeft = (int) Math.round(this.rs_room_current_width / ratioWL);
				this.marginRight = this.marginLeft;
				this.spacing = getSpacing();
				invalidate();
			}
			// 移动功能-点击事件
			this.mGestureDetector.onTouchEvent(event);
		
		} else {
			// 多点触控处理
			this.isScalable = true;
			onMultiTouchEvent(event);
		}
		
		return true;
	}

	/**
	 * 多点触控处理
	 * @param event
	 */
	private void onMultiTouchEvent(MotionEvent event) {
		double mPointsLen = getTwoPointerDistance(event);
		if (this.t < 0.0D) {
			this.t = mPointsLen;
		} else {
			try {
				this.u = (mPointsLen / this.t);
				this.t = mPointsLen;
				if ((this.isScalable)
						&& (Math.round(this.u * this.rs_room_current_width) > 0L)
						&& (Math.round(this.u * this.rs_room_current_height) > 0L)) {
					this.rs_room_current_width = (int) Math.round(this.u * this.rs_room_current_width);
					this.rs_room_current_height = (int) Math.round(this.rs_room_current_width * ratioHW);
					this.marginLeft = (int) Math.round(this.rs_room_current_width / ratioWL);
					this.marginRight = this.marginLeft;
					this.spacing = (int) Math.round(this.u * this.spacing);
					if (this.spacing <= 0)
						this.spacing = 1;
				}
				invalidate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取MotionEvent前2点的直线距离
	 * @param event
	 * @return
	 */
	private double getTwoPointerDistance(MotionEvent event) {
		float x_delta = event.getX(0) - event.getX(1);
		float y_delta = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x_delta * x_delta + y_delta * y_delta);
	}

	/**
	 * 计算间隔距离
	 * @return
	 */
	private int getSpacing() {
		return (int) Math.round(this.rs_room_current_width / 6);
		/*return (int) Math.round(this.rs_room_current_width / this.rs_room_check_size
				* this.rs_between_offset);*/
	}
	
	/**
	 * 分别绘制指定房间矩形(在选房图、缩略图中)
	 * @param colNum
	 *            每排的房间顺序号
	 * @param rowNum
	 *            排号
	 * @param bitmap
	 * @param mainCanvas
	 * @param thumbCanvas
	 * @param paint
	 */
	private void drawRoomOnBoth(int colNum, int rowNum, Bitmap bitmap,
			Canvas mainCanvas, Canvas thumbCanvas, Paint paint) {
		Room room = building.get(rowNum).getRoom(colNum);
		int sellState = Integer.valueOf(room.getSellState());
		String text = room.getName();
		float textSize = ((float)0.3 * this.rs_room_current_height);
		int color = getResources().getColor(R.color.gray_room_font);
		Rect roomOnMain = roomOnMain(colNum, rowNum);
		
		if (sellState == Room.Sold || sellState == Room.Presale) {
			text = "已售";
			color = 0xffc4c4c4;
		}
		if (sellState == Room.Presale) {
			text = "预售";//显示预售
			color = 0xffc4c4c4;
		}
		if (sellState == Room.None) {
			text = "无";//显示无
			color = 0xffcfcfcf;
		}
		if (roomStates.get(rowNum).get(colNum) == Room.Checked) {
			color = Color.WHITE;
		}
		
		if (Consts.isDebugMode) {
			paint.setColor(Color.BLUE);
			paint.setStyle(Style.FILL);
		}
		if (bitmap == null) {
			// public void drawRect (Rect r, Paint paint) 
			mainCanvas.drawRect(roomOnMain, paint);
			// 绘制房间文字信息（编号、销售信息）
			drawTextCenterInRect(text, textSize, color, mainCanvas, paint, roomOnMain);
			if (this.showThumb) {
				thumbCanvas.drawRect(roomOnThumb(colNum, rowNum), paint);
			}
		} else {
			mainCanvas.drawBitmap(bitmap, null, roomOnMain,	paint);
			drawTextCenterInRect(text, textSize, color, mainCanvas, paint, roomOnMain);
			if (this.showThumb) {
				thumbCanvas.drawBitmap(bitmap, null, roomOnThumb(colNum, rowNum),
						paint);
			}
		}
	}
	
	/**
	 * 在指定矩形中心写字
	 * @param text
	 * @param textSize
	 * @param color
	 * @param canvas
	 * @param paint
	 * @param rect
	 */
	private void drawTextCenterInRect(String text, float textSize, int color,
		Canvas canvas, Paint paint, Rect rect) {
		paint.setTextSize(textSize);
		paint.setColor(color);
		FontMetricsInt fontMetrics = paint.getFontMetricsInt();
		int baseline = rect.top + (rect.bottom - rect.top) / 2
			- (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top;
		canvas.drawText(text, rect.centerX(), baseline, paint);
	}

	/**
	 * 指定房间在选房图中的矩形
	 * @param colNum
	 *            每排的房间号
	 * @param rowNum
	 *            排号
	 * @return
	 */
	private Rect roomOnMain(int colNum, int rowNum) {
		try {
			// public Rect (int left, int top, int right, int bottom)
			Rect localRect = new Rect(
					this.marginLeft + colNum * this.rs_room_current_width + this.spacing,
					this.marginTop + rowNum * this.rs_room_current_height + this.spacing,
					this.marginLeft + (colNum + 1)	* this.rs_room_current_width - this.spacing,
					this.marginTop + (rowNum + 1) * this.rs_room_current_height - this.spacing);
			return localRect;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return new Rect();
	}


	/**
	 * 指定房间在缩略图中的矩形
	 * @param colNum
	 *            每排的房间号
	 * @param rowNum
	 *            排号
	 * @return
	 */
	private Rect roomOnThumb(int colNum, int rowNum) {
		try {
			Rect localRect = new Rect(
				5 + (int) (this.scaleFactor * (this.marginLeft + colNum * this.rs_room_current_width + this.spacing)),
				5 + (int) (this.scaleFactor * (this.marginTop + rowNum * this.rs_room_current_height + this.spacing)),
				5 + (int) (this.scaleFactor * (this.marginLeft + (colNum + 1) * this.rs_room_current_width - this.spacing)),
				5 + (int) (this.scaleFactor * (this.marginTop + (rowNum + 1) * this.rs_room_current_height - this.spacing)));
			return localRect;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return new Rect();
	}

	/**
	 * 缩略图中的高亮矩形框
	 * @param colNum
	 *            每排的房间号
	 * @param rowNum
	 *            排号
	 * @return
	 */
	private Rect borderOnThumb(int colNum, int rowNum) {
		int width_view;
		int height_view;
		try {
			if (getMeasuredWidth() < this.rs_view_width) {
				width_view = getMeasuredWidth();
			} else {
				width_view = this.rs_view_width;
			}
			if (getMeasuredHeight() < this.rs_view_height) {
				height_view = getMeasuredHeight();
			} else {
				height_view = this.rs_view_height;
			}
			return new Rect(
					(int) (5.0D + this.scaleFactor * colNum),
					(int) (5.0D + this.scaleFactor * rowNum),
					(int) (5.0D + this.scaleFactor * colNum + width_view * this.scaleFactor),
					(int) (5.0D + this.scaleFactor * rowNum + height_view * this.scaleFactor));

		} catch (Exception e) {
			e.printStackTrace();
			return new Rect();
		}
	}

	/**
	 * 计算是第几列
	 * 
	 * @param mRsView
	 * @param xPos
	 * @return
	 */
	public static int calcColNum(RoomSelectorView mRsView, int xPos) {
		return mRsView.calcColNum(xPos);
	}

	/**
	 * 计算是第几列
	 * 
	 * @param xPos
	 * @return
	 */
	private int calcColNum(int xPos) {
		try {
			int col =
				(xPos + this.xOffsetInRow - this.marginLeft)
				/ this.rs_room_current_width;
			return col;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return -1;
	}

	/**
	 * 计算是第几排
	 * 
	 * @param mRsView
	 * @param yPos
	 * @return
	 */
	public static int calcRowNum(RoomSelectorView mRsView, int yPos) {
		return mRsView.calcRowNum(yPos);
	}

	/**
	 * 计算是第几排
	 * 
	 * @param yPos
	 * @return
	 */
	private int calcRowNum(int yPos) {
		try {
			int row =
				(yPos + this.yOffsetInRow - this.marginTop)
				/ this.rs_room_current_height;
			return row;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return -1;
	}

	/**
	 * 获取整栋房间状态信息
	 * 
	 * @param mRsView
	 * @return
	 */
	public static ArrayList<ArrayList<Integer>> getListRoomConditions(RoomSelectorView mRsView) {
		return mRsView.roomStates;
	}

	/**
	 * 获取点击监听器
	 * 
	 * @param mRsView
	 * @return
	 */
	public static OnRoomClickListener getRoomClickListener(RoomSelectorView mRsView) {
		return mRsView.mOnRoomClickListener;
	}

	/**
	 * 设定并返回是否显示缩略图
	 * 
	 * @param mRsView
	 * @param thumbable
	 * @return
	 */
	public static boolean setThumb(RoomSelectorView mRsView, boolean thumbable) {
		mRsView.showThumb = thumbable;
		return mRsView.showThumb;
	}

	/**
	 * 是否可以移动和点击
	 * @param mRsView
	 * @return
	 */
	public static boolean isMoveable(RoomSelectorView mRsView) {
		return mRsView.isMoveable;
	}

	/**
	 * 获取整个view的宽度
	 * @param mRsView
	 * @return
	 */
	public static int getRSViewWidth(RoomSelectorView mRsView) {
		return mRsView.rs_view_width;
	}

	/**
	 * 获取整个view的高度
	 * @param mRsView
	 * @return
	 */
	public static int getRSViewHeight(RoomSelectorView mRsView) {
		return mRsView.rs_view_height;
	}

	/**
	 * 获取排数x轴偏移量
	 * @param mRsView
	 * @return
	 */
	public static float getXOffsetRow(RoomSelectorView mRsView) {
		return mRsView.xOffsetRow;
	}

	/**
	 * 获取排数y轴偏移量
	 * @param mRsView
	 * @return
	 */
	public static float getYOffsetRow(RoomSelectorView mRsView) {
		return mRsView.yOffsetRow;
	}

	/**
	 * 修改并返回排数x轴的偏移量
	 * @param mRsView
	 * @param xScroll
	 */
	public static float setXOffsetRowWithScroll(RoomSelectorView mRsView,
			float xScroll) {
		mRsView.xOffsetRow = mRsView.xOffsetRow - xScroll;
		return mRsView.xOffsetRow;
	}

	/**
	 * 修改房间距离排数的横向距离
	 * @param mRsView
	 * @param xScroll
	 * @return
	 */
	public static float setXOffsetInRowWithScroll(RoomSelectorView mRsView,
			int xScroll) {
		mRsView.xOffsetInRow = mRsView.xOffsetInRow + xScroll;
		return mRsView.xOffsetInRow;
	}

	/**
	 * 获取房间距离排数的横向距离
	 * @param mRsView
	 * @return
	 */
	public static int getXOffsetInRow(RoomSelectorView mRsView) {
		return mRsView.xOffsetInRow;
	}

	/**
	 * 设置排数x轴偏移量
	 * @param mRsView
	 * @param xOffsetRow
	 * @return
	 */
	public static float setXOffSetRow(RoomSelectorView mRsView, float xOffsetRow) {
		mRsView.xOffsetRow = xOffsetRow;
		return mRsView.xOffsetRow;
	}

	/**
	 * 设置房间距离排数的横向距离
	 * @param mRsView
	 * @param xOffsetInRow
	 * @return
	 */
	public static float setXOffsetInRow(RoomSelectorView mRsView, int xOffsetInRow) {
		mRsView.xOffsetInRow = xOffsetInRow;
		return mRsView.xOffsetInRow;
	}

	/**
	 * 修改排数y轴的偏移量
	 * @param mRsView
	 * @param yScroll
	 * @return
	 */
	public static float setYOffsetRowWithScroll(RoomSelectorView mRsView,
			float yScroll) {
		mRsView.yOffsetRow = mRsView.yOffsetRow - yScroll;
		return mRsView.yOffsetRow;
	}

	/**
	 * 修改可见房间距离顶端的距离
	 * @param mRsView
	 * @param yScroll
	 * @return
	 */
	public static int setYOffsetInRowWithScroll(RoomSelectorView mRsView,
			int yScroll) {
		mRsView.yOffsetInRow = mRsView.yOffsetInRow + yScroll;
		return mRsView.yOffsetInRow;
	}

	/**
	 * 获取可视房间距离顶端的距离
	 * @param mRsView
	 * @return
	 */
	public static int getYOffsetInRow(RoomSelectorView mRsView) {
		return mRsView.yOffsetInRow;
	}

	/**
	 * 设置可视房间距离顶端的距离
	 * @param mRsView
	 * @param yOffsetInRow
	 * @return
	 */
	public static int setYOffsetInRow(RoomSelectorView mRsView, int yOffsetInRow) {
		mRsView.yOffsetInRow = yOffsetInRow;
		return mRsView.yOffsetInRow;
	}

	/**
	 * 设置排数y轴偏移量
	 * @param mRsView
	 * @param yOffsetRow
	 * @return
	 */
	public static float setYOffsetRow(RoomSelectorView mRsView, float yOffsetRow) {
		mRsView.yOffsetRow = yOffsetRow;
		return mRsView.yOffsetRow;
	}

	/**
	 * 设置按钮点击事件
	 * 
	 * @param listener
	 */
	public void setOnRoomClickListener(
			OnRoomClickListener listener) {
		this.mOnRoomClickListener = listener;
	}

	public static double getRatiohw() {
		return ratioHW;
	}
	
	/*@Override
	*//**
	 * 重写该方法，达到适应ScrollView的效果
	 *//*
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
	    MeasureSpec.AT_MOST);
	    super.onMeasure(widthMeasureSpec, expandSpec);
	}*/

}
