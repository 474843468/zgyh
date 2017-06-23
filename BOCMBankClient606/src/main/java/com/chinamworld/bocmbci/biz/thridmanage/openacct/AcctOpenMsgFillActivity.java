package com.chinamworld.bocmbci.biz.thridmanage.openacct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.IdentifyType;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.ThridProvinceType;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdDataCenter;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdManagerBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.RegCode;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 开户信息填写
 * 
 * @author panwe
 * 
 */
public class AcctOpenMsgFillActivity extends ThirdManagerBaseActivity implements OnClickListener {

	private static final String TAG = AcctOpenMsgFillActivity.class.getSimpleName();
	/*** 主布局 **/
	private View viewContent;
	/** 银行账户 **/
	private TextView tvBankAcc;
	/** 客户姓名 */
	private TextView tvName;
	/** 证件类型 */
	private TextView tvIdType;
	/** 证件号 */
	private TextView tvIdNumber;
	/** 手机号 */
	private EditText edMobile;
	/** 通讯地址 **/
	private EditText edAddress;
	/** 邮政编码 */
	private EditText edPostCode;
	/** 地区 */
	private Spinner spProvince;
	/** 拦截证券公司Spinner事件 */
	private View companyView;
	/** 证券公司 */
	private Spinner spCompanyView;
	/** 营业部：getText()名称 getTag()营业部代码 */
	private TextView spCompanyParmentView;
	/** 用户协议勾选框 */
	private CheckBox checkBox;
	// /** 账户id **/
	// private String accId;
	/** 用户信息 */
	private Map<String, Object> customInfo = null;
	/** 省份标识 **/
	private List<String> provinceCodeList = null;
	/** 公司 **/
	private List<Map<String, Object>> companyData = null;

	private final int REQUEST_PROTOCOL_CODE = 1002;
	private final int REQUEST_BRANCH_CODE = 1001;

	/** 省份spinner第一次选择 **/
	// private boolean isfirst = true;

	private int mIntentPosition;
	/** 账户 */
	private Map<String, Object> mAccountMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		if (getIntentData()) {
			// 添加布局
			viewContent = LayoutInflater.from(this).inflate(R.layout.third_openacc_msgfill, null);
			addView(viewContent);
			setTitle(R.string.third_openacc_open);
			findView();
			setCostInfo();
		} else {
			finish();
		}
	}

	private boolean getIntentData() {
		mIntentPosition = getIntent().getIntExtra("position", 0);
		mAccountMap = ThirdDataCenter.getInstance().getBankAccountList().get(mIntentPosition);
		customInfo = ThirdDataCenter.getInstance().getCustomInfo();
		return mAccountMap != null && customInfo != null;
	}

	private void findView() {
		// 右上角按钮赋值
		setTitleRightText(getString(R.string.go_main));
		setRightBtnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goMainActivity();
			}
		});

		tvBankAcc = (TextView) viewContent.findViewById(R.id.tv_openacc_acc);
		tvName = (TextView) viewContent.findViewById(R.id.tv_openacc_name);
		tvIdType = (TextView) viewContent.findViewById(R.id.tv_openacc_idtype);
		tvIdNumber = (TextView) viewContent.findViewById(R.id.tv_openacc_idnumber);

		edMobile = (EditText) viewContent.findViewById(R.id.et_openacc_mobile);
		edAddress = (EditText) viewContent.findViewById(R.id.et_openacc_adress);
		edPostCode = (EditText) viewContent.findViewById(R.id.et_openacc_postcode);

		spProvince = (Spinner) viewContent.findViewById(R.id.sp_openacc_province);
		spCompanyView = (Spinner) viewContent.findViewById(R.id.sp_openacc_company);
		initSpinner(spCompanyView, ThirdDataCenter.getInstance().spInitData());
		spCompanyParmentView = (TextView) viewContent.findViewById(R.id.sp_openacc_companypart);
		companyView = viewContent.findViewById(R.id.openacc_company);
		companyView.setOnClickListener(this);

		spCompanyParmentView.setText(R.string.please_choose);
		spCompanyParmentView.setOnClickListener(this);

		spProvince.setOnItemSelectedListener(spSelectedClick);
		spCompanyView.setOnItemSelectedListener(spSelectedClick);

		Button btnNext = (Button) viewContent.findViewById(R.id.btnnext);
		btnNext.setOnClickListener(this);

		// 用户协议
		TextView tvServerInfo = (TextView) viewContent.findViewById(R.id.openacc_server_info);
		tvServerInfo.setText(Html.fromHtml( getString(R.string.third_server_info)));
		tvServerInfo.setOnClickListener(this);

		checkBox = (CheckBox) viewContent.findViewById(R.id.checkbox);
		EditTextUtils.setLengthMatcher(this, edAddress, 60);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 下一步
		case R.id.btnnext:
			if (checkSubmit()) {
				// 地区温馨提示(账户地区与选择地区不同提示)
				String accountIbkNum = (String) mAccountMap.get(Acc.RELEVANCEACCRES_LIST_ACCOUNTIBKNUM_REQ);
				String stockProvince = provinceCodeList.get(spProvince.getSelectedItemPosition() - 1);
				if (!ThridProvinceType.isProvinceByCode(stockProvince, accountIbkNum)) {
					BaseDroidApp.getInstanse().showErrorDialog(getString(R.string.stock_company_region_prompt),
							R.string.cancle, R.string.confirm, new OnClickListener() {
								@Override
								public void onClick(View v) {
									BaseDroidApp.getInstanse().dismissErrorDialog();
									if (v.getId() == R.id.retry_btn) {
										openAcctPre();
									}
								}
							});

				} else {
					openAcctPre();
				}
			}
			break;

		// 营业部
		case R.id.sp_openacc_companypart:
			if (spProvince.getSelectedItemId() == 0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.third_opendacc_pro_tip));
				return;
			}
			if (spCompanyView.getSelectedItemId() == 0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.third_opendacc_com_tip));
				return;
			}

			Intent it = new Intent(this, AccOpenStockBranchActivity.class);
			// 第一项为：请选择
			it.putExtra("PROVICE", provinceCodeList.get(spProvince.getSelectedItemPosition() - 1));
			it.putExtra("COMPANY",
					companyData.get(spCompanyView.getSelectedItemPosition() - 1).get(Third.OPENACC_COMPANY_CODE) + "/"
							+ spCompanyView.getSelectedItem().toString());
			startActivityForResult(it, REQUEST_BRANCH_CODE);

			break;

		// 用户协议
		case R.id.openacc_server_info:
			Intent intent = new Intent(this, AccOpenProtocolActivity.class);
			this.startActivityForResult(intent, REQUEST_PROTOCOL_CODE);
			break;
		case R.id.openacc_company:
			// 没有选择地区
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.third_opendacc_pro_tip));
			break;
		}

	}

	/** sp点击事件 **/
	private OnItemSelectedListener spSelectedClick = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

			// 联机选择恢复 其他项
			// 地区->证券公司->营业部
			switch (parent.getId()) {
			case R.id.sp_openacc_province:
				spCompanyParmentView.setText(R.string.please_choose);
				spCompanyParmentView.setTag(null);
				// 获取证券公司信息
				if (companyData == null || companyData.size() < 1) {
					BiiHttpEngine.showProgressDialog();
					getStockInfo();
				}
				setStockCompany(spCompanyView, companyData);
				// TODO
				if (spProvince.getSelectedItemId() == 0) {
					companyView.setVisibility(View.VISIBLE);
				} else {
					companyView.setVisibility(View.GONE);
				}
				break;
			case R.id.sp_openacc_company:
				LogGloble.d(TAG, "spCompanyView OnItemSelectedListener");
				spCompanyParmentView.setText(R.string.please_choose);
				spCompanyParmentView.setTag(null);
				break;
			default:
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	};

	/** 提交校验 */
	private boolean checkSubmit() {
		// 验证 手机
		RegexpBean moblieRegex = new RegexpBean(getString(R.string.tel_num_str), edMobile.getText().toString(),
				RegCode.LONG_MOBILE);
		// 验证 地址
		RegexpBean addressRegex = new RegexpBean(getString(R.string.postal_address), edAddress.getText().toString(),
				RegCode.TPDM_ADDRESS);
		RegexpBean postCodeRegex = new RegexpBean(getString(R.string.postcode), edPostCode.getText().toString(),
				RegCode.POSTCODE);
		// 验证 邮编
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		lists.add(moblieRegex);
		lists.add(addressRegex);
		lists.add(postCodeRegex);
		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}
		// 验证地区
		if (spProvince.getSelectedItemId() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.third_opendacc_pro_tip));
			return false;
		}
		// 验证证券公司
		if (spCompanyView.getSelectedItemId() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.third_opendacc_com_tip));
			return false;
		}
		// 验证营业部
		if (spCompanyParmentView.getTag() == null) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.third_openacc_company_department));
			return false;
		}

		if (!checkBox.isChecked()) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.third_checkbox_tip));
			return false;
		}
		return true;
	}

	// 显示用户信息
	private void setCostInfo() {
		tvBankAcc.setText(StringUtil.getForSixForString(mAccountMap.get(Third.CECURITYTRADE_BANKACCNUM).toString()));
		tvName.setText((String) customInfo.get(Third.OPENACC_CUSTINFO_NAME));
		String identifyType = (String) customInfo.get(Third.OPENACC_CUSTINFO_NAME_IDTYPE);
		tvIdType.setText(IdentifyType.getIdentifyTypeStr(identifyType));
		tvIdNumber.setText((String) customInfo.get(Third.OPENACC_CUSTINFO_NAME_NUM));
		setProcince();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_PROTOCOL_CODE:
			// 协议
			// checkBox.setChecked(true);
			if (resultCode == RESULT_OK) {
				checkBox.setChecked(true);
			} else if (resultCode == 100) {
				checkBox.setChecked(false);
			}
			break;
		case REQUEST_BRANCH_CODE:
			// 营业厅
			if (resultCode == RESULT_OK) {
				spCompanyParmentView.setText(data.getStringExtra("data"));
				spCompanyParmentView.setTag(data.getStringExtra("code"));
			} else if (resultCode == AccOpenStockBranchActivity.EMPTY_RESULTCODE) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.no_select_department));
			}
			break;
		}
	}

	// 查询证券公司信息
	private void getStockInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Third.METHOD_OPENACC_STOCKINFO);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "getStockInfoCallBack");
	}

	/*** 证券公司信息 返回 */
	public void getStockInfoCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		companyData = (List<Map<String, Object>>) biiResponseBody.getResult();
		setStockCompany(spCompanyView, companyData);
	}

	// 初始化省份
	private void setProcince() {
		provinceCodeList = ThirdDataCenter.getInstance().getProvinceCode();
		List<String> prList = new ArrayList<String>();
		prList.add(getText(R.string.please_choose).toString());
		for (int i = 0; i < provinceCodeList.size(); i++) {
			// if (LocalData.Province.get(provinceList.get(i)) != null) {
			String provinceStr = ThridProvinceType.getOldProvincesName(provinceCodeList.get(i));
			prList.add(provinceStr);
			// }
		}
		initSpinner(spProvince, prList);
	}

	private void setStockCompany(Spinner spinner, List<Map<String, Object>> companyData) {
		if (companyData == null) {
			initSpinner(spinner, getText(R.string.please_choose).toString());
		} else {
			List<String> stockList = new ArrayList<String>();
			stockList.add(getText(R.string.please_choose).toString());
			for (int i = 0; i < companyData.size(); i++) {
				if (!StringUtil.isNullOrEmpty(companyData.get(i).get(Third.OPENACC_CONFIRM_COMANY))) {
					stockList.add((String) companyData.get(i).get(Third.OPENACC_CONFIRM_COMANY));
				}
			}
			initSpinner(spinner, stockList);
		}
	}

	// 绑定spinner
	private void initSpinner(Spinner spinner, String data) {
		List<String> list = new ArrayList<String>();
		list.add(data);
		initSpinner(spinner, list);
	}

	// 绑定spinner
	private void initSpinner(Spinner spinner, List<String> list) {
		ArrayAdapter<String> accAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
		accAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(accAdapter);
	}

	private void openAcctOpenMsgConfirmActivity() {
		Intent intent = new Intent(this, AcctOpenMsgConfirmActivity.class);
		Bundle b = new Bundle();
		b.putString("ACCID", mAccountMap.get(Third.CECURITY_AMOUT_ACCID).toString());
		b.putString("ACCNUM", mAccountMap.get(Third.CECURITYTRADE_BANKACCNUM).toString());
		// b.putString("COSUTNAME", tvName.getText().toString());
		b.putString("COSUTNAME", customInfo.get(Third.OPENACC_CUSTINFO_NAME).toString());
		b.putString("MOBLIE", edMobile.getText().toString());
		b.putString("IDTYPE", customInfo.get(Third.OPENACC_CUSTINFO_NAME_IDTYPE).toString());
		b.putString("IDNUM", customInfo.get(Third.OPENACC_CUSTINFO_NAME_NUM).toString());
		b.putString("ADDRESS", edAddress.getText().toString());
		b.putString("POSTCODE", edPostCode.getText().toString());
		// -1因为第一项为请选择
		b.putString("BRANCH_ADDRESS", provinceCodeList.get(spProvince.getSelectedItemPosition() - 1));
		Map<String, Object> companyMap = companyData.get(spCompanyView.getSelectedItemPosition() - 1);
		// 证券公司code
		// 证券公司
		b.putString("COMPANY", companyMap.get(Third.OPENACC_CONFIRM_COMANY).toString());
		b.putString("STOCK_CORP_CODE", companyMap.get(Third.OPENACC_COMPANY_CODE).toString());

		// 营业部
		b.putString("BRANNAME", spCompanyParmentView.getText().toString());
		// 营业部code
		b.putString("STOCK_BRANCH_CODE", spCompanyParmentView.getTag().toString());
//		b.putSerializable("FACTORLIST", facotrs);
		intent.putExtras(b);
		startActivity(intent);
	}

	// -----------------------------------------------------------------------------------
	/**
	 * 预交易
	 */
	private void openAcctPre() {
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 发送安全因子请求
		requestGetSecurityFactor(Third.OPENACC_SERVICECODE);
	}

	/*** 安全因子返回结果 ***/
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 请求预交易
				requestOpenAccConfirm(BaseDroidApp.getInstanse().getSecurityChoosed());
			}
		});
	}

	// 请求预交易
	private void requestOpenAccConfirm(String combin) {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Third.METHOD_OPENACC_CONFIRM);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Third.PLATFORACC_LIST_ACCID, mAccountMap.get(Third.CECURITY_AMOUT_ACCID));
		params.put(Third.OPENACC_CONFIRM_ACC, mAccountMap.get(Third.CECURITYTRADE_BANKACCNUM));
		Map<String, Object> companyMap = companyData.get(spCompanyView.getSelectedItemPosition() - 1);
		params.put(Third.OPENACC_CONFIRM_COMANY, companyMap.get(Third.OPENACC_CONFIRM_COMANY).toString());
		params.put(Third.OPENACC_STOCKBRANCH_ADR, provinceCodeList.get(spProvince.getSelectedItemPosition() - 1));
		params.put(Third.OPENACC_CONFIRM_BRANCHNAME, spCompanyParmentView.getText().toString());
		params.put(Third.OPENDACC_CONFIRM_CUETNAME, customInfo.get(Third.OPENACC_CUSTINFO_NAME));
		params.put(Third.OPENDACC_CONFIRM_IDTYPE, customInfo.get(Third.OPENACC_CUSTINFO_NAME_IDTYPE));
		params.put(Third.OPENDACC_CONFIRM_IDNUM, customInfo.get(Third.OPENACC_CUSTINFO_NAME_NUM));
		params.put(Third.OPENDACC_CONFIRM_MOBILE, edMobile.getText().toString());
		params.put(Third.OPENDACC_CONFIRM_COMBINID, combin);
		biiRequestBody.setParams(params);
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "getOpenAccConfirmCallBack");
	}
	public static Map<String, Object> result;
	/*** 开户预交易 返回 */
	public void getOpenAccConfirmCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		result = (Map<String, Object>) biiResponseBody.getResult();
		if (result == null) {
			return;
		}
//		ArrayList<String> facotrs = new ArrayList<String>();
//		List<Map<String, Object>> factorList = (List<Map<String, Object>>) result.get(Third.OPENDACC_CONFIRM_FACLIST);
//		if (factorList != null) {
//			for (int i = 0; i < factorList.size(); i++) {
//				Map<String, String> map = (Map<String, String>) factorList.get(i).get(Third.OPENDACC_CONFIRM_SIP_FIED);
//				String sipType = (String) map.get(Third.OPENDACC_CONFIRM_SIP_NAME);
//				if (sipType.equals(Comm.Otp)) {
//					facotrs.add(sipType);
//				} else if (sipType.equals(Comm.Smc)) {
//					facotrs.add(sipType);
//				}
//			}
//		}
		requestForRandomNumber();
//		openAcctOpenMsgConfirmActivity();
	}
	// 请求密码控件随机数
		public void requestForRandomNumber() {
			BaseHttpEngine.showProgressDialogCanGoBack();
			BiiRequestBody biiRequestBody = new BiiRequestBody();
			biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
			biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.CONVERSATION_ID));
			HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
		}
		public static String randomNumber ;
		/*** 随机数返回 */
		public void queryRandomNumberCallBack(Object resultObj) {
			BaseHttpEngine.dissMissProgressDialog();
			BiiResponse biiResponse = (BiiResponse) resultObj;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			randomNumber = (String) biiResponseBody.getResult();
			if (StringUtil.isNull(randomNumber)) {
				return;
			}
			openAcctOpenMsgConfirmActivity();
		}

}
