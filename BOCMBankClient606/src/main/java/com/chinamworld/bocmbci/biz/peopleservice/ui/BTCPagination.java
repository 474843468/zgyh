
package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCommbean;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLableAttribute;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IFunction;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;
import com.chinamworld.bocmbci.log.LogGloble;

public class BTCPagination extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCPagination.class.getSimpleName();
	private Object callbackObject;//请求回调对象
	private HashMap<String, Integer> request_param_map = new HashMap<String, Integer>();//请求参数
	private final int BTU_TEXT_SIZE=16;// by dl +
	private BTCUiActivity uiActivity;
	private ArrayList<BTCCommbean> requestList=new ArrayList<BTCCommbean>();
	/** 当前已经显示的记录条数  */
	private int currentIndex ;//生产bug 加载更多
	
	private String totalNumber= null;
	

	
	private IHttpCallBack refleshUIData;
	
	private View moreView;//生产bug 加载更多
	
	private int showCount;//生产bug 加载更多
	/** 初始化Pagination 信息  */
	public void initPagination(int showCount,IHttpCallBack refleshUI ){
		refleshUIData = refleshUI;
		this.showCount = showCount;
	}
	
	private int count = 0;
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCPagination(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Pagination;
		//this.context = context;
		this.uiActivity=BTCUiActivity.getInstance();
	}
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		currentIndex =  showCount;//生产bug 加载更多
		BTCCMWApplication.totalNumber = currentIndex;
		Map<String, String> params = btcElement.getParams();	
		String pagesize = params.get("pagesize");
//		if (pagesize!=null) {//生产bug 加载更多
//			currentIndex=Integer.parseInt(pagesize); 
//		}
		if(params.containsKey(BTCLableAttribute.TOTALNUMBER)){
			totalNumber=params.get(BTCLableAttribute.TOTALNUMBER).replace("${", "").replace("}", "");
			try{
				count = Integer.parseInt(getValue(totalNumber)) ;
			}catch (Exception e) {
				e.printStackTrace();
			} 
			finally  {}
		}
		
		//生产bug 加载更多
		moreView = initButton("加载更多");
		((ViewGroup)view).addView(moreView);
		ShowMoreButton();
		return true;
	}
	
	//生产bug 加载更多
	private void ShowMoreButton() {
		if(count <= currentIndex) 
			moreView.setVisibility(View.GONE);	
		else 
			moreView.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 初始化  "加载更多" 按钮
	 * @param btu_text按钮名 (String)
	 * @return Button
	 */
	public View initButton(String btu_text){
		View v = LayoutInflater.from(context).inflate(R.layout.peopleservice_pagination_button, null);
		Button button = (Button) v.findViewById(R.id.pagination_button);
		button.setTextSize(15);
		button.setTextColor(context.getResources().getColor(R.color.black));
		button.setOnClickListener(onClickListener);
		button.setText(btu_text);
		return v;
	}
	
	/**
	 * "加载更多"按钮  点击事件监听
	 */
	public OnClickListener onClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			String str = null;
			if(totalNumber.contains("."))
			{
				int nIndex = totalNumber.indexOf(".");
				str = totalNumber.substring(0,nIndex);
			}
			final String name = str;
			BTCElement commonElement = findNearElemntBy(new IFunction() {
				@Override
				public <T> boolean func(T t) {
					return name.equals(((BTCElement)t).getName(null)) && (((BTCElement)t).getBTCDrawLable() instanceof BTCComm);
				}
			});
			
			final BTCCommbean request = (BTCCommbean) commonElement.getBTCDrawLable().drawLable(null, v);
			Map<String, Object> postParams = new HashMap<String, Object>();
			postParams =request.getParams();
			
			//生产版本bug ,lines  3
			String string = postParams.get("toCspParamsMap").toString();
//			string = setCurrentIndex(string);
			postParams.put("toCspParamsMap", string);
			postParams.put("isSecurityTools", "0");
			request.setParams(postParams);
		
			uiActivity.requestPeopleService(request, new IHttpCallBack(){

				@Override
				public void requestHttpSuccess(Object result) {
					ResponseDataHandle(result,request);
					if(refleshUIData != null)
						refleshUIData.requestHttpSuccess(true);
					ShowMoreButton();//生产bug 加载更多
				}
				
			});
		}
	};
	
	////生产bug 加载更多
	private String setCurrentIndex(String param) {
		String tmpString = "\"currentIndex\":\"";
		String tmpString2 = "\"currentIndex\":\"" + currentIndex + "\"";
	    if(param.contains(tmpString))
	    {
	    	int nIndex = param.indexOf(tmpString);
	    	String tmp = param.substring(nIndex + tmpString.length());
	    	nIndex = tmp.indexOf("\"");
	    	tmp = tmp.substring(0,nIndex);
	    	param =param.replace(tmpString + tmp + "\"", tmpString2);
	    }
		return param;
	}
	
	private void ResponseDataHandle(Object result,BTCCommbean reques)
	{
		try {
		//	BTCCommbean reques = commlist.get(0);
			BiiResponse biiResponse = (BiiResponse) result;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			
			LogGloble.i("info", "BTCAction result====" +  biiResponseBody.getResult().toString());
			Map<String, Object> resultmap = (Map<String, Object>) biiResponseBody.getResult();
			
			if(resultmap.size()!=0){ //为了区分没有comm标签的时候，只有sercurity时，resultmap.size=0也算成功。
				Map<String, Object> requestMap =new HashMap<String, Object>();//请求回来的json
//				String requestKeys = "";
				 for(Map.Entry<String, Object> entrys : resultmap.entrySet()){
					   String keys = entrys.getKey().toString();
					   if("cspReturnMap".equals(keys)&&resultmap.get(keys)!=null){
						   requestMap=(Map<String, Object>) resultmap.get(keys);
						   
					   }else if("pageParamsMap".equals(keys)&&resultmap.get(keys)!=null){
						   requestMap=(Map<String, Object>) resultmap.get(keys);
						   
					   }
					 //请求回来的所有数据；有可能在use,或一下次请求的用到里面的数据；
//					   BTCCMWApplication.resultmap =requestMap;
				   }
				 List<String> requestsKeys=new ArrayList<String>();
				 if(requestMap!=null&&requestMap.size()!=0){
					 if(requestMap!=null&&requestMap.size()!=0){
//					    	{ERR_CODE=00, ERR_MSG=null, DATA_LIST=[{"agent_sub_no":"0000000","merchname":"呈贡"}]}
					    	for(Map.Entry<String, Object> entry:requestMap.entrySet()){
					    		String key = entry.getKey();
					    		requestsKeys.add(key);
					    	}
					    }
					 //用BTCCMWApplication.Response 中的key与reques.getResponse().entrySet() 中的key相同
					 //用BTCCMWApplication.Response 中的value 与requestsKeys中的key比较，相同的，则做为value放入BTCCMWApplication.responsemap
					 //{cylist:DATA_LIST}
					 if(reques.getResponse() !=null && reques.getResponse().size()!=0){
//						 BTCCMWApplication.responsemap.clear();
						  for (Map.Entry<String, String> entry : reques.getResponse().entrySet()) {
//								<param name="userName">USRNAM</param>/
							  String values = entry.getValue().toString();
							  String key = entry.getKey().toString();//cylist  userName
							  if(requestMap.get(key)!=null ||requestMap.get(values)!=null){
								  String value = entry.getValue().toString();//DATA_LIST USRNAM
								   if(requestsKeys.contains(key)||requestsKeys.contains(value)){
									  //getDepartments. hospitalName组织的是xml中responsemap中的数据
									   if(requestMap.get(key)!=null ){
										   Object data = addToList(requestMap.get(key),reques.getName()+"."+key);
										   BTCCMWApplication.responsemap.put(reques.getName()+"."+key, data);
//										   InfoInquery.DsfFiller  DSF_FILLER
										   BTCCMWApplication.responsemap.put(reques.getName()+"."+value, data);
										   BTCCMWApplication.responsemap.put(value, data);
										   BTCCMWApplication.responsemap.put(key, data);
									   }else if(requestMap.get(value) !=null){
										   Object data = addToList(requestMap.get(value),reques.getName()+"."+value);
										   BTCCMWApplication.responsemap.put(reques.getName()+"."+value, data);
										   BTCCMWApplication.responsemap.put(reques.getName()+"."+key, data);
//										   BTCCMWApplication.responsemap.put(value, data);
//										   BTCCMWApplication.responsemap.put(key, data);
//										   InfoInquery.DSF_FILLER  DSF_FILLER
									   }
									   else{
//										   BTCCMWApplication.responsemap.put(key, " ");
//										   BTCCMWApplication.responsemap.put(value, " ");
										   BTCCMWApplication.responsemap.put(reques.getName()+"."+key, " ");
										   BTCCMWApplication.responsemap.put(reques.getName()+"."+value, " ");
									   }
									  
								   }
							  }
						  
						  } 
						  
						  //response里面没有的字段
						  for (int i = 0 ;i<requestsKeys.size();i++) {
							  String key = requestsKeys.get(i);
							 Map<String, String> responseMap = reques.getResponse();
							 if(!responseMap.containsValue(key)){
								 BTCCMWApplication.responsemap.put(reques.getName()+"."+key, requestMap.get(key));
							 }
						  
						  } 
						  
					 }else{//没有response的情况下
						 
						 for (Map.Entry<String, Object> entry : requestMap.entrySet()) {
							   String key = entry.getKey().toString();//cylist  hospitalName
							   if(requestMap.get(key)!=null){
								   String value = entry.getValue().toString();//DATA_LIST
								   if(requestMap.get(key)!=null ){
									   Object data = addToList(requestMap.get(key),reques.getName()+"."+key);
									   BTCCMWApplication.responsemap.put(reques.getName()+"."+key, data);
//									   InfoInquery.DsfFiller  DSF_FILLER
									   BTCCMWApplication.responsemap.put(reques.getName()+"."+value, data);
									   BTCCMWApplication.responsemap.put(key, data);
									   BTCCMWApplication.responsemap.put(value, data);
								   }else if(requestMap.get(value) !=null){
									   Object data = addToList(requestMap.get(value),reques.getName()+"."+key);
									   BTCCMWApplication.responsemap.put(value, data);
									   BTCCMWApplication.responsemap.put(key, data);
									   BTCCMWApplication.responsemap.put(reques.getName()+"."+value, data);
									   BTCCMWApplication.responsemap.put(reques.getName()+"."+key, data);
//									   InfoInquery.DSF_FILLER  DSF_FILLER
								   }
								   else{
									   BTCCMWApplication.responsemap.put(reques.getName()+"."+key, " ");
									   BTCCMWApplication.responsemap.put(reques.getName()+"."+value, " ");
								   }
							   }
						  } 
					 }
					
					  
					LogGloble.i(TAG," result="+result.toString());
				 }
				 
				 Map<String, Object> securityToolsInfo = new HashMap<String, Object>();
				 //有安全因的时情况有  securityToolsInfo active 
				 if(BTCCMWApplication.isContainSecurityActive&&resultmap.containsKey("securityToolsInfo")){
					securityToolsInfo = (Map<String, Object>) resultmap.get("securityToolsInfo"); 
					LogGloble.i("info", "------------active securityToolsInfo");
					List<HashMap<String, Object>> factorList = new ArrayList<HashMap<String,Object>>();
					factorList = (List<HashMap<String, Object>>) securityToolsInfo.get("factorList");
					BTCCMWApplication.factorList = factorList;
				 }
				    
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/** 将获得的数据，添加到原列表中 */
	private  Object addToList(Object data,String key)
	{
		try
		{
			List<Map<String, String>> list = (List<Map<String, String>>)data;
			
			List<Map<String, String>> oldList = null;
			if(BTCCMWApplication.responsemap.containsKey(key)) {
				oldList = (List<Map<String, String>>)BTCCMWApplication.responsemap.get(key);		  
			}
			for(Map<String, String> item : list){
				currentIndex++;
				oldList.add(item);
			}			
			BTCCMWApplication.totalNumber = currentIndex;
			return oldList;
		}
		catch (Exception e) {
			return data;
		}
	
	}
}











