package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils;

import android.content.SharedPreferences;

import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttype.SelectTypeData;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuweidong on 2016/10/27.
 */
public class WealthPublicUtils {

    /**
     * 判断理财开通状态（接口调用成功）
     */
    public static boolean isOpenAll(boolean[] needs, boolean[] openStatus) {
        int sum = 0;
        int need = 0;
        for (int i = 0; i < openStatus.length; i++) {
            if (needs[i]) {
                ++need;
            }
            if (needs[i] && openStatus[i]) {
                ++sum;
            }
        }
        if (sum == need) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 产品列表进行排序
     *
     * @param list
     * @param preferences
     * @return
     */
    public static List<WealthListBean> sortList(List<WealthListBean> list, SharedPreferences preferences) {
        if (list == null) {
            list = new ArrayList<>();
            return list;
        }
        for (int i = 0; i < list.size(); i++) {// 日积月累置顶
            if (WealthConst.PRODUCT_DAY.equals(list.get(i).getProdCode())) {
                WealthListBean item = list.get(i);
                list.remove(i);
                list.add(0, item);
                break;
            }
        }
        int index = 0;
        if (WealthConst.PRODUCT_DAY.equals(list.get(0).getProdCode())) {
            index = 1;
        } else {
            index = 0;
        }
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j).getProdCode().equals(preferences.getString(WealthDetailsFragment.PROD_CODE + list.get(j).getProdCode(), "default"))) {
                if (j == 0 && WealthConst.PRODUCT_DAY.equals(list.get(j).getProdCode())) {
                    continue;
                }
                WealthListBean item = list.get(j);
                list.remove(j);
                list.add(index, item);
                index++;
            }
        }
        return list;
    }

    /**
     * 重置筛选为默认状态
     *
     * @param list
     */
    public static void resetSelectDefault(List<SelectTypeData> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).getList().size(); j++) {
                Content content = list.get(i).getList().get(j);
                if (list.get(i).getDefaultId().equals(content.getContentNameID()))
                    content.setSelected(true);
                else
                    content.setSelected(false);
            }
        }
    }

    /**
     * 封装分享的字符串
     *
     * @param prodKind
     * @param values
     * @return
     */
    public static String buildShareStr(String prodKind, String[] values, String curCode){
        String str = "";
        StringBuilder sb = new StringBuilder(str);
        String money = MoneyUtils.getLoanAmountShownRMB1(values[2], curCode);// 起购金额
        if (WealthConst.PRODUCT_KIND_0.equals(prodKind)) {// 非净值
            sb.append("预期年化收益率：").append(values[0]).append("\n产品期限：").append(values[1])
                    .append("\n起购金额：").append(money);
            return sb.toString();
        } else if (WealthConst.PRODUCT_KIND_1.equals(prodKind)) {// 净值
            sb.append("单位净值：").append(values[0]).append("\n产品期限：").append(values[1])
                    .append("\n起购金额：").append(money);
            return sb.toString();
        } else {
            return str;
        }
    }

    public static String buildShareStr(PurchaseModel model) {
        String str = "";
        String date = ResultConvertUtils.convertDate(model.getProductKind(), model.getProdTimeLimit(), model.getIsLockPeriod(), model.getProductTermType());
        StringBuilder sb = new StringBuilder(str);
        if (WealthConst.PRODUCT_KIND_0.equals(model.getProductKind())) {// 非净值
            sb.append("预期年化收益率：").append(ResultConvertUtils.convertRate(model.getYearlyRR(), model.getRateDetail()))
                    .append("\n产品期限：").append(date)
                    .append("\n起购金额：").append(model.getSubAmountStr());
            return sb.toString();
        } else if (WealthConst.PRODUCT_KIND_1.equals(model.getProductKind())) {// 净值
            sb.append("单位净值：").append(MoneyUtils.getRoundNumber(model.getPeriodPrice(),4)).append("\n产品期限：").append(date)
                    .append("\n起购金额：").append(model.getSubAmountStr());
            return sb.toString();
        } else {
            return str;
        }
    }
}
