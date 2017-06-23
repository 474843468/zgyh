package com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdAccBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 虚拟银行卡申请填写信息
 * 
 * @author huangyuchao
 * 
 */
public class VirtualBCServiceWriteActivity extends CrcdAccBaseActivity {

	private View view = null;

	Button lastButton, sureButton;
	static String accountId;
	static String accountNumber;
	static String currencyCode;
	private int accNum = 0;

	TextView finc_accNumber, finc_fenqidate, finc_miaoshus;
	TextView acc_query_transfer_startdate;
	/** 累计交易限额，单笔交易限额 */
	EditText finc_remiannomoney, finc_nextdate;

	/** 系统时间 */
	private String currenttime;

	Map<String, Object> rtnMap;

	static String accountName;
	/** 生效日期 */
	static String startDate;
	/** 失效日期 */
	static String endDate;
	static String customerId;

	static String validateTime;
	/** 系统当前日期 */
	static String currencyDate;
	/** 单笔交易限额 */
	static String singleMoney;
	/** 累计交易限额 */
	static String totalMoney;

	TextView finc_startdate, finc_enddate;

	TextView crcd_accountname;
	/** 单笔交易限额最大值 */
	String maxSingleMoney;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_virtual_shenqing));
		if (view == null) {
			view = addView(R.layout.crcd_mycard_service_write);
		}
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		init();
	}

	/** 初始化界面 */
	private void init() {
		rtnMap = VirtualBCServiceReadActivity.returnMap;

		accountName = String.valueOf(rtnMap.get(Crcd.CRCD_ACCOUNTNAME_RES));
		startDate = String.valueOf(rtnMap.get(Crcd.CRCD_STARTDATE));
		endDate = String.valueOf(rtnMap.get(Crcd.CRCD_ENDDATE));
		customerId = String.valueOf(rtnMap.get(Crcd.CRCD_CUSTOMERID));

		currencyDate = String.valueOf(rtnMap.get(Crcd.CRCD_CURRENTDATE));
		// 挡板
		// currencyDate = String.valueOf(rtnMap.get(Crcd.CRCD_CURRENTDATES));

		maxSingleMoney = String.valueOf(rtnMap.get(Crcd.CRCD_MAXSINGLEAMT));
		if (StringUtil.isNullOrEmptyCaseNullString(maxSingleMoney)) {
			// 当maxSingleMoney值为空时赋值-1
			maxSingleMoney = "-1";
		}
		// StepTitleUtils.getInstance().initTitldStep(
		// this,
		// new String[] {
		// this.getResources().getString(
		// R.string.mycrcd_virtual_write_info),
		// this.getResources().getString(
		// R.string.mycrcd_setup_info),
		// this.getResources().getString(
		// R.string.mycrcd_setup_success) });
		// StepTitleUtils.getInstance().setTitleStep(1);

		finc_startdate = (TextView) view.findViewById(R.id.finc_startdate);
		finc_enddate = (TextView) view.findViewById(R.id.finc_enddate);
		finc_startdate.setText(startDate);
		finc_enddate.setText(endDate);

		finc_startdate.setOnClickListener(choosestartDateClick);
		finc_enddate.setOnClickListener(chooseendDateClick);

		crcd_accountname = (TextView) view.findViewById(R.id.crcd_accountname);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, crcd_accountname);
		crcd_accountname.setText(accountName);

		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_fenqidate = (TextView) view.findViewById(R.id.finc_fenqidate);
		finc_miaoshus = (TextView) view.findViewById(R.id.finc_miaoshus);

		acc_query_transfer_startdate = (TextView) findViewById(R.id.acc_query_transfer_startdate);

		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		acc_query_transfer_startdate.setText(currenttime);

		// acc_query_transfer_startdate.setOnClickListener(chooseDateClick);

		finc_accNumber.setText(StringUtil.getForSixForString(VirtualBCServiceListActivity.accountNumber));
		finc_fenqidate.setText("");
		finc_miaoshus.setText(VirtualBCServiceListActivity.strAccountType);

		finc_remiannomoney = (EditText) view.findViewById(R.id.finc_remiannomoney);
		finc_nextdate = (EditText) view.findViewById(R.id.finc_nextdate);

		lastButton = (Button) view.findViewById(R.id.lastButton);
		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				totalMoney = finc_remiannomoney.getText().toString();
				singleMoney = finc_nextdate.getText().toString();
				validateTime = acc_query_transfer_startdate.getText().toString();
				startDate = finc_startdate.getText().toString();
				endDate = finc_enddate.getText().toString();

				LogGloble.v("crcd", startDate);
				LogGloble.v("crcd", endDate);
				LogGloble.v("crcd", currencyDate);

				if (QueryDateUtils.compareDate(currencyDate, startDate)) {
					// 生效日期不能早于系统当前日期
				} else {
					BaseDroidApp.getInstanse()
							.showInfoMessageDialog(getString(R.string.mycrcd_shengxiao_zaoyu_current));
					return;
				}
				boolean endBoolean = QueryDateUtils.getInstance().compareDate(currencyDate, endDate);
				// 失效日期不能早于系统当前日期
				if (endBoolean) {

				} else {
					BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_shixiao_zaoyu_current));
					return;
				}

				boolean startAndEnd = QueryDateUtils.getInstance().compareDate(startDate, endDate);
				// 失效日期不能早于生效日期
				if (startAndEnd) {

				} else {
					BaseDroidApp.getInstanse()
							.showInfoMessageDialog(getString(R.string.mycrcd_shengxiao_zaoyu_shixiao));
					return;
				}

				// 验证
				RegexpBean reb1 = new RegexpBean(VirtualBCServiceWriteActivity.this
						.getString(R.string.mycrcd_virtualcard_total_money), totalMoney, "transactionAmount");
				RegexpBean reb2 = new RegexpBean(VirtualBCServiceWriteActivity.this
						.getString(R.string.mycrcd_virtualcard_single_money), singleMoney, "transactionAmount");

				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				lists.add(reb2);
				if (RegexpUtils.regexpDate(lists)) {
					if (!StringUtil.isNullOrEmpty(singleMoney)) {
						// 单笔交易限额>累计交易限额
						if (Double.parseDouble(singleMoney) > Double.parseDouble(totalMoney)) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									getString(R.string.mycrcd_total_money_error1));
							return;
						}
						// 单笔交易限额>单笔交易限额最大值
						if (Double.parseDouble(singleMoney) > Double.parseDouble(maxSingleMoney)) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									getString(R.string.mycrcd_singlemoney_above_max));
							return;
						} else {
							requestCommConversationId();
							BaseHttpEngine.showProgressDialog();
						}
					} else {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getString(R.string.mycrcd_singlemoney_above_max));
						return;
					}

				}
			}

		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 请求安全因子组合id
		requestGetSecurityFactor(psnVirsericurityId);
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestGetSecurityFactorCallBack(resultObj);
		
		
		ArrayList<String> securityIdList = BaseDroidApp.getInstanse().getSecurityIdList();
		ArrayList<String> securityNameList = BaseDroidApp.getInstanse().getSecurityNameList();
		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {						
				// 申请虚拟卡预交易
				psnCrcdVirtualCardApplyConfirm();
			}
		}, securityIdList, securityNameList);

//		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// 申请虚拟卡预交易
//				psnCrcdVirtualCardApplyConfirm();
//			}
//		});
	}

	public void psnCrcdVirtualCardApplyConfirm() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDVIRTUALCARDAPPLYCONFIRM);

		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);// 获取conversationId

		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, VirtualBCServiceListActivity.accountId);//
		map.put(Crcd.CRCD_VIRCARDACCOUNTNAME, VirtualBCServiceListActivity.accountName);
		map.put(Crcd.CRCD_VIRCARDSTARTDATE, startDate);
		map.put(Crcd.CRCD_VIRCARDENDDATE, endDate);
		map.put(Crcd.CRCD_VIRCARDCURRENTDATE, currencyDate);

		map.put(Crcd.CRCD_SINGLEEMT, singleMoney);
		map.put(Crcd.CRCD_TOTALEAMT, totalMoney);
		map.put(Crcd.CRCD_VIRCARDCUSTOMERID, customerId);
		map.put(Crcd.CRCD_VIRCARDCURRENCY, VirtualBCServiceListActivity.currencyCode);
		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdVirtualCardApplyConfirmCallBack");
	}

	static Map<String, Object> returnList;

	public void psnCrcdVirtualCardApplyConfirmCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		returnList = (Map<String, Object>) body.getResult();

		Intent it = new Intent(VirtualBCServiceWriteActivity.this, VirtualBCServiceWriteConfirmActivity.class);
		startActivity(it);
	}

	/** 设置查询日期 */
	OnClickListener choosestartDateClick = new OnClickListener() {

		@Override
		public void onClick(final View v) {

			String[] dateStr = startDate.split("/");

			// int year = Integer.valueOf(dateStr[0]);
			// int month = Integer.valueOf(dateStr[1]);
			// int day = Integer.valueOf(dateStr[2]);

			Calendar c = Calendar.getInstance(); // 实例化一个日期与时间操作对象
			QueryDateUtils.checkDate(finc_startdate, c);
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(VirtualBCServiceWriteActivity.this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					StringBuilder date = new StringBuilder();
					date.append(String.valueOf(year));
					date.append("/");
					int month = monthOfYear + 1;
					date.append(((month < 10) ? ("0" + month) : (month + "")));
					date.append("/");
					date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth) : (dayOfMonth + "")));
					// 为日期赋值
					((TextView) v).setText(String.valueOf(date));
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
		}
	};

	/** 设置查询日期 */
	OnClickListener chooseendDateClick = new OnClickListener() {

		@Override
		public void onClick(final View v) {

			String[] dateStr = endDate.split("/");

			// int year = Integer.valueOf(dateStr[0]);
			// int month = Integer.valueOf(dateStr[1]);
			// int day = Integer.valueOf(dateStr[2]);

			Calendar c = Calendar.getInstance(); // 实例化一个日期与时间操作对象
			QueryDateUtils.checkDate(finc_enddate, c);

			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(VirtualBCServiceWriteActivity.this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					StringBuilder date = new StringBuilder();
					date.append(String.valueOf(year));
					date.append("/");
					int month = monthOfYear + 1;
					date.append(((month < 10) ? ("0" + month) : (month + "")));
					date.append("/");
					date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth) : (dayOfMonth + "")));
					// 为日期赋值
					((TextView) v).setText(String.valueOf(date));
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
		}
	};

	@Override
	protected void onDestroy() {
		BaseHttpEngine.dissMissProgressDialog();
		super.onDestroy();
	}
}
