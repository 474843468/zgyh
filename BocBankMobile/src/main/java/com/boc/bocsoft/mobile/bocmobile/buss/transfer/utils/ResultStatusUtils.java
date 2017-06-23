package com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.ResultStatusConst;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * 接口返回结果状态公共方法
 * Created by liuweidong on 2016/8/30.
 */
public class ResultStatusUtils {
    /**
     * ATM无卡取款查询状态显示
     *
     * @param status
     * @return
     */
    public static String setResultStatus(Context context, String status) {
        String state = "";
        switch (status) {
            case ResultStatusConst.STATUS_OU:
            case ResultStatusConst.STATUS_CR:// 已预约
                state = context.getString(R.string.boc_result_status_ou_or_cr);
                break;
            case ResultStatusConst.STATUS_OK:// 已取现
                state = context.getString(R.string.boc_result_status_ok);
                break;
            case ResultStatusConst.STATUS_CL:// 已撤销
                state = context.getString(R.string.boc_result_status_cl);
                break;
            case ResultStatusConst.STATUS_L3:
            case ResultStatusConst.STATUS_L6:// 已锁定
                state = context.getString(R.string.boc_result_status_l3_or_l6);
                break;
            default:
                break;
        }
        return state;
    }


    /**
     * 取款查询 -- 取款状态
     *
     * @param queryStatus
     * @return
     */
    public static String getWithdrawalStatus(Context context, String queryStatus) {
        String withdrawalStatus = "";
        switch (queryStatus) {
            case ResultStatusConst.STATUS_A://取款成功
                withdrawalStatus = context.getString(R.string.boc_result_status_withdrawal_success);
                break;
            case ResultStatusConst.STATUS_B://取款失败
                withdrawalStatus = context.getString(R.string.boc_result_status_withdrawal_fail);
                break;
            default:
                break;
        }
        return withdrawalStatus;
    }


    /**
     * 转账记录 -- 转账状态
     *
     * @param queryStatus
     * @return
     */
    public static String getTransferStatus(Context context, boolean isList, String queryStatus) {
        String transferStatus = "";
        switch (queryStatus) {
            case ResultStatusConst.STATUS_A://交易成功
                if (isList){
                    transferStatus = "";
                }else {
                    transferStatus = context.getString(R.string.boc_result_status_transfer_success);
                }
                break;
            case ResultStatusConst.STATUS_B:
            case ResultStatusConst.STATUS_12://交易失败
                transferStatus = context.getString(R.string.boc_result_status_transfer_fail);
                break;
            default://银行处理中
                transferStatus = context.getString(R.string.boc_result_status_transfer_handle);
                break;
        }
        return transferStatus;
    }


    /**
     * 汇出查询 -- 详情状态
     *
     * @param queryStatus
     * @return
     */
    public static String getRemitStatus(Context context, String queryStatus) {
        String state = "";
        switch (queryStatus) {
            case ResultStatusConst.STATUS_RD://待汇出
                state = context.getString(R.string.boc_result_status_status_rd);
                break;
            case ResultStatusConst.STATUS_OU:// 已汇款未收款
                state = context.getString(R.string.boc_result_status_status_ou);
                break;
            case ResultStatusConst.STATUS_CR:// 解付冲正
                state = context.getString(R.string.boc_result_status_status_cr);
                break;
            case ResultStatusConst.STATUS_OK://已成功收款
                state = context.getString(R.string.boc_result_status_status_ok);
                break;
            case ResultStatusConst.STATUS_CL:// 已取消汇款
                state = context.getString(R.string.boc_result_status_status_cl);
                break;
            case ResultStatusConst.STATUS_L3:// 密码错3次锁定
                state = context.getString(R.string.boc_result_status_status_l3);
                break;
            case ResultStatusConst.STATUS_L6:// 永久锁定
                state = context.getString(R.string.boc_result_status_status_l6);
                break;
            case ResultStatusConst.STATUS_A:// 取款成功
                state = context.getString(R.string.boc_result_status_status_a);
                break;
            case ResultStatusConst.STATUS_B:// 取款失败
                state = context.getString(R.string.boc_result_status_status_b);
                break;
            default:
                break;
        }
        return state;
    }


    /**
     * 预约管理 -- 详情状态
     *
     * @param queryStatus
     * @return
     */
    public static String getPreRecordStatus(Context context, boolean isList, String queryStatus) {
        String state = "";
        switch (queryStatus) {
            case ResultStatusConst.STATUS_A://交易成功
                if (isList){
                    state = "";
                }else{
                    state = context.getString(R.string.boc_result_status_transfer_success);
                }
                break;
            case ResultStatusConst.STATUS_B:
            case ResultStatusConst.STATUS_12:
            case ResultStatusConst.STATUS_K://交易失败
                state = context.getString(R.string.boc_result_status_transfer_fail);
                break;
            case ResultStatusConst.STATUS_8://已删除
                state = context.getString(R.string.boc_result_status_transfer_delete);
                break;
            default://银行处理中
                state = context.getString(R.string.boc_result_status_transfer_handle);
                break;
        }
        return state;
    }


    /**
     * 获取交易渠道
     *
     * @param queryChannel
     * @return
     */
    public static String getTransChannel(Context context, String queryChannel) {
        String channel = "";
        switch (queryChannel) {
            case "1"://网上银行
                channel = context.getString(R.string.boc_channel_bank_net);
                break;
            case "2"://手机银行
                channel = context.getString(R.string.boc_channel_bank_mobile);
                break;
            case "4"://家居银行
                channel = context.getString(R.string.boc_channel_bank_home);
                break;
            case "5"://微信银行
                channel = context.getString(R.string.boc_channel_bank_wechat);
                break;
            case "6"://银企对接
                channel = context.getString(R.string.boc_channel_bank_com);
                break;
            default:
                break;
        }
        return channel;
    }


    /**
     * 获取钞汇信息
     *
     * @param cashRemit
     * @return
     */
    public static String getStrCashRemit(Context context, String cashRemit) {
        String strCashRemit = "";
        if (StringUtils.isEmpty(cashRemit)) {
            return strCashRemit;
        }
        if ("01".equals(cashRemit)) {//  /钞
            strCashRemit = context.getString(R.string.boc_result_status_cash);
        } else if ("02".equals(cashRemit)) {//  /汇
            strCashRemit = context.getString(R.string.boc_result_status_remit);
        }
        return strCashRemit;
    }


    /**
     * 根据状态修改背景颜色
     */
    public static int changeTextBackground(String status) {
        int resId;
        if (status == null) {
            return R.drawable.boc_transaction_status_bg_yellow;
        }
        switch (status) {
            case "交易成功":
            case "提交成功":
            case "取款成功":
            case "已成功取现":
            case "银行已受理":
            case "已付":
            case "已取现":
            case "成功":
                resId = R.drawable.boc_transaction_status_bg_green;
                break;
            case "交易失败":
            case "已删除":
            case "已撤销":
            case "已锁定":
            case "状态未明":
            case "过期未付":
            case "已取消取现":
            case "永久锁定":
            case "密码错3次锁定":
            case "失败":
                resId = R.drawable.boc_transaction_status_bg_red;
                break;
            default:
                resId = R.drawable.boc_transaction_status_bg_yellow;
                break;
        }
        return resId;
    }

    /**
     * 根据状态修改文字颜色
     */
    public static int changeTextBackground(Context context, String status) {
        int resId;
        if (status == null) {
            return context.getResources().getColor(R.color.boc_text_color_yellow);
        }
        switch (status) {
            case "交易成功":
            case "提交成功":
            case "取款成功":
            case "已成功取现":
            case "银行已受理":
            case "已付":
            case "已取现":
            case "成功":
                resId = context.getResources().getColor(R.color.boc_text_color_green);
                break;
            case "交易失败":
            case "已删除":
            case "已撤销":
            case "已锁定":
            case "状态未明":
            case "过期未付":
            case "已取消取现":
            case "永久锁定":
            case "密码错3次锁定":
            case "失败":
                resId = context.getResources().getColor(R.color.boc_text_color_red);
                break;
            default:
                resId = context.getResources().getColor(R.color.boc_text_color_yellow);
                break;
        }
        return resId;
    }

    /**
     * 改变文字颜色
     *
     * @param status
     * @return
     */
    public static int changeTextColor(Context context, String status) {
        int color;
        if (("失败").equals(status) || ("不可撤单").equals(status)) {
            color = context.getResources().getColor(R.color.boc_text_color_red);
        } else {
            color = context.getResources().getColor(R.color.boc_text_color_money_count);
        }
        return color;
    }

    /**
     * 改变文字颜色
     * added by wangf on 2016-11-7 20:30:47
     * @param status
     * @return
     */
    public static int changeQrpayTextColor(Context context, String status) {
        int color;
        if (("成功").equals(status)) {
            color = context.getResources().getColor(R.color.boc_text_color_green);
        } else if (("失败").equals(status)){
            color = context.getResources().getColor(R.color.boc_text_color_red);
        }else {
            color = context.getResources().getColor(R.color.boc_text_color_yellow);
        }
        return color;
    }
}
