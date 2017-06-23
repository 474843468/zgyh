package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.container.BussContainer;
import com.boc.bocsoft.mobile.bocmobile.base.container.MvpContainer;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.model.PersionaltrsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.presenter.StatementPresenter;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huixiaobo on 2016/11/24.
 * 交易流水列表
 */
public class PersionltrsView extends BussContainer{
    /**view*/
    protected View rootView;
    /**上下文*/
    protected Context mContext;
    /**交易流水列表显示组件*/
    protected PinnedSectionListView persiontrsLv;
    /**交易流水适配器*/
    private ShowListAdapter  showListAdapter;
    /**list日期bean*/
    private List<ShowListBean> listViewBeanList;
    /**交易流水为空提示组件*/
    protected TextView persiontrsHit;
    /**交易流水数据列表*/
    private List<PersionaltrsModel> mPersionaltrsList;

    public PersionltrsView(Context context) {
        super(context);
        mContext = context;
    }

    public PersionltrsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PersionltrsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View createContentView() {
        rootView = inflate(getContext(),R.layout.boc_fund_persionltrsdetail, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        persiontrsLv = (PinnedSectionListView) rootView.findViewById(R.id.persiontrsLv);
        persiontrsHit = (TextView) rootView.findViewById(R.id.persiontrsHit);

        showListAdapter = new ShowListAdapter(getContext(), -1);
        persiontrsLv.saveAdapter(showListAdapter);
        persiontrsLv.setShadowVisible(false);
        persiontrsLv.setAdapter(showListAdapter);

    }

    @Override
    public void attach(Fragment fragment) {
        super.attach(fragment);
        mBussFragment = (BussFragment) fragment;
    }

    @Override
    public void initData() {
        super.initData();
        setPinnedSectionData();
        showListAdapter.setData(listViewBeanList);
        setListViewHeight();
    }

    @Override
    public void setListener() {
        super.setListener();
        persiontrsLv.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                PersionaltrsdetailFragment persionaltrsdetail = new PersionaltrsdetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(StatementConst.FUND_PERSIONLTRS, mPersionaltrsList.get(position));
                persionaltrsdetail.setArguments(bundle);
                mBussFragment.start(persionaltrsdetail);
            }
        });
    }

    /**
     * 上页传递面交易流水页面数据显示
     * @param persionaltrsList
     */
    public void setData(List<PersionaltrsModel> persionaltrsList) {
        mPersionaltrsList = persionaltrsList;
    }

    /**
     * 设置时间分层列表数据转换
     *
     */
    private void setPinnedSectionData() {
        if (mPersionaltrsList == null || mPersionaltrsList.size() < 0) {
            persiontrsHit.setVisibility(View.VISIBLE);
            persiontrsHit.setText(mContext.getString(R.string.boc_statement_hit));
        } else {
            listViewBeanList = new ArrayList<ShowListBean>();
            for (int i = 0; i< mPersionaltrsList.size(); i++ ) {
                LocalDate localDate = LocalDate.parse(mPersionaltrsList.get(i).getPaymentDate(),
                        DateFormatters.dateFormatter1);
                String formatTime = "";
                String tempTime = "";
                if (localDate != null) {
                    formatTime= localDate.format(DateFormatters.monthFormatter1);
                }
                if (i > 0) {
                    tempTime = LocalDate.parse(mPersionaltrsList.get(i - 1).getPaymentDate(),
                            DateFormatters.dateFormatter1).format(DateFormatters.monthFormatter1);
                }

               if (tempTime.equals(formatTime)) {
                   ShowListBean item = new ShowListBean();
                   item.type = ShowListBean.CHILD;
                   item.setTitleID(ShowListConst.TITLE_WEALTH);
                   item.setChangeColor(true);
                   item.setTime(localDate);

                   //基金名称+ 基金编码
                   item.setContentLeftAbove(mPersionaltrsList.get(i).getFundName() + " " +
                           mPersionaltrsList.get(i).getFundCode());
                   //交易类型
                   item.setContentLeftBelow(mPersionaltrsList.get(i).getFundTranType());
                   //交易状态
                   item.setContentRightAbove(mPersionaltrsList.get(i).getTrsStatus());
                   //交易份额
                   item.setContentRightBelow(MoneyUtils.transMoneyFormat(mPersionaltrsList.get(i).getFundConfirmCount(),
                           mPersionaltrsList.get(i).getCurrency()));
                   listViewBeanList.add(item);

               } else {
                   for (int j = 0; j< 2; j++) {
                       ShowListBean item = new ShowListBean();
                       if (j == 0) {
                           item.type = ShowListBean.GROUP;
                           item.setGroupName(formatTime);
                           item.setTime(localDate);
                       } else {
                           item.type = ShowListBean.CHILD;
                           item.setTitleID(ShowListConst.TITLE_WEALTH);
                           item.setTime(localDate);
                           item.setChangeColor(true);
                           //交易基金名称+ 基金编码
                           item.setContentLeftAbove(mPersionaltrsList.get(i).getFundName() + " " +
                                   mPersionaltrsList.get(i).getFundCode());
                           //交易类型
                           item.setContentLeftBelow(mPersionaltrsList.get(i).getFundTranType());
                           //交易状态
                           item.setContentRightAbove(mPersionaltrsList.get(i).getTrsStatus());
                           //交易份额
                           item.setContentRightBelow(MoneyUtils.transMoneyFormat(mPersionaltrsList.get(i).getFundConfirmCount(),
                                   mPersionaltrsList.get(i).getCurrency()));
                       }
                       listViewBeanList.add(item);
                   }
               }
            }
        }
    }

    private void setListViewHeight() {
        int listviewHeight = setListViewHeightBasedOnChildren1(persiontrsLv);
        StatementFragment statement = new StatementFragment();
        ViewGroup.LayoutParams params = statement.vpContent.getLayoutParams();
        params.height = listviewHeight;
        statement.vpContent.setLayoutParams(params);
    }

    /**
     * 获取Listview的高度，然后设置ViewPager的高度
     * @param listView
     * @return
     */
    private int setListViewHeightBasedOnChildren1(ListView listView) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
        return params.height;
    }

}
