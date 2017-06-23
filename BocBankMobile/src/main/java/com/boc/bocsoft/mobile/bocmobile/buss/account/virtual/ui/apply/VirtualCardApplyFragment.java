package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.apply;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.DateUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter.VirCardPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter.VirtualCardContract;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         16/8/30 17:39
 *         申请虚拟卡
 */
@SuppressLint("ValidFragment")
public class VirtualCardApplyFragment extends BaseAccountFragment<VirCardPresenter> implements VirtualCardContract.VirCardApplyView, MClickableSpan.OnClickSpanListener, View.OnClickListener {

    //绑定账户,起始日期,截止日期
    private EditChoiceWidget etAccount, etStartDate, etEndDate;
    //单笔限额,累计限额
    private EditMoneyInputWidget etSingleLimit, etTotalLimit;
    //勾选的checkBox
    private CheckBox cbAgreement;
    //服务须知
    private SpannableString tvAgreement;
    //下一步按钮
    private Button btnNext;

    private AccountBean creditAccountBean;

    private VirtualCardModel model;

    public VirtualCardApplyFragment(AccountBean creditAccountBean) {
        this.creditAccountBean = creditAccountBean;
    }

    @Override
    protected VirCardPresenter initPresenter() {
        return new VirCardPresenter(this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_virtual_account_apply_info_title);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_virtual_apply, null);
    }

    @Override
    public void initView() {
        etAccount = (EditChoiceWidget) mContentView.findViewById(R.id.et_account);
        etAccount.setArrowImageGone(false);
        etStartDate = (EditChoiceWidget) mContentView.findViewById(R.id.et_start_date);
        etEndDate = (EditChoiceWidget) mContentView.findViewById(R.id.et_end_date);
        etSingleLimit = (EditMoneyInputWidget) mContentView.findViewById(R.id.et_single_limit);
        etTotalLimit = (EditMoneyInputWidget) mContentView.findViewById(R.id.et_total_limit);
        etSingleLimit.setScrollView(mContentView);
        etTotalLimit.setScrollView(mContentView);

        btnNext = (Button) mContentView.findViewById(R.id.btn_next);

        if (creditAccountBean.getCurrencyCode().equals(ApplicationConst.CURRENCY_JPY)) {
            etSingleLimit.setMaxLeftNumber(13);
            etSingleLimit.setMaxRightNumber(0);

            etTotalLimit.setMaxLeftNumber(13);
            etTotalLimit.setMaxRightNumber(0);
        }
        etSingleLimit.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_color_red));
        etTotalLimit.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_color_red));
        tvAgreement = (SpannableString) mContentView.findViewById(R.id.tv_agreement);
        cbAgreement = (CheckBox) mContentView.findViewById(R.id.cb_agreement);
    }

    @Override
    public void setListener() {
        etStartDate.setOnClickListener(this);
        etEndDate.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void initData() {
        String startTitle = getString(R.string.boc_virtual_account_apply_info_service_protocol_start);
        String content = getString(R.string.boc_virtual_account_apply_info_service_protocol);
        String endTitle = getString(R.string.boc_virtual_account_apply_info_service_protocol_end);
        tvAgreement.setContent(startTitle, endTitle, content,false, R.color.boc_text_mobile_color, R.color.boc_text_color_red, this);

        showLoadingDialog();
        getPresenter().psnCrcdVirtualCardApplyInit(creditAccountBean);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            if (!checkCommit())
                return;

            start(new VirtualApplyConfirmFragment(model));
        } else if (v.getId() == R.id.et_start_date) {
            choiceDate(etStartDate, model.getStartDate());
        } else if (v.getId() == R.id.et_end_date) {
            choiceDate(etEndDate, model.getEndDate());
        }
    }

    /**
     * 校验输入项
     *
     * @return
     */
    private boolean checkCommit() {
        //校验截止日期须大于起始日期
        if (model.getEndDate().compareTo(model.getStartDate()) < 1) {
            showErrorDialog(getString(R.string.boc_virtual_account_apply_info_date_error));
            return false;
        }

        //校验是否输入单笔限额
        String singleLimitString = etSingleLimit.getContentMoney();
        if (StringUtils.isEmptyOrNull(singleLimitString)) {
            showErrorDialog(getString(R.string.boc_virtual_account_apply_info_limit_single_none_error));
            return false;
        }

        //校验是否输入累计限额
        String totalLimitString = etTotalLimit.getContentMoney();
        if (StringUtils.isEmptyOrNull(totalLimitString)) {
            showErrorDialog(getString(R.string.boc_virtual_account_apply_info_limit_total_none_error));
            return false;
        }

        BigDecimal singleLimit = BigDecimal.valueOf(Double.parseDouble(singleLimitString));
        BigDecimal totalLimit = BigDecimal.valueOf(Double.parseDouble(totalLimitString));

        //单笔交易限额不能为0
        if (singleLimit.doubleValue() <= 0) {
            showErrorDialog(getString(R.string.boc_virtual_account_apply_info_limit_single_zero_error));
            return false;
        }
        //单笔限额不能大于最大限额
        if (model.getMaxSingleLimit().compareTo(singleLimit) == -1) {
            showErrorDialog(getString(R.string.boc_virtual_account_apply_info_limit_single_max_error, model.getMaxSingleLimit().toString()));
            return false;
        }
        //累计交易限额不能为0
        if (totalLimit.doubleValue() <= 0) {
            showErrorDialog(getString(R.string.boc_virtual_account_apply_info_limit_total_zero_error));
            return false;
        }
        //累计限额不能大于最大限额
        if (model.getMaxSingleLimit().compareTo(totalLimit) == -1) {
            showErrorDialog(getString(R.string.boc_virtual_account_apply_info_limit_total_max_error, model.getMaxSingleLimit().toString()));
            return false;
        }
        //单笔限额不能大于累计限额
        if (totalLimit.compareTo(singleLimit) == -1) {
            showErrorDialog(getString(R.string.boc_virtual_account_apply_info_limit_single_max_error1));
            return false;
        }
        //检验是否勾选了须知
        if (!cbAgreement.isChecked()) {
            showErrorDialog(getString(R.string.boc_account_service_bureau_hint));
            return false;
        }

        model.setSignleLimit(singleLimit);
        model.setTotalLimit(totalLimit);
        return true;
    }

    /**
     * 起始日期,截止日期
     *
     * @param etDate
     */
    private void choiceDate(final EditChoiceWidget etDate, LocalDate date) {
        long minDate = DateUtil.parse(model.getStartDate().format(DateFormatters.dateFormatter1));
        long maxDate = DateUtil.parse(model.getEndDate().format(DateFormatters.dateFormatter1));
        DateTimePicker.showRangeDatePick(mContext, date, minDate, maxDate, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
            @Override
            public void onChoiceDateSet(String strChoiceTime, LocalDate localDate) {
                if (etDate == etStartDate)
                    model.setStartDate(localDate);
                else
                    model.setEndDate(localDate);

                etDate.setChoiceTextContent(strChoiceTime);
            }
        });
    }

    @Override
    public void onClickSpan() {
        start(new ApplyVirtualCardFragment(null, true));
    }

    @Override
    public void initApplyVirtual(VirtualCardModel model) {
        closeProgressDialog();
        if (model == null)
            return;

        this.model = model;
        etAccount.setChoiceTextContent(NumberUtils.formatCardNumber(model.getAccountNumber()));
        etStartDate.setChoiceTextContent(model.getStartDate().format(DateFormatters.dateFormatter1));
        etEndDate.setChoiceTextContent(model.getEndDate().format(DateFormatters.dateFormatter1));

        etSingleLimit.setmContentMoneyEditText("");
        etTotalLimit.setmContentMoneyEditText("");
        etSingleLimit.setContentHint(getString(R.string.boc_virtual_account_apply_info_limit) + PublicCodeUtils.getCurrency(getContext(), creditAccountBean.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getMaxSingleLimit(), creditAccountBean.getCurrencyCode()));
        etTotalLimit.setContentHint(getString(R.string.boc_edit_input_hint));
    }
}
