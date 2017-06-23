package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.financelist.FinanceListItemView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.model.StatementModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huixiaobo on 2016/11/22.
 * 持仓信息适配器
 */
public class StatementAdapter  extends BaseAdapter {

    private List<StatementModel.ListBean> beans = new ArrayList<>();
    private Context mContext;

    public StatementAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return beans == null ? 0 : beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            FinanceListItemView financeListItemView = new FinanceListItemView(mContext);
            convertView = financeListItemView;
        }
        disPlayView(position, convertView);
        return convertView;
    }

    private void disPlayView(int position, View convertView) {
        if (position < 0 || beans == null || position >= beans.size()) {
            return;
        }
        StatementModel.ListBean bean = beans.get(position);
        if (bean == null) {
            return;
        }
        FinanceListItemView financeListItemView = (FinanceListItemView) convertView;
        /*设置产品名称*/
        String currency = "";
        if (!ApplicationConst.CURRENCY_CNY.equals(bean.getCurrency())) {
            currency = "[" + PublicCodeUtils.getCurrency(mContext, bean.getCurrency()) + "]";
        }
        financeListItemView.setTxtHeadLeft(currency + bean.getFundName() + " " + "(" + bean.getFundCode() + ")");

        String left = mContext.getString(R.string.boc_fund_position_current_value);
        String midd = mContext.getString(R.string.boc_fund_position_share);
        String right = mContext.getString(R.string.boc_fund_position_profileless);
        financeListItemView.setTxtCenterName(left, midd, right);
        String leftValue = MoneyUtils.transMoneyFormat(bean.getMarketValue(), bean.getCurrency());
        String middValue = bean.getTotalBalance().toString();
        String rightValue = bean.getFloatLoss().toString();
        //right值为浮动盈亏市值，其来源需要查询035 浮动盈亏接口。
        financeListItemView.setTxtCenterValue(leftValue, middValue, rightValue);

    }

    public List<StatementModel.ListBean> getBeans() {
        return beans;
    }

    public void setBeans(List<StatementModel.ListBean> beans) {
        this.beans = beans;
        notifyDataSetChanged();
    }
}
