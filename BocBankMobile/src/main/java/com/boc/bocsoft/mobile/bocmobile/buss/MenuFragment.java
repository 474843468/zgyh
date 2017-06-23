package com.boc.bocsoft.mobile.bocmobile.buss;

import android.os.Bundle;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.more.BaseMoreFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.ui.ActivityManagementPaltformHomePageFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Menu;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui.AccountManagementFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui.MarginManagementFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.mypurchase.ui.MyPurchaseProductFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.ui.PurchaseFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui.TransQueryFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.ui.ATMWithDrawQueryFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.ui.MobileRemitFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.ui.PayerFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui.OrderListFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.ui.RemitQueryFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.ui.MobileWithdrawFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.ui.WithdrawalQueryFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.ui.AccountMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.ui.InvestTreatyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui.FinancialPositionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.ui.RecommendFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui.RepealOrderFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui.TransInquireFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.data.WealthViewData;

import java.util.ArrayList;
import java.util.List;

/**
 * 转账汇款与中银理财更多菜单
 * Created by liuweidong on 2016/9/6.
 */
public class MenuFragment extends BaseMoreFragment {

    public static final String MENU = "Menu";// 往菜单页面的跳转
    public static final int ATM_WITH_DRAW = 0;// ATM无卡取款菜单
    public static final int MOBILE_REMIT = 1;// 我要汇款菜单
    public static final int MAKE_COLLECTION = 2;// 收付款管理、付款人管理
    public static final int WEALTH = 3;// 中银理财
    public static final int LONG_SHORT_FOREX = 4;// 双向宝
    public static final int LONG_SHORT_FOREX_ACCOUNT_MANAGE = 5;// 双向宝-账户管理

    @Override
    protected List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        switch (getArguments().getInt(MENU)) {
            case ATM_WITH_DRAW:
                items = getATMWithDrawMenu();
                break;
            case MOBILE_REMIT:
                items = getMobileRemitMenu();
                break;
            case MAKE_COLLECTION:
                items = getMakeCollectionMenu();
                break;
            case WEALTH:// 中银理财
                items = getWealthMenu();
                break;
            case LONG_SHORT_FOREX:// 双向宝
                items = getLongShortForexMenu();
                break;
            case LONG_SHORT_FOREX_ACCOUNT_MANAGE: // 双向宝-账户管理
                items = getLongShortForexAccountManageMenu();
                break;
            default:
                break;
        }
        return items;
    }

    /**
     * ATM无卡取款菜单
     *
     * @return
     */
    public List<Item> getATMWithDrawMenu() {
        List<Item> items = new ArrayList<>();

        Menu menu = ApplicationContext.getInstance().getMenu();
        //取款记录
        items.add(menu.findItemById(ModuleCode.MODULE_ATMWITHDRAWAL_0200));
        //手机取款
        items.add(menu.findItemById(ModuleCode.MODULE_PHONEPAYMENT_0000));
        return items;
    }

    /**
     * 汇往取款人菜单
     *
     * @return
     */
    public List<Item> getMobileRemitMenu() {
        List<Item> items = new ArrayList<>();

        Menu menu = ApplicationContext.getInstance().getMenu();
        //汇出查询
        items.add(menu.findItemById(ModuleCode.MODULE_PHONEPAYMENT_0100));
        if (ApplicationContext.getInstance().getUser().isMobileIsSignedAgent()) {// 签约代理点
            //代理点取款
            items.add(menu.findItemById(ModuleCode.MODULE_PHONEPAYMENT_0200));
            //取款查询
            items.add(menu.findItemById(ModuleCode.MODULE_PHONEPAYMENT_0300));
        }
        return items;
    }

    /**
     * 收付款管理、付款人管理
     *
     * @return
     */
    public List<Item> getMakeCollectionMenu() {
        List<Item> items = new ArrayList<>();

        Menu menu = ApplicationContext.getInstance().getMenu();
        //收付款管理
        items.add(menu.findItemById(ModuleCode.MODULE_PAYEE_0100));
        //付款人管理
        items.add(menu.findItemById(ModuleCode.MODULE_PAYEE_0200));
        return items;
    }

    /**
     * 中银理财菜单集
     *
     * @return
     */
    public List<Item> getWealthMenu() {
        List<Item> items = new ArrayList<>();

        Menu menu = ApplicationContext.getInstance().getMenu();
        // 我的持仓
        items.add(menu.findItemById(ModuleCode.MODULE_BOCINVT_0100));
        // 交易查询
        items.add(menu.findItemById(ModuleCode.MODULE_BOCINVT_0200));
        // 到期产品查询
//        items.add(menu.findItemById(ModuleCode.MODULE_BOCINVT_0300));
        // 撤单
        items.add(menu.findItemById(ModuleCode.MODULE_BOCINVT_0400));
        // 投资协议管理
        items.add(menu.findItemById(ModuleCode.MODULE_BOCINVT_0500));
        // 账户管理
        items.add(menu.findItemById(ModuleCode.MODULE_BOCINVT_0600));
        // 理财推荐
        items.add(menu.findItemById(ModuleCode.MODULE_BOCINVT_0700));
        return items;
    }

//    /**
//     * 双向宝菜单集
//     *
//     * @return
//     */
//    public List<Item> getLongShortMenu() {
//        List<Item> items = new ArrayList<>();
//        Menu menu = ApplicationContext.getInstance().getMenu();
//        // 双向宝-双向宝持仓
//        items.add(menu.findItemById(ModuleCode.MODULE_ISFOREXSTORAGECASH_0100));
//        // 双向宝-账户管理
//        items.add(menu.findItemById(ModuleCode.MODULE_ISFOREXSTORAGECASH_0200));
//        // 双向宝-委托交易查询
//        items.add(menu.findItemById(ModuleCode.MODULE_ISFOREXSTORAGECASH_0300));
//        // 双向宝-交易记录
//        items.add(menu.findItemById(ModuleCode.MODULE_ISFOREXSTORAGECASH_0400));
//        // 双向宝-保证金存入/转出
//        items.add(menu.findItemById(ModuleCode.MODULE_ISFOREXSTORAGECASH_0500));
//        // 双向宝-帮助
//        items.add(menu.findItemById(ModuleCode.MODULE_ISFOREXSTORAGECASH_0600));
//        return items;
//    }

    /**
     * 中银理财需要跳转的模块
     *
     * @param moduleId
     * @return
     */
    private BussFragment needsFragment(String moduleId) {
        BussFragment bussFragment = null;
        switch (moduleId) {
            case ModuleCode.MODULE_BOCINVT_0100:// 我的持仓
                bussFragment = new FinancialPositionFragment();
                break;
            case ModuleCode.MODULE_BOCINVT_0200:// 交易查询
                bussFragment = new TransInquireFragment();
                break;
            case ModuleCode.MODULE_BOCINVT_0400:// 撤单
                bussFragment = new RepealOrderFragment();
                break;
            case ModuleCode.MODULE_BOCINVT_0500:// 投资协议管理
                bussFragment = new InvestTreatyFragment();
                break;
            case ModuleCode.MODULE_BOCINVT_0600:// 账户管理
                bussFragment = new AccountMainFragment(MenuFragment.class);
                break;
            case ModuleCode.MODULE_BOCINVT_0700:// 理财推荐
                bussFragment = new RecommendFragment();
                break;
            default:
                break;
        }
        return bussFragment;
    }

    /**
     * 中银理财模块的跳转
     *
     * @param moduleId
     */
    private void startWealthFragment(String moduleId) {
        Menu menu = ApplicationContext.getInstance().getMenu();
        Item item = menu.findItemById(moduleId);
        if ("1".equals(item.getLogin())) {// 需要登录
            if (ApplicationContext.getInstance().isLogin()) {
                WealthProductFragment.getInstance().judgeToFragment(WealthViewData.needsStatus(moduleId), needsFragment(moduleId));
            } else {
                ModuleActivityDispatcher.startToLogin(getActivity(), new LoginCallbackImpl(moduleId));
            }
        } else {

        }
    }

    /**
     * 双向宝账户管理模块的调整
     *
     * @param moduleId
     */
    private void startLongShortAccountManageModel(String moduleId) {
        switch (moduleId) {
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0210: // 账户管理-新增保证金账户
//                start(new AddNewBailFragment());
                ToastUtils.show("新增保证金账户");
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0220: // 账户管理-变更交易账户
                ToastUtils.show("变更交易账户");
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(String id) {
        switch (getArguments().getInt("Menu")) {
            case ATM_WITH_DRAW:// ATM无卡取款菜单
            case MOBILE_REMIT:// 我要汇款菜单
            case MAKE_COLLECTION:// 收付款管理、付款人管理
                transferClickListener(id);
                break;
            case WEALTH:// 中银理财
//                ModuleActivityDispatcher.dispatch(mActivity, id);
                startWealthFragment(id);
                break;
            case LONG_SHORT_FOREX:
                startLongShortForexFragment(id);
            break;
            case LONG_SHORT_FOREX_ACCOUNT_MANAGE:
                startLongShortAccountManageModel(id);
                break;
            default:
                break;
        }
    }

    class LoginCallbackImpl implements LoginCallback {
        private String id;

        public LoginCallbackImpl(String id) {
            this.id = id;
        }

        @Override
        public void success() {
            WealthProductFragment.getInstance().setRequestStatus(false);
            WealthProductFragment.getInstance().judgeToFragment(WealthViewData.needsStatus(id), needsFragment(id));
        }
    }

    /**
     * 转账汇款菜单监听
     *
     * @param id
     */
    private void transferClickListener(String id) {
        switch (id) {
            case ModuleCode.MODULE_ATMWITHDRAWAL_0200://取款记录
                start(new ATMWithDrawQueryFragment());
                break;
            case ModuleCode.MODULE_PHONEPAYMENT_0000://手机取款
                start(new MobileRemitFragment());
                break;
            case ModuleCode.MODULE_PHONEPAYMENT_0100://汇出查询
                start(new RemitQueryFragment());
                break;
            case ModuleCode.MODULE_PHONEPAYMENT_0200://代理点取款
                start(new MobileWithdrawFragment());
                break;
            case ModuleCode.MODULE_PHONEPAYMENT_0300://取款查询
                start(new WithdrawalQueryFragment());
                break;
            case ModuleCode.MODULE_PAYEE_0100://收付款管理
                start(new OrderListFragment());
                break;
            case ModuleCode.MODULE_PAYEE_0200://付款人管理
                start(new PayerFragment());
                break;
            default:
                break;
        }
    }

    @Override
    protected void titleLeftIconClick() {
        if(WEALTH == getArguments().getInt(MENU)){
            popToAndReInit(WealthProductFragment.class);
        } else {
            super.titleLeftIconClick();
        }
    }

    @Override
    public boolean onBack() {
        if (WEALTH == getArguments().getInt(MENU)) {
            popToAndReInit(WealthProductFragment.class);
            return false;
        }
        return true;
    }
    /**
     * 双向宝-更多菜单-功能
     *
     * @param moduleId
     * @date 2016-11-28 15:16:09
     * @author yx
     */
    private void startLongShortForexFragment(String moduleId) {
//        Menu menu = ApplicationContext.getInstance().getMenu();
//        Item item = menu.findItemById(moduleId);
//        if ("1".equals(item.getLogin())) {// 需要登录
//            if (ApplicationContext.getInstance().isLogin()) {
//                LongShortForexHomeFragment.getInstance().judgeToFragment(LongShortForexHomeCodeModelUtil.needsLongShortForexStatus(moduleId), needsLongShortForexFragment(moduleId));
//            } else {
//                ModuleActivityDispatcher.startToLogin(getActivity(), new LoginCallbackImpl(moduleId));
//            }
//        } else {
//
//        }
        switch (moduleId) {
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0100: // 我的持仓
                start(new MyPurchaseProductFragment());
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0200: // 账户管理
                start(new AccountManagementFragment());
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0300://委托交易查询
                TransQueryFragment transQueryFragment = new TransQueryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fromWhere", "menu");
                transQueryFragment.setArguments(bundle);
                start(transQueryFragment);
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0400: // 交易记录
                start(new TransQueryFragment());
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0500: // 保证金存入/转出
                MarginManagementFragment.newInstance(mActivity,MenuFragment.class);
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0600://双向宝-帮助
                start(new ActivityManagementPaltformHomePageFragment());
                break;
            case "testBuy"://双向宝-购买
                start(new PurchaseFragment());
                break;
            default:
                break;
        }

    }

    /**
     * 双向宝菜单集
     *
     * @return
     */
    public List<Item> getLongShortForexMenu() {
        List<Item> items = new ArrayList<>();

        Menu menu = ApplicationContext.getInstance().getMenu();
        // 双向宝-双向宝持仓
        items.add(menu.findItemById(ModuleCode.MODULE_ISFOREXSTORAGECASH_0100));
        //  双向宝-账户管理
        items.add(menu.findItemById(ModuleCode.MODULE_ISFOREXSTORAGECASH_0200));
        //双向宝-委托交易查询
        items.add(menu.findItemById(ModuleCode.MODULE_ISFOREXSTORAGECASH_0300));
        // 双向宝-交易记录
        items.add(menu.findItemById(ModuleCode.MODULE_ISFOREXSTORAGECASH_0300));
        //双向宝-保证金存入/转出
        items.add(menu.findItemById(ModuleCode.MODULE_ISFOREXSTORAGECASH_0500));
        //  双向宝-帮助
        items.add(menu.findItemById(ModuleCode.MODULE_ISFOREXSTORAGECASH_0600));
        //  双向宝-帮助
        items.add(new Item("临时购买","testBuy"));
        return items;
    }

    /**
     * 双向宝菜单集
     *
     * @return
     */
    public List<Item> getLongShortForexAccountManageMenu() {
        List<Item> items = new ArrayList<>();

        Menu menu = ApplicationContext.getInstance().getMenu();
        //  双向宝-账户管理-新增保证金账户
        items.add(menu.findItemById(ModuleCode.MODULE_ISFOREXSTORAGECASH_0210));
        //  双向宝-账户管理-变更交易账户
        items.add(menu.findItemById(ModuleCode.MODULE_ISFOREXSTORAGECASH_0220));
        //  双向宝-账户管理-交易账户余额查询
        items.add(menu.findItemById(ModuleCode.MODULE_ISFOREXSTORAGECASH_0230));
        return items;
    }
    /**
     * 双向宝需要跳转的模块
     *
     * @param moduleId
     * @return
     */
    private BussFragment needsLongShortForexFragment(String moduleId) {
        BussFragment bussFragment = null;
        switch (moduleId) {
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0100://双向宝-双向宝持仓
                bussFragment = new MyPurchaseProductFragment();
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0200://  双向宝-账户管理
                bussFragment = new AccountManagementFragment();
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0300:// 双向宝-委托交易查询
                bussFragment = new TransQueryFragment();
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0400:// 双向宝-交易记录
                bussFragment = new TransQueryFragment();
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0500:// 双向宝-保证金存入/转出
                bussFragment = new MarginManagementFragment();
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0600://双向宝-帮助
                break;
            default:
                break;
        }
        return bussFragment;
    }


}
