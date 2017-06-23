package com.boc.bocsoft.mobile.bocmobile.base.utils;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountResult;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;

import java.util.ArrayList;
import java.util.List;

import static com.boc.bocsoft.mobile.common.utils.PublicUtils.checkNotNull;

/**
 * Created by feibin on 2016/5/24.
 * 账户工具类
 */
public class AccountUtils {

    /**
     * 账户余额查询总数
     */
    public static int ACCOUNT_BALANCE_SUM = 20;

    /**
     * 获取钞汇标识
     *
     * @param cashRemitCode added by niuguobin 20160610
     */
    public static String getCashRemit(String cashRemitCode) {
        if ("00".equals(cashRemitCode)) {
            return "";
        }
        if ("01".equals(cashRemitCode)) {
            return ApplicationContext.getAppContext().getString(R.string.boc_cash);
        }
        if ("02".equals(cashRemitCode)) {
            return ApplicationContext.getAppContext().getString(R.string.boc_spot);
        }
        return "";
    }

    /**
     * 获取钞汇标识
     *
     * @param cashRemitCode added by niuguobin 20160610
     */
    public static String getCashRemitName(String cashRemitCode) {
        if ("01".equals(cashRemitCode)) {
            return ApplicationContext.getAppContext().getString(R.string.boc_cash_name);
        }
        if ("02".equals(cashRemitCode)) {
            return ApplicationContext.getAppContext().getString(R.string.boc_spot_name);
        }
        return "";
    }

    /**
     * 中行所有账户BII Model转换为View Model
     *
     * @param sourceData added by feibin 20160629
     */
    public static List<AccountBean> convertBIIAccount2ViewModel(
            List<PsnCommonQueryAllChinaBankAccountResult> sourceData) {
        List<AccountBean> mAccountList = new ArrayList<AccountBean>();
        if (null != sourceData) {
            for (PsnCommonQueryAllChinaBankAccountResult item : sourceData) {
                AccountBean convertItem = new AccountBean();
                convertItem.setAccountType(item.getAccountType());
                convertItem.setAccountIbkNum(item.getAccountIbkNum());
                convertItem.setAccountId(String.valueOf(item.getAccountId()));
                convertItem.setAccountName(item.getAccountName());
                convertItem.setAccountNumber(item.getAccountNumber());
                //convertItem.setAccountStatus(item.getAccountStatus());
                convertItem.setBranchId(String.valueOf(item.getBranchId()));
                convertItem.setCardDescription(item.getCardDescription());
                convertItem.setCurrencyCode(item.getCurrencyCode());
                convertItem.setCurrencyCode2(item.getCurrencyCode2());
                convertItem.setCardDescriptionCode(item.getCardDescriptionCode());
                convertItem.setCustomerId(String.valueOf(item.getCustomerId()));
                convertItem.setBranchName(item.getBranchName());
                convertItem.setEcard(item.getEcard());
                convertItem.setAccountCatalog(item.getAccountCatalog());
                convertItem.setHasOldAccountFlag(item.getHasOldAccountFlag());
                convertItem.setIsECashAccount(item.getIsECashAccount());
                convertItem.setIsMedicalAccount(item.getIsMedicalAccount());
                convertItem.setNickName(item.getNickName());
                convertItem.setVerifyFactor(item.getVerifyFactor());
                mAccountList.add(convertItem);
            }
        }
        return mAccountList;
    }

    /**
     * 根据账户信息获取相应的账户图片
     */
    public static int getCardPic(AccountBean accountBean) {
        checkNotNull(accountBean, "账户信息不能为空");
        CardType cardType = getCardType(accountBean.getAccountType());
        return getCardPic(cardType);
    }

    /**
     * 根据账户信息获取相应的账户图片
     */
    public static int getCardPic(CardType cardType) {
        switch (cardType) {
            case CREDIT_CARD:
                //TODO 此处应该再根据cardDescription来判断具体的信用卡品种
                return R.drawable.boc_cardpic_credit;
            //TODO  虚拟信用卡暂时没出图，先用信用卡的
            case XNCRCD:
                return R.drawable.boc_cardpic_virtual;
            case DEBIT_CARD:
                //TODO 此处不知道应该在根据什么判断借记卡品种
                return R.drawable.boc_cardpic_debit;
            case CURRENT_ACCOUNT:
                return R.drawable.boc_cardpic_account_current;
            case TERM_ACCOUNT:
                return R.drawable.boc_cardpic_account_term;
            case BOCINVT:
                return R.drawable.boc_invest;
            case ECASH:
                return R.drawable.boc_cardpic_ecash;
            default:
                return R.drawable.boc_cardpic_other;
        }
    }

    public enum CardType {
        CREDIT_CARD, //信用卡
        DEBIT_CARD, //借记卡
        TERM_ACCOUNT, //定期
        CURRENT_ACCOUNT, //活期
        BOCINVT,//网上专属理财
        ECASH, //电子现金卡
        XNCRCD,//虚拟信用卡
        OTHER//其他
    }

    /**
     * 将账户类型转换成卡片类型
     *
     * @param accounttype 账户类型
     */
    public static CardType getCardType(String accounttype) {
        //定期
        if (ApplicationConst.ACC_TYPE_REG.equals(accounttype)
                || ApplicationConst.ACC_TYPE_EDU.equals(accounttype)
                || ApplicationConst.ACC_TYPE_ZOR.equals(accounttype)
                || ApplicationConst.ACC_TYPE_CBQX.equals(accounttype)) {
            return CardType.TERM_ACCOUNT;
        }
        //活期
        if (ApplicationConst.ACC_TYPE_RAN.equals(accounttype)
                || ApplicationConst.ACC_TYPE_ORD.equals(accounttype)) {
            return CardType.CURRENT_ACCOUNT;
        }
        //借记卡
        if (ApplicationConst.ACC_TYPE_BRO.equals(accounttype)
                || ApplicationConst.ACC_TYPE_JIEJIXN.equals(accounttype)) {
            return CardType.DEBIT_CARD;
        }
        //信用卡
        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accounttype)
                || ApplicationConst.ACC_TYPE_GRE.equals(accounttype)
                || ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(accounttype)) {
            return CardType.CREDIT_CARD;
        }
        //虚拟信用卡
        if (ApplicationConst.ACC_TYPE_XNCRCD1.equals(accounttype)
                || ApplicationConst.ACC_TYPE_XNCRCD2.equals(accounttype)) {
            return CardType.XNCRCD;
        }
        //网上专属理财
        if (ApplicationConst.ACC_TYPE_BOCINVT.equals(accounttype)) {
            return CardType.BOCINVT;
        }
        //电子现金卡
        if (ApplicationConst.ACC_TYPE_ECASH.equals(accounttype)) {
            return CardType.ECASH;
        }
        return CardType.OTHER;
    }
}
