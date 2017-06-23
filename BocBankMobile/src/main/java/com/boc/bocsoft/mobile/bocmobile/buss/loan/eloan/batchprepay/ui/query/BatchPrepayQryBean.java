package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.ui.query;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

/**
 * Created by liuzc on 2016/8/2.
 */
public class BatchPrepayQryBean  {
    private boolean isSelected; //是否选中
    private PsnLOANListEQueryResult.PsnLOANListEQueryBean bean;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public PsnLOANListEQueryResult.PsnLOANListEQueryBean getBean() {
        return bean;
    }

    public void setBean(PsnLOANListEQueryResult.PsnLOANListEQueryBean bean) {
        this.bean = bean;
    }
}
