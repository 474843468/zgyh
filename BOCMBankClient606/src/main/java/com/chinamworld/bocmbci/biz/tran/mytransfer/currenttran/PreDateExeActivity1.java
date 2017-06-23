package com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.twodimentrans.TwoDimenTransActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class PreDateExeActivity1 extends TranBaseActivity {

	private static final String TAG = "PreDateExeActivity1";
	/** 选择日期 */
	private TextView chooseDateTv;
	/** 上一步 */
	// private Button lastBtn;
	/** 下一步 */
	private Button nextBtn;

	private View view = null;

	/** 当前系统时间的下一天 */
	private String nextDate;

	private int curYear;
	private int curMonth;
	private int curDate;

	// private int tranTypeFlag;

	private String combineId;

	private String executeDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 二级菜单标识
		secondMenuFlag = getIntent().getIntExtra(SENCOND_MENU_KEY, -1);
		if (secondMenuFlag == TWO_DIMEN_TRAN) {// 从二维码转账那边跳转过来
			setTitle(R.string.two_dimen_scan);
			mTopRightBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ActivityTaskManager.getInstance().removeAllActivity();
					Intent intent = new Intent();
					intent.setClass(PreDateExeActivity1.this,
							TwoDimenTransActivity1.class);
					startActivity(intent);
					finish();
				}
			});
		} else {
			setTitle(this.getString(R.string.tran_my_trans));
		}

		LayoutInflater inflater = LayoutInflater.from(this); // 加载理财产品布局
		view = inflater.inflate(R.layout.dept_save_reg_pre_date, null);
		tabcontent.addView(view);

		tranTypeFlag = this.getIntent().getIntExtra(TRANS_TYPE, -1);
		// 步骤栏
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.save_confirm_title1),
						this.getResources().getString(
								R.string.save_confirm_title2),
						this.getResources().getString(
								R.string.save_confirm_title3) });
		StepTitleUtils.getInstance().setTitleStep(1);

		init();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		// newTranBtn.setVisibility(View.VISIBLE);

		chooseDateTv = (TextView) view.findViewById(R.id.dept_choose_date_tv);
		dateTime = this.getIntent().getStringExtra(ConstantGloble.DATE_TIEM);
		if (StringUtil.isNullOrEmpty(dateTime)) {
			return;
		}
		String currentDate = dateTime.substring(0, 10);
		nextDate = QueryDateUtils.getOneDayLater(currentDate);
		curYear = Integer.parseInt(nextDate.substring(0, 4));
		curMonth = Integer.parseInt(nextDate.substring(5, 7));
		curDate = Integer.parseInt(nextDate.substring(8, 10));
		chooseDateTv.setText(nextDate);
		chooseDateTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LogGloble.i(TAG, "chooseDateClick");
				// 第二个参数为用户选择设置按钮后的响应事件
				// 最后的三个参数为缺省显示的年度，月份，及日期信息
				String curDay = chooseDateTv.getText().toString();
				setCurDatePickTime(curDay);

				DatePickerDialog dateDialog = new DatePickerDialog(
						PreDateExeActivity1.this, onDateSetListener, curYear,
						curMonth - 1, curDate);
				dateDialog.show();
			}
		});
		nextBtn = (Button) view.findViewById(R.id.btnNext);
		nextBtn.setOnClickListener(nextBtnListener);
	}

	private void setCurDatePickTime(String curDay) {
		curYear = Integer.parseInt(curDay.substring(0, 4));
		curMonth = Integer.parseInt(curDay.substring(5, 7));
		curDate = Integer.parseInt(curDay.substring(8, 10));
	}

	/**
	 * 日期控件监听
	 */
	DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			String month = String.valueOf(monthOfYear + 1);
			String day = String.valueOf(dayOfMonth);
			if (monthOfYear + 1 < 10) {// 如果月份小于10月 前面加“0”
				month = "0" + String.valueOf(monthOfYear + 1);
			}
			if (dayOfMonth < 10) {
				day = "0" + String.valueOf(dayOfMonth);
			}
			/** 选择的截止日期 */
			String dateStr = (String.valueOf(year) + "/" + month + "/" + day);
			/** 为EditText赋值 */
			chooseDateTv.setText(dateStr);

		}
	};

	/**
	 * 下一步按钮监听
	 */
	private OnClickListener nextBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			executeDate = chooseDateTv.getText().toString();
			if (QueryDateUtils.compareDate(executeDate, dateTime)) {
				// 结束日期在服务器日期之前
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						BaseDroidApp.getInstanse().getCurrentAct()
								.getString(R.string.compare_date_1));
				return;
			} else {

			}
			boolean flag = QueryDateUtils.compareStartThreeMonthDate(
					executeDate, dateTime);
			if (!flag) {
				BaseDroidApp.getInstanse().createDialog(
						null,
						PreDateExeActivity1.this.getResources().getString(
								R.string.execute_day));
				return;
			}
			Map<String, String> userInputMap = TranDataCenter.getInstance()
					.getUserInputMap();
			userInputMap.put(Tran.INPUT_PRE_DATE, executeDate);
			Intent intent = getIntent();
			if (tranTypeFlag == TRANTYPE_NOREL_BANKIN
					|| tranTypeFlag == TRANTYPE_NOREL_BANKOTHER
					|| tranTypeFlag == TRANTYPE_DIR_BANKIN
					|| tranTypeFlag == TRANTYPE_DIR_BANKOTHER
					|| tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER
					|| tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			} else if (tranTypeFlag == TRANTYPE_REL_ACCOUNT) {
				BaseHttpEngine.showProgressDialog();
				requestForTransferCommissionChargeRel(ConstantGloble.PB021);
			}
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (tranTypeFlag == TRANTYPE_NOREL_BANKIN) {
			requestGetSecurityFactor(ConstantGloble.PB031);
		} else if (tranTypeFlag == TRANTYPE_NOREL_BANKOTHER) {
			requestGetSecurityFactor(ConstantGloble.PB032);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKIN) {
			requestGetSecurityFactor(ConstantGloble.PB033);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKOTHER) {
			requestGetSecurityFactor(ConstantGloble.PB034);
		} else if (tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {// 跨行实时定向转账
			requestGetSecurityFactor(ConstantGloble.PB118);
		} else if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
			// 跨行实时转账
			requestGetSecurityFactor(ConstantGloble.PB113);
		}
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// combinId = (String) biiResponseBody.getResult();
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		@SuppressWarnings(" unchecked")
		List<Map<String, String>> combinList = (List<Map<String, String>>) result
				.get(Acc.ACC_GETSECURITY_COMBINLIST_RES);
		
		if(combinList != null){
			//判断是否有中银E盾
			Boolean isId = false;
			for(int i=0; i<combinList.size(); i++){
				String tokenId = combinList.get(i).get(Acc.ECURITYID);
				if(Acc.ECURITY_4.equals(tokenId) || Acc.ECURITY_12.equals(tokenId) || Acc.ECURITY_36.equals(tokenId)){
					isId = true;
					Map<String, String> userInputMap = TranDataCenter
							.getInstance().getUserInputMap();
					combineId = "4";
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
					case TRANTYPE_SHISHI_DIR_BANKOTHER:// 跨行实时定向付款
						requestForBocNationalTransferVerify();
						break;
					case TRANTYPE_SHISHI_BANKOTHER:// 跨行实时付款
						Map<String, String> map = TranDataCenter
								.getInstance().getUserInputMap();
						String amount = map.get(Tran.INPUT_TRANSFER_AMOUNT);
						String remark = map.get(Tran.INPUT_TRANSFER_REMARK);
						String isSendSmc = map
								.get(ConstantGloble.IS_SEND_SMC);
						String payeeMobile = map
								.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);
						psnEbpsRealTimePaymentConfirm(isSendSmc,
								payeeMobile, amount, remark, combineId);
						break;
					default:
						break;
					}
					break;
				}
			}
			if(!isId){
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showMessageDialog("您尚未开通CA认证，无法进行此交易", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						BaseDroidApp.getInstanse().dismissMessageDialog();
					}
				});
			}
		}	
//		BaseDroidApp.getInstanse().showSeurityChooseDialog(
//				new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						Map<String, String> userInputMap = TranDataCenter
//								.getInstance().getUserInputMap();
//						combineId = BaseDroidApp.getInstanse()
//								.getSecurityChoosed();
//						userInputMap.put(Tran._COMBINID_REQ, combineId);
//						TranDataCenter.getInstance().setUserInputMap(
//								userInputMap);
//						switch (tranTypeFlag) {
//						case TRANTYPE_NOREL_BANKIN:// 行内
//						case TRANTYPE_DIR_BANKIN:// 行内定向
//							requestForTransBocTransferVerify();
//							break;
//						case TRANTYPE_NOREL_BANKOTHER:// 跨行
//						case TRANTYPE_DIR_BANKOTHER:// 跨行定向
//							requestForBocNationalTransferVerify();
//							break;
//						case TRANTYPE_SHISHI_DIR_BANKOTHER:// 跨行实时定向付款
//							requestForBocNationalTransferVerify();
//							break;
//						case TRANTYPE_SHISHI_BANKOTHER:// 跨行实时付款
//							Map<String, String> map = TranDataCenter
//									.getInstance().getUserInputMap();
//							String amount = map.get(Tran.INPUT_TRANSFER_AMOUNT);
//							String remark = map.get(Tran.INPUT_TRANSFER_REMARK);
//							String isSendSmc = map
//									.get(ConstantGloble.IS_SEND_SMC);
//							String payeeMobile = map
//									.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);
//							psnEbpsRealTimePaymentConfirm(isSendSmc,
//									payeeMobile, amount, remark, combineId);
//							break;
//						default:
//							break;
//						}
//					}
//				});
	}

	/**
	 * TODO 请求国内实时跨行预交易返回
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
		// String executeDate = "";
		// 起始日期
		String startDate = "";
		// 结束日期
		String endDate = "";
		// 周期
		String cycleSelect = "";
		// 执行类型,"0","1","2"
		String executeType = ConstantGloble.PREDATEEXE;
		// 币种
		String currency = ConstantGloble.PRMS_CODE_RMB;

		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		fromAccountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		payeeActno = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		payeeName = (String) accInInfoMap.get(Comm.ACCOUNT_NAME);
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		amount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);
		payeeMobile = userInputMap.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);
		remark = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);

		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		if (tranTypeFlag == TRANTYPE_NOREL_BANKIN) {
			// 行内转账
			biiRequestBody.setMethod(Tran.TRANSBOCTRANSFERVERIFY);
			payeeId = (String) accInInfoMap.get(Tran.TRANS_PAYEETID_RES);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKIN) {
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
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setNoRelBankInCallBackMap(result);
		// 调手续费接口
		if (tranTypeFlag == TRANTYPE_NOREL_BANKIN) {
			requestForTransferCommissionCharge(ConstantGloble.PB031);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKIN) {
			requestForTransferCommissionCharge(ConstantGloble.PB033);
		}
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
		String payeeActno = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		String payeeName = (String) accInInfoMap.get(Comm.ACCOUNT_NAME);
		String bankName = (String) accInInfoMap.get(Tran.TRANS_BANKNAME_RES);
		String toOrgName = (String) accInInfoMap.get(Tran.TRANS_ADDRESS_RES);
		String cnapsCode = (String) accInInfoMap.get(Tran.TRANS_CNAPSCODE_RES);

		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		String amount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);
		String payeeMobile = userInputMap.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);
		String remark = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);

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
		map.put(Tran.TRANS_BOCNATIONAL_CNAPSCODE_REQ, cnapsCode);
		map.put(Tran.TRANS_BOCNATIONAL_REMITTANCEINFO_REQ, "");
		map.put(Tran.TRANS_BOCNATIONAL_AMOUNT_REQ, amount);
		map.put(Tran.TRANS_BOCNATIONAL_PAYEEMOBILE_REQ, payeeMobile);
		map.put(Tran.TRANS_BOCNATIONAL_REMARK_REQ, remark);
		map.put(Tran.TRANS_BOCNATIONAL_CURRENCY_REQ,
				ConstantGloble.PRMS_CODE_RMB);
		map.put(Tran.TRANS_BOCNATIONAL_EXECUTETYPE_REQ,
				ConstantGloble.PREDATEEXE);
		map.put(Tran.TRANS_BOCNATIONAL_EXECUTEDATE_REQ, executeDate);
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
	 * 关联账户转账手续费试算
	 */
	private void requestForTransferCommissionChargeRel(String serviceId) {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		String fromAccountId = (String) accOutInfoMap.get(Tran.ACCOUNTID_RES);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String toAccountId = (String) accInInfoMap.get(Tran.ACCOUNTID_RES);
		String payeeactNo = (String) accInInfoMap.get(Tran.ACCOUNTNUMBER_RES);
		String payeeName = (String) accInInfoMap.get(Tran.ACCOUNTNAME_RES);
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		String currency = userInputMap.get(Tran.INPUT_CURRENCY_CODE);
		String cashremit = userInputMap.get(Tran.INPUT_CASHREMIT_CODE);
		String amount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);
		String memo = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANSFER_COMMISSIONCHARGE_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.SERVICEID_REQ, serviceId);
		map.put(Tran.RELTRANS_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.RELTRANS_TOACCOUNTID_REQ, toAccountId);
		map.put(Tran.RELTRANS_CURRENCY_REQ, currency);
		map.put(Tran.RELTRANS_AMOUNT_REQ, amount);
		map.put(Tran.RELTRANS_CASHREMIT_REQ, cashremit);
		map.put(Tran.RELTRANS_REMARK_REQ, memo);
		map.put(Tran.RELTRANS_PAYEEACTNO_REQ, payeeactNo);
		map.put(Tran.RELTRANS_PAYEENAME_REQ, payeeName);
		map.put(Tran.NOTIFYID, null);

		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestForTransferCommissionChargeRelCallBack");
	}

	/**
	 * 关联账户转账手续费试算返回
	 * 
	 * @param resultObj
	 */
	public void requestForTransferCommissionChargeRelCallBack(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setCommissionChargeMap(result);
		Intent intent = new Intent();
		intent.setClass(PreDateExeActivity1.this, RelConfirmInfoActivity1.class);
		startActivity(intent);

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
		if (tranTypeFlag == TRANTYPE_NOREL_BANKIN) {
			// 行内转账
			toAccountId = (String) accInInfoMap.get(Tran.TRANS_PAYEETID_RES);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKIN) {
			// 行内定向
			toAccountId = (String) accInInfoMap
					.get(Tran.TRAN_DIR_QUERYLIST_PAYEEID_RES);
		}
		String payeeactNo = (String) accInInfoMap.get(Tran.ACCOUNTNUMBER_RES);
		String payeeName = (String) accInInfoMap.get(Tran.ACCOUNTNAME_RES);

		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		String amount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);
		String memo = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANSFER_COMMISSIONCHARGE_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.SERVICEID_REQ, serviceId);
		map.put(Tran.RELTRANS_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.RELTRANS_TOACCOUNTID_REQ, toAccountId);
		map.put(Tran.RELTRANS_CURRENCY_REQ, ConstantGloble.PRMS_CODE_RMB);
		map.put(Tran.RELTRANS_AMOUNT_REQ, amount);
		map.put(Tran.RELTRANS_CASHREMIT_REQ, ConstantGloble.CASHREMIT_00);
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
		BiiHttpEngine.dissMissProgressDialog();
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setCommissionChargeMap(result);

		Intent intent = getIntent();
		intent.setClass(this, NoRelConfirmInfoActivity1.class);
		intent.putExtra(TRANS_TYPE, tranTypeFlag);
		startActivity(intent);

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
			// 跨行定向
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

		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		String amount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);
		String memo = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(Tran.PSNTRANS_GETNATIONAL_TRANSFER_COMMISSIONCHARGE);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.SERVICEID_REQ, serviceId);
		map.put(Tran.RELTRANS_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.RELTRANS_TOACCOUNTID_REQ, toAccountId);
		map.put(Tran.RELTRANS_CURRENCY_REQ, ConstantGloble.PRMS_CODE_RMB);
		map.put(Tran.RELTRANS_AMOUNT_REQ, amount);
		map.put(Tran.RELTRANS_CASHREMIT_REQ, ConstantGloble.CASHREMIT_00);
		map.put(Tran.RELTRANS_REMARK_REQ, memo);
		map.put(Tran.RELTRANS_PAYEEACTNO_REQ, payeeactNo);
		map.put(Tran.RELTRANS_PAYEENAME_REQ, payeeName);
		map.put(Tran.NATIONALADDPAYEE_TOORGNAME_REQ, toOrgName);
		map.put(Tran.TRANS_CNAPSCODE_RES, cnapsCode);

		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestNationalTransferCommissionChargeCallBack");
	}

	public void requestNationalTransferCommissionChargeCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setCommissionChargeMap(result);

		Intent intent = getIntent();
		intent.setClass(this, NoRelBankOtherConfirmInfoActivity1.class);
		intent.putExtra(TRANS_TYPE, tranTypeFlag);
		startActivity(intent);
	}

}
