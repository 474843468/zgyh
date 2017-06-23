package com.chinamworld.bocmbci.biz.blpt.sign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Blpt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.blpt.BillPaymentBaseActivity;
import com.chinamworld.bocmbci.biz.blpt.BlptUtil;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 账单缴费信息添加
 * 
 * @author panwe
 * 
 */
public class BillPaymentSignMsgAddActivity extends BillPaymentBaseActivity
		implements OnClickListener {
	/** 主布局 */
	private View viewContent;
	/** 上一步按钮 */
	private Button btnLast;
	/** 下一步按钮 */
	private Button btnNext;
	/** 动态主布局 **/
	private LinearLayout mainLayout;
	/** 标记当前显示第几个页面 */
	private int curPag = 0;
	/** 调回主界面标识 */
	private int tag;
	/** 存储所有页面数据 */
	private Map<String, Object> pagDataMap = null;
	/** 存储返回所有步骤 */
	private Map<String, List<String>> pagStep = null;
	/** 缴费账号信息 **/
	private String accNum;
	private String accId;
	/** 缴费手机号 **/
	private String mobile;
	/** 提示语 **/
	private TextView tvWarn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.blpt_bill_paypent_title));
		viewContent = (View) LayoutInflater.from(this).inflate(
				R.layout.blpt_bill_newservice_msgadd, null);
		BlptUtil.getInstance().addActivity(this);
		addView(viewContent);
		// 右上角按钮赋值
		setText(this.getString(R.string.go_main));
		setRightBtnClick(rightBtnBackmainClick);
		init();
		pagDataMap = new HashMap<String, Object>();
		pagStep = new HashMap<String, List<String>>();
		// 请求conversation
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestCommConversationId();
	}

	private void init() {
		Bundle b = getIntent().getBundleExtra(Blpt.KEY_BUNDLE);
		tag = b.getInt(Blpt.KEY_TAG);
		accNum = b.getString(Comm.ACCOUNTNUMBER);
		accId = b.getString(Comm.ACCOUNT_ID);
		mobile = b.getString("MOBLIE");
		BlptUtil.getInstance().setUserCode(mobile);
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getString(R.string.blpt_signbill_step1),
						this.getString(R.string.blpt_signbill_step2),
						this.getString(R.string.blpt_signbill_step3) });
		StepTitleUtils.getInstance().setTitleStep(1);
		mainLayout = (LinearLayout) viewContent
				.findViewById(R.id.blpt_main_layout);
		tvWarn = (TextView) findViewById(R.id.tv_bill_tip);
		btnNext = (Button) viewContent.findViewById(R.id.btnnext);
		btnLast = (Button) viewContent.findViewById(R.id.btnLast);
		Button btnBack = (Button) findViewById(R.id.ib_back);
		btnLast.setVisibility(View.GONE);
		btnBack.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnLast.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 下一步
		case R.id.btnnext:
			int mstepNo = BlptUtil.getInstance().stringToint(
					(pagStep.get("stekey" + curPag)).get(0));
			int mstepNum = BlptUtil.getInstance().stringToint(
					(pagStep.get("stekey" + curPag)).get(1));
			int curtag;
			if ((pagStep.get("stekey" + curPag)).get(2).equals("accisnull")) {
				curtag = 0;
			} else {
				curtag = 1;
			}
			// 读取用户输入内容
			Map<String, String> mapfromLayout = getTextFromLayout(
					(LinearLayout) mainLayout.getChildAt(curPag - 1), curtag);
			// 选中卡列表赋值
			List<String> bankAcctInfo = getBankAcctInfo(pagDataMap, curPag);
			if (!StringUtil.isNullOrEmpty(bankAcctInfo)) {
				accNum = bankAcctInfo.get(0);
				accId = bankAcctInfo.get(1);
			}
			if (!checkOk) return;
			if ((mstepNum - 3) >= mstepNo) {
				checkOk = true;
				requestSignPayInfo((pagStep.get("stekey" + curPag)).get(0),
						(pagStep.get("stekey" + curPag)).get(1), mapfromLayout,
						true);
			} else if ((mstepNum - 2) == mstepNo) {
				checkOk = true;
				BlptUtil.getInstance().setLayoutMap(mapfromLayout);
				BaseHttpEngine.showProgressDialog();
				requestGetSecurityFactor(Blpt.SIGNPAY_SERVICEID);
			}
			break;

		// 返回
		case R.id.ib_back:
			tvWarn.setVisibility(View.GONE);
			mainLayout.removeViewAt(curPag - 1);
			for (int i = 0; i < mainLayout.getChildCount(); i++) {
				if (i == curPag - 2) {
					LinearLayout lastLayout = (LinearLayout) mainLayout
							.getChildAt(i);
					lastLayout.setVisibility(View.VISIBLE);
				} else {
					LinearLayout otherLayout = (LinearLayout) mainLayout
							.getChildAt(i);
					otherLayout.setVisibility(View.GONE);
				}
			}
			if (curPag == 1) {
				finish();
			}
			curPag--;
			break;

		}
	}

	/** conversationid回调 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestRandomNumber();
	}

	/** 随机数返回处理 */
	@Override
	public void randomNumberCallBack(Object resultObj) {
		super.randomNumberCallBack(resultObj);
		requestSignPayInfo("-1", "0", null, false);
	}

	/*** 安全因子返回结果 ***/
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@SuppressWarnings("unchecked")
					@Override
					public void onClick(View v) {
						BlptUtil.getInstance().setPayLastPagMap(
								(Map<String, Object>) pagDataMap.get("key"+ curPag));
						Intent it = new Intent(
								BillPaymentSignMsgAddActivity.this,
								BillPaymentSignConfirmActivity.class);
						Bundle b = new Bundle();
						b.putString(Blpt.KEY_STEPNO,
								(pagStep.get("stekey" + curPag)).get(0));
						b.putString(Blpt.KEY_STEPNUMER,
								(pagStep.get("stekey" + curPag)).get(1));
						b.putString(Comm.ACCOUNTNUMBER, accNum);
						b.putString(Comm.ACCOUNT_ID, accId);
						b.putInt(Blpt.KEY_TAG, tag);
						b.putString(Blpt.KEY_COMBINID, BaseDroidApp
								.getInstanse().getSecurityChoosed());
						it.putExtra(Blpt.KEY_BUNDLE, b);
						startActivity(it);
					}
				});
	}

	/**
	 * 签约缴费动态步骤
	 * 
	 * @param stepno
	 * @param stepnum
	 */
	@SuppressWarnings("unchecked")
	private void requestSignPayInfo(String stepno, String stepnum,
			Map<String, String> layoutData, boolean isShow) {
		// 开启通讯框
		if (isShow) {
			BaseHttpEngine.showProgressDialog();
		}
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Blpt.METHOD_SIGN_PAYMENT);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> dataMap = BlptUtil.getInstance().getMapData();
		map.put(Blpt.SIGN_PAY_MERCHANTID, dataMap.get(Blpt.KEY_MERCHID));
		map.put(Blpt.SIGN_PAY_JNUM, dataMap.get(Blpt.KEY_PAYJNUM));
		map.put(Blpt.SIGN_PAY_PRVNAME, dataMap.get(Blpt.KEY_PROVICESHORTNAME));
		map.put(Blpt.PAY_RESULT_CITY, dataMap.get(Blpt.KEY_CITY));
		map.put(Blpt.BILL_DISNAME, dataMap.get(Blpt.KEY_PAYEENAME));
		map.put(Blpt.SIGN_PAY_STEPNO, stepno);
		map.put(Blpt.SIGN_PAY_STEPNUM, stepnum);
		if (!StringUtil.isNullOrEmpty(accNum)
				&& !StringUtil.isNullOrEmpty(accId)) {
			map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_DISP, accNum);
			map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_REAL, accNum);
			map.put(Blpt.NEWAPPLY_CONFIRM_ACCID, accId);
		}
		map.put(Blpt.SIGN_PAY_USERCODE, mobile);
		if (!stepno.equals("-1")) {
			Map<String, Object> curData = (Map<String, Object>) pagDataMap
					.get("key" + curPag);
			List<Map<String, Object>> exeList = (List<Map<String, Object>>) curData
					.get(Blpt.SIGN_PAY_EXELIST_RE);
			List<Map<String, Object>> spcList = (List<Map<String, Object>>) curData
					.get(Blpt.SIGN_PAY_SPELIST_RE);
			// 封装上传数据
			putData(exeList, spcList, layoutData, map);
		}
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "signPayInfoCallBack");
	}

	/** 动态步骤返回结果处理 */
	@SuppressWarnings("unchecked")
	public void signPayInfoCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(result))return;
		if (result.containsKey(Blpt.ERRORMSG) && !StringUtil.isNull((String)result.get(Blpt.ERRORMSG))) {
			BaseDroidApp.getInstanse().showMessageDialog((String)result.get(Blpt.ERRORMSG), errorClick); return;
		}
		String stepNo = "";
		String stepNum = "";
		if (!StringUtil.isNullOrEmpty((String) result
				.get(Blpt.SIGN_PAY_STEPNO_RE))) {
			stepNo = (String) result.get(Blpt.SIGN_PAY_STEPNO_RE);
		}
		if (!StringUtil.isNullOrEmpty((String) result
				.get(Blpt.SIGN_PAY_STEPNUM_RE))) {
			stepNum = (String) result.get(Blpt.SIGN_PAY_STEPNUM_RE);
		}

		List<Map<String, Object>> accList = (List<Map<String, Object>>) result
				.get(Blpt.SIGN_PAY_ACC_RE);
		List<Map<String, Object>> speList = (List<Map<String, Object>>) result
				.get(Blpt.SIGN_PAY_SPELIST_RE);
		List<Map<String, Object>> exeList = (List<Map<String, Object>>) result
				.get(Blpt.SIGN_PAY_EXELIST_RE);
		++curPag;
		// 记录步骤
		List<String> stelist = new ArrayList<String>();
		stelist.add(stepNo);
		stelist.add(stepNum);
		if (StringUtil.isNullOrEmpty(accList)) {
			stelist.add("accisnull");
		} else {
			stelist.add("");
		}
		pagStep.put("stekey" + curPag, stelist);
		// 存储当前返回数据
		pagDataMap.put("key" + curPag, result);

		for (int i = 0; i < mainLayout.getChildCount(); i++) {
			LinearLayout otherLayout = (LinearLayout) mainLayout.getChildAt(i);
			otherLayout.setVisibility(View.GONE);
		}

		BlptUtil.saveUserCode(exeList);
		BlptUtil.saveUserCode(speList);
		LinearLayout view = addViews(accList, exeList, speList, false);
		mainLayout.addView(view);
//		 if (!StringUtil.isNullOrEmpty((String) result
//		 .get(Blpt.NEWAPPLY_PRE_WARN))) {
//		 tvWarn.setVisibility(View.VISIBLE);
//		 tvWarn.setText((String) result.get(Blpt.NEWAPPLY_PRE_WARN));
//		 }
	}

	/** 通讯失败 **/
	public OnClickListener errorClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissErrorDialog();
			if (curPag == 0) {
				finish();
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (pagDataMap != null) {
			pagDataMap.clear();
		}
	}
}
