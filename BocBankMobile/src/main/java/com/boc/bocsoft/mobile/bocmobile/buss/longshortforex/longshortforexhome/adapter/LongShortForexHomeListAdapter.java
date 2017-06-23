package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.common.util.InvestUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.customModel.CustomLoginBeforeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.wfssQueryMultipleQuotation.WFSSQueryMultipleQuotationResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.utils.LongShortForexHomeCodeModelUtil;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;
import com.chinamworld.llbt.userwidget.NumberStyleTextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;


/**
 * 双向宝首页列表-列表适配器
 * Created by yx on 2016-12-14 17:21:52
 */
public class LongShortForexHomeListAdapter extends BaseListAdapter<CustomLoginBeforeModel> {
    private WFSSQueryMultipleQuotationResModel mOldData = null;
    /**
     * 涨跌幅和涨跌值切换 监听
     */
    private OnRightTextViewClickListener mListener;

    public LongShortForexHomeListAdapter(Context context) {
        super(context);
    }

    /**
     * 涨跌幅标识 默认显示涨跌幅
     */
    private boolean mIsCurrPercentDiff = true;//涨跌幅

    /**
     * 设置是否默认显示涨跌幅 true 涨跌幅，false  涨跌值
     *
     * @param iscurrPercentDiff
     */
    public void setIscurrPercentDiff(boolean iscurrPercentDiff) {
        mIsCurrPercentDiff = iscurrPercentDiff;
        notifyDataSetChanged();
    }

    /**
     * 设置旧数据，用于判断高亮显示
     *
     * @param wfssQueryMultipleQuotationResModelOld
     */
    public void setOldWfssData(WFSSQueryMultipleQuotationResModel wfssQueryMultipleQuotationResModelOld) {
        mOldData = wfssQueryMultipleQuotationResModelOld;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoldler holder = null;
        CustomLoginBeforeModel viewModel = getItem(position);
        if (convertView == null) {
            holder = new ViewHoldler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.long_short_forex_main_item, null);
            holder.detail = (LinearLayout) convertView.findViewById(R.id.detail);
            holder.tv_currency = (TextView) convertView.findViewById(R.id.tv_currency);
            holder.tv_sale = (NumberStyleTextView) convertView.findViewById(R.id.tv_sale);//卖出价
            holder.tv_buy = (NumberStyleTextView) convertView.findViewById(R.id.tv_buy);//买入价
            holder.tv_riseorfall = (TextView) convertView.findViewById(R.id.tv_riseorfall);//涨跌幅
            convertView.setTag(holder);

        } else {
            holder = (ViewHoldler) convertView.getTag();
        }
        // 得到源货币的代码
        String sourceCurrencyCode = viewModel.getPsnGetAllExchangeRatesOutlayResModel().getSourceCurrencyCode();
        String sourceDealCode = null;
        /** 得到目标货币代码*/
        String targetCurrencyCode = viewModel.getPsnGetAllExchangeRatesOutlayResModel().getTargetCurrencyCode();
        String targetDealCode = null;
        String temp = "";
        temp = InvestUtils.getCurrencyPairName(mContext, sourceCurrencyCode, targetCurrencyCode) + "";
        if (!StringUtils.isEmptyOrNull(temp)) {
            //货币对
            holder.tv_currency.setText(temp);
        } else {
            LogUtil.d("yx-------货币对-没有匹配上汉字---");
        }
        //卖出价
        String sellRate = viewModel.getPsnGetAllExchangeRatesOutlayResModel().getSellRate().toString();
        //买入价
        String buyRate = viewModel.getPsnGetAllExchangeRatesOutlayResModel().getBuyRate().toString();
        Integer sourceformatNumber = viewModel.getPsnGetAllExchangeRatesOutlayResModel().getSourceCurrency().getFraction();
        Integer targetformatNumber = viewModel.getPsnGetAllExchangeRatesOutlayResModel().getTargetCurrency().getFraction();
        sellRate = parseStringPattern(sellRate, sourceformatNumber);
        buyRate = parseStringPattern(buyRate, targetformatNumber);
        if (sellRate.contains(".")) {
            if (isLessThanTwo(sellRate)) {
                sellRate = sellRate + "0";
            }
            holder.tv_sale.setNumberText(sellRate, 2, "--");
        } else {
            holder.tv_sale.setNumberText(sellRate, 0, "--");
        }
        if (buyRate.contains(".")) {
            if (isLessThanTwo(buyRate)) {
                buyRate = buyRate + "0";
            }
            holder.tv_buy.setNumberText(buyRate, 2, "--");
        } else {
            holder.tv_buy.setNumberText(buyRate, 0, "--");
        }

        //显示涨跌幅 还是涨跌值
        if (viewModel.getItemsEntity() != null) {
            //今日涨跌幅    暂时确认 自选中的涨跌幅 从一个新接口中获取 需组装数据
            final String currPercentDiff = viewModel.getItemsEntity().getCurrPercentDiff() + "";
            //今日涨跌值
            final String currDiff = viewModel.getItemsEntity().getCurrDiff() + "";

            /** 根据涨跌标志，设置买入牌价、卖出牌价的背景图片 图片需重新修改*/
            int red = mContext.getResources().getColor(R.color.boc_titlebar_bg_red);
            int black = mContext.getResources().getColor(R.color.boc_text_color_common_gray);
            int green = mContext.getResources().getColor(R.color.boc_text_color_green);
            holder.tv_riseorfall.setText(showCurrpercentValue(mIsCurrPercentDiff, currPercentDiff, currDiff));
            int mChangeState = isHaveChange(currPercentDiff, sourceCurrencyCode + targetCurrencyCode + "");
            if (mChangeState != 0) {
                if (currPercentDiff.contains("+")) {//涨
                    //进行绿色高亮显示
                    startAnimation(convertView, green);
                    holder.tv_riseorfall.setBackgroundResource(R.drawable.boc_transaction_status_bg_green);//绿色背景
                } else if (currPercentDiff.contains("-")) {//跌
                    //进行红色高亮显示
                    startAnimation(convertView, red); //加载红色动画
                    holder.tv_riseorfall.setBackgroundResource(R.drawable.boc_loan_status_bg_red);//红色背景
                } else {
                    holder.tv_riseorfall.setBackgroundResource(R.drawable.boc_loan_status_bg_gray);//灰色背景
                }
            } else {//未变
                if (currPercentDiff.contains("+")) {//涨
                    holder.tv_riseorfall.setBackgroundResource(R.drawable.boc_transaction_status_bg_green);//绿色背景
                } else if (currPercentDiff.contains("-")) {//跌
                    holder.tv_riseorfall.setBackgroundResource(R.drawable.boc_loan_status_bg_red);//红色背景
                } else {//灰色背景
                    holder.tv_riseorfall.setBackgroundResource(R.drawable.boc_loan_status_bg_gray);//灰色背景
                }
            }
        } else {
            holder.tv_riseorfall.setBackgroundResource(R.drawable.boc_loan_status_bg_gray);
            holder.tv_riseorfall.setText(showCurrpercentValue(mIsCurrPercentDiff, "", ""));

        }

        holder.tv_riseorfall
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            mListener.onRightTextViewClick(mIsCurrPercentDiff);
                        }
                    }
                });

        return convertView;
    }

    /**
     * 涨跌值和涨跌幅 判断显示
     *
     * @param iscurrPercentDiff 是否是显示涨跌幅
     * @param currPercentDiff   涨跌幅值
     * @param currDiff          涨跌值 值
     * @return
     */
    private String showCurrpercentValue(boolean iscurrPercentDiff, String currPercentDiff, String currDiff) {
        if (iscurrPercentDiff) {
            if (!StringUtils.isEmptyOrNull(currPercentDiff)) {
                return currPercentDiff;
            } else {
                return "--";
            }
        } else {
            if (!StringUtils.isEmptyOrNull(currDiff)) {
                return currDiff;
            } else {
                return "--";
            }
        }
    }

    /**
     * 涨跌幅 跌时动画显示
     */
    public void startAnimation(final View view, final int colorID) {
        //创建动画,这里的关键就是使用 ArgbEvaluator, 后面2个参数就是 开始的颜色,和结束的颜色.
        final ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorID, Color.WHITE);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();//之后就可以得到动画的颜色了
                view.setBackgroundColor(color);//设置一下, 就可以看到效果.
            }
        });
        colorAnimator.setDuration(200);
        colorAnimator.start();
    }

    public static String parseStringPattern(String text, int scale) {
        if (!StringUtils.isEmptyOrNull(text)) {
            if (!text.contains(",") && !text.contains("，")) {
                if (text.matches("^.*[.].*[.].*$")) {
                    return text;
                } else {
                    String temp = "#######################0";
                    if (scale > 0) {
                        temp = temp + ".";
                    }
                    for (int e = 0; e < scale; ++e) {
                        temp = temp + "0";
                    }

                    try {
                        DecimalFormat var6 = new DecimalFormat(temp);
                        BigDecimal d = new BigDecimal(text);
                        return var6.format(d).toString();
                    } catch (Exception var5) {
                        return text;
                    }
                }
            } else {
                return text;
            }
        } else {
            return "-";
        }
    }

    /**
     * 通过货币对 查找涨跌幅值 是否有有变化
     *
     * @param currPercentDiff 新数据的涨跌幅
     * @param currencyPairs   货币对
     * @return
     */
    private int isHaveChange(String currPercentDiff, String currencyPairs) {
        if (mOldData == null || PublicUtils.isEmpty(mOldData.getItems())) {
            return 0;
        } else {//
            for (int i = 0; i < mOldData.getItems().size(); i++) {
                String msourceCurrencyCode = mOldData.getItems().get(i).getSourceCurrencyCode();
                String mtargetCurrencyCode = mOldData.getItems().get(i).getTargetCurrencyCode();
                if (currencyPairs.equalsIgnoreCase(msourceCurrencyCode + mtargetCurrencyCode + "")) {
                    return LongShortForexHomeCodeModelUtil.compareBigDecimal(currPercentDiff, mOldData.getItems().get(i).getCurrPercentDiff());
                }
            }
            return 0;
        }
    }

    class ViewHoldler {
        LinearLayout detail;
        TextView tv_currency;
        NumberStyleTextView tv_sale;
        NumberStyleTextView tv_buy;
        TextView tv_riseorfall;
    }

    /**
     * 实时回调接口监听{显示涨跌幅或者涨跌值}
     */
    public interface OnRightTextViewClickListener {
        /**
         * 点击实时监听 设置是否默认显示涨跌幅 true 涨跌幅，false  涨跌值
         *
         * @param isShowCurrPercentDiff
         */
        abstract void onRightTextViewClick(boolean isShowCurrPercentDiff);
    }

    /**
     * 实时回调接口监听{显示涨跌幅或者涨跌值} 方法实现
     */
    public void setOnRightTextViewClickListener(OnRightTextViewClickListener mListener) {
        this.mListener = mListener;

    }

    /**
     * @param strContent
     * @return
     */
    private boolean isLessThanTwo(String strContent) {
        if (getDecimalPointBackNumber(strContent) < 2) {
            return true;
        }
        return false;
    }

    /**
     * 字符串小数点后的长度
     *
     * @param str
     * @return
     */
    private int getDecimalPointBackNumber(String str) {
        return str.substring(str.lastIndexOf("."), str.length()).replaceAll(".", "").length();
    }
}
