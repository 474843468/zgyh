package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.adapter.SelectedPayerAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.QueryPayerListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter.QueryPayerListPresenter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Fragment：从付款账户列表中选择付款账户
 * Created by zhx on 2016/7/11
 */
public class ChoosePayerAccountFragment extends BussFragment implements QueryPayerListContract.View {
    private View mRootView;

    private QueryPayerListPresenter mPresenter;

    ArrayList<QueryPayerListViewModel.ResultBean> testBeanList = new ArrayList<QueryPayerListViewModel.ResultBean>();
    ArrayList<QueryPayerListViewModel.ResultBean> selectedTestBeanList = new ArrayList<QueryPayerListViewModel.ResultBean>();
    private EditText et_search_content;
    private TextView tv_no_data;
    private ListView lv_payer_account_list;
    private RecyclerView gv_selected_payer;
    private PayerAdapter mAdapter;

    private SelectedPayerAdapter mSelectedPayerAdapter;
    private Button btnOK;

    private boolean isListReversed; // 付款人列表集合是否被反转
    /**
     * 页面跳转数据传递
     */
    public static final String SELECTED_PAYER_ACCOUNT = "selected_payer_account";
    public static final String PAYER_ACCOUNT = "payer_account";
    public static final int REQUEST_CODE_ADD_PAYER = 103;
    public static final int RESULT_CODE_SELECTED_PAYER_ACCOUNT = 101;
    private ImageView vrightIconIv; // 标题栏右侧的按钮
    private LinearLayoutManager layoutManager;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_choose_payer_account, null);
        return mRootView;
    }

    @Override
    public void initView() {
        tv_no_data = (TextView) mRootView.findViewById(R.id.tv_no_data);
        et_search_content = (EditText) mRootView.findViewById(R.id.et_search_content);
        lv_payer_account_list = (ListView) mRootView.findViewById(R.id.lv_payer_account_list);
        gv_selected_payer = (RecyclerView) mRootView.findViewById(R.id.gv_selected_payer);
        btnOK = (Button) mRootView.findViewById(R.id.btn_ok);

    }

    @Override
    public void initData() {
        ArrayList<QueryPayerListViewModel.ResultBean> testBeanList1 = getArguments().getParcelableArrayList(PAYER_ACCOUNT);
        ArrayList<QueryPayerListViewModel.ResultBean> selectTestBeanList = getArguments().getParcelableArrayList(SELECTED_PAYER_ACCOUNT);
        if (testBeanList1 != null) {
            if (selectTestBeanList.size() > 3 && getArguments().getBoolean("isNeedReversed")) {
                Collections.reverse(selectTestBeanList);
                isListReversed = true;
            }
            testBeanList = testBeanList1;
            selectedTestBeanList = selectTestBeanList;

        } else {
            mPresenter = new QueryPayerListPresenter(this);
            QueryPayerListViewModel queryPayerListViewModel = new QueryPayerListViewModel();
            mPresenter.queryPayerList(queryPayerListViewModel);
        }

        mAdapter = new PayerAdapter(mContext, testBeanList);
        lv_payer_account_list.setAdapter(mAdapter);

        // 设置“无数据”提示的显示
        tv_no_data.setVisibility(testBeanList.size() == 0 ? View.VISIBLE : View.GONE);

        setOrNotifySelectedList();
    }

    /**
     * 设置适配器或者刷新“选中的列表”（底部的）
     */
    private void setOrNotifySelectedList() {
        // 设置确定按钮显示的条目数
        if (selectedTestBeanList != null && selectedTestBeanList.size() != 0) {
            btnOK.setText("确认 (" + selectedTestBeanList.size() + ")");
        } else {
            btnOK.setText("确认");
        }

        if (mSelectedPayerAdapter == null) { // 设置适配器
            layoutManager = new LinearLayoutManager(mContext,
                    LinearLayoutManager.HORIZONTAL, false);
            gv_selected_payer.setLayoutManager(this.layoutManager);
            mSelectedPayerAdapter = new SelectedPayerAdapter(mContext, selectedTestBeanList);
            gv_selected_payer.setAdapter(mSelectedPayerAdapter);
            if (selectedTestBeanList != null && selectedTestBeanList.size() > 3) {
                isListReversed = true;
                layoutManager.setReverseLayout(true);
            } else {
                isListReversed = false;
                layoutManager.setReverseLayout(false);
            }
        } else { // 实现列表的向右滑动效果
            if (selectedTestBeanList != null && selectedTestBeanList.size() > 3) {
                if (!isListReversed) {
                    Collections.reverse(selectedTestBeanList);
                }
                isListReversed = true;
                layoutManager.setReverseLayout(true);
            } else {
                if (isListReversed) {
                    Collections.reverse(selectedTestBeanList);
                }
                isListReversed = false;
                layoutManager.setReverseLayout(false);
            }
            mSelectedPayerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setListener() {


        lv_payer_account_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QueryPayerListViewModel.ResultBean testBean = mAdapter.updateItem(position, view);

                if (testBean.isChecked()) {
                    if (isListReversed) {
                        selectedTestBeanList.add(0, testBean);
                    } else {
                        selectedTestBeanList.add(testBean);
                    }
                } else {
                    selectedTestBeanList.remove(testBean);
                }

                setOrNotifySelectedList();
            }
        });

        et_search_content.addTextChangedListener(new TextWatcher() {
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

        // 点击x的时候，删除选中的条目
        mSelectedPayerAdapter.setOnItemClickListener(new SelectedPayerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int positon) {
                QueryPayerListViewModel.ResultBean resultBean = selectedTestBeanList.get(positon);
                resultBean.setChecked(false);
                selectedTestBeanList.remove(resultBean);
                mAdapter.notifyDataSetChanged();
                mSelectedPayerAdapter.notifyDataSetChanged();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                if (selectedTestBeanList.size() > 3) {
                    Collections.reverse(selectedTestBeanList);
                }

                bundle.putBoolean("isNeedReversed", true);
                bundle.putParcelableArrayList(PAYER_ACCOUNT, testBeanList);
                bundle.putParcelableArrayList(SELECTED_PAYER_ACCOUNT, selectedTestBeanList);

                setFramgentResult(RESULT_CODE_SELECTED_PAYER_ACCOUNT, bundle);
                pop();
            }
        });
    }

    @Override
    protected String getTitleValue() {
        return "付款人";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode == AddPayerFragment.RESULT_CODE_ADD_PAYER_SUCCESS) {
            QueryPayerListViewModel.ResultBean testBean = (QueryPayerListViewModel.ResultBean) data.getSerializable("payer");
            if (testBean != null) {
                testBeanList.add(testBean);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    protected View getTitleBarView() {
        View view = super.getTitleBarView();

        vrightIconIv = (ImageView) view.findViewById(R.id.rightIconIv);
        vrightIconIv.setImageResource(R.drawable.btn_left_top_add);
        int padding = (int) mActivity.getResources().getDimension(R.dimen.boc_space_between_30px);
        vrightIconIv.setPadding(padding, padding, padding, padding);

        vrightIconIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开添加付款人页面
                AddPayerFragment toFragment = new AddPayerFragment();
                startForResult(toFragment, REQUEST_CODE_ADD_PAYER);
            }
        });

        return view;
    }

    @Override
    public void queryPayerListSuccess(QueryPayerListViewModel queryPayerListViewModel) {
        Log.e("ljljlj", "queryPayerListViewModel = " + queryPayerListViewModel);
        Log.e("ljljlj", "queryPayerListViewModel size大小为 = " + queryPayerListViewModel.getResult().size());

        testBeanList.addAll(queryPayerListViewModel.getResult());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void queryPayerListFail(BiiResultErrorException biiResultErrorException) {
    }

    @Override
    public void setPresenter(QueryPayerListContract.Presenter presenter) {
    }
}