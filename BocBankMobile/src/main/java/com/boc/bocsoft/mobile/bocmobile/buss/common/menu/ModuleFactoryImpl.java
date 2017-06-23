package com.boc.bocsoft.mobile.bocmobile.buss.common.menu;

import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.ModuleFactory;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.ui.ApplyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.loss.ui.LossFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.ui.AccSmsNotifyHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.CurrentFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.OverviewFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.ui.TransDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.ui.ActivityManagementPaltformHomePageFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.home.BuyAndSellExchangeHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.CreditCardHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.EApplyCreditCardFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.easybuss.ui.EasyBussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardlist.HceCardListFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui.FundProductHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanManagerFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.ui.LongShortForexHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.ui.QRPayScanFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.ui.ConsumeFinanceFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.OverseasHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.system.mine.ui.MinePayMenusFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.system.mine.ui.SecuritySettingFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.ui.ATMWithDrawFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui.MeMakeCollectionFragment1;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.ui.MobileRemitFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.PayeeManageFragment2;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.ui.PhoneEditPageFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.ui.PreRecordFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui.QrcodeMeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui.QrcodeTransFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.ui.TransferRecordFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransRemitBlankFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.ui.AccountMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.ui.InvestTreatyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui.FinancialPositionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.ui.RecommendFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui.RepealOrderFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui.TransInquireFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import java.util.HashMap;

/**
 * 模块跳转
 * Created by lxw on 2016/6/4.
 */
public class ModuleFactoryImpl implements ModuleFactory {

    private static HashMap<String, Class<? extends BussFragment>> moduleMap = new HashMap();

    static {

        // 账户管理
        moduleMap.put(ModuleCode.MODULE_ACCOUNT_0000, OverviewFragment.class);
        // TODO 资产汇总

        // 交易明细
        moduleMap.put(ModuleCode.MODULE_ACCOUNT_0200, TransDetailFragment.class);
        // 申请定期/活期
        moduleMap.put(ModuleCode.MODULE_ACCOUNT_0300, ApplyFragment.class);
        // 账户动户通知
        moduleMap.put(ModuleCode.MODULE_ACCOUNT_0400, AccSmsNotifyHomeFragment.class);
        // 账户挂失冻结
        moduleMap.put(ModuleCode.MODULE_ACCOUNT_0500, LossFragment.class);

        // 转账汇款主页面-我要转账
        moduleMap.put(ModuleCode.MODULE_TRANSER_0000, TransRemitBlankFragment.class);
        // 二维码转账
        moduleMap.put(ModuleCode.MODULE_TRANSER_0100, QrcodeTransFragment.class);
        // 手机号转账
        moduleMap.put(ModuleCode.MODULE_TRANSER_0200, PhoneEditPageFragment.class);
        // 转账记录
        moduleMap.put(ModuleCode.MODULE_TRANSER_0300, TransferRecordFragment.class);
        // 预约管理
        moduleMap.put(ModuleCode.MODULE_TRANSER_0400, PreRecordFragment.class);
        // 收款人管理
        moduleMap.put(ModuleCode.MODULE_TRANSER_0500, PayeeManageFragment2.class);
        // 我的二维码
        moduleMap.put(ModuleCode.MODULE_TRANSER_0600, QrcodeMeFragment.class);

        // 我要收款
        moduleMap.put(ModuleCode.MODULE_PAYEE_0000, MeMakeCollectionFragment1.class);
        // ATM无卡取款
        moduleMap.put(ModuleCode.MODULE_ATMWITHDRAWAL_0000, ATMWithDrawFragment.class);
        // 我要汇款
        moduleMap.put(ModuleCode.MODULE_PHONEPAYMENT_0000, MobileRemitFragment.class);
        //贷款管理
        moduleMap.put(ModuleCode.MODULE_LOAN_0000, LoanManagerFragment.class);

        //信用卡
        moduleMap.put(ModuleCode.MODULE_CREDIT_CARD_0000, CreditCardHomeFragment.class);

        //网申信用卡
        moduleMap.put(ModuleCode.MODULE_EAPPLY_CREDIT_CARD_0000, EApplyCreditCardFragment.class);

        moduleMap.put(ModuleCode.MODULE_QUICK_PAY_0000, HceCardListFragment.class);

        //二维码支付 - 二维码收付款
        moduleMap.put(ModuleCode.MODULE_QR_PAY_0000, QRPayMainFragment.class);
        //基金
        moduleMap.put(ModuleCode.MODULE_AFINC_0000, FundProductHomeFragment.class);
        // 理财
        moduleMap.put(ModuleCode.MODULE_BOCINVT_0000, WealthProductFragment.class);
        // 我的持仓
        moduleMap.put(ModuleCode.MODULE_BOCINVT_0100, FinancialPositionFragment.class);
        // 交易查询
        moduleMap.put(ModuleCode.MODULE_BOCINVT_0200, TransInquireFragment.class);
        // 到期产品查询
        //moduleMap.put(ModuleCode.MODULE_BOCINVT_0300, TransInquireFragment.class);
        // 撤单
        moduleMap.put(ModuleCode.MODULE_BOCINVT_0400, RepealOrderFragment.class);
        // 投资协议管理
        moduleMap.put(ModuleCode.MODULE_BOCINVT_0500, InvestTreatyFragment.class);
        // 账户管理
        moduleMap.put(ModuleCode.MODULE_BOCINVT_0600, AccountMainFragment.class);
        // 理财推荐
        moduleMap.put(ModuleCode.MODULE_BOCINVT_0700, RecommendFragment.class);

        //我的支付
        moduleMap.put(ModuleCode.MODULE_SETTING_MANAGER_0400, MinePayMenusFragment.class);
        //在线开户 activity 不自动跳转特殊处理
        //moduleMap.put(ModuleCode.MODULE_SETTING_MANAGER_0800, remot.class);

        //安全设置
        moduleMap.put(ModuleCode.MODULE_SETTING_MANAGER_0300, SecuritySettingFragment.class);

        // 消费金融
        moduleMap.put(ModuleCode.MODULE_CONSUME_FINANCE_0000, ConsumeFinanceFragment.class);

        //跨境金融
        moduleMap.put(ModuleCode.MODULE_CROSS_BORDER_FINANCE_0000, OverseasHomeFragment.class);



        //二维码扫描
        moduleMap.put(ModuleCode.MODULE_QR_SCAN, QRPayScanFragment.class);

        //--

        //活期账户界面 - 账户详情
        moduleMap.put(ModuleCode.MODULE_ACCOUNT_DETAIL, CurrentFragment.class);

        //金融超市
        //余额理财
        moduleMap.put(ModuleCode.MODULE_BALANCE_0000, EasyBussFragment.class);
        // 结构汇
        moduleMap.put(ModuleCode.MODEUL_SBREMIT_0000, BuyAndSellExchangeHomeFragment.class);

        // 双向宝
//        moduleMap.put(ModuleCode.MODULE_ISFOREXSTORAGECASH_0000, TempFragment.class);
        moduleMap.put(ModuleCode.MODULE_ISFOREXSTORAGECASH_0000, LongShortForexHomeFragment.class);

        //热门活动
        moduleMap.put(ModuleCode.MODULE_ACTIVITY_0000, ActivityManagementPaltformHomePageFragment.class);

    }

    /**
     * 根据模块ID，获取对应的fragment实例
     */
    public BussFragment getModuleFragmentInstance(String moduleID) {
        BussFragment fragment = null;
        try {
            fragment = moduleMap.get(moduleID).newInstance();
        } catch (Exception ex) {

        }
        return fragment;
    }


}
