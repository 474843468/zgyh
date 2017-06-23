package com.chinamworld.bocmbci.biz.loan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.loanApply.LoanApplyMenuActivity;
import com.chinamworld.bocmbci.biz.loan.loanQuery.LoanQueryMenuActivity;
import com.chinamworld.bocmbci.biz.loan.loanRepay.LoanRepayMenuActivity;
import com.chinamworld.bocmbci.biz.loan.loanUse.LoanUseMenuActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 贷款提前还款测算对比
 * 
 * @author wangmengmeng
 * 
 */
public class LoanAdvanceCountMatchActivity extends BaseActivity {
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示
	/** 贷款账户提前还款试算信息对比页面 */
	private View view;
	/** 原还款金额 */
	private TextView tv_repayAmount;
	/** 提前还款后金额 */
	private TextView tv_repayAmountInAdvance;
	/** 提前还款后本金 */
	private TextView tv_capitalInAdvance;
	/** 提前还款后利息 */
	private TextView tv_interestInAdvance;
	/** 提前还款后每期金额 */
	private TextView tv_everyTermAmount;
	/** 原期数2 */
	private TextView tv_totalIssue2;
	/** 提前还款后剩余期数2 */
	private TextView tv_remainIssueforAdvance2;
	/** 剩余还款额（本金+利息）2 */
	private TextView tv_remainAmount2;
	/** 贷款账户信息 */
	Map<String, Object> loanmap = new HashMap<String, Object>();
	/** 第一个测算结果 */
	Map<String, Object> advanceresult = new HashMap<String, Object>();
	/** 第一个测算条件 */
	Map<String, String> advancecondition = new HashMap<String, String>();
	//币种
	String currency=null;
	/** 原测算方式 */
	private String type;
	/** 确定按钮 */
	private Button btnSure;
	/**还款方式*/
	private  String interestType ;
	/** 原还款金额 */
	private LinearLayout ll_repayAmount;
	/** 提前还款后每期金额 */
	private LinearLayout ll_everyTermAmount;
	public static boolean loanAmount=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		// 为界面标题赋值
		setTitle(this.getString(R.string.btnGoAdvanced));
		// 隐藏左侧展示按钮
		visible();
		view = (View) LayoutInflater.from(this).inflate(
				R.layout.loan_repay_match, null);
		tabcontent.addView(view);
		// 获取贷款账户信息
		loanmap = LoanDataCenter.getInstance().getLoanmap();
		//获取币种
		currency=(String)loanmap.get(Loan.LOANACC_CURRENCYCODE_RES);
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化底部菜单栏
		initFootMenu();
		// 初始化左边菜单
		initLeftSideList(this, LocalData.loanLeftMenuList);
		// 界面初始化
		init();
	}
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		String menuId = menuItem.MenuID;
		if(menuId.equals("loan_1")){// 贷款用款
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanUseMenuActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanUseMenuActivity.class);
				context.startActivity(intent);
			}
			
		}
		else if(menuId.equals("loan_2")){// 贷款还款
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanRepayMenuActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanRepayMenuActivity.class);
				context.startActivity(intent);
			}
			
		}
		else if(menuId.equals("loan_3")){// 贷款管理
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanQueryMenuActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanQueryMenuActivity.class);
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
	/** 隐藏左侧展示按钮 */
	private void visible() {
		Button btnhide = (Button) findViewById(R.id.btn_show);
		btnhide.setVisibility(View.GONE);
		Button back = (Button) findViewById(R.id.ib_back);
		back.setVisibility(View.GONE);
	}

	/** 界面初始化 */
	private void init() {
		// 获取贷款账户信息
		// 第一次测算条件
		advancecondition = LoanDataCenter.getInstance().getLoanRepayCount();
		// 第一次测算结果
		advanceresult = LoanDataCenter.getInstance().getCountResultmap();
		interestType=LoanAccountListActivity.interestType;
		type = this.getIntent().getStringExtra(Loan.ADVANCE_LOAN_COUNTTYPE_REQ);
		tv_repayAmount = (TextView) view
				.findViewById(R.id.loan_repayAmount_value);
		tv_repayAmountInAdvance = (TextView) view
				.findViewById(R.id.loan_repayAmountInAdvance_value);
		tv_capitalInAdvance = (TextView) view
				.findViewById(R.id.loan_capitalInAdvance_value);
		tv_interestInAdvance = (TextView) view
				.findViewById(R.id.loan_interestInAdvance_value);
		tv_everyTermAmount = (TextView) view
				.findViewById(R.id.loan_everyTermAmount_value);
		tv_remainAmount2 = (TextView) view
				.findViewById(R.id.loan_remainAmount2_value);
		tv_remainIssueforAdvance2 = (TextView) view
				.findViewById(R.id.loan_remainIssueforAdvance2_value);
		tv_totalIssue2 = (TextView) view
				.findViewById(R.id.loan_totalIssue2_value);
		ll_everyTermAmount = (LinearLayout) view
				.findViewById(R.id.ll_everyTermAmount);
		ll_repayAmount = (LinearLayout) view.findViewById(R.id.ll_repayAmount);
		TextView tv_loan_everyTermAmount = (TextView) view
				.findViewById(R.id.tv_loan_everyTermAmount);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_loan_everyTermAmount);
		TextView tv_loan_remainIssueforAdvance = (TextView) view
				.findViewById(R.id.tv_loan_remainIssueforAdvance);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_loan_remainIssueforAdvance);
		/**判断如果还款方式为G/F 的时候,才显示原还款金额和提前还款后每期金额
		 * LoanData.loanInterestType_mondy 字典*/
		System.out.println("interestTyp====222"+interestType);
		if(!StringUtil.isNull(interestType)){
			if("F".equals(interestType)||"G".equals(interestType)){
			}else{
				ll_repayAmount.setVisibility(View.GONE);
				ll_everyTermAmount.setVisibility(View.GONE);
			}
		}
		if (type.equalsIgnoreCase(this.getString(R.string.loan_advance_type_1))) {
			/** 提前还款后金额 */
			String repayAmountInAdvance = String.valueOf(advanceresult
					.get(Loan.ADVANCE_LOAN_REPAYAMOUNTINADVANCE_RES));
			tv_repayAmountInAdvance.setText((StringUtil.parseStringCodePattern(currency,
					repayAmountInAdvance, 2)));
			/** 提前还款后本金 */
			String capitalInAdvance = String.valueOf(advanceresult
					.get(Loan.ADVANCE_LOAN_CAPITALINADVANCE_RES));
			tv_capitalInAdvance.setText((StringUtil.parseStringCodePattern(currency,
					capitalInAdvance, 2)));
			/** 提前还款后利息 */
			String interestInAdvance = String.valueOf(advanceresult
					.get(Loan.ADVANCE_LOAN_INTERESTINADVANCE_RES));
			tv_interestInAdvance.setText((StringUtil.parseStringCodePattern(currency,
					interestInAdvance, 2)));
			/** 提前还款后每期金额 */
			String everyTermAmount = String.valueOf(advanceresult
					.get(Loan.ADVANCE_LOAN_EVERYTERMAMOUNT_RES));
			tv_everyTermAmount.setText((StringUtil.parseStringCodePattern(currency,
					everyTermAmount, 2)));
			/** 原还款金额 */
			String repayAmount = String.valueOf(advanceresult
					.get(Loan.ADVANCE_LOAN_REPAYAMOUNT_RES));
			tv_repayAmount.setText((StringUtil.parseStringCodePattern(currency,
					repayAmount, 2)));
		} else if (type.equalsIgnoreCase(this
				.getString(R.string.loan_advance_type_2))) {
			tv_totalIssue2.setText(String.valueOf(advanceresult
					.get(Loan.ADVANCE_LOAN_TOTALISSUE_RES)));
			tv_remainIssueforAdvance2.setText(String.valueOf(advanceresult
					.get(Loan.ADVANCE_LOAN_REMAINISSUEFORADVANCE_RES)));
			String remainAmount=(String) advanceresult
					.get(Loan.ADVANCE_LOAN_REMAINAMOUNT_RES);
			tv_remainAmount2.setText(StringUtil.parseStringCodePattern(currency, remainAmount, 2));
		}

		btnSure = (Button) view.findViewById(R.id.btnSure);
		// 请求提前还款测算信息
		requestAdvanceCount();
	}

	/** 请求提前还款测算信息 */
	public void requestAdvanceCount() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_ADVANCEREPAY_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Loan.ADVANCE_LOAN_ACTNUM_REQ, String.valueOf(loanmap
				.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES)));
		paramsmap.put(Loan.ADVANCE_LOAN_ADVANCEREPAYCAPITAL_REQ, String
				.valueOf(advancecondition
						.get(Loan.ADVANCE_LOAN_ADVANCEREPAYCAPITAL_REQ)));
		paramsmap.put(Loan.ADVANCE_LOAN_ADVANCEREPAYINTEREST_REQ, String
				.valueOf(advancecondition
						.get(Loan.ADVANCE_LOAN_ADVANCEREPAYINTEREST_REQ)));
		String countype = advancecondition.get(Loan.ADVANCE_LOAN_COUNTTYPE_REQ);
		if (countype.equalsIgnoreCase(this
				.getString(R.string.loan_advance_type_1))) {
			countype = this.getString(R.string.loan_advance_type_2);
		} else if (countype.equalsIgnoreCase(this
				.getString(R.string.loan_advance_type_2))) {
			countype = this.getString(R.string.loan_advance_type_1);
		}
		paramsmap.put(Loan.ADVANCE_LOAN_COUNTTYPE_REQ,
				LocalData.loancountType.get(countype));
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestAdvanceCountCallBack");
	}

	/**
	 * 请求提前还款测算回调信息
	 * 
	 * @param resultObj
	 */
	public void requestAdvanceCountCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> advanceCountmap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(advanceCountmap)) {
			return;
		}
		// 最新的测算结果
		if (type.equalsIgnoreCase(this.getString(R.string.loan_advance_type_1))) {
			tv_totalIssue2.setText(String.valueOf(advanceCountmap
					.get(Loan.ADVANCE_LOAN_TOTALISSUE_RES)));
			tv_remainIssueforAdvance2.setText(String.valueOf(advanceCountmap
					.get(Loan.ADVANCE_LOAN_REMAINISSUEFORADVANCE_RES)));
			//(StringUtil.parseStringCodePattern(currency, issuerepayInterest, 2))
			tv_remainAmount2.setText(StringUtil.parseStringCodePattern(currency,String
					.valueOf(advanceCountmap
							.get(Loan.ADVANCE_LOAN_REMAINAMOUNT_RES)), 2));

		} else if (type.equalsIgnoreCase(this
				.getString(R.string.loan_advance_type_2))) {
			/** 提前还款后金额 */
			String repayAmountInAdvance = String.valueOf(advanceCountmap
					.get(Loan.ADVANCE_LOAN_REPAYAMOUNTINADVANCE_RES));
			tv_repayAmountInAdvance.setText(StringUtil.parseStringCodePattern(currency,
					repayAmountInAdvance, 2));
			/** 提前还款后本金 */
			String capitalInAdvance = String.valueOf(advanceCountmap
					.get(Loan.ADVANCE_LOAN_CAPITALINADVANCE_RES));
			tv_capitalInAdvance.setText(StringUtil.parseStringCodePattern(currency,
					capitalInAdvance, 2));
			/** 提前还款后利息 */
			String interestInAdvance = String.valueOf(advanceCountmap
					.get(Loan.ADVANCE_LOAN_INTERESTINADVANCE_RES));
			tv_interestInAdvance.setText(StringUtil.parseStringCodePattern(currency,
					interestInAdvance, 2));
			/** 提前还款后每期金额 */
			String everyTermAmount = String.valueOf(advanceCountmap
					.get(Loan.ADVANCE_LOAN_EVERYTERMAMOUNT_RES));
			tv_everyTermAmount.setText(StringUtil.parseStringCodePattern(currency,
					everyTermAmount, 2));
			/** 原还款金额 */
			String repayAmount = String.valueOf(advanceCountmap
					.get(Loan.ADVANCE_LOAN_REPAYAMOUNT_RES));
			tv_repayAmount.setText(StringUtil.parseStringCodePattern(currency,
					repayAmount, 2));
		}
		btnSure.setOnClickListener(btnSureClick);
	}

	/** 确定按钮点击监听事件 */
	OnClickListener btnSureClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			loanAmount=true;
			Intent intent = new Intent(LoanAdvanceCountMatchActivity.this,
					LoanAccountListActivity.class);
			startActivity(intent);
			ActivityTaskManager.getInstance().removeAllActivity();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 屏蔽返回键
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
}
