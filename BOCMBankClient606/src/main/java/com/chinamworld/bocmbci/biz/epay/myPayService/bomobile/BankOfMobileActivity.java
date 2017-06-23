package com.chinamworld.bocmbci.biz.epay.myPayService.bomobile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.BomConstants;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.adapter.DredgedAccountListAdapter;
import com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.modifyQuota.ModifyQuotaInputActivity;
import com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.setPaymentAcc.SetPaymentAccSelectActivity;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 电子支付-手机银行支付页
 * 
 */
public class BankOfMobileActivity extends EPayBaseActivity {

	private View epay_bomobile;
	private View list_footer_view;

	private RelativeLayout rl_setting;
	private RelativeLayout rl_msg;

	private TextView tv_obligate_msg;
	private TextView tv_cust_max_quota;
	private TextView tv_day_max_quota;
	private TextView tv_per_max_quota;
	private TextView tv_currency;

	private ImageButton ibt_setting;
	private Button bt_title_right;

	private ListView lv_dredged_card_list;

	private DredgedAccountListAdapter dredgedAccountListAdapter;

	private Context bomTransContext;
	private PubHttpObserver httpObserver;
	// 每日最高限额
	private String dayMaxQuota;
	// 每笔交易最高限额
	private String perMaxQuota;
	// 用户自设限额
	private String custMaxQuota;
	// 预留信息
	private String obligateMsg;

	private String curDleAccountId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		bomTransContext = TransContext.getBomContext();
		httpObserver = PubHttpObserver.getInstance(this, PubConstants.CONTEXT_BOM);
		epay_bomobile = LayoutInflater.from(this).inflate(R.layout.epay_bank_of_mobile, null);

		super.setType(0);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_BOANK_OF_MOBILE);
		super.setContentView(epay_bomobile);
		super.onCreate(savedInstanceState);
		bt_title_right = super.initTitleRightButton("取消支付账户", new RightButtonListener());
		bt_title_right.setVisibility(View.GONE);
		slidingBody.setPadding(0, 0, 0, this.getResources().getDimensionPixelSize(R.dimen.common_bottom_padding_new));
		// 初始化当前页面内容
		initCurPage();
	}

	private View.OnClickListener closeServiceListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().showErrorDialog(null, "您确认要关闭电子支付服务吗？\n关闭该服务将同时关闭所有账户的电子支付功能",
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dissmissFootChooseDialog();
							BaseDroidApp.getInstanse().dismissErrorDialog();
							switch (PublicTools.getInt(v.getTag(), 0)) {
							case CustomDialog.TAG_SURE:
								httpObserver.req_getConversationId("getCloseConversationIdCallback");
								break;
							}
						}
					});
		}
	};

	public void getCloseConversationIdCallback(Object resultObj) {
		String conversationId = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		httpObserver.setConversationId(conversationId);
		httpObserver.req_getToken("getTokenCallback");
	}

	public void getTokenCallback(Object resultObj) {
		String token = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		httpObserver.req_closePaymentService(token, "closePaymentServiceCallback");
	}

	public void closePaymentServiceCallback(Object resultObj) {
		BaseDroidApp.getInstanse().showMessageDialog("电子支付服务关闭成功", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissErrorDialog();
				TransContext.clearData(PubConstants.CONTEXT_BOM);
				BankOfMobileActivity.this.finish();
			}
		});
	}

	private View.OnClickListener modifyQuotaListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dissmissFootChooseDialog();
			Intent intent = new Intent(BankOfMobileActivity.this, ModifyQuotaInputActivity.class);
			TransContext.getBomContext().setRightButtonClick(false);
			startActivityForResult(intent, 0);
			overridePendingTransition(R.anim.n_pop_enter_bottom_up, R.anim.no_animation);
		}
	};
	private List<Object> accountList;

	/**
	 * 初始化当前页面内容
	 */
	private void initCurPage() {
		rl_setting = (RelativeLayout) epay_bomobile.findViewById(R.id.rl_setting);
		ibt_setting = (ImageButton) epay_bomobile.findViewById(R.id.ibt_setting);

		tv_obligate_msg = (TextView) epay_bomobile.findViewById(R.id.tv_obligate_msg);
		tv_cust_max_quota = (TextView) epay_bomobile.findViewById(R.id.tv_cust_max_quota);
		tv_day_max_quota = (TextView) epay_bomobile.findViewById(R.id.tv_day_max_quota);
		tv_per_max_quota = (TextView) epay_bomobile.findViewById(R.id.tv_per_max_quota);
		tv_currency = (TextView) epay_bomobile.findViewById(R.id.tv_currency);
		View.OnClickListener settingListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				View menuView = new CustomDialog(BankOfMobileActivity.this).initEpayModifyDialog(closeServiceListener,
						modifyQuotaListener, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse().dissmissFootChooseDialog();
							}
						});
				BaseDroidApp.getInstanse().showFootChooseDialog(menuView);
			}
		};

		ibt_setting.setOnClickListener(settingListener);
//		rl_setting.setOnClickListener(settingListener);

		lv_dredged_card_list = (ListView) epay_bomobile.findViewById(R.id.lv_dredged_acc_list);

		transDataDispose();
	}

	/**
	 * 查询用户设定限额
	 * 
	 * @param resultObj
	 */
	public void queryMaxQuotaCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);

		Map<Object, Object> resultMap = EpayUtil.getMap(result);
		// 系统 每日限额
		dayMaxQuota = EpayUtil.getString(resultMap.get(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_DAY_MAX), "");
		// 系统每笔限额
		perMaxQuota = EpayUtil.getString(resultMap.get(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_PER_MAX), "");
		// 保存交易数据到临时交易上下文
		// bomTransContext.setData(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_DAY_MAX,
		// dayMaxQuota);
		// bomTransContext.setData(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_PER_MAX,
		// perMaxQuota);
		// bomTransContext.setData(BomConstants.METHOD_QUERY_MAX_QUOTA_IS_CALL,
		// true);
		requestCommConversationId();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		String conversationId = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		httpObserver.setConversationId(conversationId);
		bomTransContext.setData(PubConstants.PUB_FIELD_CONVERSATION_ID, conversationId);
		// 查询用户设定限额
		httpObserver.req_queryCustMaxQuota("queryCustMaxQuotaCallback");
	}

	/**
	 * 查询用户设定的交易限额
	 */
	@SuppressWarnings("unchecked")
	public void queryCustMaxQuotaCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) result;
		custMaxQuota = null;
		for (Map<String, Object> tempMap : resultList) {
			String serviceId = EpayUtil.getString(tempMap.get(PubConstants.PUB_FIELD_SERVICE_ID), "");
			if ("PB200".equals(serviceId)) {
				custMaxQuota = EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_CUST_MAX_QUOTA_FIELD_AMOUNT),
						"");
				break;
			}
		}
		bomTransContext.setData(PubConstants.METHOD_QUERY_CUST_MAX_QUOTA_FIELD_AMOUNT, custMaxQuota);
		// 查询已开通支付账户列表
		httpObserver.req_queryDredgedAccountList("queryDredgedAccountListCallback");
	}

	/**
	 * 查询已开通账户列表
	 */
	public void queryDredgedAccountListCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		List<Object> dredgedAccList = EpayUtil.getList(result);
		bomTransContext.setData(PubConstants.CONTEXT_FIELD_DREDGED_LIST, dredgedAccList);
		initDisplay();
		// 是否显示取消支付账户按钮
		if(!StringUtil.isNullOrEmpty(dredgedAccList) && dredgedAccList.size() > 0){
		    bt_title_right.setVisibility(View.VISIBLE);
		}else {
			bt_title_right.setVisibility(View.GONE);	
		}
	}

	/**
	 * 设置listView
	 */
	private void activateListView() {
		dredgedAccountListAdapter = new DredgedAccountListAdapter(this);
		addListFooterView();
		lv_dredged_card_list.setAdapter(dredgedAccountListAdapter);
		dredgedAccountListAdapter.setDelItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BiiHttpEngine.showProgressDialog();
				Map<Object, Object> tempMap = EpayUtil.getMap(bomTransContext.getList(
						PubConstants.CONTEXT_FIELD_DREDGED_LIST).get(position));
				curDleAccountId = EpayUtil.getString(
						tempMap.get(BomConstants.METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_ID), "");
				// String curAccountNum =
				// EpayUtil.getString(tempMap.get(BomConstants.METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_NUMBER),
				// "");
				BiiHttpEngine.dissMissProgressDialog();
				String msg = null;
				if (1 == bomTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).size()) {
					msg = "您确定取消该账户的电子支付服务吗？\n温馨提示：这是您最后一张开通电子支付功能的银行卡，取消它将同时关闭您的电子支付服务";
				} else {
					msg = "您确定取消该账户的电子支付服务吗？";
				}

				BaseDroidApp.getInstanse().showErrorDialog(null, msg, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						BaseDroidApp.getInstanse().dismissErrorDialog();
						switch (PublicTools.getInt(v.getTag(), 0)) {
						case CustomDialog.TAG_SURE:
							BiiHttpEngine.showProgressDialog();
							httpObserver.req_getConversationId("getDelConversationIdCallback");
							break;
						}
					}
				});
			}
		});
	}

	/**
	 * 请求取消支付账户ConversationId回调方法
	 * 
	 * @param resultObj
	 */
	public void getDelConversationIdCallback(Object resultObj) {
		String conversationId = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		httpObserver.setConversationId(conversationId);
		httpObserver.req_getToken("getDelTokenCallback");
	}

	/**
	 * 请求取消支付账户Token回调方法
	 * 
	 * @param resultObj
	 */
	public void getDelTokenCallback(Object resultObj) {
		String tokenId = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put(BomConstants.METHOD_CLOSE_ACC_PAYMENT_SERVICE_FIELD_ACCOUNT_ID, curDleAccountId);
		params.put(BomConstants.METHOD_CLOSE_ACC_PAYMENT_SERVICE_FIELD_TOKEN_ID, tokenId);
		httpObserver.req_closeAccPaymentService(params, "closeAccPaymentServiceCallback");
	}

	/**
	 * 取消支付账户回调方法
	 * 
	 * @param resultObj
	 */
	public void closeAccPaymentServiceCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().dismissErrorDialog();
		if (1 == bomTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).size()) {
			// 关闭最后一个支付账户 回到支付服务2级菜单
			BaseDroidApp.getInstanse().showMessageDialog("电子支付服务关闭成功", new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BaseDroidApp.getInstanse().dismissErrorDialog();
					TransContext.clearData(PubConstants.CONTEXT_BOM);
					finish();
				}
			});
		} else {
			// 剔除当前开通账户
			for (int i = 0; i < bomTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).size(); i++) {
				Map<Object, Object> temp = EpayUtil.getMap(bomTransContext.getList(
						PubConstants.CONTEXT_FIELD_DREDGED_LIST).get(i));
				String accIdPattern = EpayUtil.getString(
						temp.get(BomConstants.METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_ID), "");
				if (StringUtil.isNullOrEmpty(accIdPattern))
					continue;
				if (curDleAccountId.equals(accIdPattern)) {
					bomTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).remove(i);
					i = 0;
				}
			}

			getDredgedAccountListAdapter().notifyListViewData();

			BaseDroidApp.getInstanse().showInfoMessageDialog("取消支付账户成功");

			// CustomDialog.toastShow(this, "取消支付账户成功！");
		}

	}

	/**
	 * 在列表尾部加入加号按钮
	 */
	private void addListFooterView() {
		// int[] size = { 598, 114 };
		list_footer_view = LayoutInflater.from(this).inflate(R.layout.epay_bom_add_account_item, null);
		RelativeLayout rl_add = (RelativeLayout) list_footer_view.findViewById(R.id.rl_add_account);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_add.getLayoutParams();
		params.rightMargin = getResources().getDimensionPixelSize(R.dimen.fill_margin_right);
		list_footer_view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BiiHttpEngine.showProgressDialog();
				httpObserver.req_queryAllAccount(EpayUtil.getAccTypeList(), "queryAllAccountCallback");
			}
		});
		// LinearLayout ll = (LinearLayout)
		// list_footer_view.findViewById(R.id.epay_add_account_item_ll_content);
		// LayoutParams lp = ll.getLayoutParams();
		// lp.height = EpayPubUtil.getScaleHeight(this, size);
		lv_dredged_card_list.addFooterView(list_footer_view);
	}

	/**
	 * 根据账户类型查询用户回调方法
	 * 
	 * @param accType
	 */
	public void queryAllAccountCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);

		this.accountList = EpayUtil.getList(result);

		bomTransContext.setData(PubConstants.CONTEXT_FIELD_ALL_ACCLIST, accountList);
		filterDredgeAcclist();
	}

	class RightButtonListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (!bomTransContext.isRightButtonClick()) {
				bomTransContext.setRightButtonClick(true);
				list_footer_view.setVisibility(View.GONE);
				((Button) v).setText("完成");

				ibt_setting.setEnabled(false);
			} else {
				bomTransContext.setRightButtonClick(false);
				list_footer_view.setVisibility(View.VISIBLE);
				((Button) v).setText("取消支付账户");
				ibt_setting.setEnabled(true);
			}
			getDredgedAccountListAdapter().notifyListViewData();
		}
	}

	private void refreshDelButton(View v) {
		list_footer_view.setVisibility(View.VISIBLE);
		((Button) v).setText("取消支付账户");
		 // 是否显示取消支付账户按钮
		List<Object> dredgedAccList = bomTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST);
		if(!StringUtil.isNullOrEmpty(dredgedAccList) && dredgedAccList.size() > 0){
		    bt_title_right.setVisibility(View.VISIBLE);
		}else {
			bt_title_right.setVisibility(View.GONE);	
		}
		getDredgedAccountListAdapter().notifyListViewData();
	}

	private void initDisplay() {
		activateListView();
		if (StringUtil.isNullOrEmpty(obligateMsg)) {
			obligateMsg = EpayUtil.getLoginInfo("loginHint");
		}

		if (!StringUtil.isNullOrEmpty(obligateMsg)) {
			tv_obligate_msg.setText(obligateMsg);
		} else {
			// ll_obligate_msg.setVisibility(View.GONE);
			tv_obligate_msg.setText("-");
		}

		tv_currency.setText(R.string.tran_currency_rmb);
		tv_cust_max_quota.setText(StringUtil.parseStringPattern(custMaxQuota, 2));
		tv_per_max_quota.setText(StringUtil.parseStringPattern(perMaxQuota, 2));
		tv_day_max_quota.setText(StringUtil.parseStringPattern(dayMaxQuota, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_obligate_msg);
		BiiHttpEngine.dissMissProgressDialog();
	}

	/**
	 * 交易数据处理
	 */
	private void transDataDispose() {
		BiiHttpEngine.showProgressDialogCanGoBack();
		obligateMsg = bomTransContext.getString(BomConstants.METHOD_QUERY_MAX_QUOTA_FIELD_OBLIGATE_MSG, "");
		httpObserver.req_queryMaxQuota("PB200", "queryMaxQuotaCallback");
	}

	public void setDredgedAccountListAdapter(DredgedAccountListAdapter dredgedAccountListAdapter) {
		this.dredgedAccountListAdapter = dredgedAccountListAdapter;
	}

	public DredgedAccountListAdapter getDredgedAccountListAdapter() {
		return dredgedAccountListAdapter;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESET_DATA:
			refreshDelButton(bt_title_right);
			dredgedAccountListAdapter.notifyListViewData();
			tv_cust_max_quota.setText(StringUtil.parseStringPattern(
					bomTransContext.getString(PubConstants.METHOD_QUERY_CUST_MAX_QUOTA_FIELD_AMOUNT, ""), 2));
			break;
		}
	}

	/**
	 * 过滤已开通账户
	 */
	private void filterDredgeAcclist() {
		for (int i = 0; i < this.bomTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).size(); i++) {
			Map<Object, Object> dredgedAccount = EpayUtil.getMap(this.bomTransContext.getList(
					PubConstants.CONTEXT_FIELD_DREDGED_LIST).get(i));
			String accountId = EpayUtil.getString(
					dredgedAccount.get(BomConstants.METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_ID), "");
			for (int j = 0; j < this.accountList.size(); j++) {
				Map<Object, Object> account = EpayUtil.getMap(accountList.get(j));
				String accountIdPattern = EpayUtil.getString(
						account.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID), "");
				if (accountIdPattern.equals(accountId)) {
					this.accountList.remove(j);
				}
			}
		}
		BiiHttpEngine.dissMissProgressDialog();
		if (accountList.isEmpty()) {

			// StringBuffer sb = new
			// StringBuffer("没有可供开通电子支付的账户!是否立即关联新账户？\n支持开通电子支付功能的卡类型有：");
			StringBuffer sb = new StringBuffer(getText(R.string.no_open_electronic_payment_is_correlation).toString());
//			String temp = "";
//			for (Object accTypeStr : EpayUtil.getAccTypeList()) {
//				String accTypeName = LocalData.AccountType.get(accTypeStr);
//				if (!temp.equals(accTypeName)) {
//					temp = accTypeName;
////					sb.append("“").append(accTypeName).append("”、");
//					sb.append(accTypeName).append("、");
//				}
//			}

			BaseDroidApp.getInstanse().showErrorDialog(null,/* sb.substring(0, sb.length() - 1)*/sb.toString(),
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dismissErrorDialog();

							switch (PublicTools.getInt(v.getTag(), 0)) {
							case CustomDialog.TAG_SURE:
//								goRelevanceAccount();
								finish();
								break;
							}
						}
					});

			return;
		}

		bomTransContext.setData(PubConstants.CONTEXT_FIELD_UN_DREDGED_ACCOUNT_LIST, accountList);

		Intent intent = new Intent(BankOfMobileActivity.this, SetPaymentAccSelectActivity.class);
		intent.putExtra("acclistFlag", true);
		startActivityForResult(intent, 0);
	}

}
