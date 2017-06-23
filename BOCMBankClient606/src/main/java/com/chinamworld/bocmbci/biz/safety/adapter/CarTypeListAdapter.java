package com.chinamworld.bocmbci.biz.safety.adapter;

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
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 车型选择列表适配器
 * 
 * @author Zhi
 */
public class CarTypeListAdapter extends BaseAdapter {

	/** 车型列表数据源 */
	private List<Map<String, Object>> carTypeList;
	/** 上下文 */
	private Context context;
	/** 选中项下标 */
	private int selectedPosition;
	
	/** 构造方法 */
	public CarTypeListAdapter(Context context, List<Map<String, Object>> carTypeList, int position) {
		this.context = context;
		this.carTypeList = carTypeList;
		this.selectedPosition = position;
	}
	
	@Override
	public int getCount() {
		return carTypeList.size();
	}

	@Override
	public Object getItem(int position) {
		return carTypeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.safety_carsafety_choose_cartype_item, null);
			
			vh = new ViewHolder();
			vh.tvVehicleBrand = (TextView) convertView.findViewById(R.id.tv_vehicleBrand);
			vh.tvVehicleModel = (TextView) convertView.findViewById(R.id.tv_vehicleModel);
			vh.tvModelYear = (TextView) convertView.findViewById(R.id.tv_modelYear);
			vh.tvSeatNum = (TextView) convertView.findViewById(R.id.tv_seatNum);
			vh.tvNewCarPrice = (TextView) convertView.findViewById(R.id.tv_newCarPrice);
			vh.img = (ImageView) convertView.findViewById(R.id.imageViewright);
			
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		Map<String, Object> map = (Map<String, Object>) getItem(position);
		if (position == selectedPosition) {
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			vh.img.setVisibility(View.VISIBLE);
		} else {
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_write);
			vh.img.setVisibility(View.GONE);
		}
		
		vh.tvVehicleBrand.setText((String) map.get(Safety.VEHICLEBRAND));
		vh.tvVehicleModel.setText((String) map.get(Safety.VEHICLEMODEL));
		vh.tvModelYear.setText((String) map.get(Safety.MODELYEAR));
		vh.tvSeatNum.setText((String) map.get(Safety.SEATNUM));
		vh.tvNewCarPrice.setText(StringUtil.parseStringPattern((String) map.get(Safety.NEWCARPRICE), 2));
		
		return convertView;
	}
	
	class ViewHolder{
		/** 车辆品牌 */
		TextView tvVehicleBrand;
		/** 品牌型号 */
		TextView tvVehicleModel;
		/** 年款 */
		TextView tvModelYear;
		/** 座位数 */
		TextView tvSeatNum;
		/** 新车购置价 */
		TextView tvNewCarPrice;
		/** 标识此项选中的半透明图片 */
		ImageView img;
	}
	/** 获取列表选中项下标 */
	public int getSelectedPosition() {
		return selectedPosition;
	}
	
	/** 设置列表选中项并更新列表显示 */
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}
}
