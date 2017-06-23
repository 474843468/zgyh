package com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.ui;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ConfirmDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.model.FundbuyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.presenter.FundPurchasePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginMsgDialogView;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.ui.ConsumeFinanceFragment;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.LinkedHashMap;

/**
 * Created by zcy7065 on 2016/11/24.
 */
@SuppressLint("ValidFragment")
public class FundPurchaseConfirmFragment extends MvpBussFragment<FundPurchasePresenter> implements FundPurchaseContract.FundPurchaseConfirmView {
    private FundbuyModel model;
    private Button btnComfirm;
    private boolean nightBuyConfirmed;
    protected ConfirmInfoView confirmInfoView;
    /**
     * 构造函数-带参
     * */
    public FundPurchaseConfirmFragment(FundbuyModel fundbuyModel){
        this.model = fundbuyModel;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater){
        confirmInfoView = new ConfirmInfoView(mContext);
        return confirmInfoView;
    }

    @Override
    public void initView(){

        confirmInfoView.isShowSecurity(false);
        btnComfirm = (Button) confirmInfoView.findViewById(R.id.btn_ok);

    }
    @Override
    public void initData(){
        nightBuyConfirmed = false;

        //判定是否是货币型基金
        if(model.getFntype().equals("06")){
            confirmInfoView.setHint("咨询及投诉电话：12333");
        }else{
            confirmInfoView.setHint(model.getCompanyName()+" "+model.getCompanyPhone()+""+"咨询及投诉电话：12333");
        }
        String currency = PublicCodeUtils.getCurrency(mContext, "001");
        confirmInfoView.setHeadValue(getString(R.string.boc_purchase_confirm_head_two, currency), model.getBuyAmount());


        LinkedHashMap<String, String> datas = new LinkedHashMap<>();
        datas.put("买入基金", getString(R.string.boc_purchase_product_head, currency, model.getFundCode(), model.getFundCode()));
        datas.put("基金交易账户", "1234567890123");
        datas.put("资金账户", MoneyUtils.transMoneyFormat("100000","001"));
        datas.put("基金状态", "正常开放");
        datas.put("参考手续费", "10");
        confirmInfoView.setHint(model.getCompanyName()+model.getCompanyPhone(),R.color.boc_text_color);
        confirmInfoView.addData(datas,false,false);
    }



    @Override
    public void setListener(){
        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener(){

            @Override
            public void onClickConfirm() {
                showLoadingDialog();
                Log.d("张辰雨", "onClickConfirm: : nightBuyConfirmed is :"+nightBuyConfirmed);
                Log.d("张辰雨", "onClickConfirm: : nightBuyConfirmed is :"+model.getIsSignedFundContract());
                getPresenter().fundBuySubmit(model);
            }

            @Override
            public void onClickChange() {

            }


        });
    }





    //基金买入提交
    public void fundBuySubmit(final FundbuyModel fundbuyModel){
        model =fundbuyModel;
        if(nightBuyConfirmed){
            getPresenter().fundNightBuySubmit(fundbuyModel);
            return;
        }

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
                            nightBuyConfirmed = true;
                            getPresenter().fundNightBuySubmit(model);
                        }
                    });
            dialog.show();

        }else if(model.getTranState().equals("E136")){
            //风险级别不匹配
            final ConfirmDialog dialog = new ConfirmDialog(getContext());
            dialog.setMessage("风险差异大")
                    .setLeftButton("取消")
                    .setRightButton("确定")
                    .setButtonClickInterface(new ConfirmDialog.OnButtonClickInterface(){
                        @Override
                        public void onLeftClick(ConfirmDialog warnDialog) {
                            model.setAffirmFlag("N");
                            dialog.cancel();

                        }

                        @Override
                        public void onRightClick(ConfirmDialog warnDialog) {
                            model.setAffirmFlag("Y");
                            getPresenter().fundBuySubmit(model);
                        }
                    });
            dialog.show();
        }else if(model.getTranState().equals("E146")){
            //未签署电子合同
            final ConfirmDialog dialog = new ConfirmDialog(getContext());
            dialog.setMessage("没签署电子合同")
                    .setLeftButton("取消")
                    .setRightButton("确定")
                    .setButtonClickInterface(new ConfirmDialog.OnButtonClickInterface(){
                        @Override
                        public void onLeftClick(ConfirmDialog warnDialog) {
                            model.setIsSignedFundContract("N");
                            dialog.cancel();
                        }

                        @Override
                        public void onRightClick(ConfirmDialog warnDialog) {
                            model.setIsSignedFundContract("Y");
                            start(new FundSignFragment(model));
                        }
                    });
        } else if(model.getTranState().equals("1")){
            closeProgressDialog();
            start(new FundPurchaseResultFragment(model));
        }

    }

    //基金挂单买入返回
    public void fundNightBuySubmit(FundbuyModel fundNightBuyModel){
        model =fundNightBuyModel;
        if(model.getTranState().equals("E136")){
            //风险级别不匹配
            final ConfirmDialog dialog = new ConfirmDialog(getContext());
            dialog.setMessage("风险差异大")
                    .setLeftButton("取消")
                    .setRightButton("确定")
                    .setButtonClickInterface(new ConfirmDialog.OnButtonClickInterface(){
                        @Override
                        public void onLeftClick(ConfirmDialog warnDialog) {
                            model.setAffirmFlag("N");
                            dialog.cancel();

                        }

                        @Override
                        public void onRightClick(ConfirmDialog warnDialog) {
                            model.setAffirmFlag("Y");
                            getPresenter().fundNightBuySubmit(model);
                        }
                    });
            dialog.show();
        }else if(model.getTranState().equals("E146")){
            //未签署约定书
            final ConfirmDialog dialog = new ConfirmDialog(getContext());
            dialog.setMessage("没签署电子约定书")
                    .setLeftButton("取消")
                    .setRightButton("确定")
                    .setButtonClickInterface(new ConfirmDialog.OnButtonClickInterface(){
                        @Override
                        public void onLeftClick(ConfirmDialog warnDialog) {
                            model.setIsSignedFundContract("N");
                            dialog.cancel();
                        }

                        @Override
                        public void onRightClick(ConfirmDialog warnDialog) {
                            model.setIsSignedFundContract("Y");
                            start(new FundSignFragment(model));
                        }
                    });
        }else  if(model.getTranState().equals("1")){
            closeProgressDialog();
            start(new FundPurchaseResultFragment(model));
        }
    }

    /*//查询用户风险等级
    public void queryFundEvaluation(FundbuyModel fundbuyModel) {
        model = fundbuyModel;

        if (Integer.parseInt(model.getUserRiskLevel()) < Integer.parseInt(model.getRiskLv())) {
            final ConfirmDialog dialog = new ConfirmDialog(getContext());
            dialog.setMessage("风险等级过高，需要重新确认")
                    .setLeftButton("取消")
                    .setRightButton("确定")
                    .setButtonClickInterface(new ConfirmDialog.OnButtonClickInterface(){
                        @Override
                        public void onLeftClick(ConfirmDialog warnDialog) {
                            model.setAffirmFlag("N");
                            dialog.cancel();
                        }

                        @Override
                        public void onRightClick(ConfirmDialog warnDialog) {
                            model.setAffirmFlag("Y");
                            start(new FundSignFragment(model));
                        }
            });
            return;
        }

        if (Integer.parseInt(model.getUserRiskLevel()) >= Integer.parseInt(model.getRiskLv())) {
            getPresenter().fundBuySubmit(model);
            return;
        }

    }*/



    @Override
    protected  FundPurchasePresenter initPresenter() {
        return new FundPurchasePresenter(this);
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    protected String getTitleValue() {
        return "确认";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return super.isDisplayRightIcon();
    }

}
