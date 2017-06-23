
package com.chinamworld.bocmbci.biz.tran.ecard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.Ecard;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.login.LoginActivity;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee.QueryExternalBankActivity1;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.NoRelBankOtherConfirmInfoActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressLint("InflateParams")
public class TransferEcardActivity extends TranBaseActivity {
	private Context context = this;
	private LayoutInflater inflater = null;
	private View container;// 布局
	/** 转入列表详情 */
	private LinearLayout ecardOutDetailLayout = null;
	/** 转出列表详情 */
	private LinearLayout ecardInDetailLayout = null;
	/** 底部布局 */
	private LinearLayout bottomLayout = null;
	/** 其他模块跳入标识 */
	private int otherPartJumpFlag = 0;
	/** 转出账户控件 */
	private RelativeLayout ll_ecard_out, // 转出
			ll_ecard_in;// 转入
	// T43快捷方式按钮
	private LinearLayout tran_acc_seach_linear;
	
	/** 转出账户信息 */
	private Map<String, Object> ecardOutforMap;
	/** 转入账户信息 */
	private Map<String, Object> ecardInforMap;
	/** 转出账户可以用余额 */
	private String availableBalance;
	/** 币种代码,发送到服务器 */
	private String currencyCode;
	private String cashRemitCode;
	/** 修改昵称 */
	private TextView tv_bindName;
	private FrameLayout fl_acc_nickname;
	private LinearLayout ll_nickname;
	private Button btn_updatenickname;
	private EditText et_acc_accountnickname;
	private String nickname;
	// private String accountId;
	// 底部布局
	private RadioGroup radioGroup_ecard; //
	private RadioButton radio_shishi_ecard, radio_common_ecard;// 实时 普通
	private EditText et_ecard_count, et_ecard_fuyan;// 金额 附言
	private String amount, memo;
	private CheckBox sendSmcToPayeeCk;// 通知收款人
	private LinearLayout sendSmcToPayeeLl = null;// 通知收款人 布局
	private Button btn_ecard_next;// 下一步
	/** 是否发送手机短信通知收款人标示 */
	private String isSendSmc = ConstantGloble.FALSE;
	private LinearLayout ll_kbank, ll_sbank;// 开户行 所属行
	private TextView tv_kbank_name, tv_kbankname, tv_sbank_name, tv_sbankname;//
//	private Button btn_kbank_search, btn_sbank_search;
	/** 执行方式 */
	private String tranMode;
	/** 收款人手机号 */
	private EditText payeeMobileEt;
	private String payeeMobile;
	/** 绑定id */
	private String combineId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.tran_ecard_trans));
		container = mInflater.inflate(R.layout.tran_ecard_layout, null);
		inflater = LayoutInflater.from(this);
		tabcontent.removeAllViews();
		tabcontent.addView(container);
		
		setLeftSelectedPosition("tranManager_8");
		initView(container);
		// 处理其他模块跳转进来情况
		dealWithOtherPart();
	}

	private void dealWithOtherPart() {
		Intent intent = getIntent();
		if (intent != null) {
			otherPartJumpFlag = intent.getIntExtra(
					ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
			if (StringUtil.isNullOrEmpty(otherPartJumpFlag)) {
				return;
			}
			switch (otherPartJumpFlag) {

			case ConstantGloble.ACC_TO_TRAN_ECARD:

				ecardOutforMap = TranDataCenter.getInstance()
						.getAccOutInfoMap();
				String accountId = (String) ecardOutforMap.get(Comm.ACCOUNT_ID);
				requestForAccountDetail(accountId);

				break;
				
			default:

				String segmentId=BaseDroidApp.getInstanse().getSegmentInfo();
				if("10".equals(segmentId)
						 &&"2".equals(LoginActivity.qryCustType)){
						
					requestEcardoutAccountList();
						}else{
							ScrollView ll_isecard=(ScrollView) container.findViewById(R.id.ll_isecard);
							ll_isecard.setVisibility(View.GONE);
							LinearLayout nocardLayout=(LinearLayout) container.findViewById(R.id.nocardLayout);
							nocardLayout.setVisibility(View.VISIBLE);
						}
				
				
				break;
			}
		}

	}

	private void initView(View v) {
		
		ll_ecard_out = (RelativeLayout) v.findViewById(R.id.ll_ecard_out);
		ll_ecard_in = (RelativeLayout) v.findViewById(R.id.ll_ecard_in);
		bottomLayout = (LinearLayout) v.findViewById(R.id.ecard_bottom_layout);
		ll_ecard_in.setEnabled(false);
		ll_ecard_out.setEnabled(false);
		// ll_ecard_out.setOnClickListener(ecardOutClicklistener);
		mTopRightBtn.setVisibility(View.GONE);
		tran_acc_seach_linear = (LinearLayout) v
				.findViewById(R.id.tran_acc_seach_linear);
		tran_acc_seach_linear.setVisibility(View.GONE);

	}



//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("tranManager_8");
//
//	}


	/**
	 * 查询其他卡（除信用卡）详情 PsnAccountQueryAccountDetail
	 * 
	 * @param accountId
	 */
	private void requestForAccountDetail(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.ACCOUNTDETAIL);
		paramsmap.put(Tran.ACC_ACCOUNTID_REQ, accountId);
		biiRequestBody.setParams(paramsmap);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestForAccountDetailCallBack");
	}

	/**
	 * 查询其他卡（除信用卡）详情 PsnAccountQueryAccountDetail 返回
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestForAccountDetailCallBack(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		TranDataCenter.getInstance().setCurrOutDetail(resultMap);
		String accountId = (String) ecardOutforMap.get(Comm.ACCOUNT_ID);
		PsnCardQueryBindInfo(accountId);
	}



	/**
	 * 调用接口：PsnCommonQueryAllChinaBankAccount 请求给定类型转出账户列表
	 * */
	protected void requestEcardoutAccountList() {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.COMMONQUERYALLCHINABANKACCOUNT);
		String[] accountTypeArr = new String[] { ConstantGloble.ACC_TYPE_BRO };
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.ACCOUNTTYPE_REQ, accountTypeArr);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestEcardoutAccountListCallBack");

	}

	/**
	 * PsnCommonQueryAllChinaBankAccount接口的回调方法，返回结果 *
	 */
	@SuppressWarnings("unchecked")
	public void requestEcardoutAccountListCallBack(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody
				.getResult();

//		if (StringUtil.isNullOrEmpty(result)) {// 如果没有符合要求的数据 弹框提示
//			String message = this.getString(R.string.no_accout);
//			BaseDroidApp.getInstanse().showErrorDialog(message,
//					R.string.cancle, R.string.confirm, onNoOutclicklistener);
//			return;
//		}

		for (Map<String, Object> map : result) {
			String accType = (String) map.get(Acc.ACC_ACCOUNTTYPE_RES);
			String eCard = (String) map.get(Ecard.ACC_ECARD_RES);
			if (accType.equals(ConstantGloble.ACC_TYPE_BRO)
					&& "1".equals(eCard)) {
				ecardOutforMap = map;
				TranDataCenter.getInstance().setAccOutInfoMap(ecardOutforMap);
				String accountId = (String) map.get(Comm.ACCOUNT_ID);
				requestForAccountDetail(accountId);
				break;
			}
		}
		if(StringUtil.isNullOrEmpty(ecardOutforMap)){
			BaseHttpEngine.dissMissProgressDialog();
		
			BaseDroidApp.getInstanse().showMessageDialog( getResources().getString(R.string.collect_common_error),new OnClickListener() {
				@Override
				public void onClick(View v) {
						finish();
				}
			});
			return;
		}

	}

	/**
	 * 调用接口：PsnCardQueryBindInfo 查询电子卡对应绑定卡信息
	 * */
	protected void PsnCardQueryBindInfo(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Ecard.PSNCARDQUERYBINDIFGO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.ACC_ACCOUNTID_REQ, accountId);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"PsnCardQueryBindInfoCallBack");

	}

	public void PsnCardQueryBindInfoCallBack(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		ecardInforMap = resultMap;
		ecardInforMap.put(Ecard.ECARD_ACCOUNUNUMBER_RES, null);
		TranDataCenter.getInstance().setAccInInfoMap(ecardInforMap);
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().dismissMessageDialog();
		showEcardOutDetailView();
		showEcardInDetailView();
		showEcardBottomView();

	}

	private void showEcardBottomView() {

		currencyCode = ConstantGloble.PRMS_CODE_RMB;
		cashRemitCode = ConstantGloble.CASHREMIT_00;
		View v = mInflater.inflate(R.layout.tran_ecrcd_bottom_layout, null);
		initecardbottomview(v);
		bottomLayout.removeAllViews();
		bottomLayout.addView(v);

	}

	private void initecardbottomview(View v) {
		radioGroup_ecard = (RadioGroup) v.findViewById(R.id.radioGroup_ecard);
		radio_shishi_ecard = (RadioButton) v
				.findViewById(R.id.radio_shishi_ecard);
		radio_shishi_ecard.performClick();
		tranTypeFlag = TRANTYPE_SHISHI_BANKOTHER;
		radio_common_ecard = (RadioButton) v
				.findViewById(R.id.radio_common_ecard);
		radioGroup_ecard.setOnCheckedChangeListener(radiogrouplistener);
		et_ecard_count = (EditText) v.findViewById(R.id.et_ecard_count);
		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(et_ecard_count, tv_for_amount);
		et_ecard_fuyan = (EditText) v.findViewById(R.id.et_ecard_fuyan);
		EditTextUtils.setLengthMatcher(this, et_ecard_fuyan, 20);
		sendSmcToPayeeCk = (CheckBox) v
				.findViewById(R.id.ck_sendSmcPayee_bocTrans_transSeting);
		sendSmcToPayeeCk.setOnCheckedChangeListener(checkboxlistener);
		sendSmcToPayeeLl = (LinearLayout) v
				.findViewById(R.id.ll_sendSmcPayee_bocTrans_tranSeting);

		payeeMobileEt = (EditText) v
				.findViewById(R.id.et_commBoc_payeeMobile_transSeting);

		btn_ecard_next = (Button) v.findViewById(R.id.btn_ecard_next);
		btn_ecard_next.setOnClickListener(btnOnClicklistener);

		ll_kbank = (LinearLayout) v.findViewById(R.id.ll_kbank);
		tv_kbank_name = (TextView) v.findViewById(R.id.tv_kbank_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_kbank_name);
		tv_kbankname = (TextView) v.findViewById(R.id.tv_kbankname);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_kbankname);
//		btn_kbank_search = (Button) v.findViewById(R.id.btn_kbank_search);
//		btn_kbank_search.setOnClickListener(btnOnClicklistener);

//		ll_sbank = (LinearLayout) v.findViewById(R.id.ll_sbank);
//		tv_sbank_name = (TextView) v.findViewById(R.id.tv_sbank_name);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
//				tv_sbank_name);
//		tv_sbankname = (TextView) v.findViewById(R.id.tv_sbankname);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
//				tv_sbankname);
//		btn_sbank_search = (Button) v.findViewById(R.id.btn_sbank_search);
//		btn_sbank_search.setOnClickListener(btnOnClicklistener);

	}

	/**
	 * 转账类型radiogroup 监听
	 */
	CompoundButton.OnCheckedChangeListener checkboxlistener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				isSendSmc = ConstantGloble.TRUE;
				sendSmcToPayeeLl.setVisibility(View.VISIBLE);
			} else {
				isSendSmc = ConstantGloble.FALSE;
				sendSmcToPayeeLl.setVisibility(View.GONE);
			}
		}
	};
	/**
	 * 转账类型radiogroup 监听
	 */
	OnCheckedChangeListener radiogrouplistener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {

			case R.id.radio_shishi_ecard:// 实时
				radio_shishi_ecard.setChecked(true);
				ll_kbank.setVisibility(View.GONE);
//				ll_sbank.setVisibility(View.VISIBLE);
				tranTypeFlag = TRANTYPE_SHISHI_BANKOTHER;
				
				
//				PsnOtherBankQueryForTransToAccount("HOT");

				break;
			case R.id.radio_common_ecard:// 普通
				radio_common_ecard.setChecked(true);
				ll_kbank.setVisibility(View.VISIBLE);
//				ll_sbank.setVisibility(View.GONE);
				tranTypeFlag = TRANTYPE_NOREL_BANKOTHER;
				tv_kbankname.setVisibility(View.VISIBLE);
				tv_kbankname.setText((String) ecardInforMap
						.get(Ecard.ECARD_OPENINGBANKNAME_RES));

				break;

			}

		}
	};
	/**
	 * 转账类型radiogroup 监听
	 */
	OnClickListener btnOnClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_ecard_next:// 下一步

				if (!checkRMBMessage()) {
					return;
				}
				;
				amount = et_ecard_count.getText().toString().trim();
				memo = et_ecard_fuyan.getText().toString().trim();
				payeeMobile = payeeMobileEt.getText().toString().trim();

				// 判断转账金额是否大于5万
				if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
					if (!moneyLowFiveMiriade(amount)) {
						return;
					}
				}
				// 转账金额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成
				tranMode = ConstantGloble.NOWEXE;
				// 判断是否有开户行,如果没有报错

				if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
					
//					if(ll_sbank.getVisibility()== View.VISIBLE){
//						boolean addressflag = judgeBank();
//						if (!addressflag) {
//							return;
//						}	
//					}
					

				} else if (tranTypeFlag == TRANTYPE_NOREL_BANKOTHER) {
					boolean addressflag = judgeAddress();
					if (!addressflag) {
						return;
					}

				}

				boolean flag = judgeUserData(amount, true);
				if (!flag) {
					return;
				}
				// 保存用户输入数据
				saveBankInUserData();
				// 手续费试算接口
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();

				break;
			case R.id.btn_kbank_search:// 查询开户行
				requestQueryExternalBank(0, 10);
				break;
//			case R.id.btn_sbank_search:// 查询所属行
//				PsnOtherBankQueryForTransToAccount2("OTHER");
//				break;

			}

		}
	};

	
	/**
	 * 判断跨行是否有开户行
	 * 
	 * @return
	 */
	public boolean judgeAddress() {
		String toOrgName = tv_kbankname.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(toOrgName)) {
			String errorInfo = getResources().getString(
					R.string.inbank_name_error);
			BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
			return false;
		}
		return true;
	}
	
	/**
	 * 判断跨行是否有所属行
	 * 
	 * @return sunh
	 */
	public boolean judgeBank() {
		String toOrgName = tv_sbankname.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(toOrgName)) {
			String errorInfo = getResources().getString(
					R.string.shishibank_name_error);
			BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
			return false;
		}
		return true;
	}
	
	private boolean judgeUserData(String amount, boolean isMountCompare) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean transAmountReg = checkJapCkReg(currencyCode, getResources()
				.getString(R.string.reg_transferAmount), amount);
		lists.add(transAmountReg);
		if (isSendSmc.equals(ConstantGloble.TRUE)) {// 如果有选择短信通知收款人 则需要验证手机号
			RegexpBean payeeMobileReg = new RegexpBean(getResources()
					.getString(R.string.tel_num_str), payeeMobile,
					"shoujiH_01_15");
			lists.add(payeeMobileReg);
		}
		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}
		Map<String, Object> detailMap = TranDataCenter.getInstance()
				.getCurrOutDetail();
		if (isMountCompare && detailMap != null) {
			if (detailMap.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST) != null) {
				if (availableBalance != null
						&& Double.parseDouble(amount) > Double
								.parseDouble(availableBalance)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getString(R.string.amount_wrong_two));
					return false;
				}
			}
		}
		return true;
	}

	private boolean moneyLowFiveMiriade(String money) {
		if (!StringUtil.isNull(money)) {
			double dmoney = Double.parseDouble(money);
			if (dmoney > 50000) {
				BaseDroidApp.getInstanse().showMessageDialog(
						"实时转账的交易金额不能高于5万元", new OnClickListener() {
							@Override
							public void onClick(View v) {
								et_ecard_count.getText().clear();
								BaseDroidApp.getInstanse()
										.dismissMessageDialog();
							}
						});
				return false;
			}
		}

		return true;
	}

	private void saveBankInUserData() {
		Map<String, String> userInputMap = new HashMap<String, String>();
		userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
		userInputMap.put(Tran.INPUT_TRANSFER_REMARK, memo);
		userInputMap.put(ConstantGloble.IS_SEND_SMC, isSendSmc);
		userInputMap.put(Tran.INPUT_ADD_PAYEE_FALG, "false");
		userInputMap.put(Tran.MANAGE_PRE_transMode_RES, tranMode);
		if (isSendSmc.equals(ConstantGloble.TRUE)) {
			userInputMap.put(Tran.INPUT_PAYEE_TRANSFER_MOBILE, payeeMobile);
		} else {
			userInputMap.put(Tran.INPUT_PAYEE_TRANSFER_MOBILE, "");
		}
		TranDataCenter.getInstance().setUserInputMap(userInputMap);
	}

	/**
	 * 显示转入账户列表详情
	 */
	private void showEcardInDetailView() {
		BaseDroidApp.getInstanse().dismissMessageDialog();
		ecardInDetailLayout = (LinearLayout) inflater.inflate(
				R.layout.tran_ecard_in_detail, null);
		ll_ecard_in.removeAllViews();
		ll_ecard_in.addView(ecardInDetailLayout);
		ll_ecard_in.setVisibility(View.VISIBLE);
		showecardInListItemData(ecardInDetailLayout);// 显示转入账户条目的详细信息

	}

	private void showecardInListItemData(View v) {
		tv_bindName = (TextView) v.findViewById(R.id.tv_bindName);
		TextView tv_bindbankName = (TextView) v
				.findViewById(R.id.tv_bindbankNmae);
		TextView tv_bind_accountNumber = (TextView) v
				.findViewById(R.id.tv_bind_accountNumber);

		String bindname = (String) ecardInforMap
				.get(Ecard.ECARD_PAYEEACCOUNTNAME_RES);
		tv_bindName.setText(bindname);
		String bindbankName = null ;
		if(StringUtil.isNullOrEmpty(ecardInforMap
				.get(Ecard.ECARD_BANKCODE_RES))){
			if(StringUtil.isNullOrEmpty(ecardInforMap
					.get(Ecard.ECARD_PAYEEBANKNAME_RES))){
				if(StringUtil.isNullOrEmpty(ecardInforMap
						.get(Ecard.ECARD_OPENINGBANKNAME_RES))){
					bindbankName = (String) ecardInforMap
							.get(Ecard.ECARD_OPENINGBANKNAME_RES);	
				}else{
					bindbankName="-";	
				}
			}else{
				bindbankName = (String) ecardInforMap
						.get(Ecard.ECARD_PAYEEBANKNAME_RES);
			}
		}else{
			bindbankName = kBankListMap.get(ecardInforMap
					.get(Ecard.ECARD_BANKCODE_RES));
		}
		
		tv_bindbankName.setText(bindbankName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				tv_bindbankName);
		String bind_accountNumber = (String) ecardInforMap
				.get(Ecard.ECARD_PAYEEACCOUNTNUMBER_RES);
		tv_bind_accountNumber.setText(StringUtil
				.getForSixForString(bind_accountNumber));
		ImageView img_update_accnickname = (ImageView) findViewById(R.id.img_acc_update_nickname);

		fl_acc_nickname = (FrameLayout) findViewById(R.id.fl_nickname);
		ll_nickname = (LinearLayout) findViewById(R.id.ll_nickname);
		btn_updatenickname = (Button) findViewById(R.id.btn_update_nickname);
		et_acc_accountnickname = (EditText) findViewById(R.id.et_acc_nickname);
		EditTextUtils.setLengthMatcher(TransferEcardActivity.this,
				et_acc_accountnickname, 20);

		img_update_accnickname.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				fl_acc_nickname.setVisibility(View.VISIBLE);
				ll_nickname.setVisibility(View.GONE);
				BaseActivity activity = (BaseActivity)BaseDroidApp.getInstanse().getCurrentAct();
				if(activity != null)
					activity.upSoftInput();
				et_acc_accountnickname
						.setText(tv_bindName.getText().toString());
				et_acc_accountnickname.setSelection(et_acc_accountnickname
						.length());
			}
		});
		btn_updatenickname.setOnClickListener(updatenicknameClick);
	}

	/** 修改账户别名监听事件 */
	OnClickListener updatenicknameClick = new OnClickListener() {
		@Override
		public void onClick(View v) {

			nickname = et_acc_accountnickname.getText().toString().trim();
			RegexpBean reb = new RegexpBean(
					TransferEcardActivity.this
							.getString(R.string.nickname_regex),
					nickname, "nickname");

			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb);
			if (RegexpUtils.regexpDate(lists)) {// 校验通过
				closeInput(et_acc_accountnickname);
				ecardInforMap.put(Ecard.ECARD_PAYEEACCOUNTNAME_RES, nickname);
				fl_acc_nickname.setVisibility(View.GONE);
				ll_nickname.setVisibility(View.VISIBLE);
				tv_bindName.setText(nickname);
			}
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
	

			switch (tranTypeFlag) {
			case TRANTYPE_NOREL_BANKOTHER:// 跨行转账
				requestGetSecurityFactor(ConstantGloble.PB032);
				break;
			case TRANTYPE_SHISHI_BANKOTHER:// 跨行实时转账
				requestGetSecurityFactor(ConstantGloble.PB113);
				break;

			default:
				break;
			}
		
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Map<String, String> userInputMap = TranDataCenter
								.getInstance().getUserInputMap();
						combineId = BaseDroidApp.getInstanse()
								.getSecurityChoosed();
						userInputMap.put(Tran._COMBINID_REQ, combineId);
						TranDataCenter.getInstance().setUserInputMap(
								userInputMap);
						switch (tranTypeFlag) {

						case TRANTYPE_NOREL_BANKOTHER:// 跨行
							requestForBocNationalTransferVerify();
							break;

						case TRANTYPE_SHISHI_BANKOTHER:// 跨行实时付款
							psnEbpsRealTimePaymentConfirm();
							break;

						default:
							break;
						}
					}
				});
	}

	/**
	 * PsnTransBocNationalTransferVerify国内跨行汇款预交易
	 */
	
	private void requestForBocNationalTransferVerify() {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		String fromAccountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String payeeActno = (String) accInInfoMap
				.get(Ecard.ECARD_PAYEEACCOUNTNUMBER_RES);
		String payeeName = (String) accInInfoMap
				.get(Ecard.ECARD_PAYEEACCOUNTNAME_RES);	
		String bankName = kBankListMap.get(accInInfoMap
				.get(Ecard.ECARD_BANKCODE_RES));
		
		accInInfoMap.put(Ecard.ECARD_PAYEEBANKNAME_RES, bankName);
		TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
		String toOrgName = (String) accInInfoMap
				.get(Ecard.ECARD_OPENINGBANKNAME_RES);
		String cnapsCode = (String) accInInfoMap
				.get(Ecard.ECARD_CNAPSCODE_RES);
		
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		payeeMobile = (String) userInputMap
				.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String payeeId = "";

		// 跨行
		biiRequestBody.setMethod(Tran.TRANSBOCNATIONALTRANSFERVERIFY);
		payeeId = (String) accInInfoMap.get(Tran.TRANS_PAYEETID_RES);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.TRANS_BOCNATIONAL_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.TRANS_BOCNATIONAL_PAYEEID_REQ, payeeId);
		map.put(Tran.TRANS_BOCNATIONAL_PAYEEACTNO_REQ, payeeActno);
		map.put(Tran.TRANS_BOCNATIONAL_PAYEENAME_REQ, payeeName);
		map.put(Tran.TRANS_BOCNATIONAL_BANKNAME_REQ, bankName);
		map.put(Tran.TRANS_BOCNATIONAL_TOORGNAME_REQ, toOrgName);
		map.put(Tran.TRANS_BOCNATIONAL_CNAPSCODE_REQ, cnapsCode);
		map.put(Tran.TRANS_BOCNATIONAL_REMITTANCEINFO_REQ, "");
		map.put(Tran.TRANS_BOCNATIONAL_AMOUNT_REQ, amount);
		map.put(Tran.TRANS_BOCNATIONAL_PAYEEMOBILE_REQ, payeeMobile);
		map.put(Tran.TRANS_BOCNATIONAL_REMARK_REQ, memo);

		map.put(Tran.TRANS_BOCNATIONAL_CURRENCY_REQ,
				ConstantGloble.PRMS_CODE_RMB);
		map.put(Tran.TRANS_BOCNATIONAL_EXECUTETYPE_REQ, ConstantGloble.NOWEXE);
		map.put(Tran.TRANS_BOCNATIONAL_EXECUTEDATE_REQ, "");
		map.put(Tran.TRANS_BOCNATIONAL__COMBINID_REQ, combineId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForBocNationalTransferVerifyCallBack");
	}

	/**
	 * 请求国内跨行预交易返回
	 * 
	 * @param resultObj
	 */
	public void requestForBocNationalTransferVerifyCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setNoRelBankOtherCallBackMap(result);

		// 请求跨行手续费试算方法
		// 跨行
		requestNationalTransferCommissionCharge(ConstantGloble.PB032);

	}

	/**
	 * 跨行手续费试算
	 */
	private void requestNationalTransferCommissionCharge(String serviceId) {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		String fromAccountId = (String) accOutInfoMap.get(Tran.ACCOUNTID_RES);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String toAccountId = "";		
		String payeeActno = (String) accInInfoMap
				.get(Ecard.ECARD_PAYEEACCOUNTNUMBER_RES);
		String payeeName = (String) accInInfoMap
				.get(Ecard.ECARD_PAYEEACCOUNTNAME_RES);
		String toOrgName = (String) accInInfoMap
				.get(Ecard.ECARD_OPENINGBANKNAME_RES);
		String cnapsCode = (String) accInInfoMap
				.get(Ecard.ECARD_CNAPSCODE_RES);
		
		
		if(tranTypeFlag==TRANTYPE_SHISHI_BANKOTHER){
			 cnapsCode = (String) accInInfoMap
					.get(Ecard.ECARD_PAYEECNAPS_RES);
		}
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(Tran.PSNTRANS_GETNATIONAL_TRANSFER_COMMISSIONCHARGE);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.SERVICEID_REQ, serviceId);
		map.put(Tran.RELTRANS_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.RELTRANS_TOACCOUNTID_REQ, toAccountId);
		map.put(Tran.RELTRANS_CURRENCY_REQ, currencyCode);
		map.put(Tran.RELTRANS_AMOUNT_REQ, amount);
		map.put(Tran.RELTRANS_CASHREMIT_REQ, cashRemitCode);
		map.put(Tran.RELTRANS_REMARK_REQ, memo);
		map.put(Tran.RELTRANS_PAYEEACTNO_REQ, payeeActno);
		map.put(Tran.RELTRANS_PAYEENAME_REQ, payeeName);
		map.put(Tran.NATIONALADDPAYEE_TOORGNAME_REQ, toOrgName);
		map.put(Tran.TRANS_CNAPSCODE_RES, cnapsCode);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestNationalTransferCommissionChargeCallBack");
	}

	public void requestNationalTransferCommissionChargeCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setCommissionChargeMap(result);
		switch (Integer.parseInt(tranMode)) {
		case 0:// 立即执行
				// 请求安全因子
			BaseHttpEngine.dissMissProgressDialog();
			Intent intent = new Intent();
			intent.setClass(this, NoRelBankOtherConfirmInfoActivity1.class);
			intent.putExtra(TRANS_TYPE, tranTypeFlag);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 国内跨行实时汇款预交易
	 */
	public void psnEbpsRealTimePaymentConfirm() {

		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		String fromAccountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String payeeActno = (String) accInInfoMap
				.get(Ecard.ECARD_PAYEEACCOUNTNUMBER_RES);
		String payeeName = (String) accInInfoMap
				.get(Ecard.ECARD_PAYEEACCOUNTNAME_RES);
//		String bankName = (String) accInInfoMap
//				.get(Ecard.ECARD_PAYEEBANKNAME_RES);
//		if(StringUtil.isNullOrEmpty(bankName)){
			String bankName=kBankListMap.get(accInInfoMap
					.get(Ecard.ECARD_BANKCODE_RES));
			accInInfoMap.put(Ecard.ECARD_PAYEEBANKNAME_RES, bankName);
			TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
//		}
//		String toOrgName = (String) accInInfoMap
//				.get(Ecard.ECARD_PAYEEBANKNAME_RES);
		String cnapsCode = (String) accInInfoMap
				.get(Ecard.ECARD_PAYEECNAPS_RES);

		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		// 跨行
		biiRequestBody.setMethod(Tran.PSNEBPSREALTIMEPAYMENTCONFIRM_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		if (isSendSmc.equals(ConstantGloble.FALSE)) {
			payeeMobile = "";
		} else {
			map.put(Tran.EBPSREAL_SENDMSGFLAG_REQ, "1");
		}
		map.put(Tran.TRANS_BOCNATIONAL_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.EBPSQUERY_PAYEEACTNO2_REQ, payeeActno);
		map.put(Tran.EBPSQUERY_PAYEEACTNO_REQ, payeeActno);
		map.put(Tran.SAVEEBPS_PAYEENAME_REQ, payeeName);
		map.put(Tran.EBPSQUERY_PAYEEBANKNAME_REQ, bankName);
		map.put(Tran.EBPSQUERY_PAYEEORGNAME_REQ, bankName);

		map.put(Tran.EBPSQUERY_PAYEECNAPS_REQ, cnapsCode);

		map.put(Tran.TRANS_BOCNATIONAL_REMITTANCEINFO_REQ, "");
		map.put(Tran.TRANS_BOCNATIONAL_AMOUNT_REQ, amount);
		map.put(Tran.TRANS_BOCNATIONAL_PAYEEMOBILE_REQ, payeeMobile);
		map.put(Tran.EBPSREAL_MEMO_REQ, memo);

		map.put(Tran.TRANS_BOCNATIONAL_CURRENCY_REQ,
				ConstantGloble.PRMS_CODE_RMB);
		map.put(Tran.TRANS_BOCNATIONAL_EXECUTETYPE_REQ, ConstantGloble.NOWEXE);
		map.put(Tran.TRANS_BOCNATIONAL_EXECUTEDATE_REQ, "");
		map.put(Tran.TRANS_BOCNATIONAL__COMBINID_REQ, combineId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"psnEbpsRealTimePaymentConfirmCallBack");
	}

	/**
	 * 请求国内实时跨行预交易返回
	 * 
	 * @param resultObj
	 */
	public void psnEbpsRealTimePaymentConfirmCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setNoRelBankOtherCallBackMap(result);

			requestNationalTransferCommissionCharge(ConstantGloble.PB113);

	}



	/**
	 * 显示转出账户列表详情
	 */
	private void showEcardOutDetailView() {
		BaseDroidApp.getInstanse().dismissMessageDialog();
		ecardOutDetailLayout = (LinearLayout) inflater.inflate(
				R.layout.tran_ecard_out_detail, null);

		ll_ecard_out.removeAllViews();
		ll_ecard_out.addView(ecardOutDetailLayout);
		ll_ecard_out.setVisibility(View.VISIBLE);

		showecardOutListItemData(ecardOutDetailLayout);// 显示转出账户条目的详细信息
		// 判断是否是其他模块进来的
		Intent intent = getIntent();
		if (intent != null) {
			otherPartJumpFlag = intent.getIntExtra(
					ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
			if (StringUtil.isNullOrEmpty(otherPartJumpFlag)) {
				return;
			}
			mTopRightBtn.setVisibility(View.GONE);
			

		}

	}

	private void showecardOutListItemData(View v) {

		TextView tv_nickName = (TextView) v.findViewById(R.id.tv_nickName);
		TextView tv_accountType = (TextView) v
				.findViewById(R.id.tv_accountType);
		TextView tv_accountNumber = (TextView) v
				.findViewById(R.id.tv_accountNumber);
		TextView tv_currey = (TextView) v.findViewById(R.id.tv_currey);
		TextView tv_cash = (TextView) v.findViewById(R.id.tv_cash);

		Map<String, Object> resultMap = TranDataCenter.getInstance()
				.getCurrOutDetail();

		List<Map<String, String>> detialList = (List<Map<String, String>>) resultMap
				.get(ConstantGloble.ACC_DETAILIST);
		if (StringUtil.isNullOrEmpty(detialList)) {
			return;
		}
		currencyCode = null;
		for (int i = 0; i < detialList.size(); i++) {
			Map<String, String> map = detialList.get(i);
			String currency = map.get(Comm.CURRENCYCODE);

			if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {
				currencyCode = currency;
				availableBalance = map.get(Dept.AVAILABLE_BALANCE);
			}

		}

		if (StringUtil.isNullOrEmpty(currencyCode)) {
			currencyCode = (String) detialList.get(0).get(Comm.CURRENCYCODE);
			// 可用余额
			availableBalance = detialList.get(0).get(Dept.AVAILABLE_BALANCE);
		}

		tv_nickName.setText((String) ecardOutforMap.get(Comm.NICKNAME));
		String accountType = (String) ecardOutforMap.get(Comm.ACCOUNT_TYPE);
		String strAccountT = LocalData.AccountType.get(accountType);
		tv_accountType.setText(strAccountT);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				tv_accountType);
		String accoutNumber = (String) ecardOutforMap.get(Comm.ACCOUNTNUMBER);
		tv_accountNumber.setText(StringUtil.getForSixForString(accoutNumber));
		// String currencyCode=(String) ecardOutforMap.get(Comm.CURRENCYCODE);
		// tv_currey.setText(LocalData.Currency.get(currencyCode));
		tv_currey.setText(LocalData.Currency.get(currencyCode));
		tv_cash.setText(StringUtil.parseStringPattern(availableBalance, 2));
	}

	/**
	 * 没有转出账户情况
	 */
	private OnClickListener onNoOutclicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_CANCLE:// 取消
				BaseDroidApp.getInstanse().dismissErrorDialog();
				break;
			case CustomDialog.TAG_SURE:// 确定
				BaseDroidApp.getInstanse().dismissErrorDialog();

				break;
			default:
				break;
			}

		}
	};

	

	/**
	 * 转账银行开户行查询
	 */
	public void requestQueryExternalBank(int currentIndex, int pageSize) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.QUERYEXTERNALBANKINFO);
		Map<String, String> map = new HashMap<String, String>();

		map.put(Tran.PAYEE_TOBANKCODE_REQ,
				(String) ecardInforMap.get(Ecard.ECARD_PAYEECNAPS_RES));

		// 银行名称
		map.put(Tran.PAYEE_BANKNAME_REQ, "");
		// 当前页
		map.put(Tran.PAYEE_CURRENTINDEX_REQ, currentIndex + "");
		// 每页显示条数
		map.put(Tran.PAYEE_PAGESIZE_REQ, pageSize + "");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"queryExternalBankCallBack");
	}

	/**
	 * 转账银行开户行查询
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 * @return tokenId
	 */
	@SuppressWarnings("unchecked")
	public void queryExternalBankCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		String recordNumber = (String) result.get(Tran.RECORD_NUMBER);

		// if(Integer.parseInt(recordNumber)<=0){
		// BaseDroidApp.getInstanse().showInfoMessageDialog("shibai");
		// return;
		// }
		List<Map<String, String>> queryExternalBankList = (List<Map<String, String>>) result
				.get(ConstantGloble.LIST);
		TranDataCenter.getInstance().setExternalBankList(queryExternalBankList);
		// 跳转到下个列表页面
		Intent intent = new Intent(this, QueryExternalBankActivity1.class);
		intent.putExtra(Tran.RECORD_NUMBER, recordNumber);
		startActivityForResult(intent, ConstantGloble.PAYEE_OTHERBANK);
	}

	@Override
//	public void PsnOtherBankQueryForTransToAccountCallBack(Object resultObj) {
//		super.PsnOtherBankQueryForTransToAccountCallBack(resultObj);
//
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		Map<String, Object> result = (Map<String, Object>) biiResponseBody
//				.getResult();
//		String recordNumber = (String) result
//				.get(Tran.EBPSQUERY_RECORDNUMBER_RES);
//
//		List<Map<String, String>> queryExternalBankList = (List<Map<String, String>>) result
//				.get(Tran.EBPSQUERY_ACCOUNTOFBANKLIST_RES);
//		for (int i = 0; i < queryExternalBankList.size(); i++) {
//			queryExternalBankList.get(i).put("flag", "Y");
//		}
//		TranDataCenter.getInstance().setShishiBankList(queryExternalBankList);
//		BaseHttpEngine.dissMissProgressDialog();
//
//		String bankCode = (String) ecardInforMap
//				.get(Ecard.ECARD_BANKCODE_RES);
//
//		if (filledData(queryExternalBankList, bankCode)) {
//			// 可以切
//
//		} else {
//			ll_sbank.setVisibility(View.VISIBLE);
//
//		}
//
//	}

//	private Boolean filledData(List<Map<String, String>> date, String bankCode) {
//		for (int i = 0; i < date.size(); i++) {
//			Map<String, String> dataMap = date.get(i);
//			String bankbtp = dataMap.get(Tran.EBPSQUERY_BANKBTP_RES);
//			if (bankCode.equals(bankbtp)) {
//				return true;
//			}
//
//		}
//		return false;
//
//	}

//	public void PsnOtherBankQueryForTransToAccountCallBack2(Object resultObj) {
//		super.PsnOtherBankQueryForTransToAccountCallBack2(resultObj);
//
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		Map<String, Object> result = (Map<String, Object>) biiResponseBody
//				.getResult();
//		List<Map<String, String>> queryExternalBankList = (List<Map<String, String>>) result
//				.get(Tran.EBPSQUERY_ACCOUNTOFBANKLIST_RES);
//		List<Map<String, String>> ShishiBankList = TranDataCenter.getInstance()
//				.getShishiBankList();
//		ShishiBankList.addAll(queryExternalBankList);
//		TranDataCenter.getInstance().setShishiBankList(ShishiBankList);
//		BaseHttpEngine.dissMissProgressDialog();
//		// 跳转到下个列表页面
//		Intent intent = new Intent(TransferEcardActivity.this,
//				OrderMainActivity.class);
//		intent.putExtra(ISSHISHITYPE, true);
//		startActivityForResult(intent, ConstantGloble.CHOOSE_SHISHIBANK_NEW);
//	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ConstantGloble.PAYEE_OTHERBANK
				&& resultCode == RESULT_OK) {
			// 开户行查询返回
			String toOrgName = data
					.getStringExtra(Tran.TRANS_BOCNATIONAL_TOORGNAME_REQ);
			String orgNameCnapsCode = data
					.getStringExtra(Tran.PAYEE_CNAPSCODE_REQ);
//			if (StringUtil.isNull(LocalData.kBankListMap.get(toOrgName))) {
//				// 不是常用银行 全部设置为其他银行
//				ecardInforMap.put(Ecard.ECARD_OPENINGBANKNAME_RES, "其它银行");
//				ecardInforMap.put(Ecard.ECARD_CNAPSCODE_RES, "OTHER");
//			} else {
				ecardInforMap.put(Ecard.ECARD_OPENINGBANKNAME_RES, toOrgName);
				ecardInforMap.put(Ecard.ECARD_CNAPSCODE_RES, orgNameCnapsCode);
//			}

			TranDataCenter.getInstance().setAccInInfoMap(ecardInforMap);
			tv_kbankname.setText(toOrgName);
		} else if (requestCode == ConstantGloble.CHOOSE_SHISHIBANK_NEW) {
			if (resultCode == RESULT_OK) {

				// 要修改 sunh
				// 所属银行查询返回
				String toOrgName = data
						.getStringExtra(Tran.TRANS_BOCNATIONAL_TOORGNAME_REQ);
				String orgNameCnapsCode = data
						.getStringExtra(Tran.PAYEE_CNAPSCODE_REQ);
				tv_sbankname.setVisibility(View.VISIBLE);
				tv_sbankname.setText(toOrgName);
				ecardInforMap.put(Tran.EBPSQUERY_PAYEEBANKNAME_REQ, toOrgName);
				ecardInforMap.put(Ecard.ECARD_CNAPSCODE_RES, orgNameCnapsCode);
				TranDataCenter.getInstance().setAccInInfoMap(ecardInforMap);
			}
		}

	}

	private TextWatcher textWatcherTran = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {

			if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
				String sum = et_ecard_count.getText().toString();
				if (!StringUtil.isNull(sum)) {
					double dmoney = Double.parseDouble(sum);
					if (dmoney > 50000) {
						BaseDroidApp.getInstanse().showMessageDialog(
								"转账金额不可大于5万元 ", new OnClickListener() {
									@Override
									public void onClick(View v) {
										et_ecard_count.getText().clear();
										BaseDroidApp.getInstanse()
												.dismissMessageDialog();
									}
								});
					}
				}

			}
		}
	};
	
	public static Map<String, String> kBankListMap = new HashMap<String, String>() {
	private static final long serialVersionUID = 1L;

	{
		put("104", "中国银行");
		put("102", "中国工商银行");
		put("105", "中国建设银行");
		put("103", "中国农业银行");		
		put("308", "招商银行");
		put("201", "国家开发银行");
		put("202", "中国进出口银行");
		put("203", "中国农业发展银行");
		put("301", "交通银行");
		put("302", "中信银行");
		put("303", "中国光大银行");
		put("304", "华夏银行");
		put("305", "中国民生银行");
		put("306", "广发银行股份有限公司");
		put("307", "平安银行");
		put("309", "兴业银行");
		put("310", "上海浦东发展银行");
		put("403", "中国邮政储蓄银行");
//		put("PBC", "中国人民银行");
		put("OTHER", "其它银行");

	}
};
}

