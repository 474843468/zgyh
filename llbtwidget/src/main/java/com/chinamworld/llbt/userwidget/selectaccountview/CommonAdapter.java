package com.chinamworld.llbt.userwidget.selectaccountview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;


import com.chinamworld.llbt.model.IFunc;

import java.util.List;

/**
 * 公共ListView适配器
 * 
 * @author yuht
 *
 * @param <T> : 数据List 的 Item结构类型
 */
public class CommonAdapter<T> extends BaseAdapter {

	private List<T> sourceList;
	/**
	 * 获得当前适配器的数据源
	 */
	public List<T> getSourceList() {
		return sourceList;
	}

	/***
	 * 设置数据源
	 * @param list
	 * @param totalNumber ： 当前列表最大记录条数
	 */
	public void setSourceList(List<T> list,int totalNumber){
		sourceList = list;
		this.totalNumber = totalNumber;
		notifyDataSetChanged();
	}
	
	/***
	 * 向列表中累加数据源
	 * @param list
	 * @param  ： 当前列表最大记录条数
	 */
	public void addSourceList(List<T> list){
		if(list == null || list.size() == 0)
			return;
		if(sourceList == null)
		{
			sourceList = list;
		}
		else {
			for(T item : list){
				sourceList.add(item);
			}
		}
		notifyDataSetChanged();
	}
	
	private ListView listView = null;
	
	private View mFooterView ;
	private Context context;
	private ICommonAdapter<T> commonAdapterCallBack;
	/**
	 * 构造公共适配器
	 * @param context : 上下文
	 * @param source : 列表数据源
	 * @param getViewCallback : 适配器获得View方法的接口
	 */
	public CommonAdapter(Context context, List<T> source,
			ICommonAdapter<T> getViewCallback) {
		this.context = context;
		sourceList = source;
		commonAdapterCallBack = getViewCallback;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if(listView == null)
			return;
		if(sourceList != null && sourceList.size() < totalNumber){ // 有更多按钮
			if (listView.getFooterViewsCount() <= 0) {
				listView.addFooterView(mFooterView);
			}
		}
		else {
			if (listView.getFooterViewsCount() > 0) {
				listView.removeFooterView(mFooterView);
			}
		}
	}
	/**
	 * 	 如果当前数据源条数少于总记录条数，则会显示更多按钮
	 */
	private int totalNumber = -1;
	/**
	 * 设置总记录条数
	 * @param count
	 */
	public void setTotalNumber(int count){
		totalNumber = count;
	}

	private IFunc<Boolean> requestMoreData = null;

	/**
	 *  设置 更多 事件监听
	 * @param listener
	 */
	public void setRequestMoreDataListener(IFunc<Boolean> listener){
		requestMoreData = listener;
	}

	/**
	 * 构造公共适配器
	 * @param context : 上下文
	 * @param listView：当前适配器所属的ListView控件
	 * @param source : 列表数据源
	 * @param getViewCallback : 适配器获得View方法的接口
	 */
	public CommonAdapter(Context context,ListView listView, List<T> source,
			ICommonAdapter<T> getViewCallback) {
//		mFooterView = View.inflate(context, R.layout.epay_tq_list_more, null);
		totalNumber  = -1;
		this.context = context;
		this.listView = listView;
		sourceList = source;
		commonAdapterCallBack = getViewCallback;
//		if (listView.getFooterViewsCount() <= 0) {
//			listView.addFooterView(mFooterView);
//		}
		listView.setAdapter(this);
//		mFooterView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(requestMoreData == null || sourceList == null)
//					return;
//			    requestMoreData.callBack(v);
//			}
//		});
//		listView.removeFooterView(mFooterView);
	}
	
	@Override
	public int getCount() {
		return sourceList != null ? sourceList.size() : 0;
	}

	@Override
	public T getItem(int arg0) {
		if (sourceList != null && sourceList.size() > arg0)
			return sourceList.get(arg0);
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		LayoutInflater inflater = LayoutInflater.from(context);
		if (commonAdapterCallBack != null)
			return commonAdapterCallBack.getView(arg0, getItem(arg0),inflater, convertView, arg2);
		return null;
	}

	
}
