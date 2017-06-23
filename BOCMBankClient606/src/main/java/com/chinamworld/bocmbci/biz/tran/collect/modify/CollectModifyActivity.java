package com.chinamworld.bocmbci.biz.tran.collect.modify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectCycleCodeType;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectCycleType;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectImputationMode;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.collect.CollectBaseActivity;
import com.chinamworld.bocmbci.biz.tran.collect.CollectData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: CollectModifyActivity
 * @Description: 跨行资金归集
 * @author luql
 * @date 2014-3-18 下午02:31:08
 */
public class CollectModifyActivity extends CollectBaseActivity {

	private static final int REQUEST_PROTOCOL_CODE = 10000;
	private static final int REQUEST_PAYLIST_CODE = 10001;
	private static final int request_modify_Code = 10002;

	private View mViewContent;

	// /** 归集账户 */
	// private String mPayeeAccount;
	// /** 被归集账户 */
	// private String mPayerAccount;
	// /** 账户数据 */
	// private Map<String, Object> mAccounteData;
	// /** 被归集账户数据 */
	// private Map<String, Object> mCollectData;

	/** 归集账户 */
	private TextView payeeAccountView;
	/** 被归集账户 */
	private TextView payerAccountView;
	/** 被归集账户户名 */
	private TextView payerNameView;
	/** 被归集账户开户行 */
	private TextView payerBankView;
	/** 归集人手机号 标签 */
	private TextView mMobile;
	/** 手机号 */
	private TextView mPayeeMobileView;
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
	/** 提示信息 */
	private TextView mMobileTip;
	/** 下一步 */
	private View mNextView;

	// --------------------------------------
	// 上传参数
	/** 规则编号 */
	private String mRuleNoParam;
	/** 归集账户ID */
	private String mAccountId;
	/** 归集账户卡号 */
	private String mAccCardParam;
	/** 归集账户卡号 */
	private String mAccNumParam;
	/** 被归集账户开户行名称 */
	private String mPayerAccBankNameParam;
	/** 被归集账户账号/卡号 */
	private String mPayerAccountParam;
	/** 被归集账户户名 */
	private String mPayerAcntNameParam;

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

	// /** 归集账户户名 */
	// private String mAccountNameParam;
	// /** 归集账户类型 */
	// private String mAccountTypeParam;
	// /** 归集账户开户行名称 */
	// private String mBankNameParam;
	// /** 被归集账户人行行号 */
	// private String mPayerAcctBankNo;

	// /** 被归集账户户名 */
	// private String mPayerAccNameParam;
	/** 是否需要手机验证码 */
	private boolean isNeedSmc = false;
	/** 是否需要动态口令 */
	private boolean isNeedOtp = false;
	/** 是否需要中银E盾 */
	private boolean isNeedUsbKey = false;
	// TODO 两个标志为，setView()后Spinner的Listener在显示的时候在回调
	private boolean imputationModeFlag = true;
	private boolean ruleCodeFlag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewContent = addView(R.layout.collect_modify_activity);
		setTitle(getString(R.string.collect_modify_title));
		if (getIntentData()) {
			initParam();
			toprightBtn();
			findView();
			setListener();
			setView();
			// BiiHttpEngine.showProgressDialogCanGoBack();
			// requestPsnCBMobileQuery();
		} else {
			finish();
		}
	}

	private void setView() {
		List<String> imputationValues = CollectImputationMode.getKeys();
		int modePosition = imputationValues.indexOf(mImputationModeParam);
		mCollectModeSpView.setSelection(modePosition);
		if (CollectImputationMode.ALL.equals(mImputationModeParam)) {
			mAmountEditView.setText("");
			imputationModeFlag = false;
		} else if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
			mAmountEditView.setText(StringUtil.deleateNumber(mAmountParam));
			imputationModeFlag = false;
		} else if (CollectImputationMode.QUOTA.equals(mImputationModeParam)) {
			mAmountEditView.setText(StringUtil.deleateNumber(mAmountParam));
			imputationModeFlag = false;
		}

		int indexOf = CollectCycleType.getCycleKeys().indexOf(mCycleParam);
		mCollectRuleSpView.setSelection(indexOf);

		if (CollectCycleType.DAY.equalsIgnoreCase(mCycleParam)) {

		} else if (CollectCycleType.WEEK.equalsIgnoreCase(mCycleParam)) {
			List<String> weekValues = CollectCycleCodeType.getWeekValues();
			initSpinner(mCollectRuleCodeSpView, weekValues);
			int cycleCodePosition = CollectCycleCodeType.getWeekKeys().indexOf(mCycleCodeParam);
			mCollectRuleCodeSpView.setSelection(cycleCodePosition);
			ruleCodeFlag = false;
		} else if (CollectCycleType.MONTH.equalsIgnoreCase(mCycleParam)) {
			List<String> monthValues = CollectCycleCodeType.getMonthValues();
			initSpinner(mCollectRuleCodeSpView, monthValues);
			int cycleCodePosition = CollectCycleCodeType.getMonthKeys().indexOf(mCycleCodeParam);
			mCollectRuleCodeSpView.setSelection(cycleCodePosition);
			ruleCodeFlag = false;
		}

		mMobileCheckView.setChecked(mIfMessageParam);
		
		//如果发送短信通知，则显示归集人手机号等信息
		if(mIfMessageParam){
			mMobile.setVisibility(View.VISIBLE);
			mPayeeMobileView.setVisibility(View.VISIBLE);
			mMobileTip.setVisibility(View.VISIBLE);
		}
	}

	private void setListener() {
		mCollectModeSpView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String str = CollectImputationMode.getKeys().get(position);
				if (imputationModeFlag) {
					mAmountEditView.setText("");
				}
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
					mAmountEditView.setInputType(EditorInfo.TYPE_CLASS_NUMBER);// 小数
					mRetainAmountLayout.setVisibility(View.VISIBLE);
					mAmountTextView.requestFocus();
				}
				imputationModeFlag = true;
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
					if (ruleCodeFlag) {
						List<String> weekValues = CollectCycleCodeType.getWeekValues();
						initSpinner(mCollectRuleCodeSpView, weekValues);
					}
					mCollectRuleCodeLayout.setVisibility(View.VISIBLE);
				} else if (CollectCycleType.MONTH.equals(string)) {
					// 月
					if (ruleCodeFlag) {
						List<String> monthValues = CollectCycleCodeType.getMonthValues();
						initSpinner(mCollectRuleCodeSpView, monthValues);
					}
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
				if (ruleCodeFlag) {
					int rulePosition = mCollectRuleSpView.getSelectedItemPosition();
					String string = CollectCycleType.getCycleKeys().get(rulePosition);
					if (CollectCycleType.MONTH.equals(string)) {
						String cycleCode = CollectCycleCodeType.getMonthKeys().get(position);
						String cycleCodeStr = CollectCycleCodeType.getMonthValues().get(position);
						if (CollectCycleCodeType.isMonthEndCode(cycleCode)) {
							// 是否月末
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									getString(R.string.collect_rule_month_endcode));
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
				ruleCodeFlag = true;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
//		mMobileCheckView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				mMobileLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
//				mMobileTipLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
//			}
//		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// case R.id.tv_pay_protocol:
		// // 支付协议
		// Intent payIntent = new Intent(this, CollectPaylistActivity.class);
		// payIntent.putExtra(Collect.cbAccCard, mPayeeAccount);
		// startActivityForResult(payIntent, REQUEST_PAYLIST_CODE);
		// overridePendingTransition(R.anim.n_pop_enter_bottom_up,
		// R.anim.no_animation);
		// break;
		// case R.id.tv_server_info:
		// // 用户协议
		// Intent intent = new Intent(this, CollectProtocolActivity.class);
		// startActivityForResult(intent, REQUEST_PROTOCOL_CODE);
		// overridePendingTransition(R.anim.n_pop_enter_bottom_up,
		// R.anim.no_animation);
		// break;
		case R.id.btnnext:
			// 下一步
			if (checkSubmit()) {
				if (CollectImputationMode.ALL.equals(mImputationModeParam)) {
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
		return true;
	}

	private void initParam() {

		// mAccountId = (String) mAccounteData.get(Comm.ACCOUNT_ID);
		// mAccNumParam = (String) mAccounteData.get(Collect.cbAccNum);
		// mAccCardParam = (String) mAccounteData.get(Collect.cbAccCard);
		//
		// mRuleNoParam = (String) mCollectData.get(Collect.rusleNo);
		// mPayerAcntNameParam = (String)
		// mCollectData.get(Collect.payerAccountName);
		// mPayerAccBankNameParam = (String)
		// mCollectData.get(Collect.payerAccBankName);
		// mPayerAccountParam = (String) mCollectData.get(Collect.payerAccount);

		// mAccountNameParam = (String) mAccounteData.get(Comm.ACCOUNT_NAME);
		// mAccountTypeParam = (String) mAccounteData.get(Comm.ACCOUNT_TYPE);
		// mBankNameParam = (String) mAccounteData.get(Comm.BRANCHNAME);

		// mPayerAcctBankNo = (String)
		// mCollectData.get(Collect.payerAcctBankNo);

		// mPayerAccNameParam = (String)
		// mCollectData.get(Collect.payerAccountName);
		// mMobileParam = CollectData.getInstance().getMobile(mPayeeAccount);
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 发送安全因子请求
		requestGetSecurityFactor(Collect.modify_SERVICECODE);
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		final BaseDroidApp bdApp = BaseDroidApp.getInstanse();
		bdApp.showSeurityChooseDialog(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BiiHttpEngine.showProgressDialog();
				String combinId = bdApp.getSecurityChoosed();
				requestPsnCBCollectModifyPre(combinId);
			}
		});

	}

	/** 跨行资金归集设置 */
	private void requestPsnCBCollectModifyPre(String combinId) {
		String conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Collect.PsnCBCollectModifyPre);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> map = new HashMap<String, Object>();

		map.put(Collect.ruleNo, mRuleNoParam);
		map.put(Collect.cbAccId, mAccountId);
		map.put(Collect.cbAccCard, mAccCardParam);
		map.put(Collect.cbAccNum, mAccNumParam);
		map.put(Collect.payerAccBankName, mPayerAccBankNameParam);
		map.put(Collect.payerAccount, mPayerAccountParam);
		map.put(Collect.payerAcntName, mPayerAcntNameParam);
		map.put(Collect.currency, "001"); // "人民币元"
		// map.put(Collect.noteType, "02"); // "现汇"="02", "现钞"="01"
		// 规则参数
		map.put(Collect.imputationMode, mImputationModeParam);
		map.put(Collect.cycle, mCycleParam);
		if (!CollectCycleType.DAY.equalsIgnoreCase(mCycleParam)) {
			map.put(Collect.cycleCode, mCycleCodeParam);
		}
		if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
			map.put(Collect.retainAmt, new BigDecimal(mAmountParam));
		} else if (CollectImputationMode.QUOTA.equals(mImputationModeParam)) {
			map.put(Collect.transferAmount, new BigDecimal(mAmountParam));
		}
		map.put(Collect.ifMessage, mIfMessageParam ? "Y" : "N");
		if (mIfMessageParam) {
			if(null == mMobileParam){
				mMobileParam = "";
			}
			map.put(Collect.cbMobile,mMobileParam );
		}
		map.put(Collect.combinId, combinId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCBCollectAddPre");
	}

	public void requestPsnCBCollectAddPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		Map<Object, Object> result = (Map<Object, Object>) biiResponseBody.getResult();
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		TranDataCenter.getInstance().setAtmpremap(result);
		
		// List<Map<String, Object>> factorList = (List<Map<String, Object>>)
		// result.get(DrawMoney.FACTORLIST);
//		List<Object> factorList = EpayUtil.getFactorList(result);
//		for (Object obj : factorList) {
//			String confirmType = EpayUtil.getString(obj, "");
//			if (PubConstants.PUB_FIELD_OTP.equals(confirmType)) {
//				isNeedOtp = true;
//			}
//			if (PubConstants.PUB_FIELD_SMC.equals(confirmType)) {
//				isNeedSmc = true;
//			}
//			if (PubConstants.PUB_FIELD_USBKEY.equals(confirmType)) {
//				isNeedUsbKey = true;
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
		Intent intent = new Intent(this, CollectModifyConfirmActivity.class);

		intent.putExtra(Collect.cbAccId, mAccountId);
		intent.putExtra(Collect.ruleNo, mRuleNoParam);
		intent.putExtra(Collect.cbMobile, mMobileParam);
		intent.putExtra(Collect.cbAccCard, mAccCardParam);
		intent.putExtra(Collect.cbAccNum, mAccNumParam);
		// intent.putExtra(Collect.cbAccName, mAccountNameParam);
		// intent.putExtra(Collect.cbAccType, mAccountTypeParam);
		// intent.putExtra(Collect.cbAccBankName, mBankNameParam);
		intent.putExtra(Collect.amount, mAmountParam);
		// intent.putExtra(Collect.payerAcctBankNo, mPayerAcctBankNo);
		intent.putExtra(Collect.payerAcntName, mPayerAcntNameParam);
		intent.putExtra(Collect.payerAccBankName, mPayerAccBankNameParam);
		intent.putExtra(Collect.payerAccount, mPayerAccountParam);
		// intent.putExtra(Collect.payerAccountName, mPayerAccNameParam);

		intent.putExtra(Collect.imputationMode, mImputationModeParam);
		intent.putExtra(Collect.cycle, mCycleParam);
		if (!CollectCycleType.DAY.equalsIgnoreCase(mCycleParam)) {
			intent.putExtra(Collect.cycleCode, mCycleCodeParam);
		}

		if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
			intent.putExtra(Collect.retainAmount, mAmountParam);
		} else if (CollectImputationMode.QUOTA.equals(mImputationModeParam)) {
			intent.putExtra(Collect.transferAmount, mAmountParam);
		}

		intent.putExtra(Collect.ifMessage, mIfMessageParam);

//		intent.putExtra("isNeedSmc", isNeedSmc);
//		intent.putExtra("isNeedOtp", isNeedOtp);
//		isNeedUsbKey = true;
//		intent.putExtra("isNeedUsbKey", isNeedUsbKey);
		intent.putExtra("randomNum", randomNumber);
		startActivityForResult(intent, request_modify_Code);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == request_modify_Code && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}

	private void findView() {

		payeeAccountView = (TextView) mViewContent.findViewById(R.id.tv_payee_account);
		payerAccountView = (TextView) mViewContent.findViewById(R.id.tv_payer_account);
		payerNameView = (TextView) mViewContent.findViewById(R.id.tv_payer_name);
		payerBankView = (TextView) mViewContent.findViewById(R.id.tv_payer_bank);
		mPayeeMobileView = (TextView) findViewById(R.id.tv_payee_mobile);

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
		mNextView = findViewById(R.id.btnnext);

		mMobile = (TextView)mViewContent.findViewById(R.id.collect_mobile);
		mMobileTip = (TextView)mViewContent.findViewById(R.id.mobile_tip_layout);
		
		payeeAccountView.setText(StringUtil.getForSixForString(mAccCardParam));
		payerAccountView.setText(StringUtil.getForSixForString(mPayerAccountParam));
		payerNameView.setText(mPayerAcntNameParam);
		payerBankView.setText(mPayerAccBankNameParam);

//		mMobileLayout.setVisibility(mMobileCheckView.isChecked() ? View.VISIBLE : View.GONE);
//		mMobileTipLayout.setVisibility(mMobileCheckView.isChecked() ? View.VISIBLE : View.GONE);
		mPayeeMobileView.setText(StringUtil.valueOf1(mMobileParam));

		// 归集方式
		List<String> imputationData = CollectImputationMode.getImputationValues();
		initSpinner(mCollectModeSpView, imputationData);
		// 归集周期
		List<String> collectCycleData = CollectCycleType.getCycleValues();
		initSpinner(mCollectRuleSpView, collectCycleData);
		mNextView.setOnClickListener(this);
		
		// 执行日
		if (CollectCycleType.DAY.equals(mCycleParam)) {// 日
			mCollectRuleCodeLayout.setVisibility(View.GONE);
		} else if (CollectCycleType.WEEK.equals(mCycleParam)) {// 周
			mCollectRuleCodeLayout.setVisibility(View.VISIBLE);
		} else if (CollectCycleType.MONTH.equals(mCycleParam)) { // 月
			mCollectRuleCodeLayout.setVisibility(View.VISIBLE);
		}
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payeeAccountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerAccountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerNameView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerBankView);
	}

	// 绑定spinner
	private void initSpinner(Spinner spinner, List<String> list) {
		ArrayAdapter<String> accAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
		accAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(accAdapter);
	}

	private boolean getIntentData() {
		CollectData data = CollectData.getInstance();
		Intent intent = getIntent();
		// mPayeeAccount = intent.getStringExtra(Collect.cbAccCard);
		// mPayerAccount = intent.getStringExtra(Collect.payerAccount);
		// mAccounteData = data.getAccountData(mPayeeAccount);
		// mCollectData = data.getCollectData(mPayeeAccount, mPayerAccount);

		mAccountId = intent.getStringExtra(Collect.accountId);
		mAccNumParam = intent.getStringExtra(Collect.cbAccNum);
		mAccCardParam = intent.getStringExtra(Collect.cbAccCard);
		// mAccountNameParam = (String) mAccounteData.get(Comm.ACCOUNT_NAME);
		// mAccountTypeParam = (String) mAccounteData.get(Comm.ACCOUNT_TYPE);
		// mBankNameParam = (String) mAccounteData.get(Comm.BRANCHNAME);

		mRuleNoParam = intent.getStringExtra(Collect.ruleNo);
		mPayerAcntNameParam = intent.getStringExtra(Collect.payerAcntName);
		mPayerAccBankNameParam = intent.getStringExtra(Collect.payerAccBankName);
		mPayerAccountParam = intent.getStringExtra(Collect.payerAccount);

		String stringExtra = intent.getStringExtra(Collect.ifMessage);
		mIfMessageParam = "Y".equalsIgnoreCase(stringExtra);
		mMobileParam = intent.getStringExtra(Collect.cbMobile);
		mAmountParam = intent.getStringExtra(Collect.amount);
		mImputationModeParam = intent.getStringExtra(Collect.imputationMode);
		mCycleParam = intent.getStringExtra(Collect.cycle);
		mCycleCodeParam = intent.getStringExtra(Collect.cycleCode);
		return true;
	}

	private void requestPsnCBMobileQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Collect.PsnCBMobileQuery);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCBMobileQueryCallback");
	}

	public void requestPsnCBMobileQueryCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultData = (Map<String, String>) biiResponseBody.getResult();
		mMobileParam = resultData.get(Collect.cbMobile);
		mPayeeMobileView.setText(mMobileParam);
	}

}
