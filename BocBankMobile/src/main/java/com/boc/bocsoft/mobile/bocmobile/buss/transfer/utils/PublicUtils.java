package com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * 转账汇款功能公共方法
 * Created by liuweidong on 2016/7/29.
 */
public class PublicUtils {
    /**
     * 根据是否有数据显示提示内容
     *
     * @param haveData
     */
    public static void haveDataSelectText(boolean haveData, TextView txtNoData, boolean isSelectData) {
        if (haveData) {
            txtNoData.setVisibility(View.GONE);
        } else {
            if (isSelectData) {
                txtNoData.setText(ApplicationContext.getInstance().getResources().getString(R.string.boc_transfer_select_empty));
            } else {
                txtNoData.setText(ApplicationContext.getInstance().getResources().getString(R.string.boc_transfer_query_empty));
            }
            txtNoData.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 注意：此方法不要轻易修改，如有问题请联系 王帆
     * 比较两个日期间隔是否超过mouthRange个月，startDate < endDate
     * <p/>
     * 若两个日期一样，LocalDate.isBefore方法返回false
     *
     * @return true为超过mouthRange个月，false为没有超过。
     */
    public static boolean isCompareDateRange(LocalDate startDate, LocalDate endDate, int mouthRange) {
        if (startDate == null || endDate == null) {
            return false;
        } else {
            LocalDate newDate = endDate.plusMonths(-mouthRange);
            return !newDate.isBefore(startDate.plusDays(1));

            //下面内容不要删除
//            LocalDate newDate = endDate.plusMonths(-mouthRange);
//            boolean isIllegal = false;
//            if (mouthRange > 11) {
//                // 在年份的比较中，为直接-X年
//                isIllegal = !newDate.isBefore(startDate.plusDays(1));
//            } else {
//                //在月份的比较中，为-X月+1天
//                isIllegal = !newDate.isBefore(startDate);
//            }
//            return isIllegal;
        }
    }

    /**
     * 判断选择时间范围是否合法
     *
     * @return
     */
    public static boolean judgeChoiceDateRange(LocalDate startLocalDate, LocalDate endLocalDate, int mouthRange, BussFragment bussFragment) {
        if (startLocalDate == null || endLocalDate == null) {
            bussFragment.showErrorDialog(ApplicationContext.getInstance().getResources().getString(R.string.boc_account_transdetail_date_range_null));
            return false;
        }
        if (endLocalDate.isBefore(startLocalDate)) {
            bussFragment.showErrorDialog(ApplicationContext.getInstance().getResources().getString(R.string.boc_account_transdetail_start_end));
            return false;
        }
        if (isCompareDateRange(startLocalDate, endLocalDate, mouthRange)) {
            bussFragment.showErrorDialog(ApplicationContext.getInstance().getResources().getString(R.string.boc_account_transdetail_date_range_change, changeNumberToUpper(mouthRange)));
            return false;
        }
        return true;
    }

    /**
     * 传递安全因子给组件
     */
    public static PsnGetSecurityFactorResult copyOfSecurityCombin(SecurityViewModel securityViewModel) {
        PsnGetSecurityFactorResult result = new PsnGetSecurityFactorResult();
        // 保存默认安全因子
        if (securityViewModel.get_defaultCombin() != null) {
            CombinListBean defaultCombin = new CombinListBean();
            defaultCombin.setId(securityViewModel.get_defaultCombin().getId());
            defaultCombin.setName(securityViewModel.get_defaultCombin().getName());
            defaultCombin.setSafetyFactorList(securityViewModel.get_defaultCombin().getSafetyFactorList());
            result.set_defaultCombin(defaultCombin);
        }
        // 保存安全因子列表
        List<CombinListBean> combinList = new ArrayList<CombinListBean>();
        for (int i = 0; i < securityViewModel.get_combinList().size(); i++) {
            CombinListBean item = new CombinListBean();
            item.setId(securityViewModel.get_combinList().get(i).getId());
            item.setName(securityViewModel.get_combinList().get(i).getName());
            item.setSafetyFactorList(securityViewModel.get_combinList().get(i).getSafetyFactorList());
            combinList.add(item);
        }
        result.set_combinList(combinList);
        return result;
    }

    /**
     * 改变数字的显示格式
     *
     * @param number
     * @return
     */
    public static String changeNumberToUpper(int number) {
        String upperNum = "";
        switch (number) {
            case 1:
                upperNum = "一";
                break;
            case 2:
                upperNum = "两";
                break;
            case 3:
                upperNum = "三";
                break;
            case 6:
                upperNum = "六";
                break;
            default:
                break;
        }
        return upperNum;
    }

    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue
                * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 改变文本颜色（正：红；负：绿）
     *
     * @param context
     * @param txtValue
     * @return
     */
    public static int changeTextColor(Context context, String txtValue) {
        int color = context.getResources().getColor(R.color.boc_text_color_red);
        if (txtValue == null) {
            return color;
        }
        if (txtValue.contains("-")) {
            color = context.getResources().getColor(R.color.boc_text_color_green);
        } else {
            color = context.getResources().getColor(R.color.boc_text_color_red);
        }
        return color;
    }

    /**
     * 获取要展示的AccountBean，如果保存的账户id对应账户存在于集合中返回该账户
     * 否则返回集合第一个账户
     *
     * @return
     */
    public static AccountBean getPerformAccBean(List<AccountBean> list, String accountId) {
        if (accountId == null || accountId.length() == 0) {
            return list.get(0);
        }
        AccountBean returnBean = null;
        for (AccountBean bean : list) {
            if (bean.getAccountId().equals(accountId)) {
                returnBean = bean;
                break;
            } else {
                returnBean = list.get(0);
            }
        }
        return returnBean;
    }

}

