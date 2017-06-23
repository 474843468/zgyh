package com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.dept.adapter.AccOutListAdapter;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mobiletrans.MobileTranActivity;
import com.chinamworld.bocmbci.biz.tran.mytransfer.adapter.AccInListAdapter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee.ChooseBankActivity;
import com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee.QueryExternalBankActivity1;
import com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee.WritePayeeInfoActivity1;
import com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee.order.OrderMainActivity;
import com.chinamworld.bocmbci.biz.tran.mytransfer.creditcardrepay.RelCreditCardRemitConfirmActivity1;
import com.chinamworld.bocmbci.biz.tran.mytransfer.creditcardrepay.RelSelfCreditCardConfirmActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.IntentSpan;
import com.google.zxing.CaptureActivity;


public class TransferManagerActivity1 extends TranBaseActivity implements
		OnCheckedChangeListener {

	private Context context = this;
	private LayoutInflater inflater = null;
	private View container;
	/** 转出账户控件 */
	private RelativeLayout mAccOutLl = null;
	/** 转入账户控件 */
	private RelativeLayout mAccInLl = null;
	/** 转出账户控件高度 */
	private int mAccOutLlHeight = 150;
	/** 转入账户控件高度 */
	private int mAccInLlHeight = 150;
	/** 转出账户列表Layout */
	private LinearLayout mAccOutListLayout = null;
	/** 转入账户列表Layout */
	// private LinearLayout mAccInListLayout = null;
	/** 新增关联账户 */
	private Button newAddTranOutBtn;
	/** 新增转入账户 */
	private Button newAddTranInBtn;
	/** 底部布局 */
	private LinearLayout bottomLayout = null;
	/** 转出账户详情 布局 */
	private LinearLayout accOutDetailLayout = null;
	// /** 转出账户详情 布局 */
	private LinearLayout accInDetailLayout = null;
	/** 下标题 */
	// private TextView titleTv;
	/** 转出账户listview */
	private ListView accOutListView = null;
	/** 转入账户listview */
	private ListView accInListView = null;

	private boolean isTranOutFirst = true;
	private boolean isTranInFirst = true;

	TextView currencyCodeTv;
	TextView cashTv;

	// /** 当前选择转出账户 */
	// private Map<String, Object> accOutInfoMap;
	// /** 当前选择转入账户 局部变量*/
	// private Map<String, Object> accInInfoMap;

	/** 转出账户可以用余额 */
	private String availableBalance;

	private AccInListAdapter inadapter;
	/** 收款人列表数据 */
	private List<Map<String, Object>> accInListData = new ArrayList<Map<String, Object>>();

	private String outOrInFlag;
	private static final String OUT = "1";
	private static final String IN = "2";

	/** 信用卡第一币种 */
	private String crcdCurrency1;

	/** 信用卡第二币种 */
	private String crcdCurrency2;

	/** 转入账户信用卡第一币种 */
	private String inCrcdCurrency1;
	/** 转入账户信用卡第二币种 */
	private String inCrcdCurrency2;
	/** 转出账户 信用卡 第一币种 */
	private String outCrcdCurrency1;
	/** 转出账户 信用卡 第二币种 */
	private String outCrcdCurrency2;

	/** 币种和对应钞汇标志数据 */
	private ArrayList<Map<String, Object>> currencyAndCashRemitList;
	/** 关联账户转账对应的币种和钞汇标志数据 */
	private List<String> currencyList;
	private List<String> cashRemitList;
	private String ghcurrency;// 购汇币种
	/** 币种代码,发送到服务器 */
	private String currencyCode;
	/** 钞汇标志代码,发送到服务器 */
	private String cashRemitCode;
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
	/** 转入账户id */
	private String toPayeeId;
	/** 还款金额设定方式 */
	private String repayAmountSet;
	/** 绑定id */
	private String combineId;
	/** 是否发送手机短信通知收款人标示 */
	private String isSendSmc = ConstantGloble.FALSE;
	/** 是否保存为常用收款人 */
	private String isSavePayee = ConstantGloble.FALSE;

	/** 其他模块跳入标识 */
	private int otherPartJumpFlag = 0;

	/** 新增收款人标识 */
	private boolean addNewPayeeFlag = false;
	private String isAddPayeeChecked = ConstantGloble.FALSE;
	/** 当前选择的币种和对应钞汇标志数据 */
	private ArrayList<Map<String, Object>> currentCurrencyCashRemitList;
	/** 过滤后的转出账户列表 */
	private List<Map<String, Object>> accOutListCopy;
	/** 单外币ID */
	private String crcdId;
	private Map<String, Object> addInforMap;
	private Map<String, Object> addOutforMap;
	private Map<String, Object> currInDetail;

	// T43快捷方式按钮
	private LinearLayout tran_acc_seach_linear;
	/** 跨行转账——转账方式 */
	private RadioGroup radio_tran_type;

	// boolean common_can_shishi= true;
	// 实时到账
	private RadioButton rb_shishi;

	boolean shishi_tag = false;//
	// 普通转账
	private RadioButton rb_common;
	boolean common_tag = false;

	private String comm2shishi;
	/** 跨行转账——开户行查询 */
	/** 查询开户行名称 */
	private Button kBankBtn = null;
	/** 查询账户所属银行名称 */
	private Button isBankBtn = null;
	/** 开户行银行名称 */
	private TextView inBankNameEt_txt = null;
	private TextView inBankNameEt = null;
	/** 账户所属银行名称 */
	private TextView isBankNameEt_txt = null;
	private TextView isBankNameEt = null;
	private View containerV;
	private View containerV1;
	/** 是否请求收款人列表 */
	private boolean isrequestAccIn = false;
	/** 信用卡 全球交易人名币记账功能是否开通 */
	private boolean isOpenFlag = false;
	// 新增收款人按钮
	private Button add_receipt_person;
	// //判断 转入账户在页面创建时 是否已经
	// private boolean isFirst = true;
	//
	//
	/** 转出 转出 账户是借记卡 对应的币种 和 余额 */
	List<Map<String, String>> outCurrencyAndBalance;

	/** 转出账户 信用卡 对应的币种 和 余额 */
	List<Map<String, String>> outCreditCurrencyAndBalance;
	/** 转入账户 信用卡 对应的币种 和 余额 */
	List<Map<String, String>> inCreditCurrencyAndBalance;
	/** 转出和转入账户币种交集 集合 */
	// List<String> intersection;
	private boolean hasMeasuredOut = false;
	private boolean hasMeasuredIn = false;
	/** 跨行实时转账提示语 */
	private TextView radio_shishi_prompt;

	private RadioButton radio_shishi_orientation;
	private RadioButton radio_common_orientation;

	/** 点击转入账户 查询非信用卡详情 账户 报错是否拦截 */
	private boolean inQueryDetailIntercept = false;
	// 选择转出账户 是否需要刷新转入账户
	// private boolean refreshIn = false;
	// private boolean refreshInFrom = false;//是否由于点击转入账户 引起的刷新转出账户

	// p503 信用卡还款统一
	// 信用卡还款

	private RadioGroup radioGroupForpaytype, radioGroupForpaymode,
			radioGroupForgouhuitype;// 还款类型，还款方式,购汇方式

	private LinearLayout ll_paytype, ll_paymode, ll_huankuan, ll_gouhui,
			ll_cashRemit, ll_input;
	private LinearLayout ll_next, ll_more_next;
	private TextView tv_huankuan_count;
	private TextView crcd_trans_set_title;
	private RadioButton radio_huankuan, radio_gouhui, radio_payall,
			radio_paylimit, radio_payself, radio_gouhuiall, radio_gouhuipart; // 还款，购汇，全额，最小，自定义，全额，部分
	private Spinner crcd_currency, crcd_cashRemit;// 币种 钞汇
	private EditText et_huankuan_count, et_huankuan_fuyan, et_gouhui_count;// 还款金额，附言，购汇金额
	private TextView crcd_card_currency;// 购汇币种
	private TextView credit_new_info;
	// 转入账户信息
	private LinearLayout crcd_in_layout_one, crcd_in_layout_two;//
	private TextView crcd_in_nickName, crcd_in_accountType,
			crcd_in_accountType_value, crcd_in_currey1, crcd_in_currey1_value,
			crcd_in_currey2, crcd_in_currey2_value;

	private List<String> incurrencyList;// 转入账户币种
	private List<String> incurrencyflagList;// 转入账户币种
	private List<String> incashRemitList;// 账户钞汇
	private List<String> incashRemitflagList;// 账户钞汇
	private List<Map<String, String>> crcdcashRemitAndBalance;// 转出账户 钞汇 余额
	private ArrayAdapter<String> incurrencyAdapter = null;
	private ArrayAdapter<String> incashRemitAdapter = null;

	private String billAmout_cn; // 本期账单金额
	private String haveNotRepayAmout_cn; // 本期未还款金额
	private String billLimitAmout_cn; // 本期最低还款金额

	private String billAmout; // 本期账单金额
	private String haveNotRepayAmout; // 本期未还款金额
	private String billLimitAmout; // 本期最低还款金额
	private String gouhuiBalance;// 购汇金额
	private String accTypeout;
	private String falg_cn;// 人民币欠款标识
	private String falg_wb;// 外币欠款标识
	int position;
	private boolean cansamecard = false;
	private boolean newflag = false;

	
	private TextView tran_acc_in_person;
	private TextView tran_please_choose_in;
//	private LinearLayout dept_out_info_layout;
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
		initView();
		// 处理其他模块跳转进来情况
		dealWithOtherPart();
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("tranManager_1");
//
//	}

	private void initView() {
//		dept_out_info_layout=(LinearLayout)container.findViewById(R.id.dept_out_info_layout);
		tran_acc_in_person=(TextView)container.findViewById(R.id.tran_acc_in_person);
		tran_please_choose_in=(TextView)container.findViewById(R.id.tran_please_choose_in);
		tran_acc_in_person.setText(getResources().getString(R.string.tran_acc_in_person_tran));
		tran_please_choose_in.setText(getResources().getString(R.string.tran_please_choose_in_tran));
		// 转出账户布局
		mAccOutLl = (RelativeLayout) container
				.findViewById(R.id.ll_acc_out_mytransfer);
		mAccOutListLayout = (LinearLayout) container
				.findViewById(R.id.ll_acc_out_list_layout);
		mAccOutLl.setOnClickListener(accOutClicklistener);
		newAddTranOutBtn = (Button) container
				.findViewById(R.id.dept_add_new_tranout_btn);
		newAddTranOutBtn.setOnClickListener(addNewAcc);
		// 转入账户布局accin_layout
		mAccInLl = (RelativeLayout) container
				.findViewById(R.id.ll_acc_in_mytransfer);
		mAccInLl.setOnClickListener(accInClicklistener);
		newAddTranInBtn = (Button) container
				.findViewById(R.id.dept_add_new_tranin_btn);
		newAddTranInBtn.setOnClickListener(addAccInNoClicklistener);

		bottomLayout = (LinearLayout) container
				.findViewById(R.id.dept_save_regular_bottom);
		tran_acc_seach_linear = (LinearLayout) container
				.findViewById(R.id.tran_acc_seach_linear);
		tran_acc_seach_linear.setVisibility(View.GONE);
		add_receipt_person = (Button) findViewById(R.id.add_receipt_person);
		add_receipt_person.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (isTranOutFirst) {
					CustomDialog.toastInCenter(context,
							TransferManagerActivity1.this
									.getString(R.string.choose_outno_message));
//					dept_out_info_layout.setVisibility(View.VISIBLE);
					return;
				}
				
				
				// TODO 如果转出账户为长城信用卡，只能新增实时和行内账户
				Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
						.getAccOutInfoMap();
				// 转出账户类型 为单外币 不支持新增收款人
				String outAccountType = (String) accOutInfoMap
						.get(Comm.ACCOUNT_TYPE);
				if (ConstantGloble.SINGLEWAIBI.equals(outAccountType)) {
					String errorInfo = getResources().getString(
							R.string.out_singlewaibi_error);
					BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);

				} else {
					goAddAcc();
				}
			}
		});
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTopRightBtn.setVisibility(View.GONE);
				if (isTranOutFirst == true) {
					return;
				}
				container = mInflater.inflate(
						R.layout.tran_mytransfer_activity1, null);
				inflater = LayoutInflater.from(TransferManagerActivity1.this);
				tabcontent.removeAllViews();
				tabcontent.addView(container);
				initView();
				resetFlag();
			}
		});

		ViewTreeObserver vto = mAccOutLl.getViewTreeObserver();
		ViewTreeObserver vto1 = mAccInLl.getViewTreeObserver();

		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				if (hasMeasuredOut == false) {

					mAccOutLlHeight = mAccOutLl.getMeasuredHeight();
					hasMeasuredOut = true;
				}
				return true;
			}
		});

		vto1.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				if (hasMeasuredIn == false) {

					mAccInLlHeight = mAccInLl.getMeasuredHeight();
					hasMeasuredIn = true;
				}
				return true;
			}
		});
	}

	/**
	 * 重置标识
	 */
	private void resetFlag() {
		inQueryDetailIntercept = false;
		common_tag = false;
		shishi_tag = false;
		isTranOutFirst = true;
		isTranInFirst = true;
		isSendSmc = ConstantGloble.FALSE;
		isSavePayee = ConstantGloble.FALSE;
		addNewPayeeFlag = false;
		isAddPayeeChecked = ConstantGloble.FALSE;
		isrequestAccIn = false;
		TranDataCenter.getInstance().setAccOutInfoMap(null);
		TranDataCenter.getInstance().setAccInInfoMap(null);
//		dept_out_info_layout.setVisibility(View.GONE);
		mAccOutLl.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_card_new));
		tran_acc_in_person.setText(getResources().getString(R.string.tran_acc_in_person_tran));
		tran_please_choose_in.setText(getResources().getString(R.string.tran_please_choose_in_tran));
		
	}

	/**
	 * 处理其他模块跳转进入转账模块情况
	 */
	private void dealWithOtherPart() {
		Intent intent = getIntent();
		Map<String, Object> accInInfoMap = new HashMap<String, Object>();
		String accoutNumber = null;
		if (intent != null) {
			otherPartJumpFlag = intent.getIntExtra(
					ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
			if (StringUtil.isNullOrEmpty(otherPartJumpFlag)) {
				return;
			}
			switch (otherPartJumpFlag) {
			// case ConstantGloble.REL_CRCD_REPAY:// 信用卡还款
			// TranDataCenter.getInstance().setAccOutInfoMap(null);
			// mTopRightBtn.setVisibility(View.GONE);
			// isTranOutFirst = true;
			// mAccInLl.setEnabled(false);
			// tranTypeFlag = TRANTYPE_REL_CRCD_REPAY;
			// // View crcdRepayView = (ViewGroup) mInflater
			// // .inflate(
			// //
			// R.layout.tran_relation_self_credit_card_trans_seting_mytransfer,
			// // null);
			// //
			// // bottomLayout.removeAllViews();
			// // bottomLayout.addView(crcdRepayView);
			// // initRelCrcdRepayView(crcdRepayView);
			// inCreditCurrencyAndBalance = new ArrayList<Map<String,
			// String>>();
			// mTopRightBtn.setVisibility(View.GONE);
			// mAccInLl.setEnabled(false);
			// addInforMap = TranDataCenter.getInstance().getAccInInfoMap();
			//
			// String accountId2 = (String) addInforMap.get(Comm.ACCOUNT_ID);
			// inCrcdCurrency1 = (String) addInforMap.get("currencyCode");
			// inCrcdCurrency2 = (String) addInforMap.get("currencyCode2");
			// // 查询信用卡详情
			// if (!StringUtil.isNull(inCrcdCurrency1)) {
			// requestForCrcdDetailFromRepay(accountId2, inCrcdCurrency1);
			// }
			//
			// initRelCrcdTran33();
			// break;
			// case ConstantGloble.REL_CRCD_BUY:// 信用卡购汇
			// // sunh 信用卡购汇 优化
			// TranDataCenter.getInstance().setAccOutInfoMap(null);
			// mTopRightBtn.setVisibility(View.GONE);
			// crcdCurrency2 = intent
			// .getStringExtra(ConstantGloble.CRCD_CURRENCY2);
			// toPayeeId = intent.getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
			//
			// mAccInLl.setEnabled(false);
			// isTranOutFirst = true;
			// inCreditCurrencyAndBalance = new ArrayList<Map<String,
			// String>>();
			// tranTypeFlag = TRANTYPE_REL_CRCD_BUY;
			// requestCommConversationId();
			//
			// // mTopRightBtn.setVisibility(View.GONE);
			// // crcdCurrency2 = intent
			// // .getStringExtra(ConstantGloble.CRCD_CURRENCY2);
			// // isTranInFirst = false;
			// // showAccInDetailView();
			// // mAccInLl.setEnabled(false);
			// // tranTypeFlag = TRANTYPE_REL_CRCD_BUY;
			// // View crcdBuyView = (ViewGroup) mInflater
			// // .inflate(
			// //
			// R.layout.tran_relation_credit_card_remit_trans_seting_mytransfer,
			// // null);
			// // initRelCrcdBuyView(crcdBuyView);
			// // bottomLayout.removeAllViews();
			// // bottomLayout.addView(crcdBuyView);
			// break;
			case ConstantGloble.ACC_TO_TRAN:
				// 我要转账——作为转出账户——长城电子借记卡
				TranDataCenter.getInstance().setAccInInfoMap(null);
				mTopRightBtn.setVisibility(View.GONE);
				isTranOutFirst = false;
				mAccOutLl.setEnabled(false);
				addOutforMap = TranDataCenter.getInstance().getAccOutInfoMap();
				outCreditCurrencyAndBalance = new ArrayList<Map<String, String>>();
				// 随便请求一个接口，防止账户管理跳转过来的时候转出账户视图不显示
				BiiRequestBody biiRequestBody = new BiiRequestBody();
				biiRequestBody.setMethod(Comm.QRY_SYSTEM_TIME);
				HttpManager.requestBii(biiRequestBody, this,
						"requestSysDateTimeCallBack");

				// for(int i = 0 ; i < detailList.size(); i++){
				// Map<String, String> map = new HashMap<String, String>();
				// map.put("currentBalanceflag",
				// detailList.get(i).get("currentBalanceflag"));
				// map.put("loanBalanceLimit",
				// detailList.get(i).get("loanBalanceLimit"));
				// map.put("currency", detailList.get(i).get("currency"));
				// outCreditCurrencyAndBalance.add(map);
				// }
				break;
			case ConstantGloble.ACC_TO_TRAN_CRCD:
				// 我要转账——作为转出账户——长城信用卡
				TranDataCenter.getInstance().setAccInInfoMap(null);
				outCreditCurrencyAndBalance = new ArrayList<Map<String, String>>();
				mTopRightBtn.setVisibility(View.GONE);
				isTranOutFirst = false;
				mAccOutLl.setEnabled(false);
				addOutforMap = TranDataCenter.getInstance().getAccOutInfoMap();

				// 组装底部视图数据
				// combineCrcdData();
				// 查询信用卡币种
				// accInInfoMap =
				// TranDataCenter.getInstance().getAccInInfoMap();
				String accountId = (String) addOutforMap.get(Comm.ACCOUNT_ID);
				// requestForOutCrcdCurrency(accoutNumber);
				// requestCrcdCurrency1(accoutNumber);
				outCrcdCurrency1 = (String) addOutforMap.get("currencyCode");
				outCrcdCurrency2 = (String) addOutforMap.get("currencyCode2");
				// 查询信用卡详情
				if (!StringUtil.isNull(outCrcdCurrency1)) {
					requestForCrcdDetailFrom(accountId, outCrcdCurrency1);
				}

				break;

			case ConstantGloble.ACC_TO_TRAN_REMIT:
				// 我要转账——作为转入账户——长城信用卡
				inCreditCurrencyAndBalance = new ArrayList<Map<String, String>>();
				mTopRightBtn.setVisibility(View.GONE);
				isTranInFirst = false;
				mAccInLl.setEnabled(false);
				isOpenFlag = false;// 全球交易人民币记账功能 重置
				addInforMap = TranDataCenter.getInstance().getAccInInfoMap();
				String accountId1 = (String) addInforMap.get(Comm.ACCOUNT_ID);
				inCrcdCurrency1 = (String) addInforMap.get("currencyCode");
				inCrcdCurrency2 = (String) addInforMap.get("currencyCode2");
				// 查询信用卡详情
				if (!StringUtil.isNull(inCrcdCurrency1)) {
					requestForCrcdDetailIn(accountId1, inCrcdCurrency1);
				}

				break;

			case ConstantGloble.CREDIT_TO_TRAN:
				// P503 信用卡优化 信用卡还款统一。
				tran_acc_in_person.setText(getResources().getString(R.string.tran_acc_in_person_crcd));
				tran_please_choose_in.setText(getResources().getString(R.string.tran_please_choose_in_crcd));
				TranDataCenter.getInstance().setAccOutInfoMap(null);
				mTopRightBtn.setVisibility(View.GONE);
				isTranOutFirst = true;
				mAccInLl.setEnabled(false);
				inCreditCurrencyAndBalance = new ArrayList<Map<String, String>>();
				mTopRightBtn.setVisibility(View.GONE);
				add_receipt_person.setVisibility(View.GONE);
				mAccInLl.setEnabled(false);
				addInforMap = TranDataCenter.getInstance().getAccInInfoMap();
				currInDetail = TranDataCenter.getInstance()
						.getRelCrcdBuyCallBackMap();
				List<Map<String, Object>> actlist = (List<Map<String, Object>>) addInforMap
						.get(Crcd.CRCD_ACTLIST);
				if (actlist.size() == 1) {
					// 一个币种直接显示
					float scale = context.getResources().getDisplayMetrics().density;
					mAccInLlHeight = (int) (100 * scale + 0.5f);
					initinlistandbuttom(addInforMap, currInDetail);
				} else if (actlist.size() == 2) {
					// 2个币种请求全球人民币记账标识
					String crcdaccountId = (String) addInforMap
							.get(Comm.ACCOUNT_ID);
					requestPsnCrcdChargeOnRMBAccountQueryOther(crcdaccountId);
				}
				break;
			default:
				TranDataCenter.getInstance().setAccOutInfoMap(null);
				TranDataCenter.getInstance().setAccInInfoMap(null);
				// 默认选择第一个账户
//				requestTranoutAccountListfirst();
				break;
			}
		}

	}

	private void requestTranoutAccountListfirst() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.COMMONQUERYALLCHINABANKACCOUNT);
		String[] accountTypeArr = new String[] { ConstantGloble.ACC_TYPE_ORD,
				ConstantGloble.ACC_TYPE_BRO, ConstantGloble.ACC_TYPE_RAN,
				ConstantGloble.ACC_TYPE_GRE, ConstantGloble.ZHONGYIN,
				ConstantGloble.SINGLEWAIBI, ConstantGloble.ACC_TYPE_BOCINVT };
		// , AccBaseActivity.YOUHUITONGZH
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.ACCOUNTTYPE_REQ, accountTypeArr);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestTranoutAccountListfirstCallBack");
	
		
	}

	public void requestTranoutAccountListfirstCallBack(Object resultObj) {

		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody
				.getResult();

				if (StringUtil.isNullOrEmpty(result)) {// 如果没有符合要求的数据 弹框提示
					
					return;
				}
				
				List<Map<String, Object>> crcdList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> tranOut = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> bocinvtList = new ArrayList<Map<String, Object>>();
			
					for (Map<String, Object> map : result) {
						String accType = (String) map.get(Acc.ACC_ACCOUNTTYPE_RES);
				
						if (accType.equals(ConstantGloble.ACC_TYPE_BOCINVT)) {
							bocinvtList.add(map);
						} else {
							tranOut.add(map);
						}
					}
				

				TranDataCenter.getInstance().setCrcdList(crcdList);
				TranDataCenter.getInstance().setAccountOutList(tranOut);
				TranDataCenter.getInstance().setBocinvtList(bocinvtList);
				outOrInFlag = OUT;
				inQueryDetailIntercept = false;
				if (!StringUtil.isNullOrEmpty(outCurrencyAndBalance)) {
					outCurrencyAndBalance.clear();
				}
				if (!StringUtil.isNullOrEmpty(outCreditCurrencyAndBalance)) {
					outCreditCurrencyAndBalance.clear();
				}
				Map<String, Object> accOutInfoMap = result.get(0);
				// 存储转出账户信息
				addOutforMap = accOutInfoMap;
				// 通讯查询详情
				String accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
				String accountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
				// 根据账户类型 查询不同接口
				if (ConstantGloble.ACC_TYPE_GRE.equals(accountType)
						|| ConstantGloble.ZHONGYIN.equals(accountType)
						// p601 增加 107
						|| ConstantGloble.SINGLEWAIBI.equals(accountType)) {// 信用卡
					// 查询信用卡详情
					// 设置查询接口和条件
					outCreditCurrencyAndBalance = new ArrayList<Map<String, String>>();
					requestForCrcdDetail(accountId);
				} else if (ConstantGloble.ACC_TYPE_BOCINVT.equals(accountType)) {// 网上专属理财账户——需要查询绑定状态
					requestAcctOF();
				} else {// 其他类型卡
					requestForAccountDetail(accountId);
				}
	
	
	}
	/** 查询由信用卡进入 信用卡还款 的第一币种 详情 */
	private void requestForCrcdDetailFromRepay(String accountId,
			String outCrcdCurrency1) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.CRCDQUERYACCOUNTDETAIL);
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_ID, accountId);
		// outCrcdCurrency2 为要查询的币种
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY, outCrcdCurrency1);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCrcdDetailCallBackFromRepay");
	}

	public void requestForCrcdDetailCallBackFromRepay(Object resultObj) {
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

		for (int i = 0; i < detailList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("loanBalanceLimitFlag",
					detailList.get(i).get("loanBalanceLimitFlag"));
			map.put("loanBalanceLimit",
					detailList.get(i).get("loanBalanceLimit"));
			map.put("currency", detailList.get(i).get("currency"));
			inCreditCurrencyAndBalance.add(map);
		}

		if (!StringUtil.isNull(inCrcdCurrency2)) {
			String accountId = (String) addInforMap.get(Comm.ACCOUNT_ID);
			requestForCrcdDetailFromForeignInRepay(accountId, inCrcdCurrency2);
		} else {
			showAccInDetailView();
			BaseHttpEngine.dissMissProgressDialog();
		}

	}

	/** 由 信用卡 还款 进入查询转入账户信用卡第二币种 详情 */
	private void requestForCrcdDetailFromForeignInRepay(String accountId,
			String inCrcdCurrency2) {

		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.CRCDQUERYACCOUNTDETAIL);
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_ID, accountId);
		// outCrcdCurrency2 为要查询的币种
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY, inCrcdCurrency2);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCrcdDetailCallBackFromForeignInRepay");
	}

	public void requestForCrcdDetailCallBackFromForeignInRepay(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
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

		// 取 信用卡外币 账户信息

		for (int i = 0; i < detailList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("loanBalanceLimitFlag",
					detailList.get(i).get("loanBalanceLimitFlag"));
			map.put("loanBalanceLimit",
					detailList.get(i).get("loanBalanceLimit"));
			map.put("currency", detailList.get(i).get("currency"));
			inCreditCurrencyAndBalance.add(map);
		}

		String accountId = (String) addInforMap.get(Comm.ACCOUNT_ID);
		requestPsnCrcdChargeOnRMBAccountQueryOther(accountId);
	}

	/** 查询由信用卡进入 转账汇款页面 的第一币种 详情 */
	private void requestForCrcdDetailIn(String accountId, String inCrcdCurrency1) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.CRCDQUERYACCOUNTDETAIL);
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_ID, accountId);
		// outCrcdCurrency2 为要查询的币种
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY, inCrcdCurrency1);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCrcdDetailCallBackFromIn");

	}

	/** 查询由信用卡进入 转账汇款页面 的第一币种 详情 */
	private void requestForCrcdDetailFrom(String accountId,
			String outCrcdCurrency1) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.CRCDQUERYACCOUNTDETAIL);
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_ID, accountId);
		// outCrcdCurrency2 为要查询的币种
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY, outCrcdCurrency1);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCrcdDetailCallBackFrom");

	}

	public void requestForCrcdDetailCallBackFromIn(Object resultObj) {
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

		for (int i = 0; i < detailList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("loanBalanceLimitFlag",
					detailList.get(i).get("loanBalanceLimitFlag"));
			map.put("loanBalanceLimit",
					detailList.get(i).get("loanBalanceLimit"));
			map.put("currency", detailList.get(i).get("currency"));
			inCreditCurrencyAndBalance.add(map);
		}

		if (!StringUtil.isNull(inCrcdCurrency2)) {
			String accountId = (String) addInforMap.get(Comm.ACCOUNT_ID);
			requestForCrcdDetailFromForeignIn(accountId, inCrcdCurrency2);
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			showAccInDetailView();
		}

	}

	public void requestForCrcdDetailCallBackFrom(Object resultObj) {
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

		for (int i = 0; i < detailList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("loanBalanceLimitFlag",
					detailList.get(i).get("loanBalanceLimitFlag"));
			map.put("loanBalanceLimit",
					detailList.get(i).get("loanBalanceLimit"));
			map.put("currency", detailList.get(i).get("currency"));
			outCreditCurrencyAndBalance.add(map);
		}

		if (!StringUtil.isNull(outCrcdCurrency2)) {
			String accountId = (String) addOutforMap.get(Comm.ACCOUNT_ID);
			requestForCrcdDetailFromForeign(accountId, outCrcdCurrency2);
		} else {
			showAccOutDetailView();
			BaseHttpEngine.dissMissProgressDialog();
		}

	}

	/** 查询转出账户信用卡第二币种 详情 */
	private void requestForCrcdDetailFromForeign(String accountId,
			String outCrcdCurrency2) {

		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.CRCDQUERYACCOUNTDETAIL);
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_ID, accountId);
		// outCrcdCurrency2 为要查询的币种
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY, outCrcdCurrency2);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCrcdDetailCallBackFromForeign");
	}

	/** 查询转出账户信用卡第二币种 详情 */
	private void requestForCrcdDetailFromForeignIn(String accountId,
			String outCrcdCurrency2) {

		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.CRCDQUERYACCOUNTDETAIL);
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_ID, accountId);
		// outCrcdCurrency2 为要查询的币种
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY, outCrcdCurrency2);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCrcdDetailCallBackFromForeignIn");
	}

	public void requestForCrcdDetailCallBackFromForeignIn(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
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

		// 取 信用卡外币 账户信息

		for (int i = 0; i < detailList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("loanBalanceLimitFlag",
					detailList.get(i).get("loanBalanceLimitFlag"));
			map.put("loanBalanceLimit",
					detailList.get(i).get("loanBalanceLimit"));
			map.put("currency", detailList.get(i).get("currency"));
			inCreditCurrencyAndBalance.add(map);
		}

		String accountId = (String) addInforMap.get(Comm.ACCOUNT_ID);

		requestPsnCrcdChargeOnRMBAccountQueryOther(accountId);

	}

	/**
	 * 全球交易人民币记账功能查询
	 * 
	 * @param accountId
	 *            :网银账户标志
	 */
	private void requestPsnCrcdChargeOnRMBAccountQueryOther(String accountId) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_RMBACCOUNTQUERY_API);
		Map<String, String> params = new HashMap<String, String>();
		params.put(Crcd.CRCD_ACCOUNTID_REQ, accountId);
		biiRequestBody.setParams(params);
		// biiRequestBody.setConversationId(null);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCommonQueryAllChinaBankAccountCallBackOther");
	}

	public void requestPsnCommonQueryAllChinaBankAccountCallBackOther(
			Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, String> result = (Map<String, String>) biiResponseBody
				.getResult();

		String openFlag = result.get(Crcd.CRCD_OPENFLAG_RES);

		if ("true".equals(openFlag)) {
			isOpenFlag = true;
		} else {
			isOpenFlag = false;
		}

		if (otherPartJumpFlag == ConstantGloble.CREDIT_TO_TRAN) {
			initinlistandbuttom(addInforMap, currInDetail);
		} else {
			showAccInDetailView();
		}

	}

	public void requestForCrcdDetailCallBackFromForeign(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
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

		// 取 信用卡外币 账户信息

		for (int i = 0; i < detailList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("loanBalanceLimitFlag",
					detailList.get(i).get("loanBalanceLimitFlag"));
			map.put("loanBalanceLimit",
					detailList.get(i).get("loanBalanceLimit"));
			map.put("currency", detailList.get(i).get("currency"));
			outCreditCurrencyAndBalance.add(map);
		}
		showAccOutDetailView();
	}

	/**
	 * 转出账户按钮点击事件
	 */
	private OnClickListener accOutClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = getIntent();
			otherPartJumpFlag = intent.getIntExtra(
					ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
			if (StringUtil.isNullOrEmpty(otherPartJumpFlag)) {
				return;
			}
			if (otherPartJumpFlag == 0) {
				TranDataCenter.getInstance().setModuleType(0);
			}
			// // 点击转出账户之后 将accoutInfoMap置空
			// accOutInfoMap = null;
			// 发送通讯 请求转出账户列表
			if (isTranOutFirst) {
				requestTranoutAccountList();
			} else {
				showAccOutListDialog();
			}
			mAccOutLl.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_card_new));
//			dept_out_info_layout.setVisibility(View.GONE);
		}
	};
	/**
	 * 转入账户按钮点击事件
	 */
	private OnClickListener accInClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 自己模块进入
			if (isTranOutFirst) {
				CustomDialog.toastInCenter(context,
						TransferManagerActivity1.this
								.getString(R.string.choose_outno_message_new));
				mAccOutLl.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_card_new_red));
//				dept_out_info_layout.setVisibility(View.VISIBLE);
				return;
			}
			if (ConstantGloble.REL_CRCD_BUY == otherPartJumpFlag) {
				// sunh 信用卡购汇 优化 转入账户不可点击
				return;
			}
			Intent intent = getIntent();
			if (intent != null) {
				otherPartJumpFlag = intent.getIntExtra(
						ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
				if (StringUtil.isNullOrEmpty(otherPartJumpFlag)) {
					return;
				}
				if (otherPartJumpFlag == 0) {
					TranDataCenter.getInstance().setModuleType(0);
				}
				if (otherPartJumpFlag == ConstantGloble.ACC_TO_TRAN
						|| otherPartJumpFlag == ConstantGloble.ACC_TO_TRAN_CRCD) {
					if (isTranInFirst) {
						// 发送通讯请求转入账户信息
						requestTranFromAccountList();
					} else {
						showAccInListDialog();
					}
				} else {
					if (isTranInFirst) {
						// 发送通讯请求转入账户信息
						requestAccInBankAccountList();
					} else {
						if (!isrequestAccIn) {
							// 发送通讯请求转入账户信息
							requestAccInBankAccountList();
						} else {
							showAccInListDialog();
						}

					}
				}
			}

		}
	};
	/**
	 * 新增关联账户
	 */
	private OnClickListener addNewAcc = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			Intent intent = (new Intent(TransferManagerActivity1.this,
//					AccInputRelevanceAccountActivity.class));
//			startActivityForResult(intent,
//					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			
			BusinessModelControl.gotoAccRelevanceAccount(TransferManagerActivity1.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
		}
	};
	/**
	 * 新增收款人
	 */
	private OnClickListener addAccInNoClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO 如果转出账户为长城信用卡，只能新增实时和行内账户
			Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
					.getAccOutInfoMap();
			// 转出账户类型 为单外币 不支持新增收款人
			String outAccountType = (String) accOutInfoMap
					.get(Comm.ACCOUNT_TYPE);
			if (ConstantGloble.SINGLEWAIBI.equals(outAccountType)) {
				String errorInfo = getResources().getString(
						R.string.out_singlewaibi_error);
				BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);

			} else {
				goAddAcc();
			}

		}
	};

	private void goAddAcc() {
		Intent intent2 = new Intent(TransferManagerActivity1.this,
				WritePayeeInfoActivity1.class);
		TransferManagerActivity1.this.startActivityForResult(intent2,
				Tran.WRITEPAYEEINFOACTIVITY_REQUEST_CODE);
	}

	/**
	 * 转出账户列表ListView的监听事件
	 */
	private OnItemClickListener clickAccOutListItem = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			outOrInFlag = OUT;
			inQueryDetailIntercept = false;
			if (!StringUtil.isNullOrEmpty(outCurrencyAndBalance)) {
				outCurrencyAndBalance.clear();
			}
			if (!StringUtil.isNullOrEmpty(outCreditCurrencyAndBalance)) {
				outCreditCurrencyAndBalance.clear();
			}
			Map<String, Object> accOutInfoMap = accOutListCopy.get(position);
			// 存储转出账户信息
			addOutforMap = accOutInfoMap;
			Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
					.getAccInInfoMap();
			// 判断转出账户是否跟转入账户相同 如果相同 将转入账户卡片清空，重新弹出转入账户框
			if (!isTranInFirst) {
				Intent intent = getIntent();
				otherPartJumpFlag = intent.getIntExtra(
						ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
				if (StringUtil.isNullOrEmpty(otherPartJumpFlag)) {
					return;
				}
				String type = (String) accInInfoMap
						.get(Acc.ACC_ACCOUNTTYPE_RES);
				if (otherPartJumpFlag == ConstantGloble.CREDIT_TO_TRAN// 信用卡还款
						&& !type.equals(ConstantGloble.SINGLEWAIBI)
						// 信用卡购汇,不是单外币，人民币有结余，这时转入和转出可以是同一张卡
						&& cansamecard) {

				} else {
					String outAccNo = (String) accOutInfoMap
							.get(Comm.ACCOUNTNUMBER);
					String inAccNo = (String) accInInfoMap
							.get(Comm.ACCOUNTNUMBER);
					if(StringUtil.isNullOrEmptyCaseNullString(inAccNo)){
						inAccNo = (String) accInInfoMap
								.get(Crcd.CRCD_ACCTNUM);
					}

					String outId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
					String inId = (String) accInInfoMap.get(Comm.ACCOUNT_ID);
					if (outAccNo.equals(inAccNo) && outId.equals(inId)) {
						BaseDroidApp.getInstanse().createDialog(null,
								R.string.choose_other_accout);
						return;
					}
				}
// 103 不支持普通跨行跨行
				if(!isTranInFirst&&  tranTypeFlag == TRANTYPE_NOREL_BANKOTHER
									){
					String accountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
					if(ConstantGloble.ZHONGYIN.equals(accountType)){
						BaseDroidApp.getInstanse().createDialog(null,
								R.string.out_acctypegre_error);
						return;	
					}
				}
				
			}

			// 通讯查询详情
			String accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
			String accountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
			String accountNumber = (String) accOutInfoMap
					.get(Comm.ACCOUNTNUMBER);
			// 根据账户类型 查询不同接口
			if (ConstantGloble.ACC_TYPE_GRE.equals(accountType)
					|| ConstantGloble.ZHONGYIN.equals(accountType)
					// p601 增加 107
					|| ConstantGloble.SINGLEWAIBI.equals(accountType)) {// 信用卡
				// 查询信用卡详情
				// 设置查询接口和条件
				outCreditCurrencyAndBalance = new ArrayList<Map<String, String>>();
				requestForCrcdDetail(accountId);
				// requestForOutCrcdCurrency(accountNumber);
			} else if (ConstantGloble.ACC_TYPE_BOCINVT.equals(accountType)) {// 网上专属理财账户——需要查询绑定状态
				requestAcctOF();
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
	 * 显示转出账户列表
	 */
	private void showAccOutListDialog() {
		List<Map<String, Object>> accoutList = TranDataCenter.getInstance()
				.getAccountOutList();
		List<Map<String, Object>> bocinvtList = TranDataCenter.getInstance()
				.getBocinvtList();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.addAll(accoutList);
		list.addAll(bocinvtList);
		if (StringUtil.isNullOrEmpty(list)) {// 如果没有符合要求的数据 弹框提示
			String message = this.getString(R.string.no_accout);
			BaseDroidApp.getInstanse().showErrorDialog(message,
					R.string.cancle, R.string.confirm, onNoOutclicklistener);
			return;
		}
		accOutListCopy = new ArrayList<Map<String, Object>>();
		accOutListCopy.addAll(accoutList);
		accOutListCopy.addAll(bocinvtList);
		// 如果转入账户是信用卡 过滤掉101,188,注：403需要过滤掉199
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		if (!isTranInFirst && !StringUtil.isNullOrEmpty(accInInfoMap)) {
			String accountType = (String) accInInfoMap.get(Comm.ACCOUNT_TYPE);
			if (!StringUtil.isNullOrEmpty(accountType)) {
				if (accountType.equals(ConstantGloble.ACC_TYPE_GRE)
						|| accountType.equals(ConstantGloble.ZHONGYIN)
						|| accountType.equals(ConstantGloble.SINGLEWAIBI)) {
					accOutListCopy.removeAll(bocinvtList);
					for (int i = 0; i < accoutList.size(); i++) {// 过滤掉活期
						Map<String, Object> map = accoutList.get(i);
						String strAccType = (String) map.get(Comm.ACCOUNT_TYPE);
						if (strAccType.equals(ConstantGloble.ACC_TYPE_ORD)
								|| strAccType
										.equals(ConstantGloble.ACC_TYPE_RAN)
								|| strAccType
										.equals(AccBaseActivity.YOUHUITONGZH)) {
							accOutListCopy.remove(map);
						}
					}
				}
			}
			// 如果转入账户是非关联账户，转出账户不能为199——QDII2账户
			if (tranTypeFlag != TRANTYPE_REL_ACCOUNT// 关联账户转账
					&& tranTypeFlag != TRANTYPE_REL_CRCD_REPAY// 本人关联信用卡还款
					&& tranTypeFlag != TRANTYPE_REL_CRCD_BUY// 本人关联信用卡购汇还款
			) {
				// 转入账户选择的是非关联账户，转出账户不能是199
				// 转入账户选择的是非关联账户，转出账户不能是107
				for (int i = 0; i < accoutList.size(); i++) {// 过滤掉活期
					Map<String, Object> map = accoutList.get(i);
					String strAccType = (String) map.get(Comm.ACCOUNT_TYPE);
					if (strAccType.equals(AccBaseActivity.YOUHUITONGZH)
							|| strAccType.equals(ConstantGloble.SINGLEWAIBI)) {
						accOutListCopy.remove(map);
					}
				}
			}
		}
		if (StringUtil.isNullOrEmpty(accOutListCopy)) {// 如果没有符合要求的数据 弹框提示
			String message = this.getString(R.string.no_accout);
			BaseDroidApp.getInstanse().showErrorDialog(message,
					R.string.cancle, R.string.confirm, onNoOutclicklistener);
			return;
		}
		accOutListView = new ListView(context);
		accOutListView.setFadingEdgeLength(0);
		accOutListView.setScrollingCacheEnabled(false);
		accOutListView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		accOutListView.setBackgroundColor(Color.WHITE);
		AccOutListAdapter outadapter = new AccOutListAdapter(context,
				accOutListCopy);
		accOutListView.setAdapter(outadapter);
		accOutListView.setOnItemClickListener(clickAccOutListItem);
		// 弹出转出转换列表框
		BaseDroidApp.getInstanse().showTranoutDialog(accOutListView,
				backListener, /*addNewAcc*/null);//屏蔽自助关联
	}

	/**
	 * 没有转出账户情况
	 */
	private OnClickListener onNoOutclicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_CANCLE:// 取消
				BaseDroidApp.getInstanse().dismissErrorDialog();
				break;
			case CustomDialog.TAG_SURE:// 确定
				BaseDroidApp.getInstanse().dismissErrorDialog();
//				Intent intent = (new Intent(TransferManagerActivity1.this,
//						AccInputRelevanceAccountActivity.class));
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
				BusinessModelControl.gotoAccRelevanceAccount(TransferManagerActivity1.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
				
				break;
			default:
				break;
			}

		}
	};

	/**
	 * 显示转出账户列表详情
	 */
	private void showAccOutDetailView() {
		BaseDroidApp.getInstanse().dismissMessageDialog();
		accOutDetailLayout = (LinearLayout) inflater.inflate(
				R.layout.dept_acc_out_list_detail, null);

		mAccOutLl.removeAllViews();
		mAccOutLl.addView(accOutDetailLayout);
		mAccOutLl.setVisibility(View.VISIBLE);
		mAccOutListLayout.setVisibility(View.GONE);
		newAddTranOutBtn.setVisibility(View.GONE);
		showAccOutListItemData(accOutDetailLayout);// 显示转出账户条目的详细信息
		// 判断是否是其他模块进来的
		Intent intent = getIntent();
		if (intent != null) {
			otherPartJumpFlag = intent.getIntExtra(
					ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
			if (StringUtil.isNullOrEmpty(otherPartJumpFlag)) {
				return;
			}
			if (otherPartJumpFlag == ConstantGloble.REL_CRCD_BUY
					|| otherPartJumpFlag == ConstantGloble.REL_CRCD_REPAY
					|| otherPartJumpFlag == ConstantGloble.ACC_TO_TRAN
					|| otherPartJumpFlag == ConstantGloble.ACC_TO_TRAN_CRCD
					|| otherPartJumpFlag == ConstantGloble.CREDIT_TO_TRAN) {
				mTopRightBtn.setVisibility(View.GONE);
			} else {
				mTopRightBtn.setVisibility(View.VISIBLE);
			}
		} else {
			mTopRightBtn.setVisibility(View.VISIBLE);
		}
		isTranOutFirst = false;
		// T43 选择完转出账户之后 根据类型显示快捷按钮
		// add start
		tran_acc_seach_linear.setVisibility(View.VISIBLE);
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		String accountType = "";
		if (!StringUtil.isNullOrEmpty(accOutInfoMap)) {

			accountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
		}
		boolean iscanmobile = false;
		boolean iscantwodimen = true;
		if (!StringUtil.isNull(accountType)) {
			if (accountType.equals(ConstantGloble.ACC_TYPE_GRE)
					|| accountType.equals(ConstantGloble.ACC_TYPE_BRO)) {
				iscanmobile = true;
			}
			if (accountType.equals(AccBaseActivity.YOUHUITONGZH)
					|| accountType.equals(ConstantGloble.ACC_TYPE_BOCINVT)) {
				iscantwodimen = false;
				tran_acc_seach_linear.setVisibility(View.GONE);
			}
		}
		// 二维码转账
		Button twoDimension = (Button) container
				.findViewById(R.id.btn_twoDimension_acc_in_mytransfer);
		twoDimension.setOnClickListener(twoDimensionListener);
		// 手机号转账
		Button phoneBt = (Button) container
				.findViewById(R.id.btn_phoneNum_acc_in_mytransfer);
		phoneBt.setOnClickListener(phoneListener);
		if (iscanmobile) {
			phoneBt.setVisibility(View.VISIBLE);
		} else {
			phoneBt.setVisibility(View.INVISIBLE);
		}
		if (iscantwodimen) {
			twoDimension.setVisibility(View.VISIBLE);
		} else {
			twoDimension.setVisibility(View.INVISIBLE);
		}
		// add end
	}

	private void showAccInDetailView() {
		BaseDroidApp.getInstanse().dismissMessageDialog();
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
		// mAccInListLayout.setVisibility(View.GONE);
		newAddTranInBtn.setVisibility(View.GONE);
		showAccInListItemData(accInDetailLayout);
		// tran_acc_seach_linear.setVisibility(View.GONE);
		isTranInFirst = false;
	}

	/**
	 * 显示转出账户条目的详细信息
	 */
	private void showAccOutListItemData(View view) {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		if (accOutInfoMap == null) {
			return;
		}
		// intersection = new ArrayList<String>();
		LinearLayout tv_crrey_accOut_layout_one = (LinearLayout) view
				.findViewById(R.id.tv_crrey_accOut_layout_one);
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

		LinearLayout linerlayoutTwo = (LinearLayout) view
				.findViewById(R.id.tv_crrey_accOut_layout_two);
		TextView tv_currey_accOut_list_detail_two = (TextView) view
				.findViewById(R.id.tv_currey_accOut_list_detail_two);
		TextView tv_lastprice_accOut_list_detail_two = (TextView) view
				.findViewById(R.id.tv_lastprice_accOut_list_detail_two);

		LinearLayout linerlayoutThree = (LinearLayout) view
				.findViewById(R.id.tv_crrey_accOut_layout_three);
		TextView tv_currey_accOut_list_detail_three = (TextView) view
				.findViewById(R.id.tv_currey_accOut_list_detail_three);
		TextView tv_lastprice_accOut_list_detail_three = (TextView) view
				.findViewById(R.id.tv_lastprice_accOut_list_detail_three);

		// if (ConstantGloble.REL_CRCD_BUY != otherPartJumpFlag) {
		// // sunh 信用卡购汇 优化 显示币种余额
		tv_crrey_accOut_layout_one.setVisibility(View.GONE);
		// } else {
		// tv_crrey_accOut_layout_one.setVisibility(View.VISIBLE);

		// }
		linerlayoutTwo.setVisibility(View.GONE);
		linerlayoutThree.setVisibility(View.GONE);
		// 转出账户类型
		String strAccountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
		// 转入账户类型
		String strAccountTypeIn = "";
		if (!StringUtil.isNullOrEmpty(accInInfoMap)) {
			strAccountTypeIn = (String) accInInfoMap.get(Comm.ACCOUNT_TYPE);

		}

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
		// 根据卡类型 获取的数据字段不一样
		// 如果是长城信用卡
		if (strAccountType.equals(ConstantGloble.ACC_TYPE_GRE)
				|| strAccountType.equals(ConstantGloble.ZHONGYIN)
				// P601 107 可以转出
				|| strAccountType.equals(ConstantGloble.SINGLEWAIBI)) {
			Map<String, String> detailMap = new HashMap<String, String>();
			List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
			detailList = (List<Map<String, String>>) resultMap
					.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
			detailMap = detailList.get(0);
			availableBalance = (String) detailMap
					.get(Crcd.CRCD_LOANBALANCELIMIT);
			currencyCode = detailMap.get(Crcd.CRCD_CURRENCY_RES);
			String flag = (String) detailMap.get(Crcd.LOANBALANCELIMITFLAG);
			cashTv.setText(StringUtil.parseStringCodePattern(currencyCode,
					availableBalance, 2));

		} else {
			@SuppressWarnings("unchecked")
			List<Map<String, String>> detialList = (List<Map<String, String>>) resultMap
					.get(ConstantGloble.ACC_DETAILIST);
			if (StringUtil.isNullOrEmpty(detialList)) {
				return;
			}
			currencyCode = null;

			outCurrencyAndBalance = new ArrayList<Map<String, String>>();
			// 显示人名币的那个数据

			for (int i = 0; i < detialList.size(); i++) {
				Map<String, String> map = detialList.get(i);
				// 存放对应的 币种 和 余额
				Map<String, String> map1 = new HashMap<String, String>();
				String currency = map.get(Comm.CURRENCYCODE);
				String Balance = map.get(Dept.AVAILABLE_BALANCE);
				String cashRemit = map.get(Dept.DEPT_CASHREMIT_RES);
				map1.put("currency", currency);
				map1.put("Balance", Balance);
				map1.put("cashRemit", cashRemit);
				outCurrencyAndBalance.add(map1);
				if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {
					currencyCode = currency;
					availableBalance = map.get(Dept.AVAILABLE_BALANCE);
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

		int height = mAccOutLlHeight;
		// 如果需要修改控件高度 那就恢复为默认的
		boolean restore = true;

		if (ConstantGloble.ACC_TYPE_BRO.equals(strAccountType)) {
			// 转出账户 是 电子借记卡 转入是信用卡
			if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountTypeIn)
					|| ConstantGloble.SINGLEWAIBI.equals(strAccountTypeIn)
					|| ConstantGloble.ZHONGYIN.equals(strAccountTypeIn)) {
				for (int i = 0; i < outCurrencyAndBalance.size(); i++) {

					for (int j = 0; j < inCreditCurrencyAndBalance.size(); j++) {

						// 如果是交集 那么显示
						if (outCurrencyAndBalance
								.get(i)
								.get("currency")
								.equals(inCreditCurrencyAndBalance.get(j).get(
										"currency"))) {
							if (!tv_crrey_accOut_layout_one.isShown()) {
								tv_crrey_accOut_layout_one
										.setVisibility(View.VISIBLE);
								String flag = outCurrencyAndBalance.get(i).get(
										"cashRemit");
								flag = flagConvertRemitFlag(flag);
								String currencyCode = outCurrencyAndBalance
										.get(i).get("currency");
								String Balance = outCurrencyAndBalance.get(i)
										.get("Balance");
								currencyCodeTv.setText(LocalData.Currency
										.get(currencyCode) + flag);
								cashTv.setText(StringUtil
										.parseStringCodePattern(currencyCode,
												Balance, 2));
								continue;
							} else if (!linerlayoutTwo.isShown()) {
								height = mAccOutLlHeight + mAccOutLlHeight / 5;
								linerlayoutTwo.setVisibility(View.VISIBLE);
								String flag = outCurrencyAndBalance.get(i).get(
										"cashRemit");
								flag = flagConvertRemitFlag(flag);
								String currencyCode = outCurrencyAndBalance
										.get(i).get("currency");
								String Balance = outCurrencyAndBalance.get(i)
										.get("Balance");
								tv_currey_accOut_list_detail_two
										.setText(LocalData.Currency
												.get(currencyCode) + flag);
								tv_lastprice_accOut_list_detail_two
										.setText(StringUtil
												.parseStringCodePattern(
														currencyCode, Balance,
														2));
								continue;
							} else if (!linerlayoutThree.isShown()) {
								height = mAccOutLlHeight + mAccOutLlHeight / 2;
								String flag = outCurrencyAndBalance.get(i).get(
										"cashRemit");
								flag = flagConvertRemitFlag(flag);
								linerlayoutThree.setVisibility(View.VISIBLE);
								String currencyCode = outCurrencyAndBalance
										.get(i).get("currency");
								String Balance = outCurrencyAndBalance.get(i)
										.get("Balance");
								tv_currey_accOut_list_detail_three
										.setText(LocalData.Currency
												.get(currencyCode) + flag);
								tv_lastprice_accOut_list_detail_three
										.setText(StringUtil
												.parseStringCodePattern(
														currencyCode, Balance,
														2));
								continue;
							}

						}
					}
				}

				mAccOutLl.getMeasuredWidth();
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
						mAccOutLl.getMeasuredWidth(), height);
				mAccOutLl.setLayoutParams(ll);
				restore = false;
			}

		}

		// 转入账户是电子借记卡 转出账户信用卡
		if (ConstantGloble.ACC_TYPE_BRO.equals(strAccountTypeIn)
				&& !inQueryDetailIntercept) {
			if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountType)
					|| ConstantGloble.SINGLEWAIBI.equals(strAccountType)// 601
																		// 增加1107
					|| ConstantGloble.ZHONGYIN.equals(strAccountType)) {

				for (int i = 0; i < outCreditCurrencyAndBalance.size(); i++) {

					// for (int j = 0; j < outCurrencyAndBalance.size(); j++) {
					// // 如果是交集 那么显示
					// if (outCreditCurrencyAndBalance
					// .get(i)
					// .get("currency")
					// .equals(outCurrencyAndBalance.get(j).get(
					// "currency"))) {
					// 转出信用卡 不显示交集
					// 下边需修改 标识

					if (!tv_crrey_accOut_layout_one.isShown()) {
						tv_crrey_accOut_layout_one.setVisibility(View.VISIBLE);
						String flag = outCreditCurrencyAndBalance.get(i).get(
								"loanBalanceLimitFlag");
						flag = flagConvert(flag);
						String Balance = outCreditCurrencyAndBalance.get(i)
								.get("loanBalanceLimit");
						String currencyCode = outCreditCurrencyAndBalance
								.get(i).get("currency");
						cashTv.setText(flag
								+ StringUtil.parseStringCodePattern(
										currencyCode, Balance, 2));
						currencyCodeTv.setText(LocalData.Currency
								.get(currencyCode));
						continue;
					} else if (!linerlayoutTwo.isShown()) {
						String currencyCode = outCreditCurrencyAndBalance
								.get(i).get("currency");
						if (currencyCodeTv.getText().equals(
								LocalData.Currency.get(currencyCode))) {
							continue;
						}
						linerlayoutTwo.setVisibility(View.VISIBLE);
						height = mAccOutLlHeight + mAccOutLlHeight / 5;
						tv_currey_accOut_list_detail_two
								.setText(LocalData.Currency.get(currencyCode));
						String flag = outCreditCurrencyAndBalance.get(i).get(
								"loanBalanceLimitFlag");
						flag = flagConvert(flag);
						String Balance = outCreditCurrencyAndBalance.get(i)
								.get("loanBalanceLimit");
						tv_lastprice_accOut_list_detail_two.setText(flag
								+ StringUtil.parseStringCodePattern(
										currencyCode, Balance, 2));
					}
					// }
					// }
				}

				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
						mAccOutLl.getMeasuredWidth(), height);
				mAccOutLl.setLayoutParams(ll);
				restore = false;

			}
		}

		// 转入账户是信用卡 转出 账户信用卡
		if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountTypeIn)
				|| ConstantGloble.SINGLEWAIBI.equals(strAccountTypeIn)
				|| ConstantGloble.ZHONGYIN.equals(strAccountTypeIn)) {
			if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountType)
					|| ConstantGloble.SINGLEWAIBI.equals(strAccountType)// 601
																		// 增加107
					|| ConstantGloble.ZHONGYIN.equals(strAccountType)) {

				for (int i = 0; i < outCreditCurrencyAndBalance.size(); i++) {

					for (int j = 0; j < inCreditCurrencyAndBalance.size(); j++) {
						// 如果是交集 那么显示
						if (outCreditCurrencyAndBalance
								.get(i)
								.get("currency")
								.equals(inCreditCurrencyAndBalance.get(j).get(
										"currency"))) {
							// intersection.add(outCreditCurrencyAndBalance.get(i).get("currency"));

							if (!tv_crrey_accOut_layout_one.isShown()) {
								tv_crrey_accOut_layout_one
										.setVisibility(View.VISIBLE);
								String flag = outCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimitFlag");
								flag = flagConvert(flag);
								String Balance = outCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimit");
								String currencyCode = outCreditCurrencyAndBalance
										.get(i).get("currency");
								cashTv.setText(flag
										+ StringUtil.parseStringCodePattern(
												currencyCode, Balance, 2));
								currencyCodeTv.setText(LocalData.Currency
										.get(currencyCode));
								continue;
							} else if (!linerlayoutTwo.isShown()) {
								linerlayoutTwo.setVisibility(View.VISIBLE);
								height = mAccOutLlHeight + mAccOutLlHeight / 5;
								String currencyCode = outCreditCurrencyAndBalance
										.get(i).get("currency");
								tv_currey_accOut_list_detail_two
										.setText(LocalData.Currency
												.get(currencyCode));
								String flag = outCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimitFlag");
								flag = flagConvert(flag);
								String Balance = outCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimit");
								tv_lastprice_accOut_list_detail_two
										.setText(flag
												+ StringUtil
														.parseStringCodePattern(
																currencyCode,
																Balance, 2));
							}

						}
					}
				}

				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
						mAccOutLl.getMeasuredWidth(), height);
				mAccOutLl.setLayoutParams(ll);
				restore = false;

			}
		}

		// 是否需要回复默认高度
		if (restore) {
			LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
					mAccOutLl.getMeasuredWidth(), mAccOutLlHeight);
			mAccOutLl.setLayoutParams(ll);
			tv_crrey_accOut_layout_one.setVisibility(View.VISIBLE);
		}

		
		if(tranTypeFlag==TRANTYPE_NOREL_CRCD
		  ||tranTypeFlag==TRANTYPE_DIR_CRCD)		 
		{
			// 如果是非关联要显示第一行
			if (tv_crrey_accOut_layout_one.getVisibility() == View.GONE) {
				
				tv_crrey_accOut_layout_one.setVisibility(View.VISIBLE);
			}
		}
		if (ConstantGloble.CREDIT_TO_TRAN == otherPartJumpFlag) {

			if (tv_crrey_accOut_layout_one.getVisibility() == View.GONE) {
				// 信用卡还款 转出 保证显示一行
				tv_crrey_accOut_layout_one.setVisibility(View.VISIBLE);
			}
			// 信用好卡还款
			if (!StringUtil.isNull(strAccountTypeIn)) {
				// RefreshCrcdInDetailView(); sunh

				Refreshcrcdbottom();

			}
		} else {
			if (!StringUtil.isNull(strAccountTypeIn)) {
				// TODO sunh 2015 11-12 更换转出账户 跨行时 不刷新转入
				if(tranTypeFlag ==TRANTYPE_DIR_BANKOTHER
				   ||tranTypeFlag ==TRANTYPE_SHISHI_BANKOTHER
				   ||tranTypeFlag ==TRANTYPE_SHISHI_DIR_BANKOTHER){
					
				}else{
					RefreshAccInDetailView();
				}
				
				
			}
		}
		// if (ConstantGloble.REL_CRCD_BUY != otherPartJumpFlag) {
		// // sunh 信用卡购汇 优化 购汇于其他模块区分
		// if (!StringUtil.isNull(strAccountTypeIn)) {
		// ();
		// }
		// }

	}

	/** 刷新转入账户 详细信息 */
	private void RefreshAccInDetailView() {
		BaseDroidApp.getInstanse().dismissMessageDialog();
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
		// mAccInListLayout.setVisibility(View.GONE);
		newAddTranInBtn.setVisibility(View.GONE);
		RefreshAccInListItemData(accInDetailLayout);
		// tran_acc_seach_linear.setVisibility(View.GONE);
		isTranInFirst = false;
	}

	private void RefreshAccInListItemData(View view) {
		Map<String, Object> accInInfoMap;
		Intent intent = getIntent();
		if (intent != null) {
			otherPartJumpFlag = intent.getIntExtra(
					ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
		}
		if (StringUtil.isNullOrEmpty(otherPartJumpFlag)) {
			return;
		}
		if (otherPartJumpFlag == 0
				|| otherPartJumpFlag == ConstantGloble.ACC_TO_TRAN
				|| otherPartJumpFlag == ConstantGloble.ACC_TO_TRAN_CRCD) {
			accInInfoMap = addInforMap;
		} else {
			// 其他模块
			accInInfoMap = TranDataCenter.getInstance().getAccInInfoMap();
		}
		if (accInInfoMap == null) {
			return;
		}
		// intersection = new ArrayList<String>();
		LinearLayout tv_crrey_accOut_layout_one = (LinearLayout) view
				.findViewById(R.id.tv_crrey_accOut_layout_one);
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

		LinearLayout linerlayoutTwo = (LinearLayout) view
				.findViewById(R.id.tv_crrey_accOut_layout_two);
		TextView tv_currey_accOut_list_detail_two = (TextView) view
				.findViewById(R.id.tv_currey_accOut_list_detail_two);
		TextView tv_lastprice_accOut_list_detail_two = (TextView) view
				.findViewById(R.id.tv_lastprice_accOut_list_detail_two);

		LinearLayout linerlayoutThree = (LinearLayout) view
				.findViewById(R.id.tv_crrey_accOut_layout_three);
		TextView tv_currey_accOut_list_detail_three = (TextView) view
				.findViewById(R.id.tv_currey_accOut_list_detail_three);
		TextView tv_lastprice_accOut_list_detail_three = (TextView) view
				.findViewById(R.id.tv_lastprice_accOut_list_detail_three);

		tv_crrey_accOut_layout_one.setVisibility(View.GONE);
		linerlayoutTwo.setVisibility(View.GONE);
		linerlayoutThree.setVisibility(View.GONE);

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
		LinearLayout ll_gone_tran = (LinearLayout) findViewById(R.id.ll_gone_tran);
		Map<String, Object> resultMap = TranDataCenter.getInstance()
				.getCurrInDetail();
		// 根据卡类型 获取的数据字段不一样
		// 如果是长城信用卡
		String currencyCode = null;
		String availableBalance = null;
		if (strAccountType.equals(ConstantGloble.ACC_TYPE_GRE)
				|| strAccountType.equals(ConstantGloble.SINGLEWAIBI) // 单外币信用卡
				|| strAccountType.equals(ConstantGloble.ZHONGYIN)) { // 中银信用卡
			Map<String, String> detailMap = new HashMap<String, String>();
			List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
			detailList = (List<Map<String, String>>) resultMap
					.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
			detailMap = detailList.get(0);
			if (!StringUtil.isNullOrEmpty(otherPartJumpFlag)// 如果是从其他模块跳过来的
					// 信用卡购汇还款
					&& otherPartJumpFlag == ConstantGloble.REL_CRCD_BUY) {
				currencyCode = crcdCurrency2;
				ll_gone_tran.setVisibility(View.INVISIBLE);
			} else if (!StringUtil.isNullOrEmpty(otherPartJumpFlag)
					&& otherPartJumpFlag == ConstantGloble.REL_CRCD_REPAY) {
				// 信用卡还款
				currencyCode = ConstantGloble.PRMS_CODE_RMB;
				ll_gone_tran.setVisibility(View.INVISIBLE);
			} else {
				if (strAccountType.equals(ConstantGloble.SINGLEWAIBI)) {
					currencyCode = inCrcdCurrency1;
				} else {
					currencyCode = ConstantGloble.PRMS_CODE_RMB;
				}
			}
			availableBalance = (String) detailMap
					.get(Crcd.CRCD_LOANBALANCELIMIT);
			String flag = (String) detailMap.get(Crcd.LOANBALANCELIMITFLAG);
			flag = flagConvert(flag);
			currencyCodeTv.setText(LocalData.Currency.get(currencyCode));
			cashTv.setText(flag
					+ StringUtil.parseStringCodePattern(currencyCode,
							availableBalance, 2));

		} else {

			if (!inQueryDetailIntercept) {
				@SuppressWarnings("unchecked")
				List<Map<String, String>> detialList = (List<Map<String, String>>) resultMap
						.get(ConstantGloble.ACC_DETAILIST);
				if (StringUtil.isNullOrEmpty(detialList)) {
					return;
				}

				outCurrencyAndBalance = new ArrayList<Map<String, String>>();
				// 显示人名币的那个数据
				for (int i = 0; i < detialList.size(); i++) {
					Map<String, String> map = detialList.get(i);
					// 存放对应的 币种 和 余额
					Map<String, String> map1 = new HashMap<String, String>();
					String currency = map.get(Comm.CURRENCYCODE);
					String Balance = map.get(Dept.AVAILABLE_BALANCE);
					String cashRemit = map.get(Dept.DEPT_CASHREMIT_RES);
					map1.put("currency", currency);
					map1.put("Balance", Balance);
					map1.put("cashRemit", cashRemit);
					outCurrencyAndBalance.add(map1);
					if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {
						currencyCode = currency;
						availableBalance = map.get(Dept.AVAILABLE_BALANCE);
					}
				}
				if (StringUtil.isNullOrEmpty(currencyCode)) {
					currencyCode = (String) detialList.get(0).get(
							Comm.CURRENCYCODE);
					// 可用余额
					availableBalance = detialList.get(0).get(
							Dept.AVAILABLE_BALANCE);
				}
				currencyCodeTv.setText(LocalData.Currency.get(currencyCode));
				cashTv.setText(StringUtil.parseStringCodePattern(currencyCode,
						availableBalance, 2));
			} else {
				setInBalanceView(cashTv);
			}
		}

		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		Map<String, Object> accInInfoMap1 = TranDataCenter.getInstance()
				.getAccInInfoMap();

		// 转出账户类型
		String strAccountTypeOut = (String) accOutInfoMap
				.get(Comm.ACCOUNT_TYPE);
		// 转入账户类型
		String strAccountTypeIn = (String) accInInfoMap1.get(Comm.ACCOUNT_TYPE);

		int height = mAccInLlHeight;
		boolean restore = true;// 时候需恢复默认转入账户控件高度
		if (ConstantGloble.ACC_TYPE_BRO.equals(strAccountTypeOut)) {
			// 转出账户 是 电子借记卡 转入是信用卡
			if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountTypeIn)
					|| ConstantGloble.SINGLEWAIBI.equals(strAccountTypeIn)
					|| ConstantGloble.ZHONGYIN.equals(strAccountTypeIn)) {

				for (int i = 0; i < inCreditCurrencyAndBalance.size(); i++) {

					for (int j = 0; j < outCurrencyAndBalance.size(); j++) {
						// 如果是交集 那么显示
						if (inCreditCurrencyAndBalance
								.get(i)
								.get("currency")
								.equals(outCurrencyAndBalance.get(j).get(
										"currency"))) {

							if (!tv_crrey_accOut_layout_one.isShown()) {
								tv_crrey_accOut_layout_one
										.setVisibility(View.VISIBLE);
								String flag = inCreditCurrencyAndBalance.get(i)
										.get("loanBalanceLimitFlag");
								flag = flagConvert(flag);
								String Balance = inCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimit");
								String currencyCode1 = inCreditCurrencyAndBalance
										.get(i).get("currency");
								cashTv.setText(flag
										+ StringUtil.parseStringCodePattern(
												currencyCode1, Balance, 2));
								currencyCodeTv.setText(LocalData.Currency
										.get(currencyCode1));
								continue;
							} else if (!linerlayoutTwo.isShown()) {
								linerlayoutTwo.setVisibility(View.VISIBLE);

								height = mAccInLlHeight + mAccInLlHeight / 5;
								String currencyCodeCard = inCreditCurrencyAndBalance
										.get(i).get("currency");
								tv_currey_accOut_list_detail_two
										.setText(LocalData.Currency
												.get(currencyCodeCard));
								String flag = inCreditCurrencyAndBalance.get(i)
										.get("loanBalanceLimitFlag");
								flag = flagConvert(flag);
								String Balance = inCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimit");

								tv_lastprice_accOut_list_detail_two
										.setText(flag
												+ StringUtil
														.parseStringCodePattern(
																currencyCodeCard,
																Balance, 2));
							}

						}
					}
				}

				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
						mAccInLl.getMeasuredWidth(), height);
				mAccInLl.setLayoutParams(ll);
				restore = false;

			}
		}

		// 转入账户是电子借记卡 转出账户信用卡
		if (ConstantGloble.ACC_TYPE_BRO.equals(strAccountTypeIn)
				&& !inQueryDetailIntercept) {
			if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountTypeOut)
					|| ConstantGloble.SINGLEWAIBI.equals(strAccountTypeOut)// 601
																			// 增加107
					|| ConstantGloble.ZHONGYIN.equals(strAccountTypeOut)) {

				for (int i = 0; i < outCurrencyAndBalance.size(); i++) {

					for (int j = 0; j < outCreditCurrencyAndBalance.size(); j++) {
						// 如果是交集 那么显示
						if (outCurrencyAndBalance
								.get(i)
								.get("currency")
								.equals(outCreditCurrencyAndBalance.get(j).get(
										"currency"))) {

							if (!tv_crrey_accOut_layout_one.isShown()) {
								tv_crrey_accOut_layout_one
										.setVisibility(View.VISIBLE);
								String flag = outCurrencyAndBalance.get(i).get(
										"cashRemit");
								flag = flagConvertRemitFlag(flag);
								String currencyCodeDebit = outCurrencyAndBalance
										.get(i).get("currency");
								String Balance = outCurrencyAndBalance.get(i)
										.get("Balance");
								currencyCodeTv.setText(LocalData.Currency
										.get(currencyCodeDebit) + flag);
								cashTv.setText(StringUtil
										.parseStringCodePattern(
												currencyCodeDebit, Balance, 2));
								continue;
							} else if (!linerlayoutTwo.isShown()) {
								height = mAccInLlHeight + mAccInLlHeight / 5;
								linerlayoutTwo.setVisibility(View.VISIBLE);
								String flag = outCurrencyAndBalance.get(i).get(
										"cashRemit");
								flag = flagConvertRemitFlag(flag);
								String currencyCodeDebit = outCurrencyAndBalance
										.get(i).get("currency");
								String Balance = outCurrencyAndBalance.get(i)
										.get("Balance");
								tv_currey_accOut_list_detail_two
										.setText(LocalData.Currency
												.get(currencyCodeDebit) + flag);
								tv_lastprice_accOut_list_detail_two
										.setText(StringUtil
												.parseStringCodePattern(
														currencyCodeDebit,
														Balance, 2));
								continue;
							} else if (!linerlayoutThree.isShown()) {
								height = mAccInLlHeight + mAccInLlHeight / 2;
								linerlayoutThree.setVisibility(View.VISIBLE);
								String flag = outCurrencyAndBalance.get(i).get(
										"cashRemit");
								flag = flagConvertRemitFlag(flag);
								String currencyCodeDebit = outCurrencyAndBalance
										.get(i).get("currency");
								tv_currey_accOut_list_detail_three
										.setText(LocalData.Currency
												.get(currencyCodeDebit) + flag);
								String Balance = outCurrencyAndBalance.get(i)
										.get("Balance");
								tv_lastprice_accOut_list_detail_three
										.setText(StringUtil
												.parseStringCodePattern(
														currencyCodeDebit,
														Balance, 2));

							}

						}
					}
				}

				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
						mAccInLl.getMeasuredWidth(), height);
				mAccInLl.setLayoutParams(ll);
				restore = false;

			}

		}

		// 转入账户是信用卡 转出 账户信用卡
		if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountTypeIn)
				|| ConstantGloble.SINGLEWAIBI.equals(strAccountTypeIn)
				|| ConstantGloble.ZHONGYIN.equals(strAccountTypeIn)) {
			if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountTypeOut)
					|| ConstantGloble.SINGLEWAIBI.equals(strAccountTypeIn)// 601怎加
																			// 107
					|| ConstantGloble.ZHONGYIN.equals(strAccountTypeOut)) {
				for (int i = 0; i < inCreditCurrencyAndBalance.size(); i++) {

					for (int j = 0; j < outCreditCurrencyAndBalance.size(); j++) {
						// 如果是交集 那么显示
						if (inCreditCurrencyAndBalance
								.get(i)
								.get("currency")
								.equals(outCreditCurrencyAndBalance.get(j).get(
										"currency"))) {
							// intersection.add(inCreditCurrencyAndBalance.get(i).get("currency"));
							// 如果是人名币 已经显示过了 过滤掉
							if (!tv_crrey_accOut_layout_one.isShown()) {
								tv_crrey_accOut_layout_one
										.setVisibility(View.VISIBLE);
								String flag = inCreditCurrencyAndBalance.get(i)
										.get("loanBalanceLimitFlag");
								flag = flagConvert(flag);
								String currencyCode1 = inCreditCurrencyAndBalance
										.get(i).get("currency");
								String Balance = inCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimit");
								cashTv.setText(flag
										+ StringUtil.parseStringCodePattern(
												currencyCode1, Balance, 2));
								currencyCodeTv.setText((LocalData.Currency
										.get(currencyCode1)));
								continue;
							} else if (!linerlayoutTwo.isShown()) {
								height = mAccInLlHeight + mAccInLlHeight / 5;
								linerlayoutTwo.setVisibility(View.VISIBLE);
								String currencyCode1 = inCreditCurrencyAndBalance
										.get(i).get("currency");
								tv_currey_accOut_list_detail_two
										.setText(LocalData.Currency
												.get(currencyCode1));
								String flag = inCreditCurrencyAndBalance.get(i)
										.get("loanBalanceLimitFlag");
								flag = flagConvert(flag);
								String Balance = inCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimit");
								tv_lastprice_accOut_list_detail_two
										.setText(flag
												+ StringUtil
														.parseStringCodePattern(
																currencyCode1,
																Balance, 2));
							}
						}
					}
				}

				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
						mAccInLl.getMeasuredWidth(), height);
				mAccInLl.setLayoutParams(ll);
				restore = false;

			}
		}

		// 是否需要回复默认高度
		if (restore) {
			tv_crrey_accOut_layout_one.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
					mAccInLl.getMeasuredWidth(), mAccInLlHeight);
			mAccInLl.setLayoutParams(ll);
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

	private String flagConvertRemitFlag(String flag) {
		if ("00".equals(flag)) {
			flag = "";
		} else if ("01".equals(flag)) {
			flag = "现钞";
		} else if ("02".equals(flag)) {
			flag = "现汇";
		}
		return flag;
	}

	private void setInBalanceView(TextView textview) {
		if (!StringUtil.isNullOrEmpty(textview)) {
			textview.setText("无余额");
		}
	}

	/**
	 * 显示转入账户条目的详细信息
	 */
	private void showAccInListItemData(View view) {
		Map<String, Object> accInInfoMap;
		Intent intent = getIntent();
		if (intent != null) {
			otherPartJumpFlag = intent.getIntExtra(
					ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
		}
		if (StringUtil.isNullOrEmpty(otherPartJumpFlag)) {
			return;
		}
		if (otherPartJumpFlag == 0
				|| otherPartJumpFlag == ConstantGloble.ACC_TO_TRAN
				|| otherPartJumpFlag == ConstantGloble.ACC_TO_TRAN_CRCD) {
			accInInfoMap = addInforMap;
		} else {
			// 其他模块
			accInInfoMap = TranDataCenter.getInstance().getAccInInfoMap();
		}
		if (accInInfoMap == null) {
			return;
		}
		// intersection = new ArrayList<String>();
		LinearLayout tv_crrey_accOut_layout_one = (LinearLayout) view
				.findViewById(R.id.tv_crrey_accOut_layout_one);
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

		LinearLayout linerlayoutTwo = (LinearLayout) view
				.findViewById(R.id.tv_crrey_accOut_layout_two);
		TextView tv_currey_accOut_list_detail_two = (TextView) view
				.findViewById(R.id.tv_currey_accOut_list_detail_two);
		TextView tv_lastprice_accOut_list_detail_two = (TextView) view
				.findViewById(R.id.tv_lastprice_accOut_list_detail_two);

		LinearLayout linerlayoutThree = (LinearLayout) view
				.findViewById(R.id.tv_crrey_accOut_layout_three);
		TextView tv_currey_accOut_list_detail_three = (TextView) view
				.findViewById(R.id.tv_currey_accOut_list_detail_three);
		TextView tv_lastprice_accOut_list_detail_three = (TextView) view
				.findViewById(R.id.tv_lastprice_accOut_list_detail_three);

		tv_crrey_accOut_layout_one.setVisibility(View.GONE);
		linerlayoutTwo.setVisibility(View.GONE);
		linerlayoutThree.setVisibility(View.GONE);
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
		LinearLayout ll_gone_tran = (LinearLayout) findViewById(R.id.ll_gone_tran);
		Map<String, Object> resultMap = TranDataCenter.getInstance()
				.getCurrInDetail();
		// 根据卡类型 获取的数据字段不一样
		// 如果是长城信用卡
		String currencyCode = null;
		String availableBalance = null;
		if (strAccountType.equals(ConstantGloble.ACC_TYPE_GRE)
				|| strAccountType.equals(ConstantGloble.SINGLEWAIBI) // 单外币信用卡
				|| strAccountType.equals(ConstantGloble.ZHONGYIN)) { // 中银信用卡
			Map<String, String> detailMap = new HashMap<String, String>();
			List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
			detailList = (List<Map<String, String>>) resultMap
					.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
			detailMap = detailList.get(0);
			if (!StringUtil.isNullOrEmpty(otherPartJumpFlag)// 如果是从其他模块跳过来的
					// 信用卡购汇还款
					&& otherPartJumpFlag == ConstantGloble.REL_CRCD_BUY) {
				currencyCode = crcdCurrency2;
				ll_gone_tran.setVisibility(View.INVISIBLE);
			} else if (!StringUtil.isNullOrEmpty(otherPartJumpFlag)
					&& otherPartJumpFlag == ConstantGloble.REL_CRCD_REPAY) {
				// 信用卡还款
				currencyCode = ConstantGloble.PRMS_CODE_RMB;
				ll_gone_tran.setVisibility(View.INVISIBLE);
			} else {
				if (strAccountType.equals(ConstantGloble.SINGLEWAIBI)) {
					currencyCode = inCrcdCurrency1;
				} else {
					currencyCode = ConstantGloble.PRMS_CODE_RMB;
				}
			}
			availableBalance = (String) detailMap
					.get(Crcd.CRCD_LOANBALANCELIMIT);
			String flag = (String) detailMap.get(Crcd.LOANBALANCELIMITFLAG);
			flag = flagConvert(flag);
			currencyCodeTv.setText(LocalData.Currency.get(currencyCode));
			cashTv.setText(flag
					+ StringUtil.parseStringCodePattern(currencyCode,
							availableBalance, 2));

		} else {
			if (!inQueryDetailIntercept) {
				// 查询转入账户余额 没有异常 不需要拦截
				@SuppressWarnings("unchecked")
				List<Map<String, String>> detialList = (List<Map<String, String>>) resultMap
						.get(ConstantGloble.ACC_DETAILIST);
				if (StringUtil.isNullOrEmpty(detialList)) {
					return;
				}

				outCurrencyAndBalance = new ArrayList<Map<String, String>>();
				// 显示人名币的那个数据
				for (int i = 0; i < detialList.size(); i++) {
					Map<String, String> map = detialList.get(i);
					// 存放对应的 币种 和 余额
					Map<String, String> map1 = new HashMap<String, String>();
					String currency = map.get(Comm.CURRENCYCODE);
					String Balance = map.get(Dept.AVAILABLE_BALANCE);
					String cashRemit = map.get(Dept.DEPT_CASHREMIT_RES);
					map1.put("currency", currency);
					map1.put("Balance", Balance);
					map1.put("cashRemit", cashRemit);
					outCurrencyAndBalance.add(map1);
					if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {
						currencyCode = currency;
						availableBalance = map.get(Dept.AVAILABLE_BALANCE);
					}
				}
				if (StringUtil.isNullOrEmpty(currencyCode)) {
					currencyCode = (String) detialList.get(0).get(
							Comm.CURRENCYCODE);
					// 可用余额
					availableBalance = detialList.get(0).get(
							Dept.AVAILABLE_BALANCE);
				}
				currencyCodeTv.setText(LocalData.Currency.get(currencyCode));
				cashTv.setText(StringUtil.parseStringCodePattern(currencyCode,
						availableBalance, 2));
			} else {
				// 查询转入账户 有异常 只显示无余额
				setInBalanceView(cashTv);
			}
		}

		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		Map<String, Object> accInInfoMap1 = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String strAccountTypeOut = "";
		if (!StringUtil.isNullOrEmpty(accOutInfoMap)) {
			// 转出账户类型
			strAccountTypeOut = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
		}
		// 转入账户类型
		String strAccountTypeIn = (String) accInInfoMap1.get(Comm.ACCOUNT_TYPE);

		int height = mAccInLlHeight;
		boolean restore = true;// 时候需恢复默认转入账户控件高度
		if (ConstantGloble.ACC_TYPE_BRO.equals(strAccountTypeOut)) {
			// 转出账户 是 电子借记卡 转入是信用卡
			if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountTypeIn)
					|| ConstantGloble.ZHONGYIN.equals(strAccountTypeIn)) {

				for (int i = 0; i < inCreditCurrencyAndBalance.size(); i++) {

					for (int j = 0; j < outCurrencyAndBalance.size(); j++) {
						// 如果是交集 那么显示
						if (inCreditCurrencyAndBalance
								.get(i)
								.get("currency")
								.equals(outCurrencyAndBalance.get(j).get(
										"currency"))) {
							// 如果是人名币 已经显示过了 过滤掉
							if (!tv_crrey_accOut_layout_one.isShown()) {
								tv_crrey_accOut_layout_one
										.setVisibility(View.VISIBLE);
								String flag = inCreditCurrencyAndBalance.get(i)
										.get("loanBalanceLimitFlag");
								flag = flagConvert(flag);
								String Balance = inCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimit");
								String currencyCode1 = inCreditCurrencyAndBalance
										.get(i).get("currency");
								cashTv.setText(flag
										+ StringUtil.parseStringCodePattern(
												currencyCode1, Balance, 2));
								currencyCodeTv.setText(LocalData.Currency
										.get(currencyCode1));
								continue;
							} else if (!linerlayoutTwo.isShown()) {

								height = mAccInLlHeight + mAccInLlHeight / 5;
								linerlayoutTwo.setVisibility(View.VISIBLE);
								String currencyCodeCard = inCreditCurrencyAndBalance
										.get(i).get("currency");
								tv_currey_accOut_list_detail_two
										.setText(LocalData.Currency
												.get(currencyCodeCard));
								String flag = inCreditCurrencyAndBalance.get(i)
										.get("loanBalanceLimitFlag");
								flag = flagConvert(flag);
								String Balance = inCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimit");
								tv_lastprice_accOut_list_detail_two
										.setText(flag
												+ StringUtil
														.parseStringCodePattern(
																currencyCodeCard,
																Balance, 2));
							}

						}
					}
				}

				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
						mAccInLl.getMeasuredWidth(), height);
				mAccInLl.setLayoutParams(ll);
				restore = false;

			}
		}

		// 转入账户是电子借记卡 转出账户信用卡1111
		if (ConstantGloble.ACC_TYPE_BRO.equals(strAccountTypeIn)
				&& !inQueryDetailIntercept) {
			if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountTypeOut)
					|| ConstantGloble.SINGLEWAIBI.equals(strAccountTypeOut)// 601
																			// 增加107
					|| ConstantGloble.ZHONGYIN.equals(strAccountTypeOut)) {

				for (int i = 0; i < outCurrencyAndBalance.size(); i++) {

					for (int j = 0; j < outCreditCurrencyAndBalance.size(); j++) {
						// 如果是交集 那么显示
						if (outCurrencyAndBalance
								.get(i)
								.get("currency")
								.equals(outCreditCurrencyAndBalance.get(j).get(
										"currency"))) {

							if (!tv_crrey_accOut_layout_one.isShown()) {
								tv_crrey_accOut_layout_one
										.setVisibility(View.VISIBLE);
								String flag = outCurrencyAndBalance.get(i).get(
										"cashRemit");
								flag = flagConvertRemitFlag(flag);
								String currencyCode1 = outCurrencyAndBalance
										.get(i).get("currency");
								String Balance = outCurrencyAndBalance.get(i)
										.get("Balance");
								currencyCodeTv.setText(LocalData.Currency
										.get(currencyCode1) + flag);
								cashTv.setText(StringUtil
										.parseStringCodePattern(currencyCode1,
												Balance, 2));
								continue;
							} else if (!linerlayoutTwo.isShown()) {
								height = mAccInLlHeight + mAccInLlHeight / 5;
								linerlayoutTwo.setVisibility(View.VISIBLE);
								String flag = outCurrencyAndBalance.get(i).get(
										"cashRemit");
								flag = flagConvertRemitFlag(flag);
								String currencyCodeDebit = outCurrencyAndBalance
										.get(i).get("currency");
								String Balance = outCurrencyAndBalance.get(i)
										.get("Balance");
								tv_currey_accOut_list_detail_two
										.setText(LocalData.Currency
												.get(currencyCodeDebit) + flag);
								tv_lastprice_accOut_list_detail_two
										.setText(StringUtil
												.parseStringCodePattern(
														currencyCodeDebit,
														Balance, 2));

							} else if (!linerlayoutThree.isShown()) {
								linerlayoutThree.setVisibility(View.VISIBLE);
								height = mAccInLlHeight + mAccInLlHeight / 2;
								String flag = outCurrencyAndBalance.get(i).get(
										"cashRemit");
								flag = flagConvertRemitFlag(flag);
								String currencyCodeDebit = outCurrencyAndBalance
										.get(i).get("currency");
								String Balance = outCurrencyAndBalance.get(i)
										.get("Balance");
								tv_currey_accOut_list_detail_three
										.setText(LocalData.Currency
												.get(currencyCodeDebit) + flag);
								tv_lastprice_accOut_list_detail_three
										.setText(StringUtil
												.parseStringCodePattern(
														currencyCodeDebit,
														Balance, 2));

							}

						}
					}
				}
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
						mAccInLl.getMeasuredWidth(), height);
				mAccInLl.setLayoutParams(ll);
				restore = false;

			}

		}

		// 转入账户是信用卡 转出 账户信用卡
		if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountTypeIn)
				|| ConstantGloble.SINGLEWAIBI.equals(strAccountTypeIn)
				|| ConstantGloble.ZHONGYIN.equals(strAccountTypeIn)) {
			if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountTypeOut)
					|| ConstantGloble.SINGLEWAIBI.equals(strAccountTypeOut)// 601
																			// 增加107
					|| ConstantGloble.ZHONGYIN.equals(strAccountTypeOut)) {
				for (int i = 0; i < inCreditCurrencyAndBalance.size(); i++) {

					for (int j = 0; j < outCreditCurrencyAndBalance.size(); j++) {
						// 如果是交集 那么显示
						if (inCreditCurrencyAndBalance
								.get(i)
								.get("currency")
								.equals(outCreditCurrencyAndBalance.get(j).get(
										"currency"))) {
							if (!tv_crrey_accOut_layout_one.isShown()) {
								tv_crrey_accOut_layout_one
										.setVisibility(View.VISIBLE);
								String flag = inCreditCurrencyAndBalance.get(i)
										.get("loanBalanceLimitFlag");
								flag = flagConvert(flag);
								String Balance = inCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimit");
								String currencyCode1 = inCreditCurrencyAndBalance
										.get(i).get("currency");
								cashTv.setText(flag
										+ StringUtil.parseStringCodePattern(
												currencyCode1, Balance, 2));
								currencyCodeTv.setText(LocalData.Currency
										.get(currencyCode1));
								continue;
							} else if (!linerlayoutTwo.isShown()) {

								height = mAccInLlHeight + mAccInLlHeight / 5;
								linerlayoutTwo.setVisibility(View.VISIBLE);
								String currencyCodeDebit = inCreditCurrencyAndBalance
										.get(i).get("currency");
								tv_currey_accOut_list_detail_two
										.setText(LocalData.Currency
												.get(currencyCodeDebit));
								String flag = inCreditCurrencyAndBalance.get(i)
										.get("loanBalanceLimitFlag");
								flag = flagConvert(flag);
								String Balance = inCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimit");
								tv_lastprice_accOut_list_detail_two
										.setText(flag
												+ StringUtil
														.parseStringCodePattern(
																currencyCodeDebit,
																Balance, 2));
							}

						}
					}
				}

				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
						mAccInLl.getMeasuredWidth(), height);
				mAccInLl.setLayoutParams(ll);
				restore = false;

			}
		}

		// 是否需要回复默认高度
		if (restore) {
			tv_crrey_accOut_layout_one.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
					mAccInLl.getMeasuredWidth(), mAccInLlHeight);
			mAccInLl.setLayoutParams(ll);
		}
		// 默认每次点击完转入账户 都要刷新转出账户 转出账户不为空 刷新
		if (!StringUtil.isNullOrEmpty(strAccountTypeOut)) {
			RefreshAccOutDetailView();
		}

	}

	/** 刷新转出账户信息 */
	private void RefreshAccOutDetailView() {

		BaseDroidApp.getInstanse().dismissMessageDialog();
		accOutDetailLayout = (LinearLayout) inflater.inflate(
				R.layout.dept_acc_out_list_detail, null);

		mAccOutLl.removeAllViews();
		mAccOutLl.addView(accOutDetailLayout);
		mAccOutLl.setVisibility(View.VISIBLE);
		mAccOutListLayout.setVisibility(View.GONE);
		newAddTranOutBtn.setVisibility(View.GONE);
		RefreshshowAccOutListItemData(accOutDetailLayout);// 显示转出账户条目的详细信息
		// 判断是否是其他模块进来的
		Intent intent = getIntent();
		if (intent != null) {
			otherPartJumpFlag = intent.getIntExtra(
					ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
			if (StringUtil.isNullOrEmpty(otherPartJumpFlag)) {
				return;
			}
			if (otherPartJumpFlag == ConstantGloble.REL_CRCD_BUY
					|| otherPartJumpFlag == ConstantGloble.REL_CRCD_REPAY
					|| otherPartJumpFlag == ConstantGloble.ACC_TO_TRAN
					|| otherPartJumpFlag == ConstantGloble.ACC_TO_TRAN_CRCD) {
				mTopRightBtn.setVisibility(View.GONE);
			} else {
				mTopRightBtn.setVisibility(View.VISIBLE);
			}
		} else {
			mTopRightBtn.setVisibility(View.VISIBLE);
		}
		isTranOutFirst = false;
		// T43 选择完转出账户之后 根据类型显示快捷按钮
		// add start
		tran_acc_seach_linear.setVisibility(View.VISIBLE);
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		String accountType = "";
		if (!StringUtil.isNullOrEmpty(accOutInfoMap)) {

			accountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
		}
		boolean iscanmobile = false;
		boolean iscantwodimen = true;
		if (!StringUtil.isNull(accountType)) {
			if (accountType.equals(ConstantGloble.ACC_TYPE_GRE)
					|| accountType.equals(ConstantGloble.ACC_TYPE_BRO)) {
				iscanmobile = true;
			}
			if (accountType.equals(AccBaseActivity.YOUHUITONGZH)
					|| accountType.equals(ConstantGloble.ACC_TYPE_BOCINVT)) {
				iscantwodimen = false;
				tran_acc_seach_linear.setVisibility(View.GONE);
			}
		}
		// 二维码转账
		Button twoDimension = (Button) container
				.findViewById(R.id.btn_twoDimension_acc_in_mytransfer);
		twoDimension.setOnClickListener(twoDimensionListener);
		// 手机号转账
		Button phoneBt = (Button) container
				.findViewById(R.id.btn_phoneNum_acc_in_mytransfer);
		phoneBt.setOnClickListener(phoneListener);
		if (iscanmobile) {
			phoneBt.setVisibility(View.VISIBLE);
		} else {
			phoneBt.setVisibility(View.INVISIBLE);
		}
		if (iscantwodimen) {
			twoDimension.setVisibility(View.VISIBLE);
		} else {
			twoDimension.setVisibility(View.INVISIBLE);
		}
	}

	private void RefreshshowAccOutListItemData(View view) {

		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		if (accOutInfoMap == null) {
			return;
		}
		// intersection = new ArrayList<String>();
		LinearLayout tv_crrey_accOut_layout_one = (LinearLayout) view
				.findViewById(R.id.tv_crrey_accOut_layout_one);
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

		LinearLayout linerlayoutTwo = (LinearLayout) view
				.findViewById(R.id.tv_crrey_accOut_layout_two);
		TextView tv_currey_accOut_list_detail_two = (TextView) view
				.findViewById(R.id.tv_currey_accOut_list_detail_two);
		TextView tv_lastprice_accOut_list_detail_two = (TextView) view
				.findViewById(R.id.tv_lastprice_accOut_list_detail_two);

		LinearLayout linerlayoutThree = (LinearLayout) view
				.findViewById(R.id.tv_crrey_accOut_layout_three);
		TextView tv_currey_accOut_list_detail_three = (TextView) view
				.findViewById(R.id.tv_currey_accOut_list_detail_three);
		TextView tv_lastprice_accOut_list_detail_three = (TextView) view
				.findViewById(R.id.tv_lastprice_accOut_list_detail_three);

		tv_crrey_accOut_layout_one.setVisibility(View.GONE);
		linerlayoutTwo.setVisibility(View.GONE);
		linerlayoutThree.setVisibility(View.GONE);
		// 转出账户类型
		String strAccountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
		// 转入账户类型
		String strAccountTypeIn = "";
		if (!StringUtil.isNullOrEmpty(accInInfoMap)) {
			strAccountTypeIn = (String) accInInfoMap.get(Comm.ACCOUNT_TYPE);

		}

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
		// 根据卡类型 获取的数据字段不一样
		// 如果是长城信用卡
		if (strAccountType.equals(ConstantGloble.ACC_TYPE_GRE)
				|| strAccountType.equals(ConstantGloble.SINGLEWAIBI)// 601 增加107
				|| strAccountType.equals(ConstantGloble.ZHONGYIN)) {
			Map<String, String> detailMap = new HashMap<String, String>();
			List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
			detailList = (List<Map<String, String>>) resultMap
					.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
			detailMap = detailList.get(0);
			availableBalance = (String) detailMap
					.get(Crcd.CRCD_LOANBALANCELIMIT);
			currencyCode = ConstantGloble.BOCINVT_CURRENCY_RMB;
			String flag = (String) detailMap.get(Crcd.CRCD_LOANBALANCELIMIT);
			cashTv.setText(StringUtil.parseStringCodePattern(currencyCode,
					availableBalance, 2));

		} else {
			@SuppressWarnings("unchecked")
			List<Map<String, String>> detialList = (List<Map<String, String>>) resultMap
					.get(ConstantGloble.ACC_DETAILIST);
			if (StringUtil.isNullOrEmpty(detialList)) {
				return;
			}
			currencyCode = null;

			outCurrencyAndBalance = new ArrayList<Map<String, String>>();
			// 显示人名币的那个数据

			for (int i = 0; i < detialList.size(); i++) {
				Map<String, String> map = detialList.get(i);
				// 存放对应的 币种 和 余额
				Map<String, String> map1 = new HashMap<String, String>();
				String currency = map.get(Comm.CURRENCYCODE);
				String Balance = map.get(Dept.AVAILABLE_BALANCE);
				String cashRemit = map.get(Dept.DEPT_CASHREMIT_RES);
				map1.put("currency", currency);
				map1.put("Balance", Balance);
				map1.put("cashRemit", cashRemit);
				outCurrencyAndBalance.add(map1);
				if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {
					currencyCode = currency;
					availableBalance = map.get(Dept.AVAILABLE_BALANCE);
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

		int height = mAccOutLlHeight;
		// 如果需要修改控件高度 那就回复为默认的
		boolean restore = true;

		if (ConstantGloble.ACC_TYPE_BRO.equals(strAccountType)) {
			// 转出账户 是 电子借记卡 转入是信用卡
			if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountTypeIn)
					|| ConstantGloble.SINGLEWAIBI.equals(strAccountTypeIn)
					|| ConstantGloble.ZHONGYIN.equals(strAccountTypeIn)) {
				for (int i = 0; i < outCurrencyAndBalance.size(); i++) {

					for (int j = 0; j < inCreditCurrencyAndBalance.size(); j++) {

						// 如果是交集 那么显示
						if (outCurrencyAndBalance
								.get(i)
								.get("currency")
								.equals(inCreditCurrencyAndBalance.get(j).get(
										"currency"))) {
							if (!tv_crrey_accOut_layout_one.isShown()) {
								tv_crrey_accOut_layout_one
										.setVisibility(View.VISIBLE);
								String flag = outCurrencyAndBalance.get(i).get(
										"cashRemit");
								flag = flagConvertRemitFlag(flag);
								String currencyCode = outCurrencyAndBalance
										.get(i).get("currency");
								String Balance = outCurrencyAndBalance.get(i)
										.get("Balance");
								currencyCodeTv.setText(LocalData.Currency
										.get(currencyCode) + flag);
								cashTv.setText(StringUtil
										.parseStringCodePattern(currencyCode,
												Balance, 2));
								continue;
							} else if (!linerlayoutTwo.isShown()) {
								height = mAccOutLlHeight + mAccOutLlHeight / 5;
								linerlayoutTwo.setVisibility(View.VISIBLE);
								String flag = outCurrencyAndBalance.get(i).get(
										"cashRemit");
								flag = flagConvertRemitFlag(flag);
								String currencyCode = outCurrencyAndBalance
										.get(i).get("currency");
								String Balance = outCurrencyAndBalance.get(i)
										.get("Balance");
								tv_currey_accOut_list_detail_two
										.setText(LocalData.Currency
												.get(currencyCode) + flag);
								tv_lastprice_accOut_list_detail_two
										.setText(StringUtil
												.parseStringCodePattern(
														currencyCode, Balance,
														2));
								continue;
							} else if (!linerlayoutThree.isShown()) {
								linerlayoutThree.setVisibility(View.VISIBLE);
								height = mAccOutLlHeight + mAccOutLlHeight / 2;
								String flag = outCurrencyAndBalance.get(i).get(
										"cashRemit");
								flag = flagConvertRemitFlag(flag);
								String currencyCode = outCurrencyAndBalance
										.get(i).get("currency");
								String Balance = outCurrencyAndBalance.get(i)
										.get("Balance");
								tv_currey_accOut_list_detail_three
										.setText(LocalData.Currency
												.get(currencyCode) + flag);
								tv_lastprice_accOut_list_detail_three
										.setText(StringUtil
												.parseStringCodePattern(
														currencyCode, Balance,
														2));

							}

						}
					}
				}

				mAccOutLl.getMeasuredWidth();
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
						mAccOutLl.getMeasuredWidth(), height);
				mAccOutLl.setLayoutParams(ll);
				restore = false;
			}

		}

		// 转入账户是电子借记卡 转出账户信用卡
		if (ConstantGloble.ACC_TYPE_BRO.equals(strAccountTypeIn)
				&& !inQueryDetailIntercept) {
			if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountType)
					|| ConstantGloble.SINGLEWAIBI.equals(strAccountType)// 601
																		// 增加
																		// 107
					|| ConstantGloble.ZHONGYIN.equals(strAccountType)) {

				for (int i = 0; i < outCreditCurrencyAndBalance.size(); i++) {

					// for (int j = 0; j < outCurrencyAndBalance.size(); j++) {
					// // 如果是交集 那么显示
					// if (outCreditCurrencyAndBalance
					// .get(i)
					// .get("currency")
					// .equals(outCurrencyAndBalance.get(j).get(
					// "currency"))) {
					if (!tv_crrey_accOut_layout_one.isShown()) {
						tv_crrey_accOut_layout_one.setVisibility(View.VISIBLE);
						String flag = outCreditCurrencyAndBalance.get(i).get(
								"loanBalanceLimitFlag");
						flag = flagConvert(flag);
						String Balance = outCreditCurrencyAndBalance.get(i)
								.get("loanBalanceLimit");
						String currencyCode = outCreditCurrencyAndBalance
								.get(i).get("currency");

						currencyCodeTv.setText(LocalData.Currency
								.get(currencyCode));
						cashTv.setText(flag
								+ StringUtil.parseStringCodePattern(
										currencyCode, Balance, 2));
						continue;
					} else if (!linerlayoutTwo.isShown()) {

						height = mAccOutLlHeight + mAccOutLlHeight / 5;
						linerlayoutTwo.setVisibility(View.VISIBLE);
						String currencyCode = outCreditCurrencyAndBalance
								.get(i).get("currency");
						tv_currey_accOut_list_detail_two
								.setText(LocalData.Currency.get(currencyCode));
						String flag = outCreditCurrencyAndBalance.get(i).get(
								"loanBalanceLimitFlag");
						flag = flagConvert(flag);
						String Balance = outCreditCurrencyAndBalance.get(i)
								.get("loanBalanceLimit");

						tv_lastprice_accOut_list_detail_two.setText(flag
								+ StringUtil.parseStringCodePattern(
										currencyCode, Balance, 2));
					}

					// }
					// }
				}

				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
						mAccOutLl.getMeasuredWidth(), height);
				mAccOutLl.setLayoutParams(ll);
				restore = false;

			}
		}

		// 转入账户是信用卡 转出 账户信用卡
		if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountTypeIn)
				|| ConstantGloble.ZHONGYIN.equals(strAccountTypeIn)
				|| ConstantGloble.SINGLEWAIBI.equals(strAccountTypeIn)) {
			if (ConstantGloble.ACC_TYPE_GRE.equals(strAccountType)
					|| ConstantGloble.SINGLEWAIBI.equals(strAccountType)// 601
																		// 增加
																		// 107
					|| ConstantGloble.ZHONGYIN.equals(strAccountType)) {

				for (int i = 0; i < outCreditCurrencyAndBalance.size(); i++) {

					for (int j = 0; j < inCreditCurrencyAndBalance.size(); j++) {
						// 如果是交集 那么显示
						if (outCreditCurrencyAndBalance
								.get(i)
								.get("currency")
								.equals(inCreditCurrencyAndBalance.get(j).get(
										"currency"))) {
							// intersection.add(outCreditCurrencyAndBalance.get(i).get("currency"));
							// 如果是人名币 已经显示过了 过滤掉
							if (!tv_crrey_accOut_layout_one.isShown()) {
								tv_crrey_accOut_layout_one
										.setVisibility(View.VISIBLE);
								String flag = outCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimitFlag");
								flag = flagConvert(flag);
								String Balance = outCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimit");
								String currencyCode = outCreditCurrencyAndBalance
										.get(i).get("currency");
								cashTv.setText(flag
										+ StringUtil.parseStringCodePattern(
												currencyCode, Balance, 2));
								currencyCodeTv.setText(LocalData.Currency
										.get(currencyCode));
								continue;
							} else if (!linerlayoutTwo.isShown()) {
								height = mAccOutLlHeight + mAccOutLlHeight / 5;
								linerlayoutTwo.setVisibility(View.VISIBLE);
								String currencyCode = outCreditCurrencyAndBalance
										.get(i).get("currency");
								tv_currey_accOut_list_detail_two
										.setText(LocalData.Currency
												.get(currencyCode));
								String flag = outCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimitFlag");
								flag = flagConvert(flag);
								String Balance = outCreditCurrencyAndBalance
										.get(i).get("loanBalanceLimit");
								tv_lastprice_accOut_list_detail_two
										.setText(flag
												+ StringUtil
														.parseStringCodePattern(
																currencyCode,
																Balance, 2));

							}

						}
					}
				}

				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
						mAccOutLl.getMeasuredWidth(), height);
				mAccOutLl.setLayoutParams(ll);
				restore = false;

			}
		}

		// 是否需要回复默认高度
		if (restore) {
			tv_crrey_accOut_layout_one.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
					mAccOutLl.getMeasuredWidth(), mAccOutLlHeight);
			mAccOutLl.setLayoutParams(ll);
		}

	}

	/**
	 * 
	 * 
	 * 收款人列表框
	 */
	private void showAccInListDialog() {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		// 组装需要数据 关联账户
		List<Map<String, Object>> relAccList = new ArrayList<Map<String, Object>>();
		relAccList.addAll(TranDataCenter.getInstance().getAccountOutList());
		// 根据accountId和accountNumber 去除选择项
		for (int i = 0; i < relAccList.size(); i++) {
			Map<String, Object> map = relAccList.get(i);
			if (map.get(Comm.ACCOUNT_ID).equals(
					accOutInfoMap.get(Comm.ACCOUNT_ID))
					&& map.get(Comm.ACCOUNTNUMBER).equals(
							accOutInfoMap.get(Comm.ACCOUNTNUMBER))) {
				relAccList.remove(i);
			}
		}

		// 如果转出账户是101和188 转入账户不能为信用卡
		String accountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
		List<Map<String, Object>> newList = null;
		if (accountType.equals(ConstantGloble.ACC_TYPE_ORD)
				|| accountType.equals(ConstantGloble.ACC_TYPE_RAN)) {
			newList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < relAccList.size(); i++) {
				Map<String, Object> map = relAccList.get(i);
				String strAccType = (String) map.get(Comm.ACCOUNT_TYPE);
				if (strAccType.equals(ConstantGloble.ACC_TYPE_GRE)
						|| strAccType.equals(ConstantGloble.ZHONGYIN)
						|| strAccType.equals(ConstantGloble.SINGLEWAIBI)) {
					newList.add(map);
				}
			}
		} else if (accountType.equals(AccBaseActivity.YOUHUITONGZH)
		// 403新增199优汇通专户，优汇通专户只能作为关联转账的转出账户
		) {

			newList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < relAccList.size(); i++) {
				Map<String, Object> map = relAccList.get(i);
				String strAccType = (String) map.get(Comm.ACCOUNT_TYPE);
				if (strAccType.equals(ConstantGloble.ACC_TYPE_GRE)
						|| strAccType.equals(ConstantGloble.ZHONGYIN)
						|| strAccType.equals(ConstantGloble.SINGLEWAIBI)) {
					newList.add(map);
				}
			}
		} else {

			// 中银信用卡、单外币信用卡
			List<Map<String, Object>> commCrcdList = TranDataCenter
					.getInstance().getCrcdList();
			relAccList.addAll(commCrcdList);
		}
		// 网上专属理财
		List<Map<String, Object>> bocinvtList = TranDataCenter.getInstance()
				.getBocinvtList();
		relAccList.addAll(bocinvtList);
		if (!StringUtil.isNullOrEmpty(newList)) {
			relAccList.removeAll(newList);
		}
		accInListData.clear();
		accInListData.addAll(relAccList);
		if (accountType.equals(AccBaseActivity.YOUHUITONGZH)

		// 403新增199优汇通专户，优汇通专户只能作为关联转账的转出账户
				|| accountType.equals(ConstantGloble.SINGLEWAIBI)) {
			// 601新增107，单外币只能作为关联转账的转出账户
		} else {

			// p601 长城信用卡 104 支持 跨行普通 定向转账
			// p601 长城信用卡 103 不支持 跨行普通 定向转账
			// 常用收款人(非关联)
			if (accountType.equals(ConstantGloble.ZHONGYIN)) {
				// 长城信用卡只能做行内，跨行的实时转账
				// 行内 1 跨行0 非关联信用卡 2
				List<Map<String, Object>> list = TranDataCenter.getInstance()
						.getCommAccInList();
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					String bocFlag = (String) map.get(Tran.TRANS_BOCFLAG_REQ);
					if (bocFlag.equals("0")) {
						// 跨行
					} else if (bocFlag.equals("1") || bocFlag.equals("2")) {
						// 行内
						accInListData.add(map);
					}
				}
				// 定向收款人
				List<Map<String, Object>> dirList = TranDataCenter
						.getInstance().getDirpayeeList();
				for (int i = 0; i < dirList.size(); i++) {
					Map<String, Object> map = dirList.get(i);
					String bocFlag = (String) map.get(Tran.TRANS_BOCFLAG_REQ);
					if (bocFlag.equals("0")) {
						// 跨行
					} else if (bocFlag.equals("1") || bocFlag.equals("2")) {
						// 行内
						accInListData.add(map);
					}
				}

			} else {
				List<Map<String, Object>> commAccInList = TranDataCenter
						.getInstance().getCommAccInList();
				accInListData.addAll(commAccInList);
				// 定向收款人
				List<Map<String, Object>> dirpayeeList = TranDataCenter
						.getInstance().getDirpayeeList();
				accInListData.addAll(dirpayeeList);
			}
			// 实时定向
			List<Map<String, Object>> ebpsDirList = TranDataCenter
					.getInstance().getEbpsDirList();
			accInListData.addAll(ebpsDirList);
			// 实时普通
			List<Map<String, Object>> ebpsList = TranDataCenter.getInstance()
					.getEbpsList();
			accInListData.addAll(ebpsList);
		}

		if (StringUtil.isNullOrEmpty(accInListData)) {// 如果没有符合要求的数据 弹框提示
			// 提示信息
			if (accountType.equals(AccBaseActivity.YOUHUITONGZH)) {
				String message = this.getString(R.string.query_no_fit_in);
				BaseDroidApp.getInstanse().showInfoMessageDialog(message);
				return;

			} else {
				String message = this.getString(R.string.no_accin);
				BaseDroidApp.getInstanse().showErrorDialog(message,
						R.string.cancle, R.string.confirm, onclicklistener);
				return;
			}
		}
		accInListView = new ListView(context);
		accInListView.setScrollingCacheEnabled(false);
		accInListView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		accInListView.setBackgroundColor(Color.WHITE);

		accInListView.setFadingEdgeLength(0);
		inadapter = new AccInListAdapter(context, accInListData);
		inadapter.setRelAccList(relAccList);
		inadapter.setDirpayeeList(TranDataCenter.getInstance()
				.getDirpayeeList());
		inadapter.setEbpsDirList(TranDataCenter.getInstance().getEbpsDirList());
		inadapter.setEbpsList(TranDataCenter.getInstance().getEbpsList());
		accInListView.setAdapter(inadapter);
		accInListView.setOnItemClickListener(clickAccInListItem);
		boolean isshowtoprightBtn = true;
		if (!StringUtil.isNull(accountType)) {
			if (accountType.equals(AccBaseActivity.YOUHUITONGZH)) {
				isshowtoprightBtn = false;
			}
		}
		// 弹出转入转换列表框
		BaseDroidApp.getInstanse().showTransAccInListDialog(isshowtoprightBtn,
				accInListView, backListener, addAccInNoClicklistener,
				textWatcher);
	}

	/**
	 * 您目前暂无收款人 添加新收款人
	 */
	private OnClickListener onclicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_CANCLE:// 取消
				BaseDroidApp.getInstanse().dismissErrorDialog();
				break;
			case CustomDialog.TAG_SURE:// 确定
				BaseDroidApp.getInstanse().dismissErrorDialog();
				// TODO 如果转出账户为长城信用卡，只能新增实时和行内账户
				Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
						.getAccOutInfoMap();
				// 转出账户类型 为单外币 不支持新增收款人
				String outAccountType = (String) accOutInfoMap
						.get(Comm.ACCOUNT_TYPE);
				if (ConstantGloble.SINGLEWAIBI.equals(outAccountType)) {
					String errorInfo = getResources().getString(
							R.string.out_singlewaibi_error);
					BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);

				} else {
					goAddAcc();
				}
				break;
			default:
				break;
			}

		}
	};

	/**
	 * 转入账户列表ListView的监听事件
	 */
	private OnItemClickListener clickAccInListItem = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			outOrInFlag = IN;
			inQueryDetailIntercept = false;// 选择转入账户的 时候 重置标识
			isOpenFlag = false;// 全球交易人民币记账功能 重置
			addNewPayeeFlag = false;// 点击收款人列表的时候 重置新增收款人flag
			isSendSmc = ConstantGloble.FALSE;// 重置短信通知收款人flag
			isSavePayee = ConstantGloble.FALSE;// 重置保存为常用收款人flag
			isAddPayeeChecked = ConstantGloble.FALSE;// 重置新增收款人flag
			Map<String, Object> accInInfoMap = (Map<String, Object>) inadapter
					.getItem(position);
			// 存储转出账户信息
			// 分为三种情况 1.如果是长城信用卡（查询信用卡详情接口）
			// 2.如果是其他卡(查询详情 普通详情接口)
			// 3.常用收款人(不需要查详情 没有币种 余额字段显示)
			// 根据账户类型 查询不同接口
			addInforMap = accInInfoMap;
			// 判断是关联和非关联
			if (TranDataCenter.getInstance().getCommAccInList()
					.contains(accInInfoMap)) {// 如果是常用收款人(非关联)
				// 判断是否有开户行和所属银行
				TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);

				BaseDroidApp.getInstanse().dismissMessageDialog();

				// 添加一个新的布局文件--转入账户详细信息
				accInDetailLayout = (LinearLayout) inflater.inflate(
						R.layout.dept_acc_in_list_detail, null);
				mAccInLl.removeAllViews();
				mAccInLl.addView(accInDetailLayout);
				mAccInLl.setVisibility(View.VISIBLE);
				// mAccInListLayout.setVisibility(View.GONE);
				newAddTranInBtn.setVisibility(View.GONE);
				showAccInListItemData();// 显示转出账户条目的详细信息
				isTranInFirst = false;
				// 根据bocflag 判断 行内 1 跨行0 非关联信用卡 2
				String bocFlag = (String) accInInfoMap
						.get(Tran.TRANS_BOCFLAG_REQ);
				String type = (String) accInInfoMap.get(Tran.TRANS_TYPE_RES);
				if (StringUtil.isNullOrEmpty(bocFlag)) {
					return;
				}
				if (bocFlag.equals("0")) {
					tranTypeFlag = TRANTYPE_NOREL_BANKOTHER;
					// initBankOtherView();
					// 普通跨行转账
					initBankOtherCommonView();
				} else if (bocFlag.equals("1")) {
					if (!StringUtil.isNullOrEmpty(type)) {
						if (type.equals(ConstantGloble.ZHONGYIN)
								|| type.equals(ConstantGloble.SINGLEWAIBI)
								|| type.equals(ConstantGloble.ACC_TYPE_GRE)) {
							// 非关联信用卡还款流程
							tranTypeFlag = TRANTYPE_NOREL_CRCD;
							initNoRelView();
						} else {
							tranTypeFlag = TRANTYPE_NOREL_BANKIN;
							initBankInView();
						}
					} else {
						tranTypeFlag = TRANTYPE_NOREL_BANKIN;
						initBankInView();
					}

				} else if (bocFlag.equals("2")) {
					tranTypeFlag = TRANTYPE_NOREL_CRCD;
					initNoRelView();
				}

			} else if (TranDataCenter.getInstance().getDirpayeeList()
					.contains(accInInfoMap)) {
				// 定向
				// 判断有没有所属银行
				TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
				String bankName = (String) accInInfoMap
						.get(Tran.TRAN_DIR_QUERYLIST_BANKNAME_RES);
				// 判断是否为中行
				String bocFlag = (String) accInInfoMap
						.get(Tran.TRANS_BOCFLAG_REQ);
				if (StringUtil.isNullOrEmpty(bocFlag)) {
					// 定向收款
					BaseDroidApp.getInstanse().dismissMessageDialog();
					return;
				}
				if (bocFlag.equals("0")) {
					// 跨行
					if ((Boolean) accInInfoMap.get(ISHAVEBANKNAME)) {
						// 有所属银行
						// 定向收款
						BaseDroidApp.getInstanse().dismissMessageDialog();
						refreshTranDir(bankName);
					} else {
						// 无所属银行_进入选择银行页面
						Intent intent = new Intent(
								TransferManagerActivity1.this,
								ChooseBankActivity.class);
						startActivityForResult(intent,
								ConstantGloble.CHOOSE_BANKNAME);
					}
				} else if (bocFlag.equals("1")) {
					// 行内
					// 有所属银行
					// 定向收款
					BaseDroidApp.getInstanse().dismissMessageDialog();
					refreshTranDir(bankName);
				} else if (bocFlag.equals("2")) {
					// 非关联
					// 有所属银行
					// 定向收款
					BaseDroidApp.getInstanse().dismissMessageDialog();
					refreshTranDir(bankName);
				}

			} else if (TranDataCenter.getInstance().getEbpsDirList()
					.contains(accInInfoMap)) {
				// 实时定向
				// 判断有没有所属银行
				TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
				String bankName = (String) accInInfoMap
						.get(Tran.TRAN_DIR_QUERYLIST_BANKNAME_RES);
				// 跨行
				if ((Boolean) accInInfoMap.get(ISHAVEBANKNAME)) {
					// 有所属银行
					// 定向收款
					BaseDroidApp.getInstanse().dismissMessageDialog();
					refreshTranDir(bankName);
				} else {
					// 进入所属银行选择
					// 查询所属银行——实时
					psnEbpsQueryAccountOfBank(0, 10);
				}
			} else if (TranDataCenter.getInstance().getEbpsList()
					.contains(accInInfoMap)) {
				// 实时普通
				BaseDroidApp.getInstanse().dismissMessageDialog();
				TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
				refreshTranEbps();
			} else {// 关联账户
				String accountId = (String) accInInfoMap.get(Comm.ACCOUNT_ID);
				String accountType = (String) accInInfoMap
						.get(Comm.ACCOUNT_TYPE);
				if (ConstantGloble.ACC_TYPE_GRE.equals(accountType)
						|| ConstantGloble.ZHONGYIN.equals(accountType)) {// 信用卡
					// 查询信用卡详情
					inCreditCurrencyAndBalance = new ArrayList<Map<String, String>>();
					requestForCrcdDetail(accountId);
					// 全球交易人民币记账功能查询 openFlag 是否开通 如果开通 则交易币种只支持人民币
					// requestPsnCrcdChargeOnRMBAccountQuery(accountId);
				} else if (ConstantGloble.SINGLEWAIBI.equals(accountType)) {
					// 查询币种
					crcdId = accountId;
					// tranTypeFlag = TRANTYPE_REL_CRCD_BUY;
					// initBankInView();
					// requestCrcdCurrency((String) accInInfoMap
					// .get(Comm.ACCOUNTNUMBER));
					// requestForCrcdDetail((String) accInInfoMap
					// .get(Comm.ACCOUNT_ID));

					requestForCrcdCurrencyOne((String) accInInfoMap
							.get(Comm.ACCOUNTNUMBER));

				} else {// 其他类型卡 (非信用卡)
					requestForAccountDetail(accountId);
				}
			}

		}
	};

	/** 转入账户 选择 单外币信用卡 查询 信用卡币种 */
	public void requestForCrcdCurrencyOne(String accountNumber) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.CREDITCARDCURRENCYQUERY);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.ACCOUNTNUMBER_RES, accountNumber);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCrcdCurrencyCallBackOne");

	}

	@SuppressWarnings("unused")
	public void requestForCrcdCurrencyCallBackOne(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		TranDataCenter.getInstance().setAccInInfoMap(addInforMap);
		BaseDroidApp.getInstanse().dismissMessageDialog();
		// 对result进行非空判断
		Map<String, String> currencyMap1 = (Map<String, String>) result
				.get(Tran.CREDIT_CARD_CURRENCY1_RES);

		crcdCurrency1 = currencyMap1.get(Crcd.CRCD_CODE);
		inCrcdCurrency1 = crcdCurrency1;
		Map<String, String> currencyMap = (Map<String, String>) result
				.get(Tran.CREDIT_CARD_CURRENCY2_RES);
		// if (!StringUtil.isNullOrEmpty(currencyMap1)
		// && !StringUtil.isNullOrEmpty(currencyMap)) {
		// // 双币种信用卡
		// crcdCurrency2 = currencyMap.get(Crcd.CRCD_CODE);
		// inCrcdCurrency2 = crcdCurrency2;
		// // 全球交易人民币记账功能查询 openFlag 是否开通 如果开通 则交易币种只支持人民币
		// //如果是双币种 才调用此接口
		// Map<String, Object> accInInfoMap =
		// TranDataCenter.getInstance().getAccInInfoMap();
		// String accountId = (String) accInInfoMap.get(Comm.ACCOUNT_ID);
		// requestPsnCrcdChargeOnRMBAccountQuery(accountId);
		// return;
		// } else if (!StringUtil.isNullOrEmpty(currencyMap1)
		// && StringUtil.isNullOrEmpty(currencyMap)) {
		// String currency = currencyMap1.get(Crcd.CRCD_CODE);
		// if (!StringUtil.isNull(LocalData.Currency.get(currency))) {
		// if (LocalData.Currency.get(currency).equals(
		// ConstantGloble.ACC_RMB)) {
		// crcdCurrency2 = "";
		// } else {
		// // 单外币信用卡
		// crcdCurrency2 = currencyMap1.get(Crcd.CRCD_CODE);
		// }
		// }
		//
		// }

		/** 查询单外币信用卡详情 */
		requestForCrcdDetailOne(crcdId);

	}

	/** 查询单外币信用卡详情 */
	private void requestForCrcdDetailOne(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.CRCDQUERYACCOUNTDETAIL);
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_ID, accountId);
		// 外币
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY, crcdCurrency2);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCrcdCurrencyDetailCallBackOne");
	}

	public void requestForCrcdCurrencyDetailCallBackOne(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
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
		Map<String, String> detailMap = new HashMap<String, String>();
		List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
		detailList = (List<Map<String, String>>) resultMap
				.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		if (StringUtil.isNullOrEmpty(detailList)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}

		TranDataCenter.getInstance().setCurrInDetail(resultMap);
		inCreditCurrencyAndBalance = new ArrayList<Map<String, String>>();

		for (int i = 0; i < detailList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("loanBalanceLimitFlag",
					detailList.get(i).get("loanBalanceLimitFlag"));
			map.put("loanBalanceLimit",
					detailList.get(i).get("loanBalanceLimit"));
			map.put("currency", detailList.get(i).get("currency"));
			inCreditCurrencyAndBalance.add(map);
		}

		showAccInDetailView();

		String accountInType = (String) TranDataCenter.getInstance()
				.getAccInInfoMap().get(Comm.ACCOUNT_TYPE);
		String accountOutType = (String) TranDataCenter.getInstance()
				.getAccOutInfoMap().get(Comm.ACCOUNT_TYPE);
		// if(outOrInFlag.equals(IN)){
		// if((ConstantGloble.ACC_TYPE_GRE.equals(accountInType)
		// || ConstantGloble.ZHONGYIN.equals(accountInType)
		// || ConstantGloble.SINGLEWAIBI.equals(accountInType))
		// &&(ConstantGloble.ACC_TYPE_BRO.equals(accountOutType)
		// || ConstantGloble.ZHONGYIN.equals(accountOutType))
		// ){
		//
		// //组装数据
		// combineCreditCrcdData();
		// initRelCrcdTran22(accountOutType);
		//
		// }else{
		// combineData();
		// initRelTran();
		// }
		// }

		//

		combineSingleCredit();
		initRelCrcdTranSingleCredit();
	}

	/** 单外币组装数据 */
	private void combineSingleCredit() {
		currencyList = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(outCurrencyAndBalance)) {
			for (int i = 0; i < outCurrencyAndBalance.size(); i++) {
				if (outCurrencyAndBalance.get(i).get("currency")
						.equals(inCrcdCurrency1)) {
					currencyList.add(inCrcdCurrency1);
					return;
				}
			}
		}
		if (!StringUtil.isNullOrEmpty(outCreditCurrencyAndBalance)) {
			for (int i = 0; i < outCreditCurrencyAndBalance.size(); i++) {
				if (outCreditCurrencyAndBalance.get(i).get("currency")
						.equals(inCrcdCurrency1)) {
					currencyList.add(inCrcdCurrency1);
					return;
				}
			}
		}

		if (StringUtil.isNullOrEmpty(currencyList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.no_same_currency));

		}

	}

	/** 单外币初始化 底部按钮 */
	private void initRelCrcdTranSingleCredit() {
		View v = mInflater.inflate(
				R.layout.tran_rel_transfer_seting_mytransfer, null);

		initRelViewSingleCredit(v);
		bottomLayout.removeAllViews();
		bottomLayout.addView(v);
	}

	private void initRelViewSingleCredit(final View v) {

		final String accountOutType = (String) TranDataCenter.getInstance()
				.getAccOutInfoMap().get(Comm.ACCOUNT_TYPE);
		final String accountIntType = (String) TranDataCenter.getInstance()
				.getAccInInfoMap().get(Comm.ACCOUNT_TYPE);

		final List<String> currencyCodeFlagList = new ArrayList<String>();

		if (!StringUtil.isNullOrEmpty(currencyList)) {

			currencyCodeFlagList
					.add(LocalData.Currency.get(currencyList.get(0)));
		} else {
			currencyCodeFlagList.add("-");
		}

		// 币种
		Spinner currencySp = (Spinner) v
				.findViewById(R.id.sp_rel_trans_currency_tranSeting);
		// 钞汇标志
		final Spinner cashRemitSp = (Spinner) v
				.findViewById(R.id.sp_rel_trans_cashRemit_tranSeting);
		final List<String> cashRemitFlagList = new ArrayList<String>();
		final List<String> cashRemitList = new ArrayList<String>();

		ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(
				TransferManagerActivity1.this, R.layout.custom_spinner_item,
				currencyCodeFlagList);
		currencyAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currencySp.setAdapter(currencyAdapter);
		currencySp.setSelection(0);

		currencySp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (!StringUtil.isNullOrEmpty(currencyList)) {
					currencyCode = currencyList.get(position);
				} else {
					currencyCode = null;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if (!StringUtil.isNullOrEmpty(currencyList)) {
					currencyCode = currencyList.get(0);
				} else {
					currencyCode = null;
				}

			}
		});

		currencySp.setEnabled(false);

		if (!StringUtil.isNullOrEmpty(outCurrencyAndBalance)) {
			for (int i = 0; i < outCurrencyAndBalance.size(); i++) {
				if (outCurrencyAndBalance.get(i).get("currency")
						.equals(inCrcdCurrency1)) {

					String flag = outCurrencyAndBalance.get(i).get("cashRemit");
					cashRemitList.add(flag);
					cashRemitFlagList.add(flagConvertRemitFlag(flag));

				}
			}
		}
		if (!StringUtil.isNullOrEmpty(outCreditCurrencyAndBalance)) {
			for (int i = 0; i < outCreditCurrencyAndBalance.size(); i++) {
				if (outCreditCurrencyAndBalance.get(i).get("currency")
						.equals(inCrcdCurrency1)) {
					String flag = outCreditCurrencyAndBalance.get(i).get(
							"cashRemit");
					cashRemitList.add(flag);
					cashRemitFlagList.add(flagConvertRemitFlag(flag));
				}
			}
		}

		if (StringUtil.isNullOrEmpty(currencyCode)) {
			cashRemitSp.setBackgroundDrawable(TransferManagerActivity1.this
					.getResources().getDrawable(R.drawable.bg_spinner_default));
			cashRemitSp.setEnabled(false);
		} else {
			cashRemitSp.setBackgroundDrawable(TransferManagerActivity1.this
					.getResources().getDrawable(R.drawable.bg_spinner));
			cashRemitSp.setEnabled(true);
		}

		ArrayAdapter<String> cashRemitAdapter = new ArrayAdapter<String>(
				TransferManagerActivity1.this, R.layout.custom_spinner_item,
				cashRemitFlagList);
		cashRemitAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cashRemitSp.setAdapter(cashRemitAdapter);
		cashRemitSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (!StringUtil.isNullOrEmpty(cashRemitFlagList)) {
					cashRemitCode = cashRemitList.get(position);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if (!StringUtil.isNullOrEmpty(cashRemitFlagList)) {
					cashRemitCode = cashRemitList.get(0);
				}

			}
		});

		// 转入账户是 借记卡 转出账户是 信用卡的时候 只显示现钞 textview 形式 显示
		if (ConstantGloble.ACC_TYPE_BRO.equals(accountIntType)
				&& (ConstantGloble.ACC_TYPE_GRE.equals(accountOutType)
						|| ConstantGloble.ZHONGYIN.equals(accountOutType) || ConstantGloble.SINGLEWAIBI
							.equals(accountOutType))) {
			v.findViewById(R.id.sp_rel_trans_cashRemit_textview_layout)
					.setVisibility(View.VISIBLE);
			v.findViewById(R.id.sp_rel_trans_cashRemit_tranSeting_layout)
					.setVisibility(View.GONE);
			// 转入账户 和 转出账户 都是信用卡的时候 不显示钞汇标识
		} else if (ConstantGloble.ACC_TYPE_GRE.equals(accountOutType)
				|| ConstantGloble.ZHONGYIN.equals(accountOutType)
				|| ConstantGloble.SINGLEWAIBI.equals(accountOutType)) {

			if (ConstantGloble.ACC_TYPE_GRE.equals(accountIntType)
					|| ConstantGloble.ZHONGYIN.equals(accountIntType)
					|| ConstantGloble.SINGLEWAIBI.equals(accountIntType)) {
				v.findViewById(R.id.sp_rel_trans_cashRemit_tranSeting_layout)
						.setVisibility(View.GONE);

			}

		}

		// 转账金额
		amountEt = (EditText) v
				.findViewById(R.id.et_amount_rel_trans_mytransfer);
		// if (StringUtil.isNullOrEmpty(currencyList)) {
		//
		// amountEt.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (event.getAction() == MotionEvent.ACTION_DOWN) {
		// BaseDroidApp.getInstanse().showInfoMessageDialog(
		// getString(R.string.no_same_currency));
		// }
		// return true;
		// }
		// });
		// // amountEt.setOnClickListener(new OnClickListener() {
		// // @Override
		// // public void onClick(View v) {
		// // BaseDroidApp.getInstanse().showInfoMessageDialog(
		// // getString(R.string.no_same_currency));
		// //
		// // }
		// // });
		//
		// }

		// 附言
		remarkEt = (EditText) v
				.findViewById(R.id.et_remark_rel_trans_mytransfer);
		EditTextUtils.setLengthMatcher(TransferManagerActivity1.this, remarkEt,
				50);
		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(amountEt, tv_for_amount);
		initRelBottomBtn(v);
	}

	/**
	 * 刷新定向转入账户
	 * 
	 * @param bankname
	 */
	public void refreshTranDir(String bankname) {
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		// 添加一个新的布局文件--转入账户详细信息
		accInDetailLayout = (LinearLayout) inflater.inflate(
				R.layout.dept_acc_in_list_detail, null);
		mAccInLl.removeAllViews();
		mAccInLl.addView(accInDetailLayout);
		mAccInLl.setVisibility(View.VISIBLE);
		// mAccInListLayout.setVisibility(View.GONE);
		newAddTranInBtn.setVisibility(View.GONE);
		showAccInDirListItemData(bankname);// 显示定向收款人账户条目的详细信息
		isTranInFirst = false;
		// 根据bocflag 判断 中行内定向汇款非信用卡收款人为[”1”]
		// 国内跨行定向汇款为[”0”]
		// 非关联信用卡定向还款为[”2”]
		String bocFlag = (String) accInInfoMap.get(Tran.TRANS_BOCFLAG_REQ);
		String type = (String) accInInfoMap
				.get(Tran.TRAN_DIR_QUERYLIST_ACCOUNTTYPE_RES);
		if (StringUtil.isNullOrEmpty(bocFlag)) {
			return;
		}
		if (bocFlag.equals("0")) {

			tranTypeFlag = TRANTYPE_DIR_BANKOTHER;
			// initBankOtherView();
			initBankOtherorientationView();
			// tranTypeFlag = TRANTYPE_NOREL_BANKOTHER;
			// initBankOtherCommonView();
		} else if (bocFlag.equals("1")) {
			if (type.equals(ConstantGloble.ZHONGYIN)
					|| type.equals(ConstantGloble.SINGLEWAIBI)
					|| type.equals(ConstantGloble.ACC_TYPE_GRE)) {
				// 非关联信用卡还款流程
				tranTypeFlag = TRANTYPE_DIR_CRCD;
				initNoRelView();
			} else {
				tranTypeFlag = TRANTYPE_DIR_BANKIN;
				initBankInView();
			}
		} else if (bocFlag.equals("2")) {
			tranTypeFlag = TRANTYPE_DIR_CRCD;
			initNoRelView();
		} else if (bocFlag.equals("3")) {
			// 实时定向
			tranTypeFlag = TRANTYPE_SHISHI_DIR_BANKOTHER;
			// initBankOtherView();
			initBankOtherorientationView();

			// tranTypeFlag = TRANTYPE_SHISHI_BANKOTHER;
			// initBankOtherCommonView();
		}
	}

	/**
	 * 刷新实时转入账户
	 * 
	 * @param bankname
	 */
	public void refreshTranEbps() {
		// 添加一个新的布局文件--转入账户详细信息
		accInDetailLayout = (LinearLayout) inflater.inflate(
				R.layout.dept_acc_in_list_detail, null);
		mAccInLl.removeAllViews();
		mAccInLl.addView(accInDetailLayout);
		mAccInLl.setVisibility(View.VISIBLE);
		newAddTranInBtn.setVisibility(View.GONE);
		showAccInEbpsListItemData();
		isTranInFirst = false;
		tranTypeFlag = TRANTYPE_SHISHI_BANKOTHER;
		initBankOtherCommonView();
	}

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

		TextView accNameTv = (TextView) accInDetailLayout
				.findViewById(R.id.tv_acc_in_item_type);
		TextView accountTypeTv = (TextView) accInDetailLayout
				.findViewById(R.id.tv_acc_in_item_name);
		TextView accountNumberTv = (TextView) accInDetailLayout
				.findViewById(R.id.tv_acc_in_item_no);
		accNameTv.setText((String) accInInfoMap.get(Tran.TRANS_BANKNAME_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				accNameTv);
		String strAccountType = (String) accInInfoMap
				.get(Tran.TRANS_ACCOUNTNAME_RES);
		accountTypeTv.setText(strAccountType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				accountTypeTv);
		String accountNumberStr = (String) accInInfoMap
				.get(Tran.TRANS_ACCOUNTNUMBER_RES);
		accountNumberTv
				.setText(StringUtil.getForSixForString(accountNumberStr));
		accInDetailLayout.setOnClickListener(accInClicklistener);
	}

	/**
	 * 显示转入账户条目的详细信息(定向账户)
	 */
	private void showAccInDirListItemData(String bankname) {
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		if (accInInfoMap == null) {
			return;
		}
		// 存储转入账户信息
		TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);

		TextView accNameTv = (TextView) accInDetailLayout
				.findViewById(R.id.tv_acc_in_item_type);
		TextView accountTypeTv = (TextView) accInDetailLayout
				.findViewById(R.id.tv_acc_in_item_name);
		TextView accountNumberTv = (TextView) accInDetailLayout
				.findViewById(R.id.tv_acc_in_item_no);
		accNameTv.setText(bankname);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				accNameTv);
		String strAccountType = (String) accInInfoMap
				.get(Tran.TRAN_DIR_QUERYLIST_ACCOUNTNAME_RES);
		accountTypeTv.setText(strAccountType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				accountTypeTv);
		String accountNumberStr = (String) accInInfoMap
				.get(Tran.TRAN_DIR_QUERYLIST_ACCOUNTNUMBER_RES);
		accountNumberTv
				.setText(StringUtil.getForSixForString(accountNumberStr));
		accInDetailLayout.setOnClickListener(accInClicklistener);
	}

	/**
	 * 显示转入账户条目的详细信息(实时账户)
	 */
	private void showAccInEbpsListItemData() {
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		if (accInInfoMap == null) {
			return;
		}
		// 存储转入账户信息
		TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);

		TextView accNameTv = (TextView) accInDetailLayout
				.findViewById(R.id.tv_acc_in_item_type);
		TextView accountTypeTv = (TextView) accInDetailLayout
				.findViewById(R.id.tv_acc_in_item_name);
		TextView accountNumberTv = (TextView) accInDetailLayout
				.findViewById(R.id.tv_acc_in_item_no);
		String bankname = (String) accInInfoMap
				.get(Tran.EBPSQUERY_PAYEEBANKNAME_REQ);
		accNameTv.setText(bankname);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				accNameTv);
		String strAccountType = (String) accInInfoMap
				.get(Tran.EBPSQUERY_PAYEEACTNAME_REQ);
		accountTypeTv.setText(strAccountType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				accountTypeTv);
		String accountNumberStr = (String) accInInfoMap
				.get(Tran.EBPSQUERY_PAYEEACTNO_REQ);
		accountNumberTv
				.setText(StringUtil.getForSixForString(accountNumberStr));
		accInDetailLayout.setOnClickListener(accInClicklistener);
	}

	/**
	 * 二维码转账
	 */
	private OnClickListener twoDimensionListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Intent intent1 = new Intent(TransferManagerActivity1.this,
					CaptureActivity.class);
			Intent intent = getIntent();
			if (intent != null) {
				otherPartJumpFlag = intent.getIntExtra(
						ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
				if (StringUtil.isNullOrEmpty(otherPartJumpFlag)) {
					return;
				}
				if (otherPartJumpFlag == ConstantGloble.ACC_TO_TRAN
						|| otherPartJumpFlag == ConstantGloble.ACC_TO_TRAN_CRCD) {
					intent1.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,
							ConstantGloble.ACC_TO_TRAN);
				}
			}
			intent1.putExtra(ConstantGloble.TRAN_TWO_DIMEN_FLAG,
					ConstantGloble.TRAN_TO_TOWDIMEN);
			startActivity(intent1);
		}
	};
	/**
	 * 手机号转账
	 */
	private OnClickListener phoneListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent1 = new Intent(TransferManagerActivity1.this,
					MobileTranActivity.class);
			Intent intent = getIntent();
			if (intent != null) {
				otherPartJumpFlag = intent.getIntExtra(
						ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
				if (StringUtil.isNullOrEmpty(otherPartJumpFlag)) {
					return;
				}
				if (otherPartJumpFlag == ConstantGloble.ACC_TO_TRAN
						|| otherPartJumpFlag == ConstantGloble.ACC_TO_TRAN_CRCD) {
					intent1.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,
							ConstantGloble.ACC_TO_TRAN);
				}
			}
			intent1.putExtra(ConstantGloble.TRAN_MOBILE_FLAG,
					ConstantGloble.TRAN_TO_MOBILE);
			startActivity(intent1);
		}
	};
	/**
	 * 模糊查询
	 */
	private TextWatcher textWatcher = new TextWatcher() {
		List<Map<String, Object>> filterList = new ArrayList<Map<String, Object>>();

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			filterDate(s);
			if (inadapter != null) {
				inadapter.setDate(filterList);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}

		private void filterDate(CharSequence s) {
			filterList.clear();
			if (s == null) {
				filterList.addAll(accInListData);
				return;
			}

			if (!StringUtil.isNullOrEmpty(accInListData)) {
				for (Map<String, Object> map : accInListData) {
					String name = null;
					if (inadapter.getRelAccList().contains(map)) {
						name = (String) map.get(Tran.NICKNAME_RES);
					} else {// 常用收款人
						name = (String) map.get(Tran.TRANS_ACCOUNTNAME_RES);
					}
					if (name == null) {
						name = (String) map
								.get(Tran.EBPSQUERY_PAYEEACTNAME_REQ);
					}
					if (!StringUtil.isNullOrEmpty(name)) {
						if (name.contains(s) == true)
							filterList.add(map);
					}
				}
			}

		}
	};

	/**
	 * 初始化底部视图 关联账户转账底部视图 非信用卡
	 */
	private void initRelTran() {
		View v = mInflater.inflate(
				R.layout.tran_rel_transfer_seting_mytransfer, null);
		if (isCreditRemit()) {
			// 转出账户是 长城信用卡 转入账户是电子借记卡 显示附加提示语
			initCreditRemit(v);
			v.findViewById(R.id.comm_credit_card_repay_info).setVisibility(
					View.VISIBLE);
		} else {
			initRelTranView(v);
			v.findViewById(R.id.comm_credit_card_repay_info).setVisibility(
					View.GONE);
		}

		bottomLayout.removeAllViews();
		bottomLayout.addView(v);
	}

	/**
	 * 初始化底部视图 关联账户转账底部视图 信用卡
	 */
	private void initRelCrcdTran() {
		String accountInType = (String) TranDataCenter.getInstance()
				.getAccInInfoMap().get(Comm.ACCOUNT_TYPE);
		String accountOutType = (String) TranDataCenter.getInstance()
				.getAccOutInfoMap().get(Comm.ACCOUNT_TYPE);

		View v = mInflater.inflate(
				R.layout.tran_rel_transfer_seting_mytransfer, null);
		if (ConstantGloble.GREATWALL.equals(accountOutType)) {
			if (ConstantGloble.ZHONGYIN.equals(accountInType)
					|| ConstantGloble.GREATWALL.equals(accountInType)
					|| ConstantGloble.SINGLEWAIBI.equals(accountInType)
					|| ConstantGloble.ACC_TYPE_BRO.equals(accountInType)) {
				v.findViewById(R.id.comm_credit_card_repay_info).setVisibility(
						View.VISIBLE);
			}

		} else {
			v.findViewById(R.id.comm_credit_card_repay_info).setVisibility(
					View.GONE);
		}

		initRelView2(v);
		bottomLayout.removeAllViews();
		bottomLayout.addView(v);
	}

	/**
	 * 初始化关联信用卡 底部视图
	 */

	/*
	 * private void initCrcdView() { View v = mInflater.inflate(
	 * R.layout.tran_relation_credit_card_trans_seting_mytransfer, null); //
	 * 本人关联信用卡还款 Button relCrcdRepayBtn = (Button) v
	 * .findViewById(R.id.btn_self_rel_creditCard_transSeting); Map<String,
	 * Object> inMap = TranDataCenter.getInstance() .getAccInInfoMap(); String
	 * type = (String) inMap.get(Acc.ACC_ACCOUNTTYPE_RES); if
	 * (type.equals(ConstantGloble.SINGLEWAIBI)) { // 单外币信用卡 } else {
	 * relCrcdRepayBtn.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { tranTypeFlag =
	 * TRANTYPE_REL_CRCD_REPAY; View view = (ViewGroup) mInflater .inflate(
	 * R.layout.tran_relation_self_credit_card_trans_seting_mytransfer, null);
	 * initRelCrcdRepayView(view); bottomLayout.removeAllViews();
	 * bottomLayout.addView(view); } }); }
	 * 
	 * // 关联信用卡购汇还款 Button relCrcdBuyBtn = (Button) v
	 * .findViewById(R.id.btn_payRemit_rel_creditCard_transSeting); if
	 * (!StringUtil.isNullOrEmpty(crcdCurrency2)) {
	 * relCrcdBuyBtn.setVisibility(View.VISIBLE);
	 * relCrcdBuyBtn.setEnabled(true);
	 * relCrcdBuyBtn.setTextColor(getResources().getColor(R.color.black));
	 * relCrcdBuyBtn.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { tranTypeFlag =
	 * TRANTYPE_REL_CRCD_BUY; // 查询第二币种详情 Map<String, Object> accInInfoMap =
	 * TranDataCenter .getInstance().getAccInInfoMap(); toPayeeId = (String)
	 * accInInfoMap.get(Comm.ACCOUNT_ID);
	 * requestForCrcdCurrencyDetail(toPayeeId); // requestCommConversationId();
	 * } }); } else { relCrcdRepayBtn.setVisibility(View.VISIBLE);
	 * BottomButtonUtils.setSingleLineStyleGray(relCrcdRepayBtn);
	 * relCrcdBuyBtn.setVisibility(View.GONE); relCrcdBuyBtn.setEnabled(false);
	 * relCrcdBuyBtn.setTextColor(getResources().getColor(R.color.gray)); }
	 * bottomLayout.removeAllViews(); bottomLayout.addView(v); }
	 * 
	 * /** 初始化非关联信用卡 底部视图
	 */
	private void initNoRelView() {
		View v = mInflater.inflate(
				R.layout.tran_comm_credit_card_trans_seting_mytransfer, null);
		initNoRelCrcdRepayView(v);
		bottomLayout.removeAllViews();
		bottomLayout.addView(v);
	}

	/**
	 * 初始化行内转账底部视图
	 */
	private void initBankInView() {
		View v = mInflater.inflate(
				R.layout.tran_comm_boc_trans_seting_mytransfer, null);

		initBankInAndOtherInputView(v, false);
		initBankInBtnBottom(v);

		bottomLayout.removeAllViews();
		bottomLayout.addView(v);
	}

	/**
	 * 行内和跨行公用的 金额输入框和附言 发送手机短信
	 * 
	 * @param v
	 *            视图
	 * @param isotherBank
	 *            标记是跨行转账(true),中行转账(false)
	 * 
	 */
	private void initBankInAndOtherInputView(View v, boolean isotherBank) {
		currencyCode = ConstantGloble.PRMS_CODE_RMB;
		cashRemitCode = ConstantGloble.CASHREMIT_00;

		/** 用户输入转账金额 */
		amountEt = (EditText) v
				.findViewById(R.id.et_commBoc_transferAmount_tranSeting);
		/** 用户输入附言 */
		remarkEt = (EditText) v.findViewById(R.id.et_commBoc_remark_tranSeting);
		EditTextUtils.setLengthMatcher(this, remarkEt, 20);// 行内（电子卡转账、行内转账，行内定向，跨行转账，跨行实时转账，跨行定向，跨行实时定向转账等）均改为20字符

		if (isotherBank) {
			containerV = v.findViewById(R.id.container);
			inBankNameEt = (TextView) v
					.findViewById(R.id.et_acc_bankname_payee_other_bank_write);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					inBankNameEt);
			kBankBtn = (Button) v
					.findViewById(R.id.btn_query_kbank_othbank_write);
			radio_tran_type = (RadioGroup) v
					.findViewById(R.id.radioGroupForTranType);
			rb_shishi = (RadioButton) v.findViewById(R.id.radio_shishi);
			rb_common = (RadioButton) v.findViewById(R.id.radio_common);
			Map<String, Object> map = TranDataCenter.getInstance()
					.getAccOutInfoMap();
			String outtype = (String) map.get(Acc.ACC_ACCOUNTTYPE_RES);
			if (outtype.equals(ConstantGloble.ZHONGYIN)) {
				rb_common.setVisibility(View.INVISIBLE);
			}
			radio_tran_type.setOnCheckedChangeListener(this);
			common_tag = false;
			shishi_tag = false;
			if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER
					|| tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {
				rb_common.setVisibility(View.GONE);
				rb_shishi.setVisibility(View.VISIBLE);
				v.findViewById(R.id.radio_shishi_prompt).setVisibility(
						View.VISIBLE);
				// v.findViewById(R.id.radio_shishi_layout).setVisibility(View.VISIBLE);
				rb_shishi.setChecked(true);
				shishi_tag = true;

			} else {
				rb_shishi.setVisibility(View.GONE);
				v.findViewById(R.id.radio_shishi_prompt).setVisibility(
						View.VISIBLE);
				// v.findViewById(R.id.radio_shishi_layout).setVisibility(View.GONE);
				rb_common.setVisibility(View.VISIBLE);
				rb_common.setChecked(true);
				common_tag = true;
			}

			if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
				amountEt.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						String sum = amountEt.getText().toString();
						if (!StringUtil.isNull(sum)) {
							double dmoney = Double.parseDouble(sum);
							if (dmoney > 50000) {
								BaseDroidApp.getInstanse().showMessageDialog(
										"转账金额不可大于5万元 ", new OnClickListener() {
											@Override
											public void onClick(View v) {
												amountEt.getText().clear();
												BaseDroidApp.getInstanse()
														.dismissMessageDialog();
											}
										});
							}
						}
					}
				});
			}
		}

		/** 收款人手机号 */
		payeeMobileEt = (EditText) v
				.findViewById(R.id.et_commBoc_payeeMobile_transSeting);
		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(amountEt, tv_for_amount);
		/** 常用行内 短信通知收款人复选框 */
		final CheckBox sendSmcToPayeeCk = (CheckBox) v
				.findViewById(R.id.ck_sendSmcPayee_bocTrans_transSeting);
		final LinearLayout sendSmcToPayeeLl = (LinearLayout) v
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
							if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
								payeeMobile = (String) accInInfoMap
										.get(Tran.EBPSQUERY_PAYEEMOBILE_REQ);
							} else {
								payeeMobile = (String) accInInfoMap
										.get(Tran.TRANS_MOBILE_RES);
							}

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
		/** 保存为常用收款人 */
		LinearLayout savePayeeLayout = (LinearLayout) v
				.findViewById(R.id.tran_save_payee_layout);
		CheckBox savePayeeCk = (CheckBox) v
				.findViewById(R.id.tran_save_payee_checkbox);
		if (addNewPayeeFlag) {
			savePayeeLayout.setVisibility(View.VISIBLE);
			savePayeeCk.setChecked(true);
			isAddPayeeChecked = ConstantGloble.TRUE;
		}
		savePayeeCk
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							isAddPayeeChecked = ConstantGloble.TRUE;
						} else {
							isAddPayeeChecked = ConstantGloble.FALSE;
						}
					}
				});
	}

	/**
	 * 初始化跨行转账底部视图
	 */
	private void initBankOtherView() {
		View v = mInflater.inflate(R.layout.tran_comm_other_bank_layout, null);
		initBankInAndOtherInputView(v, true);
		initBankOtherBottom(v);
		bottomLayout.removeAllViews();
		bottomLayout.addView(v);
	}

	/**
	 * 初始化跨行转账底部视图--- 实时跨行 和 普通跨行
	 */
	private void initBankOtherCommonView() {
		View v = mInflater.inflate(R.layout.tran_comm_other_bank_layout1, null);
		initBankInAndOtherInputCommonView(v, true);
		initBankOtherBottom(v);
		bottomLayout.removeAllViews();
		bottomLayout.addView(v);

//		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
//				.getAccOutInfoMap();
//		accTypeout = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
//		boolean cuttencyflag = getoutcurrey();// 转出是否有人民币
//		if (!cuttencyflag) {
//			// 转出无人民币
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					getString(R.string.gouhui_no_cn));
//			return;
//		}
	}

	/**
	 * 初始化跨行转账底部视图--- 实时定向跨行 和 普通定向跨行 TRANTYPE_DIR_BANKOTHER
	 */
	private void initBankOtherorientationView() {
		View v = mInflater.inflate(R.layout.tran_comm_other_bank_layout2, null);
		initBankInAndOtherInputorientationView(v, true);
		initBankOtherBottom(v);
		bottomLayout.removeAllViews();
		bottomLayout.addView(v);

//		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
//				.getAccOutInfoMap();
//		accTypeout = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
//		boolean cuttencyflag = getoutcurrey();// 转出是否有人民币
//		if (!cuttencyflag) {
//			// 转出无人民币
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					getString(R.string.gouhui_no_cn));
//			return;
//		}
	}

	/**
	 * 初始化 实时跨行转账 普通跨行转账 底部视图
	 * 
	 * @param v
	 *            视图
	 * @param isotherBank
	 *            标记是跨行转账(true),中行转账(false)
	 */
	private void initBankInAndOtherInputCommonView(View v, boolean isotherBank) {
		currencyCode = ConstantGloble.PRMS_CODE_RMB;
		cashRemitCode = ConstantGloble.CASHREMIT_00;

		/** 用户输入转账金额 */
		amountEt = (EditText) v
				.findViewById(R.id.et_commBoc_transferAmount_tranSeting);
		/** 用户输入附言 */
		remarkEt = (EditText) v.findViewById(R.id.et_commBoc_remark_tranSeting);
		EditTextUtils.setLengthMatcher(this, remarkEt, 20);// 跨行（电子卡转账、行内转账，行内定向，跨行转账，跨行实时转账，跨行定向，跨行实时定向转账等）均改为20字符

		if (isotherBank) {
			containerV = v.findViewById(R.id.container);
			containerV1 = v.findViewById(R.id.container1);
			inBankNameEt_txt = (TextView) v
					.findViewById(R.id.et_acc_bankname_payee_other_bank_write_txt);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					inBankNameEt_txt);
			isBankNameEt_txt = (TextView) v
					.findViewById(R.id.txt_query_bank_txt);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					isBankNameEt_txt);
			inBankNameEt = (TextView) v
					.findViewById(R.id.et_acc_bankname_payee_other_bank_write);
			isBankNameEt = (TextView) v.findViewById(R.id.txt_query_bank);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					inBankNameEt);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					isBankNameEt);
			kBankBtn = (Button) v
					.findViewById(R.id.btn_query_kbank_othbank_write);
			isBankBtn = (Button) v.findViewById(R.id.btn_query_bank);
			radio_tran_type = (RadioGroup) v
					.findViewById(R.id.radioGroupForTranType);
			rb_shishi = (RadioButton) v.findViewById(R.id.radio_shishi);
			rb_common = (RadioButton) v.findViewById(R.id.radio_common);
			radio_tran_type.setOnCheckedChangeListener(this);
			common_tag = false;
			shishi_tag = false;
			radio_shishi_prompt = (TextView) v
					.findViewById(R.id.radio_shishi_prompt);
			if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
				rb_shishi.performClick();
				shishi_tag = true;
			} else if (tranTypeFlag == TRANTYPE_NOREL_BANKOTHER) {
				rb_common.performClick();
				common_tag = true;
			}

			if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
				amountEt.addTextChangedListener(textWatcherTran);
			}
		}

		/** 收款人手机号 */
		payeeMobileEt = (EditText) v
				.findViewById(R.id.et_commBoc_payeeMobile_transSeting);
		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(amountEt, tv_for_amount);
		/** 常用行内 短信通知收款人复选框 */
		final CheckBox sendSmcToPayeeCk = (CheckBox) v
				.findViewById(R.id.ck_sendSmcPayee_bocTrans_transSeting);
		final LinearLayout sendSmcToPayeeLl = (LinearLayout) v
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
							if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
								payeeMobile = (String) accInInfoMap
										.get(Tran.EBPSQUERY_PAYEEMOBILE_REQ);
							} else {
								payeeMobile = (String) accInInfoMap
										.get(Tran.TRANS_MOBILE_RES);
							}

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
		/** 保存为常用收款人 */
		LinearLayout savePayeeLayout = (LinearLayout) v
				.findViewById(R.id.tran_save_payee_layout);
		CheckBox savePayeeCk = (CheckBox) v
				.findViewById(R.id.tran_save_payee_checkbox);
		if (addNewPayeeFlag) {
			savePayeeLayout.setVisibility(View.VISIBLE);
			savePayeeCk.setChecked(true);
			isAddPayeeChecked = ConstantGloble.TRUE;
		}
		savePayeeCk
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							isAddPayeeChecked = ConstantGloble.TRUE;
						} else {
							isAddPayeeChecked = ConstantGloble.FALSE;
						}
					}
				});
	}

	private TextWatcher textWatcherTran = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {

			if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
				String sum = amountEt.getText().toString();
				if (!StringUtil.isNull(sum)) {
					double dmoney = Double.parseDouble(sum);
					if (dmoney > 50000) {
						BaseDroidApp.getInstanse().showMessageDialog(
								"转账金额不可大于5万元 ", new OnClickListener() {
									@Override
									public void onClick(View v) {
										amountEt.getText().clear();
										BaseDroidApp.getInstanse()
												.dismissMessageDialog();
									}
								});
					}
				}

			}
		}
	};

	/**
	 * 初始化 实时定向跨行转账 普通定向跨行转账 底部视图
	 * 
	 * @param v
	 *            视图
	 * @param isotherBank
	 *            标记是跨行转账(true),中行转账(false)
	 */
	private void initBankInAndOtherInputorientationView(View v,
			boolean isotherBank) {
		currencyCode = ConstantGloble.PRMS_CODE_RMB;
		cashRemitCode = ConstantGloble.CASHREMIT_00;

		/** 用户输入转账金额 */
		amountEt = (EditText) v
				.findViewById(R.id.et_commBoc_transferAmount_tranSeting);
		/** 用户输入附言 */
		remarkEt = (EditText) v.findViewById(R.id.et_commBoc_remark_tranSeting);
		EditTextUtils.setLengthMatcher(this, remarkEt, 20);// 跨行定向（电子卡转账、行内转账，行内定向，跨行转账，跨行实时转账，跨行定向，跨行实时定向转账等）均改为20字符

		if (isotherBank) {

			containerV = v.findViewById(R.id.container);
			containerV1 = v.findViewById(R.id.container1);
			inBankNameEt_txt = (TextView) v
					.findViewById(R.id.et_acc_bankname_payee_other_bank_write_txt);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					inBankNameEt_txt);
			isBankNameEt_txt = (TextView) v
					.findViewById(R.id.txt_query_bank_txt);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					isBankNameEt_txt);
			inBankNameEt = (TextView) v
					.findViewById(R.id.et_acc_bankname_payee_other_bank_write);
			isBankNameEt = (TextView) v.findViewById(R.id.txt_query_bank);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					inBankNameEt);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					isBankNameEt);
			kBankBtn = (Button) v
					.findViewById(R.id.btn_query_kbank_othbank_write);
			isBankBtn = (Button) v.findViewById(R.id.btn_query_bank);
			RadioGroup radio_tran_type = (RadioGroup) v
					.findViewById(R.id.radioGroupForTranType);
			radio_shishi_orientation = (RadioButton) v
					.findViewById(R.id.radio_shishi_orientation);
			radio_common_orientation = (RadioButton) v
					.findViewById(R.id.radio_common_orientation);
			radio_tran_type.setOnCheckedChangeListener(this);
			radio_shishi_prompt = (TextView) v
					.findViewById(R.id.radio_shishi_prompt);
			common_tag = false;
			shishi_tag = false;

			if (tranTypeFlag == TRANTYPE_DIR_BANKOTHER) {
				radio_common_orientation.performClick();
				common_tag = true;
			} else if (tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {
				radio_shishi_orientation.performClick();
				shishi_tag = true;
			}
		}

		/** 收款人手机号 */
		payeeMobileEt = (EditText) v
				.findViewById(R.id.et_commBoc_payeeMobile_transSeting);
		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(amountEt, tv_for_amount);
		/** 常用行内 短信通知收款人复选框 */
		final CheckBox sendSmcToPayeeCk = (CheckBox) v
				.findViewById(R.id.ck_sendSmcPayee_bocTrans_transSeting);
		final LinearLayout sendSmcToPayeeLl = (LinearLayout) v
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
							if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
								payeeMobile = (String) accInInfoMap
										.get(Tran.EBPSQUERY_PAYEEMOBILE_REQ);
							} else {
								payeeMobile = (String) accInInfoMap
										.get(Tran.TRANS_MOBILE_RES);
							}

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
		/** 保存为常用收款人 */
		LinearLayout savePayeeLayout = (LinearLayout) v
				.findViewById(R.id.tran_save_payee_layout);
		CheckBox savePayeeCk = (CheckBox) v
				.findViewById(R.id.tran_save_payee_checkbox);
		if (addNewPayeeFlag) {
			savePayeeLayout.setVisibility(View.VISIBLE);
			savePayeeCk.setChecked(true);
			isAddPayeeChecked = ConstantGloble.TRUE;
		}
		savePayeeCk
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							isAddPayeeChecked = ConstantGloble.TRUE;
						} else {
							isAddPayeeChecked = ConstantGloble.FALSE;
						}
					}
				});
	}

	/**
	 * 初始化底部视图
	 * 
	 * @param v
	 */
	private void initRelTranView(View v) {

		final List<String> currencyCodeFlagList = new ArrayList<String>();
		if (isOpenFlag) {
			currencyCodeFlagList.add(LocalData.Currency.get("001"));
		} else {
			for (int i = 0; i < currencyList.size(); i++) {
				String currencyCode = currencyList.get(i);
				currencyCodeFlagList.add(LocalData.Currency.get(currencyCode));
			}

		}
		// 币种
		Spinner currencySp = (Spinner) v
				.findViewById(R.id.sp_rel_trans_currency_tranSeting);
		// 钞汇标志
		final Spinner cashRemitSp = (Spinner) v
				.findViewById(R.id.sp_rel_trans_cashRemit_tranSeting);
		final List<String> cashRemitFlagList = new ArrayList<String>();

		ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(
				TransferManagerActivity1.this, R.layout.custom_spinner_item,
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
							.setBackgroundDrawable(TransferManagerActivity1.this
									.getResources().getDrawable(
											R.drawable.bg_spinner_default));
				} else {
					if (isCreditRemit()) {
						cashRemitSp.setEnabled(false);
						cashRemitSp
								.setBackgroundDrawable(TransferManagerActivity1.this
										.getResources().getDrawable(
												R.drawable.bg_spinner_default));
					} else {
						cashRemitSp.setEnabled(true);
						cashRemitSp
								.setBackgroundDrawable(TransferManagerActivity1.this
										.getResources().getDrawable(
												R.drawable.bg_spinner));
					}

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
						TransferManagerActivity1.this,
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

		if (StringUtil.isNullOrEmpty(currencyCodeFlagList)) {
			currencySp.setEnabled(false);
			currencySp.setBackgroundDrawable(TransferManagerActivity1.this
					.getResources().getDrawable(R.drawable.bg_spinner_default));
			cashRemitSp.setEnabled(false);
			cashRemitSp.setBackgroundDrawable(TransferManagerActivity1.this
					.getResources().getDrawable(R.drawable.bg_spinner_default));
		}

		// 转账金额
		amountEt = (EditText) v
				.findViewById(R.id.et_amount_rel_trans_mytransfer);
		// 附言
		remarkEt = (EditText) v
				.findViewById(R.id.et_remark_rel_trans_mytransfer);
		EditTextUtils.setLengthMatcher(TransferManagerActivity1.this, remarkEt,
				50);
		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(amountEt, tv_for_amount);
		initRelBottomBtn(v);
	}

	/** 返回 true 转出账户是 长城信用卡 转入账户是借记卡 */

	private boolean isCreditRemit() {

		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();

		final String accType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
		final String accType2 = (String) accInInfoMap.get(Comm.ACCOUNT_TYPE);

		if (((ConstantGloble.ACC_TYPE_GRE).equals(accType)
				|| (ConstantGloble.ZHONGYIN).equals(accType) || (ConstantGloble.SINGLEWAIBI)
					.equals(accType))
				&& ((ConstantGloble.ACC_TYPE_BRO).equals(accType2))) {
			return true;
		}

		return false;
	}

	/**
	 * 转出账户是信用卡的情况下 初始化底部视图
	 */
	private void initRelView2(View v) {
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
		cashRemitFlagList.add(LocalData.CurrencyCashremit
				.get(ConstantGloble.CASHREMIT_00));

		ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(
				TransferManagerActivity1.this, R.layout.custom_spinner_item,
				currencyCodeFlagList);
		currencyAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currencySp.setAdapter(currencyAdapter);
		currencySp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				currencyCode = currencyList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				currencyCode = currencyList.get(0);
			}
		});

		cashRemitSp.setEnabled(false);
		cashRemitSp.setBackgroundDrawable(TransferManagerActivity1.this
				.getResources().getDrawable(R.drawable.bg_spinner_default));
		ArrayAdapter<String> cashRemitAdapter = new ArrayAdapter<String>(
				TransferManagerActivity1.this, R.layout.custom_spinner_item,
				cashRemitFlagList);
		cashRemitSp.setAdapter(cashRemitAdapter);
		cashRemitCode = ConstantGloble.CASHREMIT_00;

		// 转账金额
		amountEt = (EditText) v
				.findViewById(R.id.et_amount_rel_trans_mytransfer);
		// 附言
		remarkEt = (EditText) v
				.findViewById(R.id.et_remark_rel_trans_mytransfer);
		EditTextUtils.setLengthMatcher(TransferManagerActivity1.this, remarkEt,
				50);
		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(amountEt, tv_for_amount);
		initRelBottomBtn(v);
	}

	/**
	 * 初始化关联信用卡还款
	 */
	/*
	 * private void initRelCrcdRepayView(View v) {
	 * 
	 * Button nextBtn = (Button) v
	 * .findViewById(R.id.btn_rel_self_creditCard_transSeting); final EditText
	 * repayAmountEt = (EditText) v
	 * .findViewById(R.id.et_repayAmountValue_relSelf_creditCard_transSeting);
	 * TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
	 * EditTextUtils.relateNumInputToChineseShower(repayAmountEt,
	 * tv_for_amount); nextBtn.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { // 判断有没有选择选出账户 if
	 * (isTranOutFirst) { CustomDialog.toastInCenter(context,
	 * TransferManagerActivity1.this .getString(R.string.choose_outno_message));
	 * return; }
	 * 
	 * amount = repayAmountEt.getText().toString().trim(); boolean flag =
	 * judgeUserData(amount, false); if (!flag) { return; }
	 * 
	 * Map<String, String> userInputMap = new HashMap<String, String>();
	 * userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
	 * TranDataCenter.getInstance().setUserInputMap(userInputMap); Intent intent
	 * = new Intent(); intent.setClass(TransferManagerActivity1.this,
	 * RelSelfCreditCardConfirmActivity1.class); startActivity(intent); } }); }
	 * 
	 * /** 信用卡购汇还款
	 * 
	 * @param v
	 */
	private void initRelCrcdBuyView(View v) {
		RadioGroup remitTypeRg = (RadioGroup) v
				.findViewById(R.id.rg_creditType_transSeting);
		RadioButton allRepayRb = (RadioButton) v
				.findViewById(R.id.rb_all_transSeting);
		final RadioButton partRepayRb = (RadioButton) v
				.findViewById(R.id.rb_part_transSeting);
		TextView currencyTv = (TextView) v
				.findViewById(R.id.relation_credit_card_currency);

		final EditText repayAmountEt = (EditText) v
				.findViewById(R.id.et_amount_rel_creditCard_transSeting);
		Button nextBtn = (Button) v
				.findViewById(R.id.btn_next_creditCard_transSeting);
		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(repayAmountEt,
				tv_for_amount);
		// 还款金额设定方式
		// Map<String, Object> currOutDetail = TranDataCenter.getInstance()
		// .getCurrOutDetail();
		Map<String, Object> currOutDetail = TranDataCenter.getInstance()
				.getRelCrcdBuyCallBackMap();
		amount = (String) currOutDetail.get(Tran.CREDITCARD_BALLANCEAMT_RES);
		final String currentBalance = amount;

		currencyTv.setText(LocalData.Currency.get(crcdCurrency2));
		allRepayRb.setChecked(true);
		repayAmountSet = ConstantGloble.REPAY_ALL;
		repayAmountEt.setText(StringUtil.parseStringPattern(amount, 2));
		repayAmountEt.setEnabled(false);
		remitTypeRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_all_transSeting:// 全部
					repayAmountSet = ConstantGloble.REPAY_ALL;
					repayAmountEt.setText(StringUtil.parseStringPattern(
							currentBalance, 2));
					amount = currentBalance;
					repayAmountEt.setEnabled(false);
					break;
				case R.id.rb_part_transSeting:// 部分
					repayAmountSet = ConstantGloble.REPAY_PART;
					repayAmountEt.setText(null);
					repayAmountEt.setEnabled(true);
					break;
				default:
					break;
				}
			}
		});
		// 下一步
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断有没有选择选出账户
				if (isTranOutFirst) {
					CustomDialog.toastInCenter(context,
							TransferManagerActivity1.this
									.getString(R.string.choose_outno_message));
					mAccOutLl.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_card_new_red));
//					dept_out_info_layout.setVisibility(View.VISIBLE);
					return;
				}

				if (partRepayRb.isChecked()) {
					amount = repayAmountEt.getText().toString().trim();
					boolean flag = judgeUserData(amount, false);
					if (!flag) {
						return;
					}
				}
				// 查询第二币种详情
				Intent intent1 = getIntent();
				if (intent1 != null) {
					otherPartJumpFlag = intent1.getIntExtra(
							ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
				}
				if (StringUtil.isNullOrEmpty(otherPartJumpFlag)) {
					return;
				}
				if (otherPartJumpFlag == 0) {
					// 其他模块
					TranDataCenter.getInstance().setAccInInfoMap(addInforMap);
				} else {

				}
				Map<String, String> userInputMap = new HashMap<String, String>();
				userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
				userInputMap.put(Tran.INPUT_CURRENCY_CODE, crcdCurrency2);
				userInputMap.put(Tran.CREDITCARD_FOREIGN_CRCDAUTOREPAYMODE_REQ,
						repayAmountSet);
				TranDataCenter.getInstance().setUserInputMap(userInputMap);
				// 请求CrcdForeignPayOff
				// Map<String, Object> accInInfoMap =
				// TranDataCenter.getInstance()
				// .getAccInInfoMap();
				// toPayeeId = (String) accInInfoMap.get(Comm.ACCOUNT_ID);
				//
				// BaseHttpEngine.showProgressDialog();
				// requestCommConversationId();
				Intent intent = new Intent(TransferManagerActivity1.this,
						RelCreditCardRemitConfirmActivity1.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * 初始化底部按钮 立即执行 预约日期执行 预约周期执行
	 */
	private void initRelBottomBtn(View v) {
		Map<String, Object> map = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		String accOuttype = (String) map.get(Comm.ACCOUNT_TYPE);
		LinearLayout exeDate = (LinearLayout) v.findViewById(R.id.ll_exeDate);
		Button btn_next = (Button) v.findViewById(R.id.btn_boc_next);
		LinearLayout ll_fuyan = (LinearLayout) v.findViewById(R.id.ll_fuyan);
		if (accOuttype.equals(ConstantGloble.ACC_TYPE_BOCINVT)) {
			// 专属理财账户
			tranTypeFlag = TRANTYPE_BOC_ACCOUNT;
			exeDate.setVisibility(View.GONE);
			btn_next.setVisibility(View.VISIBLE);
			ll_fuyan.setVisibility(View.GONE);
			btn_next.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 资金转出
					amount = amountEt.getText().toString().trim();
					if (StringUtil.isNullOrEmpty(currencyCode)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getString(R.string.no_same_currency));
						return;
					}
					boolean flag = judgeUserData(amount, false);
					if (!flag) {
						return;
					}
					Map<String, Object> acctOFmap = TranDataCenter
							.getInstance().getAccmap();
					BociDataCenter.getInstance().setAcctOFmap(acctOFmap);
					startActivity(new Intent(TransferManagerActivity1.this,
							TransferConfirmActivity.class)
							.putExtra(BocInvt.AMOUNT, amount)
							.putExtra(BocInvt.BOCINVT_CANCEL_CURRENCY_REQ,
									currencyCode)
							.putExtra(BocInvt.BOCINVT_CANCEL_CASHREMIT_RES,
									cashRemitCode));
				}
			});

		} else {
			tranTypeFlag = TRANTYPE_REL_ACCOUNT;
			exeDate.setVisibility(View.VISIBLE);
			btn_next.setVisibility(View.GONE);
			ll_fuyan.setVisibility(View.VISIBLE);
			/** 关联转账 立即执行 */
			Button relNowExeBtn = (Button) v
					.findViewById(R.id.btn_rel_trans_nowExe_tranSeting);
			relNowExeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					amount = amountEt.getText().toString().trim();
					memo = remarkEt.getText().toString().trim();
					// 转账金额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成
					tranMode = ConstantGloble.NOWEXE;
					if (StringUtil.isNullOrEmpty(currencyList)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getString(R.string.no_same_currency));
						return;
					}
					boolean flag = judgeUserData(amount, true);
					if (!flag) {
						return;
					}
					// 保存用户输入数据
					saveUserData();
					// 手续费试算接口
					requestForTransferCommissionCharge(ConstantGloble.PB021);
				}
			});
			/** 关联转账 预约日期执行 */
			Button relPreDateBtn = (Button) v
					.findViewById(R.id.btn_rel_trans_preDateExe_tranSeting);
			relPreDateBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					amount = amountEt.getText().toString().trim();
					memo = remarkEt.getText().toString().trim();
					tranMode = ConstantGloble.PREDATEEXE;
					if (StringUtil.isNullOrEmpty(currencyList)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getString(R.string.no_same_currency));
						return;
					}
					boolean flag = judgeUserData(amount, false);
					if (!flag) {
						return;
					}
					// 保存用户输入数据
					saveUserData();
					// 手续费试算接口
					BaseHttpEngine.showProgressDialog();
					requestSystemDateTime();
					// requestForTransferCommissionCharge(ConstantGloble.PB021);
				}
			});
			/** 关联转账 预约周期执行 */
			Button relPrePeriodBtn = (Button) v
					.findViewById(R.id.btn_rel_trans_prePeriodExe_tranSeting);
			relPrePeriodBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					amount = amountEt.getText().toString().trim();
					memo = remarkEt.getText().toString().trim();
					tranMode = ConstantGloble.PREPERIODEXE;
					if (StringUtil.isNullOrEmpty(currencyList)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getString(R.string.no_same_currency));
						return;
					}
					boolean flag = judgeUserData(amount, false);
					if (!flag) {
						return;
					}
					// 保存用户输入数据
					saveUserData();
					// 手续费试算接口
					BaseHttpEngine.showProgressDialog();
					requestSystemDateTime();
					// requestForTransferCommissionCharge(ConstantGloble.PB021);
				}
			});
		}
	}

	/**
	 * 非关联信用卡还款视图
	 * 
	 * @param v
	 */
	private void initNoRelCrcdRepayView(View v) {
		Button commCreditCardNextBtn = (Button) v
				.findViewById(R.id.btn_next_comm_creditCard_transSeting);
		final EditText repayAmountEt = (EditText) v
				.findViewById(R.id.et_repayAmountValue_comm_creditCard_transSeting);
		final EditText memoTv = (EditText) v.findViewById(R.id.tran_memo_tv);
		
		EditTextUtils.setLengthMatcher(this, memoTv, 20);// 非关联信用卡还款附言改为20字符

		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(repayAmountEt,
				tv_for_amount);
		/** 常用信用卡 短信通知收款人复选框 */
		final CheckBox sendSmcToPayeeCk = (CheckBox) v
				.findViewById(R.id.ck_sendSmcPayee_comm_creditCard_transSeting);
		/** 收款人手机号 */
		payeeMobileEt = (EditText) v
				.findViewById(R.id.et_payeeMobile_comm_creditCard_transSeting);
		final LinearLayout sendSmcToPayeeLl = (LinearLayout) v
				.findViewById(R.id.ll_payeeMobile_comm_creditCard_tranSeting);
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
		commCreditCardNextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!checkRMBMessage()) {
					return;
				}
				;
				amount = repayAmountEt.getText().toString().trim();
				memo = memoTv.getText().toString().trim();
				payeeMobile = payeeMobileEt.getText().toString().trim();
				tranMode = ConstantGloble.NOWEXE;
				boolean flag = judgeUserData(amount, false);
				if (!flag) {
					return;
				}
				Map<String, String> userInputMap = new HashMap<String, String>();
				userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
				userInputMap.put(Tran.INPUT_TRANSFER_REMARK, memo);
				userInputMap.put(ConstantGloble.IS_SEND_SMC, isSendSmc);
				userInputMap.put(ConstantGloble.IS_SAVE_PAYEE, isSavePayee);
				if (isSendSmc.equals(ConstantGloble.TRUE)) {
					userInputMap.put(Tran.INPUT_PAYEE_TRANSFER_MOBILE,
							payeeMobile);
				} else {
					userInputMap.put(Tran.INPUT_PAYEE_TRANSFER_MOBILE, "");
				}
				TranDataCenter.getInstance().setUserInputMap(userInputMap);
				// 请求安全因子
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}
		});

	}

	/**
	 * 初始化行内转转底部按钮
	 * 
	 * @param view
	 */
	private void initBankInBtnBottom(View view) {
		/** 常用行内 立即执行 */
		Button nowExeBtn = (Button) view
				.findViewById(R.id.btn_commBoc_nowExe_tranSeting);
		nowExeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!checkRMBMessage()) {
					return;
				}
				;
				amount = amountEt.getText().toString().trim();
				memo = remarkEt.getText().toString().trim();
				payeeMobile = payeeMobileEt.getText().toString().trim();
				// 转账金额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成
				tranMode = ConstantGloble.NOWEXE;
				boolean flag = judgeUserData(amount, true);
				if (!flag) {
					return;
				}
				// 保存用户输入数据
				saveBankInUserData();
				// 如果收入了手机号 还要放入手机号
				// 先调预交易接口 再掉手续费接口
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
				// 手续费试算接口
				// requestForTransferCommissionCharge(ConstantGloble.PB031);

			}
		});
		/** 常用行内 预约日期执行 */
		Button preDateExeBtn = (Button) view
				.findViewById(R.id.btn_commBoc_preDateExe_tranSeting);
		preDateExeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!checkRMBMessage()) {
					return;
				}
				;
				amount = amountEt.getText().toString().trim();
				memo = remarkEt.getText().toString().trim();
				payeeMobile = payeeMobileEt.getText().toString().trim();
				// 转账金额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成
				tranMode = ConstantGloble.PREDATEEXE;
				boolean flag = judgeUserData(amount, false);
				if (!flag) {
					return;
				}
				// 保存用户输入数据
				saveBankInUserData();
				// 先调预交易接口 再掉手续费接口
				BaseHttpEngine.showProgressDialog();
				requestSystemDateTime();
				// 手续费试算接口
				// requestForTransferCommissionCharge(ConstantGloble.PB031);
			}
		});
		/** 常用行内 预约周期执行 */
		Button prePeriodExeBtn = (Button) view
				.findViewById(R.id.btn_commBoc_prePeriodExe_tranSeting);
		prePeriodExeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!checkRMBMessage()) {
					return;
				}
				;
				amount = amountEt.getText().toString().trim();
				memo = remarkEt.getText().toString().trim();
				payeeMobile = payeeMobileEt.getText().toString().trim();
				// 转账金额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成
				tranMode = ConstantGloble.PREPERIODEXE;
				boolean flag = judgeUserData(amount, false);
				if (!flag) {
					return;
				}
				// 保存用户输入数据
				saveBankInUserData();
				// 手续费试算接口
				BaseHttpEngine.showProgressDialog();
				requestSystemDateTime();
				// requestForTransferCommissionCharge(ConstantGloble.PB031);
			}
		});
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		if (!StringUtil.isNullOrEmpty(TranDataCenter.getInstance()
				.getCommAccInList())) {
			if (TranDataCenter.getInstance().getCommAccInList()
					.contains(accInInfoMap)) {
				// 非关联账户转账 隐藏预定日期转账功能
				preDateExeBtn.setVisibility(View.GONE);
				prePeriodExeBtn.setVisibility(View.GONE);
			}
		}
		// 如果是从新增收款人 进入该界面 行内转账 按 非关联 处理 隐藏预定日期转账功能
		if (addNewPayeeFlag) {
			preDateExeBtn.setVisibility(View.GONE);
			prePeriodExeBtn.setVisibility(View.GONE);
		}
		// 如果是行内定向转账 隐藏预约日期执行 和预约周期执行
		if (tranTypeFlag == TRANTYPE_DIR_BANKIN) {
			preDateExeBtn.setVisibility(View.GONE);
			prePeriodExeBtn.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化跨行转账底部按钮监听
	 * 
	 * @param view
	 */
	private void initBankOtherBottom(View view) {
		/** 跨行 立即执行 */
		Button nowExeBtn = (Button) view
				.findViewById(R.id.btn_commBoc_nowExe_tranSeting);
		nowExeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!checkRMBMessage()) {
					return;
				}
				;
				amount = amountEt.getText().toString().trim();
				memo = remarkEt.getText().toString().trim();
				payeeMobile = payeeMobileEt.getText().toString().trim();

				// 判断转账金额是否大于5万
				if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
					if (!moneyLowFiveMiriade(amount)) {
						return;
					}
				}
				// 转账金额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成
				tranMode = ConstantGloble.NOWEXE;
				// 判断是否有开户行,如果没有报错

				if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {

					if (containerV1.getVisibility() == View.VISIBLE) {
						boolean addressflag = judgeBank();
						if (!addressflag) {
							return;
						}

					}
//					if(rb_shishi!=null){
//						if (!rb_shishi.isChecked()) {
//							boolean addressflag = judgeAddress();
//							if (!addressflag) {
//								return;
//							}
//						}	
//					}
//					
//					if(radio_shishi_orientation!=null){
//						if (!radio_shishi_orientation.isChecked()) {
//							boolean addressflag = judgeAddress();
//							if (!addressflag) {
//								return;
//							}
//						}	
//					}
					
				} else if (tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {
					if (containerV1.getVisibility() == View.VISIBLE) {
						boolean addressflag = judgeBank();
						if (!addressflag) {
							return;
						}

					}
//					if (!radio_shishi_orientation.isChecked()) {
//						boolean addressflag = judgeAddress();
//						if (!addressflag) {
//							return;
//						}
//					}
				} else if (tranTypeFlag == TRANTYPE_NOREL_BANKOTHER) {
					boolean outflag = judgeOut();

					if (outflag) {
						return;
					}
					
					if (rb_common.isChecked()) {
						boolean addressflag = judgeAddress();
						if (!addressflag) {
							return;
						}
					}
				} else if (tranTypeFlag == TRANTYPE_DIR_BANKOTHER) {

					boolean outflag = judgeOut();

					if (outflag) {
						return;
					}
					if (radio_common_orientation.isChecked()) {
						boolean addressflag = judgeAddress();
						if (!addressflag) {
							return;
						}
					}
				}

				boolean flag = judgeUserData(amount, true);
				if (!flag) {
					return;
				}

				// if(!common_can_shishi){
				// BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.tran_error_shishi));
				// return;
				// }
				// 保存用户输入数据
				saveBankInUserData();
				// 手续费试算接口
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();

			}
		});
		/** 跨行 预约日期执行 */
		Button preDateExeBtn = (Button) view
				.findViewById(R.id.btn_commBoc_preDateExe_tranSeting);
		preDateExeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!checkRMBMessage()) {
					return;
				}
				;
				amount = amountEt.getText().toString().trim();
				memo = remarkEt.getText().toString().trim();
				payeeMobile = payeeMobileEt.getText().toString().trim();
				// 判断转账金额是否大于5万
				if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
					if (!moneyLowFiveMiriade(amount)) {
						return;
					}
				}
				// 转账金额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成
				tranMode = ConstantGloble.PREDATEEXE;
				// 判断是否有开户行,如果没有报错

//				if (!rb_shishi.isChecked()) {
//					boolean addressflag = judgeAddress();
//					if (!addressflag) {
//						return;
//					}
//				}
				boolean flag = judgeUserData(amount, false);
				if (!flag) {
					return;
				}
				// 保存用户输入数据
				saveBankInUserData();
				// 手续费试算接口
				BaseHttpEngine.showProgressDialog();
				requestSystemDateTime();
			}
		});
		// 从新增收款人返回的 属于
		if (addNewPayeeFlag) {
			preDateExeBtn.setVisibility(View.GONE);
		}
		// 跨行转账 隐藏 预约日期执行按钮
		if (tranTypeFlag == TRANTYPE_NOREL_BANKOTHER) {
			preDateExeBtn.setVisibility(View.GONE);
		}
		// 跨行实时转账 隐藏 预约日期执行
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		if (!StringUtil.isNullOrEmpty(TranDataCenter.getInstance()
				.getEbpsList())) {
			if (TranDataCenter.getInstance().getEbpsList()
					.contains(accInInfoMap)) {
				preDateExeBtn.setVisibility(View.GONE);
			}
		}
		// 跨行定向转账 隐藏 预约日期执行
		if (tranTypeFlag == TRANTYPE_DIR_BANKOTHER) {
			preDateExeBtn.setVisibility(View.GONE);
		}

	}

	private boolean moneyLowFiveMiriade(String money) {
		if (!StringUtil.isNull(money)) {
			double dmoney = Double.parseDouble(money);
			if (dmoney > 50000) {
				BaseDroidApp.getInstanse().showMessageDialog(
						"实时转账的交易金额不能高于5万元", new OnClickListener() {
							@Override
							public void onClick(View v) {
								amountEt.getText().clear();
								BaseDroidApp.getInstanse()
										.dismissMessageDialog();
							}
						});
				return false;
			}
		}

		return true;
	}

	/**
	 * 判断跨行是否有开户行
	 * 
	 * @return
	 */
	public boolean judgeAddress() {
		String toOrgName = inBankNameEt.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(toOrgName)) {
			String errorInfo = getResources().getString(
					R.string.inbank_name_error);
			BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
			return false;
		}
		return true;
	}

	/**
	 * 判断转出账户类型
	 * 
	 * @return sunh
	 */
	private boolean judgeOut() {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		// 转出账户类型
		String outAccountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
		if (ConstantGloble.ZHONGYIN.equals(outAccountType)) {
			String errorInfo = getResources().getString(
					R.string.out_acctypegre_error);
			BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 判断跨行是否有所属行
	 * 
	 * @return sunh
	 */
	public boolean judgeBank() {
		String toOrgName = isBankNameEt.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(toOrgName)) {
			String errorInfo = getResources().getString(
					R.string.shishibank_name_error);
			BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
			return false;
		}
		return true;
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
		// 找出列表中所有不相同的币种8
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

		// List<Map<String, Object>> cds = (List<Map<String, Object>>)
		// currencyAndCashRemitList
		// .get(0).get(ConstantGloble.TRAN_CASHREMIT_AND_CONTENT);
		// for (int i = 0; i < cds.size(); i++) {
		// String cashRemit = (String) cds.get(i).get(
		// ConstantGloble.TRAN_CASHREMIT);
		// cashRemitList.add(cashRemit);
		// }
	}

	private void combineCrcdData() {
		if (StringUtil.isNullOrEmpty(currencyList)) {
			currencyList = new ArrayList<String>();
		} else {
			currencyList.clear();
		}

		currencyList.add(ConstantGloble.PRMS_CODE_RMB);
	}

	/**
	 * 判断用户输入数据
	 * 
	 * @param isMountCompare
	 *            是否比较金额有效余额
	 * @return true 合格 false 不合格
	 */
	private boolean judgeUserData(String amount, boolean isMountCompare) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean transAmountReg = checkJapCkReg(currencyCode, getResources()
				.getString(R.string.reg_transferAmount), amount);
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
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		Map<String, Object> acInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		// 根据转出账户类型不同 对余额的判断方式也不同
		String outAccountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
		String inAccountType = (String) acInInfoMap.get(Comm.ACCOUNT_TYPE);

		Map<String, Object> detailMap = TranDataCenter.getInstance()
				.getCurrOutDetail();
		if (isMountCompare && detailMap != null) {
			// 转出是信用卡 需要判断账户余额 状态是否是存款余额
			if (ConstantGloble.ZHONGYIN.equals(outAccountType)
					|| ConstantGloble.GREATWALL.equals(outAccountType)
					|| ConstantGloble.SINGLEWAIBI.equals(outAccountType)) {
				if (ConstantGloble.ZHONGYIN.equals(inAccountType)
						|| ConstantGloble.SINGLEWAIBI.equals(inAccountType)
						|| ConstantGloble.GREATWALL.equals(inAccountType)
						|| ConstantGloble.ACC_TYPE_BRO.equals(inAccountType)) {
					String availableAmount = "";
					for (int i = 0; i < outCreditCurrencyAndBalance.size(); i++) {
						if (outCreditCurrencyAndBalance.get(i).get("currency")
								.equals(currencyCode)) {
							String availableAmount1 = (String) outCreditCurrencyAndBalance
									.get(i).get("loanBalanceLimit");
							if (!outCreditCurrencyAndBalance.get(i)
									.get("loanBalanceLimitFlag").equals("1")) {
								availableAmount1 = "0";
							}
							if (availableAmount1 != null
									&& Double.parseDouble(amount) > Double
											.parseDouble(availableAmount1)) {
								BaseDroidApp
										.getInstanse()
										.showInfoMessageDialog(
												getString(R.string.amount_wrong_two));
								return false;
							}
						}
					}
				} else {

					if (detailMap.get(Tran.ACCOUNTDETAIL_ACCOUNTDETAILIST_RES) != null) {
						List<Map<String, Object>> detailList = (List<Map<String, Object>>) detailMap
								.get(Tran.ACCOUNTDETAIL_ACCOUNTDETAILIST_RES);
						for (int i = 0; i < detailList.size(); i++) {
							Map<String, Object> detaimap = detailList.get(i);
							String cCode = (String) detaimap
									.get(Tran.CURRENCYCODE_RES);
							String cashRemit = (String) detaimap
									.get(Tran.ACCOUNTDETAIL_CASHREMIT_RES);
							if (cCode != null && cCode.equals(currencyCode)
									&& cashRemit != null
									&& cashRemit.equals(cashRemitCode)) {
								String availableAmount = (String) detaimap
										.get(Dept.AVAILABLE_BALANCE);
								if (availableAmount != null
										&& Double.parseDouble(amount) > Double
												.parseDouble(availableAmount)) {
									BaseDroidApp
											.getInstanse()
											.showInfoMessageDialog(
													getString(R.string.amount_wrong_two));
									return false;
								}
								continue;
							}
						}
					} else if (detailMap.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST) != null) {
						if (availableBalance != null
								&& Double.parseDouble(amount) > Double
										.parseDouble(availableBalance)) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									getString(R.string.amount_wrong_two));
							return false;
						}
					}

				}
			} else if (ConstantGloble.ACC_TYPE_BRO.equals(outAccountType)) {
				// 转出是 非 信用卡 需要判 钞汇标识
				if (ConstantGloble.GREATWALL.equals(inAccountType)
						|| ConstantGloble.ZHONGYIN.equals(inAccountType)
						|| ConstantGloble.SINGLEWAIBI.equals(inAccountType)) {
					if (detailMap.get(Tran.ACCOUNTDETAIL_ACCOUNTDETAILIST_RES) != null) {
						List<Map<String, Object>> detailList = (List<Map<String, Object>>) detailMap
								.get(Tran.ACCOUNTDETAIL_ACCOUNTDETAILIST_RES);
						for (int i = 0; i < detailList.size(); i++) {
							Map<String, Object> detaimap = detailList.get(i);
							String cCode = (String) detaimap
									.get(Tran.CURRENCYCODE_RES);
							String cashRemit = (String) detaimap
									.get(Tran.ACCOUNTDETAIL_CASHREMIT_RES);
							if (cCode != null && cCode.equals(currencyCode)
									&& cashRemit != null
									&& cashRemit.equals(cashRemitCode)) {
								String availableAmount = (String) detaimap
										.get(Dept.AVAILABLE_BALANCE);
								if (availableAmount != null
										&& Double.parseDouble(amount) > Double
												.parseDouble(availableAmount)) {
									BaseDroidApp
											.getInstanse()
											.showInfoMessageDialog(
													getString(R.string.amount_wrong_two));
									return false;
								}
								continue;
							}
						}
					} else if (detailMap.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST) != null) {
						if (availableBalance != null
								&& Double.parseDouble(amount) > Double
										.parseDouble(availableBalance)) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									getString(R.string.amount_wrong_two));
							return false;
						}
					}
				} else {

					if (detailMap.get(Tran.ACCOUNTDETAIL_ACCOUNTDETAILIST_RES) != null) {
						List<Map<String, Object>> detailList = (List<Map<String, Object>>) detailMap
								.get(Tran.ACCOUNTDETAIL_ACCOUNTDETAILIST_RES);
						for (int i = 0; i < detailList.size(); i++) {
							Map<String, Object> detaimap = detailList.get(i);
							String cCode = (String) detaimap
									.get(Tran.CURRENCYCODE_RES);
							String cashRemit = (String) detaimap
									.get(Tran.ACCOUNTDETAIL_CASHREMIT_RES);
							if (cCode != null && cCode.equals(currencyCode)
									&& cashRemit != null
									&& cashRemit.equals(cashRemitCode)) {
								String availableAmount = (String) detaimap
										.get(Dept.AVAILABLE_BALANCE);
								if (availableAmount != null
										&& Double.parseDouble(amount) > Double
												.parseDouble(availableAmount)) {
									BaseDroidApp
											.getInstanse()
											.showInfoMessageDialog(
													getString(R.string.amount_wrong_two));
									return false;
								}
								continue;
							}
						}
					} else if (detailMap.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST) != null) {
						if (availableBalance != null
								&& Double.parseDouble(amount) > Double
										.parseDouble(availableBalance)) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									getString(R.string.amount_wrong_two));
							return false;
						}
					}

				}
			} else {
				if (detailMap.get(Tran.ACCOUNTDETAIL_ACCOUNTDETAILIST_RES) != null) {
					List<Map<String, Object>> detailList = (List<Map<String, Object>>) detailMap
							.get(Tran.ACCOUNTDETAIL_ACCOUNTDETAILIST_RES);
					for (int i = 0; i < detailList.size(); i++) {
						Map<String, Object> detaimap = detailList.get(i);
						String cCode = (String) detaimap
								.get(Tran.CURRENCYCODE_RES);
						String cashRemit = (String) detaimap
								.get(Tran.ACCOUNTDETAIL_CASHREMIT_RES);
						if (cCode != null && cCode.equals(currencyCode)
								&& cashRemit != null
								&& cashRemit.equals(cashRemitCode)) {
							String availableAmount = (String) detaimap
									.get(Dept.AVAILABLE_BALANCE);
							if (availableAmount != null
									&& Double.parseDouble(amount) > Double
											.parseDouble(availableAmount)) {
								BaseDroidApp
										.getInstanse()
										.showInfoMessageDialog(
												getString(R.string.amount_wrong_two));
								return false;
							}
							continue;
						}
					}
				} else if (detailMap.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST) != null) {
					if (availableBalance != null
							&& Double.parseDouble(amount) > Double
									.parseDouble(availableBalance)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getString(R.string.amount_wrong_two));
						return false;
					}
				}

			}
		}
		return true;
	}

	/**
	 * 保存用户输入数据
	 */
	private void saveUserData() {
		String accountInType = (String) TranDataCenter.getInstance()
				.getAccInInfoMap().get(Comm.ACCOUNT_TYPE);
		String accountOutType = (String) TranDataCenter.getInstance()
				.getAccOutInfoMap().get(Comm.ACCOUNT_TYPE);
		Map<String, String> userInputMap = new HashMap<String, String>();
		userInputMap.put(Tran.INPUT_CURRENCY_CODE, currencyCode);

		if (ConstantGloble.ZHONGYIN.equals(accountInType)
				|| ConstantGloble.GREATWALL.equals(accountInType)
				|| ConstantGloble.SINGLEWAIBI.equals(accountInType)) {
			if (ConstantGloble.ZHONGYIN.equals(accountOutType)
					|| ConstantGloble.GREATWALL.equals(accountOutType)
					|| ConstantGloble.SINGLEWAIBI.equals(accountOutType)) {
				cashRemitCode = "00";
			}
		}
		userInputMap.put(Tran.INPUT_CASHREMIT_CODE, cashRemitCode);
		userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
		userInputMap.put(Tran.INPUT_TRANSFER_REMARK, memo);
		userInputMap.put(Tran.MANAGE_PRE_transMode_RES, tranMode);
		TranDataCenter.getInstance().setUserInputMap(userInputMap);
	}

	/**
	 * 行内转账用户输入数据
	 */
	private void saveBankInUserData() {
		Map<String, String> userInputMap = new HashMap<String, String>();
		userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
		userInputMap.put(Tran.INPUT_TRANSFER_REMARK, memo);
		userInputMap.put(ConstantGloble.IS_SEND_SMC, isSendSmc);
		userInputMap.put(Tran.INPUT_ADD_PAYEE_FALG, isAddPayeeChecked);
		userInputMap.put(Tran.MANAGE_PRE_transMode_RES, tranMode);
		if (isSendSmc.equals(ConstantGloble.TRUE)) {
			userInputMap.put(Tran.INPUT_PAYEE_TRANSFER_MOBILE, payeeMobile);
		} else {
			userInputMap.put(Tran.INPUT_PAYEE_TRANSFER_MOBILE, "");
		}
		TranDataCenter.getInstance().setUserInputMap(userInputMap);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE
				&& resultCode == RESULT_OK) {// 关联账户转账过来
			requestTranoutAccountList();
		} else if (requestCode == Tran.WRITEPAYEEINFOACTIVITY_REQUEST_CODE) {// 新增收款人

			if (resultCode == 102) { // 为行内情况
				accInDetailLayout = (LinearLayout) inflater.inflate(
						R.layout.dept_acc_in_list_detail, null);
				mAccInLl.removeAllViews();
				mAccInLl.addView(accInDetailLayout);
				mAccInLl.setVisibility(View.VISIBLE);
				// mAccInListLayout.setVisibility(View.GONE);
				newAddTranInBtn.setVisibility(View.GONE);
				showAccInListItemData();// 显示转入账户条目的详细信息
				isTranInFirst = false;
				// 标识是从新增收款人界面返回
				addNewPayeeFlag = true;
				tranTypeFlag = TRANTYPE_NOREL_BANKIN;
				initBankInView();
			} else if (resultCode == 103) {// 为跨行情况
				boolean isshishi = data.getBooleanExtra(ISSHISHITYPE, false);
				accInDetailLayout = (LinearLayout) inflater.inflate(
						R.layout.dept_acc_in_list_detail, null);
				mAccInLl.removeAllViews();
				mAccInLl.addView(accInDetailLayout);
				mAccInLl.setVisibility(View.VISIBLE);
				// mAccInListLayout.setVisibility(View.GONE);
				newAddTranInBtn.setVisibility(View.GONE);
				showAccInListItemData();// 显示转入账户条目的详细信息
				isTranInFirst = false;
				// 标识是从新增收款人界面返回
				addNewPayeeFlag = true;
				if (isshishi) {
					tranTypeFlag = TRANTYPE_SHISHI_BANKOTHER;
				} else {
					tranTypeFlag = TRANTYPE_NOREL_BANKOTHER;
				}
				// initBankOtherView();
				initBankOtherCommonView();
			}
		} else if (requestCode == ConstantGloble.PAYEE_OTHERBANK
				&& resultCode == RESULT_OK) {
			// 开户行查询返回
			String toOrgName = data
					.getStringExtra(Tran.TRANS_BOCNATIONAL_TOORGNAME_REQ);
			String orgNameCnapsCode = data
					.getStringExtra(Tran.PAYEE_CNAPSCODE_REQ);
			Map<String, Object> map = TranDataCenter.getInstance()
					.getAccInInfoMap();
//			if(StringUtil.isNull(LocalData.kBankListMap.get(toOrgName))){
//				// 不是常用银行 全部设置为其他银行
//				map.put(Tran.TRANS_ADDRESS_RES, "其它银行");
//				map.put(Tran.PAYEE_CNAPSCODE_REQ, "OTHER");
//			}else{
				map.put(Tran.TRANS_ADDRESS_RES, toOrgName);
				map.put(Tran.PAYEE_CNAPSCODE_REQ, orgNameCnapsCode);	
//			}
				TranDataCenter.getInstance().setAccInInfoMap(map);
			inBankNameEt.setVisibility(View.VISIBLE);
			inBankNameEt.setText(toOrgName);
		} else if (requestCode == ConstantGloble.CHOOSE_SHISHIBANK_NEW) {
			if (resultCode == RESULT_OK) {

				// 要修改 sunh
				// 所属银行查询返回
				String toOrgName = data
						.getStringExtra(Tran.TRANS_BOCNATIONAL_TOORGNAME_REQ);
				String orgNameCnapsCode = data
						.getStringExtra(Tran.PAYEE_CNAPSCODE_REQ);
				isBankNameEt.setVisibility(View.VISIBLE);
				isBankNameEt.setText(toOrgName);			
				Map<String, Object> map = TranDataCenter.getInstance()
						.getAccInInfoMap();
//				map.put(Tran.SAVEEBPS_PAYEEORGNAME_REQ, toOrgName);
				map.put(Tran.SAVEEBPS_PAYEEBANKNAME_REQ, toOrgName);
				map.put(Tran.EBPSQUERY_PAYEECNAPS_REQ, orgNameCnapsCode);
//				map.put(Tran.MANAGE_PAYEELIST_BANKNAME_RES, toOrgName);
//				map.put(Tran.MANAGE_PAYEELIST_CNAPSCODE_RES, orgNameCnapsCode);
//				map.put(Tran.MANAGE_PAYEELIST_ADDRESS_RES, toOrgName);
				TranDataCenter.getInstance().setAccInInfoMap(map);;
				comm2shishi = orgNameCnapsCode;
				// TranDataCenter.getInstance().getAccInInfoMap().put(Tran.MANAGE_PAYEELIST_ADDRESS_RES,
				// toOrgName);
				// TranDataCenter.getInstance().getAccInInfoMap().put(Tran.EBPSQUERY_PAYEECNAPS_REQ,orgNameCnapsCode);

			}
		}

		else if (requestCode == ConstantGloble.CHOOSE_BANKNAME) {
			if (resultCode == RESULT_OK) {
				// 所属银行查询
				String bankName = data
						.getStringExtra(Tran.TRAN_DIR_QUERYLIST_BANKNAME_RES);
				Map<String, Object> map = TranDataCenter.getInstance()
						.getAccInInfoMap();
				
				map.put(Tran.TRAN_DIR_QUERYLIST_BANKNAME_RES, bankName);
				TranDataCenter.getInstance().setAccInInfoMap(map);
				refreshTranDir(bankName);
			} else {
				showAccInListDialog();
			}

		} else if (requestCode == ConstantGloble.CHOOSE_SHISHIBANK) {
			if (resultCode == RESULT_OK) {
				// 实时定向选择所属银行返回
				String toOrgName = data
						.getStringExtra(Tran.TRANS_BOCNATIONAL_TOORGNAME_REQ);
				String orgNameCnapsCode = data
						.getStringExtra(Tran.PAYEE_CNAPSCODE_REQ);
				Map<String, Object> map = TranDataCenter.getInstance()
						.getAccInInfoMap();
				map.put(Tran.SAVEEBPS_PAYEEORGNAME_REQ, toOrgName);
				map.put(Tran.SAVEEBPS_PAYEEBANKNAME_REQ, toOrgName);
				map.put(Tran.SAVEEBPS_PAYEECNAPS_REQ, orgNameCnapsCode);
				map.put(Tran.MANAGE_PAYEELIST_BANKNAME_RES, toOrgName);
				map.put(Tran.MANAGE_PAYEELIST_CNAPSCODE_RES, orgNameCnapsCode);
				map.put(Tran.MANAGE_PAYEELIST_ADDRESS_RES, toOrgName);
				TranDataCenter.getInstance().setAccInInfoMap(map);
				refreshTranDir(toOrgName);
			} else {
				showAccInListDialog();
			}
		}
	}

	// 通讯暂时放在下面
	// //////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 调用接口：PsnCommonQueryAllChinaBankAccount 请求给定类型转出账户列表
	 * */
	public void requestTranoutAccountList() {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.COMMONQUERYALLCHINABANKACCOUNT);

		String[] accountTypeArr = new String[] { ConstantGloble.ACC_TYPE_ORD,
				ConstantGloble.ACC_TYPE_BRO, ConstantGloble.ACC_TYPE_RAN,
				ConstantGloble.ACC_TYPE_GRE, ConstantGloble.ZHONGYIN,
				ConstantGloble.SINGLEWAIBI, ConstantGloble.ACC_TYPE_BOCINVT };
		// , AccBaseActivity.YOUHUITONGZH
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
		// ?是否需要添加提示信息，根据biiResponseBody的内容
		// 返回列表里面没有数据的时候 需要提示 modify by wjp
		if (StringUtil.isNullOrEmpty(result)) {// 如果没有符合要求的数据 弹框提示
			String message = this.getString(R.string.no_accout);
//			BaseDroidApp.getInstanse().showErrorDialog(message,
//					R.string.cancle, R.string.confirm, onNoOutclicklistener);
			BaseDroidApp.getInstanse().createDialog("", message, new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissErrorDialog();	
					BusinessModelControl.gotoAccRelevanceAccount(TransferManagerActivity1.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);				
				}
			});
			return;
		}
		List<Map<String, Object>> crcdList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> tranOut = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> bocinvtList = new ArrayList<Map<String, Object>>();
		Intent intent = getIntent();
		if (intent != null) {
			otherPartJumpFlag = intent.getIntExtra(
					ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
			if (StringUtil.isNullOrEmpty(otherPartJumpFlag)) {
				return;
			}
			if (otherPartJumpFlag == ConstantGloble.CREDIT_TO_TRAN) {
				// 信用卡还款
				// 信用卡购汇还款 ——转出账户可以为中银信用卡
				for (Map<String, Object> map : result) {
					String accType = (String) map.get(Acc.ACC_ACCOUNTTYPE_RES);
					// if (accType.equals(ConstantGloble.SINGLEWAIBI)) {
					// crcdList.add(map); // P601 关联信用卡还款 放开 107
					// } else
					if (accType.equals(ConstantGloble.ACC_TYPE_BOCINVT)) {
						bocinvtList.add(map);
					} else {
						tranOut.add(map);
					}
				}
			} else {
				for (Map<String, Object> map : result) {
					String accType = (String) map.get(Acc.ACC_ACCOUNTTYPE_RES);
					// if (accType.equals(ConstantGloble.ZHONGYIN)
					// || accType.equals(ConstantGloble.SINGLEWAIBI)) {
					// crcdList.add(map); // P601 转出放开 103 107
					// } else
					if (accType.equals(ConstantGloble.ACC_TYPE_BOCINVT)) {
						bocinvtList.add(map);
					} else {
						tranOut.add(map);
					}
				}
			}
		} else {
			for (Map<String, Object> map : result) {
				String accType = (String) map.get(Acc.ACC_ACCOUNTTYPE_RES);
				// if (accType.equals(ConstantGloble.ZHONGYIN)
				// || accType.equals(ConstantGloble.SINGLEWAIBI)) {
				// crcdList.add(map); // P601 转出放开 103 107
				// } else
				if (accType.equals(ConstantGloble.ACC_TYPE_BOCINVT)) {
					bocinvtList.add(map);
				} else {
					tranOut.add(map);
				}
			}
		}

		TranDataCenter.getInstance().setCrcdList(crcdList);
		TranDataCenter.getInstance().setAccountOutList(tranOut);
		TranDataCenter.getInstance().setBocinvtList(bocinvtList);

		showAccOutListDialog();
	}

	/**
	 * 查询信用卡详情 PsnCrcdQueryAccountDetail
	 * 
	 * @param accountId
	 */
	private void requestForCrcdDetail(String accountId) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.CRCDQUERYACCOUNTDETAIL);
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_ID, accountId);
		// 默认设置为人民币元
		// paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY,
		// ConstantGloble.BOCINVT_CURRENCY_RMB);
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY, null);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCrcdDetailCallBack");
	}

	// /** 查询转出账户 信用卡详情 */
	// private void requestForCrcdDetail(String accountId, String
	// outCrcdCurrency2) {
	// BaseHttpEngine.showProgressDialog();
	// BiiRequestBody biiRequestBody = new BiiRequestBody();
	// Map<String, String> paramsmap = new HashMap<String, String>();
	// biiRequestBody.setMethod(Tran.CRCDQUERYACCOUNTDETAIL);
	// paramsmap.put(Tran.QUERY_CONDITION_CRCD_ID, accountId);
	// // outCrcdCurrency2 为要查询的币种
	// paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY, outCrcdCurrency2);
	// biiRequestBody.setParams(paramsmap);
	// HttpManager.requestBii(biiRequestBody, this,
	// "requestForCrcdDetailCallBackTwo");
	// }
	//
	// /** 查询非人名币信用卡 详情 */
	// public void requestForCrcdDetailCallBackTwo(Object resultObj) {
	//
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// @SuppressWarnings("unchecked")
	// Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
	// .getResult();
	// if (StringUtil.isNullOrEmpty(resultMap)) {
	// BaseHttpEngine.dissMissProgressDialog();
	// return;
	// }
	//
	// List<Map<String, String>> detailList = new ArrayList<Map<String,
	// String>>();
	// detailList = (List<Map<String, String>>) resultMap
	// .get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
	// if (StringUtil.isNullOrEmpty(detailList)) {
	// BaseHttpEngine.dissMissProgressDialog();
	// return;
	// }
	//
	// // 取 信用卡外币 账户信息
	//
	// for (int i = 0; i < detailList.size(); i++) {
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("loanBalanceLimitFlag",
	// detailList.get(i).get("loanBalanceLimitFlag"));
	// map.put("loanBalanceLimit",
	// detailList.get(i).get("loanBalanceLimit"));
	// map.put("currency", detailList.get(i).get("currency"));
	// outCreditCurrencyAndBalance.add(map);
	// }
	//
	// Map<String, Object> accOutInfoMap = addOutforMap;
	// String accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
	// requestForCrcdDetail(accountId);
	// }

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

		// 显示转出账户详情界面
		if (outOrInFlag.equals(OUT)) {
			if (otherPartJumpFlag == ConstantGloble.CREDIT_TO_TRAN) {
				// 信用卡还款

				TranDataCenter.getInstance().setCurrOutDetail(resultMap);
				TranDataCenter.getInstance().setAccOutInfoMap(addOutforMap);
				BaseHttpEngine.dissMissProgressDialog();

				for (int i = 0; i < detailList.size(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("loanBalanceLimitFlag",
							detailList.get(i).get("loanBalanceLimitFlag"));
					map.put("loanBalanceLimit",
							detailList.get(i).get("loanBalanceLimit"));
					map.put("currency", detailList.get(i).get("currency"));
					outCreditCurrencyAndBalance.add(map);
				}
				showAccOutDetailView();

			} else {
				TranDataCenter.getInstance().setCurrOutDetail(resultMap);
				TranDataCenter.getInstance().setAccOutInfoMap(addOutforMap);
				BaseHttpEngine.dissMissProgressDialog();

				for (int i = 0; i < detailList.size(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("loanBalanceLimitFlag",
							detailList.get(i).get("loanBalanceLimitFlag"));
					map.put("loanBalanceLimit",
							detailList.get(i).get("loanBalanceLimit"));
					map.put("currency", detailList.get(i).get("currency"));
					if (StringUtil.isNullOrEmpty(outCrcdCurrency1)) {
						outCrcdCurrency1 = detailList.get(i).get("currency");
					} else {
						outCrcdCurrency2 = detailList.get(i).get("currency");
					}

					outCreditCurrencyAndBalance.add(map);
				}

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
				// 组装底部视图数据
				combineCrcdData();
				// 如果转入账户选定了 而且是关联账户转账 刷新底部视图
				if (!isTranInFirst && tranTypeFlag == TRANTYPE_REL_ACCOUNT) {
					initRelCrcdTran();
				}
//				// 如果转入账户选定了 而且是跨行 判断转出是否有人民币
//				if (!isTranInFirst && tranTypeFlag == TRANTYPE_NOREL_BANKOTHER
//						|| tranTypeFlag == TRANTYPE_DIR_BANKOTHER
//						|| tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER
//						|| tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {
//
//					Map<String, Object> accOutInfoMap = TranDataCenter
//							.getInstance().getAccOutInfoMap();
//					accTypeout = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
//					boolean cuttencyflag = getoutcurrey();// 转出是否有人民币
//					if (!cuttencyflag) {
//						// 转出无人民币
//						BaseDroidApp.getInstanse().showInfoMessageDialog(
//								getString(R.string.gouhui_no_cn));
//						return;
//					}
//
//				}

				// if( ConstantGloble.REL_CRCD_REPAY == otherPartJumpFlag ){
				//
				// //组装数据
				// combineCreditCrcdData();
				// //刷新 底部视图
				// initRelCrcdTran33();
				// }

				String accountOutType = (String) TranDataCenter.getInstance()
						.getAccOutInfoMap().get(Comm.ACCOUNT_TYPE);
				String accountInType = null;
				if (!StringUtil.isNullOrEmpty(TranDataCenter.getInstance()
						.getAccInInfoMap())) {
					accountInType = (String) TranDataCenter.getInstance()
							.getAccInInfoMap().get(Comm.ACCOUNT_TYPE);
				}

				// 非 信用卡购汇模块进入 才隐藏 钞汇标识
				if (otherPartJumpFlag == ConstantGloble.REL_CRCD_REPAY) {

					// 组装数据
					combineCreditCrcdData();
					// 刷新 底部视图
					initRelCrcdTran33();

				} else if (otherPartJumpFlag == ConstantGloble.REL_CRCD_BUY) {
					View crcdBuyView = (ViewGroup) mInflater
							.inflate(
									R.layout.tran_relation_credit_card_remit_trans_seting_mytransfer,
									null);
					initRelCrcdBuyView(crcdBuyView);
					bottomLayout.removeAllViews();
					bottomLayout.addView(crcdBuyView);
					return;
					// sunh 信用卡购汇 优化 购汇还款不走下边的
				}

				// 转出信用卡 转入是借记卡
				if (ConstantGloble.ZHONGYIN.equals(accountOutType)
						|| ConstantGloble.SINGLEWAIBI.equals(accountOutType)// 601
																			// 增加
																			// 107
						|| ConstantGloble.GREATWALL.equals(accountOutType)) {
					if (ConstantGloble.ACC_TYPE_BRO.equals(accountInType)) {
						initRelTran();
					}
				}
				// 转出是长城 借记卡 转入 是 中银 长城

				if (ConstantGloble.GREATWALL.equals(accountOutType)
						|| ConstantGloble.ACC_TYPE_BRO.equals(accountOutType)
						|| ConstantGloble.SINGLEWAIBI.equals(accountOutType)// 601
																			// 增加
																			// 107
						|| ConstantGloble.ZHONGYIN.equals(accountOutType)) {
					if (ConstantGloble.ZHONGYIN.equals(accountInType)
							|| ConstantGloble.GREATWALL.equals(accountInType)) {
						combineCreditCrcdData();
						initRelCrcdTran22(accountOutType);
					} else if (ConstantGloble.SINGLEWAIBI.equals(accountInType)) {
						combineSingleCredit();
						initRelCrcdTranSingleCredit();
					}

				}
			}

		} else if (outOrInFlag.equals(IN)) {// 显示转入账户详情界面
			TranDataCenter.getInstance().setCurrInDetail(resultMap);

			for (int i = 0; i < detailList.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("loanBalanceLimitFlag",
						detailList.get(i).get("loanBalanceLimitFlag"));
				map.put("loanBalanceLimit",
						detailList.get(i).get("loanBalanceLimit"));
				map.put("currency", detailList.get(i).get("currency"));
				inCreditCurrencyAndBalance.add(map);
			}
			crcdCurrency2 = "";
			// 查询信用卡币种
			Map<String, Object> accInInfoMap = addInforMap;
			String accountNumber = (String) accInInfoMap
					.get(Tran.ACCOUNTNUMBER_REQ);
			requestForCrcdCurrency(accountNumber);
		}
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
	@SuppressWarnings("unchecked")
	public void requestForAccountDetailCallBack(Object resultObj) {

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

		// 显示转出账户详情界面
		if (outOrInFlag.equals(OUT)) {

			if (otherPartJumpFlag == ConstantGloble.CREDIT_TO_TRAN) {
				// 信用卡还款
				TranDataCenter.getInstance().setCurrOutDetail(resultMap);
				TranDataCenter.getInstance().setAccOutInfoMap(addOutforMap);
				// 405 如果是专属理财账户，继续请求转入账户详情
				String tranOutType = (String) addOutforMap
						.get(Comm.ACCOUNT_TYPE);
				if (tranOutType.equals(ConstantGloble.ACC_TYPE_BOCINVT)) {
					outOrInFlag = IN;
					addNewPayeeFlag = false;// 点击收款人列表的时候 重置新增收款人flag
					isSendSmc = ConstantGloble.FALSE;// 重置短信通知收款人flag
					isSavePayee = ConstantGloble.FALSE;// 重置保存为常用收款人flag
					isAddPayeeChecked = ConstantGloble.FALSE;// 重置新增收款人flag
					Map<String, Object> bocinvtMap = TranDataCenter
							.getInstance().getAccmap();
					String accountId = (String) ((Map<String, Object>) bocinvtMap
							.get(BocInvt.BANKACCOUNT)).get(Comm.ACCOUNT_ID);
					addInforMap = (Map<String, Object>) bocinvtMap
							.get(BocInvt.BANKACCOUNT);
					requestForAccountDetail(accountId);
				} else {
					BaseHttpEngine.dissMissProgressDialog();
					if (tranOutType.equals(ConstantGloble.ACC_TYPE_BOCINVT)) {
						mAccInLl.setEnabled(false);
					} else {
						// 当转入账户是由信用卡模块进入时 转入账户点击事件 应该为false
						if (otherPartJumpFlag == ConstantGloble.CREDIT_TO_TRAN) {

							mAccInLl.setEnabled(false);
						} else {
							mAccInLl.setEnabled(true);
						}

					}
					showAccOutDetailView();

				}

			} else {
				TranDataCenter.getInstance().setCurrOutDetail(resultMap);
				TranDataCenter.getInstance().setAccOutInfoMap(addOutforMap);
				// 405 如果是专属理财账户，继续请求转入账户详情
				String tranOutType = (String) addOutforMap
						.get(Comm.ACCOUNT_TYPE);
				if (tranOutType.equals(ConstantGloble.ACC_TYPE_BOCINVT)) {
					outOrInFlag = IN;
					addNewPayeeFlag = false;// 点击收款人列表的时候 重置新增收款人flag
					isSendSmc = ConstantGloble.FALSE;// 重置短信通知收款人flag
					isSavePayee = ConstantGloble.FALSE;// 重置保存为常用收款人flag
					isAddPayeeChecked = ConstantGloble.FALSE;// 重置新增收款人flag
					Map<String, Object> bocinvtMap = TranDataCenter
							.getInstance().getAccmap();
					String accountId = (String) ((Map<String, Object>) bocinvtMap
							.get(BocInvt.BANKACCOUNT)).get(Comm.ACCOUNT_ID);
					addInforMap = (Map<String, Object>) bocinvtMap
							.get(BocInvt.BANKACCOUNT);
					requestForAccountDetail(accountId);
				} else {
					BaseHttpEngine.dissMissProgressDialog();
					if (tranOutType.equals(ConstantGloble.ACC_TYPE_BOCINVT)) {
						mAccInLl.setEnabled(false);
					} else {
						// 当转入账户是由信用卡模块进入时 转入账户点击事件 应该为false
						if (ConstantGloble.REL_CRCD_REPAY == otherPartJumpFlag
								|| ConstantGloble.ACC_TO_TRAN_REMIT == otherPartJumpFlag) {
							mAccInLl.setEnabled(false);
						} else {
							mAccInLl.setEnabled(true);
						}

					}
					showAccOutDetailView();
					// 组装数据
					combineData();
					if (!isTranInFirst && tranTypeFlag == TRANTYPE_REL_ACCOUNT) {
						initRelTran();
					}
					if (!isTranInFirst && tranTypeFlag == TRANTYPE_BOC_ACCOUNT) {
						initRelTran();
					}
					// 由信用卡模块进入 初始化底部视图 转出账户是 非 信用卡
					if (ConstantGloble.REL_CRCD_BUY == otherPartJumpFlag) {
						// //组装数据
						// combineData();
						//
						// // 由信用卡模块进入 转出账户是 非 信用卡 币种取二者的交集 取转出账户 钞汇标识
						// List<String> currencyList1 =new ArrayList<String>();
						// if(!StringUtil.isNull(inCrcdCurrency1)){
						// currencyList1.add(inCrcdCurrency1);
						// }
						//
						// if(!StringUtil.isNull(inCrcdCurrency2)){
						// currencyList1.add(inCrcdCurrency2);
						// }
						//
						// currencyList.retainAll(currencyList1);
						// 刷新 底部视图
						// initRelCrcdTran44();
						View crcdBuyView = (ViewGroup) mInflater
								.inflate(
										R.layout.tran_relation_credit_card_remit_trans_seting_mytransfer,
										null);
						initRelCrcdBuyView(crcdBuyView);
						return;
						// sunh 信用卡购汇 优化购汇还款不走下边的
					}
					// else if (ConstantGloble.REL_CRCD_REPAY ==
					// otherPartJumpFlag){
					// //组装数据
					// combineCreditCrcdData();
					// //刷新 底部视图
					// initRelCrcdTran33();
					// }
					String accountInType=null;
					if(!StringUtil.isNullOrEmpty(TranDataCenter
							.getInstance().getAccInInfoMap())){
						accountInType = (String) TranDataCenter
								.getInstance().getAccInInfoMap()
								.get(Comm.ACCOUNT_TYPE);	
					}
					
					String accountOutType = (String) TranDataCenter
							.getInstance().getAccOutInfoMap()
							.get(Comm.ACCOUNT_TYPE);
					if (!StringUtil.isNull(accountInType)) {
						if ((ConstantGloble.ACC_TYPE_GRE.equals(accountInType) || ConstantGloble.ZHONGYIN
								.equals(accountInType))
								&& (ConstantGloble.ACC_TYPE_BRO
										.equals(accountOutType)
										|| ConstantGloble.ACC_TYPE_GRE
												.equals(accountOutType)
										|| ConstantGloble.ZHONGYIN
												.equals(accountOutType) || ConstantGloble.SINGLEWAIBI
											.equals(accountOutType))) {
							// 组装数据
							combineCreditCrcdData();
							initRelCrcdTran22(accountOutType);
						} else if (ConstantGloble.SINGLEWAIBI
								.equals(accountInType)) {
							combineSingleCredit();
							initRelCrcdTranSingleCredit();
						}
						// 转出账户长城信用卡 转入账户借记卡
						if (isCreditRemit()) {
							initRelTran();
						}
					}
				}
			}

		} else {// 显示转入账户详情界面
			BaseHttpEngine.dissMissProgressDialog();
			TranDataCenter.getInstance().setCurrInDetail(resultMap);
			TranDataCenter.getInstance().setAccInInfoMap(addInforMap);
			BaseDroidApp.getInstanse().dismissMessageDialog();
			// 405 如果是专属理财账户,
			String tranOutType = (String) addOutforMap.get(Comm.ACCOUNT_TYPE);
			if (tranOutType.equals(ConstantGloble.ACC_TYPE_BOCINVT)) {
				isTranInFirst = false;
				mAccInLl.setEnabled(false);
				showAccOutDetailView();
				// 组装数据
				combineData();
			} else {
				mAccInLl.setEnabled(true);
			}
			showAccInDetailView();

			// 非信用卡 关联账户转账 改变底部视图
			// 如果转出账户是信用卡
			Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
					.getAccOutInfoMap();
			Map<String, Object> accInInfoMap1 = TranDataCenter.getInstance()
					.getAccInInfoMap();
			String accType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
			String accType1 = (String) accInInfoMap1.get(Comm.ACCOUNT_TYPE);

			if (accType.equals(ConstantGloble.ACC_TYPE_GRE)
					|| accType.equals(ConstantGloble.ZHONGYIN)// 601 1
					|| accType.equals(ConstantGloble.SINGLEWAIBI)// 601 增加 107
			) {
				// 判断转出账户 是否是信用卡 转入账户是否是借记卡
				if (isCreditRemit()) {
					initRelTran();
				} else {
					combineCrcdData();
					initRelCrcdTran();
				}

			} else {
				initRelTran();
			}
		}
	}

	/**
	 * 初始化底部视图 信用卡模块进入 非 信用卡还款底部视图
	 */
	/*
	 * private void initRelCrcdTran44() { View v = mInflater.inflate(
	 * R.layout.tran_rel_transfer_seting_credit_card_in, null); //
	 * initRelView11(v);
	 * 
	 * Button nextBtn = (Button) v
	 * .findViewById(R.id.btn_rel_self_creditCard_transSeting); final EditText
	 * repayAmountEt = (EditText) v
	 * .findViewById(R.id.et_repayAmountValue_relSelf_creditCard_transSeting);
	 * TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
	 * EditTextUtils.relateNumInputToChineseShower(repayAmountEt,
	 * tv_for_amount); nextBtn.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { // 判断有没有选择选出账户 if
	 * (isTranOutFirst) { CustomDialog.toastInCenter(context,
	 * TransferManagerActivity1.this .getString(R.string.choose_outno_message));
	 * return; }
	 * 
	 * amount = repayAmountEt.getText().toString().trim(); boolean flag =
	 * judgeUserData(amount, true); if (!flag) { return; }
	 * 
	 * Map<String, String> userInputMap = new HashMap<String, String>();
	 * userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
	 * TranDataCenter.getInstance().setUserInputMap(userInputMap); Intent intent
	 * = new Intent();
	 * intent.putExtra(BocInvt.BOCINVT_CANCEL_CURRENCY_REQ,currencyCode);
	 * intent.putExtra(Tran.ACCOUNTDETAIL_CASHREMIT_RES, cashRemitCode);
	 * intent.setClass(TransferManagerActivity1.this,
	 * RelSelfCreditCardConfirmActivity1.class); startActivity(intent); } });
	 * 
	 * final List<String> currencyCodeFlagList = new ArrayList<String>(); for
	 * (int i = 0; i < currencyList.size(); i++) { String currencyCode =
	 * currencyList.get(i);
	 * currencyCodeFlagList.add(LocalData.Currency.get(currencyCode)); } // 币种
	 * Spinner currencySp = (Spinner) v
	 * .findViewById(R.id.sp_rel_trans_currency_tranSeting); // 钞汇标志 final
	 * Spinner cashRemitSp = (Spinner) v
	 * .findViewById(R.id.sp_rel_trans_cashRemit_tranSeting); final List<String>
	 * cashRemitFlagList = new ArrayList<String>();
	 * 
	 * ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(
	 * TransferManagerActivity1.this, R.layout.custom_spinner_item,
	 * currencyCodeFlagList); currencyAdapter
	 * .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 * currencySp.setAdapter(currencyAdapter);
	 * 
	 * // 设置币种 currencySp.setOnItemSelectedListener(new OnItemSelectedListener()
	 * {
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public void onItemSelected(AdapterView<?> parent, View view,
	 * int position, long id) { currencyCode = currencyList.get(position); if
	 * (currencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {
	 * cashRemitSp.setEnabled(false); cashRemitSp
	 * .setBackgroundDrawable(TransferManagerActivity1.this
	 * .getResources().getDrawable( R.drawable.bg_spinner_default)); } else { if
	 * (isCreditRemit()) { cashRemitSp.setEnabled(false); cashRemitSp
	 * .setBackgroundDrawable(TransferManagerActivity1.this
	 * .getResources().getDrawable( R.drawable.bg_spinner_default)); } else {
	 * cashRemitSp.setEnabled(true); cashRemitSp
	 * .setBackgroundDrawable(TransferManagerActivity1.this
	 * .getResources().getDrawable( R.drawable.bg_spinner)); }
	 * 
	 * } currentCurrencyCashRemitList = (ArrayList<Map<String, Object>>)
	 * currencyAndCashRemitList .get(position).get(
	 * ConstantGloble.TRAN_CASHREMIT_AND_CONTENT); cashRemitFlagList.clear();
	 * cashRemitList.clear(); for (int i = 0; i <
	 * currentCurrencyCashRemitList.size(); i++) { String cashRemit = (String)
	 * currentCurrencyCashRemitList .get(i).get(ConstantGloble.TRAN_CASHREMIT);
	 * cashRemitList.add(cashRemit);
	 * cashRemitFlagList.add(LocalData.cashRemitBackMap .get(cashRemit)); }
	 * 
	 * ArrayAdapter<String> cashRemitAdapter = new ArrayAdapter<String>(
	 * TransferManagerActivity1.this, R.layout.custom_spinner_item,
	 * cashRemitFlagList); cashRemitAdapter
	 * .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 * cashRemitSp.setAdapter(cashRemitAdapter); cashRemitSp
	 * .setOnItemSelectedListener(cashRemitSelectedListener); }
	 * 
	 * @Override public void onNothingSelected(AdapterView<?> parent) {
	 * currencyCode = currencyList.get(0);// 默认上送人民币 } });
	 * 
	 * bottomLayout.removeAllViews(); bottomLayout.addView(v);
	 * 
	 * }
	 */
	/**
	 * 初始化底部视图 信用卡模块进入 信用卡还款底部视图
	 */
	private void initRelCrcdTran33() {
		View v = mInflater.inflate(
				R.layout.tran_rel_transfer_seting_credit_card_in, null);
		// initRelView11(v);

		Button nextBtn = (Button) v
				.findViewById(R.id.btn_rel_self_creditCard_transSeting);
		final EditText repayAmountEt = (EditText) v
				.findViewById(R.id.et_repayAmountValue_relSelf_creditCard_transSeting);
		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(repayAmountEt,
				tv_for_amount);
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断有没有选择选出账户
				if (isTranOutFirst) {
					CustomDialog.toastInCenter(context,
							TransferManagerActivity1.this
									.getString(R.string.choose_outno_message));
					mAccOutLl.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_card_new_red));
//					dept_out_info_layout.setVisibility(View.VISIBLE);
					return;
				}

				amount = repayAmountEt.getText().toString().trim();
				boolean flag = judgeUserData(amount, false);
				if (!flag) {
					return;
				}

				Map<String, String> userInputMap = new HashMap<String, String>();
				userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
				TranDataCenter.getInstance().setUserInputMap(userInputMap);
				Intent intent = new Intent();
				intent.putExtra(Tran.ACCOUNTDETAIL_CASHREMIT_RES, cashRemitCode);
				intent.setClass(TransferManagerActivity1.this,
						RelSelfCreditCardConfirmActivity1.class);
				startActivity(intent);
			}
		});

		final List<String> currencyCodeFlagList = new ArrayList<String>();
		// 币种
		Spinner currencySp = (Spinner) v
				.findViewById(R.id.sp_rel_trans_currency_tranSeting);
		ArrayAdapter<String> currencyAdapter = null;
		// 钞汇标志
		final Spinner cashRemitSp = (Spinner) v
				.findViewById(R.id.sp_rel_trans_cashRemit_tranSeting);
		final List<String> cashRemitFlagList = new ArrayList<String>();
		cashRemitFlagList.add(LocalData.CurrencyCashremit
				.get(ConstantGloble.CASHREMIT_00));
		if (!StringUtil.isNullOrEmpty(currencyList)) {
			// 如果全球交易人民币记账功能 开通 只支持人名币
			if (isOpenFlag) {
				currencyCodeFlagList.add(LocalData.Currency.get("001"));
				currencySp.setBackgroundDrawable(TransferManagerActivity1.this
						.getResources().getDrawable(
								R.drawable.bg_spinner_default));
				currencySp.setClickable(false);
			} else {
				for (int i = 0; i < currencyList.size(); i++) {
					String currencyCode = currencyList.get(i);
					currencyCodeFlagList.add(LocalData.Currency
							.get(currencyCode));
				}
			}

			currencyAdapter = new ArrayAdapter<String>(
					TransferManagerActivity1.this,
					R.layout.custom_spinner_item, currencyCodeFlagList);
			currencySp.setAdapter(currencyAdapter);
			currencyAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			currencySp.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					currencyCode = currencyList.get(position);
					cashRemitSp.setEnabled(false);

					cashRemitFlagList.clear();
					cashRemitList = new ArrayList<String>();
					cashRemitCode = null;

					if (position == 0) {
						cashRemitFlagList.add(LocalData.cashRemitBackMap
								.get("00"));
						// cashRemitList.add("00");
						cashRemitCode = ConstantGloble.CASHRMIT_RMB;
					} else {
						cashRemitFlagList.add(LocalData.cashRemitBackMap
								.get("01"));
						// cashRemitList.add("01");
						cashRemitCode = ConstantGloble.CASHRMIT_CASH;
					}

					ArrayAdapter<String> cashRemitAdapter = new ArrayAdapter<String>(
							TransferManagerActivity1.this,
							R.layout.custom_spinner_item, cashRemitFlagList);
					cashRemitAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					cashRemitSp.setAdapter(cashRemitAdapter);
					cashRemitSp.setEnabled(false);
					cashRemitSp
							.setBackgroundDrawable(TransferManagerActivity1.this
									.getResources().getDrawable(
											R.drawable.bg_spinner_default));
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					currencyCode = currencyList.get(0);
				}
			});
			cashRemitSp.setEnabled(false);
			cashRemitSp.setBackgroundDrawable(TransferManagerActivity1.this
					.getResources().getDrawable(R.drawable.bg_spinner_default));
			ArrayAdapter<String> cashRemitAdapter = new ArrayAdapter<String>(
					TransferManagerActivity1.this,
					R.layout.custom_spinner_item, cashRemitFlagList);
			cashRemitSp.setAdapter(cashRemitAdapter);
			cashRemitCode = ConstantGloble.CASHREMIT_00;
		} else {
			currencySp.setEnabled(false);
			currencySp.setBackgroundDrawable(TransferManagerActivity1.this
					.getResources().getDrawable(R.drawable.bg_spinner_default));
			cashRemitSp.setEnabled(false);
			cashRemitSp.setBackgroundDrawable(TransferManagerActivity1.this
					.getResources().getDrawable(R.drawable.bg_spinner_default));
		}

		String accountOutType = null;
		if (!StringUtil.isNullOrEmpty(TranDataCenter.getInstance()
				.getAccOutInfoMap())) {
			accountOutType = (String) TranDataCenter.getInstance()
					.getAccOutInfoMap().get(Comm.ACCOUNT_TYPE);
		}

		String accountInType = null;
		if (!StringUtil.isNullOrEmpty(TranDataCenter.getInstance()
				.getAccInInfoMap())) {
			accountInType = (String) TranDataCenter.getInstance()
					.getAccInInfoMap().get(Comm.ACCOUNT_TYPE);
		}
		// 非 信用卡购汇模块进入 才隐藏 钞汇标识
		if (otherPartJumpFlag != ConstantGloble.REL_CRCD_BUY) {
			// 转入账户 和 转出账户 都是信用卡的时候 不显示钞汇标识
			if (ConstantGloble.ACC_TYPE_GRE.equals(accountOutType)
					|| ConstantGloble.ZHONGYIN.equals(accountOutType)
					|| ConstantGloble.SINGLEWAIBI.equals(accountOutType)) {

				if (ConstantGloble.ACC_TYPE_GRE.equals(accountInType)
						|| ConstantGloble.ZHONGYIN.equals(accountInType)
						|| ConstantGloble.SINGLEWAIBI.equals(accountInType)) {
					v.findViewById(
							R.id.sp_rel_trans_cashRemit_tranSeting_layout)
							.setVisibility(View.GONE);

				}

			}
		}

		bottomLayout.removeAllViews();
		bottomLayout.addView(v);

	}

	/**
	 * 初始化底部视图 （当转出账户是长城信用卡 转入帐户是 借记卡时）
	 * 
	 * @param v
	 */
	private void initCreditRemit(final View v) {

		currencyList = new ArrayList<String>();

		final List<String> currencyCodeFlagList = new ArrayList<String>();

		if (!StringUtil.isNull(outCrcdCurrency1)) {
			currencyCodeFlagList.add(LocalData.Currency.get(outCrcdCurrency1));
			currencyList.add(outCrcdCurrency1);
		}

		if (!StringUtil.isNull(outCrcdCurrency2)) {
			currencyCodeFlagList.add(LocalData.Currency.get(outCrcdCurrency2));
			currencyList.add(outCrcdCurrency2);

		}

		final String accountOutType = (String) TranDataCenter.getInstance()
				.getAccOutInfoMap().get(Comm.ACCOUNT_TYPE);
		final String accountItType = (String) TranDataCenter.getInstance()
				.getAccInInfoMap().get(Comm.ACCOUNT_TYPE);

		// 币种
		Spinner currencySp = (Spinner) v
				.findViewById(R.id.sp_rel_trans_currency_tranSeting);
		// 钞汇标志
		final Spinner cashRemitSp = (Spinner) v
				.findViewById(R.id.sp_rel_trans_cashRemit_tranSeting);
		final List<String> cashRemitFlagList = new ArrayList<String>();

		ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(
				TransferManagerActivity1.this, R.layout.custom_spinner_item,
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
				cashRemitSp.setEnabled(false);
				cashRemitSp.setBackgroundDrawable(TransferManagerActivity1.this
						.getResources().getDrawable(
								R.drawable.bg_spinner_default));

				cashRemitFlagList.clear();
				cashRemitList = new ArrayList<String>();
				cashRemitCode = null;

				if (currencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {
					cashRemitFlagList.add(LocalData.cashRemitBackMap.get("00"));
					// cashRemitList.add("00");
					cashRemitCode = ConstantGloble.CASHRMIT_RMB;

					// if(ConstantGloble.ACC_TYPE_BRO.equals(accountItType)
					// &&( ConstantGloble.ACC_TYPE_GRE.equals(accountOutType)
					// || ConstantGloble.ZHONGYIN.equals(accountOutType))){
					v.findViewById(R.id.sp_rel_trans_cashRemit_textview_layout)
							.setVisibility(View.GONE);
					v.findViewById(
							R.id.sp_rel_trans_cashRemit_tranSeting_layout)
							.setVisibility(View.VISIBLE);
					// }
				} else {
					cashRemitFlagList.add(LocalData.cashRemitBackMap.get("01"));
					// cashRemitList.add("01");
					cashRemitCode = ConstantGloble.CASHRMIT_CASH;
					// 外币 钞汇标志 用textview 显示 不用sppiner
					if (ConstantGloble.ACC_TYPE_BRO.equals(accountItType)
							&& (ConstantGloble.ACC_TYPE_GRE
									.equals(accountOutType) || ConstantGloble.ZHONGYIN
									.equals(accountOutType)
									|| ConstantGloble.SINGLEWAIBI
									.equals(accountOutType))) {
						v.findViewById(
								R.id.sp_rel_trans_cashRemit_textview_layout)
								.setVisibility(View.VISIBLE);
						v.findViewById(
								R.id.sp_rel_trans_cashRemit_tranSeting_layout)
								.setVisibility(View.GONE);

					}

					if (ConstantGloble.ACC_TYPE_BRO.equals(accountOutType)
							&& (ConstantGloble.ACC_TYPE_GRE
									.equals(accountItType) || ConstantGloble.ZHONGYIN
									.equals(accountItType)
									|| ConstantGloble.SINGLEWAIBI
									.equals(accountItType))) {
						v.findViewById(
								R.id.sp_rel_trans_cashRemit_textview_layout)
								.setVisibility(View.VISIBLE);
						v.findViewById(
								R.id.sp_rel_trans_cashRemit_tranSeting_layout)
								.setVisibility(View.GONE);
					}
				}

				ArrayAdapter<String> cashRemitAdapter = new ArrayAdapter<String>(
						TransferManagerActivity1.this,
						R.layout.custom_spinner_item, cashRemitFlagList);
				cashRemitAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				cashRemitSp.setAdapter(cashRemitAdapter);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				currencyCode = currencyList.get(0);// 默认上送人民币
			}
		});

		// 转出账户余额
		Map<String, Object> resultMap = TranDataCenter.getInstance()
				.getCurrOutDetail();

		Map<String, String> detailMap = new HashMap<String, String>();
		List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
		detailList = (List<Map<String, String>>) resultMap
				.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		detailMap = detailList.get(0);
		availableBalance = (String) detailMap.get(Crcd.CRCD_LOANBALANCELIMIT);

		// 转账金额
		amountEt = (EditText) v
				.findViewById(R.id.et_amount_rel_trans_mytransfer);
		// 附言
		remarkEt = (EditText) v
				.findViewById(R.id.et_remark_rel_trans_mytransfer);
		EditTextUtils.setLengthMatcher(TransferManagerActivity1.this, remarkEt,
				50);
		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(amountEt, tv_for_amount);
		// cashRemitSp.setOnItemSelectedListener(cashRemitSelectedListener);
		initRelBottomBtn(v);
	}

	/**
	 * 查询常用收款人PsnTransManagePayeeQueryPayeeList
	 */
	private void requestAccInBankAccountList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANSMANAGEPAYEEQUERYPAYEELIST);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.TRANS_BOCFLAG_REQ, "");
		biiRequestBody.setParams(map);

		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestAccInBankAccountListCallBack");
	}

	/**
	 * 查询常用收款人PsnTransManagePayeeQueryPayeeList 返回
	 * 
	 * @param resultObj
	 */
	public void requestAccInBankAccountListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 常用账户
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		List<Map<String, Object>> copyResult = new ArrayList<Map<String, Object>>();
		copyResult.addAll(result);
		// 过滤掉bocFlag 不是0,1,2的
		for (int i = 0; i < result.size(); i++) {
			Map<String, Object> map = result.get(i);
			String bocFlag = (String) map.get(Tran.TRANS_BOCFLAG_REQ);
			String bankName = (String) map
					.get(Tran.TRAN_DIR_QUERYLIST_BANKNAME_RES);
			String address = (String) map
					.get(Tran.TRAN_DIR_QUERYLIST_ADDRESS_RES);
			if (StringUtil.isNullOrEmpty(bankName)) {
				map.put(ISHAVEBANKNAME, false);
			} else {
				map.put(ISHAVEBANKNAME, true);
			}
			if (StringUtil.isNullOrEmpty(address)) {
				map.put(ISHAVEADDRESS, false);
			} else {
				map.put(ISHAVEADDRESS, true);
			}
			if (!bocFlag.equals("0") && !bocFlag.equals("1")
					&& !bocFlag.equals("2")) {

				copyResult.remove(map);
			}
		}
		// 402 修改常用收款人分区显示（不须分区时，只需屏蔽即可）
		copyResult = resetCommonInList(copyResult);
		TranDataCenter.getInstance().setCommAccInList(copyResult);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.TRAN_DIR_QUERYLIST_BOCFLAG_REQ, new String[] { "" });
		requestBIIForTran(Tran.TRAN_DIR_QUERYPAYEELIST_API, map,
				"psnDirTransQueryPayeeListCallBack");
	}

	/**
	 * 查询信用卡币种
	 * 
	 * @param accountNumber
	 */
	private void requestForCrcdCurrency(String accountNumber) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.CREDITCARDCURRENCYQUERY);

		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.ACCOUNTNUMBER_RES, accountNumber);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCrcdCurrencyCallBack");
	}

	@SuppressWarnings("unchecked")
	public void requestForCrcdCurrencyCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		TranDataCenter.getInstance().setAccInInfoMap(addInforMap);
		BaseDroidApp.getInstanse().dismissMessageDialog();
		// 对result进行非空判断
		Map<String, String> currencyMap1 = (Map<String, String>) result
				.get(Tran.CREDIT_CARD_CURRENCY1_RES);

		crcdCurrency1 = currencyMap1.get(Crcd.CRCD_CODE);
		inCrcdCurrency1 = crcdCurrency1;
		Map<String, String> currencyMap = (Map<String, String>) result
				.get(Tran.CREDIT_CARD_CURRENCY2_RES);
		if (!StringUtil.isNullOrEmpty(currencyMap1)
				&& !StringUtil.isNullOrEmpty(currencyMap)) {
			// 双币种信用卡
			crcdCurrency2 = currencyMap.get(Crcd.CRCD_CODE);
			inCrcdCurrency2 = crcdCurrency2;
			// 全球交易人民币记账功能查询 openFlag 是否开通 如果开通 则交易币种只支持人民币
			// 如果是双币种 才调用此接口
			Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
					.getAccInInfoMap();
			String accountId = (String) accInInfoMap.get(Comm.ACCOUNT_ID);
			requestPsnCrcdChargeOnRMBAccountQuery(accountId);
			return;
		} else if (!StringUtil.isNullOrEmpty(currencyMap1)
				&& StringUtil.isNullOrEmpty(currencyMap)) {
			String currency = currencyMap1.get(Crcd.CRCD_CODE);
			if (!StringUtil.isNull(LocalData.Currency.get(currency))) {
				if (LocalData.Currency.get(currency).equals(
						ConstantGloble.ACC_RMB)) {
					crcdCurrency2 = "";
				} else {
					// 单外币信用卡
					crcdCurrency2 = currencyMap1.get(Crcd.CRCD_CODE);
				}
			}

		}
		showAccInDetailView();
		// 显示信用卡底部视图

		String accountInType = (String) TranDataCenter.getInstance()
				.getAccInInfoMap().get(Comm.ACCOUNT_TYPE);
		String accountOutType = (String) TranDataCenter.getInstance()
				.getAccOutInfoMap().get(Comm.ACCOUNT_TYPE);
		if (outOrInFlag.equals(IN)) {
			if ((ConstantGloble.ACC_TYPE_GRE.equals(accountInType) || ConstantGloble.ZHONGYIN
					.equals(accountInType))
					&& (ConstantGloble.ACC_TYPE_BRO.equals(accountOutType)
							|| ConstantGloble.ACC_TYPE_GRE
									.equals(accountOutType)
							|| ConstantGloble.ZHONGYIN.equals(accountOutType) || ConstantGloble.SINGLEWAIBI
								.equals(accountOutType))) {

				// 组装数据
				combineCreditCrcdData();
				initRelCrcdTran22(accountOutType);
			} else {

				// initCrcdView();1
				combineData();
				// combineCreditCrcdData();
				initRelTran();
			}
		}

	}

	/** 组装数据 转入账户 是中银 或 长城 信用卡时 转出账户是 中银信用卡 或 借记卡时 初始化数据 */

	private void combineCreditCrcdData() {
		String accountInType = (String) TranDataCenter.getInstance()
				.getAccInInfoMap().get(Comm.ACCOUNT_TYPE);
		String accountOutType = (String) TranDataCenter.getInstance()
				.getAccOutInfoMap().get(Comm.ACCOUNT_TYPE);

		final List<String> currencyCodeFlagList = new ArrayList<String>();

		currencyList = new ArrayList<String>();
		if (ConstantGloble.ACC_TYPE_GRE.equals(accountInType)
				|| ConstantGloble.ZHONGYIN.equals(accountInType)) {
			if (ConstantGloble.ACC_TYPE_GRE.equals(accountOutType)
					|| ConstantGloble.ZHONGYIN.equals(accountOutType)
					|| ConstantGloble.SINGLEWAIBI.equals(accountOutType)) {

				if (isOpenFlag) {
					currencyList.add(ConstantGloble.PRMS_CODE_RMB);
				} else {
					if (!StringUtil.isNull(inCrcdCurrency1)) {
						currencyCodeFlagList.add(LocalData.Currency
								.get(inCrcdCurrency1));
						currencyList.add(inCrcdCurrency1);
					}

					if (!StringUtil.isNull(inCrcdCurrency2)) {
						currencyList.add(inCrcdCurrency2);
						currencyCodeFlagList.add(LocalData.Currency
								.get(inCrcdCurrency2));

					}
				}

				// 转出账户是信用卡
				List<String> currencyList3 = new ArrayList<String>();

				if (!StringUtil.isNull(outCrcdCurrency1)) {
					currencyCodeFlagList.add(LocalData.Currency
							.get(outCrcdCurrency1));
					currencyList3.add(outCrcdCurrency1);
				}

				if (!StringUtil.isNull(outCrcdCurrency2)) {
					currencyCodeFlagList.add(LocalData.Currency
							.get(outCrcdCurrency2));
					currencyList3.add(outCrcdCurrency2);

				}
				currencyList.retainAll(currencyList3);
			} else if (ConstantGloble.ACC_TYPE_BRO.equals(accountOutType)) {

				// 转出账户是 借记卡

				List<String> currencyList1 = new ArrayList<String>();
				if (isOpenFlag) {
					currencyList.add(ConstantGloble.PRMS_CODE_RMB);
				} else {
					if (!StringUtil.isNull(inCrcdCurrency1)) {
						currencyCodeFlagList.add(LocalData.Currency
								.get(inCrcdCurrency1));
						currencyList1.add(inCrcdCurrency1);
					}

					if (!StringUtil.isNull(inCrcdCurrency2)) {
						currencyList1.add(inCrcdCurrency2);
						currencyCodeFlagList.add(LocalData.Currency
								.get(inCrcdCurrency2));

					}
				}
				currencyList.addAll(currencyList1);

				Map<String, Object> detailMap = TranDataCenter.getInstance()
						.getCurrOutDetail();
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> detailList = (List<Map<String, Object>>) detailMap
						.get(Tran.ACCOUNTDETAIL_ACCOUNTDETAILIST_RES);
				// 返回数据里面 存折号和存单是没有关联的 重新组装数据
				// 找出列表中所有不相同的币种8
				currencyList1.clear();
				for (int i = 0; i < detailList.size(); i++) {
					Map<String, Object> detaimap = detailList.get(i);
					String currencyCode = (String) detaimap
							.get(Tran.CURRENCYCODE_RES);
					// 过滤掉贵金属
					if (StringUtil.isNullOrEmpty(currencyCode)
							|| LocalData.prmsTradeStyleCodeList
									.contains(currencyCode)) {
						continue;
					}

					currencyList1.add(currencyCode);
				}

				currencyList.retainAll(currencyList1);

			}
		}
		if (StringUtil.isNullOrEmpty(currencyList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.no_same_currency));

		}
	}

	/**
	 * 初始化底部视图 关联账户转账底部视图 信用卡
	 */
	private void initRelCrcdTran22(String accountOutType) {

		// 非信用卡人名币还款进入
		if (ConstantGloble.REL_CRCD_REPAY != otherPartJumpFlag) {
			View v = mInflater.inflate(
					R.layout.tran_rel_transfer_seting_mytransfer, null);
			// 如果转出账户是 104 长城信用卡的时候 转入账户是 中银 长城 单外币 信用卡时 或 119 借记卡时 显示附加提示语
			if (ConstantGloble.GREATWALL.equals(accountOutType)) {
				v.findViewById(R.id.comm_credit_card_repay_info).setVisibility(
						View.VISIBLE);
			} else {
				v.findViewById(R.id.comm_credit_card_repay_info).setVisibility(
						View.GONE);
			}
			initRelView11(v);
			bottomLayout.removeAllViews();
			bottomLayout.addView(v);
		} else {
			initRelCrcdTran33();
		}

	}

	/**
	 * 转入账户是中银 或 长城信用卡 转出账户是中银信用卡 或 借记卡 的情况下 初始化底部视图
	 */
	private void initRelView11(final View v) {

		final EditText repayAmountEt = (EditText) v
				.findViewById(R.id.et_repayAmountValue_relSelf_creditCard_transSeting);

		final String accountOutType = (String) TranDataCenter.getInstance()
				.getAccOutInfoMap().get(Comm.ACCOUNT_TYPE);
		final String accountIntType = (String) TranDataCenter.getInstance()
				.getAccInInfoMap().get(Comm.ACCOUNT_TYPE);

		final List<String> currencyCodeFlagList = new ArrayList<String>();
		// 如果转入账户是信用卡 需要判断 是否开通全球交易人民币记账功能 如果开通 则只支持人民币

		if (ConstantGloble.ACC_TYPE_GRE.equals(accountIntType)
				|| ConstantGloble.ZHONGYIN.equals(accountIntType)) {
			if (!StringUtil.isNullOrEmpty(currencyList)) {
				for (int i = 0; i < currencyList.size(); i++) {
					String currencyCode = currencyList.get(i);
					currencyCodeFlagList.add(LocalData.Currency
							.get(currencyCode));
				}
			} else {
				currencyCodeFlagList.add("-");
			}

		}

		// 币种
		final Spinner currencySp = (Spinner) v
				.findViewById(R.id.sp_rel_trans_currency_tranSeting);
		// 钞汇标志
		final Spinner cashRemitSp = (Spinner) v
				.findViewById(R.id.sp_rel_trans_cashRemit_tranSeting);
		final List<String> cashRemitFlagList = new ArrayList<String>();
		cashRemitFlagList.add(LocalData.CurrencyCashremit
				.get(ConstantGloble.CASHREMIT_00));

		ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(
				TransferManagerActivity1.this, R.layout.custom_spinner_item,
				currencyCodeFlagList);
		currencyAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currencySp.setAdapter(currencyAdapter);
		currencySp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (!StringUtil.isNullOrEmpty(currencyList)) {
					currencyCode = currencyList.get(position);
				} else {
					currencyCode = null;
				}

				cashRemitFlagList.clear();
				cashRemitList = new ArrayList<String>();
				cashRemitCode = null;
				if ("人民币元".equals(currencyCodeFlagList.get(position))) {
					cashRemitFlagList.add(LocalData.cashRemitBackMap.get("00"));
					cashRemitCode = ConstantGloble.CASHRMIT_RMB;

					ArrayAdapter<String> cashRemitAdapter = new ArrayAdapter<String>(
							TransferManagerActivity1.this,
							R.layout.custom_spinner_item, cashRemitFlagList);
					cashRemitSp.setAdapter(cashRemitAdapter);
					cashRemitSp.setEnabled(false);
					cashRemitSp
							.setBackgroundDrawable(TransferManagerActivity1.this
									.getResources().getDrawable(
											R.drawable.bg_spinner_default));

				} else {

					if (ConstantGloble.ACC_TYPE_BRO.equals(accountOutType)) {
						if (ConstantGloble.ACC_TYPE_GRE.equals(accountIntType)
								|| ConstantGloble.ZHONGYIN
										.equals(accountIntType)
								|| ConstantGloble.SINGLEWAIBI
										.equals(accountIntType)) {
							for (int i = 0; i < outCurrencyAndBalance.size(); i++) {

								for (int j = 0; j < inCreditCurrencyAndBalance
										.size(); j++) {

									// 如果是交集 那么显示
									if (outCurrencyAndBalance
											.get(i)
											.get("currency")
											.equals(inCreditCurrencyAndBalance
													.get(j).get("currency"))) {
										// 如果是人名币 已经显示过了 过滤掉
										if ("001".equals(outCurrencyAndBalance
												.get(i).get("currency"))) {
											continue;
										}
										cashRemitList.add(outCurrencyAndBalance
												.get(i).get("cashRemit"));
										cashRemitFlagList.add(LocalData.cashRemitBackMap
												.get(outCurrencyAndBalance.get(
														i).get("cashRemit")));
									}
								}
							}
						}
						ArrayAdapter<String> cashRemitAdapter = new ArrayAdapter<String>(
								TransferManagerActivity1.this,
								R.layout.custom_spinner_item, cashRemitFlagList);
						cashRemitSp.setAdapter(cashRemitAdapter);
						cashRemitSp.setBackgroundDrawable(getResources()
								.getDrawable(R.drawable.bg_spinner));
						cashRemitSp.setEnabled(true);
						cashRemitAdapter
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					} else {
						cashRemitFlagList.add(LocalData.cashRemitBackMap
								.get("01"));
						cashRemitCode = ConstantGloble.CASHRMIT_CASH;

						ArrayAdapter<String> cashRemitAdapter = new ArrayAdapter<String>(
								TransferManagerActivity1.this,
								R.layout.custom_spinner_item, cashRemitFlagList);
						cashRemitSp.setAdapter(cashRemitAdapter);
						cashRemitSp.setEnabled(false);
						cashRemitSp
								.setBackgroundDrawable(TransferManagerActivity1.this
										.getResources().getDrawable(
												R.drawable.bg_spinner_default));

					}
				}

				// 转入账户是 借记卡 转出账户是 信用卡的时候 只显示现钞 textview 形式 显示
				if (ConstantGloble.ACC_TYPE_BRO.equals(accountIntType)
						&& (ConstantGloble.ACC_TYPE_GRE.equals(accountOutType)
								|| ConstantGloble.ZHONGYIN
										.equals(accountOutType) || ConstantGloble.SINGLEWAIBI
									.equals(accountOutType))) {
					v.findViewById(R.id.sp_rel_trans_cashRemit_textview_layout)
							.setVisibility(View.VISIBLE);
					v.findViewById(
							R.id.sp_rel_trans_cashRemit_tranSeting_layout)
							.setVisibility(View.GONE);
					// 转入账户 和 转出账户 都是信用卡的时候 不显示钞汇标识
				} else if (ConstantGloble.ACC_TYPE_GRE.equals(accountOutType)
						|| ConstantGloble.ZHONGYIN.equals(accountOutType)
						|| ConstantGloble.SINGLEWAIBI.equals(accountOutType)) {

					if (ConstantGloble.ACC_TYPE_GRE.equals(accountIntType)
							|| ConstantGloble.ZHONGYIN.equals(accountIntType)
							|| ConstantGloble.SINGLEWAIBI
									.equals(accountIntType)) {
						v.findViewById(
								R.id.sp_rel_trans_cashRemit_tranSeting_layout)
								.setVisibility(View.GONE);

					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if (!StringUtil.isNullOrEmpty(currencyList)) {
					currencyCode = currencyList.get(0);
				} else {
					currencyCode = null;
				}
			}
		});

		cashRemitSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (!currencySp.getSelectedItem().toString().equals("人民币元")) {
					cashRemitCode = cashRemitList.get(position);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		// 转账金额
		amountEt = (EditText) v
				.findViewById(R.id.et_amount_rel_trans_mytransfer);
		// 附言
		remarkEt = (EditText) v
				.findViewById(R.id.et_remark_rel_trans_mytransfer);
		EditTextUtils.setLengthMatcher(TransferManagerActivity1.this, remarkEt,
				50);
		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(amountEt, tv_for_amount);
		initRelBottomBtn(v);
	}

	/**
	 * 查询信用卡币种
	 * 
	 * @param accountNumber
	 */
	private void requestCrcdCurrency(String accountNumber) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.CREDITCARDCURRENCYQUERY);

		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.ACCOUNTNUMBER_RES, accountNumber);

		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestCrcdCurrencyCallBack");
	}

	/**
	 * 查询信用卡币种 信用卡模块进入时
	 * 
	 * @param accountNumber
	 */
	private void requestCrcdCurrency1(String accountNumber) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.CREDITCARDCURRENCYQUERY);

		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.ACCOUNTNUMBER_RES, accountNumber);

		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestCrcdCurrencyCallBack1");
	}

	@SuppressWarnings("unchecked")
	public void requestCrcdCurrencyCallBack1(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();

		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String accountId = (String) accInInfoMap.get(Comm.ACCOUNT_ID);
		String accountType = (String) accInInfoMap.get(Comm.ACCOUNT_TYPE);
		// 对result进行非空判断
		Map<String, String> currencyMap1 = (Map<String, String>) result
				.get(Tran.CREDIT_CARD_CURRENCY1_RES);
		if (!StringUtil.isNullOrEmpty(currencyMap1)) {

			outCrcdCurrency1 = currencyMap1.get(Crcd.CRCD_CODE);
		}
		Map<String, String> currencyMap = (Map<String, String>) result
				.get(Tran.CREDIT_CARD_CURRENCY2_RES);
		if (!StringUtil.isNullOrEmpty(currencyMap)) {
			outCrcdCurrency2 = currencyMap.get(Crcd.CRCD_CODE);
			// 只有转入账户是长城信用卡 和中银信用卡的时候才 调用 全球交易人民币记账功能查询
			if (ConstantGloble.ZHONGYIN.equals(accountType)
					|| ConstantGloble.GREATWALL.equals(accountType)) {
				requestPsnCrcdChargeOnRMBAccountQuery(accountId);
				return;
			}
		}

		BiiHttpEngine.dissMissProgressDialog();

	}

	/**
	 * 全球交易人民币记账功能查询
	 * 
	 * @param accountId
	 *            :网银账户标志
	 */
	private void requestPsnCrcdChargeOnRMBAccountQuery(String accountId) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_RMBACCOUNTQUERY_API);
		Map<String, String> params = new HashMap<String, String>();
		params.put(Crcd.CRCD_ACCOUNTID_REQ, accountId);
		biiRequestBody.setParams(params);
		// biiRequestBody.setConversationId(null);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCommonQueryAllChinaBankAccountCallBack");
	}

	public void requestPsnCommonQueryAllChinaBankAccountCallBack(
			Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, String> result = (Map<String, String>) biiResponseBody
				.getResult();

		String openFlag = result.get(Crcd.CRCD_OPENFLAG_RES);

		if ("true".equals(openFlag)) {
			isOpenFlag = true;
		} else {
			isOpenFlag = false;
		}

		if (ConstantGloble.REL_CRCD_REPAY == otherPartJumpFlag) {
			// || ConstantGloble.ACC_TO_TRAN == otherPartJumpFlag) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}

		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String accountId = (String) accInInfoMap.get(Comm.ACCOUNT_ID);

		// if (!StringUtil.isNull(inCrcdCurrency2)) {
		// // 查询转入账户第二币种 详情
		// requestForCrcdDetailInTwo(accountId, inCrcdCurrency2);
		// return;
		// }
		showAccInDetailView();
		// 显示信用卡底部视图

		String accountInType = (String) TranDataCenter.getInstance()
				.getAccInInfoMap().get(Comm.ACCOUNT_TYPE);
		String accountOutType = (String) TranDataCenter.getInstance()
				.getAccOutInfoMap().get(Comm.ACCOUNT_TYPE);
		if (outOrInFlag.equals(IN)) {
			if ((ConstantGloble.ACC_TYPE_GRE.equals(accountInType) || ConstantGloble.ZHONGYIN
					.equals(accountInType))
					&& (ConstantGloble.ACC_TYPE_BRO.equals(accountOutType)
							|| ConstantGloble.ACC_TYPE_GRE
									.equals(accountOutType)
							|| ConstantGloble.ZHONGYIN.equals(accountOutType) || ConstantGloble.SINGLEWAIBI
								.equals(accountOutType))) {

				// 组装数据
				combineCreditCrcdData();
				initRelCrcdTran22(accountOutType);
			} else {

				// initCrcdView();1
				combineData();
				initRelTran();
			}
		}
	}

	private void requestForCrcdDetailInTwo(String accountId,
			String inCrcdCurrency2) {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.CRCDQUERYACCOUNTDETAIL);
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_ID, accountId);
		// 查询转入账户 信用卡第二币种详情
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY, inCrcdCurrency2);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCrcdDetailCallBackInTwo");

	}

	public void requestForCrcdDetailCallBackInTwo(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
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

		for (int i = 0; i < detailList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("loanBalanceLimitFlag",
					detailList.get(i).get("loanBalanceLimitFlag"));
			map.put("loanBalanceLimit",
					detailList.get(i).get("loanBalanceLimit"));
			map.put("currency", detailList.get(i).get("currency"));
			inCreditCurrencyAndBalance.add(map);
		}

		showAccInDetailView();
		// 显示信用卡底部视图

		String accountInType = (String) TranDataCenter.getInstance()
				.getAccInInfoMap().get(Comm.ACCOUNT_TYPE);
		String accountOutType = (String) TranDataCenter.getInstance()
				.getAccOutInfoMap().get(Comm.ACCOUNT_TYPE);
		if (outOrInFlag.equals(IN)) {
			if ((ConstantGloble.ACC_TYPE_GRE.equals(accountInType) || ConstantGloble.ZHONGYIN
					.equals(accountInType))
					&& (ConstantGloble.ACC_TYPE_BRO.equals(accountOutType)
							|| ConstantGloble.ACC_TYPE_GRE
									.equals(accountOutType)
							|| ConstantGloble.ZHONGYIN.equals(accountOutType) || ConstantGloble.SINGLEWAIBI// 601
																											// 增加
																											// 107
								.equals(accountOutType))) {

				// 组装数据
				combineCreditCrcdData();
				initRelCrcdTran22(accountOutType);
			} else {

				// initCrcdView();1
				combineData();
				initRelTran();
			}
		}

	}

	@SuppressWarnings("unchecked")
	public void requestCrcdCurrencyCallBack(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		// 对result进行非空判断
		Map<String, String> currencyMap1 = (Map<String, String>) result
				.get(Tran.CREDIT_CARD_CURRENCY1_RES);
		if (!StringUtil.isNullOrEmpty(currencyMap1)) {

			inCrcdCurrency1 = currencyMap1.get(Crcd.CRCD_CODE);
		}
		Map<String, String> currencyMap = (Map<String, String>) result
				.get(Tran.CREDIT_CARD_CURRENCY2_RES);
		if (!StringUtil.isNullOrEmpty(currencyMap)) {
			inCrcdCurrency2 = currencyMap.get(Crcd.CRCD_CODE);
		}

		if (!StringUtil.isNullOrEmpty(currencyMap1)
				&& !StringUtil.isNullOrEmpty(currencyMap)) {
			// 双币种信用卡
			crcdCurrency2 = currencyMap.get(Crcd.CRCD_CODE);
			// 请求外币详情
			requestForCrcdCurrencyDetail(crcdId);
		} else if (!StringUtil.isNullOrEmpty(currencyMap1)
				&& StringUtil.isNullOrEmpty(currencyMap)) {

			String currency = currencyMap1.get(Crcd.CRCD_CODE);
			if (!StringUtil.isNull(LocalData.Currency.get(currency))) {
				if (LocalData.Currency.get(currency).equals(
						ConstantGloble.ACC_RMB)) {
					crcdCurrency2 = "";
					BiiHttpEngine.dissMissProgressDialog();
				} else {
					tranTypeFlag = TRANTYPE_REL_CRCD_BUY;
					// 单外币信用卡
					crcdCurrency2 = currencyMap1.get(Crcd.CRCD_CODE);
					// 请求外币详情
					requestForCrcdCurrencyDetail(crcdId);
				}
			}

		} else {
			BaseHttpEngine.dissMissProgressDialog();
		}
	}

	/**
	 * 行内手续费试算
	 */
	private void requestForTransferCommissionCharge(String serviceId) {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		String fromAccountId = (String) accOutInfoMap.get(Tran.ACCOUNTID_RES);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String toAccountId = "";
		if (tranTypeFlag == TRANTYPE_NOREL_BANKIN
				|| tranTypeFlag == TRANTYPE_NOREL_CRCD) {
			// 行内转账
			toAccountId = (String) accInInfoMap.get(Tran.TRANS_PAYEETID_RES);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKIN
				|| tranTypeFlag == TRANTYPE_DIR_CRCD) {
			// 行内定向
			toAccountId = (String) accInInfoMap
					.get(Tran.TRAN_DIR_QUERYLIST_PAYEEID_RES);
		} else if (tranTypeFlag == TRANTYPE_REL_ACCOUNT) {
			// 关联账户转账
			toAccountId = (String) accInInfoMap.get(Tran.ACCOUNTID_RES);
		}
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
	 * 行内手续费试算返回
	 * 
	 * @param resultObj
	 */
	public void requestForTransferCommissionChargeCallBack(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setCommissionChargeMap(result);
		switch (Integer.parseInt(tranMode)) {
		case 0:// 立即执行
			BaseHttpEngine.dissMissProgressDialog();
			if (tranTypeFlag == TRANTYPE_NOREL_BANKIN
					|| tranTypeFlag == TRANTYPE_DIR_BANKIN) {// 行内转账
				// 请求安全因子
				Intent nextIntent = new Intent(this,
						NoRelConfirmInfoActivity1.class);
				nextIntent.putExtra(TRANS_TYPE, tranTypeFlag);
				startActivity(nextIntent);
			} else if (tranTypeFlag == TRANTYPE_NOREL_CRCD
					|| tranTypeFlag == TRANTYPE_DIR_CRCD) {// 非关联信用卡
				Intent intent = new Intent();
				intent.setClass(this, ConfirmInfoActivity1.class);
				intent.putExtra(TRANS_TYPE, tranTypeFlag);
				startActivity(intent);
			} else {
				Intent intent0 = new Intent(TransferManagerActivity1.this,
						RelConfirmInfoActivity1.class);
				startActivity(intent0);
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		switch (Integer.parseInt(tranMode)) {
		case 1:// 预约日期执行
			Intent intent1 = new Intent(TransferManagerActivity1.this,
					PreDateExeActivity1.class);
			intent1.putExtra(ConstantGloble.DATE_TIEM, dateTime);
			intent1.putExtra(TRANS_TYPE, tranTypeFlag);
			startActivity(intent1);
			break;
		case 2:// 预约周期执行
			Intent intent2 = new Intent(TransferManagerActivity1.this,
					PrePeriodExeActivity1.class);
			intent2.putExtra(ConstantGloble.DATE_TIEM, dateTime);
			intent2.putExtra(TRANS_TYPE, tranTypeFlag);
			startActivity(intent2);
			break;

		default:
			break;
		}
	}

	/** 账户管理点击我要转账后跳转过来转出账户视图无法绘制，随便请求个接口来触发绘制，这是请求接口之后的处理 */
	public void requestSysDateTimeCallBack(Object resultObj) {
		showAccOutDetailView();
		// 组装数据
		combineData();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);

		switch (tranTypeFlag) {

		case TRANTYPE_REL_CRCD_BUY:// 信用卡购汇还款
			requestForCrcdForeignPayOff();
			break;
		case TRANTYPE_NOREL_CRCD:// 非关联信用卡还款
		case TRANTYPE_NOREL_BANKIN:// 行内转账
			requestGetSecurityFactor(ConstantGloble.PB031);
			break;
		case TRANTYPE_NOREL_BANKOTHER:// 跨行转账
			requestGetSecurityFactor(ConstantGloble.PB032);
			break;
		case TRANTYPE_DIR_BANKIN:// 行内定向
		case TRANTYPE_DIR_CRCD:// 非关联信用卡定向
			requestGetSecurityFactor(ConstantGloble.PB033);
			break;
		case TRANTYPE_DIR_BANKOTHER:// 跨行定向
			requestGetSecurityFactor(ConstantGloble.PB034);
			break;
		case TRANTYPE_SHISHI_BANKOTHER:// 跨行实时转账
			requestGetSecurityFactor(ConstantGloble.PB113);
			break;
		case TRANTYPE_SHISHI_DIR_BANKOTHER:// 跨行实时定向转账
			requestGetSecurityFactor(ConstantGloble.PB118);
			break;
		default:
			break;
		}

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
		map.put(Tran.CREDITCARD_CRCDID_REQ, toPayeeId);// 转入账户信息 crcdId
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
		Map<String, Object> resultMap = TranDataCenter.getInstance()
				.getCurrInDetail();
		Map<String, String> detailMap = new HashMap<String, String>();
		List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
		detailList = (List<Map<String, String>>) resultMap
				.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		detailMap = detailList.get(0);
		String currentBalance = (String) detailMap
				.get(Crcd.CRCD_LOANBALANCELIMIT);
		String flag = (String) detailMap.get(Crcd.LOANBALANCELIMITFLAG);
		flag = flagConvert(flag);
		BaseDroidApp.getInstanse().dismissMessageDialog();
		showAccInDetailView();
		refreshTranInDataView(currentBalance, flag);
		View view = (ViewGroup) mInflater
				.inflate(
						R.layout.tran_relation_credit_card_remit_trans_seting_mytransfer,
						null);
		initRelCrcdBuyView(view);
		bottomLayout.removeAllViews();
		bottomLayout.addView(view);
		// Intent intent = new Intent(this,
		// RelCreditCardRemitConfirmActivity1.class);
		// startActivity(intent);
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
						switch (tranTypeFlag) {
						case TRANTYPE_NOREL_BANKIN:// 行内
						case TRANTYPE_DIR_BANKIN:// 行内定向
							requestForTransBocTransferVerify();
							break;
						case TRANTYPE_NOREL_BANKOTHER:// 跨行
						case TRANTYPE_DIR_BANKOTHER:// 跨行定向
							requestForBocNationalTransferVerify();
							break;
						case TRANTYPE_NOREL_CRCD:// 非关联信用卡还款
						case TRANTYPE_DIR_CRCD:// 非关联信用卡还款定向
							requestForTransBocTransferVerify();
							break;
						case TRANTYPE_SHISHI_BANKOTHER:// 跨行实时付款
							psnEbpsRealTimePaymentConfirm();
							break;
						case TRANTYPE_SHISHI_DIR_BANKOTHER:// 跨行实时定向付款
							requestForBocNationalTransferVerify();
							break;
						default:
							break;
						}
					}
				});
	}

	/**
	 * PsnTransBocNationalTransferVerify国内跨行汇款预交易
	 */
	private void requestForBocNationalTransferVerify() {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		String fromAccountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String payeeActno = (String) accInInfoMap
				.get(Tran.MANAGE_PAYEELIST_ACCOUNTNUMBER_RES);
		String payeeName = (String) accInInfoMap
				.get(Tran.MANAGE_PAYEELIST_ACCOUNTNAME_RES);
		String bankName = (String) accInInfoMap
				.get(Tran.MANAGE_PAYEELIST_BANKNAME_RES);
		String toOrgName = (String) accInInfoMap
				.get(Tran.MANAGE_PAYEELIST_ADDRESS_RES);
		String cnapsCode = (String) accInInfoMap
				.get(Tran.MANAGE_PAYEELIST_CNAPSCODE_RES);
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		payeeMobile = (String) userInputMap
				.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String payeeId = "";
		if (tranTypeFlag == TRANTYPE_NOREL_BANKOTHER) {
			// 跨行
			biiRequestBody.setMethod(Tran.TRANSBOCNATIONALTRANSFERVERIFY);
			payeeId = (String) accInInfoMap.get(Tran.TRANS_PAYEETID_RES);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKOTHER) {
			// 跨行定向
			biiRequestBody.setMethod(Tran.TRANSDIRBOCNATIONALTRANSFERVERIFY);
			payeeId = (String) accInInfoMap
					.get(Tran.TRAN_DIR_QUERYLIST_PAYEEID_RES);
		} else if (tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {
			biiRequestBody
					.setMethod(Tran.TRANSDIREBPSBOCNATIONALTRANSFERVERIFY);
			payeeId = (String) accInInfoMap
					.get(Tran.TRAN_DIR_QUERYLIST_PAYEEID_RES);

		}
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.TRANS_BOCNATIONAL_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.TRANS_BOCNATIONAL_PAYEEID_REQ, payeeId);
		map.put(Tran.TRANS_BOCNATIONAL_PAYEEACTNO_REQ, payeeActno);
		map.put(Tran.TRANS_BOCNATIONAL_PAYEENAME_REQ, payeeName);
		map.put(Tran.TRANS_BOCNATIONAL_BANKNAME_REQ, bankName);
		map.put(Tran.TRANS_BOCNATIONAL_TOORGNAME_REQ, toOrgName);

		if (tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {
			if(StringUtil.isNullOrEmpty(toOrgName)){
				map.put(Tran.TRANS_BOCNATIONAL_TOORGNAME_REQ, bankName);	
			}
			// 跨行定向实时
			if (common_tag && radio_shishi_orientation.isChecked()) {
				map.put(Tran.TRANS_BOCNATIONAL_CNAPSCODE_REQ, comm2shishi);
			} else {
				map.put(Tran.TRANS_BOCNATIONAL_CNAPSCODE_REQ, cnapsCode);
			}
		} else {
			map.put(Tran.TRANS_BOCNATIONAL_CNAPSCODE_REQ, cnapsCode);
		}

		map.put(Tran.TRANS_BOCNATIONAL_REMITTANCEINFO_REQ, "");
		map.put(Tran.TRANS_BOCNATIONAL_AMOUNT_REQ, amount);
		map.put(Tran.TRANS_BOCNATIONAL_PAYEEMOBILE_REQ, payeeMobile);
		map.put(Tran.TRANS_BOCNATIONAL_REMARK_REQ, memo);

		map.put(Tran.TRANS_BOCNATIONAL_CURRENCY_REQ,
				ConstantGloble.PRMS_CODE_RMB);
		map.put(Tran.TRANS_BOCNATIONAL_EXECUTETYPE_REQ, ConstantGloble.NOWEXE);
		map.put(Tran.TRANS_BOCNATIONAL_EXECUTEDATE_REQ, "");
		map.put(Tran.TRANS_BOCNATIONAL__COMBINID_REQ, combineId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForBocNationalTransferVerifyCallBack");
	}

	/**
	 * 请求国内跨行预交易返回
	 * 
	 * @param resultObj
	 */
	public void requestForBocNationalTransferVerifyCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setNoRelBankOtherCallBackMap(result);

		// 请求跨行手续费试算方法
		if (tranTypeFlag == TRANTYPE_NOREL_BANKOTHER) {
			// 跨行
			requestNationalTransferCommissionCharge(ConstantGloble.PB032);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKOTHER) {
			// 跨行定向
			requestNationalTransferCommissionCharge(ConstantGloble.PB034);
		} else if (tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {
			requestNationalTransferCommissionCharge(ConstantGloble.PB118);
		}
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
		// String payeeMobile = "";
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
		payeeActno = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		if (StringUtil.isNullOrEmpty(payeeActno)) {
			payeeActno = (String) accInInfoMap.get(Crcd.CRCD_ACCTNUM);
		}
		payeeName = (String) accInInfoMap.get(Comm.ACCOUNT_NAME);
		if (StringUtil.isNullOrEmpty(payeeName)) {
			payeeName = (String) accInInfoMap.get(Crcd.CRCD_ACCTNAME_RES);
		}
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		amount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);
		payeeMobile = userInputMap.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);
		remark = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		if (tranTypeFlag == TRANTYPE_NOREL_BANKIN
				|| tranTypeFlag == TRANTYPE_NOREL_CRCD) {
			// 行内转账
			biiRequestBody.setMethod(Tran.TRANSBOCTRANSFERVERIFY);
			payeeId = (String) accInInfoMap.get(Tran.TRANS_PAYEETID_RES);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKIN
				|| tranTypeFlag == TRANTYPE_DIR_CRCD) {
			// 行内定向
			biiRequestBody.setMethod(Tran.TRANSDIRBOCTRANSFERVERIFY);
			payeeId = (String) accInInfoMap
					.get(Tran.TRAN_DIR_QUERYLIST_PAYEEID_RES);
		}
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

		if (tranTypeFlag == TRANTYPE_NOREL_CRCD
				|| tranTypeFlag == TRANTYPE_DIR_CRCD) {
			TranDataCenter.getInstance().setNoRelCrcdRepayCallBackMap(result);
		} else {
			TranDataCenter.getInstance().setNoRelBankInCallBackMap(result);
		}
	
		String toAccountType =(String)result.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);	
	
		if(!StringUtil.isNullOrEmpty(toAccountType)){
			Map<String, Object> map = TranDataCenter.getInstance()
					.getAccInInfoMap();
			map.put(Tran.RELTRANS_TOACCOUNTTYPE_RES, toAccountType);	
			TranDataCenter.getInstance().setAccInInfoMap(map);
		}
			
		// 调手续费接口
		if (tranTypeFlag == TRANTYPE_NOREL_CRCD
				|| tranTypeFlag == TRANTYPE_NOREL_BANKIN) {
			requestForTransferCommissionCharge(ConstantGloble.PB031);
		} else {
			requestForTransferCommissionCharge(ConstantGloble.PB033);
		}
	}

	/**
	 * 跨行手续费试算
	 */
	private void requestNationalTransferCommissionCharge(String serviceId) {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		String fromAccountId = (String) accOutInfoMap.get(Tran.ACCOUNTID_RES);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String toAccountId = "";
		String payeeactNo = (String) accInInfoMap.get(Tran.ACCOUNTNUMBER_RES);
		String payeeName = (String) accInInfoMap.get(Tran.ACCOUNTNAME_RES);
		String toOrgName = (String) accInInfoMap.get(Tran.TRANS_BANKNAME_RES);
		String cnapsCode = (String) accInInfoMap.get(Tran.TRANS_CNAPSCODE_RES);
		if (tranTypeFlag == TRANTYPE_NOREL_BANKOTHER) {
			// 跨行转账
			toAccountId = (String) accInInfoMap.get(Tran.TRANS_PAYEETID_RES);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKOTHER
				|| tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {
			// 跨行定向、跨行实时定向
			toAccountId = (String) accInInfoMap
					.get(Tran.TRAN_DIR_QUERYLIST_PAYEEID_RES);
		} else if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
			// 跨行实时
			toAccountId = (String) accInInfoMap
					.get(Tran.EBPSQUERY_PAYEEACTID_REQ);
			payeeactNo = (String) accInInfoMap
					.get(Tran.EBPSQUERY_PAYEEACTNO_REQ);
			payeeName = (String) accInInfoMap
					.get(Tran.EBPSQUERY_PAYEEACTNAME_REQ);
			toOrgName = (String) accInInfoMap
					.get(Tran.EBPSQUERY_PAYEEBANKNAME_REQ);
			cnapsCode = (String) accInInfoMap
					.get(Tran.EBPSQUERY_PAYEECNAPS_REQ);
		}

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(Tran.PSNTRANS_GETNATIONAL_TRANSFER_COMMISSIONCHARGE);
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
		map.put(Tran.NATIONALADDPAYEE_TOORGNAME_REQ, toOrgName);

		if (tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {
			if (common_tag && radio_shishi_orientation.isChecked()) {
				map.put(Tran.TRANS_CNAPSCODE_RES, comm2shishi);
			} else {
				map.put(Tran.TRANS_CNAPSCODE_RES, cnapsCode);
			}
		} else if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
			if(rb_shishi!=null){
				if (common_tag && rb_shishi.isChecked()) {
					map.put(Tran.TRANS_CNAPSCODE_RES, comm2shishi);
				} else {
					map.put(Tran.TRANS_CNAPSCODE_RES, cnapsCode);
				}	
			}
			if(radio_shishi_orientation!=null){
				if (common_tag && radio_shishi_orientation.isChecked()) {
					map.put(Tran.TRANS_CNAPSCODE_RES, comm2shishi);
				} else {
					map.put(Tran.TRANS_CNAPSCODE_RES, cnapsCode);
				}
			}
			
			
		} else {
			map.put(Tran.TRANS_CNAPSCODE_RES, cnapsCode);
		}
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestNationalTransferCommissionChargeCallBack");
	}

	public void requestNationalTransferCommissionChargeCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setCommissionChargeMap(result);
		switch (Integer.parseInt(tranMode)) {
		case 0:// 立即执行
				// 请求安全因子
			BaseHttpEngine.dissMissProgressDialog();
			Intent intent = new Intent();
			intent.setClass(this, NoRelBankOtherConfirmInfoActivity1.class);
			intent.putExtra(TRANS_TYPE, tranTypeFlag);

			if (tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {
				if (common_tag && radio_shishi_orientation.isChecked()) {
					intent.putExtra("comm2shishi", comm2shishi);
				}
			}
			if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
				if(rb_shishi!=null){
					if (common_tag && rb_shishi.isChecked()) {
						intent.putExtra("comm2shishi", comm2shishi);
					}
				}
				if(radio_shishi_orientation!=null){
					if (common_tag && radio_shishi_orientation.isChecked()) {
						intent.putExtra("comm2shishi", comm2shishi);
					}	
				}
				
				
			}
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 查询信用卡详情 PsnCrcdQueryAccountDetail 外币详情
	 * 
	 * @param accountId
	 */
	private void requestForCrcdCurrencyDetail(String accountId) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.CRCDQUERYACCOUNTDETAIL);
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_ID, accountId);
		// 外币
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY, crcdCurrency2);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCrcdCurrencyDetailCallBack");
	}

	public void requestForCrcdCurrencyDetailCallBack(Object resultObj) {
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
		Map<String, String> detailMap = new HashMap<String, String>();
		List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
		detailList = (List<Map<String, String>>) resultMap
				.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		if (StringUtil.isNullOrEmpty(detailList)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		detailMap = detailList.get(0);
		// 首先判断信用卡余额
		String currentflag = (String) detailMap
				.get(Crcd.CRCD_CURRENTBALANCEFLAG);
		if (StringUtil.isNull(currentflag)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		if (currentflag.equals(ConstantGloble.LOAN_CURRENTINDEX_VALUE)) {
			TranDataCenter.getInstance().setCurrInDetail(resultMap);
			requestCommConversationId();

		} else {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().createDialog(null,
					R.string.crcd_foreign_no_owe);
			return;
		}
	}

	/**
	 * 刷新转入账户的币种和余额
	 * 
	 * @param currentBalance
	 */
	private void refreshTranInDataView(String currentBalance, String flag) {
		currencyCodeTv.setText(LocalData.Currency.get(crcdCurrency2));
		// String own = this.getResources().getString(R.string.own);
		// cashTv.setText(StringUtil.parseStringPattern(currentBalance, 2) +
		// own);
		cashTv.setText(flag + StringUtil.parseStringPattern(currentBalance, 2));
	}

	// /**
	// * 查询转出信用卡币种
	// *
	// * @param accountNumber
	// */
	// private void requestForOutCrcdCurrency(String accountNumber) {
	// BiiRequestBody biiRequestBody = new BiiRequestBody();
	// biiRequestBody.setMethod(Tran.CREDITCARDCURRENCYQUERY);
	// Map<String, String> map = new HashMap<String, String>();
	// map.put(Tran.ACCOUNTNUMBER_RES, accountNumber);
	// biiRequestBody.setParams(map);
	// BaseHttpEngine.showProgressDialog();
	// HttpManager.requestBii(biiRequestBody, this,
	// "requestForOutCrcdCurrencyCallBack");
	// }
	//
	// @SuppressWarnings("unchecked")
	// public void requestForOutCrcdCurrencyCallBack(Object resultObj) {
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// Map<String, Object> result = (Map<String, Object>) biiResponseBody
	// .getResult();
	// if (StringUtil.isNullOrEmpty(result)) {
	// BaseHttpEngine.dissMissProgressDialog();
	// return;
	// }
	//
	// // 转出账户信用卡 第一币种 第二币种
	// Map<String, String> currencyMap1 = (Map<String, String>) result
	// .get(Tran.CREDIT_CARD_CURRENCY1_RES);
	// if (!StringUtil.isNullOrEmpty(currencyMap1)) {
	// outCrcdCurrency1 = currencyMap1.get(Crcd.CRCD_CODE);
	// }
	//
	// Map<String, String> currencyMap2 = (Map<String, String>) result
	// .get(Tran.CREDIT_CARD_CURRENCY2_RES);
	// if (!StringUtil.isNullOrEmpty(currencyMap2)) {
	// // 如果存在第二币种 需要查询币种的
	// outCrcdCurrency2 = currencyMap2.get(Crcd.CRCD_CODE);
	// if (!StringUtil.isNull(outCrcdCurrency2)) {
	// // 如果该信用卡存在第二币种 需要查询该币种的详情
	// Map<String, Object> accOutInfoMap = addOutforMap;
	// String accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
	// requestForCrcdDetail(accountId, outCrcdCurrency2);
	// return;
	// }
	// }
	//
	// // 如果是信用卡模块进入 只需要查到信用卡币种就可以了
	// // if(otherPartJumpFlag == ConstantGloble.REL_CRCD_REPAY
	// // || otherPartJumpFlag == ConstantGloble.ACC_TO_TRAN_CRCD){
	// // if(!mAccInLl.isEnabled()){
	// // BiiHttpEngine.dissMissProgressDialog();
	// // return;
	// // }
	// //
	// // }
	//
	// Map<String, Object> accOutInfoMap = addOutforMap;
	// String accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
	// requestForCrcdDetail(accountId);
	// }

	// 402 刚进入转账模块要请求列表数据——从账户管理和信用卡进入
	/**
	 * 调用接口：PsnCommonQueryAllChinaBankAccount 请求给定类型转出账户列表
	 * */
	public void requestTranFromAccountList() {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.COMMONQUERYALLCHINABANKACCOUNT);

		String[] accountTypeArr = new String[] { ConstantGloble.ACC_TYPE_ORD,
				ConstantGloble.ACC_TYPE_BRO, ConstantGloble.ACC_TYPE_RAN,
				ConstantGloble.ACC_TYPE_GRE, ConstantGloble.ZHONGYIN,
				ConstantGloble.SINGLEWAIBI, ConstantGloble.ACC_TYPE_BOCINVT };
		// , AccBaseActivity.YOUHUITONGZH
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.ACCOUNTTYPE_REQ, accountTypeArr);
		biiRequestBody.setParams(map);

		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestTranFromAccountListCallBack");
	}

	/**
	 * 查询账户列表回调
	 */
	@SuppressWarnings("unchecked")
	public void requestTranFromAccountListCallBack(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {// 如果没有符合要求的数据 弹框提示
			BaseHttpEngine.dissMissProgressDialog();
			String message = this.getString(R.string.no_accout);
			BaseDroidApp.getInstanse().showErrorDialog(message,
					R.string.cancle, R.string.confirm, onNoOutclicklistener);
			return;
		}
		List<Map<String, Object>> crcdList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> tranOut = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> bocinvtList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : result) {
			String accType = (String) map.get(Acc.ACC_ACCOUNTTYPE_RES);
			// if (accType.equals(ConstantGloble.ZHONGYIN)
			// || accType.equals(ConstantGloble.SINGLEWAIBI)) {
			// crcdList.add(map);
			// } else
			if (accType.equals(ConstantGloble.ACC_TYPE_BOCINVT)) {
				bocinvtList.add(map);
			} else {
				tranOut.add(map);
			}
		}
		TranDataCenter.getInstance().setCrcdList(crcdList);
		TranDataCenter.getInstance().setAccountOutList(tranOut);
		TranDataCenter.getInstance().setBocinvtList(bocinvtList);
		requestAccInBankAccountList();
	}

	// 402 常用收款人分区显示
	public List<Map<String, Object>> resetCommonInList(
			List<Map<String, Object>> list) {
		List<Map<String, Object>> commInlist = new ArrayList<Map<String, Object>>();
		// 行内
		List<Map<String, Object>> boclist = new ArrayList<Map<String, Object>>();
		// 行内_非关联信用卡还款
		List<Map<String, Object>> bocRelCrcdlist = new ArrayList<Map<String, Object>>();
		// 非关联信用卡还款
		List<Map<String, Object>> relCrcdlist = new ArrayList<Map<String, Object>>();
		// 跨行
		List<Map<String, Object>> otherBanklist = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String bocFlag = (String) map.get(Tran.TRANS_BOCFLAG_REQ);
			String type = (String) map.get(Tran.TRANS_TYPE_RES);
			if (!StringUtil.isNullOrEmpty(bocFlag)) {
				// 根据bocflag 判断 行内 1 跨行0 非关联信用卡 2
				if (bocFlag.equals("0")) {
					otherBanklist.add(map);
				} else if (bocFlag.equals("1")) {
					if (type.equals(ConstantGloble.ZHONGYIN)
							|| type.equals(ConstantGloble.SINGLEWAIBI)
							|| type.equals(ConstantGloble.ACC_TYPE_GRE)) {
						// 非关联信用卡还款流程
						bocRelCrcdlist.add(map);
					} else {
						boclist.add(map);
					}
				} else if (bocFlag.equals("2")) {
					relCrcdlist.add(map);
				}
			}
		}
		commInlist.addAll(boclist);
		commInlist.addAll(bocRelCrcdlist);
		commInlist.addAll(relCrcdlist);
		commInlist.addAll(otherBanklist);
		return commInlist;
	}

	// 请求定向收款人列表返回
	public void psnDirTransQueryPayeeListCallBack(Object resultObj) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		BiiResponseBody biiResponseBody = callBackResponse(resultObj);
		list = (List<Map<String, Object>>) biiResponseBody.getResult();
//		List<Map<String, Object>> copyResult = new ArrayList<Map<String, Object>>();
//		copyResult.addAll(list);
		
		List<Map<String, Object>> dirResultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> direbpsList = new ArrayList<Map<String, Object>>();
		// 过滤掉bocFlag 不是0,1,2的
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String bankName = (String) map
					.get(Tran.TRAN_DIR_QUERYLIST_BANKNAME_RES);
			String address = (String) map
					.get(Tran.TRAN_DIR_QUERYLIST_ADDRESS_RES);
			if (StringUtil.isNullOrEmpty(bankName)) {
				map.put(ISHAVEBANKNAME, false);
			} else {
				map.put(ISHAVEBANKNAME, true);
			}
			if (StringUtil.isNullOrEmpty(address)) {
				map.put(ISHAVEADDRESS, false);
			} else {
				map.put(ISHAVEADDRESS, true);
			}
			String bocFlag = (String) map.get(Tran.TRANS_BOCFLAG_REQ);
//			if (!bocFlag.equals("0") && !bocFlag.equals("1")
//					&& !bocFlag.equals("2")) {
//				copyResult.remove(map);
//
//			}
			if (bocFlag.equals("0") || bocFlag.equals("1")
					|| bocFlag.equals("2")) {
				dirResultList.add(map);
			} else if (bocFlag.equals("3")) {
				direbpsList.add(map);
			}
			// 605 打开定向实时收款人	
		}
//		TranDataCenter.getInstance().setDirpayeeList(copyResult);
		TranDataCenter.getInstance().setDirpayeeList(dirResultList);
		TranDataCenter.getInstance().setEbpsDirList(direbpsList);

		// TODO 请求实时收款人列表
		requestBIIForTran(Tran.PSNEBPSREALTIMEPAYMENTQUERYPAYEE_API,
				new HashMap<String, Object>(),
				"PsnEbpsRealTimePaymentQueryPayeeCallBack");

	}

	// 请求实时收款人列表返回
	public void PsnEbpsRealTimePaymentQueryPayeeCallBack(Object resultObj) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		BiiResponseBody biiResponseBody = callBackResponse(resultObj);
		list = (List<Map<String, Object>>) biiResponseBody.getResult();
		BiiHttpEngine.dissMissProgressDialog();
//		// 定向收款人列表
//		List<Map<String, Object>> dirList = TranDataCenter.getInstance()
//				.getDirpayeeList();
//		List<Map<String, Object>> dirResultList = new ArrayList<Map<String, Object>>();
//		List<Map<String, Object>> direbpsList = new ArrayList<Map<String, Object>>();
//		for (int i = 0; i < dirList.size(); i++) {
//			Map<String, Object> map = dirList.get(i);
//			String bocFlag = (String) map
//					.get(Tran.TRAN_DIR_QUERYLIST_BOCFLAG_REQ);
//			if (bocFlag.equals("0") || bocFlag.equals("1")
//					|| bocFlag.equals("2")) {
//				dirResultList.add(map);
//			} else if (bocFlag.equals("3")) {
//				direbpsList.add(map);
//			}
//		}
//		TranDataCenter.getInstance().setDirpayeeList(dirResultList);
//		TranDataCenter.getInstance().setEbpsDirList(direbpsList);
		TranDataCenter.getInstance().setEbpsList(list);
		isrequestAccIn = true;
		// 显示转入账户类表框
		showAccInListDialog();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_shishi:
			// 暂不做
			// 实时跨行转账
			// 是常用银行 设置银行行号 sunh
			// 不是常用银行 查询所属行 设置银行行号 切所属行为比选项
			if (common_tag) {
				// common_can_shishi=false;
				isAddPayeeChecked = ConstantGloble.TRUE;
				commomchangetoshishi(TranDataCenter.getInstance()
						.getAccInInfoMap());
				BaseHttpEngine.showProgressDialog();
				PsnOtherBankQueryForTransToAccount("HOT");

			}

			tranTypeFlag = TRANTYPE_SHISHI_BANKOTHER;
			containerV.setVisibility(View.GONE);
			if (!StringUtil.isNullOrEmpty(radio_shishi_prompt)) {

				radio_shishi_prompt.setVisibility(View.VISIBLE);
				PopupWindowUtils.getInstance().setOnShowAllTextListener(
						TransferManagerActivity1.this, radio_shishi_prompt);
			}
			// common_tag=false;
			// shishi_tag=true;
			break;
		case R.id.radio_common:

			if (shishi_tag) {
				isAddPayeeChecked = ConstantGloble.TRUE;
				shishichangetocommom(TranDataCenter.getInstance()
						.getAccInInfoMap());
			}
			// 判断是否有开户行——address

			containerV.setVisibility(View.VISIBLE);
			containerV1.setVisibility(View.GONE);
			// 普通跨行转账
			tranTypeFlag = TRANTYPE_NOREL_BANKOTHER;
			Map<String, Object> map = new HashMap<String, Object>();
			map = TranDataCenter.getInstance().getAccInInfoMap();
			String address = (String) map
					.get(Tran.TRAN_DIR_QUERYLIST_ADDRESS_RES);
			if (!StringUtil.isNull(address)) {
				map.put(ISHAVEADDRESS, true);
			}
			TextView accNameTv = (TextView) accInDetailLayout
					.findViewById(R.id.tv_acc_in_item_type);
			final String bankname = accNameTv.getText().toString().trim();

			if (shishi_tag) {

				// 无开户行 请求
				
				String toOrgName = inBankNameEt.getText().toString().trim();
				if (StringUtil.isNullOrEmpty(toOrgName)) {
					inBankNameEt.setVisibility(View.GONE);
					
				}else{
					inBankNameEt.setVisibility(View.VISIBLE);	
				}
			
				kBankBtn.setVisibility(View.VISIBLE);
				kBankBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// 查询开户行
						requestQueryExternalBank(0, 10, bankname);
					}
				});

			} else {
				if (!StringUtil.isNullOrEmpty(map.get(ISHAVEADDRESS))
						&& (Boolean) map.get(ISHAVEADDRESS)
						|| !StringUtil.isNullOrEmpty(map
								.get(Tran.SAVEEBPS_PAYEEORGNAME_REQ))) {
					// 有开户行显示

					inBankNameEt.setVisibility(View.VISIBLE);
					kBankBtn.setVisibility(View.VISIBLE);
					kBankBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// 查询开户行
							requestQueryExternalBank(0, 10, bankname);
						}
					});
					if (!StringUtil.isNullOrEmpty(map
							.get(Tran.SAVEEBPS_PAYEEORGNAME_REQ))) {
						inBankNameEt.setText((String) map
								.get(Tran.SAVEEBPS_PAYEEORGNAME_REQ));
					} else {
						inBankNameEt.setText(address);
					}
				} else {
					// 无开户行 请求
					inBankNameEt.setVisibility(View.GONE);
					kBankBtn.setVisibility(View.VISIBLE);
					kBankBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// 查询开户行
							requestQueryExternalBank(0, 10, bankname);
						}
					});
				}
			}

			if (!StringUtil.isNullOrEmpty(radio_shishi_prompt)) {
				radio_shishi_prompt.setVisibility(View.VISIBLE);
			}
			// common_tag=true;
			// shishi_tag=false;
			break;

		case R.id.radio_shishi_orientation:

			if (common_tag) {
				// 是常用银行 设置银行行号
				// 不是常用银行 查询所属行 设置银行行号 切所属行为比选项
				// common_can_shishi=false;
				isAddPayeeChecked = ConstantGloble.TRUE;
				BaseHttpEngine.showProgressDialog();
				PsnOtherBankQueryForTransToAccount("HOT");
				// String bankName = (String) TranDataCenter.getInstance()
				// .getAccInInfoMap().get(Tran.TRANS_BANKNAME_RES);
				// if (LocalData.kBankList.contains(bankName)) {
				//
				// TranDataCenter
				// .getInstance()
				// .getAccInInfoMap()
				// .put(Tran.TRANS_CNAPSCODE_RES,
				// LocalData.kBankListMap.get(bankName));
				// } else {
				// containerV1.setVisibility(View.VISIBLE);
				// if (!StringUtil.isNullOrEmpty(bankName)) {
				// isBankNameEt.setText(bankName);
				// }
				// isBankBtn.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View arg0) {
				// // 查询所属行
				// PsnOtherBankQueryForTransToAccount("HOT");
				// }
				// });
				// }
				commomchangetoshishi(TranDataCenter.getInstance()
						.getAccInInfoMap());
			}
			tranTypeFlag = TRANTYPE_SHISHI_DIR_BANKOTHER;
//			tranTypeFlag = TRANTYPE_SHISHI_BANKOTHER;
			containerV.setVisibility(View.GONE);
			if (!StringUtil.isNullOrEmpty(radio_shishi_prompt)) {
				radio_shishi_prompt.setVisibility(View.VISIBLE);
			}
			// common_tag=false;
			// shishi_tag=true;
			break;
		case R.id.radio_common_orientation:
			if (shishi_tag) {
				isAddPayeeChecked = ConstantGloble.TRUE;
				shishichangetocommom(TranDataCenter.getInstance()
						.getAccInInfoMap());
			}
			// 判断是否有开户行——address
			containerV.setVisibility(View.VISIBLE);
			containerV1.setVisibility(View.GONE);
			// 普通跨行转账
			tranTypeFlag = TRANTYPE_DIR_BANKOTHER;
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1 = TranDataCenter.getInstance().getAccInInfoMap();
			String address1 = (String) map1
					.get(Tran.TRAN_DIR_QUERYLIST_ADDRESS_RES);
			if (!StringUtil.isNull(address1)) {
				map1.put(ISHAVEADDRESS, true);
			}
			TextView accNameTv1 = (TextView) accInDetailLayout
					.findViewById(R.id.tv_acc_in_item_type);
			final String bankname1 = accNameTv1.getText().toString().trim();

			if (shishi_tag) {

				// 无开户行 请求
				
				String toOrgName = inBankNameEt.getText().toString().trim();
				if (StringUtil.isNullOrEmpty(toOrgName)) {
					inBankNameEt.setVisibility(View.GONE);
					
				}else{
					inBankNameEt.setVisibility(View.VISIBLE);	
				}
				kBankBtn.setVisibility(View.VISIBLE);
				kBankBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// 查询开户行
						requestQueryExternalBank(0, 10, bankname1);
					}
				});

			} else {
				if (!StringUtil.isNullOrEmpty(map1.get(ISHAVEADDRESS))
						&& (Boolean) map1.get(ISHAVEADDRESS)
						|| !StringUtil.isNullOrEmpty(map1
								.get(Tran.SAVEEBPS_PAYEEORGNAME_REQ))) {
					// 有开户行显示
					kBankBtn.setVisibility(View.GONE);
					inBankNameEt.setVisibility(View.VISIBLE);
					if (!StringUtil.isNullOrEmpty(map1
							.get(Tran.SAVEEBPS_PAYEEORGNAME_REQ))) {
						inBankNameEt.setText((String) map1
								.get(Tran.SAVEEBPS_PAYEEORGNAME_REQ));
					} else {
						inBankNameEt.setText(address1);
					}
				} else {
					// 无开户行 请求
					inBankNameEt.setVisibility(View.GONE);
					kBankBtn.setVisibility(View.VISIBLE);
					kBankBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// 查询开户行
							requestQueryExternalBank(0, 10, bankname1);
						}
					});
				}
			}

			if (!StringUtil.isNullOrEmpty(radio_shishi_prompt)) {
				radio_shishi_prompt.setVisibility(View.VISIBLE);
			}
			// common_tag=true;
			// shishi_tag=false;
			break;
		default:
			break;
		}
	}

	private void commomchangetoshishi(Map<String, Object> accInInfoMap) {

		if (!accInInfoMap.containsKey(Tran.EBPSQUERY_PAYEEACTID_REQ)) {
			accInInfoMap.put(Tran.EBPSQUERY_PAYEEACTID_REQ,
					(String) accInInfoMap.get(Tran.TRANS_PAYEETID_RES));
		}
		if (!accInInfoMap.containsKey(Tran.EBPSQUERY_PAYEEACTNO_REQ)) {
			accInInfoMap.put(Tran.EBPSQUERY_PAYEEACTNO_REQ,
					(String) accInInfoMap.get(Tran.ACCOUNTNUMBER_RES));
		}
		if (!accInInfoMap.containsKey(Tran.EBPSQUERY_PAYEEACTNAME_REQ)) {
			accInInfoMap.put(Tran.EBPSQUERY_PAYEEACTNAME_REQ,
					(String) accInInfoMap.get(Tran.ACCOUNTNAME_RES));
		}
		if (!accInInfoMap.containsKey(Tran.EBPSQUERY_PAYEEORGNAME_REQ)) {
			accInInfoMap.put(Tran.EBPSQUERY_PAYEEORGNAME_REQ,
					(String) accInInfoMap.get(Tran.TRANS_ADDRESS_RES));
		}
		if (!accInInfoMap.containsKey(Tran.EBPSQUERY_PAYEEBANKNAME_REQ)) {
			accInInfoMap.put(Tran.EBPSQUERY_PAYEEBANKNAME_REQ,
					(String) accInInfoMap.get(Tran.TRANS_BANKNAME_RES));
		}
		if (!accInInfoMap.containsKey(Tran.EBPSQUERY_PAYEECNAPS_REQ)) {
			accInInfoMap.put(Tran.EBPSQUERY_PAYEECNAPS_REQ,
					(String) accInInfoMap.get(Tran.TRANS_CNAPSCODE_RES));
		}

		TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
	}

	private void shishichangetocommom(Map<String, Object> accInInfoMap) {
		if (!accInInfoMap.containsKey(Tran.TRANS_PAYEETID_RES)) {
			accInInfoMap.put(Tran.TRANS_PAYEETID_RES,
					(String) accInInfoMap.get(Tran.EBPSQUERY_PAYEEACTID_REQ));
		}
		if (!accInInfoMap.containsKey(Tran.ACCOUNTNUMBER_RES)) {
			accInInfoMap.put(Tran.ACCOUNTNUMBER_RES,
					(String) accInInfoMap.get(Tran.EBPSQUERY_PAYEEACTNO_REQ));
		}
		if (!accInInfoMap.containsKey(Tran.ACCOUNTNAME_RES)) {
			accInInfoMap.put(Tran.ACCOUNTNAME_RES,
					(String) accInInfoMap.get(Tran.EBPSQUERY_PAYEEACTNAME_REQ));
		}
		if (!accInInfoMap.containsKey(Tran.TRANS_ADDRESS_RES)) {
			accInInfoMap.put(Tran.TRANS_ADDRESS_RES,
					(String) accInInfoMap.get(Tran.EBPSQUERY_PAYEEORGNAME_REQ));
		}
		if (!accInInfoMap.containsKey(Tran.TRANS_BANKNAME_RES)) {
			accInInfoMap
					.put(Tran.TRANS_BANKNAME_RES, (String) accInInfoMap
							.get(Tran.EBPSQUERY_PAYEEBANKNAME_REQ));
		}
		if (!accInInfoMap.containsKey(Tran.TRANS_CNAPSCODE_RES)) {
			accInInfoMap.put(Tran.TRANS_CNAPSCODE_RES,
					(String) accInInfoMap.get(Tran.EBPSQUERY_PAYEECNAPS_REQ));
		}
		TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
	}

	/**
	 * 转账银行开户行查询 
	 */
	public void requestQueryExternalBank(int currentIndex, int pageSize,
			String bankname) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.QUERYEXTERNALBANKINFO);
		Map<String, String> map = new HashMap<String, String>();
		// 银行行号
//		if (StringUtil.isNull(LocalData.kBankListMap.get(bankname))) {
//			// String cnapsCode = (String)
//			// TranDataCenter.getInstance().getAccInInfoMap().get(Tran.TRANS_NATIONAL_CNAPSCODE_REQ);
//			// if(cnapsCode.length()>3){
//			// cnapsCode=cnapsCode.substring(0, 3);
//			// }else{
//			String cnapsCode = "OTHER";
//			// }
//
//			map.put(Tran.PAYEE_TOBANKCODE_REQ, cnapsCode);
//			// map.put(Tran.PAYEE_TOBANKCODE_REQ, "OTHER");
//		} else {
//			map.put(Tran.PAYEE_TOBANKCODE_REQ,
//					LocalData.kBankListMap.get(bankname));
//		}
		map.put(Tran.PAYEE_TOBANKCODE_REQ, "OTHER");
		for(Map.Entry<String, String>entity:LocalData.kBankListMap.entrySet()){
			if(bankname.contains(entity.getKey())){
				map.put(Tran.PAYEE_TOBANKCODE_REQ,entity.getValue());
				break;
			}
		}
		// 银行名称
		map.put(Tran.PAYEE_BANKNAME_REQ, "");
		// 当前页
		map.put(Tran.PAYEE_CURRENTINDEX_REQ, currentIndex + "");
		// 每页显示条数
		map.put(Tran.PAYEE_PAGESIZE_REQ, pageSize + "");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"queryExternalBankCallBack");
	}

	/**
	 * 转账银行开户行查询
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 * @return tokenId
	 */
	@SuppressWarnings("unchecked")
	public void queryExternalBankCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		String recordNumber = (String) result.get(Tran.RECORD_NUMBER);

		// if(Integer.parseInt(recordNumber)<=0){
		// BaseDroidApp.getInstanse().showInfoMessageDialog("shibai");
		// return;
		// }
		List<Map<String, String>> queryExternalBankList = (List<Map<String, String>>) result
				.get(ConstantGloble.LIST);
		TranDataCenter.getInstance().setExternalBankList(queryExternalBankList);
		// 跳转到下个列表页面
		Intent intent = new Intent(this, QueryExternalBankActivity1.class);
		intent.putExtra(Tran.RECORD_NUMBER, recordNumber);
		TextView accNameTv = (TextView) accInDetailLayout
				.findViewById(R.id.tv_acc_in_item_type);
		final String bankname = accNameTv.getText().toString().trim();
		// if(StringUtil.isNull(LocalData.kBankListMap.get(bankname))){
		// String cnapsCode = (String)
		// TranDataCenter.getInstance().getAccInInfoMap().get(Tran.TRANS_NATIONAL_CNAPSCODE_REQ);
		// if(cnapsCode.length()>3){
		// cnapsCode=cnapsCode.substring(0, 3);
		// }else{
		// cnapsCode="OTHER";
		// }
		// intent.putExtra(Tran.PAYEE_TOBANKCODE_REQ, cnapsCode);
		// }
		intent.putExtra(Tran.PAYEE_BANKNAME_REQ, bankname);
		startActivityForResult(intent, ConstantGloble.PAYEE_OTHERBANK);
	}

	/**
	 * 实时转账——所属银行模糊查询
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	@SuppressWarnings("unchecked")
	public void psnEbpsQueryAccountOfBankCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		String recordNumber = (String) result
				.get(Tran.EBPSQUERY_RECORDNUMBER_RES);

		List<Map<String, String>> queryExternalBankList = (List<Map<String, String>>) result
				.get(Tran.EBPSQUERY_ACCOUNTOFBANKLIST_RES);
		TranDataCenter.getInstance().setShishiBankList(queryExternalBankList);
		// 跳转到下个列表页面
		Intent intent = new Intent(this, QueryExternalBankActivity1.class);
		intent.putExtra(Tran.RECORD_NUMBER, recordNumber);
		intent.putExtra(ISSHISHITYPE, true);
		startActivityForResult(intent, ConstantGloble.CHOOSE_SHISHIBANK);
	}

	/**
	 * 国内跨行实时汇款预交易
	 */
	public void psnEbpsRealTimePaymentConfirm() {

		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		String fromAccountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String payeeActno = (String) accInInfoMap
				.get(Tran.EBPSQUERY_PAYEEACTNO_REQ);
		String payeeName = (String) accInInfoMap
				.get(Tran.EBPSQUERY_PAYEEACTNAME_REQ);
		String bankName = (String) accInInfoMap
				.get(Tran.EBPSQUERY_PAYEEBANKNAME_REQ);
		String toOrgName = (String) accInInfoMap
				.get(Tran.EBPSQUERY_PAYEEBANKNAME_REQ);
		String cnapsCode = (String) accInInfoMap
				.get(Tran.EBPSQUERY_PAYEECNAPS_REQ);

		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		// 跨行
		biiRequestBody.setMethod(Tran.PSNEBPSREALTIMEPAYMENTCONFIRM_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		if (isSendSmc.equals(ConstantGloble.FALSE)) {
			payeeMobile = "";
		} else {
			map.put(Tran.EBPSREAL_SENDMSGFLAG_REQ, "1");
		}
		map.put(Tran.TRANS_BOCNATIONAL_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.EBPSQUERY_PAYEEACTNO2_REQ, payeeActno);
		map.put(Tran.EBPSQUERY_PAYEEACTNO_REQ, payeeActno);
		map.put(Tran.SAVEEBPS_PAYEENAME_REQ, payeeName);
		map.put(Tran.EBPSQUERY_PAYEEBANKNAME_REQ, bankName);
		map.put(Tran.EBPSQUERY_PAYEEORGNAME_REQ, toOrgName);

		if(rb_shishi!=null){
			if (common_tag && rb_shishi.isChecked()) {
				map.put(Tran.EBPSQUERY_PAYEECNAPS_REQ, comm2shishi);
			} else {
				map.put(Tran.EBPSQUERY_PAYEECNAPS_REQ, cnapsCode);
			}
		}
		if(radio_shishi_orientation!=null){
			if (common_tag && radio_shishi_orientation.isChecked()) {
				map.put(Tran.EBPSQUERY_PAYEECNAPS_REQ, comm2shishi);
			} else {
				map.put(Tran.EBPSQUERY_PAYEECNAPS_REQ, cnapsCode);
			}	
		}
		
		
		map.put(Tran.TRANS_BOCNATIONAL_REMITTANCEINFO_REQ, "");
		map.put(Tran.TRANS_BOCNATIONAL_AMOUNT_REQ, amount);
		map.put(Tran.TRANS_BOCNATIONAL_PAYEEMOBILE_REQ, payeeMobile);
		map.put(Tran.EBPSREAL_MEMO_REQ, memo);

		map.put(Tran.TRANS_BOCNATIONAL_CURRENCY_REQ,
				ConstantGloble.PRMS_CODE_RMB);
		map.put(Tran.TRANS_BOCNATIONAL_EXECUTETYPE_REQ, ConstantGloble.NOWEXE);
		map.put(Tran.TRANS_BOCNATIONAL_EXECUTEDATE_REQ, "");
		map.put(Tran.TRANS_BOCNATIONAL__COMBINID_REQ, combineId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"psnEbpsRealTimePaymentConfirmCallBack");
	}

	/**
	 * 请求国内实时跨行预交易返回
	 * 
	 * @param resultObj
	 */
	public void psnEbpsRealTimePaymentConfirmCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setNoRelBankOtherCallBackMap(result);

		if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
			requestNationalTransferCommissionCharge(ConstantGloble.PB113);
		}
	}

	/**
	 * 网上专属理财账户状态查询
	 */
	private void requestAcctOF() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.OFAACCOUNTSTATEQUERY);
		biiRequestBody.setParams(null);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "acctOFCallBack");
	}

	@SuppressWarnings("unchecked")
	public void acctOFCallBack(Object resultObj) {
		BiiResponseBody biiResponseBody = callBackResponse(resultObj);
		Map<String, Object> accmap = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setAccmap(accmap);
		String openStatus = (String) accmap.get(BocInvt.OPENSTATUS);
		Map<String, Object> bankMap = (Map<String, Object>) accmap
				.get(BocInvt.BANKACCOUNT);
		// 状态
		if (!StringUtil.isNullOrEmpty(openStatus)
				&& openStatus.equals(BocInvt.OPENSTATUS_S)
				&& !StringUtil.isNullOrEmpty(bankMap)) {
			// 已开通,有绑定账户——可以继续转账,需要请求详情
			Map<String, Object> accOutInfoMap = addOutforMap;
			String accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
			requestForAccountDetail(accountId);
		} else {
			// 不可以转账报错
			BiiHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					"您的网上专属理财账户不可以进行转账");
		}
	}

	@Override
	public void PsnOtherBankQueryForTransToAccountCallBack(Object resultObj) {
		super.PsnOtherBankQueryForTransToAccountCallBack(resultObj);

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		String recordNumber = (String) result
				.get(Tran.EBPSQUERY_RECORDNUMBER_RES);

		List<Map<String, String>> queryExternalBankList = (List<Map<String, String>>) result
				.get(Tran.EBPSQUERY_ACCOUNTOFBANKLIST_RES);
		for (int i = 0; i < queryExternalBankList.size(); i++) {
			queryExternalBankList.get(i).put("flag", "Y");
		}
		TranDataCenter.getInstance().setShishiBankList(queryExternalBankList);
		BaseHttpEngine.dissMissProgressDialog();
		String bankName = (String) TranDataCenter.getInstance()
				.getAccInInfoMap().get(Tran.TRANS_BANKNAME_RES);

		// String bankCode = (String) TranDataCenter.getInstance()
		// .getAccInInfoMap().get(Tran.TRANS_BANKCODE_RES);
		String cnapsCode = (String) TranDataCenter.getInstance()
				.getAccInInfoMap().get(Tran.TRANS_NATIONAL_CNAPSCODE_REQ);

		if (filledData(queryExternalBankList, cnapsCode)) {
			// 可以切

		} else {
			containerV1.setVisibility(View.VISIBLE);
			isBankBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// 查询所属行
					PsnOtherBankQueryForTransToAccount2("OTHER");
				}
			});
		}
		// 查询其他银行
		// PsnOtherBankQueryForTransToAccount2("OTHER");
	}

	@Override
	public void PsnOtherBankQueryForTransToAccountCallBack2(Object resultObj) {
		super.PsnOtherBankQueryForTransToAccountCallBack2(resultObj);

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		// String recordNumber = (String) result
		// .get(Tran.EBPSQUERY_RECORDNUMBER_RES);

		List<Map<String, String>> queryExternalBankList = (List<Map<String, String>>) result
				.get(Tran.EBPSQUERY_ACCOUNTOFBANKLIST_RES);

		List<Map<String, String>> ShishiBankList = TranDataCenter.getInstance()
				.getShishiBankList();
		ShishiBankList.addAll(queryExternalBankList);
		TranDataCenter.getInstance().setShishiBankList(ShishiBankList);
		BaseHttpEngine.dissMissProgressDialog();
		// 跳转到下个列表页面
		Intent intent = new Intent(TransferManagerActivity1.this,
				OrderMainActivity.class);
		// intent.putExtra(Tran.RECORD_NUMBER, recordNumber);
		intent.putExtra(ISSHISHITYPE, true);
		startActivityForResult(intent, ConstantGloble.CHOOSE_SHISHIBANK_NEW);
	}

	private Boolean filledData(List<Map<String, String>> date, String cnapsCode) {
		// 要修改

		// if (StringUtil.isNull(bankCode)) {
		// return false;
		// }
		if (StringUtil.isNull(cnapsCode) || cnapsCode.length() < 3) {
			return false;
		}
		String cnapsCodenew = cnapsCode.substring(0, 3);
		for (int i = 0; i < date.size(); i++) {
			Map<String, String> dataMap = date.get(i);

			String bankbtp = dataMap.get(Tran.EBPSQUERY_BANKBTP_RES);
			// String bankname=dataMap.get(Tran.EBPSQUERY_BANKNAME_RES);
			if (cnapsCodenew.equals(bankbtp)) {

				comm2shishi = dataMap.get(Tran.TRANS_BANKCODE_RES);

				return true;
			}

		}
		return false;

	}

	// 转入账户是 非信用卡 查询详情 无子账户时 对应特定的错误 屏蔽 让程序可以继续执行下去
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (Tran.ACCOUNTDETAIL.equals(biiResponseBody.getMethod())
				&& outOrInFlag == IN) {
			BaseHttpEngine.dissMissProgressDialog();
			BiiError biiError = biiResponseBody.getError();
			if (biiError != null) {
				if (biiError.getCode() != null
						&& "AccQueryDetailAction.NoSubAccount".equals(biiError
								.getCode())) {
					inQueryDetailIntercept = true;

					BaseHttpEngine.dissMissProgressDialog();
					// TranDataCenter.getInstance().setCurrInDetail(resultMap);
					TranDataCenter.getInstance().setAccInInfoMap(addInforMap);
					BaseDroidApp.getInstanse().dismissMessageDialog();
					// 405 如果是专属理财账户,
					String tranOutType = (String) addOutforMap
							.get(Comm.ACCOUNT_TYPE);
					if (tranOutType.equals(ConstantGloble.ACC_TYPE_BOCINVT)) {
						isTranInFirst = false;
						mAccInLl.setEnabled(false);
						showAccOutDetailView();
						// 组装数据
						combineData();
					} else {
						mAccInLl.setEnabled(true);
					}
					showAccInDetailView();
					// 非信用卡 关联账户转账 改变底部视图
					// 如果转出账户是信用卡
					Map<String, Object> accOutInfoMap = TranDataCenter
							.getInstance().getAccOutInfoMap();
					Map<String, Object> accInInfoMap1 = TranDataCenter
							.getInstance().getAccInInfoMap();
					String accType = (String) accOutInfoMap
							.get(Comm.ACCOUNT_TYPE);
					String accType1 = (String) accInInfoMap1
							.get(Comm.ACCOUNT_TYPE);

					if (accType.equals(ConstantGloble.ACC_TYPE_GRE)) {
						// 判断转出账户 是否是信用卡 转入账户是否是借记卡
						if (isCreditRemit()) {
							initRelTran();
						} else {
							combineCrcdData();
							initRelCrcdTran();
						}

					} else {
						initRelTran();
					}
					return true;
				}
			}
		}

		return super.httpRequestCallBackPre(resultObj);
	}

	/* 判断是否为新信用卡*/
	private boolean isxycrcd(String type) {
		if (type.equals(ConstantGloble.ACC_TYPE_GRE)
				|| type.equals(ConstantGloble.ZHONGYIN)
				|| type.equals(ConstantGloble.SINGLEWAIBI)) {
			return true;
		} else {
			return false;
		}
	}

	// P503 统一信用卡还款 start
	private void initinlistandbuttom(Map<String, Object> addInforMap,
			Map<String, Object> currInDetail) {

		showCrcdInDetailView(addInforMap);

		showCrcdBottomView(addInforMap, currInDetail);

	}

	private void showCrcdInDetailView(Map<String, Object> addInforMap) {

		accInDetailLayout = (LinearLayout) inflater.inflate(
				R.layout.crcd_in_list_detail, null);
		mAccInLl.removeAllViews();
		mAccInLl.addView(accInDetailLayout);
		mAccInLl.setVisibility(View.VISIBLE);
		newAddTranInBtn.setVisibility(View.GONE);
		showCrcdInListItemData(accInDetailLayout, addInforMap);
		isTranInFirst = false;
	}

	private void showCrcdInListItemData(View view,
			Map<String, Object> accInInfoMap) {

		if (accInInfoMap == null) {
			return;
		}
		crcd_in_layout_one = (LinearLayout) view
				.findViewById(R.id.crcd_in_layout_one);
		crcd_in_currey1 = (TextView) view.findViewById(R.id.crcd_in_currey1);
		crcd_in_currey1_value = (TextView) view
				.findViewById(R.id.crcd_in_currey1_value);
		crcd_in_layout_two = (LinearLayout) view
				.findViewById(R.id.crcd_in_layout_two);
		crcd_in_currey2 = (TextView) view.findViewById(R.id.crcd_in_currey2);
		crcd_in_currey2_value = (TextView) view
				.findViewById(R.id.crcd_in_currey2_value);
		crcd_in_nickName = (TextView) view.findViewById(R.id.crcd_in_nickName);
		crcd_in_nickName.setText((String) accInInfoMap
				.get(Crcd.CRCD_ACCTNAME_RES));
		crcd_in_accountType = (TextView) view
				.findViewById(R.id.crcd_in_accountType);
		String strAccountType = (String) accInInfoMap.get(Comm.ACCOUNT_TYPE);
		String strAccountT = LocalData.AccountType.get(strAccountType);
		crcd_in_accountType.setText(strAccountT);
		// crcd_in_accountType.setText((String) accInInfoMap
		// .get(Crcd.CRCD_PRODUCTNAME));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				crcd_in_accountType);
		crcd_in_accountType_value = (TextView) view
				.findViewById(R.id.crcd_in_accountType_value);
		String accoutNumber = (String) accInInfoMap.get(Crcd.CRCD_ACCTNUM);
		crcd_in_accountType_value.setText(StringUtil
				.getForSixForString(accoutNumber));
		crcd_in_layout_one.setVisibility(View.GONE);
		crcd_in_layout_two.setVisibility(View.GONE);

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> actlist = (List<Map<String, Object>>) addInforMap
				.get(Crcd.CRCD_ACTLIST);
		if (StringUtil.isNullOrEmpty(actlist)) {
			return;
		}

		// 显示人名币的那个数据
		int height = mAccInLlHeight;
		for (int i = 0; i < actlist.size(); i++) {

			if (crcd_in_layout_one.getVisibility() == view.GONE) {
				crcd_in_layout_one.setVisibility(View.VISIBLE);

				String flag = (String) actlist.get(i).get(
						Crcd.CRCD_RTBALANCEFLAG);
				flag = flagConvert(flag);
				String Balance = (String) actlist.get(i).get(
						Crcd.CRCD_REALTIMEBALANCE);
				String currencyCode1 = (String) actlist.get(i).get(
						Crcd.CRCD_CURRENCY_RES);
				crcd_in_currey1.setText(LocalData.Currency.get(currencyCode1));
				crcd_in_currey1_value.setText(flag
						+ StringUtil.parseStringCodePattern(currencyCode1,
								Balance, 2));
				continue;
			} else if (crcd_in_layout_two.getVisibility() == view.GONE) {

				crcd_in_layout_two.setVisibility(View.VISIBLE);
				height = mAccInLlHeight + mAccInLlHeight / 5;
				String currencyCodeCard = (String) actlist.get(i).get(
						Crcd.CRCD_CURRENCY_RES);
				crcd_in_currey2.setText(LocalData.Currency
						.get(currencyCodeCard));
				String flag = (String) actlist.get(i).get(
						Crcd.CRCD_RTBALANCEFLAG);
				flag = flagConvert(flag);
				String Balance = (String) actlist.get(i).get(
						Crcd.CRCD_REALTIMEBALANCE);

				crcd_in_currey2_value.setText(flag
						+ StringUtil.parseStringCodePattern(currencyCodeCard,
								Balance, 2));
			}

		}

		boolean restore = true;// 时候需恢复默认转入账户控件高度
		// 是否需要回复默认高度
		if (restore) {

			LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, height);
			mAccInLl.setLayoutParams(ll);
		}

	}

	private void showCrcdBottomView(Map<String, Object> addInforMap,
			Map<String, Object> currInDetail) {
		View v;

		List<Map<String, Object>> actlist = (List<Map<String, Object>>) addInforMap
				.get(Crcd.CRCD_ACTLIST);
		if (actlist.size() == 1) {
			inCrcdCurrency1 = String.valueOf(actlist.get(0).get(
					Crcd.CRCD_CURRENCY_RES));
			inCrcdCurrency2 = null;
		} else if (actlist.size() == 2) {
			inCrcdCurrency1 = String.valueOf(actlist.get(0).get(
					Crcd.CRCD_CURRENCY_RES));
			inCrcdCurrency2 = String.valueOf(actlist.get(1).get(
					Crcd.CRCD_CURRENCY_RES));
		}

		incurrencyList = new ArrayList<String>();
		for (int i = 0; i < actlist.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put(Crcd.CRCD_RTBALANCEFLAG,
					String.valueOf(actlist.get(i).get(Crcd.CRCD_RTBALANCEFLAG)));
			map.put(Crcd.CRCD_REALTIMEBALANCE, String.valueOf(actlist.get(i)
					.get(Crcd.CRCD_REALTIMEBALANCE)));
			map.put(Crcd.CRCD_CURRENCY_RES,
					String.valueOf(actlist.get(i).get(Crcd.CRCD_CURRENCY_RES)));

			if ("001".equals(String.valueOf(actlist.get(i).get(
					Crcd.CRCD_CURRENCY_RES)))) {
				incurrencyList.add(
						0,
						String.valueOf(actlist.get(i).get(
								Crcd.CRCD_CURRENCY_RES)));

				billAmout_cn = String.valueOf(actlist.get(i).get(
						Crcd.CRCD_BILLAMOUT)); // 本期账单金额
				haveNotRepayAmout_cn = String.valueOf(actlist.get(i).get(
						Crcd.CRCD_HAVENOTREPAYAMOUT)); // 本期未还款金额
				billLimitAmout_cn = String.valueOf(actlist.get(i).get(
						Crcd.CRCD_BILLLIMITAMOUT)); // 本期最低还款金额
				falg_cn = String.valueOf(actlist.get(i).get(
						Crcd.CRCD_RTBALANCEFLAG));
			} else {
				incurrencyList.add(String.valueOf(actlist.get(i).get(
						Crcd.CRCD_CURRENCY_RES)));
				billAmout = String.valueOf(actlist.get(i).get(
						Crcd.CRCD_BILLAMOUT)); // 本期账单金额
				haveNotRepayAmout = String.valueOf(actlist.get(i).get(
						Crcd.CRCD_HAVENOTREPAYAMOUT)); // 本期未还款金额
				billLimitAmout = String.valueOf(actlist.get(i).get(
						Crcd.CRCD_BILLLIMITAMOUT)); // 本期最低还款金额
				falg_wb = String.valueOf(actlist.get(i).get(
						Crcd.CRCD_RTBALANCEFLAG));
			}

			inCreditCurrencyAndBalance.add(map);
		}
		// 新判断条件

		v = mInflater.inflate(R.layout.tran_crcd_bottom_layout, null);
		crcdinit(v);

		if (actlist.size() == 1) {

			if (!StringUtil.isNull(haveNotRepayAmout_cn)) {
				// 人民币
				if (Double.parseDouble(haveNotRepayAmout_cn) > 0) {
					// 人民币有欠款 还款
					cansamecard = false;
					ll_paytype.setVisibility(View.GONE);
					currencyCode = ConstantGloble.PRMS_CODE_RMB;
					cashRemitCode = ConstantGloble.CASHREMIT_00;
					radio_huankuan.performClick();
					radio_payall.performClick();
				} else {
					cansamecard = false;
					// 人民币无欠款 行内
					ll_paytype.setVisibility(View.GONE);
					ll_paymode.setVisibility(View.GONE);
					ll_gouhui.setVisibility(View.GONE);
					currencyCode = ConstantGloble.PRMS_CODE_RMB;
					cashRemitCode = ConstantGloble.CASHREMIT_00;
					tv_huankuan_count.setText(getResources().getString(
							R.string.transferAmount));
					crcd_trans_set_title.setText(getResources().getString(
							R.string.comm_boc_trans));
					tranTypeFlag = TRANTYPE_REL_ACCOUNT;
				}
			}
			if (!StringUtil.isNull(haveNotRepayAmout)) {
				// 外币
				if (Double.parseDouble(haveNotRepayAmout) > 0) {
					// 外币有欠款 还款
					cansamecard = false;
					crcd_currency.setSelection(0);
					crcd_cashRemit.setSelection(-1);
					currencyCode = inCrcdCurrency1;
					cashRemitCode = null;
					radio_huankuan.performClick();
					radio_payall.performClick();
					radio_gouhuiall.performClick();
				} else {
					// 外币无欠款 行内
					cansamecard = false;
					ll_paytype.setVisibility(View.GONE);
					ll_paymode.setVisibility(View.GONE);
					ll_gouhui.setVisibility(View.GONE);
					currencyCode = inCrcdCurrency1;
					cashRemitCode = ConstantGloble.CASHREMIT_00;
					tv_huankuan_count.setText(getResources().getString(
							R.string.transferAmount));
					crcd_trans_set_title.setText(getResources().getString(
							R.string.comm_boc_trans));
					tranTypeFlag = TRANTYPE_REL_ACCOUNT;
				}

			}
		}
		if (actlist.size() == 2) {

			if ((!StringUtil.isNull(haveNotRepayAmout_cn)
					&& Double.parseDouble(haveNotRepayAmout_cn) > 0 && (!StringUtil
					.isNull(haveNotRepayAmout) && Double
					.parseDouble(haveNotRepayAmout) > 0))) {
				// 两个币种都有欠款
				cansamecard = false;
				crcd_currency.setSelection(0);// 默认人民币
				crcd_cashRemit.setSelection(0);
				currencyCode = ConstantGloble.PRMS_CODE_RMB;
				cashRemitCode = ConstantGloble.CASHREMIT_00;
				radio_huankuan.performClick();
				radio_payall.performClick();
				radio_gouhuiall.performClick();
			} else if ((!StringUtil.isNull(haveNotRepayAmout_cn)
					&& Double.parseDouble(haveNotRepayAmout_cn) > 0 && (!StringUtil
					.isNull(haveNotRepayAmout) && Double
					.parseDouble(haveNotRepayAmout) <= 0))) {
				cansamecard = false;
				// 人民币有欠款 外币无欠款
				crcd_currency.setSelection(0);
				crcd_cashRemit.setSelection(-1);
				currencyCode = inCrcdCurrency1;
				cashRemitCode = ConstantGloble.CASHREMIT_00;
				ll_paytype.setVisibility(View.GONE);
				radio_huankuan.performClick();
				radio_payall.performClick();

			} else if ((!StringUtil.isNull(haveNotRepayAmout_cn)
					&& Double.parseDouble(haveNotRepayAmout_cn) <= 0 && (!StringUtil
					.isNull(haveNotRepayAmout) && Double
					.parseDouble(haveNotRepayAmout) > 0))) {
				// 外币有欠款 人民币无欠款
				cansamecard = true;
				crcd_currency.setSelection(1);
				crcd_cashRemit.setSelection(-1);
				currencyCode = inCrcdCurrency2;
				cashRemitCode = null;
				radio_huankuan.performClick();
				radio_payall.performClick();
				radio_gouhuiall.performClick();
			} else if ((!StringUtil.isNull(haveNotRepayAmout_cn)
					&& Double.parseDouble(haveNotRepayAmout_cn) <= 0 && (!StringUtil
					.isNull(haveNotRepayAmout) && Double
					.parseDouble(haveNotRepayAmout) <= 0))) {
				// 2个币种都无欠款 行内
				cansamecard = false;
				ll_paytype.setVisibility(View.GONE);
				ll_paymode.setVisibility(View.GONE);
				ll_gouhui.setVisibility(View.GONE);
				currencyCode = ConstantGloble.PRMS_CODE_RMB;
				cashRemitCode = ConstantGloble.CASHREMIT_00;
				tv_huankuan_count.setText(getResources().getString(
						R.string.transferAmount));
				crcd_trans_set_title.setText(getResources().getString(
						R.string.comm_boc_trans));
				tranTypeFlag = TRANTYPE_REL_ACCOUNT;
			}

		}

		bottomLayout.removeAllViews();
		bottomLayout.addView(v);

	}

	private void crcdinit(View v) {
		// 信用卡还款
		// 信用卡还款 购汇还款
		radioGroupForpaytype = (RadioGroup) v
				.findViewById(R.id.radioGroupForpaytype);
		radio_huankuan = (RadioButton) v.findViewById(R.id.radio_huankuan);
		radio_gouhui = (RadioButton) v.findViewById(R.id.radio_gouhui);
		radioGroupForpaytype.setOnCheckedChangeListener(radiogrouplistener);
		ll_paytype = (LinearLayout) v.findViewById(R.id.ll_paytype);
		ll_paymode = (LinearLayout) v.findViewById(R.id.ll_paymode);
		ll_huankuan = (LinearLayout) v.findViewById(R.id.ll_huankuan);
		ll_gouhui = (LinearLayout) v.findViewById(R.id.ll_gouhui);

		ll_input = (LinearLayout) v.findViewById(R.id.ll_input);

		ll_next = (LinearLayout) v.findViewById(R.id.ll_next);
		ll_more_next = (LinearLayout) v.findViewById(R.id.ll_more_next);
		tv_huankuan_count = (TextView) v.findViewById(R.id.tv_huankuan_count);
		crcd_trans_set_title = (TextView) v
				.findViewById(R.id.crcd_trans_set_title);
		// 还款方式 全额，最小，自定义
		radioGroupForpaymode = (RadioGroup) v
				.findViewById(R.id.radioGroupForpaymode);
		radioGroupForpaymode.setOnCheckedChangeListener(radiogrouplistener);
		radio_payall = (RadioButton) v.findViewById(R.id.radio_payall);
		radio_paylimit = (RadioButton) v.findViewById(R.id.radio_paylimit);
		radio_payself = (RadioButton) v.findViewById(R.id.radio_payself);
		// 购汇方式 全额 部分
		radioGroupForgouhuitype = (RadioGroup) v
				.findViewById(R.id.radioGroupForgouhuitype);
		radioGroupForgouhuitype.setOnCheckedChangeListener(radiogrouplistener);
		radio_gouhuiall = (RadioButton) v.findViewById(R.id.radio_gouhuiall);
		radio_gouhuipart = (RadioButton) v.findViewById(R.id.radio_gouhuipart);
		crcd_card_currency = (TextView) v.findViewById(R.id.crcd_card_currency);
		// 币种 钞汇
		crcd_currency = (Spinner) v.findViewById(R.id.crcd_currency);
		crcd_cashRemit = (Spinner) v.findViewById(R.id.crcd_cashRemit);
		ll_cashRemit = (LinearLayout) v.findViewById(R.id.ll_cashRemit);

		// 特殊 外币有欠款 交集只有人民币 处理
		credit_new_info = (TextView) v.findViewById(R.id.credit_new_info);
		SpannableString sp = new SpannableString(
				this.getString(R.string.credit_new_info));
//		final Intent userIntent = new Intent();
//
//		userIntent.setClass(TransferManagerActivity1.this,
//				AccInputRelevanceAccountActivity.class);

		sp.setSpan(new IntentSpan(new OnClickListener() {

			public void onClick(View view) {
				Refreshcrcdbottomview(position);
			}

		}), 34, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		credit_new_info.setText(sp);
		credit_new_info.setMovementMethod(LinkMovementMethod.getInstance());
		// 还款金额 附言 购汇金额
		et_huankuan_count = (EditText) v.findViewById(R.id.et_huankuan_count);
//		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
//		EditTextUtils.relateNumInputToChineseShower(et_huankuan_count,
//				tv_for_amount);
		et_huankuan_fuyan = (EditText) v.findViewById(R.id.et_huankuan_fuyan);
		et_gouhui_count = (EditText) v.findViewById(R.id.et_gouhui_count);
//		TextView tv_for_gouhuiamount = (TextView) v.findViewById(R.id.tv_for_gouhuiamount);
//		EditTextUtils.relateNumInputToChineseShower(et_gouhui_count,
//				tv_for_gouhuiamount);

		Button btn_crcd_next = (Button) v.findViewById(R.id.btn_crcd_next);

		if (!StringUtil.isNullOrEmpty(incurrencyList)) {
			// 如果全球交易人民币记账功能 开通 只支持人名币
			// incurrencyList= new ArrayList<String>();
			incurrencyflagList = new ArrayList<String>();
			incashRemitList = new ArrayList<String>();
			incashRemitflagList = new ArrayList<String>();
			if (isOpenFlag) {
				incurrencyflagList.add(LocalData.Currency.get("001"));
			} else {
				for (int i = 0; i < incurrencyList.size(); i++) {
					String currencyCode = incurrencyList.get(i);
					incurrencyflagList
							.add(LocalData.Currency.get(currencyCode));
				}

			}

			incashRemitflagList.add(LocalData.CurrencyCashremit
					.get(ConstantGloble.CASHREMIT_00));
			incurrencyAdapter = new ArrayAdapter<String>(
					TransferManagerActivity1.this,
					R.layout.custom_spinner_item, incurrencyflagList);
			crcd_currency.setAdapter(incurrencyAdapter);
			crcd_currency.setEnabled(false);
			crcd_currency.setBackgroundDrawable(TransferManagerActivity1.this
					.getResources().getDrawable(R.drawable.bg_spinner_default));
			crcd_cashRemit.setEnabled(false);
			crcd_cashRemit.setBackgroundDrawable(TransferManagerActivity1.this
					.getResources().getDrawable(R.drawable.bg_spinner_default));
			incashRemitAdapter = new ArrayAdapter<String>(
					TransferManagerActivity1.this,
					R.layout.custom_spinner_item, incashRemitflagList);
			crcd_cashRemit.setAdapter(incashRemitAdapter);
			cashRemitCode = ConstantGloble.CASHREMIT_00;
		} else {
			crcd_currency.setEnabled(false);
			crcd_currency.setBackgroundDrawable(TransferManagerActivity1.this
					.getResources().getDrawable(R.drawable.bg_spinner_default));
			crcd_cashRemit.setEnabled(false);
			crcd_cashRemit.setBackgroundDrawable(TransferManagerActivity1.this
					.getResources().getDrawable(R.drawable.bg_spinner_default));
		}

		if ((!StringUtil.isNull(haveNotRepayAmout) && Double
				.parseDouble(haveNotRepayAmout) > 0)
		// 外币有欠款
				&& (!StringUtil.isNullOrEmpty(currInDetail))) {
			// 有购汇信息
			// currentBalance

			amount = (String) currInDetail.get(Crcd.CRCD_FOR_PAYEDAMT);
			gouhuiBalance = amount;
			if ("001".equals(inCrcdCurrency1)) {
				// 第一币种为人民币
				crcd_card_currency.setText(LocalData.Currency
						.get(inCrcdCurrency2));
				ghcurrency = inCrcdCurrency2;
			} else {
				crcd_card_currency.setText(LocalData.Currency
						.get(inCrcdCurrency1));
				ghcurrency = inCrcdCurrency1;
			}
			// crcd_card_currency.setText(LocalData.Currency.get(inCrcdCurrency2));
			radio_gouhuiall.setChecked(true);
			repayAmountSet = ConstantGloble.REPAY_ALL;
			et_gouhui_count.setText(StringUtil.parseStringCodePattern(
					ghcurrency, gouhuiBalance, 2));

			et_gouhui_count.setEnabled(false);

		}

		// 下一步 还款 购汇
		btn_crcd_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断有没有选择选出账户
				if (isTranOutFirst) {
					CustomDialog.toastInCenter(context,
							TransferManagerActivity1.this
									.getString(R.string.choose_outno_message));
					mAccOutLl.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_card_new_red));
//					dept_out_info_layout.setVisibility(View.VISIBLE);
					return;
				}

				if (radio_huankuan.isChecked()) {
					// 信用卡还款

					// if (ConstantGloble.ZHONGYIN.equals(accTypeout)) {
					// BaseDroidApp.getInstanse().showInfoMessageDialog(
					// getString(R.string.zhongyin_out_error));
					// return;
					// }
					amount = et_huankuan_count.getText().toString().trim();
					if (amount.contains(",")) {
						amount = amount.replace(",", "");
					}

					if (StringUtil.isNullOrEmpty(incurrencyList)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getString(R.string.no_same_currency));
						return;
					}

					boolean flag = judgeUserData(amount, true);
					if (!flag) {
						return;
					}

					// 自定义金额 还款 判断 还款金额 是否大于全额还款金额
					// if(radio_paylimit.isChecked()){
					// if(ConstantGloble.PRMS_CODE_RMB.equals(currencyCode)){
					// //人民币还款
					// if (Double.parseDouble(amount) > Double
					// .parseDouble(haveNotRepayAmout_cn)) {
					// BaseDroidApp.getInstanse().showInfoMessageDialog(
					// getString(R.string.huankuan_error));
					// return;
					// }
					// }else {
					// //外币还款
					// if (Double.parseDouble(amount) > Double
					// .parseDouble(haveNotRepayAmout)) {
					// BaseDroidApp.getInstanse().showInfoMessageDialog(
					// getString(R.string.huankuan_error));
					// return;
					// }
					// }
					//
					// }
					Map<String, String> userInputMap = new HashMap<String, String>();
					userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
					userInputMap.put(BocInvt.BOCINVT_CANCEL_CURRENCY_REQ, currencyCode);
					userInputMap.put(Tran.ACCOUNTDETAIL_CASHREMIT_RES, cashRemitCode);
//					intent.putExtra(BocInvt.BOCINVT_CANCEL_CURRENCY_REQ, currencyCode);
//					intent.putExtra(Tran.ACCOUNTDETAIL_CASHREMIT_RES, cashRemitCode);
					TranDataCenter.getInstance().setUserInputMap(userInputMap);
					requestPsnTransGetBocTransferCommissionCharge(ConstantGloble.PB021);

				} else if (radio_gouhui.isChecked()) {
					// 购汇还款

					// 购汇不判断是否有币种交集
					// if(StringUtil.isNullOrEmpty(incurrencyList)){
					// BaseDroidApp.getInstanse().showInfoMessageDialog(
					// getString(R.string.no_same_currency));
					// return;
					// }

					boolean cuttencyflag = getoutcurrey();// 转出是否有人民币
					if (!cuttencyflag) {
						// 转出无人民币
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getString(R.string.gouhui_no_cn));
						return;
					}

					if (radio_gouhuipart.isChecked()) {
						amount = et_gouhui_count.getText().toString().trim();
						boolean flag = judgeUserData(amount, false);
						if (!flag) {
							return;
						}
						if (gouhuiBalance.contains(",")) {
							gouhuiBalance = gouhuiBalance.replace(",", "");
						}
						// 判断 购汇金额 是否大于全额购汇金额
						if (Double.parseDouble(amount) > Double
								.parseDouble(gouhuiBalance)) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									getString(R.string.gouhui_error));
							return;
						}

					} else {
						amount = et_gouhui_count.getText().toString().trim();
						if (amount.contains(",")) {
							amount = amount.replace(",", "");
						}
						boolean flag = judgeUserData(amount, false);
						if (!flag) {
							return;
						}
					}
					
					Intent intent1 = getIntent();
					if (intent1 != null) {
						otherPartJumpFlag = intent1.getIntExtra(
								ConstantGloble.JUMP_TO_TRAN_FLAG, 0);
					}
					if (StringUtil.isNullOrEmpty(otherPartJumpFlag)) {
						return;
					}
					if (otherPartJumpFlag == 0) {
						// 其他模块
						TranDataCenter.getInstance().setAccInInfoMap(
								addInforMap);
					} else {

					}
					Map<String, String> userInputMap = new HashMap<String, String>();
					userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
					userInputMap.put(Tran.INPUT_CURRENCY_CODE, ghcurrency);
					userInputMap.put(
							Tran.CREDITCARD_FOREIGN_CRCDAUTOREPAYMODE_REQ,
							repayAmountSet);
					TranDataCenter.getInstance().setUserInputMap(userInputMap);

					requestPsnCrcdForeignPayOffFee();

				}

			}

		});

		// 行内
		/** 关联账户 立即执行 */
		Button nowExeBtn = (Button) v
				.findViewById(R.id.btn_commBoc_nowExe_tranSeting);
		nowExeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 判断有没有选择选出账户
				if (isTranOutFirst) {
					CustomDialog.toastInCenter(context,
							TransferManagerActivity1.this
									.getString(R.string.choose_outno_message));
					mAccOutLl.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_card_new_red));
//					dept_out_info_layout.setVisibility(View.VISIBLE);
					return;
				}

				// if (ConstantGloble.ZHONGYIN.equals(accTypeout)) {
				// BaseDroidApp.getInstanse().showInfoMessageDialog(
				// getString(R.string.zhongyin_out_error));
				// return;
				// }
				// if (!checkRMBMessage()) {
				// return;
				// }
				amount = et_huankuan_count.getText().toString().trim();
				memo = et_huankuan_fuyan.getText().toString().trim();

				if (StringUtil.isNullOrEmpty(incurrencyList)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getString(R.string.no_same_currency));
					return;
				}
				// payeeMobile = payeeMobileEt.getText().toString().trim();
				// 转账金额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成
				tranMode = ConstantGloble.NOWEXE;
				boolean flag = judgeUserData(amount, true);
				if (!flag) {
					return;
				}
				// 保存用户输入数据
				saveUserData();
				// 手续费试算接口
				requestForTransferCommissionCharge(ConstantGloble.PB021);

			}
		});
		/** 常用行内 预约日期执行 */
		Button preDateExeBtn = (Button) v
				.findViewById(R.id.btn_commBoc_preDateExe_tranSeting);
		preDateExeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 判断有没有选择选出账户
				if (isTranOutFirst) {
					CustomDialog.toastInCenter(context,
							TransferManagerActivity1.this
									.getString(R.string.choose_outno_message));
					mAccOutLl.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_card_new_red));
//					dept_out_info_layout.setVisibility(View.VISIBLE);
					return;
				}
				// if (ConstantGloble.ZHONGYIN.equals(accTypeout)) {
				// BaseDroidApp.getInstanse().showInfoMessageDialog(
				// getString(R.string.zhongyin_out_error));
				// return;
				// }
				// if (!checkRMBMessage()) {
				// return;
				// }
				amount = et_huankuan_count.getText().toString().trim();
				memo = et_huankuan_fuyan.getText().toString().trim();
				// payeeMobile = payeeMobileEt.getText().toString().trim();
				// 转账金额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成
				if (StringUtil.isNullOrEmpty(incurrencyList)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getString(R.string.no_same_currency));
					return;
				}
				tranMode = ConstantGloble.PREDATEEXE;
				boolean flag = judgeUserData(amount, false);
				if (!flag) {
					return;
				}
				// 保存用户输入数据
				saveUserData();
				// 先调预交易接口 再掉手续费接口
				BaseHttpEngine.showProgressDialog();
				requestSystemDateTime();
				// 手续费试算接口
				// requestForTransferCommissionCharge(ConstantGloble.PB031);
			}
		});
		/** 常用行内 预约周期执行 */
		Button prePeriodExeBtn = (Button) v
				.findViewById(R.id.btn_commBoc_prePeriodExe_tranSeting);
		prePeriodExeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 判断有没有选择选出账户
				if (isTranOutFirst) {
					CustomDialog.toastInCenter(context,
							TransferManagerActivity1.this
									.getString(R.string.choose_outno_message));
					mAccOutLl.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_card_new_red));
//					dept_out_info_layout.setVisibility(View.VISIBLE);
					return;
				}
				// if (ConstantGloble.ZHONGYIN.equals(accTypeout)) {
				// BaseDroidApp.getInstanse().showInfoMessageDialog(
				// getString(R.string.zhongyin_out_error));
				// return;
				// }
				// if (!checkRMBMessage()) {
				// return;
				// }

				amount = et_huankuan_count.getText().toString().trim();
				memo = et_huankuan_fuyan.getText().toString().trim();
				if (StringUtil.isNullOrEmpty(incurrencyList)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getString(R.string.no_same_currency));
					return;
				}
				// payeeMobile = payeeMobileEt.getText().toString().trim();
				// 转账金额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成
				tranMode = ConstantGloble.PREPERIODEXE;
				boolean flag = judgeUserData(amount, false);
				if (!flag) {
					return;
				}
				// 保存用户输入数据
				saveUserData();
				// 手续费试算接口
				BaseHttpEngine.showProgressDialog();
				requestSystemDateTime();

			}
		});

	}

	private void Refreshcrcdbottom() {
		// 选择转出账户后刷新底部试图
		// 刷新 币种 钞汇。
		// 设置币种
		newflag = false;
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		accTypeout = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
		String outAccNo = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		String inAccNo = (String) accInInfoMap.get(Crcd.CRCD_ACCTNUM);
		String outId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		String inId = (String) accInInfoMap.get(Comm.ACCOUNT_ID);
		if (outAccNo.equals(inAccNo) && outId.equals(inId)) {
			// 转入转出为通一张卡 之前已经判断 只有在 外币有欠款 人民币无欠款 才可以是同一张卡 只可以购汇
			// 有购汇信息
			ll_paytype.setVisibility(View.GONE);
			ll_huankuan.setVisibility(View.GONE);
			ll_gouhui.setVisibility(View.VISIBLE);
			radio_gouhui.setChecked(true);
			crcd_trans_set_title.setText(getResources().getString(
					R.string.crcd_credit_card_reimbursement));
			if ("001".equals(inCrcdCurrency1)) {
				// 第一币种为人民币
				crcd_card_currency.setText(LocalData.Currency
						.get(inCrcdCurrency2));
			} else {
				crcd_card_currency.setText(LocalData.Currency
						.get(inCrcdCurrency1));
			}
			return;
		}
		position = crcd_currency.getSelectedItemPosition();
		incurrencyList.clear();
		incurrencyflagList.clear();

		if (ConstantGloble.ACC_TYPE_BRO.equals(accTypeout)) {
			// 转出借记卡
			if (isOpenFlag) {
				incurrencyList.add("001");
				incurrencyflagList.add(LocalData.Currency.get("001"));
			} else {
				for (int i = 0; i < inCreditCurrencyAndBalance.size(); i++) {

					for (int j = 0; j < outCurrencyAndBalance.size(); j++) {
						// 如果是交集 那么显示
						if (inCreditCurrencyAndBalance
								.get(i)
								.get("currency")
								.equals(outCurrencyAndBalance.get(j).get(
										"currency"))) {
							String currency = inCreditCurrencyAndBalance.get(i)
									.get("currency");
							if (!incurrencyList.contains(currency)) {
								incurrencyList.add(currency);
								incurrencyflagList.add(LocalData.Currency
										.get(currency));
							}

						}
					}
				}
			}
		} else if (ConstantGloble.ACC_TYPE_GRE.equals(accTypeout)
				|| ConstantGloble.ZHONGYIN.equals(accTypeout)
		 || ConstantGloble.SINGLEWAIBI.equals(accTypeout) // 601 转出增加 107
		) {

			if (isOpenFlag) {
				for (int i = 0; i < outCreditCurrencyAndBalance.size(); i++) {
					String currency = inCreditCurrencyAndBalance.get(i).get(
							"currency");
					if ("001".equals(currency)) {
						if (!incurrencyList.contains(currency)) {
							incurrencyList.add(currency);
							incurrencyflagList.add(LocalData.Currency
									.get(currency));
						}
					}

				}

			} else {
				for (int i = 0; i < inCreditCurrencyAndBalance.size(); i++) {

					for (int j = 0; j < outCreditCurrencyAndBalance.size(); j++) {
						// 如果是交集 那么显示
						if (inCreditCurrencyAndBalance
								.get(i)
								.get("currency")
								.equals(outCreditCurrencyAndBalance.get(j).get(
										"currency"))) {
							String currency = inCreditCurrencyAndBalance.get(i)
									.get("currency");
							if (!incurrencyList.contains(currency)) {
								incurrencyList.add(currency);
								incurrencyflagList.add(LocalData.Currency
										.get(currency));
							}

						}
					}
				}
			}

		}
		if (StringUtil.isNullOrEmpty(incurrencyList)) {
			// 无交集
			incurrencyflagList.add("-");
			boolean cuttencyflag = getoutcurrey();// 转出是否有人民币
			if (cuttencyflag
			// 转出有人民币
					&& (!StringUtil.isNull(haveNotRepayAmout) && Double
							.parseDouble(haveNotRepayAmout) > 0)
					// 外币有欠款
					&& (!StringUtil.isNullOrEmpty(currInDetail)))
			// 有购汇信息
			{
				// 无提示信息

			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.no_same_currency));
			}

		}
		// }

		if (incurrencyList.size() == 1) {
			// 交集1个币种
			String currency = incurrencyList.get(0);
			if (ConstantGloble.PRMS_CODE_RMB.equals(currency)) {
				// 交集为人民币 判断人民币币是否有欠款

				if (!StringUtil.isNull(haveNotRepayAmout_cn)
						&& Double.parseDouble(haveNotRepayAmout_cn) > 0) {
					// 人民币有欠款
					Refreshcrcdbottomview(position);
				} else {
					if (!StringUtil.isNull(haveNotRepayAmout)
							&& Double.parseDouble(haveNotRepayAmout) > 0) {
						currencyCode = incurrencyList.get(0);
						RefreshcrcdbottomviewNew();

					} else {
						// 外币无欠款
						Refreshcrcdbottomview(position);
					}
				}

			} else {
				// 交集为外币 判断外币是否有欠款
				// if (!StringUtil.isNull(haveNotRepayAmout)
				// && Double.parseDouble(haveNotRepayAmout) > 0) {
				Refreshcrcdbottomview(position);
				// }else {
				// if (!StringUtil.isNull(haveNotRepayAmout_cn)
				// && Double.parseDouble(haveNotRepayAmout_cn) > 0) {
				// currencyCode = incurrencyList.get(0);
				// RefreshcrcdbottomviewNew();
				// } else {
				// // 人民币币无欠款
				// Refreshcrcdbottomview(position);
				// }
				// }

			}

		} else {
			Refreshcrcdbottomview(position);
		}

	}

	private void RefreshcrcdbottomviewNew() {
		newflag = true;
		incurrencyAdapter = new ArrayAdapter<String>(
				TransferManagerActivity1.this, R.layout.custom_spinner_item,
				incurrencyflagList);
		incurrencyAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		crcd_currency.setAdapter(incurrencyAdapter);
		crcd_currency.setBackgroundDrawable(TransferManagerActivity1.this
				.getResources().getDrawable(R.drawable.bg_spinner));
		RefreshcrcdbottomcashRemit();
		ll_paytype.setVisibility(View.VISIBLE);
		credit_new_info.setVisibility(View.VISIBLE);
		ll_input.setVisibility(View.GONE);
		ll_paymode.setVisibility(View.GONE);
		if (radio_gouhui.isChecked()) {
			ll_next.setVisibility(View.VISIBLE);
		} else {
			ll_next.setVisibility(View.GONE);
		}

		ll_more_next.setVisibility(View.GONE);
		crcd_trans_set_title.setText(getResources().getString(
				R.string.mycrcd_zhangdan_zhuan));
		crcd_currency.setOnItemSelectedListener(null);
	}

	private void Refreshcrcdbottomview(int position) {
		ll_input.setVisibility(View.VISIBLE);
		credit_new_info.setVisibility(View.GONE);
		incurrencyAdapter = new ArrayAdapter<String>(
				TransferManagerActivity1.this, R.layout.custom_spinner_item,
				incurrencyflagList);
		incurrencyAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		crcd_currency.setAdapter(incurrencyAdapter);
		crcd_currency.setBackgroundDrawable(TransferManagerActivity1.this
				.getResources().getDrawable(R.drawable.bg_spinner));
		if (StringUtil.isNullOrEmpty(incurrencyList)) {
			// 无交集 转出人民币 转入外币购汇 怎么办

			boolean cuttencyflag = getoutcurrey();// 转出是否有人民币
			crcd_currency.setEnabled(false);
			// 没有交集 sunh
			if (cuttencyflag
			// 转出有人民币
					&& (!StringUtil.isNull(haveNotRepayAmout) && Double
							.parseDouble(haveNotRepayAmout) > 0)
					// 外币有欠款
					&& (!StringUtil.isNullOrEmpty(currInDetail))) {
				// 有购汇信息
				ll_huankuan.setVisibility(View.GONE);
				ll_gouhui.setVisibility(View.VISIBLE);
				radio_gouhui.setChecked(true);
				if ("001".equals(inCrcdCurrency1)) {
					// 第一币种为人民币
					crcd_card_currency.setText(LocalData.Currency
							.get(inCrcdCurrency2));
				} else {
					crcd_card_currency.setText(LocalData.Currency
							.get(inCrcdCurrency1));
				}
				return;
			}

		} else {
			crcd_currency.setEnabled(true);
			if (incurrencyList.size() > position) {
				crcd_currency.setSelection(position);
			} else {
				crcd_currency.setSelection(0);
			}
		}

		crcd_currency.setOnItemSelectedListener(crcd_currencyListener);

	}

	private boolean getoutcurrey() {

		if (ConstantGloble.ACC_TYPE_BRO.equals(accTypeout)) {
			// 借记卡
			for (int i = 0; i < outCurrencyAndBalance.size(); i++) {
				if (ConstantGloble.PRMS_CODE_RMB.equals(outCurrencyAndBalance
						.get(i).get(Crcd.CRCD_CURRENCY_RES))) {
					return true;
				}
			}
		} else if(ConstantGloble.ZHONGYIN.equals(accTypeout)
				||ConstantGloble.GREATWALL.equals(accTypeout)
				||ConstantGloble.SINGLEWAIBI.equals(accTypeout)){
			// 信用卡
			for (int i = 0; i < outCreditCurrencyAndBalance.size(); i++) {
				if (ConstantGloble.PRMS_CODE_RMB
						.equals(outCreditCurrencyAndBalance.get(i).get(
								Crcd.CRCD_CURRENCY_RES))) {
					return true;
				}
			}
		}
		return false;
	}

	protected void RefreshcrcdbottomcashRemit() {
		if (ConstantGloble.PRMS_CODE_RMB.equals(currencyCode)) {
			incashRemitList.clear();
			incashRemitflagList.clear();
			incashRemitList.add(ConstantGloble.CASHREMIT_00);
			incashRemitflagList.add(LocalData.CurrencyCashremit
					.get(ConstantGloble.CASHREMIT_00));
			incashRemitAdapter = new ArrayAdapter<String>(
					TransferManagerActivity1.this,
					R.layout.custom_spinner_item, incashRemitflagList);
			crcd_cashRemit.setAdapter(incashRemitAdapter);
			if (ConstantGloble.ACC_TYPE_BRO.equals(accTypeout)) {

				ll_cashRemit.setVisibility(View.VISIBLE);
			} else {
				ll_cashRemit.setVisibility(View.GONE);
			}
			crcd_cashRemit.setEnabled(false);
			crcd_cashRemit.setBackgroundDrawable(TransferManagerActivity1.this
					.getResources().getDrawable(R.drawable.bg_spinner_default));
		} else {
			if (ConstantGloble.ACC_TYPE_BRO.equals(accTypeout)) {

				if (!StringUtil.isNullOrEmpty(incurrencyList)) {
					// 转出为借记卡 有钞汇

					crcdcashRemitAndBalance = new ArrayList<Map<String, String>>();
					incashRemitList.clear();
					incashRemitflagList.clear();

					crcd_cashRemit
							.setBackgroundDrawable(TransferManagerActivity1.this
									.getResources().getDrawable(
											R.drawable.bg_spinner));

					for (int i = 0; i < outCurrencyAndBalance.size(); i++) {
						Map<String, String> map = new HashMap<String, String>();

						if (outCurrencyAndBalance.get(i)
								.get(Crcd.CRCD_CURRENCY_RES)
								.equals(currencyCode)) {
							String flag = outCurrencyAndBalance.get(i).get(
									"cashRemit");
							String availableBalance = outCurrencyAndBalance
									.get(i).get("Balance");
							incashRemitList.add(flag);
							incashRemitflagList.add(LocalData.CurrencyCashremit
									.get(flag));
							map.put(flag, availableBalance);
							crcdcashRemitAndBalance.add(map);
						}
					}
					ll_cashRemit.setVisibility(View.VISIBLE);
					incashRemitAdapter = new ArrayAdapter<String>(
							TransferManagerActivity1.this,
							R.layout.custom_spinner_item, incashRemitflagList);
					incashRemitAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					crcd_cashRemit.setAdapter(incashRemitAdapter);
					crcd_cashRemit
							.setOnItemSelectedListener(crcd_cashRemitListener);
					crcd_cashRemit.setEnabled(true);
				} else {
					incashRemitflagList.add("-");
					cashRemitCode = null;
					ll_cashRemit.setVisibility(View.VISIBLE);
					incashRemitAdapter = new ArrayAdapter<String>(
							TransferManagerActivity1.this,
							R.layout.custom_spinner_item, incashRemitflagList);
					crcd_cashRemit.setEnabled(false);
					crcd_cashRemit
							.setBackgroundDrawable(TransferManagerActivity1.this
									.getResources().getDrawable(
											R.drawable.bg_spinner_default));
				}

			} else {

				ll_cashRemit.setVisibility(View.GONE);
				crcd_cashRemit.setEnabled(false);
				crcd_cashRemit
						.setBackgroundDrawable(TransferManagerActivity1.this
								.getResources().getDrawable(
										R.drawable.bg_spinner_default));
			}

		}

	}

	OnCheckedChangeListener radiogrouplistener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radio_huankuan:// 还款
				tranTypeFlag = TRANTYPE_REL_CRCD_REPAY;
				ll_huankuan.setVisibility(View.VISIBLE);
				ll_gouhui.setVisibility(View.GONE);
				if (newflag) {
					ll_next.setVisibility(View.GONE);
				} else {
					crcd_trans_set_title.setText(getResources().getString(
							R.string.mycrcd_zhangdan_zhuan));
				}

				break;
			case R.id.radio_gouhui:// 购汇
				tranTypeFlag = TRANTYPE_REL_CRCD_BUY;
				ll_huankuan.setVisibility(View.GONE);
				ll_gouhui.setVisibility(View.VISIBLE);
				ll_next.setVisibility(View.VISIBLE);
				ll_more_next.setVisibility(View.GONE);
				crcd_trans_set_title.setText(getResources().getString(
						R.string.crcd_credit_card_reimbursement));
				break;

			case R.id.radio_payall:// 全额
				et_huankuan_count.setEnabled(false);
				if (StringUtil.isNull(currencyCode)) {
					et_huankuan_count.setText("-");
				} else {
					if (ConstantGloble.PRMS_CODE_RMB.equals(currencyCode)) {
						et_huankuan_count.setText(StringUtil
								.parseStringPattern(haveNotRepayAmout_cn, 2));
					} else {
						et_huankuan_count.setText(StringUtil
								.parseStringCodePattern(currencyCode,
										haveNotRepayAmout, 2));
					}
				}

				break;
			case R.id.radio_paylimit:// 最小

				et_huankuan_count.setEnabled(false);
				if (StringUtil.isNull(currencyCode)) {
					et_huankuan_count.setText("-");
				} else {
					if (ConstantGloble.PRMS_CODE_RMB.equals(currencyCode)) {
						et_huankuan_count.setText(StringUtil
								.parseStringPattern(billLimitAmout_cn, 2));
					} else {
						et_huankuan_count.setText(StringUtil
								.parseStringCodePattern(currencyCode,
										billLimitAmout, 2));

					}
				}
				break;
			case R.id.radio_payself:// 自定义

				et_huankuan_count.setEnabled(true);
				et_huankuan_count.setText("");
				break;

			case R.id.radio_gouhuiall:// 全额
				repayAmountSet = ConstantGloble.REPAY_ALL;
				et_gouhui_count.setText(StringUtil.parseStringCodePattern(
						ghcurrency, gouhuiBalance, 2));
				et_gouhui_count.setEnabled(false);
				break;
			case R.id.radio_gouhuipart:// 部分
				repayAmountSet = ConstantGloble.REPAY_PART;
				et_gouhui_count.setText(null);
				et_gouhui_count.setEnabled(true);
				break;
			}

		}
	};

	private OnItemSelectedListener crcd_currencyListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {

			if (!StringUtil.isNullOrEmpty(incurrencyList)) {
				currencyCode = incurrencyList.get(position);
			} else {
				currencyCode = null;

			}
			if (ConstantGloble.PRMS_CODE_RMB.equals(currencyCode)) {
				if (!StringUtil.isNull(haveNotRepayAmout_cn)
						&& Double.parseDouble(haveNotRepayAmout_cn) > 0) {
					// 还款
					et_huankuan_count.setText(StringUtil.parseStringPattern(
							haveNotRepayAmout_cn, 2));
					if (!StringUtil.isNull(haveNotRepayAmout)
							&& Double.parseDouble(haveNotRepayAmout) > 0
							) {
							// 外币有欠款
							ll_paytype.setVisibility(View.VISIBLE);
						

					}
					radio_payall.performClick();
					et_huankuan_count.setEnabled(false);
					ll_paymode.setVisibility(View.VISIBLE);
					ll_next.setVisibility(View.VISIBLE);
					ll_more_next.setVisibility(View.GONE);
					tv_huankuan_count.setText(getResources().getString(
							R.string.repayAmountValue));
					crcd_trans_set_title.setText(getResources().getString(
							R.string.mycrcd_zhangdan_zhuan));

				} else {
					tranTypeFlag = TRANTYPE_REL_ACCOUNT;
					// 行内
					ll_paytype.setVisibility(View.GONE);
					ll_paymode.setVisibility(View.GONE);
					et_huankuan_count.setText("");
					et_huankuan_count.setEnabled(true);
					ll_next.setVisibility(View.GONE);
					ll_more_next.setVisibility(View.VISIBLE);
					tv_huankuan_count.setText(getResources().getString(
							R.string.transferAmount));
					crcd_trans_set_title.setText(getResources().getString(
							R.string.comm_boc_trans));
				}

			} else {
				if (!StringUtil.isNull(haveNotRepayAmout)
						&& Double.parseDouble(haveNotRepayAmout) > 0) {
					// 还款
					radio_payall.performClick();
					et_huankuan_count.setEnabled(false);
					et_huankuan_count.setText(StringUtil
							.parseStringCodePattern(currencyCode,
									haveNotRepayAmout, 2));
					ll_paytype.setVisibility(View.VISIBLE);
					ll_paymode.setVisibility(View.VISIBLE);
					ll_next.setVisibility(View.VISIBLE);
					ll_more_next.setVisibility(View.GONE);
					tv_huankuan_count.setText(getResources().getString(
							R.string.repayAmountValue));
					crcd_trans_set_title.setText(getResources().getString(
							R.string.mycrcd_zhangdan_zhuan));
				} else {
					tranTypeFlag = TRANTYPE_REL_ACCOUNT;
					// 行内
					et_huankuan_count.setText("");
					et_huankuan_count.setEnabled(true);
					ll_paytype.setVisibility(View.GONE);
					ll_paymode.setVisibility(View.GONE);
					ll_next.setVisibility(View.GONE);
					ll_more_next.setVisibility(View.VISIBLE);
					tv_huankuan_count.setText(getResources().getString(
							R.string.transferAmount));
					crcd_trans_set_title.setText(getResources().getString(
							R.string.comm_boc_trans));
				}

			}

			RefreshcrcdbottomcashRemit();

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			if (!StringUtil.isNullOrEmpty(incurrencyList)) {
				currencyCode = incurrencyList.get(0);
			} else {
				currencyCode = null;

			}
		}

	};
	private OnItemSelectedListener crcd_cashRemitListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			cashRemitCode = incashRemitList.get(position);

			// 得到对应余额
			Map<String, String> map = (Map<String, String>) crcdcashRemitAndBalance
					.get(position);
			availableBalance = map.get(cashRemitCode);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			cashRemitCode = cashRemitList.get(0);
		}
	};

	/** 信用卡购汇还款手续费试算 */
	private void requestPsnCrcdForeignPayOffFee() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDFOREIGNOFFFEE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_CRCDID, (String) addInforMap.get(Comm.ACCOUNT_ID));
		map.put(Crcd.CRCD_CRCDACCTNAME,
				(String) addInforMap.get(Crcd.CRCD_ACCTNAME_RES));
		map.put(Crcd.CRCD_CRCDACCTNO,
				(String) addInforMap.get(Crcd.CRCD_ACCTNUM));
		map.put(Crcd.CRCD_CRCDRMBACCID,
				(String) accOutInfoMap.get(Comm.ACCOUNT_ID));
		map.put(Crcd.CRCD_CRCDAUTOREPAYMODE, repayAmountSet);
		map.put(Crcd.CRCD_CRCDAMOUNT, amount);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCrcdForeignPayOffFeeCallBack");

	}

	public void requestPsnCrcdForeignPayOffFeeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setCommissionChargeMap(result);
		Intent intent = new Intent(TransferManagerActivity1.this,
				RelCreditCardRemitConfirmActivity1.class);
		startActivity(intent);

	}

	private void requestPsnTransGetBocTransferCommissionCharge(String serviceId) {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		String fromAccountId = (String) accOutInfoMap.get(Tran.ACCOUNTID_RES);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();

		String toAccountId = (String) accInInfoMap.get(Crcd.CRCD_ACCOUNTID_RES);

		String payeeactNo = (String) accInInfoMap.get(Crcd.CRCD_ACCTNUM);

		String payeeName = (String) accInInfoMap.get(Crcd.CRCD_ACCTNAME_RES);

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
				"requestPsnTransGetBocTransferCommissionChargeCallBack");
	}

	public void requestPsnTransGetBocTransferCommissionChargeCallBack(
			Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setCommissionChargeMap(result);
		Intent intent = new Intent();
		intent.putExtra("crcdflag", true);
		intent.putExtra(BocInvt.BOCINVT_CANCEL_CURRENCY_REQ, currencyCode);
		intent.putExtra(Tran.ACCOUNTDETAIL_CASHREMIT_RES, cashRemitCode);
		intent.setClass(TransferManagerActivity1.this,
				RelSelfCreditCardConfirmActivity1.class);
		startActivity(intent);

	}
}
