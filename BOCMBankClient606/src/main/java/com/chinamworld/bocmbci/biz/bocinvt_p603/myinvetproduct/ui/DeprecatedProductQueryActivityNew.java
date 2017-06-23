package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.dialogActivity.BocBindinglDialogActivity;
import com.chinamworld.bocmbci.biz.bocinvt.inflateView.InflaterViewDialog;
//import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.adapter.MyProductQueryAdapter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BocProductForDeprecateInfo;
import com.chinamworld.bocmbci.biz.plps.adapter.AnnuitySpinnerAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

/**
 * 到期产品查询页面
 * 
 * @author niuchf
 * 
 */
public class DeprecatedProductQueryActivityNew extends BociBaseActivity implements
OnClickListener,ICommonAdapter<Map<String, Object>> {
	/** 查询信息页 */
	private View view;
	/** 查询开始日期 */
	private TextView tv_startdate;
	/** 查询结束日期 */
	private TextView tv_enddate;
	/** 交易账户 */
	private TextView spAcct;
	/** 交易币种 */
	private Spinner spAcctType;
	/** 产品到期日 */
	private TextView boci_product_name;
	/** 产品名称 */
	private TextView boci_yearlyRR;
	/** 投资本金 */
	private TextView boci_timeLimit;
	/** 理财交易账户left */
	private TextView tv_bocinvt_trfTypeLeft;
	/** 交易币种left */
	private TextView tv_deal_accountLeft;
	/** 灰色分割线 */
	private ImageView img_line;
	// 按钮
	/** 查询 */
	private Button combinateQueryproduct;
	// 查询结果页面
	private LinearLayout query_result;
	/** 查询列表 */
	private ListView queryListView;
	// 查询结果头
	/** 组合查询头 */
	private LinearLayout header_for_combinate;
	/** 查询条件 查询日期、交易账户、交易币种*/
	private TextView boci_date,boci_acct,boci_tratype;
	/** 查询结果 */
	private List<Map<String, Object>> list;
	/** 系统时间 */
	private String currenttime;
	private String startDate;
	private String endDate;
	/** 收起 */
	private LinearLayout img_up;
	/** 查询条件返回结果 */
	private RelativeLayout load_more;
	private Button btn_load_more;
//	private RelativeLayout rl_query_transfer;
	private LinearLayout combinateLayout;
//	private MyProductQueryAdapter adapter;
	/**采用通用的Adapter接口来实现数据适配功能**/
	private CommonAdapter<Map<String, Object>> adapter;
	private Map<String, Object> accItemMap;
	private int loadNumber = 0;
	private int recordNumber = 0;
	private ArrayList<BocProductForDeprecateInfo> infos;
	//最近操作的账户
	private String recentAccount;
	private int mIndex;
	private boolean isOpen = false;
	private boolean isevaluation = false;
	/** 登记账户信息 */
	private List<Map<String, Object>> investBindingInfo = new ArrayList<Map<String, Object>>();
	/** 查询框交易币种 */
	private LinearLayout currency_layout;
	/** 反显交易币种 */
	private LinearLayout currency_layout2;
	private LinearLayout rl_query_transfer,ll_query_result_header;
	/** 快捷时间查询*/
	private LinearLayout acc_query_choosedate;
	/** 查询一周 */
	private Button btn_query_onweek;
	/** 查询一个月 */
	private Button btn_query_onmonth;
	/** 查询三个月 */
	private Button btn_query_threemonth;
	/** 灰色提示语*/
	private LinearLayout fengge_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getResources().getString(R.string.bocinvt_query_for_due_product));
		//requestSystemDateTime();
		//initfastfoot();
		requestCommConversationId();
		BaseHttpEngine.showProgressDialogCanGoBack();
	}
	
	/** 初始化界面 */
	private void init() {// 添加布局
		infos = new ArrayList<BocProductForDeprecateInfo>();
		view = addView(R.layout.bocinvt_commissionquery_activity_p603);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}
		tv_bocinvt_trfTypeLeft = (TextView) view.findViewById(R.id.tv_bocinvt_trfType);
		tv_bocinvt_trfTypeLeft.setText(R.string.bocinvt__deal_account);
		tv_deal_accountLeft = (TextView) view.findViewById(R.id.tv_deal_account);
		tv_deal_accountLeft.setText(R.string.bocinvt__deal_currcy);
		img_line = (ImageView) view.findViewById(R.id.img_line);
		currency_layout2 = (LinearLayout) view.findViewById(R.id.currency_layout);
		currency_layout2.setVisibility(View.GONE);
		acc_query_choosedate = (LinearLayout) view.findViewById(R.id.acc_query_choosedate);
		acc_query_choosedate.setVisibility(View.VISIBLE);
		fengge_layout = (LinearLayout) view.findViewById(R.id.fengge_layout);
		fengge_layout.setVisibility(View.VISIBLE);
		btn_query_onweek = (Button) view
				.findViewById(R.id.btn_acc_onweek);
		btn_query_onmonth = (Button) view
				.findViewById(R.id.btn_acc_onmonth);
		btn_query_threemonth = (Button) view
				.findViewById(R.id.btn_acc_threemonth);
		btn_query_onweek.setOnClickListener(this);
		btn_query_onmonth.setOnClickListener(this);
		btn_query_threemonth.setOnClickListener(this);
		//加载 交易查询主页面顶部条件选择框
//		rl_query_transfer = (RelativeLayout) LayoutInflater.from(this).inflate(
//				R.layout.bocinvt_commissionquery_condition, null);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(rl_query_transfer,
//				BaseDroidApp.getInstanse().getCurrentAct());
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		rl_query_transfer = (LinearLayout) view.findViewById(R.id.ll_query_condition);
		ll_query_result_header = (LinearLayout) view.findViewById(R.id.query_result_header);
		spAcct = (TextView) rl_query_transfer
				.findViewById(R.id.boci_acct_trfAcc);//交易账户
		spAcctType = (Spinner) rl_query_transfer.findViewById(R.id.boci_acct_trfCurrency);//交易币种
		currency_layout = (LinearLayout) rl_query_transfer.findViewById(R.id.container_account);
		currency_layout.setVisibility(View.GONE);
		rl_query_transfer.setVisibility(View.VISIBLE);
		ll_query_result_header.setVisibility(View.GONE);
		clickSpinner();
		spAcct.setOnClickListener(acctOnClick);
		if(!spAcct.getText().toString().trim().equals("")){//初始化页面时 存储默认账户信息
			accItemMap = BociDataCenter.getInstance().getBocinvtAcctList().get(0);
		}
		if (!StringUtil.isNull(recentAccount)) {
			List<Map<String,Object>> list = BociDataCenter.getInstance().getBocinvtAcctList();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get(BocInvt.ACCOUNTNO).equals(recentAccount)) {
					spAcct.setText(StringUtil.getForSixForString(recentAccount));
					mIndex = i;
					accItemMap = list.get(i);
					break;
				}
			}
		}else{
			if (BociDataCenter.getInstance().getBocinvtAcctList().size() == 1) {
				spAcct.setText(PublicTools.getSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO).get(mIndex));
			}
		}
		// 按钮初始化
		combinateQueryproduct = (Button) rl_query_transfer
				.findViewById(R.id.btn_combinate_queryproduct);
		combinateQueryproduct.setOnClickListener(this);
		img_up = (LinearLayout) rl_query_transfer.findViewById(R.id.ll_up);
		tv_startdate = (TextView) rl_query_transfer
				.findViewById(R.id.acc_query_transfer_startdate);
		tv_enddate = (TextView) rl_query_transfer
				.findViewById(R.id.acc_query_transfer_enddate);
		combinateLayout = (LinearLayout) rl_query_transfer
				.findViewById(R.id.ll_combinate_queryproduct);
		// 三天前的日期
		String startThreeDate = QueryDateUtils.getlastthreeDate(dateTime);
		tv_startdate.setText(startThreeDate);
		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		// 初始结束时间赋值
		tv_enddate.setText(currenttime);
		tv_startdate.setOnClickListener(chooseDateClick);
		tv_enddate.setOnClickListener(chooseDateClick);
		// 收起事件监听
		img_up.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断是否已经进行了查询—如果已经进行:收起所有;否则收起查询条件区域
				if (list == null || list.size() == 0) {
					// 隐藏所有

				} else {
					PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
					query_result.setVisibility(View.VISIBLE);
					ll_query_result_header.setVisibility(View.VISIBLE);
					rl_query_transfer.setVisibility(View.GONE);
					initHead();
				}
			}
		});
		boci_product_name = (TextView) view.findViewById(R.id.boci_product_name);
		boci_yearlyRR = (TextView) view.findViewById(R.id.boci_yearlyRR);
		boci_timeLimit = (TextView) view.findViewById(R.id.boci_timeLimit);
		boci_yearlyRR.setSingleLine(false);
		boci_product_name.setSingleLine(false);
		boci_timeLimit.setSingleLine(false);
		query_result = (LinearLayout) view.findViewById(R.id.ll_query_result);

		boci_date = (TextView) view.findViewById(R.id.tv_boci_date_value);//查询日期
		boci_acct = (TextView) view.findViewById(R.id.textview_tratype_value);//交易账户
		queryListView = (ListView) view.findViewById(R.id.boci_query_list);
		boci_tratype = (TextView) view.findViewById(R.id.bocinvt_acct_value3);//交易币种
		// 查询结果头返回
		ImageView boci_query_back = (ImageView) view
				.findViewById(R.id.img_boci_query_back);
		boci_query_back.setOnClickListener(backQueryClick);
		header_for_combinate = (LinearLayout) view
				.findViewById(R.id.header_for_combinate);

		header_for_combinate.setOnClickListener(backQueryClick);

		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.acc_load_more, null);
		btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);
		btn_load_more.setBackgroundResource(R.color.transparent_00);
		btn_load_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestProductForMore();
			}
		});
		
	}

	/** 账户选择点击事件 */
	OnClickListener acctOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			createDialog(PublicTools.getSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO), "理财交易账户",mIndex);
		}
	};
	
	/**
	 * 创建spinner弹窗框
	 * @param list
	 * @param title
	 * @param position
	 */
	private void createDialog(List<String> list,String title,int position){
		ListView mListView = new ListView(this);
		mListView.setFadingEdgeLength(0);
		mListView.setOnItemClickListener(acctItemOnClick);
		mListView.setScrollingCacheEnabled(false);
		AnnuitySpinnerAdapter mAdapter = new AnnuitySpinnerAdapter(this, list,position);
		mListView.setAdapter(mAdapter);
		BaseDroidApp.getInstanse().showSpinnerDialog(mListView, title);
	}
	
	/**
	 * 账户列表点击事件
	 */
	private OnItemClickListener acctItemOnClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mIndex = position;
			accItemMap = BociDataCenter.getInstance().getBocinvtAcctList().get(mIndex);
			BaseDroidApp.getInstanse().dismissMessageDialog();
			spAcct.setText(PublicTools.getSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO).get(position));
		}
	};
	
	/** 请求到期产品显示更多 */
	public void requestProductForMore() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pageSize", 15);
		map.put("_refresh", "false");
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		map.put("currentIndex", String.valueOf(loadNumber));
		map.put("accountKey", accItemMap.get(BocInvt.ACCOUNTQUERY_ACCOUNTKEY_RES));
//		Map<String, Object> bocmap = BociDataCenter.getInstance().getBocinvtAcctList().get(mIndex);
//		map.put(Comm.ACCOUNT_TYPE,bocmap.get(Comm.ACCOUNT_TYPE));
//		map.put(BocInvt.BANCACCOUNTNO,bocmap.get(BocInvt.ACCOUNTNO));
//		map.put(BocInvt.IBKNUM,bocmap.get(BocInvt.IBKNUMBER));
//		getHttpTools().requestHttp("PsnXpadDueProductProfitQuery",
//				"requestdeprecateProductListForMoreCallback", map, false);
		biiRequestBody.setMethod("PsnXpadDueProductProfitQuery");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestdeprecateProductListForMoreCallback");
		BaseHttpEngine.showProgressDialog();
	}
	
	/** 请求到期产品显示更多回调 */
	public void requestdeprecateProductListForMoreCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(map)) {
			return;
		}
		recordNumber = Integer.valueOf((String) map.get(BocInvt.PROGRESS_RECORDNUM));
		List<Map<String, Object>> listForMore = (List<Map<String, Object>>) map
				.get(BocInvt.BOCI_LIST_RES);
		if (listForMore == null || listForMore.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.acc_transferquery_null));

			return;
		} else {
			for (Map<String, Object> mapTemp : listForMore) {
				loadNumber++;
				list.add(mapTemp);
			}
			if (loadNumber < recordNumber) {

			} else {
				queryListView.removeFooterView(load_more);
			}
			// 配置适配器
			adapter.notifyDataSetChanged();
		}
	}
	/** 设置查询日期 */
	OnClickListener chooseDateClick = new OnClickListener() {

		@Override
		public void onClick(final View v) {
			TextView tv = (TextView) v;
			String time = tv.getText().toString();
			int startYear = Integer.parseInt(time.substring(0, 4));
			int startMonth = Integer.parseInt(time.substring(5, 7));
			int startDay = Integer.parseInt(time.substring(8, 10));
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(
					DeprecatedProductQueryActivityNew.this, new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							StringBuilder date = new StringBuilder();
							date.append(String.valueOf(year));
							date.append("/");
							int month = monthOfYear + 1;
							date.append(((month < 10) ? ("0" + month)
									: (month + "")));
							date.append("/");
							date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
									: (dayOfMonth + "")));
							// 为日期赋值
							((TextView) v).setText(String.valueOf(date));
						}
					}, startYear, startMonth - 1, startDay);
			dialog.show();
		}
	};
	/**
	 * 中银理财 查询
	 */
	public void queryProduct() {
		startDate = tv_startdate.getText().toString().trim();
		endDate = tv_enddate.getText().toString().trim();

		if (QueryDateUtils.compareDateOneYear(startDate, dateTime)) {
			// 起始日期不能早于系统当前日期一年前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					DeprecatedProductQueryActivityNew.this
					.getString(R.string.acc_check_start_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(endDate, dateTime)) {
			// 结束日期在服务器日期之前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					DeprecatedProductQueryActivityNew.this
					.getString(R.string.acc_check_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(startDate, endDate)) {
			// 开始日期在结束日期之前

		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					DeprecatedProductQueryActivityNew.this
					.getString(R.string.acc_query_errordate));
			return;
		}

		if (QueryDateUtils.compareDateThree(startDate, endDate)) {
			// 起始日期与结束日期最大间隔为三个自然月
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					DeprecatedProductQueryActivityNew.this
					.getString(R.string.acc_check_start_end_date));
			return;
		}
		if (queryListView.getFooterViewsCount() > 0) {
			queryListView.removeFooterView(load_more);
		}
		infos.clear();
//		this.startDate = startDate.replaceAll("/", "");
//		this.endDate = endDate.replaceAll("/", "");
		requestdeprecateProductList(this.startDate, this.endDate, true);
		
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		init();
		BiiHttpEngine.dissMissProgressDialog();
	}
	
	/**
	 * 查询到期产品
	 * @param startDate
	 * @param endDate
	 * @param refresh
	 */
	private void requestdeprecateProductList(String startDate, String endDate,
			boolean refresh) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pageSize", 15);
		map.put("_refresh", refresh + "");
		map.put("accountKey", accItemMap.get(BocInvt.ACCOUNTQUERY_ACCOUNTKEY_RES));
		map.put("currentIndex", 0);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod("PsnXpadDueProductProfitQuery");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestdeprecateProductListCallback");
//		getHttpTools().requestHttp("PsnXpadDueProductProfitQuery",
//				"requestdeprecateProductListCallback", map, false);
		BaseHttpEngine.showProgressDialog();
	}

	@SuppressWarnings("unchecked")
	public void requestdeprecateProductListCallback(Object resultObj) {

		BaseHttpEngine.dissMissProgressDialog();

		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		String recordNumber = (String) map.get(BocInvt.PROGRESS_RECORDNUM);
		List<Map<String, Object>> listFirst = (List<Map<String, Object>>) map.get(BocInvt.BOCI_LIST_RES);
		if (StringUtil.isNullOrEmpty(listFirst)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.acc_transferquery_null));
			return;
		}else{
			list = (List<Map<String, Object>>) map.get(BocInvt.BOCI_LIST_RES);
			if (Integer.valueOf(recordNumber) > 10) {
				queryListView.addFooterView(load_more);
			}
			loadNumber = list.size();
			boci_tratype.setText(spAcctCurrencyText);//交易币种
			boci_date.setText(startDate + "-" + endDate);
			boci_acct.setText(spAcctTypeText);//交易账户
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			rl_query_transfer.setVisibility(View.GONE);
			ll_query_result_header.setVisibility(View.VISIBLE);
			ArrayList<BocProductForDeprecateInfo> infos = new ArrayList<BocProductForDeprecateInfo>();
			for (Map<String, Object> mapInfo : list) {
				infos.add(new BocProductForDeprecateInfo(mapInfo));
			}
			this.infos.addAll(infos);
			initHead();
			// 配置适配器
//			adapter = new MyProductQueryAdapter(this,list);
			adapter = new CommonAdapter<Map<String,Object>>(DeprecatedProductQueryActivityNew.this, list, this);
			queryListView.setAdapter(adapter);
			queryListView.setOnItemClickListener(onListClickListener);
		}
	}
	
	/** 到期产品点击事件 */
	OnItemClickListener onListClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			BaseDroidApp.getInstanse().getBizDataMap().
				put("bocinvtDeprecateDetailMap",list.get(position));
			Intent intent = new Intent(DeprecatedProductQueryActivityNew.this, 
					DeprecateProlductDetailActivity.class);
			startActivity(intent);
		}

	};
	
	/** 查询结果头倒三角点击监听事件 */
	OnClickListener backQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			combinateLayout.setBackgroundResource(R.drawable.img_bg_query_no);
			rl_query_transfer.setVisibility(View.VISIBLE);
			ll_query_result_header.setVisibility(View.GONE);
//			PopupWindowUtils.getInstance().getQueryPopupWindow(
//					rl_query_transfer,
//					BaseDroidApp.getInstanse().getCurrentAct());
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
		}
	};
	private String spAcctTypeText,spAcctCurrencyText;//交易账户  币种 
	@Override
	public void onClick(View v) {
		if (spAcct.getText().toString().equals("请选择账户")) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择理财交易账户"); return;
		}
		spAcctTypeText = spAcct.getText().toString();
		spAcctCurrencyText = spAcctType.getSelectedItem().toString();
		switch (v.getId()) {
		case R.id.btn_combinate_queryproduct://查询
			if (!StringUtil.isNullOrEmpty(list)) {
				list.clear();
			}
			combinateLayout.setBackgroundResource(R.drawable.img_bg_query_j);
			queryProduct();
			break;
		case R.id.btn_acc_onweek:
			// 选择一周
			if (!StringUtil.isNullOrEmpty(list)) {
				list.clear();
			}
			combinateLayout.setBackgroundResource(R.drawable.img_bg_query_j);
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			startDate = QueryDateUtils.getlastWeek(dateTime).trim();
			tv_startdate.setText(startDate);
			tv_enddate.setText(endDate);
			if (queryListView.getFooterViewsCount() > 0) {
				queryListView.removeFooterView(load_more);
			}
			queryProduct();
			break;
		case R.id.btn_acc_onmonth:
			// 选择一个月
			if (!StringUtil.isNullOrEmpty(list)) {
				list.clear();
			}
			combinateLayout.setBackgroundResource(R.drawable.img_bg_query_j);
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			startDate = QueryDateUtils.getlastOneMonth(dateTime).trim();
			tv_startdate.setText(startDate);
			tv_enddate.setText(endDate);
			if (queryListView.getFooterViewsCount() > 0) {
				queryListView.removeFooterView(load_more);
			}
			queryProduct();
			break;
		case R.id.btn_acc_threemonth:
			// 选择三个月
			if (!StringUtil.isNullOrEmpty(list)) {
				list.clear();
			}
			combinateLayout.setBackgroundResource(R.drawable.img_bg_query_j);
			endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
			startDate = QueryDateUtils.getlastThreeMonth(dateTime).trim();
			tv_startdate.setText(startDate);
			tv_enddate.setText(endDate);
			if (queryListView.getFooterViewsCount() > 0) {
				queryListView.removeFooterView(load_more);
			}
			queryProduct();
			break;
		default:
			break;
		}
	}

	/**
	 * spinner初始化
	 */
	private void clickSpinner() {
		ArrayAdapter<String> accountAdapter = new ArrayAdapter<String>(this,
				R.layout.custom_spinner_item, new String[] { "全部", "人民币" });
		accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spAcctType.setAdapter(accountAdapter);
	}
	
	/*
	 * 给列表头赋值
	 */
	private void initHead() {
		boci_product_name.setText(R.string.bocinvt__product_endDate);
		boci_yearlyRR.setText(R.string.bocinvt__product_name2);
		boci_timeLimit.setText(R.string.bocinvt__inverst_cash);
		img_line.setVisibility(View.VISIBLE);
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

	@SuppressWarnings("unchecked")
	public void requestRecentAccountCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (!StringUtil.isNullOrEmpty(result)) {
			recentAccount = (String) result.get(BocInvt.ACCOUNTNO);
		}
		requestSystemDateTime();

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_FIRST_USER:
			if (!PopupWindowUtils.getInstance().isQueryPopupShowing()) {
				combinateLayout.setBackgroundResource(R.drawable.img_bg_query_no);
				rl_query_transfer.setVisibility(View.VISIBLE);
				ll_query_result_header.setVisibility(View.GONE);
//				PopupWindowUtils.getInstance().getQueryPopupWindow(
//						rl_query_transfer, DeprecatedProductQueryActivityNew.this);
//				PopupWindowUtils.getInstance().showQueryPopupWindow();
			}
			break;
		case RESULT_OK:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				// 开通成功的响应
				isOpenBefore = true;
				isOpen = true;
			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE) {
				// 登记账户成功的响应
				investBinding = (List<Map<String, Object>>) BaseDroidApp
						.getInstanse().getBizDataMap()
						.get(ConstantGloble.BOCINVT_XPADRESET_CHOOSE);
				investBindingInfo = (List<Map<String, Object>>) BaseDroidApp
						.getInstanse().getBizDataMap()
						.get(ConstantGloble.BOCINVT_XPADRESET_CHOOSE);
			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE) {
				// 风险评估成功的响应
				if (BociDataCenter.getInstance().getI() == 1) {

				} else {
//					isevaluated = true;
//					isevaluation = true;
				}
			} else {
				if (queryListView.getFooterViewsCount() > 0) {
					queryListView.removeFooterView(load_more);
				}
//				requestdeprecateProductList(this.startDate, this.endDate, true);
				break;
			}
			if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
					/*&& isevaluation*/) {
				requestRecentAccount();
//				BaseHttpEngine.showProgressDialogCanGoBack();
			} else {
				InflaterViewDialog inflater = new InflaterViewDialog(
						DeprecatedProductQueryActivityNew.this);
//				dialogView2 = (RelativeLayout) inflater.judgeViewDialog(isOpen,
//						investBindingInfo, isevaluation, manageOpenClick,
//						invtBindingClick, invtEvaluationClick, exitClick);
				dialogView2 = (RelativeLayout) inflater.judgeViewDialog_choice(isOpen,
						investBindingInfo, true, manageOpenClick, invtBindingClick, null, exitDialogClick);
				TextView tv = (TextView) dialogView2
						.findViewById(R.id.tv_acc_account_accountState);
				// 查询
				tv.setText(this.getString(R.string.bocinvt_tv_76));

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
						DeprecatedProductQueryActivityNew.this);
//				dialogView2 = (RelativeLayout) inflater.judgeViewDialog(isOpen,
//						investBindingInfo, isevaluation, manageOpenClick,
//						invtBindingClick, invtEvaluationClick, exitClick);
				dialogView2 = (RelativeLayout) inflater.judgeViewDialog_choice(isOpen,
						investBindingInfo, true, manageOpenClick, invtBindingClick, null, exitDialogClick);
				TextView tv = (TextView) dialogView2
						.findViewById(R.id.tv_acc_account_accountState);
				// 查询
				tv.setText(this.getString(R.string.bocinvt_tv_76));
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
	 * 账户余额查询
	 */
	public void requestAccBankAccountDetail() {
//		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_BALANCE_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Comm.ACCOUNT_ID, (String)BociDataCenter.getInstance().getBocinvtAcctList()
				.get(mIndex).get(Comm.ACCOUNT_ID));
		biiRequestBody.setParams(paramsmap);
//		BaseHttpEngine.showProgressDialog();
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
		startActivityForResult((new Intent(this, BocBindinglDialogActivity.class).putExtra("p", mIndex)),1004);
	}

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
			requestRecentAccount();
//			BaseHttpEngine.showProgressDialogCanGoBack();
		} else {
			// 得到result
			BaseHttpEngine.dissMissProgressDialog();
			InflaterViewDialog inflater = new InflaterViewDialog(
					DeprecatedProductQueryActivityNew.this);
//			dialogView2 = (RelativeLayout) inflater.judgeViewDialog(
//					isOpenBefore, investBinding, isevaluated, manageOpenClick,
//					invtBindingClick, invtEvaluationClick, exitClick);
			dialogView2 = (RelativeLayout) inflater.judgeViewDialog_choice(isOpen,
					investBindingInfo, true, manageOpenClick, invtBindingClick, null, exitDialogClick);
			TextView tv = (TextView) dialogView2
					.findViewById(R.id.tv_acc_account_accountState);
			// 查询
			tv.setText(this.getString(R.string.bocinvt_tv_76));

			BaseDroidApp.getInstanse().showAccountMessageDialog(dialogView2);

		}
	}
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
//		initfastfoot();
		// 判断用户是否开通投资理财服务
		requestPsnInvestmentisOpenBefore();
	}
//	/** 用于区分是否是快捷键启动 */
//	public void initfastfoot() {
//		//		if (getIntent().getBooleanExtra(ConstantGloble.COMEFROMFOOTFAST, false)) {
//		// 快捷键启动
//		// 判断用户是否开通投资理财服务
//		requestPsnInvestmentisOpenBefore();
//		//		} else {
//		//			requestRecentAccount();
//		//		}
//	}
	/**
	 * 请求conversationId 来登录之外的conversation出Id
	 */
	public void requestCommConversationId() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this,
				"requestCommConversationIdCallBack");
	}
	@Override
	public View getView(int arg0, Map<String, Object> currentItem,
			LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.bocinvt_hispro_list_item, null);
		}
		/** 产品到期日 */
		TextView boci_product_name = (TextView) convertView
				.findViewById(R.id.boci_product_name);
		/** 产品名称 */
		TextView boci_yearlyRR = (TextView) convertView
				.findViewById(R.id.boci_yearlyRR);
		/** 投资本金 */
		TextView boci_timeLimit = (TextView) convertView
				.findViewById(R.id.boci_timeLimit);
		boci_timeLimit.setTextColor(DeprecatedProductQueryActivityNew.this.getResources()
				.getColor(R.color.red));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(DeprecatedProductQueryActivityNew.this,
				boci_yearlyRR);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(DeprecatedProductQueryActivityNew.this,
				boci_timeLimit);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(DeprecatedProductQueryActivityNew.this,
				boci_timeLimit);
		/** 右三角 */
		ImageView goDetail = (ImageView) convertView
				.findViewById(R.id.boci_gotoDetail);
		goDetail.setVisibility(View.VISIBLE);
		// 赋值操作
		boci_product_name.setText(String.valueOf(currentItem.get(BocInvt.BOCINVT_EDATE_RES)));
		boci_yearlyRR.setText(String.valueOf(currentItem.get(BocInvt.BOCINVT_PRONAME_RES)));
		String timeLimit = (String) currentItem.get(BocInvt.BOCINVT_AMOUNT_RES);
		String currency = (String) currentItem.get(BocInvt.BOCINVT_PROCUR_RES);
		boci_timeLimit.setText(StringUtil.parseStringCodePattern(currency,
					timeLimit, 2));
		return convertView;
	}
}
