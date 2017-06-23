package com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQuerySystemDateTime;

import org.threeten.bp.LocalDateTime;

/**
 * Created by feibin on 2016/6/28.
 * I00公共接口 3.2 002 PsnCommonQuerySystemDateTime获取服务器时间
 */
public class PsnCommonQuerySystemDateTimeResult {
    public LocalDateTime getDateTme() {
        return dateTme;
    }

    public void setDateTme(LocalDateTime dateTme) {
        this.dateTme = dateTme;
    }

    private LocalDateTime dateTme;
}
