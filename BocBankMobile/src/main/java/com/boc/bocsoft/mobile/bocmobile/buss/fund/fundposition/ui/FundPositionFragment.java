package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.bocmobile.base.widget.expandableitemview.ExpandableItemView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.adapter.FundPositionAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.model.FundFloatingProfileLossModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.model.FundPositionModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.presenter.FundPositionPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/6.
 * 持仓首页
 */

public class FundPositionFragment extends MvpBussFragment<FundPositionContract.Presenter> implements FundPositionContract.FundPositionView, View.OnClickListener {
    private View rootView;
    private ListView listView;
    private SpannableString noDateText;
    private LinearLayout noDateParent;

    private LinearLayout headParent;
    private TextView tvChedule;
    private TextView tvPersional;
    private RelativeLayout rlContainer;
    private ExpandableItemView totalView;
    private FundPositionAdapter adapter;
    private FundPositionModel fragmentModel;
    private View viewDevide;
    private View expandBottomDevide;

    //最大查询范围为3个月
    private final static int MAX_QUERY_RANGE = 12;
    //最大查询起始日期为一年
    private final static int MAX_QUERY_DATE = 12;
    //起始时间
    private LocalDate startLocalDate;
    //结束时间
    private LocalDate endLocalDate;
    private String fundCode = "";
    private com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.model.FundFloatingProfileLossModel.ResultListBean resultListBean;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fund_position, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        listView = (ListView) rootView.findViewById(R.id.list_view);
        noDateParent = (LinearLayout) rootView.findViewById(R.id.ll_no_date);
        noDateText = (SpannableString) rootView.findViewById(R.id.tv_nodate);
    }

    @Override
    public void initData() {
        super.initData();
        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusDays(-1);
        getFundPosition();
    }

    @Override
    public void setListener() {
        super.setListener();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position <= 0 || position > fragmentModel.getFundBalance().size()) {
                    return;
                }
                FundPositionDetailFragment fundPositionDetailFragment = new FundPositionDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(DataUtils.FUND_POSITION_BEAN_KEY, fragmentModel.getFundBalance().get(position-1));
                bundle.putParcelable(DataUtils.FUND_FLOAT_PROFILELESS_BEAN_KEY,getFloatProfileLossBean( fragmentModel.getFundBalance().get(position-1).getFundCode()));
                fundPositionDetailFragment.setArguments(bundle);
                start(fundPositionDetailFragment);
            }
        });
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fund_position_title);
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
    protected FundPositionContract.Presenter initPresenter() {
        return new FundPositionPresenter(this);
    }


    private void getFundPosition() {
        showLoadingDialog();
        // 基金code
        FundPositionModel model = new FundPositionModel();
//        model.setFundCode(fundCode);
        getPresenter().QueryFundBalance(model);
    }

    @Override
    public void queryFundBalanceSuccess(FundPositionModel result) {
        if (result == null || result.getFundBalance() == null || result.getFundBalance().size() <= 0) {
            closeProgressDialog();
            showHeadView();
            showNoDate();
            return;
        }
        if (adapter == null) {
            adapter = new FundPositionAdapter(mContext);
            listView.setAdapter(adapter);
        }
        fragmentModel = result;
        FundFloatingProfileLossModel model = new FundFloatingProfileLossModel();
        model.setStartDate(startLocalDate.format(DateFormatters.dateFormatter1));
        model.setEndDate(endLocalDate.format(DateFormatters.dateFormatter1));
        getPresenter().queryFloatingProfitLoss(model);
        showHeadView();
        adapter.setBeans(fragmentModel.getFundBalance());
    }

    @Override
    public void queryFundBalanceFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showHeadView();
        showNoDate();

    }

    @Override
    public void queryFloatingProfitLossFail(BiiResultErrorException exception) {
        closeProgressDialog();
//        adapter.setBeans(fragmentModel.getFundBalance());
    }

    @Override
    public void queryFloatingProfitLossSuccess(FundFloatingProfileLossModel model) {
        closeProgressDialog();
        if (model != null && model.getResultList().size() > 0) {

            adapter.setProfileLessBean(model.getResultList());
        }

    }

    @Override
    public void setPresenter(FundPositionContract.Presenter presenter) {

    }

    private void showHeadView() {
        //此处总计需要计算，依据currency排序
        if (headParent == null) {
            headParent = (LinearLayout) View.inflate(mContext, R.layout.boc_fragment_fund_positon_head, null);
            totalView = (ExpandableItemView) headParent.findViewById(R.id.ex_item);
            rlContainer = (RelativeLayout) headParent.findViewById(R.id.rl_container);
            tvChedule = (TextView) headParent.findViewById(R.id.tv_left);
            tvChedule.setOnClickListener(this);
            tvPersional = (TextView) headParent.findViewById(R.id.tv_right);
            tvPersional.setOnClickListener(this);
            viewDevide = (View) headParent.findViewById(R.id.v_devide);
            expandBottomDevide = (View) headParent.findViewById(R.id.v_bottom_line);
            totalView.isShowRightImg(false);
            totalView.setHeadTextContent(getString(R.string.boc_fund_position_refer_value_default));
        }
        List<ExpandableItemView.ItemView> allDatebens = initTotalBean();
        if (allDatebens == null || allDatebens.size() <= 0) {
            totalView.setVisibility(View.GONE);
            expandBottomDevide.setVisibility(View.GONE);
            return;
        }
        totalView.setItems(allDatebens);
        if (listView.getHeaderViewsCount() <= 0) {
            listView.addHeaderView(headParent);
        }

    }

    private void showNoDate() {
        totalView.setVisibility(View.GONE);
        expandBottomDevide.setVisibility(View.GONE);
        rlContainer.setVisibility(View.VISIBLE);
        viewDevide.setVisibility(View.GONE);
        if (adapter == null) {
            adapter = new FundPositionAdapter(mContext);
            listView.setAdapter(adapter);
        }
        adapter.setBeans(new ArrayList<FundPositionModel.FundBalanceBean>());
        noDateText.setContent(getString(R.string.boc_fund_position_nodate_hint), getString(R.string.boc_fund_position_ondeate_hint_end),
                getString(R.string.boc_fund_position_nodate_hint_sub), false, R.color.boc_text_color_common_gray, R.color.boc_main_button_color,
                new MClickableSpan.OnClickSpanListener() {
                    @Override
                    public void onClickSpan() {

                    }
                });
        noDateParent.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
        //基金定投管理
        if (v.getId() == R.id.tv_left) {
            return;
        }
        //基金对账单
        if (v.getId() == R.id.tv_right) {
            return;
        }


    }

    private List<ExpandableItemView.ItemView> initTotalBean() {
        List<TotalBean> totalBeens = new ArrayList<>();
        for (FundPositionModel.FundBalanceBean bean : fragmentModel.getFundBalance()) {
            TotalBean totalBean = new TotalBean();
            totalBean.setCurrency(bean.getFundInfo().getCurrency());
            totalBean.setCarshFlag(bean.getFundInfo().getCashFlag());
            totalBean.setCurrentCapitalisation(bean.getCurrentCapitalisation());
            totalBeens.add(totalBean);
        }
        Collections.sort(totalBeens, new TotalBeanCompare());
        ArrayList<ExpandableItemView.ItemView> dateBeans = new ArrayList<>();
        BigDecimal bdTotalAmount = new BigDecimal(0);
        for (int i = 0; i < totalBeens.size() + 1; i++) {
            if (i == 0) {
                bdTotalAmount = new BigDecimal(0);
            } else if (i >= totalBeens.size() || !totalBeens.get(i).getCurrency().equals(totalBeens.get(i - 1).getCurrency())) {
                ExpandableItemView.ItemView datebean = new ExpandableItemView.ItemView();
                datebean.setRightContent(MoneyUtils.transMoneyFormat(bdTotalAmount, totalBeens.get(i - 1).getCurrency()));
                String unit = DataUtils.getCurrencyAndCashFlagDesSpecalRMB(mContext,totalBeens.get(i - 1).getCurrency(),totalBeens.get(i - 1).getCarshFlag());
                datebean.setLeftContent(unit);
                dateBeans.add(datebean);
                bdTotalAmount = new BigDecimal(0);
                if (i >= totalBeens.size()) {
                    break;
                }
            }
            bdTotalAmount = bdTotalAmount.add(new BigDecimal(totalBeens.get(i).currentCapitalisation));

        }
        return dateBeans;

    }

    static class TotalBean {
        String currency;
        String carshFlag;
        String currentCapitalisation;

        public TotalBean() {
        }

        public TotalBean(String currency, String carshFlag, String currentCapitalisation) {
            this.currency = currency;
            this.carshFlag = carshFlag;
            this.currentCapitalisation = currentCapitalisation;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCarshFlag() {
            return carshFlag;
        }

        public void setCarshFlag(String carshFlag) {
            this.carshFlag = carshFlag;
        }

        public String getCurrentCapitalisation() {
            return currentCapitalisation;
        }

        public void setCurrentCapitalisation(String currentCapitalisation) {
            this.currentCapitalisation = currentCapitalisation;
        }
    }
    /*
    static class TotalBeanCompare implements Comparator<FundPositionModel.FundBalanceBean> {
        @Override
        public int compare(FundPositionModel.FundBalanceBean lhs, FundPositionModel.FundBalanceBean rhs) {
            if (Integer.valueOf(lhs.getFundInfo().getCurrency()) > Integer.valueOf(rhs.getFundInfo().getCurrency())) {
                return 1;
            } else if (Integer.valueOf(lhs.getFundInfo().getCurrency()) == Integer.valueOf(rhs.getFundInfo().getCurrency())) {
                return 0;
            }
            return -1;
        }
    }
    */

    static class TotalBeanCompare implements Comparator<TotalBean> {
        @Override
        public int compare(TotalBean lhs, TotalBean rhs) {
            if (Integer.valueOf(lhs.getCurrency()) > Integer.valueOf(rhs.getCurrency())) {
                return 1;
            } else if (Integer.valueOf(lhs.getCurrency()) == Integer.valueOf(rhs.getCurrency())) {
                return 0;
            }

            return -1;
        }
    }

    private  com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.model.FundFloatingProfileLossModel.ResultListBean getFloatProfileLossBean(String fundcode){
        if (StringUtil.isNullOrEmpty(fundcode)){
            return null;
        }
        for (FundFloatingProfileLossModel.ResultListBean bean:adapter.getProfileLessBean()){
            if (fundcode.equals(bean.getFundCode())){
                com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.model.FundFloatingProfileLossModel.ResultListBean resultListBean = BeanConvertor.toBean(bean,
                        new com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.model.FundFloatingProfileLossModel.ResultListBean());
                return resultListBean;
            }
        }
        return null;
    }

}
