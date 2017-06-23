package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.GoldBean;
import com.boc.bocsoft.mobile.bocmobile.buss.common.util.InvestUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.adapter.GoldQryAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.getallexchangeratesoutlay.GetAllExchangeRatesOutlayParams;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.presenter.GoldPresenter;
import com.boc.bocsoft.mobile.framework.utils.PublicConst;

import java.util.ArrayList;
import java.util.List;

/**
 * 贵金属列表
 * Created by lxw on 2016/8/17 0017.
 */
public class GoldListViewFragment extends MvpBussFragment<GoldPresenter> implements GoldContract.View {

    private View mRoot;
    //贵金属的ListView
    ListView goldListView;
    //无数据的TextView
    private TextView tvNoData;
    private GoldPresenter presenter;
    private ArrayList<EditInvestModuleListViewFragment.InvestItem> golds;

    protected View onCreateView(LayoutInflater inflater) {
        mRoot = inflater.inflate(R.layout.boc_view_simple_listview, null);
        return mRoot;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        golds = (ArrayList) bundle.get(ApplicationConst.SELECTED_INVEST_GOLD);
    }

    @Override
    public void initView() {
        goldListView = mViewFinder.find(R.id.listview);
    }

    @Override
    protected GoldPresenter initPresenter() {
        presenter = new GoldPresenter(this);
        return presenter;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_title_gold);
    }

    @Override
    public void initData() {
        queryGoldList();
        super.initData();
    }

    @Override
    public void setListener() {
        goldListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoldBean goldBean = mAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(PublicConst.RESULT_DATA, goldBean);
                setFramgentResult(Activity.RESULT_OK, bundle);
                pop();
            }
        });
        super.setListener();
    }

    private void queryGoldList() {
        showLoadingDialog();
        GetAllExchangeRatesOutlayParams params = new GetAllExchangeRatesOutlayParams();
        params.setParitiesType("G");
        params.setOfferType("R");
        params.setIbknum("40142");
        presenter.getGoldList(params);
    }

    private GoldQryAdapter mAdapter;

    @Override
    public void updateGoldView(List<GoldBean> goldQueryOutlay) {
        closeProgressDialog();
        if (mAdapter == null) {
            mAdapter = new GoldQryAdapter(mContext);
            goldListView.setAdapter(mAdapter);
        }
        //过滤已选数据
        List<GoldBean> removeList = new ArrayList<>();
        for (GoldBean bean : goldQueryOutlay) {
            for (EditInvestModuleListViewFragment.InvestItem selectBean : golds) {
                //比较贵金属的币种组合和公共Bean的id
                if ((InvestUtils.getCurrencyPair(bean.getSourceCurrencyCode(), bean.getTargetCurrencyCode())).
                        equals(selectBean.getSaveId())) {
                    removeList.add(bean);
                }
            }
        }
        for (GoldBean removeBean : removeList) {
            goldQueryOutlay.remove(removeBean);
        }
        mAdapter.setDatas(goldQueryOutlay);
    }

    @Override
    public void onFail() {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(GoldContract.Presenter presenter) {

    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }
}
