package com.chinamworld.bocmbci.biz.dept.notmg;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.myreg.MyRegSaveChooseTranInAccActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

public class NotifyManageInfoConfirmActivity extends DeptBaseActivity {

	private LayoutInflater inflater = null;

	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;
	/** 当前账户详情 */
	private Map<String, Object> curDetailContent;
	/** 当前通知详情 */
	private Map<String, Object> curNotifyDetailContent;

	private LinearLayout btnLayout;
	private Button btnCancel;
	private Button btnCheckout;
	/** 转出账户 */
	private String accountId;
	private String accountNumber;
	/** 存折册号列表 */
	private String volumeNumber;
	/** 存单号 */
	private String cdNumber;
	/** 通知存款编号 */
	private String notifyId;
	/** 币种 */
	private String currency;
	/** 支取方式 */
	private String drawMode;
	/** 通知金额 */
	private String drawAmount;
	/** 预约支取日期 */
	private String drawDate;
	/** 通知状态 */
	private String status;
	private String token;

	private LinearLayout cashRemitLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.notify_manage);

		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		inflater = LayoutInflater.from(this); // 加载理财产品布局
		view = inflater.inflate(R.layout.dept_notmg_confirm, null);
		tabcontent.addView(view);

		// 初始化数据
		initData();

		init();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		accOutInfoMap = DeptDataCenter.getInstance().getAccOutInfoMap();
		curDetailContent = DeptDataCenter.getInstance().getCurDetailContent();
		curNotifyDetailContent = DeptDataCenter.getInstance().getCurNotifyDetail();

		accountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		accountNumber = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		volumeNumber = this.getIntent().getStringExtra(Dept.VOLUME_NUMBER);
		cdNumber = this.getIntent().getStringExtra(Dept.CD_NUMBER);

		currency = (String) curDetailContent.get(Comm.CURRENCYCODE);
		drawMode = (String) curDetailContent.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);

		notifyId = (String) curNotifyDetailContent.get(Dept.NOTIFY_ID);
		drawAmount = (String) curNotifyDetailContent.get(Dept.DRAW_AMOUNT);
		drawAmount = StringUtil.parseStringPattern(drawAmount, 2);
		drawDate = (String) curNotifyDetailContent.get(Dept.DRAW_DATE);
		status = (String) curNotifyDetailContent.get(Dept.NOTIFY_STATUS);
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		btnLayout = (LinearLayout) view.findViewById(R.id.dept_btn_layout);
		btnCancel = (Button) view.findViewById(R.id.dept_back_btn);
		btnCheckout = (Button) view.findViewById(R.id.checkout_btn);

		// TextView 定期账户
		TextView accountNoTv = (TextView) view.findViewById(R.id.dept_acc_no_tv);
		accountNoTv.setText(StringUtil.getForSixForString(accountNumber));
		// TextView 存折册号
		TextView volumeNumberTv = (TextView) view.findViewById(R.id.dept_volume_number_tv);
		volumeNumberTv.setText(volumeNumber);
		// TextView 存单序号
		TextView cdNumberTv = (TextView) view.findViewById(R.id.dept_cdnumber_tv);
		cdNumberTv.setText(cdNumber);
		// TextView 币种
		TextView currencyTv = (TextView) view.findViewById(R.id.dept_currency_tv);
		currencyTv.setText(LocalData.Currency.get(currency));
		// TextView 钞汇标志
		cashRemitLayout = (LinearLayout) view.findViewById(R.id.dept_cashremit_layout);
		if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {
			cashRemitLayout.setVisibility(View.INVISIBLE);
		}
		TextView cashRemitTv = (TextView) view.findViewById(R.id.dept_cashremit_tv);
		String strCashRemit = (String) curDetailContent.get(Dept.CASHREMIT);
		cashRemitTv.setText(LocalData.CurrencyCashremit.get(strCashRemit));
		// TextView 存单金额
		TextView bookBalanceTv = (TextView) view.findViewById(R.id.dept_bookbalance_tv);
		String strBookBalance = (String) curDetailContent.get(Dept.BOOKBALANCE);
		bookBalanceTv.setText(StringUtil.parseStringPattern(strBookBalance, 2));
		// TextView 业务品种 根据存期而来 1 为一天通知存款 7 为七天通知存款
		TextView businessTypeTv = (TextView) view.findViewById(R.id.dept_business_type_tv);
		String strTerm = (String) curDetailContent.get(Dept.CD_PERIOD);
		if (strTerm.equalsIgnoreCase(BUSINESSTYPE_ONEDAY) || strTerm.equalsIgnoreCase(BUSINESSTYPE_SEVENDAY)) {
			businessTypeTv.setText(LocalData.CallCDTerm.get(strTerm));
		} else {
			businessTypeTv.setText(strTerm);
		}
		// TextView 约定方式
		TextView promiseWayTv = (TextView) view.findViewById(R.id.dept_promise_way_tv);
		String strConvertType = LocalData.ConventionConvertType.get(drawMode);
		promiseWayTv.setText(strConvertType);
		// TextView 利率
		TextView insertRateTv = (TextView) view.findViewById(R.id.dept_insertrate_tv);
		insertRateTv.setText((String) curDetailContent.get(Dept.INTEREST_RATE));
		// TextView 通知编号
		TextView notifyIdTv = (TextView) view.findViewById(R.id.dept_notifyid_tv);
		notifyIdTv.setText(notifyId);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, notifyIdTv);
		// TextView 预约支取日期
		TextView checkoutDayTv = (TextView) view.findViewById(R.id.dept_promise_checkout_day_tv);
		checkoutDayTv.setText(drawDate);
		// TextView 支取方式
		TextView checkoutWayTv = (TextView) view.findViewById(R.id.dept_checkout_way_tv);
		String strDrawMode = (String) curNotifyDetailContent.get(Dept.DRAW_MODE);
		checkoutWayTv.setText(LocalData.drawMode.get(strDrawMode));
		
		TextView label_notifyMoneyTv = (TextView) view.findViewById(R.id.dept_notify_money_ltv);
		
		if(DRAW_MODE_Y.equals(strDrawMode)) { //如果为全部支取，改变字段名称
			label_notifyMoneyTv.setText("通知金额：");
		} else {
			label_notifyMoneyTv.setText(getResources().getString(R.string.notify_money));
		}
		
		// TextView 通知金额
		TextView notifyMoneyTv = (TextView) view.findViewById(R.id.dept_notify_money_tv);
		if(DRAW_MODE_Y.equals(strDrawMode)) {
			notifyMoneyTv.setText(StringUtil.parseStringPattern(strBookBalance, 2));
		}else{
			notifyMoneyTv.setText(drawAmount);
		}
		// TextView 通知状态
		TextView notifyStatusTv = (TextView) view.findViewById(R.id.dept_notify_status_tv);
		String strStatus = LocalData.notifyStatus.get(status);
		notifyStatusTv.setText(strStatus);

		if (status.equals(NOMARL_STATUS) || status.equals(NOTIFY_STATUS_W)) {// 正常和待销户
			// 支取按钮
			btnLayout.setVisibility(View.VISIBLE);
			btnCancel.setVisibility(View.VISIBLE);
			btnCheckout.setVisibility(View.VISIBLE);
			btnCheckout.setOnClickListener(checkoutListener);
			btnCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					BaseDroidApp.getInstanse().showErrorDialog("通知存款如已办理通知手续而不支取或在通知期限内取消通知的，通知期限内不计息",
							R.string.cancle, R.string.confirm, onBackclicklistener);

//					BaseDroidApp.getInstanse().showMessageDialog("通知存款如已办理通知手续而不支取或在通知期限内取消通知的，通知期限内不计息", new View.OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							BaseDroidApp.getInstanse().dismissMessageDialog();
//							String repealInfo = NotifyManageInfoConfirmActivity.this.getResources().getString(R.string.repeal_infomation);
//							// 发送通讯 撤销通知PsnTransDeleteNotifySumit
//							BaseDroidApp.getInstanse().showErrorDialog(repealInfo, R.string.cancle, R.string.confirm, onclicklistener);
//
//						}
//					});
				}
			});
		}

		if (status.equals(PASS_STATUS)) {// 已逾期
			btnLayout.setVisibility(View.VISIBLE);
			btnCancel.setVisibility(View.VISIBLE);
			// 撤销按钮
			btnCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					BaseDroidApp.getInstanse().showErrorDialog("通知存款如已办理通知手续而不支取或在通知期限内取消通知的，通知期限内不计息",
							R.string.cancle, R.string.confirm, onBackclicklistener);

//					BaseDroidApp.getInstanse().showMessageDialog("通知存款如已办理通知手续而不支取或在通知期限内取消通知的，通知期限内不计息", new View.OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							BaseDroidApp.getInstanse().dismissMessageDialog();
//
//							String repealInfo = NotifyManageInfoConfirmActivity.this.getResources().getString(R.string.repeal_infomation);
//							// 发送通讯 撤销通知PsnTransDeleteNotifySumit
//							BaseDroidApp.getInstanse().showErrorDialog(repealInfo, R.string.cancle, R.string.confirm, onclicklistener);
//						}
//					});
				}
			});
		}
	}

	/**
	 * 支取按钮监听
	 */
	private OnClickListener checkoutListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseHttpEngine.showProgressDialog();
			requestSystemDateTime();
		}
	};
	
	

//	private OnClickListener onclicklistener = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			switch ((Integer) v.getTag()) {
//			case CustomDialog.TAG_CANCLE:// 取消
//				BaseDroidApp.getInstanse().dismissErrorDialog();
//				break;
//			case CustomDialog.TAG_SURE:// 确定
//				// 撤销通知
//				requestCommConversationId();
//				BaseHttpEngine.showProgressDialog();
//				break;
//			default:
//				break;
//			}
//		}
//	};
	
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		BaseHttpEngine.dissMissProgressDialog();
		super.requestSystemDateTimeCallBack(resultObj);
		Intent intent = new Intent();
		intent.putExtra(NOTIFY_FLAG, 1);
		intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
		intent.setClass(NotifyManageInfoConfirmActivity.this, MyRegSaveChooseTranInAccActivity.class);
		startActivity(intent);
	}

	private OnClickListener onBackclicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_CANCLE:// 取消
				BaseDroidApp.getInstanse().dismissErrorDialog();
				break;
			case CustomDialog.TAG_SURE:// 确定
				// 撤销通知
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requestDeleteNotify(accountId, volumeNumber, cdNumber, notifyId, currency, drawMode, drawAmount, drawDate, status, token);
		BaseDroidApp.getInstanse().dismissErrorDialog();
	}

	/**
	 * 通讯返回
	 */
	@Override
	public void communicationCallBack(int flag) {
		switch (flag) {
		case DELETE_NOTIFY_CALLBACK:
			String strNotifyId = NotifyManageInfoConfirmActivity.this.getResources().getString(R.string.notice_no_message);
			String strSuccessRepeal = NotifyManageInfoConfirmActivity.this.getResources().getString(R.string.success_repeal);
			CustomDialog.toastShow(NotifyManageInfoConfirmActivity.this, strNotifyId + notifyId + strSuccessRepeal);
			setResult(100);
			NotifyManageInfoConfirmActivity.this.finish();
			break;

		default:
			break;
		}
	}
}
