package com.chinamworld.bocmbci.biz.remittance.utils.map.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 键值对索引排序的工具类 HashMap简单排序的一种实现
 *
 * @param <K>
 * @param <V>
 */
public class HashList<K, V> {

	/** 键列表 */
	private List<String> keyArr = new ArrayList<String>();
	/** 排序后的值列表 */
//	private List<Map<String, String>> keyMapArr = new ArrayList<Map<String,String>>();
	/** 键值对映射 */
	private HashMap<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();
	/** 键值分类 */
	private KeySort<K, V> keySort;

	public HashList(KeySort<K, V> keySort) {
		this.keySort = keySort;
	}

	/** 
	 * 根据value值返回key
	 * */
	public String getKey(Map<String, String> v) {
		return keySort.getKey(v);
	}

	/** 键值对排序 */
	public void sortKeyComparator(Comparator<Map<String, String>> comparator) {
//		Collections.sort(keyMapArr, comparator);
		Collections.sort(keyArr, new Comparator<String>() {

			public int compare(String lhs, String rhs) {
				return lhs.compareTo(rhs);
			}
		});
	}

	/** 根据索引返回键值 */
	public String getKeyIndex(int key) {
		return keyArr.get(key);
	}

	/** 根据索引返回键值对 */
	public List<Map<String, String>> getValueListIndex(int key) {
		return map.get(getKeyIndex(key));
	}

	/**
	 * 根据一二级下标获取Value值
	 * 
	 * @param key
	 *            一级下标
	 * @param value
	 *            二级下标
	 * @return Value返回值
	 */
	public Map<String, String> getValueIndex(int key, int value) {
		return getValueListIndex(key).get(value);
	}

	/** 返回一级大小 */
	public int size() {
		return keyArr.size();
	}

	public void clear() {
		keyArr.clear();
//		keyMapArr.clear();
		map.clear();
	}

	public boolean contains(Object object) {
		return false;
	}

	public boolean isEmpty() {
		return false;
	}

	public Object remove(int location) {
		return null;
	}

	public boolean remove(Object object) {
		return false;
	}

	public boolean removeAll(Collection<?> arg0) {
		return false;
	}

	public boolean retainAll(Collection<?> arg0) {
		return false;
	}

	public Object set(int location, Object object) {
		return keyArr.set(location, (String) object);
	}

	public List<String> subList(int start, int end) {
		return keyArr.subList(start, end);
	}

	public Object[] toArray() {
		return keyArr.toArray();
	}

	public Object[] toArray(Object[] array) {
		return keyArr.toArray(array);
	}
	
	@Override
	public String toString() {
		return "[keyArr:" + keyArr.toString() + 
//				"]keyMapArr:[" + keyMapArr.toString() + 
				"]map:[" + map.toString() + "]";
	}

	@SuppressWarnings("unchecked")
	public boolean add(Object object) {
		Map<String, String> tempMap = (Map<String, String>) object;
		String key = getKey(tempMap);
		if (!map.containsKey(key)) {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			list.add(tempMap);
			keyArr.add(key);
			map.put(key, list);
		} else {
			map.get(key).add(tempMap);
		}
		return false;
	}
	
	public int indexOfKey(K k) {
		return keyArr.indexOf(k);
	}
}
