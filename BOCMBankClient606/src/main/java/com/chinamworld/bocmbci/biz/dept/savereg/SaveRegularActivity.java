package com.chinamworld.bocmbci.biz.dept.savereg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.adapter.AccInListAdapter;
import com.chinamworld.bocmbci.biz.dept.adapter.AccOutListAdapter;
import com.chinamworld.bocmbci.biz.dept.myreg.MyRegSaveCheckOutConfirmActivity;
import com.chinamworld.bocmbci.biz.investTask.BocInvestTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.boc.ModelBoc;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.interfacemodule.IActionTwo;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

import org.w3c.dom.Text;

/**
 * 我要存定期 [整存整取,定活两便,一天通知存款,七天通知存款]
 * 
 * @author Administrator 修改 luqp 2015年11月23日11:05:17
 * 
 */
@SuppressLint("InflateParams")
public class SaveRegularActivity extends DeptBaseActivity {
	private static final String TAG = "SaveRegularActivity";

	/** 一天通知存款 */
	private static final String ONE_DAY_FLAG = "1";
	/** 七天通知存款 */
	private static final String SEVEN_DAY_FLAG = "7";
	/** 业务品种 1：一天通知存款 7：七天通知存款 */
	private String businessType = null;

	private Context context = this;

	private LinearLayout tabcontent;// 主Activity显示
	/** 转出账户控件 */
	private RelativeLayout mAccOutLl = null;
	/** 转入账户控件 */
	private RelativeLayout mAccInLl = null;
	/** 转出账户列表Layout */
	private LinearLayout mAccOutListLayout = null;
	/** 转入账户列表Layout */
	private LinearLayout mAccInListLayout = null;
	/** 新增转出账户 */
	private Button newAddTranOutBtn;
	/** 新增转入账户 */
	private Button newAddTranInBtn;
	/** 底部布局 */
	private LinearLayout bottomLayout = null;
	/** 转出账户详情 布局 */
	private LinearLayout accOutDetailLayout = null;
	/** 转出账户详情 布局 */
	private LinearLayout accInDetailLayout = null;
	/** 银行卡号 账号 */
	private String accountNumberStr = null;
	private LayoutInflater inflater = null;
	private View view = null;
	/** 转出账户listview */
	private ListView accOutListView = null;
	/** 转入账户listview */
	private ListView accInListView = null;

	private RelativeLayout rl_save_title = null;

	/** 新增整存整取 */
	private Button newAddBtn1;
	/** 新增定活两便 */
	private Button newAddBtn2;
	/** 新增一天通知存款 */
	private Button newAddBtn3;
	/** 新增七天通知存款 */
	private Button newAddBtn4;
	/** 下标题 */
	private TextView titleTv;
	/** 币种 */
	private String currencyCode = "";
	/** 钞汇标志 */
	private String cashRemit = null;
	/** 存款金额 */
	private String saveAmount = null;
	/** 存期 */
	private String saveTime = null;
	/** 附言 */
	private String attachMessage = null;
	/** 约定方式 */
	private String promiseWay = null;
	/** 转账金额 */
	private EditText saveMoneyEt = null;
	/** 附言 */
	private EditText attachMessageEt = null;
	/** 可用余额 */
	private String availableBalance;
	/** 区分新增转入 还是 新增转出 标示 1 为转出 2 为转入 */
	private int transFlag;
	/** 区分按钮点击事件 标示0为立即执行 标示1为预约日期执行 标示2为预约周期执行 */
	private int buttonFlag;
	/** list条目高度 */
	// private int itemHeight = 0;
	/** 当前货币 */
	private String curCurrencyName;

	private CheckBox checkBox;

	private boolean isTranOutFirst = true;
	private boolean isTranInFirst = true;
	private TextView tv_neiku;

	/** 当前选择的币种和对应钞汇标志数据 */
	private ArrayList<Map<String, Object>> currentCurrencyCashRemitList;
	/** 币种和对应钞汇标志数据 */
	private ArrayList<Map<String, Object>> currencyAndCashRemitList;
	/** 关联账户转账对应的币种和钞汇标志数据 */
	private List<String> currencyList;
	private List<String> cashRemitList;
	/** 续存的月存金额 */
	private String continueMonthSave = null;
	/** 续存金额 */
	private EditText continueSaveTv = null;
	/** 转入账户类型 */
	private String type;

	// ///////////////////////////// 跨省手续费
	/** 转入账户省联号 */
	private String outAccountIbkNum = null;
	/** 转出账户省联号 */
	private String inAccountIbkNum = null;
	// -----上送字段
	/** 转出账户ID */
	private String fromAccountId = null;
	/** 转入账户ID */
	private String toAccountId = null;
	/** 执行方式 */
	private String executeType = null;

	/** 定期存款-新增整存整取费用试算 确定 */
	private boolean isAgree = true;
	/** 存款类型标志 */
	private int typeFlag;

	private String saveMoney;
	/** 提示 */
	private TextView tv_prompt;

	// =================================================================
	// add by luqp 2016年3月21日 利率上浮1.3倍
	/** 用户登录信息区分 查询版还是理财版 10查询版 */
	private String segmentId = null;
	/** 查询版客户类型 :1 查询版老客户;3 查询版新客户（非电子卡签约）;2 查询版新客户（电子卡签约） */
	Map<String, Object> resultMap = null;
	/** 查询版客户类型 :1 查询版老客户;3 查询版新客户（非电子卡签约）;2 查询版新客户（电子卡签约） */
	private String qryCustType;
	/** 转出账户类型 */
	private String outAccountType = null;
	/** 转入账户类型 */
	private String inAccountType = null;
	/** 转出 电子卡账户标识 2|3：是 0：否 */
	private String accountCatalog = null;
	/** 人民币 TextView */
	private TextView renmingbiTv = null;
	/** 约定转存方式 */
	private TextView promiseType = null;
	/** 存期的value 如:put("三个月人行基准1.3倍", "3", false); */
	private String interestRateKey = null;
	/** 存期的Tag true false 如:put("三个月人行基准1.3倍", "3", false); */
	private Boolean isShowInterestRateTag;
	/** 利率上浮标示 */
	private int interestRateFlag;
	// 定利多&1.3倍提示信息
	private TextView tv_prompt_new = null;
	// ================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.save_reg);

		inflater = LayoutInflater.from(this);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this); // 加载布局
		view = inflater.inflate(R.layout.dept_savereg_activity, null);
		tabcontent.addView(view);

		newTranBtn = (Button) this.findViewById(R.id.ib_top_right_btn);
		newTranBtn.setText(this.getResources().getString(R.string.new_save));

		newTranBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), SaveRegularActivity.class);
				startActivity(intent);
				finish();
			}
		});
		newTranBtn.setVisibility(View.GONE);
		setLeftSelectedPosition("deptStorageCash_1");

		// add lqp 2015年11月10日16:07:15 判断是否开通投资理财!
		// BiiHttpEngine.showProgressDialogCanGoBack();
		BocInvestTask task = BocInvestTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction() {

			@Override
			public void SuccessCallBack(Object param) {
				/** 操作员信息 */
				resultMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
						.get(ConstantGloble.BIZ_LOGIN_DATA);
				// 查询版客户类型 :1 查询版老客户;3 查询版新客户（非电子卡签约）;2 查询版新客户（电子卡签约）
				qryCustType = (String) resultMap.get("qryCustType");
				// 用户登录信息区分 查询版还是理财版 10查询版
				segmentId = BaseDroidApp.getInstanse().getSegmentInfo();
				init();
			}
		},null);
	}

	/** 初始化控件 */
	private void init() {

		// newTranBtn.setVisibility(View.VISIBLE);
		// 转出账户布局
		mAccOutLl = (RelativeLayout) view.findViewById(R.id.ll_acc_out_mytransfer);
		mAccOutListLayout = (LinearLayout) view.findViewById(R.id.ll_acc_out_list_layout);
		mAccOutLl.setOnClickListener(accOutClicklistener);
		newAddTranOutBtn = (Button) view.findViewById(R.id.dept_add_new_tranout_btn);
		newAddTranOutBtn.setOnClickListener(addNewAcc);
		// 转入账户布局
		mAccInLl = (RelativeLayout) view.findViewById(R.id.ll_acc_in_mytransfer);
		mAccInListLayout = (LinearLayout) view.findViewById(R.id.ll_acc_in_list_layout);
		mAccInLl.setOnClickListener(accInClicklistener);
		newAddTranInBtn = (Button) view.findViewById(R.id.dept_add_new_tranin_btn);
		newAddTranInBtn.setOnClickListener(addAccInNoClicklistener);

		// 标题选择
		rl_save_title = (RelativeLayout) view.findViewById(R.id.rl_save_title);
		rl_save_title.setOnClickListener(titleClickListener);
		rl_save_title.setClickable(false);
		// 下标题
		titleTv = (TextView) view.findViewById(R.id.dept_save_title_tv);
		tv_neiku = (TextView) view.findViewById(R.id.tv_neiku);

		// add luqp 2016年2月1日 选择转入账户时不显示提示
		tv_prompt = (TextView) view.findViewById(R.id.choice_acc_prompt);
		tv_prompt.setVisibility(View.VISIBLE);

		bottomLayout = (LinearLayout) view.findViewById(R.id.dept_save_regular_bottom);
	}

	/** 显示转出账户条目的详细信息 */
	private void showAccOutListItemData(View view) {
		if (accOutInfoMap == null) {
			return;
		}
		TextView accountTypeTv = (TextView) view.findViewById(R.id.tv_accountType_accOut_list_detail);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accountTypeTv);
		TextView nickNameTv = (TextView) view.findViewById(R.id.tv_nickName_accOut_list_detail);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, nickNameTv);
		TextView accountNumberTypeTv = (TextView) view.findViewById(R.id.tv_accountNumber_accOut_list_detail);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accountNumberTypeTv);
		TextView currencyCodeTv = (TextView) accOutDetailLayout.findViewById(R.id.tv_currey_accOut_list_detail);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, currencyCodeTv);
		TextView cashTv = (TextView) accOutDetailLayout.findViewById(R.id.tv_lastprice_accOut_list_detail);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, cashTv);

		String strAccountType = LocalData.AccountType.get((String) accOutInfoMap.get(Comm.ACCOUNT_TYPE));
//		accountTypeTv.setText(strAccountType);

		nickNameTv.setText((String) accOutInfoMap.get(Comm.NICKNAME));
		// nickNameTv.setVisibility(View.GONE);//不显示账户别名
		String accoutNumber = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		accountNumberTypeTv.setText(StringUtil.getForSixForString(accoutNumber));
		accoutNumber = accoutNumber.substring(0, 4);
		if ("6216".equals(accoutNumber)) {
			accountTypeTv.setText("中银E财账户 ");
		} else {
			accountTypeTv.setText(strAccountType);
		}
		

		@SuppressWarnings("unchecked")
		List<Map<String, String>> detialList = (List<Map<String, String>>) DeptDataCenter.getInstance()
				.getCurDetailContent().get(ConstantGloble.ACC_DETAILIST);
		if (StringUtil.isNullOrEmpty(detialList)) {
			return;
		}
		boolean isHaveCNY = false;
		for (int i = 0; i < detialList.size(); i++) {
			String strCurrencyCode = (String) detialList.get(i).get(Comm.CURRENCYCODE);
			if ("001".equals(strCurrencyCode) || "CNY".equals(strCurrencyCode)) {
				currencyCodeTv.setText(LocalData.Currency.get(strCurrencyCode));
				// 可用余额
				availableBalance = detialList.get(i).get(Dept.AVAILABLE_BALANCE);
				cashTv.setText(StringUtil.parseStringPattern(availableBalance, 2));
				isHaveCNY = true;
				continue;
			}
		}
		if (!isHaveCNY) {
			String strCurrencyCode = (String) detialList.get(0).get(Comm.CURRENCYCODE);
			currencyCodeTv.setText(LocalData.Currency.get(strCurrencyCode));
			// 可用余额
			availableBalance = detialList.get(0).get(Dept.AVAILABLE_BALANCE);
			cashTv.setText(StringUtil.parseStringPattern(availableBalance, 2));
		}

		// 根据金额大小 判断下面通知存款按钮 是否显示
		// if (Double.parseDouble(availableBalance) <
		// ConstantGloble.NOTICE_SAVE_MINMONEY) {
		// newAddBtn3.setVisibility(View.INVISIBLE);
		// newAddBtn4.setVisibility(View.INVISIBLE);
		// } else {
		// newAddBtn3.setVisibility(View.VISIBLE);
		// newAddBtn4.setVisibility(View.VISIBLE);
		// }
	}

	/** 显示转入账户条目的详细信息 */
	private void showAccInListItemData(int position) {
		if (accInInfoMap == null) {
			return;
		}
		// 存储转入账户信息
		DeptDataCenter.getInstance().setAccInInfoMap(accInInfoMap);

		TextView accountTypeTv = (TextView) accInDetailLayout.findViewById(R.id.tv_acc_in_item_type);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accountTypeTv);
		TextView accNameTv = (TextView) accInDetailLayout.findViewById(R.id.tv_acc_in_item_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accNameTv);
		TextView accountNumberTv = (TextView) accInDetailLayout.findViewById(R.id.tv_acc_in_item_no);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accountNumberTv);

		String strAccountType = LocalData.AccountType.get((String) accInInfoMap.get(Comm.ACCOUNT_TYPE));
		accountTypeTv.setText(strAccountType);
		accountNumberStr = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		accountNumberTv.setText(StringUtil.getForSixForString(accountNumberStr));
		accInDetailLayout.setOnClickListener(accInClicklistener);
	}

	/**
	 * 初始化底部视图
	 * 
	 * @param view
	 * @param businessTypeFlag
	 *            存款类型 WHOLE_SAVE_BOTTOM 整存整取 REGULAR_RANDOM_BOTTOM 定活两便
	 *            NOTIFY_SAVE_BOTTOM 通知存款
	 */
	private void initBottomBtnlayout(final View view, final int businessTypeFlag) {
		// 立即执行
		Button nowExeBtn = (Button) view.findViewById(R.id.dept_nowExe_btn);
		nowExeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveAmount = saveMoneyEt.getText().toString().trim();
				attachMessage = attachMessageEt.getText().toString().trim();
				buttonFlag = IMMEDIATELY_EXCUTE;
				executeType = "0";
				// 校验存款金额
				if (!checkAmount(saveAmount, businessTypeFlag)) {
					return;
				}
				if (!RegexpUtils.regexpPostscript(attachMessage)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							SaveRegularActivity.this.getResources().getString(R.string.postscript_erroe));
					return;
				}

				if (StringUtil.isNullOrEmpty(attachMessage)) {
					attachMessage = "";
				}

				if (NOTIFY_ONE_DAY_BOTTOM == businessTypeFlag || NOTIFY_SEVEN_DAY_BOTTOM == businessTypeFlag) { // 通知存款需要阅读协议

					if (promiseWay.equals(ConstantGloble.CONVERTTYPE_R)) { // 约定转存需要阅读协议
						// 验证是否同意协议
						if (!checkBox.isChecked()) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									SaveRegularActivity.this.getResources().getString(R.string.register_server_info_toast));
							return;
						}
					}
				}

				// /////////////////////// 手续费试算
				requsetPoundageIntent(businessTypeFlag);
			}
		});

		// 预约日期执行
		Button preDateExeBtn = (Button) view.findViewById(R.id.dept_preDateExe_btn);

		preDateExeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveAmount = saveMoneyEt.getText().toString().trim();
				attachMessage = attachMessageEt.getText().toString().trim();
				buttonFlag = PRE_DATE_EXCUTE;
				executeType = "1";
				// 校验存款金额
				if (!checkAmount(saveAmount, businessTypeFlag)) {
					return;
				}
				if (!RegexpUtils.regexpPostscript(attachMessage)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							SaveRegularActivity.this.getResources().getString(R.string.postscript_erroe));
					return;
				}
				if (StringUtil.isNullOrEmpty(attachMessage)) {
					attachMessage = "";
				}
				if (NOTIFY_ONE_DAY_BOTTOM == businessTypeFlag || NOTIFY_SEVEN_DAY_BOTTOM == businessTypeFlag) { // 通知存款需要阅读协议
					// 验证是否同意协议
					if (promiseWay.equals(ConstantGloble.CONVERTTYPE_R)) { // 约定转存需要阅读协议
						if (!checkBox.isChecked()) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									SaveRegularActivity.this.getResources().getString(R.string.register_server_info_toast));
							return;
						}
					}
				}

				// /////////////////////// 手续费试算
				requsetPoundageIntent(businessTypeFlag);

			}
		});

		// 预约周期执行
		Button prePeriodExe = (Button) view.findViewById(R.id.dept_prePeriodExe_btn);

		prePeriodExe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveAmount = saveMoneyEt.getText().toString().trim();
				attachMessage = attachMessageEt.getText().toString().trim();
				buttonFlag = PRE_WEEK_EXCUTE;
				executeType = "2";

				// 校验存款金额
				if (!checkAmount(saveAmount, businessTypeFlag)) {
					return;
				}
				if (!RegexpUtils.regexpPostscript(attachMessage)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							SaveRegularActivity.this.getResources().getString(R.string.postscript_erroe));
					return;
				}
				if (StringUtil.isNullOrEmpty(attachMessage)) {
					attachMessage = "";
				}
				if (NOTIFY_ONE_DAY_BOTTOM == businessTypeFlag || NOTIFY_SEVEN_DAY_BOTTOM == businessTypeFlag) { // 通知存款需要阅读协议
					// 验证是否同意协议
					if (promiseWay.equals(ConstantGloble.CONVERTTYPE_R)) { // 约定转存需要阅读协议
						if (!checkBox.isChecked()) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									SaveRegularActivity.this.getResources().getString(R.string.register_server_info_toast));
							return;
						}
					}
				}

				// /////////////////////// 手续费试算
				requsetPoundageIntent(businessTypeFlag);

			}
		});
	}

	/** 整存整取 定活两便 一天通知存款 七天通知存款 请求试算 */
	private void requsetPoundageIntent(int businessTypeFlag) {
		// TODO add by luqp 2016年3月28日 格式化金额 上送 例如人民币：58,355.00
		String amount = StringUtil.parseStringPattern(saveAmount, 2);
		if (WHOLE_SAVE_BOTTOM == businessTypeFlag) { // 整存整取
			BaseHttpEngine.showProgressDialog();
			requsetPsnLumpsumTimeDepositComChge(fromAccountId, toAccountId, currencyCode, cashRemit, saveTime, amount,
					attachMessage, executeType);
		} else if (REGULAR_RANDOM_BOTTOM == businessTypeFlag) { // 定活两便
			BaseHttpEngine.showProgressDialog();
			requsetPsnConsolidatedTimeAndSavingsComChge(fromAccountId, toAccountId, currencyCode, cashRemit, saveTime,
					amount, attachMessage, executeType);
		} else if (NOTIFY_ONE_DAY_BOTTOM == businessTypeFlag) { // 一天通知存款
			businessType = BUSINESSTYPE_ONEDAY; // 一天通知存款标示
			BaseHttpEngine.showProgressDialog();
			requsetPsnCurrentSavingToCallDepositComChge(fromAccountId, toAccountId, businessType, promiseWay, currencyCode,
					cashRemit, amount, attachMessage, executeType);
		} else if (NOTIFY_SEVEN_DAY_BOTTOM == businessTypeFlag) { // 七天通知存款
			businessType = BUSINESSTYPE_SEVENDAY; // 七天通知存款标示
			BaseHttpEngine.showProgressDialog();
			requsetPsnCurrentSavingToCallDepositComChge(fromAccountId, toAccountId, businessType, promiseWay, currencyCode,
					cashRemit, amount, attachMessage, executeType);
		}
	}

	/** 教育储蓄 零存整取 */
	private void requsetPoundageInitRenewIntent() {
		// TODO Auto-generated method stub
		if (type.equals(EDUCATION_SAVE1) || type.equals(EDUCATION_SAVE2)) { // 教育储蓄
			BaseHttpEngine.showProgressDialog();
			requsetPsnExtendEducationDepositeComChge(fromAccountId, toAccountId, saveMoney, attachMessage, executeType);

		} else if (type.equals(ZERO_SAVE1) || type.equals(ZERO_SAVE2)) { // 零存整取
			BaseHttpEngine.showProgressDialog();
			requsetPsnExtendLingcunDepositeComChge(fromAccountId, toAccountId, saveMoney, attachMessage, executeType);
		}
	}

	/**
	 * @Title: initRenewBottomlayout
	 * @Description: 设置”教育储蓄“或”零存整取“的执行按钮
	 * @param @param view
	 * @return void
	 * @throws
	 */
	private void initRenewBottomlayout(View view) {

		Button nowExeBtn = (Button) view.findViewById(R.id.dept_nowExe_btn);
		nowExeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (accInInfoMap == null) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(chooseInNoMessage);
					return;
				}
				if (accOutInfoMap == null) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(chooseOutNoMessage);
					return;
				}
				attachMessage = attachMessageEt.getText().toString().trim();
				saveMoney = continueSaveTv.getText().toString().trim();
				executeType = "0";
				if (TextUtils.isEmpty(saveMoney)) {// 校验续存金额是否为空
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							SaveRegularActivity.this.getResources().getString(R.string.add_amount_no_empty));
					return;
				}
				// 校验存款金额
				if (!checkAmount(saveMoney, AMOUNT_DEPOSIT)) {
					return;
				}

				// 续存金额只能等于月存金额
				// if (continueSaveFlag.equals("Y")) {
				// if (Double.parseDouble(continueMonthSave) > 0) {
				// if (Double.parseDouble(continueMonthSave) !=
				// Double.parseDouble(saveMoney)) {
				// BaseDroidApp.getInstanse().showInfoMessageDialog(MyRegSaveChooseTranInAccActivity.this.getResources().getString(R.string.add_amount_equals_month));
				// return;
				// }
				// } //第一次续存时 月存金额为0 不做金额比对
				// }
				if (!RegexpUtils.regexpPostscript(attachMessage)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							SaveRegularActivity.this.getResources().getString(R.string.postscript_erroe));
					return;
				}
				if (StringUtil.isNullOrEmpty(attachMessage)) {
					attachMessage = "";
				} else {
					// RegexpBean memoBean = new RegexpBean("附言", strMessage,
					// "memo");
					// ArrayList<RegexpBean> regexList = new
					// ArrayList<RegexpBean>();
					// regexList.add(memoBean);
					// if(!RegexpUtils.regexpDate(regexList))
					// return;
				}

				// /////////////////////// 手续费试算
				requsetPoundageInitRenewIntent();
			}
		});

		Button preDateExeBtn = (Button) view.findViewById(R.id.dept_preDateExe_btn);
		preDateExeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (accInInfoMap == null) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(chooseInNoMessage);
					;
					return;
				}
				if (accOutInfoMap == null) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(chooseOutNoMessage);
					return;
				}
				attachMessage = attachMessageEt.getText().toString().trim();
				saveMoney = continueSaveTv.getText().toString().trim();
				executeType = "1";
				// 预约日期执行
				if (TextUtils.isEmpty(saveMoney)) {// 校验续存金额是否为空
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							SaveRegularActivity.this.getResources().getString(R.string.add_amount_no_empty));
					return;
				}
				// 校验存款金额
				if (!checkAmount(saveMoney, AMOUNT_DEPOSIT)) {
					return;
				}
				// if (continueSaveFlag.equals("Y")) {
				// if (Double.parseDouble(continueMonthSave) > 0) {
				// if (Double.parseDouble(continueMonthSave) !=
				// Double.parseDouble(saveMoney)) {
				// BaseDroidApp.getInstanse().showInfoMessageDialog(MyRegSaveChooseTranInAccActivity.this.getResources().getString(R.string.add_amount_equals_month));
				// return;
				// }
				// } //第一次续存时 月存金额为0 不做金额比对
				// }

				if (StringUtil.isNullOrEmpty(attachMessage)) {
					attachMessage = "";
				} else {
					// RegexpBean memoBean = new RegexpBean("附言", strMessage,
					// "memo");
					// ArrayList<RegexpBean> regexList = new
					// ArrayList<RegexpBean>();
					// regexList.add(memoBean);
					// if(!RegexpUtils.regexpDate(regexList))
					// return;
				}

				// /////////////////////// 手续费试算
				requsetPoundageInitRenewIntent();
			}
		});

		Button prePeriodExe = (Button) view.findViewById(R.id.dept_prePeriodExe_btn);
		prePeriodExe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (accInInfoMap == null) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(chooseInNoMessage);
					return;
				}
				if (accOutInfoMap == null) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(chooseOutNoMessage);
					return;
				}
				attachMessage = attachMessageEt.getText().toString().trim();
				saveMoney = continueSaveTv.getText().toString().trim();
				executeType = "2";
				if (TextUtils.isEmpty(saveMoney)) {// 校验续存金额是否为空
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							SaveRegularActivity.this.getResources().getString(R.string.add_amount_no_empty));
					return;
				}
				// 校验存款金额
				if (!checkAmount(saveMoney, AMOUNT_DEPOSIT)) {
					return;
				}

				if (StringUtil.isNullOrEmpty(attachMessage)) {
					attachMessage = "";
				} else {
					// RegexpBean memoBean = new RegexpBean("附言", strMessage,
					// "memo");
					// ArrayList<RegexpBean> regexList = new
					// ArrayList<RegexpBean>();
					// regexList.add(memoBean);
					// if(!RegexpUtils.regexpDate(regexList))
					// return;
				}

				// /////////////////////// 手续费试算
				requsetPoundageInitRenewIntent();
			}
		});

	}

	/** 转出账户按钮点击事件 */
	private OnClickListener accOutClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// // 点击转出账户之后 将accoutInfoMap置空
			// accOutInfoMap = null;
			// 发送通讯 请求转出账户列表
			if (isTranOutFirst) {
				requestTranoutAccountList();
			} else {
				List<Map<String, Object>> accoutList = DeptDataCenter.getInstance().getAccountOutList();
				accOutListView = new ListView(context);
				accOutListView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT));
				accOutListView.setBackgroundColor(Color.WHITE);
				accOutListView.setFadingEdgeLength(0);
				accOutListView.setScrollingCacheEnabled(false);
				// accOutListView.setOverscrollFooter(null);
				AccOutListAdapter outadapter = new AccOutListAdapter(context, accoutList);
				accOutListView.setAdapter(outadapter);
				accOutListView.setOnItemClickListener(clickAccOutListItem);
				// 弹出转出转换列表框
				BaseDroidApp.getInstanse().showDeptTranoutinDialog(ConstantGloble.TRANS_ACCOUNT_OPER_OUT, accOutListView,
						backListener, /* addNewAcc*/ null);// //屏蔽自助关联
			}

		}
	};

	/** 转入账户按钮点击事件 */
	private OnClickListener accInClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (isTranOutFirst) {
				CustomDialog.toastInCenter(SaveRegularActivity.this, chooseOutNoMessage);
				return;
			}
			if (isTranInFirst) {
				// 发送通讯请求转入账户信息
				requestAccInBankAccountList();
			} else {
				List<Map<String, Object>> accinList = DeptDataCenter.getInstance().getAccountInList();
				accInListView = new ListView(context);
				// accOutListView.setFadingEdgeLength(0);
				accInListView.setFadingEdgeLength(0);
				accInListView.setScrollingCacheEnabled(false);
				AccInListAdapter adapter = new AccInListAdapter(context, accinList);
				accInListView.setAdapter(adapter);
				accInListView.setOnItemClickListener(clickAccInListItem);
				// 弹出转入转换列表框
				BaseDroidApp.getInstanse().showDeptTranoutinDialog(ConstantGloble.TRANS_ACCOUNT_OPER_IN, accInListView,
						backListener, addAccInNoClicklistener);
			}
		}
	};

	/** 用户点击添加转出账户 */
	private OnClickListener addNewAcc = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// 跳转到关联账户
			transFlag = 1;
			goRelevanceAccount();
		}
	};

	/** 用户点击新增转入账户 */
	private OnClickListener addAccInNoClicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// 跳转到新增转入账户
			transFlag = 2;
			goRelevanceAccount();
		}
	};

	/** 转出账户列表ListView的监听事件 */
	private OnItemClickListener clickAccOutListItem = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, final int position, long id) {
			accOutInfoMap = DeptDataCenter.getInstance().getAccountOutList().get(position);
			// 转出账户ID
			fromAccountId = (String) DeptDataCenter.getInstance().getAccountOutList().get(position).get(Comm.ACCOUNT_ID);
			// 转出省联号
			outAccountIbkNum = (String) DeptDataCenter.getInstance().getAccountOutList().get(position)
					.get(ConstantGloble.accountIbkNum);
			// =======================================================================================
			// add by luqp 2016年3月21日 用来判断转出账户的类型和是否支持电子卡用户标识
			// 转出账户类型
			outAccountType = (String) DeptDataCenter.getInstance().getAccountOutList().get(position).get(Comm.ACCOUNT_TYPE);
			// 电子卡账户标识 1：是 0：否
			accountCatalog = (String) DeptDataCenter.getInstance().getAccountOutList().get(position).get(ACCOUNTCATALOG);
			// ======================================================================================
			// BaseDroidApp.getInstanse().dismissMessageDialog();
			// requestAccBankAccountDetail(fromAccountId);

			// //////////////////////////////////////////////////////////
			// TODO 选择转入账户 在选转出账户 非同省提示
			if (!isInfoChoosedFirst()) { // 用户未选择转入账户
				BaseDroidApp.getInstanse().dismissMessageDialog();
				requestAccBankAccountDetail(fromAccountId);
			} else { // 用户已选择转入账户
				if (!StringUtil.isNullOrEmpty(outAccountIbkNum) && !StringUtil.isNullOrEmpty(inAccountIbkNum)
						&& outAccountIbkNum.equals(inAccountIbkNum)) { // 同省
					showMyLayout(position);
					BaseDroidApp.getInstanse().dismissMessageDialog();
					requestAccBankAccountDetail(fromAccountId);
				} else { // 非同省
					BaseDroidApp.getInstanse().showErrorDialog(getResources().getString(R.string.saceregular_cost_prompted),
							R.string.saceregular_cancel, R.string.saceregular_countersign, new OnClickListener() {

								@Override
								public void onClick(View v) {
									if (v.getId() == R.id.retry_btn) {
										showMyLayout(position);
										BaseDroidApp.getInstanse().dismissMessageDialog();
										requestAccBankAccountDetail(fromAccountId);
									} else { // 用户点击取消
										BaseDroidApp.getInstanse().dismissErrorDialog();
										return;
									}
								}
							});
				}
				// /////////////////////////////////////////////////////////
			}
		}
	};

	/** 转入账户列表ListView的监听事件 */
	private OnItemClickListener clickAccInListItem = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, final int position, long id) {
			accInInfoMap = DeptDataCenter.getInstance().getAccountInList().get(position);
			// 存储转入账户信息
			DeptDataCenter.getInstance().setAccInInfoMap(accInInfoMap);

			BaseDroidApp.getInstanse().dismissMessageDialog();
			// 添加一个新的布局文件--转入账户详细信息
			accInDetailLayout = (LinearLayout) inflater.inflate(R.layout.dept_acc_in_list_detail, null);
			// 转入账号的省联号
			inAccountIbkNum = (String) DeptDataCenter.getInstance().getAccountInList().get(position)
					.get(ConstantGloble.accountIbkNum);
			// 转入账户ID
			toAccountId = (String) DeptDataCenter.getInstance().getAccountInList().get(position).get(Comm.ACCOUNT_ID);
			// ===============================================================================================
			// add by luqp 2016年3月21 转出账户卡类型
			inAccountType = (String) DeptDataCenter.getInstance().getAccountInList().get(position).get(Comm.ACCOUNT_TYPE);
			// ===============================================================================================
			// ///////////////////////////////////////////////////////////////////////////
			// 用户选择转入账户与转出账户是否为同省, 同省非同省全部调用试算手续费接口
			if (!StringUtil.isNullOrEmpty(outAccountIbkNum) && !StringUtil.isNullOrEmpty(inAccountIbkNum)
					&& outAccountIbkNum.equals(inAccountIbkNum)) {
				showMyLayout(position);
				BaseDroidApp.getInstanse().dismissMessageDialog();
			} else { // 非同省提示用户收取手续费
				// 整存整取弹出提示
				BaseDroidApp.getInstanse().showErrorDialog(getResources().getString(R.string.saceregular_cost_prompted),
						R.string.saceregular_cancel, R.string.saceregular_countersign, new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (v.getId() == R.id.retry_btn) {
									BaseDroidApp.getInstanse().closeAllDialog();
									BaseDroidApp.getInstanse().dismissMessageDialog();
									showMyLayout(position);
								} else { // 用户点击取消
									BaseDroidApp.getInstanse().dismissErrorDialog();
									return;
								}
							}
						});
				// /////////////////////////////////////////////////////////////////////////
			}
		}
	};

	/** 加载转入账户布局 */
	private void showMyLayout(int position) {
		// 添加一个新的布局文件--转入账户详细信息
		accInDetailLayout = (LinearLayout) inflater.inflate(R.layout.dept_acc_in_list_detail, null);

		tv_prompt.setVisibility(View.GONE);
		mAccInLl.removeAllViews();
		mAccInLl.addView(accInDetailLayout);
		mAccInLl.setVisibility(View.VISIBLE);
		mAccInListLayout.setVisibility(View.GONE);
		newAddTranInBtn.setVisibility(View.GONE);
		showAccInListItemData(position); // 显示转出账户条目的详细信息
		type = (String) accInInfoMap.get(Comm.ACCOUNT_TYPE);
		// ================================================================================================
		// add by luqp 2016年3月21日 判断是否是电子卡并且支持电子卡用户 利率上浮1.3倍
		if ("10".equals(segmentId) && "2".equals(qryCustType)) { // 是否查询版账户
			// 电子卡账户不为空 并且 电子卡账户 2|3支持 0不支持 转出账户电子卡 119 转入账户为定期一本通170
			if ("2".equals(accountCatalog) || "3".equals(accountCatalog) && LargeSign_WHOE_SAVE.equals(outAccountType)
					&& RANDOM_ONE_SAVE.equals(inAccountType)) {
				// initWholeSaveBottomLayout(); // 1.3倍利率上浮显示
				InterestRateFloatingLayout();
			} else { // 不支持电子卡或转入转出账户不符合要求
				if (ConstantGloble.ACC_TYPE_EDU.equals(type) || ConstantGloble.ACC_TYPE_ZOR.equals(type)) {
					showRenewBottomLayout();
				} else {
					showBottomButtons();
				}
			}
		} else { // 如果账户不是电子卡账户显示
			if (ConstantGloble.ACC_TYPE_EDU.equals(type) || ConstantGloble.ACC_TYPE_ZOR.equals(type)) {
				showRenewBottomLayout();
			} else {
				showBottomButtons();
			}
		}
		// ================================================================================================
		isTranInFirst = false;
	}
	/**C:\Users\Administrator\Downloads
	 * @Title: showRenewBottomLayout
	 * @Description: 显示“教育储蓄"或"零存整取" 续存的底部布局
	 * @param
	 * @return void
	 * @throws
	 */
	private void showRenewBottomLayout() {
		rl_save_title.setClickable(false);
		tv_neiku.setVisibility(View.GONE);
		LinearLayout layoutLayout3 = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.dept_savereg_education_bottom, null);
		// 月存金额
		continueMonthSave = (String) DeptDataCenter.getInstance().getCurDetailContent().get(Dept.MONTH_BALANCE);

		LinearLayout ll_mon_save_amount = (LinearLayout) layoutLayout3.findViewById(R.id.ll_mon_save_amount);
		TextView tv_mon_save_mount = (TextView) layoutLayout3.findViewById(R.id.tv_mon_save_mount);
		tv_mon_save_mount.setText(StringUtil.parseStringPattern(continueMonthSave, 2));
		ll_mon_save_amount.setVisibility(View.GONE);
		TextView title = (TextView) view.findViewById(R.id.dept_save_title_tv);
		String strEducationTitle = this.getResources().getString(R.string.education_title);
		String strZeroTitle = this.getResources().getString(R.string.zero_save_title);
		if (type.equals(EDUCATION_SAVE1) || type.equals(EDUCATION_SAVE2)) {
			setTitle(R.string.education_title);
			title.setText(strEducationTitle);
		} else if (type.equals(ZERO_SAVE1) || type.equals(ZERO_SAVE2)) {
			setTitle(R.string.zero_save_title);
			title.setText(strZeroTitle);
		}
		// TextView 币种
		TextView currency3Tv = (TextView) layoutLayout3.findViewById(R.id.dept_checkout_currency_tv);
		String sCurrency = (String) accInInfoMap.get(Comm.CURRENCYCODE);
		currency3Tv.setText(LocalData.Currency.get(sCurrency));
		// EditText 续存金额
		continueSaveTv = (EditText) layoutLayout3.findViewById(R.id.dept_save_money_et);
		// EditText 附言
		attachMessageEt = (EditText) layoutLayout3.findViewById(R.id.dept_message_et);
		EditTextUtils.setLengthMatcher(this, attachMessageEt, 50);

		initRenewBottomlayout(layoutLayout3);
		bottomLayout.removeAllViews();
		bottomLayout.addView(layoutLayout3);
	}

	/**
	 * @Title: showBottomButtons
	 * @Description: 显示转入账户为“定期一本通"的底部布局
	 * @param
	 * @return void
	 * @throws
	 */
	private void showBottomButtons() {
		LinearLayout layoutBottom = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.dept_savereg_bottom_for_renew, null);
		titleTv.setText(R.string.save_setting);
		newAddBtn1 = (Button) layoutBottom.findViewById(R.id.dept_new_btn1);
		newAddBtn1.setOnClickListener(addBtnClicklistener);
		newAddBtn2 = (Button) layoutBottom.findViewById(R.id.dept_new_btn2);
		newAddBtn2.setOnClickListener(addBtnClicklistener);
		newAddBtn3 = (Button) layoutBottom.findViewById(R.id.dept_new_btn3);
		newAddBtn3.setOnClickListener(addBtnClicklistener);
		newAddBtn4 = (Button) layoutBottom.findViewById(R.id.dept_new_btn4);
		newAddBtn4.setOnClickListener(addBtnClicklistener);
		bottomLayout.removeAllViews();
		bottomLayout.addView(layoutBottom);
	}

	// /**
	// * 用户点击新整存整取 PsnTransLumpsumTimeDepositNew
	// */
	// private OnClickListener addBtn1Clicklistener = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // 判断用户是否选择了转出和转入账户
	// // 如果用户没有选择转入账户
	// if (!isInfoChoosed()) {
	// return;
	// }
	//
	// initDepositSetting(v.getId());
	//
	// }
	// };
	//
	// /**
	// * 用户点击新增转入账户
	// */
	// private OnClickListener addBtn2Clicklistener = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // 判断用户是否选择了转出和转入账户
	// // 如果用户没有选择转入账户
	// if (!isInfoChoosed()) {
	// return;
	// }
	//
	// initDepositSetting(v.getId());
	// }
	// };
	//
	// /**
	// * 用户点击新增转入账户
	// */
	// private OnClickListener addBtn3Clicklistener = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // 判断用户是否选择了转出和转入账户
	// // 如果用户没有选择转入账户
	// if (!isInfoChoosed()) {
	// return;
	// }
	//
	// initDepositSetting(v.getId());
	// }
	// };

	/** 底部按钮点击事件 */
	private OnClickListener addBtnClicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// 判断用户是否选择了转出和转入账户
			// 如果用户没有选择转入账户
			if (!isInfoChoosed()) {
				return;
			}
			initDepositSetting(v.getId());
		}
	};

	/**
	 * 初始化 通知存款底部视图
	 * 
	 * @param type ONE_DAY_FLAG 一天通知存款 SEVEN_DAY_FLAG 七天通知存款
	 */
	private void initOneOrSevenBottomLayout(String type) {
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.dept_add_new3_4_detail_layout, null);
		// final TextView saveNoticeTv = (TextView)
		// linearLayout.findViewById(R.id.dept_save_notice_tv);
		// 币种
		final Spinner currencySpinner = (Spinner) linearLayout.findViewById(R.id.dept_currency_spinner);
		// 钞汇标识
		final Spinner cashRemitSpinner = (Spinner) linearLayout.findViewById(R.id.dept_money_mark_spinner);
		// 钞汇标识布局
		LinearLayout ll_cash_remit = (LinearLayout) linearLayout.findViewById(R.id.ll_cash_remit);
		LinearLayout currencyLayout = (LinearLayout) linearLayout.findViewById(R.id.dept_currency_layout);
		LinearLayout currencySpinnerLayout = (LinearLayout) linearLayout.findViewById(R.id.dept_currency_spinner_layout);
		// 协议布局
		final LinearLayout ll_agreement = (LinearLayout) linearLayout.findViewById(R.id.ll_agreement);
		TextView currencyTv = (TextView) linearLayout.findViewById(R.id.dept_currency_tv);
		checkBox = (CheckBox) linearLayout.findViewById(R.id.checkbox_agree_info);
		TextView agreeText = (TextView) linearLayout.findViewById(R.id.text_server_info);
		agreeText.setText(Html.fromHtml("<u>" + this.getResources().getString(R.string.notify_save_server_info) + "</u>"));
		agreeText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 服务条约
				Intent intent = new Intent();
				intent.setClass(SaveRegularActivity.this, SaveAgreeActivity.class);
				startActivityForResult(intent, 1);
				overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
			}
		});

		saveMoneyEt = (EditText) linearLayout.findViewById(R.id.dept_save_money_et);
		attachMessageEt = (EditText) linearLayout.findViewById(R.id.dept_message_et);
		EditTextUtils.setLengthMatcher(SaveRegularActivity.this, attachMessageEt, 50);
		// 如果是一天通知存款 只有人名币
		if (type.equals(ONE_DAY_FLAG)) {
			// 改变顶部title
			setTitle(R.string.save_one_day);
			titleTv.setText(this.getResources().getString(R.string.save_one_day));
			businessType = ONE_DAY_FLAG;
			String strCurrency = this.getResources().getString(R.string.tran_currency_rmb);
			currencyTv.setText(strCurrency);
			currencyLayout.setVisibility(View.VISIBLE);
			currencySpinnerLayout.setVisibility(View.GONE);
			currencyCode = ConstantGloble.PRMS_CODE_RMB;// PRMS_CODE_RMB
			// 钞汇标志
			cashRemit = ConstantGloble.CASHREMIT_00;
			// 币种为人民币 钞汇标志不可选 add luqp 2015年10月22日12:47:02
			ll_cash_remit.setVisibility(View.GONE);
			initBottomBtnlayout(linearLayout, NOTIFY_ONE_DAY_BOTTOM);
		} else if (type.equals(SEVEN_DAY_FLAG)) {
			// 改变顶部title
			setTitle(R.string.save_seven_day);
			titleTv.setText(this.getResources().getString(R.string.save_seven_day));
			businessType = SEVEN_DAY_FLAG;
			ll_cash_remit.setVisibility(View.VISIBLE);
			final String strSaveNotice = this.getResources().getString(R.string.save_notice);
			final String strSaveNoticeFore = this.getResources().getString(R.string.save_notice_fore);
			@SuppressWarnings("unchecked")
			final List<Map<String, Object>> detailList = (List<Map<String, Object>>) DeptDataCenter.getInstance()
					.getCurDetailContent().get(ConstantGloble.ACC_DETAILIST);
			// if (StringUtil.isNullOrEmpty(detailList)) {
			// return;
			// }
			combineData(detailList, ConstantGloble.SEVEN);
			final List<String> currencyCodeFlagList = new ArrayList<String>();
			final List<String> cashRemitFlagList = new ArrayList<String>();
			final List<String> codeFlagList = new ArrayList<String>();
			// for (int i = 0; i < currencyList.size(); i++) {
			// String currencyCode = currencyList.get(i);
			// // 七天通知存款 过滤新西兰元、丹麦克朗、挪威克朗、瑞典克朗币种
			// if (StringUtil.isNullOrEmpty(currencyCode) ||
			// LocalData.SaveRegularCodeList.contains(currencyCode)) {
			// continue;
			// }
			// currencyCodeFlagList.add(LocalData.Currency.get(currencyCode));
			// codeFlagList.add(currencyCode);
			// }
			// 七天 只支持十种币种
			for (int i = 0; i < currencyList.size(); i++) {
				String currencyCode = currencyList.get(i);
				// 七天通知存款 过滤新西兰元、丹麦克朗、挪威克朗、瑞典克朗币种
				if (StringUtil.isNullOrEmpty(currencyCode) || LocalData.SaveRegularCurrencyCodeList.contains(currencyCode)) {
					currencyCodeFlagList.add(LocalData.Currency.get(currencyCode));
					codeFlagList.add(currencyCode);
					// continue;
				}
			}

			if (StringUtil.isNullOrEmpty(codeFlagList)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("您的账户没有可用币种");
				return;
			}

			ArrayAdapter<String> currencySpinnerAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse()
					.getCurrentAct(), R.layout.dept_spinner, currencyCodeFlagList);
			currencySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			currencySpinner.setAdapter(currencySpinnerAdapter);
			currencySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					currencyCode = codeFlagList.get(position);
					// add by luqp 2015年12月16日17:58:22 选择币种前清空数据
					cashRemitList.clear();
					if (currencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) { // 选择人民币
						cashRemitSpinner.setEnabled(false);
						cashRemitSpinner.setBackgroundDrawable(SaveRegularActivity.this.getResources().getDrawable(
								R.drawable.bg_spinner_default));
						ArrayAdapter<String> cashRemitSpinnerAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse()
								.getCurrentAct(), R.layout.dept_spinner, new String[] { "-" });
						cashRemitSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						cashRemitSpinner.setAdapter(cashRemitSpinnerAdapter);
						saveMoneyEt.setHint(strSaveNotice);
						// 钞汇标志
						cashRemit = ConstantGloble.CASHREMIT_00;
						cashRemitList.add(cashRemit);
					} else { // 选择外币
						cashRemitSpinner.setEnabled(true);
						cashRemitSpinner.setBackgroundDrawable(SaveRegularActivity.this.getResources().getDrawable(
								R.drawable.bg_spinner));
						currentCurrencyCashRemitList = (ArrayList<Map<String, Object>>) currencyAndCashRemitList.get(
								position).get(ConstantGloble.TRAN_CASHREMIT_AND_CONTENT);
						cashRemitFlagList.clear();
						cashRemitList.clear();
						for (int i = 0; i < currentCurrencyCashRemitList.size(); i++) {
							String cashRemit = (String) currentCurrencyCashRemitList.get(i).get(
									ConstantGloble.TRAN_CASHREMIT);
							cashRemitList.add(cashRemit);
							cashRemitFlagList.add(LocalData.cashRemitBackMap.get(cashRemit));
						}
						ArrayAdapter<String> cashRemitSpinnerAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse()
								.getCurrentAct(), R.layout.dept_spinner, cashRemitFlagList);
						cashRemitSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						cashRemitSpinner.setAdapter(cashRemitSpinnerAdapter);
						cashRemitSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
								cashRemit = cashRemitList.get(position);
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
								cashRemitList.get(0);
							}
						});
						saveMoneyEt.setHint("");
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					currencyCode = codeFlagList.get(0);
				}
			});

			initBottomBtnlayout(linearLayout, NOTIFY_SEVEN_DAY_BOTTOM);
		}
		promiseWay = ConstantGloble.CONVERTTYPE_R;
		// 约定方式
		RadioGroup radioGroup = (RadioGroup) linearLayout.findViewById(R.id.dept_promise_way_radiogroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.dept_promise_rb:// 约定转存
					promiseWay = ConstantGloble.CONVERTTYPE_R;
					ll_agreement.setVisibility(View.VISIBLE);
					break;
				case R.id.dept_no_promise_rb:// 非约定转存
					promiseWay = ConstantGloble.CONVERTTYPE_N;
					ll_agreement.setVisibility(View.GONE);
					break;

				default:
					break;
				}
			}
		});
		// Spinner promiseWaySpinner = (Spinner) linearLayout
		// .findViewById(R.id.dept_promise_way_spinner);
		// ArrayAdapter promiseWayAdapter = new ArrayAdapter(BaseDroidApp
		// .getInstanse().getCurrentAct(),
		// android.R.layout.simple_spinner_item, promiseWayList);
		// promiseWayAdapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// promiseWaySpinner.setAdapter(promiseWayAdapter);
		// promiseWaySpinner
		// .setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent,
		// View view, int position, long id) {
		// String curPromiseWay = promiseWayList.get(position);
		// promiseWay = LocalData.promiseWayMap.get(curPromiseWay);
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent) {
		// String curPromiseWay = promiseWayList.get(0);
		// promiseWay = LocalData.promiseWayMap.get(curPromiseWay);
		// }
		// });
		bottomLayout.removeAllViews();
		bottomLayout.addView(linearLayout);
	}

	/**
	 * 初始化存款设定内容
	 * 
	 * @param id
	 */
	private void initDepositSetting(int id) {
		if (!tv_neiku.isShown())
			tv_neiku.setVisibility(View.VISIBLE);
		if (!rl_save_title.isClickable())
			rl_save_title.setClickable(true);
		// bottomLayout.setBackgroundColor(Color.parseColor("#00000000"));
		switch (id) {
		case R.id.dept_new_btn1: // 整存整取
			initWholeSaveBottomLayout();
			break;
		case R.id.dept_new_btn2:// 定活两便
			initRandomLayout();
			break;
		case R.id.dept_new_btn3:// 一天通知存款
			initOneOrSevenBottomLayout(ONE_DAY_FLAG);
			break;
		case R.id.dept_new_btn4:// 七天通知存款
			initOneOrSevenBottomLayout(SEVEN_DAY_FLAG);
			break;
		}

	}

	/** 初始化定活两便内容 */
	private void initRandomLayout() {
		setTitle(R.string.save_random);
		String strTitle = context.getResources().getString(R.string.save_random);
		titleTv.setText(strTitle);
		// 跳转到新增转入账户
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.dept_add_new2_layout, null);
		TextView currencyTv = (TextView) linearLayout.findViewById(R.id.dept_currency_tv);
		currencyCode = (String) accOutInfoMap.get(Comm.CURRENCYCODE);
		currencyTv.setText(LocalData.Currency.get(currencyCode));
		saveMoneyEt = (EditText) linearLayout.findViewById(R.id.dept_save_money_et);
		attachMessageEt = (EditText) linearLayout.findViewById(R.id.dept_message_et);
		EditTextUtils.setLengthMatcher(SaveRegularActivity.this, attachMessageEt, 50);
		bottomLayout.removeAllViews();
		bottomLayout.addView(linearLayout);
		initBottomBtnlayout(linearLayout, REGULAR_RANDOM_BOTTOM);
	}

	/** 通讯返回 */
	@Override
	public void communicationCallBack(int flag) {
		super.communicationCallBack(flag);
		switch (flag) {
		case QUERY_ACCOUNT_OUT_CALLBACK:// 查询所有账户返回 刷新界面

			List<Map<String, Object>> accoutList = DeptDataCenter.getInstance().getAccountOutList();
			if (StringUtil.isNullOrEmpty(accoutList)) {// 如果没有符合要求的数据 弹框提示

				
				
				BaseDroidApp.getInstanse().createDialog("", "您目前没有符合条件的转出账户，请您携带未关联账户、有效身份证件及已关联网银的任一账户，前往我行营业网点进行关联。", new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						BaseDroidApp.getInstanse().dismissErrorDialog();	
						goRelevanceAccount();
					}
				});
//				BaseDroidApp.getInstanse().showErrorDialog(null, /*"您没有符合类型的转出账户，是否关联新账户"*/
//						"您目前符合条件的转出账户，请您携带未关联账户、有效身份证件及已关联网银的任一账户，前往我行营业网点进行关联。",
//						new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						BaseDroidApp.getInstanse().dismissErrorDialog();
//						switch (EpayUtil.getInt(v.getTag(), 0)) {
//						case CustomDialog.TAG_SURE:
//							goRelevanceAccount();
//							break;
//						}
//					}
//				});
				// BaseDroidApp.getInstanse().showMessageDialog(this.getResources().getString(R.string.no_list_data),
				// new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// ActivityTaskManager.getInstance().removeAllActivity();
				// Intent intent = new Intent();
				// intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
				// MainActivity.class);
				// BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
				// }
				// });
				break;
			}
			accOutListView = new ListView(context);
			accOutListView.setFadingEdgeLength(0);
			accOutListView.setScrollingCacheEnabled(false);
			AccOutListAdapter outadapter = new AccOutListAdapter(context, accoutList);
			accOutListView.setAdapter(outadapter);
			accOutListView.setOnItemClickListener(clickAccOutListItem);// 点击转出账户ListView的的条目
			// 弹出转出转换列表框
			BaseDroidApp.getInstanse().showDeptTranoutinDialog(ConstantGloble.TRANS_ACCOUNT_OPER_OUT, accOutListView,
					backListener,/* addNewAcc*/ null);// //屏蔽自助关联

			// View outView = outadapter.getView(0, null, accListView);
			// outView.measure(0, 0);
			// itemHeight = outView.getMeasuredHeight();
			// // 设置listview高度
			// setLayoutHeight(mAccOutListLayout);
			//
			// mAccOutLl.setVisibility(View.GONE);
			// mAccOutListLayout.setVisibility(View.VISIBLE);
			// newAddTranOutBtn.setVisibility(View.VISIBLE);
			// mAccOutListLayout.removeAllViews();
			// mAccOutListLayout.addView(accListView);

			// mAccOutLl.removeAllViews();
			// mAccOutLl.addView(accListView);
			break;
		case QUERY_ACCOUNT_DETAIL_CALLBACK:// 查询账户详情返回 刷新界面
			// 添加一个新的布局文件--转出账户详细信息
			BaseDroidApp.getInstanse().dismissMessageDialog();
			// 存储转出账户信息
			if (accOutInfoMap != null) {
				DeptDataCenter.getInstance().setAccOutInfoMap(accOutInfoMap);
			}
			accOutDetailLayout = (LinearLayout) inflater.inflate(R.layout.dept_acc_out_list_detail, null);

			mAccOutLl.removeAllViews();
			mAccOutLl.addView(accOutDetailLayout);
			mAccOutLl.setVisibility(View.VISIBLE);
			mAccOutListLayout.setVisibility(View.GONE);
			newAddTranOutBtn.setVisibility(View.GONE);
			showAccOutListItemData(accOutDetailLayout);// 显示转出账户条目的详细信息
			TextView textTitle = (TextView) findViewById(R.id.tv_title);
			if (this.getString(R.string.save_whole).equals(textTitle.getText().toString())) {
				initWholeSaveBottomLayout();
			} else if (this.getString(R.string.save_seven_day).equals(textTitle.getText().toString())) {
				initOneOrSevenBottomLayout(SEVEN_DAY_FLAG);
			}
			isTranOutFirst = false;
			newTranBtn.setVisibility(View.VISIBLE);
			break;
		case QUERY_ALL_IN_ACCOUNT_CALLBACK:// 查询转入账户返回 刷新界面
			List<Map<String, Object>> accinList = DeptDataCenter.getInstance().getAccountInList();
			if (StringUtil.isNullOrEmpty(accinList)) {// 如果没有符合要求的数据 弹框提示

				// add by luqp 2016年11月25日 临时关闭 开通定期账户 large_apply_for_an_account提示信息修改为:You_do_not_have_a_regular_account
			// add by luqp 2016年11月24日 开通定期账户,跳转行方开通.
//			BaseDroidApp.getInstanse().showErrorDialog(getResources().getString(R.string.You_do_not_have_a_regular_account),
//					R.string.saceregular_cancel, R.string.saceregular_countersign, new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							if (v.getId() == R.id.retry_btn) {
//								BaseDroidApp.getInstanse().closeAllDialog();
//								BaseDroidApp.getInstanse().dismissMessageDialog();
//								// 跳转行方模块,申请定活期账户.
//								ModelBoc.gotoBocPageActivity(SaveRegularActivity.this, null, new IActionTwo() {
//									@Override
//									public void callBack(Object param1, Object param2) {
//										if((Boolean)param2==true&&!StringUtil.isNullOrEmpty(param1)) {
//											Map<String,Object> map = (Map<String,Object>)param1;
//											String isHome = (String) map.get("isHome"); //申请账户状态 0：开户失败 1：开户成功
//											String applyStatus = (String) map.get("applyStatus"); //申请账户状态 0：开户失败 1：开户成功
//											String linkStatus = (String) map.get("linkStatus"); //关联网银状态 0：关联网银失败 1：关联网银成功
//											String accountNum = (String) map.get("accountNum"); //新开户的账号 暂时未用
//											if (isHome.equals(true)){
//												ActivityTaskManager.getInstance().removeAllActivity();
//											}
//											// 签约成功,并且关联网银成功.
//											if (applyStatus.equals("1") && linkStatus.equals("1")){
//												if (transFlag == 1) {
//													requestTranoutAccountList();
//												} else if (transFlag == 2) {
//													requestAccInBankAccountList();
//												}
//											} else {
//												BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.You_do_not_have_a_regular_account));
//											}
//										}
//									}
//								});
//							} else { // 用户点击取消
//								BaseDroidApp.getInstanse().dismissErrorDialog();
//								return;
//							}
//						}
//					});

//				BaseDroidApp.getInstanse().showErrorDialog(null, "您没有符合条件的转入账户，是否关联新账户", new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						BaseDroidApp.getInstanse().dismissErrorDialog();
//						switch (EpayUtil.getInt(v.getTag(), 0)) {
//						case CustomDialog.TAG_SURE:
//							goRelevanceAccount();
//							break;
//						}
//					}
//				});
				
//				BaseDroidApp.getInstanse().showErrorDialog(null, "您没有符合类型的转入账户，是否申请定期账户", new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					BaseDroidApp.getInstanse().dismissErrorDialog();
//					switch (EpayUtil.getInt(v.getTag(), 0)) {
//					case CustomDialog.TAG_SURE:
////						goRelevanceAccount();
////						regularAccount();
//						interestRateFlag = APPLICATION_ACCOUNT; // 利率上浮标志
//						Intent intent = new Intent();
//						intent.setClass(SaveRegularActivity.this, ApplyTermDepositeActivity.class);
//						intent.putExtra(Dept.APPLICATION_ACCOUNT_FLAG , interestRateFlag);
//						startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
//						break;
//					}
//				}
//			});
				
				
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.You_do_not_have_a_regular_account));

				// BaseDroidApp.getInstanse().showMessageDialog(this.getResources().getString(R.string.no_list_data),
				// new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// ActivityTaskManager.getInstance().removeAllActivity();
				// Intent intent = new Intent();
				// intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
				// MainActivity.class);
				// BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
				// }
				// });
				break;
			}
			accInListView = new ListView(context);
			accOutListView.setFadingEdgeLength(0);
			accInListView.setScrollingCacheEnabled(false);
			AccInListAdapter adapter = new AccInListAdapter(context, accinList);
			accInListView.setAdapter(adapter);
			accInListView.setOnItemClickListener(clickAccInListItem);
			// 弹出转入转换列表框
			BaseDroidApp.getInstanse().showDeptTranoutinDialog(ConstantGloble.TRANS_ACCOUNT_OPER_IN, accInListView,
					backListener, /*addAccInNoClicklistener*/ null);//屏蔽自助关联
			// View inView = adapter.getView(0, null, accListView);
			// inView.measure(0, 0);
			// itemHeight = inView.getMeasuredHeight();
			// // 设置listview高度
			// setLayoutHeight(mAccInListLayout);
			//
			// mAccInLl.setVisibility(View.GONE);
			// mAccInListLayout.setVisibility(View.VISIBLE);
			// newAddTranInBtn.setVisibility(View.VISIBLE);
			// mAccInListLayout.removeAllViews();
			// mAccInListLayout.addView(accListView);
			break;

		// /////////////////新增手续费试算费用
		case WHOLE_SAVE_CALLBACK: // 整存整取
			int businessTypeFlag = WHOLE_SAVE_BOTTOM;
			if (buttonFlag == IMMEDIATELY_EXCUTE) { // 立即执行
				// ============================================================================
				// add by luqp 2016年3月21日 判断是否是电子卡并且支持电子卡用户 利率上浮1.3倍
				if ("10".equals(segmentId) && "2".equals(qryCustType)) { // 是否查询版账户
					// 电子卡账户不为空 并且 电子卡账户 2|3支持 0不支持 转出账户电子卡 119 转入账户为定期一本通170
					if ("2".equals(accountCatalog) || "3".equals(accountCatalog)  && LargeSign_WHOE_SAVE.equals(outAccountType)
							&& RANDOM_ONE_SAVE.equals(inAccountType)) {
						immediateIntentNew(businessTypeFlag);
					} else { // 不支持电子卡或转入转出账户不符合要求
						immediateIntent(businessTypeFlag);
					}
				} else { // 如果账户不是电子卡账户显示
					immediateIntent(businessTypeFlag);
				}
				// =============================================================================
//				immediateIntent(businessTypeFlag);
			} else if (buttonFlag == PRE_DATE_EXCUTE) { // 预约日期执行
				dateIntent(businessTypeFlag);
			} else if (buttonFlag == PRE_WEEK_EXCUTE) { // 预约周期执行
				periodIntent(businessTypeFlag);
			}
			break;
		case REGULAR_RANDOM_CALLBACK: // 定活两便
			int businessTypeFlag1 = REGULAR_RANDOM_BOTTOM;
			if (buttonFlag == IMMEDIATELY_EXCUTE) { // 立即执行
				immediateIntent(businessTypeFlag1);
			} else if (buttonFlag == PRE_DATE_EXCUTE) { // 预约日期执行
				dateIntent(businessTypeFlag1);
			} else if (buttonFlag == PRE_WEEK_EXCUTE) { // 预约周期执行
				periodIntent(businessTypeFlag1);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 刷新转出账户界面
		if (resultCode == RESULT_OK) {
			if (requestCode == ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE) {
				if (transFlag == 1) {
					// requestAccOutBankAccountList();
					requestTranoutAccountList();
				} else if (transFlag == 2) {
					String info = getIntent().getStringExtra("info");
					String linkStatu = getIntent().getStringExtra("accountNew");
					requestAccInBankAccountList();
				}
			}
		} else if (requestCode == 1 && resultCode == 100) {
			checkBox.setChecked(true);
		} else if (requestCode == 1 && resultCode == 101) {
			checkBox.setChecked(false);
		}

	}

	/** 初始化整存整取底部视图 */
	private void initWholeSaveBottomLayout() { // TODO
		// 改变顶部title
		setTitle(R.string.save_whole);
		String strTitle = context.getResources().getString(R.string.save_whole);
		titleTv.setText(strTitle);
		// 跳转到新增转入账户
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.dept_add_new1_layout, null);
		// 币种
		Spinner currencySpinner = (Spinner) linearLayout.findViewById(R.id.dept_currency_spinner);
		// 钞汇标志
		final Spinner cashRemitSpinner = (Spinner) linearLayout.findViewById(R.id.dept_money_mark_spinner);
		// 存期
		final Spinner saveTimeSpinner = (Spinner) linearLayout.findViewById(R.id.dept_save_time_spinner);
		saveMoneyEt = (EditText) linearLayout.findViewById(R.id.dept_save_money_et);
		// 币种列表从详情处获得
		final List<String> currencyCodeList = new ArrayList<String>();
		final List<String> cashRemitFlagList = new ArrayList<String>();
		// final List<String> currencyCodeFlagList = new ArrayList<String>();
		@SuppressWarnings("unchecked")
		final List<Map<String, Object>> detailList = (List<Map<String, Object>>) DeptDataCenter.getInstance()
				.getCurDetailContent().get(ConstantGloble.ACC_DETAILIST);
		// 为空处理
		if (StringUtil.isNullOrEmpty(detailList)) {
			return;
		}
		combineData(detailList, ConstantGloble.WHOLE);
		final List<String> currencyCodeFlagList = new ArrayList<String>();
		final List<String> codeList = new ArrayList<String>();
		for (int i = 0; i < currencyList.size(); i++) {
			String currencyCode = currencyList.get(i);
			// 过滤币种
			if (StringUtil.isNullOrEmpty(currencyCode) || LocalData.SaveRegularIlterCodeList.contains(currencyCode)) {
				currencyCodeFlagList.add(LocalData.Currency.get(currencyCode));
				codeList.add(currencyCode);
				// continue;
			}
		}

		if (StringUtil.isNullOrEmpty(codeList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("您的账户没有可用币种");
			return;
		}

		if (StringUtil.isNullOrEmpty(codeList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("您的账户没有可用币种");
			return;
		}

		// 默认选择第一个
		// currencyCode = currencyList.get(0);
		currencyCode = codeList.get(0);
		ArrayAdapter<String> currencySpinnerAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse().getCurrentAct(),
				R.layout.dept_spinner, currencyCodeFlagList);
		currencySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currencySpinner.setAdapter(currencySpinnerAdapter);
		final String strSaveReg = this.getResources().getString(R.string.least_save_money);
		if (currencyCodeList.size() == 1) {
			currencySpinner.setEnabled(false);
			initDefaultCashRemitSpinner(cashRemitSpinner);
			// 存期
			initSaveTimeSpinner(saveTimeSpinner, saveTimeList);
		} else {
			currencySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					currencyCode = codeList.get(position);
					// add by luqp 2015年12月16日17:58:22 选择币种前清空数据
					cashRemitList.clear();
					if (currencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {
						cashRemitSpinner.setEnabled(false);
						cashRemitSpinner.setBackgroundDrawable(SaveRegularActivity.this.getResources().getDrawable(
								R.drawable.bg_spinner_default));
						ArrayAdapter<String> cashRemitSpinnerAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse()
								.getCurrentAct(), R.layout.dept_spinner, new String[] { "-" });
						cashRemitSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						cashRemitSpinner.setAdapter(cashRemitSpinnerAdapter);
						saveMoneyEt.setHint(strSaveReg);
						// 存期
						initSaveTimeSpinner(saveTimeSpinner, saveTimeList);
						cashRemit = ConstantGloble.CASHREMIT_00;
						cashRemitList.add(cashRemit);
					} else {
						cashRemitSpinner.setEnabled(true);
						cashRemitSpinner.setBackgroundDrawable(SaveRegularActivity.this.getResources().getDrawable(
								R.drawable.bg_spinner));
						currentCurrencyCashRemitList = (ArrayList<Map<String, Object>>) currencyAndCashRemitList.get(
								position).get(ConstantGloble.TRAN_CASHREMIT_AND_CONTENT);
						cashRemitFlagList.clear();
						for (int i = 0; i < currentCurrencyCashRemitList.size(); i++) {
							String cashRemit = (String) currentCurrencyCashRemitList.get(i).get(
									ConstantGloble.TRAN_CASHREMIT);
							// 如果币种是韩元或卢布 钞汇标志只显示现钞不显示现汇
							if (currencyCode.equals(ConstantGloble.PRMS_CODE_HANYUAN1)
									|| currencyCode.equals(ConstantGloble.PRMS_CODE_LUBU1)) {
								cashRemit = ConstantGloble.CASHREMIT_01;
								cashRemitList.add(0, cashRemit);
								cashRemitFlagList.add(LocalData.cashRemitBackMap.get(cashRemit));
								break;
							}
							cashRemitList.add(cashRemit);
							cashRemitFlagList.add(LocalData.cashRemitBackMap.get(cashRemit));

						}
						ArrayAdapter<String> cashRemitSpinnerAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse()
								.getCurrentAct(), R.layout.dept_spinner, cashRemitFlagList);
						cashRemitSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						cashRemitSpinner.setAdapter(cashRemitSpinnerAdapter);
						cashRemitSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
								cashRemit = cashRemitList.get(position);
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
								cashRemitList.get(0);
							}
						});
						saveMoneyEt.setHint("");
						if (LocalData.prmsTradeStyleCodeCurrencyList.contains(currencyCode)) {
							initSaveTimeSpinner(saveTimeSpinner, saveTimeTwoList); // 存期
						} else {
							initSaveTimeSpinner(saveTimeSpinner, saveTimeForeList); // 存期
						}
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					currencyCode = codeList.get(0);
				}
			});
		}
		attachMessageEt = (EditText) linearLayout.findViewById(R.id.dept_message_et);
		EditTextUtils.setLengthMatcher(SaveRegularActivity.this, attachMessageEt, 50);

		bottomLayout.removeAllViews();
		bottomLayout.addView(linearLayout);
		initBottomBtnlayout(linearLayout, WHOLE_SAVE_BOTTOM);
	}

	/**
	 * 当币种是人名币的时候 钞汇标志spinner显示默认是“-”
	 * 
	 * @param cashRemitSpinner
	 */
	private void initDefaultCashRemitSpinner(Spinner cashRemitSpinner) {
		ArrayAdapter<String> cashRemitSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner,
				LocalData.nullcashremitList);
		cashRemitSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cashRemitSpinner.setAdapter(cashRemitSpinnerAdapter);
		cashRemitSpinner.setEnabled(false);
		cashRemitSpinner.setBackgroundDrawable(SaveRegularActivity.this.getResources().getDrawable(
				R.drawable.bg_spinner_default));
		String strCashRemit = LocalData.tranCashremitList.get(0);
		cashRemit = LocalData.cashRemitMap.get(strCashRemit);
	}

	/**
	 * 存期spinner
	 * 
	 * @param saveTimeSpinner
	 * @param saveTimeList
	 *            所要展示的内容
	 */
	private void initSaveTimeSpinner(Spinner saveTimeSpinner, final List<String> saveTimeList) {
		ArrayAdapter<String> saveTimeSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, saveTimeList);
		saveTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		saveTimeSpinner.setAdapter(saveTimeSpinnerAdapter);
		saveTimeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String strSaveTime = saveTimeList.get(position);
				saveTime = LocalData.SaveRegularCDPeriodValue.get(strSaveTime);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				String strSaveTime = saveTimeList.get(0);
				saveTime = LocalData.SaveRegularCDPeriodValue.get(strSaveTime);
			}
		});
	}

	/**
	 * 设置listLayout 高度
	 * 
	 * @param layout
	 */
	// private void setLayoutHeight(LinearLayout layout) {
	// // 根据返回数据 初始化mAccOutListLayout的高度
	// int padding = getResources().getDimensionPixelSize(
	// R.dimen.fill_margin_left) * 2;
	// List<Map<String, Object>> accOutList = DeptDataCenter.getInstance()
	// .getAccountOutList();
	// if (accOutList.size() > 0 && accOutList.size() <= 6) {
	// LayoutParams lp = new LayoutParams(LayoutValue.SCREEN_WIDTH
	// - padding, accOutList.size() * itemHeight);
	// layout.setLayoutParams(lp);
	// } else if (accOutList.size() > 6) {
	// LayoutParams lp = new LayoutParams(LayoutValue.SCREEN_WIDTH
	// - padding, itemHeight * 6);
	// layout.setLayoutParams(lp);
	// }
	// }

	/**
	 * 校验存款金额
	 * 
	 * @param amount 金额
	 * @return 合格 true，不合格 false
	 */
	private boolean checkAmount(String amount, int businessTypeFlag) {
		if (ConstantGloble.PRMS_CODE_YEN1.equals(currencyCode) || ConstantGloble.PRMS_CODE_YEN2.equals(currencyCode)) {
			RegexpBean regAmount = new RegexpBean(this.getResources().getString(R.string.save_money_message), amount,
					"tranSpAmount");
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(regAmount);
			if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
				return false;
			}
		} else {
			RegexpBean regAmount = new RegexpBean(this.getResources().getString(R.string.save_money_message), amount,
					"tranAmount");
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(regAmount);
			if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
				return false;
			}
		}
		// if (Double.parseDouble(amount) >
		// Double.parseDouble(availableBalance)) {
		// BaseDroidApp.getInstanse().showInfoMessageDialog(amountWrong);
		// return false;
		// }
		String errorMsg = "";
		if (businessTypeFlag == NOTIFY_ONE_DAY_BOTTOM || businessTypeFlag == NOTIFY_SEVEN_DAY_BOTTOM) {// 如果是通知存款
			if (ConstantGloble.PRMS_CODE_RMB.equals(currencyCode)) {// 根据不同货币提示不同信息
				errorMsg = this.getResources().getString(R.string.least_notify_save_money);
				if (Double.parseDouble(amount) < 50000) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(errorMsg);
					return false;
				}
			} else {
				// errorMsg = curCurrencyName + "起存金额为50,000元";
			}
		} else {
			// 只校验人民币起存金额
			if (ConstantGloble.PRMS_CODE_RMB.equals(currencyCode)) {
				if (Double.parseDouble(amount) < 50) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							this.getResources().getString(R.string.least_save_money));
					return false;
				}
			}
		}
		return true;
	}

	/** 弹出框的返回按钮监听 */
	private OnClickListener backListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
		}
	};

	/**
	 * 判断用户是否选择了转入转出账户
	 * 
	 * @return true 选择了 false 没有选择
	 */
	private boolean isInfoChoosed() {
		if (isTranOutFirst) {
			// BaseDroidApp.getInstanse()
			// .showInfoMessageDialog(chooseOutNoMessage);
			CustomDialog.toastInCenter(SaveRegularActivity.this, chooseOutNoMessage);
			return false;
		} else if (isTranInFirst) {
			// BaseDroidApp.getInstanse().showInfoMessageDialog(chooseInNoMessage);
			CustomDialog.toastInCenter(SaveRegularActivity.this, chooseInNoMessage);
			return false;
		}
		return true;
	}

	private OnClickListener titleClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			PopupWindowUtils.getInstance().setOnPullSelecterListener(
					SaveRegularActivity.this,
					v,
					new String[] { getResources().getString(R.string.save_whole),
							getResources().getString(R.string.save_random), getResources().getString(R.string.save_one_day),
							getResources().getString(R.string.save_seven_day) }, null, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							switch (v.getId()) {
							case R.id.tv_text1:
								initWholeSaveBottomLayout();
								break;
							case R.id.tv_text2:
								initRandomLayout();
								break;
							case R.id.tv_text3:
								initOneOrSevenBottomLayout(ONE_DAY_FLAG);
								break;
							case R.id.tv_text4:
								initOneOrSevenBottomLayout(SEVEN_DAY_FLAG);
								break;
							}
						}
					});
		}
	};

	/**
	 * 组装数据 讲币种和炒汇标志对应起来
	 * 
	 * @param detailList
	 */
	private void combineData(List<Map<String, Object>> detailList, String number) {
		currencyAndCashRemitList = new ArrayList<Map<String, Object>>();
		currencyList = new ArrayList<String>();
		cashRemitList = new ArrayList<String>();
		// 找出列表中所有不相同的币种
		for (int i = 0; i < detailList.size(); i++) {
			Map<String, Object> detaimap = detailList.get(i);
			String currencyCode = (String) detaimap.get(Tran.CURRENCYCODE_RES);
			// 过滤掉贵金属
			if (StringUtil.isNullOrEmpty(currencyCode) || LocalData.prmsTradeStyleCodeList.contains(currencyCode)) {
				continue;
			}
			if (currencyList.size() >= 0) {
				if (!currencyList.contains(currencyCode)) {
					if (currencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 如果是人民币
						currencyList.add(0, currencyCode);
					} else {
						// currencyList.add(currencyCode);
						if (number == ConstantGloble.WHOLE) {
							if (StringUtil.isNullOrEmpty(currencyCode)
									|| LocalData.SaveRegularIlterCodeList.contains(currencyCode)) {
								currencyList.add(currencyCode);
								// continue;
							}
						}
						// 七天预约支取币种过滤
						if (number == ConstantGloble.SEVEN) {
							if (StringUtil.isNullOrEmpty(currencyCode)
									|| LocalData.SaveRegularCurrencyCodeList.contains(currencyCode)) {
								currencyList.add(currencyCode);
								// continue;
							}
						}
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
				String currencyCode = (String) detaimap.get(Tran.CURRENCYCODE_RES);
				if (StringUtil.isNullOrEmpty(currencyCode)) {
					continue;
				}
				String cashRemit = (String) detaimap.get(Tran.ACCOUNTDETAIL_CASHREMIT_RES);
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

	// ///////////////////////////////////////////////////////////////////
	/**
	 * 判断用户是否选择了转入账户
	 * 
	 * @return true 选择了 false 没有选择
	 */
	private boolean isInfoChoosedFirst() {
		if (isTranInFirst) {
			return false;
		}
		return true;
	}

	/** 立即执行跳转 */
	private void immediateIntent(int businessTypeFlag) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(ConstantGloble.TYPE_FLAG, businessTypeFlag);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, ConstantGloble.NOWEXE);
		intent.putExtra(Dept.CURRENCY, currencyCode);
		intent.putExtra(Dept.CASHREMIT, cashRemit);
		intent.putExtra(Dept.CD_TERM, saveTime);
		intent.putExtra(Dept.AMOUNT, saveAmount);
		intent.putExtra(Dept.MEMO, attachMessage);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE, promiseWay);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_BUSINESS_TYPE, businessType);
		intent.setClass(context, SaveConfirmActivity.class);
		startActivity(intent);
	}

	/** 预约日期执行 */
	private void dateIntent(int businessTypeFlag) {
		Intent intent = new Intent();
		intent.putExtra(ConstantGloble.TYPE_FLAG, businessTypeFlag);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, ConstantGloble.PREDATEEXE);
		intent.putExtra(Dept.CURRENCY, currencyCode);
		intent.putExtra(Dept.CASHREMIT, cashRemit);
		intent.putExtra(Dept.CD_TERM, saveTime);
		intent.putExtra(Dept.AMOUNT, saveAmount);
		intent.putExtra(Dept.MEMO, attachMessage);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE, promiseWay);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_BUSINESS_TYPE, businessType);
		intent.setClass(context, SaveDateExcActivity.class);
		startActivity(intent);
	}

	/** 预约周期执行 */
	private void periodIntent(int businessTypeFlag) {
		Intent intent = new Intent();
		intent.putExtra(ConstantGloble.TYPE_FLAG, businessTypeFlag);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, ConstantGloble.PREPERIODEXE);
		intent.putExtra(Dept.CURRENCY, currencyCode);
		intent.putExtra(Dept.CASHREMIT, cashRemit);
		intent.putExtra(Dept.CD_TERM, saveTime);
		intent.putExtra(Dept.AMOUNT, saveAmount);
		intent.putExtra(Dept.MEMO, attachMessage);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE, promiseWay);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_BUSINESS_TYPE, businessType);
		intent.setClass(context, SaveWeekExcActivity.class);
		startActivity(intent);
	}

	// //////////////////////////////////////////////////////////////////////////////////
	// // 手续费试算 start /////
	// /////////////////////////////////////////////////////////////////////////////////
	/** 教育储蓄续存便费用试算-----回调 */
	public void requsetPsnExtendEducationDepositeComChgeCallback(Object resultObj) {
		super.requsetPsnExtendEducationDepositeComChgeCallback(resultObj);
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		// 将数据放入数据中心
		DeptDataCenter.getInstance().setEducationStockpileMap(result);
		communicationCallBack(EXTEND_EDUCATION_CALLLBAK);

		intentSaceCheckOutConfirm();
	}

	/** 零存整取续存便费用试算-----回调 */
	public void requsetPsnExtendLingcunDepositeComChgeCallback(Object resultObj) {
		super.requsetPsnExtendLingcunDepositeComChgeCallback(resultObj);
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		// 将数据放入数据中心
		DeptDataCenter.getInstance().setZeroDepositMap(result);
		communicationCallBack(EXTEND_EDUCATION_CALLLBAK);

		intentSaceCheckOutConfirm();
	}

	/** 教育储蓄零存整取跳转 */
	private void intentSaceCheckOutConfirm() {
		// 立即执行
		Intent intent = new Intent();
		intent.putExtra(Dept.MEMO, attachMessage);
		intent.putExtra(Dept.AMOUNT, saveMoney);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, executeType);
		intent.putExtra("isSaveRegEdu", true);
		intent.setClass(context, MyRegSaveCheckOutConfirmActivity.class);
		startActivity(intent);
	}

	/** 七天通知存款&一天通知存款 手续费试算-----回调 */
	public void requsetPsnCurrentSavingToCallDepositComChgeCallback(Object resultObj) {
		super.requsetPsnCurrentSavingToCallDepositComChgeCallback(resultObj);
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		DeptDataCenter.getInstance().setNotificationMap(result);

		if (businessType.equals(BUSINESSTYPE_ONEDAY)) { // 一天通知存款跳转
			int businessTypeFlag = NOTIFY_ONE_DAY_BOTTOM;
			if (buttonFlag == IMMEDIATELY_EXCUTE) { // 立即执行
				immediateIntent(businessTypeFlag);
			} else if (buttonFlag == PRE_DATE_EXCUTE) { // 预约日期执行
				dateIntent(businessTypeFlag);
			} else if (buttonFlag == PRE_WEEK_EXCUTE) { // 预约周期执行
				periodIntent(businessTypeFlag);
			}
		} else if (businessType.equals(BUSINESSTYPE_SEVENDAY)) { // 七天通知存款跳转
			int businessTypeFlag = NOTIFY_SEVEN_DAY_BOTTOM;
			if (buttonFlag == IMMEDIATELY_EXCUTE) { // 立即执行
				immediateIntent(businessTypeFlag);
			} else if (buttonFlag == PRE_DATE_EXCUTE) { // 预约日期执行
				dateIntent(businessTypeFlag);
			} else if (buttonFlag == PRE_WEEK_EXCUTE) { // 预约周期执行
				periodIntent(businessTypeFlag);
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////
	// // 手续费试算 end /////
	// /////////////////////////////////////////////////////////////////////////////////
	
	// ==========================================================================================================
	// add by luqp 2016年3月21日 利率上浮1.3倍 start
	/** 初始化整存整取 利率上浮1.3倍 底部视图 */
	private void InterestRateFloatingLayout() { // TODO
		setTitle(R.string.save_whole); // 改变顶部title
		String strTitle = context.getResources().getString(R.string.save_whole);
		titleTv.setText(strTitle);
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.dept_add_interest_rate, null); // 跳转到新增转入账户
		final Spinner cashRemitSpinner = (Spinner) linearLayout.findViewById(R.id.dept_money_mark_spinner); // 钞汇标志
		final Spinner saveTimeSpinner = (Spinner) linearLayout.findViewById(R.id.dept_save_time_spinner); // 存期
		final LinearLayout promiseWay_ll = (LinearLayout)linearLayout.findViewById(R.id.ll_promise_way); // 转存方式
		saveMoneyEt = (EditText) linearLayout.findViewById(R.id.dept_save_money_et); // 转账金额
		renmingbiTv = (TextView) linearLayout.findViewById(R.id.tv_renmingbi); //人民币 TextView
		final Button preDateExeBtn = (Button) linearLayout.findViewById(R.id.dept_preDateExe_btn); //预约日期执行 按钮
		final Button prePeriodExeBtn = (Button) linearLayout.findViewById(R.id.dept_prePeriodExe_btn); //预约周期执行 按钮
		promiseType = (TextView) linearLayout.findViewById(R.id.promise_way_type); // 转存方式
		tv_prompt_new = (TextView) linearLayout.findViewById(R.id.tv_prompts); //优惠利率提示信息  //提示信息默认不显示,只有1.3倍显示
		preDateExeBtn.setVisibility(View.GONE); // 预约日期支取
		prePeriodExeBtn.setVisibility(View.GONE); // 预约周期支取

		@SuppressWarnings("unchecked")
		final List<Map<String, Object>> detailList = (List<Map<String, Object>>) DeptDataCenter.getInstance()
				.getCurDetailContent().get(ConstantGloble.ACC_DETAILIST);
		// 为空处理
		if (StringUtil.isNullOrEmpty(detailList)) {
			return;
		}

		combineData(detailList, ConstantGloble.WHOLE);

		final List<String> currencyCodeFlagList = new ArrayList<String>();
		final List<String> codeList = new ArrayList<String>();
		for (int i = 0; i < currencyList.size(); i++) {
			String currencyCode = currencyList.get(i);
			// 过滤币种
			if (StringUtil.isNullOrEmpty(currencyCode) || LocalData.SaveRegularIlterCodeList.contains(currencyCode)) {
				currencyCodeFlagList.add(LocalData.Currency.get(currencyCode));
				codeList.add(currencyCode);
				// continue;
			}
		}

		if (StringUtil.isNullOrEmpty(codeList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("您的账户没有可用币种");
			return;
		}
		currencyCode = codeList.get(0);
		currencyCode = ConstantGloble.PRMS_CODE_RMB;
		renmingbiTv.setText(LocalData.Currency.get(currencyCode));

		cashRemitSpinner.setBackgroundDrawable(SaveRegularActivity.this.getResources().getDrawable(
				R.drawable.bg_spinner_default));
		ArrayAdapter<String> cashRemitSpinnerAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse().getCurrentAct(),
				R.layout.dept_spinner, new String[] { "-" });
		cashRemitSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cashRemitSpinner.setAdapter(cashRemitSpinnerAdapter);
		cashRemitSpinner.setEnabled(false);
		initDefaultCashRemitSpinner(cashRemitSpinner);
		final String strSaveRegNew = this.getResources().getString(R.string.least_save_money_new);
		final String strSaveReg = this.getResources().getString(R.string.least_save_money);
		final String strSavePrpmptNew = this.getResources().getString(R.string.least_save_prpmpt_new);
		/** 优惠利率提示信息*/
		final String strPrpmptNew = this.getResources().getString(R.string.mysave_prpmpt);
		final String strMysavePrpmptNew = this.getResources().getString(R.string.mysave_prpmpt_new);
		// initSaveTimeSpinner(saveTimeSpinner, saveTimeListNew); // 存期
		// ==============================================================================================
		// add by luqp 2016年3月21日 利率上浮1.3倍 存期
		setSpinnnerAdapter(saveTimeSpinner, fixedDumbledoreSpinner.getKeys());
		saveTimeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				interestRateKey = fixedDumbledoreSpinner.getKeys().get(position);
				saveTime = fixedDumbledoreSpinner.getValueFromKey(interestRateKey);
				isShowInterestRateTag = fixedDumbledoreSpinner.getTagFromKey(interestRateKey);
				saveMoneyEt.getText().clear();
				promiseWay_ll.setVisibility(View.GONE); // 转存方式和提示信息 只有1.3倍显示
				tv_prompt_new.setVisibility(View.GONE); // 转存方式和提示信息 只有1.3倍显示
				if (isShowInterestRateTag == true) {
					saveMoneyEt.setHint(strSaveReg); // 存款金额默认提示 50
				} else { //1.3倍&定利多提示信息
					// add by luqp 2016年8月26日 1.3倍与定利多起存金额提示
					if(interestRateKey.equals("六个月定利多升级版")||interestRateKey.equals("一年定利多升级版")
							||interestRateKey.equals("两年定利多升级版")){ //定利多提示信息
//						tv_prompt_new.setText(strMysavePrpmptNew);
						saveMoneyEt.setHint(strSavePrpmptNew); // 存款金额提示 人民币起存金额为10000元
					}else{ // 1.3倍 提示信息
						promiseWay_ll.setVisibility(View.GONE); // 转存方式和提示信息 只有1.3倍显示
						tv_prompt_new.setVisibility(View.GONE); // by luqp 2016年10月9日屏蔽1.3倍转存方式和提示信息
						tv_prompt_new.setText(strPrpmptNew);
						saveMoneyEt.setHint(strSaveRegNew); // 存款金额提示 人民币起存金额为1000元
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		// ==============================================================================================
		interestRateFlag = INTEREST_TAG; // 利率上浮标志

		attachMessageEt = (EditText) linearLayout.findViewById(R.id.dept_message_et);
		EditTextUtils.setLengthMatcher(SaveRegularActivity.this, attachMessageEt, 50);
		bottomLayout.removeAllViews();
		bottomLayout.addView(linearLayout);
		initBottomBtnlayoutNew(linearLayout, WHOLE_SAVE_BOTTOM);
	}

	/** 利率上浮1.3倍 Spinner*/
	private void setSpinnnerAdapter(Spinner spinner, List<String> data) {
		ArrayAdapter<String> remitadapter = new ArrayAdapter<String>(this, R.layout.spinner_item, data);
		remitadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(remitadapter);
	}

	/**
	 * 初始化底部视图
	 * @param view 加载布局
	 * @param businessTypeFlag  存款类型 WHOLE_SAVE_BOTTOM 整存整取 利率上浮1.3倍
	 */
	private void initBottomBtnlayoutNew(final View view, final int businessTypeFlag) {
		// 立即执行
		Button nowExeBtn = (Button) view.findViewById(R.id.dept_nowExe_btn);
		nowExeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveAmount = saveMoneyEt.getText().toString().trim();
				attachMessage = attachMessageEt.getText().toString().trim();
				buttonFlag = IMMEDIATELY_EXCUTE;
				executeType = "0";
				// 校验存款金额
				if (!checkAmountNew(saveAmount, businessTypeFlag)) {
					return;
				}
				if (!RegexpUtils.regexpPostscript(attachMessage)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							SaveRegularActivity.this.getResources().getString(R.string.postscript_erroe_new));
					return;
				}

				if (StringUtil.isNullOrEmpty(attachMessage)) {
					attachMessage = "";
				}

				// 格式化上传金额
				String amount = StringUtil.parseStringPattern(saveAmount, 2);
				if (isShowInterestRateTag == true) { // 不是1.3倍
					requsetPoundageIntent(businessTypeFlag); // 手续费试算
				} else { // 如果存期的Key包含1.3倍
					String strType = ""; // 定利多
					if(interestRateKey.equals("六个月定利多升级版")||interestRateKey.equals("一年定利多升级版")
							||interestRateKey.equals("两年定利多升级版")){
						strType = "14"; // 定利多
					}else{
						strType = "13"; // 产品类型 1.3倍利率整存整取：13
					}
					BaseHttpEngine.showProgressDialog();
					requsetPsnLumpMoreTimeDepositComChge(fromAccountId, toAccountId, currencyCode, cashRemit, saveTime,
							amount, strType, attachMessage);
				}
			}
		});
	}

	/**
	 * 校验存款金额
	 * @param amount 金额
	 * @return 合格 true，不合格 false
	 */
	private boolean checkAmountNew(String amount, int businessTypeFlag) {
		if (businessTypeFlag == WHOLE_SAVE_BOTTOM) {
			if (isShowInterestRateTag == true) { // 利率上浮1.3倍金额校验
				if (ConstantGloble.PRMS_CODE_RMB.equals(currencyCode)) {
					if (StringUtil.isNull(amount) || Double.parseDouble(amount) < 50) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								this.getResources().getString(R.string.least_save_check_amount));
						return false;
					}
				}
			} else { // 金额校验
				// add by luqp 2016年8月26日 1.3倍与定利多起存金额提示
				if(interestRateKey.equals("六个月定利多升级版")||interestRateKey.equals("一年定利多升级版")
						||interestRateKey.equals("两年定利多升级版")){ //定利多提示信息
					if (ConstantGloble.PRMS_CODE_RMB.equals(currencyCode)) { // 是否是人民币
						if (StringUtil.isNull(amount) || Double.parseDouble(amount) < 10000) { // 判断金额是否小于10000
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									this.getResources().getString(R.string.least_save_check_amount_b));
							return false;
						}
					}
				}else{ // 1.3倍 提示信息
					if (ConstantGloble.PRMS_CODE_RMB.equals(currencyCode)) { // 是否是人民币
						if (StringUtil.isNull(amount) || Double.parseDouble(amount) < 1000) { // 判断金额是否小于1000
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									this.getResources().getString(R.string.least_save_check_amount_a));
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	/** 定期存款-新增1.3倍整存整取费用试算（查询扩充版专用接口）-----回调 */
	public void requsetPsnLumpMoreTimeDepositComChgeCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog(); // 取消通讯框
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultList = (Map<String, Object>) biiResponseBody.getResult();
		// 将数据放入数据中心
		DeptDataCenter.getInstance().setInterestRateFloatingMap(resultList);
		int businessTypeFlag = WHOLE_SAVE_BOTTOM_NEW;
		immediateIntentNew(businessTypeFlag);
	}
	
	/** 立即执行跳转 */
	private void immediateIntentNew(int businessTypeFlag) {
		Intent intent = new Intent();
		intent.putExtra(ConstantGloble.TYPE_FLAG, businessTypeFlag);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, ConstantGloble.NOWEXE);
		intent.putExtra(Dept.CURRENCY, currencyCode);
		intent.putExtra(Dept.CASHREMIT, cashRemit);
		intent.putExtra(Dept.CD_TERM, saveTime);
		intent.putExtra(Dept.AMOUNT, saveAmount);
		intent.putExtra(Dept.MEMO, attachMessage);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE, promiseWay);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_BUSINESS_TYPE, businessType);
		intent.putExtra("IsShowInterestRateTag", isShowInterestRateTag);
		intent.putExtra("InterestRateFloatingKey", interestRateKey);
		intent.putExtra(Dept.INTEREST_TYPE_FLAG , interestRateFlag);
		
		intent.setClass(context, SaveConfirmActivity.class);
		startActivity(intent);
	}
	// add by luqp 2016年3月21日 利率上浮1.3倍 end
	// ================================================================================================
}
