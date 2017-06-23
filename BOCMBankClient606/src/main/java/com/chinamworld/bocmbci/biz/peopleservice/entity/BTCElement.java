package com.chinamworld.bocmbci.biz.peopleservice.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.chinamworld.bocmbci.biz.peopleservice.ui.BTCDrawLable;
import com.chinamworld.bocmbci.biz.peopleservice.ui.BTCLableFactory;

/** 
*类功能描述：该实体类是用于封装标签的相关信息
*@author：秦
*@version：1.0
*@see 包名：com.chinamworld.btwapview.domain
*相关数据：
*		elementName 标签名
*		params 标签中的属性列表
*		childElements 标签的子标签列表
*		text 如果该节点为文本节点，就会把文本节点中的文本内容赋予这个字段
*/
public class BTCElement {
	/** 父节点 */
	private BTCElement parentElement = null;
	public BTCElement getParentElement(){
		return parentElement;
	}

	/** 标签的上一元素 */
    protected BTCElement prevElement = null;

    /** 标签的下一元素 */
    protected BTCElement nextElement = null;
	
	/**标签名*/
	private String elementName;//标签名
	/**属性列表*/
	private Map<String, String> params;//属性列表
	/**文本节点的文本*/
	private String text;//文本节点的文本
	/**子标签列表*/
	public List<BTCElement> childElements = new ArrayList<BTCElement>();//子标签列表
	
	/** 当前节点的 具体执行类  */
	private BTCDrawLable btcDrawLable;
	
	/** 获得当前节点的实际执行类 */
	public BTCDrawLable getBTCDrawLable(){
		return btcDrawLable;
	}
	
	/**
	 * 带参构造函数，实例化变量
	 * @param elementName
	 * @param params
	 * @param text
	 * @param childElements
	 */
	public BTCElement(Context context,String elementName, Map<String, String> params,
			String text) {
		
		this.elementName = elementName;
		this.params = params;
		this.text = text;
		btcDrawLable = createBTCDrawLable(context);
		btcDrawLable.SetBTCElement(this);
	}
	
	public void setBtcDrawLable(Context context,String elementName){
		this.elementName = elementName;
		btcDrawLable = createBTCDrawLable(context);
		btcDrawLable.SetBTCElement(this);
	}
	
	public BTCElement(){
		
	}
	private BTCDrawLable createBTCDrawLable(Context context){
		return BTCLableFactory.createDrawLableInstance(context, this);

	}
	
	
	/** 标签的Name属性，一般也作为标签的唯一标识  */
	public String getName(String key){
		if(params == null)
			return null;
		key = key == null  ? "name" : key;
		if(params.containsKey(key)) {
			return params.get(key);
		}
		return null;
			
	}
	
	/**
	 *  标签类型名  set/get方法对
	 */
	public String getElementName() {
		return elementName;
	}
	
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Map<String, String> getParams() {
		return params;
	}
	
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	public List<BTCElement> getChildElements() {
		if(btcDrawLable == null)
			return new ArrayList<BTCElement>();
		return btcDrawLable.getChildElements();
		//return childElements;
	}
	
	/** 添加子标签 */
	public void addChildElement(BTCElement element){
		
		 if (element == null)
             return;
         if (childElements.size() > 0)
         {
             element.prevElement = childElements.get(childElements.size() - 1);
             childElements.get(childElements.size() -1).nextElement = element;             
         }
         element.parentElement = this;
		childElements.add(element);
	}
	
	/**
	 * 返回节点的所有信息
	 */
	@Override
	public String toString() {
		return "type="+ elementName +"\n\tparams="+ params +"\n\tchildElements"+childElements+"\n\t"+"text="+text;
	}
}
