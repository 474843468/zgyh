package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeResult.QRPayGetPayeeResultResult;
import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.List;

public class QrcollectionAdapterAdapter extends BaseAdapter {
    private List<QRPayGetPayeeResultResult.ResultBean> mEntities;
    
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public QrcollectionAdapterAdapter(Context context, List<QRPayGetPayeeResultResult.ResultBean> entities) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mEntities = entities;
    }

    public void updataList(List<QRPayGetPayeeResultResult.ResultBean> entities) {
        mEntities = entities;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mEntities.size();
    }

    @Override
    public QRPayGetPayeeResultResult.ResultBean getItem(int position) {
        return mEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.qrcollection_adapter, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.qrcodeAdapterMoney = (TextView) convertView.findViewById(R.id.qrcode_adapter_money);
            viewHolder.qrcode_adapter_name= (TextView) convertView.findViewById(R.id.qrcode_adapter_name);
            convertView.setTag(viewHolder);
        }
        initializeViews((QRPayGetPayeeResultResult.ResultBean) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(QRPayGetPayeeResultResult.ResultBean entity, ViewHolder holder) {    //TODO implement
        holder.qrcode_adapter_name.setText(entity.getAccName());
        holder.qrcodeAdapterMoney.setText(entity.getAmount());

    }

    protected class ViewHolder {
        private TextView qrcodeAdapterMoney;
        private TextView qrcode_adapter_name;

    }
}
