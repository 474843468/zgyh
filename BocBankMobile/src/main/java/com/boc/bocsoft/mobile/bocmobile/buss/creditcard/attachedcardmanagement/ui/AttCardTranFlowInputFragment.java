package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAppertainAndMess.PsnCrcdQueryAppertainAndMessResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardTradeFlowModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter.AttCardTradeDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter.AttCardTradeFlowContract;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter.AttCardTradeFlowPresenter;

/**
 * Name: liukai
 * Time：2016/12/4 10:08.
 * Created by lk7066 on 2016/12/4.
 * It's used to 附属卡交易流量输入页面
 */

public class AttCardTranFlowInputFragment extends MvpBussFragment<AttCardTradeFlowContract.AttCardTradeFlowPresenter> implements AttCardTradeFlowContract.AttCardTradeFlowView {

    private View mRootView = null;
    private EditMoneyInputWidget edtTranFlow;
    private Button btnNext;
    private AttCardTradeFlowModel tradeFlowModel;
    private AttCardTradeFlowContract.AttCardTradeFlowPresenter mPresenter;

    @Override
    public void beforeInitView() {
        //super.beforeInitView();
        tradeFlowModel = getArguments().getParcelable("Choose");
        updateTitleValue("修改" + PublicCodeUtils.getCurrency(mContext, tradeFlowModel.getCurrency()) + "流量");
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater){
        mRootView = mInflater.inflate(R.layout.fragment_attcard_tranflow_input, null);
        return mRootView;
    }

    @Override
    public void initView(){
        edtTranFlow = (EditMoneyInputWidget) mRootView.findViewById(R.id.attcard_tranflow_input);
        btnNext = (Button) mRootView.findViewById(R.id.btn_attcard_next);
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //首先判断金额输入框的值是否为空，如果不为空，判断是否输入为0
                if(com.boc.bocsoft.mobile.common.utils.StringUtils.isEmpty(edtTranFlow.getContentMoney())){
                    showErrorDialog(getResources().getString(R.string.boc_crcd_attcard_tranflow_error_info1));
                } else if(edtTranFlow.getContentMoney().equals("0.00")){
                    showErrorDialog(getResources().getString(R.string.boc_crcd_attcard_tranflow_error_info2));
                } else {
                    showLoadingDialog();
                    getPresenter().querySecurityFactor();
                }

            }

        });

    }

    @Override
    public void initData() {
        mPresenter = new AttCardTradeFlowPresenter(this);

        //如果前一个页面传值，那么显示默认的值，如果没有，显示hint值“请输入”
        if(tradeFlowModel.getAmount() != null){
            edtTranFlow.setmContentMoneyEditText(tradeFlowModel.getAmount());
        } else {
            edtTranFlow.setContentHint(getResources().getString(R.string.boc_crcd_attcard_tranflow_edithint));
        }

        edtTranFlow.setEditWidgetTitle(getResources().getString(R.string.boc_crcd_attcard_tranflow_edittitle));

        //输入长度12.2
        edtTranFlow.setMaxLeftNumber(12);
        edtTranFlow.setMaxRightNumber(2);

    }

    @Override
    protected String getTitleValue() {
        return "";
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
    public void onDestroy() {
        mPresenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void appertainAndMessSuccess(PsnCrcdQueryAppertainAndMessResult psnCrcdQueryAppertainAndMessResult, int flag) {

    }

    @Override
    public void appertainAndMessFailed(BiiResultErrorException exception, int flag) {

    }

    @Override
    public void querySecurityFactorSuccess(SecurityFactorModel securityFactorModel) {

        //安全因子值传入model，调确认接口
        CombinListBean combinBean = SecurityVerity.getInstance(getActivity()).getDefaultSecurityFactorId(securityFactorModel);

        tradeFlowModel.set_combinId(combinBean.getId());
        tradeFlowModel.setCombinName(combinBean.getName());
        tradeFlowModel.setAmount(edtTranFlow.getContentMoney());
        getPresenter().setAppertainTranConfirm(tradeFlowModel);

    }

    @Override
    public void querySecurityFactorFailed(BiiResultErrorException exception) {

    }

    @Override
    public void setAppertainTranConfirmSuccess(VerifyBean verifyBean) {
        closeProgressDialog();
        tradeFlowModel.setConversationId(AttCardTradeDetailPresenter.attCardConversationId);
        start(AttCardTranFlowConfirmFragment.newInstance(tradeFlowModel, verifyBean));
    }

    @Override
    public void setAppertainTranConfirmFailed(BiiResultErrorException exception) {

    }

    @Override
    public void appertainAndMessSecondSuccess(PsnCrcdQueryAppertainAndMessResult psnCrcdQueryAppertainAndMessResult, int flag) {

    }

    @Override
    public void appertainAndMessSecondFailed(BiiResultErrorException exception, int flag) {

    }

    @Override
    public void crcdCurrencyQuerySuccess(PsnCrcdCurrencyQueryResult mCurrencyQuery) {

    }

    @Override
    public void crcdCurrencyQueryFailed(BiiResultErrorException exception) {

    }

    @Override
    public void setPresenter(AttCardTradeFlowContract.AttCardTradeFlowPresenter presenter) {

    }

    @Override
    protected AttCardTradeFlowContract.AttCardTradeFlowPresenter initPresenter() {
        return new AttCardTradeFlowPresenter(this);
    }

}
