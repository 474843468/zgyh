package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ShareUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.adapter.LikeGridAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.ui.InvestTreatyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui.FinancialPositionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.tencent.mm.sdk.openapi.SendMessageToWX;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 操作结果（周期滚续）
 * Created by wangf on 2016/10/27.
 */
public class ResultContinueFragment extends BussFragment implements AdapterView.OnItemClickListener{

    public static final String CONTINUE_RESULT = "Continue_Result";
    public static final String CONTINUE_WEALTHLIST = "Continue_WealthListBean";

    protected BaseOperationResultView layoutOperator;
    private View rootView;

    private ProtocolModel model;
    private ArrayList<WealthListBean> beans;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_portfolio_result, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        layoutOperator = (BaseOperationResultView) rootView.findViewById(R.id.layout_operator);
    }

    @Override
    public void initData() {
        model = getArguments().getParcelable(CONTINUE_RESULT);
        beans = (ArrayList<WealthListBean>)getArguments().getSerializable(CONTINUE_WEALTHLIST);

        if (beans != null && !beans.isEmpty())
            layoutOperator.setYouLikeAdapter(new LikeGridAdapter(mContext, beans),this);

        layoutOperator.setProcedureLayoutVisible(true);
        //layoutOperator.setLikeLayoutVisible(true);
        layoutOperator.updateHead(OperationResultHead.Status.SUCCESS, "投资协议申请已提交");
        layoutOperator.setDetailsName(getString(R.string.boc_see_detail));

        Map<String, String> resultDetail = initResultViewData();
        if (resultDetail.size() != 0) {
            Iterator it = resultDetail.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                layoutOperator.addDetailRow(entry.getKey(), entry.getValue());
            }
        } else {
            layoutOperator.setDetailsTitleIsShow(false);
            View procedure = layoutOperator.getProcedureLayout();
            TextView titleTwo = (TextView) procedure.findViewById(R.id.title_two);
            titleTwo.setVisibility(View.VISIBLE);
            titleTwo.setText(model.secondTitle);
            procedure.findViewById(R.id.view_buy_procedure).setVisibility(View.GONE);
        }

        layoutOperator.setBodyBtnVisibility(View.VISIBLE);
        layoutOperator.addContentItem(getString(R.string.boc_protocol_result_tab_1), new YouMayNeedListener(YouMayNeedListener.ID_SHARE));
        layoutOperator.addContentItem(getString(R.string.boc_protocol_result_tab_2), new YouMayNeedListener(YouMayNeedListener.ID_PRODUCT));
        layoutOperator.addContentItem(getString(R.string.boc_protocol_result_tab_3), new YouMayNeedListener(YouMayNeedListener.ID_RECORD));
    }

    @Override
    public void setListener() {
        super.setListener();
        layoutOperator.setgoHomeOnclick(new OperationResultBottom.HomeBtnCallback() {
            @Override
            public void onHomeBack() {
                ActivityManager.getAppManager().finishActivity();
            }
        });
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

//    @Override
//    protected void titleLeftIconClick() {
//        popTo(WealthDetailsFragment.class, false);
//    }

//    @Override
//    public boolean onBackPress() {
//        popTo(WealthDetailsFragment.class, false);
//        return true;
//    }
//
//    @Override
//    public boolean onBack() {
//        popTo(WealthDetailsFragment.class, false);
//        return false;
//    }

    @Override
    protected void titleLeftIconClick() {
        goHomePage();
    }

    @Override
    public boolean onBack() {
        goHomePage();
        return false;
    }

    private void goHomePage() {
        if (findFragment(WealthProductFragment.class) == null)
            mActivity.finish();
        else
            popToAndReInit(WealthProductFragment.class);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "操作结果";
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        start(WealthDetailsFragment.newInstance(beans.get(position)));
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
            if (id == ID_SHARE) {
                SendMessageToWX.Req req = ShareUtils.shareWebPage(0, WealthConst.getShareProductUrl(model.getProId(),model.getProductKind()));
                if (getApi() != null)
                    getApi().sendReq(req);//跳转到朋友圈或会话列表
            } else if (id == ID_PRODUCT) {
                start(new FinancialPositionFragment());
            } else if (id == ID_RECORD) {
                start(new InvestTreatyFragment(InvestTreatyFragment.WEALTHPRODUCTFRAGMENT));
            }
        }
    }


    /**
     * 投资结果页面数据
     */
    private Map<String, String> initResultViewData(){
        Map<String, String> resultDetail = new LinkedHashMap<>();
        resultDetail.put("交易序号", model.getSignResultBean().getTranSeq());
        resultDetail.put("协议序号", model.getSignResultBean().getContractSeq());
        resultDetail.put("协议申请日期", model.getSignResultBean().getOperateDate());
        resultDetail.put("当前产品名称", "[" + PublicCodeUtils.getCurrency(mContext, model.getSignInitBean().getCurCode()) + "]" + model.getSignInitBean().getProductName());
        resultDetail.put("产品系列名称", model.getSignInitBean().getSerialName());
        if (!ApplicationConst.CURRENCY_CNY.equals(model.getSignInitBean().getCurCode())) {
            if ("01".equals(model.getXpadCashRemit())) {
                resultDetail.put("钞/汇", "现钞");
            } else if ("02".equals(model.getXpadCashRemit())) {
                resultDetail.put("钞/汇", "现汇");
            }
        }
        resultDetail.put("购买期数", model.getTotalPeriod());
        resultDetail.put("开始期数", model.getSignResultBean().getStartPeriod() + "");
        resultDetail.put("结束期数", model.getSignResultBean().getEndPeriod() + "");
        if ("0".equals(model.getAmountTypeCode())){//定额
            resultDetail.put("基础金额模式", "定额");
            resultDetail.put("基础金额", MoneyUtils.transMoneyFormat(model.getBaseAmount(), model.getSignInitBean().getCurCode()));

        }else if("1".equals(model.getAmountTypeCode())){//不定额
            resultDetail.put("基础金额模式", "不定额");
            resultDetail.put("最低预留金额", MoneyUtils.transMoneyFormat(model.getMinAmount(), model.getSignInitBean().getCurCode()));
            resultDetail.put("最大扣款金额", MoneyUtils.transMoneyFormat(model.getMaxAmount(), model.getSignInitBean().getCurCode()));

        }
        resultDetail.put("理财交易账户", NumberUtils.formatCardNumberStrong(model.getAccountList().get(0).getAccountNo()));

        return resultDetail;
    }

}
