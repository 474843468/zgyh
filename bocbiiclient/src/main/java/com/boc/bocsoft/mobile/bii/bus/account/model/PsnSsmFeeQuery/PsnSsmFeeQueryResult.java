package com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmFeeQuery;

import java.util.List;

/**
 * Created by wangtong on 2016/6/25.
 */
public class PsnSsmFeeQueryResult {

    private List<MaplistBean> Maplist;

    public List<MaplistBean> getMaplist() {
        return Maplist;
    }

    public void setMaplist(List<MaplistBean> Maplist) {
        this.Maplist = Maplist;
    }

    public static class MaplistBean {
        private String feestandard;
        private String feetype;

        public String getFeestandard() {
            return feestandard;
        }

        public void setFeestandard(String feestandard) {
            this.feestandard = feestandard;
        }

        public String getFeetype() {
            return feetype;
        }

        public void setFeetype(String feetype) {
            this.feetype = feetype;
        }
    }
}
