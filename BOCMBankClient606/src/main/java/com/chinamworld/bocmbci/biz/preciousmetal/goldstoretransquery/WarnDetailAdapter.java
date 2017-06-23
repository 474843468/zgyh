package com.chinamworld.bocmbci.biz.preciousmetal.goldstoretransquery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by linyl on 2016/9/13.
 */
public class WarnDetailAdapter extends BaseAdapter implements
        PinnedSectionListView.PinnedSectionListAdapter {
    // 时间以及相关信息
    private ArrayList<PinnedSectionBean> list;//这个是分好类别后的list,在所属activity进行数据分类
    private Context mContext;

    public ArrayList<PinnedSectionBean> getList() {
        return list;
    }

    public void setList(ArrayList<PinnedSectionBean> list) {
        if (list != null) {
            this.list = list;
        } else {
            list = new ArrayList<PinnedSectionBean>();
        }
    }

    public WarnDetailAdapter(ArrayList<PinnedSectionBean> list, Context mContext) {
        super();
        this.setList(list);
        this.mContext = mContext;
    }

    class ViewHolder {
        TextView item_date, item_content,tv_week,tv_add_price,tv_amount,tv_title,tv_type;
        LinearLayout ll_content,ll_title;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public PinnedSectionBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_list_layout, null);
            viewHolder.ll_title = (LinearLayout) convertView
                    .findViewById(R.id.ll_lv_title_line);
            viewHolder.tv_title = (TextView) convertView
                    .findViewById(R.id.tv_traquery_listtitle);
            viewHolder.ll_content = (LinearLayout) convertView
                    .findViewById(R.id.ll_listview_content);
            viewHolder.item_date = (TextView) convertView
                    .findViewById(R.id.item_date);
            viewHolder.item_content = (TextView) convertView
                    .findViewById(R.id.tv_one_right);
            viewHolder.tv_week = (TextView) convertView
                    .findViewById(R.id.item_week);
            viewHolder.tv_add_price = (TextView) convertView.
                    findViewById(R.id.tv_address_price);
            viewHolder.tv_amount = (TextView) convertView
                    .findViewById(R.id.tv_amount);
            viewHolder.tv_type = (TextView) convertView
                    .findViewById(R.id.item_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PinnedSectionBean bean = getItem(position);
        // 当item属于标题的时候,就只显示分类的日期yyyy-MM-dd
        if (bean.type == PinnedSectionBean.SECTION) {
            viewHolder.ll_title.setVisibility(View.VISIBLE);
            viewHolder.ll_content.setVisibility(View.GONE);
            viewHolder.tv_title.setText(list.get(position).getMessages()
                    .getTitle());

        }
        // 当item属于内容的时候,就只显示分类的日期HH:mm:ss,和其他类容
        else {
            viewHolder.ll_title.setVisibility(View.GONE);
            viewHolder.ll_content.setVisibility(View.VISIBLE);
            viewHolder.item_date.setText(list.get(position).getMessages()
                    .getDate());
            viewHolder.item_content.setText(list.get(position).getMessages()
                    .getWeightNum());
            viewHolder.tv_week.setText(list.get(position).getMessages()
                    .getWeek());
            viewHolder.tv_add_price.setText(list.get(position).getMessages()
                    .getAddr_price());
            viewHolder.tv_amount.setText(list.get(position).getMessages()
                    .getAmount());
            viewHolder.tv_type.setText(list.get(position).getMessages()
                    .getType());
        }
        return convertView;

    }

    //判断是否是属于标题悬浮的
    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == PinnedSectionBean.SECTION;
    }

    //arraylist的数据里面有2种类型,标题和内容
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return ((PinnedSectionBean) getItem(position)).type;
    }

    /**遍历新增的ArrayList<PinnedSectionBean> 添加到适配器已有数据源ArrayList<PinnedSectionBean> list**/
    public void addSourceList(ArrayList<PinnedSectionBean> list) {
        if(list == null || list.size() <= 0)
            return;

        if(this.list == null) {
            this.list = list;
            this.notifyDataSetChanged();
            return;
        }

        Iterator var3 = list.iterator();
        while(var3.hasNext()) {
            PinnedSectionBean item = (PinnedSectionBean)var3.next();
            this.list.add(item);
        }
        this.notifyDataSetChanged();

    }



}
