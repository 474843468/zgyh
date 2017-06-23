package com.chinamworld.bocmbci.biz.branchorder.unvip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Order;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderBaseActivity;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderDataCenter;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 远程取号输入验证码页
 * @author fsm
 *
 */
public class BranchOrderUnvipConfirmInfoActivity extends
		BranchOrderBaseActivity {
	
	//下一步，验证码图片
	private Button btnlast, btnnext, imageCodeButton;
	/** 客户名称  ,预约手机号码, 网点名称, 网点地址, 预约时间, 网点电话, 营业时间, 业务名称，提前提醒人数*/
	private TextView customerName, customerPhone, orderName, orderAdress, orderTimeTv,
			orderMobile, orderBusinessTime, orderServiceName, orderAheadTipNum;
	/** 验证码*/
	private EditText orderInputIdentifyCode;
	/** 验证码*/
	private String identifyCodeStr;
	/** 是否正在请求验证码 */
	private boolean isRequestingCode = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.order_unvip_confirm_book_info);
		setTitle(getString(R.string.order_main_title));
		setPadding(0, 0, 0, 0);
		setStepBackground();
		setupView();
		setDataForView();
		isRequestingCode = true;
		requestForImagecode();
	}
	
	private void setStepBackground(){
		((LinearLayout)findViewById(R.id.layout_step1))
			.setBackgroundResource(R.drawable.safety_step2);
		((LinearLayout)findViewById(R.id.layout_step2))
			.setBackgroundResource(R.drawable.safety_step1);
		((TextView)findViewById(R.id.index2)).setTextColor(getResources().getColor(R.color.red));
		((TextView)findViewById(R.id.text2)).setTextColor(getResources().getColor(R.color.red));
	}
	
	private void setupView(){
		btnlast = (Button)findViewById(R.id.finc_btn1);
		btnnext = (Button)findViewById(R.id.finc_btn2);
		imageCodeButton = (Button)findViewById(R.id.ib_image_code);
		customerName = (TextView) findViewById(R.id.customerName);
		customerPhone = (TextView) findViewById(R.id.phoneNum);
		orderName = (TextView) findViewById(R.id.orderName);
		orderAdress = (TextView) findViewById(R.id.orderAdress);
		orderTimeTv = (TextView) findViewById(R.id.orderTime);
		orderMobile = (TextView) findViewById(R.id.orderMobile);
		orderBusinessTime = (TextView) findViewById(R.id.orderBusinessTime);
		orderServiceName = (TextView) findViewById(R.id.orderServiceName);
		orderAheadTipNum = (TextView) findViewById(R.id.orderAheadTipNum);
		orderInputIdentifyCode = (EditText) findViewById(R.id.orderInputIdentifyCode);
		if(BranchOrderDataCenter.isTipChecked)
			((LinearLayout)findViewById(R.id.orderAheadTipNumLl)).setVisibility(View.VISIBLE);
		else
			((LinearLayout)findViewById(R.id.orderAheadTipNumLl)).setVisibility(View.GONE);
		BranchOrderUtils.setOnShowAllTextListener(this, customerName, customerPhone, 
				orderName, orderAdress, orderMobile, orderBusinessTime, orderServiceName,
				orderAheadTipNum);
		btnlast.setOnClickListener(this);
		btnnext.setOnClickListener(this);
		imageCodeButton.setOnClickListener(ibImageCodeClick);
	}
	
	private void setDataForView(){
		Map<String, Object> orderInfo = BranchOrderDataCenter.getInstance().getOrderListItem();
		Map<String, String> customerInfo = BranchOrderDataCenter.getInstance().getCustomerInfo();
		Map<String, Object> orderDetail = BranchOrderDataCenter.getInstance().getOrderDetail();
		if(!StringUtil.isNullOrEmpty(orderInfo)){
			setText((String)orderInfo.get(Order.ORDERORGNAME), orderName);
			setText((String)orderInfo.get(Order.ORDERORGADRESS), orderAdress);
			setText((String)orderInfo.get(Order.ORDERORGPHONE), orderMobile);
		}
		if(!StringUtil.isNullOrEmpty(customerInfo)){
			String titleTv = getString(R.string.order_please_confirm_info_ycqh).replace("XXX",
					customerInfo.get(Order.CUSTOMERNAME));
			((TextView)findViewById(R.id.title_tv)).setText(titleTv);
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
	
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_btn1:
			setResult(BranchOrderDataCenter.UN_VIP);
			finish();
			break;
		case R.id.finc_btn2:
			if(saveCustomerInfo()){
				//远程取号PsnOrderTakeOn
				BaseHttpEngine.showProgressDialog();
				requestLoginPreConversationId();
			}
			break;
		default:
			break;
		}
	}
	
	OnClickListener ibImageCodeClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//请求验证码
			if (!isRequestingCode) {
				isRequestingCode = true;
				requestForImagecode();
			}
		}
	};
	
	/**
	 * 请求验证码图片
	 */
	public void requestForImagecode() {
		isRequestingCode = false;
		imageCodeButton.setText(R.string.imagecodeloading);
		imageCodeButton.setBackgroundResource(R.drawable.selector_for_image_code);
		HttpManager.requestImage(Comm.AQUIRE_IMAGE_CODE, ConstantGloble.GO_METHOD_GET, null, this,
				"imagecodeCallBackMethod");
	}
	
	/**
	 * 验证码请求回调
	 * 
	 * @param resultObj 请求成功 返回参数
	 */
	public void imagecodeCallBackMethod(Object resultObj) {
		// 取消通讯框
		Bitmap bitmap = (Bitmap) resultObj;
		BitmapDrawable imageCodeDrawable = new BitmapDrawable(bitmap);
		imageCodeButton.setBackgroundDrawable(imageCodeDrawable);
		imageCodeButton.setText("");
		isRequestingCode = false;
	}
	
	private boolean saveCustomerInfo(){
		identifyCodeStr = orderInputIdentifyCode.getText().toString().trim();
		if(StringUtil.isNull(identifyCodeStr)){
			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入验证码");
			return false;
		}
		if (identifyCodeStr.length() != 4) {
			BaseDroidApp.getInstanse()
					.showInfoMessageDialog(getResources().getString(R.string.zcode_invalible));
			return false;
		}
		BranchOrderDataCenter.getInstance().setCustomerInfo(
				Order.VALIDATIONCHAR, identifyCodeStr);
		return true;
	}

	@Override
	public void requestLoginPreConversationIdCallBack(Object resultObj) {
		super.requestLoginPreConversationIdCallBack(resultObj);
		requestPsnOrderTakeOn();
	}
	
	//远程取号
	public void requestPsnOrderTakeOn(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOGIN_PRECONVERSATIONID).toString());
		Map<String, Object> orderInfo = BranchOrderDataCenter.getInstance().getOrderListItem();
		Map<String, String> customerInfo = BranchOrderDataCenter.getInstance().getCustomerInfo();
		biiRequestBody.setMethod(Order.METHOD_BRANCHORDER_TAKEON);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Order.ORDERBOCFLAG, BranchOrderDataCenter.customerType + "");
		params.put(Order.ORDERCERTTYPE, (String)customerInfo.get(Order.ORDERCERTTYPE));
		params.put(Order.ORDERCERTNO, (String)customerInfo.get(Order.ORDERCERTNO));
		params.put(Order.CUSTOMERNAME, (String)customerInfo.get(Order.CUSTOMERNAME));
		params.put(Order.ORDERMOBILE, (String)customerInfo.get(Order.ORDERMOBILE));
		params.put(Order.BSID, (String)customerInfo.get(Order.BSID));
		if(BranchOrderDataCenter.isTipChecked)
			params.put(Order.REMINDNUMBER, (String)customerInfo.get(Order.REMINDNUMBER));
		params.put(Order.ORDERORGCODE, (String)orderInfo.get(Order.ORDERORGCODE));
		params.put(Order.VALIDATIONCHAR, identifyCodeStr);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnOrderTakeOnCallBack");
	}
	
	public void requestPsnOrderTakeOnCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		BranchOrderDataCenter.getInstance().setOrderTakeOnMap(map);
		startActivityForResult(new Intent(this, BranchOrderUnvipResultInfoActivity.class), 
				BranchOrderDataCenter.UN_VIP);
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
				setResult(1001);
				finish();
				break;
			default:
				break;
			}
			break;
		}
	}
}
