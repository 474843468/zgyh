package com.chinamworld.bocmbci.biz.finc.finc_p606;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/26 0026.
 */
public class FincDataCenter {

    //产品类型
    public static Map<String, String> fincFntypeStr = new HashMap<String, String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            put("01", "理财型");
            put("02", "QDII");
            put("03", "ETF");
            put("04", "保本型");
            put("05", "指数型");
            put("06", "货币型");
            put("07", "股票型");
            put("08", "债券型");
            put("09", "混合型");
            put("10", "其他");

        }
    };

    //产品类型
    public static Map<String, String> fincFntypeCode = new HashMap<String, String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            put("理财型", "01");
            put("QDII",  "02");
            put( "ETF",  "03");
            put( "保本型","04");
            put( "指数型","05");
            put("货币型","06");
            put("股票型","07");
            put("债券型","08");
            put("混合型","09");
            put("其他","10");

        }
    };
    //风险级别
    public static Map<String, String> fincRisklvStr = new HashMap<String, String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            put("1", "低风险");
            put("2", "中低风险");
            put("3", "中风险");
            put("4", "中高风险");
            put("5", "高风险");

        }
    };

    //产品类型
    public static Map<String, String> fincHistoryTypeStr = new HashMap<String, String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            put("1", "历史净值");
            put("2", "历史累计收益率");
            put("3", "历史排名");
            put("4", "历史七日年化收益率");
            put("5", "历史万份收益");

        }
    };
}
