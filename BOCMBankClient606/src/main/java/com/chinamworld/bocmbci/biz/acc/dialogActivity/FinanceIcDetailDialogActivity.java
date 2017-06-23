package com.chinamworld.bocmbci.biz.acc.dialogActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.inflaterview.InflaterViewDialog;
import com.chinamworld.bocmbci.biz.acc.mybankaccount.AccountIsPayrollDetailActivity;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.myreg.CreateNoticeActivity;
import com.chinamworld.bocmbci.biz.dept.myreg.MyPositDrawMoneyActivity;
import com.chinamworld.bocmbci.biz.dept.myreg.MyRegSaveChooseTranInAccActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.TransferManagerActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/** 我的账户弹出框 */
public class FinanceIcDetailDialogActivity extends BaseActivity {
	private Context context = this;
	/** 账户列表信息页 */
	private RelativeLayout view;
	/** 电子现金账户详情信息 */
	private Map<String, String> callbackmap;
	private boolean ishavecurrencytwo = false;
	private String nickname;
	/** 选择的账户详情 */
	protected Map<String, Object> chooseBankAccount;
	private String tokenId;
	/** 信用卡详情 */
	private Map<String, Object> resultDetail;
	public static String currency;
	private static String currency1 = "";
	private static String currency2 = "";
	private RelativeLayout rl_bank;
	private int detailPosition = 0;
	/** 请求回来的账户列表信息 */
	protected List<Map<String, Object>> bankAccountList;
	/** 投资开通列表 */
	private List<Map<String, String>> serviceList;
	/** 账户状态 */
	private String status;
	private boolean gouhuiclick = false;
	/** 账户类型 */
	private String acctype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestPsnIsPayrollAccount();
		BaseDroidApp.getInstanse().setDialogAct(true);
		bankAccountList = AccDataCenter.getInstance().getBankAccountList();
		serviceList = AccDataCenter.getInstance().getServiceList();
		callbackmap = AccDataCenter.getInstance().getCallbackmap();
		resultDetail = AccDataCenter.getInstance().getResultDetail();
		currency1 = this.getIntent().getStringExtra(ConstantGloble.ACC_ISMY);
		currency2 = this.getIntent().getStringExtra(ConstantGloble.CRCD_CURRENCY);
		status = this.getIntent().getStringExtra(Acc.ACC_ACCOUNTSTATUS_RES);
		if (!StringUtil.isNull(currency1)) {
			currency = this.getIntent().getStringExtra(ConstantGloble.ACC_ISMY);
		} else {
			currency = this.getIntent().getStringExtra(ConstantGloble.CRCD_CURRENCY);
		}
		detailPosition = this.getIntent().getIntExtra(ConstantGloble.ACC_POSITION, 0);
		chooseBankAccount = bankAccountList.get(detailPosition);
		ishavecurrencytwo = this.getIntent().getBooleanExtra(ConstantGloble.CRCD_FLAG, false);
		acctype = (String) chooseBankAccount.get(Acc.ACC_ACCOUNTTYPE_RES);
		// 添加布局
		setContentView(R.layout.acc_for_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
		android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(
				LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT * 7 / 8);
		rl_bank.setLayoutParams(lp);
		InflaterViewDialog inflaterdialog = new InflaterViewDialog(this);
		if (acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_REG) || acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_EDU)
				|| acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_ZOR)) {
			// 存款管理
			view = (RelativeLayout) inflaterdialog.initMySaveDetailDialogView(onCreateNoticeListener, onCheckoutListener,
					onContinueSaveListener, chooseBankAccount, updateNicknameClick, exitAccDetailClick, serviceList, status,
					payrollQueryClick);
		} else if (acctype.equals(ConstantGloble.ZHONGYIN) || acctype.equals(ConstantGloble.GREATWALL)
				|| acctype.equals(ConstantGloble.SINGLEWAIBI)) {
			// 信用卡
			view = (RelativeLayout) inflaterdialog.initCrcdMessageDialogView(ishavecurrencytwo, chooseBankAccount,
					resultDetail, exitAccDetailClick, renmibiClick, dollerClick, currency, currency1, currency2,
					serviceList, gouhui, payrollQueryClick);
		} else if (acctype.equals(ConstantGloble.ICCARD)) {
			// 电子现金账户
			view = (RelativeLayout) inflaterdialog.initFinanceIcAccountMessageDialogView(ishavecurrencytwo, null,
					chooseBankAccount, callbackmap, exitAccDetailClick, null, null, updateNicknameClick, serviceList, null,
					payrollQueryClick);
		} else {
			view = (RelativeLayout) inflaterdialog.initAccountMessageDialogView(FinanceIcDetailDialogActivity.this,
					chooseBankAccount, exitAccDetailClick, updateNicknameClick, ishavecurrencytwo, serviceList, status,
					payrollQueryClick);
		}

		rl_bank.removeAllViews();
		rl_bank.addView(view);
	}

	/** 发送请求判断是否需要工资单查询 */
//	public void requestPsnIsPayrollAccount() {
//		BaseHttpEngine.showProgressDialog();
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Acc.ACC_PSNISPAYROLLACCOUNT);
//		// String accountId =
//		// String.valueOf(chooseBankAccount.get(Acc.ACC_ACCOUNTID_RES));
//		// Map<String, String> map = new HashMap<String, String>();
//		// map.put(Acc.ACC_ACCOUNTID, accountId);
//		// biiRequestBody.setParams(map);
//		HttpManager.requestBii(biiRequestBody, this, "requestPsnIsPayrollAccountCallBack");
//	}

	/** 判断是否需要工资单查询回调 */
//	public void requestPsnIsPayrollAccountCallBack(Object resultObj) {
//		BaseHttpEngine.dissMissProgressDialog();
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		Map<String, Object> isPayrollDetail = (Map<String, Object>) biiResponseBody.getResult();
//		LogGloble.e("====isPayrollDetail", isPayrollDetail.toString());
//		if (StringUtil.isNullOrEmpty(isPayrollDetail)) {
//			return;
//		}
//		Boolean payrollAccountFlag = (Boolean) isPayrollDetail.get(Acc.ACC_PAYROLLACCOUNTFLAG);
//		AccDataCenter.getInstance().setIsPayrollAccount(payrollAccountFlag);
//		String accountId = (String) isPayrollDetail.get(Acc.ACC_ACCOUNTID);
//		String accountType = (String) isPayrollDetail.get(Acc.ACC_ACCOUNTTYPE);
//
//	}

	/** 退出账户详情监听事件 */
	protected OnClickListener exitAccDetailClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		}
	};
	
	/** 账户工资卡查询监听器 */
	protected OnClickListener payrollQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(FinanceIcDetailDialogActivity.this, AccountIsPayrollDetailActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	/** 人民币监听事件 */
	protected OnClickListener renmibiClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			currency = currency1;
			requestPsnCrcdQueryAccountDetail(chooseBankAccount, currency);
		}
	};

	/** 外币监听事件 */
	protected OnClickListener dollerClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			currency = currency2;
			requestPsnCrcdQueryAccountDetail(chooseBankAccount, currency);
		}
	};

	/** 请求查询信用卡详情*/
	public void requestPsnCrcdQueryAccountDetail(Map<String, Object> map, String value) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_ACCOUNTDETAIL_API);
		String accountId = (String) map.get(Crcd.CRCD_ACCOUNTID_RES);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		paramsmap.put(Crcd.CRCD_CURRENCY, value);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCrcdQueryAccountDetailCallBack");
	}

	/**
	 * 信用卡详情回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnCrcdQueryAccountDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultDetail = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultDetail)) {
			return;
		}
		Map<String, Object> detailMap = new HashMap<String, Object>();
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		detailList = (List<Map<String, Object>>) resultDetail.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		if (StringUtil.isNullOrEmpty(detailList)) {
			return;
		}
		detailMap = detailList.get(0);
		if (StringUtil.isNullOrEmpty(detailMap)) {
			return;
		}
		AccDataCenter.getInstance().setResultDetail(detailMap);
		InflaterViewDialog inflaterdialog = new InflaterViewDialog(this);
		view = (RelativeLayout) inflaterdialog.initCrcdMessageDialogView(ishavecurrencytwo, chooseBankAccount, resultDetail,
				exitAccDetailClick, renmibiClick, dollerClick, currency, currency1, currency2, serviceList, gouhui,
				payrollQueryClick);
		rl_bank.removeAllViews();
		rl_bank.addView(view);

	}

	OnClickListener gouhui = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Map<String, String> detailMap = new HashMap<String, String>();
			List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
			detailList = (List<Map<String, String>>) resultDetail.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
			detailMap = detailList.get(0);
			String currentflag = (String) detailMap.get(Crcd.CRCD_CURRENTBALANCEFLAG);
			if (StringUtil.isNull(currentflag)) {
				return;
			}
			if (currentflag.equals(ConstantGloble.LOAN_CURRENTINDEX_VALUE)) {
				gouhuiclick = true;
				requestCommConversationId();
				BiiHttpEngine.showProgressDialog();
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.crcd_foreign_no_owe));
				return;
			}

		}
	};
	
	/** 修改账户别名监听事件 */
	public OnClickListener updateNicknameClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText et_acc_accountnickname = (EditText) view.findViewById(R.id.et_acc_nickname);
			nickname = et_acc_accountnickname.getText().toString().trim();
			String strNickName = (String) chooseBankAccount.get(Acc.ACC_NICKNAME_RES);
			if (nickname.equals(strNickName)) {
				// 重新展示账户详情窗口
				closeInput(et_acc_accountnickname);
				acctype = (String) chooseBankAccount.get(Acc.ACC_ACCOUNTTYPE_RES);
				if (acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_REG)
						|| acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_EDU)
						|| acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_ZOR)) {
					// 存款管理
					InflaterViewDialog inflaterViewDialog = new InflaterViewDialog(FinanceIcDetailDialogActivity.this);
					RelativeLayout accDetailDialog = (RelativeLayout) inflaterViewDialog.initMySaveDetailDialogView(
							onCreateNoticeListener, onCheckoutListener, onContinueSaveListener, chooseBankAccount,
							updateNicknameClick, exitAccDetailClick, serviceList, status, payrollQueryClick);
					view.removeAllViews();
					view.addView(accDetailDialog);
				} else if (acctype.equals(ConstantGloble.ICCARD)) {
					InflaterViewDialog inflaterViewDialog = new InflaterViewDialog(FinanceIcDetailDialogActivity.this);
					RelativeLayout accDetailDialog = (RelativeLayout) inflaterViewDialog
							.initFinanceIcAccountMessageDialogView(ishavecurrencytwo, null, chooseBankAccount, callbackmap,
									exitAccDetailClick, null, null, updateNicknameClick, serviceList, null,
									payrollQueryClick);
					view.removeAllViews();
					view.addView(accDetailDialog);
				} else {
					// 重新展示账户详情窗口
					InflaterViewDialog inflaterViewDialog = new InflaterViewDialog(FinanceIcDetailDialogActivity.this);
					RelativeLayout accountMessageView = (RelativeLayout) inflaterViewDialog.initAccountMessageDialogView(
							FinanceIcDetailDialogActivity.this, chooseBankAccount, exitAccDetailClick, updateNicknameClick,
							ishavecurrencytwo, serviceList, status, payrollQueryClick);
					view.removeAllViews();
					view.addView(accountMessageView);
				}

				return;
			}
			// 以下为验证
			// 账户别名
			RegexpBean reb = new RegexpBean(FinanceIcDetailDialogActivity.this.getString(R.string.nickname_regex), nickname,
					"nickname");

			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb);
			if (RegexpUtils.regexpDate(lists)) {// 校验通过
				closeInput(et_acc_accountnickname);
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();

			}
		}
	};

	/**
	 * 请求conversationid回调
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (gouhuiclick) {
			requestForCrcdForeignPayOff();
		} else {
			// 获取TokenId
			pSNGetTokenId();
		}
	}

	public void requestForCrcdForeignPayOff() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.CRCDFOREIGNPAYOFF);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		String toPayeeId = (String) chooseBankAccount.get(Crcd.CRCD_ACCOUNTID_RES);
		map.put(Tran.CREDITCARD_CRCDID_REQ, toPayeeId);// 转入账户信息 crcdId
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestForCrcdForeignPayOffCallBack");
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
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		TranDataCenter.getInstance().setRelCrcdBuyCallBackMap(result);
		Map<String, Object> resultMap = TranDataCenter.getInstance().getCurrInDetail();
		// 信用卡购汇还款
		TranDataCenter.getInstance().setAccInInfoMap(chooseBankAccount);
		TranDataCenter.getInstance().setCurrInDetail(resultDetail);
		Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), TransferManagerActivity1.class);
		TranDataCenter.getInstance().setModuleType(ConstantGloble.ACC_MANAGE_TYPE);
		intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG, ConstantGloble.REL_CRCD_BUY);
		intent.putExtra(ConstantGloble.CRCD_CURRENCY2, currency);
		BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
		BaseDroidApp.getInstanse().getCurrentAct().finish();

	}

	/** 获取tokenId*/
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}

	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void aquirePSNGetTokenId(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 请求修改账户别名
		requestModifyAccountAlias(tokenId);
	}

	@Override
	public void finish() {
		super.finish();
		BaseDroidApp.getInstanse().setDialogAct(false);
	}

	/** 请求修改账户别名 */
	public void requestModifyAccountAlias(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_MODIFYACCOUNT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.MODIFY_ACC_ACCOUNTID_REQ, String.valueOf(chooseBankAccount.get(Acc.ACC_ACCOUNTID_RES)));
		paramsmap.put(Acc.MODIFY_ACC_ACCOUNTNICKNAME_REQ, nickname);
		paramsmap.put(Acc.MODIFY_ACC_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "modifyAccountAliasCallBack");
	}

	/**
	 * 请求修改账户别名回调
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	public void modifyAccountAliasCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String result = (String) biiResponseBody.getResult();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(this, this.getString(R.string.acc_modifyAccountAlias));
		// 修改显示的账户别名
		chooseBankAccount.put(Acc.ACC_NICKNAME_RES, nickname);
		String acctype = (String) chooseBankAccount.get(Acc.ACC_ACCOUNTTYPE_RES);
		AccDataCenter.getInstance().setBankAccountList(bankAccountList);
		if (acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_REG) || acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_EDU)
				|| acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_ZOR)) {
			// 存款管理
			InflaterViewDialog inflaterViewDialog = new InflaterViewDialog(FinanceIcDetailDialogActivity.this);
			RelativeLayout accDetailDialog = (RelativeLayout) inflaterViewDialog.initMySaveDetailDialogView(
					onCreateNoticeListener, onCheckoutListener, onContinueSaveListener, chooseBankAccount,
					updateNicknameClick, exitAccDetailClick, serviceList, status, payrollQueryClick);
			view.removeAllViews();
			view.addView(accDetailDialog);
		} else if (acctype.equals(ConstantGloble.ICCARD)) {
			InflaterViewDialog inflaterViewDialog = new InflaterViewDialog(FinanceIcDetailDialogActivity.this);
			RelativeLayout accDetailDialog = (RelativeLayout) inflaterViewDialog.initFinanceIcAccountMessageDialogView(
					ishavecurrencytwo, null, chooseBankAccount, callbackmap, exitAccDetailClick, null, null,
					updateNicknameClick, serviceList, null, payrollQueryClick);
			view.removeAllViews();
			view.addView(accDetailDialog);
		} else {
			// 重新展示账户详情窗口
			InflaterViewDialog inflaterViewDialog = new InflaterViewDialog(this);
			RelativeLayout accountMessageView = (RelativeLayout) inflaterViewDialog.initAccountMessageDialogView(this,
					chooseBankAccount, exitAccDetailClick, updateNicknameClick, ishavecurrencytwo, serviceList, status,
					payrollQueryClick);
			view.removeAllViews();
			view.addView(accountMessageView);
		}

	}

	/** 建立通知按钮 监听*/
	private OnClickListener onCreateNoticeListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String availableBalance = (String) DeptDataCenter.getInstance().getCurDetailContent()
					.get(Dept.AVAILABLE_BALANCE);

			double balanceTemp = Double.valueOf(availableBalance);
			// 判断是否有可用余额
			if (balanceTemp <= 0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("当前存单没有可用的支取余额，请重新选择");
				return;
			}

			Intent intent = new Intent();
			intent.setClass(context, CreateNoticeActivity.class);
			startActivity(intent);
			BaseDroidApp.getInstanse().dismissMessageDialog();// 关闭详情框
			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
		}
	};

	/** 支取按钮 监听*/
	private OnClickListener onCheckoutListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 查询系统时间
			BiiHttpEngine.showProgressDialog();
			requestSystemDateTime();

		}
	};

	/** 查询系统时间返回*/
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);

		Map<String, Object> content = DeptDataCenter.getInstance().getCurDetailContent();
		String endDate = (String) content.get(Dept.INTEREST_ENDDATE);

//		if (content.get(Dept.TYPE).equals(DeptBaseActivity.NOTIFY_SAVE)) {
//			// 如果是通知存款 发送通讯 查询通知
//			String volumeNumber = (String) content.get(Dept.VOLUME_NUMBER);
//			String cdNumber = (String) content.get(Dept.CD_NUMBER);
//			requestQueryNotify((String) chooseBankAccount.get(Acc.ACC_ACCOUNTID_RES), volumeNumber, cdNumber);
//		} else {
//			BaseHttpEngine.dissMissProgressDialog();
//			if (!StringUtil.isNullOrEmpty(endDate)) {
//				Intent intent = new Intent();
//				intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MyPositDrawMoneyActivity.class);
//				startActivity(intent);
//				finish();
//			} else {// 没有违约
//				Intent intent = new Intent();
//				intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MyPositDrawMoneyActivity.class);
//				startActivity(intent);
//				finish();
//			}
//		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////
		// TODO add by 2016年3月11日 修改支取
		if (content.get(Dept.TYPE).equals(DeptBaseActivity.NOTIFY_SAVE)) {
			// 如果是通知存款 发送通讯 查询通知
			String strConvertType = (String) DeptDataCenter.getInstance().getCurDetailContent()
					.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
			if (strConvertType.equals(ConstantGloble.CONVERTTYPE_N)) {// 非约定转存
				String volumeNumber = (String) content.get(Dept.VOLUME_NUMBER);
				String cdNumber = (String) content.get(Dept.CD_NUMBER);
				// requestQueryNotify(accountId, volumeNumber, cdNumber);
				requestQueryNotify((String) chooseBankAccount.get(Acc.ACC_ACCOUNTID_RES), volumeNumber, cdNumber);
			} else {// 约定转存
				BiiHttpEngine.dissMissProgressDialog();
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
				intent.setClass(context, MyPositDrawMoneyActivity.class);
				startActivity(intent);
			}
		} else {
			BiiHttpEngine.dissMissProgressDialog();
			if (!StringUtil.isNullOrEmpty(endDate)) {
				boolean noticeFlag = QueryDateUtils.compareDate(dateTime, endDate);
				if (!noticeFlag) {// 未到期提醒
					BaseDroidApp.getInstanse().showErrorDialog(
							this.getResources().getString(R.string.checkout_notify_message), R.string.cancle,
							R.string.confirm, onclicklistener);
				} else {
					Intent intent = new Intent();
					intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
					intent.setClass(context, MyPositDrawMoneyActivity.class);
					startActivity(intent);
				}
			} else { // 没有违约
				BiiHttpEngine.dissMissProgressDialog();
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
				intent.setClass(context, MyPositDrawMoneyActivity.class);
				startActivity(intent);
			}
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////
	}

	// TODO 已注释 start-------------------------------------------------------------------------------------
	/** 查询 通知详情*/  
	public void requestQueryNotify(String accountId, String volumeNumber, String cdNumber) {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.REQUEST_QUERY_NOTIFY);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Comm.ACCOUNT_ID, accountId);
		paramsmap.put(Dept.VOLUME_NUMBER, volumeNumber);
		paramsmap.put(Dept.CD_NUMBER, cdNumber);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestQueryNotifyCallBack");
	}

	public void requestQueryNotifyCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) biiResponseBody.getResult();
		// 将数据放入数据中心
		DeptDataCenter.getInstance().setQueryNotifyCallBackList(resultList);
		List<Map<String, Object>> noticeDetailList = DeptDataCenter.getInstance().getQueryNotifyCallBackList();
		// 通知编号list
		List<String> noticeIdList = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(noticeDetailList)) {
			for (int i = 0; i < noticeDetailList.size(); i++) {
				String noticeNo = (String) noticeDetailList.get(i).get(Dept.NOTIFY_ID);
				if (!StringUtil.isNullOrEmpty(noticeNo)) {
					noticeIdList.add(noticeNo);
				}
			}
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
				return;
			} else {// 有通知编号 判断支取日期
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MyPositDrawMoneyActivity.class);
				startActivity(intent);
				finish();
			}
		} else {// 约定转存
			Intent intent = new Intent();
			intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
			intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MyPositDrawMoneyActivity.class);
			startActivity(intent);
			finish();
		}
	}	
	// TODO 已注释 end---------------------------------------------------------------------------------------

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {
			if (Dept.REQUEST_QUERY_NOTIFY.equals(biiResponseBody.getMethod())) {// 返回的是错误码
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
								BiiHttpEngine.dissMissProgressDialog(); // 要重新登录
								showTimeOutDialog(biiError.getMessage());
							} else {// 非会话超时错误拦截
								BiiHttpEngine.dissMissProgressDialog();
								DeptDataCenter.getInstance().setNoticeIdList(null);
								BaseDroidApp.getInstanse().showErrorDialog(
										this.getResources().getString(R.string.no_notify), R.string.cancle,
										R.string.confirm, onclicklistener);

							}
							return true;
						}
					}
				}
			}
		}
		return super.httpRequestCallBackPre(resultObj);
	}

//	public void showTimeOutDialog(BiiError biiError) {
//		BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(), new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				BaseDroidApp.getInstanse().dismissErrorDialog();
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(FinanceIcDetailDialogActivity.this, LoginActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//			}
//		});
//	}

	/** 未到期提醒框 按钮监听*/
	private OnClickListener onclicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_SURE:// 确定
				BaseDroidApp.getInstanse().dismissErrorDialog();
				// 如果是整存整取
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MyPositDrawMoneyActivity.class);
				startActivity(intent);
				finish();
				break;
			case CustomDialog.TAG_CANCLE:// 取消
				BaseDroidApp.getInstanse().dismissErrorDialog();

				break;

			default:
				break;
			}

		}
	};
	
	/** 续存按钮 监听 教育储蓄和零存整取*/
	private OnClickListener onContinueSaveListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra(ConstantGloble.CONTINUE_SAVE_FLAG, "Y");// 续存
			intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
			intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MyRegSaveChooseTranInAccActivity.class);
			startActivity(intent);
			finish();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		BaseDroidApp.getInstanse().setDialogAct(true);
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.OneTask;
	}
}
