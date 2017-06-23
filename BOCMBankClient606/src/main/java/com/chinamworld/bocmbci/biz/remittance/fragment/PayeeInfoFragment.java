package com.chinamworld.bocmbci.biz.remittance.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.RemittanceContent;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.remittance.adapter.RemittanceCurrencyAdapter;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.RemittanceChooseCountryActivity;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.RemittanceInfoInputActivity;
import com.chinamworld.bocmbci.biz.remittance.dialog.QuerySWIFTDialogActivity;
import com.chinamworld.bocmbci.biz.remittance.interfaces.ChooseCountryListener;
import com.chinamworld.bocmbci.biz.remittance.interfaces.OnAreaSelectListener;
import com.chinamworld.bocmbci.biz.remittance.interfaces.OnIbanFormatCheckListener;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.TextWatcherLimit;

public class PayeeInfoFragment extends Fragment {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static String TAG = "PayeeInfoFragment";
	/** 此片段依附的Activity */
	private RemittanceInfoInputActivity activity;
	/** 此片段状态 0-汇款流程 1-发起汇款流程 */
	private int state;
	/** 如果是发起汇款流程，模板详情放到这里 */
	private Map<String, Object> detailMap;
	/** 汇款地区下拉框 */
	private Spinner spArea;
	/** 选择国家控件 */
	private TextView tvCountry;
	/** 收款人名称 */
	private EditText etName;
	/** 收款人地址 */
	private EditText etAddress;
	/** 选择地区下拉框为日本时的联系电话 */
	private EditText etRbPhone;
	/** 收款人账号 */
	private EditText etAccNumber;
	/** 是否保存为模板 */
	private CheckBox cbSave;
	/** SWIFT码输入框 */
	private EditText etSWIFT;
	/** 收款银行全称 */
	private EditText etBankFullName;
	/** 收款银行全称输入监听 */
	private TextWatcherLimit twlBankFullName;
	/** 收款行行号 */
	private EditText etBankNumber;
	/** 收款行行号输入监听 */
	private TextWatcherLimit twlBankNumber;
	/** 日本时显示此地址输入框，收款银行地址 */
	private EditText etRbAdress;
	/** 用户选择的国家码 */
	private String chooseCountryCode;
	/** 选择地区的监听者 */
	private OnAreaSelectListener areaSelectListener;
	/** 主显示视图 */
	private View mMainView;
	/** 是否清空收款行行号 */
	private boolean isCleanBankNumber = true;
	/** 收款人姓名控制 */
	private TextWatcherLimit twlName;
	/** 收款人地址控制 */
	private TextWatcherLimit twlAdress;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

//	public PayeeInfoFragment(RemittanceInfoInputActivity activity) {
//		this.activity = activity;
//	}


	@Override
	public void onAttach(Activity activity) {
		this.activity = (RemittanceInfoInputActivity)activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.remittance_info_input_payee, null);
		findView(mMainView);
		viewSet();
		return mMainView;
	}

	private void findView(View mMainView) {
		spArea = (Spinner) mMainView.findViewById(R.id.sp_area);
		tvCountry = (TextView) mMainView.findViewById(R.id.tv_country);
		etName = (EditText) mMainView.findViewById(R.id.et_name);
		etAddress = (EditText) mMainView.findViewById(R.id.et_adress);
		etRbPhone = (EditText) mMainView.findViewById(R.id.et_rbPhone);
		cbSave = (CheckBox) mMainView.findViewById(R.id.cb_save);
		etSWIFT = (EditText) mMainView.findViewById(R.id.et_SWIFT);
		etBankFullName = (EditText) mMainView.findViewById(R.id.et_bankFullName);
		etBankNumber = (EditText) mMainView.findViewById(R.id.et_bankNumber);
		etRbAdress = (EditText) mMainView.findViewById(R.id.et_rbAdress);
		etAccNumber = (EditText) mMainView.findViewById(R.id.et_accNumber);
	}

	private void viewSet() {
		twlName = new TextWatcherLimit(activity, etName, 70);
		twlAdress = new TextWatcherLimit(activity, etAddress, 105);
		etName.addTextChangedListener(twlName);
		etAddress.addTextChangedListener(twlAdress);
		etRbPhone.addTextChangedListener(twlAdress);
		etName.setOnFocusChangeListener(nameFocusChangeListener);
		etAddress.setOnFocusChangeListener(adressFocusChangeListener);
		etRbPhone.setOnFocusChangeListener(adressFocusChangeListener);
		
		EditTextUtils.setLengthMatcher(getActivity(), etSWIFT, 11);
		EditTextUtils.setLengthMatcher(getActivity(), etBankFullName, 105);
		EditTextUtils.setLengthMatcher(getActivity(), etRbAdress, 104);
		EditTextUtils.setLengthMatcher(getActivity(), etAccNumber, 34);
		etSWIFT.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					String inputSWIFT = etSWIFT.getText().toString().trim();
					// 如果用户输入8位默认补齐11位
					if (inputSWIFT.length() == 8) {
						etSWIFT.setText(inputSWIFT + "XXX");
					}
				}
			}
		});

		etAccNumber.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (StringUtil.isNull(etAccNumber.getText().toString().trim())) {
						return;
					}

					if (state == 0) {
						String accNumber = etAccNumber.getText().toString().trim();
						if(regularAccNumber(true)) {
							if (accNumber.length() >= 2 && Character.isLetter(accNumber.charAt(0)) && Character.isLetter(accNumber.charAt(1))) {
								activity.setOnIbanFormatCheckListener(new OnIbanFormatCheckListener() {

									@Override
									public void onIbanFormatCheck(boolean isPass) {
										// PayeeInfoFragment.this.isPass = isPass;
										if (isPass) {
											// iban账号校验通过
											Map<String, Object> map = RemittanceDataCenter.getInstance().getMapPsnIbanFormatCheck();
											if (map.get(Remittance.CHECKRESULT).equals("0")) {
												String swift = (String) map.get(Remittance.PAYEEBANKSWIFT);
												String bankFullName = (String) map.get(Remittance.PAYEEBANKFULLNAME);
												String payeeBankAdd = (String) map.get(Remittance.PAYEEBANKADD);
												if (!StringUtil.isNull(swift)) {
													etSWIFT.setText(swift);
												}
												if (!StringUtil.isNull(bankFullName)) {
													etBankFullName.setText(bankFullName);
												}
												if (!StringUtil.isNull(payeeBankAdd)) {
													etRbAdress.setText(payeeBankAdd);
												}
											}
										}
									}
								});
								BaseHttpEngine.showProgressDialog();
								Map<String, Object> params = new HashMap<String, Object>();
								params.put(Remittance.PAYEEACTNO, (etAccNumber.getText().toString().trim()).toUpperCase());
								activity.getHttpTools().requestHttp(Remittance.PSNIBANFORMATCHECK, "requestPsnIbanFormatCheckCallBack", params, false);
							}
						}
					}
				}
			}
		});

		mMainView.findViewById(R.id.tv_querySWIFT).setOnClickListener(querySWIFTListener);
		// RemittanceUtils.initSpinnerView(activity, spArea,
		// RemittanceDataCenter.payeeArea);
		RemittanceCurrencyAdapter mAdapter = new RemittanceCurrencyAdapter(activity, RemittanceDataCenter.payeeArea);
		spArea.setAdapter(mAdapter);
		spArea.setOnItemSelectedListener(selectAreaListener);
		tvCountry.setOnClickListener(selectCountryListener);
		initViewData();
	}

	/** 初始化数据 */
	private void initViewData() {
		if (state == 1) {
			// 先清空数据，防止多次选择模板时数据混
			etName.setText("");
			etAccNumber.setText("");
			etSWIFT.setText("");
			etAddress.setText("");
			etBankNumber.setText("");
			etRbPhone.setText("");
			etRbAdress.setText("");
			etBankFullName.setText("");

			cbSave.setChecked(false);
			cbSave.setEnabled(false);
			detailMap = RemittanceDataCenter.getInstance().getMapPsnInternationalTransferTemplateDetailQuery();
			spArea.setSelection(RemittanceDataCenter.payeeAreaCode.indexOf(detailMap.get(Remittance.GATHERINGAREA)));

			// 从模板管理里面发起汇款为五个风险国不反显，让用户重新选择
			String modelCountryCode = (String) detailMap.get(Remittance.PAYEEPERMANENTCOUNTRY);
			if (RemittanceDataCenter.fiveCountry.contains(modelCountryCode)) {

			} else {
				chooseCountry(modelCountryCode);
			}

			String payeeEnName = (String) detailMap.get(Remittance.PAYEEENNAME);
			BaseDroidApp.getInstanse().setCurrentAct(activity);
			if (payeeEnName.length() > 70) {
				BaseDroidApp.getInstanse().createDialog(null, "收款人名称超过最大字符数", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						BaseDroidApp.getInstanse().dismissErrorDialog();
						initDataContinue1(detailMap);
					}
				});
			} else {
				etName.setText(payeeEnName);
				initDataContinue1(detailMap);
			}
		}
	}
	
	/** 继续初始化数据1 */
	private void initDataContinue1(final Map<String, Object> detailMap) {
		etAccNumber.setText((String) detailMap.get(Remittance.PAYEEACTNO));
		etSWIFT.setText((String) detailMap.get(Remittance.PAYEEBANKSWIFT));
		
		if (spArea.getSelectedItemPosition() == 5) {
			String payeeEnName = (String) detailMap.get(Remittance.PAYEEENNAME);
			String rbPhone = (String) detailMap.get(Remittance.PAYEEENADDRESS);
			if (rbPhone.length() > 105 || (payeeEnName.length() > 35 && rbPhone.length() > 70)) {
				BaseDroidApp.getInstanse().createDialog(null, "收款人联系电话超过最大字符数", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						BaseDroidApp.getInstanse().dismissErrorDialog();
						initDataContinue2(detailMap);
					}
				});
			} else {
				etRbPhone.setText((String) detailMap.get(Remittance.PAYEEENADDRESS));
				initDataContinue2(detailMap);
			}
		} else {
			String payeeEnName = (String) detailMap.get(Remittance.PAYEEENNAME);
			String adress = (String) detailMap.get(Remittance.PAYEEENADDRESS);
			if (adress.length() > 105 || (payeeEnName.length() > 35 && adress.length() > 70)) {
				BaseDroidApp.getInstanse().createDialog(null, "收款人地址超过最大字符数", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						BaseDroidApp.getInstanse().dismissErrorDialog();
						initDataContinue2(detailMap);
					}
				});
			} else {
				etAddress.setText((String) detailMap.get(Remittance.PAYEEENADDRESS));
				initDataContinue2(detailMap);
			}
		}
	}

	/** 继续初始化数据2 */
	private void initDataContinue2(Map<String, Object> detailMap) {

		switch (spArea.getSelectedItemPosition()) {
		case 0:

			break;
		case 1:
			etBankNumber.setText((String) detailMap.get(Remittance.PAYEEBANKNUM));
			isCleanBankNumber = false;
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			etBankNumber.setText((String) detailMap.get(Remittance.PAYEEBANKNUM));
			isCleanBankNumber = false;
			break;
		case 5:
			etRbAdress.setText((String) detailMap.get(Remittance.PAYEEBANKADD));
			break;
		case 6:
			break;
		}
		etBankFullName.setText((String) detailMap.get(Remittance.PAYEEBANKNAME));
	}

	/** 跨境汇款预交易操作，校验字段，准备上送参数 */
	public boolean remittanceConfirm() {
		if (submitRegexp(true)) {
			putParams();
			return true;
		}
		return false;
	}

	/** 准备参数 */
	public void putParams() {
		Map<String, Object> params = RemittanceDataCenter.getInstance().getSubmitParams();
		Map<String, Object> userInput = RemittanceDataCenter.getInstance().getUserInput();
		params.put(Remittance.GATHERINGAREA, RemittanceDataCenter.payeeAreaCode.get(spArea.getSelectedItemPosition()));
		params.put(Remittance.PAYEEPERMANENTCOUNTRY, chooseCountryCode);
		params.put(Remittance.PAYEEENNAME, etName.getText().toString().trim().toUpperCase());
		userInput.put(Remittance.PAYEEENNAME, etName.getText().toString().trim());
		params.put(Remittance.PAYEEACTNO, etAccNumber.getText().toString().trim().toUpperCase());
		userInput.put(Remittance.PAYEEACTNO, etAccNumber.getText().toString().trim());
		params.put(Remittance.PAYEEBANKSWIFT, etSWIFT.getText().toString().trim().toUpperCase());
		userInput.put(Remittance.PAYEEBANKSWIFT, etSWIFT.getText().toString().trim());
		params.put(Remittance.PAYEEBANKFULLNAME, (etBankFullName.getText().toString().trim()).toUpperCase());
		userInput.put(Remittance.PAYEEBANKFULLNAME, etBankFullName.getText().toString().trim());

		params.put(Remittance.TOSAVETEMPLATE, cbSave.isChecked() ? "1" : "0");

		if (spArea.getSelectedItemPosition() == 5) {
			params.put(Remittance.PAYEEENADDRESS, etRbPhone.getText().toString().trim().toUpperCase());
			userInput.put(Remittance.PAYEEENADDRESS, etRbPhone.getText().toString().trim());
			params.put(Remittance.PAYEEBANKADD, etRbAdress.getText().toString().trim().toUpperCase());
			userInput.put(Remittance.PAYEEBANKADD, etRbAdress.getText().toString().trim());
		} else if (spArea.getSelectedItemPosition() == 4) {
			params.put(Remittance.PAYEEENADDRESS, etAddress.getText().toString().trim().toUpperCase());
			userInput.put(Remittance.PAYEEENADDRESS, etAddress.getText().toString().trim());
		} else {
			params.put(Remittance.PAYEEENADDRESS, etAddress.getText().toString().trim().toUpperCase());
			userInput.put(Remittance.PAYEEENADDRESS, etAddress.getText().toString().trim());
		}
		if (spArea.getSelectedItemPosition() == 1 || spArea.getSelectedItemPosition() == 4) {
			params.put(Remittance.PAYEEBANKNUM, (etBankNumber.getText().toString().trim()).toUpperCase());
			userInput.put(Remittance.PAYEEBANKNUM, etBankNumber.getText().toString().trim());
		}
		LogGloble.i(TAG, params.toString());
	}

	/** 校验 */
	private boolean submitRegexp(boolean required) {
		if (spArea.getSelectedItemPosition() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择收款地区");
			return false;
		}
		if (StringUtil.isNull(chooseCountryCode)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择收款人常驻国家（地区）");
			return false;
		}
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		if (onlyRegular(required, etName.getText().toString().trim())) {
			RegexpBean name = new RegexpBean(RemittanceContent.PAYEENAME_CN, etName.getText().toString().trim(), RemittanceContent.PAYEENAME);
			lists.add(name);
			LogGloble.i("100", name.toString());
		}

		if (spArea.getSelectedItemPosition() != 5) {
			if (spArea.getSelectedItemPosition() == 4 || onlyRegular(false, etAddress.getText().toString().trim())) {
//				RegexpBean name = new RegexpBean(RemittanceContent.PAYEEADDRESS_CN, etAddress.getText().toString().trim(), etName.getText().toString().trim().length() < 35 ? RemittanceContent.REMITTERADDRESS : RemittanceContent.REMITTERADRESS_70);
				RegexpBean name = new RegexpBean(RemittanceContent.PAYEEADDRESS_CN, etAddress.getText().toString().trim(), RemittanceContent.REMITTERADDRESS);
				lists.add(name);
				LogGloble.i("100", name.toString());
			}
		} else {
			if (onlyRegular(required, etRbPhone.getText().toString().trim())) {
//				RegexpBean name = new RegexpBean(RemittanceContent.PAYEEPHONE_CN, etRbPhone.getText().toString().trim(), etName.getText().toString().trim().length() < 35 ? RemittanceContent.REMITTERADDRESS : RemittanceContent.REMITTERADRESS_70);
				RegexpBean name = new RegexpBean(RemittanceContent.PAYEEPHONE_CN, etRbPhone.getText().toString().trim(), RemittanceContent.REMITTERADDRESS);
				lists.add(name);
				LogGloble.i("100", name.toString());
			}
		}

		if (onlyRegular(required, etAccNumber.getText().toString().trim())) {
			RegexpBean name = new RegexpBean(RemittanceContent.PAYEEACCOUNTNUMBER_CN, etAccNumber.getText().toString().trim(), RemittanceContent.PAYEEACCOUNTNUMBER);
			lists.add(name);
			LogGloble.i("100", name.toString());
		}

		String strSWIFT = etSWIFT.getText().toString().trim();
		if (strSWIFT.length() == 8 || strSWIFT.length() == 11) {
			if (strSWIFT.length() == 8) {
				etSWIFT.setText(strSWIFT + "XXX");
			}

			String swift = strSWIFT.substring(4, 6).toUpperCase();
			if (!swift.equals("CN")) {
				if (onlyRegular(required, etSWIFT.getText().toString().trim())) {
					RegexpBean name = new RegexpBean(RemittanceContent.PAYEESWIFTCODE_CN, etSWIFT.getText().toString().trim(), RemittanceContent.PAYEESWIFTCODE);
					lists.add(name);
					LogGloble.i("100", name.toString());
				}
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog("SWIFT代码请输入8位或11位字母、数字，其中第五、六位不为CN，最大长度11个字符");
				return false;
			}
		} else {
			if (onlyRegular(required, etSWIFT.getText().toString().trim())) {
				RegexpBean name = new RegexpBean(RemittanceContent.PAYEESWIFTCODE_CN, etSWIFT.getText().toString().trim(), RemittanceContent.PAYEESWIFTCODE);
				lists.add(name);
				LogGloble.i("100", name.toString());
			}
		}

		if (spArea.getSelectedItemPosition() != 5) {
			if (onlyRegular(required, etBankFullName.getText().toString().trim())) {
				RegexpBean name = new RegexpBean(RemittanceContent.PAYEEBANKFULLNAME_CN, etBankFullName.getText().toString().trim(), RemittanceContent.REMITTANCEPAYEEBANKNAME);
				lists.add(name);
				LogGloble.i("100", name.toString());
			}
		} else {
			if (onlyRegular(required, etBankFullName.getText().toString().trim())) {
				RegexpBean name = new RegexpBean(RemittanceContent.BANKPAYEEBANKFULLNAME_CN, etBankFullName.getText().toString().trim(), RemittanceContent.PAYEEBANKFULLNAME);
				lists.add(name);
				LogGloble.i("100", name.toString());
			}
		}

		if (spArea.getSelectedItemPosition() == 5) {
			if (onlyRegular(required, etRbAdress.getText().toString().trim())) {
				RegexpBean name = new RegexpBean(RemittanceContent.PAYEEBANKADDRESS_CN, etRbAdress.getText().toString().trim(), RemittanceContent.PAYEEBANKFULLNAME);
				lists.add(name);
				LogGloble.i("100", name.toString());
			}
		}

		if (spArea.getSelectedItemPosition() == 5) {
			try {
				String strName = new String(etBankFullName.getText().toString().getBytes("GB2312"), "ISO-8859-1");
				String strAddress = new String(etRbAdress.getText().toString().getBytes("GB2312"), "ISO-8859-1");
				int length = strName.length() + strAddress.length();
				if (length > 104) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("收款银行全称和收款银行地址合计长度超过104个字符，请修改！");
					return false;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}

		if (spArea.getSelectedItemPosition() == 1) {
			if (onlyRegular(required, etBankNumber.getText().toString().trim())) {
				RegexpBean name = new RegexpBean(RemittanceContent.PAYEEBANKNUMBER_CN, etBankNumber.getText().toString().trim(), RemittanceContent.PAYEEBANKNUMBER);
				lists.add(name);
				LogGloble.i("100", name.toString());
			}
		}
		if (spArea.getSelectedItemId() == 4) {
			if (onlyRegular(required, etBankNumber.getText().toString().trim())) {
				RegexpBean name = new RegexpBean(RemittanceContent.PAYEEBANKNUMBER_CN, etBankNumber.getText().toString().trim(), RemittanceContent.REMITTANCEPAYEEBANKNOCA);
				lists.add(name);
				LogGloble.i("100", name.toString());
			}
		}
		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}
		if (!checkName()) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("“收款人名称”栏位中，第1、第36个字符不能为“-”或“:”，请调整。");
			return false;
		}
		if (!checkAdressOrRbPhone()) {
			if (spArea.getSelectedItemPosition() == 5) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("“收款人联系电话”栏位中，第1、第36、第71个字符不能为“-”或“:”，请调整。");
				return false;
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog("“收款人地址”栏位中，第1、第36、第71个字符不能为“-”或“:”，请调整。");
				return false;
			}
		}
		return true;
	}
	
	/** 账号的及时校验   先本地校验,如果是字母在进行联机校验 */
	private boolean regularAccNumber(boolean required){
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		if(onlyRegular(required, etAccNumber.getText().toString().trim())){
			RegexpBean name = new RegexpBean(RemittanceContent.PAYEEACCOUNTNUMBER_CN, etAccNumber.getText().toString().trim(), RemittanceContent.PAYEEACCOUNTNUMBER);
			lists.add(name);
		}
		
		if(!RegexpUtils.regexpDate(lists)){
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
	
	/** 设置选择汇款地区的监听者 */
	public void setOnAreaSelectListener(OnAreaSelectListener listener) {
		areaSelectListener = listener;
	}

	/** 选择收款人常驻国家 */
	private void chooseCountry(String countryCode) {
		List<Map<String, String>> list = RemittanceDataCenter.getInstance().getListPsnQryInternationalTrans4CNYCountry();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> tempMap = list.get(i);
			if (tempMap.get(Remittance.COUNTRYCODE).equals(countryCode)) {
				chooseCountryCode = countryCode;
				tvCountry.setText(tempMap.get(Remittance.NAME_CN));
				break;
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RemittanceContent.QUERY_SWIFT) {
			// 返回选择的SWIFT码信息
			Map<String, Object> selectSWIFT = RemittanceDataCenter.getInstance().getListPsnInternationalTransferSwiftQuery().get(data.getIntExtra("SWIFT", 0));
			etSWIFT.setText((String) selectSWIFT.get(Remittance.SWIFTCODE));
			etBankFullName.setText((String) selectSWIFT.get(Remittance.BANKNAME));
			if (spArea.getSelectedItemPosition() == 5) {
				String country = StringUtil.valueOfEmpty((String) selectSWIFT.get(Remittance.BANKADDRESS3));
				String city = StringUtil.valueOfEmpty((String) selectSWIFT.get(Remittance.BANKADDRESS2));
				String address = StringUtil.valueOfEmpty((String) selectSWIFT.get(Remittance.BANKADDRESS1));
				List<String> addressStr = new ArrayList<String>();
				if (!country.equals("")) {
					addressStr.add(country);
				}
				if (!city.equals("")) {
					addressStr.add(city);
				}
				if (!address.equals("")) {
					addressStr.add(address);
				}
				StringBuffer rbAddress = new StringBuffer();
				for (int i = 0; i < addressStr.size(); i++) {
					if (i == 0) {
						rbAddress.append(addressStr.get(i));
					} else {
						rbAddress.append("," + addressStr.get(i));
					}
				}
				etRbAdress.setText(rbAddress);
			}
		}
	}

	/** 设置片段状态 0-汇款流程 1-发起汇款 */
	public void setState(int state, boolean isNowNotify) {
		this.state = state;
		if (isNowNotify) {
			initViewData();
		}
	}
	
	/** 检查字符串中第1/36/71个位置的字符是否为英文冒号或- */
	private boolean checkString(String str) {
		int length = str.length();
		if (length <= 35) {
			return checkStringColon(str.charAt(0));
		} else if (length <= 70) {
			return checkStringColon(str.charAt(0)) || checkStringColon(str.charAt(35));
		} else {
			return checkStringColon(str.charAt(0)) || checkStringColon(str.charAt(35)) || checkStringColon(str.charAt(70));
		}
	}
	
	private boolean checkStringColon(char ch) {
		return ch == ':' || ch == '-';
	}
	
	/** 为TextView中显示的指定位置中的文字添加点击事件 */
	private void tvAddTextClickable(TextView tv) {
		tv.setText(getClickableSpan());
		tv.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private SpannableString getClickableSpan() {
		View.OnClickListener l = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 要显示的字符拼接
				String str1 = "您所填写的“";
				String inputStr = "";
				switch (v.getId()) {
				case R.id.tv_name_error:
					str1 += "收款人名称”（如下所示），每段的首位不能为“-”或“:”，请调整栏位中相应字符的位置或内容，以满足跨境汇款报文规则。";
					inputStr = etName.getText().toString().trim();
					break;
				case R.id.tv_adress_error:
					str1 += "收款人地址”（如下所示），每段的首位不能为“-”或“:”，请调整栏位中相应字符的位置或内容，以满足跨境汇款报文规则。";
					inputStr = etAddress.getText().toString().trim();
					break;
				case R.id.tv_rbPhone_error:
					str1 += "收款人联系电话”（如下所示），每段的首位不能为“-”或“:”，请调整栏位中相应字符的位置或内容，以满足跨境汇款报文规则。";
					inputStr = etRbPhone.getText().toString().trim();
					break;
				}
				
				int inputStrLength = inputStr.length();
				String str2 = "";
				if (inputStrLength < 35) {
					str2 += ("\t\t\t\t\t\t\t\t" + inputStr);
				} else if (inputStrLength < 70) {
					str2 += ("\t\t\t\t\t\t\t\t" + inputStr.substring(0, 35) + "\n\t\t\t\t\t\t\t\t" + inputStr.substring(35));
				} else {
					str2 += ("\t\t\t\t\t\t\t\t" + inputStr.substring(0, 35) + "\n\t\t\t\t\t\t\t\t" + inputStr.substring(35, 70) + "\n\t\t\t\t\t\t\t\t" + inputStr.substring(70));
				}
				
				BaseDroidApp.getInstanse().showInfoMessageDialog2(str1, str2, Gravity.LEFT);
			}
		};

		SpannableString spanableInfo = new SpannableString("您输入的信息存在问题，请点击这里查看具体原因");
		int start = 14;
		int end = 16;
		spanableInfo.setSpan(new Clickable(l), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanableInfo;
	}

	private class Clickable extends ClickableSpan implements OnClickListener {
		private final View.OnClickListener mListener;

		public Clickable(View.OnClickListener l) {
			mListener = l;
		}

		@Override
		public void onClick(View v) {
			mListener.onClick(v);
		}
	}
	
	private boolean checkName() {
		int tempLength = 140 - etName.getText().toString().trim().length();
		if (spArea.getSelectedItemPosition() == 5) {
			etRbPhone.removeTextChangedListener(twlAdress);
			twlAdress = new TextWatcherLimit(activity, etRbPhone, tempLength >= 105 ? 105 : 70);
			etRbPhone.addTextChangedListener(twlAdress);
		} else {
			etAddress.removeTextChangedListener(twlAdress);
			twlAdress = new TextWatcherLimit(activity, etAddress, tempLength >= 105 ? 105 : 70);
			etAddress.addTextChangedListener(twlAdress);
		}

		TextView tv = (TextView) mMainView.findViewById(R.id.tv_name_error);
		if (!StringUtil.isNull(etName.getText().toString().trim())) {
			if (checkString(etName.getText().toString().trim())) {
				tv.setVisibility(View.VISIBLE);
				tvAddTextClickable(tv);
				return false;
			} else {
				tv.setVisibility(View.GONE);
			}
		} else {
			tv.setVisibility(View.GONE);
		}
		return true;
	}
	
	private boolean checkAdressOrRbPhone() {
		etName.removeTextChangedListener(twlName);
		int tempLength;
		if (spArea.getSelectedItemPosition() == 5) {
			tempLength = 140 - etRbPhone.getText().toString().trim().length();
		} else {
			tempLength = 140 - etAddress.getText().toString().trim().length();
		}
		twlName = new TextWatcherLimit(activity, etName, tempLength >= 70 ? 70 : 35);
		etName.addTextChangedListener(twlName);
		
		int area = spArea.getSelectedItemPosition();
		
		String text = area == 5 ? etRbPhone.getText().toString().trim() : etAddress.getText().toString().trim();
		TextView tv;
		if (area == 5) {
			tv = (TextView) mMainView.findViewById(R.id.tv_rbPhone_error);
		} else {
			tv = (TextView) mMainView.findViewById(R.id.tv_adress_error);
		}
		if (!StringUtil.isNull(text)) {
			if (checkString(text)) {
				tv.setVisibility(View.VISIBLE);
				tvAddTextClickable(tv);
				return false;
			} else {
				tv.setVisibility(View.GONE);
			}
		} else {
			tv.setVisibility(View.GONE);
		}
		return true;
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 选择汇款地区监听 */
	private OnItemSelectedListener selectAreaListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			SelectAreaViewController.viewControl(mMainView, position);
			if (areaSelectListener != null) {
				areaSelectListener.onAreaSelect(position);
			}
			if (position == 1) {
				if (isCleanBankNumber) {
					etBankNumber.setText("");
				} else {
					isCleanBankNumber = true;
				}
				if (twlBankNumber != null) {
					etBankNumber.removeTextChangedListener(twlBankNumber);
				}
				twlBankNumber = new TextWatcherLimit(getActivity(), etBankNumber, 8);
				etBankNumber.addTextChangedListener(twlBankNumber);
			} else if (position == 4) {
				if (isCleanBankNumber) {
					etBankNumber.setText("");
				} else {
					isCleanBankNumber = true;
				}
				if (twlBankNumber != null) {
					etBankNumber.removeTextChangedListener(twlBankNumber);
				}
				twlBankNumber = new TextWatcherLimit(getActivity(), etBankNumber, 34);
				etBankNumber.addTextChangedListener(twlBankNumber);
			} else if (position == 5) {
				if (etBankFullName.getText().toString().trim().length() > 104) {
					etBankFullName.setText("");
				}
				if (twlBankFullName != null) {
					etBankFullName.removeTextChangedListener(twlBankFullName);
				}
				twlBankFullName = new TextWatcherLimit(getActivity(), etBankFullName, 104);
				etBankFullName.addTextChangedListener(twlBankFullName);
			} else {
				if (twlBankFullName != null) {
					etBankFullName.removeTextChangedListener(twlBankFullName);
				}
				twlBankFullName = new TextWatcherLimit(getActivity(), etBankFullName, 105);
				etBankFullName.addTextChangedListener(twlBankFullName);
			}
			mMainView.findViewById(R.id.tv_name_error).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tv_adress_error).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tv_rbPhone_error).setVisibility(View.GONE);
			checkName();
			checkAdressOrRbPhone();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	/** 选择收款人常驻国家监听 */
	private OnClickListener selectCountryListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!StringUtil.isNullOrEmpty(RemittanceDataCenter.getInstance().getListPsnQryInternationalTrans4CNYCountry())) {
				startActivityForResult(new Intent(getActivity(), RemittanceChooseCountryActivity.class), RemittanceContent.CHOOSE_CHOUNTRY);
				activity.overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
			} else {
				// 否则请求收款人常驻国家
				BaseHttpEngine.showProgressDialog();
				activity.getHttpTools().requestHttp(Remittance.PSNQRYINTERNATIONALTRANS4CNYCOUNTRY, "requestPsnQryInternationalTrans4CNYCountryCallBack", null, false);
			}
			activity.setChooseCountryListener(new ChooseCountryListener() {

				@Override
				public void chooseCountry(String countryCode) {
					LogGloble.i(TAG, "User choose countryCode:" + countryCode);
					PayeeInfoFragment.this.chooseCountry(countryCode);
				}
			});
		}
	};

	/** 查询SWIFT码监听 */
	private OnClickListener querySWIFTListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			startActivityForResult(new Intent(activity, QuerySWIFTDialogActivity.class), RemittanceContent.QUERY_SWIFT);
		}
	};
	
	/** 收款人姓名焦点事件 */
	private OnFocusChangeListener nameFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				checkName();
			}
		}
	};
	
	/** 收款人地址焦点事件 */
	private OnFocusChangeListener adressFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				checkAdressOrRbPhone();
			}
		}
	};
}
