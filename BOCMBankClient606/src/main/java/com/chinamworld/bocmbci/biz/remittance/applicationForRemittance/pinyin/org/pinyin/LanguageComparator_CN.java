package com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.pinyin.org.pinyin;


import java.util.Comparator;
import java.util.Map;

import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.plps.order.CharacterParser;
import com.chinamworld.bocmbci.biz.remittance.utils.RemittanceUtils;

/**
 * 汉字排序
 * @param <T>
 * */
public class LanguageComparator_CN<T> implements Comparator<T> {

	@SuppressWarnings("unchecked")
	public int compare(T lhs, T rhs) {
		Map<String, String> map1 = (Map<String, String>)lhs;
		Map<String, String> map2 = (Map<String, String>)rhs;
		String ostr1 = map1.get(Remittance.NAME_CN);
		String ostr2 = map2.get(Remittance.NAME_CN);
		
		for (int i = 0; i < ostr1.length() && i < ostr2.length(); i++) {

			int codePoint1 = ostr1.charAt(i);
			int codePoint2 = ostr2.charAt(i);
			if (Character.isSupplementaryCodePoint(codePoint1)
					|| Character.isSupplementaryCodePoint(codePoint2)) {
				i++;
			}
			if (codePoint1 != codePoint2) {
				if (Character.isSupplementaryCodePoint(codePoint1)
						|| Character.isSupplementaryCodePoint(codePoint2)) {
					return codePoint1 - codePoint2;
				}
				String pinyin1 = pinyin((char) codePoint1);
				String pinyin2 = pinyin((char) codePoint2);

				if (pinyin1 != null && pinyin2 != null) { // 两个字符都是汉字
					if (!pinyin1.equals(pinyin2)) {
						return pinyin1.compareTo(pinyin2);
					}
				} else {
					return codePoint1 - codePoint2;
				}
			}
		}
		return ostr1.length() - ostr2.length();
	}

	// 获得汉字拼音的首字符
	private String pinyin(char c) {
		String pinyins = null;
		if (RemittanceUtils.isChinese(c)) {
			CharacterParser characterParser = CharacterParser.getInstance();
			pinyins = characterParser.getSelling(String.valueOf(c));
		}
		if (pinyins == null) {
			return null;
		}
		return pinyins;
	}
}
