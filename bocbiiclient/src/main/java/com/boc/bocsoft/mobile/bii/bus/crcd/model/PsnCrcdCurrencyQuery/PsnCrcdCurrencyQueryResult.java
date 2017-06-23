package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询信用卡币种
 * Created by xdy4486 on 2016/6/27.
 */
public class PsnCrcdCurrencyQueryResult {


    private CurrencyBean currency1;
    private CurrencyBean currency2;

    public CurrencyBean getCurrency1() {
        return currency1;
    }

    public void setCurrency1(CurrencyBean currency1) {
        this.currency1 = currency1;
    }

    public CurrencyBean getCurrency2() {
        return currency2;
    }

    public void setCurrency2(CurrencyBean currency2) {
        this.currency2 = currency2;
    }

    public static class CurrencyBean {
        /**
         * "code":"014","fraction":2,"i18nId":"USD"
         */
        private String code;
        private int fraction;
        private String i18nId;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getFraction() {
            return fraction;
        }

        public void setFraction(int fraction) {
            this.fraction = fraction;
        }

        public String getI18nId() {
            return i18nId;
        }

        public void setI18nId(String i18nId) {
            this.i18nId = i18nId;
        }
    }

    public List<CurrencyBean> getCurrencyList() {
        List<CurrencyBean> list = new ArrayList<CurrencyBean>();

        if (currency1 != null)
            list.add(currency1);

        if (currency2 != null)
            list.add(currency2);

        return list;
    }
}
