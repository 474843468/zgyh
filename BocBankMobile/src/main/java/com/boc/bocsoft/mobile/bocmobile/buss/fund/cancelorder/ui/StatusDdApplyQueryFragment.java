package com.boc.bocsoft.mobile.bocmobile.buss.fund.cancelorder.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.cancelorder.model.PsnFundStatusDdApplyQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.cancelorder.presenter.CancelOrderContract;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.cancelorder.presenter.PsnFundStatusDdApplyQueryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/11/21.
 */

public class StatusDdApplyQueryFragment extends BussFragment implements CancelOrderContract.PsnFundStatusDdApplyQueryView {
    private View rootView;
    private PullToRefreshLayout pullToRefreshLayout;
    private PinnedSectionListView listView;

    private TextView textView;
    private PsnFundStatusDdApplyQueryPresenter presenter;
    private PsnFundStatusDdApplyQueryModel model;

    private ShowListAdapter listAdapter;
    private boolean isQueryByPullToRefresh = false;
    private int currentIndex = -1;//条数（非页数）

    private static final int pageSize = ApplicationConst.PAGE_SIZE;
    private boolean bHasRequest = false; //记录是否请求过记录，如果已经请求过数据，则当页面有隐藏变为可见时，不主动请求
    private boolean bHasInitView = false; //view是否已经初始化，当页面展示时，如果未请求过网络数据，并且页面已经初始化，则发起网络请求

    private boolean bVisibleBeforeInit = false; //是否初始化前可见，是则主动请求数据

    private static final String DES_PREFIX = "定期定额";

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fund_transontran,null);
        bHasInitView = true;
        return rootView;

    }

    @Override
    public void initView() {
        pullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.other_loan_online_query_view);
        listView = (PinnedSectionListView) rootView.findViewById(R.id.lvUseRecord);
        textView = (TextView) rootView.findViewById(R.id.tvNoData);
    }

    @Override
    public void initData() {
        presenter = new PsnFundStatusDdApplyQueryPresenter(this);
        currentIndex = 0;
        model = new PsnFundStatusDdApplyQueryModel();
        listAdapter = new ShowListAdapter(mContext,-1);
        listView.saveAdapter(listAdapter);
        listView.setAdapter(listAdapter);
        if (bVisibleBeforeInit) {
            queryDate();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser == true && !bHasRequest) {
            bVisibleBeforeInit = true;
        }
        if (bHasInitView && !bHasRequest && isVisibleToUser) {
            queryDate();
        }
    }



    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }



    @Override
    public void setListener() {
        super.setListener();
        pullToRefreshLayout.setOnLoadListener(new  PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                boolean bCanLoadMore = !(model.getResultList() != null
                        && model.getResultList().size() >= model.getRecordNumber());
                if(bCanLoadMore){
                    isQueryByPullToRefresh = true;
                    queryDate();
                }
                else{
                    pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                }
            }
        });

    }

    private void queryDate(){
        if(!isQueryByPullToRefresh){
            showLoadingDialog();
        }
        model.setRefresh("false");
        model.setCurrentIndex(String.valueOf(currentIndex));
        model.setPageSize(String.valueOf(pageSize));
        presenter.psnFundStatusDdApplyQuery(model);
    }

    @Override
    public void psnFundStatusDdApplyQuerySuccess(PsnFundStatusDdApplyQueryModel model) {

        closeProgressDialog();
        bHasRequest = true;
        boolean isNodate = (model == null || model.getRecordNumber() <= 0 || model.getResultList() == null
                || model.getResultList().size() <= 0);

        if (isNodate) {
            if (isQueryByPullToRefresh) {
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.FAIL);
            } else {
                showNodate();
            }

        }else {
            if (isQueryByPullToRefresh) {
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            }
            refleshListDate(model);
            refleshListView();

        }
    }



    private void refleshListDate(PsnFundStatusDdApplyQueryModel resultmodel){
        if (model.getResultList()== null) {
            List<PsnFundStatusDdApplyQueryModel.ResultListBean> listBeens = new ArrayList<>();
            model.setResultList(listBeens);
        }
        model.setRecordNumber(resultmodel.getRecordNumber());
        model.getResultList().addAll(resultmodel.getResultList());
        currentIndex = model.getResultList().size();
    }

    private void refleshListView() {
        List<ShowListBean>listViewBeanList = new ArrayList<>();
        List<PsnFundStatusDdApplyQueryModel.ResultListBean> viewModelList = model.getResultList();
        for (int i = 0; i < viewModelList.size(); i++){
            String loanApplyDate = viewModelList.get(i).getApplyDate();//"2016/12/14"
            LocalDate localDate = LocalDate.parse(loanApplyDate, DateFormatters.dateFormatter1);//"2016/12/14"

            String formatTime = localDate.format(DateFormatters.monthFormatter1);//"MM月/yyyy"
            String tempFormatTime = "";//上一次时间
            if (i > 0){
                String tempTime = viewModelList.get(i - 1).getApplyDate();
                LocalDate FormatTime = LocalDate.parse(tempTime,DateFormatters.dateFormatter1);
                tempFormatTime = FormatTime.format(DateFormatters.monthFormatter1);

            }

            //交易描述
            String fundName = viewModelList.get(i).getFundName();
            if(StringUtils.isEmptyOrNull(fundName)){
                fundName = "-";
            }
            String tranTypeDes = getLefeBottomDes(viewModelList.get(i));
            //DataUtils.getTransTypeDes(mContext,viewModelList.get(i).getFundTranType(),viewModelList.get(i).getBonusType());
            String rightAboveDes = getRightAboveDes(viewModelList.get(i));
            String rightBottomDes = getRightBottomDes(viewModelList.get(i));
            if (formatTime.equals(tempFormatTime)){
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;
                item.setTitleID(ShowListConst.TITLE_WEALTH);
                item.setTime(localDate);

                item.setContentLeftAbove(fundName);//基金名称
                item.setContentLeftBelow(tranTypeDes);//基金交易类型
                item.setContentRightAbove(rightAboveDes);
                item.setContentRightBelow(rightBottomDes);
                listViewBeanList.add(item);
            } else{
                for (int j = 0; j < 2; j++){
                    ShowListBean itemFirst = new ShowListBean();
                    if (j == 0){
                        itemFirst.type = ShowListBean.GROUP;
                        itemFirst.setGroupName(formatTime);
                        itemFirst.setTime(localDate);
                    }else {
                        itemFirst.type = ShowListBean.CHILD;
                        itemFirst.setTitleID(ShowListConst.TITLE_WEALTH);
                        itemFirst.setTime(localDate);
                        itemFirst.setContentLeftAbove(fundName);//交易详情
                        itemFirst.setContentLeftBelow(tranTypeDes);
                        itemFirst.setContentRightAbove(rightAboveDes);
                        itemFirst.setContentRightBelow(rightBottomDes);
                    }
                    listViewBeanList.add(itemFirst);
                }
            }


        }
        listAdapter.setData(listViewBeanList);

    }


    @Override
    public void psnFundStatusDdApplyQueryFail(BiiResultErrorException error) {

    }

    private void showNodate(){
        textView.setText(getString(R.string.boc_fund_ontran_nodate));
        textView.setVisibility(View.VISIBLE);
        pullToRefreshLayout.setVisibility(View.GONE);
    }

    private String getRightAboveDes(PsnFundStatusDdApplyQueryModel.ResultListBean bean){
        String content = DataUtils.getStatusDdRecordStatusDes(mContext,bean.getTransType(),bean.getRecordStatus());
        return content;
    }

    private String getRightBottomDes(PsnFundStatusDdApplyQueryModel.ResultListBean bean) {
        String result = MoneyUtils.transMoneyFormat(bean.getApplyAmount(),bean.getCurrency());
        return result + DataUtils.getFundUnit(mContext,bean.getTransType(),"",bean.getCurrency());
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public void onDestroy() {
        presenter.unsubscribe();
        super.onDestroy();
    }

    private String getLefeBottomDes(PsnFundStatusDdApplyQueryModel.ResultListBean bean) {
        String result = DES_PREFIX;
        return result + DataUtils.getStatusDdApplytranstypeDes(mContext,bean.getTransType());
    }

}
