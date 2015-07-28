package com.xiaoqf.customview;

import java.util.ArrayList;

import com.xiaoqf.beans.Room;
import com.xiaoqf.util.LogUtil;

import android.view.GestureDetector;
import android.view.MotionEvent;


class GestureListener extends GestureDetector.SimpleOnGestureListener {
	private static final String TAG = "GestureListener";
	private RoomSelectorView mRsView;
	private int lastCol = -1;
	private int lastRow = -1;

	GestureListener(RoomSelectorView mRsView) {
		this.mRsView = mRsView;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		return super.onDoubleTap(e);
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return super.onDoubleTapEvent(e);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1,
			MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2,
			float distanceX, float distanceY) {
		// 是否可以移动和点击
		if(!RoomSelectorView.isMoveable(mRsView)){
			return false;
		}
		// 不显示缩略图
		RoomSelectorView.setThumb(mRsView,false);
		
		// 某轴向是否可滑动
		boolean scrollableOnX = true;
		boolean scrollableOnY = true;
		if ((RoomSelectorView.getRSViewWidth(mRsView) < mRsView.getMeasuredWidth())
				&& (0.0F == RoomSelectorView.getXOffsetRow(mRsView))){
			scrollableOnX = false;
		}
		
		if ((RoomSelectorView.getRSViewHeight(mRsView) < mRsView.getMeasuredHeight())
				&& (0.0F == RoomSelectorView.getYOffsetRow(mRsView))){
			scrollableOnY  = false;
		}
		
		if(scrollableOnX){
			int xScroll = Math.round(distanceX);
			//修改排数x轴的偏移量
			RoomSelectorView.setXOffsetRowWithScroll(mRsView, (float)xScroll);
			LogUtil.log(TAG, RoomSelectorView.getXOffsetRow(mRsView)+"");
			//修改房间距离排数的横向距离
			RoomSelectorView.setXOffsetInRowWithScroll(mRsView, xScroll);
			LogUtil.log(TAG, RoomSelectorView.getXOffsetInRow(mRsView)+"");
			
			// 当前视图左越界
			if (RoomSelectorView.getXOffsetInRow(mRsView) < 0) {
				//滑到最左
				RoomSelectorView.setXOffsetInRow(mRsView, 0);
				RoomSelectorView.setXOffSetRow(mRsView, 0.0F);
			}

			// 当前视图右越界
			if(RoomSelectorView.getXOffsetInRow(mRsView) + mRsView.getMeasuredWidth()
				> RoomSelectorView.getRSViewWidth(mRsView)){
				//滑到最右
				RoomSelectorView.setXOffsetInRow(mRsView,
					RoomSelectorView.getRSViewWidth(mRsView) - mRsView.getMeasuredWidth());
				RoomSelectorView.setXOffSetRow(mRsView,
					(float)(mRsView.getMeasuredWidth() - RoomSelectorView.getRSViewWidth(mRsView)));
			}
		}
		
		if(scrollableOnY){
			//上负下正- 往下滑则减
			int yScroll = Math.round(distanceY);
			//修改排数y轴的偏移量
			RoomSelectorView.setYOffsetRowWithScroll(mRsView, (float)yScroll);
			//修改可视房间距离顶端的距离
			RoomSelectorView.setYOffsetInRowWithScroll(mRsView, yScroll);
			LogUtil.log(TAG, RoomSelectorView.getYOffsetInRow(mRsView)+"");
			
			// 当前视图上越界
			if (RoomSelectorView.getYOffsetInRow(mRsView) < 0){
				//滑到顶
				RoomSelectorView.setYOffsetInRow(mRsView, 0);
				RoomSelectorView.setYOffsetRow(mRsView, 0.0F);
			}

			// 当前视图下越界
			if (RoomSelectorView.getYOffsetInRow(mRsView) + mRsView.getMeasuredHeight()
					> RoomSelectorView.getRSViewHeight(mRsView)){
				//滑到底
				RoomSelectorView.setYOffsetInRow(mRsView,
					RoomSelectorView.getRSViewHeight(mRsView) - mRsView.getMeasuredHeight());
				RoomSelectorView.setYOffsetRow(mRsView,
					(float)(mRsView.getMeasuredHeight() - RoomSelectorView.getRSViewHeight(mRsView)));
			}
		}
		
		mRsView.invalidate();
		
		LogUtil.log("GestureDetector", "onScroll----------------------");
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		LogUtil.log("GestureDetector", "onSingleTapUp");
		if(!RoomSelectorView.isMoveable(mRsView)){
			return false;
		}
		//列数
		int colNum = RoomSelectorView.calcColNum(mRsView, (int)e.getX());
		//排数
		int rowNum = RoomSelectorView.calcRowNum(mRsView, (int)e.getY());
	
		if((rowNum>=0 && rowNum< RoomSelectorView.getListRoomConditions(mRsView).size())) {
			if(colNum>=0 && colNum<((ArrayList<Integer>)(RoomSelectorView.getListRoomConditions(mRsView).get(rowNum))).size()){
				LogUtil.log("TAG", "排数："+ rowNum + "列数："+colNum);
				ArrayList<Integer> states = RoomSelectorView.getListRoomConditions(mRsView).get(rowNum);
				ArrayList<Integer> lastStates;
				// 已售房间提示
				if (states.get(colNum).intValue()==Room.Sold) {
					RoomSelectorView.getRoomClickListener(mRsView).onSoldClick();
				}
				
				// 上次房间处理(当前可选)
				if (((lastCol>=0 && lastCol!=colNum) || (lastRow>=0 && lastRow!=rowNum))
					&& states.get(colNum).intValue()==Room.Normal) {
					lastStates = RoomSelectorView.getListRoomConditions(mRsView).get(lastRow);
					switch (lastStates.get(lastCol).intValue()) {
					case Room.Checked:
						lastStates.set(lastCol, Integer.valueOf(Room.Normal));
						break;

					default:
						break;
					}
				}
				
				// 当前房间处理
				if (lastCol != colNum || lastRow != rowNum) {
					switch (states.get(colNum).intValue()) {
					case Room.Checked:	// 已选中
						states.set(colNum, Integer.valueOf(Room.Normal));
						if(RoomSelectorView.getRoomClickListener(mRsView)!=null){
							RoomSelectorView.getRoomClickListener(mRsView).onDeSelected(colNum, rowNum);
						}
						break;
					case Room.Normal:	// 可选
						states.set(colNum, Integer.valueOf(Room.Checked));
						if(RoomSelectorView.getRoomClickListener(mRsView)!=null){
							RoomSelectorView.getRoomClickListener(mRsView).onSelected(colNum, rowNum);
						}
						break;
					default:
						break;
					}
				}
				
				// 记录房间（排除已售、不存在的）
				if (states.get(colNum).intValue()==Room.Normal ||
					states.get(colNum).intValue()==Room.Checked ) {
					lastCol = colNum;
					lastRow = rowNum;
				}
			}
		}
		
		// 不显示缩略图
		RoomSelectorView.setThumb(mRsView,false);
		mRsView.invalidate();
		return false;
	}
}