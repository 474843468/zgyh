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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
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
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;

public class PrePeriodExeActivity1 extends TranBaseActivity {
	// private static final String TAG = "SaveWeekExcActivity";

	private View view = null;
	/** 选择日期 */
	private TextView chooseStartDateTv;
	/** 选择日期 */
	private TextView chooseEndDateTv;
	/** 周期 */
	private Spinner weekSpinner;
	/** 上一步 */
	// private Button lastBtn;
	/** 下一步 */
	private Button nextBtn;
	/** 起始日期 */
	private String strStartDate = null;
	/** 结束日期 */
	private String strEndDate = null;
	/** 周期 */
	private String strWeek = null;
	/** 执行次数 */
	private String strExecuteTimes = null;
	/** 当前系统时间一天后 */
	private String oneDateLater;
	/** 当前系统时间七天后 */
	private String sevenDateLater;

	private int startYear, startMonth, startDay;
	private int endYear, endMonth, endDay;

	private String combineId;

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
					intent.setClass(PrePeriodExeActivity1.this,
							TwoDimenTransActivity1.class);
					startActivity(intent);
					finish();
				}
			});
		} else {
			setTitle(this.getString(R.string.tran_my_trans));
		}

		LayoutInflater inflater = LayoutInflater.from(this); // 加载理财产品布局
		view = inflater.inflate(R.layout.dept_save_pre_week, null);
		tabcontent.addView(view);

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

		aquireData();

		init();
	}

	/**
	 * 获取数据
	 */
	private void aquireData() {
		tranTypeFlag = this.getIntent().getIntExtra(TRANS_TYPE, -1);
		dateTime = this.getIntent().getStringExtra(ConstantGloble.DATE_TIEM);
		oneDateLater = QueryDateUtils.getOneDayLater(dateTime);
		sevenDateLater = QueryDateUtils.getSevenDayLater(dateTime);
	}

	/**
	 * 初始化控件
	 */
	private void init() {

		// newTranBtn.setVisibility(View.VISIBLE);

		chooseStartDateTv = (TextView) view
				.findViewById(R.id.dept_choose_start_date_tv);
		startYear = Integer.parseInt(oneDateLater.substring(0, 4));
		startMonth = Integer.parseInt(oneDateLater.substring(5, 7));
		startDay = Integer.parseInt(oneDateLater.substring(8, 10));
		chooseStartDateTv.setText(oneDateLater);
		chooseStartDateTv.setOnClickListener(chooseStartDateClicklistener);

		chooseEndDateTv = (TextView) view
				.findViewById(R.id.dept_choose_end_date_tv);
		endYear = Integer.parseInt(sevenDateLater.substring(0, 4));
		endMonth = Integer.parseInt(sevenDateLater.substring(5, 7));
		endDay = Integer.parseInt(sevenDateLater.substring(8, 10));
		chooseEndDateTv.setText(sevenDateLater);
		chooseEndDateTv.setOnClickListener(chooseEndDateClicklistener);

		weekSpinner = (Spinner) view.findViewById(R.id.dept_week_spinner);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				R.layout.custom_spinner_item, LocalData.weekListTrans);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weekSpinner.setAdapter(adapter1);
		weekSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String week = LocalData.weekListTrans.get(position);
				strWeek = LocalData.FrequencyValue.get(week);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				String week = LocalData.weekListTrans.get(0);
				strWeek = LocalData.FrequencyValue.get(week);
			}
		});

		// lastBtn = (Button) view.findViewById(R.id.btnLast);
		// lastBtn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// finish();
		// }
		// });

		nextBtn = (Button) view.findViewById(R.id.btnNext);
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				strStartDate = chooseStartDateTv.getText().toString().trim();
				strEndDate = chooseEndDateTv.getText().toString().trim();

				if (QueryDateUtils.compareDate(strStartDate, dateTime)) {
					// 起始日期在服务器日期之前
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							BaseDroidApp.getInstanse().getCurrentAct()
									.getString(R.string.choose_day_info));
					return;
				}

				if (!QueryDateUtils.compareStartDate(strStartDate, dateTime)) {
					BaseDroidApp.getInstanse().createDialog(
							null,
							PrePeriodExeActivity1.this.getResources()
									.getString(R.string.choose_start_day_info));
					return;
				}

				if (QueryDateUtils.compareDate(strEndDate, dateTime)) {
					// 结束日期在服务器日期之前
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							BaseDroidApp.getInstanse().getCurrentAct()
									.getString(R.string.choose_end_day_info1));
					return;
				}

				if (!QueryDateUtils.compareEndDate(strEndDate, dateTime)) {
					BaseDroidApp.getInstanse().createDialog(
							null,
							PrePeriodExeActivity1.this.getResources()
									.getString(R.string.choose_end_day_info));
					return;
				}
				boolean flag = QueryDateUtils.compareDate(strStartDate,
						strEndDate);
				if (!flag) {
					BaseDroidApp.getInstanse().createDialog(
							null,
							PrePeriodExeActivity1.this.getResources()
									.getString(R.string.acc_query_errordate));
					return;
				}
				strExecuteTimes = Integer.toString(QueryDateUtils
						.initExecuteTimes(strStartDate, strEndDate, strWeek));
				Map<String, String> userInputMap = TranDataCenter.getInstance()
						.getUserInputMap();
				userInputMap
						.put(Tran.INPUT_PRE_PERIOD_START_DATE, strStartDate);
				userInputMap.put(Tran.INPUT_PRE_PERIOD_END_DATE, strEndDate);
				userInputMap.put(Tran.INPUT_PRE_PERIOD_WEEK, strWeek);
				userInputMap.put(Tran.INPUT_PRE_PERIOD_EXECUTE_TIMES,
						strExecuteTimes);
				if (tranTypeFlag == TRANTYPE_NOREL_BANKIN
						|| tranTypeFlag == TRANTYPE_DIR_BANKIN) {
					BaseHttpEngine.showProgressDialog();
					requestCommConversationId();
				} else if (tranTypeFlag == TRANTYPE_REL_ACCOUNT) {
					BaseHttpEngine.showProgressDialog();
					requestForTransferCommissionChargeRel(ConstantGloble.PB021);
				}
			}
		});

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
		intent.setClass(PrePeriodExeActivity1.this,
				RelConfirmInfoActivity1.class);
		startActivity(intent);

	}

	/**
	 * 选择开始日期监听事件
	 */
	private OnClickListener chooseStartDateClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			String start = chooseStartDateTv.getText().toString();
			String end = chooseEndDateTv.getText().toString();
			setCurDatePickTime(start, end);
			DatePickerDialog startDateDialog = new DatePickerDialog(
					PrePeriodExeActivity1.this, onStartDateSetListener,
					startYear, startMonth - 1, startDay);
			startDateDialog.show();
		}
	};
	/**
	 * 选择结束日期监听事件
	 */
	private OnClickListener chooseEndDateClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			String start = chooseStartDateTv.getText().toString();
			String end = chooseEndDateTv.getText().toString();
			setCurDatePickTime(start, end);
			DatePickerDialog endDateDialog = new DatePickerDialog(
					PrePeriodExeActivity1.this, onEndDateSetListener, endYear,
					endMonth - 1, endDay);
			endDateDialog.show();
		}
	};

	/**
	 * 开始日期控件监听
	 */
	DatePickerDialog.OnDateSetListener onStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
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
			chooseStartDateTv.setText(dateStr);

		}
	};

	/**
	 * 结束日期控件监听
	 */
	DatePickerDialog.OnDateSetListener onEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
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
			chooseEndDateTv.setText(dateStr);
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (tranTypeFlag == TRANTYPE_NOREL_BANKIN) {
			requestGetSecurityFactor(ConstantGloble.PB031);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKIN) {
			requestGetSecurityFactor(ConstantGloble.PB033);
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
			boolean isId = false;
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

					case TRANTYPE_NOREL_BANKOTHER:// 跨行没有预约周期执行
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
//
//						case TRANTYPE_NOREL_BANKOTHER:// 跨行没有预约周期执行
//							break;
//						default:
//							break;
//						}
//					}
//				});
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
		// // 起始日期
		// String startDate = "";
		// // 结束日期
		// String endDate = "";
		// //周期
		// String cycleSelect = "";
		// 执行类型,"0","1","2"
		String executeType = ConstantGloble.PREPERIODEXE;
		// 币种
		String currency = ConstantGloble.PRMS_CODE_RMB;

		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		fromAccountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		payeeActno = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		payeeName = (String) accInInfoMap.get(Comm.ACCOUNT_NAME);
		// payeeMobile = (String) accInInfoMap.get(Tran.TRANS_MOBILE_RES);
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
		map.put(Tran.STARTDATE_REQ, strStartDate);
		// 结束日期
		map.put(Tran.ENDDATE_REQ, strEndDate);
		// 周期
		map.put(Tran.CYCLESELECT_REQ, strWeek);
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

	private void setCurDatePickTime(String strStartDate, String strEndDate) {
		startYear = Integer.parseInt(strStartDate.substring(0, 4));
		startMonth = Integer.parseInt(strStartDate.substring(5, 7));
		startDay = Integer.parseInt(strStartDate.substring(8, 10));
		endYear = Integer.parseInt(strEndDate.substring(0, 4));
		endMonth = Integer.parseInt(strEndDate.substring(5, 7));
		endDay = Integer.parseInt(strEndDate.substring(8, 10));
	}

}
