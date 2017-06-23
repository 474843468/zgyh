package com.chinamworld.bocmbci.biz.lsforex.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 签约----选择可签约保证金  适配器*/
public class IsForexProduceChoiseAdapter extends BaseAdapter {
	private Context context = null;
	private List<Map<String, String>> list;
	public static int selectedPosition = -1;

	public IsForexProduceChoiseAdapter(Context context, List<Map<String, String>> list) {
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

	public void dataChanged(List<Map<String, String>> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.isforex_manage_sign_choise_item, null);
			holder = new ViewHolder();
			holder.bailNoText = (TextView) convertView.findViewById(R.id.isForex_manage_bailNo);
			holder.isSignText = (TextView) convertView.findViewById(R.id.isForex_manage_sign_isSign);
			holder.bailCNameText = (TextView) convertView.findViewById(R.id.isForex_manage_bailCNamen);
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageViewright);
			holder.jsCodeText = (TextView) convertView.findViewById(R.id.isForex_vfgRegCurrency1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		// 选中的颜色
		if (position == selectedPosition) {
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			holder.imageView.setVisibility(View.VISIBLE);
		} else {
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_write);
			holder.imageView.setVisibility(View.INVISIBLE);
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holder.bailCNameText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holder.bailNoText);
		String bailNo = map.get(IsForex.ISFOREX_BAILNO_RES);
		String isSign = map.get(IsForex.ISFOREX_ISSIGN_RES);
		String bailCName = map.get(IsForex.ISFOREX_BAILCNAME_RES);
		String settleCurrency = map.get(IsForex.ISFOREX_SETTLECURRENCY_RES1);
		String yesOrNo = null;
		// Y:是否可签约
		if (!StringUtil.isNull(isSign) && ConstantGloble.ISFOREX_JCTAG_Y.equals(isSign)) {
			yesOrNo = context.getResources().getString(R.string.yes);
		} else if (!StringUtil.isNull(isSign) && (ConstantGloble.ISFOREX_JCTAG_N1.equals(isSign)||ConstantGloble.ISFOREX_JCTAG_N2.equals(isSign))) {
			yesOrNo = context.getResources().getString(R.string.no);
		}
		String jsCode = null;
		if (!StringUtil.isNull(settleCurrency) && LocalData.Currency.containsKey(settleCurrency)) {
			jsCode = LocalData.Currency.get(settleCurrency);
		}
		holder.bailNoText.setText(bailNo);
		holder.isSignText.setText(yesOrNo);
		holder.bailCNameText.setText(bailCName);
		holder.jsCodeText.setText(jsCode);

		return convertView;
	}

	public final class ViewHolder {
		public TextView bailNoText;
		public TextView isSignText;
		public TextView bailCNameText;
		public TextView jsCodeText;
		public ImageView imageView;
	}

}
