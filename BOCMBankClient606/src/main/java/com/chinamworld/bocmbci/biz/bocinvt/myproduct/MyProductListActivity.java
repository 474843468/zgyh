package com.chinamworld.bocmbci.biz.bocinvt.myproduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.inflateView.InflaterViewDialog;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.QueryProductActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
//import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.adapter.QuantityDetailAdapter;
//import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.adapter.QuantityDetailAdapter.ViewHolder;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.MyInvetProductActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.HoldingBOCProductInfoTransformUtil;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.HoldingBOCProductInfoUtil;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.ListviewHeightUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

/**
 * 我的理财产品列表信息页
 * 
 * @author wangmengmeng
 * 
 */
public class MyProductListActivity extends BociBaseActivity implements ICommonAdapter<Map<String, Object>>{
	private static final String TAG = "MyProductListActivity";
	/** 理财产品列表页面 */
	private View view;
	private View mFooterView;
	/** 产品列表 */
	private ListView productListView;
	/** 登记账户信息 */
	private List<Map<String, Object>> investBindingInfo = new ArrayList<Map<String, Object>>();
	/** 持有产品列表 */
	private List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
	private boolean isOpen = false;
//	private boolean isevaluation = false;
	private boolean ocrmQuery = true;
	private boolean isprogress;
	// private MyProductAdapter mAdapter;
	private String acctId;

	
	

	/***************** 下面部分为P603更改 *********************************/
	/** 理财产品列表的适配器 */
	private com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.adapter.MyProductAdapter mAdapter;
	/** 服务器获取的全部产品 */
	private ArrayList<BOCProductForHoldingInfo> productsAll = new ArrayList<BOCProductForHoldingInfo>();
	/** 现金管理类 */
	private ArrayList<BOCProductForHoldingInfo> productsCash = new ArrayList<BOCProductForHoldingInfo>();
	/** 净值型 */
	private ArrayList<BOCProductForHoldingInfo> productsValue = new ArrayList<BOCProductForHoldingInfo>();
	/** 固定期限 */
	private ArrayList<BOCProductForHoldingInfo> productsFixation = new ArrayList<BOCProductForHoldingInfo>();

	private Spinner sp_product_types;

	private View content_container;
//	private View empty_view;
	private RelativeLayout empty_view;
	private TextView tv_empty_view;
	
//	/** 服务器全部持仓总数 */
//	private int productCountAll = 0;
//	/** 服务器现金管理类持仓总数 */
//	private int productCountCash = 0;
//	/** 服务器净值型产品持仓总数 */
//	private int productCountValue = 0;
//	/** 服务器固定期限类产品持仓总数 */
//	private int productCountFixation = 0;
	/** 份额明细*/
//	private QuantityDetailAdapter quantityDetailAdapter;
	/**采用通用的Adapter接口来实现数据适配功能**/
	private CommonAdapter<Map<String, Object>> quantityDetailAdapter;
	/**产品详情 */
	private Map<String, Object> responseDeal;
	/**产品期限特性*/
//	private String termType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = addView(R.layout.bocinvt_myproduct_list);
		setTitle(getString(R.string.bocinvt_hold_manager));
		setText(getString(R.string.boci_fast_trans));
		setRightBtnClick(btnRightOnclick);
		setBackBtnClick(backBtnClick);
		setLeftSelectedPosition("bocinvtManager_2");
		content_container = view.findViewById(R.id.content_container);
		//empty_view = view.findViewById(R.id.empty_view);
		empty_view = (RelativeLayout) view.findViewById(R.id.empty_layout);
		tv_empty_view = (TextView) view.findViewById(R.id.empty_view);
		tv_empty_view.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		empty_view.setVisibility(View.GONE);
		tv_empty_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*Toast.makeText(MyProductListActivity.this, "跳转至产品查询与购买",
						Toast.LENGTH_SHORT).show();*/
				toQueryProduct();
			}
		});
		// setLeftSelectedPosition(0);
		mFooterView = View.inflate(this, R.layout.epay_tq_list_more, null);
		setUpViews();
//		initfastfoot();
		BiiHttpEngine.showProgressDialogCanGoBack();
		requestPsnInvestmentisOpenBefore();
		
	}

	/** 用于区分是否是快捷键启动 */
//	public void initfastfoot() {
//		// if (getIntent().getBooleanExtra(ConstantGloble.COMEFROMFOOTFAST,
//		// false)) {
//		// 快捷键启动
//		// 判断用户是否开通投资理财服务
//		BiiHttpEngine.showProgressDialogCanGoBack();
//		requestPsnInvestmentisOpenBefore();
//		// } else {
//		// isOpen = true;
//		// isOpenBefore = true;
//		// isevaluated = true;
//		// isevaluation = true;
//		// BaseHttpEngine.showProgressDialogCanGoBack();
//		// requestInvtEvaluation();
//		// }
//	}

	/** 返回点击事件 */
	OnClickListener backBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// ActivityTaskManager.getInstance().removeAllSecondActivity();
			// Intent intent = new Intent(MyProductListActivity.this,
			// SecondMainActivity.class);
			// startActivity(intent);
			LogGloble.d(TAG, "back button click");
			ActivityTaskManager.getInstance().removeAllSecondActivity();
			Intent intent = new Intent(MyProductListActivity.this,
					MyInvetProductActivity.class);
			startActivity(intent);
			finish();
			
		}
	};

	/** 快速交易点击事件 */
	private OnClickListener btnRightOnclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
//			LayoutValue.LEWFTMENUINDEX = 1;
			MyProductListActivity.this.setLeftSelectedPosition("bocinvtManager_3");
			ActivityTaskManager.getInstance().removeAllSecondActivity();
			startActivity(new Intent(MyProductListActivity.this,
					QueryProductActivity.class));
		}
	};

	private void setUpViews() {
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}
		productListView = (ListView) view.findViewById(R.id.boci_product_list);
		productListView.addFooterView(mFooterView);
		productListView.setOnItemClickListener(listClickListener);
		// p603

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.custom_spinner_item, getResources().getStringArray(
						R.array.bocinvt_product_types));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		sp_product_types = (Spinner) findViewById(R.id.sp_product_types);
		sp_product_types.setAdapter(adapter);
		sp_product_types
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (mAdapter == null) {
							return;
						}
						// 清空数据
						mAdapter.changeData(null);
//						mAdapter.changeData(getMoreListData(0));
						refreshListViewStatus(true);
						//refreshFooterViewStatus();
						if (mAdapter.getCount() == 0) {
							requestPsnXpadHoldProductQueryList(ConstantGloble.REFRESH_FOR_MORE);
						} else {
							showContentView();
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});
		sp_product_types.setSelection(0, true);
	}

	/**
	 * 获取缓存的持仓数据
	 * 
	 * @return
	 */
	private ArrayList<BOCProductForHoldingInfo> getCacheData() {
		ArrayList<BOCProductForHoldingInfo> cache = new ArrayList<BOCProductForHoldingInfo>();
		switch (sp_product_types.getSelectedItemPosition()) {
		case 0:
			cache = productsAll;
			break;
		case 1:
			cache = productsCash;
			break;
		case 2:
			cache = productsValue;
			break;
		case 3:
			cache = productsFixation;
			break;
		}
		return cache;
	}

	/**
	 * 请求持有产品查询
	 * 
	 * @param refresh
	 */
	public void requestPsnXpadHoldProductQueryList(String refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PRODUCTBALANCEQUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("issueType", sp_product_types.getSelectedItemPosition() + "");

//		params.put(BocInvt.BOCI_CURRENTINDEX_REQ, getCacheCount() + "");
//
//		params.put(BocInvt.BOCI_PAGESIZE_REQ,
//				ConstantGloble.LOAN_PAGESIZE_VALUE);
		params.put(BocInvt.BOCI_REFRESH_REQ, refresh);

		biiRequestBody.setParams(params);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadHoldProductQueryListCallback");
	}

//	/**
//	 * 获取已获取持仓产品的个数
//	 * 
//	 * @return
//	 */
//	private int getCacheCount() {
//		int cacheCount = 0;
//		switch (sp_product_types.getSelectedItemPosition()) {
//		case 0:
//			cacheCount = productsAll.size();
//			break;
//		case 1:
//			cacheCount = productsCash.size();
//			break;
//		case 2:
//			cacheCount = productsValue.size();
//			break;
//		case 3:
//			cacheCount = productsFixation.size();
//			break;
//
//		default:
//			break;
//		}
//		return cacheCount;
//	}

	/** 请求持有产品查询回调 */
	public void requestPsnXpadHoldProductQueryListCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		if (!StringUtil.isNullOrEmpty(investBindingInfo)) {
			BociDataCenter.getInstance()
					.setInvestBindingInfo(investBindingInfo);
		}
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(list)) {
			if(sp_product_types.getSelectedItemPosition() == 0 && !getIntent().getBooleanExtra("isFrommyinveet", true)){
				toQueryProduct();
			}else{
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					getString(R.string.acc_transferquery_null));
			content_container.setVisibility(View.GONE);
			empty_view.setVisibility(View.VISIBLE);
			}
//			return;
		}
		ArrayList<BOCProductForHoldingInfo> infos = HoldingBOCProductInfoTransformUtil
				.maps2BocProductInfos(list);
	
		switch (sp_product_types.getSelectedItemPosition()) {
		case 0:
			productsAll.clear();
			productsAll.addAll(infos);
			break;
		case 1:
			productsCash.clear();
			productsCash.addAll(infos);
			break;
		case 2:
			productsValue.clear();
			productsValue.addAll(infos);
			break;
		case 3:
			productsFixation.clear();
			productsFixation.addAll(infos);
			break;

		default:
			break;
		}
		if (mAdapter == null) {
			mAdapter = new com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.adapter.MyProductAdapter();
			productListView.setAdapter(mAdapter);
		}
	
		refreshListViewStatus(true);
		
		if (sp_product_types.getSelectedItemPosition() == 0
				&& mAdapter.getCount() == 0 && !getIntent().getBooleanExtra("isFrommyinveet", true)) {// 查询全部持有产品没有查到数据，直接跳转到产品行情页
			/*Toast.makeText(this, "跳转到产品行情页", Toast.LENGTH_SHORT).show();*/
//			content_container.setVisibility(View.GONE);
//			empty_view.setVisibility(View.VISIBLE);
			toQueryProduct();
			return;
		}
		showContentView();

	}

	/** 获得 当前需要加载的更多数据   */
	private ArrayList<BOCProductForHoldingInfo> getMoreListData(int nIndex){
		int count = getCacheData().size() < nIndex + 10 ? getCacheData().size() : nIndex + 10;
		ArrayList<BOCProductForHoldingInfo> list = new ArrayList<BOCProductForHoldingInfo>();
		for(int i = nIndex; i < count; i++){
			list.add(getCacheData().get(i));
		}
		return list;
	}
	
	
	/**
	 * 显示适当的内容View
	 */
	private void showContentView() {
		if (mAdapter.getCount() == 0) {// 没有持有该类型产品
			content_container.setVisibility(View.GONE);
			empty_view.setVisibility(View.VISIBLE);
		} else {
			content_container.setVisibility(View.VISIBLE);
			empty_view.setVisibility(View.GONE);
		}
	}

	@Override
	public void responseOcrmProductQueryCallback(Object resultObj) {
		super.responseOcrmProductQueryCallback(resultObj);
		if (!StringUtil.isNullOrEmpty(BociDataCenter.getInstance().getOcrmMap()
				.get(BocInvt.RESULTLIST))) {
			findViewById(R.id.ocrm).setVisibility(View.VISIBLE);
		}
		if (ocrmQuery) {
			ocrmQuery = false;
			requestCommConversationId();
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(new Intent(MyProductListActivity.this,
				OcrmProductListActivity.class).putExtra("flag", false));
	}

	/**
	 * 指令交易
	 * 
	 * @param v
	 */
	public void buttonOnclick(View v) {
		BaseHttpEngine.showProgressDialog();
		requestOcrmProductQuery(0, "true");
	}

	/**
	 * 判断是否开通投资理财服务---回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnInvestmentisOpenBeforeCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String isOpenresult = String.valueOf(biiResponseBody.getResult());
		if (StringUtil.isNull(isOpenresult)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		boolean isOpenBeforeOr = Boolean.valueOf(isOpenresult);
		// isOpenBefore
		if (isOpenBeforeOr) {
			isOpenBefore = true;
		} else {
			isOpenBefore = false;
		}
		// 请求风险评估
//		requestInvtEva();//p603去掉风险评估在购买时增加风险评估
		isprogress = false;
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}
	
//	/**
//	 * 请求是否进行过风险评估
//	 */
//	public void requestInvtEvaluation() {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(BocInvt.PSNINVTEVALUATIONINIT_API);
//		HttpManager.requestBii(biiRequestBody, this,
//				"requestInvtEvaluationCallback");
//	}

//	/**
//	 * 请求是否进行过风险评估---回调
//	 * 
//	 * @param resultObj
//	 */
//	@SuppressWarnings("unchecked")
//	public void requestInvtEvaluationCallback(Object resultObj) {
//		Map<String, Object> responseMap = HttpTools.getResponseResult(resultObj);
//		if (StringUtil.isNullOrEmpty(responseMap)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			return;
//		}
//		BaseDroidApp.getInstanse().getBizDataMap()
//				.put(ConstantGloble.BOICNVT_ISBEFORE_RESULT, responseMap);
//		Map<String, String> inputMap = new HashMap<String, String>();
//		inputMap.put(BocInvt.BOCINVT_EVA_GENDER_REQ,
//				(String.valueOf(responseMap.get(BocInvt.BOCIEVA_GENDER_RES))));
//		inputMap.put(BocInvt.BOCINVT_EVA_RISKBIRTHDAY_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_BIRTHDAY_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_ADDRESS_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_ADDRESS_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_PHONE_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_PHONE_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_RISKMOBILE_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_MOBILE_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_POSTCODE_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_POSTCODE_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_RISKEMAIL_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_EMAIL_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_REVENUE_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_REVENUE_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_CUSTNATIONALITY_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_CUSTNATIONALITY_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_EDUCATION_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_EDUCATION_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_OCCUPATION_REQ,
//				(String) responseMap.get(BocInvt.BOCIEVA_OCCUPATION_RES));
//		inputMap.put(BocInvt.BOCINVT_EVA_HASINVESTEXPERIENCE_REQ,
//				(String) responseMap
//						.get(BocInvt.BOCIEVA_HASINVESTEXPERIENCE_RES));
//		BaseDroidApp.getInstanse().getBizDataMap()
//				.put(ConstantGloble.BOCINVT_REQUESTMAP, inputMap);
//		isprogress = false;
//		BaseHttpEngine.showProgressDialog();
//		requestCommConversationId();
//	}
	/** 份额明细的listview*/
	private ListView detailListview;
	/** 收起*/
	private View arrow;
	/** 展开*/
	private View arrow2;
	/** 份额明细*/
//	private List<Map<String, Object>> quantityList;
	/** 灰线*/
	private View grayLine;
	/** 选中的产品信息*/
	private BOCProductForHoldingInfo item;
	/** 产品列表点击事件 */
	OnItemClickListener listClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			detailListview = (ListView) view.findViewById(R.id.boc_query_detail_result);
			
			arrow = view.findViewById(R.id.arrow);
			arrow2 = view.findViewById(R.id.arrow2);
			grayLine = view.findViewById(R.id.img_line);
			LogGloble.d(TAG, "position= " + position);
			if (position - productListView.getHeaderViewsCount() > mAdapter
					.getCount() - 1) {// 更多按钮
				requestPsnXpadHoldProductQueryList(ConstantGloble.REFRESH_FOR_MORE);
				return;
			}
			item = mAdapter.getItem(position
					- productListView.getHeaderViewsCount());
			//查询产品详情
			BaseDroidApp.getInstanse().getBizDataMap()
					.put(ConstantGloble.BOCINVT_MYPRODUCT_LIST,HoldingBOCProductInfoTransformUtil.BocProductInfo2map(item));
			requestPsnXpadProductDetailQuery(item);
//			Map<String, Object> parms_map=new HashMap<String, Object>();
//			parms_map.put("xpadAccountSatus", "1");
//			parms_map.put("queryType", "0");
//			getHttpTools().requestHttp("PsnXpadAccountQuery", "requestPsnXpadAccountQueryCallBack", parms_map, true);
//			//判断是否为业绩基准型，如果是可展开份额明细
//			if("3".equals(item.issueType)&&HoldingBOCProductInfoUtil.isStandardPro(item)){
//				if(detailListview.getVisibility() == View.VISIBLE) {  // 数据已展开，此时需要收起数据列表
//					detailListview.setVisibility(View.GONE);
//					arrow.setVisibility(View.VISIBLE);
//					arrow2.setVisibility(View.GONE);
//					grayLine.setVisibility(View.GONE);
//				}
//				else {
//					if(detailListview.getTag() != null && detailListview.getTag().toString() == "IsRequest") {
//						//判断是否请求过数据，如果已请求过直接显示
//					}
//					else {
//						termType = item.termType;
//						requestPsnXpadQuantityDetailList(item.prodCode,item.cashRemit);
//					}
//					detailListview.setVisibility(View.VISIBLE);	
//					arrow.setVisibility(View.GONE);
//					arrow2.setVisibility(View.VISIBLE);
//					grayLine.setVisibility(View.VISIBLE);
//				}
//				
//			}else{
//				BaseDroidApp.getInstanse().getBizDataMap()
//				.put(ConstantGloble.BOCINVT_QUANTITY_DETAIL_LIST,null);
//				// 进入详情页面
//				Intent intent = new Intent(MyProductListActivity.this,
//						MyProductDetailActivity.class);
//				startActivity(intent);
//			}
		}
	};

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
				investBindingInfo = BociDataCenter.getInstance()
						.getUnSetAcctList();
			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE) {
				// 风险评估成功的响应
				if (BociDataCenter.getInstance().getI() == 1) {

				} else {
//					isevaluated = true;
//					isevaluation = true;
				}
			} else {

				if (!StringUtil.isNullOrEmpty(productList)) {
					productList.clear();
				}
				btn_right.setVisibility(View.GONE);
				isprogress = false;
				BaseHttpEngine.showProgressDialog();
//				requestInvtEvaluation();
				requestCommConversationId();
				break;
			}
			if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
					/*&& isevaluation*/) {
				if (!StringUtil.isNullOrEmpty(productList)) {
					productList.clear();
				}
				btn_right.setVisibility(View.GONE);
				isprogress = false;
//				requestInvtEvaluation();
				requestCommConversationId();
				BaseHttpEngine.showProgressDialogCanGoBack();
			} else {
				InflaterViewDialog inflater = new InflaterViewDialog(
						MyProductListActivity.this);
//				dialogView2 = (RelativeLayout) inflater.judgeViewDialog(isOpen,
//						investBindingInfo, isevaluation, manageOpenClick,
//						invtBindingClick, invtEvaluationClick, exitClick);
				dialogView2 = (RelativeLayout)inflater.judgeViewDialog_choice(isOpen,
						investBinding, false, manageOpenClick,
						invtBindingClick, null, exitClick);
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
					/*&& isevaluation*/) {
			} else {
				InflaterViewDialog inflater = new InflaterViewDialog(
						MyProductListActivity.this);
//				dialogView2 = (RelativeLayout) inflater.judgeViewDialog(isOpen,
//						investBindingInfo, isevaluation, manageOpenClick,
//						invtBindingClick, invtEvaluationClick, exitClick);
				dialogView2 = (RelativeLayout)inflater.judgeViewDialog_choice(isOpen,
						investBinding, false, manageOpenClick,
						invtBindingClick, null, exitClick);
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

	@Override
	public void bocinvtAcctCallback(Object resultObj) {
		super.bocinvtAcctCallback(resultObj);
		isOpen = isOpenBefore;
//		isevaluation = isevaluated;
		investBindingInfo = investBinding;
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.BOCINVT_XPADRESET_CHOOSE, investBinding);
		if (isOpenBefore && !StringUtil.isNullOrEmpty(investBinding)
				/*&& isevaluated*/) {
//			requestInvtEvaluation();
			isprogress = false;
			requestCommConversationId();
		} else {
			// 得到result
			BaseHttpEngine.dissMissProgressDialog();
			InflaterViewDialog inflater = new InflaterViewDialog(
					MyProductListActivity.this);
//			dialogView2 = (RelativeLayout) inflater.judgeViewDialog(
//					isOpenBefore, investBindingInfo, isevaluated,
//					manageOpenClick, invtBindingClick, invtEvaluationClick,
//					exitClick);
			dialogView2 = (RelativeLayout)inflater.judgeViewDialog_choice(isOpenBefore,
					investBinding, false, manageOpenClick,
					invtBindingClick, null, exitClick);
			TextView tv = (TextView) dialogView2
					.findViewById(R.id.tv_acc_account_accountState);
			// 查询
			tv.setText(this.getString(R.string.bocinvt_query_title));

			BaseDroidApp.getInstanse().showAccountMessageDialog(dialogView2);

		}
	}

	/**
	 * 初始化分页布局
	 */
//	private void setUpGetMoreView() {
//		if (mFooterView == null)
//			mFooterView = View.inflate(this, R.layout.epay_tq_list_more, null);
//		// mFooterView .setOnClickListener(new OnClickListener() {
//		// @Override
//		// public void onClick(View v) {
//		// requestPsnXpadHoldProductQueryList(ConstantGloble.REFRESH_FOR_MORE);
//		// }
//		// });
//		// mFooterView.setVisibility(View.GONE);
//
//	}

	/**
	 * 刷新更多按钮的状态
	 */
//	private void refreshFooterViewStatus() {
//		boolean showFooter = showFooter();
//		if (showFooter && productListView.getFooterViewsCount() <= 0) {
//			productListView.addFooterView(mFooterView);
//		} else if ((!showFooter) && productListView.getFooterViewsCount() > 0) {
//
//			productListView.removeFooterView(mFooterView);
//		}
//
//	}

	/**
	 * 是否显示更多按钮
	 * 
	 * @return
	 */
//	private boolean showFooter() {
//		// 是否显示更多按钮
//		boolean showFooter = false;
//		switch (sp_product_types.getSelectedItemPosition()) {
//		case 0:
//			showFooter = productCountAll > productsAll.size() ? true : false;
//			break;
//		case 1:
//			showFooter = productCountCash > productsCash.size() ? true : false;
//			break;
//		case 2:
//			showFooter = productCountValue > productsValue.size() ? true
//					: false;
//			break;
//		case 3:
//			showFooter = productCountFixation > productsFixation.size() ? true
//					: false;
//			break;
//
//		default:
//			break;
//		}
//		return showFooter;
//	}

	/**
	 * 刷新更多按钮状态
	 * @param isClearListView : 是否需要清空当前的ListView中的数据项
	 */
	private void refreshListViewStatus(boolean isClearListView) {
		if(isClearListView == true)
			mAdapter.changeData(null);
		mAdapter.addDataAll(getMoreListData(mAdapter.getCount()));
		
		if (getCacheData().size() > mAdapter.getCount()) {
			if (productListView.getFooterViewsCount() <= 0) {
				productListView.addFooterView(mFooterView);
			}
			productListView.setClickable(true);
		} else {
			if (productListView.getFooterViewsCount() > 0) {
				productListView.removeFooterView(mFooterView);
			}
		}
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshListViewStatus(false);
			}
		});
	}


//	/** 收益累进 */
//	private OnItemClickListener progressOnclik = new OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			isprogress = true;
//			MyProductListActivity.this.position = position;
//			BaseHttpEngine.showProgressDialog();
//			requestCommConversationId();
//		}
//	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 累进产品
		if (isprogress) {
			for (int i = 0; i < BociDataCenter.getInstance()
					.getBocinvtAcctList().size(); i++) {
				String acctNum = (String) BociDataCenter.getInstance()
						.getBocinvtAcctList().get(i).get(BocInvt.ACCOUNTNO);
				if (productList.get(position).get(BocInvt.BANCACCOUNT)
						.equals(acctNum)) {
					acctId = (String) BociDataCenter.getInstance()
							.getBocinvtAcctList().get(i).get(Comm.ACCOUNT_ID);
				}
			}
			requestProgress(
					acctId,
					(String) productList.get(position).get(
							BocInvt.BOCI_DETAILPRODCODE_RES), "0", true);
			return;
		}
		// 指令交易
		if (ocrmQuery) {
			BociDataCenter.getInstance().setOcrmConversationId(
					(String) BaseDroidApp.getInstanse().getBizDataMap()
							.get(ConstantGloble.CONVERSATION_ID));
			requestOcrmProductQuery(0, "true");
			return;
		}
		// 持仓
		requestPsnXpadHoldProductQueryList(ConstantGloble.LOAN_REFRESH_FALSE);
	}

	@Override
	public void progressQueryCallBack(Object resultObj) {
		super.progressQueryCallBack(resultObj);
		String code = (String) productList.get(position).get(
				BocInvt.BOCI_DETAILPRODCODE_RES);
		String name = (String) productList.get(position).get(
				BocInvt.BOCI_DETAILPRODNAME_RES);
		startActivity(new Intent(this, ProgressInfoActivity.class)
				.putExtra(Comm.ACCOUNT_ID, acctId)
				.putExtra(BocInvt.PROGRESS_RECORDNUM, progressRecordNumber)
				.putExtra(BocInvt.BOCI_DETAILPRODNAME_RES, name)
				.putExtra(BocInvt.BOCI_PRODUCTCODE_REQ, code));
	}
	/**
	 * 跳转到查询与购买
	 */
	public void toQueryProduct(){
		Intent intent = new Intent(MyProductListActivity.this, QueryProductActivity.class);
		intent.putExtra("issueType", BocInvestControl.list_issueType.get(sp_product_types.getSelectedItemPosition()));
		startActivity(intent);
	}
	/**
	 * 拦截理财推荐后台错误
	 */
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (BocInvt.PSNOCRMPRODUCTQUERY.equals(biiResponseBody
					.getMethod())) {
				ocrmQuery = false;
				requestCommConversationId();
				
				return false;
			}
		}
		return super.httpRequestCallBackPre(resultObj);
	}
	/**
	 * 请求份额明细
	 */
	public void requestPsnXpadQuantityDetailList(String proCode,String charCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod("PsnXpadQuantityDetail");
		Map<String, String> params = new HashMap<String, String>();	
		params.put("productCode", proCode);
		params.put("charCode", charCode);
		biiRequestBody.setParams(params);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,"requestPsnXpadQuantityDetailListCallBack");
	}
	/**
	 * 请求份额明细回调
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadQuantityDetailListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		final List<Map<String, Object>> quantityList = (List<Map<String, Object>>) map.get("list");
		if (StringUtil.isNullOrEmpty(quantityList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
//		quantityDetailAdapter = new QuantityDetailAdapter(this, quantityList);
		quantityDetailAdapter = new CommonAdapter<Map<String,Object>>(MyProductListActivity.this, quantityList,this );
		detailListview.setAdapter(quantityDetailAdapter);
		detailListview.setTag("IsRequest"); // 已经请求过数据，以后不需要再次请求此数据
		ListviewHeightUtils.setListViewHeightBasedOnChildren(detailListview);
		detailListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.BOCINVT_QUANTITY_DETAIL_LIST,quantityList.get(position));
				// 进入详情页面
				requestPsnXpadProductDetailQueryforSto(quantityList.get(position).get("bancAccountKey").toString());
//				Intent intent = new Intent(MyProductListActivity.this,
//						MyProductDetailActivity.class);
//				startActivity(intent);
			}
		});
	}
	
//	/**
//	 * 
//	 * @param resultObj
//	 */
//	public void requestPsnXpadAccountQueryCallBack(Object resultObj){
//		if (StringUtil.isNullOrEmpty(resultObj)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			return;
//		}
//		Map<String, Object> result_map=getHttpTools().getResponseResult(resultObj);
//		List<Map<String, Object>> result_list = (List<Map<String, Object>>) result_map.get("list");
//		String ibknum = "";
//		for (int i = 0; i < result_list.size(); i++) {
//			if (item.bancAccountKey.toString()
//					.equals(result_list.get(i).get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES).toString())) {
//				ibknum = result_list.get(i).get("ibkNumber").toString();
//			}
//		}
//		requestPsnXpadProductDetailQuery(item/*,ibknum*/);
//	}
//	
	/**
	 * 请求产品详情
	 */
	private void requestPsnXpadProductDetailQuery(BOCProductForHoldingInfo info/*,String ibknum*/){
		List<Map<String, Object>> investBinding = BociDataCenter.getInstance().getBocinvtAcctList();
		String ibknum = "";
		if(!StringUtil.isNullOrEmpty(item.bancAccountKey)){
			for (int i = 0; i < investBinding.size(); i++) {
				if (item.bancAccountKey.toString()
						.equals(investBinding.get(i).get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES).toString())) {
					ibknum = investBinding.get(i).get("ibkNumber").toString();
				}
			}
		}
		HashMap<String, Object> map_parms = new HashMap<String, Object>();
		map_parms.put(BocInvestControl.PRODUCTKIND, String.valueOf(info.productKind));
		map_parms.put(BocInvestControl.PRODUCTCODE, String.valueOf(info.prodCode));
		map_parms.put("ibknum", ibknum);
		BaseHttpEngine.showProgressDialog();
		getHttpTools().requestHttp(BocInvt.PRODUCTDETAILQUERY, "requestPsnXpadProductDetailQueryCallBack", map_parms, true);
	}
	/**
	 * 请求产品详情 回调
	 */
	@SuppressWarnings({ "static-access" })
	public void requestPsnXpadProductDetailQueryCallBack(Object resultObj){
		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		responseDeal = getHttpTools().getResponseResult(resultObj);
		BaseDroidApp.getInstanse().getBizDataMap()
		.put(ConstantGloble.BOCINVT_PRODUCT_DETAIL_MAP,responseDeal);
//		BaseHttpEngine.dissMissProgressDialog();
		//判断是否为业绩基准型，如果是可展开份额明细
		if("3".equals(item.issueType)&&HoldingBOCProductInfoUtil.isStandardPro(item)){
			if(detailListview.getVisibility() == View.VISIBLE) {  // 数据已展开，此时需要收起数据列表
				BaseHttpEngine.dissMissProgressDialog();
				detailListview.setVisibility(View.GONE);
				arrow.setVisibility(View.VISIBLE);
				arrow2.setVisibility(View.GONE);
				grayLine.setVisibility(View.GONE);
			}
			else {
				if(detailListview.getTag() != null && detailListview.getTag().toString() == "IsRequest") {
					//判断是否请求过数据，如果已请求过直接显示
					BaseHttpEngine.dissMissProgressDialog();
				}
				else {
//					termType = item.termType;
					requestPsnXpadQuantityDetailList(item.prodCode,item.cashRemit);
				}
				detailListview.setVisibility(View.VISIBLE);	
				arrow.setVisibility(View.GONE);
				arrow2.setVisibility(View.VISIBLE);
				grayLine.setVisibility(View.VISIBLE);
			}
			
		}else{
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().getBizDataMap()
			.put(ConstantGloble.BOCINVT_QUANTITY_DETAIL_LIST,null);
			// 进入详情页面
			Intent intent = new Intent(MyProductListActivity.this,
					MyProductDetailActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * 有份额明细时再次请求产品详情
	 * accountKey 份额明细返回的bancAccountKey
	 */
	private void requestPsnXpadProductDetailQueryforSto(String accountKey){
		List<Map<String, Object>> investBinding = BociDataCenter.getInstance().getBocinvtAcctList();
		String ibknum = "";
		if(!StringUtil.isNullOrEmpty(accountKey)){
			for (int i = 0; i < investBinding.size(); i++) {
				if (accountKey.toString()
						.equals(investBinding.get(i).get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES).toString())) {
					ibknum = investBinding.get(i).get("ibkNumber").toString();
				}
			}
		}
		HashMap<String, Object> map_parms = new HashMap<String, Object>();
		map_parms.put(BocInvestControl.PRODUCTKIND, String.valueOf(item.productKind));
		map_parms.put(BocInvestControl.PRODUCTCODE, String.valueOf(item.prodCode));
		map_parms.put("ibknum", ibknum);
		BaseHttpEngine.showProgressDialog();
		getHttpTools().requestHttp(BocInvt.PRODUCTDETAILQUERY, "requestPsnXpadProductDetailQueryforStoCallBack", map_parms, true);
	}
	/**
	 * 有份额明细时再次请求产品详情回调
	 */
	@SuppressWarnings("static-access")
	public void requestPsnXpadProductDetailQueryforStoCallBack(Object resultObj){
		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		responseDeal = getHttpTools().getResponseResult(resultObj);
		BaseDroidApp.getInstanse().getBizDataMap()
		.put(ConstantGloble.BOCINVT_PRODUCT_DETAIL_MAP,responseDeal);
		Intent intent = new Intent(MyProductListActivity.this,
				MyProductDetailActivity.class);
		startActivity(intent);
	}
	
	@Override
	public View getView(int arg0, Map<String, Object> currentItem,
			LayoutInflater inflater, View convertView, ViewGroup viewGroup) {

		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.bocinvt_product_item_fixation_small, null);
			viewHolder.tv_quantity = (TextView) convertView
					.findViewById(R.id.tv_hold_share);
			viewHolder.tv_endDate = (TextView) convertView
					.findViewById(R.id.tv_due_time);
			viewHolder.tv_yearly = (TextView) convertView
					.findViewById(R.id.tv_earnings_percent);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(MyProductListActivity.this,
					viewHolder.tv_quantity);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(MyProductListActivity.this,
					viewHolder.tv_endDate);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(MyProductListActivity.this,
					viewHolder.tv_yearly);
			viewHolder.iv_go = (ImageView) convertView
					.findViewById(R.id.arrow);
			viewHolder.iv_go.setVisibility(View.VISIBLE);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 赋值操作
		viewHolder.tv_quantity.setText(StringUtil.parseStringPattern(String.valueOf(currentItem.get("holdingQuantity")),2));
		if("-1".equals(currentItem.get("prodEnd"))){
			viewHolder.tv_endDate.setText(R.string.bocinvt_longTime);
		}else{
			viewHolder.tv_endDate.setText(String.valueOf(currentItem.get("prodEnd")));
		}
		
		viewHolder.tv_yearly.setText(StringUtil.append2Decimals(String.valueOf(currentItem.get("yearlyRR")),2)+"%");	
		return convertView;
	
	}
	private class ViewHolder {
		/** 持有份额 */
		public TextView tv_quantity;
		/** 产品到期日 */
		public TextView tv_endDate;
		/** 预期年收益率 */
		public TextView tv_yearly;
		public ImageView iv_go;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		ActivityTaskManager.getInstance().removeAllSecondActivity();
		Intent intent = new Intent(MyProductListActivity.this,
				MyInvetProductActivity.class);
		startActivity(intent);
		finish();
	}
}
