package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.pdf.PDFFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.assignment.SelectAgreementView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialog.MessageDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.ui.RiskAssessFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui.FinancialPositionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadShareTransitionVerify.PsnXpadShareTransitionVerifyResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.presenter.ShareConversionContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.presenter.ShareConversionPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.ProtocolSmartFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * @author zn
 * @description 中银理财--份额转换--填写页面
 * @date 2016/9/8
 */
public class ShareConversionFragment extends MvpBussFragment<ShareConversionPresenter>
        implements OnClickListener, SelectAgreementView.OnClickContractListener, ShareConversionContract.ShareConversionView {
    /**
     * 点击勾选阅读协议
     */
    private boolean IsChecked = false;
    protected SelectAgreementView viewAgreement;
    //
    //中国银行理财产品总协议书 id
    private String ProductAgreementContractId;
    //中国银行理财产品总协议书 内容
    private String ProductAgreementContrac;
    //    产品说明书id
    private String ProductDirectionContractId;
    //    产品说明书内容
    private String ProductDirectionContrac;

    //业绩基准列表-item  model
    private PsnXpadQuantityDetailResModel.ListEntity mListInfo;

    @Override
    protected ShareConversionPresenter initPresenter() {
        return new ShareConversionPresenter(this);
    }

    // ====================view定义=================start=========
    /**
     * 页面根视图
     */
    private View mRootView;

    /**
     * 投资提示内容
     */
    private TextView sharevsion_point_out;
    /**
     * 产品名称(产品名称+产品代码)
     */
    private TextView sharevsion_prod_name;
    /**
     * 净值 显示内容（-可转换份额，持有份额，是否允许撤单）
     */
    private DetailContentView sharevsion_no_net_value_view;

    private DetailContentView sharevsion_net_value_view;
    /**
     * 份额转换
     */
    private EditMoneyInputWidget sharevsion_quantity;
//    /**
//     * 协议书（选中按钮）
//     */
//    private ImageButton fragment_shareconversion_checkbox;
    /**
     * 协议内容提示
     */
//    private TextView fragment_shareconversion_agreement;
    /**
     * 下一步按钮
     */
    private Button fragment_shareconversion_butnext;
    /**
     * 理财交易账号
     */
    private TextView fragment_shareconversion_account;
    private ShareConversionInfoFragment mShareConversionFragment;
    // ====================view定义=================start=========

    // ===================接口code===============start=============
    /**
     * I00-3.8 008 PSNGetTokenId获取token
     */
    private final static int RESULT_CODE_PSNGETTOKENID = 0xff01;
    // ===================接口code定义=================end===========
    // ===================变量义=================start===========
    private static final String TAG = "ShareConversionFragment";


//    //《中国银行股份有限公司理财产品总协议书》字段
//    private static String CREDIT_CONTRACT;
//    //《产品说明书》字段
//    private static String LOAN_CONTRACT;

    //可点击的Span
//    private MClickableSpan clickableSpanFir, clickableSpanSec;
    //SpannableString对象
//    private SpannableString spannableStringFir, spannableStringSec;
    /**
     * 查询客户风险等级与产品风险等级是否匹配  PsnXpadQueryRiskMatch
     */
    private PsnXpadQueryRiskMatchResModel PsnXpadQueryRiskMatchResModel;

    /**
     * 4.70 070份额转换预交易  PsnXpadShareTransitionVerify
     */
    private PsnXpadShareTransitionVerifyResModel psnXpadShareTransitionVerifyResModel;
    //产品详情
    private PsnXpadProductDetailQueryResModel productDeta;

    /**
     * 查询客户持仓信息PsnXpadProductBalanceQuery（Xpad-38）
     */
    private PsnXpadProductBalanceQueryResModel BalanceDate;
    /**
     * 会话id
     */
    private String mConversationId = "";
    // 会话是否创建
    private boolean hasConv;
    //37接口数据 账户信息数据
    private PsnXpadAccountQueryResModel.XPadAccountEntity mItemXPadAccountEntity = null;
    // ====================变量定义=================end===========

    /**
     * 初始化View
     *
     * @param mInflater
     * @return
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_position_shareconversion_main, null);
        return mRootView;
    }

    /**
     * 标题栏左侧图标点击事件
     */
    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }

    /**
     * 设置标题
     */
    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_trans_shareconversion_main_title);
    }

    /**
     * 头部风格
     */
    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    /**
     * 标题栏右侧图标点击事件
     */
    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    /**
     * 初始化view 之前
     */
    @Override
    public void beforeInitView() {
    }

    /**
     * 是否显示右侧标题按钮
     */
    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    /**
     * 初始化view
     */
    @Override
    public void initView() {
        sharevsion_point_out = (TextView) mRootView.findViewById(R.id.sharevsion_point_out);
        sharevsion_prod_name = (TextView) mRootView.findViewById(R.id.sharevsion_prod_name);
        sharevsion_net_value_view = (DetailContentView) mRootView.findViewById(R.id.sharevsion_net_value_view);
        sharevsion_no_net_value_view = (DetailContentView) mRootView.findViewById(R.id.sharevsion_no_net_value_view);
        sharevsion_quantity = (EditMoneyInputWidget) mRootView.findViewById(R.id.sharevsion_quantity);
        viewAgreement = (SelectAgreementView) mRootView.findViewById(R.id.view_agreement);
        viewAgreement.setSelected(true);
        //产品名称+code
        sharevsion_prod_name.setText(BalanceDate.getProdName() + "（" + BalanceDate.getProdCode() + "）");
        //可转换额度
        sharevsion_no_net_value_view.addDetailRowNotLineBg(getStr(R.string.boc_trans_shareconversion_limit),
                MoneyUtils.transMoneyFormat(mListInfo.getAvailableQuantity(), mListInfo.getCurCode()),
                R.color.boc_common_bg_color, R.color.boc_text_color_money_count);//
        //持有份额
        sharevsion_no_net_value_view.addDetailRowNotLineBg(getStr(R.string.boc_trans_shareconversion_hold_limit),
                MoneyUtils.transMoneyFormat(mListInfo.getHoldingQuantity(), mListInfo.getCurCode()),
                R.color.boc_common_bg_color, 0);//
        /**
         *  是否允许撤单    isCanCancle	认购/申购撤单设置	String
         *  0：不允许撤单
         *  1：只允许当日撤单
         *  2：允许撤单.
         */
        if ("0".equalsIgnoreCase(productDeta.getIsCanCancle())) {
            sharevsion_no_net_value_view.addDetailRowNotLineBg(getStr(R.string.boc_trans_shareconversion_hold_revoke),
                    "不允许撤单", R.color.boc_common_bg_color, 0);//
        } else if ("1".equalsIgnoreCase(productDeta.getIsCanCancle())) {
            sharevsion_no_net_value_view.addDetailRowNotLineBg(getStr(R.string.boc_trans_shareconversion_hold_revoke),
                    "只允许当日撤单", R.color.boc_common_bg_color, 0);//
        } else if ("2".equalsIgnoreCase(productDeta.getIsCanCancle())) {
            sharevsion_no_net_value_view.addDetailRowNotLineBg(getStr(R.string.boc_trans_shareconversion_hold_revoke),
                    "允许撤单", R.color.boc_common_bg_color, 0);//
        }
        fragment_shareconversion_butnext = (Button) mRootView.findViewById(R.id.fragment_shareconversion_butnext);
        fragment_shareconversion_account = (TextView) mRootView.findViewById(R.id.fragment_shareconversion_account);
        fragment_shareconversion_account.setText("理财交易账户 " + NumberUtils.formatStringNumber(mListInfo.getXpadAccount()));

        LogUtils.i("bankAccount =" + mListInfo.getXpadAccount());
        sharevsion_quantity.getContentMoneyEditText().setHint(
                getString(R.string.boc_eloan_draw_input));
        sharevsion_quantity.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_black));
        sharevsion_quantity.setTitleTextBold(true);//加粗
        sharevsion_quantity.setEditWidgetTitle(getString(R.string.boc_trans_shareconversion_main_title));
        sharevsion_quantity.setRightTextViewVisibility(true);
        sharevsion_quantity.setRightTextColor(getResources().getColor(R.color.boc_text_color_red));
        sharevsion_quantity.setRightTextViewText("全部转换");
        sharevsion_quantity.setRightTextSize(11);
        sharevsion_quantity.setMaxLeftNumber(12);
        if ("027".equalsIgnoreCase(mListInfo.getCurCode() + "")) {
            sharevsion_quantity.setMaxRightNumber(0);
        } else {
            sharevsion_quantity.setMaxRightNumber(2);
        }
        sharevsion_quantity.setVisibility(View.VISIBLE);

    }


    /**
     * 初始化数据
     */
    public void initData() {
        super.initData();
        initUserLimit();
    }

    private void initUserLimit() {
        String limitString = getString(R.string.boc_trans_shareconversion_user_limits);
        viewAgreement.setAgreement(limitString);
    }

    /**
     * 初始化监听
     */
    @Override
    public void setListener() {
        viewAgreement.setOnClickContractListener(this);
        fragment_shareconversion_butnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConditions()) {
                    onClickSubmit();
                }
            }
        });
        //全部按钮监听
        sharevsion_quantity.setRightTextViewOnClick(new EditMoneyInputWidget.RightTextClickListener() {
            @Override
            public void onRightClick(View v) {
                sharevsion_quantity.setmContentMoneyEditText(
                        MoneyUtils.transMoneyFormat(mListInfo.getAvailableQuantity(),
                                mListInfo.getCurCode()) + "");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 监听view处理方法
     */
    @Override
    public void onClick(View v) {
    }

    @Override
    public void onClickContract(int index) {
        switch (index) {
            case 0:
                //《中国银行股份有限公司理财产品客户权益须知》
                start(ContractFragment.newInstance(
                        "file:///android_asset/webviewcontent/wealthmanagement/portfoliopurchase/notice/notice.html"));
//                String url = "file:///android_asset/webviewcontent/wealthmanagement/portfoliopurchase/notice/notice.html";
//                PDFFragment.newInstance(Uri.parse(url));
                break;
            case 1:
                //《产品说明书》
                LogUtils.i("BalanceDate.getProdCode() ====>" + BalanceDate.getProdCode());
//                start(ContractFragment.newInstance(
//                        "http://srh.bankofchina.com/search/finprod/getProdPage.jsp?keyword=" + BalanceDate.getProdCode()));
//                start(ContractFragment.newInstance(urls));
                String urls = WealthConst.URL_INSTRUCTION + BalanceDate.getProdCode();
                start(PDFFragment.newInstance(Uri.parse(urls)));
                break;
        }
    }

    /**
     * 请阅知勾选产品相关协议
     *
     * @return
     */
    private boolean checkConditions() {
        boolean ret = false;
        if (!viewAgreement.isSelected()) {
            showErrorDialog(getString(R.string.boc_portfolio_assignment_not_checked));
        } else {
            ret = true;
        }
        return ret;
    }
    //    =================================重写父类方法段落================start==================

    /**
     * 获取会话ID，成功调用
     */
    @Override
    public void obtainConversationSuccess(String conversationId) {
        LogUtils.i(TAG, "-------->获取会话ID--成功！");
        hasConv = true;
        mConversationId = conversationId;
        getPresenter().getPsnXpadQueryRiskMatch("", BalanceDate.getProdCode(), "", BalanceDate.getBancAccountKey());
    }

    /**
     * 获取会话ID，失败调用
     */
    @Override
    public void obtainConversationFail() {
        LogUtils.i(TAG, "-------->获取会话ID--失败！");
        closeProgressDialog();
        hasConv = false;

    }

    /**
     * 查询客户风险等级与产品风险等级是否匹配  PsnXpadQueryRiskMatch  成功调用
     *
     * @param PsnXpadQueryRiskMatchResModel
     */
    @Override
    public void obtainPsnXpadQueryRiskMatchSuccess(PsnXpadQueryRiskMatchResModel PsnXpadQueryRiskMatchResModel) {
        LogUtils.i(TAG, "-------->查询客户风险等级与产品风险等级是否匹配--成功调用");
        handlePsnXpadQueryRiskMatchResult(PsnXpadQueryRiskMatchResModel);
    }

    /**
     * 查询客户风险等级与产品风险等级是否匹配  PsnXpadQueryRiskMatch  失败调用
     */
    @Override
    public void obtainPsnXpadQueryRiskMatchFail() {
        LogUtils.i(TAG, "-------->查询客户风险等级与产品风险等级是否匹配--失败！");
        closeProgressDialog();
    }

    /**
     * I42-4.40 040产品详情查询 PsnXpadProductDetailQuery，成功调用
     */
    @Override
    public void obtainPsnXpadProductDetailQuerySuccess(PsnXpadProductDetailQueryResModel mPsnXpadProductDetailQueryResModel) {
        closeProgressDialog();
        handlePsnXpadProductDetailQueryResult(mPsnXpadProductDetailQueryResModel);
    }

    /**
     * I42-4.40 040产品详情查询PsnXpadProductDetailQuery，失败调用
     */
    @Override
    public void obtainPsnXpadProductDetailQueryFail() {
        closeProgressDialog();
    }

    /**
     * 份额转换  预交易  成功调用
     *
     * @param PsnXpadShareTransitionVerifyResModel
     */
    @Override
    public void obtainPsnXpadShareTransitionVerifySuccess(PsnXpadShareTransitionVerifyResModel PsnXpadShareTransitionVerifyResModel) {
        LogUtils.i(TAG, "-------->份额转换预交易--成功！");
        closeProgressDialog();
        handlePsnXpadShareTransitionVerifyResult(PsnXpadShareTransitionVerifyResModel);
    }

    /**
     * 份额转换  预交易  失败调用
     */
    @Override
    public void obtainPsnXpadShareTransitionVerifyFail() {
        LogUtils.i(TAG, "-------->份额转换预交易--失败！");
        closeProgressDialog();
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }
    //    =================================重写父类方法段落================end==================

    //    =================================接口回调处理段落================start==================

    /**
     * I42-4.40 040产品详情查询 PsnXpadProductDetailQuery
     */
    private void handlePsnXpadProductDetailQueryResult(PsnXpadProductDetailQueryResModel model) {
        closeProgressDialog();
        if (model != null) {
            this.productDeta = model;
        }
    }

    /**
     * 查询客户风险等级与产品风险等级是否匹配  PsnXpadQueryRiskMatch
     */
    private void handlePsnXpadQueryRiskMatchResult(PsnXpadQueryRiskMatchResModel model) {
        if (model != null) {
            this.PsnXpadQueryRiskMatchResModel = model;
            TogetPsnXpadShareTransitionVerify();
//            if ("0".equalsIgnoreCase(PsnXpadQueryRiskMatchResModel.getRiskMatch())) {
//                //0：匹配model.setPayAmount(moneyRuler.getInputMoney());start(new PortfolioConfirmFragment());
//                TogetPsnXpadShareTransitionVerify();
//            } else if ("2".equalsIgnoreCase(PsnXpadQueryRiskMatchResModel.getRiskMatch())) {
//                //2：不匹配且拒绝交易（即客户风险低于产品风险，且系统强制风险匹配，拒绝交易）showErrorDialog("不匹配且拒绝交易（即客户风险低于产品风险，且系统强制风险匹配，拒绝交易）");
//                DialogMessage2();
//            } else if ("1".equalsIgnoreCase(PsnXpadQueryRiskMatchResModel.getRiskMatch())) {
//                //1：不匹配但允许交易（即客户风险低于产品风险，但系统不强制风险匹配，可提示客户确认风险和交易）
//                DialogMessage1();
//            }
        }
    }

    /**
     * 份额转换  预交易  到信息确认页面
     */
    private void handlePsnXpadShareTransitionVerifyResult(PsnXpadShareTransitionVerifyResModel model) {
        if (model != null) {
//            if (mShareConversionFragment == null) {
//                mShareConversionFragment = new ShareConversionInfoFragment();
//            }
//            mShareConversionFragment.setShareConversionFragmentDeta(
//                    sharevsion_quantity.getContentMoney().trim(),
//                    mConversationId,BalanceDate,model,mListInfo);
//            start(mShareConversionFragment);
            Bundle bundle = new Bundle();
            bundle.putString("ContentMoney", sharevsion_quantity.getContentMoney().trim());
            bundle.putString("ConversationId", mConversationId);
            bundle.putSerializable("BalanceDate", BalanceDate);
            bundle.putSerializable("TransitionVerify", model);
            bundle.putSerializable("productDate", productDeta);
            bundle.putSerializable("mListInfo", mListInfo);
            bundle.putSerializable("mItemXPadAccountEntity", mItemXPadAccountEntity);
            ShareConversionInfoFragment fragment = new ShareConversionInfoFragment();
            fragment.setArguments(bundle);
            start(fragment);
        }
    }
//    =================================接口回调处理段落================end==================

    //=================================自定义方法段落================start==================

    /**
     * 执行预交易方法
     */
    private void TogetPsnXpadShareTransitionVerify() {
//        showLoadingDialog();
        LogUtils.i(TAG,"-------->预交易 开始");
        getPresenter().getPsnXpadShareTransitionVerify(
                mConversationId, mListInfo,
                sharevsion_quantity.getContentMoney().trim(),
                BalanceDate.getProdCode());
    }

    /**
     * 份额转换页面设置数据
     *
     * @param BalanceDate
     * @param productDate
     */
    public void setShareConversionDeta(PsnXpadProductBalanceQueryResModel BalanceDate,
                                       PsnXpadProductDetailQueryResModel productDate,
                                       PsnXpadQuantityDetailResModel.ListEntity mListInfo, PsnXpadAccountQueryResModel.XPadAccountEntity mItemXPadAccountEntity) {
        this.BalanceDate = BalanceDate;
        this.productDeta = productDate;
        this.mListInfo = mListInfo;
        this.mItemXPadAccountEntity = mItemXPadAccountEntity;
    }

    /**
     * 判断转换份额 是否等于0
     *
     * @return true 等于0，false 大于0
     */
    private boolean isEqualToZero() {
        //校验	0
        if ("0".equalsIgnoreCase(sharevsion_quantity.getContentMoney() + "")
                || "0.0".equalsIgnoreCase(sharevsion_quantity.getContentMoney() + "")
                || ".".equalsIgnoreCase(sharevsion_quantity.getContentMoney() + "")
                || ".0".equalsIgnoreCase(sharevsion_quantity.getContentMoney() + "")
                || ".00".equalsIgnoreCase(sharevsion_quantity.getContentMoney() + "")
                || "0.00".equalsIgnoreCase(sharevsion_quantity.getContentMoney() + "")
                || "0.".equalsIgnoreCase(sharevsion_quantity.getContentMoney() + "")) {

            showErrorDialog("转换份额应大于0");
            return true;
        }
        return false;
    }

    /**
     * 比较两个数值的大小
     *
     * @return -1表示小于；0代表等于，1代表大于；-5代表传递的值为“”或者null;-10代表转换格式异常
     */
    private boolean isCanNext() {
        boolean isCan = false;
        com.boc.bocma.tools.LogUtil.d("yx---------转换份额---->" + sharevsion_quantity.getContentMoney() + "");
        com.boc.bocma.tools.LogUtil.d("yx---------转换--可用余额---->" + mListInfo.getAvailableQuantity());
        int mcompareTo = MoneyUtils.compareTo(sharevsion_quantity.getContentMoney().trim() + "",
                mListInfo.getAvailableQuantity().substring(0, mListInfo.getAvailableQuantity().indexOf(".")));
        switch (mcompareTo) {
            case -10:
                showErrorDialog("格式转换异常");
                isCan = false;
                break;
            case -5:
                showErrorDialog("份额格式错误");
                isCan = false;
                break;
            case -1:
                isCan = true;
                break;
            case 0:
                isCan = true;
                break;
            case 1:
                showErrorDialog("转换份额不能大于可转换额度");
                isCan = false;
                break;
        }
        return isCan;
    }


    //点击提交按钮
    private void onClickSubmit() {
        if (StringUtils.isEmpty(sharevsion_quantity.getContentMoney())) {
            showErrorDialog(getResources().getString(R.string.boc_trans_shareconversion_result_available_useableshare));
        } else if (!isEqualToZero()) {
            if (isCanNext()) {
                //查询客户风险等级与产品风险等级是否匹配
                showLoadingDialog();
                getPresenter().getPSNCreatConversation();
            }
        }
    }
    //=================================自定义方法段落================end==================
    //=================================自定义公共方法段落================start==================

    /**
     * 通过资源id 查找 资源文件内容
     *
     * @param mResId 资源id
     * @return
     */
    private String getStr(int mResId) {
        return getContext().getString(mResId);
    }
    //=================================自定义公共方法段落================end==================

    //=================================自定义dialog方法================start==================

    /**
     * 1：不匹配但允许交易（即客户风险低于产品风险，但系统不强制风险匹配，可提示客户确认风险和交易）
     */
    private void DialogMessage1() {
        final MessageDialog dialog = new MessageDialog(getContext());
        dialog.setLeftButtonText("放弃");
        dialog.setRightButtonText("继续购买");
        dialog.setLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setRightButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TogetPsnXpadShareTransitionVerify();
                dialog.dismiss();
            }
        });
        dialog.showDialog(getString(R.string.boc_purchase_product_risk_warn_hint));
//        dialog.showDialog(PsnXpadQueryRiskMatchResModel.getRiskMsg());
    }

    /**
     * 2：不匹配且拒绝交易（即客户风险低于产品风险，且系统强制风险匹配，拒绝交易）showErrorDialog("不匹配且拒绝交易（即客户风险低于产品风险，且系统强制风险匹配，拒绝交易）");
     */
    private void DialogMessage2() {
        final MessageDialog dialog = new MessageDialog(getContext());
        dialog.setLeftButtonText("重新风险评估");
        dialog.setRightButtonText("去看看其他产品");
        dialog.setLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                start(new RiskAssessFragment(ProtocolSmartFragment.class));
            }
        });
        dialog.setRightButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                start(new FinancialPositionFragment());
            }
        });
        dialog.showDialog(getString(R.string.boc_purchase_product_risk_fail_hint));
//        dialog.showDialog(PsnXpadQueryRiskMatchResModel.getRiskMsg());
        //=================================自定义dialog方法================end==================
    }

}
