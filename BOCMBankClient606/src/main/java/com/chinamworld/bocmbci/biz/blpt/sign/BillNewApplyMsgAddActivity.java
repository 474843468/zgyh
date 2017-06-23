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

/***
 * 申请新服务信息填写
 * 
 * @author panwe
 * 
 */
public class BillNewApplyMsgAddActivity extends BillPaymentBaseActivity
		implements OnClickListener {
	/** 主界面 */
	private View viewContent;
	/** 上一步按钮 */
	private Button btnLast;
	/** 下一步按钮 */
	private Button btnNext;
	/** 动态主界面 */
	private LinearLayout mainLayout;
	/** 标记当前页 */
	private int curPag = 0;
	/** 存储所有页面数据 */
	private Map<String, Object> pagDataMap = null;
	/** 存储返回所有步骤 */
	private Map<String, List<String>> pagStep = null;
	/** 账户号 **/
	private String accNumber;
	/** 帐号id **/
	private String accId;
	/** 提示语 **/
	private TextView tvWarn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.blpt_newapply_title));
		viewContent = (View) LayoutInflater.from(this).inflate(
				R.layout.blpt_bill_newservice_msgadd, null);
		BlptUtil.getInstance().addActivity(this);
		addView(viewContent);
		setText(this.getString(R.string.go_main));
		setRightBtnClick(rightBtnBackmainClick);
		init();
		pagDataMap = new HashMap<String, Object>();
		pagStep = new HashMap<String, List<String>>();
		// 请求conversation
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestCommConversationId();
	}

	/** 初始化 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getString(R.string.blpt_applynewbill_step2),
						this.getString(R.string.blpt_applynewbill_step3),
						this.getString(R.string.blpt_applynewbill_step4) });
		StepTitleUtils.getInstance().setTitleStep(1);

		tvWarn = (TextView) findViewById(R.id.tv_bill_tip);
		mainLayout = (LinearLayout) findViewById(R.id.blpt_main_layout);
		btnLast = (Button) findViewById(R.id.btnLast);
		btnNext = (Button) findViewById(R.id.btnnext);
		Button btnBack = (Button) findViewById(R.id.ib_back);
		btnLast.setVisibility(View.GONE);
		btnLast.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
		// 下一步
		case R.id.btnnext:
			int curtag;
			int mstepNo = BlptUtil.getInstance().stringToint(
					(pagStep.get("stekey" + curPag)).get(0));
			int mstepNum = BlptUtil.getInstance().stringToint(
					(pagStep.get("stekey" + curPag)).get(1));
			if ((pagStep.get("stekey" + curPag)).get(2).equals("accisnull")) {
				curtag = 0;
			} else {
				curtag = 1;
			}
			// 读取用户输入内容
			Map<String, String> mapfromLayout = getTextFromLayout(
					(LinearLayout) mainLayout.getChildAt(curPag - 1), curtag);
			//选中卡列表赋值
			List<String> bankAcctInfo = getBankAcctInfo(pagDataMap, curPag);
			if (!StringUtil.isNullOrEmpty(bankAcctInfo)) {
				accNumber = bankAcctInfo.get(0);
				accId = bankAcctInfo.get(1);
			}
			if (!checkOk) return;
			if ((mstepNum - 3) >= mstepNo) {
				checkOk = true;
				requestPsnPaysSignApplyPre(
						(pagStep.get("stekey" + curPag)).get(0),
						(pagStep.get("stekey" + curPag)).get(1), mapfromLayout,
						true);
			} else if ((mstepNum - 2) == mstepNo) {
				checkOk = true;
				BlptUtil.getInstance().setLayoutMap(mapfromLayout);
				// 发送安全因子请求
				BaseHttpEngine.showProgressDialog();
				requestGetSecurityFactor(Blpt.NEWAPPLY_SERVICEID);
			}
			break;
		}
	}

	// conversationid回调
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestRandomNumber();
	}

	/** 随机数返回处理 */
	@Override
	public void randomNumberCallBack(Object resultObj) {
		super.randomNumberCallBack(resultObj);
		requestPsnPaysSignApplyPre("-1", "0", null, false);
	}

	/*** 安全因子返回结果 ***/
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						@SuppressWarnings("unchecked")
						Map<String, Object> curData = (Map<String, Object>) pagDataMap
						.get("key" + curPag);
						BlptUtil.getInstance().setApplyLastPagMap(curData);
						Intent it = new Intent(BillNewApplyMsgAddActivity.this,
								BillNewApplyMsgConfirmActivity.class);
						Bundle b = new Bundle();
						b.putString(Comm.ACCOUNTNUMBER, accNumber);
						b.putString(Comm.ACCOUNT_ID, accId);
						b.putString(Blpt.KEY_STEPNO,
								(pagStep.get("stekey" + curPag)).get(0));
						b.putString(Blpt.KEY_STEPNUMER,
								(pagStep.get("stekey" + curPag)).get(1));
						b.putString(Blpt.KEY_COMBINID, BaseDroidApp
								.getInstanse().getSecurityChoosed());
						it.putExtra(Blpt.KEY_BUNDLE, b);
						startActivity(it);
					}
				});
	}

	/**
	 * 动态步骤请求
	 * 
	 * @param merId
	 * @param payId
	 * @param prvName
	 * @param stepNo
	 * @param stepNum
	 */
	private void requestPsnPaysSignApplyPre(String stepNo, String stepNum,
			Map<String, String> layoutData, boolean isShow) {
		// 开启通讯框
		if (isShow) {
			BaseHttpEngine.showProgressDialog();
		}
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Blpt.METHOD_NEWAPPLY_PRE);
		Map<String, String> datamap = BlptUtil.getInstance().getMapData();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Blpt.NEWAPPLY_PRE_MERCHANTID, datamap.get(Blpt.KEY_MERCHID));
		map.put(Blpt.NEWAPPLT_PRE_PAYID, datamap.get(Blpt.KEY_PAYJNUM));
		map.put(Blpt.NEWAPPLY_PRE_PRVNAME,
				datamap.get(Blpt.KEY_PROVICESHORTNAME));
		map.put(Blpt.NEWAPPLY_PRE_STEPNO, stepNo);
		map.put(Blpt.NEWAPPLY_PRE_STEPNUM, stepNum);
		if (!StringUtil.isNull(accNumber) && !StringUtil.isNull(accId)) {
			map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_DISP, accNumber);
			map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_REAL, accNumber);
			map.put(Blpt.NEWAPPLY_CONFIRM_ACCID, accId);
		}
		if (!stepNo.equals("-1")) {
			@SuppressWarnings("unchecked")
			Map<String, Object> curData = (Map<String, Object>) pagDataMap
					.get("key" + curPag);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> exeList = (List<Map<String, Object>>) curData
					.get(Blpt.NEWAPPLT_PRE_EXELIST);
			// 封装上传数据
			putData(exeList, null, layoutData, map);
		}
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnPayListCallBack");
	}

	/** 动态步骤返回结果处理 */
	@SuppressWarnings("unchecked")
	public void psnPayListCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(result)) return;
		if (result.containsKey(Blpt.ERRORMSG) && !StringUtil.isNull((String)result.get(Blpt.ERRORMSG))) {
			BaseDroidApp.getInstanse().showMessageDialog((String)result.get(Blpt.ERRORMSG), errorClick); return;
		}
		// 缓存下一步上传的数据
		List<Map<String, Object>> accList = (List<Map<String, Object>>) result
				.get(Blpt.NEWAPPLY_PRE_ACCLIST);
		List<Map<String, Object>> exeList = (List<Map<String, Object>>) result
				.get(Blpt.NEWAPPLT_PRE_EXELIST);
		String stepNo = "";
		String stepNum = "";
		if (!StringUtil.isNullOrEmpty((String) result
				.get(Blpt.NEWAPPLY_PRE_STEPNO_RESPONSE))) {
			stepNo = (String) result.get(Blpt.NEWAPPLY_PRE_STEPNO_RESPONSE);
		}
		if (!StringUtil.isNullOrEmpty((String) result
				.get(Blpt.NEWAPPLY_PRE_STEPNUM_RESPONSE))) {
			stepNum = (String) result.get(Blpt.NEWAPPLY_PRE_STEPNUM_RESPONSE);
		}
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
		pagDataMap.put("key" + curPag, result);

		for (int i = 0; i < mainLayout.getChildCount(); i++) {
			LinearLayout otherLayout = (LinearLayout) mainLayout.getChildAt(i);
			otherLayout.setVisibility(View.GONE);
		}

		// 动态添加控件
		LinearLayout childView = addViews(accList, exeList, null, false);
		mainLayout.addView(childView);

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
