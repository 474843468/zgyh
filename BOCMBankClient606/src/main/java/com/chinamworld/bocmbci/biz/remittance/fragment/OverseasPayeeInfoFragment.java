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
import android.util.Log;
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
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.RemittanceContent;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.OverseasChinaBankRemittanceInfoInputActivity;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.RemittanceChooseCountryActivity;

import com.chinamworld.bocmbci.biz.remittance.interfaces.ChooseCountryListener;
import com.chinamworld.bocmbci.biz.remittance.interfaces.OnAreaSelectListener;
import com.chinamworld.bocmbci.biz.remittance.interfaces.OnIbanFormatCheckListener;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.TextWatcherLimit;

public class OverseasPayeeInfoFragment extends Fragment {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static String TAG = "PayeeInfoFragment";
	/** 此片段依附的Activity */
	private OverseasChinaBankRemittanceInfoInputActivity activity;
	/** 此片段状态 0-汇款流程 1-发起汇款流程 */
	private int state;
	/** 如果是发起汇款流程，模板详情放到这里 */
	private Map<String, Object> detailMap;
	/** 汇款地区下拉框 */
	//private Spinner spArea;
	/** 收款银行所在国家（地区） */
	private TextView tv_over_area;
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

	/** 用户选择的国家码 */
	private String chooseCountryCode;
	/** 选择地区的监听者 */
	private OnAreaSelectListener areaSelectListener;
	/** 主显示视图 */
	private View mMainView;
	/** 是否清空收款行行号 */
	//private boolean isCleanBankNumber = true;
	/** 收款人姓名控制 */
	private TextWatcherLimit twlName;
	/** 收款人地址控制 */
	private TextWatcherLimit twlAdress;
	/** 收款银行*/
	private TextView tv_bankName ;
	/** 收款银行swift*/
	private TextView tv_over_swift;

	private TextView tip00;


	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

//	public OverseasPayeeInfoFragment(OverseasChinaBankRemittanceInfoInputActivity activity) {
//		this.activity = activity;
//	}


	@Override
	public void onAttach(Activity activity) {
		this.activity = (OverseasChinaBankRemittanceInfoInputActivity)activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.remittance_info_input_overpayee, null);
		findView(mMainView);
		viewSet();
		return mMainView;
	}

	private void findView(View mMainView) {

		//spArea = (Spinner) mMainView.findViewById(R.id.sp_area);
		tv_over_area = (TextView) mMainView.findViewById(R.id.tv_over_area);
		tvCountry = (TextView) mMainView.findViewById(R.id.tv_country);
		etName = (EditText) mMainView.findViewById(R.id.et_name);
		etAddress = (EditText) mMainView.findViewById(R.id.et_adress);
		etRbPhone = (EditText) mMainView.findViewById(R.id.et_rbPhone);
		cbSave = (CheckBox) mMainView.findViewById(R.id.cb_save);
		etAccNumber = (EditText) mMainView.findViewById(R.id.et_accNumber);
		tv_bankName = (TextView) mMainView.findViewById(R.id.tv_over_bankName);
		tv_over_swift = (TextView) mMainView.findViewById(R.id.tv_over_swift);

		tip00 =  (TextView) mMainView.findViewById(R.id.tip00);

		tv_over_area.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				requestPsnBOCPayeeBankRegionQuery();

			}
		});

		//添加气泡
		PopupWindowUtils.getInstance().setOnShowAllTextListener(activity, tv_bankName);

//		tv_bankName.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				requestPsnBOCPayeeBankInfoQuery();
//
//			}
//		});

	}

	private String gatheringArea;
	public void setBankName(String sear,String bocPayeeBankNameEN,String bocPayeeBankSwift ,String region) {
		RemittanceDataCenter.getInstance().setbankSearsOver(sear);
		tv_over_area.setText(sear);
		tv_bankName.setText(bocPayeeBankNameEN);
		tv_over_swift.setText(bocPayeeBankSwift);

		if (areaSelectListener != null) {
			areaSelectListener.onAreaSelect(Integer.parseInt(region));
		}

		gatheringArea = RemittanceDataCenter.payeeAreaCodeByCountry.get(region);

		if (region.equals("5")) {
			mMainView.findViewById(R.id.tip0).setVisibility(View.GONE);

			mMainView.findViewById(R.id.tip1).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tip9).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.VISIBLE);
			mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.VISIBLE);
		} else if(region.equals("2")){
			mMainView.findViewById(R.id.tip1).setVisibility(View.VISIBLE);
			mMainView.findViewById(R.id.tip9).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.VISIBLE);
			mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.GONE);

		}else if(region.equals("6")){
			mMainView.findViewById(R.id.tip1).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tip9).setVisibility(View.VISIBLE);
			mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.VISIBLE);
			mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.GONE);
		}else if(tv_over_area.getText().toString().trim().equals("")||tv_over_area.getText().toString().trim() == null) {
			mMainView.findViewById(R.id.tip1).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tip0).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tip9).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.VISIBLE);
			mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.GONE);
		} else {
			mMainView.findViewById(R.id.tip1).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tip9).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.VISIBLE);
			mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.GONE);
		}

//		if(tv_over_area.getText().toString().trim().equals("加拿大")||tv_over_area.getText().toString().trim().equals("巴西")) {
//			mMainView.findViewById(R.id.tip_new1).setVisibility(View.GONE);
//		} else {
//			mMainView.findViewById(R.id.tip_new1).setVisibility(View.VISIBLE);
//		}
		tip00.setVisibility(View.VISIBLE);
		if(tv_over_area.getText().equals("俄罗斯")) {

			tip00.setText("覆盖"+sear+"境内中行分支机构");
		} else {

			tip00.setText("覆盖"+sear+"境内中行分支机构，前8位一致即可汇达");
		}

		mMainView.findViewById(R.id.tv_name_error).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tv_adress_error).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tv_rbPhone_error).setVisibility(View.GONE);
		checkName();
		checkAdressOrRbPhone();
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


		EditTextUtils.setLengthMatcher(getActivity(), etAccNumber, 34);


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
//												if (!StringUtil.isNull(swift)) {
//													etSWIFT.setText(swift);
//												}
//												if (!StringUtil.isNull(bankFullName)) {
//													etBankFullName.setText(bankFullName);
//												}
//												if (!StringUtil.isNull(payeeBankAdd)) {
//													etRbAdress.setText(payeeBankAdd);
//												}
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


		//RemittanceCurrencyAdapter mAdapter = new RemittanceCurrencyAdapter(activity, RemittanceDataCenter.payeeArea);
		//spArea.setAdapter(mAdapter);
		//spArea.setOnItemSelectedListener(selectAreaListener);
		tvCountry.setOnClickListener(selectCountryListener);
		mMainView.findViewById(R.id.tip0).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tip9).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tv_rbPhoneTip).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tv_name_error).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tv_adress_error).setVisibility(View.GONE);
		mMainView.findViewById(R.id.tv_rbPhone_error).setVisibility(View.GONE);
		initViewData();
	}

	/** 初始化数据 */
	private void initViewData() {
		if (state == 1) {
			// 先清空数据，防止多次选择模板时数据混
			etName.setText("");
			etAccNumber.setText("");
			//etSWIFT.setText("");
			tv_over_swift.setText("");
			etAddress.setText("");
			//etBankNumber.setText("");

			etRbPhone.setText("");
			//etRbAdress.setText("");
			tv_over_area.setText("");
			//etBankFullName.setText("");
			tv_bankName.setText("");


			cbSave.setChecked(false);
			cbSave.setEnabled(false);
			detailMap = RemittanceDataCenter.getInstance().getMapPsnInternationalTransferTemplateDetailQuery();

			//spArea.setSelection(RemittanceDataCenter.payeeAreaCode.indexOf(detailMap.get(Remittance.GATHERINGAREA)));

			tv_over_area.setText((CharSequence) RemittanceDataCenter.getInstance().getmapPayeeBankOver().get("bocPayeeBankRegionCN"));
			RemittanceDataCenter.getInstance().setbankSearsOver((String) RemittanceDataCenter.getInstance().getmapPayeeBankOver().get("bocPayeeBankRegionCN"));

			String region = (String) RemittanceDataCenter.getInstance().getmapPayeeBankOver().get("region");
			gatheringArea = RemittanceDataCenter.payeeAreaCodeByCountry.get(region);

			if (region.equals("5")) {
				mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.GONE);
				mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.VISIBLE);
			} else {
				mMainView.findViewById(R.id.ll_otherAdress).setVisibility(View.VISIBLE);
				mMainView.findViewById(R.id.ll_rbPhone).setVisibility(View.GONE);
			}
			tv_bankName.setText((String) detailMap.get(Remittance.PAYEEBANKNAME));
			tv_over_swift.setText((String) detailMap.get(Remittance.PAYEEBANKSWIFT));
			etAccNumber.setText((String) detailMap.get(Remittance.PAYEEACTNO));


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
//			if(tv_over_area.getText().toString().trim().equals("加拿大")||tv_over_area.getText().toString().trim().equals("巴西")) {
//				mMainView.findViewById(R.id.tip_new1).setVisibility(View.GONE);
//			} else {
//				mMainView.findViewById(R.id.tip_new1).setVisibility(View.VISIBLE);
//			}
		}
	}

	/** 继续初始化数据1 */
	private void initDataContinue1(final Map<String, Object> detailMap) {
		detailMap.get(Remittance.GATHERINGAREA);
		//tv_over_area.getText().toString().trim().equals("日本")
		if (detailMap.get(Remittance.GATHERINGAREA).equals("JP")) {
			String payeeEnName = (String) detailMap.get(Remittance.PAYEEENNAME);
			String rbPhone = (String) detailMap.get(Remittance.PAYEEENADDRESS);
			if (rbPhone.length() > 105 || (payeeEnName.length() > 35 && rbPhone.length() > 70)) {
				BaseDroidApp.getInstanse().createDialog(null, "收款人联系电话超过最大字符数", new OnClickListener() {

					@Override
					public void onClick(View v) {
						BaseDroidApp.getInstanse().dismissErrorDialog();
						//initDataContinue2(detailMap);
					}
				});

			} else {
				etRbPhone.setText((String) detailMap.get(Remittance.PAYEEENADDRESS));
				LogGloble.i("100","222---");
				//initDataContinue2(detailMap);
			}
		} else {
			String payeeEnName = (String) detailMap.get(Remittance.PAYEEENNAME);
			String adress = (String) detailMap.get(Remittance.PAYEEENADDRESS);
			if (adress.length() > 105 || (payeeEnName.length() > 35 && adress.length() > 70)) {
				BaseDroidApp.getInstanse().createDialog(null, "收款人地址超过最大字符数", new OnClickListener() {

					@Override
					public void onClick(View v) {
						BaseDroidApp.getInstanse().dismissErrorDialog();
						//initDataContinue2(detailMap);
					}
				});

			} else {

				etAddress.setText((String) detailMap.get(Remittance.PAYEEENADDRESS));
				//initDataContinue2(detailMap);
				LogGloble.i("100","444---");
			}
		}
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

	private boolean checkAdressOrRbPhone() {
		etName.removeTextChangedListener(twlName);
		int tempLength;
		if (tv_over_area.getText().toString().trim().equals("日本")) {
			tempLength = 140 - etRbPhone.getText().toString().trim().length();
		} else {
			tempLength = 140 - etAddress.getText().toString().trim().length();
		}
		twlName = new TextWatcherLimit(activity, etName, tempLength >= 70 ? 70 : 35);
		etName.addTextChangedListener(twlName);

		String text = tv_over_area.getText().toString().trim().equals("日本") ? etRbPhone.getText().toString().trim() : etAddress.getText().toString().trim();
		TextView tv;
		if (tv_over_area.getText().toString().trim().equals("日本")) {
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

	private boolean checkName() {
		int tempLength = 140 - etName.getText().toString().trim().length();
		if (tv_over_area.getText().toString().trim().equals("日本")) {
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


	/** 设置选择汇款地区的监听者 */
	public void setOnAreaSelectListener(OnAreaSelectListener listener) {
		areaSelectListener = listener;
	}

	/** 选择汇款地区监听 */
	private OnItemSelectedListener selectAreaListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			//SelectAreaViewController.viewControlOver(mMainView, position);
			if (areaSelectListener != null) {
				areaSelectListener.onAreaSelect(position);
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

	public void initViewZero() {

		tv_over_area.setText("");
		tv_bankName.setText("") ;
		tv_over_swift.setText("");

		etName.setText("");
		etAccNumber.setText("");
		etAddress.setText("");
		etRbPhone.setText("");

	}

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
					OverseasPayeeInfoFragment.this.chooseCountry(countryCode);
				}
			});
		}
	};

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

	/**
	 * 调用查询收款银行信息接口
	 */
//	public void requestPsnBOCPayeeBankInfoQuery() {
//		Map<String, Object> params = new HashMap<String, Object>();
//		BaseHttpEngine.showProgressDialog();
//		activity.getHttpTools().requestHttp(Remittance.PSNBOCPAYEEBANKINFOQUERY, "requestPsnBOCPayeeBankInfoQueryCallBack", params, false);
//
//	}

	/**
	 *
	 * 调用境外中行收款行所属国家（地区）查询接口
	 */
	public void requestPsnBOCPayeeBankRegionQuery() {

		Map<String, Object> params = new HashMap<String, Object>();
		BaseHttpEngine.showProgressDialog();
		activity.getHttpTools().requestHttp("PsnBOCPayeeBankRegionQuery", "requestPsnBOCPayeeBankRegionQueryCallBack", params, false);

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
		params.put(Remittance.GATHERINGAREA,gatheringArea );

		params.put(Remittance.PAYEEPERMANENTCOUNTRY, chooseCountryCode);
		params.put(Remittance.PAYEEENNAME, etName.getText().toString().trim().toUpperCase());
		userInput.put(Remittance.PAYEEENNAME, etName.getText().toString().trim());
		params.put(Remittance.PAYEEACTNO, etAccNumber.getText().toString().trim().toUpperCase());
		userInput.put(Remittance.PAYEEACTNO, etAccNumber.getText().toString().trim());
		params.put(Remittance.PAYEEBANKSWIFT, tv_over_swift.getText().toString().trim().toUpperCase());
		userInput.put(Remittance.PAYEEBANKSWIFT, tv_over_swift.getText().toString().trim());
		params.put(Remittance.PAYEEBANKFULLNAME, (tv_bankName.getText().toString().trim()).toUpperCase());
		userInput.put(Remittance.PAYEEBANKFULLNAME, tv_bankName.getText().toString().trim());

		params.put(Remittance.TOSAVETEMPLATE, cbSave.isChecked() ? "1" : "0");

		if (tv_over_area.getText().toString().trim().equals("日本")) {
			params.put(Remittance.PAYEEENADDRESS, etRbPhone.getText().toString().trim().toUpperCase());
			userInput.put(Remittance.PAYEEENADDRESS, etRbPhone.getText().toString().trim());
			params.put(Remittance.PAYEEBANKADD, null);    //收款银行地址
			userInput.put(Remittance.PAYEEBANKADD, null); //收款银行地址
		} else if (tv_over_area.getText().toString().trim().equals("加拿大")) {
			params.put(Remittance.PAYEEENADDRESS, etAddress.getText().toString().trim().toUpperCase());
			userInput.put(Remittance.PAYEEENADDRESS, etAddress.getText().toString().trim());
		} else {
			params.put(Remittance.PAYEEENADDRESS, etAddress.getText().toString().trim().toUpperCase());
			userInput.put(Remittance.PAYEEENADDRESS, etAddress.getText().toString().trim());
		}
		if (gatheringArea.equals("AU") || gatheringArea.equals("CA")) {
			params.put(Remittance.PAYEEBANKNUM, null); //收款银行行号
			userInput.put(Remittance.PAYEEBANKNUM, null);// 收款银行行号
		}
		LogGloble.i(TAG, params.toString());
	}


	/** 校验 */
	private boolean submitRegexp(boolean required) {

//		if (StringUtil.isNull(chooseCountryCode)) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择收款人常驻国家（地区）");
//			return false;
//		}

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();

		// 收款银行国家
		if (onlyRegular(required, tv_over_area.getText().toString().trim())) {
			RegexpBean name = new RegexpBean(RemittanceContent.PAYBANKCOUNTRY_CN, tv_over_area.getText().toString().trim(), RemittanceContent.PAYBANKCOUNTRY);
			lists.add(name);

		}

		// 收款银行名称
		if (onlyRegular(required, tv_bankName.getText().toString().trim())) {
			RegexpBean name = new RegexpBean(RemittanceContent.BANKNAME_CN, tv_bankName.getText().toString().trim(), RemittanceContent.REMITTANCEPAYEEBANKNAME);
			lists.add(name);

		}

		// 收款银行swift代码
		if (onlyRegular(required, tv_over_swift.getText().toString().trim())) {
			RegexpBean name = new RegexpBean(RemittanceContent.PAYEESWIFTCODE_CN, tv_over_swift.getText().toString().trim(), RemittanceContent.PAYEESWIFTCODE);
			lists.add(name);

		}

//		String strSWIFT = etSWIFT.getText().toString().trim();
//		if (strSWIFT.length() == 8 || strSWIFT.length() == 11) {
//			if (strSWIFT.length() == 8) {
//				etSWIFT.setText(strSWIFT + "XXX");
//			}
//
//			String swift = strSWIFT.substring(4, 6).toUpperCase();
//			if (!swift.equals("CN")) {
//				if (onlyRegular(required, etSWIFT.getText().toString().trim())) {
//					RegexpBean name = new RegexpBean(RemittanceContent.PAYEESWIFTCODE_CN, etSWIFT.getText().toString().trim(), RemittanceContent.PAYEESWIFTCODE);
//					lists.add(name);
//					LogGloble.i("100", name.toString());
//				}
//			} else {
//				BaseDroidApp.getInstanse().showInfoMessageDialog("SWIFT代码请输入8位或11位字母、数字，其中第五、六位不为CN，最大长度11个字符");
//				return false;
//			}
//		} else {
//			if (onlyRegular(required, etSWIFT.getText().toString().trim())) {
//				RegexpBean name = new RegexpBean(RemittanceContent.PAYEESWIFTCODE_CN, etSWIFT.getText().toString().trim(), RemittanceContent.PAYEESWIFTCODE);
//				lists.add(name);
//				LogGloble.i("100", name.toString());
//			}
//		}

		//收款人账号
		if (onlyRegular(required, etAccNumber.getText().toString().trim())) {
			RegexpBean name = new RegexpBean(RemittanceContent.PAYEEACCOUNTNUMBER_CN, etAccNumber.getText().toString().trim(), RemittanceContent.PAYEEACCOUNTNUMBER);
			lists.add(name);

		}

//		请选择收款人常驻国家（地区）
		if (onlyRegular(required, chooseCountryCode)) {
			RegexpBean name = new RegexpBean(RemittanceContent.PAYMANCOUNTRY_CN, chooseCountryCode, RemittanceContent.PAYMANCOUNTRY);
			lists.add(name);

		}


		//收款人名称
		if (onlyRegular(required, etName.getText().toString().trim())) {
			RegexpBean name = new RegexpBean(RemittanceContent.PAYEENAME_CN, etName.getText().toString().trim(), RemittanceContent.PAYEENAME);
			lists.add(name);

		}

		if (!tv_over_area.getText().toString().trim().equals("日本")) {
			if (tv_over_area.getText().toString().trim().equals("加拿大") || onlyRegular(false, etAddress.getText().toString().trim()) || tv_over_area.getText().toString().trim().equals("巴西")) {

				RegexpBean name = new RegexpBean(RemittanceContent.PAYEEADDRESS_CN, etAddress.getText().toString().trim(), RemittanceContent.REMITTERADDRESS);
				lists.add(name);

			}
		} else {
			if (onlyRegular(required, etRbPhone.getText().toString().trim())) {

				RegexpBean name = new RegexpBean(RemittanceContent.PAYEEPHONE_CN, etRbPhone.getText().toString().trim(), RemittanceContent.REMITTERADDRESS);
				lists.add(name);

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
			if (tv_over_area.getText().toString().trim().equals("日本")) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("“收款人联系电话”栏位中，第1、第36、第71个字符不能为“-”或“:”，请调整。");
				return false;
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog("“收款人地址”栏位中，第1、第36、第71个字符不能为“-”或“:”，请调整。");
				return false;
			}
		}
		return true;
	}
}
