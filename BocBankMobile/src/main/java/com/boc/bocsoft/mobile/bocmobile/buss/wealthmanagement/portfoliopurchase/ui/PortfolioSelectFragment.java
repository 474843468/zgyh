package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.PortfolioProductInfoView.PortfolioProductInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.wealthmanagemenadviertisementview.WealthManagemenAdviertisementView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.presenter.PortfolioSelectPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui.PurchaseFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.data.WealthBundleData;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;
import java.util.BitSet;
import java.util.List;

/**
 * 选择被组合产品的列表
 * Created by wangtong on 2016/9/14.
 * 修改者：谢端阳，2016/11/18
 */
public class PortfolioSelectFragment extends MvpBussFragment<PortfolioSelectContract.Presenter>
        implements RadioGroup.OnCheckedChangeListener,
        WealthManagemenAdviertisementView.AdviertisementViewOnclickListener,
        PortfolioSelectContract.View {

    protected RadioGroup currencyLabel;
    protected ListView listView;
    protected TextView btnConfirm;
    protected RadioButton cash;
    protected RadioButton remit;
    protected RelativeLayout groupProduct;
    protected RelativeLayout groupEmpty;
    protected RelativeLayout groutCurrency;
    protected WealthManagemenAdviertisementView viewNoDataAdviertisement;
    private View rootView;

    private PortfolioPurchaseModel model;
    private PorfolioAdapter adapter;
    //初始化为进入该页面之前的选择状态。之后本页面的选择操作只修改对应BitSet的值，直到最后确认修改，才将BitSet的选择状态写回到产品列表里。
    private BitSet mySelectStates;
    private boolean isInit;//处于初始化状态，初始化时设置成true,结束后置为false。

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_portfolio_select, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        currencyLabel = (RadioGroup) rootView.findViewById(R.id.currency_label);
        listView = (ListView) rootView.findViewById(R.id.list_view);
        btnConfirm = (TextView) rootView.findViewById(R.id.btn_confirm);
        cash = (RadioButton) rootView.findViewById(R.id.cash);
        remit = (RadioButton) rootView.findViewById(R.id.remit);
        groupProduct = (RelativeLayout) rootView.findViewById(R.id.group_product);
        groupEmpty = (RelativeLayout) rootView.findViewById(R.id.group_empty);
        groutCurrency = (RelativeLayout) rootView.findViewById(R.id.grout_currency);
        currencyLabel.setOnCheckedChangeListener(this);
        viewNoDataAdviertisement = (WealthManagemenAdviertisementView) rootView.findViewById(
                R.id.view_no_data_adviertisement);
    }

    @Override
    public void initData() {
        super.initData();
        model = findFragment(PortfolioPurchaseFragment.class).model;
        isInit = true;
        if (PublicUtils.isEmpty(model.getMyProductList())) {
            mySelectStates = new BitSet();
        } else {
            saveOldSelectStates();
        }
        adapter = new PorfolioAdapter(mContext);
        listView.setAdapter(adapter);
        if (ApplicationConst.CURRENCY_CNY.equals(model.getCurCode())) {
            groutCurrency.setVisibility(View.GONE);
            model.setCashRemit("0");
            showOrHideDataView(!PublicUtils.isEmpty(model.getMyProductList()));
            adapter.setDatas(model.getMyProductList());
            return;
        }
        if ("0".equals(model.getCashRemit())) {
            //汇产品列表空且钞产品列表不空时，显示钞；其余显示汇
            if (PublicUtils.isEmpty(model.getRemitProductList()) && !PublicUtils.isEmpty(
                    model.getCashProductList())) {
                cash.setChecked(true);
            } else {
                remit.setChecked(true);
            }
        } else if ("1".equals(model.getCashRemit())) {
            cash.setChecked(true);
        } else if ("2".equals(model.getCashRemit())) {
            remit.setChecked(true);
        }
    }

    /**
     * 遍历保存进入界面之前的选中状态。
     */
    private void saveOldSelectStates() {
        mySelectStates = new BitSet(model.getMyProductList().size());
        mySelectStates.or(model.getMySelectStates());
    }

    @Override
    public void setListener() {
        super.setListener();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置钞汇标志
                if (!ApplicationConst.CURRENCY_CNY.equals(model.getCurCode())) {
                    model.setCashRemit(cash.isChecked() ? "1" : "2");
                }
                //清空旧的选择产品列表,重新添加被选择的产品列表
                updateSelectProductList();
                //更新model里保存的所有产品的选择状态BitSet
                model.setMySelectStates(mySelectStates);
                findFragment(PortfolioPurchaseFragment.class).adapter.refreshProductList();
                pop();
            }
        });
        viewNoDataAdviertisement.setTextOnclickListener(this);
    }

    /**
     * 清空旧的选择产品列表,重新添加被选择的产品
     */
    private void updateSelectProductList() {
        //获取旧的选择状态
        BitSet myOldSelectStates = model.getMySelectStates();
        //清空旧的选择产品列表
        model.getSelectProductList().clear();
        List<PortfolioPurchaseModel.Product> myProductList = model.getMyProductList();
        for (int i = 0; i < myProductList.size(); i++) {
            PortfolioPurchaseModel.Product product = myProductList.get(i);
            //当前被选中时
            if (mySelectStates.get(i)) {
                //如果之前没有被选中，修改组合份额。否则被组合份额不变。
                if (!myOldSelectStates.get(i)) {
                    if ("2".equals(model.getCashRemit())) {//如果标志时汇，被组合份额就等于汇；否则，就等于钞。人民币属于钞的情形。
                        product.portfolioAmount = product.remitShare;
                    } else {
                        product.portfolioAmount = product.cashShare;
                    }
                }
                //添加被选择的产品
                model.getSelectProductList().add(product);
            }
        }
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
    protected String getTitleValue() {
        return "添加可组合产品";
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        adapter.switchDataList();
    }

    @Override
    public void adviertisementViewOnclickListener() {
        //TODO 跳转到购买页面
        showLoadingDialog(false);
        getPresenter().queryProductDetail(model);
    }

    @Override
    protected PortfolioSelectContract.Presenter initPresenter() {
        return new PortfolioSelectPresenter(this);
    }

    @Override
    public void onQueryProductDetailSuccess(WealthDetailsBean wealthDetailsBean) {
        closeProgressDialog();
        PurchaseFragment purchaseFragment = PurchaseFragment.newInstance(
                WealthBundleData.buildBuyData(wealthDetailsBean,
                        findFragment(PortfolioPurchaseFragment.class).inputModel.getAccountBean()),
                null);
        start(purchaseFragment);
    }

    class PorfolioAdapter extends BaseListAdapter<PortfolioPurchaseModel.Product> {

        public PorfolioAdapter(Context context) {
            super(context);
        }

        public void switchDataList() {
            boolean isCashChecked = currencyLabel.getCheckedRadioButtonId() == R.id.cash;
            List<PortfolioPurchaseModel.Product> productList =
                    isCashChecked ? model.getCashProductList() : model.getRemitProductList();
            showOrHideDataView(!PublicUtils.isEmpty(productList));
            if (isInit) {
                isInit = false;
            } else {
                mySelectStates.clear();//切换钞汇标签时，清除选中状态
            }
            setDatas(productList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.boc_item_product_select, null);
                holder = new ViewHolder();
                holder.infoView =
                        (PortfolioProductInfoView) convertView.findViewById(R.id.group_head);
                holder.moneyLabel = (TextView) convertView.findViewById(R.id.money_label);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
                holder.amountEdit = (TextView) convertView.findViewById(R.id.purchase_amount);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PortfolioPurchaseModel.Product product = getItem(position);
            holder.infoView.setText(getString(R.string.boc_purchase_product_head_cur,
                    PublicCodeUtils.getCurrency(getActivity(), model.getCurCode())),
                    product.productName,
                    getString(R.string.boc_purchase_product_head_code, product.productCode));
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(mySelectStates.get(product.serialNumber));
            holder.setOnCheckedChangeListener(position);
            if (ApplicationConst.CURRENCY_CNY.equals(model.getCurCode())) {
                holder.moneyLabel.setText("持有份额");
                holder.amountEdit.setText(
                        MoneyUtils.transMoneyFormat(product.cashShare + "", model.getCurCode()));
            } else if (cash.isChecked()) {
                holder.moneyLabel.setText("持有现钞份额");
                holder.amountEdit.setText(
                        MoneyUtils.transMoneyFormat(product.cashShare + "", model.getCurCode()));
            } else {
                holder.moneyLabel.setText("持有现汇份额");
                holder.amountEdit.setText(
                        MoneyUtils.transMoneyFormat(product.remitShare + "", model.getCurCode()));
            }

            return convertView;
        }

        class ViewHolder {
            PortfolioProductInfoView infoView;
            TextView moneyLabel;
            CheckBox checkBox;
            TextView amountEdit;

            public void setOnCheckedChangeListener(final int position) {
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mySelectStates.set(getItem(position).serialNumber, isChecked);
                    }
                });
            }
        }
    }

    private void showOrHideDataView(boolean hasData) {
        if (hasData) {
            groupProduct.setVisibility(View.VISIBLE);
            groupEmpty.setVisibility(View.GONE);
            viewNoDataAdviertisement.setVisibility(View.GONE);
        } else {
            groupProduct.setVisibility(View.GONE);
            groupEmpty.setVisibility(View.VISIBLE);
            viewNoDataAdviertisement.setVisibility(View.VISIBLE);
        }
    }
}
