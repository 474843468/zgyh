package com.chinamworld.bocmbci.biz.tran.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.log.LogGloble;

/*
 * 功能描述：实现listview加头部，头部固定不移动
 * 创建日期：2013-05-31
 * 创建者：    xuxq
 * 
 * **/
public class HeadListView extends LinearLayout {
	
	//listview头的第一列
	private TextView column_1;
	//listview头的第二列
	private TextView column_2;
	//listview头的第三列
	private TextView column_3;
	
	private ListView listView;
	//listview头信息的容器
	private View listviewHeader;
	
	View view;
	
	public HeadListView(Context context) {
		super(context);
		init();
	}
	
	public HeadListView(Context context, AttributeSet attrs) {
		super(context,attrs);
		init();
	}
	
	private void init() {
		
	    view = inflate(this.getContext(), R.layout.tran_add_head_listview, null);
	    
	    listviewHeader = view.findViewById(R.id.list_view_header); 
	    listviewHeader.setVisibility(View.GONE);
		
		column_1 = (TextView)view.findViewById(R.id.tv_column_1);
		column_2 = (TextView)view.findViewById(R.id.tv_column_2);
		column_3 = (TextView)view.findViewById(R.id.tv_column_3);
		
		listView = (ListView)view.findViewById(R.id.list_view);
		
		LogGloble.i(HeadListView.class.getSimpleName(), "初始化....");
		
	}
	
    @Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		addView(view);
	}

	/**
     * 设置头内容
     * 
     * param col_1 第一列 
     * param col_2 第二列
     * param col_3 第三列
     * **/
	public void setHeaderContent(String col_1,String col_2,String col_3) {
		column_1.setText(col_1);
		column_2.setText(col_2);
		column_3.setText(col_3);
		
		LogGloble.i(HeadListView.class.getSimpleName(), "设置头部信息....");
	}
	
	/*
	 * 设置适配器
	 * **/
	public void setAdapter(android.widget.BaseAdapter adapter) {
		listView.setAdapter(adapter);
		listviewHeader.setVisibility(View.VISIBLE);
		LogGloble.i(HeadListView.class.getSimpleName(), "设置适配器....");
	}
	
	/*
	 * 设置listview的item点击事件
	 * **/
	public void setItemClickListener(android.widget.AdapterView.OnItemClickListener listener) {
		listView.setOnItemClickListener(listener);
		
		LogGloble.i(HeadListView.class.getSimpleName(), "设置点击事件....");
	}
	
	public void addFootView(View v){
		listView.removeFooterView(v);
		listView.addFooterView(v);
	}
	
	public void removeFootView(View v){
		if(v != null){
			listView.removeFooterView(v);
		}
	}

}
