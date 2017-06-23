package com.chinamworld.bocmbci.biz.bocinvt.inflateView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

public class InflaterViewDialog {
	private Context currentContent;
	private View contentView;

	public InflaterViewDialog(Context currentContent) {
		this.currentContent = currentContent;
	}

	/**
	 * 购买任务弹出框
	 * 
	 * @param manageOpenClick
	 *            开通投资理财服务点击事件
	 * @param invtBindingClick
	 *            登记账户点击事件
	 * @param invtEvaluationClick
	 *            风险评估点击事件
	 * @return
	 */
	public View judgeViewDialog(boolean isOpen,
			List<Map<String, Object>> investBindingInfo, boolean isevaluatedBefore,
			OnClickListener manageOpenClick, OnClickListener invtBindingClick,
			OnClickListener invtEvaluationClick, OnClickListener exitDialogClick) {
		contentView = LayoutInflater.from(currentContent).inflate(
				R.layout.bocinvt_task_notify, null);
		ImageView taskPopCloseButton = (ImageView) contentView
				.findViewById(R.id.top_right_close);
		taskPopCloseButton.setOnClickListener(exitDialogClick);
		// accButtonView:设置账户按钮
		View accButtonView = contentView
				.findViewById(R.id.bocinvt_acc_button_show);

		// moneyButtonView:理财服务按钮
		View moneyButtonView = contentView
				.findViewById(R.id.bocinvt_money_button_show);
		// 风险评估
		View invtEvaluationInit = contentView
				.findViewById(R.id.bocinvt_InvtEvaluationInit_button_show);
		// 风险评估文本框
		View invtEvaluationInitTextView = contentView
				.findViewById(R.id.bocinvt_InvtEvaluationInit_text_hide);
		// accTextView:设置账户文本框
		View accTextView = contentView.findViewById(R.id.bocinvt_acc_text_hide);
		// moneyTextView:理财服务文本框
		View moneyTextView = contentView
				.findViewById(R.id.bocinvt_money_text_hide);
		// 先判断是否开通投资理财服务
		if (isOpen) {
			// 开通投资理财服务
			moneyButtonView.setVisibility(View.GONE);
			moneyTextView.setVisibility(View.VISIBLE);
		} else {
			// 没有开通投资理财服务
			moneyButtonView.setVisibility(View.VISIBLE);
			moneyTextView.setVisibility(View.GONE);
			moneyButtonView.setOnClickListener(manageOpenClick);
			invtEvaluationInit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					CustomDialog.toastInCenter(BaseDroidApp.getInstanse()
							.getCurrentAct(),
							BaseDroidApp.getInstanse().getCurrentAct()
									.getString(R.string.bocinvt_task_toast_1));
				}
			});
			accButtonView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					CustomDialog.toastInCenter(BaseDroidApp.getInstanse()
							.getCurrentAct(),
							BaseDroidApp.getInstanse().getCurrentAct()
									.getString(R.string.bocinvt_task_toast_2));
				}
			});
		}
		if (isevaluatedBefore) {
			// 用户进行了风险评估
			invtEvaluationInit.setVisibility(View.GONE);
			invtEvaluationInitTextView.setVisibility(View.VISIBLE);
		} else {
			// 用户没有进行风险评估
			invtEvaluationInit.setVisibility(View.VISIBLE);
			invtEvaluationInitTextView.setVisibility(View.GONE);
			if (isOpen) {
				invtEvaluationInit.setOnClickListener(invtEvaluationClick);
				accButtonView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						CustomDialog.toastInCenter(
								BaseDroidApp.getInstanse().getCurrentAct(),
								BaseDroidApp
										.getInstanse()
										.getCurrentAct()
										.getString(
												R.string.bocinvt_task_toast_2));
					}
				});
			}

		}
		if (StringUtil.isNullOrEmpty(investBindingInfo)) {
			// 用户没有登记账户,必须显示设定账户按钮
			accButtonView.setVisibility(View.VISIBLE);
			accTextView.setVisibility(View.GONE);
			if (isOpen && isevaluatedBefore) {
				accButtonView.setOnClickListener(invtBindingClick);
			}
		} else {
			// 用户登记账户
			accButtonView.setVisibility(View.GONE);
			accTextView.setVisibility(View.VISIBLE);
		}

		return contentView;
	}
	
	/**
	 * 购买任务弹出框,根据参数不同选择性的展示需要的任务，其他不展示
	 * 
	 * @param isOpen 是否开通投资理财
	 * @param manageOpenClick，开通投资理财服务点击事件，传   null 不展示   "开通投资理"   任务,此时isOpen无意义
	 * 
	 * @param investBindingInfo 账户登记信息
	 * @param invtBindingClick，登记账户点击事件，传   null  不展示   "登记账户"   任务,此时investBindingInfo无意义
	 * 
	 * @param isevaluatedBefore 是否进行过风险评估
	 * @param invtEvaluationClick，风险评估点击事件，传    null 不展示   "风险评估"   任务,此时isevaluatedBefore无意义
	 * 
	 * @param exitDialogClick 任务框关闭退出点击事件
	 * @return
	 */
	public View judgeViewDialog_choice(boolean isOpen,
			List<Map<String, Object>> investBindingInfo, boolean isevaluatedBefore,
			OnClickListener manageOpenClick, OnClickListener invtBindingClick,
			OnClickListener invtEvaluationClick, OnClickListener exitDialogClick) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.bocinvt_task_notify, null);
		ImageView taskPopCloseButton = (ImageView) contentView.findViewById(R.id.top_right_close);
		taskPopCloseButton.setOnClickListener(exitDialogClick);
		
		
		// accButtonView:设置账户按钮
		View accButtonView = contentView.findViewById(R.id.bocinvt_acc_button_show);//账户未登记按钮
		// moneyButtonView:理财服务按钮
		View moneyButtonView = contentView.findViewById(R.id.bocinvt_money_button_show);//未开通投资理财服务按钮
		// 风险评估
		View invtEvaluationInit = contentView.findViewById(R.id.bocinvt_InvtEvaluationInit_button_show);//风险未评估按钮
		
		
		// 风险评估文本框
		View invtEvaluationInitTextView = contentView.findViewById(R.id.bocinvt_InvtEvaluationInit_text_hide);//风险已评估文本框
		// accTextView:设置账户文本框
		View accTextView = contentView.findViewById(R.id.bocinvt_acc_text_hide);//账户已登记文本框
		// moneyTextView:理财服务文本框
		View moneyTextView = contentView.findViewById(R.id.bocinvt_money_text_hide);//已开通投资理财服务文本框
		
		// 先判断是否开通投资理财服务
		if (StringUtil.isNullOrEmpty(manageOpenClick)) {//不展示   "开通投资理"   任务
			moneyButtonView.setVisibility(View.GONE);
			moneyTextView.setVisibility(View.GONE);
		}else {
			if (isOpen) {
				// 开通投资理财服务
				moneyButtonView.setVisibility(View.GONE);
				moneyTextView.setVisibility(View.VISIBLE);
			} else {
				// 没有开通投资理财服务
				moneyButtonView.setVisibility(View.VISIBLE);
				moneyTextView.setVisibility(View.GONE);
				moneyButtonView.setOnClickListener(manageOpenClick);
				invtEvaluationInit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						CustomDialog.toastInCenter(BaseDroidApp.getInstanse()
								.getCurrentAct(),
								BaseDroidApp.getInstanse().getCurrentAct()
								.getString(R.string.bocinvt_task_toast_1));
					}
				});
				accButtonView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						CustomDialog.toastInCenter(BaseDroidApp.getInstanse()
								.getCurrentAct(),
								BaseDroidApp.getInstanse().getCurrentAct()
								.getString(R.string.bocinvt_task_toast_2));
					}
				});
			}
		}
		
		//账户登记
		if (StringUtil.isNullOrEmpty(invtBindingClick)) {//不显示   账户登记   任务
			accButtonView.setVisibility(View.GONE);
			accTextView.setVisibility(View.GONE);
		}else {
			if (StringUtil.isNullOrEmpty(investBindingInfo)) {
				// 用户没有登记账户,必须显示设定账户按钮
				accButtonView.setVisibility(View.VISIBLE);
				accTextView.setVisibility(View.GONE);
				accButtonView.setOnClickListener(invtBindingClick);
				
//				if (isOpen && isevaluatedBefore) {
//					accButtonView.setOnClickListener(invtBindingClick);
//				}
			} else {
				// 用户登记账户
				accButtonView.setVisibility(View.GONE);
				accTextView.setVisibility(View.VISIBLE);
			}
		}
		
		
		if (StringUtil.isNullOrEmpty(invtEvaluationClick)) {//不显示    风险评估    任务
			invtEvaluationInit.setVisibility(View.GONE);
			invtEvaluationInitTextView.setVisibility(View.GONE);
		}else {
			if (isevaluatedBefore) {
				// 用户进行了风险评估
				invtEvaluationInit.setVisibility(View.GONE);
				invtEvaluationInitTextView.setVisibility(View.VISIBLE);
			} else {
				// 用户没有进行风险评估
				invtEvaluationInit.setVisibility(View.VISIBLE);
				invtEvaluationInitTextView.setVisibility(View.GONE);
				invtEvaluationInit.setOnClickListener(invtEvaluationClick);
//				if (isOpen) {
//					invtEvaluationInit.setOnClickListener(invtEvaluationClick);
//					accButtonView.setOnClickListener(new OnClickListener() {
//						
//						@Override
//						public void onClick(View arg0) {
//							CustomDialog.toastInCenter(
//									BaseDroidApp.getInstanse().getCurrentAct(),
//									BaseDroidApp
//									.getInstanse()
//									.getCurrentAct()
//									.getString(
//											R.string.bocinvt_task_toast_2));
//						}
//					});
//				}
			}
		}
		return contentView;
	}
	

	/**
	 * 重新登记账户弹出框
	 * 
	 * @param invtBindingInfo
	 *            账户信息
	 * @param bookbalance
	 *            余额
	 * @param newBindingClick
	 *            重新登记
	 * @param exitDialogClick
	 *            退出弹出框
	 * @return
	 */
	public View newBindingViewDialog(Map<String, Object> invtBindingInfo,
			List<Map<String, Object>> balanceList,
			OnClickListener newBindingClick, OnClickListener exitDialogClick) {
		contentView = LayoutInflater.from(currentContent).inflate(
				R.layout.boci_mybankaccount_detail, null);
		TextView acc_account_number_value = (TextView) contentView
				.findViewById(R.id.acc_account_number_value);
		TextView acc_accountnickname_value = (TextView) contentView
				.findViewById(R.id.acc_accountnickname_value);
		TextView acc_accounttype_value = (TextView) contentView
				.findViewById(R.id.acc_accounttype_value);
		LinearLayout bocinvt_currency = (LinearLayout) contentView
				.findViewById(R.id.bocinvt_currency);
		// 赋值
		String accNumber = (String) invtBindingInfo
				.get(BocInvt.ACCOUNTNO);
		acc_account_number_value
				.setText(StringUtil.isNull(accNumber) ? ConstantGloble.BOCINVT_DATE_ADD
						: StringUtil.getForSixForString(accNumber));
		acc_accountnickname_value.setText((String) invtBindingInfo
				.get(BocInvt.BOCIBINDING_ACCOUNTNICKNAME_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(currentContent,
				acc_accountnickname_value);
		String accounttype = (String) invtBindingInfo
				.get(BocInvt.BOCIBINDING_ACCOUNTTYPEF_RES);
		acc_accounttype_value
				.setText(StringUtil.isNull(accounttype) ? ConstantGloble.BOCINVT_DATE_ADD
						: LocalData.AccountType.get(accounttype));
		List<Map<String, Object>> accdetailList = new ArrayList<Map<String, Object>>();
		if (balanceList == null || balanceList.size() == 0) {

		} else {
			for (int i = 0; i < balanceList.size(); i++) {
				String currencyname = (String) balanceList.get(i).get(
						Acc.DETAIL_CURRENCYCODE_RES);
				// 过滤
				if (StringUtil.isNull(LocalData.currencyboci.get(currencyname))) {
					accdetailList.add(balanceList.get(i));
				}
			}
		}
		if (accdetailList == null || accdetailList.size() == 0) {

		} else {
			for (int i = 0; i < accdetailList.size(); i++) {
				View currency_view = LayoutInflater.from(currentContent)
						.inflate(R.layout.bocinvt_currency_balance, null);
				bocinvt_currency.addView(currency_view, i);

				TextView tv_acc_currencycode = (TextView) currency_view
						.findViewById(R.id.acc_currencycode);
				TextView tv_acc_accbookbalance = (TextView) currency_view
						.findViewById(R.id.acc_bookbalance);
				String currencyname = (String) accdetailList.get(i).get(
						Acc.DETAIL_CURRENCYCODE_RES);
				String cashRemit = (String) accdetailList.get(i).get(
						Acc.DETAIL_CASHREMIT_RES);
				PopupWindowUtils.getInstance().setOnShowAllTextListener(
						BaseDroidApp.getInstanse().getCurrentAct(),
						tv_acc_accbookbalance);
				PopupWindowUtils.getInstance().setOnShowAllTextListener(
						BaseDroidApp.getInstanse().getCurrentAct(),
						tv_acc_currencycode);
				if (LocalData.Currency.get(currencyname).equals(
						ConstantGloble.ACC_RMB)) {
					tv_acc_currencycode.setText(LocalData.Currency
							.get(currencyname) + ConstantGloble.ACC_COLON);
				} else {
					tv_acc_currencycode.setText(LocalData.Currency
							.get(currencyname)
							+ ConstantGloble.ACC_STRING
							+ LocalData.CurrencyCashremit.get(cashRemit)
							+ ConstantGloble.ACC_COLON);
				}
				tv_acc_accbookbalance.setText(StringUtil
						.parseStringCodePattern(
								currencyname,
								(String) accdetailList.get(i).get(
										Acc.DETAIL_AVAILABLEBALANCE_RES), 2));
			}
		}
		Button newBinding = (Button) contentView
				.findViewById(R.id.btn_for_payment);
		newBinding.setOnClickListener(newBindingClick);
		ImageView taskPopCloseButton = (ImageView) contentView
				.findViewById(R.id.img_exit_accdetail);
		taskPopCloseButton.setOnClickListener(exitDialogClick);
		return contentView;
	}

	/**
	 * 登记账户弹出框
	 * 
	 * @param newBindingClick
	 *            登记账户
	 * @param exitDialogClick
	 *            退出弹出框
	 * @return
	 */
	public View bindingAccDialog(OnClickListener newBindingClick,
			OnClickListener exitDialogClick) {
		contentView = LayoutInflater.from(currentContent).inflate(
				R.layout.bocinvt_binding_acc, null);
		Button newBinding = (Button) contentView
				.findViewById(R.id.btn_for_payment);
		newBinding.setOnClickListener(newBindingClick);
		ImageView taskPopCloseButton = (ImageView) contentView
				.findViewById(R.id.img_exit_accdetail);
		taskPopCloseButton.setOnClickListener(exitDialogClick);
		return contentView;
	}
}
