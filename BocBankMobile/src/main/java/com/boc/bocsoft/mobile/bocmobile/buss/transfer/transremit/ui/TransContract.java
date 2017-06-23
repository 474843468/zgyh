package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui;

import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.PsnQueryTransActivityStatus.PsnQueryTransActivityStatusResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCardQueryBindInfo.PsnCardQueryBindInfoParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCrcdChargeOnRMBAccountQuery.PsnCrcdChargeOnRMBAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocNationalTransferVerify.PsnDirTransBocNationalTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocNationalTransferVerify.PsnDirTransBocNationalTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferSubmit.PsnDirTransBocTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferVerify.PsnDirTransBocTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferVerify.PsnDirTransBocTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankAddPayee.PsnDirTransCrossBankAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransfer.PsnDirTransCrossBankTransferParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransfer.PsnDirTransCrossBankTransferResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransferSubmit.PsnDirTransCrossBankTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalAddPayee.PsnDirTransNationalAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalTransferSubmit.PsnDirTransNationalTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentConfirm.PsnEbpsRealTimePaymentConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentConfirm.PsnEbpsRealTimePaymentConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentSavePayee.PsnEbpsRealTimePaymentSavePayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentTransfer.PsnEbpsRealTimePaymentTransferResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAFinanceTransfer.PsnOFAFinanceTransferResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryBankInfobyCardBin.PsnQueryBankInfobyCardBinParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryBankInfobyCardBin.PsnQueryBankInfobyCardBinResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryChinaBankAccount.PsnQueryChinaBankAccountParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryChinaBankAccount.PsnQueryChinaBankAccountResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryRecentPayeeInfo.PsnQueryRecentPayeeInfoBean;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryRecentPayeeInfo.PsnQueryRecentPayeeInfoParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnSingleTransQueryTransferRecord.PsnSingleTransQueryTransferRecordParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnSingleTransQueryTransferRecord.PsnSingleTransQueryTransferRecordResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocAddPayee.PsnTransBocAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocNationalTransferVerify.PsnTransBocNationalTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocNationalTransferVerify.PsnTransBocNationalTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferSubmit.PsnTransBocTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetNationalTransferCommissionCharge.PsnTransGetNationalTransferCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetNationalTransferCommissionCharge.PsnTransGetNationalTransferCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransLinkTransferSubmit.PsnTransLinkTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalAddPayee.PsnTransNationalAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalChangeBooking.PsnTransNationalChangeBookingResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalTransferSubmit.PsnTransNationalTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim.PsnTransPayeeListqueryForDimParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim.PsnTransPayeeListqueryForDimResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryExternalBankInfo.PsnTransQueryExternalBankInfoResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordDetail.PsnTransQueryTransferRecordDetailParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordDetail.PsnTransQueryTransferRecordDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.AccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.CardQueryBindInfoResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.CrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.OFAAccountStateQueryResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.TransRemitVerifyInfoViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by wangy on 2016/6/14.
 */
public class TransContract {

    public interface TransViewAccSelectPage extends BaseView<TransPresenterAccSelectPage>{
        //查询电子卡绑定账户
        void queryPsnCardQueryBindInfoSuccess(CardQueryBindInfoResult result);
        void queryPsnCardQueryBindInfoFailed(BiiResultErrorException exception);

        //查询余额成功
        void queryAccountBalanceSuccess(AccountQueryAccountDetailResult result);
        void queryAccountBalanceFailed(BiiResultErrorException exception);

        //查询信用详情成功
        void queryCrcdAccountBalanceSuccess(CrcdQueryAccountDetailResult result);
        void queryCrcdAccountBalanceFailed(BiiResultErrorException exception);

        //查询理财账户
        void queryPsnOFAAccountStateSuccess(OFAAccountStateQueryResult result);
        void queryPsnOFAAccountStateFailed(BiiResultErrorException exception);
    }

    public interface TransViewPayeeAccSelectPage extends BaseView<TransPresenterPayeeAccSelectPage>{
        //查询电子卡绑定账户
//        void queryPsnCardQueryBindInfoSuccess(CardQueryBindInfoResult result);
//        void queryPsnCardQueryBindInfoFailed(BiiResultErrorException exception);

        //查询余额成功
        void queryAccountBalanceSuccess(AccountQueryAccountDetailResult result);
        void queryAccountBalanceFailed(BiiResultErrorException exception);

        //查询信用详情成功
        void queryCrcdAccountBalanceSuccess(CrcdQueryAccountDetailResult result);
        void queryCrcdAccountBalanceFailed(BiiResultErrorException exception);

        //查询理财账户
//        void queryPsnOFAAccountStateSuccess(OFAAccountStateQueryResult result);
//        void queryPsnOFAAccountStateFailed(BiiResultErrorException exception);
        void queryPsnCrcdChargeOnRMBAccountSuccess(PsnCrcdChargeOnRMBAccountQueryResult result);
        void queryPsnCrcdChargeOnRMBAccountFaild(BiiResultErrorException exception);
    }
    /**
     * 填写页面所调用接口
     */
    public interface TransViewBlankPage extends BaseView<TransPresenterBlankPage>{

        void getConverIdandSaftyFatorSuccess(PsnGetSecurityFactorResult factorResult,String conversationId);
        void getConverIdandSaftyFatorFailed(BiiResultErrorException exception);

        //根据卡BIN 查询所属行信息
        void queryBankInfobyCardBinSuccess(PsnQueryBankInfobyCardBinResult result);
        void queryBankInfobyCardBinFailed(BiiResultErrorException exception);

        //查询电子卡绑定账户
        void queryPsnCardQueryBindInfoSuccess(CardQueryBindInfoResult result);
        void queryPsnCardQueryBindInfoFailed(BiiResultErrorException exception);

//        void queryRecentAndCommenPayeeSuccess(List<PsnQueryRecentPayeeInfoBean> result);
//        void queryRecentAndCommenPayeeSuccess(PsnTransPayeeListqueryForDimResult result);
//        void queryRecentAndCommenPayeeFailed(BiiResultErrorException exception);
//        void queryRecentAndCommenPayeeFailed(BiiResultErrorException exception);
//        void
        //查询近期收款人
        void queryRecentPayeeInfoSuccess(List<PsnQueryRecentPayeeInfoBean> result);
        void queryRecentPayeeInfoFailed(BiiResultErrorException exception);
//        void queryRecentAndCommenPayeeFinished();
        // 查询收款人列表（模糊查询)
        void queryPayeeListForDimSuccess(PsnTransPayeeListqueryForDimResult result);
        void queryPayeeListForDimFailed(BiiResultErrorException exception);
        //查询关联账户结果
//        void queryLinkedAccountSuccess(PsnQueryChinaBankAccountResult result);
//        void queryLinkedAccountFailed(BiiResultErrorException exception);

        //查询余额成功
        void queryAccountBalanceSuccess(AccountQueryAccountDetailResult result);
        void queryAccountBalanceFailed(BiiResultErrorException exception);

        //查询信用详情成功
        void queryCrcdAccountBalanceSuccess(CrcdQueryAccountDetailResult result);
        void queryCrcdAccountBalanceFailed(BiiResultErrorException exception);

        //查询理财账户
        void queryPsnOFAAccountStateSuccess(OFAAccountStateQueryResult result);
        void queryPsnOFAAccountStateFailed(BiiResultErrorException exception);
        /**
         *  关联账户转账
         */
        //查询关联账户
//        void transLinkedAccoutSuccess(PsnTransLinkTransferSubmitResult result);
//        void transLinkedAccoutFailed(BiiResultErrorException exception);

        void queryQuotaForTransSuccess(PsnTransQuotaQueryResult result);
        void queryQuotaForTransFailed(BiiResultErrorException exception);
        //查询收款人账户列表
//        void queryTransPayeeListSuccess(PsnTransPayeeListqueryForDimResult result);
//        void queryTransPayeeListFailed(BiiResultErrorException exception);

        //转账预交易
        void transBocVerifySuccess(PsnTransBocTransferVerifyResult results, PsnGetSecurityFactorResult factorResult, String conversationId);
        void transBocVerifyFailed(BiiResultErrorException exception);

        //定向转账预交易
        void transDirBocVerifySuccess(PsnDirTransBocTransferVerifyResult result,  PsnGetSecurityFactorResult factorResult,String conversationId);
        void transDirBocVerifyFailed(BiiResultErrorException exception);

        //行内转账费用试算
        void getTransBocCommissionChargeSuccess(PsnTransGetBocTransferCommissionChargeResult result);
        void getTransBocCommissionChargeFailed(BiiResultErrorException exception);
        //行内转账费用试算
        void getTransNationalCommissionChargeSuccess(PsnTransGetNationalTransferCommissionChargeResult result);
        void getTransNationalCommissionChargeFailed(BiiResultErrorException exception);



        /**
         * 跨行
         */
        //跨行定向实时转账预交易
        void transDirNationalRealTimeVerifySuccess(PsnDirTransCrossBankTransferResult result, PsnGetSecurityFactorResult factorResult,String conversationId);
        void transDirNationalRealTimeVerifyFailed(BiiResultErrorException exception);


        //跨行定向实时转账交易提交加强认证
//        void transDirNationalRealTimeSubmitReinforceSuccess(PsnDirTransCrossBankTransferSubmitReinforceResult result);
//        void transDirNationalRealTimeSubmitReinforceFailed(BiiResultErrorException exception);


        //跨行实时转账预交易
        void transNationalRealTimeVerifySuccess(PsnEbpsRealTimePaymentConfirmResult result, PsnGetSecurityFactorResult factorResult,String conversationId);
        void transNationalRealTimeVerifyFailed(BiiResultErrorException exception);


        //跨行定向预交易
        void transDirNationalVerifySuccess(PsnDirTransBocNationalTransferVerifyResult result,  PsnGetSecurityFactorResult factorResult,String conversationId);
        void transDirNationalVerifyFailed(BiiResultErrorException exception);


        //跨行转账预交易
        void transNationalVerifySuccess(PsnTransBocNationalTransferVerifyResult result, PsnGetSecurityFactorResult factorResult,String conversationId);
        void transNationalVerifyFailed(BiiResultErrorException exception);


//        void transNationalQueryBankInfoSuccess(PsnTransQueryExternalBankInfoResult result);
//        void transNationalQueryBankInfoFailed(BiiResultErrorException exception);

    }

    /**
     * 确认页面所调用接口
     */
    public  interface TransViewVerifyPage extends BaseView<TransPresenterVerifyPage>{

        void getRandomNumSuccess(String number);
        void getRandomNumFailed(BiiResultErrorException exception);

        void addBocPayeeSuccess(PsnTransBocAddPayeeResult result);
        void addBocPayeeFailed(BiiResultErrorException exception);

        void addNationalPayeeSuccess(PsnTransNationalAddPayeeResult result);
        void addNationalPayeeFailed(BiiResultErrorException exception);

        void addNationalRealtimePayeeSuccess(PsnEbpsRealTimePaymentSavePayeeResult result);
        void addNationalRealtimePayeeFailed(BiiResultErrorException exception);


        void addDirNationalRealtimePayeeSuccess(PsnDirTransCrossBankAddPayeeResult result);
        void addDirNationalRealtimePayeeFailed(BiiResultErrorException exception);

        void addDirNationalPayeeSuccess(PsnDirTransNationalAddPayeeResult result);
        void addDirNationalPayeeFailed(BiiResultErrorException exception);

        void updatePayeeListSuccess(PsnTransPayeeListqueryForDimResult result);
        void updatePayeeListFailed(BiiResultErrorException exception);
        //请求随机数
//        void getRandomResultSuccess(String randomResponse);
//        void getRandomResultFailed(BiiResultErrorException exception);

        //中行内转账预预交易
        void transBocVerifySuccess(PsnTransBocTransferVerifyResult result);
        void transBocVerifyFailed(BiiResultErrorException exception);

        //中行内定向转账预交易
        void transDirVerifySuccess(PsnDirTransBocTransferVerifyResult result);
        void transDirVerifyFailed(BiiResultErrorException exception);
        //跨行定向实时转账预交易
        void transDirNationalRealTimeVerifySuccess(PsnDirTransCrossBankTransferResult result);
        void transDirNationalRealTimeVerifyFailed(BiiResultErrorException exception);
        //跨行转账预交易
        void transNationalVerifySuccess(PsnTransBocNationalTransferVerifyResult result);
        void transNationalVerifyFailed(BiiResultErrorException exception);
        //跨行定向预交易
        void transDirNationalVerifySuccess(PsnDirTransBocNationalTransferVerifyResult result);
        void transDirNationalVerifyFailed(BiiResultErrorException exception);
        //跨行实时转账预交易
        void transNationalRealTimeVerifySuccess(PsnEbpsRealTimePaymentConfirmResult result);
        void transNationalRealTimeVerifyFailed(BiiResultErrorException exception);


        //关联账户转账
        void transLinkTransferSubmitSuccess(PsnTransLinkTransferSubmitResult result);
        void transLinkTransferSubmitFailed(BiiResultErrorException exception);
        //资金划转
        void transPsnOFAFinanceSubmitSuccess(PsnOFAFinanceTransferResult result);
        void transPsnOFAFinanceSubmitFailed(BiiResultErrorException exception);

        //中行转账提交交易
        void transBocSubmitSuccess(PsnTransBocTransferSubmitResult result);
        void transBocSubmitFailed(BiiResultErrorException exception);
        //中行转账交易加强认证交易
//        void transBocSubmitReinforceSuccess(PsnTransBocTransferSubmitReinforceResult result);
//        void transBocSubmitReinforceFailed(BiiResultErrorException exception);


        //中行定向转账提交交易
        void transBocDirSubmitSuccess(PsnDirTransBocTransferSubmitResult result);
        void transBocDirSubmitFailed(BiiResultErrorException exception);
        //定向转账加强认证交易
//        void transBocDirSumbmitReinforeSuccess(PsnDirTransBocTransferSubmitReinforceResult result);
//        void transBocDirSumbmitReinforeFailed(BiiResultErrorException exception);

        //跨行实时转账交易提交
        void transNationalRealTimeSubmitSuccess(PsnEbpsRealTimePaymentTransferResult result);
        void transNationalRealTimeSubmitFailed(BiiResultErrorException exception);
        //跨行实时转账交易提交加强认证
//        void transNationalRealTimeSubmitReinforceSuccess(PsnEbpsRealTimePaymentTransferReinforceResult result);
//        void transNationalRealTimeSubmitReinforceFailed(BiiResultErrorException exception);

        //跨行定向实时转账交易提交
        void transDirNationalRealTimeSubmitSuccess(PsnDirTransCrossBankTransferSubmitResult result);
        void transDirNationalRealTimeFailed(BiiResultErrorException exception);

        //跨行定向提交交易
        void transDirNationalSubmitSuccess(PsnDirTransNationalTransferSubmitResult result);
        void transDirNationalSubmitFailed(BiiResultErrorException exception);
        //跨行定向提交交易加强认证
//        void transDirNationalSubmitReinforeSuccess(PsnDirTransBocTransferSubmitReinforceResult result);
//        void transDirNationalSubmitReinforeFailed(BiiResultErrorException exception);


        //跨行转账提交交易
        void transNationalSubmitSuccess(PsnTransNationalTransferSubmitResult result);
        void transNationalSubmitFailed(BiiResultErrorException exception);
        //跨行转账提交交易加强认证
//        void transNationalSubmitReinforceSuccess(PsnTransNationalTransferSubmitResult result);
//        void transNationalSubmitReinforceFailed(BiiResultErrorException exception);
        //跨行转账非工作时间转预约
        void transNationalChangeBookingSuccess(PsnTransNationalChangeBookingResult result);
        void transNationalChangeBookingFailed(BiiResultErrorException exception);

        //增加收款人
        void addTransPayeeSuccess(PsnTransBocAddPayeeResult result);
        void addTransPayeeFailed(BiiResultErrorException exception);
    }

    public  interface TransViewResultPage extends BaseView<TransPresenterResultPage>{
        //查询实时转账（包括定向实时）交易结果
//        void transNationalRealtimeRecordSuccess(PsnSingleTransQueryTransferRecordResult recordResult);
//        void transNationalRealtimeRecordFailed(BiiResultErrorException exception);//查询实时转账（包括定向实时）交易结果
        void transNationalRecordDetailSuccess(PsnTransQueryTransferRecordDetailResult recordResult);
        void transNationalRecordDetailFailed(BiiResultErrorException exception);

        //抽奖活动
        void queryTransActivityStatusSuccess(PsnQueryTransActivityStatusResult psnQueryTransActivityStatusResult);
        void queryTransActivityStatusFailed();



    }

    /**
     * 填写页面
     */
    public interface TransPresenterBlankPage extends BasePresenter {

         void getConverIdandSaftyFator(final String serviceid);
        //根据卡BIN 查询所属行信息
        void queryQueryBankInfobyCardBin(PsnQueryBankInfobyCardBinParams params);

        //查询限额quota
        void queryQuotaForTrans(PsnTransQuotaQueryParams params);
//        void queryRecentAndCommenPayee(PsnQueryRecentPayeeInfoParams params);

        //查询最近收款人
//        Observable<List<PsnQueryRecentPayeeInfoBean>> queryRecentPayeeInfo(PsnQueryRecentPayeeInfoParams params);//查询最近收款人
        void queryRecentPayeeInfo(PsnQueryRecentPayeeInfoParams params);//查询最近收款人
       // 查询收款人列表（模糊查询)
//       Observable<PsnTransPayeeListqueryForDimResult> queryPayeeListForDim();
       void queryPayeeListForDim();

        //仅供测试，可以删除
        void queryTransActivityStatusTest(String transactionId);

        /**
         * 中行
         */
        //查询电子卡绑定账户
        void querPsnCardQueryBindInfo(PsnCardQueryBindInfoParams params);
        //查询账户余额
        void queryAccountBalance(String accountId);
        //查询信用卡币种
//        void queryCrcdCurrency()
        //查询信用卡详情v
         void queryCrcdAccountDetail(String accountId, final String currency);
        //查询网上理财账户信息
        void queryPsnOFAAccountState();
        //查询理财账户
//        void queryOFAAccountState(String accountId, final String currency);
        //查询收款人账户列表
//        void queryLinkedAccountList(PsnQueryChinaBankAccountParams linkedParams);
        //查询中行或者他行收款人列表
//        void queryTransPayeeList(PsnTransPayeeListqueryForDimParams bocParams);


        //增加收款人
//        void addTransPayee();
        //转账预交易
        void transBocVerify(PsnTransBocTransferVerifyParams params);
        //转账提交交易

        //定向转账预交易
        void transDirBocVerify(PsnDirTransBocTransferVerifyParams params);

        //行内转账费用试算
        void getTransBocCommissionCharge(PsnTransGetBocTransferCommissionChargeParams params);

        //查询账户余额
        void getBocAccountBalance();

        /**
         * 跨行
         */

        //行内转账费用试算
        void getTransNationalCommissionCharge(PsnTransGetNationalTransferCommissionChargeParams params);

        //跨行定向实时转账预交易
        void transDirNationalRealTimeVerify(PsnDirTransCrossBankTransferParams dirNationalRealtimeParams);



        //跨行实时转账预交易
        void transNationalRealTimeVerify(PsnEbpsRealTimePaymentConfirmParams params);
        //跨行实时转账交易提交


        //跨行定向预交易
        void transDirNationalVerify(PsnDirTransBocNationalTransferVerifyParams dirNationalVerifyParams);


        //跨行转账预交易
        void transNationalVerify(PsnTransBocNationalTransferVerifyParams params);


        //跨行转账查询开户行
        void transNationQueryBankInfo();

    }

    public interface TransPresenterVerifyPage extends BasePresenter{

        //得到随机数
        void getRandomNum(String conversationId);
        //中行转账预交易
        void transBocVerify(PsnTransBocTransferVerifyParams params);


        //中行定向转账预交易
        void transDirBocVerify(PsnDirTransBocTransferVerifyParams params);
        //关联转账提交交易
        void transLinkTransferSubmit(TransRemitVerifyInfoViewModel dataModel);

        void transPsnOFAFinanceSubmit(TransRemitVerifyInfoViewModel dataModel);


        //提交
        void transBocSubmit(TransRemitVerifyInfoViewModel dataModel);
        //中行转账交易加强认证交易
//        void transBocSubmitReinforce();
        //跨行定向实时转账预交易
        void transDirNationalRealTimeVerify(PsnDirTransCrossBankTransferParams prams);

        //跨行定向预交易
        void transDirNationalVerify(PsnDirTransBocNationalTransferVerifyParams params);


        //跨行转账预交易
        void transNationalVerify(PsnTransBocNationalTransferVerifyParams params);

        //跨行实时转账预交易
        void transNationalRealTimeVerify(PsnEbpsRealTimePaymentConfirmParams params);
        //定向转账提交交易
        void transDirBocSubmit(TransRemitVerifyInfoViewModel dataModel);
        //定向转账加强认证交易
        void transDirBocSumbmitReinfore();

        void updatePayeeList();

        //跨行转账非工作时间转预约
//        void transNationalChangeBooking(TransRemitVerifyInfoViewModel verifyInfoViewModel,String transType);
        void transNationalChangeBooking(String conversationId,String transType);
        void transNationalRealTimeSubmit(final TransRemitVerifyInfoViewModel dataModel);
        //跨行实时转账交易提交加强认证
//        void transNationalRealTimeSubmitReinforce();

        //跨行定向提交交易
        void transDirNationalSubmit(TransRemitVerifyInfoViewModel dataModel);
        //跨行定向提交交易加强认证
        void transDirNationalSubmitReinfore();
        //跨行转账提交交易
        void transNationalSubmit(TransRemitVerifyInfoViewModel dataModel);
        //跨行转账提交交易加强认证
//        void transNationalSubmitReinforce();
        //跨行定向实时转账交易提交
        void transDirNationalRealTimeSubmit(TransRemitVerifyInfoViewModel dataModel);
        //跨行定向实时转账交易提交加强认证
//        void transDirNationalRealTimeSubmitReinforce();
    }


    public interface  TransPresenterResultPage extends BasePresenter{
        //查询实时转账（包括定向实时）交易结果PsnSingleTransQueryTransferRecordParams
//        void transNationalRealtimeRecord(PsnSingleTransQueryTransferRecordParams params);
        //查询实时转账（包括定向实时）交易结果PsnSingleTransQueryTransferRecordParams
        Observable<PsnTransQueryTransferRecordDetailResult> transNationalRecordDetail(PsnTransQueryTransferRecordDetailParams params);
        void queryTransActivityStatus(String transactionId);
    }

    public interface TransPresenterAccSelectPage extends BasePresenter{
        //查询电子卡绑定账户
        void querPsnCardQueryBindInfo(PsnCardQueryBindInfoParams params);
        //查询账户余额
        void queryAccountBalance(String accountId);
        //查询信用卡币种
//        void queryCrcdCurrency()
        //查询信用卡详情v
        void queryCrcdAccountDetail(String accountId, final String currency);
        //查询网上理财账户信息
        void queryPsnOFAAccountState();
    }
    public interface TransPresenterPayeeAccSelectPage extends BasePresenter{
        //查询电子卡绑定账户
//        void querPsnCardQueryBindInfo(PsnCardQueryBindInfoParams params);
        //查询账户余额
        void queryAccountBalance(String accountId);
        //查询信用卡币种
//        void queryCrcdCurrency()
        //查询信用卡详情v
        void queryCrcdAccountDetail(String accountId, final String currency);
        //查询网上理财账户信息
//        void queryPsnOFAAccountState();
        //全球交易人民币记账功能查询
        void queryPsnCrcdChargeOnRMBAccount(String accoutId);
    }
}
