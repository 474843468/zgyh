package com.chinamworld.bocmbci.biz.finc.orcm;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.finc_p606.FundPricesActivity;
import com.chinamworld.bocmbci.biz.finc.fundprice.FundPricesActivityNew;
import com.chinamworld.bocmbci.biz.finc.orcm.adapter.OcrmProductListAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

import java.util.List;
import java.util.Map;

/***
 * 我的基金 指令交易
 * 
 * @author fsm
 * 
 */
public class OrcmProductListActivity extends FincBaseActivity {
	/** 基金推荐主view */
	private View myFincView = null;
	/** 推荐信息 */
	private ListView balanceListView = null;

	private OnItemClickListener itermClickListener;
	private OcrmProductListAdapter adapter;
	private List<Map<String, Object>> resultList;

	private boolean isNewQuery;
	private int currentIndex = 10;
	private int totalNum;
	private View footerView;
	private OnClickListener footerOnclickListenner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!FincControl.isRecommend) {
			BaseHttpEngine.showProgressDialogCanGoBack();
			//			requestCommConversationId();
			doCheckRequestPsnInvestmentManageIsOpen();
		}
		// 初始化控件
		init();
	}

	@Override
	public void doCheckRequestQueryInvtBindingInfoCallback(
			Object resultObj) {
		super.doCheckRequestQueryInvtBindingInfoCallback(resultObj);
		if(!fincControl.ifhaveaccId || !fincControl.ifInvestMent) return;
		requestCommConversationId();
	}


	/**
	 * 初始化控件
	 */
	private void init() {
		tabcontent.setPadding(0, 0, 0, 0);
		setRightToMainHome();
		myFincView = mainInflater.inflate(R.layout.finc_myfinc_balance_main,
				null);
		tabcontent.addView(myFincView);
		findViewById(R.id.recommendation_link).setVisibility(View.GONE);
		setTitle(getResources().getString(R.string.finc_ocrm_product));
		initListHeaderView(R.string.finc_fundname, R.string.finc_title_jybz,
				R.string.finc_title_dwjz);
		balanceListView = (ListView) findViewById(R.id.finc_listView);
		itermClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!StringUtil.isNullOrEmpty(resultList)) {
					fincControl.OcrmProductDetailMap = resultList.get(position);
					BaseHttpEngine.showProgressDialog();
					//					getFundDetailByFundCode((String)fincControl.OcrmProductDetailMap.get(Finc.PRODUCTCODE));
					//					fundCode = (String) fincControl.OcrmProductDetailMap.get(Finc.FINC_FUNDCODE);
					getFundDetailByFundCode((String)fincControl.OcrmProductDetailMap.get(Finc.PRODUCTCODE));

				}

			}
		};
		balanceListView.setOnItemClickListener(itermClickListener);
		if (fincControl.OcrmProductMap != null) {
			initData();
		}
	}


	/**当前点击的基金代码*/
	//	private String fundCode = null;

	@Override
	public void getFundDetailByFundCodeCallback(Object resultObj) {
		super.getFundDetailByFundCodeCallback(resultObj);
		//		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(map)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		fincControl.fincFundBasicDetails = map;
		fincControl.OcrmProductDetailMap.put(Finc.FINC_FUNDINFO, map);

		BaseHttpEngine.dissMissProgressDialog();

		Intent intent = new Intent(OrcmProductListActivity.this,
				OrcmProductDetailActivity.class);
		startActivityForResult(intent, 1);
	}


	/**
	 * 基金基本信息查询  回调处理
	 */
	//	@Override
	//	public void getFincFundCallback(Object resultObj) {
	//		super.getFincFundCallback(resultObj);
	//		BiiResponse biiResponse = (BiiResponse) resultObj;
	//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	//		fincControl.fundDetails= (Map<String, Object>) biiResponseBody
	//				.getResult();
	//		BaseHttpEngine.dissMissProgressDialog();
	//
	//		Intent intent = new Intent(OrcmProductListActivity.this,
	//				OrcmProductDetailActivity.class);
	//		startActivityForResult(intent, 1);
	//
	//	}


	private void initData(){
		totalNum = Integer.valueOf((String) fincControl.OcrmProductMap
				.get(Finc.FINC_QUERYHISTORY_RECORDNUMBER));
		if (!isNewQuery && resultList != null && adapter != null) {
			resultList.addAll(fincControl.OcrmProductList);
			adapter.notifyDataSetChanged(resultList);
			if (resultList.size() >= totalNum) {// 全部显示完鸟
				removeFooterView(balanceListView);
			}
		} else {
			resultList = fincControl.OcrmProductList;
			adapter = new OcrmProductListAdapter(this, resultList);
			if (totalNum > 10) {// 不能显示完
				addfooteeView(balanceListView);
			} else {
				removeFooterView(balanceListView);
			}
			balanceListView.setAdapter(adapter);
		}
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
					requestPsnOcrmProductQuery("01", "", ConstantGloble.FOREX_PAGESIZE, String.valueOf(currentIndex), true);
				}
			};
		}
		footerView.setOnClickListener(footerOnclickListenner);
		listView.addFooterView(footerView, null, true);
	}

	@Override
	public void ocrmProductQueryCallBack(Object resultObj) {
		super.ocrmProductQueryCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();

		if(StringUtil.isNullOrEmpty(fincControl.OcrmProductList)){
		String head = "您的理财经理还没有向你推荐合适的基金产品，请点击";
		String middle = "这里";
		String end = "查看更多。";

			View.OnClickListener l = new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					Intent intent =new Intent(OrcmProductListActivity.this, FundPricesActivity.class/*FundPricesActivityNew.class*/);
					startActivity(intent);
					ActivityTaskManager.getInstance().removeAllActivity();
					finish();
					FincControl.recommendEnterMarket = true;
				}
			};

			Clickable  c =new Clickable(l);

			SpannableString sp = new SpannableString(head + middle + end);

			sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), head.length(),
					head.length() + middle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			sp.setSpan(c, head.length(), head.length() + middle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			BaseDroidApp.getInstanse().showFundEmptyDialog(sp, onClick);	

			return;
		}
		currentIndex += 10;
		initData();
	}


	OnClickListener  onClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};


	class Clickable extends ClickableSpan implements OnClickListener{
		private final View.OnClickListener mListener;

		public Clickable(View.OnClickListener l){
			mListener = l;
		}

		@Override
		public void onClick(View v){
			mListener.onClick(v);
		}
	}

	private void removeFooterView(ListView listView) {
		listView.removeFooterView(footerView);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if(isTaskRoot()){

			}else{
				setResult(RESULT_OK);
				finish();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPsnOcrmProductQuery("01", "", ConstantGloble.FOREX_PAGESIZE,
				"0", true);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		FincControl.isRecommend = false;
		fincControl.cleanAllData();

	}

}
