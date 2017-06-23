package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView.BtnCallback;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom.HomeBtnCallback;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanManagerFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model.LOANCycleLoanEApplySubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.presenter.DrawPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * 用款结果界面
 * Created by qiangchen on 2016/7/25.
 */
public class DrawOperatingResultsFragment extends MvpBussFragment<DrawPresenter> implements BtnCallback, HomeBtnCallback, DrawContract.DrawOperatingResultsView {
    private static String PAGENAME;
    protected LinearLayout ll_content;
    protected RelativeLayout bottom;
    protected OperationResultBottom bt_backresult;
    private View mRoot;
    private OperationResultHead.Status status;
    private LOANCycleLoanEApplySubmitReq params;
    
  //结果页
    private BaseOperationResultView borvResult;
    /***
     * 接口调用
     */
//    private DrawPresenter drawPresenter;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_eloan_draw_operatingresults, null);
        return mRoot;
    }

    public void setStatus(OperationResultHead.Status status) {
        this.status = status;
    }

    @Override
    protected void titleLeftIconClick() {
//        super.titleLeftIconClick();
//    	start(new EloanStatusFragment());
//    	 EloanStatusFragment.isStartPage=true;
//    	 popTo(EloanStatusFragment.class,false);
    	
    	popToAndReInit(LoanManagerFragment.class);
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
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void initView() {
//      ll_content = (LinearLayout) mRoot.findViewById(R.id.ll_content);
////    bottom = (RelativeLayout) mRoot.findViewById(R.id.bottom);
//    bt_backresult = (OperationResultBottom) mRoot.findViewById(R.id.bt_backresult);

    borvResult = (BaseOperationResultView) mContentView.findViewById(R.id.rv_result);
    borvResult.findViewById(R.id.txt_title).setVisibility(View.GONE);
    }

    @Override
    public void initData() {
//        params= (LOANCycleLoanEApplySubmitReq) getArguments().getSerializable(EloanConst.ELON_DRAW_COMMIT);
//        borvResult.updateHead(status,  "用款申请已提交");
//        borvResult.setDetailsName("查看明细");
//
//        borvResult.addDetailRow("用款金额", "人民币元 "+MoneyUtils.transMoneyFormat(params.getAmount(), "021"));
//        borvResult.addDetailRow("收款账户", NumberUtils.formatCardNumber(params.getLoanCycleToActNum()));
//        borvResult.addDetailRow("期限 /利率", params.getLoanPeriod().substring(1)+"个月/"+params.getLoanRate()+"%");
////        borvResult.addDetailRow("期限 /利率", params.getLoanPeriod().substring(1)+"个月/"+MoneyUtils.transRatePercentTypeFormat(params.getLoanRate()));
//        borvResult.addDetailRow("还款账户", NumberUtils.formatCardNumberStrong(params.getPayAccount()));
//        borvResult.addDetailRow("资金用途", params.getRemark());
//
//
    //2016年10月9日 16:14:31 闫勋 修改
//        drawPresenter = new DrawPresenter(this);
        params= (LOANCycleLoanEApplySubmitReq) getArguments().getSerializable(EloanConst.ELON_DRAW_COMMIT);
        getPresenter().prepayCheckAccountDetail(params.getToAccountId());
//        borvResult.setDetailsTitleIsShow(false);
        borvResult.isAddTopDetailRow(true);
        borvResult.updateHead(status,  "交易成功！");
        borvResult.addTopDetailRow("用款金额", "人民币元 "+MoneyUtils.transMoneyFormat(params.getAmount(), "021"));
        borvResult.addTopDetailRow("收款账户", NumberUtils.formatCardNumber(params.getLoanCycleToActNum()));
        borvResult.setDetailsName("查看详情");
        String mMonth=  strToInt(params.getLoanPeriod())+"";
        borvResult.addDetailRow("期限/利率" ,  mMonth+"个月/"+params.getLoanRate()+"%");
//        if("0".equalsIgnoreCase(params.getLoanPeriod().substring(0))){
//            borvResult.addDetailRow("期限 /利率", params.getLoanPeriod().substring(1)+"个月/"+params.getLoanRate()+"%");
//        }else{
//            borvResult.addDetailRow("期限 /利率", params.getLoanPeriod()+"个月/"+params.getLoanRate()+"%");
//        }

//        borvResult.addDetailRow("期限 /利率", params.getLoanPeriod().substring(1)+"个月/"+MoneyUtils.transRatePercentTypeFormat(params.getLoanRate()));
        borvResult.addDetailRow("还款账户", NumberUtils.formatCardNumberStrong(params.getPayAccount()));
        borvResult.addDetailRow("资金用途", params.getRemark());
//        borvResult.addContentItem("查看余额", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {//2016年10月9日 16:26:45 闫勋 修改
//                popToAndReInit(EloanStatusFragment.class);
//            }
//        });


//        OperationResultHead operationResultHead
//                = new OperationResultHead(mContext);
//        DetailContentView detailContentView = new DetailContentView(mContext);
//
//        operationResultHead.updateData(status, "用款申请已提交！");
//
//        detailContentView.addDetailRow("用款金额", MoneyUtils.transMoneyFormat(params.getAmount(), "021"));
//        detailContentView.addDetailRow("收款账户", NumberUtils.formatCardNumber(params.getLoanCycleToActNum()));
//        detailContentView.addDetailRow("期限 /利率", params.getLoanPeriod().substring(1)+"个月/"+MoneyUtils.transRatePercentTypeFormat(params.getLoanRate()));
////        detailContentView.addDetailRow("还款账户", NumberUtils.formatCardNumber(params.getPayAccount()));
//        detailContentView.addDetailRow("还款账户", NumberUtils.formatCardNumberStrong(params.getPayAccount()));
//        detailContentView.addDetailRow("资金用途", params.getRemark());

//        bt_backresult.updateButton("返回首页");
//        ll_content.addView(operationResultHead);
//        ll_content.addView(detailContentView);
    }

    @Override
    public void setListener() {
//        bt_backresult.setgoHomeOnclick(new OperationResultBottom.HomeBtnCallback() {
//            @Override
//            public void onHomeBack() {
//                popTo(EloanStatusFragment.class,false);
////            	start(new EloanStatusFragment());
//            }
//        });
    	
    	 borvResult.setOnclick(this);
         borvResult.setgoHomeOnclick(this);
    }

    @Override
	public void onHomeBack() {
//    	start(new EloanStatusFragment());
//    	 EloanStatusFragment.isStartPage=true;
//		 popTo(EloanStatusFragment.class,false);
    	
//    	popToAndReInit(EloanStatusFragment.class);
        ModuleActivityDispatcher.popToHomePage();
	}
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

	@Override
	public void onClickListener(View v) {
		
	}

    /**
     * 查询余额成功回调
     * @param result
     */
    @Override
    public void prepayCheckAccountDetailSuccess(PrepayAccountDetailModel.AccountDetaiListBean result) {
        borvResult.addTopDetailRow("可用余额", "人民币元 "+ MoneyUtils.transMoneyFormat(String.valueOf(result.getAvailableBalance()), "021"));
    }

    /**
     * 查询余额失败回调
     * @param e
     */
    @Override
    public void prepayCheckAccountDetailFail(ErrorException e) {

    }

    @Override
    public void setPresenter(DrawContract.Presenter presenter) {

    }

    @Override
    protected DrawPresenter initPresenter() {
        return new DrawPresenter(this);
    }

//  @Override
//  public boolean onBack() {
//      mActivity.finish();
//      return super.onBack();
//  }
    /**
     * String 转换成 int 类型
     *
     * @param number
     * @return
     */
    private int strToInt(String number) {
        if (!"".equals(number) && number != null) {
            return Integer.valueOf(number);
        }
        return 0;
    }
}
