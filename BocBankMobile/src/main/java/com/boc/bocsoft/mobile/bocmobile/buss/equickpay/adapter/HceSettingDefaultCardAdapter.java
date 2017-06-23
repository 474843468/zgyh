package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.hcecardlist.HceCardListQueryViewModel;

import java.util.List;

/**
 * Created by yangle on 2016/12/26.
 * 描述:设置默认卡的列表适配器
 */
public class HceSettingDefaultCardAdapter extends RecyclerView.Adapter<HceSettingDefaultCardAdapter.ViewHolder> {

    private List<HceCardListQueryViewModel> mData;
    private OnItemClickListener mItemClickListener;

    public HceSettingDefaultCardAdapter(List<HceCardListQueryViewModel> data) {
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.boc_item_hce_default_card, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTvHceNumber;
        private final TextView mTvMasterNumber;
        private final ImageView mIvCardBrand;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvHceNumber = (TextView) itemView.findViewById(R.id.tv_hce_number);
            mTvMasterNumber = (TextView) itemView.findViewById(R.id.tv_hce_master_number);
            mIvCardBrand = (ImageView) itemView.findViewById(R.id.iv_card_brand);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(getPosition());
            }
        }

        public void setData(HceCardListQueryViewModel models) {
            mTvHceNumber.setText(models.getSlaveCardNo());
            mTvMasterNumber.setText(models.getMasterCardNo());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
