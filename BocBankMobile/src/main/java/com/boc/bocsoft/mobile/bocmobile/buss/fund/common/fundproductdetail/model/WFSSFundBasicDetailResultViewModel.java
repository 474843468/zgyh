package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 登陆前基金详情返回-四方数据
 * Created by lzc4524 on 2016/11/25.
 */
public class WFSSFundBasicDetailResultViewModel implements Parcelable {
    /**
     * fundId : 380001
     * productFoundDate : null
     * feeRatioList : [{"strExpType":"认购费","chargeType":null,"mapplyLowerLimit":null,"mapplyUpperLimit":null,"feeRatio":0,"mAmount":null,"iholdLowerLimit":null,"iholdUpperLimit":null},{"strExpType":"申购费","chargeType":null,"mapplyLowerLimit":null,"mapplyUpperLimit":null,"feeRatio":0,"mAmount":null,"iholdLowerLimit":null,"iholdUpperLimit":null},{"strExpType":"赎回费","chargeType":null,"mapplyLowerLimit":null,"mapplyUpperLimit":null,"feeRatio":0,"mAmount":null,"iholdLowerLimit":null,"iholdUpperLimit":null},{"strExpType":"销售服务费","chargeType":null,"mapplyLowerLimit":null,"mapplyUpperLimit":null,"feeRatio":0.3,"mAmount":null,"iholdLowerLimit":null,"iholdUpperLimit":null}]
     * dwjz : 0
     * fundScale : null
     * fundCompanyCode : null
     * productType : null
     * levelOfRisk : 1
     * gradeOrg : 晨星中国
     * gradeLevel : 3
     * sgBuyStart : 1000
     * fundName : 中银14天债A
     * shBuyStart : null
     * rgBuyStart : 1000
     * collectFeeway : null
     * fundBakCode : 380001
     * yieldOfTenThousand : 0.5865
     * yieldOfWeek : 2.305
     * currPercentDiff : 5.9E-5
     * fundCompany : 中银基金管理公司
     * transCurrency : CNY
     * jzTime : 20161117
     */

    private String fundId; //基金Id,可唯一代表一只基金
    private String productFoundDate; //产品成立日期
    private String dwjz; //单位净值
    private String fundScale; //基金规模
    private String fundCompanyCode; //基金公司代码
    private String productType; //产品种类
    private String levelOfRisk; //风险级别
    private String gradeOrg; //评级机构
    private String gradeLevel;//评级级别
    private String sgBuyStart;//申购起购金额
    private String fundName; //基金名称
    private String shBuyStart; //赎回起购金额
    private String rgBuyStart; //认购起购金额
    private String collectFeeway;//收费方式
    private String fundBakCode; //基金公共代码,用于向客户展示的代码
    private String yieldOfTenThousand; //万分收益
    private String yieldOfWeek;//七日年化收益率
    private String currPercentDiff; //日涨跌幅
    private String fundCompany;//基金公司名称
    private String transCurrency; //交易币种
    private String jzTime; //单位净值
    /**
     * strExpType : 认购费
     * chargeType : null
     * mapplyLowerLimit : null
     * mapplyUpperLimit : null
     * feeRatio : 0
     * mAmount : null
     * iholdLowerLimit : null
     * iholdUpperLimit : null
     */

    private List<FeeRatioListBean> feeRatioList; //对应费率

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getProductFoundDate() {
        return productFoundDate;
    }

    public void setProductFoundDate(String productFoundDate) {
        this.productFoundDate = productFoundDate;
    }

    public String getDwjz() {
        return dwjz;
    }

    public void setDwjz(String dwjz) {
        this.dwjz = dwjz;
    }

    public String getFundScale() {
        return fundScale;
    }

    public void setFundScale(String fundScale) {
        this.fundScale = fundScale;
    }

    public String getFundCompanyCode() {
        return fundCompanyCode;
    }

    public void setFundCompanyCode(String fundCompanyCode) {
        this.fundCompanyCode = fundCompanyCode;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getLevelOfRisk() {
        return levelOfRisk;
    }

    public void setLevelOfRisk(String levelOfRisk) {
        this.levelOfRisk = levelOfRisk;
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

    public String getSgBuyStart() {
        return sgBuyStart;
    }

    public void setSgBuyStart(String sgBuyStart) {
        this.sgBuyStart = sgBuyStart;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getShBuyStart() {
        return shBuyStart;
    }

    public void setShBuyStart(String shBuyStart) {
        this.shBuyStart = shBuyStart;
    }

    public String getRgBuyStart() {
        return rgBuyStart;
    }

    public void setRgBuyStart(String rgBuyStart) {
        this.rgBuyStart = rgBuyStart;
    }

    public String getCollectFeeway() {
        return collectFeeway;
    }

    public void setCollectFeeway(String collectFeeway) {
        this.collectFeeway = collectFeeway;
    }

    public String getFundBakCode() {
        return fundBakCode;
    }

    public void setFundBakCode(String fundBakCode) {
        this.fundBakCode = fundBakCode;
    }

    public String getYieldOfTenThousand() {
        return yieldOfTenThousand;
    }

    public void setYieldOfTenThousand(String yieldOfTenThousand) {
        this.yieldOfTenThousand = yieldOfTenThousand;
    }

    public String getYieldOfWeek() {
        return yieldOfWeek;
    }

    public void setYieldOfWeek(String yieldOfWeek) {
        this.yieldOfWeek = yieldOfWeek;
    }

    public String getCurrPercentDiff() {
        return currPercentDiff;
    }

    public void setCurrPercentDiff(String currPercentDiff) {
        this.currPercentDiff = currPercentDiff;
    }

    public String getFundCompany() {
        return fundCompany;
    }

    public void setFundCompany(String fundCompany) {
        this.fundCompany = fundCompany;
    }

    public String getTransCurrency() {
        return transCurrency;
    }

    public void setTransCurrency(String transCurrency) {
        this.transCurrency = transCurrency;
    }

    public String getJzTime() {
        return jzTime;
    }

    public void setJzTime(String jzTime) {
        this.jzTime = jzTime;
    }

    public List<FeeRatioListBean> getFeeRatioList() {
        return feeRatioList;
    }

    public void setFeeRatioList(List<FeeRatioListBean> feeRatioList) {
        this.feeRatioList = feeRatioList;
    }

    public static class FeeRatioListBean {
        private String strExpType; //费用类别
        private String chargeType; //收费类型
        private String mapplyLowerLimit; //申(认)购金额下限(元)
        private String mapplyUpperLimit; //申(认)购金额上限(元)
        private String feeRatio; //对应费率
        private String mAmount; //对应金额
        private String iholdLowerLimit; //持有时间下限(天)
        private String iholdUpperLimit; //持有时间上限(天)

        public String getStrExpType() {
            return strExpType;
        }

        public void setStrExpType(String strExpType) {
            this.strExpType = strExpType;
        }

        public String getChargeType() {
            return chargeType;
        }

        public void setChargeType(String chargeType) {
            this.chargeType = chargeType;
        }

        public String getMapplyLowerLimit() {
            return mapplyLowerLimit;
        }

        public void setMapplyLowerLimit(String mapplyLowerLimit) {
            this.mapplyLowerLimit = mapplyLowerLimit;
        }

        public String getMapplyUpperLimit() {
            return mapplyUpperLimit;
        }

        public void setMapplyUpperLimit(String mapplyUpperLimit) {
            this.mapplyUpperLimit = mapplyUpperLimit;
        }

        public String getFeeRatio() {
            return feeRatio;
        }

        public void setFeeRatio(String feeRatio) {
            this.feeRatio = feeRatio;
        }

        public String getMAmount() {
            return mAmount;
        }

        public void setMAmount(String mAmount) {
            this.mAmount = mAmount;
        }

        public String getIholdLowerLimit() {
            return iholdLowerLimit;
        }

        public void setIholdLowerLimit(String iholdLowerLimit) {
            this.iholdLowerLimit = iholdLowerLimit;
        }

        public String getIholdUpperLimit() {
            return iholdUpperLimit;
        }

        public void setIholdUpperLimit(String iholdUpperLimit) {
            this.iholdUpperLimit = iholdUpperLimit;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fundId);
        dest.writeString(this.productFoundDate);
        dest.writeString(this.dwjz);
        dest.writeString(this.fundScale);
        dest.writeString(this.fundCompanyCode);
        dest.writeString(this.productType);
        dest.writeString(this.levelOfRisk);
        dest.writeString(this.gradeOrg);
        dest.writeString(this.gradeLevel);
        dest.writeString(this.sgBuyStart);
        dest.writeString(this.fundName);
        dest.writeString(this.shBuyStart);
        dest.writeString(this.rgBuyStart);
        dest.writeString(this.collectFeeway);
        dest.writeString(this.fundBakCode);
        dest.writeString(this.yieldOfTenThousand);
        dest.writeString(this.yieldOfWeek);
        dest.writeString(this.currPercentDiff);
        dest.writeString(this.fundCompany);
        dest.writeString(this.transCurrency);
        dest.writeString(this.jzTime);
        dest.writeList(this.feeRatioList);
    }

    public WFSSFundBasicDetailResultViewModel() {
    }

    protected WFSSFundBasicDetailResultViewModel(Parcel in) {
        this.fundId = in.readString();
        this.productFoundDate = in.readString();
        this.dwjz = in.readString();
        this.fundScale = in.readString();
        this.fundCompanyCode = in.readString();
        this.productType = in.readString();
        this.levelOfRisk = in.readString();
        this.gradeOrg = in.readString();
        this.gradeLevel = in.readString();
        this.sgBuyStart = in.readString();
        this.fundName = in.readString();
        this.shBuyStart = in.readString();
        this.rgBuyStart = in.readString();
        this.collectFeeway = in.readString();
        this.fundBakCode = in.readString();
        this.yieldOfTenThousand = in.readString();
        this.yieldOfWeek = in.readString();
        this.currPercentDiff = in.readString();
        this.fundCompany = in.readString();
        this.transCurrency = in.readString();
        this.jzTime = in.readString();
        this.feeRatioList = new ArrayList<FeeRatioListBean>();
        in.readList(this.feeRatioList, FeeRatioListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<WFSSFundBasicDetailResultViewModel> CREATOR = new Parcelable.Creator<WFSSFundBasicDetailResultViewModel>() {
        @Override
        public WFSSFundBasicDetailResultViewModel createFromParcel(Parcel source) {
            return new WFSSFundBasicDetailResultViewModel(source);
        }

        @Override
        public WFSSFundBasicDetailResultViewModel[] newArray(int size) {
            return new WFSSFundBasicDetailResultViewModel[size];
        }
    };
}
