package com.chinamworld.bocmbci.biz.lsforex.manageacc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.acc.IsForexSettingBindAccActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 双向宝签约详情 */
public class IsForexProduceDialogActivity extends IsForexBaseActivity {
	private int selectPosition = -1;
	private List<Map<String, String>> result = null;
	private Map<String, String> resultDatail = null;
	private String accountNumber = null;
	private String nickName = null;
	private String marginAccountNo = null;
	private String settleCurrency = null;
	private String liquidationRatio = null;
	// 变更按钮
	private Button changeButton = null;
	// 解约按钮
	private Button signButton = null;
	private ImageButton closeButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseDroidApp.getInstanse().setDialogAct(true);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		selectPosition = getIntent().getIntExtra(ConstantGloble.POSITION, -1);

		resultDatail = (Map<String, String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.ISFOREX_RESULTDATAIL);
		result = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.ISFOREX_RESULT);
		if (selectPosition == -1 || StringUtil.isNullOrEmpty(resultDatail) || StringUtil.isNullOrEmpty(result)) {
			return;
		}
		init();
		initOnClick();
	}

	private void init() {
		View popView = LayoutInflater.from(this).inflate(R.layout.isforex_manage_produce_detail, null);
		setContentView(popView);
		TextView bailAccText = (TextView) popView.findViewById(R.id.isforex_bailAcc);
		TextView tradeAccText = (TextView) popView.findViewById(R.id.isforex_tradeAcc);
		TextView nickNameText = (TextView) popView.findViewById(R.id.isforex_tradeAcc_nickname);
		TextView jsCodeText = (TextView) popView.findViewById(R.id.isForex_vfgRegCurrency1);
		TextView signText = (TextView) popView.findViewById(R.id.isForex_manage_produce_times);
		TextView zcRateText = (TextView) popView.findViewById(R.id.isForex_manage_liquidationRatio);
		TextView bjRateText = (TextView) popView.findViewById(R.id.isForex_manage_warnRatio);
		TextView needText = (TextView) popView.findViewById(R.id.isForex_manage_needMarginRatio);
		TextView kcRateText = (TextView) popView.findViewById(R.id.isForex_manage_openRate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, nickNameText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, jsCodeText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, zcRateText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, bjRateText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, needText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, kcRateText);
		Map<String, String> map = result.get(selectPosition);
		accountNumber = map.get(IsForex.ISFOREX_ACCOUNTNUMBER_RES);
		String subAcctType = map.get(IsForex.ISFOREX_SUBACCTTYPE);
		nickName = map.get(IsForex.ISFOREX_NICKNAME_RES1);
		marginAccountNo = map.get(IsForex.ISFOREX_MARGINACCOUNTNO_RES1);
		settleCurrency = map.get(IsForex.ISFOREX_SETTLECURRENCY_RES1);
		String signDate = map.get(IsForex.ISFOREX_SIGNDATE_RES);
		String needMarginRatio = resultDatail.get(IsForex.ISFOREX_NEEDMARGINRATE_RES);
		String warnRatio = resultDatail.get(IsForex.ISFOREX_WARNRATIO_RES);
		liquidationRatio = resultDatail.get(IsForex.ISFOREX_LIQUIDATIONRATIO_RES);
		String openRate = resultDatail.get(IsForex.ISFOREX_OPENRATE_RES);
		String bailAcc = null;
		if (!StringUtil.isNull(marginAccountNo)) {
			bailAcc = StringUtil.getForSixForString(marginAccountNo);
		}
		String tradeAcc = null;
		if (!StringUtil.isNull(accountNumber)) {
			tradeAcc = StringUtil.getForSixForString(accountNumber);
		}
		String code = null;
		if (!StringUtil.isNull(settleCurrency) && LocalData.Currency.containsKey(settleCurrency)) {
			code = LocalData.Currency.get(settleCurrency);
		}
		String zcRate = null;
		if (StringUtil.isNull(liquidationRatio)) {
			zcRateText.setText("-");
		} else {
			zcRate = StringUtil.dealNumber(liquidationRatio);
			zcRateText.setText(zcRate);
		}
		String bjRate = null;
		if (StringUtil.isNull(warnRatio)) {
			bjRateText.setText("-");
		} else {
			bjRate = StringUtil.dealNumber(warnRatio);
			bjRateText.setText(bjRate);
		}
		String kcRate = StringUtil.dealNumber(openRate);
		if (StringUtil.isNull(openRate)) {
			kcRateText.setText("-");
		} else {
			kcRate = StringUtil.dealNumber(openRate);
			kcRateText.setText(kcRate);
		}

		String needRate = StringUtil.dealNumber(needMarginRatio);
		if (StringUtil.isNull(needMarginRatio)) {
			needText.setText("-");
		} else {
			needRate = StringUtil.dealNumber(needMarginRatio);
			needText.setText(needRate);
		}
		bailAccText.setText(bailAcc);
		tradeAccText.setText(tradeAcc);
		nickNameText.setText(nickName);
		String cash = null;
		// 0 – 人民币户 1 – 单钞户 3 – 钞汇户 即 1、人民币账户 2、XX现钞账户 3、XX钞汇账户
		if (!StringUtil.isNull(subAcctType)) {
			if (ConstantGloble.FOREX_FLAG0.equals(subAcctType)) {
				cash = getString(R.string.isForex_manage_cash_rmb);
				jsCodeText.setText(cash);
			} else if (ConstantGloble.FOREX_FLAG1.equals(subAcctType)) {
				cash = getString(R.string.isForex_manage_cash_cash1);
				jsCodeText.setText(code + cash);
			} else if (ConstantGloble.FOREX_FLAG3.equals(subAcctType)) {
				cash = getString(R.string.isForex_manage_cash_cash2);
				jsCodeText.setText(code + cash);
			} else {
				jsCodeText.setText(code);
			}
		}
		signText.setText(signDate);

		// 变更按钮
		changeButton = (Button) popView.findViewById(R.id.dept_checkout_btn);
		// 更多按钮
		signButton = (Button) popView.findViewById(R.id.dept_create_notice_btn);
		// 关闭按钮
		closeButton = (ImageButton) popView.findViewById(R.id.dept_close_ib);
		String acc = getResources().getString(R.string.isForex_manage_acc);
		String cancel = getResources().getString(R.string.isForex_manage_qianyue);
		String[] selectors = new String[] { acc, cancel };
		PopupWindowUtils.getInstance().setshowMoreChooseUpListener(this, signButton, selectors, greatwallListener);
	}

	private void initOnClick() {

		changeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 账户变更
				BaseHttpEngine.showProgressDialog();
				// 过滤出符合条件的借记卡
				requestPsnVFGFilterDebitCard();
			}
		});
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private OnClickListener greatwallListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Integer tag = (Integer) v.getTag();
			switch (tag) {
			case 0:// 登记交易账户
				setAcconClick();
				break;
			case 1:// 解约
				cancelButtonClick();
				break;
			default:
				break;
			}
		}
	};

	/** 解约事件 */
	private void cancelButtonClick() {
		Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), IsForexCancelConfirmActivity.class);
		intent.putExtra(IsForex.ISFOREX_ACCOUNTNUMBER_RES, accountNumber);
		intent.putExtra(IsForex.ISFOREX_NICKNAME_RES1, nickName);
		intent.putExtra(IsForex.ISFOREX_MARGINACCOUNTNO_RES1, marginAccountNo);
		intent.putExtra(IsForex.ISFOREX_SETTLECURRENCY_RES1, settleCurrency);
		intent.putExtra(IsForex.ISFOREX_LIQUIDATIONRATIO_RES, liquidationRatio);
		startActivity(intent);
	}

	private void setAcconClick() {
		Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), IsForexSettingBindAccActivity.class);
		startActivity(intent);
	}

	/** 过滤出符合条件的借记卡----回调 */
	@Override
	public void requestPsnVFGFilterDebitCardCallback(Object resultObj) {
		super.requestPsnVFGFilterDebitCardCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, String>> resultList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (resultList == null || resultList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		if (StringUtil.isNull(accountNumber)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		getRightTradeAcc(resultList);

	}

	/** 得到符合条件的借记卡 */
	private void getRightTradeAcc(List<Map<String, String>> resultList) {
		List<Map<String, String>> newTradeAccResultList = null;
		newTradeAccResultList = new ArrayList<Map<String, String>>();
		int len = resultList.size();
		for (int i = 0; i < len; i++) {
			Map<String, String> map = resultList.get(i);
			String account = map.get(IsForex.ISFOREX_ACCOUNTNUMBER_RES);
			if (!StringUtil.isNull(account)) {
				if (!account.equals(accountNumber)) {
					newTradeAccResultList.add(map);
				}
			}
		}
		// 过滤之后没有符合条件的借记卡
		if (StringUtil.isNullOrEmpty(newTradeAccResultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.isForex_times_change_noacc));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(IsForex.ISFOREX_ACCOUNTNUMBER_RES, newTradeAccResultList);
		gotoActivity();
	}

	/** 跳转到账户变更页面 */
	private void gotoActivity() {
		Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), IsForexChangeSubmitActivity.class);
		intent.putExtra(IsForex.ISFOREX_ACCOUNTNUMBER_RES, accountNumber);
		intent.putExtra(IsForex.ISFOREX_NICKNAME_RES1, nickName);
		intent.putExtra(IsForex.ISFOREX_MARGINACCOUNTNO_RES1, marginAccountNo);
		intent.putExtra(IsForex.ISFOREX_SETTLECURRENCY_RES1, settleCurrency);
		intent.putExtra(IsForex.ISFOREX_LIQUIDATIONRATIO_RES, liquidationRatio);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.GENDER_MAN, 1);
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(intent);
	}
}
