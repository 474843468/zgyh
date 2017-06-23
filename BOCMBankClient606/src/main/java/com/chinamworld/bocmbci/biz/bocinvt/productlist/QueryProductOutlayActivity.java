package com.chinamworld.bocmbci.biz.bocinvt.productlist;

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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.SecondMainActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.adapter.ProductQueryOutlayAdapter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 注：P603继续使用
 * 
 * 功能外置 产品查询页面
 * 
 * @author sunh
 * 
 */
public class QueryProductOutlayActivity extends BociBaseActivity implements
		OnClickListener {
	private int iconSize;
	/** 查询信息页 */
	private View view;
	/** 组合查询 */
	private LinearLayout combinateLayout;
	// 下拉列表
	/** 产品类型 */
	private Spinner spinner_product_type;
	/** 产品币种 */
	private Spinner productCurrCode;
	/** 产品风险类型 */
	private Spinner productRiskType;
	private String riskType;
	/** 产品期限 */
	private Spinner prodTimeLimit;
	private String timeLimit;
	/** 产品销售状态 */
	private Spinner xpadStatus;
	private String status;
	/** 排序条件 **/
	// private Spinner sortType;
	// 按钮
//	private Button  ;
	
	
	/** 组合查询 */
	private LinearLayout layout_fast_more,layout_combination_more,combinateQueryproduct;

	// 查询结果页面
	private LinearLayout query_result;
	/** 查询列表 */
	private ListView queryListView, fastListView;
	// 查询结果头
	/** 组合查询头 */
	private LinearLayout header_for_combinate;

	/** 产品种类 */
	private TextView boci_producttype;
	/** 风险类型 */
	private TextView boci_riskType;
	/** 产品期限 */
	private TextView boci_timeLimit;
	/** 产品销售状态 */
	private TextView boci_status;
	// 下拉列表内容
	/** 产品种类 */
	// private List<Map<String, String>> productTypeList;
	/** 组合查询结果 */
	private List<Map<String, Object>> combinateList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> fastList = new ArrayList<Map<String, Object>>();

	/** 假数据 */
	private boolean isfirst = true;
	// private RelativeLayout rl_query_transfer;
	/** 进入即进行查询 */
	// private boolean first = true;
	private RelativeLayout load_more;
	private Button btn_load_more;
	private int recordNumber = 0;
	private int loadNumber = 0;
	private ProductQueryOutlayAdapter adapter;
	private ProductQueryOutlayAdapter fastAdapter;
	private boolean isProgress;
	private int combinIndex;
	private EditText mEditText;
	private String recentAccount;
	private LinearLayout queryLayout;
	private String requestStatus;
	private String availableAmt;
	private boolean isFast = false;
	private boolean isSort = false;
	/** 产品排序方式 */
	private String sortFlag = "1";
	/** 排序条件 */
	private String sortType = "0";
	/** 排序 **/
	private TextView sortBuyAmout, sortLimit, sortYearRR;
	/** 产品类型**/
	private String issueType_value;
	private String productCode;
	private boolean isEnterDetail = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iconSize = getResources().getDimensionPixelSize(R.dimen.dp_for_zero) / 2;
		// 为界面标题赋值
		setTitle(getResources().getString(R.string.product_outlay_list));
		setLeftSelectedPosition(null);
		setBackBtnClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent(QueryProductOutlayActivity.this,
//						SecondMainActivity.class);
//				startActivity(intent);
				goToMainActivity();
			
				
		
			}
		});
		Intent intent = getIntent();
		if (StringUtil.isNullOrEmpty(intent)) {
			return;
		}else{
			isEnterDetail = intent.getBooleanExtra("isEnterDetail", false);
			productCode = intent.getStringExtra("productCode");
		}
		if(isEnterDetail){
			isFast = true;
		}
		init();
		BaseHttpEngine.showProgressDialog();
		requestLoginPreOutlayConversationId();
	}

	/** 初始化界面 */
	private void init() {
		animation_up = AnimationUtils.loadAnimation(this, R.anim.scale_out);
		animation_down = AnimationUtils.loadAnimation(this, R.anim.scale_in);
		// 添加布局
		view = addView(R.layout.bocinvt_queryproduct_outlay_activity);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}
		queryLayout = (LinearLayout) view.findViewById(R.id.ll_query_condition);
		combinateLayout = (LinearLayout) view
				.findViewById(R.id.ll_combinate_queryproduct);
		spinner_product_type = (Spinner) view.findViewById(R.id.spinner_product_type);
		productCurrCode = (Spinner) view.findViewById(R.id.boci_currencode);
		productRiskType = (Spinner) view
				.findViewById(R.id.boci_productRiskType_query);
		prodTimeLimit = (Spinner) view
				.findViewById(R.id.boci_prodTimeLimit_query);
		xpadStatus = (Spinner) view.findViewById(R.id.boci_xpadStatus_query);
		layout_fast_more = (LinearLayout) view.findViewById(R.id.layout_fast_more);
		layout_combination_more = (LinearLayout) view
				.findViewById(R.id.layout_combination_more);
		mEditText = (EditText) view.findViewById(R.id.editView);
		if(isEnterDetail){
			mEditText.setText(productCode);
		}
		sortBuyAmout = (TextView) view
				.findViewById(R.id.sort_buystartingamount);
		sortLimit = (TextView) view.findViewById(R.id.sort_limit);
		sortYearRR = (TextView) view.findViewById(R.id.sort_yearlyrr);
		sortBuyAmout.setOnClickListener(this);
		sortLimit.setOnClickListener(this);
		sortYearRR.setOnClickListener(this);

		// 按钮初始化
		combinateQueryproduct = (LinearLayout) view
				.findViewById(R.id.btn_combinate_queryproduct);

		combinateQueryproduct.setOnClickListener(combinateQueryClick);
		queryLayout.setVisibility(View.VISIBLE);
		clickSpinner();
		initfirst();
	}

	public void initfirst() {
		query_result = (LinearLayout) view.findViewById(R.id.ll_query_result);
		queryListView = (ListView) view.findViewById(R.id.boci_query_list);
		fastListView = (ListView) view.findViewById(R.id.fast_query_list);
		queryListView.setFocusable(true);
		queryListView.setOnItemClickListener(onListClickListener);
		fastListView.setFocusable(true);
		fastListView.setOnItemClickListener(onListClickListener);
		riskType = LocalData.bocinvtProductRiskType.get(0);
		timeLimit = BocInvestControl.list_prodTimeLimit.get(0);
		status = LocalData.bocinvtXpadStatus.get(1);
		layout_combination_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				view.findViewById(R.id.layout_fast).setVisibility(View.VISIBLE);

				view.findViewById(R.id.layout_combination).setVisibility(
						View.GONE);

			}
		});
		layout_fast_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				view.findViewById(R.id.layout_fast).setVisibility(View.GONE);

				view.findViewById(R.id.layout_combination).setVisibility(
						View.VISIBLE);

			}
		});
		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.acc_load_more, null);
		btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);
		btn_load_more.setOnClickListener(new OnClickListener() {
//		load_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestCombinateForMore();
			}
		});
	}

	/** Spinner点击事件 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void clickSpinner() {
		// 产品种类
		// final List<String> typeNameList = new ArrayList<String>();
		// final List<String> typeIdList = new ArrayList<String>();
		// for (int i = 0; i < productTypeList.size(); i++) {
		// typeNameList.add(productTypeList.get(i).get(
		// String.valueOf(BocInvt.TYPE_NAME_RES)));
		// typeIdList.add(productTypeList.get(i).get(
		// String.valueOf(BocInvt.TYPE_BRANDID_RES)));
		// }
		// 下拉列表操作
		// 产品类型
		ArrayAdapter<ArrayList<String>> adapter6 = new ArrayAdapter(this,
			R.layout.custom_spinner_item, BocInvestControl.list_issueType);
		adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_product_type.setAdapter(adapter6);
		spinner_product_type.setSelection(0);
		// 产品币种
		ArrayAdapter<ArrayList<String>> adapter1 = new ArrayAdapter(this,
				R.layout.custom_spinner_item, LocalData.bocinvtProductCurCode);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		productCurrCode.setAdapter(adapter1);
		productCurrCode.setSelection(1);
		// productType.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view,
		// int position, long id) {
		// type = typeIdList.get(position);
		// typename = typeNameList.get(position);
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent) {
		// type = typeIdList.get(0);
		// typename = typeNameList.get(0);
		// }
		// });
		// TODO
		// for (int j = 0; j < typeNameList.size(); j++) {
		// String name = typeNameList.get(j);
		// if (!StringUtil.isNull(name)
		// && name.equals(ConstantGloble.BOCINVT_RMB)) {
		// productType.setSelection(j);
		// break;
		// }
		// }
		// 风险类型
		ArrayAdapter<ArrayList<String>> adapter3 = new ArrayAdapter(this,
				R.layout.custom_spinner_item, LocalData.bocinvtProductRiskType);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		productRiskType.setAdapter(adapter3);
		productRiskType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				riskType = LocalData.bocinvtProductRiskType.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				riskType = LocalData.bocinvtProductRiskType.get(0);
			}
		});
		// 产品期限
		ArrayAdapter<ArrayList<String>> adapter4 = new ArrayAdapter(this,
				R.layout.custom_spinner_item, BocInvestControl.list_prodTimeLimit);
		adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prodTimeLimit.setAdapter(adapter4);
		prodTimeLimit.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				timeLimit = BocInvestControl.list_prodTimeLimit.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				timeLimit = BocInvestControl.list_prodTimeLimit.get(0);
			}
		});
		// 产品销售状态
		ArrayAdapter<ArrayList<String>> adapter5 = new ArrayAdapter(this,
				R.layout.custom_spinner_item, LocalData.bocinvtXpadStatus);
		adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		xpadStatus.setAdapter(adapter5);
		xpadStatus.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				status = LocalData.bocinvtXpadStatus.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				status = LocalData.bocinvtXpadStatus.get(0);
			}
		});
		xpadStatus.setSelection(1);
	}

	// 组合查询点击事件 sunh
	OnClickListener combinateQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			BaseHttpEngine.showProgressDialog();
			isFast = false;
			requestLoginPreOutlayConversationId();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_FIRST_USER:
			animation_down.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// queryLayout.setVisibility(View.GONE);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					combinateLayout
							.setBackgroundResource(R.drawable.img_bg_query_no);
					queryLayout.setVisibility(View.VISIBLE);

				}
			});
			break;
		case RESULT_OK:
			if (requestCode == ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE) {
				BociDataCenter.getInstance().clearBociData();
				Intent intent = new Intent();
				intent.setClass(QueryProductOutlayActivity.this,
						QueryProductActivity.class);
				startActivity(intent);
				QueryProductOutlayActivity.this.finish();
			}

			break;
		case RESULT_CANCELED:

			break;
		}
	}

	protected OnClickListener exitDialogClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			finish();
		}
	};

	/** 请求组合查询 */
	public void requestcombinateQueryOutlay() {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PRODUCTLISTQUERYOUTLAY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap()
				.get(ConstantGloble.LOGIN_OUTLAY_PRECONVERSATIONID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCI_PRODUCTTYPE_REQ, "0");// productType
															// 产品属性分类String
															// LocalData.boci_ProductTypeMap.get(0)
															// M 0：全部 1：自营产品
															// 2：代销产品
															// 默认全部
		paramsmap.put(BocInvt.BOCI_PRODUCTCODE_REQ, mEditText.getText()
				.toString().trim());
		paramsmap.put(BocInvt.BOCI_PRODUCTSTA_REQ,
				LocalData.bociStatusMap.get(status));// productSta 产品销售状态 String
														// M 目前只支持在售产品查询
														// 0：全部产品、1：在售产品、2：停售产品

		paramsmap.put(BocInvt.SORTFLAG, sortFlag);// sortFlag 排序方式 String M 0:正序
													// 1:倒序

		paramsmap.put(BocInvt.SORTSITE, sortType); // sortSite 排序字段 String M
													// 0:产品销售期1:产品期限2:收益率3:购买起点金额

		paramsmap.put(BocInvt.BOCI_PAGESIZE_REQ,
				ConstantGloble.LOAN_PAGESIZE_VALUE); // pageSize 每页显示条数 String M
		paramsmap.put(BocInvt.BOCI_CURRENTINDEX_REQ, ConstantGloble.LOAN_CURRENTINDEX_VALUE); // currentIndex 当前页
															// String M 初始值赋值为1
		paramsmap.put(BocInvt.BOCI_REFRESH_REQ,
				ConstantGloble.LOAN_REFRESH_FALSE); // _refresh 刷新标识 String M

		paramsmap.put(BocInvt.BOCI_PRODUCTRISKTYPE_REQ,
				LocalData.bociRiskTypeMap.get(riskType)); // productRiskType
															// 产品风险类型 String o
															// 0=全部 1=保本固定收益
															// 2=保本浮动收益
															// 3=非保本浮动收益
//		paramsmap.put(BocInvt.BOCI_PRODTIMELIMIT_REQ,
//				LocalData.bocitimeLimitMap.get(timeLimit)); // prodTimeLimit
															// 产品期限 String o
															// 0=全部 1=3个月以内
															// 2=3个月-6个月
															// 3=6个月-12个月
															// 4=12个月-24个月
															// 5=24个月以上
		paramsmap.put(BocInvt.BOCI_PRODTIMELIMIT_REQ,"0"); //604修改为默认上送0
		paramsmap.put(BocInvt.BOCI_PRODUCTCURCODE_REQ, LocalData.bociCurcodeMap
				.get(productCurrCode.getSelectedItem().toString())); // productCurCode
																		// 产品币种
																		// String
																		// o

		
		//P603新增字段
				paramsmap.put("productKind", "0");//产品性质		0:全部1:结构性理财产品2:类基金理财产品可不上送，不送时“产品性质”默认为“1:结构性理财”
//				paramsmap.put("issueType", "0");//产品类型		1：现金管理类产品2：净值开放类产品3：固定期限产品默认上送0
				paramsmap.put("issueType", BocInvestControl.map_issueType
						.get(spinner_product_type.getSelectedItem().toString()));//产品类型		1：现金管理类产品2：净值开放类产品3：固定期限产品默认上送0
				paramsmap.put("dayTerm", BocInvestControl.map_prodTimeLimit.get(timeLimit));//产品期限（天数）		0：全部1：30天以内2：31-90天3：91-180天4：180天以上默认上送0
				paramsmap.put("proRisk", "0");//风险等级		0：全部1：低风险2：中低风险3：中等风险4：中高风险5：高风险默认上送0
				paramsmap.put("isLockPeriod", "1");//是否支持业绩基准产品查询     0：不支持1：支持默认上送0
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestOutlayBii(biiRequestBody, this,
				"requestCombinateOutlayCallBack");

	}

	/**
	 * 快速请求
	 */
	public void requestFastQueryOutlay() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PRODUCTLISTQUERYOUTLAY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap()
				.get(ConstantGloble.LOGIN_OUTLAY_PRECONVERSATIONID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCI_PRODUCTTYPE_REQ, "0");// productType
															// 产品属性分类String
															// LocalData.boci_ProductTypeMap.get(0)
															// M 0：全部 1：自营产品
															// 2：代销产品
															// 默认全部
		paramsmap.put(BocInvt.BOCI_PRODUCTCODE_REQ, mEditText.getText()
				.toString().trim());
		paramsmap.put(BocInvt.BOCI_PRODUCTSTA_REQ,
				LocalData.bociStatusMap.get(status));// productSta 产品销售状态 String
														// M 目前只支持在售产品查询
														// 0：全部产品、1：在售产品、2：停售产品

		paramsmap.put(BocInvt.SORTFLAG, sortFlag);// sortFlag 排序方式 String M 0:正序
													// 1:倒序

		paramsmap.put(BocInvt.SORTSITE, sortType); // sortSite 排序字段 String M
													// 0:产品销售期1:产品期限2:收益率3:购买起点金额

		paramsmap.put(BocInvt.BOCI_PAGESIZE_REQ,
				ConstantGloble.LOAN_PAGESIZE_VALUE); // pageSize 每页显示条数 String M
		paramsmap.put(BocInvt.BOCI_CURRENTINDEX_REQ, ConstantGloble.LOAN_CURRENTINDEX_VALUE); // currentIndex 当前页
															// String M 初始值赋值为1
		paramsmap.put(BocInvt.BOCI_REFRESH_REQ,
				ConstantGloble.LOAN_REFRESH_FALSE); // _refresh 刷新标识 String M

		paramsmap.put(BocInvt.BOCI_PRODUCTRISKTYPE_REQ,
				LocalData.bociRiskTypeMap.get(riskType)); // productRiskType
															// 产品风险类型 String o
															// 0=全部 1=保本固定收益
															// 2=保本浮动收益
															// 3=非保本浮动收益
//		paramsmap.put(BocInvt.BOCI_PRODTIMELIMIT_REQ,
//				LocalData.bocitimeLimitMap.get(timeLimit)); // prodTimeLimit
															// 产品期限 String o
															// 0=全部 1=3个月以内
															// 2=3个月-6个月
															// 3=6个月-12个月
															// 4=12个月-24个月
															// 5=24个月以上
		paramsmap.put(BocInvt.BOCI_PRODTIMELIMIT_REQ,"0"); 
		paramsmap.put(BocInvt.BOCI_PRODUCTCURCODE_REQ, LocalData.bociCurcodeMap
				.get(productCurrCode.getSelectedItem().toString())); // productCurCode
																		// 产品币种
																		// String
																		// o
		//P603新增字段
		paramsmap.put("productKind", "0");//产品性质		0:全部1:结构性理财产品2:类基金理财产品可不上送，不送时“产品性质”默认为“1:结构性理财”
		paramsmap.put("issueType", "0");//产品类型		1：现金管理类产品2：净值开放类产品3：固定期限产品默认上送0
		paramsmap.put("dayTerm", BocInvestControl.map_prodTimeLimit.get(timeLimit));//产品期限（天数）		0：全部1：30天以内2：31-90天3：91-180天4：180天以上默认上送0
		paramsmap.put("proRisk", "0");//风险等级		0：全部1：低风险2：中低风险3：中等风险4：中高风险5：高风险默认上送0
		paramsmap.put("isLockPeriod", "1");//是否支持业绩基准产品查询     0：不支持1：支持默认上送0
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestOutlayBii(biiRequestBody, this,
				"requestFastOutlayCallBack");
	}

	/**
	 * 功能外置 登录之前的conversationId 返回
	 */
	public void requestLoginPreOutlayConversationIdCallBack(Object resultObj) {
		super.requestLoginPreOutlayConversationIdCallBack(resultObj);
		if (isFast) {
			if (!StringUtil.isNullOrEmpty(fastList)) {
				fastList.clear();
			}
	
			query_result.setVisibility(View.GONE);
			queryListView.setVisibility(View.GONE);
			fastListView.setVisibility(View.VISIBLE);
			sortBuyAmout.setCompoundDrawables(null, null, null, null);
			sortLimit.setCompoundDrawables(null, null, null, null);
			sortYearRR.setCompoundDrawables(null, null, null, null);
			sortBuyAmout.setClickable(false);
			sortLimit.setClickable(false);
			sortYearRR.setClickable(false);
			requestFastQueryOutlay();
		} else {
			if (!StringUtil.isNullOrEmpty(combinateList)) {
				combinateList.clear();
			}
			Drawable drawableUnSelect = getResources().getDrawable(
					R.drawable.bocinvt_sort_un);
			drawableUnSelect.setBounds(0, 0, iconSize, iconSize);
			sortYearRR.setCompoundDrawables(null, null, drawableUnSelect, null);
			sortBuyAmout.setCompoundDrawables(null, null, drawableUnSelect,
					null);
			sortLimit.setCompoundDrawables(null, null, drawableUnSelect, null);
		
			sortBuyAmout.setClickable(true);
			sortLimit.setClickable(true);
			sortYearRR.setClickable(true);
			
			sortFlag = "1";
			sortType = "0";
			isSort = false;
			isProgress = false;
			query_result.setVisibility(View.GONE);
			fastListView.setVisibility(View.GONE);
			queryListView.setVisibility(View.VISIBLE);
			view.findViewById(R.id.layout_fast).setVisibility(View.VISIBLE);
			view.findViewById(R.id.layout_combination).setVisibility(View.GONE);
//			setTitle(getResources().getString(R.string.product_outlay_list));		
			mEditText.setText("");
			requestcombinateQueryOutlay();
		}
	}

	/**
	 * 请求组合查询回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	// {"List":[{"applyObj":"1","autoPermit":"0","buyPrice":"1.000000","curCode":"001","impawnPermit":"1","periodical":"false","prodCode":"43-ryg-shl","prodName":"43-ryg-shl","prodRisklvl":"4","prodTimeLimit":"1827","progressionflag":"0","remainCycleCount":"","status":"1","subAmount":"0.00","yearlyRR":"0.01"},{"applyObj":"1","autoPermit":"0","buyPrice":"1.000000","curCode":"001","impawnPermit":"1","periodical":"false","prodCode":"43-RJYLDFB-SHL","prodName":"43-RJYLDFB-SHL","prodRisklvl":"0","prodTimeLimit":"1827","progressionflag":"0","remainCycleCount":"","status":"1","subAmount":"0.00","yearlyRR":"0.01"},{"applyObj":"1","autoPermit":"0","buyPrice":"1.000000","curCode":"001","impawnPermit":"1","periodical":"false","prodCode":"rjyl-u2-405-1-y","prodName":"rjyl-u2-405-1","prodRisklvl":"0","prodTimeLimit":"1827","progressionflag":"0","remainCycleCount":"","status":"1","subAmount":"5000.00","yearlyRR":"6.00"},{"applyObj":"1","autoPermit":"0","buyPrice":"1.000000","curCode":"001","impawnPermit":"1","periodical":"false","prodCode":"43-ryz-bbshl","prodName":"43-ryz-bbshl","prodRisklvl":"2","prodTimeLimit":"1827","progressionflag":"0","remainCycleCount":"","status":"1","subAmount":"0.00","yearlyRR":"0.01"},{"applyObj":"1","autoPermit":"0","buyPrice":"1.000000","curCode":"001","impawnPermit":"1","periodical":"false","prodCode":"rjyl-bbbs-001-43-1","prodName":"rjyl-bbbs-001-43-2","prodRisklvl":"1","prodTimeLimit":"1827","progressionflag":"0","remainCycleCount":"","status":"1","subAmount":"50000.00","yearlyRR":"6.00"},{"applyObj":"1","autoPermit":"0","buyPrice":"1.000000","curCode":"001","impawnPermit":"1","periodical":"false","prodCode":"rjyl-bbbs-001-43-y","prodName":"rjyl-bbbs-001-43","prodRisklvl":"1","prodTimeLimit":"1827","progressionflag":"0","remainCycleCount":"","status":"1","subAmount":"50000.00","yearlyRR":"6.00"},{"applyObj":"1","autoPermit":"0","buyPrice":"1.000000","curCode":"001","impawnPermit":"1","periodical":"false","prodCode":"T43-MG-RJYL-SHL01","prodName":"T43-MG-RJYL-SHL高","prodRisklvl":"4","prodTimeLimit":"1827","progressionflag":"0","remainCycleCount":"","status":"1","subAmount":"0.00","yearlyRR":"0.01"},{"applyObj":"1","autoPermit":"0","buyPrice":"1.000000","curCode":"001","impawnPermit":"1","periodical":"false","prodCode":"43-RY-BB-SHL","prodName":"43-RY-BB-SHL","prodRisklvl":"0","prodTimeLimit":"1827","progressionflag":"0","remainCycleCount":"","status":"1","subAmount":"0.00","yearlyRR":"0.01"},{"applyObj":"1","autoPermit":"1","buyPrice":"1.000000","curCode":"001","impawnPermit":"1","periodical":"false","prodCode":"43-RY-ZFF-SHL","prodName":"43-RY-ZFF-SHL","prodRisklvl":"2","prodTimeLimit":"1827","progressionflag":"0","remainCycleCount":"","status":"1","subAmount":"0.00","yearlyRR":"0.01"},{"applyObj":"1","autoPermit":"0","buyPrice":"1.000000","curCode":"001","impawnPermit":"1","periodical":"false","prodCode":"YZ-T43-RJYL01","prodName":"t43日积月累1","prodRisklvl":"4","prodTimeLimit":"1827","progressionflag":"0","remainCycleCount":"","status":"1","subAmount":"0.00","yearlyRR":"15.00"}],"recordNumber":"18"}
	@SuppressWarnings("unchecked")
	public void requestCombinateOutlayCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) {
			BiiHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.acc_transferquery_null));
			return;
		}
		recordNumber = Integer.valueOf((String) result
				.get(Acc.RECORDNUMBER_RES));
		combinateList.addAll((List<Map<String, Object>>) result
				.get(BocInvt.BOCI_OURLAYLIST_RES));

		BiiHttpEngine.dissMissProgressDialog();
		if (isfirst) {
			isfirst = false;
			view.findViewById(R.id.layout_fast).setVisibility(View.VISIBLE);
		}
		if (StringUtil.isNullOrEmpty(combinateList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		} 
		query_result.setVisibility(View.VISIBLE);
	
		addFooterView(recordNumber);
		loadNumber = combinateList.size();
		if (adapter == null) {
			adapter = new ProductQueryOutlayAdapter(BaseDroidApp.getInstanse()
					.getCurrentAct(), combinateList);
			queryListView.setAdapter(adapter);
			return;
		}
		adapter.setmList(combinateList);

	}

	/** 请求组合查询 更多 */
	public void requestCombinateForMore() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PRODUCTLISTQUERYOUTLAY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap()
				.get(ConstantGloble.LOGIN_OUTLAY_PRECONVERSATIONID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();

		paramsmap.put(BocInvt.BOCI_PRODUCTTYPE_REQ, "0");
		// LocalData.boci_ProductTypeMap.get(0));// productType 产品属性分类
		// String M 0：全部 1：自营产品
		// 2：代销产品

		paramsmap.put(BocInvt.BOCI_PRODUCTSTA_REQ,
				LocalData.bociStatusMap.get(status));
		paramsmap.put(BocInvt.SORTFLAG, sortFlag);
		paramsmap.put(BocInvt.SORTSITE, sortType);
		paramsmap.put(BocInvt.BOCI_PAGESIZE_REQ,
				ConstantGloble.LOAN_PAGESIZE_VALUE);
		paramsmap
				.put(BocInvt.BOCI_CURRENTINDEX_REQ, String.valueOf(loadNumber));
		paramsmap
				.put(BocInvt.BOCI_REFRESH_REQ, ConstantGloble.REFRESH_FOR_MORE);
		biiRequestBody.setParams(paramsmap);
		
		//P603新增字段
				paramsmap.put("productKind", "0");//产品性质		0:全部1:结构性理财产品2:类基金理财产品可不上送，不送时“产品性质”默认为“1:结构性理财”
				paramsmap.put("issueType", "0");//产品类型		1：现金管理类产品2：净值开放类产品3：固定期限产品默认上送0
				paramsmap.put("dayTerm", "0");//产品期限（天数）		0：全部1：30天以内2：31-90天3：91-180天4：180天以上默认上送0
				paramsmap.put("proRisk", "0");//风险等级		0：全部1：低风险2：中低风险3：中等风险4：中高风险5：高风险默认上送0
				paramsmap.put("isLockPeriod", "1");//是否支持业绩基准产品查询     0：不支持1：支持默认上送0

		BiiHttpEngine.showProgressDialog();
		HttpManager.requestOutlayBii(biiRequestBody, this,
				"requestCombinateForMoreCallBack");
	}

	/**
	 * 请求组合查询回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	@SuppressWarnings("unchecked")
	public void requestCombinateForMoreCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result))
			return;
		recordNumber = Integer.valueOf((String) result
				.get(Acc.RECORDNUMBER_RES));
		List<Map<String, Object>> combinateListForMore = (List<Map<String, Object>>) result
				.get(BocInvt.BOCI_OURLAYLIST_RES);
		if (StringUtil.isNullOrEmpty(combinateListForMore))
			return;
		combinateList.addAll(combinateListForMore);
		addFooterView(recordNumber);
		loadNumber = combinateList.size();
		adapter.setmList(combinateList);
	}

	@SuppressWarnings("unchecked")
	public void requestFastOutlayCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) {
			BiiHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
	
		if (result.containsKey(BocInvt.BOCI_OURLAYLIST_RES)) {
			List<Map<String, Object>> mList = (List<Map<String, Object>>) result
					.get(BocInvt.BOCI_OURLAYLIST_RES);
			BiiHttpEngine.dissMissProgressDialog();
			if (StringUtil.isNullOrEmpty(mList)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
				return;
			}
			fastList.addAll(mList);
		} else {
			BiiHttpEngine.dissMissProgressDialog();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.add(result);
			fastList.addAll(list);
		}
		query_result.setVisibility(View.VISIBLE);
		if (fastListView.getFooterViewsCount() > 0) {
			BiiHttpEngine.dissMissProgressDialog();
			fastListView.removeFooterView(load_more);
		}
		if (fastAdapter == null) {
			fastAdapter = new ProductQueryOutlayAdapter(BaseDroidApp
					.getInstanse().getCurrentAct(), fastList);
			// fastAdapter.setViewOnClick(progressOnclik);
			fastListView.setAdapter(fastAdapter);
			if(isEnterDetail){
				fastListView.performItemClick(fastListView.getChildAt(0), 0, fastListView.getItemIdAtPosition(0));
			}
			return;
		}
		fastAdapter.setmList(fastList);

	}

	/** 理财产品点击事件 */
	OnItemClickListener onListClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (isFast) {
				requestStatus = (String) fastList.get(position).get(
						BocInvt.BOCI_STATUS_RES);
				availableAmt = (String) fastList.get(position).get(
						BocInvt.AVAILABLEAMT);
				BociDataCenter.getInstance().setChoosemap(
						fastList.get(position));
				// 储存剩余最大可购买期数
				BaseDroidApp
						.getInstanse()
						.getBizDataMap()
						.put(ConstantGloble.BOCINVT_REMAINCYCLECOUNT_STRING,
								(String) fastList.get(position).get(
										BocInvt.BOCI_REMAINCYCLECOUNT_RES));
				// 请求详情
				issueType_value=fastList.get(position).get("issueType").toString();
				requestProductDetailOutlay((String) fastList.get(position).get(
						BocInvt.BOCI_DETAILPRODCODE_RES));

				return;
			}
			if (combinateList == null || combinateList.size() == 0) {

			} else {

				requestStatus = (String) combinateList.get(position).get(
						BocInvt.BOCI_STATUS_RES);
				availableAmt = (String) combinateList.get(position).get(
						BocInvt.AVAILABLEAMT);
				BociDataCenter.getInstance().setChoosemap(
						combinateList.get(position));
				// 储存剩余最大可购买期数
				BaseDroidApp
						.getInstanse()
						.getBizDataMap()
						.put(ConstantGloble.BOCINVT_REMAINCYCLECOUNT_STRING,
								(String) combinateList.get(position).get(
										BocInvt.BOCI_REMAINCYCLECOUNT_RES));
				// 请求详情
				issueType_value=combinateList.get(position).get("issueType").toString();
				requestProductDetailOutlay((String) combinateList.get(position)
						.get(BocInvt.BOCI_DETAILPRODCODE_RES));
			}
		}
	};

	/**
	 * 请求产品详情
	 * 
	 * @param procode
	 *            产品代码
	 */
	public void requestProductDetailOutlay(String procode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PRODUCTDETAILQUERYOUTLAY);
		// biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
		// .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCI_PRODUCTCODE_REQ, procode);
		if (issueType_value.equals("2")) {
//			1：现金管理类产品
//			2：净值开放类产品      等同于    类基金产品
//			3：固定期限产品
			paramsmap.put("productKind", "1");
		}
		biiRequestBody.setParams(paramsmap);
		// 开始通讯,展示通讯框
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestOutlayBii(biiRequestBody, this,
				"requestProductDetailCallBack");
	}

	/** 请求产品详情回调 */
	public void requestProductDetailCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> detailMap = (Map<String, Object>) biiResponseBody
				.getResult();
		// 结束通讯,隐藏通讯框
		BiiHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(detailMap)) {
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.BOCINVT_PRODUCTDETAIL_LIST, detailMap);
		// 进入详情页面
		int index;

		index = combinIndex;

		Intent intent = new Intent(QueryProductOutlayActivity.this,
				ProductDetailOutlayNewActivity.class);
		intent.putExtra(BocInvt.BOCI_XPADSTATUS_REQ, requestStatus);
		intent.putExtra(BocInvt.AVAILABLEAMT, availableAmt);
		// intent.putExtra(Comm.ACCOUNT_ID, (String)
		// BociDataCenter.getInstance()
		// .getBocinvtAcctList().get(index).get(Comm.ACCOUNT_ID));
		startActivityForResult(intent, ACTIVITY_BUY_CODE);
	}

	/** 查询结果头倒三角点击监听事件 */
	OnClickListener backQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			combinateLayout.setBackgroundResource(R.drawable.img_bg_query_no);
			// queryLayout.startAnimation(animation_down);
			queryLayout.setVisibility(View.VISIBLE);

		}
	};

	/**
	 * 更多
	 * 
	 * @param v
	 */
	public void btnMoreOnclick(View v) {

		view.findViewById(R.id.layout_fast).setVisibility(View.GONE);

		view.findViewById(R.id.layout_combination).setVisibility(View.VISIBLE);

		setTitle(getResources().getString(R.string.combinate_query_outlay));

	}

	/**
	 * 快速查询操作
	 * 
	 * @param v
	 *            sunh
	 */
	public void btnFastQueryOnclick(View v) {

		if (StringUtil.isNull(mEditText.getText().toString())) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入产品代码");
			return;
		}

		BiiHttpEngine.showProgressDialog();
		isFast = true;
		requestLoginPreOutlayConversationId();
	}

	/**
	 * 起售金额排序
	 */
	private void sortByBuyAmoutOnclick() {
		if (!sortType.equals("3")) {
			sortFlag = "1";
		}
		Drawable drawableSelect = null;
		if (sortFlag.equals("0")) {
			sortFlag = "1";
			drawableSelect = getResources().getDrawable(
					R.drawable.bocinvt_sort_down);
		} else if (sortFlag.equals("1")) {
			sortFlag = "0";
			drawableSelect = getResources().getDrawable(
					R.drawable.bocinvt_sort_up);
		}
		isSort = true;
		sortType = "3";
		Drawable drawableUnSelect = getResources().getDrawable(
				R.drawable.bocinvt_sort_un);
		drawableSelect.setBounds(0, 0, iconSize, iconSize);
		drawableUnSelect.setBounds(0, 0, iconSize, iconSize);
		sortBuyAmout.setCompoundDrawables(null, null, drawableSelect, null);
		sortLimit.setCompoundDrawables(null, null, drawableUnSelect, null);
		sortYearRR.setCompoundDrawables(null, null, drawableUnSelect, null);
		if (!StringUtil.isNullOrEmpty(combinateList)) {
			combinateList.clear();
			addFooterView(0);
		}
		BiiHttpEngine.showProgressDialog();
		requestcombinateQueryOutlay();
	}

	/**
	 * 产品期限排序
	 */
	private void sortByLimitOnclick() {
		if (!sortType.equals("1")) {
			sortFlag = "1";
		}
		Drawable drawableSelect = null;
		if (sortFlag.equals("0")) {
			sortFlag = "1";
			drawableSelect = getResources().getDrawable(
					R.drawable.bocinvt_sort_down);
		} else if (sortFlag.equals("1")) {
			sortFlag = "0";
			drawableSelect = getResources().getDrawable(
					R.drawable.bocinvt_sort_up);
		}
		isSort = true;
		sortType = "1";
		Drawable drawableUnSelect = getResources().getDrawable(
				R.drawable.bocinvt_sort_un);
		drawableSelect.setBounds(0, 0, iconSize, iconSize);
		drawableUnSelect.setBounds(0, 0, iconSize, iconSize);
		sortLimit.setCompoundDrawables(null, null, drawableSelect, null);
		sortBuyAmout.setCompoundDrawables(null, null, drawableUnSelect, null);
		sortYearRR.setCompoundDrawables(null, null, drawableUnSelect, null);
		if (!StringUtil.isNullOrEmpty(combinateList)) {
			combinateList.clear();
			addFooterView(0);
		}
		BiiHttpEngine.showProgressDialog();
		requestcombinateQueryOutlay();
	}

	/**
	 * 年化收益排序
	 */
	private void sortByYearRROnclick() {
		if (!sortType.equals("2")) {
			sortFlag = "1";
		}
		Drawable drawableSelect = null;
		if (sortFlag.equals("0")) {
			sortFlag = "1";
			drawableSelect = getResources().getDrawable(
					R.drawable.bocinvt_sort_down);
		} else if (sortFlag.equals("1")) {
			sortFlag = "0";
			drawableSelect = getResources().getDrawable(
					R.drawable.bocinvt_sort_up);
		}
		isSort = true;
		sortType = "2";
		Drawable drawableUnSelect = getResources().getDrawable(
				R.drawable.bocinvt_sort_un);
		drawableSelect.setBounds(0, 0, iconSize, iconSize);
		drawableUnSelect.setBounds(0, 0, iconSize, iconSize);
		sortYearRR.setCompoundDrawables(null, null, drawableSelect, null);
		sortBuyAmout.setCompoundDrawables(null, null, drawableUnSelect, null);
		sortLimit.setCompoundDrawables(null, null, drawableUnSelect, null);
		if (!StringUtil.isNullOrEmpty(combinateList)) {
			combinateList.clear();
			addFooterView(0);
		}
		BiiHttpEngine.showProgressDialog();
		requestcombinateQueryOutlay();
	}

	/**
	 * 添加更多按钮
	 * 
	 * @param totalCount
	 */
	private void addFooterView(int totalCount) {
		if (totalCount > combinateList.size()) {
			if (queryListView.getFooterViewsCount() <= 0) {
				queryListView.addFooterView(load_more);
			}
			queryListView.setClickable(true);
		} else {
			if (queryListView.getFooterViewsCount() > 0) {
				queryListView.removeFooterView(load_more);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sort_buystartingamount:
			sortByBuyAmoutOnclick();
			break;
		case R.id.sort_limit:
			sortByLimitOnclick();
			break;
		case R.id.sort_yearlyrr:
			sortByYearRROnclick();
			break;

		case R.id.manual_refresh:// 手动刷新
			BiiHttpEngine.showProgressDialog();
			requestLoginPreConversationId();
			break;

		}
	}

}
