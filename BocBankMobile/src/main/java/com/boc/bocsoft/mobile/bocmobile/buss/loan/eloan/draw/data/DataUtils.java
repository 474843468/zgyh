package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.data;

import android.content.Context;
import android.widget.BaseAdapter;

import com.boc.bocsoft.mobile.bocmobile.base.widget.DistrictSelect.GetAddressAdater;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xintong on 2016/6/7.
 */
public class DataUtils {
    private Context mContext;
    private BaseAdapter adapter1;
    private BaseAdapter adapter2;
    private BaseAdapter adapter3;
    private BaseAdapter adapter4;

    public DataUtils(Context addressSelect) {
        super();
        this.mContext = addressSelect;
    }

    public static List<String> getDataTimeList(){
        List<String> stringList = new ArrayList<String>();
        stringList.add("1个月");
        stringList.add("2个月");
        stringList.add("3个月");
        stringList.add("4个月");
        stringList.add("5个月");
        stringList.add("6个月");
        stringList.add("7个月");
        stringList.add("8个月");
        stringList.add("9个月");
        stringList.add("10个月");
        stringList.add("11个月");
        stringList.add("12个月");
        return stringList;
    }
    public static List<String> getDataAccountList(){
        List<String> stringList = new ArrayList<String>();
        stringList.add(NumberUtils.formatCardNumber("6288253756898855"));
        stringList.add(NumberUtils.formatCardNumber("6136698329562542"));
        stringList.add(NumberUtils.formatCardNumber("6675425545129896"));
        stringList.add(NumberUtils.formatCardNumber("6786656986235656"));
        return stringList;
    }
    public static List<String> getDataUseOfFundList(){
        List<String> stringList = new ArrayList<String>();
        stringList.add("日常消费");
        stringList.add("教育");
        stringList.add("装修");
        stringList.add("医疗");
        stringList.add("购车");
        stringList.add("旅游");
        stringList.add("婚嫁");
        return stringList;
    }
    public static List<String> getRelationShipList(){
        List<String> stringList = new ArrayList<String>();
        stringList.add("父母");
        stringList.add("配偶");
        stringList.add("兄弟");
        stringList.add("姐妹");
        return stringList;
    }

    public static List<String> getPrepayTypeList(){
        List<String> stringList = new ArrayList<String>();
        stringList.add("全额提前还款");
        stringList.add("部分提前还款");
        return stringList;
    }

    /**
     * (id:name)例：
     * 0:虚拟
     * 4:USBKey证书
     * 8:动态口令令牌
     * 32:短信认证码
     * 12:USBKey证书+动态口令令牌
     * 36:USBKey证书+短信认证码
     * 40:动态口令令牌+短信认证码
     * @return
     */
    public static Map<String, String> getSecurityVerityTypeList(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("0","虚拟");//
        map.put("4","中银e盾");
        map.put("8","动态口令");
        map.put("32","手机交易码");
        map.put("12","USBKey证书+动态口令令牌");//
        map.put("36","USBKey证书+短信认证码");//
        map.put("40","动态口令 + 手机交易码");

        map.put("96","手机交易码");//新加
        return map;
    }


}
