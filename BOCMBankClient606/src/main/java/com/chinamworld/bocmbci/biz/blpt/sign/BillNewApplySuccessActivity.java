package com.chinamworld.bocmbci.biz.blpt.sign;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 申请新服务结果
 * 
 * @author panwe
 * 
 */
public class BillNewApplySuccessActivity extends BillPaymentBaseActivity
		implements OnClickListener {

	/** 主布局 */
	private View viewContent;
	/** 确定按钮 */
	private Button btnConfirm;
	/** 动态布局页面 **/
	private LinearLayout mainLayout;
	/** 安全口令 **/
	private String otppassword;
	private String otpraNum;
	private String smcpassword;
	private String smcraNum;
	/** 账号信息 **/
	private String accNum;
	private String accId;
	/** 提示信息 **/
	private TextView tvTip;
	/** 第几步 **/
	private String laststepNo;
	/** 总步数 **/
	private String laststepNum;
	/**安全工具参数获取*/
	private Map<String,Object> map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.blpt_newapply_title));
		viewContent = (View) LayoutInflater.from(this).inflate(
				R.layout.blpt_bill_apply_success, null);
		addView(viewContent);
		// 右上角按钮赋值
		setText(this.getString(R.string.go_main));
		setRightBtnClick(rightBtnBackmainClick);
		btnBack.setVisibility(View.GONE);
		getData();
		init();
		// 发送获取token请求
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	/** 接收上一页传入数据 */
	private void getData() {
		Intent intent = getIntent();
//		otppassword = intent.getStringExtra(Blpt.KEY_OTPPASW);
//		otpraNum = intent.getStringExtra(Blpt.KEY_OTPRANUM);
//		smcpassword = intent.getStringExtra(Blpt.KEY_SMSPASW);
//		smcraNum = intent.getStringExtra(Blpt.KEY_SMSRANUM);
		accNum = intent.getStringExtra(Comm.ACCOUNTNUMBER);
		accId = intent.getStringExtra(Comm.ACCOUNT_ID);
		laststepNo = intent.getStringExtra(Blpt.KEY_STEPNO);
		laststepNum = intent.getStringExtra(Blpt.KEY_STEPNUMER);
		map = (Map<String,Object>)intent.getSerializableExtra(Blpt.KEY_OTPPASW);
		
		
		
	}

	/** 初始化 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getString(R.string.blpt_applynewbill_step2),
						this.getString(R.string.blpt_applynewbill_step3),
						this.getString(R.string.blpt_applynewbill_step4) });
		StepTitleUtils.getInstance().setTitleStep(3);

		btnConfirm = (Button) viewContent.findViewById(R.id.btnconfirm);
		btnConfirm.setText(this.getString(R.string.blpt_delete_finsh));
		btnConfirm.setOnClickListener(this);
		mainLayout = (LinearLayout) viewContent.findViewById(R.id.main_layout);
		tvTip = (TextView) viewContent.findViewById(R.id.tv_confirm_title_2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnconfirm:
			BlptUtil.getInstance().finshActivity();
			Intent intent = new Intent(this, BillHadAppliedActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("isapplySU", true);
			startActivity(intent);
			finish();
			break;
		}
	}

	// token返回
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestApplyComit(token);
	}

	/**
	 * 申请服务提交
	 * 
	 * @param tokenId
	 */
	private void requestApplyComit(String tokenId) {
		// 开启通讯框
		BaseHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Blpt.METHOD_NEWAPPLY_RESULT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> datamap = BlptUtil.getInstance().getMapData();
	//	Map<String, Object> map = new HashMap<String, Object>();
		map.put(Blpt.NEWAPPLY_RESULT_MERCHANTID, datamap.get(Blpt.KEY_MERCHID));
		map.put(Blpt.NEWAPPLY_RESULT_PAYID, datamap.get(Blpt.KEY_PAYJNUM));
		map.put(Blpt.NEWAPPLY_RESULT_PRV,
				datamap.get(Blpt.KEY_PROVICESHORTNAME));
		map.put(Blpt.BILL_DISNAME, datamap.get(Blpt.KEY_PAYEENAME));
		map.put(Blpt.PAY_RESULT_CITY, datamap.get(Blpt.KEY_CITY));
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_DISP, accNum);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_REAL, accNum);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCID, accId);
		map.put(Blpt.NEWAPPLY_RESULT_STEPNO, laststepNo);
		map.put(Blpt.NEWAPPLY_RESULT_STEPNUM, laststepNum);
		map.put(Blpt.NEWAPPLY_RESULT_TOKEN, tokenId);
//		if (!StringUtil.isNullOrEmpty(otppassword)) {
//			map.put(Blpt.OTP, otppassword);
//		}
//		if (!StringUtil.isNullOrEmpty(otpraNum)) {
//			map.put(Blpt.OTP_RC, otpraNum);
//		}
//		if (!StringUtil.isNullOrEmpty(smcpassword)) {
//			map.put(Comm.Smc, smcpassword);
//		}
//		if (!StringUtil.isNullOrEmpty(smcraNum)) {
//			map.put(Comm.Smc_Rc, smcraNum);
//		}

		// 封装上传数据
		List<Map<String, Object>> exeList = BlptUtil.getInstance().getExeList();
		Map<String, String> layoutMap = BlptUtil.getInstance().getLayoutMap();
		putData(exeList, null, layoutMap, map);
		SipBoxUtils.setSipBoxParams(map);

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "signApplyComitCallBack");
	}

	/** 提交申请返回处理 **/
	@SuppressWarnings("unchecked")
	public void signApplyComitCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(result)) return;
		if (result.containsKey(Blpt.ERRORMSG) && !StringUtil.isNull((String)result.get(Blpt.ERRORMSG))) {
			BaseDroidApp.getInstanse().showMessageDialog((String)result.get(Blpt.ERRORMSG), errorClick); return;
		}
		List<Map<String, Object>> exeList = (List<Map<String, Object>>) result
				.get(Blpt.NEWAPPLY_RESULT_EXELIST);
		List<Map<String, Object>> acctList = (List<Map<String, Object>>) result
				.get(Blpt.NEWAPPLY_PRE_ACCLIST);
		LinearLayout view = addViews(acctList, exeList, null, false);
		mainLayout.addView(view);
		tvTip.setVisibility(View.VISIBLE);
		tvTip.setText(this.getString(R.string.blpt_apply_su_tip));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
