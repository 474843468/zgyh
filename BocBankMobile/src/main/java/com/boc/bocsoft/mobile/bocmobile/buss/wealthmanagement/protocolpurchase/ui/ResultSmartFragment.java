package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolIntelligentConfirmBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.utils.ProtocolConvertUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.ResultConvertUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.WealthPublicUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.tencent.mm.sdk.openapi.SendMessageToWX;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 智能投资--操作结果
 * Created by liuweidong on 2016/11/7.
 */
public class ResultSmartFragment extends BussFragment implements AdapterView.OnItemClickListener {

    public static final String WEALTHLIST = "wealthListBean";
    public static final String VIEWMODEL = "viewmodel";
    private View rootView;
    private BaseOperationResultView layoutOperator;
    private LinearLayout llParent;
    private View detailsView;
    private TextView txtContent1;
    private TextView txtContent2;
    private TextView txtContent3;

    private ArrayList<WealthListBean> beans;
    private ProtocolModel mViewModel;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_portfolio_result, null);
        return rootView;
    }

    @Override
    public void initView() {
        layoutOperator = (BaseOperationResultView) rootView.findViewById(R.id.layout_operator);
        llParent = layoutOperator.getLayoutDetailParent();
        detailsView = View.inflate(mContext, R.layout.boc_fragment_protocol_result, null);
        txtContent1 = (TextView) detailsView.findViewById(R.id.content_1);
        txtContent2 = (TextView) detailsView.findViewById(R.id.content_2);
        txtContent3 = (TextView) detailsView.findViewById(R.id.content_3);
        llParent.addView(detailsView);
    }

    @Override
    public void initData() {
        mViewModel = getArguments().getParcelable(VIEWMODEL);
        beans = (ArrayList<WealthListBean>) getArguments().getSerializable(WEALTHLIST);
        if (beans != null && !beans.isEmpty())
            layoutOperator.setYouLikeAdapter(new LikeGridAdapter(mContext, beans), this);

        layoutOperator.updateHead(OperationResultHead.Status.SUCCESS, "投资协议申请已提交");
        layoutOperator.setDetailsName("查看详情");
        layoutOperator.setBodyBtnVisibility(View.VISIBLE);

//        txt1.setText(getArguments().getString(VALUE1));
//        txt2.setText(getArguments().getString(VALUE2));
//        if (!StringUtils.isEmptyOrNull(getArguments().getString(VALUE3))) {
//            txt3.setText(getArguments().getString(VALUE3));
//        }

        ProtocolIntelligentConfirmBean confirmBean = mViewModel.getConfirmBean();// 响应返回数据
        /*单期购买金额*/
        String money = MoneyUtils.transMoneyFormat(confirmBean.getAmount(), mViewModel.getCurCode()) + PublicCodeUtils.getCurrency(mContext, mViewModel.getCurCode());
        String minMoney = MoneyUtils.transMoneyFormat(confirmBean.getMinAmount(), mViewModel.getCurCode()) + PublicCodeUtils.getCurrency(mContext, mViewModel.getCurCode());
        String maxMoney = MoneyUtils.transMoneyFormat(confirmBean.getMaxAmount(), mViewModel.getCurCode()) + PublicCodeUtils.getCurrency(mContext, mViewModel.getCurCode());
        /*协议总持续天数*/
        String lastDays = ProtocolConvertUtils.convertPeriodAgr(confirmBean.getLastDays());
        /*协议周期*/
        String periodAgr = ProtocolConvertUtils.convertPeriodAgr(confirmBean.getPeriodAgr());
        if (mViewModel.selectedProtocol.getInstType().equals("1")) {// 周期连续协议
            String head = Html.fromHtml(mContext.getString(R.string.boc_protocol_confirm_continue_head, confirmBean.getAgrName(),
                    confirmBean.getFirstDatePur(), "<b>" + MoneyUtils.transMoneyFormat(confirmBean.getAmount(), mViewModel.getCurCode()) + "</b>")).toString();
            if (txtContent1.getPaint().measureText(head) > txtContent1.getWidth()) {
                txtContent1.setText(Html.fromHtml(mContext.getString(R.string.boc_protocol_confirm_continue_1, confirmBean.getAgrName(),
                        confirmBean.getFirstDatePur(), "<b>" + money + "</b>", confirmBean.getTotalPeriod(), confirmBean.getEachpbalDays(), lastDays)));
            } else {
                txtContent1.setText(Html.fromHtml(mContext.getString(R.string.boc_protocol_confirm_continue, confirmBean.getAgrName(),
                        confirmBean.getFirstDatePur(), "<b>" + money + "</b>", confirmBean.getTotalPeriod(), confirmBean.getEachpbalDays(), lastDays)));
            }
            txtContent2.setText(mContext.getString(R.string.boc_protocol_confirm_continue_common, confirmBean.getFailMax()));
        } else if (mViewModel.selectedProtocol.getInstType().equals("2")) {// 周期不连续协议
            if ("0".equals(mViewModel.getAmountType())) {// 定额
                txtContent1.setText(Html.fromHtml(mContext.getString(R.string.boc_protocol_confirm_no_continue_0, confirmBean.getAgrName(),
                        confirmBean.getFirstDatePur(), "<b>" + money + "</b>", confirmBean.getFirstDateRed(),
                        periodAgr, confirmBean.getTotalPeriod(), confirmBean.getEndDate(), confirmBean.getEachpbalDays())));
            } else if ("1".equals(mViewModel.getAmountType())) {// 不定额
                txtContent1.setText(mContext.getString(R.string.boc_protocol_confirm_no_continue_1, confirmBean.getAgrName(),
                        confirmBean.getFirstDatePur(), minMoney, maxMoney,
                        confirmBean.getFirstDateRed(), periodAgr, confirmBean.getTotalPeriod(), confirmBean.getEndDate(), confirmBean.getEachpbalDays()));
            }
            txtContent2.setText(mContext.getString(R.string.boc_protocol_confirm_continue_common, confirmBean.getFailMax()));
        } else if (mViewModel.selectedProtocol.getInstType().equals("3")) {// 多次购买协议
            txtContent1.setText(Html.fromHtml(mContext.getString(R.string.boc_protocol_confirm_buy, confirmBean.getAgrName(), confirmBean.getFirstDatePur(),
                    periodAgr, "<b>" + money + "</b>", confirmBean.getTotalPeriod(), lastDays, confirmBean.getWillPurCount(), confirmBean.getWillRedCount())));
            txtContent2.setText(mContext.getString(R.string.boc_protocol_confirm_continue_common, confirmBean.getFailMax()));
        } else if (mViewModel.selectedProtocol.getInstType().equals("4")) {// 多次赎回协议
            if ("0".equals(confirmBean.getIsNeedPur())) {// 期初申购
                txtContent3.setVisibility(View.VISIBLE);
                txtContent1.setText(Html.fromHtml(mContext.getString(R.string.boc_protocol_confirm_redeem_1, confirmBean.getAgrName(), confirmBean.getFirstDatePur(), "<b>" + money + "</b>")));
                txtContent2.setText(mContext.getString(R.string.boc_protocol_confirm_redeem_2, confirmBean.getFirstDateRed(), confirmBean.getOneTmredAmt(),
                        confirmBean.getPeriodAgr(), confirmBean.getOneTmredAmt(), confirmBean.getTotalPeriod(), lastDays,
                        confirmBean.getEndDate(), confirmBean.getWillPurCount(), confirmBean.getWillRedCount()));
                txtContent3.setText(mContext.getString(R.string.boc_protocol_confirm_continue_common, confirmBean.getFailMax()));
            } else {// 不申购
                txtContent3.setVisibility(View.GONE);
                txtContent1.setText(mContext.getString(R.string.boc_protocol_confirm_redeem_0, confirmBean.getAgrName()) + mContext.getString(R.string.boc_protocol_confirm_redeem_2, confirmBean.getFirstDateRed(), confirmBean.getOneTmredAmt(),
                        confirmBean.getPeriodAgr(), confirmBean.getOneTmredAmt(), confirmBean.getTotalPeriod(), lastDays,
                        confirmBean.getEndDate(), confirmBean.getWillPurCount(), confirmBean.getWillRedCount()));
                txtContent2.setText(mContext.getString(R.string.boc_protocol_confirm_continue_common, confirmBean.getFailMax()));
            }
        }
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
            if (id == ID_SHARE) {// 分享
                String url = WealthConst.getShareProductUrl(mViewModel.getProId(),mViewModel.getProductKind());
                String title = mViewModel.getProdName();
                String content = "";
                String date = "";
                date = ResultConvertUtils.convertDate(mViewModel.getProductKind(), mViewModel.getProdTimeLimit(), mViewModel.getIsLockPeriod(), mViewModel.getProductTermType());
                if (WealthConst.PRODUCT_KIND_1.equals(mViewModel.getProductKind())) {// 净值
                    String[] values = {MoneyUtils.getRoundNumber(mViewModel.getPrice(), 4, BigDecimal.ROUND_HALF_UP),
                            date, mViewModel.getSubAmount()};
                    content = WealthPublicUtils.buildShareStr("1", values,mViewModel.getCurCode());
                } else {
                    String[] values = {ResultConvertUtils.convertRate(mViewModel.getYearlyRR(), mViewModel.getRateDetail()), date,
                            mViewModel.getSubAmount()};
                    content = WealthPublicUtils.buildShareStr("0", values,mViewModel.getCurCode());
                }
                SendMessageToWX.Req req = ShareUtils.shareWebPage(0, url, title, content);
                if (getApi() != null)
                    getApi().sendReq(req);
            } else if (id == ID_PRODUCT) {// 我的持仓
                start(new FinancialPositionFragment());
            } else if (id == ID_RECORD) {// 交易记录（投资协议管理）
                start(new InvestTreatyFragment(InvestTreatyFragment.WEALTHPRODUCTFRAGMENT));
            }
        }
    }
}
