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
 * 撤销结果页
 * 
 * @author panwe
 * 
 */
public class BillRevocationSignSuccessActivity extends BillPaymentBaseActivity
		implements OnClickListener {

	/** 主布局 */
	private View viewContent;
	/** 取消按钮 */
	private Button btnCancer;
	/** 确认按钮 */
	private Button btnConfirm;
	/** 动态主布局 **/
	private LinearLayout mainLayout;
	/** 安全控件 **/
	private String otppasword;
	private String otpranum;
	private String smcpasword;
	private String smcranum;
	private String accNum;
	private String accId;
	/** 第几步 */
	private String laststepNo;
	/** 总步数 **/
	private String laststepNum;
	/** 提示信息 */
	private TextView tvTip;
	private Map<String, Object> map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.blpt_bill_cance));
		viewContent = (View) LayoutInflater.from(this).inflate(
				R.layout.blpt_bill_newservice_msgadd, null);
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

	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getString(R.string.blpt_cancer_add),
						this.getString(R.string.blpt_cancer_confirm),
						this.getString(R.string.blpt_cancer_su) });
		StepTitleUtils.getInstance().setTitleStep(3);

		mainLayout = (LinearLayout) findViewById(R.id.blpt_main_layout);
		tvTip = (TextView) viewContent.findViewById(R.id.tv_bill_title);
		btnCancer = (Button) findViewById(R.id.btnLast);
		btnConfirm = (Button) findViewById(R.id.btnnext);
		btnConfirm.setText(this.getString(R.string.blpt_delete_finsh));
		btnCancer.setVisibility(View.GONE);
		btnConfirm.setOnClickListener(this);
		btnCancer.setOnClickListener(this);

	}

	private void getData() {
		Bundle b = getIntent().getBundleExtra(Blpt.KEY_BUNDLE);
//		otppasword = b.getString(Blpt.KEY_OTPPASW);
//		otpranum = b.getString(Blpt.KEY_OTPRANUM);
//		smcpasword = b.getString(Blpt.KEY_SMSPASW);
//		smcranum = b.getString(Blpt.KEY_SMSRANUM);
		accNum = b.getString(Comm.ACCOUNTNUMBER);
		accId = b.getString(Comm.ACCOUNT_ID);
		laststepNo = b.getString(Blpt.KEY_STEPNO);
		laststepNum = b.getString(Blpt.KEY_STEPNUMER);
		map = (Map<String, Object>)getIntent().getSerializableExtra(Blpt.KEY_OTPPASW);
	}

	// token返回
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestCancer(token);
	}

	/**
	 * 撤销缴费
	 * 
	 * @param token
	 */
	private void requestCancer(String token) {
		// 开启通讯框
		BaseHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Blpt.METHOD_CANCER_RESULT);
		Map<String, String> dataMap = BlptUtil.getInstance().getMapData();
//		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Blpt.CANCER_MERCHANTID_RESULT, dataMap.get(Blpt.KEY_MERCHID));
		map.put(Blpt.CANCER_PAIID_RESULT, dataMap.get(Blpt.KEY_PAYJNUM));
		map.put(Blpt.CANCER_PRSULT_PRV, dataMap.get(Blpt.KEY_PROVICESHORTNAME));
		map.put(Blpt.PAY_RESULT_CITY, dataMap.get(Blpt.KEY_CITY));
		map.put(Blpt.BILL_DISNAME, dataMap.get(Blpt.KEY_PAYEENAME));
		map.put(Blpt.CANCER_RESULT_STEPNO, laststepNo);
		map.put(Blpt.PAY_RESULT_STEPNUM, laststepNum);
		map.put(Blpt.CANCER_RESULT_TOKEN, token);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_DISP, accNum);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_REAL, accNum);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCID, accId);
//		if (!StringUtil.isNull(otppasword)) {
//			map.put(Blpt.OTP, otppasword);
//		}
//		if (!StringUtil.isNull(otpranum)) {
//			map.put(Blpt.OTP_RC, otpranum);
//		}
//		if (!StringUtil.isNull(smcpasword)) {
//			map.put(Comm.Smc, smcpasword);
//		}
//		if (!StringUtil.isNull(smcranum)) {
//			map.put(Comm.Smc_Rc, smcranum);
//		}
		// 封装上传数据
		List<Map<String, Object>> exeList = BlptUtil.getInstance().getExeList();
		Map<String, String> layoutMap = BlptUtil.getInstance().getLayoutMap();
		BlptUtil.setDataName(exeList, map, Blpt.SIGN_PAY_USERCODE, BlptUtil.getInstance().getUserCode());
		putData(exeList, null, layoutMap, map);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "signCancerCallBack");
	}

	/** 撤销缴费返回结果 **/
	@SuppressWarnings("unchecked")
	public void signCancerCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(result)) return;
		if (result.containsKey(Blpt.ERRORMSG) && !StringUtil.isNull((String)result.get(Blpt.ERRORMSG))) {
			BaseDroidApp.getInstanse().showMessageDialog((String)result.get(Blpt.ERRORMSG), errorClick); return;
		}
		List<Map<String, Object>> exeList = (List<Map<String, Object>>) result
				.get(Blpt.CANCER_RESULT_EXELIST);
		List<Map<String, Object>> acctList = (List<Map<String, Object>>) result
				.get(Blpt.NEWAPPLY_PRE_ACCLIST);
		LinearLayout view = addViews(acctList, exeList, null, false);
		mainLayout.addView(view);
		tvTip.setVisibility(View.VISIBLE);
		tvTip.setText(this.getString(R.string.blpt_sign_cancer_suinfo));
	}

	@Override
	public void onClick(View v) {
		BlptUtil.getInstance().finshActivity();
		Intent it = new Intent(this, BillHadAppliedActivity.class);
		it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(it);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
