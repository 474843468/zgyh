package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;

public class BTCPanel extends BTCDrawLable {
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCPanel.class.getSimpleName();

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCPanel(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Panel;
	//	this.context = context;
	}

	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name = btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		LinearLayout childLayout = new LinearLayout(context);
		LinearLayout childLayout1 = new LinearLayout(context);
//		LinearLayout childLayout2 = new LinearLayout(context);
		childLayout.setLayoutParams(layoutParams);
		childLayout.setOrientation(LinearLayout.VERTICAL);
		if (params.containsKey("type")&& params.get("type").equals(BTCLable.BUTTON)) {
			childLayout.setOrientation(LinearLayout.VERTICAL);
		}
		if (params.containsKey("layout") && params.get("layout").equals("2")) {
			childLayout.setOrientation(LinearLayout.VERTICAL);
			childLayout1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
//			childLayout1.setLayoutParams(new LayoutParams(0,
//					LayoutParams.WRAP_CONTENT,1)); 
			childLayout1.setOrientation(LinearLayout.VERTICAL);
//			childLayout2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
//					LayoutParams.WRAP_CONTENT));
//			childLayout2.setOrientation(LinearLayout.VERTICAL);
			((ViewGroup) childLayout).addView(childLayout1);
//			((ViewGroup) childLayout).addView(childLayout2);

		}
		((ViewGroup) view).addView(childLayout,layoutParams);

		for (int i = 0; i < childElements.size(); i++) {
			childElement = childElements.get(i);
			
//			btcDrawLable = BTCLableFactory.createLableInstance(null,context,
//					childElement.getElementName());
			btcDrawLable = childElement.getBTCDrawLable();
			if (btcDrawLable != null) {
				if (params.containsKey("layout")
						&& params.get("layout").equals("2")) {
//					if (i % 2 == 0) {
						btcDrawLable.drawLable(null, childLayout1);
//					} else {
//						btcDrawLable.drawLable(null, childLayout2);
//					}
				} else {
					btcDrawLable.drawLable(null, childLayout);
				}

			}
		}

		return null;
	}

}
