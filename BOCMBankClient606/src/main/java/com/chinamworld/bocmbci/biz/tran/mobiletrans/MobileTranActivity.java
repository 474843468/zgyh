package com.chinamworld.bocmbci.biz.tran.mobiletrans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
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
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

@SuppressLint("InflateParams")
public class MobileTranActivity extends TranBaseActivity {
	private static final String TAG = MobileTranActivity.class.getSimpleName();
	
	public static final int PICK_CONTACT = 1;
	private Context context = this;
	private LayoutInflater inflater = null;
	private View container;

	/** 转出账户控件 */
	private RelativeLayout mAccOutLl = null;
	/** 转出账户列表Layout */
	private LinearLayout mAccOutListLayout = null;
	/** 转入账户列表Layout */
	/** 新增关联账户 */
	private Button newAddTranOutBtn;
	/** 转出账户详情 布局 */
	private LinearLayout accOutDetailLayout = null;
	// /** 转出账户详情 布局 */
	/** 转出账户listview */
	private ListView accOutListView = null;
	/** 转入账户listview */

	private boolean isTranOutFirst = true;
	/** 转出账户可以用余额 */
	private String availableBalance;

	/** 下一步 */
	private Button nextBtn;
	/** 绑定id */
	private String combineId;
	/** 转账金额 */
	private EditText amountEt;
	private String amount;
	/** 附言 */
	private EditText remarkEt;
	private String remark;
	/** 收款人姓名 */
	private EditText payyNameEt;
	private String payeeName;
	/** 收款人手机号 */
	private EditText payeeMobileEt;
	private String payeeMobile;
	/** 转入账户控件 */
	private RelativeLayout mAccInLl = null;
	/** 导入通讯录 */
	private Button openContacts;

	private boolean isOutChoose = false;

	/** 是否从我要转账模块跳过来 */
	private int fromMyTran = 0;

	private String currencyCode;

	private String isHaveAcct;
	private Map<String, Object> addOutforMap;
	/** 其他模块跳入标识 */
	private int otherPartJumpFlag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.mobile_trans));
		container = mInflater.inflate(R.layout.tran_mobile_activity1, null);
		inflater = LayoutInflater.from(this);
		tabcontent.removeAllViews();
		tabcontent.addView(container);

		tranTypeFlag = TRANTYPE_MOBILE_TRAN;
		setLeftSelectedPosition("tranManager_2");
		initView();

		showOutDetailView();
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(MobileTranActivity.this, MobileTransThirdMenu.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent = new Intent();
			intent.setClass(MobileTranActivity.this, MobileTransThirdMenu.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("tranManager_2");
//	}

	private void initView() {
		// 转出账户布局
		mAccOutLl = (RelativeLayout) container.findViewById(R.id.ll_acc_out_mytransfer);
		mAccOutListLayout = (LinearLayout) container.findViewById(R.id.ll_acc_out_list_layout);
		mAccOutLl.setOnClickListener(accOutClicklistener);
		newAddTranOutBtn = (Button) container.findViewById(R.id.dept_add_new_tranout_btn);

		mAccInLl = (RelativeLayout) container.findViewById(R.id.ll_acc_in_mytransfer);
		mAccInLl.setEnabled(false);
		payyNameEt = (EditText) findViewById(R.id.tran_in_name);
		payeeMobileEt = (EditText) findViewById(R.id.tran_in_mobile);
		// 限制收款人姓名输入框长度
		EditTextUtils.setLengthMatcher(MobileTranActivity.this, payyNameEt, 60);
		amountEt = (EditText) findViewById(R.id.et_commBoc_transferAmount_tranSeting);
		remarkEt = (EditText) findViewById(R.id.et_commBoc_remark_tranSeting);
		TextView tv_for_amount = (TextView) findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(amountEt, tv_for_amount);
		EditTextUtils.setLengthMatcher(MobileTranActivity.this, remarkEt, 20);// 手机号转账附言改为20字符

		openContacts = (Button) findViewById(R.id.btn_add_contract);
		openContacts.setOnClickListener(openContractsListener);
		nextBtn = (Button) container.findViewById(R.id.tran_mobile_next);
		nextBtn.setOnClickListener(nextListener);
		// 发起新转账
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (isTranOutFirst == true) {
				// return;
				// }
				container = mInflater.inflate(R.layout.tran_mobile_activity1, null);
				inflater = LayoutInflater.from(MobileTranActivity.this);
				tabcontent.removeAllViews();
				tabcontent.addView(container);
				initView();
				resetFlag();
			}
		});
	}

	private void resetFlag() {
		isOutChoose = false;
		isTranOutFirst = true;
	}

	/**
	 * 显示转出账户详情 如果是从我要转账模块过来 直接显示
	 */
	private void showOutDetailView() {
		fromMyTran = getIntent().getIntExtra(ConstantGloble.TRAN_MOBILE_FLAG, 0);
		// 是否可以选择转出账户的标志
		otherPartJumpFlag = getIntent().getIntExtra(ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
		if (fromMyTran == ConstantGloble.TRAN_TO_MOBILE) {
			mTopRightBtn.setVisibility(View.GONE);
			isTranOutFirst = false;
			mAccOutLl.setEnabled(false);
			showAccOutDetailView();
		}
//		else{	
//			requestTranoutAccountListfirst();
//		}
	}

	/**
	 * 转出账户按钮点击事件
	 */
	private OnClickListener accOutClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 点击转出账户之后 将accoutInfoMap置空
			// accOutInfoMap = null;
			// 发送通讯 请求转出账户列表
			if (isTranOutFirst) {
				requestTranoutAccountList();
			} else {
				showAccOutListDialog();
			}
		}
	};
	/***
	 * 导入通讯录
	 */
	private OnClickListener openContractsListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO打开系统联系人界面
			// Intent intent = new Intent(Intent.ACTION_PICK);
			// intent.setType(ContactsContract.Contacts.CONTENT_TYPE);//
			// startActivityForResult(intent, PICK_CONTACT);
			Intent it = new Intent();
			it.setAction(Intent.ACTION_GET_CONTENT);
			it.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
			startActivityForResult(it, PICK_CONTACT);
		}
	};

	/**
	 * 显示转出账户列表
	 */
	private void showAccOutListDialog() {
		List<Map<String, Object>> accoutList = TranDataCenter.getInstance().getAccountOutList();
		if (StringUtil.isNullOrEmpty(accoutList)) {// 如果没有符合要求的数据 弹框提示
			BaseDroidApp.getInstanse().createDialog(null, this.getResources().getString(R.string.query_no_fit_out));
			return;
		}
		accOutListView = new ListView(context);
		accOutListView.setFadingEdgeLength(0);
		accOutListView.setScrollingCacheEnabled(false);
		AccOutListAdapter outadapter = new AccOutListAdapter(context, accoutList);
		accOutListView.setAdapter(outadapter);
		accOutListView.setOnItemClickListener(clickAccOutListItem);
		// 弹出转出转换列表框
		BaseDroidApp.getInstanse().showMobileTranoutDialog(accOutListView, backListener);
	}

	/**
	 * 转出账户列表ListView的监听事件
	 */
	private OnItemClickListener clickAccOutListItem = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
			mAccInLl.setEnabled(true);
			Map<String, Object> accOutInfoMap = TranDataCenter.getInstance().getAccountOutList().get(position);
			// 存储转出账户信息
			addOutforMap = accOutInfoMap;
			// 通讯查询详情
			String accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
			String accountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
			String accountNumber = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
			// 根据账户类型 查询不同接口
			if (ConstantGloble.ACC_TYPE_GRE.equals(accountType)
				||ConstantGloble.ZHONGYIN.equals(accountType)) {// 601 增加103
				// 信用卡 查询信用卡详情
				// 设置查询接口和条件
				// requestForCrcdDetail(accountId);
				requestForOutCrcdCurrency(accountNumber);
			} else {// 其他类型卡
				requestForAccountDetail(accountId);
			}
		}
	};

	/**
	 * 显示转出账户列表详情
	 */
	private void showAccOutDetailView() {
		isOutChoose = true;
		BaseDroidApp.getInstanse().dismissMessageDialog();
		accOutDetailLayout = (LinearLayout) inflater.inflate(R.layout.dept_acc_out_list_detail, null);

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
	@SuppressWarnings("unchecked")
	private void showAccOutListItemData(View view) {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance().getAccOutInfoMap();
		if (accOutInfoMap == null) {
			return;
		}
		TextView accountTypeTv = (TextView) view.findViewById(R.id.tv_accountType_accOut_list_detail);
		TextView nickNameTv = (TextView) view.findViewById(R.id.tv_nickName_accOut_list_detail);
		TextView accountNumberTypeTv = (TextView) view.findViewById(R.id.tv_accountNumber_accOut_list_detail);
		TextView currencyCodeTv = (TextView) view.findViewById(R.id.tv_currey_accOut_list_detail);
		TextView cashTv = (TextView) view.findViewById(R.id.tv_lastprice_accOut_list_detail);

		String strAccountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
		String strAccountT = LocalData.AccountType.get(strAccountType);
		accountTypeTv.setText(strAccountT);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, accountTypeTv);

		nickNameTv.setText((String) accOutInfoMap.get(Comm.NICKNAME));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, nickNameTv);
		String accoutNumber = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		accountNumberTypeTv.setText(StringUtil.getForSixForString(accoutNumber));
		Map<String, Object> resultMap = TranDataCenter.getInstance().getCurrOutDetail();
		// 根据卡类型 获取的数据字段不一样
		// 如果是长城信用卡
		if (strAccountType.equals(ConstantGloble.ACC_TYPE_GRE)
				||strAccountType.equals(ConstantGloble.ZHONGYIN)) {
			Map<String, String> detailMap = new HashMap<String, String>();
			List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
			detailList = (List<Map<String, String>>) resultMap.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
			detailMap = detailList.get(0);
			availableBalance = (String) detailMap.get(Crcd.CRCD_LOANBALANCELIMIT);
			currencyCode = ConstantGloble.BOCINVT_CURRENCY_RMB;
			// String currentflag = (String) detailMap.get(Crcd.CRCD_CURRENTBALANCEFLAG);
			// if (StringUtil.isNull(currentflag)) {
			cashTv.setText(StringUtil.parseStringCodePattern(currencyCode, availableBalance, 2));
			// } else {
			// if (currentflag.equals(ConstantGloble.ACC_CURRENTINDEX_VALUE)) {
			// String left = this.getResources().getString(R.string.left);
			// cashTv.setText(StringUtil.parseStringCodePattern(currencyCode, availableBalance, 2) + left);
			// } else if (
			// currentflag.equals(ConstantGloble.LOAN_CURRENTINDEX_VALUE)) {
			// String own = this.getResources().getString(R.string.own);
			// cashTv.setText(StringUtil.parseStringCodePattern(currencyCode, availableBalance, 2) + own);
			// } else {
			// cashTv.setText(StringUtil.parseStringCodePattern(currencyCode, availableBalance, 2));
			// }
			// }

		} else {
			List<Map<String, String>> detialList = (List<Map<String, String>>) resultMap.get(ConstantGloble.ACC_DETAILIST);
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
		}
		currencyCodeTv.setText(LocalData.Currency.get(currencyCode));
	}

	/**
	 * 弹出框的返回按钮监听
	 */
	private OnClickListener backListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
		}
	};

	private OnClickListener nextListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (StringUtil.isNullOrEmpty(TranDataCenter.getInstance().getAccOutInfoMap())) {
				// 如果没有选择转出账户，提示选择
				CustomDialog.toastInCenter(context, MobileTranActivity.this.getString(R.string.choose_outno_message));
				return;
			}
			if (!checkRMBMessage()) {
				return;
			};
			payeeName = payyNameEt.getText().toString().trim();
			payeeMobile = payeeMobileEt.getText().toString().trim();
			amount = amountEt.getText().toString().trim();
			remark = remarkEt.getText().toString().trim();
			// 校验
			if (!isOutChoose) {
				String message = getString(R.string.choose_outno_message);
				CustomDialog.toastInCenter(context, message);
				// BaseDroidApp.getInstanse().createDialog(null, R.string.choose_outno_message);
				return;
			}
			if (StringUtil.isNullOrEmpty(payeeName)) {
				BaseDroidApp.getInstanse().createDialog(null, R.string.choose_payeename);
				return;
			}
			boolean flag = judgeUserData();
			if (!flag) {
				return;
			}

			Map<String, String> userInputMap = new HashMap<String, String>();
			userInputMap.put(Tran.INPUT_PAYEE_NAME, payeeName);
			userInputMap.put(Tran.INPUT_PAYEE_TRANSFER_MOBILE, payeeMobile);
			userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
			userInputMap.put(Tran.INPUT_TRANSFER_REMARK, remark);
			TranDataCenter.getInstance().setUserInputMap(userInputMap);
			// 请求安全因子
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	};

	/**
	 * 判断用户输入数据
	 * 
	 * @return true 合格 false 不合格
	 */
	private boolean judgeUserData() {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();

		RegexpBean payeeMobileReg = new RegexpBean(getResources().getString(R.string.payeeMobile_str), payeeMobile, "shoujiH_01_15");
		RegexpBean transAmountReg = new RegexpBean(getResources().getString(R.string.reg_transferAmount), amount, "tranAmount");
		lists.add(payeeMobileReg);
		lists.add(transAmountReg);
		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}
		// 判断转出账户余额
		try{
		 if (Double.parseDouble(amount) > Double.parseDouble(availableBalance)) {
			 BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.amount_wrong_two));
			 return false;
		 }
		}catch(Exception ex){
			LogGloble.e("MobileTranActivity", ex.getMessage(), ex);
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case (PICK_CONTACT):
			if (resultCode == RESULT_OK) {
				String reslutName = null;
				String reslutMobile = null;
				Uri contactData = data.getData();
				if (contactData != null) {
					Cursor c = managedQuery(contactData, null, null, null, null);
					if(c != null && c.moveToFirst()){
						try {
							reslutName = c.getString(c.getColumnIndexOrThrow(People.NAME));
						} catch (Exception e) {
							LogGloble.e(TAG, e.getMessage(),e);
						}
						if (reslutName == null) {
							try {
								reslutName = c.getString(c.getColumnIndexOrThrow(People.DISPLAY_NAME));
							} catch (Exception e) {
								LogGloble.e(TAG, e.getMessage(),e);
							}
						}
						if (reslutMobile == null) {
							try {
								// 小米
								reslutMobile = c.getString(c.getColumnIndexOrThrow("data1"));
							} catch (Exception e) {
								LogGloble.e(TAG, e.getMessage(),e);
							}
						}
						if (reslutMobile == null) {
							try {
								reslutMobile = c.getString(c.getColumnIndexOrThrow(People.NUMBER));
							} catch (Exception e) {
								LogGloble.e(TAG, e.getMessage(),e);
							}
						}
					}
				}
				if (reslutName == null || reslutMobile == null) {
					payeeName = "";
					payeeMobile = "";
				}else{
					payeeName = reslutName;
					payeeMobile = reslutMobile;
				}
				payeeMobile = payeeMobile.replace(" ", "");
				payeeMobile = payeeMobile.replace("+86", "");
				String mobile = payeeMobileEt.getText().toString().trim();
				String name = payyNameEt.getText().toString().trim();
				// payeeName肯定不为空，payeeMobile可能为空
				// 402判断
				if (StringUtil.isNullOrEmpty(mobile) || StringUtil.isNullOrEmpty(name)
						|| StringUtil.isNullOrEmpty(payeeMobile) || StringUtil.isNullOrEmpty(payeeName)) {
					// 只要有一项为空，直接替换
					payeeMobileEt.setText(payeeMobile);
					if (!StringUtil.isNullOrEmpty(payeeMobile)) {
						payeeMobileEt.setSelection(payeeMobile.length());
					}
					payyNameEt.setText(payeeName);
					payyNameEt.setSelection(payeeName.length());
					return;
				}
				// 1.通讯录中收款人姓名与客户输入的收款人姓名不一致：您输入的收款人姓名与通讯录中不一致，是否要替换为通讯录中姓名？
				if (!StringUtil.isNullOrEmpty(name) && !name.equals(payeeName) && !StringUtil.isNullOrEmpty(mobile) && mobile.equals(payeeMobile)) {
					// 弹出对话框,是否取消
					BaseDroidApp.getInstanse().showErrorDialog(MobileTranActivity.this.getString(R.string.mobile_error_msg_1), R.string.cancle,
							R.string.confirm, new OnClickListener() {
								@Override
								public void onClick(View v) {
									switch (Integer.parseInt(v.getTag() + "")) {
									case CustomDialog.TAG_SURE:
										// 替换
										BaseDroidApp.getInstanse().dismissErrorDialog();
										payyNameEt.setText(payeeName);
										payyNameEt.setSelection(payeeName.length());
										break;
									case CustomDialog.TAG_CANCLE:
										// 取消操作
										BaseDroidApp.getInstanse().dismissErrorDialog();
										break;
									}
								}
							});
					return;
				}
				// 2.通讯录中收款人手机号与客户输入的收款人手机号不一致：您输入的收款人手机号码与通讯录中不一致，是否要替换为通讯录中手机号码？
				if (!StringUtil.isNullOrEmpty(mobile) && !mobile.equals(payeeMobile) && !StringUtil.isNullOrEmpty(name) && name.equals(payeeName)) {
					// 弹出对话框,是否取消
					BaseDroidApp.getInstanse().showErrorDialog(MobileTranActivity.this.getString(R.string.mobile_error_msg_2), R.string.cancle,
							R.string.confirm, new OnClickListener() {
								@Override
								public void onClick(View v) {
									switch (Integer.parseInt(v.getTag() + "")) {
									case CustomDialog.TAG_SURE:
										// 替换
										BaseDroidApp.getInstanse().dismissErrorDialog();
										payeeMobileEt.setText(payeeMobile);
										if (!StringUtil.isNullOrEmpty(payeeMobile)) {
											payeeMobileEt.setSelection(payeeMobile.length());
										}
										break;
									case CustomDialog.TAG_CANCLE:
										// 取消操作
										BaseDroidApp.getInstanse().dismissErrorDialog();
										break;
									}
								}
							});
					return;
				}
				// 3.通讯录中收款人姓名与手机号均与客户输入的不一致：您输入的收款人信息与通讯录中不一致，是否要替换为通讯录中信息？
				if (!StringUtil.isNullOrEmpty(name) && !name.equals(payeeName) && !StringUtil.isNullOrEmpty(mobile) && !mobile.equals(payeeMobile)) {
					// 弹出对话框,是否取消
					BaseDroidApp.getInstanse().showErrorDialog(
							MobileTranActivity.this.getString(R.string.mobile_error_msg_3), R.string.cancle,
							R.string.confirm, new OnClickListener() {
								@Override
								public void onClick(View v) {
									switch (Integer.parseInt(v.getTag() + "")) {
									case CustomDialog.TAG_SURE:
										// 替换
										BaseDroidApp.getInstanse().dismissErrorDialog();
										payeeMobileEt.setText(payeeMobile);
										if (!StringUtil.isNullOrEmpty(payeeMobile)) {
											payeeMobileEt.setSelection(payeeMobile.length());
										}
										payyNameEt.setText(payeeName);
										payyNameEt.setSelection(payeeName.length());
										break;
									case CustomDialog.TAG_CANCLE:
										// 取消操作
										BaseDroidApp.getInstanse().dismissErrorDialog();
										break;
									}
								}
							});
					return;
				}
			}
			break;
		}
	}

	// 获取联系人电话
	private String getContactPhone(Cursor cursor) {

		int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int phoneNum = cursor.getInt(phoneColumn);
		String phoneResult = "";
		// System.out.print(phoneNum);
		if (phoneNum > 0) {
			// 获得联系人的ID号
			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			String contactId = cursor.getString(idColumn);
			// 获得联系人的电话号码的cursor;
			Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
			// int phoneCount = phones.getCount();
			// allPhoneNum = new ArrayList<String>(phoneCount);
			if (phones.moveToFirst()) {
				// 遍历所有的电话号码
				for (; !phones.isAfterLast(); phones.moveToNext()) {
					int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
					int phone_type = phones.getInt(typeindex);
					String phoneNumber = phones.getString(index);
					switch (phone_type) {
					case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
					case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
						phoneResult = phoneNumber;
						break;
					}
					// allPhoneNum.add(phoneNumber);
				}
				if (!phones.isClosed()) {
					phones.close();
				}
			}
		}
		return phoneResult;
	}

	// ///////////////////////////////////////////////////////////////////////////
	/**
	 * 调用接口：PsnCommonQueryAllChinaBankAccount 请求给定类型转出账户列表
	 * */
	public void requestTranoutAccountList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.COMMONQUERYALLCHINABANKACCOUNT);
		// 手机号转账 只支持119 104  p601 增加 103
		String[] accountTypeArr = new String[] { ConstantGloble.ACC_TYPE_BRO, ConstantGloble.ACC_TYPE_GRE,ConstantGloble.ZHONGYIN };
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.ACCOUNTTYPE_REQ, accountTypeArr);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestTranoutAccountListCallBack");
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
		List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody.getResult();
		// ?是否需要添加提示信息，根据biiResponseBody的内容
		// 返回列表里面没有数据的时候 需要提示 modify by wjp
		// if (StringUtil.isNullOrEmpty(result)) {
		// return;
		// }
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
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY, ConstantGloble.BOCINVT_CURRENCY_RMB);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestForCrcdDetailCallBack");
	}

	/**
	 * 查询信用卡详情返回 PsnCrcdQueryAccountDetail
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestForCrcdDetailCallBack(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
		detailList = (List<Map<String, String>>) resultMap.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		if (StringUtil.isNullOrEmpty(detailList)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		TranDataCenter.getInstance().setCurrOutDetail(resultMap);
		TranDataCenter.getInstance().setAccOutInfoMap(addOutforMap);
		// 显示转出账户详情界面
		BaseHttpEngine.dissMissProgressDialog();
		// 首先判断信用卡余额
		// String currentBalance = (String) resultMap.get(Tran.CRCD_CURRENTBALANCE);
		// double balance = Double.parseDouble(currentBalance);
		// if (balance >= 0) {
		// BaseDroidApp.getInstanse().createDialog(null, R.string.trans_acc_out_crad_msg);
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
		HttpManager.requestBii(biiRequestBody, this, "requestForAccountDetailCallBack");
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
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		TranDataCenter.getInstance().setCurrOutDetail(resultMap);
		// 显示转出账户详情界面
		TranDataCenter.getInstance().setAccOutInfoMap(addOutforMap);
		showAccOutDetailView();
		// 组装数据
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestGetSecurityFactor(ConstantGloble.PB035);
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String, String> userInputMap = TranDataCenter.getInstance().getUserInputMap();
				combineId = BaseDroidApp.getInstanse().getSecurityChoosed();
				userInputMap.put(Tran._COMBINID_REQ, combineId);
				TranDataCenter.getInstance().setUserInputMap(userInputMap);
				// 请求手机号转账预交易
				requestMobileTransferPre();
			}
		});
	}

	/**
	 * 手机号转账预交易
	 */
	private void requestMobileTransferPre() {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance().getAccOutInfoMap();
		String fromAccountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);

		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.PSN_MOBILE_TRANSFERPRE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.TRANS_BOCNATIONAL_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.EXECUTETYPE_REQ, ConstantGloble.NOWEXE);
		map.put(Tran.TRANS_BOCNATIONAL_AMOUNT_REQ, amount);
		map.put(Tran.TRANS_BOCNATIONAL_PAYEEMOBILE_REQ, payeeMobile);
		map.put(Tran.TRANS_PAYEENAME_REQ, payeeName);
		map.put(Tran.TRANS_BOCNATIONAL_REMARK_REQ, remark);
		map.put(Tran.TRANS_BOCNATIONAL_CURRENCY_REQ, ConstantGloble.PRMS_CODE_RMB);
		map.put(Tran.TRANS_BOCNATIONAL__COMBINID_REQ, combineId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestMobileTransferPreCallBack");
	}

	/**
	 * 手机号预交易返回
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestMobileTransferPreCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		isHaveAcct = (String) resultMap.get(Tran.IS_HAVEACCT);
		TranDataCenter.getInstance().setMobileTranCallBackMap(resultMap);

		// TODO 如果为绑定需要查询手续费
		if (isHaveAcct.equals(ConstantGloble.IS_HAVE_ACC_1)) {
			requestForTransferCommissionCharge(ConstantGloble.PB035);
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			Intent intent = new Intent();
			intent.setClass(this, MobileTranConfirmActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * 行内手续费试算
	 */
	private void requestForTransferCommissionCharge(String serviceId) {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance().getAccOutInfoMap();
		String fromAccountId = (String) accOutInfoMap.get(Tran.ACCOUNTID_RES);
		// Map<String, Object> accInInfoMap = TranDataCenter.getInstance().getAccInInfoMap();
		// String toAccountId = (String) accInInfoMap.get(Tran.ACCOUNTID_RES);
		String toAccountId = "";// TODO

		// payeeactNo 预交易返回数据
		Map<String, Object> mobilePreMap = TranDataCenter.getInstance().getMobileTranCallBackMap();
		String payeeActNo = (String) mobilePreMap.get(Tran.PAYEEACTNO_REQ);
		// String payeeactNo = (String) accInInfoMap.get(Tran.ACCOUNTNUMBER_RES);
		// String payeeName = (String) accInInfoMap.get(Tran.ACCOUNTNAME_RES);

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANSFER_COMMISSIONCHARGE_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.SERVICEID_REQ, serviceId);
		map.put(Tran.RELTRANS_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.RELTRANS_TOACCOUNTID_REQ, toAccountId);
		map.put(Tran.RELTRANS_CURRENCY_REQ, currencyCode);
		map.put(Tran.RELTRANS_AMOUNT_REQ, amount);
		map.put(Tran.RELTRANS_CASHREMIT_REQ, ConstantGloble.CASHREMIT_00);
		map.put(Tran.RELTRANS_REMARK_REQ, remark);
		map.put(Tran.RELTRANS_PAYEEACTNO_REQ, payeeActNo);
		map.put(Tran.RELTRANS_PAYEENAME_REQ, payeeName);
		map.put(Tran.NOTIFYID, null);
		
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestForTransferCommissionChargeCallBack");
	}

	/**
	 * 行内手续费试算返回
	 * 
	 * @param resultObj
	 */
	public void requestForTransferCommissionChargeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		TranDataCenter.getInstance().setCommissionChargeMap(result);

		Intent intent = new Intent();
		intent.setClass(this, MobileTranConfirmActivity.class);
		startActivity(intent);
	}

	private void requestForOutCrcdCurrency(String accountNumber) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.CREDITCARDCURRENCYQUERY);

		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.ACCOUNTNUMBER_RES, accountNumber);

		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestForOutCrcdCurrencyCallBack");
	}

	@SuppressWarnings("unchecked")
	public void requestForOutCrcdCurrencyCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		Map<String, Object> accOutInfoMap = addOutforMap;
		String accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		requestForCrcdDetail(accountId);
	}
	
	
	public void requestTranoutAccountListfirst() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.COMMONQUERYALLCHINABANKACCOUNT);
		// 手机号转账 只支持119 104  p601 增加 103
		String[] accountTypeArr = new String[] { ConstantGloble.ACC_TYPE_BRO, ConstantGloble.ACC_TYPE_GRE,ConstantGloble.ZHONGYIN };
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.ACCOUNTTYPE_REQ, accountTypeArr);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestTranoutAccountListfirstCallBack");
	}

	/**
	 * PsnCommonQueryAllChinaBankAccount接口的回调方法，返回结果 *
	 */
	@SuppressWarnings("unchecked")
	public void requestTranoutAccountListfirstCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody.getResult();
		// ?是否需要添加提示信息，根据biiResponseBody的内容
		// 返回列表里面没有数据的时候 需要提示 modify by wjp
		// if (StringUtil.isNullOrEmpty(result)) {
		// return;
		// }
		TranDataCenter.getInstance().setAccountOutList(result);
		Map<String, Object> accOutInfoMap = result.get(0);

		// 存储转出账户信息
		addOutforMap = accOutInfoMap;
		// 通讯查询详情
		String accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		String accountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
		String accountNumber = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		// 根据账户类型 查询不同接口
		if (ConstantGloble.ACC_TYPE_GRE.equals(accountType)
			||ConstantGloble.ZHONGYIN.equals(accountType)) {// 601 增加103
			// 信用卡 查询信用卡详情
			// 设置查询接口和条件
			// requestForCrcdDetail(accountId);
			requestForOutCrcdCurrency(accountNumber);
		} else {// 其他类型卡
			requestForAccountDetail(accountId);
		}
	}
}