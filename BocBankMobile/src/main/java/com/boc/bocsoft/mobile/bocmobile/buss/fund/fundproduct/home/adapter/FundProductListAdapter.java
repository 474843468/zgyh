package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.FundProductShowModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui.SyncHorizontalScrollView;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 基金产品列表适配器
 * Created by liuzc on 2016/11/18.
 */
public class FundProductListAdapter extends BaseAdapter {
    private Context mContext;
    private List<FundProductShowModel> mProductShowModelList = new ArrayList<>();

    public FundProductListAdapter(Context context) {
        this.mContext = context;
    }

    private SyncHorizontalScrollView srvTitle = null; //联动的头部scrollview
    private List<SyncHorizontalScrollView> srvContentList = null;

    private IFundProductItemListener iListener = null;

    private int itemViewHeight = 0; //每一行的高度

    private boolean isFundTypeAll = true; //是否是所有基金类型（如果是货币型或者理财型，需特殊显示）

    public void setData(List<FundProductShowModel> list) {
        this.mProductShowModelList = list;
    }

    public void setListener(IFundProductItemListener listener){
        iListener = listener;
    }

    @Override
    public int getCount() {
        return mProductShowModelList.size();
    }

    @Override
    public FundProductShowModel getItem(int position) {
        return mProductShowModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null ||
                (isFundTypeAll && ((ViewHolder) convertView.getTag()).tvDwjz == null) ||
                (!isFundTypeAll && ((ViewHolder) convertView.getTag()).tvYieldOfTenThousand == null)) {

            holder = new ViewHolder();
            if(isFundTypeAll){
                convertView = View.inflate(mContext, R.layout.list_item_fund_product_alltype, null);
            }
            else{
                convertView = View.inflate(mContext, R.layout.list_item_fund_product_currency, null);
            }

            holder.llyBackground = (LinearLayout)convertView.findViewById(R.id.llyBackground);
            holder.llyValues = (LinearLayout)convertView.findViewById(R.id.llyValues);
            holder.tvFundName = (TextView)convertView.findViewById(R.id.tvFundName);
            holder.tvFundCode = (TextView)convertView.findViewById(R.id.tvFundCode);
            holder.tvJzTime = (TextView)convertView.findViewById(R.id.tvJzTime);
            holder.tvDwjz = (TextView)convertView.findViewById(R.id.tvDwjz);
            holder.tvCurrentPercentDiff = (TextView)convertView.findViewById(R.id.tvCurrentPercentDiff);
            holder.tvChangeOfMonth = (TextView)convertView.findViewById(R.id.tvChangeOfMonth);
            holder.tvChangeOfQuarter = (TextView)convertView.findViewById(R.id.tvChangeOfQuarter);
            holder.tvChangeOfHalfYear = (TextView)convertView.findViewById(R.id.tvChangeOfHalfYear);
            holder.tvChangeOfYear = (TextView)convertView.findViewById(R.id.tvChangeOfYear);
            holder.tvThisYearPriceChange = (TextView)convertView.findViewById(R.id.tvThisYearPriceChange);

            holder.tvYieldOfTenThousand = (TextView)convertView.findViewById(R.id.tvYieldOfTenThousand);
            holder.tvYieldOfWeek = (TextView)convertView.findViewById(R.id.tvYieldOfWeek);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FundProductShowModel curItem = mProductShowModelList.get(position);

        if(isFundTypeAll){
            holder.tvDwjz.setText(curItem.getDwjz());
            holder.tvCurrentPercentDiff.setText(curItem.getCurrPercentDiff());
            holder.tvChangeOfMonth.setText(curItem.getChangeOfMonth());
            holder.tvChangeOfQuarter.setText(curItem.getChangeOfQuarter());
            holder.tvChangeOfHalfYear.setText(curItem.getChangeOfHalfYear());
            holder.tvChangeOfYear.setText(curItem.getChangeOfYear());
            holder.tvThisYearPriceChange.setText(curItem.getThisYearPriceChange());

            if(srvContentList == null){
                srvContentList = new LinkedList<SyncHorizontalScrollView>();
                srvContentList.add(srvTitle);
            }
            SyncHorizontalScrollView curScrollView = (SyncHorizontalScrollView)convertView.findViewById(R.id.scrBackground);
            srvContentList.add(curScrollView);

            updateScorllView();
        }
        else{
            holder.tvYieldOfTenThousand.setText(curItem.getYieldOfTenThousand());
            holder.tvYieldOfWeek.setText(curItem.getYieldOfWeek());
        }

        holder.tvFundName.setText(curItem.getFundName());
        holder.tvFundCode.setText(curItem.getFundBakCode());
        holder.tvJzTime.setText(curItem.getJzTime());

        //点击监听
        holder.llyBackground.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickItem(position);
            }
        });

        holder.llyValues.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickItem(position);
            }
        });

        return convertView;
    }

    private void onClickItem(int position){
        if(iListener != null && mProductShowModelList != null){
            FundProductShowModel item = mProductShowModelList.get(position);
            iListener.onClickItem(item);
        }
    }

    public void updateScorllView(){
        if(srvContentList != null){
            for(int i = 0; i < srvContentList.size(); i ++){
                SyncHorizontalScrollView curView = srvContentList.get(i);
                curView.setScrollView(srvContentList);
            }
        }
    }

    public void setTitleScrollView(SyncHorizontalScrollView view){
        srvTitle = view;
    }

    public void setFundTypeAll(boolean fundTypeAll) {
        isFundTypeAll = fundTypeAll;
    }


    private class ViewHolder {
        private LinearLayout llyBackground; //整个item背景
        private TextView tvFundName; //基金名称

        private LinearLayout llyValues; //右侧可滑动部分
        private TextView tvFundCode; //基金代码
        private TextView tvDwjz;  //单位净值
        private TextView tvJzTime; //净值时间
        private TextView tvCurrentPercentDiff; //日涨跌幅
        private TextView tvChangeOfMonth; //月涨跌幅
        private TextView tvChangeOfQuarter; //季涨跌幅
        private TextView tvChangeOfHalfYear; //半年涨跌幅
        private TextView tvChangeOfYear; //年涨跌幅
        private TextView tvThisYearPriceChange; //今年涨跌幅

        private TextView tvYieldOfTenThousand; //万份收益率
        private TextView tvYieldOfWeek; //七日年化收益率
        private SyncHorizontalScrollView scrolview;
    }

    public int getItemViewHeight() {
        if(itemViewHeight <= 0){
            itemViewHeight = ResUtils.dip2px(mContext, 60);
        }

        return itemViewHeight;
    }

    public interface IFundProductItemListener{
        void onClickItem(FundProductShowModel item);
    }
}
