package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.expireproductlist.ExpireProductItemView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadDueProductProfitQueryViewModel;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import java.util.List;

/**
 * 已到期产品列表列表适配器（中银理财）
 * Created by zhx on 2016/9/17
 */
public class ExpireProductAdapter extends BaseAdapter {
    private Context context;
    List<XpadDueProductProfitQueryViewModel.DueProductEntity> dueProductList;

    public ExpireProductAdapter(Context context, List<XpadDueProductProfitQueryViewModel.DueProductEntity> viewList) {
        this.context = context;
        this.dueProductList = viewList;
    }

    public void setDueProductList(List<XpadDueProductProfitQueryViewModel.DueProductEntity> dueProductList) {
        this.dueProductList = dueProductList;
    }

    @Override
    public int getCount() {
        if (dueProductList != null && dueProductList.size() > 0) {
            return dueProductList.size();
        }
        return 0;
    }

    @Override
    public XpadDueProductProfitQueryViewModel.DueProductEntity getItem(int position) {
        return dueProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new ExpireProductItemView(context);
        }

        ExpireProductItemView expireProductItemView = (ExpireProductItemView) convertView;

        // 调节txtNameL的下边距
        TextView txtNameL = expireProductItemView.getTxtNameL();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) txtNameL.getLayoutParams();
        layoutParams.bottomMargin = (int) context.getResources().getDimension(R.dimen.boc_space_between_20px);
        txtNameL.setLayoutParams(layoutParams);

        expireProductItemView.isShowDividerLine(false);
        expireProductItemView.setTxtCenterName("实际收益", "投资本金", "到期日");
        XpadDueProductProfitQueryViewModel.DueProductEntity item = getItem(position);

        String currency = "[" + PublicCodeUtils.getCurrency(context, item.getProcur()) + "] "; // 币种
        String proName = item.getProname() + " "; // 产品名称
        String proCode = "(" + item.getProId() + ")"; // 产品代码
        if ("人民币元".equals(PublicCodeUtils.getCurrency(context, item.getProcur()))) {
            currency = "";
        }
        expireProductItemView.setTxtHeadCurrency(currency);
        expireProductItemView.setTxtHeadName(proName);
        expireProductItemView.setTxtHeadCode(proCode);

        expireProductItemView.setTxtHeadRight("");

        String payFlag = item.getPayFlag();
        String amountStr = MoneyUtils.getLoanAmountShownRMB1(item.getAmount(), item.getProcur());
        if("0".equals(payFlag)) { // 0未付(未付，前端显示“结算中”)
            expireProductItemView.setTxtCenterValue("结算中", amountStr, item.getEDate().format(
                    DateFormatters.dateFormatter1));
        } else if("1".equals(payFlag)) { // 1已付
            String payProfit = item.getPayProfit();
            String money = MoneyUtils.transMoneyFormat(payProfit, item.getProcur());
            expireProductItemView.setTxtCenterValue(setString(money), amountStr, item.getEDate().format(
                    DateFormatters.dateFormatter1));
        } else if("2".equals(payFlag)) { // 2：非理财系统入账（前端实际收益和实际收益率显示为“-”）
            expireProductItemView.setTxtCenterValue("-", amountStr, item.getEDate().format(
                    DateFormatters.dateFormatter1));
        }

        return convertView;
    }

    //设置实际收益显示格式
    public SpannableString setString(String string) {
        int start = string.indexOf(".");
        SpannableString spannableString = new SpannableString(string);
        if (start != -1) {
            int end = string.length();
            spannableString.setSpan(new AbsoluteSizeSpan(55), 0, start, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(new AbsoluteSizeSpan(45), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        } else {
            spannableString.setSpan(new AbsoluteSizeSpan(55), 0, string.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return spannableString;
    }
}
