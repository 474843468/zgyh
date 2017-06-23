package com.chinamworld.bocmbci.biz.forex.rate;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.ForexBaseActivity;
import com.chinamworld.bocmbci.biz.forex.adapter.ForexMakeCodeAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 汇率定制页面，用户可以制定6对常用的货币对
 * 
 * @author 宁焰红
 * 
 */
public class ForexRateNotifyActivity extends ForexBaseActivity implements OnClickListener {
	private static final String TAG = "ForexRateNotifyActivity";
	/**
	 * ForexRateNotifyActivity的主布局
	 */
	private View codeInfoView = null;
	/**
	 * backButton:返回按钮
	 */
	private Button backButton = null;
	/**
	 * quickTrade：关闭
	 */
	private Button quickTrade = null;
	/**
	 * leftButton:左边的按钮
	 */
	private Button leftButton = null;
	/**
	 * 显示所有的货币对
	 */
	private GridView listView = null;
	/**
	 * 取消按钮
	 */
	private Button cancelButton = null;
	/**
	 * 确定按钮
	 */
	private Button sureButton = null;
	/**
	 * allResult:全部货币对
	 */
	private List<Map<String, String>> allResult = null;
	/** 存储处理后符合条件的数据 */
	private List<Map<String, String>> allResultList = null;
	/**
	 * customerResult:用户定制的货币对
	 */
	private List<Map<String, String>> customerResult = null;
	/**
	 * 用于标志用户选中的货币对
	 */
	private List<Boolean> listFlag = null;
	private ForexMakeCodeAdapter adapter = null;
	/** 用于记录用户选择的货币对位置 */
	private List<Integer> customerChoicePosition = null;
	/** 用于记录用户选择了多少对货币对 */
	private int number = 0;
	private String tokenId = null;
	/** 提交货币对 */
	private String selectedArr[] = null;
	/** 记录定制的货币对数目 */
	private int length = 0;
	/** 是否有数据 */
	private boolean isTrue = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLeftSelectedPosition("forexStorageCash_1");
		LogGloble.d(TAG, "onCreate");
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNullOrEmpty(commConversationId)) {
			return;
		} else {
			init();
			initOnClick();
			// 查询用户定制的货币地
			requestPsnUserCrcyCodePair();
		}
	}

	/**
	 * 初始化所有的控件
	 */
	private void init() {
		codeInfoView = LayoutInflater.from(ForexRateNotifyActivity.this).inflate(R.layout.forex_rate_make_code, null);
		tabcontent.addView(codeInfoView);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.forex_rate_makeRate));
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.GONE);
		quickTrade = (Button) findViewById(R.id.ib_top_right_btn);
		quickTrade.setVisibility(View.VISIBLE);
		quickTrade.setText(getResources().getString(R.string.forex_rate_close));
		listView = (GridView) findViewById(R.id.forex_rate_gridView);
		cancelButton = (Button) findViewById(R.id.make_cancel);
		sureButton = (Button) findViewById(R.id.make_sure);
		customerChoicePosition = new ArrayList<Integer>();
		allResultList = new ArrayList<Map<String, String>>();
		allResult = new ArrayList<Map<String, String>>();
		customerResult = new ArrayList<Map<String, String>>();

	}

	private void initOnClick() {
		// 取消按钮
		cancelButton.setOnClickListener(this);
		// 确定按钮事件
		sureButton.setOnClickListener(this);
		// 关闭按钮事件，返回到外汇行情页面
		quickTrade.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (StringUtil.isNullOrEmpty(customerResult) || customerResult == null || customerResult.size() <= 0) {
					// 用户没有定制货币对

					if (listFlag.get(position)) {
						number--;
						if (number <= 0) {
							number = 0;
						}
						listFlag.set(position, false);
						adapter.dateChanged(listFlag);

					} else {
						if (number > 5) {

							/** 选择的货币对超过6对，弹出提示框 */
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									ForexRateNotifyActivity.this.getString(R.string.forex_rate_makecode));
							listFlag.set(position, false);
							adapter.dateChanged(listFlag);
							return;
						} else {
							number++;
							listFlag.set(position, true);
							adapter.dateChanged(listFlag);
						}

					}// else
				} else {
					// 用户定制货币对，得到货币对的数目
					number = adapter.getNumber();
					if (listFlag.get(position)) {
						number--;
						if (number <= 0) {
							number = 0;
						}
						listFlag.set(position, false);
						adapter.dateChanged(listFlag);
					} else {

						if (number > 5) {
							/** 选择的货币对超过6对，弹出提示框 */
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									ForexRateNotifyActivity.this.getString(R.string.forex_rate_makecode));
							listFlag.set(position, false);
							adapter.dateChanged(listFlag);

							return;
						} else {
							number++;
							listFlag.set(position, true);
							adapter.dateChanged(listFlag);
						}

					}// else

				}

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.make_cancel:// 取消按钮
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.make_sure:// 确定按钮事件
			if (isTrue) {
				getSelectedPosition();
			}
			break;
		case R.id.ib_top_right_btn:// 关闭按钮
			// 返回到外汇行情页面
			setResult(RESULT_CANCELED);
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 查询所有的货币对--05
	 */
	private void requestPsnAllCrcyCodePairs() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_ALLRATE_CODE);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnAllCrcyCodePairsCallback");
	}

	/**
	 * 查询所有的货币对05---回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPsnAllCrcyCodePairsCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		allResult = (List<Map<String, String>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(allResult) || allResult == null || allResult.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_no_list));
			return;
		} else {
			setListFlagValue();
			// 处理全部货币对数据
			dealAllCode();

		}// else

	}

	/** 将listFlag的值全部设置为false */
	private void setListFlagValue() {
		listFlag = new ArrayList<Boolean>();
		int len = allResult.size();
		for (int i = 0; i < len; i++) {
			listFlag.add(false);
		}
	}

	/**
	 * 查询用户定制的货币对---11
	 */
	private void requestPsnUserCrcyCodePair() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_USER_CrcyCode);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		BaseHttpEngine.showProgressDialogCanGoBack();

		HttpManager.requestBii(biiRequestBody, this, "requestPsnUserCrcyCodePairCallback");
	}

	/**
	 * 用户定制货币对11-----回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPsnUserCrcyCodePairCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		List<Map<String, String>> result = (List<Map<String, String>>) biiResponseBody.getResult();
		if (result != null && result.size() > 0) {
			// 用户已经定制货币对
			// 对得到的货币对进行处理，货币对只能<=6
			int len = result.size();
			for (int i = 0; i < len; i++) {
				Map<String, String> map = result.get(i);
				if (!StringUtil.isNullOrEmpty(map)) {
					// tag=1:用户定制货币对，tag=0:未定制货币对
					String sourceCode = map.get(Forex.FOREX_MAKE_SOURCECURCDE_RES);
					String targetCode = map.get(Forex.FOREX_MAKE_TARGETCURCDE_RES);
					if (!LocalData.CurrencyShort.containsKey(sourceCode)
							|| !LocalData.CurrencyShort.containsKey(targetCode)) {
					} else {
						customerResult.add(map);
					}

				}

			}
		}
		// 查询所有的货币对
		requestPsnAllCrcyCodePairs();
	}

	/** 处理全部货币对的数据 */
	public void dealAllCode() {
		int len = allResult.size();
		for (int i = 0; i < len; i++) {
			Map<String, String> map = allResult.get(i);
			String sourceCode = map.get(Forex.FOREX_MAKE_SOURCECURCDE_RES);
			String targetCode = map.get(Forex.FOREX_MAKE_TARGETCURCDE_RES);
			if (!LocalData.CurrencyShort.containsKey(sourceCode) || !LocalData.CurrencyShort.containsKey(targetCode)) {
				// 不符合数据移除

			} else {
				allResultList.add(map);
			}
		}
		if (StringUtil.isNullOrEmpty(allResultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_no_list));
			return;
		} else {
			isTrue = true;
			if (StringUtil.isNullOrEmpty(customerResult)) {
				// 用户没有定制货币对
				adapter = new ForexMakeCodeAdapter(ForexRateNotifyActivity.this, allResultList);
				listView.setAdapter(adapter);
				listView.setFocusable(true);
			} else {
				// 用户已经定制货币对
				checkCurrency(allResultList, customerResult);
				adapter = new ForexMakeCodeAdapter(ForexRateNotifyActivity.this, allResultList, listFlag);
				listView.setAdapter(adapter);
				listView.setFocusable(true);
			}
		}
	}

	/**
	 * 用于标志用户定制的货币对，true-定制，false-未定制
	 * 
	 * @param allResult
	 *            :全部货币对list
	 * @param customerResult
	 *            :用户定制的货币对
	 */
	private void checkCurrency(List<Map<String, String>> allResult, List<Map<String, String>> customerResult) {
		int len1 = allResult.size();
		int len2 = customerResult.size();
		for (int i = 0; i < len1; i++) {
			for (int j = 0; j < len2; j++) {
				String sourceCus = customerResult.get(j).get(Forex.FOREX_MAKE_SOURCECURCDE_RES);
				String targetCus = customerResult.get(j).get(Forex.FOREX_MAKE_TARGETCURCDE_RES);
				String sourceAll = allResult.get(i).get(Forex.FOREX_MAKE_SOURCECURCDE_RES);
				String targetAll = allResult.get(i).get(Forex.FOREX_MAKE_TARGETCURCDE_RES);
				if (sourceCus.equals(sourceAll) && targetCus.equals(targetAll)) {
					// 两个List里面有相同的数据时
					listFlag.set(i, true);
					continue;
				}
			}
		}
	}

	/** 得到用户选择的货币对位置 */
	public void getSelectedPosition() {
		int count = 0;
		int len = listFlag.size();
		for (int i = 0; i < len; i++) {
			if (listFlag.get(i)) {
				Map<String, String> choiseMap = allResultList.get(i);
				String sourceCurCde = choiseMap.get(Forex.FOREX_MAKE_SOURCECURCDE_RES);
				String targetCurCde = choiseMap.get(Forex.FOREX_MAKE_TARGETCURCDE_RES);
				if (!StringUtil.isNullOrEmpty(sourceCurCde) && !StringUtil.isNullOrEmpty(targetCurCde)) {
					String sourceDealCode = null;
					if (LocalData.CurrencyShort.containsKey(sourceCurCde)) {
						sourceDealCode = LocalData.CurrencyShort.get(sourceCurCde);
					}
					String targetDealCode = null;
					if (LocalData.CurrencyShort.containsKey(targetCurCde)) {
						targetDealCode = LocalData.CurrencyShort.get(targetCurCde);
					}
					if (!StringUtil.isNullOrEmpty(sourceDealCode) && !StringUtil.isNullOrEmpty(targetDealCode)) {
						customerChoicePosition.add(i);
						count++;
					}
				}

			}// if
		}// for
		if (count <= 0) {
			// 用户没有定制货币对
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					ForexRateNotifyActivity.this.getString(R.string.forex_rate_makeRate_notify));
			return;
		} else {
			getSelectedCode();
		}
	}

	/** 得到用户选择的货币对 */
	public void getSelectedCode() {
		if (customerChoicePosition == null) {
			return;
		} else {
			int len = customerChoicePosition.size();
			/** 存储用户选择的源货币对代码 */
			List<String> selectedCodeList = new ArrayList<String>();
			/** 存储用户选择的目标货币对代码 */
			List<String> selectedTargetCodeList = new ArrayList<String>();
			for (int i = 0; i < len; i++) {
				int position = customerChoicePosition.get(i);
				Map<String, String> choiseMap = allResultList.get(position);
				String sourceCurCde = choiseMap.get(Forex.FOREX_MAKE_SOURCECURCDE_RES);
				String targetCurCde = choiseMap.get(Forex.FOREX_MAKE_TARGETCURCDE_RES);
				if (!StringUtil.isNull(sourceCurCde) && !StringUtil.isNull(targetCurCde)) {
					selectedCodeList.add(sourceCurCde);
					selectedTargetCodeList.add(targetCurCde);
				}
			}
			if (selectedCodeList == null || selectedCodeList.size() <= 0 || selectedTargetCodeList == null
					|| selectedTargetCodeList.size() <= 0) {
				return;
			} else {
				int len1 = selectedCodeList.size();
				int len2 = selectedTargetCodeList.size();
				if (len1 == len2) {
					length = len1;
					selectedArr = new String[len1];
					for (int i = 0; i < len1; i++) {
						String source = selectedCodeList.get(i);
						String target = selectedTargetCodeList.get(i);
						StringBuilder sb = new StringBuilder(source);
						sb.append(target);
						selectedArr[i] = sb.toString();
					}
				} else {
					return;
				}

				requestCommConversationId();
				BaseHttpEngine.showProgressDialogCanGoBack();
			}
		}
	}

	/**
	 * 请求ConversationId--回调
	 */
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			requestPSNGetTokenId();
		}
	}

	/**
	 * 获取tocken
	 */
	private void requestPSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPSNGetTokenIdCallback");
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
		tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			LogGloble.d(TAG + " tokenId", tokenId);
			requestPsnSetCustmerCrcyPair(selectedArr);
		}
	}

	/** 客户定制货币对提交 */
	private void requestPsnSetCustmerCrcyPair(String[] list) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_PSNSETCUSTEMRCRCY_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, Object> map = new Hashtable<String, Object>();
		map.put(Forex.FOREX_HIDDENCURRENCYPAIR_REQ, list);
		map.put(Forex.FOREX_TOKEN_CODE_REQ, tokenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnSetCustmerCrcyPairCallback");
	}

	/**
	 * 客户定制货币对提交---回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnSetCustmerCrcyPairCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, String>> resultList = (List<Map<String, String>>) biiResponseBody.getResult();
		// if (resultList == null || resultList.size() <= 0 ||
		// resultList.size()!=length) {
		// CustomDialog.toastInCenter(this,
		// getString(R.string.forex_rate_makeCode_failuer));
		// return;
		// } else {
		// 返回到外汇详情页面
		CustomDialog.toastInCenter(this, getString(R.string.forex_rate_makeCode_success));
		Intent intent = new Intent(ForexRateNotifyActivity.this, ForexRateInfoOutlayActivity.class);
		startActivity(intent);
		finish();
		// }
	}

}
