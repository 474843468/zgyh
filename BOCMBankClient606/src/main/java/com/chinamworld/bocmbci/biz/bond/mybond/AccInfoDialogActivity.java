package com.chinamworld.bocmbci.biz.bond.mybond;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.biz.bond.acct.BankAcctListActivity;
import com.chinamworld.bocmbci.biz.bond.acct.NoBankAcctActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 账户信息对话框
 * 
 * @author panwe
 * 
 */
public class AccInfoDialogActivity extends BondBaseActivity implements
		OnClickListener {

	/** 0 代表不存在可用金额 */
	private final String UNUSER = "0";
	private final int TAG_BONDACCT_CANCE = 0;
	private final int TAG_PASSWORD_RESET = 1;
	/** 托管账号 **/
	private String bondAcctNum;
	/** 资金账号 */
	private String bankAcctNum;
	/** 资金账户id */
	private String bankAcctId;
	/** 注销账号功能 */
	private boolean isAcctCance = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseDroidApp.getInstanse().setDialogAct(true);
		BondDataCenter.getInstance().addActivity(this);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(lp);
		setLeftButtonPopupGone();

		init();
	}

	private void init() {
		// 接收上一页面出入数据
		Intent it = getIntent();
		bondAcctNum = it.getStringExtra(BONDACCT);
		bankAcctId = it.getStringExtra(BANKACCTID);
		bankAcctNum = it.getStringExtra(BANKACCTNUM);

		//402使用bond_dialog_402.xml
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.bond_dialog_402, null);
		setContentView(contentView);
		TextView tvAccBond = (TextView) contentView
				.findViewById(R.id.textView1);
		TextView tvAccBank = (TextView) contentView
				.findViewById(R.id.textView2);
		TextView tvAccBZ = (TextView) contentView.findViewById(R.id.textView3);
		TextView tvAccMoney = (TextView) contentView
				.findViewById(R.id.textView4);

		LinearLayout btnLayout = (LinearLayout) contentView
				.findViewById(R.id.layout_btn);
		if (Bond.isMorefunction) {
			btnLayout.setVisibility(View.VISIBLE);
		}

		Button btnAcctReset = (Button) contentView.findViewById(R.id.btnReset);
		Button btnMore = (Button) contentView.findViewById(R.id.btnMore);
		btnAcctReset.setOnClickListener(this);
		PopupWindowUtils.getInstance().setshowMoreChooseUpListener(
				BaseDroidApp.getInstanse().getCurrentAct(), btnMore,
				BondDataCenter.btnMore, btnMoreListener);

		tvAccBank.setText(StringUtil.getForSixForString(bankAcctNum));
		tvAccBond.setText(bondAcctNum);
		tvAccBZ.setText("人民币元");
		String isExist = it.getStringExtra("ISEXIST");
		if (!StringUtil.isNull(isExist) && isExist.equals(UNUSER)) {
			tvAccMoney.setText("不可用");
		} else {
			tvAccMoney.setText(StringUtil.parseStringPattern(it.getStringExtra("BALANCE"), 2));
			tvAccMoney.setTextColor(this.getResources().getColor(R.color.fonts_pink));
		}

		ImageView btnFinish = (ImageView) contentView
				.findViewById(R.id.img_exit_accdetail);
		btnFinish.setOnClickListener(this);
	}

	/** popupwd点击事件 **/
	OnClickListener btnMoreListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Integer tag = (Integer) v.getTag();
			switch (tag) {
			case TAG_BONDACCT_CANCE:
				isAcctCance = true;
				break;

			case TAG_PASSWORD_RESET:
				isAcctCance = false;
				break;
			}
			// 请求客户信息
			requestCustomerInfo(bondAcctNum, bankAcctId);
		}

	};

//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		BaseDroidApp.getInstanse().setDialogAct(true);
//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 关闭
		case R.id.img_exit_accdetail:
			AccInfoDialogActivity.this.finish();
			break;

		// 重登记
		case R.id.btnReset:
			// 请求资金账号列表
			requestBankAcctList();
			break;

		}
	}

	/** 资金账号列表返回处理 **/
	@Override
	public void bankAccListCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.bankAccListCallBack(resultObj);
		List<Map<String, Object>> bankAccList = BondDataCenter.getInstance()
				.getBankAccList();
		if (bankAccList == null) {
			// 提示关联
			startActivity(new Intent(this, NoBankAcctActivity.class));
			return;
		}
		Intent it = new Intent(this, BankAcctListActivity.class);
		BondDataCenter.getInstance().setResetup(true);
		it.putExtra(ISOPEN, false);
		startActivity(it);
		overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
	}

	/** 客户信息返回处理 **/
	@Override
	public void customsInfoCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.customsInfoCallBack(resultObj);
		Map<String, Object> customInfoMap = BondDataCenter.getInstance()
				.getCustomInfoMap();
		if (customInfoMap == null) {
			String msg;
			if (isAcctCance) {
				msg = this.getString(R.string.bond_acct_cance_error);
			} else {
				msg = this.getString(R.string.bond_acct_resetpas_error);
			}
			BaseDroidApp.getInstanse().showInfoMessageDialog(msg);
			return;
		}
		Intent it = new Intent();
		it.setClass(this, BondAcctManagerConfirmActivity.class);
		if (isAcctCance) {
			it.putExtra(ISCANCEACCT, true);
		} else {
			it.putExtra(ISCANCEACCT, false);
		}
		startActivity(it);
		overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
	}
}
