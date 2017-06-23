package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.okDetails.OKDetailTableRow;

/**
 * 微信通知好友页面
 * Created by zhx on 2016/7/12
 */
public class WeixinNotifyFriendsFragment extends BussFragment {
    private View mRootView;
    private OKDetailTableRow odtrPayerName;
    private OKDetailTableRow odtrPayDate;
    private OKDetailTableRow odtrPayeeName;
    private OKDetailTableRow odtrPayeeNo;
    private OKDetailTableRow odtrPayeeBank;
    private OKDetailTableRow odtrPayeeMobileNum;
    private OKDetailTableRow odtrPayeeAdditionalComments;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_weixin_notify_friends, null);
        return mRootView;
    }

    @Override
    public void initView() {
        odtrPayerName = (OKDetailTableRow) mRootView.findViewById(R.id.odtr_payer_name);
        odtrPayDate = (OKDetailTableRow) mRootView.findViewById(R.id.odtr_pay_date);
        odtrPayeeName = (OKDetailTableRow) mRootView.findViewById(R.id.odtr_payee_name);
        odtrPayeeNo = (OKDetailTableRow) mRootView.findViewById(R.id.odtr_payee_no);
        odtrPayeeBank = (OKDetailTableRow) mRootView.findViewById(R.id.odtr_payee_bank);
        odtrPayeeMobileNum = (OKDetailTableRow) mRootView.findViewById(R.id.odtr_payee_mobile_num);
        odtrPayeeAdditionalComments = (OKDetailTableRow) mRootView.findViewById(R.id.odtr_payee_additional_comments);

        odtrPayerName.updateData("付款人姓名","王艳丽");
        odtrPayDate.updateData("交易时间","2016/06/10");
        odtrPayeeName.updateData("收款人名称","王小华");
        odtrPayeeNo.updateData("收款账号","6000******0000");
        odtrPayeeBank.updateData("收款银行","中国银行");
        odtrPayeeMobileNum.updateData("收款人手机号","00000000000");
        odtrPayeeAdditionalComments.updateData("附言","附言内容附言内容附言内容");
    }

    @Override
    public void initData() {
    }

    @Override
    public void setListener() {
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "分享";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }
}
