package com.chinamworld.bocmbci.biz.acc.applytermdeposite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 申请定期/活期账户 用户选择界面
 * 
 * @author Administrator
 *
 */
public class ApplyChooseMsgActivity extends AccBaseActivity implements
		OnClickListener {

	/** 主界面 */
	private View mainView;
	/** 绑定介质账号 */
	private TextView tvAccNum;
	/** 绑定介质类型 */
	private TextView tvAccType;
	/** 申请定期活期 */
	private LinearLayout ll_apply;
	/** 投资理财申请定活期 */
	private LinearLayout ll_manage;
	/** 单选按钮 */
	private RadioButton rbDemand;
	/** 下一步按钮 */
	private RadioGroup rgAccOpenType;
	private TextView tv_account_open_reason;
	/** 账户用途 */
	private CheckBox cbSaved, cbWagesPaying,cbSocialMedical, cbInvestment,
			 cbRepaymentLoan,cbDealDayPaying, cbOthers;
	/** 在中国账户开立原因 */
	private LinearLayout ll_open_account_reason;
	private CheckBox  cbLeaving,cbStuding, cbWorking, cbInvesting, cbOther;
	private Button btnNext;
	/** RadioButton选中的结果 */
	private String checkedKey;
	private String checkedResult;
	public String checkedInfo;

	private List<String> accPurposeList;
	private StringBuffer accPurpose;
	private List<String> accReasonList;
	private StringBuffer accReason;
	/** 开户国籍 */
	private Map<String, Object> countryCode;
	private String code;
	/** 存款管理申请账户标示 */
	private int interestRateFlag;
	/** 资金管理标识 */
	private boolean isManageFlag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** 为界面设置标题 */
		setTitle(R.string.acc_apply_title);
		mainView = addView(R.layout.acc_apply_choosemsg);
		init();
		interestRateFlag = this.getIntent().getIntExtra(Dept.APPLICATION_ACCOUNT_FLAG, 0);
	}

	/**
	 * 初始化布局控件
	 */
	public void init() {
		checkedKey = AccBaseActivity.accountTypeList.get(11);
		checkedResult = AccBaseActivity.bankAccountType.get(checkedKey);
		checkedInfo = this.getString(R.string.acc_choose_demand);
		countryCode = AccDataCenter.getInstance().getCountryCode();
		code = (String) countryCode.get(Acc.ACC_QUERY_COUNTRYCODE);
		tvAccNum = (TextView) mainView.findViewById(R.id.tv_accnum);
		tvAccType = (TextView) mainView.findViewById(R.id.tv_acctype);
		isManageFlag = AccDataCenter.getInstance().isManageFlag();
		if (isManageFlag) {
			ll_manage = (LinearLayout) mainView.findViewById(R.id.ll_manage);
			ll_manage.setVisibility(View.VISIBLE);
		} else {
			ll_apply = (LinearLayout) mainView.findViewById(R.id.ll_apply);
			ll_apply.setVisibility(View.VISIBLE);
			rgAccOpenType = (RadioGroup) mainView.findViewById(R.id.rg_accopentype);
			rbDemand = (RadioButton) mainView.findViewById(R.id.rb_demand);
			rgAccOpenType.setOnCheckedChangeListener(onChangeLis);
		}
		tv_account_open_reason = (TextView) mainView
				.findViewById(R.id.tv_account_open_reason);
		cbSaved = (CheckBox) mainView.findViewById(R.id.cb_saved);
		cbWagesPaying = (CheckBox) mainView.findViewById(R.id.cb_wages_paying);
		cbSocialMedical = (CheckBox) mainView
				.findViewById(R.id.cb_social_medical);
		cbInvestment = (CheckBox) mainView.findViewById(R.id.cb_investment);
		cbRepaymentLoan = (CheckBox) mainView
				.findViewById(R.id.cb_repayment_loan);
		cbDealDayPaying = (CheckBox) mainView
				.findViewById(R.id.cb_deal_day_paying);
		cbOthers = (CheckBox) mainView.findViewById(R.id.cb_others);
		ll_open_account_reason = (LinearLayout) mainView
				.findViewById(R.id.ll_open_account_reason);
		if (!code.equals(CHINA)) {
			ll_open_account_reason.setVisibility(View.VISIBLE);
		}
		cbLeaving = (CheckBox) mainView.findViewById(R.id.cb_leaving);
		cbStuding = (CheckBox) mainView.findViewById(R.id.cb_studing);
		cbWorking = (CheckBox) mainView.findViewById(R.id.cb_working);
		cbInvesting = (CheckBox) mainView.findViewById(R.id.cb_investing);
		cbOther = (CheckBox) mainView.findViewById(R.id.cb_other);
		btnNext = (Button) mainView.findViewById(R.id.btnNext);
		Map<String, Object> chooseAcc = AccDataCenter.getInstance()
				.getChooseBankAccount();
		String accnum = (String) chooseAcc.get(Acc.ACC_ACCOUNTNUMBER_RES);
		tvAccNum.setText(StringUtil.getForSixForString(accnum));
		String acctype = AccBaseActivity.bankAccountType.get(chooseAcc
				.get(Acc.ACC_ACCOUNTTYPE_RES));
		tvAccType.setText(acctype);
		showTitle(tv_account_open_reason, code);
		tv_account_open_reason.setEllipsize(TruncateAt.MIDDLE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_account_open_reason);
		
		btnNext.setOnClickListener(this);
	}

	public OnCheckedChangeListener onChangeLis = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// 获取变更后选中项的ID
			int id = group.getCheckedRadioButtonId();
			// 根据ID值获取RadioButton的实例
			RadioButton rb = (RadioButton) ApplyChooseMsgActivity.this
					.findViewById(id);
			if (rb == rbDemand) {
				checkedKey = AccBaseActivity.accountTypeList.get(11);
				checkedResult = AccBaseActivity.bankAccountType.get(checkedKey);
				checkedInfo = getString(R.string.acc_choose_demand);
			} else {
				checkedKey = AccBaseActivity.accountTypeList.get(10);
				checkedResult = AccBaseActivity.bankAccountType.get(checkedKey);
				checkedInfo = getString(R.string.acc_choose_fixed);
			}
		}
	};

	@Override
	public void onClick(View v) {
		// 校验上送数据
		checkContent();

	}

	/**
	 * 获取ConversationId
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		// 发送安全因子请求
		requestGetSecurityFactor(Acc.SERVICEID_OPEN);
	}

	/**
	 * 获取安全因子组合
	 */
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 申请定期活期账户预交易
						requestApplyTermDepositeConfirm();
					}
				});
	}

	/**
	 * 校验上送数据
	 */
	public void checkContent() {
		String message="";
		if ((purposeValue().toString()).equals(ALLNONE)&&(reasonValue().toString()).equals(ALLNONE)) {
			if(code.equals(CHINA)){
				message=this.getString(R.string.acc_please_choose_purpose);
			}else if(code.equals(TAIWAN)||code.equals(HONGKONG)||code.equals(AOMEN)){
				message=this.getString(R.string.acc_please_choose_all1);
			}else{
				message=this.getString(R.string.acc_please_choose_all2);
			}
		}else if((purposeValue().toString()).equals(ALLNONE)&&!(reasonValue().toString())
				.equals(ALLNONE)){
			message=this.getString(R.string.acc_please_choose_purpose);
		}else if(!(purposeValue().toString()).equals(ALLNONE)&&(reasonValue().toString())
				.equals(ALLNONE)){
			if(code.equals(CHINA)){
				
			}else if(code.equals(TAIWAN)||code.equals(HONGKONG)||code.equals(AOMEN)){
				message=this.getString(R.string.acc_please_choose_open_reason);
			}else{
				message=this.getString(R.string.acc_please_choose_china_open_reason);
			}
		}
		if(message.equals("")){
			
		}else{
			BaseDroidApp.getInstanse().showMessageDialog(message, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					BaseDroidApp.getInstanse().dismissErrorDialog();
				}
			});
			return;
		}
		BaseHttpEngine.showProgressDialog();
		// 获取会话Id
		requestCommConversationId();
	}

	/**
	 * 获取账户用途的信息
	 */
	public StringBuffer purposeValue() {
		accPurpose = new StringBuffer();
		accPurposeList = new ArrayList<String>();
		if (cbSaved.isChecked()) {
			accPurpose.append("1");
			accPurposeList.add(getString(R.string.acc_saved));
		} else {
			accPurpose.append("0");
			accPurposeList.remove(getString(R.string.acc_saved));
		}
		if (cbWagesPaying.isChecked()) {
			accPurpose.append("1");
			accPurposeList.add(getString(R.string.acc_wages_paying));
		} else {
			accPurpose.append("0");
			accPurposeList.remove(getString(R.string.acc_wages_paying));
		}
		if (cbSocialMedical.isChecked()) {
			accPurpose.append("1");
			accPurposeList.add(getString(R.string.acc_social_medical));
		} else {
			accPurpose.append("0");
			accPurposeList.remove(getString(R.string.acc_social_medical));
		}
		if (cbInvestment.isChecked()) {
			accPurpose.append("1");
			accPurposeList.add(getString(R.string.acc_investment));
		} else {
			accPurpose.append("0");
			accPurposeList.remove(getString(R.string.acc_investment));
		}
		
		if (cbRepaymentLoan.isChecked()) {
			accPurpose.append("1");
			accPurposeList.add(getString(R.string.acc_repayment_loan));
		} else {
			accPurpose.append("0");
			accPurposeList.remove(getString(R.string.acc_repayment_loan));
		}
		if (cbDealDayPaying.isChecked()) {
			accPurpose.append("1");
			accPurposeList.add(getString(R.string.acc_deal_day_paying));
		} else {
			accPurpose.append("0");
			accPurposeList.remove(getString(R.string.acc_deal_day_paying));
		}
		if (cbOthers.isChecked()) {
			accPurpose.append("1");
			accPurposeList.add(getString(R.string.acc_others));
		} else {
			accPurpose.append("0");
			accPurposeList.remove(getString(R.string.acc_others));
		}
		accPurpose.append("000");
		return accPurpose;
	}

	/**
	 * 获取开户原因的值
	 */
	public StringBuffer reasonValue() {
		accReason = new StringBuffer();
		accReasonList = new ArrayList<String>();
		if (cbLeaving.isChecked()) {
			accReason.append("1");
			accReasonList.add(getString(R.string.acc_leaving));
		} else {
			accReason.append("0");
			accReasonList.remove(getString(R.string.acc_leaving));
		}
		if (cbStuding.isChecked()) {
			accReason.append("1");
			accReasonList.add(getString(R.string.acc_studing));
		} else {
			accReason.append("0");
			accReasonList.remove(getString(R.string.acc_studing));
		}
		if (cbWorking.isChecked()) {
			accReason.append("1");
			accReasonList.add(getString(R.string.acc_working));
		} else {
			accReason.append("0");
			accReasonList.remove(getString(R.string.acc_working));
		}
		
		if (cbInvesting.isChecked()) {
			accReason.append("1");
			accReasonList.add(getString(R.string.acc_investing));
		} else {
			accReason.append("0");
			accReasonList.remove(getString(R.string.acc_investing));
		}
		if (cbOther.isChecked()) {
			accReason.append("1");
			accReasonList.add(getString(R.string.acc_others));
		} else {
			accReason.append("0");
			accReasonList.remove(getString(R.string.acc_others));
		}
		accReason.append("00000");
		return accReason;
	}

	/**
	 * 申请定期活期账户预交易
	 */
	public void requestApplyTermDepositeConfirm() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_PSNAPPLYTERMDEPOSITECONFIRM);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> parms = new HashMap<String, Object>();
		Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		parms.put(Acc.ACC_APPLY_PLAN_ACCOUNTID, AccDataCenter.getInstance()
				.getChooseBankAccount().get(Acc.ACC_ACCOUNTID_RES));
		if (isManageFlag) {
			parms.put(Acc.ACC_APPLY_PLAN_ACCOUNTTYPE, AccBaseActivity.accountTypeList.get(10));
			parms.put(Acc.ACC_APPLY_PLAN_ACCOUNTTYPESMS, AccBaseActivity.bankAccountType.get(AccBaseActivity.accountTypeList.get(10)));
		} else {
			parms.put(Acc.ACC_APPLY_PLAN_ACCOUNTTYPE, checkedKey);
			parms.put(Acc.ACC_APPLY_PLAN_ACCOUNTTYPESMS, checkedResult);
		}
		parms.put(Acc.ACC_APPLY_PLAN_NAME, logInfo.get(Login.CUSTOMER_NAME));
		parms.put(Acc.ACC_APPLY_PLAN_ACCOUNTPURPOSE, purposeValue().toString());
		if (code.equals(CHINA)) {
			parms.put(Acc.ACC_APPLY_PLAN_OPENINGREASON, null);
		} else {
			parms.put(Acc.ACC_APPLY_PLAN_OPENINGREASON, reasonValue()
					.toString());
		}
		parms.put(Acc.ACC_APPLY_PLAN_COMBINID, BaseDroidApp.getInstanse()
				.getSecurityChoosed());
		biiRequestBody.setParams(parms);
		HttpManager.requestBii(biiRequestBody, this,
				"applyTermDepositeConfirmCallBack");
		AccDataCenter.getInstance().setPurposeList(accPurposeList);
		AccDataCenter.getInstance().setReasonList(accReasonList);
	}

	/**
	 * 申请定期活期与交易返回信息
	 */
	@SuppressWarnings("unchecked")
	public void applyTermDepositeConfirmCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_apply_confirm_fail));
			return;
		}
		AccDataCenter.getInstance().setApplyConfirmResult(result);

		LogGloble.i("Myself", result.toString());
		BaseHttpEngine.dissMissProgressDialog();
		if (interestRateFlag == APPLICATION_ACCOUNT) { // 存款管理申请账户
			Intent intent = new Intent(this, ApplyConfirmActivity.class);
			intent.putExtra(Acc.ACC_APPLY_PLAN_ACCOUNTTYPE, checkedKey);
			intent.putExtra(Acc.ACC_APPLY_PLAN_ACCOUNTTYPESMS, checkedResult);
			intent.putExtra("info", checkedInfo);
			intent.putExtra(Dept.APPLICATION_ACCOUNT_FLAG , interestRateFlag);
			startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		} else if (isManageFlag){
			Intent intent = new Intent(this, ApplyConfirmActivity.class);
			intent.putExtra(Acc.ACC_APPLY_PLAN_ACCOUNTTYPE, checkedKey);
			intent.putExtra(Acc.ACC_APPLY_PLAN_ACCOUNTTYPESMS, checkedResult);
			intent.putExtra("info", checkedInfo);
			startActivityForResult(intent, 10023);
		} else {
			Intent intent = new Intent(this, ApplyConfirmActivity.class);
			intent.putExtra(Acc.ACC_APPLY_PLAN_ACCOUNTTYPE, checkedKey);
			intent.putExtra(Acc.ACC_APPLY_PLAN_ACCOUNTTYPESMS, checkedResult);
//		intent.putExtra(Acc.ACC_APPLY_PLAN_ACCOUNTPURPOSE,
//				accPurpose.toString());
//		intent.putExtra(Acc.ACC_APPLY_PLAN_OPENINGREASON, accReason.toString());
			intent.putExtra("info", checkedInfo);
			startActivity(intent);
		}
	}
}
