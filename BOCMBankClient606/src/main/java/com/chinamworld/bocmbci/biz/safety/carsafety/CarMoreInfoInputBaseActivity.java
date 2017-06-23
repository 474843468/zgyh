package com.chinamworld.bocmbci.biz.safety.carsafety;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public abstract class CarMoreInfoInputBaseActivity extends CarSafetyBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 品牌型号 */
	public EditText etBrandName;
	/** 发动机号码 */
	public EditText etEngineNo;
	/** 车辆识别代码 */
	public EditText etFrameNo;
	/** 注册日期 */
	public TextView tvEnrollDate;
	/** 燃油类型 */
	public Spinner spFuelType;
	/** 注册日期布局，行驶城市为北京时隐藏 */
	public LinearLayout llEnrollDate;
	/** 燃油类型布局，行驶城市为北京时隐藏 */
	public LinearLayout llFuelType;
	/** 页面主显示布局 */
	public View mMainView;
	/** 提交车型查询按钮 */
	public Button btnCommitQuery;
	/** 提交车型查询按钮 */
	public Button btnCommitQueryBig;
	/** 暂存按钮 */
	public Button btnSave;
	/** 点击“示例”弹出的图片 */
	public ImageView mView;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.safety_carsafety_more_carinfo);
		findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
		setTitle(R.string.safety_msgfill_title);
		initView();
	}
	

	/** 初始化界面元素 */
	public void initView() {
		mMainView.findViewById(R.id.tv_tip).setVisibility(View.VISIBLE);
		mLeftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceQueryAutoDetail())) {
					SafetyDataCenter.getInstance().getMapAutoInsuranceQueryAutoDetail().clear();
				}
				finish();
			}
		});
		
		((TextView) mMainView.findViewById(R.id.tv_safetytype)).setText("车险");
		((TextView) mMainView.findViewById(R.id.tv_company)).setText("中银保险有限公司");
		etBrandName = (EditText) mMainView.findViewById(R.id.et_brandName);
		EditTextUtils.setLengthMatcher(this, etBrandName, 60);
		etEngineNo = (EditText) mMainView.findViewById(R.id.et_engineNo);
		EditTextUtils.setLengthMatcher(this, etEngineNo, 30);
		etFrameNo = (EditText) mMainView.findViewById(R.id.et_frameNo);
		EditTextUtils.setLengthMatcher(this, etFrameNo, 17);
		tvEnrollDate = (TextView) mMainView.findViewById(R.id.tv_enrollDate);
		spFuelType = (Spinner) mMainView.findViewById(R.id.sp_fuelType);
		llEnrollDate = (LinearLayout) mMainView.findViewById(R.id.ll_enrollDate);
		llFuelType = (LinearLayout) mMainView.findViewById(R.id.ll_fuelType);
		btnCommitQuery = (Button) mMainView.findViewById(R.id.btnCommitQuery);
		btnCommitQueryBig = (Button) mMainView.findViewById(R.id.btnCommitQuery_big);
		btnSave = (Button) mMainView.findViewById(R.id.btnSave);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, ((TextView) mMainView.findViewById(R.id.tv_frameNo_key)));

		if (SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.ZONENO).equals("0010")) {
			// 如果行驶城市为北京
			llEnrollDate.setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_brandName).setVisibility(View.GONE);
			llFuelType.setVisibility(View.GONE);
		}
		SafetyUtils.initSpinnerView(this, spFuelType, SafetyDataCenter.fuelTypeCN);

		tvEnrollDate.setOnClickListener(chooseDateClick);
		initViewData();
	}
	
	/** 为界面填充数据 */
	public abstract void initViewData();
	

	/**
	 * 设置当该字段有数据时用TextView显示，且添加浮动框
	 * 
	 * @param id
	 *            需要显示的TextView的id
	 * @param v
	 *            需要隐藏的控件
	 * @param data
	 *            要为TextView赋的值
	 */
	public void setHaveDataItemShow(int id, View v, String data) {
		if (!StringUtil.isNullOrEmpty(data)) {
			mMainView.findViewById(id).setVisibility(View.VISIBLE);
			((TextView) mMainView.findViewById(id)).setText(data);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(id));
			v.setVisibility(View.GONE);
		}
	}

	/** 初始化未上牌界面四个“示例” */
	public void exampleClick() {
		View v1 = mMainView.findViewById(R.id.example1);
		View v2 = mMainView.findViewById(R.id.example2);
		View v3 = mMainView.findViewById(R.id.example3);
		View v4 = mMainView.findViewById(R.id.example4);
		v1.setVisibility(View.VISIBLE);
		v2.setVisibility(View.VISIBLE);
		v3.setVisibility(View.VISIBLE);
		v4.setVisibility(View.VISIBLE);
		v1.setOnClickListener(exampleClickListener);
		v2.setOnClickListener(exampleClickListener);
		v3.setOnClickListener(exampleClickListener);
		v4.setOnClickListener(exampleClickListener);
	}
	
	/** 保存用户输入数据 */
	public void saveUserInput() {
		Map<String, Object> map = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
		map.put(Safety.NEWCARFLAG, "0");		
		if (etBrandName.getVisibility() == View.VISIBLE) {
			if (!StringUtil.isNullOrEmpty(etBrandName.getText().toString().trim())) {
				map.put(Safety.BRANDNAME, etBrandName.getText().toString().trim().toUpperCase());
			}
		} else {
			map.put(Safety.BRANDNAME, ((TextView) mMainView.findViewById(R.id.tv_brandName)).getText().toString().trim().toUpperCase());
		}
		
		if (etEngineNo.getVisibility() == View.VISIBLE) {
			map.put(Safety.ENGINENO, etEngineNo.getText().toString().trim().toUpperCase());
		} else {
			map.put(Safety.ENGINENO, ((TextView) mMainView.findViewById(R.id.tv_engineNo)).getText().toString().trim().toUpperCase());
		}
		
		if (etFrameNo.getVisibility() == View.VISIBLE) {
			map.put(Safety.FRAMENO, etFrameNo.getText().toString().trim().toUpperCase());
		} else {
			map.put(Safety.FRAMENO, ((TextView) mMainView.findViewById(R.id.tv_frameNo)).getText().toString().trim().toUpperCase());
		}
		
		if (llEnrollDate.getVisibility() == View.VISIBLE) {
			if (tvEnrollDate.getVisibility() == View.VISIBLE) {
				map.put(Safety.ENROLLDATE, tvEnrollDate.getText().toString().trim());
			} else {
				map.put(Safety.ENROLLDATE, ((TextView) mMainView.findViewById(R.id.tv_enrollDate_show)).getText().toString().trim());
			}
		}
		
		if (llFuelType.getVisibility() == View.VISIBLE) {
			if (spFuelType.getVisibility() == View.VISIBLE) {
				map.put(Safety.FUELTYPE, SafetyDataCenter.fuelTypeCode.get(SafetyDataCenter.fuelTypeCN.indexOf(spFuelType.getSelectedItem().toString().trim())));
			} else {
				map.put(Safety.FUELTYPE, SafetyDataCenter.fuelTypeCode.get(SafetyDataCenter.fuelTypeCN.indexOf(((TextView) mMainView.findViewById(R.id.tv_fuelType)).getText().toString().trim())));
			}
		}
	}
	
	/** 准备暂存单保存/更新接口在此页面需要的参数 */
	public void putSaveParams() {
		Map<String, Object> map = SafetyDataCenter.getInstance().getMapSaveParams();
		String isEdit = "";
		if (!StringUtil.isNullOrEmpty(map.get(Safety.BRANDNAME))) {
			map.remove(Safety.BRANDNAME);
		}
		if (!StringUtil.isNullOrEmpty(map.get(Safety.ENGINENO))) {
			map.remove(Safety.ENGINENO);
		}
		if (!StringUtil.isNullOrEmpty(map.get(Safety.FRAMENO))) {
			map.remove(Safety.FRAMENO);
		}
		// 品牌型号
		if (etBrandName.getVisibility() == View.VISIBLE) {
			if (!StringUtil.isNullOrEmpty(etBrandName.getText().toString().trim())) {
				map.put(Safety.BRANDNAME, etBrandName.getText().toString().trim().toUpperCase());
			}
			isEdit += "1";
		} else {
			map.put(Safety.BRANDNAME, ((TextView) mMainView.findViewById(R.id.tv_brandName)).getText().toString().trim().toUpperCase());
			isEdit += "0";
		}
		// 发动机号
		if (etEngineNo.getVisibility() == View.VISIBLE) {
			map.put(Safety.ENGINENO, etEngineNo.getText().toString().trim().toUpperCase());
			isEdit += "1";
		} else {
			map.put(Safety.ENGINENO, ((TextView) mMainView.findViewById(R.id.tv_engineNo)).getText().toString().trim().toUpperCase());
			isEdit += "0";
		}
		// 车辆识别代码
		if (etFrameNo.getVisibility() == View.VISIBLE) {
			map.put(Safety.FRAMENO, etFrameNo.getText().toString().trim().toUpperCase());
			isEdit += "1";
		} else {
			map.put(Safety.FRAMENO, ((TextView) mMainView.findViewById(R.id.tv_frameNo)).getText().toString().trim().toUpperCase());
			isEdit += "0";
		}
		// 注册日期
		if (llEnrollDate.getVisibility() == View.VISIBLE) {
			if (tvEnrollDate.getVisibility() == View.VISIBLE) {
				map.put(Safety.ENROLLDATE, tvEnrollDate.getText().toString().trim());
				isEdit += "1";
			} else {
				map.put(Safety.ENROLLDATE, ((TextView) mMainView.findViewById(R.id.tv_enrollDate_show)).getText().toString().trim());
				isEdit += "0";
			}
		}
		// 燃油类型
		if (llFuelType.getVisibility() == View.VISIBLE) {
			if (spFuelType.getVisibility() == View.VISIBLE) {
				map.put(Safety.FUELTYPE, SafetyDataCenter.fuelTypeCode.get(SafetyDataCenter.fuelTypeCN.indexOf(spFuelType.getSelectedItem().toString().trim())));
				isEdit += "1";
			} else {
				map.put(Safety.FUELTYPE, SafetyDataCenter.fuelTypeCode.get(SafetyDataCenter.fuelTypeCN.indexOf(((TextView) mMainView.findViewById(R.id.tv_fuelType)).getText().toString().trim())));
				isEdit += "0";
			}
		}
		isEdit += "00000";
		map.put(Safety.ISEDIT, isEdit);
		System.out.println("车辆补充信息页-暂存保单参数列表\n" + map);
	}
	
	/** 设置弹出的图片 */
	private void setShowImage(int resId) {
		if (mView == null) {
			mView = new ImageView(CarMoreInfoInputBaseActivity.this);
			mView.setImageResource(resId);
			addContentView(mView, new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		} else {
			mView.setImageResource(resId);
		}
	}
	
	/** 点击“示例”弹出图片方法 */
	private void showView(final View v) {
		hineLeftSideMenu();
		findViewById(R.id.btn_show).setVisibility(View.GONE);
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		if (mView == null) {
			mView = new ImageView(this);
			mView.setImageResource(R.drawable.pinpaixinghao);
			addContentView(mView, new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
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
		showLeftSideMenu();
		findViewById(R.id.btn_show).setVisibility(View.VISIBLE);
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
	
	/** 数据项非空校验 */
	private boolean submitNotNullRegexp() {
		if ((llEnrollDate.getVisibility() == View.VISIBLE)
				&& (etBrandName.getVisibility() == View.VISIBLE)
				&& StringUtil.isNullOrEmpty(etBrandName.getText().toString())) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入品牌型号");
			return false;
		}
		if (StringUtil.isNullOrEmpty(etEngineNo.getText().toString()) && (etEngineNo.getVisibility() == View.VISIBLE)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入发动机号");
			return false;
		}
		if (StringUtil.isNullOrEmpty(etFrameNo.getText().toString()) && (etFrameNo.getVisibility() == View.VISIBLE)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入车辆识别代码");
			return false;
		} else if ((etFrameNo.getText().toString().length() < 17) && (etFrameNo.getVisibility() == View.VISIBLE)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("车辆识别代码请输入长度为17位的字符");
			return false;
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 4) {
			setResult(4);
			finish();
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceQueryAutoDetail())) {
				SafetyDataCenter.getInstance().getMapAutoInsuranceQueryAutoDetail().clear();
			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	};
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 提交车型查询按钮点击事件 */
	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!submitNotNullRegexp()) {
				return;
			}

			if ((llEnrollDate.getVisibility() == View.VISIBLE) && (tvEnrollDate.getVisibility() == View.VISIBLE)) {
				if(!regexpDate(tvEnrollDate.getText().toString(), false)) {
					return;
				}
			}
			
			BaseHttpEngine.showProgressDialog();
			saveUserInput();
			if (!SafetyDataCenter.getInstance().isHoldToThere) {
				putSaveParams();
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Safety.CITYCODE, SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.CITYCODE));
			params.put(Safety.NEWCARFLAG, "0");
			params.put(Safety.LICENSENO, SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.LICENSENO));
			
			if (etBrandName.getVisibility() == View.VISIBLE) {
				if (!StringUtil.isNullOrEmpty(etBrandName.getText().toString().trim())) {
					params.put(Safety.BRANDNAME, etBrandName.getText().toString().trim().toUpperCase());
				}
			} else {
				params.put(Safety.BRANDNAME, ((TextView) mMainView.findViewById(R.id.tv_brandName)).getText().toString().trim().toUpperCase());
			}
			
			if (etEngineNo.getVisibility() == View.VISIBLE) {
				params.put(Safety.ENGINENO, etEngineNo.getText().toString().trim().toUpperCase());
			} else {
				params.put(Safety.ENGINENO, ((TextView) mMainView.findViewById(R.id.tv_engineNo)).getText().toString().trim().toUpperCase());
			}
			
			if (etFrameNo.getVisibility() == View.VISIBLE) {
				params.put(Safety.FRAMENO, etFrameNo.getText().toString().trim().toUpperCase());
			} else {
				params.put(Safety.FRAMENO, ((TextView) mMainView.findViewById(R.id.tv_frameNo)).getText().toString().trim().toUpperCase());
			}
			
			params.put(Safety.ZONENO, SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.ZONENO));
			if (llEnrollDate.getVisibility() == View.VISIBLE) {
				if (tvEnrollDate.getVisibility() == View.VISIBLE) {
					params.put(Safety.ENROLLDATE, tvEnrollDate.getText().toString().trim());
				} else {
					params.put(Safety.ENROLLDATE, ((TextView) mMainView.findViewById(R.id.tv_enrollDate_show)).getText().toString().trim());
				}
			}
			httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURANCEQUERYAUTOTYPE, "PsnAutoInsuranceQueryAutoTypeCallBack", params, true);
		}
	};
	
	/** 设置日期，注册日期控件的点击事件 */
	OnClickListener chooseDateClick = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			TextView tv = (TextView) v;
			String time = tv.getText().toString();
			int Year = 0;
			int Month = 0;
			int Day = 0;
			Year = Integer.parseInt(time.substring(0, 4));
			Month = Integer.parseInt(time.substring(5, 7));
			Day = Integer.parseInt(time.substring(8, 10));
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(CarMoreInfoInputBaseActivity.this, new OnDateSetListener() {

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
	
	/** 未上牌界面四个“示例”点击监听 */
	OnClickListener exampleClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.example1:
				setShowImage(R.drawable.pinpaixh_y);
				break;
			case R.id.example2:
				setShowImage(R.drawable.fadongjh_y);
				break;
			case R.id.example3:
				setShowImage(R.drawable.cheliangsbdm_y);
				break;
			case R.id.example4:
				setShowImage(R.drawable.zhucerq_y);
				break;
			}
			showView(v);
		}
	};
	
	/** 暂存保单按钮点击事件 */
	OnClickListener saveClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (!StringUtil.isNull(etFrameNo.getText().toString()) && etFrameNo.getText().toString().length() < 17) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("车辆识别代码请输入长度为17位的字符");
				return;
			}
			putSaveParams();
			showSaveDialog();
		}
	};

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 车型查询回调
	 */
	@SuppressWarnings("unchecked")
	public void PsnAutoInsuranceQueryAutoTypeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultObj)) {
			return;
		}
		Map<String, Object> resultMap =  (Map<String, Object>) this.getHttpTools().getResponseResult(resultObj);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap.get(Safety.VEHICLETYPELIST); 
		SafetyDataCenter.getInstance().setListAutoInsuranceQueryAutoType(resultList);
		
		Intent intent = new Intent(this, CarTypeChooseActivity.class);
		startActivityForResult(intent, 4);
	}
}
