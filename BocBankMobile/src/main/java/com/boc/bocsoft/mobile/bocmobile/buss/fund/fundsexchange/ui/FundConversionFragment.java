package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.ui;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundDetail.PsnGetFundDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundDetail.PsnGetFundDetailResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.ClearEditText;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.model.FundConversionConfirmModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.model.FundConversionInputModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.presenter.FundConversionInputPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/13.
 */

public class FundConversionFragment extends MvpBussFragment<FundConversionContract.Presenter> implements FundConversionContract.FundConversionInputQueryView {

    private View rootView;
    private TextView fundNameCode;
    private TextView fundCompany;
    private EditChoiceWidget fromFund;

    private LinearLayout fundDatail;
    private TextView leftTop;
    private TextView leftBottom;
    private TextView middTop;
    private TextView middBottom;
    private TextView rightTop;
    private TextView rightBottom;
    private TextView middDate;

    private ClearEditText sellfundAmount;
    private EditChoiceWidget sellType;

    private CheckBox agreeCheckBox;
    private SpannableString agreeContent;

    private String stringFundName;
    private String stringFundCode;
    private String stringFundCompany;
    private String stringFundCurrency;

    private Button next;

    private FundConversionInputModel fragmentModel;
    private SelectStringListDialog inputFundDialog;
    private SelectStringListDialog fundSellFlagDialog;
    private List<String> inputFundItems;

    private List<String> fundSellValue = new ArrayList<>();
    private String[] fundSellFlag;

    private FundConversionConfirmModel resultModel;
    private static final String BUY_BASELINE = "五万";

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fund_conversion, null);
        return rootView;
    }


    @Override
    public void initView() {
        super.initView();
        fundNameCode = (TextView) rootView.findViewById(R.id.tv_fundname_code);
        fundCompany = (TextView) rootView.findViewById(R.id.tv_fund_company);
        fromFund = (EditChoiceWidget) rootView.findViewById(R.id.item_view);

        fundDatail = (LinearLayout) rootView.findViewById(R.id.ll_fund_detail);
        leftTop = (TextView) rootView.findViewById(R.id.txt_name_left);
        leftBottom = (TextView) rootView.findViewById(R.id.txt_value_left);
        middTop = (TextView) rootView.findViewById(R.id.txt_name_center);
        middBottom = (TextView) rootView.findViewById(R.id.txt_value_center);
        rightTop = (TextView) rootView.findViewById(R.id.txt_name_right);
        rightBottom = (TextView) rootView.findViewById(R.id.txt_value_right);
        middDate = (TextView) rootView.findViewById(R.id.tv_time);

        sellfundAmount = (ClearEditText) rootView.findViewById(R.id.et_account);
        sellType = (EditChoiceWidget) rootView.findViewById(R.id.ew_fund_sell);

        agreeCheckBox = (CheckBox) rootView.findViewById(R.id.cb_agree);
        agreeContent = (SpannableString) rootView.findViewById(R.id.tv_agreement);

        next = (Button) rootView.findViewById(R.id.btn_next);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        stringFundName = bundle.getString(DataUtils.FUND_NAME_KEY, "-");
        stringFundCode = bundle.getString(DataUtils.FUND_CODE_KEY, "-");
        stringFundCompany = bundle.getString(DataUtils.FUND_COMPANY_KEY, "-");
        stringFundCurrency = bundle.getString(DataUtils.FUND_CURRENCY_KEY,"");
        resultModel = new FundConversionConfirmModel();
        resultModel.setFromFundName(stringFundName);
        resultModel.setFromFundCode(stringFundCode);
        resultModel.setFromFundCurrency(stringFundCurrency);
        disPlayView();
        queryConversionInput();
    }

    private void disPlayView() {
        fundNameCode.setText(stringFundName + "（" + stringFundCode + "）");
        fundCompany.setText(stringFundCompany);
        fromFund.setChoiceTitleBold(true);
        fromFund.setChoiceTextName(getString(R.string.boc_fund_conversion_to_title));
        fromFund.setBottomLineVisibility(true);
        sellType.setChoiceTextName(getString(R.string.boc_fund_conversion_sell_format_hint));
        initAgreement();
        initFundSellFlag();
    }

    private void initAgreement() {
        agreeContent.setText("");

        String startTitle1 = getString(R.string.boc_fund_conversion_agreement_start_title1);
        String content1 = getString(R.string.boc_fund_conversion_agreement_content1);
        String endTitle1 = getString(R.string.boc_fund_conversion_agreement_end_title1);
        agreeContent.setAppendContent(startTitle1, endTitle1, content1, new MClickableSpan.OnClickSpanListener() {
            @Override
            public void onClickSpan() {

            }
        });

        String startTitle2 = "";
        String content2 = getString(R.string.boc_fund_conversion_agreement_content2);
        String endTitle2 = getString(R.string.boc_fund_conversion_agreement_end_title2);
        agreeContent.setAppendContent(startTitle2, endTitle2, content2, new MClickableSpan.OnClickSpanListener() {
            @Override
            public void onClickSpan() {

            }
        });
    }

    private void initFundSellFlag() {
        sellType.setChoiceTitleBold(true);
        fundSellFlag = getResources().getStringArray(R.array.boc_fund_sell_other_flag);//"1","2"
        fundSellValue.add(getString(R.string.boc_fund_sell_continue_delay));
        fundSellValue.add(getString(R.string.boc_fund_sell_cancel));
    }


    @Override
    public void setListener() {
        super.setListener();
        fromFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentModel == null || fragmentModel.getListBeans() == null
                        || fragmentModel.getListBeans().size() <= 0) {
                    showErrorDialog(getString(R.string.boc_fund_conversion_input_list_fial));
                    return;
                }
                showChooseInputDialog();

            }
        });

        sellType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFundSellFlagDialog();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //转如基金
                if (StringUtil.isNullOrEmpty(fromFund.getChoiceTextContent())) {
                    showErrorDialog(getString(R.string.boc_fund_conversion_from_hint));
                    return;
                }
                //输入转出份额
                if (StringUtil.isNullOrEmpty(sellfundAmount.getText().toString())){
                    showErrorDialog(getString(R.string.boc_fund_conversion_sell_amount_hint));
                    return;
                }else{
                    resultModel.setAmount(sellfundAmount.getText().toString());
                }
                //赎回处理方式
                if (StringUtil.isNullOrEmpty(sellType.getChoiceTextContent())) {
                    showErrorDialog(getString(R.string.boc_fund_conversion_sell_hint));
                    return;
                }
                //协议
                if (agreeCheckBox.isChecked() == false) {
                    showErrorDialog(getString(R.string.boc_fund_conversion_agreement_hint));
                    return;
                }

                FundConversionConfirmFragment fragment = new FundConversionConfirmFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(DataUtils.FUND_CONVERSION_RESULT_KEY,resultModel);
                fragment.setArguments(bundle);
                start(fragment);
            }
        });
    }


    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fund_conversion_title);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    private void queryConversionInput() {
        showLoadingDialog();
        FundConversionInputModel params = new FundConversionInputModel();
        params.setFromFundCode(stringFundCode);
        getPresenter().fundConversionInputQuery(params);
    }

    @Override
    protected FundConversionContract.Presenter initPresenter() {
        return new FundConversionInputPresenter(this);
    }

    @Override
    public void fundConversionInputQueryFail(BiiResultErrorException e) {
        closeProgressDialog();

    }

    @Override
    public void fundConversionInputQuerySuccess(FundConversionInputModel model) {
        closeProgressDialog();
        fragmentModel = model;
        if (fragmentModel == null || fragmentModel.getListBeans() == null
                || fragmentModel.getListBeans().size() <= 0) {
            return;
        }
        if (inputFundItems == null) {
            inputFundItems = new ArrayList<>();
        } else {
            inputFundItems.clear();
        }
        for (FundConversionInputModel.InputBean bean : model.getListBeans()) {
            String content = bean.getFundName() + " " + bean.getFundCode();
            inputFundItems.add(content);
        }


    }

    @Override
    public void queryFundInfoFail(BiiResultErrorException e) {
        closeProgressDialog();
        fundDatail.setVisibility(View.GONE);
    }

    @Override
    public void queryFundInfoSuccess(PsnGetFundDetailResult result) {
        closeProgressDialog();
        initFundInfo(result);

    }


    private void initFundInfo(PsnGetFundDetailResult result) {
        if (result == null) {
            fundDatail.setVisibility(View.GONE);
            return;
        }
        rightTop.setText(getString(R.string.boc_fund_sell_baseline, BUY_BASELINE));
        leftBottom.setText(result.getSevenDayYield() + "%");//七日年化收益率
        middBottom.setText(result.getFundIncomeUnit());// 万份收益
        String date = result.getEndDate();
        LocalDate localDate = LocalDate.parse(date, DateFormatters.dateFormatter1);
        date = localDate.format(DateFormatters.monthDateFormatter2);
        middDate.setText(date);
        String first = getString(R.string.boc_fund_conversion_apply);
        String middle = "0.24%";
        String third = result.getFundIncomeRatio() + "%";
        String rightValue = first + middle + third;
        SpannableStringBuilder style = new SpannableStringBuilder(rightValue);
        style.setSpan(new StrikethroughSpan(), first.length(), rightValue.length() - third.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.boc_text_color_red))
                , rightValue.length() - third.length(), rightValue.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        rightBottom.setText(style);
        rightBottom.setMovementMethod(LinkMovementMethod.getInstance());
        fundDatail.setVisibility(View.VISIBLE);
    }

    private void showChooseInputDialog() {

        if (inputFundDialog == null)
            inputFundDialog = new SelectStringListDialog(mContext);
        inputFundDialog.isSetLineMargin(false);
        inputFundDialog.setListData(inputFundItems, true);
        inputFundDialog.setHeaderTitleValue(getString(R.string.boc_fund_conversion_input_list_title));
        inputFundDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
            @Override
            public void onSelect(int position, String model) {
                FundConversionInputModel.InputBean bean = fragmentModel.getListBeans().get(position);
                String content = bean.getFundName() + "（" + bean.getFundCode() + "）";
                fromFund.setChoiceTextColor(mContext.getResources().getColor(R.color.boc_text_color_common_gray));
                fromFund.setChoiceTextContent(content);
                queryFundInfo(position);
                inputFundDialog.dismiss();
            }
        });

        if (!inputFundDialog.isShowing())
            inputFundDialog.show();

    }

    private void showFundSellFlagDialog() {
        if (fundSellFlagDialog == null) {
            fundSellFlagDialog = new SelectStringListDialog(mContext);
        }
        fundSellFlagDialog.isSetLineMargin(false);
        fundSellFlagDialog.setHeaderTitleValue(getString(R.string.boc_fund_sell_select_flag));
        fundSellFlagDialog.setListData(fundSellValue, true);
        fundSellFlagDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
            @Override
            public void onSelect(int position, String model) {
                initSellFlag(model);
                fundSellFlagDialog.dismiss();
            }
        });
        if (!fundSellFlagDialog.isShowing()){
            fundSellFlagDialog.show();
        }
    }

    private void initSellFlag(String model) {
        if (StringUtil.isNullOrEmpty(model)) {
            resultModel.setSellFlag("");
            return;
        }
        sellType.setChoiceTextContent(model);
        for (int i = 0; i < fundSellFlag.length; i++) {
            if (fundSellFlag[i].equals(model)) {
                resultModel.setSellFlag("" + i);
                return;
            }
        }
        resultModel.setSellFlag("");
    }

    private void queryFundInfo(int position) {
        if (position < 0 || position >= fragmentModel.getListBeans().size()) {
            fundDatail.setVisibility(View.GONE);
            return;
        }
        String fundCode = fragmentModel.getListBeans().get(position).getFundCode();
        String fundName = fragmentModel.getListBeans().get(position).getFundName();
        if (resultModel != null){
            resultModel.setToFundCode(fundCode);
            resultModel.setToFundName(fundName);
        }
        if (StringUtil.isNullOrEmpty(fundCode)) {
            fundDatail.setVisibility(View.GONE);
            return;
        }
        showLoadingDialog();
        PsnGetFundDetailParams params = new PsnGetFundDetailParams();
        params.setFundCode(fundCode);
        getPresenter().queryFundInfo(params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (inputFundItems != null){
            inputFundItems.clear();
            inputFundItems = null;
        }
        fundSellFlag = null;
    }

    @Override
    public void setPresenter(FundConversionContract.Presenter presenter) {

    }
}
