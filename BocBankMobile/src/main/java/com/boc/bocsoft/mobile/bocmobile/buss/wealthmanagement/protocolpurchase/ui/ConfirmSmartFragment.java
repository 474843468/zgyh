package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolIntelligentConfirmBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.presenter.ProtocolPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.utils.ProtocolConvertUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;

import java.util.ArrayList;

/**
 * 智能投资协议--确认页
 * Created by liuweidong on 2016/11/7.
 */
public class ConfirmSmartFragment extends MvpBussFragment<ProtocolPresenter> implements ProtocolContact.ProtocolSmartConfirmView {
    private View rootView;
    protected TextView txtContent1;
    protected TextView txtContent2;
    protected TextView txtContent3;// 多次赎回使用
    protected SpannableString txtRiskMessage;// 温馨提示
    protected Button btnOk;

    private ProtocolModel mViewModel;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_smart_confirm, null);
        return rootView;
    }

    @Override
    public void initView() {
        txtContent1 = (TextView) rootView.findViewById(R.id.txt_content_1);
        txtContent2 = (TextView) rootView.findViewById(R.id.txt_content_2);
        txtContent3 = (TextView) rootView.findViewById(R.id.txt_content_3);
        txtRiskMessage = (SpannableString) rootView.findViewById(R.id.txt_risk_message);
        btnOk = (Button) rootView.findViewById(R.id.btn_ok);
    }

    @Override
    public void initData() {
        mViewModel = getArguments().getParcelable(ProtocolSmartFragment.MODEL);
        ProtocolIntelligentConfirmBean confirmBean = mViewModel.getConfirmBean();// 响应返回数据
        /*单期购买金额*/
        String money = MoneyUtils.transMoneyFormat(confirmBean.getAmount(), mViewModel.getCurCode()) + PublicCodeUtils.getCurrency(mContext, mViewModel.getCurCode());
        String minMoney = MoneyUtils.transMoneyFormat(confirmBean.getMinAmount(), mViewModel.getCurCode()) + PublicCodeUtils.getCurrency(mContext, mViewModel.getCurCode());
        String maxMoney = MoneyUtils.transMoneyFormat(confirmBean.getMaxAmount(), mViewModel.getCurCode()) + PublicCodeUtils.getCurrency(mContext, mViewModel.getCurCode());
        /*协议总持续天数*/
        String lastDays = ProtocolConvertUtils.convertPeriodAgr(confirmBean.getLastDays());
        /*协议周期*/
        String periodAgr = ProtocolConvertUtils.convertPeriodAgr(confirmBean.getPeriodAgr());

        if (mViewModel.selectedProtocol.getInstType().equals("1")) {// 周期连续协议
            String head = Html.fromHtml(mContext.getString(R.string.boc_protocol_confirm_continue_head, confirmBean.getAgrName(),
                    confirmBean.getFirstDatePur(), "<b>" + MoneyUtils.transMoneyFormat(confirmBean.getAmount(), mViewModel.getCurCode()) + "</b>")).toString();
            if (txtContent1.getPaint().measureText(head) > txtContent1.getWidth()) {
                txtContent1.setText(Html.fromHtml(mContext.getString(R.string.boc_protocol_confirm_continue_1, confirmBean.getAgrName(),
                        confirmBean.getFirstDatePur(), "<b>" + money + "</b>", confirmBean.getTotalPeriod(), confirmBean.getEachpbalDays(), lastDays)));
            } else {
                txtContent1.setText(Html.fromHtml(mContext.getString(R.string.boc_protocol_confirm_continue, confirmBean.getAgrName(),
                        confirmBean.getFirstDatePur(), "<b>" + money + "</b>", confirmBean.getTotalPeriod(), confirmBean.getEachpbalDays(), lastDays)));
            }
            txtContent2.setText(mContext.getString(R.string.boc_protocol_confirm_continue_common, confirmBean.getFailMax()));
        } else if (mViewModel.selectedProtocol.getInstType().equals("2")) {// 周期不连续协议
            if ("0".equals(mViewModel.getAmountType())) {// 定额
                txtContent1.setText(Html.fromHtml(mContext.getString(R.string.boc_protocol_confirm_no_continue_0, confirmBean.getAgrName(),
                        confirmBean.getFirstDatePur(), "<b>" + money + "</b>", confirmBean.getFirstDateRed(),
                        periodAgr, confirmBean.getTotalPeriod(), confirmBean.getEndDate(), confirmBean.getEachpbalDays())));
            } else if ("1".equals(mViewModel.getAmountType())) {// 不定额
                txtContent1.setText(mContext.getString(R.string.boc_protocol_confirm_no_continue_1, confirmBean.getAgrName(),
                        confirmBean.getFirstDatePur(), minMoney, maxMoney,
                        confirmBean.getFirstDateRed(), periodAgr, confirmBean.getTotalPeriod(), confirmBean.getEndDate(), confirmBean.getEachpbalDays()));
            }
            txtContent2.setText(mContext.getString(R.string.boc_protocol_confirm_continue_common, confirmBean.getFailMax()));
        } else if (mViewModel.selectedProtocol.getInstType().equals("3")) {// 多次购买协议
            txtContent1.setText(Html.fromHtml(mContext.getString(R.string.boc_protocol_confirm_buy, confirmBean.getAgrName(), confirmBean.getFirstDatePur(),
                    periodAgr, "<b>" + money + "</b>", confirmBean.getTotalPeriod(), lastDays, confirmBean.getWillPurCount(), confirmBean.getWillRedCount())));
            txtContent2.setText(mContext.getString(R.string.boc_protocol_confirm_continue_common, confirmBean.getFailMax()));
        } else if (mViewModel.selectedProtocol.getInstType().equals("4")) {// 多次赎回协议
            if ("0".equals(confirmBean.getIsNeedPur())) {// 期初申购
                txtContent3.setVisibility(View.VISIBLE);
                txtContent1.setText(Html.fromHtml(mContext.getString(R.string.boc_protocol_confirm_redeem_1, confirmBean.getAgrName(), confirmBean.getFirstDatePur(), "<b>" + money + "</b>")));
                txtContent2.setText(mContext.getString(R.string.boc_protocol_confirm_redeem_2, confirmBean.getFirstDateRed(), confirmBean.getOneTmredAmt(),
                        confirmBean.getPeriodAgr(), confirmBean.getOneTmredAmt(), confirmBean.getTotalPeriod(), lastDays,
                        confirmBean.getEndDate(), confirmBean.getWillPurCount(), confirmBean.getWillRedCount()));
                txtContent3.setText(mContext.getString(R.string.boc_protocol_confirm_continue_common, confirmBean.getFailMax()));
            } else {// 不申购
                txtContent3.setVisibility(View.GONE);
                txtContent1.setText(mContext.getString(R.string.boc_protocol_confirm_redeem_0, confirmBean.getAgrName()) + mContext.getString(R.string.boc_protocol_confirm_redeem_2, confirmBean.getFirstDateRed(), confirmBean.getOneTmredAmt(),
                        confirmBean.getPeriodAgr(), confirmBean.getOneTmredAmt(), confirmBean.getTotalPeriod(), lastDays,
                        confirmBean.getEndDate(), confirmBean.getWillPurCount(), confirmBean.getWillRedCount()));
                txtContent2.setText(mContext.getString(R.string.boc_protocol_confirm_continue_common, confirmBean.getFailMax()));
            }
        }
        setRiskMessage();
    }

    @Override
    public void setListener() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().resultTreaty(mViewModel);// 提交交易
            }
        });
    }

    @Override
    protected ProtocolPresenter initPresenter() {
        return new ProtocolPresenter(this);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "确认信息";
    }

    /**
     * 设置温馨提示
     */
    private void setRiskMessage() {
        String riskMsg = ProtocolConvertUtils.convertRiskMsg(mContext, mViewModel.getRiskMsg());
        String riskMessage = riskMsg + getString(R.string.boc_protocol_smart_tips_added);
        txtRiskMessage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        txtRiskMessage.setContent(getString(R.string.boc_confirm_hint), riskMessage, R.color.boc_text_color_red,
                R.color.boc_text_color_red, new StyleSpan(Typeface.DEFAULT.getStyle()));
    }

    /**
     * 提交交易成功
     */
    @Override
    public void resultTreatySuccess() {
        getPresenter().queryContinueProductList(mViewModel.getProId(), mViewModel.getAccountList().get(0).getAccountId(), mViewModel.getCurCode());
    }

    @Override
    public void queryProductListSuccess(ArrayList<WealthListBean> wealthListBeans) {
        ResultSmartFragment fragment = new ResultSmartFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ResultSmartFragment.WEALTHLIST, wealthListBeans);
        bundle.putParcelable(ResultSmartFragment.VIEWMODEL, mViewModel);
        fragment.setArguments(bundle);
        start(fragment);// 跳转到结果页
    }
}
