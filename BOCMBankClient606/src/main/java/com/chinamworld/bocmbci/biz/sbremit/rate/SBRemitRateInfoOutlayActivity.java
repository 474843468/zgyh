package com.chinamworld.bocmbci.biz.sbremit.rate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.biz.investTask.BocInvestTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.biz.login.observer.LoginWatcher;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitBaseActivity;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitDataCenter;
import com.chinamworld.bocmbci.biz.sbremit.rate.adapter.SbremitRateInfoOutlayAdapter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask.LoginCallback;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 结售汇行情(已登录，未登录牌价)
 * 
 */
public class SBRemitRateInfoOutlayActivity extends SBRemitBaseActivity
		implements LoginWatcher {

	private static final String TAG = "SBRemitRateInfoOutlayActivity";
	private static final int REQUEST_LOGIN_CODE = 10001;
	private View view;
	/** 行情列表 */
	private ListView sbremit_quotations_lv;
	private SbremitRateInfoOutlayAdapter adapter;
	/** 头部布局 */
	private LinearLayout header_layout;
	/** 刷新 */
	private Button manual_refresh;
	/** 更新时间 */
	private LinearLayout left_title2_ll;
	private TextView update_time;
	private boolean isLogin;
	private String updateTime;
	private TextView refresh_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isLogin = BaseDroidApp.getInstanse().isLogin();
		if(isLogin){
		BiiHttpEngine.showProgressDialogCanGoBack();
		BocInvestTask task = BocInvestTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction(){
			@Override
			public void SuccessCallBack(Object param) {
				// TODO Auto-generated method stub
				init();
			}
			
		},null);
		}else{
			init();
		}
	}

	private void init() {
		BaseDroidApp.getInstanse().getLoginObserver().addWatcher(this);

		setTitle(getString(R.string.sbremit_quotations));
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		view = LayoutInflater.from(this).inflate(
				R.layout.sbremit_quotations_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		tabcontent.setPadding(0, 0, 0, 0);

//		setLeftSelectedPosition(2);

		header_layout = (LinearLayout) findViewById(R.id.header_layout);
		manual_refresh = (Button) findViewById(R.id.manual_refresh);
		update_time = (TextView) findViewById(R.id.update_time);
		sbremit_quotations_lv = (ListView) findViewById(R.id.sbremit_quotations_lv);
		left_title2_ll = (LinearLayout) findViewById(R.id.left_title2_ll);
		refresh_title = (TextView) findViewById(R.id.left_title);
		manual_refresh.setVisibility(View.GONE);
		// manual_refresh.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// requestSBRemitQuotations();
		// }
		// });
		initListDatas();
	}

	private void initListDatas() {
		isLogin = BaseDroidApp.getInstanse().isLogin();
		if (isLogin) {
			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					ActivityTaskManager.getInstance().removeAllActivity();
					finish();
				}
			});
			// 设置右上角快速交易按钮
			setRightBtnText(getString(R.string.boci_fast_trans));
			setBtnRightClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startChooseAccountActivity(SBRemitRateInfoOutlayActivity.this);
				}
			});
			refresh_title.setText(getString(R.string.for_reference_only_new2));
			left_title2_ll.setVisibility(View.VISIBLE);
		} else {
			back.setOnClickListener(backBtnClick);
			refresh_title.setText(getString(R.string.for_reference_only_new));
			left_title2_ll.setVisibility(View.VISIBLE);
		}
		requestSBRemitQuotations();
	}

	/**
	 *  返回按钮点击事件
	 */
	private OnClickListener backBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ActivityTaskManager.getInstance().removeAllActivity();
			finish();
		}

	};

	/**
	 * 查询牌价
	 */
	public void requestSBRemitQuotations() {
		BaseHttpEngine.showProgressDialog();
		final BiiRequestBody biiRequestBody = new BiiRequestBody();
		if (isLogin) {	// 查询已登录牌价
			biiRequestBody.setMethod(SBRemit.SBREMIT_FESS_QUERY_QUOTE_PRICE);
			// if (HttpManager.mPollingRequestThread == null
			// || !HttpManager.mPollingRequestThread.pollingFlag) {
			// HttpManager.requestPollingBii(biiRequestBody, handler,
			// ConstantGloble.REQUESTINFO_REFRESH_TIMES);
			// }
			HttpManager.requestBii(biiRequestBody, this,
					"requestSBRemitQuotationsCallback");
		} else {	// 查询未登录牌价
			biiRequestBody.setMethod(SBRemit.SBREMIT_GETEXCHANGE_OUTLAY);
			HttpManager.requestOutlayBii(biiRequestBody, this,
					"requestSBRemitOutlayQuotationsCallback");
		}
	}

	public void requestSBRemitOutlayQuotationsCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		updateTime = StringUtil.valueOf1((String) resultList.get(0).get(
				SBRemit.UPDATE_DATE));
		resultList = filterUnnecessaryData(resultList);
		addNecessaryData(resultList);
		sortDataByCurrency(resultList);
		SBRemitDataCenter.getInstance().setExchangeOutlay(resultList);
		refreshRateInfo();
	}

	public void requestSBRemitQuotationsCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap
				.get(SBRemit.QUOTE_PRICE_LIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		for (int i = 0; i < resultList.size(); i++) {
			String str=(String) resultList.get(i).get(SBRemit.CURRENCY);
			String string=LocalData.moneyTypeValueKey.get((str));
//			String str1=(String) resultList.get(0).get(SBRemit.CURRENCY);
			if(string!=null){
			if (string.equals("美元")) {
				updateTime = StringUtil.valueOf1((String) resultList.get(i).get(
						SBRemit.UPDATETIME));
			}
			}
		}

		resultList = filterUnnecessaryData(resultList);
		addNecessaryData(resultList);
		sortDataByCurrency(resultList);
		SBRemitDataCenter.getInstance().setExchangeOutlay(resultList);
		refreshRateInfo();

	}

	private List<Map<String, Object>> sortDataByCurrency(
			List<Map<String, Object>> resultList) {

		Collections.sort(resultList, new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
				// TODO Auto-generated method stub
				int lpos = -1;
				int rpos = -1;
				if (isLogin) {
					lpos = (Integer) LocalData.currencySort.get(lhs
							.get(SBRemit.CURRENCY));
					rpos = (Integer) LocalData.currencySort.get(rhs
							.get(SBRemit.CURRENCY));
				} else {
					lpos = (Integer) LocalData.currencySort.get(lhs
							.get(SBRemit.CUR_CODE));
					rpos = (Integer) LocalData.currencySort.get(rhs
							.get(SBRemit.CUR_CODE));
				}
				if (lpos < rpos) {
					return -1;
				} else if (lpos > rpos) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		return resultList;
	}

	private void addNecessaryData(List<Map<String, Object>> resultList) {
		// TODO Auto-generated method stub
		Map<String, String[]> currencyTemp = new HashMap<String, String[]>() {
			{
				put("美元", new String[] { "014", "USD" });
				put("澳大利亚元", new String[] { "029", "AUD" });
				put("加拿大元", new String[] { "028", "CAD" });
				put("港币", new String[] { "013", "HKD" });
				put("英镑", new String[] { "012", "GBP" });
				put("欧元", new String[] { "038", "EUR" });
				put("日元", new String[] { "027", "JPY" });
				put("新西兰元", new String[] { "087", "NZD" });
				put("新加坡元", new String[] { "018", "SGD" });
				put("泰国铢", new String[] { "084", "THB" });
				put("韩元", new String[] { "088", "KRW" });
				put("新台币", new String[] { "213", "TWD" });
				put("瑞士法郎", new String[] { "015", "CHF" });
				put("瑞典克朗", new String[] { "021", "SEK" });
				put("丹麦克朗", new String[] { "022", "DKK" });
				put("卢布", new String[] { "196", "RUB" });
				put("挪威克朗", new String[] { "023", "NOK" });
				put("菲律宾比索", new String[] { "082", "PHP" });
				// TODO 新增币种
				put("澳门元", new String[] { "081", "MOP" });
				put("印尼卢比", new String[] { "056", "IDR" });
				put("巴西里亚尔", new String[] { "134", "BRL" });
				put("阿联酋迪拉姆", new String[] { "096", "AED" });
				put("印度卢比", new String[] { "085", "INR" });
				put("南非兰特", new String[] { "070", "ZAR" });
				
//				put("林吉特", new String[] { "032", "MYR" });603屏蔽
				
				
			}
		};
		for (int i = 0; i < resultList.size(); i++) {
			Map<String, Object> map = (Map<String, Object>) resultList.get(i);
			Object key = null;
			if (isLogin) {
				key = map.get(SBRemit.CURRENCY);
			} else {
				key = map.get(SBRemit.CUR_CODE);
			}
			String value = (String) LocalData.moneyTypeValueKey.get(key);
			if (currencyTemp.containsKey(value)) {
				currencyTemp.remove(value);
			}
		}

		if (!currencyTemp.isEmpty()) {
			Set<String> tmp = currencyTemp.keySet();
			Iterator<String> it = tmp.iterator();
			while (it.hasNext()) {
				String k = it.next();
				String[] cur = ((String[]) currencyTemp.get(k));
				Map<String, Object> m = new HashMap<String, Object>();
				if (isLogin) {
					m.put(SBRemit.CURRENCY, cur[1]);
				} else {
					m.put(SBRemit.CUR_CODE, cur[1]);
				}
				m.put(SBRemit.BUY_RATE, null);
				m.put(SBRemit.SELL_RATE, null);
				m.put(SBRemit.BUY_NOTE_RATE, null);
				m.put(SBRemit.SELL_NOTE_RATE, null);
				m.put(SBRemit.UPDATE_DATE, updateTime);
				resultList.add(m);
			}
		}
	}

	private List<Map<String, Object>> filterUnnecessaryData(
			List<Map<String, Object>> resultList) {
		List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
		Iterator<Map<String, Object>> iterator = (Iterator<Map<String, Object>>) resultList
				.iterator();
		while (iterator.hasNext()) {
			Object key = null;
			Map<String, Object> map = iterator.next();
			if (isLogin) {
				key = map.get(SBRemit.CURRENCY);
			} else {
				key = map.get(SBRemit.CUR_CODE);
			}
			if (LocalData.currencySort.containsKey(key)) {
				l.add(map);
			}
		}
		return l;
	}

	public void refreshRateInfo() {
		BaseHttpEngine.dissMissProgressDialog();
		List<Map<String, Object>> resultList = SBRemitDataCenter.getInstance()
				.getExchangeOutlay();

		if (adapter != null) {
			adapter.changeData(resultList, isLogin);
		} else {
			adapter = new SbremitRateInfoOutlayAdapter(this, resultList,
					isLogin);
			sbremit_quotations_lv.setAdapter(adapter);
			adapter.setItemClickListener(itemClickListener);
		}

		update_time.setText(updateTime);

	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			if (isLogin) {
				startChooseAccountActivity(SBRemitRateInfoOutlayActivity.this);
			} else {
//				Intent intent = new Intent();
//				intent.setClass(SBRemitRateInfoOutlayActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent, REQUEST_LOGIN_CODE);
				BaseActivity.getLoginUtils(SBRemitRateInfoOutlayActivity.this).exe(new LoginCallback() {

							@Override
							public void loginStatua(boolean isLogin) {
								// TODO Auto-generated method stub
								startChooseAccountActivity(SBRemitRateInfoOutlayActivity.this);
								finish();
							}
						});
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_LOGIN_CODE:
			switch (resultCode) {
			case RESULT_OK:
				startChooseAccountActivity(this);
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		ActivityTaskManager.getInstance().removeAllActivity();
		finish();
	}

	@Override
	public void updateLoginState(boolean isLogin) {
		BiiHttpEngine.showProgressDialogCanGoBack();
		BocInvestTask task = BocInvestTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction(){
			@Override
			public void SuccessCallBack(Object param) {
				initListDatas();
			}

		},null);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		BaseDroidApp.getInstanse().getLoginObserver().removeWatcher(this);
	}

}
