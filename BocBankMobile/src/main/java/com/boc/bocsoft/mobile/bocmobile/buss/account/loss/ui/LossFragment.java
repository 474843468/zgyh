package com.boc.bocsoft.mobile.bocmobile.buss.account.loss.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.loss.model.LossViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.loss.presenter.LossPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * 挂失/冻结
 * Created by liuweidong on 2016/6/3.
 */
public class LossFragment extends MvpBussFragment<LossContract.Presenter> implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener, LossContract.LossBeforeView {
    private View rootView;// 根视图
    private EditChoiceWidget mChoiceAccount;// 账户列表选择组件
    private LinearLayout parentLayoutFreezeAccount;// 借记卡冻结账户的父布局
    private LinearLayout layoutContent;
    private TextView txtTitle;// 挂失有效期|冻结有效期|挂失类型
    private TextView txtLossInfo;// 挂失有效期|冻结有效期|信用卡费用地址说明
    private TextView txtFreezeInfo;// 借记卡冻结信息说明
    private SelectGridView mSelectSingleMore;// 单选多选组件
    private CheckBox checkbox;// 借记卡冻结账户开关
    private Button btnNext;// 下一步

    private AccountBean curAccountBean;// 当前选中的账户数据
    private static LossViewModel mLossViewModel;// 挂失与冻结页面数据
    private Content isSelectedItem;// 单选多选组件被选择的子项

    private boolean isSingleAccount = true;// 单账户或多账户标记
    private List<Content> selectGridViewList;// 期限或信用卡挂失类型的数据
    private ArrayList<String> accountTypeList;// 过滤的账户类型

    //借记卡挂失有效期或活期一本通冻结有效期
    private String[] lossPeriod = {"5日", "长期"};
    private String[] lossPeriodID = {LOSS_DAY_5, LOSS_DAY_FOREVER};
    //信用卡挂失类型
    private String[] lossType = {"挂失", "挂失及补卡"};
    private String[] lossTypeID = {LOSS_TYPE_NORMAL, LOSS_TYPE_REAPPLY_CARD};

    // 借记卡挂失有效期或活期一本通冻结有效期：5日
    public static final String LOSS_DAY_5 = "1";
    // 借记卡挂失有效期或活期一本通冻结有效期：长期
    public static final String LOSS_DAY_FOREVER = "2";
    // 信用卡挂失类型：挂失
    public static final String LOSS_TYPE_NORMAL = "0";
    // 信用卡挂失类型：挂失及补卡
    public static final String LOSS_TYPE_REAPPLY_CARD = "1";
    // 借记卡冻结
    public static final String LOSS_TYPE_YES = "Y";
    // 借记卡不冻结
    public static final String LOSS_TYPE_NO = "N";

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_account_loss, null);
        return rootView;
    }

    @Override
    public void initView() {
        mChoiceAccount = (EditChoiceWidget) rootView.findViewById(R.id.choice_account);
        layoutContent = (LinearLayout) rootView.findViewById(R.id.layout_content);
        txtTitle = (TextView) rootView.findViewById(R.id.txt_title);
        mSelectSingleMore = (SelectGridView) rootView.findViewById(R.id.select_single_more);
        txtLossInfo = (TextView) rootView.findViewById(R.id.txt_loss_info);
        parentLayoutFreezeAccount = (LinearLayout) rootView.findViewById(R.id.layout_freeze_account);
        checkbox = (CheckBox) rootView.findViewById(R.id.checkbox);
        txtFreezeInfo = (TextView) rootView.findViewById(R.id.txt_freeze_info);
        btnNext = (Button) rootView.findViewById(R.id.btn_next);
        mChoiceAccount.setNameWidth();
    }

    @Override
    public void initData() {
        if (mLossViewModel == null) {
            mLossViewModel = new LossViewModel();
        }
        selectGridViewList = new ArrayList<Content>();

        isSingleAccount = getArguments().getBoolean("isSingleAccount");
        checkbox.setOnCheckedChangeListener(LossFragment.this);// 默认设置借记卡冻结，该控件监听放到前面

        if (isSingleAccount) {// 单账户
            layoutContent.setVisibility(View.VISIBLE);
            mChoiceAccount.getChoiceWidgetArrowImageView().setVisibility(View.GONE);
            curAccountBean = getArguments().getParcelable("AccountBean");
            if (curAccountBean != null) {
                mChoiceAccount.setChoiceTextContent(NumberUtils.formatCardNumber(curAccountBean.getAccountNumber()));
                changeLayout();
                changeLayoutData();
            }
        } else {// 多账户
            layoutContent.setVisibility(View.GONE);
            mChoiceAccount.getChoiceWidgetArrowImageView().setVisibility(View.VISIBLE);
            mChoiceAccount.setChoiceTextContent(getResources().getString(R.string.boc_common_select));
            mChoiceAccount.setChoiceTextName(getResources().getString(R.string.boc_account_loss_name));
            curAccountBean = new AccountBean();
            filterAccountType();
        }
    }

    @Override
    public void setListener() {
        if (!isSingleAccount) {
            mChoiceAccount.setOnClickListener(LossFragment.this);
        }
        btnNext.setOnClickListener(LossFragment.this);
        // 挂失有效期或信用卡挂失类型的监听
        mSelectSingleMore.setListener(new SelectGridView.ClickListener() {
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {
                isSelectedItem = (Content) parent.getAdapter().getItem(position);
                changeLossInfo();
                if (ApplicationConst.ACC_TYPE_BRO.equals(curAccountBean.getAccountType())) {
                    if (checkbox.isChecked()) {
                        if (lossPeriodID[0].equals(mLossViewModel.getLossDays())) {// 5日
                            txtFreezeInfo.setText(getResources().getString(R.string.boc_account_freeze_period_5_info));
                        } else {// 长期
                            txtFreezeInfo.setText(getResources().getString(R.string.boc_account_freeze_period_forever_info_start)
                                    + getCurSystemDate().format(
                                    DateFormatters.dateFormatter1) + getResources().getString(R.string.boc_account_freeze_period_forever_info_end));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void reInit() {
        initData();
    }

    @Override
    protected LossContract.Presenter initPresenter() {
        return new LossPresenter(this);
    }

    /**
     * 过滤账户类型
     */
    private void filterAccountType() {
        accountTypeList = new ArrayList<String>();
        // 普通活期 101
        accountTypeList.add(ApplicationConst.ACC_TYPE_ORD);
        // 借记卡 119
        accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);
        // 定期一本通 170
        accountTypeList.add(ApplicationConst.ACC_TYPE_REG);
        // 活期一本通 188
        accountTypeList.add(ApplicationConst.ACC_TYPE_RAN);
        // 优汇通专户 199
        accountTypeList.add(ApplicationConst.ACC_TYPE_YOUHUITONG);
    }

    /**
     * 查询view层的数据
     *
     * @return
     */
    public static LossViewModel getViewModel() {
        return mLossViewModel;
    }

    /**
     * 依据卡类型切换布局
     */
    private void changeLayout() {
        mChoiceAccount.setChoiceTextName("挂失/冻结\n账户");
        switch (curAccountBean.getAccountType()) {
            case ApplicationConst.ACC_TYPE_BRO:// 借记卡
                txtTitle.setText(getResources().getString(R.string.boc_account_loss_period));
                parentLayoutFreezeAccount.setVisibility(View.VISIBLE);
                break;
            default:// 活期一本通（其他所有）
                txtTitle.setText(getResources().getString(R.string.boc_account_freeze_period));
                parentLayoutFreezeAccount.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 依据卡类型修改数据
     */
    private void changeLayoutData() {
        selectGridViewList.clear();
        switch (curAccountBean.getAccountType()) {
            case ApplicationConst.ACC_TYPE_ZHONGYIN:// 信用卡
            case ApplicationConst.ACC_TYPE_GRE:
            case ApplicationConst.ACC_TYPE_SINGLEWAIBI:
                for (int i = 0; i < lossType.length; i++) {
                    Content content = new Content();
                    content.setName(lossType[i]);
                    content.setContentNameID(lossTypeID[i]);
                    if (i == 0) {// 默认选中挂失
                        content.setSelected(true);
                        mLossViewModel.setSelectLossType(lossTypeID[0]);
                    }
                    selectGridViewList.add(content);
                }
                break;
            default:// 活期一本通或借记卡
                for (int i = 0; i < lossPeriod.length; i++) {
                    Content content = new Content();
                    content.setName(lossPeriod[i]);
                    content.setContentNameID(lossPeriodID[i]);
                    if (i == 0) {// 默认选中5天
                        content.setSelected(true);
                        mLossViewModel.setLossDays(lossPeriodID[0]);
                    }
                    selectGridViewList.add(content);
                }
                break;
        }
        mSelectSingleMore.setData(selectGridViewList);
        setDefaultLossInfo();
    }

    /**
     * 设置界面默认的提示信息
     */
    private void setDefaultLossInfo() {
        if (curAccountBean != null) {
            switch (curAccountBean.getAccountType()) {
                case ApplicationConst.ACC_TYPE_BRO:// 借记卡
                    checkbox.setChecked(true);// 默认冻结借记卡
                    txtLossInfo.setVisibility(View.VISIBLE);
                    txtLossInfo.setText(getResources().getString(R.string.boc_account_loss_period_5_info));
                    break;
                default:
                    txtLossInfo.setVisibility(View.VISIBLE);
                    txtLossInfo.setText(getResources().getString(R.string.boc_account_freeze_period_5_info));
                    break;
            }
        }
    }

    /**
     * 依据卡类型修改挂失信息的提示
     */
    private void changeLossInfo() {
        if (isSelectedItem.getSelected()) {
            switch (curAccountBean.getAccountType()) {
                case ApplicationConst.ACC_TYPE_GRE:// 信用卡
                    mLossViewModel.setSelectLossType(isSelectedItem.getContentNameID());// 存储被选中的ID
                    if (isSelectedItem.getName().equals(lossType[0])) {// 挂失
                        showLoadingDialog(false);
                        // 网络请求（挂失）
                        getPresenter().queryCreditLossFee(Integer.valueOf(curAccountBean.getAccountId()));
                        mLossViewModel.setSelectLossType(LOSS_TYPE_NORMAL);
                    } else if (isSelectedItem.getName().equals(lossType[1])) {// 挂失及补卡
                        showLoadingDialog(false);
                        getPresenter().queryCreditLossAddress(Integer.valueOf(curAccountBean.getAccountId()));
                        mLossViewModel.setSelectLossType(LOSS_TYPE_REAPPLY_CARD);
                    }
                    break;
                default:// 借记卡或活期一本通
                    mLossViewModel.setLossDays(isSelectedItem.getContentNameID());// 存储被选中的ID
                    txtLossInfo.setVisibility(View.VISIBLE);// 显示挂失信息栏
                    String temp = "";

                    if (isSelectedItem.getName().equals(lossPeriod[0])) {// 5天
                        if (curAccountBean.getAccountType().equals(ApplicationConst.ACC_TYPE_BRO)) {// 借记卡
                            temp = getResources().getString(R.string.boc_account_loss_period_5_info);
                        } else {
                            temp = getResources().getString(R.string.boc_account_freeze_period_5_info);
                        }
                        mLossViewModel.setLossDays(LOSS_DAY_5);
                    } else if (isSelectedItem.getName().equals(lossPeriod[1])) {// 长期
                        if (curAccountBean.getAccountType().equals(ApplicationConst.ACC_TYPE_BRO)) {// 借记卡
                            temp = getResources().
                                    getString(R.string.boc_account_loss_period_forever_info_start)
                                    + getCurSystemDate().format(DateFormatters.dateFormatter1) + getResources().
                                    getString(R.string.boc_account_loss_period_forever_info_end);
                        } else {
                            temp = getResources().
                                    getString(R.string.boc_account_freeze_period_forever_info_start)
                                    + getCurSystemDate().format(DateFormatters.dateFormatter1) + getResources().
                                    getString(R.string.boc_account_freeze_period_forever_info_end);
                        }
                        mLossViewModel.setLossDays(LOSS_DAY_FOREVER);
                    }
                    mLossViewModel.setTime(getCurSystemDate());
                    txtLossInfo.setText(temp);
                    break;
            }
        } else {
        }
    }

    /**
     * 点击下一步按钮
     */
    private void requestNextFragment() {
        if (curAccountBean == null) {
            return;
        }
        // 判断是否有被选中
        boolean hasSelected = false;
        for (Content item : selectGridViewList) {
            if (item.getSelected()) {
                hasSelected = true;
                break;
            }
        }
        if (hasSelected) {
            LossPresenter.randomID = "";
            showLoadingDialog(false);
            getPresenter().querySecurityFactor(curAccountBean.getAccountType());
        } else {
            switch (curAccountBean.getAccountType()) {
                case ApplicationConst.ACC_TYPE_BRO:// 借记卡
                    showErrorDialog(getResources().getString(R.string.boc_hint_debit_card));
                    break;
                case ApplicationConst.ACC_TYPE_ZHONGYIN:// 信用卡
                case ApplicationConst.ACC_TYPE_GRE:
                case ApplicationConst.ACC_TYPE_SINGLEWAIBI:
                    showErrorDialog(getResources().getString(R.string.boc_hint_credit_card));
                    break;
                default:// 活期一本通（其他）
                    showErrorDialog(getResources().getString(R.string.boc_hint_current_account));
                    break;
            }
        }
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_account_loss_title);
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // 借记卡冻结
        if (isChecked) {
            txtFreezeInfo.setVisibility(View.VISIBLE);
            mLossViewModel.setAccFlozenFlag(LOSS_TYPE_YES);
            if (lossPeriodID[0].equals(mLossViewModel.getLossDays())) {// 5日
                txtFreezeInfo.setText(getResources().getString(R.string.boc_account_freeze_period_5_info));
            } else {// 长期
                txtFreezeInfo.setText(getResources().getString(R.string.boc_account_freeze_period_forever_info_start)
                        + getCurSystemDate().format(DateFormatters.dateFormatter1) + getResources().getString(R.string.boc_account_freeze_period_forever_info_end));
            }
        } else {
            txtFreezeInfo.setVisibility(View.GONE);
            mLossViewModel.setAccFlozenFlag(LOSS_TYPE_NO);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.choice_account) {// 选择账户列表
            startForResult(SelectAccoutFragment.newInstance(accountTypeList), SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
        } else if (view.getId() == R.id.btn_next) {// 下一步
            if (mContext.getString(R.string.boc_common_select).equals(mChoiceAccount.getChoiceTextContent())) {
                showErrorDialog(mContext.getString(R.string.boc_select_account));
                return;
            }
            requestNextFragment();
        }
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode == SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT) {
            layoutContent.setVisibility(View.VISIBLE);
            AccountBean item = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
            mChoiceAccount.setChoiceTextContent(NumberUtils.formatCardNumber(item.getAccountNumber()));
            saveCurAccountInfo(item);
            changeLayout();
            changeLayoutData();
        }
    }

    /**
     * 保存当前账户信息
     */
    private void saveCurAccountInfo(AccountBean accountBean) {
        curAccountBean.setAccountId(accountBean.getAccountId());
        curAccountBean.setAccountNumber(accountBean.getAccountNumber());
        curAccountBean.setAccountType(accountBean.getAccountType());
    }

    /**
     * 查询当前系统日期
     *
     * @return
     */
    private LocalDateTime getCurSystemDate() {
        return ApplicationContext.getInstance().getCurrentSystemDate();
    }

    /**
     * 信用卡挂失费用失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void queryCreditLossFeeFail(BiiResultErrorException biiResultErrorException) {
//        closeProgressDialog();
//        txtLossInfo.setVisibility(View.GONE);
//        showErrorDialog(biiResultErrorException.getErrorMessage());
//        for (Content item : selectGridViewList) {
//            if (item.getSelected()) {
//                item.setSelected(false);
//                mSelectSingleMore.getAdapter().notifyDataSetChanged();
//                break;
//            }
//        }
    }

    /**
     * 信用卡挂失费用成功
     */
    @Override
    public void queryCreditLossFeeSuccess() {
//        closeProgressDialog();
//        txtLossInfo.setVisibility(View.VISIBLE);
//        txtLossInfo.setText(getResources().getString(R.string.boc_account_loss_type_1_money)
//                + "人民币 " + MoneyUtils.transMoneyFormat(mLossViewModel.getLossFee().toString(),
//                mLossViewModel.getLossFeeCurrency()));
    }

    @Override
    public void queryCreditLossAddressFail(BiiResultErrorException biiResultErrorException) {
//        closeProgressDialog();
//        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    @Override
    public void queryCreditLossAddressSuccess() {
//        closeProgressDialog();
//        txtLossInfo.setVisibility(View.VISIBLE);
//        String lossFee = getResources().getString(R.string.boc_account_loss_type_1_money)
//                + "人民币" + MoneyUtils.transMoneyFormat(mLossViewModel.getLossFee().toString(),
//                mLossViewModel.getLossFeeCurrency()) + getResources().getString(R.string.boc_account_loss_type_2_money)
//                + "人民币" + MoneyUtils.transMoneyFormat(mLossViewModel.getReportFee().toString(),
//                mLossViewModel.getReportFeeCurrency());
//        txtLossInfo.setText(getResources().getString(R.string.boc_account_loss_type_2_post_way)
//                + mLossViewModel.getMailAddressType() + "\n"
//                + getResources().getString(R.string.boc_account_loss_type_2_post_addr)
//                + mLossViewModel.getMailAddress() + "\n" + lossFee);
    }

    /**
     * 查询安全因子失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void querySecurityFactorFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 查询安全因子成功
     *
     * @param securityViewModel
     */
    @Override
    public void querySecurityFactorSuccess(SecurityViewModel securityViewModel) {
        closeProgressDialog();
        // 传递安全因子给组件
        CombinListBean defaultCombin = SecurityVerity.getInstance(getActivity()).
                getDefaultSecurityFactorId(new SecurityFactorModel(PublicUtils.copyOfSecurityCombin(securityViewModel)));
        // 跳到信息确认页面
        Bundle bundle = new Bundle();
        bundle.putParcelable("AccountBean", curAccountBean);
        bundle.putSerializable("LossViewModel", mLossViewModel);
        bundle.putString("DefaultCombinName", defaultCombin.getName());
        LossInfoConfirmFragment lossInfoConfirmFragment = new LossInfoConfirmFragment();
        lossInfoConfirmFragment.setArguments(bundle);
        start(lossInfoConfirmFragment);
    }
}
