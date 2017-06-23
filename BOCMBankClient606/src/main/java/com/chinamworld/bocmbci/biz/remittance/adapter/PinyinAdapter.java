package com.chinamworld.bocmbci.biz.remittance.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.pinyin.AssortPinyinList;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.pinyin.org.pinyin.LanguageComparator_CN;
import com.chinamworld.bocmbci.log.LogGloble;

public class PinyinAdapter extends BaseExpandableListAdapter {

	public static final String TAG = "PinyinAdapter";
	/** 数据源 */
	List<Map<String, String>> strList;
	/** 经过处理后的数据源，由字母索引和值列表组成 */
	private AssortPinyinList assort;
	/** 上下文 */
	private Context context;
	/** 中文排序算法 */
	@SuppressWarnings("rawtypes")
	private LanguageComparator_CN cnSort = new LanguageComparator_CN();

	public PinyinAdapter(Context context, List<Map<String, String>> strList) {
		super();
		this.context = context;
		this.strList = strList;
		if (strList == null) {
			strList = new ArrayList<Map<String, String>>();
		}
		// 排序
		sort();
		LogGloble.i(TAG, assort.getHashList().toString());
	}

	@SuppressWarnings("unchecked")
	private void sort() {
		LogGloble.e(TAG, "list:" + strList.toString());
		assort = new AssortPinyinList();
		// 分类
		for (Map<String, String> map : strList) {
			assort.getHashList().add(map);
		}
		assort.getHashList().sortKeyComparator(cnSort);
		for(int i=0,length=assort.getHashList().size();i<length;i++) {
			Collections.sort((assort.getHashList().getValueListIndex(i)), cnSort);
		}
	}
	
	public void setData(List<Map<String, String>> list) {
		this.strList = list;
		notifyDataSetInvalidated();
	}
	
	@Override
	public void notifyDataSetInvalidated() {
		sort();
		super.notifyDataSetInvalidated();
	}

	public Object getChild(int group, int child) {
		return assort.getHashList().getValueIndex(group, child);
	}

	public long getChildId(int group, int child) {
		return child;
	}

	@SuppressWarnings("unchecked")
	public View getChildView(int group, int child, boolean arg2, View contentView, ViewGroup arg4) {
		ViewHolderChild vh;
		if (contentView == null) {
			vh = new ViewHolderChild();
			contentView = LayoutInflater.from(context).inflate(R.layout.remittance_choose_country_adapter_chat, null);
			vh.tv = (TextView) contentView.findViewById(R.id.name);
			contentView.setTag(vh);
		} else {
			vh = (ViewHolderChild) contentView.getTag();
		}
//		TextView textView = (TextView) contentView.findViewById(R.id.name);
		vh.tv.setText(((Map<String, String>) getChild(group, child)).get(Remittance.NAME_CN));
		return contentView;
	}

	public int getChildrenCount(int group) {
		return assort.getHashList().getValueListIndex(group).size();
	}

	public Object getGroup(int group) {
		return assort.getHashList().getValueListIndex(group);
	}

	public int getGroupCount() {
		return assort.getHashList().size();
	}

	public long getGroupId(int group) {
		return group;
	}

	@SuppressWarnings("unchecked")
	public View getGroupView(int group, boolean arg1, View contentView, ViewGroup arg3) {
		ViewHolderGroup vh;
		if (contentView == null) {
			vh = new ViewHolderGroup();
			contentView = LayoutInflater.from(context).inflate(R.layout.remittance_choose_country_group_item, null);
			vh.tv = (TextView) contentView.findViewById(R.id.name);
			contentView.setClickable(true);
			contentView.setTag(vh);
		} else {
			vh = (ViewHolderGroup) contentView.getTag();
		}
		vh.tv.setText(assort.getFirstChar(((List<Map<String, String>>) getGroup(group)).get(0)));
		// 禁止伸展
		return contentView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	public AssortPinyinList getAssort() {
		return assort;
	}
	
	class ViewHolderGroup {
		TextView tv;
	}
	
	class ViewHolderChild {
		TextView tv;
	}
}
