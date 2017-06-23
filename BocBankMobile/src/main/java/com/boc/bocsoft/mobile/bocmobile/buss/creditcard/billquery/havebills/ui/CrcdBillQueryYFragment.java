package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.model.BillInstallmentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.ui.BillInstallmentsFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model.CrcdBillQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model.CrcdBilledDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model.CrcdBilledModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model.CrcdSetingsInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.presenter.CrcdBillYPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.widget.CrcdBillInfoItemView;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.widget.SelectListDialog.DialogSelectListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.widget.SelectListDialog.RadioDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.ui.CrcdHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.ui.RepaymentMainFragment;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 已出账单——列表
 * Created by liuweidong on 2016/12/14.
 */
public class CrcdBillQueryYFragment extends MvpBussFragment<CrcdBillYPresenter> implements CrcdBillYContract.CrcdBillQueryView, View.OnClickListener, PinnedSectionListView.ClickListener, PullToRefreshLayout.OnLoadListener {


    private View mRootView;
    private PinnedSectionListView transactionView;
    private CrcdBillInfoItemView billInfoItemView;
    private TextView tvSendBill;
    private TextView tvRepayment;
    private TextView tvInstallment;
    private AccountBean accountBean;
    private CrcdBillQueryModel billQueryModle;
    private TextView tvBillDate;
    private TextView tvBillState;
    private LinearLayout ll_container;
    private int currentPage=1;

    private  List<CrcdBilledDetailModel.TransListBean> transBeanList = null;
    private List<ShowListBean> transactionBeans;// 查询列表组件的数据集合
    private PullToRefreshLayout flRefresh;
    private TextView tv_no_result;
    private ShowListAdapter mAdapter;
    private View headView;

    private boolean isNoMoreData=false;
    private CrcdBillInfoItemView incomeExpendInfoItem;
    private String billMonth;

    @Override
    protected CrcdBillYPresenter initPresenter() {
        return new CrcdBillYPresenter(this);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_crcd_bill, null);
        return mRootView;
    }

    public static CrcdBillQueryYFragment  newInstance(AccountBean accountBean) {
        CrcdBillQueryYFragment crcdBillQueryYFragment =new CrcdBillQueryYFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(CrcdHomeFragment.CUR_ACCOUNT,accountBean);
        crcdBillQueryYFragment.setArguments(bundle);
        return  crcdBillQueryYFragment;
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        accountBean= getArguments().getParcelable(CrcdHomeFragment.CUR_ACCOUNT);
         billMonth=getArguments().getString("","9999/99");
    }

    @Override
    public void initView() {
        super.initView();

        ll_container=(LinearLayout)mRootView.findViewById(R.id.ll_container);
        headView = View.inflate(mContext, R.layout.boc_fragment_crcd_bill_headview, null);

        tvBillDate=(TextView)headView.findViewById(R.id.bill_date);
        tvBillState=(TextView)headView.findViewById(R.id.bill_state);
        tvRepayment=(TextView)headView.findViewById(R.id.tv_immediaterepayment);
        tvInstallment=(TextView)headView.findViewById(R.id.tv_billinstallments);
        tvSendBill=(TextView)headView.findViewById(R.id.tv_send_bill);

        billInfoItemView=(CrcdBillInfoItemView)headView.findViewById(R.id.crcdBillInfoItemLayout);
        billInfoItemView.setContainerBackgroudResource(R.color.boc_text_color_green);
        incomeExpendInfoItem=(CrcdBillInfoItemView)headView.findViewById(R.id.incomeExpendInfoItemLayout);

        tv_no_result=(TextView)headView.findViewById(R.id.tv_no_result);

        headView.setVisibility(View.GONE);


        flRefresh = (PullToRefreshLayout) mRootView.findViewById(R.id.fl_refresh);
        transactionView = (PinnedSectionListView) mRootView.findViewById(R.id.tran_detail);


    }

    @Override
    public void initData() {
        super.initData();

        mTitleBarView.setRightButton(getString(R.string.boc_crcd_bill_history_title));// 设置标题右边文本

        transactionView.addHeaderView(headView, null, false);
        mAdapter = new ShowListAdapter(mContext, -1);
        transactionView.saveAdapter(mAdapter);
        transactionView.setShadowVisible(false);
        transactionView.setAdapter(mAdapter);

        showLoadingDialog();

//        getPresenter().queryCrcdBillIsExist(accountBean.getAccountId());
        billQueryModle=new CrcdBillQueryModel();
        billQueryModle.setAccountId(accountBean.getAccountId());
        billQueryModle.setStatementMonth("9999/99");
        getPresenter().crcdQueryBilledTrans(billQueryModle);

    }


    @Override
    public void setListener() {
        super.setListener();
        flRefresh.setOnLoadListener(this);
        transactionView.setListener(this);
        tvRepayment.setOnClickListener(this);
        tvInstallment.setOnClickListener(this);
        tvSendBill.setOnClickListener(this);
    }

    /**
     *   初始化账单信息界面
     */
    private void updateBillView(CrcdBilledModel billedModle) {
        headView.setVisibility(View.VISIBLE);
        tvBillDate.setText(billedModle.getCrcdCustomerInfo().getBillDate());

        billMonth=billedModle.getCrcdCustomerInfo().getBillDate().substring(0,7);

        String bilState;
        if ((!StringUtil.isNullOrEmpty(billedModle.getCrcdCustomerInfo().getCurTermBalanceflag1())&&"0".equals(billedModle.getCrcdCustomerInfo().getCurTermBalanceflag1()))
                ||(!StringUtil.isNullOrEmpty(billedModle.getCrcdCustomerInfo().getCurTermBalanceflag2())&&"0".equals(billedModle.getCrcdCustomerInfo().getCurTermBalanceflag2()))){
            bilState= "还款日 "+billedModle.getCrcdCustomerInfo().getRepayDate().substring(5);
            tvBillState.setTextColor(getResources().getColor(R.color.boc_text_color_yellow));
            tvBillState.setBackgroundResource(R.drawable.bg_crcd_bill_state_yellow);

        }else{
            bilState= "已还清";
            tvBillState.setTextColor(getResources().getColor(R.color.boc_text_color_green));
            tvBillState.setBackgroundResource(R.drawable.bg_crcd_bill_state_green);
        }
        tvBillState.setText(bilState);

        billInfoItemView.showRightContent(true);
        String haveNotRepay1=null;
        if (!StringUtil.isNullOrEmpty(billedModle.getCrcdCustomerInfo().getCurTermBalanceflag1())&&"0".equals(billedModle.getCrcdCustomerInfo().getCurTermBalanceflag1()))
            haveNotRepay1=billedModle.getCrcdCustomerInfo().getCurTermBalance1();
        String haveNotRepay2=null;
        if (!StringUtil.isNullOrEmpty(billedModle.getCrcdCustomerInfo().getCurTermBalanceflag2())&&"0".equals(billedModle.getCrcdCustomerInfo().getCurTermBalanceflag2()))
            haveNotRepay2=billedModle.getCrcdCustomerInfo().getCurTermBalance2();

        billInfoItemView.setRightContentText(getString(R.string.boc_crcd_bill_y_bill_not_repay),
                StringUtil.isNullOrEmpty(haveNotRepay1)? getString(R.string.boc_crcd_bill_y_currency_amount, PublicCodeUtils.getCurrency(mContext,billedModle.getCrcdCustomerInfo().getCurrencyCode1()),
                        MoneyUtils.transMoneyFormat("0.00",billedModle.getCrcdCustomerInfo().getCurrencyCode1()) ):
                        getString(R.string.boc_crcd_bill_y_currency_amount, PublicCodeUtils.getCurrency(mContext,billedModle.getCrcdCustomerInfo().getCurrencyCode1()),
                       MoneyUtils.transMoneyFormat(haveNotRepay1,billedModle.getCrcdCustomerInfo().getCurrencyCode1()) ),
                StringUtil.isNullOrEmpty(haveNotRepay2)?"":
                        getString(R.string.boc_crcd_bill_y_currency_amount, PublicCodeUtils.getCurrency(mContext,billedModle.getCrcdCustomerInfo().getCurrencyCode2()),
                                haveNotRepay2 ));

        billInfoItemView.setLeftContentText(getString(R.string.boc_crcd_bill_y_bill_amount),
                getString(R.string.boc_crcd_bill_y_currency_amount, PublicCodeUtils.getCurrency(mContext,billedModle.getCrcdCustomerInfo().getCurrencyCode1()),
                        billedModle.getCrcdCustomerInfo().getCurTermBalance1()),
                StringUtil.isNullOrEmpty(billedModle.getCrcdCustomerInfo().getCurrencyCode2())?"":
                        getString(R.string.boc_crcd_bill_y_currency_amount, PublicCodeUtils.getCurrency(mContext,billedModle.getCrcdCustomerInfo().getCurrencyCode2()),
                                billedModle.getCrcdCustomerInfo().getCurTermBalance2()) );


        //支出部分
        incomeExpendInfoItem.setVisibility(View.VISIBLE);
        String foreignExpendAoumnt=billedModle.getCrcdAccountInfoList().size()>1?
                MoneyUtils.transMoneyFormat(billedModle.getCrcdAccountInfoList().get(1).getTotalExpend(),billedModle.getCrcdAccountInfoList().get(1).getAcntType()):"";

        incomeExpendInfoItem.setLeftContentText("支出",
                getString(R.string.boc_crcd_bill_y_currency_amount,PublicCodeUtils.getCurrencyWithLetter(mContext,billedModle.getCrcdAccountInfoList().get(0).getAcntType()),
                MoneyUtils.transMoneyFormat(billedModle.getCrcdAccountInfoList().get(0).getTotalExpend(),billedModle.getCrcdAccountInfoList().get(0).getAcntType())),
                StringUtil.isNullOrEmpty(foreignExpendAoumnt)?"":getString(R.string.boc_crcd_bill_y_currency_amount,PublicCodeUtils.getCurrencyWithLetter(mContext,billedModle.getCrcdAccountInfoList().get(1).getAcntType()),
                        foreignExpendAoumnt),true);
        incomeExpendInfoItem.setLeftContentTextColor(getResources().getColor(R.color.boc_text_color_dark_gray),
                Double.parseDouble(billedModle.getCrcdAccountInfoList().get(0).getTotalExpend().replace(",",""))>0?getResources().getColor(R.color.boc_text_color_dark_gray):getResources().getColor(R.color.boc_text_color_rest_gray),
                (!StringUtil.isNullOrEmpty(foreignExpendAoumnt)&& Double.parseDouble(foreignExpendAoumnt.replace(",",""))>0)?getResources().getColor(R.color.boc_text_color_dark_gray):getResources().getColor(R.color.boc_text_color_rest_gray),true);


        //收入
        String foreignDepAoumnt=billedModle.getCrcdAccountInfoList().size()>1?MoneyUtils.formatMoney(billedModle.getCrcdAccountInfoList().get(1).getTotalDeposit()):
                "";
        incomeExpendInfoItem.setRightContentText("收入",
                getString(R.string.boc_crcd_bill_y_currency_amount,PublicCodeUtils.getCurrencyWithLetter(mContext,billedModle.getCrcdAccountInfoList().get(0).getAcntType()),
                        MoneyUtils.transMoneyFormat(billedModle.getCrcdAccountInfoList().get(0).getTotalDeposit(),billedModle.getCrcdAccountInfoList().get(0).getAcntType())),
                StringUtil.isNullOrEmpty(foreignDepAoumnt)?"":getString(R.string.boc_crcd_bill_y_currency_amount,PublicCodeUtils.getCurrencyWithLetter(mContext,billedModle.getCrcdAccountInfoList().get(1).getAcntType()),
                        foreignDepAoumnt),true);
        incomeExpendInfoItem.setRightContentTextColor(getResources().getColor(R.color.boc_text_color_dark_gray),
                Double.parseDouble(billedModle.getCrcdAccountInfoList().get(0).getTotalDeposit().replace(",",""))>0?getResources().getColor(R.color.boc_text_color_dark_gray):getResources().getColor(R.color.boc_text_color_rest_gray),
                (!StringUtil.isNullOrEmpty(foreignExpendAoumnt)&& Double.parseDouble(foreignDepAoumnt.replace(",",""))>0)?getResources().getColor(R.color.boc_text_color_dark_gray):getResources().getColor(R.color.boc_text_color_rest_gray),true);

        ll_container.setVisibility(View.VISIBLE);

    }


    @Override
    public void onItemClickListener(int position) {
        gotoTransDetailFragment(transBeanList.get(position));
    }
    /**
     *  跳转详情页面
     */
    private void gotoTransDetailFragment(CrcdBilledDetailModel.TransListBean transDetail) {
        CrcdBillHistoryDetailsFragment detailsFragment=new CrcdBillHistoryDetailsFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(CrcdBillHistoryDetailsFragment.DETAIL_INFO,transDetail);
        detailsFragment.setArguments(bundle);
        start(detailsFragment);
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_immediaterepayment){
            Bundle bundle = new Bundle();
            bundle.putParcelable(CrcdHomeFragment.CUR_ACCOUNT, accountBean);
            RepaymentMainFragment fragment = new RepaymentMainFragment();
            fragment.setArguments(bundle);
            start(fragment);
        }else if (v.getId()==R.id.tv_billinstallments){
            if (ApplicationConst.CURRENCY_CNY.equals(accountBean.getCurrencyCode())||
                    ApplicationConst.CURRENCY_CNY.equals(accountBean.getCurrencyCode2())){
                showLoadingDialog();
                getPresenter().crcdDividedPayBillSetInput(accountBean);
            }else {
                ToastUtils.show("仅支持人民币账单分期");
            }


        }else if (v.getId()==R.id.tv_send_bill){
//            ToastUtils.show("发送账单");
            showLoadingDialog();
            getPresenter().querySettingsInfo(accountBean.getAccountId());
        }
    }

    @Override
    public void queryCrcdBillIsExistSuccess(String billExsitFlag) {
//        if ("1".equals(billExsitFlag)){// 1-已出账单  0-没有出账单
//        billQueryModle.setStatementMonth("9999/99");
//        getPresenter().crcdQueryBilledTrans(billQueryModle);
//        }else{
//            closeProgressDialog();
//        }
    }

    @Override
    public void crcdQueryBilledTrans(CrcdBilledModel billedModle) {

        billQueryModle.setStatementMonth(billedModle.getCrcdCustomerInfo().getBillDate());
//        billQueryModle.setAccountType();//可选 为空时不区分账户查询
//        billQueryModle.setPrimary();
        billQueryModle.setCreditcardId(billedModle.getCrcdCustomerInfo().getCreditcardId());
        billQueryModle.setLineNum("10");

        loadTransDetailsData();

        updateBillView(billedModle);

    }


    @Override
    public void crcdQueryBilledTransDetail(CrcdBilledDetailModel billedDetailModel) {
        closeProgressDialog();
        if (billedDetailModel==null||billedDetailModel.getTransList()==null){
            flRefresh.loadmoreCompleted(PullToRefreshLayout.FAIL);
            if (currentPage==1) {
                tv_no_result.setVisibility(View.VISIBLE);
            }
            return;
        }

        currentPage++;

        if (currentPage > 1) {
            if (billedDetailModel.getTransList()!=null&&!StringUtil.isNullOrEmpty(billedDetailModel.getDealCount())
                    &&billedDetailModel.getTransList().size()<Integer.parseInt(billedDetailModel.getDealCount()))
                flRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            else{
                isNoMoreData=true;
                flRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            }
        }
        if (Integer.parseInt(billedDetailModel.getDealCount())==0 || transactionView.getAdapter().getCount() == 0) {
            tv_no_result.setVisibility(View.VISIBLE);
            return;
        }
        mAdapter.setData(generateTransactionBean(billedDetailModel));

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (!isNoMoreData)
            loadTransDetailsData();
        else
            flRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
    }

    /**
     *  查询账单分期上下限 返回结果
     *  跳转账单分期
     */
    @Override
    public void crcdDividedPayBillSetInput(BigDecimal upInstmtAmount, BigDecimal lowInstmtAmount) {
        closeProgressDialog();
        if (upInstmtAmount!=null&&lowInstmtAmount!=null){
            BillInstallmentModel billInstallmentModel=new BillInstallmentModel();
            billInstallmentModel.setUpInstmtAmount(upInstmtAmount);
            billInstallmentModel.setLowInstmtAmount(lowInstmtAmount);
            start(BillInstallmentsFragment.getInstance(billInstallmentModel,accountBean));
        }
    }

    @Override
    public void querySettingsInfo(CrcdSetingsInfoModel crcdSetingsInfoModel) {
        closeProgressDialog();
        if ("0".equals(crcdSetingsInfoModel.getEmailStatmentStaus())&&"0".equals(crcdSetingsInfoModel.getPhoneStatmentStaus())){
            ToastUtils.show(getString(R.string.boc_crcd_bill_not_settings));
        }else{
            showSendBillDialog(crcdSetingsInfoModel);
        }

    }

    @Override
    public void reSetEmailPagerCheck() {
        closeProgressDialog();
        ToastUtils.show(getString(R.string.boc_crcd_bill_sendemail_suc));
    }

    @Override
    public void reSetSmsPagerCheck() {
        closeProgressDialog();
        ToastUtils.show(getString(R.string.boc_crcd_bill_sendsms_suc));
    }

    /**
    *  发送账单对话框
    */
    private void showSendBillDialog(CrcdSetingsInfoModel crcdSetingsInfoModel) {
        final RadioDialog radioDialog=new RadioDialog(mContext);

        DialogSelectListAdapter adapter=new DialogSelectListAdapter(mContext);
        adapter.setData(getSendBilledWay(crcdSetingsInfoModel));
        radioDialog.setAdapter(adapter);
//        radioDialog.setListData();
        if (adapter.getCount()==0){
            ToastUtils.show(getString(R.string.boc_crcd_bill_not_settings));
            return;
        }
        radioDialog.setRightButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioDialog.dismiss();
               if (radioDialog.getSelectedItem()!=null&&radioDialog.getSelectedItem().getId()==0){//电子对账单
                   showLoadingDialog();
                   getPresenter().reSetEmailPagerCheck(accountBean.getAccountId(),billMonth,radioDialog.getSelectedItem().getValue());
               }else if (radioDialog.getSelectedItem()!=null&&radioDialog.getSelectedItem().getId()==1){//手机对账单
                   showLoadingDialog();
                   getPresenter().reSetSmsPagerCheck(accountBean.getAccountId(),billMonth,radioDialog.getSelectedItem().getValue());
               }
            }
        });
        if (!radioDialog.isShowing())
        radioDialog.show();
    }

    /**
    *  获取账单发送方式列表
    */
    private List<RadioDialog.Option> getSendBilledWay(CrcdSetingsInfoModel crcdSetingsInfoModel) {
        List<RadioDialog.Option> optionList=new ArrayList<>();
        if (!StringUtil.isNullOrEmpty(crcdSetingsInfoModel.getEmailStatment())){
            optionList.add(new RadioDialog.Option(0,getString(R.string.boc_crcd_bill_email),crcdSetingsInfoModel.getEmailStatment(),true));
        }
        if (!StringUtil.isNullOrEmpty(crcdSetingsInfoModel.getPhoneStatment())){
            optionList.add(new RadioDialog.Option(1,getString(R.string.boc_crcd_bill_sms),crcdSetingsInfoModel.getPhoneStatment(),optionList.size()==0));
        }
        return optionList;
    }

    /**
     *  查询交易明细  分页加载
     */
    private  void  loadTransDetailsData(){
        showLoadingDialog();
        billQueryModle.setPageNo(currentPage+"");
        getPresenter().crcdQueryBilledTransDetail(billQueryModle);
    }



    /**
     * 根据交易明细生成TransactionView需要的TransactionBean
     * @return
     */
    public  List<ShowListBean> generateTransactionBean(CrcdBilledDetailModel billedDetailModel) {

        billQueryModle.setPrimary(billedDetailModel.getPrimary());
        billQueryModle.setPageNo(billedDetailModel.getPageNo());

        if (transactionBeans==null)
            transactionBeans = new ArrayList<>();

        transBeanList=billedDetailModel.getTransList();
        if (transBeanList==null)
            return transactionBeans;

        for (int i = 0; i < transBeanList.size(); i++) {
            LocalDate localDate = LocalDate.parse(transBeanList.get(i).getDealDt().replace("/","-"));
            String formatTime = "";// 当前时间 MM月/yyyy
            String tempTime = "";// 上一次时间
            if (localDate != null) {
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0) {
                tempTime = LocalDate.parse(transBeanList.get(i-1).getDealDt().replace("/","-")).format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)) {// child
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;
                item.setTitleID(ShowListConst.TITLE_CRCD_BILL);
                item.setTime(localDate);
                //存入类型
                if ("CRED".equals(transBeanList.get(i).getLoanSign())){
                    item.setChangeColor(true);
                    item.setContentLeftBelow("[存入]");
                }
                item.setContentLeftAbove(transBeanList.get(i).getDealDesc());
                item.setContentRightBelow(PublicCodeUtils.getCurrency(getContext(), transBeanList.get(i).getDealCcy())+
                        MoneyUtils.transMoneyFormat(transBeanList.get(i).getBalCnt(),transBeanList.get(i).getDealCcy()));
                transactionBeans.add(item);
            } else {// group
                for (int j = 0; j < 2; j++) {
                    ShowListBean itemFirst = new ShowListBean();
                    if (j == 0) {
                        itemFirst.type = ShowListBean.GROUP;
                        itemFirst.setGroupName(formatTime);
                        itemFirst.setTime(localDate);
                    } else {
                        itemFirst.type = ShowListBean.CHILD;
                        itemFirst.setTitleID(ShowListConst.TITLE_CRCD_BILL);
                        itemFirst.setTime(localDate);
                        //存入类型
                        if ("CRED".equals(transBeanList.get(i).getLoanSign())){
                            itemFirst.setChangeColor(true);
                            itemFirst.setContentLeftBelow("[存入]");
                        }

                        itemFirst.setContentLeftAbove(transBeanList.get(i).getDealDesc());
                        itemFirst.setContentRightBelow(PublicCodeUtils.getCurrency(getContext(), transBeanList.get(i).getDealCcy())+
                                MoneyUtils.transMoneyFormat(transBeanList.get(i).getBalCnt(),transBeanList.get(i).getDealCcy()));
                    }
                    transactionBeans.add(itemFirst);
                }
            }
        }


        return transactionBeans;
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }   @Override
    protected boolean isHaveTitleBarView() {
        return true;
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
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_crcd_bill_y_title);
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
        start(new CrcdBillHistoryFragment());// 历史账单
    }
}
