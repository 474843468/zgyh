package com.chinamworld.bocmbci.biz.plps.order;

import java.util.Comparator;

import com.chinamworld.bocmbci.biz.plps.SortData;


/*
 * @author zxj
 */
public class PinyinComparatorSid implements Comparator<SortData>{

	public int compare(SortData o1, SortData o2) {
		  //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序  // -1 0 1
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
