package com.chinamworld.bocmbci.biz.branchorder.vip;

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
 * Vip特殊业务输入验证码页
 * @author fsm
 *
 */
public class BranchOrderVipSpecialComfirmInfoActivity extends
		BranchOrderBaseActivity {
	
	//下一步，验证码图片
	private Button btnlast, btnnext, imageCodeButton;
	/** 客户名称  ,预约手机号码, 网点名称, 网点地址, 预约时间, 网点电话, 营业时间, 业务名称，
	 * 提前提醒人数, 预约时段, 备注, 取现金额*/
	private TextView customerName, customerPhone, orderName, orderAdress, orderTimeTv,
			orderMobile, orderBusinessTime, orderServiceName, beizhu, 
			orderAmountOfCash;
	/** 验证码*/
	private EditText orderInputIdentifyCode;
	/** 验证码*/
	private String identifyCodeStr;
	/** 是否正在请求验证码 */
	private boolean isRequestingCode = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.order_vip_special_confirm_book_info);
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
		beizhu = (TextView) findViewById(R.id.beizhu);
		
		orderAmountOfCash = (TextView) findViewById(R.id.orderAmountOfCash);
		
		orderInputIdentifyCode = (EditText) findViewById(R.id.orderInputIdentifyCode);
		BranchOrderUtils.setOnShowAllTextListener(this, customerName, customerPhone, 
				orderName, orderAdress, orderMobile, orderBusinessTime, orderServiceName, 
				beizhu, orderAmountOfCash, orderTimeTv);
		btnlast.setOnClickListener(this);
		btnnext.setOnClickListener(this);
		imageCodeButton.setOnClickListener(ibImageCodeClick);
	}
	
	private void setDataForView(){
		Map<String, Object> orderInfo = BranchOrderDataCenter.getInstance().getOrderListItem();
		Map<String, String> customerInfo = BranchOrderDataCenter.getInstance().getCustomerInfo();
		Map<String, Object> orderDetail = BranchOrderDataCenter.getInstance().getOrderDetail();
		if(!StringUtil.isNullOrEmpty(orderDetail)){
			List<Map<String, Object>> list = (List<Map<String, Object>>)orderDetail.
					get(Order.BRANCHBUSINESSDATELIST);
			setText(getBusinessTime(list), orderBusinessTime);
		}
		if(!StringUtil.isNullOrEmpty(orderInfo)){
			setText((String)orderInfo.get(Order.ORDERORGNAME), orderName);
			setText((String)orderInfo.get(Order.ORDERORGADRESS), orderAdress);
			setText((String)orderInfo.get(Order.ORDERORGPHONE), orderMobile);
		}
		if(!StringUtil.isNullOrEmpty(customerInfo)){
			String titleTv = getString(R.string.order_please_confirm_info_yypd).replace("XXX",
					customerInfo.get(Order.CUSTOMERNAME));
			((TextView)findViewById(R.id.title_tv)).setText(titleTv);
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
	
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_btn1:
			finish();
			break;
		case R.id.finc_btn2:
			if(saveCustomerInfo()){
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
		requestPsnOrderApply();
	}
	
	//预约排队
	public void requestPsnOrderApply(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOGIN_PRECONVERSATIONID).toString());
		Map<String, Object> orderInfo = BranchOrderDataCenter.getInstance().getOrderListItem();
		Map<String, String> customerInfo = BranchOrderDataCenter.getInstance().getCustomerInfo();
		biiRequestBody.setMethod(Order.METHOD_BRANCHORDER_APPLY);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Order.ORDERAPPLYTYPE, 2 + "");
		params.put(Order.ORDERCERTTYPE, customerInfo.get(Order.ORDERCERTTYPE));
		params.put(Order.ORDERCERTNO, customerInfo.get(Order.ORDERCERTNO));
		params.put(Order.CUSTOMERNAME, customerInfo.get(Order.CUSTOMERNAME));
		params.put(Order.ORDERMOBILE, customerInfo.get(Order.ORDERMOBILE));
		params.put(Order.ORDERORGCODE, (String)orderInfo.get(Order.ORDERORGCODE));
		params.put(Order.ORDERAPPLYDATE, customerInfo.get(Order.ORDERAPPLYDATE));
		params.put(Order.ORDERPERIOD, (String)customerInfo.get(Order.ORDERPERIOD));
//		params.put(Order.BSID, (String)orderInfo.get(Order.BSID));
//		params.put(Order.REMINDTIME, (String)orderInfo.get(Order.REMINDTIME));
		
		//特殊业务需要上送的字段
		params.put(Order.SPECIALITEMID, (String)customerInfo.get(Order.BSID));
		params.put(Order.SPECIALREMARK, (String)customerInfo.get(Order.SPECIALREMARK));
		//特殊业务ID为“1”时（大额取现），且当客户填写了大额取现金额时送，默认送“CNY”
		if(BranchOrderUtils.isStrEquals(customerInfo.get(Order.BSID), "1")){
			params.put(Order.CURRENCY, "CNY");
			//特殊业务ID为“1”时（大额取现），且当客户填写了大额取现金额时送。前端页面控制金额只能输入整数，且大于50000，上送整数位，最大12位数字，不能有小数点及小数位。
			params.put(Order.AMOUNT, (String)customerInfo.get(Order.AMOUNTOFCASH));
		}else if(BranchOrderUtils.isStrEquals(customerInfo.get(Order.BSID), "2")){
			params.put(Order.CURRENCY, (String)customerInfo.get(Order.CURRENCY));
			//特殊业务ID为“1”时（大额取现），且当客户填写了大额取现金额时送。前端页面控制金额只能输入整数，且大于50000，上送整数位，最大12位数字，不能有小数点及小数位。
			params.put(Order.AMOUNT, (String)customerInfo.get(Order.AMOUNTOFCASH));
		}
		
		params.put(Order.VALIDATIONCHAR, identifyCodeStr);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnOrderApplyCallBack");
	}
	
	public void requestPsnOrderApplyCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		BranchOrderDataCenter.getInstance().setOrderApplyMap(map);
		startActivityForResult(new Intent(this, BranchOrderVipSpecialResultInfoActivity.class), 
				BranchOrderDataCenter.VIP);
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
			default:
				break;
			}
			break;
		}
	}
}
