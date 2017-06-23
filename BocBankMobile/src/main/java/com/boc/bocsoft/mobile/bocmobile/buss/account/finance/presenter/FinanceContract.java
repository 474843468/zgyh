package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.presenter;

import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.FinanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.TransDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.TransferModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.TransferResultModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangyang
 *         16/6/20 16:01
 *         电子现金业务逻辑处理接口
 */
public class FinanceContract {

    public interface FinanceAccountView extends BaseView<BasePresenter> {

        /**
         * 电子现金账户概览
         * @param models
         */
        void psnFinanceAccountView(List<FinanceModel> models);
        /**
         * 设置账户余额
         * @param financeModel
         */
        void psnFinanceAccountBalanceView(FinanceModel financeModel);
        /**
         * 获取FinanceModel
         * @param financeModel
         */
        FinanceModel getFinanceModel(FinanceModel financeModel);
    }

    public interface FinanceAccountSignView extends BaseView<BasePresenter> {
        /**
         * 新建签约关系成功
         */
        void psnFinanceICSignCreateResSuccess();
    }

    public interface FinanceAccountTransferView extends BaseView<BasePresenter> {
        /**
         * 查询账户详情
         * @param financeModel
         */
        void psnFinanceAccountDetail(FinanceModel financeModel);
        /**
         * 账户交易明细
         * @param detailModels
         * @param transactionList
         */
        void psnFinanceTransferDetail(List<TransDetailModel> detailModels, List<TransactionBean> transactionList);
        /**
         * //查询绑定关系
         * @param isSign
         */
        void psnFinanceSignQuery(String isSign);
        /**
         * 解除签约关系成功
         */
        void cancelSignSuccess();
    }

    public interface FinanceAccountRechargeView extends BaseView<BasePresenter> {
        /**
         * 充值成功,给自己/他人
         * @param model
         */
        void psnFinanceTransferSuccess(TransferResultModel model);
    }

    public interface FinanceAccountRechargeInputView extends BaseView<BasePresenter>{
        /**
         * 默认电子现金账户
         * @param financeModel
         */
        void psnFinanceAccount(FinanceModel financeModel);
        /**
         * 默认银行账户余额
         * @param availableBalance
         * @param currency
         */
        void psnFinanceExpendAccount(BigDecimal availableBalance,String currency);

        void psnTransQuotaQuery(BigDecimal bigDecimal);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 电子现金账户概览
         */
        void psnFinanceAccountView();
        /**
         * 查询账户详情
         * @param financeModels
         */
        void psnFinanceICAccountDetail(List<FinanceModel> financeModels);
        /**
         * 查询电子现金账户所有信息 账户详情/交易明细/是否签约
         */
        void queryAllOfFinance(FinanceModel financeModel);
        /**
         * 查询电子现金账户详情
         * @param financeModel
         */
        void psnFinanceICAccountDetail (FinanceModel financeModel);
        /**
         * 解除签约绑定关系
         * @param financeModel
         */
        void psnFinanceICSignCancel(FinanceModel financeModel);
        /**
         * 新建签约关系预交易
         * @param financeModel
         * @param currentSecurityVerifyTypeId
         */
        void psnFinanceICSignCreate(FinanceModel financeModel, String currentSecurityVerifyTypeId);
        /**
         * 新建签约关系交易
         * @param financeModel
         * @param deviceInfoModel
         * @param factorId
         * @param randomNums
         * @param encryptPasswords
         */
        void psnFinanceICSignCreateRes(FinanceModel financeModel, DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) ;
    }

    public interface TransferPresenter extends BasePresenter{
        /**
         * 给自己充值
         * @param transferModel
         */
        void psnFinanceTransferSelf(TransferModel transferModel);
        /**
         * 给他人充值预交易
         * @param transferModel
         * @param combinId
         */
        void psnFinanceTransferOtherPre(TransferModel transferModel,String combinId);
        /**
         * 给他人充值交易
         * @param transferModel
         * @param deviceInfoModel
         * @param factorId
         * @param randomNums
         * @param encryptPasswords
         */
        void psnFinanceTransferOther(TransferModel transferModel, DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords);
        /**
         * 获取电子账户列表及余额
         */
        void psnFinanceAccount(FinanceModel financeModel);
        /**
         * 获取银行账户余额
         * @param accountId
         */
        void psnBankAccount(String accountId);
        /**
         * 获取银行交易限额
         */
        void psnTransQuotaQuery();
    }
}
