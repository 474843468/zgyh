package com.chinamworld.bocmbci.biz.safety.carsafety;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 汽车商业险套餐界面
 * 
 * @author Zhi
 */
public class CarBIZActivity extends CarSafetyBaseActivity {
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 主显示视图 */
	private View mMainView;
	/** 投保方案下拉框 */
	private Spinner spPackageType;
	/** 套餐信息列表 */
	private List<Map<String, Object>> packageList;
	/** 报价按钮 */
	private Button btnOffer;
	/** 下一步按钮 */
	private Button btnNext;
	/** 下一步按钮*/
	private Button btnNextBig;
	/** 险别列表 */
	private LinearLayout llBizList;
	/** 可销售的险别列表 */
	private List<Map<String, Object>> avaInsurList;
	/** 存放可销售保险列表项的数组 */
	private List<View> viewList = new ArrayList<View>();
	/** 套餐名称列表 */
	private List<String> packageNameList;
	/** 保费计算方式 0-实时报价 1-统一报价 */
	private String calFlag = (String) SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCommercial().get(Safety.CALFLAG);
	/** 标识险别在该套餐内是否存在 0-套餐内不含该险别 1-套餐内包含该险别 */
	private static String ISCHECK = "isCheck";
	/** 标识套餐内的险别是否不计免赔 */
	private static String _ISMP = "_isMp";
	/** 指定套餐内险别的保额信息 */
	private static String _AMOUNTINFO = "_amountInfo";
	/** 保费试算接口返回的不计免赔合计条目 */
	private static Map<String, Object> bjMap;
	/**
	 * 标识是否进行过报价操作<br>
	 * 当进行了报价操作时该标识改为true，报价后如果用户又对商业险列表内做了修改，则先清空报价接口返回数据<br>
	 * 再判断“若此标识为true且报价接口返回数据为空”，弹出提示需要再次进行报价操作，然后将此标识置false以避免多次提示
	 */
	private boolean isOffer = false;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.safety_msgfill_title);
		mMainView = LayoutInflater.from(this).inflate(R.layout.safety_carsafety_carbiz, null);
		findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
		initData();
		initView();
		addView(mMainView);
	}
	
	@SuppressWarnings("unchecked")
	private void initData() {
		packageList = (List<Map<String, Object>>) SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCommercial().get(Safety.PACKAGELIST);
		avaInsurList = (List<Map<String, Object>>) SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCommercial().get(Safety.AVAINSURLIST);
	}
	
	private void initView() {
		setStep2();
		mLeftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation()))
					SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation().clear();
				finish();
			}
		});
		
		((TextView) mMainView.findViewById(R.id.tv_InsBeginDate)).setText((String) SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCompulsory().get(Safety.BIZINSBEGINDATE));
		((TextView) mMainView.findViewById(R.id.tv_InsEndDate)).setText((String) SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCompulsory().get(Safety.BIZINSENDDATE));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_InsBeginDate));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_InsEndDate));
		
		spPackageType = (Spinner) mMainView.findViewById(R.id.sp_bizType);
		llBizList = (LinearLayout) mMainView.findViewById(R.id.ll_bizList);
		btnOffer = (Button) mMainView.findViewById(R.id.btnOffer);
		btnNext = (Button) mMainView.findViewById(R.id.btnNext);
		packageNameList = getPackageNameList();
		SafetyUtils.initSpinnerView(this, spPackageType, packageNameList);
		
		if (calFlag.equals("0")) {
			mMainView.findViewById(R.id.ll_offer).setVisibility(View.GONE);
		} else {
			btnOffer.setOnClickListener(listener);
		}

		btnNext = (Button) mMainView.findViewById(R.id.btnNext);
		btnNextBig = (Button) mMainView.findViewById(R.id.btnNext_big);
		btnNext.setOnClickListener(listener);
		btnNextBig.setOnClickListener(listener);
		
		if (!SafetyDataCenter.getInstance().isHoldToThere) {
			mMainView.findViewById(R.id.btnSave).setOnClickListener(saveClickListener);
		} else {
			mMainView.findViewById(R.id.btnSave).setVisibility(View.GONE);
			btnNext.setVisibility(View.GONE);
			btnNextBig.setVisibility(View.VISIBLE);
		}
		spPackageType.setOnItemSelectedListener(onItemSelectedListener);
		
		initIsCheck();
		if (!StringUtil.isNullOrEmpty(packageList)) {
			putIsCheck(0);
		}
		viewList.clear();
		
		BaseHttpEngine.dissMissProgressDialog();
	}
	
	/** 工具方法，用来遍历packageList取出套餐名称列表 */
	private List<String> getPackageNameList() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < packageList.size(); i++) {
			list.add((String) packageList.get(i).get(Safety.PACKAGENAME));
		}
		// 自定义套餐自行添加
		list.add("自定义套餐");
		return list;
	}
	
	/**
	 * 初始化可销售险别内的isCheck为未选中，在putIsCheck之前调用
	 */
	private void initIsCheck() {
		for (int i = 0; i < avaInsurList.size(); i++) {
			Map<String, Object> map = avaInsurList.get(i);
			map.put(ISCHECK, "0");
			avaInsurList.set(i, map);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void putIsCheck(int position) {
		// 套餐内险别信息列表中的险别代码临时存储
		List<String> list = new ArrayList<String>();
		List<Map<String, Object>> insPackList = (List<Map<String, Object>>) packageList.get(position).get(Safety.INSURINPACKAGELIST);
		// 循环取出套餐内险别信息中的险别代码
		for (int i = 0; i < insPackList.size(); i++) {
			list.add((String) insPackList.get(i).get(Safety.INSCODE));
		}
		
		// 循环判断套餐内包含哪些可销售险别，如果套餐包含该险，则put一个标识
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < avaInsurList.size(); j++) {
				Map<String, Object> map = avaInsurList.get(j);
				String insCode = (String) map.get(Safety.INSCODE);
				
				if (list.get(i).equals(insCode)) {
					Map<String, Object> insPackMap = insPackList.get(i); 
					map.put(ISCHECK, "1");
					map.put(_ISMP, insPackMap.get(Safety.ISMP));
					map.put(_AMOUNTINFO, insPackMap.get(Safety.AMOUNTINFO));
					avaInsurList.set(j, map);
					continue;
				}
			}
		}
	}
	
	/**
	 * 添加列表项方法
	 * 
	 * @param map
	 *            险种信息map，该方法应在putIsCheck方法后调用
	 */
	@SuppressLint("InflateParams")
	private void addItem(Map<String, Object> map) {
		View view = LayoutInflater.from(this).inflate(R.layout.safety_carsafety_carbiz_item, null);
		// 以下三个TextView标识此条险别属性
		TextView tvInsCode = (TextView) view.findViewById(R.id.tv_insCode);// 险别代码
		TextView tvIsMainIns = (TextView) view.findViewById(R.id.tv_isMainIns);// 主险附加险标识
		TextView tvMainInsCode = (TextView) view.findViewById(R.id.tv_mainInsCode);// 对应主险代码
		// 设置标识内容
		tvInsCode.setText((CharSequence) map.get(Safety.INSCODE));
		tvIsMainIns.setText((CharSequence) map.get(Safety.ISMAININS));
		tvMainInsCode.setText((CharSequence) map.get(Safety.MAININSCODE));
		// 设置险别名称
		((TextView) view.findViewById(R.id.tv_insName)).setText((CharSequence) map.get(Safety.INSNAME));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, ((TextView) view.findViewById(R.id.tv_insName)));
		// 设置是否不计免赔
		if (map.get(Safety.ISMP).equals("1")) {
			((RadioGroup) view.findViewById(R.id.rg_ismp)).setVisibility(View.VISIBLE);
			((TextView) view.findViewById(R.id.tv_isMp)).setVisibility(View.GONE);
			((RadioButton) view.findViewById(R.id.rb_yes)).setSelected(true);
			
			((RadioButton) view.findViewById(R.id.rb_yes)).setOnClickListener(offerListener);
			((RadioButton) view.findViewById(R.id.rb_no)).setOnClickListener(offerListener);
		} else {
//			((RadioGroup) view.findViewById(R.id.rg_ismp)).setVisibility(View.GONE);
//			((TextView) view.findViewById(R.id.tv_isMp)).setVisibility(View.VISIBLE);
//			((TextView) view.findViewById(R.id.tv_isMp)).setText("否");
			
			view.findViewById(R.id.ll_bj).setVisibility(View.GONE);
		}
		// 设置保额/限额
		String[] amountList = null;
		String amountStr = (String) map.get(Safety.AMOUNTINFO);
		if (!StringUtil.isNullOrEmpty(amountStr)) {
			amountList = amountStr.split("\\|");
			if (amountList.length == 1) {
				((TextView) view.findViewById(R.id.tv_amount)).setVisibility(View.VISIBLE);
				((Spinner) view.findViewById(R.id.sp_amount)).setVisibility(View.GONE);
				if (amountList[0].equals("国产")) {
					((TextView) view.findViewById(R.id.tv_amount)).setText("国产");
					((TextView) view.findViewById(R.id.tv_amount)).setTextColor(getResources().getColor(R.color.black));
				} else if (amountList[0].equals("进口")) {
					((TextView) view.findViewById(R.id.tv_amount)).setText("进口");
					((TextView) view.findViewById(R.id.tv_amount)).setTextColor(getResources().getColor(R.color.black));
				} else {
					((TextView) view.findViewById(R.id.tv_amount)).setText(StringUtil.parseStringPattern(amountList[0], 2));
				}
			} else {
				((TextView) view.findViewById(R.id.tv_amount)).setVisibility(View.GONE); 
				((Spinner) view.findViewById(R.id.sp_amount)).setVisibility(View.VISIBLE);
				SafetyUtils.initSpinnerView(this, ((Spinner) view.findViewById(R.id.sp_amount)), Arrays.asList(amountList));
			}
		} else {
			((TextView) view.findViewById(R.id.tv_amount)).setVisibility(View.VISIBLE);
			((Spinner) view.findViewById(R.id.sp_amount)).setVisibility(View.GONE);
			((TextView) view.findViewById(R.id.tv_amount)).setText("-");
		}
		// 设置CheckBox的选中状态
		if (map.get(ISCHECK).equals("1")){
			if (map.get(_ISMP).equals("1")) {
				((RadioButton) view.findViewById(R.id.rb_yes)).setChecked(true);
			} else {
				((RadioButton) view.findViewById(R.id.rb_no)).setChecked(true);
			}
			((CheckBox) view.findViewById(R.id.cb_isCheck)).setChecked(true);
			setItemEnable(view, true);
			
			if (view.findViewById(R.id.sp_amount).getVisibility() == View.VISIBLE) {
				((Spinner) view.findViewById(R.id.sp_amount)).setSelection(Arrays.asList(amountList).indexOf(map.get(_AMOUNTINFO)), true);
				((Spinner) view.findViewById(R.id.sp_amount)).setOnItemSelectedListener(spSelectedListener);
			} 
		} else {
			((CheckBox) view.findViewById(R.id.cb_isCheck)).setChecked(false);
			setItemEnable(view, false);
			
			if (view.findViewById(R.id.sp_amount).getVisibility() == View.VISIBLE) {
				((Spinner) view.findViewById(R.id.sp_amount)).setSelection(0, true);
				((Spinner) view.findViewById(R.id.sp_amount)).setOnItemSelectedListener(spSelectedListener);
			} 
		}
		((CheckBox) view.findViewById(R.id.cb_isCheck)).setOnClickListener(listener);
		// 将这一条目添加到视图数组
		viewList.add(view);
		// 将这一条目添加到显示视图
		llBizList.addView(view);
	}
	
	/** 统一报价方法，遍历viewList取出数据进行请求 */
	private void offer() {
		BaseHttpEngine.showProgressDialog();
		BaseHttpEngine.dissmissCloseOfProgressDialog(); //取消通讯框右上角叉叉
		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> insurForCalcuList = new ArrayList<Map<String,Object>>();
		
		for (int i = 0; i < viewList.size(); i++) {
			CheckBox cb = (CheckBox) viewList.get(i).findViewById(R.id.cb_isCheck);
			if (cb.isChecked()) {
				Map<String, Object> param = new HashMap<String, Object>();
				View v = viewList.get(i);
				
				if ((v.findViewById(R.id.sp_amount)).getVisibility() == View.VISIBLE) {
					param.put(Safety.AMOUNTINFO, ((Spinner) v.findViewById(R.id.sp_amount)).getSelectedItem().toString().trim());
				} else {
					for (int j = 0; j < avaInsurList.size(); j++) {
						Map<String, Object> map = avaInsurList.get(j);
						if (map.get(Safety.INSCODE).equals(((TextView) v.findViewById(R.id.tv_insCode)).getText().toString().trim())) {
							param.put(Safety.AMOUNTINFO, map.get(Safety.AMOUNTINFO));
							break;
						}
					}
				}
				param.put(Safety.INSCODE, ((TextView) v.findViewById(R.id.tv_insCode)).getText().toString().trim());
				
				if ((v.findViewById(R.id.ll_bj)).getVisibility() == View.VISIBLE) {
					if (((RadioButton) v.findViewById(R.id.rb_yes)).isChecked()) {
						param.put(Safety.ISMP, "1");
					} else {
						param.put(Safety.ISMP, "0");
					}
				} else {
					param.put(Safety.ISMP, "0");
				}
				insurForCalcuList.add(param);
			}
		}
		
		params.put(Safety.INSURFORCALCULIST, insurForCalcuList);
		params.put(Safety.ZONENO, SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.ZONENO));
		httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURANCECALCULATION, "requestPsnAutoInsuranceCalculationCallBack", params, true);
	}
	
	/** 保存用户输入数据，本页面保存用户选择的商业险各险别数据 */
	@SuppressWarnings("unchecked")
	private void saveUserInput() {
		Map<String, Object> userInfoMap = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
		List<Map<String, Object>> bizList = new ArrayList<Map<String,Object>>();
		// 遍历商业险列表，取出用户选择的险别信息
		for (int i = 0; i < viewList.size(); i++) {
			CheckBox cb = (CheckBox) viewList.get(i).findViewById(R.id.cb_isCheck);
			if (cb.isChecked()) {
				Map<String, Object> bizInfo = new HashMap<String, Object>();
				View v = viewList.get(i);
				// 该险别的保额选项
				if (((TextView) v.findViewById(R.id.tv_insName)).getText().toString().trim().equals("玻璃单独破碎险")) {
					if ((v.findViewById(R.id.sp_amount)).getVisibility() == View.VISIBLE) {
						if (((Spinner) v.findViewById(R.id.sp_amount)).getSelectedItem().equals("国产")) {
							bizInfo.put(Safety.AMOUNTINFO, "0");
						} else if (((Spinner) v.findViewById(R.id.sp_amount)).getSelectedItem().equals("进口")){
							bizInfo.put(Safety.AMOUNTINFO, "1");
						}
					} else {
						if (((TextView) v.findViewById(R.id.tv_amount)).getText().toString().trim().equals("国产")) {
							bizInfo.put(Safety.AMOUNTINFO, "0");
						} else {
							bizInfo.put(Safety.AMOUNTINFO, "1");
						}
					}
				} else {
					if (((Spinner) v.findViewById(R.id.sp_amount)).getVisibility() == View.VISIBLE) {
						bizInfo.put(Safety.AMOUNTINFO, ((Spinner) v.findViewById(R.id.sp_amount)).getSelectedItem().toString().trim());
					} else {
						for (int j = 0; j < avaInsurList.size(); j++) {
							Map<String, Object> map = avaInsurList.get(j);
							if (map.get(Safety.INSCODE).equals(((TextView) v.findViewById(R.id.tv_insCode)).getText().toString().trim())) {
								bizInfo.put(Safety.AMOUNTINFO, map.get(Safety.AMOUNTINFO));
								break;
							}
						}
					}
				}
				// 该险别的险别代码
				bizInfo.put(Safety.INSCODE, ((TextView) v.findViewById(R.id.tv_insCode)).getText().toString().trim());
				// 该险别是否投保不计免赔
				if ((v.findViewById(R.id.ll_bj)).getVisibility() == View.VISIBLE) {
					if (((RadioButton) v.findViewById(R.id.rb_yes)).isChecked()) {
						bizInfo.put(Safety.ISMP, "1");
					} else {
						bizInfo.put(Safety.ISMP, "0");
					}
				} else {
					bizInfo.put(Safety.ISMP, "0");
					bizInfo.put("ISMPSHOW", "0");
				}
				// 险别大类标识
				bizInfo.put(Safety.ISFORCE, "1");
				// 该险别的险别名称
				bizInfo.put(Safety.INSNAME, ((TextView) v.findViewById(R.id.tv_insName)).getText().toString().trim());
				// 主付险标识
				bizInfo.put(Safety.ISMAININS, ((TextView) v.findViewById(R.id.tv_isMainIns)).getText().toString().trim());
				// 实收保费，从保费试算接口取
				List<Map<String, Object>> insurAfterCalcuList = (List<Map<String, Object>>) SafetyDataCenter.getInstance()
						.getMapAutoInsuranceCalculation().get(Safety.INSURAFTERCALCULIST);
				for (int j = 0; j < insurAfterCalcuList.size(); j++) {
					Map<String, Object> map = insurAfterCalcuList.get(j);
					if (((TextView) v.findViewById(R.id.tv_insCode)).getText().toString().trim().equals(map.get(Safety.INSCODE))) {
						// 如果险别代码相同，取出实收保费
						bizInfo.put(Safety.REALPREMIUM, map.get(Safety.REALPREMIUM));
					}
				}
				// 把这条险别信息添加到险别信息列表中
				bizList.add(bizInfo);
			}
		}
		// 如果用户选择了商业险，且保费试算中有不计免赔合计条目
		if (!StringUtil.isNullOrEmpty(bizList)) {
			if (!StringUtil.isNullOrEmpty(bjMap)) {
				bjMap.remove(Safety.STANDPREMIUM);
				bjMap.put(Safety.INSNAME, "不计免赔合计");
				bjMap.put(Safety.ISMAININS, "1");
				bjMap.put(Safety.ISFORCE, "1");
				bjMap.put(Safety.ISMP, null);
				bjMap.put(Safety.AMOUNTINFO, null);
				bizList.add(bjMap);
			}
		}
		userInfoMap.put("bizList", bizList);
	}
	
	private void setStep2() {
		mMainView.findViewById(R.id.layout_step1).setBackgroundResource(R.drawable.safety_step2);
		mMainView.findViewById(R.id.layout_step2).setBackgroundResource(R.drawable.safety_step1);
		mMainView.findViewById(R.id.layout_step3).setBackgroundResource(R.drawable.safety_step4);
		((TextView) mMainView.findViewById(R.id.index1)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.index2)).setTextColor(getResources().getColor(R.color.red));
		((TextView) mMainView.findViewById(R.id.index3)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.text1)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.text2)).setTextColor(getResources().getColor(R.color.red));
		((TextView) mMainView.findViewById(R.id.text3)).setTextColor(getResources().getColor(R.color.gray));
	}
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation())) {
				SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation().clear();
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 4) {
			setResult(4);
			finish();
		}
	}
	
	/** 当报价方式为统一报价时，如果已经做过报价，且保费试算接口返回数据为空，提醒用户重新进行保费试算 */
	private void tellOfferAgain() {
		if (!StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation())) {
			SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation().clear();
		}
		if (isOffer && StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation()) && calFlag.equals("1")) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("保险套餐有改变，请重新进行车险保费试算");
			((TextView) mMainView.findViewById(R.id.tv_bizBJ)).setText("-");
			((TextView) mMainView.findViewById(R.id.tv_bizRealPremium)).setText("-");
			isOffer = false;
		}
	}
	
	private void offerAgain() {
		spPackageType.setSelection(packageNameList.indexOf("自定义套餐"));
		if (calFlag.equals("0") && StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation())) {
			offer();
		}
	}
	
	/**
	 * 设置该条险别是否可被编辑，包括单选按钮和保额选项的Spinner
	 * 
	 * @param v
	 *            设置的险别视图
	 * @param isCheck
	 *            是否可编辑
	 */
	private void setItemEnable(View v, boolean isCheck) {
		v.findViewById(R.id.rb_yes).setEnabled(isCheck);
		v.findViewById(R.id.rb_no).setEnabled(isCheck);
		v.findViewById(R.id.sp_amount).setEnabled(isCheck);
		SafetyUtils.setSpinnerBackground((Spinner) v.findViewById(R.id.sp_amount), isCheck);
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 按钮监听 */
	OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnOffer:
				offer();
				break;

			case R.id.btnNext:
				if (StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation())) {
					// 如果保费试算接口返回数据为空，提示
					BaseDroidApp.getInstanse().showInfoMessageDialog("请先进行车险保费试算");
					return;
				} else {
					// 如果保费试算接口返回数据不为空，则跳转
					saveUserInput();
					Intent intent = new Intent(CarBIZActivity.this, CalculationDetilActivity.class);
					startActivityForResult(intent, 4);
					break;
				}
				
			case R.id.btnNext_big:
				btnNext.performClick();
				break;
			case R.id.cb_isCheck:
				View view = (View) v.getParent();
				if (((CheckBox) v).isChecked()) {
					// 如果选中这条险别
					if (((TextView) view.findViewById(R.id.tv_isMainIns)).getText().toString().trim().equals("1")) {
						// 表明这个险别是附加险，要找到对应主险，看主险是否被选中，如果没有选中则提示
						TextView tvMainInsCode = (TextView) view.findViewById(R.id.tv_mainInsCode);
						
						for (int i = 0; i < viewList.size(); i++) {
							if (tvMainInsCode.getText().toString().trim().equals(((TextView) viewList.get(i)
									.findViewById(R.id.tv_insCode)).getText().toString().trim())) {
								CheckBox cb = (CheckBox) viewList.get(i).findViewById(R.id.cb_isCheck);
								if (!cb.isChecked()) {
									// 如果其主险没有被选中，弹窗提示，且将此附加险也置为未选择，return掉不进行下面的报价操作
									BaseDroidApp.getInstanse().showInfoMessageDialog(
											getString(R.string.safety_carsafety_pleaseChooseMainIns)
											+ ((TextView) viewList.get(i).findViewById(R.id.tv_insName)).getText());
									((CheckBox) v).setChecked(false);
									return;
								}
								// 否则跳出循环，继续报价操作
								setItemEnable(view, true);
								break;
							}
						}
					} else {
						// 否则此为主险，直接设置可编辑
						setItemEnable(view, true);
					}
				} else {
					((TextView) view.findViewById(R.id.tv_Premium)).setText("-");
					setItemEnable(view, false);					
					if (((TextView) view.findViewById(R.id.tv_isMainIns)).getText().toString().trim().equals("0")) {
						// 如果要取消选择该险别，且此险别是主险，搜索其所有附加险，把这些附加险变为未选择
						String insCode = ((TextView) view.findViewById(R.id.tv_insCode)).getText().toString().trim();
						
						for (int i = 0; i < viewList.size(); i++) {
							if (insCode.equals(((TextView) viewList.get(i).findViewById(R.id.tv_mainInsCode)).getText().toString().trim())) {
								CheckBox cb = (CheckBox) viewList.get(i).findViewById(R.id.cb_isCheck);
								if (cb.isChecked()) {
									cb.setChecked(false);
									((TextView) viewList.get(i).findViewById(R.id.tv_Premium)).setText("-");
									setItemEnable(viewList.get(i), false);
								}
							}
						}
					}
				}
				tellOfferAgain();
				offerAgain();
				break;
			}
		}
	};
	
	/** 如果calFlag为0实时报价，当列表内不计免赔单选按钮点击时调用报价接口*/
	OnClickListener offerListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			tellOfferAgain();
			offerAgain();
		}
	};
	
	/** 如果calFlag为0实时报价，列表项内每个保额选项改变都要调用报价接口 */
	OnItemSelectedListener spSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			tellOfferAgain();
			offerAgain();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	};
	
	/** 投保方案Spinner监听 */
	OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (arg2 == packageNameList.indexOf("自定义套餐")) {
				if (StringUtil.isNullOrEmpty(packageList)) {
					// 如果是自定义套餐且套餐列表为空，直接将可销售险别列表添加进去
					for (int i = 0; i < avaInsurList.size(); i++) {
						addItem(avaInsurList.get(i));
					}
				}
				return;
			}
			if (!StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation())) {
				SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation().clear();
			}
			llBizList.removeAllViews();
			viewList.clear();
			initIsCheck();
			putIsCheck(arg2);
			((TextView) mMainView.findViewById(R.id.tv_bizBJ)).setText("-");
			((TextView) mMainView.findViewById(R.id.tv_bizRealPremium)).setText("-");
			for (int i = 0; i < avaInsurList.size(); i++) {
				// 如果是交强险将不显示在列表中
//				if (avaInsurList.get(i).get(Safety.ISFORCE).equals("0")) {
//					continue;
//				}
				addItem(avaInsurList.get(i));
			}
			if (calFlag.equals("0")) {
				offer();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	};
	
	/** 暂存保单按钮点击事件 */
	OnClickListener saveClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			showSaveDialog();
		}
	};
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 保费试算返回 */
	@SuppressWarnings("unchecked")
	public void requestPsnAutoInsuranceCalculationCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap =  this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		SafetyDataCenter.getInstance().setMapAutoInsuranceCalculation(resultMap);
		isOffer = true;
		// 无论是实时报价还是统一报价，都遍历返回的列表为视图赋值
		List<Map<String, Object>> insurAfterCalCuList = (List<Map<String, Object>>) resultMap.get(Safety.INSURAFTERCALCULIST);
		if (StringUtil.isNullOrEmpty(insurAfterCalCuList)) {
			((TextView) mMainView.findViewById(R.id.tv_bizBJ)).setText("0.00");
		}
		for (int i = 0; i < insurAfterCalCuList.size(); i++) {
			Map<String, Object> insurAfterCalcuMap = insurAfterCalCuList.get(i);
			if (insurAfterCalcuMap.get(Safety.INSCODE).equals("BJ")) {
				bjMap = insurAfterCalcuMap;
				((TextView) mMainView.findViewById(R.id.tv_bizBJ)).setText(StringUtil.parseStringPattern((String) insurAfterCalcuMap.get(Safety.REALPREMIUM), 2));
				continue;
			}
			for (int j = 0; j < viewList.size(); j++) {
				View v = viewList.get(j);
				if (((CheckBox) v.findViewById(R.id.cb_isCheck)).isChecked()) {
					if (((TextView) v.findViewById(R.id.tv_insCode)).getText().toString().trim().equals(insurAfterCalcuMap.get(Safety.INSCODE))) {
						((TextView) v.findViewById(R.id.tv_Premium)).setText(StringUtil.parseStringPattern((String) insurAfterCalcuMap.get(Safety.REALPREMIUM), 2));
						break;
					}
				}
			}
		}
		((TextView) mMainView.findViewById(R.id.tv_bizRealPremium)).setText(StringUtil.parseStringPattern((String) resultMap.get(Safety.TOTALBIZREALPREMIUM), 2));
	}
}