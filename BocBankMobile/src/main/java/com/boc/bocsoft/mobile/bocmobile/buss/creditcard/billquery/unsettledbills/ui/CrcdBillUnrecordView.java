package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.container.MvpContainer;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.model.CrcdBillQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.model.CrcdUnsettledBillDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.model.CrcdUnsettledBillsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.presenter.CrcdBillNPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.widget.CrcdBillInfoItemView;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * 未出账单 -- 未入账
 * Created by wangf on 2016/12/21.
 */

public class CrcdBillUnrecordView extends MvpContainer<CrcdBillNPresenter> implements CrcdBillNContract.UnrecordView,BaseView<CrcdBillNPresenter>,PullToRefreshLayout.OnLoadListener ,PinnedSectionListView.ClickListener{

    private View rootView;
    private CrcdBillInfoItemView billInfoItemView;
    private PullToRefreshLayout flRefresh;
    private PinnedSectionListView transactionView;
    private TextView tv_no_result;
    private CrcdBillQueryViewModel billQueryModle;

    private int currentPage=0;
    private final int PAGE_SIZE=10;
    private List<ShowListBean> transactionBeans;
    private List<CrcdUnsettledBillDetailModel.CrcdTransactionListBean> transBeanList;
    private ShowListAdapter mAdapter;

    private boolean isLoaded=false;
    private boolean isNoMoreData=false;


    public CrcdBillUnrecordView(Context context) {
        super(context);
    }

    public CrcdBillUnrecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CrcdBillUnrecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View createContentView() {
        rootView=inflate(getContext(), R.layout.boc_crcd_bill_view,null);
        return rootView;
    }


    @Override
    protected void initView() {
        super.initView();

        billInfoItemView=new CrcdBillInfoItemView(getContext());
        billInfoItemView.setBackgroundColor(getResources().getColor(R.color.boc_common_cell_color));
        billInfoItemView.setNoResultText(getResources().getString(R.string.boc_crcd_bill_y_bill_unrecord_no_result));
        billInfoItemView.setVisibility(GONE);


        flRefresh = (PullToRefreshLayout) rootView.findViewById(R.id.fl_refresh);
        transactionView = (PinnedSectionListView) rootView.findViewById(R.id.tran_detail);
    }

    @Override
    protected void initData() {
        super.initData();
        transactionView.addHeaderView(billInfoItemView, null, false);
        mAdapter = new ShowListAdapter(getContext(), -1);
        transactionView.saveAdapter(mAdapter);
        transactionView.setShadowVisible(false);
        transactionView.setAdapter(mAdapter);
    }

    /**
     *  设置加载数据
     */
    public void setData(AccountBean accountBean){

        billQueryModle=new CrcdBillQueryViewModel();
        billQueryModle.setAccountId(accountBean.getAccountId());
        billQueryModle.setPageSize(PAGE_SIZE+"");
        billQueryModle.set_refresh("true");
    }


    /**
     *  查询交易列表数据
     */
    private void loadDetailListData() {
        showLoadingDialog();
        billQueryModle.setCurrentIndex(currentPage+"");
        getPresenter().crcdQueryUnauthorizedTrans(billQueryModle);
    }

    @Override
    protected void setListener() {
        super.setListener();
        flRefresh.setOnLoadListener(this);
        transactionView.setListener(this);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isLoaded&&isVisibleToUser){
            getPresenter().crcdQueryUnauthorizedTransToatal(billQueryModle);
            loadDetailListData();
        }
    }

    @Override
    protected CrcdBillNPresenter initPresenter() {
        return new CrcdBillNPresenter(this);
    }

    @Override
    public void setPresenter(CrcdBillNPresenter presenter) {

    }

    @Override
    public void crcdQueryUnauthorizedTrans(CrcdUnsettledBillDetailModel crcdBillQueryModel) {
        isLoaded=true;
        currentPage++;
        closeProgressDialog();

        if (crcdBillQueryModel!=null&&crcdBillQueryModel.getCrcdTransactionList().size()>0){
            mAdapter.setData(generateTransactionBean(crcdBillQueryModel));
        }else {
            isNoMoreData=true;
            billInfoItemView.setNoResultVisble(true);
        }

        if (!StringUtil.isNullOrEmpty(crcdBillQueryModel.getRecordNumber())&&Integer.parseInt(crcdBillQueryModel.getRecordNumber())>currentPage*PAGE_SIZE){
            flRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
        }else{
            isNoMoreData=true;
            flRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
        }
    }

    @Override
    public void crcdQueryUnauthorizedTransToatal(List<CrcdUnsettledBillsModel> crcdUnsettledBillsModels) {
        billInfoItemView.setVisibility(VISIBLE);
        if (crcdUnsettledBillsModels!=null&&crcdUnsettledBillsModels.size()>1){
            billInfoItemView.setBottomDividerVisble(true);
            billInfoItemView.setLeftContentText("支出",
                    PublicCodeUtils.getCurrency(getContext(),crcdUnsettledBillsModels.get(0).getCurrency())+" "+MoneyUtils.transMoneyFormat(crcdUnsettledBillsModels.get(0).getDebitSum(),crcdUnsettledBillsModels.get(0).getCurrency()),
                    PublicCodeUtils.getCurrency(getContext(),crcdUnsettledBillsModels.get(1).getCurrency())+" "+MoneyUtils.transMoneyFormat(crcdUnsettledBillsModels.get(1).getDebitSum(),crcdUnsettledBillsModels.get(1).getCurrency()));
            billInfoItemView.setRightContentText("收入",
                    PublicCodeUtils.getCurrency(getContext(),crcdUnsettledBillsModels.get(0).getCurrency())+" "+MoneyUtils.transMoneyFormat(crcdUnsettledBillsModels.get(0).getCreditSum(),crcdUnsettledBillsModels.get(0).getCurrency()),
                    PublicCodeUtils.getCurrency(getContext(),crcdUnsettledBillsModels.get(1).getCurrency())+" "+MoneyUtils.transMoneyFormat(crcdUnsettledBillsModels.get(0).getCreditSum(),crcdUnsettledBillsModels.get(1).getCurrency()));
            billInfoItemView.getTvLeftTop().setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
            billInfoItemView.getTvRightTop().setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
            billInfoItemView.getTvLeftMiddle().setTextColor(Double.parseDouble(crcdUnsettledBillsModels.get(0).getDebitSum().replace(",",""))>0?
                    getResources().getColor(R.color.boc_text_color_dark_gray):getResources().getColor(R.color.boc_text_color_rest_gray));
            billInfoItemView.getTvRightMiddle().setTextColor(Double.parseDouble(crcdUnsettledBillsModels.get(0).getCreditSum().replace(",",""))>0?
                    getResources().getColor(R.color.boc_text_color_dark_gray):getResources().getColor(R.color.boc_text_color_rest_gray));
            billInfoItemView.getTvLeftBottom().setTextColor(Double.parseDouble(crcdUnsettledBillsModels.get(1).getDebitSum().replace(",",""))>0?
                    getResources().getColor(R.color.boc_text_color_dark_gray):getResources().getColor(R.color.boc_text_color_rest_gray));
            billInfoItemView.getTvRightBottom().setTextColor(Double.parseDouble(crcdUnsettledBillsModels.get(1).getCreditSum().replace(",",""))>0?
                    getResources().getColor(R.color.boc_text_color_dark_gray):getResources().getColor(R.color.boc_text_color_rest_gray));

        }else if (crcdUnsettledBillsModels!=null&&crcdUnsettledBillsModels.size()==1){
            billInfoItemView.setBottomDividerVisble(true);
            billInfoItemView.setLeftContentText("支出",
                    PublicCodeUtils.getCurrency(getContext(),crcdUnsettledBillsModels.get(0).getCurrency())+" "+MoneyUtils.transMoneyFormat(crcdUnsettledBillsModels.get(0).getDebitSum(),crcdUnsettledBillsModels.get(0).getCurrency()));
            billInfoItemView.setRightContentText("收入",
                    PublicCodeUtils.getCurrency(getContext(),crcdUnsettledBillsModels.get(0).getCurrency())+" "+MoneyUtils.transMoneyFormat(crcdUnsettledBillsModels.get(0).getCreditSum(),crcdUnsettledBillsModels.get(0).getCurrency()));
            billInfoItemView.getTvLeftTop().setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
            billInfoItemView.getTvRightTop().setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
            billInfoItemView.getTvLeftMiddle().setTextColor(Double.parseDouble(crcdUnsettledBillsModels.get(0).getDebitSum().replace(",",""))>0?
                    getResources().getColor(R.color.boc_text_color_dark_gray):getResources().getColor(R.color.boc_text_color_rest_gray));
            billInfoItemView.getTvRightMiddle().setTextColor(Double.parseDouble(crcdUnsettledBillsModels.get(0).getCreditSum().replace(",",""))>0?
                    getResources().getColor(R.color.boc_text_color_dark_gray):getResources().getColor(R.color.boc_text_color_rest_gray));
        }else {
            billInfoItemView.setContainerVisble(false);
            billInfoItemView.setNoResultVisble(true);
        }


    }

    /**
     * 根据交易明细生成TransactionView需要的TransactionBean
     * @return
     */
    private List<ShowListBean> generateTransactionBean(CrcdUnsettledBillDetailModel crcdBillQueryModel) {
        if (transactionBeans==null)
            transactionBeans = new ArrayList<>();

        transBeanList=crcdBillQueryModel.getCrcdTransactionList();

        for (int i = 0; i < transBeanList.size(); i++) {
            LocalDate localDate = LocalDate.parse(transBeanList.get(i).getTransDate().replace("/","-"));
            String formatTime = "";// 当前时间 MM月/yyyy
            String tempTime = "";// 上一次时间
            if (localDate != null) {
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0) {
                tempTime = LocalDate.parse(transBeanList.get(i-1).getTransDate().replace("/","-")).format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)) {// child
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;
                item.setTitleID(ShowListConst.TITLE_CRCD_BILL);
                item.setTime(localDate);
                //存入类型
                if ("0".equals(transBeanList.get(i).getDebitCreditFlag())){
                    item.setChangeColor(true);
                    item.setContentLeftBelow("[存入]");
                }
                item.setContentLeftAbove(transBeanList.get(i).getRemark());
//                item.setContentRightAbove(PublicCodeUtils.getCurrency(getContext(), transBeanList.get(i).getBookCurrency()));
                item.setContentRightBelow(PublicCodeUtils.getCurrency(getContext(), transBeanList.get(i).getTranCurrency())+MoneyUtils.transMoneyFormat(transBeanList.get(i).getTranAmount(),transBeanList.get(i).getTranCurrency()));

                transactionBeans.add(item);
            } else {// group
                for (int j = 0; j < 2; j++) {
                    ShowListBean itemFirst = new ShowListBean();
                    if (j == 0) {
                        itemFirst.type = ShowListBean.GROUP;
                        itemFirst.setGroupName(formatTime);
                        itemFirst.setTime(localDate);
                    } else {
                        itemFirst.type = ShowListBean.CHILD;
                        itemFirst.setTitleID(ShowListConst.TITLE_CRCD_BILL);
                        itemFirst.setTime(localDate);
                        //存入类型
                        if ("0".equals(transBeanList.get(i).getDebitCreditFlag())){
                            itemFirst.setChangeColor(true);
                            itemFirst.setContentLeftBelow("[存入]");
                        }

                        itemFirst.setContentLeftAbove(transBeanList.get(i).getRemark());
                        itemFirst.setContentRightBelow(PublicCodeUtils.getCurrency(getContext(), transBeanList.get(i).getTranCurrency())+MoneyUtils.transMoneyFormat(transBeanList.get(i).getTranAmount(),transBeanList.get(i).getTranCurrency()));
                    }
                    transactionBeans.add(itemFirst);
                }
            }
        }
        return transactionBeans;
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (!isNoMoreData){
            billQueryModle.set_refresh("false");
            loadDetailListData();
        }
        else
            flRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
    }

    @Override
    public void onItemClickListener(int position) {
        gotoTransDetailFragment(transBeanList.get(position));
    }

    /**
     *  跳转详情页面
     */
    private void gotoTransDetailFragment(CrcdUnsettledBillDetailModel.CrcdTransactionListBean transDetail) {
        CrcdBillDetailsNFragment detailsFragment=new CrcdBillDetailsNFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(CrcdBillDetailsNFragment.DETAIL_INFO,transDetail);
        detailsFragment.setArguments(bundle);
        mBussFragment.start(detailsFragment);
    }
}
