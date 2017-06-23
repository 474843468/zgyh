package com.boc.bocsoft.mobile.bocmobile.buss.system.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.FundBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.PartialLoadView;
import com.boc.bocsoft.mobile.bocmobile.buss.common.product.ProductDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.util.InvestUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import java.math.BigDecimal;

/**
 * 频道页-基金item
 * Created by lxw on 2016/8/4 0004.
 */
public class FundItemView extends BaseItemView<FundBean> {

    private ViewGroup mRoot;
    private Context mContext;
    private TextView tvFundName;
    private TextView tvFundCode;

    // 货币基金
    private ViewGroup monetaryFundContainer;
    // 七日年化收益率
    private TextView tvFundIncomeRatio;
    // 万份收益
    private TextView tvFundIncomeUnit;
    // 起购金额
    private TextView tvMoneyOrderLowLimit;
    // 手续费率
    //private TextView tvMoneyChargeRate;

    // 其他类型基金
    private ViewGroup otherFundContainer;
    // 日增长率
    private TextView tvDayIncomeRatio;
    // 最新净值
    private TextView tvNetPrice;
    //重新请求
    private PartialLoadView iv_reload;
    // 起购金额
    private TextView tvOtherOrderLowLimit;
    // 手续费率
   // private TextView tvOtherChargeRate;

    // 净值截至日期
    private TextView tvEndDate;
    private TextView tvMoneyEndDate;

    private FundBean fundBean;

    private TextView tvFundType;

    public FundItemView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public FundItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public FundItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        mRoot = (ViewGroup) View.inflate(mContext, R.layout.boc_item_main_fund, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        // 货币基金
        monetaryFundContainer = (ViewGroup) mRoot.findViewById(R.id.monetaryFundContainer);
        // 七日年化收益率
        tvFundIncomeRatio = (TextView) mRoot.findViewById(R.id.tvFundIncomeRatio);
        // 万份收益
        tvFundIncomeUnit = (TextView) mRoot.findViewById(R.id.tvFundIncomeUnit);
        // 起购金额
        tvMoneyOrderLowLimit = (TextView) mRoot.findViewById(R.id.tvMoneyOrderLowLimit);
        // 手续费率
        //tvMoneyChargeRate = (TextView) mRoot.findViewById(R.id.tvMoneyChargeRate);

        // 其它类型基金
        otherFundContainer = (ViewGroup) mRoot.findViewById(R.id.otherFundContainer);
        // 日增长率
        tvDayIncomeRatio = (TextView) mRoot.findViewById(R.id.tvDayIncomeRatio);
        // 最新净值
        tvNetPrice = (TextView) mRoot.findViewById(R.id.tvNetPrice);
        // 起购金额
        tvOtherOrderLowLimit = (TextView) mRoot.findViewById(R.id.tvOtherOrderLowLimit);
        // 手续费率
        //tvOtherChargeRate = (TextView) mRoot.findViewById(R.id.tvOtherChargeRate);
        // 净值截至日期
        tvEndDate = (TextView) mRoot.findViewById(R.id.tvEndDate);
        tvFundName = (TextView) mRoot.findViewById(R.id.tvFundName);
        tvFundCode = (TextView)mRoot.findViewById(R.id.tvFundCode);
        tvNetPrice = (TextView) mRoot.findViewById(R.id.tvNetPrice);
        iv_reload = (PartialLoadView) mRoot.findViewById(R.id.iv_reload);

        tvFundType = (TextView) mRoot.findViewById(R.id.tvFundType);
        tvMoneyEndDate = (TextView)mRoot.findViewById(R.id.tvMoneyEndDate);
        iv_reload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PartialLoadView.LoadStatus loadStatus = iv_reload.getLoadStatus();
                if (loadStatus == PartialLoadView.LoadStatus.REFRESH) {
                    iv_reload.setLoadStatus(PartialLoadView.LoadStatus.LOADING);
                    if (mCallback != null) {
                        mCallback.onReload(itemData);
                    }
                }
            }
        });
        this.addView(mRoot, params);

        mRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(fundBean.getRefreshState())){
                    ProductDispatcher.dispatchToFund(ActivityManager.getAppManager().currentActivity(), fundBean.getFundCode());
                }
            }
        });
    }

    /**
     * 设置显示数据
     *
     * @param fund
     */
    @Override
    public void setData(FundBean fund) {
        super.setData(fund);
        fundBean = fund;
        if (fund != null) {
            String refreshState = fund.getRefreshState();
            if (refreshState.equals("0")) {
                iv_reload.setLoadStatus(PartialLoadView.LoadStatus.LOADING);
                tvFundType.setVisibility(View.GONE);
                tvFundType.setText("--");
            } else if (refreshState.equals("1")) {
                iv_reload.setLoadStatus(PartialLoadView.LoadStatus.SUCCESS);
                tvFundType.setVisibility(View.VISIBLE);
                tvFundType.setText(PublicCodeUtils.getFundType(mContext, fund.getFntype()));
            } else if (refreshState.equals("2")) {
                iv_reload.setLoadStatus(PartialLoadView.LoadStatus.REFRESH);
                tvFundType.setVisibility(View.GONE);
                tvFundType.setText("--");
            }

            tvFundName.setText(fund.getFundName());
            tvFundCode.setText("（" + fund.getFundCode() + "）");

            // 基金为货币或理财型时显示年化收益率
            if ("06".equals(fund.getFntype()) || "01".equals(fund.getFntype())) {
                // 7日年化收益率
                if (fund.getSevenDayYield() != null && !"".equals(fund.getSevenDayYield())) {
                    try {
                        BigDecimal showValue = new BigDecimal(fund.getSevenDayYield());
                        if (showValue.compareTo(new BigDecimal(0)) > 0) {
                            tvFundIncomeRatio.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_red));
                        } else if (showValue.compareTo(new BigDecimal(0)) < 0) {
                            tvFundIncomeRatio.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_green));
                        } else {
                            tvFundIncomeRatio.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_dark_gray));
                        }

                        String value = showValue.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%";
                        tvFundIncomeRatio.setText(value);
                    } catch (Exception ex) {
                        tvFundIncomeRatio.setText(fund.getSevenDayYield());
                    }

                } else {
                    tvFundIncomeRatio.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_dark_gray));
                    tvFundIncomeRatio.setText("--");
                }

                // 万份收益
                if (fund.getFundIncomeUnit() != null) {
                    tvFundIncomeUnit.setText(fund.getFundIncomeUnit().setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    tvFundIncomeUnit.setText("--");
                }
                // 起购金额
                if (fund.getOrderLowLimit() != null) {
                    tvMoneyOrderLowLimit.setText(InvestUtils.fundBoyLimitMoney(fund));
                }
                //
//                if (fund.getChargeRate() != null) {
//                    tvMoneyChargeRate.setText(getBuyType(fund) + fund.getChargeRate().toString() + "%");
//                }
                tvMoneyEndDate.setText(fund.getEndDate());
                monetaryFundContainer.setVisibility(View.VISIBLE);
                otherFundContainer.setVisibility(View.GONE);
            } else {
                // 日增长率
                if (fund.getDayIncomeRatio() != null) {
                    if (fund.getDayIncomeRatio().compareTo(new BigDecimal(0)) > 0) {
                        tvDayIncomeRatio.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_red));
                    } else if (fund.getDayIncomeRatio().compareTo(new BigDecimal(0)) < 0) {
                        tvDayIncomeRatio.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_green));
                    } else {
                        tvDayIncomeRatio.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_dark_gray));
                    }
                    tvDayIncomeRatio.setText(fund.getDayIncomeRatio().setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "%");
                } else {
                    tvDayIncomeRatio.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_dark_gray));
                    tvDayIncomeRatio.setText("--");
                }

                // 最新净值
                if (fund.getNetPrice() != null) {
                    tvNetPrice.setText(fund.getNetPrice().setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                } else{
                    tvNetPrice.setText("--");
                }
                // 起购金额
                if (fund.getOrderLowLimit() != null) {
                    tvOtherOrderLowLimit.setText(InvestUtils.fundBoyLimitMoney(fund));
                }
//                if (fund.getChargeRate() != null) {
//                    tvOtherChargeRate.setText(getBuyType(fund) + fund.getChargeRate().toString() + "%");
//                } else {
//                    tvOtherChargeRate.setText("--");
//                }
                tvEndDate.setText(fund.getEndDate());
                otherFundContainer.setVisibility(View.VISIBLE);
                monetaryFundContainer.setVisibility(View.GONE);
            }

        }


    }

    /**
     * 生成显示的基金名称
     * @param fundBean
     * @return
     */
    public String createFundNameForDisplay(FundBean fundBean){

        return fundBean.getFundName() + "(" + fundBean.getFundCode() + ")";
    }

    private String getBuyMoney(FundBean fundBean){

        String result = "--";
        if (fundBean != null) {

            if ("Y".equals(fundBean.getIsBuy())) {
                return fundBean.getOrderLowLimit().toString();
            } else if ("Y".equals(fundBean.getIsInvt())){
                return fundBean.getApplyLowLimit().toString();
            }
        }
        return result;
    }

    private String getBuyType(FundBean fundBean){
        String result = "";
        if (fundBean != null) {

            if ("Y".equals(fundBean.getIsBuy())) {
                return "认购";
            } else if ("Y".equals(fundBean.getIsInvt())){
                return "申购";
            }
        }
        return result;
    }
}
