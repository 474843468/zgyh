package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.AccountListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.emptyview.CommonEmptyView;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.model.BillInstallmentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.presenter.BillInstallmentsMainPresenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Name: liukai
 * Time：2017/1/3 16:43.
 * Created by lk7066 on 2017/1/3.
 * It's used to 账单分期首页面
 */

public class BillInstallmentsMainFragment extends MvpBussFragment<BillInstallmentMainContract.BillInstallmentMainPresenter> implements BillInstallmentMainContract.BillInstallmentMainView {

    private View rootView;
    //首页卡列表list
    private ListView billList;
    //无结果页面组件，没有103、104和107的信用卡
    private CommonEmptyView billNoCard;
    //103、104、107信用卡的list集合
    private List<AccountBean> billCrcdList;

    /**
     * 账户列表的adapter
     */
    protected AccountListAdapter accountListAdapter;

    //仅仅是两个布尔值，有点多余
    private boolean arrowVisible = false;
    private boolean medicalEcashVisible = false;

    //点击的位置
    private int clickPosition;

    private BillInstallmentMainContract.BillInstallmentMainPresenter mBillInstallmentPresenter;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_consume_main , null);
        return rootView;
    }

    @Override
    public void initView() {
        billList = (ListView) rootView.findViewById(R.id.lv_consume);
        billNoCard = (CommonEmptyView) rootView.findViewById(R.id.consume_no_data);
    }

    @Override
    public void initData() {
        mBillInstallmentPresenter = new BillInstallmentsMainPresenter(this);
        billCrcdList = ApplicationContext.getInstance().getChinaBankAccountList(getCrcdType());

        //如果缓存中的筛选后列表为空，说明无结果，负责加载界面
        if(0 == billCrcdList.size()){
            noDataView();
        } else {
            loadView(billCrcdList);
        }

    }

    /**
     * 无结果页面，初始化界面
     * */
    public void noDataView(){

        billList.setVisibility(View.GONE);
        billNoCard.setVisibility(View.VISIBLE);

        //设置需要显示的文字和图片
        billNoCard.setEmptyTips(getResources().getString(R.string.boc_crcd_attcard_main_nodata_info1), R.drawable.boc_cash_installment_no_account);

    }

    /**
     * 需要查询的信用卡类型
     * */
    public ArrayList<String> getCrcdType(){
        ArrayList<String> accoutType = new ArrayList<>();
        accoutType.add(ApplicationConst.ACC_TYPE_ZHONGYIN);//103
        accoutType.add(ApplicationConst.ACC_TYPE_GRE);//104
        accoutType.add(ApplicationConst.ACC_TYPE_SINGLEWAIBI);//107
        return accoutType;
    }

    /**
     * 加载页面
     * */
    public void loadView(final List<AccountBean> list){
        accountListAdapter = new AccountListAdapter(mContext);
        accountListAdapter.setAmountInfoVisible(false);
        accountListAdapter.setMedicalEcashVisible(medicalEcashVisible);
        accountListAdapter.setArrowVisible(arrowVisible);
        billList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickPosition = position;

                if(list.get(position).getAccountType().equals("107")){
                    showErrorDialog("此卡不支持账单分期");
                } else {
                    getPresenter().queryBillInput(list.get(position).getAccountId());
                }

            }

        });

        billList.setAdapter(accountListAdapter);
        accountListAdapter.setDatas(buildAccountModel(list));
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
    protected String getTitleValue() {
        return "选择账单分期信用卡";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void onDestroy() {
        mBillInstallmentPresenter.unsubscribe();
        super.onDestroy();
    }


    @Override
    public void queryBillInputSuccess(BigDecimal upLimit, BigDecimal lowLimit) {
        BillInstallmentModel billModel = new BillInstallmentModel();
        billModel.setUpInstmtAmount(upLimit);
        billModel.setLowInstmtAmount(lowLimit);
        start(BillInstallmentsFragment.getInstance(billModel, billCrcdList.get(clickPosition)));
    }

    @Override
    public void queryBillInputFailed(BiiResultErrorException e) {

    }

    @Override
    public void setPresenter(BillInstallmentMainContract.BillInstallmentMainPresenter presenter) {

    }

    @Override
    protected BillInstallmentMainContract.BillInstallmentMainPresenter initPresenter() {
        return mBillInstallmentPresenter;
    }
    
}
