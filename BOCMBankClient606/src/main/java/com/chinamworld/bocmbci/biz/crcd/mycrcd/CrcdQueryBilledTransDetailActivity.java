package com.chinamworld.bocmbci.biz.crcd.mycrcd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdQueryBilledTransDetailAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 信用卡已出账单查询明细页面 */
public class CrcdQueryBilledTransDetailActivity extends CrcdBaseActivity {
	private static final String TAG = "CrcdQueryBilledTransDetailActivity";
	private View view = null;
	private Map<String, Object> transMap = null;
	/** 交易信息 */
	private List<Map<String, String>> transList = null;
	private View resultView = null;
	/** 未出-收入、支出总计 */
	private View ll_shouandzhi = null;
	/** 已出---还款金额 */
	private View ll_currentandlowmoney = null;
	/** 当前应还金额 */
	private TextView tv_crcd_ying_huan_Money = null;
	/** 当前应还金额币种 */
	private TextView crcd_ying_currencycode = null;
	/** 当前应还金额标标记 */
	private TextView crcd_forei_ying_huan_tag = null;
	/** 当前最低还款金额 */
	private TextView tv_crcd_dangqi_low_huan_money = null;
	/** 当前最低还款金额币种 */
	private TextView crcd_min_currencycode = null;
	private TextView accNOText = null;
	private View xialaView = null;
	/** 排序区域 */
	private View sortView = null;
	private TextView sortText = null;
	private ListView lv_acc_query_result = null;
	private View moreButtonView = null;
	/** 当前应还金额 */
	private String periodAvailbleCreditLimit = null;
	/** 当期最低还款额 */
	private String lowestRepayAmount = null;
	/** 信用卡账户标识 */
	private String creditcardId = null;
	/** 已出账单月份 */
	private String statementMonth = null;
	private String currencyCode = null;
	/** 总页数 */
	private int sumNo = 0;
	/** 当前页 */
	private int pageNo = 0;
	private String primary = null;
	private CrcdQueryBilledTransDetailAdapter adapter = null;
	/** 是否刷新数据 1-否，2-是 */
	private int tag = 1;
	/** 每页显示10条 */
	private int lineNum = 10;
	/** 卡号 */
	private String cardNo = null;
	/** 总笔数 */
	private int dealCount = 0;
	/** 当前应还款金额标记 */
	private String periodAvailbleCreditLimitflag = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_yichu_card_detail));
		view = addView(R.layout.crcd_querytranns_detail);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		transMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CRCD_TRANSMAP);
		if (StringUtil.isNullOrEmpty(transMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}

		transList = (List<Map<String, String>>) transMap.get(Crcd.CRCD_TRANSLIST_RES);
		if (transList == null || transList.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		creditcardId = getIntent().getStringExtra(Crcd.CRCD_CREDITCARDID_REQ);
		statementMonth = getIntent().getStringExtra(Crcd.CRCD_STATEMENTMONTHN_REQ);
		currencyCode = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTTYPR_REQ);
		periodAvailbleCreditLimit = getIntent().getStringExtra(Crcd.CRCD_PERIODAVAILBLECRCDITLIMIT_RES);
		lowestRepayAmount = getIntent().getStringExtra(Crcd.CRCD_LOWESTREPAYAMOUNT_RES);
		cardNo = getIntent().getStringExtra(Crcd.CRCD_CADRNO_RES);
		periodAvailbleCreditLimitflag = getIntent().getStringExtra(Crcd.CRCD_PERIODAVAILBLECRCDITLIMITFLAG_RES);
		if (StringUtil.isNull(cardNo)) {
			return;
		}
		getPage(transMap);
		if (pageNo == 0 || sumNo == 0) {
			return;
		}
		init();
		setValue();
	}

	private void init() {
		resultView = view.findViewById(R.id.acc_query_result_layout);
		resultView.setVisibility(View.VISIBLE);
		ll_shouandzhi = findViewById(R.id.ll_shouandzhi);
		ll_shouandzhi.setVisibility(View.GONE);
		ll_currentandlowmoney = findViewById(R.id.ll_currentandlowmoney);
		ll_currentandlowmoney.setVisibility(View.VISIBLE);
		tv_crcd_ying_huan_Money = (TextView) findViewById(R.id.tv_crcd_ying_huan);
		crcd_ying_currencycode = (TextView) findViewById(R.id.crcd_ying_currencycode);
		tv_crcd_dangqi_low_huan_money = (TextView) findViewById(R.id.tv_crcd_dangqi_low_huan);
		crcd_min_currencycode = (TextView) findViewById(R.id.crcd_min_currencycode);
		crcd_forei_ying_huan_tag = (TextView) view.findViewById(R.id.crcd_forei_ying_huan);
		accNOText = (TextView) findViewById(R.id.tv_acc_info_currency_value);
		xialaView = findViewById(R.id.xiala_view);
		xialaView.setVisibility(View.GONE);
		sortView = findViewById(R.id.ll_sort);
//		<!-- 603 去掉全部，支出，收入 -->
		sortView.setVisibility(View.GONE);
		sortText = (TextView) findViewById(R.id.sort_text);
		sortText.setText(LocalData.sortMap[0]);
		sortView.setClickable(true);
		// 初始化排序框
		PopupWindowUtils.getInstance().setOnPullSelecterListener(this, sortView, LocalData.sortMap, null, sortClick);
		lv_acc_query_result = (ListView) findViewById(R.id.lv_acc_query_result);
		moreButtonView = LayoutInflater.from(this).inflate(R.layout.acc_load_more, null);

		// 更多按钮
		Button moreButton = (Button) moreButtonView.findViewById(R.id.btn_load_more);
		moreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tag = 2;
				if (StringUtil.isNull(creditcardId) || StringUtil.isNull(statementMonth)
						|| StringUtil.isNull(currencyCode)) {
					return;
				}
				int p = pageNo + 1;
				String page = String.valueOf(p);
				requestPsnCrcdQueryBilledTransDetail(creditcardId, statementMonth, currencyCode, page, primary,
						String.valueOf(lineNum));
			}
		});
		String codeName = null;
		if (StringUtil.isNull(currencyCode) || !LocalData.Currency.containsKey(currencyCode)) {
			codeName = "-";
		} else {
			codeName = LocalData.Currency.get(currencyCode);
		}
		String limit = null;
		if (StringUtil.isNull(periodAvailbleCreditLimit)) {
			limit = "-";
		} else {
			limit = StringUtil.parseStringCodePattern(currencyCode, periodAvailbleCreditLimit, 2);
		}
		if (!StringUtil.isNull(periodAvailbleCreditLimitflag)) {
			if (ConstantGloble.CRCD_SEARCH_ZERO.equals(periodAvailbleCreditLimitflag)) {
				crcd_forei_ying_huan_tag.setText(ConstantGloble.TOUZHI);
			} else if (ConstantGloble.CRCD_SEARCH_ONE.equals(periodAvailbleCreditLimitflag)) {
				crcd_forei_ying_huan_tag.setText(ConstantGloble.JIEYU);
			} else if (ConstantGloble.CRCD_SEARCH_TWO.equals(periodAvailbleCreditLimitflag)) {
				crcd_forei_ying_huan_tag.setVisibility(View.GONE);
			}
		}
		String acc = StringUtil.getForSixForString(cardNo);
		accNOText.setText(acc);
		tv_crcd_ying_huan_Money.setText(limit);

		crcd_ying_currencycode.setText(codeName);
		String lowest = null;
		if (StringUtil.isNull(lowestRepayAmount)) {
			lowest = "-";
		} else {
			lowest = StringUtil.parseStringCodePattern(currencyCode, lowestRepayAmount, 2);
		}
		tv_crcd_dangqi_low_huan_money.setText(lowest);
		crcd_min_currencycode.setText(codeName);
	}

	/** 得到分页标志 */
	private void getPage(Map<String, Object> transMap) {
		String sumNomber = (String) transMap.get(Crcd.CRCD_SUMNO_RES);
		if (!StringUtil.isNull(sumNomber)) {
			sumNo = Integer.valueOf(sumNomber);
		}
		String pageNumber = (String) transMap.get(Crcd.CRCD_PAGENO_REQ);
		if (!StringUtil.isNull(pageNumber)) {
			pageNo = Integer.valueOf(pageNumber);
		}
		primary = (String) transMap.get(Crcd.CRCD_PRIMARY_REQ);
		String count = (String) transMap.get(Crcd.CRCD_DEALCOUNT_REQ);
		if (!StringUtil.isNull(count)) {
			dealCount = Integer.valueOf(count);
		} else {
			dealCount = 0;
		}

	}

	/** 为控件赋值 */
	private void setValue() {
		if (tag == 1) {
			if (dealCount > 0) {
				if (lineNum < dealCount) {
					// 显示更多按钮
					lv_acc_query_result.addFooterView(moreButtonView);
				}
			} else {
				if (pageNo < sumNo) {
					// 显示更多按钮
					lv_acc_query_result.addFooterView(moreButtonView);
				}
			}

		}
		if (tag == 2) {
			if (dealCount > 0) {
				if (transList.size() >= dealCount) {
					// 已出更多按钮
					cleanMoreButton();
				}
			} else {
				if (pageNo >= sumNo) {
					// 已出更多按钮
					cleanMoreButton();
				}
			}

		}
		if (tag == 1) {
			adapter = new CrcdQueryBilledTransDetailAdapter(this, transList);
			lv_acc_query_result.setAdapter(adapter);
		} else if (tag == 2) {
			adapter.changeDate(transList);
		}

	}

	/** 查询信用卡已出账单交易明细 */
	private void requestPsnCrcdQueryBilledTransDetail(String creditcardId, String statementMonth, String accountType,
			String pageNo, String primary, String lineNum) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYBIIEDTRANSNDETAIL_API);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_CREDITCARDID_REQ, creditcardId);
		map.put(Crcd.CRCD_STATEMENTMONTHN_REQ, statementMonth);
		map.put(Crcd.CRCD_ACCOUNTTYPR_REQ, accountType);
		map.put(Crcd.CRCD_PAGENO_REQ, pageNo);
		map.put(Crcd.CRCD_PRIMARY_REQ, primary);
		map.put(Crcd.CRCD_LINENUM_REQ, lineNum);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCrcdQueryBilledTransDetailCallBack");
	}

	/** 查询信用卡已出账单交易明细-----回调 */
	public void requestPsnCrcdQueryBilledTransDetailCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		List<Map<String, String>> list = (List<Map<String, String>>) result.get(Crcd.CRCD_TRANSLIST_RES);
		if (list == null || list.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		transList.addAll(list);
		// 得到分页标志
		getPage(result);
		// 为控件赋值
		setValue();
	}

	/** 排序事件 */
	private OnClickListener sortClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_text1:// 全部
				sortText.setText(LocalData.sortMap[0]);
				allSort(transList);
				break;
			case R.id.tv_text2:// 收入
				sortText.setText(LocalData.sortMap[1]);
				inSort(transList);
				break;
			case R.id.tv_text3:// 支出
				sortText.setText(LocalData.sortMap[2]);
				outSort(transList);
				break;
			default:
				break;
			}
		}
	};

	/** 全部 */
	private void allSort(List<Map<String, String>> lists) {
		adapter.changeDate(new ArrayList<Map<String, String>>());
		if (lists == null || lists.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			return;
		}
		if (dealCount > 0) {
			if (dealCount > lists.size()) {
				if (lv_acc_query_result.getFooterViewsCount() > 0) {

				} else {
					lv_acc_query_result.addFooterView(moreButtonView);
				}
			}
		} else {
			if (sumNo > pageNo) {
				if (lv_acc_query_result.getFooterViewsCount() > 0) {

				} else {
					lv_acc_query_result.addFooterView(moreButtonView);
				}
			}
		}

		adapter = new CrcdQueryBilledTransDetailAdapter(this, lists);
		lv_acc_query_result.setAdapter(adapter);
	}

	/** 收入排序 */
	private void inSort(List<Map<String, String>> lists) {
		cleanMoreButton();
		adapter.changeDate(new ArrayList<Map<String, String>>());
		if (lists == null || lists.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			return;
		}
		List<Map<String, String>> inList = getIn(lists);
		if (inList == null || inList.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullin));
			return;
		}
		adapter = new CrcdQueryBilledTransDetailAdapter(this, inList);
		lv_acc_query_result.setAdapter(adapter);
	}

	/** 支出排序 */
	private void outSort(List<Map<String, String>> lists) {
		cleanMoreButton();
		adapter.changeDate(new ArrayList<Map<String, String>>());
		if (lists == null || lists.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			return;
		}
		List<Map<String, String>> outList = getOut(lists);
		if (outList == null || outList.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullout));
			return;
		}
		adapter = new CrcdQueryBilledTransDetailAdapter(this, outList);
		lv_acc_query_result.setAdapter(adapter);
	}

	/** 移除更多按钮 */
	private void cleanMoreButton() {
		if (lv_acc_query_result.getFooterViewsCount() > 0) {
			lv_acc_query_result.removeFooterView(moreButtonView);
		}

	}

	/** 收入 */
	public List<Map<String, String>> getIn(List<Map<String, String>> list) {
		List<Map<String, String>> inList = new ArrayList<Map<String, String>>();
		if (!StringUtil.isNullOrEmpty(list)) {
			for (Map<String, String> map : list) {
				String loanSign = (String) map.get(Crcd.CRCD_LOANSIGN_RES);
				if (ConstantGloble.CRCD_CRED.equals(loanSign)) {
					inList.add(map);
				}
			}
		}
		return inList;
	}

	/** 支出 */
	public static List<Map<String, String>> getOut(List<Map<String, String>> list) {
		List<Map<String, String>> outList = new ArrayList<Map<String, String>>();
		if (!StringUtil.isNullOrEmpty(list)) {
			for (Map<String, String> map : list) {
				String loanSign = (String) map.get(Crcd.CRCD_LOANSIGN_RES);
				if (ConstantGloble.CRCD_DEBT.equals(loanSign) || ConstantGloble.CRCD_NMON.equals(loanSign)) {
					outList.add(map);
				}
			}
		}
		return outList;
	}
}
