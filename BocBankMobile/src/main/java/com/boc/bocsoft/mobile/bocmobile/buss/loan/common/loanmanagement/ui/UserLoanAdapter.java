package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.progressbar.RoundProgressBar;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model.LoanAccountListModel;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;
import com.boc.bocsoft.mobile.framework.utils.ViewFinder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huixiaobo on 2016/8/23.
 * 适配器
 */
public class UserLoanAdapter extends BaseAdapter{

    private Context mContext;
    /**052贷款产品*/
    private List<LoanAccountListModel.PsnLOANListEQueryBean> mLoanAccountList;
    /**字体大小*/
    public static final int TEXT_SIZE = 10;

    public UserLoanAdapter(Context context) {
         mContext = context;
         mLoanAccountList = new ArrayList<LoanAccountListModel.PsnLOANListEQueryBean>();
    }

    /**
     * 贷款列表数据
     * @param loanList
     */
    public void setLoanAccountList (List<LoanAccountListModel.PsnLOANListEQueryBean> loanList) {
        mLoanAccountList = loanList;
    }

    @Override
    public int getCount() {
        return mLoanAccountList.size();
    }

    @Override
    public Object getItem(int position) {
        return mLoanAccountList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        String remaindAut = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.boc_userloan_item, parent, false);
            ViewFinder finder = new ViewFinder(convertView);
            viewHodler = new ViewHodler(finder);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        //循环类贷款
        if (mLoanAccountList != null && mLoanAccountList.size() >0) {
             if (!TextUtils.isEmpty(mLoanAccountList.get(position).getCycleType())
                     && mLoanAccountList.get(position).getCycleType ().equals("R")) {
                 viewHodler.getVisible();
                 if ((!TextUtils.isEmpty(mLoanAccountList.get(position).getLoanType())
                         && mLoanAccountList.get(position).getLoanType().equals("1046"))
                         || mLoanAccountList.get(position).getLoanType().equals("1047")) {
                     viewHodler.eloanTv.setText(mContext.getString(R.string.boc_loan_eloanp));
                 } else {
                     String loanType = PublicCodeUtils.getLoanTypeName(mContext,
                             mLoanAccountList.get(position).getLoanType());
                     viewHodler.eloanTv.setText(loanType);
                 }
                 //逾期信息
                 if ("01".equals(mLoanAccountList.get(position).getOverDueStat())) {
                     viewHodler.loanStatusOv.setVisibility(View.VISIBLE);
                 }
                 viewHodler.loanAmTv.setText(mContext.getString(R.string.boc_loan_amountmg));
                 //总额
                 viewHodler.loanNo.setText("（" +getCurrency(mLoanAccountList.get(position).getCurrencyCode()) +"）"+ " " +
                         MoneyUtils.transMoneyFormat(mLoanAccountList.get(position).getLoanCycleAppAmount(),mLoanAccountList.get(position).getCurrencyCode()));
                 viewHodler.remindTv.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_dark_gray));
                 viewHodler.remindTv.setText(mContext.getString(R.string.boc_loan_overde));
                 viewHodler.remaindNo.setVisibility(View.VISIBLE);
                 viewHodler.remaindNo.setText(getCurrency(mLoanAccountList.get(position).getCurrencyCode()) + " " +
                         MoneyUtils.transMoneyFormat(mLoanAccountList.get(position).getLoanCycleAvaAmount(),
                                 mLoanAccountList.get(position).getCurrencyCode()));
                 //显示比列
                 int max = (int)(Math.rint(Float.parseFloat(mLoanAccountList.get(position).getLoanCycleAvaAmount())/Float.parseFloat(mLoanAccountList.get(position).getLoanCycleAppAmount()) * 100));
                 viewHodler.amountBar.setCricleColor(R.color.boc_round_progressbar_gray);
                 viewHodler.amountBar.setProgress(max);
                 viewHodler.amountBar.setTextSize(ResUtils.dip2px(mContext, TEXT_SIZE));

             } //非循环类贷款
             else {
                 viewHodler.getVibity();
                 //逾期信息
                 if ("01".equals(mLoanAccountList.get(position).getOverDueStat())) {
                     viewHodler.loanStatusOv.setVisibility(View.VISIBLE);
                 }
                 String loanType = PublicCodeUtils.getLoanTypeName(mContext,
                         mLoanAccountList.get(position).getLoanType());
                 viewHodler.eloanTv.setText(loanType);
                 viewHodler.remindTv.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_common_gray));
                 //贷款总额
                 viewHodler.remaindNo.setVisibility(View.GONE);
                 viewHodler.remindTv.setText(mContext.getString(R.string.boc_loan_amountName) + " "
                         +"（"+getCurrency(mLoanAccountList.get(position).getCurrencyCode())+"）"+ " " +
                         MoneyUtils.transMoneyFormat(mLoanAccountList.get(position).getLoanCycleAdvVal(), mLoanAccountList.get(position).getCurrencyCode()));
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
                mCurrency = mContext.getString(R.string.boc_loan_mcurrency);
            } else {
                mCurrency = PublicCodeUtils.getCurrencyWithLetter(mContext, currency);
            }
        }
        return  mCurrency;
    }

    /**
     * 试算方法
     * @param loanCycleAppAmount 核准金额
     * @param loanCycleBalance 贷款余额
     */
    private String getAmount(String loanCycleAppAmount, String loanCycleBalance) {
        BigDecimal amount = new BigDecimal(loanCycleAppAmount);
        BigDecimal balance = new BigDecimal(loanCycleBalance);
        String remaindAmount = amount.subtract(balance).toString();
        return remaindAmount;

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
        private  TextView loanStatusfz;
        //总额度布局
        private LinearLayout loanApplyAt;
        //总额度
        private TextView loanAmTv;
        //额度金额
        private TextView loanNo;
        //间隔线
        View viewLine;
        TextView remindTv;
        TextView remaindNo;
        RoundProgressBar amountBar;

       public ViewHodler(ViewFinder finder){
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
           loanApplyAt = finder.find(R.id.loanApplyAt);
       }
        private void getVibity() {
            loanStatusOff.setVisibility(View.GONE);
            loanStatusOv.setVisibility(View.GONE);
            loanStatusCancel.setVisibility(View.GONE);
            loanStatusfz.setVisibility(View.GONE);
            loanApplyAt.setVisibility(View.GONE);
            amountBar.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
        }

        private void getVisible() {
            loanApplyAt.setVisibility(View.VISIBLE);
            amountBar.setVisibility(View.VISIBLE);
            viewLine.setVisibility(View.VISIBLE);
        }
    }

}
