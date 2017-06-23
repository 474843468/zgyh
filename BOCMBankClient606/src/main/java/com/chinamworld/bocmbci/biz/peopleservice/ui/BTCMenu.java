package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IFunction;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;
import com.chinamworld.bocmbci.biz.peopleservice.utils.PeopleServiceMenuAdapter;

/**
 * 类功能描述：用于画menu标签
 * 
 * @author：秦
 * @version： 1.0
 * @see 包名：com.chinamworld.btwapview.ui
 *      父类：com.chinamworld.btwapview.ui.BTCDrawLable 相关数据：
 */
public class BTCMenu extends BTCDrawLable {
	/** 上下文对象 */
//	private Context context;// 上下文对象
	private static final String TAG = BTCMenu.class.getSimpleName();
	 BTCUiActivity uiActivity;
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCMenu(Context context,BTCElement element) {
		super(context,element);
		elementType = ElementType.Menu;
		this.uiActivity = BTCUiActivity.getInstance();
		
	}

	private ArrayList<Map<String,String>> itemList = new ArrayList<Map<String,String>>();
	
	private void init(){
		itemList.clear();
		BTCElement childElement;
		Map<String,String> map = null;
		for (int i = 0; i < btcElement.getChildElements().size(); i++) {
			childElement = btcElement.getChildElements().get(i);

			List<BTCElement> child= childElement.getChildElements();
			String buttontext = child.get(0).getText();
			String key = childElement.getParams().get(BTCLable.FLOW);
			map = new HashMap<String,String>();
			map.put("name", buttontext);
			map.put("tag", key);
			itemList.add(map);
		}
	}
	
	/**
	 * 方法功能说明 ：重写父类BTCDrawLable的该方法,在这个方法中, 根据子标签的type属性的不同,跳转到不同的方法中去执行不同的操作
	 * 
	 * @param btcElement
	 *            menu标签的实体对象 dbMap 数据库查询结果集的一条记录的数据信息 view 父容器
	 * @return void
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {	
		init();
		LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.height=ViewGroup.LayoutParams.FILL_PARENT;
		layoutParams.width=ViewGroup.LayoutParams.FILL_PARENT;
		view.setLayoutParams(layoutParams);
		elementView  =View.inflate(context, R.layout.peopleservice_listview_1, null);
		ListView lv1=(ListView)elementView.findViewById(R.id.ps_listview);
		lv1.setOnItemClickListener(onclick);

		PeopleServiceMenuAdapter adapter = new PeopleServiceMenuAdapter(context, itemList);
//			listview.setAdapter(adapter);
		
		/*by dl*/
		lv1.setAdapter(adapter);

		
		adapter.notifyDataSetChanged();
		
		/*by dl*/
	
		((ViewGroup) view).addView(elementView);
		
		BTCUiActivity.Instance().IntentToActivity(this,view);
		return false;
	}

	
	public OnItemClickListener onclick  = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
				long arg3) {
			requestSystemTime(new IHttpCallBack() {
				
				@Override
				public void requestHttpSuccess(Object result) {
					BTCCMWApplication.security= null;
					BTCCMWApplication.isContainSecurityInit = false;
					BTCCMWApplication.isContainSecurityActive = false;
					BTCCMWApplication.securityRandom = "";
					BTCCMWApplication.factorList = null;
					BTCCMWApplication.responsemap.clear();
					BTCCMWApplication.requestParamsMap.clear();
					BTCCMWApplication.requestMap.clear();
//					BTCCMWApplication.linkParamsMap.clear();
					//_SYSTEMDATETIME
					//发送请求系统时间
					String nameflow = itemList.get(arg2).get("tag");
					BTCCMWApplication.setFlowElement(nameflow);
				//	BTCElement element = BTCMenu.this.FindElementBy(BTCMenu.this.itemList.get(arg2).get("tag"));
					 BTCElement bt = BTCMenu.this.findNearElemntBy(new IFunction(){

							@Override
							public <T> boolean func(T t) {
								String name = ((BTCElement)t).getName(null);
								
								if(name != null &&  name.equals(BTCMenu.this.itemList.get(arg2).get("tag"))) {
									return true;
								}
								return false;
							}});	
					BTCUiActivity.Instance().navigationToActivity(bt);
				}
			});
    		
		}
	};

	
	private void requestSystemTime(final IHttpCallBack callback){
		uiActivity.requestFromMenuSystemDateTime(callback);
		
	}
}
