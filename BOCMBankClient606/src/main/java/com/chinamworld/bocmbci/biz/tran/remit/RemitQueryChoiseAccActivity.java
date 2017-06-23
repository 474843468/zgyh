package com.chinamworld.bocmbci.biz.tran.remit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.remit.adapters.RemitQueryChoiseAccAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 套餐查询-选择账户页面 */
public class RemitQueryChoiseAccActivity extends TranBaseActivity {
	private static String TAG = "RemitQueryChoiseAccActivity";
	private ListView listView = null;
	private Button nextButton = null;
	/** 签约账户数据 */
	private List<Map<String, String>> dateList = null;
	private RemitQueryChoiseAccAdapter adapter = null;
	private String accountId = null;
	private String accountNumber = null;
	/** 1-套餐明细查询,2-解除自动续约 */
	private int tag = 0;

	/** 套餐类型 */
	// private String signType = null;
	private Map<String, String> paySetMealEntity;
	private HashMap<String, String> remitSetMealCancelPreMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		tag = getIntent().getIntExtra(ConstantGloble.FOREX_TYPE, 0);
		if (tag == RemitThirdMenu.Query_Tag) {
			setTitle(getResources().getString(R.string.trans_remit_menu_three));
		} else if (tag == RemitThirdMenu.Cancel_Tag) {
			setTitle(getResources().getString(R.string.trans_remit_menu_five));
		}
		View view = mInflater.inflate(R.layout.tran_remit_search_acc, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		listView = (ListView) findViewById(R.id.product_list);
		toprightBtn();
		nextButton = (Button) findViewById(R.id.trade_nextButton);
		dateList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TRAN_REMIT_SIGN_RESULT);
		RemitQueryChoiseAccAdapter.selectedPosition = -1;
		if (dateList == null || dateList.size() <= 0) {
			return;
		}
		adapter = new RemitQueryChoiseAccAdapter(this, dateList);
		listView.setAdapter(adapter);
		init();
	}

	private void init() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				RemitQueryChoiseAccAdapter.selectedPosition = position;
				Map<String, String> map = dateList.get(position);
				accountId = map.get(Crcd.CRCD_ACCOUNTID_RES);
				accountNumber = map.get(Crcd.CRCD_ACCOUNTNUMBER_RES);
				// 更新数据显示
				adapter.notifyDataSetChanged();
			}
		});
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (RemitQueryChoiseAccAdapter.selectedPosition < 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.dept_zntzck_sign_select));
				} else {
					// 套餐查询-----解除自动续约
					BaseHttpEngine.showProgressDialog();
					// 汇款笔数套餐查询
					RemitSetMealQuery(accountId);
				}
			}
		});
	}

	/** 汇款套餐查询 */
	private void RemitSetMealQuery(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRAN_REMITSETMEALQUERY_API);
		Map<String, String> params = new HashMap<String, String>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "RemitSetMealQueryCallBack");
	}

	/** 汇款套餐查询------回调 */
	public void RemitSetMealQueryCallBack(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		Map<String, Object> mealQueryList = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(mealQueryList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.TRAN_REMIT_QUERY_RESULT, mealQueryList);
		if (tag == RemitThirdMenu.Query_Tag) {
			openQueryActivity();
		} else if (tag == RemitThirdMenu.Cancel_Tag) {
			paySetMealEntity = (Map<String, String>) mealQueryList.get(Tran.TRAN_PAYSETMEALENTITY_RES);
			String autoFlag = paySetMealEntity.get("remitSetMealautoFlag");
			String signType = paySetMealEntity.get("extensionFlag");
			if (ConstantGloble.COMBINE_FLAG_Y.equals(autoFlag)) {
				if (ConstantGloble.COMBINE_FLAG_Y.equals(signType)) {
					requestCommConversationId();
				} else {
					BaseHttpEngine.dissMissProgressDialog();
					BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.tran_error_cancel_n));
				}
			} else {
				// 解除自动续约
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.tran_error_cancel_n));
			}
		}
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		String dateTime = (String) resultMap.get(Comm.DATETME);
		Intent intent = new Intent(RemitQueryChoiseAccActivity.this, RemitQueryDetailActivity.class);
		// 明细查询时使用selectedPosition
		intent.putExtra(ConstantGloble.FOREX_customerSelectedPosition, RemitQueryChoiseAccAdapter.selectedPosition);
		intent.putExtra(Comm.DATETME, dateTime);
		intent.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		startActivity(intent);
		BaseHttpEngine.dissMissProgressDialog();
	}

	// 解除自动续约
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 请求安全因子组合id
		requestGetSecurityFactor(ConstantGloble.TRAN_SERVICEID);
	}

	// 请求安全因子组合id
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);

		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 解除自动预约预交易
				requestRemitSetMealCancelPre(BaseDroidApp.getInstanse().getSecurityChoosed());
			}
		});
	}

	/** 解除自动预约预交易 */
	private void requestRemitSetMealCancelPre(String combin) {
		Map<String, Object> loginMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String cusName = (String) loginMap.get(Login.CUSTOMER_NAME);

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRAN_REMITSETMEALCANCELPRE_API);
		String commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID);
		biiRequestBody.setConversationId(commConversationId);
		HashMap<String, String> maps = new HashMap<String, String>();
		maps.put(Tran.TRAN_REMIT_ACCOUNTID_REQ, accountId);
		
		maps.put(Tran.TRAN_REMIT_REMITSETMEALPRODUCTTYPE_REQ, paySetMealEntity.get("signType"));
		maps.put(Tran.TRAN_REMIT_REMITSETMEALPRODUCPROPERTY_REQ, paySetMealEntity.get("remitSetMealProducProperty"));
		
		//截取 小数点后面位数 服务器返回三位    截取保留2位
		maps.put(Tran.TRAN_REMIT_ORIGNAMOUNT_REQ, formatMount(paySetMealEntity.get("orignAmount")));
		maps.put(Tran.TRAN_REMIT_NAME_REQ, cusName);
		maps.put(Tran.TRAN_REMIT_REMITSETMEALPRODUCPROPERTYID_REQ, paySetMealEntity.get("remitSetMealProductId"));
		maps.put(Tran.TRAN_REMIT_REMITSETMEALPRODUCTIDESC_REQ, paySetMealEntity.get("remitSetMealProductIDesc"));
		maps.put(Tran._COMBINID_REQ, combin);
		biiRequestBody.setParams(maps);
		remitSetMealCancelPreMap = maps;
		HttpManager.requestBii(biiRequestBody, this, "requestRemitSetMealCancelPreCallback");
	}

	/** 解除自动预约预交易  回调*/
	public void requestRemitSetMealCancelPreCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> preResult = (Map<String, Object>) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.TRAN_PRERESULT, preResult);
		// 请求密码控件随机数
		requestForRandomNumber();
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String randomNumber = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.TRAN_RANDOMNUMBER, randomNumber);
		openCancelActivity();
	}

	/** 进入到解约确认页面 */
	private void openCancelActivity() {
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(RemitQueryChoiseAccActivity.this, RemitQuerySetMealCancelConfirmActivity.class);
		intent.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		intent.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		intent.putExtra(Tran.TRAN_REMITSETMEALCANCELPRE_API, remitSetMealCancelPreMap);
		startActivity(intent);
	}

	/**汇款套餐查询  明细页面 */
	private void openQueryActivity() {
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(RemitQueryChoiseAccActivity.this, RemitQueryDetailActivity.class);
		intent.putExtra(ConstantGloble.FOREX_customerSelectedPosition, RemitQueryChoiseAccAdapter.selectedPosition);
		intent.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		intent.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		startActivity(intent);
	}
}
