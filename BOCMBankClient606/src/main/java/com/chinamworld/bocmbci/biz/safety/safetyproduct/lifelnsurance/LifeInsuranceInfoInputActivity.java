package com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.adapter.LifeInsuranceInfoInputPagerAdapter;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance.Fragment.ApplInfoFrag;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance.Fragment.InsuranceInfoFrag;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance.Fragment.OtherInfoFrag;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

public class LifeInsuranceInfoInputActivity extends LifeInsuranceBaseActivity {
	
	/** 侧滑控件 */
	private ViewPager viewPager;
	/** viewPager下标 */
	private int pageIndex;
	/** 产品信息Fragment */
	private InsuranceInfoFrag insurFrag;
	/** 投保人信息Fragment */
	private ApplInfoFrag applFrag;
	/** 被保人信息Fragment */
//	private BenInfoFrag benFrag;
	/** 其他信息Fragment */
	private OtherInfoFrag otherFrag;
	/** 视图列表 */
	private List<Fragment> listFragment;
	/** 下一步按钮 */
	private TextView btnNext;
	/** 上一步按钮 */
	private TextView btnLast;
	/** 暂存保单按钮，一期没有暂存保单，有的时候把暂存单有关代码全部打开完善 */
//	private TextView btnSave;
	/** 是否显示引导页 */
	private boolean isShowguid = true;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.safety_life_product_info_input);
		setTitle(R.string.safety_msgfill_title);
		findViewById(R.id.sliding_body).setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.fill_margin_bottom));
		findView();
		viewSet();
	};
	
	@Override
	protected void findView() {
		viewPager = (ViewPager) mMainView.findViewById(R.id.viewpager);
		btnNext = (TextView) mMainView.findViewById(R.id.fourth_btn_next);
		btnLast = (TextView) mMainView.findViewById(R.id.btnLast);
//		btnSave = (TextView) mMainView.findViewById(R.id.fourth_btn_next);
	}

	@Override
	protected void viewSet() {
		// 滑动时缓存前两页和后两页
		viewPager.setOffscreenPageLimit(2);
		// 导航事件
		mMainView.findViewById(R.id.layout_step1).setOnClickListener(stepOnClick);
		mMainView.findViewById(R.id.layout_step2).setOnClickListener(stepOnClick);
//		mMainView.findViewById(R.id.layout_step3).setOnClickListener(stepOnClick);
		mMainView.findViewById(R.id.layout_step4).setOnClickListener(stepOnClick);
		
		// 滑动视图数据和事件
		insurFrag = new InsuranceInfoFrag();
		applFrag = new ApplInfoFrag();
//		benFrag = new BenInfoFrag();
		otherFrag = new OtherInfoFrag();
		
		listFragment = new ArrayList<Fragment>();
		listFragment.add(insurFrag);
		listFragment.add(applFrag);
//		listFragment.add(benFrag);
		listFragment.add(otherFrag);
		viewPager.setAdapter(new LifeInsuranceInfoInputPagerAdapter(getSupportFragmentManager(), listFragment));
		viewPager.setOnPageChangeListener(pageChangeListener);
		
		btnNext.setOnClickListener(nextListener);
		btnLast.setOnClickListener(lastListener);
//		btnSave.setOnClickListener(saveListener);
	}

	/** viewPager滑动事件 */
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			pageChange(arg0);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	
	/** 下一步事件 */
	private OnClickListener nextListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (pageIndex == 2) {
				if (otherFrag.submit()) {
					Intent intent = new Intent(LifeInsuranceInfoInputActivity.this, LifeInsuranceConfirmActivity.class);
					startActivityForResult(intent, 4);
					return;
				}
				return;
			}
			pageChange(pageIndex + 1);
		}
	};
	
	/** 上一步事件 */
	private OnClickListener lastListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			pageChange(pageIndex - 1);
		}
	};
	
	/** 暂存保单事件 */
//	private OnClickListener saveListener = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			
//		}
//	};
	
	/** 页面改变 */
	private void pageChange(int position) {
		if (pageIndex == position) {
			return;
		}
		if (pageIndex > position) {
			// 上一步的情況
			pageChangeSuccessViewSet(position);
			return;
		} else {
			// 下一步的情况
			if (pageIndex == 0) {
				if (position == 1) {
					// 点击导航栏时，如果此时下标为0，目标下标为1（在产品信息视图点下一步也是这种情况），则校验产品信息
					if (insurFrag.submit()) {
						pageChangeSuccessViewSet(position);
						return;
					}
				} else if (position == 2) {
					// 点击导航栏时，如果此时下标为0，目标下标为2
					if (!insurFrag.submit()) {
						pageChangeSuccessViewSet(0);
						return;
					}
					
					if (!applFrag.submit()) {
						pageChangeSuccessViewSet(1);
						return;
					}

					pageChangeSuccessViewSet(2);
					return;
				}
//				else if (position == 3) {
//					// 点击导航栏时，如果此时下标为0，目标下标为3
//					if (insurFrag.submit() && applFrag.submit() && benFrag.submit()) {
//						pageChangeSuccessViewSet(position);
//						return;
//					}
//				}
			} else if (pageIndex == 1) {
				if (position == 2) {
					// 点击导航栏时，如果此时下标为1，目标下标为2，下一步也是这种情况
					if (applFrag.submit()) {
						pageChangeSuccessViewSet(position);
						return;
					}
				}
//				else if (position == 3) {
//					// 点击导航栏时，如果此时下标为1，目标下标为3
//					if (applFrag.submit() && benFrag.submit()) {
//						pageChangeSuccessViewSet(position);
//						return;
//					}
//				}
			}
//			else if (pageIndex == 2) {
//				// 点击导航栏时，如果此时下标为2，目标下标为3，下一步也是这种情况
//				if (benFrag.submit()) {
//					pageChangeSuccessViewSet(position);
//					return;
//				}
//			}
			// 如果上面都没走，说明有校验没过，把页面再设置回来
			viewPager.setCurrentItem(pageIndex);
		}
	}
	
	/** 页面改变成功 */
	private void pageChangeSuccessViewSet(int position) {
		controlStep(position);
		pageIndex = position;
		if (pageIndex == 0) {
			btnLast.setVisibility(View.GONE);
			btnNext.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_blue));
		} else {
			btnLast.setVisibility(View.VISIBLE);
			btnNext.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_pink));
		}
		
//		if (pageIndex == 2) {
//			otherFrag.viewSet();
//		}
		
		if (viewPager.getCurrentItem() != pageIndex) {
			viewPager.setCurrentItem(pageIndex);
		}
	}
	
	/** 步骤跳转 */
	private OnClickListener stepOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_step1:
				pageChange(0);
				break;
			case R.id.layout_step2:
				pageChange(1);
				break;
//			case R.id.layout_step3:
//				pageChange(2);
//				break;
			case R.id.layout_step4:
				pageChange(2);
				break;
			}
		}
	};

	/** 控制步骤条 */
	private void controlStep(int index) {
		if (index == 0) {
			mMainView.findViewById(R.id.layout_step1).setBackgroundResource(R.drawable.safety_step1);
			((TextView) mMainView.findViewById(R.id.index1)).setTextColor(this.getResources().getColor(R.color.red));
			((TextView) mMainView.findViewById(R.id.text1)).setTextColor(this.getResources().getColor(R.color.red));
			mMainView.findViewById(R.id.layout_step2).setBackgroundResource(R.drawable.safety_step3);
			((TextView) mMainView.findViewById(R.id.index2)).setTextColor(this.getResources().getColor(R.color.gray));
			((TextView) mMainView.findViewById(R.id.text2)).setTextColor(this.getResources().getColor(R.color.gray));
//			mMainView.findViewById(R.id.layout_step3).setBackgroundResource(R.drawable.safety_step3);
//			((TextView) mMainView.findViewById(R.id.index3)).setTextColor(this.getResources().getColor(R.color.gray));
//			((TextView) mMainView.findViewById(R.id.text3)).setTextColor(this.getResources().getColor(R.color.gray));
			mMainView.findViewById(R.id.layout_step4).setBackgroundResource(R.drawable.safety_step4);
			((TextView) mMainView.findViewById(R.id.index4)).setTextColor(this.getResources().getColor(R.color.gray));
			((TextView) mMainView.findViewById(R.id.text4)).setTextColor(this.getResources().getColor(R.color.gray));
		} else if (index == 1) {
			mMainView.findViewById(R.id.layout_step1).setBackgroundResource(R.drawable.safety_step2);
			((TextView) mMainView.findViewById(R.id.index1)).setTextColor(this.getResources().getColor(R.color.gray));
			((TextView) mMainView.findViewById(R.id.text1)).setTextColor(this.getResources().getColor(R.color.gray));
			mMainView.findViewById(R.id.layout_step2).setBackgroundResource(R.drawable.safety_step1);
			((TextView) mMainView.findViewById(R.id.index2)).setTextColor(this.getResources().getColor(R.color.red));
			((TextView) mMainView.findViewById(R.id.text2)).setTextColor(this.getResources().getColor(R.color.red));
//			mMainView.findViewById(R.id.layout_step3).setBackgroundResource(R.drawable.safety_step3);
//			((TextView) mMainView.findViewById(R.id.index3)).setTextColor(this.getResources().getColor(R.color.gray));
//			((TextView) mMainView.findViewById(R.id.text3)).setTextColor(this.getResources().getColor(R.color.gray));
			mMainView.findViewById(R.id.layout_step4).setBackgroundResource(R.drawable.safety_step4);
			((TextView) mMainView.findViewById(R.id.index4)).setTextColor(this.getResources().getColor(R.color.gray));
			((TextView) mMainView.findViewById(R.id.text4)).setTextColor(this.getResources().getColor(R.color.gray));
		}
//		else if (index == 2) {
//			mMainView.findViewById(R.id.layout_step1).setBackgroundResource(R.drawable.safety_step3);
//			((TextView) mMainView.findViewById(R.id.index1)).setTextColor(this.getResources().getColor(R.color.gray));
//			((TextView) mMainView.findViewById(R.id.text1)).setTextColor(this.getResources().getColor(R.color.gray));
//			mMainView.findViewById(R.id.layout_step2).setBackgroundResource(R.drawable.safety_step2);
//			((TextView) mMainView.findViewById(R.id.index2)).setTextColor(this.getResources().getColor(R.color.gray));
//			((TextView) mMainView.findViewById(R.id.text2)).setTextColor(this.getResources().getColor(R.color.gray));
//			mMainView.findViewById(R.id.layout_step3).setBackgroundResource(R.drawable.safety_step1);
//			((TextView) mMainView.findViewById(R.id.index3)).setTextColor(this.getResources().getColor(R.color.red));
//			((TextView) mMainView.findViewById(R.id.text3)).setTextColor(this.getResources().getColor(R.color.red));
//			mMainView.findViewById(R.id.layout_step4).setBackgroundResource(R.drawable.safety_step4);
//			((TextView) mMainView.findViewById(R.id.index4)).setTextColor(this.getResources().getColor(R.color.gray));
//			((TextView) mMainView.findViewById(R.id.text4)).setTextColor(this.getResources().getColor(R.color.gray));
//		}
		else if (index == 2) {
			mMainView.findViewById(R.id.layout_step1).setBackgroundResource(R.drawable.safety_step3);
			((TextView) mMainView.findViewById(R.id.index1)).setTextColor(this.getResources().getColor(R.color.gray));
			((TextView) mMainView.findViewById(R.id.text1)).setTextColor(this.getResources().getColor(R.color.gray));
			mMainView.findViewById(R.id.layout_step2).setBackgroundResource(R.drawable.safety_step3);
			((TextView) mMainView.findViewById(R.id.index2)).setTextColor(this.getResources().getColor(R.color.gray));
			((TextView) mMainView.findViewById(R.id.text2)).setTextColor(this.getResources().getColor(R.color.gray));
//			mMainView.findViewById(R.id.layout_step3).setBackgroundResource(R.drawable.safety_step2);
//			((TextView) mMainView.findViewById(R.id.index3)).setTextColor(this.getResources().getColor(R.color.gray));
//			((TextView) mMainView.findViewById(R.id.text3)).setTextColor(this.getResources().getColor(R.color.gray));
			mMainView.findViewById(R.id.layout_step4).setBackgroundResource(R.drawable.safety_step5);
			((TextView) mMainView.findViewById(R.id.index4)).setTextColor(this.getResources().getColor(R.color.red));
			((TextView) mMainView.findViewById(R.id.text4)).setTextColor(this.getResources().getColor(R.color.red));
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && isShowguid) {
			isShowguid = false;
			showCardPriceGuid(ConstantGloble.SAFETYGUIDE);
		}
	}
	
	@Override
	public boolean doHttpErrorHandler(String method, BiiError biiError) {
		otherFrag.doHttpErrorHandler(biiError);
		showHttpErrorDialog(biiError.getCode(), biiError.getMessage());
		return true;
	}

	/** 缴费种类信息查询回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnInsurancePayTypeInfoQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap.get(Safety.PRODUCT_LIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			return;
		}
		
		SafetyDataCenter.getInstance().setListInsurancePayTypeInfoQuery(resultList);
		insurFrag.setPayTypeInfo(resultList);
	}
	
	/** 请求账户详情回调，其他信息片段需要 */
	public void requestPsnAccountQueryAccountDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		otherFrag.setAccDetail(resultMap);
	}
}
