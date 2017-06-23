package com.chinamworld.bocmbci.biz.loan.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贷款申请进度查询列表适配器
 * 
 * @author wangmengmeng
 * 
 */
public class LoanApplyQueryAdapter extends BaseAdapter {
	/** 贷款账户列表信息 */
	private List<Map<String, Object>> loanlist;
	private Context context;
	/** 详细信息监听事件 */
	private OnItemClickListener loanAccountMessageClick;
	// 布局文件
	private int layout;

	public LoanApplyQueryAdapter(Context context, int layout,
			List<Map<String, Object>> loanlist) {
		this.context = context;
		this.loanlist = loanlist;
		this.layout = layout;
	}
	
	public void changeData(List<Map<String, Object>> loanlist){
		if (loanlist == null) {
			loanlist = new ArrayList<Map<String,Object>>();
		}
		this.loanlist = loanlist;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return loanlist.size();
	}

	@Override
	public Object getItem(int position) {
		return loanlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(layout, null);
			holder = new ViewHolder();
			/** 贷款姓名*/
			holder.loan_apply_name_value = (TextView) convertView
					.findViewById(R.id.loan_apply_name_value);
			/** 贷款产品 */
			holder.loan_apply_product_value = (TextView) convertView
					.findViewById(R.id.loan_apply_product_value);
			/** 贷款金额 */
			holder.loan_apply_money_value = (TextView) convertView
					.findViewById(R.id.loan_apply_money_value);
			/** 贷款币种 */
			holder.loan_apply_currency = (TextView) convertView
					.findViewById(R.id.loan_apply_currency);
			/** 贷款期限 */
			holder.loan_apply_deadline_value = (TextView) convertView
					.findViewById(R.id.loan_apply_deadline_value);
			/**贷款状态*/
			holder.loan_apply_status_value = (TextView) convertView
					.findViewById(R.id.loan_apply_status_value);
			/**申请时间*/
			holder.loan_apply_time_value = (TextView) convertView
					.findViewById(R.id.loan_apply_time_value);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		/** 右箭头—进入进度详细信息页面 */
		ImageView btGoItem = (ImageView) convertView
				.findViewById(R.id.acc_btn_goitem_back);
		btGoItem.setVisibility(View.VISIBLE);

		
		
		/** 进入贷款详细信息界面 */
//		ImageView img_loan_accmessage = (ImageView) convertView
//				.findViewById(R.id.img_loan_apply_accmessage);
		// 贷款姓名
		String loan_apply_name= (String) loanlist.get(position).get(
				Loan.LOAN_APPLY_NAME_API);
		holder.loan_apply_name_value.setText(loan_apply_name);
		
		//贷款产品 
		String loan_apply_product = (String) loanlist.get(position).get(
				Loan.LOAN_APPLY_PRODUCTNAME_API);
		holder.loan_apply_product_value.setText(loan_apply_product);
		
       //币种码（1,2,3.。。。。）		
		String currency = (String) loanlist.get(position).get(
				Loan.LOAN_APPLY_CURRENCY_QRY);
		//根据币种码获得相对应的币种
		String cy=LoanData.LoanApplyCurcodeMap.get(currency);
		//根据币种的中文名称，获得想对应的币种code值，用于根据币种code值，进行格式化
		String currencyCode=LoanData.bociCurcodeMap.get(cy);
		
		//贷款金额
		String loan_amount = (String.valueOf(loanlist.get(position).get(
				Loan.LOAN_APPLY_LOANAMOUNT_API)));
		holder.loan_apply_money_value.setText(StringUtil.parseStringCodePattern(currencyCode,loan_amount, 2));
	
//		if("外汇留学贷款".equals(loan_apply_product)){
//			holder.loan_apply_currency.setVisibility(View.VISIBLE);
//			holder.loan_apply_currency.setText(cy);
//		}else{
//			holder.loan_apply_currency.setVisibility(View.GONE);
//		}
		//贷款期限loan_apply_deadline_value
		String loan_apply_deadline = (String) loanlist.get(position).get(
				Loan.LOAN_APPLY_LOANTERM_API);
		holder.loan_apply_deadline_value.setText(loan_apply_deadline);
		
		//贷款状态 loan_apply_status_value
		String loan_apply_status = (String) loanlist.get(position).get(
				Loan.LOAN_APPLY_LOANSTATUS_API);
		String status=LoanData.loanStatusMap.get(loan_apply_status);
		holder.loan_apply_status_value.setText(status);
		//申请时间   applyDate
		String applyDate = (String) loanlist.get(position).get(
				Loan.LOAN_APPLY_APPLYDATE_API);
		holder.loan_apply_time_value.setText(applyDate);
		
		return convertView;
	}
	
	private class ViewHolder {
		TextView loan_apply_name_value ;
		/** 贷款产品 */
		TextView loan_apply_product_value;
		/** 贷款金额 */
		TextView loan_apply_money_value;
		/** 贷款币种 */
		TextView loan_apply_currency;
		/** 贷款期限 */
		TextView loan_apply_deadline_value ;
		/**贷款状态*/
		TextView loan_apply_status_value ;
		/**申请时间*/
		TextView loan_apply_time_value ;
	}
}
