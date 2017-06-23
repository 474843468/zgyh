package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct;

import com.boc.bocsoft.mobile.bocmobile.WebUrl;

/**
 * 中银理财常量
 * Created by liuweidong on 2016/9/28.
 */
public class WealthConst {
    public static final String PRODUCT_DAY = "AMRJYL01";// 日积月累
    public static final String URL_INSTRUCTION = WebUrl.URL_INSTRUCTION;

    public static final String ALL_0 = "0";// 全部
    public static final String NO_0 = "0";// 否
    public static final String YES_1 = "1";// 是
    public static final String YES_0 = "0";// 是
    public static final String NO_1 = "1";// 否
    /**
     * 是否为业绩基准产品
     */
    public static final String IS_LOCK_PERIOD_0 = "0";// 非业绩基准产品
    public static final String IS_LOCK_PERIOD_1 = "1";// 业绩基准-锁定期转低收益
    public static final String IS_LOCK_PERIOD_2 = "2";// 业绩基准-锁定期后入账
    public static final String IS_LOCK_PERIOD_3 = "3";// 业绩基准-锁定期周期滚续
    /**
     * 产品期限特性
     */
    public static final String TERM_TYPE_0 = "0";// 有限期封闭式
    public static final String TERM_TYPE_1 = "1";// 有限半开放式
    public static final String TERM_TYPE_3 = "3";// 无限开放式
    /**
     * 产品类型
     */
    public static final String PRODUCT_TYPE_1 = "1";// 现金管理类产品
    public static final String PRODUCT_TYPE_2 = "2";// 净值开放类产品
    public static final String PRODUCT_TYPE_3 = "3";// 固定期限产品
    /**
     * 产品性质
     */
    public static final String PRODUCT_KIND_0 = "0";// 全部|结构性理财产品（响应）
    public static final String PRODUCT_KIND_1 = "1";// 类基金理财产品
    /**
     * 产品销售状态
     */
    public static final String PRODUCT_STA_1 = "1";// 在售产品
    public static final String PRODUCT_STA_2 = "2";// 停售产品
    /**
     * 适用对象
     */
    public static final String APPLY_OBJ_0 = "0";// 有投资经验
    public static final String APPLY_OBJ_1 = "1";// 无投资经验
    /**
     * 风险级别
     */
    public static final String RISK_LEVEL_0 = "0";// 低风险
    public static final String RISK_LEVEL_1 = "1";// 中低风险
    public static final String RISK_LEVEL_2 = "2";// 中等风险
    public static final String RISK_LEVEL_3 = "3";// 中高风险
    public static final String RISK_LEVEL_4 = "4";// 高风险
    /**
     * 风险类型
     */
    public static final String RISK_TYPE_1 = "1";// 保本固定
    public static final String RISK_TYPE_2 = "2";// 保本浮动
    public static final String RISK_TYPE_3 = "3";// 非保本浮动
    /**
     * 购买开放规则
     */
    public static final String BUY_TYPE_00 = "00";// 关闭
    public static final String BUY_TYPE_01 = "01";// 开放期购买
    public static final String BUY_TYPE_02 = "02";// 周期开放购买
    public static final String BUY_TYPE_03 = "03";// 起息后每日申购
    /**
     * 赎回开放规则
     */
    public static final String SELL_TYPE_00 = "00";// 不允许赎回
    public static final String SELL_TYPE_01 = "01";// 开放期赎回
    public static final String SELL_TYPE_02 = "02";// 付息日赎回
    public static final String SELL_TYPE_03 = "03";// 起息后每日赎回
    public static final String SELL_TYPE_04 = "04";// 周期开放赎回

    public static String getShareProductUrl(String productCode, String productKind) {
        return WebUrl.getDetailShareUrl(productCode,productKind);
        //return URL_SHARE + productCode + "&productKind=" + productKind;
    }

    public static String getProductListShareUrl(){
        return  WebUrl.getListShareUrl();
    }
}
