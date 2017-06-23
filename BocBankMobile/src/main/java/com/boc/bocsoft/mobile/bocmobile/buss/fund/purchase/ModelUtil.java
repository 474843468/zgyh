package com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundBuy.PsnFundBuyParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundBuy.PsnFundBuyResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightBuy.PsnFundNightBuyParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightBuy.PsnFundNightBuyResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundSignElectronicContract.PsnFundSignElectronicContractParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundSignElectronicContract.PsnFundSignElectronicContractResult;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.model.FundbuyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcy7065 on 2016/11/26.
 * 基金购买模块model生成
 */
public class ModelUtil {

    /**
     * 生成基金买入上送参数params
     * */
    public static PsnFundBuyParams generateFundBuyParams(FundbuyModel fundbuyModel, String token,String conversationId){
        PsnFundBuyParams params = new PsnFundBuyParams();
        params.setFundCode(fundbuyModel.getFundCode());
        params.setBuyAmount(fundbuyModel.getBuyAmount());
        params.setFeetype(fundbuyModel.getFeeType());
        params.setToken(token);
        params.setAssignedDate(fundbuyModel.getAssignedDate());
        params.setAffirmFlag(fundbuyModel.getAffirmFlag());
        params.setExecuteType(fundbuyModel.getExecuteType());
        params.setConversationId(conversationId);
        return  params;
    }

    /**
     * 生成基金买入model
     * */
    public static FundbuyModel generateFundBuyModel(FundbuyModel fundbuyModel,PsnFundBuyResult psnFundBuyResult){
        if (psnFundBuyResult == null){
            return fundbuyModel;
        }
        fundbuyModel.setFundSeq(psnFundBuyResult.getFundSeq());
        fundbuyModel.setIsSignedFundEval(psnFundBuyResult.getIsSignedFundEval());
        fundbuyModel.setTranState(psnFundBuyResult.getTranState());
        fundbuyModel.setIsMatchEval(psnFundBuyResult.getIsMatchEval());
        fundbuyModel.setIsSignedFundStipulation(psnFundBuyResult.getIsSignedFundStipulation());
        fundbuyModel.setConsignSeq(psnFundBuyResult.getConsignSeq());
        fundbuyModel.setTransactionId(psnFundBuyResult.getTransactionId());
        return fundbuyModel;
    }
    /**
     * 生成挂单买入上送参数params
     * */
    public  static PsnFundNightBuyParams generateFundNightBuyParams(FundbuyModel fundNightBuyModel, String token,String conversationId){
        PsnFundNightBuyParams params = new PsnFundNightBuyParams();
        params.setFundCode(fundNightBuyModel.getFundCode());
        params.setBuyAmount(fundNightBuyModel.getBuyAmount());
        params.setFeetype(fundNightBuyModel.getFeeType());
        params.setToken(token);
        params.setAssignedDate(fundNightBuyModel.getAssignedDate());
        params.setAffirmFlag(fundNightBuyModel.getAffirmFlag());
        params.setConversationId(conversationId);
        return params;
    }

    /**
     * 生成挂单买入Model
     * */
    public static FundbuyModel generateFundNightBuyModel(FundbuyModel fundNightBuyModel, PsnFundNightBuyResult psnFundNightBuyResult){
        if(psnFundNightBuyResult == null)
            return  fundNightBuyModel;
        fundNightBuyModel.setFundSeq(psnFundNightBuyResult.getFundSeq());
        fundNightBuyModel.setIsSignedFundEval(psnFundNightBuyResult.getIsSignedFundEval());
        fundNightBuyModel.setTranState(psnFundNightBuyResult.getTranState());
        fundNightBuyModel.setIsMatchEval(psnFundNightBuyResult.getIsMatchEval());
        fundNightBuyModel.setIsSignedFundStipulation(psnFundNightBuyResult.getIsSignedFundStipulation());
        fundNightBuyModel.setConsignSeq(psnFundNightBuyResult.getConsignSeq());
        fundNightBuyModel.setTransactionId(psnFundNightBuyResult.getTransactionId());
        return fundNightBuyModel;
    }

    /**
     * 生成签约合同Params
     * */
    public static PsnFundSignElectronicContractParams generateSignContractParams(FundbuyModel signContractModel,String token,String conversationId){
        PsnFundSignElectronicContractParams params = new PsnFundSignElectronicContractParams();
        params.setFincCode(signContractModel.getFundCode());
        params.setToken(token);
        params.setConversationId(conversationId);
        return  params;
    }

    /**
     * 生成签约合同Model
     * */
    public static FundbuyModel generateSignContractModel(FundbuyModel signContractModel, PsnFundSignElectronicContractResult result){
        if(result == null)
            return signContractModel;
        signContractModel.setInvestAccount(result.getInvestAccount());
        signContractModel.setAccountId(result.getAccountId());
        return  signContractModel;
    }

    /**
     * 生成账户详情查询返回Model
     * */
    public static FundbuyModel generateQueryAccountDetailModel(FundbuyModel queryAccountDetailModel, PsnAccountQueryAccountDetailResult result){
        if(result == null)
            return queryAccountDetailModel;
        queryAccountDetailModel.setAccOpenBank(result.getAccOpenBank());
        queryAccountDetailModel.setAccountType(result.getAccountType());
        queryAccountDetailModel.setAccountStatus(result.getAccountStatus());
        queryAccountDetailModel.setAccOpenDate(result.getAccOpenDate());
        List<FundbuyModel.AccountDetaiListBean> list = new ArrayList<>();
        for(PsnAccountQueryAccountDetailResult.AccountDetaiListBean i:result.getAccountDetaiList() ){
            FundbuyModel.AccountDetaiListBean modelBean = new FundbuyModel.AccountDetaiListBean();
            modelBean.setCurrencyCode(i.getCurrencyCode());
            modelBean.setCashRemit(i.getCashRemit());
            modelBean.setStatus(i.getStatus());
            modelBean.setBookBalance(i.getBookBalance());
            modelBean.setAvailableBalance(i.getAvailableBalance());
            list.add(modelBean);
        }
        queryAccountDetailModel.setAccountDetaiList(list);
        return  queryAccountDetailModel;
    }

}
