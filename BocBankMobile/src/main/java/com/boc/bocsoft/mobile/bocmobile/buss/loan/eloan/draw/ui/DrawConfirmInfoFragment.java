package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model.LOANCycleLoanEApplySubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model.LOANCycleLoanEApplyVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model.LOANCycleLoanEApplyVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.presenter.DrawPresenter;
import com.boc.bocsoft.mobile.common.utils.ButtonClickLock;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.LinkedHashMap;

/**
 * 贷款管理-中银E贷-用款确认信息页面fragment
 * Created by xintong on 2016/6/7.
 */
public class DrawConfirmInfoFragment extends MvpBussFragment<DrawPresenter> implements DrawContract.DrawConfirmView {
    private static final String TAG = "DrawConfirmInfoFragment";
    //页面名称
    private static String PAGENAME;
    //贷款用途
    protected TextView hint;
    //结果页
    private DrawOperatingResultsFragment resultsFragment;
  //预交易后，再次确认安全因子  为true调用预交易接口
    private boolean available;
    
    private LinearLayout content;
  //安全认证组件
    private DetailTableRowButton bocBtntv;
    //头布局组件
    private DetailTableHead detailTableHead;
  //详情内容控件
    private DetailContentView detailContentView;
    //底部确认按钮
    Button confirm;
    private SecurityVerity securityVerity;
    //页面根视图
    private View mRoot;
//    private DrawPresenter drawPresenter;
    //会话id
    private String conversationId;
  //获取安全因子成功后返回的默认安全因子组合id
    private String _combinId,combinId_name;
    private LOANCycleLoanEApplyVerifyReq loanCycleLoanEApplyVerifyReq;
    private LOANCycleLoanEApplySubmitReq req;
    //随机数
    private String random;
    /** 提交按钮 防暴力点击 */
    private String click_more = "click_more";
    //确认界面公共组件
    private ConfirmInfoView confirmInfoView;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_eloan_draw_confirminfo, null);
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
        return getString(R.string.boc_eloan_drawconfirmation_info);
    }

//    public void setLoanCycleLoanEApplyVerifyReq(LOANCycleLoanEApplyVerifyReq req) {
//        this.loanCycleLoanEApplyVerifyReq = req;
//    }

    @Override
    public void beforeInitView() {

    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public void set_combinId_Name(String combinId_name) {
        this.combinId_name = combinId_name;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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
    	
//        drawPresenter = new DrawPresenter(this);
        securityVerity = SecurityVerity.getInstance(getActivity());
        //初始化组件
        content = (LinearLayout) mRoot.findViewById(R.id.content);

//        detailTableHead = new DetailTableHead(mContext);
//        detailContentView = new DetailContentView(mContext);
//        bocBtntv = new DetailTableRowButton(mContext);
//        confirm = (Button) mRoot.findViewById(R.id.confirm);

        confirmInfoView=new ConfirmInfoView(mContext);

        hint = (TextView) mRoot.findViewById(R.id.hint);
        resultsFragment = new DrawOperatingResultsFragment();
    }

    @Override
    public void initData() {
    	
    	loanCycleLoanEApplyVerifyReq=(LOANCycleLoanEApplyVerifyReq) getArguments().getSerializable("LOANCycleLoanEApplyVerifyReq");

        getPresenter().setConversationId(conversationId);
        securityVerity.setConversationId(conversationId);
        
        ButtonClickLock.getLock(click_more).lockDuration = EloanConst.CLICK_MORE_TIME;
        LinkedHashMap<String,String> map=new LinkedHashMap<>();

        if(null!=loanCycleLoanEApplyVerifyReq){
        	//为组建添加默认数据
            confirmInfoView.setHeadValue("用款金额（人民币元）",
                    MoneyUtils.transMoneyFormat(loanCycleLoanEApplyVerifyReq.getAmount().toString(), "021"),false);
            //确认页显示数据
          String mMonth=  strToInt(loanCycleLoanEApplyVerifyReq.getLoanPeriod())+"";
            map.put("期限/利率" ,  mMonth+"个月/"+loanCycleLoanEApplyVerifyReq.getLoanRate()+"%");
//            if("0".equalsIgnoreCase(loanCycleLoanEApplyVerifyReq.getLoanPeriod().substring(0))){
//                map.put("期限/利率" ,
//                        mMonth+"个月/"+
//                                loanCycleLoanEApplyVerifyReq.getLoanRate()+"%");
//            }else{
//                map.put("期限/利率" ,
//                        loanCycleLoanEApplyVerifyReq.getLoanPeriod()+"个月/"+
//                                loanCycleLoanEApplyVerifyReq.getLoanRate()+"%");
//            }
            if("1".equals(loanCycleLoanEApplyVerifyReq.getPayType())){
                map.put("还款方式" , getString(R.string.boc_details_repaytypeTwo));
            }else if("2".equals(loanCycleLoanEApplyVerifyReq.getPayType())){
                map.put("还款方式" , getString(R.string.boc_details_repaytypeOne));
            }else{
                map.put("还款方式" , "");
            }
            map.put("收款账户" , NumberUtils.formatCardNumberStrong(loanCycleLoanEApplyVerifyReq.getLoanCycleToActNum()));
            map.put("还款账户" , NumberUtils.formatCardNumberStrong(loanCycleLoanEApplyVerifyReq.getPayAccount()));
            map.put("资金用途" , loanCycleLoanEApplyVerifyReq.getRemark());

            confirmInfoView.addData(map,true);
            // 设置默认的安全因子名称
            confirmInfoView.updateSecurity("安全认证",combinId_name);
            confirmInfoView.setBottomHint("贷款用途仅可用于个人合法合理的消费支出。" +
                    "借款人不得将贷款用于购房、投资经营和无指定用途的个人支出。" +
                    "不得用于任何法律法规、监管规定、国家政策禁止银行贷款投入的" +
                    "项目、用途、包括股票、证券投资等。");
            content.addView(confirmInfoView);
        }
    }

    @Override
    public void setListener() {

        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
                if (!ButtonClickLock.isCanClick(click_more)) {
                    return;
                }
                if (available) {
                    showLoadingDialog(false);
                    //获取随机数
                    getPresenter().getRandom();
                }
            }

            @Override
            public void onClickChange() {//显示安全认证选择对话框
                if (!ButtonClickLock.isCanClick(click_more)) {
                    return;
                }
                securityVerity.selectSecurityType();
            }
        });

        //安全组件点击监听
        securityVerity.setSecurityVerifyListener(new SecurityVerity.VerifyCodeResultListener() {
            @Override
            public void onSecurityTypeSelected(CombinListBean bean) {

            	if(!StringUtils.isEmpty(_combinId)){
            		if(!_combinId.equalsIgnoreCase(bean.getId())){

            			 _combinId = bean.getId();
                        combinId_name=bean.getName();
                        // 设置更改的安全因子名称
                        confirmInfoView.updateSecurity(combinId_name);

       	              loanCycleLoanEApplyVerifyReq.set_combinId(_combinId);
                        getPresenter().setLoanCycleLoanEApplyVerifyReq(loanCycleLoanEApplyVerifyReq);
    	              showLoadingDialog(false);
    	              //点击下一步按钮，跳转到提款确认页
                        getPresenter().drawApplyVerify();
            		}
            	}
            }

            @Override
            public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
                showLoadingDialog(false);
                //提交接口调用
                buildSubmitReq(factorId, randomNums, encryptPasswords);
            }

            @Override
            public void onSignedReturn(String signRetData) {
                try{
                    showLoadingDialog(false);
                    buildSubmitReq(signRetData);
                }catch (Exception e){
                    e.printStackTrace();
                    closeProgressDialog();
                }


            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().unsubscribe();
    }


    @Override
    public void drawApplyVerifySuccess(LOANCycleLoanEApplyVerifyRes result) {
        Log.i(TAG, "提款预交易成功！");
        closeProgressDialog();
        available = securityVerity.confirmFactor(result.getFactorList());
        
      //音频key加密
        EShieldVerify.getInstance(getActivity()).setmPlainData(result.get_plainData());
        
//        if (available) {
//            //获取随机数
//            drawPresenter.getRandom();
//          //音频key加密
//            EShieldVerify.getInstance(getActivity()).setmPlainData(result.get_plainData());
////            showLoadingDialog();
//        } else {
//            //TODO 获取确定的安全因子失败
//        }

    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public void drawApplyVerifyFail(ErrorException e) {
        Log.i(TAG, "提款预交易失败！");
        closeProgressDialog();
    }

    @Override
    public void obtainRandomSuccess(String random) {
        this.random=random;
        Log.i(TAG, "获取随机数成功！");
        closeProgressDialog();
        //弹出选择的安全认证工具
        securityVerity.showSecurityDialog(random);
//        securityVerity.showSecurityDialog("232");


    }

    @Override
    public void obtainRandomFail(ErrorException e) {
        Log.i(TAG, "获取随机数失败！");
        closeProgressDialog();
    }

    @Override
    public void drawApplySubmitSuccess() {
        Log.i(TAG, "提款交易成功！");
        closeProgressDialog();
        resultsFragment.setStatus(OperationResultHead.Status.SUCCESS);
        Bundle bundle=new Bundle();
        bundle.putSerializable(EloanConst.ELON_DRAW_COMMIT,req);
        resultsFragment.setArguments(bundle);
        start(resultsFragment);
    }

    @Override
    public void drawApplySubmitFail(ErrorException e) {
        Log.i(TAG, "提款交易失败！");
        closeProgressDialog();
//        resultsFragment.setStatus(OperationResultHead.Status.FAIL);
//        start(resultsFragment);
    }

    @Override
    public void setPresenter(DrawContract.Presenter presenter) {

    }

    private void buildSubmitReq(String factorId, String[] randomNums, String[] encryptPasswords){
//        LOANCycleLoanEApplySubmitReq req = new LOANCycleLoanEApplySubmitReq();
        req = new LOANCycleLoanEApplySubmitReq();
        LogUtils.i(TAG, "---------------randomNums----------》"+randomNums.length);
        LogUtils.i(TAG, "---------------encryptPasswords----------》"+encryptPasswords.length);
        
        if ("8".equals(factorId)) {
//        	if(check(factorId,encryptPasswords)){
        		//动态口令
                req.setOtp(encryptPasswords[0]);
                req.setOtp_RC(randomNums[0]);
                req.setFactorId(factorId);
                buildSubmit();
//        	}
        } else if ("32".equals(factorId)) {
//        	if(check(factorId,encryptPasswords)){
        		 //短信验证码
                req.setSmc(encryptPasswords[0]);
                req.setSmc_RC(randomNums[0]);
                req.setFactorId(factorId);
                buildSubmit();
//        	}
        } else if ("40".equals(factorId)) {
//        	if(check(factorId,encryptPasswords)){
        		  //动态口令+短信验证码
                req.setOtp(encryptPasswords[0]);
                req.setOtp_RC(randomNums[0]);
                req.setSmc(encryptPasswords[1]);
                req.setSmc_RC(randomNums[1]);
                req.setFactorId(factorId);
                LogUtils.i("cq---------->动态口令+短信验证码-------"
                        +encryptPasswords[0]+"----------"
                        		+randomNums[0]+"----------"
                        +encryptPasswords[1]+"----------"
                        		+randomNums[1]);
                buildSubmit();
//        	}
        } else if ("96".equals(factorId)) {
//        	if(check(factorId,encryptPasswords)){
        		//短信验证码
                req.setSmc(encryptPasswords[0]);
                req.setSmc_RC(randomNums[0]);
                req.setFactorId(factorId);
//                DeviceInfoModel infoModel = DeviceInfoUtils.getDeviceInfo(getActivity()
//                        ,random, SpUtils.getSpString(getContext(),EloanConst.OPETATOR_ID,null));
//
//                req.setDeviceInfo(infoModel.getDeviceInfo());
//                req.setDeviceInfo_RC(infoModel.getDeviceInfo_RC());
//                req.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
            DeviceInfoModel info = DeviceInfoUtils.getDeviceInfo(getActivity(), random);
            req.setDeviceInfo(info.getDeviceInfo());
            req.setDeviceInfo_RC(info.getDeviceInfo_RC());
            req.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));

            buildSubmit();
//        	}
        }else if ("4".equals(factorId)) {
            //TODO 音频key
//            req.set_signedData(req.get_signedData());
        }     
    }

    private void buildSubmitReq(String signRetData){
    	//TODO 音频key
        req = new LOANCycleLoanEApplySubmitReq();
        req.set_signedData(signRetData);
        req.setFactorId("4");
        buildSubmit();
    }

   private void buildSubmit(){
	   //cfn版本号和状态
       req.setActiv(SecurityVerity.getInstance(getActivity()).getCfcaVersion());
       req.setState(SecurityVerity.SECURITY_VERIFY_STATE);

       req.setConversationId(conversationId);
       req.setQuoteType(loanCycleLoanEApplyVerifyReq.getQuoteType());
       req.setLoanType(loanCycleLoanEApplyVerifyReq.getLoanType());
       req.setQuoteNo(loanCycleLoanEApplyVerifyReq.getQuoteNo());
       req.setLoanCycleAvaAmount(loanCycleLoanEApplyVerifyReq.getLoanCycleAvaAmount());
       req.setCurrencyCode(loanCycleLoanEApplyVerifyReq.getCurrencyCode());
       req.setAmount(loanCycleLoanEApplyVerifyReq.getAmount());
       req.setRemark(loanCycleLoanEApplyVerifyReq.getRemark());
       req.setLoanCycleToActNum(loanCycleLoanEApplyVerifyReq.getLoanCycleToActNum());
       req.setToAccountId(loanCycleLoanEApplyVerifyReq.getToAccountId());
       req.setLoanPeriod(loanCycleLoanEApplyVerifyReq.getLoanPeriod());
       req.setPayType(loanCycleLoanEApplyVerifyReq.getPayType());
       req.setLoanRate(loanCycleLoanEApplyVerifyReq.getLoanRate());
       req.setIssueRepayDate(loanCycleLoanEApplyVerifyReq.getIssueRepayDate());
       req.setPayAccount(loanCycleLoanEApplyVerifyReq.getPayAccount());
       req.setNextRepayDate(loanCycleLoanEApplyVerifyReq.getNextRepayDate());
       getPresenter().setLoanCycleLoanEApplySubmitReq(req);
       getPresenter().drawApplySubmit();
   }

    @Override
    protected DrawPresenter initPresenter() {
        return new DrawPresenter(this);
    }
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
}
