package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.widget.SelectListDialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 单选列表dialog 适配器｛包含底部按钮-（确定和取消）｝
 */

public  class DialogSelectListAdapter extends BaseAdapter {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 数据列表
     */
    private List<RadioDialog.Option> mListData = new ArrayList<>();

    /**
     * 构造方法
     */
    public DialogSelectListAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    /**
     * 构造方法
     */
    public DialogSelectListAdapter(List<RadioDialog.Option> mList, Context mContext) {
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
    public void setData(List<RadioDialog.Option> mList) {
        if (null != mListData) {
            this.mListData = mList;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public RadioDialog.Option getItem(int position) {
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
                    R.layout.radio_button_dialog_listview_item, null);
            holder.rl_content_view = (RelativeLayout) convertView.findViewById(R.id.rl_content_view);
            holder.tv_message = (TextView) convertView
                    .findViewById(R.id.tv_message);
            holder.tv_message_below = (TextView) convertView
                    .findViewById(R.id.tv_message_below);
            holder.iv_check=(ImageView)convertView.findViewById(R.id.ivCheck);
            holder.view_linear = (View) convertView
                    .findViewById(R.id.view_linear);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_message.setText(mListData.get(position).getName());
        holder.tv_message_below.setText(mListData.get(position).getValue());
        holder.iv_check.setImageResource(mListData.get(position).isCkecked()?R.drawable.tabrow_selected:R.drawable.tabrow_unselected);
        if (position == (mListData.size() - 1)) {
            holder.view_linear.setVisibility(View.INVISIBLE);
        } else {
            holder.view_linear.setVisibility(View.VISIBLE);
        }
        return convertView;
    }


    class ViewHolder {
        /**
         * 名称
         */
        RelativeLayout rl_content_view;
        /**
         * 名称
         */
        TextView tv_message;
        TextView tv_message_below;
        ImageView iv_check;
        /**
         * 间隔线
         */
        View view_linear;
    }
}
