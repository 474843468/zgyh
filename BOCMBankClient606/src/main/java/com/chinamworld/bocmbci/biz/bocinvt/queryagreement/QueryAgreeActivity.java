package com.chinamworld.bocmbci.biz.bocinvt.queryagreement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.SecondMainActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.adapter.AgreeMentQueryAdapter;
import com.chinamworld.bocmbci.biz.bocinvt.adapter.AgreeQueryExtendAdapter;
import com.chinamworld.bocmbci.biz.bocinvt.inflateView.InflaterViewDialog;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 协议查询页面
 * 
 * @author wangmengmeng
 * 
 */
public class QueryAgreeActivity extends BociBaseActivity implements
		OnCheckedChangeListener {
	/** 协议查询页面 */
	private View view;
	/** 查询切换 */
	private RadioGroup mRadioGroup;
	/** 周期性产品续约协议 */
	private RadioButton mRadioButton1;
	/** 定时定额协议 */
	private RadioButton mRadioButton2;
	/** 自动投资协议 */
	private RadioButton mRadioButton3;
	private LinearLayout mViewPager;
	/** 查询周期性产品续约协议View */
	private View zhouqiView;
	/** 查询定时定额协议View */
	private View dingshiView;
	/** 查询自动投资协议View */
	private View zidongView;
	/** 周期性产品续约协议列表 */
	private ListView lvZhouQi;
	private AgreeMentQueryAdapter zhouqiAdapter;
	/** 定时定额协议列表 */
	private ListView lvDingShi;
	private AgreeQueryExtendAdapter dingshiAdapter;
	/** 自动投资协议列表 */
	private ListView lvZiDong;
	private AgreeQueryExtendAdapter zidongAdapter;
	private boolean isOpen = false;
	private boolean isevaluation = false;
	private boolean isSignedAgreement;
	/** 登记账户信息 */
	private List<Map<String, Object>> investBindingInfo = new ArrayList<Map<String, Object>>();
	private View mFooterView;
	private List<Map<String, Object>> zqList = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> dsList = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> zdList = new ArrayList<Map<String,Object>>();
	private int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_query_agree_title));
		gonerightBtn();
		setBackBtnClick(backBtnClick);
		setLeftSelectedPosition("bocinvtManager_5");
		setUpGetMoreView();
		initfastfoot();
	}
	/** 返回点击事件 */
	OnClickListener backBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
//			ActivityTaskManager.getInstance().removeAllSecondActivity();
//			Intent intent = new Intent(QueryAgreeActivity.this,
//					SecondMainActivity.class);
//			startActivity(intent);
		goToMainActivity();

		}
	};
	/** 用于区分是否是快捷键启动 */
	public void initfastfoot() {
//		if (getIntent().getBooleanExtra(ConstantGloble.COMEFROMFOOTFAST, false)) {
			// 快捷键启动
			// 判断用户是否开通投资理财服务
			requestPsnInvestmentisOpenBefore();
			BaseHttpEngine.showProgressDialogCanGoBack();
//		} else {
//			// 添加布局
//			tabcontent.removeAllViews();
//			view = addView(R.layout.bocinvt_query_agreement);
//			initPre();
//		}
	}

	public void initPre() {
		mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
		mRadioButton1 = (RadioButton) view.findViewById(R.id.btn1);
		mRadioButton2 = (RadioButton) view.findViewById(R.id.btn2);
		mRadioButton3 = (RadioButton) view.findViewById(R.id.btn3);
		mViewPager = (LinearLayout) view.findViewById(R.id.pager);
		// 初始化监听事件
		iniListener();
		int i = this.getIntent().getIntExtra(ConstantGloble.ACC_POSITION, 0);
		if (i == 0) {
			mRadioButton1.setChecked(true);
		} else if (i == 1) {
			mRadioButton2.setChecked(true);
		} else if (i == 2) {
			mRadioButton3.setChecked(true);
		}

	}
	
	/**
	 * 初始化分页布局
	 */
	private void setUpGetMoreView(){
		mFooterView = View.inflate(this, R.layout.epay_tq_list_more, null);
		((RelativeLayout) mFooterView.findViewById(R.id.rl_get_more))
				.setBackgroundColor(getResources().getColor(
						R.color.transparent_00));
	}

	@Override
	public void bocinvtAcctCallback(Object resultObj) {
		super.bocinvtAcctCallback(resultObj);
		isOpen = isOpenBefore;
		isevaluation = isevaluated;
		investBindingInfo = investBinding;
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.BOCINVT_XPADRESET_CHOOSE, investBinding);
		if (isOpenBefore && !StringUtil.isNullOrEmpty(investBinding)
				&& isevaluated) {
			tabcontent.removeAllViews();
			view = addView(R.layout.bocinvt_query_agreement);
			initPre();
		} else {
			// 得到result
			BaseHttpEngine.dissMissProgressDialog();
			InflaterViewDialog inflater = new InflaterViewDialog(
					QueryAgreeActivity.this);
			dialogView2 = (RelativeLayout) inflater.judgeViewDialog(
					isOpenBefore, investBinding, isevaluated, manageOpenClick,
					invtBindingClick, invtEvaluationClick, exitClick);
			TextView tv = (TextView) dialogView2
					.findViewById(R.id.tv_acc_account_accountState);
			// 查询
			tv.setText(this.getString(R.string.bocinvt_query_title));

			BaseDroidApp.getInstanse().showAccountMessageDialog(dialogView2);

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				// 开通成功的响应
				isOpenBefore = true;
				isOpen = true;
			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE) {
				// 登记账户成功的响应
				investBinding = BociDataCenter.getInstance().getUnSetAcctList();
				investBindingInfo = BociDataCenter.getInstance().getUnSetAcctList();
			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE) {
				// 风险评估成功的响应
				if (BociDataCenter.getInstance().getI() == 1) {

				} else {
					isevaluated = true;
					isevaluation = true;
				}
			} else {
				// 添加布局
				tabcontent.removeAllViews();
				view = addView(R.layout.bocinvt_query_agreement);
				initPre();
				break;
			}
			if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
					&& isevaluation) {
				// 添加布局
				tabcontent.removeAllViews();
				view = addView(R.layout.bocinvt_query_agreement);
				initPre();
			} else {
				InflaterViewDialog inflater = new InflaterViewDialog(
						QueryAgreeActivity.this);
				dialogView2 = (RelativeLayout) inflater.judgeViewDialog(isOpen,
						investBindingInfo, isevaluation, manageOpenClick,
						invtBindingClick, invtEvaluationClick, exitClick);
				TextView tv = (TextView) dialogView2
						.findViewById(R.id.tv_acc_account_accountState);
				// 查询
				tv.setText(this.getString(R.string.bocinvt_query_title));

				BaseDroidApp.getInstanse()
						.showAccountMessageDialog(dialogView2);
			}

			break;
		case RESULT_CANCELED:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE
					|| requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE
					|| requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE) {

			} else {

				break;
			}
			if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
					&& isevaluation) {
			} else {
				InflaterViewDialog inflater = new InflaterViewDialog(
						QueryAgreeActivity.this);
				dialogView2 = (RelativeLayout) inflater.judgeViewDialog(isOpen,
						investBindingInfo, isevaluation, manageOpenClick,
						invtBindingClick, invtEvaluationClick, exitClick);
				TextView tv = (TextView) dialogView2
						.findViewById(R.id.tv_acc_account_accountState);
				// 查询
				tv.setText(this.getString(R.string.bocinvt_query_title));
				BaseDroidApp.getInstanse()
						.showAccountMessageDialog(dialogView2);

			}
			break;
		}
	}

	protected OnClickListener exitClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			finish();
		}
	};

	/**
	 * RadioGroup点击CheckedChanged监听
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.btn1:
			// 周期性产品续约视图
			zhouqiView = (View) this.getLayoutInflater().inflate(
					R.layout.bocinvt_query_agree_pager, null);
			mViewPager.removeAllViews();
			mViewPager.addView(zhouqiView);
			zqList.clear();
			inithistory(zhouqiView);
			break;
		case R.id.btn2:
			// 定时定额协议视图
			dingshiView = (View) getLayoutInflater().inflate(
					R.layout.bocinvt_query_agree_pager, null);
			mViewPager.removeAllViews();
			mViewPager.addView(dingshiView);
			// 初始化定时定额协议页面
			dsList.clear();
			initoverdue(dingshiView);
			break;
		case R.id.btn3:
			// 自动投资协议视图
			zidongView = (View) getLayoutInflater().inflate(
					R.layout.bocinvt_query_agree_pager, null);
			mViewPager.removeAllViews();
			zdList.clear();
			mViewPager.addView(zidongView);
			// 初始化自动投资协议页面
			initremain(zidongView);
			break;
		default:
			break;
		}

	}

	/** 初始化周期性产品续约协议页面 */
	private void inithistory(View view) {
		TextView boci_product_name = (TextView) view
				.findViewById(R.id.boci_product_name);
		boci_product_name.setText(this.getString(R.string.serialName_null));
		lvZhouQi = (ListView) view.findViewById(R.id.boc_query_result);
		zhouqiAdapter = new AgreeMentQueryAdapter(this, zqList);
		lvZhouQi.setAdapter(zhouqiAdapter);
		isSignedAgreement = false;
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}

	/** 初始化定时定额协议页面 */
	private void initoverdue(View view) {
		TextView boci_product_name = (TextView) view
				.findViewById(R.id.boci_product_name);
		boci_product_name.setText(this.getString(R.string.prod_name));
		lvDingShi = (ListView) view.findViewById(R.id.boc_query_result);
		dingshiAdapter = new AgreeQueryExtendAdapter(this, dsList);
		lvDingShi.setAdapter(dingshiAdapter);
		isSignedAgreement = true;
		BiiHttpEngine.showProgressDialog();
		requestCommConversationId();
	}

	/** 初始化自动投资协议页面 */
	private void initremain(View view) {
		TextView boci_product_name = (TextView) view
				.findViewById(R.id.boci_product_name);
		boci_product_name.setText(this.getString(R.string.prod_name));
		lvZiDong = (ListView) view.findViewById(R.id.boc_query_result);
		zidongAdapter = new AgreeQueryExtendAdapter(this, zdList);
		lvZiDong.setAdapter(zidongAdapter);
		isSignedAgreement = true;
		BiiHttpEngine.showProgressDialog();
		requestCommConversationId();
	}

	/** 初始化监听事件 */
	private void iniListener() {
		mRadioGroup.setOnCheckedChangeListener(this);
	}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (isSignedAgreement) {
			requestPsnXpadAgreementQueryExtend();return;
		}
		requestPsnXpadAgreementQuery();
	}

	/** 请求周期性产品续约协议 */
	public void requestPsnXpadAgreementQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.CYCICALAGREEMENTQUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadAgreementQueryCallBack");
	}

	/**
	 * 请求周期性产品续约协议 回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadAgreementQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		String recoderNumber = (String) map.get(BocInvt.PROGRESS_RECORDNUM);
		List<Map<String, Object>> periodList = (List<Map<String, Object>>) map.get(BocInvt.BOCI_LIST_RES);
		if (StringUtil.isNullOrEmpty(periodList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		index = index+Integer.valueOf(ConstantGloble.FOREX_PAGESIZE);
		zqList.addAll(periodList);
		addFooterView(lvZhouQi, zqList, zqListener, recoderNumber);
		zhouqiAdapter.setAgreeQueryList(zqList);
		lvZhouQi.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 周期性产品续约协议详情页面
				BociDataCenter.getInstance().setPeriodDetailMap(
						zqList.get(position));
				Intent intent = new Intent(QueryAgreeActivity.this,
						PeriodAgreeDetailActivity.class);
				startActivity(intent);
			}
		});
	}

	/** 请求定时定额协议 */
	public void requestPsnXpadAgreementQueryExtend() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.SIGNEDAGREEMENTQUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> params = new HashMap<String, String>();
		if (mRadioButton2.isChecked()) {
			// 定时定额
			params.put(BocInvt.BOC_EXTEND_AGREEMENTTYPE_REQ,
					investTypeSubList.get(0));
		} else if (mRadioButton3.isChecked()) {
			// 自动投资
			params.put(BocInvt.BOC_EXTEND_AGREEMENTTYPE_REQ,
					investTypeSubList.get(1));
		}
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadAgreementQueryExtendCallBack");
	}

	/**
	 * 请求定时定额协议
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadAgreementQueryExtendCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		String recoderNumber = (String) map.get(BocInvt.PROGRESS_RECORDNUM);
		List<Map<String, Object>> historyList = (List<Map<String, Object>>) map.get(BocInvt.BOCI_LIST_RES);
		if (StringUtil.isNullOrEmpty(historyList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}

		if (mRadioButton2.isChecked()) {
			index = index+Integer.valueOf(ConstantGloble.FOREX_PAGESIZE);
			dsList.addAll(historyList);
			addFooterView(lvDingShi, dsList, dsListener, recoderNumber);
			dingshiAdapter.setAgreeQueryList(dsList);
			lvDingShi.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO 定时定额协议详情页面
					BociDataCenter.getInstance().setPeriodDetailMap(
							dsList.get(position));
					Intent intent = new Intent(QueryAgreeActivity.this,
							DextendAgreeDetailActivity.class);
					startActivity(intent);
				}
			});
		} else if (mRadioButton3.isChecked()) {
			index = index+Integer.valueOf(ConstantGloble.FOREX_PAGESIZE);
			zdList.addAll(historyList);
			addFooterView(lvZiDong, zdList, dsListener, recoderNumber);
			zidongAdapter.setAgreeQueryList(zdList);
			lvZiDong.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO 自动投资协议详情页面
					BociDataCenter.getInstance().setPeriodDetailMap(
							zdList.get(position));
					Intent intent = new Intent(QueryAgreeActivity.this,
							ZextendAgreeDetailActivity.class);
					startActivity(intent);
				}
			});
		}

	}
	
	private final OnClickListener zqListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			requestPsnXpadAgreementQuery();
		}
	};
	
	private final OnClickListener dsListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			requestPsnXpadAgreementQueryExtend();
		}
	};
	
	/**
	 * 添加更多按钮
	 * @param v
	 * @param list
	 * @param listener
	 * @param totalCount
	 */
	private void addFooterView(ListView v,List<Map<String, Object>> list,OnClickListener listener,String totalCount) {
		if (Integer.valueOf(totalCount) > list.size()) {
			if (v.getFooterViewsCount() <= 0) {
				v.addFooterView(mFooterView);
			}
			v.setClickable(true);
		} else {
			if (v.getFooterViewsCount() > 0) {
				v.removeFooterView(mFooterView);
			}
		}
		mFooterView.setOnClickListener(listener);
	}

}
