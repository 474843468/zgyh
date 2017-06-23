package com.chinamworld.bocmbci.biz.epay.myPayService.treaty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TreatyConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.myPayService.treaty.adapter.RelationMerchantAdapter;
import com.chinamworld.bocmbci.biz.epay.myPayService.treaty.detail.MerchantDetailActivity;
import com.chinamworld.bocmbci.biz.epay.myPayService.treaty.dialog.DeleteMerchantConfirmDialog;
import com.chinamworld.bocmbci.biz.epay.myPayService.treaty.setMerchantRelation.SelecteMerchantActivity;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.DeviceUtils;
import com.chinamworld.bocmbci.utils.PublicTools;

public class TreatyActivity extends EPayBaseActivity {

	private View treaty;
	private ListView lv_merchant;
	private View list_get_more;

	private ProgressBar progressBar;
	private TextView tv_get_more;

	private Button bt_title_right;
	private Button bt_title_right_b;

	private Context treatyContext;
	private PubHttpObserver httpObserver;

	private List<Object> merchants;
	private int recordNumber;

	private int pageIndex = 0;
	private int pageSize = 10;

	private boolean isFirstQuery;
	private RelationMerchantAdapter relationMerchantAdapter;
	private Map<Object, Object> delMerchant;

	private String systemTime;

	private String tag = "TreatyActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		treatyContext = TransContext.getTreatyTransContext();
		httpObserver = PubHttpObserver.getInstance(this, PubConstants.CONTEXT_TREATY);
		treaty = LayoutInflater.from(this).inflate(R.layout.epay_treaty, null);

		super.setType(0);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_TREATY);
		super.setContentView(treaty);
		super.onCreate(savedInstanceState);

		bt_title_right = super.initTitleRightButton("解约", new RightButtonListener());
		bt_title_right_b = super.initTitleRightButton_B("签约", new RightButtonBListener());
		slidingBody.setPadding(getResources().getDimensionPixelSize(R.dimen.fill_padding_left), 0, 0, getResources()
				.getDimensionPixelSize(R.dimen.common_bottom_padding_new));

		LayoutParams ll = bt_title_right.getLayoutParams();
		if (ll instanceof MarginLayoutParams) {
			MarginLayoutParams mlp = (MarginLayoutParams) ll;
			mlp.setMargins(0, 0, (int) DeviceUtils.dip2px(5, this), 0);
			bt_title_right.setLayoutParams(mlp);
		}
		getTransData();
	}

	private void getTransData() {
		BiiHttpEngine.showProgressDialogCanGoBack();
		requestCommConversationId();
	}

	/**
	 * 获取conversationId回调方法
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		httpObserver.setConversationId(EpayUtil.getString(httpObserver.getResult(resultObj), ""));
		// 查询已开通协议支付的商户
		isFirstQuery = true;
		httpObserver.req_queryTreatyRelations(0, true, "queryTreatyRelationsCallback");
	}

	/**
	 * 查询签约商户回调方法
	 * 
	 * @param resultObj
	 */
	public void queryTreatyRelationsCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		Map<Object, Object> resultMap = EpayUtil.getMap(result);
		recordNumber = PublicTools.getInt(
				resultMap.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_RECORD_NUMBER), 0);

		if (merchants == null || merchants.isEmpty()) {
			merchants = EpayUtil.getList(resultMap.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_LIST));
		} else {
			merchants.addAll(EpayUtil.getList(resultMap.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_LIST)));
		}

		treatyContext.setData(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_RECORD_NUMBER, recordNumber);
		treatyContext.setData(TreatyConstants.PUB_FEILD_TREATY_MERCHANTS, merchants);
		setListFooter();
		if (isFirstQuery) {
			initCurPage();
		} else {
			progressBar.setVisibility(View.GONE);
			tv_get_more.setVisibility(View.VISIBLE);
			list_get_more.setClickable(true);
			relationMerchantAdapter.notifyDataSetChanged();
		}
	}

	private void initCurPage() {
		list_get_more = LayoutInflater.from(this).inflate(R.layout.epay_tq_list_more, null);
		RelativeLayout ll_get_more = (RelativeLayout) list_get_more.findViewById(R.id.rl_get_more);
		// ll_get_more.setBackgroundResource(R.drawable.acc_card);
		ll_get_more.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.dp_five_zero);
		LayoutParams listGetMoreParams = ll_get_more.getLayoutParams();
		((RelativeLayout.LayoutParams) listGetMoreParams).setMargins(0, 0,
				getResources().getDimensionPixelSize(R.dimen.fill_margin_right), 0);
		// listGetMoreParams.height = LayoutValue.SCREEN_WIDTH * 50 / 320;
		progressBar = (ProgressBar) list_get_more.findViewById(R.id.progressBar);
		tv_get_more = (TextView) list_get_more.findViewById(R.id.tv_get_more);
		list_get_more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isFirstQuery = false;
				tv_get_more.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
				list_get_more.setClickable(false);
				httpObserver.req_queryTreatyRelations(pageIndex, false, "queryTreatyRelationsCallback");
			}
		});
		lv_merchant = (ListView) treaty.findViewById(R.id.lv_merchant);
		// 设置页尾 更多按钮
		setListFooter();
		relationMerchantAdapter = new RelationMerchantAdapter(this);
		lv_merchant.setAdapter(relationMerchantAdapter);
		relationMerchantAdapter.setDelItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
				BiiHttpEngine.showProgressDialog();
				delMerchant = EpayUtil.getMap(merchants.get(position));
				httpObserver.req_getConversationId("getConversationIdCallback");
			}
		});
		lv_merchant.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
				Map<Object, Object> merchant = EpayUtil.getMap(merchants.get(position));
				treatyContext.setData(TreatyConstants.PUB_FEILD_SELECTED_MERCHANT, merchant);
				Intent intent = new Intent(TreatyActivity.this, MerchantDetailActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		BiiHttpEngine.dissMissProgressDialog();
	}

	private void setListFooter() {

		if (null == lv_merchant)
			return;

		if (merchants.size() < recordNumber) {
			pageIndex += pageSize;
			// list_footer = list_get_more;
			if (lv_merchant.getFooterViewsCount() < 1) {
				lv_merchant.addFooterView(list_get_more);
			}
		} else {
			if (lv_merchant.getFooterViewsCount() >= 1) {
				lv_merchant.removeFooterView(list_get_more);
			}
			return;
		}
	}

	public void getConversationIdCallback(Object resultObj) {
		String conversationId = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		treatyContext.setData(PubConstants.PUB_FIELD_CONVERSATION_ID, conversationId);
		httpObserver.setConversationId(conversationId);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CONVERSATION_ID, conversationId);
		httpObserver.req_getSystemTime("getSystemTimeCallback");
		// httpObserver.req_queryTreatyQuota("queryTreatyQuotaCallback");
	}

	public void getSystemTimeCallback(Object resultObj) {
		systemTime = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		systemTime = EpayUtil.converSystemTime(systemTime);
		requestGetSecurityFactor("PB202C");
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		final BaseDroidApp baseDroidApp = BaseDroidApp.getInstanse();
		baseDroidApp.showSeurityChooseDialog(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BiiHttpEngine.showProgressDialog();
				Object merchantNo = delMerchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_BOC_NO);
				Object agreementId = delMerchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_AGREEMENT_ID);
				Object holderId = delMerchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_HOLDER_MERID);
				Object merchantName = delMerchant
						.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_MERCHANT_NAME);
				Object cardNo = delMerchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_NO);
				Object cardType = delMerchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_TYPE);
				Object dailyQuota = delMerchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_DAILY_QUOTA);
				String combinId = baseDroidApp.getSecurityChoosed();
				HashMap<Object, Object> params = new HashMap<Object, Object>();
				params.put(PubConstants.PUB_FIELD_COMBIN_ID, combinId);
				params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_MERCHANT_NO, merchantNo);
				params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_AGREEMENT_ID, agreementId);
				params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_HOLDER_MER_ID, holderId);
				params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_MERCHANT_NAME, merchantName);
				params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_CARD_NO, cardNo);
				params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_CARD_TYPE, cardType);
				params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_DAILY_QUOTA, dailyQuota);
				params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_SIGN_DATE, systemTime);
				httpObserver.req_deleteMerchantRelationPre(params, "deleteMerchantRelationPreCallback");
			}
		});
	}
//	public static Map<Object, Object> resultMap = null;
//	public static Map<String, Object> resultMap = null;
	/** 删除签约商户预交易会调方法 */
	public void deleteMerchantRelationPreCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);

//		resultMap = EpayUtil.getMap(result);
		Map<String, Object> resultMap = EpayUtil.getMapResponse(result);
//		List<Object> factorList = EpayUtil.getFactorList(resultMap);
//		List<Object> factorList = EpayUtil.getFactorListResponse(resultMap);

		BiiHttpEngine.dissMissProgressDialog();
		showDeleteMerchantConfirmDialog(resultMap);
	}

	private void showDeleteMerchantConfirmDialog(final Map<String, Object> resultMap) {
		dcDialog = new DeleteMerchantConfirmDialog(systemTime, resultMap, delMerchant, this);
		dcDialog.setErrorOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
				showDeleteMerchantConfirmDialog(resultMap);
			}
		});
	}

	private DeleteMerchantConfirmDialog dcDialog;

	@Override
	public void afterKeyboardHidden(SipBox arg0, int arg1) {
		if (dcDialog != null)
			((RelativeLayout) dcDialog.getView()).scrollTo(0, 0);
	}

	@Override
	public void beforeKeyboardShow(SipBox sipbox, int keyboardHeight) {
		// 判断弹出的安全键盘是否会遮掩输入框
		int[] location = new int[2];
		sipbox.getLocationOnScreen(location);
		int y = location[1];
		// 距底距离
		int bottom = getWindowHeight() - y - sipbox.getHeight();
		if (bottom < keyboardHeight) {// 说明遮掩
			if (dcDialog != null)
				((RelativeLayout) dcDialog.getView()).scrollTo(0, keyboardHeight - bottom);
		}
	}

	public void setRelationMerchantAdapter(RelationMerchantAdapter relationMerchantAdapter) {
		this.relationMerchantAdapter = relationMerchantAdapter;
	}

	public RelationMerchantAdapter getRelationMerchantAdapter() {
		return relationMerchantAdapter;
	}

	class RightButtonBListener implements View.OnClickListener {

		public void onClick(View v) {
			BiiHttpEngine.showProgressDialog();
			httpObserver.req_queryMerchants(0, "queryMerchantsCallback");
		}
	}

	/**
	 * 查询商户回调方法
	 * 
	 * @param resultObj
	 */
	public void queryMerchantsCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Object result = httpObserver.getResult(resultObj);
		List<Object> merchantList = EpayUtil.getList(result);
		treatyContext.setData(TreatyConstants.PUB_FEILD_TREATY_UN_MERCHANTS, merchantList);

		if (merchantList.isEmpty()) {
			final BaseDroidApp bdApp = BaseDroidApp.getInstanse();
			bdApp.showMessageDialog("没有可供选择的商户！请联系客服", new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					bdApp.dismissMessageDialog();
				}
			});

			return;
		}

		Intent intent = new Intent(TreatyActivity.this, SelecteMerchantActivity.class);
		startActivityForResult(intent, 0);
	}

	class RightButtonListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (!treatyContext.isRightButtonClick()) {
				treatyContext.setRightButtonClick(true);
				((Button) v).setText("完成");
				list_get_more.setVisibility(View.GONE);
			} else {
				treatyContext.setRightButtonClick(false);
				((Button) v).setText("解约");
				list_get_more.setVisibility(View.VISIBLE);
			}
			getRelationMerchantAdapter().notifyDataSetChanged();
		}
	}

	@Override
	public void finish() {
		treatyContext.clear(null);
		super.finish();
	}

	private void refreshDelButton(View v) {
		((Button) v).setText("解约");
		list_get_more.setVisibility(View.VISIBLE);
		getRelationMerchantAdapter().notifyDataSetChanged();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESET_DATA:
			refreshPageData();
			break;
		default:
			refreshDelButton(bt_title_right);
			break;
		}
	}

	/**
	 * 刷新页面数据
	 */
	public void refreshPageData() {
		merchants = null;
		pageIndex = 0;
		BiiHttpEngine.showProgressDialog();
		requestCommConversationId();
	}

}
