package com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.adapter.QueryExternalBankAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 查询开户行
 * 
 * @author
 * 
 */
public class QueryExternalBankActivity1 extends BaseActivity {

	/** 账户所属银行行号 */
	// private String accInKBankCode = null;
	/** 账户所属银行名称 */
	private String accInKBankName = null;
	/**  */
//	private String cnapsCode = null;

	
	private ListView queryResultLv = null;
	/** 用于listview显示数据 */
	private List<Map<String, String>> allBankList = new ArrayList<Map<String, String>>();

	private int currentIndex = 10;
	private int pageSize = 10;

	private QueryExternalBankAdapter adapter;

	/** 底部layout */
	private LinearLayout footLayout;
	/** 左侧菜单 */
	private Button showBtn;

	private String recordNumber;
	private Button queryBtn;

	/** 开户行银行名称 */
	private EditText inBankNameEt = null;
	/** 查询开户行名称 */
	private Button kBankBtn = null;
	/** 账户所属银行 */
	private String toOrgName = "";

	private boolean isClickMore = false;
	public LayoutInflater mInflater;
	/** 加载布局 */
	public LinearLayout tabcontent = null;

	/** 实时 */
	public boolean ISCHOOSEBANK = false;
	// 是否为加急转账
	public static final String ISSHISHITYPE = "isshishitrantype";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		initPulldownBtn(); // 加载上边下拉菜单
		initFootMenu(); // 加载底部菜单栏
//		initLeftSideList(this, LocalData.tranManagerLeftList); // 加载左边菜单栏
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		ISCHOOSEBANK = this.getIntent().getBooleanExtra(ISSHISHITYPE, false);
		mInflater = LayoutInflater.from(this);
		View view;
		if (ISCHOOSEBANK) {
			setTitle(this.getString(R.string.tran_choose_address));
			view = mInflater.inflate(
					R.layout.tran_test_payee_shishi_bank_query_kbank, null);

			allBankList = TranDataCenter.getInstance().getShishiBankList();
		} else {
			setTitle(this.getString(R.string.tran_addNewPayee_head));
			view = mInflater.inflate(
					R.layout.tran_payee_other_bank_query_kbank, null);

			allBankList = TranDataCenter.getInstance().getExternalBankList();
		}

		tabcontent.removeAllViews();
		tabcontent.addView(view);
		Button mTopRightBtn = (Button) findViewById(R.id.ib_top_right_btn);
		Button back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		mTopRightBtn.setVisibility(View.INVISIBLE);
		// 隐藏底部菜单
		footLayout = (LinearLayout) this.findViewById(R.id.foot_layout);
		footLayout.setVisibility(View.GONE);
		// 隐藏左侧菜单
		showBtn = (Button) this.findViewById(R.id.btn_show);
		showBtn.setVisibility(View.GONE);
		setLeftButtonPopupGone();
		setupView();
	}

	
	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
		Intent intent = this.getIntent();

		inBankNameEt = (EditText) findViewById(R.id.et_acc_bankname_payee_other_bank_write);
		kBankBtn = (Button) findViewById(R.id.btn_query_kbank_othbank_write);
		kBankBtn.setOnClickListener(queryListener);

		recordNumber = intent.getStringExtra(Tran.RECORD_NUMBER);
		if (intent != null) {
			accInKBankName = (String) intent
					.getStringExtra(Tran.PAYEE_BANKNAME_REQ);
			accInKBankName = (StringUtil.isNull(accInKBankName)) ? (LocalData.kBankList
					.get(0)) : accInKBankName;
//			if(intent.hasExtra(Tran.PAYEE_TOBANKCODE_REQ)){
//				cnapsCode=intent.getStringExtra(Tran.PAYEE_TOBANKCODE_REQ);
//			}
			
		}
		queryResultLv = (ListView) findViewById(R.id.lv_query_result_kbank);
		if (!StringUtil.isNullOrEmpty(recordNumber)
				&& (Integer.parseInt(recordNumber) > currentIndex)) {
			queryBtn = newMoreBtn();
			queryResultLv.addFooterView(queryBtn);
		}
		adapter = new QueryExternalBankAdapter(QueryExternalBankActivity1.this,
				allBankList);
		queryResultLv.setAdapter(adapter);
		queryResultLv.setOnItemClickListener(itemListener);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			isClickMore = true;
			if (ISCHOOSEBANK) {
				// 实时转账——所属银行模糊查询
				psnEbpsQueryAccountOfBank(currentIndex, pageSize);
			} else {
				// 转账银行开户行查询
				requestQueryExternalBank(currentIndex, pageSize);
			}
		}
	};

	private OnClickListener queryListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			isClickMore = false;
			toOrgName = inBankNameEt.getText().toString().trim();
			// 清空listview内容
			allBankList.clear();
			if (adapter != null) {
				adapter.setData(allBankList);
				queryResultLv.removeFooterView(queryBtn);
			}
			currentIndex = 0;
			if (ISCHOOSEBANK) {
				// 实时转账——所属银行模糊查询
				psnEbpsQueryAccountOfBank(0, 10);
			} else {
				// 转账银行开户行查询
				requestQueryExternalBank(0, 10);
			}
		}
	};

	private OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (allBankList != null) {
				String toOrgName = (String) allBankList.get(position).get(
						Tran.PAYEE_BANKNAME_REQ);
				String cnapsCode = "";
				if (ISCHOOSEBANK) {
					cnapsCode = (String) allBankList.get(position).get(
							Tran.EBPSQUERY_BANKCODE_RES);
				} else {
					cnapsCode = (String) allBankList.get(position).get(
							Tran.PAYEE_CNAPSCODE_REQ);
				}

				Intent intent = new Intent();
				intent.putExtra(Tran.PAYEE_CNAPSCODE_REQ, cnapsCode);
				intent.putExtra(Tran.TRANS_BOCNATIONAL_TOORGNAME_REQ, toOrgName);
				setResult(RESULT_OK, intent);
				finish();
			}
		}
	};

	/**
	 * 是否去掉底部更多按钮
	 */
	private void removeListViewFoot() {
		int rec = Integer.parseInt(recordNumber);
		if (rec < currentIndex) {
			queryResultLv.removeFooterView(queryBtn);
		}
	}

	/**
	 * 转账银行开户行查询
	 */
	public void requestQueryExternalBank(int currentIndex, int pageSize) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.QUERYEXTERNALBANKINFO);
		Map<String, String> map = new HashMap<String, String>();
		// 银行行号
//		
//		if(StringUtil.isNull(LocalData.kBankListMap.get(accInKBankName))){
////			if(StringUtil.isNull(cnapsCode)){
//				String cnapsCode="OTHER";
////			}
//			map.put(Tran.PAYEE_TOBANKCODE_REQ,cnapsCode);	
//		}else{
//			map.put(Tran.PAYEE_TOBANKCODE_REQ,
//					LocalData.kBankListMap.get(accInKBankName));
//		}
//		
		map.put(Tran.PAYEE_TOBANKCODE_REQ, "OTHER");
		for(Map.Entry<String, String>entity:LocalData.kBankListMap.entrySet()){
			if(accInKBankName.contains(entity.getKey())){
				map.put(Tran.PAYEE_TOBANKCODE_REQ,entity.getValue());
				break;
			}
		}
		// 银行名称
		map.put(Tran.PAYEE_BANKNAME_REQ, toOrgName);
		// 当前页
		map.put(Tran.PAYEE_CURRENTINDEX_REQ, currentIndex + "");
		// 每页显示条数
		map.put(Tran.PAYEE_PAGESIZE_REQ, pageSize + "");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"queryExternalBankCallBack");
	}

	/**
	 * 转账银行开户行查询
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 * @return tokenId
	 */
	@SuppressWarnings("unchecked")
	public void queryExternalBankCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		recordNumber = (String) result.get(Tran.RECORD_NUMBER);
		List<Map<String, String>> queryExternalBankList = (List<Map<String, String>>) result
				.get(ConstantGloble.LIST);
		if (StringUtil.isNullOrEmpty(queryExternalBankList)) {
			String message = this.getString(R.string.acc_transferquery_null);
			BaseDroidApp.getInstanse().showInfoMessageDialog(message);
			return;
		}
		allBankList.addAll(queryExternalBankList);
		currentIndex = allBankList.size();
		if (isClickMore) {
			removeListViewFoot();
			adapter.setData(allBankList);
		} else {
			if (queryBtn != null) {
				queryResultLv.removeFooterView(queryBtn);
			}
			if (!StringUtil.isNullOrEmpty(recordNumber)
					&& Integer.parseInt(recordNumber) > currentIndex) {
				queryBtn = newMoreBtn();
				queryBtn.setOnClickListener(listener);
				queryResultLv.addFooterView(queryBtn);
			}
			adapter = new QueryExternalBankAdapter(
					QueryExternalBankActivity1.this, queryExternalBankList);
			queryResultLv.setAdapter(adapter);
			queryResultLv.setOnItemClickListener(itemListener);
		}
	}

	/**
	 * 实时转账——所属银行模糊查询
	 */
	public void psnEbpsQueryAccountOfBank(int currentIndex, int pageSize) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.PSNEBPSQUERYACCOUNTOFBANK_API);
		Map<String, String> map = new HashMap<String, String>();
		// 银行名称
		map.put(Tran.EBPSQUERY_BANKNAME_REQ, toOrgName);
		// 当前页
		map.put(Tran.EBPSQUERY_CURRENTINDEX_REQ, currentIndex + "");
		// 每页显示条数
		map.put(Tran.EBPSQUERY_pageSize_REQ, pageSize + "");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"psnEbpsQueryAccountOfBankCallBack");
	}

	/**
	 * 实时转账——所属银行模糊查询
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	@SuppressWarnings("unchecked")
	public void psnEbpsQueryAccountOfBankCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		String recordNumber = (String) result
				.get(Tran.EBPSQUERY_RECORDNUMBER_RES);
		List<Map<String, String>> queryExternalBankList = (List<Map<String, String>>) result
				.get(Tran.EBPSQUERY_ACCOUNTOFBANKLIST_RES);
		if (StringUtil.isNullOrEmpty(queryExternalBankList)) {
			String message = this.getString(R.string.acc_transferquery_null);
			BaseDroidApp.getInstanse().showInfoMessageDialog(message);
			return;
		}
		allBankList.addAll(queryExternalBankList);
		currentIndex = allBankList.size();
		if (isClickMore) {
			removeListViewFoot();
			adapter.setData(allBankList);
		} else {
			if (queryBtn != null) {
				queryResultLv.removeFooterView(queryBtn);
			}
			if (!StringUtil.isNullOrEmpty(recordNumber)
					&& Integer.parseInt(recordNumber) > currentIndex) {
				queryBtn = newMoreBtn();
				queryBtn.setOnClickListener(listener);
				queryResultLv.addFooterView(queryBtn);
			}
			adapter = new QueryExternalBankAdapter(
					QueryExternalBankActivity1.this, queryExternalBankList);
			queryResultLv.setAdapter(adapter);
			queryResultLv.setOnItemClickListener(itemListener);
		}
	}

	private Button newMoreBtn() {
		queryBtn = new Button(this);
		queryBtn.setText(this.getResources().getString(R.string.query_more));
		queryBtn.setTextColor(this.getResources().getColor(R.color.gray));

		int height = this.getResources().getDimensionPixelSize(
				R.dimen.btn_bottom_height);
		LayoutParams lp = new LayoutParams(new LayoutParams(-1, height));
		queryBtn.setLayoutParams(lp);
		queryBtn.setBackgroundColor(this.getResources().getColor(
				R.color.transparent_00));
		queryBtn.setOnClickListener(listener);
		return queryBtn;
	}


	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}

}
