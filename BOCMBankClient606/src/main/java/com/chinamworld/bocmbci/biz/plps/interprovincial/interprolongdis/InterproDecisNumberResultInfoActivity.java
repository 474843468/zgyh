package com.chinamworld.bocmbci.biz.plps.interprovincial.interprolongdis;

import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.utils.StringUtil;

public class InterproDecisNumberResultInfoActivity extends PlpsBaseActivity{
	
	
	/**决定书编号*/
	private String decisionNo;
	private TextView decisionNoTextView;
	/**驾驶证号*/
	private String driverLicenseNo;
	private TextView driverLicenseNoTextView;
	/**当事人*/
	private String party;
	private TextView partyTextView;
	/**处理时间*/
	private String dealTime;
	private TextView dealTimeTextView;
	/**机关名称*/
	private String dealAuthority;
	private TextView dealAuthorityTextView;
	/**处理机关名称*/
	private String dealAuthorityName;
	private TextView dealAuthorityNameTextView;
	/**违法地点*/
	private String illegalPlace;
	private TextView illegalPlaceTexyView;
	/**罚款金额*/
	private String fineAmount;
	private TextView fineAmountTextView;
	/**加处罚款*/
	private String additionalAmount;
	private TextView additionalAmountTextView;
	/**合计金额*/
	private String totalAmount;
	private TextView totalAmountTextView;
	/**缴费帐号*/
	private String paymentAccount;
	private TextView paymentAccountTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.plps_interprov_titlename);
		findViewById(R.id.ib_back).setVisibility(View.GONE);
		inflateLayout(R.layout.plps_interprov_dicisnumresultinfo);
		getIntentData();
		initData();
	}
	
	/**获取上个页面传来的数据*/
	private void getIntentData(){
		Map<String, Object> statusResult = PlpsDataCenter.getInstance().getInterMapSubmitresult();
		decisionNo = (String)statusResult.get(Plps.DECISIONNO);
		driverLicenseNo = (String)statusResult.get(Plps.DRIVERLICENSENO);
		party = (String)statusResult.get(Plps.PARTY);
		dealTime = (String)statusResult.get(Plps.DEALTIME);
		dealAuthority = (String)statusResult.get(Plps.DEALAUTHORITY);
		dealAuthorityName = (String)statusResult.get(Plps.DEALAUTHORITYNAME);
		illegalPlace = (String)statusResult.get(Plps.ILLEGAPLACE);
		fineAmount = (String)statusResult.get(Plps.FINEAMOUNT);
		additionalAmount = (String)statusResult.get(Plps.ADDITIONALAMOUNT);
		totalAmount = (String)statusResult.get(Plps.TOTALAMOUNT);
		paymentAccount = (String)statusResult.get(Plps.ACCOUNTNO);
	}
	/**初始化并加载数据*/
	private void initData(){
		decisionNoTextView = (TextView)findViewById(R.id.decisionno);
		driverLicenseNoTextView = (TextView)findViewById(R.id.divingno);
		partyTextView = (TextView)findViewById(R.id.party);
		dealTimeTextView = (TextView)findViewById(R.id.processtime);
		dealAuthorityTextView = (TextView)findViewById(R.id.deal_authori);
		dealAuthorityNameTextView = (TextView)findViewById(R.id.dealauthorityname);
		illegalPlaceTexyView = (TextView)findViewById(R.id.illegal_sites);
		fineAmountTextView = (TextView)findViewById(R.id.the_fines);
		additionalAmountTextView = (TextView)findViewById(R.id.additional);
		totalAmountTextView = (TextView)findViewById(R.id.total_amount);
		paymentAccountTextView = (TextView)findViewById(R.id.paymnetacct);
		
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
			illegalPlaceTexyView.setText(illegalPlace);
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
		if(!StringUtil.isNullOrEmpty(paymentAccount)){
			paymentAccountTextView.setText(StringUtil.getForSixForString(paymentAccount));
		}
	}
	
	public void finishBtnClick(View v){
		setResult(RESULT_OK);
		finish();
	}
}
