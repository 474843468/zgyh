package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetConfirm.PsnCrcdDividedPayBillSetConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetResult.PsnCrcdDividedPayBillSetResultResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.gridbtn.GridButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyruler.ExactBoundaryMoneyRulerWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.model.BillInstallmentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.presenter.BillInstallmentsPresenter;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 信用卡账单分期首页面
 * 作者：lq7090
 * 创建时间：2016/12/9.
 */
public class BillInstallmentsFragment extends BussFragment implements BillInstallmentsContract.BaseView, GridButton.OnClickListener, ExactBoundaryMoneyRulerWidget.MoneyRulerScrollerListener {

    private static final String BILL_INSTALL = "billInstallment";
    private static final String BILL_ACOUNTBEAN = "acountbean";
    private View rootView;
    private static BillInstallmentsFragment billInstallmentsFragment;
    private BillInstallmentsContract.Presenter mBillInstallmentsPresenter;
    protected ExactBoundaryMoneyRulerWidget moneyRulerWidget;//金额滑动标尺
    private GridButton mGB;
    int scaleValue;//金额滑动标尺刻度
    private TextView divPeriodTv, divInformationTv,
            divPeriodTvThree, divInstmtChargeTv, divFirstInAmountTv, divRestPerTimeInAmountTv,
            notTv;
    private LinearLayout divInformation, divInformationThree;//列表按钮下方的分期信息divInformation，换行时改为显示divInformationThree
    private TextView nextBtn;
    private int div_period;//分期数
    private PsnGetSecurityFactorResult mSecurityFactorResult; //用户已经绑定的安全认证方式
    private BigDecimal amount, imstCharge, firstInAmount, restPerTimeInAmount;//手续费首期应还，每期应还
    private BillInstallmentModel billInstallmentModel;//数据模型
    private Boolean isPermit;//是否允许跳转

    private AccountBean ab;//账号信息
    private VerifyBean verifyBean;

    private Boolean isChoosePeriod;//用于控制是否选择期数



    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_billinstallments, null);
        return rootView;
    }

    public static BillInstallmentsFragment getInstance(BillInstallmentModel model, AccountBean ab) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BILL_INSTALL, model);
        bundle.putParcelable(BILL_ACOUNTBEAN, ab);

        billInstallmentsFragment = new BillInstallmentsFragment();
        billInstallmentsFragment.setArguments(bundle);

        return billInstallmentsFragment;
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();


        billInstallmentModel = new BillInstallmentModel();
        ab = new AccountBean();
//        billInstallmentModel = (BillInstallmentModel) getArguments().getSerializable(BILL_INSTALL);
//        ab = getArguments().getParcelable(BILL_ACOUNTBEAN);

//        billInstallmentModel.setLowInstmtAmount(new BigDecimal("500"));
//        billInstallmentModel.setUpInstmtAmount(new BigDecimal("1656.90"));
        ab.setAccountId("125964207");
        ab.setAccountNumber("6227521192627023");

//        billInstallmentModel.setAccountId(125965193);
        billInstallmentModel.setCurrencyCode("001");//应该从明细页面获得
        billInstallmentModel.setChargeMode("0");//只有0
//        billInstallmentModel.setCrcdFinalFour("7321");
//        billInstallmentModel.setAcctNum("5149581129607321");

        billInstallmentModel.setAccountId(Integer.parseInt(ab.getAccountId()));
                billInstallmentModel.setAcctNum(ab.getAccountNumber());
        if (billInstallmentModel.getAcctNum().length() > 5) {
            int l = billInstallmentModel.getAcctNum().length();
            billInstallmentModel.setCrcdFinalFour(ab.getAccountNumber().substring(l - 5, l));
        }
        billInstallmentModel.setCurrencyCode("001");//从明细页面获得
        billInstallmentModel.setChargeMode("0");//只有0



        billInstallmentModel.setLowInstmtAmount(new BigDecimal("100.00"));
        billInstallmentModel.setUpInstmtAmount(new BigDecimal("11110.18"));

        isPermit = false;//是否允许跳转
        isChoosePeriod = false;//用于控制是否选择期数
        scaleValue = 1;//默认值

    }

    @Override
    public void initView() {
        moneyRulerWidget = (ExactBoundaryMoneyRulerWidget) rootView.findViewById(R.id.ruler_amount);

        if (billInstallmentModel.getUpInstmtAmount().doubleValue() - billInstallmentModel.getLowInstmtAmount().doubleValue() > 500)
            scaleValue = 10;
        if (billInstallmentModel.getUpInstmtAmount().doubleValue() - billInstallmentModel.getLowInstmtAmount().doubleValue() > 5000)
            scaleValue = 100;
        if (billInstallmentModel.getUpInstmtAmount().doubleValue() - billInstallmentModel.getLowInstmtAmount().doubleValue() > 50000)
            scaleValue = 1000;
        if (billInstallmentModel.getUpInstmtAmount().doubleValue() - billInstallmentModel.getLowInstmtAmount().doubleValue() > 500000)
            scaleValue = 10000;
        if (billInstallmentModel.getUpInstmtAmount().doubleValue() - billInstallmentModel.getLowInstmtAmount().doubleValue() > 5000000)
            scaleValue = 100000;

        moneyRulerWidget.initMoneyRuler(billInstallmentModel.getLowInstmtAmount().doubleValue(), billInstallmentModel.getUpInstmtAmount().doubleValue(), scaleValue, "001");

        moneyRulerWidget.setCurrentMoney(billInstallmentModel.getUpInstmtAmount() + "");

        moneyRulerWidget.setMinTip(getResources().getString(R.string.boc_crcd_installment_noless) + billInstallmentModel.getLowInstmtAmount());
        moneyRulerWidget.setMaxTip(getResources().getString(R.string.boc_crcd_installment_nomore) + billInstallmentModel.getUpInstmtAmount());
        moneyRulerWidget.setMoneyLabel(getResources().getString(R.string.boc_crcd_myinstallment_amount));


        billInstallmentModel = new BillInstallmentModel();
        amount = billInstallmentModel.getAmount();//此段话改为获取明细页面上的值
        billInstallmentModel.setAmount(amount);

        mGB = (GridButton) rootView.findViewById(R.id.gb);
        mGB.setOnGvItemClickListener(this);
        nextBtn = (TextView) rootView.findViewById(R.id.btn_next);
        divInformation = (LinearLayout) rootView.findViewById(R.id.div_information_l);
        divInformationTv = (TextView) rootView.findViewById(R.id.div_information);
        divPeriodTv = (TextView) rootView.findViewById(R.id.tv_gv_btn);
        divInformationThree = (LinearLayout) rootView.findViewById(R.id.div_information3_l);
        divPeriodTvThree = (TextView) rootView.findViewById(R.id.tv_gv_btn3);
        divInstmtChargeTv = (TextView) rootView.findViewById(R.id.div_instmtCharge);
        divFirstInAmountTv = (TextView) rootView.findViewById(R.id.div_firstInAmount);
        divRestPerTimeInAmountTv = (TextView) rootView.findViewById(R.id.div_restPerTimeInAmount);
        notTv = (TextView) rootView.findViewById(R.id.tv_gv_btn_not);//用于占位的TextView

    }

    @Override
    public void initData() {
        mBillInstallmentsPresenter = new BillInstallmentsPresenter(this);

        /**
         * 获取从首页传来的金额
         */
//        billInstallmentModel = new BillInstallmentModel();

        isPermit = false;
        /**
         * 从明细页面获取的值，装入consumeInstallmentModel  应在上一页面通过调用getInstance方法来实现
         *
         * accountId
         :
         "125965193"
         currencyCode
         :
         "001"
         *
         */
        billInstallmentModel.setAccountId(125964207);
        billInstallmentModel.setCurrencyCode("001");//应该从明细页面获得
        billInstallmentModel.setChargeMode("0");//只有0
        billInstallmentModel.setCrcdFinalFour("7023");
        billInstallmentModel.setAcctNum("6227521192627023");

//        billInstallmentModel.setAccountId(Integer.parseInt(ab.getAccountId()));
//        billInstallmentModel.setCurrencyCode("001");//从明细页面获得
//        billInstallmentModel.setChargeMode("0");//只有0
//
//        billInstallmentModel.setLowInstmtAmount(new BigDecimal("100"));
//        billInstallmentModel.setUpInstmtAmount(new BigDecimal("3194.39"));

//        billInstallmentModel.setAcctNum(ab.getAccountNumber());
//        if (billInstallmentModel.getAcctNum().length() > 5) {
//            int l = billInstallmentModel.getAcctNum().length();
//            billInstallmentModel.setCrcdFinalFour(ab.getAccountNumber().substring(l - 5, l));
//        }

    }


    @Override
    public void setListener() {
        moneyRulerWidget.setOnMoneyRulerScrollerListener(this);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * 跳转到确认页面
             * @param v
             */
            @Override
            public void onClick(View v) {
                if (isPermit)
                    start(BillInstallmentsConfirmFragment.newInstance(billInstallmentModel,verifyBean));
                else {
                    showErrorDialog(getResources().getString(R.string.boc_crcd_please_select_pivot));
                }
            }
        });
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
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_crcd_bill_installment);
    }


    @Override
    public void onDestroy() {
        mBillInstallmentsPresenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onClickGV(LinearLayout l, TextView v, int position) {
        v.setTextColor(Color.WHITE);
        if (position <= 3) div_period = (position + 1) * 3;
        else if (position == 4) div_period = (position + 2) * 3;
        else {
            div_period = (position + 3) * 3;

        }
        showLoadingDialog();
        /**
         * 点击分期按钮后，
         */

        billInstallmentModel.setDivPeriod(div_period);
        billInstallmentModel.setAmount(moneyRulerWidget.getMoney());
        billInstallmentModel.setLowAmt(new BigDecimal("500.00"));


        /**
         * 请求安全因子，在securityFactorSuccess方法中获取默认安全因子，以及安全因子列表
         */
        mBillInstallmentsPresenter.querySecurityFactor();
        /**
         * 消费分期预交易，并获取手续费，首期应还，每期应还
         */
        showLoadingDialog(false);
        mBillInstallmentsPresenter.crcdDividedPayBillSetConfirm(billInstallmentModel);

    }


    @Override
    public void securityFactorSuccess(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
        if (null == psnGetSecurityFactorResult) {
            showErrorDialog("该用户没有绑定安全工具，请先去绑定安全工具");
            ((BaseMobileActivity) getActivity()).setErrorDialogClickListener(new BaseMobileActivity.ErrorDialogClickCallBack() {
                @Override
                public void onEnterBtnClick() {
                    /**
                     * 此处做请求安全因子失败时，的操作
                     */
                }
            });
            return;
        }
        //获取的安全认证方式
        mSecurityFactorResult = psnGetSecurityFactorResult;
        //转换为view层Model
        SecurityFactorModel securityFactorModel = new SecurityFactorModel(mSecurityFactorResult);
        if (null == mSecurityFactorResult.get_combinList()) {
            showErrorDialog("该用户没有绑定安全工具，请先去绑定安全工具");
            ((BaseMobileActivity) getActivity()).setErrorDialogClickListener(new BaseMobileActivity.ErrorDialogClickCallBack() {
                @Override
                public void onEnterBtnClick() {
                    /**
                     * 此处做请求安全因子失败时，的操作
                     */
                }
            });
            return;
        }
        billInstallmentModel.set_combinId(mSecurityFactorResult.get_defaultCombin().getId());
        billInstallmentModel.setCombinName(mSecurityFactorResult.get_defaultCombin().getName());
        billInstallmentModel.setSecurityFactorModel(securityFactorModel);
        billInstallmentModel.setConversationId(mBillInstallmentsPresenter.getConversationId());

    }

    @Override
    public void randomSuccess(String random) {
    }

    @Override
    public void crcdDividedPayBillSetConfirmSuccess(PsnCrcdDividedPayBillSetConfirmResult result, PsnGetSecurityFactorResult securityFactorResult) {

        closeProgressDialog();

        SecurityFactorModel securityFactorModel = ModelUtil.generateSecurityFactorModel(securityFactorResult);
        CombinListBean combinBean = SecurityVerity.getInstance(getActivity()).getDefaultSecurityFactorId(securityFactorModel);
        billInstallmentModel.setCombinName(combinBean.getName());
        billInstallmentModel.set_combinId(combinBean.getId());


        BeanConvertor.toBean(result,billInstallmentModel);//将预交易成功结果映射给账单分期数据模型
        verifyBean = new VerifyBean();

        verifyBean.set_certDN(result.getSecurityMap().get_certDN());
        verifyBean.set_plainData(result.getSecurityMap().get_plainData());

        List<FactorBean> factorList = new ArrayList<>();
        for (PsnCrcdDividedPayBillSetConfirmResult.SecurityMapBean.FactorListBean fb :  result.getSecurityMap().getFactorList() )
        {
            factorList.add(BeanConvertor.toBean(fb,(new FactorBean())));
        }
        verifyBean.setFactorList(factorList);
        verifyBean.setSmcTrigerInterval(result.getSecurityMap().getSmcTrigerInterval());
//        verifyBean = BeanConvertor.toBean(result.getSecurityMap(),verifyBean);
        BeanConvertor.toBean(verifyBean,billInstallmentModel);
        isChoosePeriod = true;//已选择期数
        isPermit = true;//允许跳转

        imstCharge = result.getInstmtCharge();//手续费
        firstInAmount = result.getFirstInAmount();//首期应还
        restPerTimeInAmount = result.getRestPerTimeInAmount();//每期应还

//        BeanConvertor.toBean(result, billInstallmentModel);//将预交易成功结果映射给账单分期数据模型

        billInstallmentModel.setSecurityFactorModel(new SecurityFactorModel(securityFactorResult));
        String s = getResources().getString(R.string.boc_crcd_imstCharge) + MoneyUtils.transMoneyFormat(imstCharge, billInstallmentModel.getCurrencyCode())

                + ", " + getResources().getString(R.string.boc_crcd_firstinamount) + MoneyUtils.transMoneyFormat(firstInAmount, billInstallmentModel.getCurrencyCode())
                + ", " + getResources().getString(R.string.boc_crcd_restPerTimeInAmount) + MoneyUtils.transMoneyFormat(restPerTimeInAmount, billInstallmentModel.getCurrencyCode());
        divPeriodTv.setText(div_period + getResources().getString(R.string.boc_crcd_period));

        closeProgressDialog();
        divInformationTv.setText(s);//用于1行显示三个字段

        /**
         * 用于判断一行内能否显示全
         */

        TextPaint mTP = divInformationTv.getPaint();
        int divInformationTvWidth = (int) mTP.measureText(s);

        TextPaint mTP1 = divPeriodTv.getPaint();
        int divPerionTcWidth = (int) mTP1.measureText(div_period + getResources().getString(R.string.boc_crcd_period));
        int mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        int layoutOtherWidth = ResUtils.dip2px(getContext(), 43);//43为其余空间+10
        if ((divInformationTvWidth + divPerionTcWidth + layoutOtherWidth) < mScreenWidth) {
            divInformationThree.setVisibility(View.GONE);
            divInformation.setVisibility(View.VISIBLE);

        } else {
            divInformation.setVisibility(View.GONE);
            divPeriodTvThree.setText(div_period + getResources().getString(R.string.boc_crcd_period));
            notTv.setText(div_period + getResources().getString(R.string.boc_crcd_period));
            divInstmtChargeTv.setText(getResources().getString(R.string.boc_crcd_imstCharge) + MoneyUtils.transMoneyFormat(imstCharge, billInstallmentModel.getCurrencyCode()));
            divFirstInAmountTv.setText(getResources().getString(R.string.boc_crcd_firstinamount) + MoneyUtils.transMoneyFormat(firstInAmount, billInstallmentModel.getCurrencyCode()));
            divRestPerTimeInAmountTv.setText(getResources().getString(R.string.boc_crcd_restPerTimeInAmount) + MoneyUtils.transMoneyFormat(restPerTimeInAmount, billInstallmentModel.getCurrencyCode()));
            divInformationThree.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void crcdDividedPayBillSetConfirmFailed(BiiResultErrorException exception) {

        isChoosePeriod = false;
        isPermit = false;
        mGB.recover();

    }


    /**
     * 此页面无需做关于提交交易的回调
     *
     * @param result
     */

    @Override
    public void crcdDividedPayBillSetResultSuccess(PsnCrcdDividedPayBillSetResultResult result) {

    }

    @Override
    public void crcdDividedPayBillSetResultFailed(BiiResultErrorException exception) {

    }

    @Override
    public void onMoneyRulerScrollered(BigDecimal money) {
        //以下操作为在选择期数，请求预交易成功后，如果变更了金额，则网格按钮及手续费三条数据复原到未选择状态
        if (isChoosePeriod) {
            isChoosePeriod = false;
            isPermit = false;
            mGB.recover();
            divInformationThree.setVisibility(View.GONE);
            divInformation.setVisibility(View.GONE);
        }
    }

    @Override
    public void onVerifySuccess(VerifyBean verifyBean, String mRandom, String tokenId) {

    }

    @Override
    public void onSubmitSuccess(Object submitResult) {

    }
}