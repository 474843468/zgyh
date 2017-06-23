package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardTradeDetailModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * Name: liukai
 * Time：2016/12/5 9:13.
 * Created by lk7066 on 2016/12/5.
 * It's used to 附属卡交易明细详情页面
 */

public class AttCardTranDetailFragment extends BussFragment{

    private View mRootView = null;
    private DetailTableHead detailTableHead;
    private DetailContentView detailContentView;
    private AttCardTradeDetailModel.ListBean listBean;
    private String masterCrcdNum;
    private String subCrcdNum;

    @Override
    protected View onCreateView(LayoutInflater mInflater){
        super.onCreateView(mInflater);
        mRootView = mInflater.inflate(R.layout.fragment_attcard_tranflow_detail, null);
        return mRootView;
    }

    @Override
    public void initView() {
        detailTableHead = (DetailTableHead) mRootView.findViewById(R.id.attcard_head_view);
        detailContentView = (DetailContentView) mRootView.findViewById(R.id.attcard_body_view);
    }

    @Override
    public void initData() {
        listBean = getArguments().getParcelable("detail");
        masterCrcdNum = getArguments().getString("crcdNum");
        subCrcdNum = getArguments().getString("subNum");
        loadData();
    }

    public void loadData(){

        if(listBean.getDebitCreditFlag().equals("CRED")){
            detailTableHead.updateData(getResources().getString(R.string.boc_crcd_attcard_detail_bookamount2) + "(" + PublicCodeUtils.getCurrency(mContext, listBean.getBookCurrency()) + ")", listBean.getBookAmount());
        } else {
            detailTableHead.updateData(getResources().getString(R.string.boc_crcd_attcard_detail_bookamount1) + "(" + PublicCodeUtils.getCurrency(mContext, listBean.getBookCurrency()) + ")", listBean.getBookAmount());

        }
        detailTableHead.addDetail(getResources().getString(R.string.boc_crcd_attcard_detail_transdate), listBean.getTransDate());

        detailContentView.addDetail(getResources().getString(R.string.boc_crcd_attcard_crcd_no), NumberUtils.formatCardNumber(masterCrcdNum));
        detailContentView.addDetail(getResources().getString(R.string.boc_crcd_attcard_attcard_no), NumberUtils.formatCardNumber(subCrcdNum));
        detailContentView.addDetail(getResources().getString(R.string.boc_crcd_attcard_detail_bookdate), listBean.getBookDate());
        detailContentView.addDetail(getResources().getString(R.string.boc_crcd_attcard_detail_transcode), listBean.getTransCode());
        detailContentView.addDetail(getResources().getString(R.string.boc_crcd_attcard_detail_remark), listBean.getRemark());
        detailContentView.addDetail(getResources().getString(R.string.boc_crcd_attcard_detail_transamount), PublicCodeUtils.getCurrency(mContext, listBean.getTranCurrency()) + listBean.getTranAmount());

    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_crcd_attcard_history_title);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
