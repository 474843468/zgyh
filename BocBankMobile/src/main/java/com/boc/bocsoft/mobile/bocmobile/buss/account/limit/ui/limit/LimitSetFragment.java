package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui.limit;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountCheckBox.AccountCheckBox;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.QuotaModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter.LimitContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter.LimitPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * @author wangyang
 *         2016/10/10 19:26
 *         限额设置界面
 */
@SuppressLint("ValidFragment")
public class LimitSetFragment extends BaseAccountFragment<LimitPresenter> implements LimitContract.QuotaView, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private QuotaModel quotaModel;

    /**
     * 当前账户
     */
    private TextView tvAccount;

    /**
     * 限额控件
     */
    private AccountCheckBox acbAtm, acbPos, acbAtmCash, acbBorderPos, acbBorderAtm;

    /**
     * 下一步按钮
     */
    private Button btnNext;

    public LimitSetFragment(QuotaModel quotaModel) {
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
        return mInflater.inflate(R.layout.boc_fragment_account_limit_update, null);
    }

    /**
     * 修改标题文字
     *
     * @return
     */
    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_limit_update);
    }

    @Override
    protected LimitPresenter initPresenter() {
        return new LimitPresenter(this);
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView() {
        tvAccount = (TextView) mContentView.findViewById(R.id.tv_account);
        acbAtm = (AccountCheckBox) mContentView.findViewById(R.id.acb_atm);
        acbPos = (AccountCheckBox) mContentView.findViewById(R.id.acb_pos);
        acbAtmCash = (AccountCheckBox) mContentView.findViewById(R.id.acb_atm_cash);
        acbBorderPos = (AccountCheckBox) mContentView.findViewById(R.id.acb_border_pos);
        acbBorderAtm = (AccountCheckBox) mContentView.findViewById(R.id.acb_border_atm);
        btnNext = (Button) mContentView.findViewById(R.id.btn_next);
    }

    @Override
    public void setListener() {
        acbAtm.setOnCheckedChangeListener(this);
        acbPos.setOnCheckedChangeListener(this);
        acbAtmCash.setOnCheckedChangeListener(this);
        acbBorderPos.setOnCheckedChangeListener(this);
        acbBorderAtm.setOnCheckedChangeListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tvAccount.setText(getString(R.string.boc_account_limit_update_account, NumberUtils.formatCardNumber(quotaModel.getAccountNumber())));

        showLoadingDialog();
        getPresenter().queryAllQuota(quotaModel);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isHadChecked())
            btnNext.setVisibility(View.VISIBLE);
        else
            btnNext.setVisibility(View.GONE);
    }

    private boolean isHadChecked() {
        return acbAtm.isChecked() || acbPos.isChecked() || acbAtmCash.isChecked() || acbBorderPos.isChecked() || acbBorderAtm.isChecked();
    }

    @Override
    public void onClick(View v) {
        quotaModel.setUpdateAtm(acbAtm.isChecked());
        quotaModel.setUpdatePos(acbPos.isChecked());
        quotaModel.setUpdateAtmCash(acbAtmCash.isChecked());
        quotaModel.setUpdateBorderPos(acbBorderPos.isChecked());
        quotaModel.setUpdateBorderAtm(acbBorderAtm.isChecked());
        start(new LimitSetInfoFragment(quotaModel));
    }

    @Override
    public void queryAllQuota(QuotaModel quotaModel) {
        closeProgressDialog();

        if (quotaModel == null)
            return;

        this.quotaModel = quotaModel;
        String currency = PublicCodeUtils.getCurrency(mContext, quotaModel.getCurrency());

        acbAtm.checked(false);
        acbPos.checked(false);
        acbAtmCash.checked(false);
        acbBorderPos.checked(false);
        acbBorderAtm.checked(false);

        acbAtm.setData(getString(R.string.boc_account_limit_update_atm), currency, MoneyUtils.transMoneyFormat(quotaModel.getTransDay(), quotaModel.getCurrency()));
        acbPos.setData(getString(R.string.boc_account_limit_update_pos), currency, MoneyUtils.transMoneyFormat(quotaModel.getAllDayPOS(), quotaModel.getCurrency()));
        acbAtmCash.setData(getString(R.string.boc_account_limit_update_atm_cash), currency, MoneyUtils.transMoneyFormat(quotaModel.getCashDayATM(), quotaModel.getCurrency()));
        acbBorderPos.setData(getString(R.string.boc_account_limit_update_border_pos), currency, MoneyUtils.transMoneyFormat(quotaModel.getConsumeForeignPOS(), quotaModel.getCurrency()));
        acbBorderAtm.setData(getString(R.string.boc_account_limit_update_border_atm), currency, MoneyUtils.transMoneyFormat(quotaModel.getCashDayForeignATM(), quotaModel.getCurrency()));
    }
}
