package com.chinamworld.bocmbci.biz.bocinvt.myproduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.SecondMainActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.adapter.OcrmProductAdapter;
import com.chinamworld.bocmbci.biz.bocinvt.inflateView.InflaterViewDialog;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.ProductDetailActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.QueryProductActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IntentSpan;
/**
 * 指令交易产品列表
 * @author panwe
 *
 */
public class OcrmProductListActivity extends BociBaseActivity{
	private View mFooterView;
	private ListView mListView;
	private OcrmProductAdapter mAdapter;
	private List<Map<String, Object>> mList = new ArrayList<Map<String,Object>>();
	private String index = ConstantGloble.LOAN_PAGESIZE_VALUE;
	private String isPre;
	private String transType;
	private String periods;

	private Boolean flag;
	private boolean isOpen = false;
	private boolean isevaluation = false;
	private boolean ocrmQuery = true;
	/** 登记账户信息 */
	private List<Map<String, Object>> investBindingInfo = new ArrayList<Map<String, Object>>();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.bocinvt_orcm_list);
		setTitle(R.string.ocrm_title);
		Intent intent = getIntent();
		flag = intent.getBooleanExtra("flag", true);
		setLeftSelectedPosition("bocinvtManager_5");
		//setBackBtnClick(backBtnClick);
		if (flag) {
			BiiHttpEngine.showProgressDialogCanGoBack();
			requestPsnInvestmentisOpenBefore();
	
		} else {
			setUpView();
		}
	}
	/** 返回点击事件 */
	OnClickListener backBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
//			ActivityTaskManager.getInstance().removeAllSecondActivity();
//			Intent intent = new Intent(OcrmProductListActivity.this,
//					SecondMainActivity.class);
//			startActivity(intent);
			goToMainActivity();

		}
	};
	@SuppressWarnings("unchecked")
	private void setUpView(){
		 List<Map<String, Object>> addList = new ArrayList<Map<String,Object>>();
		 addList=(List<Map<String, Object>>) BociDataCenter.getInstance().getOcrmMap().get(BocInvt.RESULTLIST);		 
		if(addList.size()>0){
			RelativeLayout sort_layout=(RelativeLayout)findViewById(R.id.sort_layout);
			sort_layout.setVisibility(View.VISIBLE);
			mFooterView = View.inflate(this, R.layout.epay_tq_list_more, null);
			mListView = (ListView) findViewById(R.id.listview);
			mListView.setOnItemClickListener(itenOnClick);
			mList.addAll((List<Map<String, Object>>) BociDataCenter.getInstance()
					.getOcrmMap().get(BocInvt.RESULTLIST));
			addFooterView((String) BociDataCenter.getInstance().getOcrmMap()
					.get(BocInvt.PROGRESS_RECORDNUM));
			mAdapter = new OcrmProductAdapter(this, mList);
			mListView.setAdapter(mAdapter);
			}else{
				
				
				SpannableString sp = new SpannableString(this.getString(R.string.bocinvt_orcmlist_isnull));
				final Intent userIntent = new Intent();

				userIntent.setClass(OcrmProductListActivity.this, QueryProductActivity.class);
			
				sp.setSpan(new IntentSpan(new OnClickListener() {

					public void onClick(View view) {

						OcrmProductListActivity.this.startActivity(userIntent);

					}

				}), 24, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);		
				BaseDroidApp.getInstanse().showcancheckErrorDialog(	sp,exitClick);

			}
	}
	
	@Override
	public void bocinvtAcctCallback(Object resultObj) {
		super.bocinvtAcctCallback(resultObj);
		isOpen = isOpenBefore;
		isevaluation = isevaluated;
		investBindingInfo = investBinding;
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.BOCINVT_XPADRESET_CHOOSE, investBinding);
		if (isOpenBefore && !StringUtil.isNullOrEmpty(investBinding)
				&& isevaluated) {
			
			requestCommConversationId();
//			requestOcrmProductQuery(0,"true");
		} else {
			// 得到result
			BaseHttpEngine.dissMissProgressDialog();
			InflaterViewDialog inflater = new InflaterViewDialog(
					OcrmProductListActivity.this);
			dialogView2 = (RelativeLayout) inflater.judgeViewDialog(
					isOpenBefore, investBindingInfo, isevaluated,
					manageOpenClick, invtBindingClick, invtEvaluationClick,
					exitClick);
			TextView tv = (TextView) dialogView2
					.findViewById(R.id.tv_acc_account_accountState);
			// 查询
			tv.setText(this.getString(R.string.bocinvt_query_title));

			BaseDroidApp.getInstanse().showAccountMessageDialog(dialogView2);

		}
	}


	protected OnClickListener exitClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			finish();
		}
	};
	private OnItemClickListener itenOnClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			String productCode = (String)mList.get(position).get(BocInvt.BOCI_PRODUCTCODE_REQ);
			if (productCode.substring(0,2).equals("83")) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("当前理财产品暂不支持在手机银行购买，请您前往网上银行进行操作");return;
			}
			isPre = (String)mList.get(position).get(BocInvt.ISPRE);
			transType = (String)mList.get(position).get(BocInvt.TRANSTYPE);
			periods = (String)mList.get(position).get(BocInvt.MAXPERIODS);
			requestProductDetail((String)mList.get(position).get(BocInvt.BOCI_PRODUCTCODE_REQ));
		}
	};
	
	/**
	 * 添加更多按钮
	 * @param totalCount
	 */
	private void addFooterView(String totalCount) {
		if (Integer.valueOf(totalCount) > mList.size()) {
			if (mListView.getFooterViewsCount() <= 0) {
				mListView.addFooterView(mFooterView);
			}
			mListView.setClickable(true);
		} else {
			if (mListView.getFooterViewsCount() > 0) {
				mListView.removeFooterView(mFooterView);
			}
		}
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				requestOcrmProductQuery(Integer.valueOf(index), "false");
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void responseOcrmProductQueryCallback(Object resultObj) {
		super.responseOcrmProductQueryCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (flag) {
			flag = false;
			setUpView();
		} else {
			index = String.valueOf(Integer.valueOf(index)
					+ Integer.valueOf(ConstantGloble.LOAN_PAGESIZE_VALUE));
			mList.addAll((List<Map<String, Object>>) BociDataCenter
					.getInstance().getOcrmMap().get(BocInvt.RESULTLIST));
			addFooterView((String) BociDataCenter.getInstance().getOcrmMap()
					.get(BocInvt.PROGRESS_RECORDNUM));
			mAdapter.setmList(mList);
		}
	}
	
	/**
	 * 请求产品详情
	 * @param procode
	 *            产品代码
	 */
	private void requestProductDetail(String procode) {
		BiiHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PRODUCTDETAILQUERY);
		biiRequestBody.setConversationId(BociDataCenter.getInstance().getOcrmConversationId());
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCI_PRODUCTCODE_REQ, procode);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,"requestProductDetailCallBack");
	}

	@SuppressWarnings("unchecked")
	public void requestProductDetailCallBack(Object resultObj) {
		Map<String, Object> detailMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(detailMap)) {
			BiiHttpEngine.dissMissProgressDialog(); return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_PRODUCTDETAIL_LIST, detailMap);
		if (!StringUtil.isNull(transType)) {
			if (transType.equals("05")) {
				requestBociAcctListocrm("1", "1");
			}else if(transType.equals("06")){
				BiiHttpEngine.dissMissProgressDialog();
				Intent intent = new Intent(OcrmProductListActivity.this,ProductDetailActivity.class);
				intent.putExtra(BocInvt.ISPRE, getIsPre());
				intent.putExtra(BocInvt.TRANSTYPE, transType);
				startActivityForResult(intent, ACTIVITY_BUY_CODE);
			}
		}
	}
	
	/**
	 * 请求理财账户信息
	 * 
	 * @param acctSatus
	 * @param acctType
	 */
	public void requestBociAcctListocrm(String acctSatus, String acctType) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVTACCTINFO);
		Map<String, String> map = new Hashtable<String, String>();
		map.put(BocInvt.ACCOUNTSATUS, acctSatus);
		map.put(BocInvt.QUERYTYPE, acctType);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "bocinvtAcctlistCallback");
	}

	public void bocinvtAcctlistCallback(Object resultObj) {

		BiiHttpEngine.dissMissProgressDialog();
		if (!StringUtil.isNull(getIsPre())) {
			BaseDroidApp
					.getInstanse()
					.getBizDataMap()
					.put(ConstantGloble.BOCINVT_REMAINCYCLECOUNT_STRING,
							periods);
		}
		Intent intent = new Intent(OcrmProductListActivity.this,
				ProductDetailActivity.class);
		intent.putExtra(BocInvt.ISPRE, getIsPre());
		intent.putExtra(BocInvt.TRANSTYPE, transType);
		startActivityForResult(intent, ACTIVITY_BUY_CODE);
	}
	
	/**
	 * 是否周期性产品判断
	 * @return 这里推荐产品和产品详情判断值刚好相反，ztmd恶心
	 */
	@SuppressWarnings("unchecked")
	private String getIsPre(){
		if (!StringUtil.isNull(isPre)) {
			return isPre;
		}
		Map<String, Object> map = (Map<String, Object>) BaseDroidApp.getInstanse()
		.getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCTDETAIL_LIST);
		String periodical = (String) map.get(BocInvt.BOCI_DETAILPERIODICAL_RES);
		if (!StringUtil.isNull(periodical) && periodical.equals("1")) {
			return "0";
		}else if(!StringUtil.isNull(periodical) && periodical.equals("0")){
			return "1";
		}
		return "";
	}
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
			BociDataCenter.getInstance().setOcrmConversationId(
					(String) BaseDroidApp.getInstanse().getBizDataMap()
							.get(ConstantGloble.CONVERSATION_ID));
			requestOcrmProductQuery(0, "true");
		
		
	}
}
