package com.chinamworld.bocmbci.biz.tran.remit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.remit.adapters.RemitSharedAccAdapter;
import com.chinamworld.bocmbci.biz.tran.remit.adapters.SimpleOnItemSelectedListener;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.Dictionary;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.LinearListView;

/**
 * 套餐签约输入信息页面
 * 
 * @author wangmengmeng
 * 
 */
public class RemitSetMealInputActivity extends TranBaseActivity {

	private static final int Request_Code_AddAcc = 10001;
	/** 输入信息页面 */
	private View view;
	/** 签约账户 */
	private TextView tran_remit_account;
	/** 套餐属性 */
	private Spinner sp_remit_type;
	/** 自动续约套餐类型 */
	private Spinner sp_remit_extension_type;
	/** 套餐起点金额 */
	private EditText remit_amount_view;
	/** 是否自动续约 */
	private RadioGroup rg_extension_flag;
	private RadioButton rb_yes;
	private RadioButton rb_no;
	/** 手机号码 */
	private EditText tran_remit_phone;
	/** 收款付费起点金额布局 */
	private View ll_amount_layout;
	/** 是否自动续约布局 */
	private View ll_extension_flag;
	/** 下一步 */
	private Button nextButton;
	/** 添加按钮 */
	private TextView addButton;
	/** 删除按钮 */
	// private TextView deleteButton;
	/** 共享账户列表 */
	private LinearListView lv_shareAcc;
	/** 共享账户列表 布局 */
	private LinearLayout share_account;

	// private String flag;

	/** 套餐属性 */
	private String remitSetMealProducPropertParam;
	/** 套餐起点金额 */
	private String orignAmountParam;
	/** 套餐类型 */
	/** 套餐描述 */
	private String remitSetMealProductTypeParam;
	private String remitSetMealProductIDescParam;
	/** 套餐ID */
	private String remitSetMealProducPropertyIdParam;
	/** 是否自动续约 (Y|N) */
	private String extensionFlagValueParam;
	/** 手机号 */
	private String phoneNumber;
	/** 选择的签约账户 */
	private Map<String, Object> chooseMap;
	/** 共享账户 */
	private List<Map<String, String>> shareAccList;
	/**
	 * 套餐类型 key: 展示 value:上传 tag:数据
	 * */
	private Dictionary<String, String, Map<String, Object>> remitSetMealTypeResDic = new Dictionary<String, String, Map<String, Object>>();

	/** 优惠后费用 */
	private String afterThePreferentialFee;
	/**显示收款金额*/
	private boolean isShowAmonutLayout;
	/**显示共享账号*/
	private boolean isShowShareAccounLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.trans_remit_menu_one));
		toprightBtn();
		// 添加布局
		view = addView(R.layout.tran_remit_setmeal_input);
		dateTime = this.getIntent().getStringExtra(ConstantGloble.DATE_TIEM);
		chooseMap = TranDataCenter.getInstance().getAtmChooseMap();
		shareAccList = new ArrayList<Map<String, String>>();
		TranDataCenter.getInstance().setShareAccountList(shareAccList);

		// 初始化界面
		init();
		BiiHttpEngine.showProgressDialogCanGoBack();
		requestCommConversationId();
	}

	/** 初始化界面 */
	private void init() {
		tran_remit_account = (TextView) view.findViewById(R.id.tran_remit_account);
		sp_remit_type = (Spinner) view.findViewById(R.id.sp_remit_type);
		sp_remit_extension_type = (Spinner) view.findViewById(R.id.sp_remit_extension_type);
		remit_amount_view = (EditText) view.findViewById(R.id.tran_remit_amount);
		ll_amount_layout = view.findViewById(R.id.ll_extension_amount);
		ll_amount_layout.setVisibility(View.GONE);
		ll_extension_flag = view.findViewById(R.id.ll_extension_flag);
		rg_extension_flag = (RadioGroup) view.findViewById(R.id.yes_or_no);
		rb_yes = (RadioButton) view.findViewById(R.id.yes);
		rb_no = (RadioButton) view.findViewById(R.id.no);
		tran_remit_phone = (EditText) view.findViewById(R.id.tran_remit_phone);
		addButton = (TextView) view.findViewById(R.id.btn_add);
		nextButton = (Button) view.findViewById(R.id.remit_input_next_btn);
		lv_shareAcc = (LinearListView) view.findViewById(R.id.lv_sharedAcc);
		share_account = (LinearLayout) view.findViewById(R.id.view_share_account_input);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) findViewById(R.id.tran_remit_amount_lable));

		SpannableStringBuilder spannedAdd = new SpannableStringBuilder(addButton.getText());
		spannedAdd.setSpan(new URLSpan(""), 0, spannedAdd.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannedAdd.setSpan(new ForegroundColorSpan(Color.RED), 0, spannedAdd.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		addButton.setText(spannedAdd);
		addButton.setOnClickListener(addClicker);
		nextButton.setOnClickListener(nextListener);
		rb_yes.setChecked(true);

		// 赋值
		String account = (String) chooseMap.get(Acc.ACC_ACCOUNTNUMBER_RES);
		tran_remit_account.setText(StringUtil.getForSixForString(account));
		setSpinnnerAdapter(sp_remit_type, RemitSetMealProducDic.getKeys());
		sp_remit_type.setOnItemSelectedListener(new SimpleOnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String key = RemitSetMealProducDic.getKeys().get(position);
				remitSetMealProducPropertParam = RemitSetMealProducDic.getValueFromKey(key);
				isShowAmonutLayout = RemitSetMealProducDic.getTagFromKey(key);
				isShowShareAccounLayout = "1".equals(remitSetMealProducPropertParam); //
				ll_amount_layout.setVisibility(isShowAmonutLayout ? View.VISIBLE : View.GONE);
				//share_account.setVisibility(isShowShareAccount ? View.GONE : View.VISIBLE);
				remit_amount_view.setText("");
				if (isLoadSharedAccountList(remitSetMealProducPropertyIdParam) && isShowShareAccounLayout) {
					share_account.setVisibility(View.VISIBLE);
				} else {
					share_account.setVisibility(View.GONE);
				}
			}
		});
		sp_remit_extension_type.setOnItemSelectedListener(new SimpleOnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String key = remitSetMealTypeResDic.getKeys().get(position);
				remitSetMealProductTypeParam = remitSetMealTypeResDic.getValueFromKey(key);
				Map<String, Object> tag2 = (Map<String, Object>) remitSetMealTypeResDic.getTagFromKey(key);
				String flag = (String) tag2.get(Tran.MealTypeQuery_remitSetMealautoFlag);
				remitSetMealProducPropertyIdParam = (String) tag2.get(Tran.MealTypeQuery_remitSetMealProductId);

				if (isLoadSharedAccountList(remitSetMealProducPropertyIdParam) && isShowShareAccounLayout) {
					share_account.setVisibility(View.VISIBLE);
				} else {
					share_account.setVisibility(View.GONE);
				}

				ll_extension_flag.setVisibility("Y".equalsIgnoreCase(flag) ? View.VISIBLE : View.GONE);
			}
		});
	}

	private void setSpinnnerAdapter(Spinner spinner, List<String> data) {
		ArrayAdapter<String> remitadapter = new ArrayAdapter<String>(this, R.layout.spinner_item, data);
		remitadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(remitadapter);
	}

	/** 添加共享账号 */
	OnClickListener addClicker = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if (shareAccList.size() >= 10) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("您最多只能添加10个账户");
				return;
			}
			// Map<String, Object> tagFromValue =
			// remitSetMealTypeResDic.getTagFromValue(remitSetMealProductTypeParam);
			// remitSetMealProducPropertyIdParam = (String)
			// tagFromValue.get(Tran.MealTypeQuery_remitSetMealProductId);
			// if ("7".equals(remitSetMealProducPropertyIdParam.substring(0,
			// 1))) {
			// BaseDroidApp.getInstanse().showInfoMessageDialog("该套餐不允许增加共享账户");
			// return;
			// }
			Map<String, Object> tagFromValue = remitSetMealTypeResDic.getTagFromValue(remitSetMealProductTypeParam);
			remitSetMealProducPropertyIdParam = (String) tagFromValue.get(Tran.MealTypeQuery_remitSetMealProductId);
			Intent intent = new Intent(RemitSetMealInputActivity.this, RemitAddSharedAccountActivity.class);
			intent.putExtra(Tran.TRAN_REMIT_APPPRE_REMITSETMEALPRODUCPROPERTYID, remitSetMealProducPropertyIdParam);
			startActivityForResult(intent, Request_Code_AddAcc);
		}
	};

	OnClickListener nextListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// 校验手机号 和 输入金额
			phoneNumber = tran_remit_phone.getText().toString().trim();
			orignAmountParam = "0";
//			if (phoneNumber.length() > 11) {
//				BaseDroidApp.getInstanse().showInfoMessageDialog("手机号只能输入11位数字!");
//				return;
//			}
			
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			RegexpBean regShouji = new RegexpBean(RemitSetMealInputActivity.this.getString(R.string.trans_remit_phone_nolabel),
					phoneNumber, "shoujiH_11");
			lists.add(regShouji);
			
			if (ll_amount_layout.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(remit_amount_view.getText().toString().trim())) {
					orignAmountParam = remit_amount_view.getText().toString().trim();
					RegexpBean regAmount = new RegexpBean(getString(R.string.trans_remit_input_amount), orignAmountParam,
							"orignAmountParam");
					lists.add(regAmount);
			}
			
			if (RegexpUtils.regexpData(lists)) {
				BiiHttpEngine.showProgressDialog();
				// requestGetSecurityFactor("PB039");
				/** 优惠后费用 */
				requestRemitSetMealApplyCharge();
			}
		}
	};

	/** 优惠后费用 */
	public void requestRemitSetMealApplyCharge() {
		Map<String, Object> tagFromValue = remitSetMealTypeResDic.getTagFromValue(remitSetMealProductTypeParam);
		remitSetMealProducPropertyIdParam = (String) tagFromValue.get(Tran.MealTypeQuery_remitSetMealProductId);
		remitSetMealProductIDescParam = (String) tagFromValue.get(Tran.MealTypeQuery_remitSetMealProductIDesc);
		extensionFlagValueParam = (ll_extension_flag.getVisibility() == View.VISIBLE && rb_yes.isChecked()) ? "Y" : "N";

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.RemitSetMealApplyCharge_API);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		String accountID = (String) chooseMap.get(Acc.ACC_ACCOUNTID_RES);
		paramsmap.put(Tran.AfterThePreferentialFee_accountId, accountID);
		paramsmap.put(Tran.AfterThePreferentialFee_remitSetMealProductType, remitSetMealProductTypeParam);
		paramsmap.put(Tran.AfterThePreferentialFee_extensionFlag, extensionFlagValueParam);
		paramsmap.put(Tran.AfterThePreferentialFee_effecttiveDate, QueryDateUtils.getcurrentDate(dateTime));
		if (isLoadSharedAccountList(remitSetMealProducPropertyIdParam) && isShowShareAccounLayout) {
			paramsmap.put(Tran.TRAN_REMIT_APP_SHAREDACCOUNTLIST_REQ, shareAccList);
		}
		paramsmap.put(Tran.AfterThePreferentialFee_remitSetMealProducProperty, remitSetMealProducPropertParam);
		paramsmap.put(Tran.AfterThePreferentialFee_orignAmount, orignAmountParam);
		paramsmap.put(Tran.AfterThePreferentialFee_curCde, "001");
		paramsmap.put("remitSetMealProducPropertyId", remitSetMealProducPropertyIdParam);
		paramsmap.put("remitSetMealProductIDesc", remitSetMealProductIDescParam);

		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestRemitSetMealApplyChargeCallBack");
	}

	/** 优惠后费用回调 */
	public void requestRemitSetMealApplyChargeCallBack(Object resultObj) {
		//BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		// 拼装交易
		/** 应收费用 */
		// accountsReceivableCost = (String)
		// map.get(Tran.AfterThePreferentialFee_needCommissionCharge);
		String getChargeFlag = (String) result.get(Tran.AfterThePreferentialFee_getChargeFlag);

		// String chargeFlag = (String) result.get("getChargeFlag");
		if ("1".equals(getChargeFlag)) {
			afterThePreferentialFee = (String) result.get(Tran.AfterThePreferentialFee_preCommissionCharge);
		} else {
			afterThePreferentialFee = "";
		}

		// 获取安全因子组合
		requestGetSecurityFactor("PB039");
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestRemitSetMealTypeQuery();
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 进行签约预交易
				requestSetMealPre();
			}
		});
	}

	/** 刷新共享账户列表 */
	public void refreshListView() {
		shareAccList = TranDataCenter.getInstance().getShareAccountList();
		RemitSharedAccAdapter adapter = new RemitSharedAccAdapter(RemitSetMealInputActivity.this, shareAccList);
		adapter.setDeleteOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 对话框二次确认
				Map<String, String> tag = (Map<String, String>) v.getTag();
				shareAccList.remove(tag);
				refreshListView();
			}
		});
		lv_shareAcc.setAdapter(adapter);
	}

	/** 汇款套餐类型查询 */
	public void requestRemitSetMealTypeQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.RemitSetMealTypeQuery_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		String accountID = (String) chooseMap.get(Acc.ACC_ACCOUNTID_RES);
		paramsmap.put(Tran.ACCOUNTID_RES, accountID);
		paramsmap.put(Tran.TRAN_PAGESIZE_REQ, Integer.MAX_VALUE);
		paramsmap.put(Tran.TRAN_CURRENTINDEX_REQ, 0);
		paramsmap.put(Tran.TRAN_REFRESH_REQ, String.valueOf(true));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestRemitSetMealTypeQueryCallBack");
	}

	/** 汇款套餐类型查询回调 */
	public void requestRemitSetMealTypeQueryCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		List<Map<String, Object>> mealtypeList = (List<Map<String, Object>>) result.get(Tran.MealTypeQuery_MealtypeList);
		for (int i = 0; i < mealtypeList.size(); i++) {
			Map<String, Object> map = mealtypeList.get(i);
			String remitSetMealProductICount = (String) map.get(Tran.MealTypeQuery_remitSetMealProductICount);
			String remitSetMealProductIMonth = (String) map.get(Tran.MealTypeQuery_remitSetMealProductIMonth);
			String amount = (String) map.get(Tran.MealTypeQuery_amount);
			String mount = amount.substring(0, amount.lastIndexOf(".")+3);
			String value = remitSetMealProductICount + "/" + remitSetMealProductIMonth + "/" + mount;
			String key = encodeMealType(value);
			remitSetMealTypeResDic.put(key, value, map);
		}
		setSpinnnerAdapter(sp_remit_extension_type, remitSetMealTypeResDic.getKeys());
	}

	/**
	 * 请求签约预交易
	 */
	public void requestSetMealPre() {
		Map<String, Object> loginMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String cusName = (String) loginMap.get(Login.CUSTOMER_NAME);

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.REMITSETMEALAPPLYPRE_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));

		Map<String, Object> paramsmap = new HashMap<String, Object>();
		String accountID = (String) chooseMap.get(Acc.ACC_ACCOUNTID_RES);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_ACCOUNTID_REQ, accountID);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_REMITSETMEALPRODUCTTYPE_REQ, remitSetMealProductTypeParam);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_PHONENUMBER_REQ, phoneNumber);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_EFFECTTIVEDATE_REQ, QueryDateUtils.getcurrentDate(dateTime));
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_NAME_REQ, cusName);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_REMITSETMEALPRODUCPROPERTY_REQ, remitSetMealProducPropertParam);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_ORIGNAMOUNT_REQ, orignAmountParam);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_CURCDE_REQ, "001");
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_REMITSETMEALPRODUCPROPERTYID, remitSetMealProducPropertyIdParam);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_REMITSETMEALPRODUCTIDESC, remitSetMealProductIDescParam);
		
		// paramsmap.put(Tran.TRAN_REMIT_APP_EXTENSIONFLAG_REQ,
		// extensionFlagValueParam);
		// paramsmap.put(Tran.TRAN_REMIT_APP_SHAREDACCOUNTLIST_REQ,
		// shareAccList);

		paramsmap.put(Tran.TRAN_REMIT_APPPRE_COMBINID_REQ, BaseDroidApp.getInstanse().getSecurityChoosed());
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestSetMealPreCallBack");
	}

	/**
	 * 请求签约预交易回调
	 * 
	 * @param resultObj
	 */
	public void requestSetMealPreCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		// 拼装交易
		String accountID = (String) chooseMap.get(Acc.ACC_ACCOUNTID_RES);
		inputMap.put(Tran.TRAN_REMIT_APP_ACCOUNTID_REQ, accountID);
		inputMap.put(Tran.TRAN_REMIT_APP_REMITSETMEALPRODUCTTYPE_REQ, remitSetMealProductTypeParam);
		inputMap.put(Tran.TRAN_REMIT_APP_EXTENSIONFLAG_REQ, extensionFlagValueParam);
		inputMap.put(Tran.TRAN_REMIT_APP_PHONENUMBER_REQ, phoneNumber);
		inputMap.put(Tran.TRAN_REMIT_APP_EFFECTTIVEDATE_REQ, QueryDateUtils.getcurrentDate(dateTime));
		if (isLoadSharedAccountList(remitSetMealProducPropertyIdParam) && isShowShareAccounLayout) {
			inputMap.put(Tran.TRAN_REMIT_APP_SHAREDACCOUNTLIST_REQ, shareAccList);
		}
		inputMap.put(Tran.TRAN_REMIT_APP_REMITSETMEALPRODUCPROPERTY_REQ, remitSetMealProducPropertParam);
		inputMap.put(Tran.TRAN_REMIT_APP_ORIGNAMOUNT_REQ, orignAmountParam);
		inputMap.put(Tran.TRAN_REMIT_APP_CURCDE_REQ, "001");
		inputMap.put(Tran.TRAN_REMIT_APPPRE_REMITSETMEALPRODUCPROPERTYID, remitSetMealProducPropertyIdParam);
		inputMap.put(Tran.TRAN_REMIT_APPPRE_REMITSETMEALPRODUCTIDESC, remitSetMealProductIDescParam);

		TranDataCenter.getInstance().setRemitPreMap(result);
		TranDataCenter.getInstance().setShareAccountList(shareAccList);
		TranDataCenter.getInstance().setShareInputMap(inputMap);
		TranDataCenter.getInstance().setMealTypeResDic(remitSetMealTypeResDic);
		// 进入确认页面
		Intent intent = new Intent(this, RemitSetMealConfirmActivity.class);
		
		
		intent.putExtra(Tran.AfterThePreferentialFee_preCommissionCharge, afterThePreferentialFee);
		intent.putExtra("isShowShareAccount", isShowShareAccounLayout);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Request_Code_AddAcc && resultCode == RESULT_OK) {
			refreshListView();
		}
	}

}