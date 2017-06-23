package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails;

import java.text.DecimalFormat;

/**
 * Created by zc7067 on 2016/11/17.
 *双向宝代码转化成对应汉字
 */
public class TransQueryUtils {

//    protected Context mContext;

    //委托类型:MI=市价即时,LI=限价即时,PO=获利委托,SO=止损委托,OO=二选一委托,IO=追加委托,TO=连环委托,MO=多选一委托,CO=委托撤单,FO=追击止损挂单,
    public String getExchangeTranType(String exchangeTranType){
        String exchangeTranTypes = "";
        if("MI".equals(exchangeTranType)){
            exchangeTranTypes = "市价即时";
        }else if("LI".equals(exchangeTranType)){
            exchangeTranTypes = "限价即时";
        }else if("PO".equals(exchangeTranType)){
            exchangeTranTypes = "获利委托";
        }else if("SO".equals(exchangeTranType)){
            exchangeTranTypes = "止损委托";
        }else if("OO".equals(exchangeTranType)){
            exchangeTranTypes = "二选一委托";
        }else if("IO".equals(exchangeTranType)){
            exchangeTranTypes = "追加委托";
        }else if("TO".equals(exchangeTranType)){
            exchangeTranTypes = "连环委托";
        }else if("MO".equals(exchangeTranType)){
            exchangeTranTypes = "多选一委托";
        }else if("CO".equals(exchangeTranType)){
            exchangeTranTypes = "委托撤单";
        }else if("FO".equals(exchangeTranType)){
            exchangeTranTypes = "追击止损挂单";
        }
        return exchangeTranTypes;
    }
    //交易状态:N=有效,U=未生效,S=成交,R=成交,C=撤销,E=过期,O=选择失效,X=失败
    public String getOrderStatusList(String orderStatus){
        String status = "";
        if("N".equals(orderStatus)){
            status = "有效";
        }else if("U".equals(orderStatus)){
            status = "未生效";
        }else if("S".equals(orderStatus)){
            status = "";
        }else if("R".equals(orderStatus)){
            status = "";
        }else if("C".equals(orderStatus)){
            status = "已撤销";
        }else if("E".equals(orderStatus)){
            status = "已过期";
        }else if("O".equals(orderStatus)){
            status = "选择失效";
        }else if("X".equals(orderStatus)){
            status = "失败";
        }
        return status;
    }
    //交易状态:N=有效,U=未生效,S=成交,R=成交,C=撤销,E=过期,O=选择失效,X=失败
    public String getOrderStatusDetail(String orderStatus){
        String status = "";
        if("N".equals(orderStatus)){
            status = "有效";
        }else if("U".equals(orderStatus)){
            status = "未生效";
        }else if("S".equals(orderStatus)){
            status = "成交";
        }else if("R".equals(orderStatus)){
            status = "成交";
        }else if("C".equals(orderStatus)){
            status = "已撤销";
        }else if("E".equals(orderStatus)){
            status = "已过期";
        }else if("O".equals(orderStatus)){
            status = "选择失效";
        }else if("X".equals(orderStatus)){
            status = "失败";
        }
        return status;
    }
    //转账类型:TF=资金转账,PL=损益结转保证金,TD=交易利息结转保证金
    public String getFundTransferType(String fundTransferType){
        String transferType = "";
        if("TF".equals(fundTransferType)){
            transferType = "资金转账";
        }else if("PL".equals(fundTransferType)){
            transferType = "损益结转保证金";
        }else if("TD".equals(fundTransferType)){
            transferType = "交易利息结转保证金";
        }
        return transferType;
    }
    //买卖方向:B=买入,S=卖出
    public String getBuyDirection(String direction){
        String dir = "";
        if ("B".equals(direction)){
            dir = "买入";
        }else if ("S".equals(direction)){
            dir = "卖出";
        }
        return dir;
    }
    //第一成交类型 :P=获利,S=止损,追击止损挂单时为S
    public String getSuccessType(String successtype){
        String type = "";
        if ("P".equals(successtype)){
            type = "获利";
        }else if("S".equals(successtype)){
            type = "止损";
        }
        return type;

    }

    //O –建仓, N –优先平仓（根据业务需求，此字段前端展示为先开先平）,C –指定平仓,L–司法强制处理,S –系统斩仓
    public String getopenPositionFlag(String Flag){
        String positionFlag = "";
        if("O".equals(Flag)){
            positionFlag = "建仓";
        }else if ("N".equals(Flag)){
            positionFlag = "先开先平";
        }else if ("C".equals(Flag)){
            positionFlag = "指定平仓";
        }else if ("L".equals(Flag)){
            positionFlag = "司法强制处理";
        }else if ("S".equals(Flag)){
            positionFlag = "系统斩仓";
        }
        return positionFlag;
    }

    //货币对判断方法
    public String getCurrencyUnit(String currency1,String currency2){
        String unit = "";
        if ("035".equals(currency1)&&"001".equals(currency2)) {
            unit = " 克";
        }else if ("068".equals(currency1)&&"001".equals(currency2)){
            unit = " 克";
        }else if ("034".equals(currency1)&&"014".equals(currency2)){
            unit = " 盎司";
        }else if ("036".equals(currency1)&&"014".equals(currency2)){
            unit = " 盎司";
        }else if ("845".equals(currency1)&&"001".equals(currency2)){
            unit = " 克";
        }else if ("844".equals(currency1)&&"001".equals(currency2)){
            unit = " 克";
        }else if ("841".equals(currency1)&&"014".equals(currency2)){
            unit = " 盎司";
        }else if ("045".equals(currency1)&&"014".equals(currency2)){
            unit = " 盎司";
        }else{
            unit = currency1;
        }
        return unit;
    }
    //获取详情页货币名称
    public String getCurrencyType(String currency){
        String currencyType = "";
        if ("035".equals(currency)||"034".equals(currency)){
            currencyType = " (黄金)";
        }else if ("068".equals(currency)||"036".equals(currency)){
            currencyType = " (白银)";
        }else if ("845".equals(currency)||"045".equals(currency)){
            currencyType = " (铂金)";
        }else if ("844".equals(currency)||"841".equals(currency)){
            currencyType = " (钯金)";
        }else {
            currencyType ="";
        }
        return currencyType;
    }
    //I-转入，O-转出
    public String gettransferDir(String transferDir){
        String  dir="";
        if("O".equals(transferDir)){
            dir = "转出";
        }else if ("I".equals(transferDir)) {
            dir = "转入";
        }
        return dir;
    }

    //00-现钞，01-现汇
    public String getcashRemit(String cashRemit){
        String car="";
        if("01".equals(cashRemit)){
            car = "现钞";
        }else if ("02".equals(cashRemit)) {
            car = "现汇";
        }
        return car;
    }

    //处理数字保留的小数位数
    public String getBases(String rate){
        String ss = "";
        DecimalFormat decimalFormat = new DecimalFormat("#.0000");
        ss = decimalFormat.format(rate);
        return ss;
    }
}
