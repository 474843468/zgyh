package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan;

/**
 * 中银E贷常量类
 * Created by lxw4566 on 2016/6/23.
 */
public class EloanConst {
    /**用款传递对象*/
    public static final String ELON_DRAW = "EloanDrawModel";
    /**中银E贷对象*/
    public static final String ELON_QUOTE = "LoanQuoteViewModel";
    /**用户激活*/
    public static final String ELON_APPLY = "EloanApplyModel";
    /**用户用款确认页*/
    public static final String ELON_DRAW_COMMIT = "LOANCycleLoanEApplySubmitReq";
    /**变更还款账户*/
    public static final String ELON_ACCOUNT = "EloanAccountModel";
    /**提前还款*/
    public static final String ELON_PREPAY = "EaccountModel";
    /**提前还款确认页*/
    public static final String ELON_PREPAY_COMMIT = "PrepaySubmitRes";
    /**还款详情*/
    public static final String ELON_PREPAY_DETAIL = "ErepayDetailModel";
    /**额度详情传递对象*/
    public static final String ELOAN_QUOTA = "EloanQuoteModel";
    /**签约类型*/
    public static final String ELOAN_QUOTETYPE = "QuoteType";
    /**贷款账户*/
    public static final String LOAN_ACCOUNT_NUM = "loannum";
    /**结清标识*/
    public static final String ELOAN_REPAYFLAG= "RepayRecord";
    /**电话号码*/
    public static final String LOAN_MOBILE = "mobile";
    /**服务器时间*/
    public static final String DATA_TIME = "dataTime";
    /**还款账号*/
    public static final String PEPAY_ACCOUNT = "payAccount";
    /**操作员信息Id*/
    public static final String  OPETATOR_ID = "operatorId";
    /**详情对象*/
    public static final String LOAN_DRAWDETA = "EloanDrawDetailModel";
    
    public static final String LOAN_REPAYMNETHIT = "repayStatus";
    /** 提交按钮 防暴力点击时间间隔 */
    public static final long CLICK_MORE_TIME = 2000;
    /**还款总额信息标识*/
    public static final String ELOAN_REPAY_TOTAL = "repayTotal";
    /**服务器时间*/
    public static final String DEFAULT_PAGE_INDEX = "defaultpageindex";
    /**额度编号*/
    public static final String LOAN_QUOTENO = "quoteNo";
    /**提前还款结果*/
    public static final String LOAN_PREPY_RESULT = "prepayresult";
    /**提前还款手续费*/
    public static final String LOAN_PREPAY_CHARGES = "prepay_charges";
    /**账户可用余额*/
    public static final String LOAN_PREPAY_AVLAMOUNT = "available_amount";
    /**用款详情跳转页*/
    public static final String LOAN_PREPAY_DETAIL = "drawDetail";
    /**币种*/
    public static final String LOAN_PREPAY_CURRENCYCODE = "currency_code";
    /**还款周期*/
    public static final String LOAN_REPAY_PERIOD = "repay_period";
    /**提款金额每笔提取的金额下线*/
    public static final String ELOAN_DRAWAMOUNT = "1000";
    /**还款方式*/
    public static final String LOAN_REPAYTYPE = "repaytype";


}
