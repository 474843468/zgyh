package com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.BomConstants;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.WithoutCardContants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.adapter.DredgedAccountListAdapter;
import com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.modifyQuota.ModifyQuotaInputActivity;
import com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.setPaymentAcc.SetPaymentAccSelectActivity;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.widget.CustomDialog;

public class WithoutCardActivity extends EPayBaseActivity {

	private View ePayWithoutCard;
	private ListView lv_dredged_acc_list;
	private View list_footer_view;
	private TextView tv_title;

	private Button bt_title_right;

	private DredgedAccountListAdapter dredgedAccountListAdapter;

	private Context withoutCardTransContext;
	private PubHttpObserver httpObserver;

	private boolean isInitial = false;

	private String deleteAccId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		withoutCardTransContext = TransContext.getWithoutCardContext();
		httpObserver = PubHttpObserver.getInstance(this, PubConstants.CONTEXT_WITHOUT_CARD);
		ePayWithoutCard = LayoutInflater.from(this).inflate(R.layout.epay_without_card, null);

		super.setType(0);
		super.setShowBackBtn(true);
		if (serviceType == 1) {
			super.setTitleName(PubConstants.TITLE_SECOND_PAY);
		} else if (serviceType == 2) {
			super.setTitleName(PubConstants.TITLE_SECOND_RECEVICE);
		}
		
		
//		super.setTitleName(PubConstants.TITLE_WITHOUT_CARD);
		super.setContentView(ePayWithoutCard);
		super.onCreate(savedInstanceState);
		bt_title_right = super.initTitleRightButton("关闭", new RightButtonListener());
		tv_title = (TextView) super.findViewById(R.id.tv_title);

		slidingBody.setPadding(0, 0, 0, this.getResources().getDimensionPixelSize(R.dimen.common_bottom_padding_new));
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setTextSize(getResources().getDimensionPixelSize(R.dimen.textsize_one_five)/getResources().getDisplayMetrics().density);
		
		// 初始化当前页面内容
		initCurPage();
	}

	private void initCurPage() {
		TextView  tv_msg =(TextView)ePayWithoutCard.findViewById(R.id.tv_msg);
		if (serviceType == 1) {
			tv_msg.setText(getResources().getString(R.string.epay_wc_tv_title1));
		} else if (serviceType == 2) {
			tv_msg.setText(getResources().getString(R.string.epay_wc_tv_title2));
		}
		lv_dredged_acc_list = (ListView) ePayWithoutCard.findViewById(R.id.lv_dredged_acc_list);
		initDisplay();
	}

	/**
	 * 设置显示内容
	 */
	private void initDisplay() {
		this.dredgedAccountListAdapter = new DredgedAccountListAdapter(this);
		this.dredgedAccountListAdapter.setDelItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Map<Object, Object> tempMap = EpayUtil.getMap(withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).get(position));
				withoutCardTransContext.setData(PubConstants.CONTEXT_FIELD_SELECTED_ACC, tempMap);
				deleteAccId = EpayUtil.getString(tempMap.get(BomConstants.METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_ID), "");
				BiiHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
		this.dredgedAccountListAdapter.setSettingClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Map<Object, Object> tempMap = EpayUtil.getMap(withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).get(position));
				withoutCardTransContext.setData(PubConstants.CONTEXT_FIELD_SELECTED_ACC, tempMap);
				Intent intent = new Intent(WithoutCardActivity.this, ModifyQuotaInputActivity.class);
				startActivityForResult(intent, 0);
				overridePendingTransition(R.anim.n_pop_enter_bottom_up, R.anim.no_animation);
			}
		});
		addListFooterView();
		lv_dredged_acc_list.setAdapter(dredgedAccountListAdapter);
		isInitial = true;
	}

	/**
	 * 获取关闭支付账户预交易ConversationId回调方法
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		String conversationId = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		httpObserver.setConversationId(conversationId);
		withoutCardTransContext.setData(PubConstants.PUB_FIELD_CONVERSATION_ID, conversationId);
		httpObserver.req_closeWcAccountPre(deleteAccId, "closeWcAccountPre");
	}

	/**
	 * 在列表尾部加入加号按钮
	 */
	private void addListFooterView() {
		list_footer_view = LayoutInflater.from(this).inflate(R.layout.epay_bom_add_account_item, null);
		RelativeLayout rl_add = (RelativeLayout) list_footer_view.findViewById(R.id.rl_add_account);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_add.getLayoutParams();
		params.rightMargin = getResources().getDimensionPixelSize(R.dimen.fill_margin_right);
		list_footer_view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (0 == withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_UN_DREDGED_ACCOUNT_LIST).size()) {
					// BiiHttpEngine.dissMissProgressDialog();

					//StringBuffer sb = new StringBuffer("没有可供开通电子支付的账户!是否立即关联新账户？\n支持开通电子支付功能的卡类型有：");
					StringBuffer sb = new StringBuffer();
					if (serviceType == 1) {
						sb.append(getText(R.string.no_open_boc_payment_is_correlation1).toString());
					} else if (serviceType == 2) {
						sb.append(getText(R.string.no_open_boc_payment_is_correlation2).toString());
					}
//					sb.append(LocalData.AccountType.get("119"));

					BaseDroidApp.getInstanse().showErrorDialog(null, sb.toString(), new View.OnClickListener() {
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
				Intent intent = new Intent(WithoutCardActivity.this, SetPaymentAccSelectActivity.class);
				WithoutCardActivity.this.startActivityForResult(intent, 0);
			}
		});
		lv_dredged_acc_list.addFooterView(list_footer_view);
	}

	/**
	 * 关闭无卡支付预交易回调方法
	 * 
	 * @param resultObj
	 */
	public void closeWcAccountPre(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		String message = null;
		if(serviceType==1){
			message="您确定要关闭该账户的银联在线支付功能吗？";
		}else if(serviceType==2){
			message="您确定要关闭该账户的银联跨行代扣功能吗？";		
		}
		BaseDroidApp.getInstanse().showErrorDialog(null, message, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissErrorDialog();
				switch (PublicTools.getInt(v.getTag(), 0)) {
				case CustomDialog.TAG_SURE:
//					httpObserver.req_getConversationId("getCloseConversationIdCallback");
					httpObserver.req_getToken("getCloseTokenCallback");
					break;
				}
			}
		});
	}

	/**
	 * 获取关闭支付账户conversationId回调方法
	 * 
	 * @param resultObj
	 */
//	public void getCloseConversationIdCallback(Object resultObj) {
//		String conversationId = EpayUtil.getString(httpObserver.getResult(resultObj), "");
//		httpObserver.setConversationId(conversationId);
//		withoutCardTransContext.setData(PubConstants.PUB_FIELD_CONVERSATION_ID, conversationId);
//		httpObserver.req_getToken("getCloseTokenCallback");
//	}

	/**
	 * 获取关闭支付账户Token回调方法
	 * 
	 * @param resultObj
	 */
	public void getCloseTokenCallback(Object resultObj) {
		String token = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		Map<Object, Object> account = withoutCardTransContext.getMap(PubConstants.CONTEXT_FIELD_SELECTED_ACC);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(WithoutCardContants.METHOD_CLOSE_WC_ACC_FIELD_ACCOUNT_ID, EpayUtil.getString(account.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID), ""));
//		params.put(WithoutCardContants.METHOD_CLOSE_WC_ACC_FIELD_CLIENT_NAME, EpayUtil.getString(account.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_CUSTNAM), ""));
		params.put(WithoutCardContants.METHOD_CLOSE_WC_ACC_FIELD_CURRENT_QUOTA, EpayUtil.getString(account.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_QUOTA), ""));
//		params.put(WithoutCardContants.METHOD_CLOSE_WC_ACC_FIELD_TELPHONE, EpayUtil.getString(account.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_PHONE), ""));
//		params.put(WithoutCardContants.METHOD_CLOSE_WC_ACC_FIELD_IDENTITY_TYPE, EpayUtil.getString(account.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_IDENTITYTYPE), ""));
//		params.put(WithoutCardContants.METHOD_CLOSE_WC_ACC_FIELD_IDENTITY_NUMBER, EpayUtil.getString(account.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_IDENTITYNUM), ""));
//		params.put(WithoutCardContants.METHOD_CLOSE_WC_ACC_FIELD_CUST_CIF, EpayUtil.getString(account.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_CIF), ""));
//		params.put(WithoutCardContants.METHOD_CLOSE_WC_ACC_FIELD_STATUS, EpayUtil.getString(account.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_STATUS), ""));
		params.put(PubConstants.PUB_FIELD_TOKEN_ID, token);
		if (serviceType == 1) {
			params.put(
					WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_SERVICE_TYPE,
					"1");
			params.put(WithoutCardContants.METHOD_CLOSE_WC_ACC_FIELD_STATUS, EpayUtil.getString(account.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_STATUS), ""));
			params.put(WithoutCardContants.METHOD_CLOSE_WC_ACC_FIELD_CURRENT_QUOTA, EpayUtil.getString(account.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_QUOTA), ""));
		} else if (serviceType == 2) {
			params.put(
					WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_SERVICE_TYPE,
					"2");
			params.put(WithoutCardContants.METHOD_CLOSE_WC_ACC_FIELD_STATUS, EpayUtil.getString(account.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_COLLECTSTATUS), ""));
			params.put(WithoutCardContants.METHOD_CLOSE_WC_ACC_FIELD_CURRENT_QUOTA, EpayUtil.getString(account.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_COLLECTQUOTA), ""));
		}
		httpObserver.req_closeWcAccPaymentService(params, "closeWcAccPaymentServiceCallback");
	}

	/**
	 * 关闭支付账户回调方法
	 * 
	 * @param resultObj
	 */
	public void closeWcAccPaymentServiceCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		removeAccountByAccountId();
		getDredgedAccountListAdapter().notifyDataSetChanged();
		String message = null;
		if(serviceType==1){
			message="关闭银联在线支付功能成功";
		}else if(serviceType==2){
			message="关闭银联跨行代扣功能成功";		
		}
		
		BaseDroidApp.getInstanse().showMessageDialog(message, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissErrorDialog();
				if (withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).isEmpty()) {
					finish();
					return;
				}
				
			}
		});
	}

	/**
	 * 剔除已关闭账户
	 */
	private void removeAccountByAccountId() {
		String accIdPattern = EpayUtil.getString(withoutCardTransContext.getMap(PubConstants.CONTEXT_FIELD_SELECTED_ACC).get(BomConstants.METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_ID), "");
		List<Object> newDredgedList = EpayUtil.filterAccList(withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST), accIdPattern);
		List<Object> newUnDredgedList = EpayUtil.filterAccList(withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_UN_DREDGED_ACCOUNT_LIST), accIdPattern);
		newUnDredgedList.add(withoutCardTransContext.getMap(PubConstants.CONTEXT_FIELD_SELECTED_ACC));
		withoutCardTransContext.setData(PubConstants.CONTEXT_FIELD_DREDGED_LIST, newDredgedList);
		withoutCardTransContext.setData(PubConstants.CONTEXT_FIELD_UN_DREDGED_ACCOUNT_LIST, newUnDredgedList);
		withoutCardTransContext.clear(PubConstants.CONTEXT_FIELD_SELECTED_ACC);
	}

	public void setDredgedAccountListAdapter(DredgedAccountListAdapter dredgedAccountListAdapter) {
		this.dredgedAccountListAdapter = dredgedAccountListAdapter;
	}

	public DredgedAccountListAdapter getDredgedAccountListAdapter() {
		return dredgedAccountListAdapter;
	}

	/**
	 * 标题右侧按钮点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	class RightButtonListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (!withoutCardTransContext.isRightButtonClick()) {
				withoutCardTransContext.setRightButtonClick(true);
				list_footer_view.setVisibility(View.GONE);
				((Button) v).setText("完成");
			} else {
				withoutCardTransContext.setRightButtonClick(false);
				list_footer_view.setVisibility(View.VISIBLE);
				((Button) v).setText("关闭");
			}
			getDredgedAccountListAdapter().notifyListViewData();
		}
	}

	private void refreshDelButton(View v) {
		list_footer_view.setVisibility(View.VISIBLE);
		((Button) v).setText("关闭");
		getDredgedAccountListAdapter().notifyListViewData();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode != R.id.ib_top_right_btn)
			withoutCardTransContext.setRightButtonClick(false);
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESET_DATA:
			refreshDelButton(bt_title_right);
			break;
		}
	}
}
