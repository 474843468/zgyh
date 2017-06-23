package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccountBalance.PsnFessQueryAccountBalanceResult;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyExchangeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuyExchangeContract;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.common.BuyExchangeCurrencyUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.model.SellAccountBalanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.presenter.AccBalanceAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.presenter.SellExchangeCurrencyPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginMainFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 选择币种的界面
 * Created by gwluo on 2016/12/21.
 */

public class CurrencyAccFragment extends MvpBussFragment<SellExchangeCurrencyPresenter> implements BuyExchangeContract.CurrencyAccView {
    protected BuyExchangeModel model = new BuyExchangeModel();//页面model
    private View root;
    private ListView listview;
    private AccBalanceAdapter accBalanceAdapter;
    private boolean isSetResult;//关闭时是否用setFragmentResult
    public static final int RESULT_CODE_CURRENCY = 1;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        root = mInflater.inflate(R.layout.boc_view_simple_listview, null);
        return root;
    }

    public BuyExchangeModel getModel() {
        return model;
    }

    @Override
    public void initView() {
        listview = mViewFinder.find(R.id.listview);
    }

    @Override
    public void reInit() {
        initView();
        initData();
        setListener();
    }

    @Override
    public void initData() {
        if (!ApplicationContext.getInstance().isLogin()) {
            start(new LoginMainFragment());
            return;
        }
        accBalanceAdapter = new AccBalanceAdapter(mContext, this);
        listview.setAdapter(accBalanceAdapter);
        Bundle arguments = getArguments();
        if (arguments != null) {
            isSetResult = arguments.getBoolean("isSetResult");
            //由币种点击进入
            if (isSetResult) {
                model = (BuyExchangeModel) arguments.getSerializable("globalModel");
                updateBalanceView();
            } else {
                getPresenter().getAccountList();
            }
        }
    }

    @Override
    public void setListener() {
        listview.setOnItemClickListener(onCurrencyClick);
    }

    private AdapterView.OnItemClickListener onCurrencyClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (isSetResult) {
                Bundle bundle = new Bundle();
                SellAccountBalanceModel itemData = accBalanceAdapter.getData().get(position);
                bundle.putSerializable("item", itemData);
                setFramgentResult(RESULT_CODE_CURRENCY, bundle);
                pop();
            } else {
                ArrayList<SellAccountBalanceModel> data = accBalanceAdapter.getData();
                SellAccountBalanceModel item = data.get(position);
                model.setAccountId(item.getAccountId());
                model.setCurrency(item.getCurrency());
                model.setCashRemit(item.getCashRemit());
                SellExchangeFragment fragment = new SellExchangeFragment();
                Bundle bundle = new Bundle();
                //账户、币种和余额
                bundle.putSerializable("accAndBalance", data);
                //整个交易的model
                bundle.putSerializable("globalModel", model);
                bundle.putInt("position", position);
                fragment.setArguments(bundle);
                start(fragment);
            }
        }
    };

    @Override
    protected SellExchangeCurrencyPresenter initPresenter() {
        return new SellExchangeCurrencyPresenter(this);
    }

    private int accRequestBalance = 0;//账户请求余额接口剩余

    @Override
    public void onAccListSucc() {
        String identityType = model.getIdentityType();
        if (BuyExchangeCurrencyUtil.isSupportSellExchange(identityType)) {
            if (model.getAccList().size() > 0) {
//                AccountBean accountBean = null;
                accRequestBalance = model.getAccList().size();
//                accountBean = model.getAccList().get(accIndex);
                model.getBalanceMap().clear();
                for (int i = 0; i < model.getAccList().size(); i++) {
                    getPresenter().getPsnFessQueryAccountBalance(model.getAccList().get(i).getAccountId(), false);
                }
            } else {
                closeProgressDialog();
                showErrorDialog("无可用交易账户，本功能支持活期一本通账户或借记卡（主账户为活期一本通）");
                ((BaseMobileActivity) getActivity()).setErrorDialogClickListener(new BaseMobileActivity.ErrorDialogClickCallBack() {
                    @Override
                    public void onEnterBtnClick() {
                        pop();
                    }
                });
            }
        } else {
            closeProgressDialog();
            showErrorDialog(getString(R.string.boc_not_support_buysell));
            ((BaseMobileActivity) getActivity()).setErrorDialogClickListener(new BaseMobileActivity.ErrorDialogClickCallBack() {
                @Override
                public void onEnterBtnClick() {
                    pop();
                }
            });
        }

    }

    public void getSingleAccBalance(String id) {
        accRequestBalance = 1;
        getPresenter().getPsnFessQueryAccountBalance(id, true);
    }


    @Override
    public void updateAccBalance(boolean isSingle) {
        accRequestBalance--;
        if (accRequestBalance == 0) closeProgressDialog();
        if (model.getBalanceMap().size() == 0) {
            showErrorDialog("无可结汇币种");
            ((BaseMobileActivity) getActivity()).setErrorDialogClickListener(new BaseMobileActivity.ErrorDialogClickCallBack() {
                @Override
                public void onEnterBtnClick() {
                    pop();
                }
            });
        }
//        if (isSingle) {
//            closeProgressDialog();
        updateBalanceView();
//        } else {
//            accRequestBalance++;
//            if (accRequestBalance + 1 <= model.getAccList().size()) {
//                getPresenter().getPsnFessQueryAccountBalance(model.getAccList().get(accRequestBalance).getAccountId(), false);
//            } else {
//                closeProgressDialog();
//                updateBalanceView();
//            }
//        }
    }

    /**
     * 设置余额数据
     */
    private void updateBalanceView() {
        Map<String, List<PsnFessQueryAccountBalanceResult>> balanceMap = model.getBalanceMap();
        Set<String> keys = balanceMap.keySet();
        Iterator<String> iterator = keys.iterator();
        ArrayList<SellAccountBalanceModel> balanceModelList = new ArrayList<>();
        while (iterator.hasNext()) {
            String accId = iterator.next();
            AccountBean accountBean = BuyExchangeCurrencyUtil.getAccFromList(model, accId);
            List<PsnFessQueryAccountBalanceResult> balanceList = balanceMap.get(accId);
            if (balanceList == null) {//请求余额失败
                SellAccountBalanceModel balanceModel = new SellAccountBalanceModel();
                balanceModel.setAccountId(accId);
                balanceModel.setAccountNum(accountBean.getAccountNumber());
                balanceModel.setAccountType(accountBean.getAccountType());
                balanceModel.setBalanceFail(true);
                balanceModelList.add(balanceModel);
            } else {
                String lastId = "";//记录上一个币种所属账户id
                for (PsnFessQueryAccountBalanceResult item : balanceList) {
                    SellAccountBalanceModel balanceModel = new SellAccountBalanceModel();
                    //每个账户第一个余额显示账号
                    balanceModel.setShowTitle(!lastId.equals(accId));
                    lastId = accId;
                    balanceModel.setAccountId(accId);
                    balanceModel.setAccountNum(accountBean.getAccountNumber());
                    balanceModel.setAccountType(accountBean.getAccountType());
                    balanceModel.setCurrency(item.getCurrency());
                    balanceModel.setCashRemit(item.getCashRemit());
                    balanceModel.setAvailableBalance(
                            MoneyUtils.transMoneyFormat(item.getAvailableBalance(), item.getCurrency()));
                    balanceModel.setBalanceFail(false);
                    balanceModelList.add(balanceModel);
                }
            }
        }
        accBalanceAdapter.update(balanceModelList);

    }

    @Override
    public void setPresenter(BuyExchangeContract.Presenter presenter) {

    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_select_sell_exchange_curr);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }
}
