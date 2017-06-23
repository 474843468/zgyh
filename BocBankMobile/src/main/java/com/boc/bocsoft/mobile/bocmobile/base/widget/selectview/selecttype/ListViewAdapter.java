package com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 筛选组件ListView的适配器
 * <p/>
 * Created by liuweidong on 2016/6/3.
 */
public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    /**
     * 数据集合
     */
    public List<SelectTypeData> list;
    private boolean isUpdate = false;
    private int width;
    private int height;

    public ListViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        list = new ArrayList<SelectTypeData>();
    }

    public void setItemSize(boolean isUpdate, int width, int height) {
        this.isUpdate = isUpdate;
        this.width = width;
        this.height = height;
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setData(List<SelectTypeData> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.boc_select_item_list_view, null);
            holder.textView = (TextView) convertView
                    .findViewById(R.id.txt_type_title);
            holder.gridView = (SelectGridView2) convertView
                    .findViewById(R.id.listview_item);
            if (isUpdate) {
                holder.gridView.setItemSize(true, width, height);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(list.get(position).getTitle());
        // GridView的数据集合
        holder.gridView.setData(list.get(position).getList());
        holder.gridView.setFocusable(false);
        return convertView;
    }
}

class ViewHolder {
    /**
     * 标题
     */
    public TextView textView;
    /**
     * 内容
     */
    public SelectGridView2 gridView;
}
