package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.adapter.HistoryCombinationProductAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadQueryGuarantyProductListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadQueryGuarantyProductResultModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.presenter.HistoryComPurchaseDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import java.util.List;

/**
 * * Fragment：中银理财-历史交易-组合购买交易详情
 * Created by zc on 2016/9/12
 *
 */
public class HistoryComPurchaseDetailFragment extends BussFragment implements HistoryComPurchaseDetailContract.View {

    private View rootView;
    private HistoryComPurchaseDetailContract.Presenter mHistoryComPurchaseDetailPresenter;

    private TextView tv_pay_currency;
    private TextView tv_pay_amount;
    private TextView product_name;
    private TextView product_code;
    private DetailTableRow oprate_date;
    private ListView lv_com_product;

    private LinearLayout ll_head_layout;
    private LinearLayout ll_list_layout;

    private String currencyCode;//币种
    private String cashRemit;

    private XpadQueryGuarantyProductListViewModel.QueryGuarantyProductListEntity mQueryGuarantyProductListEntity;
    private XpadAccountQueryViewModel.XPadAccountEntity currentQueryAccount;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_history_com_purchase_detail, null);
        return rootView;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            HistoryComPurchaseDetailFragment.this.pop();
        }
    };

    @Override
    public void initView() {
        tv_pay_currency = (TextView) rootView.findViewById(R.id.pay_currency);
        tv_pay_amount = (TextView) rootView.findViewById(R.id.pay_amount);
        product_name = (TextView) rootView.findViewById(R.id.product_name);
        product_code = (TextView) rootView.findViewById(R.id.product_code);
        oprate_date = (DetailTableRow) rootView.findViewById(R.id.detail_operate_date);
        lv_com_product = (ListView) rootView.findViewById(R.id.lv_list);
        ll_head_layout = (LinearLayout) rootView.findViewById(R.id.head_layout);
        ll_list_layout = (LinearLayout) rootView.findViewById(R.id.list_layout);

        ll_head_layout.setVisibility(View.GONE);
        ll_list_layout.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        mHistoryComPurchaseDetailPresenter = new HistoryComPurchaseDetailPresenter(this);
        mQueryGuarantyProductListEntity = getArguments().getParcelable("hisComTradEntity");
        currentQueryAccount = getArguments().getParcelable("currentQueryAccount");

        XpadQueryGuarantyProductResultModel viewModel = new XpadQueryGuarantyProductResultModel();
        viewModel.setAccountKey(currentQueryAccount.getAccountKey());
        viewModel.setTranSeq(mQueryGuarantyProductListEntity.getTranSeq());
        viewModel.setIbknum(currentQueryAccount.getIbkNumber());
        viewModel.setTypeOfAccount(currentQueryAccount.getAccountType());

        mHistoryComPurchaseDetailPresenter.psnXpadQueryCombinationGuarantyProductResult(viewModel);

        currencyCode = PublicCodeUtils.getCurrency(getActivity(),mQueryGuarantyProductListEntity.getCurrency());
        //// 钞汇标识（01：钞 02：汇 00：人民币钞汇）
        if ("1".equals(mQueryGuarantyProductListEntity.getCashRemit())){
            cashRemit = "/钞";
        }else if ("2".equals(mQueryGuarantyProductListEntity.getCashRemit())){
            cashRemit = "/汇";
        }else if ("0".equals(mQueryGuarantyProductListEntity.getCashRemit())){
            cashRemit = "";
        }

        currencyCode = PublicCodeUtils.getCurrency(getActivity(),mQueryGuarantyProductListEntity.getCurrency());
        if("人民币元".equals(currencyCode)) {
            currencyCode="元";
        }
        tv_pay_currency.setText("成交金额 ("+currencyCode+cashRemit+")");
        // 设置界面上显示的数据
        tv_pay_amount.setText(MoneyUtils.transMoneyFormat(mQueryGuarantyProductListEntity.getBuyAmt(),mQueryGuarantyProductListEntity.getCurrency()));
//        product_name.addTextAndButtonContent(getResources().getString(R.string.boc_wealth_product_name),mQueryGuarantyProductListEntity.getProdName()," ("+mQueryGuarantyProductListEntity.getProdCode()+")");


//        product_name.setRightTvListener(new DetailContentView.DetailContentRightTvOnClickListener() {
//            @Override
//            public void onClickRightTextView() {
//
//            }
//        });
        product_name.setText(mQueryGuarantyProductListEntity.getProdName());
        product_code.setText("("+mQueryGuarantyProductListEntity.getProdCode()+")");
        oprate_date.updateValue(mQueryGuarantyProductListEntity.getReturnDate().format(DateFormatters.dateFormatter1));

    }

    @Override
    public void setListener() {
        product_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WealthDetailsFragment detailsFragment = new WealthDetailsFragment();
                Bundle bundle = new Bundle();
                DetailsRequestBean detailsRequestBean = new DetailsRequestBean();
                detailsRequestBean.setProdCode(mQueryGuarantyProductListEntity.getProdCode());
                detailsRequestBean.setProdKind(mQueryGuarantyProductListEntity.getProductKind());
                detailsRequestBean.setIbknum(currentQueryAccount.getIbkNumber());
                bundle.putBoolean(WealthDetailsFragment.OTHER, true);
                bundle.putParcelable(WealthDetailsFragment.DETAILS_REQUEST, detailsRequestBean);
                detailsFragment.setArguments(bundle);
                start(detailsFragment);
            }
        });
    }

    @Override
    protected String getTitleValue() {
        return "明细";
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
        mHistoryComPurchaseDetailPresenter.unsubscribe();
        super.onDestroy();
    }

    /**
     * 成功回调：查询组合购买详情成功
     */
    @Override
    public void psnXpadQueryCombinationProductComSuccess(List<XpadQueryGuarantyProductResultModel.QueryGuarantyProductResultEntity> viewModel) {
        ll_head_layout.setVisibility(View.VISIBLE);
        ll_list_layout.setVisibility(View.VISIBLE);
        List<XpadQueryGuarantyProductResultModel.QueryGuarantyProductResultEntity>  viewModelList= viewModel;
        lv_com_product.setAdapter(new HistoryCombinationProductAdapter(mActivity, viewModelList));
    }
    /**
     * 失败回调：查询组合购买详情失败
     */
    @Override
    public void psnXpadQueryCombinationGuarantyProductFail(BiiResultErrorException biiResultErrorException) {

    }
}
