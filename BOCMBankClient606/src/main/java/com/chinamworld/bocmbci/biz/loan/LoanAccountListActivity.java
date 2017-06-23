package com.chinamworld.bocmbci.biz.loan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanAccountAdapter;
import com.chinamworld.bocmbci.biz.loan.inflaterDialogView.Loaninflaterdialog;
import com.chinamworld.bocmbci.biz.loan.loanApply.LoanApplyMenuActivity;
import com.chinamworld.bocmbci.biz.loan.loanQuery.LoanQueryMenuActivity;
import com.chinamworld.bocmbci.biz.loan.loanRepay.LoanRepayMenuActivity;
import com.chinamworld.bocmbci.biz.loan.loanUse.LoanUseMenuActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.ErrorCode;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.BaseSwipeListViewListener;
import com.chinamworld.bocmbci.widget.SwipeListView;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 贷款管理账户列表
 * 
 * @author wangmengmeng
 * 
 */
public class LoanAccountListActivity extends LoanBaseActivity {
	/** 贷款账号列表 */
	private SwipeListView lvLoan;
	/** 贷款列表内容 */
	private List<Map<String, Object>> loanlist = null;
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示
	/** 贷款账户信息页面 */
	private View view;
	/** 贷款账户信息 */
	private Map<String, Object> loanmap = new HashMap<String, Object>();
	/** 点击还款测算标记 */
	private int clickid = 0;
	/** 添加底部提示语 */
	private View ll_add_bottom;
	private TextView tv_add_bottom;
	/** 变更还款账户信息 */
	private Map<String, String> loanChangeRepayAccmap;
	/**实际还款账户*/
	public static  String  cardAccunt;
	/**用户点击的item位置*/
	private int clickPosition = 0;
	private List<Map<String, String>> loanAccountList = null;
	private String payAccountFlag=null;
	/**贷款品种*/
     public static String currencyCode =null;
     /**贷款品种*/
     public static String hCurrencyCode =null;
     public static String issuerepayInterest=null;
     /**贷款币种*/
     public static  String currency=null;
     /**还款方式*/
     public static   String interestType=null;
     /**还款周期*/
     public static   String loanRepayPeriod=null;
      /**贷款剩余期数*/
      String remainIssue=null;
      /**贷款账户*/
      String loan_account;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//包含向右滑动的listview 目的是屏蔽左侧菜单的滑动事件
		setContainsSwipeListView(true);
		setContentView(R.layout.biz_activity_layout);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		// 为界面标题赋值
		setTitle(this.getString(R.string.loan_myloan));
		// 隐藏左侧展示按钮
		visible();
		Button btn_right = (Button) findViewById(R.id.ib_top_right_btn);
		btn_right.setVisibility(View.VISIBLE);
		view = (View) LayoutInflater.from(this).inflate(
				R.layout.loan_account_list, null);
		tabcontent.addView(view);
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化底部菜单栏
		initFootMenu();
		// 初始化左边菜单
		initLeftSideList(this, LocalData.loanLeftMenuList);
		// 初始化界面
		init();
		if(LoanAdvanceCountMatchActivity.loanAmount==true){
			BaseHttpEngine.showProgressDialog();
			requestPsnLOANAdvanceRepayAccountQuery();
		}
	}
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		String menuId = menuItem.MenuID;
		if(menuId.equals("loan_1")){// 贷款管理
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanQueryMenuActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						LoanQueryMenuActivity.class);
				context.startActivity(intent);
			}
		}
		else if(menuId.equals("loan_2")){// 贷款用款
				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanUseMenuActivity)) {
					ActivityTaskManager.getInstance().removeAllActivity();
					Intent intent = new Intent();
					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
							LoanUseMenuActivity.class);
					context.startActivity(intent);
				}
						
		}
		else if(menuId.equals("loan_3")){// 贷款还款
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanRepayMenuActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						LoanRepayMenuActivity.class);
				context.startActivity(intent);
			}
			
		}
		else if(menuId.equals("loan_4")){// 贷款在线申请
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanApplyMenuActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanApplyMenuActivity.class);
				context.startActivity(intent);
			}
			
		}
		return true;
		
//		
//		super.setSelectedMenu(clickIndex);
//		switch (clickIndex) {
//		case 0:// 贷款管理
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanQueryMenuActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanQueryMenuActivity.class);
//				startActivity(intent);
//			}
//			break;
//		case 1:// 贷款用款
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanUseMenuActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanUseMenuActivity.class);
//				startActivity(intent);
//			}
//			break;
//		case 2:// 贷款还款
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanRepayMenuActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanRepayMenuActivity.class);
//				startActivity(intent);
//			}
//			break;
//		case 3:// 贷款在线申请
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanApplyMenuActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanApplyMenuActivity.class);
//				startActivity(intent);
//			}
//			break;
//
//		default:
//			break;
//		}
	}
	/** 初始化界面 */
	private void init() {
		ll_add_bottom = LayoutInflater.from(this).inflate(
				R.layout.loan_account_add_bottom, null);
		tv_add_bottom = (TextView) view.findViewById(R.id.tv_add_bottom);
		lvLoan = (SwipeListView) view.findViewById(R.id.loan_accountlist);
		lvLoan.addFooterView(ll_add_bottom, null, false);
		// ll_add_bottom.setClickable(false);
		// 请求贷款账户列表信息
		requestLoanList();

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

	/** 请求贷款账户列表信息 */
	public void requestLoanList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_ACCOUNT_LIST_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOANACC_E_LOAN_STATE, "10");
		biiRequestBody.setParams(map);
//		BaseHttpEngine.showProgressDialog();
		BaseHttpEngine.showProgressDialogCanGoBack();
		HttpManager.requestBii(biiRequestBody, this, "loanAccountListCallBack");
	}

	/**
	 * 请求贷款账户列表回调
	 * 
	 * @param resultObj
	 */
	public void loanAccountListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> loanlistmap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(loanlistmap)) {
			return;
		}
		loanlist = (List<Map<String, Object>>) (loanlistmap
				.get(Loan.LOANACC_LOAN_LIST_RES));
		if (loanlist == null || loanlist.size() == 0) {
			BaseDroidApp.getInstanse().showMessageDialog(
					this.getString(R.string.loan_null_toast),
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
						}
					});
			return;
		}
		tv_add_bottom.setVisibility(View.GONE);
		LoanAccountAdapter adapter = new LoanAccountAdapter(this,
				R.layout.loan_account_list_item, loanlist);
		lvLoan.setLastPositionClickable(false);
		lvLoan.setAdapter(adapter);
		lvLoan.setAllPositionClickable(true);
		lvLoan.setSwipeListViewListener(swipeListViewListener);
		}
	    /**
	    * 根据主账户查询对应的借记卡卡号
	    * @param acctNum:主账户
	    */
	public void requestPsnQueryCardNumByAcctNum(String acctNum) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.PSN_LOAN_PSNQUERYCARDNUMBYACCTNUM_API);
		biiRequestBody.setConversationId(null);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOAN_ACCTNUM_REQ, acctNum);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnQueryCardNumByAcctNumCallback");
	}
	/** 根据主账户查询对应的借记卡卡号---回调*/
	public void requestPsnQueryCardNumByAcctNumCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		//cardNum = (String) (biiResponseBody.getResult());
		//payAccountNumber 拿的这个字段有问题
		cardAccunt=(String) (biiResponseBody.getResult());
		requestPsnLOANAdvanceRepayAccountDetailQuery(loan_account);
		
	}

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
			if (action == 0) {
				LoanDataCenter.getInstance().setLoanmap(loanlist.get(position));
				hCurrencyCode = String.valueOf(loanlist.get(position).get(Loan.LOANACC_CURRENCYCODE_RES));
				Intent intent = new Intent(LoanAccountListActivity.this,
						LoanQueryActivity.class);
				startActivity(intent);
			}
		}

		@Override
		public void onStartClose(int position, boolean right) {
		}

		@Override
		public void onClickFrontView(int position) {
			loanAccountMessageClick.onItemClick(null, lvLoan, position,
					position);
		}

		@Override
		public void onClickBackView(int position) {
		}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {

		}
	};
	/** list列表编辑按钮点击事件 */
	OnItemClickListener loanAccountMessageClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (id == -1) {
				// 点击的是HeaderView或者是FooterView
				return;
			}
			clickPosition=position;
			BiiHttpEngine.dissMissProgressDialog();
			loan_account = (String) loanlist.get(position).get(
					Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES);
			//605字段接口名修改
			String  accountNum = String.valueOf(loanlist.get(clickPosition).get(Loan.LOAN_CYCLE_REPAYACCOUNT));
			 currencyCode = String.valueOf(loanlist.get(clickPosition).get(Loan.LOANACC_CURRENCYCODE_RES));
			 interestType=String.valueOf(loanlist.get(clickPosition).get(Loan.LOANACC_INTERESTTYPE_RES));
			 requestPsnQueryCardNumByAcctNum(accountNum);
			
			
		}

	};
	
	/**显示dialog窗口*/
	private void showLoanMessageDialog(){
		
		loanmap = loanlist.get(clickPosition);
		LoanDataCenter.getInstance().setLoanmap(loanmap);
		Loaninflaterdialog accmessagedialog = new Loaninflaterdialog(
				LoanAccountListActivity.this);
		View accmessageView = accmessagedialog.initLoanMessageDialogView(
				loanlist.get(clickPosition), goAdvanceClick, exitDetailClick);
		BaseDroidApp.getInstanse().showLoanMessageDialog(accmessageView);
		issuerepayInterest=(String)loanmap.get(Loan.LOANACC_THISISSUEREPAYINTEREST_RES);
		remainIssue = (String) loanmap.get(Loan.LOANACC_REMAINISSUE_RES);
		currency = (String) loanmap.get(Loan.LOANACC_CURRENCYCODE_RES);
	
	}
	
	
	
	/** 退出项情况监听事件 */
	OnClickListener exitDetailClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
		}
	};
	/** 进入提前还款试算页 */
	OnClickListener goAdvanceClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//本期应还款总额
			String issuerepayAmount =
		StringUtil.parseStringCodePattern(currency, String.valueOf(loanmap.get(Loan.LOANACC_THISISSUEREPAYAMOUNT_RES)), 2);
			if(Integer.valueOf(remainIssue)>0){
			}else{
			//本期截止当前应还利息
			String issuerepayInterest = 
					StringUtil.parseStringCodePattern(currency,String.valueOf(loanmap.get(Loan.LOANACC_THISISSUEREPAYINTEREST_RES)), 2);
			if("0.00".equals(issuerepayAmount)||"0.00".equals(issuerepayInterest)||"0".equals(issuerepayAmount)||"0".equals(issuerepayInterest)){
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getResources().getString(
										R.string.loan_cannot_meet));
						return;
					}
			}	
			clickid = 1;
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();

		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestOverDue();
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {
			if (clickid == 1) {// 返回的是错误码
				if (biiResponseBody.getError().getCode()
						.equals(ErrorCode.LOAN_NO_OVERDUE)) {
					BiiHttpEngine.dissMissProgressDialog();
					BaseDroidApp.getInstanse().dismissMessageDialog();
					Intent intent = new Intent(LoanAccountListActivity.this,
							LoanAdvanceCountActivity.class);
					intent.putExtra(Loan.LOAN_CURRENCYCODE_RES, currency);
					startActivity(intent);
					return true;
				}
			}
		}
		return super.httpRequestCallBackPre(resultObj);

	}
	/** 提前还款账户查询 */
	private void requestPsnLOANAdvanceRepayAccountQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNLOANADVANCEREPAYACCOUNTQUERY_API);
		String conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		biiRequestBody.setConversationId(conversationId);
		Map<String, String> params = new HashMap<String, String>();
		params.put(Loan.LOANACC_E_LOAN_STATE, "10");
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANAdvanceRepayAccountQueryCallback");
	}
	
	public void requestPsnLOANAdvanceRepayAccountQueryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, String>> loanAccountList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(loanAccountList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		
		BaseHttpEngine.dissMissProgressDialog();
	}


	/** 请求逾期还款信息 */
	public void requestOverDue() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_OVERDUE_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Loan.OVERDUE_LOAN_ACTNUM_REQ, String.valueOf(loanmap
				.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES)));
		paramsmap.put(Loan.OVERDUE_LOAN_CURRENTINDEX_REQ,
				String.valueOf(ConstantGloble.LOAN_CURRENTINDEX_VALUE));
		paramsmap.put(Loan.OVERDUE_LOAN_PAGESIZE_REQ,
				String.valueOf(ConstantGloble.LOAN_PAGESIZE_VALUE));
		paramsmap.put(Loan.OVERDUE_LOAN_REFRESH_REQ,
				String.valueOf(ConstantGloble.LOAN_REFRESH_FALSE));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestOverDueCallBack");
	}

	/**
	 * 请求逾期还款信息
	 * 
	 * @param resultObj
	 */
	public void requestOverDueCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			Intent intent = new Intent(LoanAccountListActivity.this,
					LoanAdvanceCountActivity.class);
			intent.putExtra(Loan.LOAN_CURRENCYCODE_RES, currency);
			startActivity(intent);
			
		}else{
		/*	System.out.println("11111111111111111111111");
		Map<String, Object> overduemap = (Map<String, Object>) resultMap
				.get(Loan.OVERDUE_LOAN_OVERDUELOANOBJ_RES);
		if (StringUtil.isNullOrEmpty(overduemap)) {
			return;
		}*/
		List<Map<String, Object>> overDueList = (List<Map<String, Object>>) (resultMap
				.get(Loan.OVERDUE_LOAN_OVERDUELIST_RES));
		if (overDueList == null || overDueList.size() == 0) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			Intent intent = new Intent(LoanAccountListActivity.this,
					LoanAdvanceCountActivity.class);
			intent.putExtra(Loan.LOAN_CURRENCYCODE_RES, currency);
			startActivity(intent);
		} else {
			// 不可进行测算
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.loan_cannot_overdue));
			return;
		}
		
		}
	}
	/** 提前还款贷款账户详情查询*/
	private void requestPsnLOANAdvanceRepayAccountDetailQuery(String loanAccount) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNLOANADVANCEREPAYACCOUNTDETAILQUERY_API);
		biiRequestBody.setConversationId(null);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Loan.LOAN_LOANACCOUNT_REQ, loanAccount);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANAdvanceRepayAccountDetailQueryCallback");

	}
	/** 提前还款贷款账户详情查询-----回调 */
	public void requestPsnLOANAdvanceRepayAccountDetailQueryCallback(Object resultObj) {
//		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}else{
			loanRepayPeriod=(String)result.get(Loan.LOAN_REPAYPERIOD_REO);
			interestType=(String)result.get(Loan.LOAN_INTTYPETERES_REQ);
		}
		showLoanMessageDialog();
	}
	
	//TODO 拦截错误信息
		public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
			List<BiiResponseBody> biiResponseBodyList = response.getResponse();
		
			if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
				for (BiiResponseBody body : biiResponseBodyList) {
					if (!ConstantGloble.STATUS_SUCCESS.equals(body.getStatus())) {
						// 消除通信框
						BaseHttpEngine.dissMissProgressDialog();
						if (Login.LOGOUT_API.equals(body.getMethod())) {// 退出的请求

							return false;
						}
						BiiError biiError = body.getError();
						// 判断是否存在error
						if (biiError != null) {
							//过滤错误
							//LocalData.Code_Error_Message.errorToMessage(body);
							if(Loan.LOAN_ACCOUNT_LIST_API.equals(body.getMethod())){
								if("validation.no.relating.acc".equals(biiError.getCode())){
									biiError.setMessage("无符合查询条件的记录");
								}
							}
							
							
							
							if (biiError.getCode() != null) {
								if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
																							// 要重新登录
									// TODO 错误码是否显示"("+biiError.getCode()+") "
									showTimeOutDialog(biiError.getMessage());
//									BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(),
//											new OnClickListener() {
//
//												@Override
//												public void onClick(View v) {
//													BaseDroidApp.getInstanse().dismissErrorDialog();
//													ActivityTaskManager.getInstance().removeAllActivity();
////													Intent intent = new Intent();
////													intent.setClass(LoanAccountListActivity.this, LoginActivity.class);
////													startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//													BaseActivity.getLoginUtils(LoanAccountListActivity.this).exe(new LoginTask.LoginCallback() {
//
//														@Override
//														public void loginStatua(boolean isLogin) {
//
//														}
//													});
//												}
//											});

								} else {// 非会话超时错误拦截
									BaseDroidApp.getInstanse().createDialog(biiError.getCode(), biiError.getMessage(),
											new OnClickListener() {

												@Override
												public void onClick(View v) {
													BaseDroidApp.getInstanse().dismissErrorDialog();
													if (BaseHttpEngine.canGoBack) {
														finish();
														BaseHttpEngine.canGoBack = false;
													}
												}
											});
								}
								return true;
							}
							// 弹出公共的错误框
							BaseDroidApp.getInstanse().dismissErrorDialog();
							BaseDroidApp.getInstanse().createDialog("", biiError.getMessage(), new OnClickListener() {
								@Override
								public void onClick(View v) {
									BaseDroidApp.getInstanse().dismissErrorDialog();
									if (BaseHttpEngine.canGoBack) {
										finish();
										BaseHttpEngine.canGoBack = false;
									}
								}
							});
						} else {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							// 避免没有错误信息返回时给个默认的提示
							BaseDroidApp.getInstanse().createDialog("", getResources().getString(R.string.request_error),
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											BaseDroidApp.getInstanse().dismissErrorDialog();
											if (BaseHttpEngine.canGoBack) {
												finish();
												BaseHttpEngine.canGoBack = false;
											}
										}
									});
						}

						return true;
					}
				}
			}

			return false;
		}
}
