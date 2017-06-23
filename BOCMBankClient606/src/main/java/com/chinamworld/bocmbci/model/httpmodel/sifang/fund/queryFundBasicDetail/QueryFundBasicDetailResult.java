package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.queryFundBasicDetail;

import java.util.List;

/**
 * Created by Administrator on 2016/10/27 0027.
 * 基金详情接口（基本信息）接口返回字段
 */
public class QueryFundBasicDetailResult {

        private String fundId;//	基金Id
        private String fundBakCode;//	基金公共代码
        private String fundName;//	基金名称
        private String dBuyStart;//	起购金额
        private String transCurrency;//	交易币种
        private String fundCompanyCode;//	基金公司代码
        private String fundCompany;//	基金公司名称
        private String collectFeeWay;//	收费方式
        private String productFoundDate;//	产品成立日期
        private String dwjz;//	单位净值
        private String jzTime;//	单位净值时间
        private String levelOfRisk;//	风险级别
        private String gradeOrg;//	评级机构
        private String gradeLevel;//	评级级别
        private String strDataStand;//	评级跨度数据
        private String preferential;//	优惠
//        private String sdtEnd;//	会计期间截止
//        private String mFundNetAsset;//	期末基金资产净值(元)
//        private String sdtPeriod;//	截止日期
//        private String mZiChanJingZhi;//	基金资产净值（元）
//        private String strSType;//	基金类型1
        private String currPercentDiff;//	日涨跌幅
        private String yieldOfWeek;//	七日年化收益率
        private String yieldOfTenThousand;//	万分收益
        private String applyBuyFeeLow;//	起购金额
        private String productType;//	产品种类
        private String sgBuyStart;//	申购起购金额
        private String rgBuyStart;//	认购起购金额
        private String shBuyStart;//	赎回起购金额
        private String fundScale;//基金规模

        public String getFundScale() {
                return fundScale;
        }

        public String getGradeOrg() {
                return gradeOrg;
        }

        public void setGradeOrg(String gradeOrg) {
                this.gradeOrg = gradeOrg;
        }

        public String getGradeLevel() {
                return gradeLevel;
        }

        public void setGradeLevel(String gradeLevel) {
                this.gradeLevel = gradeLevel;
        }

        public String getCurrPercentDiff() {
                return currPercentDiff;
        }

        public void setCurrPercentDiff(String currPercentDiff) {
                this.currPercentDiff = currPercentDiff;
        }

        public String getdBuyStart() {
                return dBuyStart;
        }

        public void setdBuyStart(String dBuyStart) {
                this.dBuyStart = dBuyStart;
        }

        private List<FeeRatioListItem> feeRatioList;

        public List<FeeRatioListItem> getFeeRatioList() {
                return feeRatioList;
        }

        public void setFeeRatioList(List<FeeRatioListItem> feeRatioList) {
                this.feeRatioList = feeRatioList;
        }

        public class FeeRatioListItem {
                private String strExpType;//	费用类别
                private String chargeType;//	收费类型
                private String mapplyLowerLimit;//	申(认)购金额下限(元)
                private String mapplyUpperLimit;//	申(认)购金额上限(元)
                private String feeRatio;//	对应费率
                private String mAmount;//	对应金额
                private String iholdLowerLimit;//	持有时间下限(天)
                private String iholdUpperLimit;//	持有时间上限(天)

                public String getStrExpType() {
                        return strExpType;
                }

                public String getFeeRatio() {
                        return feeRatio;
                }
        }
}
