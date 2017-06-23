package com.chinamworld.bocmbci.biz.bond.allbond;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondConstant;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.biz.bond.acct.BankAcctListActivity;
import com.chinamworld.bocmbci.biz.bond.bondtran.BuyBondMsgFillActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask.LoginCallback;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 债券行情详情
 * 
 * @author panwe
 * 
 */
public class BondInfoActivity extends BondBaseActivity {

	protected static final int REQUEST_LOGIN_CODE = 10001;

	/** 主布局 */
	private View mainView;
	/** 当前显示条目 */
	private int mPostion;
	/** 利率 */
	private TextView tvBondRate;
	/** 债券简称和债券代码 */
	private TextView tvBondNameAndCode;
	/** 债券状态 */
	private String bondStatusStr;
	private TextView tvBondState;
	/** 利率类型 */
	private TextView tvInterest;
	/** 债券全称 */
	private TextView tvFullName;
	/** 买入全价 */
	private TextView tvBuyFullPrice;
	/** 卖出全价 */
	private TextView tvSellFullPrice;
	/** 买入净价 */
	private TextView tvBuyPrice;
	/** 卖出净价 */
	private TextView tvSellPrice;
	/** 期限 */
	private TextView tvTotalBondTerm;
	/** 计息周期 */
	private TextView tvInterestCycle;
	/** 应计利息 */
	private TextView tvDdbAmt2;
	/** 下次付息日 */
	private TextView tvNextInterestDate;
	/** 发售期-开始 */
	private TextView tvStartDateStart;
	/** 发售期-结束 */
	private TextView tvStartDateEnd;
	/** 起息日 */
	private TextView tvInterestBeginDate;
	/** 到期日 */
	private TextView tvEndDate;
	private Button btnBuy;
	private boolean isLogin;

	/** 国债代码 */
	private String bondCodeStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.bond_bondinfo_title));
		// 添加布局
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_allbond_info, null);
		addView(mainView);
		layoutContent.setPadding(0, 0, 0, 0);
		BondDataCenter.getInstance().addActivity(this);
		init();
	}

	private void init() {
		mPostion = getIntent().getIntExtra(POSITION, 0);
		tvBondRate = (TextView) mainView.findViewById(R.id.tv_bond_rate);
		tvBondNameAndCode = (TextView) mainView
				.findViewById(R.id.tv_bond_name_and_code);
		tvFullName = (TextView) mainView.findViewById(R.id.tv_full_name);
		tvBondState = (TextView) mainView.findViewById(R.id.tv_state);
		// TextView tvBeginDate = (TextView) mainView
		// .findViewById(R.id.tv_beginDate);
		//
		tvEndDate = (TextView) mainView.findViewById(R.id.tv_endDate);
		// TextView tvEmitEndDate = (TextView) mainView
		// .findViewById(R.id.tv_emitEndDate);
		// TextView tvMarketDate = (TextView) mainView
		// .findViewById(R.id.tv_marketDate);
		// TextView tvChangeEndDate = (TextView) mainView
		// .findViewById(R.id.tv_changeEndDate);
		tvInterest = (TextView) mainView.findViewById(R.id.tv_interest);
		//
		tvTotalBondTerm = (TextView) mainView
				.findViewById(R.id.tv_totalBondTerm);
		tvInterestCycle = (TextView) mainView
				.findViewById(R.id.tv_interestCycle);
		tvInterestBeginDate = (TextView) mainView
				.findViewById(R.id.tv_interestBeginDate);
		tvDdbAmt2 = (TextView) mainView.findViewById(R.id.tv_dbAmt2);
		// TextView tvNextInterestDate = (TextView) mainView
		// .findViewById(R.id.tv_nextInterestDate);
		//
		// TextView tvBondInt2 = (TextView) mainView
		// .findViewById(R.id.tv_bondInt2);
		tvBuyFullPrice = (TextView) mainView
				.findViewById(R.id.tv_bond_buyFullPrice);
		tvBuyPrice = (TextView) mainView.findViewById(R.id.tv_bond_buyPrice);
		// TextView tvBuyFullPrice1 = (TextView) mainView
		// .findViewById(R.id.tv_buyFullPrice1);
		// TextView tvBuyFullPrice2 = (TextView) mainView
		// .findViewById(R.id.tv_buyFullPrice2);
		// TextView tvBuyFullPrice3 = (TextView) mainView
		// .findViewById(R.id.tv_buyFullPrice3);
		tvSellFullPrice = (TextView) mainView
				.findViewById(R.id.tv_bond_sellFullPrice);
		tvSellPrice = (TextView) mainView.findViewById(R.id.tv_bond_sellPrice);
		// TextView tvSellFullPrice1 = (TextView) mainView
		// .findViewById(R.id.tv_sellFullPrice1);
		// TextView tvSellFullPrice2 = (TextView) mainView
		// .findViewById(R.id.tv_sellFullPrice2);
		// TextView tvSellFullPrice3 = (TextView) mainView
		// .findViewById(R.id.tv_sellFullPrice3);
		//
		// TextView tvBuyPrice1 = (TextView) mainView
		// .findViewById(R.id.tv_buyPrice1);
		// TextView tvSellPrice1 = (TextView) mainView
		// .findViewById(R.id.tv_sellPrice1);
		// TextView tvBuyPrice2 = (TextView) mainView
		// .findViewById(R.id.tv_buyPrice2);
		// TextView tvSellPrice2 = (TextView) mainView
		// .findViewById(R.id.tv_sellPrice2);
		// TextView tvBuyPrice3 = (TextView) mainView
		// .findViewById(R.id.tv_buyPrice3);
		// TextView tvSellPrice3 = (TextView) mainView
		// .findViewById(R.id.tv_sellPrice3);
		tvStartDateStart = (TextView) mainView
				.findViewById(R.id.tv_startDate_start);
		tvStartDateEnd = (TextView) mainView
				.findViewById(R.id.tv_startDate_end);
		// TextView tvLastChangeEndDate = (TextView) mainView
		// .findViewById(R.id.tv_lastChangeEndDate);
		tvNextInterestDate = (TextView) mainView
				.findViewById(R.id.tv_bond_nextInterestDate);

		btnBuy = (Button) mainView.findViewById(R.id.btnbuy);

		initData();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE:
			switch (resultCode) {
			case RESULT_OK:
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent = new Intent(BondInfoActivity.this,
						AllBondListActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}

			break;
		case REQUEST_LOGIN_CODE:
			switch (resultCode) {
			case RESULT_OK:
				// 请求是否开通投资理财
				 BondDataCenter.getInstance().setBuy(true);
				 requestPsnInvestmentManageIsOpen();
				break;


			default:
				break;
			}
			break;

		case BondConstant.BOND_REQUEST_OPEN_ACCT_CODE:
		case BondConstant.BOND_REQUEST_REG_ACCT_CODE:
			switch (resultCode) {
			case RESULT_OK:
				BaseDroidApp.getInstanse().dismissMessageDialog();
				requestPsnInvestmentManageIsOpen();
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	private void requestDetail(String bondId) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put(Bond.BOND_CODE, bondId);
		parms.put(Bond.BOND_TYPE, BondDataCenter.bondType_re.get(1));
		biiRequestBody.setParams(parms);
		biiRequestBody.setMethod(Bond.EMTHOD_BOND_DETAIL);
		HttpManager.requestBii(biiRequestBody, this, "bondDetailCallBack");
	}

	/** 债券详情返回处理 */
	public void bondDetailCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (result == null) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.bond_comm_error));
			return;
		}
		BondDataCenter.getInstance().setBuy(true);
		BondDataCenter.getInstance().setBondMap(result);
		requestPsnInvestmentManageIsOpen();
	}

	/** 登记--资金账号列表返回处理 **/
	@Override
	public void bankAccListCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.bankAccListCallBack(resultObj);
		List<Map<String, Object>> bankAccList = BondDataCenter.getInstance()
				.getBankAccList();
		if (bankAccList == null) {
			// 提示关联
		} else {
			Intent it = new Intent(this, BankAcctListActivity.class);
			it.putExtra(ISOPEN, false);
			it.putExtra(ISBUY, true);
			startActivityForResult(it, BondConstant.BOND_REQUEST_REG_ACCT_CODE);
			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
		}
	}

	/** 系统时间 */
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(dateTime)) {
			return;
		}
		BondDataCenter.getInstance().setSysTime(dateTime);
		startBuyBondMsgFillAct();
	}

	public void startBuyBondMsgFillAct() {
		Intent it = new Intent(BondInfoActivity.this,
				BuyBondMsgFillActivity.class);
		it.putExtra(POSITION, mPostion);
		if ("Y".equals(bondStatusStr)) {
			it.putExtra(Bond.RE_HISTORYTRAN_QUERY_TRANTYPE,
					Bond.BOND_TRANTYPE_BUY);
		} else {
			it.putExtra(Bond.RE_HISTORYTRAN_QUERY_TRANTYPE,
					Bond.BOND_TRANTYPE_APPLY);
		}
		BondInfoActivity.this.startActivity(it);
	}

	private void initData() {
		Map<String, Object> bondDetail = BondDataCenter.getInstance()
				.getBondMap();
		tvBondRate.setText(StringUtil.parseStringPattern(
				(String) bondDetail.get(Bond.BOND_INT), 2)
				+ "%");
		String bondNameStr = StringUtil.valueOf1((String) bondDetail
				.get(Bond.BOND_SHORTNAME));
		bondCodeStr = StringUtil.valueOf1((String) bondDetail
				.get(Bond.BOND_CODE));
		StringBuffer sb = new StringBuffer();
		sb.append(bondNameStr);
		sb.append("(");
		sb.append(bondCodeStr);
		sb.append(")");
		tvBondNameAndCode.setText(sb.toString());
		tvFullName.setText(StringUtil.valueOf1((String) bondDetail
				.get(Bond.BOND_NAME)));

		// /** 债券代码 */
		// tvBondCode
		// .setText(commSetText((String) bondDetail.get(Bond.BOND_CODE)));
		// /** 债券全称 */
		// tvBondName
		// .setText(commSetText((String) bondDetail.get(Bond.BOND_NAME)));
		// PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
		// tvBondName);
		// /** 债券简称 */
		// tvBondShortName.setText(commSetText((String) bondDetail
		// .get(Bond.BOND_SHORTNAME)));
		// PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
		// tvBondShortName);
		/** 状态 */
		bondStatusStr = (String) bondDetail.get(Bond.BOND_STATUS);
		String bondStateStr = BondDataCenter.bondStatus.get(bondStatusStr);
		int bstart = bondStateStr.indexOf("买卖");
		if (bstart == -1) {
			bstart = bondStateStr.indexOf("申购");
		}
		SpannableStringBuilder bstyle = new SpannableStringBuilder(bondStateStr);
		bstyle.setSpan(
				new BackgroundColorSpan(getResources().getColor(
						R.color.share_button_normal_color)), 0, bstart,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		bstyle.setSpan(new ForegroundColorSpan(Color.WHITE), 0, bstart,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvBondState.setText(bstyle);
		// /** 发行日 */
		// tvBeginDate.setText(commSetText((String) bondDetail
		// .get(Bond.BOND_BEGINDATE)));
		/** 到期日 */
		tvEndDate.setText(StringUtil.valueOf1((String) bondDetail
				.get(Bond.BOND_ENDDATE)));
		// /** 发行截止日 */
		// tvEmitEndDate.setText(commSetText((String) bondDetail
		// .get(Bond.BOND_EMITENDDATE)));
		// /** 上市流通日 */
		// tvMarketDate.setText(commSetText((String) bondDetail
		// .get(Bond.BOND_MARKETDATE)));
		// /** 截止过户日 */
		// tvChangeEndDate.setText(commSetText((String) bondDetail
		// .get(Bond.BOND_CHANGEENDDATE)));
		/** 利率类型 */
		String interestStr = BondDataCenter.bondInterestTypeNew.get(bondDetail
				.get(Bond.BOND_INTERESTTYPE));
		int istart = interestStr.indexOf("利率");
		SpannableStringBuilder istyle = new SpannableStringBuilder(interestStr);
		istyle.setSpan(
				new BackgroundColorSpan(getResources().getColor(
						R.color.orange_new)), 0, istart,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		istyle.setSpan(new ForegroundColorSpan(Color.WHITE), 0, istart,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvInterest.setText(istyle);
		/** 期限 */

		String bondDateStr = StringUtil.valueOf1((String) bondDetail
				.get(Bond.BOND_BONDTERM));
		try {
			int bondDate=Integer.parseInt(bondDateStr);
			bondDateStr=String.valueOf(bondDate);
		} catch (Exception e) {
			bondDateStr="-";
		}
		if ("-".equals(bondDateStr)) {
			tvTotalBondTerm.setText(bondDateStr);
		} else {
			tvTotalBondTerm.setText(bondDateStr + "个月");
		}

		/** 计息周期    012 M*/

		String intersCycleStr = StringUtil.valueOf1((String) bondDetail
				.get(Bond.BOND_INTERESCYCLE)).trim();
		String intersCycleNum = intersCycleStr.substring(0 , 3);
		try {
			int intersDate=Integer.parseInt(intersCycleNum);
			intersCycleNum =String.valueOf(intersDate);
		} catch (Exception e) {
			intersCycleNum="-";
		}
		if ("-".equals(intersCycleNum)) {
			tvInterestCycle.setText(intersCycleNum);
		} else {
			if (intersCycleStr.length() == 3) {
				tvInterestCycle.setText(intersCycleNum + "个月");
			} else {
				if (intersCycleStr.contains("M")) {
					tvInterestCycle.setText(intersCycleNum + "个月");
				} else if (intersCycleStr.contains("D")) {
					tvInterestCycle.setText(intersCycleNum + "天");
				}
			}
		}


		/** 起息日 */
		tvInterestBeginDate.setText(StringUtil.valueOf1((String) bondDetail
				.get(Bond.BOND_INTERESBEFINDATE)));
		/** 应计利息 */
		String dbamtStr = StringUtil.valueOf1((String) bondDetail
				.get(Bond.BOND_DBAMT));
		if ("-".equals(dbamtStr)) {
			tvDdbAmt2.setText(dbamtStr);
		} else {
			tvDdbAmt2.setText(StringUtil.parseStringPattern(dbamtStr, 2));
		}
		/** 下次付息日 */
		tvNextInterestDate.setText(StringUtil.valueOf1((String) bondDetail
				.get(Bond.BOND_NEXTINTERESDATE)));

		String buyFullPriceStr = StringUtil.valueOf1((String) bondDetail
				.get(Bond.BOND_BUYFILL_PRICE));
		if ("-".equals(buyFullPriceStr)) {
			tvBuyFullPrice.setText(buyFullPriceStr);
		} else {
			tvBuyFullPrice.setText(StringUtil.parseStringPattern(
					buyFullPriceStr, 2));
		}

		String buyPriceStr = StringUtil.valueOf1((String) bondDetail
				.get(Bond.BOND_BUY_PRICE));
		if ("-".equals(buyPriceStr)) {
			tvBuyPrice.setText(buyPriceStr);
		} else {
			tvBuyPrice.setText(StringUtil.parseStringPattern(buyPriceStr, 2));
		}

		String sellFullPriceStr = StringUtil.valueOf1((String) bondDetail
				.get(Bond.BOND_SELLFILL_PRICE));
		if ("-".equals(sellFullPriceStr)) {
			tvSellFullPrice.setText(sellFullPriceStr);
		} else {
			tvSellFullPrice.setText(StringUtil.parseStringPattern(
					sellFullPriceStr, 2));
		}

		String sellPriceStr = StringUtil.valueOf1((String) bondDetail
				.get(Bond.BOND_SELL_PRICE));
		if ("-".equals(sellPriceStr)) {
			tvSellPrice.setText(sellPriceStr);
		} else {
			tvSellPrice.setText(StringUtil.parseStringPattern(sellPriceStr, 2));
		}

		tvStartDateStart.setText(StringUtil.valueOf1((String) bondDetail
				.get(Bond.BOND_BEGINDATE)) + "-");

		tvStartDateEnd.setText(StringUtil.valueOf1((String) bondDetail
				.get(Bond.BOND_EMITENDDATE)));

		if ("Y".equals(bondStatusStr)) {
			btnBuy.setText(getString(R.string.bond_buy));
		} else {
			btnBuy.setText(getString(R.string.bond_shengou));
		}
		// /** 利率 */
		// tvBondInt2.setText(StringUtil.parseStringPattern(
		// (String) bondDetail.get(Bond.BOND_INT), 2)
		// + "%");
		// /** 买入全价1 */
		// tvBuyFullPrice1.setText(StringUtil.parseStringPattern(
		// (String) bondDetail.get(Bond.BOND_BUYFILL_PRICE), 2));
		// /** 买入全价2 */
		// tvBuyFullPrice2.setText(StringUtil.parseStringPattern(
		// (String) bondDetail.get(Bond.BOND_BUYFILL_PRICE1), 2));
		// /** 买入全价3 */
		// tvBuyFullPrice3.setText(StringUtil.parseStringPattern(
		// (String) bondDetail.get(Bond.BOND_BUYFILL_PRICE2), 2));
		// /** 卖出全价1 */
		// tvSellFullPrice1.setText(StringUtil.parseStringPattern(
		// (String) bondDetail.get(Bond.BOND_SELLFILL_PRICE), 2));
		// /** 卖出全价2 */
		// tvSellFullPrice2.setText(StringUtil.parseStringPattern(
		// (String) bondDetail.get(Bond.BOND_SELLFILL_PRICE1), 2));
		// /** 卖出全价3 */
		// tvSellFullPrice3.setText(StringUtil.parseStringPattern(
		// (String) bondDetail.get(Bond.BOND_SELLFILL_PRICE2), 2));
		// /** 买入净价1 */
		// tvBuyPrice1.setText(StringUtil.parseStringPattern(
		// (String) bondDetail.get(Bond.BOND_BUY_PRICE), 2));
		// /** 买入净价2 */
		// tvBuyPrice2.setText(StringUtil.parseStringPattern(
		// (String) bondDetail.get(Bond.BOND_BUY_PRICE1), 2));
		// /** 买入净价3 */
		// tvBuyPrice3.setText(StringUtil.parseStringPattern(
		// (String) bondDetail.get(Bond.BOND_BUY_PRICE2), 2));
		// /** 卖出净价1 */
		// tvSellPrice1.setText(StringUtil.parseStringPattern(
		// (String) bondDetail.get(Bond.BOND_SELL_PRICE), 2));
		// /** 卖出净价2 */
		// tvSellPrice2.setText(StringUtil.parseStringPattern(
		// (String) bondDetail.get(Bond.BOND_SELL_PRICE1), 2));
		// /** 卖出净价3 */
		// tvSellPrice3.setText(StringUtil.parseStringPattern(
		// (String) bondDetail.get(Bond.BOND_SELL_PRICE2), 2));
		// /** 下次转托管停办日 */
		// tvNextChangeEndDate.setText(commSetText((String) bondDetail
		// .get(Bond.BOND_NEXTCHANGEENDDATE)));
		// /** 最后转托管停办日 */
		// tvLastChangeEndDate.setText(commSetText((String) bondDetail
		// .get(Bond.BOND_LASTCHANGEENDDATE)));
		setShowAllTextListener(tvFullName, tvBuyFullPrice, tvSellFullPrice,
				tvBuyPrice, tvSellPrice, tvDdbAmt2);
		setListeners();
	}


	private void setListeners() {
		btnBuy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isLogin) {
					// startBuyBondMsgFillAct();
					// 请求是否开通投资理财
					BondDataCenter.getInstance().setBuy(true);
					requestPsnInvestmentManageIsOpen();
				} else {
//					Intent it = new Intent(BondInfoActivity.this,
//							LoginActivity.class);
//					startActivityForResult(it, REQUEST_LOGIN_CODE);
					BaseActivity.getLoginUtils(BondInfoActivity.this).exe(new LoginCallback() {

						@Override
						public void loginStatua(boolean isLogin) {
							if(isLogin){
								BondDataCenter.getInstance().setBuy(true);
								requestPsnInvestmentManageIsOpen();
							}
						}
					});
				}
			}
		});

	}

	public void setShowAllTextListener(TextView... textViews) {
		for (TextView tv : textViews) {
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv);
		}
	}

	private OnClickListener rightBtnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
//			Intent intent =new Intent(BondInfoActivity.this , LoginActivity.class);
//			startActivityForResult(intent,
//					ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE);
			BaseActivity.getLoginUtils(BondInfoActivity.this).exe(new LoginCallback() {

				@Override
				public void loginStatua(boolean isLogin) {
					if(isLogin){
						ActivityTaskManager.getInstance().removeAllSecondActivity();
						Intent intent = new Intent(BondInfoActivity.this,
								AllBondListActivity.class);
						startActivity(intent);
					}
				}
			});
		}
	};
	@Override
	protected void onResume() {
		super.onResume();
		isLogin = BaseDroidApp.getInstanse().isLogin();
		if (isLogin) {
			setText(this.getString(R.string.go_main));
			setRightBtnClick(backMain);
		} else {
			setText(this.getString(R.string.login));
			setRightBtnClick(rightBtnClick);
		}
	}
	
}
