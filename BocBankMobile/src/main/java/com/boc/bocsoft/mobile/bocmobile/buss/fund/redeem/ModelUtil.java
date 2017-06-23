package com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightSell.PsnFundNightSellParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightSell.PsnFundNightSellResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQuickSell.PsnFundQuickSellParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQuickSell.PsnFundQuickSellResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundSell.PsnFundSellParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundSell.PsnFundSellResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQuickSellQuotaQuery.PsnQuickSellQuotaQueryResult;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem.model.FundRedeemModel;

/**
 * Created by zcy7065 on 2016/12/14.
 */
public class ModelUtil {
    /**
     * 生成基金赎回上送参数params
     * */
    public static PsnFundSellParams generateFundSellParams(FundRedeemModel fundRedeemModel,String token,String conversationId){
        PsnFundSellParams params = new PsnFundSellParams();
        params.setFundCode(fundRedeemModel.getFundCode());
        params.setFeeType(fundRedeemModel.getFeeType());
        params.setSellAmount(fundRedeemModel.getSellAmount());
        params.setFundSellFlag(fundRedeemModel.getFundSellFlag());
        params.setToken(token);
        params.setExecuteType(fundRedeemModel.getExecuteType());
        if(fundRedeemModel.getExecuteType().equals("1")){
            params.setAssignedDate(fundRedeemModel.getAssignedDate());
        }
        params.setConversationId(fundRedeemModel.getConversationId());
        return params;
    }

    /**
     * 生成基金赎回model
     * */
    public static FundRedeemModel generateFundSellModel(FundRedeemModel fundRedeemModel, PsnFundSellResult psnFundSellResult){
        if (psnFundSellResult == null){
            return fundRedeemModel;
        }
        fundRedeemModel.setFundSeq(psnFundSellResult.getFundSeq());
        fundRedeemModel.setTranState(psnFundSellResult.getTranState());
        fundRedeemModel.setTransactionId(psnFundSellResult.getTransactionId());
        return fundRedeemModel;
    }

    /**
     * 生成基金(挂单)赎回上送参数params
     * */
    public static PsnFundNightSellParams generateFundNightSellParams(FundRedeemModel fundNightRedeemModel, String token, String conversationId){
        PsnFundNightSellParams params = new PsnFundNightSellParams();
        params.setFundCode(fundNightRedeemModel.getFundCode());
        params.setFeeType(fundNightRedeemModel.getFeeType());
        params.setSellAmount(fundNightRedeemModel.getSellAmount());
        params.setFundSellFlag(fundNightRedeemModel.getFundSellFlag());
        params.setToken(token);
        if(fundNightRedeemModel.getExecuteType().equals("1")){
            params.setAssignedDate(fundNightRedeemModel.getAssignedDate());
        }
        params.setAssignedDate(fundNightRedeemModel.getAssignedDate());
        params.setConversationId(fundNightRedeemModel.getConversationId());
        return params;
    }

    /**
     * 生成基金(挂单)赎回model
     * */
    public static FundRedeemModel generateFundNightSellModel(FundRedeemModel fundNgihtRedeemModel, PsnFundNightSellResult psnFundNightSellResult){
        if (psnFundNightSellResult == null){
            return fundNgihtRedeemModel;
        }
        fundNgihtRedeemModel.setConsignSeq(psnFundNightSellResult.getConsignSeq());
        fundNgihtRedeemModel.setTranState(psnFundNightSellResult.getTranState());
        fundNgihtRedeemModel.setTransactionId(psnFundNightSellResult.getTransactionId());
        return fundNgihtRedeemModel;
    }

    /**
     * 生成基金(快速)赎回上送参数params
     * */
    public static PsnFundQuickSellParams generateFundQuickSellParams(FundRedeemModel fundQuickRedeemModel, String token, String conversationId){
        PsnFundQuickSellParams params = new PsnFundQuickSellParams();
        return params;
    }

    /**
     * 生成基金(快速)赎回结果model
     * */
    public static FundRedeemModel generateFundQuickSellModel(FundRedeemModel fundQuickRedeemModel, PsnFundQuickSellResult psnFundQuickSellResult){
        if (psnFundQuickSellResult == null){
            return fundQuickRedeemModel;
        }

        return fundQuickRedeemModel;
    }

    /**
     * 生成基金快速赎回额度查询结果model
     * */
    public static FundRedeemModel generateQuickSellQuotaQuery(final FundRedeemModel fundRedeemModel, PsnQuickSellQuotaQueryResult psnQuickSellQuotaQueryResult){
        if(psnQuickSellQuotaQueryResult == null){
            return fundRedeemModel;
        }
        fundRedeemModel.setDayLimit(psnQuickSellQuotaQueryResult.getDayLimit());
        fundRedeemModel.setDayCompleteNum(psnQuickSellQuotaQueryResult.getDayCompleteNum());
        fundRedeemModel.setDayCompleteShare(psnQuickSellQuotaQueryResult.getDayCompleteShare());
        fundRedeemModel.setTotalLimit(psnQuickSellQuotaQueryResult.getTotalLimit());
        fundRedeemModel.setTotalBalance(psnQuickSellQuotaQueryResult.getTotalBalance());
        fundRedeemModel.setPerDealNum(psnQuickSellQuotaQueryResult.getPerDealNum());
        fundRedeemModel.setDayFrozenLimit(psnQuickSellQuotaQueryResult.getDayFrozenLimit());
        fundRedeemModel.setDayQuickSellLimit(psnQuickSellQuotaQueryResult.getDayQuickSellLimit());
        fundRedeemModel.setDayQuickSellNum(psnQuickSellQuotaQueryResult.getDayQuickSellNum());
        fundRedeemModel.setDayQuickSellLimit(psnQuickSellQuotaQueryResult.getDayQuickSellLimit());
        return fundRedeemModel;
    }

}
