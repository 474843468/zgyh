package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCardQueryBindInfo.PsnCardQueryBindInfoParams;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.AccountListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.AccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.CardQueryBindInfoResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.CrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.OFAAccountStateQueryResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.PayerAndPayeeInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.presenter.TransAccSelectPagePresenter;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 选择付款账户
 * Created by wangf on 2016/6/13.
 */
public class TransPayerSelectFragment extends MvpBussFragment<TransAccSelectPagePresenter> implements AdapterView.OnItemClickListener, TransContract.TransViewAccSelectPage {

    private View mRootView;

    protected ListView mListView;

    /**
     * 账户列表的adapter
     */
    protected AccountListAdapter accountListAdapter;
    private ItemListener itemListener;

    private boolean isRequestNet = false;

    /**
     * 页面跳转数据传递
     */


    private  String transType="";

    public static final int REQUEST_CODE_SELECT_ACCOUNT = 1;
    public static final int RESULT_CODE_SELECT_ACCOUNT = 100;
//    private TransAccSelectPagePresenter accSelectPresenter;
    private AccountBean  accBean;
    private PayerAndPayeeInfoModel accAlreadyInfo;
    public static TransPayerSelectFragment newInstanceWithData(ArrayList<AccountBean> accountBeanList, PayerAndPayeeInfoModel accModel) {
        Bundle bundle1 = new Bundle();
        bundle1.putParcelable(TransRemitBlankFragment.ACCOUNT_EXIST,accModel);
        bundle1.putParcelableArrayList(TransRemitBlankFragment.ACCOUNT_LIST, accountBeanList);
        TransPayerSelectFragment fragment = new TransPayerSelectFragment();
        fragment.setArguments(bundle1);
        return fragment;
    }

    public static TransPayerSelectFragment newInstance(ArrayList<String> typelist) {
        Bundle bundle1 = new Bundle();
        bundle1.putStringArrayList(TransRemitBlankFragment.ACCOUNT_TYPE_LIST, typelist);
        TransPayerSelectFragment fragment = new TransPayerSelectFragment();
        fragment.setArguments(bundle1);
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
        accountListAdapter = new AccountListAdapter(mContext);
        accountListAdapter.setAmountInfoVisible(false);
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(accountListAdapter);
    }


//    public ListView getmListView() {
//        return mListView;
//    }


    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_account_select_account);
    }
    @Override
    public boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void initData() {
//        accSelectPresenter = new TransAccSelectPagePresenter(this);
        List<AccountBean> accountBeanList = null;
        if ((accountBeanList = getArguments().getParcelableArrayList(TransRemitBlankFragment.ACCOUNT_LIST)) == null) {
            ArrayList<String> stringList = getArguments().getStringArrayList(TransRemitBlankFragment.ACCOUNT_TYPE_LIST);
            accountBeanList = ApplicationContext.getInstance().getChinaBankAccountList(stringList);
        }
        accAlreadyInfo=getArguments().getParcelable(TransRemitBlankFragment.ACCOUNT_EXIST);
        accountListAdapter.setDatas(buildAccountModel(accountBeanList));
        accountListAdapter.loadAmountInfo();
        accountBeanList1 =(ArrayList<AccountBean>)
                ApplicationContext.getInstance().getChinaBankAccountList(getFilteredAccountType());
        int a=1;
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
//        BaseMobileActivity.ErrorDialogClickCallBack errorDialogClickCallBack=new BaseMobileActivity.ErrorDialogClickCallBack() {
//            @Override
//            public void onEnterBtnClick() {
//                if (canJumpback){
//                    setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundleBack);
//                    pop();
//                }
//            }
//        };
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }
    private Bundle bundleBack;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        accBean= accountListAdapter.getItem(position).getAccountBean();
        if (null==accBean)
            return;
        if (null!=accAlreadyInfo&&accBean.getAccountNumber().equals(accAlreadyInfo.getPayeeAccoutNum())){
            showErrorDialog("付款账户和收款账户相同，请修改");
            return;
        }

        if (isRequestNet) {
            if (itemListener != null) {
                itemListener.onItemClick(bundleBack);
            }
        } else {
            bundleBack=null;
            queryAccoutDetailInfo(accBean);
//            setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundleBack);
//            pop();
        }
    }


    public void queryAccoutDetailInfo(AccountBean accountBean) {
        showLoadingDialog();
        //103 104 107 信用卡
        if ("103".equals(accountBean.getAccountType()) || "104".equals(accountBean.getAccountType())
                || "107".equals(accountBean.getAccountType())) {
            getPresenter().queryCrcdAccountDetail(accountBean.getAccountId(), "");
        } else {
            if ("190".equals(accountBean.getAccountType())) {//190 理财账户
//                showLoadingDialog();
                getPresenter().queryPsnOFAAccountState();//查询绑定账户
            } else if ("1".equals(accountBean.getEcard())) {//电子卡绑定账户
                PsnCardQueryBindInfoParams params = new PsnCardQueryBindInfoParams();
                params.setAccountId(accountBean.getAccountId());
//                showLoadingDialog();
                getPresenter().querPsnCardQueryBindInfo(params);//查接口
            } else {
                getPresenter().queryAccountBalance(accountBean.getAccountId());
            }
        }

    }
    @Override
    public void queryPsnCardQueryBindInfoSuccess(CardQueryBindInfoResult result) {
//        closeProgressDialog();
        transType=TransRemitBlankFragment.TYPE_ECARD;
        bundleBack = new Bundle();
        bundleBack.putString(TransRemitBlankFragment.TYPE_ACC,transType);
        bundleBack.putParcelable(transType,result);
//        showLoadingDialog();
        getPresenter().queryAccountBalance(accBean.getAccountId());
    }

    @Override
    public void queryPsnCardQueryBindInfoFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    private List<String> currencyList =new ArrayList();
//    boolean canJumpback=false;
    @Override
    public void queryAccountBalanceSuccess(AccountQueryAccountDetailResult result) {
        closeProgressDialog();

        if (StringUtils.isEmptyOrNull(transType)){
            bundleBack = new Bundle();// 转账类型没有设定过，说明不是ofa后者ecard账户
            transType= TransRemitBlankFragment.TYPE_NORMAL;
            bundleBack.putString(TransRemitBlankFragment.TYPE_ACC,transType);
            bundleBack.putParcelable(transType,result);
        }else{
            bundleBack.putParcelable(TransRemitBlankFragment.TYPE_NORMAL,result);
        }
        bundleBack.putParcelable(TransRemitBlankFragment.ACCOUNT_SELECT, accBean);
        if (null!=accAlreadyInfo.getPayeeCurrencyList()&&accAlreadyInfo.getPayeeCurrencyList().size()>0){
            for (AccountQueryAccountDetailResult.AccountDetaiListBean oneBean : result.getAccountDetaiList()) {
                currencyList.add(oneBean.getCurrencyCode());
            }
            if (currencyList.contains(accAlreadyInfo.getTransCurrency())){
                setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundleBack);
                pop();
            }else{
                currencyList.retainAll(accAlreadyInfo.getPayeeCurrencyList());
                if (currencyList.size()==0){
                    showErrorDialog("收款、付款账户币种不符，请修改");
                    return;
//                    canJumpback=false;
                }else{
//                    canJumpback=true;
                    setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundleBack);
                    pop();
                }
            }
        }else{
            setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundleBack);
            pop();
        }
    }

    @Override
    public void queryAccountBalanceFailed(BiiResultErrorException exception) {
            closeProgressDialog();
            showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void queryCrcdAccountBalanceSuccess(CrcdQueryAccountDetailResult result) {
        closeProgressDialog();
        if (null==bundleBack){
            bundleBack = new Bundle();
        }
        transType=TransRemitBlankFragment.TYPE_CRCD;
        bundleBack.putString("type",transType);
        bundleBack.putParcelable(transType,result);
        bundleBack.putParcelable(TransRemitBlankFragment.ACCOUNT_SELECT, accBean);

        if (null!=accAlreadyInfo.getPayeeCurrencyList()&&accAlreadyInfo.getPayeeCurrencyList().size()>0){
            for (CrcdQueryAccountDetailResult.CrcdAccountDetailListBean oneBean:result.getCrcdAccountDetailList() ) {
                currencyList.add(oneBean.getCurrency());
            }
            if (currencyList.contains(accAlreadyInfo.getTransCurrency())){
                setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundleBack);
                pop();
            }else{
                currencyList.retainAll(accAlreadyInfo.getPayeeCurrencyList());
                if (currencyList.size()==0){
//                    canJumpback=false;
                    showErrorDialog("付款、收款账户币种不符，请修改");
                    return;
                }else{
//                    canJumpback=true;
                    setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundleBack);
                    pop();
                }
            }
        }else{
            setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundleBack);
            pop();
        }
    }

    @Override
    public void queryCrcdAccountBalanceFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }
    private OFAAccountStateQueryResult.BankAccountBean ofaBankAccount;//
    @Override
    public void queryPsnOFAAccountStateSuccess(OFAAccountStateQueryResult result) {
//        closeProgressDialog();
        String financialOpenStatus=result.getOpenStatus();
//        S-已开通   B-账户未绑定  R-账户未关联网银  N-未开通
        if (financialOpenStatus.equals("S")||financialOpenStatus.equals("R")||financialOpenStatus.equals("B")){
            if (financialOpenStatus.equals("S")){//说明有mainAcc 用于展示收款账户
                ofaBankAccount=result.getBankAccount();
                if(null==ofaBankAccount||null== getLinkedAccByAccId(String.valueOf(ofaBankAccount.getAccountId()))){
                    //绑定的收款账户不在网银关联账户里面，不报错。
                    showErrorDialog("该理财账户未绑定转账账户，请重新选择付款账户");
                    return;
                }
                bundleBack = new Bundle();
                transType=TransRemitBlankFragment.TYPE_OFA;
                bundleBack.putString(TransRemitBlankFragment.TYPE_ACC,transType);
                bundleBack.putParcelable(transType,result);
                getPresenter().queryAccountBalance(accBean.getAccountId());
            }else{
                closeProgressDialog();
                showErrorDialog("该理财账户未绑定转账账户，请重新选择付款账户");
                return;
            }
        }
        else{
            showErrorDialog("该理财账户未绑定转账账户，请重新选择付款账户");
            return;
        }
    }
    private ArrayList<AccountBean> accountBeanList1 ;//付款账户列表

    //通过accoutId 找到关联账户
    public AccountBean getLinkedAccByAccId(String accid){
        if (StringUtils.isEmptyOrNull(accid)){
            return null;
        }
        accountBeanList1 =(ArrayList<AccountBean>)
                ApplicationContext.getInstance().getChinaBankAccountList(getFilteredAccountType());
        if (null==accountBeanList1||accountBeanList1.size()==0){
            return null;
        }
        for (AccountBean oneBean:accountBeanList1){
            if(accid.equals(oneBean.getAccountId())){
                return oneBean;
            }
        }
//        showErrorDialog("账户错误，请修改");
        return null;
    }

    public ArrayList<String> getFilteredAccountType() {
        ArrayList<String> accoutType = new ArrayList<>();
//        00：对公账户,  101：普通活期,  103：中银信用卡, 104：长城信用卡 ,107: 单外币信用卡, 借记卡119（包括医保账户）
//        140：存本取息,150：零存整取,152：教育储蓄,170：定期一本通,188：活期一本通，190网上理财账户 、优汇通专户199
        String[] accType={
                ApplicationConst.ACC_TYPE_BRO,//借记卡119
                ApplicationConst.ACC_TYPE_RAN,//活期一本通188
                ApplicationConst.ACC_TYPE_ORD,//普活 101
                ApplicationConst.ACC_TYPE_GRE,//长城信用卡104
                ApplicationConst.ACC_TYPE_ZHONGYIN,//中银系列信用卡 103
                ApplicationConst.ACC_TYPE_SINGLEWAIBI,//单外币信用卡107
                ApplicationConst.ACC_TYPE_BOCINVT,//网上专属理财190
                ApplicationConst.ACC_TYPE_YOUHUITONG,//优汇通专户199
        };
        accoutType.addAll(Arrays.asList(accType));
        return accoutType;
    }
    @Override
    public void queryPsnOFAAccountStateFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void setPresenter(TransContract.TransPresenterAccSelectPage presenter) {

    }

    @Override
    protected TransAccSelectPagePresenter initPresenter() {
        return new TransAccSelectPagePresenter(this);
    }

    public interface ItemListener {
        void onItemClick(Bundle bundle);
    }

}
