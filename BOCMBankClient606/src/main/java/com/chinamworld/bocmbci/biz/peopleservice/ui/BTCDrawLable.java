package com.chinamworld.bocmbci.biz.peopleservice.ui;

import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.biz.login.LoginActivity;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IExecuteFuction;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *类功能描述：标签类的超类
 * @author：秦
 *@version： 1.0
 *@see 包名：com.chinamworld.btwapview.ui
 *	   父类：com.chinamworld.btwapview.ui.BTCDrawLable
 *	   相关数据：
 */
public class BTCDrawLable {
	
	public enum ElementType
	{
		None,
		Menu,	
		Item,
		Flow,
		Step,
		Action,
		Comm,
		Security,
		Request,
		Response,
		Datalist,
		Param,
		View,
		Hiddenbox,
		Panel,
		Pagetip,
		Newline,
		Table,
		Row,
		Cell,
		Pagination,
		Pageitem,
		Page,
		Button,
		Font,
		Actcombo,
		Actuse,
		Textbox,
		Combo,
		Option,
		Radio,
		Dateinput,
		Link,
		Space,
		Use,
		Translate,
		Openwindow,
		Loop,
		If,
		Checkbox,
		Optvalue,
		Registerdb,
		Dynamictextbox,
		Formatdate,
		Formatnumber,
	}
	
	protected ElementType elementType = ElementType.None;
			
	/** 当前标签对应的节点  */
	protected BTCElement btcElement;
	/** 上下文对象 */
	protected Context context;// 上下文对象
	
	protected View elementView = null;
	
	/** 画子标签时，需要组装的数据  */
	protected Map<String,String> childParamMap = null;
	
	public BTCDrawLable(Context context,BTCElement element){
		this.context = context;
		btcElement = element;
	}
	
	public void SetBTCElement(BTCElement element){
		btcElement = element;
	}
	
	/** 画子标签时，需要组装的数据  */
	public Map<String,String> getChildParamMap(){
		return childParamMap;
	}
	
	
	public List<BTCElement> getChildElements() {
		return btcElement.childElements;  //此处不能调用对应的Get方法，因为其Get方法来是调用此类的Get方法。此Get方法将会在If中被重写
	}

	/**
	 * 方法功能说明 ：用于画标签
	 * @param btcElement 需要处理的标签的实体对象
	 * 		  dbMap 数据库查询结果集的一条记录的数据信息
	 * 		  view 父容器
	 * 返回false 则表示中断执行
	 * @return void
	 */	
	public Object drawLable(Map<String, String> dbMap, View view){
		BTCDrawLable btcDrawLable;

		for(BTCElement childElement : btcElement.getChildElements()){
			btcDrawLable = childElement.getBTCDrawLable();
			if(btcDrawLable == null)
				continue;
			Object res = btcDrawLable.drawLable(null, view);
			try{
				if(Boolean.getBoolean(res.toString()) == false)
					return false;
			}
			catch (Exception e){
				return true;
			}
		}
		return true;
	}
	
	/** 执行标签 */
	public boolean awaitExecute(Map<String, String> dbMap, View view,
			IExecuteFuction executeCallBack) {
	
		try{
			if(Boolean.parseBoolean(drawLable(dbMap,view).toString()) == false)
				return false;
		}
		catch (Exception e){
		}
		executeChildren(view,0,executeCallBack);
		return true;
	}

	private boolean executeChildren(final View view, int i, final IExecuteFuction executeCallBack){
		boolean flag = true;
		for (; i < btcElement.getChildElements().size(); i++) {  
			flag = false;
			BTCDrawLable btcDrawLable = btcElement.getChildElements().get(i).getBTCDrawLable();
			if (btcDrawLable == null) 
				continue;
			final int k = i + 1;
			btcDrawLable.awaitExecute(getChildParamMap(), view, new IExecuteFuction(){
				@Override
				public void executeResultCallBack(Object param) {
					// TODO Auto-generated method stub
					executeChildren(view,k,executeCallBack);
				}}
			);
			break;
		}
		
		if(flag &&  executeCallBack != null) {
			executeCallBack.executeResultCallBack(true);
		}
		return true;
	}
	
	
	/** 通过标签名，找到对应的标签 */
 	protected BTCElement FindElementBy(String name){
 		
		BTCElement root = BTCActivityManager.getInstance().getPageData().getRootElement();
        return FindElementBy(root,name);
	}
	
	private BTCElement FindElementBy(BTCElement element,String name){
		String elementName = element.getName(null);
		if(elementName!= null && elementName.equals(name))
			return element;
		BTCElement el = null;
		if(element.getChildElements() == null)
			return null;
		for(BTCElement item: element.getChildElements()){
			el = FindElementBy(item,name) ;
			if(el != null) return el;
		}
		return null;
	}
	
	/** 按条件查找最近符合条件的记录 */
	public  BTCElement findNearElemntBy(IFunction compare)
	{
		BTCElement element = findElementToChildren(this.btcElement,compare);
		if(element != null)
			return element;
		return  findElementToParent(this.btcElement,compare);
	}
	
  /** 在指定的标签内部寻找符合条件的标签  */
	protected BTCElement findElementToChildren(BTCElement element,IFunction compare){
		if(compare.func(element))
			return element;
		BTCElement bt = null;
		
		for(BTCElement e : element.getChildElements()){
			bt = findElementToChildren(e,compare);
			if(bt != null)
				return bt;
		}
		return null;
	}
	
	/** 向上遍历，寻找符合条件的 */
	private BTCElement findElementToParent(BTCElement element,IFunction compare){
		if(compare.func(element))
			return element;
		BTCElement bt = null;
		while(element.getParentElement() != null){
			if(compare.func(element.getParentElement()))
				return element.getParentElement();
			for(BTCElement e : element.getParentElement().getChildElements()){
				if(e.equals(element) ){
					continue;
				}
				bt = findElementToChildren(e,compare);
				if(bt != null)
					return bt;
			}
			element = element.getParentElement();
		}
	
		
		return null;
	}

    /// <summary>
    /// 寻找根节点
    /// </summary>
    protected BTCElement FindRootElement()
    {
    	BTCElement rootElmenent = this.btcElement;
        while (rootElmenent.getParentElement() != null)
        {
            rootElmenent = rootElmenent.getParentElement();
        }
        return rootElmenent;
    }

	/// <summary>
    /// 按条件搜索目录中的标签集合
    /// </summary>
    protected List<BTCElement> FindElementByCondition(IFunction compare)
    {
        List<BTCElement> list = new ArrayList<BTCElement>();
        BTCElement root = FindRootElement();
        list = FindElementBy(null, root, compare);
        return list;
    }

    /// <summary>
    /// 从当前标签中寻找符合条件的记录
    /// </summary>
    protected List<BTCElement> FindElementBy(List<BTCElement> list, BTCElement root, IFunction conditionFunc)
    {
    	if(list == null)
    		list = new ArrayList<BTCElement>();
        if (conditionFunc.func(root) == true)
        {
            list.add(root);
        }
        for (BTCElement children : root.getChildElements())
        {
            FindElementBy(list, children, conditionFunc);
        }
        return list;
    }

    
    /** 通过Key取对应的Value值 */
    protected String getValue(String key) {
    	
    	String value = null;
    	if(BTCCMWApplication.responsemap.containsKey(key)&&BTCCMWApplication.responsemap.get(key).toString()!=null )
    		value = BTCCMWApplication.responsemap.get(key).toString();
    	else if(BTCCMWApplication.hiddenboxmap.containsKey(key)&&BTCCMWApplication.hiddenboxmap.get(key).toString()!=null &&!"".equals(BTCCMWApplication.hiddenboxmap.get(key).toString()))
    		value =BTCCMWApplication.hiddenboxmap.get(key).toString();
		else if(BTCCMWApplication.flowFileLangMap.containsKey(key)&&BTCCMWApplication.flowFileLangMap.get(key)!=null ){
			value =BTCCMWApplication.flowFileLangMap.get(key).toString();
		}else if(BTCCMWApplication.requestParamsMap.containsKey(key)&&null!=BTCCMWApplication.requestParamsMap.get(key) ){
			value =BTCCMWApplication.requestParamsMap.get(key).toString();
		}
		else if("_CIFNUMBER".equals(key)){
			if(LoginActivity.cifNumber!=null){
				value = LoginActivity.cifNumber;
			}
		}
//		else if(BTCCMWApplication.resultmap.containsKey(key)&&BTCCMWApplication.resultmap.get(key)!=null){
//			//网络请求回来的数据
//			value = BTCCMWApplication.resultmap.get(key).toString();
//		}
		else if(BTCCMWApplication.getRadioRequest.containsKey(key)&&BTCCMWApplication.getRadioRequest.get(key)!=null){//在loop中有radio的情况
			value = BTCCMWApplication.getRadioRequest.get(key).toString();
		}else if(key.equals("currentIndex")){
			value = BTCCMWApplication.totalNumber+"";
		}else if(BTCCMWApplication.linkParamsMap.containsKey(key) &&BTCCMWApplication.linkParamsMap.get(key)!=null){
			value = BTCCMWApplication.linkParamsMap.get(key).toString();
		}
    	return value;
    }
}
