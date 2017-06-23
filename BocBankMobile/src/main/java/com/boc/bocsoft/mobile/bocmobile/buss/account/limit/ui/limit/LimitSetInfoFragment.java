package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui.limit;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.QuotaModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.BalanceHint.BalanceHintView;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         2016/10/17 11:11
 *         限额修改输入界面
 */
@SuppressLint("ValidFragment")
public class LimitSetInfoFragment extends BaseAccountFragment implements View.OnClickListener {

    private QuotaModel quotaModel;

    private EditMoneyInputWidget etAtm,etPos,etAtmCash,etBorderPos,etBorderAtm;

    private BalanceHintView hvAtm,hvPos,hvAtmCash,hvBorderPos,hvBorderAtm;

    private Button btnNext;

    public LimitSetInfoFragment(QuotaModel quotaModel) {
        this.quotaModel = quotaModel;
    }

    /**
     * 初始化布局
     *
     * @param mInflater
     * @return
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_account_limit_update_info, null);
    }

    /**
     * 修改标题文字
     *
     * @return
     */
    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_limit_update_info);
    }

    @Override
    public void initView() {
        etAtm = (EditMoneyInputWidget) mContentView.findViewById(R.id.et_atm);
        etPos = (EditMoneyInputWidget) mContentView.findViewById(R.id.et_pos);
        etAtmCash = (EditMoneyInputWidget) mContentView.findViewById(R.id.et_atm_cash);
        etBorderPos = (EditMoneyInputWidget) mContentView.findViewById(R.id.et_border_pos);
        etBorderAtm = (EditMoneyInputWidget) mContentView.findViewById(R.id.et_border_atm);

        hvAtm = (BalanceHintView) mContentView.findViewById(R.id.hv_atm);
        hvPos = (BalanceHintView) mContentView.findViewById(R.id.hv_pos);
        hvAtmCash = (BalanceHintView) mContentView.findViewById(R.id.hv_atm_cash);
        hvBorderAtm = (BalanceHintView) mContentView.findViewById(R.id.hv_border_atm);
        hvBorderPos = (BalanceHintView) mContentView.findViewById(R.id.hv_border_pos);

        btnNext = (Button) mContentView.findViewById(R.id.btn_ok);

        etAtm.setScrollView(mContentView);
        etPos.setScrollView(mContentView);
        etAtmCash.setScrollView(mContentView);
        etBorderPos.setScrollView(mContentView);
        etBorderAtm.setScrollView(mContentView);
    }

    @Override
    public void setListener() {
        btnNext.setOnClickListener(this);
    }

    @Override
    public void initData() {
        String hintContent = getString(R.string.boc_account_limit_update_hint) + " " + PublicCodeUtils.getCurrency(mContext, quotaModel.getCurrency());

        if(quotaModel.isUpdateAtm()){
            etAtm.setVisibility(View.VISIBLE);
            hvAtm.setVisibility(View.VISIBLE);
        }

        if(quotaModel.isUpdatePos()){
            etPos.setVisibility(View.VISIBLE);
            hvPos.setVisibility(View.VISIBLE);
        }

        if(quotaModel.isUpdateAtmCash()){
            etAtmCash.setVisibility(View.VISIBLE);
            hvAtmCash.setVisibility(View.VISIBLE);
        }

        if(quotaModel.isUpdateBorderPos()){
            etBorderPos.setVisibility(View.VISIBLE);
            hvBorderPos.setVisibility(View.VISIBLE);
        }

        if(quotaModel.isUpdateBorderAtm()){
            etBorderAtm.setVisibility(View.VISIBLE);
            hvBorderAtm.setVisibility(View.VISIBLE);
        }

        etAtm.setmContentMoneyEditText(quotaModel.getTransDay().toString());
        hvAtm.setData(hintContent, quotaModel.getCurrency(),QuotaModel.LIMIT_MAX,0,R.color.boc_text_color_cinerous);
        etPos.setmContentMoneyEditText(quotaModel.getAllDayPOS().toString());
        hvPos.setData(hintContent, quotaModel.getCurrency(),QuotaModel.LIMIT_MAX,0,R.color.boc_text_color_cinerous);
        etAtmCash.setmContentMoneyEditText(quotaModel.getCashDayATM().toString());
        hvAtmCash.setData(hintContent, quotaModel.getCurrency(),QuotaModel.LIMIT_MAX,0,R.color.boc_text_color_cinerous);
        etBorderPos.setmContentMoneyEditText(quotaModel.getConsumeForeignPOS().toString());
        hvBorderPos.setData(hintContent, quotaModel.getCurrency(),QuotaModel.LIMIT_MAX,0,R.color.boc_text_color_cinerous);
        etBorderAtm.setmContentMoneyEditText(quotaModel.getCashDayForeignATM().toString());
        hvBorderAtm.setData(hintContent, quotaModel.getCurrency(),QuotaModel.LIMIT_MAX,0,R.color.boc_text_color_cinerous);
    }

    @Override
    public void onClick(View v) {
        quotaModel.setTransDay(new BigDecimal(Double.parseDouble(etAtm.getContentMoney())));
        quotaModel.setAllDayPOS(new BigDecimal(Double.parseDouble(etPos.getContentMoney())));
        quotaModel.setCashDayATM(new BigDecimal(Double.parseDouble(etAtmCash.getContentMoney())));
        quotaModel.setConsumeForeignPOS(new BigDecimal(Double.parseDouble(etBorderPos.getContentMoney())));
        quotaModel.setCashDayForeignATM(new BigDecimal(Double.parseDouble(etBorderAtm.getContentMoney())));

        start(new LimitSetConfirmFragment(quotaModel));
    }
}
