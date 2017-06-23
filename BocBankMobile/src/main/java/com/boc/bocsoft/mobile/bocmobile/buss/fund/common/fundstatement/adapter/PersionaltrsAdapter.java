package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.model.PersionaltrsModel;

import java.util.List;

/**
 * Created by huixiaobo on 2016/11/22.
 * 交易流水列表适配器
 */
public class PersionaltrsAdapter extends BaseAdapter{
    /**上下文*/
    private Context mContext;
    /***/
    private List<PersionaltrsModel> mPersionaltrsList;
    @Override
    public int getCount() {
        return mPersionaltrsList.size()> 0 ? mPersionaltrsList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mPersionaltrsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }
}
