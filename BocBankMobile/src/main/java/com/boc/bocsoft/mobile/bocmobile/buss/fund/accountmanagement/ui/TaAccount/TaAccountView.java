package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.TaAccount;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.container.MvpContainer;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.presenter.TaAccountPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.AccountManagementFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.TaAccountDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.TaAccountRegisterFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.adapter.TaAccListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.InvstBindingInfoViewModel;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * 账户管理首页-基金TA账户View
 * Created by lyf7084 on 2016/12/13.
 */
public class TaAccountView extends MvpContainer<TaAccountPresenter>
        implements TaAccountContract.View, BaseView<TaAccountPresenter> {

    private View rootView;
    private ListView taAccList;    // TA账户列表
    private TaAccListAdapter mTaAccListAdapter;
    private Context mContext;
    public TaAccountModel taAccountModel;

    private LinearLayout llyTaAccount;    // 用户有TA账户Layout
    private LinearLayout llyNoAccount;    // 用户无TA账户Layout
    private View footView;    // 脚布局，登记+号
    private TextView tvRegist;    // 可点击“登记”按钮
    private Class<? extends BussFragment> fromClass;

    private TaAccountDetailFragment taAccountDetailFragment;

    private InvstBindingInfoViewModel mBindingInfoModel = null; //基金账户信息

//    private boolean isTaView = true;                                          // 是否是初次实现view

    public TaAccountView(Context context) {
        this(context, null, 0);
    }

    public TaAccountView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaAccountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    /**
     * 初始化布局
     */
    @Override
    protected View createContentView() {
        rootView = inflate(getContext(), R.layout.boc_fragment_fund_ta_account, null);
        LogUtil.d("Liyifan------------> OnCreate TA Account View");
        return rootView;
    }

    private void initView(Context mContext) {
        this.mContext = mContext;
        taAccList = (ListView) rootView.findViewById(R.id.lv_ta_acc);
        tvRegist = (TextView) rootView.findViewById(R.id.tvRegist);
        llyTaAccount = (LinearLayout) rootView.findViewById(R.id.llyTaAccount);
        llyNoAccount = (LinearLayout) rootView.findViewById(R.id.llyNoAccount);
        footView = View.inflate(mContext, R.layout.boc_fragment_fund_ta_list_footview, null);
        llyTaAccount.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        taAccountModel = new TaAccountModel();
        mTaAccListAdapter = new TaAccListAdapter(mContext);
        taAccList.setAdapter(mTaAccListAdapter);
        taAccList.addFooterView(footView);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isTaView && isVisibleToUser) {
//            showLoadingDialog();
//            getPresenter().queryTaAccList();                                    // 上送参数为空
//        }
        if (((AccountManagementFragment) mBussFragment).getTAList() == null && isVisibleToUser) {
            showLoadingDialog();
            getPresenter().queryTaAccList();                                    // 上送参数为空
        } else {
            showTAListView();
        }
    }

    public void showTAListView() {
        if (mTaAccListAdapter == null) {
            mTaAccListAdapter = new TaAccListAdapter(mContext);
            taAccList.setAdapter(mTaAccListAdapter);
        }
        if (((AccountManagementFragment) mBussFragment).getTAList().size() == 0) {
            //列表为空的时候显示空页面
            llyTaAccount.setVisibility(View.GONE);
            llyNoAccount.setVisibility(View.VISIBLE);
        } else {
            //列表不为空的时候显示列表数据
            llyTaAccount.setVisibility(View.VISIBLE);
            llyNoAccount.setVisibility(View.GONE);
            TaAccountModel result = new TaAccountModel();
            result.setTaAccountList(((AccountManagementFragment) mBussFragment).getTAList());
            mTaAccListAdapter.setTaAccountModel(result);
            mTaAccListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void setListener() {
        // 您暂无关联的TA账户，请登记：登记 按钮的点击事件
        tvRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaAccountRegisterFragment taAccountRegisterFragment = new TaAccountRegisterFragment();
                mBussFragment.start(taAccountRegisterFragment);
            }
        });

        // 一条TA账户的点击事件
        taAccList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                // 将封装的QueryTaAccountDetailResModel的Bean中的一条传递
                bundle.putSerializable("TaAccountSelectedItem", taAccountModel.getTaAccountList().get(position));
                //  new 一个详情页传入数据为：resModel.List中的一个Item
                TaAccountDetailFragment fragment = new TaAccountDetailFragment();
                fragment.setArguments(bundle);
                mBussFragment.start(fragment);
            }
        });

//        脚布局的登记加号 点击事件
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaAccountRegisterFragment taAccountRegisterFragment = new TaAccountRegisterFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("binding_info", mBindingInfoModel);
                taAccountRegisterFragment.setArguments(bundle);

                mBussFragment.start(taAccountRegisterFragment);
            }
        });
    }

    @Override
    protected TaAccountPresenter initPresenter() {
        return new TaAccountPresenter(this);
    }

    @Override
    public void queryTaAccListSuccess(TaAccountModel result) {
        //  判断取回的list的size判断显示llyTaAccount/llyTaAccount
        closeProgressDialog();
        taAccountModel = result;
        ((AccountManagementFragment) mBussFragment).setTAList(result.getTaAccountList());
        showTAListView();
    }

    @Override
    public void queryTaAccListFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();

        llyTaAccount.setVisibility(View.GONE);
        llyNoAccount.setVisibility(View.VISIBLE);
    }

    public void setBindingInfo(InvstBindingInfoViewModel mBindingInfoModel) {
        this.mBindingInfoModel = mBindingInfoModel;
    }
}
