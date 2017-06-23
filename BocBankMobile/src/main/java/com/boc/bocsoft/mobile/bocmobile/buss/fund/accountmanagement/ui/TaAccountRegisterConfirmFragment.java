package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailRow;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.FundRegCompanyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountRegisterModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.presenter.TaAccountRegisterConfirmPresenter;

/**
 * 中银基金-TA账户登记确认页
 * 继承BaseConfirmFragment
 * Created by Liyf 20161205
 */

public class TaAccountRegisterConfirmFragment extends MvpBussFragment<TaAccountRegisterConfirmContract.Presenter>
        implements TaAccountRegisterConfirmContract.View {

    private View rootView;
    private DetailRow fincAccount;    // 基金交易账户item
    private DetailRow taAccount;     // 基金TA账户item
    private DetailRow regOrgCode;     // 注册登记机构名称item
    private Button btn_confirm;    // Button登记确认

    private String transAccount;    // 交易账户
    private String taAccountNumber;    // TA账户
    private FundRegCompanyModel.RegCompanyBean mSelectCompanyBean = null;    // 选择注册登记机构viewModel

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_ta_account_register_confirm, null);
        return rootView;
    }

    @Override
    public void initView() {
        fincAccount = (DetailRow) rootView.findViewById(R.id.fincAccount);
        taAccount = (DetailRow) rootView.findViewById(R.id.taAccount);
        regOrgCode = (DetailRow) rootView.findViewById(R.id.regOrgCode);
        btn_confirm = (Button) rootView.findViewById(R.id.btn_confirm);
    }

    @Override
    public void initData() {
        transAccount = getArguments().getString("trans_account");
        taAccountNumber = getArguments().getString("ta_account");
        mSelectCompanyBean = (FundRegCompanyModel.RegCompanyBean)getArguments().getSerializable("select_company");

        fincAccount.updateData(getResources().getString(R.string.boc_fund_trans_account), transAccount);
        fincAccount.showDividerLine(View.GONE);
        taAccount.updateData(getResources().getString(R.string.boc_fund_ta_account), taAccountNumber);
        taAccount.showDividerLine(View.GONE);
        regOrgCode.updateData(getResources().getString(R.string.boc_fund_reg_name), mSelectCompanyBean.getFundRegName());
        regOrgCode.showDividerLine(View.GONE);
    }

    @Override
    public void setListener() {
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
                TaAccountRegisterModel model = new TaAccountRegisterModel();
                model.setRegOrgCode(mSelectCompanyBean.getFundRegCode());
                model.setTaAccount(taAccountNumber);
                getPresenter().registerFundTaAccount(model);
            }
        });
    }

    @Override
    protected TaAccountRegisterConfirmContract.Presenter initPresenter() {
        return new TaAccountRegisterConfirmPresenter(this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fragment_confirm_title);
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
    public void registerFundTaAccountSuccess() {
        closeProgressDialog();
        Toast.makeText(getContext(), "登记成功", Toast.LENGTH_LONG).show();
        popToAndReInit(AccountManagementFragment.class);
    }

    @Override
    public void registerFundTaAccountFail(BiiResultErrorException biiResultErrorExceptio) {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(TaAccountRegisterConfirmContract.Presenter presenter) {

    }

}


