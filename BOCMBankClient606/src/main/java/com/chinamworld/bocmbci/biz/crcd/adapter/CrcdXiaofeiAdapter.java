package com.chinamworld.bocmbci.biz.crcd.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 信用卡消费查询适配器
 * 
 * @author huangyuchao
 * 
 */
public class CrcdXiaofeiAdapter extends BaseAdapter {

	/** 账户交易列表信息 */
	private List<Map<String, Object>> transferList;
	private Context context;
	/** 账户交易收入支出金额 */
	private TextView tv_acc_query_result_amount;
	/** 账户交易时间 */
	private TextView tv_acc_query_result_paymentdate;
	/** 账户交易账户描述 */
	private TextView tv_acc_query_result_balance;

	TextView acc_query_transfer_amount;

	private Button img_crcd_setup;

	/** 左上角标识 */
	private ImageView img_flag;

	int isShow;

	/** 分期点击事件 */
	private OnItemClickListener oncrcdTransDivideListener;

	public CrcdXiaofeiAdapter(Context context, List<Map<String, Object>> transferList, int isShow) {
		this.context = context;
		this.transferList = transferList;
		this.isShow = isShow;
	}

	public void dataChanged(List<Map<String, Object>> list) {
		this.transferList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return transferList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return transferList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.crcd_trans_query_list_item, null);
			TextView tv_acc_query_result_acm = (TextView) convertView.findViewById(R.id.acc_query_transfer_amount);
			/** 账户交易收入支出金额 */
			tv_acc_query_result_amount = (TextView) convertView.findViewById(R.id.acc_query_transfer_amount_value);
			/** 账户交易时间 */
			tv_acc_query_result_paymentdate = (TextView) convertView
					.findViewById(R.id.acc_query_transfer_paymentdate_value);
			/** 账户交易账户描述 */
			tv_acc_query_result_balance = (TextView) convertView.findViewById(R.id.acc_query_transfer_balance_value);
			/** 左上角标志 */
			img_flag = (ImageView) convertView.findViewById(R.id.img_flag);

			acc_query_transfer_amount = (TextView) convertView.findViewById(R.id.acc_query_transfer_amount);

			img_crcd_setup = (Button) convertView.findViewById(R.id.img_crcd_setup);
			img_crcd_setup.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (oncrcdTransDivideListener != null) {
						oncrcdTransDivideListener.onItemClick(null, v, position, position);
					}

				}
			});

			tv_acc_query_result_paymentdate.setText((String) (transferList.get(position).get(Crcd.CRCD_TRANSDATE)));

			PopupWindowUtils.getInstance().setOnShowAllTextListener(context, tv_acc_query_result_paymentdate);

			// String result_balance = (String) (transferList.get(position)
			// .get(Crcd.CRCD_TRANAMOUNT));

			String result_bookAmount = (String) (transferList.get(position).get(Crcd.CRCD_BOOKAMOUNT));
			String result_tranAmount = (String) (transferList.get(position).get(Crcd.CRCD_TRANAMOUNT));
			if (!StringUtil.isNullOrEmpty(result_bookAmount)) {
				tv_acc_query_result_amount.setText(StringUtil.parseStringPattern(result_bookAmount, 2));
			} else if (!StringUtil.isNullOrEmpty(result_tranAmount)) {
				tv_acc_query_result_amount.setText(StringUtil.parseStringPattern(result_tranAmount, 2));
			}

			PopupWindowUtils.getInstance().setOnShowAllTextListener(context, tv_acc_query_result_amount);

			String transDesc = String.valueOf((transferList.get(position).get(Crcd.CRCD_TRANSDESC)));
			tv_acc_query_result_balance.setText(transDesc);

			PopupWindowUtils.getInstance().setOnShowAllTextListener(context, tv_acc_query_result_balance);

			String debitCreditFlag = String.valueOf((transferList.get(position).get(Crcd.CRCD_DEBITFLAG)));

			acc_query_transfer_amount.setText(context.getString(R.string.acc_query_amount_trans));

			// if ("CRED".equals(debitCreditFlag)) {
			// img_flag.setImageResource(R.drawable.img_triangle_green);
			// acc_query_transfer_amount.setText(context
			// .getString(R.string.acc_query_amount_in));
			// } else if ("DEBT".equals(debitCreditFlag)
			// || "NMON".equals(debitCreditFlag)) {
			// img_flag.setImageResource(R.drawable.img_triangle_red);
			// acc_query_transfer_amount.setText(context
			// .getString(R.string.acc_query_amount_out));
			// }

			if (isShow == 1) {
				img_crcd_setup.setVisibility(View.VISIBLE);
			} else if (isShow == 2) {
				img_crcd_setup.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	public OnItemClickListener getOncrcdTransDivideListener() {
		return oncrcdTransDivideListener;
	}

	public void setOncrcdTransDivideListener(OnItemClickListener oncrcdTransDivideListener) {
		this.oncrcdTransDivideListener = oncrcdTransDivideListener;
	}

}
