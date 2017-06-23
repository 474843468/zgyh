package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryMasterAndSupplInfo.PsnCrcdQueryMasterAndSupplInfoResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.emptyview.CommonEmptyView;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter.AttCardMainContract;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter.AttCardMainPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: liukai
 * Time：2016/12/2 16:33.
 * Created by lk7066 on 2016/12/2.
 * It's used to 附属卡主页
 */

public class AttCardMainFragment extends MvpBussFragment<AttCardMainContract.AttCardMainPresenter> implements AttCardMainContract.AttCardMainView {

    private View mRootView = null;
    private AttCardMainContract.AttCardMainPresenter mPresenter;
    //页面显示为list
    private ListView attCardList;

    //查询附属卡需要上送的值为该用户的第一张信用卡
    private String firstAccountId = "";

    private List<AttCardModel> attCardModels;
    private AttCardModel attCardModel;

    //自定义适配器
    private AttCardAdapter attCardAdapter;
    //中行账户所有账户列表，从缓存中读取
    private List<AccountBean> attCardAccountList;
    //附属卡仅限于信用卡，从中行账户中筛选出信用卡类型的账户，用于匹配得出账户Id
    private ArrayList<String> attCardAccountTypeList;// 过滤的账户类型
    //无结果页面组件，没有附属卡页面
    private CommonEmptyView attCardNoCard;

    @Override
    protected View onCreateView(LayoutInflater mInflater){
        super.onCreateView(mInflater);
        mRootView = mInflater.inflate(R.layout.fragment_attcard_main, null);
        return mRootView;
    }

    @Override
    public void initView() {
        attCardList = (ListView) mRootView.findViewById(R.id.lv_attcard);
        attCardNoCard = (CommonEmptyView) mRootView.findViewById(R.id.attcard_no_data);
        mContext = getActivity();
    }

    @Override
    public void initData(){

        mPresenter = new AttCardMainPresenter(this);

        //获取用户第一张信用卡Id
        firstAccountId = ApplicationContext.getInstance().getChinaBankAccountList(getCrcdType()).get(0).getAccountId();

        //添加数据
        getPresenter().queryMasterAndSupplInfo(firstAccountId);

        showLoadingDialog();

    }

    public ArrayList<String> getCrcdType(){
        ArrayList<String> accoutType = new ArrayList<>();
        accoutType.add(ApplicationConst.ACC_TYPE_ZHONGYIN);//103
        accoutType.add(ApplicationConst.ACC_TYPE_GRE);//104
        accoutType.add(ApplicationConst.ACC_TYPE_SINGLEWAIBI);//107
        return accoutType;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_crcd_attcard_main_title);
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
        mPresenter.unsubscribe();
        super.onDestroy();
    }

    /**
     * 将返回结果的两个嵌套的list转换为一个list
     * */
    public void getAttCardDataFromResult(List<PsnCrcdQueryMasterAndSupplInfoResult> mResult){

        filterAccountType();//获取信用卡账户列表
        //获取信用卡列表
        attCardAccountList = ApplicationContext.getInstance().getChinaBankAccountList(attCardAccountTypeList);

        int k = 0;//新的List的长度
        attCardModels = new ArrayList<>();

        //i，返回结果中的list长度，j每一个list当中小list的长度
        for(int i = 0; i < mResult.size(); i++){

            if(0 == mResult.get(i).getList().size()){
                continue;
            } else {

                for(int j = 0; j < mResult.get(i).getList().size(); j++){
                    attCardModel = new AttCardModel();
                    if(0 == j){
                        attCardModel.setFlag(1);
                    } else {
                        attCardModel.setFlag(0);
                    }
                    attCardModel.setMasterCrcdNum(mResult.get(i).getMasterCrcdNum());
                    attCardModel.setMasterCrcdProductName(mResult.get(i).getMasterCrcdProductName());
                    attCardModel.setMasterCrcdType(mResult.get(i).getMasterCrcdType());

                    attCardModel.setSubCrcdNum(mResult.get(i).getList().get(j).getSubCrcdNum());
                    attCardModel.setSubCrcdHolder(mResult.get(i).getList().get(j).getSubCrcdHolder());

                    attCardModels.add(attCardModel);
                    k++;
                }

            }

        }

        if(0 == attCardModels.size()){//无结果页面

            noDataView();
            closeProgressDialog();

        } else {

            for(int i = 0; i < attCardModels.size(); i++){

                for(int j = 0; j < attCardAccountList.size(); j++){

                    if(attCardModels.get(i).getMasterCrcdNum().equals(attCardAccountList.get(j).getAccountNumber())){
                        attCardModels.get(i).setMasterCrcdId(attCardAccountList.get(j).getAccountId());
                        continue;
                    }

                }

            }

            closeProgressDialog();

        }

    }

    /**
     * 无结果页面初始化以及操作
     * */
    public void noDataView(){

        attCardList.setVisibility(View.GONE);
        attCardNoCard.setVisibility(View.VISIBLE);
        //attCardNoCard.setEmptyTips(R.drawable.boc_cash_installment_no_account, 1, getResources().getString(R.string.boc_crcd_attcard_main_nodata_info1), getResources().getString(R.string.boc_crcd_attcard_main_nodata_info2));
        attCardNoCard.setEmptyTips(getResources().getString(R.string.boc_crcd_attcard_main_nodata_info1), R.drawable.boc_cash_installment_no_account);

//        attCardNoCard.setTextOnclickListener(new CommonEmptyView.TextOnclickListener() {
//
//            @Override
//            public void textOnclickListener() {
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(getResources().getString(R.string.boc_crcd_attcard_main_tel)));
//                AttCardMainFragment.this.startActivity(intent);
//            }
//
//        });

    }

    /**
     * 过滤账户类型
     */
    private void filterAccountType() {
        attCardAccountTypeList = new ArrayList<String>();
        // 中银系列信用卡 103
        attCardAccountTypeList.add(ApplicationConst.ACC_TYPE_ZHONGYIN);
        //长城信用卡 104
        attCardAccountTypeList.add(ApplicationConst.ACC_TYPE_GRE);
        // 单外币信用卡 107
        attCardAccountTypeList.add(ApplicationConst.ACC_TYPE_SINGLEWAIBI);
    }

    @Override
    protected AttCardMainContract.AttCardMainPresenter initPresenter() {
        return new AttCardMainPresenter(this);
    }

    @Override
    public void masterAndSupplInfoSuccess(List<PsnCrcdQueryMasterAndSupplInfoResult> psnCrcdQueryMasterAndSupplInfoResult) {

        getAttCardDataFromResult(psnCrcdQueryMasterAndSupplInfoResult);
        attCardAdapter = new AttCardAdapter(mContext, R.layout.boc_attcard_item);

        attCardList.setAdapter(attCardAdapter);
        attCardAdapter.setDatas(attCardModels);

        attCardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AttCardModel model = attCardModels.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("AttCard", model);
                AttCardTranHistoryFragment attCardTranHistoryFragment = new AttCardTranHistoryFragment();
                attCardTranHistoryFragment.setArguments(bundle);
                start(attCardTranHistoryFragment);
            }

        });

    }

    @Override
    public void masterAndSupplInfoFailed(BiiResultErrorException exception) {

    }

    @Override
    public void setPresenter(AttCardMainContract.AttCardMainPresenter presenter) {

    }

}
