package com.xiaoqf.view;

/**
 * 
 * @ClassName: OnRoomClickListener
 * @Description: Room点击监听器
 * @author tongdu
 * @date 2015-5-19 下午4:02:49
 *
 */
public abstract interface OnRoomClickListener
{
  public abstract boolean onSelected(int inFloorNum, int floorNum);

  public abstract boolean onDeSelected(int inFloorNum, int floorNum);
  
  public abstract void onSoldClick();
}