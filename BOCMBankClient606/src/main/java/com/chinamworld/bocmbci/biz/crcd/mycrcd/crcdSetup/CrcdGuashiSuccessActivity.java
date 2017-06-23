package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 
 * 信用卡挂失成功
 * 
 * @author huangyuchao
 * 
 */
public class CrcdGuashiSuccessActivity extends CrcdBaseActivity {

	/** 信用卡挂失 */
	private View view;

	Button sureBtn;

	TextView tv_cardNumber, tv_cardtype, tv_cardnickname, tv_cardsendtype,
			tv_cardsendaddress;
	private Button backButton = null;
	private TextView titleText = null;
	private String accountNumber = null;
	private String nickName = null;
	private String strAccountType = null;


	/** 挂失手续费试算 */
	private String getChargeFlag;
	/* 挂失*/
	private LinearLayout ll_lossfee;
	private String lossFeetv=null;
	private String lossFeeCurrency=null;
	private TextView crcd_guashi_lossFee;
	private TextView crcd_guashi_lossfeecurrency;
	/* 补卡*/	
	private LinearLayout ll_reportfee;
	private String reportFeetv=null;
	private String reportFeeCurrency=null;
	private TextView crcd_guashi_reportFee;
	private TextView crcd_guashi_portfeecurrency;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_creditcard_guashi_title));
		if (view == null) {
			view = addView(R.layout.crcd_guashi_info_success);
		}
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		nickName = getIntent().getStringExtra(Crcd.CRCD_NICKNAME_RES);
		strAccountType = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTTYPE_RES);
		init();

	}

	LinearLayout cardsendtypeLayout, cardsendaddressLayout;

	static int guaShiType;

	public void init() {
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.GONE);

		guaShiType = CrcdGuashiInfoActivity.guaShiType;

		cardsendtypeLayout = (LinearLayout) findViewById(R.id.cardsendtypeLayout);
		cardsendaddressLayout = (LinearLayout) findViewById(R.id.cardsendaddressLayout);

		ll_lossfee = (LinearLayout) findViewById(R.id.ll_lossfee);	
		crcd_guashi_lossFee = (TextView) findViewById(R.id.crcd_guashi_lossfee);
		crcd_guashi_lossfeecurrency= (TextView) findViewById(R.id.crcd_guashi_lossfeecurrency);
		ll_reportfee = (LinearLayout) findViewById(R.id.ll_reportfee);	
		crcd_guashi_reportFee = (TextView) findViewById(R.id.crcd_guashi_portfee);
		crcd_guashi_portfeecurrency= (TextView) findViewById(R.id.crcd_guashi_portfeecurrency);
		

		/** 手续费试算 */
		/** 手续费试算 */
		Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
				.getCommissionChargeMap();

		if (!StringUtil.isNullOrEmpty(chargeMissionMap)) {
			getChargeFlag =(String)chargeMissionMap.get(Crcd.CRCD_GETCHARGEFLAG);
			 lossFeetv = (String) chargeMissionMap.get(Crcd.CRCD_LOSSFEE);
			 lossFeeCurrency = (String) chargeMissionMap
					.get(Crcd.CRCD_LOSSFEECURRENCY);
			if (!StringUtil.isNullOrEmpty(lossFeetv)) {
				lossFeetv = StringUtil.parseStringCodePattern(lossFeeCurrency,
						lossFeetv, 2);
			}
		     reportFeetv = (String) chargeMissionMap
					.get(Crcd.CRCD_REPORTFEE);
			 reportFeeCurrency = (String) chargeMissionMap
					.get(Crcd.CRCD_REPORTFEECURRENCY);			
			if (!StringUtil.isNullOrEmpty(reportFeetv)) {
				reportFeetv = StringUtil.parseStringCodePattern(reportFeeCurrency,
						reportFeetv, 2);
			}
			
		}
		crcd_guashi_lossFee.setText(lossFeetv);
		if(StringUtil.isNullOrEmpty(lossFeeCurrency)){
			crcd_guashi_lossfeecurrency.setText("-");	
		}else{
			crcd_guashi_lossfeecurrency.setText(LocalData.Currency.get(lossFeeCurrency));
		}
		
		crcd_guashi_reportFee.setText(reportFeetv);
		if(StringUtil.isNullOrEmpty(lossFeeCurrency)){
			crcd_guashi_portfeecurrency.setText("-");	
		}else{
			crcd_guashi_portfeecurrency.setText(LocalData.Currency.get(reportFeeCurrency));
		}	
		titleText = (TextView) findViewById(R.id.tv_acc_loss_actnum);
		String text1 = getResources().getString(R.string.crcd_setup_guashi1);
		String text2 = getResources().getString(R.string.crcd_setup_buka1);
		// 0=挂失1=挂失及补卡
		if (0 == guaShiType) {
			cardsendtypeLayout.setVisibility(View.GONE);
			cardsendaddressLayout.setVisibility(View.GONE);
			ll_lossfee.setVisibility(View.VISIBLE);
			ll_reportfee.setVisibility(View.GONE);				
			titleText.setText(text1);
		
		
			
		} else if (1 == guaShiType) {
			cardsendtypeLayout.setVisibility(View.VISIBLE);
			cardsendaddressLayout.setVisibility(View.VISIBLE);
			ll_lossfee.setVisibility(View.VISIBLE);
			ll_reportfee.setVisibility(View.VISIBLE);	
			titleText.setText(text2);
	
		
			
		}

		tv_cardNumber = (TextView) findViewById(R.id.tv_cardNumber);
		tv_cardNumber.setText(StringUtil.getForSixForString(accountNumber));

		tv_cardtype = (TextView) findViewById(R.id.tv_cardtype);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_cardtype);
		tv_cardtype.setText(strAccountType);

		tv_cardnickname = (TextView) findViewById(R.id.tv_cardnickname);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_cardnickname);
		tv_cardnickname.setText(nickName);

		tv_cardsendtype = (TextView) findViewById(R.id.tv_cardsendtype);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_cardsendtype);
		tv_cardsendtype.setText(CrcdGuashiConfirmActivity.mailAddressType);

		tv_cardsendaddress = (TextView) findViewById(R.id.tv_cardsendaddress);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_cardsendaddress);
		tv_cardsendaddress.setText(CrcdGuashiConfirmActivity.mailAddress);

		sureBtn = (Button) findViewById(R.id.sureButton);
		sureBtn.setOnClickListener(sureClick);
	}

	OnClickListener sureClick = new View.OnClickListener() {

		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		};

	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
