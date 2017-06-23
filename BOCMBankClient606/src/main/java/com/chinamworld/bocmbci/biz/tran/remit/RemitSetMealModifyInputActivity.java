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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.chinamworld.bocmbci.biz.tran.remit.adapters.RemitModifyDelAccAdapter;
import com.chinamworld.bocmbci.biz.tran.remit.adapters.RemitSharedAccAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.Dictionary;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.LinearListView;

/**
 * 套餐签约修改输入信息页面
 * 
 * @author wangmengmeng
 * @alter luqipeng
 * 
 */
public class RemitSetMealModifyInputActivity extends TranBaseActivity {

	private static final int Request_Code_AddAcc = 10001;

	/** 修改输入信息页面 */
	private View view;
	/** 签约账户 */
	private TextView tran_remit_account;
	/** 套餐属性 */
	private TextView remit_type_view;
	/** 自动续约套餐类型 */
	private TextView remit_extension_type_view;
	/** 收款付费起点金额布局 */
	private View ll_amount_layout;
	/** 套餐起点金额 */
	private EditText remit_amount_view;
	/** 是否自动续约布局 */
	private View ll_extension_flag;
	/** 是否自动续约 显示 */
	private TextView tv_extension_flag;
	/** 是否自动续约 */
	private RadioGroup rg_extension_flag;
	private RadioButton rb_yes;
	private RadioButton rb_no;
	/** 手机号码 */
	private EditText tran_remit_phone;
	/** 生效日期 */
	private TextView startDateText;
	/** 截止日期 */
	private TextView endDateText;
	/** 添加按钮 */
	private TextView addButton;
	/** 共享账户列表 */
	private LinearListView lv_addshareAcc;
	/** 共享账户列表 */
	private LinearListView lv_shareAcc;
	/** 下一步 */
	private Button nextButton;
	/** 共享账户 布局 */
	private LinearLayout remit_setmeal_attribute;
	/** 用户添加共享账户 */
	private List<Map<String, String>> shareAccList = new ArrayList<Map<String, String>>();
	/** 选择的签约账户 */
	private Map<String, Object> chooseMap;
	/** 查询结果 */
	private Map<String, Object> queryMap;
	/** 查询结果中的基本信息 */
	private Map<String, String> entity = new HashMap<String, String>();
	/** 返回的有效账户列表 */
	private List<Map<String, String>> validAccList = new ArrayList<Map<String, String>>();
	/** 选中序号列表 */
	private List<Integer> select = new ArrayList<Integer>();
	/** 手机号 */
	private String phoneNumberParam;
	/** 收款付费起点金额 */
	private String orignAmountParam;
	/** 套餐编号*/
	private String comboNumber;
	/** 是否自动续约*/
	private String extensionFlagParam = null;
	/**
	 * 套餐类型 key: 展示 value:上传 tag:数据
	 * */
	private Dictionary<String, String, Map<String, Object>> remitSetMealTypeResDic = new Dictionary<String, String, Map<String, Object>>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.trans_remit_menu_two));
		toprightBtn();
		// 添加布局
		view = addView(R.layout.tran_remit_setmeal_modify_input);
		// 初始化界面
		queryMap = TranDataCenter.getInstance().getShareQueryMap();
		chooseMap = TranDataCenter.getInstance().getAtmChooseMap();
		entity = (Map<String, String>) queryMap.get(Tran.TRAN_PAYSETMEALENTITY_RES);
		List<Map<String, String>> accList = (List<Map<String, String>>) queryMap.get(Tran.TRAN_VALIDACCOUNTLIST_RES);
		for(int i = 0; i < accList.size() ;i++){
			Map<String, String> accListMap = accList.get(i);
			if("S".equals(accListMap.get("remitSetMealAccountType")) && "Y".equals(accListMap.get("shareStatus"))){
				validAccList.add(accListMap);
			}
		}
		
		TranDataCenter.getInstance().setShareAccountList(shareAccList);

		init();
	}

	/** 初始化界面 */
	private void init() {

		tran_remit_account = (TextView) view.findViewById(R.id.tran_remit_account);
		remit_type_view = (TextView) view.findViewById(R.id.sp_remit_type);
		remit_extension_type_view = (TextView) view.findViewById(R.id.remit_extension_type);
		remit_amount_view = (EditText) view.findViewById(R.id.tran_remit_amount);
		ll_amount_layout = view.findViewById(R.id.ll_extension_amount);
		ll_extension_flag = view.findViewById(R.id.ll_extension_flag);
		tv_extension_flag = (TextView) view.findViewById(R.id.tv_yes_or_no);
		rg_extension_flag = (RadioGroup) view.findViewById(R.id.yes_or_no);
		rb_yes = (RadioButton) view.findViewById(R.id.yes);
		rb_no = (RadioButton) view.findViewById(R.id.no);
		tran_remit_phone = (EditText) view.findViewById(R.id.tran_remit_phone);
		startDateText = (TextView) view.findViewById(R.id.tv_remit_valuedate);
		endDateText = (TextView) view.findViewById(R.id.tv_remit_enddate);
		lv_addshareAcc = (LinearListView) view.findViewById(R.id.lv_modify_sharedAcc);
		lv_shareAcc = (LinearListView) view.findViewById(R.id.lv_sharedAcc);
		addButton = (TextView) view.findViewById(R.id.tv_account_add);
		nextButton = (Button) view.findViewById(R.id.remit_input_next_btn);
		remit_setmeal_attribute = (LinearLayout) view.findViewById(R.id.remit_setmeal_attribute);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) findViewById(R.id.tran_remit_amount_lable));

		SpannableStringBuilder spannedAdd = new SpannableStringBuilder(addButton.getText());
		spannedAdd.setSpan(new URLSpan(""), 0, spannedAdd.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannedAdd.setSpan(new ForegroundColorSpan(Color.RED), 0, spannedAdd.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		addButton.setText(spannedAdd);
		addButton.setOnClickListener(addListener);
		nextButton.setOnClickListener(nextListener);

		// 赋值
		String account = (String) chooseMap.get(Acc.ACC_ACCOUNTNUMBER_RES);
		tran_remit_account.setText(StringUtil.getForSixForString(account));
		remit_type_view.setText(RemitSetMealProducDic.getKeyFromValue(entity.get(Tran.TRAN_REMITSETMEALPRODUCPROPERTY)));
		remit_extension_type_view.setText(encodeMealTypEcutout2(entity.get(Tran.TRAN_SIGNTYPE_RES)));
		ll_amount_layout
				.setVisibility(RemitSetMealProducDic.getTagFromValue(entity.get(Tran.TRAN_REMITSETMEALPRODUCPROPERTY)) ? View.VISIBLE
						: View.GONE);
		remit_amount_view.setText(StringUtil.parseStringPattern(entity.get(Tran.TRAN_ORIGNAMOUNT), 2));
		
		/** 共享账户列表 (根据返回结果反显(默认返回false) 付款方套餐显示  收款方套餐和双向套餐不显示) */
		if (RemitSetMealProducDic.getTagFromValue((String) entity
				.get(Tran.TRAN_REMIT_APP_REMITSETMEALPRODUCPROPERTY_REQ))) {
				remit_setmeal_attribute.setVisibility(View.GONE);
		} else {
				remit_setmeal_attribute.setVisibility(View.VISIBLE);
		}
		
		// 根据账户判断是否支持自动续约，不支持则不显示此项；如果账户支持自动续约并且已设定自动续约，反显信息，如果已设定非自动续约，则提供客户选择
		extensionFlagParam = "N";
		if ("Y".equalsIgnoreCase(entity.get("remitSetMealautoFlag"))) {
			// 支持自动续约
			ll_extension_flag.setVisibility(View.VISIBLE);
			if ("Y".equalsIgnoreCase(entity.get("extensionFlag"))) {
				// 已设定非自动续约,续约布局根据账号是否支持续约进行显示隐藏
				tv_extension_flag.setText("是");
				tv_extension_flag.setVisibility(View.VISIBLE);
				rg_extension_flag.setVisibility(View.GONE);
				rb_yes.setChecked(true);
				extensionFlagParam = "Y";
			} else {
				tv_extension_flag.setVisibility(View.GONE);
				rg_extension_flag.setVisibility(View.VISIBLE);
				rb_no.setChecked(true);
				tv_extension_flag.setText("否");
			}
		} else {
			// 不支持自动续约
			ll_extension_flag.setVisibility(View.GONE);
		}

		tran_remit_phone.setText(entity.get("mobile"));
		startDateText.setText(entity.get("startDate"));
		endDateText.setText(entity.get("endDate"));

		//带解除的共享账号
		LinearLayout ll_sharedAcc = (LinearLayout) view.findViewById(R.id.ll_sharedAcc);

		//待解除的共享账号
		if (!StringUtil.isNullOrEmpty(validAccList)) {
			/** 是否选中 true代表可选,false代表不可选 */
			final RemitModifyDelAccAdapter adapter = new RemitModifyDelAccAdapter(RemitSetMealModifyInputActivity.this,
					validAccList, true);
			lv_shareAcc.setAdapter(adapter);
			ll_sharedAcc.setVisibility(View.VISIBLE);
		} else {
			ll_sharedAcc.setVisibility(View.GONE);
		}
	}

	/** 刷新共享账户列表 */
	public void refreshListView() {
		shareAccList = TranDataCenter.getInstance().getShareAccountList();

		RemitSharedAccAdapter adapter = new RemitSharedAccAdapter(this, shareAccList);
		adapter.setDeleteOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 对话框二次确认
				Map<String, String> tag = (Map<String, String>) v.getTag();
				shareAccList.remove(tag);
				refreshListView();
			}
		});
		lv_addshareAcc.setAdapter(adapter);
	}

	/** 添加共享账户 */
	OnClickListener addListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO 数量判断
			if (validAccList.size() + shareAccList.size() >= 10) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("您最多只能添加10个账户");
				return;
			}
			//comboNumber.charAt(0) == 7
			comboNumber = entity.get("remitSetMealProductId");
			if ("7".equals(comboNumber.substring(0, 1))) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("该套餐不允许增加共享账户");
				return;
			}
			Intent intent = new Intent(RemitSetMealModifyInputActivity.this, RemitAddSharedAccountActivity.class);
			intent.putExtra(Tran.TRAN_REMIT_APPPRE_REMITSETMEALPRODUCPROPERTYID, comboNumber);
			startActivityForResult(intent, Request_Code_AddAcc);
		}
	};
	OnClickListener nextListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) { 
			phoneNumberParam = tran_remit_phone.getText().toString().trim();
			orignAmountParam = "0";
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			RegexpBean regShouji = new RegexpBean(getString(R.string.trans_remit_phone_nolabel),
					phoneNumberParam, "shoujiH_11");
			lists.add(regShouji);
			
			if (ll_amount_layout.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(remit_amount_view.getText().toString().trim())) {
				String orignAmount = remit_amount_view.getText().toString().trim();
				orignAmountParam = orignAmount.replaceAll(",", "");
					RegexpBean regAmount = new RegexpBean(getString(R.string.trans_remit_input_amount), orignAmountParam,
							"orignAmountParam");
					lists.add(regAmount);
			}
			
			if (RegexpUtils.regexpData(lists)) {
				// 请求预交易
				requestCommConversationId();
				BiiHttpEngine.showProgressDialog();
			}
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestGetSecurityFactor("PB039");
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 进行签约预交易
				requestSetMealModifyPre();
			}
		});
	}

	/**
	 * 请求签约修改预交易
	 */
	public void requestSetMealModifyPre() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRAN_REMITSETMEALMODIFYPRE_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		String accountID = (String) chooseMap.get(Acc.ACC_ACCOUNTID_RES);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_ACCOUNTID_REQ, accountID);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_REMITSETMEALPRODUCTTYPE_REQ, entity.get(Tran.TRAN_SIGNTYPE_RES));
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_PHONENUMBER_REQ, phoneNumberParam);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_REMITSETMEALPRODUCPROPERTY_REQ, entity.get("remitSetMealProducProperty"));
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_EFFECTTIVEDATE_REQ, (String) entity.get(Tran.TRAN_STARTDATE_RES));
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_ORIGNAMOUNT_REQ, orignAmountParam);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_CURCDE_REQ, "001");
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_REMITSETMEALPRODUCPROPERTYID, entity.get("remitSetMealProductId"));
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_REMITSETMEALPRODUCTIDESC, entity.get("remitSetMealProductIDesc"));
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_REMITSETMEALACCOUNTTYPE_REQ, entity.get("remitSetMealAccountType"));
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_SHARESTATUS_REQ, entity.get("shareStatus"));
		
		
		Map<String, Object> loginMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String cusName = (String) loginMap.get(Login.CUSTOMER_NAME);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_NAME_REQ, cusName);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_COMBINID_REQ, BaseDroidApp.getInstanse().getSecurityChoosed());
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestSetMealModifyPreCallBack");
	}

	/**
	 * 请求签约修改预交易回调
	 * 
	 * @param resultObj
	 */
	public void requestSetMealModifyPreCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		TranDataCenter.getInstance().setAtmpremap(result);
		TranDataCenter.getInstance().setShareAccountList(shareAccList);
		// 进入确认页面

		// 修改账户列表
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		RemitModifyDelAccAdapter modifyAdapter = (RemitModifyDelAccAdapter) lv_shareAcc.getAdapter();
		if (modifyAdapter != null && modifyAdapter.getModifyRequestList() != null) {
			list.addAll(modifyAdapter.getModifyRequestList());
		}

		RemitSharedAccAdapter addAdapter = (RemitSharedAccAdapter) lv_addshareAcc.getAdapter();
		if (addAdapter != null && addAdapter.getModifyRequestList() != null) {
			list.addAll(addAdapter.getModifyRequestList());
		}

		
		if (rg_extension_flag.getVisibility() == View.VISIBLE) {
			if(rb_yes.isChecked()){
				extensionFlagParam = "Y";
			}else{
				extensionFlagParam = "N";
			}
		}

		Intent intent = new Intent(this, RemitSetMealModifyConfirmActivity.class);
		intent.putExtra("phoneNumber", phoneNumberParam);
		intent.putExtra("orignAmount", orignAmountParam);
		intent.putExtra("extensionFlagParam", extensionFlagParam);
		intent.putExtra("toDelList", list);
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
