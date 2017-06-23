package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model.MedicalModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.presenter.MedicalInsurancePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.presenter.OverviewContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.ui.TransDetailFragment;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import java.util.List;

/**
 * @author wangyang
 *         16/8/18 20:12
 *         医保账户
 */
@SuppressLint("ValidFragment")
public class MedicalInsuranceFragment extends BaseOverviewFragment<MedicalInsurancePresenter> implements View.OnClickListener, OverviewContract.MedicalView {

    private TransactionView tranDetail;
    private DetailContentView dcvDetail;
    private TextView tvDetail;
    private Button btnMore;

    private AccountBean accountBean;


    public MedicalInsuranceFragment(AccountBean accountBean) {
        this.accountBean = accountBean;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_overview_medical_title);
    }

    @Override
    protected MedicalInsurancePresenter initPresenter() {
        return new MedicalInsurancePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.fragment_account_medical_insurance, null);
    }

    @Override
    public void initView() {
        /**添加账户信息*/
        dcvDetail = (DetailContentView) mContentView.findViewById(R.id.dcv_detail);
        tvDetail = (TextView) mContentView.findViewById(R.id.tv_detail);

        /**交易明细*/
        tranDetail = (TransactionView) mContentView.findViewById(R.id.tran_detail);

        /**底部更多按钮*/
        btnMore = (Button) mContentView.findViewById(R.id.btn_more);
    }

    @Override
    public void initData() {
        tranDetail.setAdapter();

        showLoadingDialog();
        getPresenter().queryMedicalDetail(accountBean.getAccountId());
    }

    @Override
    public void setListener() {
        btnMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        goTransDetailFragment(accountBean, TransDetailFragment.DETAIL_ACCOUNT_TYPE_MEDICAL);
    }

    @Override
    public void queryMedicalDetail(MedicalModel medicalModel) {
        if (medicalModel == null){
            closeProgressDialog();
            return;
        }

        //显示账号详情,设置相关内容
        dcvDetail.setVisibility(View.VISIBLE);
        MedicalModel.MedicalDetail detail = medicalModel.getMedicalDetailList().get(0);
        dcvDetail.addDetailRow(getString(R.string.boc_overview_detail_balance), MoneyUtils.transMoneyFormat(detail.getBookBalance(), detail.getCurrencyCode()));
        dcvDetail.addDetailRow(getString(R.string.boc_account_detail_open_bank), medicalModel.getAccOpenBank());
        dcvDetail.addDetailRow(getString(R.string.boc_account_detail_open_date), medicalModel.getAccOpenDate().format(
                DateFormatters.dateFormatter1));
        dcvDetail.addDetailRowNotLine(getString(R.string.boc_account_detail_status), PublicCodeUtils.getFacilityChildStatusCode(mContext, accountBean.getAccountStatus()));

        //请求交易明细
        getPresenter().queryMedicalTransferDetail(medicalModel);
    }

    @Override
    public void queryMedicalTransferDetail(List<TransactionBean> transactionList) {
        closeProgressDialog();
        if(transactionList==null){
            tvDetail.setText(getString(R.string.boc_trans_detail_title_no_data));
            return;
        }

        //设置交易明细
        tvDetail.setText(getString(R.string.boc_trans_detail_title_data));
        tranDetail.setData(transactionList);
    }
}
