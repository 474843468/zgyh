package com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银理财-收益试算页
 * @author Administrator
 */
public class ProductQueryAndBuyProfitComputeActivity extends BocInvtBaseActivity{

	private Map<String, Object> chooseMap;
	private Map<String, Object> detailMap;
//	private Map<String, Object> accound_map;
	private boolean isfirst;//	false/进入页面不请求、true/进入页面需请求
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBaseLayout();
		setContentView(R.layout.product_query_and_buy_profit_count);
		initDate();
		initUI();
		if (isfirst) {
			//2016/3/24经确认进入页面后不再做收益试算请求，用户点击时才做试算,2016/04/27业务确认固定期限类产品如果能取到默认值则进行一次默认试算
			requestPsnXpadProfitCount();
		}
	}
	/**
	 * 组件初始化及赋值
	 */
	private void initUI(){
		//初始化
		Button btn_compute = (Button) findViewById(R.id.btn_compute);
//		tv_title_profit_compute = (TextView) findViewById(R.id.tv_title_profit_compute);
		layout_public = findViewById(R.id.layout_public);
		et_11 = (EditText) findViewById(R.id.et_11);
		et_12 = (EditText) findViewById(R.id.et_12);
		layout_1 = findViewById(R.id.layout_1);
		et_13 = (EditText) findViewById(R.id.et_13);
		et_14 = (EditText) findViewById(R.id.et_14);
		tv_11 = (TextView) findViewById(R.id.tv_11);
		layout_2 = findViewById(R.id.layout_2);
		et_21 = (EditText) findViewById(R.id.et_21);
		et_22 = (EditText) findViewById(R.id.et_22);
		et_24 = (EditText) findViewById(R.id.et_24);
		et_211 = (EditText) findViewById(R.id.et_211);
		et_212 = (EditText) findViewById(R.id.et_212);
		et_214 = (EditText) findViewById(R.id.et_214);
		tv_21 = (TextView) findViewById(R.id.tv_21);
		//赋值
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_yearRR_1));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_yearRR_2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_yearRR_3));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_21));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_11));
		initLayoutAndView();
		btn_compute.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					if (resultRegex()) {
						//校验通过
						requestPsnXpadProfitCount();
					}
			}
		});
	}
	/**
	 * 输入值校验
	 * @return
	 */
	private boolean resultRegex(){
		ArrayList<RegexpBean> list_rb = new ArrayList<RegexpBean>();
		switch (profit_compute_type) {
		case 1:{
			list_rb.add(new RegexpBean("预计年化收益率", et_21.getText().toString().trim(), "amount1"));
			list_rb.add(new RegexpBean("产品期限", et_22.getText().toString().trim(), "sixNumber"));
			list_rb.add(BocInvestControl.getRegexpBean((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), 
					"投资金额", et_24.getText().toString().trim(),null/*"profitCompute"*/));
			if (!et_211.getText().toString().trim().equals("")) {
				list_rb.add(new RegexpBean("预计年化收益率", et_211.getText().toString().trim(), "amount1"/*"profitCompute"*/));
			}
			if (!et_212.getText().toString().trim().equals("")) {
				list_rb.add(new RegexpBean("产品期限", et_212.getText().toString().trim(), "sixNumber"));
			}
			if (!et_214.getText().toString().trim().equals("")) {
				list_rb.add(BocInvestControl.getRegexpBean((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), 
						"投资金额", et_214.getText().toString().trim(),null/*"profitCompute"*/));
			}
		}break;
		case 2:{addRegex(list_rb);}break;
		case 3:{addRegex1(list_rb);}break;
		case 4:{addRegex1(list_rb);}break;
		case 5:{addRegex(list_rb);}break;
		case 6:{addRegex(list_rb);}break;
		case 7:{addRegex(list_rb);}break;
		}
		if (RegexpUtils.regexpDate(list_rb)) {//校验通过
			return true;
		}
		return false;
	}
	private void addRegex(ArrayList<RegexpBean> list_rb){
		list_rb.add(new RegexpBean("预计年化收益率", et_11.getText().toString().trim(), "profitCompute"));
		list_rb.add(new RegexpBean("产品期限", et_12.getText().toString().trim(), "sixNumber"));
		list_rb.add(BocInvestControl.getRegexpBean((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES),
				"投资金额", et_14.getText().toString().trim(), null));
	}
	private void addRegex1(ArrayList<RegexpBean> list_rb){
		list_rb.add(new RegexpBean("预计年化收益率", et_11.getText().toString().trim(), "profitCompute"));
		list_rb.add(new RegexpBean("产品期限", et_12.getText().toString().trim(), "sixNumber"));
		list_rb.add(BocInvestControl.getRegexpBean((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES),
				"投资金额", et_14.getText().toString().trim(), null));
		if (((String)detailMap.get("periodical")).equals("1")) {//是否周期性产品    0：否1：是
			list_rb.add(new RegexpBean("投资期数", et_13.getText().toString().trim(), "sixNumberAndOne"));
		}else {
			list_rb.add(new RegexpBean("投资期数", et_13.getText().toString().trim(), "sixNumberAndOne1"));
		}
	}
	/**
	 * 判断该加载哪种试算类型界面及字段
	 */
	private void initLayoutAndView(){
		if (detailMap.get(BocInvestControl.PRODUCTKIND).toString().equals("0")) {//结构性理财
			switch (Integer.parseInt(detailMap.get("isLockPeriod").toString())) {
			case 0://非业绩基准产品
			{
				initLayoutAndView1();
			}
			break;
			case 1://业绩基准-锁定期转低收益 
			{
				profit_compute_type=1;
				layout_2.setVisibility(View.VISIBLE);
				//赋值
//				et_21.setText("");
				if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES).toString().equals("0")//0:结构性理财产品
						&&!detailMap.get("isLockPeriod").toString().equals("0")//0：非业绩基准产品
						||detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES).toString().equals("0")
						&&detailMap.get("isLockPeriod").toString().equals("0")
						&&!detailMap.get("productTermType").toString().equals("3")) {
					et_22.setText(detailMap.get(BocInvt.BOCI_DETAILPRODTIMELIMIT_RES).toString());
				}
				et_24.setText(BocInvestControl.getMoney((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), detailMap.get(BocInvt.SUBAMOUNT).toString()));
				et_214.setText(BocInvestControl.getMoney((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), detailMap.get(BocInvt.SUBAMOUNT).toString()));
				if (!StringUtil.isNullOrEmpty(et_21.getText().toString().trim())&&!StringUtil.isNullOrEmpty(et_22.getText().toString().trim())
						&&!StringUtil.isNullOrEmpty(et_24.getText().toString().trim())) {
					isfirst=true;
				}
				//				isfirst=true;
			}
			break;
			case 2://业绩基准-锁定期后/入账
			{
				profit_compute_type=2;
				layout_public.setVisibility(View.VISIBLE);
				//赋值
				setContentForView();
//				et_11.setText("");
//				isfirst=true;
				if (!StringUtil.isNullOrEmpty(et_11.getText().toString().trim())&&!StringUtil.isNullOrEmpty(et_12.getText().toString().trim())
						&&!StringUtil.isNullOrEmpty(et_14.getText().toString().trim())) {
					isfirst=true;
				}
			}
			break;
			case 3://业绩基准-锁定期周期滚续
			{
				profit_compute_type=3;
				layout_public.setVisibility(View.VISIBLE);
				layout_1.setVisibility(View.VISIBLE);
				//赋值
				setContentForView();
//				et_11.setText("");
				et_13.setText("1");
//				isfirst=true;
				if (!StringUtil.isNullOrEmpty(et_11.getText().toString().trim())&&!StringUtil.isNullOrEmpty(et_12.getText().toString().trim())
						&&!StringUtil.isNullOrEmpty(et_14.getText().toString().trim())) {
					isfirst=true;
				}
			}
			break;
			default:
				break;
			}
		}else {
			initLayoutAndView1();
		}
	}
	/**
	 * 初始化非业绩基准型产品界面及字段
	 */
	private void initLayoutAndView1(){
		switch (Integer.parseInt(detailMap.get("progressionflag").toString())) {
		case 0://不是收益累进产品
		{
			switch (Integer.parseInt(chooseMap.get("termType").toString())) {
			case 2://对应表内周期续约产品的收益试算 
			{
				profit_compute_type=4;
				layout_public.setVisibility(View.VISIBLE);
				layout_1.setVisibility(View.VISIBLE);
				//赋值
				setContentForView();
				et_13.setText("1");
				et_13.setHint("请输入");
				if (!StringUtil.isNullOrEmpty(et_11.getText().toString().trim())&&!StringUtil.isNullOrEmpty(et_12.getText().toString().trim())
						&&!StringUtil.isNullOrEmpty(et_14.getText().toString().trim())) {
					isfirst=true;
				}
			}
			break;
			case 3://对应日积月累产品的收益试算
			{
				profit_compute_type=5;
				layout_public.setVisibility(View.VISIBLE);
				//赋值
				setContentForView();
//				et_12.setText("");
				if (!StringUtil.isNullOrEmpty(et_11.getText().toString().trim())&&!StringUtil.isNullOrEmpty(et_12.getText().toString().trim())
						&&!StringUtil.isNullOrEmpty(et_14.getText().toString().trim())) {
					isfirst=true;
				}
			}
			break;
			case 4:
				break;
			default://对应固定期限产品的收益试算
			{
				profit_compute_type=6;
				layout_public.setVisibility(View.VISIBLE);
				//赋值
				setContentForView();
				if (!StringUtil.isNullOrEmpty(et_11.getText().toString().trim())&&!StringUtil.isNullOrEmpty(et_12.getText().toString().trim())
						&&!StringUtil.isNullOrEmpty(et_14.getText().toString().trim())) {
					isfirst=true;
				}
			}
			break;
			}
		}
		break;
		case 1://是收益累进产品
		{
			profit_compute_type=7;
			layout_public.setVisibility(View.VISIBLE);
			//赋值
			setContentForView();
//			et_12.setText("");
			if (!StringUtil.isNullOrEmpty(et_11.getText().toString().trim())&&!StringUtil.isNullOrEmpty(et_12.getText().toString().trim())
					&&!StringUtil.isNullOrEmpty(et_14.getText().toString().trim())) {
				isfirst=true;
			}
		}
		break;
		default:
			break;
		}
	}
	/**
	 * 公共输入框赋值
	 */
	private void setContentForView(){
		setYearRR(et_11);
		if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES).toString().equals("0")//0:结构性理财产品
				&&(!detailMap.get("isLockPeriod").toString().equals("0")//0：非业绩基准产品
				||(detailMap.get("isLockPeriod").toString().equals("0")
				&&!detailMap.get("productTermType").toString().equals("3")))) {
			et_12.setText(detailMap.get(BocInvt.BOCI_DETAILPRODTIMELIMIT_RES).toString());
		}
		tv_11.setText(BocInvestControl.getMoney(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString(), "0"));
		et_14.setText(BocInvestControl.getMoney((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES),detailMap.get(BocInvt.SUBAMOUNT).toString()));
	}
	/**
	 * 设置预计年收益率反显
	 */
	private void setYearRR(EditText edt){
		Object obj_yearRR = detailMap.get(BocInvt.BOCI_DETAILYEARLYRR_RES);
		Object obj_max = detailMap.get(BocInvestControl.RATEDETAIL);
		if (!StringUtil.isNullOrEmpty(obj_max)&&Double.parseDouble(obj_max.toString())>Double.parseDouble("0")) {
			edt.setText("");
			return;
		}
		if (!StringUtil.isNullOrEmpty(obj_yearRR)&&Double.parseDouble(obj_yearRR.toString())>Double.parseDouble("0")) {
			edt.setText(StringUtil.append2Decimals(detailMap.get(BocInvt.BOCI_DETAILYEARLYRR_RES).toString(), 2));
			return;
		}
		edt.setText("");
	}
	/**
	 * 请求 收益试算
	 */
	private void requestPsnXpadProfitCount(){
		Map<String, Object> parms_map=new HashMap<String, Object>();
			addParms(parms_map);
		BaseHttpEngine.showProgressDialog();
		getHttpTools().requestHttp(BocInvestControl.PSNXPADPROFITCOUNT, "requestPsnXpadProfitCountCallBack", parms_map, false);
	}
	private void addParms(Map<String, Object> parms_map){
		switch (profit_compute_type) {
		case 1:{
			parms_map.put("exyield", et_21.getText().toString());
			parms_map.put("dayTerm", et_22.getText().toString());
			parms_map.put("puramt", et_24.getText().toString());
			parms_map.put("commonExyield", et_211.getText().toString());//普通份额预计年收益率（%）
			parms_map.put("commonDayTerm", et_212.getText().toString());//普通份额产品期限（天数）
			parms_map.put("commonPurAmt", et_214.getText().toString());//普通份额投资金额
		}break;
		case 2:{addParmsForMap(parms_map);}break;
		case 3:{addParmsForMap_1(parms_map);}break;
		case 4:{addParmsForMap_1(parms_map);}break;
		case 5:{addParmsForMap(parms_map);}break;
		case 6:{addParmsForMap(parms_map);}break;
		case 7:{addParmsForMap(parms_map);}break;
		}
		parms_map.put("proId", detailMap.get(BocInvt.BOCINVT_HOLDPRO_PRODCODE_RES).toString());//产品代码
		parms_map.put("opt", "1");//操作类型,0：查询产品信息、1：产品收益试算（默认为1）
	}
	private void addParmsForMap(Map<String, Object> parms_map){
		parms_map.put("exyield", et_11.getText().toString().trim());//预计年收益率（%）
		parms_map.put("dayTerm", et_12.getText().toString().trim());//产品期限（天数）
		parms_map.put("puramt", et_14.getText().toString().trim());//投资金额
	}
	private void addParmsForMap_1(Map<String, Object> parms_map){
		parms_map.put("exyield", et_11.getText().toString().trim());//预计年收益率（%）
		parms_map.put("dayTerm", et_12.getText().toString().trim());//产品期限（天数）
		parms_map.put("puramt", et_14.getText().toString().trim());//投资金额
		parms_map.put("totalPeriod", et_13.getText().toString().trim());//购买期数
	}
	/**
	 * 请求 收益试算  回调
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void requestPsnXpadProfitCountCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultObj)) {
			return;
		}
		Map<String, Object> responseDeal = (Map<String, Object>)getHttpTools().getResponseResult(resultObj);
		switch (profit_compute_type) {
		case 1:{tv_21.setText(StringUtil.parseStringCodePattern((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), responseDeal.get("expprofit").toString(), 2));//试算收益
		}
			break;
		default:{tv_11.setText(StringUtil.parseStringCodePattern((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), responseDeal.get("expprofit").toString(), 2));//试算收益
		}
			break;
		}
	}
	/**
	 * 初始化数据
	 */
	private void initDate(){
		chooseMap = BociDataCenter.getInstance().getChoosemap();
		detailMap=BociDataCenter.getInstance().getDetailMap();
//		accound_map=BocInvestControl.accound_map;
	}
	/**
	 * 初始化基类布局
	 */
	private void initBaseLayout(){
		setLeftButtonPopupGone();
		getBackgroundLayout().setTitleText("收益试算");
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
//	private TextView tv_title_profit_compute;
	private View layout_public;
	private EditText et_11;
	private EditText et_12;
	private View layout_1;
	private EditText et_13;
	private EditText et_14;
	private TextView tv_11;
	private View layout_2;
	private EditText et_21;
	private EditText et_22;
	private EditText et_24;
	private EditText et_211;
	private EditText et_212;
	private EditText et_214;
	private TextView tv_21;
	/**
	 * 1、业绩基准-锁定期转低收益 
	 * 2、业绩基准-锁定期后入账
	 * 3、业绩基准-锁定期周期滚续
	 * 4、对应表内周期续约产品的收益试算
	 * 5、对应日积月累产品的收益试算
	 * 6、对应固定期限产品的收益试算
	 * 7、是收益累进产品
	 */
	private int profit_compute_type;		

}
