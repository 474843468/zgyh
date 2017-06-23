package com.chinamworld.bocmbci.biz.bond.historytran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

/**
 * 交易列表
 * 
 * @author panwe
 * 
 */
public class HistoryTransferListActivity extends BondBaseActivity implements
		OnClickListener {

	/** 主布局 */
	private View mainView;
	/** 列表布局 */
	private LinearLayout queryLayout;
	private LinearLayout resultLayout;
	private LinearLayout listLayout;
	/** 债券类型 */
	private Spinner spBondType;
	/** 查询条件按钮 */
	private RadioButton btnOneWeek;
	private RadioButton btnOneMonth;
	private RadioButton btnThreeMonth;
	/** 下拉 、弹起 */
	private LinearLayout upLayout;
	private LinearLayout downLayout;
	/** 查询时间 */
	private TextView tvQueryDate;
	/** 子定义时间 */
	private TextView tvStartDate, tvEndDate;
	/** 债券类型 */
	private TextView tvBondType;
	/** listvirew */
	private ListView listView;
	private View vLine;
	/** 查询按钮 */
	private Button btnQuery;
	/** 债券名称 */
	private TextView tvBondName;
	private RelativeLayout queryCondition;
	private PopupWindowUtils mPopupWindowUtils;
	/** 查询列表数据 */
	private List<Map<String, Object>> mList;

	private String startDate;
	private String endDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setLeftSelectedPosition("bond_3");
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_historytran_list, null);
		addView(mainView);
		btnRight.setVisibility(View.GONE);
		setTitle(this.getString(R.string.bond_transtatue_title));
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}
		// 请求是否开通投资理财
		requestPsnInvestmentManageIsOpen();
	}

//	private void initPupoWindow() {
////		queryCondition = (RelativeLayout) LayoutInflater.from(this).inflate(
////				R.layout.bond_historytrade_query_condition, null);
////		mPopupWindowUtils = PopupWindowUtils.getInstance();
////		mPopupWindowUtils.getQueryPopupWindow(queryCondition, BaseDroidApp
////				.getInstanse().getCurrentAct());
////		mPopupWindowUtils.showQueryPopupWindowFirst();
//		init();
//	}

	private void init() {
		queryLayout=(LinearLayout) mainView.findViewById(R.id.query_condition);
		listLayout = (LinearLayout) mainView.findViewById(R.id.layout_tl);
		spBondType = (Spinner) mainView.findViewById(R.id.sp_bondtype);
		btnOneWeek = (RadioButton) mainView.findViewById(R.id.btn_acc_onweek);
		btnOneMonth = (RadioButton) mainView
				.findViewById(R.id.btn_acc_onmonth);
		btnThreeMonth = (RadioButton) mainView
				.findViewById(R.id.btn_threemoth);
		upLayout = (LinearLayout) mainView.findViewById(R.id.ll_up);
		downLayout = (LinearLayout) mainView.findViewById(R.id.layoudown);

		btnOneWeek.setOnClickListener(this);
		btnOneMonth.setOnClickListener(this);
		btnThreeMonth.setOnClickListener(this);
		upLayout.setOnClickListener(this);
		upLayout.setClickable(false);
		downLayout.setOnClickListener(this);

		tvQueryDate = (TextView) mainView
				.findViewById(R.id.tv_acc_query_date_value);
		tvBondType = (TextView) mainView.findViewById(R.id.tv_bond_type);
		tvBondName=(TextView) mainView.findViewById(R.id.tvtitle_type);
		tvBondName.setEllipsize(TruncateAt.MIDDLE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvBondName);
		listView = (ListView) mainView.findViewById(R.id.listview);
		listView.setOnItemClickListener(itemClick);
		vLine = (View) mainView.findViewById(R.id.view_line);

		tvStartDate = (TextView) mainView
				.findViewById(R.id.acc_query_transfer_startdate);
		tvEndDate = (TextView) mainView
				.findViewById(R.id.acc_query_transfer_enddate);
		tvStartDate.setText(QueryDateUtils.getlastthreeDate(BondDataCenter
				.getInstance().getSysTime()));
		tvStartDate.setOnClickListener(chooseDateClick);
		tvEndDate.setText(QueryDateUtils.getcurrentDate(BondDataCenter
				.getInstance().getSysTime()));
		tvEndDate.setOnClickListener(chooseDateClick);

		btnQuery = (Button) mainView
				.findViewById(R.id.btn_acc_query_transfer);
		btnQuery.setOnClickListener(this);
//		resultLayout = (LinearLayout) mainView
//				.findViewById(R.id.ll_query_result);
		resultLayout=(LinearLayout) mainView.findViewById(R.id.acc_query_result_condition);
		spInit();
		// 历史交易查询
//		requestHistoryTran(QueryDateUtils.getlastthreeDate(BondDataCenter
//				.getInstance().getSysTime()),
//				QueryDateUtils.getcurrentDate(BondDataCenter.getInstance()
//						.getSysTime()));

//		tvQueryDate.setText(QueryDateUtils.getlastthreeDate(BondDataCenter
//				.getInstance().getSysTime())
//				+ "-"
//				+ QueryDateUtils.getcurrentDate(BondDataCenter.getInstance()
//						.getSysTime()));
	}

	/** 初始化查询条件下拉框 */
	private void spInit() {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayAdapter<ArrayList<String>> bondTypeAdapter = new ArrayAdapter(
				this, R.layout.custom_spinner_item, BondDataCenter
						.getInstance().bondType());
		bondTypeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spBondType.setAdapter(bondTypeAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 一周
		case R.id.btn_acc_onweek:
			listLayout.setVisibility(View.GONE);
			requestHistoryTran(QueryDateUtils.getlastWeek(BondDataCenter
					.getInstance().getSysTime()),
					QueryDateUtils.getcurrentDate(BondDataCenter.getInstance()
							.getSysTime()));

			tvQueryDate.setText(QueryDateUtils.getlastWeek(BondDataCenter
					.getInstance().getSysTime())
					+ "-"
					+ QueryDateUtils.getcurrentDate(BondDataCenter
							.getInstance().getSysTime()));
			break;

		// 一月
		case R.id.btn_acc_onmonth:
			listLayout.setVisibility(View.GONE);
			requestHistoryTran(QueryDateUtils.getlastOneMonth(BondDataCenter
					.getInstance().getSysTime()),
					QueryDateUtils.getcurrentDate(BondDataCenter.getInstance()
							.getSysTime()));

			tvQueryDate.setText(QueryDateUtils.getlastOneMonth(BondDataCenter
					.getInstance().getSysTime())
					+ "-"
					+ QueryDateUtils.getcurrentDate(BondDataCenter
							.getInstance().getSysTime()));
			break;

		// 三月
		case R.id.btn_threemoth:
			listLayout.setVisibility(View.GONE);
			requestHistoryTran(QueryDateUtils.getlastThreeMonth(BondDataCenter
					.getInstance().getSysTime()),
					QueryDateUtils.getcurrentDate(BondDataCenter.getInstance()
							.getSysTime()));

			tvQueryDate.setText(QueryDateUtils.getlastThreeMonth(BondDataCenter
					.getInstance().getSysTime())
					+ "-"
					+ QueryDateUtils.getcurrentDate(BondDataCenter
							.getInstance().getSysTime()));
			break;

		// 查询
		case R.id.btn_acc_query_transfer:
			listLayout.setVisibility(View.GONE);
			if (check()) {
				requestHistoryTran(tvStartDate.getText().toString(), tvEndDate
						.getText().toString());

				tvQueryDate.setText(tvStartDate.getText().toString() + "-"
						+ tvEndDate.getText().toString());
			} else {
				upLayout.setClickable(false);
			}
			break;

		case R.id.ll_up:
//			mPopupWindowUtils.dissMissQueryPopupWindow();
			queryLayout.setVisibility(View.GONE);
			resultLayout.setVisibility(View.VISIBLE);
			break;

		case R.id.layoudown:
//			mPopupWindowUtils.getQueryPopupWindow(queryCondition, this);
//			mPopupWindowUtils.showQueryPopupWindow();
			resultLayout.setVisibility(View.GONE);
			queryLayout.setVisibility(View.VISIBLE);
			break;
		}
	}

	private boolean check() {
		dateTime = BondDataCenter.getInstance().getSysTime();
		startDate = tvStartDate.getText().toString().trim();
		endDate = tvEndDate.getText().toString().trim();
		// 起始日期不能早于系统当前日期一年前
		if (!QueryDateUtils.compareDateOneYear(startDate, dateTime)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					HistoryTransferListActivity.this
							.getString(R.string.acc_check_start_enddate));
			return false;
		}
		// 结束日期在服务器日期之前
		if (!QueryDateUtils.compareDate(endDate, dateTime)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					HistoryTransferListActivity.this
							.getString(R.string.acc_check_enddate));
			return false;
		}
		// 开始日期在结束日期之前
		if (!QueryDateUtils.compareDate(startDate, endDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					HistoryTransferListActivity.this
							.getString(R.string.acc_query_errordate));
			return false;
		}
		// 起始日期与结束日期最大间隔为三个自然月
		if (!QueryDateUtils.compareDateThree(startDate, endDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					HistoryTransferListActivity.this
							.getString(R.string.acc_check_start_end_date));
			return false;
		}
		return true;

	}

	/** 列表点击事件 */
	private OnItemClickListener itemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			BondDataCenter.getInstance().setHistoryDetailMap(
					mList.get(position));
			Intent it = new Intent(HistoryTransferListActivity.this,
					HistoryTransferInfoActivity.class);
			HistoryTransferListActivity.this.startActivity(it);
		}
	};

	/** 设置日期 */
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
					HistoryTransferListActivity.this, new OnDateSetListener() {

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
	 * 历史对账单查询
	 * 
	 * @param startDate
	 * @param endDate
	 */
	private void requestHistoryTran(String startDate, String endDate) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_BOND_HISTORYTRAN_QUERY);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(Bond.BOND_TYPE, BondDataCenter.bondType_re.get(1));
		param.put(Bond.BOND_HISTORYTRAN_QUERY_STARTDATE, startDate);
		param.put(Bond.BOND_HISTORYTRAN_QUERY_ENDDATE, endDate);
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "historyTranListCallBack");
	}

	/** 历史对账单返回 */
	@SuppressWarnings("unchecked")
	public void historyTranListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BaseHttpEngine.dissMissProgressDialog();
		mList = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(mList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.bond_comm_error));
			upLayout.setClickable(false);
			return;
		}
//		mPopupWindowUtils.dissMissQueryPopupWindow();
		queryLayout.setVisibility(View.GONE);
		upLayout.setClickable(true);
		resultLayout.setVisibility(View.VISIBLE);
		listLayout.setVisibility(View.VISIBLE);
		vLine.setVisibility(View.VISIBLE);
		tvBondType.setText((String) spBondType.getSelectedItem());
		initList();
	}

	/** 初始化列表数据 */
	private void initList() {
//		BondHistoryAdapter mAdapter=new BondHistoryAdapter(this, mList);
//		listView.setAdapter(mAdapter);
//		
		CommonAdapter<Map<String, Object>> adapte = new CommonAdapter<Map<String, Object>>(this, mList,new ICommonAdapter<Map<String, Object>>(){

			@Override
			public View getView(int arg0, Map<String, Object> currentItem,
					LayoutInflater inflater, View convertView,
					ViewGroup viewGroup) {
				ViewHolder vHolder=null;
				if(convertView==null){
					convertView=inflater.inflate(R.layout.bond_historytran_item, null);
					vHolder=new ViewHolder();
					vHolder.tvBondDate=(TextView) convertView.findViewById(R.id.tv_tran_date);
					vHolder.tvBondName=(TextView) convertView.findViewById(R.id.tv_bond_name);
					vHolder.tvBondType=(TextView) convertView.findViewById(R.id.tv_bond_type);
					convertView.setTag(vHolder);
				}else{
					vHolder=(ViewHolder) convertView.getTag();
				}
				vHolder.tvBondDate.setText((String) currentItem.get(Bond.RE_HISTORYTRAN_QUERY_TRANDATE));
				vHolder.tvBondName.setText((String) currentItem.get(Bond.BOND_SHORTNAME));
				vHolder.tvBondName.setEllipsize(TruncateAt.MIDDLE);
				PopupWindowUtils.getInstance().setOnShowAllTextListener(HistoryTransferListActivity.this, vHolder.tvBondName);
				vHolder.tvBondType.setText((String) currentItem.get(Bond.RE_HISTORYTRAN_QUERY_TRANTYPE));
				return convertView;
			}
		
		});
		listView.setAdapter(adapte);
		
	}

	/** 系统时间 */
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		if (StringUtil.isNull(dateTime)) {
			return;
		}
		BondDataCenter.getInstance().setSysTime(dateTime);
		BaseHttpEngine.dissMissProgressDialog();
		// 初始化页面
		init();
	}

	class ViewHolder{
		TextView tvBondDate;
		TextView tvBondName;
		TextView tvBondType;
	}
	
}
