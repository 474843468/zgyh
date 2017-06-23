package com.chinamworld.bocmbci.biz.safety.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.safety.adapter.InsuranceProductAdapter;
import com.chinamworld.bocmbci.biz.safety.adapter.LifeInsuranceProductAdapter;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.SafetyProductListActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.widget.BaseSwipeListViewListener;
import com.chinamworld.bocmbci.widget.SwipeListView;

/**
 * 家财险意外险产品列表视图片段<br>
 * 该片段用于将产品列表和事件从Activity中独立出来，通过getActivity()方法与该片段依附的Activity通讯<br>
 * 所调用Activity的方法均要改为public或protected的<br>
 * Activity通过setData方法更新此片段视图数据
 * 
 * @author Zhi
 */
public class SafetyProductListFragment extends Fragment {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private SafetyProductListActivity activity;
	/** 主显示视图 */
	private View mMainView;
	/** 列表头视图 */
	private View headView;
	/** 列表底部“更多”选项 */
	private View mFooterView;
	/** 保险产品列表控件 */
	private SwipeListView mSwipeListView;
	/** 保险产品列表适配器*/
	private InsuranceProductAdapter mAdapter;
	/** 保险产品列表适配器*/
	private LifeInsuranceProductAdapter mLifeAdapter;
	/** 产品列表 */
	private List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();
	/** 保险类型 */
	private int safetyType = 0;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	public SafetyProductListFragment(SafetyProductListActivity activity) {
//		this.activity = activity;
//	}

	@Override
	public void onAttach(Activity activity) {
		this.activity = (SafetyProductListActivity)activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.safety_product_list_fragment, null);
		headView = mMainView.findViewById(R.id.sort_layout);
		mSwipeListView = (SwipeListView) mMainView.findViewById(R.id.safety_listview);
		mSwipeListView.setSwipeListViewListener(swipeListViewListener);  
		activity.setContainsSwipeListView(true);
		mSwipeListView.setLastPositionClickable(true);
		mSwipeListView.setAllPositionClickable(true);
		mFooterView = View.inflate(activity, R.layout.safety_hold_footer, null);
		return mMainView;
	}
	
	/** 为此片段设置数据 */
	public void setData(String recordNumber, List<Map<String, Object>> productList, int safetyType) {
		if (safetyType == 0) {
			return;
		}
		this.productList = productList;
		if ((this.safetyType == 1 || this.safetyType == 2) && safetyType == 3) {
			mAdapter = null;
			mLifeAdapter = null;
		}
		if ((safetyType == 1 || safetyType == 2) && this.safetyType == 3) {
			mAdapter = null;
			mLifeAdapter = null;
		}
		this.safetyType = safetyType;
		addFooterView(recordNumber);
		if (safetyType != 3) {
			headView.setVisibility(View.VISIBLE);
		} else {
			headView.setVisibility(View.GONE);
		}
		
		if (safetyType == 1 || safetyType == 2) {
			if (mAdapter == null) {
				addFooterView((productList.size() + 1) + "");
				mAdapter = new InsuranceProductAdapter(activity, productList);
				mSwipeListView.setAdapter(mAdapter);
				mSwipeListView.removeFooterView(mFooterView);
				addFooterView(recordNumber);
			} else {
				mSwipeListView.requestLayout();
				mAdapter.setData(productList);
			}
		} else if (safetyType == 3) {
			if (mLifeAdapter == null) {
				addFooterView((productList.size() + 1) + "");
				mLifeAdapter = new LifeInsuranceProductAdapter(activity, productList);
				mSwipeListView.setAdapter(mLifeAdapter);
				mSwipeListView.removeFooterView(mFooterView);
				addFooterView(recordNumber);
			} else {
				mSwipeListView.requestLayout();
				mLifeAdapter.setData(productList);
			}
		}
	}
	
	/**
	 * 添加分页布局
	 * 
	 * @param totalCount
	 *            后台的产品列表总长度
	 */
	private void addFooterView(String totalCount) {
		int productSize = 0;
		productSize = productList.size();
		if (Integer.valueOf(totalCount) > productSize) {
			mSwipeListView.setLastPositionClickable(false);
			if (mSwipeListView.getFooterViewsCount() <= 0) {
				mSwipeListView.addFooterView(mFooterView);
			}
		} else {
			mSwipeListView.setLastPositionClickable(true);
			if (mSwipeListView.getFooterViewsCount() > 0) {
				mSwipeListView.removeFooterView(mFooterView);
			}
		}
		((TextView) mFooterView.findViewById(R.id.finc_listiterm_tv1)).
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				// 请求更多
				activity.requestInsuranceList(false);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private void toNext(int position) {
		if (safetyType == 3) {
			Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
			String identityType = (String) logInfo.get(Comm.IDENTITYTYPE);
			if ("1".equals(identityType) || "2".equals(identityType) || "3".equals(identityType)) {
				if (((String) logInfo.get(Comm.IDENTITYNUMBER)).length() != 18) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("您在我行系统中留存的15位身份证信息不符合办理当前业务要求，请前往我行网点对相关信息进行更新后再试，对此造成的不便敬请谅解。");
					return;
				}
			}
		}
		activity.toProductDetail(position);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 列表项向右滑动事件 */
	BaseSwipeListViewListener swipeListViewListener = new BaseSwipeListViewListener() {
		@Override
		public void onStartOpen(int position, int action, boolean right) {
			if (action == 0) {
				toNext(position);
			}
		}
		
		@Override
		public void onClickFrontView(int position) {
			super.onClickFrontView(position);
			toNext(position);
		}
	};
}
