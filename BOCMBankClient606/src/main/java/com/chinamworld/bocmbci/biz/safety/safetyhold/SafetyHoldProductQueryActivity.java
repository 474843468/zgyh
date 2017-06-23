package com.chinamworld.bocmbci.biz.safety.safetyhold;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.biz.safety.adapter.SafetyHoldQueryListAdapter;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.BaseSwipeListViewListener;
import com.chinamworld.bocmbci.widget.SwipeListView;

/**
 * 持有保单查询列表页
 * 
 * @author fsm
 */
public class SafetyHoldProductQueryActivity extends SafetyBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 左侧菜单选中项 */
	// private final int INIT_LEFTEVALUE = 2;
	/** 主显示视图 */
	private View mMainView;
	/** 是否是中行购买下拉框 */
	private Spinner spInsurCompany;
	/** 保险公司下拉框 */
	private Spinner spCompany;
	/** 保险公司列表，此列表中暂时手动添加数据，后期批次如果增加查询保险公司功能，保险公司放到这个列表 */
	private List<String> listInsurCompany;
	/** 持有保单信息列表控件 */
	private SwipeListView lvHoldPro = null;
	/** 列表适配器 */
	private SafetyHoldQueryListAdapter adapter;
	/** 起始序号 */
	private int currentIndex = 0;
	/** 总笔数 */
	private int totalNumber;
	/** 上一次返回报文中总笔数 */
	int pageSize;
	/** 更多视图 */
	private View footerView;

	/** 保险名称 */
	private TextView tvSortBySafetyName;
	/** 购买日期 */
	private TextView tvSortByBuyDate;
	/** 保费 */
	private TextView tvSortByAmount;

	/** 排序条件 */
	private String sortType;
	/** 排序标识，在点击标题时置反 1-正序 2-倒序 */
	private String sortFlag;
	/** 图标大小 */
	private int iconSize;
	/** 持有保险数据列表 */
	private List<Map<String, Object>> listHoldPro;
	/** 保险公司列表 */
	private List<Map<String, Object>> listHoldCompany;
	/** 保险公司列表 */
	private List<String> listStrCompany = new ArrayList<String>();
	/** 产品详情数据，该数据是持有保险列表中的详情数据，非详情接口返回的数据 */
	private Map<String, Object> mapHoldPro;
	/** 0-中行购买 1-非中行购买 */
	private String qryFlag;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iconSize = getResources().getDimensionPixelSize(R.dimen.dp_for_zero) / 2;
		// cacheFlag = "0";
		mMainView = LayoutInflater.from(this).inflate(R.layout.safety_holdsafety, null);
		addView(mMainView);
		initView();
	}

	private void initView() {
		// setLeftSelectedPosition(INIT_LEFTEVALUE);
		setTitle(getString(R.string.safety_hold_pro_query_title));
		setRightTopGone();
		spInsurCompany = (Spinner) mMainView.findViewById(R.id.sp_insurCompany);
		spCompany = (Spinner) mMainView.findViewById(R.id.sp_company);
		lvHoldPro = (SwipeListView) mMainView.findViewById(R.id.lv_holdPro);
		tvSortBySafetyName = (TextView) mMainView.findViewById(R.id.tv_safetyName);
		tvSortByBuyDate = (TextView) mMainView.findViewById(R.id.tv_buyDate);
		tvSortByAmount = (TextView) mMainView.findViewById(R.id.tv_amount);

		listStrCompany.add(0, "全部");
		SafetyUtils.initSpinnerView(this, spCompany, listStrCompany);

		lvHoldPro.setSwipeListViewListener(swipeListViewListener);
		setContainsSwipeListView(true);
		lvHoldPro.setLastPositionClickable(true);
		lvHoldPro.setAllPositionClickable(true);
		footerView = getFooterView();

		qryFlag = "0";
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}

	/** 创建底部“更多”视图 */
	private View getFooterView() {
		View view = View.inflate(this, R.layout.safety_hold_footer, null);
		return view;
	}

	/** 为ListView添加加载更多 */
	private void addFooterView() {
		if (listHoldPro == null)
			return;
		if (listHoldPro.size() < totalNumber) {// 如果当前页不是最后一页则添加加载更多
			lvHoldPro.setLastPositionClickable(false);
			if (lvHoldPro.getFooterViewsCount() <= 0) {
				lvHoldPro.addFooterView(footerView);
			}
			footerView.setClickable(true);
		} else {
			lvHoldPro.setLastPositionClickable(true);
			if (lvHoldPro.getFooterViewsCount() > 0) {
				lvHoldPro.removeFooterView(footerView);
			}
		}
		((TextView) footerView.findViewById(R.id.finc_listiterm_tv1)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				requestPsnInsuranceListQuery();
			}
		});

	}

	/**
	 * @Description: 填充视图的数据
	 * @param
	 */
	private void setListView(List<Map<String, Object>> remainList) {
		addFooterView();
		if (adapter == null) {
			adapter = new SafetyHoldQueryListAdapter(this, remainList);
			lvHoldPro.setAdapter(adapter);
		} else {
			lvHoldPro.requestLayout();
			adapter.setData(remainList);
		}
	}

	private void removeFooterView() {
		if (lvHoldPro != null) {
			if (lvHoldPro.getFooterViewsCount() > 0) {
				lvHoldPro.removeFooterView(footerView);
			}
		}
	}

	/** 按保险名称排序，方法作用是修改三列标题的显示效果 */
	private void sortBySafetyName() {
		Drawable drawableSelect = null;
		if (sortFlag.equals("1")) {
			drawableSelect = getResources().getDrawable(R.drawable.bocinvt_sort_up);
		} else {
			drawableSelect = getResources().getDrawable(R.drawable.bocinvt_sort_down);
		}
		Drawable drawableUnSelect = getResources().getDrawable(R.drawable.bocinvt_sort_un);
		drawableSelect.setBounds(0, 0, iconSize, iconSize);
		drawableUnSelect.setBounds(0, 0, iconSize, iconSize);
		tvSortBySafetyName.setCompoundDrawables(null, null, drawableSelect, null);
		tvSortByBuyDate.setCompoundDrawables(null, null, drawableUnSelect, null);
		tvSortByAmount.setCompoundDrawables(null, null, drawableUnSelect, null);
	}

	/** 按买入时间排序，方法作用是修改三列标题的显示效果 */
	private void sortByBuyDate() {
		Drawable drawableSelect = null;
		if (sortFlag.equals("1")) {
			drawableSelect = getResources().getDrawable(R.drawable.bocinvt_sort_up);
		} else {
			drawableSelect = getResources().getDrawable(R.drawable.bocinvt_sort_down);
		}
		Drawable drawableUnSelect = getResources().getDrawable(R.drawable.bocinvt_sort_un);
		drawableSelect.setBounds(0, 0, iconSize, iconSize);
		drawableUnSelect.setBounds(0, 0, iconSize, iconSize);
		tvSortBySafetyName.setCompoundDrawables(null, null, drawableUnSelect, null);
		tvSortByBuyDate.setCompoundDrawables(null, null, drawableSelect, null);
		tvSortByAmount.setCompoundDrawables(null, null, drawableUnSelect, null);
	}

	/** 按保费排序，方法作用是修改三列标题的显示效果 */
	private void sortByAmount() {
		Drawable drawableSelect = null;
		if (sortFlag.equals("1")) {
			drawableSelect = getResources().getDrawable(R.drawable.bocinvt_sort_up);
		} else {
			drawableSelect = getResources().getDrawable(R.drawable.bocinvt_sort_down);
		}
		Drawable drawableUnSelect = getResources().getDrawable(R.drawable.bocinvt_sort_un);
		drawableSelect.setBounds(0, 0, iconSize, iconSize);
		drawableUnSelect.setBounds(0, 0, iconSize, iconSize);
		tvSortBySafetyName.setCompoundDrawables(null, null, drawableUnSelect, null);
		tvSortByBuyDate.setCompoundDrawables(null, null, drawableUnSelect, null);
		tvSortByAmount.setCompoundDrawables(null, null, drawableSelect, null);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				// if (spInsurCompany.getSelectedItemPosition() == 0) {
				// if (spCompany.getSelectedItemPosition() == 0) {
				// clearView();
				// } else if (listHoldPro.size() == 1) {
				// clearView();
				// } else {
				// listHoldPro.clear();
				// adapter.setData(listHoldPro);
				// currentIndex = 0;
				// removeFooterView();
				// BaseHttpEngine.showProgressDialog();
				// requestPsnInsuranceListQuery();
				// }
				// }
				clearView();
				// BaseHttpEngine.showProgressDialog();
				// requestCommConversationId();
			}
			break;
		case SafetyConstant.QUIT_RESULT_CODE:
			finish();
			break;
		default:
			break;
		}
	}

	/** 重新刷新页面和数据 */
	private void clearView() {
		SafetyDataCenter.getInstance().clearAllData();
		listHoldCompany.clear();
		spCompany.setOnItemSelectedListener(null);
		listStrCompany.clear();
		listStrCompany.add(0, "全部");
		SafetyUtils.initSpinnerView(this, spCompany, listStrCompany);
		listHoldPro.clear();
		adapter.setData(listHoldPro);
		currentIndex = 0;
		removeFooterView();

		listInsurCompany = new ArrayList<String>();
		listInsurCompany.add("中行购买");
		listInsurCompany.add("非中行购买");

		SafetyUtils.initSpinnerView(this, spInsurCompany, listInsurCompany);
		spInsurCompany.setOnItemSelectedListener(selectInsurCompanyListener);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 列表向右滑动事件 */
	BaseSwipeListViewListener swipeListViewListener = new BaseSwipeListViewListener() {
		@Override
		public void onStartOpen(int position, int action, boolean right) {
			if (action == 0) {
				if (position >= listHoldPro.size()) {
					return;
				} else {
					// 请求产品详情
					mapHoldPro = listHoldPro.get(position);
					if (mapHoldPro != null) {
						// 当这条数据里的详情不为空时，请求保险详情，详情接口需要上送此数据中的字段
						if (mapHoldPro.get(Safety.DETAILFLAG).equals("0")) {
							BaseDroidApp.getInstanse().showInfoMessageDialog("您所选择的保单无法查询详情！");
							return;
						} else {
							requestPsnInsuranceDetailsQuery();
						}
					}
				}
			}
		}

		@Override
		public void onClickFrontView(int position) {
			if (position >= listHoldPro.size()) {
				return;
			} else {
				// 请求产品详情
				mapHoldPro = listHoldPro.get(position);
				if (mapHoldPro != null) {
					// 当这条数据里的详情不为空时，请求保险详情，详情接口需要上送此数据中的字段
					if (mapHoldPro.get(Safety.DETAILFLAG).equals("0")) {
						BaseDroidApp.getInstanse().showInfoMessageDialog("您所选择的保单无法查询详情！");
						return;
					} else {
						requestPsnInsuranceDetailsQuery();
					}
				}
			}
			super.onClickFrontView(position);
		}
	};

	/** 点击列标题的监听事件 */
	OnClickListener sortClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 当点击列标题的时候分页重新计算，点“更多”时才做累加操作
			currentIndex = 0;
			if (!StringUtil.isNullOrEmpty(listHoldPro)) {
				listHoldPro.clear();
			}
			switch (v.getId()) {
			case R.id.tv_safetyName:
				if ("RISKCHNNM".equals(sortType)) {
					changeSortType();
				} else {
					sortFlag = "1";
				}
				sortType = "RISKCHNNM";
				break;
			case R.id.tv_buyDate:
				if ("POLEFFDATE".equals(sortType)) {
					changeSortType();
				} else {
					sortFlag = "1";
				}
				sortType = "POLEFFDATE";
				break;
			case R.id.tv_amount:
				if ("RISKPREM".equals(sortType)) {
					changeSortType();
				} else {
					sortFlag = "1";
				}
				sortType = "RISKPREM";
				break;
			}
			BaseHttpEngine.showProgressDialog();
			requestPsnInsuranceListQuery();
		}
	};

	/** 切换升降序标识 */
	private void changeSortType() {
		if (sortFlag.equals("1")) {
			sortFlag = "2";
		} else {
			sortFlag = "1";
		}
	}

	/** 清空列表 */
	private void cleanListView() {
		currentIndex = 0;
		if (!StringUtil.isNullOrEmpty(listHoldPro)) {
			listHoldPro.clear();
			// 清空显示列表
			adapter.setData(listHoldPro);
		}
		// 去掉更多视图
		lvHoldPro.setLastPositionClickable(true);
		if (lvHoldPro.getFooterViewsCount() > 0) {
			lvHoldPro.removeFooterView(footerView);
		}
	}

	/** 选择是否是中行购买下拉框监听事件 */
	private OnItemSelectedListener selectInsurCompanyListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			cleanListView();
			if (!StringUtil.isNullOrEmpty(listHoldCompany)) {
				listHoldCompany.clear();
			}
			if (!StringUtil.isNullOrEmpty(listStrCompany)) {
				spCompany.setOnItemSelectedListener(null);
				listStrCompany.clear();
				listStrCompany.add(0, "全部");
				SafetyUtils.initSpinnerView(SafetyHoldProductQueryActivity.this, spCompany, listStrCompany);
			}
			if (position == 0) {
				mMainView.findViewById(R.id.ll_company).setVisibility(View.VISIBLE);
				qryFlag = "0";
				BaseHttpEngine.showProgressDialog();
				getHttpTools().requestHttp(Safety.PSNINSURANCEPOLICYCOMPANYLISTQUERY, "requestPsnInsurancePolicyCompanyListQueryCallBack", null);
			} else {
				qryFlag = "1";
				mMainView.findViewById(R.id.ll_company).setVisibility(View.GONE);
				BaseHttpEngine.showProgressDialog();
				requestPsnInsuranceListQuery();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	/** 选择保险公司事件 */
	private OnItemSelectedListener selectCompanyListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			cleanListView();
			BaseHttpEngine.showProgressDialog();
			requestPsnInsuranceListQuery();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 请求保险产品列表 */
	private void requestPsnInsuranceListQuery() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put(Safety.SAFETY_HOLD_CURRENT_INDEX, String.valueOf(currentIndex));
		params.put(Safety.CACHEFLAG, "1");
		params.put(Safety.QRYFLAG, qryFlag);
		params.put(Safety.SORTTYPE, sortType);
		params.put(Safety.SORTFLAG, sortFlag);
		if (spInsurCompany.getSelectedItemPosition() == 0) {
			if (spCompany.getSelectedItemPosition() == 0) {
				params.put(Safety.QRYTYPE, "0");
			} else {
				Map<String, Object> map = listHoldCompany.get(spCompany.getSelectedItemPosition() - 1);
				params.put(Safety.SAFETY_HOLD_INSU_ID, map.get(Safety.SAFETY_HOLD_INSU_ID));
				params.put(Safety.QRYTYPE, "1");
			}
		}
		httpTools.requestHttp(Safety.SAFETYHOLDHISTORYQUERY, "requestPsnInsuranceListQueryCallBack", params, true);
	}

	/** 请求保单详情 */
	private void requestPsnInsuranceDetailsQuery() {
		BaseHttpEngine.showProgressDialog();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Safety.SAFETY_HOLD_TRANS_DATE, mapHoldPro.get(Safety.SAFETY_HOLD_TRANS_DATE));
		params.put(Safety.SAFETY_HOLD_TRANS_ACCNO, mapHoldPro.get(Safety.SAFETY_HOLD_TRANS_ACCNO));
		params.put(Safety.SAFETY_HOLD_POLICY_NO, mapHoldPro.get(Safety.SAFETY_HOLD_POLICY_NO));
		params.put(Safety.SAFETY_HOLD_INSU_ID, mapHoldPro.get(Safety.SAFETY_HOLD_INSU_ID));
		httpTools.requestHttp(Safety.SafetyHisTranDetailQuery, "requestPsnInsuranceDetailsQueryCallBack", params, false);
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 初始按照保单生效日期倒序排序
		sortType = "POLEFFDATE";
		sortFlag = "2";
		listInsurCompany = new ArrayList<String>();
		listInsurCompany.add("中行购买");
		listInsurCompany.add("非中行购买");

		SafetyUtils.initSpinnerView(this, spInsurCompany, listInsurCompany);
		// spInsurCompany.setSelection(0, true);
		spInsurCompany.setOnItemSelectedListener(selectInsurCompanyListener);
		// requestPsnInsuranceListQuery();
	}

	/** 请求持有保险列表回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnInsuranceListQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (!StringUtil.isNullOrEmpty(resultMap)) {
			// 赋值总笔数
			totalNumber = Integer.parseInt((String) resultMap.get(Safety.SAFETY_HOLD_TOTLE_NUMBER));
			// 取出返回的持有保单列表
			List<Map<String, Object>> list = (List<Map<String, Object>>) resultMap.get(Safety.HOLD_PRODUCT_LIST);
			if (!StringUtil.isNullOrEmpty(list)) {
				currentIndex += list.size();
				if (!StringUtil.isNullOrEmpty(listHoldPro)) {
					listHoldPro.addAll(list);
				} else {
					listHoldPro = list;
				}
			}
		}
		if (sortType.equals("RISKCHNNM")) {
			sortBySafetyName();
		} else if (sortType.equals("POLEFFDATE")) {
			sortByBuyDate();
		} else if (sortType.equals("RISKPREM")) {
			sortByAmount();
		}
		tvSortBySafetyName.setOnClickListener(sortClickListener);
		tvSortByBuyDate.setOnClickListener(sortClickListener);
		tvSortByAmount.setOnClickListener(sortClickListener);

		setListView(listHoldPro);
		// cacheFlag = "1";
	}

	/** 客户持有保单保险公司列表查询回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnInsurancePolicyCompanyListQueryCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		listHoldCompany = (List<Map<String, Object>>) resultMap.get(Safety.HOLD_PRODUCT_LIST);
		if (StringUtil.isNullOrEmpty(listHoldCompany)) {
			return;
		}
		listStrCompany.addAll(PublicTools.getSpinnerData(listHoldCompany, Safety.SAFETY_HOLD_INSU_NAME));
		SafetyUtils.initSpinnerView(this, spCompany, listStrCompany);
		spCompany.setOnItemSelectedListener(selectCompanyListener);
	}

	/** 持有保险查询详情回调 */
	public void requestPsnInsuranceDetailsQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		/** 此处针对BII极度不负责任的行为进行高尚的补偿，接口文档没有这些返回字段 */
		/** 但是BII不修改代码，导致不该出现的字段出现，putAll之后会覆盖之前的字段导致无法预知的错误 */
		resultMap.remove(Safety.CONTINUEFLAG);
		resultMap.remove(Safety.SAFETY_HOLD_MAINTAIN_FLAG);
		resultMap.remove(Safety.SAFETY_HOLD_RETURN_FLAG);
		resultMap.remove(Safety.SAFETY_HOLD_CANCEL_FLAG);
		resultMap.remove(Safety.DETAILFLAG);
		this.mapHoldPro.putAll(resultMap);
		SafetyDataCenter.getInstance().setHoldProDetail(this.mapHoldPro);
		if (resultMap.get(Safety.SAFETY_HOLD_RISK_TYPE).equals("8")) {
			// 选择的保险类型为车险，进入车险详情界面
			startActivity(new Intent(SafetyHoldProductQueryActivity.this, CarSafetyHoldDetil.class));
		} else if (resultMap.get(Safety.SAFETY_HOLD_RISK_TYPE).equals("4") || resultMap.get(Safety.SAFETY_HOLD_RISK_TYPE).equals("5")) {
			startActivityForResult(new Intent(this, SafetyHoldProductQueryDetailActivity.class), 1);
		} else {
			startActivityForResult(new Intent(this, LifeInsuDetailActivity.class), 1);
		}
	}
}
