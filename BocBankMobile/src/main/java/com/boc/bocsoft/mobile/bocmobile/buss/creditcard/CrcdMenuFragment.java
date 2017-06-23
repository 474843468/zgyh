package com.boc.bocsoft.mobile.bocmobile.buss.creditcard;

import android.content.Intent;
import android.os.Bundle;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.WebUrl;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.ClipBoardWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.more.BaseMoreFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.more.MoreItem;
import com.boc.bocsoft.mobile.bocmobile.buss.account.relation.ui.AccountRelationCancelFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.overview.VirtualCardListFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.ui.AttCardMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.ui.AutoCrcdPaymentMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.ui.BillInstallmentsMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.ui.CashInstallmentFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.ui.CashNoAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.ui.ConsumeInstallmentMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.model.CrcdSettingsInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.presenter.CrcdPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.ui.CrcdContract;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.ui.InstallmentSelectAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.utils.CrcdUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.webplugin.model.AccountInfo;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 信用卡更多菜单
 * Created by wangf on 2016-12-2
 */
public class CrcdMenuFragment extends BaseMoreFragment<CrcdPresenter> implements CrcdContract.MenuView {

    public static final String MENU = "Menu";// 往菜单页面的跳转
    public static final String MENU_ACC = "AccountBean";// 账户信息
    public static final int CREDIT_MORE_ACCOUNT = 0;// 信用卡账户更多
    public static final int CREDIT_MORE_CARD = 1;// 信用卡卡片更多
    private static int currentMenu;// 当前的Menu
    private AccountBean accountBean;
    private List<AccountBean> mAccountList = new ArrayList<>();// 过滤的所有信用卡
    private CrcdSettingsInfoBean mCrcdSetInfoBean;

    /**
     * 信用卡账户更多
     */
    // 现金分期（提款）
    public static final String MODULE_CREDIT_ACCOUNT_0100 = "credit_account_0100";
    // 账单分期
    public static final String MODULE_CREDIT_ACCOUNT_0200 = "credit_account_0200";
    // 消费分期
    public static final String MODULE_CREDIT_ACCOUNT_0300 = "credit_account_0300";
    // 分期记录
    public static final String MODULE_CREDIT_ACCOUNT_0400 = "credit_account_0400";
    // 附属卡管理
    public static final String MODULE_CREDIT_ACCOUNT_0500 = "credit_account_0500";
    // 虚拟银行卡服务
    public static final String MODULE_CREDIT_ACCOUNT_0600 = "credit_account_0600";
    // 查询密码设置
    public static final String MODULE_CREDIT_ACCOUNT_0700 = "credit_account_0700";
    // 申请信用卡/进度查询
    public static final String MODULE_CREDIT_ACCOUNT_0800 = "credit_account_0800";
    // 激活信用卡
    public static final String MODULE_CREDIT_ACCOUNT_0900 = "credit_account_0900";

    /**
     * 信用卡卡片更多
     */
    // 挂失/补卡
    public static final String MODULE_CREDIT_CARD_0200 = "credit_card_0200";
    // 显示完整卡号
    public static final String MODULE_CREDIT_CARD_0300 = "credit_card_0300";
    // 自动还款
    public static final String MODULE_CREDIT_CARD_0400 = "credit_card_0400";
    // 全球交易人民币记账
    public static final String MODULE_CREDIT_CARD_0500 = "credit_card_0500";
    // 外币账单自动购汇
    public static final String MODULE_CREDIT_CARD_0600 = "credit_card_0600";
    // 对账单设置
    public static final String MODULE_CREDIT_CARD_0700 = "credit_card_0700";
    // 交易密码设置
    public static final String MODULE_CREDIT_CARD_0800 = "credit_card_0800";
    // POS消费功能设置
    public static final String MODULE_CREDIT_CARD_0900 = "credit_card_0900";
    // 3D安全认证
    public static final String MODULE_CREDIT_CARD_1000 = "credit_card_1000";
    // 取消关联
    public static final String MODULE_CREDIT_CARD_1100 = "credit_card_1100";


//    @Override
//    public void reInit() {
//        if (currentMenu == CREDIT_MORE_CARD) {
//            setContentById(MODULE_CREDIT_CARD_0400, "");
//            setContentById(MODULE_CREDIT_CARD_0500, "");
//            setContentById(MODULE_CREDIT_CARD_0600, "");
//            setContentById(MODULE_CREDIT_CARD_1000, "");
//            getPresenter().querySettingsInfo(accountBean.getAccountId());
//            getPresenter().query3DCertifInfo(accountBean.getAccountId());
//        }
//    }

    @Override
    protected List<MoreItem> getItems() {
        currentMenu = getArguments().getInt(MENU);
        List<MoreItem> items = new ArrayList<>();
        switch (currentMenu) {
            case CREDIT_MORE_ACCOUNT:// 信用卡账户更多
                items = getCreditAccountMenu();
                break;
            case CREDIT_MORE_CARD:// 信用卡卡片更多
                items = getCreditCardMenu();
                break;
            default:
                break;
        }
        return items;
    }


    @Override
    public void onClick(String id) {
        switch (currentMenu) {
            case CREDIT_MORE_ACCOUNT:// 信用卡账户更多
                startAccountMoreFragment(id);
                break;
            case CREDIT_MORE_CARD:// 信用卡卡片更多
                startCardMoreFragment(id);
                break;
            default:
                break;
        }
    }


    @Override
    protected void loadData() {
        mAccountList = ApplicationContext.getInstance().getChinaBankAccountList(CrcdUtil.filterAccountType());
        if (currentMenu == CREDIT_MORE_CARD) {// 详情页更多
            accountBean = getArguments().getParcelable(MENU_ACC);
            getPresenter().querySettingsInfo(accountBean.getAccountId());
            getPresenter().query3DCertifInfo(accountBean.getAccountId());
        }
    }

    /**
     * 信用卡首页更多菜单添加
     *
     * @return
     */
    public List<MoreItem> getCreditAccountMenu() {
        List<MoreItem> items = new ArrayList<>();
        // 现金分期（提款）
        items.add(new MoreItem(getString(R.string.boc_crcd_acc_more_cash), CrcdMenuFragment.MODULE_CREDIT_ACCOUNT_0100));
        // 账单分期
        items.add(new MoreItem(getString(R.string.boc_crcd_acc_more_bill_part), CrcdMenuFragment.MODULE_CREDIT_ACCOUNT_0200));
        // 消费分期
        items.add(new MoreItem(getString(R.string.boc_crcd_acc_more_consume_part), CrcdMenuFragment.MODULE_CREDIT_ACCOUNT_0300));
        // 分期记录
        items.add(new MoreItem(getString(R.string.boc_crcd_acc_more_installments), CrcdMenuFragment.MODULE_CREDIT_ACCOUNT_0400));
        // 附属卡管理
        items.add(new MoreItem(getString(R.string.boc_crcd_acc_more_attach), CrcdMenuFragment.MODULE_CREDIT_ACCOUNT_0500, true));
        // 虚拟银行卡服务
        items.add(new MoreItem(getString(R.string.boc_crcd_acc_more_virtual), CrcdMenuFragment.MODULE_CREDIT_ACCOUNT_0600));
        // 查询密码设置
        items.add(new MoreItem(getString(R.string.boc_crcd_acc_more_pwd), CrcdMenuFragment.MODULE_CREDIT_ACCOUNT_0700));
        // 申请信用卡/进度查询
        items.add(new MoreItem(getString(R.string.boc_crcd_acc_more_apply), CrcdMenuFragment.MODULE_CREDIT_ACCOUNT_0800, true));
        // 激活信用卡
        items.add(new MoreItem(getString(R.string.boc_crcd_acc_more_activation), CrcdMenuFragment.MODULE_CREDIT_ACCOUNT_0900));

        return items;
    }


    /**
     * 信用卡详情更多菜单添加
     *
     * @return
     */
    public List<MoreItem> getCreditCardMenu() {
        List<MoreItem> items = new ArrayList<>();
        // 挂失/补卡
        items.add(new MoreItem(getString(R.string.boc_crcd_card_more_loss), CrcdMenuFragment.MODULE_CREDIT_CARD_0200));
        // 显示完整卡号
        items.add(new MoreItem(getString(R.string.boc_crcd_card_more_complete), CrcdMenuFragment.MODULE_CREDIT_CARD_0300));
        // 自动还款
        items.add(new MoreItem(getString(R.string.boc_crcd_card_more_auto), CrcdMenuFragment.MODULE_CREDIT_CARD_0400, true));
        // 全球交易人民币记账
        items.add(new MoreItem(getString(R.string.boc_crcd_card_more_global), CrcdMenuFragment.MODULE_CREDIT_CARD_0500));
        // 外币账单自动购汇
        items.add(new MoreItem(getString(R.string.boc_crcd_card_more_foreign), CrcdMenuFragment.MODULE_CREDIT_CARD_0600));
        // 对账单设置
        items.add(new MoreItem(getString(R.string.boc_crcd_card_more_statement), CrcdMenuFragment.MODULE_CREDIT_CARD_0700));
        // 交易密码设置
        items.add(new MoreItem(getString(R.string.boc_crcd_card_more_pwd), CrcdMenuFragment.MODULE_CREDIT_CARD_0800));
        // POS消费功能设置
        items.add(new MoreItem(getString(R.string.boc_crcd_card_more_pos), CrcdMenuFragment.MODULE_CREDIT_CARD_0900));
        // 3D安全认证
        items.add(new MoreItem(getString(R.string.boc_crcd_card_more_3d), CrcdMenuFragment.MODULE_CREDIT_CARD_1000));
        // 取消关联
        items.add(new MoreItem(getString(R.string.boc_crcd_card_more_connect), CrcdMenuFragment.MODULE_CREDIT_CARD_1100, true));

        return items;
    }

    /**
     * 信用卡首页更多跳转
     *
     * @param id
     */
    private void startAccountMoreFragment(String id) {
        switch (id) {
            case MODULE_CREDIT_ACCOUNT_0100:// 现金分期（提款）
                List<String> accountType = new ArrayList<>();
                accountType.add(ApplicationConst.ACC_TYPE_BRO);
                List<AccountBean> accountList = ApplicationContext.getInstance().getChinaBankAccountList(accountType);
                if (accountList.size() == 0) {
                    start(new CashNoAccountFragment());
                } else {
                    start(new CashInstallmentFragment());
                }
                break;
            case MODULE_CREDIT_ACCOUNT_0200:// 账单分期
                start(new BillInstallmentsMainFragment());
                break;
            case MODULE_CREDIT_ACCOUNT_0300:// 消费分期
                start(new ConsumeInstallmentMainFragment());
                break;
            case MODULE_CREDIT_ACCOUNT_0400:// 分期记录
                start(InstallmentSelectAccountFragment.newInstance_(CrcdUtil.filterAccountType()));
                break;
            case MODULE_CREDIT_ACCOUNT_0500:// 附属卡管理
                start(new AttCardMainFragment());
                break;
            case MODULE_CREDIT_ACCOUNT_0600:// 虚拟银行卡服务
                start(new VirtualCardListFragment());
                break;
            case MODULE_CREDIT_ACCOUNT_0700:// 查询密码设置
                startH5Activity(WebUrl.CRCD_SET_QUERY_PASSWORD);
                break;
            case MODULE_CREDIT_ACCOUNT_0800:// 申请信用卡/进度查询
                ModuleActivityDispatcher.dispatch(mActivity, ModuleCode.MODULE_EAPPLY_CREDIT_CARD_0000);//申请信用卡
                break;
            case MODULE_CREDIT_ACCOUNT_0900:// 激活信用卡
                startH5Activity(WebUrl.CRCD_ACTIVE);
                break;
            default:
                break;
        }
    }

    /**
     * 信用卡详情更多跳转
     *
     * @param id
     */
    private void startCardMoreFragment(String id) {
        switch (id) {
            case MODULE_CREDIT_CARD_0200:// 挂失/补卡
                startH5Activity(WebUrl.CRCD_LOSS);
                break;
            case MODULE_CREDIT_CARD_0300:// 显示完整卡号
                String cardNum = NumberUtils.formatCardNumber2(accountBean.getAccountNumber());
                new ClipBoardWidget(mContext, cardNum).show();
                break;
            case MODULE_CREDIT_CARD_0400:// 自动还款
                Bundle bundle = new Bundle();
                bundle.putString(AutoCrcdPaymentMainFragment.CRCD_AUTOPAY_ACCOUNT_ID, accountBean.getAccountId());
                bundle.putString(AutoCrcdPaymentMainFragment.CRCD_AUTOPAY_ACCOUNT_NO, accountBean.getAccountNumber());
                AutoCrcdPaymentMainFragment fragment = new AutoCrcdPaymentMainFragment();
                fragment.setArguments(bundle);
                start(fragment);
                break;
            case MODULE_CREDIT_CARD_0500:// 全球交易人民币记账
                startH5Activity(WebUrl.CRCD_RMB_RECORD);
                break;
            case MODULE_CREDIT_CARD_0600:// 外币账单自动购汇
                startH5Activity(WebUrl.CRCD_BILL_AUTO_PURCHASE);
                break;
            case MODULE_CREDIT_CARD_0700:// 对账单设置
                startH5Activity(WebUrl.CRCD_SET_BILL);
                break;
            case MODULE_CREDIT_CARD_0800:// 交易密码设置
                startH5Activity(WebUrl.CRCD_SET_TRANSACTION_PASSWORD);
                break;
            case MODULE_CREDIT_CARD_0900:// POS消费功能设置
                startH5Activity(WebUrl.CRCD_SET_POS_MSG);
                break;
            case MODULE_CREDIT_CARD_1000:// 3D安全认证
                startH5Activity(WebUrl.CRCD_3D_SECURITY);
                break;
            case MODULE_CREDIT_CARD_1100:// 取消关联
                AccountRelationCancelFragment accountCancelFragment = new AccountRelationCancelFragment(accountBean);
                start(accountCancelFragment);
                break;
            default:
                break;
        }
    }

    @Override
    protected CrcdPresenter initPresenter() {
        return new CrcdPresenter(this);
    }

    /**
     * 信用卡设置类信息查询 成功
     */
    @Override
    public void querySettingsInfoSuccess(CrcdSettingsInfoBean settingsInfoBean) {
        mCrcdSetInfoBean = settingsInfoBean;
        // 自动还款状态 - 默认返回人民币账户的自动还款状态 1-自动还款  0-非自动还款
        if ("1".equals(settingsInfoBean.getPaymentStatus())) {
            setContentById(MODULE_CREDIT_CARD_0400, getString(R.string.boc_crcd_card_more_open), R.color.boc_text_color_green);
        } else {
            setContentById(MODULE_CREDIT_CARD_0400, getString(R.string.boc_crcd_card_more_close), R.color.boc_text_color_red);
        }
        //全球交易人民币记账标识 - 外币账户才会有“ADTE”表示已设定 其他表示未设定
        if ("ADTE".equals(settingsInfoBean.getChargeFlag())) {
            setContentById(MODULE_CREDIT_CARD_0500, getString(R.string.boc_crcd_card_more_open), R.color.boc_text_color_green);
        } else {
            setContentById(MODULE_CREDIT_CARD_0500, getString(R.string.boc_crcd_card_more_close), R.color.boc_text_color_red);
        }
        //外币账单自动购汇设置状态 - “0”未设定 “1”已设定
        if ("1".equals(settingsInfoBean.getForeignPayOffStatus())) {
            setContentById(MODULE_CREDIT_CARD_0600, getString(R.string.boc_crcd_card_more_open), R.color.boc_text_color_green);
        } else {
            setContentById(MODULE_CREDIT_CARD_0600, getString(R.string.boc_crcd_card_more_close), R.color.boc_text_color_red);
        }
    }

    /**
     * 3D安全认证信息查询 成功
     */
    @Override
    public void query3DCertifInfoSuccess(String openFlag) {
        // 3D安全认证开通标识 - “1”：开通 “0”：未开通
        if ("1".equals(openFlag))
            setContentById(MODULE_CREDIT_CARD_1000, getString(R.string.boc_crcd_card_more_bind), R.color.boc_text_color_green);
        else
            setContentById(MODULE_CREDIT_CARD_1000, getString(R.string.boc_crcd_card_more_unbind), R.color.boc_text_color_red);
    }

    /**
     * H5页面跳转
     */
    private void startH5Activity(String url) {
        Intent intent = new Intent();
        intent.setClass(mActivity, CreditCordovaActivity.class);
        intent.putExtra(CreditCordovaActivity.PARAM_TARGETURL, url);
        if (CREDIT_MORE_CARD == currentMenu) {// 详情更多
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setAccountId(accountBean.getAccountId());
            accountInfo.setCreditCardNum(accountBean.getAccountNumber());
            accountInfo.setUserName(ApplicationContext.getInstance().getUser().getCustomerName());
            intent.putExtra(CreditCordovaActivity.PARAM_ACCOUNT_INFO, accountInfo);// 传递账户信息
            if (WebUrl.CRCD_RMB_RECORD.equals(url)) {// 全球交易人民币记账
                intent.putExtra(CreditCordovaActivity.PARAM_OPENFLAG, "");
            }
            if (WebUrl.CRCD_BILL_AUTO_PURCHASE.equals(url)) {// 外币账单自动购汇
                intent.putExtra(CreditCordovaActivity.PARAM_FOREIGNPAYOFFSTATUS, mCrcdSetInfoBean.getForeignPayOffStatus());
            }
            if (WebUrl.CRCD_3D_SECURITY.equals(url)) {// 3D安全认证
                intent.putExtra(CreditCordovaActivity.PARAM_CERTIFINFO, "");
            }
        } else if (CREDIT_MORE_ACCOUNT == currentMenu) {// 首页更多
            AccountInfo accountInfo = new AccountInfo();
            AccountBean accountBean = mAccountList.get(0);
            accountInfo.setAccountId(accountBean.getAccountId());
            accountInfo.setCreditCardNum(accountBean.getAccountNumber());
            accountInfo.setUserName(ApplicationContext.getInstance().getUser().getCustomerName());
            intent.putExtra(CreditCordovaActivity.PARAM_ACCOUNT_INFO, accountInfo);// 传递账户信息
        }
        startActivity(intent);
    }

}
