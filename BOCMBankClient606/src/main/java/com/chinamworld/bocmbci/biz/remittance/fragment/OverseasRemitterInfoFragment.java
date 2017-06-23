package com.chinamworld.bocmbci.biz.remittance.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.RemittanceContent;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.remittance.adapter.AccAdapter;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.OverseasChinaBankRemittanceInfoInputActivity;
import com.chinamworld.bocmbci.biz.remittance.dialog.AccDetailDialogActivity;
import com.chinamworld.bocmbci.biz.remittance.interfaces.AccChangeListener;
import com.chinamworld.bocmbci.biz.remittance.interfaces.AccDetailListnenr;
import com.chinamworld.bocmbci.biz.remittance.interfaces.AccListCallBackInterface;
import com.chinamworld.bocmbci.biz.remittance.interfaces.AccSelectionListener;
import com.chinamworld.bocmbci.biz.sbremit.mysbremit.ChooseAccountActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 汇款人信息填写片段
 * 
 * @author Zhi
 */
public class OverseasRemitterInfoFragment extends Fragment implements AccDetailListnenr, AccListCallBackInterface,AccSelectionListener {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final String TAG = "OverseasRemitterInfoFragment";
	/** 片段所依附的Activity */
	private OverseasChinaBankRemittanceInfoInputActivity activity;
	/** 此片段状态 0-汇款流程 1-发起汇款流程 */
	private int state;
	/** 是否查询汇款人名称 */
	private boolean  isQuery;
	/** 如果是发起汇款流程，模板详情放到这里 */
	private Map<String, Object> detailMap;
	/** 片段视图 */
	private View mMainView;
	/** 扣款账户列表 */
	private List<Map<String, Object>> accList;
	/** 扣款账户列表数据 */
	private List<String> accStrList;
	/** 选择扣款账户下拉框 */
	private Spinner spPayAcct;
	/** 用户选择的账户下标 */
	private int position;
	/** 汇款人名称 */
	private TextView tvName;
	/** 汇款人名称（英文或拼音） */
	private EditText etName;
	/** 地址 */
	private EditText etAdress;
	/** 邮编 */
	private EditText etPost;
	/** 电话 */
	private EditText etPhone;
	/** 要根据选择扣款账户作出响应的页面或片段 */
	private AccChangeListener[] accChangeView;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

//	/**
//	 * 构造方法
//	 *
//	 * @param activity
//	 *            此片段依附的activity
//	 */
//	public OverseasRemitterInfoFragment(OverseasChinaBankRemittanceInfoInputActivity activity) {
//		this.activity = activity;
//	}

	public void spPayAcctSetZero() {
		spPayAcct.setSelection(0,false);
	}

	@Override
	public void onAttach(Activity activity) {
		this.activity = (OverseasChinaBankRemittanceInfoInputActivity)activity;
		super.onAttach(activity);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.remittance_info_input_overremitter, null);
		spPayAcct = (Spinner) mMainView.findViewById(R.id.sp_payAcct);
		List<String> mList = new ArrayList<String>();
		mList.add("请选择扣款账户");
		AccAdapter accAdapter = new AccAdapter(activity, mList);
		spPayAcct.setAdapter(accAdapter);
		tvName = (TextView) mMainView.findViewById(R.id.tv_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(activity, tvName);
		etName = (EditText) mMainView.findViewById(R.id.et_name);
		etAdress = (EditText) mMainView.findViewById(R.id.et_adress);
		etPost = (EditText) mMainView.findViewById(R.id.et_post);
		etPhone = (EditText) mMainView.findViewById(R.id.et_phone);

		mMainView.findViewById(R.id.btn_accDetail).setOnClickListener(accDetailClickListener);
		mMainView.findViewById(R.id.btn_use_template).setOnClickListener(useTemplateListener);
		
		EditTextUtils.setLengthMatcher(getActivity(), etName, 35);
		EditTextUtils.setLengthMatcher(getActivity(), etAdress, 84);
		EditTextUtils.setLengthMatcher(getActivity(), etPost, 6);
		EditTextUtils.setLengthMatcher(getActivity(), etPhone, 15);

		mMainView.findViewById(R.id.tv_there).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转结汇购汇
				startActivity(new Intent(activity, ChooseAccountActivity.class));
			}
		});


		return mMainView;
	}

	@SuppressWarnings("unchecked")
	private void initViewData() {
		tvName.setText(StringUtil.valueOf1((String) ((Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA)).get(Inves.CUSTOMERNAME)));

		if (state == 1) {
			// 先清空数据，防止多次选择模板时数据混
			clearViewData();

			detailMap = RemittanceDataCenter.getInstance().getMapPsnInternationalTransferTemplateDetailQuery();
			String isLinked = (String) detailMap.get(Remittance.SWIFTACCLINKED);
			if("true".equals(isLinked)) {
				spPayAcct.setSelection(accStrList.indexOf(StringUtil.getForSixForString(
						(String) RemittanceDataCenter.getInstance().getMapPsnInternationalTransferTemplateDetailQuery().get(Remittance.SWIFTACCOUNTNUMBER))));
				spPayAcct.getOnItemSelectedListener().onItemSelected(null, null, spPayAcct.getSelectedItemPosition(), 0);
			} else {
				spPayAcct.setSelection(0);
			}

			etName.setText((String) detailMap.get(Remittance.REMITTORNAME));
			String str = (String) detailMap.get(Remittance.REMITTORADDRESS);
			int end = str.indexOf(",CHINA");
			if (end < 0) {
				etAdress.setText(str);
			} else {
				etAdress.setText(str.substring(0, end));
			}
			etPost.setText((String) detailMap.get(Remittance.REMITTERSZIP));
			etPhone.setText((String) detailMap.get(Remittance.PAYERPHONE));
			if(spPayAcct.getSelectedItemPosition() !=0 ){
				position = spPayAcct.getSelectedItemPosition();
				if (!StringUtil.isNullOrEmpty(RemittanceDataCenter.getInstance().getAccDetail())) {
					RemittanceDataCenter.getInstance().getAccDetail().clear();
				}

				if(isQuery){
					Map<String, Object> params = new HashMap<String, Object>();
					params.put(Comm.ACCOUNT_ID, accList.get(spPayAcct.getSelectedItemPosition() - 1).get(Comm.ACCOUNT_ID));
					BaseHttpEngine.showProgressDialog();
					activity.getHttpTools().requestHttp(Remittance.PSNOTHERNAMEOFCUSTQUERY, "requestPsnOtherNameOfCustQueryCallBack", params, false);
				}
			}
		}
	}

	/** 清空数据 */
	private void clearViewData() {
		spPayAcct.setSelection(0);
		etName.setText("");
		etAdress.setText("");
		etPost.setText("");
		etPhone.setText("");
	}

	/** 设置要根据选择扣款账户作出响应的页面 */
	public void setAccChangeView(AccChangeListener[] accChangeView) {
		this.accChangeView = accChangeView;
	}

	/** 跨境汇款预交易操作，校验字段，准备上送参数 */
	public boolean remittanceConfirm() {
		if (submitRegexp(true)) {
			Map<String, Object> params = RemittanceDataCenter.getInstance().getSubmitParams();
			Map<String, Object> userInput = RemittanceDataCenter.getInstance().getUserInput();
			params.put(Remittance.SWIFTACCOUNTID, accList.get(spPayAcct.getSelectedItemPosition() - 1).get(Acc.ACC_ACCOUNTID));
			params.put(Remittance.SWIFTACCOUNTNUMBER, accList.get(spPayAcct.getSelectedItemPosition() - 1).get(Acc.ACC_ACCOUNTNUMBER_RES));
			params.put(Remittance.REMITTORNAME, (etName.getText().toString().trim()).toUpperCase());
			userInput.put(Remittance.REMITTORNAME, etName.getText().toString().trim());
			params.put(Remittance.REMITTORADDRESS, (etAdress.getText().toString().trim()).toUpperCase() + ",CHINA");
			userInput.put(Remittance.REMITTORADDRESS, etAdress.getText().toString().trim() + ",CHINA");
			params.put(Remittance.REMITTERSZIP, etPost.getText().toString().trim());
			params.put(Remittance.PAYERPHONE, etPhone.getText().toString().trim().toUpperCase());
			userInput.put(Remittance.PAYERPHONE, etPhone.getText().toString().trim());
			LogGloble.i(TAG, params.toString());
			return true;
		}
		return false;
	}

	/** 校验 */
	private boolean submitRegexp(boolean required) {
		if (spPayAcct.getSelectedItemPosition() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择扣款账户");
			return false;
		}
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		if (onlyRegular(required, etName.getText().toString().trim())) {
			RegexpBean name = new RegexpBean(RemittanceContent.REMITTERNAME_CN, etName.getText().toString().trim(), RemittanceContent.REMITTERNAME);
			lists.add(name);
			LogGloble.i(TAG, name.toString());
		}
		if (onlyRegular(required, etAdress.getText().toString().trim())) {
			RegexpBean address = new RegexpBean(RemittanceContent.REMITTERADDRESS_CN, etAdress.getText().toString().trim(), RemittanceContent.REMITTERPAYEEADRESS);
			lists.add(address);
			LogGloble.i(TAG, address.toString());
		}
		if (onlyRegular(required, etPost.getText().toString().trim())) {
			RegexpBean post = new RegexpBean(RemittanceContent.REMITTERPOST_CN, etPost.getText().toString().trim(), RemittanceContent.REMITTANCECODE);
			lists.add(post);
			LogGloble.i(TAG, post.toString());
		}
		if (onlyRegular(required, etPhone.getText().toString().trim())) {
			RegexpBean phone = new RegexpBean(RemittanceContent.REMITTERPHONE_CN, etPhone.getText().toString().trim(), RemittanceContent.REMITTERPHONE);
			lists.add(phone);
			LogGloble.i(TAG, phone.toString());
		}
		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}
		return true;
	}

	/** 只作正则校验 */
	private boolean onlyRegular(Boolean required, String content) {
		if ((!required && !StringUtil.isNull(content)) || required) {
			return true;
		}
		return false;
	}

	@Override
	public void detailCallBack(List<Map<String, Object>> detailList) {
		startActivity(new Intent(activity, AccDetailDialogActivity.class));
	}

	@Override
	public void accListCallBack() {
		// 初始化扣款账户下拉框
		accList = RemittanceDataCenter.getInstance().getAccList();
		LogGloble.i(TAG, accList.toString());
		spPayAcct = (Spinner) mMainView.findViewById(R.id.sp_payAcct);
		accStrList = PublicTools.getSpinnerDataWithDefaultValue(accList, Comm.ACCOUNTNUMBER, "请选择扣款账户");
		AccAdapter mAdapter=new AccAdapter(activity, accStrList);

		spPayAcct.setAdapter(mAdapter);
		if (state == 1) {
			String swiftAccountNumber = StringUtil.getForSixForString((String) RemittanceDataCenter.getInstance().getMapPsnInternationalTransferTemplateDetailQuery().get(Remittance.SWIFTACCOUNTNUMBER));
			if (accStrList.contains(swiftAccountNumber)) {
				String isLinked = (String) RemittanceDataCenter.getInstance().getMapPsnInternationalTransferTemplateDetailQuery().get(Remittance.SWIFTACCLINKED);
				if ("true".equals(isLinked)) {
					spPayAcct.setSelection(accStrList.indexOf(swiftAccountNumber));
				} else {
					spPayAcct.setSelection(0);
				}
				
			} else {
				spPayAcct.setSelection(0);
			}
		}
		spPayAcct.setOnItemSelectedListener(chooseAccListener);
		state = getActivity().getIntent().getBooleanExtra(RemittanceContent.JUMPFLAG, false) ? 1 : 0;
		initViewData();
	}

	/** 设置汇款人名称（英文或拼音） */
	public void setRemitterName(String name) {
		if (!StringUtil.isNull(name) && ((AccAdapter) spPayAcct.getAdapter()).isTouch()) {
			etName.setText(name);
		}

		for (int i = 0; i < accChangeView.length; i++) {
			if (position == 0) {
				accChangeView[i].accChange("");
			} else {
				accChangeView[i].accChange((String) accList.get(position - 1).get(Comm.ACCOUNT_ID));
			}
		}
	}

	/** 设置片段状态 0-汇款流程 1-发起汇款 */
	public void setState(int state, boolean isNowNotify ,boolean isQuery) {
		this.state = state;
		this.isQuery = isQuery;
		if (isNowNotify) {
			initViewData();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// TODO
		// --------------------------------------------控件事件-------------------------------------------------//
		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////


		/** 选择扣款账户监听 */
		private OnItemSelectedListener chooseAccListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 0) {
					for (int i = 0; i < accChangeView.length; i++) {
						accChangeView[i].accChange("");
					}
					return;
				}
				position = arg2;
				if (!StringUtil.isNullOrEmpty(RemittanceDataCenter.getInstance().getAccDetail())) {
					RemittanceDataCenter.getInstance().getAccDetail().clear();
				}
				if(state == 1 && spPayAcct.getSelectedItemPosition() != accStrList.indexOf(StringUtil.getForSixForString(
						(String) RemittanceDataCenter.getInstance().getMapPsnInternationalTransferTemplateDetailQuery().get(Remittance.SWIFTACCOUNTNUMBER)))){
					isQuery = true;
				}
				if(isQuery) {

					//Log.i("BiiHttpEngine", "1------dianji-1");
					Map<String, Object> params = new HashMap<String, Object>();
					params.put(Comm.ACCOUNT_ID, accList.get(arg2 - 1).get(Comm.ACCOUNT_ID));
					BaseHttpEngine.showProgressDialog();
					activity.getHttpTools().requestHttp(Remittance.PSNOTHERNAMEOFCUSTQUERY, "requestPsnOtherNameOfCustQueryCallBack", params, false);
				} else {
					for (int i = 0; i < accChangeView.length; i++) {
						if (position == 0) {
							accChangeView[i].accChange("");
						} else {
							accChangeView[i].accChange((String) accList.get(position - 1).get(Comm.ACCOUNT_ID));

							Log.i("100", "1------dianji");
						}
					}
					BaseHttpEngine.showProgressDialog();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put(Comm.ACCOUNT_ID, accList.get(spPayAcct.getSelectedItemPosition() - 1).get(Comm.ACCOUNT_ID));
					Log.i("100", "1------dianji2");
					activity.getHttpTools().requestHttp(Acc.QRY_ACC_BALANCE_API, "requestPsnAccountQueryAccountDetailCallBack", params, false);
					activity.getHttpTools().registErrorCode(Acc.QRY_ACC_BALANCE_API,"AccQueryDetailAction.NoSubAccount");
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		};

	/** 账户详情按钮监听 */
	private OnClickListener accDetailClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (spPayAcct.getSelectedItemPosition() == 0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择扣款账户");
				return;
			}
			if (!StringUtil.isNullOrEmpty(RemittanceDataCenter.getInstance().getAccDetail())) {
				startActivity(new Intent(activity, AccDetailDialogActivity.class));
			} else {
				BaseHttpEngine.showProgressDialog();
				activity.setNeedDetailView(OverseasRemitterInfoFragment.this);
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(Comm.ACCOUNT_ID, accList.get(spPayAcct.getSelectedItemPosition() - 1).get(Comm.ACCOUNT_ID));
				activity.getHttpTools().requestHttp(Acc.QRY_ACC_BALANCE_API, "requestPsnAccountQueryAccountDetailCallBack", params, false);
				activity.getHttpTools().registErrorCode(Acc.QRY_ACC_BALANCE_API,"AccQueryDetailAction.NoSubAccount");
			}
		}
	};

	/** 使用模板按钮监听 */
	private OnClickListener useTemplateListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseHttpEngine.showProgressDialog();
			requestModelList();
		}
	};

	/**
	 * 模板列表查询
	 */
	public void requestModelList() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Remittance.CURRENTINDEX, "0");
		params.put(Remittance.PAGESIZE, RemittanceContent.PAGESIZE);
		params.put(Remittance._REFRESH, "true");
		activity.getHttpTools().requestHttp(Remittance.PSNINTERNATIONALTRANSFERTEMPLATEQUERY, "transferTemplateQueryCallBack", params, true);
		activity.getHttpTools().registErrorCode(Remittance.PSNINTERNATIONALTRANSFERTEMPLATEQUERY,"CCSS.S0012");
	}

	@Override
	public void accSelection(int position) {
		spPayAcct.setSelection(position);
		
	}
}
