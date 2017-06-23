package com.chinamworld.bocmbci.biz.safety.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.biz.safety.carsafety.CarSafetyBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class WeiShangPaiFragment extends CarSafetyBaseFragment {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 按首字母排序后的行驶省份列表*/
	private List<String> listZone;
	/** 品牌型号，新车未上牌时显示 */
	private EditText etBrandName;
	/** 发动机号码，新车未上牌时显示 */
	private EditText etEngineNo;
	/** 车辆识别代码，新车未上牌时显示 */
	private EditText etFrameNo;
	/** 购车发票日期，新车未上牌时显示 */
	private TextView tvFapiaoDate;
	/** 发票号，新车未上牌时显示 */
	private EditText etFapiaoNumber;
	/** 燃油类型 */
	private Spinner spFuelType;
	/** 点击“示例”弹出的图片 */
	private ImageView mView;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	public WeiShangPaiFragment(CarSafetyBaseActivity activity) {
//		this.activity = activity;
//	}

	@Override
	public void onAttach(Activity activity) {
		this.activity = (CarSafetyBaseActivity)activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 续保只支持已上牌车辆，这里给默认新单
		continueFlag = "false";
		mMainView = inflater.inflate(R.layout.safety_carsafety_fragment_weishangpai, null);
		findView(mMainView);
		viewSet();
		return mMainView;
	}
	
	/** 控件赋值 */
	private void findView(View mMainView) {
		spZone = (Spinner) mMainView.findViewById(R.id.sp_zone);
		spCity = (Spinner) mMainView.findViewById(R.id.sp_city);
		etCarOwnerName = (EditText) mMainView.findViewById(R.id.et_carOwnerName);
		spCarOwnerIdType = (Spinner) mMainView.findViewById(R.id.sp_carOwnerIdType);
		etCarOwnerId = (EditText) mMainView.findViewById(R.id.et_carOwnerId);
		etBrandName = (EditText) mMainView.findViewById(R.id.et_brandName);
		etEngineNo = (EditText) mMainView.findViewById(R.id.et_engineNo);
		etFrameNo = (EditText) mMainView.findViewById(R.id.et_frameNo);
		tvFapiaoDate = (TextView) mMainView.findViewById(R.id.tv_fapiaoDate);
		etFapiaoNumber = (EditText) mMainView.findViewById(R.id.et_fapiaoNumber);
		spFuelType = (Spinner) mMainView.findViewById(R.id.sp_fuelType);
	}
	
	/** 控件设置 */
	private void viewSet() {
//		tvFapiaoDate.setText(SafetyDataCenter.getInstance().getSysTime());
		tvFapiaoDate.setText("请选择");
		tvFapiaoDate.setOnClickListener(chooseDateClick);
		SafetyUtils.initSpinnerView(activity, spCarOwnerIdType, SafetyDataCenter.credTypeList);
		spZone.setOnItemSelectedListener(zoneSelectListener);
		exampleClick();
		spCarOwnerIdType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if ((spCarOwnerIdType.getSelectedItemPosition() == 0)
						|| (spCarOwnerIdType.getSelectedItemPosition() == 1)
						|| (spCarOwnerIdType.getSelectedItemPosition() == 3)) {
					etCarOwnerId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
				}else{
					etCarOwnerId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});

		SafetyUtils.initSpinnerView(activity, spZone, listZone);
		if (tempState) {
			initCarSafetyTempContinue();
		}

		EditTextUtils.setLengthMatcher(activity, etBrandName, 60);
		EditTextUtils.setLengthMatcher(activity, etEngineNo, 30);
		EditTextUtils.setLengthMatcher(activity, etFrameNo, 17);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(activity, ((TextView) mMainView.findViewById(R.id.tv_keyForFrameNo)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(activity, ((TextView) mMainView.findViewById(R.id.tv_keyForFapiaoDate)));
	}
	
	public void setZone(List<String> list) {
		listZone = list;
	}

	@Override
	public void initViewData(Map<String,Object> tempInfo) {
		super.initViewData(tempInfo);
		String cityCode = (String) tempInfo.get(Safety.CITYCODE);
		String cityCodeSim = cityCode.substring(0, 2) + "0000";
		
		// 未上牌承保地区省份是可选的，暂存单恢复的时候按下标设置就可以
		spZone.setSelection(listZone.indexOf(SafetyDataCenter.mapCityCode_CN.get(cityCodeSim)));

		etBrandName.setText((String) tempInfo.get(Safety.BRANDNAME));
		etEngineNo.setText((String) tempInfo.get(Safety.ENGINENO));
		etFrameNo.setText((String) tempInfo.get(Safety.FRAMENO));
		if (cityCode.equals("110000")) {
			String invoiceDate = (String) tempInfo.get(Safety.INVOICEDATE);
			if (StringUtil.isNull(invoiceDate)) {
//				tvFapiaoDate.setText(SafetyDataCenter.getInstance().getSysTime());
				tvFapiaoDate.setText("请选择");
			} else {
				tvFapiaoDate.setText(invoiceDate);
			}
			etFapiaoNumber.setText((String) tempInfo.get(Safety.INVOICENO));
			spFuelType.setSelection(SafetyDataCenter.BJfuelTypeCode.indexOf(tempInfo.get(Safety.FUELTYPE)));
		} else {
			String enrollDate = (String) tempInfo.get(Safety.ENROLLDATE);
			if (StringUtil.isNull(enrollDate)) {
//				tvFapiaoDate.setText(SafetyDataCenter.getInstance().getSysTime());
				tvFapiaoDate.setText("请选择");
			} else {
				tvFapiaoDate.setText(enrollDate);
			}
			spFuelType.setSelection(SafetyDataCenter.fuelTypeCode.indexOf(tempInfo.get(Safety.FUELTYPE)));
		}
	};
	
	/** 初始化车险暂存保单继续投保 */
	private void initCarSafetyTempContinue() {
		tempInfo = SafetyDataCenter.getInstance().getMapAutoInsurPolicyDetailQry();
		initViewData(tempInfo);
		// 把暂存单名称放到参数列表里
		SafetyDataCenter.getInstance().getMapSaveParams().put(Safety.POLICYNAME, tempInfo.get(Safety.POLICYNAME));
	}
	
	@Override
	public void zoneChange() {
		super.zoneChange();
		if (cityCode.equals("110000")) {
			// 未上牌北京显示 ，设置日期字段为“购车发票日期”，显示发票号字段，为燃油类型绑北京数据
			((TextView) mMainView.findViewById(R.id.tv_keyForFapiaoDate)).setText(getResources().getString(R.string.safety_carinfo_fapiaoDate));
			mMainView.findViewById(R.id.ll_fapiaoNumber).setVisibility(View.VISIBLE);
			SafetyUtils.initSpinnerView(activity, spFuelType, SafetyDataCenter.BJfuelTypeCN);
		} else {
			// 未上牌非北京显示，设置日期字段为“注册日期”，隐藏发票号字段，为燃油类型绑全国数据
			((TextView) mMainView.findViewById(R.id.tv_keyForFapiaoDate)).setText(getResources().getString(R.string.safety_carinfo_enrollDate));
			mMainView.findViewById(R.id.ll_fapiaoNumber).setVisibility(View.GONE);
			SafetyUtils.initSpinnerView(activity, spFuelType, SafetyDataCenter.fuelTypeCN);
		}
		if (tempState) {
			if(!StringUtil.isNullOrEmpty(tempInfo)){
				String cityCode = (String) tempInfo.get(Safety.CITYCODE);
				if (cityCode.equals("110000")) {	
					// 北京
					spFuelType.setSelection(SafetyDataCenter.BJfuelTypeCode.indexOf(tempInfo.get(Safety.FUELTYPE)));
				} else {	
					// 全国
					spFuelType.setSelection(SafetyDataCenter.fuelTypeCode.indexOf(tempInfo.get(Safety.FUELTYPE)));
				}
			}				
		}
	}

	@SuppressWarnings("unchecked")
	public void query() {
		Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		if (cityCode.equals("110000") && !logInfo.get(Comm.IDENTITYTYPE).equals("1")) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					"根据保险监管要求，车险投保人的证件类型必须为身份证。请到我行任一网点变更您的证件信息后再进行投保。");
			return;
		}
		if (!submitRegexp(true)) {
			return;
		}

		if (StringUtil.isNullOrEmpty(etBrandName.getText().toString())) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入品牌型号");
			return;
		}
		if (StringUtil.isNullOrEmpty(etEngineNo.getText().toString())) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入发动机号");
			return;
		}
		if (StringUtil.isNullOrEmpty(etFrameNo.getText().toString())) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入车辆识别代码");
			return;
		} else if (etFrameNo.getText().toString().length() < 17) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("车辆识别代码请输入长度为17位的字符");
			return;
		}
		if ("请选择".equals(tvFapiaoDate.getText().toString())) {
			if ("110000".equals(cityCode)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择购车发票日期");
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择注册日期");
			}
			return;
		}
		if (!activity.regexpDate(tvFapiaoDate.getText().toString(), cityCode.equals("110000"))) {
			return;
		}
		saveUserInput();
		putSaveParams();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Safety.LICENSENO, "");
		params.put(Safety.CITYCODE, cityCode);
		params.put(Safety.NEWCARFLAG, "1");
		params.put(Safety.BRANDNAME, etBrandName.getText().toString().toUpperCase());
		params.put(Safety.ENGINENO, etEngineNo.getText().toString().toUpperCase());
		params.put(Safety.FRAMENO, etFrameNo.getText().toString().toUpperCase());
		params.put(Safety.ENROLLDATE, tvFapiaoDate.getText().toString());
		params.put(Safety.ZONENO, zoneNo);
		BaseHttpEngine.showProgressDialog();
		activity.getHttpTools().requestHttp(Safety.METHOD_PSNAUTOINSURANCEQUERYAUTOTYPE, "requestPsnAutoInsuranceQueryAutoTypeCallBack", params, true);
	}
	
	public void save() {
		if (submitRegexp(false)) {
			if (!StringUtil.isNull(etFrameNo.getText().toString()) && etFrameNo.getText().toString().length() < 17) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("车辆识别代码请输入长度为17位的字符");
				return;
			}
			putSaveParams();
			activity.showSaveDialog();
		}
	}
	
	@Override
	public void putSaveParams() {
		super.putSaveParams();
		Map<String, Object> params = SafetyDataCenter.getInstance().getMapSaveParams();
		// 如果是从后面页面返回回来的，需要把可能与页面数据状态不同步的字段清空再重新赋值
		if (!StringUtil.isNullOrEmpty(params.get(Safety.LICENSENO))) {
			params.remove(Safety.LICENSENO);
		}
		if (!StringUtil.isNullOrEmpty(params.get(Safety.INVOICEDATE))) {
			params.remove(Safety.INVOICEDATE);
		}
		if (!StringUtil.isNullOrEmpty(params.get(Safety.INVOICENO))) {
			params.remove(Safety.INVOICENO);
		}
		if (!StringUtil.isNullOrEmpty(params.get(Safety.ENROLLDATE))) {
			params.remove(Safety.ENROLLDATE);
		}
		params.put(Safety.ISEDIT, "1111100000");
		params.put(Safety.BRANDNAME, etBrandName.getText().toString().trim());
		params.put(Safety.ENGINENO, etEngineNo.getText().toString().trim());
		params.put(Safety.FRAMENO, etFrameNo.getText().toString().trim());
		if (cityCode.equals("110000")) {
			params.put(Safety.INVOICEDATE, "请选择".equals(tvFapiaoDate.getText().toString()) ? "" : tvFapiaoDate.getText().toString());
			params.put(Safety.INVOICENO, etFapiaoNumber.getText().toString().trim().toUpperCase());
			params.put(Safety.FUELTYPE, SafetyDataCenter.BJfuelTypeCode.get(spFuelType.getSelectedItemPosition()));
		} else {
			params.put(Safety.ENROLLDATE, "请选择".equals(tvFapiaoDate.getText().toString()) ? "" : tvFapiaoDate.getText().toString());
			params.put(Safety.FUELTYPE, SafetyDataCenter.fuelTypeCode.get(spFuelType.getSelectedItemPosition()));
		}
		System.out.println("车辆基本信息页-暂存保单参数列表\n" + params);
	}
	
	@Override
	public void saveUserInput() {
		super.saveUserInput();
		Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
		if (!StringUtil.isNullOrEmpty(userInput.get(Safety.LICENSENO))) {
			userInput.remove(Safety.LICENSENO);
		}
		userInput.put(Safety.NEWCARFLAG, "1");
		userInput.put(Safety.BRANDNAME, etBrandName.getText().toString().trim().toUpperCase());
		userInput.put(Safety.ENGINENO, etEngineNo.getText().toString().trim().toUpperCase());
		userInput.put(Safety.FRAMENO, etFrameNo.getText().toString().trim().toUpperCase());
		userInput.put(Safety.ENROLLDATE, tvFapiaoDate.getText().toString());
		if (zoneNo.equals("0010")) {
			userInput.put(Safety.INVOICENO, etFapiaoNumber.getText().toString().trim().toUpperCase());
			userInput.put(Safety.FUELTYPE, SafetyDataCenter.BJfuelTypeCode.get(spFuelType.getSelectedItemPosition()));
		} else {
			userInput.put(Safety.FUELTYPE, SafetyDataCenter.fuelTypeCode.get(spFuelType.getSelectedItemPosition()));
		}
	}

	/** 提交数据校验 */
	public boolean submitRegexp(boolean required) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// 车主姓名
		if (onlyRegular(required, etCarOwnerName.getText().toString())) {
			RegexpBean carOwnerName = new RegexpBean(SafetyConstant.CAROWNERNAME, etCarOwnerName.getText().toString(), SafetyConstant.CAROWNER_NAME);
			lists.add(carOwnerName);
		}
		// 车主证件号码
		String AppIdType = spCarOwnerIdType.getSelectedItem().toString();
		if (AppIdType.equals(SafetyDataCenter.credTypeList.get(0)) 
				|| AppIdType.equals(SafetyDataCenter.credTypeList.get(1)) 
				|| AppIdType.equals(SafetyDataCenter.credTypeList.get(3))) {
			if (onlyRegular(required, etCarOwnerId.getText().toString().trim())) {
				RegexpBean carOwnerIdType = new RegexpBean(SafetyConstant.IDENTITYNUM_CAROWNER, etCarOwnerId.getText().toString().trim(), SafetyConstant.CAROWNER_ID);
				lists.add(carOwnerIdType);
			}
		} else {
			if (onlyRegular(required, etCarOwnerId.getText().toString().trim())) {
				RegexpBean carOwnerIdType = new RegexpBean(SafetyConstant.IDENTITYNUM_CAROWNER, etCarOwnerId.getText().toString().trim(), SafetyConstant.CARSAFETY_OTHERIDTYPE);
				lists.add(carOwnerIdType);
			}
		}
		if (zoneNo.equals("0010")) {
			// 发票号
			if (onlyRegular(required, etFapiaoNumber.getText().toString())) {
				RegexpBean fapiaoNumber = new RegexpBean(SafetyConstant.FAPIAONUMBER, etFapiaoNumber.getText().toString(), SafetyConstant.FAPIAO_NO);
				lists.add(fapiaoNumber);
			}
		}
		if (RegexpUtils.regexpDate(lists)) {
			return true;
		}
		if (!required) {
			// 暂时只有暂存时此方法参数传false，所以这里只判此参数
			if (!StringUtil.isNull(etFrameNo.getText().toString()) && etFrameNo.getText().toString().length() < 17) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("车辆识别代码请输入长度为17位的字符");
				return false;
			}
		}
		return false;
	}
	
	/** 只作正则校验  */
	private boolean onlyRegular(Boolean required, String content){
		if ((!required && !StringUtil.isNull(content)) || required) {
			return true;
		}
		return false;
	}
	
	/** 初始化未上牌界面五个“示例” */
	private void exampleClick() {
		mMainView.findViewById(R.id.example1).setOnClickListener(exampleClickListener);
		mMainView.findViewById(R.id.example2).setOnClickListener(exampleClickListener);
		mMainView.findViewById(R.id.example3).setOnClickListener(exampleClickListener);
		mMainView.findViewById(R.id.example4).setOnClickListener(exampleClickListener);
		mMainView.findViewById(R.id.example5).setOnClickListener(exampleClickListener);
	}
	
	/** 设置弹出的图片 */
	private void setShowImage(int resId) {
		if (mView == null) {
			mView = new ImageView(activity);
			mView.setImageResource(resId);
			activity.addContentView(mView, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			mView.setImageResource(resId);
		}
	}
	
	/** 点击“示例”弹出图片方法 */
	private void showView(final View v) {
		activity.hineLeftSideMenu();
		activity.findViewById(R.id.btn_show).setVisibility(View.GONE);
		int[] location = new int[2];
		v.getLocationOnScreen(location);
//		if (mView == null) {
//			mView = new ImageView(activity);
//			mView.setImageResource(R.drawable.pinpaixinghao);
//			activity.addContentView(mView, new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//		}
		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				hideView(v);
			}
		});

		AnimationSet animSet = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation((float) v.getWidth()
				/ ((View) v.getParent()).getWidth(), 1.0f,
				(float) v.getHeight() / ((View) v.getParent()).getHeight(),
				1.0f, location[0] + v.getWidth() / 2, location[1] + v.getHeight() / 2);
		sa.setDuration(10);
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1);
		aa.setDuration(10);
		animSet.addAnimation(sa);
		animSet.addAnimation(aa);
		mView.startAnimation(animSet);
		mView.setVisibility(View.VISIBLE);
	}

	/** 点击弹出的图片消失方法 */
	private void hideView(View v) {
		activity.showLeftSideMenu();
		activity.findViewById(R.id.btn_show).setVisibility(View.VISIBLE);
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		AnimationSet animSet = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(1, (float) v.getWidth()
				/ ((View) v.getParent()).getWidth(), 1, (float) v.getHeight()
				/ ((View) v.getParent()).getHeight(), location[0] + v.getWidth()
				/ 2, location[1] + v.getHeight() / 2);
		sa.setDuration(10);
		AlphaAnimation aa = new AlphaAnimation(1f, 0f);
		aa.setDuration(10);
		animSet.addAnimation(sa);
		animSet.addAnimation(aa);
		mView.startAnimation(animSet);
		mView.setVisibility(View.GONE);
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 未上牌界面五个“示例”点击监听 */
	OnClickListener exampleClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.example1:
				setShowImage(R.drawable.pinpaixinghao);
				break;
			case R.id.example2:
				setShowImage(R.drawable.fadongjihao);
				break;
			case R.id.example3:
				setShowImage(R.drawable.cheliangshibiedaima);
				break;
			case R.id.example4:
				setShowImage(R.drawable.fapiaoriqi);
				break;
			case R.id.example5:
				setShowImage(R.drawable.fapiaohao);
				break;
			}
			showView(v);
		}
	};

	/** 设置日期，注册日期控件的点击事件 */
	OnClickListener chooseDateClick = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			TextView tv = (TextView) v;
			String time = tv.getText().toString();
			if ("请选择".equals(time)) {
				time = SafetyDataCenter.getInstance().getSysTime();
			}
			int Year = 0;
			int Month = 0;
			int Day = 0;
			Year = Integer.parseInt(time.substring(0, 4));
			Month = Integer.parseInt(time.substring(5, 7));
			Day = Integer.parseInt(time.substring(8, 10));
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(activity, new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					StringBuilder date = new StringBuilder();
					date.append(String.valueOf(year));
					date.append("/");
					int month = monthOfYear + 1;
					date.append(((month < 10) ? ("0" + month) : (month + "")));
					date.append("/");
					date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth) : (dayOfMonth + "")));
					// 为日期赋值
					((TextView) v).setText(String.valueOf(date));
				}
			}, Year, Month - 1, Day);
			dialog.show();
		}
	};
}
