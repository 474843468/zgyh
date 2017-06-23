package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IAwaitExecute;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IExecuteFuction;

public class BTCStep  extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCStep.class.getSimpleName();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCStep(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Step;
	//	this.context = context;
	}
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name = btcElement.getElementName();
		BTCCMWApplication.totalNumber = 0;//每一个step里面都可能会有pageniation,所以要对此数据清空。
		String stepName = params.get("name");
		BTCCMWApplication.stepName = stepName;
		drawLableByChild(childElements,view,0);
		
		return false;
	}
	
	private void drawLableByChild(final List<BTCElement> childElements,final View view, int i) {
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		
		for (; i < childElements.size(); i++) {  
			childElement = childElements.get(i);

			btcDrawLable = childElement.getBTCDrawLable();
			if (btcDrawLable == null) 
				continue;
			if(btcDrawLable instanceof IAwaitExecute){
				final int k = i + 1;
				((IAwaitExecute)btcDrawLable).awaitExecute( null, view,new IExecuteFuction(){
					@Override
					public void executeResultCallBack(Object param) {
						// TODO Auto-generated method stub
						drawLableByChild(childElements,view,k);
					}}
				);
				break;
			}
			
			btcDrawLable.drawLable(null, view);
		}
		
		BTCCMWApplication.dispMap=null;
		BTCCMWApplication.isDisp = "";
	}
	

}
