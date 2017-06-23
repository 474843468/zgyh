package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.container.BussContainer;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.adapter.StatementAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.model.StatementModel;

import java.util.List;

/**
 * Created by huixiaobo on 2016/11/24.
 * 持仓信息列表
 */
public class StatementRcdView extends BussContainer {
    /**
     * view
     */
    protected View rootView;
    /**
     * 上下文
     */
    protected Context mContext;
    /**
     * 加载的ListView
     */
    protected ListView stateRecordLv;
    protected SpannableString tvNodate;
    protected LinearLayout llNoDate;
    /**
     * 适配器
     */
    private StatementAdapter mStatementAdapter;
    /**
     * 网络求放回数据
     */
    private StatementModel mStatement;
    /**
     * 为空提示
     */
    private TextView stateRecordHit;

    public StatementRcdView(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected View createContentView() {
        rootView = inflate(getContext(), R.layout.boc_statementrcd_fragment, null);
        return rootView;
    }

    @Override
    public void attach(Fragment fragment) {
        super.attach(fragment);
        mBussFragment = (BussFragment) fragment;
    }

    @Override
    public void initView() {
        super.initView();
        stateRecordLv = (ListView) rootView.findViewById(R.id.stateRecordLv);
        tvNodate = (SpannableString) rootView.findViewById(R.id.tv_nodate);
        llNoDate = (LinearLayout) rootView.findViewById(R.id.ll_no_date);
    }

    /**
     * 上页面传值持仓信息
     *
     * @param statement
     */
    public void setDate(StatementModel statement) {
        mStatement = statement;
    }

    @Override
    public void initData() {
        super.initData();
        //上页面传过的值查询时间
        if (mStatement != null && mStatement.getList() != null && mStatement.getList().size() > 0) {
            setStatementData(mStatement.getList());
            setListViewHeight();
        } else {
            llNoDate.setVisibility(VISIBLE);
            tvNodate.setText(getResources().getString(R.string.boc_statement_hit));
        }

    }

    /**
     * 页面显示数据
     *
     * @param statementList
     */
    private void setStatementData(List<StatementModel.ListBean> statementList) {
        if (statementList == null || statementList.size() < 0) {
            return;
        }

        if (mStatementAdapter == null) {
            mStatementAdapter = new StatementAdapter(getContext());
            mStatementAdapter.setBeans(statementList);
        }
        stateRecordLv.setAdapter(mStatementAdapter);
        mStatementAdapter.notifyDataSetChanged();
    }

    /**
     * 处理点击事件
     */
    @Override
    public void setListener() {
        super.setListener();
        stateRecordLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StatementdetailFragment statementdetail = new StatementdetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(StatementConst.FUND_STATEMENT, mStatement.getList().get(position));
                statementdetail.setArguments(bundle);
                mBussFragment.start(statementdetail);
            }
        });
    }

    private void setListViewHeight() {
        int listviewHeight = setListViewHeightBasedOnChildren1(stateRecordLv);
        StatementFragment statement = new StatementFragment();
        ViewGroup.LayoutParams params = statement.vpContent.getLayoutParams();
        params.height = listviewHeight;
        statement.vpContent.setLayoutParams(params);
    }

    /**
     * 获取Listview的高度，然后设置ViewPager的高度
     *
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
