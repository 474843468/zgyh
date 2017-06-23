package com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.pinyin;

import java.util.Map;

import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.plps.order.CharacterParser;
import com.chinamworld.bocmbci.biz.remittance.utils.RemittanceUtils;
import com.chinamworld.bocmbci.biz.remittance.utils.map.sort.HashList;
import com.chinamworld.bocmbci.biz.remittance.utils.map.sort.KeySort;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

public class AssortPinyinList {
	
	private static final String TAG = "AssortPinyinList";
	private HashList<String, Map<String, String>> hashList = new HashList<String, Map<String, String>>(new KeySort<String, Map<String, String>>() {
		public String getKey(Map<String, String> value) {
			return getFirstChar(value);
		}
	});

	// 获得字符串的首字母 首字符 转汉语拼音
	public String getFirstChar(Map<String, String> value) {
		LogGloble.i(TAG, "getFirstChar:" + value.toString());
		String name = value.get(Remittance.NAME_CN);
		// 首字符
		char firstChar = name.charAt(0);
		// 首字母分类
		String first = null;
		// 是否是非汉字
//		String[] print = PinyinHelper.toHanyuPinyinStringArray(firstChar);
		String print = null;
		if (RemittanceUtils.isChinese(firstChar)) {
			CharacterParser characterParser = CharacterParser.getInstance();
			print = characterParser.getSelling(String.valueOf(firstChar));
		}
		
		if (StringUtil.isNull(print)) {

			// 将小写字母改成大写
			if ((firstChar >= 97 && firstChar <= 122)) {
				firstChar -= 32;
			}
			if (firstChar >= 65 && firstChar <= 90) {
				first = String.valueOf((char) firstChar);
			} else {
				// 认为首字符为数字或者特殊字符
				first = "#";
			}
		} else {
			// 如果是中文 分类大写字母
			first = String.valueOf((char) (print.charAt(0) - 32));
		}
		if (first == null) {
			first = "?";
		}
//		LogGloble.i(TAG, "getFirstChar-value:" + value + "-firstChar:" + first);
		return first;
	}

	public HashList<String, Map<String, String>> getHashList() {
//		LogGloble.i(TAG, "getHashList-" + hashList.toString());
		return hashList;
	}
}
