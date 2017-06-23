package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.ui;

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
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.presenter.RedeemPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.ValidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestConst;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundBalanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundCompanyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.ui.RedeemInfoFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huixiaobo on 2016/12/16.
 * 定赎申请
 */
public class RedeemFragmentView extends MvpContainer<RedeemPresenter> implements RedeemContract.RedeemView,
        BaseView<RedeemPresenter> {

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
    protected EditChoiceWidget fundRedeemDate;
    protected EditMoneyInputWidget funRedeemCont;
    protected TextView fundmnt;
    protected EditChoiceWidget funRedeemtype;
    protected TextView fundhit;
    protected EditChoiceWidget fundEndFlag;
    protected EditChoiceWidget fundEndContent;
    protected SelectAgreementView viewAgreement;
    protected RelativeLayout redeemTrans;
    protected EditChoiceWidget redeemUpDate;
    protected TextView fundAvailableCount;
    protected Button btnNext;
    private Context mContext;
    /**每月选择标识*/
    private static final String MONTHS_FLAG = "0";
    /**每周选择标识*/
    private static final String WEEK_FLAG = "1";
    /**指定条件标识*/
    private static final String DING_FLAG = "2";
    /**赎回条件*/
    private static final String SELL_FLAG = "3";
    /**输入赎回份额*/
    protected EditMoneyInputWidget funRedeemContent;
    /**结束指定条件标签*/
    private int endflag;
    /**周的指定标签*/
    private String weekflag;
    /**是否顺延赎回*/
    private int sellflag;
    /**定赎制定条件*/
    private String redeemType = "";
    /**结束指定条件*/
    private String endType = "";
    /**有效定申传值对象*/
    private ValidinvestModel.ListBean mSellData;
    /**基金持仓信息*/
    private FundBalanceModel mBalance;
    /**基金公司信息*/
    private FundCompanyModel mFundcompany;
    /**是否是初次实现view*/
    private boolean isRedeemView;
    /**基金编码*/
    private String mFundCode;
    /**选中周和月按钮标识*/
    private String timeInvestType = "0";
    /**弹出时间选择器*/
    private SelectStringListDialog dateMothsDialog;
    /**是否选择顺延*/
    private boolean isRedeemType;
    /**定赎修改是否有数据*/
    private boolean isRedeemUpData;
    //最大查询起始日期为一年
    private final static int MAX_QUERY_DATE = 12;
    /**015返回基金对象*/
    private FundBalanceModel.FundBalanceBean fundInfo;

    public RedeemFragmentView(Context context){
        this(context, null,0);
    }

    public RedeemFragmentView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public RedeemFragmentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected RedeemPresenter initPresenter() {
        return new RedeemPresenter(this);
    }

    @Override
    protected View createContentView() {
        rootView = inflate(getContext(), R.layout.boc_redeem_apply_fragment, null);
        isRedeemView = true;
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        fundName = (TextView) rootView.findViewById(R.id.fundName);
        fundCode = (TextView) rootView.findViewById(R.id.fundCode);
        fundAvailableCount = (TextView) rootView.findViewById(R.id.fundAvailableCount);
        fundConpany = (TextView) rootView.findViewById(R.id.fundConpany);
        redeemImage = (ImageView) rootView.findViewById(R.id.redeem_image);
        redeemText = (TextView) rootView.findViewById(R.id.redeem_text);
        redeemBox = (LinearLayout) rootView.findViewById(R.id.redeem_box);
        purchaseImage = (ImageView) rootView.findViewById(R.id.purchase_image);
        purchaseText = (TextView) rootView.findViewById(R.id.purchase_text);
        purchaseBox = (LinearLayout) rootView.findViewById(R.id.purchase_box);
        fundRedeemDate = (EditChoiceWidget) rootView.findViewById(R.id.fundRedeemDate);
        funRedeemCont = (EditMoneyInputWidget) rootView.findViewById(R.id.funRedeemCont);
        fundmnt = (TextView) rootView.findViewById(R.id.fundmnt);
        funRedeemtype = (EditChoiceWidget) rootView.findViewById(R.id.funRedeemtype);
        fundhit = (TextView) rootView.findViewById(R.id.fundhit);
        fundEndFlag = (EditChoiceWidget) rootView.findViewById(R.id.fundEndFlag);
        fundEndContent = (EditChoiceWidget) rootView.findViewById(R.id.fundEndContent);
        viewAgreement = (SelectAgreementView) rootView.findViewById(R.id.view_agreement);
        btnNext = (Button) rootView.findViewById(R.id.btn_next);
        funRedeemContent = (EditMoneyInputWidget) rootView.findViewById(R.id.funRedeemContent);
        redeemTrans = (RelativeLayout) rootView.findViewById(R.id.redeemTrans);
        redeemUpDate = (EditChoiceWidget) rootView.findViewById(R.id.redeemUpDate);
    }

    @Override
    public void attach(Fragment fragment) {
        super.attach(fragment);
        mBussFragment = (BussFragment) fragment;
    }

    @Override
    public void initData() {
        super.initData();
      //  setUserVisibleHint(true);
        pagerData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isRedeemView && isVisibleToUser) {
            showLoadingDialog();
            if (mSellData != null && mSellData.isInvestUpdate()) {
                getPresenter().qureyfundBalance(mSellData.getFundCode());
                getPresenter().qureyfundBalance(mSellData.getFundCode());
            } else {
                getPresenter().qureyfundBalance(mFundCode);
                getPresenter().qureyfundBalance(mFundCode);
            }
        }
    }
    /**页面赋值*/
    private void pagerData(){
        if (isRedeemUpData) {
            redeemUpDate.setVisibility(VISIBLE);
            redeemTrans.setVisibility(GONE);

            fundName.setText(mSellData.getFundName());
            fundCode.setText(mSellData.getFundCode());
            fundConpany.setText(mSellData.getFundInfo().getFundCompanyName());

            redeemUpDate.setChoiceTextContent(mSellData.getDtdsFlag());
            funRedeemCont.setContentHint(mSellData.getApplyAmount());
            funRedeemtype.setChoiceTextContent(mSellData.getSellFlag());
            fundEndFlag.setChoiceTextContent(mSellData.getEndFlag());
            timeInvestType = mSellData.getDtdsFlag();
            endflag = Integer.parseInt(mSellData.getEndFlag());
            weekflag = mSellData.getSubDate();
            if ("1".equals(mSellData.getEndFlag())) {
                selcetEndContent(mSellData.getEndFlag());
                fundEndContent.setChoiceTextContent(mSellData.getEndDate());
            } else if ("2".equals(mSellData.getEndFlag())){
                selcetEndContent(mSellData.getEndFlag());
                funRedeemContent.setmContentMoneyEditText(mSellData.getEndSum());
            } else if ("3".equals(mSellData.getEndFlag())) {
                selcetEndContent(mSellData.getEndFlag());
                funRedeemContent.setmContentMoneyEditText(mSellData.getEndAmt());
            }
        } else {
            funRedeemCont.setmContentMoneyEditText("200");
            funRedeemtype.setChoiceTextContent(mContext.getString(R.string.boc_redeem_sellflag));
            fundEndFlag.setChoiceTextContent(mContext.getString(R.string.boc_fund_update_endflaghit));
            viewAgreement.setSelected(false);
        }
        viewAgreement.setAgreement(mContext.getString(R.string.boc_fund_contract));
    }

    @Override
    protected void setListener() {
        super.setListener();
        if (!isRedeemUpData) {
            // “每月”按钮
            purchaseBox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLeftSelect();
                }
            });
            // "每周"按钮
            redeemBox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRightSelect();
                }
            });
        }

        fundRedeemDate.setOnClickListener(new OnClickListener() {
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
                redeemType = DING_FLAG;
                showMonthsDialog();
            }
        });

        //金额 键盘消失和显示监听
        funRedeemCont.setOnKeyBoardListener(new EditMoneyInputWidget.KeyBoardDismissOrShowCallBack() {
            @Override
            public void onKeyBoardDismiss() {
                setSellAmount();
            }

            @Override
            public void onKeyBoardShow() {

            }
        });

        funRedeemContent.setOnKeyBoardListener(new EditMoneyInputWidget.KeyBoardDismissOrShowCallBack() {
            @Override
            public void onKeyBoardDismiss() {
            }

            @Override
            public void onKeyBoardShow() {
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

        funRedeemtype.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                redeemType = SELL_FLAG;
                showMonthsDialog();
            }
        });

        fundEndContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDateTime date = ApplicationContext.getInstance().getCurrentSystemDate();
                String startTime= date.format(DateFormatters.dateFormatter1);
                judgeStartTimeAndSet(LocalDate.parse(startTime, DateFormatters.dateFormatter1));
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeStrong();
            }
        });

    }

    /**结束条件
     * @param endcontent
     */
    private void selcetEndContent(String endcontent) {
        if ("1".equals(endcontent)) {
            funRedeemContent.setVisibility(GONE);
            fundEndContent.setVisibility(VISIBLE);
            fundEndContent.setChoiceTextName(mContext.getString(R.string.boc_fund_enddate));
        } else if ("2".equals(endcontent)){
            fundEndContent.setVisibility(GONE);
            funRedeemContent.setVisibility(VISIBLE);
            funRedeemContent.setEditWidgetTitle(mContext.getString(R.string.boc_fund_endsum));
        } else if ("3".equals(endcontent)) {
            fundEndContent.setVisibility(GONE);
            funRedeemContent.setVisibility(VISIBLE);
            funRedeemContent.setEditWidgetTitle(mContext.getString(R.string.boc_fund_endamount));
        }
    }

    /**效验赎回基金*/
    private void setSellAmount() {
        int fundCountType = compareBigDecimal(funRedeemCont.getContentMoney(), "100") ;
        if (fundCountType != 1) {
            mBussFragment.showErrorDialog("赎回基金不能低于100");
            return;
        }
    }
    /**下一步数据效验*/
    private void judgeStrong(){
        int fundCount = compareBigDecimal( "100", funRedeemCont.getContentMoney()) ;
        if (fundCount != 1) {
            mBussFragment.showErrorDialog("赎回基金不能低于100");
            return;
        }
        if (!viewAgreement.isSelected()) {
            mBussFragment.showErrorDialog(mContext.getString(R.string.boc_fund_update_cimthit));
            return;
        }
        startFragmentInfo();
    }

    /**下一步跳转页面*/
    private void startFragmentInfo() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(InvestConst.VALID_DETAIL, builtSellData());
        bundle.putSerializable(InvestConst.FUND_COMPANYTYPE, mFundcompany);
        if (isRedeemUpData) {
            RedeemInfoFragment infoFragment = new RedeemInfoFragment();
            infoFragment.setArguments(bundle);
            mBussFragment.start(infoFragment);
        } else {
            RedeemConfirmFragment confirmFragment = new RedeemConfirmFragment();
            confirmFragment.setArguments(bundle);
            mBussFragment.start(confirmFragment);
        }

    }

    //比较金额大小
    private int compareBigDecimal(String s1, String s2) {
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return b2.compareTo(b1);
    }

    /**
     * 日期月份选择框
     */
    private void showMonthsDialog() {
        if (dateMothsDialog == null) {
            dateMothsDialog = new SelectStringListDialog(getContext());
        }
        List<String> monthsList = new ArrayList<String>();
        if (DING_FLAG.equals(redeemType)) {
            for (int i =1; i< 4; i++) {
                monthsList.add(DataUtils.getEndFlag(i+""));
            }
            dateMothsDialog.setHeaderTitleValue(mContext.getString(R.string.boc_fund_update_endType));
        } else if (SELL_FLAG.equals(redeemType)){
            for (int i =1; i< 3; i++) {
                monthsList.add(DataUtils.getSellFlag(i+""));
            }
            dateMothsDialog.setHeaderTitleValue(mContext.getString(R.string.boc_fund_update_redeemType));
        }
        dateMothsDialog.isShowHeaderTitle(true);
        dateMothsDialog.isSetLineMargin(true);
        dateMothsDialog.setListData(monthsList);
        dateMothsDialog.setOnSelectListener(new SelectStringListDialog.OnSelectListener<String>() {
            @Override
            public void onSelect(int position, String model) {
                if (DING_FLAG.equals(redeemType)){
                    endflag = position +1;
                    selcetEndContent(endflag +"");
                    fundEndFlag.setChoiceTextContent(model);
                } else if (SELL_FLAG.equals(redeemType)) {
                    sellflag = position;
                    isRedeemType = true;
                    funRedeemtype.setChoiceTextContent(model);
                }
                dateMothsDialog.dismiss();
            }
        });
        if (!dateMothsDialog.isShowing()) {
            dateMothsDialog.show();
        }
    }

    /**
     * 选择周
     */
    private void showDialog() {
        if (dateMothsDialog == null) {
            dateMothsDialog = new SelectStringListDialog(getContext());
        }
        List<String> monthsList = new ArrayList<String>();
        if (WEEK_FLAG.equals(timeInvestType)) {
            for (int i = 1; i< 6; i++) {
                monthsList.add(DataUtils.getWeekDate((i)+"") );
            }
            dateMothsDialog.setHeaderTitleValue(mContext.getString(R.string.boc_fund_update_months));
        } else if (MONTHS_FLAG.equals(timeInvestType)) {
            for (int i = 1; i < 29; i++) {
                if (i < 10) {
                    monthsList.add(String.format("0%s",i));
                } else {
                    monthsList.add(i+"");
                }
            }
            dateMothsDialog.setHeaderTitleValue(mContext.getString(R.string.boc_fund_update_weeks));
        }

        dateMothsDialog.isShowHeaderTitle(true);
        dateMothsDialog.isSetLineMargin(true);
        dateMothsDialog.setListData(monthsList);
        dateMothsDialog.setOnSelectListener(new SelectStringListDialog.OnSelectListener<String>() {
            @Override
            public void onSelect(int position, String model) {
                if (WEEK_FLAG.equals(timeInvestType)) {
                    if (position < 10) {
                        weekflag = String.format("0%s",position +1);
                    } else {
                        weekflag = String.format("%s",position +1) ;
                    }
                    fundRedeemDate.setChoiceTextContent(model);
                } else if (MONTHS_FLAG.equals(timeInvestType)) {
                    if (position <10) {
                        weekflag =String.format("0%s", position+1);
                    } else {
                        weekflag =String.valueOf(position +1) ;
                    }
                    fundRedeemDate.setChoiceTextContent(model);
                }
                dateMothsDialog.dismiss();
            }
        });
        if (!dateMothsDialog.isShowing()) {
            dateMothsDialog.show();
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
                redeemUpDate.setChoiceTextContent(strChoiceTime);
            }
        });
    }

    /**
     * 定赎修改数据
     */
    private ValidinvestModel.ListBean builtSellData() {
        if (mSellData == null) {
            mSellData = new ValidinvestModel.ListBean();
        }
        mSellData.setDtdsFlag(timeInvestType);
        mSellData.setEndFlag(endflag +"");
        mSellData.setEndAmt(funRedeemContent.getMoney() +"");
        mSellData.setEndDate(fundEndContent.getChoiceTextContent());
        mSellData.setEndSum(funRedeemContent.getContentMoney());

        if (StringUtils.isEmptyOrNull(mSellData.getFundCode())) {
            mSellData.setFundCode(mFundCode);
        } else {
            mSellData.setFundCode(mSellData.getFundCode());
        }
        mSellData.setApplyAmount(funRedeemCont.getMoney()+"");
        mSellData.setSellFlag(funRedeemtype.getChoiceAutoTextContent());
        mSellData.setSubDate(weekflag);
        return mSellData;
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

    /**
     * 选中周按钮
     */
    private void setRightSelect() {
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

    /**
     * 定赎传值基金编号
     */
    public void setFundCode(String fundCode) {
        mFundCode = fundCode;
    }

    /**
     * 修改定赎传值对象
     * @param investSellData
     */
    public void setRedeemData(ValidinvestModel.ListBean investSellData) {
        mSellData = investSellData;
        if (mSellData != null && mSellData.isInvestUpdate()) {
            isRedeemUpData = true;
        }
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
            fundInfo = mBalance.getFundBalance().get(0);
            fundName.setText(fundInfo.getFundInfo().getFundName());
            fundCode.setText(fundInfo.getFundInfo().getFundCode());
            fundAvailableCount.setText(fundInfo.getTotalAvailableBalance());
            fundConpany.setText(fundInfo.getFundInfo().getFundCompanyName());
        }
    }

    @Override
    public void fundCompanyFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void fundCompanySuccess(FundCompanyModel companyResult) {
        closeProgressDialog();
        if (companyResult != null) {
            mFundcompany = companyResult;
        }
    }

    @Override
    public void setPresenter(RedeemPresenter presenter) {

    }

}
