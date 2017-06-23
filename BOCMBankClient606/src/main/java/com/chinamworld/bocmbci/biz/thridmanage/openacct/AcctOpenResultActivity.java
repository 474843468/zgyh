package com.chinamworld.bocmbci.biz.thridmanage.openacct;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdManagerBaseActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 开户结果页
 * 
 * @author panwe
 * 
 */
public class AcctOpenResultActivity extends ThirdManagerBaseActivity {

	/*** 主布局 **/
	private View viewContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.third_openacc_open));
		// 添加布局
		viewContent = LayoutInflater.from(this).inflate(R.layout.third_openacc_result, null);
		addView(viewContent);
		setdate();
	}

	private void setdate() {
		// 右上角按钮赋值
		setTitleRightText(getString(R.string.go_main));
		setRightBtnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goMainActivity();
			}
		});
		//隐藏返回
		findViewById(R.id.ib_back).setVisibility(View.INVISIBLE);
		/** 银行账户 */
		TextView mBankAccoutView = (TextView) viewContent.findViewById(R.id.tv_bank_account);
		/** 证券公司 */
		TextView mStockView = (TextView) viewContent.findViewById(R.id.tv_stock_company);
		/** 营业部所在地区 */
		TextView mDepartmentRegionView = (TextView) viewContent.findViewById(R.id.tv_department_region);
		/** 营业部名称 */
		TextView mDepartmentNameView = (TextView) viewContent.findViewById(R.id.tv_department_name);
		/** 营业部地址 */
		TextView mDepartmentAddressView = (TextView) viewContent.findViewById(R.id.tv_department_address);
		/** 营业部联系人 */
		TextView mDepartmentLineNameView = (TextView) viewContent.findViewById(R.id.tv_department_link_name);
		/** 营业部联系电话 */
		TextView mDepartmentLinePhoneView = (TextView) viewContent.findViewById(R.id.tv_department_link_phone);

		Bundle b = getIntent().getExtras();
		String accNum = b.getString("accNum");
		String stockBranchName = b.getString("stockBranchName");
		String departmentRegion = b.getString("departmentRegion");
		String departmentName = b.getString("departmentName");
		String departmentAddr = b.getString("departmentAddr");
		String departmentLinkman = b.getString("departmentLinkman");
		String departmentLinkMobile = b.getString("departmentLinkMobile");

		mBankAccoutView.setText(StringUtil.getForSixForString(accNum));
		mStockView.setText(stockBranchName);
		mDepartmentRegionView.setText(departmentRegion);
		mDepartmentNameView.setText(departmentName);
		mDepartmentAddressView.setText(departmentAddr);
		mDepartmentLineNameView.setText(departmentLinkman);
		mDepartmentLinePhoneView.setText(departmentLinkMobile);
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mStockView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mDepartmentRegionView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mDepartmentNameView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mDepartmentAddressView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mDepartmentLinePhoneView);

		TextView departmentRegionLable = (TextView) findViewById(R.id.tv_department_region_lable);
		TextView departmentLinkPhoneLable = (TextView) findViewById(R.id.tv_department_link_phone_lable);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, departmentRegionLable);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, departmentLinkPhoneLable);
		
		
		Button btnFinish = (Button) viewContent.findViewById(R.id.btnfinish);
		btnFinish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openAccActivity();
			}
		});
	}
	
	@Override
	protected void titleBackClick() {
		openAccActivity();
	}

	@Override
	public void onBackPressed() {
		openAccActivity();
	}
	
	private void openAccActivity() {
		ActivityTaskManager.getInstance().removeAllActivity();
		Intent intent = new Intent(AcctOpenResultActivity.this, OpenAccActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}
}
