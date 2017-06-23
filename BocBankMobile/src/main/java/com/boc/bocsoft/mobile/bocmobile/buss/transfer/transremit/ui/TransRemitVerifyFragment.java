package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocNationalTransferVerify.PsnDirTransBocNationalTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocNationalTransferVerify.PsnDirTransBocNationalTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferSubmit.PsnDirTransBocTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferVerify.PsnDirTransBocTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferVerify.PsnDirTransBocTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankAddPayee.PsnDirTransCrossBankAddPayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankAddPayee.PsnDirTransCrossBankAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransfer.PsnDirTransCrossBankTransferParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransfer.PsnDirTransCrossBankTransferResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransferSubmit.PsnDirTransCrossBankTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalAddPayee.PsnDirTransNationalAddPayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalAddPayee.PsnDirTransNationalAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalTransferSubmit.PsnDirTransNationalTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentConfirm.PsnEbpsRealTimePaymentConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentConfirm.PsnEbpsRealTimePaymentConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentSavePayee.PsnEbpsRealTimePaymentSavePayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentSavePayee.PsnEbpsRealTimePaymentSavePayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentTransfer.PsnEbpsRealTimePaymentTransferResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAFinanceTransfer.PsnOFAFinanceTransferResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocAddPayee.PsnTransBocAddPayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocAddPayee.PsnTransBocAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocNationalTransferVerify.PsnTransBocNationalTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocNationalTransferVerify.PsnTransBocNationalTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferSubmit.PsnTransBocTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransLinkTransferSubmit.PsnTransLinkTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalAddPayee.PsnTransNationalAddPayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalAddPayee.PsnTransNationalAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalChangeBooking.PsnTransNationalChangeBookingResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalTransferSubmit.PsnTransNationalTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim.PsnTransPayeeListqueryForDimResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PinYinUtil;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerifyChangeDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransPayeeListqueryForDimViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransPayeeListqueryForDimViewModel.PayeeEntity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.TransRemitResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.TransRemitVerifyInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.presenter.PsnTransVerifyPagePresenter;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.bocmobile.yun.other.DictKey;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by wangy on 2016/6/15.
 * 转账汇款确认页面
 */
public class TransRemitVerifyFragment extends MvpBussFragment<PsnTransVerifyPagePresenter> implements TransContract.TransViewVerifyPage {

    private View verifyView;
    private String transMode;//转账类型payerAccountSelcted
//    private PsnTransVerifyPagePresenter getPresenter();//
    private TransRemitVerifyInfoViewModel verifyInfoViewModel;//从上个页面传过来的model
    private SecurityVerifyChangeDialog verifyChangeDialog;
    private boolean isFactorChanged;//是否是改变了安全认证工具
    private String current_conbinId;//选择的安全因子Id
    private TransferService transService;
    private GlobalService globalService;
    private PsnGetSecurityFactorResult securityFactorResult;
    private SecurityFactorModel securityFactorModel;
    private SecurityVerity securityVerity;//安全认证工具
    private static TransRemitVerifyFragment verifyFragment;
    private AccountBean selectAccoutBean;
    private  String conversationId;//会话ID
    private  String randomNum;//随机数
    private TitleAndBtnDialog  changeBookingDialog;//是否转预约对话框
    private String executeDay;//预约转账日期
    private TransRemitResultViewModel resultInfoViewModel2;
    private ConfirmInfoView confirmInfoView;
    private boolean isBoc=false;//是否是给行内他人转账
    private boolean getPasswordAlready =false;//是否已拿到密码
    private  ApplicationContext context2;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        super.onCreateView(mInflater);
        verifyView = mInflater.inflate(R.layout.trans_remit_verify_fragment, null);
        return verifyView;
    }
//    public static TransRemitVerifyFragment getInstance(String transMode, TransRemitVerifyInfoViewModel model , AccountBean accountBean, List<PayeeEntity> payeeList) {
    public static TransRemitVerifyFragment getInstance(String transMode, TransRemitVerifyInfoViewModel model , AccountBean accountBean) {
        Bundle bundle = new Bundle();
        bundle.putString("TRANSMODE", transMode);
        bundle.putParcelable(transMode, model);
        bundle.putParcelable("verifyParams",accountBean);
//        bundle.putParcelableArrayList("payeeList",payeeList);
//        if (null == verifyFragment)
        verifyFragment = new TransRemitVerifyFragment();
        verifyFragment.setArguments(bundle);
        return verifyFragment;
    }

    @Override
    public void beforeInitView() {
        transMode = getArguments().getString("TRANSMODE");//得到转账类型
        verifyInfoViewModel=null;
        verifyInfoViewModel=getArguments().getParcelable(transMode);
        selectAccoutBean=getArguments().getParcelable("verifyParams");
    }

    @Override
    public void initView() {
//        infoList = (LinearLayout) verifyView.findViewById(R.id.trans_verify_info_listview);
//        bt_submit = (Button) verifyView.findViewById(R.id.trans_verify_submit);
//        transFactorName = (TextView) verifyView.findViewById(R.id.trans_factor_name);
//        titleview= (ConfirmInfoView) verifyView.findViewById(R.id.confirm_view_title);
//        transFactorChange = (TextView) verifyView.findViewById(R.id.trans_factor_change);
//        transVerifySaftyfactor = (LinearLayout) verifyView.findViewById(R.id.trans_verify_saftyfactor);
        confirmInfoView= (ConfirmInfoView) verifyView.findViewById(R.id.confirm_view_title);
        if (!StringUtils.isEmptyOrNull(verifyInfoViewModel.get_plainData()))
                         EShieldVerify.getInstance(getActivity()).setmPlainData(verifyInfoViewModel.get_plainData());
        setViewByTranscode(transMode);
        setBottomInfo(transMode);
        if(transMode!= TransRemitBlankFragment.TRANS_TO_LINKED){
            securityVerity= SecurityVerity.getInstance();
        }

    }

    //跨行转账底部信息
    public void setBottomInfo(String mode){
        switch (mode){
            case TransRemitBlankFragment.TRANS_TO_NATIONAL:
            case TransRemitBlankFragment.TRANS_TO_NATIONAL_DIR:
                confirmInfoView.setBottomHint(getResources().getString(R.string.trans_national_bottom_info));
                break;
            case TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME:
            case TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME_DIR:
                confirmInfoView.setBottomHint(getResources().getString(R.string.trans_realtime_bottom_info));
                break;
            default:
                confirmInfoView.setBottomHint("");
        }
    }
//
//    public void setFactorSMSInfo(boolean isSms){
//        if (isSms){
//            confirmInfoView.setClickHint(getResources().getString(R.string.trans_sms_tips), "查看详情", new MClickableSpan.OnClickSpanListener() {
//                @Override
//                public void onClickSpan() {
//                    start(new TransRemitSmsUrlFragment());
//                }
//            });
//        }else{
//            confirmInfoView.setClickHint("","",null);
//        }
//    }
    //只要用到安全工具也就是非关联账户转账，就展示该提示
    public void setSaftyFatorHintInfo(){
        if (TransRemitBlankFragment.TRANS_TO_LINKED.equals(transMode)){
            confirmInfoView.setClickHint("","",null);
        }else{
            confirmInfoView.setClickHint(getResources().getString(R.string.trans_sms_tips), "查看详情", new MClickableSpan.OnClickSpanListener() {
                @Override
                public void onClickSpan() {
                    start(new TransRemitSmsUrlFragment());
                }
            });
        }
    }
    public void setViewByTranscode(String code) {
        switch (code) {
            case TransRemitBlankFragment.TRANS_TO_LINKED:
                confirmInfoView.isShowSecurity(false);
                isBoc=true;
                break;
            case TransRemitBlankFragment.TRANS_TO_BOC:
            case TransRemitBlankFragment.TRANS_TO_BOC_DIR:
                isBoc=true;
                confirmInfoView.isShowSecurity(true);
                break;
            case TransRemitBlankFragment.TRANS_TO_NATIONAL:
            case TransRemitBlankFragment.TRANS_TO_NATIONAL_DIR:
            case TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME:
            case TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME_DIR:
//                transVerifySaftyfactor.setVisibility(View.VISIBLE);
                isBoc=false;
                confirmInfoView.isShowSecurity(true);
                break;
        }
    }

    /**
     * 获取随机数
     */
    public void getRandomNum(){
        showLoadingDialog();
        getPresenter().getRandomNum(verifyInfoViewModel.getConversationId());
    }

    public void getCommenDeviceInfo(){
        DeviceInfoModel info = DeviceInfoUtils.getDeviceInfo(getContext(), randomNum);
        verifyInfoViewModel.setDeviceInfo(info.getDeviceInfo());
        verifyInfoViewModel.setDeviceInfo_RC(info.getDeviceInfo_RC());
        verifyInfoViewModel.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
        verifyInfoViewModel.setState(SecurityVerity.SECURITY_VERIFY_STATE);
        verifyInfoViewModel.setActiv(SecurityVerity.getInstance().getCfcaVersion());
    }

    @Override
    public void initData() {
        transService = new TransferService();
        globalService = new GlobalService();
        context2= (ApplicationContext) mActivity.getApplicationContext();
        String limit_5w=BocCloudCenter.getInstance().getDirt(DictKey.SAFETOOLSLIMIT);
        TRANS_REMIT_LIMIT_5W=(StringUtils.isEmptyOrNull(limit_5w)?(new Double(50000.00)):Double.valueOf(limit_5w));
        if (transMode != TransRemitBlankFragment.TRANS_TO_LINKED){
            setSecurityInfo();
        }
        setViewAndData();

    }
//请求random成功则展示页面数据
public static  double TRANS_REMIT_LIMIT_5W ;
public void setSecurityInfo(){
    securityVerity.setConversationId(verifyInfoViewModel.getConversationId());
    CombinListBean fatorBean= securityVerity.getDefaultSecurityFactorId(securityVerity.getFactorSecurity());
    List<CombinListBean> fatorBeanList=securityVerity.getFactorSecurity().getCombinList();
    if (null==fatorBeanList){
        showErrorDialog("请重新选择安全工具");
        return;
    }
    if ("1".equals(verifyInfoViewModel.getExecuteType())){
        //如果是预约交易，则默认是中银E盾
       for (CombinListBean onebean:fatorBeanList){
           if ("4".equals(onebean.getId())||"12".equals(onebean.getId())||"36".equals(onebean.getId())){
               fatorBean=onebean;
               break;
           }
       }
       if ("4".equals(fatorBean.getId())||"12".equals(fatorBean.getId())||"36".equals(fatorBean.getId())){
       }else{
           showErrorDialog("预约交易需要使用中银e盾，请先到营业网点办理");
           return;
       }
   } else if("96".equals(fatorBean.getId())&&Double.valueOf(verifyInfoViewModel.getAmount())>TRANS_REMIT_LIMIT_5W) {
        //如果金额大于5w，且默认是96，那么取第二个安全工具
        if (fatorBeanList.size()>1){
            for (CombinListBean onebean:fatorBeanList){
                if (!"96".equals(onebean.getId())&&!"32".equals(onebean.getId())){
                    fatorBean=onebean;
                    break;
                }
            }
        }
    }
    confirmInfoView.updateSecurity(fatorBean.getName());
    current_conbinId = fatorBean.getId();
//    if ("32".equals(current_conbinId)||"96".equals(current_conbinId)){
//        setFactorSMSInfo(true);
//    }else{
//        setFactorSMSInfo(false);
//    }
    setSaftyFatorHintInfo();

    securityVerity.setCurrentSecurityVerifyTypeId(current_conbinId);
//    setViewAndData();
}
//  展示view 和数据
    public void setViewAndData(){
        boolean isPayeeMobileExist;//收款人手机号存在
        boolean isOrgnameExist;//开户行存在
        boolean isRemarkExist;
        if (!StringUtils.isEmptyOrNull(verifyInfoViewModel.getPayeeMobile())){
            isPayeeMobileExist=true;
        }else {
            isPayeeMobileExist=false;
        }
        if(transMode.equals(TransRemitBlankFragment.TRANS_TO_NATIONAL)||transMode.equals(TransRemitBlankFragment.TRANS_TO_NATIONAL_DIR)){
            isOrgnameExist=true;
        }else{
            isOrgnameExist=false;
        }
        if (!StringUtils.isEmptyOrNull(verifyInfoViewModel.getRemark())||!StringUtils.isEmptyOrNull(verifyInfoViewModel.getMemo())){
            isRemarkExist=true;
        }else{
            isRemarkExist=false;
        }
        String  cashremit=verifyInfoViewModel.getCashRemit();
        String  cashRemitFlag="00".equals(cashremit)?"":("01".equals(cashremit)?"/钞":"/汇");
        confirmInfoView.setHeadValue(getResources().getString(R.string.trans_trans_money)+"("+PublicCodeUtils.getCurrency(mContext,verifyInfoViewModel.getCurrency())+cashRemitFlag+")",MoneyUtils.transMoneyFormat(verifyInfoViewModel.getAmount(),verifyInfoViewModel.getCurrency()));
        //nameAndVelue 是字段名和取值的hashmap
        LinkedHashMap<String,String> nameAndVelue=new LinkedHashMap<>();
        if (!ApplicationConst.ACC_TYPE_BOCINVT.equals(selectAccoutBean.getAccountType())){
            if (null!=verifyInfoViewModel.getPreCommissionCharge()&&!StringUtils.isEmptyOrNull(verifyInfoViewModel.getPreCommissionCharge().toString())){
                nameAndVelue.put(getResources().getString(R.string.trans_commsion_fee),MoneyUtils.transMoneyFormat(verifyInfoViewModel.getPreCommissionCharge(),verifyInfoViewModel.getCurrency()));
            }else{
                nameAndVelue.put(getResources().getString(R.string.trans_commsion_fee),getResources().getString(R.string.trans_error_commision_fee_failed));
            }
        }
        nameAndVelue.put(getResources().getString(R.string.trans_payee_name),(verifyInfoViewModel.getPayeeName()));
//        nameAndVelue.put(getResources().getString(R.string.trans_payee_accno),NumberUtils.formatCardNumberStrong(verifyInfoViewModel.getPayeeActno()));
        nameAndVelue.put(getResources().getString(R.string.trans_payee_accno),NumberUtils.formatCardNumber2(verifyInfoViewModel.getPayeeActno()));
            if (isBoc){
//                nameAndVelue.put(getResources().getString(R.string.trans_payee_bank_location),PublicCodeUtils.getTransferIbk(mContext,verifyInfoViewModel.getPayeeIbkNum()));
            }else {
                nameAndVelue.put(getResources().getString(R.string.trans_payee_bankname), (StringUtils.isEmptyOrNull(verifyInfoViewModel.getBankName()) ? verifyInfoViewModel.getPayeeBankName() : verifyInfoViewModel.getBankName()));
                if (isOrgnameExist) {
                    nameAndVelue.put(getResources().getString(R.string.trans_payee_orgname), StringUtils.isEmptyOrNull(verifyInfoViewModel.getPayeeOrgName()) ? verifyInfoViewModel.getToOrgName() : verifyInfoViewModel.getPayeeOrgName());
                }
            }
        if(!transMode.equals(TransRemitBlankFragment.TRANS_TO_LINKED)){
            nameAndVelue.put("转账方式",verifyInfoViewModel.getExecuteTypeName());
            if("1".equals(verifyInfoViewModel.getExecuteType())&&!StringUtils.isEmptyOrNull(verifyInfoViewModel.getExecuteDate())){
                nameAndVelue.put("预约执行日期",verifyInfoViewModel.getExecuteDate());
            }
        }
        if(isPayeeMobileExist){
            nameAndVelue.put(getResources().getString(R.string.trans_payee_mobile), NumberUtils.formatMobileNumber(verifyInfoViewModel.getPayeeMobile()));
        }
        if (isRemarkExist){
            nameAndVelue.put(getResources().getString(R.string.trans_remark),StringUtils.isEmptyOrNull(verifyInfoViewModel.getRemark())?verifyInfoViewModel.getMemo():verifyInfoViewModel.getRemark());
        }
        nameAndVelue.put(getResources().getString(R.string.trans_payer_accno),NumberUtils.formatCardNumberStrong(verifyInfoViewModel.getAccountNumber()));

        confirmInfoView.addData(nameAndVelue,true);
    }

    @Override
    public void setListener() {
        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
                if (transMode.equals(TransRemitBlankFragment.TRANS_TO_LINKED)){
                    showLoadingDialog(false);
                    if(ApplicationConst.ACC_TYPE_BOCINVT.equals(selectAccoutBean.getAccountType())){
                        getPresenter().transPsnOFAFinanceSubmit(verifyInfoViewModel);
                    }else{
                        getPresenter().transLinkTransferSubmit(verifyInfoViewModel);
                    }
                }else{
                    if (isFactorChanged){
                        switch (transMode){
                            case TransRemitBlankFragment.TRANS_TO_BOC:
                                PsnTransBocTransferVerifyParams bocVerifyParams = new
                                        PsnTransBocTransferVerifyParams();
                                bocVerifyParams.setAmount(verifyInfoViewModel.getAmount());
                                bocVerifyParams.setFromAccountId(verifyInfoViewModel.getFromAccountId());
                                bocVerifyParams.setPayeeActno(verifyInfoViewModel.getPayeeActno());
                                bocVerifyParams.setCurrency(verifyInfoViewModel.getCurrency());
                                bocVerifyParams.setRemark(verifyInfoViewModel.getRemark());
                                bocVerifyParams.setPayeeName(verifyInfoViewModel.getPayeeName());
                                bocVerifyParams.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
                                bocVerifyParams.setExecuteType(verifyInfoViewModel.getExecuteType());//0 代表立即执行
                                bocVerifyParams.setExecuteDate(verifyInfoViewModel.getExecuteDate());
                                bocVerifyParams.set_combinId(current_conbinId);
                                bocVerifyParams.setConversationId(verifyInfoViewModel.getConversationId());
                                showLoadingDialog(false);
                                getPresenter().transBocVerify(bocVerifyParams);
                                break;
                            case TransRemitBlankFragment.TRANS_TO_BOC_DIR:
                                PsnDirTransBocTransferVerifyParams dirBocVerifyParams = new PsnDirTransBocTransferVerifyParams();
                                dirBocVerifyParams.setAmount(verifyInfoViewModel.getAmount());
                                dirBocVerifyParams.setFromAccountId(verifyInfoViewModel.getFromAccountId());
                                dirBocVerifyParams.setCurrency(verifyInfoViewModel.getCurrency());
                                dirBocVerifyParams.setExecuteType(verifyInfoViewModel.getExecuteType());
                                dirBocVerifyParams.setExecuteDate(verifyInfoViewModel.getExecuteDate());
                                dirBocVerifyParams.setRemark(verifyInfoViewModel.getRemark());
                                dirBocVerifyParams.setPayeeName(verifyInfoViewModel.getPayeeName());
                                dirBocVerifyParams.setPayeeActno(verifyInfoViewModel.getPayeeActno());
                                dirBocVerifyParams.setPayeeId(verifyInfoViewModel.getPayeeId());
                                dirBocVerifyParams.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
                                dirBocVerifyParams.set_combinId(current_conbinId);
                                dirBocVerifyParams.setConversationId(verifyInfoViewModel.getConversationId());
                                showLoadingDialog(false);
                                getPresenter().transDirBocVerify(dirBocVerifyParams);
                                break;
                            case TransRemitBlankFragment.TRANS_TO_NATIONAL:
                                PsnTransBocNationalTransferVerifyParams nationalVerifyParams = new PsnTransBocNationalTransferVerifyParams();
                                nationalVerifyParams.setAmount(verifyInfoViewModel.getAmount());
                                nationalVerifyParams.setFromAccountId(selectAccoutBean.getAccountId());
                                nationalVerifyParams.setPayeeActno(verifyInfoViewModel.getPayeeActno());
                                nationalVerifyParams.setAccountIbkNum(selectAccoutBean.getAccountIbkNum());
                                nationalVerifyParams.setAccountNumber(selectAccoutBean.getAccountNumber());
                                nationalVerifyParams.setAccountType(selectAccoutBean.getAccountType());
                                nationalVerifyParams.setCurrency(verifyInfoViewModel.getCurrency());
                                nationalVerifyParams.setExecuteType(verifyInfoViewModel.getExecuteType());
                                nationalVerifyParams.setExecuteDate(verifyInfoViewModel.getExecuteDate());
                                nationalVerifyParams.setRemark(verifyInfoViewModel.getRemark());
                                nationalVerifyParams.setPayeeName(verifyInfoViewModel.getPayeeName());
                                nationalVerifyParams.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
                                nationalVerifyParams.setPayeeId(verifyInfoViewModel.getPayeeId());
                                nationalVerifyParams.setCnapsCode(verifyInfoViewModel.getCnapsCode());
                                nationalVerifyParams.setPayeeType(verifyInfoViewModel.getPayeeType());
                                nationalVerifyParams.setBankName(verifyInfoViewModel.getBankName());
                                nationalVerifyParams.setToOrgName(verifyInfoViewModel.getToOrgName());
                                nationalVerifyParams.setSaveAsPayeeYn(verifyInfoViewModel.isSaveAsPayeeYn());
                                nationalVerifyParams.setSendmessageYn(verifyInfoViewModel.isSendmessageYn());
                                nationalVerifyParams.setNickName(verifyInfoViewModel.getNickName());
                                nationalVerifyParams.setNickName(selectAccoutBean.getNickName());
                                nationalVerifyParams.setPayeeNickName(verifyInfoViewModel.getPayeeNickName());
                                nationalVerifyParams.set_combinId(current_conbinId);
                                nationalVerifyParams.setConversationId(verifyInfoViewModel.getConversationId());
                                showLoadingDialog(false);
                                getPresenter().transNationalVerify(nationalVerifyParams);
                                break;
                            case TransRemitBlankFragment.TRANS_TO_NATIONAL_DIR:
                                PsnDirTransBocNationalTransferVerifyParams dirNationalVerifyParams=new PsnDirTransBocNationalTransferVerifyParams();
                                dirNationalVerifyParams.setAmount(verifyInfoViewModel.getAmount());
                                dirNationalVerifyParams.setFromAccountId(verifyInfoViewModel.getFromAccountId());
                                dirNationalVerifyParams.setPayeeActno(verifyInfoViewModel.getPayeeActno());
                                dirNationalVerifyParams.setCurrency(verifyInfoViewModel.getCurrency());
                                dirNationalVerifyParams.setExecuteType(verifyInfoViewModel.getExecuteType());
                                dirNationalVerifyParams.setExecuteDate(verifyInfoViewModel.getExecuteDate());
                                dirNationalVerifyParams.setRemark(verifyInfoViewModel.getRemark());
                                dirNationalVerifyParams.setPayeeName(verifyInfoViewModel.getPayeeName());
                                dirNationalVerifyParams.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
                                dirNationalVerifyParams.setPayeeId(verifyInfoViewModel.getPayeeId());
                                dirNationalVerifyParams.setCnapsCode(verifyInfoViewModel.getCnapsCode());
                                dirNationalVerifyParams.setBankName(verifyInfoViewModel.getBankName());
                                dirNationalVerifyParams.setToOrgName(verifyInfoViewModel.getToOrgName());
                                dirNationalVerifyParams.setRemittanceInfo(verifyInfoViewModel.getRemittanceInfo());
                                dirNationalVerifyParams.set_combinId(current_conbinId);
                                dirNationalVerifyParams.setConversationId(verifyInfoViewModel.getConversationId());
                                showLoadingDialog(false);
                                getPresenter().transDirNationalVerify(dirNationalVerifyParams);
                                break;
                            case TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME:
                                PsnEbpsRealTimePaymentConfirmParams nationalRealtimeVerifyParams=new  PsnEbpsRealTimePaymentConfirmParams();
                                nationalRealtimeVerifyParams.setAmount(verifyInfoViewModel.getAmount());
//                                nationalRealtimeVerifyParams.setTransoutaccparent("活期一本通1020******4370活期一本通 陕西");
                                nationalRealtimeVerifyParams.setFromAccountId(verifyInfoViewModel.getFromAccountId());
                                nationalRealtimeVerifyParams.setPayeeActno(verifyInfoViewModel.getPayeeActno());
                                nationalRealtimeVerifyParams.setPayeeActno2(verifyInfoViewModel.getPayeeActno2());
                                nationalRealtimeVerifyParams.setPayeeName(verifyInfoViewModel.getPayeeName());
                                nationalRealtimeVerifyParams.setPayeeCnaps(verifyInfoViewModel.getPayeeCnaps());
                                nationalRealtimeVerifyParams.setPayeeBankName(verifyInfoViewModel.getPayeeBankName());
                                nationalRealtimeVerifyParams.setPayeeOrgName(verifyInfoViewModel.getPayeeOrgName());
                                nationalRealtimeVerifyParams.setCurrency(verifyInfoViewModel.getCurrency());
                                nationalRealtimeVerifyParams.setSendMsgFlag(verifyInfoViewModel.getSendMsgFlag());
                                nationalRealtimeVerifyParams.setMemo(verifyInfoViewModel.getMemo());
                                nationalRealtimeVerifyParams.setExecuteType(verifyInfoViewModel.getExecuteType());
                                nationalRealtimeVerifyParams.setExecuteDate(verifyInfoViewModel.getExecuteDate());
                                nationalRealtimeVerifyParams.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
                                nationalRealtimeVerifyParams.setRemittanceInfo(verifyInfoViewModel.getRemittanceInfo());
                                nationalRealtimeVerifyParams.set_combinId(current_conbinId);
                                nationalRealtimeVerifyParams.setConversationId(verifyInfoViewModel.getConversationId());
                                showLoadingDialog(false);
                                getPresenter().transNationalRealTimeVerify(nationalRealtimeVerifyParams);
                                break;
                            case TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME_DIR:
                                PsnDirTransCrossBankTransferParams dirNationalRealtimeParams=new PsnDirTransCrossBankTransferParams();
                                dirNationalRealtimeParams.setAmount(verifyInfoViewModel.getAmount());
                                dirNationalRealtimeParams.setFromAccountId(verifyInfoViewModel.getFromAccountId());
                                dirNationalRealtimeParams.setPayeeActno(verifyInfoViewModel.getPayeeActno());
                                dirNationalRealtimeParams.setPayeeName(verifyInfoViewModel.getPayeeName());
                                dirNationalRealtimeParams.setCnapsCode(verifyInfoViewModel.getCnapsCode());
                                dirNationalRealtimeParams.setBankName(verifyInfoViewModel.getBankName());
                                dirNationalRealtimeParams.setToOrgName(verifyInfoViewModel.getToOrgName());
                                dirNationalRealtimeParams.setCurrency(verifyInfoViewModel.getCurrency());
                                dirNationalRealtimeParams.setSendMsgFlag(verifyInfoViewModel.getSendMsgFlag());
                                dirNationalRealtimeParams.setRemark(verifyInfoViewModel.getRemark());
                                dirNationalRealtimeParams.setExecuteType(verifyInfoViewModel.getExecuteType());
                                dirNationalRealtimeParams.setExecuteDate(verifyInfoViewModel.getExecuteDate());
                                dirNationalRealtimeParams.setPayeeId(verifyInfoViewModel.getPayeeId());
                                dirNationalRealtimeParams.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
                                dirNationalRealtimeParams.setRemittanceInfo(verifyInfoViewModel.getRemittanceInfo());
                                dirNationalRealtimeParams.set_combinId(current_conbinId);
                                dirNationalRealtimeParams.setConversationId(verifyInfoViewModel.getConversationId());
                                showLoadingDialog(false);
                                getPresenter().transDirNationalRealTimeVerify(dirNationalRealtimeParams);
                                break;
                        }
                    }else{
                        getRandomNum();
                    }
                }
            }
            @Override
            public void onClickChange() {
                securityVerity.selectSecurityType();
            }
        });
        if(transMode!= TransRemitBlankFragment.TRANS_TO_LINKED){
            securityVerity.setSecurityVerifyListener(new SecurityVerity.VerifyCodeResultListener() {
                @Override
                public void onSecurityTypeSelected(CombinListBean bean) {
                    if (!current_conbinId.equals(bean.getId())) {
                        //如果改变了安全认证方式，需要重新调用预交易
                        isFactorChanged = true;
                        current_conbinId=bean.getId();
                        confirmInfoView.updateSecurity(bean.getName());
//                        if ("32".equals(current_conbinId)||"96".equals(current_conbinId)){
//                            setFactorSMSInfo(true);
//                        }else{
//                            setFactorSMSInfo(false);
//                        }
                        setSaftyFatorHintInfo();
                    } else {
                        isFactorChanged = false;
                    }
                }
                @Override
                public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
                    //  0不需要上送密码 ，需要上送atm密码 1，和电话银行密码 2，存折密码 3
                    String password=verifyInfoViewModel.getNeedPassword();
                    if ("-1".equals(factorId)){//
                        if ("1".equals(password)){
                            verifyInfoViewModel.setAtmPassword(encryptPasswords[0]);
                            verifyInfoViewModel.setAtmPassword_RC(randomNums[0]);
                            getPasswordAlready =true;
                        }else if("2".equals(password)){
                            verifyInfoViewModel.setPhoneBankPassword(encryptPasswords[0]);
                            verifyInfoViewModel.setPhoneBankPassword_RC(randomNums[0]);
                            getPasswordAlready =true;
                        }else if("3".equals(password)){
                            verifyInfoViewModel.setPassbookPassword(encryptPasswords[0]);
                            verifyInfoViewModel.setPassbookPassword_RC(randomNums[0]);
                            getPasswordAlready =true;
                        }else{
                        }
                    }else{
                        setVerifyInfoViewModelData();
                        if ("8".equals(factorId)) {
                            //动态口令
                            verifyInfoViewModel.setOtp(encryptPasswords[0]);
                            verifyInfoViewModel.setOtp_RC(randomNums[0]);
                        } else if ("32".equals(factorId)) {
                            //短信验证码
                            verifyInfoViewModel.setSmc(encryptPasswords[0]);
                            verifyInfoViewModel.setSmc_RC(randomNums[0]);
                        } else if ("96".equals(factorId)) {
                            //短信验证码+硬件绑定
                            verifyInfoViewModel.setSmc(encryptPasswords[0]);
                            verifyInfoViewModel.setSmc_RC(randomNums[0]);
                        } else if ("40".equals(factorId)) {
                            //动态口令+短信验证码
                            verifyInfoViewModel.setOtp(encryptPasswords[0]);
                            verifyInfoViewModel.setOtp_RC(randomNums[0]);
                            verifyInfoViewModel.setSmc(encryptPasswords[1]);
                            verifyInfoViewModel.setSmc_RC(randomNums[1]);
                        } else if ("4".equals(factorId)) {
                        }
                    }
                    verifyInfoViewModel.set_combinId(current_conbinId);
                 if(!getPasswordAlready && null!=password &&!"0".equals(password)){
                        SecurityVerity.getInstance().showTransPasswordDialog(password);
                 }else{
                     gotoSubmit();
                 }
                }
                @Override
                public void onSignedReturn(String signRetData) {
                    if (!StringUtils.isEmptyOrNull(signRetData)){
                        verifyInfoViewModel.set_signedData(signRetData);
                        gotoSubmit();
                    }
                }
            });
        }
    }

    //清除数据
    public void setVerifyInfoViewModelData(){
        verifyInfoViewModel.setOtp(null);
        verifyInfoViewModel.setOtp_RC(null);
        verifyInfoViewModel.setSmc(null);
        verifyInfoViewModel.setSmc_RC(null);
        verifyInfoViewModel.setAtmPassword(null);
        verifyInfoViewModel.setAtmPassword_RC(null);
        verifyInfoViewModel.setPhoneBankPassword(null);
        verifyInfoViewModel.setPhoneBankPassword_RC(null);
        verifyInfoViewModel.setPassbookPassword(null);
        verifyInfoViewModel.setPassbookPassword_RC(null);

    }

//   提交
    public  void gotoSubmit() {
        showLoadingDialog(false);
        switch (transMode){
            case TransRemitBlankFragment.TRANS_TO_BOC:
                getPresenter().transBocSubmit(verifyInfoViewModel);
                break;
            case TransRemitBlankFragment.TRANS_TO_BOC_DIR:
                getPresenter().transDirBocSubmit(verifyInfoViewModel);
                break;
            case TransRemitBlankFragment.TRANS_TO_NATIONAL:
                getPresenter().transNationalSubmit(verifyInfoViewModel);
                break;
            case TransRemitBlankFragment.TRANS_TO_NATIONAL_DIR:
                getPresenter().transDirNationalSubmit(verifyInfoViewModel);
                break;
            case TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME:
                getPresenter().transNationalRealTimeSubmit(verifyInfoViewModel);
                break;
            case TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME_DIR:
                getPresenter().transDirNationalRealTimeSubmit(verifyInfoViewModel);
                break;
        }
    }

    @Override
    public void transBocVerifySuccess(PsnTransBocTransferVerifyResult result) {
        verifyInfoViewModel.setNeedPassword(result.getNeedPassword());
        verifyInfoViewModel.set_certDN(result.get_certDN());
        EShieldVerify.getInstance(getActivity()).setmPlainData(result.get_plainData());
           closeProgressDialog();
            isFactorChanged=false;
            if( securityVerity.confirmFactor(result.getFactorList())){
//                securityVerity.showSecurityDialog(randomNum);
                getRandomNum();
            }else{
                showErrorDialog(getResources().getString(R.string.trans_error_no_combin_factor));
                return;
            }
     }

    @Override
    public void transBocVerifyFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transDirVerifySuccess(PsnDirTransBocTransferVerifyResult result) {
        closeProgressDialog();
        verifyInfoViewModel.set_certDN(result.get_certDN());
        EShieldVerify.getInstance(getActivity()).setmPlainData(result.get_plainData());
        isFactorChanged=false;
        if( securityVerity.confirmFactor(result.getFactorList())){
//            securityVerity.showSecurityDialog(randomNum);
            getRandomNum();
        }else{
            showErrorDialog(getResources().getString(R.string.trans_error_no_combin_factor));
            return;
        }
    }

    @Override
    public void transDirVerifyFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transDirNationalRealTimeVerifySuccess(PsnDirTransCrossBankTransferResult result) {
        closeProgressDialog();
        verifyInfoViewModel.set_certDN(result.get_certDN());
        EShieldVerify.getInstance(getActivity()).setmPlainData(result.get_plainData());
        isFactorChanged=false;
        if( securityVerity.confirmFactor(result.getFactorList())){
//            securityVerity.showSecurityDialog(randomNum);
            getRandomNum();
        }else{
            showErrorDialog(getResources().getString(R.string.trans_error_no_combin_factor));
            return;
        }
    }

    @Override
    public void transDirNationalRealTimeVerifyFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transNationalVerifySuccess(PsnTransBocNationalTransferVerifyResult result) {
        verifyInfoViewModel.setNeedPassword(result.getNeedPassword());
        EShieldVerify.getInstance(getActivity()).setmPlainData(result.get_plainData());
        verifyInfoViewModel.set_certDN(result.get_certDN());
        closeProgressDialog();
        isFactorChanged=false;
        if( securityVerity.confirmFactor(result.getFactorList())){
//            securityVerity.showSecurityDialog(randomNum);
            getRandomNum();
        }else{
            showErrorDialog(getResources().getString(R.string.trans_error_no_combin_factor));
            return;
        }
    }

    @Override
    public void transNationalVerifyFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transDirNationalVerifySuccess(PsnDirTransBocNationalTransferVerifyResult result) {
        closeProgressDialog();
        isFactorChanged=false;
//
        EShieldVerify.getInstance(getActivity()).setmPlainData(result.get_plainData());
        verifyInfoViewModel.set_certDN(result.get_certDN());
        if( securityVerity.confirmFactor(result.getFactorList())){
//            securityVerity.showSecurityDialog(randomNum);
            getRandomNum();
        }else{
            showErrorDialog(getResources().getString(R.string.trans_error_no_combin_factor));
            return;
        }
    }

    @Override
    public void transDirNationalVerifyFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transNationalRealTimeVerifySuccess(PsnEbpsRealTimePaymentConfirmResult result) {
        verifyInfoViewModel.setNeedPassword(result.getNeedPassword());
        verifyInfoViewModel.set_certDN(result.get_certDN());
        EShieldVerify.getInstance(getActivity()).setmPlainData(result.get_plainData());
        closeProgressDialog();
        isFactorChanged=false;
        if( securityVerity.confirmFactor(result.getFactorList())){
//            securityVerity.showSecurityDialog(randomNum);
            getRandomNum();
        }else{
            showErrorDialog(getResources().getString(R.string.trans_error_no_combin_factor));
            return;
        }
    }

    @Override
    public void transNationalRealTimeVerifyFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }
    @Override
    public void transLinkTransferSubmitSuccess(PsnTransLinkTransferSubmitResult result) {
        closeProgressDialog();
        TransRemitResultViewModel resultInfoViewModel=new TransRemitResultViewModel();
//        isChangeBooking="0";
//        resultInfoViewModel.setIschangeBooking(isChangeBooking);
        resultInfoViewModel.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
        resultInfoViewModel.setAmount(verifyInfoViewModel.getAmount());
        resultInfoViewModel.setBankname(getResources().getString(R.string.trans_boc));
        resultInfoViewModel.setPayeeName(verifyInfoViewModel.getPayeeName());
        resultInfoViewModel.setFromAccountNum(verifyInfoViewModel.getAccountNumber());
        resultInfoViewModel.setRemark(verifyInfoViewModel.getRemark());
        resultInfoViewModel.setToOrgname(verifyInfoViewModel.getToOrgName());
        resultInfoViewModel.setCurrency(verifyInfoViewModel.getCurrency());
//        resultInfoViewModel.setPayeeBankIbkNum(verifyInfoViewModel.ibk);
        resultInfoViewModel.setFinalCommissionCharge(result.getFinalCommissionCharge());
        resultInfoViewModel.setTransactionId(String.valueOf(result.getTransactionId()));
        resultInfoViewModel.setToAccountNumber(result.getToAccountNumber());
        resultInfoViewModel.setAvailableBalance2(getNewBalance(result.getFinalCommissionCharge()));
        resultInfoViewModel.setCashRemit(verifyInfoViewModel.getCashRemit());
        resultInfoViewModel.setPayeeBankIbkNum(verifyInfoViewModel.getPayeeIbkNum());
        resultInfoViewModel.setPayerAccIbkNum(verifyInfoViewModel.getAccountIbkNum());
        resultInfoViewModel.setPayerName(verifyInfoViewModel.getPayerName());
        resultInfoViewModel.setStatus(result.getStatus());
        resultInfoViewModel.setFunctionFrom(verifyInfoViewModel.getFunctionFrom());
        //start(TransRemitResultFragment.getInstance(transMode,resultInfoViewModel));
        goToResultFragment(resultInfoViewModel);
    }

    @Override
    public void transLinkTransferSubmitFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transPsnOFAFinanceSubmitSuccess(PsnOFAFinanceTransferResult result) {
        closeProgressDialog();
        TransRemitResultViewModel resultInfoViewModel=new TransRemitResultViewModel();
//      isChangeBooking="0";
//      resultInfoViewModel.setIschangeBooking(isChangeBooking);
        resultInfoViewModel.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
        resultInfoViewModel.setAmount(verifyInfoViewModel.getAmount());
        resultInfoViewModel.setBankname(getResources().getString(R.string.trans_boc));
        resultInfoViewModel.setPayeeName(verifyInfoViewModel.getPayeeName());
        resultInfoViewModel.setFromAccountNum(verifyInfoViewModel.getAccountNumber());
        resultInfoViewModel.setRemark(verifyInfoViewModel.getRemark());
        resultInfoViewModel.setToOrgname(verifyInfoViewModel.getToOrgName());
        resultInfoViewModel.setCurrency(verifyInfoViewModel.getCurrency());
//      resultInfoViewModel.setPayeeBankIbkNum(verifyInfoViewModel.ibk);
        resultInfoViewModel.setFinalCommissionCharge(new BigDecimal(0.00));
        resultInfoViewModel.setTransactionId(String.valueOf(String.valueOf(result.getTransId())));
        resultInfoViewModel.setToAccountNumber(result.getBankAccount().getAccountNumber());
        resultInfoViewModel.setAvailableBalance2(getNewBalance(new BigDecimal(0.00)));
        resultInfoViewModel.setCashRemit(verifyInfoViewModel.getCashRemit());
        resultInfoViewModel.setPayeeBankIbkNum(verifyInfoViewModel.getPayeeIbkNum());
        resultInfoViewModel.setPayerAccIbkNum(verifyInfoViewModel.getAccountIbkNum());
        resultInfoViewModel.setPayerName(verifyInfoViewModel.getPayerName());
        resultInfoViewModel.setStatus(result.getTransStatus());
        resultInfoViewModel.setFunctionFrom(verifyInfoViewModel.getFunctionFrom());
        resultInfoViewModel.setExecuteTypeName(verifyInfoViewModel.getExecuteTypeName());
        resultInfoViewModel.setExecuteType(verifyInfoViewModel.getExecuteType());
        resultInfoViewModel.setExecuteDate(verifyInfoViewModel.getExecuteDate());
        //start(TransRemitResultFragment.getInstance(transMode,resultInfoViewModel));
        goToResultFragment(resultInfoViewModel);
    }

    @Override
    public void transPsnOFAFinanceSubmitFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transBocSubmitSuccess(PsnTransBocTransferSubmitResult result) {

        TransRemitResultViewModel resultInfoViewModel=new TransRemitResultViewModel();
//        isChangeBooking="0";
//        resultInfoViewModel.setIschangeBooking(isChangeBooking);
        resultInfoViewModel.setPayeeName(verifyInfoViewModel.getPayeeName());
        resultInfoViewModel.setToAccountNumber(verifyInfoViewModel.getPayeeActno());
        resultInfoViewModel.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
        resultInfoViewModel.setFinalCommissionCharge(result.getFinalCommissionCharge());
        resultInfoViewModel.setAvailableBalance2(getNewBalance(result.getFinalCommissionCharge()));
        resultInfoViewModel.setAmount(verifyInfoViewModel.getAmount());
        resultInfoViewModel.setBankname(getResources().getString(R.string.trans_boc));
        resultInfoViewModel.setFromAccountNum(verifyInfoViewModel.getAccountNumber());
        resultInfoViewModel.setRemark(verifyInfoViewModel.getRemark());
        resultInfoViewModel.setToOrgname(verifyInfoViewModel.getToOrgName());
        resultInfoViewModel.setCurrency(verifyInfoViewModel.getCurrency());
        resultInfoViewModel.setCashRemit(verifyInfoViewModel.getCashRemit());
//      start(TransRemitResultFragment.getInstance(transMode,resultInfoViewModel));
        resultInfoViewModel.setTransactionId(String.valueOf(result.getTransactionId()));
        resultInfoViewModel.setPayeeBankIbkNum(verifyInfoViewModel.getPayeeIbkNum());
        resultInfoViewModel.setPayerAccIbkNum(verifyInfoViewModel.getAccountIbkNum());
        resultInfoViewModel.setPayerName(verifyInfoViewModel.getPayerName());
        resultInfoViewModel.setFunctionFrom(verifyInfoViewModel.getFunctionFrom());
        resultInfoViewModel.setStatus(result.getStatus());
        resultInfoViewModel.setExecuteTypeName(verifyInfoViewModel.getExecuteTypeName());
        resultInfoViewModel.setExecuteType(verifyInfoViewModel.getExecuteType());
        resultInfoViewModel.setExecuteDate(verifyInfoViewModel.getExecuteDate());
        resultInfoViewModel2=resultInfoViewModel;

        if (verifyInfoViewModel.isSaveAsPayeeYn()){
            transAddPayee();
        }else{
            closeProgressDialog();
            goToResultFragment(resultInfoViewModel2);
        }
    }

    @Override
    public void transBocSubmitFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        getPasswordAlready =false;
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transBocDirSubmitSuccess(PsnDirTransBocTransferSubmitResult result) {

        TransRemitResultViewModel resultInfoViewModel=new TransRemitResultViewModel();
//        isChangeBooking="0";
//        resultInfoViewModel.setIschangeBooking(isChangeBooking);
        resultInfoViewModel.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
        resultInfoViewModel.setAmount(verifyInfoViewModel.getAmount());
        resultInfoViewModel.setBankname(getResources().getString(R.string.trans_boc));
        resultInfoViewModel.setFromAccountNum(verifyInfoViewModel.getAccountNumber());
        resultInfoViewModel.setRemark(verifyInfoViewModel.getRemark());
        resultInfoViewModel.setToOrgname(verifyInfoViewModel.getToOrgName());
        resultInfoViewModel.setCurrency(verifyInfoViewModel.getCurrency());
        resultInfoViewModel.setFinalCommissionCharge(result.getFinalCommissionCharge());
        resultInfoViewModel.setTransactionId(String.valueOf(result.getTransactionId()));
        resultInfoViewModel.setAvailableBalance2(getNewBalance(result.getFinalCommissionCharge()));
        resultInfoViewModel.setCashRemit(verifyInfoViewModel.getCashRemit());
        resultInfoViewModel.setPayeeName(verifyInfoViewModel.getPayeeName());
        resultInfoViewModel.setToAccountNumber(verifyInfoViewModel.getPayeeActno());
        resultInfoViewModel.setPayeeBankIbkNum(verifyInfoViewModel.getPayeeIbkNum());
        resultInfoViewModel.setPayerAccIbkNum(verifyInfoViewModel.getAccountIbkNum());
        resultInfoViewModel.setPayerName(verifyInfoViewModel.getPayerName());
        resultInfoViewModel.setStatus(result.getStatus());
        resultInfoViewModel.setFunctionFrom(verifyInfoViewModel.getFunctionFrom());
//        start(TransRemitResultFragment.getInstance(transMode,resultInfoViewModel));;
        resultInfoViewModel.setExecuteTypeName(verifyInfoViewModel.getExecuteTypeName());
        resultInfoViewModel.setExecuteType(verifyInfoViewModel.getExecuteType());
        resultInfoViewModel.setExecuteDate(verifyInfoViewModel.getExecuteDate());
        resultInfoViewModel2=resultInfoViewModel;
//        goToResultFragment(resultInfoViewModel2);
        closeProgressDialog();
        goToResultFragment(resultInfoViewModel2);
//        if (verifyInfoViewModel.isSaveAsPayeeYn()){
//            transAddPayee();
//        }else{
//            goToResultFragment(resultInfoViewModel2);
//        }
    }
    @Override
    public void transBocDirSubmitFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transNationalRealTimeSubmitSuccess(PsnEbpsRealTimePaymentTransferResult result) {

        TransRemitResultViewModel resultInfoViewModel=new TransRemitResultViewModel();
//        isChangeBooking="0";
//        resultInfoViewModel.setIschangeBooking(isChangeBooking);
        resultInfoViewModel.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
        resultInfoViewModel.setToAccountNumber(verifyInfoViewModel.getPayeeActno());
        resultInfoViewModel.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
        resultInfoViewModel.setAmount(verifyInfoViewModel.getAmount());
        resultInfoViewModel.setBankname(verifyInfoViewModel.getPayeeBankName() );
        resultInfoViewModel.setFromAccountNum(verifyInfoViewModel.getAccountNumber());
        resultInfoViewModel.setPayeeName(verifyInfoViewModel.getPayeeName());

        resultInfoViewModel.setRemark(StringUtils.isEmptyOrNull(verifyInfoViewModel.getRemark())?
                verifyInfoViewModel.getMemo():verifyInfoViewModel.getRemark());
//        resultInfoViewModel.setToOrgname(verifyInfoViewModel.getToOrgName());
        resultInfoViewModel.setCurrency(verifyInfoViewModel.getCurrency());
        resultInfoViewModel.setFinalCommissionCharge(result.getFinalCommissionCharge());
        resultInfoViewModel.setTransactionId(String.valueOf(result.getTransactionId()));
        resultInfoViewModel.setBatSequence(String.valueOf(result.getBatSequence()));
        resultInfoViewModel.setAvailableBalance2(getNewBalance(result.getFinalCommissionCharge()));
        resultInfoViewModel.setCashRemit(verifyInfoViewModel.getCashRemit());
        resultInfoViewModel.setPayerAccIbkNum(verifyInfoViewModel.getAccountIbkNum());
        resultInfoViewModel.setPayerName(verifyInfoViewModel.getPayerName());
        resultInfoViewModel.setFunctionFrom(verifyInfoViewModel.getFunctionFrom());
        resultInfoViewModel.setExecuteTypeName(verifyInfoViewModel.getExecuteTypeName());
        resultInfoViewModel.setExecuteType(verifyInfoViewModel.getExecuteType());
        resultInfoViewModel.setExecuteDate(verifyInfoViewModel.getExecuteDate());
        resultInfoViewModel2=resultInfoViewModel;
//        goToResultFragment(resultInfoViewModel2);

        if (verifyInfoViewModel.isSaveAsPayeeYn()){
            transAddPayee();
        }else{
            closeProgressDialog();
            goToResultFragment(resultInfoViewModel2);
        }

    }

    @Override
    public void transNationalRealTimeSubmitFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        getPasswordAlready =false;
        showErrorDialog(exception.getErrorMessage());
    }
    private String   submitResultStatus;
    @Override
    public void transDirNationalSubmitSuccess(PsnDirTransNationalTransferSubmitResult result) {

        submitResultStatus=result.getStatus();
            if (!"1".equals(result.getWorkDayFlag())){
                    TransRemitResultViewModel resultInfoViewModel=new TransRemitResultViewModel();

//                isChangeBooking="0";
//                resultInfoViewModel.setIschangeBooking(isChangeBooking);
                    resultInfoViewModel.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
                    resultInfoViewModel.setAmount(verifyInfoViewModel.getAmount());
                    resultInfoViewModel.setBankname(verifyInfoViewModel.getBankName() );
                    resultInfoViewModel.setFromAccountNum(verifyInfoViewModel.getAccountNumber());
                    resultInfoViewModel.setRemark(verifyInfoViewModel.getRemark());
                    resultInfoViewModel.setToOrgname(verifyInfoViewModel.getToOrgName());
                    resultInfoViewModel.setCurrency(verifyInfoViewModel.getCurrency());
                    resultInfoViewModel.setFinalCommissionCharge(result.getFinalCommissionCharge());
                    resultInfoViewModel.setTransactionId(String.valueOf(result.getTransactionId()));
                    resultInfoViewModel.setAvailableBalance2(getNewBalance(result.getFinalCommissionCharge()));
                    resultInfoViewModel.setCashRemit(verifyInfoViewModel.getCashRemit());
                    resultInfoViewModel.setPayerAccIbkNum(verifyInfoViewModel.getAccountIbkNum());
                    resultInfoViewModel.setPayerName(verifyInfoViewModel.getPayerName());
                    resultInfoViewModel.setPayeeName(verifyInfoViewModel.getPayeeName());
                    resultInfoViewModel.setToAccountNumber(verifyInfoViewModel.getPayeeActno());
                    resultInfoViewModel.setStatus(result.getStatus());
                    resultInfoViewModel.setFunctionFrom(verifyInfoViewModel.getFunctionFrom());
                resultInfoViewModel.setExecuteTypeName(verifyInfoViewModel.getExecuteTypeName());
                resultInfoViewModel.setExecuteType(verifyInfoViewModel.getExecuteType());
                resultInfoViewModel.setExecuteDate(verifyInfoViewModel.getExecuteDate());
                    resultInfoViewModel2=resultInfoViewModel;
//            goToResultFragment(resultInfoViewModel2);
                    if (verifyInfoViewModel.isSaveAsPayeeYn()){
                        transAddPayee();
                    }else{
                        closeProgressDialog();
                        goToResultFragment(resultInfoViewModel2);
                    }
            }else{
                executeDay=result.getExecuteDate();
                if (null==changeBookingDialog) {
                    getChangeBookinDialog();
                }
//                isChangeBooking="1";
                closeProgressDialog();
                changeBookingDialog.show();
        }
    }

    @Override
    public void transDirNationalSubmitFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transNationalSubmitSuccess(PsnTransNationalTransferSubmitResult result) {

//        if (isResultOk){

        submitResultStatus=result.getStatus();
            if (!"1".equals(result.getWorkDayFlag())) {
                TransRemitResultViewModel resultInfoViewModel = new TransRemitResultViewModel();
//                isChangeBooking="0";
//                resultInfoViewModel.setIschangeBooking(isChangeBooking);
                resultInfoViewModel.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
                resultInfoViewModel.setAmount(verifyInfoViewModel.getAmount());
                resultInfoViewModel.setBankname(verifyInfoViewModel.getBankName());
                resultInfoViewModel.setFromAccountNum(verifyInfoViewModel.getAccountNumber());
                resultInfoViewModel.setRemark(verifyInfoViewModel.getRemark());
                resultInfoViewModel.setToOrgname(verifyInfoViewModel.getToOrgName());
                resultInfoViewModel.setCurrency(verifyInfoViewModel.getCurrency());
                resultInfoViewModel.setFinalCommissionCharge(result.getFinalCommissionCharge());
                resultInfoViewModel.setTransactionId(String.valueOf(result.getTransactionId()));
                resultInfoViewModel.setAvailableBalance2(getNewBalance(result.getFinalCommissionCharge()));
                resultInfoViewModel.setCashRemit(verifyInfoViewModel.getCashRemit());
                resultInfoViewModel.setPayeeName(verifyInfoViewModel.getPayeeName());
                resultInfoViewModel.setToAccountNumber(verifyInfoViewModel.getPayeeActno());
                resultInfoViewModel.setPayerAccIbkNum(verifyInfoViewModel.getAccountIbkNum());
                resultInfoViewModel.setPayerName(verifyInfoViewModel.getPayerName());
                resultInfoViewModel.setStatus(result.getStatus());
                resultInfoViewModel.setFunctionFrom(verifyInfoViewModel.getFunctionFrom());
                resultInfoViewModel.setExecuteTypeName(verifyInfoViewModel.getExecuteTypeName());
                resultInfoViewModel.setExecuteType(verifyInfoViewModel.getExecuteType());
                resultInfoViewModel.setExecuteDate(verifyInfoViewModel.getExecuteDate());
                resultInfoViewModel2 = resultInfoViewModel;
                if (verifyInfoViewModel.isSaveAsPayeeYn()) {
                    transAddPayee();
                } else {
                    closeProgressDialog();
                    goToResultFragment(resultInfoViewModel2);
                }
            }
            else{
                executeDay=result.getExecuteDate();
                if (null==changeBookingDialog){
                    getChangeBookinDialog();
                }
//                isChangeBooking="1";
                closeProgressDialog();
                changeBookingDialog.show();

            }
//        }
    }

    public void getChangeBookinDialog(){
        changeBookingDialog=new TitleAndBtnDialog(mContext);
        String[] btName={getResources().getString(R.string.trans_bt_cancel),getResources().getString(R.string.trans_bt_comfirm)};
        changeBookingDialog.setBtnName(btName);
        changeBookingDialog.setTitle(getResources().getString(R.string.trans_is_booking));
        changeBookingDialog.setDialogBtnClickListener(
                new TitleAndBtnDialog.DialogBtnClickCallBack() {
                    @Override
                    public void onLeftBtnClick(View view) {
                    }
                    @Override
                    public void onRightBtnClick(View view) {
                        showLoadingDialog();
                        getPresenter().transNationalChangeBooking(verifyInfoViewModel.getConversationId(),verifyInfoViewModel.getIsAppointed());
                    }
                });
    }
    @Override
    public void transNationalSubmitFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        getPasswordAlready =false;
        showErrorDialog(exception.getErrorMessage());
    }

//    private String isChangeBooking;
    @Override
    public void transDirNationalRealTimeSubmitSuccess(PsnDirTransCrossBankTransferSubmitResult result) {

        TransRemitResultViewModel resultInfoViewModel=new TransRemitResultViewModel();
//        isChangeBooking="0";
//        resultInfoViewModel.setIschangeBooking(isChangeBooking);
        resultInfoViewModel.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
        resultInfoViewModel.setPayeeName(verifyInfoViewModel.getPayeeName());
        resultInfoViewModel.setToAccountNumber(verifyInfoViewModel.getPayeeActno());
        resultInfoViewModel.setAmount(verifyInfoViewModel.getAmount());
        resultInfoViewModel.setBankname(verifyInfoViewModel.getBankName() );
        resultInfoViewModel.setFromAccountNum(verifyInfoViewModel.getAccountNumber());
        resultInfoViewModel.setRemark(verifyInfoViewModel.getRemark());
        resultInfoViewModel.setToOrgname(verifyInfoViewModel.getToOrgName());
        resultInfoViewModel.setCurrency(verifyInfoViewModel.getCurrency());
        resultInfoViewModel.setFinalCommissionCharge(result.getFinalCommissionCharge());
        resultInfoViewModel.setTransactionId(String.valueOf(result.getTransactionId()));
        resultInfoViewModel.setAvailableBalance2(getNewBalance(result.getFinalCommissionCharge()));
        resultInfoViewModel.setCashRemit(verifyInfoViewModel.getCashRemit());
        resultInfoViewModel.setPayerAccIbkNum(verifyInfoViewModel.getAccountIbkNum());
        resultInfoViewModel.setPayerName(verifyInfoViewModel.getPayerName());
        resultInfoViewModel.setFunctionFrom(verifyInfoViewModel.getFunctionFrom());
        resultInfoViewModel.setStatus(result.getStatus());
        resultInfoViewModel.setIschangeBooking("1");//0 不是，1 是预约的
        resultInfoViewModel.setExecuteTypeName(verifyInfoViewModel.getExecuteTypeName());
        resultInfoViewModel.setExecuteType(verifyInfoViewModel.getExecuteType());
        resultInfoViewModel.setExecuteDate(verifyInfoViewModel.getExecuteDate());
        resultInfoViewModel2=resultInfoViewModel;
//        goToResultFragment(resultInfoViewModel2);
        if (verifyInfoViewModel.isSaveAsPayeeYn()) {
            transAddPayee();
        } else {
            closeProgressDialog();
            goToResultFragment(resultInfoViewModel2);
        }
    }

    @Override
    public void transDirNationalRealTimeFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void transNationalChangeBookingSuccess(PsnTransNationalChangeBookingResult result) {

        TransRemitResultViewModel resultInfoViewModel=new TransRemitResultViewModel();
        resultInfoViewModel.setIschangeBooking("1");
        resultInfoViewModel.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
        resultInfoViewModel.setAmount(verifyInfoViewModel.getAmount());
        resultInfoViewModel.setBankname(verifyInfoViewModel.getBankName() );
        resultInfoViewModel.setFromAccountNum(verifyInfoViewModel.getAccountNumber());
        resultInfoViewModel.setRemark(verifyInfoViewModel.getRemark());
        resultInfoViewModel.setToOrgname(verifyInfoViewModel.getToOrgName());
        resultInfoViewModel.setCurrency(verifyInfoViewModel.getCurrency());
        resultInfoViewModel.setFinalCommissionCharge(result.getCommissionCharge());
        resultInfoViewModel.setTransactionId(String.valueOf(result.getTransactionId()));
        resultInfoViewModel.setBatSequence(String.valueOf(result.getBatSeq()));
        resultInfoViewModel.setExecuteDate(executeDay);
        resultInfoViewModel.setCashRemit(verifyInfoViewModel.getCashRemit());
        resultInfoViewModel.setAvailableBalance2(getNewBalance(result.getCommissionCharge()));
        resultInfoViewModel.setPayerAccIbkNum(verifyInfoViewModel.getAccountIbkNum());
        resultInfoViewModel.setFunctionFrom(verifyInfoViewModel.getFunctionFrom());
        resultInfoViewModel.setStatus(submitResultStatus);
        resultInfoViewModel.setExecuteTypeName(verifyInfoViewModel.getExecuteTypeName());
        resultInfoViewModel.setExecuteType(verifyInfoViewModel.getExecuteType());
        resultInfoViewModel.setExecuteDate(verifyInfoViewModel.getExecuteDate());
        resultInfoViewModel2=resultInfoViewModel;
//        goToResultFragment(resultInfoViewModel2);
        if (verifyInfoViewModel.isSaveAsPayeeYn()) {
            transAddPayee();
        } else {
            closeProgressDialog();
            goToResultFragment(resultInfoViewModel2);
        }
    }

    public String getNewBalance(BigDecimal commisonCharge){
        if (null==commisonCharge){
            return "";
        }
        BigDecimal nb;
        nb=BigDecimal.valueOf(Double.valueOf(verifyInfoViewModel.getAmount())).add(commisonCharge);
        return verifyInfoViewModel.getAvailableBalance().subtract(nb).toString();
    }

    @Override
    public void transNationalChangeBookingFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void setPresenter(TransContract.TransPresenterVerifyPage presenter) {

    }

    public void goToResultFragment(TransRemitResultViewModel resultInfoViewModel){
        //跳转页面之前，如果本次转账使用的付款账户与本地存储的不一样，则更新//
        String payerAcc=BocCloudCenter.getInstance().getAccountId(AccountType.ACC_TYPE_TRANSREMIT);
        if (!verifyInfoViewModel.getFromAccountId().equals(payerAcc)){
            BocCloudCenter.getInstance().updateLastAccountId
                    (AccountType.ACC_TYPE_TRANSREMIT,verifyInfoViewModel.getFromAccountId());
        }
        start(TransRemitResultFragment.getInstance(transMode,resultInfoViewModel));
    }
    //保存常用收款人
    public void transAddPayee(){
//        showLoadingDialog(false);
//        PayeeEntity newPayeeEntity=new PayeeEntity();
        switch (transMode) {
            case TransRemitBlankFragment.TRANS_TO_BOC_DIR:
                break;
            case TransRemitBlankFragment.TRANS_TO_BOC:
//            case TransRemitBlankFragment.TRANS_TO_LINKED:
                PsnTransBocAddPayeeParams bocPayeeParams=new PsnTransBocAddPayeeParams();
                bocPayeeParams.setPayeeBankNum(verifyInfoViewModel.getPayeeBankNum());
                bocPayeeParams.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
                bocPayeeParams.setToAccountId(verifyInfoViewModel.getPayeeActno());
                bocPayeeParams.setToAccountType(verifyInfoViewModel.getToAccountType());
                bocPayeeParams.setPayeeName(verifyInfoViewModel.getPayeeName());
                getPresenter().saveBocPayee(bocPayeeParams);
                break;
            case TransRemitBlankFragment.TRANS_TO_NATIONAL:
                PsnTransNationalAddPayeeParams nationalPayeeParams
                        =new PsnTransNationalAddPayeeParams();
                nationalPayeeParams.setPayeeName(verifyInfoViewModel.getPayeeName());
                nationalPayeeParams.setBankName(verifyInfoViewModel.getBankName());
                nationalPayeeParams.setCnapsCode(verifyInfoViewModel.getCnapsCode());
                nationalPayeeParams.setToOrgName(verifyInfoViewModel.getToOrgName());
                nationalPayeeParams.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
                nationalPayeeParams.setToAccountId(verifyInfoViewModel.getPayeeActno());
                getPresenter().saveNationalPayee(nationalPayeeParams);
                break;
            case TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME:
                PsnEbpsRealTimePaymentSavePayeeParams nationalRealTimePayeeParams
                        =new PsnEbpsRealTimePaymentSavePayeeParams();
                nationalRealTimePayeeParams.setPayeeName(verifyInfoViewModel.getPayeeName());
                nationalRealTimePayeeParams.setPayeeBankName(verifyInfoViewModel.getPayeeBankName());
                nationalRealTimePayeeParams.setPayeeCnaps(verifyInfoViewModel.getPayeeCnaps());
                nationalRealTimePayeeParams.setPayeeOrgName(verifyInfoViewModel.getPayeeOrgName());
                nationalRealTimePayeeParams.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
                nationalRealTimePayeeParams.setPayeeActno(verifyInfoViewModel.getPayeeActno());
                getPresenter().saveNationalRealtimePayee(nationalRealTimePayeeParams);
                break;
            case TransRemitBlankFragment.TRANS_TO_NATIONAL_DIR:
                PsnDirTransNationalAddPayeeParams dirTransNationalAddPayeeParams
                        = new PsnDirTransNationalAddPayeeParams();
                dirTransNationalAddPayeeParams.setBankName(verifyInfoViewModel.getBankName());
                dirTransNationalAddPayeeParams.setCnapsCode(verifyInfoViewModel.getCnapsCode());
                dirTransNationalAddPayeeParams.setPayeeActno(verifyInfoViewModel.getPayeeActno());
                dirTransNationalAddPayeeParams.setPayeeId(verifyInfoViewModel.getPayeeId());
                dirTransNationalAddPayeeParams.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
                dirTransNationalAddPayeeParams.setPayeeName(verifyInfoViewModel.getPayeeName());
                dirTransNationalAddPayeeParams.setToOrgName(verifyInfoViewModel.getToOrgName());
                getPresenter().saveDirNationalPayee(dirTransNationalAddPayeeParams);
              break;
            case TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME_DIR:
                PsnDirTransCrossBankAddPayeeParams  dirNationalRealtimePayeeParams
                        =new PsnDirTransCrossBankAddPayeeParams();
                dirNationalRealtimePayeeParams.setBankName(verifyInfoViewModel.getBankName());
                dirNationalRealtimePayeeParams.setCnapsCode(verifyInfoViewModel.getCnapsCode());
                dirNationalRealtimePayeeParams.setPayeeActno(verifyInfoViewModel.getPayeeActno());
                dirNationalRealtimePayeeParams.setPayeeId(verifyInfoViewModel.getPayeeId());
                dirNationalRealtimePayeeParams.setPayeeMobile(verifyInfoViewModel.getPayeeMobile());
                dirNationalRealtimePayeeParams.setPayeeName(verifyInfoViewModel.getPayeeName());
                dirNationalRealtimePayeeParams.setToOrgName(verifyInfoViewModel.getToOrgName());
                getPresenter().saveDirRealtimeNationalPayee(dirNationalRealtimePayeeParams);
                break;

            default:
        }
    }

//    public void addPayeeForSave(){
//        PayeeEntity newPayee=new PayeeEntity();
//        newPayee.setAccountName(verifyInfoViewModel.getPayeeName());
//        newPayee.setMobile(verifyInfoViewModel.getPayeeMobile());
//        newPayee.setAccountNumber(verifyInfoViewModel.getPayeeActno());
//        String pinYin = PinYinUtil.getPinYin(verifyInfoViewModel.getPayeeName()).trim();
//        pinYin = TextUtils.isEmpty(pinYin) ? "z" : pinYin.toUpperCase();
//        newPayee.setPinyin(pinYin);
//        if (!StringUtils.isEmptyOrNull(verifyInfoViewModel.getPayeeId())){
//            newPayee.setPayeetId(Integer.valueOf(verifyInfoViewModel.getPayeeId()));
//        }
//        switch (transMode){
//            case TransRemitBlankFragment.TRANS_TO_BOC:
//            case TransRemitBlankFragment.TRANS_TO_BOC_DIR:
//                newPayee.setAccountIbkNum(verifyInfoViewModel.getPayeeBankNum());
//                newPayee.setBocFlag("1");
//                newPayee.setBankName("中国银行");
//               break;
//            case TransRemitBlankFragment.TRANS_TO_NATIONAL:
//                newPayee.setCnapsCode(verifyInfoViewModel.getCnapsCode());
//                newPayee.setBankName(verifyInfoViewModel.getBankName());
//                newPayee.setAddress(verifyInfoViewModel.getToOrgName());
//                newPayee.setBocFlag("0");
//                break;
//            case TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME:
//                newPayee.setBankName(verifyInfoViewModel.getPayeeBankName());
//                newPayee.setAddress(verifyInfoViewModel.getPayeeOrgName());
//                newPayee.setCnapsCode(verifyInfoViewModel.getPayeeCnaps());
//                newPayee.setBocFlag("3");
//                break;
//            case TransRemitBlankFragment.TRANS_TO_NATIONAL_DIR:
//                newPayee.setCnapsCode(verifyInfoViewModel.getCnapsCode());
//                newPayee.setBankName(verifyInfoViewModel.getBankName());
//                newPayee.setAddress(verifyInfoViewModel.getToOrgName());
//                newPayee.setBocFlag("0");
////                newPayee.setPayeetId(Integer.valueOf(verifyInfoViewModel.getPayeeId()));
//                break;
//            case TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME_DIR:
//                newPayee.setCnapsCode(verifyInfoViewModel.getCnapsCode());
//                newPayee.setBankName(verifyInfoViewModel.getBankName());
//                newPayee.setAddress(verifyInfoViewModel.getToOrgName());
//                newPayee.setBocFlag("3");
////                newPayee.setPayeetId(Integer.valueOf(verifyInfoViewModel.getPayeeId()));
//                break;
//        }
//        context2.getPayeeEntityList().add(newPayee);
//    }

    public void updatePayeeListinCache(){
        getPresenter().updatePayeeList();
    }

    @Override
    public void addTransPayeeSuccess(PsnTransBocAddPayeeResult result) {

    }

    @Override
    public void addTransPayeeFailed(BiiResultErrorException exception) {

    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.trans_title_comfirm);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void getRandomNumSuccess(String number) {
        closeProgressDialog();
        randomNum=number;
        getCommenDeviceInfo();
        securityVerity.showSecurityDialog(randomNum);
    }

    @Override
    public void getRandomNumFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    public void addBocPayeeSuccess(PsnTransBocAddPayeeResult result) {
//        closeProgressDialog();
        updatePayeeListinCache();
//        goToResultFragment(resultInfoViewModel2);
    }

    @Override
    public void addBocPayeeFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        goToResultFragment(resultInfoViewModel2);
    }

    @Override
    public void addNationalPayeeSuccess(PsnTransNationalAddPayeeResult result) {
//        closeProgressDialog();
//        addPayeeForSave();
        updatePayeeListinCache();
//        goToResultFragment(resultInfoViewModel2);
    }

    @Override
    public void addNationalPayeeFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        goToResultFragment(resultInfoViewModel2);
    }

    @Override
    public void addNationalRealtimePayeeSuccess(PsnEbpsRealTimePaymentSavePayeeResult result) {
        updatePayeeListinCache();
    }

    @Override
    public void addNationalRealtimePayeeFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        goToResultFragment(resultInfoViewModel2);
    }

    @Override
    public void addDirNationalRealtimePayeeSuccess(PsnDirTransCrossBankAddPayeeResult result) {
        updatePayeeListinCache();
    }

    @Override
    public void addDirNationalRealtimePayeeFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        goToResultFragment(resultInfoViewModel2);
    }

    @Override
    public void addDirNationalPayeeSuccess(PsnDirTransNationalAddPayeeResult result) {
//        closeProgressDialog();
//        addPayeeForSave();
//        goToResultFragment(resultInfoViewModel2);
        updatePayeeListinCache();
    }

    @Override
    public void addDirNationalPayeeFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        goToResultFragment(resultInfoViewModel2);
    }

    @Override
    public void updatePayeeListSuccess(PsnTransPayeeListqueryForDimResult result) {
        context2.getPayeeEntityList().clear();
        context2.getPayeeEntityList().addAll(getPayeeEntityListFromResult(result));
        closeProgressDialog();
        goToResultFragment(resultInfoViewModel2);
    }
    public List<PayeeEntity> payeeEntityListTmp;
    // 把请求结果转换为ViewModel，给收款人选择页面用
    private  List<PayeeEntity> getPayeeEntityListFromResult(PsnTransPayeeListqueryForDimResult psnTransPayeeListqueryForDimResult) {
        payeeEntityListTmp=new ArrayList<>();
        List<PsnTransPayeeListqueryForDimResult.PayeeAccountBean> accountBeanList = psnTransPayeeListqueryForDimResult.getList();
//        viewModel.getPayeeEntityList().clear();
        for (PsnTransPayeeListqueryForDimResult.PayeeAccountBean payeeAccountBean : accountBeanList) {
            PsnTransPayeeListqueryForDimViewModel.PayeeEntity payeeEntity = new PsnTransPayeeListqueryForDimViewModel.PayeeEntity();
            BeanConvertor.toBean(payeeAccountBean, payeeEntity);
            payeeEntity.setPinyin("");
            payeeEntityListTmp.add(payeeEntity);
        }
        return payeeEntityListTmp;
    }
    @Override
    public void updatePayeeListFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        goToResultFragment(resultInfoViewModel2);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected PsnTransVerifyPagePresenter initPresenter() {
        return new PsnTransVerifyPagePresenter(this);
    }
}
