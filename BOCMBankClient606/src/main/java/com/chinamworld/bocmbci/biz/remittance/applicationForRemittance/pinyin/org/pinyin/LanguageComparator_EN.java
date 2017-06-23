package com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.pinyin.org.pinyin;

import java.util.Comparator;

public class LanguageComparator_EN implements Comparator<String> {

	public int compare(String ostr1, String ostr2) {
		return ostr1.compareToIgnoreCase(ostr2);
	}
}
