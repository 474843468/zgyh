package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.financelist.FinanceListItemView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.ResultConvertUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 理财产品列表适配器
 * Created by liuweidong on 2016/9/18.
 */
public class WealthProductAdapter extends BaseAdapter {
    private Context mContext;
    private List<WealthListBean> list = new ArrayList<>();
    private boolean mIsLoginBeforeI;// 是否全国发行的理财产品列表接口

    public WealthProductAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<WealthListBean> list, boolean isLoginBeforeI) {
        this.list = list;
        this.mIsLoginBeforeI = isLoginBeforeI;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public WealthListBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.boc_fragment_wealth_product_item, null);
            holder.itemView = (FinanceListItemView) convertView.findViewById(R.id.list_item);
            holder.itemView.isShowDividerLine(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String[] names = getName(list.get(position));// 获取名称
        String[] values = getValue(list.get(position));// 获取值
        holder.itemView.setValueLAttributeDp(mContext.getResources().getColor(R.color.boc_text_color_red), 15);// 设置金额属性
        /*设置产品名称*/
        String currency = "";
        if (!ApplicationConst.CURRENCY_CNY.equals(list.get(position).getCurCode())) {
            currency = "[" + PublicCodeUtils.getCurrency(mContext, list.get(position).getCurCode()) + "]";
        }
        holder.itemView.setTxtHeadLeft(currency + list.get(position).getProdName());
        /*设置产品列表值*/
        holder.itemView.setTxtCenterName(names[0], names[1], names[2]);
        holder.itemView.setTxtCenterValue(values[0], values[1], values[2]);
        /*设置关注与额度紧张*/
        boolean isCollect = false;
        boolean isLimit = false;
        SharedPreferences preferences = mContext.getSharedPreferences(WealthDetailsFragment.FILE_NAME, Context.MODE_APPEND);
        String proCode = preferences.getString(WealthDetailsFragment.PROD_CODE + list.get(position).getProdCode(), "default");
        if (proCode.equals(list.get(position).getProdCode())) {
            isCollect = true;
        } else {
            isCollect = false;
        }

        if (!mIsLoginBeforeI) {// 登录后剩余额度判断
            if (list.get(position).getAvailableAmt() != null) {
                BigDecimal surplus = new BigDecimal(list.get(position).getAvailableAmt());// 剩余额度
                if (surplus.compareTo(BigDecimal.valueOf(10000000)) == -1 && surplus.compareTo(BigDecimal.valueOf(0)) != 0) {// 小于1000万额度紧张
                    isLimit = true;
                } else {
                    isLimit = false;
                }
                if (surplus.compareTo(BigDecimal.valueOf(0)) == 0) {// 售罄
                    holder.itemView.setImgBottom(true);
                } else {
                    holder.itemView.setImgBottom(false);
                }
            }
        }
        holder.itemView.setHeadRight(isCollect, isLimit);// 是否显示已关注与额度紧张
        return convertView;
    }

    private class ViewHolder {
        private FinanceListItemView itemView;
    }

    /**
     * 设置名称
     *
     * @param item
     * @return
     */
    private String[] getName(WealthListBean item) {
        String[] names = new String[3];
        switch (item.getIssueType()) {
            case WealthConst.PRODUCT_TYPE_1:// 现金管理类产品
            case WealthConst.PRODUCT_TYPE_3:// 固定期限产品
                names[0] = "预期年化收益率";
                break;
            case WealthConst.PRODUCT_TYPE_2:// 净值开放类产品
                names[0] = "单位净值";
                break;
            default:
                break;
        }
        if (!WealthConst.IS_LOCK_PERIOD_0.equals(item.getIsLockPeriod())) {
            names[0] = "业绩基准";
        }
        names[1] = "产品期限";
        if (mIsLoginBeforeI) {// 登录前接口
            names[2] = "起购金额";
        } else {// 登录后接口
            names[2] = "剩余额度";
        }
        return names;
    }

    /**
     * 设置数据
     *
     * @param item
     * @return
     */
    private String[] getValue(WealthListBean item) {
        String[] values = new String[3];
        switch (item.getIssueType()) {
            case WealthConst.PRODUCT_TYPE_1:// 现金管理类产品
            case WealthConst.PRODUCT_TYPE_3:// 固定期限产品
                values[0] = ResultConvertUtils.convertRate(item.getYearlyRR(), item.getRateDetail());// 预期年化收益率
                break;
            case WealthConst.PRODUCT_TYPE_2:// 净值开放类产品
                values[0] = MoneyUtils.getRoundNumber(item.getPrice(), 4, BigDecimal.ROUND_HALF_UP);// 单位净值
                break;
            default:
                break;
        }
        if (mIsLoginBeforeI) {// 登录前接口
            values[1] = ResultConvertUtils.convertDate(item.getProductKind(), item.getProdTimeLimit(), item.getIsLockPeriod(), item.getTermType());
            values[2] = MoneyUtils.getLoanAmountShownRMB1(item.getSubAmount(), item.getCurCode());// 起购金额
        } else {// 登录后接口
            values[1] = ResultConvertUtils.convertDate(item.getProductKind(), item.getPeriedTime(), item.getIsLockPeriod(), item.getTermType());
            BigDecimal surplus = new BigDecimal(item.getAvailableAmt());// 剩余额度
            if (surplus.compareTo(BigDecimal.valueOf(1000000000)) == 1) {
                values[2] = mContext.getString(R.string.boc_wealth_money_ample);// 额度充足
            } else {
                values[2] = MoneyUtils.formatMoney(item.getAvailableAmt());
            }
        }
        return values;
    }

}
