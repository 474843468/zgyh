package com.chinamworld.bocmbci.biz.safety.safetyproduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.biz.safety.carsafety.CarMoreInfoInputActivity;
import com.chinamworld.bocmbci.biz.safety.carsafety.CarMoreInfoInputHoldActivity;
import com.chinamworld.bocmbci.biz.safety.carsafety.CarMoreInfoInputTempActivity;
import com.chinamworld.bocmbci.biz.safety.carsafety.CarSafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.carsafety.CarTypeChooseActivity;
import com.chinamworld.bocmbci.biz.safety.fragment.CarSafetyFragment;
import com.chinamworld.bocmbci.biz.safety.fragment.SafetyProductListFragment;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance.LifeInsuranceMustKnowActivity;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.riskAssessment.RiskAssessmentActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 保险主界面
 * 
 * @author Zhi
 */
public class SafetyProductListActivity extends CarSafetyBaseActivity {

	// TODO
	// --------------------------------------------成员变量-------------------------------------------------//

	private static final String TAG = "SafetyProductListActivity";
	/** 左侧菜单选中项 */
	// private final int INIT_LEFTEVALUE = 0;
	/** 主显示布局 */
	private View mMainView;
	/** 登录状态 */
	private boolean isLogin;
	/** 保险公司文本显示框，当返回的保险公司只有一个时用此文本控件显示 */
	private TextView mTextView;
	/** 保险公司下拉显示框，当返回的保险公司有多个时用此控件显示，当选择保险类型为寿险时显示寿险承保公司 */
	private Spinner mSpinner;
	/** 承保子公司tv，当请求回一个承保子公司时使用该控件显示 */
	private TextView tvSubCompany;
	/** 承保子公司sp，当请求回多个承保子公司时使用该控件显示 */
	private Spinner spSubCompany;
	/** 保险产品类型下拉框 */
	private Spinner spSafetyType;
	/** 保险公司列表 */
	private List<Map<String, Object>> companyList;
	/** 家财险产品列表 */
	private List<Map<String, Object>> familyList = new ArrayList<Map<String, Object>>();
	/** 意外险产品列表 */
	private List<Map<String, Object>> accidentList = new ArrayList<Map<String, Object>>();
	/** 寿险产品列表 */
	private List<Map<String, Object>> lifeInsuranceList = new ArrayList<Map<String, Object>>();
	/** 请求家财险保险产品时的起始下标 */
	private int indexForFamily = 0;
	/** 请求意外险保险产品时的起始下标 */
	private int indexForAccident = 0;
	/** 请求寿险保险产品时的起始下标 */
	private int indexForLife = 0;
	/** 保险公司名称 */
	private String insuName;
	/** 保险公司代码 */
	private String insurId;
	/** 承保公司代码，寿险使用 */
	private String subInsuId;
	/** 承保公司名称，寿险使用 */
	private String subInsuName;
	/** 承保公司列表 */
	private List<Map<String, Object>> subCompanyList;
	/** 保险类型标识 0-车险 1-家财险 2-意外险 3-寿险，该类型值与数据中心中保险类型列表内险别所处下标一一对应 */
	private int safetyType;
	/** 用户点击的列表项下标 */
	private int selectIndex;
	/** 家财险意外险列表片段 */
	private SafetyProductListFragment productListFragment;
	/** 家财险列表总数 */
	private String familyRecodNum = "";
	/** 意外险列表总数 */
	private String accidentRecodNum = "";
	/** 寿险列表总数 */
	private String lifeInsuranceNum = "";

	// 车险内容布局元素 start//
	/** 新单/续保标识 true-续保 false-新单 */
	private String continueFlag;
	/** 车险片段 */
	private CarSafetyFragment carSafetyFragment;

	// 车险内容布局元素 end//

	// TODO
	// --------------------------------------------成员方法-------------------------------------------------//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setLeftSelectedPosition(INIT_LEFTEVALUE);
		findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
		SafetyDataCenter.getInstance().isSaveToThere = isSaveToThere();
		if (SafetyDataCenter.getInstance().isSaveToThere) {
			SafetyDataCenter.getInstance().isHoldToThere = false;
		}
		mMainView = LayoutInflater.from(this).inflate(R.layout.safety_product_list, null);
		isLogin = BaseDroidApp.getInstanse().isLogin();
		// 如果用户已登录
		if (isLogin) {
			initLoginTrueView();
		} else {
			initLoginFalseView();
		}
		initViews();
		addView(mMainView);
	}

	/** 初始化本页面公共布局元素 */
	private void initViews() {
		mTextView = (TextView) mMainView.findViewById(R.id.tv_company);
		mSpinner = (Spinner) mMainView.findViewById(R.id.sp_company);
		tvSubCompany = (TextView) mMainView.findViewById(R.id.tv_subCompany);
		spSubCompany = (Spinner) mMainView.findViewById(R.id.sp_subCompany);
		spSafetyType = (Spinner) mMainView.findViewById(R.id.sp_safetytype);
		// 初始化产品类型下拉框
		SafetyUtils.initSpinnerView(this, spSafetyType, SafetyDataCenter.safetyType);

		spSafetyType.setOnItemSelectedListener(chooseSafetyTypeListener);
		if (isLogin) {
			spSafetyType.setSelection(0);
		} else {
			spSafetyType.setSelection(2);
		}

		mSpinner.setOnItemSelectedListener(itemSelectListener);
		spSubCompany.setOnItemSelectedListener(subCompanySelectListener);
	}

	/** 初始化未登录界面 */
	private void initLoginFalseView() {
		showTitlebarLoginButton();
	}

	/** 初始化已登录界面 */
	private void initLoginTrueView() {
		setRightTopGone();
		safetyType = 0;
		if (StringUtil.isNullOrEmpty(getIntent().getStringExtra(Safety.CONTINUEFLAG))) {
			continueFlag = "false";
			SafetyDataCenter.getInstance().isHoldToThere = false;
		} else {
			continueFlag = getIntent().getStringExtra(Safety.CONTINUEFLAG);
			if (continueFlag.equals("true")) {
				SafetyDataCenter.getInstance().isHoldToThere = true;
				SafetyDataCenter.getInstance().isSaveToThere = false;
			} else {
				SafetyDataCenter.getInstance().isHoldToThere = false;
			}
		}
	}

	/** 初始化家财险、意外险或寿险的产品列表布局 */
	private void initProductListView() {
		safetyCompanyView();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		if (productListFragment == null) {
			productListFragment = new SafetyProductListFragment();
		}
		transaction.replace(R.id.ll_productList, productListFragment);
		transaction.commit();

		setTitle(getResources().getString(R.string.safety_productQuery));
	}

	/** 初始化车险信息布局 */
	private void initCarSafetyView() {
		setTitle(getResources().getString(R.string.safety_msgfill_title));
		carSafetyCompanyView();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.ll_productList, carSafetyFragment);
		ft.commit();
		SafetyDataCenter.getInstance().isSaved = false;
		Map<String, Object> params = new HashMap<String, Object>();
		SafetyDataCenter.getInstance().setMapSaveParams(params);
	}

	/** 车险控制保险公司视图 */
	private void carSafetyCompanyView() {
		mMainView.findViewById(R.id.layout_step).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.layout_text).setVisibility(View.VISIBLE);
		mSpinner.setVisibility(View.GONE);
		mTextView.setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.layout_sp).setVisibility(View.GONE);
	}

	/** 车险之外其他险别保险公司视图 */
	private void safetyCompanyView() {
		mMainView.findViewById(R.id.layout_step).setVisibility(View.GONE);
		mMainView.findViewById(R.id.ll_safety_listview).setVisibility(View.VISIBLE);
		mMainView.findViewById(R.id.layout_sp).setVisibility(View.GONE);
	}

	/**
	 * 承保公司控件显示
	 * 
	 * @param companyList
	 *            承保公司列表
	 */
	private void insurCompanyInitView(List<Map<String, Object>> companyList) {
		mMainView.findViewById(R.id.layout_text).setVisibility(View.VISIBLE);

		if (companyList.size() == 1) {
			mTextView.setVisibility(View.VISIBLE);
			mSpinner.setVisibility(View.GONE);
			insurId = (String) companyList.get(0).get(Safety.SAFETY_HOLD_INSU_ID);
			insuName = (String) companyList.get(0).get(Safety.SAFETY_HOLD_INSU_NAME);
			mTextView.setText((String) companyList.get(0).get(Safety.INSURANCE_COMANY));
			if (safetyType == 3) {
				mMainView.findViewById(R.id.layout_sp).setVisibility(View.VISIBLE);
				requestSubCompanyListQuery(insurId);
			} else if (safetyType == 1 || safetyType == 2) {
				mMainView.findViewById(R.id.layout_sp).setVisibility(View.GONE);
				// 走请求保险产品流程，当有一条保险公司返回时无法触发保险公司下拉框事件，要在这里请求产品列表
				if (isLogin) {
					requestCommConversationId();
				} else {
					httpTools.requestHttpOutlay(Safety.PSNBMPSCREATCONVERSATION, "requestPSNBmpsCreatConversationCallBack", null, false);
				}
			}
		} else {
			mTextView.setVisibility(View.GONE);
			mSpinner.setVisibility(View.VISIBLE);
			SafetyUtils.initSpinnerView(this, mSpinner, PublicTools.getSpinnerData(companyList, Safety.INSURANCE_COMANY));
		}
	}

	/**
	 * 承保子公司控件显示
	 * 
	 * @param subCompanyList
	 *            承保子公司列表
	 */
	private void insurSubCompanyInitView(List<Map<String, Object>> subCompanyList) {
		mMainView.findViewById(R.id.layout_sp).setVisibility(View.VISIBLE);

		if (subCompanyList.size() == 1) {
			tvSubCompany.setVisibility(View.VISIBLE);
			spSubCompany.setVisibility(View.GONE);

			subInsuId = (String) subCompanyList.get(0).get(Safety.SUBINSUID);
			subInsuName = (String) subCompanyList.get(0).get(Safety.SUBINSUNAME);
			tvSubCompany.setText(subInsuName);

			if (isLogin) {
				requestInsuranceList(true);
			} else {
				// 未登录，跳转登录页面
//				startActivityForResult(new Intent(SafetyProductListActivity.this, LoginActivity.class), ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE);
				BaseActivity.getLoginUtils(SafetyProductListActivity.this).exe(new LoginTask.LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {
						if(isLogin){
							isLogin = true;
							initLoginTrueView();
							initViews();
						}
					}
				});
			}
		} else {
			tvSubCompany.setVisibility(View.GONE);
			spSubCompany.setVisibility(View.VISIBLE);

			SafetyUtils.initSpinnerView(this, spSubCompany, PublicTools.getSpinnerData(subCompanyList, Safety.SUBINSUNAME));
		}
	}

	/** 请求保险子公司 */
	private void requestSubCompanyListQuery(String insurId) {
		if (safetyType == 1 || safetyType == 2) {

		} else if (safetyType == 3) {
			requestPsnLifeInsuranceSubCompanyListQuery(insurId);
		}
	}

	/** 设置三个导航栏中选中第一个的效果 */
	private void setTitleSelectFirst() {
		mMainView.findViewById(R.id.layout_step).setVisibility(View.VISIBLE);
		((TextView) mMainView.findViewById(R.id.index1)).setTextColor(getResources().getColor(R.color.red));
		((TextView) mMainView.findViewById(R.id.text1)).setTextColor(getResources().getColor(R.color.red));
		mMainView.findViewById(R.id.layout_step1).setBackgroundResource(R.drawable.safety_step1);
		((TextView) mMainView.findViewById(R.id.index2)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.text2)).setTextColor(getResources().getColor(R.color.gray));
		mMainView.findViewById(R.id.layout_step2).setBackgroundResource(R.drawable.safety_step3);
		((TextView) mMainView.findViewById(R.id.index3)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.text3)).setTextColor(getResources().getColor(R.color.gray));
		mMainView.findViewById(R.id.layout_step3).setBackgroundResource(R.drawable.safety_step4);
	}

	/**
	 * 请求保险产品列表
	 * 
	 * @param refresh
	 *            刷新标识
	 */
	public void requestInsuranceList(boolean refresh) {
		BaseHttpEngine.showProgressDialog();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Safety.INSURANCE_ID, insurId);
		params.put(Safety.SUBINSUID, subInsuId);
		params.put(Safety.PAGESIZE, SafetyConstant.PAGESIZE);
		params.put(Safety.REFRESH, String.valueOf(refresh));
		if (isLogin) {
			if (safetyType == 1) {
				// 请求登录后家财险列表
				params.put(Safety.CURRENT_INDEX, String.valueOf(indexForFamily));
				httpTools.requestHttp(Safety.METHOD_PSNINSURPRODQRYFORHOME, "requestPsnInsurProdQryCallBack", params, true);
			} else if (safetyType == 2) {
				// 请求登录后意外险列表
				params.put(Safety.CURRENT_INDEX, String.valueOf(indexForAccident));
				httpTools.requestHttp(Safety.METHOD_PSNINSURPRODQRYFORACCDNT, "requestPsnInsurProdQryCallBack", params, true);
			} else if (safetyType == 3) {
				// 请求登录后寿险列表
				params.put(Safety.CURRENT_INDEX, indexForLife);
				httpTools.requestHttp(Safety.PSNLIFEINSURANCEPRODUCTQUERY, "requestPsnLifeInsuranceProductQueryCallBack", params, true);
			}
		} else {
			if (safetyType == 1) {
				params.put(Safety.PRODUCTTYPE, "4");
				params.put(Safety.CURRENT_INDEX, String.valueOf(indexForFamily));
			} else if (safetyType == 2) {
				params.put(Safety.PRODUCTTYPE, "5");
				params.put(Safety.CURRENT_INDEX, String.valueOf(indexForAccident));
			}
			// 请求登录前保险产品列表
			httpTools.requestHttpOutlay(Safety.PSNINSURANCEPRODUCTQUERYOUTLAY, "requestPsnInsurProdQryCallBack", params, true);
		}
	}

	/** 请求保险详情 */
	public void toProductDetail(int position) {
		selectIndex = position;
		BaseHttpEngine.showProgressDialog();
		if (safetyType != 3) {
			String insuCode;
			String riskCode;
			if (safetyType == 1) {
				insuCode = (String) familyList.get(position).get(Safety.INSUCODE);
				riskCode = (String) familyList.get(position).get(Safety.RISKCODE);
			} else {
				insuCode = (String) accidentList.get(position).get(Safety.INSUCODE);
				riskCode = (String) accidentList.get(position).get(Safety.RISKCODE);
			}
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put(Safety.INSURANCE_ID, insurId);
			params.put(Safety.INSUCODE, insuCode);
			params.put(Safety.RISKCODE, riskCode);
			if (isLogin) {
				httpTools.requestHttp(Safety.METHOD_INSURANCE_PRODUCT_INFO, "requestPsnInsuranceProductDetailsCallBack", params, false);
			} else {
				httpTools.requestHttpOutlay(Safety.PSNINSURANCEPRODUCTDETAILSQUERYOUTLAY, "requestPsnInsuranceProductDetailsCallBack", params, false);
			}
		} else {
			// 寿险没有详情，请求风险评估信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Safety.SAFETY_HOLD_INSU_ID, insurId);
			params.put(Safety.RISKCODE, lifeInsuranceList.get(position).get(Safety.RISKCODE));
			getHttpTools().requestHttp(Safety.PSNINSURANCERISKEVALUATIONQUERY, "requestPsnInsuranceRiskEvaluationQueryCallBack", params);
			getHttpTools().registErrorCode(Safety.PSNINSURANCERISKEVALUATIONQUERY, "IBAS.T1167");
		}
	}

	/** 寿险参数保存 */
	private void lifeSaveParams() {
		Map<String, Object> userInput = new HashMap<String, Object>();
		Map<String, Object> userInputTemp = new HashMap<String, Object>();
		Map<String, Object> selectProduct = lifeInsuranceList.get(selectIndex);
		userInput.put(Safety.INSURANCE_ID, insurId);
		userInput.put(Safety.INSURANCE_COMANY, insuName);
		userInput.put(Safety.SUBINSUID, subInsuId);
		userInput.put(Safety.SUBINSUNAME, subInsuName);
		userInput.put(Safety.RISKNAME, selectProduct.get(Safety.RISKNAME));
		userInput.put(Safety.RISKCODE, selectProduct.get(Safety.RISKCODE));
		userInput.put(Safety.RISKTYPE, selectProduct.get(Safety.RISKTYPE));
		userInput.put(Safety.INSUCODE, selectProduct.get(Safety.INSUCODE));
		userInput.put(Safety.MAINRISKCODE, selectProduct.get(Safety.RISKCODE));
		if (companyList.size() == 1) {
			userInputTemp.put(Safety.POLICYHANDFLAG, companyList.get(0).get(Safety.POLICYHANDFLAG));
			userInputTemp.put(Safety.TELEPHONE, companyList.get(0).get(Safety.TELEPHONE));
			userInputTemp.put(Safety.URL, companyList.get(0).get(Safety.URL));
		} else {
			userInputTemp.put(Safety.POLICYHANDFLAG, companyList.get(mSpinner.getSelectedItemPosition()).get(Safety.POLICYHANDFLAG));
			userInputTemp.put(Safety.TELEPHONE, companyList.get(mSpinner.getSelectedItemPosition()).get(Safety.TELEPHONE));
			userInputTemp.put(Safety.URL, companyList.get(mSpinner.getSelectedItemPosition()).get(Safety.URL));
		}
		userInputTemp.put(SELECTPOSITION, selectIndex);
		SafetyDataCenter.getInstance().setMapUserInput(userInput);
		SafetyDataCenter.getInstance().setMapCarSafetyUserInput(userInputTemp);
	}

	@Override
	protected void onPause() {
		super.onPause();
		spSafetyType.setOnItemSelectedListener(null);
		spSafetyType.setSelection(safetyType, true);
		spSafetyType.setOnItemSelectedListener(chooseSafetyTypeListener);
	}

	/** 判断是否是从暂存单模块跳转过来 */
	private boolean isSaveToThere() {
		String jumpFlag = getIntent().getStringExtra(JUMPFLAG);
		if (!StringUtil.isNull(jumpFlag) && jumpFlag.equals("save")) {
			return true;
		}
		return false;
	}

	/** 设置车险开通地区相关的数据 */
	private void initAreaInfo(List<String> areaCodeList) {
		SafetyDataCenter.getInstance().setListAutoInsuranceQueryAllowArea(areaCodeList);
		if (carSafetyFragment == null) {
			carSafetyFragment = new CarSafetyFragment();
		}
		carSafetyFragment.setActivity(this);
		carSafetyFragment.initAreaInfo(areaCodeList);
	}

	/** 清空三险数据 */
	private void clearData() {
		if (!StringUtil.isNullOrEmpty(lifeInsuranceList))
			lifeInsuranceList.clear();
		if (!StringUtil.isNullOrEmpty(familyList))
			familyList.clear();
		if (!StringUtil.isNullOrEmpty(accidentList))
			accidentList.clear();
		indexForAccident = 0;
		indexForFamily = 0;
		indexForLife = 0;
	}

	@Override
	public boolean doHttpErrorHandler(String method, BiiError biiError) {
		if (biiError.getCode().equals("IBAS.2001000901")) {
			Intent intent;
			if (SafetyDataCenter.getInstance().isHoldToThere) {
				intent = new Intent(this, CarMoreInfoInputHoldActivity.class);
			} else if (SafetyDataCenter.getInstance().isSaveToThere) {
				intent = new Intent(this, CarMoreInfoInputTempActivity.class);
			} else {
				intent = new Intent(this, CarMoreInfoInputActivity.class);
			}
			startActivityForResult(intent, 4);
		} else if (biiError.getCode().equals("IBAS.T1167")) {
			// 寿险没做过风评
			Intent intent = new Intent(this, RiskAssessmentActivity.class);
			intent.putExtra(SafetyConstant.RISKFLAG, "0");
			intent.putExtra(Safety.INSURANCE_ID, insurId);
			intent.putExtra(Safety.RISKCODE, (String) lifeInsuranceList.get(selectIndex).get(Safety.RISKCODE));
			startActivityForResult(intent, 6);
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE) {
			if (resultCode == RESULT_OK) {
				isLogin = true;
				initLoginTrueView();
				initViews();
			}
		}
		if (requestCode == 5) {
			isLogin = data.getBooleanExtra("ISLOGIN", false);
		}

		if (requestCode == 6) {
			if (resultCode == 4) {
				// 风评成功
				lifeSaveParams();
				startActivityForResult(new Intent(this, LifeInsuranceMustKnowActivity.class), 0);
			} else if (resultCode == RESULT_CANCELED) {
				// 风评退出
				// spSafetyType.setSelection(0);
			}
			return;
		}
		
		if (resultCode == SafetyConstant.QUIT_RESULT_CODE) {
			// 寿险投保完成
			finish();
		}

		if (resultCode == 4) {
			finish();
		}
	}

	// TODO
	// --------------------------------------------控件事件-------------------------------------------------//

	/** 选择产品类型事件 */
	private OnItemSelectedListener chooseSafetyTypeListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			if (position == 0) {
				// 车险
				if (isLogin) {
					if (!StringUtil.isNullOrEmpty(companyList)) {
						companyList.clear();
					}
					clearData();
					// 已登录，从家财险或意外险跳到车险界面，清空其他两险的列表
					indexForLife = 0;
					setTitleSelectFirst();
					// 车险暂时只支持中银保险，这里直接调一家保险公司的显示方法
					carSafetyCompanyView();
					mTextView.setText("中银保险有限公司");

					BaseHttpEngine.showProgressDialog();
					safetyType = position;
					requestSystemDateTime();
				} else {
					// 未登录，跳转登录页面
//					startActivityForResult(new Intent(SafetyProductListActivity.this, LoginActivity.class), ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE);
					BaseActivity.getLoginUtils(SafetyProductListActivity.this).exe(new LoginTask.LoginCallback() {

						@Override
						public void loginStatua(boolean isLogin) {
							if(isLogin){
								isLogin = true;
								initLoginTrueView();
								initViews();
							}
						}
					});
				}
			} else if ((position == 1) || (position == 2)) {
				// 家财险或意外险
				safetyType = position;
				if (!StringUtil.isNullOrEmpty(companyList)) {
					companyList.clear();
				}
				clearData();
				if (productListFragment != null) {
					productListFragment.setData("0", new ArrayList<Map<String,Object>>(), safetyType);
				}
				// if (StringUtil.isNullOrEmpty(companyList)) {
				// 如果保险公司列表为空，请求保险公司列表，登录前和登录后查询保险公司使用同一个回调方法
				BaseHttpEngine.showProgressDialog();
				if (isLogin) {
					httpTools.requestHttp(Safety.METHOD_INSURANCE_COMPANY, "requestPsnInsuranceCompanyQueryCallBack", null, false);
				} else {
					httpTools.requestHttpOutlay(Safety.PSNINSURANCECOMPANYQUERYOUTLAY, "requestPsnInsuranceCompanyQueryCallBack", null, false);
				}
			} else if (position == 3) {
				if (isLogin) {
					if (!StringUtil.isNullOrEmpty(companyList)) {
						companyList.clear();
					}
					clearData();
					if (productListFragment != null) {
						productListFragment.setData("0", new ArrayList<Map<String,Object>>(), safetyType);
					}
					safetyType = position;
					BaseHttpEngine.showProgressDialog();
					requestSystemDateTime();
				} else {
					// 未登录，跳转登录页面
//					startActivityForResult(new Intent(SafetyProductListActivity.this, LoginActivity.class), ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE);
					BaseActivity.getLoginUtils(SafetyProductListActivity.this).exe(new LoginTask.LoginCallback() {

						@Override
						public void loginStatua(boolean isLogin) {
							if(isLogin){
								isLogin = true;
								initLoginTrueView();
								initViews();
							}
						}
					});
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	/** 公司选中事件 */
	private OnItemSelectedListener itemSelectListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			clearData();
			if (productListFragment != null) {
				productListFragment.setData("0", new ArrayList<Map<String,Object>>(), safetyType);
			}
			if (safetyType != 0) {
				insurId = (String) companyList.get(position).get(Safety.INSURANCE_ID);
				insuName = (String) companyList.get(position).get(Safety.INSURANCE_COMANY);
				BaseHttpEngine.showProgressDialog();
				if ((safetyType == 1) || (safetyType == 2)) {
					// SafetyUtils.initSpinnerView(SafetyProductListActivity.this,
					// spSubCompany,
					// Arrays.asList(getResources().getStringArray(R.array.subInsuId_CN)));
					if (isLogin) {
						requestCommConversationId();
					} else {
						httpTools.requestHttpOutlay(Safety.PSNBMPSCREATCONVERSATION, "requestPSNBmpsCreatConversationCallBack", null, false);
					}
				} else if (safetyType == 3) {
					tvSubCompany.setText("");
					mMainView.findViewById(R.id.layout_sp).setVisibility(View.GONE);
					requestPsnLifeInsuranceSubCompanyListQuery(insurId);
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	/** 选择承保子公司事件 */
	private OnItemSelectedListener subCompanySelectListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			clearData();
			if (productListFragment != null) {
				productListFragment.setData("0", new ArrayList<Map<String,Object>>(), safetyType);
			}
			subInsuId = (String) subCompanyList.get(position).get(Safety.SUBINSUID);
			subInsuName = (String) subCompanyList.get(position).get(Safety.SUBINSUNAME);
			requestInsuranceList(true);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	// TODO
	// ---------------------------------------网络请求与回调方法--------------------------------------------//

	// -------------------------------------------------公共回调-------------------------------------------------//

	/** 获取系统时间回调，该时间用于为未上牌注册日期（购车发票日期，两个是同一个下拉框）和车辆补充信息的注册日期赋默认值 */
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		dateTime = (String) resultMap.get(Comm.DATETME);
		// 设置完全的系统时间，用于为暂存保单赋值默认的保单名称
		SafetyDataCenter.getInstance().setSysTimeFull(dateTime);
		// 只需要年月日，因此只取年月日
		String[] str = dateTime.split(" ");
		dateTime = str[0];
		SafetyDataCenter.getInstance().setSysTime(dateTime);
		requestCommConversationId();
	}

	/** 已登录会话id返回 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (safetyType == 0) {
			// 如果当前是车险，请求查询车险开通地区
			// 车险开通地区代码列表可能在暂存保单继续投保时或持有保单续保时已请求过
			List<String> areaCodeList = SafetyDataCenter.getInstance().getListAutoInsuranceQueryAllowArea();
			if (!StringUtil.isNullOrEmpty(areaCodeList)) {
				initAreaInfo(areaCodeList);
			} else {
				httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURANCEQUERYALLOWAREA, "requestPsnAutoInsuranceQueryAllowAreaCallBack", null, true);
			}
		} else if (safetyType == 1 || safetyType == 2) {
			requestInsuranceList(true);
		} else if (safetyType == 3) {
			getHttpTools().requestHttp(Safety.PSNLIFEINSURANCECOMPANYLISTQUERY, "requestPsnLifeInsuranceCompanyListQueryCallBack", null);
		}
	}

	/** 未登录会话ID返回 */
	public void requestPSNBmpsCreatConversationCallBack(Object resultObj) {
		this.getHttpTools();
		// 功能外置暂时只有家财险和意外险在使用
		String conversationId = (String) HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNull(conversationId)) {
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOGIN_OUTLAY_PRECONVERSATIONID, conversationId);
		requestInsuranceList(true);
	}

	// --------------------------------------------家财险和意外险部分--------------------------------------------//

	/** 查询保险公司回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnInsuranceCompanyQueryCallBack(Object resultObj) {
		this.getHttpTools();
		companyList = (List<Map<String, Object>>) HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(companyList)) {
			return;
		}
		initProductListView();
		insurCompanyInitView(companyList);
	}

	/** 查询保险产品列表回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnInsurProdQryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		this.getHttpTools();
		Map<String, Object> resultMap = (Map<String, Object>) HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> mList = (List<Map<String, Object>>) resultMap.get(Safety.PRODUCT_LIST);
		if (StringUtil.isNullOrEmpty(mList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.bond_comm_error));
			return;
		}
		if (safetyType == 1) {
			familyRecodNum = (String) resultMap.get(Safety.RECORDNUMBER);
			indexForFamily = indexForFamily + Integer.valueOf(SafetyConstant.PAGESIZE);
			familyList.addAll(mList);
		} else if (safetyType == 2) {
			accidentRecodNum = (String) resultMap.get(Safety.RECORDNUMBER);
			indexForAccident = indexForAccident + Integer.valueOf(SafetyConstant.PAGESIZE);
			accidentList.addAll(mList);
		}
		if (safetyType == 1) {
			productListFragment.setData(familyRecodNum, familyList, safetyType);
		} else if (safetyType == 2) {
			productListFragment.setData(accidentRecodNum, accidentList, safetyType);
		}
		return;
	}

	/** 请求保险详情回调 */
	public void requestPsnInsuranceProductDetailsCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result))
			return;
		BaseHttpEngine.dissMissProgressDialog();
		if (safetyType == 1) {
			result.putAll(familyList.get(selectIndex));
		} else if (safetyType == 2) {
			result.putAll(accidentList.get(selectIndex));
		}
		SafetyDataCenter.getInstance().setProductInfoMap(result);
		startActivityForResult(new Intent(this, SafetyProductInfoActivity.class).putExtra(Safety.INSURANCE_COMANY, insuName).putExtra(SafetyConstant.PRODUCTORSAVE, true).putExtra("ISLOGIN", isLogin), 5);
	}

	// -------------------------------------------------车险部分-------------------------------------------------//

	/** 查询开通车险省份代码回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnAutoInsuranceQueryAllowAreaCallBack(Object resultObj) {
		this.getHttpTools();
		Map<String, Object> map = (Map<String, Object>) HttpTools.getResponseResult(resultObj);
		List<Map<String, String>> list = (List<Map<String, String>>) map.get(Safety.OPENAREALIST);
		/** 开通车险地区城市码列表，接口返回的城市码只有省份城市码，6位，末4位全0 */
		List<String> areaCodeList = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				// 只需要开通车险省份的代码的字段，其他字段暂不处理
				String areaCode = list.get(i).get(Safety.AREACODE);
				if (StringUtil.isNull(areaCode) || areaCode.equals("")) {
					continue;
				}
				areaCodeList.add(areaCode);
			}
		}
		if (StringUtil.isNullOrEmpty(areaCodeList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog("后台数据返回异常");
			return;
		}
		initAreaInfo(areaCodeList);
	}

	/** 暂存单Id */
	@SuppressWarnings("unchecked")
	public void requestPsnAutoInsurGetPolicyIdCallBack(Object resultObj) {
		this.getHttpTools();
		Map<String, Object> resultMap = (Map<String, Object>) HttpTools.getResponseResult(resultObj);
		String policyId = (String) resultMap.get(Safety.POLICYID);
		SafetyDataCenter.getInstance().setPolicyId(policyId);
		httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURANCEGETTRANSID, "requestPsnAutoInsuranceGetTransIdCallBack", null, true);
	}

	/** 获取车险交易ID回调方法 */
	public void requestPsnAutoInsuranceGetTransIdCallBack(Object resultObj) {
		if (SafetyDataCenter.getInstance().isSaveToThere) {
			httpTools.requestHttp(Comm.PSN_GETTOKENID, "requestPSNGetTokenIdCallBackSave", null, true);
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			initCarSafetyView();
		}
	}

	@SuppressWarnings("unchecked")
	public void requestPSNGetTokenIdCallBackSave(Object resultObj) {
		this.getHttpTools();
		String token = (String) HttpTools.getResponseResult(resultObj);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.TOKEN_ID, token);
		Map<String, Object> params = SafetyDataCenter.getInstance().getMapAutoInsurPolicyDetailQry();
		Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		params.put(Safety.INV_TITLE, params.get(Safety.INVTITLE));
		params.put(Safety.POSTCODE, params.get(Safety.POSTCODE_TEMP));
		params.put(ConstantGloble.PUBLIC_TOKEN, token);
		params.put(Safety.TYPEFLAG, "1");
		params.put(Safety.APPL_IDTYPE, SafetyDataCenter.IDENTITYTYPE_credType.get(logInfo.get(Comm.IDENTITYTYPE)));
		params.put(Safety.APPL_IDNO, logInfo.get(Comm.IDENTITYNUMBER));
		httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURPOLICYSAVE, "requestPsnAutoInsurPolicySaveCallBackSave", params, true);
	}

	/** 如果是暂存单继续投保进来，在车险交易id之后要自动调用下更新接口，此方法为回调 */
	public void requestPsnAutoInsurPolicySaveCallBackSave(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		initCarSafetyView();
	}

	/** 车辆补充信息查询返回 */
	public void requestPsnAutoInsuranceQueryAutoDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap))
			return;
		SafetyDataCenter.getInstance().setMapAutoInsuranceQueryAutoDetail(resultMap);
		SafetyDataCenter.getInstance().getMapSaveParams().put(Safety.ISEDIT, "0000000000");

		Intent intent;
		if (SafetyDataCenter.getInstance().isHoldToThere) {
			intent = new Intent(this, CarMoreInfoInputHoldActivity.class);
		} else if (SafetyDataCenter.getInstance().isSaveToThere) {
			intent = new Intent(this, CarMoreInfoInputTempActivity.class);
		} else {
			intent = new Intent(this, CarMoreInfoInputActivity.class);
		}
		startActivityForResult(intent, 4);
	}

	/** 车型查询返回 */
	@SuppressWarnings("unchecked")
	public void requestPsnAutoInsuranceQueryAutoTypeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultObj)) {
			return;
		}
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap.get(Safety.VEHICLETYPELIST);
		SafetyDataCenter.getInstance().setListAutoInsuranceQueryAutoType(resultList);

		Intent intent = new Intent(this, CarTypeChooseActivity.class);
		startActivityForResult(intent, 4);
	}

	// -------------------------------------------------寿险部分-------------------------------------------------//

	/** 寿险承保公司列表查询回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnLifeInsuranceCompanyListQueryCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		companyList = (List<Map<String, Object>>) resultMap.get(Safety.COMPANYLIST);
		initProductListView();
		// 如果多家保险公司，查找中银三星人寿显示在第一位
		for (int i = 0; i < companyList.size(); i++) {
			Map<String, Object> map = companyList.get(i);
			if (map.get(Safety.SAFETY_HOLD_INSU_ID).equals("0024")) {
				companyList.remove(i);
				companyList.add(0, map);
				break;
			}
		}
		// 如果多家保险公司，直接下拉框显示
		insurCompanyInitView(companyList);
	}

	/** 寿险承保子公司列表查询 */
	private void requestPsnLifeInsuranceSubCompanyListQuery(String insurId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Safety.SAFETY_HOLD_INSU_ID, insurId);
		getHttpTools().requestHttp(Safety.PSNLIFEINSURANCESUBCOMPANYLISTQUERY, "requestPsnLifeInsuranceSubCompanyListQueryCallBack", params);
	}

	/** 寿险承保子公司列表查询回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnLifeInsuranceSubCompanyListQueryCallBack(Object resultObj) {
		try {
			Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
			subCompanyList = (List<Map<String, Object>>) resultMap.get(Safety.SUBCOMPANYLIST);
			insurSubCompanyInitView(subCompanyList);
		} catch (Exception e) {
			LogGloble.e(TAG, e.toString());
		}
	}

	/** 寿险产品列表查询回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnLifeInsuranceProductQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> mList = (List<Map<String, Object>>) resultMap.get(Safety.PRODUCTLIST);
		if (StringUtil.isNullOrEmpty(mList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.bond_comm_error));
			return;
		}
		lifeInsuranceNum = (String) resultMap.get(Safety.RECORDNUMBER);
		indexForLife = indexForLife + Integer.valueOf(SafetyConstant.PAGESIZE);
		lifeInsuranceList.addAll(mList);
		SafetyDataCenter.getInstance().setListLifeInsuranceProductQuery(lifeInsuranceList);
		productListFragment.setData(lifeInsuranceNum, lifeInsuranceList, safetyType);
	}

	/** 风险测评信息查询回调 */
	public void requestPsnInsuranceRiskEvaluationQueryCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		SafetyDataCenter.getInstance().setMapInsuranceRiskEvaluationQuery(resultMap);

		Intent intent = new Intent();
		try {
			if ("0".equals(resultMap.get(Safety.MACHFLAG))) {
				// 类型不符合
				intent.setClass(this, RiskAssessmentActivity.class);
				intent.putExtra(SafetyConstant.RISKFLAG, "2");
				intent.putExtra(Safety.INSURANCE_ID, insurId);
				intent.putExtra(Safety.RISKCODE, (String) lifeInsuranceList.get(selectIndex).get(Safety.RISKCODE));
				BaseHttpEngine.dissMissProgressDialog();
				startActivityForResult(intent, 6);
				return;
			}

			if (!QueryDateUtils.compareDate(dateTime, (String) resultMap.get(Safety.VALIDDATE))) {
				// 风评到期
				intent.setClass(this, RiskAssessmentActivity.class);
				intent.putExtra(SafetyConstant.RISKFLAG, "1");
				intent.putExtra(Safety.INSURANCE_ID, insurId);
				intent.putExtra(Safety.RISKCODE, (String) lifeInsuranceList.get(selectIndex).get(Safety.RISKCODE));
				BaseHttpEngine.dissMissProgressDialog();
				startActivityForResult(intent, 6);
				return;
			}

			lifeSaveParams();

			intent.setClass(this, LifeInsuranceMustKnowActivity.class);
			BaseHttpEngine.dissMissProgressDialog();
			startActivityForResult(intent, 0);
		} catch (Exception e) {
			LogGloble.e(TAG, e.toString());
		}
	}
}