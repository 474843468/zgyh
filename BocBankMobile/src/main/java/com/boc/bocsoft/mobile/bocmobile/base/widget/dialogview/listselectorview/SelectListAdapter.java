package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 单选列表dialog 适配器｛不包含底部按钮-（确定和取消）｝
 *
 * @author yx
 * @Date 2015-11-17
 */

public abstract class SelectListAdapter<T> extends BaseAdapter {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 数据列表
     */
    private List<T> mListData = new ArrayList<T>();
    /**
     * 是否内容居左显示
     */
    private boolean isLeftShow = false;

    /**
     * listview 分割线是否设置margin
     */
    private boolean isSetLineMargin = true;

    /**
     * 构造方法
     */
    public SelectListAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    /**
     * 构造方法
     */
    public SelectListAdapter(List<T> mList, Context mContext) {
        super();
        this.mContext = mContext;
        if (null != mListData) {
            this.mListData = mList;
        }
        mListData.clear();
    }

    /**
     * 设置数据
     *
     * @param mList
     */
    public void setData(List<T> mList) {
        if (null != mListData) {
            this.mListData = mList;
            notifyDataSetChanged();
        }
    }

    /**
     * 设置数据
     *
     * @param mList
     * @param isLeftShow 内容是否居左显示
     */
    public void setData(List<T> mList, boolean isLeftShow) {
        if (null != mListData) {
            this.mListData = mList;
            this.isLeftShow = isLeftShow;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public T getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext,
                    R.layout.radio_button_listview_item, null);
            holder.ll_content_view = (LinearLayout) convertView.findViewById(R.id.ll_content_view);
            holder.tv_message = (TextView) convertView
                    .findViewById(R.id.tv_message);
            holder.view_linear = (View) convertView
                    .findViewById(R.id.view_linear);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //holder.tv_message.setText(mListData.get(position).getName());
        holder.tv_message.setText(displayValue(mListData.get(position)));
        if (isLeftShow) {
            holder.ll_content_view.setGravity(Gravity.LEFT|Gravity.CENTER);
            holder.ll_content_view.setPadding(30,0,0,0);
            if (isSetLineMargin == false){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_2px));
                params.leftMargin = 0;
                params.rightMargin = 0;
                holder.view_linear.setLayoutParams(params);
            }
        }else{
            holder.ll_content_view.setGravity(Gravity.CENTER);
        }
        if (position == (mListData.size() - 1)) {
            holder.view_linear.setVisibility(View.INVISIBLE);
        } else {
            holder.view_linear.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public abstract String displayValue(T model);


    class ViewHolder {
        /**
         * 名称
         */
        LinearLayout ll_content_view;
        /**
         * 名称
         */
        TextView tv_message;
        /**
         * 间隔线
         */
        View view_linear;
    }

    public void setSetLineMargin(boolean setLineMargin) {
        isSetLineMargin = setLineMargin;
    }
}
