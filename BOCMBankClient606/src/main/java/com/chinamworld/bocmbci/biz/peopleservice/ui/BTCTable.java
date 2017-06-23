package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IFunction;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;

public class BTCTable extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCTable.class.getSimpleName();
	
	private  View curView = null;
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCTable(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Table;
		//this.context = context;
	}
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name=btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		Map<String, String> dbMaps = new HashMap<String, String>(); 
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		LinearLayout childLayout = new LinearLayout(context);
		childLayout.setLayoutParams(layoutParams);
		//之前。
		childLayout.setOrientation(LinearLayout.VERTICAL);//item 的布局显示
		//wuhan
//		childLayout.setOrientation(LinearLayout.HORIZONTAL);
		((ViewGroup) view).addView(childLayout, layoutParams);
//		return super.drawLable(null, childLayout);
		for (int i = 0; i < childElements.size(); i++) {
			childElement = childElements.get(i);

			btcDrawLable = childElement.getBTCDrawLable();
			if (btcDrawLable != null) {

				btcDrawLable.drawLable(dbMaps, childLayout);
			}
		}
		curView = childLayout;
		//生产bug 加载更多
		intPagination(BTCCMWApplication.paginationCount/*childLayout.getChildCount()*/);
		
		
		return null;
	}

	
	private void intPagination(int count) {
	
		BTCElement viewElement = findNearElemntBy(new IFunction() {
			@Override
			public <T> boolean func(T t) {
				return ((BTCElement)t).getBTCDrawLable().elementType == ElementType.View;
			}
		});
		
		BTCElement paginationElement = findElementToChildren(viewElement,new IFunction() {
			@Override
			public <T> boolean func(T t) {
				return ((BTCElement)t).getBTCDrawLable().elementType == ElementType.Pagination;
			}
		});
		if(paginationElement == null)
			return;
		
		BTCPagination element = (BTCPagination)paginationElement.getBTCDrawLable();
		element.initPagination(count,new IHttpCallBack(){

			@Override
			public void requestHttpSuccess(Object result) {
				// TODO Auto-generated method stub
				// 开始刷新页面
				Map<String, String> dbMap= new HashMap<String,String>();
				dbMap.put("tableReflesh", "true");
				BTCElement childElement;
				BTCDrawLable btcDrawLable;
				List<BTCElement> childElements = btcElement.getChildElements();
				for (int i = 0; i < childElements.size(); i++) {
					childElement = childElements.get(i);
					btcDrawLable = childElement.getBTCDrawLable();
					if (btcDrawLable != null) {
						btcDrawLable.drawLable(dbMap, curView);
					}
				}
			}

		});
	}
	
	
}

