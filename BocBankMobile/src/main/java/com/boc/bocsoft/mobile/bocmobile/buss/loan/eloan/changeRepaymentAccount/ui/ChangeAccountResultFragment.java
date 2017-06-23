package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView.BtnCallback;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom.HomeBtnCallback;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanManagerFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ChangeAccountVerifyReq;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * 变更还款结果界面
 * Created by qiangchen on 2016/7/25.
 */
public class ChangeAccountResultFragment extends BussFragment implements BtnCallback,HomeBtnCallback{

    private View mRoot;
    private OperationResultHead.Status status;
    protected LinearLayout ll_content;
    protected OperationResultBottom bt_backresult;
    private ChangeAccountVerifyReq params;
    private String oldAccount;
    //结果页
    private BaseOperationResultView borvResult;
    public static  String newAccount;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_eloan_change_results, null);
        return mRoot;
    }

    @Override
    public void initView() {
        borvResult = (BaseOperationResultView) mContentView.findViewById(R.id.rv_result);
        borvResult.findViewById(R.id.txt_title).setVisibility(View.GONE);
    }

    public void setStatus(OperationResultHead.Status status) {
        this.status = status;
    }


    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_draw_operatingresults_pagename);
    }

    @Override
    public void beforeInitView() {

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
    public void initData() {
//      params= (ChangeAccountVerifyReq) getArguments().getSerializable(EloanConst.ELON_ACCOUNT);
      oldAccount=getArguments().getString("oldAccount");
      newAccount=getArguments().getString("newAccount");

      borvResult.updateHead(status,  "变更还款账户申请已提交");
      borvResult.isShowInfo(true, "还款账户变更申请提交后，新的还款账户将在第二天正式生效。");
      borvResult.setDetailsName("查看详情");

      borvResult.addDetailRow("当前还款账户", NumberUtils.formatCardNumberStrong(oldAccount),true,false);
      borvResult.addDetailRow("变更后\n还款账户", NumberUtils.formatCardNumberStrong(newAccount),false,true);
      
//      borvResult.addDetailRow("当前还款账户", NumberUtils.formatCardNumberStrong(oldAccount));
//      borvResult.addDetailRow("变更后\n还款账户", NumberUtils.formatCardNumberStrong(newAccount));
      
////      OperationResultHead operationResultHead
////              = new OperationResultHead(mContext);
//      DetailContentView detailContentView = new DetailContentView(mContext);
//
//      operationResultHead.updateData(status, "变更还款账户申请已提交！");

////      detailContentView.addDetailRow("当前还款账户", NumberUtils.formatCardNumber(oldAccount));
//      detailContentView.addDetailRow("当前还款账户", NumberUtils.formatCardNumberStrong(oldAccount));
//      detailContentView.addDetailRow("变更后还款账户", NumberUtils.formatCardNumber(newAccount));

//      bt_backresult.updateButton("返回首页");
////      ll_content.addView(operationResultHead);
//      ll_content.addView(detailContentView);

    }
  
    @Override
    public void setListener() {
        borvResult.setOnclick(this);
        borvResult.setgoHomeOnclick(this);
    }

    @Override
    protected void titleLeftIconClick() {
        onClickBack();
    }

    @Override
    public boolean onBack() {
        onClickBack();
        return false;
    }

    /**
     * 返回事件处理
     */
    private void onClickBack(){
        popToAndReInit(LoanManagerFragment.class);
    }
    
    @Override
    public void onHomeBack() {
        ModuleActivityDispatcher.popToHomePage();
    }
   
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

	@Override
	public void onClickListener(View v) {
		
	}

}
