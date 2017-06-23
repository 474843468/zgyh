package com.chinamworld.bocmbci.biz.loan.loanApply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanDataCenter;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanApplyQueryAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.BaseSwipeListViewListener;
import com.chinamworld.bocmbci.widget.SwipeListView;

/**
 * 贷款申请查询列表
 * 
 * @author dxd
 * 
 */
public class LoanApplyQueryChooseActivity extends LoanBaseActivity {
	/** 贷款账号列表 */
	private SwipeListView lvLoan;
	LoanApplyQueryAdapter adapter;
	/** 贷款列表内容 */
	private List<Map<String, Object>> loanlist = null;
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示
	/** 贷款申请信息页面 */
	private View view;
	/** 贷款账户信息 */
	private Map<String, Object> loanmap = new HashMap<String, Object>();
	/** 点击还款测算标记 */
	private int clickid = 0;
	/** 当前请求list数据的index */
	private int mCurrentIndex = 0;
	private boolean needShow;
	/** 总条目数 */
	private int totalCount = 0;
	private String totalCounts=null;
	/** 添加底部提示语 */
	private View ll_add_bottom;
	private TextView tv_add_bottom;
	/** 变更还款账户信息 */
	private Map<String, String> loanChangeRepayAccmap;
	/**实际还款账户*/
	public static  String  cardAccunt;
	private List<Map<String, Object>> loanApplyList = null;
	/**存储 PsnOnLineLoanDetailQry 接口中返回的结果*/
	private Map<String, String> loanApplyMap= null;
	//贷款编号, commConversationId
	private String loanNumber,commConversationId;
	// 用户输入的资料
	private String infoNames, infoIphones, infoEmails;
	
	View viewFooter ;
	Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//包含向右滑动的listview 目的是屏蔽左侧菜单的滑动事件
		setContainsSwipeListView(true);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		// 为界面标题赋值
		setTitle(R.string.loan_apply_three_new);
		loanApplyList=(List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_RESULT);
		commConversationId = getIntent().getStringExtra("commConversationId");
		totalCounts = getIntent().getStringExtra(ConstantGloble.APPLY_TOTALCOUNTS);
		infoNames = getIntent().getStringExtra(ConstantGloble.APPLY_INFONAMES);
		infoIphones = getIntent().getStringExtra(ConstantGloble.APPLY_INFOIPHONES);
		infoEmails = getIntent().getStringExtra(ConstantGloble.APPLY_INFOEMAILS);
		// 隐藏左侧展示按钮
		visible();
		Button btn_right = (Button) findViewById(R.id.ib_top_right_btn);
		btn_right.setVisibility(View.VISIBLE);
		view = (View) LayoutInflater.from(this).inflate(
				R.layout.loan_apply_account_list, null);
		tabcontent.addView(view);
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化底部菜单栏
		initFootMenu();
		// 初始化左边菜单
		initLeftSideList(this, LocalData.loanLeftMenuList);
		// 初始化界面
		init();
		setAdater();
	}
	
	/** 初始化界面 */
	private void init() {
		lvLoan = (SwipeListView) view.findViewById(R.id.acc_accountlist);
		totalCount = Integer.parseInt(totalCounts);
	}

	private void setAdater() {
		 adapter = new LoanApplyQueryAdapter(this,
				R.layout.loan_apply_list_item,loanApplyList);
		
		 viewFooter = (View) LayoutInflater.from(this).inflate(
				R.layout.acc_load_more, null);
		 btn=(Button) viewFooter.findViewById(R.id.btn_load_more);
		
		 //viewFooter.setOnClickListener(clickListener);
		if (loanApplyList.size() >= totalCount) {
			lvLoan.removeFooterView(viewFooter);
		} else {
			if (lvLoan.getFooterViewsCount() > 0) {

			} else {
				lvLoan.addFooterView(viewFooter);
			}
		}
			lvLoan.setAdapter(adapter);
			lvLoan.setLastPositionClickable(true);
			lvLoan.setAllPositionClickable(true);
			lvLoan.setSwipeListViewListener(swipeListViewListener);
			
			viewFooter.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				
				@Override
				public void onGlobalLayout() {
					if( viewFooter.getParent() != null){
						 viewFooter.getParent().requestDisallowInterceptTouchEvent(true);
					}
				}
			});
//		
//			viewFooter.postDelayed(new Runnable() {
//				
//				@Override
//				public void run() {
//					if( viewFooter.getParent() != null){
//						 viewFooter.getParent().requestDisallowInterceptTouchEvent(true);
//					}
//				}
//			},2000);
			
			 viewFooter.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
					LogGloble.d("LoanApplyQueryChooseActivity","viewFooter ");
					if( viewFooter.getParent() != null){
						 viewFooter.getParent().requestDisallowInterceptTouchEvent(true);
					}
					
					switch (paramMotionEvent.getAction()) {
					case MotionEvent.ACTION_UP:
						 requestOnLineLoanAppliedQry(infoNames,infoIphones, infoEmails,
								commConversationId,true);
						break;

					default:
						break;
					}
					return true;
				}
			});
	}

	/** 隐藏左侧展示按钮 */
	private void visible() {
		Button btnhide = (Button) findViewById(R.id.btn_show);
		btnhide.setVisibility(View.GONE);
		Button back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
    OnClickListener clickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			requestOnLineLoanAppliedQry(infoNames,infoIphones, infoEmails,
					commConversationId,true);
		}
	};
	/** 列表向右滑动事件 */
	BaseSwipeListViewListener swipeListViewListener = new BaseSwipeListViewListener() {
		@Override
		public void onOpened(int position, boolean toRight) {
		}

		@Override
		public void onClosed(int position, boolean fromRight) {
		}

		@Override
		public void onListChanged() {
		}

		@Override
		public void onMove(int position, float x) {
		}

		@Override
		public void onStartOpen(int position, int action, boolean right) {

		 loanNumber=(String) loanApplyList.get(position).get(Loan.LOAN_APPLY_LOANNUMBER_API);
				requestPsnOnLineLoanDetailQry(loanNumber,commConversationId);	
		}

		@Override
		public void onStartClose(int position, boolean right) {
		}

		@Override
		public void onClickFrontView(int position) {
			
		}

		@Override
		public void onClickBackView(int position) {
			LogGloble.i("info", "onClickBackView");
		}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {

		}
	};
	/** 贷款申请---查询贷款记录列表 */
	public void requestOnLineLoanAppliedQry(String name, String appPhone,
			String AppEmail,String conversationId, boolean isRefresh) {
		if (isRefresh) {
			mCurrentIndex = 0;
		} else {
			mCurrentIndex += Third.PAGESIZE;
		}
		needShow = isRefresh;
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNONLINELOANAPPLIEDQRY_API);
		biiRequestBody.setConversationId(conversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOAN_APPLY_NAME_API, name);
		if(!StringUtil.isNullOrEmpty(appPhone)){
			map.put(Loan.LOAN_APPLY_APPPHONE_API, appPhone);
		}
		if(!StringUtil.isNullOrEmpty(AppEmail)){
			map.put(Loan.LOAN_APPLY_APPEMAIL_API, AppEmail);
		}
		map.put(PubConstants.PUB_QUERY_FEILD_PAGE_SIZE,
				String.valueOf(Third.PAGESIZE));
		map.put(PubConstants.PUB_QUERY_FEILD_CURRENT_INDEX,
				EpayUtil.getString(mCurrentIndex, "0"));
		map.put(PubConstants.PUB_QUERY_FEILD_IS_REFRESH,
				EpayUtil.getString(isRefresh, "true"));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,"requestOnLineLoanAppliedQryCallback");
	}

	public void requestOnLineLoanAppliedQryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap=  (Map<String, Object>) biiResponseBody.getResult();
		List<Map<String, Object>> resultList=new ArrayList<Map<String,Object>>();
	    totalCounts=	(String) resultMap.get(Loan.LOAN_APPLY_RECORDNUMBER_QRY);
		resultList=(List<Map<String, Object>>)resultMap.get("list");
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(
							R.string.acc_transferquery_no_limit_null));
			return;
		}
		for (int i = 0; i < resultList.size(); i++) {
			loanApplyList.add((Map<String, Object>) resultList.get(i));
		}
		if (loanApplyList.size() >= totalCount) {
			lvLoan.removeFooterView(viewFooter);
		} else {
			if (lvLoan.getFooterViewsCount() > 0) {

			} else {
				lvLoan.addFooterView(viewFooter);
			}
		}
		adapter.changeData(loanApplyList);
		BaseHttpEngine.dissMissProgressDialog();
	}
	
	/**查询单笔贷款记录详情*/
	public void requestPsnOnLineLoanDetailQry(String loanNumber,String commConversationId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		BaseHttpEngine.showProgressDialog();
		biiRequestBody.setMethod(Loan.LOAN_PSNONLINELOANDETAIL_QRY);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Loan.LOAN_APPLY_LOANNUMBER_API, loanNumber);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnOnLineLoanDetailQryCallback");
	}

	/** 查询单笔贷款记录详情------回调 */
	public void requestPsnOnLineLoanDetailQryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		LoanDataCenter.getInstance().setLoanApplymap(result);
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(LoanApplyQueryChooseActivity.this,
				LoanApplyQueryDetailActivity.class);
		startActivity(intent);
	}
	

}
