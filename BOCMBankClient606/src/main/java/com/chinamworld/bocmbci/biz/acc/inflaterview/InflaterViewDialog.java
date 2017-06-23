package com.chinamworld.bocmbci.biz.acc.inflaterview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.Ecard;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.adapter.FinanceIcSignChooseAdapter;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.QueryProductActivity;
import com.chinamworld.bocmbci.biz.bond.allbond.AllBondListActivity;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.finc.fundprice.FundPricesMenuActivity;
import com.chinamworld.bocmbci.biz.forex.rate.ForexRateInfoOutlayActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherBaseActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherInitiativeData;
import com.chinamworld.bocmbci.biz.gatherinitiative.creatgather.CreatGatherInputInfoActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.payquery.PayQueryActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.ecard.TransferEcardActivity;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.TransferManagerActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 存放账户管理中弹出框视图类
 * 
 * @author wangmengmeng
 * 
 */
@SuppressWarnings("ResourceType")
public class InflaterViewDialog {
	private Context currentContent;
	private View contentView;
	private List<Map<String, Object>> volumesAndCdnumbers;
	private ArrayAdapter<String> banksheetAdapter;
	/** 组装数据中 当前选中的list */
	private List<Map<String, Object>> currentList;
	private List<Map<String, Object>> deptcurrentList;
	private String balance = "";

	public InflaterViewDialog(Context currentContent) {
		this.currentContent = currentContent;
	}

	// 买基金
	final OnClickListener buyFincClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), FundPricesMenuActivity.class);
			BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
			BaseDroidApp.getInstanse().getCurrentAct().finish();
		}
	};
	// 买外汇
	final OnClickListener buyForexClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), ForexRateInfoOutlayActivity.class);
			intent.putExtra(ConstantGloble.FOREX_FOREXRATE_TO_ACC, true);
			BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
			BaseDroidApp.getInstanse().getCurrentAct().finish();
		}
	};
	// 买债券
	final OnClickListener buybondClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), AllBondListActivity.class);
			BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
			BaseDroidApp.getInstanse().getCurrentAct().finish();
		}
	};
	// 买理财
	final OnClickListener buyBocinvtClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), QueryProductActivity.class);
			BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
			BaseDroidApp.getInstanse().getCurrentAct().finish();
		}
	};
	// // 401 我要收款
	final OnClickListener goShouKuanClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), CreatGatherInputInfoActivity.class);
			intent.putExtra(GatherBaseActivity.IS_NEED_CHOOSE_ACCOUNT, false);
			BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
			BaseDroidApp.getInstanse().getCurrentAct().finish();
		}
	};
	// 我要付款
	final OnClickListener goFuKuanClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), PayQueryActivity.class);
			intent.putExtra(GatherBaseActivity.IS_NEED_CHOOSE_ACCOUNT, false);
			BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
			BaseDroidApp.getInstanse().getCurrentAct().finish();
		}
	};

	/** 更多点击事件 */
	final OnClickListener btnMoreNullBocinvtClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button tv = (Button) v;
			String text = tv.getText().toString();
			moreClick(text, v);
		}
	};

	/**
	 * 账户详情弹出框
	 * 
	 * @param bankAccountList
	 *            账户详情列表
	 * @param exitAccDetailClick
	 *            关闭详情监听事件
	 * @param updateNicknameClick
	 *            更新账号别名事件
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public View initAccountMessageDialogView(Activity act,
			final Map<String, Object> bankAccount,
			View.OnClickListener exitAccDetailClick,
			final OnClickListener updatenicknameClick, boolean ishavebinding,
			List<Map<String, String>> serviceList, String status, final OnClickListener payrollQueryClick) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.acc_mybankaccount_detail, null);

		final FrameLayout fl_acc_nickname = (FrameLayout) contentView.findViewById(R.id.fl_nickname);
		final LinearLayout ll_nickname = (LinearLayout) contentView.findViewById(R.id.ll_nickname);
		Button btn_updatenickname = (Button) contentView.findViewById(R.id.btn_update_nickname);
		final TextView tv_acc_accounttype = (TextView) contentView.findViewById(R.id.acc_accounttype_value);

		final TextView tv_acc_accountnickname = (TextView) contentView.findViewById(R.id.acc_accountnickname_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(BaseDroidApp.getInstanse().getCurrentAct(), tv_acc_accountnickname);
		TextView tv_acc_accountnumber = (TextView) contentView.findViewById(R.id.acc_account_number_value);
		/** 开户网点 */
		TextView acc_account_branchName = (TextView) contentView.findViewById(R.id.acc_account_branchName);
		/** 开户时间 */
		TextView acc_account_OpenDate = (TextView) contentView.findViewById(R.id.acc_account_OpenDate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(BaseDroidApp.getInstanse().getCurrentAct(), acc_account_branchName);
		
		String accOpenBank = (String) bankAccount.get(Acc.ACCOPENBANK);
		if (!StringUtil.isNull(accOpenBank)) {
			// 如果该字段不为空则反显，否则隐藏该字段
			acc_account_branchName.setText(accOpenBank);
		} else {
			contentView.findViewById(R.id.ll_acc_branchName).setVisibility(View.GONE);
		}
		String openDate = (String) bankAccount.get(Acc.ACC_OPENDATE_RES);
		if (!StringUtil.isNull(openDate)) {
			// 如果该字段不为空则反显，否则隐藏该字段
			acc_account_OpenDate.setText(openDate);
		} else {
			contentView.findViewById(R.id.ll_acc_openDate).setVisibility(View.GONE);
		}
		TextView tv_acc_account_state = (TextView) contentView.findViewById(R.id.acc_account_state);
		if (!StringUtil.isNull(status)) {
			tv_acc_account_state.setText(LocalData.SubAccountsStatus.get(status));
		}
		final EditText et_acc_accountnickname = (EditText) contentView.findViewById(R.id.et_acc_nickname);
		EditTextUtils.setLengthMatcher(act, et_acc_accountnickname, 20);
		ImageView img_exit = (ImageView) contentView.findViewById(R.id.img_exit_accdetail);
		final ImageView img_update_accnickname = (ImageView) contentView.findViewById(R.id.img_acc_update_nickname);
		final String acc_type = String.valueOf(bankAccount.get(Acc.ACC_ACCOUNTTYPE_RES));
		tv_acc_accounttype.setText(LocalData.AccountType.get(acc_type.trim()));
		tv_acc_accountnickname.setText((String) bankAccount.get(Acc.ACC_NICKNAME_RES));
		String acc_accountnumber = String.valueOf(bankAccount.get(Acc.ACC_ACCOUNTNUMBER_RES));
		tv_acc_accountnumber.setText(StringUtil.getForSixForString(acc_accountnumber));

		LinearLayout acc_currency_add = (LinearLayout) contentView.findViewById(R.id.ll_add_currency);
		/** 账户详情列表信息 */
		final List<Map<String, Object>> accountDetailList = (List<Map<String, Object>>) bankAccount.get(ConstantGloble.ACC_DETAILIST);
		List<Map<String, Object>> accdetailList = new ArrayList<Map<String, Object>>();
		if (accountDetailList == null || accountDetailList.size() == 0) {

		} else {
			for (int i = 0; i < accountDetailList.size(); i++) {
				String currencyname = (String) accountDetailList.get(i).get(Acc.DETAIL_CURRENCYCODE_RES);
				// 过滤
				if (StringUtil.isNull(LocalData.currencyboci.get(currencyname))) {
					accdetailList.add(accountDetailList.get(i));
				}
			}

			if (accdetailList == null || accdetailList.size() == 0) {

			} else {
				for (int i = 0; i < accdetailList.size(); i++) {
					View currency_view = LayoutInflater.from(currentContent).inflate(R.layout.acc_currency_balance, null);
					acc_currency_add.addView(currency_view, i);

					TextView tv_acc_currencycode = (TextView) currency_view.findViewById(R.id.acc_currencycode);
					TextView tv_acc_accbookbalance = (TextView) currency_view.findViewById(R.id.acc_bookbalance);
					PopupWindowUtils.getInstance().setOnShowAllTextListener(BaseDroidApp.getInstanse().getCurrentAct(), tv_acc_accbookbalance);
					PopupWindowUtils.getInstance().setOnShowAllTextListener(BaseDroidApp.getInstanse().getCurrentAct(), tv_acc_currencycode);
					String currencyname = (String) accdetailList.get(i).get(Acc.DETAIL_CURRENCYCODE_RES);
					String cashRemit = (String) accdetailList.get(i).get(Acc.DETAIL_CASHREMIT_RES);
					if (LocalData.Currency.get(currencyname).equals(ConstantGloble.ACC_RMB)) {
						tv_acc_currencycode.setText(LocalData.Currency.get(currencyname) + ConstantGloble.ACC_COLON);
					} else {
						tv_acc_currencycode.setText(LocalData.Currency
								.get(currencyname)
								+ ConstantGloble.ACC_STRING
								+ LocalData.CurrencyCashremit.get(cashRemit)
								+ ConstantGloble.ACC_COLON);
					}

					tv_acc_accbookbalance.setText(
							StringUtil.parseStringCodePattern(currencyname,(String) accdetailList.get(i).get(Acc.DETAIL_AVAILABLEBALANCE_RES), 2));
				}
			}
		}
		// 退出账户详情点击事件
		img_exit.setOnClickListener(exitAccDetailClick);
		// 修改账户别名点击事件
		img_update_accnickname.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				fl_acc_nickname.setVisibility(View.VISIBLE);
				ll_nickname.setVisibility(View.GONE);
				BaseActivity activity = (BaseActivity) BaseDroidApp.getInstanse()
						.getCurrentAct();
				if(activity != null)
					activity.upSoftInput();
				et_acc_accountnickname.setText(tv_acc_accountnickname.getText().toString());
				et_acc_accountnickname.setSelection(et_acc_accountnickname.length());
			}
		});
		// 005（债券）、006（基金）、012（外汇）
		btn_updatenickname.setOnClickListener(updatenicknameClick);
		Button btn_one = (Button) contentView.findViewById(R.id.btn_one);
		/** 更多 */
		Button btn_many = (Button) contentView.findViewById(R.id.btn_many);
		// 判断显示按钮
		List<String> serlist = new ArrayList<String>();
		serlist.add(ConstantGloble.ACC_GO_TRANSFERDETAIL);
		if (serviceList == null || serviceList.size() == 0) {

		} else {
			for (int m = 0; m < LocalData.serviceCodelist.size(); m++) {
				for (int j = 0; j < serviceList.size(); j++) {
					Map<String, String> map = serviceList.get(j);
					String mm = map.get(Acc.QUERY_BUSINESSTYPECODE_RES) + ConstantGloble.BOCINVT_DATE_ADD + map.get(Acc.QUERY_BUSINESSSIGNCODE_RES);
					if (LocalData.serviceCodelist.get(m).equals(mm)) {
						serlist.add(LocalData.serviceMap.get(mm));
						break;
					}
				}
			}
		}
		// // TODO 401
		if (acc_type.equals(ConstantGloble.ACC_TYPE_BRO)) {
			// 我要收款、我要付款

			serlist.add(ConstantGloble.ACC_CRCD_TRAN);	
			serlist.add(ConstantGloble.ACC_GO_SHOUKUAN);
			serlist.add(ConstantGloble.ACC_GO_FUKUAN);
			if("1".equals(bankAccount.get(Ecard.ACC_ECARD_RES))){
				//电子卡添加电子卡转账
				serlist.remove(ConstantGloble.ACC_CRCD_TRAN);
				serlist.add(ConstantGloble.ACC_GO_ECARDTRANSFER);
			}
			
			
			ArrayList<Map<String, Object>> queryCallBackList = new ArrayList<Map<String, Object>>();
			queryCallBackList.add(bankAccount);
			GatherInitiativeData.getInstance().setQueryAcountCallBackList(queryCallBackList);
			GatherInitiativeData.getInstance().setPayAcountCallBackList(queryCallBackList);
		}
		// 我要转账
		final OnClickListener tranclick = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 我要转账
				TranDataCenter.getInstance().setAccOutInfoMap(bankAccount);
				TranDataCenter.getInstance().setCurrOutDetail(bankAccount);
				Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), TransferManagerActivity1.class);
				TranDataCenter.getInstance().setModuleType(ConstantGloble.ACC_MANAGE_TYPE);
				intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,ConstantGloble.ACC_TO_TRAN);
				BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
				BaseDroidApp.getInstanse().getCurrentAct().finish();
			}
		};
		
		// 电子卡转账
				final OnClickListener ecardtransferClick = new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						// 我要转账
						TranDataCenter.getInstance().setAccOutInfoMap(bankAccount);
						TranDataCenter.getInstance().setModuleType(ConstantGloble.ACC_MANAGE_TYPE);
						Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), TransferEcardActivity.class);
						intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,ConstantGloble.ACC_TO_TRAN_ECARD);
						BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
						BaseDroidApp.getInstanse().getCurrentAct().finish();
					}
				};
		/** 更多点击事件 */
		final OnClickListener btnMoreNullBocinvtClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Button tv = (Button) v;
				String text = tv.getText().toString();
				moreClick(text, v);
				if (text.equals(ConstantGloble.ACC_CRCD_TRAN)) {
					// 长城电子借记卡-我要转账
					tranclick.onClick(v);
				}
				
				if(text.equals(ConstantGloble.ACC_GO_ECARDTRANSFER)){
					// 电子卡转账
					ecardtransferClick.onClick(v);
				}
			}
		};
		if (serlist == null || serlist.size() == 0) {
			btn_one.setVisibility(View.GONE);
			btn_many.setVisibility(View.GONE);
		} else if (serlist.size() == 1) {
			btn_one.setVisibility(View.VISIBLE);
			btn_many.setVisibility(View.GONE);
			btn_one.setText(serlist.get(0));
			btn_one.setOnClickListener(btnMoreNullBocinvtClick);
		} else if (serlist.size() == 2) {
			btn_one.setVisibility(View.VISIBLE);
			btn_many.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_many
					.getLayoutParams();
			btn_one.setLayoutParams(param);
			btn_one.setText(serlist.get(0));
			btn_one.setOnClickListener(btnMoreNullBocinvtClick);
			btn_many.setText(serlist.get(1));
			btn_many.setOnClickListener(btnMoreNullBocinvtClick);
		} else {
			btn_one.setVisibility(View.VISIBLE);
			btn_many.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_many
					.getLayoutParams();
			btn_one.setLayoutParams(param);
			btn_one.setText(serlist.get(0));
			btn_one.setOnClickListener(btnMoreNullBocinvtClick);
			btn_many.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.more));
			String[] service = new String[serlist.size() - 1];
			for (int k = 0; k < serlist.size() - 1; k++) {
				service[k] = serlist.get(k + 1);
			}
			PopupWindowUtils.getInstance().setshowMoreChooseUpListener(BaseDroidApp.getInstanse().getCurrentAct(), btn_many, service, btnMoreNullBocinvtClick);
		}

		/**工资卡账户查询*/
		LinearLayout payroll_query = (LinearLayout)contentView
				.findViewById(R.id.acc_payroll_query);
//		LogGloble.i("payroll_query==getIsPayrollAccount", String.valueOf(AccDataCenter.getInstance().getIsPayrollAccount()));
		LogGloble.i("payroll_query==Dialog", String.valueOf(StringUtil.isNullOrEmpty(AccDataCenter.getInstance().getIsPayrollAccount())));
		if(AccDataCenter.getInstance().getIsPayrollAccount() != null && AccDataCenter.getInstance().getIsPayrollAccount()){

			payroll_query.setVisibility(View.VISIBLE);
			payroll_query.setOnClickListener(payrollQueryClick);
		}	
		return contentView;
	}

	/**
	 * 电子现金账户详情弹出框
	 * 
	 * @param financeIcAccount
	 *            账户类型、别名、账号
	 * @param financeIcAccountDetail
	 *            账户余额信息
	 * @param signNum
	 *            签约账户账号
	 * @param exitAccDetailClick
	 *            关闭详情监听事件
	 * @param forPaymentClick
	 *            用以收款监听事件
	 * @param forThePaymentClick
	 *            用以付款监听事件
	 * @param creditCardPaymentClick
	 *            信用卡还款监听事件
	 * @param creditCardReimbursementClick
	 *            信用卡购汇还款监听事件
	 * @param creatIcSignClick
	 *            新建签约监听事件
	 * @param deleteIcSignClick
	 *            删除签约监听事件
	 * @param updatenicknameClick
	 *            更新账号别名事件
	 * @return
	 */
	public View initFinanceIcAccountMessageDialogView(
			boolean ishavecurrencyTwo, String signnum,
			final Map<String, Object> financeIcAccount,
			final Map<String, String> financeIcAccountDetail,
			View.OnClickListener exitAccDetailClick,
			final View.OnClickListener creatIcSignClick,
			final View.OnClickListener deleteIcSignClick,
			final OnClickListener updatenicknameClick,
			List<Map<String, String>> serviceList, OnClickListener crcdPayment, final OnClickListener payrollQueryClick) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.acc_financeic_detail, null);
		final FrameLayout fl_acc_nickname = (FrameLayout) contentView.findViewById(R.id.fl_nickname);
		final LinearLayout ll_nickname = (LinearLayout) contentView.findViewById(R.id.ll_nickname);
		Button btn_updatenickname = (Button) contentView.findViewById(R.id.btn_update_nickname);
		/** 借记IC卡详情 */
		LinearLayout acc_debibtIc = (LinearLayout) contentView.findViewById(R.id.ll_acc_debibtIc);
		/** 长城信用IC卡详情 */
		LinearLayout acc_Ic2 = (LinearLayout) contentView.findViewById(R.id.ll_acc_Ic2);
		/** 中银系列信用卡IC卡详情 */
		LinearLayout acc_Ic3 = (LinearLayout) contentView.findViewById(R.id.ll_acc_Ic3);
		/** 纯IC卡详情 */
		LinearLayout acc_Ic4 = (LinearLayout) contentView.findViewById(R.id.ll_acc_Ic4);
		/** 账户类型 */
		TextView tv_acc_accounttype = (TextView) contentView.findViewById(R.id.acc_accounttype_value);
		final String accounttype = (String) financeIcAccount.get(Acc.ACC_ACCOUNTTYPE_RES);
		tv_acc_accounttype.setText(StringUtil.isNull(accounttype) ? ConstantGloble.BOCINVT_DATE_ADD : LocalData.AccountType.get(accounttype.trim()));
		/** 账户别名 */
		final TextView tv_acc_accountnickname = (TextView) contentView.findViewById(R.id.acc_accountnickname_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(BaseDroidApp.getInstanse().getCurrentAct(), tv_acc_accountnickname);
		String nickname = (String) financeIcAccount.get(Acc.ACC_NICKNAME_RES);
		tv_acc_accountnickname.setText(StringUtil.isNull(nickname) ? ConstantGloble.BOCINVT_DATE_ADD : nickname);
		final EditText et_acc_accountnickname = (EditText) contentView.findViewById(R.id.et_acc_nickname);
		EditTextUtils.setLengthMatcher(BaseDroidApp.getInstanse().getCurrentAct(), et_acc_accountnickname, 20);
		/** 电子现金账户状态 */
		TextView acc_account_accountState = (TextView) contentView.findViewById(R.id.acc_account_accountState_value);
		String acc_accountState = (String) financeIcAccountDetail.get(Acc.FINANCEICDETAIL_ACCOUNTSTATE_RES);
		acc_account_accountState.setText(
				StringUtil.isNull(acc_accountState) ? ConstantGloble.BOCINVT_DATE_ADD : AccBaseActivity.accountState.get(acc_accountState));
		/** 账 号 */
		TextView tv_acc_accountnumber = (TextView) contentView.findViewById(R.id.acc_account_actnum_value);
		String accountNumber = (String) financeIcAccount.get(Acc.ACC_ACCOUNTNUMBER_RES);
		tv_acc_accountnumber.setText(StringUtil.isNull(accountNumber) ? ConstantGloble.BOCINVT_DATE_ADD : StringUtil.getForSixForString(accountNumber));
		/** 币种 */
		TextView acc_account_currencycode = (TextView) contentView.findViewById(R.id.acc_account_currencycode_value);
		final String currency = (String) financeIcAccountDetail.get(Acc.FINANCEICDETAIL_CURRENCY_RES);
		acc_account_currencycode.setText(StringUtil.isNull(currency) ? ConstantGloble.BOCINVT_DATE_ADD : LocalData.Currency.get(currency));
		/** 电子现金卡片余额上限 */
		TextView acc_financeic_eCashUpperLimit = (TextView) contentView.findViewById(R.id.acc_financeic_eCashUpperLimit_value);
		String upperLimit = (String) financeIcAccountDetail.get(Acc.FINANCEICDETAIL_ECASHUPPERLIMIT_RES);
		acc_financeic_eCashUpperLimit.setText(StringUtil.parseStringPattern(upperLimit, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(BaseDroidApp.getInstanse().getCurrentAct(), acc_financeic_eCashUpperLimit);
		/** 电子现金卡片单笔交易限额 */
		TextView acc_financeic_singleLimit = (TextView) contentView.findViewById(R.id.acc_financeic_singleLimit_value);
		String singleLimit = (String) financeIcAccountDetail.get(Acc.FINANCEICDETAIL_SINGLELIMIT_RES);
		acc_financeic_singleLimit.setText(StringUtil.parseStringPattern(singleLimit, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(BaseDroidApp.getInstanse().getCurrentAct(), acc_financeic_singleLimit);
		/** 余额 */
		TextView acc_financeic_totalBalance = (TextView) contentView.findViewById(R.id.acc_financeic_totalBalance_value);
		final String totalBalance = (String) financeIcAccountDetail.get(Acc.FINANCEICDETAIL_SUPPLYBALANCE_RES);
		acc_financeic_totalBalance.setText(StringUtil.parseStringPattern(totalBalance, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(BaseDroidApp.getInstanse().getCurrentAct(), acc_financeic_totalBalance);
		ImageView img_exit = (ImageView) contentView.findViewById(R.id.img_exit_accdetail);
		final ImageView img_update_accnickname = (ImageView) contentView.findViewById(R.id.img_acc_update_nickname);

		// 按钮初始化
		/** 信用卡还款1 */
		Button btn_credit_card_payment = (Button) contentView.findViewById(R.id.btn_credit_card_payment);
		btn_credit_card_payment.setOnClickListener(crcdPayment);
		/** 信用卡还款2 */
		Button btn_creditcard_payment = (Button) contentView.findViewById(R.id.btn_creditcard_payment);
		btn_creditcard_payment.setOnClickListener(crcdPayment);
		/** 信用卡购汇还款 ——电子现金账户中只有人民币 */
		Button btn_creditcard_reimbursement = (Button) contentView.findViewById(R.id.btn_creditcard_reimbursement);
		btn_creditcard_reimbursement.setVisibility(View.GONE);
		/** 新建签约 */
		Button btn_creat_icsign = (Button) contentView.findViewById(R.id.btn_creat_icsign);
		if (creatIcSignClick != null) {
			btn_creat_icsign.setOnClickListener(creatIcSignClick);
		}

		/** 删除签约 */
		final Button btn_delete_icsign = (Button) contentView.findViewById(R.id.btn_delete_icsign);
		if (deleteIcSignClick != null) {
			btn_delete_icsign.setOnClickListener(deleteIcSignClick);
		}
		final OnClickListener btnMoreNullBocinvtClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Button tv = (Button) v;
				String text = tv.getText().toString();
				moreClick(text, v);
				if (text.equals(BaseDroidApp.getInstanse().getCurrentAct()
						.getString(R.string.acc_delete_icsign))) {
					// 删除签约
					if (deleteIcSignClick != null) {
						btn_delete_icsign.setOnClickListener(creatIcSignClick);
					}
				}
			}
		};
		// 根据账户类型，显示不同按钮
		if (accounttype.equals(LocalData.cardcodeList.get(0))) {
			// 借记IC
			acc_debibtIc.setVisibility(View.GONE);
			acc_Ic2.setVisibility(View.GONE);
			acc_Ic3.setVisibility(View.GONE);
			acc_Ic4.setVisibility(View.GONE);
		} else if (accounttype.equals(LocalData.cardcodeList.get(2))) {
			// 长城信用IC卡
			acc_debibtIc.setVisibility(View.GONE);
			acc_Ic2.setVisibility(View.VISIBLE);
			acc_Ic3.setVisibility(View.GONE);
			acc_Ic4.setVisibility(View.GONE);
		} else if (accounttype.equals(LocalData.cardcodeList.get(1))) {
			// 中银系列信用卡IC卡——只有人民币
			acc_debibtIc.setVisibility(View.GONE);
			acc_Ic2.setVisibility(View.GONE);
			acc_Ic3.setVisibility(View.VISIBLE);
			acc_Ic4.setVisibility(View.GONE);
			btn_creditcard_reimbursement.setVisibility(View.GONE);
		} else if (accounttype.equals(LocalData.cardcodeList.get(3))) {
			// 纯IC卡详情
			acc_debibtIc.setVisibility(View.GONE);
			acc_Ic2.setVisibility(View.GONE);
			acc_Ic3.setVisibility(View.GONE);
			if (creatIcSignClick != null) {
				acc_Ic4.setVisibility(View.VISIBLE);
				if (StringUtil.isNull(signnum)) {
					btn_delete_icsign.setEnabled(false);
					btn_delete_icsign.setVisibility(View.GONE);
				} else {
					if (signnum.toString().trim().length() > 0) {
						// 有签约账户
						btn_delete_icsign.setClickable(true);
						btn_delete_icsign.setEnabled(true);
						btn_delete_icsign.setVisibility(View.VISIBLE);
						LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_delete_icsign.getLayoutParams();
						btn_creat_icsign.setLayoutParams(param);
					} else {
						btn_delete_icsign.setEnabled(false);
						btn_delete_icsign.setVisibility(View.GONE);
					}
				}

			} else {
				acc_Ic4.setVisibility(View.VISIBLE);
				List<String> serlist = new ArrayList<String>();
				serlist.add(ConstantGloble.ACC_GO_TRANSFERDETAIL);
				if (serviceList == null || serviceList.size() == 0) {

				} else {
					for (int m = 0; m < LocalData.serviceCodelist.size(); m++) {
						for (int j = 0; j < serviceList.size(); j++) {
							Map<String, String> map = serviceList.get(j);
							String mm = map.get(Acc.QUERY_BUSINESSTYPECODE_RES) + ConstantGloble.BOCINVT_DATE_ADD + map.get(Acc.QUERY_BUSINESSSIGNCODE_RES);
							if (LocalData.serviceCodelist.get(m).equals(mm)) {
								serlist.add(LocalData.serviceMap.get(mm));
								break;
							}
						}
					}
				}
				if (serlist == null || serlist.size() == 0) {
					btn_creat_icsign.setVisibility(View.GONE);
					btn_delete_icsign.setVisibility(View.GONE);
				} else if (serlist.size() == 1) {
					btn_creat_icsign.setVisibility(View.VISIBLE);
					btn_delete_icsign.setVisibility(View.GONE);
					btn_creat_icsign.setText(serlist.get(0));
					btn_creat_icsign
							.setOnClickListener(btnMoreNullBocinvtClick);
				} else if (serlist.size() == 2) {
					btn_creat_icsign.setVisibility(View.VISIBLE);
					btn_delete_icsign.setVisibility(View.VISIBLE);
					LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_delete_icsign.getLayoutParams();
					btn_creat_icsign.setLayoutParams(param);
					btn_creat_icsign.setText(serlist.get(0));
					btn_creat_icsign.setOnClickListener(btnMoreNullBocinvtClick);
					btn_delete_icsign.setText(serlist.get(1));
					btn_delete_icsign.setOnClickListener(btnMoreNullBocinvtClick);
				} else {
					btn_creat_icsign.setVisibility(View.VISIBLE);
					btn_delete_icsign.setVisibility(View.VISIBLE);
					LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_delete_icsign.getLayoutParams();
					btn_creat_icsign.setLayoutParams(param);
					btn_creat_icsign.setText(serlist.get(0));
					btn_creat_icsign.setOnClickListener(btnMoreNullBocinvtClick);
					btn_delete_icsign.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.more));
					String[] service = new String[serlist.size() - 1];
					for (int k = 0; k < serlist.size() - 1; k++) {
						service[k] = serlist.get(k + 1);
					}
					PopupWindowUtils.getInstance().setshowMoreChooseUpListener(BaseDroidApp.getInstanse().getCurrentAct(), btn_delete_icsign, service, btnMoreNullBocinvtClick);
				}
			}

		}
		/**工资卡账户查询*/
		LinearLayout payroll_query = (LinearLayout)contentView.findViewById(R.id.acc_payroll_query);
//		LogGloble.i("payroll_query==getIsPayrollAccount", String.valueOf(AccDataCenter.getInstance().getIsPayrollAccount()));
		LogGloble.i("payroll_query==Dialog", String.valueOf(StringUtil.isNullOrEmpty(AccDataCenter.getInstance().getIsPayrollAccount())));
		if(AccDataCenter.getInstance().getIsPayrollAccount() != null && AccDataCenter.getInstance().getIsPayrollAccount()){
			payroll_query.setVisibility(View.VISIBLE);
			payroll_query.setOnClickListener(payrollQueryClick);
		}	
		// 退出账户详情点击事件
		img_exit.setOnClickListener(exitAccDetailClick);
		// 修改账户别名点击事件
		img_update_accnickname.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				fl_acc_nickname.setVisibility(View.VISIBLE);
				ll_nickname.setVisibility(View.GONE);
				BaseActivity activity = (BaseActivity) BaseDroidApp.getInstanse()
						.getCurrentAct();
				if(activity != null)
					activity.upSoftInput();
				et_acc_accountnickname.setWidth(LayoutParams.FILL_PARENT);
				et_acc_accountnickname.setText(tv_acc_accountnickname.getText().toString().trim());
				et_acc_accountnickname.setSelection(et_acc_accountnickname.length());
			}
		});
		btn_updatenickname.setOnClickListener(updatenicknameClick);

		return contentView;
	}

	/**
	 * 新建签约选择账户弹出框
	 * 
	 * @param financeic_accountnum
	 *            电子现金账户账号
	 * @param accountList
	 *            账户列表
	 * @param exitAccDetailClick
	 *            退出监听事件
	 * @param btnNextClick
	 *            确定监听事件
	 * @param btnLastClick
	 *            上一步监听事件
	 * @return
	 */
	int selectposition = -1;

	public View initFinanceICCreatSignView(String financeic_accountnum,
			final List<Map<String, String>> accountList,
			View.OnClickListener exitAccDetailClick,
			final OnItemClickListener btnNextClick,
			final OnItemClickListener btnLastClick) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.acc_financeic_icsign_list, null);

		ImageView img_exit = (ImageView) contentView.findViewById(R.id.img_exit_accdetail);
		// 退出弹出框监听事件
		img_exit.setOnClickListener(exitAccDetailClick);
		/** 电子现金账户账号 */
		TextView financeic_actnumber = (TextView) contentView.findViewById(R.id.tv_financeic_actnum_value);
		financeic_actnumber.setText(StringUtil.getForSixForString(financeic_accountnum));
		/** 选择签约账户列表 */
		ListView lv_choose = (ListView) contentView.findViewById(R.id.lv_icsign_choose);
		final FinanceIcSignChooseAdapter adapter = new FinanceIcSignChooseAdapter(currentContent, accountList);
		lv_choose.setAdapter(adapter);
		lv_choose.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (selectposition == position) {
					return;
				} else {
					selectposition = position;
					adapter.setSelectedPosition(position);
				}
			}
		});
		Button btnNext = (Button) contentView.findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (selectposition == -1) {

				} else {
					AccDataCenter.getInstance().setIcsignmap(accountList.get(adapter.getSelectedPosition()));
				}
				btnNextClick.onItemClick(null, v, selectposition, selectposition);
			}
		});
		Button btnLast = (Button) contentView.findViewById(R.id.btnLast);
		btnLast.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 选择签约账户弹窗的上一步点击事件
				btnLastClick.onItemClick(null, v, 0, 0);

			}
		});
		return contentView;
	}

	/**
	 * * 签约账户确认页面
	 * 
	 * @param param
	 *            布局参数
	 * @param activity
	 *            Activity
	 * @param signnum
	 *            签约账号
	 * @param financeic_accountnum
	 *            电子现金账户账号
	 * @param icsignmap
	 *            选择的要签约的账户
	 * @param accountList
	 *            账户列表
	 * @param exitAccDetailClick
	 *            退出监听事件
	 * @param btnNextClick
	 *            确定监听事件
	 * @param btnLastClick
	 *            上一步监听事件
	 * @param isOtp
	 *            是否需要动态口令
	 * @param isSmc
	 *            是否需要手机验证码
	 * @param randomNumber
	 *            随机数
	 * @return
	 */
	public View initFinanceIcSignConfirmDialog(LinearLayout.LayoutParams param,
			Activity activity, String signnum, String financeic_accountnum,
			Map<String, String> icsignmap,
			final List<Map<String, String>> accountList,
			View.OnClickListener exitAccDetailClick,
			final View.OnClickListener btnNextClick,
			final OnItemClickListener btnLastClick, boolean isOtp,
			boolean isSmc, String randomNumber,
			OnClickListener sendSmcListener, Map<String, Object> securityMap) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.acc_financeic_icsign_confirm, null);
		ImageView img_exit = (ImageView) contentView.findViewById(R.id.img_exit_accdetail);
		TextView tv_icsign_title = (TextView) contentView.findViewById(R.id.tv_icsign_title);
				
		/**中银E盾*/
		final UsbKeyText usbKeyText;
		BaseActivity baseActivity;
		baseActivity = (BaseActivity) currentContent;
		/** 中银E盾初始化 */
		usbKeyText = (UsbKeyText) contentView.findViewById(R.id.sip_usbkey);
		String commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText.Init(commConversationId, randomNumber, securityMap, baseActivity);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		// 动态口令

		final SipBox sipBoxActiveCode = (SipBox) contentView.findViewById(R.id.sipbox_active);
		if(isOtp){
		// 动态口令
		LinearLayout ll_active_code = (LinearLayout) contentView.findViewById(R.id.ll_active_code);
		ll_active_code.setVisibility(View.VISIBLE);
		sipBoxActiveCode.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
		sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
		sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
		sipBoxActiveCode.setId(10002);
		sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
		sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
		sipBoxActiveCode.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
		sipBoxActiveCode.setSingleLine(true);
		BaseActivity a = (BaseActivity) BaseDroidApp.getInstanse()
				.getCurrentAct();
		if(a != null)
			sipBoxActiveCode.setSipDelegator(a);
		sipBoxActiveCode.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
		// 加密控件设置随机数
		sipBoxActiveCode.setRandomKey_S(randomNumber);

		}
		// 手机交易码
		final SipBox sipBoxSmc = (SipBox) contentView
				.findViewById(R.id.sipbox_smc);

		
		if(isSmc){
		// 手机交易码
		LinearLayout ll_smc = (LinearLayout) contentView.findViewById(R.id.ll_smc);
		ll_smc.setVisibility(View.VISIBLE);
		sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
		sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
		sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
		sipBoxSmc.setId(10002);
		sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
		sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
		sipBoxSmc.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
		sipBoxSmc.setSingleLine(true);
		BaseActivity a = (BaseActivity) BaseDroidApp.getInstanse()
				.getCurrentAct();
		if(a != null)
			sipBoxSmc.setSipDelegator(a);
		sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
		sipBoxSmc.setRandomKey_S(randomNumber);

		}
		
		
		Button smsBtn = (Button) contentView.findViewById(R.id.smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn, sendSmcListener);
//		
//		if (isSmc) {
//			ll_smc.setVisibility(View.VISIBLE);
//		}
//		if (isOtp) {
//			ll_active_code.setVisibility(View.VISIBLE);
//		}
		/** 电子现金账户账号 */
		TextView financeic_actnumber = (TextView) contentView.findViewById(R.id.tv_financeic_actnum_value);
		financeic_actnumber.setText(StringUtil.getForSixForString(financeic_accountnum));
		RelativeLayout ll_bankaccount_old = (RelativeLayout) contentView.findViewById(R.id.ll_bankaccount_old);

		RelativeLayout ll_bankaccount_new = (RelativeLayout) contentView.findViewById(R.id.ll_bankaccount_new);
		/** 账户类型old */
		TextView tv_acc_type_value_old = (TextView) contentView.findViewById(R.id.acc_type_value_old);
		/** 账户别名old */
		TextView tv_acc_account_nickname_old = (TextView) contentView.findViewById(R.id.acc_account_nickname_old);
		/** 账户账号 old */
		TextView tv_acc_account_num_old = (TextView) contentView.findViewById(R.id.acc_account_num_old);
		/** 账户类型new */
		TextView tv_acc_type_value_new = (TextView) contentView.findViewById(R.id.acc_type_value_new);
		/** 账户别名 new */
		TextView tv_acc_account_nickname_new = (TextView) contentView.findViewById(R.id.acc_account_nickname_new);
		/** 账户账号new */
		TextView tv_acc_account_num_new = (TextView) contentView.findViewById(R.id.acc_account_num_new);
		// 退出弹出框监听事件
		img_exit.setOnClickListener(exitAccDetailClick);
		if (StringUtil.isNull(signnum)) {
			// 没有签约账户
			ImageView img_flag_new = (ImageView) contentView.findViewById(R.id.img_flag_new);
			img_flag_new.setVisibility(View.INVISIBLE);
			tv_icsign_title.setText(currentContent.getString(R.string.acc_choose_icsign_confirm_title2));
			ll_bankaccount_old.setVisibility(View.INVISIBLE);
			ll_bankaccount_new.setVisibility(View.VISIBLE);
		} else {
			if (signnum.trim().length() > 0) {
				// 有签约账户
				ImageView img_flag_new = (ImageView) contentView.findViewById(R.id.img_flag_new);
				ImageView img_flag_old = (ImageView) contentView.findViewById(R.id.img_flag_old);
				img_flag_new.setVisibility(View.VISIBLE);
				img_flag_old.setVisibility(View.VISIBLE);
				ll_bankaccount_old.setVisibility(View.VISIBLE);
				tv_icsign_title.setText(currentContent.getString(R.string.acc_choose_icsign_confirm_title));
				tv_acc_account_num_old.setText(StringUtil.getForSixForString(signnum));
				for (int i = 0; i < accountList.size(); i++) {
					if (signnum.equalsIgnoreCase(accountList.get(i).get(Acc.ACC_ACCOUNTNUMBER_RES))) {
						String acc_type_old = accountList.get(i).get(Acc.ACC_ACCOUNTTYPE_RES);
						tv_acc_type_value_old.setText(LocalData.AccountType.get(acc_type_old.trim()));
						tv_acc_account_nickname_old.setText(accountList.get(i).get(Acc.ACC_NICKNAME_RES));
						break;
					}
				}
				ll_bankaccount_new.setVisibility(View.VISIBLE);
			} else {
				// 没有签约账户
				ImageView img_flag_new = (ImageView) contentView.findViewById(R.id.img_flag_new);
				img_flag_new.setVisibility(View.INVISIBLE);
				tv_icsign_title.setText(currentContent.getString(R.string.acc_choose_icsign_confirm_title2));
				ll_bankaccount_old.setVisibility(View.INVISIBLE);
				ll_bankaccount_new.setVisibility(View.VISIBLE);
			}
		}
		String acc_type_new = icsignmap.get(Acc.ACC_ACCOUNTTYPE_RES);
		tv_acc_type_value_new.setText(LocalData.AccountType.get(acc_type_new.trim()));
		tv_acc_account_nickname_new.setText(icsignmap.get(Acc.ACC_NICKNAME_RES));
		tv_acc_account_num_new.setText(StringUtil.getForSixForString(icsignmap.get(Acc.ACC_ACCOUNTNUMBER_RES)));
		Button btnNext = (Button) contentView.findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				// 动态口令
//				RegexpBean sipRegexpBean = new RegexpBean(BaseDroidApp
//						.getInstanse().getCurrentAct()
//						.getString(R.string.active_code_regex),
//						sipBoxActiveCode.getText().toString(),
//						ConstantGloble.SIPOTPPSW);
//				RegexpBean sipSmcpBean = new RegexpBean(BaseDroidApp
//						.getInstanse().getCurrentAct()
//						.getString(R.string.acc_smc_regex), sipBoxSmc.getText()
//						.toString(), ConstantGloble.SIPSMCPSW);
//				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//
//				if (isSmc) {
//					lists.add(sipSmcpBean);
//				}
//				if (isOtp) {
//					lists.add(sipRegexpBean);
//				}
				BaseHttpEngine.showProgressDialog();
				/** 音频Key安全工具认证 */
				usbKeyText.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {
					
							@Override
							public void SuccessCallBack(String result, int errorCode) {
								// TODO Auto-generated method stub

								BaseHttpEngine.showProgressDialog();
//								requestPSNGetTokenId(commConversationId);

							}
						});
				Map<String, Object> paramsmap = new HashMap<String, Object>();
				usbKeyText.InitUsbKeyResult(paramsmap);
				AccDataCenter.getInstance().setUsbparams(paramsmap);
// 				if (RegexpUtils.regexpDate(lists)) {// 校验通过

//					if (isSmc) {
//						try {
//							smcStr = sipBoxSmc.getValue().getEncryptPassword();
//							smc_password_RC = sipBoxSmc.getValue()
//									.getEncryptRandomNum();
//						} catch (CodeException e) {
//							LogGloble.exceptionPrint(e);
//						}
//					}
//					if (isOtp) {
//						try {
//							otpStr = sipBoxActiveCode.getValue()
//									.getEncryptPassword();
//							otp_password_RC = sipBoxActiveCode.getValue()
//									.getEncryptRandomNum();
//						} catch (CodeException e) {
//							LogGloble.exceptionPrint(e);
//						}
//					}
//					AccDataCenter.getInstance().setOtp(otpStr);
//					AccDataCenter.getInstance().setSmc(smcStr);
//					AccDataCenter.getInstance().setOtp_password_RC(
//							otp_password_RC);
//					AccDataCenter.getInstance().setSmc_password_RC(
//							smc_password_RC);
					btnNextClick.onClick(v);
				//}

			}
		});
		Button btnLast = (Button) contentView.findViewById(R.id.btnLast);

		btnLast.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 新建签约弹窗确认界面上一步点击事件
				btnLastClick.onItemClick(null, v, 1, 1);
			}
		});
		return contentView;
	}

	/**
	 * 签约账户成功页面
	 * 
	 * @param financeic_accountnum
	 *            电子现金账户账号
	 * @param icsignmap
	 *            选择的要签约的账户
	 * @param exitAccDetailClick
	 *            退出监听事件
	 * @param btnNextClick
	 *            确定监听事件
	 * @return
	 */
	public View initFinanceIcSignSuccessDialog(String financeic_accountnum,
			Map<String, String> icsignmap,
			View.OnClickListener exitAccDetailClick,
			final View.OnClickListener btnNextClick) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.acc_financeic_icsign_success, null);
		ImageView img_exit = (ImageView) contentView.findViewById(R.id.img_exit_accdetail);
		/** 电子现金账户账号 */
		TextView financeic_actnumber = (TextView) contentView.findViewById(R.id.tv_financeic_actnum_value);
		financeic_actnumber.setText(StringUtil.getForSixForString(financeic_accountnum));
		/** 账户类型old */
		TextView tv_acc_type_value_old = (TextView) contentView.findViewById(R.id.acc_type_value_old);
		/** 账户别名old */
		TextView tv_acc_account_nickname_old = (TextView) contentView.findViewById(R.id.acc_account_nickname_old);
		/** 账户账号 old */
		TextView tv_acc_account_num_old = (TextView) contentView.findViewById(R.id.acc_account_num_old);
		// 退出弹出框监听事件
		img_exit.setOnClickListener(exitAccDetailClick);
		String acc_type_old = icsignmap.get(Acc.ACC_ACCOUNTTYPE_RES);
		tv_acc_type_value_old.setText(LocalData.AccountType.get(acc_type_old.trim()));
		tv_acc_account_nickname_old.setText(icsignmap.get(Acc.ACC_NICKNAME_RES));
		tv_acc_account_num_old.setText(StringUtil.getForSixForString(icsignmap.get(Acc.ACC_ACCOUNTNUMBER_RES)));
		Button btnNext = (Button) contentView.findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				btnNextClick.onClick(v);
			}
		});
		return contentView;
	}

	/**
	 * 存款账户详情框（定期一本通）
	 * 
	 * @param saveType
	 *            存储类型
	 * @param onBankbookListener
	 *            存折下拉框监听
	 * @param onBanksheetListener
	 *            存单号下拉监听
	 * @param onCreateNoticeListener
	 *            建立通知下拉监听
	 * @param onCheckoutListener
	 *            支取按钮监听
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public View initMySaveDetailDialogView(
			final View.OnClickListener onCreateNoticeListener,
			final View.OnClickListener onCheckoutListener,
			final View.OnClickListener onContinueSaveListener,
			final Map<String, Object> bankAccount,
			final View.OnClickListener onModifyNickNameListener,
			View.OnClickListener exitDetailClick,
			final List<Map<String, String>> serviceList, final String status, final OnClickListener payrollQueryClick) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.acc_my_save_detail, null);
		DeptDataCenter.getInstance().setAccountContent(bankAccount);
		if (bankAccount == null) {
			return contentView;
		}

		// 账户详情列表
		List<Map<String, Object>> accountDetaiList = (List<Map<String, Object>>) bankAccount.get(ConstantGloble.ACC_DETAILIST);

		ImageButton closeIb = (ImageButton) contentView.findViewById(R.id.dept_close_ib);
		closeIb.setOnClickListener(exitDetailClick);

		// TextView 账户类型
		TextView accountTypeTv = (TextView) contentView.findViewById(R.id.dept_accounttype_tv);
		String strAccountType = (String) bankAccount.get(Comm.ACCOUNT_TYPE);
		accountTypeTv.setText(LocalData.AccountType.get(strAccountType));
		// Layout账户别名
		final LinearLayout nickNameLayout = (LinearLayout) contentView
				.findViewById(R.id.dept_nickname_layout);
		// Layout修改账户别名
		final FrameLayout modifyNickNameLayout = (FrameLayout) contentView.findViewById(R.id.fl_nickname);
		// TextView 账户别名
		final TextView accountNickName = (TextView) contentView.findViewById(R.id.dept_nickname_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(BaseDroidApp.getInstanse().getCurrentAct(), accountNickName);
		// EditText 账户别名
		final EditText nickNameEt = (EditText) contentView.findViewById(R.id.et_acc_nickname);
		EditTextUtils.setLengthMatcher(BaseDroidApp.getInstanse().getCurrentAct(), nickNameEt, 20);
		Button updateNickNameBtn = (Button) contentView.findViewById(R.id.btn_update_nickname);

		accountNickName.setText((String) bankAccount.get(Comm.NICKNAME));
		ImageView modifyNickNameIv = (ImageView) contentView.findViewById(R.id.img_dept_update_nickname);
		modifyNickNameIv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				nickNameLayout.setVisibility(View.GONE);
				modifyNickNameLayout.setVisibility(View.VISIBLE);
				BaseActivity activity = (BaseActivity)BaseDroidApp.getInstanse().getCurrentAct();
				if(activity != null)
					activity.upSoftInput();
				nickNameEt.setText(accountNickName.getText().toString());
				nickNameEt.setSelection(nickNameEt.getText().toString().length());

			}
		});

		updateNickNameBtn.setOnClickListener(onModifyNickNameListener);

		// TextView 账号
		TextView accountNo = (TextView) contentView.findViewById(R.id.dept_accountno_tv);
		String strAccNo = (String) bankAccount.get(Comm.ACCOUNTNUMBER);
		accountNo.setText(StringUtil.getForSixForString(strAccNo));

		// 存折号Layout
		LinearLayout bankbookLayout = (LinearLayout) contentView.findViewById(R.id.dept_bankbook_layout);
		// 存单Layout
		LinearLayout banksheetLayout = (LinearLayout) contentView.findViewById(R.id.dept_banksheet_layout);
		// 开户网点Layout
		LinearLayout llAccBranchName = (LinearLayout) contentView.findViewById(R.id.ll_acc_branchName);
		// 开户网点TextView
		TextView tvAccAccountBranchName = (TextView) contentView.findViewById(R.id.acc_account_branchName);
		// 开户日期Layout
		LinearLayout opendateLayout = (LinearLayout) contentView.findViewById(R.id.dept_opendate_layout);
		// 开户日期TextView
		TextView opendateTv = (TextView) contentView.findViewById(R.id.dept_opendate_tv);

		String openDate = (String) bankAccount.get(Dept.ACCOPENDATE);
		if (!StringUtil.isNull(openDate)) {
			opendateTv.setText(openDate);
		} else {
			opendateLayout.setVisibility(View.GONE);
		}
		String branchName = (String) bankAccount.get(Dept.ACCOPENBANK);
		if (!StringUtil.isNull(branchName)) {
			tvAccAccountBranchName.setText(branchName);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(currentContent, tvAccAccountBranchName);
		} else {
			llAccBranchName.setVisibility(View.GONE);
		}
		
		// 如果是教育续存 或者 零存整取 没有存折存单 返回的accountDetaiList里面只有一个数据
		if (bankAccount.get(Comm.ACCOUNT_TYPE).equals(DeptBaseActivity.EDUCATION_SAVE1)
				|| bankAccount.get(Comm.ACCOUNT_TYPE).equals(DeptBaseActivity.ZERO_SAVE1)) {
			bankbookLayout.setVisibility(View.GONE);
			banksheetLayout.setVisibility(View.GONE);
			llAccBranchName.setVisibility(View.VISIBLE);
			opendateLayout.setVisibility(View.VISIBLE);
			Map<String, Object> content = accountDetaiList.get(0);
			DeptDataCenter.getInstance().setCurDetailContent(content);
			refreshMySaveContent(bankAccount, content, onCreateNoticeListener, onCheckoutListener, onContinueSaveListener, serviceList, status);
		} else {
			// Spinner 存折册号 根据账户列表来显示
			// 返回数据里面 存折号和存单是没有关联的 重新组装数据
			volumesAndCdnumbers = AccDataCenter.getInstance().getVolumesAndCdnumbers();

			final List<String> volumes = new ArrayList<String>();
			for (Map<String, Object> map : volumesAndCdnumbers) {
				volumes.add((String) map.get(Dept.VOLUME_NUMBER));
			}
			/**工资卡账户查询*/
			LinearLayout payroll_query = (LinearLayout)contentView.findViewById(R.id.acc_payroll_query);
			
			if(AccDataCenter.getInstance().getIsPayrollAccount() != null && AccDataCenter.getInstance().getIsPayrollAccount()){
				payroll_query.setVisibility(View.VISIBLE);
				payroll_query.setOnClickListener(payrollQueryClick);
			}	
			// 默认显示 第一个存折册号
			// Spinner 存折册号
			Spinner bankbookSpinner = (Spinner) contentView.findViewById(R.id.dept_bankbook_spinner);
			ArrayAdapter<String> bankbookAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse().getCurrentAct(), R.layout.custom_spinner_item, volumes);
			bankbookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			bankbookSpinner.setAdapter(bankbookAdapter);
			// 默认显示第一个存单号
			currentList = (List<Map<String, Object>>) volumesAndCdnumbers.get(0).get(Dept.CD_NUMBER);
			deptcurrentList = new ArrayList<Map<String, Object>>();
			List<String> cdnums = new ArrayList<String>();
			for (int i = 0; i < currentList.size(); i++) {
				String vol = (String) currentList.get(i).get(ConstantGloble.VOL);
				cdnums.add(vol);
				deptcurrentList.add(currentList.get(i));
			}
			// Spinner 存单号
			final Spinner banksheetSpinner = (Spinner) contentView.findViewById(R.id.dept_banksheet_spinner);
			banksheetAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse().getCurrentAct(), R.layout.custom_spinner_item, cdnums);
			banksheetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			banksheetSpinner.setAdapter(banksheetAdapter);
			if (StringUtil.isNullOrEmpty(cdnums)) {
				refreshMySaveContent(null, null, onCreateNoticeListener,
						onCheckoutListener, onContinueSaveListener,
						serviceList, status);
			} else {
				banksheetSpinner.setSelection(0);
			}
			
			bankbookSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					deptcurrentList = new ArrayList<Map<String, Object>>();
					currentList = (List<Map<String, Object>>) volumesAndCdnumbers.get(position).get(Dept.CD_NUMBER);
					// 从新组装一个cdnumberlist
					List<String> cdlist = new ArrayList<String>();
					for (int i = 0; i < currentList.size(); i++) {
						String vol = (String) currentList.get(i).get(ConstantGloble.VOL);
						cdlist.add(vol);
						deptcurrentList.add(currentList.get(i));
					}
					banksheetAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse().getCurrentAct(), R.layout.custom_spinner_item, cdlist);
					banksheetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					banksheetSpinner.setAdapter(banksheetAdapter);
					if (StringUtil.isNullOrEmpty(cdlist)) {
						refreshMySaveContent(null, null,
								onCreateNoticeListener,
								onCheckoutListener,
								onContinueSaveListener, serviceList,
								status);
					} else {
						}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			banksheetSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					if (!StringUtil.isNullOrEmpty(deptcurrentList)) {
						Map<String, Object> content = (Map<String, Object>) deptcurrentList.get(position).get(ConstantGloble.CONTENT);
						DeptDataCenter.getInstance().setCurDetailContent(content);
						refreshMySaveContent(bankAccount, content,
								onCreateNoticeListener,
								onCheckoutListener,
								onContinueSaveListener, serviceList,
								status);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		}

		return contentView;
	}

	/**
	 * 刷新账号详情内容
	 * 
	 * @param position
	 */
	public void refreshMySaveContent(final Map<String, Object> bankAccount,
			Map<String, Object> content,
			final View.OnClickListener onCreateNoticeListener,
			View.OnClickListener onCheckoutListener,
			View.OnClickListener onContinueSaveListener,
			List<Map<String, String>> serviceList, final String status) {
		RelativeLayout linearLayout = (RelativeLayout) contentView
				.findViewById(R.id.dept_my_detail_content);
		if (StringUtil.isNullOrEmpty(bankAccount)) {
			linearLayout.removeAllViews();
			BaseDroidApp.getInstanse().showInfoMessageDialog(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.no_dept_status));
			return;
		}
		RelativeLayout contentLayout = (RelativeLayout) LayoutInflater.from(BaseDroidApp.getInstanse().getCurrentAct()).inflate(R.layout.acc_my_save_detail_content, null);
		// 月存金额layout
		LinearLayout monthSaveLayout = (LinearLayout) contentLayout.findViewById(R.id.dept_month_save_layout);
		// 起期layout
		LinearLayout startAccrualLayout = (LinearLayout) contentLayout.findViewById(R.id.layout_start_accrual_day);
		// 当前余额layout
		LinearLayout currentMontyLayout = (LinearLayout) contentLayout.findViewById(R.id.dept_current_money_layout);
		LinearLayout ll_status = (LinearLayout) contentLayout.findViewById(R.id.ll_status);
		LinearLayout sumDayLayout = (LinearLayout) contentLayout.findViewById(R.id.layout_sum_day);
		// 存单金额textview
		LinearLayout moneyAmountLayout = (LinearLayout) contentLayout.findViewById(R.id.layout_money_amount);
		// 到期日layout
		LinearLayout endDayLayout = (LinearLayout) contentLayout.findViewById(R.id.layout_end_day);
		TextView tv_status = (TextView) contentLayout.findViewById(R.id.dept_state);
		LinearLayout ll_mode = (LinearLayout) contentLayout.findViewById(R.id.ll_mode);
		LinearLayout ll_type = (LinearLayout) contentLayout.findViewById(R.id.ll_type);
		// 月存金额textview
		TextView monthSaveTv = (TextView) contentLayout.findViewById(R.id.dept_month_save_tv);
		String strMonthSave = (String) content.get(Dept.MONTH_BALANCE);
		if (StringUtil.isNullOrEmpty(strMonthSave)) {
			strMonthSave = "0.0";
		}
		// 当前余额textview
		TextView currentMoneyTv = (TextView) contentLayout.findViewById(R.id.dept_current_money_tv);

		TextView currencyTv = (TextView) contentLayout.findViewById(R.id.dept_currency_tv);
		TextView cashRemitTv = (TextView) contentLayout.findViewById(R.id.dept_cashremit_tv);
		TextView convertTypeTv = (TextView) contentLayout.findViewById(R.id.dept_convert_type_tv);
		TextView cdPeriodTv = (TextView) contentLayout.findViewById(R.id.dept_cd_period_tv);
		TextView cdInterestStartsDateTv = (TextView) contentLayout.findViewById(R.id.dept_cd_interest_startsdate_tv);
		TextView cdInterestEndDateTv = (TextView) contentLayout.findViewById(R.id.dept_cd_interest_enddate_tv);
		TextView cdCloseDateTv = (TextView) contentLayout.findViewById(R.id.dept_cd_close_date_tv);
		TextView saveTypeTv = (TextView) contentLayout.findViewById(R.id.dept_save_type_tv);
		TextView interestRateTv = (TextView) contentLayout.findViewById(R.id.dept_interest_rate_tv);
		TextView moneyAmountTv = (TextView) contentLayout.findViewById(R.id.dept_cd_money_amount_tv);
		// 支取按钮
		Button checkoutBtn = (Button) contentView.findViewById(R.id.dept_checkout_btn);
		// 建立通知按钮
		Button createNoticeBtn = (Button) contentView.findViewById(R.id.dept_create_notice_btn);
		// 隐藏按钮
		Button deptgoneBtn = (Button) contentView.findViewById(R.id.dept_btn_gone);
		// 币种
		String currencyCode = (String) content.get(Comm.CURRENCYCODE);
		currencyTv.setText(LocalData.Currency.get(currencyCode));
		// 钞汇标志
		String strCashRemit = (String) content.get(Dept.CASHREMIT);
		cashRemitTv.setText(LocalData.CurrencyCashremit.get(strCashRemit));
		// 如果币种是人名币 钞汇标志不显示
		LinearLayout cashRemitLayout = (LinearLayout) contentLayout.findViewById(R.id.dept_cashremit_layout);
		if (currencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {
			cashRemitLayout.setVisibility(View.INVISIBLE);
		}
		TextView curAndCashTv = (TextView) contentLayout.findViewById(R.id.dept_currency_cashremit_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(currentContent, curAndCashTv);

		// 可支取余额
		String strAvailableBalance = (String) content.get(Dept.AVAILABLE_BALANCE);
		// 是否自动转存
		TextView isConvertTv = (TextView) contentLayout.findViewById(R.id.dept_is_convert_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(currentContent, isConvertTv);
		// 是否自动转存
		String strConvertType = (String) content.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
		convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
		// 存期
		String strCdPeriod = (String) content.get(Dept.CD_PERIOD);
		cdPeriodTv.setText(LocalData.CDPeriod.get(strCdPeriod));
		// 起期日
		cdInterestStartsDateTv.setText(StringUtil.isNullChange((String) content.get(Dept.INTEREST_STARTSDATE)));
		// 到期日
		cdInterestEndDateTv.setText(StringUtil.isNullChange((String) content.get(Dept.INTEREST_ENDDATE)));
		// 结期日
		cdCloseDateTv.setText(StringUtil.isNullChange((String) content.get(Dept.SETTLEMENT_DATE)));
		// 存款种类
		String strType = (String) content.get(Dept.TYPE);
		saveTypeTv.setText(LocalData.fixAccTypeMap.get(strType));
		// 利率
		interestRateTv.setText((String) content.get(Dept.INTEREST_RATE));
		// 根据账户类型 和 存单类型 显示不同的界面
		String accountType = (String) bankAccount.get(Comm.ACCOUNT_TYPE);
		final OnClickListener btnMoreNullBocinvtClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Button tv = (Button) v;
				String text = tv.getText().toString();
				moreClick(text, v);
				if (text.equals(BaseDroidApp.getInstanse().getCurrentAct().getResources().getString(R.string.create_notify))) {
					// 建立通知
					onCreateNoticeListener.onClick(v);
				}

			}
		};
		String strContinueSaveBtn = BaseDroidApp.getInstanse().getCurrentAct().getResources().getString(R.string.continue_save_btn);
		List<String> serlist = new ArrayList<String>();
		if (serviceList == null || serviceList.size() == 0) {

		} else {
			for (int m = 0; m < LocalData.serviceCodelist.size(); m++) {
				for (int j = 0; j < serviceList.size(); j++) {
					Map<String, String> map = serviceList.get(j);
					String mm = map.get(Acc.QUERY_BUSINESSTYPECODE_RES)
							+ ConstantGloble.BOCINVT_DATE_ADD
							+ map.get(Acc.QUERY_BUSINESSSIGNCODE_RES);
					if (LocalData.serviceCodelist.get(m).equals(mm)) {
						serlist.add(LocalData.serviceMap.get(mm));
						break;
					}
				}
			}
		}
		// 非约定转存时 “到期日”不显示
		if (strConvertType != null && strConvertType.equals(ConstantGloble.CONVERTTYPE_N)) {
			endDayLayout.setVisibility(View.GONE);
		}
		if (StringUtil.isNull(strConvertType)) {
			endDayLayout.setVisibility(View.GONE);
		}
		if (accountType.equals(ConstantGloble.ACC_TYPE_REG)) {// 定期一本通
			ll_mode.setVisibility(View.VISIBLE);
			ll_type.setVisibility(View.VISIBLE);
			moneyAmountLayout.setVisibility(View.VISIBLE);
			ll_status.setVisibility(View.GONE);
			tv_status.setText(LocalData.SubAccountsStatus.get((String) content.get(Dept.STATUS)));
			startAccrualLayout.setVisibility(View.VISIBLE);
			String strMoneyAmount = (String) content.get(Dept.BOOKBALANCE);
			moneyAmountTv.setText(StringUtil.parseStringCodePattern(currencyCode, strMoneyAmount, 2));
			moneyAmountTv.setTextColor(currentContent.getResources().getColor(R.color.red));
			sumDayLayout.setVisibility(View.GONE);
			moneyAmountLayout.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) deptgoneBtn.getLayoutParams();
			checkoutBtn.setLayoutParams(params);
			// 定期一本通下面存单又分为 定期一本通 定活两便 通知存款
			if (content.get(Dept.TYPE).equals(DeptBaseActivity.WHOE_SAVE)) {// 整存整取
				checkoutBtn.setVisibility(View.VISIBLE);
				checkoutBtn.setOnClickListener(onCheckoutListener);
				convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
				if (serlist == null || serlist.size() == 0) {
					createNoticeBtn.setVisibility(View.GONE);
				} else if (serlist.size() == 1) {

					createNoticeBtn.setVisibility(View.VISIBLE);
					LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) createNoticeBtn.getLayoutParams();
					checkoutBtn.setLayoutParams(param);
					createNoticeBtn.setText(serlist.get(0));
					createNoticeBtn.setOnClickListener(btnMoreNullBocinvtClick);
				} else {
					createNoticeBtn.setVisibility(View.VISIBLE);
					LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) createNoticeBtn.getLayoutParams();
					checkoutBtn.setLayoutParams(param);
					createNoticeBtn.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.more));
					String[] service = new String[serlist.size()];
					for (int k = 0; k < serlist.size(); k++) {
						service[k] = serlist.get(k);
					}
					PopupWindowUtils.getInstance().setshowMoreChooseUpListener(BaseDroidApp.getInstanse().getCurrentAct(), createNoticeBtn, service, btnMoreNullBocinvtClick);
				}
			} else if (content.get(Dept.TYPE).equals(DeptBaseActivity.RANDOM_SAVE)) {// 定活两便
				checkoutBtn.setVisibility(View.VISIBLE);
				// 定活两便
				ll_mode.setVisibility(View.GONE);
				endDayLayout.setVisibility(View.GONE);
				checkoutBtn.setOnClickListener(onCheckoutListener);
				((LinearLayout) contentLayout.findViewById(R.id.layout_period)).setVisibility(View.GONE);
				convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
				if (serlist == null || serlist.size() == 0) {
					createNoticeBtn.setVisibility(View.GONE);
				} else if (serlist.size() == 1) {
					createNoticeBtn.setVisibility(View.VISIBLE);
					LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) createNoticeBtn.getLayoutParams();
					checkoutBtn.setLayoutParams(param);
					createNoticeBtn.setText(serlist.get(0));
					createNoticeBtn.setOnClickListener(btnMoreNullBocinvtClick);
				} else {
					createNoticeBtn.setVisibility(View.VISIBLE);
					LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) createNoticeBtn.getLayoutParams();
					checkoutBtn.setLayoutParams(param);
					createNoticeBtn.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.more));
					String[] service = new String[serlist.size()];
					for (int k = 0; k < serlist.size(); k++) {
						service[k] = serlist.get(k);
					}
					PopupWindowUtils.getInstance().setshowMoreChooseUpListener(BaseDroidApp.getInstanse().getCurrentAct(), createNoticeBtn, service, btnMoreNullBocinvtClick);
				}
			} else if (content.get(Dept.TYPE).equals(
					DeptBaseActivity.NOTIFY_SAVE)) {// 通知存款
				if (content.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE).equals(ConstantGloble.CONVERTTYPE_N)) {
					checkoutBtn.setVisibility(View.VISIBLE);
					// 只有这种情况下是两个按钮
					createNoticeBtn.setOnClickListener(onCreateNoticeListener);
					createNoticeBtn.setVisibility(View.VISIBLE);
					LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) createNoticeBtn.getLayoutParams();
					checkoutBtn.setLayoutParams(param);
					if (serlist == null || serlist.size() == 0) {
						createNoticeBtn.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.create_notify));
					} else {
						serlist.add(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.create_notify));
						createNoticeBtn.setVisibility(View.VISIBLE);
						createNoticeBtn.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.more));
						String[] service = new String[serlist.size()];
						for (int k = 0; k < serlist.size(); k++) {
							service[k] = serlist.get(k);
						}
						PopupWindowUtils.getInstance().setshowMoreChooseUpListener(BaseDroidApp.getInstanse().getCurrentAct(), createNoticeBtn, service, btnMoreNullBocinvtClick);
					}
				} else {
					checkoutBtn.setVisibility(View.VISIBLE);
					if (serlist == null || serlist.size() == 0) {
						createNoticeBtn.setVisibility(View.GONE);
					} else if (serlist.size() == 1) {
						createNoticeBtn.setVisibility(View.VISIBLE);
						LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) createNoticeBtn.getLayoutParams();
						checkoutBtn.setLayoutParams(param);
						createNoticeBtn.setText(serlist.get(0));
						createNoticeBtn.setOnClickListener(btnMoreNullBocinvtClick);
					} else {
						createNoticeBtn.setVisibility(View.VISIBLE);
						LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) createNoticeBtn.getLayoutParams();
						checkoutBtn.setLayoutParams(param);
						createNoticeBtn.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.more));
						String[] service = new String[serlist.size()];
						for (int k = 0; k < serlist.size(); k++) {
							service[k] = serlist.get(k);
						}
						PopupWindowUtils.getInstance().setshowMoreChooseUpListener(BaseDroidApp.getInstanse().getCurrentAct(), createNoticeBtn, service, btnMoreNullBocinvtClick);
					}
				}

				convertTypeTv.setText(LocalData.ConventionConvertType.get(strConvertType));
				cdPeriodTv.setText(LocalData.InfoDeposit.get(strCdPeriod));
				checkoutBtn.setOnClickListener(onCheckoutListener);
			}
		} else if (accountType.equals(ConstantGloble.ACC_TYPE_EDU)) {// 教育储蓄
			serlist.add(ConstantGloble.ACC_GO_TRANSFERDETAIL);
			ll_mode.setVisibility(View.GONE);
			ll_type.setVisibility(View.GONE);
			ll_status.setVisibility(View.VISIBLE);
			tv_status.setText(LocalData.SubAccountsStatus.get(status));
			monthSaveLayout.setVisibility(View.VISIBLE);
			currentMontyLayout.setVisibility(View.VISIBLE);
			curAndCashTv.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.currency));
			monthSaveTv.setText(StringUtil.parseStringPattern(strMonthSave, 2));
			currentMoneyTv.setText(StringUtil.parseStringPattern(strAvailableBalance, 2));

			convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
			startAccrualLayout.setVisibility(View.VISIBLE);
			endDayLayout.setVisibility(View.VISIBLE);
			if (status.equals(ConstantGloble.FOREX_ACCTYPE_NORMAL)) {
				// 有效存单
				checkoutBtn.setText(strContinueSaveBtn);
				checkoutBtn.setVisibility(View.VISIBLE);
				checkoutBtn.setOnClickListener(onContinueSaveListener);
				if (serlist == null || serlist.size() == 0) {
					createNoticeBtn.setVisibility(View.GONE);
				} else if (serlist.size() == 1) {
					createNoticeBtn.setVisibility(View.VISIBLE);
					LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) createNoticeBtn.getLayoutParams();
					checkoutBtn.setLayoutParams(param);
					createNoticeBtn.setText(serlist.get(0));
					createNoticeBtn.setOnClickListener(btnMoreNullBocinvtClick);
				} else {
					createNoticeBtn.setVisibility(View.VISIBLE);
					LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) createNoticeBtn.getLayoutParams();
					checkoutBtn.setLayoutParams(param);
					createNoticeBtn.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.more));
					String[] service = new String[serlist.size()];
					for (int k = 0; k < serlist.size(); k++) {
						service[k] = serlist.get(k);
					}
					PopupWindowUtils.getInstance().setshowMoreChooseUpListener(BaseDroidApp.getInstanse().getCurrentAct(), createNoticeBtn, service, btnMoreNullBocinvtClick);
				}
			} else {
				if (serlist == null || serlist.size() == 0) {
					checkoutBtn.setVisibility(View.GONE);
					createNoticeBtn.setVisibility(View.GONE);
				} else if (serlist.size() == 1) {
					checkoutBtn.setVisibility(View.VISIBLE);
					createNoticeBtn.setVisibility(View.GONE);
					checkoutBtn.setText(serlist.get(0));
					checkoutBtn.setOnClickListener(btnMoreNullBocinvtClick);
				} else if (serlist.size() == 2) {
					checkoutBtn.setVisibility(View.VISIBLE);
					createNoticeBtn.setVisibility(View.VISIBLE);
					LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) createNoticeBtn.getLayoutParams();
					checkoutBtn.setLayoutParams(param);
					checkoutBtn.setText(serlist.get(0));
					checkoutBtn.setOnClickListener(btnMoreNullBocinvtClick);
					createNoticeBtn.setText(serlist.get(1));
					createNoticeBtn.setOnClickListener(btnMoreNullBocinvtClick);
				} else {
					checkoutBtn.setVisibility(View.VISIBLE);
					createNoticeBtn.setVisibility(View.VISIBLE);
					LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) createNoticeBtn.getLayoutParams();
					checkoutBtn.setLayoutParams(param);
					checkoutBtn.setText(serlist.get(0));
					checkoutBtn.setOnClickListener(btnMoreNullBocinvtClick);
					createNoticeBtn.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.more));
					String[] service = new String[serlist.size() - 1];
					for (int k = 0; k < serlist.size() - 1; k++) {
						service[k] = serlist.get(k + 1);
					}
					PopupWindowUtils.getInstance().setshowMoreChooseUpListener(BaseDroidApp.getInstanse().getCurrentAct(), createNoticeBtn, service, btnMoreNullBocinvtClick);
				}
			}

		} else if (accountType.equals(ConstantGloble.ACC_TYPE_ZOR)) {// 零存整取
			serlist.add(ConstantGloble.ACC_GO_TRANSFERDETAIL);
			ll_mode.setVisibility(View.GONE);
			ll_type.setVisibility(View.GONE);
			ll_status.setVisibility(View.VISIBLE);
			tv_status.setText(LocalData.SubAccountsStatus.get(status));
			monthSaveLayout.setVisibility(View.VISIBLE);
			currentMontyLayout.setVisibility(View.VISIBLE);
			curAndCashTv.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.currency));
			monthSaveTv.setText(StringUtil.parseStringPattern(strMonthSave, 2));
			currentMoneyTv.setText(StringUtil.parseStringPattern(strAvailableBalance, 2));
			checkoutBtn.setText(strContinueSaveBtn);
			checkoutBtn.setOnClickListener(onContinueSaveListener);
			convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
			startAccrualLayout.setVisibility(View.VISIBLE);
			endDayLayout.setVisibility(View.VISIBLE);
			checkoutBtn.setVisibility(View.VISIBLE);
			if (serlist == null || serlist.size() == 0) {
				createNoticeBtn.setVisibility(View.GONE);
			} else if (serlist.size() == 1) {
				createNoticeBtn.setVisibility(View.VISIBLE);
				LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) createNoticeBtn.getLayoutParams();
				checkoutBtn.setLayoutParams(param);
				createNoticeBtn.setText(serlist.get(0));
				createNoticeBtn.setOnClickListener(btnMoreNullBocinvtClick);
			} else {
				createNoticeBtn.setVisibility(View.VISIBLE);
				LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) createNoticeBtn.getLayoutParams();
				checkoutBtn.setLayoutParams(param);
				createNoticeBtn.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.more));
				String[] service = new String[serlist.size()];
				for (int k = 0; k < serlist.size(); k++) {
					service[k] = serlist.get(k);
				}
				PopupWindowUtils.getInstance().setshowMoreChooseUpListener(BaseDroidApp.getInstanse().getCurrentAct(), createNoticeBtn, service, btnMoreNullBocinvtClick);
			}
		} else if (accountType.equals(ConstantGloble.ACC_TYPE_CBQX)) {// 存本取息
			ll_mode.setVisibility(View.VISIBLE);
			ll_type.setVisibility(View.VISIBLE);
			monthSaveLayout.setVisibility(View.VISIBLE);
			currentMontyLayout.setVisibility(View.VISIBLE);
			monthSaveTv.setText(StringUtil.parseStringPattern(strMonthSave, 2));
			currentMoneyTv.setText(StringUtil.parseStringPattern(strAvailableBalance, 2));
			checkoutBtn.setVisibility(View.GONE);
			convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
		} else {
			convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
		}
		linearLayout.removeAllViews();
		linearLayout.addView(contentLayout);
	}

	/**
	 * 信用卡账户详情弹出框
	 * 
	 * @param bankAccountList
	 *            账户详情列表
	 * @param exitAccDetailClick
	 *            关闭详情监听事件
	 * @param updateNicknameClick
	 *            更新账号别名事件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public View initCrcdMessageDialogView(boolean ishavecurrencytwo,
			final Map<String, Object> crcdAccount,
			final Map<String, Object> mapResult,
			View.OnClickListener exitAccDetailClick,
			final OnClickListener renmibiClick,
			final OnClickListener dollerClick, final String currency,
			final String currency1, String currency2,
			List<Map<String, String>> serviceList, final OnClickListener gouhui, final OnClickListener payrollQueryClick) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.acc_mycrcd_detail, null);
		TextView tv_prodCode_detail = (TextView) contentView.findViewById(R.id.tv_prodCode_detail);
		final TextView acc_accountnickname_value = (TextView) contentView.findViewById(R.id.acc_accountnickname_value);
		TextView tv_curCode_detail = (TextView) contentView.findViewById(R.id.tv_curCode_detail);
		TextView tv_buyPrice_detail = (TextView) contentView.findViewById(R.id.tv_buyPrice_detail);
		TextView tv_prodTimeLimit_detail = (TextView) contentView.findViewById(R.id.tv_prodTimeLimit_detail);
		TextView tv_applyObj_detail = (TextView) contentView.findViewById(R.id.tv_applyObj_detail);
		TextView tv_periodical_detail = (TextView) contentView.findViewById(R.id.tv_periodical_detail);
		ImageView img_exit = (ImageView) contentView.findViewById(R.id.img_exit_accdetail);
		
		TextView tv_billdivide_keyong = (TextView) contentView.findViewById(R.id.tv_billdivide_keyong);
		TextView tv_score = (TextView) contentView.findViewById(R.id.tv_score);

		LinearLayout ll_cun_lixi = (LinearLayout) contentView.findViewById(R.id.ll_cun_lixi);
		LinearLayout ll_lixi_tax = (LinearLayout) contentView.findViewById(R.id.ll_lixi_tax);
		TextView tv_cun_lixi = (TextView) contentView.findViewById(R.id.tv_cun_lixi);
		TextView tv_lixitax = (TextView) contentView.findViewById(R.id.tv_lixitax);
		Button btn_description_buydetail = (Button) contentView.findViewById(R.id.btn_description_buydetail);
		Button btn_buy_buydetail = (Button) contentView.findViewById(R.id.btn_buy_buydetail);
		/** 402新增可转账余额 */
		TextView tv_balancelimit = (TextView) contentView.findViewById(R.id.tv_balance_limit);
		final String acc_type = (String) crcdAccount.get(Crcd.CRCD_ACCOUNTTYPE_RES);
		Map<String, String> detailMap = new HashMap<String, String>();
		List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
		detailList = (List<Map<String, String>>) mapResult.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		detailMap = detailList.get(0);
		String strAccountType = "";
		if (!StringUtil.isNull(acc_type)) {
			strAccountType = LocalData.AccountType.get(acc_type);
			tv_prodCode_detail.setText(strAccountType);
		}
		if (CrcdBaseActivity.GREATWALL.equals(acc_type)) {
			ll_cun_lixi.setVisibility(View.VISIBLE);
			ll_lixi_tax.setVisibility(View.VISIBLE);
			tv_cun_lixi.setText(StringUtil.parseStringCodePattern(currency, (String) detailMap.get(Crcd.CRCD_SAVINGINTEREST), 2));
			tv_lixitax.setText(StringUtil.parseStringCodePattern(currency, (String) detailMap.get(Crcd.CRCD_SAVINGINTERESTTAX), 2));
		}
		tv_balancelimit.setText(StringUtil.parseStringCodePattern(currency, detailMap.get(Crcd.CRCD_LOANBALANCELIMIT), 2));
		String nickname = String.valueOf(crcdAccount.get(Crcd.CRCD_NICKNAME_RES));
		if (!StringUtil.isNull(acc_type)) {
			acc_accountnickname_value.setText(nickname);
		}
		String acc_accountnumber = String.valueOf(crcdAccount.get(Crcd.CRCD_ACCOUNTNUMBER_RES));
		if (!StringUtil.isNull(acc_accountnumber)) {
			tv_curCode_detail.setText(StringUtil.getForSixForString(acc_accountnumber));
		}
		final String currentBalance = (String) detailMap.get(Crcd.CRCD_CURRENTBALANCE);
		TextView tv_buyPrice_add = (TextView) contentView.findViewById(R.id.tv_buyPrice_add);
		final String currentflag = (String) detailMap.get(Crcd.CRCD_CURRENTBALANCEFLAG);
		LinearLayout ll_add = (LinearLayout) contentView.findViewById(R.id.ll_add);
		if (StringUtil.isNull(currentBalance)) {
			balance = ConstantGloble.BOCINVT_DATE_ADD;
			ll_add.setVisibility(View.GONE);
		} else {
			balance = StringUtil.parseStringCodePattern(currency, currentBalance, 2);
		}
		if (StringUtil.isNull(currentflag)) {
			ll_add.setVisibility(View.GONE);
		} else {
			if (currentflag.equals(ConstantGloble.ACC_CURRENTINDEX_VALUE)) {
				tv_buyPrice_add.setText(ConstantGloble.JIEYU);
				ll_add.setVisibility(View.VISIBLE);
			} else if (currentflag.equals(ConstantGloble.LOAN_CURRENTINDEX_VALUE)) {
				tv_buyPrice_add.setText(ConstantGloble.TOUZHI);
				ll_add.setVisibility(View.VISIBLE);
			} else {
				ll_add.setVisibility(View.GONE);
			}
		}
		/**工资卡账户查询*/
		LinearLayout payroll_query = (LinearLayout)contentView.findViewById(R.id.acc_payroll_query);
		//
		if(AccDataCenter.getInstance().getIsPayrollAccount() != null && AccDataCenter.getInstance().getIsPayrollAccount()){
			payroll_query.setVisibility(View.VISIBLE);
			payroll_query.setOnClickListener(payrollQueryClick);
		}	
		/** 账户详情信息 */
		tv_buyPrice_detail.setText(balance);
		tv_prodTimeLimit_detail.setText(StringUtil.parseStringCodePattern(currency, String.valueOf(detailMap.get(Crcd.CRCD_TOTALBALANCE)), 2));
		tv_applyObj_detail.setText(StringUtil.parseStringCodePattern(currency, String.valueOf(detailMap.get(Crcd.CRCD_TOTALLIMIT)), 2));
		tv_periodical_detail.setText(StringUtil.parseStringCodePattern(currency, String.valueOf(detailMap.get(Crcd.CRCD_INSTALLMENTLIMIT)), 2));
		tv_billdivide_keyong.setText(StringUtil.parseStringCodePattern(currency, String.valueOf(detailMap.get(Crcd.CRCD_INSTALLLMENTBALANCE)), 2));
		tv_score.setText(StringUtil.valueOf1((String) mapResult.get(Crcd.CRCD_COSUMPTITONPOINT)));

		// 退出账户详情点击事件
		img_exit.setOnClickListener(exitAccDetailClick);
		int black = currentContent.getResources().getColor(R.color.black);
		int gray = currentContent.getResources().getColor(R.color.gray);
		if (ishavecurrencytwo) {
			// 有双币种
			btn_description_buydetail.setOnClickListener(renmibiClick);
			btn_buy_buydetail.setOnClickListener(dollerClick);
			btn_description_buydetail
					.setText(LocalData.Currency.get(currency1));
			btn_buy_buydetail.setText(LocalData.Currency.get(currency2));
			if (LocalData.Currency.get(currency).equals(ConstantGloble.ACC_RMB)) {
				// 第一个按钮被选中
				btn_description_buydetail.setBackgroundResource(R.drawable.acc_top_left);
				btn_description_buydetail.setTextColor(black);
				btn_buy_buydetail.setBackgroundResource(R.drawable.acc_top_right);
				btn_buy_buydetail.setTextColor(gray);
				// btn_description_buydetail.setBackgroundColor(BaseDroidApp
				// .getInstanse().getCurrentAct().getResources()
				// .getColor(R.color.white));
				// btn_buy_buydetail.setBackgroundColor(BaseDroidApp.getInstanse()
				// .getCurrentAct().getResources()
				// .getColor(R.color.transparent_00));
			} else {
				// 第二个按钮被选中
				btn_description_buydetail.setBackgroundResource(R.drawable.acc_top_right);
				btn_description_buydetail.setTextColor(gray);
				btn_buy_buydetail.setBackgroundResource(R.drawable.acc_top_left);
				btn_buy_buydetail.setTextColor(black);
				// btn_description_buydetail.setBackgroundColor(BaseDroidApp
				// .getInstanse().getCurrentAct().getResources()
				// .getColor(R.color.transparent_00));
				// btn_buy_buydetail
				// .setBackgroundColor(BaseDroidApp.getInstanse()
				// .getCurrentAct().getResources()
				// .getColor(R.color.white));
			}
		} else {
			btn_buy_buydetail.setVisibility(View.GONE);
			btn_description_buydetail.setText(LocalData.Currency.get(currency));
			// btn_description_buydetail.setBackgroundColor(BaseDroidApp
			// .getInstanse().getCurrentAct().getResources()
			// .getColor(R.color.white));
			btn_description_buydetail.setBackgroundResource(R.drawable.acc_top_left);
			btn_description_buydetail.setTextColor(black);
		}

		Button btn_one = (Button) contentView.findViewById(R.id.btn_one);
		/** 更多 */
		Button btn_many = (Button) contentView.findViewById(R.id.btn_many);
		OnClickListener huankuan = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 信用卡还款
				TranDataCenter.getInstance().setAccInInfoMap(crcdAccount);
				TranDataCenter.getInstance().setCurrInDetail(mapResult);
				Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), TransferManagerActivity1.class);
				TranDataCenter.getInstance().setModuleType(ConstantGloble.ACC_MANAGE_TYPE);
				intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG, ConstantGloble.REL_CRCD_REPAY);
				BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
				BaseDroidApp.getInstanse().getCurrentAct().finish();
			}
		};
		final OnClickListener zhuanzhang = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 我要转账
				TranDataCenter.getInstance().setAccOutInfoMap(crcdAccount);
				TranDataCenter.getInstance().setCurrOutDetail(mapResult);
				Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), TransferManagerActivity1.class);
				TranDataCenter.getInstance().setModuleType(ConstantGloble.ACC_MANAGE_TYPE);
				intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG, ConstantGloble.ACC_TO_TRAN_CRCD);
				BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
				BaseDroidApp.getInstanse().getCurrentAct().finish();
			}
		};
		// final OnClickListener gouhui = new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (Double.valueOf(currentBalance) > 0) {
		// // 信用卡购汇还款
		// TranDataCenter.getInstance().setAccInInfoMap(crcdAccount);
		// TranDataCenter.getInstance().setCurrOutDetail(mapResult);
		// Intent intent = new Intent(BaseDroidApp.getInstanse()
		// .getCurrentAct(), TransferManagerActivity1.class);
		// TranDataCenter.getInstance().setModuleType(
		// ConstantGloble.ACC_MANAGE_TYPE);
		// intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,
		// ConstantGloble.REL_CRCD_BUY);
		// intent.putExtra(ConstantGloble.CRCD_CURRENCY2, currency);
		// BaseDroidApp.getInstanse().getCurrentAct()
		// .startActivity(intent);
		// BaseDroidApp.getInstanse().getCurrentAct().finish();
		// } else {
		// BaseDroidApp.getInstanse().showInfoMessageDialog(
		// BaseDroidApp.getInstanse().getCurrentAct()
		// .getString(R.string.crcd_foreign_no_owe));
		// return;
		// }
		//
		// }
		// };
		final OnClickListener btnMoreNullBocinvtClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Button tv = (Button) v;
				String text = tv.getText().toString();
				moreClick(text, v);
				if (text.equals(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.acc_credit_card_reimbursement))) {
					// 信用卡购汇还款
					gouhui.onClick(v);
				}
				if (text.equals(ConstantGloble.ACC_CRCD_TRAN)) {
					// 信用卡-我要转账
					zhuanzhang.onClick(v);
				}
			}
		};
		List<String> serlist = new ArrayList<String>();
		if (serviceList == null || serviceList.size() == 0) {
			btn_one.setVisibility(View.GONE);
			btn_many.setVisibility(View.GONE);
		} else {
			for (int m = 0; m < LocalData.serviceCodelist.size(); m++) {
				for (int j = 0; j < serviceList.size(); j++) {
					Map<String, String> map = serviceList.get(j);
					String mm = map.get(Acc.QUERY_BUSINESSTYPECODE_RES)
							+ ConstantGloble.BOCINVT_DATE_ADD
							+ map.get(Acc.QUERY_BUSINESSSIGNCODE_RES);
					if (LocalData.serviceCodelist.get(m).equals(mm)) {
						serlist.add(LocalData.serviceMap.get(mm));
						break;
					}
				}
			}
		}

		// 判断显示按钮
		if (acc_type.equalsIgnoreCase(AccBaseActivity.accountTypeList.get(2))) {
			// 1、长城信用卡：104
			// 用以收款、更多里面显示：用以付款、信用卡设定里的那几个功能
			btn_one.setVisibility(View.VISIBLE);
			if (LocalData.Currency.get(currency).equals(ConstantGloble.ACC_RMB)) {
				btn_one.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.acc_credit_card_payment));
				btn_one.setOnClickListener(huankuan);
				serlist.add(ConstantGloble.ACC_CRCD_TRAN);
			} else {
				// 购汇还款
				btn_one.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.acc_credit_card_reimbursement));
				btn_one.setOnClickListener(gouhui);

			}
			// // TODO 401 我要收款、我要付款
			serlist.add(ConstantGloble.ACC_GO_SHOUKUAN);
//			serlist.add(ConstantGloble.ACC_GO_FUKUAN);603去掉
			serlist.add(ConstantGloble.ACC_GO_CREDITTRANSFER);
			ArrayList<Map<String, Object>> queryCallBackList = new ArrayList<Map<String, Object>>();
			queryCallBackList.add(crcdAccount);
			GatherInitiativeData.getInstance().setQueryAcountCallBackList(queryCallBackList);
			GatherInitiativeData.getInstance().setPayAcountCallBackList(queryCallBackList);
			if (serlist == null || serlist.size() == 0) {
				btn_many.setVisibility(View.GONE);
			} else if (serlist.size() == 1) {
				btn_many.setVisibility(View.VISIBLE);
				LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_many.getLayoutParams();
				btn_one.setLayoutParams(param);
				btn_many.setText(serlist.get(0));
				btn_many.setOnClickListener(btnMoreNullBocinvtClick);
			} else {
				btn_many.setVisibility(View.VISIBLE);
				LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_many.getLayoutParams();
				btn_one.setLayoutParams(param);
				btn_many.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.more));
				String[] service = new String[serlist.size()];
				for (int k = 0; k < serlist.size(); k++) {
					service[k] = serlist.get(k);
				}
				PopupWindowUtils.getInstance().setshowMoreChooseUpListener(BaseDroidApp.getInstanse().getCurrentAct(), btn_many, service, btnMoreNullBocinvtClick);
			}
		}
		if (acc_type.equalsIgnoreCase(AccBaseActivity.accountTypeList.get(1))) {
			// 2、中银信用卡：103
			// 信用卡还款、更多里显示购汇还款、信用卡设定里的几个功能
			// 信用卡还款 103
			serlist.add(ConstantGloble.ACC_GO_CREDITTRANSFER);
			btn_one.setVisibility(View.VISIBLE);
			if (LocalData.Currency.get(currency).equals(ConstantGloble.ACC_RMB)) {
				btn_one.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.acc_credit_card_payment));
				btn_one.setOnClickListener(huankuan);
			} else {
				// 购汇还款
				btn_one.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.acc_credit_card_reimbursement));
				btn_one.setOnClickListener(gouhui);

			}
			if (serlist == null || serlist.size() == 0) {
				btn_many.setVisibility(View.GONE);
			} else if (serlist.size() == 1) {
				btn_many.setVisibility(View.VISIBLE);
				LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_many.getLayoutParams();
				btn_one.setLayoutParams(param);
				btn_many.setText(serlist.get(0));
				btn_many.setOnClickListener(btnMoreNullBocinvtClick);
			} else {
				btn_many.setVisibility(View.VISIBLE);
				LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_many.getLayoutParams();
				btn_one.setLayoutParams(param);
				btn_many.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.more));
				String[] service = new String[serlist.size()];
				for (int k = 0; k < serlist.size(); k++) {
					service[k] = serlist.get(k);
				}
				PopupWindowUtils.getInstance().setshowMoreChooseUpListener(BaseDroidApp.getInstanse().getCurrentAct(), btn_many, service, btnMoreNullBocinvtClick);
			}
		}
		if (acc_type.equalsIgnoreCase(AccBaseActivity.accountTypeList.get(4))) {
			// 3、单外币信用卡：107
			// 购汇还款、更多里显示信用卡设定里的几个功能
			// 购汇还款
			serlist.add(ConstantGloble.ACC_GO_CREDITTRANSFER);
			btn_one.setVisibility(View.VISIBLE);
			btn_one.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.acc_credit_card_reimbursement));
			btn_one.setOnClickListener(gouhui);
			if (serlist == null || serlist.size() == 0) {
				btn_many.setVisibility(View.GONE);
			} else if (serlist.size() == 1) {
				btn_many.setVisibility(View.VISIBLE);
				LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_many.getLayoutParams();
				btn_one.setLayoutParams(param);
				btn_many.setText(serlist.get(0));
				btn_many.setOnClickListener(btnMoreNullBocinvtClick);
			} else {
				btn_many.setVisibility(View.VISIBLE);
				LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_many.getLayoutParams();
				btn_one.setLayoutParams(param);
				btn_many.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.more));
				String[] service = new String[serlist.size()];
				for (int k = 0; k < serlist.size(); k++) {
					service[k] = serlist.get(k);
				}
				PopupWindowUtils.getInstance().setshowMoreChooseUpListener(BaseDroidApp.getInstanse().getCurrentAct(), btn_many, service, btnMoreNullBocinvtClick);
			}
		}
		return contentView;
	}

	/** 判断点击事件 */
	public void moreClick(String text, View v) {
		if (text.equals(LocalData.serviceMapList.get(0))) {
			// "买债券"
			buybondClick.onClick(v);
		}
		if (text.equals(LocalData.serviceMapList.get(1))) {
			// "买基金"
			buyFincClick.onClick(v);
		}
		if (text.equals(LocalData.serviceMapList.get(2))) {
			// "买外汇"
			buyForexClick.onClick(v);
		}
		if (text.equals(LocalData.serviceMapList.get(3))) {
			// 买理财
			buyBocinvtClick.onClick(v);
		}
		if (text.equals(ConstantGloble.ACC_GO_SHOUKUAN)) {
			// 我要收款
			goShouKuanClick.onClick(v);
		}
		if (text.equals(ConstantGloble.ACC_GO_FUKUAN)) {
			// 我要付款
			goFuKuanClick.onClick(v);
		}
		if (text.equals(ConstantGloble.ACC_GO_TRANSFERDETAIL) || text.equals(ConstantGloble.ACC_GO_CREDITTRANSFER)) {
			BaseDroidApp.getInstanse().getCurrentAct().setResult(100);
			BaseDroidApp.getInstanse().getCurrentAct().finish();
		}

	}
	//403医保账户
	/**
	 * 医保账户详情弹出框
	 * 
	 * @param bankAccountList
	 *            账户详情列表
	 * @param exitAccDetailClick
	 *            关闭详情监听事件
	 * @param updateNicknameClick
	 *            更新账号别名事件
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public View initMedicalAccountMessageDialogView(Activity act,
			final Map<String, Object> bankAccount,final Map<String,Object> detailMap,
			View.OnClickListener exitAccDetailClick,
			final OnClickListener updatenicknameClick) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.acc_mybankaccount_detail, null);

		final FrameLayout fl_acc_nickname = (FrameLayout) contentView.findViewById(R.id.fl_nickname);
		final LinearLayout ll_nickname = (LinearLayout) contentView.findViewById(R.id.ll_nickname);
		Button btn_updatenickname = (Button) contentView.findViewById(R.id.btn_update_nickname);
		final TextView tv_acc_accounttype = (TextView) contentView.findViewById(R.id.acc_accounttype_value);

		final TextView tv_acc_accountnickname = (TextView) contentView.findViewById(R.id.acc_accountnickname_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(BaseDroidApp.getInstanse().getCurrentAct(), tv_acc_accountnickname);
		TextView tv_acc_accountnumber = (TextView) contentView.findViewById(R.id.acc_account_number_value);
		/** 开户网点 */
		TextView acc_account_branchName = (TextView) contentView.findViewById(R.id.acc_account_branchName);
		// 开户时间 
		TextView acc_account_OpenDate = (TextView) contentView.findViewById(R.id.acc_account_OpenDate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(BaseDroidApp.getInstanse().getCurrentAct(), acc_account_branchName);
		String branchName = (String) detailMap.get(Acc.ACCOPENBANK);
		String openDate = (String) detailMap.get(Acc.ACC_OPENDATE_RES);
		if (StringUtil.isNull(branchName)) {
			contentView.findViewById(R.id.ll_acc_branchName).setVisibility(View.GONE);
		} else {
			contentView.findViewById(R.id.ll_acc_branchName).setVisibility(View.VISIBLE);
			acc_account_branchName.setText(branchName);
		}
		if (StringUtil.isNull(openDate)) {
			contentView.findViewById(R.id.ll_acc_openDate).setVisibility(View.GONE);
		} else {
			contentView.findViewById(R.id.ll_acc_openDate).setVisibility(View.VISIBLE);
			acc_account_OpenDate.setText(openDate);
		}
		
		TextView tv_acc_account_state = (TextView) contentView.findViewById(R.id.acc_account_state);
		String status=(String) detailMap.get(Acc.ACC_ACCOUNTSTATUS_RES);
		if (!StringUtil.isNull(status)) {
			tv_acc_account_state.setText(LocalData.SubAccountsStatus.get(status));
		}
		final EditText et_acc_accountnickname = (EditText) contentView.findViewById(R.id.et_acc_nickname);
		EditTextUtils.setLengthMatcher(act, et_acc_accountnickname, 20);
		ImageView img_exit = (ImageView) contentView.findViewById(R.id.img_exit_accdetail);
		final ImageView img_update_accnickname = (ImageView) contentView.findViewById(R.id.img_acc_update_nickname);
		final String acc_type = String.valueOf(bankAccount.get(Acc.ACC_ACCOUNTTYPE_RES));
		tv_acc_accounttype.setText(LocalData.AccountType.get(acc_type.trim()));
		tv_acc_accountnickname.setText((String) bankAccount.get(Acc.ACC_NICKNAME_RES));
		String acc_accountnumber = String.valueOf(bankAccount.get(Acc.ACC_ACCOUNTNUMBER_RES));
		tv_acc_accountnumber.setText(StringUtil.getForSixForString(acc_accountnumber));

		LinearLayout acc_currency_add = (LinearLayout) contentView.findViewById(R.id.ll_add_currency);
		/** 账户详情列表信息 */
		final List<Map<String, Object>> accountDetailList = (List<Map<String, Object>>) ((detailMap.get(ConstantGloble.ACC_DETAILIST)));
		List<Map<String, Object>> accdetailList = new ArrayList<Map<String, Object>>();
		if (accountDetailList == null || accountDetailList.size() == 0) {

		} else {
			for (int i = 0; i < accountDetailList.size(); i++) {
				String currencyname = (String) accountDetailList.get(i).get(Acc.DETAIL_CURRENCYCODE_RES);
				// 过滤
				if (StringUtil.isNull(LocalData.currencyboci.get(currencyname))) {
					accdetailList.add(accountDetailList.get(i));
				}
			}

			if (accdetailList == null || accdetailList.size() == 0) {

			} else {
				for (int i = 0; i < accdetailList.size(); i++) {
					View currency_view = LayoutInflater.from(currentContent).inflate(R.layout.acc_currency_balance, null);
					acc_currency_add.addView(currency_view, i);

					TextView tv_acc_currencycode = (TextView) currency_view.findViewById(R.id.acc_currencycode);
					TextView tv_acc_accbookbalance = (TextView) currency_view.findViewById(R.id.acc_bookbalance);
					PopupWindowUtils.getInstance().setOnShowAllTextListener(BaseDroidApp.getInstanse().getCurrentAct(), tv_acc_accbookbalance);
					PopupWindowUtils.getInstance().setOnShowAllTextListener(BaseDroidApp.getInstanse().getCurrentAct(), tv_acc_currencycode);
					String currencyname = (String) accdetailList.get(i).get(Acc.DETAIL_CURRENCYCODE_RES);
					String cashRemit = (String) accdetailList.get(i).get(Acc.DETAIL_CASHREMIT_RES);
					if (LocalData.Currency.get(currencyname).equals(ConstantGloble.ACC_RMB)) {
						tv_acc_currencycode.setText(LocalData.Currency.get(currencyname) + ConstantGloble.ACC_COLON);
					} else {
						tv_acc_currencycode.setText(LocalData.Currency.get(currencyname)
								+ ConstantGloble.ACC_STRING
								+ LocalData.CurrencyCashremit.get(cashRemit)
								+ ConstantGloble.ACC_COLON);
					}

					tv_acc_accbookbalance.setText(StringUtil.parseStringCodePattern(currencyname, (String) accdetailList.get(i).get(Acc.DETAIL_AVAILABLEBALANCE_RES), 2));
				}
			}
		}
		// 退出账户详情点击事件
		img_exit.setOnClickListener(exitAccDetailClick);
		// 修改账户别名点击事件
		img_update_accnickname.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				fl_acc_nickname.setVisibility(View.VISIBLE);
				ll_nickname.setVisibility(View.GONE);
				BaseActivity activity = (BaseActivity)BaseDroidApp.getInstanse().getCurrentAct();
				if(activity != null)
					activity.upSoftInput();
				et_acc_accountnickname.setText(tv_acc_accountnickname.getText().toString());
				et_acc_accountnickname.setSelection(et_acc_accountnickname.length());
			}
		});
		btn_updatenickname.setOnClickListener(updatenicknameClick);
		return contentView;
	}
}
