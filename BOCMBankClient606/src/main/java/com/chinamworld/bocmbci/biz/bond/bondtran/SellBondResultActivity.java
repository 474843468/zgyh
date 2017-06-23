package com.chinamworld.bocmbci.biz.bond.bondtran;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.biz.bond.mybond.MyBondListActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 卖债券结果页
 * 
 * @author panwe
 * 
 */
public class SellBondResultActivity extends BondBaseActivity {

	/** 主布局 */
	private View mainView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加布局
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_sell_result, null);
		addView(mainView);
		setTitle(this.getString(R.string.bond_tran_title));
		btnBack.setVisibility(View.GONE);
		init();
	}

	private void init() {
		// 接收上页传入数据
		Intent intent = getIntent();
		String tranAmount = intent.getStringExtra(TRANAMOUNT);
		String amount = intent.getStringExtra(AMOUNT);
		String tranprice = intent.getStringExtra(TRANPRICE);
		String tranId = intent.getStringExtra(SEQ);
		// 初始化控件
		TextView tvTranseq = (TextView) mainView.findViewById(R.id.tv_transeq);
		TextView tvBondName = (TextView) mainView
				.findViewById(R.id.tv_bond_name);
		TextView tvBondType = (TextView) mainView
				.findViewById(R.id.tv_bond_type);
		TextView tvEncy = (TextView) mainView.findViewById(R.id.tv_bizhong);
		TextView tvTranMoney = (TextView) mainView.findViewById(R.id.tv_money1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tvTranMoney);
		TextView tvPrice = (TextView) mainView.findViewById(R.id.tv_money2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvPrice);
		TextView tvAmount = (TextView) mainView.findViewById(R.id.tv_money3);

		Map<String, Object> dataMap = BondDataCenter.getInstance()
				.getMyBondDetailMap();
		tvBondType.setText(commSetText(BondDataCenter.bondType_cc.get(dataMap
				.get(Bond.BOND_TYPE))));
		tvBondName.setText(commSetText((String) dataMap
				.get(Bond.MYBOND_SHORTNAME)));
		tvTranMoney.setText(tranAmount);
		tvTranseq.setText(commSetText(tranId));
		tvAmount.setText(StringUtil.parseStringPattern(amount, 2));
		String str_tem=StringUtil.parseStringPattern(tranprice, 2);
		SpannableStringBuilder span = new SpannableStringBuilder(str_tem+"人民币元/每100元面额");
		span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fonts_pink)),0,str_tem.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvPrice.setText(span);
		tvEncy.setText("人民币元");

		Button btnFinish = (Button) mainView.findViewById(R.id.btnConfirm);
		btnFinish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent it = new Intent(SellBondResultActivity.this,
						MyBondListActivity.class);
				// it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				SellBondResultActivity.this.startActivity(it);
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
