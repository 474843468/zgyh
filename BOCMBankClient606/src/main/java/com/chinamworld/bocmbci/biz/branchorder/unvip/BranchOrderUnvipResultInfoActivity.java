package com.chinamworld.bocmbci.biz.branchorder.unvip;

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
 * 非中行客户远程取号成功
 * @author fsm
 *
 */
public class BranchOrderUnvipResultInfoActivity extends
		BranchOrderBaseActivity {
	
	//下一步，验证码图片
	private Button btnnext;
	/** 客户名称  ,预约手机号码, 网点名称, 网点地址, 预约时间, 网点电话, 营业时间, 业务名称，提前提醒人数
	 * 票号*/
	private TextView customerName, customerPhone, orderName, orderAdress, orderTimeTv,
			orderMobile, orderBusinessTime, orderServiceName, orderAheadTipNum, vTitle,
			takeOnNumber,vIsbirthday;
	String titleTv,titleIsbirday;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.order_unvip_result_book_info);
		setTitle(getString(R.string.order_main_title));
		setPadding(0, 0, 0, 0);
		setStepBackground();
		setupView();
		setDataForView();
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
		setLeftInvisible();
		btnnext = (Button)findViewById(R.id.btnnext);
		customerName = (TextView) findViewById(R.id.customerName);
		customerPhone = (TextView) findViewById(R.id.phoneNum);
		orderName = (TextView) findViewById(R.id.orderName);
		orderAdress = (TextView) findViewById(R.id.orderAdress);
		orderTimeTv = (TextView) findViewById(R.id.orderTime);
		orderMobile = (TextView) findViewById(R.id.orderMobile);
		orderBusinessTime = (TextView) findViewById(R.id.orderBusinessTime);
		orderServiceName = (TextView) findViewById(R.id.orderServiceName);
		orderAheadTipNum = (TextView) findViewById(R.id.orderAheadTipNum);
		takeOnNumber = (TextView) findViewById(R.id.takeOnNumber);
//		vTitle =	(TextView)findViewById(R.id.title_tv);
		vIsbirthday=(TextView) findViewById(R.id.title_isBirthday);
		if(BranchOrderDataCenter.isTipChecked)
			((LinearLayout)findViewById(R.id.orderAheadTipNumLl)).setVisibility(View.VISIBLE);
		else
			((LinearLayout)findViewById(R.id.orderAheadTipNumLl)).setVisibility(View.GONE);
		BranchOrderUtils.setOnShowAllTextListener(this, customerName, customerPhone, 
				orderName, orderAdress, orderMobile, orderBusinessTime, orderServiceName,
				orderAheadTipNum, takeOnNumber);
		btnnext.setOnClickListener(nextClick);
	}
	
	private void setDataForView(){
		Map<String, Object> orderInfo = BranchOrderDataCenter.getInstance().getOrderListItem();
		Map<String, String> customerInfo = BranchOrderDataCenter.getInstance().getCustomerInfo();
		Map<String, Object> orderDetail = BranchOrderDataCenter.getInstance().getOrderDetail();
		Map<String, Object> takeOn = BranchOrderDataCenter.getInstance().getOrderTakeOnMap();
		if(!StringUtil.isNullOrEmpty(orderInfo)){
			setText((String)orderInfo.get(Order.ORDERORGNAME), orderName);
			setText((String)orderInfo.get(Order.ORDERORGADRESS), orderAdress);
			setText((String)orderInfo.get(Order.ORDERORGPHONE), orderMobile);
		}
		if(!StringUtil.isNullOrEmpty(customerInfo)){
			//判断今天是否是用户的生日
			System.out.println("isBirthday===="+BranchOrderCustomerActivity.isBirthday);
			if(BranchOrderCustomerActivity.isBirthday){
				((LinearLayout)findViewById(R.id.take_on_success_tip_isBirthday)).setVisibility(View.VISIBLE);
				((LinearLayout)findViewById(R.id.take_on_success_tip_ll)).setVisibility(View.VISIBLE);
				
//				 titleIsbirday=getString(R.string.order_success_ycqh_newtitle) + getString(R.string.order_success_ycqh_isBirthday);
//				// titleIsbirday=	titleIsbirday .replace("XXX",customerInfo.get(Order.CUSTOMERNAME));
//				titleTv= getString(R.string.order_success_ycqh );
//				vIsbirthday.setText(titleTv);
//				vTitle.setText(titleIsbirday);
				
			}else{
				
				String text =getResources().getString(R.string.order_success_common_yypd);
				text = text.replace("name", customerInfo.get(Order.CUSTOMERNAME));
				((LinearLayout)findViewById(R.id.take_on_success_tip_unBirthday)).setVisibility(View.VISIBLE);
				((TextView)findViewById(R.id.title_unisBirthday)).setText(text);
			}
			setText(customerInfo.get(Order.CUSTOMERNAME), customerName);
			setText(customerInfo.get(Order.ORDERMOBILE), customerPhone);
			setText(customerInfo.get(Order.BSNAME), orderServiceName);
			if(StringUtil.isNull(customerInfo.get(Order.REMINDNUMBER)) || customerInfo.get(Order.REMINDNUMBER).equals("无须提醒"))
				orderAheadTipNum.setText("-");
			else
				orderAheadTipNum.setText(StringUtil.valueOf1(customerInfo.get(Order.REMINDNUMBER)) + "人");
			setText(QueryDateUtils.getcurrentDate(customerInfo.get(Order.DATETIME)), orderTimeTv);
		}
		if(!StringUtil.isNullOrEmpty(orderDetail)){
			List<Map<String, Object>> list = (List<Map<String, Object>>)orderDetail.
					get(Order.BRANCHBUSINESSDATELIST);
			orderBusinessTime.setText(getBusinessTime(list));
		}
		if(!StringUtil.isNullOrEmpty(takeOn)){
			setText((String)takeOn.get(Order.TAKEONNUMBER), takeOnNumber);
		}
	}
	
	/**
	 * 获取当日营业时间
	 * @param list 网点营业时间列表
	 * @return
	 */
	private String getBusinessTime(List<Map<String, Object>> list){
		String curDate = QueryDateUtils.getcurrentDate(BranchOrderDataCenter.getInstance().
				getCustomerInfo().get(Order.DATETIME));
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
			if(BranchOrderDataCenter.isSelectCommomAndSpecial){
				String orderFinishInfo = BranchOrderUnvipResultInfoActivity.this.getString(R.string.order_common_finish_iscontinue);
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
		}else {
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
