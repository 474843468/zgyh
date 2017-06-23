package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.financelist.FinanceListItemView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.model.FundFloatingProfileLossModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.model.FundPositionModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/7.
 */

public class FundPositionAdapter extends BaseAdapter {
    private List<FundFloatingProfileLossModel.ResultListBean> profileLessBean = new ArrayList<>();

    private List<FundPositionModel.FundBalanceBean> beans = new ArrayList<>();
    private Context mContext;

    public FundPositionAdapter(Context mContext) {
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
        FundPositionModel.FundBalanceBean bean = beans.get(position);
        if (bean == null) {
            return;
        }
        FinanceListItemView financeListItemView = (FinanceListItemView) convertView;
        financeListItemView.isShowDividerLine(false);
        financeListItemView.setTxtCenterColor(R.color.boc_text_color_common_gray);
        /*设置产品名称*/
        String currency = "";
        if (!ApplicationConst.CURRENCY_CNY.equals(bean.getFundInfo().getCurrency())) {
            currency = "[" + PublicCodeUtils.getCurrency(mContext, bean.getFundInfo().getCurrency()) + "]";
        }
        financeListItemView.setTxtHeadLeft(currency + bean.getFundInfo().getFundName() + " " + "(" + bean.getFundInfo().getFundCode() + ")");
        //快速赎回
        if ("Y".equals(bean.getFundInfo().getIsQuickSale())) {

        }
        String left = mContext.getString(R.string.boc_fund_position_current_value_without_unit);
        String midd = mContext.getString(R.string.boc_fund_position_share);
        String right = mContext.getString(R.string.boc_fund_position_profileless);
        financeListItemView.setTxtCenterName(left, midd, right);
        String leftValue = MoneyUtils.transMoneyFormat(bean.getCurrentCapitalisation(), bean.getFundInfo().getCurrency());
        String middValue = bean.getTotalBalance();
        String rightValue = getProfileLess(bean.getFundInfo().getFundCode());
        //right值为浮动盈亏市值，其来源需要查询035 浮动盈亏接口。
        financeListItemView.setValueLeft(leftValue,R.color.boc_text_color_dark_gray);
        financeListItemView.setValueCenter(middValue,R.color.boc_text_color_dark_gray);
        int rightColor = R.color.boc_text_color_dark_gray;
        if ("-".equals(rightValue)){
            rightColor = R.color.boc_text_color_dark_gray;
        }else if (rightValue.startsWith(DataUtils.NUM_POSITIVE_FLAG)){
            rightColor = R.color.boc_text_color_green;
        }else{
            rightColor = R.color.boc_text_money_color_red;
        }
        financeListItemView.setValueRight(rightValue,rightColor);

    }


    public List<FundFloatingProfileLossModel.ResultListBean> getProfileLessBean() {
        return profileLessBean;
    }

    public String getProfileLess(String fundCode) {
        String result = "-";
        if (profileLessBean == null || profileLessBean.size() <= 0) {
            return result;
        }
        if (StringUtil.isNullOrEmpty(fundCode)) {
            return result;
        }
        for (FundFloatingProfileLossModel.ResultListBean bean : profileLessBean) {
            if (fundCode.equals(bean.getFundCode())) {
                result = bean.getMiddleFloat();
            }
        }
        return result;
    }

    public void setProfileLessBean(List<FundFloatingProfileLossModel.ResultListBean> profileLessBean) {
        this.profileLessBean = profileLessBean;
        notifyDataSetChanged();
    }

    public List<FundPositionModel.FundBalanceBean> getBeans() {
        return beans;
    }

    public void setBeans(List<FundPositionModel.FundBalanceBean> beans) {
        if (beans == null){
            this.beans.clear();
        }else{
            this.beans = beans;
        }
        notifyDataSetChanged();
    }
}
