package com.boc.bocsoft.remoteopenacc.buss.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.remoteopenacc.R;
import com.boc.bocsoft.remoteopenacc.buss.model.queryelecaccopeningbank.QueryElecAccOpeningBankResponseModel.BocBankInfo;
import com.boc.bocsoft.remoteopenacc.buss.model.queryprovinceandcity.QueryProvinceAndCityResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryprovinceandcity.QueryProvinceAndCityResponseModel.DistMapping;

/**
 * 只有一个ListView的侧边view
 * 
 * @author chf
 * 
 */

public class RemobeQueryElecacCattrView extends LinearLayout {

	protected Context mContext;
	private RelativeLayout rl_title;
	private ListView lv;// 展示内容的listview
	private TextView tv_title;// listview中显示的标题

	/** 存放开户行的集合 */
	private List<BocBankInfo> openingBankLists;
	private List<DistMapping> provinceLists;
	private BocBankInfo currentBocBank;
	private DistMapping currentDistMapping;
	/** 存放title值 */
	private String mTitle;

	/**
	 * listview监听事件
	 * 
	 */
	private QueryElecacCattrViewListener mQueryElecacCattrViewListener;
	
	private QueryCityViewListener mQueryCityViewListener;
	
	public final static String TITLE = "title";

	public RemobeQueryElecacCattrView(Context context) {
		super(context);
		mContext = context;
		initView();
		initData();
	}

	public RemobeQueryElecacCattrView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
	}

	private void initView() {
		View.inflate(mContext, R.layout.bocro_base_list_view, this);

		lv = (ListView) findViewById(R.id.lv);
		tv_title = (TextView) findViewById(R.id.tv_title);
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);
	}

	/**
	 * 设置标题
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		if (mTitle == null || mTitle.length() == 0) {
			rl_title.setVisibility(View.GONE);
			tv_title.setVisibility(View.GONE);
			tv_title.setText("");
		} else {
			rl_title.setVisibility(View.VISIBLE);
			tv_title.setVisibility(View.VISIBLE);
			tv_title.setText(text);
		}
	}
	
	public void clearTitle(){
		mTitle = "";
		rl_title.setVisibility(View.GONE);
		tv_title.setVisibility(View.GONE);
	}

	public void setOpeningBankData(List<BocBankInfo> list) {
		MyAdapter mAdapter = new MyAdapter(mContext);
		mAdapter.setOpeningBankData(list);
		if (mAdapter != null) {
			lv.setAdapter(mAdapter);
		}
	}

	/**
	 * 设置省份
	 * @param list
	 */
	public void setProvinceOrCityData(List<QueryProvinceAndCityResponseModel.DistMapping> list){
		MyAdapter mAdapter = new MyAdapter(mContext);
		mAdapter.setProvinceOrCity(list);
		if (mAdapter != null) {
			lv.setAdapter(mAdapter);
		}
	}
	protected List<? extends Object> mList;

	
	public BocBankInfo getCurrentBocBank() {
		return currentBocBank;
	}

	/**
	 * 开户行显示View
	 */
	public void showOpeningBankView(final List<BocBankInfo> mOpeningBank) {
		openingBankLists = mOpeningBank;
		setOpeningBankData(openingBankLists);
		setTitle(mTitle);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mQueryElecacCattrViewListener != null) {
					mTitle = openingBankLists.get(position).orgName;
					currentBocBank = openingBankLists.get(position);
					mQueryElecacCattrViewListener.onOpeningBankItemClickListener(openingBankLists
							.get(position));
				}
			}
		});
	}

	/**
	 * 显示城市
	 */
	public void showProvinceOrCityView(final List<DistMapping> list){
		setProvinceOrCityData(list);
		provinceLists = list;
		setTitle(mTitle);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mQueryCityViewListener != null) {
					mTitle = provinceLists.get(position).distName;
					currentDistMapping = provinceLists.get(position);
					mQueryCityViewListener.onQueryCityItemClickListener(currentDistMapping);
				}
			}
		});
	}
	


	public void setQueryElecacCattrViewListener(
			QueryElecacCattrViewListener listener) {
		mQueryElecacCattrViewListener = listener;
	}

	public void setQueryCityViewListener(QueryCityViewListener listener) {
		mQueryCityViewListener = listener;
	}
	
	public interface QueryElecacCattrViewListener {

		public void onOpeningBankItemClickListener(BocBankInfo pos);

	}

	public interface QueryCityViewListener{
		public void onQueryCityItemClickListener(DistMapping dist);
	}
	
	
	
	public void setmQueryCityViewListener(
			QueryCityViewListener mQueryCityViewListener) {
		this.mQueryCityViewListener = mQueryCityViewListener;
	}

	/**
	 * 适配器
	 */
	class MyAdapter extends BaseAdapter {

		private Context context;
		private ViewHolder holder = null;
		private List<BocBankInfo> mOpeningBankListData;
		private List<DistMapping> mProvinceOrCityListData;
		private int type;

		public MyAdapter(Context context) {
			super();
			this.context = context;
			this.mOpeningBankListData = new ArrayList<BocBankInfo>();
		}

		public void setOpeningBankData(List<BocBankInfo> mList) {
			this.mOpeningBankListData = mList;
			type = 1;
			notifyDataSetChanged();
		}
		
		public void setProvinceOrCity(List<DistMapping> list){
			mProvinceOrCityListData = list;
			type = 0;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			if (type == 0) {
				return mProvinceOrCityListData == null ? 0 : mProvinceOrCityListData.size();
			} else {
				return mOpeningBankListData == null ? 0 : mOpeningBankListData
						.size();
			}
		}

		@Override
		public Object getItem(int position) {
			if (type == 0) {
				return mProvinceOrCityListData.get(position);
			} else {
				return mOpeningBankListData.get(position);
			}
			
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.bocroa_base_list_item_view, null, false);
				holder.tv_distName = (TextView) convertView
						.findViewById(R.id.tv_is_cbr_se_base_list_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if ( type == 0) {
				holder.tv_distName
				.setText(mProvinceOrCityListData.get(position).distName);
			} else {
				holder.tv_distName
				.setText(mOpeningBankListData.get(position).orgName);
			}
			
			return convertView;
		}

		private class ViewHolder {
			TextView tv_distName;
		}
	}
}
