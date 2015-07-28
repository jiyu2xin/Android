package com.xiaoqf.util;

import android.os.Handler;
import android.os.Message;

//��android�Դ���CountdownTimer�������ʵ�ֶ�ʱ����
public abstract class AdvancedCountdownTimer {
	private static final int MSG_RUN = 1;	

	private final long mCountdownInterval;// ��ʱ������Ժ����
	private long mTotalTime;// ��ʱʱ��
	private long mRemainTime;// ʣ��ʱ��

	// ���캯��
	public AdvancedCountdownTimer(long millisInFuture, long countDownInterval) {
		mTotalTime = millisInFuture;
		mCountdownInterval = countDownInterval;
		mRemainTime = millisInFuture;
	}

	// ȡ����ʱ
	public final void cancel() {
		mHandler.removeMessages(MSG_RUN);
	}

	// ���¿�ʼ��ʱ
	public final void resume() {
		mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_RUN));
	}

	// ��ͣ��ʱ
	public final void pause() {
		mHandler.removeMessages(MSG_RUN);
	}

	// ��ʼ��ʱ
	public synchronized final AdvancedCountdownTimer start() {
		if (mRemainTime <= 0) {// ��ʱ�����󷵻�
			onFinish();
			return this;
		}
		// ���ü�ʱ���
		mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_RUN),
				mCountdownInterval);
		return this;
	}

	public abstract void onTick(long millisUntilFinished, int percent);// ��ʱ��

	public abstract void onFinish();// ��ʱ����

	// ͨ��handler����android UI����ʾ��ʱʱ��
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			synchronized (AdvancedCountdownTimer.this) {
				if (msg.what == MSG_RUN) {
					mRemainTime = mRemainTime - mCountdownInterval;

					if (mRemainTime <= 0) {
						onFinish();
					} else if (mRemainTime < mCountdownInterval) {
						sendMessageDelayed(obtainMessage(MSG_RUN), mRemainTime);
					} else {

						onTick(mRemainTime, new Long(100
								* (mTotalTime - mRemainTime) / mTotalTime)
								.intValue());

						sendMessageDelayed(obtainMessage(MSG_RUN),
								mCountdownInterval);
					}
				} 
			}
		}
	};
}
