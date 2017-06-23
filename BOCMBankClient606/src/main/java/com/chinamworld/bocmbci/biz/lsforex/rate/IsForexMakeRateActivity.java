package com.chinamworld.bocmbci.biz.lsforex.rate;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.adapter.IsForexMakeRateAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/** 双向宝行情-----汇率定制 */
public class IsForexMakeRateActivity extends IsForexBaseActivity implements OnClickListener {
	private static final String TAG = "IsForexMakeRateActivity";
	/** IsForexMakeRateActivity的主布局 */
	private View rateInfoView = null;
	/** backButton:返回按钮 */
	private Button backButton = null;
	/** quickTrade：关闭 */
	private Button quickTrade = null;
	/** leftButton:左边的按钮 */
	private Button leftButton = null;
	/** 显示所有的货币对 */
	private GridView listView = null;
	/** 取消按钮 */
	private Button cancelButton = null;
	/** 确定按钮 */
	private Button sureButton = null;
	/** allResult:全部货币对 */
	private List<Map<String, Object>> allResult = null;
	/** customerResult:用户定制的货币对 */
	private List<Map<String, Object>> customerResult = null;
	/** 用于标志用户选中的货币对 */
	private List<Boolean> listFlag = null;
	private IsForexMakeRateAdapter adapter = null;
	/** 用于记录用户选择的货币对位置 */
	private List<Integer> customerChoicePosition = null;
	/** 用于记录用户选择了多少对货币对 */
	private int number = 0;
	/** 提交货币对 */
	private String selectedArr[] = null;
	/** 记录定制的货币对数目 */
	private int length = 0;
	/** 是否有数据 */
	private boolean isTrue = false;
	private TextView titleText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		if (StringUtil.isNullOrEmpty(commConversationId)) {
			return;
		} else {
			init();
			initOnClick();
			// 查询用户定制的货币地
			BaseHttpEngine.showProgressDialog();
			requestCustomerSetRate();
		}
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
									IsForexMakeRateActivity.this.getString(R.string.forex_rate_makecode));
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
									IsForexMakeRateActivity.this.getString(R.string.forex_rate_makecode));
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

	/** 初始化所有的控件*/
	private void init() {
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.forex_rate_make_code, null);
		tabcontent.addView(rateInfoView);
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
		titleText = (TextView) findViewById(R.id.forex_make_text);
		titleText.setText(getResources().getString(R.string.isForex_rate_make_code));
		customerChoicePosition = new ArrayList<Integer>();
		allResult = new ArrayList<Map<String, Object>>();
		customerResult = new ArrayList<Map<String, Object>>();
	}

	/** 查询用户定制的货币对 */
	@Override
	public void requestCustomerSetRateCallback(Object resultObj) {
		super.requestCustomerSetRateCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		customerResult = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (customerResult != null && customerResult.size() > 0) {
			// 处理用户定制的货币对
			customerResult = getTrueDate(customerResult);
		}
		// 查询全部汇率
		requestPsnVFGGetAllRate("");
	}

	/** 查询全部汇率---回调 */
	@Override
	public void requestPsnVFGGetAllRateCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		allResult = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (allResult == null || allResult.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_no_list));
			return;
		}
		setListFlagValue();
		allResult = getTrueDate(allResult);
		if (allResult == null || allResult.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_no_list));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		isTrue = true;
		if (StringUtil.isNullOrEmpty(customerResult)) {
			// 用户没有定制货币对
			adapter = new IsForexMakeRateAdapter(IsForexMakeRateActivity.this, allResult);
			listView.setAdapter(adapter);
			listView.setFocusable(true);
		} else {
			// 用户已经定制货币对
			checkCurrency(allResult, customerResult);
			adapter = new IsForexMakeRateAdapter(IsForexMakeRateActivity.this, allResult, listFlag);
			listView.setAdapter(adapter);
			listView.setFocusable(true);
		}
	}

	/** 处理货币对，返回的货币对可能没有对应的名称，将其除去 */
	private List<Map<String, Object>> getTrueDate(List<Map<String, Object>> list) {
		int len = list.size();
		List<Map<String, Object>> dateList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = (Map<String, Object>) list.get(i);
			// 得到源货币的代码
			String sourceCurrencyCode = (String) map.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
			String targetCurrencyCode = (String) map.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
			if (!StringUtil.isNull(sourceCurrencyCode) && !StringUtil.isNull(targetCurrencyCode)
					&& LocalData.Currency.containsKey(sourceCurrencyCode) && LocalData.Currency.containsKey(targetCurrencyCode)) {
				dateList.add(map);
			}
		}
		return dateList;
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
	 * 用于标志用户定制的货币对，true-定制，false-未定制
	 * 
	 * @param allResult
	 *            :全部货币对list
	 * @param customerResult
	 *            :用户定制的货币对
	 */
	private void checkCurrency(List<Map<String, Object>> allResult, List<Map<String, Object>> customerResult) {
		int len1 = allResult.size();
		int len2 = customerResult.size();
		for (int i = 0; i < len1; i++) {
			for (int j = 0; j < len2; j++) {
				String sourceCus = (String) customerResult.get(j).get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
				String targetCus = (String) customerResult.get(j).get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
				String sourceAll = (String) allResult.get(i).get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
				String targetAll = (String) allResult.get(i).get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
				if (sourceCus.equals(sourceAll) && targetCus.equals(targetAll)) {
					// 两个List里面有相同的数据时
					listFlag.set(i, true);
					continue;
				}
			}
		}
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
				BaseHttpEngine.showProgressDialog();
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

	/** 得到用户选择的货币对位置 */
	public void getSelectedPosition() {
		int count = 0;
		int len = listFlag.size();
		for (int i = 0; i < len; i++) {
			if (listFlag.get(i)) {
				Map<String, Object> choiseMap = allResult.get(i);
				String sourceCurCde = (String) choiseMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
				String targetCurCde = (String) choiseMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
				if (!StringUtil.isNullOrEmpty(sourceCurCde) && !StringUtil.isNullOrEmpty(targetCurCde)) {
					String sourceDealCode = null;
					if (LocalData.Currency.containsKey(sourceCurCde)) {
						sourceDealCode = LocalData.Currency.get(sourceCurCde);
					}
					String targetDealCode = null;
					if (LocalData.Currency.containsKey(targetCurCde)) {
						targetDealCode = LocalData.Currency.get(targetCurCde);
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
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					IsForexMakeRateActivity.this.getString(R.string.forex_rate_makeRate_notify));
			return;
		} else {
			getSelectedCode();
		}
	}

	/** 得到用户选择的货币对 */
	public void getSelectedCode() {
		if (customerChoicePosition == null || customerChoicePosition.size() <= 0) {
			return;
		} else {
			int len = customerChoicePosition.size();
			/** 存储用户选择的源货币对代码 */
			List<String> selectedCodeList = new ArrayList<String>();
			/** 存储用户选择的目标货币对代码 */
			List<String> selectedTargetCodeList = new ArrayList<String>();
			for (int i = 0; i < len; i++) {
				int position = customerChoicePosition.get(i);
				Map<String, Object> choiseMap = allResult.get(position);
				String sourceCurCde = (String) choiseMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
				String targetCurCde = (String) choiseMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
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
				// 货币对提交
				requestPsnVFGRateSetting(ConstantGloble.ISFOREX_UPDATE, selectedArr);
			}
		}
	}

	/** 汇率定制 */
	private void requestPsnVFGRateSetting(String submitType, String[] list) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGRATESETTING_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(IsForex.ISFOREX_SUBMITTYPE_REQ, submitType);
		map.put(IsForex.ISFOREX_HIDDENCURRENCYPAIR_REQ, list);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGRateSettingCallback");
	}

	/** 汇率定制 -----回调 */
	public void requestPsnVFGRateSettingCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, String>> result = (List<Map<String, String>>) biiResponseBody.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		// if (result == null || result.size() != length) {
		// CustomDialog.toastInCenter(this,
		// getString(R.string.forex_rate_makeCode_failuer));
		// return;
		// } else {
		// 返回到外汇详情页面
		CustomDialog.toastInCenter(this, getString(R.string.forex_rate_makeCode_success));
		Intent intent = new Intent(IsForexMakeRateActivity.this, IsForexTwoWayTreasureActivity.class);
		startActivity(intent);
		finish();
		// }
	}
}
