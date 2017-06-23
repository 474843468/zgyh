package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog.OnSelectListener;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.ui.RepayPlanCalcFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.CollectionAccountCheckRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.AccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.AccoutFragment.AccountType;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.data.DataUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model.LOANCycleLoanEApplyVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model.LOANCycleLoanEApplyVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model.LoanRepaymentPlanRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.presenter.DrawPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanStatusListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.EloanDrawFragment;
import com.boc.bocsoft.mobile.common.utils.ButtonClickLock;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 贷款管理-中银E贷-用款页面fragment Created by xintong on 2016/6/4.
 */
public class DrawFragment extends MvpBussFragment<DrawPresenter> implements DrawContract.DrawView {
	private static final String TAG = "DrawFragment";
	// 获取安全因子成功后返回的默认安全因子组合id
	private String _combinId,combinId_name;
	// 页面名称
	private static String PAGENAME;
	// 利率
	protected TextView rate;
	// 还款方式
	protected TextView hint;

	protected RelativeLayout includee;
	protected LinearLayout linearLayout;
	// 预交易后，再次确认安全因子 为true调用预交易接口
	private boolean available;
	private DrawConfirmInfoFragment drawConfirmInfoFragment;
	// 安全组件
	private SecurityVerity securityVerity;
	// 弹出框组件
	private SelectListDialog selectListDialog;
	// 弹出框内列表适配器
	private DrawListAdapter drawListAdapter;
	// 会话id
	private String conversationId;
	// 还款方式类别
	private String payType = "";
	// 利息，本息合计，还款计划动态视图
	private LinearLayout include;
	// 可用额度 value
	private TextView availableCredit_value;
	// 提款金额
	private EditMoneyInputWidget drawAmount;
	// private EditClearWidget drawAmount;
	// 贷款期限
	private EditChoiceWidget time;
	private int times;
	// 收款账户
	private EditChoiceWidget receiptAccount;
	// 资金用途
	private EditChoiceWidget useOffund;
	// 下一步按钮
	private Button next;
	// 利息（估算）
	private TextView interest;
	// 本息合计
	private TextView title;
	// 还款计划
	private TextView repaymentPlan;
	// 是否首次提款
	private boolean isFirst;
	// 页面根视图
	private View mRoot;
//	private DrawPresenter drawPresenter;
	/** 用款传递对象 */
	private EloanStatusListModel mEloanDrawModel;
	// 还款账号
	private String payAccount;
	// 账户id
	private String accountId;
	// 账户
	private String account;
	private LOANCycleLoanEApplyVerifyReq req;

	/** 提交按钮 防暴力点击 */
	private String click_more = "click_more";
	/**试算还款计划*/
	private TextView tv_sshkjh;

	@Override
	protected View onCreateView(LayoutInflater mInflater) {
		mRoot = mInflater.inflate(R.layout.fragment_eloan_draw, null);
		return mRoot;
	}

	@Override
	protected void titleLeftIconClick() {
		super.titleLeftIconClick();
		popToAndReInit(EloanDrawFragment.class);
	}

	@Override
	protected void titleRightIconClick() {
		super.titleRightIconClick();
	}

	@Override
	protected String getTitleValue() {
		return getString(R.string.boc_eloan_draw_pagename);
	}

	@Override
	public void beforeInitView() {
	}

	@Override
	protected boolean isDisplayRightIcon() {
		return false;
	}

	@Override
	public void initView() {
//		drawPresenter = new DrawPresenter(this);

		showLoadingDialog(false);
		getPresenter().creatConversation();

		securityVerity = SecurityVerity.getInstance(getActivity());
		mEloanDrawModel = (EloanStatusListModel) getArguments()
				.getSerializable(EloanConst.ELON_DRAW);
		//还款账号  2016-11-21 15:05:44 惠晓博 修改
		payAccount = getArguments().getString(EloanConst.PEPAY_ACCOUNT);
		// 组件初始化
		availableCredit_value = (TextView) mRoot.findViewById(R.id.availableCredit_value);
		drawAmount = (EditMoneyInputWidget) mRoot.findViewById(R.id.drawAmount);
		time = (EditChoiceWidget) mRoot.findViewById(R.id.time);
		receiptAccount = (EditChoiceWidget) mRoot
				.findViewById(R.id.receiptAccount);
		useOffund = (EditChoiceWidget) mRoot.findViewById(R.id.useOffund);
		interest = (TextView) mRoot.findViewById(R.id.interest);
		title = (TextView) mRoot.findViewById(R.id.title);
		repaymentPlan = (TextView) mRoot.findViewById(R.id.repaymentPlan);
		include = (LinearLayout) mRoot.findViewById(R.id.include);
		next = (Button) mRoot.findViewById(R.id.next);
		rate = (TextView) mRoot.findViewById(R.id.rate);
		hint = (TextView) mRoot.findViewById(R.id.hint);
		includee = (RelativeLayout) mRoot.findViewById(R.id.includee);
		linearLayout = (LinearLayout) mRoot.findViewById(R.id.linearLayout);
		tv_sshkjh = (TextView) mRoot.findViewById(R.id.tv_sshkjh);
	}

	@Override
	public void initData() {
		ButtonClickLock.getLock(click_more).lockDuration = EloanConst.CLICK_MORE_TIME;
		// 第一次提款，页面初始值设置
		availableCredit_value.setText("人民币元 "
				+ MoneyUtils.transMoneyFormat(
						mEloanDrawModel.getAvailableAvl(), "001"));
		rate.setText("利率：" + mEloanDrawModel.getRate() + "%");
		// rate.setText("利率："+
		// MoneyUtils.transRatePercentTypeFormat(mEloanDrawModel.getRate()));

		drawAmount.getContentMoneyEditText().setHint(
				getString(R.string.boc_eloan_draw_input));
		drawAmount.getContentMoneyEditText().setTextColor(Color.RED);
		drawAmount.setMaxLeftNumber(11);
		drawAmount.setMaxRightNumber(2);
		// drawAmount.setEditWidgetHint(getString(R.string.boc_eloan_draw_input));
		// drawAmount.setEditWidgetTitle(getString(R.string.boc_eloan_draw_drawAmount));
		drawAmount.setTitleTextBold(true);//加粗
		drawAmount.setEditWidgetTitle(getString(R.string.boc_details_drawamount));
		time.setChoiceTextContent(getString(R.string.boc_eloan_choice));
		time.setChoiceTitleBold(true);
		//账户选择
		 receiptAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
		receiptAccount.setChoiceTitleBold(true);
		useOffund.setChoiceTextContent("日常消费");
		useOffund.setChoiceTitleBold(true);
		include.setVisibility(View.GONE);
		includee.setVisibility(View.GONE);

		// 选择框组件初始化
		selectListDialog = new SelectListDialog(mContext);
		drawListAdapter = new DrawListAdapter(mContext);
		// 选择框组件列表适配器
		selectListDialog.setAdapter(drawListAdapter);
	}

	@Override
	public void setListener() {
		// 贷款期限
		time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 贷款期限
				onClickItem(DataUtils.getDataTimeList(), time, true);
				includee.setVisibility(View.VISIBLE);
			}
		});
		// 账户选择
		receiptAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AccoutFragment accoutFragment = new AccoutFragment();
				Bundle bundle = new Bundle();
				bundle.putString("type", "draw");
				accoutFragment.setArguments(bundle);
				accoutFragment
						.setAccountType(AccountType.SELECT_COLLECTIONACCOUNT);
				accoutFragment.setConversationId(conversationId);
				startForResult(accoutFragment, AccoutFragment.RequestCode);
			}
		});
		//金额 键盘消失和显示监听
		drawAmount.setOnKeyBoardListener(new EditMoneyInputWidget.KeyBoardDismissOrShowCallBack() {
			@Override
			public void onKeyBoardDismiss() {

				judgeSshkjh();
			}

			@Override
			public void onKeyBoardShow() {

			}
		});
		//试算还款计划 - 2016年10月9日 15:56:26 yx add
		tv_sshkjh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RepayPlanCalcFragment mRepayPlanCalcFragment = new RepayPlanCalcFragment();
				mRepayPlanCalcFragment.setData(mEloanDrawModel.getQuoteViewModel());
				mRepayPlanCalcFragment.setUseAmount(drawAmount.getContentMoney());
				mRepayPlanCalcFragment.setCurrencyCode(mEloanDrawModel.getCurrencyCode());
				if(times < 10){
					mRepayPlanCalcFragment.setLoanPeriod(String.format("0%s", times),payType);
				}
				else{
					mRepayPlanCalcFragment.setLoanPeriod(String.format("%s", times),payType);
				}

				start(mRepayPlanCalcFragment);
			}
		});


		// 资金用途
		useOffund.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 资金用途
				onClickItem(DataUtils.getDataUseOfFundList(), useOffund, false);
			}
		});
		repaymentPlan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 还款计划，待补充
			}
		});
		// 下一步
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!ButtonClickLock.isCanClick(click_more)) {
					return;
				}
				judgeStrong();
			}
		});
	}

	/**
	 * 校验 试算还款计划
	 */
    private void judgeSshkjh(){
	//校验用款金额
	if (StringUtils.isEmptyOrNull(drawAmount.getContentMoney())) {
		tv_sshkjh.setVisibility(View.INVISIBLE);
		return;
	}

	//校验用款金额比较可用余额
	int n1 = compareBigDecimal(mEloanDrawModel.getAvailableAvl(),drawAmount.getContentMoney());
	LogUtils.i("cq--------用款金额比较可用余额----n1--" + n1);
	if (n1 == 1) {
		tv_sshkjh.setVisibility(View.INVISIBLE);
		return;
	}

	//用款金额比较1000
	int n2 = compareBigDecimal(drawAmount.getContentMoney(), "1000");
	LogUtils.i("cq--------用款金额比较1000----n2--" + n2);
	if (n2 == 1) {
		LogUtils.i("cq--------用款金额比较1000--1");
		tv_sshkjh.setVisibility(View.INVISIBLE);
		return;
	}

	//用款期限
	if("请选择".equalsIgnoreCase(time.getChoiceTextContent())){
		tv_sshkjh.setVisibility(View.INVISIBLE);
		LogUtils.i("cq--------用款期限没选择--");
		return;
	}
		tv_sshkjh.setText("试算还款计划");
		tv_sshkjh.setVisibility(View.VISIBLE);
		LogUtils.i("cq--------试算还款计划显示--");
}
	private void judgeStrong() {
				
		//校验用款金额
		if (StringUtils.isEmptyOrNull(drawAmount.getContentMoney())) {
			showErrorDialog("请输入用款金额");
			return;
		}
		
		//校验用款金额比较可用余额
		int n1 = compareBigDecimal(mEloanDrawModel.getAvailableAvl(),drawAmount.getContentMoney());
		LogUtils.i("cq--------用款金额比较可用余额----n1" + n1);
		if (n1 == 1) {
			showErrorDialog("用款金额不能大于可用额度");
			return;
		}
		
		//用款金额比较1000
		int n2 = compareBigDecimal(drawAmount.getContentMoney(), "1000");
		LogUtils.i("cq--------用款金额比较1000----n2" + n2);
		if (n2 == 1) {
			showErrorDialog("用款金额不能小于用款下限1,000.00元 ");
			return;
		}
		
		//用款期限
		if("请选择".equalsIgnoreCase(time.getChoiceTextContent())){
			showErrorDialog("请选择用款期限");
			return;
		}
		
		//收款账户
		if("请选择".equalsIgnoreCase(receiptAccount.getChoiceTextContent())){
			showErrorDialog("请选择收款账户");
			return;
		}
		
		// 获取安全因子
		showLoadingDialog(false);
		getPresenter().getSecurityFactor();
	}

	//比较金额大小
	private int compareBigDecimal(String s1, String s2) {
		BigDecimal b1 = new BigDecimal(s1);
		BigDecimal b2 = new BigDecimal(s2);
		LogUtils.i("cq--------b1----" + b1 + "----b2---" + b2);
		return b2.compareTo(b1);
	}

	@Override
	protected boolean getTitleBarRed() {
		return false;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getPresenter().unsubscribe();
	}

	/**
	 * 点击显示弹框选择列表
	 *
	 * @param list
	 *            要往弹框列表容器内填装的数据
	 * @param view
	 *            点击弹出弹框的组件
	 */
	private void onClickItem(List<String> list, final EditChoiceWidget view,
			final boolean isTime) {
		// selectListDialog.setTitle("选择期限");
		selectListDialog.setListData(list);
		selectListDialog.setOnSelectListener(new OnSelectListener() {
			@Override
			public void onSelect(int position, Object model) {
				if (null != model) {
					if (isTime) {
						times = position + 1;
						if (position + 1 <= 3) {
							payType = "1";
							// hint.setText("还款方式：到期一次性还本付息");
							hint.setText("还款方式："
									+ getString(R.string.boc_details_repaytypeTwo));
						} else {
							payType = "2";
							// hint.setText("还款方式：按月付息到期还本");
							hint.setText("还款方式："
									+ getString(R.string.boc_details_repaytypeOne));
						}
					}
					// 为组件传入选中的值
					view.setChoiceTextContent(model.toString());
					// 弹框消失
					selectListDialog.dismiss();
					judgeSshkjh();
				}
			}
		});
		// 显示弹框
		selectListDialog.show();
	}

	@Override
	public void obtainConversationSuccess(String conversationId) {
		Log.i(TAG, "获取会话成功！");
		// closeProgressDialog();
		this.conversationId = conversationId;
		getPresenter().setConversationId(conversationId);
		// drawConfirmInfoFragment.setConversationId(conversationId);

		// 查询中行所有帐户列表
		List<String> list = new ArrayList<String>();
		list.add("119");
		getPresenter().setAccountType(list);
		getPresenter().queryAllChinaBankAccount();
		// showLoadingDialog();

	}

	@Override
	public void obtainConversationFail(ErrorException e) {
		Log.i(TAG, "获取会话失败！");
		closeProgressDialog();
	}

	@Override
	public void obtainSecurityFactorSuccess(SecurityFactorModel result) {
		Log.i(TAG, "-------->获取安全因子成功！");
		_combinId = securityVerity.getDefaultSecurityFactorId(result).getId();
		combinId_name = securityVerity.getDefaultSecurityFactorId(result).getName();
		LogUtils.i(TAG, "-------->获取安全因子成功！_combinId+combinId_name"+_combinId+combinId_name);
		// drawConfirmInfoFragment.set_combinId(_combinId);
		// closeProgressDialog();
		buildVerifyReq();
	}

	@Override
	public void obtainSecurityFactorFail(ErrorException e) {
		Log.i(TAG, "-------->获取安全因子失败！");
		closeProgressDialog();
	}

	@Override
	public void obtainLoanRepaymentPlanSuccess(LoanRepaymentPlanRes result) {
		Log.i(TAG, "获取还款计划成功！");
		closeProgressDialog();
	}

	@Override
	public void obtainLoanRepaymentPlanFail(ErrorException e) {
		Log.i(TAG, "获取还款计划成功！");
		closeProgressDialog();
	}

	@Override
	public void drawApplyVerifySuccess(LOANCycleLoanEApplyVerifyRes result) {
		LogUtils.i(TAG, "提款预交易成功！");
		closeProgressDialog();
		// 音频key加密
		EShieldVerify.getInstance(getActivity()).setmPlainData(
				result.get_plainData());

		drawConfirmInfoFragment = new DrawConfirmInfoFragment();

		available = securityVerity.confirmFactor(result.getFactorList());

		drawConfirmInfoFragment.set_combinId(_combinId);
		drawConfirmInfoFragment.set_combinId_Name(combinId_name);
		drawConfirmInfoFragment.setAvailable(available);
		drawConfirmInfoFragment.setConversationId(conversationId);
		Bundle bundle = new Bundle();
		bundle.putSerializable("LOANCycleLoanEApplyVerifyReq", req);
		drawConfirmInfoFragment.setArguments(bundle);

		start(drawConfirmInfoFragment);
		LogUtils.i("cq------available------>" + available);
	}

	@Override
	public void drawApplyVerifyFail(ErrorException e) {
		Log.i(TAG, "提款预交易失败！");
		closeProgressDialog();
	}

	@Override
	public void obtainAllChinaBankAccountSuccess(
			List<QueryAllChinaBankAccountRes> res) {
		boolean isPP = false;//是否匹配
		LogUtils.i(TAG, "------------>获取账户列表成功!当前账户" + payAccount);
		if (res.size() > 0) {
			for (QueryAllChinaBankAccountRes queryAllChinaBankAccountRes : res) {
				LogUtils.i(TAG, "------------>获取账户列表成功!---->所有账户"
						+ queryAllChinaBankAccountRes.getAccountNumber());
				if (payAccount.equals(
						queryAllChinaBankAccountRes.getAccountNumber())) {
					account = queryAllChinaBankAccountRes.getAccountNumber();
					accountId = queryAllChinaBankAccountRes.getAccountId() + "";
//					receiptAccount.setChoiceTextContent(NumberUtils.formatCardNumber(account));
					getPresenter().checkCollectionAccount(queryAllChinaBankAccountRes);
					isPP = true;
					LogUtils.i(TAG, "------------>获取账户列表成功!----->有匹配的");
					break;

				} else {
					LogUtils.i(TAG, "------------>获取账户列表成功!---->没有匹配的");
					receiptAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
				}
			}
			if(!isPP){
				closeProgressDialog();
			}
		} else {
			LogUtils.i(TAG, "------------>获取账户列表成功!------->为空");
			receiptAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
			closeProgressDialog();
		}
	}

	@Override
	public void obtainAllChinaBankAccountFail(ErrorException e) {
		Log.i(TAG, "------------>获取账户列表失败!");
		closeProgressDialog();
	}


	//===================2016年10月10日 17:10:28 yx add
	@Override
	public void doCollectionAccountCheckSuccess(CollectionAccountCheckRes result) {
		LogUtils.i(TAG, "yx------------>收款账户检查成功!");
			if("01".equals(result.getCheckResult().get(0))){
				receiptAccount.setChoiceTextContent(NumberUtils.formatCardNumber(account));
			}else{
			receiptAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
			}
        closeProgressDialog();
	}

	@Override
	public void doCollectionAccountCheckFail(ErrorException e) {
		LogUtils.i(TAG, "yx------------>收款账户检查失败!");
		receiptAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
		closeProgressDialog();
	}
	@Override
	public void setPresenter(DrawContract.Presenter presenter) {
	}

	/**
	 * 构造预交易上送参数
	 *
	 */
	private void buildVerifyReq() {
		req = new LOANCycleLoanEApplyVerifyReq();
		req.setConversationId(conversationId);
		req.set_combinId(_combinId);
		req.setQuoteType(mEloanDrawModel.getQuoteType());
		req.setLoanType(mEloanDrawModel.getLoanType());
		req.setQuoteNo(mEloanDrawModel.getQuoteNo());
		req.setLoanCycleAvaAmount(BigDecimal.valueOf(Double
				.parseDouble(mEloanDrawModel.getAvailableAvl())));
		req.setCurrencyCode(mEloanDrawModel.getCurrencyCode());
		req.setAmount(BigDecimal.valueOf(Double.parseDouble(drawAmount
				.getContentMoney())));
		// req.setAmount(BigDecimal.valueOf(Double.parseDouble(drawAmount.getEditWidgetContent())));
		// req.setRemark("APP提款;"+useOffund.getChoiceTextContent()+";");
		req.setRemark(useOffund.getChoiceTextContent());
		req.setLoanCycleToActNum(account);
		req.setToAccountId(accountId);
		if(times>=10){
			req.setLoanPeriod(""+times);
		}else{
			req.setLoanPeriod("0" + times);
		}

		req.setPayType(payType);
		req.setLoanRate(mEloanDrawModel.getRate());
		req.setIssueRepayDate(mEloanDrawModel.getIssueRepayDate());
		req.setPayAccount(payAccount);
		req.setNextRepayDate(mEloanDrawModel.getNextRepayDate());

		// drawConfirmInfoFragment.setLoanCycleLoanEApplyVerifyReq(req);
		getPresenter().setLoanCycleLoanEApplyVerifyReq(req);
		// 点击下一步按钮，跳转到提款确认页
		getPresenter().drawApplyVerify();
		// showLoadingDialog();
	}

	@Override
	protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
		super.onFragmentResult(requestCode, resultCode, data);
		if (resultCode == AccoutFragment.ResultCode) {
			if (data != null) {
				Log.i(TAG, "-------->account" + data.get("account").toString());
				receiptAccount.setChoiceTextContent(NumberUtils
						.formatCardNumber(data.get("account").toString()));
				account = data.get("account").toString();
				accountId = data.get("accountId").toString();
			}
		}
	}

	@Override
	protected DrawPresenter initPresenter() {
		return new DrawPresenter(this);
	}
}
