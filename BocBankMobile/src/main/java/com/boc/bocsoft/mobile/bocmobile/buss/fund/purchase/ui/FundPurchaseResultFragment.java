package com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui.FundProductHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundguessyoulike.adapter.FundGuessYouLikeAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundguessyoulike.bean.FundListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.model.FundbuyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.presenter.FundPurchasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.widget.listview.OnItemClickListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zcy7065 on 2016/11/24.
 */
@SuppressLint("ValidFragment")
public class FundPurchaseResultFragment extends MvpBussFragment<FundPurchaseContract.FundPurchasePresenter> implements FundPurchaseContract.FundPurchaseResultView,OnItemClickListener,BaseResultView.HomeBackListener {

    private  View rootView;
    private FundbuyModel model;
    private FundGuessYouLikeAdapter gussYouLikeAdapter;
    private BaseResultView brResult;


    public FundPurchaseResultFragment(FundbuyModel fundbuyModel){
        this.model = fundbuyModel;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_loss_success_title);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return super.isDisplayRightIcon();
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        super.onCreateView(mInflater);
        rootView = mInflater.inflate(R.layout.boc_fragment_fund_result, null);
        return rootView;
    }
    @Override
    public void initView() {
        super.initView();
        brResult = (BaseResultView) rootView.findViewById(R.id.fund_purchase_result_view);
        brResult.setVisibility(View.VISIBLE);

    }

    @Override
    public void initData() {

        //头部信息
        brResult.addStatus(ResultHead.Status.SUCCESS, "基金买入的申请已经提交成功");
        brResult.addTitle(model.getBuyAmount());

        //添加详情
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("网银交易序号", model.getTransactionId());
        map.put("基金交易账号", model.getInvestAccount());
        map.put("资金账号", model.getAccount());
        map.put("基金交易流水号", model.getFundSeq());
        brResult.addDetail(map);

        //添加“您可能需要”
        brResult.addNeedItem(getString(R.string.boc_purchase_result_share),new YouMayNeedListener(YouMayNeedListener.ID_SHARE));
        brResult.addNeedItem(getString(R.string.boc_purchase_result_my),new YouMayNeedListener(YouMayNeedListener.ID_PRODUCT));
        brResult.addNeedItem(getString(R.string.boc_purchase_result_transaction_record),new YouMayNeedListener(YouMayNeedListener.ID_RECORD));

        //添加“猜你喜欢”
        List<FundListBean> list = new ArrayList<FundListBean>();
        for (int i = 0; i < 10; i++) {
            FundListBean bean = new FundListBean();
            bean.setFundName("产品" + i);
            bean.setAddUpNetVal((25 + i) + "%");
            bean.setAlwRdptDat((25 + i) + "25+i");
            list.add(bean);
        }
        gussYouLikeAdapter =new FundGuessYouLikeAdapter(getContext(),list);
        gussYouLikeAdapter.setOnItemClickListener(this);
        brResult.addLikeView(gussYouLikeAdapter);
    }

    /**
     * 顶部返回箭头
     * */
    @Override
    protected void titleLeftIconClick() {
        onClickBack();
    }

    /**
     * 底部返回首页
     * */
    @Override
    public void onHomeBack() {
        ModuleActivityDispatcher.popToHomePage();
    }

    /**
     * 物理返回键
     * */
    @Override
    public boolean onBack() {
        onClickBack();
        return false;
    }

    /**
     * 点击返回
     */
    private void onClickBack(){
        if(getFragmentManager().findFragmentByTag(FundProductHomeFragment.class.getName()) != null){
            popToAndReInit(FundProductHomeFragment.class);
        }
        else{
            getActivity().finish();
        }
    }

    @Override
    public void setListener() {
        brResult.setOnHomeBackClick(this);
    }
    @Override
    protected FundPurchaseContract.FundPurchasePresenter initPresenter() {
        return new FundPurchasePresenter(this);
    }

    class YouMayNeedListener implements View.OnClickListener {

        public static final int ID_SHARE = 101;
        public static final int ID_PRODUCT = 102;
        public static final int ID_RECORD = 103;

        int id;

        public YouMayNeedListener(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public void onItemClick(View itemView, int position) {
        ToastUtils.show("猜你喜欢产品===="+gussYouLikeAdapter.getItem(position).getFundName());
    }
}
