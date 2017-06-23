package com.chinamworld.bocmbci.biz.finc.finc_p606.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.jzTendency.JzTendencyResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.ljYieldRateTendency.LjYieldRateTendencyResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.rankTendency.RankTendencyResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.yieldOfWanFenTendency.YieldOfWanFenTendencyResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.yieldOfWeekTendency.YieldOfWeekTendencyResult;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/29 0029.
 * 0：等于0 1：小于0 2：大于0
 */
public class FincUtil {

    /**
     * 把数字转换成百分数
     * @param cent
     * @return
     */
    public static String setCent(String cent){
        if(StringUtil.isNullOrEmpty(cent)){
            cent = "--";
        }else{
            cent = StringUtil.append2Decimals(Double.parseDouble(cent)*100+"",4)+"%";
        }
        return cent;
    }

    /**
     * 把数字转换成百分数 不带%
     * @param cent
     * @return
     */
    public static String setCent2(String cent){
        if(StringUtil.isNullOrEmpty(cent)){
            cent = "--";
        }else{
            cent = StringUtil.append2Decimals(Double.parseDouble(cent)*100+"",4);
        }
        return cent;
    }

    /**
     *
     * @param tv1 文本
     * @param text true:字体显示黑色 false：字体显示红色
     * @param img 下划线
     * @param imgVisible true: 隐藏底部图标 false 显示图标
     */
    public static void setVisibilityAndTextColor(Context context, TextView tv1, boolean text, ImageView img, boolean imgVisible){
        tv1.setTextColor(text ? context.getResources().getColor(R.color.fonts_dark_gray):
                context.getResources().getColor(R.color.boc_text_color_red));
        img.setVisibility(imgVisible ? View.INVISIBLE : View.VISIBLE);
    }

    /**
     * 设置图片宽高 先把px转换成dp
     * @param width
     * @param heigth
     */
    public static void setImageHeigthAndWidth(Context context,ImageView imageView,int width,int heigth){
        float scale = context.getResources().getDisplayMetrics().density;
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.height = (int)(heigth*scale+0.5f);
        params.width = (int)(width*scale+0.5f);
        imageView.setLayoutParams(params);
    }


    private static String CASHFLAG = "cashflag";
    private static String TOTALVALUE = "totalValue";

    /**
     * 获得参考市值
     * @param accbalancList
     * @return
     */
    public static String getTotalValueList(List<Map<String, Object>> accbalancList) {
        String fundTotalValue = "";
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Map<String, Object> map : accbalancList) {
            Map<String, String> fundInfoMap = (Map<String, String>) map
                    .get(Finc.FINC_FUNDINFO_REQ);
            String currency = fundInfoMap.get(Finc.FINC_CURRENCY);
            String cashRemit = fundInfoMap.get(Finc.FINC_CASHFLAG);
            Double totalValue;
            if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {// 币种为人民币时
                boolean isadd = true;
                totalValue = Double.valueOf((String)map
                        .get(Finc.FINC_CURRENTCAPITALISATION_REQ));
                for (Map<String, String> resultmap : list) {
                    if (resultmap.get(CASHFLAG).equals(
                            LocalData.Currency.get(currency))) {// 里面包含人民币
                        totalValue += Double.valueOf(resultmap.get(TOTALVALUE));
                        isadd = false;
                        resultmap.remove(TOTALVALUE);
                        resultmap.put(TOTALVALUE, String.valueOf(totalValue));
                    }
                }
                if(isadd){// 如果不包含人民币
                    Map<String, String> newMap = new HashMap<String, String>();
                    newMap.put(CASHFLAG, LocalData.Currency.get(currency));
                    newMap.put(TOTALVALUE, String.valueOf(totalValue));
                    list.add(newMap);
                }
            } else {
                boolean isadd = true;
                totalValue = Double.valueOf((String)map
                        .get(Finc.FINC_CURRENTCAPITALISATION_REQ));
                for (Map<String, String> resultmap : list) {
                    if (resultmap.get(CASHFLAG).equals(
                            LocalData.Currency.get(currency)
                                    + LocalData.CurrencyCashremit
                                    .get(cashRemit))) {
                        isadd = false;
                        totalValue += Double.valueOf(resultmap.get(TOTALVALUE));
                        resultmap.remove(TOTALVALUE);
                        resultmap.put(TOTALVALUE, String.valueOf(totalValue));
                    }
                }
                if(isadd){// 如果不包含人民币
                    isadd = true;
                    Map<String, String> newMap = new HashMap<String, String>();
                    newMap.put(CASHFLAG, LocalData.Currency.get(currency)
                            + LocalData.CurrencyCashremit.get(cashRemit));
                    newMap.put(TOTALVALUE, String.valueOf(totalValue));
                    list.add(newMap);
                }
            }
        }
        for(Map map :list){
            if(!StringUtil.isNullOrEmpty(map.get(CASHFLAG))&&
                    "人民币元".equals((String)map.get(CASHFLAG))){
                fundTotalValue = StringUtil.parseStringPattern((String)map.get(TOTALVALUE), 2);
            }
        }
        return fundTotalValue;

    }

    /**
     * 把四方历史净值数据转换成List<Map<String, Object>>
     * @param itemList
     * @return
     */
    public static List<Map<String, Object>> siFangListToList(List<JzTendencyResult.JzTendencyItem> itemList){
        List<Map<String, Object>> list = new ArrayList<>();
        for(JzTendencyResult.JzTendencyItem item : itemList){
            Map map = new HashMap();
            map.put("jzTime",item.getJzTime());
            map.put("dwjz",item.getDwjz());
            map.put("ljjz",item.getLjjz());
            map.put("currPercentDiff",item.getCurrPercentDiff());
            list.add(map);
        }
        return list;
    }

    /**
     * 把四方累计收益数据转换成List<Map<String, Object>>
     * @param
     * @return
     */
    public static List<Map<String, Object>> siFangListToList2(List<LjYieldRateTendencyResult.LjYieldRateTendencyItem> itemList){
        List<Map<String, Object>> list = new ArrayList<>();
        for(LjYieldRateTendencyResult.LjYieldRateTendencyItem item : itemList){
            Map map = new HashMap();
            map.put("jzTime",item.getJzTime());
            map.put("ljYieldRate",item.getLjYieldRate());
            map.put("szzjYieldRate",item.getSzzjYieldRate());
            map.put("yjbjjzYieldRate",item.getYjbjjzYieldRate());
            map.put("sectionDepositRate",item.getSectionDepositRate());
            list.add(map);
        }
        return list;
    }

    /**
     * 把四方排名变化数据转换成List<Map<String, Object>>
     * @param itemList
     * @return
     */
    public static List<Map<String, Object>> siFangListToList3(List<RankTendencyResult.RankTendencyItem> itemList){
        List<Map<String, Object>> list = new ArrayList<>();
        for(RankTendencyResult.RankTendencyItem item : itemList){
            Map map = new HashMap();
            map.put("jzTime",item.getJzTime());
            map.put("rankScore",item.getRankScore());
            map.put("rank",item.getRank());
            map.put("yieldSection",item.getYieldSection());
            list.add(map);
        }
        return list;
    }

    /**
     * 把四方七日年化收益数据转换成List<Map<String, Object>>
     * @param itemList
     * @return
     */
    public static List<Map<String, Object>> siFangListToList4(List<YieldOfWeekTendencyResult.YieldOfWeekTendencyItem> itemList){
        List<Map<String, Object>> list = new ArrayList<>();
        for(YieldOfWeekTendencyResult.YieldOfWeekTendencyItem item : itemList){
            Map map = new HashMap();
            map.put("date",item.getDate());
            map.put("yieldOfWeek",item.getYieldOfWeek());
            list.add(map);
        }
        return list;
    }

    /**
     * 把四方万份收益数据转换成List<Map<String, Object>>
     * @param itemList
     * @return
     */
    public static List<Map<String, Object>> siFangListToList5(List<YieldOfWanFenTendencyResult.YieldOfWanFenTendencyItem> itemList){
        List<Map<String, Object>> list = new ArrayList<>();
        for(YieldOfWanFenTendencyResult.YieldOfWanFenTendencyItem item : itemList){
            Map map = new HashMap();
            map.put("date",item.getDate());
            map.put("yieldOfTenThousand",item.getYieldOfTenThousand());
            list.add(map);
        }
        return list;
    }

    /**
     * 转换数值，为空返回--，用于数字类型的显示
     * @param value
     * @return
     */
    public static String setvalue(String value){
        return StringUtil.isNullOrEmpty(value)||"-".equals(value)?"--":value;
    }

    /**
     * 把string转换成float 小数部分小于0.5返回整数部分，大于0.5返回整数+1 等于0.5直接转换成float
     * @param s s不能为空，使用前需判断
     * @return
     */
    public static float getStarNum(String s){
        if(s.contains(".")){
            String[] a= s.split("\\.");
            int a1 = Integer.parseInt(a[0]);
            int a2 = Integer.parseInt(a[1]);
            if(a2>5){
                a1++;
                return a1*1.0f;
            }else if(a2<5){
                return a1*1.0f;
            }
        }
        return Float.parseFloat(s);
    }

    protected static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    protected static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
    /**
     * 日期格式yyyy-MM-dd转换成yyyy/mm/dd
     * @param dateString
     * @return
     */
    public static String getDateFormat(String dateString) {
        if(StringUtil.isNullOrEmpty(dateString)){
            return dateString;
        }
        try {
            Date date = sdf.parse(dateString);
            return sdf2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    /**
     * 日期格式yyyymmdd转换成yyyy\nmm-dd
     * @param dateTime
     * @return
     */
    public static String getcurrentDate(String dateTime) {
        if(StringUtil.isNullOrEmpty(dateTime)){
            return dateTime;
        }
        if(dateTime.length() == 8){
            return dateTime.substring(0,4)+"-"+dateTime.substring(4,6)+"-"+dateTime.substring(6);
        }
        return dateTime;
    }

    /**
     * 获得drawable图片
     * @param context
     * @param drawableId
     * @return
     */
    public static Drawable getDrawable(Context context,int drawableId){
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        return drawable;
    }
}
