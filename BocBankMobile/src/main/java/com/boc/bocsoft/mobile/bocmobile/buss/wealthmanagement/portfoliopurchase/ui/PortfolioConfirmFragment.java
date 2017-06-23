package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.ui;

import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by wangtong on 2016/9/17.
 */
public class PortfolioConfirmFragment extends MvpBussFragment<PortfolioPurchaseContact.Presenter>
        implements PortfolioPurchaseContact.ConfirmView {

    protected ConfirmInfoView confirmView;
    private View rootView;

    private PortfolioPurchaseModel model;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_portfolio_confirm, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        confirmView = (ConfirmInfoView) rootView.findViewById(R.id.confirm_view);
    }

    @Override
    public void initData() {
        super.initData();
        model = findFragment(PortfolioPurchaseFragment.class).model;
        getPresenter().setConfirmView(this);

        confirmView.isShowSecurity(false);
        String currency = PublicCodeUtils.getCurrency(getActivity(), model.getCurCode());
        if (model.getCashRemit().equals("1")) {
            currency = currency + "/钞";
        } else if (model.getCashRemit().equals("2")) {
            currency = currency + "/汇";
        }
        String payAmount =
                MoneyUtils.transMoneyFormat(model.getPayAmount() + "", model.getCurCode());
        confirmView.setHeadValue(getString(R.string.boc_purchase_confirm_head, currency),
                payAmount);
        LinkedHashMap<String, String> datas = new LinkedHashMap<>();
        datas.put("产品名称", model.getProdName() + "(" + model.getProdCode() + ")");
        datas.put("被组合购买产品总数", model.getPortfolioNum() + "");
        if (model.getProductRisk().equals("0")) {
            datas.put("产品风险等级", "低风险产品");
        } else if (model.getProductRisk().equals("1")) {
            datas.put("产品风险等级", "中低风险产品");
        } else if (model.getProductRisk().equals("2")) {
            datas.put("产品风险等级", "中等风险产品");
        } else if (model.getProductRisk().equals("3")) {
            datas.put("产品风险等级", "中高风险产品");
        } else if (model.getProductRisk().equals("4")) {
            datas.put("产品风险等级", "高风险产品");
        }

        if (model.getCustomerRisk().equals("1")) {
            datas.put("客户风险等级", "保守型投资者");
        } else if (model.getCustomerRisk().equals("2")) {
            datas.put("客户风险等级", "稳健型投资者");
        } else if (model.getCustomerRisk().equals("3")) {
            datas.put("客户风险等级", "平衡型投资者");
        } else if (model.getCustomerRisk().equals("4")) {
            datas.put("客户风险等级", "成长型投资者");
        } else if (model.getCustomerRisk().equals("5")) {
            datas.put("客户风险等级", "进取型投资者");
        }

        if (model.getRiskMessage().equals("0")) {
            confirmView.setHint("本理财产品有投资风险，只能保证获得合同明确承诺的收益，您应充分认识投资风险，谨慎投资");
        } else if (model.getRiskMessage().equals("1")) {
            confirmView.setHint("本理财产品有投资风险，只保障理财资金本金，不保证理财收益，您应当充分认识投资风险，谨慎投资");
        } else if (model.getRiskMessage().equals("2")) {
            confirmView.setHint(
                    "本产品为非保本浮动收益理财产品，并不保证理财资金本金和收益，投资者可能会因市场变动而蒙受不同程度的损失，投资者应充分认识投资风险，谨慎投资");
        }
        confirmView.addData(datas, true, false);
        confirmView.isShowSecurity(false);
    }

    @Override
    public void setListener() {
        super.setListener();
        confirmView.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
                showLoadingDialog(false);
                getPresenter().psnXpadGuarantyBuyResult();
            }

            @Override
            public void onClickChange() {

            }
        });
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        pop();
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "确认信息";
    }

    @Override
    public void psnXpadGuarantyBuyResultReturned(List<WealthListBean> wealthListBeen) {
        // TODO 最终刷新数据后的会带哦
        if (!PublicUtils.isEmpty(wealthListBeen)){
            onQueryProductListSuccess(wealthListBeen);
        }else {
            onQueryProductListFail();
        }
    }

    public void onQueryProductListSuccess(List<WealthListBean> wealthListBeen) {
        closeProgressDialog();
        start(PortfolioResultFragment.newInstance(PublicUtils.convertToArrayList(wealthListBeen)));
    }

    public void onQueryProductListFail() {
        closeProgressDialog();
        //TODO 需要设置数据
        start(new PortfolioResultFragment());
    }

    @Override
    protected PortfolioPurchaseContact.Presenter initPresenter() {
        return findFragment(PortfolioPurchaseFragment.class).getPresenter();
    }
}
