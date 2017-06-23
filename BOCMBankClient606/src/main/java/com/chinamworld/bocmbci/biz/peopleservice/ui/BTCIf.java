package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLableAttribute;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLableAttributeValue;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IAwaitExecute;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IExecuteFuction;

public class BTCIf extends BTCDrawLable implements IAwaitExecute {
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCIf.class.getSimpleName();
	/**全局变量对象*/
	private BTCCMWApplication cmwApplication;
	
	/** if标签执行结果 */
	private boolean executeResult = true;
	
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCIf(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.If;
		cmwApplication = BTCUiActivity.getApp();
		//this.context = context;
	}

	@Override
	public List<BTCElement> getChildElements() {
		if(executeResult())
			return super.getChildElements();
		
		return new ArrayList<BTCElement>(); 
	}
	
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		// 若不存在标签，或不存在判断属性，或者没有要显示的子标签内容，都不做处理
		if (btcElement == null || btcElement.getParams() == null
				|| btcElement.getChildElements() == null) {
			return null;
		}
		executeResult = executeResult();
		if (executeResult) {
			drawLableByChild(btcElement.getChildElements(), view, 0,null,dbMap);
		}
		return true;
	}

	/** 计算if标签的值  */
	private boolean executeResult(){
		boolean result = false;
		Map<String, String> params = btcElement.getParams();
		
		String test = params.get(BTCLableAttribute.TEST);
//		BTCCMWApplication.ifMap;
//		BTCCMWApplication.boungtragetValue;
		// &&（与）&amp;&amp; 、
		if (test.contains(BTCLableAttributeValue.IF_COMPARE_YU)) {
			String teststring = test.substring(2, test.length() - 1);
			String[] temp = teststring
					.split(BTCLableAttributeValue.IF_COMPARE_YU);
			Boolean [] part = new Boolean[temp.length];
			result = true;
			for(int i = 0 ;i<temp.length;i++){
				part[i] = isCanDeal(temp[i]);
				if(false == part[i]){
					return false;
					
				}
			}
//			if(!result){
//				result = false;
//			}
			
//			String part1 = temp[0];
//			boolean result1 = isCanDeal(part1);
//			String part2 = temp[1];
//			boolean result2 = isCanDeal(part2);
//			result = result1 && result2;

		}// ||（或）
		else if (test.contains(BTCLableAttributeValue.IF_COMPARE_HUO)) {
			String teststring = test.substring(2, test.length() - 1);
			String[] temp = teststring.replace(" ", "").split("\\|\\|");
			Boolean [] part = new Boolean[temp.length];
			result= false;
			for(int i = 0 ;i<temp.length;i++){
				part[i] = isCanDeal(temp[i]);
				if(part[i]){
					return true;
					
				}
			}
//			if(!result){
//				result = false;
//			}
//			String part1 = temp[0];
//			boolean result1 = isCanDeal(part1);
//			String part2 = temp[1];
//			boolean result2 = isCanDeal(part2);
//			result = result1 || result2;

		} else {
			String teststring = test.substring(2, test.length() - 1);
			result = isCanDeal(teststring);
		}
		
		return result;
	}
	
	/*
	 * / 得到if中的条件
	 */
	private boolean isCanDeal(String teststring) {
		boolean result = false;
		// 等于
		if (teststring.contains(BTCLableAttributeValue.IF_COMPARE_EQUAL)) {
			String[] temp = teststring
					.split(BTCLableAttributeValue.IF_COMPARE_EQUAL);
			String key = temp[0].trim();//SignResultInfo.ERROR fillAmount
			
			String value = temp[1];//Amount
			String optionValue = value.trim();
			if(optionValue.contains("'")){
				optionValue = optionValue.replace("'", "");
			}
			
			key = getValues(key);
			optionValue = getValues(optionValue);
			
			try {
				if(Double.parseDouble(optionValue)==Double.parseDouble(key)){
					 result = true;
				}
			} catch (Exception e) {
				if(optionValue.equals(key)){
					 result = true;
				}
//				return false;
			}
			
//			
//			String patterns = "^(\\d.\\d)|(\\d)$";
//			Pattern pattern = Pattern.compile(patterns);  
//	        Matcher matcher1 = pattern.matcher(optionValue);
//	        Matcher matcher2 = pattern.matcher(key);
//	        if(matcher1.matches()&& matcher2.matches()){  
//	        	 try {
//						if(Double.parseDouble(optionValue)==Double.parseDouble(key)){
//							 result = true;
//						}
//					} catch (Exception e) {
//						return false;
//					}
//	            
//	        }else{
//				 if(optionValue.equals(key)){
//					 result = true;
//				 }
//	        }
	        
	        
	        
			
//			if ((cmwApplication.hiddenboxmap.containsKey(optionValue)||cmwApplication.getVar(optionValue)!=null)
//					&&(cmwApplication.hiddenboxmap.containsKey(key)||cmwApplication.getVar(key)!=null)){
//				 if (cmwApplication.hiddenboxmap.containsKey(optionValue)){
//					 optionValue = (String) cmwApplication.hiddenboxmap.get(optionValue);
//				 }else if(cmwApplication.getVar(optionValue)!=null){
//					optionValue = cmwApplication.getVar(optionValue);
//				 }
//				 if (cmwApplication.hiddenboxmap.containsKey(key)){
//					 key = (String) cmwApplication.hiddenboxmap.get(key);
//				}else if(cmwApplication.getVar(key)!=null){
//					 key = cmwApplication.getVar(key);
//				 }
//				 
//
////				 String patterns = "^\\d.\\d";
////					Pattern pattern = Pattern.compile(patterns);  
////			        Matcher matcher1 = pattern.matcher(optionValue);
////			        Matcher matcher2 = pattern.matcher(key);
//			        if(matcher1.matches()&& matcher2.matches()){  
//			        	 try {
//								if(Double.parseDouble(optionValue.trim())==Double.parseDouble(key.trim())){
//									 result = true;
//								}
//							} catch (Exception e) {
//								return false;
//							}
//			            
//			        }else{
//						 if(optionValue.equals(key)){
//							 result = true;
//						 }
//			        }
//				
//			}
//			else{
//				if (optionValue.trim().equals(BTCCMWApplication.responsemap.get(key))) {
//					result = true;
//				}else if(BTCCMWApplication.ifMap.containsValue(key)){
//					String values = (String) BTCCMWApplication.ifMap.get("value");
//					if(optionValue.equals(values)){
//						result = true;
//					}
//					
//				} else if(cmwApplication.getVar(key)!=null){
//					if(cmwApplication.getVar(key).equals(optionValue)){
//						result = true;
//					}
//				} 
//				
//			}

		}
		// 不等于
		if (teststring.contains(BTCLableAttributeValue.IF_COMPARE_NOEQUAL)) {
			String[] temp = teststring
					.split(BTCLableAttributeValue.IF_COMPARE_NOEQUAL);
			String key = temp[0].trim();
			String value = temp[1];
			String optionValue = value.trim();
			if(optionValue.contains("'")){
				optionValue = optionValue.replace("'", "");
			}
			
			key = getValues(key);
			optionValue = getValues(optionValue);
			
			 try {
					if(Double.parseDouble(optionValue)!=Double.parseDouble(key)){
						 result = true;
					}
				} catch (Exception e) {
					if (!optionValue.equals(key)) {
						result = true;
					}
				}
			
			
//			String patterns = "^\\d.\\d$";
//			Pattern pattern = Pattern.compile(patterns);  
//	        Matcher matcher1 = pattern.matcher(optionValue);
//	        Matcher matcher2 = pattern.matcher(key);
//	        if(matcher1.matches()&& matcher2.matches()){  
//	        	 try {
//						if(Double.parseDouble(optionValue.trim())!=Double.parseDouble(key.trim())){
//							 result = true;
//						}
//					} catch (Exception e) {
//						return false;
//					}
//	            
//	        }else{
//	        	if (!optionValue.trim().equals(key)) {
//					result = true;
//				}
//	        }
	        
	        
			
//			String resValue = "";//北京移动通讯联通。radio中有value="";
//			if ((cmwApplication.hiddenboxmap.containsKey(optionValue.trim())||cmwApplication.getVar(optionValue)!=null)
//					&&(cmwApplication.hiddenboxmap.containsKey(key)||cmwApplication.getVar(key)!=null)){
//				 if (cmwApplication.hiddenboxmap.containsKey(optionValue)){
//					 optionValue = (String) cmwApplication.hiddenboxmap.get(optionValue);
//				 }else if(cmwApplication.getVar(optionValue)!=null){
//					optionValue = cmwApplication.getVar(optionValue);
//				 }
//				 if (cmwApplication.hiddenboxmap.containsKey(key)){
//					 key = (String) cmwApplication.hiddenboxmap.get(key);
//				}else if(cmwApplication.getVar(key)!=null){
//					 key = cmwApplication.getVar(key);
//				 }
//				 
//				 try {
//					if(Double.parseDouble(optionValue.trim())!=Double.parseDouble(key.trim())){
//						 result = true;
//					}
//				} catch (Exception e) {
//					return false;
//				}
//			}else {
//				if(BTCCMWApplication.responsemap.containsKey(key)){
//					resValue = BTCCMWApplication.responsemap.get(key).toString();
//					if("".equals(resValue)){
//						resValue = "null";
//					}
//				}else if(BTCCMWApplication.ifMap.containsValue(key))
//				{
//					resValue = BTCCMWApplication.ifMap.get("value").toString();
//				}
//				if (!optionValue.trim().equals(resValue)) {
//					result = true;
//				}
//			}
			

		}

		// 小于
		if (teststring.contains(BTCLableAttributeValue.IF_COMPARE_ONE)&&!teststring.contains("=")) {
			String[] temp = teststring
					.split(BTCLableAttributeValue.IF_COMPARE_ONE);
			String key = temp[0].trim();
			String value = temp[1];
			String optionValue =value.trim();
			if(optionValue.contains("'")){
				optionValue = optionValue.replace("'", "");
			}
			
			key = getValues(key);
			optionValue = getValues(optionValue);
			
			try{
//				if(BTCCMWApplication.responsemap.containsKey(key)){//tradeInfo.PAY_AMOUNT = -10.00
					if (Double.parseDouble(optionValue.trim())> Double.parseDouble(key)) {
						result = true;
					}		
					
//				}
				
			}
		     catch(Exception e){
		    	 return false;
		    	
		     }
			
			
//			String patterns = "^\\d.\\d$";
//			Pattern pattern = Pattern.compile(patterns);  
//	        Matcher matcher1 = pattern.matcher(optionValue);
//	        Matcher matcher2 = pattern.matcher(key);
//	        if(matcher1.matches()&& matcher2.matches()){  
//	        	try{
//					if(BTCCMWApplication.responsemap.containsKey(key)){//tradeInfo.PAY_AMOUNT = -10.00
//						if (Double.parseDouble(optionValue.trim())> Double.parseDouble(BTCCMWApplication.responsemap
//								.get(key).toString().trim())) {
//							result = true;
//						}		
//						
//					}
//					
//				}
//			     catch(Exception e){
//			    	 return false;
//			     }
//	        }else{
////	        	if (optionValue.toCharArray()>key.toCharArray()) {
////					result = true;
////				}
//	        }
			
//			try{
//				if(BTCCMWApplication.responsemap.containsKey(key)){//tradeInfo.PAY_AMOUNT = -10.00
//					if (Double.parseDouble(optionValue.trim())> Double.parseDouble(BTCCMWApplication.responsemap
//							.get(key).toString().trim())) {
//						result = true;
//					}		}
//				
//			}
//		     catch(Exception e){
//		    	 return false;
//		     }

		}
		// 大于
		if (teststring.contains(BTCLableAttributeValue.IF_COMPARE_TWO)&&!teststring.contains("=")) {
			String[] temp = teststring
					.split(BTCLableAttributeValue.IF_COMPARE_TWO);
			String key = temp[0].trim();
			String value = temp[1];
			String optionValue = value.trim();
			if(optionValue.contains("'")){
				optionValue = optionValue.replace("'", "");
			}
			
			key = getValues(key);
			optionValue = getValues(optionValue);
			
//			String patterns = "^\\d.\\d$";
//			Pattern pattern = Pattern.compile(patterns);  
//	        Matcher matcher1 = pattern.matcher(optionValue);
//	        Matcher matcher2 = pattern.matcher(key);
	        try{
//				if(BTCCMWApplication.responsemap.containsKey(key)){//tradeInfo.PAY_AMOUNT = -10.00
					if (Double.parseDouble(optionValue.trim()) < Double.parseDouble(key)) {
						result = true;
					}
//				}
				
			}
			catch(Exception e){
		    	 return false;
		     }
	        
//	        if(matcher1.matches()&& matcher2.matches()){  
//	        	try{
//					if(BTCCMWApplication.responsemap.containsKey(key)){//tradeInfo.PAY_AMOUNT = -10.00
//						if (Double.parseDouble(optionValue.trim()) < Double.parseDouble(BTCCMWApplication.responsemap
//								.get(key).toString().trim())) {
//							result = true;
//						}
//					}
//					
//				}
//				catch(Exception e){
//			    	 return false;
//			     }
//	            
//	        }else{
////	        	if (optionValue.trim()<key) {
////					result = true;
////				}
//	        }
	        
			
//			try{
//				if(BTCCMWApplication.responsemap.containsKey(key)){//tradeInfo.PAY_AMOUNT = -10.00
//					if (Double.parseDouble(optionValue.trim()) < Double.parseDouble(BTCCMWApplication.responsemap
//							.get(key).toString().trim())) {
//						result = true;
//					}
//				}
//				
//			}
//			catch(Exception e){
//		    	 return false;
//		     }

		}
		// 大于等于tradeInfo.PAY_AMOUNT >= 0
		if (teststring.contains(BTCLableAttributeValue.IF_COMPARE_THREE)&&!teststring.contains("<")) {
			String[] temp = teststring
					.split(BTCLableAttributeValue.IF_COMPARE_THREE);
			String key = temp[0].trim();
			String value = temp[1];
			String optionValue =value.trim();
			if(optionValue.contains("'")){
				optionValue = optionValue.replace("'", "");
			}
			
			key = getValues(key);
			optionValue = getValues(optionValue);
			
			try{
        		if (Double.parseDouble(optionValue) <=  Double.parseDouble(key)) {
					result = true;
				}
				
			}
			catch(Exception e){
		    	 return false;
		     }
			
//			String patterns = "^\\d.\\d$";
//			Pattern pattern = Pattern.compile(patterns);  
//	        Matcher matcher1 = pattern.matcher(optionValue);
//	        Matcher matcher2 = pattern.matcher(key);
//			
//	        if(matcher1.matches()&& matcher2.matches()){  
//	        	try{
//	        		if (Double.parseDouble(optionValue) <=  Double.parseDouble(key)) {
//						result = true;
//					}
//					
//				}
//				catch(Exception e){
//			    	 return false;
//			     }
//	            
//	        }else{
////	        	
//	        }
	        
//			if(BTCCMWApplication.responsemap.containsKey(key)){//tradeInfo.PAY_AMOUNT = -10.00
//				if (Integer.parseInt(optionValue.trim()) <=  Double.parseDouble(BTCCMWApplication.responsemap
//						.get(key).toString().trim())) {
//					result = true;
//				}
//			}
//			else if(BTCCMWApplication.ifMap.containsValue(key)){
//				String values = (String) BTCCMWApplication.ifMap.get("value");
//				if(Integer.parseInt(optionValue.trim())<= Double.parseDouble(values)){
//					result = true;
//				}
//				
//			}else if(cmwApplication.getVar(key)!=null){
//				if(Double.parseDouble(cmwApplication.getVar(key))<= Double.parseDouble(optionValue)){
//					result = true;
//				}
//			}
			
		}
		// 小于 等于
		if (teststring.contains(BTCLableAttributeValue.IF_COMPARE_FOUR)&&!teststring.contains(">")) {
			String[] temp = teststring
					.split(BTCLableAttributeValue.IF_COMPARE_FOUR);
			String key = temp[0].trim();
			String value = temp[1];
			String optionValue = value.trim();
			if(optionValue.contains("'")){
				optionValue = optionValue.replace("'", "");
			}
			key = getValues(key);
			optionValue = getValues(optionValue);
			
			try{
        		if (Double.parseDouble(optionValue) >= Double.parseDouble(key)) {
					result = true;
				}
				
			}
			catch(Exception e){
		    	 return false;
		     }
			
//			String patterns = "^\\d.\\d$";
//			Pattern pattern = Pattern.compile(patterns);  
//	        Matcher matcher1 = pattern.matcher(optionValue);
//	        Matcher matcher2 = pattern.matcher(key);
//			
//	        if(matcher1.matches()&& matcher2.matches()){  
//	        	try{
//	        		if (Double.parseDouble(optionValue) >= Double.parseDouble(key)) {
//						result = true;
//					}
//					
//				}
//				catch(Exception e){
//			    	 return false;
//			     }
//	            
//	        }else{
////	        	
//	        }
			
//			try{
//				if(BTCCMWApplication.responsemap.containsKey(key)){//tradeInfo.PAY_AMOUNT = -10.00
//					if (Double.parseDouble(optionValue.trim()) >= Double.parseDouble(BTCCMWApplication.responsemap
//							.get(key).toString().trim())) {
//						result = true;
//					}
//				}
//				
//			}
//			catch(Exception e){
//		    	 return false;
//		     }
		}
		return result;

	}

	
	private void drawLableByChild(final List<BTCElement> childElements,final View view, int i,final IExecuteFuction executeCallBack,final Map<String, String> dbMap) {
		
		
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		
		for (; i < childElements.size(); i++) {  
			childElement = childElements.get(i);
			btcDrawLable = childElement.getBTCDrawLable();
			if (btcDrawLable == null) 
				continue;
			if(btcDrawLable instanceof IAwaitExecute){
				final int k = i + 1;
				((IAwaitExecute)btcDrawLable).awaitExecute(dbMap, view,new IExecuteFuction(){
					@Override
					public void executeResultCallBack(Object param) {
						drawLableByChild(childElements,view,k,executeCallBack,dbMap);
						
					}
				});
				break;
			}
			btcDrawLable.drawLable(dbMap, view);
		}
		if(i >= childElements.size()){
			if(executeCallBack != null)
				executeCallBack.executeResultCallBack(true);
			return;
		}
	}
	
	private String getValues(String key){
		if(BTCCMWApplication.responsemap.containsKey(key)){
			key = BTCCMWApplication.responsemap.get(key).toString();
			if("".equals(key)){
				key = "null";
			}
		}else if(BTCCMWApplication.ifMap.containsValue(key))
		{
			key = BTCCMWApplication.ifMap.get("value").toString();
		}else if (cmwApplication.hiddenboxmap.containsKey(key)){
			 key = (String) cmwApplication.hiddenboxmap.get(key);
		 }else if(cmwApplication.getVar(key)!=null){
			key = cmwApplication.getVar(key);
		 }else{
			 key = key;
		 }
		return key;
	}


	@Override
	public boolean awaitExecute(Map<String, String> dbMap, View view,
			IExecuteFuction executeCallBack) {
		// 若不存在标签，或不存在判断属性，或者没有要显示的子标签内容，都不做处理
		if (btcElement == null || btcElement.getParams() == null
				|| btcElement.getChildElements() == null) {
			return true;
		}
		executeResult = executeResult();
		if (executeResult) {
			drawLableByChild(btcElement.getChildElements(), view, 0,executeCallBack,dbMap);
		}
		else if(executeCallBack != null){
			executeCallBack.executeResultCallBack(true);
		}
		return false;
	}

}
