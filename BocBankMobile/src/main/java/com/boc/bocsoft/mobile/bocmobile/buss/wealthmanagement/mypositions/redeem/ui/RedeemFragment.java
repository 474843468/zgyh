package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.ui;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.DateUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadholdproductredeemverify.PsnXpadHoldProductRedeemVerifyResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.presenter.RedeemContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.presenter.RedeemPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.util.DateUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yx
 * @description 中银理财-我的持仓-赎回-申请界面
 * @date 2016/9/05
 */
public class RedeemFragment extends MvpBussFragment<RedeemPresenter> implements View.OnClickListener, RedeemContract.RedeemView {
    // ====================view定义=================start=========
    /**
     * 页面根视图
     */
    private View mRootView;
    /**
     * 产品名称(产品名称+产品代码)
     */
    private TextView redeem_prod_name;
    /**
     * 净值显示内容{可赎回份额，持有份额，赎回手续费，业绩报酬，浮动管理费}
     */
    private DetailContentView redeem_net_value_view;
    /**
     * 非净值显示内容{可赎回份额，持有份额，份额面值}
     */
    private DetailContentView redeem_no_net_value_view;
    /**
     * -赎回份额
     */
    private EditMoneyInputWidget redeem_redeem_quantity;
    /**
     * -赎回份额-全部赎回-不允许部分赎回
     */
    private EditChoiceWidget redeem_redeem_quantity_all;

    /**
     * 最少赎回份数和最低持有份额
     */
    private TextView redeem_minnumber_and_minhold;
    /**
     * 赎回方式
     */
    private SelectGridView redeem_redeem_type_value;
    /**
     * -赎回日期
     */
    private EditChoiceWidget redeem_redeem_date;
    /**
     * 下一步 按钮
     */
    private Button redeem_btnext;
    /**
     * 温馨提示
     */
    private TextView redeem_tips;
    // ====================view定义=================end===========

    // ===================接口code===============start=============
    /**
     * I00-3.8 008 PSNGetTokenId获取token
     */
    private final static int RESULT_CODE_PSNGETTOKENID = 0xff01;
    /**
     * I42-4.13 013持有产品赎回PsnXpadHoldProductAndRedeem
     */
    private final static int RESULT_CODE_PSNXPADHOLDPRODUCTANDREDEEM = 0xff02;
    // ===================接口code定义=================end===========

    // ===================变量义=================start===========

    /**
     * 页面跳转相关-给确认信息界面传递数据-标示
     */
    public static final String REDEEM_CONFIRM_INFO = "RedeemConfirmInfo";
    /**
     * I42-4.40 040产品详情查询PsnXpadProductDetailQuery-响应model
     */
    private PsnXpadProductDetailQueryResModel mPsnXpadProductDetailQueryResModel;
    /**
     * 查询客户持仓信息PsnXpadProductBalanceQuery（Xpad-38）
     */
    private PsnXpadProductBalanceQueryResModel mPsnXpadProductBalanceQueryResModel;

    /**
     * 是否允许部分赎回
     */
    private boolean isPartRedeemQuantity = false;
    /**
     * 是否指定日期赎回
     */
    private boolean isRedeemDate = false;
    /**
     * 是否是净值类型
     */
    private boolean isNetValue = false;
    /**
     * 赎回方式的数据
     */
    private List<Content> selectGridViewList;
    /**
     * 会话id
     */
    private String mConversationId = "";
    /**
     * 是否立即赎回
     */
    private boolean isApplyNow = true;
    /**
     * 推荐过来的默认显示赎回份额
     */
    private String mRedeemQuantity = "";
    /**
     * 推荐过来的是否显示赎回份额
     */
    private boolean isShowRedeemQuantity = false;

    /**
     * 系统日期
     */
    private String mSystemDate = "";
    //业绩基准-详情进入-传递使用此数据，不用36接口数据
    private PsnXpadQuantityDetailResModel.ListEntity mListInfo = null;
    // 是否是业绩基准进入界面
    private boolean isOutStand = false;
    //37接口数据 账户详情
    private PsnXpadAccountQueryResModel.XPadAccountEntity xpadAccountEntity = null;
    //业绩基准-交易流水号
    private String mTranSeq = "";
    // ====================变量定义=================end===========


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_position_redeem_main, null);
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
     * 标题栏右侧图标点击事件
     */
    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    /**
     * 设置标题
     */
    @Override
    protected String getTitleValue() {
        return getContext().getString(R.string.boc_position_redeem_main_title);
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
     * 是否显示红头
     *
     * @return
     */
    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    /**
     * 初始化view
     */
    @Override
    public void initView() {
        redeem_prod_name = (TextView) mRootView.findViewById(R.id.redeem_prod_name);
        redeem_net_value_view = (DetailContentView) mRootView.findViewById(R.id.redeem_net_value_view);
        redeem_no_net_value_view = (DetailContentView) mRootView.findViewById(R.id.redeem_no_net_value_view);
        redeem_redeem_quantity = (EditMoneyInputWidget) mRootView.findViewById(R.id.redeem_redeem_quantity);
        redeem_redeem_quantity_all = (EditChoiceWidget) mRootView.findViewById(R.id.redeem_redeem_quantity_all);
        redeem_minnumber_and_minhold = (TextView) mRootView.findViewById(R.id.redeem_minnumber_and_minhold);
        redeem_redeem_type_value = (SelectGridView) mRootView.findViewById(R.id.redeem_redeem_type_value);
        redeem_redeem_date = (EditChoiceWidget) mRootView.findViewById(R.id.redeem_redeem_date);
        redeem_btnext = (Button) mRootView.findViewById(R.id.redeem_btnext);
        redeem_tips = (TextView) mRootView.findViewById(R.id.redeem_tips);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        isApplyNow = true;
        selectGridViewList = new ArrayList<Content>();
        Content mContent1 = new Content();
        mContent1.setName("立即赎回");
        mContent1.setContentNameID("0");
        mContent1.setSelected(true);
        selectGridViewList.add(mContent1);
        //是否允许指定日期赎回	String	0：否;1：是
        if ("1".equalsIgnoreCase(mPsnXpadProductDetailQueryResModel.getAppdatered())) {
            Content mContent2 = new Content();
            mContent2.setName("指定日期赎回");
            mContent2.setContentNameID("1");
            mContent2.setSelected(false);
            selectGridViewList.add(mContent2);
        }
        // 系统时间
        LocalDateTime mLocalDateTime = ApplicationContext.getInstance().getCurrentSystemDate();
        mSystemDate = mLocalDateTime.format(DateFormatters.dateFormatter1);
        LogUtil.d("yx------赎回申请界面-系统日期---->" + mSystemDate);

        redeem_redeem_type_value.setData(selectGridViewList);
        redeem_redeem_date.setChoiceTextName(getStr(R.string.boc_position_redeem_red_date));
        redeem_redeem_date.setChoiceTitleBold(true);
        redeem_redeem_date.setChoiceTextContent(DateUtils.getDateAfterDayCount(mSystemDate, 1));
        if (isShowRedeemQuantity) {
            isPartRedeemQuantity = true;
            redeem_redeem_quantity.getContentMoneyEditText().setHint(
                    getString(R.string.boc_eloan_draw_input));
            redeem_redeem_quantity.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_black));
            redeem_redeem_quantity.setTitleTextBold(true);//加粗
            redeem_redeem_quantity.setEditWidgetTitle(getString(R.string.boc_position_redeem_shares_redemption));
            redeem_redeem_quantity.setRightTextViewVisibility(true);
            redeem_redeem_quantity.setRightTextColor(getResources().getColor(R.color.boc_text_color_red));
            redeem_redeem_quantity.setRightTextViewText("全部赎回");
            redeem_redeem_quantity.setRightTextSize(11);
            redeem_redeem_quantity.setMaxLeftNumber(12);
            if ("027".equalsIgnoreCase(mPsnXpadProductDetailQueryResModel.getCurCode() + "")) {
                redeem_redeem_quantity.setMaxRightNumber(0);
                if (!StringUtils.isEmptyOrNull(mRedeemQuantity)) {
                    redeem_redeem_quantity.setmContentMoneyEditText(MoneyUtils.transMoneyFormat(mRedeemQuantity, "027"));
                }
            } else {
                redeem_redeem_quantity.setMaxRightNumber(2);
                if (!StringUtils.isEmptyOrNull(mRedeemQuantity)) {
                    redeem_redeem_quantity.setmContentMoneyEditText(MoneyUtils.transMoneyFormat(mRedeemQuantity, "001"));
                }
            }
            redeem_redeem_quantity.setEditWidgetTitle(getStr(R.string.boc_position_redeem_shares_redemption));
            redeem_redeem_quantity.setVisibility(View.VISIBLE);
            redeem_redeem_quantity_all.setVisibility(View.GONE);
        } else {
            //是否允许部分赎回	String	0：是 ,1：否
            if ("0".equalsIgnoreCase(mPsnXpadProductBalanceQueryResModel.getCanPartlyRedeem() + "")) {
                isPartRedeemQuantity = true;
                redeem_redeem_quantity.getContentMoneyEditText().setHint(
                        getString(R.string.boc_eloan_draw_input));
                redeem_redeem_quantity.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_black));
                redeem_redeem_quantity.setTitleTextBold(true);//加粗
                redeem_redeem_quantity.setEditWidgetTitle(getString(R.string.boc_position_redeem_shares_redemption));
                redeem_redeem_quantity.setRightTextViewVisibility(true);
                redeem_redeem_quantity.setRightTextColor(getResources().getColor(R.color.boc_text_color_red));
                redeem_redeem_quantity.setRightTextViewText("全部赎回");
                redeem_redeem_quantity.setRightTextSize(11);
                redeem_redeem_quantity.setMaxLeftNumber(12);
                if ("027".equalsIgnoreCase(mPsnXpadProductDetailQueryResModel.getCurCode() + "")) {
                    redeem_redeem_quantity.setMaxRightNumber(0);
                } else {
                    redeem_redeem_quantity.setMaxRightNumber(2);
                }
                redeem_redeem_quantity.setEditWidgetTitle(getStr(R.string.boc_position_redeem_shares_redemption));
                redeem_redeem_quantity.setVisibility(View.VISIBLE);
                redeem_redeem_quantity_all.setVisibility(View.GONE);
            } else if ("1".equalsIgnoreCase(mPsnXpadProductBalanceQueryResModel.getCanPartlyRedeem() + "")) {
                isPartRedeemQuantity = false;
                redeem_redeem_quantity_all.setChoiceTextName(getStr(R.string.boc_position_redeem_shares_redemption));
                redeem_redeem_quantity_all.setArrowImageGone(false);
                redeem_redeem_quantity_all.setChoiceTextContent(MoneyUtils.transMoneyFormat(mPsnXpadProductBalanceQueryResModel.getAvailableQuantity(), mPsnXpadProductBalanceQueryResModel.getCurCode()) + "");
                redeem_redeem_quantity_all.setVisibility(View.VISIBLE);
                redeem_redeem_quantity.setVisibility(View.GONE);
            }
        }

        handlePsnXpadProductDetailQueryResult(mPsnXpadProductDetailQueryResModel);
    }

    /**
     * 初始化监听
     */
    @Override
    public void setListener() {

        redeem_redeem_quantity.setRightTextViewOnClick(new EditMoneyInputWidget.RightTextClickListener() {//全部按钮监听
            @Override
            public void onRightClick(View v) {
                redeem_redeem_quantity.setmContentMoneyEditText(MoneyUtils.transMoneyFormat(mPsnXpadProductBalanceQueryResModel.getAvailableQuantity(), mPsnXpadProductBalanceQueryResModel.getCurCode()) + "");
            }
        });
        redeem_btnext.setOnClickListener(this);
        redeem_redeem_date.setOnClickListener(this);
        redeem_redeem_type_value.setListener(new SelectGridView.ClickListener() {
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) { //赎回方式
                for (int i = 0; i < selectGridViewList.size(); i++) {
                    selectGridViewList.get(i).setSelected(false);
                    if (i == position) {
                        selectGridViewList.get(position).setSelected(true);
                    }
                }
                redeem_redeem_type_value.getAdapter().notifyDataSetChanged();
                if (position == 0) {
                    redeem_redeem_date.setVisibility(View.GONE);
                    isApplyNow = true;
                    isRedeemDate = false;
                } else if (position == 1) {
                    redeem_redeem_date.setVisibility(View.VISIBLE);
                    isApplyNow = false;
                    isRedeemDate = true;
                }
            }
        });
    }

    
    /**
     * 监听view处理方法
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.redeem_redeem_date) {//赎回日期
            LocalDate mStartDate = LocalDate.parse(DateUtils.getDateAfterDayCount(mSystemDate, 1), DateFormatters.dateFormatter1);
            LocalDate mEndDate = LocalDate.parse(mPsnXpadProductDetailQueryResModel.getRedEmptionEndDate(), DateFormatters.dateFormatter1);
            long minDate = DateUtil.parse(mStartDate.format(DateFormatters.dateFormatter1));
            long maxDate = DateUtil.parse(mEndDate.format(DateFormatters.dateFormatter1));
            DateTimePicker.showRangeDatePick(mContext, ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(), minDate, maxDate, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
                @Override
                public void onChoiceDateSet(String strChoiceTime, LocalDate localDate) {
                    redeem_redeem_date.setChoiceTextContent(strChoiceTime);
                }
            });

        } else if (v.getId() == R.id.redeem_btnext) {//下一步按钮
            if (isPartRedeemQuantity) {
                if (StringUtils.isEmpty(redeem_redeem_quantity.getContentMoney())) {
                    showErrorDialog("请输入赎回份额");
                } else if (!isEqualToZero()) {
                    if (isCanNext()) {
                        if (isMoreMinValue()) {
                            showLoadingDialog();
                            getPresenter().getPSNCreatConversation();
                        }
                    }
                }
            } else {
                if (StringUtils.isEmpty(redeem_redeem_quantity_all.getChoiceTextContent().toString())) {
                    showErrorDialog("请输入赎回份额");
                } else if (!isEqualToZero()) {
//                    if (isCanNext()) {
//                        if (isMoreMinValue()) {
                    showLoadingDialog();
                    getPresenter().getPSNCreatConversation();
//                        }
//                    }
                }

            }

        }

    }

    @Override
    protected RedeemPresenter initPresenter() {
        return new RedeemPresenter(this);
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
     * 获取会话ID，成功调用
     */


    @Override
    public void obtainConversationSuccess(String conversationId) {
        mConversationId = conversationId;
        LogUtil.d("yx-------req---指定日期--" + redeem_redeem_date.getChoiceTextContent());
        getPresenter().getPsnXpadHoldProductRedeemVerify(conversationId, mPsnXpadProductBalanceQueryResModel,
                mPsnXpadProductDetailQueryResModel, isPartRedeemQuantity, redeem_redeem_quantity.getContentMoney().trim(),
                isRedeemDate, redeem_redeem_date.getChoiceTextContent(),mTranSeq);
    }

    /**
     * 获取会话ID，失败调用
     */
    @Override
    public void obtainConversationFail() {
        closeProgressDialog();

    }

    /**
     * 持有产品赎回预交易，成功调用
     */
    @Override
    public void obtainPsnXpadHoldProductRedeemVerifySuccess(PsnXpadHoldProductRedeemVerifyResModel mPsnXpadHoldProductRedeemVerifyResModel) {
        closeProgressDialog();
        handlePsnXpadHoldProductRedeemVerify(mPsnXpadHoldProductRedeemVerifyResModel);
    }

    /**
     * 持有产品赎回预交易，失败调用
     */
    @Override
    public void obtainPsnXpadHoldProductRedeemVerifyFail() {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }
    //=================================接口回调处理段落================start==================

    /**
     * I42-4.40 040产品详情查询 PsnXpadProductDetailQuery
     *
     * @param mResModel
     */
    private void handlePsnXpadProductDetailQueryResult(PsnXpadProductDetailQueryResModel mResModel) {

        if (mResModel != null) {
            this.mPsnXpadProductDetailQueryResModel = mResModel;
            if ("2".equalsIgnoreCase(mPsnXpadProductDetailQueryResModel.getProductType())) {
                isNetValue = true;
            } else {
                isNetValue = false;
            }
            redeem_prod_name.setText(mResModel.getProdName() + "（" + mResModel.getProdCode() + "）");
            if (isNetValue) {
                String mContrfee = "业绩报酬\n(浮动管理费)";
                // SpannableString对象
                SpannableString spannableStringOne = new SpannableString(mContrfee);
                spannableStringOne.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.boc_text_color_gray)), 5, mContrfee.length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                redeem_net_value_view.addDetailRowNotLineBg(getStr(R.string.boc_position_redeem_redeem_shares), MoneyUtils.transMoneyFormat(mPsnXpadProductBalanceQueryResModel.getAvailableQuantity(), mPsnXpadProductBalanceQueryResModel.getCurCode()) + "", R.color.boc_common_bg_color, R.color.boc_text_color_money_count);//
                redeem_net_value_view.addDetailRowNotLineBg(getStr(R.string.boc_position_redeem_shares_held), MoneyUtils.transMoneyFormat(mPsnXpadProductBalanceQueryResModel.getHoldingQuantity(), mPsnXpadProductBalanceQueryResModel.getCurCode()) + "", R.color.boc_common_bg_color, 0);//
                LogUtil.d("yx--------赎回手续费----》" + mPsnXpadProductDetailQueryResModel.getRedeemFee());
                redeem_net_value_view.addDetailRowNotLineBg(getStr(R.string.boc_position_redeem_redeemfee), mPsnXpadProductDetailQueryResModel.getRedeemFee().replaceAll("\\|", "\n") + "", R.color.boc_common_bg_color, 0);//
//                redeem_net_value_view.addDetailRowNotLineBg(getStr(R.string.boc_position_redeem_contrfee) + "\n"
//                        + getStr(R.string.boc_position_redeem_float_management_fee), "实际年化收益率大于" + mResModel.getPfmcDrawStart() + "%时，超出部分收益按照" + mResModel.getPfmcDrawScale() + "%收取业绩报酬", R.color.boc_common_bg_color, 0);//?没赋值
                redeem_net_value_view.addDetailRowNotLineBgSpannable(spannableStringOne, "实际年化收益率大于" + mResModel.getPfmcDrawStart() + "%时，超出部分收益按照"
                        + mResModel.getPfmcDrawScale() + "%收取业绩报酬", R.color.boc_common_bg_color, 0);//?没赋值
            } else {
                redeem_no_net_value_view.addDetailRowNotLineBg(getStr(R.string.boc_position_redeem_redeem_shares), MoneyUtils.transMoneyFormat(mPsnXpadProductBalanceQueryResModel.getAvailableQuantity(), mPsnXpadProductBalanceQueryResModel.getCurCode()), R.color.boc_common_bg_color, R.color.boc_text_color_money_count);//
                redeem_no_net_value_view.addDetailRowNotLineBg(getStr(R.string.boc_position_redeem_shares_held), MoneyUtils.transMoneyFormat(mPsnXpadProductBalanceQueryResModel.getHoldingQuantity(), mPsnXpadProductBalanceQueryResModel.getCurCode()), R.color.boc_common_bg_color, 0);//
                redeem_no_net_value_view.addDetailRowNotLineBg(getStr(R.string.boc_position_redeem_par_value_tranches), MoneyUtils.transMoneyFormat(mPsnXpadProductBalanceQueryResModel.getSellPrice(), mPsnXpadProductBalanceQueryResModel.getCurCode()), R.color.boc_common_bg_color, 0);//
            }
            String mStr1,mStr2;
            if(StringUtils.isEmptyOrNull(mPsnXpadProductBalanceQueryResModel.getRedeemStartingAmount())){
                mStr1 = "0.00";
            }else{
                mStr1 = MoneyUtils.transMoneyFormat(mPsnXpadProductBalanceQueryResModel.getRedeemStartingAmount(), "001");
            }
            if(StringUtils.isEmptyOrNull(mPsnXpadProductBalanceQueryResModel.getLowestHoldQuantity())){
                mStr2 = "0.00";
            }else{
                mStr2 = MoneyUtils.transMoneyFormat(mPsnXpadProductBalanceQueryResModel.getLowestHoldQuantity(), mPsnXpadProductBalanceQueryResModel.getCurCode());
            }

            redeem_minnumber_and_minhold.setText("最少赎回" + mStr1 + "份，" + "最低持有份额" + mStr2);
        }
    }

    /**
     * I42-4.33 033持有产品赎回预交易PsnXpadHoldProductRedeemVerify
     *
     * @param mResModel
     */
    private void handlePsnXpadHoldProductRedeemVerify(PsnXpadHoldProductRedeemVerifyResModel mResModel) {
        if (mResModel != null) {
            RedeemConfirmInfoFragment mRedeemConfirmInfoFragment = new RedeemConfirmInfoFragment();
            if(isPartRedeemQuantity){
                mRedeemConfirmInfoFragment.setData(mConversationId, mPsnXpadProductBalanceQueryResModel, mPsnXpadProductDetailQueryResModel, mResModel, isApplyNow, redeem_redeem_quantity.getContentMoney() + "", isNetValue, xpadAccountEntity);
                start(mRedeemConfirmInfoFragment);
            }else{
                mRedeemConfirmInfoFragment.setData(mConversationId, mPsnXpadProductBalanceQueryResModel, mPsnXpadProductDetailQueryResModel, mResModel, isApplyNow, mPsnXpadProductBalanceQueryResModel.getAvailableQuantity() + "", isNetValue, xpadAccountEntity);
                start(mRedeemConfirmInfoFragment);
            }

        }
    }

    //=================================接口回调处理段落================end==================

    //=================================自定义方法段落================start==================

    /**
     * @param mPsnXpadProductBalanceQueryResModel I42-4.36 036查询客户持仓信息
     * @param psnXpadProductDetailQueryResModel   I42-4.40 040 产品详情查询
     * @param xpadAccountEntity                   37账户详情接口
     */
    public void setData(PsnXpadProductBalanceQueryResModel mPsnXpadProductBalanceQueryResModel, PsnXpadProductDetailQueryResModel psnXpadProductDetailQueryResModel, PsnXpadAccountQueryResModel.XPadAccountEntity xpadAccountEntity) {
        this.mPsnXpadProductBalanceQueryResModel = mPsnXpadProductBalanceQueryResModel;
        this.mPsnXpadProductDetailQueryResModel = psnXpadProductDetailQueryResModel;
        this.xpadAccountEntity = xpadAccountEntity;
    }

    /**
     * @param mPsnXpadProductBalanceQueryResModel I42-4.36 036查询客户持仓信息
     * @param psnXpadProductDetailQueryResModel   I42-4.40 040 产品详情查询
     * @param mListInfo                           业绩基准 详情传递数据
     * @param xpadAccountEntity                   37账户详情接口
     */
    public void setData(PsnXpadProductBalanceQueryResModel mPsnXpadProductBalanceQueryResModel, PsnXpadProductDetailQueryResModel psnXpadProductDetailQueryResModel, PsnXpadQuantityDetailResModel.ListEntity mListInfo, PsnXpadAccountQueryResModel.XPadAccountEntity xpadAccountEntity) {
        this.mPsnXpadProductBalanceQueryResModel = mPsnXpadProductBalanceQueryResModel;
        this.mPsnXpadProductDetailQueryResModel = psnXpadProductDetailQueryResModel;
        this.mListInfo = mListInfo;
        this.xpadAccountEntity = xpadAccountEntity;
        mPsnXpadProductBalanceQueryResModel.setBancAccount(mListInfo.getBancAccount());
        mPsnXpadProductBalanceQueryResModel.setHoldingQuantity(mListInfo.getHoldingQuantity());
        mPsnXpadProductBalanceQueryResModel.setAvailableQuantity((mListInfo.getAvailableQuantity()));
        mPsnXpadProductBalanceQueryResModel.setCanPartlyRedeem(mListInfo.getCanPartlyRedeem());
        mPsnXpadProductBalanceQueryResModel.setLowestHoldQuantity(mListInfo.getLowestHoldQuantity());
        mPsnXpadProductBalanceQueryResModel.setRedeemStartingAmount(mListInfo.getRedeemStartingAmount());
        mPsnXpadProductBalanceQueryResModel.setBancAccountKey(mListInfo.getBancAccountKey());
        mPsnXpadProductBalanceQueryResModel.setTranSeq(mListInfo.getTranSeq());
        mPsnXpadProductBalanceQueryResModel.setCashRemit(mListInfo.getCashRemit());
        mPsnXpadProductBalanceQueryResModel.setProdEnd(mListInfo.getProdEnd());
        mPsnXpadProductBalanceQueryResModel.setCurCode(mListInfo.getCurCode());
        mPsnXpadProductBalanceQueryResModel.setXpadAccount(mListInfo.getXpadAccount());
        mPsnXpadProductBalanceQueryResModel.setCurrPeriod(mListInfo.getCurrPeriod());
        mPsnXpadProductBalanceQueryResModel.setTotalPeriod(mListInfo.getTotalPeriod());
        mPsnXpadProductBalanceQueryResModel.setProdBegin(mListInfo.getProdBegin());
        mTranSeq = mListInfo.getTranSeq();
        isOutStand = true;
    }

    /**
     * @param mPsnXpadProductBalanceQueryResModel I42-4.36 036查询客户持仓信息
     * @param psnXpadProductDetailQueryResModel   I42-4.40 040 产品详情查询
     * @param redeemQuantity                      默认赎回份额
     * @param xpadAccountEntity                   37账户详情接口
     */
    public void setData(PsnXpadProductBalanceQueryResModel mPsnXpadProductBalanceQueryResModel, PsnXpadProductDetailQueryResModel psnXpadProductDetailQueryResModel,
                        String redeemQuantity, PsnXpadAccountQueryResModel.XPadAccountEntity xpadAccountEntity) {
        this.mPsnXpadProductBalanceQueryResModel = mPsnXpadProductBalanceQueryResModel;
        this.mPsnXpadProductDetailQueryResModel = psnXpadProductDetailQueryResModel;
        this.mRedeemQuantity = redeemQuantity;
        isShowRedeemQuantity = true;
        this.xpadAccountEntity = xpadAccountEntity;
    }

    /**
     * 比较两个数值的大小
     *
     * @return -1表示小于；0代表等于，1代表大于；-5代表传递的值为“”或者null;-10代表转换格式异常
     */
    private boolean isCanNext() {
        boolean isCan = false;
        LogUtil.d("yx---------赎回--赎回份额---->" + redeem_redeem_quantity.getContentMoney() + "");
        LogUtil.d("yx---------赎回--可用余额---->" + mPsnXpadProductBalanceQueryResModel.getAvailableQuantity());
        int mcompareTo = MoneyUtils.compareTo(redeem_redeem_quantity.getContentMoney().trim() + "", mPsnXpadProductBalanceQueryResModel.getAvailableQuantity());
//        int mcompareTo = MoneyUtils.compareTo(redeem_redeem_quantity.getContentMoney().trim() + "", mPsnXpadProductBalanceQueryResModel.getAvailableQuantity().substring(0, mPsnXpadProductBalanceQueryResModel.getAvailableQuantity().indexOf(".")));

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
                showErrorDialog("赎回份额不能大于可赎回份额");
                isCan = false;
                break;
        }
        return isCan;
    }

    /**
     * 比较两个数值的大小
     *
     * @return -1表示小于；0代表等于，1代表大于；-5代表传递的值为“”或者null;-10代表转换格式异常
     */
    private boolean isMoreMinValue() {
        boolean isMoreMinValue = false;
        LogUtil.d("yx---------赎回--赎回份额---->" + redeem_redeem_quantity.getContentMoney() + "");
        LogUtil.d("yx---------赎回--最少赎回份额---->" + mPsnXpadProductBalanceQueryResModel.getRedeemStartingAmount() + "");
        if (!StringUtils.isEmptyOrNull(mPsnXpadProductBalanceQueryResModel.getRedeemStartingAmount())) {
            int mcompareTo = MoneyUtils.compareTo(redeem_redeem_quantity.getContentMoney().trim() + "", mPsnXpadProductBalanceQueryResModel.getRedeemStartingAmount() + "");
//        int mcompareTo = MoneyUtils.compareTo(redeem_redeem_quantity.getContentMoney().trim() + "", mPsnXpadProductBalanceQueryResModel.getAvailableQuantity().substring(0, mPsnXpadProductBalanceQueryResModel.getAvailableQuantity().indexOf(".")));

            switch (mcompareTo) {
                case -10:
                    showErrorDialog("格式转换异常");
                    isMoreMinValue = false;
                    break;
                case -5:
                    showErrorDialog("份额格式错误");
                    isMoreMinValue = false;
                    break;
                case -1:
                    showErrorDialog("最少赎回" + MoneyUtils.transMoneyFormat(mPsnXpadProductBalanceQueryResModel.getRedeemStartingAmount(), "001") + "份");
                    isMoreMinValue = false;
                    break;
                case 0:
                    isMoreMinValue = true;
                    break;
                case 1:

                    isMoreMinValue = true;
                    break;
            }
            return isMoreMinValue;
        } else {
            return true;
        }

    }

    /**
     * 判断赎回份额 是否等于0
     *
     * @return true 等于0，false 大于0
     */
    private boolean isEqualToZero() {
        //校验	0
        if ("0".equalsIgnoreCase(redeem_redeem_quantity.getContentMoney() + "")
                || "0.0".equalsIgnoreCase(redeem_redeem_quantity.getContentMoney() + "")
                || ".".equalsIgnoreCase(redeem_redeem_quantity.getContentMoney() + "")
                || ".0".equalsIgnoreCase(redeem_redeem_quantity.getContentMoney() + "")
                || ".00".equalsIgnoreCase(redeem_redeem_quantity.getContentMoney() + "")
                || "0.00".equalsIgnoreCase(redeem_redeem_quantity.getContentMoney() + "")
                || "0.".equalsIgnoreCase(redeem_redeem_quantity.getContentMoney() + "")) {

            showErrorDialog("赎回份额应大于0");
            return true;
        }
        return false;
    }
    //=================================自定义方法段落================end==================
    //=================================自定义公共方法段落================start==================

    /**
     * String 转换成 int 类型
     *
     * @param number
     * @return
     */
    private int strToInt(String number) {
        if (!"".equals(number) && number != null) {
            return Integer.valueOf(number);
        }
        return 0;
    }

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
}
