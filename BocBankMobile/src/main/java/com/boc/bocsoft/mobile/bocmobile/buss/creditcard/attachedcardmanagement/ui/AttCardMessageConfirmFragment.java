package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainMessSetResult.PsnCrcdAppertainMessSetResultResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAppertainAndMess.PsnCrcdQueryAppertainAndMessResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardMessModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter.AttCardMessContract;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter.AttCardMessPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 * Name: liukai
 * Time：2016/12/2 16:47.
 * Created by lk7066 on 2016/12/2.
 * It's used to 附属卡交易短信信息确认页面
 */

public class AttCardMessageConfirmFragment extends MvpBussFragment<AttCardMessContract.AttCardMessPresenter> implements AttCardMessContract.AttCardMessView {

    private View mRootView = null;

    //信息确认页面组件
    private ConfirmInfoView attCardConfirmInfoView;
    private AttCardMessContract.AttCardMessPresenter mPresenter;
    private AttCardMessModel messModel;

    @Override
    protected View onCreateView(LayoutInflater mInflater){
        messModel = getArguments().getParcelable("Message");
        super.onCreateView(mInflater);
        mRootView = mInflater.inflate(R.layout.fragment_attcard_message_confirm, null);
        return mRootView;
    }

    @Override
    public void initView(){

        attCardConfirmInfoView = (ConfirmInfoView) mRootView.findViewById(R.id.attcard_confirm_view_title);

        attCardConfirmInfoView.setListener(new ConfirmInfoView.OnClickListener(){

            @Override
            public void onClickConfirm() {
                getPresenter().setAppertainMessResult(messModel);
                showLoadingDialog(false);
            }

            @Override
            public void onClickChange() {

            }

        });

    }

    @Override
    public void initData(){

        mPresenter = new AttCardMessPresenter(this);

        //不加载安全工具
        attCardConfirmInfoView.isShowSecurity(false);

        //定义哈希表，写入需要显示的字段值
        LinkedHashMap<String, String> nameAndVelue=new LinkedHashMap<>();

        if(messModel.getSmeSendFlag().equals("0")){
            nameAndVelue.put(getResources().getString(R.string.boc_crcd_attcard_message_tranmessage), getResources().getString(R.string.boc_crcd_attcard_message_info1));
        } else if(messModel.getSmeSendFlag().equals("1")){
            nameAndVelue.put(getResources().getString(R.string.boc_crcd_attcard_message_tranmessage), getResources().getString(R.string.boc_crcd_attcard_message_info2));
        } else {
            nameAndVelue.put(getResources().getString(R.string.boc_crcd_attcard_message_tranmessage), getResources().getString(R.string.boc_crcd_attcard_message_info3));
        }

        nameAndVelue.put(getResources().getString(R.string.boc_crcd_attcard_crcd_no), NumberUtils.formatCardNumber(messModel.getCrcdNo()));
        nameAndVelue.put(getResources().getString(R.string.boc_crcd_attcard_attcard_name), messModel.getAttCardName());
        nameAndVelue.put(getResources().getString(R.string.boc_crcd_attcard_attcard_no), NumberUtils.formatCardNumber(messModel.getSubCrcdNo()));

        attCardConfirmInfoView.addData(nameAndVelue, true, true);

    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_crcd_autopay_confirm_title);
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
    public void appertainAndMessSuccess(PsnCrcdQueryAppertainAndMessResult psnCrcdQueryAppertainAndMessResult) {

    }

    @Override
    public void appertainAndMessFailed(BiiResultErrorException exception) {

    }

    @Override
    public void setAppertainMessResultSuccess(PsnCrcdAppertainMessSetResultResult psnCrcdAppertainMessSetResultResult) {
        //跳转至交易明细页面，并且去调用Reinit方法，虽然交易明细页面没有这个方式。。
        //将短信设置的页面全部出栈，避免影响流程
        popToAndReInit(AttCardTranHistoryFragment.class);
        closeProgressDialog();
        //Toast显示设置成功信息
        Toast.makeText(mContext, getResources().getString(R.string.boc_crcd_attcard_message_setup_success), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setAppertainMessResultFailed(BiiResultErrorException exception) {
        //showErrorDialog("短信提醒设置失败，请重试");
    }

    @Override
    public void crcdCurrencyQuerySuccess(PsnCrcdCurrencyQueryResult mCurrencyQuery) {

    }

    @Override
    public void crcdCurrencyQueryFailed(BiiResultErrorException exception) {

    }

    @Override
    public void setPresenter(AttCardMessContract.AttCardMessPresenter presenter) {

    }

    @Override
    protected AttCardMessContract.AttCardMessPresenter initPresenter() {
        return new AttCardMessPresenter(this);
    }

}
