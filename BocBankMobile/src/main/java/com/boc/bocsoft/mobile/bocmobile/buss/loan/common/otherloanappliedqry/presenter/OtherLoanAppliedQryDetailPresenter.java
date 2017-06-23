package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.presenter;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanDetailQry.PsnOnLineLoanDetailQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanDetailQry.PsnOnLineLoanDetailQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.model.OtherLoanAppliedQryDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.ui.OtherLoanAppliedQryDetailContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * 其他类型贷款-单笔贷款详情 通信逻辑处理
 * Created by liuzc on 2016/8/16.
 */
public class OtherLoanAppliedQryDetailPresenter extends RxPresenter implements OtherLoanAppliedQryDetailContract.Presenter {

    private OtherLoanAppliedQryDetailContract.View mLoanOnlineView;
    private PsnLoanService mLoanService;

    /**
     * 会话
     */
    private String conversationID;

    /**
     * 查询转账记录service
     */

    public OtherLoanAppliedQryDetailPresenter(OtherLoanAppliedQryDetailContract.View loanOnlineQryView){
        mLoanOnlineView = loanOnlineQryView;
        mLoanOnlineView.setPresenter(this);
        mLoanService = new PsnLoanService();
    }

    /**
     * 设置会话ID
     * @param value
     */
    public void setConverSationID(String value){
        conversationID = value;
    }

    @Override
    public void queryOtherLoanOnlineDetail(final OtherLoanAppliedQryDetailViewModel viewModel) {
//        //测试代码
//        OtherLoanAppliedQryDetailViewModel result = new OtherLoanAppliedQryDetailViewModel();
//
//        result.setProductName("一手住房贷款"); //产品名称
//        result.setAppName("张三"); //姓名
//        result.setAppAge("20"); //年龄
//        result.setAppSex("1");    //性别: 1:男、2:女
//        result.setAppPhone("13831489900");    //联系电话
//        result.setAppEmail("dafasfdsa@163.com");    //Email地址
//        result.setHouseTradePrice("23000");    //房屋交易价
//        result.setTuitionTradePrice("");    //学费生活费总额
//
//        result.setCarTradePrice(""); //净车价
//        result.setLoanAmount("2000"); //贷款金额
//        result.setLoanTerm("12个月"); //贷款期限
//        result.setCurrency("1");    //币种
//        result.setHouseAge("12年");    //所购住房房龄
//        result.setGuaWay("");    //担保方式
//        result.setDeptName("中国银行北京王府井支行");    //网点名称
//        result.setDeptAddr("北京市东城区东方广场103号");    //网点地址
//
//        result.setLoanStatus("4"); //贷款状态
//        result.setEntName(""); //企业名称
//        result.setOfficeAddress(""); //办公地址
//        result.setMainBusiness("");    //主营业务
//        result.setPrincipalName("");    //负责人姓名
//        result.setGuaTypeFlag("");    //是否能提供抵押担保
//        result.setGuaType("");    //担保类别
//        result.setRefuseReason("1");    //拒绝原因
//        mLoanOnlineView.queryOtherLoanOnlineDetailSuccess(result);

        PsnOnLineLoanDetailQryParams params =
                buildParamsFromView(viewModel);
        params.setConversationId(conversationID);

        mLoanService.psnOnLineLoanDetailQry(params)
                .compose(this.<PsnOnLineLoanDetailQryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnOnLineLoanDetailQryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnOnLineLoanDetailQryResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnOnLineLoanDetailQryResult qryResult) {
                        OtherLoanAppliedQryDetailViewModel viewModel = buildViewModelFromResult(qryResult);
                        mLoanOnlineView.queryOtherLoanOnlineDetailSuccess(viewModel);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mLoanOnlineView.queryOtherLoanOnlineDetailFail(biiResultErrorException);
                    }
                });
    }

    //根据屏幕数据构造请求参数
    private PsnOnLineLoanDetailQryParams buildParamsFromView(OtherLoanAppliedQryDetailViewModel viewModel){
        PsnOnLineLoanDetailQryParams result = new PsnOnLineLoanDetailQryParams();

        result.setLoanNumber(viewModel.getLoanNumber());

        return result;
    }
    //根据网络返回结果构造屏幕显示数据
    private OtherLoanAppliedQryDetailViewModel buildViewModelFromResult(PsnOnLineLoanDetailQryResult qryResult){
        OtherLoanAppliedQryDetailViewModel result = new OtherLoanAppliedQryDetailViewModel();

        result.setProductName(qryResult.getProductName()); //产品名称
        result.setAppName(qryResult.getAppName()); //姓名
        result.setAppAge(qryResult.getAppAge()); //年龄
        result.setAppSex(qryResult.getAppSex());	//性别: 1:男、2:女
        result.setAppPhone(qryResult.getAppPhone());	//联系电话
        result.setAppEmail(qryResult.getAppEmail());	//Email地址
        result.setHouseTradePrice(qryResult.getHouseTradePrice());	//房屋交易价
        result.setTuitionTradePrice(qryResult.getTuitionTradePrice());	//学费生活费总额

        result.setCarTradePrice(qryResult.getCarTradePrice()); //净车价
        result.setLoanAmount(qryResult.getLoanAmount()); //贷款金额
        result.setLoanTerm(qryResult.getLoanTerm()); //贷款期限
        result.setCurrency(qryResult.getCurrency());	//币种
        result.setHouseAge(qryResult.getHouseAge());	//所购住房房龄
        result.setGuaWay(qryResult.getGuaWay());	//担保方式
        result.setDeptName(qryResult.getDeptName());	//网点名称
        result.setDeptAddr(qryResult.getDeptAddr());	//网点地址

        result.setLoanStatus(qryResult.getLoanStatus()); //贷款状态
        result.setEntName(qryResult.getEntName()); //企业名称
        result.setOfficeAddress(qryResult.getOfficeAddress()); //办公地址
        result.setMainBusiness(qryResult.getMainBusiness());	//主营业务
        result.setPrincipalName(qryResult.getPrincipalName());	//负责人姓名
        result.setGuaTypeFlag(qryResult.getGuaTypeFlag());	//是否能提供抵押担保
        result.setGuaType(qryResult.getGuaType());	//担保类别
        result.setRefuseReason(qryResult.getRefuseReason());	//拒绝原因
        return result;
    }
}
