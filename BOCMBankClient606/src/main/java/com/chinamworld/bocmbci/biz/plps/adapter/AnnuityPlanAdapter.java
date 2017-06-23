package com.chinamworld.bocmbci.biz.plps.adapter;

/**
 * 变更信息ListView适配器
 * @author panwe
 *注释此类 因 dex方法超出限制  sunh 
 */
public class AnnuityPlanAdapter /*extends BaseAdapter*/{/*
	
	private Context mContext;
	
	private List<Map<String, Object>> mList;
	
	private int selectedPosition = -1;
	
	public AnnuityPlanAdapter(Context cn,List<Map<String, Object>> list){
		this.mContext = cn;
		this.mList = list;
	}

	@Override
	public int getCount() {
		if (StringUtil.isNullOrEmpty(mList)) {
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		if (StringUtil.isNullOrEmpty(mList)) {
			return null;
		}
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler h;
		if (convertView == null) {
			h = new ViewHodler();
			convertView = LinearLayout.inflate(mContext, R.layout.plps_annuity_plan_list_item, null);
			h.tvPlan = (TextView) convertView.findViewById(R.id.planname);
			h.imChecked = (ImageView) convertView.findViewById(R.id.imageViewright);
			h.mLayout = (FrameLayout) convertView.findViewById(R.id.ll_acc_msg);
			convertView.setTag(h);
		}else{
			h = (ViewHodler) convertView.getTag();
		}
		final Map<String, Object> map = mList.get(position);
		h.tvPlan.setText(map.get(Plps.PLANNAME)+"/"+map.get(Plps.PLANNO));
		if (position == selectedPosition) {
			h.mLayout.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			h.imChecked.setVisibility(View.VISIBLE);
		} else {
			h.mLayout.setBackgroundResource(R.drawable.bg_for_listview_item_write);
			h.imChecked.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	public List<Map<String, Object>> getmList() {
		return mList;
	}

	public void setmList(List<Map<String, Object>> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}
	
	public class ViewHodler {
		public TextView tvPlan;
		public ImageView imChecked;
		public FrameLayout mLayout;
	}
*/}
