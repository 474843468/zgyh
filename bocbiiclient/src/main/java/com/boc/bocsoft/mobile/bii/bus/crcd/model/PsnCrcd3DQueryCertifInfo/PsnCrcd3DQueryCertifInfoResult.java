package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcd3DQueryCertifInfo;

/**
 * 3D安全认证信息查询
 * Created by wangf on 2016/11/22.
 */
public class PsnCrcd3DQueryCertifInfoResult {

    //开通标识 - “1”：开通 “0”：未开通
    private String openFlag;
    //令牌厂牌
    private String merchId;
    //令牌序号
    private String tokenId;

    public String getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(String openFlag) {
        this.openFlag = openFlag;
    }

    public String getMerchId() {
        return merchId;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
