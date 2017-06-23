package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.presenter;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model.AccountInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model.MedicalModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model.TermlyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.TransDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * @author wangyang
 *         16/8/10 17:57
 */
public class OverviewContract {

    /**
     * 账户概览界面回调
     */
    public interface Overview extends BaseView<BasePresenter>{
        /**
         * 查询账户详情界面回调
         * @param model
         */
        void queryAccountDetail(AccountListItemViewModel model);
    }

    /**
     * 活期账户详情回调界面
     */
    public interface CurrentView extends BaseView<BasePresenter>{
        /**
         * 查询交易详情界面回调
         * @param list
         */
        void queryAccountTransDetail(List<TransactionBean> list);
        /**
         * 查询账户详情界面回调
         * @param model
         * @param accountInfoBean
         */
        void queryAccountDetail(AccountListItemViewModel model,AccountInfoBean accountInfoBean);
    }

    public interface RegularView extends BaseView<BasePresenter>{
        /**
         * 查询账户详情界面回调
         * @param models
         * @param accountInfoBean
         */
        void queryAccountDetail(List<TermlyViewModel> models, AccountInfoBean accountInfoBean);
        /**
         * 查询账户详情界面回调失败
         */
        void queryAccountDetailFail();
    }

    public interface MoreAccountView extends BaseView<BasePresenter>{
        /**
         * 查询动户通知
         * @param isOpen
         */
        void querySsm(boolean isOpen);
    }

    public interface AccountDetailView extends BaseView<BasePresenter>{
        /**
         * 修改账户别名
         * @param accountBean
         */
        void updateAccountNickName(AccountBean accountBean);
    }

    public interface MedicalView extends BaseView<BasePresenter>{
        /**
         * 医保账户详情查询回调
         *
         * @param medicalModel
         */
        void queryMedicalDetail(MedicalModel medicalModel);
        /**
         * 医保账户交易明细查询回调
         * @param transactionList
         */
        void queryMedicalTransferDetail(List<TransactionBean> transactionList);
    }

    public interface VirtualView extends BaseView<BasePresenter>{
        /**
         * 回调界面,返回虚拟卡Model
         * @param virtualCardModel
         */
        void queryVirtualDetail(VirtualCardModel virtualCardModel);
    }

    /**
     * 账户概览逻辑处理
     */
    public interface Presenter extends BasePresenter{
        /**
         * 根据账户列表查询账户详情
         * @param accountBeans
         */
        void queryAccountDetail(List<AccountBean> accountBeans);
        /**
         * 根据账户信息查询账户详情
         * @param accountBean
         */
        void queryAccountDetail(AccountBean accountBean);
        /**
         * 根据账户信息查询账户详情和交易详情
         * @param accountBean
         */
        void queryAccountDetailAndTransaction(AccountBean accountBean);
        /**
         *   查询普通电子现金账户余额
         */
        void queryFinanceDetail(AccountBean accountBean);
        /**
         * 根据定期账户信息查询账户详情
         * @param accountBean
         */
        void queryRegularAccountDetail(AccountBean accountBean);
        /**
         *  根据索引获取交易明细
         * @param position
         * @return
         */
        TransDetailViewModel.ListBean getTransDetail(int position);
        /**
         * 查询动户通知
         * @param accountId
         */
        void querySsm(String accountId);
        /**
         *  修改账户别名
         * @param accountBean
         * @param nickName
         */
        void updateAccountNickName(AccountBean accountBean,String nickName);
        /**
         * 查询虚拟卡详情
         * @param accountBean
         */
        void queryVirtualDetail(AccountBean accountBean);
    }

    public interface MedicalInsurancePresenter extends BasePresenter{
        /**
         * 查询医保账户详情
         * @param accountId
         */
        void queryMedicalDetail(String accountId);
        /**
         * 医保账户交易明细查询
         * @param medicalModel
         */
        void queryMedicalTransferDetail(MedicalModel medicalModel);
    }
}
