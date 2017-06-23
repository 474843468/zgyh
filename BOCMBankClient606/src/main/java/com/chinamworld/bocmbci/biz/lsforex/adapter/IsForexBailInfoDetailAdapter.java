package com.chinamworld.bocmbci.biz.lsforex.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 保证金账户详情----适配器 */
public class IsForexBailInfoDetailAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, String>> list;

	public IsForexBailInfoDetailAdapter(Context context, List<Map<String, String>> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.isforex_bail_bzjinfo_item, null);
			holder = new ViewHolder();
			holder.fsettleCurrencyText = (TextView) convertView.findViewById(R.id.isForex_vfgRegCurrency1);
			holder.marginNetBalanceText = (TextView) convertView.findViewById(R.id.isForex_bzj_netBalance);
			holder.currentProfitLossText = (TextView) convertView.findViewById(R.id.isForex_bzj_currentProfitLoss);
			holder.marginOccupiedText = (TextView) convertView.findViewById(R.id.isForex_bzj_marginOccupied);
			holder.marginAvailableText = (TextView) convertView.findViewById(R.id.isForex_bzj_marginAvailable);
			holder.maxTradeAmountText = (TextView) convertView.findViewById(R.id.isForex_bzj_maxTradeAmount);
			holder.marginRateText = (TextView) convertView.findViewById(R.id.isForex_bzj_marginRate);
			holder.messageText = (TextView) convertView.findViewById(R.id.isForex_bzj_message);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holder.messageText);
			holder.marginFundText = (TextView) convertView.findViewById(R.id.isForex_bzj_marginFund);
			holder.xianChaoText = (TextView) convertView.findViewById(R.id.isForex_bill1);
			holder.xianHuiText = (TextView) convertView.findViewById(R.id.isForex_remit1);
			holder.xianChaoView = convertView.findViewById(R.id.xianchaoView);
			holder.xianHuiView = convertView.findViewById(R.id.xianHuiView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		// 结算币种
		String code = map.get(IsForex.ISFOREX_SETTLECURRENCY1_RES);
		String jsCode = null;
		if (!StringUtil.isNull(code) && LocalData.Currency.containsKey(code)) {
			jsCode = LocalData.Currency.get(code);
		}

		// 保证金净值
		String marginNetBalance = (String) map.get(IsForex.ISFOREX_MARGINNETBALANCE_RES);
		// 账户暂计盈亏
		String currentProfitLoss = (String) map.get(IsForex.ISFOREX_CURRENTPROFITLOSS_RES);
		// 暂计盈亏标志
		String profitLossFlag = map.get(IsForex.ISFOREX_PROFITLOSSFLAG_RES);
		// 占用保证金
		String marginOccupied = (String) map.get(IsForex.ISFOREX_MARGINOCCUPIED_RES);
		// 可用保证金
		String marginAvailable = (String) map.get(IsForex.ISFOREX_MARGINAVAILABLE_RES);
		// 最大可交易额
		String maxTradeAmount = (String) map.get(IsForex.ISFOREX_MAXTRADEAMOUNT_RES);
		// 保证金充足率
		String marginRate = (String) map.get(IsForex.ISFOREX_MARGINRATE_RES);
		// 提示信息
		String message = (String) map.get("alarmFlag");
		// 账户余额
		String marginFund = (String) map.get(IsForex.ISFOREX_MARGINFUND_RES);
		// dealMap.put(ConstantGloble.ISFOREX_CASH1, noteCashFlag1);
		// dealMap.put(ConstantGloble.ISFOREX_CASH2, noteCashFlag2);
		// dealMap.put(ConstantGloble.ISFOREX_AMOUNT1, amount1);
		// dealMap.put(ConstantGloble.ISFOREX_AMOUNT2, amount2);
		String noteCashFlag1 = map.get(ConstantGloble.ISFOREX_CASH1);
		String noteCashFlag2 = map.get(ConstantGloble.ISFOREX_CASH2);
		String amount1 = map.get(ConstantGloble.ISFOREX_AMOUNT1);
		String amount2 = map.get(ConstantGloble.ISFOREX_AMOUNT2);
		if (LocalData.rmbCodeList.contains(code)) {
			// 人民币
			holder.xianChaoView.setVisibility(View.GONE);
			holder.xianHuiView.setVisibility(View.GONE);
		} else {//
			if (!StringUtil.isNull(noteCashFlag1) && StringUtil.isNull(noteCashFlag2)) {
				if (LocalData.isForexcashRemitList.get(0).equals(noteCashFlag1)) {
					// 显示现钞
					holder.xianHuiView.setVisibility(View.GONE);
					holder.xianChaoView.setVisibility(View.VISIBLE);
					if (StringUtil.isNull(amount1)) {
						holder.xianChaoText.setText("-");
					} else {
						String aa = StringUtil.parseStringCodePattern(code, amount1, 2);
						holder.xianChaoText.setText(aa);
					}
				} else if (LocalData.isForexcashRemitList.get(1).equals(noteCashFlag1)) {
					// 显示现汇
					holder.xianChaoView.setVisibility(View.GONE);
					holder.xianHuiView.setVisibility(View.VISIBLE);
					if (StringUtil.isNull(amount1)) {
						holder.xianHuiText.setText("-");
					} else {
						String aa = StringUtil.parseStringCodePattern(code, amount1, 2);
						holder.xianHuiText.setText(aa);
					}
				}

			} else if (!StringUtil.isNull(noteCashFlag2) && StringUtil.isNull(noteCashFlag1)) {
				// 显示现汇
				if (LocalData.isForexcashRemitList.get(0).equals(noteCashFlag2)) {
					// 显示现钞
					holder.xianHuiView.setVisibility(View.GONE);
					holder.xianChaoView.setVisibility(View.VISIBLE);
					if (StringUtil.isNull(amount2)) {
						holder.xianChaoText.setText("-");
					} else {
						String aa = StringUtil.parseStringCodePattern(code, amount2, 2);
						holder.xianChaoText.setText(aa);
					}
				} else if (LocalData.isForexcashRemitList.get(1).equals(noteCashFlag2)) {
					// 显示现汇
					holder.xianChaoView.setVisibility(View.GONE);
					holder.xianHuiView.setVisibility(View.VISIBLE);
					if (StringUtil.isNull(amount2)) {
						holder.xianHuiText.setText("-");
					} else {
						String aa = StringUtil.parseStringCodePattern(code, amount2, 2);
						holder.xianHuiText.setText(aa);
					}
				}
			} else if (!StringUtil.isNull(noteCashFlag1) && !StringUtil.isNull(noteCashFlag2)) {
				holder.xianHuiView.setVisibility(View.VISIBLE);
				holder.xianChaoView.setVisibility(View.VISIBLE);
				if (LocalData.isForexcashRemitList.get(0).equals(noteCashFlag1)) {
					// noteCashFlag1---现钞
					if (StringUtil.isNull(amount1)) {
						holder.xianChaoText.setText("-");
					} else {
						String aa = StringUtil.parseStringCodePattern(code, amount1, 2);
						holder.xianChaoText.setText(aa);
					}
					if (StringUtil.isNull(amount2)) {
						holder.xianHuiText.setText("-");
					} else {
						String aa = StringUtil.parseStringCodePattern(code, amount2, 2);
						holder.xianHuiText.setText(aa);
					}
				} else {
					// noteCashFlag1-----现汇
					if (StringUtil.isNull(amount1)) {
						holder.xianHuiText.setText("-");
					} else {
						String aa = StringUtil.parseStringCodePattern(code, amount1, 2);
						holder.xianHuiText.setText(aa);
					}
					if (StringUtil.isNull(amount2)) {
						holder.xianChaoText.setText("-");
					} else {
						String aa = StringUtil.parseStringCodePattern(code, amount2, 2);
						holder.xianChaoText.setText(aa);
					}
				}

			} else {
				holder.xianChaoView.setVisibility(View.GONE);
				holder.xianHuiView.setVisibility(View.GONE);
			}
		}

		holder.fsettleCurrencyText.setText(jsCode);
		String bzjJZ = StringUtil.parseStringPattern(marginNetBalance, 2);
		holder.marginNetBalanceText.setText(bzjJZ);

		String zjyh = StringUtil.parseStringPattern(currentProfitLoss, 2);
		if (ConstantGloble.ISFOREX_PROFITLOSSFLAG.equals(profitLossFlag)) {
			holder.currentProfitLossText.setText("-" + zjyh);
		} else {
			holder.currentProfitLossText.setText(zjyh);
		}
		String zhZJYK = StringUtil.parseStringPattern(marginOccupied, 2);
		holder.marginOccupiedText.setText(zhZJYK);

		String max = StringUtil.parseStringPattern(maxTradeAmount, 2);
		holder.maxTradeAmountText.setText(max);

		String mar = StringUtil.parseStringPattern(marginAvailable, 2);
		if(StringUtil.isNull(marginAvailable)){
			holder.marginAvailableText.setText("-");
		}else{
			holder.marginAvailableText.setText(mar);
		}
		String rate = StringUtil.dealNumber(marginRate);
		holder.marginRateText.setText(rate);
		if (StringUtil.isNull(message)) {
			holder.messageText.setText("-");
		} else {
			if("Y".equals(message)){
				holder.messageText.setText("保证金不足");
			}else{
				holder.messageText.setText("保证金充足");
			}
				
			
		}

		String m = StringUtil.parseStringCodePattern(code, marginFund, 2);
		if (StringUtil.isNull(marginFund)){
			holder.marginFundText.setText("-");
		}else{
			holder.marginFundText.setText(m);
		}

		return convertView;
	}

	public final class ViewHolder {
		/**
		 * 结算币种
		 */
		public TextView fsettleCurrencyText;
		/**
		 * 保证金净值
		 */
		public TextView marginNetBalanceText;
		/**
		 * 账户暂计盈亏
		 */
		public TextView currentProfitLossText;
		/**
		 * 占用保证金
		 */
		public TextView marginOccupiedText;
		/**
		 * 可用保证金
		 */
		public TextView marginAvailableText;
		/**
		 * 最大可交易额
		 */
		public TextView maxTradeAmountText;
		/**
		 * 保证金充足率
		 */
		public TextView marginRateText;
		/**
		 * 提示信息
		 */
		public TextView messageText;
		/**
		 * 账户余额
		 */
		public TextView marginFundText;
		/** 现钞 */
		public TextView xianChaoText = null;
		/** 现汇 */
		public TextView xianHuiText = null;
		public View xianChaoView = null;
		public View xianHuiView = null;
	}
}
