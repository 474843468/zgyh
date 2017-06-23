package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.ChangeAccountResultFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.ChangeRepaymentAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanStatusListModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;


/**
 * Created by huixiaobo on 2016/6/14.
 * 中银E贷额度详情
 */
public class EloanAmountDetailFragment extends BussFragment {
    /**
     * view
     */
    protected View rootView;
    /**
     * 显示详情公共组件
     */
    protected DetailContentView amountDetail;

    /**上额度详情model*/
    private EloanStatusListModel mEloanDrawModel;
    

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_eamountdetail_fragment, null);
        return rootView;
    }

    @Override
    public void initView() {
        
        amountDetail = (DetailContentView) rootView.findViewById(R.id.amountDetail);
      
    }


    @Override
    public void initData() {
       
        mEloanDrawModel = (EloanStatusListModel) getArguments().getSerializable(EloanConst.ELOAN_QUOTA);
        amountDetail.addDetail(getResources().getString(R.string.boc_details_amount), "人民币元" + MoneyUtils.transMoneyFormat(mEloanDrawModel.getLoanBanlance(), "001"));
        amountDetail.addDetail(getResources().getString(R.string.boc_eloan_explainTv), "人民币元" + MoneyUtils.transMoneyFormat(mEloanDrawModel.getAvailableAvl(), "001"));
        amountDetail.addDetail(getString(R.string.boc_facility_quota_used),
               "人民币元"+ MoneyUtils.transMoneyFormat(mEloanDrawModel.getUseAvl(), "001"));
        
        if(!TextUtils.isEmpty(ChangeRepaymentAccountFragment.mQuoteno)
                && ChangeRepaymentAccountFragment.mQuoteno.equals(mEloanDrawModel.getQuoteNo())
                &&!StringUtils.isEmptyOrNull(ChangeAccountResultFragment.newAccount)){

        	 amountDetail.getDetailTableRowButton().addTextBtn(getResources().getString(R.string.boc_details_repayaccount),
                     NumberUtils.formatCardNumber(ChangeAccountResultFragment.newAccount), getResources().getString(R.string.boc_details_update),getResources().getColor(R.color.boc_main_button_color));
		}else{
			amountDetail.getDetailTableRowButton().addTextBtn(getResources().getString(R.string.boc_details_repayaccount),
	                NumberUtils.formatCardNumber(mEloanDrawModel.getPayAccount()), getResources().getString(R.string.boc_details_update),getResources().getColor(R.color.boc_main_button_color));
		}	

        amountDetail.addDetail(getString(R.string.boc_details_date), "每月" + mEloanDrawModel.getIssueRepayDate()+ "日");

        amountDetail.addDetail(getResources().getString(R.string.boc_details_interest),
               mEloanDrawModel.getRate() + "%");
        amountDetail.addDetail(getResources().getString(R.string.boc_details_edate),
                mEloanDrawModel.getLoanToDate());
        amountDetail.addDetail(getString(R.string.boc_quote_No),
                mEloanDrawModel.getQuoteNo());

        String quoteState = getValue(mEloanDrawModel.getQuoteState());
        amountDetail.addDetail(getString(R.string.boc_quote_status),quoteState);

    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_quote_details);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }
    @Override
    public void setListener() {
    
    	amountDetail.getDetailTableRowButton().setOnclick(new DetailTableRowButton.BtnCallback() {
            @Override
            public void onClickListener() {
            	if (ApplicationContext.getInstance().getUser().getMobile() == null) {
                 	showErrorDialog(getString(R.string.boc_eloan_mobile));
             		return;
                 }
            	
                if (mEloanDrawModel != null) {
                    ChangeRepaymentAccountFragment accountFragment = new ChangeRepaymentAccountFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(EloanConst.ELON_ACCOUNT, mEloanDrawModel);
                    accountFragment.setArguments(bundle);
                    start(accountFragment);
                }
            }
        });
    }
    
    private String getValue(String statuskey) {

		if ("05".equals(statuskey)){
    		return "正常";
    	} else if ("10".equals(statuskey)) {
    		return "取消";
    	} else if ("20".equals(statuskey)) {
    		return "冻结";
    	} else if ("40".equals(statuskey)) {
    		return "已到期";
    	}
    	return null;
    }
   
}



