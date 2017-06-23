package com.chinamworld.llbt.utils;

import com.chinamworld.llbt.model.AccountItem;
import com.chinamworld.llbtwidget.R;

/**
 * Created by Administrator on 2016/8/27.
 */
public class AccountInfoTransferTools {

    /**
     * 根据账户信息获取相应的账户图片
     */
    public static int getCardPic(AccountItem accountBean) {
        if(accountBean == null)
            return R.drawable.boc_cardpic_other;
        CardType cardType = getCardType(accountBean.getAccountType());
        switch (cardType) {
            case CREDIT_CARD:
                //TODO 此处应该再根据cardDescription来判断具体的信用卡品种
                return R.drawable.llbt_cardpic_credit;
            case DEBIT_CARD:
                //TODO 此处不知道应该在根据说什么判断借记卡品种
                return R.drawable.llbt_cardpic_debit;
            case CURRENT_ACCOUNT:
                return R.drawable.llbt_cardpic_account_current;
            case TERM_ACCOUNT:
//                return R.drawable.llbt_cardpic_account_term;
                return R.drawable.llbt_cardpic_account_current;
            case BOCINVT:
                return R.drawable.llbt_invest;
            case ECASH:
                return R.drawable.llbt_cardpic_ecash;
            default:
                return R.drawable.boc_cardpic_other;
        }
    }

    public enum CardType {
        CREDIT_CARD, DEBIT_CARD, TERM_ACCOUNT, CURRENT_ACCOUNT, BOCINVT, ECASH, OTHER
    }

    /**
     * 将账户类型转换成卡片类型
     *
     * @param accounttype 账户类型
     */
    public static CardType getCardType(String accounttype) {
        //定期
        if ("188".equals(accounttype)) {
            return CardType.TERM_ACCOUNT;
        }
        //活期
        if ("101".equals(accounttype)) {
            return CardType.CURRENT_ACCOUNT;
        }
        //借记卡
        if ("119".equals(accounttype)) {
            return CardType.DEBIT_CARD;
        }
        //信用卡
        if ("103".equals(accounttype) || "104".equals(accounttype) || "107".equals(accounttype)) {
            return CardType.CREDIT_CARD;
        }
        //网上专属理财
//        if (ApplicationConst.ACC_TYPE_BOCINVT.equals(accounttype)) {
//            return CardType.BOCINVT;
//        }
        //电子现金卡
//        if (ApplicationConst.ACC_TYPE_ECASH.equals(accounttype)) {
//            return CardType.ECASH;
//        }
        return CardType.OTHER;
    }


    /** 账号464显示 */
    public static String getForSixForString(String accountNum) {
        if(accountNum == null) {
            return "-";
        } else if(accountNum.length() < 8) {
            return accountNum;
        } else {
            StringBuffer sbShow = new StringBuffer(accountNum.subSequence(0, 4));
            sbShow.append("******");
            sbShow.append(accountNum.substring(accountNum.length() - 4, accountNum.length()));
            return sbShow.toString();
        }
    }
}
