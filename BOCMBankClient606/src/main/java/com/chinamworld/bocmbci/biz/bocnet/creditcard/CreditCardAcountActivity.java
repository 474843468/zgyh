package com.chinamworld.bocmbci.biz.bocnet.creditcard;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Bocnet;
import com.chinamworld.bocmbci.biz.bocnet.BocnetBaseActivity;
import com.chinamworld.bocmbci.biz.bocnet.BocnetDataCenter;
import com.chinamworld.bocmbci.biz.bocnet.BocnetUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 信用卡账户主页
 * @author panwe
 *
 */
public class CreditCardAcountActivity extends BocnetBaseActivity{

	/** 人民币元，美元， */
	private Button btn_renminbi, btn_waibi, btn_bill_query, btn_regi;
	/** 卡类型， 卡号， 卡昵称 */
	private TextView crcd_type_value, crcd_account_num, crcd_account_nickname;
	/** 账户类型，账户别名，账号，币种， 积分，总可用额， */
	private TextView tv_prodCode_detail, acc_accountnickname_value,
			tv_curCode_detail, tv_bill_type, tv_score, tv_prodTimeLimit_detail;
	/** 账面余额， 透支情况 */
	private TextView tv_buyPrice_detail, tv_buyPrice_name;
	/** 总授信额度, 分期额度， 分期可用额，存款利息， 存款利息税 */
	private TextView tv_applyObj_detail, tv_periodical_detail,
			tv_billdivide_keyong, tv_cun_lixi, tv_lixitax;
	/** 当前显示币种信息*/
	private String currencyCode;
	private String eBankingFlag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.bocnet_acc_detail);
		setTitle(getString(R.string.acc_main_title));
		setupViews();
		setBtnListeners();
		setDataForView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(getLeftBtnVisible() == View.VISIBLE)
		setLeftSelectedPosition("bocnet_1");
	}
	
	public void setupViews(){
		setLeftButtonGone();
		setRightButton(getString(R.string.exit), exitClickListener);
		
		btn_renminbi = (Button)findViewById(R.id.btn_renminbi);
		btn_waibi = (Button)findViewById(R.id.btn_waibi);
		btn_bill_query = (Button)findViewById(R.id.btn_bill_query);
		btn_regi = (Button)findViewById(R.id.btn_regi);
		
		crcd_type_value = (TextView)findViewById(R.id.crcd_type_value);
		crcd_account_num = (TextView)findViewById(R.id.crcd_account_num);
		crcd_account_nickname = (TextView)findViewById(R.id.crcd_account_nickname);
		
		tv_prodCode_detail = (TextView)findViewById(R.id.tv_prodCode_detail);
		acc_accountnickname_value = (TextView)findViewById(R.id.acc_accountnickname_value);
		tv_curCode_detail = (TextView)findViewById(R.id.tv_curCode_detail);
		tv_bill_type = (TextView)findViewById(R.id.tv_bill_type);
		tv_score = (TextView)findViewById(R.id.tv_score);
		tv_prodTimeLimit_detail = (TextView)findViewById(R.id.tv_prodTimeLimit_detail);
		tv_buyPrice_detail = (TextView)findViewById(R.id.tv_buyPrice_detail);
		tv_buyPrice_name = (TextView)findViewById(R.id.tv_buyPrice_name);
		
		tv_applyObj_detail = (TextView)findViewById(R.id.tv_applyObj_detail);
		tv_periodical_detail = (TextView)findViewById(R.id.tv_periodical_detail);
		tv_billdivide_keyong = (TextView)findViewById(R.id.tv_billdivide_keyong);
		tv_cun_lixi = (TextView)findViewById(R.id.tv_cun_lixi);
		tv_lixitax = (TextView)findViewById(R.id.tv_lixitax);
		
		if(BocnetUtils.isStrEquals(BocnetDataCenter.getInstance().getCardTypeStr(), "104")){
			findViewById(R.id.ll_cun_lixi).setVisibility(View.GONE);
			findViewById(R.id.ll_lixi_tax).setVisibility(View.GONE);
		}
	}
	
	private void setBtnListeners(){
		btn_renminbi.setOnClickListener(this);
		btn_waibi.setOnClickListener(this);
		btn_bill_query.setOnClickListener(this);
		btn_regi.setOnClickListener(this);
	}
	
	private void setDataForView(){
		Map<String, Object> loginInfo = BocnetDataCenter.getInstance().getLoginInfo();
		Map<String, Object> crcdDetail = BocnetDataCenter.getInstance().getCrcdDetail();
		initBankingFlagBtn(btn_regi);
		if (!StringUtil.isNullOrEmpty(loginInfo)) {
			crcd_type_value.setText(LocalData.AccountType.get((String)loginInfo.get(Bocnet.ACCOUNTTYPE)));
			crcd_account_num.setText(StringUtil.getForSixForString((String)loginInfo.get(Bocnet.ACCOUNTNUMBER)));
			crcd_account_nickname.setText(StringUtil.valueOf1((String)loginInfo.get(Bocnet.NAME)));
			
			tv_prodCode_detail.setText(LocalData.AccountType.get((String)loginInfo.get(Bocnet.ACCOUNTTYPE)));
			acc_accountnickname_value.setText(StringUtil.valueOf1((String)loginInfo.get(Bocnet.NAME)));
			tv_curCode_detail.setText(StringUtil.getForSixForString((String)loginInfo.get(Bocnet.ACCOUNTNUMBER)));
			
		}
		if(!StringUtil.isNullOrEmpty(crcdDetail)){
			tv_score.setText(StringUtil.valueOf1((String)crcdDetail.get(Bocnet.CONSUMPTIONPOINT)));
			List<Map<String, Object>> crcdAccountDetailList = (List<Map<String, Object>>) crcdDetail
					.get(Bocnet.CRCDACCOUNTDETAILLIST);
			if(!StringUtil.isNullOrEmpty(crcdAccountDetailList)){
				if(crcdAccountDetailList.size() == 1){
					btn_renminbi.setBackgroundResource(R.drawable.acc_top_left);
					Map<String, Object> map = crcdAccountDetailList.get(0);
					currencyCode = (String)map.get(Bocnet.CURRENCY);
					btn_renminbi.setText(LocalData.Currency.get(currencyCode));
					tv_prodTimeLimit_detail.setText(StringUtil.parseStringCodePattern(
							currencyCode, (String)map.get(Bocnet.TOTALBALANCE), 2));
					tv_buyPrice_detail.setText(StringUtil.parseStringCodePattern(currencyCode, 
							(String)map.get(Bocnet.CURRENTBALANCE), 2));
					String balanceFlag = (String)map.get(Bocnet.CURRENTBALANCEFLAG);
					if(BocnetUtils.isStrEquals(balanceFlag, "2"))
						tv_buyPrice_name.setVisibility(View.GONE);
					else 
						tv_buyPrice_name.setText(BocnetDataCenter.balanceFlag.get(balanceFlag));
					
					tv_applyObj_detail.setText(StringUtil.parseStringCodePattern(currencyCode, 
							(String)map.get(Bocnet.TOTALLIMIT), 2));
					tv_periodical_detail.setText(StringUtil.parseStringCodePattern(currencyCode, 
							(String)map.get(Bocnet.INSTALLMENTLIMIT), 2));
					tv_billdivide_keyong.setText(StringUtil.parseStringCodePattern(currencyCode, 
							(String)map.get(Bocnet.INSTALLMENTBALANCE), 2));
					tv_cun_lixi.setText(StringUtil.parseStringCodePattern(currencyCode, 
							(String)map.get(Bocnet.SAVINGINTEREST), 2));
					tv_lixitax.setText(StringUtil.parseStringCodePattern(currencyCode, 
							(String)map.get(Bocnet.SAVINGINTERESTTAX), 2));
				}else if(crcdAccountDetailList.size() > 1){
					btn_waibi.setVisibility(View.VISIBLE);
					btn_renminbi.setBackgroundResource(R.drawable.acc_top_left);
					for(Map<String, Object> map : crcdAccountDetailList){
						if(!StringUtil.isNullOrEmpty(map)){
							currencyCode = (String)map.get(Bocnet.CURRENCY);
							if(BocnetUtils.isStrEquals(currencyCode, ConstantGloble.PRMS_CURRENCYCODE_RMB)){
								btn_renminbi.setText(LocalData.Currency.get(currencyCode));
								tv_prodTimeLimit_detail.setText(StringUtil.parseStringCodePattern(
										currencyCode, (String)map.get(Bocnet.TOTALBALANCE), 2));
								tv_buyPrice_detail.setText(StringUtil.parseStringCodePattern(currencyCode, 
										(String)map.get(Bocnet.CURRENTBALANCE), 2));
								String balanceFlag = (String)map.get(Bocnet.CURRENTBALANCEFLAG);
								if(BocnetUtils.isStrEquals(balanceFlag, "2"))
									tv_buyPrice_name.setVisibility(View.GONE);
								else 
									tv_buyPrice_name.setText(BocnetDataCenter.balanceFlag.get(balanceFlag));
								
								tv_applyObj_detail.setText(StringUtil.parseStringCodePattern(currencyCode, 
										(String)map.get(Bocnet.TOTALLIMIT), 2));
								tv_periodical_detail.setText(StringUtil.parseStringCodePattern(currencyCode, 
										(String)map.get(Bocnet.INSTALLMENTLIMIT), 2));
								tv_billdivide_keyong.setText(StringUtil.parseStringCodePattern(currencyCode, 
										(String)map.get(Bocnet.INSTALLMENTBALANCE), 2));
								tv_cun_lixi.setText(StringUtil.parseStringCodePattern(currencyCode, 
										(String)map.get(Bocnet.SAVINGINTEREST), 2));
								tv_lixitax.setText(StringUtil.parseStringCodePattern(currencyCode, 
										(String)map.get(Bocnet.SAVINGINTERESTTAX), 2));
							}else{
								btn_waibi.setText(LocalData.Currency.get(currencyCode));
							}
						}
					}
				}
				
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_renminbi:
			btn_renminbi.setBackgroundResource(R.drawable.acc_top_left);
			btn_waibi.setBackgroundResource(R.drawable.acc_top_right);
			setCurrencyData(true);
			break;
		case R.id.btn_waibi:
			btn_renminbi.setBackgroundResource(R.drawable.acc_top_right);
			btn_waibi.setBackgroundResource(R.drawable.acc_top_left);
			setCurrencyData(false);
			break;
		case R.id.btn_bill_query:
			startActivity(new Intent(this, CreditCardBillQueryActivity.class));
			break;
		case R.id.btn_regi:
			eBankingFlag();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 设置币种信息
	 * @param isRMB
	 */
	private void setCurrencyData(boolean isRMB){
		Map<String, Object> crcdDetail = BocnetDataCenter.getInstance().getCrcdDetail();
		if(!StringUtil.isNullOrEmpty(crcdDetail)){
			List<Map<String, Object>> crcdAccountDetailList = (List<Map<String, Object>>) crcdDetail
					.get(Bocnet.CRCDACCOUNTDETAILLIST);
			if(!StringUtil.isNullOrEmpty(crcdAccountDetailList)){
				for(Map<String, Object> map : crcdAccountDetailList){
					if(!StringUtil.isNullOrEmpty(map)){
						currencyCode = (String)map.get(Bocnet.CURRENCY);
						if(isRMB){
							if(!BocnetUtils.isStrEquals(currencyCode, ConstantGloble.PRMS_CURRENCYCODE_RMB)){
								continue;
							}
						}else{
							if(BocnetUtils.isStrEquals(currencyCode, ConstantGloble.PRMS_CURRENCYCODE_RMB))
								continue;
						}
						tv_prodTimeLimit_detail.setText(StringUtil.parseStringCodePattern(
								currencyCode, (String)map.get(Bocnet.TOTALBALANCE), 2));
						tv_buyPrice_detail.setText(StringUtil.parseStringCodePattern(currencyCode, 
								(String)map.get(Bocnet.CURRENTBALANCE), 2));
						String balanceFlag = (String)map.get(Bocnet.CURRENTBALANCEFLAG);
						if(BocnetUtils.isStrEquals(balanceFlag, "2"))
							tv_buyPrice_name.setVisibility(View.GONE);
						else 
							tv_buyPrice_name.setText(BocnetDataCenter.balanceFlag.get(balanceFlag));
						
						tv_applyObj_detail.setText(StringUtil.parseStringCodePattern(currencyCode, 
								(String)map.get(Bocnet.TOTALLIMIT), 2));
						tv_periodical_detail.setText(StringUtil.parseStringCodePattern(currencyCode, 
								(String)map.get(Bocnet.INSTALLMENTLIMIT), 2));
						tv_billdivide_keyong.setText(StringUtil.parseStringCodePattern(currencyCode, 
								(String)map.get(Bocnet.INSTALLMENTBALANCE), 2));
						tv_cun_lixi.setText(StringUtil.parseStringCodePattern(currencyCode, 
								(String)map.get(Bocnet.SAVINGINTEREST), 2));
						tv_lixitax.setText(StringUtil.parseStringCodePattern(currencyCode, 
								(String)map.get(Bocnet.SAVINGINTERESTTAX), 2));
					}
				}
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
