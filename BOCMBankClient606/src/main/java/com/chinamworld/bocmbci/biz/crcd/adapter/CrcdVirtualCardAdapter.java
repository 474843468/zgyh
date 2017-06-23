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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 虚拟银行卡---选择虚拟银行卡适配器 */
public class CrcdVirtualCardAdapter extends BaseAdapter {
	private static final String TAG = "CrcdVirtualCardAdapter";
	private List<Map<String, Object>> bankAccountList;

	Map<String, Object> returnMap;

	private Context context;

	private int selectedPosition;
	private int conid;

	/** 进入账单明细点击事件 */
	private OnItemClickListener ontransDetailListener;

	/** 详情点击事件 */
	private OnItemClickListener oncrcdDetailListener;

	/** 取消账户关联事件 */
	private OnItemClickListener onItemCanceListener;

	public CrcdVirtualCardAdapter(Context c, List<Map<String, Object>> bankAccountList, Map<String, Object> returnMap,
			int conid) {
		this.context = c;
		this.bankAccountList = bankAccountList;
		this.returnMap = returnMap;
		this.conid = conid;
	}

	@Override
	public int getCount() {
		if (!StringUtil.isNullOrEmpty(bankAccountList)) {
			return bankAccountList.size();
		} else if (!StringUtil.isNullOrEmpty(returnMap)) {
			return returnMap.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (StringUtil.isNullOrEmpty(bankAccountList)) {
			return bankAccountList.get(position);
		} else if (StringUtil.isNullOrEmpty(returnMap)) {
			return returnMap;
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		CrcdViewHolder holder;
		LogGloble.d(TAG, "getView");
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.crcd_virtual_list_item, null);
			holder = new CrcdViewHolder();
			holder.crcd_type_value = (TextView) convertView.findViewById(R.id.crcd_type_value);
			holder.crcd_account_nickname = (TextView) convertView.findViewById(R.id.crcd_account_nickname);
			holder.crcd_account_num = (TextView) convertView.findViewById(R.id.crcd_account_num);
			holder.crcd_account_status = (TextView) convertView.findViewById(R.id.crcd_bookbalance_value);

			holder.ivdetail = (ImageView) convertView.findViewById(R.id.img_crcd_detail);
			holder.ivgoitem = (ImageView) convertView.findViewById(R.id.crcd_btn_goitem);
			holder.ivCancel = (ImageView) convertView.findViewById(R.id.crcd_btn_gocancelrelation);
			holder.ivCurrCode = (ImageView) convertView.findViewById(R.id.img_crcd_currencycode);

			holder.rightFlag = (ImageView) convertView.findViewById(R.id.imageViewright);

			holder.ll_acc_msg = (FrameLayout) convertView.findViewById(R.id.ll_acc_msg);

			convertView.setTag(holder);
		} else {
			holder = (CrcdViewHolder) convertView.getTag();
		}
		// 虚拟卡状态
		String status = null;
		if (!StringUtil.isNullOrEmpty(bankAccountList)) {
			Map<String, Object> map = bankAccountList.get(position);

			holder.crcd_type_value.setText(context.getString(R.string.mycrcd_xuni_crcd));
			holder.crcd_account_nickname.setText("");
			holder.crcd_account_num.setText(StringUtil.getForSixForString(String.valueOf(map
					.get(Crcd.CRCD_VIRTUALCARDNO))));
			status = (String) map.get(Crcd.CRCD_STATUS);
			String s = null;
			if (!StringUtil.isNull(status)) {
				s = LocalData.virtualCrcdStatusMap.get(status);
				holder.crcd_account_status.setText(s);
			}

		} else if (!StringUtil.isNullOrEmpty(returnMap)) {
			holder.crcd_type_value.setText(context.getString(R.string.mycrcd_xuni_crcd));
			holder.crcd_account_nickname.setText("");
			holder.crcd_account_num.setText(StringUtil.getForSixForString(String.valueOf(returnMap
					.get(Crcd.CRCD_VIRTUALCARDNO))));
			status = (String) returnMap.get(Crcd.CRCD_STATUS);
			String s = null;
			if (!StringUtil.isNull(status)) {
				s = LocalData.virtualCrcdStatusMap.get(status);
				holder.crcd_account_status.setText(s);
			}

		}

		holder.ivCurrCode.setVisibility(View.GONE);

		holder.ivdetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (oncrcdDetailListener != null) {
					oncrcdDetailListener.onItemClick(null, v, position, position);
				}

			}
		});

		holder.ivgoitem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ontransDetailListener != null) {
					ontransDetailListener.onItemClick(null, v, position, position);
				}
			}
		});

		if (conid == 1) {
			holder.ivgoitem.setVisibility(View.VISIBLE);
			holder.ivCancel.setVisibility(View.GONE);

		} else if (conid == 2) {
			// 如果虚拟卡已注销，不显示注销图片按钮
			if (ConstantGloble.CRCD_STATUS_ZERO.equals(status)) {
				holder.ivCancel.setVisibility(View.GONE);
				holder.ivgoitem.setVisibility(View.VISIBLE);
			} else {
				holder.ivCancel.setVisibility(View.VISIBLE);
				holder.ivgoitem.setVisibility(View.GONE);
			}
//			holder.ivgoitem.setVisibility(View.GONE);
//			holder.ivCancel.setVisibility(View.VISIBLE);
		}
		/** 注销事件 */
		holder.ivCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onItemCanceListener != null) {
					onItemCanceListener.onItemClick(null, v, position, position);
				}
			}
		});

		return convertView;
	}

	class CrcdViewHolder {
		/** 虚拟卡类型 */
		TextView crcd_type_value;

		TextView crcd_account_nickname;
		/** 虚拟卡账号 */
		TextView crcd_account_num;
		/** 虚拟卡状态 */
		TextView crcd_account_status;

		ImageView ivdetail;
		/** 进入到明细查询页面 */
		ImageView ivgoitem;
		/** 取消注销图片按钮 */
		ImageView ivCancel;
		ImageView ivCurrCode;

		ImageView rightFlag;
		FrameLayout ll_acc_msg;

	}

	public OnItemClickListener getOncrcdDetailListener() {
		return oncrcdDetailListener;
	}

	public void setOncrcdDetailListener(OnItemClickListener oncrcdDetailListener) {
		this.oncrcdDetailListener = oncrcdDetailListener;
	}

	public OnItemClickListener getOnItemCanceListener() {
		return onItemCanceListener;
	}

	public void setOnItemCanceListener(OnItemClickListener onItemCanceListener) {
		this.onItemCanceListener = onItemCanceListener;
	}

	public OnItemClickListener getOntransDetailListener() {
		return ontransDetailListener;
	}

	public void setOntransDetailListener(OnItemClickListener ontransDetailListener) {
		this.ontransDetailListener = ontransDetailListener;
	}

}
