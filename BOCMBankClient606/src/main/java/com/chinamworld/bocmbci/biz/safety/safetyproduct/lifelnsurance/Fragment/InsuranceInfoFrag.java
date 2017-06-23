package com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance.ChoosePayTypeDialogActivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class InsuranceInfoFrag extends LifeInsuranceBaseFragment {

	private static final String TAG = "InsuranceInfoFrag";
	/** 缴费年期类型 */
	private TextView tvPayYearType;
	/** 缴费期间 */
	private TextView tvPayYear;
	/** 保障年期类型 */
	private TextView tvInsuYearType;
	/** 保障期间 */
	private TextView tvInsuYear;
	/** 投保份数 */
	private EditText etCopies;
	/** 保费 */
	private EditText etRiskPrem;
	/** 保额 */
	private EditText etCoverage;
	/** 计算依据 */
	private String calFlag;
	/** 缴费种类信息列表 */
	private List<Map<String, Object>> listPayTypeInfo;
	/** 用户选择的缴费类型的下标值 */
	private int selectIndex = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.safety_life_input_insurance_info_frag, null);
		try {
			findView();
			viewSet();
		} catch (Exception e) {
			LogGloble.e(TAG, e.toString());
		}
		return mMainView;
	}

	@Override
	protected void findView() {
		etCopies = (EditText) mMainView.findViewById(R.id.et_copies);
		etRiskPrem = (EditText) mMainView.findViewById(R.id.et_riskPrem);
		etCoverage = (EditText) mMainView.findViewById(R.id.et_coverage);

		tvPayYearType = (TextView) mMainView.findViewById(R.id.tv_payYearType);
		tvPayYear = (TextView) mMainView.findViewById(R.id.tv_payYear);
		tvInsuYearType = (TextView) mMainView.findViewById(R.id.tv_insuYearType);
		tvInsuYear = (TextView) mMainView.findViewById(R.id.tv_insuYear);
	}

	@Override
	protected void viewSet() {
		int selectPosition = (Integer) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(SELECTPOSITION);
		Map<String, Object> selectInsur = SafetyDataCenter.getInstance().getListLifeInsuranceProductQuery().get(selectPosition);
		calFlag = String.valueOf(selectInsur.get(Safety.CALFLAG));
		if (calFlag.equals("0")) {
			// 按份数
			mMainView.findViewById(R.id.ll_copies).setVisibility(View.VISIBLE);
			EditTextUtils.setLengthMatcher(getActivity(), etCopies, 8);

//			List<String[]> d = SafetyDataCenter.getInstance().getControlInfoA();
//			for (int i = 0; i < d.size(); i++) {
//				String[] state = d.get(i);
//				if (state[0].equals("D1")) {
//					EditTextUtils.setLengthMatcher(getActivity(), etCopies, Integer.valueOf(state[2]));
//					break;
//				}
//			}
		} else if (calFlag.equals("1")) {
			// 按保额
			mMainView.findViewById(R.id.ll_coverage).setVisibility(View.VISIBLE);
			EditTextUtils.setLengthMatcher(getActivity(), etCoverage, 13);

//			List<String[]> d = SafetyDataCenter.getInstance().getControlInfoA();
//			for (int i = 0; i < d.size(); i++) {
//				String[] state = d.get(i);
//				if (state[0].equals("D3")) {
//					EditTextUtils.setLengthMatcher(getActivity(), etCoverage, Integer.valueOf(state[2]));
//					break;
//				}
//			}
		} else if (calFlag.equals("2")) {
			// 按保费
			mMainView.findViewById(R.id.ll_riskPrem).setVisibility(View.VISIBLE);
			EditTextUtils.setLengthMatcher(getActivity(), etRiskPrem, 13);

//			List<String[]> d = SafetyDataCenter.getInstance().getControlInfoA();
//			for (int i = 0; i < d.size(); i++) {
//				String[] state = d.get(i);
//				if (state[0].equals("D2")) {
//					EditTextUtils.setLengthMatcher(getActivity(), etRiskPrem, Integer.valueOf(state[2]));
//					break;
//				}
//			}
		}
		mMainView.findViewById(R.id.ll_copiesTip).setVisibility(View.VISIBLE);
		((TextView) mMainView.findViewById(R.id.tv_copiesTip)).setText((String) selectInsur.get(Safety.SALEDESCRIP));

		tvPayYearType.setOnClickListener(showPayTypeInfoViewListener);
		tvPayYear.setOnClickListener(showPayTypeInfoViewListener);
		tvInsuYearType.setOnClickListener(showPayTypeInfoViewListener);
		tvInsuYear.setOnClickListener(showPayTypeInfoViewListener);
	}

	@Override
	public boolean submit() {
		if (inputRegexp()) {
			Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapUserInput();
			Map<String, Object> userInputTemp = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
			if (calFlag.equals("0")) {
				// 按份数
				userInput.put(Safety.SAFETY_HOLD_RISK_UNIT, etCopies.getText().toString().trim());
				Map<String, Object> productMap = SafetyDataCenter.getInstance().getListLifeInsuranceProductQuery().get((Integer) userInputTemp.get(SELECTPOSITION));
				userInput.put(Safety.UNITATTR, productMap.get(Safety.ATTR));
				userInput.put(Safety.UNITPRICE, productMap.get(Safety.PRICE));
			} else if (calFlag.equals("1")) {
				// 按保额
				userInput.put(Safety.SAFETY_HOLD_RISK_AMT, etCoverage.getText().toString().trim());
			} else if (calFlag.equals("2")) {
				// 按保费
				userInput.put(Safety.SAFETY_HOLD_RISK_PREM, etRiskPrem.getText().toString().trim());
			}

			LogGloble.i(TAG, userInput.toString());
			return true;
		}
		return false;
	}

	/** 输入字段校验 */
	private boolean inputRegexp() {
		if (selectIndex < 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.safety_lifeInsur_noChoosePayYearType));
			return false;
		}

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();

		if (calFlag.equals("0")) {
			if (onlyRegular(true, etCopies.getText().toString())) {
				RegexpBean rb = new RegexpBean(SafetyConstant.COPIES, etCopies.getText().toString(), SafetyConstant.EIGHTNUMBER);
				rb.setExtentParam(etCopies);
				lists.add(rb);
			}
		} else if (calFlag.equals("1")) {
			if (onlyRegular(true, etCoverage.getText().toString())) {
				RegexpBean rb = new RegexpBean(SafetyConstant.COVERAGE, etCoverage.getText().toString(), SafetyConstant.LIFEINSURAMOUNT);
				rb.setExtentParam(etCoverage);
				lists.add(rb);
			}
		} else if (calFlag.equals("2")) {
			if (onlyRegular(true, etRiskPrem.getText().toString())) {
				RegexpBean rb = new RegexpBean(SafetyConstant.RISKPREM, etRiskPrem.getText().toString(), SafetyConstant.LIFEINSURAMOUNT);
				rb.setExtentParam(etRiskPrem);
				lists.add(rb);
			}
		}

		RegexpBean result = RegexpUtils.regexpDateWithRgexpBean(lists, null);
		if (result != null) {
			View v = (View) result.getExtentParam();
			changeViewBg(v);
			return false;
		}
		return true;
	}

	/** 是否只作正则校验 */
	private boolean onlyRegular(Boolean required, String content) {
		if ((!required && !StringUtil.isNull(content)) || required) {
			return true;
		}
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			if (resultCode >= 0) {
				selectIndex = resultCode;
				Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapUserInput();
				Map<String, Object> userInputTemp = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
				if (userInput.containsKey(Safety.PAYYEARTYPE)) {
					userInput.remove(Safety.PAYYEARTYPE);
					userInputTemp.remove(Safety.PAYYEARNAME);
					userInput.remove(Safety.PAYYEAR);
					userInput.remove(Safety.INSUYEARTYPE);
					userInputTemp.remove(Safety.INSUYEARNAME);
					userInput.remove(Safety.INSUYEAR);

					LogGloble.i(TAG, "remove payYearType");
				}
				userInput.put(Safety.PAYYEARTYPE, listPayTypeInfo.get(resultCode).get(Safety.PAYYEARTYPE));
				userInputTemp.put(Safety.PAYYEARNAME, listPayTypeInfo.get(resultCode).get(Safety.PAYYEARNAME));
				userInput.put(Safety.PAYYEAR, listPayTypeInfo.get(resultCode).get(Safety.PAYYEAR));
				userInput.put(Safety.INSUYEARTYPE, listPayTypeInfo.get(resultCode).get(Safety.INSUYEARTYPE));
				userInputTemp.put(Safety.INSUYEARNAME, listPayTypeInfo.get(resultCode).get(Safety.INSUYEARNAME));
				userInput.put(Safety.INSUYEAR, listPayTypeInfo.get(resultCode).get(Safety.INSUYEAR));

				tvPayYearType.setText((String) listPayTypeInfo.get(resultCode).get(Safety.PAYYEARNAME));
				tvPayYear.setText((String) listPayTypeInfo.get(resultCode).get(Safety.PAYYEAR));
				tvInsuYearType.setText((String) listPayTypeInfo.get(resultCode).get(Safety.INSUYEARNAME));
				tvInsuYear.setText((String) listPayTypeInfo.get(resultCode).get(Safety.INSUYEAR));
			}
		}
	}

	/** 缴费类型监听 */
	private OnClickListener showPayTypeInfoViewListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseHttpEngine.showProgressDialog();
			Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapUserInput();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Safety.INSURANCE_ID, userInput.get(Safety.INSURANCE_ID));
			params.put(Safety.RISKCODE, userInput.get(Safety.MAINRISKCODE));
			params.put(Safety.SUBINSUID, userInput.get(Safety.SUBINSUID));
			((BaseActivity) getActivity()).getHttpTools().requestHttp(Safety.PSNINSURANCEPAYTYPEINFOQUERY, "requestPsnInsurancePayTypeInfoQueryCallBack", params);
		}
	};

	/** 设置缴费种类，Activity得到接口数据之后调用 */
	public void setPayTypeInfo(List<Map<String, Object>> list) {
		listPayTypeInfo = list;
		Intent intent = new Intent(getActivity(), ChoosePayTypeDialogActivity.class);
		startActivityForResult(intent, 100);
	}
}
