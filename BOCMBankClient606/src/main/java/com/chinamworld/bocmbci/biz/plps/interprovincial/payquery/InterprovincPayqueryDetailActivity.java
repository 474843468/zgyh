package com.chinamworld.bocmbci.biz.plps.interprovincial.payquery;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/*
 * 缴纳查询详情页面
 * @author zxj
 */
public class InterprovincPayqueryDetailActivity extends PlpsBaseActivity{
	
	/**信息数据*/
	private Map<String, Object> interDetailResult = new HashMap<String, Object>();
	/**缴款帐号*/
	private TextView payAccountTextView;
	private String payAccount;
	/**决定书编号*/
	private TextView decisionNoTextView;
	private String decisionNo;
	/**驾驶证号*/
	private TextView driverLicenseNoTextView;
	private String driverLicenseNo;
	/**当事人*/
	private TextView partyTextView;
	private String party;
	/**处理时间*/
	private TextView dealTimeTextView;
	private String dealTime;
	/**处理机关*/
	private TextView dealAuthorityTextView;
	private String dealAuthority;
	/**处理机关名称*/
	private TextView dealAuthorityNameTextView;
	private String dealAuthorityName;
	/**违法地点*/
	private TextView illegalPlaceTextView;
	private String illegalPlace;
	/**罚款金额*/
	private TextView fineAmountTextView;
	private String fineAmount;
	/**加处罚款*/
	private TextView additionalAmountTextView;
	private String additionalAmount;
	/**合计金额*/
	private TextView totalAmountTextView;
	private String totalAmount;
	/**交易时间*/
	private TextView transTimeTextView;
	private String transTime;
	/**交易状态*/
	private TextView statusTextView;
	private String status;
	/**交易渠道*/
	private TextView channelTextView;
	private String channel;
	/**撤销原因*/
	private TextView cancelReasonTextView;
	private String cancelReason;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("详情");
		inflateLayout(R.layout.plps_interprov_payquery_detail);
		interDetailResult = (Map<String, Object>)PlpsDataCenter.getInstance().getInterDetailQueryResult();
		getData();
		init();
	}
	
	private void getData(){
		payAccount = (String)interDetailResult.get(Plps.PAYACCOUNT);
		decisionNo = (String)interDetailResult.get(Plps.DECISIONNO);
		driverLicenseNo = (String)interDetailResult.get(Plps.DRIVERLICENSENO);
		party = (String)interDetailResult.get(Plps.PARTY);
		dealTime = (String)interDetailResult.get(Plps.DEALTIME);
		dealAuthority = (String)interDetailResult.get(Plps.DEALAUTHORITY);
		dealAuthorityName = (String)interDetailResult.get(Plps.DEALAUTHORITYNAME);
		illegalPlace = (String)interDetailResult.get(Plps.ILLEGAPLACE);
		fineAmount = (String)interDetailResult.get(Plps.FINEAMOUNT);
		additionalAmount = (String)interDetailResult.get(Plps.ADDITIONALAMOUNT);
		totalAmount = (String)interDetailResult.get(Plps.TOTALAMOUNT);
		transTime = (String)interDetailResult.get(Plps.TRANSTIME);
		status = (String)interDetailResult.get(Plps.STATUS);
		channel = (String)interDetailResult.get(Plps.CHANNELL);
		cancelReason = (String)interDetailResult.get(Plps.CANCELREASON);
	}
	
	private void init(){
		payAccountTextView = (TextView)findViewById(R.id.payment_amount);
		decisionNoTextView = (TextView)findViewById(R.id.decisionno);
		driverLicenseNoTextView = (TextView)findViewById(R.id.driverLicenseno);
		partyTextView = (TextView)findViewById(R.id.party);
		dealTimeTextView = (TextView)findViewById(R.id.dealtime);
		dealAuthorityTextView = (TextView)findViewById(R.id.dealauthority);
		dealAuthorityNameTextView = (TextView)findViewById(R.id.dealauthorityname);
		illegalPlaceTextView = (TextView)findViewById(R.id.illegalplace);
		fineAmountTextView = (TextView)findViewById(R.id.the_fines);
		additionalAmountTextView = (TextView)findViewById(R.id.additional);
		totalAmountTextView = (TextView)findViewById(R.id.total_amount);
		transTimeTextView = (TextView)findViewById(R.id.trading_hours);
		statusTextView = (TextView)findViewById(R.id.trading_status);
		channelTextView = (TextView)findViewById(R.id.trading_channel);
		cancelReasonTextView = (TextView)findViewById(R.id.case_for_revocation);
		
		PlpsUtils.setOnShowAllTextListener(this, payAccountTextView,decisionNoTextView, driverLicenseNoTextView,
				partyTextView, dealTimeTextView, dealAuthorityTextView, dealAuthorityNameTextView, illegalPlaceTextView,
				fineAmountTextView, additionalAmountTextView, totalAmountTextView, transTimeTextView, statusTextView,channelTextView, cancelReasonTextView);
		
		initAndData();
	}
	
	private void initAndData(){
		if(!StringUtil.isNullOrEmpty(payAccount)){
			payAccountTextView.setText(StringUtil.getForSixForString(payAccount));
		}
		if(!StringUtil.isNullOrEmpty(decisionNo)){
			decisionNoTextView.setText(decisionNo);
		}
		if(!StringUtil.isNullOrEmpty(driverLicenseNo)){
			driverLicenseNoTextView.setText(driverLicenseNo);
		}
		if(!StringUtil.isNullOrEmpty(party)){
			partyTextView.setText(party);
		}
		if(!StringUtil.isNullOrEmpty(dealTime)){
			dealTimeTextView.setText(dealTime);
		}
		if(!StringUtil.isNullOrEmpty(dealAuthority)){
			dealAuthorityTextView.setText(dealAuthority);
		}
		if(!StringUtil.isNullOrEmpty(dealAuthorityName)){
			dealAuthorityNameTextView.setText(dealAuthorityName);
		}
		if(!StringUtil.isNullOrEmpty(illegalPlace)){
			illegalPlaceTextView.setText(illegalPlace);
		}
		if(!StringUtil.isNullOrEmpty(fineAmount)){
			fineAmountTextView.setText(StringUtil.parseStringPattern(fineAmount, 2));
		}
		if(!StringUtil.isNullOrEmpty(additionalAmount)){
			additionalAmountTextView.setText(StringUtil.parseStringPattern(additionalAmount, 2));
		}
		if(!StringUtil.isNullOrEmpty(totalAmount)){
			totalAmountTextView.setText(StringUtil.parseStringPattern(totalAmount, 2));
		}
		if(!StringUtil.isNullOrEmpty(transTime)){
			transTimeTextView.setText(transTime);
		}
		if(!StringUtil.isNullOrEmpty(status)){
			if(status.equals("1")){
				statusTextView.setText("罚款缴纳成功");
			}else if (status.equals("2")) {
				statusTextView.setText("撤销成功");
			}
		}
		if(!StringUtil.isNullOrEmpty(channel)){
			String channelValue = (String)PlpsDataCenter.channelType.get(channel);
			channelTextView.setText(channelValue);
		}
		if(!StringUtil.isNullOrEmpty(cancelReason)){
			if(cancelReason.equals("1")){
				cancelReasonTextView.setText("未缴款");
			}else if (cancelReason.equals("2")) {
				cancelReasonTextView.setText("操作错误");
			}
		}
	}
	/**确定按钮*/
	public void lastBtnClick(View v){
		finish();
	}
}
