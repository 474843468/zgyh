package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.container.MvpContainer;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.widget.assignment.SelectAgreementView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyruler.MoneyRulerWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.presenter.InvestPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.ValidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestConst;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundBalanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundCompanyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.ui.InvestInfoFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huixiaobo on 2016/12/16.
 * 定投申请
 */
public class InvestFragmentView extends MvpContainer<InvestPresenter> implements InvestContract.InvestView,BaseView<InvestPresenter>,
        MoneyRulerWidget.MoneyRulerScrollerListener{

    protected View rootView;
    protected TextView fundName;
    protected TextView fundCode;
    protected TextView fundConpany;
    protected ImageView redeemImage;
    protected TextView redeemText;
    protected LinearLayout redeemBox;
    protected ImageView purchaseImage;
    protected TextView purchaseText;
    protected LinearLayout purchaseBox;
    protected EditChoiceWidget fundSttime;
    protected MoneyRulerWidget moneyRulerWidget;
    protected EditChoiceWidget fundEndFlag;
    protected EditChoiceWidget fundEndContent;
    protected SelectAgreementView viewAgreement;
    protected EditMoneyInputWidget funInvestContent;
    protected RelativeLayout invesTrans;
    protected EditChoiceWidget investUpDate;
    protected Button btnNext;
    private BussFragment investFragment;
    private Context mContext;
    //最大查询起始日期为一年
    private final static int MAX_QUERY_DATE = 12;
    /**每月选择标识*/
    private static final String MONTHS_FLAG = "0";
    /**每周选择标识*/
    private static final String WEEK_FLAG = "1";
    /**指定条件标识*/
    private static final String DING_FLAG = "2";
    /**选中周和月按钮标识*/
    private String timeInvestType = "0";
    /**有效定申传值对象*/
    private ValidinvestModel.ListBean mBuyData;
    /**结束条件上传值*/
    private int endPosition = 0;
    /**周期时间上传字段*/
    private String sunPosition;
    /**基金持仓信息*/
    private FundBalanceModel mBalance;
    /**基金公司信息*/
    private FundCompanyModel mFundcompany;
    /**是否是初次实现view*/
    private boolean isInestView;
    /**基金编码*/
    private String mFundcode;
    /**定申修改数据*/
    private boolean isInvestUpData;
    /**015返回基金对象*/
    private FundBalanceModel.FundBalanceBean.FundInfoBean fundInfo;
    /**弹出时间选择器*/
    private SelectStringListDialog dateMothsDialog;


    public InvestFragmentView(Context context) {
        this(context,null, 0);
    }

    public InvestFragmentView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public InvestFragmentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }


    @Override
    protected View createContentView() {
        rootView = inflate(getContext(),R.layout.boc_invest_apply_fragment, null);
        isInestView = true;
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        fundName = (TextView) rootView.findViewById(R.id.fundName);
        fundCode = (TextView) rootView.findViewById(R.id.fundCode);
        fundConpany = (TextView) rootView.findViewById(R.id.fundConpany);
        invesTrans = (RelativeLayout) rootView.findViewById(R.id.invesTrans);
        investUpDate = (EditChoiceWidget) rootView.findViewById(R.id.investUpDate);
        investUpDate.setArrowImageGone(false);
        investUpDate.setBottomLineVisibility(true);
        redeemImage = (ImageView) rootView.findViewById(R.id.redeem_image);
        redeemText = (TextView) rootView.findViewById(R.id.redeem_text);
        redeemBox = (LinearLayout) rootView.findViewById(R.id.redeem_box);
        purchaseImage = (ImageView) rootView.findViewById(R.id.purchase_image);
        purchaseText = (TextView) rootView.findViewById(R.id.purchase_text);
        purchaseBox = (LinearLayout) rootView.findViewById(R.id.purchase_box);
        fundSttime = (EditChoiceWidget) rootView.findViewById(R.id.fundSttime);
        moneyRulerWidget = (MoneyRulerWidget) rootView.findViewById(R.id.money_ruler_widget);
        fundEndFlag = (EditChoiceWidget) rootView.findViewById(R.id.fundEndFlag);
        fundEndFlag.setBottomLineVisibility(true);
        fundEndContent = (EditChoiceWidget) rootView.findViewById(R.id.fundEndContent);
        fundEndContent.setBottomLineVisibility(true);
        viewAgreement = (SelectAgreementView) rootView.findViewById(R.id.view_agreement);
        funInvestContent = (EditMoneyInputWidget) rootView.findViewById(R.id.funInvestContent);
        //investFragment = this.mBussFragment;
        btnNext = (Button) rootView.findViewById(R.id.btn_next);
    }


    @Override
    public void attach(Fragment fragment) {
        super.attach(fragment);
        mBussFragment = (BussFragment) fragment;
    }

    /**定申页面传值*/
    public void setFundCode(String fundCode) {
        mFundcode = fundCode;
    }

    /**定申修改页面传值*/
    public void setInvestBuyData(ValidinvestModel.ListBean investBuyData) {
        mBuyData = investBuyData;
        if (mBuyData != null && mBuyData.isInvestUpdate()) {
            isInvestUpData = true;
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isInestView && isVisibleToUser) {
            showLoadingDialog();
            if (isInvestUpData) {
                getPresenter().queryFundCompany(mBuyData.getFundCode());
            } else {
                getPresenter().queryFundCompany(mFundcode);
            }
        }
    }

    @Override
    public void initData() {
        super.initData();
        setUserVisibleHint(true);
        pagerData();
    }

    /**页面传值*/
    private void pagerData() {
        if (isInvestUpData) {
            invesTrans.setVisibility(GONE);
            investUpDate.setVisibility(VISIBLE);
            investUpDate.setChoiceTextContent(DataUtils.getDtdsFlag(mBuyData.getDtdsFlag()));
            fundSttime.setChoiceTextContent(mBuyData.getSubDate());
            fundName.setText(mBuyData.getFundName());
            fundCode.setText(mBuyData.getFundCode());
            fundConpany.setText(mBuyData.getFundInfo().getFundCompanyName());
            moneyRulerWidget.initMoneyRuler(200,100,mBuyData.getCurrency());
            moneyRulerWidget.setMoneyLabel("交易金额");
            moneyRulerWidget.setInitMoney(mBuyData.getApplyAmount());
            fundEndFlag.setChoiceTextContent(DataUtils.getEndFlag(mBuyData.getEndFlag()));
            endPosition = Integer.parseInt(mBuyData.getEndFlag());
            timeInvestType = mBuyData.getDtdsFlag();
            sunPosition = mBuyData.getSubDate();
            if ("1".equals(mBuyData.getEndFlag())) {
                selcetEndContent(mBuyData.getEndFlag());
                fundEndContent.setChoiceTextContent(mBuyData.getEndDate());
            } else if ("2".equals(mBuyData.getEndFlag())) {
                selcetEndContent(mBuyData.getEndFlag());
                funInvestContent.setmContentMoneyEditText(mBuyData.getEndSum());
            } else if ("3".equals(mBuyData.getEndFlag())) {
                selcetEndContent(mBuyData.getEndFlag());
                funInvestContent.setmContentMoneyEditText(mBuyData.getEndAmt());
            }
        } else {
            fundSttime.setChoiceTextContent(mContext.getString(R.string.boc_fund_update_sundate, 1));
            fundEndFlag.setChoiceTextContent(mContext.getString(R.string.boc_fund_update_endflaghit));
            moneyRulerWidget.initMoneyRuler(200, 10000, 100, "001");
            //  moneyRulerWidget.initMoneyRuler(Long.parseLong(mBuyData.getLowInstmtAmount()), Long.parseLong(mBuyData.getUpInstmtAmount()), 100, "001");
            moneyRulerWidget.setMinTip(mContext.getString(R.string.boc_fund_update_MinTip));
            moneyRulerWidget.setMaxTip(mContext.getString(R.string.boc_fund_update_MaxTip));
            moneyRulerWidget.setMoneyLabel(mContext.getString(R.string.boc_fund_update_transAmount));
            moneyRulerWidget.setCurrentMoney("200");
        }
        viewAgreement.setAgreement(mContext.getString(R.string.boc_fund_contract));
    }
    @Override
    protected void setListener() {
        super.setListener();
        if (!isInvestUpData) {
            // “每月”按钮
            purchaseBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLeftSelect();
                }
            });
            // "每周"按钮
            redeemBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRightSelect();
                }
            });
        }
        moneyRulerWidget.setOnMoneyRulerScrollerListener(this);

        fundSttime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               if (MONTHS_FLAG.equals(timeInvestType)) {
                   timeInvestType = MONTHS_FLAG;
                   showDialog();
               } else if (WEEK_FLAG.equals(timeInvestType)) {
                   timeInvestType = WEEK_FLAG;
                   showDialog();
               }
            }
        });

        fundEndFlag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDialog();
            }
        });

        fundEndContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDateTime date = ApplicationContext.getInstance().getCurrentSystemDate();
                String stratTime= date.format(DateFormatters.dateFormatter1);
                judgeStartTimeAndSet(LocalDate.parse(stratTime,DateFormatters.dateFormatter1));
            }
        });

        viewAgreement.setOnClickContractListener(new SelectAgreementView.OnClickContractListener() {
            @Override
            public void onClickContract(int index) {
                if (index == 0) {
                    ContractFragment.newInstance("");
                } else {
                    ContractFragment.newInstance("");
                }
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeStrong();
            }
        });
    }

    /*选中月按钮*/
    private void setLeftSelect() {
        ImageView icon = (ImageView) purchaseBox.findViewById(R.id.purchase_image);
        TextView text = (TextView) purchaseBox.findViewById(R.id.purchase_text);
        icon.setVisibility(View.VISIBLE);
        text.setTextColor(getResources().getColor(R.color.boc_text_color_red));
        purchaseBox.setBackgroundResource(R.drawable.boc_textview_bg_light);

        ImageView icon2 = (ImageView) redeemBox.findViewById(R.id.redeem_image);
        TextView text2 = (TextView) redeemBox.findViewById(R.id.redeem_text);
        icon2.setVisibility(View.GONE);
        text2.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
        redeemBox.setBackgroundResource(R.drawable.boc_textview_bg_default);
        timeInvestType = MONTHS_FLAG;
    }
    /**选中周按钮*/
    private void setRightSelect(){
        ImageView icon = (ImageView) redeemBox.findViewById(R.id.redeem_image);
        TextView text = (TextView) redeemBox.findViewById(R.id.redeem_text);
        icon.setVisibility(View.VISIBLE);
        text.setTextColor(getResources().getColor(R.color.boc_text_color_red));
        redeemBox.setBackgroundResource(R.drawable.boc_textview_bg_light);

        ImageView icon2 = (ImageView) purchaseBox.findViewById(R.id.purchase_image);
        TextView text2 = (TextView) purchaseBox.findViewById(R.id.purchase_text);
        icon2.setVisibility(View.GONE);
        text2.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
        purchaseBox.setBackgroundResource(R.drawable.boc_textview_bg_default);
        timeInvestType = WEEK_FLAG;
    }

    /**月和周选择弹出框*/
    private void showDialog() {
        if (dateMothsDialog == null) {
            dateMothsDialog = new SelectStringListDialog(getContext());
        }
        List<String> monthsList = new ArrayList<String>();
        if (MONTHS_FLAG.equals(timeInvestType)) {
            for (int i = 1; i < 29; i++) {
                if (i < 10) {
                    monthsList.add(String.format("0%s", i));
                } else {
                    monthsList.add(i+"");
                }
            }
            dateMothsDialog.setHeaderTitleValue(mContext.getString(R.string.boc_fund_update_months));
        } else if (WEEK_FLAG.equals(timeInvestType)) {
            for (int i = 1; i< 6; i++) {
                monthsList.add(DataUtils.getWeekDate(i+""));
            }
            dateMothsDialog.setHeaderTitleValue(mContext.getString(R.string.boc_fund_update_weeks));
        }
        dateMothsDialog.isShowHeaderTitle(true);
        dateMothsDialog.isSetLineMargin(true);
        dateMothsDialog.setListData(monthsList);
        dateMothsDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
            @Override
            public void onSelect(int position, String model) {
                if (MONTHS_FLAG.equals(timeInvestType)) {
                    if (position < 10) {
                        sunPosition = String.format("0%s",position +1);
                    } else {
                        sunPosition = String.format("%s",position +1) ;
                    }
                    fundSttime.setChoiceTextContent(model);
                } else if (WEEK_FLAG.equals(timeInvestType)) {
                    if (position <10) {
                        sunPosition = String.format("0%s",position +1) ;
                    } else {
                        sunPosition = String.format("%s",position +1) ;
                    }
                    fundSttime.setChoiceTextContent(model);
                }
                dateMothsDialog.dismiss();
            }
        });
        if(!dateMothsDialog.isShowing()){
            dateMothsDialog.show();
        }
    }

    /**结束条件选择*/
    private void showEndDialog() {
        if (dateMothsDialog == null) {
            dateMothsDialog = new SelectStringListDialog(getContext());
        }
        List<String> endList = new ArrayList<String>();
        for (int i =1; i< 4; i++) {
            endList.add(DataUtils.getEndFlag(i+""));
        }
        dateMothsDialog.setHeaderTitleValue(mContext.getString(R.string.boc_fund_update_endType));
        dateMothsDialog.isShowHeaderTitle(true);
        dateMothsDialog.isSetLineMargin(true);
        dateMothsDialog.setListData(endList);
        dateMothsDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
            @Override
            public void onSelect(int position, String model) {
                endPosition = position +1;
                fundEndFlag.setChoiceTextContent(model);
                selcetEndContent(endPosition+"");
                dateMothsDialog.dismiss();
            }
        });
        if(!dateMothsDialog.isShowing()){
            dateMothsDialog.show();
        }
    }

    /**结束条件
     * @param endcontent
     */
    private void selcetEndContent(String endcontent) {
        if ("1".equals(endcontent)) {
            fundEndContent.setVisibility(VISIBLE);
            funInvestContent.setVisibility(GONE);
            fundEndContent.setChoiceTextName(mContext.getString(R.string.boc_fund_enddate));
        } else if ("2".equals(endcontent)){
            fundEndContent.setVisibility(GONE);
            funInvestContent.setVisibility(VISIBLE);
            funInvestContent.setEditWidgetTitle(mContext.getString(R.string.boc_fund_endsum));
        } else if ("3".equals(endcontent)) {
            fundEndContent.setVisibility(GONE);
            funInvestContent.setVisibility(VISIBLE);
            funInvestContent.setEditWidgetTitle(mContext.getString(R.string.boc_fund_endamount));
        }
    }

    /**点击效验数据*/
    private void judgeStrong(){
        if (!viewAgreement.isSelected()) {
            mBussFragment.showErrorDialog(mContext.getString(R.string.boc_fund_update_cimthit));
            return;
        }
        startFragmentInfo();
    }

    /**跳转页面*/
    private void startFragmentInfo(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(InvestConst.VALID_DETAIL,builtBuyData());
        bundle.putSerializable(InvestConst.FUND_COMPANYTYPE, mFundcompany);
        if (isInvestUpData) {
            InvestInfoFragment infoFragment = new InvestInfoFragment();
            infoFragment.setArguments(bundle);
            mBussFragment.start(infoFragment);
        } else {
            InvestConfirmFragment confirmFragment = new InvestConfirmFragment();
            confirmFragment.setArguments(bundle);
            mBussFragment.start(confirmFragment);
        }
    }

    /**
     * 起始日期的选择
     */
    private void judgeStartTimeAndSet(LocalDate currentDate) {
        DateTimePicker.showDatePick(mContext, currentDate, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
            @Override
            public void onChoiceDateSet(String strChoiceTime, LocalDate choiceDate) {
                if (choiceDate.isBefore(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate())) {
                    mBussFragment.showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_year_change));
                    return;
                }
                if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(), MAX_QUERY_DATE)) {
                    mBussFragment.showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE / 12)));
                    return;
                }
                fundEndContent.setChoiceTextContent(strChoiceTime);
            }
        });
    }

    /**定申修改数据处理*/
    private ValidinvestModel.ListBean builtBuyData() {

        ValidinvestModel.ListBean  mBuyBeabData = new ValidinvestModel.ListBean();
        mBuyBeabData.setDtdsFlag(timeInvestType);
        mBuyBeabData.setEndFlag(endPosition +"");
        mBuyBeabData.setEndAmt(funInvestContent.getMoney() +"");
        mBuyBeabData.setEndDate(fundEndContent.getChoiceTextContent());
        mBuyBeabData.setEndSum(funInvestContent.getContentMoney());
        if (isInvestUpData) {
            mBuyBeabData.setFundName(mBuyData.getFundName());
            mBuyBeabData.setFundCode(mBuyData.getFundCode());
            mBuyBeabData.setApplyDate(mBuyData.getApplyDate());
            mBuyBeabData.setFundSeq(mBuyData.getFundSeq());
        } else {
            mBuyBeabData.setFundName("");
            mBuyBeabData.setFundCode("");
            mBuyBeabData.setFundSeq("");
        }
        mBuyBeabData.setApplyAmount(moneyRulerWidget.getMoney()+"");
        mBuyBeabData.setSubDate(sunPosition != null ? sunPosition : "01");
        return  mBuyBeabData;
    }

    @Override
    protected InvestPresenter initPresenter() {
        return new InvestPresenter(this);
    }


    @Override
    public void fundBalanceFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void fundBalanceSuccess(FundBalanceModel balanceResult) {
        closeProgressDialog();
        if (balanceResult != null) {
            mBalance = balanceResult;
            fundInfo = mBalance.getFundBalance().get(0).getFundInfo();
            fundName.setText(fundInfo.getFundName());
            fundCode.setText(fundInfo.getFundCode());
            fundConpany.setText(fundInfo.getFundCompanyName());

        }
    }

    @Override
    public void fundCompanyFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void fundCompanySuccess(FundCompanyModel companyResult) {
        closeProgressDialog();
        if (companyResult !=null) {
            mFundcompany = companyResult;
        }
        isInestView = false;
       // pagerData();
    }

    @Override
    public void setPresenter(InvestPresenter presenter) {

    }

    @Override
    public void onMoneyRulerScrollered(BigDecimal money) {

    }
}
