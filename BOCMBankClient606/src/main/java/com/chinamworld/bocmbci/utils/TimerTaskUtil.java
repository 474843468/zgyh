package com.chinamworld.bocmbci.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
/**
 * 获取按钮   倒计时
 * @author Administrator
 *
 */
public class TimerTaskUtil {

	private  static TimerTaskUtil  instance;
	private  MyCount smsCount;
	private Button getTv;
	private int a;
	private Context context;
	private  TimerTaskUtil (){
		
	}
	public static TimerTaskUtil getInstance(){
		if (instance ==null){
			return new TimerTaskUtil();
		}
		return instance;
	}
	public  class MyCount extends CountDownTimer {
		

		public MyCount(long millisInFuture, long countDownInterval) {
			// 1.倒计时的事件数 2. 间隔时间
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			if(getTv!=null){
				getTv.setText(String.valueOf(a));
				a--;
			}
		}

		@Override
		public void onFinish() {
			getTv.setText(context.getString(R.string.epay_pub_bt_get_note_code));
			getTv.setClickable(true);
			cancel();
		}

	}
	/** */
	public  void startGet(Context context,Button tv){
		this.context = context;
		this.getTv =tv;
		this.a = 60;
		getTv.setClickable(false);
		smsCount = new MyCount(60000, 1000);
		smsCount.start();
	}
}
