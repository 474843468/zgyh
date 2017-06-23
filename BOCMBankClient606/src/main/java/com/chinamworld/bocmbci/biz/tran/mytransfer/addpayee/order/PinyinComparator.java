package com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee.order;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<SortModel> {

	public int compare(SortModel o1, SortModel o2) {
		  //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序  // -1 0 1
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			if(o1.getSortLetters().equals("常用")
					|| o2.getSortLetters().equals("常用") ){
				return 1;
			}
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
