package com.chinamworld.bocmbci.biz.acc.dialogActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.inflaterview.InflaterViewDialog;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.TransferManagerActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/** 电子现金账户弹出框 */
public class IcCardDetailDialog extends BaseActivity {
	private RelativeLayout rl_bank;
	private String nickname;
	/** 选择的电子现金账户 */
	private Map<String, Object> bankmap;
	/** 账户详情及余额 */
	private Map<String, String> callbackmap;
	/** 签约账户账号 */
	private String signnum;
	/** 所有账户列表 */
	// 签约使用
	private List<Map<String, String>> accountList;
	/** 接口标记选择,1代表修改账户别名,2代表新增绑定关系 */
	private int conid = 0;
	/** 获取到的tokenId 保存 */
	private String tokenId;
	/** 选择的签约账户 */
	private Map<String, String> icsignmap;
	/** 签约提交弹出框 */
	private View icSignCreatResView;
	/** 第一个弹出框View */
	private RelativeLayout icAccountDetailView;// 第一个
	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	/** 是否上传短信验证码 */
	private boolean isSms = false;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/**是否上传中银E盾*/
	private boolean isNeedUsbKey = false;
	/** 加密控件里的随机数 */
	protected String randomNumber;
	private boolean ishavecurrencytwo = false;
	private int detailPosition = 0;
	/** 请求回来的电子现金账户列表 */
	protected List<Map<String, Object>> financeIcAccountList;
	/** 投资理财列表 */
	private List<Map<String, String>> serviceList = new ArrayList<Map<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseDroidApp.getInstanse().setDialogAct(true);
		// 添加布局
		setContentView(R.layout.acc_for_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
		android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(
				LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT * 7 / 8);
		rl_bank.setLayoutParams(lp);
		financeIcAccountList = AccDataCenter.getInstance()
				.getFinanceIcAccountList();
		callbackmap = AccDataCenter.getInstance().getCallbackmap();
		signnum = this.getIntent().getStringExtra(ConstantGloble.ACC_ISMY);
		detailPosition = this.getIntent().getIntExtra(
				ConstantGloble.ACC_POSITION, 0);
		bankmap = financeIcAccountList.get(detailPosition);
		InflaterViewDialog inflaterdialog = new InflaterViewDialog(this);
		icAccountDetailView = (RelativeLayout) inflaterdialog
				.initFinanceIcAccountMessageDialogView(ishavecurrencytwo,
						signnum, bankmap, callbackmap, exitAccDetailClick,
						creatIcSignClick, deleteIcSignClick,
						updateNicknameClick, serviceList, crcdPayment, null);
		rl_bank.removeAllViews();
		rl_bank.addView(icAccountDetailView);
	}

	OnClickListener crcdPayment = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// 信用卡还款
			String accountIdcrcd = (String) bankmap.get(Acc.ACC_ACCOUNTID_RES);

			requestPsnCrcdQueryAccountDetail(accountIdcrcd,
					ConstantGloble.BOCINVT_CURRENCY_RMB);
		}
	};

	/**
	 * 请求查询信用卡详情
	 */
	public void requestPsnCrcdQueryAccountDetail(String accountId, String value) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_ACCOUNTDETAIL_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		paramsmap.put(Crcd.CRCD_CURRENCY, value);
		biiRequestBody.setParams(paramsmap);
		BaseActivity activity = (BaseActivity) BaseDroidApp.getInstanse()
				.getCurrentAct();
		if(activity != null)
			HttpManager.requestBii(biiRequestBody, activity, "requestPsnCrcdQueryAccountDetailCallBack");
	}

	public void requestPsnCrcdQueryAccountDetailCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultDetail = (Map<String, Object>) biiResponseBody
				.getResult();
		BiiHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultDetail)) {
			return;
		}
		TranDataCenter.getInstance().setAccInInfoMap(bankmap);
		TranDataCenter.getInstance().setCurrInDetail(resultDetail);
		Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(),
				TransferManagerActivity1.class);
		TranDataCenter.getInstance().setModuleType(
				ConstantGloble.ACC_MANAGE_ELEC_TYPE);
		intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,
				ConstantGloble.REL_CRCD_REPAY);
		BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
	}

	/** 新建签约监听事件 */
	OnClickListener creatIcSignClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 选择签约账户
			// 请求所有账户列表
			requestBankAccountList();
		}
	};

	/** 请求所有账户列表信息 借记卡、信用卡 */
	public void requestBankAccountList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_LIST_API);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> paramslist = new ArrayList<String>();
		paramslist.add(AccBaseActivity.accountTypeList.get(1));
		paramslist.add(AccBaseActivity.accountTypeList.get(2));
		paramslist.add(AccBaseActivity.accountTypeList.get(3));
		paramslist.add(AccBaseActivity.accountTypeList.get(4));
		paramsmap.put(Acc.ACC_ACCOUNTTYPE_REQ, paramslist);
		biiRequestBody.setParams(paramsmap);
		// 通讯开始,开启通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestBankAccountListCallBack");
	}

	/**
	 * 请求所有账户列表回调 不传参数
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestBankAccountListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		accountList = (List<Map<String, String>>) (biiResponseBody.getResult());
		if (accountList == null || accountList.size() == 0) {
			BaseDroidApp.getInstanse().showMessageDialog(
					BaseDroidApp.getInstanse().getCurrentAct()
							.getString(R.string.acc_transferquery_null),
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							ActivityTaskManager.getInstance()
									.removeAllActivity();
						}
					});
			return;
		}
		// 过滤借记虚拟卡
		List<Map<String, String>> bankAccountList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < accountList.size(); i++) {
			String type = (String) accountList.get(i).get(
					Acc.ACC_ACCOUNTTYPE_RES);
			if (type.equals(ConstantGloble.JIEJIXN)) {

			} else {
				bankAccountList.add(accountList.get(i));
			}
		}
		accountList.clear();
		accountList = bankAccountList;
		if (accountList == null || accountList.size() == 0) {
			// 通讯结束,关闭通讯框
			BaseDroidApp.getInstanse().showMessageDialog(
					BaseDroidApp.getInstanse().getCurrentAct()
							.getString(R.string.acc_transferquery_null),
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							ActivityTaskManager.getInstance()
									.removeAllActivity();
						}
					});
			return;
		}
		// 新建签约选择账户弹出框
		InflaterViewDialog inflaterdialog = new InflaterViewDialog(this);
		View inflaterview = (View) inflaterdialog.initFinanceICCreatSignView(
				(String) bankmap.get(Acc.ACC_ACCOUNTNUMBER_RES), accountList,
				exitAccDetailClick, btnNextClick, btnLastClick);
		rl_bank.removeAllViews();
		rl_bank.addView(inflaterview);
	}

	/** 删除签约监听事件 */
	OnClickListener deleteIcSignClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp
					.getInstanse()
					.showErrorDialog(
							IcCardDetailDialog.this
									.getString(R.string.acc_deleteIcSign_msg),
							R.string.cancle, R.string.confirm,
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									switch (Integer.parseInt(v.getTag() + "")) {

									case CustomDialog.TAG_CANCLE:
										// 取消操作
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										break;
									case CustomDialog.TAG_SURE:
										// 确定取消关联
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										conid = 3;
										requestAllBankAccountList();
										break;
									}
								}
							});
		}
	};

	/** 修改账户别名监听事件 */
	public OnClickListener updateNicknameClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText et_acc_accountnickname = (EditText) rl_bank
					.findViewById(R.id.et_acc_nickname);
			nickname = et_acc_accountnickname.getText().toString().trim();
			TextView acc_accountnickname_value = (TextView) rl_bank
					.findViewById(R.id.acc_accountnickname_value);
			if (nickname.equals(acc_accountnickname_value.getText().toString()
					.trim())) {
				// 重新展示账户详情窗口
				closeInput(et_acc_accountnickname);
				InflaterViewDialog inflaterdialog = new InflaterViewDialog(
						IcCardDetailDialog.this);
				RelativeLayout icActDetailView = (RelativeLayout) inflaterdialog
						.initFinanceIcAccountMessageDialogView(
								ishavecurrencytwo, signnum, bankmap,
								callbackmap, exitAccDetailClick,
								creatIcSignClick, deleteIcSignClick,
								updateNicknameClick, serviceList, crcdPayment, null);
				rl_bank.removeAllViews();
				rl_bank.addView(icActDetailView);
				return;
			}
			// 以下为验证
			// 账户别名
			RegexpBean reb = new RegexpBean(
					IcCardDetailDialog.this.getString(R.string.nickname_regex),
					nickname, "nickname");

			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb);
			if (RegexpUtils.regexpDate(lists)) {// 校验通过
				conid = 1;
				closeInput(et_acc_accountnickname);
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}

		}
	};

	/** 请求所有账户列表信息 不传参数 */
	public void requestAllBankAccountList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_LIST_API);
		// 通讯开始,开启通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestAllBankAccountListCallBack");
	}

	/**
	 * 请求所有账户列表回调 不传参数
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestAllBankAccountListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		List<Map<String, Object>> accountList = (List<Map<String, Object>>) (biiResponseBody
				.getResult());
		if (accountList == null || accountList.size() == 0) {
			// 通讯结束,关闭通讯框
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showMessageDialog(
					BaseDroidApp.getInstanse().getCurrentAct()
							.getString(R.string.acc_transferquery_null),
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							ActivityTaskManager.getInstance()
									.removeAllActivity();
						}
					});
			return;
		}
		AccDataCenter.getInstance().setBankAccountList(accountList);
		requestCommConversationId();
	}

	/**
	 * 请求conversationid回调
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {

		super.requestCommConversationIdCallBack(resultObj);
		if (conid == 1) {
			// 修改账户别名
			pSNGetTokenId();
		} else if (conid == 2) {
			// 新增绑定关系
			// 请求安全因子组合
			requestGetSecurityFactor(ConstantGloble.FINANCEICSIGNCREATSERVICEID);
		} else if (conid == 3) {
			// 删除绑定关系
			requestTokenId(1);
		}

	}

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
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
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 请求修改账户别名
		requestModifyAccountAlias(tokenId);
	}

	/** 请求tokenid */
	public void requestTokenId(int i) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		if (i == 0) {
			BiiHttpEngine.showProgressDialog();
		}
		HttpManager.requestBii(biiRequestBody, this, "requestTokenIdCallBack");
	}

	/** 请求tokenid回调 */
	public void requestTokenIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		if (conid == 2) {
			// 新增绑定提交交易
			requestPsnFinanceICSignCreatRes();
		} else if (conid == 3) {
			// 请求删除绑定关系
			requestDeleteIcSign();
		}

	}

	/** 请求删除绑定关系 */
	public void requestDeleteIcSign() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.PSNFINANCEICSIGNCANCLE_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		List<Map<String, Object>> bankList = AccDataCenter.getInstance()
				.getBankAccountList();
		for (int i = 0; i < bankList.size(); i++) {
			if (signnum.equals(bankList.get(i).get(Acc.ACC_ACCOUNTNUMBER_RES))) {
				paramsmap.put(Acc.ICDELECTSIGN_BANKACTID_REQ, (String) bankList
						.get(i).get(Acc.ACC_ACCOUNTID_RES));
				break;
			}
		}
		paramsmap.put(Acc.ICDELECTSIGN_ICACTNUM_REQ,
				String.valueOf(bankmap.get(Acc.ACC_ACCOUNTNUMBER_RES)));
		paramsmap.put(Acc.ICSIGN_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestDeleteIcSignCallBack");
	}

	/**
	 * 请求删除绑定关系回调
	 * 
	 * @param resultObj
	 */
	public void requestDeleteIcSignCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();

		CustomDialog.toastShow(this,
				this.getString(R.string.acc_deleteIcSign_toast));
		signnum = "";
		InflaterViewDialog inflaterdialog = new InflaterViewDialog(this);
		RelativeLayout icActDetailView = (RelativeLayout) inflaterdialog
				.initFinanceIcAccountMessageDialogView(ishavecurrencytwo,
						signnum, bankmap, callbackmap, exitAccDetailClick,
						creatIcSignClick, deleteIcSignClick,
						updateNicknameClick, serviceList, crcdPayment, null);
		rl_bank.removeAllViews();
		rl_bank.addView(icActDetailView);

	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						requestForRandomNumber();
					}
				});
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,
				"queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		// 请求新增绑定关系预交易
		requestFinanceICSignCreat();

	}

	OnClickListener sendSmcListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			sendMSCToMobile();
		}
	};

	public void sendMSCToMobile() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_SENDMSC);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "sendMSCToMobileCallback");
	}

	public void sendMSCToMobileCallback(Object resultObj) {
	}

	/** 请求新增绑定关系预交易 */
	public void requestFinanceICSignCreat() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.FINANCEICSIGNCREAT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.ICSIGN_BANKACTNUM_REQ,
				String.valueOf(icsignmap.get(Acc.ACC_ACCOUNTNUMBER_RES)));
		paramsmap.put(Acc.ICSIGN_ICACTNUM_REQ,
				String.valueOf(bankmap.get(Acc.ACC_ACCOUNTNUMBER_RES)));
		paramsmap.put(Acc.ICSIGN_COMBINID_REQ, BaseDroidApp.getInstanse()
				.getSecurityChoosed());
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestFinanceICSignCreatCallBack");
	}

	/**
	 * 请求新增绑定关系预交易回调
	 * 
	 * @param resultObj
	 */
	public void requestFinanceICSignCreatCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		Map<String, Object> icSignCreatmap = (Map<String, Object>) (biiResponseBody
				.getResult());
		if (StringUtil.isNullOrEmpty(icSignCreatmap)) {
			return;
		}
		factorList = (List<Map<String, Object>>) icSignCreatmap
				.get(Acc.RELEVANCEACCPRE_ACC_FACTORLIST_RES);
		InflaterViewDialog inflaterdialog = new InflaterViewDialog(this);
		LinearLayout.LayoutParams param = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
//		if (!StringUtil.isNullOrEmpty(factorList)) {
//			for (int i = 0; i < factorList.size(); i++) {
//				Map<String, Object> itemMap = factorList.get(i);
//				Map<String, String> securityMap = (Map<String, String>) itemMap
//						.get(Inves.FIELD);
//				String name = securityMap.get(Inves.NAME);
//				if (Inves.Smc.equals(name)) {
//					isSms = true;
//				} else if (Inves.Otp.equals(name)) {
//					isOtp = true;
//				}else if (Comm.CA.equals(name)) {
//					isNeedUsbKey = true;
//				}
//			}
//		}
		BiiHttpEngine.dissMissProgressDialog();
		// 签约账户确认页
		icSignCreatResView = (View) inflaterdialog
				.initFinanceIcSignConfirmDialog(param, this, signnum,
						String.valueOf(bankmap.get(Acc.ACC_ACCOUNTNUMBER_RES)),
						icsignmap, accountList, exitAccDetailClick,
						btnConfirmClick, btnLastClick, isOtp, isSms,
						randomNumber, sendSmcListener, icSignCreatmap);
		rl_bank.removeAllViews();
		rl_bank.addView(icSignCreatResView);

	}

	/** 上一步点击事件 */
	OnItemClickListener btnLastClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == 0) {
				// 选择签约账户弹窗的上一步点击事件
				InflaterViewDialog inflaterdialog = new InflaterViewDialog(
						IcCardDetailDialog.this);
				RelativeLayout icActDetailView = (RelativeLayout) inflaterdialog
						.initFinanceIcAccountMessageDialogView(
								ishavecurrencytwo, signnum, bankmap,
								callbackmap, exitAccDetailClick,
								creatIcSignClick, deleteIcSignClick,
								updateNicknameClick, serviceList, crcdPayment, null);
				rl_bank.removeAllViews();
				rl_bank.addView(icActDetailView);
			} else if (position == 1) {
				// 新建签约弹窗确认界面上一步点击事件
				InflaterViewDialog inflaterdialog = new InflaterViewDialog(
						IcCardDetailDialog.this);
				View inflaterview = (View) inflaterdialog
						.initFinanceICCreatSignView(
								(String) bankmap.get(Acc.ACC_ACCOUNTNUMBER_RES),
								accountList, exitAccDetailClick, btnNextClick,
								btnLastClick);
				rl_bank.removeAllViews();
				rl_bank.addView(inflaterview);
			}
		}
	};
	/** 新建签约弹窗确认界面确认按钮点击事件 */
	OnClickListener btnConfirmClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 进入签约成功页面
			// 调用PsnFinanceICSignCreatRes接口进行提交
			conid = 2;
			requestTokenId(0);

		}
	};
	/** 选择签约账户弹窗的下一步点击事件 */
	OnItemClickListener btnNextClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// 进入签约确认页面
			if (position == -1) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						IcCardDetailDialog.this
								.getString(R.string.acc_choose_icsign_title));
				return;
			}
			icsignmap = AccDataCenter.getInstance().getIcsignmap();

			// 进行预交易
			conid = 2;
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
		}
	};

	/** 请求新增绑定关系提交交易 */
	public void requestPsnFinanceICSignCreatRes() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.FINANCEICSIGNCREATRES_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = AccDataCenter.getInstance().getUsbparams();
		paramsmap.put(Acc.ICSIGNRES_BANKACTNUM_REQ,
				String.valueOf(icsignmap.get(Acc.ACC_ACCOUNTNUMBER_RES)));
		paramsmap.put(Acc.ICSIGNRES_ICACTNUM_REQ,
				String.valueOf(bankmap.get(Acc.ACC_ACCOUNTNUMBER_RES)));
//		if (isOtp) {
//			paramsmap.put(Comm.Otp, AccDataCenter.getInstance().getOtp());
//			paramsmap.put(Comm.Otp_Rc, AccDataCenter.getInstance()
//					.getOtp_password_RC());
//
//		}
//		if (isSms) {
//			paramsmap.put(Comm.Smc, AccDataCenter.getInstance().getSmc());
//			paramsmap.put(Comm.Smc_Rc, AccDataCenter.getInstance()
//					.getSmc_password_RC());
//		}
		
		paramsmap.put(Acc.ICSIGNRES_TOKEN_REQ, tokenId);
//		UsbKeyText usbKeytext = null;
//		/** 安全工具参数获取 */
//		usbKeytext.InitUsbKeyResult(paramsmap);
		SipBoxUtils.setSipBoxParams(paramsmap);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnFinanceICSignCreatResCallBack");
	}

	/**
	 * 请求新增绑定关系提交交易回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestPsnFinanceICSignCreatResCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();

		InflaterViewDialog inflaterdialog = new InflaterViewDialog(this);
		// 签约账户成功页面
		View inflaterview = (View) inflaterdialog
				.initFinanceIcSignSuccessDialog(
						(String) bankmap.get(Acc.ACC_ACCOUNTNUMBER_RES),
						icsignmap, exitAccDetailClick, btnSuccessClick);
		signnum = icsignmap.get(Acc.ACC_ACCOUNTNUMBER_RES);
		rl_bank.removeAllViews();
		rl_bank.addView(inflaterview);
	}

	/** 回到第一个详情页 */
	OnClickListener btnSuccessClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 显示详情页面
			signnum = icsignmap.get(Acc.ACC_ACCOUNTNUMBER_RES);
			InflaterViewDialog inflaterdialog = new InflaterViewDialog(
					IcCardDetailDialog.this);
			RelativeLayout icBackAccountDetailView = (RelativeLayout) inflaterdialog
					.initFinanceIcAccountMessageDialogView(ishavecurrencytwo,
							signnum, bankmap, callbackmap, exitAccDetailClick,
							creatIcSignClick, deleteIcSignClick,
							updateNicknameClick, serviceList, crcdPayment, null);
			rl_bank.removeAllViews();
			rl_bank.addView(icBackAccountDetailView);
		}
	};

	/** 请求修改账户别名 */
	public void requestModifyAccountAlias(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_MODIFYACCOUNT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.MODIFY_ACC_ACCOUNTID_REQ,
				String.valueOf(bankmap.get(Acc.ACC_ACCOUNTID_RES)));
		paramsmap.put(Acc.MODIFY_ACC_ACCOUNTNICKNAME_REQ, nickname);
		paramsmap.put(Acc.MODIFY_ACC_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"modifyAccountAliasCallBack");
	}

	/**
	 * 请求修改账户别名回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void modifyAccountAliasCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String modifyResult = (String) biiResponseBody.getResult();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(this,
				this.getString(R.string.acc_modifyAccountAlias));
		// 修改显示的账户别名
		bankmap.put(Acc.ACC_NICKNAME_RES, nickname);
		// 重新展示账户详情窗口
		InflaterViewDialog inflaterdialog = new InflaterViewDialog(this);
		RelativeLayout accDetailView = (RelativeLayout) inflaterdialog
				.initFinanceIcAccountMessageDialogView(ishavecurrencytwo,
						signnum, bankmap, callbackmap, exitAccDetailClick,
						creatIcSignClick, deleteIcSignClick,
						updateNicknameClick, serviceList, crcdPayment, null);
		rl_bank.removeAllViews();
		rl_bank.addView(accDetailView);
	}

	/** 退出账户详情监听事件 */
	protected OnClickListener exitAccDetailClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			AccDataCenter.getInstance().setFinanceIcAccountList(
					financeIcAccountList);
			setResult(RESULT_OK);
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
