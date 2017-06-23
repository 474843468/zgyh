package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.finance;

import android.content.Context;
import android.util.AttributeSet;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.ProductsData;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.PledgeLoanTypeView;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;

/**
 *
 */
public class PledgeLoanTypeFinanceView extends PledgeLoanTypeView {

    private ProductsData mProductsData;

    public PledgeLoanTypeFinanceView(Context context) {
        super(context);
    }

    public PledgeLoanTypeFinanceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PledgeLoanTypeFinanceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        super.initView();
        tvTitle.setText(getContext().getString(R.string.boc_loan_pledge_title_finance));
        tvDescription.setText(getContext().getString(R.string.boc_pledge_description_finance));
        tvEmpty.setText(R.string.boc_pledge_product_empty);
    }

    @Override
    protected void applyLoan() {
        mBussFragment.start(
                PledgeLoanFinanceProductSelectFragment.newInstance(mProductsData));
    }

    public void onLoadSuccess(ProductsData productsData) {
        mProductsData =productsData;
        boolean hasData = !PublicUtils.isEmpty(mProductsData.getPledgeProductBeanList());
        showData(hasData);
        if (hasData) {
            tvCount.setText(String.format(getContext().getString(R.string.boc_pledge_count_finance),
                    mProductsData.getPledgeProductBeanList().size()));
        }
    }

    public void onLoadFailed() {
        showData(false);
    }
}