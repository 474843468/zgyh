package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyExchangeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuyExchangeConfirmInfoPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuyExchangeContract;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.common.BuyExchangeCurrencyUtil;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;

import java.util.LinkedHashMap;

/**
 * 购汇确认信息页面
 * Created by gwluo on 2016/12/8.
 */

public class BuyExchangeConfirmInfoFragment extends MvpBussFragment<BuyExchangeConfirmInfoPresenter> implements BuyExchangeContract.ConfirmView {
    private ConfirmInfoView confirmDetail;
    private BuyExchangeModel model;//页面model

    @Override
    protected BuyExchangeConfirmInfoPresenter initPresenter() {
        return new BuyExchangeConfirmInfoPresenter(mContext, this);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        View mRoot = mInflater.inflate(R.layout.fragment_buy_exchange_confirm_info, null);
        return mRoot;
    }

    @Override
    public void initView() {
        confirmDetail = mViewFinder.find(R.id.confirmMsg);
        super.initView();
    }

    @Override
    public void initData() {
        Bundle arguments = getArguments();
        model = (BuyExchangeModel) arguments.get("model");

        confirmDetail.setHeadValue("购汇金额（" + PublicCodeUtils.getCurrency(mContext, model.getCurrency()) +
                        " " + PublicCodeUtils.getCashSpot(mContext, model.getCashRemit()) + "）",
                MoneyUtils.transMoneyFormat(model.getTransAmount(), model.getCurrency()));
        LinkedHashMap confirmMap = new LinkedHashMap<>();
        confirmMap.put("基准牌价参考", BuyExchangeCurrencyUtil.formatExchangeRate(model.getReferenceRate()));
        confirmMap.put("优惠后牌价参考", BuyExchangeCurrencyUtil.formatExchangeRate(model.getExchangeRate()));
        confirmMap.put("参考金额", "人民币元 " + MoneyUtils.transMoneyFormat(model.getRMBCost(), ApplicationConst.CURRENCY_CNY));
        confirmMap.put("账户", model.getPayerAccount());
        confirmMap.put("资金用途", model.getMoneyUse());
        confirmDetail.isShowSecurity(false);
        confirmDetail.setHint(getResources().getString(R.string.boc_buy_exc_notice), R.color.boc_text_color_red);
        confirmDetail.addData(confirmMap);
        super.initData();
    }

    @Override
    public void setListener() {
        confirmDetail.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
                getPresenter().psnFessBuyExchangeHibs();
            }

            @Override
            public void onClickChange() {

            }
        });
        super.setListener();
    }

    @Override
    protected String getTitleValue() {
        return "确认信息";
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
    public void onSubmit() {
        BuyExchangeResultFragment fragment = new BuyExchangeResultFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        fragment.setArguments(bundle);
        start(fragment);
        // 保存账户id
        BocCloudCenter.getInstance().updateLastAccountId(AccountType.ACC_TYPE_FESS_BUY_EXCHANGE,
                model.getAccountId());
    }

    @Override
    public void setPresenter(BuyExchangeContract.Presenter presenter) {

    }

    public BuyExchangeModel getModel() {
        return model;
    }
}
