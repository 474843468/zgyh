package com.chinamworld.bocmbci.biz.remittance.applicationForRemittance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.remittance.RemittanceBaseActivity;
import com.chinamworld.bocmbci.biz.remittance.RemittanceContent;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.remittance.RemittanceMustKnowActivity;
import com.chinamworld.bocmbci.biz.remittance.dialog.NationalTransferLimitDialogActivity;
import com.chinamworld.bocmbci.biz.remittance.dialog.UseModelDialogActivity;
import com.chinamworld.bocmbci.biz.remittance.fragment.OverseasPayeeInfoFragment;
import com.chinamworld.bocmbci.biz.remittance.fragment.OverseasRemitterInfoFragment;
import com.chinamworld.bocmbci.biz.remittance.fragment.OverseasTransationInfoFragment;
import com.chinamworld.bocmbci.biz.remittance.interfaces.AccChangeListener;
import com.chinamworld.bocmbci.biz.remittance.interfaces.AccDetailListnenr;
import com.chinamworld.bocmbci.biz.remittance.interfaces.AccListCallBackInterface;
import com.chinamworld.bocmbci.biz.remittance.interfaces.ChooseCountryListener;
import com.chinamworld.bocmbci.biz.remittance.interfaces.NeedAccDetailListener;
import com.chinamworld.bocmbci.biz.remittance.interfaces.OnAreaSelectListener;
import com.chinamworld.bocmbci.biz.remittance.interfaces.OnIbanFormatCheckListener;
import com.chinamworld.bocmbci.biz.remittance.overseaschinabank.OverseasChinaBankCollectionBank;
import com.chinamworld.bocmbci.biz.remittance.overseaschinabank.OverseasChinaBankPayBankCountry;
import com.chinamworld.bocmbci.boc.ModelBoc;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 汇款信息填写页面
 * 
 * @author Zhi
 */
public class OverseasChinaBankRemittanceInfoInputActivity extends
		RemittanceBaseActivity implements NeedAccDetailListener {

	// TODO -------------------成员变量------------------------------//

	public static final String ACTION_COLLECTION_BANK = "com.chinamworld.bocmbci.biz.remittance.overseaschinabank.OverseasChinaBankCollectionBank";

	private static final String TAG = "OverseasChinaBankRemittanceInfoInputActivity";
	/** 汇款人信息输入页面片段 */
	private OverseasRemitterInfoFragment over_remitterFrag;
	/** 收款人信息输入页面片段 */
	private OverseasPayeeInfoFragment over_payeeFrag;
	/** 交易信息输入页面片段 */
	private OverseasTransationInfoFragment over_transationFrag;
	/** 多个片段可能需要调用账户详情接口，片段调用详情接口前先把自己注册在这里，详情返回后本Activity会自动通知 */
	private AccDetailListnenr needDetailView;
	/** 多个片段可能需要使用账户列表接口数据，本Activity在请求账户列表时将需要通知的类注册在这里 */
	private AccListCallBackInterface needAccListView;
	/** 用户选择国家监听器 */
	private ChooseCountryListener chooseCountryListener;
	/** ibank账号合法性校验监听者 */
	private OnIbanFormatCheckListener onIbanFormatCheckListener;
	/** 查询剩余额度的用途标识 0-查询剩余额度 1-校验金额是否超过额度 */
	private int limitFlag = 0;

	// TODO -------------------成员方法------------------------------//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLeftSelectedPosition("crossBorderRemit_4");
		setTitle(this.getString(R.string.remittance_apply_overseas_chian_bank));
		addView(R.layout.remittance_info_input);
		initView();

		findViewById(R.id.ib_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//ModelBoc.gotoMainActivity(OverseasChinaBankRemittanceInfoInputActivity.this);
				//goToMainActivity();
				finish();
			}
		});
	}

	private void initView() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if (over_remitterFrag == null) {
			over_remitterFrag = new OverseasRemitterInfoFragment();
		}
		if (over_payeeFrag == null) {
			over_payeeFrag = new OverseasPayeeInfoFragment();
		}
		if (over_transationFrag == null) {
			over_transationFrag = new OverseasTransationInfoFragment();
			over_transationFrag.setNeedDetailListener(this);
			over_remitterFrag.setAccChangeView(new AccChangeListener[] { over_transationFrag });
			over_transationFrag.setAccSelectionListener(over_remitterFrag);
		}
		boolean state = getIntent().getBooleanExtra(RemittanceContent.JUMPFLAG, false);
		if (state) {

			over_remitterFrag.setState(1, false, false);
			over_payeeFrag.setState(1, false);
			over_transationFrag.setState(1, false);
		} else {
			over_remitterFrag.setState(0, false, true);
			over_payeeFrag.setState(0, false);
			over_transationFrag.setState(0, false);
		}
		ft.replace(R.id.ll_input1, over_remitterFrag);
		ft.replace(R.id.ll_input2, over_payeeFrag);
		ft.replace(R.id.ll_input3, over_transationFrag);
		ft.commit();

		over_payeeFrag.setOnAreaSelectListener(new OnAreaSelectListener() {
			@Override
			public void onAreaSelect(int position) {
				over_transationFrag.onAreaSelect(position);
			}
		});

		/** 下一步按钮监听 */
		findViewById(R.id.btnNext).setOnClickListener(nextListener);

		boolean isOhter = getIntent().getBooleanExtra("isOhter",false);
		if (isOhter) {

			needAccListView = over_remitterFrag;
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestCommConversationId();

		} else {
			if (getIntent().getBooleanExtra("ISSHOWMUSTKNOW", true)) {
				startActivityForResult(new Intent(this, RemittanceMustKnowActivity.class), 0);
				overridePendingTransition(R.anim.no_animation, R.anim.no_animation);
			} else {
				onActivityResult(0, RemittanceContent.RESULT_CODE_MUSTKNOW_RESULT, null);
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RemittanceContent.CHOOSE_CHOUNTRY) {
			chooseCountryListener.chooseCountry(data.getStringExtra(Remittance.COUNTRYCODE));
		} else if (resultCode == RemittanceContent.RESULT_CODE_MUSTKNOW_RESULT) {
			needAccListView = over_remitterFrag;
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestCommConversationId();
		} else if (resultCode == RemittanceContent.CHOOSE_MODE) {

			 over_remitterFrag.setState(1, true, false);
			 over_payeeFrag.setState(1, true);
			 over_transationFrag.setState(1, false);
			 Log.i("100",data.getStringExtra("bocPayeeBankRegionCN")+"===activity===");
		} else if (resultCode == RemittanceContent.QUERY_SWIFT) {
			over_payeeFrag.onActivityResult(requestCode, resultCode, data);
		} else if (resultCode == RemittanceContent.RESULT_CODE_COLLECTION_BANK_RESULT) {

			int position = data.getIntExtra("position", 0);
			String sear = data.getStringExtra("sear");
			Map<String, Object> map = RemittanceDataCenter.getInstance()
					.getlistBySear().get(position);
			over_payeeFrag.setBankName(sear,(String) map.get("bocPayeeBankNameEN"),(String) map.get("bocPayeeBankSwift"),(String) map.get("region"));
			over_transationFrag.setspBiZhong(map);

		} else if(resultCode == RemittanceContent.RESULT_CODE_PAY_BANK_AREA) {

			String sear = data.getStringExtra("sear");
			Map<String, Object> map = RemittanceDataCenter.getInstance()
					.getlistBySear().get(0);
			over_payeeFrag.setBankName(sear,(String) map.get("bocPayeeBankNameEN"),(String) map.get("bocPayeeBankSwift"),(String) map.get("region"));
			over_transationFrag.setspBiZhong(map);

		} else if (resultCode == QUIT_CODE) {
			finish();
		}
	}

	@Override
	public boolean doHttpErrorHandler(String method, BiiError biiError) {
		super.doHttpErrorHandler(method, biiError);
		String errorCode = biiError.getCode();
		if ("CCSS.S0012".equals(errorCode)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("您尚未保存模板！");
		} else if ("safetyComBin.nosSafetyComins".equals(errorCode)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					"您所持认证工具不支持此交易，如您需要使用此交易，请前往柜台申请中银e令或中银e盾");
		} else if ("AccQueryDetailAction.NoSubAccount".equals(errorCode)) {
			BaseDroidApp.getInstanse().showMessageDialog("您的账户无存单信息",
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							//over_transationFrag.initViewZero();
							over_remitterFrag.spPayAcctSetZero();
							over_transationFrag.initViewZeroNoSp();
						}
					});
		}
		return true;
	}

	/** 从CCSS查询收款行SWIFT代码回调后续操作 */
	private void swiftWrongButContinue() {
		String accNumber = (String) RemittanceDataCenter.getInstance().getSubmitParams().get(Remittance.PAYEEACTNO);
		if (Character.isLetter(accNumber.charAt(0)) && Character.isLetter(accNumber.charAt(1))) {
			ibanFormatCheck();
		} else {
			checkAUOrCA();
			// queryTransferLimit();
		}
	}
	/** 设置limitFlag标识位 */
	public void setLimitFlag(int limitFlag) {
		this.limitFlag = limitFlag;
	}

	/** 跨境汇款额度查询 */
	private void queryTransferLimit() {
		limitFlag = 1;
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> userInput = RemittanceDataCenter.getInstance().getSubmitParams();
		params.put(Remittance.SWIFTACCOUNTID, userInput.get(Remittance.SWIFTACCOUNTID));
		params.put(Acc.QUERYTRANSFER_ACC_CURRENCY_REQ, userInput.get(Remittance.REMITCURRENCYCODE));
		getHttpTools().requestHttp(Remittance.PSNQUERYNATIONALTRANSFERLIMIT, "requestPsnQueryNationalTransferLimitCallBack", params, false);
	}

	/** ibank账号合法性校验 */
	private void ibanFormatCheck() {
		// 设置ibank合法性校验结果监听器
		setOnIbanFormatCheckListener(new OnIbanFormatCheckListener() {

			@Override
			public void onIbanFormatCheck(boolean isPass) {
				if (isPass) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put(
							Remittance.PAYEEACTNO,
							RemittanceDataCenter.getInstance()
									.getSubmitParams()
									.get(Remittance.PAYEEACTNO));
					params.put(
							Remittance.PAYEEBANKSWIFT,
							RemittanceDataCenter.getInstance()
									.getSubmitParams()
									.get(Remittance.PAYEEBANKSWIFT));
					getHttpTools().requestHttp(Remittance.PSNIBANBICMATCHCHECK,
							"requestPsnIbanBicMatchCheckCallBack", params,
							false);
				} else {
					BaseHttpEngine.dissMissProgressDialog();
					return;
				}
			}
		});
		// 请求ibank合法性校验
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Remittance.PAYEEACTNO, RemittanceDataCenter.getInstance()
				.getSubmitParams().get(Remittance.PAYEEACTNO));
		getHttpTools().requestHttp(Remittance.PSNIBANFORMATCHECK,
				"requestPsnIbanFormatCheckCallBack", params, false);
	}

	/** 判断收款人地区是否是AU或CA */
	private void checkAUOrCA() {

//		String gatheringArea = (String) RemittanceDataCenter.getInstance().getSubmitParams().get(Remittance.GATHERINGAREA);
//		if (gatheringArea.equals("澳大利亚") || gatheringArea.equals("加拿大")) {
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put(Remittance.PAYEEBANKNUM, RemittanceDataCenter.getInstance().getSubmitParams().get(Remittance.PAYEEBANKNUM));
//			getHttpTools().requestHttp(Remittance.PSNPAYEEBANKBSBCHECK, "requestPsnPayeeBankBSBCheckCallBack", params, false);
//		} else {
//			queryTransferLimit();
//		}

		queryTransferLimit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			RemittanceDataCenter.getInstance().clearAllData();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	// TODO -------------------接口、事件------------------------------//

	/** 下一步事件 */
	private OnClickListener nextListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 检查提交交易时需要上送的参数列表，首次使用初始化
			RemittanceDataCenter.getInstance().setSubmitParams(new HashMap<String, Object>());
			RemittanceDataCenter.getInstance().setUserInput(new HashMap<String, Object>());
			if (!over_remitterFrag.remittanceConfirm()) {
				return;
			}
			if (!over_payeeFrag.remittanceConfirm()) {
				return;
			}

			if (!over_transationFrag.remittanceConfirm()) {
				return;
			}
			BaseHttpEngine.showProgressDialog();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Remittance.PAYEEBANKSWIFT, RemittanceDataCenter.getInstance().getSubmitParams().get(Remittance.PAYEEBANKSWIFT));
			getHttpTools().requestHttp(Remittance.PSNPAYEEBANKSWIFTQUERY, "requestPsnPayeeBankSwiftQueryCallBack", params, false);
		}
	};

	public void sendInsertTranSeqCallback(Object resultObj) {
		BaseDroidApp.getInstanse().showInfoMessageDialog(resultObj.toString());
	}

	/** 注册需要详情数据的片段 */
	@Override
	public void setNeedDetailView(AccDetailListnenr needDetailView) {
		this.needDetailView = needDetailView;
	}

	public void setChooseCountryListener(ChooseCountryListener listener) {
		chooseCountryListener = listener;
	}

	public void setChooseAreaListener(OnAreaSelectListener listener) {
		over_payeeFrag.setOnAreaSelectListener(listener);
	}

	public void setOnIbanFormatCheckListener(
			OnIbanFormatCheckListener onIbanFormatCheckListener) {
		this.onIbanFormatCheckListener = onIbanFormatCheckListener;
	}

	// TODO -------------------网络请求与回调方法------------------------------//

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		/** 发送安全因子请求 */
		requestSecurityFactor();
		getHttpTools().registErrorCode(Acc.ACC_GETSECURITYFACTOR_API,
				"safetyComBin.nosSafetyComins");
	}

	/**
	 * 请求安全因子
	 */
	private void requestSecurityFactor() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_GETSECURITYFACTOR_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Acc.ACC_GETSECURITY_SERVICEID_REQ, "PB047");
		isHoldUpGetSecurityFactorNull = true;
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestGetSecurityFactorCallBack");
	}

	/** 查询扣款账户返回 */
	@SuppressWarnings("unchecked")
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(
			Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		getHttpTools();
		List<Map<String, Object>> accList = (List<Map<String, Object>>) HttpTools
				.getResponseResult(resultObj);
		List<Map<String, Object>> haveAccList = new ArrayList<Map<String, Object>>();
		if (StringUtil.isNullOrEmpty(accList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					"您当前没有可以办理跨境汇款服务的账户");
			return;
		}
		String accountType = null;
		for (int i = 0; i < accList.size(); i++) {
			accountType = (String) accList.get(i).get(Comm.ACCOUNT_TYPE);
			if (accountType.equals("119") || accountType.equals("188")) {
				haveAccList.add(accList.get(i));
			} else {
				continue;
			}
		}

		if (haveAccList.size() > 0) {
			RemittanceDataCenter.getInstance().setAccList(haveAccList);
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					"因您名下的关联账户中无可用的活一本或主账户是活一本的借记卡，故无法使用该功能服务");
			return;
		}
		LogGloble.i(TAG, RemittanceDataCenter.getInstance().getAccList()
				.toString());
		needAccListView.accListCallBack();
	}

	/** 查询账户详情回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnAccountQueryAccountDetailCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> accountDetailList = (List<Map<String, Object>>) (resultMap
				.get(ConstantGloble.ACC_DETAILIST));
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(accountDetailList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("无可用的汇款货币");
			return;
		}
		RemittanceDataCenter.getInstance().setAccDetail(accountDetailList);
		if (needDetailView != null) {
			needDetailView.detailCallBack(accountDetailList);
		}
	}

	/** 查询客户其他姓名回调 */
	public void requestPsnOtherNameOfCustQueryCallBack(Object resultObj) {
		getHttpTools();
		String name = (String) HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		over_remitterFrag.setRemitterName(name);
	}

	/** 从CCSS查询收款行SWIFT代码回调 */
	public void requestPsnPayeeBankSwiftQueryCallBack(Object resultObj) {
		getHttpTools();
		String resultStr = (String) HttpTools.getResponseResult(resultObj);
		if (resultStr.equals("00")) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showErrorDialog("为确保您的款项顺利汇出，请确认您填写的收款银行SWIFT代码是否正确。", R.string.remittance_btn_jixutijiao, R.string.remittance_btn_backCheck, new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (Integer.parseInt(v.getTag() + "")) {
						case CustomDialog.TAG_SURE:
							BaseDroidApp.getInstanse().dismissErrorDialog();
							break;

						case CustomDialog.TAG_CANCLE:
							BaseHttpEngine.showProgressDialog();
							swiftWrongButContinue();
							BaseDroidApp.getInstanse().dismissErrorDialog();
							break;
					}

				}
			});
		} else {
			swiftWrongButContinue();
		}
	}

	/** 费用试算返回 */
	@SuppressWarnings("unchecked")
	public void requestPsnTransGetInternationalTransferCommissionChargeCallBack(Object resultObj) {
		getHttpTools();
		Map<String, Object> resultMap = (Map<String, Object>) HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}

		RemittanceDataCenter.getInstance().setMapPsnTransGetInternationalTransferCommissionCharge(resultMap);

		LogGloble.i(TAG, BaseDroidApp.getInstanse().getSecurityIdList().toString());
		if (BaseDroidApp.getInstanse().getSecurityIdList().size() != 1) {
			BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Map<String, Object> params = RemittanceDataCenter.getInstance().getSubmitParams();
					params.put(Safety.COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
					getHttpTools().requestHttp(Remittance.PSNTRANSINTERNATIONALTRANSFERCONFIRM, "requestPsnTransInternationalTransferConfirmCallBack", params, true);
				}
			});
		} else {
			Map<String, Object> params = RemittanceDataCenter.getInstance().getSubmitParams();
			params.put(Safety.COMBINID, BaseDroidApp.getInstanse().getSecurityIdList().get(0));
			getHttpTools().requestHttp(Remittance.PSNTRANSINTERNATIONALTRANSFERCONFIRM, "requestPsnTransInternationalTransferConfirmCallBack", params, true);
		}
	}

	/** 联机校验收款行行号 */
	public void requestPsnPayeeBankBSBCheckCallBack(Object resultObj) {
		getHttpTools();
		String resultStr = (String) HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNull(resultStr)) {
			return;
		}
		if (resultStr.equals("01") || resultStr.equals("02")
				|| resultStr.equals("03")) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showErrorDialog(
					"为确保您的款项顺利汇出，请确认您填写的收款行行号是否正确。",
					R.string.remittance_btn_jixutijiao,
					R.string.remittance_btn_backCheck, new OnClickListener() {

						@Override
						public void onClick(View v) {
							switch (Integer.parseInt(v.getTag() + "")) {
							case CustomDialog.TAG_SURE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;

							case CustomDialog.TAG_CANCLE:
								BaseHttpEngine.showProgressDialog();
								queryTransferLimit();
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;
							}

						}
					});
		} else {
			queryTransferLimit();
		}
	}

	/** iban账号校验返回 */
	@SuppressWarnings("unchecked")
	public void requestPsnIbanFormatCheckCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		getHttpTools();
		Map<String, Object> resultMap = (Map<String, Object>) HttpTools
				.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		RemittanceDataCenter.getInstance().setMapPsnIbanFormatCheck(resultMap);
		if (onIbanFormatCheckListener != null) {
			String checkResult = (String) resultMap.get(Remittance.CHECKRESULT);
			if (checkResult.equals("1") || checkResult.equals("2")
					|| checkResult.equals("3")) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						"您输入的IBAN账号不正确");
				onIbanFormatCheckListener.onIbanFormatCheck(false);
			} else {
				onIbanFormatCheckListener.onIbanFormatCheck(true);
			}
		}
	}

	/** IBAN账号与BIC CODE一致性校验返回 */
	public void requestPsnIbanBicMatchCheckCallBack(Object resultObj) {
		getHttpTools();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		String resultStr = (String) resultMap.get(Remittance.CHECKRESULT);
		BaseHttpEngine.dissMissProgressDialog();
		if (resultStr.equals("3")) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog("您输入的IBAN账号不正确");
			return;
		} else if (resultStr.equals("2")) {
			BaseDroidApp.getInstanse().showErrorDialog(
					getResources().getString(
							R.string.remittance_IbanSwiftBicMatchCheck),
					R.string.remittance_btn_jixutijiao,
					R.string.remittance_btn_backCheck, new OnClickListener() {
						@Override
						public void onClick(View v) {
							switch (Integer.parseInt(v.getTag() + "")) {
							case CustomDialog.TAG_SURE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;
							case CustomDialog.TAG_CANCLE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								BaseHttpEngine.showProgressDialog();
								checkAUOrCA();
								break;
							}
							return;
						}
					});
		} else {
			checkAUOrCA();
		}
	}

	/**
	 * 是否包含USB组合
	 * 
	 * @param tokenId
	 * @return
	 */
	private boolean isContainUsb(String tokenId) {
		if (Acc.ECURITY_8.equals(tokenId) || Acc.ECURITY_32.equals(tokenId)
				|| Acc.ECURITY_40.equals(tokenId)
				|| Acc.ECURITY_4.equals(tokenId)
				|| Acc.ECURITY_12.equals(tokenId)
				|| Acc.ECURITY_36.equals(tokenId)
				|| Acc.ECURITY_96.equals(tokenId))
			return false;
		return true;
	}

	public void doSecurityFactorNull() {
		super.doSecurityFactorNull();
		BaseDroidApp.getInstanse().showMessageDialog(
				"您所持认证工具不支持此交易，如您需要使用此交易，请前往柜台申请中银e令或中银e盾。",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						BaseDroidApp.getInstanse().dismissErrorDialog();
						finish();
					}
				});
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		BaseDroidApp.getInstanse().setSecurityIdList(new ArrayList<String>());
		BaseDroidApp.getInstanse().setSecurityNameList(new ArrayList<String>());
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// combinId = (String) biiResponseBody.getResult();
		if (biiResponseBody.getResult() == null
				|| "".equals(biiResponseBody.getResult())) {
			doSecurityFactorNull();
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		// 默认因子
		@SuppressWarnings("unchecked")
		Map<String, String> defaultCombin = (Map<String, String>) result
				.get(Acc.ACC_GETSECURITY_DEFAULTCOMBIN_RES);
		BaseDroidApp.getInstanse().setDefaultCombinId(String.valueOf(-1));
		if (defaultCombin != null
				&& !isContainUsb(defaultCombin.get(Acc.ECURITYID))) {// 不包含USB组合
			BaseDroidApp.getInstanse().setDefaultCombinId(
					defaultCombin.get(Acc.ECURITYID));
		}
		@SuppressWarnings("unchecked")
		List<Map<String, String>> combinList = (List<Map<String, String>>) result
				.get(Acc.ACC_GETSECURITY_COMBINLIST_RES);
		int length = combinList.size();
		String[] securityNames = new String[length + 5];
		String[] securityIds = new String[length + 5];
		int index = 0;
		if (combinList != null) {
			if (length == 1 && combinList.get(0).get(Acc.ECURITYID) == "96") {
				BaseDroidApp.getInstanse().showMessageDialog(
						"您所持认证工具不支持此交易，如您需要使用此交易，请前往柜台申请中银e令或中银e盾。",
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								RemittanceDataCenter.getInstance()
										.clearAllData();
								finish();
							}
						});
				return;
			} else {
				for (int i = 0; i < length; i++) {
					if (!isContainUsb(combinList.get(i).get(Acc.ECURITYID))) {// 不是USB组合就添加
						if (!BaseDroidApp.getInstanse().getSecurityIdList()
								.contains(combinList.get(i).get(Acc.ECURITYID))) {
							String securityId = combinList.get(i).get(
									Acc.ECURITYID);
							String securityName = combinList.get(i).get(
									Acc.TOKENNAME);
							if ("32".equals(securityId)) {
								securityIds[0] = securityId;
								securityNames[0] = securityName;
							} else if ("96".equals(securityId)) {
								continue;
							} else if ("8".equals(securityId)) {
								securityIds[1] = securityId;
								securityNames[1] = securityName;
							} else if ("40".equals(securityId)) {
								securityIds[2] = securityId;
								securityNames[2] = securityName;
							} else if ("4".equals(securityId)) {
								securityIds[3] = securityId;
								securityNames[3] = "中银e盾";
							} else {
								securityIds[4 + index] = securityId;
								securityNames[4 + index] = securityName;
								index++;
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < securityIds.length; i++) {
			if (securityIds[i] != null) {
				BaseDroidApp.getInstanse().getSecurityIdList()
						.add(securityIds[i]);
				BaseDroidApp.getInstanse().getSecurityNameList()
						.add(securityNames[i]);
			}
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.ACCOUNT_TYPE, RemittanceDataCenter.payAcc);
		getHttpTools().requestHttp(Comm.QRY_ALL_BANK_ACCOUNT,
				"requestPsnCommonQueryAllChinaBankAccountCallBack", params,
				false);
	}

	/** 跨境汇款预交易回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnTransInternationalTransferConfirmCallBack(Object resultObj) {
		getHttpTools();
		Map<String, Object> resultMap = (Map<String, Object>) HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		RemittanceDataCenter.getInstance().setMapPsnTransInternationalTransferConfirm(resultMap);
		requestForRandomNumber();
	}

	/** 请求随机数 */
	private void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}

	public void queryRandomNumberCallBack(Object resultObj) {
		this.getHttpTools();
		String randomNumber = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(randomNumber))
			return;

		Intent intent = new Intent(OverseasChinaBankRemittanceInfoInputActivity.this,RemittanceConfirmOverActivity.class);
		intent.putExtra(Remittance.RANDOM, randomNumber);

		startActivityForResult(intent, QUIT_CODE);
	}

	/** 查询收款人常驻国家列表回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnQryInternationalTrans4CNYCountryCallBack(
			Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		getHttpTools();
		Map<String, Object> resultMap = (Map<String, Object>) HttpTools
				.getResponseResult(resultObj);
		List<Map<String, String>> resultList = (List<Map<String, String>>) resultMap
				.get(Remittance.COUNTRYLIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			return;
		}
		RemittanceDataCenter.getInstance()
				.setListPsnQryInternationalTrans4CNYCountry(resultList);
		startActivityForResult(new Intent(this,
				RemittanceChooseCountryActivity.class),
				RemittanceContent.CHOOSE_CHOUNTRY);
		overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
	}

	/** 查询可用额度回调 */
//	@SuppressWarnings("unchecked")
//	public void requestPsnQueryNationalTransferLimitCallBack(Object resultObj) {
//		Map<String, Object> resultMap = (Map<String, Object>) HttpTools.getResponseResult(resultObj);
//		if (StringUtil.isNullOrEmpty(resultMap)) {
//			return;
//		}
//		if (limitFlag == 0) {
//			BaseHttpEngine.dissMissProgressDialog();
//			RemittanceDataCenter.getInstance().setMapPsnQueryNationalTransferLimit(resultMap);
//			Log.v("BiiHttpEngine",over_transationFrag.getCurrency()+"over---requestPsnQueryNationalTransferLimitCallBack");
//			startActivity(new Intent(this, NationalTransferLimitDialogActivity.class).putExtra("CURRENCY", over_transationFrag.getCurrency()));
//			overridePendingTransition(R.anim.no_animation, R.anim.no_animation);
//		} else if (limitFlag == 1) {
//			// 汇款金额
//			BigDecimal limitAmount = new BigDecimal((String) RemittanceDataCenter.getInstance().getSubmitParams().get(Remittance.REMITAMOUNT));
//			// 剩余额度
//			BigDecimal convertRemainLimit = new BigDecimal((String) resultMap.get(Remittance.CONVERTREMAINLIMIT));
//			if (limitAmount.compareTo(convertRemainLimit) > 0) {
//				BaseHttpEngine.dissMissProgressDialog();
//				if (over_transationFrag.getCurrency().equals("014")) {
//					BaseDroidApp.getInstanse().showInfoMessageDialog("您的跨境汇款当日累计限额不能超过等值" + StringUtil.parseStringPattern((String) resultMap.get(Remittance.TOTALLIMIT), 2) + "美元，今日剩余额度为等值" + StringUtil.parseStringPattern((String) resultMap.get(Remittance.REMAININGLIMIT), 2) + "美元。请修改汇款金额，或持本人有效证件及相关资料至我行办理。");
//				} else {
//					BaseDroidApp.getInstanse().showInfoMessageDialog("您的跨境汇款当日累计限额不能超过等值" + StringUtil.parseStringPattern((String) resultMap.get(Remittance.TOTALLIMIT), 2) + "美元，今日剩余额度为等值" + StringUtil.parseStringPattern((String) resultMap.get(Remittance.REMAININGLIMIT), 2) + "美元（折合" + StringUtil.parseStringCodePattern(over_transationFrag.getCurrency(), (String) resultMap.get(Remittance.CONVERTREMAINLIMIT), 2) + LocalData.Currency.get(over_transationFrag.getCurrency()) + "）。请修改汇款金额，或持本人有效证件及相关资料至我行办理。");
//				}
//				return;
//			}
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put(Remittance.REMITTANCEINFO, RemittanceDataCenter.getInstance().getSubmitParams().get(Remittance.REMITTANCEINFO));
//			params.put(Remittance.REMITTANCEDESCRIPTION, RemittanceDataCenter.getInstance().getSubmitParams().get(Remittance.REMITTANCEDESCRIPTION));
//			getHttpTools().requestHttp(Remittance.PSNREMITTANCEDESCRIPTIONCHECK, "requestPsnRemittanceDescriptionCheckCallBack", params, false);
//		}
//	}

	/** 查询可用额度回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnQueryNationalTransferLimitCallBack(Object resultObj) {
		Map<String, Object> resultMap = (Map<String, Object>) HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}

		// 客户标识 1：居民类客户 2：非居民类客户
		String customerFlag = (String) resultMap.get("customerFlag");
		if (customerFlag.equals("1")) {

			if (limitFlag == 0) {
				BaseHttpEngine.dissMissProgressDialog();
				RemittanceDataCenter.getInstance().setMapPsnQueryNationalTransferLimit(resultMap);
				startActivity(new Intent(this, NationalTransferLimitDialogActivity.class).putExtra("CURRENCY", over_transationFrag.getCurrency()));
				Log.v("BiiHttpEngine",over_transationFrag.getCurrency()+"---requestPsnQueryNationalTransferLimitCallBack");
				overridePendingTransition(R.anim.no_animation, R.anim.no_animation);
			} else if (limitFlag == 1) {
				// 汇款金额
				BigDecimal limitAmount = new BigDecimal((String) RemittanceDataCenter.getInstance().getSubmitParams().get(Remittance.REMITAMOUNT));
				// 剩余额度
				BigDecimal convertRemainLimit = new BigDecimal((String) resultMap.get(Remittance.CONVERTREMAINLIMIT));
				if (limitAmount.compareTo(convertRemainLimit) > 0) {
					BaseHttpEngine.dissMissProgressDialog();
					if (over_transationFrag.getCurrency().equals("014")) {
						BaseDroidApp.getInstanse().showInfoMessageDialog("您的跨境汇款当日累计限额不能超过等值" + StringUtil.parseStringPattern((String) resultMap.get(Remittance.TOTALLIMIT), 2) + "美元，今日剩余额度为等值" + StringUtil.parseStringPattern((String) resultMap.get(Remittance.REMAININGLIMIT), 2) + "美元。请修改汇款金额，或持本人有效证件及相关资料至我行办理。");
					} else {
						BaseDroidApp.getInstanse().showInfoMessageDialog("您的跨境汇款当日累计限额不能超过等值" + StringUtil.parseStringPattern((String) resultMap.get(Remittance.TOTALLIMIT), 2) + "美元，今日剩余额度为等值" + StringUtil.parseStringPattern((String) resultMap.get(Remittance.REMAININGLIMIT), 2) + "美元（折合" + StringUtil.parseStringCodePattern(over_transationFrag.getCurrency(), (String) resultMap.get(Remittance.CONVERTREMAINLIMIT), 2) + LocalData.Currency.get(over_transationFrag.getCurrency()) + "）。请修改汇款金额，或持本人有效证件及相关资料至我行办理。");
					}
					return;
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(Remittance.REMITTANCEINFO, RemittanceDataCenter.getInstance().getSubmitParams().get(Remittance.REMITTANCEINFO));
				params.put(Remittance.REMITTANCEDESCRIPTION, RemittanceDataCenter.getInstance().getSubmitParams().get(Remittance.REMITTANCEDESCRIPTION));
				getHttpTools().requestHttp(Remittance.PSNREMITTANCEDESCRIPTIONCHECK, "requestPsnRemittanceDescriptionCheckCallBack", params, false);
			}

		} else {
			if (limitFlag == 0) {

				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().
						showInfoMessageDialog("尊敬的客户，根据国家外汇管理局规定，非居民客户个人外币跨境汇款交易不受"+"\""+"每日不可超过累计等值五万美元"+"\""+"的限额控制");

			} else if (limitFlag == 1) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(Remittance.REMITTANCEINFO,
						RemittanceDataCenter.getInstance().getSubmitParams()
								.get(Remittance.REMITTANCEINFO));
				params.put(Remittance.REMITTANCEDESCRIPTION,
						RemittanceDataCenter.getInstance().getSubmitParams()
								.get(Remittance.REMITTANCEDESCRIPTION));
				getHttpTools().requestHttp(
						Remittance.PSNREMITTANCEDESCRIPTIONCHECK,
						"requestPsnRemittanceDescriptionCheckCallBack", params,
						false);
			}
		}

	}

	/** 国际收支申报汇款用途详细说明校验回调 */
	public void requestPsnRemittanceDescriptionCheckCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		String checkResult = (String) resultMap.get(Remittance.CHECKRESULT);
		if ("2".equals(checkResult)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog("您填写的“汇款用途详细说明” 不可与“汇款用途”内容在文字上完全或部分相同，请根据您的实际汇款情况准确填写汇款用途详细说明。");
			return;
		} else if ("3".equals(checkResult)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog("您填写的“汇款用途详细说明”与选择的“汇款用途”在用途性质上可能存在差异，影响跨境汇款汇出，请检查。（性质差异情况举例：您的“汇款用途”已选择“公务及商务旅行”，但是在“汇款用途详细说明”中填写为“交学费”。）");
			return;
		} else if ("4".equals(checkResult)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog("您选择的“汇款用途”有误，请重新选择。");
			return;
		}
		getHttpTools().requestHttp(Remittance.PSNTRANSGETINTERNATIONALTRANSFERCOMMISSIONCHARGE, "requestPsnTransGetInternationalTransferCommissionChargeCallBack", RemittanceDataCenter.getInstance().getSubmitParams(), false);
	}

	/**
	 * 模板列表查询返回结果
	 */
	@SuppressWarnings("unchecked")
	public void transferTemplateQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		String totalNumber = (String) resultMap.get(Remittance.RECORDNUMBER);
		List<Map<String, Object>> modelList = (List<Map<String, Object>>) resultMap.get(Remittance.TEMPLATELIST);
		if (StringUtil.isNullOrEmpty(modelList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.remittance_no_save_model));
			return;
		}
		RemittanceDataCenter.getInstance().setTotalNumber(totalNumber);
		RemittanceDataCenter.getInstance().setModelList(modelList);
		startActivityForResult(new Intent(this, UseModelDialogActivity.class).putExtra("isOversearChinaBankModel",2), RemittanceContent.CHOOSE_MODE);
	}

	/**
	 * 境外中行收款行信息查询接口回调
	 * @param resultObj
     */
//	public void requestPsnBOCPayeeBankInfoQueryCallBack(Object resultObj) {
//
//		if (StringUtil.isNullOrEmpty(resultObj)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			return;
//		}
//		BaseHttpEngine.dissMissProgressDialog();
//		Log.v("BiiHttpEngine", resultObj + "");
//
//		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
//		@SuppressWarnings("unchecked")
//		List<Map<String, Object>> list = (List<Map<String, Object>>) result
//				.get("bocPayeeBankInfoList");
//
//		RemittanceDataCenter.getInstance().setCollectionBankList(list);
//
//		Intent intent = new Intent(
//				OverseasChinaBankRemittanceInfoInputActivity.this,
//				OverseasChinaBankCollectionBank.class);
//
//		startActivityForResult(intent,
//				RemittanceContent.RESULT_CODE_COLLECTION_BANK_RESULT);
//
//	}

	/**
	 * 境外中行收款行所属国家（地区）查询接口回调
	 * @param resultObj
     */
	public void requestPsnBOCPayeeBankRegionQueryCallBack(Object resultObj) {

		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		Log.v("BiiHttpEngine", resultObj + "");

		Map<String, Object> result = HttpTools.getResponseResult(resultObj);

		List<String> list = (List<String>) result.get("bocPayeeBankRegionList");
		String bocPayeeBankRegion = (String) result.get("bocPayeeBankRegion");

		RemittanceDataCenter.getInstance().setBocPayeeBankRegionList(list);

		Intent intent = new Intent(
				OverseasChinaBankRemittanceInfoInputActivity.this,
				OverseasChinaBankPayBankCountry.class);

		startActivityForResult(intent,
				RemittanceContent.RESULT_CODE_PAY_BANK_AREA);


	}


}
