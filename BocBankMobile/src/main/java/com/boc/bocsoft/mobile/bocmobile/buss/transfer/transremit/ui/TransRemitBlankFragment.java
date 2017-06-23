package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCardQueryBindInfo.PsnCardQueryBindInfoParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocNationalTransferVerify.PsnDirTransBocNationalTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocNationalTransferVerify.PsnDirTransBocNationalTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferVerify.PsnDirTransBocTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferVerify.PsnDirTransBocTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransfer.PsnDirTransCrossBankTransferParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransfer.PsnDirTransCrossBankTransferResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentConfirm.PsnEbpsRealTimePaymentConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentConfirm.PsnEbpsRealTimePaymentConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryBankInfobyCardBin.PsnQueryBankInfobyCardBinParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryBankInfobyCardBin.PsnQueryBankInfobyCardBinResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryRecentPayeeInfo.PsnQueryRecentPayeeInfoBean;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryRecentPayeeInfo.PsnQueryRecentPayeeInfoParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocNationalTransferVerify.PsnTransBocNationalTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocNationalTransferVerify.PsnTransBocNationalTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetNationalTransferCommissionCharge.PsnTransGetNationalTransferCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetNationalTransferCommissionCharge.PsnTransGetNationalTransferCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim.PsnTransPayeeListqueryForDimResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PhoneNumberFormat;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PinYinUtil;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.OverviewFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.AccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.BankEntity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransPayeeListqueryForDimViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransQueryExternalBankInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.ChooseBankFragment1;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.ChooseOpenAcountBankFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.PayeeManageFragment2;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.ScanCardNumFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.adapter.RecentPayeeListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.AccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.AccountQueryAccountDetailResult.AccountDetaiListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.CardQueryBindInfoResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.CrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.CrcdQueryAccountDetailResult.CrcdAccountDetailListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.OFAAccountStateQueryResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.PayerAndPayeeInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.TransPayeeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.TransRecentPayeeBean;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.TransRemitVerifyInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.presenter.PsnTransBlankPagePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.ui.AccountDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui.PurchaseFragment;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.bocmobile.yun.other.DictKey;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexResult;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexUtils;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDateTime;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransPayeeListqueryForDimViewModel.*;

/**
 * Created by wangyuan on 2016/6/7.
 * 转账汇款主界面，包含中行内，关联账户转账和跨行转账
 */

public class TransRemitBlankFragment extends MvpBussFragment<PsnTransBlankPagePresenter> implements TransContract.TransViewBlankPage {
    public final static String TRANS_TO_BOC = "boc";//行内
    public final static String TRANS_TO_BOC_DIR = "boc_dir";//行内
    public final static String TRANS_TO_LINKED = "boc_linked";//关联账户
    public final static String TRANS_TO_NATIONAL = "boc_national";//跨行转账汇款
    public final static String TRANS_TO_NATIONAL_DIR = "boc_national_dir";//跨行定向转账汇款
    public final static String TRANS_TO_NATIONAL_REALTIME = "boc_realtime";//跨行实时转账汇款
    public final static String TRANS_TO_NATIONAL_REALTIME_DIR = "boc_realtime_dir";//跨行实时定向转账汇款
    //    public final static String TRANS_TO_OFA = "boc_ofa";//资金划转转账
    public static final String LINKED_ACCOUNT_TYPE = "linked_account_type";//筛选出来的关联账户账户类型
    public static final String PAYEE_ACCOUNT_TYPE = "payee_account_type";//筛选出来的关联账户账户类型
    public static final String ACCOUNT_EXIST = "Accountexist";
    public static final String PAYEE_CURRENCY_LIST = "payeecurrencylist"; //
    public static final String PAYEE_CASHREMIT_LIST = "payeecashremitlist"; //
    private static final int REQUEST_CODE_SCAN_CARD = 111; // 请求码，扫描银行卡
    private static final int REQUEST_CODE_BELONG_BANK = 35; // 请求码，选择所属银行
    private static final int REQUEST_CODE_OPEN_BANK = 36; // 请求码，选择所属银行
    private static final int REQUEST_CODE_SELECT_PAYEE_ACCOUNT = 37; // 请求码，选择所属银行
    public static final int PICK_CONTACT = 1;
    public static  double TRANS_REMIT_LIMIT ;
    public static  double TRANS_REMIT_LIMIT_TOTOAL=300000.00 ;//累计交易限额
    public static final String ACCOUNT_FROM_ACCOUNTMANAGEMENT = "AccountFromAccManagement";//账户管理
    public static final String ACCOUNT_FROM_PAYEEMANAGEMENT = "AccountFromPayeeManagement";//收款人管理
    public static final String ACCOUNT_FROM_GOLDSTOREBUY = "AccountFromLianLong";//贵金属积存
    public static final String ACCOUNT_FROM_ACCOUTDETAILFRAGMENT = "AccountFromAccDetailFragment";//资金划转
    public static final String ACCOUNT_FROM_PURCHASEFRAGMENT = "AccountFromPurchaseFragment";//理财购买-->账户余额不足-->账户充值
    public static final String TYPE_ACC = "type";//
    public static final String TYPE_NORMAL = "typeother";//其他
    public static final String TYPE_OFA = "typeofa";//理财账户
    public static final String TYPE_ECARD = "typeecard";//电子卡
    public static final String TYPE_CRCD = "typecrcd";//信用卡
    public static final String ACCOUNT_TYPE_LIST = "AccountTypeList";
    public static final String ACCOUNT_LIST = "AccountList";
    public static final String ACCOUNT_SELECT = "AccountBean";
    protected View view_tansRemitPayerAccout1; //付款账户选择
    protected TextView tv_transRemitBalance;//余额
    protected TextView tv_transBalanceCurrency;//显示余额的币种
    protected EditMoneyInputWidget et_transRemitMoney1;//转账金额
    protected EditChoiceWidget et_Currency;//转账币种
    protected EditChoiceWidget et_executeType;//
    protected View line_executeType;
    protected EditChoiceWidget et_tansPayerAccout;//付款账户
    protected AutoEditTextClearView et_transRemitPayeeName;//收款人姓名
    protected ImageView im_payeeimage;//常用收款人图片按钮
    protected EditChoiceWidget et_transRemiPayeebank;//收款银行
    protected EditChoiceWidget et_transRemitOrgName;//开户网点
    protected EditClearWidget et_transRemitMessage1;//附言
    protected  EditClearWidget     et_transRemitPayeeAccNo1;
    protected EditClearWidget et_transRemitPayeePhone1;//收款人电话
    private CheckBox  cb_savePayee;
    protected Button bt_transRemitNext;//确定按钮
    protected LinearLayout ll_transPayerAccountBalance;
    private View view_transMain;//转账主view
    private View view_savePayee;//是否保存收款人view
    private View line_bank;//是否保存收款人
    private View line_org;//是否保存收款人view
    private View line_mobile;//是否保存收款人view
    private TextView tv_payeenametitle;
    private String transtype;//关联账户、行内转账，跨行转账
    private TransPayeeViewModel payeeModel;//收款人model
    private List<TransPayeeViewModel> payeeList;
    private SelectStringListDialog dl_currencySelectDialog;
    private SelectStringListDialog dl_transWaySelect;
    //    private String transExecuteType="0";//0：立即执行 1：预约日期执行 2：预约周期执行
    private AccountBean payerAccountSelected;//点选的账户
    private TransRemitVerifyInfoViewModel verifyInfoModel;//填写页面向确认页面传递的数据model
    private PsnTransBocTransferVerifyParams bocVerifyParams;//中行转账预交易参数
    private PsnDirTransBocTransferVerifyParams dirBocVerifyParams;//中行定向转账预交易参数
    private PsnTransBocNationalTransferVerifyParams nationalVerifyParams;//国内跨行预计预交易
    private PsnDirTransBocNationalTransferVerifyParams dirNationalVerifyParams;//国内跨行预计预交易
    private PsnDirTransCrossBankTransferParams dirNationalRealtimeVerifyParams;//跨行定向实时预交易
    private PsnEbpsRealTimePaymentConfirmParams nationalRealtimeVerifyParams;//跨行实时预交易
    private BankEntity mCurrentSelectBankEntity; // 当前用户选择的银行
//    private BankEntity tmpBankEntity; // 当前用户选择的银行
    private PsnTransQueryExternalBankInfoViewModel.OpenBankBean mCurrentOpenBankBean;//选择的开户行信息实体
    private PayeeEntity  payeeSelectedBean;//选择的收款人信息实体
    private String transMoneyAmount="";//转账金额
    private String transPayeeMobile="";//收款人手机号
    private String transPayeeName="";//收款人姓名
    private String transCurrency="";//转账币种
    private String executeType="";//转账方式  0 实时，1普通，2次日到账
    private String executeTypeName="";//转账方式名 0 实时，1普通，2次日到账
    private String executeDate="";
//    private double payerAccBalance;//可用余额
    private BigDecimal  availableBalance;//可用余额
    private String transCashRemit="";//钞汇标识
    private String balanceLoanFlag;//余额
    private String transPayeeAccountNumber="";//转账账号
    private String transPayeeAccountNumberBefore="";//转账账号,每次账号失去焦点的时候取值
//    private String accountId="";//账户Id
    private String payeeBankName="";//收款银行名
    private String payeeBankCode="";//收款银行号
    private String payeeBankOrgName="";//收款银行开户网点名
    private String payeeBankCnapsCode="";//收款银行Cnaps
    private String payeeBankCnapsCodeOrg;//收款银行开户网点Cnaps
    private String selectedPayeeBocFlag;//收款人标识
    //    private AccountDetaiListBean detailListBean;//选择付款账户的详情
    //    private CrcdAccountDetailListBean crcdAccountDetailListBean;//信用卡付款账户详情
    //    private AccountQueryAccountDetailResult payerAccountDetail;//付款账户详情
    private boolean  isOkToNext;//是否各个数据项都满足下一步条件？
    private String theErrorMessage;//报错的信息
    private PayerAndPayeeInfoModel payerAndPayeeMainInfo;
    private RecentPayeeListAdapter payeeNameDataAdapter;
    private List<BankEntity> hotBankEntityList;//常用银行
    //    private boolean isNeedtoSavePayee;//是否需要保存收款人
    //    private boolean isTrueAppointed=true;//是否是真的符合定向收款，因为有些人会改收款人信息，如果改了姓名或者账号，就不算定向了。
    private ArrayList<AccountBean> accountBeanList ;//付款账户列表
    //近期收款人列表
    private  List<TransRecentPayeeBean> recentPayeeList=new ArrayList<>();
    private TitleAndBtnDialog scanCardDialog;//扫描回来的卡号弹窗
    private TitleAndBtnDialog quotaWarningDailog;//限额弹框提示框
    private String cardNumber;//扫描出来的卡号
    private String functionId="";//
    //    private boolean needQueryPayeeForDim =false;//是否需要调接口查常用收款人
    private boolean onlyQueryPayeeToJump=false;// 只是查常用收款人，别的接口不用调用

    public TransRemitBlankFragment() {
    }
    //    private BocCloudCenter cloudBackupData;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        view_transMain = mInflater.from(mContext).inflate(R.layout.boc_trans_remit, null);
        return view_transMain;
    }
    @Override
    public void beforeInitView() {
        //仅供测试，可以删除
//        getPresenter().subscribe();//不显示等待按钮
//        //判断是否满足抽奖资格，并返回票信息
//        getPresenter().queryTransActivityStatusTest("2224928233");
    }
    @Override
    public void initView() {
        line_bank=view_transMain.findViewById(R.id.line_openbank);
        line_org=view_transMain.findViewById(R.id.line_orgname);
        line_mobile=view_transMain.findViewById(R.id.line_mobile);
        et_tansPayerAccout = (EditChoiceWidget) view_transMain.findViewById(R.id.transfer_payeracc);
        et_tansPayerAccout.setChoiceTextContentHint("请选择");
        et_tansPayerAccout.setChoiceTextName("付款账户");
        tv_transRemitBalance = (TextView) view_transMain.findViewById(R.id.trans_remit_balance);
        tv_transBalanceCurrency = (TextView) view_transMain.findViewById(R.id.trans_accout_balance_currency);
        et_transRemitMoney1 = (EditMoneyInputWidget) view_transMain.findViewById(R.id.trans_remit_money);
        et_transRemitMoney1.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_money_color_red));
        et_Currency = (EditChoiceWidget) view_transMain.findViewById(R.id.transfer_currency);
        et_Currency.setChoiceTitleBold(true);
        et_Currency.setArrowImageGone(false);
        et_executeType = (EditChoiceWidget) view_transMain.findViewById(R.id.transfer_way);
        line_executeType =view_transMain.findViewById(R.id.line_transway);
        et_Currency.setChoiceTitleBold(true);
        setExecuteTypeVisible(false);
        et_executeType.setChoiceTextContent("实时");
        executeType="0";
        executeTypeName="实时";
        et_executeType.setChoiceTextContentHint("请选择");
        et_transRemitPayeeName = (AutoEditTextClearView) view_transMain.findViewById(R.id.trans_remit_payeename);
        im_payeeimage = (ImageView) view_transMain.findViewById(R.id.payeeimage);
        et_transRemitPayeeAccNo1 = (EditClearWidget) view_transMain.findViewById(R.id.trans_remit_payee_acc);
        et_transRemitPayeeAccNo1.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        et_transRemitPayeeAccNo1.setEditWidgetTitle("收款账号");
        et_transRemitPayeeAccNo1.getContentEditText().setSingleLine();
        et_transRemitPayeeAccNo1.setEditWidgetHint("请输入");
        et_transRemitPayeeAccNo1.setClearEditRightImage(R.drawable.boc_camera);
        et_transRemitPayeeAccNo1.showEditWidgetRightImage(true);
        et_transRemiPayeebank = (EditChoiceWidget) view_transMain.findViewById(R.id.trans_remit_openbank);
        et_transRemiPayeebank.setVisibility(View.GONE);
        line_bank.setVisibility(View.GONE);
        et_transRemiPayeebank.setChoiceTitleBold(true);
        et_transRemitOrgName = (EditChoiceWidget) view_transMain.findViewById(R.id.trans_remit_orgname);
        et_transRemitOrgName.setVisibility(View.GONE);
        line_org.setVisibility(View.GONE);
        et_transRemitOrgName.setChoiceTitleBold(true);
        et_transRemitMessage1 = (EditClearWidget) view_transMain.findViewById(R.id.trans_remit_memo);
        et_transRemitMessage1.setEditWidgetTitle("附言");
        et_transRemitMessage1.setEditWidgetHint("选填，最多10个汉字");
        et_transRemitMessage1.setAllNumPermit(20);
        et_transRemitPayeePhone1 = (EditClearWidget) view_transMain.findViewById(R.id.trans_remit_payee_mobile);
        et_transRemitPayeePhone1.setClearEditRightImage(R.drawable.boc_callbook);
        et_transRemitPayeePhone1.showEditWidgetRightImage(true);
        et_transRemitPayeePhone1.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        et_transRemitPayeePhone1.getContentEditText().setSingleLine();
        et_transRemitPayeePhone1.getContentEditText().setMaxEms(13);
        et_transRemitPayeePhone1.setEditWidgetTitle("收款人手机号");
        et_transRemitPayeePhone1.setEditWidgetHint("短信通知收款人,选填");
        et_transRemitPayeePhone1.setVisibility(View.GONE);
        line_mobile.setVisibility(View.GONE);
        bt_transRemitNext = (Button) view_transMain.findViewById(R.id.trans_remit_next);
        ll_transPayerAccountBalance = (LinearLayout) view_transMain.findViewById(R.id.trans_payer_account_balance);
        ll_transPayerAccountBalance.setVisibility(View.GONE);
        cb_savePayee= (CheckBox) view_transMain.findViewById(R.id.cb_save_payee);
        view_savePayee = view_transMain.findViewById(R.id.ll_save_payee);
        view_savePayee.setVisibility(View.GONE);
        cb_savePayee.setChecked(false);
        tv_payeenametitle= (TextView) view_transMain.findViewById(R.id.payee_name_title);
        scanCardDialog=new TitleAndBtnDialog(mContext);
        quotaWarningDailog=new TitleAndBtnDialog(mContext);
    }

    @Override
    public void initData() {
//        transPresenter = new PsnTransBlankPagePresenter(this);
        context1= (ApplicationContext) mActivity.getApplicationContext();
        payeeList = new ArrayList<>();
        et_transRemiPayeebank.setChoiceTextName(getResources().getString(R.string.trans_payee_bank_name));
        et_transRemiPayeebank.setChoiceTextContentHint(getResources().getString(R.string.please_select));
        et_transRemitOrgName.setChoiceTextName(getResources().getString(R.string.trans_payee_bank_orgname));
        et_transRemitOrgName.setChoiceTextContentHint(getResources().getString(R.string.please_select));
        et_transRemitMoney1.setContentHint(getResources().getString(R.string.free_fee));
        et_transRemitMoney1.setEditWidgetTitle(getResources().getString(R.string.trans_trans_money));
        et_transRemitMoney1.setMaxLeftNumber(11);
        et_Currency.setChoiceTextName(getResources().getString(R.string.trans_currency));
        et_executeType.setChoiceTextName(getResources().getString(R.string.trans_way));
        dl_currencySelectDialog = new SelectStringListDialog(mContext);
        dl_transWaySelect = new SelectStringListDialog(mContext);
        mCurrentSelectBankEntity=new BankEntity();
        et_transRemitPayeeName.setThreshold(1);
        getOtherFocused();
        cb_savePayee.setChecked(false);
        setFixedValue();
        initPayerAndPayeeInfo();
    }
    //让其他地方获取焦点
    public void getOtherFocused(){
        tv_payeenametitle.setFocusableInTouchMode(true);
        tv_payeenametitle.setFocusable(true);
        tv_payeenametitle.requestFocus();
    }
    //初始化收付款人信息
    public void initPayerAndPayeeInfo(){
        hotBankEntityList = getHotBankList();//公共方法？
        accountBeanList =(ArrayList<AccountBean>)
                ApplicationContext.getInstance().getChinaBankAccountList(getFilteredAccountType());
        if (null==accountBeanList||accountBeanList.size()==0){
            showErrorDialog("无可操作账户，请确认");
            return;//没有查到付款账户
        }
        String accIdFromAccmanagement=getArguments().getString(ACCOUNT_FROM_ACCOUNTMANAGEMENT);//从账户管理跳过来，作为付款账户
        String accIdFromAccDetailfragment=getArguments().getString(ACCOUNT_FROM_ACCOUTDETAILFRAGMENT);//
        String accIdFromBackup=BocCloudCenter.getInstance().getAccountId(AccountType.ACC_TYPE_TRANSREMIT);
        String accIdFromPurchaseFragment=getArguments().getString(ACCOUNT_FROM_PURCHASEFRAGMENT);//理财购买
        PayeeEntity accIdFromPayeeManagement=getArguments().getParcelable(ACCOUNT_FROM_PAYEEMANAGEMENT);//从账户管理跳过来，作为付款账户
        String accIdFromGodStoreBuy=getArguments().getString(ACCOUNT_FROM_GOLDSTOREBUY);//从联龙跳过来，作为收款账户
        //付款账户,要放在收款账户设置之前，因为functionId要以收款账户类型为准
        if (!StringUtils.isEmptyOrNull(accIdFromAccmanagement)){ //账户管理
            functionId= ACCOUNT_FROM_ACCOUNTMANAGEMENT;
            payerAccountSelected= getLinkedAccByAccId(accIdFromAccmanagement);
        }else if(!StringUtils.isEmptyOrNull(accIdFromAccDetailfragment)){
            functionId= ACCOUNT_FROM_ACCOUTDETAILFRAGMENT;
            payerAccountSelected= getLinkedAccByAccId(accIdFromAccDetailfragment);
        }
        else {
            functionId="";
           if(!StringUtils.isEmptyOrNull(accIdFromBackup)){
                payerAccountSelected= getLinkedAccByAccId(accIdFromBackup);
            }
            if (null==payerAccountSelected){
                payerAccountSelected= accountBeanList.get(0);
            }
//            if (!PublicUtils.isEmpty(accountBeanList)){
//                for(int i=0;i<accountBeanList.size();i++){
//                    String accId= accountBeanList.get(i).getAccountId();
//                    if (accId.equals(accIdFromGodStoreBuy)
//                            ||accId.equals(accIdFromPurchaseFragment)
//                            ||(null==accIdFromPayeeManagement?false:accId.equals(accIdFromPayeeManagement.getAccountId()))){
//                    }else{
//                        payerAccountSelected=accountBeanList.get(i);
//                        break;
//                    }
//                }
//            }
        }
//放在收款账户下边，是为了有时候payerAccount可能为null，
        if (null==payerAccountSelected){
        }else{
            queryAccoutDetailInfo(payerAccountSelected);
        }
        //收款账户
        if(!StringUtils.isEmptyOrNull(accIdFromGodStoreBuy)) {//联龙贵金属积存
            functionId=ACCOUNT_FROM_GOLDSTOREBUY;
            payeeSelectedBean=changeLinkedAcctoPayeeEntity(getLinkedAccByAccId(accIdFromGodStoreBuy));
        }else if(null!=accIdFromPayeeManagement){
            functionId=ACCOUNT_FROM_PAYEEMANAGEMENT;
            payeeSelectedBean=accIdFromPayeeManagement;
        }else if (!StringUtils.isEmptyOrNull(accIdFromPurchaseFragment)){
            functionId=ACCOUNT_FROM_PURCHASEFRAGMENT;
            payeeSelectedBean=changeLinkedAcctoPayeeEntity(getLinkedAccByAccId(accIdFromPurchaseFragment));
        }
        if(null==payeeSelectedBean){
            return ;
        }else {
            setViewAfterPayeeSelected(payeeSelectedBean);
        }
    }

    //通过accoutId 找到关联账户
    public AccountBean getLinkedAccByAccId(String accid){
        if (StringUtils.isEmptyOrNull(accid)){
            return null;
        }
        for (AccountBean oneBean:accountBeanList){
            if(accid.equals(oneBean.getAccountId())||accid.equals(BocCloudCenter.getSha256String(oneBean.getAccountId()))){
                return oneBean;
            }
        }
//  showErrorDialog("账户错误，请修改");
        return null;
    }

    //设置限制
    private void setFixedValue(){
        String limitMoney=BocCloudCenter.getInstance().getDirt(DictKey.CHECKREALTIMEFLAG);
        TRANS_REMIT_LIMIT=StringUtils.isEmptyOrNull(limitMoney)
                ?50000.00: new Double(limitMoney);
    }
    //根据当前信息判断显示的转账方式
    public boolean isBocTrans(){
//         if (null!=payeeSelectedBean&&payeeSelectedBean.isLinked()){
//             return true;
//         }
        if (et_transRemiPayeebank.getVisibility()==View.VISIBLE&&!"中国银行".equals(et_transRemiPayeebank.getChoiceTextContent())){
            return false;
        }
        return true;
    }
    public void queryAccoutDetailInfo(AccountBean accountBean){
        showLoadingDialog();
        //103 104 107 信用卡,
        if(ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountBean.getAccountType())||ApplicationConst.ACC_TYPE_GRE.equals(accountBean.getAccountType())
                ||ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(accountBean.getAccountType())){
            getPresenter().queryCrcdAccountDetail(accountBean.getAccountId(),"");
        }
        else{
            if(ApplicationConst.ACC_TYPE_BOCINVT.equals(payerAccountSelected.getAccountType())) {//190 理财账户
                getPresenter().queryPsnOFAAccountState();//查询绑定账户
//            }else if ("1".equals(payerAccountSelected.getEcard())){//电子卡绑定账户
            }else if ("2".equals(payerAccountSelected.getAccountCatalog())||"3".equals(payerAccountSelected.getAccountCatalog())){//电子卡绑定账户
                PsnCardQueryBindInfoParams params=new PsnCardQueryBindInfoParams();
                params.setAccountId(payerAccountSelected.getAccountId());
                getPresenter().querPsnCardQueryBindInfo(params);//查接口电子卡绑定账户
            }else{
                getPresenter().queryAccountBalance(accountBean.getAccountId());// 查询账户余额
            }
        }
    }
    /**
     * @return 展示需要显示账户里类型
     */
    public ArrayList<String> getFilteredAccountType() {
        ArrayList<String> accoutType = new ArrayList<>();
//        00：对公账户,  101：普通活期,  103：中银信用卡, 104：长城信用卡 ,107: 单外币信用卡, 借记卡119（包括医保账户）
//        140：存本取息,150：零存整取,152：教育储蓄,170：定期一本通,188：活期一本通，190网上理财账户 、优汇通专户199
        String[] accType={
                ApplicationConst.ACC_TYPE_BRO,//借记卡119
                ApplicationConst.ACC_TYPE_RAN,//活期一本通188
                ApplicationConst.ACC_TYPE_ORD,//普活 101
                ApplicationConst.ACC_TYPE_GRE,//长城信用卡104
                ApplicationConst.ACC_TYPE_ZHONGYIN,//中银系列信用卡 103
                ApplicationConst.ACC_TYPE_SINGLEWAIBI,//单外币信用卡107
                ApplicationConst.ACC_TYPE_BOCINVT,//网上专属理财190
                ApplicationConst.ACC_TYPE_YOUHUITONG,//优汇通专户199
        };
        accoutType.addAll(Arrays.asList(accType));
        return accoutType;
    }
    //得到当前收 付款账户信息
    public PayerAndPayeeInfoModel getLatestPayerAndPayeeInfo(){
        payerAndPayeeMainInfo=new PayerAndPayeeInfoModel();
        payerAndPayeeMainInfo.setTransCsahRemit(transCashRemit);
        payerAndPayeeMainInfo.setTransCurrency(transCurrency);
        if (null!=payerAccountSelected){
            payerAndPayeeMainInfo.setPayerAccoutNum(payerAccountSelected.getAccountNumber());
            payerAndPayeeMainInfo.setPayerCurrencyList(payerAccCurrencyList);
        }
        if (null!=payeeSelectedBean && payeeSelectedBean.isLinked()){
            payerAndPayeeMainInfo.setPayeeAccoutNum(payeeSelectedBean.getAccountNumber());
            payerAndPayeeMainInfo.setPayeeBoc("1".equals(payeeSelectedBean.getBocFlag())?true:false);
            payerAndPayeeMainInfo.setPayeeLinked(payeeSelectedBean.isLinked());
            if (isPayeeAccCcrd){
                payerAndPayeeMainInfo.setPayeeCurrencyList(payeeLinkedAccCurrencyList);
            }else{
                payerAndPayeeMainInfo.setPayeeCurrencyList(null);
            }
        }else{
            payerAndPayeeMainInfo.setPayeeAccoutNum("");
            payerAndPayeeMainInfo.setPayeeBoc("中国银行".equals(et_transRemiPayeebank.getChoiceAutoTextContent()));
            payerAndPayeeMainInfo.setPayeeLinked(false);
        }
        return payerAndPayeeMainInfo;
    }


    /**
     * 判断逻辑：收款人如果是选择出来的，如果是定向收款人，改动过收款人姓名和账号，就不算是定向了，同时需要最后保存到新的常用收款人。更改手机号或者
     * 收款银行则不影响，还算是定向的。 更改收款人的任意信息都需要调用增加收款人信息接口。
     * 如果是手输入的收款人，那就需要调用接口增加收款人
     */
    public void setExecuteTypeVisible(boolean isVisible){
        if (isVisible){
            et_executeType.setVisibility(View.VISIBLE);
            line_executeType.setVisibility(View.VISIBLE);
        }else{
            et_executeType.setVisibility(View.GONE);
            line_executeType.setVisibility(View.GONE);
        }
//        et_executeType.setChoiceTextContent("");
//        executeType="";
//        executeTypeName="";
//        executeDate="";
    }

//    boolean cb_savePayeeIsByExetype;//展示保存收款人
//    public void setSavePayeeVisibility(boolean isVisible,boolean isExecuteTypeChanged){
//    public void setSavePayeeVisibility(boolean isVisible){
////        cb_savePayeeIsByExetype=isExecuteTypeChanged;
//        if(!StringUtils.isEmptyOrNull(et_transRemitPayeeName.getText().toString().trim())
//                ||!StringUtils.isEmptyOrNull(et_transRemitPayeeAccNo1.getEditWidgetContent().trim())
//                ||(et_transRemiPayeebank.getVisibility()==View.VISIBLE&&!StringUtils.isEmptyOrNull(et_transRemiPayeebank.getChoiceTextContent().trim()))
//                ||(et_transRemitPayeePhone1.getVisibility()==View.VISIBLE&&!StringUtils.isEmptyOrNull(et_transRemitPayeePhone1.getEditWidgetContent().trim()))
//                ||(et_transRemitPayeePhone1.getVisibility()==View.VISIBLE &&!StringUtils.isEmptyOrNull(et_transRemitOrgName.getChoiceTextContent().trim()))){
//            if (isVisible){
//                view_savePayee.setVisibility(View.VISIBLE);
//                cb_savePayee.setChecked(true);
//            }else{
//                view_savePayee.setVisibility(View.GONE);
//                cb_savePayee.setChecked(false);
//            }
//        }else{
//            view_savePayee.setVisibility(View.GONE);
//            cb_savePayee.setChecked(false);
//        }
//    }


//bocflag 1 中行，3跨行实时，0 跨行
public void setSavePayeeView(){
    boolean  isPayeeNameChanged=false;
    boolean  isPayeeAccnoChanged=false;
    boolean  isPayeeBankChanged=false;
    boolean  isPayeeOrgChanged=false;
    boolean  isPayeePhoneChanged=false;
    boolean  isExecuteTypeChangeed=false;
    boolean isNeedtoSavePayee=false;
    if (null!=payeeSelectedBean){
        if (!et_transRemitPayeeName.getText().toString().trim().equals(payeeSelectedBean.getAccountName())){
            isPayeeNameChanged=true;
        }else{
            isPayeeNameChanged=false;
        }

        if (!payeeSelectedBean.getAccountNumber().equals(transPayeeAccountNumber)){
            isPayeeAccnoChanged=true;
        }else{
            isPayeeAccnoChanged=false;
        }

        if (et_transRemiPayeebank.getVisibility()==View.VISIBLE){
            if (!payeeSelectedBean.isLinked()){
                if ("1".equals(payeeSelectedBean.getBocFlag())){
                    if (!getResources().getString(R.string.trans_boc).equals(payeeBankName)){
                        isPayeeBankChanged=true;
                    }else{
                        isPayeeBankChanged=false;
                    }
                }else{
//                    if (!(null==payeeSelectedBean.getBankName()?"":payeeSelectedBean.getBankName()).contains(et_transRemiPayeebank.getChoiceTextContent())){
                    if (null!=payeeBankName&&!payeeBankName.equals(payeeSelectedBean.getBankName())){
                        isPayeeBankChanged=true;
                    }else{
                        isPayeeBankChanged=false;
                    }
                }
            }
        }
        if (et_transRemitOrgName.getVisibility()==View.VISIBLE){
            if (!payeeSelectedBean.isLinked()){
                if (!et_transRemitOrgName.getChoiceTextContent().equals(null==payeeSelectedBean.getAddress()?"":payeeSelectedBean.getAddress())){
                    isPayeeOrgChanged=true;
                }else{
                    isPayeeOrgChanged=false;
                }
            }
        }
        if(et_transRemitPayeePhone1.getVisibility()==View.VISIBLE){
            if (!payeeSelectedBean.isLinked()){
                if (null!=transPayeeMobile){ //transPayeeMobile有可能会是null
                    if (!transPayeeMobile.equals(payeeSelectedBean.getMobile())){
                        isPayeePhoneChanged=true;
                    }else{
                        isPayeePhoneChanged=false;
                    }
                }else if (null!=payeeSelectedBean.getMobile()){
                    if (!payeeSelectedBean.getMobile().equals(transPayeeMobile)){
                        isPayeePhoneChanged=true;
                    }else{
                        isPayeePhoneChanged=false;
                    }
                }else{
                    isPayeePhoneChanged=false;
                }
            }
        }
        if (et_executeType.getVisibility()==View.VISIBLE&&!"1".equals(payeeSelectedBean.getBocFlag())){
            if(!payeeSelectedBean.isLinked()){
                if (("实时".equals(executeTypeName)&&!"3".equals(payeeSelectedBean.getBocFlag()))
                        || (!"实时".equals(executeTypeName)&&"3".equals(payeeSelectedBean.getBocFlag()))){
                    isExecuteTypeChangeed=true;
                }else{
                    isExecuteTypeChangeed=false;
                }
            }
        }
        isNeedtoSavePayee=isPayeeNameChanged||isPayeeAccnoChanged||isPayeeBankChanged||isPayeeOrgChanged||isPayeePhoneChanged||isExecuteTypeChangeed;
    }else{
        //除非所有的输入框都是空，否则就要展示保存为常用收款人view
        if(StringUtils.isEmptyOrNull(et_transRemitPayeeName.getText().toString().trim())
                &&StringUtils.isEmptyOrNull(et_transRemitPayeeAccNo1.getEditWidgetContent().trim())
                &&StringUtils.isEmptyOrNull(et_transRemiPayeebank.getChoiceTextContent().trim())
                &&StringUtils.isEmptyOrNull(et_transRemitPayeePhone1.getEditWidgetContent().trim())
                &&StringUtils.isEmptyOrNull(et_transRemitOrgName.getChoiceTextContent().trim())){
            isNeedtoSavePayee =false;
        }else{
            isNeedtoSavePayee =true;
        }
    }
    if (isNeedtoSavePayee){
        view_savePayee.setVisibility(View.VISIBLE);
        cb_savePayee.setChecked(true);
    }else{
        view_savePayee.setVisibility(View.GONE);
        cb_savePayee.setChecked(false);
    }
}
    private AutoEditTextClearView.AutoCompeleteEditListener payeeNameListener=new AutoEditTextClearView.AutoCompeleteEditListener() {
        @Override
        public void onFocusChange(boolean hasFocus) {
            if (!hasFocus) {
                hideSoftInput();
            }
        }
        @Override
        public void afterTextChange(Editable s) {
            transPayeeName = et_transRemitPayeeName.getText().toString().trim();
            et_transRemiPayeebank.setVisibility(View.VISIBLE);
//            et_transRemiPayeebank.setChoiceTextContent("");;2016年12月1日注释
            line_bank.setVisibility(View.VISIBLE);
//            et_transRemitOrgName.setChoiceTextContent("");
            et_transRemitPayeePhone1.setVisibility(View.VISIBLE);
            line_mobile.setVisibility(View.VISIBLE);
            if (null!=payeeSelectedBean){
                if (!payeeSelectedBean.isLinked()){
                    if (!transPayeeName.equals(payeeSelectedBean.getAccountName())){
                        payeeSelectedBean=null;
                        isPayeeAccCcrd=false;//2016年12.6
                    }
                }else{
                    if (!transPayeeName.equals(context1.getUser().getCustomerName())){
                        payeeSelectedBean=null;//2016年10月25日
                        if (isPayeeAccCcrd){
                            isPayeeAccCcrd=false;//2016年12.6
                            getCurrencyToShowList();
                            setBalanceView();
                        }else{
                            isPayeeAccCcrd=false;//2016年12.6
                        }

                    }
                }}
            setExecuteTypeVisible(true);
//          setSavePayeeView();??
//            setSavePayeeVisibility(true);//ab12
            setSavePayeeView();
        }

        @Override
        public void onTextchangeing(CharSequence s, int start, int before, int count) {
        }
    };


    // 处理金额数据， 安卓金额输入框允许输入05这样的数据；
    public String dealwithMoneyString(String moneyString){
        if (StringUtils.isEmptyOrNull(moneyString)){
            return moneyString;
        }
//        if (0==moneyString.indexOf("0")&&1!=moneyString.indexOf(".")){
//            return moneyString.substring(1,moneyString.length());
//        }else{
//            return moneyString;
//
//        }
        return StringUtils.subZeroAndDot(moneyString);
    }

    public String getTheNextDay(){
        LocalDateTime nextDay=context1.getCurrentSystemDate().plusDays(1);
        String nextDayString=nextDay.format(DateFormatters.dateFormatter1);
        return nextDayString;
    }


    @Override
    public void  setListener() {
        //付款账户选择
        et_tansPayerAccout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                startForResult(TransPayerSelectFragment.newInstanceWithData(accountBeanList,getLatestPayerAndPayeeInfo()),SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
            }
        });
        //币种监听
        et_Currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrencyToShowList();//可能用户手动编辑了收款账户，这时候需要重新更新combineCurrencyList
                if (1< combineCurrencyList.size()){
                    dl_currencySelectDialog.setListData(
//                            PublicCodeUtils.getCurrencyStringList(mContext, payerAccCurrencyList, payerAccCashRemitList));
                            PublicCodeUtils.getCurrencyStringList(mContext, combineCurrencyList, combineCashRemitList));
                    dl_currencySelectDialog.show();
                }
            }
        });

        //币种选择框
        dl_currencySelectDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
            @Override
            public void onSelect(int position, String model) {
                if (null!=payeeSelectedBean&&!payeeSelectedBean.isLinked()){
//                    if (!ApplicationConst.CURRENCY_CNY.equals(transCurrency= payerAccCurrencyList.get(position))){
                    if (!ApplicationConst.CURRENCY_CNY.equals(transCurrency= combineCurrencyList.get(position))){
                        showErrorDialog(getResources().getString(R.string.trans_error_only_linkedAcc));
                        dl_currencySelectDialog.dismiss();
                        return;}}
//                if (payerAccCurrencyList.get(position).equals(transCurrency)&& payerAccCashRemitList.get(position).equals(transCashRemit)){
                if (combineCurrencyList.get(position).equals(transCurrency)&& combineCashRemitList.get(position).equals(transCashRemit)){
                }else{
//                    et_transRemitMoney1.setmContentMoneyEditText("");//2016-9-24
//                    transMoneyAmount="";
                    et_Currency.setChoiceTextContent(model);
                    transCurrency= combineCurrencyList.get(position);
                    transCashRemit= combineCashRemitList.get(position);
                    availableBalance=combineCurrencyBalanceList.get(position);
//                    payerAccBalance=combineCurrencyBalanceList.get(position).doubleValue();
                    balanceLoanFlag =combineLoanFlagList.get(position);//0欠款，
//                    setBalanceView1(transCurrency, combineCurrencyBalanceList.get(position),transCashRemit,"");//更新UI
                    setBalanceView1(transCurrency, combineCurrencyBalanceList.get(position),transCashRemit,balanceLoanFlag);//更新UI
                }
                dl_currencySelectDialog.dismiss();
            }
        });
        /**
         * 转账金额实时格式化
         */
        et_transRemitMoney1.setOnKeyBoardListener(new EditMoneyInputWidget.KeyBoardDismissOrShowCallBack() {
            @Override
            public void onKeyBoardDismiss() {
                transMoneyAmount=dealwithMoneyString(et_transRemitMoney1.getContentMoney());
//                if (!"".equals(transMoneyAmount)){
//                    if (null==payeeSelectedBean){
//                        if (((!getResources().getString(R.string.trans_boc).equals(payeeBankName))
//                                &&Double.valueOf(transMoneyAmount)>TRANS_REMIT_LIMIT) ||!isRealtimeBank(payeeBankName)){
//                            if(et_transRemiPayeebank.getVisibility()==View.VISIBLE){
//                                et_transRemitOrgName.setVisibility(View.VISIBLE);
//                                line_org.setVisibility(View.VISIBLE);
//                            }
//                        }else{
////                            et_transRemiPayeebank.setVisibility(View.GONE);//2016年11月17日注释
////                            line_bank.setVisibility(View.GONE);
//                            et_transRemitOrgName.setVisibility(View.GONE);
//                            line_org.setVisibility(View.GONE);
//                        }
//                    }else{
//                        if (payeeSelectedBean.isLinked()){
//                        } else {
//                            if (Double.valueOf(transMoneyAmount)>TRANS_REMIT_LIMIT){
//                                et_transRemitOrgName.setVisibility(View.VISIBLE);
//                                line_org.setVisibility(View.VISIBLE);
//                            }else{
//                                if (isRealtimeBank(payeeBankName)){
//                                    et_transRemitOrgName.setVisibility(View.GONE);
//                                    line_org.setVisibility(View.GONE);
//                                }else{
//                                    et_transRemitOrgName.setVisibility(View.VISIBLE);
//                                    line_org.setVisibility(View.VISIBLE);
//                                }
//                            }
//                        }
//                    }
//                }
            }

            @Override
            public void onKeyBoardShow() {
                if (ApplicationConst.CURRENCY_JPY.equals(transCurrency)||"088".equals(transCurrency)){
                    et_transRemitMoney1.setMaxRightNumber(0);
                }else{
                    et_transRemitMoney1.setMaxRightNumber(2);
                }
                et_transRemitMoney1.setCurrency(transCurrency);
            }
        });


//      收款人名称
        et_transRemitPayeeName.setAutoCopEditViewListener(payeeNameListener
        );

        //收款人名称
        et_transRemitPayeeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideSoftInput();
                TransRecentPayeeBean selectedRecentPayee= payeeNameDataAdapter.getItem(position);
                 payeeSelectedBean=new PayeeEntity();
//                accountId=selectedRecentPayee.getPayeetId();//账户Id
                BeanConvertor.toBean(selectedRecentPayee,payeeSelectedBean);
                //转换
//                if(null==accountId ||"null".equals(accountId)){
//
//                }else{
//                    payeeSelectedBean.setPayeetId(Integer.valueOf(selectedRecentPayee.getPayeetId()));
//                }
                if (null==payeeSelectedBean){
                    showErrorDialog(getResources().getString(R.string.trans_error_select_another_payee_account));
                    return;
                }else{
                    setViewAfterPayeeSelected(payeeSelectedBean);
                }
            }
        });
        /**
         * 调用接口，查询联系人展示
         */
        im_payeeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ArrayList<AccountBean> accountBeanList =(ArrayList<AccountBean>)
//                        ApplicationContext.getInstance().getChinaBankAccountList(getFilteredAccountType());
                hideSoftInput();
                onlyQueryPayeeToJump=true;
                toGetPayeeListForDim();

            }
        });

        //扫描卡的弹窗
        scanCardDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {
                scanCardDialog.dismiss();
            }
            @Override
            public void onRightBtnClick(View view) {
                transPayeeAccountNumber=cardNumber.replace(" ","");
                transPayeeAccountNumberBefore=transPayeeAccountNumber;
//              et_transRemitPayeeAccNo1.setEditWidgetContent(NumberUtils.formatCardNumberStrong(transPayeeAccountNumber));
                et_transRemitPayeeAccNo1.setEditWidgetContent(transPayeeAccountNumber);
                scanCardDialog.dismiss();
            }
        });


        final EditClearWidget.ClearEditTextWatcher watcher1=new EditClearWidget.ClearEditTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                onEditTextChanged(s.toString().trim(), start, before, et_transRemitPayeeAccNo1.getContentEditText());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                transPayeeAccountNumber=s.toString().replace(" ","").trim();
                et_transRemiPayeebank.setVisibility(View.VISIBLE);//2016-11-4
                line_bank.setVisibility(View.VISIBLE);
                et_transRemitPayeePhone1.setVisibility(View.VISIBLE);
                line_mobile.setVisibility(View.VISIBLE);
                if (null!=payeeSelectedBean&&!transPayeeAccountNumber.equals(payeeSelectedBean.getAccountNumber())){
                    payeeSelectedBean=null;//2016年10月25日 这句话很危险
                    if (isPayeeAccCcrd){
                        isPayeeAccCcrd=false;
                        getCurrencyToShowList();
                        setBalanceView();
                    }else{
                        isPayeeAccCcrd=false;
                    }
                }
                setExecuteTypeVisible(true);
                setSavePayeeView();
            }
        };
        //收款人账号
        et_transRemitPayeeAccNo1.setEditClearWidgetOnFocusListener(new EditClearWidget.EditClearWidgetOnFocusCallback(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){ //失去焦点
                    //如果是有改动
                    et_transRemitPayeeAccNo1.setClearEditTextWatcherListener(null);
                    if(!"".equals(et_transRemitPayeeAccNo1.getEditWidgetContent().trim())){
                        transPayeeAccountNumber = et_transRemitPayeeAccNo1.getEditWidgetContent().trim().replace(" ", "");
                        if(!transPayeeAccountNumber.equals(transPayeeAccountNumberBefore)) {
//                            et_transRemitPayeeAccNo1.setEditWidgetContent(NumberUtils.formatCardNumberStrong(transPayeeAccountNumber));
                            et_transRemitPayeeAccNo1.setEditWidgetContent(transPayeeAccountNumber);
                            PsnQueryBankInfobyCardBinParams params = new PsnQueryBankInfobyCardBinParams();
                            params.setAccountNumber(transPayeeAccountNumber);
                            showLoadingDialog();
                            getPresenter().queryQueryBankInfobyCardBin(params);//通过卡号查所属银行
                        }
                        transPayeeAccountNumberBefore=transPayeeAccountNumber;
                    }else{
                    }
                }else{//获取焦点的时候
                    if (!StringUtils.isEmptyOrNull(transPayeeAccountNumber)){
                        et_transRemitPayeeAccNo1.setEditWidgetContent(transPayeeAccountNumber.trim());
                    }
                    et_transRemitPayeeAccNo1.setClearEditTextWatcherListener(watcher1);
                }
            }
        });
        /**
         * 打开相机，这里调用已有
         */
        et_transRemitPayeeAccNo1.setRightImageCallBack(new EditClearWidget.EditRightImageOnClick(){
            @Override
            public void onClick() {
                hideSoftInput();
                ScanCardNumFragment fragment = new ScanCardNumFragment();
                try{
                    startForResult(fragment, REQUEST_CODE_SCAN_CARD);
                }catch (Exception e){
                    showErrorDialog(e.getMessage());
                }
            }
        });

        et_transRemiPayeebank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                ChooseBankFragment1 chooseBankFragment=new ChooseBankFragment1() ;//收款银行页面
                Bundle bankBundle=new Bundle();
                if (et_executeType.getChoiceTextContent().equals("")|| et_executeType.getChoiceTextContent().equals("实时")){
                    bankBundle.putInt(ChooseBankFragment1.KEY_CHOOSE_BANK_TYPE,1);
                }else{
                    bankBundle.putInt(ChooseBankFragment1.KEY_CHOOSE_BANK_TYPE,2);
                }
                chooseBankFragment.setArguments(bankBundle);
                startForResult(chooseBankFragment,REQUEST_CODE_BELONG_BANK );
            }
        });
        et_transRemitOrgName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                ChooseOpenAcountBankFragment chooseOpenAcountBankFragment = new ChooseOpenAcountBankFragment();
                Bundle args = new Bundle();
                args.putParcelable("bank", mCurrentSelectBankEntity);
                chooseOpenAcountBankFragment.setArguments(args);
                startForResult(chooseOpenAcountBankFragment, REQUEST_CODE_OPEN_BANK);
            }
        });

        //转账方式监听
        et_executeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] tranwayString={"实时","普通","次日到账"};
                String[] tranwayString2={"实时","次日到账"};
                dl_transWaySelect.setListData(
                        Arrays.asList(isBocTrans()?tranwayString2:tranwayString));
                dl_transWaySelect.show();
            }
        });
        dl_transWaySelect.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
            @Override
            public void onSelect(int position, String model) {
                if (!model.equals(et_executeType.getChoiceTextContent())){
                    et_executeType.setChoiceTextContent(model);
//                    if (null!=payeeSelectedBean ){
//                        if((!model.equals("实时")&&payeeSelectedBean.getBocFlag().equals("3")) ||(model.equals("实时")&&!payeeSelectedBean.getBocFlag().equals("0"))){
//                            view_savePayee.setVisibility(View.VISIBLE);
//                            cb_savePayee.setChecked(true);
//                        }else{
////                            setSavePayeeVisibility();
//                        }
//                    }
//                if (!StringUtils.isEmptyOrNull(executeTypeName)&&!model.equals(executeTypeName)){
//                    if ((model.equals("普通")&&executeTypeName.equals("次日到账"))||(model.equals("次日到账")&&executeTypeName.equals("普通"))){
//
//                    }else{
//                        view_savePayee.setVisibility(View.VISIBLE);
//                        cb_savePayee.setChecked(true);
//                    }
//                }

//                if (!model.equals(executeTypeName)){
                    switch (model){
                        case "实时":
                            executeType="0";
                            executeTypeName="实时";
//                        executeDate="";
//                        if (!isRealtimeBank(payeeBankName)){
                            if (et_transRemitOrgName.getVisibility()!=View.GONE){
                                et_transRemitOrgName.setVisibility(View.GONE);
                                line_org.setVisibility(View.GONE);
                            }

                            if (!isRealtimeBank(et_transRemiPayeebank.getChoiceTextContent())){
                                et_transRemiPayeebank.setChoiceTextContent("");
                                et_transRemitOrgName.setChoiceTextContent("");
                            }

                            break;
                        case "普通":
                            executeType="0";
                            executeTypeName="普通";
//                        executeDate="";
                            if (et_transRemitOrgName.getVisibility()!=View.VISIBLE){
                                et_transRemitOrgName.setVisibility(View.VISIBLE);
                                line_org.setVisibility(View.VISIBLE);
                            }

                            if (null==isHotBank(et_transRemiPayeebank.getChoiceTextContent())){
                                et_transRemiPayeebank.setChoiceTextContent("");
                                et_transRemitOrgName.setChoiceTextContent("");
                            }

                            break;
                        case "次日到账":
                            executeType="1";
                            executeTypeName="次日到账";
                            executeDate=getTheNextDay();
                            if (et_transRemiPayeebank.getVisibility()==View.VISIBLE&&!et_transRemiPayeebank.getChoiceTextContent().equals("中国银行")){
                                if (et_transRemitOrgName.getVisibility()!=View.VISIBLE){
                                    et_transRemitOrgName.setVisibility(View.VISIBLE);
                                    line_org.setVisibility(View.VISIBLE);
                                }
                            }

                            if (null==isHotBank(et_transRemiPayeebank.getChoiceTextContent())){
                                et_transRemiPayeebank.setChoiceTextContent("");
                                et_transRemitOrgName.setChoiceTextContent("");
                            }
                            break;
                    }
                }
//                }
                setSavePayeeView();
                dl_transWaySelect.dismiss();
            }
        });

        /**
         * 手机号实时格式化
         */

        et_transRemitPayeePhone1.setClearEditTextWatcherListener(new EditClearWidget.ClearEditTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //            实时格式化手机号方法调用
//                String str=s.toString();
                Editable editable=et_transRemitPayeePhone1.getContentEditText().getEditableText();
                int len=editable.length();
                if (len<=13){
                    PhoneNumberFormat.onEditTextChanged(s.toString().trim(), start, before, et_transRemitPayeePhone1.getContentEditText());
                }else{
                    int selEndIndex= Selection.getSelectionEnd(editable);
                    String str=editable.toString();
                    String newString=str.substring(0,13);
                    et_transRemitPayeePhone1.setEditWidgetContent(NumberUtils.formatMobileNumber(newString));
//                    PhoneNumberFormat.onEditTextChanged(newString, start, before, et_transRemitPayeePhone1.getContentEditText());
                    editable=et_transRemitPayeePhone1.getContentEditText().getEditableText();
                    if (selEndIndex>editable.length()){
                        selEndIndex=editable.length();
                    }
                    Selection.setSelection(editable,selEndIndex);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                transPayeeMobile=s.toString().replace(" ","");
                transPayeeMobile=et_transRemitPayeePhone1.getEditWidgetContent().replace(" ","").trim();
//                setSavePayeeView();
//                setSavePayeeVisibility(true);//ab12
                setSavePayeeView();
            }
        });
        et_transRemitPayeePhone1.setEditClearWidgetOnFocusListener(new EditClearWidget.EditClearWidgetOnFocusCallback(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    transPayeeMobile=et_transRemitPayeePhone1.getEditWidgetContent().replace(" ","").trim();
            }
        });

        et_transRemitPayeePhone1.setRightImageCallBack(new EditClearWidget.EditRightImageOnClick(){
            //打开手机联系人查询手机号
            @Override
            public void onClick() {
                try{
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                }catch (Exception e){
                    showErrorDialog(e.getMessage());
                }
            }
        });

        et_transRemitMessage1.setClearEditTextWatcherListener(new EditClearWidget.ClearEditTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                try {
                    byte[] bt = str.getBytes("gbk");
                    int n = 0;
                    if (bt.length>20) {
                        for (int x = 20 - 1; x >= 0; x--) {
                            if (bt[x] < 0) {
                                n = n + 1;
                            } else {
                                break;
                            }
                        }
                        if (n % 2 == 1) {
                            et_transRemitMessage1.setEditWidgetContent( new String(bt, 0, 20 - 1, "gbk"));
                            et_transRemitMessage1.getContentEditText().setSelection(et_transRemitMessage1.getContentEditText().length());
                        } else{
                            et_transRemitMessage1.setEditWidgetContent( new String(bt, 0, 20, "gbk"));
                            et_transRemitMessage1.getContentEditText().setSelection(et_transRemitMessage1.getContentEditText().length());
                        }
                    }
                } catch (Exception e) {
                }
            }
        });

        // 限额弹框
        quotaWarningDailog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {
                quotaWarningDailog.dismiss();
            }

            @Override
            public void onRightBtnClick(View view) {
                showLoadingDialog();
//                getPresenter().getConverIdandSaftyFator(getServiceId(transtype));
                queryVerifyFunc(conversationId,factorIdToVerify);
                quotaWarningDailog.dismiss();
            }
        });

        /**
         * 转账交易确认，调用预交易
         */
        bt_transRemitNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transPayeeMobile=et_transRemitPayeePhone1.getEditWidgetContent().replace(" ","").trim();
                getOtherFocused();
                if (!checkInputData()){
                    return;
                }
                setTransType();//设置转账类型
                if (!checkPayeeAccNo()){
                    return;
                }
                if (transtype==TRANS_TO_LINKED){ //关联账户转账，不需要预交易
                    verifyInfoModel = new TransRemitVerifyInfoViewModel();
                    verifyInfoModel.setAvailableBalance(availableBalance);
                    verifyInfoModel.setAmount(transMoneyAmount);
                    verifyInfoModel.setAccountNumber(payerAccountSelected.getAccountNumber());
                    verifyInfoModel.setFromAccountId(payerAccountSelected.getAccountId());
                    verifyInfoModel.setAccountIbkNum(payerAccountSelected.getAccountIbkNum());
                    verifyInfoModel.setCurrency(transCurrency);
                    verifyInfoModel.setExecuteType(executeType);
                    verifyInfoModel.setExecuteTypeName(executeTypeName);
//                    verifyInfoModel.setExecuteDate(executeDate);
                    verifyInfoModel.setRemark(et_transRemitMessage1.getEditWidgetContent().trim());
                    verifyInfoModel.setPayeeName(transPayeeName);
                    verifyInfoModel.setIsAppointed("0");
                    if ((ApplicationConst.ACC_TYPE_ZHONGYIN.equals(payerAccountSelected.getAccountType())||ApplicationConst.ACC_TYPE_GRE.equals(payerAccountSelected.getAccountType())
                            ||ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(payerAccountSelected.getAccountType())) &&isPayeeAccCcrd){
                        verifyInfoModel.setCashRemit("00");
                    }else{
                        verifyInfoModel.setCashRemit(transCashRemit);
                    }
                    verifyInfoModel.setAccountIbkNum(payerAccountSelected.getAccountIbkNum());
                    verifyInfoModel.setFunctionFrom(functionId);
                    if (ApplicationConst.ACC_TYPE_BOCINVT.equals(payerAccountSelected.getAccountType())){
//                        verifyInfoModel.setPayeeBankName("中国银行");
                        verifyInfoModel.setPayeeBankName(payeeBankName);
//                        verifyInfoModel.setPayeeBankIbkNum(payeeLinkedAcc.getAccountIbkNum());
                        verifyInfoModel.setPayeeBankIbkNum(payeeSelectedBean.getAccountIbkNum());
//                        verifyInfoModel.setPayeeBankNum("");
//                        verifyInfoModel.setPayeeBankNum(payeeLinkedAcc.getBankNum());
                        verifyInfoModel.setPayeeBankNum(payeeSelectedBean.getBankNum());
//                        verifyInfoModel.setPayeeActno(payeeLinkedAcc.getAccountNumber());
                        verifyInfoModel.setPayeeActno(payeeSelectedBean.getAccountNumber());
//                        verifyInfoModel.setToAccountId(String.valueOf(payeeLinkedAcc.getAccountId()));
                        verifyInfoModel.setToAccountId(String.valueOf(payeeSelectedBean.getAccountId()));
                    }else{
                        verifyInfoModel.setPayeeBankName(payeeBankName);
                        verifyInfoModel.setPayeeActno(payeeSelectedBean.getAccountNumber());
                        verifyInfoModel.setPayeeBankIbkNum(payeeSelectedBean.getAccountIbkNum());
                        verifyInfoModel.setPayeeBankNum(payeeSelectedBean.getBankNum());
                        verifyInfoModel.setToAccountId(payeeSelectedBean.getAccountId());
                    }
                    if (ApplicationConst.ACC_TYPE_BOCINVT.equals(payerAccountSelected.getAccountType())){
                        //跳转，不需要费用试算
                        hideSoftInput();
                        start(TransRemitVerifyFragment.getInstance(transtype, verifyInfoModel, payerAccountSelected));
                    }else{
                        showLoadingDialog(false);
                        getTransBocCommissionCharge(verifyInfoModel);
                    }
                }
                else{
                    showLoadingDialog(false);
                    getPresenter().getConverIdandSaftyFator(getServiceId(transtype));
                }
            }
        });
    }

    //跳转到收款人选择页面
    public void jumpToPayeeSelectFragment(){
        TransPayeeSelectFragment payeeSelectFragement= new TransPayeeSelectFragment();
        Bundle bundle =new Bundle();
        bundle.putString(TransPayeeSelectFragment.DO_WHAT,TransPayeeSelectFragment.ACTION_JUST_CHOOSE_ACCOUNT);
        bundle.putParcelable(ACCOUNT_EXIST,getLatestPayerAndPayeeInfo());
        bundle.putParcelableArrayList(LINKED_ACCOUNT_TYPE,accountBeanList);
        bundle.putParcelableArrayList(PAYEE_ACCOUNT_TYPE,mPayeeEntityListAfterMerge);
        payeeSelectFragement.setArguments(bundle);
        startForResult(payeeSelectFragement,REQUEST_CODE_SELECT_PAYEE_ACCOUNT);
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if(resultCode ==RESULT_OK){
            if (requestCode == REQUEST_CODE_BELONG_BANK) { // 选择所属银行
                mCurrentSelectBankEntity=new BankEntity();
                BankEntity mCurrentSelectBankEntity2= data.getParcelable(ChooseBankFragment1.KEY_BANK);
                BeanConvertor.toBean(mCurrentSelectBankEntity2,mCurrentSelectBankEntity);
                mCurrentSelectBankEntity.setHot(mCurrentSelectBankEntity2.isHot());
                if (StringUtils.isEmptyOrNull(mCurrentSelectBankEntity.getBankName())){
                    if ("中国银行".equals(mCurrentSelectBankEntity.getBankAlias())){
                        //有时候银行实体解析出来，中国银行bankname字段偶尔是null
                        mCurrentSelectBankEntity.setBankName("中国银行");
                    }else{
                        showErrorDialog("所选择的银行信息错误,请重新选择");
                        return;
                    }
                }
                if(!mCurrentSelectBankEntity.getBankName().equals(payeeBankName)){
                    payeeBankOrgName="";
                    payeeBankCnapsCodeOrg="";
                    et_transRemitOrgName.setChoiceTextContent("");
                    payeeBankName=mCurrentSelectBankEntity.getBankName();
                    if (null!=payeeSelectedBean){
                        if ("1".equals(payeeSelectedBean.getIsAppointed())){
                            //中行定向收款人改变了银行算改变了人，跨行定向收款人改变了收款银行不算改变人
                            if  ("1".equals(payeeSelectedBean.getBocFlag())){
                                payeeSelectedBean=null;
                                isPayeeAccCcrd=false;
                            }
                        }else{
                            payeeSelectedBean=null;
                            isPayeeAccCcrd=false;
                        }
                    }
                    setSavePayeeView();
                }
                payeeBankName=mCurrentSelectBankEntity.getBankName();
                if (null==mCurrentSelectBankEntity.getBankCode()&&!"中国银行".equals(payeeBankName)){
                    mCurrentSelectBankEntity=isHotBank(mCurrentSelectBankEntity.getBankName());
                }
                payeeBankCnapsCode =mCurrentSelectBankEntity.getBankCode();
                payeeBankCode=mCurrentSelectBankEntity.getBankBtp();
                et_transRemiPayeebank.setChoiceTextContent(mCurrentSelectBankEntity.isHot() ? mCurrentSelectBankEntity.getBankAlias()
                        : mCurrentSelectBankEntity.getBankName());
                if(et_transRemiPayeebank.getChoiceTextContent().equals(getResources().getString(R.string.trans_boc))){
                    if (et_executeType.getChoiceTextContent().equals("普通")){
                        et_executeType.setChoiceTextContent("");
                    }
                }else {

                }
                if(payeeBankName.contains("江苏银行")){
                    mCurrentSelectBankEntity.setBankName("江苏银行");
                }
            }
            if (requestCode == REQUEST_CODE_OPEN_BANK) { // 选择开户网点
                mCurrentOpenBankBean = data.getParcelable("openBank");
                if (!mCurrentOpenBankBean.getBankName().equals(et_transRemitOrgName.getChoiceTextContent())){
//                    setSavePayeeVisibility(true);//ab12
                    setSavePayeeView();
                }
                et_transRemitOrgName.setChoiceTextContent(mCurrentOpenBankBean.getBankName());
                payeeBankOrgName=mCurrentOpenBankBean.getBankName();
                et_transRemitOrgName.setChoiceTextContent(payeeBankOrgName);
                payeeBankCnapsCodeOrg =mCurrentOpenBankBean.getCnapsCode();
//                setSavePayeeView();??
            }
            if (requestCode == REQUEST_CODE_SCAN_CARD) { // 扫描银行卡
                cardNumber = data.getString("cardNumber").replace(" ","").trim();
//                scanCardDialog.setNoticeContent(NumberUtils.formatCardNumber2(cardNumber));
                scanCardDialog.setNoticeContent(cardNumber);
                scanCardDialog.show();
            }
        }
        if (resultCode== TransPayerSelectFragment.RESULT_CODE_SELECT_ACCOUNT){
            if (requestCode == TransPayerSelectFragment.REQUEST_CODE_SELECT_ACCOUNT) {
//                String accFalg;//这个保存是什么类型的账户做的操作
                payerAccountSelected = data.getParcelable(ACCOUNT_SELECT);
//              这个地方成功了再去返显数据，报错要在账户选择页面。
                isPayerAccSelected =true;
                String acctype=data.getString(TYPE_ACC);
//                et_tansPayerAccout.setChoiceTextContent(payerAccountSelected.getAccountNumber());
                et_tansPayerAccout.setChoiceTextContent(NumberUtils.formatCardNumberStrong(payerAccountSelected.getAccountNumber()));
                if (acctype.equals(TYPE_NORMAL)){
                    AccountQueryAccountDetailResult tmpAccDetialResult=data.getParcelable(acctype);
                    dealWithBanlanceResult(tmpAccDetialResult);
                }else if(acctype.equals(TYPE_CRCD)){//信用卡
                    CrcdQueryAccountDetailResult result=data.getParcelable(acctype);
                    dealCrcdQueryResult(result);
                }else if(acctype.equals(TYPE_OFA)){//190理财账户
                    OFAAccountStateQueryResult result=data.getParcelable(acctype);
                    dealwithOFAQueryResult(result);
                    AccountQueryAccountDetailResult tmpAccDetialResult1=data.getParcelable(TYPE_NORMAL);
                    dealWithBanlanceResult(tmpAccDetialResult1);
                }else if(acctype.equals(TYPE_ECARD)){//电子卡
                    CardQueryBindInfoResult result=data.getParcelable(acctype);
                    dealEcardQureyResult(result);
                    AccountQueryAccountDetailResult tmpAccDetialResult1=data.getParcelable(TYPE_NORMAL);
                    dealWithBanlanceResult(tmpAccDetialResult1);
                }else{
                }
                if(!ApplicationConst.ACC_TYPE_BOCINVT.equals(payerAccountSelected.getAccountType())){
                    payeeLinkedAcc=null;
                }
//                queryAccoutBalance(payerAccountSelected);
            }
        }

        // 判断是哪一种转账类型，需要通过转账金额和收款人类型共同确定 ,金额5万，是否是定向 ，开户行
        if (resultCode==TransPayeeSelectFragment.RESULT_CODE_SELECT_ACCOUNT_SUCCESS){
            if (requestCode==REQUEST_CODE_SELECT_PAYEE_ACCOUNT){
//                payeeNameDataAdapter.setRecentPayeeList(null); //选择收款人回来
                payeeSelectedBean=data.getParcelable(TransPayeeSelectFragment.KEY_PAYEE_ENTITY);
                isPayeeAccCcrd=false;
                if (payeeSelectedBean.isLinked()){//如果是关联账户
                    if(null== getLinkedAccByAccId(payeeSelectedBean.getAccountId())){
                        return;
                    }
                    payeeLinkedAccCurrencyList=data.getStringArrayList(PAYEE_CURRENCY_LIST);
                    payeeLinkedAccCashRemitList=data.getStringArrayList(PAYEE_CASHREMIT_LIST);
                    String linedPayeeAccType= getLinkedAccByAccId(payeeSelectedBean.getAccountId()).getAccountType();
                    if (!StringUtils.isEmptyOrNull(transCurrency)&&!StringUtils.isEmptyOrNull(transCashRemit)&&!StringUtils.isEmptyOrNull(linedPayeeAccType)){
                        if(linedPayeeAccType.equals(ApplicationConst.ACC_TYPE_GRE) ||linedPayeeAccType.equals(ApplicationConst.ACC_TYPE_SINGLEWAIBI)
                                ||linedPayeeAccType.equals(ApplicationConst.ACC_TYPE_ZHONGYIN)){
                            isPayeeAccCcrd=true;
                        }else{
                            isPayeeAccCcrd=false;
                        }
//                            if(linedPayeeAccType.equals(ApplicationConst.ACC_TYPE_ORD)){
//                                if (transCurrency.equals(payeeLinkedAccCurrencyList.get(0))&&transCashRemit.equals(payeeLinkedAccCashRemitList.get(0))){
//                                }else{
//                                    showErrorDialog("收款账户不支持该币种转账，请修改");}
//                            }
                    }
                }else{
                    if(!StringUtils.isEmptyOrNull(transCurrency)&&!ApplicationConst.CURRENCY_CNY.equals(transCurrency)){
                        showErrorDialog(getResources().getString(R.string.trans_error_currency_wrong));
                    }
                }
                //在张海星页面提示，不要返回来,
                if (null!=payerAccountSelected&&payeeSelectedBean.getAccountNumber().equals(payerAccountSelected.getAccountNumber())){
                    showErrorDialog(getResources().getString(R.string.trans_error_payee_accountno_is_same));
                    return;
                }
                //在张海星页面提示，不要返回来
                if (!payeeSelectedBean.isLinked() && !StringUtils.isEmptyOrNull(transCurrency)&&!ApplicationConst.CURRENCY_CNY.equals(transCurrency)){
                    showErrorDialog(getResources().getString(R.string.trans_error_currency_wrong));
//                   return;
                }
                hideSoftInput();
                if (null!=payerAccountSelected && isPayeeAccCcrd){
                    getCurrencyToShowList();
                    setBalanceView();
                }

                if (null==payeeSelectedBean){
                    showErrorDialog(getResources().getString(R.string.trans_error_select_another_payee_account));
                    return;
                }else{
                    setViewAfterPayeeSelected(payeeSelectedBean);
                }
            }
        }
    }
    private  boolean isPayeeAccCcrd=false;
    private ArrayList<String> combineCurrencyList =new ArrayList<>();//如果收款账户是信用卡，取收付款账户币种并集
    private ArrayList<String> combineCashRemitList =new ArrayList<>();//如果收款账户是信用卡，取收付款账户币种并集
    private ArrayList<BigDecimal> combineCurrencyBalanceList =new ArrayList<>();//如果收款账户是信用卡，取收付款账户币种并集
    private ArrayList<String> combineLoanFlagList =new ArrayList<>();//余额标志位，信用卡的时候用

    //如果收款账户是信用卡需要合并币种展示
    public void getCurrencyToShowList(){
        combineCurrencyList.clear();
        combineCurrencyBalanceList.clear();
        combineCashRemitList.clear();
        combineLoanFlagList.clear();
        if (isPayeeAccCcrd){
            if( null==payeeSelectedBean||!payeeSelectedBean.isLinked()){
//                showErrorDialog("isPayeeAccCcrd2");
                return;
           }
            if (null==payeeLinkedAccCurrencyList||payeeLinkedAccCurrencyList.size()==0){
//                showErrorDialog("网络错误，请重新选择收款账户");
                return;
            }
        }
        if (isPayeeAccCcrd){
            combineCurrencyList.addAll(payerAccCurrencyList);
            combineCurrencyList.retainAll(payeeLinkedAccCurrencyList);//交集
            if (combineCurrencyList.size()==0) {
//                showErrorDialog("币种信息错误");
                return;
            }
            //取交集
            for (int i = 0; i< this.payerAccCurrencyList.size(); i++){
                if (combineCurrencyList.contains(this.payerAccCurrencyList.get(i))){
                    combineCashRemitList.add(payerAccCashRemitList.get(i));
                    combineCurrencyBalanceList.add(payerAccBalanceList.get(i));
                    combineLoanFlagList.add(payerAccLoanFlagList.get(i));
                 }
            }
            for(int i=0;i<combineCurrencyList.size();i++){
                if (combineCurrencyList.get(i).equals(transCurrency)&&combineCashRemitList.get(i).equals(transCashRemit)){
                    balanceLoanFlag =combineLoanFlagList.get(i);
                    BigDecimal amount=combineCurrencyBalanceList.get(i);
//                payerAccBalance=amount.doubleValue();
                    availableBalance=amount;
                    return;
                }
            }
            //找不到就取第一个
            transCurrency= combineCurrencyList.get(0);
            transCashRemit=combineCashRemitList.get(0);
            balanceLoanFlag =combineLoanFlagList.get(0);
            BigDecimal amount=combineCurrencyBalanceList.get(0);
//                payerAccBalance=amount.doubleValue();
            availableBalance=amount;
            return;

        }else{
            combineCurrencyList.addAll(payerAccCurrencyList);
            combineCurrencyBalanceList.addAll(payerAccBalanceList);
            combineCashRemitList.addAll(payerAccCashRemitList);
            combineLoanFlagList.addAll(payerAccLoanFlagList);
            if (combineCurrencyList.size()==0){
                showErrorDialog("币种信息错误");
                return;
            }
            for(int i=0;i<combineCurrencyList.size();i++){
                if (combineCurrencyList.get(i).equals(transCurrency)&&combineCashRemitList.get(i).equals(transCashRemit)){
                    transCurrency= combineCurrencyList.get(i);
                    transCashRemit=combineCashRemitList.get(i);
                    balanceLoanFlag =combineLoanFlagList.get(i);
                    BigDecimal amount=combineCurrencyBalanceList.get(i);
//                    payerAccBalance=amount.doubleValue();
                    availableBalance=amount;
                    return;
                }
            }
            //如果更换了付款账户，且币种不在付款账户币种列表里面
                transCurrency=combineCurrencyList.get(0);
                transCashRemit= combineCashRemitList.get(0);
                balanceLoanFlag =combineLoanFlagList.get(0);
                BigDecimal amount=combineCurrencyBalanceList.get(0);
//                payerAccBalance=amount.doubleValue();
                availableBalance=amount;
            return;
        }
    }

    private ArrayList<String> payeeLinkedAccCurrencyList;//收款账户是关联账户时，查询回来的币种
    private ArrayList<String> payeeLinkedAccCashRemitList;
    public void setViewAfterPayeeSelected(PayeeEntity payeeSelectedBean){
//        et_transRemitPayeeAccNo1.setEditWidgetContent("");
        et_transRemiPayeebank.setChoiceTextContent("");
        payeeBankName="";
        et_transRemitOrgName.setChoiceTextContent("");
        payeeBankOrgName="";
        et_transRemitPayeePhone1.setEditWidgetContent("");
        transPayeeMobile="";
        selectedPayeeBocFlag =payeeSelectedBean.getBocFlag();
        transPayeeMobile=payeeSelectedBean.getMobile();
        if (!StringUtils.isEmptyOrNull(transPayeeMobile)){
            transPayeeMobile=transPayeeMobile.replace(" ","").trim();
        }
//        accountId=payeeSelectedBean.getAccountId();
        transPayeeAccountNumber=payeeSelectedBean.getAccountNumber();
        transPayeeAccountNumberBefore=transPayeeAccountNumber;
        et_transRemitPayeeAccNo1.setEditWidgetContent(transPayeeAccountNumber);
        payeeBankName=payeeSelectedBean.getBankName();
        //展示银行名和开户网点名称的逻辑
        if (payeeSelectedBean.isLinked()){
            transtype=TRANS_TO_LINKED;
            transPayeeName=context1.getUser().getCustomerName();
            et_transRemitPayeeName.setText(transPayeeName);
            et_transRemiPayeebank.setVisibility(View.GONE);
            line_bank.setVisibility(View.GONE);
            et_transRemitOrgName.setVisibility(View.GONE);
            line_org.setVisibility(View.GONE);
            et_transRemitPayeePhone1.setVisibility(View.GONE);
            line_mobile.setVisibility(View.GONE);
            setExecuteTypeVisible(false);
        }else if("1".equals(selectedPayeeBocFlag)){
            setExecuteTypeVisible(true);
            transPayeeName=payeeSelectedBean.getAccountName();
            et_transRemitPayeeName.setText(transPayeeName);
            et_transRemiPayeebank.setVisibility(View.VISIBLE);
            line_bank.setVisibility(View.VISIBLE);
            payeeBankName=getResources().getString(R.string.trans_boc);
            et_transRemiPayeebank.setChoiceTextContent(getResources().getString(R.string.trans_boc));
            et_transRemitOrgName.setChoiceTextContent("");
            et_transRemitOrgName.setVisibility(View.GONE);
            if (et_executeType.getChoiceTextContent().equals("普通")){
                et_executeType.setChoiceTextContent("实时");
                executeTypeName="实时";
                executeType="0";
            }
            line_org.setVisibility(View.GONE);
            et_transRemitPayeePhone1.setVisibility(View.VISIBLE);
            line_mobile.setVisibility(View.VISIBLE);
            et_transRemitPayeePhone1.setEditWidgetContent(NumberUtils.formatMobileNumber(transPayeeMobile));

        }else  {
            setExecuteTypeVisible(true);
            transPayeeName=payeeSelectedBean.getAccountName();
            et_transRemitPayeeName.setText(transPayeeName);
            if ("3".equals(selectedPayeeBocFlag)){//实时收款人,返回的cnapscode 就是收款银行cnapscode
//                payeeBankOrgName = payeeSelectedBean.getAddress();//2016-11-2 屏蔽
                payeeBankCnapsCode = payeeSelectedBean.getCnapsCode();
//                if("".equals(executeTypeName)||"实时".equals(executeTypeName)){
//                    et_transRemitOrgName.setVisibility(View.GONE);
//                    line_org.setVisibility(View.GONE);
//                }else{
//                    et_transRemitOrgName.setVisibility(View.VISIBLE);
////                    et_transRemitOrgName.setChoiceTextContent("");
//                    line_org.setVisibility(View.VISIBLE);
//                }
            }
            if ("0".equals(selectedPayeeBocFlag)){//普通收款人，返回的cnapscode 是开户行cnapscode
                payeeBankCnapsCodeOrg = payeeSelectedBean.getCnapsCode();
                payeeBankOrgName = payeeSelectedBean.getAddress();
                if (!StringUtils.isEmptyOrNull(payeeBankName)&&payeeBankOrgName.equals(payeeBankName)){
                    payeeBankOrgName="";
                }
                et_transRemitOrgName.setChoiceTextContent(payeeBankOrgName);

            }
            et_transRemitPayeePhone1.setVisibility(View.VISIBLE);
            line_mobile.setVisibility(View.VISIBLE);
            et_transRemitPayeePhone1.setEditWidgetContent(NumberUtils.formatMobileNumber(transPayeeMobile));
            et_transRemiPayeebank.setVisibility(View.VISIBLE);
            line_bank.setVisibility(View.VISIBLE);
            payeeBankCode = payeeSelectedBean.getBankCode();

            BankEntity oneBean=isHotBank(payeeBankName);//
            if (null!=oneBean) {
                payeeBankCode = oneBean.getBankBtp();
                payeeBankCnapsCode = oneBean.getBankCode();
                et_transRemiPayeebank.setChoiceTextContent(oneBean.getBankAlias());
            }else {
                if ("3".equals(payeeSelectedBean.getBocFlag())) {
                    if (executeTypeName.equals("实时")) {
                        if(isRealtimeBank(payeeBankName)){
                            et_transRemiPayeebank.setChoiceTextContent(payeeBankName);
                            payeeBankCnapsCode=payeeSelectedBean.getCnapsCode();
                        }else{
                            et_transRemiPayeebank.setChoiceTextContent("");
                            payeeBankCnapsCode="";
                            et_transRemitOrgName.setChoiceTextContent("");
                        }
                    }else{
                        et_transRemiPayeebank.setChoiceTextContent("");
                        payeeBankCnapsCode="";
                        et_transRemitOrgName.setChoiceTextContent("");
                    }
                }else if("0".equals(payeeSelectedBean.getBocFlag())){
                    if (executeTypeName.equals("实时")) {
                        et_transRemiPayeebank.setChoiceTextContent("");
                        et_transRemitOrgName.setChoiceTextContent("");
                        payeeBankCnapsCode="";
                    }else{
                        if(payeeBankName.equals("其他银行")){
                            et_transRemiPayeebank.setChoiceTextContent(payeeBankName);
                            et_transRemitOrgName.setChoiceTextContent(payeeBankOrgName);
                            et_transRemitOrgName.setVisibility(View.VISIBLE);
                            payeeBankCode="OTHER";
                        }else{
                            et_transRemiPayeebank.setChoiceTextContent("");
                            et_transRemitOrgName.setChoiceTextContent("");
                            payeeBankCnapsCode="";
                        }
                    }
                }
            }

//            if (StringUtils.isEmptyOrNull(payeeBankCode) ||StringUtils.isEmptyOrNull(payeeBankName)) {
////              这里不是取bankcode，而是bankBtp
//                BankEntity oneBean=isHotBank(payeeBankName);
//                if (null!=oneBean){
//                    payeeBankCode = oneBean.getBankBtp();
//                    payeeBankCnapsCode=oneBean.getBankCode();
//                    et_transRemiPayeebank.setChoiceTextContent(oneBean.getBankAlias());
//                    if ("3".equals(payeeSelectedBean.getBocFlag())){
//                        if (executeTypeName.equals("实时")){
//                            et_transRemiPayeebank.setChoiceTextContent(payeeBankName);
//                        }
//                    }
//
//
//                }else{
//                    if (!StringUtils.isEmptyOrNull(payeeBankName)&&payeeBankName.contains("其他银行")){
//                        if (!executeType.equals("实时")){
//                            et_transRemiPayeebank.setChoiceTextContent(payeeBankName);
//                            payeeBankCode="OTHER";//如果收款银行是“其他银行”，那么银行号设为other
//                            et_transRemitOrgName.setChoiceTextContent(payeeBankOrgName);
//                        }else{
//                            et_transRemiPayeebank.setChoiceTextContent("");
//                            payeeBankCode="";
//                        }
//
//                    }else{
//                        //此处处理非常用银行，但是获取不到bankcode的情况,不显示所属银行的内容，让用户去手选银行出来。不然页面无法得到
////                        if(!StringUtils.isEmptyOrNull(payeeBankCnapsCode)){
//////                            et_transRemiPayeebank.setChoiceTextContent(payeeBankName);
//////                            payeeBankCode="OTHER";
////                        }else{
//                        if (executeType.equals("实时")){
//                            et_transRemiPayeebank.setChoiceTextContent(payeeBankName);
//                            payeeBankCode="OTHER";//如果收款银行是“其他银行”，那么银行号设为other
//                            et_transRemitOrgName.setChoiceTextContent(payeeBankOrgName);
//                        }else{
//                            et_transRemiPayeebank.setChoiceTextContent("");
//                            payeeBankCode="";
//                        }
//                            et_transRemiPayeebank.setChoiceTextContent("");
//                            et_transRemitOrgName.setChoiceTextContent("");
////                        }
//                    }
//                }
//            }else{
//                et_transRemiPayeebank.setChoiceTextContent(payeeBankName);
//            }
            mCurrentSelectBankEntity=new BankEntity();
            if(null!=payeeBankName&&payeeBankName.contains("江苏银行")){
                BankEntity oneBean1=isHotBank("江苏银行");
                mCurrentSelectBankEntity.setBankCode(oneBean1.getBankBtp());
                mCurrentSelectBankEntity.setBankName("江苏银行");
            }else{
                mCurrentSelectBankEntity.setBankName(payeeBankName);
                mCurrentSelectBankEntity.setBankBtp(payeeBankCode);
            }
        }
        setSavePayeeView();
    }

    /**
     * 是否是常用银行
     * @param bankname
     * @return
     */
    public BankEntity  isHotBank(String bankname){
        for (BankEntity oneBean : hotBankEntityList) {
            if (null != oneBean.getBankName() && oneBean.getBankName().equals(bankname) ||
                    null != oneBean.getBankAlias() && oneBean.getBankAlias().equals(bankname)
                    || null != oneBean.getBankBpm() && oneBean.getBankBpm().equals(bankname)) {
                BankEntity oneBean1=new BankEntity();//2016-11-10
                BeanConvertor.toBean(oneBean,oneBean1); //2016-11-10
                return oneBean1;
            }
        }
        return null;
    }
    /**
     * 是否支持二代支付银行,实时
     * @param bankname
     * @return
     */
    public boolean  isRealtimeBank(String bankname) {
        if (!StringUtils.isEmptyOrNull(bankname)) {
            if (bankname.contains("中国农业发展银行") || bankname.contains("国家开发银行") || bankname.contains("中国进出口银行") ||bankname.contains("其他银行")) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT)://电话本
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        final List<String> phoneList = PhoneNumberFormat.insertPhonenumber1(mActivity, data);
                        List<String> phoneNumList = new ArrayList<String>(); // 手机号码列表
                        // 截取字符串,构建一个纯的号码列表
                        for (String phone : phoneList) {
                            phoneNumList.add(PhoneNumberFormat.getPhoneNum(phone));
                        }
                        transPayeeMobile = "";
                        if(phoneList != null) {
                            if (phoneList.size() == 1) {
                                transPayeeMobile = phoneList.get(0);
                                et_transRemitPayeePhone1.setEditWidgetContent(PhoneNumberFormat.getPhoneNum(transPayeeMobile));

                                // 判断是否需要带回name
                                String name =et_transRemitPayeeName.getText().toString().trim();
                                if(TextUtils.isEmpty(name)) {
                                    et_transRemitPayeeName.setText(PhoneNumberFormat.getPhoneName(transPayeeMobile));
                                }
                                return;
                            }
                            if (phoneList.size() > 1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                int size = phoneList.size();
                                builder.setTitle("请选择一个号码").setItems(phoneNumList.toArray(new String[size]), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int position = which;
                                        transPayeeMobile = phoneList.get(position);
                                        et_transRemitPayeePhone1.setEditWidgetContent(PhoneNumberFormat.getPhoneNum(transPayeeMobile));
                                        // 判断是否需要带回name
                                        String name =et_transRemitPayeeName.getText().toString().trim();
                                        if(TextUtils.isEmpty(name)) {
                                            et_transRemitPayeeName.setText(PhoneNumberFormat.getPhoneName(transPayeeMobile));
                                        }
                                    }
                                }).create().show();
                            }
                        }
                    }
                }
                break;
        }
    }
    @Override
    protected String getTitleValue() {
        return getString(R.string.trans_remit2);
    }

    private String factorIdToVerify;//预交易使用的安全工具
    private String conversationId;
    @Override
    public void getConverIdandSaftyFatorSuccess(PsnGetSecurityFactorResult factorResult,String conversationId) {
//        closeProgressDialog();
        if (null==factorResult||StringUtils.isEmptyOrNull(conversationId)){
            showErrorDialog("网络错误，请重新登陆");
            return;
        }
        this.conversationId=conversationId;
        CombinListBean factorbean=SecurityVerity.getInstance(getActivity()).getDefaultSecurityFactorId(new SecurityFactorModel(factorResult));//安全认证工具
        List<CombinListBean> listBeen=SecurityVerity.getInstance().getFactorSecurity().getCombinList();
        factorIdToVerify=factorbean.getId();//这里是默认的ID
        if (null==listBeen||listBeen.size()==0){
//            showErrorDialog("安全工具错误");
            return;
        }
        if("1".equals(executeType)){
            if (haveCA(listBeen)){
                if (transtype.equals(TRANS_TO_BOC_DIR)||transtype.equals(TRANS_TO_NATIONAL_DIR)||transtype.equals(TRANS_TO_NATIONAL_REALTIME_DIR)){
                    queryVerifyFunc(conversationId,factorIdToVerify);//有ca要上送ca，没有则默认
                }else{
                    queryTransQuota();
                }
            }else{
                showErrorDialog("您的安全工具没有中银e盾，无法预约转账，请先到营业网点办理中银e盾");
                return;
            }
        }else{
            if("96".equals(factorIdToVerify)&&Double.valueOf(transMoneyAmount)>TRANS_REMIT_LIMIT&&listBeen.size()>1) {
                //如果金额大于5w，且默认是96，那么取第二个安全工具
                for (CombinListBean onebean:listBeen){
                   if (!"96".equals(onebean.getId())&&!"32".equals(onebean.getId())){
                        factorIdToVerify=onebean.getId();//有则取，没有就上送96
                        break;
                   }
                }
            }
            if (transtype.equals(TRANS_TO_BOC_DIR)||transtype.equals(TRANS_TO_NATIONAL_DIR)||transtype.equals(TRANS_TO_NATIONAL_REALTIME_DIR)){
                queryVerifyFunc(conversationId,factorIdToVerify);
            }else{
                queryTransQuota();
            }
        }
    }

    // 是否有CA
    public boolean haveCA(List<CombinListBean> listBeen){
        for(CombinListBean onebean:listBeen){
            if ("4".equals(onebean.getId())||"12".equals(onebean.getId())||"36".equals(onebean.getId())){
                factorIdToVerify=onebean.getId();
                return true;
            }
        }
        return false;
    }

    public void queryVerifyFunc(String conversationId,String combinId){
        if (transtype==TRANS_TO_BOC ) {//非定向
            bocVerifyParams = new PsnTransBocTransferVerifyParams();
            bocVerifyParams.setConversationId(conversationId);
            bocVerifyParams.set_combinId(combinId);
            bocVerifyParams.setAmount(transMoneyAmount);
            bocVerifyParams.setFromAccountId(payerAccountSelected.getAccountId());
            bocVerifyParams.setPayeeActno(transPayeeAccountNumber);
            bocVerifyParams.setCurrency(ApplicationConst.CURRENCY_CNY);
            bocVerifyParams.setRemark(et_transRemitMessage1.getEditWidgetContent().trim());
            if (null!=payeeSelectedBean){
                if ("null".equals(String.valueOf(payeeSelectedBean.getPayeetId()))){
                    bocVerifyParams.setPayeeId("");
                }else{
                    bocVerifyParams.setPayeeId(String.valueOf(payeeSelectedBean.getPayeetId()));
                }
            }
            bocVerifyParams.setPayeeName(transPayeeName);
            bocVerifyParams.setPayeeMobile(transPayeeMobile);
            bocVerifyParams.setExecuteType(executeType);//0 代表立即执行
            if ("1".equals(executeType)){
                bocVerifyParams.setExecuteDate(executeDate);
            }
//            showLoadingDialog();
            getPresenter().transBocVerify(bocVerifyParams);
        }
        else  if(transtype==TRANS_TO_BOC_DIR) {//定向
            dirBocVerifyParams = new PsnDirTransBocTransferVerifyParams();
            dirBocVerifyParams.setConversationId(conversationId);
            dirBocVerifyParams.set_combinId(combinId);
            dirBocVerifyParams.setAmount(transMoneyAmount);
            dirBocVerifyParams.setFromAccountId(payerAccountSelected.getAccountId());
            dirBocVerifyParams.setCurrency(ApplicationConst.CURRENCY_CNY);
            dirBocVerifyParams.setExecuteType(executeType);//0 代表立即执行
            if ("1".equals(executeType)){
                dirBocVerifyParams.setExecuteDate(executeDate);
            }
            dirBocVerifyParams.setRemark(et_transRemitMessage1.getEditWidgetContent().trim());
            dirBocVerifyParams.setPayeeName(transPayeeName);
            dirBocVerifyParams.setPayeeActno(transPayeeAccountNumber);

            if ("null".equals(String.valueOf(payeeSelectedBean.getPayeetId()))){
                dirBocVerifyParams.setPayeeId("");
            }else{
                dirBocVerifyParams.setPayeeId(String.valueOf(payeeSelectedBean.getPayeetId()));
            }

            dirBocVerifyParams.setPayeeMobile(transPayeeMobile);
//            showLoadingDialog();
            getPresenter().transDirBocVerify(dirBocVerifyParams);
        }

        else if (transtype == TRANS_TO_NATIONAL) {//
            nationalVerifyParams = new PsnTransBocNationalTransferVerifyParams();
            nationalVerifyParams.setConversationId(conversationId);
            nationalVerifyParams.set_combinId(combinId);
            nationalVerifyParams.setAmount(transMoneyAmount);
            nationalVerifyParams.setFromAccountId(payerAccountSelected.getAccountId());
            nationalVerifyParams.setAccountIbkNum(payerAccountSelected.getAccountIbkNum());
            nationalVerifyParams.setAccountNumber(payerAccountSelected.getAccountNumber());
            nationalVerifyParams.setAccountType(payerAccountSelected.getAccountType());
            nationalVerifyParams.setPayeeActno(transPayeeAccountNumber);
            nationalVerifyParams.setCurrency(ApplicationConst.CURRENCY_CNY);
            nationalVerifyParams.setExecuteType(executeType);
            if ("1".equals(executeType)){
                nationalVerifyParams.setExecuteDate(executeDate);
            }
            nationalVerifyParams.setRemark(et_transRemitMessage1.getEditWidgetContent().trim());
            nationalVerifyParams.setPayeeName(transPayeeName);
            nationalVerifyParams.setPayeeMobile(transPayeeMobile);
            nationalVerifyParams.setPayeeId("");
            nationalVerifyParams.setCnapsCode(payeeBankCnapsCodeOrg);
            nationalVerifyParams.setPayeeType("");
            nationalVerifyParams.setBankName(payeeBankName);
            nationalVerifyParams.setToOrgName(payeeBankOrgName);
            nationalVerifyParams.setSendmessageYn(!"".equals(transPayeeMobile));
//            nationalVerifyParams.setSaveAsPayeeYn(cb_savePayee.isChecked() && isNeedtoSavePayee);
            nationalVerifyParams.setSaveAsPayeeYn(cb_savePayee.isChecked() );
            nationalVerifyParams.setNickName(payerAccountSelected.getNickName());
//         nationalRealtimeVerifyParams.setre(et_transRemitMessage.getText().toString().trim());
            nationalVerifyParams.setPayeeNickName("");
//            showLoadingDialog();
            getPresenter().transNationalVerify(nationalVerifyParams);
        }
        else if(transtype == TRANS_TO_NATIONAL_DIR){
            dirNationalVerifyParams=new PsnDirTransBocNationalTransferVerifyParams();
            dirNationalVerifyParams.setConversationId(conversationId);
            dirNationalVerifyParams.set_combinId(combinId);
            dirNationalVerifyParams.setAmount(transMoneyAmount);
            dirNationalVerifyParams.setFromAccountId(payerAccountSelected.getAccountId());
            dirNationalVerifyParams.setPayeeActno(transPayeeAccountNumber);
            dirNationalVerifyParams.setCurrency(ApplicationConst.CURRENCY_CNY);
            dirNationalVerifyParams.setExecuteType(executeType);
            if ("1".equals(executeType)){
                dirNationalVerifyParams.setExecuteDate(executeDate);
            }
            dirNationalVerifyParams.setRemark(et_transRemitMessage1.getEditWidgetContent().trim());
            dirNationalVerifyParams.setPayeeName(transPayeeName);
            dirNationalVerifyParams.setPayeeMobile(transPayeeMobile);
            if ("null".equals(String.valueOf(payeeSelectedBean.getPayeetId()))){
                dirNationalVerifyParams.setPayeeId("");
            }else{
                dirNationalVerifyParams.setPayeeId(String.valueOf(payeeSelectedBean.getPayeetId()));
            }
            dirNationalVerifyParams.setCnapsCode(payeeBankCnapsCodeOrg);
            dirNationalVerifyParams.setBankName(payeeBankName);
            dirNationalVerifyParams.setToOrgName(payeeBankOrgName);
//          dirNationalVerifyParams.setRemittanceInfo(et_transRemitMessage.getText().toString().trim());
//            showLoadingDialog();
            getPresenter().transDirNationalVerify(dirNationalVerifyParams);
        }
        else if  (transtype == TRANS_TO_NATIONAL_REALTIME) {//
            nationalRealtimeVerifyParams=new  PsnEbpsRealTimePaymentConfirmParams();
            nationalRealtimeVerifyParams.setConversationId(conversationId);
            nationalRealtimeVerifyParams.set_combinId(combinId);
            nationalRealtimeVerifyParams.setAmount(transMoneyAmount);
//          nationalRealtimeVerifyParams.setTransoutaccparent("活期一本通1020******4370活期一本通 陕西");
            nationalRealtimeVerifyParams.setFromAccountId(payerAccountSelected.getAccountId());
            nationalRealtimeVerifyParams.setPayeeActno(transPayeeAccountNumber);
            nationalRealtimeVerifyParams.setPayeeActno2(transPayeeAccountNumber);
            nationalRealtimeVerifyParams.setPayeeName(transPayeeName);
            if("0".equals(selectedPayeeBocFlag)){
                BankEntity oneBean=isHotBank(payeeBankName);
                if(null!=oneBean){
                    payeeBankCnapsCode = oneBean.getBankCode();
                }
            }
            nationalRealtimeVerifyParams.setPayeeCnaps(payeeBankCnapsCode);
            nationalRealtimeVerifyParams.setPayeeBankName(payeeBankName);
            nationalRealtimeVerifyParams.setPayeeOrgName(payeeBankName);
            nationalRealtimeVerifyParams.setCurrency(ApplicationConst.CURRENCY_CNY);
            nationalRealtimeVerifyParams.setMemo(et_transRemitMessage1.getEditWidgetContent().trim());
            nationalRealtimeVerifyParams.setExecuteType(executeType);
            if ("1".equals(executeType)){
                nationalRealtimeVerifyParams.setExecuteDate(executeDate);
            }
            nationalRealtimeVerifyParams.setPayeeMobile(transPayeeMobile);
            nationalRealtimeVerifyParams.setSendMsgFlag("".equals(transPayeeMobile)+"");
//              showLoadingDialog();
            getPresenter().transNationalRealTimeVerify(nationalRealtimeVerifyParams);
        }
        else if(transtype == TRANS_TO_NATIONAL_REALTIME_DIR){
            dirNationalRealtimeVerifyParams =new PsnDirTransCrossBankTransferParams();
            dirNationalRealtimeVerifyParams.setConversationId(conversationId);
            dirNationalRealtimeVerifyParams.set_combinId(combinId);
            dirNationalRealtimeVerifyParams.setAmount(transMoneyAmount);
            dirNationalRealtimeVerifyParams.setFromAccountId(payerAccountSelected.getAccountId());
            dirNationalRealtimeVerifyParams.setPayeeActno(transPayeeAccountNumber);
            dirNationalRealtimeVerifyParams.setPayeeName(transPayeeName);
            dirNationalRealtimeVerifyParams.setCnapsCode(payeeBankCnapsCode);
            dirNationalRealtimeVerifyParams.setBankName(payeeBankName);
            dirNationalRealtimeVerifyParams.setToOrgName(payeeBankName);
            dirNationalRealtimeVerifyParams.setCurrency(ApplicationConst.CURRENCY_CNY);
            dirNationalRealtimeVerifyParams.setSendMsgFlag(!"".equals(transPayeeMobile)+"");
            dirNationalRealtimeVerifyParams.setRemark(et_transRemitMessage1.getEditWidgetContent().trim());
            dirNationalRealtimeVerifyParams.setExecuteType(executeType);
            if ("1".equals(executeType)){
                dirNationalRealtimeVerifyParams.setExecuteDate(executeDate);
            }
            dirNationalRealtimeVerifyParams.setPayeeMobile(transPayeeMobile);

            if ("null".equals(String.valueOf(payeeSelectedBean.getPayeetId()))){
                dirNationalRealtimeVerifyParams.setPayeeId("");
            }else{
                dirNationalRealtimeVerifyParams.setPayeeId(String.valueOf(payeeSelectedBean.getPayeetId()));
            }
//          dirNationalRealtimeVerifyParams.setRemittanceInfo(et_transRemitMessage.getText().toString().trim());
//          showLoadingDialog();
            getPresenter().transDirNationalRealTimeVerify(dirNationalRealtimeVerifyParams);
        }
    }
    @Override
    public void getConverIdandSaftyFatorFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }
    /**
     * 根据用户输入的accountNumber来判断输入的账号是关联账户 行内 还是跨行等
     */
    @Override
    public void queryBankInfobyCardBinSuccess(PsnQueryBankInfobyCardBinResult result) {
        payeeSelectedBean=null;
        isPayeeAccCcrd=false;
        closeProgressDialog();//放这里是因为想显示完收款银行在关闭loading
        et_transRemitPayeePhone1.setVisibility(View.VISIBLE);
        line_mobile.setVisibility(View.VISIBLE);
        et_transRemiPayeebank.setVisibility(View.VISIBLE);
        line_bank.setVisibility(View.VISIBLE);
        et_transRemitOrgName.setChoiceTextContent("");
        payeeBankCnapsCodeOrg="";
        payeeBankOrgName="";
        if (null!=result){
            payeeBankName=result.getBankName();
            payeeBankCnapsCode=result.getCnapsCode();
            BankEntity oneBean=isHotBank(payeeBankName);
            if (null!=oneBean){
                payeeBankCode = oneBean.getBankBtp();
                payeeBankCnapsCode=oneBean.getBankCode();
                et_transRemiPayeebank.setChoiceTextContent(oneBean.getBankAlias());
            }else{
                if (!StringUtils.isEmptyOrNull(payeeBankName)&&payeeBankName.contains("其他银行")){
                    et_transRemiPayeebank.setChoiceTextContent(payeeBankName);
                    payeeBankCode="OTHER";//如果收款银行是“其他银行”，那么银行号设为other
                }else{
                    //此处处理非常用银行，但是获取不到bankcode的情况,不显示所属银行的内容，让用户去手选银行出来。不然页面无法得到
                    if(!StringUtils.isEmptyOrNull(payeeBankCnapsCode)){
                        et_transRemiPayeebank.setChoiceTextContent(payeeBankName);
                        payeeBankCode="OTHER";
                    }else{
                        et_transRemiPayeebank.setChoiceTextContent("");
                    }
                }
            }
            mCurrentSelectBankEntity.setBankCode(payeeBankCode);
            mCurrentSelectBankEntity.setBankName(result.getBankName());
//            view_savePayee.setVisibility(View.VISIBLE);
//           setExecuteTypeVisible(true);
//            if("".equals(executeTypeName)||"实时".equals(executeTypeName)){
//                et_transRemitOrgName.setVisibility(View.GONE);
////                et_transRemitOrgName.setChoiceTextContent("");
//                line_org.setVisibility(View.GONE);
//            }else{
//                et_transRemitOrgName.setVisibility(View.VISIBLE);
//                line_org.setVisibility(View.VISIBLE);
//            }
//            if ((!getResources().getString(R.string.trans_boc).equals(payeeBankName)
//                    &&(!"".equals(transMoneyAmount)
//                    &&Double.valueOf(transMoneyAmount)>TRANS_REMIT_LIMIT))
//                    ||!isRealtimeBank(payeeBankName)){
//                et_transRemiPayeebank.setVisibility(View.VISIBLE);
//                line_bank.setVisibility(View.VISIBLE);
//                et_transRemiPayeebank.setChoiceTextContent(payeeBankName);
//                et_transRemitOrgName.setVisibility(View.VISIBLE);
//                line_org.setVisibility(View.VISIBLE);
//            }else{
//                et_transRemiPayeebank.setVisibility(View.VISIBLE);
//                line_bank.setVisibility(View.VISIBLE);
//                et_transRemiPayeebank.setChoiceTextContent(payeeBankName);
//                et_transRemitOrgName.setVisibility(View.GONE);
//                line_org.setVisibility(View.GONE);
//            }
        }
        else {
//            closeProgressDialog();
//            et_transRemiPayeebank.setVisibility(View.VISIBLE);
//            line_bank.setVisibility(View.VISIBLE);
//            et_transRemitPayeePhone1.setVisibility(View.VISIBLE);
//            line_mobile.setVisibility(View.VISIBLE);
//            setExecuteTypeVisible(true);
//            if("".equals(executeTypeName)||"实时".equals(executeTypeName) ){
//                et_transRemitOrgName.setVisibility(View.GONE);
////                et_transRemitOrgName.setChoiceTextContent("");
//                line_org.setVisibility(View.GONE);
//            }else{
//                et_transRemitOrgName.setVisibility(View.VISIBLE);
//                line_org.setVisibility(View.VISIBLE);
//
//            }
//            view_savePayee.setVisibility(View.VISIBLE);
        }
//        setSavePayeeView();??
//        setSavePayeeVisibility(true);//ab12
        setSavePayeeView();
    }

    @Override
    public void queryBankInfobyCardBinFailed(BiiResultErrorException exception) {
        closeProgressDialog();
//        showErrorDialog(exception.getErrorMessage());
        et_transRemiPayeebank.setVisibility(View.VISIBLE);
        line_bank.setVisibility(View.VISIBLE);
        et_transRemitPayeePhone1.setVisibility(View.VISIBLE);
        line_mobile.setVisibility(View.VISIBLE);
        view_savePayee.setVisibility(View.VISIBLE);
        cb_savePayee.setChecked(true);
    }

    private CardQueryBindInfoResult tmpEcardPayeeResult; //电子卡
    //电子卡账户  M：他行卡号；N：他行账号；C：中行借记卡卡号；D：中行存款账号；X：中行信用卡卡号；
    @Override
    public void queryPsnCardQueryBindInfoSuccess(CardQueryBindInfoResult result) {
        closeProgressDialog();
        dealEcardQureyResult(result);
    }
    public void dealEcardQureyResult(CardQueryBindInfoResult result){
        if (!isPayerAccSelected){
//            showLoadingDialog();
            getPresenter().queryAccountBalance(payerAccountSelected.getAccountId());
        }
        tmpEcardPayeeResult=result;
    }
    @Override
    public void queryPsnCardQueryBindInfoFailed(BiiResultErrorException exception) {
//        closeProgressDialog();//2016年11月10日 屏蔽
        toGetPayeeListForDim();//2016年11月10日 添加，查询失败继续查询
        payerAccountSelected=null;
//        showErrorDialog(exception.getErrorMessage());
    }
    @Override
    public void queryRecentPayeeInfoSuccess(List<PsnQueryRecentPayeeInfoBean> result) {
        closeProgressDialog();
        recentPayeeList.clear();
        if (!PublicUtils.isEmpty(result)){
            for (PsnQueryRecentPayeeInfoBean biibean:result){
                TransRecentPayeeBean newBean=new TransRecentPayeeBean();
                BeanConvertor.toBean(biibean,newBean);
                newBean.setBankName(getBankAlias(newBean.getBankName()));
                recentPayeeList.add(newBean);
            }
        }
        payeeNameDataAdapter =new RecentPayeeListAdapter(mContext,recentPayeeList, commenPayeeList);
        et_transRemitPayeeName.setAdapter(payeeNameDataAdapter);
    }

    public String getBankAlias(String bankName){
        if (!StringUtils.isEmptyOrNull(bankName)){
            for (BankEntity oneEntyty:hotBankEntityList){
                if (bankName.contains(oneEntyty.getBankAlias())){
                    return oneEntyty.getBankAlias();}}}
        return bankName;
    }

    @Override
    public void queryRecentPayeeInfoFailed(BiiResultErrorException exception) {
        recentPayeeList.clear();
        closeProgressDialog();
        payeeNameDataAdapter =new RecentPayeeListAdapter(mContext,recentPayeeList, commenPayeeList);
        et_transRemitPayeeName.setAdapter(payeeNameDataAdapter);
    }

    private List<TransRecentPayeeBean> commenPayeeList =new ArrayList<>();
    @Override
    public void queryPayeeListForDimSuccess(PsnTransPayeeListqueryForDimResult result) {
//        isPayeeSavedFanally=false;
//        isFromResultPage=false;
        mPayeeEntityList.clear();//2016-10-12
        mPayeeEntityListAfterMerge.clear();
        mPayeeEntityList.addAll(transResult2ViewModel(result).getPayeeEntityList());
        // 保存到全局缓存中
//        Collections.sort(mPayeeEntityList);//没必要排序
        context1.setPayeeEntityList(mPayeeEntityList);
        //保存......
        mPayeeEntityListAfterMerge.addAll(mPayeeEntityList);
        mergeSamePayee();
        getRecentPayeeListFromPayeeList(mPayeeEntityListAfterMerge);

        if (onlyQueryPayeeToJump){//只需要查询常用收款人,跳转收款人管理界面
            closeProgressDialog();
            jumpToPayeeSelectFragment();
        }else{
            PsnQueryRecentPayeeInfoParams params=new PsnQueryRecentPayeeInfoParams();
            getPresenter().queryRecentPayeeInfo(params);
        }
    }
    //  如果同时有普通和实时重复的收款人 去掉实时的
    public void mergeSamePayee(){
        if (0==mPayeeEntityListAfterMerge.size()||1==mPayeeEntityListAfterMerge.size()){
            return;
        }

        for (int i=0;i<=mPayeeEntityListAfterMerge.size()-2;i++){
            String tmpName1=mPayeeEntityListAfterMerge.get(i).getAccountName();
            String tmpAcc1=mPayeeEntityListAfterMerge.get(i).getAccountNumber();
            String tmpRealtime1=mPayeeEntityListAfterMerge.get(i).getBocFlag();//0普通，3实时
            String tmpBankname1=(null==mPayeeEntityListAfterMerge.get(i).getBankName())?""
                    :mPayeeEntityListAfterMerge.get(i).getBankName();
            String tmpIsPointed1=(null==mPayeeEntityListAfterMerge.get(i).getIsAppointed()?
                    "":mPayeeEntityListAfterMerge.get(i).getIsAppointed());
            for (int j=i+1;j<=mPayeeEntityListAfterMerge.size()-1;j++){
                String tmpName2=mPayeeEntityListAfterMerge.get(j).getAccountName();
                String tmpAcc2=mPayeeEntityListAfterMerge.get(j).getAccountNumber();
                String tmpRealtime2=mPayeeEntityListAfterMerge.get(j).getBocFlag();
                String tmpBankname2=(null==mPayeeEntityListAfterMerge.get(j).getBankName())?""
                        :mPayeeEntityListAfterMerge.get(j).getBankName();
                String tmpIsPointed2=(null==mPayeeEntityListAfterMerge.get(j).getIsAppointed()?
                        "":mPayeeEntityListAfterMerge.get(j).getIsAppointed());
                if (tmpName1.equals(tmpName2)&&tmpAcc1.equals(tmpAcc2)&&tmpIsPointed1.equals(tmpIsPointed2)&&tmpBankname1.equals(tmpBankname2)){
                    if ("3".equals(tmpRealtime1)&&"0".equals(tmpRealtime2)){ //屏蔽实时的
                        mPayeeEntityListAfterMerge.remove(mPayeeEntityListAfterMerge.get(i));
                        mergeSamePayee();
                    }else if("3".equals(tmpRealtime2)&&"0".equals(tmpRealtime1)){
                        mPayeeEntityListAfterMerge.remove(mPayeeEntityListAfterMerge.get(j));
                        mergeSamePayee();
                    }else{

                    }
                }else{

                }
            }
        }
        return;
    }

    private ArrayList<PayeeEntity> mPayeeEntityList = new ArrayList<>(); // 收款人列表
    private ArrayList<PayeeEntity> mPayeeEntityListAfterMerge = new ArrayList<>(); // 收款人列表
    PsnTransPayeeListqueryForDimViewModel viewModel = new PsnTransPayeeListqueryForDimViewModel();
    // 把请求结果转换为ViewModel，给收款人选择页面用
    private PsnTransPayeeListqueryForDimViewModel transResult2ViewModel(PsnTransPayeeListqueryForDimResult psnTransPayeeListqueryForDimResult) {
        List<PsnTransPayeeListqueryForDimResult.PayeeAccountBean> accountBeanList = psnTransPayeeListqueryForDimResult.getList();
        viewModel.getPayeeEntityList().clear();
        for (PsnTransPayeeListqueryForDimResult.PayeeAccountBean payeeAccountBean : accountBeanList) {
            PsnTransPayeeListqueryForDimViewModel.PayeeEntity payeeEntity = new PsnTransPayeeListqueryForDimViewModel.PayeeEntity();
            BeanConvertor.toBean(payeeAccountBean, payeeEntity);
            payeeEntity.setPinyin("");
            viewModel.getPayeeEntityList().add(payeeEntity);
        }
        return viewModel;
    }

    //把收款人列表转换格式
    private void getRecentPayeeListFromPayeeList(List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> mPayeeEntityList){
        commenPayeeList.clear();//2016-10-12
        for (PsnTransPayeeListqueryForDimViewModel.PayeeEntity payeeEntity: mPayeeEntityList){
            TransRecentPayeeBean oneBean=new TransRecentPayeeBean();
            BeanConvertor.toBean(payeeEntity,oneBean);
            oneBean.setBankName(getBankAlias(payeeEntity.getBankName()));
            oneBean.setPayeetId( String.valueOf(payeeEntity.getPayeetId()));
            commenPayeeList.add(oneBean);
        }
    }
    @Override
    public void queryPayeeListForDimFailed(BiiResultErrorException exception) {
        commenPayeeList.clear();
//        needQueryPayeeForDim =true;
        if (onlyQueryPayeeToJump){//只需要查询常用收款人,出错时报错。
            closeProgressDialog();
            showErrorDialog(exception.getErrorMessage());
        }else{
            PsnQueryRecentPayeeInfoParams params=new PsnQueryRecentPayeeInfoParams();
            getPresenter().queryRecentPayeeInfo(params);
        }
    }

    private List<String> payerAccCurrencyList =new ArrayList<>();//付款账户币种
    private List<String> payerAccCashRemitList =new ArrayList<>();//c钞汇
    private List<BigDecimal> payerAccBalanceList =new ArrayList<>();//可用余额
    private List<String> payerAccLoanFlagList =new ArrayList<>();//余额标志位，信用卡的时候用
    private List<String> currencyListTmp=new ArrayList<>();//币种
    @Override
    public void queryAccountBalanceSuccess(AccountQueryAccountDetailResult result) {
        et_tansPayerAccout.setChoiceTextContent(NumberUtils.formatCardNumberStrong(payerAccountSelected.getAccountNumber()));
        dealWithBanlanceResult(result);
//        getPresenter().queryPayeeListForDim();
        toGetPayeeListForDim();
    }
    private List<PayeeEntity> globalPayeeList;
    ApplicationContext context1 ;
    //获取常用收款人数据，如果缓存有就用，没有调接口查询。查询成功要更新缓存
    public void toGetPayeeListForDim(){
        globalPayeeList=context1.getPayeeEntityList();
        //这里还需要判断是不是从结果页面返回来的，如果是且同时是新增了或者更新了收款人信息 则需要重新请求。
        if(globalPayeeList != null ){
//            mPayeeEntityList.clear();//2016-10-12
            mPayeeEntityListAfterMerge.clear();
            mPayeeEntityListAfterMerge.addAll(globalPayeeList);
            mergeSamePayee();//合并相同收款人
            getRecentPayeeListFromPayeeList(mPayeeEntityListAfterMerge);//转换，给payeename的Adapter用。
            if (onlyQueryPayeeToJump){
                jumpToPayeeSelectFragment();//如果有值就跳转，不继续查询
            }else{
                //查近期收款人
                PsnQueryRecentPayeeInfoParams params=new PsnQueryRecentPayeeInfoParams();
//             needQueryPayeeForDim =false;
                getPresenter().queryRecentPayeeInfo(params);
            }
        }else{//无缓存或者确定是从结果页面回来的
            if (onlyQueryPayeeToJump){
                showLoadingDialog();
            }
            getPresenter().queryPayeeListForDim();
        }
    }
    public void dealWithBanlanceResult(AccountQueryAccountDetailResult result){
        isOkToNext=true;
//        payerAccountDetail=result;//详情结果
//        detailListBean=null;
        if (null != result && !PublicUtils.isEmpty(result.getAccountDetaiList())) {
            payerAccCurrencyList.clear();
            payerAccCashRemitList.clear();
            payerAccBalanceList.clear();
            ll_transPayerAccountBalance.setVisibility(View.VISIBLE);
            for (AccountDetaiListBean oneBean : result.getAccountDetaiList()) {
                if(ApplicationConst.CURRENCY_CNY.equals(oneBean.getCurrencyCode())){
                    payerAccCurrencyList.add(0,oneBean.getCurrencyCode());
                    payerAccCashRemitList.add(0,"00");
//                    detailListBean=oneBean;
                    payerAccBalanceList.add(0,oneBean.getAvailableBalance());
                    payerAccLoanFlagList.add(0,"");
                }else{
                    payerAccCurrencyList.add(oneBean.getCurrencyCode());
                    payerAccCashRemitList.add(oneBean.getCashRemit());
                    payerAccBalanceList.add(oneBean.getAvailableBalance());
                    payerAccLoanFlagList.add("");
                }
            }
            getCurrencyToShowList();
            setBalanceView();
            //多币种账户 要处理多币种，有人民币展示人民币，此步骤是为了防止人民币不在币种列表第一个
            //如果没有人民币，则默认展示列表里第一个币种

            // 查询余额成功，如果是理财账户那么就直接反显
            if (ApplicationConst.ACC_TYPE_BOCINVT.equals(payerAccountSelected.getAccountType())){
                showOFAPayee();
            }
            //如果是电子卡账户也是返显
            if ("2".equals(payerAccountSelected.getAccountCatalog())||"3".equals(payerAccountSelected.getAccountCatalog())){
                showEcardPayee(tmpEcardPayeeResult);
            }
        }
    }

    //显示网上理财账户绑定收款人
    public void showOFAPayee(){
        if (null==payeeSelectedBean){
            return;
        }else{
            setViewAfterPayeeSelected(payeeSelectedBean);
        }
    }
    //显示电子卡绑定收款人
    public void showEcardPayee(CardQueryBindInfoResult result) {
        if (null != result) {
            transPayeeName=result.getPayeeAccountName();
            et_transRemitPayeeName.setText(transPayeeName);
            transPayeeAccountNumber=result.getPayeeaccountNumber();
            transPayeeAccountNumberBefore=transPayeeAccountNumber;
//          et_transRemitPayeeAccNo1.setEditWidgetContent(NumberUtils.formatCardNumberStrong(transPayeeAccountNumber));
            et_transRemitPayeeAccNo1.setEditWidgetContent(transPayeeAccountNumber);
            payeeBankName = result.getPayeeBankName();
            payeeBankCnapsCode = result.getPayeeCnaps();
            et_transRemiPayeebank.setChoiceTextContent(payeeBankName);
            payeeBankOrgName = result.getOpeningBankName();
            et_transRemitOrgName.setChoiceTextContent(payeeBankOrgName);
            payeeBankCnapsCodeOrg = result.getCnapsCode();
            payeeBankCode = result.getBankCode();
//            mCurrentSelectBankEntity.setBankCode(result.getCnapsCode());//2016年11月30日
            mCurrentSelectBankEntity.setBankCode(result.getBankCode());
            mCurrentSelectBankEntity.setBankName(result.getPayeeBankName());
            et_transRemitPayeePhone1.setVisibility(View.VISIBLE);
            setExecuteTypeVisible(true);
            et_transRemiPayeebank.setVisibility(View.VISIBLE);
            line_bank.setVisibility(View.VISIBLE);
            if ("M".equals(result.getPayeeaccountType()) || "N".equals(result.getPayeeaccountType())) {
            } else if ("C".equals(result.getPayeeaccountType()) || "D".equals(result.getPayeeaccountType()) || "X".equals(result.getPayeeaccountType())) {
                if (et_executeType.getChoiceTextContent().equals("普通")){
                    et_executeType.setChoiceTextContent("");
                    executeTypeName="";
                    executeType="";
                }
                et_transRemitOrgName.setVisibility(View.GONE);
//               et_transRemitOrgName.setChoiceTextContent("");
                line_org.setVisibility(View.GONE);
            }
            setSavePayeeView();
        }

    }
    /**
     * 设置账户余额显示
     */
    public void setBalanceView(){
        setBalanceView1(transCurrency,availableBalance,transCashRemit, balanceLoanFlag);
    }

    public void setBalanceView1(String currency,BigDecimal amount,String cashRemit,String balanceFlag) {
//        currencyCode	币种, cashRemit	钞汇标识
        if ("".equals(balanceFlag)){
            tv_transBalanceCurrency.setText(PublicCodeUtils.getCurrency(mContext, currency)+getCsahRemitString(cashRemit));
            et_Currency.setChoiceTextContent(PublicCodeUtils.getCurrency(mContext, currency)+getCsahRemitString(cashRemit));
            tv_transRemitBalance.setText(MoneyUtils.transMoneyFormat(amount,currency));
        }else{
            //     “0”-欠款;“1”-存款; “2”-余额0
            String flagStr="0".equals(balanceFlag)?"欠款":"存款";
            tv_transBalanceCurrency.setText(flagStr+" "+PublicCodeUtils.getCurrency(mContext, currency)+getCsahRemitString(cashRemit));
            et_Currency.setChoiceTextContent(PublicCodeUtils.getCurrency(mContext, currency)+getCsahRemitString(cashRemit));
            tv_transRemitBalance.setText(MoneyUtils.transMoneyFormat(amount,currency));
        }
        if (combineCurrencyList.size()>1){//看合并结果
            et_Currency.setArrowImageGone(true);
        }else{
            et_Currency.setArrowImageGone(false);
        }
    }
    //钞汇标示转换
    public String getCsahRemitString(String cashRemit){
        return "00".equals(cashRemit)?"":("01".equals(cashRemit)?"/钞":("02".equals(cashRemit)?"/汇":""));
    }

    @Override
    public void queryAccountBalanceFailed(BiiResultErrorException exception) {
//        closeProgressDialog();2016-11-10 屏蔽。
        payerAccountSelected=null;
        ll_transPayerAccountBalance.setVisibility(View.GONE);
        toGetPayeeListForDim();//2016-11-10 添加，查询账户信息失败也要继续查询收款人信息。
        isOkToNext=false;
//        showErrorDialog(exception.getErrorMessage());
    }


    @Override
    public void queryCrcdAccountBalanceSuccess(CrcdQueryAccountDetailResult result) {
        et_tansPayerAccout.setChoiceTextContent(NumberUtils.formatCardNumberStrong(payerAccountSelected.getAccountNumber()));
//        et_tansPayerAccout.setChoiceTextContent(NumberUtils.formatCardNumber2(payerAccountSelected.getAccountNumber()));
        dealCrcdQueryResult(result);
//        getPresenter().queryPayeeListForDim();
        toGetPayeeListForDim();//查询常用收款人  近期收款人
    }

    public void dealCrcdQueryResult(CrcdQueryAccountDetailResult result){
        isOkToNext=true;
        if (null != result&& !PublicUtils.isEmpty(result.getCrcdAccountDetailList())) {
            ll_transPayerAccountBalance.setVisibility(View.VISIBLE);
            payerAccCurrencyList.clear();//
            payerAccCashRemitList.clear();
            payerAccBalanceList.clear();
            payerAccLoanFlagList.clear();
            for (CrcdAccountDetailListBean oneBean : result.getCrcdAccountDetailList()) {
                if (ApplicationConst.CURRENCY_CNY.equals(oneBean.getCurrency())){//人民币置顶
                    payerAccCurrencyList.add(0,oneBean.getCurrency());
                    payerAccCashRemitList.add(0,"00");//人民币都是00
                    payerAccBalanceList.add(0,oneBean.getLoanBalanceLimit());
                    payerAccLoanFlagList.add(0,oneBean.getLoanBalanceLimitFlag());
//                    crcdAccountDetailListBean=oneBean;
                }else{
                    payerAccCurrencyList.add(oneBean.getCurrency());
                    payerAccCashRemitList.add("01");//信用卡外币种都是钞
                    payerAccBalanceList.add(oneBean.getLoanBalanceLimit());
                    payerAccLoanFlagList.add(oneBean.getLoanBalanceLimitFlag());
                }
            }
            getCurrencyToShowList();
            setBalanceView();
        }
    }

    @Override
    public void queryCrcdAccountBalanceFailed(BiiResultErrorException exception) {
//        closeProgressDialog();//2016-11-10 屏蔽
        payerAccountSelected=null;
        ll_transPayerAccountBalance.setVisibility(View.GONE);
        toGetPayeeListForDim();//2016-11-10 添加，查询账户信息失败也要继续查询收款人信息。
        isOkToNext=false;
    }

    //190 网上理财账户相关代码
    private String financialOpenStatus;//
    //    private MainAccountBean mainAccount;//
    private OFAAccountStateQueryResult.BankAccountBean ofaBankAccount;//

    @Override
    public void queryPsnOFAAccountStateSuccess(OFAAccountStateQueryResult result) {
//        closeProgressDialog();
        dealwithOFAQueryResult(result);
    }

    AccountBean payeeLinkedAcc;
    public PayeeEntity changeLinkedAcctoPayeeEntity(AccountBean payeeLinkedAcc){
        PayeeEntity oneLinkedPayeeEntity = new PayeeEntity();
        BeanConvertor.toBean(payeeLinkedAcc, oneLinkedPayeeEntity);
        oneLinkedPayeeEntity.setPinyin("我");
        oneLinkedPayeeEntity.setLinked(true);
        oneLinkedPayeeEntity.setNickNamepinyin("");
        oneLinkedPayeeEntity.setType(payeeLinkedAcc.getAccountType());
        oneLinkedPayeeEntity.setPayeetId(0);
        return oneLinkedPayeeEntity;
    }
    boolean isPayerAccSelected =false;//如果付款账户是从账户选择页面过来的，而不是首次进入页面时的默认账户

    public void dealwithOFAQueryResult(OFAAccountStateQueryResult result){
        financialOpenStatus=result.getOpenStatus();
//        S-已开通   B-账户未绑定  R-账户未关联网银  N-未开通
        if (financialOpenStatus.equals("S")||financialOpenStatus.equals("R")||financialOpenStatus.equals("B")){
            if (financialOpenStatus.equals("S")){//说明有mainAcc 用于展示收款账户
                ofaBankAccount=result.getBankAccount();
                if(null==ofaBankAccount||null== getLinkedAccByAccId(String.valueOf(ofaBankAccount.getAccountId()))){
                    //绑定的收款账户不在网银关联账户里面，不报错。
                    showErrorDialog("当前理财账户未绑定主账户，请重新选择付款账户");
                    return;
                }
                payeeLinkedAcc= getLinkedAccByAccId(String.valueOf(ofaBankAccount.getAccountId()));
                payeeSelectedBean=changeLinkedAcctoPayeeEntity(payeeLinkedAcc);//把结果转换一下
                if (!isPayerAccSelected){ //如果是从选择页面跳过来，就已经查好了余额信息了
                    getPresenter().queryAccountBalance(payerAccountSelected.getAccountId());
                }
            }else{
                closeProgressDialog();
                showErrorDialog("当前理财账户未绑定主账户，请重新选择付款账户");
                return;
            }
        }
        else{
            closeProgressDialog();
            showErrorDialog("当前理财账户未绑定主账户，请重新选择付款账户");
            return;
        }
    }

    @Override
    public void queryPsnOFAAccountStateFailed(BiiResultErrorException exception) {
//        closeProgressDialog();//2016-11-10 屏蔽
        toGetPayeeListForDim();//2016-11-10 添加，查询账户信息失败也要继续查询收款人信息。
        payerAccountSelected=null;
    }

    @Override
    public void transBocVerifySuccess(PsnTransBocTransferVerifyResult results, PsnGetSecurityFactorResult securityFactorResult, String conversationId) {
//        closeProgressDialog();

        verifyInfoModel = new TransRemitVerifyInfoViewModel();
        verifyInfoModel.setAvailableBalance(availableBalance);
        verifyInfoModel.setAccountIbkNum(payerAccountSelected.getAccountIbkNum());
        verifyInfoModel.setAmount(bocVerifyParams.getAmount());
        verifyInfoModel.setAccountNumber(payerAccountSelected.getAccountNumber());
        verifyInfoModel.setPayeeMobile(bocVerifyParams.getPayeeMobile());
        verifyInfoModel.setCurrency(bocVerifyParams.getCurrency());
        verifyInfoModel.setPayeeName(bocVerifyParams.getPayeeName());
        verifyInfoModel.setRemark(bocVerifyParams.getRemark());
        verifyInfoModel.setFromAccountId(bocVerifyParams.getFromAccountId());
        verifyInfoModel.setPayeeActno(bocVerifyParams.getPayeeActno());
        verifyInfoModel.setExecuteType(bocVerifyParams.getExecuteType());
        verifyInfoModel.setExecuteDate(bocVerifyParams.getExecuteDate());
        verifyInfoModel.setPayeeId(bocVerifyParams.getPayeeId());
        verifyInfoModel.setExecuteTypeName(executeTypeName);
//        verifyInfoModel.setSaveAsPayeeYn(cb_savePayee.isChecked()&&isNeedtoSavePayee);
        verifyInfoModel.setSaveAsPayeeYn(cb_savePayee.isChecked());
        verifyInfoModel.setSmcTrigerInterval(results.getSmcTrigerInterval());
        verifyInfoModel.set_plainData(results.get_plainData());
        verifyInfoModel.set_certDN(results.get_certDN());
        verifyInfoModel.setToAccountType(results.getToAccountType());
        verifyInfoModel.setPayeeBankNum(results.getPayeeBankNum());
        verifyInfoModel.setPayeeBankIbkNum(results.getPayeeBankNum());
        verifyInfoModel.setNeedPassword(results.getNeedPassword());
        verifyInfoModel.setConversationId(conversationId);//提交交易和预交易的conversionId必须一致
        verifyInfoModel.setFactorList(results.getFactorList());
        verifyInfoModel.setNeedPassword(results.getNeedPassword());
        verifyInfoModel.setSendmessageYn(!StringUtils.isEmptyOrNull(transPayeeMobile));
        verifyInfoModel.setCashRemit(transCashRemit);
//        verifyInfoModel.setCashRemit("0");
        verifyInfoModel.setPayerName(payerAccountSelected.getAccountName());
        verifyInfoModel.setIsAppointed("0");
        verifyInfoModel.setFunctionFrom(functionId);
//        EShieldVerify.getInstance(getActivity()).setmPlainData(verifyInfoModel.get_plainData());
//        queryTransQuota();
        getTransBocCommissionCharge(verifyInfoModel);
    }

    @Override
    public void transBocVerifyFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transDirBocVerifySuccess(PsnDirTransBocTransferVerifyResult results,
                                         PsnGetSecurityFactorResult factorResult, String conversationId) {
//        closeProgressDialog();
        verifyInfoModel = new TransRemitVerifyInfoViewModel();
        verifyInfoModel.setAvailableBalance(availableBalance);

        verifyInfoModel.setAccountNumber(payerAccountSelected.getAccountNumber());
        verifyInfoModel.setAmount(dirBocVerifyParams.getAmount());
        verifyInfoModel.setPayeeMobile(dirBocVerifyParams.getPayeeMobile());
        verifyInfoModel.setCurrency(dirBocVerifyParams.getCurrency());
        verifyInfoModel.setPayeeName(dirBocVerifyParams.getPayeeName());
        verifyInfoModel.setRemark(dirBocVerifyParams.getRemark());
        verifyInfoModel.setFromAccountId(dirBocVerifyParams.getFromAccountId());
        verifyInfoModel.setPayeeActno(dirBocVerifyParams.getPayeeActno());
        verifyInfoModel.setExecuteType(dirBocVerifyParams.getExecuteType());
        verifyInfoModel.setExecuteDate(dirBocVerifyParams.getExecuteDate());
        verifyInfoModel.setExecuteTypeName(executeTypeName);
        verifyInfoModel.setPayeeId(dirBocVerifyParams.getPayeeId());
        verifyInfoModel.setPayeeBankNum(results.getPayeeBankNum());
        verifyInfoModel.setPayeeBankIbkNum(results.getPayeeBankNum());
        verifyInfoModel.setAccountIbkNum(payerAccountSelected.getAccountIbkNum());
        verifyInfoModel.setSmcTrigerInterval(results.getSmcTrigerInterval());
        verifyInfoModel.setSendmessageYn(!StringUtils.isEmptyOrNull(transPayeeMobile));
//        verifyInfoModel.setSaveAsPayeeYn(isNeedtoSavePayee&&cb_savePayee.isChecked());
        verifyInfoModel.setSaveAsPayeeYn(cb_savePayee.isChecked());
        verifyInfoModel.set_plainData(results.get_plainData());
        verifyInfoModel.set_certDN(results.get_certDN());
        verifyInfoModel.setToAccountType(results.getToAccountType());
        verifyInfoModel.setConversationId(conversationId);
        verifyInfoModel.setCashRemit(transCashRemit);
        verifyInfoModel.setFactorList(results.getFactorList());
        verifyInfoModel.setPayerName(payerAccountSelected.getAccountName());
        verifyInfoModel.setIsAppointed("1");
        verifyInfoModel.setFunctionFrom(functionId);
//        EShieldVerify.getInstance(getActivity()).setmPlainData(verifyInfoModel.get_plainData());
        getTransBocCommissionCharge(verifyInfoModel);
//        queryTransQuota();
    }

    @Override
    public void transDirBocVerifyFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transDirNationalRealTimeVerifySuccess(PsnDirTransCrossBankTransferResult result,PsnGetSecurityFactorResult factorResult,String conversationId) {
//        closeProgressDialog();
        verifyInfoModel = new TransRemitVerifyInfoViewModel();
        verifyInfoModel.setAvailableBalance(availableBalance);
        verifyInfoModel.setAmount(dirNationalRealtimeVerifyParams.getAmount());
        verifyInfoModel.setFromAccountId(dirNationalRealtimeVerifyParams.getFromAccountId());
        verifyInfoModel.setAccountNumber(payerAccountSelected.getAccountNumber());
        verifyInfoModel.setAccountIbkNum(payerAccountSelected.getAccountIbkNum());
        verifyInfoModel.setPayeeActno(dirNationalRealtimeVerifyParams.getPayeeActno());
        verifyInfoModel.setPayeeName(dirNationalRealtimeVerifyParams.getPayeeName());
        verifyInfoModel.setCnapsCode(dirNationalRealtimeVerifyParams.getCnapsCode());
        verifyInfoModel.setBankName(dirNationalRealtimeVerifyParams.getBankName());
        verifyInfoModel.setToOrgName(dirNationalRealtimeVerifyParams.getToOrgName());
        verifyInfoModel.setCurrency(dirNationalRealtimeVerifyParams.getCurrency());
        verifyInfoModel.setSendMsgFlag(dirNationalRealtimeVerifyParams.isSendMsgFlag());
        verifyInfoModel.setRemark(dirNationalRealtimeVerifyParams.getRemark());
//        verifyInfoModel.setRemittanceInfo(dirNationalRealtimeVerifyParams.getRemark());
        verifyInfoModel.setExecuteType(dirNationalRealtimeVerifyParams.getExecuteType());
        verifyInfoModel.setExecuteDate(dirNationalRealtimeVerifyParams.getExecuteDate());
        verifyInfoModel.setExecuteTypeName(executeTypeName);
        verifyInfoModel.setPayeeMobile(dirNationalRealtimeVerifyParams.getPayeeMobile());
//        verifyInfoModel.setRemittanceInfo(et_transRemitMessage.getText().toString().trim());
//        verifyInfoModel.setMemo(et_transRemitMessage.getText().toString().trim());
        verifyInfoModel.setPayeeId(dirNationalRealtimeVerifyParams.getPayeeId());
        verifyInfoModel.setSendmessageYn(!StringUtils.isEmptyOrNull(transPayeeMobile));
//        verifyInfoModel.setSaveAsPayeeYn(isNeedtoSavePayee&&cb_savePayee.isChecked());
        verifyInfoModel.setSaveAsPayeeYn(cb_savePayee.isChecked());
        verifyInfoModel.setCashRemit(transCashRemit);
        verifyInfoModel.set_plainData(result.get_plainData());
        verifyInfoModel.set_certDN(result.get_certDN());
        verifyInfoModel.setFactorList(result.getFactorList());
        verifyInfoModel.setSmcTrigerInterval(result.getSmcTrigerInterval());
        verifyInfoModel.setPayerName(payerAccountSelected.getAccountName());
        verifyInfoModel.setIsAppointed("1");
        verifyInfoModel.setConversationId(conversationId);
        verifyInfoModel.setFunctionFrom(functionId);
//        EShieldVerify.getInstance(getActivity()).setmPlainData(verifyInfoModel.get_plainData());
        getTransNationalCommissionCharge(verifyInfoModel);
//        queryTransQuota();
    }

    @Override
    public void transDirNationalRealTimeVerifyFailed(BiiResultErrorException exception) {
//        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transNationalRealTimeVerifySuccess(PsnEbpsRealTimePaymentConfirmResult result,PsnGetSecurityFactorResult factorResult,String conversationId) {
//       closeProgressDialog();
        verifyInfoModel = new TransRemitVerifyInfoViewModel();
        verifyInfoModel.setAvailableBalance(availableBalance);
        verifyInfoModel.setAccountIbkNum(payerAccountSelected.getAccountIbkNum());
        verifyInfoModel.setAmount(nationalRealtimeVerifyParams.getAmount());
        verifyInfoModel.setPayeeMobile(nationalRealtimeVerifyParams.getPayeeMobile());
        verifyInfoModel.setCurrency(nationalRealtimeVerifyParams.getCurrency());
        verifyInfoModel.setPayeeName(nationalRealtimeVerifyParams.getPayeeName());
//        verifyInfoModel.setRemark(nationalRealtimeVerifyParams.getMemo()); 没有remark
        verifyInfoModel.setFromAccountId(nationalRealtimeVerifyParams.getFromAccountId());
        verifyInfoModel.setExecuteType(nationalRealtimeVerifyParams.getExecuteType());
        verifyInfoModel.setExecuteDate(nationalRealtimeVerifyParams.getExecuteDate());
        verifyInfoModel.setExecuteTypeName(executeTypeName);
        verifyInfoModel.setPayeeActno(nationalRealtimeVerifyParams.getPayeeActno());
        verifyInfoModel.setPayeeCnaps(nationalRealtimeVerifyParams.getPayeeCnaps());
        verifyInfoModel.setAccountNumber(payerAccountSelected.getAccountNumber());
//        verifyInfoModel.setTransoutaccparent("活期一本通1020******4370活期一本通 陕西");
        verifyInfoModel.setPayeeActno2(nationalRealtimeVerifyParams.getPayeeActno2());
        verifyInfoModel.setPayeeBankName(nationalRealtimeVerifyParams.getPayeeBankName());
        verifyInfoModel.setPayeeOrgName(nationalRealtimeVerifyParams.getPayeeOrgName());
//        verifyInfoModel.setRemittanceInfo(et_transRemitMessage.getText().toString().trim());
        verifyInfoModel.setMemo(et_transRemitMessage1.getEditWidgetContent().trim());
//        verifyInfoModel.setSaveAsPayeeYn(cb_savePayee.isChecked()&&isNeedtoSavePayee);
        verifyInfoModel.setSaveAsPayeeYn(cb_savePayee.isChecked());
        verifyInfoModel.setCashRemit(transCashRemit);
        verifyInfoModel.setSendMsgFlag(StringUtils.isEmptyOrNull(transPayeeMobile)?"0":"1");
        verifyInfoModel.setSmcTrigerInterval(result.getSmcTrigerInterval());
        verifyInfoModel.set_plainData(result.get_plainData());
        verifyInfoModel.set_certDN(result.get_certDN());
        verifyInfoModel.setFactorList(result.getFactorList());
        verifyInfoModel.setConversationId(conversationId);
        verifyInfoModel.setNeedPassword(result.getNeedPassword());
        verifyInfoModel.setPayerName(payerAccountSelected.getAccountName());
        verifyInfoModel.setIsAppointed("0");
        verifyInfoModel.setFunctionFrom(functionId);
//        EShieldVerify.getInstance(getActivity()).setmPlainData(verifyInfoModel.get_plainData());
        if(null!=payeeSelectedBean)
            verifyInfoModel.setToAccountId(payeeSelectedBean.getAccountId());
        getTransNationalCommissionCharge(verifyInfoModel);
//        queryTransQuota();

    }
    @Override
    public void transNationalRealTimeVerifyFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transDirNationalVerifySuccess(PsnDirTransBocNationalTransferVerifyResult result, PsnGetSecurityFactorResult factorResult,String conversationId) {
//        closeProgressDialog();
        verifyInfoModel = new TransRemitVerifyInfoViewModel();
        verifyInfoModel.setAvailableBalance(availableBalance);
        verifyInfoModel.setAccountNumber(payerAccountSelected.getAccountNumber());
        verifyInfoModel.setFromAccountId(payerAccountSelected.getAccountId());
        verifyInfoModel.setAccountType(payerAccountSelected.getAccountType());
        verifyInfoModel.setAccountIbkNum(payerAccountSelected.getAccountIbkNum());
        verifyInfoModel.setNickName(payerAccountSelected.getNickName());
        verifyInfoModel.setAmount(dirNationalVerifyParams.getAmount());
        verifyInfoModel.setPayeeActno(dirNationalVerifyParams.getPayeeActno());
        verifyInfoModel.setCurrency(dirNationalVerifyParams.getCurrency());
        verifyInfoModel.setExecuteType(dirNationalVerifyParams.getExecuteType());
        verifyInfoModel.setExecuteDate(dirNationalVerifyParams.getExecuteDate());
        verifyInfoModel.setExecuteTypeName(executeTypeName);
        verifyInfoModel.setRemark(dirNationalVerifyParams.getRemark());
        verifyInfoModel.setPayeeName(dirNationalVerifyParams.getPayeeName());
        verifyInfoModel.setPayeeMobile(dirNationalVerifyParams.getPayeeMobile());
        verifyInfoModel.setPayeeId(dirNationalVerifyParams.getPayeeId());
        verifyInfoModel.setCnapsCode(dirNationalVerifyParams.getCnapsCode());
        verifyInfoModel.setBankName(dirNationalVerifyParams.getBankName());
        verifyInfoModel.setToOrgName(dirNationalVerifyParams.getToOrgName());
        verifyInfoModel.setCashRemit(transCashRemit);
//        verifyInfoModel.setRemittanceInfo(et_transRemitMessage.getText().toString().trim());
//        verifyInfoModel.setMemo(et_transRemitMessage.getText().toString().trim());
        verifyInfoModel.setConversationId(conversationId);
        verifyInfoModel.setSendmessageYn(!StringUtils.isEmptyOrNull(transPayeeMobile));
//        verifyInfoModel.setSaveAsPayeeYn(cb_savePayee.isChecked()&&isNeedtoSavePayee);
        verifyInfoModel.setSaveAsPayeeYn(cb_savePayee.isChecked());
        verifyInfoModel.setFactorList(result.getFactorList());
        verifyInfoModel.setSmcTrigerInterval(result.getSmcTrigerInterval());
        verifyInfoModel.set_certDN(result.get_certDN());
        verifyInfoModel.set_plainData(result.get_plainData());
        verifyInfoModel.setPayerName(payerAccountSelected.getAccountName());
        verifyInfoModel.setIsAppointed("1");
        verifyInfoModel.setFunctionFrom(functionId);
//        EShieldVerify.getInstance(getActivity()).setmPlainData(verifyInfoModel.get_plainData());
        getTransNationalCommissionCharge(verifyInfoModel);
//        queryTransQuota();
    }

    @Override
    public void transDirNationalVerifyFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transNationalVerifySuccess(PsnTransBocNationalTransferVerifyResult result,PsnGetSecurityFactorResult factorResult, String conversationId) {
//        closeProgressDialog();
        verifyInfoModel = new TransRemitVerifyInfoViewModel();
        verifyInfoModel.setAvailableBalance(availableBalance);
//        verifyInfoModel.setRemittanceInfo(et_transRemitMessage.getText().toString().trim());
//        verifyInfoModel.setMemo(et_transRemitMessage.getText().toString().trim());
        verifyInfoModel.setAmount(nationalVerifyParams.getAmount());
        verifyInfoModel.setPayeeMobile(nationalVerifyParams.getPayeeMobile());
        verifyInfoModel.setCurrency(nationalVerifyParams.getCurrency());
        verifyInfoModel.setPayeeName(nationalVerifyParams.getPayeeName());
        verifyInfoModel.setRemark(nationalVerifyParams.getRemark());
        verifyInfoModel.setFromAccountId(nationalVerifyParams.getFromAccountId());
        verifyInfoModel.setExecuteType(nationalVerifyParams.getExecuteType());
        verifyInfoModel.setExecuteDate(nationalVerifyParams.getExecuteDate());
        verifyInfoModel.setExecuteTypeName(executeTypeName);
        verifyInfoModel.setPayeeActno(nationalVerifyParams.getPayeeActno());
        verifyInfoModel.setFactorList(result.getFactorList());
        verifyInfoModel.setToOrgName(nationalVerifyParams.getToOrgName());
        verifyInfoModel.setBankName(nationalVerifyParams.getBankName());
        verifyInfoModel.setCnapsCode(nationalVerifyParams.getCnapsCode());
        verifyInfoModel.setCashRemit(transCashRemit);
        verifyInfoModel.setDueDate("");
        verifyInfoModel.setAccountIbkNum(nationalVerifyParams.getAccountIbkNum());
        verifyInfoModel.setAccountNumber(nationalVerifyParams.getAccountNumber());
        verifyInfoModel.setPayeeId(nationalVerifyParams.getPayeeId());
        verifyInfoModel.setPayeeType("");
        verifyInfoModel.setSaveAsPayeeYn(cb_savePayee.isChecked());
//        verifyInfoModel.setSaveAsPayeeYn(cb_savePayee.isChecked()&&isNeedtoSavePayee);
        if (null!=payeeSelectedBean)
            verifyInfoModel.setToAccountId(payeeSelectedBean.getAccountId());
        verifyInfoModel.setSendmessageYn(!StringUtils.isEmptyOrNull(transPayeeMobile));
        verifyInfoModel.setNickName(nationalVerifyParams.getNickName());
        verifyInfoModel.setPayeeNickName(nationalVerifyParams.getPayeeNickName());
        verifyInfoModel.setNeedPassword(result.getNeedPassword());
        verifyInfoModel.setSmcTrigerInterval(result.getSmcTrigerInterval());
        verifyInfoModel.set_plainData(result.get_plainData());
        verifyInfoModel.set_certDN(result.get_certDN());
        verifyInfoModel.setPayerName(payerAccountSelected.getAccountName());
        verifyInfoModel.setIsAppointed("0");
        verifyInfoModel.setConversationId(conversationId);
        verifyInfoModel.setFunctionFrom(functionId);
//        EShieldVerify.getInstance(getActivity()).setmPlainData(verifyInfoModel.get_plainData());
        getTransNationalCommissionCharge(verifyInfoModel);
//        queryTransQuota();
    }

    @Override
    public void transNationalVerifyFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());}

    @Override
    public void setPresenter(TransContract.TransPresenterBlankPage presenter) {
    }

    public void  getTransBocCommissionCharge(TransRemitVerifyInfoViewModel viewModel){
        PsnTransGetBocTransferCommissionChargeParams commissionChargeParams = new PsnTransGetBocTransferCommissionChargeParams();
        commissionChargeParams.setAmount(viewModel.getAmount());
        commissionChargeParams.setCurrency(viewModel.getCurrency());
        commissionChargeParams.setFromAccountId(viewModel.getFromAccountId());
        commissionChargeParams.setServiceId(getServiceId(transtype));
        switch (transtype){
            case TransRemitBlankFragment.TRANS_TO_LINKED:
                commissionChargeParams.setCashRemit(viewModel.getCashRemit());
                commissionChargeParams.setToAccountId(viewModel.getToAccountId());
                break;
            case TransRemitBlankFragment.TRANS_TO_BOC:
                commissionChargeParams.setPayeeName(viewModel.getPayeeName());
                commissionChargeParams.setPayeeActno(viewModel.getPayeeActno());
                break;
            case TransRemitBlankFragment.TRANS_TO_BOC_DIR:
                commissionChargeParams.setPayeeName(viewModel.getPayeeName());
                commissionChargeParams.setPayeeActno(viewModel.getPayeeActno());
                break;
        }
//        showLoadingDialog();

        getPresenter().getTransBocCommissionCharge(commissionChargeParams);
    }

    public void  getTransNationalCommissionCharge(TransRemitVerifyInfoViewModel viewModel){
        PsnTransGetNationalTransferCommissionChargeParams commissionChargeParams = new PsnTransGetNationalTransferCommissionChargeParams();
        commissionChargeParams.setAmount(viewModel.getAmount());
        commissionChargeParams.setCurrency(viewModel.getCurrency());
        commissionChargeParams.setCashRemit("00");
        commissionChargeParams.setFromAccountId(viewModel.getFromAccountId());
        commissionChargeParams.setPayeeActno(viewModel.getPayeeActno());
        commissionChargeParams.setPayeeName(viewModel.getPayeeName());
        switch (transtype){
            case TRANS_TO_NATIONAL:
                commissionChargeParams.setToOrgName(viewModel.getToOrgName());
                commissionChargeParams.setCnapsCode(viewModel.getCnapsCode());
                break;
            case TRANS_TO_NATIONAL_DIR:
                commissionChargeParams.setToOrgName(viewModel.getToOrgName());
                commissionChargeParams.setCnapsCode(viewModel.getCnapsCode());
                break;
            case TRANS_TO_NATIONAL_REALTIME:
                commissionChargeParams.setToOrgName(viewModel.getPayeeOrgName());
                commissionChargeParams.setCnapsCode(viewModel.getPayeeCnaps());
                break;
            case TRANS_TO_NATIONAL_REALTIME_DIR:
                commissionChargeParams.setToOrgName(viewModel.getToOrgName());
                commissionChargeParams.setCnapsCode(viewModel.getCnapsCode());
                break;
        }
        commissionChargeParams.setRemark(viewModel.getRemark());
        commissionChargeParams.setServiceId(getServiceId(transtype));
//        showLoadingDialog();
        getPresenter().getTransNationalCommissionCharge(commissionChargeParams);
    }
    /**
     * 获取serviceId
     * @return
     */
    public String getServiceId(String transtype) {
        switch (transtype) {
            case TransRemitBlankFragment.TRANS_TO_LINKED:
                return ApplicationConst.PB021;
            case TransRemitBlankFragment.TRANS_TO_BOC:
                return ApplicationConst.PB031;
            case TransRemitBlankFragment.TRANS_TO_BOC_DIR:
                return ApplicationConst.PB033;
            case TransRemitBlankFragment.TRANS_TO_NATIONAL:
                return ApplicationConst.PB032;
            case TransRemitBlankFragment.TRANS_TO_NATIONAL_DIR:
                return ApplicationConst.PB034;
            case TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME:
                return ApplicationConst.PB113;
            case TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME_DIR:
                return ApplicationConst.PB118;
        }
        return null;
    }
    @Override
    public void getTransBocCommissionChargeSuccess(PsnTransGetBocTransferCommissionChargeResult result) {
        //1 成功； 0 失败
        closeProgressDialog();
        if ("1".equals(result.getGetChargeFlag())) {
            verifyInfoModel.setPreCommissionCharge(result.getPreCommissionCharge());
            verifyInfoModel.setNeedCommissionCharge(result.getNeedCommissionCharge());
        } else {

        }
        hideSoftInput();
//        isPayeeSavedFanally=cb_savePayee.isChecked() && isNeedtoSavePayee;//收款人是否需要保存或更新
        start(TransRemitVerifyFragment.getInstance(transtype, verifyInfoModel, payerAccountSelected));
    }

    @Override
    public void getTransBocCommissionChargeFailed(BiiResultErrorException exception) {
        closeProgressDialog();
//        showErrorDialog(exception.getErrorMessage());
        hideSoftInput();
//        isPayeeSavedFanally=cb_savePayee.isChecked() && isNeedtoSavePayee;//收款人是否需要保存或更新
        start(TransRemitVerifyFragment.getInstance(transtype, verifyInfoModel, payerAccountSelected));
    }

    @Override
    public void getTransNationalCommissionChargeSuccess(PsnTransGetNationalTransferCommissionChargeResult result) {
        //1 成功； 0 失败
        closeProgressDialog();
        if ("1".equals(result.getGetChargeFlag())) {
            verifyInfoModel.setPreCommissionCharge(result.getPreCommissionCharge());
            verifyInfoModel.setNeedCommissionCharge(result.getNeedCommissionCharge());
        } else {
//            showErrorDialog("");
        }
        hideSoftInput();
//        isPayeeSavedFanally=cb_savePayee.isChecked() && isNeedtoSavePayee;//收款人是否需要保存或更新
        start(TransRemitVerifyFragment.getInstance(transtype, verifyInfoModel, payerAccountSelected));
    }
    @Override
    public void getTransNationalCommissionChargeFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        hideSoftInput();
        start(TransRemitVerifyFragment.getInstance(transtype, verifyInfoModel, payerAccountSelected));
//        showErrorDialog(exception.getErrorMessage());
    }

    //查询限额
    public void queryTransQuota(){
        PsnTransQuotaQueryParams params=new PsnTransQuotaQueryParams();
        getPresenter().queryQuotaForTrans(params);
    }

    /**
     * 数据项校验
     */
    public boolean checkInputData(){
        if (null==payerAccountSelected||StringUtils.isEmptyOrNull(et_tansPayerAccout.getChoiceTextContent())) {
            isOkToNext = false;
            theErrorMessage = getResources().getString(R.string.trans_error_choose_payer_first);
            showErrorDialog(theErrorMessage);
            return isOkToNext;
        }
        //"088"韩元
        transMoneyAmount=dealwithMoneyString(et_transRemitMoney1.getContentMoney());
        if ("088".equals(transCurrency)||ApplicationConst.CURRENCY_JPY.equals(transCurrency)){
            if( !getRegexResult(TransRegex.REGEX_TRANS_MONEY_SPECIAL, transMoneyAmount,true)) {
                isOkToNext = false;
                return isOkToNext;
            }
        }else{
            if( !getRegexResult(TransRegex.REGEX_TRANS_MONEY, transMoneyAmount,true)) {
                isOkToNext = false;
                return isOkToNext;
            }
        }
        /**
         * 校验收款人姓名
         */

        if( !getRegexResult(TransRegex.REGEX_TRANS_PAYEENAME, et_transRemitPayeeName.getText().toString().trim(),true)){
            isOkToNext=false;
            return isOkToNext;}
        if("".equals(et_transRemitPayeeAccNo1.getEditWidgetContent().trim())){
            isOkToNext=false;
            theErrorMessage=getResources().getString(R.string.trans_error_no_payee_accno);
            showErrorDialog(theErrorMessage);
            return isOkToNext;}
        if (payerAccountSelected.getAccountNumber().equals(transPayeeAccountNumber)){
            isOkToNext=false;
            showErrorDialog("付款账户和收款账户相同，请修改");
            return isOkToNext;
        }
        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(payerAccountSelected.getAccountType())||ApplicationConst.ACC_TYPE_GRE.equals(payerAccountSelected.getAccountType())
                ||ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(payerAccountSelected.getAccountType())){
            //     “0”-欠款;“1”-存款; “2”-余额0
            if("0".equals(balanceLoanFlag)){
                isOkToNext = false;
                theErrorMessage ="该账户下无存款余额，请修改";//报错信息以网银为准
                showErrorDialog(theErrorMessage);
                return isOkToNext;}
        }

        if(Double.valueOf(transMoneyAmount)>availableBalance.doubleValue()){
            isOkToNext=false;
            theErrorMessage=getResources().getString(R.string.trans_error_money_over);
            showErrorDialog(theErrorMessage);
            return isOkToNext;}


        if (et_transRemiPayeebank.getVisibility()==View.VISIBLE
                &&StringUtils.isEmptyOrNull(et_transRemiPayeebank.getChoiceTextContent())){
            isOkToNext=false;
            theErrorMessage=getResources().getString(R.string.trans_error_choose_bank);
            showErrorDialog(theErrorMessage);
            return isOkToNext;}
        if (et_transRemitOrgName.getVisibility()==View.VISIBLE
                &&StringUtils.isEmptyOrNull(et_transRemitOrgName.getChoiceTextContent())){
            isOkToNext=false;
            theErrorMessage=getResources().getString(R.string.trans_error_choose_org);
            showErrorDialog(theErrorMessage);
            return isOkToNext;}
//        if (payerAccountSelected.getAccountType().equals("190")&&!payeeSelectedBean.isLinked()){
//            isOkToNext=false;
//            theErrorMessage="中银信用卡只支持关联账户转账";
//            showErrorDialog(theErrorMessage);
//            return isOkToNext;
//        }

        //190
        if (ApplicationConst.ACC_TYPE_BOCINVT.equals(payerAccountSelected.getAccountType())){
            if (et_transRemitPayeeName.getText().toString().equals(context1.getUser().getCustomerName())
//                    &&transPayeeAccountNumber.equals(ofaBankAccount.getAccountNumber())){
                    &&transPayeeAccountNumber.equals(payeeLinkedAcc.getAccountNumber())){
                if(null==payeeSelectedBean&&et_transRemiPayeebank.getVisibility()==View.VISIBLE){
                    //说明用户有修改过账户或者收款人信息
                    isOkToNext = false;
                    theErrorMessage = getResources().getString(R.string.trans_error_financialAcc_tips);
                    showErrorDialog(theErrorMessage);
                    return isOkToNext;
                }
            }else{
                isOkToNext = false;
                theErrorMessage = getResources().getString(R.string.trans_error_financialAcc_tips);
                showErrorDialog(theErrorMessage);
                return isOkToNext;
            }
        }

        if(et_executeType.getVisibility()==View.VISIBLE){
            if(StringUtils.isEmptyOrNull(et_executeType.getChoiceTextContent())||StringUtils.isEmptyOrNull(executeType)
                    ||StringUtils.isEmptyOrNull(executeTypeName)){
                showErrorDialog("请选择转账方式");
                isOkToNext = false;
                return isOkToNext;}
            if(isBocTrans()){
                if(executeTypeName.equals("普通")){
                    showErrorDialog("请修改转账方式");
                    isOkToNext = false;
                    return isOkToNext;}
            }else{
                if (executeTypeName.equals("实时")){
                    if (Double.valueOf(transMoneyAmount)>TRANS_REMIT_LIMIT){
                        showErrorDialog("跨行实时转账金额不能大于5万，请修改");
                        isOkToNext = false;
                        return isOkToNext;
                    }
                    if (!isRealtimeBank(payeeBankName)){
                        showErrorDialog("收款银行不支持实时转账，请修改");
                        isOkToNext = false;
                        return isOkToNext;
                    }
                }
                if(executeTypeName.equals("普通")){
                   if ( et_transRemitOrgName.getVisibility()!=View.VISIBLE){
                       showErrorDialog("请重新选择转账方式");
                       isOkToNext = false;
                       return isOkToNext;
                   }
                }
            }
        }

        //校验手机号
        if(et_transRemitPayeePhone1.getVisibility()==View.VISIBLE && !getRegexResult(TransRegex.REGEX_TRANS_PAYEEMOBILE, transPayeeMobile,false)){
            isOkToNext=false;
            return isOkToNext;
        }
        //校验附言
        if( !getRegexResult(TransRegex.REGEX_TRANS_REMARK, et_transRemitMessage1.getEditWidgetContent().trim(),false)){
            isOkToNext=false;
            return isOkToNext;
        }

        isOkToNext=true;
        return isOkToNext;
    }
    public boolean checkPayeeAccNo(){
        /**
         * 校验收款人账号，因为需要先判断转账类型，所以单独拿出来校验
         */
        if(ApplicationConst.ACC_TYPE_BOCINVT.equals(payerAccountSelected.getAccountType())){

        }
        else  if (TRANS_TO_LINKED.equals(transtype)||TRANS_TO_BOC.equals(transtype)||TRANS_TO_BOC_DIR .equals(transtype)){
            if(!getRegexResult(TransRegex.REGEX_TRANS_BOC_ACCNO,transPayeeAccountNumber,true)){
                isOkToNext=false;
            }else {
                isOkToNext=true;
            }
        }else{
            if (!getRegexResult(TransRegex.REGEX_TRANS_NATIONAL_ACCNO,transPayeeAccountNumber,true)){
                isOkToNext=false;
            }else{
                isOkToNext=true;
            }
            if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(payerAccountSelected.getAccountType())){
                if(TRANS_TO_NATIONAL.equals(transtype)||TRANS_TO_NATIONAL_DIR.equals(transtype)){
                    isOkToNext = false;
                    theErrorMessage ="中银信用卡跨行转账仅支持5万及以下实时转账";//报错信息以网银为准
                    showErrorDialog(theErrorMessage);
                    return isOkToNext;
                }
            }

            if (ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(payerAccountSelected.getAccountType())){
                if(!TRANS_TO_LINKED.equals(transtype)){
                    isOkToNext = false;
                    theErrorMessage ="非关联账户转账不支持外币，请修改";//报错信息以网银为准
                    showErrorDialog(theErrorMessage);
                    return isOkToNext;
                }
            }
        }
        if (TRANS_TO_LINKED.equals(transtype)) {
            if (null!=payeeLinkedAccCurrencyList) {
                if(null== getLinkedAccByAccId(payeeSelectedBean.getAccountId())){
                    return false;
                }
                String linedPayeeAccType= getLinkedAccByAccId(payeeSelectedBean.getAccountId()).getAccountType();
                if (linedPayeeAccType.equals(ApplicationConst.ACC_TYPE_GRE)||linedPayeeAccType.equals(ApplicationConst.ACC_TYPE_SINGLEWAIBI)
                        ||linedPayeeAccType.equals(ApplicationConst.ACC_TYPE_ZHONGYIN)){
                    if (!payeeLinkedAccCurrencyList.contains(transCurrency)){
                        showErrorDialog("收款账户不支持该币种转账，请修改");
                        isOkToNext = false;
                        return isOkToNext;
                    }
                }
            }
            if(et_executeType.getVisibility()==View.VISIBLE){//关联账户转账不显示转账方式
                showErrorDialog("请重新选择收款账户");
                isOkToNext = false;
                return isOkToNext;
            }
        }else{
            //外币转账仅支持关联账户
            if(!StringUtils.isEmptyOrNull(transCurrency)&&!ApplicationConst.CURRENCY_CNY.equals(transCurrency)){
                showErrorDialog(getResources().getString(R.string.trans_error_currency_wrong));
                isOkToNext = false;
                return isOkToNext;
            }
        }
        return isOkToNext;
    }
    /**
     * @param regex 正则
     * @param inputStr 输入的string
     * @param isMust 是否必输
     * @return boolean
     */
    public boolean getRegexResult(String regex,String inputStr,boolean isMust){
        RegexResult  regexResult = RegexUtils.check(mContext,regex,
                inputStr,isMust);
        if (!regexResult.isAvailable){
            showErrorDialog(regexResult.errorTips);
            return false;
        }else{
            return true;}
    }

    /**
     * 设置transtype值
     */
    public void setTransType(){

        if(ApplicationConst.ACC_TYPE_BOCINVT.equals(payerAccountSelected.getAccountType())){
            transtype=TRANS_TO_LINKED;
            executeType="0";}
        else if(null==payeeSelectedBean){
//            if (getResources().getString(R.string.trans_boc).equals(mCurrentSelectBankEntity.getBankName())){
            if (getResources().getString(R.string.trans_boc).equals(et_transRemiPayeebank.getChoiceTextContent())){
                transtype=TRANS_TO_BOC;
            }else{
//                if (Double.valueOf(transMoneyAmount)<=TRANS_REMIT_LIMIT&&
//                        isRealtimeBank(mCurrentSelectBankEntity.getBankName())){
                if (executeTypeName.equals("实时")){
                    transtype=TRANS_TO_NATIONAL_REALTIME;
                }else{
                    transtype=TRANS_TO_NATIONAL;}

            }
        }else{
            //收款账户标志(0他行,1中行,2信用卡(对私),3二代支付,4跨境汇款收款人)
            if (!payeeSelectedBean.isLinked()) {
                if ("1".equals(payeeSelectedBean.getBocFlag())) {
//                       if ("1".equals(payeeSelectedBean.getIsAppointed())&&isTrueAppointed) {
                    if ("1".equals(payeeSelectedBean.getIsAppointed())) {
                        transtype = TRANS_TO_BOC_DIR;
                    } else {
                        transtype = TRANS_TO_BOC;}
                }
                if ("0".equals(payeeSelectedBean.getBocFlag())) {
                    if ("1".equals(payeeSelectedBean.getIsAppointed())) {
//                        if (Double.valueOf(transMoneyAmount) <= TRANS_REMIT_LIMIT
//                                &&isRealtimeBank(payeeBankName)) {
                        if (executeTypeName.equals("实时")){
                            transtype = TRANS_TO_NATIONAL_REALTIME_DIR;
                        }else{
                            transtype = TRANS_TO_NATIONAL_DIR;
                        }
                    } else {
                        if (executeTypeName.equals("实时")){
                            transtype = TRANS_TO_NATIONAL_REALTIME;
                        }else{
                            transtype = TRANS_TO_NATIONAL;
                        }
                    }
                }
                if ("3".equals(payeeSelectedBean.getBocFlag())) {
//                       if ("1".equals(payeeSelectedBean.getIsAppointed())&&isTrueAppointed) {
                    if ("1".equals(payeeSelectedBean.getIsAppointed())) {
//                        if (Double.valueOf(transMoneyAmount) <= TRANS_REMIT_LIMIT) {
                        if (executeTypeName.equals("实时")){
                            transtype = TRANS_TO_NATIONAL_REALTIME_DIR;
                        }else{
                            transtype = TRANS_TO_NATIONAL_DIR;
                        }
//                        } else {
//                            transtype = TRANS_TO_NATIONAL_DIR;
//                        }
                    } else {
//                        if (Double.valueOf(transMoneyAmount) <= TRANS_REMIT_LIMIT) {
                        if (executeTypeName.equals("实时")){
                            transtype = TRANS_TO_NATIONAL_REALTIME;
                        }else{
                            transtype = TRANS_TO_NATIONAL;}
//                        } else{
//                            transtype = TRANS_TO_NATIONAL;}
                    }}
            }else{
                if(et_transRemitPayeeName.getText().toString().trim().equals(context1.getUser().getCustomerName())
                        &&transPayeeAccountNumber.equals(payeeSelectedBean.getAccountNumber())){
                    transtype = TRANS_TO_LINKED;
                    executeType="0";}
                else{
                    transtype = TRANS_TO_BOC;}}}
    }

    //获取银行列表,解析文档（张海星）
    public List<BankEntity> getHotBankList() {
        List<BankEntity> hotBankEntityList = new ArrayList<>();
        InputStream in = null;
        try {
            in = mActivity.getAssets().open("hotbank.txt");
            hotBankEntityList = parserXml(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotBankEntityList;
    }
    //解析银行实体xml
    private List<BankEntity> parserXml(InputStream is) {
        List<BankEntity> bankEntityList = new ArrayList<BankEntity>();
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            BankEntity bankEntity = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                if (eventType == XmlPullParser.START_TAG) {
                    if ("com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.BankEntity".equals(name)) {
                        bankEntity = new BankEntity();
                    } else if ("bankAlias".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankAlias(text);
                    } else if ("bankBpm".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankBpm(text);
                    } else if ("bankBtp".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankBtp(text);
                    } else if ("bankCcpc".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankCcpc(text);
                    } else if ("bankClr".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankClr(text);
                    } else if ("bankCode".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankCode(text);
                    } else if ("bankName".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankName(text);
                    } else if ("bankStatus".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankStatus(text);
                    } else if ("bankType".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankType(text);
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if ("com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.BankEntity".equals(name)) {
                        bankEntity.setBankNamePinyin(PinYinUtil.getPinYin(bankEntity.getBankAlias()));
                        bankEntityList.add(bankEntity);
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bankEntityList;
    }

    @Override
    protected void titleRightIconClick() {
        hideSoftInput();
        start(new TransMoreFragment());
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return true;
    }

    @Override
    protected View getTitleBarView() {
        TitleBarView titleBarView = (TitleBarView) super.getTitleBarView();
        titleBarView.setRightButton(getResources().getDrawable(R.drawable.boc_overview_more));//横着的。。。me
        return titleBarView;
    }
    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();

    }
    // 用来重置页面
//    private boolean  isFromResultPage=false;//用来和isPayeeSavedFanally 来判断收款人是否需要查询后台

    @Override
    public boolean onBack() {
        if (!isFromResult){
            return super.onBack();
        }else{
            if ("".equals(functionId)){
                return super.onBack();
            }else if(TransRemitBlankFragment.ACCOUNT_FROM_GOLDSTOREBUY.equals(functionId)){
//            start(new BuyMainActivity());//跳回贵金属积存
                ActivityManager.getAppManager().finishActivity();//关了转账汇款的activity，跳回贵金属积存
            }else if(TransRemitBlankFragment.ACCOUNT_FROM_ACCOUNTMANAGEMENT.equals(functionId)){
//            start(new CurrentFragment());//跳回账户管理
                popToAndReInit(OverviewFragment.class);
            }else if(TransRemitBlankFragment.ACCOUNT_FROM_PAYEEMANAGEMENT.equals(functionId)){
                popTo(PayeeManageFragment2.class,false);
            }else if(TransRemitBlankFragment.ACCOUNT_FROM_ACCOUTDETAILFRAGMENT.equals(functionId) ){
                popToAndReInit(AccountDetailFragment.class);
            }else if(TransRemitBlankFragment.ACCOUNT_FROM_PURCHASEFRAGMENT.equals(functionId)){
                popToAndReInit(PurchaseFragment.class);
            }
            return false;
        }
    }

    private  boolean  isFromResult=false;
    @Override
    public void reInit() {
        super.reInit();
//        isFromResultPage=true;
        isPayeeAccCcrd=false;
        payerAndPayeeMainInfo=null;
        et_transRemitMoney1.setmContentMoneyEditText("");
        et_transRemitPayeeName.setText("");
        onlyQueryPayeeToJump=false;
        transPayeeName="";
        payeeSelectedBean=null;
        transMoneyAmount="";
        et_Currency.setChoiceTextContent("");
        setExecuteTypeVisible(false);
        line_executeType.setVisibility(View.GONE);
        et_executeType.setChoiceTextContent("实时");
        executeType="0";
        executeTypeName="实时";
        et_transRemitPayeeAccNo1.setEditWidgetContent("");
        transPayeeAccountNumber="";
        transPayeeAccountNumberBefore=transPayeeAccountNumber;
        et_transRemiPayeebank.setChoiceTextContent("");
        et_transRemiPayeebank.setVisibility(View.GONE);
        line_bank.setVisibility(View.GONE);
        payeeBankCnapsCode="";
        et_transRemitOrgName.setChoiceTextContent("");
        et_transRemitOrgName.setVisibility(View.GONE);
        line_org.setVisibility(View.GONE);
        payeeBankCnapsCodeOrg="";
        et_transRemitPayeePhone1.setVisibility(View.GONE);
        et_transRemitPayeePhone1.setEditWidgetContent("");
        line_mobile.setVisibility(View.GONE);
        transPayeeMobile="";
        et_transRemitMessage1.setEditWidgetContent("");
        view_savePayee.setVisibility(View.GONE);
        cb_savePayee.setChecked(false);
//        transtype="";
//        et_executeType.setChoiceTextContent("");
        payeeModel=null;
        payeeList=new ArrayList<>();
//        linkedAccountResult=null;
        verifyInfoModel=null;
        bocVerifyParams=null;
        dirBocVerifyParams=null;
        nationalRealtimeVerifyParams=null;
        nationalVerifyParams=null;
        dirNationalRealtimeVerifyParams=null;
        dirNationalVerifyParams=null;
        isOkToNext=false;
        isPayerAccSelected=false;
        payerAccCurrencyList.clear();//币种
        payerAccCashRemitList.clear();//c钞汇
        payerAccBalanceList.clear();//可用余额
        currencyListTmp.clear();//币种
//        isTrueAppointed=true;
//        payeeLinkedAccCashRemitList.clear();
//        payeeLinkedAccCurrencyList.clear();
        payerAccCurrencyList.clear();
        payerAccCashRemitList.clear();
        payerAccBalanceList.clear();
        ll_transPayerAccountBalance.setVisibility(View.GONE);
        et_tansPayerAccout.setChoiceTextContent("");
        isFromResult=true;
        queryAccoutDetailInfo(payerAccountSelected);
    }

    @Override
    public void queryQuotaForTransSuccess(PsnTransQuotaQueryResult result) {
        if(executeType.equals("1")){
            if (Double.valueOf(transMoneyAmount)>TRANS_REMIT_LIMIT_TOTOAL){
                closeProgressDialog();
                quotaWarningDailog.setNoticeContent(getResources().getString(R.string.trans_quato_warning));
                quotaWarningDailog.show();
            }else{
//                getPresenter().getConverIdandSaftyFator(getServiceId(transtype));
                queryVerifyFunc(conversationId,factorIdToVerify);
            }
        }else if(executeType.equals("0")){
            if ((Double.valueOf(result.getQuotaAmount()) +Double.valueOf(transMoneyAmount))>TRANS_REMIT_LIMIT_TOTOAL){
                closeProgressDialog();
                quotaWarningDailog.setNoticeContent(getResources().getString(R.string.trans_quato_warning));
                quotaWarningDailog.show();
            }else{
//                getPresenter().getConverIdandSaftyFator(getServiceId(transtype));
                queryVerifyFunc(conversationId,factorIdToVerify);
            }}else{}
    }
    @Override
    public void queryQuotaForTransFailed(BiiResultErrorException exception) {
//        closeProgressDialog();
//        showErrorDialog(exception.getErrorMessage());
        queryVerifyFunc(conversationId,factorIdToVerify);
    }
    @Override
    protected PsnTransBlankPagePresenter initPresenter() {
        return new PsnTransBlankPagePresenter(this);
    }
}
