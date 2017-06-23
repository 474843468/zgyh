package com.chinamworld.bocmbci.biz.crcd.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 未出账单查询适配器------不需要分期按钮
 * 
 * @author huangyuchao
 * 
 */
public class CrcdTransferAdapter extends BaseAdapter {

	/** 账户交易列表信息 */
	private List<Map<String, Object>> transferList;
	private Context context;	
	int isShow;
	private Boolean isWrOrYr=true;
	/** 分期点击事件 */
	private OnItemClickListener oncrcdTransDivideListener;

	public CrcdTransferAdapter(Context context, List<Map<String, Object>> transferList, int isShow) {
		this.context = context;
		this.transferList = transferList;
		this.isShow = isShow;
	}

	public Boolean getIsWrOrYr() {
		return isWrOrYr;
	}

	public void setIsWrOrYr(Boolean isWrOrYr) {
		this.isWrOrYr = isWrOrYr;
	}

	@Override
	public int getCount() {
		return transferList.size();
	}

	@Override
	public Object getItem(int position) {
		return transferList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void changeDate(List<Map<String, Object>> transferList) {
		this.transferList = transferList;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
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
			holer.ll_bookAmount = (LinearLayout) convertView.findViewById(R.id.ll_bookAmount);
			holer.crcd_wc_bookDate_txt = (TextView) convertView.findViewById(R.id.crcd_wc_bookDate_txt);
			convertView.setTag(holer);
		} else {
			holer=(ViewHolder) convertView.getTag();
		}
		
		if(isWrOrYr){
			holer.ll_bookAmount.setVisibility(View.VISIBLE);
			holer.crcd_wc_bookDate_txt.setText(context.getResources().getString(R.string.crcd_wc_bookDate));
		}else{
			holer.ll_bookAmount.setVisibility(View.GONE);
			holer.crcd_wc_bookDate_txt.setText(context.getResources().getString(R.string.crcd_wc_jiaoyiDate));
		}
		Map<String, Object> map = transferList.get(position);
		String debitCreditFlag = (String) map.get(Crcd.CRCD_DEBITFLAG);
		// 交易日期
		String transDate = (String) map.get(Crcd.CRCD_TRANSDATE);
		// 交易金额
		String tranAmount = (String) map.get(Crcd.CRCD_TRANAMOUNT);
		// 交易币种
		String tranCurrency = (String) map.get(Crcd.CRCD_TRANCURRENCY);
		// 记账金额
		String bookAmount = (String) map.get(Crcd.CRCD_BOOKAMOUNT);
		// 记账币种
		String bookCurrency = (String) map.get(Crcd.CRCD_BOOKCURRENCY);
		// 记账日期		
		String bookDate = (String) map.get(Crcd.CRCD_BOOKDATE);	
		
		
		// 摘要
		String remark = (String) map.get(Crcd.CRCD_REMARK);
		String tranCurrencys = null;
		if (StringUtil.isNull(tranCurrency)) {
			tranCurrencys = "-";
		} else {
			if (LocalData.Currency.containsKey(tranCurrency)) {
				tranCurrencys = LocalData.Currency.get(tranCurrency);
			} else {
				tranCurrencys = "-";
			}
		}
		String tranAmounts = null;
		if (StringUtil.isNull(tranAmount)) {
			tranAmounts = "-";
		} else {
			if (!StringUtil.isNull(tranCurrency)) {
				tranAmounts = StringUtil.parseStringCodePattern(tranCurrency, tranAmount, 2);
			} else {
				tranAmounts = tranAmount;
			}
		}
		String outOrIn = null;
		
//		CRED表示贷方
//		DEBT表示借方
//		NMON表示减免年费
//		
//		1:表示借记交易，如POS消费
//		0:表示贷记交易，如存款。
		
		if(isWrOrYr){
			if ("CRED".equals(debitCreditFlag)) {
				// 收入
				outOrIn = context.getString(R.string.crcd_wc_in);
			} else if ("DEBT".equals(debitCreditFlag) || "NMON".equals(debitCreditFlag)) {
				outOrIn = context.getString(R.string.crcd_wc_out);
			}	
		}else{
			if ("0".equals(debitCreditFlag)) {
				// 收入
				outOrIn = context.getString(R.string.crcd_wc_in);
			} else if ("1".equals(debitCreditFlag)) {
				outOrIn = context.getString(R.string.crcd_wc_out);
			}	
		}
		
		String bookCurrencys = null;
		if (StringUtil.isNull(bookCurrency)) {
			bookCurrencys = "-";
		} else {
			if (LocalData.Currency.containsKey(bookCurrency)) {
				bookCurrencys = LocalData.Currency.get(bookCurrency);
			} else {
				bookCurrencys = "-";
			}
		}
		String bookAmounts = null;
		if (StringUtil.isNull(bookAmount)) {
			bookAmounts = "-";
		} else {
			if (!StringUtil.isNull(bookCurrency)) {
				bookAmounts = StringUtil.parseStringCodePattern(bookCurrency, bookAmount, 2);
			} else {
				bookAmounts = bookAmount;
			}
		}
		holer.outOrInText.setText(outOrIn);
		holer.transDateText.setText(transDate);
		holer.tranAmountText.setText(tranAmounts);
		holer.tranCurrencyText.setText(tranCurrencys);
		holer.bookAmountText.setText(bookAmounts);
		holer.bookCurrencyText.setText(bookCurrencys);
		if(isWrOrYr){
			holer.bookDateText.setText(bookDate);	
		}else{
			holer.bookDateText.setText(transDate);
		}
		
		holer.remarkText.setText(remark);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holer.remarkText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holer.tranCurrencyText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holer.bookCurrencyText);		
		return convertView;
	}

	public OnItemClickListener getOncrcdTransDivideListener() {
		return oncrcdTransDivideListener;
	}

	public void setOncrcdTransDivideListener(OnItemClickListener oncrcdTransDivideListener) {
		this.oncrcdTransDivideListener = oncrcdTransDivideListener;
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
		private LinearLayout ll_bookAmount=null;
		private TextView crcd_wc_bookDate_txt= null;
	}
}
