package com.boc.bocsoft.remoteopenacc.common.regex;

/**
 * 	PESAPRegexResult regexResult = PESAPRegexUtils.check(getApplicationContext(), PESAPRegexUtils.STYLE_AMOUNT_15_2, "adfadf");
 if(!regexResult.isAvailable){
 //输入错误
 String error = regexResult.errorTips;
 //show tips
 }
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

/**
 * 参数校验工具类
 * 
 * @author dingeryue </br>2015-01-26
 * 
 */
public class RegexUtils {

	private final static String XML_PATH = "bocroa_regex.xml";
	private final static String ROOT_NAME = "regex";

	/**
	 * 身份证号
	 */
	public final static String STYLE_CERT_NO = "certNo";
	/**
	 * 姓名
	 */
	public final static String STYLE_NAME = "name";
	/**
	 * 他行卡号
	 */
	public final static String STYLE_OTHERBANKCARKNUM = "otherBankCarkNum";
	/**
	 * 拼音
	 */
	public final static String STYLE_NAMESPELL = "nameSpell";
	/**
	 * 拼音
	 */
	public final static String STYLE_NAMESPELLS = "nameSpells";
	/**
	 * 详细地址
	 */
	public final static String STYLE_DETAILADDRESS = "detailAddress";
	/**
	 * 邮编
	 */
	public final static String STYLE_POSTCODE = "postCode";
	/**
	 * 月收入
	 */
	public final static String STYLE_SALARY = "salary";
	/**
	 * 开户行查询
	 */
	public final static String STYLE_OPENBANKQUERY = "openBankQuery";
	/**
	 * 有效期
	 */
	public final static String STYLE_VALIDITY_START = "validityStart";
	public final static String STYLE_VALIDITY_END = "validityEnd";
	/**
	 * 所在区县
	 */
	public final static String STYLE_REGION = "region";
	/**
	 * 单位名称
	 */
	public final static String STYLE_COMPANY_NAME = "companyName";
	/**
	 * 手机号
	 */
	public final static String STYLE_TELEPHONE = "telephone";
	/**
	 * 短信验证码
	 */
	public final static String STYLE_MESSAGECODE = "messagecode";
	/**
	 * 姓氏
	 */
	public final static String STYLE_FAMILYNAME = "familyname";
	/**
	 * 名字
	 */
	public final static String STYLE_FIRSTNAME = "firstname";

	private static RegexUtils mRegexUtils;
	private HashMap<String, RegexItem> regexMaps;

	private RegexUtils() {
	};

	private static RegexUtils getInstance() {

		if (mRegexUtils == null) {
			mRegexUtils = new RegexUtils();
			mRegexUtils.loadData();
		}

		return mRegexUtils;
	}

	/**
	 * 获取正则校验类
	 * 
	 * @param reg
	 * @return
	 */
	private RegexItem getRegexItem(String reg) {
		return regexMaps.get(reg);
	}

	/**
	 * 进行校验
	 * 
	 * @param mContext
	 * @param type
	 *            校验类型 {@link #ITEM_AMOUNT_15_2 。。。}
	 * @param input
	 *            输入
	 * @param isMust
	 *            是否必须输入 不能为空
	 * @return
	 */
	public static RegexResult check(Context mContext, String type,
			String input, boolean isMust) {
		// TODO 这里不做异常控制 方便开发时发现错误！！！
		return getInstance().getRegexItem(type).check(mContext, input, isMust);
	}

	/**
	 * 解析xml数据
	 * 
	 * @param parser
	 */
	private void parseData(XmlPullParser parser) {
		try {
			int eventType = parser.getEventType();
			for (; XmlPullParser.END_DOCUMENT != eventType;) {
				String name = parser.getName();
				if (ROOT_NAME.equals(name)
						&& eventType == XmlPullParser.START_TAG) {
					regexMaps = new HashMap<String, RegexItem>();
				} else if (RegexItem.X_ITEM_ROOT_NAME.equals(name)) {
					// 交给model解析
					RegexItem item = new RegexItem();
					item.parse(parser);
					regexMaps.put(item.getStyle(), item);
				}

				parser.next();
				eventType = parser.getEventType();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadData() {
		XmlPullParser parser = Xml.newPullParser();
		InputStream ins = null;
		try {
			ins = getClass().getClassLoader().getResourceAsStream(XML_PATH);
			parser.setInput(ins, "utf-8");
			parseData(parser);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		}

	}

}
