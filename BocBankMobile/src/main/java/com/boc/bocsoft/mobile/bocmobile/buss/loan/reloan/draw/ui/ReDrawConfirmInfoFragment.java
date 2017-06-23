package com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.draw.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * Created by qiangchen on 2016/8/13.
 */
public class ReDrawConfirmInfoFragment extends BussFragment{
    //页面根视图
    private View mRoot;
    private LinearLayout content;
    private DetailTableRowButton bocBtntv;
    private DetailTableHead detailTableHead;
    private DetailContentView detailContentView;
    private Button confirm;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_eloan_draw_confirminfo, null);
        return mRoot;
    }

    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_drawconfirmation_pagename);
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
    public void initView() {
        //初始化组件
        content = (LinearLayout) mRoot.findViewById(R.id.content);
        detailTableHead = new DetailTableHead(mContext);
        detailContentView = new DetailContentView(mContext);
        bocBtntv = new DetailTableRowButton(mContext);
        confirm = (Button) mRoot.findViewById(R.id.confirm);
    }

    @Override
    public void initData() {
        //为组建添加默认数据
        detailTableHead.updateData("用款金额（人民币元）", "5000");
        detailTableHead.setDetailVisable(false);

        detailContentView.addDetailRowNotLine("期限","3个月");
        detailContentView.addDetailRowNotLine("利率","80.56%");
        detailContentView.addDetailRowNotLine("还款方式", "按月付息到期还本");
        detailContentView.addDetailRowNotLine("还款周期", "每月");
        detailContentView.addDetailRowNotLine("收款账户","6217******9854");
        detailContentView.addDetailRowNotLine("还款账户","6217******9854");
        detailContentView.addDetailRowNotLine("贷款到期日","2020/12/10");

        content.addView(detailTableHead);
        content.addView(detailContentView);
    }

    @Override
    public void setListener() {

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loanCycleLoanEApplyVerifyReq.set_combinId(_combinId);
//                drawPresenter.setVerifyReq(loanCycleLoanEApplyVerifyReq);
//                //点击下一步按钮，跳转到提款确认页
//                drawPresenter.drawApplyVerify();
//                showLoadingDialog();

            }
        });
    }
}
