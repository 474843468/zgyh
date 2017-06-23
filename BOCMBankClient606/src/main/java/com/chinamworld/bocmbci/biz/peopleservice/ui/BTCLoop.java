package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;
import com.chinamworld.bocmbci.log.LogGloble;

public class BTCLoop extends BTCDrawLable {
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCLoop.class.getSimpleName();

	String loopfor = null;
	String loopuse = null;
//	Map<String, String> optionMap = new HashMap<String, String>();
	
	/** 更多分页时，需要保留最原始 的数据 */
	private List<Map<String, String>> oldList = new ArrayList<Map<String, String>>();
	
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCLoop(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Loop;
		oldList.clear();
		//this.context = context;
	}

	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements(); 
		if (BTCLable.OPTION.equals(childElements.get(0).getElementName())) {

			return drawLableoption(btcElement, dbMap, view);
		}
		if(!(dbMap != null && dbMap.containsKey("tableReflesh") && "true" == dbMap.get("tableReflesh")))
			oldList.clear();  // 如果不是table刷新，则需要将oldList清空
		
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		loopfor = params.get(BTCLable.FOR);//待循环的列表别名
		loopuse = params.get(BTCLable.USE);//列表集合
		LogGloble.i("wuhan", "loop for=="+loopfor);
		LogGloble.i("wuhan", "loop use=="+loopuse);

		
		List<Map<String, String>> datalist = (List<Map<String, String>>) BTCCMWApplication.responsemap.get(loopuse);
		  if (datalist == null)
			  datalist = new ArrayList<Map<String, String>>();
		  if (datalist.size() == 0){
			  if(datalist.size()==0 && btcElement.getParentElement().getElementName().equals(BTCLable.TABLE)){
				  LinearLayout Layout1 = new LinearLayout(context);
					Layout1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,(int)context.getResources().getDimension(R.dimen.btn_bottom_height))); 
					Layout1.setOrientation(LinearLayout.HORIZONTAL);
					Layout1.setGravity(Gravity.CENTER_VERTICAL);
					TextView textview = new TextView(context);
					textview.setTextSize(15);
					textview.setTextColor(context.getResources().getColor(R.color.black));
					textview.setText("无符合查询条件的记录！");
					Layout1.addView(textview);
					((ViewGroup) view).addView(Layout1);
			  }
		  }
		  int nIndex = oldList.size();
//		  List<Map<String, String>> itemsSource = new ArrayList<Map<String, String>>();
//		  if (datalist != null)
//		  {
//		      for (Map<String, String> item : datalist)
//		      {
//		    	  int n = oldList.indexOf(item);
//		          if (!oldList.contains(item))
//		        	  itemsSource.add(item);
//		      }
//		  }
		  if (datalist != null)
		      oldList = new ArrayList<Map<String, String>>(datalist); // 保存现在已经处理的数据源
          
		if(datalist == null || datalist.size() <= nIndex){
			return true;
		}
		
		List<Map<String, String>> loopList = new ArrayList<Map<String,String>>();//改变后的。department.name
		//生产bug 加载更多
		BTCCMWApplication.paginationCount = datalist.size();
		for(int h=nIndex;h<datalist.size();h++){
			Map<String,String> map=datalist.get(h);
			Map<String, String> loopMap = new HashMap<String, String>();
			 for(Map.Entry<String, String> entrys : map.entrySet()){
				 	
				   String keys = entrys.getKey().toString();
				   String loopKey=loopfor+"."+keys;
				   String value = entrys.getValue();
				   loopMap.put(loopKey, value);
			   }
			 loopMap.put("loopIndex", h + "");
			 loopList.add(loopMap);
			 
		}
//		BTCCMWApplication.loopList = loopList;
	
		for (int j = 0; j < loopList.size(); j++) {
			for (int i = 0; i < childElements.size(); i++) {
				btcDrawLable = childElements.get(i).getBTCDrawLable();
				if (btcDrawLable != null) {
					btcDrawLable.drawLable(loopList.get(j), view);
				}
				
			}
			
		}

		return null;
	}

	public Object drawLableoption(BTCElement btcElement,
			Map<String, String> dbMap, View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		List<String> spinnerList = new ArrayList<String>();
		loopfor = params.get(BTCLable.FOR);
		loopuse = params.get(BTCLable.USE);
		List<Map<String, String>>  loopMap = null;
		Map<String, String> optionMap = new HashMap<String, String>();
		if(BTCCMWApplication.responsemap.containsKey(loopuse)){
			loopMap =  (List<Map<String, String>>) BTCCMWApplication.responsemap.get(loopuse);
		}
		String text  ="";
//		childElements.get(0).getChildElements()
		if(childElements.get(0).getChildElements().get(0).getChildElements()!=null){
			for (int i = 0; i < childElements.size(); i++) {
				childElement = childElements.get(0);
//				btcDrawLable = BTCLableFactory.createLableInstance(null,context,
//						childElement.getElementName());
				btcDrawLable = childElement.getBTCDrawLable();
				if(btcDrawLable!=null){
					optionMap=(Map<String, String>) btcDrawLable.drawLable(new HashMap<String, String>(), view);//item.SCHNAME
					
//					text = text.substring(text.indexOf(".")+1,text.length()-1);
					//item.SCHNAME   loopMap里面有的只是SCHNAME	
					
					for(Map.Entry<String, String> entry:optionMap.entrySet()){
						text = entry.getKey();//${item.merchname}
						if(text.contains("$")&&text.contains(".")){
							String strs  = text;
							strs = strs.substring(strs.indexOf(".")+1, strs.length()-1);
							if(loopMap!=null ){
								for(int k = 0 ;k <loopMap.size();k++){
									if(loopMap.get(k).containsKey(strs)){
										text = text.substring(text.indexOf(".")+1, text.length()-1);
										break;
									}
								}
								
							}else{
								text = text.substring(text.indexOf(".")+1, text.length()-1);
								break;
							}
						}else if(text.contains(".")){//item.SCHNAME
							String strs  = text;
							strs = strs.substring(strs.indexOf(".")+1, strs.length());
							if(loopMap!=null ){
								for(int k = 0 ;k <loopMap.size();k++){
									if(loopMap.get(k).containsKey(strs)){
										text = text.substring(text.indexOf(".")+1, text.length());
										break;
									}
								}
								
							}else{
								text = text.substring(text.indexOf(".")+1, text.length()-1);
								break;
							}
						}
					}
					
				}
				
			}
		}else {
			text= childElements.get(0).getChildElements().get(0).getText();//item.merchname//进行显示的值
			text = text.substring(text.indexOf(".")+1,text.length()-1);//${item.merchname}
		}
		
		List<Map<String, String>> datalist = (List<Map<String, String>>) BTCCMWApplication.responsemap.get(loopuse);
//		  [{"agent_sub_no":"0000000","merchname":"呈贡"}]
		
//		List<Map<String, String>> datalist =new ArrayList<Map<String,String>>();
//		Map<String, String> map=new HashMap<String, String>();
//		map.put("sun", "1");
//		datalist.add(map);
		
		for (int j = 0; j < datalist.size(); j++) {// [{"agent_sub_no":"0000000","merchname":"呈贡"}]
			childElement = childElements.get(0);
			String value1 = "";
			String key =childElement.getParams().get(BTCLable.VALUE);//${item.agent_sub_no}  //上送的值  item.SCH_CODE
			if(key.contains("$") && key.contains("{")&&key.contains(".")){
				key = key.substring(key.indexOf(".")+1, key.length()-1);//agent_sub_no
			}else if(key.contains(".")){//item.SCH_CODE
				key = key.substring(key.indexOf(".")+1, key.length());
			}
			
			Map<String, String> mas = datalist.get(j);//{"SCHNAME":"TEST_SCHOOL","SCH_CODE":"00006129"}
			for(Map.Entry<String, String> entry : mas.entrySet()){
				String keys1= entry.getKey();
				if(text.equals(keys1)){//"merchname"
					value1 = entry.getValue();//:"呈贡"
					spinnerList.add(value1);
				}
			}
//			if(key.substring(key.indexOf(".")+1, key.length()-1).equals(arg0))
			String value=datalist.get(j).get(key);
			dbMap.put(key, value);
			
			optionMap.put(value1, value);//"呈贡",0000000 value1显示的值,value是当选重 显示的值后，上送的值。
		}
		BTCCMWApplication.optionMap.putAll(optionMap);
		return spinnerList;
	}
}

