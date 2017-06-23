package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.data;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttype.SelectTypeData;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.ResultConvertUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuweidong on 2016/10/25.
 */
public class WealthViewData {
    /**
     * 封装筛选数据
     */
    public static List<SelectTypeData> buildSelectData() {
        List<SelectTypeData> list = new ArrayList<>();
        String[] name = {"产品类型", "产品币种", "产品期限", "收益类型", "风险等级"};// 标题
        String[] value0 = {"全部", "现金管理类", "净值开放类", "固定期限类"};
        String[] value1 = {"全部", "人民币元", "美元", "英镑", "港币", "加拿大元", "澳大利亚元", "欧元", "日元"};
        String[] value2 = {"全部", "30天以内", "31-90天", "91-180天", "180天以上"};
        String[] value3 = {"全部", "保本固定收益", "保本浮动收益", "非保本浮动收益"};
        String[] value4 = {"全部", "低风险", "中低风险", "中等风险", "中高风险", "高风险"};
        String[] id1 = {"000", "001", "014", "012", "013", "028", "029", "038", "027"};

        List<String[]> contentList = new ArrayList<>();// 内容的集合
        contentList.add(value0);
        contentList.add(value1);
        contentList.add(value2);
        contentList.add(value3);
        contentList.add(value4);

        for (int i = 0; i < name.length; i++) {
            SelectTypeData item = new SelectTypeData();// 筛选单个元素
            if (i == 1) {
                item.setDefaultId(id1[i]);
            } else {
                item.setDefaultId("" + 0);
            }
            List<Content> itemList = new ArrayList<>();
            String[] tempValues = contentList.get(i);
            for (int j = 0; j < tempValues.length; j++) {
                Content content = new Content();
                content.setName(tempValues[j]);// 设置名称
                if (i == 1) {// 币种
                    content.setContentNameID(id1[j]);
                    if (j == 1) {// 人民币
                        content.setSelected(true);
                    }
                } else {
                    content.setContentNameID("" + j);
                    if (j == 0) {
                        content.setSelected(true);
                    }
                }
                itemList.add(content);
            }
            item.setTitle(name[i]);
            item.setList(itemList);
            list.add(item);
        }
        return list;
    }

    /**
     * 封装排序数据
     *
     * @return
     */
    public static List<SelectTypeData> buildSortData() {
        List<SelectTypeData> list = new ArrayList<>();
        String[] name = {"排序方式", "排序条件"};// 名称
        String[] value0 = {"从高到低", "从低到高"};
        String[] value1 = {"收益率", "产品期限", "购买起点金额"};
        String[] id0 = {"1", "0"};
        String[] id1 = {"2", "1", "3"};

        List<String[]> contentList = new ArrayList<>();// 内容的集合
        contentList.add(value0);
        contentList.add(value1);

        for (int i = 0; i < name.length; i++) {
            SelectTypeData item = new SelectTypeData();
            if (i == 0) {
                item.setDefaultId("" + 1);// 从高到低（默认）
            } else {
                item.setDefaultId("" + 2);// 收益率（默认）
            }
            List<Content> itemList = new ArrayList<>();
            String[] tempValues = contentList.get(i);
            for (int j = 0; j < tempValues.length; j++) {
                Content content = new Content();
                content.setName(tempValues[j]);// 设置内容名称
                if (j == 0) {
                    content.setSelected(true);
                }
                if (i == 0) {
                    content.setContentNameID(id0[j]);
                } else {
                    content.setContentNameID(id1[j]);
                }
                itemList.add(content);
            }
            item.setTitle(name[i]);
            item.setList(itemList);
            list.add(item);
        }
        return list;
    }

    /**
     * 理财账户（登录后）
     *
     * @param accountList
     * @return
     */
    public static List<Content> buildSingleData(List<WealthAccountBean> accountList) {
        List<Content> list = new ArrayList<>();
        for (int i = 0; i < accountList.size(); i++) {
            Content content = new Content();
            content.setName(NumberUtils.formatCardNumberStrong(accountList.get(i).getAccountNo()));
            content.setContentNameID(accountList.get(i).getAccountId());
            if (i == 0) {
                content.setSelected(true);
            }
            list.add(content);
        }
        return list;
    }

    /**
     * 产品属性名称（非净值）
     *
     * @return
     */
    public static String[] productAttrNameN() {
        String[] name = new String[19];
        int index = 0;
        name[index++] = "产品名称";
        name[index++] = "产品代码";
        name[index++] = "产品币种";
        name[index++] = "预期年化收益率";
        name[index++] = "份额面值";
        name[index++] = "产品期限";
        name[index++] = "适用对象";
        name[index++] = "销售状态";
        name[index++] = "产品类型";
        name[index++] = "收益类型";
        name[index++] = "风险等级";
        name[index++] = "产品销售期";
        name[index++] = "产品成立日";
        name[index++] = "产品到期日";
        name[index++] = "交易渠道";
        name[index++] = "交易时间";
        name[index++] = "挂单时间";
        name[index++] = "到期本金到账日";
        name[index++] = "付息规则";
        return name;
    }

    /**
     * 产品属性名称（净值）
     *
     * @return
     */
    public static String[] productAttrNameY() {
        String[] name = new String[17];
        int index = 0;
        name[index++] = "产品名称";
        name[index++] = "产品代码";
        name[index++] = "产品币种";
        name[index++] = "单位净值";
        name[index++] = "净值日期";
        name[index++] = "产品期限";
        name[index++] = "适用对象";
        name[index++] = "销售状态";
        name[index++] = "产品类型";
        name[index++] = "收益类型";
        name[index++] = "风险等级";
        name[index++] = "产品销售期";
        name[index++] = "产品成立日";
        name[index++] = "产品到期日";
        name[index++] = "交易渠道";
        name[index++] = "交易时间";
        name[index++] = "挂单时间";
        return name;
    }

    /**
     * 购买属性名称（非净值）
     *
     * @return
     */
    public static String[] buyAttrNameN() {
        String[] name = new String[5];
        int index = 0;
        name[index++] = "首次购买起点\n金额(起购金额)";
        name[index++] = "追加购买起点\n金额";
        name[index++] = "购买金额基数";
        name[index++] = "申购开放规则";
        name[index++] = "购买扣款规则";
        return name;
    }

    /**
     * 购买属性名称（净值）
     *
     * @return
     */
    public static String[] buyAttrNameY() {
        String[] name = new String[7];
        int index = 0;
        name[index++] = "首次购买起点\n金额（起购金额）";
        name[index++] = "追加购买起点\n金额";
        name[index++] = "购买金额基数";
        name[index++] = "申购开放规则";
        name[index++] = "购买扣款规则";
        name[index++] = "认购手续费";
        name[index++] = "申购手续费";
        return name;
    }

    /**
     * 赎回属性名称（非净值）
     *
     * @return
     */
    public static String[] redeemAttrNameN(String sellType) {
        if (WealthConst.SELL_TYPE_00.equals(sellType)) {// 不允许赎回
            String[] name = new String[1];
            name[0] = "赎回开放规则";
            return name;
        }
        String[] name = new String[5];
        int index = 0;
        name[index++] = "赎回起点份额";
        name[index++] = "最低持有份额";
        name[index++] = "赎回开放规则";
        name[index++] = "赎回本金到账规则";
        name[index++] = "赎回收益到账规则";
        return name;
    }

    /**
     * 赎回属性名称（净值）
     *
     * @return
     */
    public static String[] redeemAttrNameY(String sellType) {
        if (WealthConst.SELL_TYPE_00.equals(sellType)) {// 不允许赎回
            String[] name = new String[1];
            name[0] = "赎回开放规则";
            return name;
        }
        String[] name = new String[6];
        int index = 0;
        name[index++] = "赎回起点份额";
        name[index++] = "最低持有份额";
        name[index++] = "赎回开放规则";
        name[index++] = "资金到账规则";
        name[index++] = "赎回手续费";
        name[index++] = "业绩报酬（浮\n动管理费）";
        return name;
    }

    /**
     * 产品属性值（非净值）
     *
     * @return
     */
    public static String[] productAttrValueN(WealthDetailsBean detailsBean, Context context) {
        String[] value = new String[19];
        int index = 0;
        value[index++] = detailsBean.getProdName();// 产品名称
        value[index++] = detailsBean.getProdCode();// 产品代码
        value[index++] = PublicCodeUtils.getCurrency(context, detailsBean.getCurCode());// 产品币种
        value[index++] = ResultConvertUtils.convertRate(detailsBean.getYearlyRR(), detailsBean.getRateDetail());// 预期年化收益率
        value[index++] = MoneyUtils.getRoundNumber(detailsBean.getBuyPrice(), 4, BigDecimal.ROUND_HALF_UP);// 份额面值
        value[index++] = ResultConvertUtils.convertDate(detailsBean.getProductKind(), detailsBean.getProdTimeLimit(), detailsBean.getIsLockPeriod(), detailsBean.getProductTermType());// 产品期限
        value[index++] = ResultConvertUtils.convertApplyObj(detailsBean);// 适用对象
        value[index++] = ResultConvertUtils.convertProductStatus(detailsBean.getStatus());// 销售状态
        value[index++] = ResultConvertUtils.convertProductType(detailsBean.getProductType());// 产品类型
        value[index++] = ResultConvertUtils.convertRiskType(detailsBean.getProdRiskType());// 收益类型
        value[index++] = ResultConvertUtils.convertRiskLevel(detailsBean.getProdRisklvl());// 风险等级
        value[index++] = detailsBean.getSellingStartingDate() + "-" + detailsBean.getSellingEndingDate();// 产品销售期
        value[index++] = detailsBean.getProdBegin();// 产品成立日
        value[index++] = ResultConvertUtils.convertProdEnd(detailsBean.getProdEnd(), detailsBean.getProductKind(), detailsBean.getIsLockPeriod(), detailsBean.getProductTermType());// 产品到期日
        value[index++] = ResultConvertUtils.convertWay(detailsBean);// 交易渠道
        value[index++] = detailsBean.getStartTime() + "-" + detailsBean.getEndTime();// 交易时间
        value[index++] = "0".equals(detailsBean.getOutTimeOrder()) ? "不允许挂单" : (detailsBean.getOrderStartTime() + "-" + detailsBean.getOrderEndTime());// 挂单时间
        value[index++] = ResultConvertUtils.convertPaymentDate(detailsBean.getPaymentDate(), detailsBean.getIsLockPeriod(), detailsBean.getRedPaymentDate(), detailsBean.getDatesPaymentOffset());// 到期本金到账日
        value[index++] = ResultConvertUtils.convertCouponpayFreq(detailsBean.getCouponpayFreq(), detailsBean.getInterestDate());// 付息规则
        return value;
    }

    /**
     * 产品属性值（净值）
     *
     * @return
     */
    public static String[] productAttrValueY(WealthDetailsBean detailsBean, Context context) {
        String[] value = new String[17];
        int index = 0;
        value[index++] = detailsBean.getProdName();// 产品名称
        value[index++] = detailsBean.getProdCode();// 产品代码
        value[index++] = PublicCodeUtils.getCurrency(context, detailsBean.getCurCode());// 产品币种
        value[index++] = MoneyUtils.getRoundNumber(detailsBean.getPrice(), 4, BigDecimal.ROUND_HALF_UP);// 单位净值
        value[index++] = detailsBean.getPriceDate();// 净值日期
        value[index++] = ResultConvertUtils.convertDate(detailsBean.getProductKind(), detailsBean.getProdTimeLimit(), detailsBean.getIsLockPeriod(), detailsBean.getProductTermType());// 产品期限
        value[index++] = ResultConvertUtils.convertApplyObj(detailsBean);// 适用对象
        value[index++] = ResultConvertUtils.convertProductStatus(detailsBean.getStatus());// 销售状态
        value[index++] = ResultConvertUtils.convertProductType(detailsBean.getProductType());// 产品类型
        value[index++] = ResultConvertUtils.convertRiskType(detailsBean.getProdRiskType());// 收益类型
        value[index++] = ResultConvertUtils.convertRiskLevel(detailsBean.getProdRisklvl());// 风险等级
        value[index++] = detailsBean.getSellingStartingDate() + "-" + detailsBean.getSellingEndingDate();// 产品销售期
        value[index++] = detailsBean.getProdBegin();// 产品成立日
        value[index++] = ResultConvertUtils.convertProdEnd(detailsBean.getProdEnd(), detailsBean.getProductKind(), detailsBean.getIsLockPeriod(), detailsBean.getProductTermType());// 产品到期日
        value[index++] = ResultConvertUtils.convertWay(detailsBean);// 交易渠道
        value[index++] = detailsBean.getStartTime() + "-" + detailsBean.getEndTime();// 交易时间
        value[index++] = "0".equals(detailsBean.getOutTimeOrder()) ? "不允许挂单" : (detailsBean.getOrderStartTime() + "-" + detailsBean.getOrderEndTime());// 挂单时间
        return value;
    }

    /**
     * 购买属性值（非净值）
     *
     * @return
     */
    public static String[] buyAttrValueN(WealthDetailsBean detailsBean, Context context) {
        String[] value = new String[5];
        int index = 0;
        value[index++] = MoneyUtils.getLoanAmountShownRMB1(detailsBean.getSubAmount(), detailsBean.getCurCode());// 首次购买起点金额（起购金额）
        value[index++] = MoneyUtils.transMoneyFormat(detailsBean.getAddAmount(), detailsBean.getCurCode()).replaceAll(",", "");// 追加购买起点金额
        value[index++] = MoneyUtils.transMoneyFormat(detailsBean.getBaseAmount(), detailsBean.getCurCode()).replaceAll(",", "");// 购买金额基数
        value[index++] = ResultConvertUtils.convertBuyType(detailsBean);// 申购开放规则
        value[index++] = context.getString(R.string.boc_wealth_buy_deduct_rule);// 购买扣款规则
        return value;
    }

    /**
     * 购买属性值（净值）
     *
     * @return
     */
    public static String[] buyAttrValueY(WealthDetailsBean detailsBean, Context context) {
        String[] value = new String[7];
        int index = 0;
        value[index++] = MoneyUtils.getLoanAmountShownRMB1(detailsBean.getSubAmount(), detailsBean.getCurCode());// 首次购买起点金额（起购金额）
        value[index++] = MoneyUtils.transMoneyFormat(detailsBean.getAddAmount(), detailsBean.getCurCode()).replaceAll(",", "");// 追加购买起点金额
        value[index++] = MoneyUtils.transMoneyFormat(detailsBean.getBaseAmount(), detailsBean.getCurCode()).replaceAll(",", "");// 购买金额基数
        value[index++] = ResultConvertUtils.convertBuyType(detailsBean);// 申购开放规则
        value[index++] = context.getString(R.string.boc_wealth_buy_deduct_rule_2);// 购买扣款规则
        value[index++] = StringUtils.isEmptyOrNull(detailsBean.getSubscribeFee()) ? "无" : detailsBean.getSubscribeFee().trim().substring(0, detailsBean.getSubscribeFee().length() - 1);// 认购手续费
        value[index++] = StringUtils.isEmptyOrNull(detailsBean.getPurchFee()) ? "无" : detailsBean.getPurchFee().trim().substring(0, detailsBean.getPurchFee().length() - 1);// 申购手续费
        return value;
    }

    /**
     * 赎回属性值（非净值）
     *
     * @return
     */
    public static String[] redeemAttrValueN(WealthDetailsBean detailsBean) {
        if (WealthConst.SELL_TYPE_00.equals(detailsBean.getSellType())) {// 不允许赎回
            String[] value = new String[1];
            value[0] = "不开放主动赎回";
            return value;
        }
        String[] value = new String[5];
        int index = 0;
        value[index++] = MoneyUtils.transMoneyFormat(detailsBean.getLowLimitAmount(), detailsBean.getCurCode()).replaceAll(",", "");// 赎回起点份额
        value[index++] = MoneyUtils.transMoneyFormat(detailsBean.getLimitHoldBalance(), detailsBean.getCurCode()).replaceAll(",", "");// 最低持有份额
        value[index++] = ResultConvertUtils.convertRedeemType(detailsBean);// 赎回开放规则
        value[index++] = ResultConvertUtils.convertRedPaymentMode(detailsBean.getRedPaymentMode(),
                detailsBean.getDateModeType(), detailsBean.getRedPaymentDate(), detailsBean.getIsLockPeriod(),
                detailsBean.getPaymentDate(), detailsBean.getDatesPaymentOffset());// 赎回本金到账规则
        value[index++] = ResultConvertUtils.convertProfitMode(detailsBean.getProfitMode(),
                detailsBean.getDateModeType(), detailsBean.getProfitDate(), detailsBean.getIsLockPeriod(),
                detailsBean.getRedPayDate(), detailsBean.getDatesPaymentOffset());// 赎回收益到账规则
        return value;
    }

    /**
     * 赎回属性值（净值）
     *
     * @return
     */
    public static String[] redeemAttrValueY(WealthDetailsBean detailsBean) {
        if (WealthConst.SELL_TYPE_00.equals(detailsBean.getSellType())) {// 不允许赎回
            String[] value = new String[1];
            value[0] = "不开放主动赎回";
            return value;
        }
        String[] value = new String[6];
        int index = 0;
        value[index++] = MoneyUtils.transMoneyFormat(detailsBean.getLowLimitAmount(), detailsBean.getCurCode()).replaceAll(",", "");// 赎回起点份额
        value[index++] = MoneyUtils.transMoneyFormat(detailsBean.getLimitHoldBalance(), detailsBean.getCurCode()).replaceAll(",", "");// 最低持有份额
        value[index++] = ResultConvertUtils.convertRedeemType(detailsBean);// 赎回开放规则
        value[index++] = ResultConvertUtils.convertRedPaymentMode(detailsBean.getRedPaymentMode(),
                detailsBean.getDateModeType(), detailsBean.getRedPaymentDate(), "", detailsBean.getPaymentDate(), "");// 资金到账规则
        value[index++] = StringUtils.isEmptyOrNull(detailsBean.getRedeemFee()) ? "无" : detailsBean.getRedeemFee().trim().substring(0, detailsBean.getRedeemFee().length() - 1);// 赎回手续费
        value[index++] = "实际年化收益大于" + detailsBean.getPfmcdrawStart() + "%时，超出部分收益按照" + detailsBean.getPfmcdrawScale() + "%收取业绩报酬";// 业绩报酬（浮动管理费）
        return value;
    }

    /**
     * 中银理财各模块需要开通理财个数
     *
     * @param moduleId
     * @return
     */
    public static boolean[] needsStatus(String moduleId) {
        boolean[] needs = new boolean[3];
        switch (moduleId) {
            case ModuleCode.MODULE_BOCINVT_0100:// 我的持仓
                needs[0] = true;
                needs[1] = true;
                needs[2] = true;
                break;
            case ModuleCode.MODULE_BOCINVT_0200:// 交易查询
                needs[0] = true;
                needs[1] = false;
                needs[2] = true;
                break;
            case ModuleCode.MODULE_BOCINVT_0400:// 撤单
                needs[0] = true;
                needs[1] = false;
                needs[2] = true;
                break;
            case ModuleCode.MODULE_BOCINVT_0500:// 投资协议管理
                needs[0] = true;
                needs[1] = false;
                needs[2] = true;
                break;
            case ModuleCode.MODULE_BOCINVT_0600:// 账户管理
                needs[0] = true;
                needs[1] = true;
                needs[2] = false;
                break;
            case ModuleCode.MODULE_BOCINVT_0700:// 理财推荐
                needs[0] = true;
                needs[1] = true;
                needs[2] = true;
                break;
            case WealthDetailsFragment.BUY:// 购买
                needs[0] = true;
                needs[1] = true;
                needs[2] = true;
                break;
            case WealthDetailsFragment.BUYGROUP:// 组合购买
                needs[0] = true;
                needs[1] = true;
                needs[2] = true;
                break;
            case WealthDetailsFragment.PROTOCOL:// 投资协议申请
                needs[0] = true;
                needs[1] = true;
                needs[2] = true;
                break;
            case WealthDetailsFragment.PROFIT:// 收益试算
                needs[0] = true;
                needs[1] = false;
                needs[2] = false;
                break;
            case ModuleCode.MODULE_BALANCE_0000:
                needs[0] = true;
                needs[1] = false;
                needs[2] = false;
            default:
                break;
        }
        return needs;
    }

}
