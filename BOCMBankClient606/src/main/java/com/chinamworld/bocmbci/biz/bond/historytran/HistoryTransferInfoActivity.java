package com.chinamworld.bocmbci.biz.bond.historytran;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/***
 * 交易详情
 * 
 * @author panwe
 * 
 */
public class HistoryTransferInfoActivity extends BondBaseActivity {
	
	/** 主布局 */
	private View mainView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加布局
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_historytran_info, null);
		addView(mainView);
		setTitle(this.getString(R.string.bond_transtatue_title));

		TextView tvTranDate = (TextView)mainView.findViewById(R.id.tv_trandate);
		TextView tvshorName = (TextView)mainView.findViewById(R.id.tv_tran_name);
		TextView tvTranType = (TextView)mainView.findViewById(R.id.tv_tran_type);
		TextView tvBondMoney = (TextView)mainView.findViewById(R.id.tv_bondmoney);
		TextView tvPayMoney = (TextView)mainView.findViewById(R.id.tv_paymoney);
		TextView tvblance = (TextView)mainView.findViewById(R.id.tv_blan);
		TextView tvTime = (TextView)mainView.findViewById(R.id.tv_time);
		TextView tvRate = (TextView)mainView.findViewById(R.id.tv_rate);
		TextView tvGetInterest = (TextView)mainView.findViewById(R.id.tv_getInterest);
		TextView tvMinusInterest = (TextView)mainView.findViewById(R.id.tv_minusInterest);
		TextView tvTransFee = (TextView)mainView.findViewById(R.id.tv_transFee);
		
		Map<String, Object> map = BondDataCenter.getInstance().getHistoryDetailMap();
		/** 交易日期   */
		tvTranDate.setText(commSetText((String)map.get(Bond.RE_HISTORYTRAN_QUERY_TRANDATE)));
		/** 债券简称   */
		tvshorName.setText(commSetText((String)map.get(Bond.MYBOND_SHORTNAME)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tvshorName);
		/** 交易类型   */
		tvTranType.setText(commSetText((String)(map.get(Bond.RE_HISTORYTRAN_QUERY_TRANTYPE))));
		/** 债券面额   */
		tvBondMoney.setText((String)map.get(Bond.RE_HISTORYTRAN_QUERY_BONDNUM));
		/** 结算金额   */
		tvPayMoney.setText(StringUtil.parseStringPattern((String)map.get(Bond.RE_HISTORYTRAN_QUERY_TRANPAYMONEY),2));
		/** 余额   */
		tvblance.setText(StringUtil.parseStringPattern((String)map.get(Bond.RE_HISTORYTRAN_QUERY_TRANBALAN),2));
		/** 持有时间   */
		tvTime.setText(commSetText((String)map.get(Bond.RE_HISTORYTRAN_QUERY_TRANHOLDTIME)));
		/** 执行利率   */
		tvRate.setText(StringUtil.parseStringPattern((String)map.get(Bond.RE_HISTORYTRAN_QUERY_TRANTATE),2)+"%");
		/** 应计利息   */
		tvGetInterest.setText(StringUtil.parseStringPattern((String)map.get(Bond.RE_HISTORYTRAN_QUERY_TRANGET),2));
		/** 应扣利息   */
		tvMinusInterest.setText(StringUtil.parseStringPattern((String)map.get(Bond.RE_HISTORYTRAN_QUERY_TRANMINUS),2));
		/** 手续费   */
		tvTransFee.setText(StringUtil.parseStringPattern((String)map.get(Bond.RE_HISTORYTRAN_QUERY_TRANFEE),2));
	}
}
