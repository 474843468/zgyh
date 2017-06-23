package com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ConfirmDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem.model.FundRedeemModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem.presenter.FundRedeemPresenter;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.LinkedHashMap;

/**
 * Created by zcy7065 on 2016/12/9.
 */
@SuppressLint("ValidFragment")
public class FundRedeemConfirmFragment extends MvpBussFragment<FundRedeemContract.FundRedeemPresenter> implements FundRedeemContract.FundRedeemConfirmView {


    protected ConfirmInfoView confirmInfoView;
    private FundRedeemModel model;
    private boolean isQuickFundSell;
    private boolean nightSellComfirmed;

    /**
     * 构造函数-带参
     * */
    public FundRedeemConfirmFragment(FundRedeemModel fundRedeemModel,boolean isQuickFundSell){
        model = fundRedeemModel;
        this.isQuickFundSell = isQuickFundSell;
    }

    protected View onCreateView(LayoutInflater mInflater){
        confirmInfoView = new ConfirmInfoView(mContext);
        return confirmInfoView;
    }

    public void initView(){
        confirmInfoView.isShowSecurity(false);
    }

    public void initData(){

        //判定是否是快速赎回
        if(isQuickFundSell = false){
            confirmInfoView.setHeadValue("赎回份额","1000000",false);
        }else{
            confirmInfoView.setHeadValue("快速赎回份额","1000000",false);
        }

        //判定是否是货币型基金
        if(model.getFntype().equals("06")){
            confirmInfoView.setHint("咨询及投诉电话：12333");
        }else{
            confirmInfoView.setHint(model.getCompanyName()+" "+model.getCompanyPhone()+""+"咨询及投诉电话：12333");
        }
        LinkedHashMap<String, String> datas = new LinkedHashMap<String, String>();
        datas.put( "手续费","888888888888888");
        datas.put( "利息","888188888");
        datas.put( "利率","88828888888");
        datas.put( "手续费","882883888");
        confirmInfoView.addData(datas,false,false);
    }

    @Override
    public void setListener(){
        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener(){

            @Override
            public void onClickConfirm(){
                if(isQuickFundSell){
                    showLoadingDialog();
                    getPresenter().fundQuickSell(model);

                }else {
                    showLoadingDialog();
                    getPresenter().fundSell(model);
                }
            }

            @Override
            public void onClickChange() {

            }


        });
    }


    @Override
    protected String getTitleValue() {
        return "赎回";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return super.isDisplayRightIcon();
    }

    @Override
    protected  FundRedeemContract.FundRedeemPresenter initPresenter(){
        return new FundRedeemPresenter(this);
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    //基金赎回成功返回
    public void fundSell(FundRedeemModel fundsellModel){
        model = fundsellModel;

        if(model.getTranState().equals("9000")){
            final ConfirmDialog dialog = new ConfirmDialog(getContext());
            dialog.setMessage("超过交易时间，需要挂单")
                    .setLeftButton("取消")
                    .setRightButton("确定")
                    .setButtonClickInterface(new ConfirmDialog.OnButtonClickInterface(){
                        @Override
                        public void onLeftClick(ConfirmDialog warnDialog) {
                            dialog.cancel();
                        }

                        @Override
                        public void onRightClick(ConfirmDialog warnDialog) {
                            nightSellComfirmed = true;
                            getPresenter().fundNightSell(model);
                        }
                    });
            dialog.show();
        } else if(model.getTranState().equals("1")){
            closeProgressDialog();
            start(new FundRedeemResultFragment(model));
        }

    }

    //基金挂单赎回成功返回
    public void fundNightSell(FundRedeemModel fundnightsellModel){
        model = fundnightsellModel;
        closeProgressDialog();
        if (model.getTranState().equals("1")){
            start(new FundRedeemResultFragment(model));
        }

    }

    //基金快速赎回成功返回
    public void fundQuickSell(FundRedeemModel fundQuickSellModel){
        model = fundQuickSellModel;
        closeProgressDialog();
        if(model.getTranState().equals("1")){
            start(new FundRedeemResultFragment(model));
        }
    }

    //查询基金公司详情
    public void queryFundCompanyDetail(FundRedeemModel fundRedeemModel){

    }
}
