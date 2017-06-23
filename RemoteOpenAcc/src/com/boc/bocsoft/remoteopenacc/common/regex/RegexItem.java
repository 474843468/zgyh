package com.boc.bocsoft.remoteopenacc.common.regex;

import org.xmlpull.v1.XmlPullParser;

import com.boc.bocsoft.remoteopenacc.common.util.BocroaUtils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

/**
 * 
 * @author dingeryue</br> 2015-01-26
 *
 */
public class RegexItem {

	public final static String X_ITEM_ROOT_NAME = "item";
	private final static String X_ITEM_STYLE = "style";
	private final static String X_ITEM_DES = "des";
	
	private final static String X_EMPTY_TIPS = "empty_tips";
	private final static String X_REGEX_TIPS = "regex_tips";
	private final static String X_REGEX_STR = "regex_str";
	private final static String X_MAX_LENGTH = "max_length";
	private final static String X_MIN_LENGTH = "min_length";
	private final static String X_CHINESE_LENGTH = "chinese_length";
	private final static String X_IS_FULL_WORD = "is_full_word";
	
	
	
	public RegexItem(){};
	 
	private String style;
	private String desc;
	
	private String tips_empty_ids;
	private String tips_regex_ids;
	private String regex_str;
	private int max_len =- 1;
	private int min_len = -1;
	private int chinese_word_len = 2;
	private boolean isCanFullWord = true;
	

	private Context mContext;
	public RegexResult check(Context mContext,String input,boolean isMust){
		this.mContext = mContext.getApplicationContext();
		RegexResult regexResult = new RegexResult();
		regexResult.isAvailable = true;

		// 空校验
		if (BocroaUtils.isEmpty(input) && isMust) {
			if(isMust){
				// 空的提示
				regexResult.isAvailable = false;
				regexResult.errorTips = getResString(tips_empty_ids);
				return regexResult;
			}else{
				// 空的提示
				regexResult.isAvailable = true;
				return regexResult;
			}
			
		}else if(BocroaUtils.isEmpty(input) && !isMust){
			return regexResult;
		}
		// 检查长度
		if (min_len > 0 || max_len > 0) {
			if (max_len < 0) {
				max_len = Integer.MAX_VALUE;
			}
			int length = getStringLength(input,chinese_word_len);
			if (length < min_len || length > max_len) {
				// 长度不合要求
				regexResult.isAvailable = false;
				regexResult.errorTips = getResString(tips_regex_ids);
				return regexResult;
			}
		}
		
		//全角
		if(!isCanFullWord){
			boolean isHas = BocroaUtils.isContainFullWidthChar(input);
			if(isHas){
				regexResult.isAvailable = false;
				//含有全角字符
				regexResult.errorTips = getResString(tips_regex_ids);
				return regexResult;
			}
		}
		
		//正则表达式校验  正则不为空 并且输入不为空才校验正则
		if(!BocroaUtils.isEmpty(regex_str) && !BocroaUtils.isEmpty(input)){
			//
			boolean is = input.matches(regex_str);
			
			if(!is){
				regexResult.isAvailable = false;
				//正则校验失败
				regexResult.errorTips = getResString(tips_regex_ids);
				return regexResult;
			}
		}
		
		
		regexResult.isAvailable = true;
		
		return regexResult;
		
	}
	
	
	private String getResString(String resName){
		Resources resources = mContext.getResources();
		int ids = resources.getIdentifier(resName, "string", mContext.getPackageName());
		if(ids!= 0){
			return resources.getString(ids);
		}
		return "";
	}
	
	
	public void parse(XmlPullParser parser) throws Exception {
		style = parser.getAttributeValue(null, X_ITEM_STYLE);
		desc = parser.getAttributeValue(null, X_ITEM_DES);
		
		//Log.d("dding", "--tag:"+parser.getName());
		for (;;) {
			 parser.next();
			 
			 int type = parser.getEventType();
			 
			if (XmlPullParser.END_TAG == type) {
				String name = parser.getName();
				if (X_ITEM_ROOT_NAME.equals(name)) {
					break;
				}

				continue;
			}
			
			if (XmlPullParser.START_TAG != type) {
				continue;
			}

			// 解析属性
			parseItem(parser);
		}
	}
	
	private void parseItem(XmlPullParser mParser)throws Exception{
		//开始加载数据
		String name = mParser.getName();
		
		if(X_EMPTY_TIPS.equals(name)){
			//空提示
			this.tips_empty_ids = mParser.nextText();
			return;
		}
		 
		if(X_REGEX_TIPS.equals(name)){
			this.tips_regex_ids = mParser.nextText();
			return;
		}
		
		if(X_REGEX_STR.equals(name)){
			this.regex_str = mParser.nextText();
			return;
		}
		
		if(X_MAX_LENGTH.equals(name)){
			this.max_len = parseInt(mParser.nextText(), -1);
			return;
		}
		
		if(X_MIN_LENGTH.equals(name)){
			this.min_len = parseInt(mParser.nextText(), -1);
			return;
		}
		
		if(X_CHINESE_LENGTH.equals(name)){
			this.chinese_word_len = parseInt(mParser.nextText(), 2);
			return;
		}
		
		if(X_IS_FULL_WORD.equals(name)){
			this.isCanFullWord = parseBoolean(mParser.nextText(), true);
			return;
		}
		
	}
	
	
	public String getStyle() {
		return style; 
	}
	
	private int parseInt(String ins,int defaultValue){
		try{
			return Integer.parseInt(ins);
		}catch(RuntimeException t){
			return defaultValue;
		}
	}
	
	private boolean parseBoolean(String str,boolean defaultValue){
		if(defaultValue){
			return !"false".equalsIgnoreCase(str);
		}else{
			return "true".equalsIgnoreCase(str);
		}
	}


	@Override
	public String toString() {
		return "PESAPRegexItem [style=" + style + ", desc=" + desc
				+ ", tips_empty_ids=" + tips_empty_ids + ", tips_regex_ids="
				+ tips_regex_ids + ", regex_str=" + regex_str + ", max_len="
				+ max_len + ", min_len=" + min_len + ", chinese_word_len="
				+ chinese_word_len + ", isCanFullWord=" + isCanFullWord
				+ ", mContext=" + mContext + "]";
	}


	public static int getStringLength(String s ,int chineseLen) {
		char[] chs = s.toCharArray();
		int count = 0;
		for (int i = 0; i < chs.length; i++) {
			if ((chs[i] + "").getBytes().length >= 2) {
				count += chineseLen;
			} else {
				count++;
			}
		}
		return count;
	}
	
	
}
