package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.limit;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.BalanceHint.BalanceHintView;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         16/9/4 00:56
 *         修改交易限额
 */
@SuppressLint("ValidFragment")
public class VirtualUpdateLimitFragment extends BaseAccountFragment implements View.OnClickListener {

    private EditMoneyInputWidget etSingle, etTotal;

    private BalanceHintView hvSingle, hvTotal;

    private Button btnNext;

    /**
     * 虚拟卡信息
     */
    private VirtualCardModel model;

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_virtual_account_detail_update_title);
    }

    @Override
    public void initView() {
        etSingle = (EditMoneyInputWidget) mContentView.findViewById(R.id.et_single);
        etTotal = (EditMoneyInputWidget) mContentView.findViewById(R.id.et_total);
        btnNext = (Button) mContentView.findViewById(R.id.btn_next);

        etSingle.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_color_red));
        etTotal.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_color_red));

        hvSingle = (BalanceHintView) mContentView.findViewById(R.id.hv_single);
        hvTotal = (BalanceHintView) mContentView.findViewById(R.id.hv_total);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_virtual_limit_update, null);
    }

    @Override
    public void initData() {
        model = (VirtualCardModel) getArguments().getSerializable(KEY_BEAN);

        if(ApplicationConst.CURRENCY_JPY.equals(model.getCurrencyCode())){
            etSingle.setMaxLeftNumber(13);
            etSingle.setMaxRightNumber(0);

            etTotal.setMaxLeftNumber(13);
            etTotal.setMaxRightNumber(0);
        }

        String singleContent = getString(R.string.boc_virtual_account_detail_update_account_single_hint);
        hvSingle.setDataColor(R.color.boc_text_color_cinerous,R.color.boc_text_color_cinerous);
        hvSingle.setData(singleContent , model.getMaxSingleLimit(),model.getCurrencyCode());

        String totalContent = getString(R.string.boc_virtual_account_detail_update_account_total_hint);
        hvTotal.setDataColor(R.color.boc_text_color_cinerous,R.color.boc_text_color_cinerous);
        hvTotal.setData(totalContent , model.getAtotalLimit(),model.getCurrencyCode());
    }

    @Override
    public void setListener() {
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!checkCommit())
            return;
        start(new VirtualUpdateLimitConfirmFragment(model));
    }

    private boolean checkCommit() {
        //校验是否输入单笔限额
        String singleLimitString = etSingle.getContentMoney();
        if (StringUtils.isEmptyOrNull(singleLimitString)) {
            showErrorDialog(getString(R.string.boc_virtual_account_apply_info_limit_single_none_error));
            return false;
        }

        //校验是否输入累计限额
        String totalLimitString = etTotal.getContentMoney();
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
        //累计限额须大于已累计最大限额
        if (model.getAtotalLimit().compareTo(totalLimit) == 1) {
            showErrorDialog(getString(R.string.boc_virtual_account_apply_info_limit_total_min_error, model.getAtotalLimit().toString()));
            return false;
        }
        //单笔限额不能大于累计限额
        if (totalLimit.compareTo(singleLimit) == -1) {
            showErrorDialog(getString(R.string.boc_virtual_account_apply_info_limit_single_max_error1));
            return false;
        }

        model.setSignleLimit(singleLimit);
        model.setTotalLimit(totalLimit);
        return true;
    }
}
