package com.chinamworld.bocmbci.biz.branchorder.vip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Order;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderBaseActivity;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderDataCenter;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderUtils;
import com.chinamworld.bocmbci.biz.branchorder.unvip.BranchOrderUnvipInputInfoActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

public class BranchOrderVipInputInfoActivity extends BranchOrderBaseActivity implements OnCheckedChangeListener{
	
	/** 公用组件*/
	private Button btnlast, btnnext;
	//预约事项， 预约时间， 预约时段
	private Spinner serviceName, orderTime, orderPeriod;
	private TextView orderPeriodTv;//预约时段
	
	/** 普通业务*/
	//当前时段已预约人数, 当前日期预约人数, 当前时段可预约人数
	private TextView orderHaveBookNum, orderCurDateHaveBookNum, orderCurPeroidMaxNumber; 
	private EditText orderAheadTipTime;//提前提醒时间
	
	/** 特殊业务  大额取现*/
	//取现金额， 备注
	private EditText orderAmountOfLargeCash, beizhuLargeCash;
	
	/** 特殊业务  外币取现*/
	private Spinner currencyFreign;//币种
	//取现金额， 备注
	private EditText orderAmountOfForeignCash, beizhuForeignCash;
	
	/** 特殊业务   取卡换卡*/
	private EditText beizhuCardService;
	
	private boolean isSpecialFlag, isNumOver;//是否选择了特殊业务,  预约人数已满
	/** 网点业务列表, 预约时间列表*/
	private List<Map<String, Object>> businessList, branchBusinessList, orderPeriodMapList;
	/** 预约事项List, 预约日期List, 预约时段List*/
	private List<String> serviceNameList, orderTimeList,  orderPeriodList, foreignCurrencyList;
	private Map<String, Object> serviceIdName;
	private String serviceNameStr, serviceIdStr, orderTimeStr, orderPeriodStr;
	private String foreignCurrency;
	/** 业务类型 */
	private CheckBox cbbtn1,cbbtn2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.order_vip_input_book_info);
		setTitle(R.string.order_main_title);
		setPadding(0, 0, 0, 0);
		setStepBackground();
		setupView();
		setDataForView();
		setOnClickListener();
	}
	
	private void setStepBackground(){
		((LinearLayout)findViewById(R.id.layout_step1))
			.setBackgroundResource(R.drawable.safety_step1);
		((TextView)findViewById(R.id.index1)).setTextColor(getResources().getColor(R.color.red));
		((TextView)findViewById(R.id.text1)).setTextColor(getResources().getColor(R.color.red));
	}
	
	private void setupView(){
		btnlast = (Button)findViewById(R.id.finc_btn1);btnlast.setOnClickListener(this);
		btnnext = (Button)findViewById(R.id.finc_btn2);btnnext.setOnClickListener(this);
		serviceName = (Spinner)findViewById(R.id.serviceName);
		serviceName.setOnItemSelectedListener(this);
		orderTime = (Spinner)findViewById(R.id.orderTime);
		orderTime.setOnItemSelectedListener(this);
		orderPeriod = (Spinner)findViewById(R.id.orderPeriod);
		orderPeriod.setOnItemSelectedListener(this);
		orderPeriodTv = (TextView)findViewById(R.id.orderPeriodTv);
		//普通业务组件
		orderHaveBookNum = (TextView)findViewById(R.id.orderHaveBookNum);
		orderCurDateHaveBookNum = (TextView)findViewById(R.id.orderCurDateHaveBookNum);
		orderCurPeroidMaxNumber = (TextView)findViewById(R.id.orderCurPeroidMaxNumber);
		orderAheadTipTime = (EditText)findViewById(R.id.orderAheadTipTime);
		BranchOrderUtils.setOnShowAllTextListener(this, (TextView)findViewById(R.id.orderHaveBookNumTv), 
				(TextView)findViewById(R.id.orderCurDateHaveBookNumTv), 
				(TextView)findViewById(R.id.orderCurPeroidMaxNumberTv));
		//特殊业务  大额取现
		orderAmountOfLargeCash = (EditText)findViewById(R.id.orderAmountOfLargeCash);
		beizhuLargeCash = (EditText)findViewById(R.id.beizhuLargeCash);
		EditTextUtils.setLengthMatcher(this, beizhuLargeCash, 100);
		//特殊业务  外币取现
		currencyFreign = (Spinner)findViewById(R.id.currencyFreign);
		currencyFreign.setOnItemSelectedListener(this);
		orderAmountOfForeignCash = (EditText)findViewById(R.id.orderAmountOfForeignCash);
		beizhuForeignCash = (EditText)findViewById(R.id.beizhuForeignCash);
		EditTextUtils.setLengthMatcher(this, beizhuForeignCash, 100);
		//特殊业务   取卡换卡
		beizhuCardService = (EditText)findViewById(R.id.beizhuCardService);
		EditTextUtils.setLengthMatcher(this, beizhuCardService, 100);
		
		cbbtn1 = (CheckBox) findViewById(R.id.cbbtn1);
		cbbtn2 = (CheckBox) findViewById(R.id.cbbtn2);
//		if(BranchOrderDataCenter.isSelectCommomAndSpecial){
//			setCheckboxStatus();
//		}
		cbbtn1.setOnCheckedChangeListener(this);
		cbbtn2.setOnCheckedChangeListener(this);
	}
	
	private void setDataForView(){
		Map<String, Object> orderDetail = BranchOrderDataCenter.getInstance().getOrderDetail();
		if(!StringUtil.isNullOrEmpty(orderDetail)){
			if(!isSpecialFlag){
				businessList = (List<Map<String, Object>>)
						orderDetail.get(Order.NORMALBUSINESSLIST);
				serviceIdName = BranchOrderUtils.getMapFromMapList(businessList, 
						Order.BSID, Order.BSNAME);
				serviceNameList = BranchOrderUtils.initServiceSpinnerView(this, serviceName, serviceIdName);
			}else{
				businessList = (List<Map<String, Object>>)
						orderDetail.get(Order.SPECIALBUSINESSLIST);
				serviceIdName = BranchOrderUtils.getMapFromMapList(businessList, 
						Order.SPECIALITEMID, Order.SPECIALITEMNAME);
				branchBusinessList = (List<Map<String, Object>>)
						orderDetail.get(Order.BRANCHBUSINESSDATELIST);
				String currentDate = QueryDateUtils.getcurrentDate(BranchOrderDataCenter.getInstance().getCustomerInfo(Order.DATETIME));
				orderTimeList = BranchOrderUtils.getSpecialOrderTimeList(branchBusinessList, Order.DATE, true,currentDate); 
				serviceNameList = BranchOrderUtils.initServiceSpinnerView(this, serviceName, serviceIdName);
				BranchOrderUtils.initSpinnerView(this, orderTime, orderTimeList);
				orderPeriodStr = getOrderPeriod(orderDetail);
				orderPeriodTv.setText(orderPeriodStr);
			}
		}
	}
	
	/**
	 * 获取预约时段
	 * @return
	 */
	private String getOrderPeriod(Map<String, Object> orderDetail){
		if(!StringUtil.isNullOrEmpty(orderDetail)){
			String speStartTime = (String)orderDetail.get(Order.SPESTARTTIME);
			String speEndTime = (String)orderDetail.get(Order.SPEENDTIME);
			if(StringUtil.isNull(speStartTime) || StringUtil.isNull(speEndTime))
				return ConstantGloble.BOCINVT_DATE_ADD;
			else
				return speStartTime + "-" + speEndTime;
		}
		return ConstantGloble.BOCINVT_DATE_ADD;
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_btn1:
			finish();
			BranchOrderDataCenter.isSelectCommomAndSpecial = false;
			break;
		case R.id.finc_btn2:
			if(saveCustomerInfo())
				if(isSpecialFlag){
					startActivityForResult(new Intent(BranchOrderVipInputInfoActivity.this, 
							BranchOrderVipSpecialComfirmInfoActivity.class), BranchOrderDataCenter.VIP);
				}else{
					startActivityForResult(new Intent(BranchOrderVipInputInfoActivity.this, 
							BranchOrderVipCommonConfirmInfoActivity.class), BranchOrderDataCenter.VIP);
				}
			break;

		default:
			break;
		}
	}
	
	private boolean saveCustomerInfo(){
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		if(isSpecialFlag){
			//特殊业务保存数据
			BranchOrderDataCenter.getInstance().setCustomerInfo(Order.BSID,
					serviceIdStr);
			BranchOrderDataCenter.getInstance().setCustomerInfo(Order.BSNAME,
					serviceNameStr);
			BranchOrderDataCenter.getInstance().setCustomerInfo(
					Order.ORDERAPPLYDATE, orderTimeStr);
			BranchOrderDataCenter.getInstance().setCustomerInfo(
					Order.ORDERPERIOD, orderPeriodStr);
			BranchOrderDataCenter.getInstance().setCustomerInfo(Order.CURRENCY,
					foreignCurrency);
			if(BranchOrderUtils.isStrEquals(serviceIdStr, "1")){
				String amountOfCash = orderAmountOfLargeCash.getText().toString();
				RegexpBean orderAmount = new RegexpBean(getNoColonStr(getString(R.string.order_amount_of_cash)), 
						amountOfCash,"orderAmount");
				RegexpBean orderAmount2 = new RegexpBean(getNoColonStr(getString(R.string.order_amount_of_cash)), 
						amountOfCash,"orderAmount2");
				lists.add(orderAmount);
				lists.add(orderAmount2);
				BranchOrderDataCenter.getInstance().setCustomerInfo(
						Order.AMOUNTOFCASH, orderAmountOfLargeCash.getText().toString());
				BranchOrderDataCenter.getInstance().setCustomerInfo(
						Order.SPECIALREMARK, beizhuLargeCash.getText().toString());
			}else if(	BranchOrderUtils.isStrEquals(serviceIdStr, "2")){
				String amountOfCash = orderAmountOfForeignCash.getText().toString();
				RegexpBean orderAmount = new RegexpBean(getNoColonStr(getString(R.string.order_amount_of_cash)), 
						amountOfCash,"orderAmount");
				lists.add(orderAmount);
				BranchOrderDataCenter.getInstance().setCustomerInfo(
						Order.AMOUNTOFCASH, orderAmountOfForeignCash.getText().toString());
				BranchOrderDataCenter.getInstance().setCustomerInfo(
						Order.SPECIALREMARK, beizhuForeignCash.getText().toString());
			}else{
				BranchOrderDataCenter.getInstance().setCustomerInfo(
						Order.SPECIALREMARK, beizhuCardService.getText().toString());
			}
			if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
				return false;
			}
		}else{
			//普通业务保存数据
			String remindTime = orderAheadTipTime.getText().toString();
			if(StringUtil.isNull(remindTime)){
				BranchOrderDataCenter.isTipChecked = false;
			}else{
				BranchOrderDataCenter.isTipChecked = true;
			}
			if(BranchOrderDataCenter.isTipChecked && (Integer.parseInt(remindTime) > 120  
					|| Integer.parseInt(remindTime) < 1)){
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.order_ahead_tip_time_regix));
				return false;
			}
			if(isNumOver){//当前预约已满
				BaseDroidApp.getInstanse().showErrorDialog(
						getString(R.string.order_num_over_tip), R.string.cancle, 
						R.string.order_continue,new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								switch ((Integer) arg0.getTag()) {
								case CustomDialog.TAG_CANCLE:
									BaseDroidApp.getInstanse().dismissErrorDialog();
									break;
								case CustomDialog.TAG_SURE:
									//远程取号
									if(!isHaveData()){
										BaseHttpEngine.dissMissProgressDialog();
										BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.order_no_book_service));
										return;
									}
									startActivityForResult(new Intent(BranchOrderVipInputInfoActivity.this, 
											BranchOrderUnvipInputInfoActivity.class), 
											BranchOrderDataCenter.UN_VIP);
									BaseDroidApp.getInstanse().dismissErrorDialog();
									break;
								}
							}
						});
				return false;
			}
			BranchOrderDataCenter.getInstance().setCustomerInfo(
					Order.REMINDTIME, remindTime);
			
			BranchOrderDataCenter.getInstance().setCustomerInfo(
					Order.BSID, serviceIdStr);
			BranchOrderDataCenter.getInstance().setCustomerInfo(
					Order.BSNAME, serviceNameStr);
			BranchOrderDataCenter.getInstance().setCustomerInfo(
					Order.ORDERAPPLYDATE, orderTimeStr);
			BranchOrderDataCenter.getInstance().setCustomerInfo(
					Order.ORDERPERIOD, orderPeriodStr);
		}
		return true;
	}
	
	private boolean isHaveData(){
		if(StringUtil.isNullOrEmpty(BranchOrderDataCenter.getInstance().getOrderDetail()))
			return false;
		List<Map<String, Object>> normalBusinessList = (List<Map<String, Object>>)
				BranchOrderDataCenter.getInstance().getOrderDetail().get(Order.NORMALBUSINESSLIST);
		if(StringUtil.isNullOrEmpty(normalBusinessList))
			return false;
		Map<String, Object> serviceIdName = BranchOrderUtils.getMapFromMapListByDate(normalBusinessList, 
				Order.BSID, Order.BSNAME, QueryDateUtils.getcurrentDate(BranchOrderDataCenter.getInstance().
						getCustomerInfo(Order.DATETIME)));
		if(StringUtil.isNullOrEmpty(serviceIdName)){
			return false;
		}
		return true;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		super.onItemSelected(arg0, arg1, arg2, arg3);
		switch (arg0.getId()) {
		case R.id.serviceName:
			serviceNameStr = serviceNameList.get(arg2);
			serviceIdStr = BranchOrderUtils.getKeyByValue(serviceIdName, serviceNameStr);
			if(!isSpecialFlag){
				orderTimeList = getOrderTimeList(businessList, serviceIdStr, Order.BSDATE);
				/**如果当前预约时段没有值  预约日期过滤当天日期 */
				orderTimeStr = orderTimeList.get(0);
				if(!StringUtil.isNullOrEmpty(businessList)){
					orderPeriodMapList = BranchOrderUtils.getListPeroidByDateAndService(businessList, 
							Order.BSDATE, orderTimeStr, Order.PLIST, serviceIdStr);
					String currentDate = QueryDateUtils.getcurrentDate(BranchOrderDataCenter.getInstance().getCustomerInfo(Order.DATETIME));
					orderPeriodList = getPeroidListString(orderPeriodMapList,currentDate);
					if(StringUtil.isNullOrEmpty(orderPeriodList)){
						orderTimeList = BranchOrderUtils.filterCurrentDate(orderTimeList, currentDate);
					}
				}
				BranchOrderUtils.initSpinnerView(this, orderTime, orderTimeList);
				if(StringUtil.isNullOrEmpty(orderTimeList)){
					BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.order_no_book_date));
					return;
				}
			}else{
				if(BranchOrderUtils.isStrEquals(serviceIdStr, "1")){
					//大额取现
					((LinearLayout)findViewById(R.id.commonServiceLl)).setVisibility(View.GONE);
					((LinearLayout)findViewById(R.id.largeAmountCashLl)).setVisibility(View.VISIBLE);
					((LinearLayout)findViewById(R.id.foreignAmountCashLl)).setVisibility(View.GONE);
					((LinearLayout)findViewById(R.id.cardServiceLl)).setVisibility(View.GONE);
				}else if(BranchOrderUtils.isStrEquals(serviceIdStr, "2")){
					//外币取现
					((LinearLayout)findViewById(R.id.commonServiceLl)).setVisibility(View.GONE);
					((LinearLayout)findViewById(R.id.largeAmountCashLl)).setVisibility(View.GONE);
					((LinearLayout)findViewById(R.id.foreignAmountCashLl)).setVisibility(View.VISIBLE);
					((LinearLayout)findViewById(R.id.cardServiceLl)).setVisibility(View.GONE);
					foreignCurrencyList = BranchOrderUtils.initSpinnerView(this, currencyFreign
							, BranchOrderDataCenter.Currency);
				}else{
					//取卡换卡
					((LinearLayout)findViewById(R.id.commonServiceLl)).setVisibility(View.GONE);
					((LinearLayout)findViewById(R.id.largeAmountCashLl)).setVisibility(View.GONE);
					((LinearLayout)findViewById(R.id.foreignAmountCashLl)).setVisibility(View.GONE);
					((LinearLayout)findViewById(R.id.cardServiceLl)).setVisibility(View.VISIBLE);
				}
			}
			break;
			/**当点击：预约日期：orderTime 时
			 * isSpecialFlag =false 时候，是表示选择的普通业务
			 * isSpecialFlag =true 选择的是特殊业务
			 * 当为普通业务时。调用：getListPeroidByDateAndService（）方法，根据选择日期动态更换当日可预约时段 列表
			 * 特殊业务则调用接口：PsnOrderApplyQuery 进行网点人数查询
			 * 
			 * */
		case R.id.orderTime:
			orderTimeStr = orderTimeList.get(arg2);
			if(isSpecialFlag==false){
				if(!StringUtil.isNullOrEmpty(businessList)){
					orderPeriodMapList = BranchOrderUtils.getListPeroidByDateAndService(businessList, 
							Order.BSDATE, orderTimeStr, Order.PLIST, serviceIdStr);
					String currentDate = QueryDateUtils.getcurrentDate(BranchOrderDataCenter.getInstance().getCustomerInfo(Order.DATETIME));
					if(orderTimeStr.equals(currentDate)){
						requestSystemDateTimeForLoginPre();
					}
					orderPeriodList = getPeroidListString(orderPeriodMapList,orderTime.getSelectedItem());
					BranchOrderUtils.initSpinnerView(this, orderPeriod, orderPeriodList);
					if(StringUtil.isNullOrEmpty(orderPeriodList)){
						BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.order_no_book_peroid));
						return;
					}
				}
			}else{
				BaseHttpEngine.showProgressDialog();
				requestPsnOrderApplyQuery();
			}
			break;
			/**当点击预约时段时：orderPeriod
			 * isSpecialFlag =false 时候，是表示选择的普通业务
			 * 则上送时间段，调用 PsnOrderApplyQuery接口 进行网点人数查询
			 * */
		case R.id.orderPeriod:
			orderPeriodStr = orderPeriodList.get(arg2);
			if(isSpecialFlag==false){
				BaseHttpEngine.showProgressDialog();
				requestPsnOrderApplyQuery();
			}
			break;
		case R.id.currencyFreign:
			if(!StringUtil.isNullOrEmpty(foreignCurrencyList)){
				foreignCurrency = foreignCurrencyList.get(arg2);
				foreignCurrency = BranchOrderUtils.getKeyByValue(BranchOrderDataCenter.Currency, foreignCurrency);
			}
		default:
			break;
		}
	}
	public void requestSystemDateTimeForLoginPreCallBack(Object resultObj) {
		super.requestSystemDateTimeForLoginPreCallBack(resultObj);
		BranchOrderDataCenter.getInstance().setCustomerInfo(
				Order.DATETIME, dateTime);
		orderPeriodList = getPeroidListString(orderPeriodMapList,orderTime.getSelectedItem());
		BranchOrderUtils.initSpinnerView(this, orderPeriod, orderPeriodList);
		if(StringUtil.isNullOrEmpty(orderPeriodList)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.order_no_book_peroid));
			return;
		}
	};
	/**
	 * 组合预约时间列表
	 * @param listMap
	 * @return
	 */
	private List<String> getOrderTimeList(List<Map<String, Object>> listMap, String serviceId, String dateKey){
//		requestSystemDateTimeForLoginPre();
		List<String> list = new ArrayList<String>();
		if(!StringUtil.isNullOrEmpty(listMap) && !StringUtil.isNull(serviceId)){
			for(Map<String, Object> map: listMap){
				String serviceIdStr = (String)map.get(Order.BSID);
				if(BranchOrderUtils.isStrEquals(serviceId, serviceIdStr)){
					if(!StringUtil.isNullOrEmpty(map))
						list.add((String)map.get(dateKey) );
				}
			}
		}
		return BranchOrderUtils.sortByDate(list);
	}
	
	/**
	 * add by niuchf
	 * 请求系统时间回调
	 */
//	public void requestSystemDateTimeForLoginPreCallBack(Object resultObj) {
//		super.requestSystemDateTimeForLoginPreCallBack(resultObj);
//		BranchOrderDataCenter.getInstance().setCustomerInfo(Order.DATETIME, dateTime);
//	};
	
	/**
	 * 组合预约时段列表
	 * @param listMap 预约时间Map列表
	 * @return
	 */
	private List<String> getPeroidListString(List<Map<String, Object>> listMap,Object selected){
		String currentTime = BranchOrderDataCenter.getInstance().getCustomerInfo(Order.DATETIME);
		String currentHourMinute = currentTime.substring(11, 16);
		List<String> list = new ArrayList<String>();
		if(!StringUtil.isNullOrEmpty(listMap)){
			for(Map<String, Object> map: listMap){
				if(!StringUtil.isNullOrEmpty(map))
					if(BranchOrderUtils.isStrEquals(QueryDateUtils.getcurrentDate(currentTime),selected.toString())&&BranchOrderUtils.compareStartDateToEndDate((String)map.get(Order.BSSTARTTIME), (String)map.get(Order.BSENDTIME), currentHourMinute)){
					}else{
						list.add((String)map.get(Order.BSSTARTTIME) + "-" + (String)map.get(Order.BSENDTIME));
					}
			}
		}
		return list;
	}
	
	public void requestPsnOrderApplyQuery(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, Object> map = BranchOrderDataCenter.getInstance().getOrderListItem();
		biiRequestBody.setMethod(Order.METHOD_BRANCHORDER_APPLY_QUERY);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Order.ORDERORGCODE, (String)map.get(Order.ORDERORGCODE));
		params.put(Order.ORDERAPPLYDATE, orderTimeStr);
		params.put(Order.ORDERPERIOD, orderPeriodStr);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnOrderApplyQueryCallBack");
	}
	
	public void requestPsnOrderApplyQueryCallBack(Object resultObj){
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BranchOrderDataCenter.getInstance().setOrderApplyQueryMap(map);
		String orderNumber = (String)map.get(Order.ORDERNUMBER);//当前时段预约人数
		String bsMaxNumber = (String)map.get(Order.BSMAXNUMBER);//当前时段可预约人数
		String orderDateNumber = (String)map.get(Order.ORDERDATENUMBER);//当前日期可预约人数
		if(!StringUtil.isNull(orderNumber) && !StringUtil.isNull(bsMaxNumber)){
			isNumOver = Integer.parseInt(orderNumber) >= Integer.parseInt(bsMaxNumber) ? true : false;
			orderHaveBookNum.setText(StringUtil.valueOf1((String)map.get(Order.ORDERNUMBER)) + "人");
			orderCurDateHaveBookNum.setText(StringUtil.valueOf1((String)map.get(Order.ORDERDATENUMBER)) + "人");
			orderCurPeroidMaxNumber.setText(StringUtil.valueOf1((String)map.get(Order.BSMAXNUMBER)) + "人");
		} 
		if (StringUtil.isNull(bsMaxNumber)) {
			isNumOver = true;
			orderCurPeroidMaxNumber.setText("人");
		}else {
			orderCurPeroidMaxNumber.setText(StringUtil.valueOf1((String)map.get(Order.BSMAXNUMBER)) + "人");
		}
		if(StringUtil.isNull(orderNumber)){
			orderHaveBookNum.setText("人");
		}else {
			orderHaveBookNum.setText(StringUtil.valueOf1((String)map.get(Order.ORDERNUMBER)) + "人");
		}
		if(StringUtil.isNullOrEmpty(orderDateNumber)){
			orderCurDateHaveBookNum.setText("人");
		}else {
			orderCurDateHaveBookNum.setText(StringUtil.valueOf1((String)map.get(Order.ORDERDATENUMBER)) + "人");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case BranchOrderDataCenter.UN_VIP:
		default:
			switch (resultCode) {
			case RESULT_OK:
				setResult(RESULT_OK);
				finish();
				break;
			case 1001:
				setCheckboxStatus();
				break;
			default:
				break;
			}
			break;
		}
	}
	
	/**
	 * 同时选中普通业务和特殊业务，办理特殊业务时复选框设置为选中不可点击
	 */
	public void setCheckboxStatus(){
		cbbtn1.setChecked(true);
		cbbtn2.setChecked(true);
		cbbtn1.setClickable(false);	
		cbbtn2.setClickable(false);
		isSpecialFlag = true;
		orderPeriod.setVisibility(View.GONE);
		orderPeriodTv.setVisibility(View.VISIBLE);
		((LinearLayout)findViewById(R.id.commonServiceLl)).setVisibility(View.GONE);
		if(BranchOrderDataCenter.isSelectCommomAndSpecial){
			mLeftButton.setVisibility(View.GONE);
			btnlast.setVisibility(View.GONE);
		}
		setDataForView();
	}

	/**
	 * add by niuchf
	 * 复选框设置
	 * @param buttonView
	 * @param isChecked
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(cbbtn1.isChecked()) {
			if(!cbbtn2.isChecked()){
				cbbtn1.setClickable(false);
				cbbtn2.setClickable(true);
				BranchOrderDataCenter.isSelectCommomAndSpecial = false;
			}else{
				cbbtn1.setClickable(true);
				cbbtn2.setClickable(true);
				BranchOrderDataCenter.isSelectCommomAndSpecial = true;
			}
			isSpecialFlag = false;
			orderPeriod.setVisibility(View.VISIBLE);
			orderPeriodTv.setVisibility(View.GONE);
			((LinearLayout)findViewById(R.id.commonServiceLl)).setVisibility(View.VISIBLE);
			((LinearLayout)findViewById(R.id.largeAmountCashLl)).setVisibility(View.GONE);
			((LinearLayout)findViewById(R.id.foreignAmountCashLl)).setVisibility(View.GONE);
			((LinearLayout)findViewById(R.id.cardServiceLl)).setVisibility(View.GONE);
			
		}
		if(cbbtn2.isChecked()) {
			if(cbbtn1.isChecked()) {
				cbbtn1.setClickable(true);
				cbbtn2.setClickable(true);
				BranchOrderDataCenter.isSelectCommomAndSpecial = true;				
			}else{
				cbbtn1.setClickable(true);
				cbbtn2.setClickable(false);
				isSpecialFlag = true;
				BranchOrderDataCenter.isSelectCommomAndSpecial = false;
				orderPeriod.setVisibility(View.GONE);
				orderPeriodTv.setVisibility(View.VISIBLE);
				((LinearLayout)findViewById(R.id.commonServiceLl)).setVisibility(View.GONE);
			}
		}
		setDataForView();
	}
	/**
	 * 重新设定返回时点击事件，清除选中的业务类型数据
	 */
	private void setOnClickListener(){
		mLeftButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				finish();
				BranchOrderDataCenter.isSelectCommomAndSpecial = false;
			}
		});
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(!(BranchOrderDataCenter.isSelectCommomAndSpecial)){
			super.onBackPressed();
		}
	}
}
