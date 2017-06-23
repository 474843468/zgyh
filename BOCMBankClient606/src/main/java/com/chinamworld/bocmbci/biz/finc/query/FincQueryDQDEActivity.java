package com.chinamworld.bocmbci.biz.finc.query;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.FincUtils;
import com.chinamworld.bocmbci.biz.finc.adapter.FundQueryDQDEListAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

/**
 * 基金定期定额交易 查询
 * 
 * @author xyl
 * 
 */
public class FincQueryDQDEActivity extends FincBaseActivity {
	
	private Spinner tradeSycleSp;
	private EditText fundCodeEt;
	private TextView transCycle, fundCode;
	private ListView listView;
	private Button searchBtn;
	private List<Map<String, Object>> resultList;
	private FundQueryDQDEListAdapter apdater;
	private OnItemClickListener onItemClickListener;

	private boolean isNewQuery;
	private int totalNum;
	private int currentIndex = 10;
	private int pageSize = 10;
	private String transCycleStr, fundCodeStr;
	private View footerView, up;
	private OnClickListener footerOnclickListenner;

	private boolean changeQuery ;
	/** 退出动画 , 进入动画 */
	private Animation animation_up, animation_down;
	private View query_resultLayout, queryBeforLayoutOut, queryBeforLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
//		initQueryPopupWindow();
	}
	
	@Override
	public void fundQueryDQDECallback(Object resultObj) {
		super.fundQueryDQDECallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
//		transCycle.setText(StringUtil.valueOf1((String)LocalData.transCycleMap.get(transCycleStr)));
//		fundCode.setText(StringUtil.valueOf1(fundCodeStr));
		if(StringUtil.isNullOrEmpty(fincControl.fundAvailableList)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
//			if(changeQuery && apdater != null)
//				apdater.notifyDataSetChanged(new ArrayList<Map<String,Object>>());
			return;
		}
//		query_resultLayout.setVisibility(View.VISIBLE);
//		queryBeforLayout.startAnimation(animation_up);
//		queryBeforLayoutOut.setBackgroundResource(R.drawable.img_bg_query_no);
//		up.setOnClickListener(this);
		currentIndex += 10;
//		resultList = fincControl.sortListByKey(resultList, Finc.I_APPLYDATE);
		initData();
	}

	/** 添加显示更多的 功能 */
	private void addfooteeView(ListView listView) {
		removeFooterView(listView);
		footerView = ViewUtils.getQuryListFooterView(this);
		if (footerOnclickListenner == null) {
			footerOnclickListenner = new OnClickListener() {

				@Override
				public void onClick(View v) {
					BaseHttpEngine.showProgressDialog();
					isNewQuery = false;
					fundQueryDQDE(String.valueOf(currentIndex),
							String.valueOf(pageSize), isNewQuery, fundCodeStr, transCycleStr);
				}
			};
		}
		footerView.setOnClickListener(footerOnclickListenner);
		listView.addFooterView(footerView, null, true);
	}

	private void removeFooterView(ListView listView) {
		listView.removeFooterView(footerView);
	}

	private void init() {
		View childView = mainInflater.inflate(R.layout.finc_query_dqde_list,
				null);
		tabcontent.addView(childView);
		tabcontent.setPadding(0,
				getResources().getDimensionPixelSize(R.dimen.fill_margin_left),
				0, 0);
		setTitle(R.string.finc_scheduled_available);
		//申请日期，基金名称，交易类型
		initListHeaderView(R.string.finc_applayDate, R.string.finc_fundname,
				R.string.finc_tradetype);
		listView = (ListView) childView.findViewById(R.id.query_list);
		transCycle = (TextView) findViewById(R.id.transCycle);
		fundCode = (TextView) findViewById(R.id.fundCode);
		onItemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				fincControl.fundScheduQueryMap = resultList.get(position);
				if (!StringUtil.isNullOrEmpty(fincControl.fundScheduQueryMap)) {
					//请求定投定赎明细
					String applyType = (String)fincControl.fundScheduQueryMap.get(Finc.I_TRANSTYPE);
					String fundScheduleDate = (String)fincControl.fundScheduQueryMap.get(Finc.I_APPLYDATE);
					String scheduleBuyNum = (String)fincControl.fundScheduQueryMap.get(Finc.FINC_FUNDSEQ_REQ);
					BaseHttpEngine.showProgressDialog();
					if(FincUtils.isStrEquals(applyType, "0")){//定投
						requestFundScheduledBuyDetailQuery(fundScheduleDate, scheduleBuyNum);
					}else{//定赎
						requestFundScheduledSellDetailQuery(fundScheduleDate, scheduleBuyNum);
					}
				}
			}
		};
		listView.setOnItemClickListener(onItemClickListener);
		resultList = fincControl.fundAvailableList;
		initData();
	}
	
	private void initQueryPopupWindow(){
//		view = mainInflater.inflate(R.layout.finc_dqde_query_condition, null);
//		up = view.findViewById(R.id.up);
//		tradeSycleSp = (Spinner)view.findViewById(R.id.tradeSycleSp);
//		fundCodeEt = (EditText)view.findViewById(R.id.fundCodeEt);
//		searchBtn = (Button)view.findViewById(R.id.searchBtn);
//		searchBtn.setOnClickListener(this);
//		FincUtils.initSpinnerView(this, tradeSycleSp, LocalData.transCycleMap);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(view, this).setFocusable(true);
		
		query_resultLayout = (View)findViewById(R.id.result);
		queryBeforLayoutOut = (View)findViewById(R.id.ll_query_condition);
		queryBeforLayout = (View)findViewById(R.id.condition_layout);
		up = findViewById(R.id.up);
		tradeSycleSp = (Spinner)findViewById(R.id.tradeSycleSp);
		fundCodeEt = (EditText)findViewById(R.id.fundCodeEt);
		searchBtn = (Button)findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(this);
		FincUtils.initSpinnerView(this, tradeSycleSp, LocalData.transCycleMap);
		initanimation();
		query_resultLayout.setVisibility(View.GONE);
		queryBeforLayoutOut.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void fundScheduledBuyDetailQueryCallback(Object resultObj) {
		super.fundScheduledBuyDetailQueryCallback(resultObj);
		startDetailActivity();
	}
	
	@Override
	public void fundScheduledSellDetailQueryCallback(Object resultObj) {
		super.fundScheduledSellDetailQueryCallback(resultObj);
		startDetailActivity();
	}
	
	private void startDetailActivity(){
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent();
		intent.setClass(this, FundQueryDQDEDetailsActivity.class);
		startActivityForResult(intent, 1);
	}
	
	private void initData(){
		totalNum = Integer.valueOf((String) fincControl.fundUAvailableResult
				.get(Prms.PRMS_QUERY_DEAL_RECORDNUMBER));
		if (!isNewQuery && resultList != null && apdater != null) {
			resultList.addAll(fincControl.fundAvailableList);
//			resultList = fincControl.sortListByKey(resultList, Finc.I_APPLYDATE);
			apdater.notifyDataSetChanged(resultList);
			if (resultList.size() >= totalNum) {// 全部显示完鸟
				removeFooterView(listView);
			}
		} else {
			resultList = fincControl.fundAvailableList;
//			resultList = fincControl.sortListByKey(resultList, Finc.I_APPLYDATE);
			apdater = new FundQueryDQDEListAdapter(this, resultList);
			if (totalNum > 10) {// 不能显示完
				addfooteeView(listView);
			} else {
				removeFooterView(listView);
			}
			listView.setAdapter(apdater);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.searchBtn:
			BaseHttpEngine.showProgressDialog();
			isNewQuery = true;
//			fundCodeStr = fundCodeEt.getText().toString();
//			String oldKey = transCycleStr;
//			transCycleStr = FincUtils.getKeyByValue(
//					LocalData.transCycleMap, tradeSycleSp
//					.getSelectedItem().toString());
//			if(!FincUtils.isStrEquals(oldKey, transCycleStr))
//				changeQuery = true;
//			else changeQuery = false;
			resultList = null;
			fundQueryDQDE(String.valueOf(currentIndex),
					String.valueOf(pageSize), isNewQuery, fundCodeStr, transCycleStr);
			break;
		case R.id.up:
			queryBeforLayout.startAnimation(animation_up);
			break;
		case R.id.finc_down_LinearLayout:
			queryBeforLayoutOut.setVisibility(View.VISIBLE);
			queryBeforLayoutOut.bringToFront();
			query_resultLayout.setVisibility(View.VISIBLE);
			queryBeforLayout.startAnimation(animation_down);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE:// 开通基金账户
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifhaveaccId = true;
				setResult(RESULT_OK);
				finish();
				break;
			default:
				fincControl.ifhaveaccId = false;
				getPopup();
				break;
			}
			break;
		default:
			switch (resultCode) {
			case RESULT_OK:
				setResult(RESULT_OK);
				finish();
				break;
			default:
				getPopup();
				break;
			}
		}
		
	}
	
	private void initanimation() {
		animation_up = AnimationUtils.loadAnimation(this, R.anim.scale_out);
		animation_down = AnimationUtils.loadAnimation(this, R.anim.scale_in);
		animation_up.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				query_resultLayout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				queryBeforLayoutOut.setVisibility(View.GONE);
				query_resultLayout.setVisibility(View.VISIBLE);
			}
		});
		animation_down.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				query_resultLayout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

				queryBeforLayoutOut.setVisibility(View.VISIBLE);
			}
		});

	}
}
