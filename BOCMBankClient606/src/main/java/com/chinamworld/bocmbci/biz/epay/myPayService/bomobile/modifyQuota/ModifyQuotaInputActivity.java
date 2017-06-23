package com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.modifyQuota;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.BomConstants;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 电子支付-限额输入页
 * 
 */
public class ModifyQuotaInputActivity extends EPayBaseActivity {

	private String tag = "EPayModifyQuotaActivity";

	private View epayModifyQuota;

	private TextView tv_dayMaxQuota;
	private TextView tv_perMaxQuota;
	private TextView tv_curCustMaxQuota;
	private EditText et_custMaxQuota;

	private Button bt_next;

	private String dayMaxQuota;
	private String perMaxQuota;
	private String custMaxQuota;

	private String combinId;

	private Context bomTransContext;
	private PubHttpObserver httpObserver;

	private String conversationId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		bomTransContext = TransContext.getBomContext();
		httpObserver = PubHttpObserver.getInstance(this, PubConstants.CONTEXT_BOM);
		epayModifyQuota = LayoutInflater.from(this).inflate(R.layout.epay_bom_modify_quota_input, null);

		super.setType(0);
		super.setShowBackBtn(false);
		super.setTitleName(PubConstants.TITLE_BOANK_OF_MOBILE);
		super.setContentView(epayModifyQuota);
		super.onCreate(savedInstanceState);
		initTitleRightButton("关闭", rBtncloseListener);
		setLeftButtonPopupGone();
		hideFoot();
		// 初始化导航条
		// EpayPubUtil.initStepBar(this, 1, new String[] { "修改限额", "确认信息",
		// "修改成功" });
		// 初始化当前页
		initCurPage();
	}

	private void initCurPage() {
		tv_dayMaxQuota = (TextView) epayModifyQuota.findViewById(R.id.tv_day_max_quota);
		tv_perMaxQuota = (TextView) epayModifyQuota.findViewById(R.id.tv_per_max_quota);
		tv_curCustMaxQuota = (TextView) epayModifyQuota.findViewById(R.id.tv_cur_cust_max_quota);
		et_custMaxQuota = (EditText) epayModifyQuota.findViewById(R.id.et_cust_max_quota);

		bt_next = (Button) epayModifyQuota.findViewById(R.id.bt_next);
		bt_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!checkSubmitData()) {
					return;
				}
				BiiHttpEngine.showProgressDialog();
				httpObserver.req_getConversationId("requestCommConversationIdCallBack");
			}
		});
		transDataDispose();
	}

	/**
	 * 校验提交参数
	 * 
	 * @return
	 */
	protected boolean checkSubmitData() {
		custMaxQuota = EpayUtil.getString(et_custMaxQuota.getText(), "");

//		if (StringUtil.isNullOrEmpty(custMaxQuota)) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getText(R.string.set_everyday_limit_no_empty).toString());
//			return false;
//		}
//
//		if (!Pattern.matches("(?!^0$)(?!^[0]{2,})(?!^0[1-9]+)(?!^0\\.[0]*$)^\\d{1,11}(\\.\\d{1,2})?$", custMaxQuota)) {
////			BaseDroidApp.getInstanse().showInfoMessageDialog("最多13位数字且不能为0（小数点前最多11位数字，小数点后最多2位数字）");
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getText(R.string.trade_amount_maximum_prompt).toString());
//			return false;
//		}

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
				BaseDroidApp.getInstanse().showInfoMessageDialog("自设每日最高限额不能超过系统每日最高限额");
				return false;
			}
		}
		return true;
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		conversationId = EpayUtil.getString(BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID), "");
		httpObserver.setConversationId(conversationId);
		bomTransContext.setData(PubConstants.PUB_FIELD_CONVERSATION_ID, conversationId);
		requestGetSecurityFactor("PB200C1");
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		final BaseDroidApp baseDroidApp = BaseDroidApp.getInstanse();
		baseDroidApp.showSeurityChooseDialog(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String, String> params = new HashMap<String, String>();
				combinId = baseDroidApp.getSecurityChoosed();
				params.put(BomConstants.METHOD_SET_MAX_QUOTA_PRE_FIELD_COMBIN_ID, combinId);
				params.put(BomConstants.METHOD_SET_MAX_QUOTA_PRE_FIELD_QUOTA, custMaxQuota);
				params.put(PubConstants.PUB_FIELD_SERVICE_ID, "PB200C1");
				httpObserver.req_setBomPaymentQuotaPre(params, "setPaymentQuotaPreCallback");
			}
		});
	}

	/**
	 * 设置支付限额预交易
	 * 
	 * @param resultObj
	 */
	public void setPaymentQuotaPreCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Object result = httpObserver.getResult(resultObj);
		Map<Object, Object> resultMap = EpayUtil.getMap(result);
		
		// TODO 安全因子
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_PRERESULT_KEY, resultMap);
		
		List<Object> factorList = EpayUtil.getFactorList(resultMap);
		bomTransContext.setData(PubConstants.PUB_FIELD_FACTORLIST, factorList);
		Intent intent = new Intent(this, ModifyQuotaConfirmActivity.class);
		intent.putExtra(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_DAY_MAX, dayMaxQuota);
		intent.putExtra(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_PER_MAX, perMaxQuota);
		intent.putExtra(BomConstants.METHOD_SET_MAX_QUOTA_PRE_FIELD_QUOTA, custMaxQuota);

		startActivityForResult(intent, 0);
	}

	/**
	 * 获取交易数据
	 */
	private void transDataDispose() {
		BiiHttpEngine.showProgressDialogCanGoBack();
		httpObserver.req_queryMaxQuota("PB200", "queryMaxQuotaCallback");
	}

	/**
	 * 查询系统限额回调方法
	 * 
	 * @param resultObj
	 */
	public void queryMaxQuotaCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		Map<Object, Object> resultMap = EpayUtil.getMap(result);

		dayMaxQuota = EpayUtil.getString(resultMap.get(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_DAY_MAX), "");
		perMaxQuota = EpayUtil.getString(resultMap.get(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_PER_MAX), "");
		
		httpObserver.req_getConversationId("getConversationIdCallBack");
	}
	
	public void getConversationIdCallBack(Object resultObj) {
		String conversationId = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		httpObserver.setConversationId(conversationId);
		httpObserver.req_queryCustMaxQuota("queryCustMaxQuotaCallback");
	}

	/**
	 * 查询用户设定的交易限额
	 */
	public void queryCustMaxQuotaCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Object result = httpObserver.getResult(resultObj);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) result;
		for (Map<String, Object> tempMap : resultList) {
			String serviceId = EpayUtil.getString(tempMap.get(PubConstants.PUB_FIELD_SERVICE_ID), "");
			if ("PB200".equals(serviceId)) {
				custMaxQuota = EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_CUST_MAX_QUOTA_FIELD_AMOUNT), "");
				break;
			}
		}

		bomTransContext.setData(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_DAY_MAX, dayMaxQuota);
		bomTransContext.setData(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_PER_MAX, perMaxQuota);
		bomTransContext.setData(PubConstants.METHOD_QUERY_CUST_MAX_QUOTA_FIELD_AMOUNT, custMaxQuota);

		initDisplay();
	}

	private void initDisplay() {
		tv_dayMaxQuota.setText(StringUtil.parseStringPattern(dayMaxQuota, 2));
		tv_perMaxQuota.setText(StringUtil.parseStringPattern(perMaxQuota, 2));
		tv_curCustMaxQuota.setText(StringUtil.parseStringPattern(custMaxQuota, 2));
	}

	@Override
	public void finish() {
		setResult(RESET_DATA);
		super.finish();
		overridePendingTransition(R.anim.no_animation, R.anim.n_pop_exit_bottom_down);
	}
}
