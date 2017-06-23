package com.chinamworld.bocmbci.biz.thridmanage.cecuritytrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cfca.mobile.sip.CFCASipDelegator;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.AccoutType;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.CurrencyType;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.TransferWayType;
import com.chinamworld.bocmbci.biz.thridmanage.ServiceType;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdDataCenter;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdManagerBaseActivity;
import com.chinamworld.bocmbci.biz.thridmanage.cecuritytrade.adapter.BankAdapter;
import com.chinamworld.bocmbci.biz.thridmanage.cecuritytrade.adapter.CecurityAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 证券第三方存管
 * 
 * @author panwe
 */
@SuppressWarnings("ResourceType")
public class CecurityTradeActivity extends ThirdManagerBaseActivity implements OnClickListener {

	private static final String TAG = CecurityTradeActivity.class.getSimpleName();
	private static final int REQUEST_CONFIRM_CODE = 1002;
	/** 主布局 **/
	private View viewContent;
	// 资金账户
	/** 资金账户 详情 */
	private ViewGroup liMoneyContentLayout;
	// 保证金账户
	/** 保证金账户 详情 */
	private ViewGroup liCecContentLayout;
	/** 查询余额按钮 */
	private Button btnQueryAmoutView;
	/** 保证金余额 */
	private TextView tvAmoutView;
	/** 保证金查询密码控件 */
	private SipBox sipBox;
	/** 资金账卡片底部提示 */
	private TextView mBankSeleView;
	/** 保证金卡片底部提示 */
	private TextView mCecSeleView;
	// 提示信息
	/** 提示信息布局 */
	private View mTipView;
	/** 底部提示信息标题 */
	private TextView mTipTitleView;
	// 转帐设定
	/** 转账操作按钮域 */
	private LinearLayout layoutTradebtnLayout;
	/** 银行资金转证券资金按钮 */
	private Button btnBankToCecurityView;
	/** 证券资金转银行资金 */
	private Button btnCecurityToBankView;
	/** 转账信息填写 */
	private LinearLayout layoutMsgFillLayout;
	/** 保证金币种 */
	private TextView mCecBiTypeView;
	/** 转账金额 */
	private EditText edMoneyView;
	/** 密码控件 */
	private LinearLayout layoutSip;
	/** 证转银密码控件 */
	private SipBox tradeSipBxo;

	/** 资金选中条目 */
	private int bankselectpostion;
	/** 保证金选中条目 */
	private int cecselectpostion;
	/** 资金选中最后选择的账户 */
	private String lastAccountId;
	/** 转账方式 */
	private String mTradeTypeParam;

	private int sipTag;
	/** 余额查询密码控件数据 */
	private String password, raNum;
	private ThirdDataCenter mThirdDataCenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加布局
		viewContent = LayoutInflater.from(this).inflate(R.layout.third_cecuritytrade, null);
		addView(viewContent);
		setTitle(getString(R.string.third_cecuritytrade));
		mThirdDataCenter = ThirdDataCenter.getInstance();
		findView();
		setLeftSelectedPosition("thirdMananger_1");
		checkServiceState(ServiceType.InvestmentService);
	}
 
	private void findView() {
		// 右上角按钮赋值
		setRightBtnClick(rightBtnClick);
		setTitleRightText(getString(R.string.third_right_btn_newtrade));
		btnRight.setVisibility(View.GONE);
		// 资金账户
		viewContent.findViewById(R.id.rootlayout_content_bank).setOnClickListener(this);
		// 保证金账户
		viewContent.findViewById(R.id.rootlayout_content_cec).setOnClickListener(this);
		
		liMoneyContentLayout = (ViewGroup) viewContent.findViewById(R.id.layout_content_bank);
		liCecContentLayout = (ViewGroup) viewContent.findViewById(R.id.layout_content_cec);
		mTipTitleView = (TextView) viewContent.findViewById(R.id.dept_save_title_tv);
		layoutTradebtnLayout = (LinearLayout) viewContent.findViewById(R.id.layout_tradebtn);
		btnBankToCecurityView = (Button) viewContent.findViewById(R.id.btn_banktocecurity);
		btnCecurityToBankView = (Button) viewContent.findViewById(R.id.btn_eceuritytobank);
		mBankSeleView = (TextView) findViewById(R.id.tv_bank_sele);
		mCecSeleView = (TextView) findViewById(R.id.tv_cec_sele);
		 

		layoutMsgFillLayout = (LinearLayout) viewContent.findViewById(R.id.layout_content);
		tvAmoutView = (TextView) viewContent.findViewById(R.id.tv_bank_amout_ac);
		mTipView = viewContent.findViewById(R.id.ll_tip);

		btnBankToCecurityView.setOnClickListener(this);
		btnCecurityToBankView.setOnClickListener(this);

		mCecBiTypeView = (TextView) viewContent.findViewById(R.id.tv_bitype);
		mCecBiTypeView.setText(getString(R.string.tran_currency_rmb));
		edMoneyView = (EditText) viewContent.findViewById(R.id.et_cecurity_money);
		layoutSip = (LinearLayout) viewContent.findViewById(R.id.layout_sip);
		tradeSipBxo = (SipBox) viewContent.findViewById(R.id.et_cecurity_ps);
		tradeSipBxo.setCipherType(SystemConfig.CIPHERTYPE);
		tradeSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
		tradeSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
		tradeSipBxo.setId(10002);
		tradeSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
		tradeSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
		tradeSipBxo.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
		// new MyCFCASipDelegator(findViewById(R.id.rltotal))
		tradeSipBxo.setSipDelegator(new MyCFCASipDelegator(findViewById(R.id.rltotal)));
		// 转帐下一步
		viewContent.findViewById(R.id.btn_next).setOnClickListener(this);

		btnQueryAmoutView = (Button) viewContent.findViewById(R.id.btn_query);
		btnQueryAmoutView.setOnClickListener(this);
	}

	/** 右上角按钮点击事件 */
	private OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 恢复原始状态
			mThirdDataCenter.clear();
			// 恢复初始化
			initMoneyAccLayout();
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 资金账户
		case R.id.rootlayout_content_bank:
			// 获取卡列表
			List<String> paramslist = new ArrayList<String>();
			paramslist.add(AccoutType.CHANGCHENG_DIANZI_CARD_CODE);
			BaseHttpEngine.showProgressDialog();
			getAllBankList(paramslist/* ,false */);

			break;
		// 保证金账户
		case R.id.rootlayout_content_cec:
			if (liMoneyContentLayout.getVisibility() != View.VISIBLE) {
				CustomDialog.toastInCenter(this, getString(R.string.third_bankacc_tip));
				return;
			}
			requestCecurityForBankAcc((String) ThirdDataCenter.getInstance().getBankAccountList()
					.get(bankselectpostion).get(Comm.ACCOUNTNUMBER));

			break;
		// 银行资金转证券资金
		case R.id.btn_banktocecurity:
			// tag = 1;
			if (liMoneyContentLayout.getVisibility() != View.VISIBLE) {
				CustomDialog.toastInCenter(this, getString(R.string.third_bankacc_tip));
				return;
			}
			if (liCecContentLayout.getVisibility() != View.VISIBLE) {
				CustomDialog.toastInCenter(this, getString(R.string.third_cecacc_tip));
				return;
			}
			mTradeTypeParam = TransferWayType.BANK_TO_STOCK_CODE;
			showBanktoEceurityLayout();
			break;
		// 证券资金转银行资金
		case R.id.btn_eceuritytobank:
			// tag = 2;
			if (liMoneyContentLayout.getVisibility() != View.VISIBLE) {
				CustomDialog.toastInCenter(this, getString(R.string.third_bankacc_tip));
				return;
			}
			if (liCecContentLayout.getVisibility() != View.VISIBLE) {
				CustomDialog.toastInCenter(this, getString(R.string.third_cecacc_tip));
				return;
			}
			mTradeTypeParam = TransferWayType.STOCK_TO_BANK_CODE;
			sipTag = 2;
//			if (ishadConversation) {
//				requestRandomNumber();
//			} else {
			// 获取会话id
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
//			}

			break;
		// 余额查询
		case R.id.btn_query:
			// 弹出密码输入框
			if (StringUtil.isNullOrEmpty(password) || StringUtil.isNullOrEmpty(raNum)) {
				initMentionDialogView();
			} else {
				getCecurityAmout(password, raNum);
			}

			break;
		// 下一步
		case R.id.btn_next:
			String amount = edMoneyView.getText().toString();
			ArrayList<RegexpBean> regexpBeanLists = new ArrayList<RegexpBean>();
			RegexpBean transAmountReg = new RegexpBean(getString(R.string.third_trade_mny), amount, "tranAmount");
			regexpBeanLists.add(transAmountReg);
			if (!RegexpUtils.regexpDate(regexpBeanLists)) {
				return;
			}
			if (TransferWayType.BANK_TO_STOCK_CODE.equals(mTradeTypeParam)) {
				goToInfoConfirm("", "", true);
			} else if (TransferWayType.STOCK_TO_BANK_CODE.equals(mTradeTypeParam)) {
				try {
					// 验证
					RegexpBean reb1 = new RegexpBean(getString(R.string.third_pas_tip_next), tradeSipBxo.getText()
							.toString(), ConstantGloble.SIPOTPPSW);
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					lists.add(reb1);
					if (RegexpUtils.regexpDate(lists)) {
						String password = tradeSipBxo.getValue().getEncryptPassword();
						String raNum = tradeSipBxo.getValue().getEncryptRandomNum();
						BaseDroidApp.getInstanse().dismissErrorDialog();
						goToInfoConfirm(password, raNum, false);
					}
				} catch (Exception e) {
					BaseDroidApp.getInstanse().createDialog(null, e.getMessage());
					LogGloble.e(TAG, e.getMessage(), e);
				}
			}
			break;
		}
	}

	// 跳往信息确认页
	private void goToInfoConfirm(String pas, String pas_rc, boolean isRequestCommConversationId) {
		Map<String, Object> cecAccount = ThirdDataCenter.getInstance().getCecAccountList().get(cecselectpostion);
		Map<String, Object> bankAccount = ThirdDataCenter.getInstance().getBankAccountList().get(bankselectpostion);
		Intent it = new Intent(this, CecurityTransferConfirmActivity.class);
		Bundle b = new Bundle();
		b.putString("ACCID", (String) bankAccount.get(Comm.ACCOUNT_ID));
		b.putString("BANKACCNUM", (String) bankAccount.get(Comm.ACCOUNTNUMBER));
		b.putString("CECACCNUM", (String) cecAccount.get(Third.CECURITYTRADE_BANKACCNUM_RE));
		b.putString("TRADETYPE", mTradeTypeParam);
		b.putString("AMOUT", edMoneyView.getText().toString());
		b.putString("CNCY", "001");// 这是根据返回上送
		b.putString("financeCompany", (String) cecAccount.get(Third.CECURITYTRADE_COMPANY));
		b.putString("stockCode", (String) cecAccount.get(Third.CECURITYTRADE_STOCKNO));
		b.putString("capitalAcc", (String) cecAccount.get(Third.CECURITYTRADE_BANKACCNUM_RE));
		if (!StringUtil.isNullOrEmpty(pas) || !StringUtil.isNullOrEmpty(pas_rc)) {
			b.putString("PAS", pas);
			b.putString("PAS_RC", pas_rc);
		}
		it.putExtra("B", b);
		// 是否重新请求commid，如果银转证需要申请CommConversationId，如果证转银不需要申请CommConversationId
		it.putExtra("isRequestCommConversationId", isRequestCommConversationId);
		startActivityForResult(it, REQUEST_CONFIRM_CODE);
	}

	/** 初始化dialog密码框 */
	private void initMentionDialogView() {
		sipTag = 1;
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}

	/**
	 * 资金账户列表ListView的监听事件
	 */
	private OnItemClickListener moneyAccItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
			bankselectpostion = position;
			// 查询详情
			String accountId = (String) ThirdDataCenter.getInstance().getBankAccountList().get(position)
					.get(Comm.ACCOUNT_ID);
			if (lastAccountId != null && !lastAccountId.equals(accountId)) {
				initMoneyAccLayout();
			}
			lastAccountId = accountId;
			requestBankAccountDetail(accountId);
		}

	};

	/**
	 * 证券账户列表ListView的监听事件
	 */
	private OnItemClickListener eceurityAccItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
			cecselectpostion = position;

			initCecurAccLayout();
			// 获取详情
			requestCecurityDetail();
		}
	};

	// 密码输入框按钮
	public void dialogClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cance:
			BaseDroidApp.getInstanse().dismissErrorDialog();
			break;

		case R.id.btn_confirm:
			try {
				// 验证
				RegexpBean reb1 = new RegexpBean(getString(R.string.third_pas_tip_next), sipBox.getText().toString(),
						ConstantGloble.SIPOTPPSW);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				boolean regexpDate = RegexpUtils.regexpDate(lists, new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 查询
						btnQueryAmoutView.performClick();
					}
				});
				if (regexpDate) {
					password = sipBox.getValue().getEncryptPassword();
					raNum = sipBox.getValue().getEncryptRandomNum();
					BaseDroidApp.getInstanse().dismissErrorDialog();
					getCecurityAmout(password, raNum);
				}
			} catch (Exception e) {
				BaseDroidApp.getInstanse().createDialog(null, e.getMessage());
				LogGloble.e(TAG, e.getMessage(), e);
			}
			break;
		}
	}

	// conversationid回调
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {

		super.requestCommConversationIdCallBack(resultObj);
		// 请求随机数
		requestRandomNumber();
	}

	/** 获取账户列表返回 */
	@Override
	public void allBankAccListCallBack(Object resultObj) {
		super.allBankAccListCallBack(resultObj);
		showBankAccListDialog();
	}

	/**
	 * 获取资金账户详情
	 * 
	 * @param accId
	 */
	private void requestBankAccountDetail(String accId) {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();
		List<BiiRequestBody> biiRequestBodyList = new ArrayList<BiiRequestBody>();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_BALANCE_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Comm.ACCOUNT_ID, accId);
		biiRequestBody.setParams(paramsmap);
		biiRequestBodyList.add(biiRequestBody);
		HttpManager.requestBii(biiRequestBodyList, this, "bankAccountDetailCallback");
	}

	/** 资金账户详情返回处理 */
	public void bankAccountDetailCallback(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().dismissMessageDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> bankAccDetail = (Map<String, Object>) (biiResponseBody.getResult());
		List<Map<String, String>> deTailList = (List<Map<String, String>>) bankAccDetail.get("accountDetaiList");

		// liMoneyContentLayout.setVisibility(View.VISIBLE);
		// liCecContentLayout.setVisibility(View.VISIBLE);
		// liMoneyNoContentLayout.setVisibility(View.GONE);
		// accBank = true;
		showAccItemDetail(ThirdDataCenter.getInstance().getBankAccountList().get(bankselectpostion), deTailList);
	}

	/**
	 * 证券保证金列表返回
	 * 
	 * @param resultObj
	 */
	@Override
	public void cecurityListCallBack(Object resultObj) {
		super.cecurityListCallBack(resultObj);
		showCecAccListDialog();
	}

	// // 获取证券详情
	private void requestCecurityDetail() {
		BaseDroidApp.getInstanse().dismissMessageDialog();
		showAccItemDetail(ThirdDataCenter.getInstance().getCecAccountList().get(cecselectpostion), null);
	}

	// 获取证券资金账户余额
	private void getCecurityAmout(String ps, String ps_rc) {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Third.METHOD_CECURITY_AMOUT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));

		Map<String, Object> data = ThirdDataCenter.getInstance().getCecAccountList().get(cecselectpostion);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Third.CECURITY_AMOUT_ACCID, ThirdDataCenter.getInstance().getBankAccountList()
				.get(bankselectpostion).get(Comm.ACCOUNT_ID));
		params.put(Third.CECURITY_AMOUT_BZ, Third.ENCY_RMB);
		params.put(Third.CECURITY_AMOUT_PS, ps);
		params.put(Third.CECURITY_AMOUT_PS_RC, ps_rc);
		params.put(Third.CECURITY_AMOUT_COMANY, data.get(Third.CECURITYTRADE_COMPANY));
		params.put(Third.CECURITY_AMOUT_STCODE, data.get(Third.CECURITYTRADE_STOCKNO));
		params.put(Third.CECURITY_AMOUT_CAACC, data.get(Third.CECURITYTRADE_BANKACCNUM_RE));
		SipBoxUtils.setSipBoxParams(params);
		biiRequestBody.setParams(params);
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "cecurityAmoutCallBack");
	}

	/** 证券资金账户余额 返回 **/
	public void cecurityAmoutCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		tvAmoutView.setVisibility(View.VISIBLE);
		tvAmoutView.setText(StringUtil.parseStringPattern((String) result.get(Third.CECURITY_AMOUT_AVAI), 2));
		btnQueryAmoutView.setText(R.string.refresh);
		// 清空密码刷新重新输入密码
		password = "";
	}

	// 请求随机数
	private void requestRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "queryTradeRandomNumberCallBack");
	}

	/** 请求密码控件随机数 回调 **/
	public void queryTradeRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		// 加密控件设置随机数
		if (sipTag == 1) {
			View dialogView = LayoutInflater.from(this).inflate(R.layout.third_cecuritytrade_sipbox, null);
			// 初始化密码控件
			sipBox = (SipBox) dialogView.findViewById(R.id.modify_pwd_sip);
			sipBox.setCipherType(SystemConfig.CIPHERTYPE);
			sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
			sipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
			sipBox.setId(10002);
			sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
			sipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
			sipBox.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
			sipBox.setSipDelegator(new MyCFCASipDelegator(dialogView));
			sipBox.setRandomKey_S(randomNumber);
			BaseDroidApp.getInstanse().showDialog(dialogView);
		} else if (sipTag == 2) {
			showEceuritytoBankLyout();
			tradeSipBxo.setRandomKey_S(randomNumber);
		}
	}

	// 银行账户列表
	private void showBankAccListDialog() {
		List<Map<String, Object>> bankAccoutList = ThirdDataCenter.getInstance().getBankAccountList();
		if (StringUtil.isNullOrEmpty(bankAccoutList)) {// 如果没有符合要求的数据 弹框提示
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.query_no_results));
			return;
		}
		ListView bankListView = new ListView(this);
		bankListView.setFadingEdgeLength(0);
		bankListView.setScrollingCacheEnabled(false);
		BankAdapter mAdapter = new BankAdapter(this, bankAccoutList);
		bankListView.setAdapter(mAdapter);
		bankListView.setOnItemClickListener(moneyAccItemClick);
		// 弹出转出转换列表框
		BaseDroidApp.getInstanse().showTranoutDialog(bankListView, getText(R.string.third_bankacc_tip).toString(),
				backListener, /*addNewAcc*/null);
	}

	// 保证金账户列表
	private void showCecAccListDialog() {
		List<Map<String, Object>> cecAccoutList = ThirdDataCenter.getInstance().getCecAccountList();
		if (StringUtil.isNullOrEmpty(cecAccoutList)) {// 如果没有符合要求的数据 弹框提示
			// BaseDroidApp.getInstanse().showErrorDialog(message,
			// R.string.cancle, R.string.confirm,
			// oncecaccClicklistener);
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.query_no_result));
			return;
		}
		ListView cecListView = new ListView(this);
		cecListView.setFadingEdgeLength(0);
		cecListView.setScrollingCacheEnabled(false);
		CecurityAdapter mAdapter = new CecurityAdapter(this, cecAccoutList);
		cecListView.setAdapter(mAdapter);
		cecListView.setOnItemClickListener(eceurityAccItemClick);
		// 弹出转入转换列表框
		BaseDroidApp.getInstanse().showTranoutDialog(cecListView, getText(R.string.third_cecacc_tip).toString(),
				backListener, null);
	}

	/** 新增关联账户 **/
	private OnClickListener addNewAcc = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().showErrorDialog(getText(R.string.third_relevance_account_prompt).toString(),
					R.string.cancle, R.string.confirm, new OnClickListener() {
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							switch ((Integer) v.getTag()) {
							case CustomDialog.TAG_SURE:// 确定
//								Intent intent = new Intent(getActivity(), AccInputRelevanceAccountActivity.class);
//								startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
								BusinessModelControl.gotoAccRelevanceAccount(getActivity(),  ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
								break;
							}
						}
					});
		}
	};

	private OnClickListener backListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
		}
	};

	/**
	 * 显示转出账户条目的详细信息 dataMap 账户 accountDetaiList 账号列表
	 */
	private void showAccItemDetail(Map<String, Object> dataMap, List<Map<String, String>> accountDetaiList) {
		if (dataMap == null) {
			return;
		}
		btnRight.setVisibility(View.VISIBLE);
		if (accountDetaiList != null) {
			// 资金
			if (!accountDetaiList.isEmpty()) {
				TextView tvAccType_bank = (TextView) viewContent.findViewById(R.id.tv_bank_type);
				TextView tvNickName_bank = (TextView) viewContent.findViewById(R.id.tv_bank_niname);
				TextView tvNumber_bank = (TextView) viewContent.findViewById(R.id.tv_bank_num);
				TextView TvBiZhong_bank = (TextView) viewContent.findViewById(R.id.tv_bank_bizhong);
				TextView tvAmout_bank = (TextView) viewContent.findViewById(R.id.tv_bank_amout);
				PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvAccType_bank);
				PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvNickName_bank);

				Map<String, String> accountDetaiMap = null;
				for (Map<String, String> acc : accountDetaiList) {
					if (CurrencyType.RMB.equals(acc.get(Comm.CURRENCYCODE))) {
						accountDetaiMap = acc;
						break;
					}
				}

				String strAccountType = LocalData.AccountType.get((String) dataMap.get(Comm.ACCOUNT_TYPE));
				tvAccType_bank.setText(strAccountType);
				tvNickName_bank.setText((String) dataMap.get(Comm.NICKNAME));
				String accoutNumber = (String) dataMap.get(Comm.ACCOUNTNUMBER);
				tvNumber_bank.setText(StringUtil.getForSixForString(accoutNumber));
				String currencyCode = (String) dataMap.get(Comm.CURRENCYCODE);
				TvBiZhong_bank.setText(BiiConstant.CurrencyType.getCurrencyTypeStr(currencyCode));

				if (accountDetaiMap != null) {
					tvAmout_bank.setText(StringUtil.parseStringPattern(
							(String) accountDetaiMap.get(Third.CECURITY_AMOUT_AVAI), 2));
				} else {
					tvAmout_bank.setText("");
				}

//				mBankSeleView.setText(R.string.tran_select_again);
				mBankSeleView.setVisibility(View.GONE);
				liMoneyContentLayout.setVisibility(View.VISIBLE);
			}
		} else {
			// 保证金
//			TextView tvAccType = (TextView) viewContent.findViewById(R.id.tv_bank_type_ac);
			TextView tvNumber = (TextView) viewContent.findViewById(R.id.tv_bank_num_ac);
			TextView tvBiZhong = (TextView) viewContent.findViewById(R.id.tv_bank_bizhong_ac);
//			tvAccType.setText(R.string.trans_atm_account);
			tvBiZhong.setText(R.string.tran_currency_rmb);
			String num = (String) dataMap.get(Third.CECURITYTRADE_BANKACCNUM_RE);
			tvNumber.setText(num);
			btnQueryAmoutView.setText(getString(R.string.query));
//			mCecSeleView.setText(R.string.tran_select_again);
			mCecSeleView.setVisibility(View.GONE);
			liCecContentLayout.setVisibility(View.VISIBLE);
			showTradebtnLayout();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE) {
			if (resultCode == RESULT_OK) {
//				requestCecurityForBankAcc((String) ThirdDataCenter.getInstance().getBankAccountList()
//						.get(bankselectpostion).get(Comm.ACCOUNTNUMBER));
				findViewById(R.id.layout_moneyacc).performClick();
			}
		} else if (requestCode == REQUEST_CONFIRM_CODE) {
			tradeSipBxo.clearText();
		}
	}

	/** 恢复资金账户 */
	private void initMoneyAccLayout() {
		liMoneyContentLayout.setVisibility(View.GONE);
//		mBankSeleView.setText(R.string.please_choose);
		mBankSeleView.setVisibility(View.VISIBLE);
		initCecurAccLayout();
	}

	/** 恢复保证金账户 */
	private void initCecurAccLayout() {
		liCecContentLayout.setVisibility(View.GONE);
		btnQueryAmoutView.setText(getString(R.string.query));
//		mCecSeleView.setText(R.string.please_choose);
		mCecSeleView.setVisibility(View.VISIBLE);
		password = "";
		raNum = "";
		tvAmoutView.setText("");
		initTradebtnLayout();
	}

	/** 恢复提示信息 */
	private void initTradebtnLayout() {
		layoutMsgFillLayout.setVisibility(View.GONE);
		layoutTradebtnLayout.setVisibility(View.GONE);
		mTipView.setVisibility(View.VISIBLE);
		mTipTitleView.setText(R.string.third_account_setting);
	}

	/** 显示操作按钮 */
	private void showTradebtnLayout() {
		mTipView.setVisibility(View.GONE);
		layoutMsgFillLayout.setVisibility(View.GONE);
		layoutTradebtnLayout.setVisibility(View.VISIBLE);
	}

	/** 显示银转证 */
	private void showBanktoEceurityLayout() {
		mTipTitleView.setText(this.getString(R.string.third_btn_banktocecurity));
		layoutSip.setVisibility(View.GONE);
		layoutTradebtnLayout.setVisibility(View.GONE);
		layoutMsgFillLayout.setVisibility(View.VISIBLE);
		edMoneyView.setText("");
		tradeSipBxo.clearText();
	}

	/** 显示证转银 */
	private void showEceuritytoBankLyout() {
		mTipTitleView.setText(this.getString(R.string.third_btn_cecuritytobank));
		layoutTradebtnLayout.setVisibility(View.GONE);
		layoutMsgFillLayout.setVisibility(View.VISIBLE);
		layoutSip.setVisibility(View.VISIBLE);
		edMoneyView.setText("");
		tradeSipBxo.clearText();
	}

	private class MyCFCASipDelegator implements CFCASipDelegator {

		private View _paramsView;
		private boolean isKeyboardShow;

		public MyCFCASipDelegator(View paramsView) {
			super();
			this._paramsView = paramsView;
			isKeyboardShow = true;
		}

		@Override
		public void afterClickDown(SipBox sipbox) {

		}

		@Override
		public void afterKeyboardHidden(SipBox sipbox, int keyboardHeight) {
			isKeyboardShow = false;
			if (_paramsView != null) {
				_paramsView.scrollTo(0, 0);
			}
		}

		@Override
		public void beforeKeyboardShow(final SipBox sipbox, final int keyboardHeight) {
			isKeyboardShow = true;
			if (_paramsView != null) {

				// XXX 800毫秒优化
				sipbox.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (isKeyboardShow) {
							// 判断弹出的安全键盘是否会遮掩输入框
							int[] location = new int[2];
							sipbox.getLocationOnScreen(location);
							int y = location[1];
							// 距底距离
							int bottom = getWindowHeight() - y - sipbox.getHeight();
							if (bottom < keyboardHeight) {// 说明遮掩
								_paramsView.scrollTo(0, keyboardHeight - bottom);

							}
						}
					}
				}, 800);
			}
		}

	}

	private CecurityTradeActivity getActivity() {
		return this;
	}

}
