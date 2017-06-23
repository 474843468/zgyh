package com.chinamworld.bocmbci.biz.acc.relevanceaccount;

import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 信用卡关联成功页
 * 
 * @author wangmengmeng
 * 
 */
public class AccCreditCardSuccessActivity extends AccBaseActivity {
	/** 信用卡关联成功页 */
	private View view;
	/** 待关联账户类型 */
	private TextView acc_relevance_type;
	private String relevance_type;
	/** 待关联账号 */
	private TextView acc_relevance_actnum;
	private String relevance_actnum;
	/** 确定 */
	public Button btn_confirm;
	/** 账户自助关联返回的信用卡信息 */
	private Map<String, String> creditMap;
	/** 账户自助关联预交易返回信息 */
	private Map<String, Object> preResultMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_myaccount_relevance_title));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		// 添加布局
		view = addView(R.layout.acc_relevanceaccount_iccredit_success);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		back.setVisibility(View.INVISIBLE);
		// 初始化界面
		init();
	}

	/** 初始化界面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getString(R.string.acc_relevance_step1),
						this.getString(R.string.acc_relevance_step2),
						this.getString(R.string.acc_relevance_step3) });
		StepTitleUtils.getInstance().setTitleStep(3);
		// 通过账户自助关联结果取到要用的信用卡信息
		preResultMap = AccDataCenter.getInstance().getRelevancePremap();
		creditMap = (Map<String, String>) (preResultMap
				.get(Acc.RELEVANCEACCPRE_ACC_RELEVANCECREDITACCOUNT_RES));
		acc_relevance_type = (TextView) view
				.findViewById(R.id.tv_relevance_type_value);

		acc_relevance_actnum = (TextView) view
				.findViewById(R.id.tv_relevance_actnum_value);
		// relevance_type = creditMap
		// .get(Acc.RELEVANCEACCPRE_ACC_RELEVANCECREDITACCOUNTTYPE_RES);
		relevance_type = (String) preResultMap
				.get(Acc.RELEVANCEACCPRE_ACC_ACCOUNTTYPE_RES);
		acc_relevance_type
				.setText(StringUtil.isNull(relevance_type) ? ConstantGloble.BOCINVT_DATE_ADD
						: LocalData.AccountType.get(relevance_type));
		relevance_actnum = creditMap
				.get(Acc.RELEVANCEACCPRE_ACC_CREDITACCOUNTNUMBER_RES);
		acc_relevance_actnum
				.setText(StringUtil.isNull(relevance_actnum) ? ConstantGloble.BOCINVT_DATE_ADD
						: StringUtil.getForSixForString(relevance_actnum));

		btn_confirm = (Button) view.findViewById(R.id.btnConfirm);
		btn_confirm.setOnClickListener(confirmClickListener);

	}

	/** 确定按钮点击事件 */
	OnClickListener confirmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
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
