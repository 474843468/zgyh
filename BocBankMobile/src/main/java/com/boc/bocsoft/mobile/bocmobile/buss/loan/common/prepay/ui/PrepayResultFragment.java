package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView.BtnCallback;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom.HomeBtnCallback;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanManagerFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepaySubmitRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.math.BigDecimal;

/**
 * 提前还款结果界面
 * Created by qiangchen on 2016/7/25.
 */
public class PrepayResultFragment extends BussFragment implements BtnCallback, HomeBtnCallback {

    private View mRoot;
    private OperationResultHead.Status status;
    protected LinearLayout ll_content;
    protected OperationResultBottom bt_backresult;
    private PrepaySubmitRes prepaySubmitRes;
    //还款本金
    private String mAdvanceRepayCapital = "";
    //还款利息
    private String mAdvanceRepayInterest = "";
    //结果页
    private BaseOperationResultView borvResult;
    //还款总额是否需要添加 手续费
    private boolean isaddCharges = false;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_eloan_prepay_results, null);
        return mRoot;
    }

    @Override
    public void initView() {
//      ll_content = (LinearLayout) mRoot.findViewById(R.id.ll_content);
//      bt_backresult = (OperationResultBottom) mRoot.findViewById(R.id.bt_backresult);
//
//      tv_look= (TextView) mRoot.findViewById(R.id.tv_look);
//      operationResultHead= (OperationResultHead) mRoot.findViewById(R.id.orh_head);
//      tv_look.setOnClickListener(this);

        borvResult = (BaseOperationResultView) mContentView.findViewById(R.id.rv_result);
        borvResult.findViewById(R.id.txt_title).setVisibility(View.GONE);
    }

    public void setStatus(OperationResultHead.Status status) {
        this.status = status;
    }

    /**
     * 传递  还款本金和还款利息 value
     *
     * @param mStr1
     * @param mStr2
     * @param  isaddCharges
     */
    public void setRepayCapitalAndInterest(String mStr1, String mStr2,boolean isaddCharges) {
        this.mAdvanceRepayCapital = mStr1;
        this.mAdvanceRepayInterest = mStr2;
        this.isaddCharges = isaddCharges;
    }

    @Override
    protected void titleLeftIconClick() {
//        super.titleLeftIconClick();
//    	 EloanStatusFragment.isStartPage=true;
//    	 popTo(EloanStatusFragment.class,false);

        popToAndReInit(LoanManagerFragment.class);
//    	start(new EloanStatusFragment());
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_draw_operatingresults_pagename);
//        return getString(R.string.boc_eloan_prepayResult);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void initData() {

        prepaySubmitRes = (PrepaySubmitRes) getArguments().getSerializable(EloanConst.ELON_PREPAY_COMMIT);
        borvResult.updateHead(status, "还款成功");
        borvResult.setDetailsName("交易详情");
        if(isaddCharges){
            BigDecimal b1 = new BigDecimal(prepaySubmitRes.getRepayAmount().toString());//本息合计
            BigDecimal b2 = new BigDecimal(prepaySubmitRes.getCharges().toString()); //手续费
            b2 = b2.setScale(2, BigDecimal.ROUND_HALF_UP);
            String totalMoney = (b2.add(b1)).toString();
            borvResult.addDetailRow("还款总额", PublicCodeUtils.getCurrencyWithLetter(mContext,prepaySubmitRes.getCurrency())+" " + MoneyUtils.transMoneyFormat(totalMoney, prepaySubmitRes.getCurrency()), true, false);
        }else{
            borvResult.addDetailRow("还款总额", PublicCodeUtils.getCurrencyWithLetter(mContext,prepaySubmitRes.getCurrency())+" " + MoneyUtils.transMoneyFormat(prepaySubmitRes.getRepayAmount().toString(), prepaySubmitRes.getCurrency()), true, false);
        }
        borvResult.addDetailRow("还款本金", "" + mAdvanceRepayCapital, true, false);
        borvResult.addDetailRow("还款利息", "" + mAdvanceRepayInterest, true, false);
        if(isaddCharges){
            borvResult.addDetailRow("手续费", MoneyUtils.transMoneyFormat(prepaySubmitRes.getCharges(), prepaySubmitRes.getCurrency()), true, false);
        }else{
            if("027".equalsIgnoreCase(prepaySubmitRes.getCurrency())||"JPY".equalsIgnoreCase(prepaySubmitRes.getCurrency())){
                borvResult.addDetailRow("手续费", "0", true, false);
            }else{
                borvResult.addDetailRow("手续费", "0.00", true, false);
            }
        }

        borvResult.addDetailRow("还款账户", NumberUtils.formatCardNumber(prepaySubmitRes.getFromAccount()), true, false);
        borvResult.addDetailRow("还款后\n剩余金额", MoneyUtils.transMoneyFormat(prepaySubmitRes.getAfterRepayRemainAmount(), prepaySubmitRes.getCurrency()), false, true);

//        borvResult.addDetailRow("提前还款金额","人民币元 "+MoneyUtils.transMoneyFormat(prepaySubmitRes.getRepayAmount(), "021"));
//        borvResult.addDetailRow("手续费", MoneyUtils.transMoneyFormat(prepaySubmitRes.getCharges(), "021"));
//        borvResult.addDetailRow("还款账户", NumberUtils.formatCardNumber(prepaySubmitRes.getFromAccount()));
//        borvResult.addDetailRow("还款后剩余本金", MoneyUtils.transMoneyFormat(prepaySubmitRes.getAfterRepayRemainAmount(), "021"));
//        


//        borvResult.addDetailRow("还款后每期还款额", MoneyUtils.transMoneyFormat(prepaySubmitRes.getAfterRepayissueAmount(), "021"));
//        borvResult.addDetailRow("贷款品种/账号", "中银E贷/"+NumberUtils.formatCardNumberStrong(prepaySubmitRes.getLoanAccount()));//???
//        borvResult.addDetailRow("贷款金额/币种", MoneyUtils.transMoneyFormat(prepaySubmitRes.getLoanAmount(), "021")+"/人民币元");
//        borvResult.addDetailRow("期限/期限单位", prepaySubmitRes.getLoanPeriod()+"/月");
//        borvResult.addDetailRow("到期日", prepaySubmitRes.getLoanToDate());

//        OperationResultHead operationResultHead
//                = new OperationResultHead(mContext);
//        DetailContentView detailContentView = new DetailContentView(mContext);
//
//          operationResultHead.updateData(status, "还款成功！");
//		  detailContentView.addDetailRow("收款账户", NumberUtils.formatCardNumber(prepaySubmitRes.getFromAccount()));
//		  detailContentView.addDetailRow("手续费", MoneyUtils.transMoneyFormat(prepaySubmitRes.getCharges(), "021"));
//	      detailContentView.addDetailRow("提前还款金额", MoneyUtils.transMoneyFormat(prepaySubmitRes.getRepayAmount(), "021"));
//	      detailContentView.addDetailRow("还款后本金", MoneyUtils.transMoneyFormat(prepaySubmitRes.getAfterRepayRemainAmount(), "021"));
//	      detailContentView.addDetailRow("还款后每期还款额", MoneyUtils.transMoneyFormat(prepaySubmitRes.getAfterRepayissueAmount(), "021"));
//	      detailContentView.addDetailRow("贷款品种/账号", "中银e贷/"+NumberUtils.formatCardNumberStrong(prepaySubmitRes.getLoanAccount()));
//	      detailContentView.addDetailRow("贷款金额/币种", MoneyUtils.transMoneyFormat(prepaySubmitRes.getLoanAmount(), "021"));
//	      detailContentView.addDetailRow("期限/期限单位", prepaySubmitRes.getLoanPeriod()+"个月");
//	      detailContentView.addDetailRow("到期日", prepaySubmitRes.getLoanToDate());
//        
//        bt_backresult.updateButton("返回首页");
////        ll_content.addView(operationResultHead);
//        ll_content.addView(detailContentView);
    }

    @Override
    public void setListener() {
//        bt_backresult.setgoHomeOnclick(new OperationResultBottom.HomeBtnCallback() {
//            @Override
//            public void onHomeBack() {
//                popTo(EloanStatusFragment.class,false);
////            	start(new EloanStatusFragment());
//            }
//        });
        borvResult.setOnclick(this);
        borvResult.setgoHomeOnclick(this);
    }

    @Override
    public void onHomeBack() {
//    	 EloanStatusFragment.isStartPage=true;
//		 popTo(EloanStatusFragment.class,false);

        ModuleActivityDispatcher.popToHomePage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClickListener(View v) {

    }

//  @Override
//  public boolean onBack() {
//      mActivity.finish();
//      return super.onBack();
//  }
}
