package com.xiaoqf.view;

import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiaoqf.beans.Floor;
import com.xiaoqf.beans.Room;
import com.xiaoqf.customview.RoomSelectorThumbView;
import com.xiaoqf.customview.RoomSelectorView;
import com.xiaoqf.util.EventHandler;
import com.xiaoqf.app.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * @ClassName: TestActivity
 * @Description: 测试用，发布时删除
 *
 */
public class TestActivity extends Activity {
	
	/** 缩略图视图 */
	/*@ViewInject(R.id.mRSThumbView)
	private RoomSelectorThumbView mRSThumbView;*/
	
	@ViewInject(R.id.mRSView)
	private RoomSelectorView mRSView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_select);
		
		ViewUtils.inject(this);
		
	}

}
