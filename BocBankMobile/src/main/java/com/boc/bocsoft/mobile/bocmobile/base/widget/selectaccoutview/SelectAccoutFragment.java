package com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.AccountListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择账户
 * Created by wangf on 2016/6/13.
 */
public class SelectAccoutFragment<P extends BasePresenter> extends MvpBussFragment<P> implements AdapterView.OnItemClickListener {

    private View mRootView;

    protected ListView mListView;
    private TextView tvCenterTop;
    private TextView tvAccountTip;

    /**
     * 账户列表的adapter
     */
    protected AccountListAdapter accountListAdapter;
    private ItemListener itemListener;

    private boolean isRequestNet = false;

    /**
     * 页面跳转数据传递
     */
    public static final String ACCOUNT_TYPE_LIST = "AccountTypeList";
    public static final String ACCOUNT_LIST = "AccountList";
    public static final String ACCOUNT_SELECT = "AccountBean";

    public static final int REQUEST_CODE_SELECT_ACCOUNT = 1;
    public static final int RESULT_CODE_SELECT_ACCOUNT = 100;
    public static final int RESULT_CODE_SELECT_ACCOUNT_NULL = 400;

    private boolean arrowVisible = false;
    private boolean medicalEcashVisible = false;

    private String strFragmentTitle;
    private String strAccountTip;


    public static SelectAccoutFragment newInstanceWithData(ArrayList<AccountBean> accountBeanList) {
        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList(SelectAccoutFragment.ACCOUNT_LIST, accountBeanList);
        SelectAccoutFragment fragment = new SelectAccoutFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SelectAccoutFragment newInstance(ArrayList<String> typelist) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(SelectAccoutFragment.ACCOUNT_TYPE_LIST, typelist);
        SelectAccoutFragment fragment = new SelectAccoutFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_transdetail_selectaccount, null);
        return mRootView;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        mListView = (ListView) mRootView.findViewById(R.id.lv_transdetail_selectaccount);
        tvAccountTip = (TextView) mRootView.findViewById(R.id.tv_amount_choice);
        tvCenterTop = (TextView) mRootView.findViewById(R.id.tv_center_top);

        accountListAdapter = new AccountListAdapter(mContext);
        accountListAdapter.setAmountInfoVisible(false);
        accountListAdapter.setMedicalEcashVisible(medicalEcashVisible);
        accountListAdapter.setArrowVisible(arrowVisible);

        mListView.setOnItemClickListener(this);
        mListView.setAdapter(accountListAdapter);
    }


//    public ListView getmListView() {
//        return mListView;
//    }


    @Override
    protected String getTitleValue() {
        if (StringUtils.isEmpty(strFragmentTitle)){
            return getResources().getString(R.string.boc_account_select_account);
        }else{
            return strFragmentTitle;
        }
    }

    @Override
    public boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void initData() {

        if (!StringUtils.isEmpty(strAccountTip)){
            tvAccountTip.setText(strAccountTip);
            tvAccountTip.setVisibility(View.VISIBLE);
        }

        List<AccountBean> accountBeanList=null;
        if ((accountBeanList=getArguments().getParcelableArrayList(ACCOUNT_LIST))==null){
            ArrayList<String> stringList = getArguments().getStringArrayList(ACCOUNT_TYPE_LIST);
            accountBeanList= ApplicationContext.getInstance().getChinaBankAccountList(stringList);
        }
        accountListAdapter.setDatas(buildAccountModel(accountBeanList));
        accountListAdapter.loadAmountInfo();
    }


    /**
     * 将AccountBean的List转换成AccountListItemViewModel的List
     *
     * @param accountBeanList
     * @return
     */
    private List<AccountListItemViewModel> buildAccountModel(List<AccountBean> accountBeanList) {
        List<AccountListItemViewModel> viewModelList = new ArrayList<AccountListItemViewModel>();
        for (int i = 0; i < accountBeanList.size(); i++) {
            AccountListItemViewModel listItemViewModel = new AccountListItemViewModel();
            listItemViewModel.setAccountBean(accountBeanList.get(i));

            viewModelList.add(listItemViewModel);
        }
        return viewModelList;
    }


    @Override
    public void setListener() {
        super.setListener();
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ACCOUNT_SELECT, accountListAdapter.getItem(position).getAccountBean());
        if(isRequestNet){
            if(itemListener != null){
                itemListener.onItemClick(bundle);
            }
        } else {
            setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundle);
            pop();
        }
    }


    /**
     * 设置右侧arrow是否显示
     * @param arrowVisible
     */
    public void setArrowVisible(boolean arrowVisible) {
        this.arrowVisible = arrowVisible;
    }


    /**
     * 设置医保和电子现金标志图片是否显示
     * @param visible
     */
    public void setMedicalEcashVisible(boolean visible) {
        this.medicalEcashVisible = visible;
    }


    /**
     * 设置页面无数据的提示信息
     * @param strTip
     */
    public void showNoDataView(String strTip){
        if (!StringUtils.isEmpty(strTip)){
            tvCenterTop.setText(strTip);
            tvCenterTop.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
    }


    /**
     * 设置页面的标题
     * @param strFragmentTitle
     */
    public void setFragmentTitle(String strFragmentTitle){
        this.strFragmentTitle = strFragmentTitle;
    }


    /**
     * 设置页面上方的提示信息
     * @param strTip
     */
    public void setAccountTip(String strTip){
        this.strAccountTip = strTip;
    }


    @Override
    protected P initPresenter() {
        return null;
    }

    public interface ItemListener {
        void onItemClick(Bundle bundle);
    }

    public void setOnItemListener(ItemListener itemListener){
        this.itemListener = itemListener;
    }

    public void isRequestNet(boolean isRequestNet){
        this.isRequestNet = isRequestNet;
    }


    //    @Override
//    protected void titleLeftIconClick() {
//        setFramgentResult(RESULT_CODE_SELECT_ACCOUNT_NULL, null);
//        pop();
//    }


    @Override
    public boolean onBack() {
        setFramgentResult(RESULT_CODE_SELECT_ACCOUNT_NULL, null);
        pop();
        return false;
    }

}
