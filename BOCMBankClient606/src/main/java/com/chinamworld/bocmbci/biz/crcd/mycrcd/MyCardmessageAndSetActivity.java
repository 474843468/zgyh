package com.chinamworld.bocmbci.biz.crcd.mycrcd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.CardApplyTmpLimitPreActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.CrcdGuashiInfoActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.CrcdPsnCheckCloseActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.CrcdPsnCheckOpenActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.CrcdPsnEmailResetSubmitActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.CrcdServiceInfoActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.CrcdSetAutoForeignPayOffConfirmActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.CrcdSetConsumePWPreActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.CrcdpaymentSetupAutoActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.MyCrcdSetupReadActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery.CrcdHasQueryDetailActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery.CrcdHasQueryListActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.TransferManagerActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 我的信用卡 信息和设置界面
 * 
 * @author sunh
 * 
 */
public class MyCardmessageAndSetActivity extends CrcdBaseActivity implements
		OnClickListener, OnTouchListener {
	private static final String TAG = "MyCardmessageAndSetActivity";
	private View view;
	/** 账户基本信息 */
	private RadioButton card__btn1;
	/** 账面信息 */
	private RadioButton card__btn2;
	/** 账户设置 */
	private RadioButton card__btn3;

	/** 账户ID accountId */
	private String accountId = null;
	/** 信用卡卡号 */
	private String accountNumber = null;
	/** 币种 */
	private String currencyCode = null;

	private String accountIbkNum = null;
	private String nickName = null;
	/** 卡类型 */
	private String cardType;

	/** 卡积分 */
	private String crcdPoint;

	/** 用户选择的信用卡数据 */
	Map<String, Object> crcdAccount;
	// /** 用户选择的信用卡的币种对应的详情 */
	// Map<String, Object> mapResult;
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
	
	private FrameLayout ff_guashi;
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

	/** 账户设置 */
	LinearLayout card__lin3;
	/**
	 * 自动还款状态 =1,外币账单自动购汇设置=2,消费密码设置=3,POS消费验证方式=4,POS消费验密触发金额=5, 短信提示触发金额=6
	 */
	private int type = 0;
	/** 0=纸质对账单,1=电子对账单,2=推入式对账单 */
	private int billSetupId;

	/** 自动还款状态 */
	private TextView card_paymentstatus;
	private LinearLayout lin_card_paymentstatus;
	String[] paymentstatus;
	/** 外币账单自动购汇设置 */
	private TextView card_foreignpayoffstatus;
	private LinearLayout lin_card_foreignpayoffstatus;
	String[] foreignpayoffstatu;
	String autoopenflag = null;
	/** 查询密码设置 */
	private TextView card_searchpassword;
	private LinearLayout lin_card_searchpassword;

	/** 消费密码设置 */
	private TextView card_passwordstatus;
	private LinearLayout lin_card_passwordstatus;

	/** POS消费验证方式 */
	private TextView card_verifymode;
	private LinearLayout lin_card_verifymode;
	private String verifymodeflg;
	/** POS消费验密触发金额 */
	private TextView card_postriggeramount;
	private LinearLayout lin_card_postriggeramount;
	private String postriggeramount;
	/** 短信提示触发金额 */
	private TextView card_smstriggeramount;
	private LinearLayout lin_card_smstriggeramount;
	private String smstriggeramount;

	/** 对账单 open--开通 edit--编辑 */
	public String isOpenOrEdit;

	/** 纸质对账单 */
	private TextView card_paperstatment;
	private LinearLayout lin_card_paperstatment;
	protected String paperAddress;
	String[] paperstatment;
	/** 电子邮件对账单 */
	private TextView card_emailstatment;
	private LinearLayout lin_card_emailstatment;
	protected String email;
	String[] emailstatment;
	/** 手机对账单 */
	private TextView card_phonestatment;
	private LinearLayout lin_card_phonestatment;
	protected String mobile;
	String[] phonestatment;
	/** 全球交易人民币记账标识 */
	/** 全球交易人民币记账标识 */
	private TextView card_fchargeflag_txt;
	private TextView card_fchargeflag;
	private LinearLayout lin_card_fchargeflag;
	private RelativeLayout rla_card_fchargeflag;
	String[] fchargeflag;
	/** 开通标志 */
	private String openFlag = null;
	/** 显示。。。标题 */
	/** 外币账单自动购汇设置 */
	private TextView card_foreignpayoffstatus_txt;
	/** POS消费验密触发金额 */
	private TextView card_postriggeramount_txt;

	/** 可能要隐藏的布局 */
	/** 外币账单自动购汇设置 */
	private RelativeLayout rla_card_foreignpayoffstatus;
	/** POS消费验密触发金额 */
	private LinearLayout rla_card_postriggeramount;

	/** 选择的是第几个 */
	int position;
	/** 申请分期 */
	Button card_fenqi;

	/** 查看账单 */
	Button card_zhangdan;
	/** 1-全球交易人民币记账功能,2-账户设置,3-外币账单自动购汇设置,4-信用卡购汇还款 */
	private int tag = 1;
	/** 当前币种代码 */
	String currency;
	String currency1, currency2;

	int currencyflg = 0;// 第一币种1 第二币种 2
	private String token = null;

	String payorsearch;
	// 购汇还款账户币种
	private String currType = null;
	private String haveNotRepayAmout = null; // 本期未还款金额
	private boolean isBillExist=false; // 当月账单是否已出
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.card_messageandset_tittle));
		view = addView(R.layout.crcd_messageandset_layout);
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ActivityTaskManager.getInstance().closeAllActivityExceptOne(
//						"MainActivity");
//				Intent intent = new Intent(MyCardmessageAndSetActivity.this,
//						MyCreditCardActivity.class);
//				startActivity(intent);
				finish();
			}
		});
		init();
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		cardType = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTTYPE_RES);
		position = this.getIntent().getIntExtra(ConstantGloble.ACC_POSITION, 0);
		crcdPoint = getIntent().getStringExtra(Crcd.CRCD_CRCDPOINT);
		crcdAccount = MyCreditCardActivity.crcdAccount;
		currencyCode = (String) crcdAccount.get(Crcd.CRCD_CURRENCYCODE);
		accountNumber = (String) crcdAccount.get(Crcd.CRCD_ACCOUNTNUMBER_RES);
		accountIbkNum = (String) crcdAccount.get(Tran.ACCOUNTIBKNUM_RES);
		nickName = (String) crcdAccount.get(Crcd.CRCD_NICKNAME_RES);

		BaseHttpEngine.showProgressDialog();
		initdata(); // requestPsnCrcdQueryGeneralInfo();

	}

	private void init() {
		card__btn1 = (RadioButton) view.findViewById(R.id.card__btn1);
		card__btn2 = (RadioButton) view.findViewById(R.id.card__btn2);
		card__btn3 = (RadioButton) view.findViewById(R.id.card__btn3);
		/** 账户基本信息 */
		card_acctnum = (TextView) view.findViewById(R.id.card_acctnum);
		card_acctname = (TextView) view.findViewById(R.id.card_acctname);
		card_acctbank = (TextView) view.findViewById(R.id.card_acctbank);
		card_type = (TextView) view.findViewById(R.id.card_type);
		productName = (TextView) view.findViewById(R.id.productName);
		card_carflag = (TextView) view.findViewById(R.id.card_carflag);
		card_level = (TextView) view.findViewById(R.id.card_level);
		card_startdate = (TextView) view.findViewById(R.id.card_startdate);
		card_avaidate = (TextView) view.findViewById(R.id.card_avaidate);
		card_billdate = (TextView) view.findViewById(R.id.card_billdate);
		card_duedate = (TextView) view.findViewById(R.id.card_duedate);
		card_crcdscore = (TextView) view.findViewById(R.id.card_crcdscore);
		card_approvallimit = (TextView) view
				.findViewById(R.id.card_approvallimit);
		card_approvallimit1 = (TextView) view
				.findViewById(R.id.card_approvallimit1);
		card_carstatus = (TextView) view.findViewById(R.id.card_carstatus);
		ff_guashi= (FrameLayout) view.findViewById(R.id.ff_guashi);
		/** 取现额度 */
		card_cashLimit = (TextView) view.findViewById(R.id.card_cashLimit);
		card_cashLimit1 = (TextView) view.findViewById(R.id.card_cashLimit1);
		card_cashLimit2 = (TextView) view.findViewById(R.id.card_cashLimit2);
		card_cashbalance = (TextView) view.findViewById(R.id.card_cashbalance);
		card_annualfee = (TextView) view.findViewById(R.id.card_annualfee);
		/** 账户账面信息 */
		/** 账户余额 */
		card_realtimebalance = (TextView) view
				.findViewById(R.id.card_realtimebalance);
		/** 总可用额 */
		card_toltalBalance = (TextView) view
				.findViewById(R.id.card_toltalBalance);
		/** 本期账单金额 */
		card_billamout = (TextView) view.findViewById(R.id.card_billamout);
		/** 本期未还金款额 */
		card_havenotrepayamout = (TextView) view
				.findViewById(R.id.card_havenotrepayamout);
		/** 本期最低还款额 */
		card_billLimitamout = (TextView) view
				.findViewById(R.id.card_billLimitamout);
		/** 分期额度 */
		card_dividedpayLimit = (TextView) view
				.findViewById(R.id.card_dividedpayLimit);
		/** 分期可用额 */
		card_dividedpayavaibalance = (TextView) view
				.findViewById(R.id.card_dividedpayavaibalance);

		/** 取现可用额 */
		card_cashbalance = (TextView) view.findViewById(R.id.card_cashbalance);
		/** 申请分期 */
		card_fenqi = (Button) view.findViewById(R.id.card_fenqi);
		/** 查看账单 */
		card_zhangdan = (Button) view.findViewById(R.id.card_zhangdan);

	}

	private void initdata() {

		Map<String, Object> result = TranDataCenter.getInstance()
				.getCrcdGeneralInfo();
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
		if ("1".equals(String.valueOf(result.get(Crcd.CRCD_CARFLAG)))) {
			card_carflag.setText(getResources().getString(
					R.string.mycrcd_carFlag_zhuka));
		} else if ("0".equals(String.valueOf(result.get(Crcd.CRCD_CARFLAG)))) {
			card_carflag.setText(getResources().getString(
					R.string.mycrcd_carFlag_fushuka));
		} else {
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
		card_billdate.setText("每月"
				+ String.valueOf(result.get(Crcd.CRCD_BILLDATE)) + "日");
		/** 本期到期还款日 */
		if (StringUtil.isNullOrEmpty(result.get(Crcd.CRCD_DUEDATE_RES))) {
			card_duedate.setText("-");
		} else {
			card_duedate.setText(DateUtils.cardDateFormatter(String
					.valueOf(result.get(Crcd.CRCD_DUEDATE_RES))));
		}

		/** 积分 */
		card_crcdscore.setText(crcdPoint);
		/** 信用卡审批额度 */
		if (actlist.size() == 1) {
			String totalLimt = (String) actlist.get(0).get(
					Crcd.CRCD_TOTALLIMIT_NEW);
			String bizhong = (String) actlist.get(0)
					.get(Crcd.CRCD_CURRENCY_RES);
			card_approvallimit.setText(StringUtil.parseStringCodePattern(bizhong,totalLimt,
					2) + " " + LocalData.Currency.get(bizhong));

			String cashLimit1 = (String) actlist.get(0)
					.get(Crcd.CRCD_CASHLIMIT);
			card_cashLimit1.setText(StringUtil
					.parseStringCodePattern(bizhong,cashLimit1, 2)
					+ " "
					+ LocalData.Currency.get(bizhong));

		}
		if (actlist.size() > 1) {
			String totalLimt = (String) actlist.get(0).get(
					Crcd.CRCD_TOTALLIMIT_NEW);
			String bizhong = (String) actlist.get(0)
					.get(Crcd.CRCD_CURRENCY_RES);
			card_approvallimit.setText(StringUtil.parseStringCodePattern(bizhong,totalLimt,
					2) + "\n" + LocalData.Currency.get(bizhong));
			String cashLimit1 = (String) actlist.get(0)
					.get(Crcd.CRCD_CASHLIMIT);
			card_cashLimit1.setText(StringUtil
					.parseStringCodePattern(bizhong,cashLimit1, 2)
					+ " "
					+ LocalData.Currency.get(bizhong));

			card_approvallimit1.setVisibility(View.VISIBLE);
			String totalLimt1 = (String) actlist.get(1).get(
					Crcd.CRCD_TOTALLIMIT_NEW);
			String bizhong1 = (String) actlist.get(1).get(
					Crcd.CRCD_CURRENCY_RES);
			card_approvallimit1.setText(StringUtil.parseStringCodePattern(
					bizhong1,totalLimt1, 2) + "\n" + LocalData.Currency.get(bizhong1));
			card_cashLimit2.setVisibility(View.VISIBLE);
			String cashLimit2 = (String) actlist.get(1)
					.get(Crcd.CRCD_CASHLIMIT);
			card_cashLimit2.setText(StringUtil
					.parseStringCodePattern(bizhong1,cashLimit2, 2)
					+ " "
					+ LocalData.Currency.get(bizhong1));

		}
		/** 卡状态 */
		if (StringUtil.isNullOrEmpty(result.get(Crcd.CRCD_CARSTATUS))) {
			card_carstatus.setText("正常");
			ff_guashi.setVisibility(View.VISIBLE);
		} else {
			card_carstatus.setText(carStatusTypegMap.get(String.valueOf(result
					.get(Crcd.CRCD_CARSTATUS))));
			ff_guashi.setVisibility(View.GONE);
		}

		/** 年费减免情况 1免年费0不免年费 */
		if ("1".equals(String.valueOf(result.get(Crcd.CRCD_ANNUALFEE)))) {
			String date=String.valueOf(result.get(Crcd.CRCD_WAIVEMEMFEEENDDATE));
			if(StringUtil.isNull(date)){
				date="-";
			}else{
				date=DateUtils.cardDateFormat(date);
			}
			card_annualfee.setText(getResources().getString(
					R.string.mycrcd_annualfee_yes)+" 至 "+date);
		} else {
			card_annualfee.setText(getResources().getString(
					R.string.mycrcd_annualfee_no));
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

	/** 信用卡综合信息查询 */
	private void requestPsnCrcdQueryGeneralInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYGENERALINFO);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCrcdQueryGeneralInfoCallBack");

	}

	@SuppressWarnings("unchecked")
	public void requestPsnCrcdQueryGeneralInfoCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) {
			BiiHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		// {"acctBank":"中国银行广东省分行","acctName":"张飞","acctNum":"123456","actList":[{"billAmout":"500.123","billLimitAmout":"200.123","cashBalance":"20000.000","cashLimit":"20000.000","currency":"001","dividedPayAvaiBalance":"30000.000","dividedPayLimit":"30000.000","haveNotRepayAmout":"100.123","realTimeBalance":"10000.120","toltalBalance":"50000.000","totalLimt":"50000.000"},{"billAmout":"500.123","billLimitAmout":"200.123","cashBalance":"20000.000","cashLimit":"20000.000","currency":"014","dividedPayAvaiBalance":"30000.000","dividedPayLimit":"30000.000","haveNotRepayAmout":"100.123","realTimeBalance":"10000.120","toltalBalance":"50000.000","totalLimt":"50000.000"}],"annualFee":"1","billDate":"10号","carAvaiDate":"2030-05-01","carFlag":"1","carStatus":"abcd","dueDate":"2030-05-01","productName":"万事达普通卡","startDate":"2015-05-01"}
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
		if ("1".equals(String.valueOf(result.get(Crcd.CRCD_CARFLAG)))) {
			card_carflag.setText(getResources().getString(
					R.string.mycrcd_carFlag_zhuka));
		} else {
			card_carflag.setText(getResources().getString(
					R.string.mycrcd_carFlag_fushuka));
		}

		/** 卡等级 */
		card_level.setText("-");
		/** 启用日期 */
		card_startdate.setText(String.valueOf(result.get(Crcd.START_DATE)));
		/** 卡有效期截止日 */
		card_avaidate
				.setText(String.valueOf(result.get(Crcd.CRCD_CARAVAIDATE)));
		/** 账单日 */
		card_billdate.setText(String.valueOf(result.get(Crcd.CRCD_BILLDATE)));
		/** 本期到期还款日 */
		card_duedate.setText(String.valueOf(result.get(Crcd.CRCD_DUEDATE_RES)));
		/** 积分 */
		card_crcdscore.setText(crcdPoint);
		/** 信用卡审批额度 */
		if (actlist.size() == 1) {
			String totalLimt = (String) actlist.get(0)
					.get(Crcd.CRCD_TOTALLIMIT);
			String bizhong = (String) actlist.get(0)
					.get(Crcd.CRCD_CURRENCY_RES);
			card_approvallimit.setText(StringUtil.parseStringPattern(totalLimt,
					2) + " " + LocalData.Currency.get(bizhong));
		}
		if (actlist.size() > 1) {
			String totalLimt = (String) actlist.get(0).get(
					Crcd.CRCD_TOTALLIMIT_NEW);
			String bizhong = (String) actlist.get(0)
					.get(Crcd.CRCD_CURRENCY_RES);
			card_approvallimit.setText(StringUtil.parseStringPattern(totalLimt,
					2) + "\n" + LocalData.Currency.get(bizhong));

			card_approvallimit1.setVisibility(View.VISIBLE);
			String totalLimt1 = (String) actlist.get(1).get(
					Crcd.CRCD_TOTALLIMIT_NEW);
			String bizhong1 = (String) actlist.get(1).get(
					Crcd.CRCD_CURRENCY_RES);
			card_approvallimit1.setText(StringUtil.parseStringPattern(
					totalLimt1, 2) + "\n" + LocalData.Currency.get(bizhong1));
		}
		/** 卡状态 */
		card_carstatus.setText(carStatusTypegMap.get(String.valueOf(result
				.get(Crcd.CRCD_CARSTATUS))));
		/** 年费减免情况 1免年费0不免年费 */
		if ("1".equals(String.valueOf(result.get(Crcd.CRCD_ANNUALFEE)))) {
			card_annualfee.setText(getResources().getString(
					R.string.mycrcd_annualfee_yes));
		} else {
			card_annualfee.setText(getResources().getString(
					R.string.mycrcd_annualfee_no));
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
			card_fenqi.setVisibility(View.VISIBLE);
		} else {
			card_fenqi.setVisibility(View.GONE);
			currencyflg = 2;
		}

		/** 双币种 外币不显示查看账单 单外币显示 */
		if (actlist.size() > 1) {
			// 2币种
			if ("001".equals(currency)) {
				// 不是人民币
				card_zhangdan.setVisibility(View.VISIBLE);
			} else {
				card_zhangdan.setVisibility(View.GONE);
			}
		}

		/** 账户余额 */

		String flag = String.valueOf(map.get(Crcd.CRCD_RTBALANCEFLAG));
		flag = flagConvert(flag);
		card_realtimebalance.setText(flag
				+ StringUtil.parseStringCodePattern(currency,
						String.valueOf(map.get(Crcd.CRCD_REALTIMEBALANCE)), 2));
		/** 总可用额 */
		card_toltalBalance.setText(StringUtil.parseStringCodePattern(currency,
				String.valueOf(map.get(Crcd.CRCD_TOLTALBALANCE)), 2));
		/** 本期账单金额 */
		card_billamout.setText(StringUtil.parseStringCodePattern(currency,
				String.valueOf(map.get(Crcd.CRCD_BILLAMOUT)), 2));
		/** 本期未还金款额 */
		card_havenotrepayamout.setText(StringUtil.parseStringCodePattern(currency,
				String.valueOf(map.get(Crcd.CRCD_HAVENOTREPAYAMOUT)), 2));
		/** 本期最低还款额 */
		card_billLimitamout.setText(StringUtil.parseStringCodePattern(currency,
				String.valueOf(map.get(Crcd.CRCD_BILLLIMITAMOUT)), 2));
		/** 分期额度 */
		card_dividedpayLimit.setText(StringUtil.parseStringCodePattern(currency,
				String.valueOf(map.get(Crcd.CRCD_DIVIDEDPAYLIMIT)), 2));
		/** 分期可用额 */
		card_dividedpayavaibalance.setText(StringUtil.parseStringCodePattern(currency,
				String.valueOf(map.get(Crcd.CRCD_DIVIDEDPAYAVAIBABLANCE)), 2));
		/** 取现额度 */
		card_cashLimit.setText(StringUtil.parseStringCodePattern(currency,
				String.valueOf(map.get(Crcd.CRCD_CASHLIMIT)), 2));
		/** 取现可用额 */
		card_cashbalance.setText(StringUtil.parseStringCodePattern(currency,
				String.valueOf(map.get(Crcd.CRCD_CASHBALANCE)), 2));

	}

	/** 信用卡设置类信息查询 */
	private void requestPsnCrcdQuerySettingsInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYSETTINGSINFO);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCrcdQuerySettingsInfoCallBack");
	}

	public void requestPsnCrcdQuerySettingsInfoCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) {
			BiiHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		BiiHttpEngine.dissMissProgressDialog();

		/** 账户设置 */
		card__lin3 = (LinearLayout) view.findViewById(R.id.card__lin3);
		/** 自动还款状态 0 未设置 1 已设置 */
		card_paymentstatus = (TextView) view
				.findViewById(R.id.card_paymentstatus);
		lin_card_paymentstatus = (LinearLayout) view
				.findViewById(R.id.lin_card_paymentstatus);

		if ("1".equals(String.valueOf(result.get(Crcd.CRCD_PAYMENTSTATUS)))) {
			/** FULL全额还款MPAY部分还款 */
			String autoRepayMode = String.valueOf(result
					.get(Crcd.CRCD_PAYMENTMODE));
			if ("FULL".equals(autoRepayMode)) {
				card_paymentstatus.setText(getResources().getString(
						R.string.mycrcd_allmoney_huan));
			} else if ("MPAY".equals(autoRepayMode)) {
				card_paymentstatus.setText(getResources().getString(
						R.string.mycrcd_minmoney_huan));
			}

			paymentstatus = new String[] {
					getResources().getString(R.string.mycrcd_set_change),
					getResources().getString(R.string.mycrcd_set_close),
					// getResources().getString(R.string.mycrcd_set_open),
					getResources().getString(R.string.mycrcd_set_cancel), };

		} else {
			card_paymentstatus.setText(getResources().getString(
					R.string.mycrcd_set_no));
			paymentstatus = new String[] {
					// getResources().getString(R.string.mycrcd_set_change),
					// getResources().getString(R.string.mycrcd_set_close),
					getResources().getString(R.string.mycrcd_set_open),
					getResources().getString(R.string.mycrcd_set_cancel), };
		}
		lin_card_paymentstatus.setOnClickListener(paymentstatusListener);
		PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
				BaseDroidApp.getInstanse().getCurrentAct(),
				lin_card_paymentstatus, paymentstatus, paymentstatusListener);
		/** 外币账单自动购汇设置 0未设定1已设定 */
		rla_card_foreignpayoffstatus = (RelativeLayout) view
				.findViewById(R.id.rla_card_foreignpayoffstatus);
		card_foreignpayoffstatus = (TextView) view
				.findViewById(R.id.card_foreignpayoffstatus);
		lin_card_foreignpayoffstatus = (LinearLayout) view
				.findViewById(R.id.lin_card_foreignpayoffstatus);
		/** 单币种卡没有外币账单自动购汇设置 */
		if (actlist.size() == 1) {
			rla_card_foreignpayoffstatus.setVisibility(View.GONE);
		}

		if ("1".equals(String.valueOf(result.get(Crcd.CRCD_FOREIGNPAYOFFSTATUS)))) {

			card_foreignpayoffstatus.setText(getResources().getString(
					R.string.mycrcd_set_yes));
			foreignpayoffstatu = new String[] {
					getResources().getString(R.string.mycrcd_set_close),
					// getResources().getString(R.string.mycrcd_set_open),
					getResources().getString(R.string.mycrcd_set_cancel), };
		} else {
			card_foreignpayoffstatus.setText(getResources().getString(
					R.string.mycrcd_set_no));
			foreignpayoffstatu = new String[] {
					// getResources().getString(R.string.mycrcd_set_close),
					getResources().getString(R.string.mycrcd_set_open),
					getResources().getString(R.string.mycrcd_set_cancel), };
		}

		lin_card_foreignpayoffstatus
				.setOnClickListener(foreignpayoffstatusListener);

		PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
				BaseDroidApp.getInstanse().getCurrentAct(),
				lin_card_foreignpayoffstatus, foreignpayoffstatu,
				foreignpayoffstatusListener);
		card_foreignpayoffstatus_txt = (TextView) view
				.findViewById(R.id.card_foreignpayoffstatus_txt);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				MyCardmessageAndSetActivity.this, card_foreignpayoffstatus_txt);

		/** 查询密码设置 */
		card_searchpassword = (TextView) view
				.findViewById(R.id.card_searchpassword);
		lin_card_searchpassword = (LinearLayout) view
				.findViewById(R.id.lin_card_searchpassword);
		lin_card_searchpassword
				.setOnClickListener(searchpasswordstatussetClick);
		// lin_card_searchpassword.setOnClickListener(searchpasswordListener);
		// String[] searchstatus = {
		// getResources().getString(R.string.mycrcd_set_setting),
		// getResources().getString(R.string.mycrcd_set_change),
		// getResources().getString(R.string.mycrcd_set_cancel),
		// };
		// PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
		// BaseDroidApp.getInstanse().getCurrentAct(),
		// lin_card_searchpassword, searchstatus, searchpasswordListener);

		/** 消费密码设置 */
		card_passwordstatus = (TextView) view
				.findViewById(R.id.card_passwordstatus);
		lin_card_passwordstatus = (LinearLayout) view
				.findViewById(R.id.lin_card_passwordstatus);
		lin_card_passwordstatus.setOnClickListener(passwordstatussetClick);
		// lin_card_passwordstatus.setOnClickListener(passwordstatusListener);
		// String[] passwordstatus = {
		// getResources().getString(R.string.mycrcd_set_setting),
		// getResources().getString(R.string.mycrcd_set_change),
		// getResources().getString(R.string.mycrcd_set_cancel),
		// };
		// PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
		// BaseDroidApp.getInstanse().getCurrentAct(),
		// lin_card_passwordstatus, passwordstatus, passwordstatusListener);

		lin_card_verifymode = (LinearLayout) view
				.findViewById(R.id.lin_card_verifymode);
		lin_card_verifymode.setOnClickListener(verifymodeListener);

		/** POS消费验证方式 0签名1签名+密码 */
		/** POS验证方式为密码＋签名时才显示 POS消费验密触发金额 */
		rla_card_postriggeramount = (LinearLayout) view
				.findViewById(R.id.rla_card_postriggeramount);
		/** POS消费验证方式 */
		card_verifymode = (TextView) view.findViewById(R.id.card_verifymode);
		verifymodeflg = String.valueOf(result.get(Crcd.CRCD_VERIFYMODE));
		if ("1".equals(String.valueOf(result.get(Crcd.CRCD_VERIFYMODE)))) {
			rla_card_postriggeramount.setVisibility(View.VISIBLE);

			card_verifymode.setText(getResources().getString(
					R.string.mycrcd_service_passandqianming));
		} else {
			rla_card_postriggeramount.setVisibility(View.GONE);
			card_verifymode.setText(getResources().getString(
					R.string.mycrcd_service_qianming));
		}
		/** POS消费验密触发金额 */
		card_postriggeramount_txt = (TextView) view
				.findViewById(R.id.card_postriggeramount_txt);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				MyCardmessageAndSetActivity.this, card_postriggeramount_txt);
		card_postriggeramount = (TextView) view
				.findViewById(R.id.card_postriggeramount);
		postriggeramount = String.valueOf(result
				.get(Crcd.CRCD_POSTRIGGERAMOUNT));
		postriggeramount = StringUtil.parseStringCodePattern(currencyCode,postriggeramount, 2);
		card_postriggeramount.setText(postriggeramount);

		/** 短信提示触发金额 */
		card_smstriggeramount = (TextView) view
				.findViewById(R.id.card_smstriggeramount);
		smstriggeramount = String.valueOf(result
				.get(Crcd.CRCD_SMSTRIGGERAMOUNT));
		smstriggeramount = StringUtil.parseStringCodePattern(currencyCode,smstriggeramount, 2);
		card_smstriggeramount.setText(smstriggeramount);

		String[] verifymode = {
				getResources().getString(R.string.mycrcd_set_change),
				getResources().getString(R.string.mycrcd_set_cancel), };
		PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
				BaseDroidApp.getInstanse().getCurrentAct(),
				lin_card_verifymode, verifymode, verifymodeListener);

		// lin_card_postriggeramount = (LinearLayout) view
		// .findViewById(R.id.lin_card_postriggeramount);
		// lin_card_postriggeramount.setOnClickListener(postriggeramountListener);
		// String[] postriggeramount = {
		// getResources().getString(R.string.mycrcd_set_change),
		// getResources().getString(R.string.mycrcd_set_cancel),
		// };
		// PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
		// BaseDroidApp.getInstanse().getCurrentAct(),
		// lin_card_postriggeramount, postriggeramount,
		// postriggeramountListener);

		// lin_card_smstriggeramount = (LinearLayout) view
		// .findViewById(R.id.lin_card_smstriggeramount);
		// lin_card_smstriggeramount.setOnClickListener(smstriggeramountListener);
		// String[] smstriggeramount = {
		// getResources().getString(R.string.mycrcd_set_change),
		// getResources().getString(R.string.mycrcd_set_cancel),
		// };
		// PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
		// BaseDroidApp.getInstanse().getCurrentAct(),
		// lin_card_smstriggeramount, smstriggeramount,
		// smstriggeramountListener);

		/** 纸质对账单 1:开通 0:未开通 */
		lin_card_paperstatment = (LinearLayout) view
				.findViewById(R.id.lin_card_paperstatment);
		card_paperstatment = (TextView) view
				.findViewById(R.id.card_paperstatment);

		if ("1".equals(String.valueOf(result.get(Crcd.CRCD_PAPERSTATMENTSTATUS)))) {

			paperstatment = new String[] {
					// getResources().getString(R.string.mycrcd_set_open),
					getResources().getString(R.string.mycrcd_set_change),
					// getResources().getString(R.string.mycrcd_set_restore),
					getResources().getString(R.string.mycrcd_set_close),
					getResources().getString(R.string.mycrcd_set_cancel), };
			paperAddress = String.valueOf(result.get(Crcd.CRCD_PAPERSTATMENT));

			card_paperstatment.setText(paperAddress);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(
					MyCardmessageAndSetActivity.this, card_paperstatment);

		} else {
			paperstatment = new String[] {
					getResources().getString(R.string.mycrcd_set_open),
					// getResources().getString(R.string.mycrcd_set_change),
					// getResources().getString(R.string.mycrcd_set_restore),
					// getResources().getString(R.string.mycrcd_set_close),
					getResources().getString(R.string.mycrcd_set_cancel), };
			card_paperstatment.setText(getResources().getString(
					R.string.mycrcd_set_weikaitong));
		}

		lin_card_paperstatment.setOnClickListener(paperstatmentListener);
		PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
				BaseDroidApp.getInstanse().getCurrentAct(),
				lin_card_paperstatment, paperstatment, paperstatmentListener);

		/** 电子邮件对账单 1:开通 0:未开通 */

		
		lin_card_emailstatment = (LinearLayout) view
				.findViewById(R.id.lin_card_emailstatment);
		card_emailstatment = (TextView) view
				.findViewById(R.id.card_emailstatment);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				MyCardmessageAndSetActivity.this, card_emailstatment);
		if ("1".equals(String.valueOf(result.get(Crcd.CRCD_EMAILSTATMENTSTATUS)))) {
			email = String.valueOf(result.get(Crcd.CRCD_EMAILSTATMENT));
			card_emailstatment.setText(email);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(
					MyCardmessageAndSetActivity.this, card_emailstatment);
			emailstatment = new String[] {
					// getResources().getString(R.string.mycrcd_set_open),
					getResources().getString(R.string.mycrcd_set_change),
					getResources().getString(R.string.mycrcd_set_restore),
					getResources().getString(R.string.mycrcd_set_close),
					getResources().getString(R.string.mycrcd_set_cancel), };

		} else {
			emailstatment = new String[] {
					getResources().getString(R.string.mycrcd_set_open),
					// getResources().getString(R.string.mycrcd_set_change),
					// getResources().getString(R.string.mycrcd_set_restore),
					// getResources().getString(R.string.mycrcd_set_close),
					getResources().getString(R.string.mycrcd_set_cancel), };
			card_emailstatment.setText(getResources().getString(
					R.string.mycrcd_set_weikaitong));
		}
		lin_card_emailstatment.setOnClickListener(emailstatmentListener);

		PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
				BaseDroidApp.getInstanse().getCurrentAct(),
				lin_card_emailstatment, emailstatment, emailstatmentListener);
		/** 手机对账单 */

		card_phonestatment = (TextView) view
				.findViewById(R.id.card_phonestatment);
		lin_card_phonestatment = (LinearLayout) view
				.findViewById(R.id.lin_card_phonestatment);
		if ("1".equals(String.valueOf(result.get(Crcd.CRCD_PHONESTATMENTSTATUS)))) {
			mobile = String.valueOf(result.get(Crcd.CRCD_PHONESTATMENT));
			card_phonestatment.setText(mobile);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(
					MyCardmessageAndSetActivity.this, card_phonestatment);
			phonestatment = new String[] {
					// getResources().getString(R.string.mycrcd_set_open),
					getResources().getString(R.string.mycrcd_set_change),
					getResources().getString(R.string.mycrcd_set_restore),
					getResources().getString(R.string.mycrcd_set_close),
					getResources().getString(R.string.mycrcd_set_cancel), };

		} else {
			card_phonestatment.setText(getResources().getString(
					R.string.mycrcd_set_weikaitong));
			phonestatment = new String[] {
					getResources().getString(R.string.mycrcd_set_open),
					// getResources().getString(R.string.mycrcd_set_change),
					// getResources().getString(R.string.mycrcd_set_restore),
					// getResources().getString(R.string.mycrcd_set_close),
					getResources().getString(R.string.mycrcd_set_cancel), };
		}

		lin_card_phonestatment.setOnClickListener(phonestatmentListener);

		PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
				BaseDroidApp.getInstanse().getCurrentAct(),
				lin_card_phonestatment, phonestatment, phonestatmentListener);

		/** 全球交易人民币记账标识 */
		rla_card_fchargeflag = (RelativeLayout) view
				.findViewById(R.id.rla_card_fchargeflag);
		card_fchargeflag = (TextView) view.findViewById(R.id.card_fchargeflag);
		lin_card_fchargeflag = (LinearLayout) view
				.findViewById(R.id.lin_card_fchargeflag);

		if (actlist.size() == 1) {
			rla_card_fchargeflag.setVisibility(View.GONE);
		}

		/** 全球交易人民币记账标识 */
		if ("ADTE".equals(String.valueOf(result.get(Crcd.CRCD_FCHARGEFLAG)))) {
			openFlag = ConstantGloble.CRCD_TRUE_KEY;
			card_fchargeflag.setText(getResources().getString(
					R.string.mycrcd_set_yikaitong));
		} else {
			openFlag = ConstantGloble.CRCD_FALSE_KEY;
			card_fchargeflag.setText(getResources().getString(
					R.string.mycrcd_set_weikaitong));
		}

		lin_card_fchargeflag.setOnClickListener(fchargeflagListener);

		if (openFlag.equals(ConstantGloble.CRCD_TRUE_KEY)) {
			fchargeflag = new String[] {
					getResources().getString(R.string.mycrcd_set_close),
					getResources().getString(R.string.mycrcd_set_cancel), };
		} else if (openFlag.equals(ConstantGloble.CRCD_FALSE_KEY)) {
			fchargeflag = new String[] {
					getResources().getString(R.string.mycrcd_set_open),
					getResources().getString(R.string.mycrcd_set_cancel), };
		}
		/** 全球交易人民币记账标识 */
		card_fchargeflag_txt = (TextView) view
				.findViewById(R.id.card_fchargeflag_txt);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				MyCardmessageAndSetActivity.this, card_fchargeflag_txt);
		PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
				BaseDroidApp.getInstanse().getCurrentAct(),
				lin_card_fchargeflag, fchargeflag, fchargeflagListener);

		findViewById(R.id.card__lin1).setVisibility(View.GONE);
		findViewById(R.id.card__lin2).setVisibility(View.GONE);
		findViewById(R.id.card__lin3).setVisibility(View.VISIBLE);
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

	/** 账户设置 */
	public void card__btn3Onclick(View v) {

		BaseHttpEngine.showProgressDialog();

		tag = 2;
		requestCommConversationId();

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

	/** 挂失/补卡 */
	public void card__guashiOnclick(View v) {
		String nick = (String) crcdAccount.get(Crcd.CRCD_NICKNAME_RES);
		String type = (String) crcdAccount.get(Crcd.CRCD_ACCOUNTTYPE_RES);
		String accountNumber = (String) crcdAccount
				.get(Crcd.CRCD_ACCOUNTNUMBER_RES);
		String strAccountType = LocalData.AccountType.get(type);
		Intent intent = new Intent(MyCardmessageAndSetActivity.this,
				CrcdGuashiInfoActivity.class);
		intent.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		intent.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		intent.putExtra(Crcd.CRCD_NICKNAME_RES, nick);
		intent.putExtra(Crcd.CRCD_ACCOUNTTYPE_RES, strAccountType);
		startActivityForResult(intent,
				ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE);
	}

	/** 临时额度 */
	public void card_TmpLimitOnclick(View v) {

		Intent intent = new Intent(this, CardApplyTmpLimitPreActivity.class);
		String accountNumber = (String) crcdAccount
				.get(Crcd.CRCD_ACCOUNTNUMBER_RES);
		intent.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		intent.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);

		if (StringUtil.isNull(currency2)) {
			intent.putExtra(ConstantGloble.CRCD_CURRENCY2, currency1);
		} else {
			intent.putExtra(ConstantGloble.CRCD_CURRENCY2, currency2);
		}
		intent.putExtra(ConstantGloble.TRANS_ACCOUNT_TYPE, Crcd.TRANS_CREDIT);
		startActivity(intent);

	}

	/** 查看账单 */
	@SuppressLint("ResourceAsColor")
	public void card__searchaccountOnclick(View v) {

		Intent it = new Intent(MyCardmessageAndSetActivity.this,
				MyCrcdDetailActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(ConstantGloble.ACC_POSITION, position);
		startActivity(it);

	}

	/** 申请分期 */
	@SuppressLint("ResourceAsColor")
	public void card_fenqiOnclick(View v) {
		CrcdHasQueryListActivity.accountId=String.valueOf(crcdAccount
				.get(Crcd.CRCD_ACCOUNTID_RES));	
		CrcdHasQueryListActivity.accountNumber = String.valueOf(crcdAccount
				.get(Crcd.CRCD_ACCOUNTNUMBER_RES));
		Intent it = new Intent(MyCardmessageAndSetActivity.this,
				CrcdHasQueryDetailActivity.class);
		startActivity(it);
	}

	/** 快速还款 */
	public void card_kuaisuOnclick(View v) {
		// 统一处理
		Map<String, Object> result = TranDataCenter.getInstance()
				.getCrcdGeneralInfo();
		List<Map<String, Object>> actlist = (List<Map<String, Object>>) result
				.get(Crcd.CRCD_ACTLIST);
		result.put(Crcd.CRCD_ACCOUNTTYPE_RES, cardType);
		result.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		result.put(Tran.ACCOUNTIBKNUM_RES, accountIbkNum);
		result.put(Crcd.CRCD_NICKNAME_RES, nickName);
		TranDataCenter.getInstance().setAccInInfoMap(result);
		TranDataCenter.getInstance().setRelCrcdBuyCallBackMap(null);
		for (int i = 0; i < actlist.size(); i++) {

			if (!"001".equals(String.valueOf(actlist.get(i).get(
					Crcd.CRCD_CURRENCY_RES)))) {
				// 得到外币 账单信息
				haveNotRepayAmout = (String) actlist.get(i).get(
						Crcd.CRCD_HAVENOTREPAYAMOUT); // 本期未还款金额
				currType = (String) actlist.get(i).get(Crcd.CRCD_CURRENCY); // 本期未还款金额
			}
		}
		if (!StringUtil.isNull(haveNotRepayAmout)
				&& Double.parseDouble(haveNotRepayAmout) > 0) {

			// 外币有欠款 请求购汇信息
			BaseHttpEngine.showProgressDialog();
			
			tag = 4;
			requestCommConversationId();
		} else {
			// 无欠款 直接到转账界面
			Intent intent = new Intent(MyCardmessageAndSetActivity.this,
					TransferManagerActivity1.class);
			 TranDataCenter.getInstance().setModuleType(ConstantGloble.CRCE_TYPE);
			intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,
					ConstantGloble.CREDIT_TO_TRAN);
			startActivity(intent);
		}

		// if (actlist.size() == 1) {
		//
		// String currency1 = String.valueOf(actlist.get(0)
		// .get(Crcd.CRCD_CURRENCY_RES));
		//
		// if(ConstantGloble.FOREX_RMB_TAG1.equals(currency1) ||
		// ConstantGloble.FOREX_RMB_CNA_TAG2.equals(currency1)){
		// // 单人民币
		//
		// TranDataCenter.getInstance().setRelCrcdBuyCallBackMap(null);
		// Intent it3 = new Intent(MyCardmessageAndSetActivity.this,
		// TransferManagerActivity1.class);
		//
		// it3.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,
		// ConstantGloble.CREDIT_TO_TRAN);
		//
		// startActivity(it3);
		//
		//
		// }else{
		//
		// currType=currency1;
		// // 单外币
		// tag = 4;
		// BaseHttpEngine.showProgressDialog();
		// requestCommConversationId();
		// }
		//
		// }
		// if (actlist.size() > 1) {
		// String currency1 = String.valueOf(actlist.get(0).get(
		// Crcd.CRCD_CURRENCY_RES));
		// String currency2 = String.valueOf(actlist.get(1).get(
		// Crcd.CRCD_CURRENCY_RES));
		//
		// if(ConstantGloble.FOREX_RMB_TAG1.equals(currency1) ||
		// ConstantGloble.FOREX_RMB_CNA_TAG2.equals(currency1)){
		//
		// }else{
		// currType=currency1;
		// }
		// if(ConstantGloble.FOREX_RMB_TAG1.equals(currency2) ||
		// ConstantGloble.FOREX_RMB_CNA_TAG2.equals(currency2)){
		//
		// }else{
		// currType=currency2;
		// }
		//
		// if(currType!=null){
		// tag = 4;
		// BaseHttpEngine.showProgressDialog();
		// requestCommConversationId();
		// }
		// }
	}

	/**
	 * 信用卡查询购汇还款信息返回 sunh
	 * 
	 * @param resultObj
	 */
	public void requesPsnCrcdQueryForeignPayOffCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setRelCrcdBuyCallBackMap(result);
		Intent intent = new Intent(MyCardmessageAndSetActivity.this,
				TransferManagerActivity1.class);
		intent.putExtra(ConstantGloble.TRANS_ACCOUNT_TYPE, Crcd.TRANS_CREDIT);
		 TranDataCenter.getInstance().setModuleType(ConstantGloble.CRCE_TYPE);
		intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,
				ConstantGloble.CREDIT_TO_TRAN);
		intent.putExtra(ConstantGloble.CRCD_CURRENCY2, currType);
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(intent);
	}

	// if (actlist.size() == 1) {
	//
	// String currency1 = String.valueOf(actlist.get(0)
	// .get(Crcd.CRCD_CURRENCY_RES));
	//
	// if(ConstantGloble.FOREX_RMB_TAG1.equals(currency1) ||
	// ConstantGloble.FOREX_RMB_CNA_TAG2.equals(currency1)){
	// // 单人民币
	//
	// TranDataCenter.getInstance().setRelCrcdBuyCallBackMap(null);
	// Intent it3 = new Intent(MyCardmessageAndSetActivity.this,
	// TransferManagerActivity1.class);
	// it3.putExtra(ConstantGloble.TRANS_ACCOUNT_TYPE,
	// Crcd.TRANS_CREDIT);
	// Crcd.tranState = Crcd.TRANS_IN;
	// TranDataCenter.getInstance().setModuleType(
	// ConstantGloble.CRCE_TYPE);
	// it3.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,
	// ConstantGloble.CREDIT_TO_TRAN);
	//
	// startActivity(it3);
	//
	//
	// }else{
	//
	// currType=currency1;
	// // 单外币
	// requestCommConversationId();
	// }
	//
	// }
	// if (actlist.size() > 1) {
	// String currency1 = String.valueOf(actlist.get(0).get(
	// Crcd.CRCD_CURRENCY_RES));
	// String currency2 = String.valueOf(actlist.get(1).get(
	// Crcd.CRCD_CURRENCY_RES));
	//
	// if(ConstantGloble.FOREX_RMB_TAG1.equals(currency1) ||
	// ConstantGloble.FOREX_RMB_CNA_TAG2.equals(currency1)){
	//
	// }else{
	// currType=currency1;
	// }
	// if(ConstantGloble.FOREX_RMB_TAG1.equals(currency2) ||
	// ConstantGloble.FOREX_RMB_CNA_TAG2.equals(currency2)){
	//
	// }else{
	// currType=currency2;
	// }
	//
	// if(currType!=null){
	// tag = 4;
	// requestCommConversationId();
	// }
	// }

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (tag == 1) {
			commConversationId = (String) BaseDroidApp.getInstanse()
					.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
			// 获取tockenId
			requestPSNGetTokenId(commConversationId);
		} else if (tag == 2) {
			requestPsnCrcdQuerySettingsInfo();

		} else if (tag == 3) {
			requestGetSecurityFactor(psnGouhuisecurityId);
		} else if (tag == 4) {

			requesPsnCrcdQueryForeignPayOff();
			// // 查询信用卡购汇还款信息

		}

	}

	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);

		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 信用卡信用卡外币账单自动购汇设置预交易
						requestPsnCrcdSetAutoForeignPayOffPre();

					}
				});
	}

	protected void requestPsnCrcdSetAutoForeignPayOffPre() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDSETAUTOFOREGINPAYOFFPRE);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);// 获取conversationId
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);// 账户ID
		// map.put(Crcd.CRCD_TOACTIVEACT, accountNumber);//卡号
		map.put(Crcd.CRCD_GOUHUIOPENFLAG_RES, autoopenflag);// 开通标识
		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse()
				.getSecurityChoosed());// 安全因子组合id
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"PsnCrcdSetAutoForeignPayOffPreCallBack");

	}

	Map<String, Object> returnList;

	public void PsnCrcdSetAutoForeignPayOffPreCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		returnList = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList)) {
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(Crcd.CRCD_PSNCRCDSETAUTOFOREGINPAYOFFPRE, returnList);
		Intent it = new Intent(MyCardmessageAndSetActivity.this,
				CrcdSetAutoForeignPayOffConfirmActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_GOUHUIOPENFLAG_RES, autoopenflag);
		startActivityForResult(it,
				ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}

	/**
	 * 信用卡查询购汇还款信息 sunh
	 */
	public void requesPsnCrcdQueryForeignPayOff() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNQUERYDOREIGNPAYOFF);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);// 转入账户标识 accountId
		map.put(Crcd.CRCD_CURRTYPE_REQ, currType);// 购汇还款账户币种 currType
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requesPsnCrcdQueryForeignPayOffCallBack");
	}

	/**
	 * 信用卡查询购汇还款信息
	 */
	public void requestForCrcdForeignPayOff() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.CRCDFOREIGNPAYOFF);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.CREDITCARD_CRCDID_REQ, accountId);// 转入账户信息 crcdId
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCrcdForeignPayOffCallBack");
	}

	/**
	 * 信用卡查询购汇还款信息返回
	 * 
	 * @param resultObj
	 */
	public void requestForCrcdForeignPayOffCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setRelCrcdBuyCallBackMap(result);

		Intent intent = new Intent(this, TransferManagerActivity1.class);
		Crcd.tranState = Crcd.TRANS_GOHUAN;
		TranDataCenter.getInstance().setModuleType(ConstantGloble.CRCE_TYPE);
		intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,
				ConstantGloble.REL_CRCD_BUY);
		if (StringUtil.isNull(currency2)) {
			intent.putExtra(ConstantGloble.CRCD_CURRENCY2, currency1);
		} else {
			intent.putExtra(ConstantGloble.CRCD_CURRENCY2, currency2);
		}
		intent.putExtra(ConstantGloble.TRANS_ACCOUNT_TYPE, Crcd.TRANS_CREDIT);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		case RESULT_OK:// 成功

			if (card__btn3.isChecked()) {
				BaseHttpEngine.showProgressDialog();

				tag = 2;
				requestCommConversationId();
			}else{
				finish();
			}
			break;
		default:
			break;

		case RESULT_CANCELED:// 失败
			break;
		}
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.lin_card_paymentstatus) {
			/** 自动还款状态 */
			// String[] service = { "修改","关闭","开通","取消" };
			// PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
			// BaseDroidApp.getInstanse().getCurrentAct(),
			// lin_card_paymentstatus, service, paymentstatusListener);

		}
		if (v.getId() == R.id.lin_card_foreignpayoffstatus) {
			/** 外币账单自动购汇设置 */
			// String[] service = { "开通","关闭","修改","取消" };
			// PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
			// BaseDroidApp.getInstanse().getCurrentAct(),
			// lin_card_foreignpayoffstatus, service,
			// foreignpayoffstatusListener);
		}

		if (v.getId() == R.id.lin_card_passwordstatus) {
			/** 消费密码设置 */
			// String[] service = { "设置","修改","取消" };
			// PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
			// BaseDroidApp.getInstanse().getCurrentAct(),
			// lin_card_passwordstatus, service, passwordstatusListener);
		}

		if (v.getId() == R.id.lin_card_verifymode) {
			/** POS消费验证方式 */
			// String[] service = { "修改","取消" };
			// PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
			// BaseDroidApp.getInstanse().getCurrentAct(),
			// lin_card_verifymode, service, verifymodeListener);

		}
		// if (v.getId() == R.id.lin_card_postriggeramount) {
		// /** POS消费验密触发金额 */
		// // String[] service = { "修改","取消" };
		// // PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
		// // BaseDroidApp.getInstanse().getCurrentAct(),
		// // lin_card_postriggeramount, service, verifymodeListener);
		// }

		// if (v.getId() == R.id.lin_card_smstriggeramount) {
		// /** 短信提示触发金额 */
		// // String[] service = { "修改","取消" };
		// // PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
		// // BaseDroidApp.getInstanse().getCurrentAct(),
		// // lin_card_smstriggeramount, service, verifymodeListener);
		// }

		if (v.getId() == R.id.lin_card_paperstatment) {
			/** 纸质对账单 */
			String[] service = { "开通", "修改", "重置", "关闭", "取消" };
			// PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
			// BaseDroidApp.getInstanse().getCurrentAct(),
			// lin_card_paperstatment, service, statmentListener);

		}
		if (v.getId() == R.id.lin_card_emailstatment) {
			/** 电子邮件对账单 */
			String[] service = { "开通", "修改", "重置", "关闭", "取消" };
			// PopupWindowUtils.getInstance().setshowMoreChooseUpListener(
			// BaseDroidApp.getInstanse().getCurrentAct(),
			// lin_card_emailstatment, service, statmentListener);

		}
		if (v.getId() == R.id.lin_card_phonestatment) {
			/** 手机对账单 */
			// String[] service = {"开通", "修改","重置","关闭","取消" };
			// PopupWindowUtils.getInstance().setshowMoreChooseUpListener(
			// BaseDroidApp.getInstanse().getCurrentAct(),
			// lin_card_phonestatment, service, statmentListener);
		}
		if (v.getId() == R.id.lin_card_fchargeflag) {
			/** 全球交易人民币记账标识 */

		}

	}

	/** 全球交易人民币记账标识 */

	final OnClickListener fchargeflagListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button tv = (Button) v;
			String text = tv.getText().toString();
			fchargeflagClick(text, v);

		}
	};
	/** 全球交易人民币记账标识 开通 关闭 */
	final OnClickListener fchargeflagopenorcloseClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			tag = 1;
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	};
	/** 全球交易人民币记账标识 取消 */
	final OnClickListener fchargeflagcancelClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};

	protected void fchargeflagClick(String text, View v) {

		if (text.equals(getResources().getString(R.string.mycrcd_set_close))) {

			fchargeflagopenorcloseClick.onClick(v);
		}
		if (text.equals(getResources().getString(R.string.mycrcd_set_open))) {

			fchargeflagopenorcloseClick.onClick(v);
		}
		if (text.equals(getResources().getString(R.string.mycrcd_set_cancel))) {

			fchargeflagcancelClick.onClick(v);
		}

	}

	/** 自动还款状态 */
	final OnClickListener paymentstatusListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button tv = (Button) v;
			String text = tv.getText().toString();
			paymentstatusClick(text, v);

		}
	};
	/** 自动还款 修改 */
	final OnClickListener paymentstatuschangeClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent it = new Intent(MyCardmessageAndSetActivity.this,
					MyCrcdSetupReadActivity.class);
			it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
			it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
			it.putExtra(Crcd.CRCD_REPAYTYPE, 1);
			// startActivity(it);
			startActivityForResult(it,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);

		}
	};
	/** 自动还款 关闭 设定为主动还款 */
	final OnClickListener paymentstatuscloseClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Intent it = new Intent(MyCardmessageAndSetActivity.this,
					CrcdpaymentSetupAutoActivity.class);
			it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
			it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
			// it.putExtra(Crcd.CRCD_REPAYTYPE, 0);
			/** 0 = 主动还款,1 = 自动还款 */
			// startActivity(it);
			startActivityForResult(it,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		}
	};
	/** 自动还款 开通 */
	final OnClickListener paymentstatusopenClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent it = new Intent(MyCardmessageAndSetActivity.this,
					MyCrcdSetupReadActivity.class);
			it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
			it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
			it.putExtra(Crcd.CRCD_REPAYTYPE, 1);
			// startActivity(it);
			startActivityForResult(it,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);

		}
	};
	/** 自动还款 取消 */
	final OnClickListener paymentstatuscancelClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};

	protected void paymentstatusClick(String text, View v) {
		if (text.equals(getResources().getString(R.string.mycrcd_set_change))) {

			paymentstatuschangeClick.onClick(v);
		}
		if (text.equals(getResources().getString(R.string.mycrcd_set_close))) {

			paymentstatuscloseClick.onClick(v);
		}
		if (text.equals(getResources().getString(R.string.mycrcd_set_open))) {

			paymentstatusopenClick.onClick(v);
		}
		if (text.equals(getResources().getString(R.string.mycrcd_set_cancel))) {

			paymentstatuscancelClick.onClick(v);
		}

	}

	/** 查询密码设置 */

	final OnClickListener searchpasswordListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button tv = (Button) v;
			String text = tv.getText().toString();
			payorsearch = "search";
			passwordstatusClick(text, v);

		}
	};
	/** 消费密码设置 */

	final OnClickListener passwordstatusListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button tv = (Button) v;
			String text = tv.getText().toString();
			payorsearch = "pay";
			passwordstatusClick(text, v);

		}
	};

	/** 消费密码 设置 */
	final OnClickListener searchpasswordstatussetClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Intent it = new Intent(MyCardmessageAndSetActivity.this,
					CrcdSetConsumePWPreActivity.class);
			it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
			it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
			it.putExtra("payorsearch", "search");
			// startActivity(it);
			startActivityForResult(it,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);

		}
	};
	/** 消费密码 设置 */
	final OnClickListener passwordstatussetClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Intent it = new Intent(MyCardmessageAndSetActivity.this,
					CrcdSetConsumePWPreActivity.class);
			it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
			it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
			it.putExtra("payorsearch", "pay");
			// startActivity(it);
			startActivityForResult(it,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);

		}
	};
	/** 消费密码 修改 */
	final OnClickListener passwordstatuschangeClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Intent it = new Intent(MyCardmessageAndSetActivity.this,
					CrcdSetConsumePWPreActivity.class);
			it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
			it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
			it.putExtra("payorsearch", payorsearch);
			// startActivity(it);
			startActivityForResult(it,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		}
	};
	/** 消费密码 取消 */
	final OnClickListener passwordstatuscancelClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};

	protected void passwordstatusClick(String text, View v) {

		if (text.equals(getResources().getString(R.string.mycrcd_set_setting))) {

			passwordstatussetClick.onClick(v);
		}
		if (text.equals(getResources().getString(R.string.mycrcd_set_change))) {

			passwordstatuschangeClick.onClick(v);
		}

		if (text.equals(getResources().getString(R.string.mycrcd_set_cancel))) {

			passwordstatuscancelClick.onClick(v);
		}

	}

	/** 外币账单自动购汇设置 */
	final OnClickListener foreignpayoffstatusListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button tv = (Button) v;
			String text = tv.getText().toString();
			foreignpayoffstatusClick(text, v);

		}
	};

	// /** 外币账单自动购汇 修改 */
	// final OnClickListener foreignpayoffstatuschangeClick = new
	// OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	//
	// }
	// };
	/** 外币账单自动购汇关闭 */
	final OnClickListener foreignpayoffstatuscloseClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			tag = 3;
			BaseHttpEngine.showProgressDialog();
			// 请求conversationId
			requestCommConversationId();

			autoopenflag = "0000";

			// Intent it = new Intent(MyCardmessageAndSetActivity.this,
			// CrcdSetAutoForeignPayOffPreActivity.class);
			// it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
			// it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
			// it.putExtra(Crcd.CRCD_GOUHUIOPENFLAG_RES, "0000");
			// // startActivity(it);
			// startActivityForResult(it,
			// ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);

		}
	};
	/** 外币账单自动购汇 开通 */
	final OnClickListener foreignpayoffstatusopenClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			tag = 3;
			BaseHttpEngine.showProgressDialog();
			// 请求conversationId
			requestCommConversationId();

			autoopenflag = "1000";

			// Intent it = new Intent(MyCardmessageAndSetActivity.this,
			// CrcdSetAutoForeignPayOffPreActivity.class);
			// it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
			// it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
			// it.putExtra(Crcd.CRCD_GOUHUIOPENFLAG_RES, "1000");
			// // startActivity(it);
			// startActivityForResult(it,
			// ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);

		}
	};
	/** 外币账单自动购汇 取消 */
	final OnClickListener foreignpayoffstatuscancelClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};

	protected void foreignpayoffstatusClick(String text, View v) {
		// if
		// (text.equals(getResources().getString(R.string.mycrcd_set_change))) {
		//
		// foreignpayoffstatuschangeClick.onClick(v);
		// }
		if (text.equals(getResources().getString(R.string.mycrcd_set_close))) {

			foreignpayoffstatuscloseClick.onClick(v);
		}
		if (text.equals(getResources().getString(R.string.mycrcd_set_open))) {

			foreignpayoffstatusopenClick.onClick(v);
		}
		if (text.equals(getResources().getString(R.string.mycrcd_set_cancel))) {

			foreignpayoffstatuscancelClick.onClick(v);
		}

	}

	/** pos消费验证 */
	final OnClickListener verifymodeListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Button tv = (Button) v;
			String text = tv.getText().toString();
			verifymodeClick(text, v);

		}
	};
	/** pos消费触发金额 */
	final OnClickListener postriggeramountListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Button tv = (Button) v;
			String text = tv.getText().toString();
			verifymodeClick(text, v);

		}
	};
	/** 短信提示触发金额 */
	final OnClickListener smstriggeramountListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Button tv = (Button) v;
			String text = tv.getText().toString();
			verifymodeClick(text, v);

		}
	};
	/** pos 修改 */
	final OnClickListener verifymodechangeClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent it = new Intent(MyCardmessageAndSetActivity.this,
					CrcdServiceInfoActivity.class);
			it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
			it.putExtra(Crcd.CRCD_CURRENCYCODE, currencyCode);
			it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
			it.putExtra(Crcd.CRCD_POSLIMITAMOUNT, postriggeramount);
			it.putExtra(Crcd.CRCD_SHORTMSGLIMITAMOUNT, smstriggeramount);
			it.putExtra(Crcd.CRCD_POSFLAG, verifymodeflg);
			// startActivity(it);
			startActivityForResult(it,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);

		}
	};
	/** pos 取消 */
	final OnClickListener verifymodecancelClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};

	protected void verifymodeClick(String text, View v) {
		if (text.equals(getResources().getString(R.string.mycrcd_set_change))) {

			verifymodechangeClick.onClick(v);
		}
		if (text.equals(getResources().getString(R.string.mycrcd_set_cancel))) {

			verifymodecancelClick.onClick(v);
		}

	}

	/** 纸质对账单 */
	final OnClickListener paperstatmentListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			billSetupId = 0;
			Button tv = (Button) v;
			String text = tv.getText().toString();
			statmentClick(text, v);

		}
	};
	/** 邮件对账单 */
	final OnClickListener emailstatmentListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			billSetupId = 1;
			Button tv = (Button) v;
			String text = tv.getText().toString();
			statmentClick(text, v);

		}
	};
	/** 手机对账单 */
	final OnClickListener phonestatmentListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			billSetupId = 2;
			Button tv = (Button) v;
			String text = tv.getText().toString();
			statmentClick(text, v);

		}
	};
	/** 对账单 开通 */
	final OnClickListener phonestatmentopenClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			isOpenOrEdit = "open";
			Intent it = new Intent(MyCardmessageAndSetActivity.this,
					CrcdPsnCheckOpenActivity.class);
			it.putExtra(ConstantGloble.CRCD_ISOPENOREDIT, isOpenOrEdit);
			it.putExtra(ConstantGloble.CRCD_BILLSETUPID, billSetupId);
			it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
			it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
			// startActivity(it);
			startActivityForResult(it,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			LogGloble.e(TAG, "billSetupId===" + billSetupId);

		}
	};
	/** 对账单 修改 */
	final OnClickListener phonestatmentchangeClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			isOpenOrEdit = "edit";
			Intent it = new Intent(MyCardmessageAndSetActivity.this,
					CrcdPsnCheckOpenActivity.class);
			it.putExtra(ConstantGloble.CRCD_ISOPENOREDIT, isOpenOrEdit);
			it.putExtra(ConstantGloble.CRCD_BILLSETUPID, billSetupId);
			it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
			it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
			it.putExtra(Crcd.CRCD_PAPERADDRESS, paperAddress);
			it.putExtra(Crcd.CRCD_MOBILE, mobile);
			it.putExtra(Crcd.CRCD_EMAIL, email);
			// startActivity(it);
			startActivityForResult(it,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);

			LogGloble.e(TAG, "billSetupId===" + billSetupId);

		}
	};
	/** 对账单 重置 */
	final OnClickListener phonestatmentrestoreClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			BaseHttpEngine.showProgressDialog();
			
			PsnQueryCrcdBillIsExist(accountId);
		


		}
	};
	/** 对账单 关闭 */
	final OnClickListener phonestatmentcloseClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent it = new Intent(MyCardmessageAndSetActivity.this,
					CrcdPsnCheckCloseActivity.class);
			it.putExtra(ConstantGloble.CRCD_ISOPENOREDIT, isOpenOrEdit);
			it.putExtra(ConstantGloble.CRCD_BILLSETUPID, billSetupId);
			it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
			it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
			it.putExtra(Crcd.CRCD_PAPERADDRESS, paperAddress);
			it.putExtra(Crcd.CRCD_MOBILE, mobile);
			it.putExtra(Crcd.CRCD_EMAIL, email);
			// startActivity(it);
			startActivityForResult(it,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);

			LogGloble.e(TAG, "type===" + type);

		}
	};
	/** 对账单 取消 */
	final OnClickListener phonestatmentcancelClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			LogGloble.e(TAG, "type===" + type);
		}
	};

	protected void statmentClick(String text, View v) {

		if (text.equals(getResources().getString(R.string.mycrcd_set_open))) {

			phonestatmentopenClick.onClick(v);
		}
		if (text.equals(getResources().getString(R.string.mycrcd_set_change))) {

			phonestatmentchangeClick.onClick(v);
		}
		if (text.equals(getResources().getString(R.string.mycrcd_set_restore))) {

			phonestatmentrestoreClick.onClick(v);
		}
		if (text.equals(getResources().getString(R.string.mycrcd_set_close))) {

			phonestatmentcloseClick.onClick(v);
		}
		if (text.equals(getResources().getString(R.string.mycrcd_set_cancel))) {

			phonestatmentcancelClick.onClick(v);
		}
		// if (text.equals(getResources().getString(R.string.product_contract)))
		// {
		//
		// contractClick.onClick(v);
		// }

	}

	private void PsnQueryCrcdBillIsExist(String accountId) {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNQUERYCRCDBILLISEXIST);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnQueryCrcdBillIsExistCallBack");
	
		
	}


	public void requestPsnQueryCrcdBillIsExistCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		String BillExist=(String)resultMap.get(Crcd.CRCD_ISBILLEXIST);
//		0：没有出账单  1：已出
		if("0".equals(BillExist)){
			
			isBillExist=false;	
		}else if("1".equals(BillExist)){
			isBillExist=true;
		}else{
			isBillExist=false;	
		}
		
		requestSystemDateTime();	
	}
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
//		String date = dateTime;

		if (billSetupId == 1) {
			resetEmail();
		} else if (billSetupId == 2) {
			resetPhone();
		}

	}

	/** 重置电子邮件对账单 */
	private void resetEmail() {
		Intent it = new Intent(MyCardmessageAndSetActivity.this,
				CrcdPsnEmailResetSubmitActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_EMAIL, email);
		it.putExtra(Comm.DATETME, dateTime);
		it.putExtra(Crcd.CRCD_ISBILLEXIST, isBillExist);
		it.putExtra(ConstantGloble.FOREX_TAG, billSetupId);
		startActivityForResult(it,
				ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		BaseHttpEngine.dissMissProgressDialog();
	}

	/** 重置手机号对账单 */
	private void resetPhone() {
		Intent it = new Intent(MyCardmessageAndSetActivity.this,
				CrcdPsnEmailResetSubmitActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_MOBILE, mobile);
		it.putExtra(Comm.DATETME, dateTime);
		it.putExtra(Crcd.CRCD_ISBILLEXIST, isBillExist);
		it.putExtra(ConstantGloble.FOREX_TAG, billSetupId);
		startActivityForResult(it,
				ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		BaseHttpEngine.dissMissProgressDialog();
	}

	/** tokenId---回调 */
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		if (StringUtil.isNull(token)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		String openTag = null;
		if (openFlag.equals(ConstantGloble.CRCD_FALSE_KEY)) {
			// 开通上送：ADTE
			openTag = ConstantGloble.CRCD_OPEN_KEY;
		} else {
			// 关闭上送：MINP
			openTag = ConstantGloble.CRCD_CLOSE_KEY;
		}
		requestPsnCrcdChargeOnRMBAccountSet(accountId, openTag, token);
	}

	/**
	 * 全球交易人民币记账功能设置（开通/关闭）
	 * 
	 * @param accountId
	 *            ：账户标志
	 * @param openFlag
	 *            ：开通标志
	 * @param token
	 */
	private void requestPsnCrcdChargeOnRMBAccountSet(String accountId,
			String openFlag, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_RMBACCOUNTSET_API);
		Map<String, String> params = new HashMap<String, String>();
		params.put(Crcd.CRCD_ACCOUNTIDS_REQ, accountId);
		params.put(Crcd.CRCD_OPENFLAG_REQ, openFlag);
		params.put(Crcd.CRCD_TOCKEN_REQ, token);
		biiRequestBody.setParams(params);
		biiRequestBody.setConversationId(commConversationId);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCrcdChargeOnRMBAccountSetCallBack");
	}

	/** 全球交易人民币记账功能设置（开通/关闭）---回调 */
	public void requestPsnCrcdChargeOnRMBAccountSetCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Object result = biiResponseBody.getResult();
		String card = getResources()
				.getString(R.string.crcd_setUp_confirm_card);
		String rmb = getResources().getString(R.string.crcd_setUp_confirm_rmb);
		String open = getResources().getString(
				R.string.crcd_setUp_confirm_rmb_opene);
		String close = getResources().getString(
				R.string.crcd_setUp_confirm_rmb_close);

		String openText = card + StringUtil.getForSixForString(accountNumber)
				+ rmb + open;
		String closeText = card + StringUtil.getForSixForString(accountNumber)
				+ rmb + close;
		// 返回true
		if (openFlag.equals(ConstantGloble.CRCD_TRUE_KEY)) {
			// 开通记账功能,现在需关闭记账功能
			CustomDialog.toastInCenter(this, closeText);
			openFlag = ConstantGloble.CRCD_FALSE_KEY;
			card_fchargeflag.setText(getResources().getString(
					R.string.mycrcd_set_weikaitong));
		} else {
			// 关闭记账功能,现在需开通记账功能
			CustomDialog.toastInCenter(this, openText);
			openFlag = ConstantGloble.CRCD_TRUE_KEY;
			card_fchargeflag.setText(getResources().getString(
					R.string.mycrcd_set_yikaitong));
		}
		if (openFlag.equals(ConstantGloble.CRCD_TRUE_KEY)) {
			fchargeflag = new String[] {
					getResources().getString(R.string.mycrcd_set_close),
					getResources().getString(R.string.mycrcd_set_cancel), };
		} else if (openFlag.equals(ConstantGloble.CRCD_FALSE_KEY)) {
			fchargeflag = new String[] {
					getResources().getString(R.string.mycrcd_set_open),
					getResources().getString(R.string.mycrcd_set_cancel), };
		}

		PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
				BaseDroidApp.getInstanse().getCurrentAct(),
				lin_card_fchargeflag, fchargeflag, fchargeflagListener);

	}

	// ACTIVATION PENDING
	// CANCELLED PLASTIC
	// DECLINE WITH FRAUD
	// FRAUD BLOCK CODE
	// FIRST USAGE LIMITS EXCEEDED
	// LOST PLASTIC
	// LOCK
	// PICK UP WITH FRAUD
	// PIN RETRIES EXCEEDED
	// QCC CLOSE PENDING & BLOCK
	// REFER WITH FRAUD
	// STOLEN PLASTIC
	// STOP PAYMENT
	// BRANCH BLOCK CODE
	// COLLECTION BLOCK CODE
	// CSR BLOCK CODE
	//

}
