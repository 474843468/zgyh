package com.chinamworld.bocmbci.biz.peopleservice.xmlinterface;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCUiData;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCGlobal;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;
/**
 * 类功能描述：1、实现对xml页面的解析方法。 2、登录验证发送http请求以及结果页面的解析。
 * 该类主要实现对xml的解析。并且，发送http请求的接口也是在这个类中调用的。
 * 
 * @author：zld
 * @version：1.0
 * @see 包名：com.chinamworld.klb.btw.xmlinterface
 */
public class BTCKxmlParser {

	/**
	 * 方法功能说明：该方法是用于解析xml文件。 从wap服务端返回的xml文件字符流在这个方法中进行解析，
	 * xml中的每一个标签以及其中的属性和子标签都被封装到一个实体类CcbElement的对象当中，
	 * 并把这些实体对象依次添加到一个列表中，再把这个列表返回给控制中心。
	 * 
	 * @param xml
	 *            -从服务端返回来的数据的xml文件字符流
	 * @return List<BTCElement>-该列表中包含有需要在页面上进行展示的标签对象
	 * @see BTCControlCenter#getView（）
	 */
	public static BTCUiData parseXml(Context context,String xml) throws Exception {
		// 把字符串转化为流，以便进行解析
		InputStream inStream = null;
		String s = charParse(xml);
		int index = s.indexOf("<?xml");
		if (index > -1) {
			s = s.substring(index);
		}
		if (!"".equals(s) && s != null) {
			inStream = new ByteArrayInputStream(s.getBytes(BTCGlobal.DEFAULT_ENCORD));
		} else {
			return null;
		}
		// 节点对象列表
		Element root = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inStream).getDocumentElement();
		inStream.close();
		// 由根节点<wml>开始解析，并且在方法parseElement内递归进行子标签解析。
		List<BTCElement> ccbElements = new ArrayList<BTCElement>();
		BTCElement element =parseElement(context,root);
		ccbElements.add(element);
		return new BTCUiData(element,ccbElements, null, null, xml);
	}

	/** 保存需要转义的字符字段 **/
	public static final String[] XMLCHARS = { "&lt;", "&gt;", "&quot;", "&apos;", "&amp;" }; // 这五个不做处理
	public static final String[] CHARSFROM = { "&#60;", "&#62;", "&#34;", "&#39;", "&nbsp;" }; // 需要处理的转义字符
	public static final String[] CHARSTO = { "&lt;", "&gt;", "&quot;", "&apos;", " " };

	/**
	 * 对xml字符串中出现的转义字符进行替换
	 * 
	 * @param xml
	 * @return 特殊处理之后的页面字符串
	 */
	public static String charParse(String xml) {
		StringBuffer sb = new StringBuffer(xml);
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < CHARSFROM.length; i++) {
			map.put(CHARSFROM[i], CHARSTO[i]);
		}
		int start = -1; // 记录下次开始查找的位置
		int length = 0;
		while ((start = sb.indexOf("&", start + 1)) != -1) {
			if (isXmlChars(sb, start)) {
				// 匹配&lt; &gt; &quot; &apos; &amp;不做处理
			} else if (isAscOrHtmlChars(sb, start)) {
				// 匹配 &#60; &#62; &#34; &#39; &nbsp;--> &lt; &gt; &quot; &apos;
				// " "
				length = des.length();
				sb.replace(start, start + length, map.get(des));
			} else {
				// & --> &amp;
				sb.replace(start, start + 1, "&amp;");
			}
		}
		return sb.toString();
	}

	static String des; // 临时保存需要转换的字符段

	public static boolean isXmlChars(StringBuffer sb, int start) {
		for (int i = 0; i < XMLCHARS.length; i++) {
			if (sb.substring(start).length() >= XMLCHARS[i].length()) {
				if (XMLCHARS[i].equals(sb.substring(start, start + XMLCHARS[i].length()))) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isAscOrHtmlChars(StringBuffer sb, int start) {
		for (int i = 0; i < CHARSFROM.length; i++) {
			if (sb.substring(start).length() >= CHARSFROM[i].length()) {
				if (CHARSFROM[i].equals(sb.substring(start, start + CHARSFROM[i].length()))) {
					des = CHARSFROM[i];
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 方法功能说明：解析标签中的节点
	 * 
	 * @param element
	 *            -xml文件中的节点元素
	 * @return BTCElement-把节点元素中的相关信息封装到该对象中
	 * @see BTCXmlParse#parseXml（）
	 */
	public static BTCElement parseElement(Context context,Element element) {
		// 节点对象
		BTCElement ccbElement = new BTCElement();
		// 节点属性列表
		Map<String, String> params = new HashMap<String, String>();
		// 子节点列表
	//	List<BTCElement> childElements = new ArrayList<BTCElement>();

		// 取得元素节点的名字，并把这个名字设置到实体对象中
		ccbElement.setElementName(element.getTagName().toLowerCase());
		// 循环处理节点属性
		NamedNodeMap nnm = element.getAttributes();
		int attr_count = nnm.getLength();
		for (int i = 0; i < attr_count; i++)
			params.put(nnm.item(i).getNodeName().toLowerCase(), nnm.item(i).getNodeValue());
		// 循环处理子节点
		NodeList nl = element.getChildNodes();
		Node n;
		String text = null;
		for (int i = 0; i < nl.getLength(); i++) {
			n = nl.item(i);
			switch (n.getNodeType()) {
			case Node.TEXT_NODE:
				text = n.getNodeValue().trim();
				if (text.length() > 0)
					ccbElement.addChildElement(new BTCElement(context,BTCLable.TEXTNODE, null, text));
				break;
			case Node.ELEMENT_NODE:
				ccbElement.addChildElement(parseElement(context,(Element) n));
				break;
			}
		}
		// 设置节点对象属性列表及子节点列表
		ccbElement.setParams(params);
	//	ccbElement.setChildElements(childElements);
		ccbElement.setBtcDrawLable(context, element.getTagName().toLowerCase());
		// 返回节点对象
		// return new BTCElement(context,element.getTagName().toLowerCase(),params,text,childElements);
		return ccbElement;
	}

}
