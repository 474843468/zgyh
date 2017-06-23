package com.boc.bocsoft.mobile.framework.utils.regex;



/**
 * 	PESAPRegexResult regexResult = PESAPRegexUtils.check(getApplicationContext(), PESAPRegexUtils.STYLE_AMOUNT_15_2, "adfadf");
 if(!regexResult.isAvailable){
 //输入错误
 String error = regexResult.errorTips;
 //show tips
 }
 */
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Xml;

import com.boc.bocsoft.mobile.framework.R;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * 参数校验工具类
 * 
 * @author dingeryue </br>2015-01-26
 * 
 */
public class RegexUtils {

	private final static String XML_PATH = "regex.xml";
	private final static String ROOT_NAME = "regex";

	private static RegexUtils mRegexUtils;
	private static int regexFileId;
	private HashMap<String, RegexItem> regexMaps;

	private RegexUtils() {
	}

	private static RegexUtils getInstance(Context context) {

		if (mRegexUtils == null) {
			mRegexUtils = new RegexUtils();
			mRegexUtils.loadData(context);
		}

		return mRegexUtils;
	}

	public static void configRegexFileId(int id){
		regexFileId = id;
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
	 *            校验类型
	 * @param input
	 *            输入
	 * @param isMust
	 *            是否必须输入 不能为空
	 * @return
	 */
	public static RegexResult check(Context mContext, String type,
									String input, boolean isMust) {
		// TODO 这里不做异常控制 方便开发时发现错误！！！
		return getInstance(mContext).getRegexItem(type).check(mContext, input, isMust);
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

	private void loadData(Context context) {
		XmlPullParser parser = Xml.newPullParser();
		InputStream ins = null;
		context = context.getApplicationContext();
		try {
			//ins = getClass().getClassLoader().getResourceAsStream(XML_PATH);
				ins = context.getResources().openRawResource(regexFileId);
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
