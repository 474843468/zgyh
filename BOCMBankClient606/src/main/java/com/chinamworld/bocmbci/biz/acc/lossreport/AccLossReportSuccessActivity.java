package com.chinamworld.bocmbci.biz.acc.lossreport;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 临时挂失成功页
 * 
 * @author wangmengmeng
 * 
 */
public class AccLossReportSuccessActivity extends AccBaseActivity {
	/** 临时挂失成功页 */
	private View view;
	/** 账户类型 */
	private TextView acc_loss_type;
	/** 账号 */
	private TextView acc_loss_actnum;
	/** 账户别名 */
	private TextView acc_loss_nicknames;
	// 借记卡临时挂失优化
	/** 是否冻结主账户 */
	private TextView acc_flozen_value;
	/** 冻结主账户日期布局 */
	private View ll_acc_valid_date;
	/** 冻结主账户日期 */
	private TextView acc_flozen_data_value;

	/** 确定 */
	private Button btn_confirm;
	/** 进行临时挂失账户信息 */
	private Map<String, Object> lossReportMap;
	/** 临时挂失期限 */
	private String lossDays;
	/** 临时挂失  */
	private TextView acc_loss_lossDays;
	private TextView tv_success_title;
	/** 是否冻结主账户布局 */
	private LinearLayout acc_flozen_card;

	// // 是否冻结主账户
	// private boolean isAccFlozenFlag;
	// /** 借记卡临时挂失是否成功 true成功 false失败 */
	// private String reportLossStatus;
	// /** 借记卡临时挂失是否成功 true成功 false失败 */
	// private String accFrozenStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_lossreport_title));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		// 添加布局
		view = addView(R.layout.acc_lossreport_success);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		back.setVisibility(View.GONE);
		// 初始化界面
		init();
	}

	/** 初始化界面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getString(R.string.acc_debit_step1), this.getString(R.string.acc_debit_step2),
						this.getString(R.string.acc_debit_step3) });
		StepTitleUtils.getInstance().setTitleStep(3);
		// 得到选择的账户信息
		lossReportMap = AccDataCenter.getInstance().getLossReportMap();
		lossDays = this.getIntent().getStringExtra(Acc.LOSSCONFIRM_LOSSDAYS_REQ);
		/** 是否冻结主账户 */
		boolean isAccFlozenFlag = this.getIntent().getBooleanExtra(Acc.LOSSCONFIRM_ACC_FLOZENFLAG, false);
		/** 借记卡临时挂失是否成功 */
		boolean reportLossStatus = this.getIntent().getBooleanExtra(Acc.LOSSRESULT_ACC_REPORTLOSS_STATUS, false);
		/** 冻结是否成功 */
		boolean accFrozenStatus = this.getIntent().getBooleanExtra(Acc.LOSSRESULT_ACC_FROZEN_STATUS, false);

		tv_success_title = (TextView) view.findViewById(R.id.tv_success_title_2);
		
		acc_loss_type = (TextView) view.findViewById(R.id.tv_acc_loss_type_value);
		String loss_type = String.valueOf(lossReportMap.get(Acc.ACC_ACCOUNTTYPE_RES));
		acc_loss_type.setText(LocalData.AccountType.get(loss_type));
		acc_loss_actnum = (TextView) view.findViewById(R.id.tv_acc_loss_actnum_value);
		String loss_actnum = String.valueOf(lossReportMap.get(Acc.ACC_ACCOUNTNUMBER_RES));
		acc_loss_actnum.setText(StringUtil.getForSixForString(loss_actnum));
		acc_loss_nicknames = (TextView) view.findViewById(R.id.tv_acc_loss_nicknames);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, acc_loss_nicknames);
		acc_loss_nicknames.setText(String.valueOf(lossReportMap.get(Acc.ACC_NICKNAME_RES)));
		acc_loss_lossDays = (TextView) view.findViewById(R.id.tv_acc_loss_lossDays);

		btn_confirm = (Button) view.findViewById(R.id.btnConfirm);
		btn_confirm.setOnClickListener(confirmClickListener);

		acc_flozen_value = (TextView) view.findViewById(R.id.tv_acc_flozen_value);
		acc_flozen_data_value = (TextView) view.findViewById(R.id.tv_acc_flozen_date_value);
		ll_acc_valid_date = view.findViewById(R.id.acc_flozen_date_layout);

		/** 有效期和是否冻结主账户的弹框设置 */
		TextView tv_acc_flozen_date = (TextView) view.findViewById(R.id.tv_acc_flozen_date);
		TextView tv_acc_flozen = (TextView) view.findViewById(R.id.tv_acc_flozen_values);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_acc_flozen_date);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_acc_flozen);
		acc_flozen_card = (LinearLayout) view.findViewById(R.id.ll_acc_flozen_card);

		 

		/**
		 * reportLossStatus 借记卡临时挂失是否成功 accFrozenStatus 账户借方冻结是否成功
		 * 
		 * 说明：如果AccFlozenFlag上送Y，则返回字段reportLossStatus和accFrozenStatus两个值是有意义的。
		 * 如果AccFlozenFlag上送N，则返回字段reportLossStatus有意义，accFrozenStatus没有意义。
		 */
		// 借记卡改造:备注：挂失、冻结逻辑判断分三种：1.挂失成功，冻结成功；2.挂失成功，冻结失败；3.挂失失败，冻结失败

		if (isAccFlozenFlag) { // 单选按钮 是 (冻结主账户)
			// 冻结主账户 借记卡临时挂失是否成功 (是) reportLossStatus = true;
			if (reportLossStatus) {
				// 挂失成功 借记卡临时挂失是否成功 (是) accFrozenStatus = true;
				if (accFrozenStatus) {
					// 主账户冻结成功
					if (lossDays.equals(lossDaysList.get(0))) {
						// 选择5天
						// 临时挂失成功，冻结成功，请5日内到柜台办理正式挂失
						tv_success_title.setText(this.getString(R.string.acc_loss_success_title_1));
						
						acc_loss_lossDays.setText(lossDaysMap.get(lossDays));
						acc_flozen_card.setVisibility(View.VISIBLE);
						ll_acc_valid_date.setVisibility(View.VISIBLE);
						acc_flozen_value.setText("是");
						acc_flozen_data_value.setText(lossDaysMap.get(lossDays));
					} else {
						// 长期
						// 临时挂失成功，冻结成功，请尽快内到柜台办理正式挂失
						tv_success_title.setText(this.getString(R.string.acc_loss_success_title_2));
						
						acc_loss_lossDays.setText(lossDaysMap.get(lossDays));
						acc_flozen_card.setVisibility(View.VISIBLE);
						ll_acc_valid_date.setVisibility(View.VISIBLE);
						acc_flozen_value.setText("是");
						acc_flozen_data_value.setText(lossDaysMap.get(lossDays));
					}
				} else {
					// 临时挂失成功 主账户冻结失败
					if (lossDays.equals(lossDaysList.get(0))) {
						// 5天
						// 临时挂失成功，冻结失败，请5日内到柜台办理正式挂失
						tv_success_title.setText(this.getString(R.string.acc_loss_success_title_3));
						
						acc_loss_lossDays.setText(lossDaysMap.get(lossDays));
						acc_flozen_card.setVisibility(View.VISIBLE);
						ll_acc_valid_date.setVisibility(View.VISIBLE);
						acc_flozen_value.setText("是");
						acc_flozen_data_value.setText("失败");
					} else {
						// 长期
						// 临时挂失成功，冻结失败，请尽快内到柜台办理正式挂失
						tv_success_title.setText(this.getString(R.string.acc_loss_success_title_4));
						
						acc_loss_lossDays.setText(lossDaysMap.get(lossDays));
						acc_flozen_card.setVisibility(View.VISIBLE);
						ll_acc_valid_date.setVisibility(View.VISIBLE);
						acc_flozen_value.setText("是");
						acc_flozen_data_value.setText("失败");
					}
				}
			} else {
				// 临时卡 挂失失败 accFrozenStatus = true;
				// 临时挂失失败，请重新进行挂失操作或到柜台办理正式挂失
				tv_success_title.setText(this.getString(R.string.acc_loss_success_title_6));
				
				acc_loss_lossDays.setText("失败");
				acc_flozen_card.setVisibility(View.GONE);
				ll_acc_valid_date.setVisibility(View.VISIBLE);
				//acc_flozen_value.setText("是");
				acc_flozen_data_value.setText("失败");
			}
		} else {
			// 单选按钮 否 (不冻结主账户)
			// 临时挂失成功 不冻结主账户 reportLossStatus = true;
			if (reportLossStatus) {
				// 挂失成功
				if (lossDays.equals(lossDaysList.get(0))) {
					// 5天
					// 临时挂失成功，请5日内到柜台办理正式挂失
					tv_success_title.setText(this.getString(R.string.acc_loss_success_title_7));
					
					acc_loss_lossDays.setText(lossDaysMap.get(lossDays));
					acc_flozen_card.setVisibility(View.VISIBLE);
					ll_acc_valid_date.setVisibility(View.GONE);
					 acc_flozen_value.setText("否");
					//acc_flozen_data_value.setText("失败");
				} else {
					// 长期
					// 临时挂失成功，请尽快内到柜台办理正式挂失
					tv_success_title.setText(this.getString(R.string.acc_loss_success_title_8));
					
					acc_loss_lossDays.setText(lossDaysMap.get(lossDays));
					acc_flozen_card.setVisibility(View.VISIBLE);
					ll_acc_valid_date.setVisibility(View.GONE);
					 acc_flozen_value.setText("否");
					//acc_flozen_data_value.setText("失败");
				}
			} else {
				// 挂失失败
				// 临时挂失失败，请重新进行挂失操作或到柜台办理正式挂失
				tv_success_title.setText(this.getString(R.string.acc_loss_success_title_5));
				
				acc_loss_lossDays.setText("失败");
				acc_flozen_card.setVisibility(View.GONE);
				ll_acc_valid_date.setVisibility(View.GONE);
				//acc_flozen_value.setText("是");
				//acc_flozen_data_value.setText("失败");
			}
		}
	}

	/** 确定按钮点击事件 */
	OnClickListener confirmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent = new Intent(AccLossReportSuccessActivity.this, AccDebitCardLossActivity.class);
			startActivity(intent);
		}
	};
	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 回主页面
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
}
