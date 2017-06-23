package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.PortfolioProductInfoView.PortfolioProductInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyNoTitleWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseInputModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.presenter.PortfolioPurchasePersenter;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

/**
 * 组合购买
 * Created by wangtong on 2016/9/12.
 * 修订者：谢端阳
 */
public class PortfolioPurchaseFragment extends MvpBussFragment<PortfolioPurchaseContact.Presenter>
        implements PortfolioPurchaseContact.MainView {

    public static final String INPUT_MODEL = "input_model";
    protected ListView portfolio;
    protected PortfolioProductInfoView productHead;
    private View rootView;

    public PortfolioPurchaseInputModel inputModel;
    public PortfolioPurchaseModel model;
    public String productCurrency;

    public PorfolioAdapter adapter;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_portfolio_purchase, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        portfolio = (ListView) rootView.findViewById(R.id.portfolio);
        productHead = (PortfolioProductInfoView) rootView.findViewById(R.id.product_head);
    }

    @Override
    public void initData() {
        super.initData();
        inputModel = getArguments().getParcelable(INPUT_MODEL);
        model = new PortfolioPurchaseModel(inputModel);

        productCurrency = PublicCodeUtils.getCurrency(getActivity(), model.getCurCode());
        productHead.setText(getString(R.string.boc_purchase_product_head_cur, productCurrency),
                model.getProdName(),
                getString(R.string.boc_purchase_product_head_code, model.getProdCode()));

        adapter = new PorfolioAdapter(mContext);
        adapter.addFootView(portfolio);
        portfolio.setAdapter(adapter);
        getPresenter().psnXpadQueryGuarantyProductList();
    }

    @Override
    public void setListener() {
        super.setListener();
        //mTitleBarView.setRightButton("帮助", new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        start(new PortfolioHelpFragment());
        //    }
        //});
    }

    @Override
    public void reInit() {
        super.reInit();
        model.getMyProductList().clear();
        model.clearSelectProductList();
        adapter.refreshProductList();
        getPresenter().psnXpadQueryGuarantyProductList();
    }

    //@Override
    //protected void titleRightIconClick() {
    //    start(new PortfolioHelpFragment());
    //}

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
        return "组合购买";
    }

    @Override
    public void psnXpadQueryGuarantyProductListReturned() {

    }

    @Override
    protected PortfolioPurchaseContact.Presenter initPresenter() {
        return new PortfolioPurchasePersenter(this);
    }

    class PorfolioAdapter extends BaseListAdapter<PortfolioPurchaseModel.Product>
            implements View.OnClickListener {

        TextView addButton;
        TextView nextButton;

        public PorfolioAdapter(Context context) {
            super(context);
            setDatas(model.getSelectProductList());
        }

        public void refreshProductList() {
            if (getCount() > 0) {
                nextButton.setVisibility(View.VISIBLE);
            } else {
                nextButton.setVisibility(View.GONE);
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView =
                        View.inflate(getContext(), R.layout.boc_item_portfolio_purchase, null);
                holder = new ViewHolder();
                holder.infoView =
                        (PortfolioProductInfoView) convertView.findViewById(R.id.group_head);
                holder.deleteBtn = (ImageView) convertView.findViewById(R.id.btn_delete);
                holder.amountEdit =
                        (EditMoneyNoTitleWidget) convertView.findViewById(R.id.purchase_amount);
                holder.amountEdit.setContentMoneyEditTextColor(
                        getResources().getColor(R.color.boc_text_color_red));
                holder.amountEdit.setMaxLeftNumber(12);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            PortfolioPurchaseModel.Product product = getItem(position);
            holder.infoView.setText(
                    getString(R.string.boc_purchase_product_head_cur, productCurrency),
                    product.productName,
                    getString(R.string.boc_purchase_product_head_code, product.productCode));
            holder.amountEdit.setCurrency(model.getCurCode());
            if ("0".equals(model.getCashRemit())) {
                holder.amountEdit.setContentHint(
                        "不得大于" + MoneyUtils.transMoneyFormat(String.valueOf(product.cashShare),
                                model.getCurCode()));
            } else if ("1".equals(model.getCashRemit())) {
                holder.amountEdit.setContentHint(
                        "不得大于" + MoneyUtils.transMoneyFormat(String.valueOf(product.cashShare),
                                model.getCurCode()));
            } else {
                holder.amountEdit.setContentHint(
                        "不得大于" + MoneyUtils.transMoneyFormat(String.valueOf(product.remitShare),
                                model.getCurCode()));
            }
            holder.amountEdit.setmContentMoneyEditText(product.portfolioAmount + "");
            holder.setOnDeleteButtonClicked(position);
            holder.setMoneyInputChanged(position);
            return convertView;
        }

        public void addFootView(ListView listView) {
            View footView =
                    View.inflate(getContext(), R.layout.boc_item_portfolio_purchase_foot, null);
            addButton = (TextView) footView.findViewById(R.id.add_purchase);
            nextButton = (TextView) footView.findViewById(R.id.btn_next);
            addButton.setOnClickListener(this);
            nextButton.setOnClickListener(this);
            listView.addFooterView(footView);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.add_purchase) {
                start(new PortfolioSelectFragment());
            } else if (id == R.id.btn_next) {
                model.setPortfolioAmount(0);
                for (int i = 0; i < model.getSelectProductList().size(); i++) {
                    PortfolioPurchaseModel.Product product = model.getSelectProductList().get(i);
                    model.setPortfolioAmount(model.getPortfolioAmount() + product.portfolioAmount);
                }
                model.setPortfolioNum(model.getSelectProductList().size());
                start(new PortfolioBuyFragment());
            }
        }

        class ViewHolder {
            PortfolioProductInfoView infoView;
            ImageView deleteBtn;
            EditMoneyNoTitleWidget amountEdit;

            public void setOnDeleteButtonClicked(final int position) {
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PortfolioPurchaseModel.Product product = getDatas().remove(position);
                        model.getMySelectStates().set(product.serialNumber,false);
                        refreshProductList();
                    }
                });
            }

            public void setMoneyInputChanged(final int position) {
                //amountEdit.getContentMoneyEditText().onClick(amountEdit);
                amountEdit.setEditWidgetListener(new EditMoneyNoTitleWidget.EditWidgetListener() {
                    @Override
                    public void onClear() {
                        amountEdit.getContentMoneyEditText().onClick(amountEdit);
                        //ErrorDialog errorDialog = new ErrorDialog(mContext);
                        //errorDialog.setBtnText(getString(R.string.boc_common_sure));
                        //errorDialog.setErrorData(
                        //        getString(R.string.boc_portfolio_combined_shares_empty));
                        //errorDialog.setOnBottomViewClickListener(
                        //        new ErrorDialog.OnBottomViewClickListener() {
                        //            @Override
                        //            public void onBottomViewClick() {
                        //                amountEdit.setmContentMoneyEditText(
                        //                        adapter.getItem(position).portfolioAmount + "");
                        //            }
                        //        });
                        //errorDialog.show();
                    }
                });
                amountEdit.setOnKeyBoardListener(
                        new EditMoneyNoTitleWidget.KeyBoardDismissOrShowCallBack() {
                            @Override
                            public void onKeyBoardDismiss() {
                                PortfolioPurchaseModel.Product product = getItem(position);
                                if (TextUtils.isEmpty(amountEdit.getContentMoney())) {
                                    product.portfolioAmount = -1;
                                    if (model.getCashRemit().equals("2")) {
                                        amountEdit.setmContentMoneyEditText(
                                                product.remitShare + "");
                                        product.portfolioAmount = product.remitShare;
                                    } else {
                                        amountEdit.setmContentMoneyEditText(product.cashShare + "");
                                        product.portfolioAmount = product.cashShare;
                                    }
                                    showErrorDialog(getString(
                                            R.string.boc_portfolio_combined_shares_empty));
                                } else {
                                    double inputValue =
                                            Double.parseDouble(amountEdit.getContentMoney());
                                    if (inputValue == 0) {
                                        if (model.getCashRemit().equals("2")) {
                                            amountEdit.setmContentMoneyEditText(
                                                    product.remitShare + "");
                                            product.portfolioAmount = product.remitShare;
                                        } else {
                                            amountEdit.setmContentMoneyEditText(
                                                    product.cashShare + "");
                                            product.portfolioAmount = product.cashShare;
                                        }
                                        showErrorDialog(getString(
                                                R.string.boc_portfolio_combined_shares_out_limit));
                                    } else if (model.getCashRemit().equals("0") && (inputValue
                                            > product.cashShare)) {
                                        amountEdit.setmContentMoneyEditText(product.cashShare + "");
                                        product.portfolioAmount = product.cashShare;
                                        showErrorDialog(getString(
                                                R.string.boc_portfolio_combined_shares_out_limit));
                                    } else if (model.getCashRemit().equals("1") && (inputValue
                                            > product.cashShare)) {
                                        amountEdit.setmContentMoneyEditText(product.cashShare + "");
                                        product.portfolioAmount = product.cashShare;
                                        showErrorDialog(getString(
                                                R.string.boc_portfolio_combined_shares_out_limit));
                                    } else if (model.getCashRemit().equals("2")
                                            && inputValue > product.remitShare) {
                                        amountEdit.setmContentMoneyEditText(
                                                product.remitShare + "");
                                        product.portfolioAmount = product.remitShare;
                                        showErrorDialog(getString(
                                                R.string.boc_portfolio_combined_shares_out_limit));
                                    } else {
                                        product.portfolioAmount = inputValue;
                                    }
                                }
                            }

                            @Override
                            public void onKeyBoardShow() {
                            }
                        });
            }
        }
    }
}
