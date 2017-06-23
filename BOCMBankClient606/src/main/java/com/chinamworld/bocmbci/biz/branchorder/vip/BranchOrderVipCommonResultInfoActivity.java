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
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Order;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderBaseActivity;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderCustomerActivity;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderDataCenter;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * Vip客户普通业务预约成功
 * @author fsm
 *
 */
public class BranchOrderVipCommonResultInfoActivity extends
		BranchOrderBaseActivity {
	
	//下一步，验证码图片
	private Button btnnext;
	/** 客户名称  ,预约手机号码, 网点名称, 网点地址, 预约时间, 网点电话, 业务名称，提前提醒时间
	 * 预约号码*/
	private TextView customerName, customerPhone, orderName, orderAdress, orderTimeTv,
			orderMobile, orderServiceName,orderBusinessTime, orderAheadTipTime, vTitle,vIsbirthday,
			orderApplyNumber;
	/** 预约时间段*/
	private TextView orderPeriod;
	String titleTv,titleIsbirday;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.order_vip_common_result_book_info);
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
		if(BranchOrderDataCenter.isTipChecked)
			((LinearLayout)findViewById(R.id.orderAheadTipNumLl)).setVisibility(View.VISIBLE);
		else
			((LinearLayout)findViewById(R.id.orderAheadTipNumLl)).setVisibility(View.GONE);
		btnnext = (Button)findViewById(R.id.btnnext);
		customerName = (TextView) findViewById(R.id.customerName);
		customerPhone = (TextView) findViewById(R.id.phoneNum);
		orderName = (TextView) findViewById(R.id.orderName);
		orderAdress = (TextView) findViewById(R.id.orderAdress);
		orderMobile = (TextView) findViewById(R.id.orderMobile);
		orderBusinessTime = (TextView) findViewById(R.id.orderBusinessTime);
		orderServiceName = (TextView) findViewById(R.id.orderServiceName);
		orderTimeTv = (TextView) findViewById(R.id.orderTime);
		orderPeriod = (TextView) findViewById(R.id.orderPeriod);
		orderAheadTipTime = (TextView) findViewById(R.id.orderAheadTipTime);
		orderApplyNumber = (TextView) findViewById(R.id.takeOnNumber);
		vTitle =	(TextView)findViewById(R.id.title_tv);
		 vIsbirthday=(TextView) findViewById(R.id.title_isBirthday);
		BranchOrderUtils.setOnShowAllTextListener(this, customerName, customerPhone, 
				orderName, orderAdress, orderMobile,orderBusinessTime, orderServiceName,
				orderAheadTipTime, orderTimeTv, orderPeriod, orderApplyNumber);
		btnnext.setOnClickListener(nextClick);
	}
	
	private void setDataForView(){
		Map<String, Object> orderInfo = BranchOrderDataCenter.getInstance().getOrderListItem();
		Map<String, String> customerInfo = BranchOrderDataCenter.getInstance().getCustomerInfo();
		Map<String, Object> orderDetail = BranchOrderDataCenter.getInstance().getOrderDetail();
		Map<String, Object> takeOn = BranchOrderDataCenter.getInstance().getOrderApplyMap();
		if(!StringUtil.isNullOrEmpty(orderInfo)){
			setText((String)orderInfo.get(Order.ORDERORGNAME), orderName);
			setText((String)orderInfo.get(Order.ORDERORGADRESS), orderAdress);
			setText((String)orderInfo.get(Order.ORDERORGPHONE), orderMobile);
		}
		if(!StringUtil.isNullOrEmpty(customerInfo)){
			if(BranchOrderCustomerActivity.isBirthday==true){
				((LinearLayout)findViewById(R.id.take_on_success_tip_isBirthday)).setVisibility(View.VISIBLE);
				((LinearLayout)findViewById(R.id.take_on_success_tip_ll)).setVisibility(View.VISIBLE);
				
			}else{
				((LinearLayout)findViewById(R.id.take_on_success_tip_ll)).setVisibility(View.VISIBLE);
				 titleTv=getString(R.string.order_success_common_yypd3);
				 titleTv=titleTv .replace("name",customerInfo.get(Order.CUSTOMERNAME));
				vTitle.setText(titleTv);
			}
			setText(customerInfo.get(Order.CUSTOMERNAME), customerName);
			setText(customerInfo.get(Order.ORDERMOBILE), customerPhone);
			setText(customerInfo.get(Order.BSNAME), orderServiceName);
			setText(customerInfo.get(Order.ORDERPERIOD), orderPeriod);
			setText(customerInfo.get(Order.REMINDTIME)+ "分钟", orderAheadTipTime);
			setText(customerInfo.get(Order.ORDERAPPLYDATE), orderTimeTv);
		}
		if(!StringUtil.isNullOrEmpty(orderDetail)){
			List<Map<String, Object>> list = (List<Map<String, Object>>)orderDetail.
					get(Order.BRANCHBUSINESSDATELIST);
			orderBusinessTime.setText(getBusinessTime(list));
		}
		if(!StringUtil.isNullOrEmpty(takeOn)){
			setText((String)takeOn.get(Order.ORDERSEQ), orderApplyNumber);
		}
	}
	
	/**
	 * 获取当日营业时间
	 * @param list 网点营业时间列表
	 * @return
	 */
	private String getBusinessTime(List<Map<String, Object>> list){
//		String curDate = QueryDateUtils.getcurrentDate(BranchOrderDataCenter.getInstance().
//				getCustomerInfo().get(Order.DATETIME));
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
						return startTime + "--" + endTime;
				}
			}
		}
		return ConstantGloble.BOCINVT_DATE_ADD;
	}
	
	OnClickListener nextClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(BranchOrderDataCenter.isSelectCommomAndSpecial){
				String orderFinishInfo = BranchOrderVipCommonResultInfoActivity.this.getString(R.string.order_common_finish_iscontinue);
				orderFinishInfo = "<font color=\"#ba001d\">" + orderFinishInfo + "</font>";
				BaseDroidApp.getInstanse().showErrorDialog(
						orderFinishInfo, R.string.cancle, 
					R.string.confirm,new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							switch ((Integer) arg0.getTag()) {
							case CustomDialog.TAG_CANCLE:
								BaseDroidApp.getInstanse().dismissMessageDialog();
								BranchOrderDataCenter.getInstance().clearAllData();
								setResult(RESULT_OK);
								finish();
								break;
							case CustomDialog.TAG_SURE:
								BaseDroidApp.getInstanse().dismissMessageDialog();
								setResult(1001);
								finish();
//								BranchOrderDataCenter.isXGrade = true;
//								Intent intent = new Intent(BranchOrderVipCommonResultInfoActivity.this, BranchOrderVipInputInfoActivity.class);
//								startActivityForResult(intent, BranchOrderDataCenter.UN_VIP);
							}
						}
						
					});
		}else{
			BranchOrderDataCenter.getInstance().clearAllData();
			setResult(RESULT_OK);
			finish();
		}
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
