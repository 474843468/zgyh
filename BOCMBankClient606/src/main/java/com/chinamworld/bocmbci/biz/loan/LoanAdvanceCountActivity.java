package com.chinamworld.bocmbci.biz.loan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 贷款提前还款测算
 * 
 * @author wangmengmeng
 * 
 */
public class LoanAdvanceCountActivity extends BaseActivity {
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示
	/** 贷款账户提前还款试算信息页面 */
	private View view;
	/** 提前还款本金 */
	private EditText et_advanceRepayCapital;
	/** 提前还款利息 */
//	private EditText et_advanceRepayInterest;
	/** 测算方式 */
	private RadioGroup rg_advanceType;
	/** 分期数不变 */
	private RadioButton rb_advanceType_1;
	/** 还款金额不变 */
	private RadioButton rb_advanceType_2;
	/** 选择的测算方式 */
	private String advanceType = "";
	/** 提前还款本金 */
	private String advanceRepayCapital;
	/** 提前还款利息 */
//	private String advanceRepayInterest;
	/** 测算按钮 */
	private Button btnRepayCount;
	/** 贷款账户信息 */
	Map<String, Object> loanmap = new HashMap<String, Object>();
    private TextView tv_advanceRepayInterest=null;
    String issuerepayInterest=null;
    /**币种*/
    private String currency;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		// 为界面标题赋值
		setTitle(this.getString(R.string.btnGoAdvanced));
		// 隐藏左侧展示按钮
		visible();
		view = (View) LayoutInflater.from(this).inflate(R.layout.loan_advancerepay_count, null);
		// 获取贷款账户信息
		loanmap = LoanDataCenter.getInstance().getLoanmap();
		currency = getIntent().getStringExtra(Loan.LOAN_CURRENCYCODE_RES);
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化底部菜单栏
		initFootMenu();
		// 初始化左边菜单
		initLeftSideList(this, LocalData.loanLeftMenuList);
		tabcontent.addView(view);
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
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/** 界面初始化 */
	private void init() {
		et_advanceRepayCapital = (EditText) view.findViewById(R.id.et_loan_advanceRepayCapital_value);
	//	et_advanceRepayInterest = (EditText) view.findViewById(R.id.et_loan_advanceRepayInterest_value);
		tv_advanceRepayInterest=(TextView)view.findViewById(R.id.et_loan_advanceRepayInterest_value);
		String currency=LoanAccountListActivity.currency;
	    //拟提前还款利息
		issuerepayInterest=StringUtil.parseStringPattern(LoanAccountListActivity.issuerepayInterest, 2);
     String issuerepayInterests=StringUtil.parseStringCodePattern(currency, issuerepayInterest, 2);
		tv_advanceRepayInterest.setText((StringUtil.isNull(issuerepayInterest)) ? ConstantGloble.BOCINVT_DATE_ADD : 
			(issuerepayInterests));
		rg_advanceType = (RadioGroup) view.findViewById(R.id.loan_advance_type);
		rb_advanceType_1 = (RadioButton) view.findViewById(R.id.loan_advance_type_1);
		rb_advanceType_2 = (RadioButton) view.findViewById(R.id.loan_advance_type_2);
		rg_advanceType.setOnCheckedChangeListener(advanceTypeListener);
		btnRepayCount = (Button) view.findViewById(R.id.btnLoanRepayCount);
		btnRepayCount.setOnClickListener(repayCountClick);
		advanceType = this.getString(R.string.loan_advance_type_1);

	}

	/** 测算方式改变监听事件 */
	OnCheckedChangeListener advanceTypeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			switch (checkedId) {
			case R.id.loan_advance_type_1:
				advanceType = rb_advanceType_1.getText().toString().trim();
				break;
			case R.id.loan_advance_type_2:
				advanceType = rb_advanceType_2.getText().toString().trim();
				break;
			default:
				break;
			}
		}
	};
	/** 进行测算监听事件 */
	OnClickListener repayCountClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			advanceRepayCapital = et_advanceRepayCapital.getText().toString().trim();
		//	advanceRepayInterest=LoanAccountListActivity.issuerepayInterest;
					// et_advanceRepayInterest.getText().toString().trim();
			// 以下为验证
			// 提前还款本金
			if(LocalData.codeNoNumber.contains(currency)){
				RegexpBean reb = new RegexpBean(
						LoanAdvanceCountActivity.this.getString(R.string.loan_advance_capital_regex), advanceRepayCapital,
						"spetialAmountJPCK");
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb);
//				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {// 校验通过
					Map<String, String> loanRepayCount = new HashMap<String, String>();
					loanRepayCount.put(Loan.ADVANCE_LOAN_ADVANCEREPAYCAPITAL_REQ, advanceRepayCapital);
					loanRepayCount.put(Loan.ADVANCE_LOAN_ADVANCEREPAYINTEREST_REQ,issuerepayInterest);
					loanRepayCount.put(Loan.ADVANCE_LOAN_COUNTTYPE_REQ, advanceType);
					LoanDataCenter.getInstance().setLoanRepayCount(loanRepayCount);
					// 请求提前还款测算信息
					requestAdvanceCount();
				}
			}else{
				RegexpBean reb = new RegexpBean(
						LoanAdvanceCountActivity.this.getString(R.string.loan_advance_capital_regex), advanceRepayCapital,
						"advanceCapitalAmount");
				// 提前还款利率
//				RegexpBean reb1 = new RegexpBean(
//						LoanAdvanceCountActivity.this.getString(R.string.loan_advance_interest_regex),
//						issuerepayInterest, "repaymentRate");
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb);
//				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {// 校验通过
					Map<String, String> loanRepayCount = new HashMap<String, String>();
					loanRepayCount.put(Loan.ADVANCE_LOAN_ADVANCEREPAYCAPITAL_REQ, advanceRepayCapital);
					loanRepayCount.put(Loan.ADVANCE_LOAN_ADVANCEREPAYINTEREST_REQ,issuerepayInterest);
					loanRepayCount.put(Loan.ADVANCE_LOAN_COUNTTYPE_REQ, advanceType);
					LoanDataCenter.getInstance().setLoanRepayCount(loanRepayCount);
					// 请求提前还款测算信息
					requestAdvanceCount();
				}
			}
		}
	};

	/** 请求提前还款测算信息 */
	public void requestAdvanceCount() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_ADVANCEREPAY_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Loan.ADVANCE_LOAN_ACTNUM_REQ, String.valueOf(loanmap.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES)));
		paramsmap.put(Loan.ADVANCE_LOAN_ADVANCEREPAYCAPITAL_REQ, advanceRepayCapital);
		paramsmap.put(Loan.ADVANCE_LOAN_ADVANCEREPAYINTEREST_REQ, issuerepayInterest);
		paramsmap.put(Loan.ADVANCE_LOAN_COUNTTYPE_REQ, LocalData.loancountType.get(advanceType));
		biiRequestBody.setParams(paramsmap);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestAdvanceCountCallBack");
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
		Map<String, Object> countResultmap = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(countResultmap)) {
			return;
		}
		LoanDataCenter.getInstance().setCountResultmap(countResultmap);
		Intent intent = new Intent(LoanAdvanceCountActivity.this, LoanAdvanceCountResultActivity.class);
		startActivity(intent);
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
}
