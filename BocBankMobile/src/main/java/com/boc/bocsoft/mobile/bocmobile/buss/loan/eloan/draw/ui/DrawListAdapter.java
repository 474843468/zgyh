package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.ui;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListAdapter;


/**
 * 贷款管理-中银E贷-弹出框内列表适配器
 * Created by xintong on 2016/6/7.
 */
public class DrawListAdapter extends SelectListAdapter {

    public DrawListAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public String displayValue(Object model) {
        return model.toString();
    }
}
