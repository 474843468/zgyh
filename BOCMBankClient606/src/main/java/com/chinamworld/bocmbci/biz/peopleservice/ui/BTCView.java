package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IAwaitExecute;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IExecuteFuction;
import com.chinamworld.bocmbci.log.LogGloble;

public class BTCView extends BTCDrawLable {
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCView.class.getSimpleName();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCView(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.View;
	}

	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		view = BTCUiActivity.Instance().getActivityView(this.btcElement);
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name = btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		drawLableByChild(childElements,view,0);
//		List<BTCElement> list = FindElementBy(null, btcElement,new IFunction(){
//			@Override
//			public <T> boolean func(T t) {
//				return ((BTCElement)t).getBTCDrawLable().elementType == ElementType.Hiddenbox;
//			}});
//		
//		for (int k = 0; k < childElements.size(); k++){
//			childElement = childElements.get(k);
//			btcDrawLable = childElement.getBTCDrawLable();
//			if((btcDrawLable instanceof BTCHiddenbox)){
//				btcDrawLable.drawLable(BTCCMWApplication.hiddenboxmap, view);
//			}else{
//				btcDrawLable.drawLable(dbMap, view);
//			}
//		}
		
		
		
		
		
		
		
		
//		for (int k = 0; k < childElements.size(); k++){
//			childElement = childElements.get(k);
//			btcDrawLable = childElement.getBTCDrawLable();
//			if(!(btcDrawLable instanceof BTCHiddenbox)){
//				btcDrawLable.drawLable(dbMap, view);
//			}
//		}
//		for (int i = 0; i < list.size(); i++) {
//			childElement = list.get(i);
//			btcDrawLable = childElement.getBTCDrawLable();
//			if (btcDrawLable != null) {
//				btcDrawLable.drawLable(BTCCMWApplication.hiddenboxmap, view);
//			}
//		}
		
//		if(childElements.toString().contains(BTCLable.HIDDENBOX)){
//			for (int k = 0; k < childElements.size(); k++){
//				childElement = childElements.get(k);
//				btcDrawLable = childElement.getBTCDrawLable();
//				if(!(btcDrawLable instanceof BTCHiddenbox)){
//					btcDrawLable.drawLable(dbMap, view);
//				}
//			}
//			for (int i = 0; i < childElements.size(); i++) {
//				
//				childElement = childElements.get(i);
//
//				btcDrawLable = childElement.getBTCDrawLable();
//				if (btcDrawLable != null) {
//					 if (BTCLable.HIDDENBOX.equals(childElement.getElementName())) {
//						btcDrawLable.drawLable(BTCCMWApplication.hiddenboxmap, view);
//					}
//				}
//			}
//		}else{
//			for (int i = 0; i < childElements.size(); i++) {
//				childElement = childElements.get(i);
//				btcDrawLable = childElement.getBTCDrawLable();
//				if (btcDrawLable != null) {
//					btcDrawLable.drawLable(dbMap, view);
//				}
//			}
//		}
//		LogGloble.i(TAG, BTCCMWApplication.hiddenboxmap.toString());
//		BTCUiActivity.Instance().IntentToActivity(this,view);
		return true;
	}

	
	private void drawLableByChild(final List<BTCElement> childElements,final View view, int i) {
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		
		for (; i < childElements.size(); i++) {  
			childElement = childElements.get(i);

			btcDrawLable = childElement.getBTCDrawLable();
			if (btcDrawLable == null) 
				continue;
			if((btcDrawLable instanceof BTCHiddenbox)){
//				btcDrawLable.drawLable(null, view);
				continue;
			}
			if(btcDrawLable instanceof IAwaitExecute){
				final int k = i + 1;
				((IAwaitExecute)btcDrawLable).awaitExecute( null, view,new IExecuteFuction(){
					@Override
					public void executeResultCallBack(Object param) {
						// TODO Auto-generated method stub
						drawLableByChild(childElements,view,k);
					}}
				);
				return;
			}
			
			btcDrawLable.drawLable(null, view);
		}
		
		LogGloble.i(TAG, BTCCMWApplication.hiddenboxmap.toString());
		BTCUiActivity.Instance().IntentToActivity(this,view);
//		BTCCMWApplication.linkParamsMap.clear();
	}
	

}
