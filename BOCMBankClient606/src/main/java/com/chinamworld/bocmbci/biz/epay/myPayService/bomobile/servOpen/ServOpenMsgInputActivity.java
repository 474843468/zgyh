package com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.servOpen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.VirtualBCServiceWriteConfirmActivity;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.BomConstants;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 电子支付-开通输入页面
 * 
 * @author Administrator
 *
 */
public class ServOpenMsgInputActivity extends EPayBaseActivity {

	public String TAG = "EPayServOpenMsgInputActivity";

	private View epayServiceOpenMsgInput;

	// 预留信息
	private EditText et_obligateMsg;
	// 系统设置每日最高限额
	private TextView tv_sysEachDayMaxAmount;
	// 客户自设每日最高限额
	private EditText et_custEachDayMaxAmount;
	// 每笔最高限额
	private TextView tv_eachMaxAmount;
	// //验证方式 下拉框
	// private Spinner s_confirm_type;
	private Button bt_next;
	private LinearLayout ll_selected_acclist;

	private Context bomTransContext;

	private String dayMaxQuota;
	private String perMaxQuota;
	private String custMaxQuota;
	private String obligateMsg;

	private String conversationId;

	private PubHttpObserver httpObserver;

	private String combinId;

	// 是否从电子支付直接进来
	private boolean acclistFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		httpObserver = PubHttpObserver.getInstance(this,
				PubConstants.CONTEXT_BOM);
		bomTransContext = TransContext.getBomContext();
		epayServiceOpenMsgInput = LayoutInflater.from(this).inflate(
				R.layout.epay_bom_service_open_message_input, null);
		acclistFlag = getIntent().getBooleanExtra("acclistFlag", false);
		super.setType(0);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_BOANK_OF_MOBILE);
		super.setContentView(epayServiceOpenMsgInput);
		super.onCreate(savedInstanceState);
		// 初始化导航条
		// EpayPubUtil.initStepBar(this, 3, new String[] { "阅读协议", "选择账户",
		// "填写信息" });
		// 初始化当前页

		initCurPage();
	}

	private void initCurPage() {
		et_obligateMsg = (EditText) epayServiceOpenMsgInput
				.findViewById(R.id.et_obligate_msg);
		EditTextUtils.setLengthMatcher(this, et_obligateMsg, 60);
		tv_sysEachDayMaxAmount = (TextView) epayServiceOpenMsgInput
				.findViewById(R.id.tv_sys_eachday_maxamount);
		et_custEachDayMaxAmount = (EditText) epayServiceOpenMsgInput
				.findViewById(R.id.tv_cust_eachday_maxamount);
		tv_eachMaxAmount = (TextView) epayServiceOpenMsgInput
				.findViewById(R.id.tv_each_maxamount);
		// s_confirm_type = (Spinner)findViewById(R.id.s_confirm_type);
		bt_next = (Button) epayServiceOpenMsgInput.findViewById(R.id.bt_next);
		ll_selected_acclist = (LinearLayout) epayServiceOpenMsgInput
				.findViewById(R.id.ll_selected_acclist);
		// 添加提示
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) epayServiceOpenMsgInput.findViewById(R.id.tip_one));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) epayServiceOpenMsgInput.findViewById(R.id.tip_two));
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(
						this,
						(TextView) epayServiceOpenMsgInput
								.findViewById(R.id.tip_three));
		bt_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!checkSubmitData()) {
					return;
				}
				BiiHttpEngine.showProgressDialog();
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}
		});
		transDataDispose();
	}

	/**
	 * 校验提交数据
	 * 
	 * @return
	 */
	protected boolean checkSubmitData() {
		String editText = et_obligateMsg.getText().toString();
		if(!StringUtil.simpleCheckPreHint(editText)){
			StringBuffer sb = new StringBuffer();
			sb.append("预留信息最长60个字符或30个中文字符，不能包含特殊字符（");
			sb.append("[]^$~@#%&<>{}'\" ）和特殊语句。\n");
			String editInfo = sb.toString();
			BaseDroidApp.getInstanse().showMessageDialog(editInfo, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
			return false;
		}
//		editText = editText.toLowerCase();
//		//去掉空格
//		editText = editText.replaceAll(" ", "");
//		//去掉换行符
//		editText = editText.replaceAll("\r|\n", "");
		
//		if(editText.contains("eval(")){
//			int evalLength = editText.indexOf("eval(");
//			int last = editText.indexOf(")", evalLength);
//			if(last>=0){
//				StringBuffer sb = new StringBuffer();
//				sb.append("预留信息最长60个字符或30个中文字符，不能包含特殊字符（");
//				sb.append("[]^$~@#%&<>{}'\" ）和特殊语句。\n");
//				String editInfo = sb.toString();
//				BaseDroidApp.getInstanse().showMessageDialog(editInfo, new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						BaseDroidApp.getInstanse().dismissMessageDialog();
//					}
//				});
//				return false;
//			}
//		}
//		if(editText.contains("onload(")){
//			int onloadLength = editText.indexOf("onload(");
//			int last = editText.indexOf(")", onloadLength);
//			if(last>=0){
//				StringBuffer sb = new StringBuffer();
//				sb.append("预留信息最长60个字符或30个中文字符，不能包含特殊字符（");
//				sb.append("[]^$~@#%&<>{}'\" ）和特殊语句。\n");
//				String editInfo = sb.toString();
//				BaseDroidApp.getInstanse().showMessageDialog(editInfo, new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						BaseDroidApp.getInstanse().dismissMessageDialog();
//					}
//				});
//				return false;
//			}
//		}
//		if(editText.contains("javascript:")||editText.contains("vbscript:")||editText.contains("src='")){
//			StringBuffer sb = new StringBuffer();
//			sb.append("预留信息最长60个字符或30个中文字符，不能包含特殊字符（");
//			sb.append("[]^$~@#%&<>{}'\" ）和特殊语句。\n");
//			String editInfo = sb.toString();
//			BaseDroidApp.getInstanse().showMessageDialog(editInfo, new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					BaseDroidApp.getInstanse().dismissMessageDialog();
//				}
//			});
//			return false;
//		}	
		custMaxQuota = EpayUtil
				.getString(et_custEachDayMaxAmount.getText(), "");
		obligateMsg = EpayUtil.getString(et_obligateMsg.getText(), "");
		RegexpBean obligateMsgBean = new RegexpBean(getString(R.string.set_obligatemessage_no), obligateMsg, "oblimessage");
		ArrayList<RegexpBean> list = new ArrayList<RegexpBean>();
		list.add(obligateMsgBean);

		if (!RegexpUtils.regexpDate(list)) {
			return false;
		}

		// if (StringUtil.isNullOrEmpty(custMaxQuota)) {
		// //BaseDroidApp.getInstanse().showInfoMessageDialog("请输入每日交易限额");
		// BaseDroidApp.getInstanse().showInfoMessageDialog(getText(R.string.set_everyday_limit_no_empty).toString());
		// return false;
		// }
		//
		// if
		// (!Pattern.matches("(?!^0$)(?!^[0]{2,})(?!^0[1-9]+)(?!^0\\.[0]*$)^\\d{1,11}(\\.\\d{1,2})?$",
		// custMaxQuota)) {
		// //
		// BaseDroidApp.getInstanse().showInfoMessageDialog("最多13位数字且不能为0（小数点前最多11位数字，小数点后最多2位数字）");
		// BaseDroidApp.getInstanse().showInfoMessageDialog(getText(R.string.trade_amount_maximum_prompt).toString());
		// return false;
		// }

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean transAmountReg = new RegexpBean("自设每日最高限额", custMaxQuota,
				"tranAmount");
		lists.add(transAmountReg);
		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}

		if (!StringUtil.isNullOrEmpty(dayMaxQuota)) {
			double d_curQuota = Double.valueOf(custMaxQuota);
			double d_sysQuota = Double.valueOf(dayMaxQuota);
			if (d_curQuota > d_sysQuota) {
				// BaseDroidApp.getInstanse().showInfoMessageDialog("每日交易限额不能超过系统最高限额！");
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						"自设每日最高限额不能超过系统每日最高限额");
				return false;
			}
		}
		return true;
	}

	/**
	 * 初始化页面显示数据
	 */
	private void initDisplayData() {

		if (acclistFlag) {
			// 从电子支付多选进来
			List<Object> list = bomTransContext
					.getList(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST);
			for (int i = 0; i < list.size(); i++) {
				Map<Object, Object> map = EpayUtil.getMap(list.get(i));
				View view = LayoutInflater.from(this).inflate(
						R.layout.epay_bom_selected_acc_list_item, null);
				TextView tv_acc_number = (TextView) view
						.findViewById(R.id.item_acc_number);
				String acc_number = EpayUtil
						.getString(
								map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER),
								"");
				tv_acc_number
						.setText(StringUtil.getForSixForString(acc_number));
				ll_selected_acclist.addView(view);
			}
		} else {
			// 从银联跨行无卡支付进来
			String virtualNo = String
					.valueOf(VirtualBCServiceWriteConfirmActivity.vcardinfo
							.get(Crcd.CRCD_VIRTUALCARDNO));
			View view = LayoutInflater.from(this).inflate(
					R.layout.epay_bom_selected_acc_list_item, null);
			TextView tv_acc_number = (TextView) view
					.findViewById(R.id.item_acc_number);
			tv_acc_number.setText(StringUtil.getForSixForString(virtualNo));
			ll_selected_acclist.addView(view);
		}

		tv_sysEachDayMaxAmount.setText(StringUtil.parseStringPattern(
				dayMaxQuota, 2));
		tv_eachMaxAmount.setText(StringUtil.parseStringPattern(perMaxQuota, 2));
		et_obligateMsg.setText(obligateMsg);
		BiiHttpEngine.dissMissProgressDialog();
	}

	/**
	 * 查询系统预设限额信息
	 */
	public void queryMaxQuotaCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		Map<Object, Object> resultMap = EpayUtil.getMap(result);
		dayMaxQuota = EpayUtil.getString(resultMap
				.get(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_DAY_MAX), "");
		perMaxQuota = EpayUtil.getString(resultMap
				.get(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_PER_MAX), "");
		obligateMsg = EpayUtil.getLoginInfo("loginHint");
		bomTransContext.setData(
				BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_DAY_MAX, dayMaxQuota);
		bomTransContext.setData(
				BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_PER_MAX, perMaxQuota);
		bomTransContext.setData(
				BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_OBLIGATE_MSG,
				obligateMsg);
		// httpObserver.req_queryCustMaxQuota("queryCustMaxQuotaCallback");

		initDisplayData();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		conversationId = EpayUtil.getString(BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID), "");
		httpObserver.setConversationId(conversationId);
		bomTransContext.setData(PubConstants.PUB_FIELD_CONVERSATION_ID,
				conversationId);
		requestGetSecurityFactor("PB200C1");
	}

	/**
	 * 请求安全因子回调方法
	 */
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		final BaseDroidApp baseDroidApp = BaseDroidApp.getInstanse();
		baseDroidApp.showSeurityChooseDialog(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String, String> params = new HashMap<String, String>();
				combinId = BaseDroidApp.getInstanse().getSecurityChoosed();
				params.put(BomConstants.METHOD_OPEN_SERVICE_PRE_FIELD_QUOTA,
						custMaxQuota);
				params.put(BomConstants.METHOD_OPEN_SERVICE_PRE_FIELD_COMBINID,
						combinId);
				httpObserver.req_openServicePre(params,
						"openServicePreCallback");
			}
		});
		// securityList = BaseDroidApp.getInstanse().getSecurityIdList();
		// securityNameList = BaseDroidApp.getInstanse().getSecurityNameList();
		// ArrayAdapter adapter = new ArrayAdapter(this,
		// android.R.layout.simple_spinner_item, securityNameList);
		// s_confirm_type.setAdapter(adapter);
		// s_confirm_type.setOnItemSelectedListener(new OnItemSelectedListener()
		// {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view,
		// int position, long id) {
		// combinId = securityList.get(position);
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent) {
		// combinId = securityList.get(0);
		// }
		// });
	}

	/**
	 * 开通预交易
	 * 
	 * @param resultObj
	 */
	public void openServicePreCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		Map<Object, Object> resultMap = EpayUtil.getMap(result);

		// TODO 安全因子
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.ISFOREX_PRERESULT_KEY, resultMap);
		// 获取安全验证工具
		List<Object> factorList = EpayUtil.getFactorList(resultMap);

		bomTransContext.setData(
				PubConstants.METHOD_QUERY_CUST_MAX_QUOTA_FIELD_AMOUNT,
				custMaxQuota);
		bomTransContext.setData(
				BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_OBLIGATE_MSG,
				obligateMsg);
		bomTransContext.setData(PubConstants.PUB_FIELD_CONVERSATION_ID,
				conversationId);
		bomTransContext.setData(PubConstants.PUB_FIELD_FACTORLIST, factorList);
		BiiHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(this, ServOpenConfirmActivity.class);
		intent.putExtra("acclistFlag", acclistFlag);
		startActivityForResult(intent, 0);
	}

	/**
	 * 交易数据处理
	 */
	private void transDataDispose() {
		BiiHttpEngine.showProgressDialogCanGoBack();
		// boolean queryMaxQuotaIsCall = bomTransContext
		// .getBoolean(BomConstants.METHOD_QUERY_MAX_QUOTA_IS_CALL);
		// boolean queryCustMaxQuotaIsCall = bomTransContext
		// .getBoolean(PubConstants.METHOD_QUERY_CUST_MAX_QUOTA_IS_CALL);
		// if (queryMaxQuotaIsCall) {
		// dayMaxQuota = bomTransContext.getString(
		// BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_DAY_MAX, "");
		// perMaxQuota = bomTransContext.getString(
		// BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_PER_MAX, "");
		// if (queryCustMaxQuotaIsCall) {
		// custMaxQuota = bomTransContext.getString(
		// PubConstants.METHOD_QUERY_CUST_MAX_QUOTA_FIELD_AMOUNT,
		// "");
		// initDisplayData();
		// } else {
		// httpObserver.req_queryCustMaxQuota("queryMaxQuotaCallback");
		// }
		// } else {
		httpObserver.req_queryMaxQuota("PB200", "queryMaxQuotaCallback");
		// }
	}
}
