package com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银理财-预计年收益率页
 * @author Administrator
 *
 */
public class ProductQueryAndBuyYearRateActivity extends BocInvtBaseActivity{

	/**存储客户类型及相应类型下的table_row*/
	private Map<String, ViewGroup> map_custLevel=new HashMap<String, ViewGroup>();
	private LayoutInflater inflater;
	private View view_bottom_left,view_bottom_right;
	private Map<String, Object> detailMap;
	private String after_date;
	private Calendar calendar=Calendar.getInstance();
	@SuppressLint("SimpleDateFormat")
	private DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
	@SuppressLint("SimpleDateFormat")
	private DateFormat dateFormat1=new SimpleDateFormat("yyyy年MM月dd日");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater=LayoutInflater.from(this);
		initBaseLayout();
		setContentView(R.layout.product_query_and_buy_year_rate);
		initDate();
		initUI();
	}
	
	/**
	 * 组件初始化及赋值
	 */
	private void initUI(){
		//初始化
		TextView tv_1 = (TextView) findViewById(R.id.tv_1);
		TextView tv_2 = (TextView) findViewById(R.id.tv_2);
		tv_3 = (TextView) findViewById(R.id.tv_3);
		layout_add_table_layout = (ViewGroup) findViewById(R.id.layout_add_table_layout);
		tv_4 = (TextView) findViewById(R.id.tv_4);
		ImageView img_minus = (ImageView) findViewById(R.id.img_minus);
		ImageView img_plus = (ImageView) findViewById(R.id.img_plus);
		//赋值
		tv_1.setText(detailMap.get(BocInvt.BOCINVT_BUYRES_PRODCODE_RES).toString());
		tv_2.setText(detailMap.get(BocInvt.BOCINVT_BUYRES_PRODNAME_RES).toString());
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_2);
		tv_3.setText(getCurrentDate());
		tv_3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				TextView tv=(TextView) v;
				String tv_date = tv.getText().toString().trim();
				if (StringUtil.isNullOrEmpty(tv_date)) {
					return;
				}
				int startYear = Integer.parseInt(tv_date.substring(0, 4));
				int startMonthOfYear = Integer.parseInt(tv_date.substring(5, 7));
				int startDayOfMonth = Integer.parseInt(tv_date.substring(8, 10));
				DatePickerDialog datePickerDialog = new DatePickerDialog(ProductQueryAndBuyYearRateActivity.this, new OnDateSetListener(){
					@Override
					public void onDateSet(DatePicker view, int year1,int monthOfYear, int dayOfMonth) {
						StringBuilder date = new StringBuilder();
						date.append(String.valueOf(year1));
						date.append("年");
						int month1 = monthOfYear + 1;
						date.append(((month1 < 10) ? ("0" + month1): (month1 + "")));
						date.append("月");
						date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth): (dayOfMonth + "")));
						date.append("日");
//						((TextView) v).setText(date.toString());
						after_date=date.toString();
//						year=year1;
//						month=monthOfYear+1;
//						day=dayOfMonth;
						if (isLimit(current_date)) {
							requestPsnXpadExpectYieldQuery(after_date);
						}
					}
				},startYear, startMonthOfYear-1, startDayOfMonth);
				datePickerDialog.show();
			}
		});
		img_minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					after_date=getLastDate(tv_3.getText().toString(),false);
					if (isLimit(current_date)) {
						requestPsnXpadExpectYieldQuery(after_date);
					}
			}
		});
		img_plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					after_date=getLastDate(tv_3.getText().toString(),true);
					if (isLimit(current_date)) {
						requestPsnXpadExpectYieldQuery(after_date);
					}
			}
		});
		requestPsnXpadExpectYieldQuery(tv_3.getText().toString());
	}
	/**
	 * 判断查询日期是否在1年范围内
	 * @param str 进入页面时的当前系统时间(即：基数时间)，时间格式：2016/01/01
	 * @return true/在一年范围内,响应用户点击查询、false/超出当前时间开始往前1年范围,不响应点击查询事件
	 */
	private boolean isLimit(String str){
		if (StringUtil.isNullOrEmpty(str)) {
			LogGloble.e("预计年收益率查询", "查询时间为空");
			return false;
		}
		try {
			String str_1 = str.replace("年", "/").replace("月", "/").replace("日", "");
			String str_2 = after_date.replace("年", "/").replace("月", "/").replace("日", "");
			Date date_current = dateFormat.parse(str_1);
			Date date_choice = dateFormat.parse(str_2);
			calendar.setTime(date_current);
			calendar.add(Calendar.YEAR, -1);
			Date date_last_year = calendar.getTime();
			if (!date_choice.after(date_current)&&date_choice.after(date_last_year)) {
				return true;
			}else {
				BaseDroidApp.getInstanse().showInfoMessageDialog("查询范围为1年");
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	private String getCurrentDate(){
		current_date=BocInvestControl.SYSTEM_DATE;
		after_date = current_date.substring(0,4)+"年"+current_date.substring(5,7)+"月"+current_date.substring(8,10)+"日";
		current_date = after_date;
		return after_date;
	}
	private void addConcentForTableLayout(Map<String, Object> map){
		if (StringUtil.isNullOrEmpty(map_custLevel.get(map.get("custLevel").toString()))) {
			addTableLayout(map);
		}else {
			addTableRow(map);
		}
	}
	private void addTableRow(Map<String, Object> map){
		ViewGroup viewGroup = map_custLevel.get(map.get("custLevel").toString());
		View tableRow = inflateTableRow(map);
		viewGroup.addView(tableRow);
	}
	private void addTableLayout(Map<String, Object> map){
		View table_layout = inflater.inflate(R.layout.product_query_and_buy_year_rate_table_layout, layout_add_table_layout,false);
		TextView tv_table_layout_custom_type = (TextView) table_layout.findViewById(R.id.tv_table_layout_custom_type);
		ViewGroup layout_add_table_row = (ViewGroup) table_layout.findViewById(R.id.layout_add_table_row);
		View tableRow = inflateTableRow(map);
		//赋值
		tv_table_layout_custom_type.setText(BocInvestControl.map_custLevel_code_key.get(map.get("custLevel").toString()));
		layout_add_table_row.addView(tableRow);
		layout_add_table_layout.addView(table_layout);
		map_custLevel.put(map.get("custLevel").toString(), layout_add_table_row);
		view_bottom_left=tv_table_layout_custom_type;
	}
	private View inflateTableRow(Map<String, Object> map){
		View product_query_and_buy_year_rate_table_row = inflater.inflate(R.layout.product_query_and_buy_year_rate_table_row, map_custLevel.get(map.get("custLevel").toString()),false);
		TextView tv_table_row_left = (TextView) product_query_and_buy_year_rate_table_row.findViewById(R.id.tv_table_row_left);
		TextView tv_table_row_right = (TextView) product_query_and_buy_year_rate_table_row.findViewById(R.id.tv_table_row_right);
		tv_table_row_left.setText(StringUtil.parseStringCodePattern((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), map.get("minAmt").toString(), 2)+"(含)"+getMaxAmt(map.get("maxAmt").toString()));
		tv_table_row_right.setText(StringUtil.append2Decimals(map.get("rates").toString(), 2)+"%");
		view_bottom_right=tv_table_row_right;
		return product_query_and_buy_year_rate_table_row;
	}
	private String getMaxAmt(String	str){
		if (str.equals("-1")) {
			return "以上";
		}
		return "-"+StringUtil.parseStringCodePattern((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), str, 2);
	}
	/**
	 * 请求 预计年收益率
	 * @param str_query_date 要查询的日期，格式：XXXX年XX月XX日
	 */
	private void requestPsnXpadExpectYieldQuery(String str_query_date){
		Map<String, Object> parms_map=new HashMap<String, Object>();
		parms_map.put(BocInvt.BOCINVT_SIGNINIT_PRODUCTCODE_REQ, detailMap.get(BocInvt.BOCINVT_HOLDPRO_PRODCODE_RES).toString());
		parms_map.put("queryDate", str_query_date.replace("年", "/").replace("月", "/").replace("日", ""));//查询日期
		BaseHttpEngine.showProgressDialog();
		getHttpTools().requestHttp(BocInvestControl.PSNXPADEXPECTYIELDQUERY, "requestPsnXpadExpectYieldQueryCallBack", parms_map, true);
	}
	/**
	 * 请求 预计年收益率 回调
	 * @param resultObj
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void requestPsnXpadExpectYieldQueryCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultObj)) {
			return;
		}
		Map<String, Object> responseDeal = (Map<String, Object>)getHttpTools().getResponseResult(resultObj);
		tv_3.setText(after_date);
		if (responseDeal.get("isLowProfit").toString().equals("1")) {//是否允许转低收益产品,0：否1：是
			tv_4.setVisibility(View.VISIBLE);
			tv_4.setText(tv_3.getText().toString()+"，普通份额预计收益率为："+StringUtil.append2Decimals(responseDeal.get("exYield").toString(), 2)+"%");
		}else {
			tv_4.setVisibility(View.GONE);
		}
		List<Map<String, Object>> list_result = (List<Map<String, Object>>) responseDeal.get(BocInvestControl.key_list);
		map_custLevel.clear();
		layout_add_table_layout.removeAllViews();
		for (Map<String, Object> map : list_result) {
			addConcentForTableLayout(map);
		}
		view_bottom_left.setBackgroundResource(R.drawable.shape_round_bottom_left);//最后一条记录设置圆角
		view_bottom_right.setBackgroundResource(R.drawable.shape_round_bottom_right);//最后一条记录设置圆角
	}
	/**
	 * 点击"左、右"箭头，返回点击后的日期
	 * @param currentDate 当前日期,要求格式11位，2012*09*08*
	 * @param plusOrMinus true/增加一天、false/减少一天
	 * @return 返回""或11位日期，返回日期格式2012年09月08日
	 */
	private String getLastDate(String currentDate,boolean plusOrMinus){
		String str_replace_date = currentDate.replace("年", "/").replace("月", "/").replace("日", "");
		try {
			calendar.setTime(dateFormat.parse(str_replace_date));
			calendar.add(Calendar.DAY_OF_MONTH, plusOrMinus?1:-1);
			return dateFormat1.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return currentDate;
	}
	/**
	 * 初始化数据
	 */
	private void initDate(){
//		chooseMap = BociDataCenter.getInstance().getChoosemap();
		detailMap=BociDataCenter.getInstance().getDetailMap();
//		accound_map=BocInvestControl.accound_map;
//		result_agreement=BocInvestControl.result_agreement;
//		map_agreement_detail=BocInvestControl.map_agreement_detail;
//		map_listview_choose=BocInvestControl.map_listview_choose;
	}
	/**
	 * 初始化基类布局
	 */
	private void initBaseLayout(){
		setLeftButtonPopupGone();
		getBackgroundLayout().setTitleText("预计年收益率");
		getBackgroundLayout().setLeftButtonText("返回");
		getBackgroundLayout().setLeftButtonClickListener(backClickListener);
	}
	/**
	 * 监听事件，返回
	 */
	private OnClickListener backClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	private TextView tv_4;
	private ViewGroup layout_add_table_layout;
	private TextView tv_3;
	private String current_date;

}
