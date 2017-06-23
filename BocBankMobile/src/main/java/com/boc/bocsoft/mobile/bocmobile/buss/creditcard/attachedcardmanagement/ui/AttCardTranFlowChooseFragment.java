package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAppertainAndMess.PsnCrcdQueryAppertainAndMessResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardSetUpModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardTradeFlowModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter.AttCardTradeFlowContract;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter.AttCardTradeFlowPresenter;

/**
 * Name: liukai
 * Time：2016/12/5 9:50.
 * Created by lk7066 on 2016/12/5.
 * It's used to 附属卡交易流量分币种选择页面
 */

public class AttCardTranFlowChooseFragment extends MvpBussFragment<AttCardTradeFlowContract.AttCardTradeFlowPresenter> implements AttCardTradeFlowContract.AttCardTradeFlowView {

    private View mRootView = null;

    //两个框，需要两个线性布局
    private LinearLayout attCardTranFlowRMB, attCardTranFlowForeign;

    //两个编辑框
    private EditChoiceWidget attCardRMB, attCardForeign;
    private AttCardSetUpModel setUpModel;
    private AttCardTradeFlowModel tradeFlowModel;
    private AttCardTradeFlowContract.AttCardTradeFlowPresenter mPresenter;

    //设置成功跳转回来刷新的标志，根据标识的值去判断是哪种币种，然后去调用相应的接口
    //0，单外币去请求接口刷新；1，单人民币去请求接口刷新；2多币种去请求接口刷新
    private int refreshFlag = 0;

    private AttCardTranFlowInputFragment flowInputFragment;

    //用来保存多币种情况下，传递给下一个页面是本币的值，还是外币的值
    private String mutiCurrencyRMB, mutiCurrencyFor;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        super.onCreateView(mInflater);
        setUpModel = getArguments().getParcelable("History");
        mRootView = mInflater.inflate(R.layout.fragment_attcard_tranflow_choose, null);
        return mRootView;
    }

    @Override
    public void initView(){
        attCardTranFlowRMB = (LinearLayout) mRootView.findViewById(R.id.ll_attcard_rmb);
        attCardTranFlowForeign = (LinearLayout) mRootView.findViewById(R.id.ll_attcard_foreign);

        //初始化页面，只有两个编辑框，没有后面的金额值，后面的值需要请求接口，然后去刷新页面
        firstInitLoadView(setUpModel.getmCurrency());

    }

    @Override
    public void initData() {

        mPresenter = new AttCardTradeFlowPresenter(this);

        tradeFlowModel = new AttCardTradeFlowModel();

        //mCurrency值初始为-1，如果前一个页面查询币种成功，那么该标志位修改，如果失败，那么该值为-1，需要重新查询币种
        //mCurrency：-1，表示接口请求失败，重新查询；0，单外币；1，单人民币；2，多币种
        if(setUpModel.getmCurrency() != -1) {
            if (1 == setUpModel.getmCurrency()) {
                //单人民币
                getPresenter().queryAppertainAndMess(setUpModel, setUpModel.getCurrency1(), 1);
            } else if (0 == setUpModel.getmCurrency()) {
                //单外币
                getPresenter().queryAppertainAndMess(setUpModel, setUpModel.getCurrency1(), 0);
            } else {
                //多币种
                //查询设置的流量值，需要上送不同的币种代码，需要请求两次同一个接口，下面两支接口是一样的，分开请求，并行请求
                getPresenter().queryAppertainAndMess(setUpModel, setUpModel.getCurrency1(), 2);
                getPresenter().queryAppertainAndMessSecond(setUpModel, setUpModel.getCurrency2(), 2);
            }
        } else {
            getPresenter().queryCrcdCurrency(setUpModel.getCrcdNo());
        }


    }

    /**
     * 初始化界面，查询失败或者成功都先加载页面
     * */
    private void firstInitLoadView(int flag){

        if(1 == flag) {//单人民币

            attCardRMB = new EditChoiceWidget(mContext);
            attCardRMB.setChoiceTitleBold(true);
            attCardRMB.setChoiceTextName(PublicCodeUtils.getCurrency(mContext, setUpModel.getCurrency1()) + "流量");

            attCardRMB.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    refreshFlag = 1;
                    passData();
                    tradeFlowModel.setCurrency(setUpModel.getCurrency1());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Choose", tradeFlowModel);
                    flowInputFragment = new AttCardTranFlowInputFragment();
                    flowInputFragment.setArguments(bundle);
                    start(flowInputFragment);
                }

            });

            attCardTranFlowRMB.addView(attCardRMB);

            attCardTranFlowRMB.setVisibility(View.VISIBLE);
            attCardTranFlowForeign.setVisibility(View.GONE);

        } else if(0 == flag) {//单外币

            attCardForeign = new EditChoiceWidget(mContext);
            attCardForeign.setChoiceTitleBold(true);
            attCardForeign.setChoiceTextName(PublicCodeUtils.getCurrency(mContext, setUpModel.getCurrency1()) + "流量");

            attCardForeign.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    refreshFlag = 0;
                    passData();
                    tradeFlowModel.setCurrency(setUpModel.getCurrency1());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Choose", tradeFlowModel);
                    flowInputFragment = new AttCardTranFlowInputFragment();
                    flowInputFragment.setArguments(bundle);
                    start(flowInputFragment);
                }

            });

            attCardTranFlowForeign.addView(attCardForeign);

            attCardTranFlowRMB.setVisibility(View.GONE);
            attCardTranFlowForeign.setVisibility(View.VISIBLE);

        } else {//多币种

            //人民币
            attCardRMB = new EditChoiceWidget(mContext);
            attCardRMB.setChoiceTitleBold(true);
            attCardRMB.setChoiceTextName(PublicCodeUtils.getCurrency(mContext, setUpModel.getCurrency1()) + "流量");

            attCardRMB.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    refreshFlag = 2;
                    passData();
                    tradeFlowModel.setCurrency(setUpModel.getCurrency1());
                    tradeFlowModel.setAmount(mutiCurrencyRMB);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Choose", tradeFlowModel);
                    flowInputFragment = new AttCardTranFlowInputFragment();
                    flowInputFragment.setArguments(bundle);
                    start(flowInputFragment);
                }

            });

            attCardTranFlowRMB.addView(attCardRMB);
            attCardTranFlowRMB.setVisibility(View.VISIBLE);

            //外币
            attCardForeign = new EditChoiceWidget(mContext);
            attCardForeign.setChoiceTitleBold(true);
            attCardForeign.setChoiceTextName(PublicCodeUtils.getCurrency(mContext, setUpModel.getCurrency2()) + "流量");

            attCardForeign.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    refreshFlag = 2;
                    passData();
                    tradeFlowModel.setCurrency(setUpModel.getCurrency2());
                    tradeFlowModel.setAmount(mutiCurrencyFor);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Choose", tradeFlowModel);
                    flowInputFragment = new AttCardTranFlowInputFragment();
                    flowInputFragment.setArguments(bundle);
                    start(flowInputFragment);
                }

            });

            attCardTranFlowForeign.addView(attCardForeign);
            attCardTranFlowForeign.setVisibility(View.VISIBLE);

        }

    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_crcd_attcard_tranflow_title);
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

    /**
     * 如果接口查询成功，初始化界面值，把流量值写入到编辑框内
     * flag标志位：0，单人民币；1，单外币；2，多币种人民币；3，多币种外币
     * */
    public void loadViewData(final PsnCrcdQueryAppertainAndMessResult result, int flag){

        String money = MoneyUtils.getRoundNumber(String.valueOf(result.getTradeFlowAmount()), 2);

        if(0 == flag) {//单人民币

            attCardRMB.setChoiceTextContent(MoneyUtils.transMoneyFormat(money, setUpModel.getCurrency1()));
            attCardRMB.getChoiceContentTextView().setTextColor(getResources().getColor(R.color.boc_text_color_green));
            tradeFlowModel.setAmount(String.valueOf(result.getTradeFlowAmount()));

        } else if(1 == flag) {//单外币

            attCardForeign.setChoiceTextContent(MoneyUtils.transMoneyFormat(money, setUpModel.getCurrency1()));
            attCardForeign.getChoiceContentTextView().setTextColor(getResources().getColor(R.color.boc_text_color_green));
            tradeFlowModel.setAmount(String.valueOf(result.getTradeFlowAmount()));

        } else if(2 == flag) {//多币种人民币

            attCardRMB.setChoiceTextContent(MoneyUtils.transMoneyFormat(money, setUpModel.getCurrency1()));
            attCardRMB.getChoiceContentTextView().setTextColor(getResources().getColor(R.color.boc_text_color_green));
            mutiCurrencyRMB = String.valueOf(result.getTradeFlowAmount());

        } else {//多币种外币

            attCardForeign.setChoiceTextContent(MoneyUtils.transMoneyFormat(money, setUpModel.getCurrency2()));
            attCardForeign.getChoiceContentTextView().setTextColor(getResources().getColor(R.color.boc_text_color_green));
            mutiCurrencyFor = String.valueOf(result.getTradeFlowAmount());

        }

    }

    /**
     * 从设置的model中把值传给交易流量的model，交易流量的model还需要其他的值
     * */
    public void passData(){
        tradeFlowModel.setAccountId(setUpModel.getAccountId());
        tradeFlowModel.setAccountNo(setUpModel.getCrcdNo());
        tradeFlowModel.setSubCrcdNo(setUpModel.getSubCrcdNo());
        tradeFlowModel.setSubCardName(setUpModel.getAttCardName());
        tradeFlowModel.setMasterCrcdType(setUpModel.getMasterCrcdType());
    }

    /**
     * 设置成功以后跳转至此页面，调用这个方法去刷新页面
     * */
    @Override
    public void reInit(){
        super.reInit();
        closeProgressDialog();

        //跳转至此页面，首先将原来的元素全部去除掉，重新初始化页面
        attCardTranFlowRMB.setVisibility(View.GONE);
        attCardTranFlowForeign.setVisibility(View.GONE);
        attCardTranFlowRMB.removeView(attCardRMB);
        attCardTranFlowForeign.removeView(attCardForeign);

        //初始化页面
        firstInitLoadView(setUpModel.getmCurrency());

        switch (refreshFlag) {
            case 0:
                getPresenter().queryAppertainAndMess(setUpModel, setUpModel.getCurrency1(), 0);
                break;
            case 1:
                getPresenter().queryAppertainAndMess(setUpModel, setUpModel.getCurrency1(), 1);
                break;
            case 2:
                getPresenter().queryAppertainAndMess(setUpModel, setUpModel.getCurrency1(), 2);
                getPresenter().queryAppertainAndMessSecond(setUpModel, setUpModel.getCurrency2(), 2);
                break;
            default:
                break;
        }

    }

    /**
     * 附属卡短信和流量查询返回结果
     * */
    @Override
    public void appertainAndMessSuccess(PsnCrcdQueryAppertainAndMessResult psnCrcdQueryAppertainAndMessResult, int flag) {
        loadViewData(psnCrcdQueryAppertainAndMessResult, flag);
    }

    @Override
    public void appertainAndMessFailed(BiiResultErrorException exception, int flag) {
    }

    @Override
    public void appertainAndMessSecondSuccess(PsnCrcdQueryAppertainAndMessResult psnCrcdQueryAppertainAndMessResult, int flag) {
        loadViewData(psnCrcdQueryAppertainAndMessResult, flag);
    }

    @Override
    public void appertainAndMessSecondFailed(BiiResultErrorException exception, int flag) {

    }

    @Override
    public void querySecurityFactorSuccess(SecurityFactorModel securityFactorModel) {

    }

    @Override
    public void querySecurityFactorFailed(BiiResultErrorException exception) {

    }

    @Override
    public void setAppertainTranConfirmSuccess(VerifyBean verifyBean) {

    }

    @Override
    public void setAppertainTranConfirmFailed(BiiResultErrorException exception) {

    }

    @Override
    public void crcdCurrencyQuerySuccess(PsnCrcdCurrencyQueryResult mCurrencyQuery) {

        if(mCurrencyQuery.getCurrency2() != null){
            setUpModel.setCurrency2(mCurrencyQuery.getCurrency2().getCode());
            setUpModel.setmCurrency(2);
        } else if(mCurrencyQuery.getCurrency1().getCode().equals("001")) {
            setUpModel.setmCurrency(1);
        } else {
            setUpModel.setmCurrency(0);
        }
        setUpModel.setCurrency1(mCurrencyQuery.getCurrency1().getCode());

        if (1 == setUpModel.getmCurrency()) {
            //单人民币
            getPresenter().queryAppertainAndMess(setUpModel, setUpModel.getCurrency1(), 1);
        } else if (0 == setUpModel.getmCurrency()) {
            //单外币
            getPresenter().queryAppertainAndMess(setUpModel, setUpModel.getCurrency1(), 0);
        } else {
            //多币种
            getPresenter().queryAppertainAndMess(setUpModel, setUpModel.getCurrency1(), 2);
            getPresenter().queryAppertainAndMessSecond(setUpModel, setUpModel.getCurrency2(), 2);
        }

    }

    @Override
    public void crcdCurrencyQueryFailed(BiiResultErrorException exception) {

    }

    @Override
    public void setPresenter(AttCardTradeFlowContract.AttCardTradeFlowPresenter presenter) {

    }

    @Override
    protected AttCardTradeFlowContract.AttCardTradeFlowPresenter initPresenter() {
        return new AttCardTradeFlowPresenter(this);
    }

}
