package com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡列表适配器
 * @param <T>
 * Created by liuweidong on 2016/11/22.
 */
public abstract class CardStackAdapter<T> extends CardStackView.Adapter<CardStackView.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<T> mData;// 数据

    public CardStackAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList();
    }

    public LayoutInflater getLayoutInflater() {
        return this.mInflater;
    }

    /**
     * 刷新数据
     *
     * @param data
     */
    public void updateData(List<T> data) {
        this.setData(data);
        this.notifyDataSetChanged();
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setData(List<T> data) {
        this.mData.clear();
        if (data != null) {
            this.mData.addAll(data);
        }
    }

    @Override
    public void onBindViewHolder(CardStackView.ViewHolder holder, int position) {
        T data = this.getItem(position);
        this.bindView(data, position, holder);
    }

    public abstract void bindView(T data, int position, CardStackView.ViewHolder holder);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public T getItem(int position) {
        return this.mData.get(position);
    }

}
