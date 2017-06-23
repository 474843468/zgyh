package com.chinamworld.bocmbci.biz.branchorder.vip;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Order;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderBaseActivity;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderCustomerActivity;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderDataCenter;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * Vip特殊业务成功页
 * @author fsm
 *
 */
public class BranchOrderVipSpecialResultInfoActivity extends
		BranchOrderBaseActivity {
	
	//下一步，验证码图片
	private Button btnnext;
	/** 客户名称  ,预约手机号码, 网点名称, 网点地址, 预约时间, 网点电话, 业务名称，
	 * 提前提醒人数, 预约时段, 备注, 取现金额, 预约号码,不是生日的提醒,生日提醒*/
	private TextView customerName, customerPhone, orderName, orderAdress, orderTimeTv,
			orderMobile, orderBusinessTime,orderServiceName, beizhu, orderAmountOfCash,vTitle,vIsbirthday;
	String titleTv,titleIsbirday;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.order_vip_special_result_book_info);
		setTitle(getString(R.string.order_main_title));
		setPadding(0, 0, 0, 0);
		setStepBackground();
		setupView();
		setDataForView();
		setLeftInvisible();
	}
	
	private void setStepBackground(){
		((LinearLayout)findViewById(R.id.layout_step2))
			.setBackgroundResource(R.drawable.safety_step2);
		((LinearLayout)findViewById(R.id.layout_step3))
			.setBackgroundResource(R.drawable.safety_step5);
		((TextView)findViewById(R.id.index3)).setTextColor(getResources().getColor(R.color.red));
		((TextView)findViewById(R.id.text3)).setTextColor(getResources().getColor(R.color.red));
	}
	
	private void setupView(){
//		((LinearLayout)findViewById(R.id.take_on_success_tip_ll)).setVisibility(View.VISIBLE);
		btnnext = (Button)findViewById(R.id.btnnext);
		customerName = (TextView) findViewById(R.id.customerName);
		customerPhone = (TextView) findViewById(R.id.phoneNum);
		orderName = (TextView) findViewById(R.id.orderName);
		orderAdress = (TextView) findViewById(R.id.orderAdress);
		orderTimeTv = (TextView) findViewById(R.id.orderTime);
		orderMobile = (TextView) findViewById(R.id.orderMobile);
		orderBusinessTime = (TextView) findViewById(R.id.orderBusinessTime);
		orderServiceName = (TextView) findViewById(R.id.orderServiceName);
		beizhu = (TextView) findViewById(R.id.beizhu);
		
		orderAmountOfCash = (TextView) findViewById(R.id.orderAmountOfCash);
		vTitle =	(TextView)findViewById(R.id.title_tv);
		 vIsbirthday=(TextView) findViewById(R.id.title_isBirthday);
		 
		BranchOrderUtils.setOnShowAllTextListener(this, customerName, customerPhone, 
				orderName, orderAdress, orderMobile, orderBusinessTime,orderServiceName,
				beizhu, orderAmountOfCash, orderTimeTv);
		btnnext.setOnClickListener(nextClick);
	}
	
	private void setDataForView(){
		Map<String, Object> orderInfo = BranchOrderDataCenter.getInstance().getOrderListItem();
		Map<String, String> customerInfo = BranchOrderDataCenter.getInstance().getCustomerInfo();
		Map<String, Object> orderDetail = BranchOrderDataCenter.getInstance().getOrderDetail();
		Map<String, Object> orderApply = BranchOrderDataCenter.getInstance().getOrderApplyMap();
		List<Map<String, Object>> list = (List<Map<String, Object>>)orderDetail.
				get(Order.BRANCHBUSINESSDATELIST);
		if(!StringUtil.isNullOrEmpty(list)){
			setText(getBusinessTime(list), orderBusinessTime);
		}
		if(!StringUtil.isNullOrEmpty(orderInfo)){
			setText((String)orderInfo.get(Order.ORDERORGNAME), orderName);
			setText((String)orderInfo.get(Order.ORDERORGADRESS), orderAdress);
			setText((String)orderInfo.get(Order.ORDERORGPHONE), orderMobile);
		}
		if(!StringUtil.isNullOrEmpty(customerInfo)){
			//order_vip_special_result_book_info
			if(BranchOrderCustomerActivity.isBirthday){
				((LinearLayout)findViewById(R.id.take_on_success_tip_isBirthday)).setVisibility(View.VISIBLE);
				((LinearLayout)findViewById(R.id.take_on_success_tip_ll)).setVisibility(View.VISIBLE);
				
			}else{
				((LinearLayout)findViewById(R.id.take_on_success_tip_ll)).setVisibility(View.VISIBLE);
				 titleTv=getString(R.string.order_success_common_yypd3);
				 titleTv=titleTv .replace("name",customerInfo.get(Order.CUSTOMERNAME));
				vTitle.setText(titleTv);
			}
//			String titleTv = getString(R.string.order_success_special_yypd).replace("XXX",
//					customerInfo.get(Order.CUSTOMERNAME));
//			((TextView)findViewById(R.id.title_tv)).setText(titleTv);
			setText(customerInfo.get(Order.CUSTOMERNAME), customerName);
			setText(customerInfo.get(Order.ORDERMOBILE), customerPhone);
			setText(customerInfo.get(Order.BSNAME), orderServiceName);
			setText(customerInfo.get(Order.ORDERAPPLYDATE) + " " 
					+ customerInfo.get(Order.ORDERPERIOD), orderTimeTv);
			String serviceIdStr = customerInfo.get(Order.BSID);
			if(BranchOrderUtils.isStrEquals(serviceIdStr, "1")){
				((LinearLayout)findViewById(R.id.orderAmountOfCashLl)).setVisibility(View.VISIBLE);
				orderAmountOfCash.setText(getString(R.string.rmb_currency) + " " + 
						StringUtil.parseStringPattern(customerInfo.get(Order.AMOUNTOFCASH), 2));
			}else if(BranchOrderUtils.isStrEquals(serviceIdStr, "2")){
				((LinearLayout)findViewById(R.id.orderAmountOfCashLl)).setVisibility(View.VISIBLE);
				orderAmountOfCash.setText((String)BranchOrderDataCenter.Currency
						.get(customerInfo.get(Order.CURRENCY)) + " " + 
						StringUtil.parseStringPattern(customerInfo.get(Order.AMOUNTOFCASH), 2));
			}
			setText(customerInfo.get(Order.SPECIALREMARK), beizhu);
		}
	}
	
	/**
	 * 获取当日营业时间
	 * @param list 网点营业时间列表
	 * @return
	 */
	private String getBusinessTime(List<Map<String, Object>> list){
		String curDate = QueryDateUtils.getcurrentDate(BranchOrderDataCenter.getInstance().
				getCustomerInfo().get(Order.ORDERAPPLYDATE));
		if(!StringUtil.isNullOrEmpty(list)){
			for(Map<String, Object> map : list){
				String date = (String)map.get(Order.DATE);
				if(BranchOrderUtils.isStrEquals(curDate, date)){
					String startTime = (String)map.get(Order.STARTTIME);
					String endTime = (String)map.get(Order.ENDTIME);
					if(StringUtil.isNull(startTime) || StringUtil.isNull(endTime))
						return ConstantGloble.BOCINVT_DATE_ADD;
					else
						return startTime + "-" + endTime;
				}
			}
		}
		return ConstantGloble.BOCINVT_DATE_ADD;
	}
	
	OnClickListener nextClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			BranchOrderDataCenter.getInstance().clearAllData();
			setResult(RESULT_OK);
			finish();
			
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
