package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.FessBean;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.adapter.FessQryAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.getexchangeoutlay.GetExchangeOutlayParams;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.presenter.FessPresenter;
import com.boc.bocsoft.mobile.framework.utils.PublicConst;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 结购汇列表
 * Created by lxw on 2016/8/17 0017.
 */
public class FessListViewFragment extends MvpBussFragment<FessPresenter> implements FessContract.View {

    private View mRoot;
    //结汇购汇的ListView
    ListView fessListView;

    private FessPresenter presenter;
    private ArrayList<EditInvestModuleListViewFragment.InvestItem>
            selectedFesses = new ArrayList<>();//选中的结构汇，界面显示要过滤

    protected View onCreateView(LayoutInflater inflater) {
        mRoot = inflater.inflate(R.layout.boc_view_simple_listview, null);
        return mRoot;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedFesses = (ArrayList) bundle.get(ApplicationConst.SELECTED_INVEST_FESS);
        }
    }

    @Override
    protected FessPresenter initPresenter() {
        presenter = new FessPresenter(this);
        return presenter;
    }

    @Override
    public void initView() {
        fessListView = mViewFinder.find(R.id.listview);
    }

    @Override
    public void initData() {
        queryFessList();
        super.initData();
    }

    @Override
    public void setListener() {
        fessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FessBean fessBean = mAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(PublicConst.RESULT_DATA, fessBean);
                setFramgentResult(Activity.RESULT_OK, bundle);
                pop();
            }
        });
        super.setListener();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_title_fess);
    }

    private void queryFessList() {
        showLoadingDialog();
        GetExchangeOutlayParams params = new GetExchangeOutlayParams();
        presenter.getFessList(params);
    }

    private FessQryAdapter mAdapter;

    @Override
    public void updateFessView(List<FessBean> resultList) {
        closeProgressDialog();
        if (mAdapter == null) {
            mAdapter = new FessQryAdapter(mContext);
            fessListView.setAdapter(mAdapter);
        }
        //过滤已选数据
        List<FessBean> removeList = new ArrayList<>();
        for (FessBean bean : resultList) {
            for (EditInvestModuleListViewFragment.InvestItem selectBean : selectedFesses) {
                if (bean.getCurCode().equals(selectBean.getSaveId())) {
                    removeList.add(bean);
                }
            }
        }
        for (FessBean removeBean : removeList) {
            resultList.remove(removeBean);
        }
        //只显示限定的币种
        String[] fessArray = ApplicationConst.FESS_BOC_ARRAY;//限定的币种集合
        List<String> fessCurrList = Arrays.asList(fessArray);
        List<FessBean> filterFessList = new ArrayList<>();
        //按照限定的币种集合排序
        for (String currency : fessCurrList) {
            for (FessBean bean : resultList) {
                if (currency.equals(bean.getCurCode())) {
                    filterFessList.add(bean);
                }
            }
        }
        mAdapter.setDatas(filterFessList);
    }

    @Override
    public void updateFail() {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(FessContract.Presenter presenter) {

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
