package com.chinamworld.bocmbci.biz.peopleservice.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;

import com.chinamworld.bocmbci.biz.peopleservice.ui.BTCUiActivity;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 类功能描述：管理全局变量(也就是由于setvar标签产生的全局变量)
 * 
 * @author：秦
 * @version： 1.0
 * @see 包名：com.chinamworld.klb.btw.domain 父类：android.app.Application 相关数据：
 */
public class BTCCMWApplication extends Application {

	public BTCCMWApplication(){
		clearData();
	}
	
	public void clearData(){
		requestMap = new HashMap<String, String>();
		Response = new HashMap<String, String>();
		getRadioRequest  = new HashMap<String, String>();
		ListAccountMap = new ArrayList<HashMap<String,String>>();
		checklist = new ArrayList<String>();
		BTCCMWApplication.clearVars();
		ListAccountMap.clear();
		linkParamsMap.clear();
		requestParamsMap.clear();
		headerList.clear();
		acctype.clear();
		boungtragetValue.clear();
		ifMap.clear();
		if(factorList != null)
			factorList.clear();
		optionMap.clear();
		Response.clear();
		if(security != null)
			security.clear();
		resultmap.clear();
		getRadioRequest.clear();
		stepmap.clear();
		folwmap.clear();
		responsemap.clear();
		hiddenboxmap.clear();
		combomap.clear();
		checkboxlist.clear();
		radioMap.clear();
		listradio.clear();
		listDynamictextbox.clear();
		inputCheckbox.clear();
		loginMap.clear();
		cachePageMap.clear();
		cachePageNumber.clear();
		flowFileLangMap.clear();
		optionValue.clear();
		checklist.clear();
		requestMap.clear();
	}
	
	
	
	public static BTCUiActivity selfs ;
	public static boolean flag = false;
	
	
	public static int currentIndex = 0;
	public static int totalNumber =0;//记录pageination 中indexcurent数
	public static String stepName = "";
	public static boolean isContainSecurityInit = false;
	public static boolean isContainSecurityActive = false;
	public static UsbKeyText usbKeyText = null;
	/** 是否需要音频Key确认  */
	public static boolean isAudioKeySign = false;
	//标签里含有link标时，会用到，记录用户选定的哪一项进入到link里面去
//	public static List<Map<String, String>> loopList = new ArrayList<Map<String,String>>();
	public static List<HashMap<String, String>> ListAccountMap = new ArrayList<HashMap<String,String>>();
	//link里面用于请求的参数
	public static Map<String, String> linkParamsMap = new HashMap<String, String>();
	public static Map<String, String> requestParamsMap = new HashMap<String, String>();
	public static ArrayList<String> headerList = new ArrayList<String>(); //保存table 中的header
	public static List<String> acctype = new ArrayList<String>();
	public static String securityRandom = "";//安全因子随机数
	public static Map<String, String> boungtragetValue = new HashMap<String, String>();//bundflag里面的值。
	public static Map<String, Object> ifMap = new HashMap<String, Object>();//if标签请求中用到的
	public static List<HashMap<String, Object>> factorList = new ArrayList<HashMap<String,Object>>();//请求回来的安全因子控件列表。
	public static String _plainData = null;
	public static String _certDN = null;
//	public static List<BTCElement> stepList = new ArrayList<BTCElement>();
	public static Map<String, String> optionMap = new HashMap<String, String>();//loop标签中value1显示的值,value是当选重 显示的值后，上送的值。
	public static Map<String, String> Response = new HashMap<String, String>();
	public static Map<String, String> security = new HashMap<String, String>();
	public static Map<String, Object> resultmap = new HashMap<String, Object>();
	public static Map<String, String> optionValue = new HashMap<String, String>();//currentValue
	/**getDepartments**/
//	public static Map<String ,Object> getDepartmentsAll = new HashMap<String, Object>();
//	
	public static Map<String, String> requestMap = new HashMap<String, String>();
	public static Map<String ,String> getRadioRequest = new HashMap<String, String>();
	
	public static Map<String, Object> dispMap = new HashMap<String, Object>();//继续缴费的时候用到。在step完之后要清空。
	public static String isDisp="";
	public static String conversationId ;
	public static String conversationIdLang ;
	public static String flowFileId; 

	/** 记录step */
	private static Map<String, BTCElement> stepmap = new HashMap<String, BTCElement>(); 		
	/**  记录flow */
	private static Map<String, BTCElement> folwmap = new HashMap<String, BTCElement>(); 		
	
	
	public static Map<String, Object> responsemap= new HashMap<String, Object>(); 		
	//生产bug 加载更多
	public static int paginationCount = 0;
	
	/**  记录Hiddenbox */
	public static Map<String, Object> hiddenboxmap = new HashMap<String, Object>(); 		
	/**  记录combo */
	public static Map<String, Map<String,String>> combomap = new HashMap<String,  Map<String,String>>(); 		
	/**  记录checkbox 选中项的值 */
	public static Map<String, List<String>>checkboxlist= new HashMap<String, List<String>>();
	/** 由setvar标签生成的全局变量 */
	private static Map<String, String> map = new HashMap<String, String>(); // 由setvar标签生成的全局变量
	/** 由setvar标签生成的全局变量 */
	private Map<String, String> radioMap = new HashMap<String, String>(); // 由setvar标签生成的全局变量
	public static  List< String> checklist = new ArrayList<String>(); //记录有checkbox标签  
	public static  List< String> listradio = new ArrayList<String>(); //记录radio 
	public static  List< String> listDynamictextbox = new ArrayList<String>(); // Dynamictextbox id
	public static Map<String, String> dynamictextboxmap=new HashMap<String, String>();// 记录 Dynamictextbox 中的id
	/**  记录Dynamictextbox 中项的值 */
	public static List< Map<String,String>>dynamictextboxlist= new ArrayList<Map<String,String>>();	
	private Map<String, ArrayList<String>> inputCheckbox = new HashMap<String, ArrayList<String>>();
	/** 登录时的一些信息 */
	private Map<String, String> loginMap = new HashMap<String, String>(); // 登录时的一些信息

	private Map<String, List<BTCUiData>> cachePageMap = new HashMap<String, List<BTCUiData>>();
	private Map<String, String> cachePageNumber = new HashMap<String, String>();
	public static String results = new String();
	private boolean refreshFlag = true;
	private boolean stopHttpFlag = false;
	private static String cookie;
	private static String b2ccookie;
	public static String contentType;
	
	
	public static String boundflag=null;
	private static  String flowElements;
	public static  String getFlowElement() {
		return flowElements;
	}

	public static void setFlowElement(String flowElement) {
		flowElements = flowElement;
	}
	public static String getBoundflag() {
		return boundflag;
	}

	public static void setBoundflag(String boundflag) {
		BTCCMWApplication.boundflag = boundflag;
	}

	public static Map<String, BTCElement> getStepmap() {
		return stepmap;
	}

	public static void setStepmap(Map<String, BTCElement> stepmap) {
		BTCCMWApplication.stepmap = stepmap;
	}

	public static Map<String, BTCElement> getFolwmap() {
		return folwmap;
	}

	public static void setFolwmap(Map<String, BTCElement> folwmap) {
		BTCCMWApplication.folwmap = folwmap;
	}
	
	


	public boolean isRefreshFlag() {
		return refreshFlag;
	}

	public void setRefreshFlag(boolean refreshFlag) {
		this.refreshFlag = refreshFlag;
	}

	public boolean isStopHttpFlag() {
		return stopHttpFlag;
	}

	public void setStopHttpFlag(boolean stopHttpFlag) {
		this.stopHttpFlag = stopHttpFlag;
	}

	public Map<String, ArrayList<String>> getInputCheckbox() {
		return inputCheckbox;
	}

	public ArrayList<String> getInputCheckboxValues(String key) {
		return inputCheckbox.get(key);
	}

	public void setInputCheckbox(String name, String value) {
		ArrayList<String> checkboxList = inputCheckbox.get(name);
		if (checkboxList == null) {
			checkboxList = new ArrayList<String>();
		}
		if (!checkboxList.contains(value)) {
			checkboxList.add(value);
		}
		inputCheckbox.put(name, checkboxList);
	}

	public void setInputCheckboxIndex(String name, String value) {
		ArrayList<String> checkboxList = inputCheckbox.get(name);
		if (checkboxList == null) {
			checkboxList = new ArrayList<String>();
		} else {
			checkboxList.remove(0);
		}
		checkboxList.add(0, value);
		inputCheckbox.put(name, checkboxList);
	}

	public void removeCheckboxRecord(String name, String value) {
		ArrayList<String> checkboxList = inputCheckbox.get(name);
		if (checkboxList == null) {
			checkboxList = new ArrayList<String>();
		} else {
			if (checkboxList.contains(value)) {
				checkboxList.remove(value);
			}
		}
		inputCheckbox.put(name, checkboxList);
	}

	public void removeCheckboxByName(String name) {
		inputCheckbox.remove(name);
	}

	public void setInputCheckbox(Map<String, ArrayList<String>> inputCheckbox) {
		this.inputCheckbox = inputCheckbox;
	}

	public void clearCheckBox() {
		if (inputCheckbox != null) {
			inputCheckbox = null;
			inputCheckbox = new HashMap<String, ArrayList<String>>();
		}
	}

	public static String getCookie() {
		return cookie;
	}

	public static void setCookie(String c) {
		cookie = c;
	}

	public void addCachePage(String pageId, BTCUiData page) {
		List<BTCUiData> pageList = cachePageMap.get(pageId);
		if (pageList == null) {
			pageList = new ArrayList<BTCUiData>();
			cachePageNumber.put(pageId, "0");
			pageList.add(0, page);
		} else {
			int curNum = Integer.parseInt(cachePageNumber.get(pageId));
			String curNum_2 = String.valueOf(curNum + 1);
			cachePageNumber.put(pageId, curNum_2);
			pageList.add(curNum + 1, page);
		}
		cachePageMap.put(pageId, pageList);
	}

	public BTCUiData getPrevPage(String pageId) {
		BTCUiData page = null;
		List<BTCUiData> pageList = cachePageMap.get(pageId);
		int currentNum = Integer.parseInt(cachePageNumber.get(pageId));
		if (pageList != null && currentNum > 0) {
			page = pageList.get(currentNum - 1);
			String curNum_2 = String.valueOf(currentNum - 1);
			cachePageNumber.put(pageId, curNum_2);
		}

		return page;
	}

	public BTCUiData getNextPage(String pageId) {
		BTCUiData page = null;
		List<BTCUiData> pageList = cachePageMap.get(pageId);
		int currentNum = Integer.parseInt(cachePageNumber.get(pageId));
		if (pageList != null && currentNum + 1 < pageList.size()) {
			page = pageList.get(currentNum + 1);
			String curNum_2 = String.valueOf(currentNum + 1);
			cachePageNumber.put(pageId, curNum_2);
		}
		return page;
	}

	public void deletePagesById(String pageId) {
		cachePageMap.remove(pageId);
		cachePageNumber.remove(pageId);
	}

	public void deletAllCachePages() {
		cachePageMap.clear();
		cachePageNumber.clear();
	}

	public String getCurPageNumById(String pageId) {
		return cachePageNumber.get(pageId);
	}

	/**
	 * 保存登录时的相关变量
	 * 
	 * @param key
	 *            -变量
	 * @param value
	 *            -变量值
	 */
	public void setLoginMsg(String key, String value) {
		loginMap.put(key, value);
	}

	/**
	 * 返回登录保存的所有变量
	 * 
	 * @return
	 */
	public Map<String, String> getLoginMap() {
		return loginMap;
	}

	/**
	 * 根据变量名取的登录保存的变量值
	 * 
	 * @param key
	 * @return
	 */
	public String getLoginMsg(String key) {
		return loginMap.get(key);
	}

	/**
	 * 取得所有setvar的变量数据
	 * 
	 * @return
	 */
	public Map<String, String> getMap() {
		return map;
	}

	/**
	 * 保存所有setvar的数据
	 * 
	 * @param map
	 */
	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	/**
	 * 保存setvar变量
	 * 
	 * @param key
	 * @param value
	 */
	public void setVar(String key, String value) {
		map.put(key, value);
	}

	/**
	 * 根据名字取得setvar变量
	 * 
	 * @param key
	 * @return
	 */
	public String getVar(String key) {
		return map.get(key);
	}

	public void removeVar(String key){
		if(map.containsKey(key))
			map.remove(key);
	}
	
	/**
	 * 清空setvar保存的变量
	 */
	public static void clearVars() {
		map.clear();
	}

	/**
	 * 清空除指定保存之外的变量
	 */
	public void clearVarsExceptSave(String[] saves) {
		Map<String, String> maptemp = new HashMap<String, String>();
		for (int i = 0; i < saves.length; i++) {
			maptemp.put(saves[i], map.get(saves[i]));
		}
		map.clear();
		map = maptemp;
	}

	/**
	 * 得到所有的radio的值
	 * 
	 * @return
	 */
	public Map<String, String> getRadioMap() {
		return radioMap;
	}

	/**
	 * 保存所有radio的数据
	 * 
	 * @param map
	 */
	public void setRadioMap(Map<String, String> map) {
		this.radioMap = map;
	}



	
	/**
	 * 保存radio变量
	 * 
	 * @param key
	 * @param value
	 */
	public void setRadioVar(String key, String value) {
		radioMap.put(key, value);
	}

	/**
	 * 根据名字取得radio变量
	 * 
	 * @param key
	 * @return
	 */
	public String getRadioVar(String key) {
		return radioMap.get(key);
	}

	/**
	 * 清空radio保存的变量
	 */
	public void clearRadioVars() {
		radioMap.clear();
	}

	public static String getB2ccookie() {
		return b2ccookie;
	}

	public static void setB2ccookie(String b2ccookie) {
		BTCCMWApplication.b2ccookie = b2ccookie;
	}

	public static String getContentType() {
		return contentType;
	}

	public static void setContentType(String contentType) {
		BTCCMWApplication.contentType = contentType;
	}
	
	
	
	/** 字典 */
	public static  Map<String, Object> flowFileLangMap= new HashMap<String, Object>(); 		
	
	/** 设置国际滑字典  */
	public void setFlowFileLangMap(Map<String, Object> map){
		flowFileLangMap = map;
	}
	
	/** 流程文件中是否包含key  */
	public boolean containKeyFromFlowFileLangMap(String key){
		return flowFileLangMap.containsKey(key);
	}
	
	/** 通过Key获得国际滑字典中的值 */
	public String getFlowFileLangMapByKey(String key){
		if(flowFileLangMap.containsKey(key)){
			return flowFileLangMap.get(key).toString();
		}
		return null;
	}
	
	
}
