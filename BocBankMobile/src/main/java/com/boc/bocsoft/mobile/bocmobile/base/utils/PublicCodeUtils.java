package com.boc.bocsoft.mobile.bocmobile.base.utils;

import android.content.Context;

import com.boc.bocsoft.mobile.common.utils.PublicUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.boc.bocsoft.mobile.framework.utils.ResUtils.loadProperties;
import static com.boc.bocsoft.mobile.framework.utils.ResUtils.loadPropertiesToMap;

/**
 * code转换工具类
 *
 * @author xdy
 */
public class PublicCodeUtils {

    // =====================================================
    private static Properties mCurrenyProperties;
    private static Properties mCurrenySymbolProperties;
    private static Properties mCurrenyLetterPro;
    private static Properties mFundTypeProperties;
    private static Properties mTransferTypeProperties;
    private static Properties mTransFerAttrProperties;
    private static Properties mTransFerChannelProperties;
    private static Properties mGoldCurrencyCodeProperties;
    private static Properties mCurrenyCodeProperties;//CNY-001形式
    private static Properties mCurrenyLetterCodeProperties;//001-CNY形式
    private static Properties mFessTransType;//结汇购汇交易类型 01结汇， 11购汇

    /**
     * 将货币代码转换成货币名称
     *
     * @param mContext     context
     * @param currencyCode 货币code 或缩写
     * @return 货币code
     */
    public static String getCurrency(Context mContext, String currencyCode) {
        if (mCurrenyProperties == null) {
            mCurrenyProperties = loadProperties(mContext, "code/currency_code");
        }
        return mCurrenyProperties.getProperty(currencyCode == null ? "" : currencyCode.trim(), "")
                .trim();
    }

    /**
     * 将货币代码转换成货币符号
     *
     * @param mContext
     * @param currencyCode
     * @return
     */
    public static String getCurrencySymbol(Context mContext, String currencyCode) {
        if (mCurrenySymbolProperties == null) {
            mCurrenySymbolProperties = loadProperties(mContext, "code/currency_symbol_code");
        }
        return mCurrenySymbolProperties.getProperty(currencyCode == null ? "" : currencyCode.trim(), "")
                .trim();
    }

    /**
     * 将货币字符转换成货币代码{例如：CNY---->001}
     *
     * @param mContext     context
     * @param currencyCode 缩写{CNy}
     * @return 货币code {001}
     * @date 2016-12-05 19:21:51
     * @author yx
     */
    public static String getCurrencyCode(Context mContext, String currencyCode) {
        if (mCurrenyCodeProperties == null) {
            mCurrenyCodeProperties = loadProperties(mContext, "code/currency_transverter");
        }
        return mCurrenyCodeProperties.getProperty(currencyCode == null ? "" : currencyCode.trim(), "")
                .trim();
    }

    /**
     * 将货币字符转换成货币代码{例如：001---->CNY}
     *
     * @param mContext     context
     * @param currencyCode {001}
     * @return 货币字母 {CNY}
     * @date 2016-12-07 17:32:40
     * @author yx
     */
    public static String getCurrencyLetterCode(Context mContext, String currencyCode) {
        if (mCurrenyLetterCodeProperties == null) {
            mCurrenyLetterCodeProperties = loadProperties(mContext, "code/currency_num_transfer_letter");
        }
        return mCurrenyLetterCodeProperties.getProperty(currencyCode == null ? "" : currencyCode.trim(), "")
                .trim();
    }

    /**
     * 将交易类型代码转换成交易类型名称（中银理财-委托常规交易状况查询-交易类型）
     *
     * @param mContext         context
     * @param transferTypeCode 交易类型code
     * @return 交易类型
     */
    public static String getTransferType(Context mContext, String transferTypeCode) {
        if (mTransferTypeProperties == null) {
            mTransferTypeProperties = loadProperties(mContext, "code/transfer_type_code");
        }
        return mTransferTypeProperties.getProperty(transferTypeCode == null ? "" : transferTypeCode.trim(), "")
                .trim();
    }

    /**
     * 将交易属性代码转换成交易属性名称（中银理财-委托常规交易状况查询-交易属性）
     *
     * @param mContext         context
     * @param transferAttrCode 交易属性code
     * @return 交易属性
     */
    public static String getTransferAttr(Context mContext, String transferAttrCode) {
        if (mTransFerAttrProperties == null) {
            mTransFerAttrProperties = loadProperties(mContext, "code/transfer_attr_code");
        }
        return mTransFerAttrProperties.getProperty(transferAttrCode == null ? "" : transferAttrCode.trim(), "")
                .trim();
    }

    /**
     * 将交易渠道代码转换成交易渠道名称（中银理财-组合购买-交易渠道）
     *
     * @param mContext            context
     * @param transferChannelCode 交易渠道code
     * @return 交易渠道
     */
    public static String getTransferChannel(Context mContext, String transferChannelCode) {
        if (mTransFerChannelProperties == null) {
            mTransFerChannelProperties = loadProperties(mContext, "code/transfer_channel_code");
        }
        return mTransFerChannelProperties.getProperty(transferChannelCode == null ? "" : transferChannelCode.trim(), "")
                .trim();
    }

    /**
     * 将货币英文货币名称互转
     *
     * @param mContext       context
     * @param currencyLetter 货币code 或缩写
     * @return 货币名称
     */
    public static String getCurrencyWithLetter(Context mContext, String currencyLetter) {
        if (mCurrenyLetterPro == null) {
            mCurrenyLetterPro = loadProperties(mContext, "code/currency_letter");
        }
        return mCurrenyLetterPro.getProperty(currencyLetter == null ? "" : currencyLetter.trim(), "")
                .trim();
    }

    /**
     * 将币种代码列表转变成币种名称列表(带钞汇标示)
     *
     * @param context           context
     * @param currencyCodeList  币种代码列表
     * @param cashRemitCodeList 币种代码列表
     * @return
     */
    public static List<String> getCurrencyStringList(final Context context,
                                                     List<String> currencyCodeList, List<String> cashRemitCodeList) {
        if (PublicUtils.isEmpty(currencyCodeList)) {
            return null;
        }
        List<String> result = new ArrayList<>();
        if (!PublicUtils.isEmpty(cashRemitCodeList)) {
            for (int i = 0; i < currencyCodeList.size(); i++) {
                String oneBean = getCurrency(context, currencyCodeList.get(i)) +
                        ("00".equals(cashRemitCodeList.get(i)) ? "" : ("01".equals(cashRemitCodeList.get(i)) ? "/钞" : "/汇"));
                result.add(oneBean);
            }
        } else {
            for (String currencyCode : currencyCodeList) {
                result.add(getCurrency(context, currencyCode));
            }
        }
        return result;
    }

    // ============================================================
    private static Properties mAccountTypeProperties;

    /**
     * 将账户类型code转换成对应字符串
     *
     * @param mContext        context
     * @param accountTypeCode 账户类型code
     * @return 账户类型转换后额值
     */
    public static String getAccountType(Context mContext, String accountTypeCode) {
        if (mAccountTypeProperties == null) {
            mAccountTypeProperties = loadProperties(mContext, "code/account_type_code");
        }
        return mAccountTypeProperties.getProperty(
                accountTypeCode == null ? "" : accountTypeCode.trim(), "").trim();
    }

    // ============================================================
    private static Properties mAccountPurposeProperties;

    /**
     * 将账户用途code转换成对应字符串
     *
     * @param mContext           context
     * @param accountPurposeCode 账户用途 code
     * @return 账户用途转换后值
     */
    public static String getAccountPurpose(Context mContext, String accountPurposeCode) {
        if (mAccountPurposeProperties == null) {
            mAccountPurposeProperties = loadProperties(mContext, "code/account_purpose_code");
        }

        return mAccountPurposeProperties.getProperty(
                accountPurposeCode == null ? "" : accountPurposeCode.trim(), "").trim();
    }

    public static Map<String, String> getAccountPurposeMap(Context mContext) {
        return loadPropertiesToMap(mContext, "code/account_purpose_code");
    }

    // ============================================================
    private static Properties mCardDescriptionProperties;

    /**
     * 将账户类型code转换成对应字符串
     *
     * @param mContext            context
     * @param cardDescriptionCode 卡片品种代码
     * @return 卡片品种
     */
    public static String getCardDescription(Context mContext, String cardDescriptionCode) {
        if (mCardDescriptionProperties == null) {
            mCardDescriptionProperties = loadProperties(mContext, "code/account_type_code");
        }
        return mCardDescriptionProperties.getProperty(
                cardDescriptionCode == null ? "" : cardDescriptionCode.trim(), "").trim();
    }

    // ============================================================
    private static Properties mgetPayerCustomer;

    /**
     * 将账户号code转换成对应字符串
     *
     * @param mContext          context
     * @param payerCustomerCode 付款人类型
     */
    public static String getPayerCustomer(Context mContext, String payerCustomerCode) {
        if (mgetPayerCustomer == null) {
            mgetPayerCustomer = loadProperties(mContext, "code/payer_customer_code");
        }
        return mgetPayerCustomer.getProperty(
                payerCustomerCode == null ? "" : payerCustomerCode.trim(), "").trim();
    }

    // ============================================================
    private static Properties mDepositReceiptProperties;

    /**
     * 将存单类型code转换成对应字符串
     *
     * @param mContext           context
     * @param depositReceiptCode 存单类型代码
     * @return 卡片品种
     */
    public static String getDepositReceiptType(Context mContext, String depositReceiptCode) {
        if (mDepositReceiptProperties == null) {
            mDepositReceiptProperties = loadProperties(mContext, "code/deposit_receipt_type_code");
        }
        return mDepositReceiptProperties.getProperty(
                depositReceiptCode == null ? "" : depositReceiptCode.trim(), "").trim();
    }

    // ============================================================
    private static Properties mTransferIbkProperties;

    /**
     * 将账户地区code转换成对应字符串
     *
     * @param mContext        context
     * @param transferIbkCode 地区代码
     * @return 地区
     */
    public static String getTransferIbk(Context mContext, String transferIbkCode) {
        if (mTransferIbkProperties == null) {
            mTransferIbkProperties = loadProperties(mContext, "code/transfer_ibk_code");
        }
        return mTransferIbkProperties.getProperty(
                transferIbkCode == null ? "" : transferIbkCode.trim(), "").trim();
    }

    public static Map<String, String> getTransferIbkMap(Context mContext) {
        return loadPropertiesToMap(mContext, "code/transfer_ibk_code");
    }

    // ============================================================
    private static Properties mLoanFacilityProperties;

    /**
     * 将账户类型code转换成对应字符串
     *
     * @param mContext         context
     * @param loanFacilityCode 贷款额度类型
     * @return 卡片品种
     */
    public static String getLoanFacility(Context mContext, String loanFacilityCode) {
        if (mLoanFacilityProperties == null) {
            mLoanFacilityProperties = loadProperties(mContext, "code/loan_facility_type_code");
        }
        return mLoanFacilityProperties.getProperty(
                loanFacilityCode == null ? "" : loanFacilityCode.trim(), "-").trim();
    }

    // ============================================================
    private static Properties mFacilityStatusProperties;

    /**
     * 将账户类型code转换成对应字符串
     *
     * @param mContext           context
     * @param facilityStatusCode 额度状态类型
     * @return 卡片品种
     */
    public static String getFacilityStatus(Context mContext, String facilityStatusCode) {
        if (mFacilityStatusProperties == null) {
            mFacilityStatusProperties = loadProperties(mContext, "code/facility_status_code");
        }
        return mFacilityStatusProperties.getProperty(
                facilityStatusCode == null ? "" : facilityStatusCode.trim(), "-").trim();
    }

    // ============================================================
    private static Properties mFacilityChildStatusCodeProperties;

    /**
     * 将子账户状态code转换成对应字符串
     *
     * @param mContext                context
     * @param facilityChildStatusCode 子账户状态
     * @return 状态
     */
    public static String getFacilityChildStatusCode(Context mContext,
                                                    String facilityChildStatusCode) {
        if (mFacilityChildStatusCodeProperties == null) {
            mFacilityChildStatusCodeProperties =
                    loadProperties(mContext, "code/facility_child_status_code");
        }
        return mFacilityChildStatusCodeProperties.getProperty(
                facilityChildStatusCode == null ? "" : facilityChildStatusCode.trim(), "").trim();
    }

    //=========================
    private static Properties mQuoteType;

    /**
     * 将签约code转换成对应的字符串
     *
     * @param mContext  context
     * @param QuoteType 额度状态类型
     * @return 签约类型
     */
    public static String getQuoteType(Context mContext, String QuoteType) {
        if (mQuoteType == null) {
            mQuoteType = loadProperties(mContext, "code/loan_quote_type_code");
        }
        return mQuoteType.getProperty(QuoteType == null ? "" : QuoteType.trim(), "-").trim();
    }

    // ============================================================
    private static Properties mCdPeriodProperties;

    /**
     * 将存期类型code转换成对应字符串
     *
     * @param mContext context
     * @param cdPeriod 存期
     * @return 存期
     */
    public static String getCdPeriod(Context mContext, String cdPeriod) {
        if (mCdPeriodProperties == null) {
            mCdPeriodProperties = loadProperties(mContext, "code/account_cdPeriod_code");
        }
        return mCdPeriodProperties.getProperty(cdPeriod == null ? "" : cdPeriod.trim(), "").trim();
    }

    // ============================================================
    private static Properties mCdPeriodDayProperties;

    /**
     * 将存期类型code转换成对应字符串
     *
     * @param mContext context
     * @param cdPeriod 存期
     * @return 存期
     */
    public static String getCdPeriodDay(Context mContext, String cdPeriod) {
        if (mCdPeriodDayProperties == null) {
            mCdPeriodDayProperties = loadProperties(mContext, "code/account_cdPeriod_day_code");
        }
        return mCdPeriodDayProperties.getProperty(cdPeriod == null ? "" : cdPeriod.trim(), "")
                .trim();
    }

    private static Properties mIdentityType;

    /**
     * 将签约code转换成对应的字符串
     *
     * @param mContext     context
     * @param identityType 额度状态类型
     * @return 签约类型
     */
    public static String getIdentityType(Context mContext, String identityType) {
        if (mIdentityType == null) {
            mIdentityType = loadProperties(mContext, "code/identity_type_code");
        }
        return mIdentityType.getProperty(identityType == null ? "" : identityType.trim(), "-")
                .trim();
    }

    /**
     * 将基金类型code转换成对应的字符串
     *
     * @param mContext     context
     * @param fundTypeCode 基金类型码
     * @return 基金类型
     */
    public static String getFundType(Context mContext, String fundTypeCode) {
        if (mFundTypeProperties == null) {
            mFundTypeProperties = loadProperties(mContext, "code/fund_type_code");
        }
        return mFundTypeProperties.getProperty(fundTypeCode == null ? "" : fundTypeCode.trim(), "-")
                .trim();
    }

    /**
     * 将贵金属类型code转换成对应的字符串
     *
     * @param mContext     context
     * @param currencyCode 基金类型码
     * @return 基金类型
     */
    public static String getGoldCurrencyCode(Context mContext, String currencyCode) {
        if (mGoldCurrencyCodeProperties == null) {
            mGoldCurrencyCodeProperties = loadProperties(mContext, "code/gold_currency_code");
        }
        return mGoldCurrencyCodeProperties.getProperty(currencyCode == null ? "" : currencyCode.trim(), "--")
                .trim();
    }

    /**
     * 贷款类型名称
     */
    private static Properties mloanType;

    /**
     * 贷款类型code转换为贷款名称
     *
     * @param mContext
     * @param loanTypeCode
     */
    public static String getLoanTypeName(Context mContext, String loanTypeCode) {
        if (mloanType == null) {
            mloanType = loadProperties(mContext, "code/loan_type_code");
        }
        return mloanType.getProperty(loanTypeCode == null ? "" : loanTypeCode.trim(), "")
                .trim();
    }

    /**
     * 账户类型字符串码accountType转换为相对应字符串
     * <p>
     * 101：普通活期    103：中银信用卡    104：长城信用卡    119：长城电子借记卡
     * 107：单外币信用卡    108：虚拟卡(贷记)    109：虚拟卡(准贷记)
     * 110：虚拟卡（借记卡）    140：存本取息    150：零存整取    152：教育储蓄
     * 170：定期一本通    188：活期一本通    190：网上专属理财账户    300：电子现金账户
     *
     * @param accounttype
     */
    public static String changeaccounttypetoName(String accounttype) {
        String accoutnName = null;
        if ("101".equals(accounttype)) {
            accoutnName = "普通活期";
        } else if ("103".equals(accounttype)) {
            accoutnName = "中银信用卡";
        } else if ("104".equals(accounttype)) {
            accoutnName = "长城信用卡";
        } else if ("119".equals(accounttype)) {
            accoutnName = "长城电子借记卡";
        } else if ("107".equals(accounttype)) {
            accoutnName = "单外币信用卡";
        } else if ("108".equals(accounttype)) {
            accoutnName = "虚拟卡(贷记)";
        } else if ("109".equals(accounttype)) {
            accoutnName = "虚拟卡(准贷记)";
        } else if ("110".equals(accounttype)) {
            accoutnName = "虚拟卡（借记卡）";
        } else if ("140".equals(accounttype)) {
            accoutnName = "存本取息";
        } else if ("150".equals(accounttype)) {
            accoutnName = "零存整取";
        } else if ("152".equals(accounttype)) {
            accoutnName = "教育储蓄";
        } else if ("170".equals(accounttype)) {
            accoutnName = "定期一本通";
        } else if ("188".equals(accounttype)) {
            accoutnName = "活期一本通";
        } else if ("190".equals(accounttype)) {
            accoutnName = "网上专属理财账户";
        } else if ("300".equals(accounttype)) {
            accoutnName = "电子现金账户";
        }
        return accoutnName;
    }

    /**
     * 贷款类型名称
     */
    private static Properties mCashSpot;

    /**
     * 结售汇现钞现汇
     *
     * @param mContext
     * @param cashSpot
     */
    public static String getCashSpot(Context mContext, String cashSpot) {
        if (mCashSpot == null) {
            mCashSpot = loadProperties(mContext, "code/cashspot_type");
        }
        return mCashSpot.getProperty(cashSpot == null ? "" : cashSpot.trim(), "")
                .trim();
    }

    private static Properties mBuyBankrollUse;

    /**
     * 购汇资金用途
     *
     * @param mContext
     * @param bankrollUse
     */
    public static String getBankrollUse(Context mContext, String bankrollUse) {
        if (mBuyBankrollUse == null) {
            mBuyBankrollUse = loadProperties(mContext, "code/fess_bankroll_use");
        }
        return mBuyBankrollUse.getProperty(bankrollUse == null ? "" : bankrollUse.trim(), "")
                .trim();
    }

    private static Properties mSellBankrollOverseasUse;

    /**
     * 结汇资金境外用途
     *
     * @param mContext
     * @param bankrollUse
     */
    public static String getSellExchangeBankrollOverseasUse(Context mContext, String bankrollUse) {
        if (mSellBankrollOverseasUse == null) {
            mSellBankrollOverseasUse = loadProperties(mContext, "code/fess_overseas_sell_use");
        }
        return mSellBankrollOverseasUse.getProperty(bankrollUse == null ? "" : bankrollUse.trim(), "")
                .trim();
    }

    private static Properties mSellBankrollTerritoryUse;

    /**
     * 结汇资金境内用途
     *
     * @param mContext
     * @param bankrollUse
     */
    public static String getSellExchangeBankrollTerritoryUse(Context mContext, String bankrollUse) {
        if (mSellBankrollTerritoryUse == null) {
            mSellBankrollTerritoryUse = loadProperties(mContext, "code/fess_territory_sell_use");
        }
        return mSellBankrollTerritoryUse.getProperty(bankrollUse == null ? "" : bankrollUse.trim(), "")
                .trim();
    }

    private static Properties fessChannel;//渠道标识

    /**
     * 结购汇渠道标识
     * add by wang zhenning
     * @param mContext
     * @param channel
     */
    public static String getFessChannel(Context mContext, String channel) {
        if (fessChannel == null) {
            fessChannel = loadProperties(mContext, "code/fess_channel");
        }
        return fessChannel.getProperty(channel == null ? "" : channel.trim(), "")
                .trim();
    }

    /**
     * 结购汇交易类型
     * add by wang zhenning
     * @param mContext
     * @param transType
     */
    public static String getFessTransType(Context mContext, String transType) {
        if (mFessTransType == null) {
            mFessTransType = loadProperties(mContext, "code/fess_transType");
        }
        return mFessTransType.getProperty(transType == null ? "" : transType.trim(), "")
                .trim();
    }
}
