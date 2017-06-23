package com.chinamworld.bocmbci.biz.blpt.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.blpt.BillPaymentBaseActivity;
import com.chinamworld.bocmbci.biz.blpt.BlptUtil;
import com.chinamworld.bocmbci.utils.StepTitleUtils;

/**
 * 申请新服务用户须知
 * 
 * @author panwe
 * 
 */
public class BillNewApplyActivity extends BillPaymentBaseActivity implements
		OnClickListener {

	/** 主布局 */
	private View viewContent;
	/** 不接受按钮 */
	private Button btnUnAccept;
	/** 接受按钮 */
	private Button btnAccept;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.blpt_newapply_title));
		// 添加布局
		viewContent = (View) LayoutInflater.from(this).inflate(
				R.layout.blpt_bill_newservice, null);
		BlptUtil.getInstance().addActivity(this);
		addView(viewContent);
		// 右上角按钮赋值
		setText(this.getString(R.string.go_main));
		setRightBtnClick(rightBtnBackmainClick);
		init();
	}

	/** 初始化 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getString(R.string.blpt_applynewbill_step1),
						this.getString(R.string.blpt_applynewbill_step2),
						this.getString(R.string.blpt_applynewbill_step3) });
		StepTitleUtils.getInstance().setTitleStep(1);

		btnUnAccept = (Button) viewContent.findViewById(R.id.btnunaccept);
		btnAccept = (Button) viewContent.findViewById(R.id.btnaccept);
		btnUnAccept.setOnClickListener(this);
		btnAccept.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnunaccept:
			finish();
			break;

		case R.id.btnaccept:
			Intent it = new Intent(this, BillNewApplyMsgAddActivity.class);
			startActivity(it);
			break;
		}
	}

}
