
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
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
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.SecondMainActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.adapter.ProductQueryAdapter;
import com.chinamworld.bocmbci.biz.bocinvt.dialogActivity.BocBindinglDialogActivity;
import com.chinamworld.bocmbci.biz.bocinvt.inflateView.InflaterViewDialog;
import com.chinamworld.bocmbci.biz.bocinvt.myproduct.ProgressInfoActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeActivity;
import com.chinamworld.bocmbci.biz.plps.adapter.AnnuitySpinnerAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 产品查询页面
 * 
 * @author wangmengmeng
 * 
 */
public class QueryProductActivity extends BociBaseActivity implements OnClickListener {
	private final int SPCOMBINACCT_LISTVIEW_ID = 1001;
//	private final int SPFASTACCT_LISTVIEW_ID = 1002;
	private int iconSize;
	/** 查询信息页 */
	private View view;
	/** 组合查询 */
	private LinearLayout combinateLayout;
	// 下拉列表
	/** 产品币种 */
	private Spinner productCurrCode;
//	private String type;
//	private String typename;
	private TextView spCombinAcct;
	/** 产品风险类型 */
	private Spinner productRiskType;
	private String riskType;
	/** 产品期限 */
	private Spinner prodTimeLimit;
	private String timeLimit;
	/** 产品销售状态 */
	private Spinner xpadStatus;
	private String status;
	/** 排序条件  **/
//	private Spinner sortType;
	// 按钮
	/** 组合查询 */
	private Button combinateQueryproduct/*,mBtnAcctQuery*/;
	private RelativeLayout query_btn_combinate;
	// 查询结果页面
//	private LinearLayout query_result;
	/** 查询列表 */
	private ListView queryListView,fastListView;
	// 查询结果头
	/** 组合查询头 */
	private LinearLayout header_for_combinate;
	private TextView boci_acct,boci_acct_fast;
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
//	private List<Map<String, String>> productTypeList;
	/** 组合查询结果 */
	private List<Map<String, Object>> combinateList = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> fastList = new ArrayList<Map<String,Object>>();
	// 购买弹出框
	/** 是否开通投资理财 */
	private boolean isOpen;
	/** 是否有风险评估经验 */
//	private boolean isevaluatedBefore;
	/** 登记账户信息 */
	private List<Map<String, Object>> investBindingInfo;
	/** 任务弹出框视图 */
	private RelativeLayout dialogView;
	/** 收起 */
	private LinearLayout img_up;
	/** conid为1是组合查询 */
	private int conid = 0;
	/** 假数据 */
	private boolean isfirst = true;
//	private RelativeLayout rl_query_transfer;
	/** 进入即进行查询 */
//	private boolean first = true;
	private RelativeLayout load_more;
	private Button btn_load_more;
	private int recordNumber = 0;
	private int loadNumber = 0;
	private ProductQueryAdapter adapter;
	private ProductQueryAdapter fastAdapter;
	private boolean isProgress;
	private int combinIndex;
	private EditText mEditText;
	private String recentAccount;
	private LinearLayout queryLayout;
	private String requestStatus;
	private String availableAmt;
	private boolean isFast = false;
	private boolean isSort = false;
	private String sortFlag = "0";
	private String sortType = "0";
	/** 排序  **/
	private TextView sortBuyAmout,sortLimit,sortYearRR;
	private LinearLayout layoutAcctFast,layoutAcctCombin;
	/**产品风险级别-对应的中文*/
	private String proRisk_key;
	/**产品类型-对应的中文*/
	private String issueType_key;
	private boolean first_query_for_activity=true;

	/**产品类型、产品代码-从别的页面传过来的值*/
	private String issueTypeFromOther,productCode;
	/** 是否直接进入详情页面*/
	private boolean isEnterDetail = false;
	/** 按产品代码查询按钮*/
	private RadioButton btn_cardbank;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iconSize = getResources().getDimensionPixelSize(R.dimen.dp_three_zero)/2;
		// 为界面标题赋值
		setTitle(this.getResources().getString(R.string.boci_product_titlelist));
		// 右上角按钮赋值
		gonerightBtn();
//		setText(this.getResources().getString(R.string.boci_fast_trans));
		setLeftSelectedPosition("bocinvtManager_3");
		conid = 1;
		setBackBtnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent(QueryProductActivity.this,SecondMainActivity.class);
//				startActivity(intent);
				goToMainActivity();
			}
		});
		// 请求是否开通投资理财
		requestPsnInvestmentManageIsOpen(0);
	}


	/** 初始化界面 */
	private void init() {
		animation_up = AnimationUtils.loadAnimation(this, R.anim.scale_out);
		animation_down = AnimationUtils.loadAnimation(this, R.anim.scale_in);
		// 添加布局
		view = addView(R.layout.bocinvt_queryproduct_activity);
		
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}
		// 右上角按钮点击事件
//		setRightBtnClick(rightBtnClick);

//		rl_query_transfer = (RelativeLayout) LayoutInflater.from(this).inflate(
//				R.layout.bocinvt_queryproduct_condition, null);
//		PopupWindow p = PopupWindowUtils.getInstance().getQueryPopupWindow(rl_query_transfer,
//				QueryProductActivity.this);
//		p.setFocusable(true);
//		p.setBackgroundDrawable(null);
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		btn_cardbank = (RadioButton) findViewById(R.id.btn_cardbank);
		spinner_product_type = (Spinner) findViewById(R.id.spinner_product_type);
		spinner_danager_grade = (Spinner) findViewById(R.id.spinner_danager_grade);
		
		queryLayout = (LinearLayout) view.findViewById(R.id.ll_query_condition);
		query_result_header = findViewById(R.id.query_result_header);
		sort_layout = findViewById(R.id.sort_layout);
		combinateLayout = (LinearLayout) view.findViewById(R.id.ll_combinate_queryproduct);
		spCombinAcct = (TextView) view.findViewById(R.id.boci_combinacct);//账户选择框  tv
//		spFastAcct = (TextView) view
//				.findViewById(R.id.boci_fastacct);
		productCurrCode = (Spinner) view.findViewById(R.id.boci_currencode);//币种
		productRiskType = (Spinner) view.findViewById(R.id.boci_productRiskType_query);//风险类型
		prodTimeLimit = (Spinner) view.findViewById(R.id.boci_prodTimeLimit_query);//期限
		xpadStatus = (Spinner) view.findViewById(R.id.boci_xpadStatus_query);//销售状态
		img_up = (LinearLayout) view.findViewById(R.id.bocinvt_query_up);//收起按钮
		mEditText = (EditText) view.findViewById(R.id.editView);//快速查询 -产品代码输入框
		//界面内容布局
		sortBuyAmout = (TextView) view.findViewById(R.id.sort_buystartingamount);//起售金额
		sortLimit = (TextView) view.findViewById(R.id.sort_limit);//产品期限
		sortYearRR = (TextView) view.findViewById(R.id.sort_yearlyrr);//年化收益率
		sortBuyAmout.setOnClickListener(this);
		sortLimit.setOnClickListener(this);
		sortYearRR.setOnClickListener(this);
		spCombinAcct.setOnClickListener(acctOnClick);
//		spFastAcct.setOnClickListener(acctOnClick);
		initSpAcct();
		// PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
		// boci_product_name);
		// PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
		// boci_yearlyRR);
		// PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
		// boci_timeLimit);
		// 按钮初始化
		combinateQueryproduct = (Button) view
				.findViewById(R.id.btn_combinate_queryproduct);
		query_btn_combinate = (RelativeLayout) view
				.findViewById(R.id.query_btn);//查询按钮所在布局
		query_btn_combinate.setOnClickListener(combinateQueryClick);
		combinateQueryproduct.setOnClickListener(combinateQueryClick);
		queryLayout.setVisibility(View.VISIBLE);
		query_result_header.setVisibility(View.GONE);
		sort_layout.setVisibility(View.GONE);//++++++++++
		clickSpinner();
		Intent intent = getIntent();
		if (StringUtil.isNullOrEmpty(intent)) {
			return;
		}else{
			issueTypeFromOther = intent.getStringExtra("issueType");
			isEnterDetail = intent.getBooleanExtra("isEnterDetail", false);
			productCode = intent.getStringExtra("productCode");
		}
		if(isEnterDetail){
			mEditText.setText(productCode);
		}
		initfirst();
	}
	private void initSpAcct(){
		if (!StringUtil.isNull(recentAccount)) {//recentAccount最近交易的账户
			boolean tem_bo=false;
			List<Map<String,Object>> list = BociDataCenter.getInstance().getBocinvtAcctList();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get(BocInvt.ACCOUNTNO).equals(recentAccount)) {
					spCombinAcct.setText(StringUtil.getForSixForString(recentAccount));
//					spFastAcct.setText(StringUtil.getForSixForString(recentAccount));
					combinIndex = i;
//					fastIndex = i;
					tem_bo=true;
					break;
				}
			}
			if (!tem_bo) {
				spCombinAcct.setText(StringUtil.getForSixForString(list.get(combinIndex).get(BocInvt.ACCOUNTNO).toString()));//P603新规则，账户默认选中第一个
			}
		}else{
			//原代码
//			if (BociDataCenter.getInstance().getBocinvtAcctList().size() == 1) {
//				spCombinAcct.setText(BocinvtUtils.initSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO).get(combinIndex));
//				spFastAcct.setText(BocinvtUtils.initSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO).get(fastIndex));
//			}else {
//				spCombinAcct.setText("请选择账户");
//				spFastAcct.setText("请选择账户");
//			}
			//P603新规则，账户默认选中第一个
			//新代码
			if (BociDataCenter.getInstance().getBocinvtAcctList().size()!=0) {
				spCombinAcct.setText(StringUtil.getForSixForString(BociDataCenter.getInstance().getBocinvtAcctList().get(combinIndex).get(BocInvt.ACCOUNTNO).toString()));
			}
		}
		saveAccDate(BociDataCenter.getInstance().getBocinvtAcctList().get(combinIndex));//存储用户选择的交易账户信息
	}

	public void initfirst() {
//		query_result = (LinearLayout) view.findViewById(R.id.ll_query_result);
		// 查询结果页面初始化
		if(StringUtil.isNullOrEmpty(issueTypeFromOther)){
			issueType_key = BocInvestControl.list_issueType.get(0);
		}else{
			issueType_key = issueTypeFromOther;
			spinner_product_type.setSelection(Integer.parseInt(BocInvestControl.map_issueType.get(issueType_key).toString()));
		}
		proRisk_key=BocInvestControl.list_proRisk.get(0);
		layoutAcctFast = (LinearLayout) view.findViewById(R.id.acct_layout_fast);
		layoutAcctCombin = (LinearLayout) view.findViewById(R.id.acct_layout_combin);
		boci_acct = (TextView) view.findViewById(R.id.bocinvt_acct_value);
		boci_acct_fast = (TextView) view.findViewById(R.id.bocinvt_acct_value_fast);
		boci_producttype = (TextView) view
				.findViewById(R.id.productCurCode);
		boci_riskType = (TextView) view
				.findViewById(R.id.tv_boci_riskType_value);
		boci_timeLimit = (TextView) view
				.findViewById(R.id.tv_boci_timeLimit_value);
		boci_status = (TextView) view.findViewById(R.id.tv_boci_status_value);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
//				boci_producttype);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				boci_riskType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				boci_timeLimit);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				boci_status);
		queryListView = (ListView) view.findViewById(R.id.boci_query_list);
		fastListView = (ListView) view.findViewById(R.id.fast_query_list);
		queryListView.setFocusable(true);
		queryListView.setOnItemClickListener(onListClickListener);
		fastListView.setFocusable(true);
		fastListView.setOnItemClickListener(onListClickListener);
		if(isEnterDetail){
			btn_cardbank.setChecked(true);
			view.findViewById(R.id.layout_fast).setVisibility(View.VISIBLE);
			view.findViewById(R.id.layout_combination).setVisibility(View.GONE);
			view.findViewById(R.id.layout_riskType).setVisibility(View.GONE);
			view.findViewById(R.id.layout_status).setVisibility(View.GONE);
			sortBuyAmout.setCompoundDrawables(null, null, null, null);
			sortLimit.setCompoundDrawables(null, null, null, null);
			sortYearRR.setCompoundDrawables(null, null, null, null);
			sortBuyAmout.setClickable(false);
			sortLimit.setClickable(false);
			sortYearRR.setClickable(false);
			sort_layout.setVisibility(View.GONE);//++++++++++
			if (view.findViewById(R.id.layout_combination).getVisibility() == View.GONE) {
				isFast = true;
				fastList.clear();
				queryListView.setVisibility(View.GONE);
				fastListView.setVisibility(View.VISIBLE); 
			}
			}
//		final List<String> typeNameList = new ArrayList<String>();
//		final List<String> typeIdList = new ArrayList<String>();
//		for (int i = 0; i < productTypeList.size(); i++) {
//			typeNameList.add(productTypeList.get(i).get(
//					String.valueOf(BocInvt.TYPE_NAME_RES)));
//			typeIdList.add(productTypeList.get(i).get(
//					String.valueOf(BocInvt.TYPE_BRANDID_RES)));
//		}
//		for (int j = 0; j < typeNameList.size(); j++) {
//			String name = typeNameList.get(j);
//			if (!StringUtil.isNull(name)
//					&& name.equals(ConstantGloble.BOCINVT_RMB)) {
//				type = typeIdList.get(j);
//				typename = typeNameList.get(j);
//				break;
//			}
//		}
		riskType = LocalData.bocinvtProductRiskType.get(0);
		timeLimit = BocInvestControl.list_prodTimeLimit.get(0);
		status = LocalData.bocinvtXpadStatus.get(1);
		if (isFast) {
			layoutAcctCombin.setVisibility(View.GONE);
			layoutAcctFast.setVisibility(View.VISIBLE);
			boci_acct_fast.setText(spCombinAcct.getText().toString());
		}else{
			layoutAcctCombin.setVisibility(View.VISIBLE);
			layoutAcctFast.setVisibility(View.GONE);
			boci_acct.setText(spCombinAcct.getText().toString());
		}
		boci_producttype.setText(productCurrCode.getSelectedItem().toString());
		boci_riskType.setText(riskType);
		boci_status.setText(status);
		boci_timeLimit.setText(timeLimit);
		// 查询结果头返回
		ImageView boci_query_back = (ImageView) view
				.findViewById(R.id.img_boci_query_back);
		boci_query_back.setOnClickListener(backQueryClick);
		header_for_combinate = (LinearLayout) view
				.findViewById(R.id.header_for_combinate);
		header_for_combinate.setOnClickListener(backQueryClick);
		// 收起事件监听
		img_up.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断是否已经进行了查询—如果已经进行:收起所有;否则收起查询条件区域
				if (isFast) {
					if (!StringUtil.isNullOrEmpty(fastList)) {
						queryLayout.setVisibility(View.GONE);
						query_result_header.setVisibility(View.VISIBLE);
						sort_layout.setVisibility(View.VISIBLE);//++++++++++
//						query_result.setVisibility(View.VISIBLE);
					}
				}else{
					if (!StringUtil.isNullOrEmpty(combinateList)) {
						queryLayout.setVisibility(View.GONE);
						query_result_header.setVisibility(View.VISIBLE);
						sort_layout.setVisibility(View.VISIBLE);//++++++++++
//						query_result.setVisibility(View.VISIBLE);
					}
				}
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
		if (first_query_for_activity) {
			startCommQuery();
		}
//		getShowHeight();
	}

	/** Spinner点击事件 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void clickSpinner() {
		// 产品种类
//		final List<String> typeNameList = new ArrayList<String>();
//		final List<String> typeIdList = new ArrayList<String>();
//		for (int i = 0; i < productTypeList.size(); i++) {
//			typeNameList.add(productTypeList.get(i).get(
//					String.valueOf(BocInvt.TYPE_NAME_RES)));
//			typeIdList.add(productTypeList.get(i).get(
//					String.valueOf(BocInvt.TYPE_BRANDID_RES)));
//		}
		// 下拉列表操作
		// 产品种类
		ArrayAdapter<ArrayList<String>> adapter1 = new ArrayAdapter(this,
				R.layout.custom_spinner_item, LocalData.bocinvtProductCurCode);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		productCurrCode.setAdapter(adapter1);
		productCurrCode.setSelection(1);
//		productType.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				type = typeIdList.get(position);
//				typename = typeNameList.get(position);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				type = typeIdList.get(0);
//				typename = typeNameList.get(0);
//			}
//		});
//		for (int j = 0; j < typeNameList.size(); j++) {
//			String name = typeNameList.get(j);
//			if (!StringUtil.isNull(name)
//					&& name.equals(ConstantGloble.BOCINVT_RMB)) {
//				productType.setSelection(j);
//				break;
//			}
//		}
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
//				riskType = LocalData.bocinvtProductRiskType.get(0);
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
//				timeLimit = BocInvestControl.list_prodTimeLimit.get(0);
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
//				status = LocalData.bocinvtXpadStatus.get(0);
			}
		});
		xpadStatus.setSelection(1);
		
		// 产品类型
		ArrayAdapter<ArrayList<String>> adapter6 = new ArrayAdapter(this,
				R.layout.custom_spinner_item, BocInvestControl.list_issueType);
		adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_product_type.setAdapter(adapter6);
		spinner_product_type.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				issueType_key = BocInvestControl.list_issueType.get(position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
//				issueType_key = BocInvestControl.list_issueType.get(0);
			}
		});
		spinner_product_type.setSelection(0);
//		产品风险级别
		ArrayAdapter<ArrayList<String>> adapter7 = new ArrayAdapter(this,
				R.layout.custom_spinner_item, BocInvestControl.list_proRisk);
		adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_danager_grade.setAdapter(adapter7);
		spinner_danager_grade.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				proRisk_key=BocInvestControl.list_proRisk.get(position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
//				proRisk_key=BocInvestControl.list_proRisk.get(0);
			}
		});
		spinner_danager_grade.setSelection(0);
		
	}

	/** 账户选择点击事件 */
	OnClickListener acctOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.boci_combinacct:
				createDialog(PublicTools.getSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO), "理财交易账户",combinIndex,SPCOMBINACCT_LISTVIEW_ID);
				break;
//			case R.id.boci_fastacct:
//				createDialog(BocinvtUtils.initSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO), "理财交易账户",fastIndex,SPFASTACCT_LISTVIEW_ID);
//				break;
			}
		}
	};
	
	/**
	 * 创建spinner弹窗框
	 * @param list
	 * @param title
	 * @param position
	 */
	private void createDialog(List<String> list,String title,int position,int vId){
		ListView mListView = new ListView(this);
		mListView.setId(vId);
		mListView.setFadingEdgeLength(0);
		mListView.setOnItemClickListener(acctItemOnClick);
		mListView.setScrollingCacheEnabled(false);
		AnnuitySpinnerAdapter mAdapter = new AnnuitySpinnerAdapter(this, list,position);
		mListView.setAdapter(mAdapter);
		BaseDroidApp.getInstanse().showSpinnerDialog(mListView, title);
	}
	
	private OnItemClickListener acctItemOnClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (parent.getId() == SPCOMBINACCT_LISTVIEW_ID) {
				combinIndex = position;
				BaseDroidApp.getInstanse().dismissMessageDialog();
				spCombinAcct.setText(PublicTools.getSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO).get(position));
				saveAccDate(BociDataCenter.getInstance().getBocinvtAcctList().get(position));//存储用户选择的账户信息
//			}else if(parent.getId() == SPFASTACCT_LISTVIEW_ID){
//				fastIndex = position;
//				BaseDroidApp.getInstanse().dismissMessageDialog();
//				spFastAcct.setText(BocinvtUtils.initSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO).get(position));
			}
		}
	};
	// 组合查询点击事件
	OnClickListener combinateQueryClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			startCommQuery();
		}
	};
	
	private void startCommQuery(){
		if (spCombinAcct.getText().toString().equals("请选择账户")) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择账户"); return;
		}
		if (!StringUtil.isNullOrEmpty(combinateList)) {
			combinateList.clear();
		}
		Drawable drawableUnSelect = getResources().getDrawable(R.drawable.bocinvt_sort_un);
		drawableUnSelect.setBounds(0, 0, iconSize, iconSize);
		sortYearRR.setCompoundDrawables(null, null, drawableUnSelect, null);
		sortBuyAmout.setCompoundDrawables(null, null, drawableUnSelect, null);
		sortLimit.setCompoundDrawables(null, null, drawableUnSelect, null);
		combinateLayout.setBackgroundResource(R.drawable.img_bg_query_j);
//		query_result.setVisibility(View.GONE);
		sortFlag = "0";
		sortType = "0";
		isSort = false;
		conid = 1;
		isProgress = false;
		queryListView.setVisibility(View.VISIBLE);
		if (!first_query_for_activity) {
			BaseHttpEngine.showProgressDialog();
		}
		requestCommConversationId();

	
	}

	/**
	 * 判断是否开通投资理财服务
	 */
	private void requestPsnInvestmentManageIsOpen(int i) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		if (i == 0) {
			BaseHttpEngine.showProgressDialogCanGoBack();
		} else {
			BaseHttpEngine.showProgressDialog();
		}

		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInvestmentManageIsOpenCallback");
	}

	/**
	 * 判断是否开通投资理财服务---回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnInvestmentManageIsOpenCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String isopen = String.valueOf(biiResponseBody.getResult());
		if (StringUtil.isNull(isopen)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		boolean isOpenOr = Boolean.valueOf(isopen);
		// isOpen
		if (isOpenOr) {
			isOpen = true;
		} else {
			isOpen = false;
		}
		// 请求风险评估
//		requestInvtEvaluation();//P603开始一、二级菜单风险评估去掉，往下级操作迁移，例如：购买、投资协议申请、继续购买等处增加风险评估
		requestBociAcctList("1","1");
	}

	/**
	 * 请求是否进行过风险评估
	 */
//	public void requestInvtEvaluation() {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(BocInvt.PSNINVTEVALUATIONINIT_API);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//		HttpManager.requestBii(biiRequestBody, this,
//				"requestInvtEvaluationCallback");
//	}

	/**
	 * 请求是否进行过风险评估---回调
	 * 
	 * @param resultObj
	 */
//	@SuppressWarnings("unchecked")
//	public void requestInvtEvaluationCallback(Object resultObj) {
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		// 得到response
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		Map<String, Object> responseMap = (Map<String, Object>) biiResponseBody
//				.getResult();
//		if (StringUtil.isNullOrEmpty(responseMap)) {
//			BiiHttpEngine.dissMissProgressDialog();
//			return;
//		}
//		BaseDroidApp.getInstanse().getBizDataMap()
//				.put(ConstantGloble.BOICNVT_ISBEFORE_RESULT, responseMap);
//		String status = (String) responseMap.get(BocInvt.BOCIEVA_STATUS_RES);
//		if (!StringUtil.isNull(status)
//				&& status.equals(ConstantGloble.BOCINVT_EVA_SUC_STATUS)) {
//			isevaluatedBefore = true;
//		} else {
//			isevaluatedBefore = false;
//		}
//		requestBociAcctList("1","1");
//	}

	/**
	 * 请求查询登记账户---回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void bocinvtAcctCallback(Object resultObj) {
		super.bocinvtAcctCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {

		} else {
			// 得到result
			Map<String, Object> map = HttpTools.getResponseResult(resultObj);
			investBindingInfo = (List<Map<String, Object>>) map.get(BocInvt.BOCI_LIST_RES);;

		}
		BaseDroidApp
				.getInstanse()
				.getBizDataMap()
				.put(ConstantGloble.BOCINVT_XPADRESET_CHOOSE, investBindingInfo);
		if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
				/*&& isevaluatedBefore*/) {

			if (conid == 1) {
				if (isfirst) {
					isfirst = false;
					// 请求组合查询产品种类
					requestRecentAccount();
				} else {
					isProgress = false;
					requestCommConversationId();
				}
			} else {
				BaseHttpEngine.dissMissProgressDialog();
				// 快速交易
				BaseDroidApp
						.getInstanse()
						.getBizDataMap()
						.put(ConstantGloble.BOCINVT_XPADRESET_CHOOSE,
								investBindingInfo);
				Intent intent = new Intent(QueryProductActivity.this,
						BuyProductChooseActivity.class);
				intent.putExtra(ConstantGloble.BOICNVT_ISDETAIL_BUY, true);
				startActivity(intent);
			}
		} else {
			// 得到result
			BaseHttpEngine.dissMissProgressDialog();
			InflaterViewDialog inflater = new InflaterViewDialog(QueryProductActivity.this);
//			dialogView = (RelativeLayout) inflater.judgeViewDialog(isOpen,
//					investBindingInfo, isevaluatedBefore, manageOpenClick,
//					invtBindingClick, invtEvaluationClick, exitDialogClick);
			dialogView=(RelativeLayout)inflater.judgeViewDialog_choice(isOpen,
					investBindingInfo, false, manageOpenClick,
					invtBindingClick, null, exitDialogClick);
			TextView tv = (TextView) dialogView.findViewById(R.id.tv_acc_account_accountState);
			if (conid != 0) {
				// 查询
				tv.setText(this.getString(R.string.bocinvt_tv_76));
			} else {
				// 购买
				tv.setText(this.getString(R.string.bocinvt_tv_76));
			}
			BaseDroidApp.getInstanse().showAccountMessageDialog(dialogView);

		}
	}

	/**
	 * 请求登记资金账户列表信息
	 */
	public void requestXpadReset() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PSNXPADRESET_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		// 通讯开始,展示通讯框
		BiiHttpEngine.showProgressDialog();
		HttpManager
				.requestBii(biiRequestBody, this, "requestXpadResetCallback");
	}

	/**
	 * 请求登记资金账户列表信息---回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestXpadResetCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,隐藏通讯框
		BiiHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			// 没有可登记账户
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.bocinvt_binding_relevance));
		} else {
			List<Map<String, Object>> responseList = (List<Map<String, Object>>) biiResponseBody
					.getResult();
			// 有可登记账户
			BaseDroidApp.getInstanse().getBizDataMap()
					.put(ConstantGloble.BOCINVT_XPADRESET_LIST, responseList);
			Intent intent = new Intent(this, InvtBindingChooseActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE);
			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
		}

	}

	/** 开通投资理财监听事件 */
	OnClickListener manageOpenClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 跳转到投资理财服务协议页面
			Intent gotoIntent = new Intent(QueryProductActivity.this,
					InvesAgreeActivity.class);
			startActivityForResult(gotoIntent,
					ConstantGloble.ACTIVITY_RESULT_CODE);
			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
		}
	};
	/** 登记账户监听事件 */
	OnClickListener invtBindingClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 请求登记资金账户信息
			requestXpadReset();
		}
	};
	/** 风险评估监听事件 */
	OnClickListener invtEvaluationClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(QueryProductActivity.this,
					InvtEvaluationInputActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE);
			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
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
						queryLayout.setVisibility(View.GONE);
						query_result_header.setVisibility(View.VISIBLE);
						sort_layout.setVisibility(View.VISIBLE);//++++++++++
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						combinateLayout.setBackgroundResource(R.drawable.img_bg_query_no);
						queryLayout.setVisibility(View.VISIBLE);
						query_result_header.setVisibility(View.GONE);
						sort_layout.setVisibility(View.GONE);//++++++++++
					}
				});
			break;
		case RESULT_OK:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				// 开通成功的响应
				isOpen = true;
			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE) {
				// 登记账户成功的响应
				investBindingInfo = BociDataCenter.getInstance().getUnSetAcctList();
			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE) {
				// 风险评估成功的响应
				if (BociDataCenter.getInstance().getI() == 1) {

				} else {
//					isevaluatedBefore = true;
				}
			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE) {
				requestPsnInvestmentManageIsOpen(0);
				return;
			}
			if (requestCode != ACTIVITY_BUY_CODE) {
				if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
						/*&& isevaluatedBefore*/) {
					isfirst = false;
					requestRecentAccount();
					BaseHttpEngine.showProgressDialogCanGoBack();
				} else {
					InflaterViewDialog inflater = new InflaterViewDialog(
							QueryProductActivity.this);
//					dialogView = (RelativeLayout) inflater.judgeViewDialog(
//							isOpen, investBindingInfo, isevaluatedBefore,
//							manageOpenClick, invtBindingClick,
//							invtEvaluationClick, exitDialogClick);
					dialogView = (RelativeLayout) inflater.judgeViewDialog_choice(isOpen,
							investBindingInfo, true, manageOpenClick, invtBindingClick, null, exitDialogClick);
					TextView tv = (TextView) dialogView.findViewById(R.id.tv_acc_account_accountState);
					if (conid != 0) {
						// 查询
						tv.setText(this.getString(R.string.bocinvt_tv_76));
					} else {
						// 购买
						tv.setText(this.getString(R.string.bocinvt_tv_76));
					}
					BaseDroidApp.getInstanse().showAccountMessageDialog(
							dialogView);
				}
			} else if (requestCode == ACTIVITY_BUY_CODE) {
				isfirst = false;
				isProgress = false;
				conid = 1;
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}

			break;
		case RESULT_CANCELED:
			if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
					/*&& isevaluatedBefore*/) {
			} else {
				InflaterViewDialog inflater = new InflaterViewDialog(
						QueryProductActivity.this);
//				dialogView = (RelativeLayout) inflater.judgeViewDialog(isOpen,
//						investBindingInfo, isevaluatedBefore, manageOpenClick,
//						invtBindingClick, invtEvaluationClick, exitDialogClick);
				dialogView = (RelativeLayout) inflater.judgeViewDialog_choice(isOpen,
						investBindingInfo, true, manageOpenClick, invtBindingClick, null, exitDialogClick);
				TextView tv = (TextView) dialogView.findViewById(R.id.tv_acc_account_accountState);
				if (conid != 0) {
					// 查询
					tv.setText(this.getString(R.string.bocinvt_tv_76/*bocinvt_query_title*/));
				} else {
					// 购买
					tv.setText(this.getString(R.string.bocinvt_tv_76/*bocinvt_task_title*/));
				}
				BaseDroidApp.getInstanse().showAccountMessageDialog(dialogView);

			}
			break;
		}
	}

	protected OnClickListener exitDialogClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
//			Intent intent = new Intent(QueryProductActivity.this,
//					SecondMainActivity.class);
//			startActivity(intent);
//			ActivityTaskManager.getInstance().removeAllSecondActivity();
			goToMainActivity();
//		 	finish();
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (isProgress) {
			int index;
//			if (isFast) {
//				index = fastIndex;
//			}else {
				index = combinIndex;
//			}
			requestProgress((String)BociDataCenter.getInstance().getBocinvtAcctList()
					.get(index).get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES),
					(String) combinateList.get(position).get(
							BocInvt.BOCI_DETAILPRODCODE_RES), "0", true);
		} else if(isFast){
			if (!StringUtil.isNullOrEmpty(fastList)) {
				fastList.clear();
			}
			requestFastQuery();
		}else{
			if (!StringUtil.isNullOrEmpty(combinateList)) {
				combinateList.clear();
			}
			requestCombinate();
		}
	}
	
	/**
	 * 请求最近操作理财账户
	 */
	public void requestRecentAccount() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.RECENTACCOUNTQUERY);
		HttpManager.requestBii(biiRequestBody, this,
				"requestRecentAccountCallBack");
	}

	public void requestRecentAccountCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (!StringUtil.isNullOrEmpty(result)) {
			recentAccount = (String) result.get(BocInvt.ACCOUNTNO);
		}
		init();
		if (isfirst) {
			// 组合查询
			isProgress = false;
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
		} else {
			if (!first_query_for_activity) {
				BiiHttpEngine.dissMissProgressDialog();
			}else {
				first_query_for_activity=false;
			}
		}


	}

//	/** 请求产品种类信息 */
//	public void requestProductType() {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(BocInvt.BOCI_QUERYANDBUY_INIT_API);
//		HttpManager.requestBii(biiRequestBody, this,
//				"requestProductTypeCallBack");
//	}

	/**
	 * 请求产品种类信息回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
//	public void requestProductTypeCallBack(Object resultObj) {
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		// 产品种类
//		productTypeList = (List<Map<String, String>>) (biiResponseBody
//				.getResult());
//		if (productTypeList == null || productTypeList.size() == 0) {
//			BiiHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					this.getString(R.string.bocinvt_error_pro));
//			return;
//		}
//		init();
//		if (isfirst) {
//			// 组合查询
//			isProgress = false;
//			requestCommConversationId();
//			BaseHttpEngine.showProgressDialog();
//		} else {
//			BiiHttpEngine.dissMissProgressDialog();
//		}
//
//	}
	
	/**
	 * 存储用户选择的账户信息
	 */
	private void saveAccDate(Map<String , Object> map){
		BocInvestControl.accound_map.clear();
		BocInvestControl.accound_map.putAll(map);
	}

	/** 请求组合查询 */
	public void requestCombinate() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PRODUCTLISTQUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCI_ACCOUNTID_REQ,BociDataCenter.getInstance().getBocinvtAcctList().get(combinIndex).get(BocInvt.BOCIBINDING_ACCOUNTIDF_RES));
		paramsmap.put(BocInvt.BOCI_PRODUCTRISKTYPE_REQ,LocalData.bociRiskTypeMap.get(riskType));//即收益类型
		paramsmap.put(BocInvt.BOCI_PRODUCTCURCODE_REQ, LocalData.bociCurcodeMap.get(productCurrCode.getSelectedItem().toString()));
		paramsmap.put(BocInvt.BOCI_XPADSTATUS_REQ,LocalData.bociStatusMap.get(status));//P603开始只支持在售产品，因此该字段的初始值status不要变动
//		paramsmap.put(BocInvt.BOCI_PRODTIMELIMIT_REQ,LocalData.bocitimeLimitMap.get(timeLimit));
		paramsmap.put(BocInvt.BOCI_PRODTIMELIMIT_REQ,"0");//p603之前字段0：全部、1：3个月以内、2：3个月-6个月、3：6个月-12个月、4：12个月-24个月、5：24个月以上默认为0全部
		paramsmap.put("isLockPeriod","1");//是否支持业绩基准产品查询,0：不支持1：支持
		paramsmap.put(BocInvt.SORTFLAG,sortFlag);
		paramsmap.put(BocInvt.SORTTYPE,sortType);
		paramsmap.put("productKind","0");		//产品性质,0:全部1:结构性理财产品2:类基金理财产品,默认值为0
		paramsmap.put(BocInvestControl.ISSUETYPE, BocInvestControl.map_issueType.get(issueType_key).toString());		//产品类型,1：现金管理类产品2：净值开放类产品3：固定期限产品 默认上送0
		paramsmap.put("dayTerm",BocInvestControl.map_prodTimeLimit.get(timeLimit));			//产品期限,0：全部  1：30天以内    2：31-90天   3：91-180天   4：180天以上 默认上送0
		paramsmap.put(BocInvestControl.PRORISK, BocInvestControl.map_proRisk.get(proRisk_key));			//风险等级,0：全部1：低风险2：中低风险3：中等风险4：中高风险5：高风险  默认上送0
		paramsmap.put(BocInvt.BOCI_PAGESIZE_REQ,ConstantGloble.LOAN_PAGESIZE_VALUE);
		paramsmap.put(BocInvt.BOCI_CURRENTINDEX_REQ,ConstantGloble.LOAN_CURRENTINDEX_VALUE);
		paramsmap.put(BocInvt.BOCI_REFRESH_REQ,ConstantGloble.LOAN_REFRESH_FALSE);
		
//		paramsmap.put(BocInvt.BOCI_PRODUCTTYPE_REQ, type);
		
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestCombinateCallBack");
	}

	/**
	 * 请求组合查询回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	@SuppressWarnings("unchecked")
	public void requestCombinateCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(result)){
			BiiHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
				getString(R.string.acc_transferquery_null)); return;
		}
		recordNumber = Integer.valueOf((String) result.get(Acc.RECORDNUMBER_RES));
		combinateList.addAll((List<Map<String, Object>>) result.get(BocInvt.BOCI_LIST_RES));
		if (!isSort) {
			pSNGetTokenId();return;
		}
		BiiHttpEngine.dissMissProgressDialog();
		queryLayout.setVisibility(View.GONE);
		query_result_header.setVisibility(View.VISIBLE);
		sort_layout.setVisibility(View.VISIBLE);//++++++++++
//		query_result.setVisibility(View.VISIBLE);
		addFooterView(recordNumber);
		loadNumber = combinateList.size();
		boci_riskType.setText(riskType);
		boci_status.setText(status);
		boci_timeLimit.setText(timeLimit);
		layoutAcctCombin.setVisibility(View.VISIBLE);
		layoutAcctFast.setVisibility(View.GONE);
		boci_acct.setText(spCombinAcct.getText().toString());
		boci_producttype.setText(productCurrCode.getSelectedItem().toString());
		if (adapter ==  null) {
			adapter = new ProductQueryAdapter(BaseDroidApp.getInstanse()
					.getCurrentAct(), combinateList);
			adapter.setViewOnClick(progressOnclik);
			queryListView.setAdapter(adapter); return;
		}
		adapter.setmList(combinateList);
	}

	/** 请求组合查询-更多按钮 */
	public void requestCombinateForMore() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PRODUCTLISTQUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCI_ACCOUNTID_REQ,BociDataCenter.getInstance().getBocinvtAcctList().get(combinIndex).get(BocInvt.BOCIBINDING_ACCOUNTIDF_RES));
		paramsmap.put(BocInvt.BOCI_PRODUCTRISKTYPE_REQ,LocalData.bociRiskTypeMap.get(boci_riskType.getText().toString()));
//		String typemore = boci_producttype.getText().toString();
//		for (int i = 0; i < productTypeList.size(); i++) {
//			if (productTypeList.get(i)
//					.get(String.valueOf(BocInvt.TYPE_NAME_RES))
//					.equals(typemore)) {
//				paramsmap.put(BocInvt.BOCI_PRODUCTTYPE_REQ, productTypeList
//						.get(i).get(String.valueOf(BocInvt.TYPE_BRANDID_RES)));
//				break;
//			}
//		}
		paramsmap.put(BocInvt.BOCI_PRODUCTCURCODE_REQ, LocalData.bociCurcodeMap.get(productCurrCode.getSelectedItem().toString()));
		paramsmap.put(BocInvt.BOCI_XPADSTATUS_REQ,LocalData.bociStatusMap.get(/*boci_status.getText().toString()*/status));
		paramsmap.put(BocInvt.BOCI_PRODTIMELIMIT_REQ,/*LocalData.bocitimeLimitMap.get(boci_timeLimit.getText().toString())*/"0");
		paramsmap.put("isLockPeriod","1");
		paramsmap.put(BocInvt.SORTFLAG,sortFlag);
		paramsmap.put(BocInvt.SORTTYPE,sortType);
		paramsmap.put("productKind","0");
		paramsmap.put(BocInvestControl.ISSUETYPE, BocInvestControl.map_issueType.get(issueType_key).toString());
		paramsmap.put("dayTerm",BocInvestControl.map_prodTimeLimit.get(timeLimit));
		paramsmap.put(BocInvestControl.PRORISK, BocInvestControl.map_proRisk.get(proRisk_key));
		paramsmap.put(BocInvt.BOCI_CURRENTINDEX_REQ, String.valueOf(loadNumber));
		paramsmap.put(BocInvt.BOCI_PAGESIZE_REQ,ConstantGloble.LOAN_PAGESIZE_VALUE);
		paramsmap.put(BocInvt.BOCI_REFRESH_REQ, ConstantGloble.REFRESH_FOR_MORE);
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestCombinateForMoreCallBack");
	}

	/**
	 * 请求组合查询回调--更多按钮
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	@SuppressWarnings("unchecked")
	public void requestCombinateForMoreCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) return;
		recordNumber = Integer.valueOf((String) result.get(Acc.RECORDNUMBER_RES));
		List<Map<String, Object>> combinateListForMore = (List<Map<String, Object>>) result
				.get(BocInvt.BOCI_LIST_RES);
		if (StringUtil.isNullOrEmpty(combinateListForMore)) return;
		combinateList.addAll(combinateListForMore);
		addFooterView(recordNumber);
		loadNumber = combinateList.size();
		adapter.setmList(combinateList);
	}
	
	/**
	 * 快速请求
	 */
	public void requestFastQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PRODUCTLISTQUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCI_ACCOUNTID_REQ,BociDataCenter.getInstance().getBocinvtAcctList().get(combinIndex).get(BocInvt.BOCIBINDING_ACCOUNTIDF_RES));
		paramsmap.put(BocInvt.BOCI_PRODUCTRISKTYPE_REQ,"0");
		paramsmap.put(BocInvt.BOCI_PRODUCTCURCODE_REQ,"000");
		paramsmap.put(BocInvt.BOCI_XPADSTATUS_REQ,"0");
		paramsmap.put(BocInvt.BOCI_PRODTIMELIMIT_REQ,"0");
		paramsmap.put("isLockPeriod","1");					//是否支持业绩基准产品查询,0：不支持1：支持
		paramsmap.put(BocInvt.SORTFLAG,"1");
		paramsmap.put(BocInvt.SORTTYPE,"0");
		paramsmap.put("productKind","0");					//产品性质,0:全部1:结构性理财产品2:类基金理财产品,默认值为0
		paramsmap.put(BocInvestControl.ISSUETYPE, "0");		//产品类型,1：现金管理类产品2：净值开放类产品3：固定期限产品 默认上送0
		paramsmap.put("dayTerm","0");						//产品期限,0：全部  1：30天以内    2：31-90天   3：91-180天   4：180天以上 默认上送0
		paramsmap.put(BocInvestControl.PRORISK, "0");		//风险等级,0：全部1：低风险2：中低风险3：中等风险4：中高风险5：高风险  默认上送0
		paramsmap.put(BocInvt.BOCI_PAGESIZE_REQ,ConstantGloble.LOAN_PAGESIZE_VALUE);
		paramsmap.put(BocInvt.BOCI_CURRENTINDEX_REQ, "0");
		paramsmap.put(BocInvt.BOCI_REFRESH_REQ, ConstantGloble.LOAN_REFRESH_FALSE);
		paramsmap.put(BocInvt.BOCI_PRODUCTCODE_REQ, mEditText.getText().toString().trim());
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestFastCallBack");
	}

	@SuppressWarnings("unchecked")
	public void requestFastCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)){ 
			BiiHttpEngine.dissMissProgressDialog(); 
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));return;
		}
		if (result.containsKey(BocInvt.BOCI_LIST_RES)) {
			List<Map<String, Object>> mList = (List<Map<String, Object>>) result.get(BocInvt.BOCI_LIST_RES);
			if (StringUtil.isNullOrEmpty(mList)){
				BiiHttpEngine.dissMissProgressDialog(); 
				BaseDroidApp.getInstanse().showInfoMessageDialog(
				this.getString(R.string.acc_transferquery_null));
				return;
			}
			fastList.addAll(mList);
		}else{
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			list.add(result);
			fastList.addAll(list);
		}
		pSNGetTokenId();
	}

	/** 理财产品点击事件 */
	OnItemClickListener onListClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (isFast) {
				requestStatus = (String) fastList.get(position).get(BocInvt.BOCI_STATUS_RES);
				availableAmt = (String) fastList.get(position).get(BocInvt.AVAILABLEAMT);
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
				requestProductDetail((String) fastList.get(position).get(
						BocInvt.BOCI_PRODCODE_RES)); 
				return;
			}
			if (combinateList == null || combinateList.size() == 0) {

			} else {
				requestStatus = (String) combinateList.get(position).get(BocInvt.BOCI_STATUS_RES);
				availableAmt = (String) combinateList.get(position).get(BocInvt.AVAILABLEAMT);
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
				requestProductDetail((String) combinateList.get(position).get(
						BocInvt.BOCI_PRODCODE_RES));
			}
		}
	};

	/**
	 * 请求产品详情
	 * 
	 * @param procode
	 *            产品代码
	 */
	public void requestProductDetail(String procode) {
		mProcode=procode;
		requestSystemDateTime();
		BaseHttpEngine.showProgressDialog();
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(BocInvt.PRODUCTDETAILQUERY);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//		Map<String, Object> paramsmap = new HashMap<String, Object>();
//		paramsmap.put(BocInvt.BOCI_PRODUCTCODE_REQ, procode);
//		paramsmap.put(BocInvestControl.PRODUCTKIND, BociDataCenter.getInstance().getChoosemap().get(BocInvestControl.PRODUCTKIND));
////		if (isFast) {
////			paramsmap.put(BocInvt.IBKNUM, BociDataCenter.getInstance().getBocinvtAcctList().get(fastIndex).get(BocInvt.IBKNUMBER));
////		}else{
//			paramsmap.put(BocInvt.IBKNUM, BociDataCenter.getInstance().getBocinvtAcctList().get(combinIndex).get(BocInvt.IBKNUMBER));
////		}
//		biiRequestBody.setParams(paramsmap);
//		// 开始通讯,展示通讯框
//		BiiHttpEngine.showProgressDialog();
//		HttpManager.requestBii(biiRequestBody, this,
//				"requestProductDetailCallBack");
	}
	private String mProcode;
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		super.requestSystemDateTimeCallBack(resultObj);
		BocInvestControl.SYSTEM_DATE=dateTime;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PRODUCTDETAILQUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCI_PRODUCTCODE_REQ, mProcode);
		paramsmap.put(BocInvestControl.PRODUCTKIND, BociDataCenter.getInstance().getChoosemap().get(BocInvestControl.PRODUCTKIND));
//		if (isFast) {
//			paramsmap.put(BocInvt.IBKNUM, BociDataCenter.getInstance().getBocinvtAcctList().get(fastIndex).get(BocInvt.IBKNUMBER));
//		}else{
			paramsmap.put(BocInvt.IBKNUM, BociDataCenter.getInstance().getBocinvtAcctList().get(combinIndex).get(BocInvt.IBKNUMBER));
//		}
		biiRequestBody.setParams(paramsmap);
		// 开始通讯,展示通讯框
		HttpManager.requestBii(biiRequestBody, this,
				"requestProductDetailCallBack");
	}

	/** 请求产品详情回调 */
	@SuppressWarnings("unchecked")
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
//		if (isFast) {
//			index = fastIndex;
//		}else{
			index = combinIndex;
//		}
		BaseDroidApp.getInstanse().getBizDataMap()
		.put(ConstantGloble.BOCINVT_BUYINIT_MAP, BociDataCenter.getInstance().getBocinvtAcctList()
				.get(index));
		Intent intent = new Intent(QueryProductActivity.this,
				ProductDetailActivity.class);
		intent.putExtra(BocInvt.BOCI_XPADSTATUS_REQ, requestStatus);
		intent.putExtra(BocInvt.AVAILABLEAMT, availableAmt);
		intent.putExtra(Comm.ACCOUNT_ID,
				(String) BociDataCenter.getInstance().getBocinvtAcctList()
				.get(index).get(Comm.ACCOUNT_ID));
		startActivityForResult(intent, ACTIVITY_BUY_CODE);
	}

	/** 查询结果头倒三角点击监听事件 */
	OnClickListener backQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			combinateLayout.setBackgroundResource(R.drawable.img_bg_query_no);
			queryLayout.setVisibility(View.VISIBLE);
			query_result_header.setVisibility(View.GONE);
		}
	};

	/** 收益累进 */
	private OnItemClickListener progressOnclik = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			QueryProductActivity.this.position = position;
			isProgress = true;
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	};
	/**产品类型*/
	private Spinner spinner_product_type;
	/**风险等级*/
	private Spinner spinner_danager_grade;
	private View query_result_header;
	private View sort_layout;

	@Override
	public void progressQueryCallBack(Object resultObj) {
		super.progressQueryCallBack(resultObj);
		int index;
//		if (isFast) {
//			index = fastIndex;
//		}else{
			index = combinIndex;
//		}
		String code = (String) combinateList.get(position).get(
				BocInvt.BOCI_DETAILPRODCODE_RES);
		String name = (String) combinateList.get(position).get(
				BocInvt.BOCI_DETAILPRODNAME_RES);
		startActivity(new Intent(this, ProgressInfoActivity.class)
				.putExtra(Comm.ACCOUNT_ID,(String)BociDataCenter.getInstance().getBocinvtAcctList()
						.get(index).get(Comm.ACCOUNT_ID))
				.putExtra(BocInvt.PROGRESS_RECORDNUM, progressRecordNumber)
				.putExtra(BocInvt.BOCI_DETAILPRODNAME_RES, name)
				.putExtra(BocInvt.BOCI_PRODUCTCODE_REQ, code));
	}
	
	/**
	 * 账户详情查询操作
	 * @param v
	 */
	public void combinAcctDetailOnclick(View v){
		if (spCombinAcct.getText().toString().equals("请选择账户")) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择账户"); return;
		}
		requestAccBankAccountDetail(combinIndex);
	}
	
	public void fastAcctDetailOnclick(View v){
		if (spCombinAcct.getText().toString().equals("请选择账户")) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择账户"); return;
		}
		requestAccBankAccountDetail(combinIndex);
	}
	
	/**
	 * 组合查询操作
	 * @param v
	 */
	public void btnCombinationOnclick(View v){
		view.findViewById(R.id.layout_fast).setVisibility(View.GONE);
		view.findViewById(R.id.layout_combination).setVisibility(View.VISIBLE);
		view.findViewById(R.id.layout_riskType).setVisibility(View.VISIBLE);
		sort_layout.setVisibility(View.GONE);//++++++++++
//		view.findViewById(R.id.layout_status).setVisibility(View.VISIBLE);
//		view.findViewById(R.id.layout_fastacct).setVisibility(View.GONE);
//		view.findViewById(R.id.layout_combinacct).setVisibility(View.VISIBLE);
		setCombinSort();
		if (view.findViewById(R.id.layout_fast).getVisibility() == View.GONE) {
			isFast = false;
			combinateList.clear();
			if (queryListView.getFooterViewsCount()>0) {
				queryListView.removeFooterView(load_more);//++++++++++
			}
//			query_result.setVisibility(View.GONE);
			queryListView.setVisibility(View.VISIBLE);
			fastListView.setVisibility(View.GONE);return;
		}
	}
	
	private void setCombinSort(){
		sortBuyAmout.setClickable(true);
		sortLimit.setClickable(true);
		sortYearRR.setClickable(true);
		Drawable drawableSelect_d = getResources().getDrawable(R.drawable.bocinvt_sort_down);
		Drawable drawableSelect_u = getResources().getDrawable(R.drawable.bocinvt_sort_up);
		Drawable drawableUnSelect = getResources().getDrawable(R.drawable.bocinvt_sort_un);
		drawableSelect_d.setBounds(0, 0, iconSize, iconSize);
		drawableSelect_u.setBounds(0, 0, iconSize, iconSize);
		drawableUnSelect.setBounds(0, 0, iconSize, iconSize);
		if (sortType.equals("1")) {
			if (sortFlag.equals("0")) {
				sortLimit.setCompoundDrawables(null, null, drawableSelect_d, null);
				sortBuyAmout.setCompoundDrawables(null, null, drawableUnSelect, null);
				sortYearRR.setCompoundDrawables(null, null, drawableUnSelect, null);
			}else if(sortFlag.equals("1")){
				sortLimit.setCompoundDrawables(null, null, drawableSelect_u, null);
				sortBuyAmout.setCompoundDrawables(null, null, drawableUnSelect, null);
				sortYearRR.setCompoundDrawables(null, null, drawableUnSelect, null);
			}
		}else if(sortType.equals("2")){
			if (sortFlag.equals("0")) {
				sortYearRR.setCompoundDrawables(null, null, drawableSelect_d, null);
				sortLimit.setCompoundDrawables(null, null, drawableUnSelect, null);
				sortBuyAmout.setCompoundDrawables(null, null, drawableUnSelect, null);
			}else if(sortFlag.equals("1")){
				sortYearRR.setCompoundDrawables(null, null, drawableSelect_u, null);
				sortLimit.setCompoundDrawables(null, null, drawableUnSelect, null);
				sortBuyAmout.setCompoundDrawables(null, null, drawableUnSelect, null);
			}
		}else if(sortType.equals("3")){
			if (sortFlag.equals("0")) {
				sortBuyAmout.setCompoundDrawables(null, null, drawableSelect_d, null);
				sortYearRR.setCompoundDrawables(null, null, drawableUnSelect, null);
				sortLimit.setCompoundDrawables(null, null, drawableUnSelect, null);
			}else if(sortFlag.equals("1")){
				sortBuyAmout.setCompoundDrawables(null, null, drawableSelect_u, null);
				sortYearRR.setCompoundDrawables(null, null, drawableUnSelect, null);
				sortLimit.setCompoundDrawables(null, null, drawableUnSelect, null);
			}
		}else{
			sortBuyAmout.setCompoundDrawables(null, null, drawableUnSelect, null);
			sortYearRR.setCompoundDrawables(null, null, drawableUnSelect, null);
			sortLimit.setCompoundDrawables(null, null, drawableUnSelect, null);
		}
	}
	
	/**
	 * 快速查询
	 * @param v
	 */
	public void btnFastOnclick(View v){
		view.findViewById(R.id.layout_fast).setVisibility(View.VISIBLE);
//		view.findViewById(R.id.layout_fastacct).setVisibility(View.VISIBLE);
//		view.findViewById(R.id.layout_combinacct).setVisibility(View.GONE);
		view.findViewById(R.id.layout_combination).setVisibility(View.GONE);
		view.findViewById(R.id.layout_riskType).setVisibility(View.GONE);
		view.findViewById(R.id.layout_status).setVisibility(View.GONE);
		sortBuyAmout.setCompoundDrawables(null, null, null, null);
		sortLimit.setCompoundDrawables(null, null, null, null);
		sortYearRR.setCompoundDrawables(null, null, null, null);
		sortBuyAmout.setClickable(false);
		sortLimit.setClickable(false);
		sortYearRR.setClickable(false);
		sort_layout.setVisibility(View.GONE);//++++++++++
		if (view.findViewById(R.id.layout_combination).getVisibility() == View.GONE) {
			isFast = true;
			fastList.clear();
//			query_result.setVisibility(View.GONE);
			queryListView.setVisibility(View.GONE);
			fastListView.setVisibility(View.VISIBLE); return;
		}
	}
	
	/**
	 * 快速查询操作
	 * @param v
	 */
	public void btnFastQueryOnclick(View v){
		if (spCombinAcct.getText().toString().equals("请选择账户")) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择账户"); return;
		}else if(StringUtil.isNull(mEditText.getText().toString())){
			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入产品代码"); return;
		}
		if (!StringUtil.isNullOrEmpty(fastList)) {
			fastList.clear();
		}
		combinateLayout.setBackgroundResource(R.drawable.img_bg_query_j);
//		query_result.setVisibility(View.GONE);
		fastListView.setVisibility(View.VISIBLE);
		BiiHttpEngine.showProgressDialog();
		requestCommConversationId();
	}
	
	/**
	 * 起售金额排序
	 */
	private void sortByBuyAmoutOnclick(){
		if (!sortType.equals("3")) {
			sortFlag = "1";
		}
		Drawable drawableSelect = null;
		if (sortFlag.equals("0")) {
			sortFlag = "1";
			drawableSelect = getResources().getDrawable(R.drawable.bocinvt_sort_down);
		}else if(sortFlag.equals("1")){
			sortFlag = "0";
			drawableSelect = getResources().getDrawable(R.drawable.bocinvt_sort_up);
		}
		isSort = true;
		sortType = "3";
		Drawable drawableUnSelect = getResources().getDrawable(R.drawable.bocinvt_sort_un);
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
		requestCombinate();
	}
	
	/**
	 * 产品期限排序
	 */
	private void sortByLimitOnclick(){
		if (!sortType.equals("1")) {
			sortFlag = "1";
		}
		Drawable drawableSelect = null;
		if (sortFlag.equals("0")) {
			sortFlag = "1";
			drawableSelect = getResources().getDrawable(R.drawable.bocinvt_sort_down);
		}else if(sortFlag.equals("1")){
			sortFlag = "0";
			drawableSelect = getResources().getDrawable(R.drawable.bocinvt_sort_up);
		}
		isSort = true;
		sortType = "1";
		Drawable drawableUnSelect = getResources().getDrawable(R.drawable.bocinvt_sort_un);
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
		requestCombinate();
	}
	
	/**
	 * 年化收益排序
	 */
	private void sortByYearRROnclick(){
		if (!sortType.equals("2")) {
			sortFlag = "1";
		}
		Drawable drawableSelect = null;
		if (sortFlag.equals("0")) {
			sortFlag = "1";
			drawableSelect = getResources().getDrawable(R.drawable.bocinvt_sort_down);
		}else if(sortFlag.equals("1")){
			sortFlag = "0";
			drawableSelect = getResources().getDrawable(R.drawable.bocinvt_sort_up);
		}
		isSort = true;
		sortType = "2";
		Drawable drawableUnSelect = getResources().getDrawable(R.drawable.bocinvt_sort_un);
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
		requestCombinate();
	}
	
	/**
	 *  账户余额查询
	 * @param mIndex
	 */
	public void requestAccBankAccountDetail(int mIndex) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_BALANCE_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Comm.ACCOUNT_ID, (String)BociDataCenter.getInstance().getBocinvtAcctList()
				.get(mIndex).get(Comm.ACCOUNT_ID));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"accBankAccountDetailCallback");

	}

	@SuppressWarnings("unchecked")
	public void accBankAccountDetailCallback(Object resultObj) {
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> balanceList = (List<Map<String, Object>>) map
				.get(Acc.DETAIL_ACCOUNTDETAILIST_RES);
		if (StringUtil.isNullOrEmpty(balanceList)) return;
		BociDataCenter.getInstance().setBalanceList(balanceList);
		BaseHttpEngine.dissMissProgressDialog();
		int index;
//		if (isFast) {
//			index = fastIndex;
//		}else{
			index = combinIndex;
//		}
		startActivityForResult((new Intent(this, BocBindinglDialogActivity.class).putExtra("p", index)),1004);
	}
	
	/**
	 * 请求token 
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}
	
	@SuppressWarnings("static-access")
	public void aquirePSNGetTokenId(Object resultObj) {
		String tokenId = (String) this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog(); return;
		}
		requestRecentAccountUpdate(tokenId);
	}
	
	/**
	 * 发送更新最近操作理财账户请求  
	 * @param tokenId
	 */
	public void requestRecentAccountUpdate(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.RECENTACCOUNTUPDATE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> params = new HashMap<String, Object>();
		int index;
//		if (isFast) {
//			index = fastIndex;
//		}else{
			index = combinIndex;
//		}
		Map<String, Object> map = BociDataCenter.getInstance().getBocinvtAcctList().get(index);
		params.put(Comm.TOKEN_REQ, tokenId);
		params.put(BocInvestControl.CAPITALACTNOKEY,BociDataCenter.getInstance().getBocinvtAcctList().get(combinIndex).get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES));
		params.put(BocInvt.BOCINACCOUNT, map.get(BocInvt.BOCINACCOUNT));
		params.put(BocInvt.CAPITALACTNO, map.get(BocInvt.ACCOUNTNO));
		params.put(Comm.ACCOUNT_TYPE, map.get(Comm.ACCOUNT_TYPE));
		params.put(BocInvt.BANCID, map.get(BocInvt.BANCID));
		params.put(Comm.ACCOUNTSTATUS, map.get(BocInvt.ACCOUNTSATUS));
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"recentAccountUpdateCallback");

	}

	public void recentAccountUpdateCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		if (isFast) {
			if (StringUtil.isNullOrEmpty(fastList)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
				return;
			}
		}else{
			if (StringUtil.isNullOrEmpty(combinateList)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						this.getString(R.string.acc_transferquery_null));
				return;
			} 
		}
		queryLayout.setVisibility(View.GONE);
		query_result_header.setVisibility(View.VISIBLE);
		sort_layout.setVisibility(View.VISIBLE);//++++++++++
//		query_result.setVisibility(View.VISIBLE);
		if (isFast) {
			((TextView)findViewById(R.id.productCur)).setText("产品代码：");
			boci_producttype.setText(mEditText.getText().toString());
			layoutAcctCombin.setVisibility(View.GONE);
			layoutAcctFast.setVisibility(View.VISIBLE);
			boci_acct_fast.setText(spCombinAcct.getText().toString());
			if (fastListView.getFooterViewsCount() > 0) {
				fastListView.removeFooterView(load_more);
			}
			if (fastAdapter ==  null) {
				fastAdapter = new ProductQueryAdapter(BaseDroidApp.getInstanse()
						.getCurrentAct(), fastList);
				fastAdapter.setViewOnClick(progressOnclik);
				fastListView.setAdapter(fastAdapter); 
				if(isEnterDetail){
					fastListView.performItemClick(fastListView.getChildAt(0), 0, fastListView.getItemIdAtPosition(0));
				}return;
			}
			fastAdapter.setmList(fastList);
			if(isEnterDetail){
				fastListView.performItemClick(fastListView.getChildAt(0), 0, fastListView.getItemIdAtPosition(0));
			}return;
		}
		addFooterView(recordNumber);
		loadNumber = combinateList.size();
		boci_riskType.setText(riskType);
		boci_status.setText(status);
		boci_timeLimit.setText(timeLimit);
		layoutAcctCombin.setVisibility(View.VISIBLE);
		layoutAcctFast.setVisibility(View.GONE);
		boci_acct.setText(spCombinAcct.getText().toString());
		boci_producttype.setText(productCurrCode.getSelectedItem().toString());
		if (adapter ==  null) {
			adapter = new ProductQueryAdapter(BaseDroidApp.getInstanse()
					.getCurrentAct(), combinateList);
			adapter.setViewOnClick(progressOnclik);
			queryListView.setAdapter(adapter); return;
		}
		adapter.setmList(combinateList);
	}
	
	/**
	 * 添加更多按钮
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
		}
	}
	public void onBackPressed() {
//		Intent intent = new Intent(QueryProductActivity.this,
//				SecondMainActivity.class);
//		startActivity(intent);
//		ActivityTaskManager.getInstance().removeAllSecondActivity();
		goToMainActivity();
	}
}
