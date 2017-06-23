package com.boc.bocsoft.remoteopenacc.buss.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.boc.bocsoft.remoteopenacc.R;
import com.boc.bocsoft.remoteopenacc.buss.activity.RemoteApplication;
import com.boc.bocsoft.remoteopenacc.common.util.BocroaUtils;

/**
 * 获取验证码，并且拥有倒计时的Button
 * 
 * @author chf 2015-9-2
 * 
 *         注意把该类的onCreate()onDestroy()和activity的onCreate()onDestroy()同步处理
 * 
 */
public class RemobeTimeButtonView extends Button implements OnClickListener {

	private final String TIME = "time";
	private final String CTIME = "ctime";

	private final int START = 100;// 开始倒计时
	private final int FINISH = 200;

	private long lenght = 60 * 1000;// 倒计时长度,这里给了默认60秒
	private int code;// 是否请求成功
	private OnClickListener mOnclickListener;// 设置监听
	private String textafter = "秒后重新获取";
	private String textbefore = "获取验证码";
	private Timer timer;
	private TimerTask tt;
	private long time;
	private Map<String, Long> map = new HashMap<String, Long>();

	public RemobeTimeButtonView(Context context) {
		super(context);
		setOnClickListener(this);
	}

	public RemobeTimeButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundResource(R.drawable.bocroa_msg_code);
		setText("获取验证码");
		setTextSize(12);
		setTextColor(Color.rgb(255, 255, 255));
		setPadding(0, 0, 0, 2);
		setGravity(Gravity.CENTER);
		setOnClickListener(this);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			RemobeTimeButtonView.this.setText(time / 1000 + textafter);
			time -= 1000;
			if (time < 0) {
				RemobeTimeButtonView.this.setEnabled(true);
				RemobeTimeButtonView.this.setText(textbefore);
				clearTimer();
				if (mEndTimeListener != null) {
					setBackgroundResource(R.drawable.bocroa_msg_code);
					mEndTimeListener.onEndTimeListener();
				}
			}
		};
	};

	/**
	 * 初始化时间
	 */
	private void initTimer() {
		time = lenght;
		timer = new Timer();
		tt = new TimerTask() {

			@Override
			public void run() {
				// Log.d("chf", time / 1000 + "");
				handler.sendEmptyMessage(0x01);
			}
		};
	}

	/**
	 * 时间归零
	 */
	public void clearTimer() {
		if (tt != null) {
			tt.cancel();
			tt = null;
		}
		if (timer != null)
			timer.cancel();
		timer = null;
	}

	/**
	 * 重置按钮
	 */
	public void resetTimer() {
		if (tt != null) {
			tt.cancel();
			tt = null;
		}
		if (timer != null)
			timer.cancel();
		timer = null;
		// 设置重新开始
		RemobeTimeButtonView.this.setEnabled(true);
		RemobeTimeButtonView.this.setText(textbefore);
		setBackgroundResource(R.drawable.bocroa_msg_code);
	}

	@Override
	public void onClick(View v) {
		if (mOnclickListener != null)
			mOnclickListener.onClick(v);
		System.out.println("chf-------onclick-----");
	}

	/**
	 * 和activity的onDestroy()方法同步
	 */
	public void onDestroy() {
		if (RemoteApplication.map == null)
			RemoteApplication.map = new HashMap<String, Long>();
		RemoteApplication.map.put(TIME, time);
		RemoteApplication.map.put(CTIME, System.currentTimeMillis());
		clearTimer();
		Log.d("chf", "onDestroy");
	}

	/**
	 * 和activity的onCreate()方法同步
	 */
	public void onCreate(Bundle bundle) {

		Log.d("chf", RemoteApplication.map + "");
		if (RemoteApplication.map == null)
			return;
		if (RemoteApplication.map.size() <= 0)// 这里表示没有上次未完成的计时
			return;
		long time = System.currentTimeMillis()
				- RemoteApplication.map.get(CTIME)
				- RemoteApplication.map.get(TIME);
		RemoteApplication.map.clear();
		if (time > 0)
			return;
		else {
			initTimer();
			this.time = Math.abs(time);
			timer.schedule(tt, 0, 1000);
			this.setText(time + textafter);
			this.setEnabled(false);
		}
	}

	/**
	 * 设置监听
	 */
	@Override
	public void setOnClickListener(OnClickListener click) {
		if (click instanceof RemobeTimeButtonView) {
			super.setOnClickListener(click);
		} else
			this.mOnclickListener = click;
	}

	/**
	 * 倒数计时结束后的监听
	 */
	private EndTimeListener mEndTimeListener;

	public void setEndTimeListener(EndTimeListener listener) {
		mEndTimeListener = listener;
	}

	public interface EndTimeListener {

		public void onEndTimeListener();

	}

	/**
	 * 设置计时器开始计时
	 */
	public void startTimer(int code) {
		System.out.println("chf------------code-->" + code);
		this.code = code;
		if (code == START) {
			this.setBackgroundResource(R.drawable.bocroa_msg_code2);
			clearTimer();
			initTimer();
			this.setText(time / 1000 + textafter);
			this.setEnabled(false);
			timer.schedule(tt, 0, 1000);
		} else if (code == FINISH) {
			this.setEnabled(true);
		}
	}

	/** 设置计时时候显示的文本 */
	public void setTextAfter(String text1) {
		this.textafter = text1;
		this.setText(textafter);
	}

	/** 设置点击之前的文本 */
	public void setTextBefore(String text0) {
		this.textbefore = text0;
		this.setText(textbefore);
	}

	/**
	 * 设置到计时长度
	 * 
	 * @param lenght
	 *            时间 默认毫秒
	 * @return
	 */
	public void setLenght(long lenght) {
		this.lenght = lenght;
	}
}