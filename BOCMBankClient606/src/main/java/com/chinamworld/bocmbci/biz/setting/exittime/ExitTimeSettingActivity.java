package com.chinamworld.bocmbci.biz.setting.exittime;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.setting.SettingBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.SharedPreUtils;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 退出时间设定
 * 
 * @author xyl
 * 
 */
public class ExitTimeSettingActivity extends SettingBaseActivity implements
		SeekBar.OnSeekBarChangeListener {
	private static final String TAG = "ExitTimeSettingActivity";

	// private Spinner timeSpinner;
	private Button edit;
	/** 原时间初始化 分钟 */
	private String oldTime;
	private String newTimeMin = "0";
	private String newTimeS = "0";
	/** 十位 */
	private TextView minTime1Tv;
	/** 个位 */
	private TextView minTime2Tv;
	private SeekBar mSeekBar;

	private TextView exiteTimeInfoTv;

	private static int MAXTIME = 15;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View childView = mainInflater.inflate(
				R.layout.setting_editexittimesetting, null);
		tabcontent.addView(childView);
		right.setVisibility(View.GONE);
		setTitle(getResources().getString(R.string.set_editexittime));
		initRightBtnForMain();
		edit = (Button) findViewById(R.id.set_edit);
		edit.setOnClickListener(this);
		minTime1Tv = (TextView) findViewById(R.id.tvone);
		minTime2Tv = (TextView) findViewById(R.id.tvtwo);
		mSeekBar = (SeekBar) findViewById(R.id.seekBar);
		mSeekBar.setOnSeekBarChangeListener(this);
		mSeekBar.setMax(MAXTIME);
		BaseActivity activity = (BaseActivity)BaseDroidApp.getInstanse().getCurrentAct();
		if(activity != null)
		oldTime = activity.getOutTimeMilins()
				/ 60 + "";

		exiteTimeInfoTv = (TextView) findViewById(R.id.setting_editexittime_info_tv);
		initView(oldTime);
		setLeftSelectedPosition("settingManager_5");
	}
//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("settingManager_5");
//	}



	private void initView(String oldTime) {
		int oldDate = Integer.valueOf(oldTime);
		if (oldDate < 1) {
			exiteTimeInfoTv.setText(R.string.set_selectexittime_no);
			minTime1Tv.setText("0");
			minTime2Tv.setText("0");
			mSeekBar.setProgress(0);
		} else {
			exiteTimeInfoTv.setText(R.string.set_now_exitTime);
			int a = oldDate / 10;// 十位数
			int b = oldDate % 10;// 个位数
			minTime1Tv.setText(String.valueOf(a));
			minTime2Tv.setText(String.valueOf(b));
			mSeekBar.setProgress(oldDate);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.set_edit:
			BaseActivity activity = (BaseActivity)BaseDroidApp.getInstanse().getCurrentAct();
			if(activity != null)
			oldTime = activity
					.getOutTimeMilins()
					/ 60 + "";
			// if (newTimeMin.equals(oldTime)) {// 未改变
			// String errorInfo = getResources().getString(
			// R.string.set_exittime_notchage_error);
			// BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
			// } else {
			if (Integer.valueOf(newTimeS) == 0) {
				SharedPreUtils.getInstance().addOrModify(
						ConstantGloble.SCREEN_TIME_OUT, String.valueOf(-1));
			} else {
				SharedPreUtils.getInstance().addOrModify(
						ConstantGloble.SCREEN_TIME_OUT, newTimeS);
			}
			BaseDroidApp.getInstanse().screenOutTime = Integer
					.valueOf(SharedPreUtils.getInstance().getString(
							ConstantGloble.SCREEN_TIME_OUT, "-1"));
			BaseDroidApp.getInstanse().reSetalarmPre();
			CustomDialog.toastShow(
					this,
					getResources().getString(
							R.string.set_selectexittime_success));
			exiteTimeInfoTv.setText(R.string.set_now_exitTime);
			initView(newTimeMin);
			// }
			break;
		case R.id.ib_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		int newTime = progress;
		newTimeMin = String.valueOf(newTime);
		newTimeS = String.valueOf(newTime * 60);
		minTime1Tv.setText(newTime / 10 + "");
		minTime2Tv.setText(newTime % 10 + "");
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

}
