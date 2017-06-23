package com.chinamworld.bocmbci.biz.tran.twodimentrans;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author
 * 
 */
public class TwoDimenTransActivity1 extends TranBaseActivity {

	private LinearLayout mGenerate2DimenLl, mScan2DimenLl;

	private int codeFlag;
	private static final int SCAN_CODE = 1;
	private static final int GEN_CODE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.two_dimen_trans));
		View view = mInflater
				.inflate(R.layout.tran_2dimen_trans_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		setLeftSelectedPosition("tranManager_3");
		mTopRightBtn.setVisibility(View.INVISIBLE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(TwoDimenTransActivity1.this, MainActivity.class);
//				startActivity(intent);
				goToMainActivity();
				finish();
			}
		});
		setupView();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			ActivityTaskManager.getInstance().removeAllActivity();
//			Intent intent = new Intent();
//			intent.setClass(TwoDimenTransActivity1.this, MainActivity.class);
//			startActivity(intent);
			goToMainActivity();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("tranManager_3");
//	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {

		mGenerate2DimenLl = (LinearLayout) findViewById(R.id.ll_2dimen_generate_trans);
		mScan2DimenLl = (LinearLayout) findViewById(R.id.ll_2dimen_scan_trans);

		mGenerate2DimenLl.setOnClickListener(generate2DimenListener);
		mScan2DimenLl.setOnClickListener(scan2DimenListener);

	}

	/**
	 * 生成二维码
	 */
	private OnClickListener generate2DimenListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			codeFlag = GEN_CODE;
			// 请求账户列表
			requestTranoutAccountList();

		}
	};
	/**
	 * 扫描二维码
	 */
	private OnClickListener scan2DimenListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			codeFlag = SCAN_CODE;
			// 发通讯查询关联账户列表，如果是关联账户列表里面的 弹出提示框 不能转账
			requestTranoutAccountList();
		}
	};

	/**
	 * 调用接口：PsnCommonQueryAllChinaBankAccount 请求给定类型转出账户列表
	 * */
	public void requestTranoutAccountList() {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.COMMONQUERYALLCHINABANKACCOUNT);

		String[] accountTypeArr = new String[] { ConstantGloble.ACC_TYPE_ORD,
				ConstantGloble.ACC_TYPE_BRO, ConstantGloble.ACC_TYPE_RAN,
				ConstantGloble.ACC_TYPE_GRE, ConstantGloble.ZHONGYIN,
				ConstantGloble.SINGLEWAIBI };
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.ACCOUNTTYPE_REQ, accountTypeArr);
		biiRequestBody.setParams(map);

		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestTranoutAccountListCallBack");
	}

	/**
	 * PsnCommonQueryAllChinaBankAccount接口的回调方法，返回结果 *
	 */
	@SuppressWarnings("unchecked")
	public void requestTranoutAccountListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		// ?是否需要添加提示信息，根据biiResponseBody的内容
		// TODO 返回列表里面没有数据的时候 需要提示 modify by wjp

		TranDataCenter.getInstance().setAccountOutList(result);
		if (codeFlag == GEN_CODE) {// 生成二维码
			if (StringUtil.isNullOrEmpty(result)) {
				String message = this.getString(R.string.query_no_account);
				BaseDroidApp.getInstanse().createDialog(null, message);
				return;
			}
			Intent intent0 = new Intent(this, SelectAccountActivity.class);
			startActivity(intent0);
		} else if (codeFlag == SCAN_CODE) {// 扫描二维码
			scan2DimentionCode();
		}
	}

}
