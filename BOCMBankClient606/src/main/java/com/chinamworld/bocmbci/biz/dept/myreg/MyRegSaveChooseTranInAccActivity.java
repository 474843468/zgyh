package com.chinamworld.bocmbci.biz.dept.myreg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.adapter.AccInListAdapter;
import com.chinamworld.bocmbci.biz.dept.adapter.AccOutListAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * @ClassName: MyRegSaveChooseTranInAccActivity
 * @Description: 定期存款 “支取”和“续存”页面
 * @author JiangWei
 * @date 2013-8-19下午3:20:21
 */
public class MyRegSaveChooseTranInAccActivity extends DeptBaseActivity {
	private static final String TAG = "MyRegSaveChooseTranInAccActivity";
	private LayoutInflater inflater = null;
	private Context context = this;
	private LinearLayout tabcontent;// 主Activity显示
	private LinearLayout bottomLayout;
	private View view = null;
	private ListView accListView = null;
	private int currentPosition = 0;
	/** 账户类型 */
	private String accountType = null;
	/** 存单类型 */
	private String type = null;
	/** 转存方式 */
	private String convertType = null;
	/** 转出账户控件 */
	private RelativeLayout mAccOutLl = null;
	/** 转入账户控件 */
	private RelativeLayout mAccInLl = null;
	/** 转出账户详情 布局 */
	private LinearLayout accOutDetailLayout = null;
	/** 转出账户详情 布局 */
	private LinearLayout accInDetailLayout = null;
	/** 转出账户列表Layout */
	private LinearLayout mAccOutListLayout = null;
	/** 转入账户列表Layout */
	private LinearLayout mAccInListLayout = null;
	/** 附言 */
	private EditText messageEt = null;
	private String strMessage = null;
	/** 通知编号 */
	private Spinner noticeNoSpinner = null;
	private String strNoticeNo = "";
	/** 续存的月存金额 */
	private String continueMonthSave = null;
	/** 续存金额 */
	private EditText continueSaveTv = null;
	/** 支取金额 */
	private EditText checkoutMoneyEt;
	/** 可用余额 */
	private String availableBalance = null;
	/** 续存标识 */
	private String continueSaveFlag = null;
	/** 支取方式 */
	private String drawMode = null;
	/** 当前通知编号的详情 */
	private Map<String, Object> curDetail;
	/** 支取金额 */
	private String money = null;
	/** list条目高度 */
	private int itemHeight = 0;
	/** 到期日 */
	private String drawDate = null;
	/** 当前时间 */
	private String nowDate = null;
	/** 是否到期支取 */
	private boolean isExpire = true;
	/** 支取方式 */
	private int checkoutWay;
	/** 是否是从通知管理那边进入 0不是 1是 */
	private int notifyFlag = 0;
	/** 当前用户id */
	private String accountId= null;
	private List<String> noticeNoList;
	private String strBookBalance= null;
	private String strCurrency = "";
	private ScrollView scrollview;
	/** 部分支取 */
	// ///////////////////////////// 跨省手续费
	/** 转入账户省联号 */
	private String outAccountIbkNum = null;
	/** 转出账户省联号 */
	private String inAccountIbkNum = null;
	/** 教育储蓄&零存整取转出金额 */
	private String saveMoney = null;
	/** 执行方式 立即执行 预约日期执行 预约周期执行 */
	private String transMode = null;
	/** 转出账户ID */
	private String fromAccountId = null;
	/** 转入账户ID */
	private String toAccountId = null;
	/** 存折序号 */
	private String passBookNumber = null;
	/** 存单序号 */
	private String cdNumber = null;
	/** 钞汇 */
	private String cashRemit = null;
	/** 原存单金额 */
	private String avamoney = null;
	/** 执行日期 */
	private String executeDate = null;
	/** 转账金额 */
	private String drawMoney = null;
	// / 支取通知存款
	/** 存期 */
	private String cDTerm = null;
	/** 是否通知银行 */
	private String isScheduled = null;
	/** 通知存款 存期 一天 七天 */
	private String cdperiod = null;
	/** 支取金额 */
	private String inputAmount;
	/** Token */
	private String token = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.whole_save_checkout);
		DeptDataCenter.getInstance().isFromBoc = false;
		inflater = LayoutInflater.from(this);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this); // 加载理财产品布局
		view = inflater.inflate(R.layout.dept_myreg_choose_tran_in_acc_activity, null);
		tabcontent.addView(view);
		scrollview = (ScrollView) view.findViewById(R.id.scrollview);
		accountType = (String) DeptDataCenter.getInstance().getAccountContentMap().get("type");
		convertType = (String) DeptDataCenter.getInstance().getAccountContentMap().get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
		setLeftSelectedPosition("deptStorageCash_2");
		ibBack = (Button) this.findViewById(R.id.ib_back);
		ibBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityTaskManager.getInstance().removeAllActivity(); // 清除 activity
//				goToMainActivity();
				finish();
			}
		});
		continueSaveFlag = this.getIntent().getStringExtra(ConstantGloble.CONTINUE_SAVE_FLAG);
		notifyFlag = this.getIntent().getIntExtra(NOTIFY_FLAG, 0);
		nowDate = this.getIntent().getStringExtra(ConstantGloble.DATE_TIEM);
		DeptDataCenter.getInstance().isFromBoc = this.getIntent().getBooleanExtra("isFromBoc",false);
		// 初始化转入转出账户 布局
		initTranOutInLayout();

		initBottomLayout();
	}

	/** 初始化转出账户布局*/
	private void initTranOutInLayout() {
		// 转出账户布局
		mAccOutLl = (RelativeLayout) view.findViewById(R.id.ll_acc_out_mytransfer);
		mAccOutListLayout = (LinearLayout) view.findViewById(R.id.ll_acc_out_list_layout);
		// 转入账户布局
		mAccInLl = (RelativeLayout) view.findViewById(R.id.ll_acc_in_mytransfer);
		mAccInListLayout = (LinearLayout) view.findViewById(R.id.ll_acc_in_list_layout);
		// 如果是续存 默认显示的是 转入账户
		if (!StringUtil.isNullOrEmpty(continueSaveFlag) && continueSaveFlag.equals("Y")) {
			// 添加一个新的布局文件--转入账户详细信息
			showInDetailView();
			showAccInListItemData();// 显示转入账户条目的详细信息
			// mAccInLl.setOnClickListener(accInClicklistener);// 重新选择转入账户
			mAccOutLl.setOnClickListener(accOutClicklistener);// 选择转出账户
		} else {// 如果是支取 默认显示的是 转出账户
			// 添加一个新的布局文件--转出账户详细信息
			showOutDetailView();
			showAccWholeSaveOutListItemData();// 显示转出账户条目详细信息
			// mAccOutLl.setOnClickListener(accWhoeSaveOutClicklistener);
			mAccInLl.setOnClickListener(accInClicklistener);// 重新选择转入账户
		}

	}

	/** 根据不同类型的存款种类 显示不同的布局 初始化底部布局 */
	private void initBottomLayout() {
		bottomLayout = (LinearLayout) view.findViewById(R.id.dept_my_save_bottom);
		final Map<String, Object> accountDetailMap = DeptDataCenter.getInstance().getAccountContentMap();
		if (accountDetailMap == null) {
			return;
		}
		type = (String) accountDetailMap.get(Dept.TYPE);
		// 根据type 加载不同的布局
		switch (Integer.parseInt(type)) {
		case 110:// 整存整取
		case 160:// 定活两便
			LinearLayout layoutLayout1 = (LinearLayout) LayoutInflater.from(context).inflate(
					R.layout.dept_myreg_whole_save_bottom, null);
			TextView titleTv = (TextView) layoutLayout1.findViewById(R.id.dept_save_title_tv);
			TextView currencyRemitTv = (TextView) layoutLayout1.findViewById(R.id.dept_currency_cashremit_tv);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, currencyRemitTv);
			// TextView 币种
			TextView currencyTv = (TextView) layoutLayout1.findViewById(R.id.dept_currency_tv);
			strCurrency = (String) accountDetailMap.get(Comm.CURRENCYCODE);
			currencyTv.setText(LocalData.Currency.get(strCurrency));
			// TextView 钞汇标志
			TextView cashRemitTv = (TextView) layoutLayout1.findViewById(R.id.dept_cashremit_tv);
			String strCashRemit = (String) accountDetailMap.get(Dept.CASHREMIT);
			cashRemitTv.setText(LocalData.CurrencyCashremit.get(strCashRemit));
			// 支取方式
			LinearLayout dept_checkout_way_layout = (LinearLayout) layoutLayout1.findViewById(R.id.dept_checkout_way_layout);
			// 支取方式下拉框
			LinearLayout ll_checkout_way = (LinearLayout) layoutLayout1.findViewById(R.id.ll_checkout_way);
			// TextView 支取金额
			TextView checkoutMoney = (TextView) layoutLayout1.findViewById(R.id.dept_checkmoeny_tv);
			// 存单金额
			String strMoney = (String) accountDetailMap.get(Dept.BOOKBALANCE);
			// 可用金额
			String avamoney = (String) accountDetailMap.get(Dept.AVAILABLE_BALANCE);
			// 格式化 小数点
			strMoney = StringUtil.parseStringCodePattern(strCurrency, strMoney, 2);
			avamoney = StringUtil.parseStringCodePattern(strCurrency, avamoney, 2);
			checkoutMoney.setText(avamoney);
			final LinearLayout ll_dept_despite_amount = (LinearLayout) layoutLayout1
					.findViewById(R.id.ll_dept_despite_amount);
			TextView dept_despite_amount_tv = (TextView) layoutLayout1.findViewById(R.id.dept_despite_amount_tv);
			dept_despite_amount_tv.setText(StringUtil.parseStringPattern(avamoney, 2));
			final LinearLayout checkoutMoenyLayout = (LinearLayout) layoutLayout1
					.findViewById(R.id.dept_checkout_money_layout);
			final LinearLayout amountLayout = (LinearLayout) layoutLayout1.findViewById(R.id.dept_amount_layout);
			final String[] i_checkout_way = new String[] { DRAW_MODE_N, DRAW_MODE_Y };
			checkoutMoneyEt = (EditText) layoutLayout1.findViewById(R.id.dept_amount_et);

			Spinner s_checkout_way = (Spinner) layoutLayout1.findViewById(R.id.s_checkout_way);
			ArrayAdapter<String> checkoutWayAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, new String[] {
					"部分支取", "全部支取" });
			checkoutWayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s_checkout_way.setAdapter(checkoutWayAdapter);
			s_checkout_way.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					drawMode = i_checkout_way[position];

					if (DRAW_MODE_N.equals(drawMode)) {
						checkoutMoenyLayout.setVisibility(View.GONE);
						amountLayout.setVisibility(View.VISIBLE);
						ll_dept_despite_amount.setVisibility(View.VISIBLE);
					} else {
						checkoutMoenyLayout.setVisibility(View.VISIBLE);
						amountLayout.setVisibility(View.GONE);
						ll_dept_despite_amount.setVisibility(View.VISIBLE);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					drawMode = i_checkout_way[0];
					checkoutMoenyLayout.setVisibility(View.GONE);
					amountLayout.setVisibility(View.VISIBLE);
					ll_dept_despite_amount.setVisibility(View.VISIBLE);
				}
			});

			LinearLayout cashRemitLayout = (LinearLayout) layoutLayout1.findViewById(R.id.dept_cashremit_layout);
			if (strCurrency.equals(ConstantGloble.PRMS_CODE_RMB)) {
				cashRemitLayout.setVisibility(View.INVISIBLE);
			}
			// 整存整取支取金额
			if (type.equals(RANDOM_SAVE)) {
				dept_checkout_way_layout.setVisibility(View.GONE);
				setTitle(R.string.random_save_title);
				titleTv.setText(R.string.random_save_title);
			} else if (type.equals(WHOE_SAVE)) {
				ll_checkout_way.setVisibility(View.VISIBLE);
				setTitle(R.string.whole_save_checkout);
				titleTv.setText(R.string.whole_save_checkout);
			}

			// TextView 存款种类
			TextView saveTypeTv = (TextView) layoutLayout1.findViewById(R.id.dept_save_type_tv);
			String strType = (String) accountDetailMap.get(Dept.TYPE);
			saveTypeTv.setText(LocalData.fixAccTypeMap.get(strType));
			// EditText 附言
			messageEt = (EditText) layoutLayout1.findViewById(R.id.dept_message_et);
			EditTextUtils.setLengthMatcher(MyRegSaveChooseTranInAccActivity.this, messageEt, 50);

			Button nowExeBtn = (Button) layoutLayout1.findViewById(R.id.dept_nowExe_btn);
			Button preDateExeBtn = (Button) layoutLayout1.findViewById(R.id.dept_preDateExe_btn);
			nowExeBtn.setOnClickListener(nowExcuteBtnListener);
			preDateExeBtn.setOnClickListener(preDateExcuteBtnListener);
			bottomLayout.removeAllViews();
			bottomLayout.addView(layoutLayout1);
			break;
		case 166:// 通知存款 分为约定转存和非约定转存
					// 约定转存 只有一个附言输入框 支取方式为 全额支取
					// 非约定转存 正常显示
			LinearLayout layoutLayout2 = (LinearLayout) LayoutInflater.from(context).inflate(
					R.layout.dept_myreg_one_seven_bottom, null);
			TextView titleTv2 = (TextView) layoutLayout2.findViewById(R.id.dept_save_title_tv);
			String cdperiod = (String) accountDetailMap.get(Dept.CD_PERIOD);
			if (cdperiod.equals(BUSINESSTYPE_ONEDAY)) {
				titleTv2.setText(oneDayTitle);
				setTitle(R.string.one_day_title);
			} else if (cdperiod.equals(BUSINESSTYPE_SEVENDAY)) {
				titleTv2.setText(sevenDayTitle);
				setTitle(R.string.seven_day_title);
			}
			TextView currencyRemitTv2 = (TextView) layoutLayout2.findViewById(R.id.dept_currency_cashremit_tv);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(context, currencyRemitTv2);
			// TextView 支取币种
			TextView currency2Tv = (TextView) layoutLayout2.findViewById(R.id.dept_checkout_currency_tv);
			strCurrency = (String) accountDetailMap.get(Comm.CURRENCYCODE);
			currency2Tv.setText(LocalData.Currency.get(strCurrency));
			// TextView 钞汇标志
			TextView cashRemit2Tv = (TextView) layoutLayout2.findViewById(R.id.dept_cashremit_tv);
			String strCashRemitflag = (String) accountDetailMap.get(Dept.CASHREMIT);
			cashRemit2Tv.setText(LocalData.CurrencyCashremit.get(strCashRemitflag));
			// 币种是人名币 不显示钞汇标志
			LinearLayout cashRemitLayout2 = (LinearLayout) layoutLayout2.findViewById(R.id.dept_cashremit_layout);
			if (strCurrency.equals(ConstantGloble.PRMS_CODE_RMB)) {
				cashRemitLayout2.setVisibility(View.INVISIBLE);
			}
			// TextView 存单金额
			TextView booksheetMoneyTv = (TextView) layoutLayout2.findViewById(R.id.dept_booksheet_money_tv);
			strBookBalance = (String) accountDetailMap.get(Dept.BOOKBALANCE);
			// TextView 可用金额
			TextView checkoutMoney2Tv = (TextView) layoutLayout2.findViewById(R.id.dept_availablebalance_tv);
			money = (String) accountDetailMap.get(Dept.AVAILABLE_BALANCE);
			// checkoutMoney2Tv.setText(StringUtil.parseStringPattern(money,
			// 2));
			// 如果是日元 和韩元 存单金额和可用金额不显示小数点位
			strBookBalance = StringUtil.parseStringCodePattern(strCurrency, strBookBalance, 2);
			money = StringUtil.parseStringCodePattern(strCurrency, money, 2);
			booksheetMoneyTv.setText(strBookBalance);
			checkoutMoney2Tv.setText(money);

			// EditText 通知编号
			LinearLayout noticeLayout = (LinearLayout) layoutLayout2.findViewById(R.id.dept_notice_no_layout);
			noticeNoSpinner = (Spinner) layoutLayout2.findViewById(R.id.dept_notice_no_spinner);
			// EditText 支取金额
			final LinearLayout checkoutMoneyLayout = (LinearLayout) layoutLayout2
					.findViewById(R.id.dept_checkout_moeny_layout);
			checkoutMoneyEt = (EditText) layoutLayout2.findViewById(R.id.dept_checkout_money_et);
			// Layout TextView通知金额
			final LinearLayout checkoutMoneyTvLayout = (LinearLayout) layoutLayout2
					.findViewById(R.id.dept_checkout_money_tvlayout);
			final TextView checkoutMoneyTv = (TextView) layoutLayout2.findViewById(R.id.dept_checkout_money_tv);
			// Layout TextView支取金额
			final LinearLayout checkMoneyBottomLayout = (LinearLayout) layoutLayout2
					.findViewById(R.id.dept_checkout_money_layout_bottom);
			final TextView checkoutMoneyBottomTv = (TextView) layoutLayout2.findViewById(R.id.dept_checkout_money_tv_bottom);
			// Layout 支取方式
			final LinearLayout checkoutWayLayout = (LinearLayout) layoutLayout2.findViewById(R.id.dept_checkout_way_layout);
			final TextView checkoutWayTv = (TextView) checkoutWayLayout.findViewById(R.id.dept_checkout_way_tv);
			// Layout 支取方式2 2个radiobutton
			final LinearLayout checkoutWayLayout2 = (LinearLayout) layoutLayout2
					.findViewById(R.id.dept_checkout_way_layout2);
			// 部分支取按钮
			final RadioButton partRb = (RadioButton) layoutLayout2.findViewById(R.id.dept_checkout_part_rb);

			RadioGroup radioGroup = (RadioGroup) layoutLayout2.findViewById(R.id.dept_checkout_way_radiogroup);
			// EditText 附言
			messageEt = (EditText) layoutLayout2.findViewById(R.id.dept_message_et);
			EditTextUtils.setLengthMatcher(MyRegSaveChooseTranInAccActivity.this, messageEt, 50);

			String strConvertType = (String) DeptDataCenter.getInstance().getAccountContentMap()
					.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
			if (strConvertType.equals(ConstantGloble.CONVERTTYPE_N)) {
				if (StringUtil.isNullOrEmpty(noticeNoList)) {
					noticeNoList = new ArrayList<String>();
				}
				List<Map<String, Object>> noticeDetailList = DeptDataCenter.getInstance().getQueryNotifyCallBackList();
				List<String> otherData = DeptDataCenter.getInstance().getNoticeIdList();
				if (otherData != null) {// add by xby 2013年8月23日10:43:15
					// 只保留 a w两种状态
					for (int i = 0; i < otherData.size(); i++) {
						String notifyStatus = (String) noticeDetailList.get(i).get(Dept.NOTIFY_STATUS);
						if (!StringUtil.isNullOrEmpty(otherData.get(i))) {
							if (notifyStatus.equals("A") || notifyStatus.equals("W")) {
								noticeNoList.add(otherData.get(i));
							}
						}
					}
				}
				final String str = this.getResources().getString(R.string.checkout_default);
				noticeNoList.add(str);
				ArrayAdapter<String> noticeNoSpinnerAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse()
						.getCurrentAct(), R.layout.dept_spinner, noticeNoList);
				noticeNoSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				noticeNoSpinner.setAdapter(noticeNoSpinnerAdapter);
				noticeNoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						strNoticeNo = noticeNoList.get(position);
						if (strNoticeNo.equals(str)) {
							checkoutWayLayout.setVisibility(View.GONE);
							checkoutWayLayout2.setVisibility(View.VISIBLE);
							partRb.setChecked(true);
							drawMode = DRAW_MODE_N;
							checkoutMoneyTvLayout.setVisibility(View.GONE);
							checkoutMoneyLayout.setVisibility(View.VISIBLE);
							checkMoneyBottomLayout.setVisibility(View.GONE);
							// checkoutMoneyTv.setText(StringUtil.parseStringPattern(money,
							// 2));
							checkoutMoneyTv.setText("-");
							isExpire = false;
							strNoticeNo = null;
							///////////////////////////////////////////////////
							// add by 2015年12月8日18:51:15 修改通知取款缓存错误问题
							if(StringUtil.isNullOrEmpty(DeptDataCenter.getInstance().getQueryNotifyCallBackList())){
								DeptDataCenter.getInstance().setCurNotifyDetail(null);	
							}
							//////////////////////////////////////////////////
						} else {
							curDetail = DeptDataCenter.getInstance().getQueryNotifyCallBackList().get(position);
							DeptDataCenter.getInstance().setCurNotifyDetail(curDetail);
							checkoutWayLayout.setVisibility(View.VISIBLE);
							checkoutWayLayout2.setVisibility(View.GONE);
							drawMode = (String) curDetail.get(Dept.DRAW_MODE);
							LogGloble.d("MyRegSaveChooseTranActivity", "支取方式： " + drawMode);
							checkoutWayTv.setText(LocalData.drawMode.get(drawMode));
							// add by wjp 2013年8月10日14:17:54
							// 部分支取的情况下显示drawAmount，全部支取的情况下显示availableBalance
							if (drawMode.equals(DRAW_MODE_N)) {
								money = (String) curDetail.get(Dept.DRAW_AMOUNT);
								checkoutMoneyLayout.setVisibility(View.VISIBLE);
								checkoutMoneyTvLayout.setVisibility(View.VISIBLE);
								checkMoneyBottomLayout.setVisibility(View.GONE);
							} else if (drawMode.equals(DRAW_MODE_Y)) {
								money = (String) accountDetailMap.get(Dept.AVAILABLE_BALANCE);
								checkoutMoneyLayout.setVisibility(View.GONE);
								checkoutMoneyTvLayout.setVisibility(View.VISIBLE);
								checkMoneyBottomLayout.setVisibility(View.VISIBLE);
							}
							// 如果是韩元和日元 不显示小数点位
							money = StringUtil.parseStringCodePattern(strCurrency, money, 2);

							checkoutMoneyTv.setText(money);
							checkoutMoneyBottomTv.setText(money);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						strNoticeNo = noticeNoList.get(0);
						drawMode = (String) DeptDataCenter.getInstance().getQueryNotifyCallBackList().get(0)
								.get(Dept.DRAW_MODE);
						LogGloble.d("MyRegSaveChooseTranActivity", "默认支取方式： " + drawMode);
						checkoutWayTv.setText(LocalData.drawMode.get(drawMode));
					}
				});
			} else {// 约定转存
				checkoutWayLayout2.setVisibility(View.GONE);
			}

			// 支取方式
			radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					switch (checkedId) {
					case R.id.dept_checkout_part_rb:// 部分支取
						checkoutMoneyLayout.setVisibility(View.VISIBLE);
						checkoutMoneyTvLayout.setVisibility(View.GONE);
						checkMoneyBottomLayout.setVisibility(View.GONE);
						drawMode = DRAW_MODE_N;
						break;
					case R.id.dept_checkout_all_rb:// 全部支取
						checkoutMoneyLayout.setVisibility(View.GONE);
						checkoutMoneyTvLayout.setVisibility(View.GONE);
						checkMoneyBottomLayout.setVisibility(View.VISIBLE);
						// 如果是违约支取 全部支取金额显示可用余额
						if (strNoticeNo == null) {
							money = (String) accountDetailMap.get(Dept.AVAILABLE_BALANCE);
							// 如果是韩元和日元 不显示小数点位
							money = StringUtil.parseStringCodePattern(strCurrency, money, 2);
							checkoutMoneyBottomTv.setText(money);
						} else {
							money = (String) curDetail.get(Dept.DRAW_AMOUNT);
							// 如果是韩元和日元 不显示小数点位
							money = StringUtil.parseStringCodePattern(strCurrency, money, 2);
							checkoutMoneyBottomTv.setText(money);
						}
						drawMode = DRAW_MODE_Y;
						break;

					default:
						break;
					}
				}
			});

			if (convertType.equalsIgnoreCase(ConstantGloble.CONVERTTYPE_R)) {// 约定转存
				noticeLayout.setVisibility(View.GONE);
				checkoutWayLayout.setVisibility(View.VISIBLE);
				checkoutMoneyLayout.setVisibility(View.GONE);
				checkoutMoneyTv.setText(money);
				drawMode = DRAW_MODE_Y;
			}
			Button nextBtn = (Button) layoutLayout2.findViewById(R.id.btnNext);
			nextBtn.setOnClickListener(preDateNextBtnListener);
			bottomLayout.removeAllViews();
			bottomLayout.addView(layoutLayout2);
			break;
		case 152:// 教育储蓄
		case 935:// 教育储蓄
		case 150:// 零存整取
		case 912:// 零存整取
			LinearLayout layoutLayout3 = (LinearLayout) LayoutInflater.from(context).inflate(
					R.layout.dept_myreg_education_bottom, null);
			// 月存金额
			continueMonthSave = (String) DeptDataCenter.getInstance().getCurDetailContent().get(Dept.MONTH_BALANCE);
			LinearLayout ll_mon_save_amount = (LinearLayout) layoutLayout3.findViewById(R.id.ll_mon_save_amount);
			TextView tv_mon_save_mount = (TextView) layoutLayout3.findViewById(R.id.tv_mon_save_mount);
			tv_mon_save_mount.setText(StringUtil.parseStringPattern(continueMonthSave, 2));
			ll_mon_save_amount.setVisibility(View.GONE);
			TextView title = (TextView) layoutLayout3.findViewById(R.id.dept_save_title_tv);
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
			strCurrency = (String) accountDetailMap.get(Comm.CURRENCYCODE);
			currency3Tv.setText(LocalData.Currency.get(strCurrency));
			// EditText 续存金额
			continueSaveTv = (EditText) layoutLayout3.findViewById(R.id.dept_save_money_et);
			// EditText 附言
			messageEt = (EditText) layoutLayout3.findViewById(R.id.dept_message_et);
			EditTextUtils.setLengthMatcher(MyRegSaveChooseTranInAccActivity.this, messageEt, 50);

			initBottomBtnlayout(layoutLayout3);
			bottomLayout.removeAllViews();
			bottomLayout.addView(layoutLayout3);
			break;
		default:
			break;
		}
	}

	/**
	 * 显示教育储蓄或者零存整取转出账户详情
	 * @param view
	 */
	private void showEduAndZeroOutListItemData(View view) {
		TextView accountTypeTv = (TextView) view.findViewById(R.id.tv_accountType_accOut_list_detail);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accountTypeTv);
		TextView nickNameTv = (TextView) view.findViewById(R.id.tv_nickName_accOut_list_detail);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, nickNameTv);
		TextView accountNumberTypeTv = (TextView) view.findViewById(R.id.tv_accountNumber_accOut_list_detail);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accountNumberTypeTv);
		TextView currencyCodeTv = (TextView) view.findViewById(R.id.tv_currey_accOut_list_detail);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, currencyCodeTv);
		TextView cashTv = (TextView) view.findViewById(R.id.tv_lastprice_accOut_list_detail);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, cashTv);

		String strAccountType = LocalData.AccountType.get((String) accOutInfoMap.get(Comm.ACCOUNT_TYPE));
		accountTypeTv.setText(strAccountType);

		nickNameTv.setText((String) accOutInfoMap.get(Comm.NICKNAME));
		String accoutNumber = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		accountNumberTypeTv.setText(StringUtil.getForSixForString(accoutNumber));

		@SuppressWarnings("unchecked")
		List<Map<String, String>> detialList = (List<Map<String, String>>) DeptDataCenter.getInstance()
				.getAccountDetailCallBackMap().get(ConstantGloble.ACC_DETAILIST);
		if (StringUtil.isNullOrEmpty(detialList)) {
			return;
		}

		boolean isHaveCNY = false;
		for (int i = 0; i < detialList.size(); i++) {
			strCurrency = (String) detialList.get(i).get(Comm.CURRENCYCODE);
			if ("001".equals(strCurrency) || "CNY".equals(strCurrency)) {
				currencyCodeTv.setText(LocalData.Currency.get(strCurrency));
				// 可用余额
				availableBalance = detialList.get(i).get(Dept.AVAILABLE_BALANCE);
				cashTv.setText(StringUtil.parseStringPattern(availableBalance, 2));
				isHaveCNY = true;
				continue;
			}
		}
		if (!isHaveCNY) {
			strCurrency = (String) detialList.get(0).get(Comm.CURRENCYCODE);
			currencyCodeTv.setText(LocalData.Currency.get(strCurrency));
			// 可用余额
			availableBalance = detialList.get(0).get(Dept.AVAILABLE_BALANCE);
			cashTv.setText(StringUtil.parseStringPattern(availableBalance, 2));
		}
		mAccOutLl.removeAllViews();
		mAccOutLl.addView(view);
	}

	/** 转入账户按钮点击事件 */
	private OnClickListener accInClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 发送通讯请求转入账户信息
			// requestAccInBankAccountList();
			accListView = new ListView(context);
			accListView.setFadingEdgeLength(0);
			accListView.setScrollingCacheEnabled(false);
			// 如果是续存 默认显示的是 转入账户
			if (!StringUtil.isNullOrEmpty(continueSaveFlag) && continueSaveFlag.equals("Y")) {
				// 如果点击的是教育储蓄续存 转入账户列表显示的就是教育储蓄账户账户列表
				if (accountType.equals(ConstantGloble.ACC_TYPE_EDU)) {// 教育储蓄
					if (DeptDataCenter.getInstance().getEducationAccountList() != null
							&& !DeptDataCenter.getInstance().getEducationAccountList().isEmpty()) {
						initInListView(DeptDataCenter.getInstance().getEducationAccountList());
						accListView.setOnItemClickListener(clickAccEducationListItem);
					}
				} else if (accountType.equals(ConstantGloble.ACC_TYPE_ZOR)) {// 零存整取
					if (DeptDataCenter.getInstance().getZerosaveAccountList() != null
							&& !DeptDataCenter.getInstance().getZerosaveAccountList().isEmpty()) {
						initInListView(DeptDataCenter.getInstance().getZerosaveAccountList());
						accListView.setOnItemClickListener(clickAccZerosaveListItem);
					}
				}
				if (accountInList != null && !accountInList.isEmpty()) {
					BaseDroidApp.getInstanse().showDeptTranoutinDialog(ConstantGloble.TRANS_ACCOUNT_OPER_IN, accListView,
							backListener, null);
				}
			} else {
				// 转入账户选择范围（借记卡、普通活期、活期一本通、长城信用卡） 发送通信取转入账户
				requestForWholeTranInNewList();
			}
		}
	};

	/** 教育储蓄 转入账户列表点击事件 */
	private OnItemClickListener clickAccEducationListItem = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			DeptDataCenter.getInstance().setAccountContent(
					DeptDataCenter.getInstance().getEducationAccountList().get(position));
			BaseDroidApp.getInstanse().dismissMessageDialog();
			// 显示转入账户详情
			showInDetailView();
			showAccInListItemData();
		}
	};

	/** 零存整取 转入账户列表点击事件 */
	private OnItemClickListener clickAccZerosaveListItem = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			DeptDataCenter.getInstance().setAccountContent(
					DeptDataCenter.getInstance().getZerosaveAccountList().get(position));
			BaseDroidApp.getInstanse().dismissMessageDialog();
			// 显示转入账户详情
			showInDetailView();
			showAccInListItemData();
		}
	};

	/** 整存整取 转入账户列表点击事件 */
	private OnItemClickListener clickWhosesaveListItem = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			DeptDataCenter.getInstance().setAccountContent(
					DeptDataCenter.getInstance().getTranoutAccountList().get(position));
			BaseDroidApp.getInstanse().dismissMessageDialog();
			// 显示转入账户详情
			showInDetailView();
			showAccInListItemData();
		}
	};

	/** 如果是续存 转出账户按钮点击事件 */
	private OnClickListener accOutClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			requestForWholeTranInList();
		}
	};
	/** 如果是支取 转出账户按钮点击事件 */
	private OnClickListener accWhoeSaveOutClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (notifyFlag == 1) {
				BaseHttpEngine.showProgressDialog();
				requestQueryNotifyAccountList(); // 发送通讯查定期一本通数据
			} else {
				if (DeptDataCenter.getInstance().getWholesaveAccountList() != null
						&& !DeptDataCenter.getInstance().getWholesaveAccountList().isEmpty()) {
					initOutListView(DeptDataCenter.getInstance().getWholesaveAccountList());
					accListView.setOnItemClickListener(clickWholesaveAccOutListItem);// 点击转出账户ListView的的条目
					// 弹出转出转换列表框
					BaseDroidApp.getInstanse().showDeptTranoutinDialog(ConstantGloble.TRANS_ACCOUNT_OPER_OUT, accListView,
							backListener, null);
				}
			}
		}
	};

	/** 续存 转出账户列表ListView的监听事件 */
	private OnItemClickListener clickAccOutListItem = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
			accOutInfoMap = DeptDataCenter.getInstance().getTranoutAccountList().get(position);
			accountId = (String) DeptDataCenter.getInstance().getTranoutAccountList().get(position).get(Comm.ACCOUNT_ID);
			// 转出账户ID
			fromAccountId = (String) DeptDataCenter.getInstance().getTranoutAccountList().get(position).get(Comm.ACCOUNT_ID);
			// 转出省联号 TODO
			outAccountIbkNum = (String) DeptDataCenter.getInstance().getTranoutAccountList().get(position)
					.get(ConstantGloble.accountIbkNum);
			// 如果用户已选择 转入账户 在次点击转出账户时 判断是否为同省 同省不做处理 非同省弹出提示框
			// 用户选择转入账户与转出账户是否为同省, 同省非同省全部调用试算手续费接口
			if (!StringUtil.isNullOrEmpty(outAccountIbkNum) && !StringUtil.isNullOrEmpty(inAccountIbkNum)
					&& outAccountIbkNum.equals(inAccountIbkNum)) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
				requestQueryAccountDetail(accountId);
			} else { // 提示用户非同省将会收取手续费
				// 弹出提示 手续费试算提示 同意显示转出账户  取消停留在选择页面
				BaseDroidApp.getInstanse().showErrorDialog(getResources().getString(R.string.saceregular_cost_prompted),
						R.string.saceregular_cancel, R.string.saceregular_countersign, new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (v.getId() == R.id.retry_btn) {
									BaseDroidApp.getInstanse().closeAllDialog();
									BaseDroidApp.getInstanse().dismissMessageDialog();
									requestQueryAccountDetail(accountId);
								} else { // 用户点击取消
									BaseDroidApp.getInstanse().dismissErrorDialog();
									return;
								}
							}
						});
			}
		}
	};

	/** 支取 转出账户列表ListView的监听事件 弹出dialog框 */
	private OnItemClickListener clickWholesaveAccOutListItem = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
			currentPosition = position;
			accOutInfoMap = DeptDataCenter.getInstance().getWholesaveAccountList().get(position);
			accountId = (String) DeptDataCenter.getInstance().getWholesaveAccountList().get(position).get(Comm.ACCOUNT_ID);
			requestQueryAccountDetail(accountId);
		}
	};

	/** 整存整取 和 定活两便 立即执行监听 */
	private OnClickListener nowExcuteBtnListener = new OnClickListener() {
		String availableAmount = (String) DeptDataCenter.getInstance().getAccountContentMap().get(Dept.AVAILABLE_BALANCE);
//		String drawMoney = availableAmount;

		@Override
		public void onClick(View v) {
			drawMoney = availableAmount;
			// 原存单金额
			availableAmount = (String) DeptDataCenter.getInstance().getAccountContentMap().get(Dept.AVAILABLE_BALANCE);
			// 存折号
			passBookNumber = (String) DeptDataCenter.getInstance().getAccountContentMap().get(Dept.VOLUME_NUMBER);
			// 存单号
			cdNumber = (String) DeptDataCenter.getInstance().getAccountContentMap().get(Dept.CD_NUMBER);
			// 钞汇
			cashRemit = (String) DeptDataCenter.getInstance().getAccountContentMap().get(Dept.CASHREMIT);
			// 执行日期
			executeDate = (String) DeptDataCenter.getInstance().getAccountContentMap().get(Dept.INTEREST_ENDDATE);
			// 执行类型 立即执行
			transMode = ConstantGloble.NOWEXE;



			//立即执行
			if (accInInfoMap == null) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(chooseInNoMessage);
				return;
			}
			strMessage = messageEt.getText().toString().trim();
			if (StringUtil.isNullOrEmpty(strMessage)) {
				strMessage = "-";
			}
			if (WHOE_SAVE.equals(type) && DRAW_MODE_N.equals(drawMode)) { // 整存整取
				// 部分支取 需要校验金额
				drawMoney = checkoutMoneyEt.getText().toString().trim();
				if (!checkAmount(drawMoney, AMOUNT_OBTAIN)) {
					return;
				}

				if (StringUtil.isNullOrEmpty(availableAmount)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("转出账户没有可用余额，请重新选择");
					return;
				}

				if (Double.valueOf(drawMoney) > Double.valueOf(availableAmount)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("支取金额大于账户可用余额，请重新输入");
					return;
				}
			}

			if (type.equals(WHOE_SAVE)) {
				// 整存整取弹出提示
				BaseDroidApp.getInstanse().showErrorDialog(getResources().getString(R.string.draw_money_por),
						R.string.cancel_draw_money, R.string.continue_draw_money, new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (v.getId() == R.id.retry_btn) {
//									skipSaveCheckOutConfirmActivity(drawMoney, availableAmount);
									BaseDroidApp.getInstanse().dismissErrorDialog();
									drawLumpReQuset();
								} else {
									BaseDroidApp.getInstanse().closeAllDialog();
								}
							}
						});
			} else {
//				skipSaveCheckOutConfirmActivity(drawMoney, availableAmount);
				drawFixedRequset();
			}

		}
	};

	/** 支取整存整取试算请求 */
	private void drawLumpReQuset() {
		BaseHttpEngine.showProgressDialog();
		requsetPsnDepositWithdrawComChge(fromAccountId, toAccountId, strCurrency, cashRemit, drawMoney, availableBalance,
				strMessage, passBookNumber, cdNumber, transMode);
	}


	/** 支取整存整取费用试算-----回调 */
	public void requsetPsnDepositWithdrawComChgeCallback(Object resultObj) {
		super.requsetPsnDepositWithdrawComChgeCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		// 将数据放入数据中心
		DeptDataCenter.getInstance().setDrawMoneyMap(result);

		skipSaveCheckMoneConfirmActivity();
	}

	/** 支取定活两便试算请求 */
	private void drawFixedRequset() {
		BaseHttpEngine.showProgressDialog();
		requsetPsnTerminalOrCurrentDepositComChge(fromAccountId, toAccountId, strCurrency, cashRemit, drawMoney, strMessage,
				passBookNumber, cdNumber, transMode);
	}

	/** 支取定活两便费用试算-----回调 */
	public void requsetPsnTerminalOrCurrentDepositComChgeCallback(Object resultObj) {
		super.requsetPsnTerminalOrCurrentDepositComChgeCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		// 将数据放入数据中心
		DeptDataCenter.getInstance().setDrawFixedMoneyMap(result);

		skipSaveCheckMoneConfirmActivity();
	}

	/** 整存整取 定活两便跳转 */
	private void skipSaveCheckMoneConfirmActivity() {
		String availableAmount = (String) DeptDataCenter.getInstance().getAccountContentMap().get(Dept.AVAILABLE_BALANCE);
		String drawMoney = availableAmount;
		drawMoney = (String) DeptDataCenter.getInstance().getAccountContentMap().get(Dept.AVAILABLE_BALANCE);

		if (accInInfoMap == null) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(chooseInNoMessage);
			return;
		}
		if (WHOE_SAVE.equals(type) && DRAW_MODE_N.equals(drawMode)) { // 整存整取
			// 部分支取
			// 需要校验金额
			drawMoney = checkoutMoneyEt.getText().toString().trim();
			if (!checkAmount(drawMoney, AMOUNT_OBTAIN)) {
				return;
			}

			if (StringUtil.isNullOrEmpty(availableAmount)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("转出账户没有可用余额，请重新选择");
				return;
			}

			if (Double.valueOf(drawMoney) > Double.valueOf(availableAmount)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("支取金额大于账户可用余额，请重新输入");
				return;
			}

			Intent intent = new Intent();
			intent.putExtra(Dept.MEMO, strMessage);
			drawDate = (String) DeptDataCenter.getInstance().getAccountContentMap().get(Dept.INTEREST_ENDDATE);
			intent.putExtra(Dept.AMOUNT, drawMoney);
			intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, transMode);
			intent.putExtra(Dept.DRAW_MODE, drawMode);
			intent.putExtra("isFromBoc", DeptDataCenter.getInstance().isFromBoc);
			intent.setClass(context, MyRegSaveCheckOutConfirmActivity.class);
			startActivity(intent);
		} else { // 全部支取
			Intent intent = new Intent();
			intent.putExtra(Dept.MEMO, strMessage);
			drawDate = (String) DeptDataCenter.getInstance().getAccountContentMap().get(Dept.INTEREST_ENDDATE);
			intent.putExtra(Dept.AMOUNT, drawMoney);
			intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, transMode);
			intent.putExtra(Dept.DRAW_MODE, drawMode);
			intent.putExtra("isFromBoc", DeptDataCenter.getInstance().isFromBoc);
			intent.setClass(context, MyRegSaveCheckOutConfirmActivity.class);
			startActivity(intent);
		}
	}

	private void skipSaveCheckOutConfirmActivity(String drawMoney, String availableAmount) {
		Intent intent = new Intent();
		intent.putExtra(Dept.MEMO, strMessage);
		drawDate = (String) DeptDataCenter.getInstance().getAccountContentMap().get(Dept.INTEREST_ENDDATE); //null
		intent.putExtra(Dept.AMOUNT, drawMoney);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, ConstantGloble.NOWEXE);  //0
		intent.putExtra(Dept.DRAW_MODE, drawMode); //null
		intent.putExtra("isFromBoc", DeptDataCenter.getInstance().isFromBoc);
		intent.setClass(context, MyRegSaveCheckOutConfirmActivity.class);
		startActivity(intent);
	}


	/** 整存整取 和 定活两便 预约日期执行监听 */
	private OnClickListener preDateExcuteBtnListener = new OnClickListener() {
		String availableAmount = (String) DeptDataCenter.getInstance().getAccountContentMap().get(Dept.AVAILABLE_BALANCE);
		String drawMoney = availableAmount;

		@Override
		public void onClick(View v) {
			drawMoney = (String) DeptDataCenter.getInstance().getAccountContentMap().get(Dept.AVAILABLE_BALANCE);
			// 预约日期执行
			if (accInInfoMap == null) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(chooseInNoMessage);
				return;
			}
			strMessage = messageEt.getText().toString().trim();
			if (StringUtil.isNullOrEmpty(strMessage)) {
				strMessage = "-";
			} else {
				// return;
			}

			if (WHOE_SAVE.equals(type) && DRAW_MODE_N.equals(drawMode)) { // 整存整取 部分支取 校验金额
				drawMoney = checkoutMoneyEt.getText().toString().trim();
				if (!checkAmount(drawMoney, AMOUNT_OBTAIN)) {
					return;
				}
				if (StringUtil.isNullOrEmpty(availableAmount)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("转出账户没有可用余额，请重新选择");
					return;
				}
				if (Double.valueOf(drawMoney) > Double.valueOf(availableAmount)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("支取金额大于账户可用余额，请重新输入");
					return;
				}
			}
			if (type.equals(WHOE_SAVE)) {
				// 整存整取弹出提示
				BaseDroidApp.getInstanse().showErrorDialog(getResources().getString(R.string.order_draw_money_por),
						R.string.cancel_draw_money, R.string.continue_draw_money, new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (v.getId() == R.id.retry_btn) {
									skipOrderPage(drawMoney);
								} else {
									BaseDroidApp.getInstanse().closeAllDialog();
								}
							}
						});
			} else {
				skipOrderPage(drawMoney);
			}

		}
	};

	private void skipOrderPage(String drawMoney) {
		Intent intent = new Intent();
		intent.putExtra(Dept.MEMO, strMessage);
		intent.putExtra(Dept.AMOUNT, drawMoney);
		intent.putExtra(Dept.DRAW_MODE, drawMode);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, ConstantGloble.PREDATEEXE);
		intent.putExtra("isFromBoc", DeptDataCenter.getInstance().isFromBoc);
		intent.setClass(context, MyRegSaveCheckOutConfirmActivity.class);
		startActivity(intent);
	}


	/** 通知存款底部 下一步按钮监听 */
	private OnClickListener preDateNextBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			strMessage = messageEt.getText().toString().trim();
			if (StringUtil.isNullOrEmpty(strMessage)) {
				strMessage = "-";
			}
			if (accInInfoMap == null) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(chooseInNoMessage);
				return;
			}

			// //////////////////////////////////////////////////////////////////
			// add by luqp 2015年11月23日10:27:55 手续费试算上送字段
			Map<String, Object> contents = DeptDataCenter.getInstance().getCurDetailContent();
			// 存折号
			passBookNumber = (String) contents.get(Dept.VOLUME_NUMBER);
			// 存单号
			cdNumber = (String) contents.get(Dept.CD_NUMBER);
			// 钞汇
			cashRemit = (String) contents.get(Dept.CASHREMIT);
			// 存期
			cDTerm = (String) contents.get(Dept.CD_TERM);
			// 转存方式
			convertType = (String) contents.get(Dept.DEPT_CONVERTTYPE_RES);
			// 是否通知银行
			isScheduled = String.valueOf(notifyFlag);
			// 执行方式
			transMode = ConstantGloble.NOWEXE;
			// /////////////////////////////////////////////////////////////////

			inputAmount = availableBalance;
			if (drawMode.equals(DRAW_MODE_N)) { // 部分支取
				inputAmount = checkoutMoneyEt.getText().toString().trim();
				RegexpBean regAmount = new RegexpBean(getResources().getString(R.string.checkout_money_no_label),
						inputAmount, "tranAmount");
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(regAmount);
				if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
					return;
				}

				// add by lqp 2015年11月12日10:42:57 部分支取并且是人民币 支取金额和存单剩余金额小于5W提示.
				if (ConstantGloble.PRMS_CODE_RMB.equals(strCurrency)) {
					String errorMsg = MyRegSaveChooseTranInAccActivity.this.getResources().getString(R.string.notice_save_money);
					// //////////////////////////////////////////////////////////////////////
					// add by 2015年11月23日10:41:51 部分支取并且是人民币 支取金额和存单剩余金额小于5W提示.
					String strBalance = StringUtil.deleateNumber(strBookBalance); // 把存单金额
					// 格式化成整数位
					if (strBalance.contains(",")) { // 去掉存单金额里面的逗号
						strBalance = strBalance.replaceAll(",", "");
					}
					// 账户余额减去用户输入金额
					String residualAmount = getResidualAmount(strBalance, inputAmount);
					double inputAmountD = Double.parseDouble(inputAmount); // 转换成double类型
					double residualAmountD = Double.parseDouble(residualAmount);// 转换成double类型
					// 如果输入金额和存单剩余金额小于50000 提示.
					if (inputAmountD < 50000 || residualAmountD < 50000) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(errorMsg);
						return;
					}
					// /////////////////////////////////////////////////////////////////////

					if (strBookBalance.contains(",")) {
						strBookBalance = strBookBalance.replaceAll(",", "");
					}
					double sheetMoneyF = Double.parseDouble(strBookBalance);// 把存单金额转换成double类型
					// 通知存款编号不为空时 支取金额大于存单金额 提示!
					if (strNoticeNo != null && !strNoticeNo.equals("")) { // 通知编号不为空时
						if (inputAmountD > sheetMoneyF) {// 当支取金额大于存单金额弹出提示
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									getResources().getString(R.string.dept_check_money));
							return;
						}
					}
				}
			}
			String strConvertType = null;
			if (!StringUtil.isNullOrEmpty(DeptDataCenter.getInstance().getCurNotifyDetail())) {// 通知存款支取日期
				drawDate = (String) DeptDataCenter.getInstance().getCurNotifyDetail().get(Dept.DRAW_DATE);
				strConvertType = (String) DeptDataCenter.getInstance().getCurNotifyDetail()
						.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
				if (StringUtil.isNullOrEmpty(strConvertType)) {
					strConvertType = (String) DeptDataCenter.getInstance().getCurDetailContent()
							.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
				}
			} else {
				Map<String, Object> content = DeptDataCenter.getInstance().getCurDetailContent();
				drawDate = (String) content.get(Dept.INTEREST_ENDDATE);
				strConvertType = (String) DeptDataCenter.getInstance().getCurDetailContent()
						.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
			}
			//
			if (drawMode.equals(DRAW_MODE_N) && strNoticeNo != null) {
				BaseDroidApp.getInstanse().showErrorDialog(
						MyRegSaveChooseTranInAccActivity.this.getResources().getString(R.string.checkout_part_notify_message),
						R.string.cancle, R.string.confirm, onclicklistener);
				return;
			} else if (!StringUtil.isNullOrEmpty(drawDate) && strConvertType != null
					&& ConstantGloble.CONVERTTYPE_N.equals(strConvertType)) {
				boolean noticeFlag = QueryDateUtils.compareDate(nowDate, drawDate);
				if (noticeFlag) {// 未到期提醒
					BaseDroidApp.getInstanse().showErrorDialog(
							MyRegSaveChooseTranInAccActivity.this.getResources().getString(R.string.checkout_notify_message_no_due),
							R.string.cancle, R.string.confirm, onclicklistener1);
					return;
				}
			}

			// 转出转入账户非同省,用户点击确认请求 手续费试算
			if (StringUtil.isNullOrEmpty(strNoticeNo) && strConvertType.equals(ConstantGloble.CONVERTTYPE_N)) {
				BaseDroidApp.getInstanse().showErrorDialog(null, "如您选择未到期支取，将无法按约定利率计息", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						BaseDroidApp.getInstanse().dismissErrorDialog();
						switch (Integer.valueOf(v.getTag().toString())) {
							case CustomDialog.TAG_SURE:
								strMessage = messageEt.getText().toString().trim();
								if (StringUtil.isNullOrEmpty(strMessage)) {
									strMessage = "-";
								}
								BaseHttpEngine.showProgressDialog();
								requestCommConversationId();
								break;
							case CustomDialog.TAG_CANCLE:
								return;
						}
					}
				});
			} else { // 到期
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		if (type.equals(NOTIFY_SAVE)) { // 支取通知存款
			requsetPsnCallDepositToCurrentSavingComChge(fromAccountId, toAccountId, strCurrency, cashRemit, inputAmount,
					strMessage, passBookNumber, cdNumber, cdperiod, convertType, isScheduled, strNoticeNo, availableBalance,
					transMode);
		}
	}

	/** 支取通知存款费用试算-----回调 */
	public void requsetPsnCallDepositToCurrentSavingComChgeCallback(Object resultObj) {
		super.requsetPsnCallDepositToCurrentSavingComChgeCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		// 将数据放入数据中心
		DeptDataCenter.getInstance().setInformMoneyMap(result);

		if (drawMode.equals(DRAW_MODE_N) && strNoticeNo != null) { // 部分支取
			Intent intent = new Intent();
			intent.putExtra("isExpire", isExpire);
			intent.putExtra(Dept.SCHEDULE_NUMBER, strNoticeNo);
			intent.putExtra(Dept.AMOUNT, inputAmount);
			intent.putExtra(Dept.DRAW_DATE, drawDate);
			intent.putExtra(Dept.MEMO, strMessage);
			intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, ONE_DAY_EXCUTE + "");
			intent.putExtra(Dept.DRAW_MODE, drawMode);
			intent.putExtra(NOTIFY_FLAG, notifyFlag);
			intent.putExtra("isFromBoc", DeptDataCenter.getInstance().isFromBoc);
			intent.setClass(context, MyPositDrawMoneConfirmActivity.class);
			startActivity(intent);
		} else { // 全部支取
			strMessage = messageEt.getText().toString().trim();
			if (StringUtil.isNullOrEmpty(strMessage)) {
				strMessage = "-";
			}
			Intent intent = new Intent();
			intent.putExtra(Dept.SCHEDULE_NUMBER, strNoticeNo);
			intent.putExtra(Dept.AMOUNT, inputAmount);
			intent.putExtra(Dept.MEMO, strMessage);
			intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, ONE_DAY_EXCUTE + "");
			intent.putExtra(Dept.DRAW_MODE, drawMode);
			intent.putExtra(NOTIFY_FLAG, notifyFlag);
			intent.putExtra("isFromBoc", DeptDataCenter.getInstance().isFromBoc);
			intent.setClass(context, MyPositDrawMoneConfirmActivity.class);
			startActivity(intent);
		}
	}

	/** 转入账户列表ListView的监听事件 */
	private OnItemClickListener clickAccInListItem = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
			showInDetailView();
			showAccInListItemData();// 显示转出账户条目的详细信息
		}
	};

	/** 显示转入账户条目的详细信息 */
	private void showAccInListItemData() {
		accInInfoMap = DeptDataCenter.getInstance().getAccountContent();
		DeptDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
		if (accInInfoMap == null) {
			return;
		}
		// 存储转入账户信息
		TextView accountTypeTv = (TextView) accInDetailLayout.findViewById(R.id.tv_acc_in_item_type);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accountTypeTv);
		TextView accNameTv = (TextView) accInDetailLayout.findViewById(R.id.tv_acc_in_item_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accNameTv);
		TextView accountNumberTv = (TextView) accInDetailLayout.findViewById(R.id.tv_acc_in_item_no);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accountNumberTv);

		String strAccountType = LocalData.AccountType.get((String) accInInfoMap.get(Comm.ACCOUNT_TYPE));
		accountTypeTv.setText(strAccountType);

		accNameTv.setText((String) accInInfoMap.get(Comm.NICKNAME));
		String accountNumberStr = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		accountNumberTv.setText(StringUtil.getForSixForString(accountNumberStr));
		// 转入账户 省联号
		inAccountIbkNum =  (String) accInInfoMap.get(ConstantGloble.accountIbkNum);
		// 转入账号ID
		toAccountId =  (String) accInInfoMap.get(Comm.ACCOUNT_ID);
		accInDetailLayout.setOnClickListener(accInClicklistener);
	}

	/**
	 * 初始化底部视图  教育储蓄 && 零存整取点击事件
	 * @param view
	 */
	private void initBottomBtnlayout(View view) {
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
				strMessage = messageEt.getText().toString().trim();
				saveMoney = continueSaveTv.getText().toString().trim();

				if (TextUtils.isEmpty(saveMoney)) {// 校验续存金额是否为空
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							MyRegSaveChooseTranInAccActivity.this.getResources().getString(R.string.add_amount_no_empty));
					return;
				}
				// 校验存款金额
				if (!checkAmount(saveMoney, AMOUNT_DEPOSIT)) {
					return;
				}

				if (StringUtil.isNullOrEmpty(strMessage)) {
					strMessage = "-";
				} 
				transMode = ConstantGloble.NOWEXE;
				
				if (type.equals(EDUCATION_SAVE1) || type.equals(EDUCATION_SAVE2)) { // 教育储蓄
					// 立即执行
					BaseHttpEngine.showProgressDialog();
					requsetPsnExtendEducationDepositeComChge(fromAccountId, toAccountId, saveMoney, strMessage, transMode);					
				} else if (type.equals(ZERO_SAVE1) || type.equals(ZERO_SAVE2)) { // 零存整取
					// 立即执行
					BaseHttpEngine.showProgressDialog();
					requsetPsnExtendLingcunDepositeComChge(fromAccountId, toAccountId, saveMoney, strMessage, transMode);
				}
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
				strMessage = messageEt.getText().toString().trim();
				saveMoney = continueSaveTv.getText().toString().trim();
				// 预约日期执行
				// 校验存款金额
				if (!checkAmount(saveMoney, AMOUNT_DEPOSIT)) {
					return;
				}

				if (StringUtil.isNullOrEmpty(strMessage)) {
					strMessage = "-";
				}
				transMode = ConstantGloble.PREDATEEXE;
				
				if (type.equals(EDUCATION_SAVE1) || type.equals(EDUCATION_SAVE2)) { // 教育储蓄
					// 预约日期执行
					BaseHttpEngine.showProgressDialog();
					requsetPsnExtendEducationDepositeComChge(fromAccountId, toAccountId, saveMoney, strMessage, transMode);
				} else if (type.equals(ZERO_SAVE1) || type.equals(ZERO_SAVE2)) { // 零存整取
					// 预约日期执行
					BaseHttpEngine.showProgressDialog();
					requsetPsnExtendLingcunDepositeComChge(fromAccountId, toAccountId, saveMoney, strMessage, transMode);
				}
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
				strMessage = messageEt.getText().toString().trim();
				saveMoney = continueSaveTv.getText().toString().trim();
				// 校验存款金额
				if (!checkAmount(saveMoney, AMOUNT_DEPOSIT)) {
					return;
				}

				if (StringUtil.isNullOrEmpty(strMessage)) {
					strMessage = "-";
				} 
				transMode = ConstantGloble.PREPERIODEXE;
				
				if (type.equals(EDUCATION_SAVE1) || type.equals(EDUCATION_SAVE2)) { // 教育储蓄
					// 预约周期执行
					BaseHttpEngine.showProgressDialog();
					requsetPsnExtendEducationDepositeComChge(fromAccountId, toAccountId, saveMoney, strMessage, transMode);
				} else if (type.equals(ZERO_SAVE1) || type.equals(ZERO_SAVE2)) { // 零存整取
					// 预约周期执行
					BaseHttpEngine.showProgressDialog();
					requsetPsnExtendLingcunDepositeComChge(fromAccountId, toAccountId, saveMoney, strMessage, transMode);
				}
			}
		});
	}

	/** 通讯返回 */
	@Override
	public void communicationCallBack(int flag) {
		super.communicationCallBack(flag);
		switch (flag) {
		case QUERY_ALL_IN_ACCOUNT_CALLBACK:// 查询转入账户返回 刷新界面
			accListView = new ListView(context);
			accListView.setFadingEdgeLength(0);
			accListView.setScrollingCacheEnabled(false);
			AccInListAdapter adapter = new AccInListAdapter(context, accountInList);
			accListView.setAdapter(adapter);
			accListView.setOnItemClickListener(clickAccInListItem);
			// 如果是续存 默认显示的是 转入账户
			if (!StringUtil.isNullOrEmpty(continueSaveFlag) && continueSaveFlag.equals("Y")) {
				showInListView();
			} else {
				showOutListView();
			}
			break;

		case QUERY_ACCOUNT_DETAIL_CALLBACK: // 续存返回数据
			if (accOutInfoMap != null) {
				DeptDataCenter.getInstance().setAccOutInfoMap(accOutInfoMap);
			}
			// 添加一个新的布局文件--转出账户详细信息
			if (!StringUtil.isNullOrEmpty(continueSaveFlag) && continueSaveFlag.equals("Y")) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
				showEduAndZeroOutDetailView();
				showEduAndZeroOutListItemData(accOutDetailLayout);// 显示转出账户条目的详细信息
			} else {// 如果是定期一本通 弹出详情框
				if (notifyFlag == 1) {
					List<Map<String, Object>> listData = DeptDataCenter.getInstance().getAccountList();
					DeptDataCenter.getInstance().setWholesaveAccountList(listData);
				}
				BaseDroidApp.getInstanse().dismissMessageDialog();
				BaseDroidApp.getInstanse().showMyRegSaveWholesaveAccDetailMessageDialog(currentPosition,
						onCreateNoticeListener, onCheckoutListener);
			}

			break;
		case QUERY_ACCOUNT_IN_CALLBACK:// 转出账户是定期一本通的时候 查询转入账户

			if (!StringUtil.isNullOrEmpty(continueSaveFlag) && continueSaveFlag.equals("Y")) {// 续存
				List<Map<String, Object>> mList = DeptDataCenter.getInstance().getTranoutAccountList();
				if (mList != null && !mList.isEmpty()) {
					initOutListView(mList);
					accListView.setOnItemClickListener(clickAccOutListItem);
					BaseDroidApp.getInstanse().showDeptTranoutinDialog(ConstantGloble.TRANS_ACCOUNT_OPER_OUT, accListView,
							backListener, null);
				} else {
					BaseDroidApp.getInstanse()
							.showInfoMessageDialog(getResources().getString(R.string.dept_tranOut_no_date));
				}
			} else {
				List<Map<String, Object>> mList = DeptDataCenter.getInstance().getTranoutAccountList();
				if (mList != null && !mList.isEmpty()) {
					initInListView(DeptDataCenter.getInstance().getTranoutAccountList());
					accListView.setOnItemClickListener(clickWhosesaveListItem);
					BaseDroidApp.getInstanse().showDeptTranoutinDialog(ConstantGloble.TRANS_ACCOUNT_OPER_IN, accListView,
							backListener, null);
				} else {
					BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.dept_tranIn_no_date));
				}
			}

			break;
		case QUERY_ALL_ACCOUNT_CALLBACK:// 返回定期一本通数据
			List<Map<String, Object>> listData = DeptDataCenter.getInstance().getAccountList();
			DeptDataCenter.getInstance().setWholesaveAccountList(listData);
			initOutListView(listData);
			accListView.setOnItemClickListener(clickWholesaveAccOutListItem);
			BaseDroidApp.getInstanse().showDeptTranoutinDialog(ConstantGloble.TRANS_ACCOUNT_OPER_IN, accListView,
					backListener, null);
			break;
		case QUERY_NOTIFY_ACCOUNT_DETAIL_CALLBACK:
			List<Map<String, Object>> noticeDetailList = DeptDataCenter.getInstance().getQueryNotifyCallBackList();
			// 通知编号list
			List<String> noticeIdList = new ArrayList<String>();
			for (int i = 0; i < noticeDetailList.size(); i++) {
				String noticeNo = (String) noticeDetailList.get(i).get(Dept.NOTIFY_ID);
				noticeIdList.add(noticeNo);
			}
			DeptDataCenter.getInstance().setNoticeIdList(noticeIdList);
			// 非约定转存 首先判断是否有无通知编号 如果没有通知编号 违约支取
			String strConvertType = (String) DeptDataCenter.getInstance().getCurDetailContent()
					.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
			if (strConvertType.equals(ConstantGloble.CONVERTTYPE_N)) {// 如果是非约定
				// 判断有无通知
				if (StringUtil.isNullOrEmpty(noticeIdList)) {// 没有通知编号
					BaseDroidApp.getInstanse().showErrorDialog(this.getResources().getString(R.string.no_notify),
							R.string.cancle, R.string.confirm, onclicklistener);
					break;
				} else {// 有通知编号 判断支取日期
					BaseDroidApp.getInstanse().dismissMessageDialog();
					showOutDetailView();
					showAccWholeSaveOutListItemData();// 显示转出账户条目详细信息
					initBottomLayout();
				}
			} else {// 约定转存
				BaseDroidApp.getInstanse().dismissMessageDialog();
				showOutDetailView();
				showAccWholeSaveOutListItemData();// 显示转出账户条目详细信息
				initBottomLayout();
			}
			break;
		default:
			break;
		}
	}

	/** 显示转出账户条目的详细信息 */
	private void showAccWholeSaveOutListItemData() {
		accOutInfoMap = DeptDataCenter.getInstance().getAccountContentMap();
		if (accOutInfoMap == null){ // 如果用户选择的账户为空 显示第三方支取跳转账户信息
			accOutInfoMap = DeptDataCenter.getInstance().getAccountContent();
		}
		DeptDataCenter.getInstance().setAccOutInfoMap(accOutInfoMap); // 存储转出账户信息
		// 转出账户ID
		fromAccountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		// 转出账户的省联号
		outAccountIbkNum = (String) accOutInfoMap.get(ConstantGloble.accountIbkNum);
		TextView accountTypeTv = (TextView) accOutDetailLayout.findViewById(R.id.tv_accountType_accOut_list_detail);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accountTypeTv);
		TextView nickNameTv = (TextView) accOutDetailLayout.findViewById(R.id.tv_nickName_accOut_list_detail);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, nickNameTv);
		TextView accountNumberTv = (TextView) accOutDetailLayout.findViewById(R.id.tv_accountNumber_accOut_list_detail);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accountNumberTv);
		TextView volumeNumberTv = (TextView) accOutDetailLayout.findViewById(R.id.dept_volume_number_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, volumeNumberTv);
		TextView cdNumberTv = (TextView) accOutDetailLayout.findViewById(R.id.dept_cdnumber_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, cdNumberTv);
		String strAccountType = LocalData.AccountType.get(accOutInfoMap.get(Comm.ACCOUNT_TYPE));
		accountTypeTv.setText(strAccountType);
		nickNameTv.setText((String) accOutInfoMap.get(Comm.NICKNAME));
		String accoutNumber = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		accountNumberTv.setText(StringUtil.getForSixForString(accoutNumber));
		String strVolumeNumber = (String) accOutInfoMap.get(Dept.VOLUME_NUMBER);
		volumeNumberTv.setText(strVolumeNumber); // 存折号
		String strCdNumber = (String) accOutInfoMap.get(Dept.CD_NUMBER);
		cdNumberTv.setText(strCdNumber); // 存单号
		// 可用余额
		availableBalance = (String) accOutInfoMap.get(Dept.AVAILABLE_BALANCE);
		if (StringUtil.isNullOrEmpty(availableBalance)) {
			availableBalance = "0.0";
		}
	}

	/** 建立通知按钮 监听 */
	private OnClickListener onCreateNoticeListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();// 关闭详情框
			Intent intent = new Intent();
			intent.setClass(context, CreateNoticeActivity.class);
			startActivity(intent);
		}
	};

	/** 支取按钮 监听 */
	private OnClickListener onCheckoutListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 发送通讯查询通知详情
			requestSystemDateTime();
			BiiHttpEngine.showProgressDialog();
		}
	};

	/** 查询系统时间返回 */
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> content = DeptDataCenter.getInstance().getCurDetailContent();
		String endDate = (String) content.get(Dept.INTEREST_ENDDATE);

		if (content.get(Dept.TYPE).equals(DeptBaseActivity.NOTIFY_SAVE)) {
			// 如果是通知存款 发送通讯 查询通知
			String volumeNumber = (String) content.get(Dept.VOLUME_NUMBER);
			String cdNumber = (String) content.get(Dept.CD_NUMBER);
			requestQueryNotify(accountId, volumeNumber, cdNumber);
		} else {
			if (!StringUtil.isNullOrEmpty(endDate)) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
				// 如果是整存整取 刷新accountLayout
				showOutDetailView();
				showAccWholeSaveOutListItemData();// 显示转出账户条目详细信息

				initBottomLayout();
			} else {// 没有违约
				BaseDroidApp.getInstanse().dismissMessageDialog();
				// 如果是整存整取 刷新accountLayout
				showOutDetailView();
				showAccWholeSaveOutListItemData();// 显示转出账户条目详细信息

				initBottomLayout();
			}
		}
	}

	/** 整存整取未到期提醒的确定按钮 监听 */
	private OnClickListener wholeSaveOnclickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.putExtra(Dept.MEMO, strMessage);
			intent.putExtra(Dept.AMOUNT, money);
			intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, ConstantGloble.NOWEXE);
			intent.putExtra("isFromBoc", DeptDataCenter.getInstance().isFromBoc);
			intent.setClass(context, MyRegSaveCheckOutConfirmActivity.class);
			startActivity(intent);
		}
	};

	/** 未到期提醒框 按钮监听 */
	private OnClickListener onclicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_SURE:
				if (drawMode.equals(DRAW_MODE_N) && strNoticeNo != null && !strNoticeNo.equals("")) {
					// Intent intent = new Intent();
					// intent.putExtra("isExpire", isExpire);
					// intent.putExtra(Dept.SCHEDULE_NUMBER, strNoticeNo);
					// intent.putExtra(Dept.AMOUNT, money);
					// intent.putExtra(Dept.DRAW_DATE, drawDate);
					// intent.putExtra(Dept.MEMO, strMessage);
					// intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE,
					// ONE_DAY_EXCUTE + "");
					// intent.putExtra(Dept.DRAW_MODE, drawMode);
					// intent.putExtra(NOTIFY_FLAG, notifyFlag);
					// intent.setClass(context,
					// MyRegSaveCheckOutConfirmActivity.class);
					// startActivity(intent);
					boolean noticeFlag = QueryDateUtils.compareDate(nowDate, drawDate);
					if (noticeFlag) {// 未到期提醒
						BaseDroidApp.getInstanse().showErrorDialog(
								MyRegSaveChooseTranInAccActivity.this.getResources().getString(
										R.string.checkout_notify_message_no_due), R.string.cancle, R.string.confirm,
								onclicklistener1);
						return;
					}
				} else {
					strMessage = messageEt.getText().toString().trim();
					if (StringUtil.isNullOrEmpty(strMessage)) {
						strMessage = "-";
					} else {
						// RegexpBean memoBean = new RegexpBean("附言",
						// strMessage,
						// "memo");
						// ArrayList<RegexpBean> regexList = new
						// ArrayList<RegexpBean>();
						// regexList.add(memoBean);
						// if(!RegexpUtils.regexpDate(regexList))
						// return;
					}
					Intent intent = new Intent();
					intent.putExtra(Dept.SCHEDULE_NUMBER, strNoticeNo);
					intent.putExtra(Dept.AMOUNT, money);
					intent.putExtra(Dept.MEMO, strMessage);
					intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, ONE_DAY_EXCUTE + "");
					intent.putExtra(Dept.DRAW_MODE, drawMode);
					intent.putExtra(NOTIFY_FLAG, notifyFlag);
					intent.putExtra("isFromBoc", DeptDataCenter.getInstance().isFromBoc);
					intent.setClass(context, MyRegSaveCheckOutConfirmActivity.class);
					startActivity(intent);
				}
				break;
			case CustomDialog.TAG_CANCLE:
				BaseDroidApp.getInstanse().dismissErrorDialog();
				BaseDroidApp.getInstanse().dismissMessageDialog();
				break;
			default:
				break;
			}
		}
	};

	/** 未到期提醒框 按钮监听 */
	private OnClickListener onclicklistener1 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_SURE:
				if (drawMode.equals(DRAW_MODE_N) && strNoticeNo != null) {
					Intent intent = new Intent();
					intent.putExtra("isExpire", isExpire);
					intent.putExtra(Dept.SCHEDULE_NUMBER, strNoticeNo);
					intent.putExtra(Dept.AMOUNT, money);
					intent.putExtra(Dept.DRAW_DATE, drawDate);
					intent.putExtra(Dept.MEMO, strMessage);
					intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, ONE_DAY_EXCUTE + "");
					intent.putExtra(Dept.DRAW_MODE, drawMode);
					intent.putExtra(NOTIFY_FLAG, notifyFlag);
					intent.putExtra("isFromBoc", DeptDataCenter.getInstance().isFromBoc);
					intent.setClass(context, MyRegSaveCheckOutConfirmActivity.class);
					startActivity(intent);
				} else {
					strMessage = messageEt.getText().toString().trim();
					if (StringUtil.isNullOrEmpty(strMessage)) {
						strMessage = "-";
					} else {
						// RegexpBean memoBean = new RegexpBean("附言",
						// strMessage,
						// "memo");
						// ArrayList<RegexpBean> regexList = new
						// ArrayList<RegexpBean>();
						// regexList.add(memoBean);
						// if(!RegexpUtils.regexpDate(regexList))
						// return;
					}
					Intent intent = new Intent();
					intent.putExtra(Dept.SCHEDULE_NUMBER, strNoticeNo);
					intent.putExtra(Dept.AMOUNT, money);
					intent.putExtra(Dept.MEMO, strMessage);
					intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, ONE_DAY_EXCUTE + "");
					intent.putExtra(Dept.DRAW_MODE, drawMode);
					intent.putExtra(NOTIFY_FLAG, notifyFlag);
					intent.putExtra("isFromBoc", DeptDataCenter.getInstance().isFromBoc);
					intent.setClass(context, MyRegSaveCheckOutConfirmActivity.class);
					startActivity(intent);
				}

				break;
			case CustomDialog.TAG_CANCLE:
				BaseDroidApp.getInstanse().dismissErrorDialog();
				BaseDroidApp.getInstanse().dismissMessageDialog();
				break;

			default:
				break;
			}

		}
	};

	/**
	 * 初始化转出账户列表
	 * 
	 * @param accountList
	 */
	private void initOutListView(List<Map<String, Object>> accountList) {
		accListView = new ListView(context);
		accListView.setFadingEdgeLength(0);
		accListView.setScrollingCacheEnabled(false);
		accListView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		AccOutListAdapter outAdapter = new AccOutListAdapter(context, accountList);
		accListView.setAdapter(outAdapter);

		View outView = outAdapter.getView(0, null, accListView);
		outView.measure(0, 0);
		itemHeight = outView.getMeasuredHeight();
	}

	/**
	 * 初始化转入账户
	 * 
	 * @param accountList
	 */
	private void initInListView(List<Map<String, Object>> accountList) {
		accListView = new ListView(context);
		accListView.setFadingEdgeLength(0);
		accListView.setScrollingCacheEnabled(false);
		AccInListAdapter inListadapter = new AccInListAdapter(context, accountList);
		accListView.setAdapter(inListadapter);

		View inView = inListadapter.getView(0, null, accListView);
		inView.measure(0, 0);
		itemHeight = inView.getMeasuredHeight();
	}

	/**
	 * 显示转出账户列表
	 */
	private void showOutListView() {
		mAccOutLl.setVisibility(View.GONE);
		mAccOutListLayout.setVisibility(View.VISIBLE);
		mAccOutListLayout.removeAllViews();
		mAccOutListLayout.addView(accListView);
	}

	/**
	 * 
	 */
	private void showInListView() {
		mAccInLl.setVisibility(View.GONE);
		mAccInListLayout.setVisibility(View.VISIBLE);
		mAccInListLayout.removeAllViews();
		mAccInListLayout.addView(accListView);
	}

	/** 显示转出账户详情view */
	private void showOutDetailView() {
		accOutDetailLayout = (LinearLayout) inflater.inflate(R.layout.dept_mysave_acc_out_list_detail, null);
		mAccOutLl.setVisibility(View.VISIBLE);
		mAccOutListLayout.setVisibility(View.GONE);
		mAccOutLl.removeAllViews();
		mAccOutLl.addView(accOutDetailLayout);
	}

	/** 零存整取或者教育储蓄的时候 转出账户详情 */
	private void showEduAndZeroOutDetailView() {
		accOutDetailLayout = (LinearLayout) inflater.inflate(R.layout.dept_acc_out_list_detail, null);
		mAccOutLl.setVisibility(View.VISIBLE);
		mAccOutListLayout.setVisibility(View.GONE);
		mAccOutLl.removeAllViews();
		mAccOutLl.addView(accOutDetailLayout);
	}

	/** 显示转入账户详情view */
	private void showInDetailView() {
		accInDetailLayout = (LinearLayout) inflater.inflate(R.layout.dept_acc_in_list_detail, null);
		mAccInLl.setVisibility(View.VISIBLE);
		mAccInListLayout.setVisibility(View.GONE);
		mAccInLl.removeAllViews();
		mAccInLl.addView(accInDetailLayout);
	}

	/**
	 * 设置listLayout 高度
	 * 
	 * @param layout
	 */
	private void setLayoutHeight(LinearLayout layout, List<Map<String, Object>> tranoutAccountList) {
		// 根据返回数据 初始化mAccOutListLayout的高度
		if (tranoutAccountList == null || tranoutAccountList.size() < 1) {
			return;
		}
		int padding = getResources().getDimensionPixelSize(R.dimen.fill_margin_left) * 2;
		if (tranoutAccountList.size() > 0 && tranoutAccountList.size() <= 6) {
			LayoutParams lp = new LayoutParams(LayoutValue.SCREEN_WIDTH - padding, tranoutAccountList.size() * itemHeight);
			layout.setLayoutParams(lp);
		} else if (tranoutAccountList.size() > 6) {
			LayoutParams lp = new LayoutParams(LayoutValue.SCREEN_WIDTH - padding, itemHeight * 6);
			layout.setLayoutParams(lp);
		}
	}

	/**
	 * 校验存款金额
	 * 
	 * @param amount
	 * @param excType
	 *            校验类型 1-支取金额 2-存储金额 金额
	 * @return 合格 true，不合格 false
	 */
	private boolean checkAmount(String amount, int excType) {
		// String msgLabel = null;
		// String errorMsg = null;
		// if (AMOUNT_OBTAIN == excType) {
		// msgLabel = "支取金额";
		// errorMsg = "支取金额不能超过可用余额";
		// } else {
		// msgLabel =
		// this.getResources().getString(R.string.save_money_message);
		// if (!StringUtil.isNullOrEmpty(continueSaveFlag) &&
		// continueSaveFlag.equals("Y")) {
		// errorMsg = "续存金额不能超过可用余额";
		// } else {
		// errorMsg = "存款金额不能超过可用余额";
		// }
		// }
		// RegexpBean regAmount = new
		// RegexpBean(getResources().getString(R.string.reg_transferAmount),
		// amount,
		// "tranAmount");
		// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// lists.add(regAmount);
		// if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
		// return false;
		// }

		String msgLabel = "";
		if (AMOUNT_OBTAIN == excType) {
			msgLabel = "支取金额";
		} else {
			msgLabel = getResources().getString(R.string.save_money_message);
		}
		if (strCurrency.equals(ConstantGloble.PRMS_CODE_YEN1) || strCurrency.equals(ConstantGloble.PRMS_CODE_YEN2)
				|| strCurrency.equals(ConstantGloble.PRMS_CODE_HANYUAN1)
				|| strCurrency.equals(ConstantGloble.PRMS_CODE_HANYUAN2)) {
			RegexpBean regAmount = new RegexpBean(msgLabel, amount, "tranSpAmount");
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(regAmount);
			if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
				return false;
			}
		} else {
			RegexpBean regAmount = new RegexpBean(msgLabel, amount, "tranAmount");
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(regAmount);
			if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
				return false;
			}
		}
		// if (Double.parseDouble(amount) >
		// Double.parseDouble(availableBalance)) {
		// BaseDroidApp.getInstanse().showInfoMessageDialog(errorMsg);
		// return false;
		// }
		/*
		 * else if (Double.parseDouble(amount) < 50) {
		 * BaseDroidApp.getInstanse().showInfoMessageDialog(
		 * this.getResources().getString(R.string.least_save_money)); return
		 * false; }
		 */

		// else {
		return true;
		// }

	}

	private OnClickListener backListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
		}
	};

	// ///////////////////////////////手续费试算
	/** 手续费试算监听 */
	private OnClickListener OnClickListenerButton = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_SURE:// 确定
				// 显示转入账户详情
				showInDetailView();
				showAccInListItemData();
				BaseDroidApp.getInstanse().closeAllDialog();
				BaseDroidApp.getInstanse().dismissMessageDialog();
				break;
			case CustomDialog.TAG_CANCLE:// 取消
				BaseDroidApp.getInstanse().dismissErrorDialog();
				break;
			default:
				break;
			}
		}
	};
	
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
	
	/** 教育储蓄 & 零存整取跳转 */
	private void intentSaceCheckOutConfirm() {
		// 立即执行
		Intent intent = new Intent();
		intent.putExtra(Dept.MEMO, strMessage);
		intent.putExtra(Dept.AMOUNT, saveMoney);
		intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, transMode);
		intent.putExtra("isFromBoc", DeptDataCenter.getInstance().isFromBoc);
		intent.setClass(context, MyRegSaveCheckOutConfirmActivity.class);
		startActivity(intent);
	}

	/**
	 * add by lqp 2015年11月12日11:37:24 得到剩余存款
	 *
	 * @param blance
	 *            存单金额
	 * @param bb
	 *            输入金额
	 * @return 返回剩余金额
	 */
	private String getResidualAmount(String blance, String bb) {
		int b1 = Integer.parseInt(blance);
		int b2 = Integer.parseInt(bb);
		return String.valueOf(b1 - b2);
	}
}