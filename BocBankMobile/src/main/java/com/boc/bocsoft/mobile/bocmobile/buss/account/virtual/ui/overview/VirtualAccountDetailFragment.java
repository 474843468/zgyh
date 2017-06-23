package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.overview;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.ClipBoardWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * @author wangyang
 *         16/8/26 17:04
 *         账户详情
 */
@SuppressLint("ValidFragment")
public class VirtualAccountDetailFragment extends BaseAccountFragment implements DetailTableRowButton.BtnCallback {

    private DetailTableRow dtrDate, dtrSingleLimit, dtrMaxLimit, dtrLimit,dtrType;
    private DetailTableRow dtrNumber, dtrName, dtrStatus, dtrChannel;

    private DetailTableRowButton btnVirtualNumber;

    private VirtualCardModel model;

    public VirtualAccountDetailFragment(VirtualCardModel model) {
        this.model = model;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_virtual_account_detail, null);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_detail);
    }

    @Override
    public void initView() {

        dtrType = (DetailTableRow) mContentView.findViewById(R.id.dtr_type);
        dtrNumber = (DetailTableRow) mContentView.findViewById(R.id.dtr_number);
        btnVirtualNumber = (DetailTableRowButton) mContentView.findViewById(R.id.btn_virtual_number);
        dtrDate = (DetailTableRow) mContentView.findViewById(R.id.dtr_date);
        dtrSingleLimit = (DetailTableRow) mContentView.findViewById(R.id.dtr_single_limit);
        dtrMaxLimit = (DetailTableRow) mContentView.findViewById(R.id.dtr_max_limit);
        dtrLimit = (DetailTableRow) mContentView.findViewById(R.id.dtr_limit);
        dtrName = (DetailTableRow) mContentView.findViewById(R.id.dtr_name);
        dtrStatus = (DetailTableRow) mContentView.findViewById(R.id.dtr_status);
        dtrChannel = (DetailTableRow) mContentView.findViewById(R.id.dtr_channel);

        int height = getResources().getDimensionPixelOffset(R.dimen.boc_button_height_96px);
        dtrType.setBodyHeight(height);
        dtrNumber.setBodyHeight(height);
        btnVirtualNumber.setBodyHeight(height);
        dtrDate.setBodyHeight(height);
        dtrSingleLimit.setBodyHeight(height);
        dtrMaxLimit.setBodyHeight(height);
        dtrLimit.setBodyHeight(height);
        dtrName.setBodyHeight(height);
        dtrStatus.setBodyHeight(height);
        dtrChannel.setBodyHeight(height);
    }

    @Override
    public void setListener() {
        btnVirtualNumber.setOnclick(this);
    }

    @Override
    public void initData() {
        //设置实体卡,虚拟卡
        dtrNumber.updateWithLineMargin(NumberUtils.formatCardNumber(model.getAccountNumber()),0,0);
        btnVirtualNumber.addImgBtn(getString(R.string.boc_virtual_account_detail_info_card_number), NumberUtils.formatCardNumber(model.getAccountIbkNum()), R.drawable.boc_account_detail_copy);

        //设置有效日期范围
        String startDate = model.getStartDate().format(DateFormatters.dateFormatter1);
        String endDate = model.getEndDate().format(DateFormatters.dateFormatter1);
        dtrDate.updateWithLineMargin(startDate + " ~ " + endDate,0,0);

        //设置单笔,累计交易,已累计交易限额
        dtrSingleLimit.updateWithLineMargin(PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getSignleLimit(), model.getCurrencyCode()),0,0);
        dtrMaxLimit.updateWithLineMargin(PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getTotalLimit(), model.getCurrencyCode()),0,0);
        dtrLimit.updateWithLineMargin(PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getAtotalLimit(), model.getCurrencyCode()),0,0);

        //设置账户户名,卡状态,申请渠道
        dtrName.updateWithLineMargin(model.getAccountName(),0,0);
        dtrStatus.updateWithLineMargin(model.getStatus(),0,0);
        dtrChannel.updateWithLineMargin(model.getChannelString(),0,0);
    }

    @Override
    public void onClickListener() {
        String cardNum = NumberUtils.formatCardNumber2(model.getAccountIbkNum());
        new ClipBoardWidget(mContext, cardNum).show();
    }
}
