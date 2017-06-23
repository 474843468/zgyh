package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQuerySystemDateTime.PsnCommonQuerySystemDateTimeParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageIsOpen.PsnInvestmentManageIsOpenParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadExpectYieldQuery.PsnXpadExpectYieldQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadExpectYieldQueryOutlay.PsnXpadExpectYieldQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBalanceQuery.PsnXpadProductBalanceQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBalanceQuery.PsnXpadProductBalanceQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQuery.PsnXpadProgressQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQuery.PsnXpadProgressQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQueryOutlay.PsnXpadProgressQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitDetailQuery.PsnXpadReferProfitDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitDetailQuery.PsnXpadReferProfitDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitQuery.PsnXpadReferProfitQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitQuery.PsnXpadReferProfitQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSetBonusMode.PsnXpadSetBonusModeResult;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadExpectYieldQuery.PsnXpadExpectYieldQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadExpectYieldQueryOutlay.PsnXpadExpectYieldQueryOutlayResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadProgressQueryOutlay.PsnXpadProgressQueryOutlayResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadSetBonusMode.psnXpadSetBonusModeResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadreferprofitdetailquery.PsnXpadReferProfitDetailQueryReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadreferprofitdetailquery.PsnXpadReferProfitDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadprogressquery.PsnXpadProgressQueryReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadprogressquery.PsnXpadProgressQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadreferprofitquery.PsnXpadReferProfitQueryReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadreferprofitquery.PsnXpadReferProfitQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseInputMode;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.ResultConvertUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的持仓-接口Result转换界面Model/Model转换数据
 * Created by cff on 2016/9/14.
 */
public class FinancialPositionCodeModeUtil {
    /**
     * I42-4.12 012  请求当前日期时间  PsnCommonQuerySystemDateTime  请求Model
     * Created by cff on 2016/9/14.
     */
    public static PsnCommonQuerySystemDateTimeParams buildPsnCommonQuerySystemDateTimeParams() {
        PsnCommonQuerySystemDateTimeParams mParams = new PsnCommonQuerySystemDateTimeParams();
        return mParams;
    }


    /**
     * 第一节	持仓管理-持仓列表页 PsnInvestmentManageIsOpen  转换请求Model
     * Created by cff on 2016/9/14.
     * 请求参数为null,响应结果为boolean值
     */
    public static PsnInvestmentManageIsOpenParams buildPsnInvestmentManageIsOpenParams() {
        PsnInvestmentManageIsOpenParams mParams = new PsnInvestmentManageIsOpenParams();
        return mParams;
    }

    /**
     * 第一节	持仓管理-持仓列表页 PsnInvestmentManageIsOpen  转换请求Model
     * Created by cff on 2016/9/14.
     * 请求参数为null,响应结果为boolean值
     */
    public static boolean transverterPsnInvestmentManageIsOpen(boolean isTrue) {
        return isTrue;
    }

    /**
     * 查询客户理财账户信息
     * Created by cff on 2016/9/14
     */
    public static PsnXpadAccountQueryParams buildPsnXpadAccountQueryParams(PsnXpadAccountQueryReqModel reqModel) {
        PsnXpadAccountQueryParams mParams = new PsnXpadAccountQueryParams();
        mParams.setQueryType(reqModel.getQueryType());
        mParams.setXpadAccountSatus(reqModel.getXpadAccountSatus());
        return mParams;
    }

    /**
     * 查询客户理财账户信息  响应model
     * Created by cff on 2016/9/14
     */
    public static PsnXpadAccountQueryResModel transverterPsnXpadAccountQueryResModel(PsnXpadAccountQueryResult mResult) {
        PsnXpadAccountQueryResModel mViewModel = new PsnXpadAccountQueryResModel();
        List<PsnXpadAccountQueryResModel.XPadAccountEntity> list = new ArrayList<>();
        for (int i = 0; i < mResult.getList().size(); i++) {
            PsnXpadAccountQueryResModel.XPadAccountEntity entity = new PsnXpadAccountQueryResModel.XPadAccountEntity();
            entity.setXpadAccountSatus(mResult.getList().get(i).getXpadAccountSatus());
            entity.setXpadAccount(mResult.getList().get(i).getXpadAccount());
            entity.setAccountId(mResult.getList().get(i).getAccountId());
            entity.setAccountKey(mResult.getList().get(i).getAccountKey());
            entity.setAccountNo(mResult.getList().get(i).getAccountNo());
            entity.setAccountType(mResult.getList().get(i).getAccountType());
            entity.setBancID(mResult.getList().get(i).getBancID());
            entity.setIbkNumber(mResult.getList().get(i).getIbkNumber());
            list.add(entity);
        }
        mViewModel.setList(list);
        return mViewModel;
    }

    /**
     * PsnXpadProductBalanceQuery  请求Model
     * Created by cff on 2016/9/14.
     */
    public static PsnXpadProductBalanceQueryParams buildPsnXpadAccountQueryParams(PsnXpadProductBalanceQueryReqModel reqModel) {
        PsnXpadProductBalanceQueryParams mParams = new PsnXpadProductBalanceQueryParams();
        mParams.setAccountKey(reqModel.getAccountKey());
        mParams.setIssueType(reqModel.getIssueType());
        return mParams;
    }

    /**
     * I42-4.36 036查询客户持仓信息 PsnXpadProductBalanceQuery
     * PsnXpadProductBalanceQuery  响应Model
     * Created by cff on 2016-9-14.
     */
    public static List<PsnXpadProductBalanceQueryResModel> transverterPsnXpadProductBalanceQueryResModel(List<PsnXpadProductBalanceQueryResult> mListModel) {

        List<PsnXpadProductBalanceQueryResModel> listModel = new ArrayList<PsnXpadProductBalanceQueryResModel>();
        for (int i = 0; i < mListModel.size(); i++) {
            PsnXpadProductBalanceQueryResModel mViewModel = new PsnXpadProductBalanceQueryResModel();

            mViewModel.setSellPrice(mListModel.get(i).getSellPrice());
            mViewModel.setXpadAccount(mListModel.get(i).getXpadAccount());
            mViewModel.setBancAccount(mListModel.get(i).getBancAccount());
            mViewModel.setProdCode(mListModel.get(i).getProdCode());
            mViewModel.setProdName(mListModel.get(i).getProdName());
            mViewModel.setCurCode(mListModel.get(i).getCurCode());
            mViewModel.setYearlyRR(mListModel.get(i).getYearlyRR());
            mViewModel.setProdBegin(mListModel.get(i).getProdBegin());
            mViewModel.setProdEnd(mListModel.get(i).getProdEnd());
            mViewModel.setHoldingQuantity(mListModel.get(i).getHoldingQuantity());
            mViewModel.setAvailableQuantity(mListModel.get(i).getAvailableQuantity());
            mViewModel.setCanRedeem(mListModel.get(i).getCanRedeem());
            mViewModel.setCanPartlyRedeem(mListModel.get(i).getCanPartlyRedeem());
            mViewModel.setCanChangeBonusMode(mListModel.get(i).getCanChangeBonusMode());
            mViewModel.setCurrentBonusMode(mListModel.get(i).getCurrentBonusMode());
            mViewModel.setLowestHoldQuantity(mListModel.get(i).getLowestHoldQuantity());
            mViewModel.setRedeemStartingAmount(mListModel.get(i).getRedeemStartingAmount());
            mViewModel.setCashRemit(mListModel.get(i).getCashRemit());
            mViewModel.setProgressionflag(mListModel.get(i).getProgressionflag());
            mViewModel.setBancAccountKey(mListModel.get(i).getBancAccountKey());
            mViewModel.setProductKind(mListModel.get(i).getProductKind());
            mViewModel.setExpProfit(mListModel.get(i).getExpProfit());
            mViewModel.setPrice(mListModel.get(i).getPrice());
            mViewModel.setPriceDate(mListModel.get(i).getPriceDate());
            mViewModel.setExpAmt(mListModel.get(i).getExpAmt());
            mViewModel.setTermType(mListModel.get(i).getTermType());
            mViewModel.setCanAddBuy(mListModel.get(i).getCanAddBuy());
            mViewModel.setStandardPro(mListModel.get(i).getStandardPro());
            mViewModel.setCanAgreementMange(mListModel.get(i).getCanAgreementMange());
            mViewModel.setProductTerm(mListModel.get(i).getProductTerm());
            mViewModel.setCanAssignDate(mListModel.get(i).getCanAssignDate());
            mViewModel.setShareValue(mListModel.get(i).getShareValue());
            mViewModel.setCurrPeriod(mListModel.get(i).getCurrPeriod());
            mViewModel.setTotalPeriod(mListModel.get(i).getTotalPeriod());
            mViewModel.setCanQuantityExchange(mListModel.get(i).getCanQuantityExchange());
            mViewModel.setYearlyRRMax(mListModel.get(i).getYearlyRRMax());
            mViewModel.setTranSeq(mListModel.get(i).getTranSeq());
            mViewModel.setIssueType(mListModel.get(i).getIssueType());
            listModel.add(mViewModel);
        }
        return listModel;
    }

    /**
     * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery  请求Model
     * Created by cff on 2016/9/22.
     */
    public static PsnXpadReferProfitDetailQueryParams buildPsnXpadReferProfitDetailQuery(PsnXpadReferProfitDetailQueryReqModel reqModel) {
        PsnXpadReferProfitDetailQueryParams mParams = new PsnXpadReferProfitDetailQueryParams();
        mParams.setAccountKey(reqModel.getAccountKey());
        mParams.set_refresh(reqModel.get_refresh());
        mParams.setCashRemit(reqModel.getCashRemit());
        mParams.setCurrentIndex(reqModel.getCurrentIndex());
        mParams.setEndDate(reqModel.getEndDate());
        mParams.setPageSize(reqModel.getPageSize());
        mParams.setKind(reqModel.getKind());
        mParams.setProductCode(reqModel.getProductCode());
        mParams.setProgressionflag(reqModel.getProgressionflag());
        mParams.setAccountKey(reqModel.getAccountKey());
        mParams.setStartDate(reqModel.getStartDate());
        return mParams;
    }

    /**
     * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery  响应Model
     * Created by cff on 2016/9/22.
     */
    public static PsnXpadReferProfitDetailQueryResModel transverterPsnXpadReferProfitDetailQuery(PsnXpadReferProfitDetailQueryResult mResult) {
        PsnXpadReferProfitDetailQueryResModel mViewModel = new PsnXpadReferProfitDetailQueryResModel();
        mViewModel.setRecordNumber(mResult.getRecordNumber());
        List<PsnXpadReferProfitDetailQueryResModel.QueryModel> list = new ArrayList<>();
        for (int i = 0; i < mResult.getList().size(); i++) {
            PsnXpadReferProfitDetailQueryResModel.QueryModel model = new PsnXpadReferProfitDetailQueryResModel.QueryModel();
            model.setProname(mResult.getList().get(i).getProname());
            model.setPayprofit(mResult.getList().get(i).getPayprofit());
            model.setExtfield(mResult.getList().get(i).getExtfield());
            model.setIntsdate(mResult.getList().get(i).getIntsdate());
            model.setIntedate(mResult.getList().get(i).getIntedate());
            model.setPayflag(mResult.getList().get(i).getPayflag());
            model.setStartdate(mResult.getList().get(i).getStartdate());
            model.setBalunit(mResult.getList().get(i).getBalunit());
            model.setBaldays(mResult.getList().get(i).getBaldays());
            model.setExyield(mResult.getList().get(i).getExyield());
            model.setNextdays(mResult.getList().get(i).getNextdays());
            list.add(model);
        }
        mViewModel.setList(list);
        return mViewModel;
    }

    /**
     * 收益总汇查询  请求Model
     *
     * @param reqModel
     * @return PsnXpadProductDetailQueryParams(bii层)
     */
    public static PsnXpadReferProfitQueryParams buildPsnXpadReferProfitQueryParams(PsnXpadReferProfitQueryReqModel reqModel) {
        PsnXpadReferProfitQueryParams mParams = new PsnXpadReferProfitQueryParams();
        mParams.setCharCode(reqModel.getCharCode());
        mParams.setAccountKey(reqModel.getAccountKey());
        mParams.setKind(reqModel.getKind());
        mParams.setProductCode(reqModel.getProductCode());
        mParams.setTranSeq(reqModel.getTranSeq());
        return mParams;
    }

    /**
     * 收益总汇查询 PsnXpadReferProfitQuery 响应Model
     *
     * @param mListModel
     * @return
     */
    public static PsnXpadReferProfitQueryResModel transverterPsnXpadReferProfitQueryResmodel(PsnXpadReferProfitQueryResult mListModel) {
        PsnXpadReferProfitQueryResModel mViewModel = new PsnXpadReferProfitQueryResModel();
        mViewModel.setProfitdate(mListModel.getProfitdate());
        mViewModel.setEdate(mListModel.getEdate());
        mViewModel.setProcur(mListModel.getProcur());
        mViewModel.setIntsdate(mListModel.getIntsdate());
        mViewModel.setIntedate(mListModel.getIntedate());
        mViewModel.setProgressionflag(mListModel.getProgressionflag());
        mViewModel.setKind(mListModel.getKind());
        mViewModel.setTermType(mListModel.getTermType());
        mViewModel.setProfit(mListModel.getProfit());
        mViewModel.setTotalprofit(mListModel.getTotalprofit());
        mViewModel.setUnpayprofit(mListModel.getUnpayprofit());
        mViewModel.setPayprofit(mListModel.getPayprofit());
        mViewModel.setPaydate(mListModel.getPaydate());
        mViewModel.setRedeemrule(mListModel.getRedeemrule());
        mViewModel.setRedpayamtmode(mListModel.getRedpayamtmode());
        mViewModel.setRedpayamountdate(mListModel.getRedpayamountdate());
        mViewModel.setRedpayprofitmode(mListModel.getRedpayprofitmode());
        mViewModel.setRedpayprofitdate(mListModel.getRedpayprofitdate());
        mViewModel.setExtfield(mListModel.getExtfield());
        mViewModel.setProfitdate(mListModel.getProfitdate());
        mViewModel.setTotalamt(mListModel.getTotalamt());
        mViewModel.setAmt(mListModel.getAmt());
        mViewModel.setBalamt(mListModel.getBalamt());
        return mViewModel;
    }

    /**
     * I42-4.40 040产品详情查询PsnXpadProductDetailQuery{请求model}
     *
     * @param ibknum
     * @param productKind
     * @param productCode
     * @return PsnXpadProductDetailQueryParams(bii层)
     */
    public static PsnXpadProductDetailQueryParams buildPsnXpadProductDetailQueryParams(String ibknum, String productKind, String productCode) {
        PsnXpadProductDetailQueryParams mParams = new PsnXpadProductDetailQueryParams();
        mParams.setIbknum(ibknum);
        mParams.setProductCode(productCode);
        mParams.setProductKind(productKind);
        return mParams;
    }

    /**
     * 42-4.40 040产品详情查询PsnXpadProductDetailQuery{响应model}
     *
     * @param mResult
     * @return PsnXpadProductDetailQueryResModel
     */
    public static PsnXpadProductDetailQueryResModel transverterPsnXpadProductDetailQueryResModel(PsnXpadProductDetailQueryResult mResult) {
        PsnXpadProductDetailQueryResModel mViewModel = new PsnXpadProductDetailQueryResModel();
        mViewModel.setSellAutoBanc(mResult.getSellAutoBanc());
        mViewModel.setPublishByPeple(mResult.getPublishByPeple());
        mViewModel.setPublishHomeBanc(mResult.getPublishHomeBanc());
        mViewModel.setRedPaymentMode(mResult.getRedPaymentMode());
        mViewModel.setAvailamt(mResult.getAvailamt());
        mViewModel.setYearlyRR(mResult.getYearlyRR());
        mViewModel.setProdEnd(mResult.getProdEnd());
        mViewModel.setCouponpayFreq(mResult.getCouponpayFreq());
        mViewModel.setProdTimeLimit(mResult.getProdTimeLimit());
        mViewModel.setPublishTelphone(mResult.getPublishTelphone());
        mViewModel.setProdBegin(mResult.getProdBegin());
        mViewModel.setBidPeriodStartDate(mResult.getBidPeriodStartDate());
        mViewModel.setPublishAutoBanc(mResult.getPublishAutoBanc());
        mViewModel.setLowLimitAmount(mResult.getLowLimitAmount());
        mViewModel.setProdName(mResult.getProdName());
        mViewModel.setPublishOnline(mResult.getPublishOnline());
        mViewModel.setProductTermType(mResult.getProductTermType());
        mViewModel.setSellHomeBanc(mResult.getSellHomeBanc());
        mViewModel.setRateDetail(mResult.getRateDetail());
        mViewModel.setAddAmount(mResult.getAddAmount());
        mViewModel.setProfitMode(mResult.getProfitMode());
        mViewModel.setAppdatered(mResult.getAppdatered());
        mViewModel.setBaseAmount(mResult.getBaseAmount());
        mViewModel.setProductKind(mResult.getProductKind());
        mViewModel.setSellingStartingDate(mResult.getSellingStartingDate());
        mViewModel.setIsSMS(mResult.getIsSMS());
        mViewModel.setProfitDate(mResult.getProfitDate());
        mViewModel.setInterestDate(mResult.getInterestDate());
        mViewModel.setIsLockPeriod(mResult.getIsLockPeriod());
        mViewModel.setStatus(mResult.getStatus());
        mViewModel.setDatesPaymentOffset(mResult.getDatesPaymentOffset());
        mViewModel.setOutTimeOrder(mResult.getOutTimeOrder());
        mViewModel.setCustLevelSale(mResult.getCustLevelSale());
        mViewModel.setPublishWeChat(mResult.getPublishWeChat());
        mViewModel.setProdRisklvl(mResult.getTransTypeCode());
        mViewModel.setBidStartDate(mResult.getBidStartDate());
        mViewModel.setSellType(mResult.getSellType());
        mViewModel.setSellingEndingDate(mResult.getSellingEndingDate());
        mViewModel.setIsBancs(mResult.getIsBancs());
        mViewModel.setRedPayDate(mResult.getRedPayDate());
        mViewModel.setSellTelByPeple(mResult.getSellTelByPeple());
        mViewModel.setSubAmount(mResult.getSubAmount());
        mViewModel.setLimitHoldBalance(mResult.getLimitHoldBalance());
        mViewModel.setRedEmperiodStart(mResult.getRedEmperiodStart());
        mViewModel.setBidPeriodEndDate(mResult.getBidPeriodEndDate());
        mViewModel.setSellMobile(mResult.getSellMobile());
        mViewModel.setStartTime(mResult.getStartTime());
        mViewModel.setOrderEndTime(mResult.getOrderEndTime());
        mViewModel.setCurCode(mResult.getCurCode());
        mViewModel.setProdRiskType(mResult.getProdRiskType());
        mViewModel.setRedEmptionEndDate(mResult.getRedEmptionEndDate());
        mViewModel.setProductType(mResult.getProductType());
        mViewModel.setBuyPrice(mResult.getBuyPrice());
        mViewModel.setIsCanCancle(mResult.getIsCanCancle());
        mViewModel.setIsRedask(mResult.getIsRedask());
        mViewModel.setSellTelphone(mResult.getSellTelphone());
        mViewModel.setBidEndDate(mResult.getBidEndDate());
        mViewModel.setRedEmptionStartDate(mResult.getRedEmptionStartDate());
        mViewModel.setRedEmperiodfReq(mResult.getRedEmperiodfReq());
        mViewModel.setRedEmptionHoliday(mResult.getRedEmptionHoliday());
        mViewModel.setSellOnline(mResult.getSellOnline());
        mViewModel.setRedPaymentDate(mResult.getRedPaymentDate());
        mViewModel.setBidHoliday(mResult.getBidHoliday());
        mViewModel.setRedEmperiodEnd(mResult.getRedEmperiodEnd());
        mViewModel.setPublishMobile(mResult.getPublishMobile());
        mViewModel.setMaxPeriod(mResult.getMaxPeriod());
        mViewModel.setProdCode(mResult.getProdCode());
        mViewModel.setBidPeriodMode(mResult.getBidPeriodMode());
        mViewModel.setDateModeType(mResult.getDateModeType());
        mViewModel.setPeriodical(mResult.getPeriodical());
        mViewModel.setSellWeChat(mResult.getSellWeChat());
        mViewModel.setBuyType(mResult.getBuyType());
        mViewModel.setEndTime(mResult.getEndTime());
        mViewModel.setPaymentDate(mResult.getPaymentDate());
        mViewModel.setApplyObj(mResult.getApplyObj());
        mViewModel.setTransTypeCode(mResult.getTransTypeCode());
        mViewModel.setProgressionflag(mResult.getProgressionflag());
        mViewModel.setOrderStartTime(mResult.getOrderStartTime());
        mViewModel.setRedaskDay(mResult.getRedaskDay());
        mViewModel.setFeeMode(mResult.getFeeMode());
        mViewModel.setCollectDistributeMode(mResult.getCollectDistributeMode());
        mViewModel.setPfmcDrawScale(mResult.getPfmcDrawScale());
        mViewModel.setPfmcDrawStart(mResult.getPfmcDrawStart());
        mViewModel.setFundOrderTime(mResult.getFundOrderTime());
        mViewModel.setChangeFeeMode(mResult.getChangeFeeMode());
        mViewModel.setChangePermit(mResult.getChangePermit());
        mViewModel.setBalexecDays(mResult.getBalexecDays());
        mViewModel.setChangeFromIssueid(mResult.getChangeFromIssueid());
        mViewModel.setSellNumMax(mResult.getSellNumMax());
        mViewModel.setOrdSingleMax(mResult.getOrdSingleMax());
        mViewModel.setOrdTotalMax(mResult.getOrdTotalMax());
        mViewModel.setDayPurchMax(mResult.getDayPurchMax());
        mViewModel.setDayPerPurchMax(mResult.getDayPerPurchMax());
        mViewModel.setOrdredSingleMax(mResult.getOrdredSingleMax());
        mViewModel.setOrdredTotalMax(mResult.getOrdredTotalMax());
        mViewModel.setPrice(mResult.getPrice());
        mViewModel.setPriceDate(mResult.getPriceDate());
        mViewModel.setSubscribeFee(mResult.getSubscribeFee());
        mViewModel.setPurchFee(mResult.getPurchFee());
        mViewModel.setRedeemFee(mResult.getRedeemFee());
        mViewModel.setPfmcdrawScale(mResult.getPfmcdrawScale());
        mViewModel.setPfmcdrawStart(mResult.getPfmcdrawStart());
        mViewModel.setStatus(mResult.getStatus());
        mViewModel.setLimitHoldBalance(mResult.getLimitHoldBalance());
        return mViewModel;
    }

    /**
     * 4.68 068份额明细查询 PsnXpadQuantityDetail{请求model}
     *
     * @param productCode
     * @param charCode
     * @return
     */
    public static PsnXpadQuantityDetailParams buildPsnXpadQuantityDetailParams(String productCode, String charCode) {
        PsnXpadQuantityDetailParams mParams = new PsnXpadQuantityDetailParams();
        mParams.setProductCode(productCode);
        mParams.setCharCode(charCode);
        return mParams;
    }

    /**
     * 4.68 068份额明细查询 PsnXpadQuantityDetail{返回model}
     *
     * @param mResult
     * @return PsnXpadQuantityDetailResult
     */
    public static PsnXpadQuantityDetailResModel transverterPsnXpadQuantityDetailResModel(
            PsnXpadQuantityDetailResult mResult) {
        PsnXpadQuantityDetailResModel mViewModel = new PsnXpadQuantityDetailResModel();
        mViewModel.setRecordNumber(mResult.getRecordNumber());
        List<PsnXpadQuantityDetailResModel.ListEntity> list = new ArrayList<>();
        LogUtils.i("mResult.getList() =" + mResult.getList());
        for (int i = 0; i < mResult.getList().size(); i++) {
            PsnXpadQuantityDetailResModel.ListEntity model = new PsnXpadQuantityDetailResModel.ListEntity();
            model.setSellPrice(mResult.getList().get(i).getSellPrice());
            model.setProdCode(mResult.getList().get(i).getProdCode());
            model.setCanChangeBonusMode(mResult.getList().get(i).getCanChangeBonusMode());
            model.setCurrentBonusMode(mResult.getList().get(i).getCurrentBonusMode());
            model.setProgressionflag(mResult.getList().get(i).getProgressionflag());
            model.setProductKind(mResult.getList().get(i).getProductKind());
            model.setPrice(mResult.getList().get(i).getPrice());
            model.setPriceDate(mResult.getList().get(i).getPriceDate());
            model.setExpAmt(mResult.getList().get(i).getExpAmt());
            model.setTermType(mResult.getList().get(i).getTermType());
            model.setCanAddBuy(mResult.getList().get(i).getCanAddBuy());
            model.setStandardPro(mResult.getList().get(i).getStandardPro());
            model.setCanAssignDate(mResult.getList().get(i).getCanAssignDate());
            model.setShareValue(mResult.getList().get(i).getShareValue());
            model.setYearlyRRMax(mResult.getList().get(i).getYearlyRRMax());
            model.setIssueType(mResult.getList().get(i).getIssueType());

            model.setXpadAccount(mResult.getList().get(i).getXpadAccount());
            model.setBancAccount(mResult.getList().get(i).getBancAccount());
            model.setProdName(mResult.getList().get(i).getProdName());
            model.setCurCode(mResult.getList().get(i).getCurCode());
            model.setYearlyRR(mResult.getList().get(i).getYearlyRR());
            model.setProdEnd(mResult.getList().get(i).getProdEnd());
            model.setHoldingQuantity(mResult.getList().get(i).getHoldingQuantity());
            model.setAvailableQuantity(mResult.getList().get(i).getAvailableQuantity());
            model.setCanRedeem(mResult.getList().get(i).getCanRedeem());
            model.setCanPartlyRedeem(mResult.getList().get(i).getCanPartlyRedeem());
            model.setLowestHoldQuantity(mResult.getList().get(i).getLowestHoldQuantity());
            model.setRedeemStartingAmount(mResult.getList().get(i).getRedeemStartingAmount());
            model.setCashRemit(mResult.getList().get(i).getCashRemit());
            model.setExpProfit(mResult.getList().get(i).getExpProfit());
            model.setCanQuantityExchange(mResult.getList().get(i).getCanQuantityExchange());
            model.setProductTerm(mResult.getList().get(i).getProductTerm());
            model.setCanAgreementMange(mResult.getList().get(i).getCanAgreementMange());
            model.setCurrPeriod(mResult.getList().get(i).getCurrPeriod());
            model.setTotalPeriod(mResult.getList().get(i).getTotalPeriod());
            model.setTranSeq(mResult.getList().get(i).getTranSeq());
            model.setProdBegin(mResult.getList().get(i).getProdBegin());
            model.setBancAccountKey(mResult.getList().get(i).getBancAccountKey());
            list.add(model);
        }
        mViewModel.setList(list);
        return mViewModel;
    }

    /**
     * 4.34 034累进产品收益率查询PsnXpadProgressQuery  请求Model
     *
     * @param reqModel
     * @return
     */
    public static PsnXpadProgressQueryParams buildPsnXpadProgressQueryParams(PsnXpadProgressQueryReqModel reqModel) {
        PsnXpadProgressQueryParams mParams = new PsnXpadProgressQueryParams();
//        mParams.setAccountId(reqModel.getAccountId());
        mParams.setAccountKey(reqModel.getAccountKey());
        mParams.setProductCode(reqModel.getProductCode());
        mParams.setCurrentIndex(reqModel.getCurrentIndex());
        mParams.setPageSize(reqModel.getPageSize());
        mParams.set_refresh(reqModel.get_refresh());
        mParams.setConversationId(reqModel.getConversationId());
        return mParams;
    }

    /**
     * 4.34 034累进产品收益率查询PsnXpadProgressQuery  响应Model
     *
     * @param mResult
     * @return
     */
    public static PsnXpadProgressQueryResModel transverterPsnXpadProgressQuery(PsnXpadProgressQueryResult mResult) {
        PsnXpadProgressQueryResModel mViewModel = new PsnXpadProgressQueryResModel();
        mViewModel.setRecordNumber(mResult.getRecordNumber());
        List<PsnXpadProgressQueryResModel.ListBean> list = new ArrayList<>();
        for (int i = 0; i < mResult.getList().size(); i++) {
            PsnXpadProgressQueryResModel.ListBean model = new PsnXpadProgressQueryResModel.ListBean();
            model.setProdCode(mResult.getList().get(i).getProdCode());
            model.setMinDays(mResult.getList().get(i).getMinDays());
            model.setMaxDays(mResult.getList().get(i).getMaxDays());
            model.setYearlyRR(mResult.getList().get(i).getYearlyRR());
            model.setPubrateDate(mResult.getList().get(i).getProdCode());
            model.setExcuteDate(mResult.getList().get(i).getExcuteDate());
            list.add(model);
        }
        mViewModel.setList(list);
        return mViewModel;
    }


    /**
     * 4.53 053 PsnXpadProgressQueryOutlay 登录前累进产品收益率查询  响应Model
     *
     * @param mResult
     * @return
     */
    public static PsnXpadProgressQueryOutlayResModel transverterPsnXpadProgressQueryOutlay(PsnXpadProgressQueryOutlayResult mResult) {
        PsnXpadProgressQueryOutlayResModel mViewModel = new PsnXpadProgressQueryOutlayResModel();
        mViewModel.setRecordNumber(mResult.getRecordNumber());
        List<PsnXpadProgressQueryOutlayResModel.ListBean> list = new ArrayList<>();
        for (int i = 0; i < mResult.getList().size(); i++) {
            PsnXpadProgressQueryOutlayResModel.ListBean model = new PsnXpadProgressQueryOutlayResModel.ListBean();
            model.setProdCode(mResult.getList().get(i).getProdCode());
            model.setMinDays(mResult.getList().get(i).getMinDays());
            model.setMaxDays(mResult.getList().get(i).getMaxDays());
            model.setYearlyRR(mResult.getList().get(i).getYearlyRR());
            model.setPubrateDate(mResult.getList().get(i).getProdCode());
            model.setExcuteDate(mResult.getList().get(i).getExcuteDate());
            list.add(model);
        }
        mViewModel.setList(list);
        return mViewModel;
    }


    /**
     * 产品购买数据组装 -列表使用
     *
     * @param mResult
     * @return
     * @author yx
     * @date 2016年10月31日 14:03:31
     */
    public static PurchaseInputMode buildPurchaseInputModeParams(PsnXpadProductDetailQueryResModel mResult, String dealCode) {

        PurchaseInputMode mViewModel = new PurchaseInputMode();
        //        2016年11月13日 17:29:24  yx  add  start
        mViewModel.couponpayFreq = mResult.getCouponpayFreq();// 付息频率
        mViewModel.interestDate = mResult.getInterestDate();// 收益到帐日
        mViewModel.purchFee = mResult.getPurchFee();
        mViewModel.creditBalance = mResult.getAvailamt();
        //                2016年11月13日 17:29:39  yx add end

        mViewModel.dealCode = dealCode;
        mViewModel.productKind = mResult.getProductKind();// 产品性质
        mViewModel.prodCode = mResult.getProdCode();
        mViewModel.prodName = mResult.getProdName();
        mViewModel.curCode = mResult.getCurCode();
        mViewModel.prodRiskType = mResult.getProdRiskType();
        mViewModel.isCanCancle = mResult.getIsCanCancle();// 认购/申购撤单设置
        mViewModel.transTypeCode = mResult.getTransTypeCode();// 购买交易类型
        mViewModel.subscribeFee = mResult.getSubscribeFee();
        mViewModel.purchFee = mResult.getPurchFee();// 申购手续费（净值）
        mViewModel.subAmount = mResult.getSubAmount();// 认购起点金额
        mViewModel.addAmount = mResult.getAddAmount();// 追加认申购起点金额
        mViewModel.baseAmount = mResult.getBaseAmount();
        mViewModel.buyAmount = mResult.getBuyAmount();
        mViewModel.appdatered = mResult.getAppdatered();// 是否允许指定日期赎回
        mViewModel.redEmptionStartDate = mResult.getRedEmptionStartDate();
        mViewModel.redEmptionEndDate = mResult.getRedEmptionEndDate();
        mViewModel.redEmptionHoliday = mResult.getRedEmptionHoliday();
        mViewModel.redEmperiodfReq = mResult.getRedEmperiodfReq();
        mViewModel.redEmperiodStart = mResult.getRedEmperiodStart();
        mViewModel.redEmperiodEnd = mResult.getRedEmperiodEnd();
        mViewModel.isLockPeriod = mResult.getIsLockPeriod();
        mViewModel.prodTimeLimit = mResult.getProdTimeLimit();
        mViewModel.periodPrice = mResult.getPrice();
        mViewModel.priceDate = mResult.getPriceDate();
        mViewModel.yearlyRR = mResult.getYearlyRR();
        mViewModel.rateDetail = mResult.getRateDetail();
        mViewModel.periodical = ResultConvertUtils.convertPeriodical(mResult.getPeriodical());
        return mViewModel;
    }

    /**
     * 产品购买数据组装-详情使用
     *
     * @param mResult
     * @param mItemXPadAccountEntity
     * @return
     * @author yx
     * @date 2016年10月31日 14:08:07
     */
    public static PurchaseInputMode buildPurchaseInputModeItemParams(PsnXpadProductDetailQueryResModel mResult, PsnXpadAccountQueryResModel.XPadAccountEntity mItemXPadAccountEntity) {
        PurchaseInputMode mViewModel = new PurchaseInputMode();
        mViewModel.payAccountNum = mItemXPadAccountEntity.getAccountNo();
        LogUtils.d("yx---------------------给产品购买传递的AccountNo-->" + mItemXPadAccountEntity.getAccountNo() + "");
        mViewModel.payAccountId = mItemXPadAccountEntity.getAccountId() + "";
        mViewModel.payAccountType = mItemXPadAccountEntity.getAccountType();
        mViewModel.payAccountStatus = mItemXPadAccountEntity.getXpadAccountSatus();
        mViewModel.payAccountBancID = mItemXPadAccountEntity.getBancID();
        LogUtils.d("yx---------------------给产品购买传递的accountID-->" + mItemXPadAccountEntity.getAccountId() + "");
        //        2016年11月13日 17:29:24  yx  add  start
        mViewModel.couponpayFreq = mResult.getCouponpayFreq();// 付息频率
        mViewModel.interestDate = mResult.getInterestDate();// 收益到帐日
        mViewModel.purchFee = mResult.getPurchFee();
        mViewModel.creditBalance = mResult.getAvailamt();
        mViewModel.accountKey = mItemXPadAccountEntity.getAccountKey();
        //                2016年11月13日 17:29:39  yx add end


        mViewModel.payAccountAmount = "";
        mViewModel.productKind = mResult.getProductKind();// 产品性质
        mViewModel.prodCode = mResult.getProdCode();
        mViewModel.prodName = mResult.getProdName();
        mViewModel.prodRiskType = mResult.getProdRiskType();
        mViewModel.curCode = mResult.getCurCode();
        mViewModel.isCanCancle = mResult.getIsCanCancle();// 认购/申购撤单设置
        mViewModel.transTypeCode = mResult.getTransTypeCode();// 购买交易类型
        mViewModel.subscribeFee = mResult.getSubscribeFee();
        mViewModel.purchFee = mResult.getPurchFee();// 申购手续费（净值）
        mViewModel.subAmount = mResult.getSubAmount();// 认购起点金额
        mViewModel.addAmount = mResult.getAddAmount();// 追加认申购起点金额
        mViewModel.baseAmount = mResult.getBaseAmount();
        mViewModel.buyAmount = mResult.getBuyAmount();
        mViewModel.appdatered = mResult.getAppdatered();// 是否允许指定日期赎回
        mViewModel.redEmptionStartDate = mResult.getRedEmptionStartDate();
        mViewModel.redEmptionEndDate = mResult.getRedEmptionEndDate();
        mViewModel.redEmptionHoliday = mResult.getRedEmptionHoliday();
        mViewModel.redEmperiodfReq = mResult.getRedEmperiodfReq();
        mViewModel.redEmperiodStart = mResult.getRedEmperiodStart();
        mViewModel.redEmperiodEnd = mResult.getRedEmperiodEnd();
        mViewModel.isLockPeriod = mResult.getIsLockPeriod();
        mViewModel.prodTimeLimit = mResult.getProdTimeLimit();
        mViewModel.periodical = ResultConvertUtils.convertPeriodical(mResult.getPeriodical());
        mViewModel.periodPrice = mResult.getPrice();
        mViewModel.priceDate = mResult.getPriceDate();
        mViewModel.yearlyRR = mResult.getYearlyRR();
        mViewModel.rateDetail = mResult.getRateDetail();
        return mViewModel;
    }


    /**
     * 持仓列表传递详情-详情使用单条model传递给继续购买
     *
     * @param mResult                      I42-4.36 036查询客户持仓信息 PsnXpadProductBalanceQuery
     * @param mPsnXpadAccountQueryResModel I42-4.37 037 查询客户理财账户信息PsnXpadAccountQuery -响应model
     * @return
     * @author yx
     * @date 2016年10月31日 14:25:15
     */
    public static PsnXpadAccountQueryResModel.XPadAccountEntity buildXPadAccountEntity(PsnXpadProductBalanceQueryResModel mResult, PsnXpadAccountQueryResModel mPsnXpadAccountQueryResModel) {
        PsnXpadAccountQueryResModel.XPadAccountEntity mXPadAccountEntity = null;
        if (mResult != null && mPsnXpadAccountQueryResModel != null) {
            for (int i = 0; i < mPsnXpadAccountQueryResModel.getList().size(); i++) {
                if ((mResult.getBancAccount() + "").equalsIgnoreCase(mPsnXpadAccountQueryResModel.getList().get(i).getAccountNo())) {
                    mXPadAccountEntity = mPsnXpadAccountQueryResModel.getList().get(i);
                    break;
                }
            }
        }
        return mXPadAccountEntity;
    }
    /**
     * 持仓列表传递详情-详情使用单条model传递给继续购买
     *
     * @param mResult                      I42-4.68
     * @param mPsnXpadAccountQueryResModel I42-4.37 037 查询客户理财账户信息PsnXpadAccountQuery -响应model
     * @return
     * @author yx
     * @date 2016-12-06 20:36:14
     */
    public static PsnXpadAccountQueryResModel.XPadAccountEntity buildXPadAccountEntity(PsnXpadQuantityDetailResModel.ListEntity mResult, PsnXpadAccountQueryResModel mPsnXpadAccountQueryResModel) {
        PsnXpadAccountQueryResModel.XPadAccountEntity mXPadAccountEntity = null;
        if (mResult != null && mPsnXpadAccountQueryResModel != null) {
            for (int i = 0; i < mPsnXpadAccountQueryResModel.getList().size(); i++) {
                if ((mResult.getBancAccount() + "").equalsIgnoreCase(mPsnXpadAccountQueryResModel.getList().get(i).getAccountNo())) {
                    mXPadAccountEntity = mPsnXpadAccountQueryResModel.getList().get(i);
                    break;
                }
            }
        }
        return mXPadAccountEntity;
    }

    /**
     * 4.72 072业绩基准产品预计年收益率查询PsnXpadExpectYieldQuery
     * @param mResult
     * @return
     */
    public static PsnXpadExpectYieldQueryResModel transverterPsnXpadExpectYieldQuery(PsnXpadExpectYieldQueryResult mResult) {
        PsnXpadExpectYieldQueryResModel mViewModel = new PsnXpadExpectYieldQueryResModel();
        mViewModel.setExYield(mResult.getExYield());
        mViewModel.setIsLowProfit(mResult.getIsLowProfit());
        mViewModel.setLastDate(mResult.getLastDate());
        mViewModel.setNextDate(mResult.getNextDate());
        mViewModel.setPostDate(mResult.getPostDate());
        mViewModel.setProdID(mResult.getProdID());
        mViewModel.setProdName(mResult.getProdName());
        List<PsnXpadExpectYieldQueryResModel.ListEntity> list = new ArrayList<>();
        LogUtils.i("list ===========>" + mResult.getList().toString());
        for (int i = 0; i < mResult.getList().size(); i++) {
            PsnXpadExpectYieldQueryResModel.ListEntity model = new PsnXpadExpectYieldQueryResModel.ListEntity();
            model.setCustLevel(mResult.getList().get(i).getCustLevel());
            model.setMaxAmt(mResult.getList().get(i).getMaxAmt());
            model.setMinAmt(mResult.getList().get(i).getMinAmt());
            model.setRates(mResult.getList().get(i).getRates());
            list.add(model);
        }
        mViewModel.setList(list);
        return mViewModel;
    }
    /**
     * 4.73 073登录前业绩基准产品预计年收益率查询  PsnXpadExpectYieldQueryOutlay
     * @param mResult
     * @return
     */
    public static PsnXpadExpectYieldQueryOutlayResModel transverterPsnXpadExpectYieldQueryOutlay(
            PsnXpadExpectYieldQueryOutlayResult mResult) {
        PsnXpadExpectYieldQueryOutlayResModel mViewModel = new PsnXpadExpectYieldQueryOutlayResModel();
        mViewModel.setExYield(mResult.getExYield());
        mViewModel.setIsLowProfit(mResult.getIsLowProfit());
        mViewModel.setLastDate(mResult.getLastDate());
        mViewModel.setNextDate(mResult.getNextDate());
        mViewModel.setPostDate(mResult.getPostDate());
        mViewModel.setProdID(mResult.getProdID());
        mViewModel.setProdName(mResult.getProdName());
        List<PsnXpadExpectYieldQueryOutlayResModel.ListEntity> list = new ArrayList<>();
        LogUtils.i("list ===========>" + mResult.getList().toString());
        for (int i = 0; i < mResult.getList().size(); i++) {
            PsnXpadExpectYieldQueryOutlayResModel.ListEntity model = new PsnXpadExpectYieldQueryOutlayResModel.ListEntity();
            model.setCustLevel(mResult.getList().get(i).getCustLevel());
            model.setMaxAmt(mResult.getList().get(i).getMaxAmt());
            model.setMinAmt(mResult.getList().get(i).getMinAmt());
            model.setRates(mResult.getList().get(i).getRates());
            list.add(model);
        }
        mViewModel.setList(list);
        return mViewModel;
    }
    /**
     * 4.14 014修改分红方式交易 PsnXpadSetBonusMode
     *
     * @param mResult
     * @return
     */
    public static psnXpadSetBonusModeResModel transverterPsnXpadSetBonusMode(PsnXpadSetBonusModeResult mResult) {
        psnXpadSetBonusModeResModel mViewModel = new psnXpadSetBonusModeResModel();
        mViewModel.setProdCode(mResult.getProdCode());
        mViewModel.setTranSeq(mResult.getTranSeq());
        mViewModel.setTrfAmount(mResult.getTrfAmount());
        mViewModel.setProdName(mResult.getProdName());
        mViewModel.setTrfPrice(mResult.getTrfPrice());
        mViewModel.setPaymentDate(mResult.getPaymentDate());
        mViewModel.setTransactionId(mResult.getTransactionId());
        return mViewModel;
    }
}
