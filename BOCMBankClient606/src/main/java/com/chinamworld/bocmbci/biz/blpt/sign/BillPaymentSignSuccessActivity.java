package com.chinamworld.bocmbci.biz.blpt.sign;

import java.util.List;
import java.util.Map;

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

/***
 * 签约缴费结果页面
 * 
 * @author panwe
 * 
 */
public class BillPaymentSignSuccessActivity extends BillPaymentBaseActivity
		implements OnClickListener {
	/** 主布局 */
	private View viewContent;
	/** 上一步按钮 */
	private Button btnLast;
	/** 下一步按钮 */
	private Button btnNext;
	/** 动态主布局 **/
	private LinearLayout mainLayout;
	/** 签约、非签约标识 **/
	public static int tag;
	/** 加密控件参数 */
	private String otppassword;
	private String otpraNum;
	private String smcpassword;
	private String smcraNum;
	/** 账号信息 **/
	private String accNum;
	private String accId;
	/** 第几步 */
	private String lastpagstepNo;
	/** 总步数 **/
	private String lastpagstepNum;
	/** 提示信息 */
	private TextView tvTiptitle;
	private Map<String, Object> map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.blpt_bill_paypent_title));
		viewContent = (View) LayoutInflater.from(this).inflate(
				R.layout.blpt_bill_newservice_msgadd, null);
		addView(viewContent);
		// 右上角按钮赋值
		setText(this.getString(R.string.go_main));
		setRightBtnClick(rightBtnBackmainClick);
		btnBack.setVisibility(View.GONE);
		initData();
		init();
		// 获取token
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	private void initData() {
		tag = getIntent().getIntExtra(Blpt.KEY_TAG, 0);
//		otppassword = getIntent().getStringExtra(Blpt.KEY_OTPPASW);
//		otpraNum = getIntent().getStringExtra(Blpt.KEY_OTPRANUM);
//		smcpassword = getIntent().getStringExtra(Blpt.KEY_SMSPASW);
//		smcraNum = getIntent().getStringExtra(Blpt.KEY_SMSRANUM);
		accNum = getIntent().getStringExtra(Comm.ACCOUNTNUMBER);
		accId = getIntent().getStringExtra(Comm.ACCOUNT_ID);
		lastpagstepNo = getIntent().getStringExtra(Blpt.KEY_STEPNO);
		lastpagstepNum = getIntent().getStringExtra(Blpt.KEY_STEPNUMER);
		map = (Map<String, Object>)getIntent().getSerializableExtra(Blpt.KEY_OTPPASW);
	}

	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getString(R.string.blpt_signbill_step1),
						this.getString(R.string.blpt_signbill_step2),
						this.getString(R.string.blpt_signbill_step3) });
		StepTitleUtils.getInstance().setTitleStep(3);

		mainLayout = (LinearLayout) viewContent
				.findViewById(R.id.blpt_main_layout);
		btnNext = (Button) viewContent.findViewById(R.id.btnnext);
		btnLast = (Button) viewContent.findViewById(R.id.btnLast);
		btnNext.setOnClickListener(this);
		btnNext.setText(this.getString(R.string.blpt_delete_finsh));
		btnLast.setVisibility(View.GONE);
		tvTiptitle = (TextView) viewContent.findViewById(R.id.tv_bill_title);
	}

	@Override
	public void onClick(View v) {
		BlptUtil.getInstance().finshActivity();
		finish();
	}

	// token返回
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestComit(token);
	}

	/**
	 * 账单缴费缴费请求
	 * 
	 * @param token
	 */
	private void requestComit(String token) {
		// 开启通讯框
		BaseHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Blpt.METHOD_PAY_RESULT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> dataMap = BlptUtil.getInstance().getMapData();
//		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Blpt.PAY_RESULT_MERCHANTID, dataMap.get(Blpt.KEY_MERCHID));
		map.put(Blpt.PAY_RESULT_JNUM, dataMap.get(Blpt.KEY_PAYJNUM));
		map.put(Blpt.PAY_RESULT_PRVNAME, dataMap.get(Blpt.KEY_PROVICESHORTNAME));
		map.put(Blpt.PAY_RESULT_CITY, dataMap.get(Blpt.KEY_CITY));
		map.put(Blpt.BILL_DISNAME, dataMap.get(Blpt.KEY_PAYEENAME));
		map.put(Blpt.PAY_RESULT_STEPNO, lastpagstepNo);
		map.put(Blpt.PAY_RESULT_STEPNUM, lastpagstepNum);
		map.put(Blpt.PAY_RESULT_TOKEN, token);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_DISP, accNum);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_REAL, accNum);
		map.put(Blpt.NEWAPPLY_CONFIRM_ACCID, accId);
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
		List<Map<String, Object>> spcList = BlptUtil.getInstance().getSpList();
		Map<String, String> layoutMap = BlptUtil.getInstance().getLayoutMap();
		BlptUtil.setDataName(exeList, map, Blpt.SIGN_PAY_USERCODE, BlptUtil.getInstance().getUserCode());
		BlptUtil.setDataName(spcList, map, Blpt.SIGN_PAY_USERCODE, BlptUtil.getInstance().getUserCode());
		putData(exeList, spcList, layoutMap, map);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "signComitCallBack");
	}

	/** 提交缴费返回处理 **/
	@SuppressWarnings("unchecked")
	public void signComitCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		
		if (StringUtil.isNullOrEmpty(result)) return;
		if(StringUtil.isNullOrEmpty((String)result.get(Blpt.ERRORCODE))){
			
			List<Map<String, Object>> exeList = (List<Map<String, Object>>) result
					.get(Blpt.PAY_RESULT_EXELIST);
			List<Map<String, Object>> accList = (List<Map<String, Object>>) result
					.get(Blpt.SIGN_PAY_CONFIRM_ACC_LIST);
			List<Map<String, Object>> respList = (List<Map<String, Object>>) result
					.get(Blpt.SIGN_PAY_CONFIRM_SPLIST);
			String tranId = (String) result.get(Blpt.PAY_RESULT_TRANID);
			if (!StringUtil.isNull(tranId)) {
				LinearLayout layoutTranid = (LinearLayout) viewContent
						.findViewById(R.id.layout_tran);
				layoutTranid.setVisibility(View.VISIBLE);
				TextView tvTranid = (TextView) viewContent
						.findViewById(R.id.tv_tranid);
				tvTranid.setText(tranId);
			}
			LinearLayout view = addViews(accList, exeList, respList, false);
			mainLayout.addView(view);

			tvTiptitle.setVisibility(View.VISIBLE);
			tvTiptitle.setText(this.getString(R.string.blpt_pay_success));
		}else {
			BaseDroidApp.getInstanse().showMessageDialog(getResources().getString(R.string.blpt_pay_eorror), errorClick); 
		}
			
//			if (result.containsKey(Blpt.ERRORMSG) && !StringUtil.isNull((String)result.get(Blpt.ERRORMSG))) {
//			BaseDroidApp.getInstanse().showMessageDialog((String)result.get(Blpt.ERRORMSG), errorClick); return;
//		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
