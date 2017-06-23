package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBuyPre.PsnXpadProductBuyPreParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBuyPre.PsnXpadProductBuyPreResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBuyResult.PsnXpadProductBuyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBuyResult.PsnXpadProductBuyResultParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountUpdate.PsnXpadRecentAccountUpdateParams;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyang
 *         2016/11/8 09:56
 */
public class ModelUtil {

    /***************************** 公共模块 *****************************/
    /**
     * 设置请求公共参数,不包含安全因子
     *
     * @param params
     * @param conversationId
     * @param token
     */
    private static void setPublicParamsWithOutSecurity(PublicParams params, String conversationId, String token) {
        params.setConversationId(conversationId);
        params.setToken(token);
    }


    /***************************** 理财购买模块 *****************************/

    /**
     * 设置PurchaseModel,对应币种钞/汇余额
     *
     * @param purchaseModel
     * @param result
     * @return
     */
    public static PurchaseModel generatePurchaseModel(PurchaseModel purchaseModel, PsnAccountQueryAccountDetailResult result) {
        if (result == null)
            return purchaseModel;

        List<PsnAccountQueryAccountDetailResult.AccountDetaiListBean> list = result.getAccountDetaiList();
        for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean bean : list) {
            if (bean.getCurrencyCode().equals(purchaseModel.getCurCode())) {
                if (ApplicationConst.CURRENCY_CNY.equals(purchaseModel.getCurCode())) {
                    purchaseModel.setPayBalance(bean.getAvailableBalance());
                    continue;
                }

                if (bean.getCashRemit().equals(PurchaseModel.CODE_CASH))
                    purchaseModel.setPayBalanceCash(bean.getAvailableBalance());
                else
                    purchaseModel.setPayBalanceRemit(bean.getAvailableBalance());
            }
        }

        if (ApplicationConst.CURRENCY_CNY.equals(purchaseModel.getCurCode())) {
            purchaseModel.setCashRemitCode(PurchaseModel.CODE_CNY);
        } else if (purchaseModel.getPayBalanceRemit().compareTo(purchaseModel.getSubAmount()) >= 0) {
            purchaseModel.setCashRemitCode(PurchaseModel.CODE_REMIT);
            purchaseModel.setPayBalance(purchaseModel.getPayBalanceRemit());
        } else if (purchaseModel.getPayBalanceCash().compareTo(purchaseModel.getSubAmount()) >= 0) {
            purchaseModel.setCashRemitCode(PurchaseModel.CODE_CASH);
            purchaseModel.setPayBalance(purchaseModel.getPayBalanceCash());
        } else {
            purchaseModel.setCashRemitCode(PurchaseModel.CODE_REMIT);
            purchaseModel.setPayBalance(purchaseModel.getPayBalanceRemit());
        }

        return purchaseModel;
    }

    /**
     * 生成切换账户后账户信息至PurchaseModel
     *
     * @param model
     * @param accountBean
     * @param entity
     * @return
     */
    public static PurchaseModel generatePurchaseModel(PurchaseModel model, AccountBean accountBean, WealthAccountBean entity) {
        model.setPayAccountNum(accountBean.getAccountNumber());
        model.setPayAccountId(accountBean.getAccountId());
        model.setAccountKey(entity.getAccountKey());
        model.setPayAccountType(entity.getAccountType());
        model.setPayAccountBancID(entity.getBancID());
        model.setPayAccountStatus(entity.getXpadAccountSatus());
        return model;
    }

    /**
     * 设置交易Model,风险控制信息
     *
     * @param purchaseModel
     * @param result
     * @return
     */
    public static PurchaseModel generatePurchaseModel(PurchaseModel purchaseModel, PsnXpadQueryRiskMatchResult result) {
        if (result == null)
            return purchaseModel;

        purchaseModel.setRiskMatch(result.getRiskMatch());
        purchaseModel.setProductRisk(result.getProRisk());
        purchaseModel.setCustomerRisk(result.getCustRisk());
        purchaseModel.setRiskMessage(result.getRiskMsg());
        return purchaseModel;
    }

    /**
     * 生成预交易参数
     *
     * @param conversationId
     * @param token
     * @param purchaseModel
     * @return
     */
    public static PsnXpadProductBuyPreParam generateProductBuyPreParams(String conversationId, String token, PurchaseModel purchaseModel) {
        PsnXpadProductBuyPreParam param = new PsnXpadProductBuyPreParam();
        param.setProductName(purchaseModel.getProdName());
        param.setProductCode(purchaseModel.getProdCode());
        param.setCurCode(purchaseModel.getCurCode());
        if (ApplicationConst.currencyCodeNoPoint.contains(purchaseModel.getCurCode()))
            param.setBuyPrice(purchaseModel.getBuyAmount().setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString());
        else
            param.setBuyPrice(purchaseModel.getBuyAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        param.setMartCode(purchaseModel.getMartCode());
        param.setProductKind(purchaseModel.getProductKind());
        if (purchaseModel.isCanRedeem() && purchaseModel.getRedeemDate() != null)
            param.setRedDate(purchaseModel.getRedeemDate().format(DateFormatters.dateFormatter1));
        if (purchaseModel.isPeriodical())
            param.setIsAutoser("");
        if (purchaseModel.isPeriodProduct())
            param.setInvestCycle(purchaseModel.getPeriodNumber() + "");

        param.setAccountId(purchaseModel.getPayAccountId());
        param.setXpadCashRemit(purchaseModel.getCashRemitCode());

        setPublicParamsWithOutSecurity(param, conversationId, token);
        return param;
    }

    /**
     * 生成预交易结果
     *
     * @param purchaseModel
     * @param result
     * @return
     */
    public static PurchaseModel generatePurchaseModel(PurchaseModel purchaseModel, PsnXpadProductBuyPreResult result) {
        if (result == null)
            return purchaseModel;

        purchaseModel.setHasInvestXp(result.isHasInvestXp());
        purchaseModel.setOrderTime(result.getOrderTime());
        purchaseModel.setPurchFee(result.getTransFee());
        purchaseModel.setTrfPrice(result.getTrfPrice());
        return purchaseModel;
    }

    /**
     * 生成提交交易参数
     *
     * @param conversationId
     * @param token
     * @param purchaseModel
     * @return
     */
    public static PsnXpadProductBuyResultParam generateProductBuyParams(String conversationId, String token, PurchaseModel purchaseModel) {
        PsnXpadProductBuyResultParam param = new PsnXpadProductBuyResultParam();
        param.setProductName(purchaseModel.getProdName());
        param.setProductCode(purchaseModel.getProdCode());
        if (ApplicationConst.currencyCodeNoPoint.contains(purchaseModel.getCurCode()))
            param.setBuyPrice(purchaseModel.getBuyAmount().setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString());
        else
            param.setBuyPrice(purchaseModel.getBuyAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        param.setProductKind(purchaseModel.getProductKind());
        param.setCurrency(purchaseModel.getCurCode());
        param.setAccountId(purchaseModel.getPayAccountId());
        param.setXpadCashRemit(purchaseModel.getCashRemitCode());

        if (ApplicationConst.currencyCodeNoPoint.contains(purchaseModel.getCurCode()))
            param.setAmount(new BigDecimal(purchaseModel.getTrfPrice()).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString());
        else
            param.setAmount(new BigDecimal(purchaseModel.getTrfPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());

        param.setDealCode(purchaseModel.getDealCode());
        if (purchaseModel.isPeriodical())
            param.setIsAutoser("");

        setPublicParamsWithOutSecurity(param, conversationId, token);
        return param;
    }

    public static PurchaseModel generatePurchaseModel(PurchaseModel purchaseModel, PsnXpadProductBuyResult result) {
        if (result == null)
            return purchaseModel;

        purchaseModel.setTransNum(result.getTransactionId());
        return purchaseModel;
    }

    /**
     * @param list
     * @return
     */
    public static ArrayList<WealthAccountBean> generateWealthBeans(List<PsnXpadAccountQueryResModel.XPadAccountEntity> list) {
        ArrayList accountList = new ArrayList();
        if (list == null || list.isEmpty())
            return accountList;
        for (PsnXpadAccountQueryResModel.XPadAccountEntity entity : list) {
            WealthAccountBean wealthAccountBean = new WealthAccountBean();
            wealthAccountBean.setAccountType(entity.getAccountType());
            wealthAccountBean.setAccountId(entity.getAccountId());
            wealthAccountBean.setAccountNo(entity.getAccountNo());
            wealthAccountBean.setXpadAccount(entity.getXpadAccount());
            wealthAccountBean.setXpadAccountSatus(entity.getXpadAccountSatus());
            wealthAccountBean.setBancID(entity.getBancID());
            wealthAccountBean.setAccountKey(entity.getAccountKey());
            wealthAccountBean.setIbkNumber(entity.getIbkNumber());
            accountList.add(wealthAccountBean);
        }
        return accountList;
    }

    /**
     * 生成修改最近操作理财账户
     *
     * @param conversationId
     * @param token
     * @param purchaseModel  @return
     */
    public static PsnXpadRecentAccountUpdateParams generateUpdateRecentAccountParams(String conversationId, String token, PurchaseModel purchaseModel) {
        PsnXpadRecentAccountUpdateParams params = new PsnXpadRecentAccountUpdateParams();
        params.setAccountStatus(purchaseModel.getPayAccountStatus());
        params.setXpadAccount(purchaseModel.getPayAccountId());
        params.setAccountType(purchaseModel.getPayAccountType());
        params.setBancID(purchaseModel.getPayAccountBancID());
        params.setCapitalActNoKey(purchaseModel.getAccountKey());
        setPublicParamsWithOutSecurity(params, conversationId, token);
        return params;
    }

    /**
     * 生成猜你喜欢参数
     *
     * @param payAccountId
     * @param curCode
     * @param conversationId
     * @return
     */
    public static PsnXpadProductListQueryParams generateProductListQueryParams(String payAccountId, String curCode, String conversationId) {
        PsnXpadProductListQueryParams params = new PsnXpadProductListQueryParams();
        params.setConversationId(conversationId);
        params.setProductKind(WealthConst.PRODUCT_KIND_0);
        params.setAccountId(payAccountId);
        params.setProductRiskType(WealthConst.ALL_0);
        params.setPageSize(String.valueOf(ApplicationConst.WEALTH_PAGE_SIZE));
        params.setSortFlag(WealthConst.YES_1);// 排序方式
        params.setProductCurCode(curCode);
        params.setCurrentIndex(WealthConst.YES_0);
        params.setXpadStatus(WealthConst.YES_1);
        params.setSortType(WealthConst.YES_0);// 排序条件
        params.setIssueType(WealthConst.YES_0);// 产品类型
        params.setProRisk(WealthConst.YES_0);// 风险等级
        params.set_refresh("true");
        params.setIsLockPeriod(WealthConst.YES_1);// 是否支持业绩基准产品查询（支持）
        params.setProdTimeLimit(WealthConst.YES_0);// 产品期限（月）
        params.setDayTerm(WealthConst.YES_0);// 产品期限（天）
        return params;
    }

    /**
     * 生成猜你喜欢产品列表
     *
     * @param productCode
     * @param productRisk
     * @param result
     * @return
     */
    public static List<WealthListBean> generateWealthListBeans(String productCode, String productRisk, PsnXpadProductListQueryResult result) {
        if (result == null || result.getList() == null)
            return null;

        List<WealthListBean> list = new ArrayList<>();
        for (int i = 0; i < result.getList().size(); i++) {
            PsnXpadProductListQueryResult.ListBean resultItem = result.getList().get(i);

            //过滤掉传入ProductCode
            if (productCode.equals(resultItem.getProdCode()))
                continue;

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
            item.setProdRisklvl(resultItem.getProdRisklvl());// 风险等级
            item.setAvailableAmt(resultItem.getAvailableAmt());// 剩余额度
            item.setPrice(resultItem.getPrice());// 单位净值
            item.setIsProfiTest(resultItem.getIsProfitest());// 是否可以收益试算
            item.setRemainCycleCount(resultItem.getRemainCycleCount());
            list.add(item);
        }


        List<WealthListBean> resultList = new ArrayList<>();
        for (WealthListBean wealthListBean : list) {
            if (!productRisk.equals(wealthListBean.getProdRisklvl()))
                continue;

            if (WealthConst.YES_1.equals(wealthListBean.getIssueType()))
                continue;

            resultList.add(wealthListBean);
        }

        if (resultList.isEmpty())
            resultList = list;

        if (resultList.size() > 10)
            resultList = resultList.subList(0, 10);

        return resultList;
    }
}
