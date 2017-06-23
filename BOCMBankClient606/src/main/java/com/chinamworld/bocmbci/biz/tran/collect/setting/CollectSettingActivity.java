package com.chinamworld.bocmbci.biz.tran.collect.setting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectCycleCodeType;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectCycleType;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectImputationMode;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.thridmanage.openacct.AccOpenStockBranchActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.collect.CollectBaseActivity;
import com.chinamworld.bocmbci.biz.tran.collect.CollectData;
import com.chinamworld.bocmbci.biz.tran.collect.CollectListActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: CollectSettingActivity
 * @Description: 跨行资金归集
 * @author luql
 * @date 2014-3-18 下午02:31:08
 */
public class CollectSettingActivity extends CollectBaseActivity {

	private static final int REQUEST_PROTOCOL_CODE = 10000;
	private static final int REQUEST_PAYLIST_CODE = 10001;
	private static final int request_setting_Code = 10002;

	private View mViewContent;

	/** 归集账户 */
	private String mPayeeAccount;
	/** 被归集账户 */
	private String mPayerAccount;
	/** 账户数据 */
	private Map<String, Object> mAccounteData;
	/** 被归集账户数据 */
	private Map<String, Object> mUnCollectData;
	/** 查询协议 */
	private Map<String, Object> mQueryData;
	/** 支付协议 */
	private Map<String, Object> mPayData;

	/** 归集账户 */
	private TextView payeeAccountView;
	/** 被归集账户 */
	private TextView payerAccountView;
	/** 被归集账户户名 */
	private TextView payerNameView;
	/** 被归集账户开户行 */
	private TextView payerBankView;
	/** 手机号 */
	private TextView mPayeeMobileView;
	/** 查询协议 */
	private TextView mQueryView;
	/** 支付协议 */
	private TextView mPayView;
	/** 归集方式 */
	private Spinner mCollectModeSpView;
	/** 留存金额布局 */
	private View mRetainAmountLayout;
	/** 转账金额 |留存金额 标签 */
	private TextView mAmountTextView;
	/** 转账金额 | 留存金额 */
	private EditText mAmountEditView;
	/** 归集周期 */
	private Spinner mCollectRuleSpView;
	/** 归集周期代码 */
	private Spinner mCollectRuleCodeSpView;
	/** 归集规则布局 */
	private View mCollectRuleCodeLayout;
	/** 向归集人发送短信通知 */
	private CheckBox mMobileCheckView;
	/** 手机号布局 */
	private View mMobileLayout;
	/** 手机号提示布局 */
	private View mMobileTipLayout;
	/** 用户协议勾选框 */
	private CheckBox mCheckBox;

	/** 下一步 */
	private View mNextView;

	// --------------------------------------
	// 上传参数
	/** 是否发送短信通知 */
	private boolean mIfMessageParam;
	/** 客户手机号 */
	private String mMobileParam;
	/** 金额参数 */
	private String mAmountParam;
	/** 归集方式参数 */
	private String mImputationModeParam;
	/** 归集周期参数 */
	private String mCycleParam;
	/** 归集周期执行日参数 */
	private String mCycleCodeParam;

	private String mAccountIdParam;
	/** 归集账户卡号 */
	private String mAccCardParam;
	/** 归集账户卡号 */
	private String mAccountNumParam;
	/** 归集账户户名 */
	private String mAccountNameParam;
	/** 归集账户类型 */
	private String mAccountTypeParam;
	/** 归集账户开户行名称 */
	private String mBankNameParam;

	/** 被归集账户人行行号 */
	private String mPayerAcctBankNo;
	/** 被归集账户开户行名称 */
	private String mPayerAccBankNameParam;
	/** 被归集账户账号/卡号 */
	private String mPayerAccountParam;
	/** 被归集账户户名 */
	private String mPayerAccNameParam;
	/** 被归集账户类型 */
	private String mPayerAcctTypeParam;
	/** RCPS查询协议号 */
	private String mQueryNoParam;
	/** RCPS查询协议号开始日期 */
	private String mQueryBeginParam;
	/** RCPS查询协议号结束日期 */
	private String mQueryEndParam;
	/** RCPS查询协议状态 */
	private String mQueryStatusParam;
	/** RCPS支付协议号 */
	private String mPayNoParam;
	/** RCPS支付协议号开始日期 */
	private String mPayBeginParam;
	/** RCPS支付协议号结束日期 */
	private String mPayEndParam;
	/** RCPS支付协议状态 */
	private String mPayStatusParam;
	/** 业务类型编码 */
	private String mPayTypeNoParam;
	/** 业务种类编码 */
	private String mPaySortNoParam;
	/** 支付协议单笔业务金额上限 */
	private String mPayQuotePerParam;
	/** 支付协议日累计业务笔数上限 */
	private String mPayLimitDParam;
	/** 支付协议日累计金额上限 */
	private String mPayQuotaDParam;
	/** 支付协议月累计业务笔数上限 */
	private String mPayLimitMParam;
	/** 支付协议月累计金额上限 */
	private String mPayQuotaMParam;
	/** 是否需要手机验证码 */
	private boolean isNeedSmc = false;
	/** 是否需要动态口令 */
	private boolean isNeedOtp = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewContent = addView(R.layout.collect_setting_activity);
		setTitle(getString(R.string.collect_setting_title));
		if (getIntentData()) {
			initParam();
			toprightBtn();
			findView();
			setListener();
			// BiiHttpEngine.showProgressDialogCanGoBack();
			// requestPsnCBMobileQuery();
		} else {
			finish();
		}
	}

	// private void requestPsnCBMobileQuery() {
	// BiiRequestBody biiRequestBody = new BiiRequestBody();
	// biiRequestBody.setMethod(Collect.PsnCBMobileQuery);
	// HttpManager.requestBii(biiRequestBody, this,
	// "requestPsnCBMobileQueryCallback");
	// }
	//
	// public void requestPsnCBMobileQueryCallback(Object resultObj) {
	// BiiHttpEngine.dissMissProgressDialog();
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// Map<String, String> resultData = (Map<String, String>)
	// biiResponseBody.getResult();
	// mMobileParam = resultData.get(Collect.cbMobile);
	// mPayeeMobileView.setText(mMobileParam);
	// }

	private void setListener() {
		mCollectModeSpView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String str = CollectImputationMode.getKeys().get(position);
				mAmountEditView.setText("");
				if (CollectImputationMode.ALL.equals(str)) {
					// 全额归集
					mRetainAmountLayout.setVisibility(View.GONE);
					// BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.collect_amount_all_prompt));
				} else if (CollectImputationMode.KEEP.equals(str)) {
					// 留存余额归集
					mAmountEditView.setHint("请输入整数");
					mAmountEditView.setInputType(EditorInfo.TYPE_CLASS_NUMBER);// 整数
					mAmountTextView.setText(R.string.collect_retain_amount);
					mRetainAmountLayout.setVisibility(View.VISIBLE);
					mAmountTextView.requestFocus();
				} else if (CollectImputationMode.QUOTA.equals(str)) {
					// 定额归集
					mAmountEditView.setHint("请输入整数");
					mAmountTextView.setText(R.string.collect_transferAmount_amount);
					mAmountEditView.setInputType(EditorInfo.TYPE_CLASS_NUMBER);// 整数
					mRetainAmountLayout.setVisibility(View.VISIBLE);
					mAmountTextView.requestFocus();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		mCollectRuleSpView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String string = CollectCycleType.getCycleKeys().get(position);
				if (CollectCycleType.DAY.equals(string)) {
					// 日
					mCollectRuleCodeLayout.setVisibility(View.GONE);
				} else if (CollectCycleType.WEEK.equals(string)) {
					// 周
					List<String> weekValues = CollectCycleCodeType.getWeekValues();
					initSpinner(mCollectRuleCodeSpView, weekValues);
					mCollectRuleCodeLayout.setVisibility(View.VISIBLE);
				} else if (CollectCycleType.MONTH.equals(string)) {
					// 月
					List<String> monthValues = CollectCycleCodeType.getMonthValues();
					initSpinner(mCollectRuleCodeSpView, monthValues);
					mCollectRuleCodeLayout.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		mCollectRuleCodeSpView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				int rulePosition = mCollectRuleSpView.getSelectedItemPosition();
				String string = CollectCycleType.getCycleKeys().get(rulePosition);
				if (CollectCycleType.MONTH.equals(string)) {
					String cycleCode = CollectCycleCodeType.getMonthKeys().get(position);
					String cycleCodeStr = CollectCycleCodeType.getMonthValues().get(position);
					if (CollectCycleCodeType.isMonthEndCode(cycleCode)) {
						// 是否月末
						BaseDroidApp.getInstanse()
								.showInfoMessageDialog(getString(R.string.collect_rule_month_endcode));
					} else if (CollectCycleCodeType.isMonthEnd(cycleCode)) {
						// 29-31
						String content = String.format(getString(R.string.collect_rule_month_end), cycleCodeStr);
						BaseDroidApp.getInstanse().showInfoMessageDialog(content);
					} else {
						// 1-28
						String content = String.format(getString(R.string.collect_rule_month_begin), cycleCodeStr);
						BaseDroidApp.getInstanse().showInfoMessageDialog(content);
					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		mMobileCheckView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mMobileLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
				mMobileTipLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_pay_protocol:
			// 支付协议
			Intent payIntent = new Intent(this, CollectPaylistActivity.class);
			payIntent.putExtra(Collect.cbAccCard, mPayeeAccount);
			startActivityForResult(payIntent, REQUEST_PAYLIST_CODE);
			overridePendingTransition(R.anim.n_pop_enter_bottom_up, R.anim.no_animation);
			break;
		case R.id.tv_server_info:
			// 用户协议
			Intent intent = new Intent(this, CollectProtocolActivity.class);
			startActivityForResult(intent, REQUEST_PROTOCOL_CODE);
			overridePendingTransition(R.anim.n_pop_enter_bottom_up, R.anim.no_animation);
			break;
		case R.id.btnnext:
			// 下一步
			if (checkSubmit()) {
				if (CollectImputationMode.ALL.equals(mImputationModeParam)) {
					// 全部归集
					BaseDroidApp.getInstanse().showErrorDialog(getString(R.string.collect_amount_all_prompt),
							R.string.cancle, R.string.confirm, new OnClickListener() {
								@Override
								public void onClick(View v) {
									BaseDroidApp.getInstanse().dismissErrorDialog();
									if (v.getId() == R.id.retry_btn) {
										BiiHttpEngine.showProgressDialog();
										requestCommConversationId();
									}
								}
							});

				} else if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
					// 留存归集
					BaseDroidApp.getInstanse().showErrorDialog(getString(R.string.collect_amount_keep_prompt),
							R.string.cancle, R.string.confirm, new OnClickListener() {
								@Override
								public void onClick(View v) {
									BaseDroidApp.getInstanse().dismissErrorDialog();
									if (v.getId() == R.id.retry_btn) {
										BiiHttpEngine.showProgressDialog();
										requestCommConversationId();
									}
								}
							});					
				} else if (CollectImputationMode.QUOTA.equals(mImputationModeParam)) {
					// 定额归集
					BaseDroidApp.getInstanse().showErrorDialog(getString(R.string.collect_amount_quota_prompt),
							R.string.cancle, R.string.confirm, new OnClickListener() {
								@Override
								public void onClick(View v) {
									BaseDroidApp.getInstanse().dismissErrorDialog();
									if (v.getId() == R.id.retry_btn) {
										BiiHttpEngine.showProgressDialog();
										requestCommConversationId();
									}
								}
							});					
				} else {
					BiiHttpEngine.showProgressDialog();
					requestCommConversationId();
				}
			}
			break;
		}
	}

	private boolean checkSubmit() {
		int modeSelectedPosition = mCollectModeSpView.getSelectedItemPosition();
		mImputationModeParam = CollectImputationMode.getKeys().get(modeSelectedPosition);
		if (CollectImputationMode.ALL.equals(mImputationModeParam)) {
			// 全额归集
			mAmountParam = null;
		} else if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
			// 留存余额归集
			mAmountParam = mAmountEditView.getText().toString().trim();
			ArrayList<RegexpBean> regexpBeanLists = new ArrayList<RegexpBean>();
			RegexpBean transAmountReg = new RegexpBean(getString(R.string.collect_retain_amount_prompt), mAmountParam,
					"orderAmount11");
			regexpBeanLists.add(transAmountReg);
			if (!RegexpUtils.regexpDate(regexpBeanLists)) {
				return false;
			}
		} else if (CollectImputationMode.QUOTA.equals(mImputationModeParam)) {
			// 定额归集
			mAmountParam = mAmountEditView.getText().toString().trim();
			ArrayList<RegexpBean> regexpBeanLists = new ArrayList<RegexpBean>();
			RegexpBean transAmountReg = new RegexpBean(getString(R.string.collect_transferAmount_amount_prompt),
					mAmountParam, "orderAmount11");
			regexpBeanLists.add(transAmountReg);
			if (!RegexpUtils.regexpDate(regexpBeanLists)) {
				return false;
			}
		}

		int ruleSelectedPosition = mCollectRuleSpView.getSelectedItemPosition();
		mCycleParam = CollectCycleType.getCycleKeys().get(ruleSelectedPosition);
		int ruleCodePosition = mCollectRuleCodeSpView.getSelectedItemPosition();
		if (CollectCycleType.DAY.equals(mCycleParam)) {
			mCycleCodeParam = null;
		} else if (CollectCycleType.WEEK.equals(mCycleParam)) {
			// 周
			mCycleCodeParam = CollectCycleCodeType.getWeekKeys().get(ruleCodePosition);
		} else if (CollectCycleType.MONTH.equals(mCycleParam)) {
			// 月
			mCycleCodeParam = CollectCycleCodeType.getMonthKeys().get(ruleCodePosition);
		}
		mIfMessageParam = mMobileCheckView.isChecked();
		if (!mCheckBox.isChecked()) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.collect_protocol_check_prompt));
			return false;
		}

		return true;
	}

	private void initParam() {
		mAccountIdParam = (String) mAccounteData.get(Comm.ACCOUNT_ID);
//		mAccCardParam = (String) mAccounteData.get(Collect.cbAccCard);
		mAccCardParam = (String) mAccounteData.get(Acc.ACC_ACCOUNTNUMBER_RES);
		mAccountNumParam = (String) mAccounteData.get(Collect.cbAccNum);
//		mAccountNameParam = (String) mAccounteData.get(Collect.cbAccName);
		mAccountNameParam = (String) mAccounteData.get("accountName");

		mAccountTypeParam = (String) mAccounteData.get(Comm.ACCOUNT_TYPE);
		mBankNameParam = (String) mAccounteData.get(Comm.BRANCHNAME);

		mPayerAcctBankNo = (String) mUnCollectData.get(Collect.payerAcctBankNo);
		mPayerAccBankNameParam = (String) mUnCollectData.get(Collect.payerAccBankName);
		mPayerAccountParam = (String) mUnCollectData.get(Collect.payerAccount);
		mPayerAccNameParam = (String) mUnCollectData.get(Collect.payerAccountName);
		mPayerAcctTypeParam = (String) mUnCollectData.get(Collect.retActype);
		mQueryNoParam = (String) mQueryData.get(Collect.queryNo);
		mQueryBeginParam = (String) mQueryData.get(Collect.queryBegin);
		mQueryEndParam = (String) mQueryData.get(Collect.queryEnd);
		mQueryStatusParam = (String) mQueryData.get(Collect.queryStatus);
		mPayNoParam = (String) mPayData.get(Collect.payNo);
		mPayBeginParam = (String) mPayData.get(Collect.payBegin);
		mPayEndParam = (String) mPayData.get(Collect.payEnd);
		mPayStatusParam = (String) mPayData.get(Collect.payStatus);
		mPayTypeNoParam = (String) mPayData.get(Collect.payTypeNo);
		mPaySortNoParam = (String) mPayData.get(Collect.paySortNo);
		mPayQuotePerParam = (String) mPayData.get(Collect.payQuotaPer);
		mPayLimitDParam = (String) mPayData.get(Collect.payLimitD);
		mPayQuotaDParam = (String) mPayData.get(Collect.payQuotaD);
		mPayLimitMParam = (String) mPayData.get(Collect.payLimitM);
		mPayQuotaMParam = (String) mPayData.get(Collect.payQuotaM);
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 发送安全因子请求
		requestGetSecurityFactor(Collect.Setting_SERVICECODE);
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		final BaseDroidApp bdApp = BaseDroidApp.getInstanse();
		bdApp.showSeurityChooseDialog(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String combinId = bdApp.getSecurityChoosed();
				requestPsnCBCollectAddPre(combinId);
			}
		});

	}

	/** 跨行资金归集添加预交易 */
	private void requestPsnCBCollectAddPre(String combinId) {
		BiiHttpEngine.showProgressDialog();
		String conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Collect.PsnCBCollectAddPre);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> map = new HashMap<String, Object>();

		map.put(Collect.cbAccId, mAccountIdParam);
		map.put(Collect.cbAccCard, mAccCardParam);
		map.put(Collect.cbAccNum, mAccountNumParam);
		map.put(Collect.cbAccName, mAccountNameParam);
		map.put(Collect.cbAccType, mAccountTypeParam);
		map.put(Collect.cbAccBankName, mBankNameParam);
		map.put(Collect.payerAcctBankNo, mPayerAcctBankNo);
		map.put(Collect.payerAccBankName, mPayerAccBankNameParam);
		map.put(Collect.payerAcctType, mPayerAcctTypeParam);
		map.put(Collect.payerAccount, mPayerAccountParam);
		map.put(Collect.payerAcntName, mPayerAccNameParam);
		map.put(Collect.queryNo, mQueryNoParam);
		map.put(Collect.queryBegin, mQueryBeginParam);
		map.put(Collect.queryEnd, mQueryEndParam);
		map.put(Collect.queryStatus, mQueryStatusParam);
		map.put(Collect.payNo, mPayNoParam);
		map.put(Collect.payBegin, mPayBeginParam);
		map.put(Collect.payEnd, mPayEndParam);
		map.put(Collect.payStatus, mPayStatusParam);
		map.put(Collect.payTypeNo, mPayTypeNoParam);
		map.put(Collect.paySortNo, mPaySortNoParam);
		map.put(Collect.payQuotaPer, mPayQuotePerParam);
		map.put(Collect.payLimitD, mPayLimitDParam);
		map.put(Collect.payQuotaD, mPayQuotaDParam);
		map.put(Collect.payLimitM, mPayLimitMParam);
		map.put(Collect.payQuotaM, mPayQuotaMParam);
		map.put(Collect.currency, "001"); // "人民币元"
		// map.put(Collect.noteType, "02"); // "现汇"="02", "现钞"="01"

		// 规则参数
		map.put(Collect.imputationMode, mImputationModeParam);
		map.put(Collect.cycle, mCycleParam);
		if (!CollectCycleType.DAY.equalsIgnoreCase(mCycleParam)) {
			map.put(Collect.cycleCode, mCycleCodeParam);
		}
		if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
			map.put(Collect.retainAmount, new BigDecimal(mAmountParam));
		} else if (CollectImputationMode.QUOTA.equals(mImputationModeParam)) {
			map.put(Collect.transferAmount, new BigDecimal(mAmountParam));
		}
		map.put(Collect.ifMessage, mIfMessageParam ? "Y" : "N");
		if (mIfMessageParam) {
			map.put(Collect.cbMobile, mMobileParam);
		}
		map.put(Collect.combinId, combinId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCBCollectAddPreCallback");
	}

	public void requestPsnCBCollectAddPreCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		// TODO
		TranDataCenter.getInstance().setAtmpremap(result);
//		List<Object> factorList = EpayUtil.getFactorList(result);
//		for (Object obj : factorList) {
//			String confirmType = EpayUtil.getString(obj, "");
//			if (PubConstants.PUB_FIELD_OTP.equals(confirmType)) {
//				isNeedOtp = true;
//			}
//			if (PubConstants.PUB_FIELD_SMC.equals(confirmType)) {
//				isNeedSmc = true;
//			}
//		}
		requestForRandomNumber();
	}

	/**
	 * 请求随机数
	 */
	private void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		String conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		biiRequestBody.setConversationId(conversationId);
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}

	public void queryRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String randomNumber = (String) biiResponseBody.getResult();
		Intent intent = new Intent(this, CollectSettingConfirmActivity.class);
		// cbAccId归集账户ID
		// cbAccCard归集账户卡号
		// cbAccNum归集账户号
		// cbAccName归集账户户名
		// cbAccType归集账户类型 网银账户类型
		// cbAccBankName归集账户开户行名称
		// payerAcctBankNo被归集账户人行行号
		// payerAccBankName被归集账户开户行名称
		// payerAcctType被归集账户类型
		// payerAccount被归集账户账号/卡号
		// payerAcntName被归集账户户名
		intent.putExtra(Collect.cbAccId, mAccountIdParam);
		intent.putExtra(Collect.cbAccCard, mAccCardParam);
		intent.putExtra(Collect.cbAccNum, mAccountNumParam);
		intent.putExtra(Collect.cbAccName, mAccountNameParam);
		intent.putExtra(Collect.cbAccType, mAccountTypeParam);
		intent.putExtra(Collect.cbAccBankName, mBankNameParam);
		intent.putExtra(Collect.payQuotaM, mPayQuotaMParam);
		intent.putExtra(Collect.payerAcctBankNo, mPayerAcctBankNo);
		intent.putExtra(Collect.payerAccBankName, mPayerAccBankNameParam);
		intent.putExtra(Collect.payerAccount, mPayerAccountParam);
		intent.putExtra(Collect.payerAccountName, mPayerAccNameParam);
		intent.putExtra(Collect.payerAcctType, mPayerAcctTypeParam);
		intent.putExtra(Collect.queryNo, mQueryNoParam);
		intent.putExtra(Collect.queryBegin, mQueryBeginParam);
		intent.putExtra(Collect.queryEnd, mQueryEndParam);
		intent.putExtra(Collect.queryStatus, mQueryStatusParam);
		intent.putExtra(Collect.payNo, mPayNoParam);
		intent.putExtra(Collect.payBegin, mPayBeginParam);
		intent.putExtra(Collect.payEnd, mPayEndParam);
		intent.putExtra(Collect.payStatus, mPayStatusParam);
		intent.putExtra(Collect.payTypeNo, mPayTypeNoParam);
		intent.putExtra(Collect.paySortNo, mPaySortNoParam);
		intent.putExtra(Collect.payQuotaPer, mPayQuotePerParam);
		intent.putExtra(Collect.payLimitD, mPayLimitDParam);
		intent.putExtra(Collect.payQuotaD, mPayQuotaDParam);
		intent.putExtra(Collect.payLimitM, mPayLimitMParam);
		intent.putExtra(Collect.payQuotaM, mPayQuotaMParam);

		intent.putExtra(Collect.imputationMode, mImputationModeParam);
		intent.putExtra(Collect.amount, mAmountParam);
		intent.putExtra(Collect.cycle, mCycleParam);
		if (!CollectCycleType.DAY.equalsIgnoreCase(mCycleParam)) {
			intent.putExtra(Collect.cycleCode, mCycleCodeParam);
		}

		intent.putExtra(Collect.ifMessage, mIfMessageParam);
		intent.putExtra(Collect.cbMobile, mMobileParam);
//		intent.putExtra("isNeedSmc", isNeedSmc);
//		intent.putExtra("isNeedOtp", isNeedOtp);
		intent.putExtra("randomNum", randomNumber);
		startActivityForResult(intent, request_setting_Code);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_PROTOCOL_CODE:
			if (resultCode == RESULT_OK) {
				mCheckBox.setChecked(true);
			} else if (resultCode == RESULT_CANCELED) {
				mCheckBox.setChecked(false);
			}
			break;
		case REQUEST_PAYLIST_CODE:
			// 支付协议
			if (resultCode == RESULT_OK) {
				int position = data.getIntExtra("position", -1);
				if (position != -1) {
					List<Map<String, Object>> payList = CollectData.getInstance().getPayList(mPayeeAccount);
					mPayData = payList.get(position);
					// mPayView.setText((String) mPayData.get(Collect.payNo));
					mPayView.setText(Html.fromHtml("<u>" + (String) mPayData.get(Collect.payNo) + "</u>"));
				}
			} else if (resultCode == AccOpenStockBranchActivity.EMPTY_RESULTCODE) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.no_select_department));
			}
			break;
		case request_setting_Code:
			// 设置
			if (resultCode == RESULT_OK) {
				setResult(CollectListActivity.refresh_list);
				finish();
			}
			break;
		}
	}

	private void findView() {

		payeeAccountView = (TextView) mViewContent.findViewById(R.id.tv_payee_account);
		payerAccountView = (TextView) mViewContent.findViewById(R.id.tv_payer_account);
		payerNameView = (TextView) mViewContent.findViewById(R.id.tv_payer_name);
		payerBankView = (TextView) mViewContent.findViewById(R.id.tv_payer_bank);
		mPayeeMobileView = (TextView) findViewById(R.id.tv_payee_mobile);
		mQueryView = (TextView) findViewById(R.id.tv_query_protocol);
		mPayView = (TextView) findViewById(R.id.tv_pay_protocol);
		mCollectModeSpView = (Spinner) findViewById(R.id.sp_collect_mode);
		mRetainAmountLayout = findViewById(R.id.retain_amount_layout);
		mAmountEditView = (EditText) findViewById(R.id.et_amount);
		mAmountTextView = (TextView) findViewById(R.id.tv_amount);
		mCollectRuleSpView = (Spinner) findViewById(R.id.sp_collect_rule);
		mCollectRuleCodeSpView = (Spinner) findViewById(R.id.sp_collect_rule_code);
		mCollectRuleCodeLayout = findViewById(R.id.collect_rule_code_layout);
		mMobileCheckView = (CheckBox) findViewById(R.id.cb_message);
		mMobileLayout = findViewById(R.id.mobile_layout);
		mMobileTipLayout = findViewById(R.id.mobile_tip_layout);
		mCheckBox = (CheckBox) findViewById(R.id.checkbox);
		mNextView = findViewById(R.id.btnnext);

		payeeAccountView.setText(StringUtil.getForSixForString(mAccCardParam));
		payerAccountView.setText(StringUtil.getForSixForString(mPayerAccountParam));
		payerNameView.setText(mPayerAccNameParam);
		payerBankView.setText(mPayerAccBankNameParam);
		// mPayView.setText(R.string.please_choose);
		mPayView.setText(Html.fromHtml("<u>" + (String) mPayData.get(Collect.payNo) + "</u>"));

		String queryNo = (String) mQueryData.get(Collect.queryNo);
		mQueryView.setText(queryNo);

		mMobileCheckView.setChecked(true);
		mMobileLayout.setVisibility(mMobileCheckView.isChecked() ? View.VISIBLE : View.GONE);
		mMobileTipLayout.setVisibility(mMobileCheckView.isChecked() ? View.VISIBLE : View.GONE);
		// 用户协议
		TextView tvServerInfo = (TextView) mViewContent.findViewById(R.id.tv_server_info);
		tvServerInfo.setText(Html.fromHtml("<u>" + getString(R.string.collect_server_info) + "</u>"));
		tvServerInfo.setOnClickListener(this);

		mPayeeMobileView.setText(mMobileParam);

		// 归集方式
		List<String> imputationData = CollectImputationMode.getImputationValues();
		initSpinner(mCollectModeSpView, imputationData);
		// 归集周期
		List<String> collectCycleData = CollectCycleType.getCycleValues();
		initSpinner(mCollectRuleSpView, collectCycleData);

		mPayView.setOnClickListener(this);
		mNextView.setOnClickListener(this);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payeeAccountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerAccountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerNameView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerBankView);
	}

	// private List<String> addPleaseStr(List<String> data) {
	// // data.add(0, getString(R.string.please_choose));
	// return data;
	// }

	// 绑定spinner
	private void initSpinner(Spinner spinner, List<String> list) {
		ArrayAdapter<String> accAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
		accAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(accAdapter);
	}

	private boolean getIntentData() {
		CollectData data = CollectData.getInstance();
		Intent intent = getIntent();
		mMobileParam = intent.getStringExtra(Collect.cbMobile);
		mPayeeAccount = intent.getStringExtra(Collect.cbAccCard);
		mPayerAccount = intent.getStringExtra(Collect.payerAccount);
		//mMobileParam = intent.getStringExtra(Collect.cbMobile);
		mAccounteData = data.getAccountData(mPayeeAccount);
		mUnCollectData = data.getUnCollectData(mPayeeAccount, mPayerAccount);
		
		// 查询协议
		List<Map<String, Object>> queryList = CollectData.getInstance().getQueryList(mPayeeAccount);
		mQueryData = queryList.get(0);
		List<Map<String, Object>> payList = CollectData.getInstance().getPayList(mPayeeAccount);
		mPayData = payList.get(0);
		return mUnCollectData != null && mAccounteData != null && mQueryData != null && mPayData != null;
	}
}
