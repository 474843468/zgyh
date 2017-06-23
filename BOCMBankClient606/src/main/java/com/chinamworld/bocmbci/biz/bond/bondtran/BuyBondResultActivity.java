package com.chinamworld.bocmbci.biz.bond.bondtran;

import java.util.Map;

import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 买债券结果页
 * 
 * @author panwe
 * 
 */
public class BuyBondResultActivity extends BondBaseActivity implements
		OnClickListener {

	/** 主布局 */
	private View mainView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加布局
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_buy_result, null);
		addView(mainView);
		setTitle(this.getString(R.string.bond_tran_title));
		btnBack.setVisibility(View.GONE);
		inIt();
	}

	private void inIt() {
		// 接受上页传入数据
		int mPostion = getIntent().getIntExtra(POSITION, 0);
		String tranAmount = getIntent().getStringExtra(TRANAMOUNT);
		String tranType = getIntent().getStringExtra(TRANTYPE);

		TextView tvTranSeq = (TextView) mainView.findViewById(R.id.tv_transeq);
		TextView tvBondType = (TextView) mainView
				.findViewById(R.id.tv_bond_type);
		TextView tvBondName = (TextView) mainView
				.findViewById(R.id.tv_bond_name);
		TextView tvTranType = (TextView) mainView
				.findViewById(R.id.tv_trantype);
		TextView tvENey = (TextView) mainView.findViewById(R.id.tv_bizhong);
		TextView tvBanlan = (TextView) mainView.findViewById(R.id.tv_money1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvBanlan);
		TextView tvPrice = (TextView) mainView.findViewById(R.id.tv_money2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvPrice);
		TextView tvAmount = (TextView) mainView.findViewById(R.id.tv_money3);

		TextView tvTip = (TextView) mainView.findViewById(R.id.tv_bill_tip);
		if (tranType.equals(Bond.BOND_TRANTYPE_BUY)) {
			tvTip.setText(this.getString(R.string.bond_buy_result_tip1));
		} else {
			tvTip.setText(this.getString(R.string.bond_buy_result_tip2));
		}

		Button btnConfirm = (Button) mainView.findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(this);

		// 行情数据
		Map<String, Object> bondMap = BondDataCenter.getInstance()
				.getBondList().get(mPostion);

		// 预交易返回数据
		Map<String, Object> resultMap = (Map<String, Object>) BondDataCenter
				.getInstance().getBuyComitResult();

		if (StringUtil.isNull((String) resultMap.get(Bond.BOND_TYPE))) {
			tvBondType.setText(BondDataCenter.bondType_hq.get("2"));
		} else {
			tvBondType.setText(commSetText(BondDataCenter.bondType_hq
					.get(resultMap.get(Bond.BOND_TYPE))));
		}
		tvBondName.setText(commSetText((String) bondMap
				.get(Bond.BOND_SHORTNAME)));
		tvTranType.setText(commSetText(BondDataCenter.tranType.get(tranType)));
		tvENey.setText("人民币元");
		tvBanlan.setText(tranAmount);
		tvTranSeq.setText(commSetText((String) resultMap
				.get(Bond.SELL_RESULT_TRANID)));
		String str_tem=StringUtil.parseStringPattern((String) resultMap.get(Bond.SELL_CONFIRM_TRANPRICE), 2);
		String str_tem_1=str_tem+"人民币元/每100元面额";
		SpannableStringBuilder span=new SpannableStringBuilder(str_tem_1);
		span.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.fonts_pink)),0,str_tem.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvPrice.setText(span);
		tvAmount.setText(StringUtil.parseStringPattern(
				(String) resultMap.get(Bond.SELL_CONFIRM_TRANAMOUT), 2));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		BondDataCenter.getInstance().finshActivity();
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
