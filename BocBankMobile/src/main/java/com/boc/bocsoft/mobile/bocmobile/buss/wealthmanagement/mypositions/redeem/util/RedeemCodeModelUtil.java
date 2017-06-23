package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.util;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductAndRedeem.PsnXpadHoldProductAndRedeemParms;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductAndRedeem.PsnXpadHoldProductAndRedeemResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductQueryList.PsnXpadHoldProductQueryListParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductQueryList.PsnXpadHoldProductQueryListResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductRedeemVerify.PsnXpadHoldProductRedeemVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductRedeemVerify.PsnXpadHoldProductRedeemVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountUpdate.PsnXpadRecentAccountUpdateParams;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadholdproductandredeem.PsnXpadHoldProductAndRedeemResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadholdproductquerylist.PsnXpadHoldProductQueryListResListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadholdproductquerylist.PsnXpadHoldProductQueryListResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadholdproductredeemverify.PsnXpadHoldProductRedeemVerifyResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口Result转换界面Model/Model转换数据
 * <p>
 * Created by yx on 2016/9/12.
 *
 * @date 2016-9-12 11:21:00
 */
public class RedeemCodeModelUtil {

    /**
     * I42-4.33 033持有产品赎回预交易  PsnXpadHoldProductRedeemVerify  请求数据  转换成bii层
     * Created by yx on 2016/9/27.
     *
     * @param mBalanceQueryResModel 客户信息
     * @param mDetailQueryResModel  view层请求model
     * @param isPartRedeemQuantity  是否允许部分部赎回 全部赎回时上送“0”
     * @param mSharesRedemption
     * @param isRedeemDate
     * @param mRedeemDate
     * @param mTranSeq              业绩基准 上送交易流水号
     * @return
     */
    public static PsnXpadHoldProductRedeemVerifyParams buildPsnXpadHoldProductRedeemVerifyBiiParams(PsnXpadProductBalanceQueryResModel mBalanceQueryResModel, PsnXpadProductDetailQueryResModel mDetailQueryResModel, boolean isPartRedeemQuantity, String mSharesRedemption, boolean isRedeemDate, String mRedeemDate, String mTranSeq) {
        PsnXpadHoldProductRedeemVerifyParams mParams = new PsnXpadHoldProductRedeemVerifyParams();
        mParams.setAccountKey("");//?cashRemit
        mParams.setProdCode(mDetailQueryResModel.getProdCode());
        mParams.setXpadCashRemit("" + mBalanceQueryResModel.getCashRemit());//?   00:人民币钞汇,01：钞,02：汇
        if (!isPartRedeemQuantity) {
            mParams.setRedeemQuantity("0");//
        } else {
            mParams.setRedeemQuantity(mSharesRedemption);//
        }
        mParams.setProductKind(mDetailQueryResModel.getProductKind());
        if (isRedeemDate) {
            mParams.setRedeemDate(mRedeemDate);
            LogUtil.d("yx-------req---指定日期--" + mRedeemDate);
        }
        mParams.setTranSeq("" + mTranSeq);//?
        return mParams;
    }


    /**
     * I42-4.33 033持有产品赎回预交易 PsnXpadHoldProductRedeemVerify-- 返回数据 -- 转换成View层Model
     *
     * @param mResult bii层model
     * @return view层model
     */
    public static PsnXpadHoldProductRedeemVerifyResModel transverterPsnXpadHoldProductRedeemVerifyViewModel(PsnXpadHoldProductRedeemVerifyResult mResult) {
        PsnXpadHoldProductRedeemVerifyResModel mViewModel = new PsnXpadHoldProductRedeemVerifyResModel();
        mViewModel.setProdCode(mResult.getProdCode());
        mViewModel.setProdName(mResult.getProdName());
        mViewModel.setCurrencyCode(mResult.getCurrencyCode());
        mViewModel.setPaymentDate(mResult.getPaymentDate());
        mViewModel.setSellPrice(mResult.getSellPrice());
        mViewModel.setRedeemQuantity(mResult.getRedeemQuantity());
        mViewModel.setRedeemAmount(mResult.getRedeemAmount());
        mViewModel.setTranflag(mResult.getTranflag());
        mViewModel.setTranSeq(mResult.getTranSeq());
        mViewModel.setRedeemDate(mResult.getRedeemDate());
        mViewModel.setCashRemit(mResult.getCashRemit());
        return mViewModel;
    }

    /**
     * **
     * I42-4.13 013持有产品赎回  PsnXpadHoldProductAndRedeem   转换请求Model
     * Created by cff on 2016-9-13
     *
     * @param token          防重标识
     * @param dealCode       指令交易后台交易ID   指令交易上送字段不可为空
     * @param conversationId 会话ID
     * @return
     */
    public static PsnXpadHoldProductAndRedeemParms buildPsnXpadHoldProductAndRedeemParms(String token, String dealCode, String conversationId) {
        PsnXpadHoldProductAndRedeemParms mParams = new PsnXpadHoldProductAndRedeemParms();
        mParams.setToken(token);
        mParams.setDealCode(dealCode);
        mParams.setConversationId(conversationId);
        return mParams;
    }

    /**
     * **
     * I42-4.13 013持有产品赎回  PsnXpadHoldProductAndRedeem   转换为响应Model
     * Created by cff on 2016/9/13.
     *
     * @param mResult
     * @return
     */
    public static PsnXpadHoldProductAndRedeemResModel transverterPsnXpadHoldProductAndRedeemResModel(PsnXpadHoldProductAndRedeemResult mResult) {
        PsnXpadHoldProductAndRedeemResModel mViewModel = new PsnXpadHoldProductAndRedeemResModel();
        mViewModel.setCurrencyCode(mResult.getCurrencyCode());
        mViewModel.setPaymentDate(mResult.getPaymentDate());
        mViewModel.setProdCode(mResult.getProdCode());
        mViewModel.setProdName(mResult.getProdName());
        mViewModel.setRedeemAmount(mResult.getRedeemAmount());
        mViewModel.setTranflag(mResult.getTranflag());
        mViewModel.setTransactionId(mResult.getTransactionId());
        mViewModel.setTranSeq(mResult.getTranSeq());
        mViewModel.setTrfAmount(mResult.getTrfAmount());
        mViewModel.setPaymentDate(mResult.getPaymentDate());
        mViewModel.setTransDate(mResult.getTransDate());
        return mViewModel;
    }

    /**
     * I42-4.12 012持有产品查询与赎回  PsnXpadHoldProductQueryList  请求Model
     * Created by cff on 2016/9/7.
     */
    public static PsnXpadHoldProductQueryListParams buildPsnXpadHoldProductAndRedeemParms() {
        PsnXpadHoldProductQueryListParams mParams = new PsnXpadHoldProductQueryListParams();
        return mParams;
    }

    /**
     * **
     * I42-4.12 012持有产品查询与赎回  PsnXpadHoldProductQueryList  响应Model
     * Created by cff on 2016/9/13.
     *
     * @param mResult
     * @return
     */
    public static PsnXpadHoldProductQueryListResModel transverterPsnXpadHoldProductAndRedeemResModel(List<PsnXpadHoldProductQueryListResult> mResult) {
        PsnXpadHoldProductQueryListResModel mViewModel = new PsnXpadHoldProductQueryListResModel();
        List<PsnXpadHoldProductQueryListResListModel> listModel = new ArrayList<PsnXpadHoldProductQueryListResListModel>();
        for (int i = 0; i < mResult.size(); i++) {
            PsnXpadHoldProductQueryListResListModel model = new PsnXpadHoldProductQueryListResListModel();
            PsnXpadHoldProductQueryListResult biimodel = mResult.get(i);
            model.setStatus(biimodel.getStatus());
            model.setCashRemit(biimodel.getCashRemit());
            model.setRemark(biimodel.getRemark());
            model.setBuyPrice(biimodel.getBuyPrice());
            model.setCurCode(biimodel.getCurCode());
            model.setProdCode(biimodel.getProdCode());
            model.setProdName(biimodel.getProdName());
            model.setProdBegin(biimodel.getProdBegin());
            model.setProdEnd(biimodel.getProdEnd());
            model.setProdRisklvl(biimodel.getProdRisklvl());
            model.setBrandId(biimodel.getBrandId());
            model.setSellPrice(biimodel.getSellPrice());
            model.setBrandName(biimodel.getBrandName());
            model.setYearlyRR(biimodel.getYearlyRR());
            model.setProdTimeLimit(biimodel.getProdTimeLimit());
            model.setApplyObj(biimodel.getApplyObj());
            model.setProductCat(biimodel.getProductCat());
            model.setBuyStartingAmount(biimodel.getBuyStartingAmount());
            model.setAppendStartingAmount(biimodel.getAppendStartingAmount());
            model.setSellingStartingDate(biimodel.getSellingStartingDate());
            model.setSellingEndingDate(biimodel.getSellingEndingDate());
            model.setPeriodical(biimodel.isPeriodical());
            model.setRemainCycleCount(biimodel.getRemainCycleCount());
            model.setHoldingQuantity(biimodel.getHoldingQuantity());
            model.setAvailableQuantity(biimodel.getAvailableQuantity());
            model.setCanRedeem(biimodel.isCanRedeem());
            model.setCanPartlyRedeem(biimodel.isCanPartlyRedeem());
            model.setCanChangeBonusMode(biimodel.isCanChangeBonusMode());
            model.setCurrentBonusMode(biimodel.getCurrentBonusMode());
            model.setLowestHoldQuantity(biimodel.getLowestHoldQuantity());
            model.setRedeemStartingAmount(biimodel.getRedeemStartingAmount());
            model.setPayProfit(biimodel.getPayProfit());
            model.setProgressionflag(biimodel.getProgressionflag());
        }
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
     * 生成修改最近操作理财账户
     *
     * @param conversationId
     * @param token
     * @param xpadAccountEntity @return
     */
    public static PsnXpadRecentAccountUpdateParams generateUpdateRecentAccountParams(String conversationId, String token, PsnXpadAccountQueryResModel.XPadAccountEntity xpadAccountEntity) {
        PsnXpadRecentAccountUpdateParams params = new PsnXpadRecentAccountUpdateParams();
        params.setAccountStatus(xpadAccountEntity.getAccountStatus());
        params.setXpadAccount(xpadAccountEntity.getXpadAccount());
        params.setAccountType(xpadAccountEntity.getAccountType());
        params.setBancID(xpadAccountEntity.getBancID());
        params.setCapitalActNoKey(xpadAccountEntity.getAccountKey());
//        setPublicParamsWithOutSecurity(params,conversationId,token);
        return params;
    }

    /**
     * 本金到帐规则
     *
     * @param redPaymentMode     本金返还方式
     *                           0：实时返还
     *                           1：T+N返还
     *                           2：期末返还
     * @param dateModeType       dateModeType	节假日调整方式
     *                           0：unAdjust
     *                           1：Following
     *                           2：Modify following
     *                           3：Forward
     *                           4：Modify forward
     * @param redPaymentDate     本金返还T+N(天数)
     * @param isLockPeriod       是否为业绩基准产品
     *                           0：非业绩基准产品
     *                           1：业绩基准-锁定期转低收益
     *                           2：业绩基准-锁定期后入账
     *                           3：业绩基准-锁定期周期滚续
     * @param paymentDate        本金到帐日
     * @param datesPaymentOffset 到期本金付款延迟天数
     * @param transDate          交易日期	String	交易实际将要执行的日期
     * @return
     */
    public static String convertRedPaymentMode(String redPaymentMode, String dateModeType, String redPaymentDate,
                                               String isLockPeriod, String paymentDate, String datesPaymentOffset, String transDate) {

        String mShowDate = DateUtils.getDateAfterDayCount(transDate, strToInt(redPaymentDate));
        String convertStr = "";
        if (StringUtils.isEmptyOrNull(redPaymentMode)) {
            return "";
        }
        if ("0".equals(redPaymentMode)) {// 实时返还
            convertStr = "T日赎回，本金实时到账";
        } else if ("1".equals(redPaymentMode)) {// T+N返还
            if (StringUtils.isEmptyOrNull(dateModeType))
                return "";
            if ("0".equals(dateModeType)) {
                convertStr = "预计" + mShowDate + "左右到账";
            } else if ("1".equals(dateModeType)) {
                convertStr = "预计" + mShowDate + "左右到账（若遇节假日顺延至下一工作日）";
            } else if ("2".equals(dateModeType)) {
                convertStr = "预计" + mShowDate + "左右到账（若遇节假日顺延至下一工作日，如顺延日期跨月，则在当月最后一个工作日到账）";
            } else if ("3".equals(dateModeType)) {
                convertStr = "预计" + mShowDate + "左右到账（若遇节假日自动向前调整）";
            } else if ("4".equals(dateModeType)) {
                convertStr = "预计" + mShowDate + "左右到账（若遇节假日自动向前调整，如顺延日期跨月，则在当月第一个工作日到账）";
            }
        } else if ("2".equals(redPaymentMode)) {// 期末返还
            if (isLockPeriod == null)
                return "";
            if ("1".equals(isLockPeriod)) {// 业绩基准-锁定期转低收益
                convertStr = "预计赎回后" + redPaymentDate + "日内到账";
            } else if ("2".equals(isLockPeriod) || "3".equals(isLockPeriod)) {// 业绩基准-锁定期后入账 业绩基准-锁定期周期滚续
                convertStr = "预计到期后" + datesPaymentOffset + "日内到账";
            } else {// 非业绩基准产品
                convertStr = "预计" + paymentDate + "左右到账";
            }
        }
        return convertStr;
    }

    /**
     * 赎回收益到账规则
     *
     * @param profitMode         收益返还方式
     *                           1：T+N返还
     *                           2：期末返还
     * @param dateModeType       dateModeType	节假日调整方式
     *                           0：unAdjust
     *                           1：Following
     *                           2：Modify following
     *                           3：Forward
     *                           4：Modify forward
     * @param profitDate         收益返还T+N(天数)
     * @param isLockPeriod       是否为业绩基准产品
     *                           0：非业绩基准产品
     *                           1：业绩基准-锁定期转低收益
     *                           2：业绩基准-锁定期后入账
     *                           3：业绩基准-锁定期周期滚续
     * @param redPayDate         赎回本金收益到账日
     * @param datesPaymentOffset 到期本金付款延迟天数
     * @param transDate          交易日期	String	交易实际将要执行的日期
     * @return
     */
//
    public static String convertProfitMode(String profitMode, String dateModeType, String profitDate,
                                           String isLockPeriod, String redPayDate, String datesPaymentOffset, String transDate) {
        String convertStr = "";
        String mShowDate = DateUtils.getDateAfterDayCount(transDate, strToInt(profitDate));
        if (StringUtils.isEmptyOrNull(profitMode)) {
            return "";
        }
        if ("1".equals(profitMode)) {// T+N返还
            if (StringUtils.isEmptyOrNull(dateModeType))
                return "";
            if ("0".equals(dateModeType)) {
                convertStr = "预计" + mShowDate + "左右到账";
            } else if ("1".equals(dateModeType)) {
                convertStr = "预计" + mShowDate + "左右到账（若遇节假日顺延至下一工作日）";
            } else if ("2".equals(dateModeType)) {
                convertStr = "预计" + mShowDate + "左右到账（若遇节假日顺延至下一工作日，如顺延日期跨月，则在当月最后一个工作日到账）";
            } else if ("3".equals(dateModeType)) {
                convertStr = "预计" + mShowDate + "左右日到账（若遇节假日自动向前调整）";
            } else if ("4".equals(dateModeType)) {
                convertStr = "预计" + mShowDate + "左右到账（若遇节假日自动向前调整，如顺延日期跨月，则在当月第一个工作日到账）";
            }
        } else if ("2".equals(profitMode)) {// 期末返还
            if (isLockPeriod == null)
                return "";
            if ("1".equals(isLockPeriod)) {// 业绩基准-锁定期转低收益
                convertStr = "预计赎回后" + profitDate + "日内到账";
            } else if ("2".equals(isLockPeriod) || "3".equals(isLockPeriod)) {// 业绩基准-锁定期后入账 业绩基准-锁定期周期滚续
                convertStr = "预计到期后" + datesPaymentOffset + "日内到账";
            } else {// 非业绩基准产品
                convertStr = "预计" + redPayDate + "左右到账";
            }
        }
        return convertStr;
    }

    /**
     * String 转换成 int 类型
     *
     * @param number
     * @return
     */
    private static int strToInt(String number) {
        if (!"".equals(number) && number != null) {
            return Integer.valueOf(number);
        }
        return 0;
    }

}
