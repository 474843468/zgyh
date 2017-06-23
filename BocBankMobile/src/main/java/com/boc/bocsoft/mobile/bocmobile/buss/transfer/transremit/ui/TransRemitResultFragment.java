package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.PsnQueryTransActivityStatus.PsnQueryTransActivityStatusResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordDetail.PsnTransQueryTransferRecordDetailParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordDetail.PsnTransQueryTransferRecordDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.countdowntime.CountDownPollingView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.countdowntime.CountDownPollingView.CountDownPollingTaskListener;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ShareInfoFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.OverviewFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.model.cabiienteracty.TransRemitCABIIEnterActyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.PayeeDetailFragment2;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.PayeeManageFragment2;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.ui.TransferRecordFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.TransRemitResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.presenter.PsnTransResultPagePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.ui.AccountDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.widget.BaseResultView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui.PurchaseFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.widget.imageLoader.ImageLoader;
import com.boc.bocsoft.mobile.mbcg.activityinfo.ActivityInfoResult;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import org.threeten.bp.LocalDateTime;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by wangyuan on 2016/6/24.
 */
public class TransRemitResultFragment extends MvpBussFragment<PsnTransResultPagePresenter> implements TransContract.TransViewResultPage{

//    private TextView  im_award;  //去掉之前的抽奖判断 deleted by cry7096
    private TitleAndBtnDialog showAwardDialog;//抽奖提示弹框
    private BaseResultView resultView;
    private View rootView;
    private String transMode;//转账类型
    private String functionFrom;//从哪个功能过来的
    private IWXAPI api;//第三方app和微信通信的接口
    private static TransRemitResultFragment transResultFragment;
    private CountDownPollingView<PsnTransQueryTransferRecordDetailResult> timeCounter;
    private TransferService transferService;
    private TransRemitResultViewModel resultDataModel;
    private boolean  canGoback;//能否点击左上角返回 或者物理键返回
    // 分享至微信
    public static final int TRANS_SHARE_WEIXIN = 0;
    // 继续转账
    public static final int TRANS_AGAIN = 1;
    // 转账记录
    public static final int TRANS_RECORD = 2;

    public static final Double TRANS_AWARD_LIMIT = 100.00;

    boolean isPayeeMobileExist;//收款人手机号存在
    boolean isOrgnameExist;//开户行存在
    boolean isRemarkExist;
    boolean isBoc;//是否是中国银行
    boolean isOrderNextDay;//是否是次日到账类型

    private ImageView imageView; //抽奖图片
    private String actyUrl; //微信url
    private TransRemitCABIIEnterActyModel transRemitCABIIEnterActyModel;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        super.onCreateView(mInflater);
        rootView = mInflater.from(mContext).inflate(R.layout.trans_remit_result_fragment, null);
        return rootView;
    }

    public static TransRemitResultFragment getInstance(String transType, TransRemitResultViewModel dataModel) {
        Bundle bundle = new Bundle();
        bundle.putString("TRANSMODE", transType);
        bundle.putParcelable(transType, dataModel);
//        if (null == transResultFragment)
            transResultFragment = new TransRemitResultFragment();
        transResultFragment.setArguments(bundle);
        return transResultFragment;
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        transMode = getArguments().getString("TRANSMODE");
        resultDataModel = getArguments().getParcelable(transMode);
        functionFrom=resultDataModel.getFunctionFrom();
        if(transMode.equals(TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME)
                ||transMode.equals(TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME_DIR)){
            canGoback=false;
        }else{
            canGoback=true;
        }
    }

    @Override
    public void initView() {
        super.initView();
        api = WXAPIFactory.createWXAPI(mContext, ApplicationConst.APP_ID, false);
        api.registerApp(ApplicationConst.APP_ID);//将应用的APPID注册到微信
//        im_award= (TextView) rootView.findViewById(R.id.im_trans_award);
//        im_award.setVisibility(View.GONE);
        resultView = mViewFinder.find(R.id.trans_result_view);
        if(!StringUtils.isEmptyOrNull(functionFrom)){
            resultView.updateGoHome("完成");
        }
        timeCounter= (CountDownPollingView) rootView.findViewById(R.id.lyt_countdown);
        resultView.setOnHomeBackClick(new com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView.HomeBackListener() {
            @Override public void onHomeBack() {
                if ("".equals(functionFrom)){
                    ActivityManager.getAppManager().finishActivity();
                    //                    popToAndReInit(TransRemitBlankFragment.class);
                }else if(TransRemitBlankFragment.ACCOUNT_FROM_GOLDSTOREBUY.equals(functionFrom)){
                    //                    start(new BuyMainActivity());//跳回贵金属积存
                    ActivityManager.getAppManager().finishActivity();//关了转账汇款的activity，跳回贵金属积存
                }else if(TransRemitBlankFragment.ACCOUNT_FROM_ACCOUNTMANAGEMENT.equals(functionFrom)){
                    //                    start(new CurrentFragment());//跳回账户管理
                    popToAndReInit(OverviewFragment.class);
                }else if(TransRemitBlankFragment.ACCOUNT_FROM_PAYEEMANAGEMENT.equals(functionFrom)){
                    //                    start(new CurrentFragment());//跳回收款人详情
                    popTo(PayeeDetailFragment2.class,false);
                }else if(TransRemitBlankFragment.ACCOUNT_FROM_ACCOUTDETAILFRAGMENT.equals(functionFrom) ){
                    popToAndReInit(AccountDetailFragment.class);
                }else if(TransRemitBlankFragment.ACCOUNT_FROM_PURCHASEFRAGMENT.equals(functionFrom)){
                    popToAndReInit(PurchaseFragment.class);
                }
            }
        });
        showAwardDialog=new TitleAndBtnDialog(mContext);
        showAwardDialog.getTvNotice().setGravity(Gravity.LEFT);
    }

    @Override
    public void initData() {
        transferService=new TransferService();
        if (!StringUtils.isEmptyOrNull(resultDataModel.getPayeeMobile())){
            isPayeeMobileExist=true;
        }else {
            isPayeeMobileExist=false;
        }
        if("1".equals(resultDataModel.getExecuteType())&&!StringUtils.isEmptyOrNull(resultDataModel.getExecuteDate())){
            isOrderNextDay=true;
        }else{
            isOrderNextDay=false;
        }
        if(transMode.equals(TransRemitBlankFragment.TRANS_TO_NATIONAL)||transMode.equals(TransRemitBlankFragment.TRANS_TO_NATIONAL_DIR)){
            isOrgnameExist=true;
        }else{
            isOrgnameExist=false;
        }
        if (!StringUtils.isEmptyOrNull(resultDataModel.getRemark())){
            isRemarkExist=true;
        }else{
            isRemarkExist=false;
        }
        if (transMode.equals(TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME)||
                transMode.equals(TransRemitBlankFragment.TRANS_TO_NATIONAL_REALTIME_DIR)){
            timeCounter.setVisibility(View.VISIBLE);
            timeCounter.setTips(getResources().getString(R.string.trans_dealing_tips));
            timeCounter.setTotalTime(20);
            timeCounter.setPeriod(5);
            timeCounter.setCountDownTimeListener(countDownPollingTaskListener);
            timeCounter.startCountDownTime(new Func1<Integer, Observable<PsnTransQueryTransferRecordDetailResult>>() {
                @Override
                public Observable call(Integer integer) {
                    PsnTransQueryTransferRecordDetailParams params=new PsnTransQueryTransferRecordDetailParams();
                    params.setTransId(resultDataModel.getTransactionId());
                    return getPresenter().transNationalRecordDetail(params);
                }
            },new Func1<PsnTransQueryTransferRecordDetailResult, Boolean>() {
                @Override
                public Boolean call(PsnTransQueryTransferRecordDetailResult t) {
                    if ("A".equals(t.getStatus())||"B".equals(t.getStatus())||"12".equals(t.getStatus())||"K".equals(t.getStatus())){
                        return true;
                    }else{
                        return false;
                    }
                }
            });
        }else{
            setResultContentAndShow(resultDataModel);
        }

        /**
        * Created by cry7096 on 2016/12/26.
        * 增加结果页面-判断是否符合抽奖条件-跳微信H5界面
        */
        getPresenter().subscribe();//不显示等待按钮
        //判断是否满足抽奖资格，并返回票信息
        getPresenter().queryTransActivityStatus(resultDataModel.getTransactionId());
//        getPresenter().queryTransActivityStatus("2224928233");
    }

    private  String statusStr;
    public ResultHead.Status getTransResultStatus(String status){
        switch (status){
            case "A":
//                isResultOk=true;
                statusStr="交易成功";
                return ResultHead.Status.SUCCESS;
            case "B":
            case "12":
            case "K":
//                isResultOk=false;
                statusStr="交易失败";
                return ResultHead.Status.FAIL;
            default:
//                isResultOk=true;
                statusStr="银行处理中";
                return ResultHead.Status.INPROGRESS;
        }
    }
    // TODO: 2016/9/24  公共方法，显示交易结果 王帆
    //设置底部链接，只有跨行交易且交易结果为成功才会显示微信分享，
    public void setResultContentAndShow(TransRemitResultViewModel resultDataModel  ){
        timeCounter.setVisibility(View.GONE);
        resultView.setVisibility(View.VISIBLE);

        //交易状态
        resultView.addStatus(getTransResultStatus(resultDataModel.getStatus()),
                        (MoneyUtils.transMoneyFormat(resultDataModel.getAmount(), resultDataModel.getCurrency()) +
                                getCurrencyAndCashRemitString(resultDataModel.getCurrency(), resultDataModel.getCashRemit()))+statusStr);

        LinkedHashMap<String, String> topMaps = new LinkedHashMap<>();

        //如果费用失败了，就显示报错语
        if(!ApplicationConst.ACC_TYPE_BOCINVT.equals(resultDataModel.getAccType())){
            if(null==resultDataModel.getFinalCommissionCharge()){
                topMaps.put(getResources().getString(R.string.trans_commsion_fee), getResources().getString(R.string.trans_error_commision_fee_failed));
            }else{
                topMaps.put(getResources().getString(R.string.trans_commsion_fee), MoneyUtils.transMoneyFormat(resultDataModel.getFinalCommissionCharge(), resultDataModel.getCurrency()));
            }
        }
        topMaps.put(getResources().getString(R.string.trans_transId), resultDataModel.getTransactionId());
        resultView.addTopDetail(topMaps);

        //交易详情
        LinkedHashMap<String, String> detailMaps = new LinkedHashMap<>();

        detailMaps.put(getResources().getString(R.string.trans_payee_name), resultDataModel.getPayeeName());
//        resultview.addDetailRow(getResources().getString(R.string.trans_payee_accno), NumberUtils.formatCardNumber (resultDataModel.getToAccountNumber()));
        detailMaps.put(getResources().getString(R.string.trans_payee_accno), NumberUtils.formatCardNumber2(resultDataModel.getToAccountNumber()));

        if (transMode.equals(TransRemitBlankFragment.TRANS_TO_LINKED)||transMode.equals(TransRemitBlankFragment.TRANS_TO_BOC)||transMode.equals(TransRemitBlankFragment.TRANS_TO_BOC_DIR)){
//            bankLocation=PublicCodeUtils.getTransferIbk(mContext,resultDataModel.getPayeeBankIbkNum());
//            resultview.addDetailRow(getResources().getString(R.string.trans_payee_bank_location),bankLocation );
            isBoc=true;
        }else{
            isBoc=false;
            detailMaps.put(getResources().getString(R.string.trans_payee_bankname), resultDataModel.getBankname());
            if (isOrgnameExist)
                detailMaps.put(getResources().getString(R.string.trans_payee_orgname), resultDataModel.getToOrgname());
        }
        if (!TransRemitBlankFragment.TRANS_TO_LINKED.equals(transMode)){
            detailMaps.put("转账方式",resultDataModel.getExecuteTypeName());
        }
        if (isOrderNextDay){
            detailMaps.put("预约执行日期",resultDataModel.getExecuteDate());
        }
        if (isPayeeMobileExist) {
            detailMaps.put(getResources().getString(R.string.trans_payee_mobile), NumberUtils.formatMobileNumber(resultDataModel.getPayeeMobile()));
        }

        if (isRemarkExist){
            detailMaps.put(getResources().getString(R.string.trans_remark), resultDataModel.getRemark());
        }
//        resultview.addDetailRow(getResources().getString(R.string.trans_payer_accno), NumberUtils.formatCardNumberStrong(resultDataModel.getFromAccountNum())+" "+PublicCodeUtils.getTransferIbk(mContext,resultDataModel.getPayerAccIbkNum()));
        detailMaps.put(getResources().getString(R.string.trans_payer_accno), NumberUtils.formatCardNumberStrong(resultDataModel.getFromAccountNum()));
//        resultview.addDetailRow(getResources().getString(R.string.trans_payer_accno), NumberUtils.formatCardNumber2(resultDataModel.getFromAccountNum())+" "+PublicCodeUtils.getTransferIbk(mContext,resultDataModel.getPayerAccIbkNum()));

        detailMaps.put(getResources().getString(R.string.trans_acc_balance_new2),
                MoneyUtils.transMoneyFormat(resultDataModel.getAvailableBalance2(), resultDataModel.getCurrency()));

        resultView.addDetail(detailMaps);
        //可能需要

        // 分享
        if(!transMode.equals(TransRemitBlankFragment.TRANS_TO_LINKED) && resultDataModel.getStatus().equals("A"))
            resultView.addNeedItem(getResources().getString(R.string.trans_share_weixin), TRANS_SHARE_WEIXIN);
        // 继续转账
        resultView.addNeedItem(getResources().getString(R.string.trans_again), TRANS_AGAIN);
        // 转账记录
        resultView.addNeedItem(getResources().getString(R.string.trans_record), TRANS_RECORD);

        /**
         * Deleted by cry7096 on 2017/1/3
         * 去掉之前的抽奖判断
         */
//        if ("A".equals(resultDataModel.getStatus()) &&!transMode.equals(TransRemitBlankFragment.TRANS_TO_LINKED)
//                &&Double.valueOf(resultDataModel.getAmount())>=TRANS_AWARD_LIMIT&&isTimeForAward()){
////                &&Double.valueOf(resultDataModel.getAmount())>=TRANS_AWARD_LIMIT){
//            im_award.setVisibility(View.VISIBLE);
//        }else{
//            im_award.setVisibility(View.GONE);
//        }
    }

    @Override
    public void setListener() {
        resultView.setNeedListener(new ResultBottom.OnClickListener() {
            @Override
            public void onClick(int id) {
                if (id == TRANS_RECORD) {
                    TransferRecordFragment recordFragment = new TransferRecordFragment();
                    Bundle bundle=new Bundle();
                    recordFragment.setArguments(bundle);
                    if("".equals(functionFrom)){
                        bundle.putInt(TransferRecordFragment.TRANS_FROM_KEY, TransferRecordFragment.TRANS_FROM_RESULT);
                    }else if(TransRemitBlankFragment.ACCOUNT_FROM_ACCOUNTMANAGEMENT.equals(functionFrom)){
                        bundle.putInt(TransferRecordFragment.TRANS_FROM_KEY, TransferRecordFragment.TRANS_FROM_TRANS_OVERVIEW);
                    }
                    start(recordFragment);
                } else if (id == TRANS_AGAIN) {
//                    TransRemitBlankFragment transfragment = new TransRemitBlankFragment();
                    popToAndReInit(TransRemitBlankFragment.class);
                } else if(id==TRANS_SHARE_WEIXIN){
                    String[] moneyInfo = {"转账金额", MoneyUtils.transMoneyFormat(resultDataModel.getAmount(),
                            resultDataModel.getCurrency()) + "元"};
                    ShareInfoFragment fragment = ShareInfoFragment.newInstance("转账交易成功", getWeiXinNameModel(),
                            getWeiXinDataModel(), moneyInfo);
                    start(fragment);
                }else{
                }
            }
        });

        /**
         * Deleted by cry7096 on 2017/1/3
         * 去掉之前的抽奖判断
         */
//        im_award.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String awardString=String.format(getResources().getString(R.string.trans_award_info),resultDataModel.getTransactionId());
//                showAwardDialog.setNoticeContent(awardString);
//                showAwardDialog.show();
//            }
//        });
//        showAwardDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
//            @Override
//            public void onLeftBtnClick(View view) {
//                showAwardDialog.dismiss();
//            }
//
//            @Override
//            public void onRightBtnClick(View view) {
//                if (api.isWXAppInstalled()) {
//                    api.openWXApp();
//                    showAwardDialog.dismiss();
//                } else {
//                    showAwardDialog.dismiss();
//                    showErrorDialog(mContext.getString(R.string.wx_share_message));
//                }
//            }
//        });
    }
    private  String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    //生成微信分享的数据
    public String[] getWeiXinNameModel(){
        if(isBoc){
            if (isRemarkExist){
//                String[] names={"交易日期","收款人名称","收款账号","所属地区","付款人名称","付款账户","附言","交易序号"};
                String[] names={"交易日期","收款人名称","收款账号","付款人名称","付款账号","附言","交易序号"};
                return names;
            }else{
                String[] names={"交易日期","收款人名称","收款账号","付款人名称","付款账号","交易序号"};
//                String[] names={"交易日期","收款人名称","收款账号","所属地区","付款人名称","付款账户","交易序号"};
                return names;
            }

        }else if (isOrgnameExist){
            if (isRemarkExist){
                String[] names={"交易日期","收款人名称","收款账号","收款银行","开户行","付款人名称","付款账号","附言","交易序号"};
                return names;
            }else{
                String[] names={"交易日期","收款人名称","收款账号","收款银行","开户行","付款人名称","付款账号","交易序号"};
                return names;
            }

        }else{
            if (isRemarkExist){
                String[] names={"交易日期","收款人名称","收款账号","收款银行","付款人名称","付款账号","附言","交易序号"};
                return names;
            }else{
                String[] names={"交易日期","收款人名称","收款账号","收款银行","付款人名称","付款账号","交易序号"};
                return names;
            }
        }
    }
    //生成微信分享数据
    public String[] getWeiXinDataModel(){
        String curDate=ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(
                DateFormatters.dateFormatter1);
        if(isBoc){
            if (isRemarkExist){
//                String[] datas={curDate,resultDataModel.getPayeeName(),NumberUtils.formatCardNumberStrong(resultDataModel.getToAccountNumber()),
                String[] datas={curDate,resultDataModel.getPayeeName(),NumberUtils.formatCardNumber2(resultDataModel.getToAccountNumber()),
//                        bankLocation,resultDataModel.getPayerName(),NumberUtils.formatCardNumberStrong(resultDataModel.getFromAccountNum()),resultDataModel.getRemark(),resultDataModel.getTransactionId()};
                        resultDataModel.getPayerName(),NumberUtils.formatCardNumberStrong(resultDataModel.getFromAccountNum()),resultDataModel.getRemark(),resultDataModel.getTransactionId()};
                return datas;
            }else{
//                String[] datas={curDate,resultDataModel.getPayeeName(),NumberUtils.formatCardNumberStrong(resultDataModel.getToAccountNumber()),
                String[] datas={curDate,resultDataModel.getPayeeName(),NumberUtils.formatCardNumber2(resultDataModel.getToAccountNumber()),
//                        bankLocation,resultDataModel.getPayerName(),NumberUtils.formatCardNumberStrong(resultDataModel.getFromAccountNum()),resultDataModel.getTransactionId()};
                        resultDataModel.getPayerName(),NumberUtils.formatCardNumberStrong(resultDataModel.getFromAccountNum()),resultDataModel.getTransactionId()};
                return datas;
            }
        }else if (isOrgnameExist){
            if (isRemarkExist){
//                String[] datas={curDate,resultDataModel.getPayeeName(),NumberUtils.formatCardNumberStrong(resultDataModel.getToAccountNumber()),resultDataModel.getBankname()
                String[] datas={curDate,resultDataModel.getPayeeName(),NumberUtils.formatCardNumber2(resultDataModel.getToAccountNumber()),resultDataModel.getBankname()
                        ,resultDataModel.getToOrgname(),resultDataModel.getPayerName(),NumberUtils.formatCardNumberStrong(resultDataModel.getFromAccountNum()),resultDataModel.getRemark(),resultDataModel.getTransactionId()};
                return datas;
            }else {
//                String[] datas={curDate,resultDataModel.getPayeeName(),NumberUtils.formatCardNumberStrong(resultDataModel.getToAccountNumber()),resultDataModel.getBankname()
                String[] datas={curDate,resultDataModel.getPayeeName(),NumberUtils.formatCardNumber2(resultDataModel.getToAccountNumber()),resultDataModel.getBankname()
                        ,resultDataModel.getToOrgname(),resultDataModel.getPayerName(),NumberUtils.formatCardNumberStrong(resultDataModel.getFromAccountNum()),resultDataModel.getTransactionId()};
                return datas;
            }
        }else{
            if (isRemarkExist){
//                String[] datas={curDate,resultDataModel.getPayeeName(),NumberUtils.formatCardNumberStrong(resultDataModel.getToAccountNumber()),resultDataModel.getBankname()
                String[] datas={curDate,resultDataModel.getPayeeName(),NumberUtils.formatCardNumber2(resultDataModel.getToAccountNumber()),resultDataModel.getBankname()
                        ,resultDataModel.getPayerName(),NumberUtils.formatCardNumberStrong(resultDataModel.getFromAccountNum()),resultDataModel.getRemark(),resultDataModel.getTransactionId()};
                return datas;
            }else {
//                String[] datas={curDate,resultDataModel.getPayeeName(),NumberUtils.formatCardNumberStrong(resultDataModel.getToAccountNumber()),resultDataModel.getBankname()
                String[] datas={curDate,resultDataModel.getPayeeName(),NumberUtils.formatCardNumber2(resultDataModel.getToAccountNumber()),resultDataModel.getBankname()
                        ,resultDataModel.getPayerName(),NumberUtils.formatCardNumberStrong(resultDataModel.getFromAccountNum()),resultDataModel.getTransactionId()};
                return datas;
            }
        }
    }

    /**
     * Deleted by cry7096 on 2017/1/3
     * 去掉之前的抽奖判断
     */
//    //是否符合抽奖时间
    public boolean isTimeForAward(){
//        ActivityInfoResult.ActivityBean activityBean=ApplicationContext.getInstance().getActivityInfoList().get(0);
        //获取保存在全局变量里面的活动列表
        List<ActivityInfoResult.ActivityBean> activityInfoList=((ApplicationContext) mActivity.getApplicationContext()).getActivityInfoList();
        if (null==activityInfoList||activityInfoList.size()==0){
            return false;
        }
        ActivityInfoResult.ActivityBean activityBean1= activityInfoList.get(0);
        if ("1".equals(activityBean1.getIsUsed())){ //
            LocalDateTime nowTime=ApplicationContext.getInstance().getCurrentSystemDate();
            String startTime=activityBean1.getStartTime();
//            String startTime="2020-03-08 01:10:33";
            String endTime=activityBean1.getEndTime();
//            String endTime="2020-03-09 01:10:33";
            //返回的时间是 2016-12-15 11:43:17
            LocalDateTime dateTimeStart= LocalDateTime.parse(startTime.replace("-","/"), DateFormatters.dateAndTimeFormatter);
            LocalDateTime dateTimeEnd= LocalDateTime.parse(endTime.replace("-","/"), DateFormatters.dateAndTimeFormatter);
            if (nowTime.isAfter(dateTimeStart)&&nowTime.isBefore(dateTimeEnd)){
                return true;
            }
        }
        return false;
    }

    //拼接字符串
    public String getCurrencyAndCashRemitString(String currency, String cashRemit) {
        if (ApplicationConst.CURRENCY_CNY.equals(resultDataModel.getCurrency())){
            return "元";
        }else{
            return PublicCodeUtils.getCurrency(mContext, currency) +
                    (StringUtils.isEmptyOrNull(cashRemit) || "00".equals(cashRemit) ? "" : ("01".equals(cashRemit) ? "(钞)" : "(汇)"));
        }
    }

    @Override
    protected void titleLeftIconClick() {
//     if (canGoback){
//         if ("".equals(functionFrom)){
//             popToAndReInit(TransRemitBlankFragment.class);
//         }else if(TransRemitBlankFragment.ACCOUNT_FROM_GOLDSTOREBUY.equals(functionFrom)){
////            start(new BuyMainActivity());//跳回贵金属积存
//             ActivityManager.getAppManager().finishActivity();//关了转账汇款的activity，跳回贵金属积存
//         }else if(TransRemitBlankFragment.ACCOUNT_FROM_ACCOUNTMANAGEMENT.equals(functionFrom)){
////            start(new CurrentFragment());//跳回账户管理
//             popToAndReInit(OverviewFragment.class);
//         }else if(TransRemitBlankFragment.ACCOUNT_FROM_PAYEEMANAGEMENT.equals(functionFrom)){
//             popTo(PayeeManageFragment2.class,false);
//         }else if(TransRemitBlankFragment.ACCOUNT_FROM_ACCOUTDETAILFRAGMENT.equals(functionFrom) ){
//             popToAndReInit(AccountDetailFragment.class);
//         }else if(TransRemitBlankFragment.ACCOUNT_FROM_PURCHASEFRAGMENT.equals(functionFrom)){
//             popToAndReInit(PurchaseFragment.class);
//         }
//     }
        super.titleLeftIconClick();
    }
    @Override
    public boolean onBack() {
        if (canGoback){
            if ("".equals(functionFrom)){
//                ActivityManager.getAppManager().finishActivity();
                popToAndReInit(TransRemitBlankFragment.class);
            }else if(TransRemitBlankFragment.ACCOUNT_FROM_GOLDSTOREBUY.equals(functionFrom)){
                ActivityManager.getAppManager().finishActivity();//关了转账汇款的activity，跳回贵金属积存
            }else if(TransRemitBlankFragment.ACCOUNT_FROM_ACCOUNTMANAGEMENT.equals(functionFrom)){
//            start(new CurrentFragment());//跳回账户管理
                popToAndReInit(OverviewFragment.class);
            }else if(TransRemitBlankFragment.ACCOUNT_FROM_PAYEEMANAGEMENT.equals(functionFrom)){
                popTo(PayeeManageFragment2.class,false);
            }else if(TransRemitBlankFragment.ACCOUNT_FROM_ACCOUTDETAILFRAGMENT.equals(functionFrom) ){
                popToAndReInit(AccountDetailFragment.class);
            }else if(TransRemitBlankFragment.ACCOUNT_FROM_PURCHASEFRAGMENT.equals(functionFrom)){
                popToAndReInit(PurchaseFragment.class);
            }
            return false;
        }
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "操作结果";
    }

    //倒计时组件
    private CountDownPollingTaskListener<PsnTransQueryTransferRecordDetailResult>
            countDownPollingTaskListener =new CountDownPollingTaskListener<PsnTransQueryTransferRecordDetailResult>(){

        @Override
        public void onTaskFailed() {
            canGoback=true;
            resultDataModel.setStatus("");//如果最后还是没有查询回来结果，显示银行处理中
            setResultContentAndShow(resultDataModel);
        }

        @Override
        public void onTaskSuccess(PsnTransQueryTransferRecordDetailResult detailResult) {
            canGoback=true;
            resultDataModel.setFinalCommissionCharge(new BigDecimal(detailResult.getCommissionCharge()));
            resultDataModel.setStatus(detailResult.getStatus());
            setResultContentAndShow(resultDataModel);
        }
    };

    /**
     * 查询抽奖资格返回成功函数——包括七种交易类型
     */
    @Override
    public void queryTransActivityStatusSuccess(PsnQueryTransActivityStatusResult result) {
        //如果没有可参与的活动，直接返回
        if(result==null || result.getActyList()==null || 0 == result.getActyList().size())
            return;
        //已约定抽奖图片长宽比3:1
        imageView = new ImageView(getContext());
        int w = getResources().getDisplayMetrics().widthPixels;
        int h = w / 3;
        //图片url—加载图片-如果url没变化就取缓存里的，如果没请求回来或者请求中显示默认图片

        if(null == result.getActyList().get(0).getActyPicUrl()){
            ImageLoader.with(getContext()).load(R.drawable.trans_lottery_default).into(imageView);
        }else{
            ImageLoader.with(getContext()).load(result.getActyList().get(0).getActyPicUrl()).resize(w, h)
//        ImageLoader.with(getContext()).load("http://22.11.140.11/WeiBankFiles/WeiBankFiles/1408605605866.png").resize(w, h)
                    .skipDiskCache(false)
                    .skipMemoryCache(false)
                    .placeholder(R.drawable.trans_lottery_default)
                    .error(R.drawable.trans_lottery_default)
                    .into(imageView);
        }

        resultView.addBottomView(imageView); //将图片加载上去
        //设置图片长宽
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = w;
        layoutParams.height = h;
        imageView.setLayoutParams(layoutParams);

        //写入微信传值model——真数据
        transRemitCABIIEnterActyModel= new TransRemitCABIIEnterActyModel();
        String ticketMsg = result.getCustomerId() + "|" + result.getCifNumber() + "|" + "2" + "|"
                + result.getOwnerIbkNum() + "|" + resultDataModel.getTransactionId()
                + "|" + result.getFirstSubmitDate() + "|" + result.getServiceId() + "|" +
                result.getAmount() + "|" + result.getActType();
        transRemitCABIIEnterActyModel.setTicketMsg(ticketMsg);
        transRemitCABIIEnterActyModel.setChannel("2");
        transRemitCABIIEnterActyModel.setTokenCode(result.getTicketInfo());
        transRemitCABIIEnterActyModel.setActyId(result.getActyList().get(0).getActyId());
        actyUrl = result.getActyUrl(); //活动url链接

        //当具有抽奖资格时，点击图片跳转微信页面——不能放在setListener里，因为没有初始化
        imageView.setClickable(true);
        imageView.setFocusable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(WechatActivityFragment.getInstance(actyUrl, transRemitCABIIEnterActyModel));
            }
        });
    }

    /**
     * 查询抽奖资格返回失败函数--do nothing
     */
    @Override
    public void queryTransActivityStatusFailed() {

    }

//    @Override
//    public void transNationalRealtimeRecordSuccess(final PsnSingleTransQueryTransferRecordResult recordResult) {
//        closeProgressDialog();
//        resultDataModel.setFinalCommissionCharge(new BigDecimal(recordResult.getCommissionCharge()));
//        resultDataModel.setStatus(recordResult.getStatus());
////        if (timeCounter.getVisibility()!=View.VISIBLE){
//            timeCounter.setVisibility(View.VISIBLE);
//            timeCounter.setTips(getResources().getString(R.string.trans_dealing_tips));
//            timeCounter.setTotalTime(Integer.valueOf(recordResult.getWaitTimeForRealTime()));
//            timeCounter.setPeriod(Integer.valueOf(recordResult.getDefaultTimeForRealTime()));
//            timeCounter.setCountDownTimeListener(countDownPollingTaskListener);
//            timeCounter.startCountDownTime(new Func1<Integer, Observable<PsnSingleTransQueryTransferRecordResult>>() {
//                @Override
//                public Observable call(Integer integer) {
//                    PsnSingleTransQueryTransferRecordParams params =new PsnSingleTransQueryTransferRecordParams();
//                    params.setTransId(resultDataModel.getTransactionId());
//                    return transferService.psnSingleTransQueryTransferRecord(params);
//                }
//            },new Func1<PsnSingleTransQueryTransferRecordResult, Boolean>() {
//                @Override
//                public Boolean call(PsnSingleTransQueryTransferRecordResult t) {
//                    if ("A".equals(t.getStatus())||"B".equals(t.getStatus())||"12".equals(t.getStatus())||"K".equals(t.getStatus())){
//                        return true;
//                    }else{
//                        return false;
//                    }
//                }
//            });
////        }
//    }
//

//    @Override
//    public void transNationalRealtimeRecordFailed(BiiResultErrorException exception) {
//
//    }

    @Override
    public void transNationalRecordDetailSuccess(PsnTransQueryTransferRecordDetailResult recordResult) {
    }
    @Override
    public void transNationalRecordDetailFailed(BiiResultErrorException exception) {

    }
    @Override
    public void setPresenter(TransContract.TransPresenterResultPage presenter) {
    }

    @Override
    protected PsnTransResultPagePresenter initPresenter() {
        return new PsnTransResultPagePresenter(this);
    }
}
