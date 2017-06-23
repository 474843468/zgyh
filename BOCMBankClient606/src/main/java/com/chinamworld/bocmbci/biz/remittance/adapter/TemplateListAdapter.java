package com.chinamworld.bocmbci.biz.remittance.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.interfaces.OnOtherOperationListener;
import com.chinamworld.bocmbci.biz.remittance.interfaces.OnStartRemittanceListener;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

public class TemplateListAdapter extends BaseAdapter {

	/** 上下文对象 */
	private Context context;
	/** 模板列表 */
	private List<Map<String, Object>> templateList;
	private OnStartRemittanceListener startRemittanceListener;
	private OnOtherOperationListener othorOperationListener;
	
	public TemplateListAdapter(Context context, List<Map<String, Object>> templateList, OnStartRemittanceListener startRemittanceListener, OnOtherOperationListener othorOperationListener) {
		this.context = context;
		this.templateList = templateList;
		this.startRemittanceListener = startRemittanceListener;
		this.othorOperationListener = othorOperationListener;
	}
	
	@Override
	public int getCount() {
		return templateList.size();
	}

	@Override
	public Object getItem(int position) {
		return templateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Viewholder vh;
		if (convertView == null) {
			vh = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(R.layout.remittance_template_list_item, null);
			
			vh.etTemplateName = (TextView) convertView.findViewById(R.id.et_templateName);
			vh.btnStartRemittance = (Button) convertView.findViewById(R.id.btn_startRemittance);
			vh.btnOtherOperation = (Button) convertView.findViewById(R.id.btn_otherOperation);
			vh.tvPayeeEnName = (TextView) convertView.findViewById(R.id.tv_payeeEnName);
			convertView.setTag(vh);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(context, (TextView) convertView.findViewById(R.id.et_templateName));
		} else {
			vh = (Viewholder) convertView.getTag();
		}
		
		vh.etTemplateName.setText((String) ((Map<String, Object>) getItem(position)).get(Remittance.TEMPLATENAME));
		
		vh.btnStartRemittance.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startRemittanceListener.onStartRemittance(position);
			}
		});
		
		vh.btnOtherOperation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				othorOperationListener.onOtherOperation(position);
			}
		});
		
		vh.tvPayeeEnName.setText((String) ((Map<String, Object>) getItem(position)).get(Remittance.PAYEEENNAME));
		return convertView;
	}
	
	public void setData(List<Map<String, Object>> list) {
		templateList = list;
		notifyDataSetChanged();
	}
	
	private class Viewholder {
		TextView etTemplateName;
		Button btnStartRemittance;
		Button btnOtherOperation;
		TextView tvPayeeEnName;
	}
}
