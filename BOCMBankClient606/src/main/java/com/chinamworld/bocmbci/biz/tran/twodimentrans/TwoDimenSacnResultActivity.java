package com.chinamworld.bocmbci.biz.tran.twodimentrans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.dept.adapter.AccOutListAdapter;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.NoRelConfirmInfoActivity1;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.PreDateExeActivity1;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.PrePeriodExeActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 二维码扫描 填写信息
 * 
 * @author
 * 
 */
public class TwoDimenSacnResultActivity extends TranBaseActivity implements
		OnClickListener {
	private Context context = this;
	private View container;
	/** 转出账户控件 */
	private RelativeLayout mAccOutLl = null;
	/** 转入账户控件 */
	private RelativeLayout mAccInLl = null;
	/** 转出账户列表Layout */
	private LinearLayout mAccOutListLayout = null;
	/** 转入账户列表Layout */
	private LinearLayout mAccInListLayout = null;
	/** 新增关联账户 */
	private Button newAddTranOutBtn;
	/** 新增转入账户 */
	private Button newAddTranInBtn;
	/** 转出账户详情 布局 */
	private LinearLayout accOutDetailLayout = null;
	// /** 转出账户详情 布局 */
	private LinearLayout accInDetailLayout = null;
	/** 转出账户listview */
	private ListView accOutListView = null;

	private boolean isTranOutFirst = true;
	/** 转出账户可以用余额 */
	private String availableBalance;
	/** 转账金额 */
	private EditText amountEt;
	private String amount;
	/** 附言 */
	private EditText remarkEt;
	/** 收款人手机号 */
	private EditText payeeMobileEt;
	private String payeeMobile;

	private String memo;
	/** 执行方式 */
	private String tranMode;
	/** 绑定id */
	private String combineId;
	/** 币种代码,发送到服务器 */
	private String currencyCode;
	/** 钞汇标志代码,发送到服务器 */
	private String cashRemitCode;

	/** 是否选择转出账户标识 */
	private boolean isOutChoose = false;
	/** 是否从我要转账模块跳过来 */
	private int fromMyTran = 0;
	private Map<String, Object> addOutforMap;
	/** 其他模块跳入标识 */
	private int otherPartJumpFlag = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.two_dimen_scan));
		container = mInflater.inflate(R.layout.tran_twodimen_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(container);
		mTopRightBtn.setVisibility(View.INVISIBLE);
		setLeftSelectedPosition("tranManager_3");
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(TwoDimenSacnResultActivity.this,
						TwoDimenTransActivity1.class);
				startActivity(intent);
				finish();
			}
		});

		initView();

		showOutDetailView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent = new Intent();
			intent.setClass(TwoDimenSacnResultActivity.this,
					TwoDimenTransActivity1.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("tranManager_3");
//	}

	private void initView() {
		// 转出账户布局
		mAccOutLl = (RelativeLayout) container
				.findViewById(R.id.ll_acc_out_mytransfer);
		mAccOutListLayout = (LinearLayout) container
				.findViewById(R.id.ll_acc_out_list_layout);
		mAccOutLl.setOnClickListener(accOutClicklistener);
		newAddTranOutBtn = (Button) container
				.findViewById(R.id.dept_add_new_tranout_btn);
		// newAddTranOutBtn.setOnClickListener(addNewAcc);
		// 转入账户布局
		mAccInLl = (RelativeLayout) container
				.findViewById(R.id.ll_acc_in_mytransfer);
		mAccInListLayout = (LinearLayout) container
				.findViewById(R.id.ll_acc_in_list_layout);
		mAccInLl.setOnClickListener(accInClicklistener);
		newAddTranInBtn = (Button) container
				.findViewById(R.id.dept_add_new_tranin_btn);
		// newAddTranInBtn.setOnClickListener(addAccInNoClicklistener);

		showAccInDetailView();
		initBankInView();
	}

	/**
	 * 显示转出账户详情 如果是从我要转账模块过来 直接显示
	 */
	private void showOutDetailView() {
		fromMyTran = getIntent().getIntExtra(
				ConstantGloble.TRAN_TWO_DIMEN_FLAG, 0);
		// 是否可以选择转出账户的标志
		otherPartJumpFlag = getIntent().getIntExtra(
				ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
		if (fromMyTran == ConstantGloble.TRAN_TO_TOWDIMEN) {
			isTranOutFirst = false;
			mAccOutLl.setEnabled(false);
			showAccOutDetailView();
		}
	}

	/**
	 * 初始化行内转账底部视图
	 */
	private void initBankInView() {
		initBankInAndOtherInputView();
		initBankInBtnBottom();
	}

	/** 是否发送手机短信通知收款人标示 */
	private String isSendSmc = ConstantGloble.FALSE;

	/**
	 * 行内和跨行公用的 金额输入框和附言 发送手机短信
	 * 
	 * @param v
	 */
	private void initBankInAndOtherInputView() {
		/** 用户输入转账金额 */
		amountEt = (EditText) this
				.findViewById(R.id.et_commBoc_transferAmount_tranSeting);
		/** 用户输入附言 */
		remarkEt = (EditText) this
				.findViewById(R.id.et_commBoc_remark_tranSeting);
		EditTextUtils.setLengthMatcher(this, remarkEt, 20);//二维码转账附言20字符
		TextView tv_for_amount = (TextView) findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(amountEt, tv_for_amount);
		/** 收款人手机号 */
		payeeMobileEt = (EditText) this
				.findViewById(R.id.et_commBoc_payeeMobile_transSeting);
		/** 常用行内 短信通知收款人复选框 */
		final CheckBox sendSmcToPayeeCk = (CheckBox) this
				.findViewById(R.id.ck_sendSmcPayee_bocTrans_transSeting);
		final LinearLayout sendSmcToPayeeLl = (LinearLayout) this
				.findViewById(R.id.ll_sendSmcPayee_bocTrans_tranSeting);
		sendSmcToPayeeCk
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							isSendSmc = ConstantGloble.TRUE;
							// 反显手机号
							Map<String, Object> accInInfoMap = TranDataCenter
									.getInstance().getAccInInfoMap();
							payeeMobile = (String) accInInfoMap
									.get(Tran.TRANS_MOBILE_RES);
							if (!StringUtil.isNullOrEmpty(payeeMobile)) {
								payeeMobileEt.setText(payeeMobile);
								payeeMobileEt.setSelection(payeeMobile.length());
							}
							sendSmcToPayeeLl.setVisibility(View.VISIBLE);
						} else {
							isSendSmc = ConstantGloble.FALSE;
							sendSmcToPayeeLl.setVisibility(View.GONE);
						}
					}
				});
	}

	/**
	 * 初始化行内转转底部按钮
	 * 
	 * @param view
	 */
	private void initBankInBtnBottom() {
		/** 常用行内 立即执行 */
		Button nowExeBtn = (Button) this
				.findViewById(R.id.btn_commBoc_nowExe_tranSeting);
		nowExeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				amount = amountEt.getText().toString().trim();
				memo = remarkEt.getText().toString().trim();
				payeeMobile = payeeMobileEt.getText().toString().trim();
				// 转账金额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成
				tranMode = ConstantGloble.NOWEXE;
				if (!isOutChoose) {
					String message = getString(R.string.two_dimen_choose_msg);
					CustomDialog.toastInCenter(context, message);
					// BaseDroidApp.getInstanse().createDialog(null,
					// R.string.choose_outno_message);
					return;
				}
				if (!checkRMBMessage()) {
					return;
				};
				boolean flag = judgeUserData(amount, true);
				if (!flag) {
					return;
				}
				// 保存用户输入数据
				saveBankInUserData();
				// TODO 如果收入了手机号 还要放入手机号
				// 先调预交易接口 再掉手续费接口
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
				// requestForTransferCommissionCharge(ConstantGloble.PB031);
			}
		});
		/** 常用行内 预约日期执行 */
		Button preDateExeBtn = (Button) this
				.findViewById(R.id.btn_commBoc_preDateExe_tranSeting);
		//二维码转账 属于非关联账户转账   不显示预约日期执行 按钮
		preDateExeBtn.setVisibility(View.GONE);
//		preDateExeBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				amount = amountEt.getText().toString().trim();
//				memo = remarkEt.getText().toString().trim();
//				payeeMobile = payeeMobileEt.getText().toString().trim();
//				// 转账金额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成
//				tranMode = ConstantGloble.PREDATEEXE;
//				if (!isOutChoose) {
//					String message = getString(R.string.two_dimen_choose_msg);
//					CustomDialog.toastInCenter(context, message);
//					return;
//				}
//				if (!checkRMBMessage()) {
//					return;
//				};
//				boolean flag = judgeUserData(amount);
//				if (!flag) {
//					return;
//				}
//				// 保存用户输入数据
//				saveBankInUserData();
//				// 手续费试算接口
//				// requestForTransferCommissionCharge(ConstantGloble.PB031);
//				BaseHttpEngine.showProgressDialog();
//				requestSystemDateTime();
//			}
//		});
		/** 常用行内 预约周期执行 */
		Button prePeriodExeBtn = (Button) this
				.findViewById(R.id.btn_commBoc_prePeriodExe_tranSeting);
		//二维码转账 属于非关联账户转账   不显示预约周期 按钮
		prePeriodExeBtn.setVisibility(View.GONE);
//		prePeriodExeBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				amount = amountEt.getText().toString().trim();
//				memo = remarkEt.getText().toString().trim();
//				payeeMobile = payeeMobileEt.getText().toString().trim();
//				// 转账金额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成
//				tranMode = ConstantGloble.PREPERIODEXE;
//				if (!isOutChoose) {
//					String message = getString(R.string.two_dimen_choose_msg);
//					CustomDialog.toastInCenter(context, message);
//					return;
//				}
//				if (!checkRMBMessage()) {
//					return;
//				};
//				boolean flag = judgeUserData(amount);
//				if (!flag) {
//					return;
//				}
//				// 保存用户输入数据
//				saveBankInUserData();
//				// 手续费试算接口
//				// requestForTransferCommissionCharge(ConstantGloble.PB031);
//				BaseHttpEngine.showProgressDialog();
//				requestSystemDateTime();
//			}
//		});
	}

	/**
	 * 判断用户输入数据
	 * @param isMountCompare 是否比较金额有效余额
	 * @return true 合格 false 不合格
	 */
	private boolean judgeUserData(String amount,boolean  isMountCompare) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean transAmountReg = new RegexpBean(getResources().getString(
				R.string.reg_transferAmount), amount, "tranAmount");
		lists.add(transAmountReg);
		if (isSendSmc.equals(ConstantGloble.TRUE)) {// 如果有选择短信通知收款人 则需要验证手机号
			RegexpBean payeeMobileReg = new RegexpBean(getResources()
					.getString(R.string.tel_num_str), payeeMobile,
					"shoujiH_01_15");
			lists.add(payeeMobileReg);
		}
		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}
		// 判断转出账户余额
		 Map<String, Object> detailMap = TranDataCenter.getInstance().getCurrOutDetail();
		 if(isMountCompare &&  detailMap != null   ){
			 if( detailMap.get(Tran.ACCOUNTDETAIL_ACCOUNTDETAILIST_RES) != null){
				 List<Map<String, Object>> detailList = (List<Map<String, Object>>) detailMap.get(Tran.ACCOUNTDETAIL_ACCOUNTDETAILIST_RES); 
					for (int i = 0; i < detailList.size(); i++) {
						Map<String, Object> detaimap = detailList.get(i);
						String cCode = (String) detaimap.get(Tran.CURRENCYCODE_RES);
						if(cCode != null && cCode.equals(currencyCode)){
							String availableAmount =  (String) detaimap.get(Dept.AVAILABLE_BALANCE);
							 if (availableAmount !=null && Double.parseDouble(amount) >  Double.parseDouble(availableAmount)) {
								 BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.amount_wrong_two));
							     return false;
							 }
							 continue;
						}
					}
			 }else if(detailMap.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST) != null){
				 if (!TextUtils.isEmpty(amount) && !TextUtils.isEmpty(availableBalance)
							&& Double.parseDouble(amount) >  Double.parseDouble(availableBalance)) {
					 BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.amount_wrong_two));
				     return false;
				 }
			 }
		 }
		return true;
	}

	/**
	 * 行内转账用户输入数据
	 */
	private void saveBankInUserData() {
		Map<String, String> userInputMap = new HashMap<String, String>();
		userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
		userInputMap.put(Tran.INPUT_TRANSFER_REMARK, memo);
		userInputMap.put(Tran.INPUT_PAYEE_TRANSFER_MOBILE, payeeMobile);
		userInputMap.put(ConstantGloble.IS_SEND_SMC, isSendSmc);
		userInputMap.put(Tran.MANAGE_PRE_transMode_RES, tranMode);
		TranDataCenter.getInstance().setUserInputMap(userInputMap);
	}

	/**
	 * 转出账户按钮点击事件
	 */
	private OnClickListener accOutClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// // 点击转出账户之后 将accoutInfoMap置空
			// accOutInfoMap = null;
			// 发送通讯 请求转出账户列表
			if (isTranOutFirst) {
				requestTranoutAccountList();
			} else {
				showAccOutListDialog();
			}

		}
	};
	/**
	 * 转入账户按钮点击事件
	 */
	private OnClickListener accInClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			scan2DimentionCode();
		}
	};

	/**
	 * 显示转出账户列表
	 */
	private void showAccOutListDialog() {
		List<Map<String, Object>> accoutList = TranDataCenter.getInstance()
				.getAccountOutList();
		if (StringUtil.isNullOrEmpty(accoutList)) {// 如果没有符合要求的数据 弹框提示
			String message = this.getString(R.string.query_no_fit_out);
			BaseDroidApp.getInstanse().createDialog(null, message);
			return;
		}
		accOutListView = new ListView(context);
		accOutListView.setFadingEdgeLength(0);
		accOutListView.setScrollingCacheEnabled(false);
		AccOutListAdapter outadapter = new AccOutListAdapter(context,
				accoutList);
		accOutListView.setAdapter(outadapter);
		accOutListView.setOnItemClickListener(clickAccOutListItem);
		// 弹出转出转换列表框
		BaseDroidApp.getInstanse().showMobileTranoutDialog(accOutListView,
				backListener);
	}

	/**
	 * 转出账户列表ListView的监听事件
	 */
	private OnItemClickListener clickAccOutListItem = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
					.getAccountOutList().get(position);
			// 存储转出账户信息
			addOutforMap = accOutInfoMap;
			// 通讯查询详情
			String accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
			String accountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
			String accountNumber = (String) accOutInfoMap
					.get(Comm.ACCOUNTNUMBER);
			// 根据账户类型 查询不同接口
			if (ConstantGloble.ACC_TYPE_GRE.equals(accountType)
				||ConstantGloble.ZHONGYIN.equals(accountType)) {// 信用卡 查询信用卡详情
				// 设置查询接口和条件
				// requestForCrcdDetail(accountId);
				requestForOutCrcdCurrency(accountNumber);
			} else {// 其他类型卡
				requestForAccountDetail(accountId);
			}
		}
	};

	/**
	 * 弹出框的返回按钮监听
	 */
	private OnClickListener backListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
		}
	};

	/**
	 * 显示转出账户列表详情
	 */
	private void showAccOutDetailView() {
		isOutChoose = true;
		BaseDroidApp.getInstanse().dismissMessageDialog();
		accOutDetailLayout = (LinearLayout) mInflater.inflate(
				R.layout.dept_acc_out_list_detail, null);

		mAccOutLl.removeAllViews();
		mAccOutLl.addView(accOutDetailLayout);
		mAccOutLl.setVisibility(View.VISIBLE);
		mAccOutListLayout.setVisibility(View.GONE);
		newAddTranOutBtn.setVisibility(View.GONE);
		showAccOutListItemData(accOutDetailLayout);// 显示转出账户条目的详细信息

		isTranOutFirst = false;
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
		accountTypeTv.setText(StringUtil.isNullChange(strAccountT));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				accountTypeTv);

		nickNameTv.setText(StringUtil.isNullChange((String) accOutInfoMap
				.get(Comm.NICKNAME)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				nickNameTv);
		String accoutNumber = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		accountNumberTypeTv
				.setText(StringUtil.getForSixForString(accoutNumber));

		Map<String, Object> resultMap = TranDataCenter.getInstance()
				.getCurrOutDetail();
		// 根据卡类型 获取的数据字段不一样
		// 如果是长城信用卡
		if (strAccountType.equals(ConstantGloble.ACC_TYPE_GRE)) {
			Map<String, String> detailMap = new HashMap<String, String>();
			List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
			detailList = (List<Map<String, String>>) resultMap
					.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
			detailMap = detailList.get(0);
			availableBalance = (String) detailMap
					.get(Crcd.CRCD_LOANBALANCELIMIT);
			currencyCode = ConstantGloble.BOCINVT_CURRENCY_RMB;
			// String currentflag = (String) detailMap
			// .get(Crcd.CRCD_CURRENTBALANCEFLAG);
			// if (StringUtil.isNull(currentflag)) {
			cashTv.setText(StringUtil.parseStringCodePattern(currencyCode,
					availableBalance, 2));
			// } else {
			// if (currentflag.equals(ConstantGloble.ACC_CURRENTINDEX_VALUE)) {
			// String left = this.getResources().getString(R.string.left);
			// cashTv.setText(StringUtil.parseStringCodePattern(
			// currencyCode, availableBalance, 2) + left);
			// } else if (currentflag
			// .equals(ConstantGloble.LOAN_CURRENTINDEX_VALUE)) {
			// String own = this.getResources().getString(R.string.own);
			// cashTv.setText(StringUtil.parseStringCodePattern(
			// currencyCode, availableBalance, 2) + own);
			// } else {
			// cashTv.setText(StringUtil.parseStringCodePattern(
			// currencyCode, availableBalance, 2));
			// }
			// }

		} else {
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
				currencyCode = (String) detialList.get(0)
						.get(Comm.CURRENCYCODE);
				// 可用余额
				availableBalance = detialList.get(0)
						.get(Dept.AVAILABLE_BALANCE);
			}
			cashTv.setText(StringUtil.parseStringPattern(availableBalance, 2));
		}

		currencyCodeTv.setText(LocalData.Currency.get(currencyCode));
	}

	/**
	 * 显示转入账户详细信息
	 */
	private void showAccInDetailView() {
		BaseDroidApp.getInstanse().dismissMessageDialog();
		accInDetailLayout = (LinearLayout) mInflater.inflate(
				R.layout.dept_acc_in_list_detail, null);
		mAccInLl.removeAllViews();
		mAccInLl.addView(accInDetailLayout);
		mAccInLl.setVisibility(View.VISIBLE);
		mAccInListLayout.setVisibility(View.GONE);
		newAddTranInBtn.setVisibility(View.GONE);
		showAccInListItemData();
	}

	/**
	 * 显示转出账户条目的详细信息
	 */
	/**
	 * 显示转入账户条目的详细信息(非关联账户)
	 */
	private void showAccInListItemData() {
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		if (accInInfoMap == null) {
			return;
		}
		// 存储转入账户信息
		TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);

		TextView accountTypeTv = (TextView) accInDetailLayout
				.findViewById(R.id.tv_acc_in_item_name);
		TextView accNameTv = (TextView) accInDetailLayout
				.findViewById(R.id.tv_acc_in_item_type);
		TextView accountNumberTv = (TextView) accInDetailLayout
				.findViewById(R.id.tv_acc_in_item_no);

		accNameTv.setText(StringUtil.isNullChange((String) accInInfoMap
				.get(Tran.TRANS_BANKNAME_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				accNameTv);
		String strAccountType = (String) accInInfoMap
				.get(Tran.TRANS_ACCOUNTNAME_RES);
		accountTypeTv.setText(StringUtil.isNullChange(strAccountType));
		String accountNumberStr = (String) accInInfoMap
				.get(Tran.TRANS_ACCOUNTNUMBER_RES);
		accountNumberTv
				.setText(StringUtil.getForSixForString(accountNumberStr));
	}

	// ///////////////////////////////////////////////////////////////////////////
	/**
	 * 调用接口：PsnCommonQueryAllChinaBankAccount 请求给定类型转出账户列表
	 * */
	public void requestTranoutAccountList() {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.COMMONQUERYALLCHINABANKACCOUNT);

		String[] accountTypeArr = new String[] { ConstantGloble.ACC_TYPE_ORD,
				ConstantGloble.ACC_TYPE_BRO, ConstantGloble.ACC_TYPE_RAN,
				ConstantGloble.ACC_TYPE_GRE,ConstantGloble.ZHONGYIN};// 601 转出增加103
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.ACCOUNTTYPE_REQ, accountTypeArr);
		biiRequestBody.setParams(map);

		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestTranoutAccountListCallBack");
	}

	/**
	 * PsnCommonQueryAllChinaBankAccount接口的回调方法，返回结果 *
	 */
	@SuppressWarnings("unchecked")
	public void requestTranoutAccountListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody
				.getResult();

		TranDataCenter.getInstance().setAccountOutList(result);

		showAccOutListDialog();
	}

	/**
	 * 查询信用卡详情 PsnCrcdQueryAccountDetail
	 * 
	 * @param accountId
	 */
	private void requestForCrcdDetail(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.CRCDQUERYACCOUNTDETAIL);
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_ID, accountId);
		// 默认设置为人民币元
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY,
				ConstantGloble.BOCINVT_CURRENCY_RMB);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCrcdDetailCallBack");
	}

	/**
	 * 查询信用卡详情返回 PsnCrcdQueryAccountDetail
	 * 
	 * @param resultObj
	 */
	public void requestForCrcdDetailCallBack(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
		detailList = (List<Map<String, String>>) resultMap
				.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		if (StringUtil.isNullOrEmpty(detailList)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		TranDataCenter.getInstance().setCurrOutDetail(resultMap);
		TranDataCenter.getInstance().setAccOutInfoMap(addOutforMap);
		// 显示转出账户详情界面
		BaseHttpEngine.dissMissProgressDialog();
		// 首先判断信用卡余额
		// String currentBalance = (String) resultMap
		// .get(Tran.CRCD_CURRENTBALANCE);
		// double balance = Double.parseDouble(currentBalance);
		// if (balance >= 0) {
		// BaseDroidApp.getInstanse().createDialog(null,
		// R.string.trans_acc_out_crad_msg);
		// return;
		// }
		showAccOutDetailView();
	}

	/**
	 * 查询其他卡（除信用卡）详情 PsnAccountQueryAccountDetail
	 * 
	 * @param accountId
	 */
	private void requestForAccountDetail(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.ACCOUNTDETAIL);
		paramsmap.put(Tran.ACC_ACCOUNTID_REQ, accountId);
		biiRequestBody.setParams(paramsmap);

		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestForAccountDetailCallBack");
	}

	/**
	 * 查询其他卡（除信用卡）详情 PsnAccountQueryAccountDetail 返回
	 * 
	 * @param resultObj
	 */
	public void requestForAccountDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setCurrOutDetail(resultMap);
		// 显示转出账户详情界面
		TranDataCenter.getInstance().setAccOutInfoMap(addOutforMap);
		showAccOutDetailView();
		// 组装数据
	}

	/**
	 * 手续费试算
	 */
	private void requestForTransferCommissionCharge(String serviceId) {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		String fromAccountId = (String) accOutInfoMap.get(Tran.ACCOUNTID_RES);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String toAccountId = (String) accInInfoMap.get(Tran.ACCOUNTID_RES);
		String payeeactNo = (String) accInInfoMap.get(Tran.ACCOUNTNUMBER_RES);
		String payeeName = (String) accInInfoMap.get(Tran.ACCOUNTNAME_RES);

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANSFER_COMMISSIONCHARGE_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.SERVICEID_REQ, serviceId);
		map.put(Tran.RELTRANS_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.RELTRANS_TOACCOUNTID_REQ, toAccountId);
		map.put(Tran.RELTRANS_CURRENCY_REQ, currencyCode);
		map.put(Tran.RELTRANS_AMOUNT_REQ, amount);
		map.put(Tran.RELTRANS_CASHREMIT_REQ, cashRemitCode);
		map.put(Tran.RELTRANS_REMARK_REQ, memo);
		map.put(Tran.RELTRANS_PAYEEACTNO_REQ, payeeactNo);
		map.put(Tran.RELTRANS_PAYEENAME_REQ, payeeName);
		map.put(Tran.NOTIFYID, null);

		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestForTransferCommissionChargeCallBack");
	}

	/**
	 * 手续费试算返回
	 * 
	 * @param resultObj
	 */
	public void requestForTransferCommissionChargeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setCommissionChargeMap(result);
		Intent nextIntent = new Intent(this, NoRelConfirmInfoActivity1.class);
		nextIntent.putExtra(TRANS_TYPE, TRANTYPE_NOREL_BANKIN);
		nextIntent.putExtra(SENCOND_MENU_KEY, TWO_DIMEN_TRAN);
		nextIntent.putExtra(Tran.MANAGE_PRE_transMode_RES, tranMode);
		startActivity(nextIntent);
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 行内转账
		requestGetSecurityFactor(ConstantGloble.PB031);
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Map<String, String> userInputMap = TranDataCenter
								.getInstance().getUserInputMap();
						combineId = BaseDroidApp.getInstanse()
								.getSecurityChoosed();
						userInputMap.put(Tran._COMBINID_REQ, combineId);
						TranDataCenter.getInstance().setUserInputMap(
								userInputMap);
						requestForTransBocTransferVerify();
					}
				});
	}

	/**
	 * PsnTransBocTransferVerify中行内转账预交易 req 中国银行行内转账预交易
	 * 接口:PsnTransBocTransferVerify
	 * 
	 */
	// @Override
	public void requestForTransBocTransferVerify() {
		// 转出账户ID
		String fromAccountId = "";
		// 收款人ID
		String payeeId = "";
		// 转入账户账号
		String payeeActno = "";
		// 收款人姓名
		String payeeName = "";
		// 汇款用途
		String remittanceInfo = "";
		// 转账金额
		String amount = "";
		// 收款人手机号
		String payeeMobile = "";
		// 备注
		String remark = "";
		// 执行日期
		String executeDate = "";
		// 起始日期
		String startDate = "";
		// 结束日期
		String endDate = "";
		// 周期
		String cycleSelect = "";
		// 执行类型,"0","1","2"
		String executeType = ConstantGloble.NOWEXE;
		// 币种
		String currency = ConstantGloble.PRMS_CODE_RMB;

		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		fromAccountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		payeeId = (String) accInInfoMap.get(Comm.ACCOUNT_ID);
		payeeActno = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		payeeName = (String) accInInfoMap.get(Comm.ACCOUNT_NAME);
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		amount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);
		payeeMobile = userInputMap.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);
		remark = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);

		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANSBOCTRANSFERVERIFY);
		// 设置交易码
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

		Map<String, String> map = new HashMap<String, String>();
		// 转出账户ID
		map.put(Tran.FROMACCOUNTID_REQ, fromAccountId);
		// 收款人ID
		map.put(Tran.PAYEEID_REQ, payeeId);
		// 转入账户账号
		map.put(Tran.PAYEEACTNO_REQ, payeeActno);
		// 收款人姓名
		map.put(Tran.PAYEENAME_REQ, payeeName);
		// 汇款用途
		map.put(Tran.REMITTANCEINFO_REQ, remittanceInfo);
		// 转账金额
		map.put(Tran.AMOUNT_REQ, amount);
		// 收款人手机号
		map.put(Tran.PAYEEMOBILE_REQ, payeeMobile);
		// 备注
		map.put(Tran.REMARK_REQ, remark);
		// 执行日期
		map.put(Tran.EXECUTEDATE_REQ, executeDate);
		// 起始日期
		map.put(Tran.STARTDATE_REQ, startDate);
		// 结束日期
		map.put(Tran.ENDDATE_REQ, endDate);
		// 周期
		map.put(Tran.CYCLESELECT_REQ, cycleSelect);
		// 执行类型,"0","1","2"
		map.put(Tran.EXECUTETYPE_REQ, executeType);
		// 币种
		map.put(Tran.CURRENCY_REQ, currency);
		// 安全因子组合id
		map.put(Tran._COMBINID_REQ, combineId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForTransBocTransferVerifyCallBack");
	}

	/**
	 * PsnTransBocTransferVerify中行内转账预交易 req 中国银行行内转账预交易 返回
	 * 
	 * @param resultObj
	 */
	public void requestForTransBocTransferVerifyCallBack(Object resultObj) {
		// BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();

		TranDataCenter.getInstance().setNoRelBankInCallBackMap(result);
		requestForTransferCommissionCharge(ConstantGloble.PB031);
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		switch (Integer.parseInt(tranMode)) {
		case 1:// 预约日期执行
			Intent intent1 = new Intent(TwoDimenSacnResultActivity.this,
					PreDateExeActivity1.class);
			intent1.putExtra(ConstantGloble.DATE_TIEM, dateTime);
			intent1.putExtra(TRANS_TYPE, TRANTYPE_NOREL_BANKIN);
			intent1.putExtra(SENCOND_MENU_KEY, TWO_DIMEN_TRAN);
			startActivity(intent1);
			break;
		case 2:// 预约周期执行
			Intent intent2 = new Intent(TwoDimenSacnResultActivity.this,
					PrePeriodExeActivity1.class);
			intent2.putExtra(ConstantGloble.DATE_TIEM, dateTime);
			intent2.putExtra(TRANS_TYPE, TRANTYPE_NOREL_BANKIN);
			intent2.putExtra(SENCOND_MENU_KEY, TWO_DIMEN_TRAN);
			startActivity(intent2);
			break;

		default:
			break;
		}
	}

	/**
	 * 查询转出信用卡币种
	 * 
	 * @param accountNumber
	 */
	private void requestForOutCrcdCurrency(String accountNumber) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.CREDITCARDCURRENCYQUERY);

		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.ACCOUNTNUMBER_RES, accountNumber);

		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestForOutCrcdCurrencyCallBack");
	}

	@SuppressWarnings("unchecked")
	public void requestForOutCrcdCurrencyCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		Map<String, Object> accOutInfoMap = addOutforMap;
		String accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		requestForCrcdDetail(accountId);
	}
}