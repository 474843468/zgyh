package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanProductBean;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

/**
 * Created by XieDu on 2016/7/21.
 */
public class LoanTypeListAdapter extends BaseListAdapter<OnLineLoanProductBean> {

    public LoanTypeListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EditChoiceWidget itemView = convertView == null ? new EditChoiceWidget(mContext)
                : (EditChoiceWidget) convertView;
        RelativeLayout.LayoutParams lp =
                (RelativeLayout.LayoutParams) itemView.getChoiceNameTextView().getLayoutParams();
        lp.width= ViewGroup.LayoutParams.WRAP_CONTENT;
        itemView.getChoiceNameTextView().setLayoutParams(lp);
        OnLineLoanProductBean item = getItem(position);
        itemView.setChoiceTextName(item.getProductName());
        return itemView;
    }
}
