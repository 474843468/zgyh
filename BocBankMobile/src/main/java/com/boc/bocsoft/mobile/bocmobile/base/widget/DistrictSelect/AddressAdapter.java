package com.boc.bocsoft.mobile.bocmobile.base.widget.DistrictSelect;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieDu on 2016/7/20.
 */
public class AddressAdapter extends BaseAdapter {
    protected List<? extends IAddress> mList = new ArrayList<>();
    //标记选中条目
    private int selectPosition;

    protected LayoutInflater mInflater;

    protected Context mContext;

    public AddressAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
        selectPosition = -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        IAddress address = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.boc_districtselect_listitem, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = address == null ? "" : address.getName();
        holder.name.setText(name);
        holder.selected.setVisibility(View.INVISIBLE);
        if (selectPosition == position) {
            holder.name.setTextColor(Color.RED);
            convertView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.name.setTextColor(Color.BLACK);
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        ImageView selected;

        public ViewHolder(View itemView) {
            name = (TextView) itemView.findViewById(R.id.name);
            selected = (ImageView) itemView.findViewById(R.id.selected);
        }
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    public IAddress getItem(int position) {
        return PublicUtils.isEmpty(mList) ? null : mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void setDatas(List<? extends IAddress> datas) {
        mList = datas;
        notifyDataSetChanged();
    }

    public List<? extends IAddress> getDatas() {
        return mList;
    }

    public void clearData(){
        mList=null;
    }

}
