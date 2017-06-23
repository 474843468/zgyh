package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadreferprofitdetailquery.PsnXpadReferProfitDetailQueryResModel;

import java.util.List;

/**
 * 中信理财 - 持仓详情  收益明细列表适配器 {除了收益累计}
 * Created by cff on 2016/9/23.
 */
public class FinancialTypeReferProfitAdapter extends BaseAdapter {
    private Context mContext;
    //收益明细列表数据
    private List<PsnXpadReferProfitDetailQueryResModel.QueryModel> profirList;
    private String mCurCode = "001";//币种
    //适配器构造器
    public FinancialTypeReferProfitAdapter(Context mContext) {
        super();
        this.mContext = mContext;

    }

    /**
     * 为适配器设置数据
     */
    public void setProfirDate(List<PsnXpadReferProfitDetailQueryResModel.QueryModel> profirList) {
        this.profirList = profirList;
        notifyDataSetChanged();
    }
    /**
     * 为适配器设置数据
     */
    public void setProfirDate(List<PsnXpadReferProfitDetailQueryResModel.QueryModel> profirList,String mCurCode) {
        this.profirList = profirList;
        LogUtil.d("yx--------------收益明细--集合数据---》"+profirList.size());
        this.mCurCode = mCurCode;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return profirList == null ? 0 : profirList.size();
    }

    @Override
    public Object getItem(int position) {
        return profirList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (null == vh) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_fixedterm_referpro_listitem, null);
            vh.profit_item_type = (TextView) convertView.findViewById(R.id.profit_item_type);
            vh.profit_starttime = (TextView) convertView.findViewById(R.id.profit_starttime);
            vh.profit_endtime = (TextView) convertView.findViewById(R.id.profit_endtime);
            vh.profit_amount = (TextView) convertView.findViewById(R.id.profit_amount);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        PsnXpadReferProfitDetailQueryResModel.QueryModel model = profirList.get(position);
        //在此判断类型
        vh.profit_item_type.setText(getPayFlag(model.getPayflag()));
        //日期格式化
        vh.profit_starttime.setText(model.getIntsdate());
        vh.profit_endtime.setText(model.getIntedate());
        //金额
        vh.profit_amount.setText(MoneyUtils.transMoneyFormat(model.getPayprofit()+"", mCurCode+""));

        return convertView;
    }

    private class ViewHolder {
        TextView profit_item_type;//状态
        TextView profit_starttime;//开始时间
        TextView profit_endtime;//结束时间
        TextView profit_amount;//金额
    }

    private String getPayFlag(String payflag) {
        //    0：未付
        //    1：现金分红
        //    2：红利再投
        //    PsnXpadReferProfitDetailQuery 这个接口的 payflag
        if ("0".equals(payflag)) {
            return mContext.getString(R.string.boc_trans_financial_fixedterm_payflagnull);
        } else if ("1".equals(payflag)) {
            return mContext.getString(R.string.boc_trans_financial_fixedterm_dispense_cash);
        } else if ("2".equals(payflag)) {
            return mContext.getString(R.string.boc_trans_financial_fixedterm_cashagin);
        }
        return payflag;
    }

}
