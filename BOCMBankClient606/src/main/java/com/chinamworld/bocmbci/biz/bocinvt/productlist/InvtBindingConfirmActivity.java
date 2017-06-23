package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 登记账户确认页面
 * 
 * @author wangmengmeng
 * 
 */
public class InvtBindingConfirmActivity extends BociBaseActivity {
	/** 登记账户确认信息页 */
	private View view;
	/** 选择的资金账户信息 */
	private Map<String, Object> chooseBindingMap;
	/** 账户类型 */
	private TextView accTypeText;
	/** 账户别名 */
	private TextView accAlisText;
	/** 账户 */
	private TextView accNumberText;
	private Button lastButton;
	private Button sureButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getResources().getString(R.string.boci_binding_title));
		// 右上角按钮赋值
		setText(this.getResources().getString(R.string.close));
		// 添加布局
		view = addView(R.layout.bocinvt_binding_confirm);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		goneLeftView();
		// 初始化界面
		init();
	}

	private void init() {
		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
		back.setVisibility(View.GONE);
		// 步骤条
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.bocinvt_rel_step1),
						this.getResources().getString(
								R.string.bocinvt_rel_step2),
						this.getResources().getString(
								R.string.bocinvt_rel_step3) });
		StepTitleUtils.getInstance().setTitleStep(2);
		chooseBindingMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_XPADRESET_CHOOSE);
		accTypeText = (TextView) view.findViewById(R.id.acc_type);
		accAlisText = (TextView) view.findViewById(R.id.acc_alias);
		accNumberText = (TextView) view.findViewById(R.id.acc_number);
		lastButton = (Button) view.findViewById(R.id.acc_last);
		sureButton = (Button) view.findViewById(R.id.acc_sure);
//		LinearLayout bocinvt_currency = (LinearLayout) view
//				.findViewById(R.id.bocinvt_currency);
//		List<Map<String, Object>> currencyList = (List<Map<String, Object>>) chooseBindingMap
//				.get(ConstantGloble.ACC_DETAILIST);
//		List<Map<String, Object>> accdetailList = new ArrayList<Map<String, Object>>();
//		if (currencyList == null || currencyList.size() == 0) {
//
//		} else {
//			for (int i = 0; i < currencyList.size(); i++) {
//				String currencyname = (String) currencyList.get(i).get(
//						Acc.DETAIL_CURRENCYCODE_RES);
//				// 过滤
//				if (StringUtil.isNull(LocalData.currencyboci.get(currencyname))) {
//					accdetailList.add(currencyList.get(i));
//				}
//			}
//		}
//		if (accdetailList == null || accdetailList.size() == 0) {
//
//		} else {
//			for (int i = 0; i < accdetailList.size(); i++) {
//				View currency_view = LayoutInflater.from(
//						InvtBindingConfirmActivity.this).inflate(
//						R.layout.bocinvt_currency_balance_forbinding, null);
//				bocinvt_currency.addView(currency_view, i);
//
//				TextView tv_acc_currencycode = (TextView) currency_view
//						.findViewById(R.id.acc_currencycode);
//				TextView tv_acc_accbookbalance = (TextView) currency_view
//						.findViewById(R.id.acc_bookbalance);
//				PopupWindowUtils.getInstance().setOnShowAllTextListener(
//						BaseDroidApp.getInstanse().getCurrentAct(),
//						tv_acc_accbookbalance);
//				PopupWindowUtils.getInstance().setOnShowAllTextListener(
//						BaseDroidApp.getInstanse().getCurrentAct(),
//						tv_acc_currencycode);
//				String currencyname = (String) accdetailList.get(i).get(
//						Acc.DETAIL_CURRENCYCODE_RES);
//				String cashRemit = (String) accdetailList.get(i).get(
//						Acc.DETAIL_CASHREMIT_RES);
//				if (LocalData.Currency.get(currencyname).equals(
//						ConstantGloble.ACC_RMB)) {
//					tv_acc_currencycode.setText(LocalData.Currency
//							.get(currencyname) + ConstantGloble.ACC_COLON);
//				} else {
//					tv_acc_currencycode.setText(LocalData.Currency
//							.get(currencyname)
//							+ ConstantGloble.ACC_STRING
//							+ LocalData.CurrencyCashremit.get(cashRemit)
//							+ ConstantGloble.ACC_COLON);
//				}
//				tv_acc_accbookbalance.setText(StringUtil
//						.parseStringCodePattern(
//								currencyname,
//								(String) accdetailList.get(i).get(
//										Acc.DETAIL_AVAILABLEBALANCE_RES), 2));
//			}
//		}
		// 赋值操作
		String account_type = String.valueOf(chooseBindingMap
				.get(BocInvt.BOCINVT_BINDING_ACCOUNTTYPE_RES));
		accTypeText.setText(LocalData.AccountType.get(account_type.trim()));
		accAlisText.setText(String.valueOf(chooseBindingMap
				.get(BocInvt.BOCINVT_BINDING_NICKNAME_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accAlisText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accTypeText);
		String acc_account_num = String.valueOf(chooseBindingMap
				.get(BocInvt.BOCINVT_BINDING_ACCOUNTNUMBER_RES));
		accNumberText.setText(StringUtil.getForSixForString(acc_account_num));
		lastButton.setOnClickListener(lastBtnClick);
		sureButton.setOnClickListener(sureBtnClick);
	}

	/** 上一步按钮监听事件 */
	OnClickListener lastBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_CANCELED);
			finish();
		}
	};
	/** 确定按钮监听事件 */
	OnClickListener sureBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 请求conversationid
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 请求tokenid
		requestPSNGetTokenId();
	}

	/**
	 * 获取tocken
	 */
	private void requestPSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPSNGetTokenIdCallback");
	}

	/**
	 * 获取tokenId----回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
		} else {
			// 请求登记结果
			requestXpadResult(tokenId);
		}

	}

	/**
	 * 获取登记账户结果
	 */
	private void requestXpadResult(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PSNXPADRESULT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(BocInvt.BOCINVT_XPADRESULT_ACCOUNTID_REQ, String
				.valueOf(chooseBindingMap
						.get(BocInvt.BOCINVT_BINDING_ACCOUNTID_RES)));
		paramsmap.put(BocInvt.BOCINVT_XPADRESULT_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestXpadResultCallback");
	}

	/**
	 * 获取登记账户结果——回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestXpadResultCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String result = (String) biiResponseBody.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		// if(StringUtil.isNullOrEmpty(result)){

		// }else if(result.equalsIgnoreCase(ConstantGloble.STATUS_SUCCESS)){
		// 登记成功进入确定页面
		Intent intent = new Intent(this, InvtBindingSuccessActivity.class);
		startActivityForResult(intent,
				ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE);
		// }

	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_CANCELED);
			finish();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		case RESULT_CANCELED:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
	}
}
