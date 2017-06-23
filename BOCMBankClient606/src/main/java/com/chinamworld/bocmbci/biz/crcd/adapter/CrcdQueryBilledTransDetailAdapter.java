package com.chinamworld.bocmbci.biz.crcd.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 已出账单查询明细 */
public class CrcdQueryBilledTransDetailAdapter extends BaseAdapter {
	private List<Map<String, String>> list = null;
	private Context context = null;

	public CrcdQueryBilledTransDetailAdapter(Context context, List<Map<String, String>> list) {
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

	public void changeDate(List<Map<String, String>> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holer = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.crcd_wcquery_item, null);
			holer = new ViewHolder();
			holer.outOrInText = (TextView) convertView.findViewById(R.id.crcd_outOrIn);
			holer.transDateText = (TextView) convertView.findViewById(R.id.crcd_bookDate);
			holer.tranAmountText = (TextView) convertView.findViewById(R.id.crcd_wc_tranAmount);
			holer.tranCurrencyText = (TextView) convertView.findViewById(R.id.crcd_wc_tranCurrency);
			holer.bookAmountText = (TextView) convertView.findViewById(R.id.crcd_wc_bookAmount);
			holer.bookCurrencyText = (TextView) convertView.findViewById(R.id.crcd_wc_bookCurrency);
			holer.bookDateText = (TextView) convertView.findViewById(R.id.crcd_wc_bookDate);
			holer.remarkText = (TextView) convertView.findViewById(R.id.crcd_wc_remark);
			convertView.setTag(holer);
		} else {
			holer = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		// 交易日期
		String dealDt = map.get(Crcd.CRCD_DEALDT_RES);
		// 记账日期
		String checkDt = map.get(Crcd.CRCD_CHECKDT_RES);
		// 交易币种
		String dealCcy = map.get(Crcd.CRCD_DEALCCY_RES);
		// 交易金额
		String dealCnt = map.get(Crcd.CRCD_DEALCNT_RES);
		// 账户类型
		String acntType = map.get(Crcd.CRCD_ACNTTYPE_RES);
		// 结算金额
		String balCnt = map.get(Crcd.CRCD_BALCNT_RES);
		//借贷记账标志
		String loanSign = map.get(Crcd.CRCD_LOANSIGN_RES);
		//交易描述
		String dealDesc = map.get(Crcd.CRCD_DEALDESC_RES);
		String outOrIn = null;
		if (ConstantGloble.CRCD_CRED.equals(loanSign)) {
			// 收入
			outOrIn = context.getString(R.string.crcd_wc_in);
		} else if (ConstantGloble.CRCD_DEBT.equals(loanSign) || ConstantGloble.CRCD_NMON.equals(loanSign)) {
			// 支出
			outOrIn = context.getString(R.string.crcd_wc_out);
		}
		String dealCcys = null;
		if (StringUtil.isNull(dealCcy)) {
			dealCcys = "-";
		} else {
			if (LocalData.Currency.containsKey(dealCcy)) {
				dealCcys = LocalData.Currency.get(dealCcy);
			} else {
				dealCcys = "-";
			}
		}
		String dealCnts = null;
		if (StringUtil.isNull(dealCnt)) {
			dealCnts = "-";
		} else {
			if (!StringUtil.isNull(dealCcy)) {
				dealCnts = StringUtil.parseStringCodePattern(dealCcy, dealCnt, 2);
			} else {
				dealCnts = dealCnt;
			}
		}

		String acntTypes = null;
		if (StringUtil.isNull(acntType)) {
			acntTypes = "-";
		} else {
			if (LocalData.Currency.containsKey(acntType)) {
				acntTypes = LocalData.Currency.get(acntType);
			} else {
				acntTypes = "-";
			}
		}

		String bal = null;
		if (StringUtil.isNull(balCnt)) {
			bal = "-";
		} else {
			if (StringUtil.isNull(acntType)) {
				bal = balCnt;
			} else {
				bal = StringUtil.parseStringCodePattern(acntType, balCnt, 2);
			}
		}
		holer.outOrInText.setText(outOrIn);
		holer.transDateText.setText(dealDt);
		holer.tranAmountText.setText(dealCnts);
		holer.tranCurrencyText.setText(dealCcys);
		holer.bookAmountText.setText(bal);
		holer.bookCurrencyText.setText(acntTypes);
		holer.bookDateText.setText(checkDt);
		holer.remarkText.setText(dealDesc);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holer.remarkText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holer.tranCurrencyText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holer.bookCurrencyText);
		return convertView;
	}

	private class ViewHolder {
		private TextView outOrInText = null;
		private TextView transDateText = null;
		private TextView tranAmountText = null;
		private TextView tranCurrencyText = null;
		private TextView bookAmountText = null;
		private TextView bookCurrencyText = null;
		private TextView bookDateText = null;
		private TextView remarkText = null;
	}
}
