package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.boc.bocsoft.mobile.framework.widget.listview.BaseRecycleViewAdapter;

/**
 * @author wangyang
 *         2016/10/10 20:23
 *         交易限额设置Adapter
 */
public class LimitAdapter extends BaseRecycleViewAdapter<Object,LimitAdapter.LimitHolder>{


    public LimitAdapter(Context context) {
        super(context);
    }

    @Override
    public LimitHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(LimitHolder limitHolder, int i) {

    }

    public static class LimitHolder extends RecyclerView.ViewHolder{

        public LimitHolder(View itemView) {
            super(itemView);
        }
    }
}
