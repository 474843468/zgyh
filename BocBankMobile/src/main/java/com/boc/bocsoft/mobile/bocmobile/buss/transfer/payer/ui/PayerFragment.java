package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.QueryPayerListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui.ChoosePayerAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.adapter.PayerListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.model.PayerBean;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.model.PayerListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.presenter.PayerListPresenter;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 2016/7/21.
 * 付款人列表
 */
public class PayerFragment extends BussFragment implements PayerListContract.QueryList {

    //根布局
    private View rootView;
    //
    private PayerListPresenter payerListPresenter;
    //
    private PayerListModel listModel;
    //ListView所需要的List
    private List<PayerBean> payerBeanList = new ArrayList<PayerBean>();
    ;
    //加载数据
    private PayerListAdapter mAdapter;
    //详情的ListView
    protected ListView transferPayerList;
    //列表详情
    private PayerDetailFragment detailFragment;
    //搜索
    private EditText searchName;
    //查询失败文字提示
    private TextView tv_queryfaile;
    private MyReceiver myReceiver;
    private boolean isChoosePayerAccount = false;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_transfer_payer, null);
        return rootView;
    }


    @Override
    public void initView() {
        tv_queryfaile = (TextView) rootView.findViewById(R.id.tv_queryfaile);
        searchName = (EditText) rootView.findViewById(R.id.et_search_payer);
        transferPayerList = (ListView) rootView.findViewById(R.id.transfer_payer_list);
    }

    @Override
    public void initData() {
        isChoosePayerAccount = getArguments().getBoolean("isChoosePayerAccount"); // 是否是选择付款帐号

        payerListPresenter = new PayerListPresenter(this);
        listModel = new PayerListModel();
        //获取付款人列表
        showLoadingDialog();
        payerListPresenter.queryPayerList();

        //将所有数据处理完后为ListView设置数据
        mAdapter = new PayerListAdapter(payerBeanList, mContext);
        transferPayerList.setAdapter(mAdapter);

    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
                tv_queryfaile.setVisibility(payerBeanList.size() == 0 ? View.VISIBLE : View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(myReceiver);
    }

    @Override
    public void setListener() {
        myReceiver = new MyReceiver();
        mActivity.registerReceiver(myReceiver, new IntentFilter(PayerDetailFragment.ACTION_NOTIFY));

        searchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                mAdapter.setFilterWord(str);
            }
        });

    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_payer_manage);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    protected boolean isDisplayRightIcon() {
        return false;
    }

    // 查询付款人列表成功
    @Override
    public void queryPayerListSuccess(final PayerListModel queryPayerListModel) {
        closeProgressDialog();

        if (queryPayerListModel.getResult() != null && queryPayerListModel.getResult().size() != 0) {
            List<PayerListModel.ResultBean> resultBean = new ArrayList<PayerListModel.ResultBean>();
            resultBean.addAll(queryPayerListModel.getResult());

            if (null != listModel.getResult()) {
                listModel.getResult().clear();
            }

            listModel.setResult(resultBean);
            copyPayerResult2PayerBean(listModel);
        } else {
            payerBeanList.clear();
            haveDataSelectText();
        }

        mAdapter.notifyDataSetChanged();
        tv_queryfaile.setVisibility(payerBeanList.size() == 0 ? View.VISIBLE : View.GONE);


        // listView的item点击事件
        transferPayerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isChoosePayerAccount) {
                    ArrayList<QueryPayerListViewModel.ResultBean> mSelectedPayerList = new ArrayList<QueryPayerListViewModel.ResultBean>();
                    PayerBean payerBean = mAdapter.getItem(position);
                    QueryPayerListViewModel.ResultBean resultBean = new QueryPayerListViewModel.ResultBean();
                    BeanConvertor.toBean(payerBean, resultBean);

                    mSelectedPayerList.add(resultBean);

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(ChoosePayerAccountFragment.SELECTED_PAYER_ACCOUNT, mSelectedPayerList);
                    setFramgentResult(ChoosePayerAccountFragment.RESULT_CODE_SELECTED_PAYER_ACCOUNT, bundle);
                    pop();
                } else {
                    Bundle bundle = new Bundle();
                    detailFragment = new PayerDetailFragment();
                    bundle.putParcelable("DetailsInfo", mAdapter.getItem(position));
                    detailFragment.setArguments(bundle);
                    startForResult(detailFragment, 101);
                }

                hideSoftInput(); // 隐藏软件盘
            }
        });
    }

    private void haveDataSelectText() {
        tv_queryfaile.setVisibility(View.VISIBLE);
        tv_queryfaile.setText("暂无常用付款人");
    }

    /**
     * 数据返回
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        Result数据
     */
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {


        if (101 == requestCode && (103 == resultCode || 104 == resultCode)) {
            //回调刷新数据
            showLoadingDialog();
            payerListPresenter.queryPayerList();
        }

    }

    @Override
    public void queryPayerListFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    @Override
    public void setPresenter(PayerListContract.Presenter presenter) {

    }


    /**
     * 将接口返回数据封装成ListView所需要的model
     *
     * @param listModel
     */
    private void copyPayerResult2PayerBean(PayerListModel listModel) {

        if (null != payerBeanList) {
            payerBeanList.clear();
        } else {
            payerBeanList = new ArrayList<PayerBean>();
        }

        for (int i = 0; i < listModel.getResult().size(); i++) {
            PayerBean payerBean = new PayerBean();
            PayerListModel.ResultBean resultBean = listModel.getResult().get(i);
            payerBean.setPayerId(Integer.parseInt(resultBean.getPayerId()));
            payerBean.setPayerName(resultBean.getPayerName());
            payerBean.setPinyin("");
            payerBean.setIdentifyType(resultBean.getIdentifyType());
            payerBean.setPayerMobile(NumberUtils.formatMobileNumber(resultBean.getPayerMobile()));
            payerBean.setPayerCustomerId(Integer.parseInt(resultBean.getPayerCustomerId()));
            payerBeanList.add(payerBean);
        }

        if (listModel.getResult().size() == 0) {
            tv_queryfaile.setText("暂无常用付款人");
            tv_queryfaile.setVisibility(View.VISIBLE);
        }
    }

}

