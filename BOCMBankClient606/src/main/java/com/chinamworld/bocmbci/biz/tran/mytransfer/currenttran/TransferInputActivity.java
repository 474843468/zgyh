package com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 资金转出输入页面
 * 
 * @author panwe
 * 
 */
public class TransferInputActivity extends TranBaseActivity {
	private Context context = this;
	private LayoutInflater inflater = null;
	private View container;
	/** 转出账户控件 */
	private RelativeLayout mAccOutLl = null;
	/** 转入账户控件 */
	private RelativeLayout mAccInLl = null;
	/** 转出账户列表Layout */
	private LinearLayout mAccOutListLayout = null;
	/** 底部布局 */
	private LinearLayout bottomLayout = null;
	/** 转出账户详情 布局 */
	private LinearLayout accOutDetailLayout = null;
	// /** 转出账户详情 布局 */
	private LinearLayout accInDetailLayout = null;
	TextView currencyCodeTv;
	TextView cashTv;
	/** 转出账户可以用余额 */
	private String availableBalance;
	/** 币种代码,发送到服务器 */
	private String currencyCode;
	/** 钞汇标志代码,发送到服务器 */
	private String cashRemitCode;
	/** 转账金额 */
	private EditText amountEt;
	private String amount;
	/** 币种和对应钞汇标志数据 */
	private ArrayList<Map<String, Object>> currencyAndCashRemitList;
	/** 关联账户转账对应的币种和钞汇标志数据 */
	private List<String> currencyList;
	private List<String> cashRemitList;
	/** 当前选择的币种和对应钞汇标志数据 */
	private ArrayList<Map<String, Object>> currentCurrencyCashRemitList;
	// T43快捷方式按钮
	private LinearLayout tran_acc_seach_linear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.tran_my_trans));
		container = mInflater.inflate(R.layout.tran_mytransfer_activity1, null);
		inflater = LayoutInflater.from(this);
		tabcontent.removeAllViews();
		tabcontent.addView(container);
		mTopRightBtn.setVisibility(View.GONE);
		setLeftSelectedPosition("tranManager_1");
		// 转出账户布局
		mAccOutLl = (RelativeLayout) container
				.findViewById(R.id.ll_acc_out_mytransfer);
		mAccOutListLayout = (LinearLayout) container
				.findViewById(R.id.ll_acc_out_list_layout);
		// 转入账户布局accin_layout
		mAccInLl = (RelativeLayout) container
				.findViewById(R.id.ll_acc_in_mytransfer);
		bottomLayout = (LinearLayout) container
				.findViewById(R.id.dept_save_regular_bottom);
		tran_acc_seach_linear = (LinearLayout) container
				.findViewById(R.id.tran_acc_seach_linear);
		tran_acc_seach_linear.setVisibility(View.GONE);
		mTopRightBtn.setVisibility(View.GONE);
		showAccOutDetailView();
		showAccInDetailView();
		combineData();
		initRelTran();
	}


//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("tranManager_1");
//	}
	

	/**
	 * 显示转出账户列表详情
	 */
	private void showAccOutDetailView() {
		accOutDetailLayout = (LinearLayout) inflater.inflate(
				R.layout.dept_acc_out_list_detail, null);

		mAccOutLl.removeAllViews();
		mAccOutLl.addView(accOutDetailLayout);
		mAccOutLl.setVisibility(View.VISIBLE);
		mAccOutListLayout.setVisibility(View.GONE);
		showAccOutListItemData(accOutDetailLayout);// 显示转出账户条目的详细信息

	}

	private void showAccInDetailView() {
		accInDetailLayout = (LinearLayout) inflater.inflate(
				R.layout.dept_acc_out_list_detail, null);
		TextView tranFlagTv = (TextView) accInDetailLayout
				.findViewById(R.id.tran_flag_tv);
		tranFlagTv.setText(this.getResources().getString(
				R.string.tran_acc_in_person));
		ImageView iv_in = (ImageView) accInDetailLayout
				.findViewById(R.id.imageview);
		iv_in.setImageResource(R.drawable.tran_acc_in_person);
		mAccInLl.removeAllViews();
		mAccInLl.addView(accInDetailLayout);
		mAccInLl.setVisibility(View.VISIBLE);
		showAccInListItemData(accInDetailLayout);
	}

	/**
	 * 显示转出账户条目的详细信息
	 */
	private void showAccOutListItemData(View view) {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		if (accOutInfoMap == null) {
			return;
		}
		TextView accountTypeTv = (TextView) view
				.findViewById(R.id.tv_accountType_accOut_list_detail);
		TextView nickNameTv = (TextView) view
				.findViewById(R.id.tv_nickName_accOut_list_detail);
		TextView accountNumberTypeTv = (TextView) view
				.findViewById(R.id.tv_accountNumber_accOut_list_detail);
		TextView currencyCodeTv = (TextView) view
				.findViewById(R.id.tv_currey_accOut_list_detail);
		TextView cashTv = (TextView) view
				.findViewById(R.id.tv_lastprice_accOut_list_detail);

		String strAccountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
		String strAccountT = LocalData.AccountType.get(strAccountType);
		accountTypeTv.setText(strAccountT);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				accountTypeTv);

		nickNameTv.setText((String) accOutInfoMap.get(Comm.NICKNAME));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				nickNameTv);
		String accoutNumber = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		accountNumberTypeTv
				.setText(StringUtil.getForSixForString(accoutNumber));

		Map<String, Object> resultMap = TranDataCenter.getInstance()
				.getCurrOutDetail();

		@SuppressWarnings("unchecked")
		List<Map<String, String>> detialList = (List<Map<String, String>>) resultMap
				.get(ConstantGloble.ACC_DETAILIST);
		if (StringUtil.isNullOrEmpty(detialList)) {
			return;
		}
		currencyCode = null;
		// 显示人名币的那个数据
		for (int i = 0; i < detialList.size(); i++) {
			Map<String, String> map = detialList.get(i);
			String currency = map.get(Comm.CURRENCYCODE);
			if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {
				currencyCode = currency;
				availableBalance = map.get(Dept.AVAILABLE_BALANCE);
				break;
			}
		}
		if (StringUtil.isNullOrEmpty(currencyCode)) {
			currencyCode = (String) detialList.get(0).get(Comm.CURRENCYCODE);
			// 可用余额
			availableBalance = detialList.get(0).get(Dept.AVAILABLE_BALANCE);
		}
		cashTv.setText(StringUtil.parseStringPattern(availableBalance, 2));
		currencyCodeTv.setText(LocalData.Currency.get(currencyCode));
	}

	/**
	 * 显示转入账户条目的详细信息
	 */
	private void showAccInListItemData(View view) {
		Map<String, Object> accInInfoMap;

		// 其他模块
		accInInfoMap = TranDataCenter.getInstance().getAccInInfoMap();

		if (accInInfoMap == null) {
			return;
		}
		TextView accountTypeTv = (TextView) view
				.findViewById(R.id.tv_accountType_accOut_list_detail);
		TextView nickNameTv = (TextView) view
				.findViewById(R.id.tv_nickName_accOut_list_detail);
		TextView accountNumberTypeTv = (TextView) view
				.findViewById(R.id.tv_accountNumber_accOut_list_detail);
		currencyCodeTv = (TextView) view
				.findViewById(R.id.tv_currey_accOut_list_detail);
		cashTv = (TextView) view
				.findViewById(R.id.tv_lastprice_accOut_list_detail);

		String strAccountType = (String) accInInfoMap.get(Comm.ACCOUNT_TYPE);
		String strAccountT = LocalData.AccountType.get(strAccountType);
		accountTypeTv.setText(strAccountT);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				accountTypeTv);

		nickNameTv.setText((String) accInInfoMap.get(Comm.NICKNAME));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				nickNameTv);
		String accoutNumber = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		accountNumberTypeTv
				.setText(StringUtil.getForSixForString(accoutNumber));
		Map<String, Object> resultMap = TranDataCenter.getInstance()
				.getCurrInDetail();
		// 根据卡类型 获取的数据字段不一样
		// 如果是长城信用卡
		String currencyCode = null;
		String availableBalance = null;
		@SuppressWarnings("unchecked")
		List<Map<String, String>> detialList = (List<Map<String, String>>) resultMap
				.get(ConstantGloble.ACC_DETAILIST);
		if (StringUtil.isNullOrEmpty(detialList)) {
			return;
		}
		// 显示人名币的那个数据
		for (int i = 0; i < detialList.size(); i++) {
			Map<String, String> map = detialList.get(i);
			String currency = map.get(Comm.CURRENCYCODE);
			if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {
				currencyCode = currency;
				availableBalance = map.get(Dept.AVAILABLE_BALANCE);
				break;
			}
		}
		if (StringUtil.isNullOrEmpty(currencyCode)) {
			currencyCode = (String) detialList.get(0).get(Comm.CURRENCYCODE);
			// 可用余额
			availableBalance = detialList.get(0).get(Dept.AVAILABLE_BALANCE);
		}
		currencyCodeTv.setText(LocalData.Currency.get(currencyCode));
		cashTv.setText(StringUtil.parseStringCodePattern(currencyCode,
				availableBalance, 2));
	}

	/**
	 * 组装币种和钞汇标志数据
	 */
	private void combineData() {
		currencyAndCashRemitList = new ArrayList<Map<String, Object>>();
		currencyList = new ArrayList<String>();
		cashRemitList = new ArrayList<String>();
		Map<String, Object> detailMap = TranDataCenter.getInstance()
				.getCurrOutDetail();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> detailList = (List<Map<String, Object>>) detailMap
				.get(Tran.ACCOUNTDETAIL_ACCOUNTDETAILIST_RES);
		// 返回数据里面 存折号和存单是没有关联的 重新组装数据
		// 找出列表中所有不相同的币种
		for (int i = 0; i < detailList.size(); i++) {
			Map<String, Object> detaimap = detailList.get(i);
			String currencyCode = (String) detaimap.get(Tran.CURRENCYCODE_RES);
			// 过滤掉贵金属
			if (StringUtil.isNullOrEmpty(currencyCode)
					|| LocalData.prmsTradeStyleCodeList.contains(currencyCode)) {
				continue;
			}
			if (currencyList.size() > 0) {
				if (!currencyList.contains(currencyCode)) {
					if (currencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 如果是人民币
																			// 默认放第一个
						currencyList.add(0, currencyCode);
					} else {
						currencyList.add(currencyCode);
					}
				}
			} else {
				currencyList.add(currencyCode);
			}
		}
		if (StringUtil.isNullOrEmpty(currencyList)) {
			return;
		}

		// 币种和钞汇标志 对应起来
		for (int i = 0; i < currencyList.size(); i++) {
			ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapContent = null;
			for (int j = 0; j < detailList.size(); j++) {
				Map<String, Object> detaimap = detailList.get(j);
				String currencyCode = (String) detaimap
						.get(Tran.CURRENCYCODE_RES);
				if (StringUtil.isNullOrEmpty(currencyCode)) {
					continue;
				}
				String cashRemit = (String) detaimap
						.get(Tran.ACCOUNTDETAIL_CASHREMIT_RES);
				if (currencyCode.equals(currencyList.get(i))) {
					mapContent = new HashMap<String, Object>();
					mapContent.put(ConstantGloble.TRAN_CASHREMIT, cashRemit);
					mapContent.put(ConstantGloble.CONTENT, detailList.get(j));
					list.add(mapContent);
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Tran.CURRENCYCODE_RES, currencyList.get(i));
			map.put(ConstantGloble.TRAN_CASHREMIT_AND_CONTENT, list);
			currencyAndCashRemitList.add(map);
		}
	}

	/**
	 * 初始化底部视图 关联账户转账底部视图 非信用卡
	 */
	private void initRelTran() {
		View v = mInflater.inflate(
				R.layout.tran_rel_transfer_seting_mytransfer, null);
		initRelTranView(v);
		bottomLayout.removeAllViews();
		bottomLayout.addView(v);
	}

	/**
	 * 初始化底部视图
	 * 
	 * @param v
	 */
	private void initRelTranView(View v) {
		final List<String> currencyCodeFlagList = new ArrayList<String>();
		for (int i = 0; i < currencyList.size(); i++) {
			String currencyCode = currencyList.get(i);
			currencyCodeFlagList.add(LocalData.Currency.get(currencyCode));
		}
		// 币种
		Spinner currencySp = (Spinner) v
				.findViewById(R.id.sp_rel_trans_currency_tranSeting);
		// 钞汇标志
		final Spinner cashRemitSp = (Spinner) v
				.findViewById(R.id.sp_rel_trans_cashRemit_tranSeting);
		final List<String> cashRemitFlagList = new ArrayList<String>();

		ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(
				TransferInputActivity.this, R.layout.custom_spinner_item,
				currencyCodeFlagList);
		currencyAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currencySp.setAdapter(currencyAdapter);
		// 设置币种
		currencySp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				currencyCode = currencyList.get(position);
				if (currencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {
					cashRemitSp.setEnabled(false);
					cashRemitSp
							.setBackgroundDrawable(TransferInputActivity.this
									.getResources().getDrawable(
											R.drawable.bg_spinner_default));
				} else {
					cashRemitSp.setEnabled(true);
					cashRemitSp
							.setBackgroundDrawable(TransferInputActivity.this
									.getResources().getDrawable(
											R.drawable.bg_spinner));
				}
				currentCurrencyCashRemitList = (ArrayList<Map<String, Object>>) currencyAndCashRemitList
						.get(position).get(
								ConstantGloble.TRAN_CASHREMIT_AND_CONTENT);
				cashRemitFlagList.clear();
				cashRemitList.clear();
				for (int i = 0; i < currentCurrencyCashRemitList.size(); i++) {
					String cashRemit = (String) currentCurrencyCashRemitList
							.get(i).get(ConstantGloble.TRAN_CASHREMIT);
					cashRemitList.add(cashRemit);
					cashRemitFlagList.add(LocalData.cashRemitBackMap
							.get(cashRemit));
				}
				ArrayAdapter<String> cashRemitAdapter = new ArrayAdapter<String>(
						TransferInputActivity.this,
						R.layout.custom_spinner_item, cashRemitFlagList);
				cashRemitAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				cashRemitSp.setAdapter(cashRemitAdapter);
				cashRemitSp
						.setOnItemSelectedListener(cashRemitSelectedListener);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				currencyCode = currencyList.get(0);// 默认上送人民币
			}
		});

		// 转账金额
		amountEt = (EditText) v
				.findViewById(R.id.et_amount_rel_trans_mytransfer);
		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(amountEt, tv_for_amount);
		initRelBottomBtn(v);
	}

	private OnItemSelectedListener cashRemitSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			cashRemitCode = cashRemitList.get(position);
			// 得到对应余额
			Map<String, Object> map = currentCurrencyCashRemitList
					.get(position);
			@SuppressWarnings("unchecked")
			Map<String, String> content = (Map<String, String>) map
					.get(ConstantGloble.CONTENT);
			availableBalance = content.get(Dept.AVAILABLE_BALANCE);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			cashRemitCode = cashRemitList.get(0);
		}
	};

	/**
	 * 初始化底部按钮 立即执行 预约日期执行 预约周期执行
	 */
	private void initRelBottomBtn(View v) {
		Button btn_next = (Button) v.findViewById(R.id.btn_boc_next);
		// 专属理财账户
		tranTypeFlag = TRANTYPE_BOC_ACCOUNT;
		LinearLayout exeDate = (LinearLayout) v.findViewById(R.id.ll_exeDate);
		LinearLayout ll_fuyan = (LinearLayout) v.findViewById(R.id.ll_fuyan);
		exeDate.setVisibility(View.GONE);
		btn_next.setVisibility(View.VISIBLE);
		ll_fuyan.setVisibility(View.GONE);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 资金转出
				amount = amountEt.getText().toString().trim();
				boolean flag = judgeUserData(amount);
				if (!flag) {
					return;
				}
				Map<String, Object> acctOFmap = TranDataCenter.getInstance()
						.getAccmap();
				BociDataCenter.getInstance().setAcctOFmap(acctOFmap);
				startActivity(new Intent(TransferInputActivity.this,
						TransferConfirmActivity.class)
						.putExtra(BocInvt.AMOUNT, amount)
						.putExtra(BocInvt.BOCINVT_CANCEL_CURRENCY_REQ,
								currencyCode)
						.putExtra(BocInvt.BOCINVT_CANCEL_CASHREMIT_RES,
								cashRemitCode));
			}
		});
	}

	/**
	 * 判断用户输入数据
	 * 
	 * @return true 合格 false 不合格
	 */
	private boolean judgeUserData(String amount) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean transAmountReg = checkJapReg(currencyCode, getResources()
				.getString(R.string.reg_transferAmount), amount);
		lists.add(transAmountReg);
		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}
		return true;
	}
}
