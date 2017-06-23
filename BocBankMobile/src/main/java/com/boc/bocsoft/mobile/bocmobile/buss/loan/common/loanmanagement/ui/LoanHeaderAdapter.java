package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.progressbar.RoundProgressBar;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model.LoanQuoteViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanQuoteViewModel;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;
import com.boc.bocsoft.mobile.framework.utils.ViewFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huixiaobo on 2016/9/8.
 * 头部区域条目
 */
public class LoanHeaderAdapter extends BaseAdapter {

    private Context context;
    /**字体大小*/
    public static final int TEXT_SIZE = 10;
    private List<EloanQuoteViewModel> mQuoteList;

    public LoanHeaderAdapter(Context context) {
        this.context = context;
        mQuoteList = new ArrayList<EloanQuoteViewModel>();
    }

    /**
     * 签约的中银E贷
     * @param quoteList
     */
    public void setLoanQuote(List<EloanQuoteViewModel> quoteList) {
        mQuoteList = quoteList;
    }


    @Override
    public int getCount() {
        return mQuoteList.size();
    }

    @Override
    public Object getItem(int position) {
        return mQuoteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.boc_userloan_item, parent, false);
                ViewFinder finder = new ViewFinder(convertView);
                viewHodler = new ViewHodler(finder);
                convertView.setTag(viewHodler);
            } else {
                viewHodler = (ViewHodler) convertView.getTag();
            }
            //判断取消、关闭、冻结状态
            if ("10".equals(mQuoteList.get(position).getQuoteState())) {
                viewHodler.loanStatusCancel.setVisibility(View.VISIBLE);
            } else if ("40".equals(mQuoteList.get(position).getQuoteState())) {
                viewHodler.loanStatusOff.setVisibility(View.VISIBLE);
            } else if ("20".equals(mQuoteList.get(position).getQuoteState())) {
                viewHodler.loanStatusfz.setVisibility(View.VISIBLE);
            }else {
                viewHodler.getVisibie();
            }

            if (mQuoteList != null && mQuoteList.size() > 0) {
                if ((!TextUtils.isEmpty(mQuoteList.get(position).getQuoteType())
                        &&"01".equals(mQuoteList.get(position).getQuoteType()))
                        || "1".equals(mQuoteList.get(position).getQuoteType())){

                    viewHodler.eloanTv.setText(context.getString(R.string.boc_loan_eloanw));
                    viewHodler.loanAmTv.setText(context.getString(R.string.boc_loan_amountmg));

                    viewHodler.loanNo.setText("（" +(getCurrency(mQuoteList.get(position).getCurrency()) + "）" +
                            MoneyUtils.transMoneyFormat(mQuoteList.get(position).getLoanBanlance(), mQuoteList.get(position).getCurrency())));
                    viewHodler.remindTv.setText(context.getString(R.string.boc_loan_overde));
                    viewHodler.remaindNo.setText(getCurrency(mQuoteList.get(position).getCurrency()) + " " +
                            MoneyUtils.transMoneyFormat(mQuoteList.get(position).getAvailableAvl(), mQuoteList.get(position).getCurrency()));
                    //显示百分比
                    int max = (int) (Math.rint(Float.parseFloat(mQuoteList.get(position).getAvailableAvl())/Float.parseFloat(mQuoteList.get(position).getLoanBanlance()) * 100));
                    viewHodler.amountBar.setCricleColor(R.color.boc_round_progressbar_gray);
                    viewHodler.amountBar.setProgress(max);
                    viewHodler.amountBar.setTextSize(ResUtils.dip2px(context, TEXT_SIZE));

                } else  if ((!TextUtils.isEmpty(mQuoteList.get(position).getQuoteType())
                        &&"02".equals(mQuoteList.get(position).getQuoteType()))
                        || "2".equals(mQuoteList.get(position).getQuoteType())){

                    viewHodler.eloanTv.setText(context.getString(R.string.boc_loan_eloanp));
                    viewHodler.loanAmTv.setText(context.getString(R.string.boc_loan_amountmg));
                    viewHodler.loanNo.setText("（" +(getCurrency(mQuoteList.get(position).getCurrency()) + "）" + " " +
                            MoneyUtils.transMoneyFormat(mQuoteList.get(position).getLoanBanlance(), mQuoteList.get(position).getCurrency())));
                    viewHodler.remindTv.setText(context.getString(R.string.boc_loan_overde));
                    viewHodler.remaindNo.setText(getCurrency(mQuoteList.get(position).getCurrency()) + " " +
                            MoneyUtils.transMoneyFormat (mQuoteList.get(position).getAvailableAvl(), mQuoteList.get(position).getCurrency()));
                    //显示百分比
                    int max = (int) (Math.rint(Float.parseFloat(mQuoteList.get(position).getAvailableAvl())/Float.parseFloat(mQuoteList.get(position).getLoanBanlance()) * 100));
                    viewHodler.amountBar.setCricleColor(R.color.boc_round_progressbar_gray);
                    viewHodler.amountBar.setProgress(max);
                    viewHodler.amountBar.setTextSize(ResUtils.dip2px(context, TEXT_SIZE));
                }
            }
        }
        return convertView;
    }

    /**
     * 币种格式化
     */
    private String getCurrency(String  currency) {
        String mCurrency = null;
        if (!TextUtils.isEmpty(currency)) {
            if (currency.equals("CNY")) {
                mCurrency = context.getString(R.string.boc_loan_mcurrency);
            } else {
                mCurrency = PublicCodeUtils.getCurrencyWithLetter(context, currency);
            }
        }
        return  mCurrency;
    }

    class ViewHodler {
        //贷款名称
        private TextView eloanTv;
        //状态关闭
        private TextView loanStatusOff;
        //逾期状态
        private TextView loanStatusOv;
        //取消状态
        private TextView loanStatusCancel;
        //冻结状态
        private TextView loanStatusfz;
        //额度描述
        private TextView loanAmTv;
        //总额度金额
        private TextView loanNo;
        //间隔线
        private View viewLine;
        //剩余额度描述
        private TextView remindTv;
        //剩余额度金额
        private TextView remaindNo;

        private RoundProgressBar amountBar;

        public ViewHodler(ViewFinder finder) {
            eloanTv = finder.find(R.id.eloanTv);
            loanStatusOff = finder.find(R.id.loanStatusOff);
            loanStatusOv = finder.find(R.id.loanStatusOv);
            loanStatusCancel = finder.find(R.id.loanStatusCancel);
            loanStatusfz = finder.find(R.id.loanStatusfz);
            loanAmTv = finder.find(R.id.loanAmTv);
            loanNo = finder.find(R.id.loanNo);
            viewLine = finder.find(R.id.viewLine);
            remindTv = finder.find(R.id.remindTv);
            remaindNo = finder.find(R.id.remaindNo);
            amountBar = finder.find(R.id.amountBar);
        }

        private void getVisibie() {
            loanStatusCancel.setVisibility(View.GONE);
            loanStatusOff.setVisibility(View.GONE);
            loanStatusfz.setVisibility(View.GONE);
        }
    }

}
