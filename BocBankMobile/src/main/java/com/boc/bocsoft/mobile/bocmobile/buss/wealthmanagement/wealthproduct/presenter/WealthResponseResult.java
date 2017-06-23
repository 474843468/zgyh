package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.presenter;

import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXadProductQueryOutlay.PsnXadProductQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXpadProductDetailQueryOutlay.PsnXpadProductDetailQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadNetHistoryQuery.PsnXpadNetHistoryQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadNetHistoryQueryOutlay.PsnXpadNetHistoryQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductInvestTreatyQuery.PsnXpadProductInvestTreatyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryResult;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.InvestTreatyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthHistoryBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装理财接口参数--响应
 * Created by liuweidong on 2016/10/24.
 */
public class WealthResponseResult {
    /**
     * 理财账户信息（登录后）
     *
     * @param result
     * @return
     */
    public static void copyResponseAccount(PsnXpadAccountQueryResult result) {
        WealthViewModel viewModel = WealthProductFragment.getInstance().getViewModel();
        List<WealthAccountBean> list = new ArrayList<>();
        for (int i = 0; i < result.getList().size(); i++) {
            PsnXpadAccountQueryResult.XPadAccountEntity resultItem = result.getList().get(i);
            WealthAccountBean item = new WealthAccountBean();
            item.setAccountNo(resultItem.getAccountNo());// 资金账号
            item.setBancID(resultItem.getBancID());// 账户开户行
            item.setXpadAccountSatus(resultItem.getXpadAccountSatus());// 账户状态
            item.setIbkNumber(resultItem.getIbkNumber());// 省行联行号
            item.setXpadAccount(resultItem.getXpadAccount());// 客户理财账户
            item.setAccountKey(resultItem.getAccountKey());// 账户缓存标识
            item.setAccountId(resultItem.getAccountId());// 账户ID 已关联进网银的账户返回，未关联进网银的为空
            item.setAccountType(resultItem.getAccountType());// 账户类型
            list.add(item);
        }
        viewModel.setAccountList(list);
    }

    /**
     * 查询理财产品列表（登录前）
     *
     * @param result
     * @return
     */
    public static WealthViewModel copyResultToViewModel(PsnXadProductQueryOutlayResult result) {
        WealthViewModel viewModel = new WealthViewModel();
        viewModel.setRecordNumber(result.getRecordNumber());// 总记录数
        List<WealthListBean> list = new ArrayList<>();
        for (int i = 0; i < result.getList().size(); i++) {
            PsnXadProductQueryOutlayResult.XPadProductBean resultItem = result.getList().get(i);
            WealthListBean item = new WealthListBean();
            item.setIssueType(resultItem.getIssueType());// 产品类型
            if ("2".equals(resultItem.getIssueType())) {
                item.setProductKind("1");
            } else {
                item.setProductKind("0");
            }
            item.setProdName(resultItem.getProdName());// 产品名称
            item.setProdCode(resultItem.getProdCode());// 产品代码
            item.setCurCode(resultItem.getCurCode());// 产品币种
            item.setYearlyRR(resultItem.getYearlyRR());// 预计年收益率%
            item.setRateDetail(resultItem.getRateDetail());// 预计年收益率（%）(最大值)
            item.setProdTimeLimit(resultItem.getProdTimeLimit());// 产品期限
            item.setIsLockPeriod(resultItem.getIsLockPeriod());// 是否为业绩基准产品
            item.setTermType(resultItem.getTermType());// 产品期限特性
            item.setSubAmount(resultItem.getSubAmount());// 起购金额
            item.setPrice(resultItem.getPrice());// 最新净值
            item.setStatus(resultItem.getStatus());// 产品销售状态
            item.setIsBuy(resultItem.getIsBuy());// 是否可购买
            item.setIsAgreement(resultItem.getIsAgreement());// 是否允许投资协议申请
            item.setImpawnPermit(resultItem.getImpawnPermit());// 是否允许组合购买
            item.setIsProfiTest(resultItem.getIsProfiTest());// 是否可以收益试算
            item.setRemainCycleCount(resultItem.getRemainCycleCount());
            list.add(item);
        }
        viewModel.setProductList(list);
        return viewModel;
    }

    /**
     * 查询理财产品列表（登录后）
     *
     * @param result
     * @return
     */
    public static WealthViewModel copyResultToViewModel(PsnXpadProductListQueryResult result) {
        WealthViewModel viewModel = new WealthViewModel();
        viewModel.setRecordNumber(result.getRecordNumber());
        List<WealthListBean> list = new ArrayList<>();
        for (int i = 0; i < result.getList().size(); i++) {
            PsnXpadProductListQueryResult.ListBean resultItem = result.getList().get(i);
            WealthListBean item = new WealthListBean();
            item.setIssueType(resultItem.getIssueType());// 产品类型
            item.setProdName(resultItem.getProdName());// 产品名称
            item.setProdCode(resultItem.getProdCode());// 产品代码
            item.setCurCode(resultItem.getCurCode());// 产品币种
            item.setSubPayAmt(resultItem.getSubPayAmt());// 购买起点金额
            item.setPeriedTime(resultItem.getPeriedTime());// 产品期限
            item.setIsLockPeriod(resultItem.getIsLockPeriod());// 是否为业绩基准产品
            item.setTermType(resultItem.getTermType());// 产品期限特性
            item.setYearlyRR(resultItem.getYearlyRR());// 预计年收益率%
            item.setRateDetail(resultItem.getRateDetail());// 预计年收益率（%）(最大值)
            item.setStatus(resultItem.getStatus());// 产品销售状态
            item.setIsBuy(resultItem.getIsBuy());// 是否可购买
            item.setIsAgreement(resultItem.getIsAgreement());// 是否允许投资协议申请
            item.setImpawnPermit(resultItem.getImpawnPermit());// 是否允许组合购买
            item.setProductKind(resultItem.getProductKind());// 产品性质
            item.setAvailableAmt(resultItem.getAvailableAmt());// 剩余额度
            item.setPrice(resultItem.getPrice());// 单位净值
            item.setIsProfiTest(resultItem.getIsProfitest());// 是否可以收益试算
            item.setRemainCycleCount(resultItem.getRemainCycleCount());
            list.add(item);
        }
        viewModel.setProductList(list);
        return viewModel;
    }

    /**
     * 理财产品详情（登录前）
     *
     * @param result
     * @return
     */
    public static WealthDetailsBean copyResultToViewModel(PsnXpadProductDetailQueryOutlayResult result) {
        WealthDetailsBean detailsBean = new WealthDetailsBean();
        detailsBean.setLoginBeforeI(true);
        detailsBean.setProductKind(result.getProductKind());// 产品性质
        detailsBean.setProductType(result.getProductType());// 产品类型
        detailsBean.setPeriodical(result.getPeriodical());// 是否周期性产品
        detailsBean.setProdCode(result.getProdCode());// 产品代码
        detailsBean.setProdName(result.getProdName());// 产品名称
        detailsBean.setCurCode(result.getCurCode());// 产品币种
        detailsBean.setStatus(result.getStatus());// 产品销售状态
        detailsBean.setProdRisklvl(result.getProdRisklvl());// 风险级别
        detailsBean.setProdRiskType(result.getProdRiskType());// 风险类别
        detailsBean.setApplyObj(result.getApplyObj());// 适用对象
        detailsBean.setProgressionflag(result.getProgressionflag());
        /**年化收益率*/
        detailsBean.setYearlyRR(result.getYearlyRR());// 预计年收益率（%）
        detailsBean.setRateDetail(result.getRateDetail());// 预计年收益率（%）(最大值)
        /**产品期限*/
        detailsBean.setProdTimeLimit(result.getProdTimeLimit());// 产品期限
        detailsBean.setProductTermType(result.getProductTermType());// 产品期限特性
        detailsBean.setSellingStartingDate(result.getSellingStartingDate());// 销售开始日期
        detailsBean.setSellingEndingDate(result.getSellingEndingDate());// 销售结束日期
        detailsBean.setProdBegin(result.getProdBegin());// 产品起息日
        detailsBean.setProdEnd(result.getProdEnd());// 产品到期日
        detailsBean.setStartTime(result.getStartTime());// 产品工作开始时间
        detailsBean.setEndTime(result.getEndTime());// 产品工作结束时间
        /**交易渠道*/
        detailsBean.setIsBancs(result.getIsBancs());// 是否允许柜台
        detailsBean.setIsSMS(result.getIsSMS());
        detailsBean.setSellOnline(result.getSellOnline());// 是否允许网上销售
        detailsBean.setSellMobile(result.getSellMobile());
        detailsBean.setSellHomeBanc(result.getSellHomeBanc());
        detailsBean.setSellAutoBanc(result.getSellAutoBanc());
        detailsBean.setSellTelphone(result.getSellTelphone());
        detailsBean.setSellTelByPeple(result.getSellTelByPeple());
        detailsBean.setOutTimeOrder(result.getOutTimeOrder());
        detailsBean.setSellWeChat(result.getSellWeChat());
        /**购买*/
        detailsBean.setBuyPrice(result.getBuyPrice());// 购买价格
        detailsBean.setSubAmount(result.getSubAmount());// 认购起点金额
        detailsBean.setAddAmount(result.getAddAmount());// 追加认申购起点金额
        detailsBean.setBuyType(result.getBuyType());// 购买开放规则
        detailsBean.setBidHoliday(result.getBidHoliday());// 允许节假日购买
        detailsBean.setBidStartDate(result.getBidStartDate());// 购买开始日期
        detailsBean.setBidEndDate(result.getBidEndDate());// 购买结束日期
        detailsBean.setBidPeriodMode(result.getBidPeriodMode());
        detailsBean.setBaseAmount(result.getBaseAmount());// 购买基数
        detailsBean.setIsCanCancle(result.getIsCanCancle());// 认购/申购撤单设置
        detailsBean.setBidPeriodStartDate(result.getBidPeriodStartDate());
        detailsBean.setBidPeriodEndDate(result.getBidPeriodEndDate());
        detailsBean.setOrderStartTime(result.getOrderStartTime());
        detailsBean.setOrderEndTime(result.getOrderEndTime());
        /**赎回*/
        detailsBean.setLowLimitAmount(result.getLowLimitAmount());// 赎回起点份额
        detailsBean.setLimitHoldBalance(result.getLimitHoldBalance());// 最低持有份额
        detailsBean.setSellType(result.getSellType());// 赎回开放规则
        detailsBean.setRedEmptionHoliday(result.getRedEmptionHoliday());
        detailsBean.setRedEmperiodfReq(result.getRedEmperiodfReq());
        detailsBean.setRedEmperiodStart(result.getRedEmperiodStart());
        detailsBean.setRedEmperiodEnd(result.getRedEmperiodEnd());
        detailsBean.setRedEmptionStartDate(result.getRedEmptionStartDate());// 赎回开始日期
        detailsBean.setRedEmptionEndDate(result.getRedEmptionEndDate());// 赎回结束日期
        detailsBean.setDateModeType(result.getDateModeType());// 节假日调整方式
        detailsBean.setRedPaymentMode(result.getRedPaymentMode());// 本金返还方式
        detailsBean.setRedPaymentDate(result.getRedPaymentDate());// 本金返还T+N(天数)
        detailsBean.setPaymentDate(result.getPaymentDate());// 本金到账日
        detailsBean.setCouponpayFreq(result.getCouponpayFreq());// 付息频率
        detailsBean.setInterestDate(result.getInterestDate());// 收益到账日
        detailsBean.setProfitMode(result.getProfitMode());// 收益返还方式
        detailsBean.setProfitDate(result.getProfitDate());// 收益返还T+N(天数)
        detailsBean.setRedPayDate(result.getRedPayDate());// 赎回本金收益到账日

        detailsBean.setCustLevelSale(result.getCustLevelSale());

        /**类基金理财产品（净值）*/
        if (WealthConst.PRODUCT_KIND_1.equals(result.getProductKind())) {// 类基金
            detailsBean.setPrice(result.getPrice());// 单位净值
            detailsBean.setPriceDate(result.getPriceDate());// 净值日期
            detailsBean.setSubscribeFee(result.getSubscribeFee());// 认购手续费
            detailsBean.setPurchFee(result.getPurchFee());// 申购手续费
            detailsBean.setRedeemFee(result.getRedeemFee());// 赎回手续费
            detailsBean.setFundOrderTime(result.getFundOrderTime());// 挂单时间
            detailsBean.setPfmcdrawStart(result.getPfmcdrawStart());// 业绩费提取起点收益率
            detailsBean.setPfmcdrawScale(result.getPfmcdrawScale());// 业绩费提取比例
        } else {// 结构性
            detailsBean.setIsLockPeriod(result.getIsLockPeriod());// 是否为业绩基准产品
            detailsBean.setMaxPeriod(result.getMaxPeriod());
        }
        return detailsBean;
    }

    /**
     * 产品详情（登录后）
     *
     * @param result
     * @return
     */
    public static WealthDetailsBean copyResultToViewModel(PsnXpadProductDetailQueryResult result) {
        WealthDetailsBean detailsBean = new WealthDetailsBean();
        detailsBean.setLoginBeforeI(false);
        detailsBean.setProductKind(result.getProductKind());// 产品性质
        detailsBean.setProductType(result.getProductType());// 产品类型
        detailsBean.setPeriodical(result.getPeriodical());// 是否周期性产品
        detailsBean.setProdCode(result.getProdCode());// 产品代码
        detailsBean.setProdName(result.getProdName());// 产品名称
        detailsBean.setCurCode(result.getCurCode());// 产品币种
        detailsBean.setStatus(result.getStatus());// 产品销售状态
        detailsBean.setProdRisklvl(result.getProdRisklvl());// 风险级别
        detailsBean.setProdRiskType(result.getProdRiskType());// 风险类别
        detailsBean.setApplyObj(result.getApplyObj());// 适用对象
        detailsBean.setProgressionflag(result.getProgressionflag());
        /**年化收益率*/
        detailsBean.setYearlyRR(result.getYearlyRR());// 预计年收益率（%）
        detailsBean.setRateDetail(result.getRateDetail());// 预计年收益率（%）(最大值)
        /**产品期限*/
        detailsBean.setProdTimeLimit(result.getProdTimeLimit());// 产品期限
        detailsBean.setProductTermType(result.getProductTermType());// 产品期限特性
        detailsBean.setSellingStartingDate(result.getSellingStartingDate());// 销售开始日期
        detailsBean.setSellingEndingDate(result.getSellingEndingDate());// 销售结束日期
        detailsBean.setProdBegin(result.getProdBegin());// 产品起息日
        detailsBean.setProdEnd(result.getProdEnd());// 产品到期日
        detailsBean.setStartTime(result.getStartTime());// 产品工作开始时间
        detailsBean.setEndTime(result.getEndTime());// 产品工作结束时间
        /**交易渠道*/
        detailsBean.setIsBancs(result.getIsBancs());// 是否允许柜台
        detailsBean.setIsSMS(result.getIsSMS());
        detailsBean.setSellOnline(result.getSellOnline());// 是否允许网上销售
        detailsBean.setSellMobile(result.getSellMobile());
        detailsBean.setSellHomeBanc(result.getSellHomeBanc());
        detailsBean.setSellAutoBanc(result.getSellAutoBanc());
        detailsBean.setSellTelphone(result.getSellTelphone());
        detailsBean.setSellTelByPeple(result.getSellTelByPeple());
        detailsBean.setOutTimeOrder(result.getOutTimeOrder());
        detailsBean.setSellWeChat(result.getSellWeChat());
        /**购买*/
        detailsBean.setBuyPrice(result.getBuyPrice());// 购买价格
        detailsBean.setSubAmount(result.getSubAmount());// 认购起点金额
        detailsBean.setAddAmount(result.getAddAmount());// 追加认申购起点金额
        detailsBean.setBuyType(result.getBuyType());// 购买开放规则
        detailsBean.setBidHoliday(result.getBidHoliday());// 允许节假日购买
        detailsBean.setBidStartDate(result.getBidStartDate());// 购买开始日期
        detailsBean.setBidEndDate(result.getBidEndDate());// 购买结束日期
        detailsBean.setBidPeriodMode(result.getBidPeriodMode());
        detailsBean.setBidPeriodStartDate(result.getBidPeriodStartDate());
        detailsBean.setBidPeriodEndDate(result.getBidPeriodEndDate());
        detailsBean.setAvailamt(String.valueOf(result.getAvailamt()));// 剩余额度
        detailsBean.setBaseAmount(result.getBaseAmount());// 购买基数
        detailsBean.setIsCanCancle(result.getIsCanCancle());// 认购/申购撤单设置
        detailsBean.setTransTypeCode(result.getTransTypeCode());
        detailsBean.setOrderStartTime(result.getOrderStartTime());
        detailsBean.setOrderEndTime(result.getOrderEndTime());
        /**赎回*/
        detailsBean.setLowLimitAmount(result.getLowLimitAmount());// 赎回起点份额
        detailsBean.setLimitHoldBalance(result.getLimitHoldBalance());// 最低持有份额
        detailsBean.setSellType(result.getSellType());// 赎回开放规则
        detailsBean.setRedEmptionHoliday(result.getRedEmptionHoliday());
        detailsBean.setRedEmperiodfReq(result.getRedEmperiodfReq());
        detailsBean.setRedEmperiodStart(result.getRedEmperiodStart());// 赎回周期开始
        detailsBean.setRedEmperiodEnd(result.getRedEmperiodEnd());// 赎回周期结束
        detailsBean.setRedEmptionStartDate(result.getRedEmptionStartDate());// 赎回开始日期
        detailsBean.setRedEmptionEndDate(result.getRedEmptionEndDate());// 赎回结束日期

        detailsBean.setCustLevelSale(result.getCustLevelSale());

        detailsBean.setAppdatered(result.getAppdatered());// 是否允许指定日期赎回
        detailsBean.setDateModeType(result.getDateModeType());// 节假日调整方式
        detailsBean.setRedPaymentMode(result.getRedPaymentMode());// 本金返还方式
        detailsBean.setRedPaymentDate(result.getRedPaymentDate());// 本金返还T+N(天数)
        detailsBean.setPaymentDate(result.getPaymentDate());// 本金到账日
        detailsBean.setCouponpayFreq(result.getCouponpayFreq());// 付息频率
        detailsBean.setInterestDate(result.getInterestDate());// 收益到账日
        detailsBean.setProfitMode(result.getProfitMode());// 收益返还方式
        detailsBean.setProfitDate(result.getProfitDate());// 收益返还T+N(天数)
        detailsBean.setRedPayDate(result.getRedPayDate());// 赎回本金收益到账日
        /**类基金理财产品（净值）*/
        if (WealthConst.PRODUCT_KIND_1.equals(result.getProductKind())) {// 类基金
            detailsBean.setPrice(result.getPrice());// 单位净值
            detailsBean.setPriceDate(result.getPriceDate());// 净值日期
            detailsBean.setSubscribeFee(result.getSubscribeFee());// 认购手续费
            detailsBean.setPurchFee(result.getPurchFee());// 申购手续费
            detailsBean.setRedeemFee(result.getRedeemFee());// 赎回手续费
            detailsBean.setFundOrderTime(result.getFundOrderTime());// 挂单时间
            detailsBean.setPfmcdrawStart(result.getPfmcDrawStart());// 业绩费提取起点收益率
            detailsBean.setPfmcdrawScale(result.getPfmcDrawScale());// 业绩费提取比例
        } else {// 结构性
            detailsBean.setIsLockPeriod(result.getIsLockPeriod());// 是否为业绩基准产品
            detailsBean.setDatesPaymentOffset(result.getDatesPaymentOffset());
            detailsBean.setMaxPeriod(result.getMaxPeriod());
        }
        return detailsBean;
    }

    /**
     * 历史净值（登录前）
     *
     * @param result
     * @return
     */
    public static WealthHistoryBean copyResultToViewModel(PsnXpadNetHistoryQueryOutlayResult result) {
        WealthHistoryBean wealthHistoryBean = new WealthHistoryBean();
        wealthHistoryBean.setRecordNumber(result.getRecordNumber());
        wealthHistoryBean.setProductCode(result.getProductCode());
        wealthHistoryBean.setMinPrice(result.getMinPrice());
        wealthHistoryBean.setMaxPrice(result.getMaxPrice());
        List<WealthHistoryBean.Item> list = new ArrayList<>();
        for (PsnXpadNetHistoryQueryOutlayResult.ListBean item : result.getList()) {
            WealthHistoryBean.Item itemPrice = new WealthHistoryBean.Item();
            itemPrice.setNetValue(item.getNetValue());
            itemPrice.setValueDate(item.getValueDate());
            list.add(itemPrice);
        }
        wealthHistoryBean.setList(list);
        return wealthHistoryBean;
    }

    /**
     * 历史净值（登录后）
     *
     * @param result
     * @return
     */
    public static WealthHistoryBean copyResultToViewModel(PsnXpadNetHistoryQueryResult result) {
        WealthHistoryBean wealthHistoryBean = new WealthHistoryBean();
        wealthHistoryBean.setRecordNumber(result.getRecordNumber());
        wealthHistoryBean.setProductCode(result.getProductCode());
        wealthHistoryBean.setMinPrice(result.getMinPrice());
        wealthHistoryBean.setMaxPrice(result.getMaxPrice());
        List<WealthHistoryBean.Item> list = new ArrayList<>();
        for (PsnXpadNetHistoryQueryResult.ListBean item : result.getList()) {
            WealthHistoryBean.Item itemPrice = new WealthHistoryBean.Item();
            itemPrice.setNetValue(item.getNetValue());
            itemPrice.setValueDate(item.getValueDate());
            list.add(itemPrice);
        }
        wealthHistoryBean.setList(list);
        return wealthHistoryBean;
    }

    /**
     * 产品投资协议查询
     *
     * @param result
     */
    public static ProtocolModel copyResultToViewModel(PsnXpadProductInvestTreatyQueryResult result) {
        ProtocolModel viewModel = new ProtocolModel();
        viewModel.setRecordNumber(result.getRecordNumber() + "");
        List<InvestTreatyBean> list = new ArrayList<>();
        for (int i = 0; i < result.getList().size(); i++) {
            PsnXpadProductInvestTreatyQueryResult.ListBean resultItem = result.getList().get(i);
            InvestTreatyBean item = new InvestTreatyBean();
            item.setAgrCode(resultItem.getAgrCode());// 产品协议编号
            item.setAgrName(resultItem.getAgrName());// 产品协议名称
            item.setAgrType(resultItem.getAgrType());// 协议类型
            item.setProid(resultItem.getProid());// 产品ID
            item.setProNam(resultItem.getProNam());// 产品名称
            item.setInstType(resultItem.getInstType());// 投资方式
            item.setPeriodAgr(resultItem.getPeriodAgr());// 协议周期频率
            item.setPeriodPur(resultItem.getPeriodPur());// 剩余可购买期数频率
            item.setProCur(resultItem.getProCur());// 币种
            item.setPeriodBal(resultItem.getPeriodBal());// 份额持有天数
            item.setIsNeedPur(resultItem.getIsNeedPur());// 购买选项
            item.setIsNeedRed(resultItem.getIsNeedRed());// 赎回选项
            item.setKind(resultItem.getKind());// 产品性质
            list.add(item);
        }
        viewModel.setProtocolList(list);
        return viewModel;
    }
}
