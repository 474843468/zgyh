package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanCosnt;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.presenter.PrepayPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.RepaymentAccountCheckRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.AccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.AccoutFragment.AccountType;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.ui.DrawListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanAccountListModel;
import com.boc.bocsoft.mobile.common.utils.ButtonClickLock;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 贷款管理-非中银E贷-提前还款Fragment Created by xintong on 2016/6/3.
 */
public class PrepayFragment extends MvpBussFragment<PrepayPresenter> implements
        PrepayContract.PrepayView {

    private static final String TAG = "PrepayFragment";
    private static String PAGENAME;
    // 获取安全因子成功后返回的默认安全因子组合id
    private String _combinId, combinId_name;
    // 预交易后，再次确认安全因子 为true调用预交易接口
    private boolean available;
    // 利息
    private TextView interest;
    // 账户id
    private String accountId;
    // 账户
    private String account;
    // 总额
    private TextView total;
    // 利息和总额
    private LinearLayout include;
    // 可用余额(不用)
    private EditChoiceWidget availableAvl;
    // 剩余未还本金
    private TextView prepayRemain_value;
    // 提前还款本金类型
//    private EditChoiceWidget prepayType;
    /**
     * 提前还款方式 全部和部分
     */
    private SelectGridView prepay_type_value;

    // 提前还款本金
    private EditMoneyInputWidget prepay_input_money;
    // private EditClearWidget prepay;
    // 还款账户
    private EditChoiceWidget repaymentAccount;
    //    private PrepayPresenter prepayPresenter;
    //可用余额 布局
    private LinearLayout ll_availableAvl;
    //可用余额 value
    private TextView tv_availableAvl_value;

    // 下一步按钮
    private Button next;
    private View mRoot;
    // 会话id
    private String conversationId;

    // 列表对话框
    private SelectListDialog selectListDialog;
    // 列表对话框
    private DrawListAdapter drawListAdapter;
    // 安全组件
    private SecurityVerity securityVerity;
    // 确认页
    private PrepayConfirmInfoFragment prepayConfirmInfoFragment;
    private EloanAccountListModel.PsnLOANListEQueryBean mPrepayDetailModel;
    // 可用余额
    private String availableBalance;
    // 未还本金
    private String remainCapital;
    // 本金加利息
    private String totalMoney;
    private PrepayVerifyReq req;
    /**
     * 提交按钮 防暴力点击
     */
    private String click_more = "click_more";
    private String mPrepayAccount = "";
    /**
     * 还款方式的数据
     */
    private List<Content> selectGridViewList;
    private String mcurrencyCode;//选择账户币种

    private String mCurrencyCOdeTest1 = ""; //传递进入的币种
    private String mCurrencyCOdeTest2 = ""; //转换后的币种 显示是 001格式
    private String mCurrencyCOdeTest3 = ""; //CNY格式

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_eloan_prepay, null);
        return mRoot;
    }

    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_prepay_pagename);
    }

    @Override
    public void beforeInitView() {
        PAGENAME = getString(R.string.boc_eloan_prepay_pagename);
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
    public void initView() {
        showLoadingDialog();
        getPresenter().creatConversation();

        securityVerity = SecurityVerity.getInstance(getActivity());
        mPrepayDetailModel = (EloanAccountListModel.PsnLOANListEQueryBean) getArguments()
                .getSerializable(EloanConst.ELON_PREPAY);
        mPrepayAccount = getArguments().getString(LoanCosnt.LOAN_REPYNUM) + "";
        LogUtils.i("cq------->提前还款传递过来的对象---mPrepayDetailModel--" + mPrepayDetailModel.toString());
        LogUtils.i("cq------->提前还款传递过来的--------mPrepayAccount--" + mPrepayAccount);
        mCurrencyCOdeTest1 = mPrepayDetailModel.getCurrencyCode();
        LogUtil.d("yx-----------传递进入的币种----" + mCurrencyCOdeTest1);
        getPresenter().setcurrencyCode(PublicCodeUtils.getCurrencyCode(mContext, mPrepayDetailModel.getCurrencyCode()));
        // 初始化组件
        prepayRemain_value = (TextView) mRoot.findViewById(R.id.prepayRemain_value);
//        prepayType = (EditChoiceWidget) mRoot.findViewById(R.id.prepayType);
        prepay_type_value = (SelectGridView) mRoot.findViewById(R.id.prepay_type_value);
        prepay_input_money = (EditMoneyInputWidget) mRoot.findViewById(R.id.prepay_input_money);
        repaymentAccount = (EditChoiceWidget) mRoot
                .findViewById(R.id.repaymentAccount);
        next = (Button) mRoot.findViewById(R.id.next);
        interest = (TextView) mRoot.findViewById(R.id.interest);
        total = (TextView) mRoot.findViewById(R.id.total);
        include = (LinearLayout) mRoot.findViewById(R.id.include);
//        availableAvl = (EditChoiceWidget) mRoot.findViewById(R.id.availableAvl);
        ll_availableAvl = (LinearLayout) mRoot.findViewById(R.id.ll_availableAvl);
        tv_availableAvl_value = (TextView) mRoot.findViewById(R.id.tv_availableAvl_value);
        mCurrencyCOdeTest2 = transforCodeFormat(mCurrencyCOdeTest1);
        LogUtil.d("yx-----------转换后的币种-数字---" + mCurrencyCOdeTest2);
        mCurrencyCOdeTest3 =  transforCodeFormatLetter(mCurrencyCOdeTest1);
        LogUtil.d("yx-----------转换后的币种-字母---" + mCurrencyCOdeTest3);

    }

    @Override
    public void initData() {
        ButtonClickLock.getLock(click_more).lockDuration = EloanConst.CLICK_MORE_TIME;
        selectGridViewList = new ArrayList<Content>();
        Content mContent1 = new Content();
        mContent1.setName("部分还款");
        mContent1.setContentNameID("0");
        mContent1.setSelected(false);
        selectGridViewList.add(mContent1);
        Content mContent2 = new Content();
        mContent2.setName("全额还款");
        mContent2.setContentNameID("1");
        mContent2.setSelected(true);
        selectGridViewList.add(mContent2);
        prepay_type_value.setData(selectGridViewList);
        // 初始化组件
        remainCapital = mPrepayDetailModel.getRemainCapital();


        prepayRemain_value.setText(PublicCodeUtils.getCurrencyWithLetter(mContext, mPrepayDetailModel.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(remainCapital, mPrepayDetailModel.getCurrencyCode()));
        prepay_input_money.setmContentMoneyEditText(MoneyUtils.transMoneyFormat(
                remainCapital, mPrepayDetailModel.getCurrencyCode()));

        BigDecimal b1 = new BigDecimal(remainCapital);
        BigDecimal b2 = new BigDecimal(mPrepayDetailModel.getThisIssueRepayInterest());
        // b2=b2.setScale(2, BigDecimal.ROUND_DOWN);
        b2 = b2.setScale(2, BigDecimal.ROUND_HALF_UP);
        totalMoney = (b2.add(b1)).toString();
        totalMoney = MoneyUtils.transMoneyFormat(totalMoney, mPrepayDetailModel.getCurrencyCode());
        total.setText(PublicCodeUtils.getCurrencyWithLetter(mContext, mPrepayDetailModel.getCurrencyCode()) + " " +totalMoney);

        LogUtils.i("金额格式位数-------------》" + totalMoney + "------" + b2);

        interest.setText(getString(R.string.boc_eloan_prepay_money_interest)
                + "：" + MoneyUtils.transMoneyFormat(b2.toString(), mPrepayDetailModel.getCurrencyCode()));
        repaymentAccount.setChoiceTextName(getString(R.string.boc_eloan_repaymentAccount));
        repaymentAccount.setChoiceTitleBold(true);
        //账户选择
        repaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
        // 还款本金
        prepay_input_money.setClearIconVisible(false);
        prepay_input_money.getContentMoneyEditText().setClickable(false);
        prepay_input_money.setTitleTextBold(true);
        prepay_input_money.setMaxLeftNumber(11);
        prepay_input_money.setMaxRightNumber(2);
        prepay_input_money.setCurrency(mPrepayDetailModel.getCurrencyCode());
        prepay_input_money.setEditWidgetTitle(getString(R.string.boc_eloan_prepay_capital));
//        prepay_input_money.getContentMoneyEditText().setTextColor(Color.RED);
        include.setVisibility(View.VISIBLE);
        prepay_input_money.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
        prepay_input_money.setMoneyEditTextShowLocation(false);

//        availableAvl.isVisibilityChoiceAutoTextView(true);
//        availableAvl.setArrowImageGone(false);


        // 初始化弹出框组件
        selectListDialog = new SelectListDialog(getContext());
        drawListAdapter = new DrawListAdapter(getContext());
        // 初始化弹出框组件列表适配器
        selectListDialog.setAdapter(drawListAdapter);
    }

    @Override
    public void setListener() {
        // 提前还款本金组件
        prepay_input_money.setMoneyInputTextWatcherListener(new EditMoneyInputWidget.MoneyInputTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogUtils.i("yx-------------->" + s.toString());
                if (!TextUtils.isEmpty(s.toString())) {
                    include.setVisibility(View.VISIBLE);
                    BigDecimal b1 = new BigDecimal(s.toString().replaceAll(",", ""));
                    BigDecimal b2 = new BigDecimal(mPrepayDetailModel.getThisIssueRepayInterest());
                    b2 = b2.setScale(2, BigDecimal.ROUND_HALF_UP);
                    // b2=b2.setScale(2, BigDecimal.ROUND_DOWN);
                    totalMoney = (b2.add(b1)).toString();
                    totalMoney = MoneyUtils.transMoneyFormat(totalMoney, mPrepayDetailModel.getCurrencyCode());
//                    total.setText("人民币元 "  + MoneyUtils.transMoneyFormat(totalMoney, "001"));
                    total.setText(PublicCodeUtils.getCurrencyWithLetter(mContext, mPrepayDetailModel.getCurrencyCode()) + " " + totalMoney);

                    LogUtils.i("提前還款金額----》" + totalMoney + "-------" + b2);

                } else {
                    include.setVisibility(View.GONE);
                }
            }
        });
        // 还款方式
        prepay_type_value.setListener(new SelectGridView.ClickListener() {
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < selectGridViewList.size(); i++) {
                    selectGridViewList.get(i).setSelected(false);
                    if (i == position) {
                        selectGridViewList.get(position).setSelected(true);
                    }
                }
                prepay_type_value.getAdapter().notifyDataSetChanged();
                if (position == 0) { // 部分
                    include.setVisibility(View.GONE);
                    prepay_input_money.setClearIconVisible(true);
                    prepay_input_money.getContentMoneyEditText().clearText();
                    prepay_input_money.getContentMoneyEditText().setClickable(true);
                    prepay_input_money.setMaxLeftNumber(11);
                    prepay_input_money.setMaxRightNumber(2);
                    prepay_input_money.setCurrency(mPrepayDetailModel.getCurrencyCode());
                    prepay_input_money.getContentMoneyEditText().setTextColor(Color.RED);
                    prepay_input_money.setMoneyEditTextShowLocation(true);
                } else if (position == 1) { // 全额
                    prepay_input_money.setmContentMoneyEditText(MoneyUtils
                            .transMoneyFormat(remainCapital, mPrepayDetailModel.getCurrencyCode()));
                    prepay_input_money.setClearIconVisible(false);
                    prepay_input_money.getContentMoneyEditText().setClickable(
                            false);
                    prepay_input_money.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
                    prepay_input_money.setMoneyEditTextShowLocation(false);
                }
            }
        });
        // 账户选择
        repaymentAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccoutFragment accoutFragment = new AccoutFragment();
                accoutFragment.setIsBocELoan(false, mPrepayDetailModel.getCurrencyCode());
                Bundle bundle = new Bundle();
                bundle.putString("type", "prepay");
                bundle.putString("commonType", "Nprepay");
                accoutFragment
                        .setAccountType(AccountType.SELECT_REPAYMENTACCOUNT);
                accoutFragment.setConversationId(conversationId);
                accoutFragment.setArguments(bundle);
                startForResult(accoutFragment, AccoutFragment.RequestCode);
            }
        });

        // 下一步
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonClickLock.isCanClick(click_more)) {
                    return;
                }
                judgeStrong();
            }
        });
    }

    private void judgeStrong() {
        //校验提前还款本金
        if (StringUtils.isEmptyOrNull(prepay_input_money.getContentMoney())) {
            showErrorDialog("请输入提前还款本金");
            return;
        }

        //校验	0
        if ("0".equalsIgnoreCase(prepay_input_money.getContentMoney())
                || "0.0".equalsIgnoreCase(prepay_input_money.getContentMoney())
                || ".".equalsIgnoreCase(prepay_input_money.getContentMoney())
                || ".0".equalsIgnoreCase(prepay_input_money.getContentMoney())
                || ".00".equalsIgnoreCase(prepay_input_money.getContentMoney())
                || "0.00".equalsIgnoreCase(prepay_input_money.getContentMoney())
                || "0.".equalsIgnoreCase(prepay_input_money.getContentMoney())) {

            showErrorDialog("提前还款本金应大于0");
            return;
        }

        //校验
        int n1 = compareBigDecimal(remainCapital, prepay_input_money.getContentMoney());
        LogUtils.i("cq--------还款本金不能大于未还本金----n1" + n1);
        if (n1 == 1) {
            showErrorDialog("还款本金不能大于未还本金");
            return;
        }
        if (!"1".equalsIgnoreCase(mPrepayDetailModel.getRemainIssue())) {// 剩余期数
            if ("F".equalsIgnoreCase(mPrepayDetailModel.getInterestType() + "") || "G".equalsIgnoreCase(mPrepayDetailModel.getInterestType() + "")) {
//                还款本金 + 应还利息 >= 本期应还金额//废弃
//            还款本金  >= 本期应还金额//废弃
                BigDecimal b1 = new BigDecimal(prepay_input_money.getContentMoney());
                BigDecimal b2 = new BigDecimal(mPrepayDetailModel.getThisIssueRepayInterest());
                b2 = b2.setScale(2, BigDecimal.ROUND_HALF_UP);
                String mtotalMoney = (b2.add(b1)).toString();//还款本金 + 应还利息 = 本息
//                String mtotalMoney = (b1).toString();
                mtotalMoney = MoneyUtils.transMoneyFormat(mtotalMoney, mPrepayDetailModel.getCurrencyCode());
                int mN2 = compareBigDecimal(mtotalMoney, MoneyUtils.transMoneyFormat(mPrepayDetailModel.getThisIssueRepayAmount(), mPrepayDetailModel.getCurrencyCode()));
               LogUtil.d("yx----mN2-->"+mN2);
                //   判断--》    还款本金 +利息 >= 本期应还金额
                if (mN2 == 1) {
//                    showErrorDialog("还款本金应大于等于本期应还金额");
                    showErrorDialog("还款金额应大于等于本期应还金额");
                    return;
                }
            }
        }


        //校验还款账户
        if ("请选择".equals(repaymentAccount.getChoiceTextContent())) {
            showErrorDialog("请选择还款账户");
            return;
        }

        //校验余额
        int n2 = compareBigDecimal(availableBalance + "", totalMoney);
        LogUtils.i("cq--------当前账户余额不足----n2" + n2);
        if (n2 == 1) {
            showErrorDialog("当前账户余额不足");
            return;
        }

        // 获取安全因子
        showLoadingDialog();
        getPresenter().getSecurityFactor();
    }

    //比较金额大小
    private int compareBigDecimal(String s1, String s2) {
        try {
            s1= s1.replaceAll(",","");
            s2 = s2.replaceAll(",","");
            BigDecimal b1 = new BigDecimal(s1);
            BigDecimal b2 = new BigDecimal(s2);
            LogUtils.i("cq--------b1----" + b1 + "----b2---" + b2);
            return b2.compareTo(b1);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().unsubscribe();
    }

    /**
     * 为选项卡组件分配点击事件，点击弹出对话框公共组件
     *
     * @param list Dialog内的列表数据
     * @param view 点击的选项卡组件对象
     */
    private void onClickItem(List<String> list, final EditChoiceWidget view) {

        // selectListDialog.setTitle("选择期限");
        selectListDialog.setListData(list);
        selectListDialog
                .setOnSelectListener(new SelectListDialog.OnSelectListener() {
                    @Override
                    public void onSelect(int position, Object model) {
                        if (null != model) {
                            if (position == 0) { // 全额
                                prepay_input_money.setmContentMoneyEditText(MoneyUtils
                                        .transMoneyFormat(remainCapital, mPrepayDetailModel.getCurrencyCode()));
                                prepay_input_money.setClearIconVisible(false);
                                prepay_input_money.getContentMoneyEditText().setClickable(
                                        false);
                            } else if (position == 1) { // 部分
                                include.setVisibility(View.GONE);
                                prepay_input_money.setClearIconVisible(true);
                                prepay_input_money.getContentMoneyEditText().clearText();
                                prepay_input_money.getContentMoneyEditText().setClickable(
                                        true);
                            }
                            view.setChoiceTextContent(model.toString());
                            selectListDialog.dismiss();
                        }
                    }
                });
        selectListDialog.show();
    }

    @Override
    public void obtainConversationSuccess(String conversationId) {
        Log.i(TAG, "获取会话成功！");
        this.conversationId = conversationId;
        getPresenter().setConversationId(conversationId);
        // prepayConfirmInfoFragment.setConversationId(conversationId);

        // 查询中行所有帐户列表
        List<String> list = new ArrayList<String>();
        list.add("119");//借记卡119
        list.add("101");// 普活101
        list.add("188");//活一本188
        getPresenter().queryAllChinaBankAccount(list);
    }

    @Override
    public void obtainConversationFail(ErrorException e) {
        Log.i(TAG, "获取会话失败！");
        closeProgressDialog();
    }

    @Override
    public void obtainAllChinaBankAccountSuccess(List<QueryAllChinaBankAccountRes> res) {
        LogUtils.d(TAG, "查询账户列表成功！");
        //是否进行可用余额接口调用标记 ： true调用  false 不调用
        boolean istrue = false;
        if (!PublicUtils.isEmpty(res) && mPrepayDetailModel != null) {
            for (QueryAllChinaBankAccountRes queryAllChinaBankAccountRes : res) {
                LogUtils.i(TAG, "------------>获取账户列表成功!之前的-model--->"
                        + mPrepayDetailModel.getLoanCycleRepayAccount());
                LogUtils.i(TAG, "------------>获取账户列表成功!之前的--mPrepayAccount-->"
                        + mPrepayAccount);
                LogUtils.i(TAG, "------------>获取账户列表成功!查询的---->"
                        + queryAllChinaBankAccountRes.getAccountNumber());
                if (mPrepayAccount.equals(
                        queryAllChinaBankAccountRes.getAccountNumber())) {
                    account = queryAllChinaBankAccountRes.getAccountNumber();
                    accountId = queryAllChinaBankAccountRes.getAccountId() + "";
//					repaymentAccount.setChoiceTextContent(NumberUtils.formatCardNumber(account));
                    //2016年10月10日 20:42:12  yx 修改
                    getPresenter().checkRepaymentAccount(queryAllChinaBankAccountRes);
                    LogUtils.i(TAG, "------------>获取账户列表成功!----->有匹配的");
                    istrue = true;
                    break;
                } else {
                    LogUtils.i(TAG, "------------>获取账户列表成功!---->没有匹配的");
                    repaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
                    istrue = false;
                }
            }
            if (!istrue) {
                closeProgressDialog();
            }
        } else {
            LogUtils.i(TAG, "------------>获取账户列表成功!------->为空");
            repaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
            closeProgressDialog();
        }
    }

    @Override
    public void obtainAllChinaBankAccountFail(ErrorException e) {
        LogUtils.d(TAG, "查询账户列表失败！");
        closeProgressDialog();
    }

    /**
     * 还款账户校验  成功回调
     *
     * @param res
     */
    @Override
    public void doRepaymentAccountCheckSuccess(RepaymentAccountCheckRes res) {
        if ("01".equals(res.getCheckResult().get(0))) {
            repaymentAccount.setChoiceTextContent(NumberUtils.formatCardNumber(account));
            if (!StringUtils.isEmptyOrNull(accountId)) {
                getPresenter().prepayCheckAccountDetail(accountId);
            }
        } else {
            closeProgressDialog();
            repaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
        }
    }

    /**
     * 还款账户校验  失败回调
     */
    @Override
    public void doRepaymentAccountCheckFail(ErrorException e) {
        closeProgressDialog();
        repaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
    }

    @Override
    public void obtainSecurityFactorSuccess(SecurityFactorModel result) {
        LogUtils.i(TAG, "-------->获取安全因子成功！");
        _combinId = securityVerity.getDefaultSecurityFactorId(result).getId();
        combinId_name = securityVerity.getDefaultSecurityFactorId(result).getName();
        LogUtils.i(TAG, "-------->获取安全因子成功！_combinId+combinId_name" + _combinId + combinId_name);
        // prepayConfirmInfoFragment.set_combinId(_combinId);

        req = new PrepayVerifyReq();
        req.setConversationId(conversationId);
        req.setLoanType(mPrepayDetailModel.getLoanType());
        req.setLoanAccount(mPrepayDetailModel.getAccountNumber());
        req.setCurrency(mPrepayDetailModel.getCurrencyCode());

        req.setRepayAmount(new BigDecimal(totalMoney.replaceAll(",","")));

        LogUtils.i(TAG, "预交移上传参数----------bigDecimal--->"
                + Double.parseDouble(MoneyUtils.getNormalMoneyFormat(totalMoney)));
        LogUtils.i(TAG, "预交移上传参数----------bigDecimalhkkgllk--->"
                + BigDecimal.valueOf(Double.parseDouble(MoneyUtils.getNormalMoneyFormat(totalMoney))));

        req.setFromAccountId(accountId);
        req.setAccountNumber(account);
        req.set_combinId(_combinId);
        LogUtils.i(TAG, "预交移上传参数----------req--->.toString()" + req.toString());
        getPresenter().setPrepayVerifyReq(req);
        getPresenter().prepayVerify();

    }

    @Override
    public void obtainSecurityFactorFail(ErrorException e) {
        Log.i(TAG, "-------->获取安全因子失败！");
        closeProgressDialog();
    }

    @Override
    public void prepayVerifySuccess(PrepayVerifyRes result) {
        Log.i(TAG, "提前还款预交易成功！");
        closeProgressDialog();
        // 音频key加密
        EShieldVerify.getInstance(getActivity()).setmPlainData(
                result.get_plainData());

        prepayConfirmInfoFragment = new PrepayConfirmInfoFragment();
        available = securityVerity.confirmFactor(result.getFactorList());
        prepayConfirmInfoFragment.setPrepayVerifyRes(result);
        prepayConfirmInfoFragment.setAvailable(available);

        prepayConfirmInfoFragment.setConversationId(conversationId);
        prepayConfirmInfoFragment.set_combinId(_combinId);
        prepayConfirmInfoFragment.set_combinId_Name(combinId_name);

        prepayConfirmInfoFragment.setPrepayVerifyReq(req);
        prepayConfirmInfoFragment.setmPrepayDetailModel(mPrepayDetailModel);

        start(prepayConfirmInfoFragment);
        LogUtils.i("cq------available------>" + available);
    }

    @Override
    public void prepayVerifyFail(ErrorException e) {
        Log.i(TAG, "提前还款预交易失败！");
        closeProgressDialog();
    }

    /**
     * 提前还款查询账户详情成功
     *
     * @param result
     */
    @Override
    public void prepayCheckAccountDetailSuccess(
            PrepayAccountDetailModel.AccountDetaiListBean result) {
        Log.i(TAG, "提前还款查询账户详情成功！");
        availableBalance = String.valueOf(result.getAvailableBalance());

//        availableAvl.setVisibility(View.VISIBLE);
//        availableAvl.setChoiceAutoContent("人民币元" + MoneyUtils.transMoneyFormat(availableBalance, "001"));
//        availableAvl.setChoiceTitleBold(true);
        LogUtil.d("yx-----未还本金--币种-" + mPrepayDetailModel.getCurrencyCode());
        LogUtil.d("yx-----首次--提前还款查询账户详情成功--币种-" + result.getCurrencyCode());
//        if((""+mPrepayDetailModel.getCurrencyCode()).equalsIgnoreCase(result.getCurrencyCode()+"")){
//            tv_availableAvl_value.setText(PublicCodeUtils.getCurrencyWithLetter(mContext,result.getCurrencyCode()+" "+
//                    MoneyUtils.transMoneyFormat(availableBalance, result.getCurrencyCode())));
//            ll_availableAvl.setVisibility(View.VISIBLE);
//        }
        if ((mCurrencyCOdeTest2.equalsIgnoreCase(transforCodeFormat(result.getCurrencyCode() + "")))){
            tv_availableAvl_value.setText(PublicCodeUtils.getCurrencyWithLetter(mContext, mCurrencyCOdeTest3)+ " " +
                    MoneyUtils.transMoneyFormat(availableBalance, result.getCurrencyCode()));
            ll_availableAvl.setVisibility(View.VISIBLE);
        }
//        tv_availableAvl_value.setText(PublicCodeUtils.getCurrencyWithLetter(mContext,result.getCurrencyCode()) + MoneyUtils.transMoneyFormat(availableBalance, result.getCurrencyCode()));
//
//        ll_availableAvl.setVisibility(View.VISIBLE);
        closeProgressDialog();
    }

    /**
     * 提前还款查询账户详情失败！
     *
     * @param e
     */
    @Override
    public void prepayCheckAccountDetailFail(ErrorException e) {
        Log.i(TAG, "提前还款查询账户详情失败！");
//        availableAvl.setVisibility(View.GONE);
        ll_availableAvl.setVisibility(View.GONE);
        closeProgressDialog();
    }

    @Override
    public void setPresenter(PrepayContract.Presenter presenter) {

    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == AccoutFragment.ResultCode) {
            if (data != null) {
                Log.i(TAG, "-------->account" + data.get("account").toString());
                account = data.get("account").toString();
                accountId = data.get("accountId").toString();
                availableBalance = data.get("availableBalance").toString();
                mcurrencyCode = data.get("currencyCode").toString();
                Log.i(TAG, "-------->availableBalance---------"
                        + availableBalance);
                LogUtil.d("------------mcurrencyCode--->" + mcurrencyCode);
                repaymentAccount.setChoiceTextContent(NumberUtils
                        .formatCardNumber(account));

                if (!TextUtils.isEmpty(availableBalance)) {
//                    availableAvl.setVisibility(View.VISIBLE);
//                    availableAvl.setChoiceAutoContent("人民币元" + MoneyUtils.transMoneyFormat(availableBalance, "001"));
//                    tv_availableAvl_value.setText("人民币元" + MoneyUtils.transMoneyFormat(availableBalance, mcurrencyCode));
//                    if (("" + PublicCodeUtils.getCurrencyCode(mContext, mPrepayDetailModel.getCurrencyCode())).equalsIgnoreCase(mcurrencyCode + "")) {
//                        tv_availableAvl_value.setText(PublicCodeUtils.getCurrencyWithLetter(mContext, mPrepayDetailModel.getCurrencyCode()) + " " +
//                                MoneyUtils.transMoneyFormat(availableBalance, mcurrencyCode));
//                        ll_availableAvl.setVisibility(View.VISIBLE);
//                    }
                    if ((mCurrencyCOdeTest2.equalsIgnoreCase(transforCodeFormat(mcurrencyCode + "")))){
                        tv_availableAvl_value.setText(PublicCodeUtils.getCurrencyWithLetter(mContext, mCurrencyCOdeTest3)+ " " +
                                MoneyUtils.transMoneyFormat(availableBalance,mcurrencyCode));
                        ll_availableAvl.setVisibility(View.VISIBLE);
                    }
                } else {
//                    availableAvl.setVisibility(View.GONE);
                    ll_availableAvl.setVisibility(View.GONE);
                    showErrorDialog("请更改提前还款账号");
                }
                // getPresenter().prepayCheckAccountDetail(accountId);
            }
        }

    }

    /**
     * 转换成 数字
     *
     * @param currencyCode
     * @return
     */
    private String transforCodeFormat(String currencyCode) {
        String mCode = "";
        if (!StringUtils.isEmptyOrNull(currencyCode)) {
            if (Pattern.compile("(?i)[A-Z]").matcher(currencyCode).find()) {
                mCode = PublicCodeUtils.getCurrencyCode(getContext(), currencyCode);
            } else if (Pattern.compile("(?i)[0-9]").matcher(currencyCode).find()) {
                mCode = currencyCode;
            } else {
                mCode = PublicCodeUtils.getCurrency(getContext(), currencyCode);
            }
        }
        return mCode;
    }

    /**
     * 转换成字母
     *
     * @param currencyCode
     * @return
     */
    private String transforCodeFormatLetter(String currencyCode) {
        String mCode = "";
        if (!StringUtils.isEmptyOrNull(currencyCode)) {
            if (Pattern.compile("(?i)[A-Z]").matcher(currencyCode).find()) {
                mCode = currencyCode;
            } else if (Pattern.compile("(?i)[0-9]").matcher(currencyCode).find()) {
                mCode = PublicCodeUtils.getCurrencyLetterCode(getContext(), currencyCode);
            }
        }
        return mCode;
    }

    @Override
    protected PrepayPresenter initPresenter() {
        return new PrepayPresenter(this);
    }
}
