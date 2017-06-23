package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.ui;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.InvestTreatyDetail;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.dialog.InvestTreatyDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyConfirmModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.presenter.InvestTreatyContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.presenter.InvestTreatyPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.ui.RiskAssessFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchReqModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.List;

/**
 * 协议明细界面
 * Created by guokai on 2016/9/13.
 */
@SuppressLint("ValidFragment")
public class InvestTreatyInfoFragment extends MvpBussFragment<InvestTreatyPresenter> implements InvestTreatyContract.InvestTreatyInfoView, View.OnClickListener, TitleAndBtnDialog.DialogBtnClickCallBack, InvestTreatyDetail.ClickListener {

    //协议查询
    private InvestTreatyModel.CapacityQueryBean model;
    //协议明细
    private InvestTreatyInfoModel infoModel;
    private InvestTreatyConfirmModel infoConfirmModel;

    private InvestTreatyDetail detailTop, detailMiddle, detailButtom, detailTreaty;
    private Button bottomLeft, bottomRight;
    private LinearLayout bottom;
    private TitleAndBtnDialog cancelDialog, dialog, riskDialog;

    /**
     * 协议状态 true 有效 false 无效
     */
    private boolean isValid;
    private InvestTreatyDialog alterDialog;

    public InvestTreatyInfoFragment(InvestTreatyModel.CapacityQueryBean item1, boolean isValid) {
        this.isValid = isValid;
        this.model = item1;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_view_invest_treaty_info, null);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_invest_treaty_title_info);
    }

    @Override
    public void reInit() {
    }

    /**
     * 是否显示右侧标题按钮
     */
    protected boolean isDisplayRightIcon() {
        return false;
    }

    /**
     * 是否显示左侧标题按钮
     */
    protected boolean isDisplayLeftIcon() {
        return true;
    }

    /**
     * 红色主题titleBar：true ；
     * 白色主题titleBar：false ；
     */
    protected boolean getTitleBarRed() {
        return false;
    }


    @Override
    public void initView() {
        detailTop = (InvestTreatyDetail) mContentView.findViewById(R.id.detail_top);
        detailTreaty = (InvestTreatyDetail) mContentView.findViewById(R.id.detail_treaty);
        detailMiddle = (InvestTreatyDetail) mContentView.findViewById(R.id.detail_middle);
        detailButtom = (InvestTreatyDetail) mContentView.findViewById(R.id.detail_buttom);
        bottomLeft = (Button) mContentView.findViewById(R.id.bottom_left);
        bottomRight = (Button) mContentView.findViewById(R.id.bottom_right);
        bottom = (LinearLayout) mContentView.findViewById(R.id.bottom);

    }

    @Override
    public void initData() {
        showLoadingDialog();
        //投资信息查询
        getPresenter().psnXpadAgreementInfoQuery(model);
    }

    @Override
    public void setListener() {
        detailTop.setClickListener(this);
        bottomLeft.setOnClickListener(this);
        bottomRight.setOnClickListener(this);
    }

    /**
     * 协议明细回调
     *
     * @param infoModel
     */
    @Override
    public void psnXpadAgreementInfoQuery(InvestTreatyInfoModel infoModel) {
        startPresenter();
        //客户投资协议交易明细查询
        getPresenter().psnXpadCapacityTransList(model);
        this.infoModel = infoModel;
        infoConfirmModel = new InvestTreatyConfirmModel();
        BeanConvertor.toBean(infoModel,infoConfirmModel);
        setDetail();
        if (isValid) {
            if ("1".equals(infoModel.getCanCancel())) {
                bottomLeft.setVisibility(View.VISIBLE);
            } else {
                bottomLeft.setVisibility(View.GONE);
            }
            if ("1".equals(infoModel.getCanUpdate())) {
                bottomRight.setVisibility(View.VISIBLE);
            } else {
                bottomRight.setVisibility(View.GONE);
            }
        } else {
            bottom.setVisibility(View.GONE);
        }
    }

    /**
     * 协议终止回调
     */
    @Override
    public void psnXpadInvestAgreementCancel(InvestTreatyInfoModel infoModel) {
        closeProgressDialog();
        if (!StringUtils.isEmptyOrNull(infoModel.getTransactionId())) {
            ToastUtils.show(getString(R.string.boc_invest_treaty_message_stop_period));
            popToAndReInit(InvestTreatyFragment.class);
        } else {
            ToastUtils.show(getString(R.string.boc_invest_treaty_message_stop_faild));
        }
    }

    /**
     * 业绩基准周期滚续协议终止回调
     */
    @Override
    public void psnXpadBenchmarkMaintainResult(InvestTreatyConfirmModel infoModel) {
        closeProgressDialog();
        if (!StringUtils.isEmptyOrNull(infoModel.getTransactionId())) {
            ToastUtils.show(getString(R.string.boc_invest_treaty_message_stop_period));
            popToAndReInit(InvestTreatyFragment.class);
        } else {
            ToastUtils.show(getString(R.string.boc_invest_treaty_message_stop_faild));
        }
    }

    /**
     * 客户投资协议交易明细查询回调
     *
     * @param tradeList
     */
    @Override
    public void psnXpadCapacityTransList(List<InvestTreatyInfoModel> tradeList) {
        closeProgressDialog();
        detailTreaty.setTvTitle(getString(R.string.boc_invest_treaty_message_tom));
        if (PublicUtils.isEmpty(tradeList)) {
            detailTreaty.addNullBetweenRow();
        } else {
            for (InvestTreatyInfoModel tradeInfoModel : tradeList) {
                detailTreaty.addDetailBetweenRow(tradeInfoModel.getTdsType(), tradeInfoModel.getMemo(), tradeInfoModel.getTdsState(), tradeInfoModel.getTdsDate(), tradeInfoModel.getTdsAmt(), tradeInfoModel.getTdsUnit());
            }
        }

    }

    /**
     * 客户投资协议交易明细查询回调 失败
     *
     */
    @Override
    public void psnXpadCapacityTransListFailed() {
        closeProgressDialog();
        detailTreaty.setTvTitle(getString(R.string.boc_invest_treaty_message_tom));
        detailTreaty.addNullBetweenRow();
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    /**
     * 协议类型转换
     *
     * @return
     */
    public String setAgrType() {
        if (infoModel.getAgrType().equals("1")) {
            return getString(R.string.boc_invest_treaty_title_info_treaty);
        } else if (infoModel.getAgrType().equals("2")) {
            return getString(R.string.boc_invest_treaty_title_info4);
        } else if (infoModel.getAgrType().equals("3")) {
            return getString(R.string.boc_invest_treaty_title_info_name);
        } else if (infoModel.getAgrType().equals("4")) {
            return getString(R.string.boc_invest_treaty_title_info5);
        } else if (infoModel.getAgrType().equals("5")) {
            return getString(R.string.boc_invest_treaty_title_info7);
        }
        return "";
    }

    /**
     * 投资方式转换
     *
     * @return
     */
    public String setInstType() {
        switch (infoModel.getInstType()) {
            case "1":
                return getString(R.string.boc_invest_treaty_title_info0);
            case "2":
                return getString(R.string.boc_invest_treaty_title_info1);
            case "3":
                return getString(R.string.boc_invest_treaty_title_info2);
            case "4":
                return getString(R.string.boc_invest_treaty_title_info3);
            case "5":
                return getString(R.string.boc_invest_treaty_title_info4);
            case "6":
                return getString(R.string.boc_invest_treaty_title_info5);
            case "7":
                return getString(R.string.boc_invest_treaty_title_info6);
            case "8":
                return getString(R.string.boc_invest_treaty_title_info7);
        }
        return "";
    }

    /**
     * 协议份额转换
     */
    public String setAmount(String str) {
        if ("-1.00".equals(str)) {
            return getString(R.string.boc_invest_treaty_all);
        } else {
            return str;
        }
    }

    /**
     * 协议周期数转换
     */
    public String setPeriodtotal(String str) {
        if ("-1".equals(str)) {
            return getString(R.string.boc_invest_treaty_no_period);
        } else {
            return str;
        }
    }

    /**
     * 投资模式转换
     */
    public String setAmountType() {
        if ("0".equals(infoModel.getAmountType())) {
            return getString(R.string.boc_invest_treaty_money);
        } else if ("1".equals(infoModel.getAmountType())) {
            return getString(R.string.boc_invest_treaty_no_money);
        }
        return "";
    }

    /**
     * 交易方向转换
     */
    public String setTdsType(String str) {
        if ("0".equals(str)) {
            return getString(R.string.boc_invest_treaty_isneedred);
        } else {
            return getString(R.string.boc_invest_treaty_isneed_pur);
        }
    }

    /**
     * 协议周期转换
     */
    public String setPeriodAge(String str) {
        if (str.endsWith("d")) {
            String text = str.replace("d", getString(R.string.boc_invest_treaty_period_day));
            return text;
        } else if (str.endsWith("m")) {
            String text = str.replace("m", getString(R.string.boc_invest_treaty_period_month));
            return text;
        } else if (str.endsWith("w")) {
            String text = str.replace("w", getString(R.string.boc_invest_treaty_period_zhou));
            return text;
        } else if (str.endsWith("y")) {
            String text = str.replace("y", getString(R.string.boc_invest_treaty_period_year));
            return text;
        }
        return "";
    }

    /**
     * 是否购买字段内容显示
     *
     * @return
     */
    public String getIsneedpur() {
        if ("0".equals(infoModel.getIsneedpur())) {
            return getString(R.string.boc_invest_treaty_period_isneedpur);
        } else {
            return getString(R.string.boc_invest_treaty_period_no_isneedpur);
        }
    }

    /**
     * 是否赎回字段内容显示
     *
     * @return
     */
    public String getIsneedred() {
        if ("0".equals(infoModel.getIsneedred())) {
            return getString(R.string.boc_invest_treaty_period_isneedred);
        } else {
            return getString(R.string.boc_invest_treaty_period_no_isneedred);
        }
    }

    /**
     * 设置界面详情
     */
    public void setDetail() {
        detailTop.setTvTitle(getString(R.string.boc_invest_treaty_my_message));
        String accno = model.getAccNo().replace(model.getAccNo().substring(4, model.getAccNo().length() - 4), "******");
        detailTop.addDetailRow(getString(R.string.boc_invest_treaty_my_acc_num), accno);
        detailMiddle.setTvTitle(getString(R.string.boc_invest_treaty_message));
        detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_agr_name), infoModel.getAgrName());
        detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_agr_type), setAgrType());

        detailButtom.setTvTitle(getString(R.string.boc_invest_treaty_product_message));
        detailButtom.addDetailRow(getString(R.string.boc_invest_treaty_product_name), infoModel.getProName());
        detailButtom.addDetailRow(getString(R.string.boc_invest_treaty_pro_cur), getCurrney());
        if (Double.valueOf(infoModel.getRatedetail()) <= 0) {
            detailButtom.addDetailRow(getString(R.string.boc_invest_treaty_rate_detail), MoneyUtils.transRatePercentTypeFormat(infoModel.getRate()));
        } else {
            detailButtom.addDetailRow(getString(R.string.boc_invest_treaty_rate_detail), MoneyUtils.transRatePercentTypeFormat(infoModel.getRate()) + "~" + MoneyUtils.transRatePercentTypeFormat(infoModel.getRatedetail()));
        }
        detailButtom.addDetailRow(getString(R.string.boc_invest_treaty_agr_prostart), MoneyUtils.transMoneyFormat(infoModel.getAgrPurstart(), infoModel.getProCur()));

        switch (infoModel.getInstType()) {
            case "1"://周期连续协议
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_agr_code), infoModel.getAgrCode());
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_inst_type), setInstType());
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_total), setPeriodtotal(infoModel.getPeriodtotal()));
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period), infoModel.getPeriod());
                }
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_age), setPeriodAge(infoModel.getPeriodAge()));
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_minin_period), infoModel.getMininsperiod());
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_one), setPeriodAge(infoModel.getOneperiod()));
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_first_date_pur), infoModel.getFirstdatepur());
                }
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount_type), setAmountType());
                if ("1".equals(infoModel.getAmountType())) {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_my_min_amount), MoneyUtils.transMoneyFormat(infoModel.getMinAmount(), infoModel.getProCur()));
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_my_max_amount), MoneyUtils.transMoneyFormat(infoModel.getMaxAmount(), infoModel.getProCur()));
                } else if ("0".equals(infoModel.getAmountType())) {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount), MoneyUtils.transMoneyFormat(infoModel.getAmount(), infoModel.getProCur()));
                }
                if (isValid) {
                    if ("1".equals(infoModel.getCanUpdate())) {
                        detailTop.addDetailAlterRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                    } else {
                        detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                    }
                } else {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                }
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_finish_period), infoModel.getFinishperiod());
                if (isValid) {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_remain_period), setPeriodtotal(infoModel.getRemaindperiod()));
                }
                break;
            case "2"://周期不连续协议
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_agr_code), infoModel.getAgrCode());
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_inst_type), setInstType());
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_total), setPeriodtotal(infoModel.getPeriodtotal()));
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period), infoModel.getPeriod());
                }
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_age), setPeriodAge(infoModel.getPeriodAge()));
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_minin_period), infoModel.getMininsperiod());
                }
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_one), setPeriodAge(infoModel.getOneperiod()));
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_is_need_pur), getString(R.string.boc_invest_treaty_every) + setPeriodAge(infoModel.getPeriodpur()) + getString(R.string.boc_invest_treaty_isneedpur));
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_first_date_pur), infoModel.getFirstdatepur());
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_is_need_red), getString(R.string.boc_invest_treaty_every) + setPeriodAge(infoModel.getPeriodred()) + getString(R.string.boc_invest_treaty_isneedred));
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_red), infoModel.getFirstdatered());
                }
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount_type), setAmountType());
                if ("1".equals(infoModel.getAmountType())) {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_my_min_amount), MoneyUtils.transMoneyFormat(infoModel.getMinAmount(), infoModel.getProCur()));
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_my_max_amount), MoneyUtils.transMoneyFormat(infoModel.getMaxAmount(), infoModel.getProCur()));
                } else if ("0".equals(infoModel.getAmountType())) {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount), MoneyUtils.transMoneyFormat(infoModel.getAmount(), infoModel.getProCur()));
                }
                if (isValid) {
                    if ("1".equals(infoModel.getCanUpdate())) {
                        detailTop.addDetailAlterRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                    } else {
                        detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                    }
                } else {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                }
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_finish_period), infoModel.getFinishperiod());
                if (isValid) {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_remain_period), setPeriodtotal(infoModel.getRemaindperiod()));
                }
                break;
            case "3"://多次购买协议
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_agr_code), infoModel.getAgrCode());
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_inst_type), setInstType());
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_total), setPeriodtotal(infoModel.getPeriodtotal()));
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period), infoModel.getPeriod());
                }
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_age), setPeriodAge(infoModel.getPeriodAge()));
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_minin_period), infoModel.getMininsperiod());
                }
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_is_need_pur), getString(R.string.boc_invest_treaty_every) + setPeriodAge(infoModel.getPeriodpur()) + getString(R.string.boc_invest_treaty_isneedpur));
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_first_date_pur), infoModel.getFirstdatepur());
                }
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_is_need_red_precent), getIsneedred());
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_red), infoModel.getFirstdatered());
                }
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount_type), setAmountType());
                if ("1".equals(infoModel.getAmountType())) {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_my_min_amount), MoneyUtils.transMoneyFormat(infoModel.getMinAmount(), infoModel.getProCur()));
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_my_max_amount), MoneyUtils.transMoneyFormat(infoModel.getMaxAmount(), infoModel.getProCur()));
                } else if ("0".equals(infoModel.getAmountType())) {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount), MoneyUtils.transMoneyFormat(infoModel.getAmount(), infoModel.getProCur()));
                }
                if (isValid) {
                    if ("1".equals(infoModel.getCanUpdate())) {
                        detailTop.addDetailAlterRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                    } else {
                        detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                    }
                } else {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                }
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_finish_period), infoModel.getFinishperiod());
                if (isValid) {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_remain_period), setPeriodtotal(infoModel.getRemaindperiod()));
                }
                break;
            case "4"://多次赎回协议
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_agr_code), infoModel.getAgrCode());
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_inst_type), setInstType());
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_total), setPeriodtotal(infoModel.getPeriodtotal()));
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period), infoModel.getPeriod());
                }
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_age), setPeriodAge(infoModel.getPeriodAge()));
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_minin_period), infoModel.getMininsperiod());
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_first_date_pur), infoModel.getFirstdatepur());
                }
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_is_need_red), getString(R.string.boc_invest_treaty_every) + setPeriodAge(infoModel.getPeriodred()) + getString(R.string.boc_invest_treaty_isneedred));
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_red), infoModel.getFirstdatered());
                }
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_is_need_pur_precent), getIsneedpur());
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount_type), setAmountType());
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_unit_precent), infoModel.getUnit());
                if (isValid) {
                    if ("1".equals(infoModel.getCanUpdate())) {
                        detailTop.addDetailAlterRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                    } else {
                        detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                    }
                } else {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                }
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_finish_period), infoModel.getFinishperiod());
                if (isValid) {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_remain_period), setPeriodtotal(infoModel.getRemaindperiod()));
                }
                if ("0".equals(infoModel.getIsneedpur())) {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_money_precent), MoneyUtils.transMoneyFormat(infoModel.getAmount(), infoModel.getProCur()));
                }
                break;
            case "5"://定时定额投资
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_agr_code), infoModel.getAgrCode());
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_trade_code_precent), setTdsType(infoModel.getTradeCode()));
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_age), setPeriodAge(infoModel.getPeriodAge()));
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_minin_period), infoModel.getMininsperiod());
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_one), setPeriodAge(infoModel.getOneperiod()));
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_first_date_pur), infoModel.getFirstdatepur());
                }
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount_type), setAmountType());
                if (isValid) {
                    if ("1".equals(infoModel.getCanUpdate())) {
                        detailTop.addDetailAlterRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                    } else {
                        detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                    }
                } else {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                }
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_finish_period), infoModel.getFinishperiod());
                if (isValid) {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_remain_period), setPeriodtotal(infoModel.getRemaindperiod()));
                }
                if (isValid) {
                    if ("0".equals(infoModel.getTradeCode())) {
                        if ("1".equals(infoModel.getCanUpdate())) {
                            detailTop.addDetailAlterRow(getString(R.string.boc_invest_treaty_amount_fen_precent), setAmount(infoModel.getAmount()));
                        } else {
                            detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount_fen_precent), setAmount(infoModel.getAmount()));
                        }
                    } else {
                        if ("1".equals(infoModel.getCanUpdate())) {
                            detailTop.addDetailAlterRow(getString(R.string.boc_invest_treaty_amount_jin_precent), MoneyUtils.transMoneyFormat(infoModel.getAmount(), infoModel.getProCur()));
                        } else {
                            detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount_jin_precent), MoneyUtils.transMoneyFormat(infoModel.getAmount(), infoModel.getProCur()));
                        }
                    }
                } else {
                    if ("0".equals(infoModel.getTradeCode())) {
                        detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount_fen_precent), setAmount(infoModel.getAmount()));
                    } else {
                        detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount_jin_precent), MoneyUtils.transMoneyFormat(infoModel.getAmount(), infoModel.getProCur()));
                    }
                }
                break;
            case "6"://余额理财投资
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_agr_code), infoModel.getAgrCode());
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_minin_period), infoModel.getMininsperiod());
                }
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_firist_date_pur_precent), infoModel.getFirstdatepur());
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount_type), setAmountType());
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_finish_period), infoModel.getFinishperiod());
                if (isValid) {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_remain_period), setPeriodtotal(infoModel.getRemaindperiod()));
                }
                if (isValid) {
                    if ("1".equals(infoModel.getCanUpdate())) {
                        detailTop.addDetailAlterRow(getString(R.string.boc_invest_treaty_trade_code_min_amount), MoneyUtils.transMoneyFormat(infoModel.getMinAmount(), infoModel.getProCur()));
                        detailTop.addDetailAlterRow(getString(R.string.boc_invest_treaty_trade_code_max_amount), MoneyUtils.transMoneyFormat(infoModel.getMaxAmount(), infoModel.getProCur()));
                    } else {
                        detailTop.addDetailRow(getString(R.string.boc_invest_treaty_trade_code_min_amount), MoneyUtils.transMoneyFormat(infoModel.getMinAmount(), infoModel.getProCur()));
                        detailTop.addDetailRow(getString(R.string.boc_invest_treaty_trade_code_max_amount), MoneyUtils.transMoneyFormat(infoModel.getMaxAmount(), infoModel.getProCur()));
                    }
                } else {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_trade_code_min_amount), MoneyUtils.transMoneyFormat(infoModel.getMinAmount(), infoModel.getProCur()));
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_trade_code_max_amount), MoneyUtils.transMoneyFormat(infoModel.getMaxAmount(), infoModel.getProCur()));
                }
                break;
            case "7"://周期滚续协议
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_agr_code), infoModel.getAgrCode());
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_total), infoModel.getPeriodtotal());
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period), infoModel.getPeriod());
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_minin_period), infoModel.getMininsperiod());
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_one), setPeriodAge(infoModel.getOneperiod()));
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_first_date_pur), infoModel.getFirstdatepur());
                }
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_age), setPeriodAge(infoModel.getPeriodAge()));
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount_type), setAmountType());
                if (isValid) {
                    if ("1".equals(infoModel.getCanUpdate())) {
                        detailTop.addDetailAlterRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                    } else {
                        detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                    }
                } else {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                }
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_finish_period), infoModel.getFinishperiod());
                if (isValid) {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_remain_period), setPeriodtotal(infoModel.getRemaindperiod()));
                }
                if (isValid) {
                    if ("1".equals(infoModel.getAmountType())) {
                        if ("1".equals(infoModel.getCanUpdate())) {
                            detailTop.addDetailAlterRow(getString(R.string.boc_invest_treaty_trade_min_amount), MoneyUtils.transMoneyFormat(infoModel.getMinAmount(), infoModel.getProCur()));
                            detailTop.addDetailAlterRow(getString(R.string.boc_invest_treaty_trade_max_amount), MoneyUtils.transMoneyFormat(infoModel.getMaxAmount(), infoModel.getProCur()));
                        } else {
                            detailTop.addDetailRow(getString(R.string.boc_invest_treaty_trade_min_amount), MoneyUtils.transMoneyFormat(infoModel.getMinAmount(), infoModel.getProCur()));
                            detailTop.addDetailRow(getString(R.string.boc_invest_treaty_trade_max_amount), MoneyUtils.transMoneyFormat(infoModel.getMaxAmount(), infoModel.getProCur()));
                        }
                    } else {
                        if ("1".equals(infoModel.getCanUpdate())) {
                            detailTop.addDetailAlterRow(getString(R.string.boc_invest_treaty_amount), MoneyUtils.transMoneyFormat(infoModel.getAmount(), infoModel.getProCur()));
                        } else {
                            detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount), MoneyUtils.transMoneyFormat(infoModel.getAmount(), infoModel.getProCur()));
                        }
                    }
                } else {
                    if ("1".equals(infoModel.getAmountType())) {
                        detailTop.addDetailRow(getString(R.string.boc_invest_treaty_trade_min_amount), MoneyUtils.transMoneyFormat(infoModel.getMinAmount(), infoModel.getProCur()));
                        detailTop.addDetailRow(getString(R.string.boc_invest_treaty_trade_max_amount), MoneyUtils.transMoneyFormat(infoModel.getMaxAmount(), infoModel.getProCur()));
                    } else {
                        detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount), MoneyUtils.transMoneyFormat(infoModel.getAmount(), infoModel.getProCur()));
                    }
                }
                break;
            case "8"://业绩基准周期滚续
                detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_age), setPeriodAge(infoModel.getPeriodAge()));
                if (isValid) {
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_minin_period), infoModel.getMininsperiod());
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_period_one), setPeriodAge(infoModel.getOneperiod()));
                    detailMiddle.addDetailRow(getString(R.string.boc_invest_treaty_first_date_pur), infoModel.getFirstdatepur());
                }
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount_type), setAmountType());
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_amount), MoneyUtils.transMoneyFormat(infoModel.getAmount(), infoModel.getProCur()));
                if (isValid) {
                    if ("1".equals(infoModel.getCanUpdate())) {
                        detailTop.addDetailAlterRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                    } else {
                        detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                    }
                } else {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                }
                detailTop.addDetailRow(getString(R.string.boc_invest_treaty_finish_period), infoModel.getFinishperiod());
                if (isValid) {
                    detailTop.addDetailRow(getString(R.string.boc_invest_treaty_remain_period), setPeriodtotal(infoModel.getRemaindperiod()));
                }
                break;
        }
    }

    /**
     *  币种显示的转换
     * @return
     */
    private String getCurrney() {
        String currency = PublicCodeUtils.getCurrency(getContext(), infoModel.getProCur());
        String cashRemit = infoModel.getCashRemit().equals("01") ? getString(R.string.boc_invest_treaty_pro_cur01) : getString(R.string.boc_invest_treaty_pro_cur02);
        if (infoModel.getProCur().equals("000") || infoModel.getProCur().equals("001") || infoModel.getProCur().equals("zzz")){
            return  currency;
        }else {
            return currency + "/" + cashRemit;
        }
    }

    /**
     * 点击底部按钮
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bottom_left) {
            setStopTreaty();
        } else if (v.getId() == R.id.bottom_right) {
            setAlterTreaty();
        }
    }


    @Override
    public void onClickListener() {
        setAlterTreaty();
    }

    /**
     * 终止协议弹框提示
     */
    private void setStopTreaty() {
        if (cancelDialog == null) {
            cancelDialog = new TitleAndBtnDialog(getActivity());
            cancelDialog.setBtnName(new String[]{getString(R.string.boc_common_cancel), getString(R.string.boc_common_sure)});
            cancelDialog.setGravity(Gravity.CENTER_VERTICAL);
            switch (infoModel.getInstType()) {
                case "1"://周期连续协议
                case "2"://周期不连续协议
                case "3"://多次购买协议
                case "4"://多次赎回协议
                    cancelDialog.setNoticeContent(getString(R.string.boc_invest_treaty_stop_capacity));
                    break;
                case "5"://定时定额投资
                case "6"://余额理财投资
                    cancelDialog.setNoticeContent(getString(R.string.boc_invest_treaty_stop_period));
                    break;
                case "7"://周期滚续协议
                case "8"://业绩基准周期滚续
                    cancelDialog.setNoticeContent(getString(R.string.boc_invest_treaty_stop_timing));
                    break;
            }
            cancelDialog.setTitleBackground(getResources().getColor(R.color.boc_common_cell_color));
            cancelDialog.setLeftBtnTextBgColor(getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color));
            cancelDialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red));
            cancelDialog.setDialogBtnClickListener(this);
        }
        cancelDialog.show();
    }

    @Override
    public void onLeftBtnClick(View view) {
        cancelDialog.dismiss();
    }

    /**
     * 终止协议请求
     *
     * @param view
     */
    @Override
    public void onRightBtnClick(View view) {
        cancelDialog.dismiss();
        showLoadingDialog();
        if ("8".equals(infoModel.getInstType())) {
            getPresenter().psnXpadBenchmarkMaintainResult(infoConfirmModel, model, "2");
        } else {
            getPresenter().psnXpadInvestAgreementCancel(infoModel, model);
        }
    }

    /**
     * 修改协议内容弹框提示
     */
    private void setAlterTreaty() {
        alterDialog = new InvestTreatyDialog(getActivity());
        switch (infoModel.getInstType()) {
            case "1"://周期连续协议
            case "2"://周期不连续协议
            case "3"://多次购买协议
            case "4"://多次赎回协议
                alterDialog.llEtPeriodVisibility(infoModel.getBuyPeriod(), false);
                break;
            case "5"://定时定额投资
                if ("-1".equals(infoModel.getBuyPeriod()) || "-1".equals(infoModel.getRemaindperiod())) {
                    alterDialog.llEtBuyPeriodVisibility("", true);
                } else {
                    alterDialog.llEtPeriodVisibility(infoModel.getBuyPeriod(), true);
                }
                alterDialog.llEtBaseAmountVisibility(infoModel.getAmount(), infoModel.getProCur(), infoModel.getTradeCode());
                break;
            case "6"://余额理财投资
                alterDialog.llEtRedemmAmountVisibility(infoModel.getMinAmount(), infoModel.getMaxAmount(), infoModel.getProCur());
                break;
            case "7"://周期滚续协议
                alterDialog.llEtPeriodVisibility(infoModel.getBuyPeriod(), false);
                if ("1".equals(infoModel.getAmountType())) {
                    alterDialog.llEtAmountVisibility(infoModel.getMinAmount(), infoModel.getMaxAmount(), infoModel.getProCur());
                } else {
                    alterDialog.llEtInvestBaseAmountVisibility(infoModel.getAmount(), infoModel.getProCur());
                }
                break;
            case "8"://业绩基准周期滚续
                alterDialog.llEtPeriodVisibility(infoModel.getBuyPeriod(), false);
                break;
        }
        if ("-1".equals(infoModel.getRemaindperiod())) {
            alterDialog.setNoticeContent(getString(R.string.boc_invest_treaty_period_num) + infoModel.getFinishperiod() + getString(R.string.boc_invest_treaty_period_num1));
        } else {
            alterDialog.setNoticeContent(getString(R.string.boc_invest_treaty_period_num) + infoModel.getFinishperiod() + getString(R.string.boc_invest_treaty_period_num2) + infoModel.getRemaindperiod() + getString(R.string.boc_invest_treaty_period_num3));
        }
        alterDialog.setDialogBtnClickListener(new InvestTreatyDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {
                alterDialog.dismiss();
            }

            @Override
            public void onRightBtnClick(View view) {
                switch (infoModel.getInstType()) {
                    case "1"://周期连续协议
                    case "2"://周期不连续协议
                    case "3"://多次购买协议
                    case "4"://多次赎回协议
                        if (StringUtils.isEmptyOrNull(alterDialog.inputPeriod)) {
                            showErrorDialog(getString(R.string.boc_invest_treaty_import_buy_period));
                            return;
                        }
                        if ("0".equals(alterDialog.inputPeriod)) {
                            showErrorDialog(getString(R.string.boc_invest_treaty_empty_zero_buy_period_precent));
                            return;
                        }
                        infoConfirmModel.setBuyPeriod(alterDialog.inputPeriod);
                        break;
                    case "5"://定时定额投资
                        if ("-1".equals(infoModel.getBuyPeriod())) {
                            if (!alterDialog.cbCheck.isChecked() && StringUtils.isEmptyOrNull(alterDialog.inputPeriod)) {
                                showErrorDialog(getString(R.string.boc_invest_treaty_import_buy_period));
                                return;
                            }
                            if ("0".equals(alterDialog.inputPeriod)) {
                                showErrorDialog(getString(R.string.boc_invest_treaty_empty_zero_buy_period_precent));
                                return;
                            }
                            if (alterDialog.cbCheck.isChecked()) {
                                infoConfirmModel.setBuyPeriod("-1");
                            } else {
                                infoConfirmModel.setBuyPeriod(alterDialog.inputPeriod);
                            }
                        } else {
                            if (StringUtils.isEmptyOrNull(alterDialog.inputPeriod)) {
                                showErrorDialog(getString(R.string.boc_invest_treaty_import_buy_period));
                                return;
                            }
                            if ("0".equals(alterDialog.inputPeriod)) {
                                showErrorDialog(getString(R.string.boc_invest_treaty_empty_zero_buy_period_precent));
                                return;
                            }
                            infoConfirmModel.setBuyPeriod(alterDialog.inputPeriod);
                        }
                        if ("0".equals(infoModel.getTradeCode())) {
                            if (StringUtils.isEmptyOrNull(alterDialog.inputMinAmount)) {
                                showErrorDialog(getString(R.string.boc_invest_treaty_empty_treaty_isneedpur));
                                return;
                            }
                        } else {
                            if (StringUtils.isEmptyOrNull(alterDialog.inputMinAmount)) {
                                showErrorDialog(getString(R.string.boc_invest_treaty_empty_treaty_isneedred));
                                return;
                            }

                            if ("0.00".equals(alterDialog.inputMinAmount)) {
                                showErrorDialog(getString(R.string.boc_invest_treaty_empty_zero_treaty_isneedred));
                                return;
                            }
                        }
                        infoConfirmModel.setAmount(alterDialog.inputMinAmount);
                        break;
                    case "6"://余额理财投资
                        if (StringUtils.isEmptyOrNull(alterDialog.inputMinAmount)) {
                            showErrorDialog(getString(R.string.boc_invest_treaty_empty_treaty_reddeem_min));
                            return;
                        }
                        if (StringUtils.isEmptyOrNull(alterDialog.inputMaxAmount)) {
                            showErrorDialog(getString(R.string.boc_invest_treaty_empty_treaty_buy_max));
                            return;
                        }
                        if (Double.valueOf(alterDialog.inputMaxAmount) < Double.valueOf(alterDialog.inputMinAmount)) {
                            showErrorDialog(getString(R.string.boc_invest_treaty_redeem_buy));
                            return;
                        }
                        infoConfirmModel.setMinAmount(alterDialog.inputMinAmount);
                        infoConfirmModel.setMaxAmount(alterDialog.inputMaxAmount);
                        break;
                    case "7"://周期滚续协议
                        if ("1".equals(infoModel.getAmountType())) {
                            if (StringUtils.isEmptyOrNull(alterDialog.inputMinAmount)) {
                                showErrorDialog(getString(R.string.boc_invest_treaty_empty_treaty_min_redeem));
                                return;
                            }
                            if (StringUtils.isEmptyOrNull(alterDialog.inputMaxAmount)) {
                                showErrorDialog(getString(R.string.boc_invest_treaty_empty_treaty_max_buy));
                                return;
                            }
                            infoConfirmModel.setMinAmount(alterDialog.inputMinAmount);
                            infoConfirmModel.setMinAmount(alterDialog.inputMaxAmount);
                        } else if ("0".equals(infoModel.getAmountType())) {
                            if (StringUtils.isEmptyOrNull(alterDialog.inputMinAmount)) {
                                showErrorDialog(getString(R.string.boc_invest_treaty_empty_treaty_base_amount));
                                return;
                            }
                            if ("0.00".equals(alterDialog.inputMinAmount)) {
                                showErrorDialog(getString(R.string.boc_invest_treaty_empty_zero_treaty_base_amount));
                                return;
                            }
                            infoConfirmModel.setAmount(alterDialog.inputMinAmount);
                        }
                        if (StringUtils.isEmptyOrNull(alterDialog.inputPeriod)) {
                            showErrorDialog(getString(R.string.boc_invest_treaty_import_buy_period));
                            return;
                        }
                        if ("0".equals(alterDialog.inputPeriod)) {
                            showErrorDialog(getString(R.string.boc_invest_treaty_empty_zero_buy_period_precent));
                            return;
                        }
                        infoConfirmModel.setBuyPeriod(alterDialog.inputPeriod);
                        break;
                    case "8"://业绩基准周期滚续
                        if (StringUtils.isEmptyOrNull(alterDialog.inputPeriod)) {
                            showErrorDialog(getString(R.string.boc_invest_treaty_import_buy_period));
                            return;
                        }
                        if ("0".equals(alterDialog.inputPeriod)) {
                            showErrorDialog(getString(R.string.boc_invest_treaty_empty_zero_buy_period_precent));
                            return;
                        }
                        infoConfirmModel.setBuyPeriod(alterDialog.inputPeriod);
                        break;
                }
                showLoadingDialog();
                getPresenter().psnXpadQueryRiskMatch(model, infoModel);
            }
        });
        alterDialog.show();
    }

    @Override
    protected InvestTreatyPresenter initPresenter() {
        return new InvestTreatyPresenter(this);
    }

    /**
     * 查询客户风险等级与产品风险等级是否匹配
     */
    @Override
    public void psnXpadQueryRiskMatch(PsnXpadQueryRiskMatchReqModel reqModel) {
        alterDialog.dismiss();
        closeProgressDialog();
        if (reqModel.getRiskMatch().equals("0")) {
            start(new InvestTreatyConfirmFragment(infoConfirmModel, model));
        } else if (reqModel.getRiskMatch().equals("1")) {
            showConfirmRisk();
        } else if (reqModel.getRiskMatch().equals("2")) {
            showRiskAssess();
        }
    }

    private void showConfirmRisk() {
        if (dialog == null) {
            dialog = new TitleAndBtnDialog(getActivity());
            dialog.setBtnName(new String[]{getString(R.string.boc_invest_treaty_change_period_up), getString(R.string.boc_invest_treaty_change_period_sure)});
            dialog.setGravity(Gravity.CENTER_VERTICAL);
            dialog.setNoticeContent(getString(R.string.boc_invest_treaty_change_period_sure_content));
            dialog.setLeftBtnTextBgColor(getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color));
            dialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red));
            dialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
                @Override
                public void onLeftBtnClick(View view) {
                    dialog.dismiss();
                }

                @Override
                public void onRightBtnClick(View view) {
                    start(new InvestTreatyConfirmFragment(infoConfirmModel, model));
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    private void showRiskAssess() {
        if (riskDialog == null) {
            riskDialog = new TitleAndBtnDialog(getActivity());
            riskDialog.setBtnName(new String[]{getString(R.string.boc_invest_treaty_change_period_up), getString(R.string.boc_invest_treaty_change_period_again_risk)});
            riskDialog.setGravity(Gravity.CENTER_VERTICAL);
            riskDialog.setNoticeContent(getString(R.string.boc_invest_treaty_change_period_risk_content));
            riskDialog.setLeftBtnTextBgColor(getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color));
            riskDialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red));
            riskDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
                @Override
                public void onLeftBtnClick(View view) {
                    riskDialog.dismiss();
                }

                @Override
                public void onRightBtnClick(View view) {
                    start(new RiskAssessFragment(InvestTreatyInfoFragment.class));
                    riskDialog.dismiss();
                }
            });
        }
        riskDialog.show();
    }
}
