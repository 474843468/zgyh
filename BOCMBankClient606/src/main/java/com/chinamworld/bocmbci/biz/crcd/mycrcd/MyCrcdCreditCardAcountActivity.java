package com.chinamworld.bocmbci.biz.crcd.mycrcd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Bocnet;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.bocnet.BocnetBaseActivity;
import com.chinamworld.bocmbci.biz.bocnet.BocnetDataCenter;
import com.chinamworld.bocmbci.biz.bocnet.creditcard.CreditCardBillQueryActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 信用卡 持卡登录账户主页
 * @author sunh
 *
 */
public class MyCrcdCreditCardAcountActivity extends BocnetBaseActivity{

	/** 人民币元，美元， */
	private Button btn_bill_query, btn_regi;
	/** 卡类型， 卡号， 卡昵称 */
	private TextView crcd_type_value, crcd_account_num, crcd_account_nickname;
	
	/** 账户基本信息 */
	private RadioButton card__btn1;
	/** 账面信息 */
	private RadioButton card__btn2;
	/** 账户设置 */
	private RadioButton card__btn3;
	
	/** 账户ID accountId */
	private String accountId=null;
	/** 信用卡卡号 */
	private String accountNumber=null;
	/** 币种 */
	private String currencyCode=null;
	
	/** 卡类型 */
	private String cardType;
	
	/** 卡积分*/
	private String crcdPoint;
	
	/** 用户选择的信用卡数据 */
	Map<String, Object> crcdAccount;
	/** 用户选择的信用卡的币种对应的详情 */
	Map<String, Object> mapResult;
	/** 账户基本信息 */
	/** 信用卡卡号 */
	private TextView card_acctnum;
	/** 户名 */
	private TextView card_acctname;
	/** 开户行 */
	private TextView card_acctbank;
	/** 卡类型 */
	private TextView card_type;
	/** 产品名称 */
	private TextView productName;
	/** 主副卡标识 */
	private TextView card_carflag;
	/** 卡等级 */
	private TextView card_level;
	/** 启用日期 */
	private TextView card_startdate;
	/** 卡有效期截止日 */
	private TextView card_avaidate;
	/** 账单日 */
	private TextView card_billdate;
	/** 本期到期还款日 */
	private TextView card_duedate;
	/** 积分 */
	private TextView card_crcdscore;
	/** 信用卡审批额度 */
	private TextView card_approvallimit;
	private TextView card_approvallimit1;
	
	/** 取现额度 */
	private TextView card_cashLimit;
	private TextView card_cashLimit1;
	private TextView card_cashLimit2;
	/** 卡状态 */
	private TextView card_carstatus;
	/** 年费减免情况 */
	private TextView card_annualfee;

	/** 账户账面信息 */
	/** 账户账面信息列表 */
	private List<Map<String, Object>> actlist = new ArrayList<Map<String, Object>>();

	/** 账户余额 */
	private TextView card_realtimebalance;
	/** 总可用额 */
	private TextView card_toltalBalance;
	/** 本期账单金额 */
	private TextView card_billamout;
	/** 本期未还金款额 */
	private TextView card_havenotrepayamout;
	/** 本期最低还款额 */
	private TextView card_billLimitamout;
	/** 分期额度 */
	private TextView card_dividedpayLimit;
	/** 分期可用额 */
	private TextView card_dividedpayavaibalance;
	/** 取现可用额 */
	private TextView card_cashbalance;
	
	/** 当前币种代码 */
	String currency;
	String currency1, currency2;

	int currencyflg = 0;// 第一币种1 第二币种 2
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.bocnet_acc_detail_new);
		setTitle(getString(R.string.acc_main_title));
		crcdPoint=getIntent().getStringExtra(Crcd.CRCD_CRCDPOINT);
		accountId=getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		init();
		setBtnListeners();
		initdata();	
	}
	
	private void init() {
		setLeftButtonGone();
		setLeftButtonPopupGone();
		setRightButton(getString(R.string.exit), exitClickListener);
		

		btn_bill_query = (Button)findViewById(R.id.btn_bill_query);
		btn_regi = (Button)findViewById(R.id.btn_regi);
		

		crcd_type_value = (TextView)findViewById(R.id.crcd_type_value);
		crcd_account_num = (TextView)findViewById(R.id.crcd_account_num);
		crcd_account_nickname = (TextView)findViewById(R.id.crcd_account_nickname);
		
		
		
		card__btn1= (RadioButton)findViewById(R.id.card__btn1);
		card__btn2= (RadioButton)findViewById(R.id.card__btn2);
		card__btn3= (RadioButton)findViewById(R.id.card__btn3);
		/** 账户基本信息 */
		card_acctnum = (TextView)findViewById(R.id.card_acctnum);
		card_acctname = (TextView)findViewById(R.id.card_acctname);
		card_acctbank = (TextView)findViewById(R.id.card_acctbank);
		card_type = (TextView)findViewById(R.id.card_type);
		productName= (TextView)findViewById(R.id.productName);
		card_carflag = (TextView)findViewById(R.id.card_carflag);
		card_level = (TextView)findViewById(R.id.card_level);
		card_startdate = (TextView)findViewById(R.id.card_startdate);
		card_avaidate = (TextView)findViewById(R.id.card_avaidate);
		card_billdate = (TextView)findViewById(R.id.card_billdate);
		card_duedate = (TextView)findViewById(R.id.card_duedate);
		card_crcdscore = (TextView)findViewById(R.id.card_crcdscore);
		card_approvallimit = (TextView)findViewById(R.id.card_approvallimit);
		card_approvallimit1= (TextView)findViewById(R.id.card_approvallimit1);
		card_carstatus = (TextView)findViewById(R.id.card_carstatus);
			/** 取现额度 */
		card_cashLimit = (TextView) findViewById(R.id.card_cashLimit);
		card_cashLimit1 = (TextView) findViewById(R.id.card_cashLimit1);
		card_cashLimit2 = (TextView) findViewById(R.id.card_cashLimit2);
		card_cashbalance = (TextView)findViewById(R.id.card_cashbalance);
		card_annualfee = (TextView)findViewById(R.id.card_annualfee);
		/** 账户账面信息 */
		/** 账户余额 */
		card_realtimebalance = (TextView)findViewById(R.id.card_realtimebalance);
		/** 总可用额 */
		card_toltalBalance= (TextView)findViewById(R.id.card_toltalBalance);
		/** 本期账单金额 */
		card_billamout = (TextView)findViewById(R.id.card_billamout);
		/** 本期未还金款额 */
		card_havenotrepayamout = (TextView)findViewById(R.id.card_havenotrepayamout);
		/** 本期最低还款额 */
		card_billLimitamout = (TextView)findViewById(R.id.card_billLimitamout);
		/** 分期额度 */
		card_dividedpayLimit = (TextView)findViewById(R.id.card_dividedpayLimit);
		/** 分期可用额 */
		card_dividedpayavaibalance = (TextView)findViewById(R.id.card_dividedpayavaibalance);
		/** 取现额度 */
		card_cashLimit = (TextView)findViewById(R.id.card_cashLimit);
		/** 取现可用额 */
		card_cashbalance = (TextView)findViewById(R.id.card_cashbalance);
		

		

	}
	
	private void setBtnListeners(){

		btn_bill_query.setOnClickListener(this);
		btn_regi.setOnClickListener(this);
	}
	
	private void initdata(){
		
		Map<String, Object> loginInfo = BocnetDataCenter.getInstance().getLoginInfo();

		if (!StringUtil.isNullOrEmpty(loginInfo)) {
			crcd_type_value.setText(LocalData.AccountType.get((String)loginInfo.get(Bocnet.ACCOUNTTYPE)));
			crcd_account_num.setText(StringUtil.getForSixForString((String)loginInfo.get(Bocnet.ACCOUNTNUMBER)));
			crcd_account_nickname.setText(StringUtil.valueOf1((String)loginInfo.get(Bocnet.NAME)));
			
		
		}
		
		Map<String, Object> result =TranDataCenter.getInstance().getCrcdGeneralInfo();
		actlist = (List<Map<String, Object>>) result.get(Crcd.CRCD_ACTLIST);
		BiiHttpEngine.dissMissProgressDialog();

		/** 信用卡卡号 */
		card_acctnum.setText(StringUtil.getForSixForString(String
				.valueOf(result.get(Crcd.CRCD_ACCTNUM))));
		/** 户名 */
		card_acctname
				.setText(String.valueOf(result.get(Crcd.CRCD_ACCTNAME_RES)));
		/** 开户行 */
		card_acctbank.setText(String.valueOf(result.get(Crcd.CRCD_ACCTBANK)));
		/** 卡类型 */
		card_type.setText(cardType);		
		/** 产品名称 */
		productName.setText(String.valueOf(result.get(Crcd.CRCD_PRODUCTNAME)));
		/** 主副卡标识 */
		if("1".equals(String.valueOf(result.get(Crcd.CRCD_CARFLAG)))){
			card_carflag.setText(getResources().getString(R.string.mycrcd_carFlag_zhuka));	
		}else if ("0".equals(String.valueOf(result.get(Crcd.CRCD_CARFLAG)))){
			card_carflag.setText(getResources().getString(R.string.mycrcd_carFlag_fushuka));	
		}else{
			card_carflag.setText("-");
		}				
		/** 卡等级 */
		card_level.setText("-");
		/** 启用日期 */
		card_startdate.setText(String.valueOf(result.get(Crcd.START_DATE)));
		/** 卡有效期截止日 */
		card_avaidate
				.setText(String.valueOf(result.get(Crcd.CRCD_CARAVAIDATE)));
		/** 账单日 */
		card_billdate.setText("每月"+String.valueOf(result.get(Crcd.CRCD_BILLDATE))+"日");
		/** 本期到期还款日 */
		if(StringUtil.isNullOrEmpty(result.get(Crcd.CRCD_DUEDATE_RES))){
			card_duedate.setText("-");
		}else{
			card_duedate.setText(DateUtils.cardDateFormatter(String.valueOf(result.get(Crcd.CRCD_DUEDATE_RES))));
		}
		/** 积分 */
		card_crcdscore.setText(crcdPoint);
		/** 信用卡审批额度 */
		if (actlist.size() == 1) {
			String totalLimt=(String)actlist.get(0).get(Crcd.CRCD_TOTALLIMIT);
			String bizhong=(String)actlist.get(0).get(Crcd.CRCD_CURRENCY_RES);			
			card_approvallimit.setText(StringUtil.parseStringPattern(totalLimt,2)+" "+LocalData.Currency.get(bizhong));
			
			String cashLimit1=(String)actlist.get(0).get(Crcd.CRCD_CASHLIMIT);
			card_cashLimit1.setText(StringUtil.parseStringPattern(cashLimit1,2)+" "+LocalData.Currency.get(bizhong));		
			
		}
		if (actlist.size() > 1) {
			String totalLimt=(String)actlist.get(0).get(Crcd.CRCD_TOTALLIMIT_NEW);
			String bizhong=(String)actlist.get(0).get(Crcd.CRCD_CURRENCY_RES);			
			card_approvallimit.setText(StringUtil.parseStringPattern(totalLimt,2)+"\n"+LocalData.Currency.get(bizhong));
			String cashLimit1=(String)actlist.get(0).get(Crcd.CRCD_CASHLIMIT);
			card_cashLimit1.setText(StringUtil.parseStringPattern(cashLimit1,2)+" "+LocalData.Currency.get(bizhong));		
	
			card_approvallimit1.setVisibility(View.VISIBLE);
			String totalLimt1=(String)actlist.get(1).get(Crcd.CRCD_TOTALLIMIT_NEW);
			String bizhong1=(String)actlist.get(1).get(Crcd.CRCD_CURRENCY_RES);						
			card_approvallimit1.setText(StringUtil.parseStringPattern(totalLimt1,2)+"\n"+LocalData.Currency.get(bizhong1));
			card_cashLimit2.setVisibility(View.VISIBLE);
			String cashLimit2=(String)actlist.get(1).get(Crcd.CRCD_CASHLIMIT);
			card_cashLimit2.setText(StringUtil.parseStringPattern(cashLimit2,2)+" "+LocalData.Currency.get(bizhong1));		
	
		}
		/** 卡状态 */
		if(StringUtil.isNullOrEmpty(result.get(Crcd.CRCD_CARSTATUS))){
			card_carstatus.setText("正常");	
		}else{
			card_carstatus.setText(carStatusTypegMap.get(String.valueOf(result.get(Crcd.CRCD_CARSTATUS))));	
		}
		/** 年费减免情况  1免年费0不免年费*/
		if("1".equals(String.valueOf(result.get(Crcd.CRCD_ANNUALFEE)))){
			String date=String.valueOf(result.get(Crcd.CRCD_WAIVEMEMFEEENDDATE));
			if(StringUtil.isNull(date)){
				date="-";
			}else{
				date=DateUtils.cardDateFormat(date);	
			}
			card_annualfee.setText(getResources().getString(R.string.mycrcd_annualfee_yes)+" 至 "+date);
		}else{
			card_annualfee.setText(getResources().getString(R.string.mycrcd_annualfee_no));	
		}
		
		if (actlist.size() == 1) {
			findViewById(R.id.lin_waibi).setVisibility(View.GONE);
			currency = String.valueOf(actlist.get(0)
					.get(Crcd.CRCD_CURRENCY_RES));
			Button btn = (Button) findViewById(R.id.btn_renminbi);
			btn.setText(LocalData.Currency.get(currency));
			setsecondtab(actlist.get(0));

		}
		if (actlist.size() > 1) {
			currency1 = String.valueOf(actlist.get(0).get(
					Crcd.CRCD_CURRENCY_RES));
			currency2 = String.valueOf(actlist.get(1).get(
					Crcd.CRCD_CURRENCY_RES));
			Button btn = (Button) findViewById(R.id.btn_renminbi);
			btn.setText(LocalData.Currency.get(currency1));
			Button btn2 = (Button) findViewById(R.id.btn_waibi);
			btn2.setText(LocalData.Currency.get(currency2));
			setsecondtab(actlist.get(0));
		}

	}

	/** 展示 账户账面信息 */
	private void setsecondtab(Map<String, Object> map) {

		String currency = String.valueOf(map.get(Crcd.CRCD_CURRENCY_RES));
		/** 外币不显示申请分期 */
		if (currency.equals("001")) {
			currencyflg = 1;
//			card_fenqi.setVisibility(View.VISIBLE);
		} else {
//			card_fenqi.setVisibility(View.GONE);
			currencyflg = 2;
		}

		
		/** 账户余额 */
	
	
		String flag=String.valueOf(map.get(Crcd.CRCD_RTBALANCEFLAG));
		flag = flagConvert(flag);
		card_realtimebalance.setText(flag+StringUtil.parseStringPattern(
				String.valueOf(map.get(Crcd.CRCD_REALTIMEBALANCE)), 2));
		/** 总可用额 */
		card_toltalBalance.setText(StringUtil.parseStringPattern(
				String.valueOf(map.get(Crcd.CRCD_TOLTALBALANCE)), 2));
		/** 本期账单金额 */
		card_billamout.setText(StringUtil.parseStringPattern(
				String.valueOf(map.get(Crcd.CRCD_BILLAMOUT)), 2));
		/** 本期未还金款额 */
		card_havenotrepayamout.setText(StringUtil.parseStringPattern(
				String.valueOf(map.get(Crcd.CRCD_HAVENOTREPAYAMOUT)), 2));
		/** 本期最低还款额 */
		card_billLimitamout.setText(StringUtil.parseStringPattern(
				String.valueOf(map.get(Crcd.CRCD_BILLLIMITAMOUT)), 2));
		/** 分期额度 */
		card_dividedpayLimit.setText(StringUtil.parseStringPattern(
				String.valueOf(map.get(Crcd.CRCD_DIVIDEDPAYLIMIT)), 2));
		/** 分期可用额 */
		card_dividedpayavaibalance.setText(StringUtil.parseStringPattern(
				String.valueOf(map.get(Crcd.CRCD_DIVIDEDPAYAVAIBABLANCE)), 2));
		/** 取现额度 */
		card_cashLimit.setText(StringUtil.parseStringPattern(
				String.valueOf(map.get(Crcd.CRCD_CASHLIMIT)), 2));
		/** 取现可用额 */
		card_cashbalance.setText(StringUtil.parseStringPattern(
				String.valueOf(map.get(Crcd.CRCD_CASHBALANCE)), 2));

	}
	@Override
	protected void onResume() {
		super.onResume();
		if(getLeftBtnVisible() ==View.VISIBLE)
		setLeftSelectedPosition("myCrcd_1");
	}
	
	
	
	/** 展示 账户账面信息 */
	/** 账户基本信息 */
	public void card__btn1Onclick(View v) {
		findViewById(R.id.card__lin1).setVisibility(View.VISIBLE);
		findViewById(R.id.card__lin2).setVisibility(View.GONE);
		findViewById(R.id.card__lin3).setVisibility(View.GONE);

	}

	/** 账户账面信息 */
	public void card__btn2Onclick(View v) {
		findViewById(R.id.card__lin1).setVisibility(View.GONE);
		findViewById(R.id.card__lin2).setVisibility(View.VISIBLE);
		findViewById(R.id.card__lin3).setVisibility(View.GONE);

	}
	/** 币种一 */
	@SuppressLint("ResourceAsColor")
	public void btn_renminbiOnclick(View v) {
		findViewById(R.id.img_renminbi).setVisibility(View.VISIBLE);
		findViewById(R.id.img_waibi).setVisibility(View.GONE);
		setsecondtab(actlist.get(0));
	}

	/** 币种二 */
	@SuppressLint("ResourceAsColor")
	public void btn_waibiOnclick(View v) {
		findViewById(R.id.img_renminbi).setVisibility(View.GONE);
		findViewById(R.id.img_waibi).setVisibility(View.VISIBLE);
		setsecondtab(actlist.get(1));
	}

	/** 卡状态*/
	public  Map<String, String> carStatusTypegMap = new HashMap<String, String>() {
		{
			put("ACCC", "销户销卡");
			put("ACTP", "卡未激活");
			put("CANC", "卡片取消");
			put("DCFR", "欺诈拒绝");
			put("FRAU", "欺诈冻结");
			put("FUSA", "首次使用超限");
			put("LOST", "挂失");
			put("LOCK", "锁卡");
			put("PIFR", "没收卡");			
			put("PINR", "密码输入错误超次");			
			put("QCPB", "卡预销户");			
			put("REFR", "欺诈卡，需联系发卡行");			
			put("STOL", "偷窃卡");			
			put("STPP", "止付");			
			put("BFRA", "分行冻结");
			put("CFRA", "催收冻结");
			put("SFRA", "客服冻结");
//			put("NORMAL", "正常");
		}
	};	
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		case R.id.btn_bill_query:
			startActivity(new Intent(this, CreditCardBillQueryActivity.class).putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId));
			break;
		case R.id.btn_regi:
			eBankingFlag();
			break;
		default:
			break;
		}
	}
	private String flagConvert(String flag) {
		if ("0".equals(flag)) {
			flag = "欠款";
		} else if ("1".equals(flag)) {
			flag = "存款";
		} else if ("2".equals(flag)) {
			flag = "";
		}else{
			flag = "";
		}

		return flag;
	}
}
