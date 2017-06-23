package com.chinamworld.bocmbci.biz.remittance.utils.map.sort;

import java.util.Map;

/***
 * 分类接口，根据V value返回K key
 *
 * @param <K>
 * @param <V>
 */
public interface KeySort<K, V> {
	public String getKey(Map<String, String> v);
}
