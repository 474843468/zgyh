package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.emptyview.CommonEmptyView;


/**
 * Created by cry7096 on 2016/11/28.
 * 现金分期选张账户空fragment
 */
public class CashNoAccountFragment extends BussFragment {
    private View rootView;
    /**
     * 空界面显示图片和文案，文案可以点击跳转
     */
    private CommonEmptyView view_no_data;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_cash_no_account, null);
        return rootView;
    }

    @Override
    public void initView() {
        view_no_data = (CommonEmptyView) rootView.findViewById(R.id.view_no_data);
    }

    @Override
    public void initData(){
        view_no_data.setEmptyTips(R.drawable.boc_cash_installment_no_account, 1, getResources().getString(R.string.boc_crcd_empty_view_text),
                getResources().getString(R.string.boc_crcd_empty_view_phone));
    }

    @Override
    public void setListener() {
        view_no_data.setTextOnclickListener(new CommonEmptyView.TextOnclickListener() {//点我去看看点击事件
            @Override
            public void textOnclickListener() {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4006695566"));
                CashNoAccountFragment.this.startActivity(intent);
            }
        });
    }

    /**
     * 是否显示右侧标题按钮
     */
    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    /**
     * 是否显示红头
     *
     * @return
     */
    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    /**
     * 设置标题
     */
    @Override
    protected String getTitleValue() {
        return getContext().getString(R.string.boc_crcd_cash_installment_title);
    }

}
