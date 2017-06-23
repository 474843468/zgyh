package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.buyprocedure.BuyProcedureWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ShareUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.adapter.LikeGridAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui.FinancialPositionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui.TransInquireFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.ResultConvertUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.WealthPublicUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.tencent.mm.sdk.openapi.SendMessageToWX;

import java.util.List;

/**
 * Created by wangtong on 2016/9/19.
 */
@SuppressLint("ValidFragment")
public class PurchaseResultFragment extends BaseAccountFragment implements OperationResultBottom.HomeBtnCallback, AdapterView.OnItemClickListener {

    protected BaseOperationResultView layoutOperator;
    private View rootView;

    private PurchaseModel model;

    private List<WealthListBean> beans;

    public PurchaseResultFragment(PurchaseModel purchaseModel, List<WealthListBean> wealthListBeans) {
        this.model = purchaseModel;
        this.beans = wealthListBeans;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_loss_success_title);
    }

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
        if (beans != null && !beans.isEmpty())
            layoutOperator.setYouLikeAdapter(new LikeGridAdapter(mContext, beans), this);

        if (!WealthConst.PRODUCT_KIND_0.equals(model.getProductKind()) || (WealthConst.PRODUCT_KIND_0.equals(model.getProductKind()) && WealthConst.IS_LOCK_PERIOD_0.equals(model.getIsLockPeriod()))) {
            layoutOperator.setProcedureLayoutVisible(true, true);
            setProcedureData();
        }

        layoutOperator.updateHead(OperationResultHead.Status.SUCCESS, getString(R.string.boc_purchase_result_success));
        layoutOperator.setDetailsName(getString(R.string.boc_see_detail));
        layoutOperator.addDetailRow(getString(R.string.boc_purchase_result_transaction_num), model.getTransNum());

        String buyAmount = MoneyUtils.transMoneyFormat(model.getBuyAmount() + "", model.getCurCode());
        String currency = PublicCodeUtils.getCurrency(mContext, model.getCurCode());
        String cashRemit = "/" + AccountUtils.getCashRemit(model.getCashRemitCode());

        layoutOperator.addDetailRow(getString(R.string.boc_purchase_confirm_product), getString(R.string.boc_purchase_product_head, currency, model.getProdName(), model.getProdCode()));

        if (ApplicationConst.CURRENCY_CNY.equals(model.getCurCode()))
            layoutOperator.addDetailRow(getString(R.string.boc_purchase_product_amount), currency + " " + buyAmount);
        else
            layoutOperator.addDetailRow(getString(R.string.boc_purchase_product_amount), currency + cashRemit + " " + buyAmount);

        if (model.isFundProKind())
            layoutOperator.addDetailRow(getString(R.string.boc_purchase_confirm_fee), MoneyUtils.transMoneyFormat(model.getPurchFee(), model.getCurCode()));

        layoutOperator.addContentItem(getString(R.string.boc_purchase_result_share), new YouMayNeedListener(YouMayNeedListener.ID_SHARE));
        layoutOperator.addContentItem(getString(R.string.boc_purchase_result_my), new YouMayNeedListener(YouMayNeedListener.ID_PRODUCT));
        layoutOperator.addContentItem(getString(R.string.boc_purchase_result_transaction_record), new YouMayNeedListener(YouMayNeedListener.ID_RECORD));
    }

    private void setProcedureData() {
        String[] textStr = new String[3];
        String[] dateStr = new String[3];
        textStr[0] = "今日发起";
        dateStr[0] = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1);
        if (WealthConst.PRODUCT_TYPE_3.equals(model.getProductType())) {// 固定期限产品
            textStr[1] = "产品起息";
            dateStr[1] = model.getProductBegin();
        } else {
            textStr[1] = "开始计息";
            dateStr[1] = "付息规则";
            layoutOperator.getBuyProcedureWidget().setMiddHint(ResultConvertUtils.convertCouponpayFreq(model.getCouponpayFreq(), model.getInterestDate()));
        }
        if (WealthConst.TERM_TYPE_0.equals(model.getProductTermType())) {// 有限期封闭式
            textStr[2] = "产品到期";
            dateStr[2] = model.getProductEnd();
        } else if (WealthConst.TERM_TYPE_1.equals(model.getProductTermType())) {// 有限半开放式
            textStr[2] = "产品可赎回";
            dateStr[2] = model.getProductEnd();
        } else {
            textStr[2] = "产品赎回";
            dateStr[2] = "赎回开放规则";
            layoutOperator.getBuyProcedureWidget().setRightHint(ResultConvertUtils.convertBuyType(model));
        }
        layoutOperator.getBuyProcedureWidget().setText(textStr);
        layoutOperator.getBuyProcedureWidget().setDate(dateStr);
        layoutOperator.getBuyProcedureWidget().setStatus(BuyProcedureWidget.CompleteStatus.PAY);
    }

    @Override
    public void setListener() {
        layoutOperator.setgoHomeOnclick(this);
    }

    @Override
    public void onHomeBack() {
        mActivity.finish();
    }

    @Override
    public boolean onBackPress() {
        goHomePage();
        return true;
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
                String url = WealthConst.getShareProductUrl(model.getProdCode(), model.getProductKind());
                String title = model.getProdName();
                String content = WealthPublicUtils.buildShareStr(model);
                SendMessageToWX.Req req = ShareUtils.shareWebPage(0, url, title, content);
                if (getApi() != null)
                    getApi().sendReq(req);//跳转到朋友圈或会话列表
            } else if (id == ID_PRODUCT) {
                FinancialPositionFragment fragment = new FinancialPositionFragment();
                fragment.setBackToHome(true);
                startWithPop(fragment);
            } else if (id == ID_RECORD) {
                TransInquireFragment.newinstance(mActivity, 1);
            }
        }
    }
}
