package com.chinamworld.bocmbci.biz.finc.fundacc;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金账户开户/登记
 * 
 * @author xyl
 * 
 */
public class FincFundAccMainActivity extends FincBaseActivity {
	private static final String TAG = "FincFundAccMainActivity";
	/** 基金账户开户 */
	private Button registAccBtn;
	/** 登记账户按钮 */
	private Button checkInAccBtn;

	/** 开户布局 */
	private RelativeLayout registLayout;
	/**
	 * 登记账户布局
	 */
	private RelativeLayout checkInLayout;

	private int flag = REGITACC;
	/**
	 * 注册
	 */
	private static final int REGITACC = 0;
	/** 登记 */
	private static final int CHECKINACC = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		settingbaseinit();
		init();
	}

	@Override
	public void queryAccListCallback(Object resultObj) {
		super.queryAccListCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		fincControl.fundAccList = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		Intent intent;
		if (StringUtil.isNullOrEmpty(fincControl.fundAccList)) {
			startActivity(new Intent(this, FincNoAccActivity.class));
			return;
		} else {
			switch (flag) {
			case REGITACC:
				intent = new Intent();
				intent.setClass(this, FincFundRegistAccMainActivity.class);
				startActivityForResult(intent,
						ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE);
				break;
			case CHECKINACC:
				intent = new Intent();
				intent.setClass(this, FincFundCheckInAccMainActivity.class);
				startActivityForResult(intent,
						ConstantGloble.ACTIVITY_REQUEST_CHECKINFUNCACC_CODE);
				break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		BaseDroidApp.getInstanse().setCurrentAct(this);
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE:// 开户
			switch (resultCode) {
			case RESULT_OK:
				// height = checkInLayout.getHeight();
				// LayoutParams params = new
				// LayoutParams(LayoutParams.MATCH_PARENT, height);
				// checkInLayout.setLayoutParams(params);
				// registLayout.setVisibility(View.GONE);
				setResult(RESULT_OK);
				fincControl.ifhaveaccId = true;
				finish();
				break;
			case RESULT_CANCELED:

				break;
			default:
				break;
			}
			break;
		case ConstantGloble.ACTIVITY_REQUEST_CHECKINFUNCACC_CODE:// 登记
			switch (resultCode) {
			case RESULT_OK:
				setResult(RESULT_OK);
				fincControl.ifhaveaccId = true;
				finish();
				break;
			case RESULT_CANCELED:

				break;
				

			default:
				break;
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 初始化布局
	 */
	private void init() {
		View childView = mainInflater.inflate(
				R.layout.finc_acc_fundacc_setting, null);
		tabcontent.addView(childView);
		// height =(int) (childView.getMeasuredHeight() -
		// getResources().getDimension(R.dimen.common_row_margin)/2);
		setTitle(R.string.finc_acc_registAcc);
		registAccBtn = (Button) childView
				.findViewById(R.id.finc_acc_regist_btn);
		checkInAccBtn = (Button) childView
				.findViewById(R.id.finc_acc_checkin_btn);
		registLayout = (RelativeLayout) childView
				.findViewById(R.id.registAcc_Layout);
		checkInLayout = (RelativeLayout) childView
				.findViewById(R.id.checkIn_Layout);
		registAccBtn.setOnClickListener(this);
		checkInAccBtn.setOnClickListener(this);
		// TODO 判断哪个显示
		checkInLayout.setVisibility(View.VISIBLE);
		registLayout.setVisibility(View.VISIBLE);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_acc_regist_btn:// 开户
			flag = REGITACC;
			BaseHttpEngine.showProgressDialog();
			queryAccList();
			break;
		case R.id.finc_acc_checkin_btn:// 登记
			flag = CHECKINACC;
			BaseHttpEngine.showProgressDialog();
			queryAccList();
			break;
		default:
			break;
		}
	}

}
