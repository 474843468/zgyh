package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.ui;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selectdialog.SelectDialog;

import java.util.List;

/**
 * @author wangyang
 *         2016/12/29 09:44
 */
public class PurchaseSelectDialog extends SelectDialog{

    private int type;

    public final static int SELECT_TYPE_TRANS = 1;
    public final static int SELECT_TYPE_CURRENCY = 2;


    public PurchaseSelectDialog(Context context) {
        super(context);
    }

    public void showDialog(List<String> list,int type){
        this.type = type;
        switch (this.type){
            case SELECT_TYPE_TRANS:
                setTitle(mContext.getString(R.string.boc_purchase_trans_type_choice));
                break;
            case SELECT_TYPE_CURRENCY:
                setTitle(mContext.getString(R.string.boc_purchase_currency_choice));
                break;
        }
        showDialog(list);
    }

    public String getItem(int position){
        return (String) adapter.getItem(position);
    }

    public int getType() {
        return type;
    }
}
