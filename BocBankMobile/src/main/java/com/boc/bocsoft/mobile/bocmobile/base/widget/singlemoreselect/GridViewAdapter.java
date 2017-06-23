package com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 单选多选组件适配器
 * Created by liuweidong on 2016/6/2.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Content> list;
    private LayoutInflater inflater;
    // 是否设置尺寸
    private boolean isUpdated = false;
    // 子条目的宽
    private int width;
    // 子条目的高
    private int height;

    public GridViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        list = new ArrayList<>();
    }

    public void setData(List<Content> list) {
        this.list = list;
    }

    public  List<Content> getData(){
        return list;
    }

    /**
     * 设置子条目的尺寸
     *
     * @param isUpdated
     * @param width
     */
    public void setItemSize(boolean isUpdated, int width, int height) {
        this.isUpdated = isUpdated;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Content getItem(int position) {
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
            convertView = inflater.inflate(R.layout.boc_select_item_grid_view, parent, false);
            holder.textView = (TextView) convertView.findViewById(R.id.textview);
            // 设置子条目的尺寸
            setItemSize(holder.textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Content dataItem = list.get(position);
        holder.textView.setText(dataItem.getName());
        setBackground(dataItem.getSelected(), holder.textView);
        return convertView;
    }

    /**
     * 设置背景
     *
     * @param isSelected
     * @param v
     */
    private void setBackground(boolean isSelected, TextView v) {
        if (isSelected) {
            v.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.boc_textview_bg_light));
            Drawable select = context.getResources().getDrawable(R.drawable.boc_check_box_true);
            v.setCompoundDrawablesWithIntrinsicBounds(select, null, null, null);
            v.setTextColor(context.getResources().getColor(R.color.boc_text_color_red));
        } else {
            v.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.boc_textview_bg_default));
            v.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            v.setTextColor(context.getResources().getColor(R.color.boc_text_color_common_gray));
        }
        int px = context.getResources().getDimensionPixelSize(R.dimen.boc_space_between_14px);
        v.setPadding(px, 0, px, 0);
    }

    /**
     * 设置子条目的尺寸
     *
     * @param textView
     */
    private void setItemSize(TextView textView) {
        if (isUpdated) {
            ViewGroup.LayoutParams lp = textView.getLayoutParams();
            lp.width = width;
            lp.height = height;
            textView.setLayoutParams(lp);
        }
    }

    class ViewHolder {
        public TextView textView;
    }
}
