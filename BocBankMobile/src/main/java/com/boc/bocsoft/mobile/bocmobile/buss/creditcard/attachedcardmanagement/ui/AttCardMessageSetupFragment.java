package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainMessSetResult.PsnCrcdAppertainMessSetResultResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAppertainAndMess.PsnCrcdQueryAppertainAndMessResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardMessModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardSetUpModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter.AttCardMessContract;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter.AttCardMessPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.widget.SelectTabRow;

/**
 * Name: liukai
 * Time：2016/12/2 17:16.
 * Created by lk7066 on 2016/12/2.
 * It's used to 附属卡交易短信设置页面
 */

public class AttCardMessageSetupFragment extends MvpBussFragment<AttCardMessContract.AttCardMessPresenter> implements AttCardMessContract.AttCardMessView, View.OnClickListener{

    private View mRootView = null;

    //单选条添加到这个线性布局中
    private LinearLayout attCardLayoutContent;

    //下一步按钮
    private Button btnNext;

    //该页面由三个单选条
    private SelectTabRow messageToAll, messageToAttCard, messageToCrcd;
    private AttCardSetUpModel setUpModel;
    private AttCardMessModel messModel;
    private AttCardMessContract.AttCardMessPresenter mPresenter;

    //标志位，0，表示接口请求失败，单选框都没选中，弹出提示框；1，表示单选框已经有选中状态
    private int flag = 1;

    @Override
    protected View onCreateView(LayoutInflater mInflater){
        super.onCreateView(mInflater);
        setUpModel = getArguments().getParcelable("History");
        showLoadingDialog(false);
        mRootView = mInflater.inflate(R.layout.fragment_attcard_message_setup, null);
        return mRootView;
    }

    @Override
    public void initView(){
        attCardLayoutContent = (LinearLayout) mRootView.findViewById(R.id.layout_content_attcard);
        btnNext = (Button) mRootView.findViewById(R.id.btn_next_attcard);

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(0 == flag){
                    showErrorDialog(getResources().getString(R.string.boc_crcd_attcard_message_setup_error_info));
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Message", messModel);
                    AttCardMessageConfirmFragment attCardMessageConfirmFragment = new AttCardMessageConfirmFragment();
                    attCardMessageConfirmFragment.setArguments(bundle);
                    start(attCardMessageConfirmFragment);
                }

            }

        });

        //主
        messageToCrcd = new SelectTabRow(mContext);
        messageToCrcd.setContent(getResources().getString(R.string.boc_crcd_attcard_message_info1));
        attCardLayoutContent.addView(messageToCrcd, 0);

        //附
        messageToAttCard = new SelectTabRow(mContext);
        messageToAttCard.setContent(getResources().getString(R.string.boc_crcd_attcard_message_info2));
        attCardLayoutContent.addView(messageToAttCard, 1);

        //主附
        messageToAll = new SelectTabRow(mContext);
        messageToAll.setContent(getResources().getString(R.string.boc_crcd_attcard_message_info3));
        attCardLayoutContent.addView(messageToAll, 2);

    }

    @Override
    public void initData(){
        mPresenter = new AttCardMessPresenter(this);

        messModel = new AttCardMessModel();
        messModel.setCrcdNo(setUpModel.getCrcdNo());
        messModel.setAttCardName(setUpModel.getAttCardName());
        messModel.setAccountId(setUpModel.getAccountId());
        messModel.setSubCrcdNo(setUpModel.getSubCrcdNo());

        if(setUpModel.getCurrency1() != null){
            getPresenter().queryAppertainAndMess(setUpModel);
        } else {
            getPresenter().queryCrcdCurrency(setUpModel.getCrcdNo());
        }

    }

    /**
     *  添加选择条
     *  0主1附2主附
     */
    private void initSelectTabRow() {

        //接口请求完毕，根据请求的结果设置默认选项，如果请求失败，那么没有默认选项
        if(messModel.getSmeSendFlag() != null){
            if(messModel.getSmeSendFlag().equals("0")){
                messageToCrcd.setTabChecked(true);
            }
            if(messModel.getSmeSendFlag().equals("1")){
                messageToAttCard.setTabChecked(true);
            }
            if(messModel.getSmeSendFlag().equals("2")){
                messageToAll.setTabChecked(true);
            }
        }

        //点击事件
        messageToAll.setOnClickListener(this);
        messageToAttCard.setOnClickListener(this);
        messageToCrcd.setOnClickListener(this);

    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_crcd_attcard_message_setup_title);
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
        mPresenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    protected AttCardMessContract.AttCardMessPresenter initPresenter() {
        return new AttCardMessPresenter(this);
    }

    @Override
    public void onClick(View v) {

        if(v == messageToAll){
            clearSelectedState();
            messageToAll.setTabChecked(true);
            messModel.setSmeSendFlag("2");
        } else if(v == messageToAttCard){
            clearSelectedState();
            messageToAttCard.setTabChecked(true);
            messModel.setSmeSendFlag("1");
        } else {
            clearSelectedState();
            messageToCrcd.setTabChecked(true);
            messModel.setSmeSendFlag("0");
        }

        //将flag置为1，表示已经有选中了的单选条
        flag = 1;

    }

    /**
     * 每次点击选择单选条的时候，先将原来的状态清空，然后去设置选中状态
     * */
    private void clearSelectedState(){
        messageToAll.setTabChecked(false);
        messageToAttCard.setTabChecked(false);
        messageToCrcd.setTabChecked(false);
    }

    @Override
    public void appertainAndMessSuccess(PsnCrcdQueryAppertainAndMessResult psnCrcdQueryAppertainAndMessResult) {
        closeProgressDialog();
        messModel.setAccountId(setUpModel.getAccountId());
        messModel.setSubCrcdNo(setUpModel.getSubCrcdNo());
        messModel.setSmeSendFlag(psnCrcdQueryAppertainAndMessResult.getSmeSendFlag());

        //初始化选中状态
        initSelectTabRow();
    }

    @Override
    public void appertainAndMessFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        messModel.setAccountId(setUpModel.getAccountId());
        messModel.setSubCrcdNo(setUpModel.getSubCrcdNo());

        //表示请求失败，flag置为0，如果点击下一步，会有提示弹框
        flag = 0;

        //初始化选中状态
        initSelectTabRow();
    }

    @Override
    public void setAppertainMessResultSuccess(PsnCrcdAppertainMessSetResultResult psnCrcdAppertainMessSetResultResult) {

    }

    @Override
    public void setAppertainMessResultFailed(BiiResultErrorException exception) {

    }

    /**
     * 查询币种，如果前一个页面查询失败，此页面再次查询币种，根据返回的第一币种就可以查询短信设置的对象了
     * */
    @Override
    public void crcdCurrencyQuerySuccess(PsnCrcdCurrencyQueryResult mCurrencyQuery) {
        setUpModel.setCurrency1(mCurrencyQuery.getCurrency1().getCode());
        getPresenter().queryAppertainAndMess(setUpModel);
    }

    @Override
    public void crcdCurrencyQueryFailed(BiiResultErrorException exception) {

    }

    @Override
    public void setPresenter(AttCardMessContract.AttCardMessPresenter presenter) {

    }

}
