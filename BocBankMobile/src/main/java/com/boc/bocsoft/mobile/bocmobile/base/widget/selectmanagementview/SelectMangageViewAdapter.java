package com.boc.bocsoft.mobile.bocmobile.base.widget.selectmanagementview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;

import java.util.List;

/**
 * Created by liuyang on 2016/7/5.
 */
public class SelectMangageViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<AccountBean> cardList;
    private ItemClickListener mItemClickListener;

    public SelectMangageViewAdapter(Context context) {
        super();
        this.mContext = context;
    }

    //传入数据
    public void update(List mlist) {
        this.cardList = mlist;
        notifyDataSetChanged();
    }

    //传入数据和点击监听
    public void setData(List<AccountBean> list,ItemClickListener mItemClickListener){
        cardList=list;
        this.mItemClickListener = mItemClickListener;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cardList == null ? 0 : cardList.size();
    }

    @Override
    public Object getItem(int position) {
        return cardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SelectManageViewItem itemview = (SelectManageViewItem) convertView;
        if (convertView == null) {
            itemview = new SelectManageViewItem(mContext);
        }
        if(cardList!=null&&cardList.size()>0){
            itemview.updateData(cardList.get(position),position);
            itemview.setClickListener(mItemClickListener);
        }
        return itemview;
    }


    public interface ItemClickListener {
        void onItemClick(int pos, AccountBean item);
    }
}




