package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayBasePresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 二维码支付 - 默认支付账户
 * Created by wangf on 2016/8/23.
 */
public class QRPayDefaultAccountFragment extends BussFragment implements QRPayBaseContract.QrAccountBaseView ,QRPayBaseContract.QrSetCardBaseView{

    private View mRootView;

    private EditChoiceWidget editChoiceWidget;
    private LinearLayout llAccountAmount;
    private TextView tvAccountTitle;
    private TextView tvAccountAmount;
    private Button btnAccountConfirm;


    // 选择的AccountBean
    private AccountBean selectPayAccount;
    private boolean isSelectAccount;
    // 选择账户页面选中的账户
    private Bundle accountBundle;
    //可用余额
    private BigDecimal mAvailableBalance;

    private SelectAccoutFragment selectAccoutFragment;

    private QRPayBasePresenter mPayBasePresenter;
    private QRPayBasePresenter mSetCardBasePresenter;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_qrpay_default_account, null);
        return mRootView;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_qrpay_default_pay_account);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void initView() {
        editChoiceWidget = (EditChoiceWidget) mRootView.findViewById(R.id.qrcode_pay_more_pwd);
        llAccountAmount = (LinearLayout) mRootView.findViewById(R.id.ll_qrpay_default_account_amount);
        tvAccountTitle = (TextView) mRootView.findViewById(R.id.tv_qrpay_default_account_title);
        tvAccountAmount = (TextView) mRootView.findViewById(R.id.tv_qrpay_default_account_amount);
        btnAccountConfirm = (Button) mRootView.findViewById(R.id.qrcode_pay_btn_default_account_confirm);
    }

    @Override
    public void initData() {
        mPayBasePresenter = new QRPayBasePresenter((QRPayBaseContract.QrAccountBaseView)this);
        mSetCardBasePresenter = new QRPayBasePresenter((QRPayBaseContract.QrSetCardBaseView)this);

        //默认账户
        if (QRPayMainFragment.isSetDefaultCard){
            llAccountAmount.setVisibility(View.GONE);
            editChoiceWidget.setChoiceTextContent(NumberUtils.formatCardNumberStrong(QRPayMainFragment.mDefaultAccountBean.getAccountNumber()));
            String accountType = QRPayMainFragment.mDefaultAccountBean.getAccountType();
            if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
                    ApplicationConst.ACC_TYPE_GRE.equals(accountType)){
                //信用卡账户详情 - 不需查询
//                showLoadingDialog();
//                isSelectAccount = false;
//                mPayBasePresenter.queryCreditAccountDetail(QRPayMainFragment.mDefaultAccountBean.getAccountId(), ApplicationConst.CURRENCY_CNY);
            }else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)){
                //借记卡账户详情
                showLoadingDialog();
                isSelectAccount = false;
                mPayBasePresenter.queryAccountDetails(QRPayMainFragment.mDefaultAccountBean.getAccountId());
            }
        }else{
            llAccountAmount.setVisibility(View.GONE);
            editChoiceWidget.setChoiceTextContent("请选择");
        }
    }

    @Override
    public void setListener() {
        //账户选择
        editChoiceWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> accountTypeList = new ArrayList<String>();
                accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);//借记卡
                accountTypeList.add(ApplicationConst.ACC_TYPE_ZHONGYIN);//中银系列信用卡
                accountTypeList.add(ApplicationConst.ACC_TYPE_GRE);//长城信用卡
                List<AccountBean> accountBeansList = QRPayMainFragment.getRelativeBankAccountList(accountTypeList);
                selectAccoutFragment = new SelectAccoutFragment().newInstanceWithData((ArrayList) accountBeansList);
//                selectAccoutFragment = new SelectAccoutFragment().newInstance(accountTypeList);
                selectAccoutFragment.isRequestNet(true);
                startForResult(selectAccoutFragment, SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
                selectAccoutFragment.setOnItemListener(new SelectAccoutFragment.ItemListener() {
                    @Override
                    public void onItemClick(Bundle bundle) {
                        accountBundle = bundle;
                        AccountBean accountBean = bundle.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
                        String accountType = accountBean.getAccountType();
                        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
                                ApplicationConst.ACC_TYPE_GRE.equals(accountType)) {
                            //信用卡不需要查询余额信息，直接获取账户信息后返回
                            mAvailableBalance = null;
                            llAccountAmount.setVisibility(View.GONE);
                            selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
                            selectAccoutFragment.pop();
//                            //信用卡账户详情
//                            showLoadingDialog();
//                            mPayBasePresenter.queryCreditAccountDetail(accountBean.getAccountId(), ApplicationConst.CURRENCY_CNY);
                        } else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)) {
                            //借记卡账户详情
                            showLoadingDialog();
                            mAvailableBalance = null;
                            isSelectAccount = true;
                            mPayBasePresenter.queryAccountDetails(accountBean.getAccountId());
                        }
                    }
                });
            }
        });
        //确认按钮
        btnAccountConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            	if (selectPayAccount != null && !StringUtils.isEmpty(selectPayAccount.getAccountId())){
//                    showLoadingDialog();
//                    mSetCardBasePresenter.loadQRPaySetDefaultCard(selectPayAccount.getAccountId());
//                }else{
//                	if(!QRPayMainFragment.isSetDefaultCard){
//                		showErrorDialog("请选择默认支付账户");
//                	}
//                }
            	
            	if (QRPayMainFragment.isSetDefaultCard) {
            		if (selectPayAccount != null && !StringUtils.isEmpty(selectPayAccount.getAccountId())){
	                    showLoadingDialog();
	                    mSetCardBasePresenter.loadQRPaySetDefaultCard(selectPayAccount.getAccountId());
	                }else {
	                	showLoadingDialog();
	                    mSetCardBasePresenter.loadQRPaySetDefaultCard(QRPayMainFragment.mDefaultAccountBean.getAccountId());
					}
				}else{
					if (selectPayAccount != null && !StringUtils.isEmpty(selectPayAccount.getAccountId())){
	                    showLoadingDialog();
	                    mSetCardBasePresenter.loadQRPaySetDefaultCard(selectPayAccount.getAccountId());
	                }else{
	                	showErrorDialog("请选择默认支付账户");
	                }
				}
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode == SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT){
            selectPayAccount = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
            editChoiceWidget.setChoiceTextContent(NumberUtils.formatCardNumberStrong(selectPayAccount.getAccountNumber()));
            if (mAvailableBalance != null){
                llAccountAmount.setVisibility(View.VISIBLE);
                tvAccountAmount.setText(MoneyUtils.transMoneyFormat(mAvailableBalance, ApplicationConst.CURRENCY_CNY));
            }else{
                llAccountAmount.setVisibility(View.GONE);
            }
        }
    }

    /***
     * 查询账户详情成功
     */
    @Override
    public void queryAccountDetailsSuccess(BigDecimal availableBalance) {
        closeProgressDialog();
        mAvailableBalance = availableBalance;
        if (isSelectAccount) {
            selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
            selectAccoutFragment.pop();
        }else{
            if (mAvailableBalance != null){
                llAccountAmount.setVisibility(View.VISIBLE);
                tvAccountAmount.setText(MoneyUtils.transMoneyFormat(mAvailableBalance, ApplicationConst.CURRENCY_CNY));
            }else{
                llAccountAmount.setVisibility(View.GONE);
            }
        }
    }

    /***
     * 查询账户详情失败
     */
    @Override
    public void queryAccountDetailsFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        llAccountAmount.setVisibility(View.GONE);
        if (isSelectAccount) {
            selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
            selectAccoutFragment.pop();
        }
    }

    /*** 查询信用卡账户详情成功 */
    @Override
    public void queryCreditAccountDetailSuccess(BigDecimal availableBalance) {
        closeProgressDialog();
        mAvailableBalance = availableBalance;
        if (isSelectAccount){
            selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
            selectAccoutFragment.pop();
        }else{
            tvAccountAmount.setText(mAvailableBalance.toString());
        }
    }

    /*** 查询信用卡账户详情失败 */
    @Override
    public void queryCreditAccountDetailFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 设置默认卡成功
     */
    @Override
    public void loadQRPaySetDefaultCardSuccess() {
        closeProgressDialog();
        showSetDefaultCardSuccessDialog();
    }

    /**
     * 设置默认卡失败
     * @param biiResultErrorException
     */
    @Override
    public void loadQRPaySetDefaultCardFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }



    /**
     * 显示设置默认卡成功对话框
     */
    private void showSetDefaultCardSuccessDialog() {
        final TitleAndBtnDialog setCardSuccessDialog = new TitleAndBtnDialog(mContext);
        setCardSuccessDialog.setBtnName(new String[] {getString(R.string.boc_common_sure)});
        setCardSuccessDialog.isShowTitle(false);
        setCardSuccessDialog.setCanceledOnTouchOutside(false);
        setCardSuccessDialog.setCancelable(false);
        setCardSuccessDialog.setNoticeContent("默认支付账户设置成功");
        setCardSuccessDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {
            }

            @Override
            public void onRightBtnClick(View view) {
                setCardSuccessDialog.dismiss();
                QRPayMainFragment.isSetDefaultCard = true;
                QRPayMainFragment.mDefaultAccountBean = selectPayAccount;
                popToAndReInit(QRPayMainFragment.class);
            }
        });
        setCardSuccessDialog.show();
    }

}
