package com.chinamworld.bocmbci.biz.finc.trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.adapter.FundThrowInListAdapter;
import com.chinamworld.bocmbci.biz.finc.adapter.FundThrowListAdapter;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金转换页面
 * 
 * @author xyl
 * 
 */
public class FincTradeThrowActivity extends FincBaseActivity {
	private static final String TAG = "FincTradeThrowActivity";

	private Button confirmBtn;
	private Spinner fundSellFlagSp;
	private EditText amountEdit;

	private String fundSellFlagStr;
	/** 基金代码 */
	// private List<Map<String, Object>> showfundList;

	private List<Map<String, Object>> changInFundList;
	private List<Map<String, Object>> changOutFundList;

	private RelativeLayout outLayout;
	private LinearLayout outInfoLayout;
	private LinearLayout outListLayout;

	private RelativeLayout inLayout;
	private LinearLayout inInfoLayout;
	private LinearLayout inListLayout;

	private ListView accList;
	/** list条目高度 */
	private int itemHeight = 0;

	private String outFundCodeStr, outFundNameStr, outRiskLevelStr,
			outaBalanceStr, outFundCurrencyCode;
	private String inFundCodeStr, inFundNameStr, inaBalanceStr;

	private TextView throwOutSelectTv;
	private TextView throwInSelectTv;

	private String amountStr;
	private OnItemClickListener outListItermClick;
	private OnItemClickListener inListItermClick;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// changInFundList = getTransInList(fincControl.fundBalancList);
		changOutFundList = getTransOutList(fincControl.fundBalancList);
		init();
		initData();
	}

	private void initData() {
		if (!StringUtil.isNullOrEmpty(fincControl.tradeFundDetails)) {
			Map<String, Object> tempMap = fincControl.tradeFundDetails;
			Map<String, Object> map = (Map<String, Object>) tempMap
					.get(Finc.FINC_FUNDINFO_REQ);
			outFundCodeStr = (String) map.get(Finc.FINC_FUNDCODE_REQ);
			outFundNameStr = (String) map.get(Finc.FINC_FUNDNAME_REQ);
			outFundCurrencyCode = (String) map
					.get(Finc.FINC_QUERYBANCE_CURRENCY);
			StringUtil.setInPutValueByCurrency(amountEdit, outFundCurrencyCode);
			outaBalanceStr = (String) tempMap
					.get(Finc.FINC_TOTALAVAILABLEBALANCE);
			outRiskLevelStr = (String) map.get(Finc.FINC_RISKLV);
			outLayout.setVisibility(View.VISIBLE);
			outListLayout.setVisibility(View.GONE);
			outInfoLayout.addView(getShowView(outFundNameStr,
					outFundCodeStr, outRiskLevelStr, outaBalanceStr));
			throwOutSelectTv.setVisibility(View.GONE);
			// BaseHttpEngine.showProgressDialog();
			// fundThrowInput(outFundCodeStr);
		}
	}

	private void init() {
		View childView = mainInflater.inflate(
				R.layout.finc_myfinc_balance_throw_submit2, null);
		tabcontent.addView(childView);
		setTitle(R.string.finc_myfinc_button_give);
		confirmBtn = (Button) childView.findViewById(R.id.finc_confrim);
		amountEdit = (EditText) childView.findViewById(R.id.finc_throw_edit);
		fundSellFlagSp = (Spinner) childView.findViewById(R.id.spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.dept_spinner, LocalData.fundSellFlagStr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fundSellFlagSp.setAdapter(adapter);
		fundSellFlagSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				fundSellFlagStr = LocalData.fundSellFlag.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		throwOutSelectTv = (TextView) findViewById(R.id.finc_throw_out_select_tv);
		throwInSelectTv = (TextView) findViewById(R.id.finc_throw_in_select_tv);
		confirmBtn.setOnClickListener(this);
		outLayout = (RelativeLayout) childView
				.findViewById(R.id.finc_throwout_layout);
		outInfoLayout = (LinearLayout) childView
				.findViewById(R.id.finc_out_info_layout);
		outListLayout = (LinearLayout) childView
				.findViewById(R.id.finc_throwout_list_layout);

		inLayout = (RelativeLayout) childView
				.findViewById(R.id.finc_throwin_layout);
		inInfoLayout = (LinearLayout) childView
				.findViewById(R.id.finc_in_info_layout);
		inListLayout = (LinearLayout) childView
				.findViewById(R.id.finc_throwin_list_layout);
		
		TextView finc_myfinc_throw_throwType = (TextView) findViewById(R.id.finc_myfinc_throw_throwType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_myfinc_throw_throwType);
		
		outLayout.setOnClickListener(this);
		inLayout.setOnClickListener(this);
		outListItermClick = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, Object> tempMap = changOutFundList
						.get(position);
				Map<String, String> map = (Map<String, String>) tempMap
						.get(Finc.FINC_FUNDINFO_REQ);
				outFundCodeStr = map.get(Finc.FINC_FUNDCODE_REQ);
				outFundNameStr = map.get(Finc.FINC_FUNDNAME_REQ);
				outFundCurrencyCode = (String) map
						.get(Finc.FINC_QUERYBANCE_CURRENCY);
				StringUtil.setInPutValueByCurrency(amountEdit,
						outFundCurrencyCode);
				outaBalanceStr = (String) tempMap
						.get(Finc.FINC_TOTALAVAILABLEBALANCE);
				outRiskLevelStr = map.get(Finc.FINC_RISKLV);
				outLayout.setVisibility(View.VISIBLE);
				outListLayout.setVisibility(View.GONE);
				outInfoLayout.removeAllViews();
				outInfoLayout.addView(getShowView(outFundNameStr,
						outFundCodeStr, outRiskLevelStr, outaBalanceStr));
				inInfoLayout.removeAllViews();
				inFundCodeStr = null;
				throwOutSelectTv.setVisibility(View.GONE);
				throwInSelectTv.setVisibility(View.VISIBLE);
			}
		};
		inListItermClick = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, Object> tempMap = changInFundList.get(position);
				// Map<String, String> map = (Map<String, String>) tempMap
				// .get(Finc.FINC_FUNDINFO_REQ);
				inFundCodeStr = String.valueOf(tempMap
						.get(Finc.FINC_FUNDCODE_REQ));
				inFundNameStr = String.valueOf(tempMap
						.get(Finc.FINC_FUNDNAME_REQ));
				// inaBalanceStr = (String) tempMap
				// .get(Finc.FINC_FRONTAVAILABLEBALANCE);
				// inRiskLevelStr = map.get(Finc.FINC_RISKLV);
				inLayout.setVisibility(View.VISIBLE);
				inListLayout.setVisibility(View.GONE);
				inInfoLayout.removeAllViews();
				inInfoLayout.addView(getShowView(inFundNameStr, inFundCodeStr));
				throwInSelectTv.setVisibility(View.GONE);
			}
		};
		initRightBtnForMain();

	}

	/**
	 * 设置listLayout 高度
	 * 
	 * @param layout
	 */
	private void setLayoutHeight(LinearLayout layout,
			List<Map<String, Object>> tranoutAccountList) {
		// 根据返回数据 初始化mAccOutListLayout的高度
		if (tranoutAccountList == null || tranoutAccountList.size() < 1) {
			return;
		}
		int padding = getResources().getDimensionPixelSize(
				R.dimen.fill_margin_left) * 2;
		if (tranoutAccountList.size() > 1 && tranoutAccountList.size() <= 4) {
			LayoutParams lp = new LayoutParams(LayoutValue.SCREEN_WIDTH
					- padding, tranoutAccountList.size() * itemHeight);
			layout.setLayoutParams(lp);
		} else if (tranoutAccountList.size() > 4) {
			LayoutParams lp = new LayoutParams(LayoutValue.SCREEN_WIDTH
					- padding, itemHeight * 4);
			layout.setLayoutParams(lp);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_throwout_layout:// 选择转出账户
			if (inLayout.getVisibility() == View.GONE) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.finc_selece_throw_infund_error));
				return;
			}
			if (changOutFundList.size() < 1) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.finc_nothrow_outfund_error));
				return;
			}
			accList = new ListView(this);
			accList.setFadingEdgeLength(0);
			accList.setScrollingCacheEnabled(false);
//			accList.addHeaderView(getHeaderView(R.string.finc_transout_select_info), null, false);
			FundThrowListAdapter outadapter = new FundThrowListAdapter(this,
					changOutFundList);
			View inView = outadapter.getView(0, null, accList);
			inView.measure(0, 0);
			itemHeight = getResources().getDimensionPixelSize(R.dimen.btn_bottom_height);
			accList.setAdapter(outadapter);
			accList.setOnItemClickListener(outListItermClick);
			outListLayout.addView(accList);
			setLayoutHeight(outListLayout, changOutFundList);
			outLayout.setVisibility(View.GONE);
			outListLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.finc_throwin_layout:
			if (outFundCodeStr == null
					|| outLayout.getVisibility() == View.GONE) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.finc_selece_throw_outfund_error));
				return;
			}
			BaseHttpEngine.showProgressDialog();
			fundThrowInput(outFundCodeStr);
			// if (changInFundList ==null||changInFundList.size() < 1) {
			// BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_nothrow_infund_error));
			// return;
			// }
			break;
		case R.id.finc_confrim:
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			if (outFundCodeStr == null
					|| outListLayout.getVisibility() == View.VISIBLE) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.finc_selece_throw_outfund_error));
				return;
			} else if (inFundCodeStr == null
					|| inListLayout.getVisibility() == View.VISIBLE) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.finc_selece_throw_infund_error));
				return;
			} else {
				amountStr = amountEdit.getText().toString().trim();
				RegexpBean regexp = StringUtil.getRegexBeanByCurrency(
						getString(R.string.finc_myfinc_throw_thow_total_no),
						amountStr, outFundCurrencyCode);
				lists.add(regexp);
				if (RegexpUtils.regexpDate(lists)) {
					if (Double.valueOf(amountStr) > Double
							.valueOf(outaBalanceStr)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getString(R.string.finc_throw_loss_error));
						return;
					}
					BaseHttpEngine.showProgressDialog();
					requestFundCompanyInfoQuery(outFundCodeStr);
				}
			}

			break;
		default:
			break;
		}
	}
	
	@Override
	public void fundCompanyInfoQueryCallback(Object resultObj) {
		super.fundCompanyInfoQueryCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent();
		intent.setClass(this, FincTradeThrowConfirmActivity.class);
		intent.putExtra(Finc.I_OUTFUNDCODE, outFundCodeStr);
		intent.putExtra(Finc.I_OUTFUDNAME, outFundNameStr);
		intent.putExtra(Finc.I_INFUDCODE, inFundCodeStr);
		intent.putExtra(Finc.I_INFUNDNAME, inFundNameStr);
		intent.putExtra(Finc.I_AMOUNT, amountStr);
		intent.putExtra(Finc.I_FUNDSELLFLAGSTR, fundSellFlagStr);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void fundThrowInputCallback(Object resultObj) {
		super.fundThrowInputCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> list = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		inFundCodeStr = null;
		inFundNameStr = null;
		inaBalanceStr = null;
		changInFundList = list;
		inLayout.setVisibility(View.VISIBLE);
		inListLayout.setVisibility(View.GONE);
		inInfoLayout.removeAllViews();
//		throwInSelectTv.setText(getString(R.string.tran_please_choose));
		accList = new ListView(this);
		accList.setFadingEdgeLength(0);
		accList.setScrollingCacheEnabled(false);
//		accList.addHeaderView(getHeaderView(R.string.finc_transin_select_info), null, false);
		FundThrowInListAdapter inadapter = new FundThrowInListAdapter(this,
				changInFundList);
		View outView = inadapter.getView(0, null, null);
		outView.measure(0, 0);
		itemHeight = getResources().getDimensionPixelOffset(R.dimen.btn_bottom_height);
		accList.setAdapter(inadapter);
		accList.setOnItemClickListener(inListItermClick);
		inListLayout.removeAllViews();
		inListLayout.addView(accList);
		setLayoutHeight(inListLayout, changInFundList);
		inLayout.setVisibility(View.GONE);
		inListLayout.setVisibility(View.VISIBLE);

	}

	/**
	 * 可转出账户的列表
	 * 
	 * @return
	 */
	private List<Map<String, Object>> getTransOutList(
			List<Map<String, Object>> sourcelist) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : sourcelist) {
			Map<String, String> fundInfoMap = (Map<String, String>) map
					.get(Finc.FINC_FUNDINFO);
			if (StringUtil.parseStrToBoolean(fundInfoMap.get(Finc.ISCHANGEOUT))) {
				list.add(map);
			}
		}
		return list;
	}

	// private List<Map<String, Object>> getList(String fundCode,
	// List<Map<String, Object>> sourceMap) {
	// List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	// for (Map<String, Object> map : sourceMap) {
	// if (!fundCode.equals(map.get(Finc.FINC_FUNDCODE_REQ))) {
	// list.add(map);
	// }
	// }
	// return list;
	//
	// }

	private View getShowView(String fundName, String fundCode, String risklevel, String balance) {
		View tempView = mainInflater.inflate(R.layout.finc_throw_show, null);
		TextView fundNameTv = (TextView) tempView
				.findViewById(R.id.finc_fundname_tv);
		TextView fundCodeTv = (TextView) tempView
				.findViewById(R.id.finc_fundcode_tv);
		TextView riskLevelTv = (TextView) tempView
				.findViewById(R.id.finc_risklevel_tv);
		TextView aBalanceTv = (TextView) tempView
				.findViewById(R.id.finc_avilablebalance_tv);
		if (!StringUtil.isNullOrEmpty(fundName) && !StringUtil.isNullOrEmpty(fundCode)) {
			fundNameTv.setText(fundName);
			fundCodeTv.setText("【" + fundCode + "】");
		} else {
			fundNameTv.setText("");
			fundCodeTv.setText("");
		}
		if (!StringUtil.isNullOrEmpty(risklevel)) {
			riskLevelTv.setText(LocalData.fincRiskLevelCodeToStrFUND
					.get(risklevel));
		} else {
			riskLevelTv.setText("");
		}
		if (!StringUtil.isNullOrEmpty(balance)) {
			aBalanceTv.setText(StringUtil.parseStringPattern(balance, 2));
		} else {
			aBalanceTv.setText("");
		}
		// 给 TextView 设定 Text
//		throwOutSelectTv.setText(getString(R.string.finc_myfinc_fund));
		return tempView;
	}

	/** 转入布局　 */
	private View getShowView(String fundName, String fundCode) {
		View tempView = mainInflater.inflate(R.layout.finc_throw_show, null);
		TextView fundNameTv = (TextView) tempView
				.findViewById(R.id.finc_fundname_tv);
		TextView fundCodeTv = (TextView) tempView
				.findViewById(R.id.finc_fundcode_tv);
//		TextView fundCodeTv = (TextView) tempView
//				.findViewById(R.id.finc_risklevel_tv);
		LinearLayout layout = (LinearLayout) tempView
				.findViewById(R.id.finc_avilablebalance_layout);
		layout.setVisibility(View.GONE);
		fundNameTv.setText(StringUtil.valueOf1(fundName));
		fundCodeTv.setText("【" + StringUtil.valueOf1(fundCode) + "】");
//		throwInSelectTv.setText(getString(R.string.finc_myfinc_fund));
		return tempView;
	}

	private TextView getHeaderView(int textResouceId) {
		TextView headerView = new TextView(this);
		headerView.setText(textResouceId);
		headerView.setTextSize(13);
		headerView.setTextColor(getResources().getColor(R.color.gray));
		headerView.setSingleLine(true);
		return headerView;
	}

	@Override
	public boolean httpCodeErrorCallBackPre(String code) {
		return super.httpCodeErrorCallBackPre(code);
	}

}
