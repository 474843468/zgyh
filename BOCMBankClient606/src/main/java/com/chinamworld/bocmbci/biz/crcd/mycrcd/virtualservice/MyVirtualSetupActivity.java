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
 * 虚拟卡设定-----填写信息页面
 * 
 * @author huangyuchao
 * 
 */
public class MyVirtualSetupActivity extends CrcdAccBaseActivity {
	private static final String TAG = "MyVirtualSetupActivity";
	private View view;
	static String accountId;
	static String accountNumber;
	static String currencyCode;
	private int accNum = 0;

	TextView finc_accNumber, finc_fenqidate, finc_miaoshus;
	TextView acc_query_transfer_startdate;
	EditText finc_remiannomoney, finc_nextdate, finc_et_pass;

	/** 系统时间 */
	private String currenttime;

	Map<String, Object> rtnMap;

	static String accountName;
	static String startDate;
	static String endDate;
	static String customerId;

	static String validateTime;
	static String currencyDate;
	/** 已累计交易金额 */
	static String atotaLeamt;
	/** 单笔交易限额 */
	static String singleMoney;
	/** 累计交易限额 */
	static String totalMoney;
	static String virtualCardNo;
	static String lastUpdateUser;
	static String lastUpdates;

	Button sureButton;

	TextView crcd_total_money;

	TextView crcd_accountname, finc_startdate, finc_enddate;
	/** 生效日期 */
	private String strstartDate = null;
	/** 失效日期 */
	private String strendDate = null;
	/** 单笔交易限额 */
	private String single = null;
	/** 累计交易限额 */
	private String total = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_virtualcard_setup));
		view = addView(R.layout.crcd_virtualcard_setup);
		single = getIntent().getStringExtra(Crcd.CRCD_SINGLEEMT);
		total = getIntent().getStringExtra(Crcd.CRCD_TOTALEAMT);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);

		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(dateTime)) {
			return;
		}

		init();
	}

	/** 初始化界面 */
	private void init() {

		rtnMap = VirtualBCListActivity.virCardItem;
		accountName = String.valueOf(rtnMap.get(Crcd.CRCD_ACCOUNTNAME_RES));
		startDate = String.valueOf(rtnMap.get(Crcd.CRCD_STARTDATE));
		endDate = String.valueOf(rtnMap.get(Crcd.CRCD_ENDDATE));
		customerId = String.valueOf(rtnMap.get(Crcd.CRCD_CUSTOMERID));
		currencyDate = String.valueOf(rtnMap.get(Crcd.CRCD_CURRENTDATE));
		virtualCardNo = String.valueOf(rtnMap.get(Crcd.CRCD_VIRTUALCARDNO));
		atotaLeamt = String.valueOf(rtnMap.get(Crcd.CRCD_ATOTALEAMT));
		lastUpdateUser = String.valueOf(rtnMap.get(Crcd.CRCD_LASTUPDATEUSER));
		lastUpdates = String.valueOf(rtnMap.get(Crcd.CRCD_LASTUPDATES));

		// StepTitleUtils.getInstance().initTitldStep(
		// this,
		// new String[] {
		// this.getResources().getString(
		// R.string.mycrcd_virtual_write_info),
		// this.getResources().getString(
		// R.string.mycrcd_setup_info),
		// this.getResources().getString(
		// R.string.mycrcd_step_success) });
		// StepTitleUtils.getInstance().setTitleStep(1);

		crcd_accountname = (TextView) view.findViewById(R.id.crcd_accountname);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, crcd_accountname);
		finc_startdate = (TextView) view.findViewById(R.id.finc_startdate);
		finc_enddate = (TextView) view.findViewById(R.id.finc_enddate);

		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_fenqidate = (TextView) view.findViewById(R.id.finc_fenqidate);
		finc_miaoshus = (TextView) view.findViewById(R.id.finc_miaoshus);

		crcd_total_money = (TextView) view.findViewById(R.id.crcd_total_money);
		crcd_total_money.setText(StringUtil.parseStringPattern(atotaLeamt, 2));

		acc_query_transfer_startdate = (TextView) findViewById(R.id.acc_query_transfer_startdate);

		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		acc_query_transfer_startdate.setText(currenttime);
		// 电弧银行密码
		acc_query_transfer_startdate.setOnClickListener(chooseDateClick);

		finc_accNumber.setText(StringUtil.getForSixForString(MyVirtualBCListActivity.accountNumber));
		finc_fenqidate.setText(StringUtil.getForSixForString(VirtualBCListActivity.virtualCardNo));
		finc_miaoshus.setText(MyVirtualBCListActivity.strAccountType);

		crcd_accountname.setText(MyVirtualBCListActivity.accountName);

		strstartDate = QueryDateUtils.getInstance().getCurDate(Long.parseLong(startDate));

		strendDate = QueryDateUtils.getInstance().getCurDate(Long.parseLong(endDate));

		finc_startdate.setText(strstartDate);
		finc_enddate.setText(strendDate);

		finc_remiannomoney = (EditText) view.findViewById(R.id.finc_remiannomoney);
		finc_nextdate = (EditText) view.findViewById(R.id.finc_nextdate);
		finc_remiannomoney.setText(total);
		finc_nextdate.setText(single);
		finc_et_pass = (EditText) view.findViewById(R.id.finc_et_pass);

		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 累计交易限额
				totalMoney = finc_remiannomoney.getText().toString();
				// 单笔交易限额
				singleMoney = finc_nextdate.getText().toString();
				validateTime = acc_query_transfer_startdate.getText().toString();
				// 验证
				RegexpBean reb1 = new RegexpBean(MyVirtualSetupActivity.this
						.getString(R.string.mycrcd_virtualcard_total_money), totalMoney, "transactionAmount");
				RegexpBean reb2 = new RegexpBean(MyVirtualSetupActivity.this
						.getString(R.string.mycrcd_virtualcard_single_money), singleMoney, "transactionAmount");

				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				lists.add(reb2);
				if (RegexpUtils.regexpDate(lists)) {
					if (Double.parseDouble(singleMoney) > Double.parseDouble(totalMoney)) {
						// 单笔交易限额>累计交易限额
						BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_money_error1));
						return;
					}
					// 单笔交易限额
					requestCommConversationId();
					BaseHttpEngine.showProgressDialog();
				}
			}
		});

	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestCommConversationIdCallBack(resultObj);
		// 请求安全因子组合id
		requestGetSecurityFactor(psnVirsericurityId);
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestGetSecurityFactorCallBack(resultObj);

		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 申请虚拟银行卡预交易
				psnCrcdVirtualCardFunctionSetConfirm();
			}
		});
	}

	public void psnCrcdVirtualCardFunctionSetConfirm() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDVIRTUALCARDFUNCTIONSETCONFIRM);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));

		biiRequestBody.setConversationId(commConversationId);// 获取conversationId

		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, MyVirtualBCListActivity.accountId);//
		map.put(Crcd.CRCD_VIRTUALCARDNO, virtualCardNo);
		map.put(Crcd.CRCD_VIRCARDACCOUNTNAME, MyVirtualBCListActivity.accountName);

		// startDate =
		// QueryDateUtils.getInstance().getCurDate(Long.parseLong(startDate));
		map.put(Crcd.CRCD_VIRCARDSTARTDATE, strstartDate);
		// endDate =
		// QueryDateUtils.getInstance().getCurDate(Long.parseLong(endDate));
		map.put(Crcd.CRCD_VIRCARDENDDATE, strendDate);

		map.put(Crcd.CRCD_SINGLEEMT, singleMoney);
		map.put(Crcd.CRCD_TOTALEAMT, totalMoney);

		Map<String, Object> loginMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		customerId = String.valueOf(loginMap.get(Crcd.CRCD_CUSTOMERID));
		map.put(Crcd.CRCD_VIRCARDCUSTOMERID, customerId);

		map.put(Crcd.CRCD_ATOTALEAMT, atotaLeamt);
		map.put(Crcd.CRCD_VIRCARDCURRENCY, MyVirtualBCListActivity.currencyCode);
		map.put(Crcd.CRCD_LASTUPDATEUSER, lastUpdateUser);
		map.put(Crcd.CRCD_LASTUPDATES, lastUpdates);
		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdVirtualCardFunctionSetConfirmCallBack");
	}

	/** 虚拟银行卡交易限额修改预交易---数据 */
	static Map<String, Object> returnList;

	public void psnCrcdVirtualCardFunctionSetConfirmCallBack(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		returnList = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(MyVirtualSetupActivity.this, MyVirtualSetupConfirmActivity.class);
		it.putExtra(Crcd.CRCD_VIRCARDSTARTDATE, strstartDate);
		it.putExtra(Crcd.CRCD_VIRCARDENDDATE, strendDate);
		startActivity(it);
	}

	/** 设置查询日期 */
	OnClickListener chooseDateClick = new OnClickListener() {

		@Override
		public void onClick(final View v) {

			Calendar c = Calendar.getInstance(); // 实例化一个日期与时间操作对象
			QueryDateUtils.checkDate(acc_query_transfer_startdate, c);

			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(MyVirtualSetupActivity.this, new OnDateSetListener() {

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

//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//	}

	@Override
	protected void onDestroy() {
		BaseHttpEngine.dissMissProgressDialog();
		super.onDestroy();
	}

}
