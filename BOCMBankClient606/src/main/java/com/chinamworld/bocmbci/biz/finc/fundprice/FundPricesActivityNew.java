
package com.chinamworld.bocmbci.biz.finc.fundprice;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
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
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.adapter.FundPricesListAdapter;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.control.entity.FundCompany;
import com.chinamworld.bocmbci.biz.finc.orcm.OrcmProductListActivity;
import com.chinamworld.bocmbci.biz.finc.trade.FincTradeBuyActivity;
import com.chinamworld.bocmbci.biz.finc.trade.FincTradeScheduledBuyActivity;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.biz.login.observer.LoginWatcher;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.ErrorCode;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.ChineseCharToEn;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基金组合查询页面
 * 
 */
public class FundPricesActivityNew extends FincBaseActivity implements LoginWatcher{
	private static final String TAG = "FundPricesActivityNew";

	/** 退出动画 */
	protected Animation animation_up;
	/** 进入动画 */
	protected Animation animation_down;

	/** 查询结果页面 */
	private LinearLayout query_result;
	/** 查询信息页 */
	private View view;
	/** 查询列表和快速查询列表 */
	private ListView queryListView, fastListView;
	private FundPricesListAdapter combinAdapter, fastAdapter;
	private int iconSize;
	/** 排序 **/
	private TextView sort_unit_net, sort_daily_growth_rate;
	/** 产品排序方式 1:正序；2:逆序 */
	private String unit_net_sortflag , daily_growth_rate_sortflag;
	/** 当前排序字段 1:单位净值；3:日净值增长率 */
	private String current_sortfield;
	/** 当前排序方式 1:正序；2:逆序 */
	private String currSortFlag ;

	/**
	 * 查询条件
	 */
	private LinearLayout queryLayout;
	/** 组合查询 */
	private LinearLayout combinateLayout;
	private LinearLayout layout_fast_more, layout_combination_more,
			combinateQueryproduct;
	/** 快速查询按钮 */
	private Button fastQueryBtn;
	/** 快速查询条件 */
	private EditText mEditText;
	// 组合查询条件 下拉列表
	/** 基金公司 */
	private TextView fundCompanySpinner;
	private int fundCompanyPos = -1;
	/** 交易币种 */
	private Spinner tradeCurrCodeSpinner;
	private int tradeCurrCodePos;
	/** 风险级别 */
	private Spinner riskLevelSpinner;
	private int riskLevelPos;
	/** 产品种类 */
	private Spinner fundKindSpinner;
	private int fundTypePos;
	/** 基金状态 */
	private Spinner fundStateSpinner;
	/**基金状态 默认正常开放0*/
	private String fundState = "0";
	/** 产品类型 */
	private Spinner fundModelSpinner;
	private int fundModelPos;
	/** 基金公司名称集合 */
	private ArrayList<FundCompany> companyList= new ArrayList<FundCompany>() ;
	/** 币种集合 */
	private List<String> currencyCodeList = new ArrayList<String>();
	/** 风险级别集合 */
	private List<String> fincRiskLevel = new ArrayList<String>();
	/** 产品种类 */
	private List<String> fincKindType = new ArrayList<String>();
	/** 产品类型 */
	private List<String> fundProductType = new ArrayList<String>();
	/** 产品类型布局 */
	private LinearLayout fundModelSpinnerLayout;

	/** 组合查询结果 */
	private List<Map<String, Object>> combinateList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> fastList = new ArrayList<Map<String, Object>>();

	private RelativeLayout load_more;
	private Button btn_load_more;

	/** 理财经理推荐 */
	private RelativeLayout recommendation_link;

	/** 是否登录 */
	private boolean isLogin;
	/** 是否快速查询 */
	private boolean isFast = false;
	/** 是否刷新 */
	private boolean isRefresh = false;
	/** 首次初始化或组合查询 */
	private boolean firstInit = true;
	/** 点击更多 */
	private boolean isMoreOrCombain;
	/** 是否跳转到推荐 */
	private boolean canIntent = false;

	/** 总记录条数--默认0 */
	private int recordNumber = 0;
	/** 每页显示记录条数--默认10 */
	private int pageSize = 10;
	/** 起始索引 */
	private int currentIndex, fastCurrentIndex;
	// 组合查询默认条件
	/** 基金公司代码--默认上送"" */
	private String fundCompanyCode = "";
	/** 币种--默认上送人民币 001 */
	private String currencyCode = "001";
	/** 风险等级--默认上送"" */
	private String riskLevelStr = "";
	/** 产品种类--默认上送"00" */
	private String fundKindType = "00";
	/** 产品类型--默认上送"00" */
	private String fundType = "00";
	/** 基金名称或代码 */
	private String fastFundCodeOrName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.finc_title_allprices);
		setLeftSelectedPosition("finc_1");
		init();
	}

	/**
	 * 初始化
	 * 
	 * @Author xyl
	 */
	private void init() {

		initMainView();
		initQueryConditionView();
		initData();
		initAnim();
		initListViewListener();
		initRightBtnForTrade();
		initListDatas();
		sortView();
	}
	
	/**
	 * 排序
	 */
	private void sortView(){
		iconSize = getResources().getDimensionPixelSize(R.dimen.dp_for_zero) / 2;
		Drawable drawableUnSelect = getResources().getDrawable(
				R.drawable.bocinvt_sort_un);
		drawableUnSelect.setBounds(0, 0, iconSize, iconSize);
		sort_unit_net = (TextView)findViewById(R.id.sort_unit_net);
		sort_daily_growth_rate = (TextView)findViewById(R.id.sort_daily_growth_rate);
		sort_unit_net.setCompoundDrawables(null, null, drawableUnSelect, null);
		sort_daily_growth_rate.setCompoundDrawables(null, null, drawableUnSelect,
				null);
		sort_unit_net.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				current_sortfield = "1";
				sortUnLoginOnclick();
				
			}
		});
		sort_daily_growth_rate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				current_sortfield = "3";
				sortUnLoginOnclick();
				
			}
		});
	}
	
	/**
	 * 未登录基金行情排序 1:单位净值;3:日净值增长率
	 */
	private void sortUnLoginOnclick() {
		Drawable drawableUnitNet = null;
		Drawable drawableDailyGrowthRate = null;
		if("1".equals(current_sortfield)){
			drawableDailyGrowthRate = getResources().getDrawable(
					R.drawable.bocinvt_sort_un);
			daily_growth_rate_sortflag = "";
			if(unit_net_sortflag == null || "".equals(unit_net_sortflag)||"2".equals(unit_net_sortflag)){
				unit_net_sortflag = "1";
				drawableUnitNet = getResources().getDrawable(
						R.drawable.bocinvt_sort_up);
			}else if("1".equals(unit_net_sortflag)){
				unit_net_sortflag = "2";
				drawableUnitNet = getResources().getDrawable(
						R.drawable.bocinvt_sort_down);
			}
			currSortFlag = unit_net_sortflag;
		}else if("3".equals(current_sortfield)){
			drawableUnitNet = getResources().getDrawable(
					R.drawable.bocinvt_sort_un);
			unit_net_sortflag = "";
			if(daily_growth_rate_sortflag == null || "".equals(daily_growth_rate_sortflag)||"2".equals(daily_growth_rate_sortflag)){
				daily_growth_rate_sortflag = "1";
				drawableDailyGrowthRate = getResources().getDrawable(
						R.drawable.bocinvt_sort_up);
			}else if("1".equals(daily_growth_rate_sortflag)){
				daily_growth_rate_sortflag = "2";
				drawableDailyGrowthRate = getResources().getDrawable(
						R.drawable.bocinvt_sort_down);
			}
			currSortFlag = daily_growth_rate_sortflag;
		}
		drawableUnitNet.setBounds(0, 0, iconSize, iconSize);
		drawableDailyGrowthRate.setBounds(0, 0, iconSize, iconSize);
		sort_unit_net.setCompoundDrawables(null, null, drawableUnitNet, null);
		sort_daily_growth_rate.setCompoundDrawables(null, null, drawableDailyGrowthRate, null);
		BiiHttpEngine.showProgressDialog();
		if (queryListView.isShown()) {
			currentIndex = 0 ;
			pageSize = 10;
			combinateList.clear();
			if (isLogin) {
				requestCommConversationId();
			} else {
				combainQueryFundInfosOutlay(currentIndex, pageSize,
						currencyCode, fundCompanyCode, riskLevelStr,
						fundKindType, fundType,fundState,currSortFlag,current_sortfield);
			}
		} else if (fastListView.isShown()){
			fastCurrentIndex = 0 ;
			fastList.clear();
			if (isLogin) {
				fastQuery(fastFundCodeOrName, fastCurrentIndex,fundState,currSortFlag,current_sortfield);
			} else {
				fastQueryOutlay(fastFundCodeOrName, fastCurrentIndex,fundState,currSortFlag,current_sortfield);
			}
		}
		
	}

	private void initListViewListener() {
		queryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {

				FincControl.getInstance().fundDetails = combinateList
						.get(position);
				FincControl.getInstance().setAttentionFlag(false);
				if (isLogin) {
					BaseHttpEngine.showProgressDialog();
					attentionFundQuery();
				} else {
					startFincFundDetailAct();
				}

			}
		});
		fastListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {

				FincControl.getInstance().fundDetails = fastList
						.get(position);
				FincControl.getInstance().setAttentionFlag(false);
				if (isLogin) {
					BaseHttpEngine.showProgressDialog();
					attentionFundQuery();
				} else {
					startFincFundDetailAct();
				}

			}
		});
		
	}
	
	@Override
	public void attentionFundQueryCallback(Object resultObj) {
		super.attentionFundQueryCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (!StringUtil.isNullOrEmpty(resultMap)) {
			if (!StringUtil.isNullOrEmpty(resultMap
					.get(Finc.FINC_ATTENTIONQUERYLIST_ATTENTIONLIST))) {
				FincControl.getInstance().attentionFundList = (List<Map<String, Object>>) resultMap
						.get(Finc.FINC_ATTENTIONQUERYLIST_ATTENTIONLIST);
				for (Map<String, Object> map : FincControl.getInstance().attentionFundList) {
					// 已关注的基金
					String fundCodeAttentioned = (String) map.get(Finc.FINC_FUNDCODE);
					// 当前选择的基金
					String fundCodeCurrent = (String) fincControl.fundDetails
							.get(Finc.FINC_FUNDCODE);
					if (fundCodeAttentioned.equals(fundCodeCurrent)) {// 如果已关注
						FincControl.getInstance().setAttentionFlag(true);
					}
				}
			}
		}
		if (StringUtil.isNullOrEmpty(fincControl.attentionFundList)) {
			FincControl.getInstance().setAttentionCount(0);
		} else {
			FincControl.getInstance().setAttentionCount(
					fincControl.attentionFundList.size());
		}
		startFincFundDetailAct();
	}
	
	private void startFincFundDetailAct() {
		startActivity(new Intent(FundPricesActivityNew.this,
				FincFundDetailActivityNew.class));
	}

	/**
	 * 初始化查询条件下拉框(不包含基金公司)
	 */
	private void initData() {
		BaseDroidApp.getInstanse().getLoginObserver().addWatcher(this);

		currencyCodeList.add(getString(R.string.all));
		currencyCodeList.addAll(LocalData.fincCurrencyCodeStrList);

		fincRiskLevel.add(getString(R.string.all));
		fincRiskLevel.addAll(LocalData.fincRiskLevel);

		fincKindType.add(getString(R.string.all));
		fincKindType.addAll(LocalData.fincFundType);

		fundProductType.add(getString(R.string.all));
		fundProductType.addAll(LocalData.fundProductTypeNew);

		// 交易币种
		ArrayAdapter<ArrayList<String>> adapter2 = new ArrayAdapter(this,
				R.layout.custom_spinner_item, currencyCodeList);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tradeCurrCodeSpinner.setAdapter(adapter2);
		tradeCurrCodeSpinner.setSelection(1);
		tradeCurrCodeSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						tradeCurrCodePos = position;
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						tradeCurrCodePos = 1;
					}
				});
		// 风险级别
		ArrayAdapter<ArrayList<String>> adapter3 = new ArrayAdapter(this,
				R.layout.custom_spinner_item, fincRiskLevel);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		riskLevelSpinner.setAdapter(adapter3);
		riskLevelSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						riskLevelPos = position;
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						riskLevelPos = 0;
					}
				});
		// 产品种类
		ArrayAdapter<ArrayList<String>> adapter4 = new ArrayAdapter(this,
				R.layout.custom_spinner_item, fincKindType);
		adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fundKindSpinner.setAdapter(adapter4);
		fundKindSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				fundTypePos = position;
				String fundKindTypeStr = (String) fundKindSpinner
						.getItemAtPosition(position);
				if (LocalData.fincFundType.get(0).equals(fundKindTypeStr)) {
					fundModelSpinnerLayout.setVisibility(View.VISIBLE);
				} else {
					fundModelSpinnerLayout.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				fundTypePos = 0;
			}
		});
		// 产品类型
		ArrayAdapter<ArrayList<String>> adapter5 = new ArrayAdapter(this,
				R.layout.custom_spinner_item, fundProductType);
		adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fundModelSpinner.setAdapter(adapter5);
		fundModelSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						fundModelPos = position;
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						fundModelPos = 0;
					}
				});
				// 基金状态
				ArrayAdapter<String[]> adapter6 = new ArrayAdapter(this,
						R.layout.custom_spinner_item, new String[]{"全部","正常开放","可认购","暂停交易","暂停申购","暂停赎回"});
				adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				fundStateSpinner.setAdapter(adapter6);
				fundStateSpinner.setSelection(1);
				fundStateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int position, long id) {
						switch (position) {
						case 0:
							fundState = "";
							break;
						case 1:
							fundState = "0";
							break;
						case 2:
							fundState = "1";
							break;
						case 3:
							fundState = "4";
							break;
						case 4:
							fundState = "5";
							break;
						case 5:
							fundState = "6";
							break;

						default:
							break;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						fundState = "";
					}
				});
		/**基金公司*/
		
//		initFundCompany();
	}
	
	/***
	 * 基金公司
	 */
	private void initFundCompany(){
		FundCompany fundCompany = new FundCompany();
		fundCompany.setFundCompanyName("中银基金管理有限公司");
		fundCompany.setFundCompanyCode("50400000");
		fundCompany.setAlpha("推荐");
		fundCompany.setChecked(false);
		companyList.add(0,fundCompany);
		
		fundCompany = new FundCompany();
		fundCompany.setFundCompanyName("中银国际证券有限责任公司");
		fundCompany.setFundCompanyCode("13190000");
		fundCompany.setAlpha("推荐");
		fundCompany.setChecked(false);
		companyList.add(1,fundCompany);
		
		fundCompanySpinner.setText("全部");
		fundCompanySpinner.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FundPricesActivityNew.this , FundFundCompanyActivity.class);
				intent.putExtra("flag", "all");
				intent.putParcelableArrayListExtra("companyList", companyList);
				startActivityForResult(intent, 101);
			}
		});
	}
	
	/**
	 * 初始化查询条件布局
	 */
	private void initQueryConditionView() {
		queryLayout = (LinearLayout) view.findViewById(R.id.ll_query_condition);
		combinateLayout = (LinearLayout) view
				.findViewById(R.id.ll_combinate_queryproduct);

		recommendation_link = (RelativeLayout) queryLayout
				.findViewById(R.id.recommendation_link);
		// 初始化快速查询条件
		layout_fast_more = (LinearLayout) view
				.findViewById(R.id.layout_fast_more);
		layout_fast_more.setOnClickListener(this);

		fastQueryBtn = (Button) view.findViewById(R.id.btn_fastquery);
		fastQueryBtn.setOnClickListener(this);
		mEditText = (EditText) view.findViewById(R.id.editView);

		// 初始化组合查询条件
		fundCompanySpinner = (TextView) view
				.findViewById(R.id.finc_fund_company_query);
		tradeCurrCodeSpinner = (Spinner) view
				.findViewById(R.id.finc_currencode);
		riskLevelSpinner = (Spinner) view.findViewById(R.id.risk_level_query);
		fundKindSpinner = (Spinner) view
				.findViewById(R.id.finc_fund_type_query);
		fundStateSpinner = (Spinner) view.findViewById(R.id.finc_fund_state_query);
		fundModelSpinnerLayout = (LinearLayout) view
				.findViewById(R.id.finc_fund_model_query_ll);
		fundModelSpinnerLayout.setVisibility(View.GONE);
		fundModelSpinner = (Spinner) view
				.findViewById(R.id.finc_fund_model_query);

		layout_combination_more = (LinearLayout) view
				.findViewById(R.id.layout_combination_more);
		layout_combination_more.setOnClickListener(this);

		// 查询按钮初始化
		combinateQueryproduct = (LinearLayout) view
				.findViewById(R.id.btn_combinate_queryproduct);

		combinateQueryproduct.setOnClickListener(this);
		queryLayout.setVisibility(View.VISIBLE);

		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.acc_load_more, null);
		btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);
		btn_load_more.setOnClickListener(this);

		recommendation_link.setVisibility(View.GONE);
		query_result.setVisibility(View.INVISIBLE);
		fastListView.setVisibility(View.GONE);
		queryListView.setVisibility(View.VISIBLE);
		view.findViewById(R.id.layout_fast).setVisibility(View.VISIBLE);
		view.findViewById(R.id.layout_combination).setVisibility(View.GONE);
	}

	/**
	 * 初始化动画
	 */
	private void initAnim() {
		animation_up = AnimationUtils.loadAnimation(this, R.anim.scale_out);
		animation_down = AnimationUtils.loadAnimation(this, R.anim.scale_in);
		animation_down.setFillAfter(true);
	}

	/**
	 * 初始化查询页面主布局
	 */
	private void initMainView() {
		view = mainInflater.inflate(R.layout.finc_queryprices_activity_new,
				null);
		tabcontent.addView(view);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}

		// 初始化查询结果布局
		query_result = (LinearLayout) view.findViewById(R.id.ll_query_result);
		queryListView = (ListView) view.findViewById(R.id.finc_query_list);
		fastListView = (ListView) view.findViewById(R.id.fast_query_list);
		queryListView.setFocusable(true);
		fastListView.setFocusable(true);
	}

	private void initListDatas() {
		isLogin = BaseDroidApp.getInstanse().isLogin();
		isRefresh = true;
//		combinateList.clear();
//		fastList.clear();
		// 获取基金公司列表
		if (isLogin) {
			BaseHttpEngine.showProgressDialogCanGoBack();
			doCheckRequestPsnInvestmentManageIsOpen();
//			getFundCompanyList();
		} else {
			back.setOnClickListener(backBtnClick);
//			recommendation_link.setVisibility(View.GONE);
			BaseHttpEngine.showProgressDialog();
			getFundCompanyListOutlay();
		}
	}

	
	@Override
	public void doCheckRequestQueryInvtBindingInfoCallback(
			Object resultObj) {
		super.doCheckRequestQueryInvtBindingInfoCallback(resultObj);
		if(!fincControl.ifhaveaccId || !fincControl.ifInvestMent) return;
		getFundCompanyList();

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 101:
			if(resultCode == RESULT_OK){
				fundCompanyPos = data.getIntExtra("fundCompanyPos", -1);
				if(fundCompanyPos>=0){
					fundCompanySpinner.setText(companyList.get(fundCompanyPos).getFundCompanyName());
					fundCompanyCode = companyList.get(fundCompanyPos).getFundCompanyCode();
				}else{
					fundCompanySpinner.setText("全部");
					fundCompanyCode = "";
				}
				fundCompanySpinner.postInvalidate();
			}
			break;
		case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 开通理财服务
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifInvestMent = true;
				if (!fincControl.ifhaveaccId) {// 如果还没有基金账户
					getPopup();
				}
				break;

			default:
				fincControl.ifInvestMent = false;
				getPopup();
				break;
			}
			break;
		case ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE:// 开通基金账户
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifhaveaccId = true;
				BaseHttpEngine.showProgressDialog();
				getFundCompanyList();
				break;

			default:
				fincControl.ifhaveaccId = false;
				getPopup();
				break;
			}
			break;
		case InvestConstant.FUNDRISK:// 基金风险评估
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifdorisk = true;
				switch (httpFlag) {
				case FASTDEALBUY:
					fincControl.cleanTrade();
					Intent intent = new Intent();
					intent.setClass(BaseDroidApp.getInstanse()
							.getCurrentAct(), FincTradeBuyActivity.class);
					startActivityForResult(intent, 1);
					break;
				case FASTDEALSCHEDUBUY:
					fincControl.cleanTrade();
					intent = new Intent();
					intent.setClass(BaseDroidApp.getInstanse()
							.getCurrentAct(),
							FincTradeScheduledBuyActivity.class);
					startActivityForResult(intent, 1);
					break;
				default:
					break;
				}
				break;
			default:
				fincControl.ifdorisk = false;
				getPopup();
				break;
			}
			break;

		default:
			break;
		}
	}
	
	/**
	 *  返回按钮点击事件
	 */
	private OnClickListener backBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String isFromModuleManager = (String)BaseDroidApp.getInstanse().getBizDataMap().get("isFromModuleManager");
			if(!StringUtil.isNullOrEmpty(isFromModuleManager)&&"true".equals(isFromModuleManager)){
				finish();
			}else{
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent(FundPricesActivityNew.this,
//						SecondMainActivity.class);
//				startActivity(intent);
//				goToMainActivity();
				finish();
			}
		}

	};

	private void getFundCompanyListOutlay() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_GETFUNDCOMPANCYLIST_OUTLAY);
		biiRequestBody.setParams(null);
		HttpManager.requestOutlayBii(biiRequestBody, this,
				"getFundCompanyListCallback");
	}

	@Override
	public void getFundCompanyListCallback(Object resultObj) {
		super.getFundCompanyListCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		fincControl.fundCompanyList = (List<Map<String, String>>) biiResponseBody
				.getResult();
		List<Map<String, String>> list =  fincControl.fundCompanyList;
		for (Map<String, String> map : list) {
			String companyName = map.get(Finc.FINC_GETFUNDCOMPANCYLIST_FUNDCOMPANYNAME);
			// 为基金公司下拉列表赋值
			FundCompany fundCompany = new FundCompany();
			fundCompany.setFundCompanyName(companyName);
			fundCompany.setFundCompanyCode(map
					.get(Finc.FINC_GETFUNDCOMPANCYLIST_FUNDCOMPANYCODE));
			fundCompany.setChecked(false);
			fundCompany.setAlpha(ChineseCharToEn.cn2py(companyName));
			companyList.add(fundCompany);
			
		}
		Collections.sort(companyList, new Comparator<FundCompany>() {

			public int compare(FundCompany o1, FundCompany o2) {  
		        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序  
		        if (o2.getAlpha().equals("#")) {  
		            return -1;  
		        } else if (o1.getAlpha().equals("#")) {  
		            return 1;  
		        } else {  
		            return o1.getAlpha().compareTo(o2.getAlpha());  
		        }  
		    }});
		initFundCompany();
		if (isLogin) {
			requestCommConversationId();
		} else {
			combainQueryFundInfosOutlay(currentIndex, pageSize, currencyCode,
					fundCompanyCode, riskLevelStr, fundKindType, fundType,fundState,currSortFlag,current_sortfield);
		}

	}

	/** 获取未登录基金行情 */
	private void combainQueryFundInfosOutlay(int currentIndex, int pageSize,
			String currencyCode, String fundCompanyCode, String risklv,
			String fntype, String fundProductTypeStr,String fundState,
			String sortFlag ,String sortField) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.PSNQUERYFUNDDETAIL_OUTLAY);
		Map<String, String> params = new HashMap<String, String>();
		params.put(Finc.COMBINQUERY_CURRENTINDEX, String.valueOf(currentIndex));
		params.put(Finc.COMBINQUERY_PAGESIZE, String.valueOf(pageSize));
		params.put(Finc.PSNQUERYFUNDDETAIL_CURRENCYCODE, currencyCode);
		params.put(Finc.PSNQUERYFUNDDETAIL_COMPANY, fundCompanyCode);
		params.put(Finc.PSNQUERYFUNDDETAIL_RISKGRADE, risklv);
		params.put(Finc.PSNQUERYFUNDDETAIL_FUNDKIND, fntype);
		params.put(Finc.PSNQUERYFUNDDETAIL_FUNDTYPE, fundProductTypeStr);
		params.put(Finc.PSNQUERYFUNDDETAIL_FUNDSTATE, fundState);
		params.put(Finc.PSNQUERYFUNDDETAIL_SORTFLAG, sortFlag);
		params.put(Finc.PSNQUERYFUNDDETAIL_SORTFIELD, sortField);
		biiRequestBody.setParams(params);
		HttpManager.requestOutlayBii(biiRequestBody, this,
				"combainQueryFundInfosCallback");
		
	}

	@Override
	public void combainQueryFundInfosCallback(Object resultObj) {
		super.combainQueryFundInfosCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		currentIndex += 10;
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BiiHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.acc_transferquery_null));
			addFooterView(recordNumber);
			return;
		}
		recordNumber = Integer.valueOf((String) resultMap
				.get(Acc.RECORDNUMBER_RES));

		if (isRefresh) {
			isRefresh = false;
			combinateList.clear();
			fastList.clear();

			// 收起
			if (view.findViewById(R.id.layout_combination).isShown()) {
				view.findViewById(R.id.layout_fast).setVisibility(View.VISIBLE);
				view.findViewById(R.id.layout_combination).setVisibility(
						View.GONE);
				// view.findViewById(R.id.layout_combination).startAnimation(animation_up);
			}
		}
		combinateList.addAll((List<Map<String, Object>>) resultMap
				.get(Finc.COMBINQUERY_LIST));

		BiiHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(combinateList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			addFooterView(recordNumber);
			return;
		}
		query_result.setVisibility(View.VISIBLE);
		fastListView.setVisibility(View.GONE);
		queryListView.setVisibility(View.VISIBLE);


		addFooterView(recordNumber);

		if (combinAdapter == null) {
			combinAdapter = new FundPricesListAdapter(this, combinateList);
			queryListView.setAdapter(combinAdapter);
		} else {
			combinAdapter.notifyDataSetChanged(combinateList);
		}

	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (isMoreOrCombain) {
			isMoreOrCombain = false;
			combainQueryFundInfos(currentIndex, pageSize, currencyCode,
					fundCompanyCode, riskLevelStr, fundKindType, fundType,fundState,currSortFlag,current_sortfield);
		} else {
			requestPsnOcrmProductQuery("01", "", ConstantGloble.FOREX_PAGESIZE,
					"0", true);
		}
	}

	@Override
	public void ocrmProductQueryCallBack(Object resultObj) {
		super.ocrmProductQueryCallBack(resultObj);

		if (StringUtil.isNullOrEmpty(fincControl.OcrmProductList)) {
			recommendation_link.setVisibility(View.GONE);
		} else {
			recommendation_link.setVisibility(View.VISIBLE);
		}

		if (firstInit) {
//			firstInit = false;
			combainQueryFundInfos(currentIndex, pageSize, currencyCode,
					fundCompanyCode, riskLevelStr, fundKindType, fundType,fundState,currSortFlag,current_sortfield);
		} else {
//			if (canIntent) {
//				canIntent = false;
//				if (StringUtil.isNullOrEmpty(fincControl.OcrmProductMap)
//						|| StringUtil
//								.isNullOrEmpty(fincControl.OcrmProductList)) {
//					BaseDroidApp.getInstanse().showInfoMessageDialog(
//							getString(R.string.finc_query_noresult_error));
//					return;
//				}
//				FincControl.isRecommend = true;
//				startActivityForResult(new Intent(this,
//						OrcmProductListActivity.class), 1);
//			}
		}

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

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& FincControl.recommendEnterMarket) {
			Intent intent = new Intent();
			if (isLogin) {
				intent.setClass(FundPricesActivityNew.this,
						FundPricesMenuActivity.class);
				startActivity(intent);
			} else {
//				intent.setClass(FundPricesActivityNew.this,
//						SecondMainActivity.class);
				goToMainActivity();
			}

			finish();
			return false;
		}

		return super.onKeyDown(keyCode, event);
	};

	/** 点击推荐栏 请查看 的按钮事件 */
	public void buttonOnclick(View v) {
//		BaseHttpEngine.showProgressDialog();
//		canIntent = true;
//		requestCommConversationId();
		if (StringUtil.isNullOrEmpty(fincControl.OcrmProductMap)
				|| StringUtil
						.isNullOrEmpty(fincControl.OcrmProductList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_query_noresult_error));
			return;
		}
		FincControl.isRecommend = true;
		startActivityForResult(new Intent(this,
				OrcmProductListActivity.class), 1);
	}


	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_combinate_queryproduct:// 组合查询按钮
			BaseHttpEngine.showProgressDialog();
			isMoreOrCombain = true;
			isRefresh = true;
			currentIndex = 0;
			getQueryCondition();
			if (isLogin) {
				requestCommConversationId();
			} else {
				combainQueryFundInfosOutlay(currentIndex, pageSize,
						currencyCode, fundCompanyCode, riskLevelStr,
						fundKindType, fundType,fundState,currSortFlag,current_sortfield);
			}
			break;
		case R.id.btn_fastquery:// 快速查询按钮
			fastCurrentIndex = 0;
			fastList.clear();
			fastFundCodeOrName = StringUtil
					.trim(mEditText.getText().toString());
			if (StringUtil.isNull(fastFundCodeOrName)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请输入基金代码或名称");
				return;
			}
			if (!setFincinputRegexpBean(fastFundCodeOrName, "")) {
				return;
			}
			if (StringUtil.isNullOrEmpty(fastFundCodeOrName)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.forex_no_fundcode_error));
				return;
			}
			BaseHttpEngine.showProgressDialog();
			/**快速查询，基金状态上送全部*/
			if (isLogin) {
				fastQuery(fastFundCodeOrName, fastCurrentIndex,"",currSortFlag,current_sortfield);
			} else {
				fastQueryOutlay(fastFundCodeOrName, fastCurrentIndex,"",currSortFlag,current_sortfield);
			}
			query_result.setVisibility(View.VISIBLE);
			queryListView.setVisibility(View.GONE);
			fastListView.setVisibility(View.VISIBLE);

			break;
		case R.id.layout_fast_more: // 下拉筛选
			view.findViewById(R.id.layout_combination).setVisibility(
					View.VISIBLE);
			view.findViewById(R.id.layout_fast).setVisibility(View.GONE);
			// view.findViewById(R.id.layout_combination).startAnimation(
			// animation_down);

			break;
		case R.id.layout_combination_more: // 收起筛选
			view.findViewById(R.id.layout_combination).setVisibility(View.GONE);
			view.findViewById(R.id.layout_fast).setVisibility(View.VISIBLE);
			// view.findViewById(R.id.layout_combination).startAnimation(
			// animation_up);

			break;
		case R.id.btn_load_more: // 更多
			BaseHttpEngine.showProgressDialog();
			if (queryListView.isShown()) {
				isMoreOrCombain = true;
				isRefresh = false;
				if (isLogin) {
					requestCommConversationId();
				} else {
					combainQueryFundInfosOutlay(currentIndex, pageSize,
							currencyCode, fundCompanyCode, riskLevelStr,
							fundKindType, fundType,fundState,currSortFlag,current_sortfield);
					
				}
			} else if (fastListView.isShown()){
				if (isLogin) {
					fastQuery(fastFundCodeOrName, fastCurrentIndex,fundState,currSortFlag,current_sortfield);
				} else {
					fastQueryOutlay(fastFundCodeOrName, fastCurrentIndex,fundState,currSortFlag,current_sortfield);
				}
			}
			break;
		default:
			break;
		}
	}
	
	// 用于验证用户输入基金代码或基金名称。 基金代码或基金名称由数字、字母、汉字组成，且不能超过50个字符
	// <rule type="fundInput" pattern="^[a-zA-Z0-9\u4e00-\u9fa5]{0,50}$"
	// tip="基金代码或基金名称由数字、字母、汉字组成，且不能超过50个字符" />

	private boolean setFincinputRegexpBean(String repayAmount, String massage) {
		RegexpBean reb1 = new RegexpBean(massage, repayAmount,
				"fincinputcheckout");
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		lists.add(reb1);
		if (RegexpUtils.regexpDate(lists)) {
			return true;
		} else {
			return false;
		}
	}

	private void fastQueryOutlay(String fundInfo,Integer currentIndex,String fundState,
			String sortFlag ,String sortField) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.PSNQUERYFUNDDETAIL_OUTLAY);
		Map<String, String> params = new HashMap<String, String>();
		params.put(Finc.PSNQUERYFUNDDETAIL_FUNDINFO, fundInfo);
		params.put(Finc.COMBINQUERY_CURRENTINDEX, String.valueOf(currentIndex));
		params.put(Finc.COMBINQUERY_PAGESIZE, String.valueOf(10));
		params.put(Finc.PSNQUERYFUNDDETAIL_FUNDTYPE, "00");
		params.put(Finc.PSNQUERYFUNDDETAIL_FUNDKIND, "00");
		params.put(Finc.PSNQUERYFUNDDETAIL_FUNDSTATE, fundState);
		params.put(Finc.PSNQUERYFUNDDETAIL_SORTFLAG, sortFlag);
		params.put(Finc.PSNQUERYFUNDDETAIL_SORTFIELD, sortField);
		biiRequestBody.setParams(params);
		HttpManager.requestOutlayBii(biiRequestBody, this, "fastQueryCallback");
	}
	
	@Override
	public void fastQueryCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.fastQueryCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap
				.get(Finc.COMBINQUERY_LIST);
		int fastTotalNum = 0;
		if(StringUtil.isNull(((String) resultMap
				.get(Finc.COMBINQUERY_RECORDNUMBER))) == false){
			fastTotalNum = Integer
					.valueOf((String) resultMap
							.get(Finc.COMBINQUERY_RECORDNUMBER));
		}
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_query_noresult_error));
			return;
		} else {
			fastCurrentIndex += 10;
			if (fastList == null || fastList.isEmpty() ) {// 新查询
				fastList = resultList;
				addFastFooterView(fastTotalNum);
				fastAdapter = new FundPricesListAdapter(this, fastList);
				fastListView.setAdapter(fastAdapter);
			} else {
				fastList.addAll(resultList);
				addFastFooterView(fastTotalNum);
				fastAdapter.notifyDataSetChanged(fastList);
			}
		}
	}	
	
	private void addFastFooterView(int totalCount) {
		if (totalCount > fastList.size()) {
			if (fastListView.getFooterViewsCount() <= 0) {
				fastListView.addFooterView(load_more);
			}
			fastListView.setClickable(true);
		} else {
			if (fastListView.getFooterViewsCount() > 0) {
				fastListView.removeFooterView(load_more);
			}
		}
	}	

	/**
	 * 获取查询条件
	 */
	private void getQueryCondition() {
		// 基金公司
		if(fundCompanyPos >= 0)
			fundCompanyCode = (String) companyList.get(fundCompanyPos).getFundCompanyCode();
		// 交易币种
		if (tradeCurrCodePos > 0) {
			currencyCode = (String) LocalData.fincCurrencyCodeList
					.get(tradeCurrCodePos - 1);
		} else {
			currencyCode = "";
		}
		// 风险级别
		if (riskLevelPos > 0) {
			riskLevelStr = (String) LocalData.fincRiskLevelCode
					.get(riskLevelPos - 1);
		} else {
			riskLevelStr = "";
		}
		// 产品种类
		fundKindType = (String) LocalData.fincFundTypeCode.get(fundTypePos);
		// 产品类型
		fundType = (String) LocalData.fundProductTypeListNew.get(fundModelPos);
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (Finc.FINC_ATTENTIONQUERYLIST
					.equals(biiResponseBody.getMethod())) {
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError
									.getCode())) {// 表示回话超时 要重新登录
								showTimeOutDialog(biiError.getMessage());
//								BaseDroidApp.getInstanse().showMessageDialog(
//										biiError.getMessage(),
//										new OnClickListener() {
//											@Override
//											public void onClick(View v) {
//												BaseDroidApp.getInstanse()
//														.dismissErrorDialog();
//												ActivityTaskManager
//														.getInstance()
//														.removeAllSecondActivity();
////												Intent intent = new Intent();
////												intent.setClass(FundPricesActivityNew.this,
////														LoginActivity.class);
////												startActivityForResult(
////														intent,
////														ConstantGloble.ACTIVITY_RESULT_CODE);
//												BaseActivity.getLoginUtils(FundPricesActivityNew.this).exe(new LoginTask.LoginCallback() {
//
//													@Override
//													public void loginStatua(boolean isLogin) {
//
//													}
//												});
//											}
//										});
								// return true;
							} else
								return false;// 会话未超时
						}
					}
				}
				return false;// 没有异常
			}else if(Finc.METHOD_PSNOCRMPRODUCTQUERY.equals(biiResponseBody.getMethod())) {
				BiiHttpEngine.dissMissProgressDialog();
				BiiError biiError = biiResponseBody.getError();
				// 判断是否存在error
				if (biiError != null) {
					recommendation_link.setVisibility(View.GONE);
					if (firstInit) {
//						firstInit = false;
						BaseHttpEngine.showProgressDialog();
						combainQueryFundInfos(currentIndex, pageSize, currencyCode,
								fundCompanyCode, riskLevelStr, fundKindType, fundType,fundState,currSortFlag,current_sortfield);
					}
					if (biiError.getCode() != null) {
						if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
							showTimeOutDialog(biiError.getMessage());
//							BaseDroidApp.getInstanse().showMessageDialog(
//									biiError.getMessage(),
//									new OnClickListener() {
//										@Override
//										public void onClick(View v) {
//											BaseDroidApp.getInstanse()
//													.dismissErrorDialog();
//											ActivityTaskManager
//													.getInstance()
//													.removeAllSecondActivity();
////											Intent intent = new Intent();
////											intent.setClass(FundPricesActivityNew.this,
////													LoginActivity.class);
////											startActivityForResult(
////													intent,
////													ConstantGloble.ACTIVITY_RESULT_CODE);
//											BaseActivity.getLoginUtils(FundPricesActivityNew.this).exe(new LoginTask.LoginCallback() {
//
//												@Override
//												public void loginStatua(boolean isLogin) {
//
//												}
//											});
//										}
//									});
						}
					}else {
//						largest_exchange_layout.setVisibility(View.VISIBLE);
					}
				}
		
//				initViewInfos();
//				BaseDroidApp.getInstanse().createDialog("",
//						biiError.getMessage(), new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								BaseDroidApp.getInstanse()
//										.dismissErrorDialog();
//								
//							}
//						});
				return true;
			} else {	// add
				String errorCode = biiResponseBody.getError().getCode();
				if (errorCode.equals(ErrorCode.FINC_ACCCHECIN_ERROR)
						||errorCode.equals(ErrorCode.FINC_ACCNO_ERROR)
						||errorCode.equals(ErrorCode.FINC_ACCNO_ERROR_2)) {
					fincControl.ifhaveaccId = false;
					fincControl.ifdorisk = false;
					BaseHttpEngine.dissMissProgressDialog();
					getPopup();
					return true;
				}
				String method = biiResponseBody.getMethod();
				if("PsnOcrmProductQuery".equals(method)){
					if("MCIS.SDERR".equals(errorCode)){
						BaseDroidApp.getInstanse().showMessageDialog(
								biiResponseBody.getError().getMessage(),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										BaseHttpEngine.dissMissProgressDialog();
									}
								});
						return true;
					}
				}
			}
		}
		return super.httpRequestCallBackPre(resultObj);
	}

	@Override
	protected void onDestroy() {
		BaseDroidApp.getInstanse().getLoginObserver().removeWatcher(this);
		super.onDestroy();
	}
	
	@Override
	public void updateLoginState(boolean isLogin) {
		initListDatas();
	}
	
	/**
	 * 快速交易按钮初始化
	 */
	protected void initRightBtnForTrade() {
		right.setText(getResources().getString(R.string.boci_fast_trans));
		right.setVisibility(View.VISIBLE);
		rightBtnOnClickListenerForTrade = new OnClickListener() {

			@Override
			public void onClick(View view) {
				int tag = (Integer) view.getTag();
				switch (tag) {
				case CustomDialog.TAG_CANCLE:
					BaseDroidApp.getInstanse().dismissMessageDialog();
					break;
				case CustomDialog.TAG_RELA_ACC_TRAN:// 买入
					if (docheck1()) {
						httpFlag = FASTDEALBUY;
						/**基金买入，判断是否未风险评估*/
						BaseHttpEngine.showProgressDialog();
						doCheckRequestPsnFundRiskEvaluationQueryResult();
					}
					break;
				case CustomDialog.TAG_COMMON_RECEIVER_TRAN:// 卖出
					if (docheck1()) {
						httpFlag = FASTDEALSCHEDUBUY;
						/**基金买入，判断是否未风险评估*/
						BaseHttpEngine.showProgressDialog();
						doCheckRequestPsnFundRiskEvaluationQueryResult();
					}
					break;

				default:
					break;
				}

			}
		};
		OnClickListener rightClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!StringUtil
						.isNullOrEmpty(FincControl.getInstance().tradeFundDetails)) {
					FincControl.getInstance().tradeFundDetails.clear();
				}
				BaseDroidApp.getInstanse().showSelectBuyOrSaleDialog(
						getString(R.string.prms_buy),
						getString(R.string.finc_inves),
						rightBtnOnClickListenerForTrade);
			}
		};

		right.setOnClickListener(rightClick);

	}
	
	/**
 * 检查是否做了风险认证的回调处理
 * 
 * @param resultObj
 */
public void doCheckRequestPsnFundRiskEvaluationQueryResultCallback(
		Object resultObj) {
	super.doCheckRequestPsnFundRiskEvaluationQueryResultCallback(resultObj);
	if(fincControl.ifdorisk){
		switch (httpFlag) {
		case FASTDEALBUY:
			fincControl.cleanTrade();
			Intent intent = new Intent();
			intent.setClass(BaseDroidApp.getInstanse()
					.getCurrentAct(), FincTradeBuyActivity.class);
			startActivityForResult(intent, 1);
			break;
		case FASTDEALSCHEDUBUY:
			fincControl.cleanTrade();
			intent = new Intent();
			intent.setClass(BaseDroidApp.getInstanse()
					.getCurrentAct(),
					FincTradeScheduledBuyActivity.class);
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
	}
}

}

