package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.QueryPayerListViewModel;

import java.util.List;

/**
 * Adapter：选中的账户列表
 * Created by zhx on 2016/7/14
 */
public class SelectedPayerAdapter extends RecyclerView.Adapter<SelectedPayerAdapter.MyHolder> {
    private Context context;
    private List<QueryPayerListViewModel.ResultBean> datas;
    int clickPosition;

    public SelectedPayerAdapter(Context context, List<QueryPayerListViewModel.ResultBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup viewGroup, final int i) { // 决定根布局
        View itemView = View.inflate(context, R.layout.list_item_payer_bottom, null);
        return new MyHolder(itemView);
    }

    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(SelectedPayerAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(int positon);
    }

    @Override
    public void onBindViewHolder(MyHolder myHolder, final int position) { // 填充数据
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        myHolder.setDataAndRefreshUI(datas.get(position));
    }

    @Override
    public int getItemCount() { // 条目总数
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        //孩子对象
        private TextView tv_item;
        public View itemView;

        public MyHolder(View itemView) {
            super(itemView);
            //初始化孩子对象
            this.itemView = itemView;
            tv_item = (TextView) itemView.findViewById(R.id.tv_item);
        }

        /**
         * 设置itemView的数据展示
         *
         * @param dataBean
         */
        public void setDataAndRefreshUI(QueryPayerListViewModel.ResultBean dataBean) {
            tv_item.setText(dataBean.getPayerName());
        }
    }
}