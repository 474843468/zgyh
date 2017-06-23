package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeConfirm.PsnCrcdDividedPayConsumeConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeResult.PsnCrcdDividedPayConsumeResultResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.gridbtn.GridButton;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model.ConsumeInstallmentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.presenter.ConsumeInstallmentPresenter;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

import java.math.BigDecimal;

/**
 * 信用卡消费分期首页面 linq7090
 */
public class ConsumeInstallmentFragment extends BussFragment implements ConsumeInstallmentContract.BaseView, GridButton.OnClickListener {
    private static final String CONSUME_INSTALL = "consumeInstallment";
    private static final String CONSUME_ACOUNTBEAN = "acountbean";
    private View rootView;
    private static ConsumeInstallmentFragment consumeInstallmentFragment;
    private ConsumeInstallmentContract.Presenter mConsumeInstallmentPresenter;//presenter
    private GridButton mGB;
    private TextView
            amountTv,
            divPeriodTv, divInformationTv,
            divPeriodTvThree, divInstmtChargeTv, divFirstInAmountTv, divRestPerTimeInAmountTv,
            notTv;
    private LinearLayout divInformation, divInformationThree;//列表按钮下方的分期信息divInformation，换行时改为显示divInformationThree
    private TextView nextBtn;
    private int div_period;//分期数
    private PsnGetSecurityFactorResult mSecurityFactorResult; //用户已经绑定的安全认证方式
    private BigDecimal amount, imstCharge, firstInAmount, restPerTimeInAmount;//手续费首期应还，每期应还
    private ConsumeInstallmentModel consumeInstallmentModel;//数据模型
    private Boolean isPermit;//是否允许跳转
    AccountBean ab;//账户信息
    private VerifyBean verifyBean;

    public static ConsumeInstallmentFragment getInstance(ConsumeInstallmentModel model, AccountBean ab) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CONSUME_INSTALL, model);
        bundle.putParcelable(CONSUME_ACOUNTBEAN, ab);
        consumeInstallmentFragment = new ConsumeInstallmentFragment();
        consumeInstallmentFragment.setArguments(bundle);
        return consumeInstallmentFragment;
    }

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_consume_installment, null);
        return rootView;
    }

    @Override
    public void initView() {
        mGB = (GridButton) rootView.findViewById(R.id.gb);
        mGB.setOnGvItemClickListener(this);
        nextBtn = (TextView) rootView.findViewById(R.id.btn_next);
        divInformation = (LinearLayout) rootView.findViewById(R.id.div_information_l);
        divInformationTv = (TextView) rootView.findViewById(R.id.div_information);
        divPeriodTv = (TextView) rootView.findViewById(R.id.tv_gv_btn);
        amountTv = (TextView) rootView.findViewById(R.id.tv_value);
        divInformationThree = (LinearLayout) rootView.findViewById(R.id.div_information3_l);
        divPeriodTvThree = (TextView) rootView.findViewById(R.id.tv_gv_btn3);
        divInstmtChargeTv = (TextView) rootView.findViewById(R.id.div_instmtCharge);
        divFirstInAmountTv = (TextView) rootView.findViewById(R.id.div_firstInAmount);
        divRestPerTimeInAmountTv = (TextView) rootView.findViewById(R.id.div_restPerTimeInAmount);
        notTv = (TextView) rootView.findViewById(R.id.tv_gv_btn_not);//用于占位的TextView

    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        consumeInstallmentModel = new ConsumeInstallmentModel();
        ab = new AccountBean();
        consumeInstallmentModel = (ConsumeInstallmentModel) getArguments().getSerializable(CONSUME_INSTALL);
        ab = getArguments().getParcelable(CONSUME_ACOUNTBEAN);
        consumeInstallmentModel.setAccountId(Integer.parseInt(ab.getAccountId()));
        consumeInstallmentModel.setCurrencyCode("001");//从明细页面获得
        consumeInstallmentModel.setChargeMode("0");//只有0
        consumeInstallmentModel.setAcctNum(ab.getAccountNumber());
        if (consumeInstallmentModel.getAcctNum().length() > 5) {
            int l = consumeInstallmentModel.getAcctNum().length();
            consumeInstallmentModel.setCrcdFinalFour(ab.getAccountNumber().substring(l - 5, l));
        }

    }

    @Override
    public void initData() {

        mConsumeInstallmentPresenter = new ConsumeInstallmentPresenter(this);
        /**
         * 获取从首页传来的金额  应在上级页面通过getInstance 来操作。
         */
        isPermit = false;
        /**
         * 人民币元 1，0000
         */
        amountTv.setText(PublicCodeUtils.getCurrency(getContext(),
                consumeInstallmentModel.getCurrencyCode())
                + " " + MoneyUtils.transMoneyFormat(consumeInstallmentModel.getAmount(), consumeInstallmentModel.getCurrencyCode()));

    }

    @Override
    public void setListener() {
        nextBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * 跳转到确认页面
             * @param v
             */
            @Override
            public void onClick(View v) {
                if (isPermit)
                    start(ConsumeInstallmentConfirmFragment.getInstance(consumeInstallmentModel, verifyBean));
                else {
                    showErrorDialog(getResources().getString(R.string.boc_crcd_please_select_pivot));
                }
            }
        });
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_crcd_consume_installment);
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
    public void onDestroy() {
        mConsumeInstallmentPresenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onClickGV(LinearLayout l, TextView v, int position) {
        divInformationThree.setVisibility(View.GONE);
        divInformation.setVisibility(View.GONE);

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
        consumeInstallmentModel.setDivPeriod(div_period);

        /**
         * 请求安全因子，在securityFactorSuccess方法中获取默认安全因子，以及安全因子列表
         */
        mConsumeInstallmentPresenter.querySecurityFactor();
        /**
         * 消费分期预交易，并获取手续费，首期应还，每期应还
         */
        showLoadingDialog(false);
        mConsumeInstallmentPresenter.crcdDividedPayConsumeConfirm(consumeInstallmentModel);

    }

    /**
     * 请求安全因子成功
     *
     * @param psnGetSecurityFactorResult
     */
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
                }
            });
            return;
        }
        consumeInstallmentModel.set_combinId(mSecurityFactorResult.get_defaultCombin().getId());
        consumeInstallmentModel.setSecurityFactorModel(securityFactorModel);
        consumeInstallmentModel.setConversationId(mConsumeInstallmentPresenter.getConversationId());
    }

    /**
     * 请求随机数成功，此页面无需实现
     *
     * @param random
     */
    @Override
    public void randomSuccess(String random) {

    }

    /**
     * 消费分期预交易成功回调
     *
     * @param result
     */
    @Override
    public void crcdDividedPayConsumeConfirmSuccess(PsnCrcdDividedPayConsumeConfirmResult result, PsnGetSecurityFactorResult securityFactorResult) {

        closeProgressDialog();
        isPermit = true;
        imstCharge = result.getInstmtCharge();
        firstInAmount = result.getFirstInAmount();
        restPerTimeInAmount = result.getRestPerTimeInAmount();
        SecurityFactorModel securityFactorModel = ModelUtil.generateSecurityFactorModel(securityFactorResult);
        CombinListBean combinBean = SecurityVerity.getInstance(getActivity()).getDefaultSecurityFactorId(securityFactorModel);
        consumeInstallmentModel.setCombinName(combinBean.getName());
        consumeInstallmentModel.set_combinId(combinBean.getId());
        verifyBean = new VerifyBean();
        verifyBean = BeanConvertor.toBean(result.getSecurityMap(), verifyBean);
        BeanConvertor.toBean(result, consumeInstallmentModel);//将预交易成功结果映射给账单分期数据模型
        consumeInstallmentModel.setSecurityFactorModel(new SecurityFactorModel(securityFactorResult));
        String s = getResources().getString(R.string.boc_crcd_imstCharge) + MoneyUtils.transMoneyFormat(imstCharge, consumeInstallmentModel.getCurrencyCode())

                + ", " + getResources().getString(R.string.boc_crcd_firstinamount) + MoneyUtils.transMoneyFormat(firstInAmount, consumeInstallmentModel.getCurrencyCode())
                + ", " + getResources().getString(R.string.boc_crcd_restPerTimeInAmount) + MoneyUtils.transMoneyFormat(restPerTimeInAmount, consumeInstallmentModel.getCurrencyCode());
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
            divInstmtChargeTv.setText(getResources().getString(R.string.boc_crcd_imstCharge) + MoneyUtils.transMoneyFormat(imstCharge, consumeInstallmentModel.getCurrencyCode()));
            divFirstInAmountTv.setText(getResources().getString(R.string.boc_crcd_firstinamount) + MoneyUtils.transMoneyFormat(firstInAmount, consumeInstallmentModel.getCurrencyCode()));
            divRestPerTimeInAmountTv.setText(getResources().getString(R.string.boc_crcd_restPerTimeInAmount) + MoneyUtils.transMoneyFormat(restPerTimeInAmount, consumeInstallmentModel.getCurrencyCode()));
            divInformationThree.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 消费分期预交易失败回调 此页面不做处理
     *
     * @param exception
     */
    @Override
    public void crcdDividedPayConsumeConfirmFailed(BiiResultErrorException exception) {
        isPermit = false;
        mGB.recover();

    }


    /**
     * 消费分期提交交易成功回调，此页面无需实现
     *
     * @param result
     */
    @Override
    public void crcdDividedPayConsumeResultSuccess(PsnCrcdDividedPayConsumeResultResult result) {

    }

    /**
     * 消费分期提交交易失败回调，此页面无需实现
     *
     * @param exception
     */
    @Override
    public void crcdDividedPayConsumeResultFailed(BiiResultErrorException exception) {

    }

    @Override
    public void onVerifySuccess(VerifyBean verifyBean, String mRandom, String tokenId) {

    }

    @Override
    public void onSubmitSuccess(Object submitResult) {

    }
}