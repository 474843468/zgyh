package com.chinamworld.bocmbci.biz.loan;

import java.util.HashMap;
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
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.loanApply.LoanApplyMenuActivity;
import com.chinamworld.bocmbci.biz.loan.loanQuery.LoanQueryMenuActivity;
import com.chinamworld.bocmbci.biz.loan.loanRepay.LoanRepayMenuActivity;
import com.chinamworld.bocmbci.biz.loan.loanUse.LoanUseMenuActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 贷款提前还款测算结果
 * 
 * @author wangmengmeng
 * 
 */
public class LoanAdvanceCountResultActivity extends BaseActivity {
	private static final String TAG = "LoanAdvanceCountResultActivity";
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示
	/** 贷款账户提前还款试算结果信息页面 */
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
	/** 原还款金额 */
	private LinearLayout ll_repayAmount;
	/** 提前还款后金额 */
	private LinearLayout ll_repayAmountInAdvance;
	/** 提前还款后本金 */
	private LinearLayout ll_capitalInAdvance;
	/** 提前还款后利息 */
	private LinearLayout ll_interestInAdvance;
	/** 提前还款后每期金额 */
	private LinearLayout ll_everyTermAmount;
	/** 原期数 */
	private LinearLayout ll_totalIssue;
	/** 提前还款后剩余期数 */
	private LinearLayout ll_remainIssueforAdvance;
	/** 剩余还款额（本金+利息） */
	private LinearLayout ll_remainAmount;
	/** 原期数 */
	private TextView tv_totalIssue;
	/** 提前还款后剩余期数 */
	private TextView tv_remainIssueforAdvance;
	/** 剩余还款额（本金+利息） */
	private TextView tv_remainAmount;
	/** 重测按钮 */
	private Button btnRepayReelect;
	/** 贷款账户信息 */
	Map<String, Object> loanmap = new HashMap<String, Object>();
	/** 提前还款测算条件 */
	Map<String, String> loanRepayCount = new HashMap<String, String>();
	/** 提前还款对比按钮 */
	private Button btnLoanRepayMatch;
	/**还款方式*/
	private  String interestType ;
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
				R.layout.loan_advancerepay_count_result, null);
		tabcontent.addView(view);

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
		loanmap = LoanDataCenter.getInstance().getLoanmap();
		//获取币种
		String currency=(String)loanmap.get(Loan.LOANACC_CURRENCYCODE_RES);
		interestType=LoanAccountListActivity.interestType;
		// 获取提前还款测算条件信息
		loanRepayCount = LoanDataCenter.getInstance().getLoanRepayCount();
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
		ll_capitalInAdvance = (LinearLayout) view
				.findViewById(R.id.ll_capitalInAdvance);
		ll_everyTermAmount = (LinearLayout) view
				.findViewById(R.id.ll_everyTermAmount);
		ll_interestInAdvance = (LinearLayout) view
				.findViewById(R.id.ll_interestInAdvance);
		ll_remainAmount = (LinearLayout) view
				.findViewById(R.id.ll_remainAmount);
		ll_remainIssueforAdvance = (LinearLayout) view
				.findViewById(R.id.ll_remainIssueforAdvance);
		ll_repayAmount = (LinearLayout) view.findViewById(R.id.ll_repayAmount);
		ll_repayAmountInAdvance = (LinearLayout) view
				.findViewById(R.id.ll_repayAmountInAdvance);
		ll_totalIssue = (LinearLayout) view.findViewById(R.id.ll_totalIssue);
		tv_remainIssueforAdvance = (TextView) view
				.findViewById(R.id.loan_remainIssueforAdvance_value);
		tv_remainAmount = (TextView) view
				.findViewById(R.id.loan_remainAmount_value);
		tv_totalIssue = (TextView) view
				.findViewById(R.id.loan_totalIssue_value);
		TextView tv_loan_everyTermAmount = (TextView) view
				.findViewById(R.id.tv_loan_everyTermAmount);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_loan_everyTermAmount);
		TextView tv_loan_remainIssueforAdvance = (TextView) view
				.findViewById(R.id.tv_loan_remainIssueforAdvance);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_loan_remainIssueforAdvance);
		btnRepayReelect = (Button) view.findViewById(R.id.btnLoanRepayReelect);
		btnRepayReelect.setOnClickListener(repaySureClick);
		btnLoanRepayMatch = (Button) view.findViewById(R.id.btnLoanRepayMatch);
		btnLoanRepayMatch.setOnClickListener(repayMatchClick);
		Map<String, Object> countResultmap = LoanDataCenter.getInstance()
				.getCountResultmap();
		String type = loanRepayCount.get(Loan.ADVANCE_LOAN_COUNTTYPE_REQ);
		System.out.println("interestTyp====1111"+interestType);
		if(!StringUtil.isNull(interestType)){
			if("F".equals(interestType)||"G".equals(interestType)){
			}else{
				ll_repayAmount.setVisibility(View.GONE);
				ll_everyTermAmount.setVisibility(View.GONE);
			}
		}
		if (type.equals(LocalData.loancountTypeList.get(0))) {
			// 还款额不变
			ll_repayAmount.setVisibility(View.GONE);
			ll_repayAmountInAdvance.setVisibility(View.GONE);
			ll_capitalInAdvance.setVisibility(View.GONE);
			ll_interestInAdvance.setVisibility(View.GONE);
			ll_everyTermAmount.setVisibility(View.GONE);
			tv_totalIssue.setText(String.valueOf(countResultmap
					.get(Loan.ADVANCE_LOAN_TOTALISSUE_RES)));
			tv_remainIssueforAdvance.setText(String.valueOf(countResultmap
					.get(Loan.ADVANCE_LOAN_REMAINISSUEFORADVANCE_RES)));
			tv_remainAmount.setText(StringUtil.parseStringCodePattern(currency,String
					.valueOf(countResultmap
							.get(Loan.ADVANCE_LOAN_REMAINAMOUNT_RES)), 2));
		} else {
			ll_totalIssue.setVisibility(View.GONE);
			ll_remainIssueforAdvance.setVisibility(View.GONE);
			ll_remainAmount.setVisibility(View.GONE);
			/** 提前还款后金额 */
			String repayAmountInAdvance = String.valueOf(countResultmap
					.get(Loan.ADVANCE_LOAN_REPAYAMOUNTINADVANCE_RES));
			tv_repayAmountInAdvance.setText((StringUtil.parseStringCodePattern(currency,
					repayAmountInAdvance, 2)));
			/** 提前还款后本金 */
			String capitalInAdvance = String.valueOf(countResultmap
					.get(Loan.ADVANCE_LOAN_CAPITALINADVANCE_RES));
			tv_capitalInAdvance.setText((StringUtil.parseStringCodePattern(currency,
					capitalInAdvance, 2)));
			/** 提前还款后利息 */
			String interestInAdvance = String.valueOf(countResultmap
					.get(Loan.ADVANCE_LOAN_INTERESTINADVANCE_RES));
			tv_interestInAdvance.setText((StringUtil.parseStringCodePattern(currency,
					interestInAdvance, 2)));
			/** 提前还款后每期金额 */
			String everyTermAmount = String.valueOf(countResultmap
					.get(Loan.ADVANCE_LOAN_EVERYTERMAMOUNT_RES));
			tv_everyTermAmount.setText((StringUtil.parseStringCodePattern(currency,
					everyTermAmount, 2)));
			/** 原还款金额 */
			String repayAmount = String.valueOf(countResultmap
					.get(Loan.ADVANCE_LOAN_REPAYAMOUNT_RES));
			tv_repayAmount.setText((StringUtil.parseStringCodePattern(currency,
					repayAmount, 2)));
		}

	}

	/** 重选按钮监听事件 */
	OnClickListener repaySureClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(LoanAdvanceCountResultActivity.this,
					LoanAccountListActivity.class);
			startActivity(intent);
			ActivityTaskManager.getInstance().removeAllActivity();
		}
	};
	/** 对比按钮监听事件 */
	OnClickListener repayMatchClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(LoanAdvanceCountResultActivity.this,
					LoanAdvanceCountMatchActivity.class);
			// 测算方式
			intent.putExtra(Loan.ADVANCE_LOAN_COUNTTYPE_REQ,
					loanRepayCount.get(Loan.ADVANCE_LOAN_COUNTTYPE_REQ));
			startActivity(intent);

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
