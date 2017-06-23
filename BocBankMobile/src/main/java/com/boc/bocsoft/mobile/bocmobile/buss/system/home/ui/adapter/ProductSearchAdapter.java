package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 产品搜索adapter
 * Created by gwluo on 2016/11/13.
 */

public class ProductSearchAdapter implements ExpandableListAdapter {
    private Context mContext;
    private LinkedHashMap<String, List<String>> performProData;//界面展示的数据集合（折叠时显示三条）
    private LinkedHashMap<String, List<String>> allProductData;//所有数据集合（接口请求返回）
    private List<String> mKeys;// 分类key
    private List<Boolean> collapseList;// 展开还是折叠状态

    public ProductSearchAdapter(Context context) {
        mContext = context;
    }


    /**
     * @param performData 展示数据
     * @param allData     全量数据
     */
    public void updateDate(LinkedHashMap<String, List<String>> performData, LinkedHashMap<String,
            List<String>> allData, List<Boolean> isCollapse, List<String> keys) {
        mKeys = keys;
        collapseList = isCollapse;
        performProData = performData;
        allProductData = allData;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return performProData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return performProData.get(mKeys.get(groupPosition)).size();
    }

    /**
     * 获取全量数据中childcount
     *
     * @param groupPosition
     * @return
     */
    public int getAllChildCount(int groupPosition) {
        return allProductData.get(mKeys.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        ViewGroup groupView = (ViewGroup) View.inflate(mContext, R.layout.boc_item_product_query_title, null);
        TextView title = (TextView) groupView.findViewById(R.id.tv_title);
        TextView tv_btn = (TextView) groupView.findViewById(R.id.tv_btn);
        if (collapseList.get(groupPosition)) {
            if (getAllChildCount(groupPosition) > 3) {// 数据超过三条才显示折叠和展开按钮
                tv_btn.setText(mContext.getString(R.string.boc_product_unexpand));
            } else {
                tv_btn.setText("");
            }
        } else {
            if (getAllChildCount(groupPosition) > 3) {// 数据超过三条才显示折叠和展开按钮
                tv_btn.setText(mContext.getString(R.string.boc_product_expand));
            } else {
                tv_btn.setText("");
            }
        }
        title.setText(mKeys.get(groupPosition));
        return groupView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ViewGroup groupView = (ViewGroup) View.inflate(mContext, R.layout.boc_item_record_history, null);
        TextView content = (TextView) groupView.findViewById(R.id.tv_product_name);
        TextView tv_divider = (TextView) groupView.findViewById(R.id.tv_divider);
        content.setText(performProData.get(mKeys.get(groupPosition)).get(childPosition));
        if (isLastChild) {
            tv_divider.setVisibility(View.GONE);
        } else {
            tv_divider.setVisibility(View.VISIBLE);
        }
        return groupView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
