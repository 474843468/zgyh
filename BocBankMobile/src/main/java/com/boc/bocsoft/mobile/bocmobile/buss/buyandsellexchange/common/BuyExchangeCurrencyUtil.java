package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.common;

import android.content.Context;
import android.util.Xml;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyExchangeModel;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * 购汇-币种和对应的结构汇工具类，币种和结汇购汇的匹配
 * Created by gwluo on 2016/12/12.
 */

public class BuyExchangeCurrencyUtil {
    private static BuyExchangeCurrencyUtil currencyUtil;
    private static Context mContext;

    public static BuyExchangeCurrencyUtil getInstance(Context context) {
        if (currencyUtil == null) {
            mContext = context;
            currencyUtil = new BuyExchangeCurrencyUtil();
        }
        return currencyUtil;
    }

    private BuyExchangeCurrencyUtil() {
        loadData();
    }

    /**
     * 格式化牌价
     * 如果小数位大于4位，则保留前4位，后面的直接截掉（不进行四舍五入显示）；
     * 如小数位小于4位，则按照后台取回牌价直接显示；
     * 如果小数末尾是数字“0”的，则去掉数字“0”的显示 。
     *
     * @param rate
     * @return
     */
    public static String formatExchangeRate(String rate) {
        if (StringUtils.isEmpty(rate))
            return "";
        String[] split = rate.split("\\.");
        String returnRate;
        int length;
        if (split.length == 2) {
            length = split[1].length();
        } else {
            return rate;
        }
        BigDecimal normalRate = new BigDecimal(rate);
        if (length > 4) {
            returnRate = normalRate.divide(new BigDecimal("1"), 4, BigDecimal.ROUND_DOWN).toPlainString();
            returnRate = formatExchangeRate(returnRate);
        } else {
            returnRate = MoneyUtils.trimAmountZero(normalRate.divide(new BigDecimal("1"), length, BigDecimal.ROUND_DOWN).toPlainString());
        }
        return returnRate;
    }

    LinkedHashMap<String, List<String>> cashSpot;

    public LinkedHashMap<String, List<String>> getData() {
        return cashSpot;
    }

    public List<String> getCurrencyList() {
        List<String> currencuList = new ArrayList<>();
        Set<String> strings = cashSpot.keySet();
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            currencuList.add(iterator.next());
        }
        return currencuList;
    }

    public List<String> getCashSpot(String key) {
        return cashSpot.get(key);
    }

    private void loadData() {
        XmlPullParser parser = Xml.newPullParser();
        InputStream inputStream = mContext.getApplicationContext().getResources().openRawResource(R.raw.buy_exchange);
        try {
            parser.setInput(inputStream, "utf-8");
            cashSpot = new LinkedHashMap<>();
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                if (null == name) {
                    parser.next();
                    continue;
                }
                int eventType = parser.getEventType();
                String currencyName = "";
                if ("currency".equals(name) && eventType == XmlPullParser.START_TAG) {
                    currencyName = parser.getAttributeValue(null, "name");
                    List<String> cashSpotList = new ArrayList<>();
                    for (; ; ) {
                        parser.next();
                        int type = parser.getEventType();
                        String name1 = parser.getName();
                        if (null == name1) {
                            continue;
                        }
                        if (XmlPullParser.END_TAG == type) {
                            if ("currency".equals(name1)) {
                                break;
                            }
                            if ("cash".equals(name1) || "spot".equals(name1)) {
                                continue;
                            }
                        }
                        if (XmlPullParser.START_TAG == type && "cash".equals(name1)) {
                            cashSpotList.add(parser.nextText());
                        }
                        if (XmlPullParser.START_TAG == type && "spot".equals(name1)) {
                            cashSpotList.add(parser.nextText());
                        }
                    }
                    cashSpot.put(currencyName, cashSpotList);
                }
                parser.next();
            }
        } catch (Exception e) {
            ToastUtils.show(e.getMessage());
        }
    }

    /**
     * 集合中取出特定Id的账户
     *
     * @param accId
     * @return
     */
    public static AccountBean getAccFromList(BuyExchangeModel model, String accId) {
        for (AccountBean bean : model.getAccList()) {
            if (bean.getAccountId().equals(accId)) {
                return bean;
            }
        }
        return null;
    }

    /**
     * 是否支持购汇
     *
     * @return
     */
    public static boolean isSupportBuyExchange(String id) {
        if ("01".equals(id) || "02".equals(id)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否支持结汇
     *
     * @return
     */
    public static boolean isSupportSellExchange(String id) {
        if ("01".equals(id)
                || "02".equals(id)
                || "03".equals(id)
                || "47".equals(id)
                || "48".equals(id)
                || "49".equals(id)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据币种返回国旗drawable的id
     *
     * @param currency
     * @return
     */
    public static int getNationalFlagFromCurrency(String currency) {
//        if (ApplicationConst.CURRENCY_USD.equals(currency)) {
//            return R.drawable.boc_mei_guo;
//        } else if ("096".equals(currency)) {
//            return R.drawable.boc_a_lian_qiu;
//        } else if (ApplicationConst.CURRENCY_AUD.equals(currency)) {
//            return R.drawable.boc_ao_da_li_ya;
//        } else if (ApplicationConst.CURRENCY_MOP.equals(currency)) {
//            return R.drawable.boc_ao_men;
//        } else if (ApplicationConst.CURRENCY_BRL.equals(currency)) {
//            return R.drawable.boc_ba_xi;
//        } else if (ApplicationConst.CURRENCY_DKK.equals(currency)) {
//            return R.drawable.boc_dan_mai;
//        } else if (ApplicationConst.CURRENCY_E_LUO_SI.equals(currency)) {
//            return R.drawable.boc_e_luo_si;
//        } else if ("082".equals(currency)) {
//            return R.drawable.boc_fei_lv_bin;
//        } else if ("088".equals(currency)) {
//            return R.drawable.boc_han_guo;
//        } else if (ApplicationConst.CURRENCY_CAD.equals(currency)) {
//            return R.drawable.boc_jia_na_da;
//        } else if ("070".equals(currency)) {
//            return R.drawable.boc_nan_fei;
//        } else if ("023".equals(currency)) {
//            return R.drawable.boc_nuo_wei;
//        } else if ("038".equals(currency)) {
//            return R.drawable.boc_ou_men;
//        } else if (ApplicationConst.CURRENCY_JPY.equals(currency)) {
//            return R.drawable.boc_ri_ben;
//        } else if ("021".equals(currency)) {
//            return R.drawable.boc_rui_dian;
//        } else if ("015".equals(currency)) {
//            return R.drawable.boc_rui_shi;
//        } else if ("213".equals(currency)) {
//            return R.drawable.boc_tai_wan;
//        } else if ("084".equals(currency)) {
//            return R.drawable.boc_tai_guo;
//        } else if ("013".equals(currency)) {
//            return R.drawable.boc_xiang_gang;
//        } else if (ApplicationConst.CURRENCY_SGD.equals(currency)) {
//            return R.drawable.boc_xin_jia_po;
//        } else if ("087".equals(currency)) {
//            return R.drawable.boc_xin_xi_lan;
//        } else if ("085".equals(currency)) {
//            return R.drawable.boc_yin_du;
//        } else if ("056".equals(currency)) {
//            return R.drawable.boc_yin_ni;
//        } else if (ApplicationConst.CURRENCY_GBP.equals(currency)) {
//            return R.drawable.boc_ying_guo;
//        } else {
            return 0;
//        }
    }
}
