package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui;

import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.pdf.PDFFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.assignment.SelectAgreementView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialog.MessageDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.ui.RiskAssessFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolIntelligentDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.presenter.ProtocolPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.data.BuildViewData;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.widget.SelectedDialogNew;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 智能投资协议--委托页
 * Created by liuweidong on 2016/11/7.
 */
public class ProtocolSmartFragment extends MvpBussFragment<ProtocolContact.Presenter> implements View.OnClickListener, ProtocolContact.ProtocolIntelligentView {
    public static final String SELECT = "select";// 从选择投资协议过来
    public static final String DETAILS = "details";
    public static final String MODEL = "model";
    private View rootView;
    private LinearLayout llParent;
    private TextView txtHead;
    private LinearLayout llHeadList;
    private EditChoiceWidget choiceCurrency;// 外币选择钞汇
    private EditChoiceWidget choiceMode;// 投资金额模式
    private EditMoneyInputWidget money1;
    private EditMoneyInputWidget money2;
    private EditClearWidget editDivider;
    private SelectAgreementView read;
    private Button btnNext;
    private TextView txtAccount;

    private ProtocolIntelligentDetailsBean detailsBean;
    private ProtocolModel mViewModel;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_protocol_intelligent, null);
        return rootView;
    }

    @Override
    public void initView() {
        llParent = (LinearLayout) rootView.findViewById(R.id.ll_parent);
        txtHead = (TextView) rootView.findViewById(R.id.txt_head);
        llHeadList = (LinearLayout) rootView.findViewById(R.id.ll_head_list);
        choiceCurrency = (EditChoiceWidget) rootView.findViewById(R.id.choice_currency);
        choiceMode = (EditChoiceWidget) rootView.findViewById(R.id.choice_mode);
        money1 = (EditMoneyInputWidget) rootView.findViewById(R.id.money_1);
        money2 = (EditMoneyInputWidget) rootView.findViewById(R.id.money_2);
        editDivider = (EditClearWidget) rootView.findViewById(R.id.edt_divider);
        read = (SelectAgreementView) rootView.findViewById(R.id.read);
        btnNext = (Button) rootView.findViewById(R.id.btn_next);
        txtAccount = (TextView) rootView.findViewById(R.id.txt_account);
        setWidgetAttribute();
    }

    @Override
    public void initData() {
        mViewModel = getArguments().getParcelable(MODEL);
        showViewData();
        txtAccount.setText("理财交易账户 " + NumberUtils.formatCardNumberStrong(mViewModel.getAccountList().get(0).getAccountNo()));
        read.setAgreement(mContext.getString(R.string.boc_protocol_read));// 设置阅读
        money1.setCurrency(mViewModel.getCurCode());
        money2.setCurrency(mViewModel.getCurCode());
    }

    @Override
    public void setListener() {
        btnNext.setOnClickListener(this);
        choiceCurrency.setOnClickListener(this);
        choiceMode.setOnClickListener(this);
        read.setOnClickContractListener(new SelectAgreementView.OnClickContractListener() {
            @Override
            public void onClickContract(int index) {
                if (index == 0) {
                    start(ContractFragment.newInstance("file:///android_asset/webviewcontent/wealthmanagement/portfoliopurchase/notice/notice.html"));
                } else {
                    String url = WealthConst.URL_INSTRUCTION + mViewModel.getProId();
                    start(PDFFragment.newInstance(Uri.parse(url)));
                }
            }
        });
        money1.setOnKeyBoardListener(new EditMoneyInputWidget.KeyBoardDismissOrShowCallBack() {
            @Override
            public void onKeyBoardDismiss() {

            }

            @Override
            public void onKeyBoardShow() {
                editDivider.getTitleTextView().setFocusable(true);
                editDivider.getTitleTextView().setFocusableInTouchMode(true);
                editDivider.getTitleTextView().requestFocus();
            }
        });
        money2.setOnKeyBoardListener(new EditMoneyInputWidget.KeyBoardDismissOrShowCallBack() {
            @Override
            public void onKeyBoardDismiss() {

            }

            @Override
            public void onKeyBoardShow() {
                editDivider.getTitleTextView().setFocusable(true);
                editDivider.getTitleTextView().setFocusableInTouchMode(true);
                editDivider.getTitleTextView().requestFocus();
            }
        });
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
        detailsBean = getArguments().getParcelable(DETAILS);
        String titleStr = "";
        /*判断投资方式*/
        if ("1".equals(detailsBean.getInvestType())) {
            titleStr = "周期连续协议";
        } else if ("2".equals(detailsBean.getInvestType())) {
            titleStr = "周期不连续协议";
        } else if ("3".equals(detailsBean.getInvestType())) {
            titleStr = "多次购买协议";
        } else if ("4".equals(detailsBean.getInvestType())) {
            titleStr = "多次赎回协议";
        }
        return titleStr;
    }

    @Override
    protected ProtocolContact.Presenter initPresenter() {
        return new ProtocolPresenter(this);
    }

    public ProtocolModel getViewModel() {
        return mViewModel;
    }

    /**
     * 设置其他组件属性
     */
    private void setWidgetAttribute() {
        money1.setScrollView(llParent);
        money2.setScrollView(llParent);
        money1.getContentMoneyEditText().setTextColor(mContext.getResources().getColor(R.color.boc_text_money_color_red));
        money1.setMaxLeftNumber(12);
        money1.setMaxRightNumber(2);
        money2.setMaxLeftNumber(12);
        money2.setMaxRightNumber(2);
        /*投资期数*/
        editDivider.getContentEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        editDivider.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    /**
     * 页面显示的布局
     */
    private void showViewData() {
        txtHead.setText("[" + PublicCodeUtils.getCurrency(mContext, mViewModel.getCurCode()) + "]" + detailsBean.getProductName());// 设置名称
        if (ApplicationConst.CURRENCY_CNY.equals(mViewModel.getCurCode())) {// 币种
            choiceCurrency.setVisibility(View.GONE);
        } else {
            choiceCurrency.setVisibility(View.VISIBLE);
        }
        if (WealthConst.NO_0.equals(detailsBean.getIsQuota())) {// 不支持非定额
            choiceMode.setArrowImageGone(false);
        }
        if ("1".equals(detailsBean.getInvestType())) {// 周期连续协议
            String[] names = BuildViewData.buildPeriodSeriesName();
            String[] values = BuildViewData.buildPeriodSeriesValue(detailsBean, mViewModel);
            showHeadListView(names, values);
            money2.setVisibility(View.GONE);// 默认显示定额
            money1.setEditWidgetTitle("单期投资金额");
        } else if ("2".equals(detailsBean.getInvestType())) {// 周期不连续协议
            String[] names = BuildViewData.buildPeriodNoSeriesName();
            String[] values = BuildViewData.buildPeriodNoSeriesValue(detailsBean, mViewModel);
            showHeadListView(names, values);
            money2.setVisibility(View.GONE);// 默认显示定额
            money1.setEditWidgetTitle("单期投资金额");
        } else if ("3".equals(detailsBean.getInvestType())) {// 多次购买协议
            String[] names = BuildViewData.buildBuyName(detailsBean.getIsNeedRed());
            String[] values = BuildViewData.buildBuyValue(detailsBean, mViewModel);
            showHeadListView(names, values);
            money2.setVisibility(View.GONE);// 默认显示定额
            money1.setEditWidgetTitle("单期投资金额");
        } else if ("4".equals(detailsBean.getInvestType())) {// 多次赎回协议
            String[] names = BuildViewData.buildRedeemName(detailsBean.getIsNeedPur());
            String[] values = BuildViewData.buildRedeemValue(detailsBean, mViewModel);
            showHeadListView(names, values);
            money2.setVisibility(View.GONE);// 默认显示定额
            money1.setEditWidgetTitle("单次赎回份额");
        }
    }

    private void showHeadListView(String[] names, String[] values) {
        llHeadList.removeAllViews();
        for (int i = 0; i < names.length; i++) {
            View view = View.inflate(mContext, R.layout.boc_fragment_protocol_item, null);
            TextView txtName = (TextView) view.findViewById(R.id.txt_name);
            TextView txtValue = (TextView) view.findViewById(R.id.txt_content);
            txtName.setText(names[i]);
            txtValue.setText(values[i]);
            if ("是否申购".equals(names[i]) || "是否赎回".equals(names[i])) {
                txtValue.setTextColor(mContext.getResources().getColor(R.color.boc_text_color));
            }
            llHeadList.addView(view);
        }
    }

    /**
     * 智能投资确认（请求）
     *
     * @return
     */
    private void buildViewData() {
        mViewModel.setAgrCode(detailsBean.getAgrCode());// 协议编号
        mViewModel.setIsControl("0");// 是否不限期
        if (ApplicationConst.CURRENCY_CNY.equals(mViewModel.getCurCode())) {
            mViewModel.setCharCode("00");
        } else {
            if ("现钞".equals(choiceCurrency.getChoiceTextContent())) {
                mViewModel.setCharCode("01");
            } else if ("现汇".equals(choiceCurrency.getChoiceTextContent())) {
                mViewModel.setCharCode("02");
            }
        }
        if ("定额".equals(choiceMode.getChoiceTextContent())) {
            mViewModel.setAmountType("0");
        } else if ("不定额".equals(choiceMode.getChoiceTextContent())) {
            mViewModel.setAmountType("1");
            mViewModel.setMinAmount(money1.getContentMoney());// 账户保留余额
            mViewModel.setMaxAmount(money2.getContentMoney());// 最大购买金额
        }
        mViewModel.setAmount(money1.getContentMoney());// 单期投资金额
        mViewModel.setTotalPeriod(editDivider.getEditWidgetContent());// 投资期数
    }

    @Override
    public void onClick(View v) {
        final int i = v.getId();
        if (i == R.id.btn_next) {
            if ("1".equals(detailsBean.getInvestType()) || "2".equals(detailsBean.getInvestType()) || "3".equals(detailsBean.getInvestType())) {// 周期连续协议
                if ("定额".equals(choiceMode.getChoiceTextContent())) {
                    if (StringUtils.isEmptyOrNull(money1.getContentMoney()) || new BigDecimal(money1.getContentMoney()).compareTo(new BigDecimal(0)) != 1) {
                        showErrorDialog("请输入大于0的单期投资金额");
                        return;
                    }
                } else {
                    if (StringUtils.isEmptyOrNull(money1.getContentMoney())) {
                        showErrorDialog("请输入账户保留余额");
                        return;
                    }
                    if (StringUtils.isEmptyOrNull(money2.getContentMoney()) || new BigDecimal(money2.getContentMoney()).compareTo(new BigDecimal(0)) != 1) {
                        showErrorDialog("请输入大于0的最大购买金额");
                        return;
                    }
                }
            } else if ("4".equals(detailsBean.getInvestType())) {// 多次赎回
                if (StringUtils.isEmptyOrNull(money1.getContentMoney()) || new BigDecimal(money1.getContentMoney()).compareTo(new BigDecimal(0)) != 1) {
                    showErrorDialog("请输入大于0的单次赎回份额");
                    return;
                }
            }
            if (StringUtils.isEmptyOrNull(editDivider.getEditWidgetContent())) {// 投资期数的校验
                showErrorDialog("请输入投资期数");
                return;
            }
            if (new BigDecimal(editDivider.getEditWidgetContent()).compareTo(new BigDecimal(0)) != 1) {
                showErrorDialog("投资期数输入有误，请输入大于0且最大六位整数");
                return;
            }
            if (!read.isSelected()) {
                showErrorDialog("请阅知勾选产品相关协议后进行下一步操作");
                return;
            }
            getPresenter().queryRiskMatch(mViewModel);// 风险匹配
        } else if (i == R.id.choice_currency) {// 钞汇
            SelectedDialogNew dialog = new SelectedDialogNew(mContext);
            final List<String> list = new ArrayList<>();
            list.add("现钞");
            list.add("现汇");
            dialog.showDialog(list);
            dialog.setTitle("选择钞汇");
            dialog.setListener(new SelectedDialogNew.OnItemSelectDialogClicked() {
                @Override
                public void onListItemClicked(int index) {
                    if (index == 0) {
                        choiceCurrency.setChoiceTextContent("现钞");
                    } else {
                        choiceCurrency.setChoiceTextContent("现汇");
                    }
                }
            });
        } else if (i == R.id.choice_mode) {// 投资金额模式
            if (WealthConst.YES_1.equals(detailsBean.getIsQuota())) {
                SelectedDialogNew dialog = new SelectedDialogNew(mContext);
                final List<String> list = new ArrayList<>();
                list.add("定额");
                list.add("不定额");
                dialog.showDialog(list);
                dialog.setTitle(mContext.getString(R.string.boc_protocol_select_mode));
                dialog.setListener(new SelectedDialogNew.OnItemSelectDialogClicked() {
                    @Override
                    public void onListItemClicked(int index) {// 周期连续与不连续
                        choiceMode.setChoiceTextContent(list.get(index));
                        if (index == 0) {// 定额
                            money1.getContentMoneyEditText().setTextColor(mContext.getResources().getColor(R.color.boc_text_money_color_red));
                            money1.setEditWidgetTitle("单期投资金额");
                            money2.setVisibility(View.GONE);
                            money1.setmContentMoneyEditText("");
                        } else {// 不定额
                            money1.getContentMoneyEditText().setTextColor(mContext.getResources().getColor(R.color.boc_text_color_dark_gray));
                            money2.getContentMoneyEditText().setTextColor(mContext.getResources().getColor(R.color.boc_text_money_color_red));
                            money2.setVisibility(View.VISIBLE);
                            money1.setEditWidgetTitle("账户保留余额");
                            money2.setEditWidgetTitle("最大购买金额");
                            money1.setmContentMoneyEditText("");
                            money2.setmContentMoneyEditText("");
                        }
                    }
                });
            }
        }
    }

    /**
     * 风险匹配成功
     */
    @Override
    public void queryRiskMatchSuccess() {
        buildViewData();
        if ("0".equals(mViewModel.getRiskMatch())) {
            getPresenter().confirmTreaty(mViewModel);// 预交易
        } else if ("1".equals(mViewModel.getRiskMatch())) {
            final MessageDialog dialog = new MessageDialog(getContext());
            dialog.setLeftButtonText("放弃");
            dialog.setRightButtonText("确认购买");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setRightButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().confirmTreaty(mViewModel);// 预交易
                    dialog.dismiss();
                }
            });
            dialog.showDialog(getString(R.string.boc_protocol_risk_hint_1));
        } else if ("2".equals(mViewModel.getRiskMatch())) {
            final MessageDialog dialog = new MessageDialog(getContext());
            dialog.setLeftButtonText("重新风险评估");
            if (findFragment(WealthProductFragment.class) != null) {
                dialog.setRightButtonText("去看看其他产品");
            } else {
                dialog.setRightButtonText("关闭");
            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.showDialog(getString(R.string.boc_protocol_risk_hint_2));
            dialog.setLeftButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    start(new RiskAssessFragment(ProtocolSmartFragment.class));
                }
            });
            dialog.setRightButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (findFragment(WealthProductFragment.class) != null)
                        popToAndReInit(WealthProductFragment.class);
                }
            });
        }
    }

    /**
     * 智能协议申请预交易成功
     */
    @Override
    public void confirmTreatySuccess() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MODEL, mViewModel);
        ConfirmSmartFragment fragment = new ConfirmSmartFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }
}
