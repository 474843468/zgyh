package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.model.BalanceEnquiryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.model.XpadPsnVFGGetBindAccount;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.presenter.BalanceEnquiryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.ui.balanceenquirylistview.BalanceEnquiryAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.ui.balanceenquirylistview.BalanceEnquiryBean;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.ui.balanceenquirylistview.BalanceEnquiryListView;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui.MarginManagementFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.TransQueryUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 **双向宝-余额查询
 * Created by wjk7118 on 2016/12/14.
 */
public class BalanceEnquiryFragment extends MvpBussFragment<BalanceEnquiryContract.Presenter> implements BalanceEnquiryContract.View {

    private View rootView;
    private BalanceEnquiryPresenter mBalanceEnquiryPresenter;
    private TransQueryUtils mTransQueryUtils;
    private BalanceEnquiryBean vBean;

    private BalanceEnquiryListView mBalanceEnquiryListView=null;//listview

    private LinearLayout normallCurrentShow;//人民币以外的当前额度线性布局
    private LinearLayout ll_renminbiCurrent;//人民币当前额度线性布局
    private LinearLayout normallShow;//人民币以外的可用额度的线性布局
    private LinearLayout ll_renminbi;//人民币可用额度线性布局
    private LinearLayout ll_no_data_query;
    private TextView tvNodata = null; //无数据提示
    private BalanceEnquiryModel mBalanceEnquiryModel=null;//页面显示数据
    private Button button;
    private List<BalanceEnquiryBean> mBalanceEnquiryBean;//listview详情
    private BalanceEnquiryAdapter mAdapter;//listview适配器
    private boolean mChoose=false;
    private String mCode=null;
    //当前加载页码
    private int pageCurrentIndex = 0;
    //每页大小
    private final static int pageSize = ApplicationConst.PAGE_SIZE;


    private String accountId;
    private String accountNumber;


    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_balance_enquiry, null);
        return rootView;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        mBalanceEnquiryListView = (BalanceEnquiryListView) rootView.findViewById(R.id.lv_balance_query);
        normallCurrentShow=(LinearLayout) rootView.findViewById(R.id.normallCurrentShow);
        ll_renminbiCurrent=(LinearLayout) rootView.findViewById(R.id.ll_renminbiCurrent);
        normallShow=(LinearLayout) rootView.findViewById(R.id.normallShow);
        ll_renminbi=(LinearLayout) rootView.findViewById(R.id.ll_renminbi);

        ll_no_data_query = (LinearLayout) rootView.findViewById(R.id.no_data_query);
        tvNodata = (TextView)rootView.findViewById(R.id.no_data);
        button=(Button) rootView.findViewById(R.id.btn_back);
    }

    @Override
    public void initData() {
        mBalanceEnquiryBean = new ArrayList<BalanceEnquiryBean>();
        mAdapter = new BalanceEnquiryAdapter(mContext);
        mBalanceEnquiryListView.setAdapter(mAdapter);
        mTransQueryUtils = new TransQueryUtils();
        mBalanceEnquiryPresenter=new BalanceEnquiryPresenter(this);
        mBalanceEnquiryModel=new BalanceEnquiryModel();

        accountNumber = getArguments().getString("accountNumber");

        updateTitle();
        pageCurrentIndex = 0;

        XpadPsnVFGGetBindAccount xpadPsnVFGGetBindAccount=new XpadPsnVFGGetBindAccount();
        mBalanceEnquiryPresenter.queryPsnVFGGetBindAccount(xpadPsnVFGGetBindAccount);
    }


    //查询交易账户成功
    public void queryPsnVFGGetBindAccountSuccess(PsnVFGGetBindAccountResult psnVFGGetBindAccountResult){
        accountId=psnVFGGetBindAccountResult.getAccountId();
        queryBalanceEnquiry();
    }

    //查询交易账户失败
    public void queryPsnVFGGetBindAccountFail(BiiResultErrorException biiResultErrorException){
        closeProgressDialog();
    }


    public void queryBalanceEnquiry(){
        showLoadingDialog();

        BalanceEnquiryModel viewModel=new BalanceEnquiryModel();
        viewModel.setAccountId(accountId);
        viewModel.setPageSize(String.valueOf(pageSize));
        viewModel.setCurrentIndex(String.valueOf(pageCurrentIndex));
        mBalanceEnquiryPresenter.queryBalanceEnquiryList(viewModel);
    }

    @Override
    public void queryBalanceEnquiryListSuccess(BalanceEnquiryModel balanceEnquiryModel) {
        closeProgressDialog();
        button.setVisibility(View.VISIBLE);
        this.mBalanceEnquiryModel=balanceEnquiryModel;
        List<BalanceEnquiryBean> listBean = new LinkedList<>();
        List<BalanceEnquiryModel.AccountDetaiListBean> viewModelList=mBalanceEnquiryModel.getAccountDetaiList();

        for (int i = 0; i < viewModelList.size(); i++) {
            BalanceEnquiryBean bean = new BalanceEnquiryBean();
            this.vBean=bean;
            vBean.setNumber(viewModelList.size());
            BalanceEnquiryModel.AccountDetaiListBean modelBean = viewModelList.get(i);

            mCode = modelBean.getCurrencyCode();
            mChoose =  mCode.equals(ApplicationConst.CURRENCY_USD) || mCode.equals(ApplicationConst.CURRENCY_EUR) ||
                    mCode.equals(ApplicationConst.CURRENCY_HKD) || mCode.equals(ApplicationConst.CURRENCY_JPY) ||
                    mCode.equals(ApplicationConst.CURRENCY_AUD);


            if (mChoose) {
                    BalanceEnquiryModel.AccountDetaiListBean modelBeanB = viewModelList.get(i+1);
                    if (modelBean.getCurrencyCode().equals(modelBeanB.getCurrencyCode())&&(modelBeanB!=null)) {
                        viewSetData(modelBean);
                        viewSetCash(modelBean);
                        viewSetRemit(modelBeanB);
                        vBean.setCurrenyA(modelBean.getCurrencyCode());
                        vBean.setCurrenyB(modelBeanB.getCurrencyCode());
                        listBean.add(vBean);
                        i=i+1;
                    } else{
                        viewSetData(modelBean);
                        viewSetSingle(modelBean);
                        vBean.setCurrenyA(modelBean.getCurrencyCode());
                        vBean.setCurrenyB(modelBeanB.getCurrencyCode());
                        listBean.add(vBean);
                    }
            }else if(mCode.equals(ApplicationConst.CURRENCY_CNY ) ){
                viewSetData(modelBean);
                viewSetSingle(modelBean);
                vBean.setCurrenyA(modelBean.getCurrencyCode());
                listBean.add(vBean);
            }
        }
        mAdapter.setDatas(listBean);
        handleNoData();
    }

    @Override
    public void setListener() {
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MarginManagementFragment.newInstance(mActivity, BalanceEnquiryFragment.class, "");
            }
        });
    }

    @Override
    public void queryBalanceEnquiryListFail(BiiResultErrorException biiResultErrorException){
        closeProgressDialog();
        handleNoData();
    }

    //页面填充数据
    public BalanceEnquiryBean viewSetData(BalanceEnquiryModel.AccountDetaiListBean viewBean){
        String firLineInfo = PublicCodeUtils.getCurrency(mActivity, viewBean.getCurrencyCode());
        vBean.setFirstLineInfo(firLineInfo);
        String secLineLeftInfo = getResources().getString(R.string.boc_long_short_forex_current_limit);
        vBean.setSecLineLeftInfo(secLineLeftInfo);
        String thrLeftInfo = getResources().getString(R.string.boc_long_short_forex_available_limit);
        vBean.setThrLineLeftInfo(thrLeftInfo);
        return vBean;
    }
    public BalanceEnquiryBean viewSetCash(BalanceEnquiryModel.AccountDetaiListBean viewBean){
        String secRightBelFInfo = mTransQueryUtils.getcashRemit(viewBean.getCashRemit());
        vBean.setSecLineRighBeloFInfo(secRightBelFInfo);
        String thrRightBelFInfo =mTransQueryUtils.getcashRemit(viewBean.getCashRemit());
        vBean.setThrLineRighBeloFInfo(thrRightBelFInfo);
        String secRighBeloSInfo =MoneyUtils.transMoneyFormat(viewBean.getBookBalance(), viewBean.getCurrencyCode());
        vBean.setSecLineRighBeloSInfo(secRighBeloSInfo);
        String thrRightBelSInfo =MoneyUtils.transMoneyFormat(viewBean.getAvailableBalance(), viewBean.getCurrencyCode());
        vBean.setThrLineRighBeloSInfo(thrRightBelSInfo);
        return vBean;
    }
    public BalanceEnquiryBean viewSetRemit(BalanceEnquiryModel.AccountDetaiListBean viewBean) {
        String secLineRighAboFInfo =mTransQueryUtils.getcashRemit(viewBean.getCashRemit());
        vBean.setSecLineRighAboFInfo(secLineRighAboFInfo);
        String thrRighAboFInfo =mTransQueryUtils.getcashRemit(viewBean.getCashRemit());
        String secRighAboSInfo =MoneyUtils.transMoneyFormat(viewBean.getBookBalance(), viewBean.getCurrencyCode());
        vBean.setSecLineRighAboSInfo(secRighAboSInfo);
        String thrRighAboSInfo =MoneyUtils.transMoneyFormat(viewBean.getAvailableBalance(), viewBean.getCurrencyCode());
        vBean.setThrLineRighAboSInfo(thrRighAboSInfo);
        vBean.setThrLineRighAboFInfo(thrRighAboFInfo);
        return vBean;
    }
    public BalanceEnquiryBean viewSetSingle(BalanceEnquiryModel.AccountDetaiListBean viewBean) {
        if(viewBean.getCashRemit()=="00") {
            String secRighAboSInfo = MoneyUtils.transMoneyFormat(viewBean.getBookBalance(), viewBean.getCurrencyCode());
            vBean.setSecRenminbi(secRighAboSInfo);
            String thrRighAboSInfo = MoneyUtils.transMoneyFormat(viewBean.getAvailableBalance(), viewBean.getCurrencyCode());
            vBean.setThrRenminbi(thrRighAboSInfo);
        }else{
            String sec=mTransQueryUtils.getcashRemit(viewBean.getCashRemit());
            vBean.setSecSingle(sec);
            String secRigh=MoneyUtils.transMoneyFormat(viewBean.getBookBalance(), viewBean.getCurrencyCode());
            vBean.setSecRenminbi(secRigh);
            String thr=mTransQueryUtils.getcashRemit(viewBean.getCashRemit());
            vBean.setThrSingle(thr);
            String thrRigh=MoneyUtils.transMoneyFormat(viewBean.getAvailableBalance(), viewBean.getCurrencyCode());
            vBean.setThrRenminbi(thrRigh);
        }
        return vBean;
    }
    // 处理有无数据时的情况
    private void handleNoData() {
        // 处理“数据是否为空”的情况
        if(mBalanceEnquiryBean.size() > 0) {
            ll_no_data_query.setVisibility(View.GONE);
        } else {
            ll_no_data_query.setVisibility(View.VISIBLE);
            tvNodata.setText(getResources().getString(R.string.boc_qry_empty_result_again));
        }
    }

    @Override
    public void setPresenter(BalanceEnquiryContract.Presenter presenter) {
    }

    public void reInit(){
        queryBalanceEnquiry();
    }

    private void updateTitle(){
        String title = NumberUtils.formatCardNumberStrong(accountNumber);
        if(!StringUtils.isEmptyOrNull(title) || !title.equals("-")){
            updateTitleValue(title);
        }
    }

    @Override
    protected BalanceEnquiryContract.Presenter initPresenter() {
        return new BalanceEnquiryPresenter(this);
    }

    @Override
    protected String getTitleValue() {
        return NumberUtils.formatCardNumberStrong(accountNumber);
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