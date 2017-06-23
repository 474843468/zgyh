package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.adapter.FreePassAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.QRPayFreePwdViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayFreePwdPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.FreePassListItemView.FreePassViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.FreePassListItemView.OnRefreshListener;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 二维码支付 - 小额免密
 * Created by wangf on 2016/8/23.
 */
public class QRPayFreePwdFragment extends BussFragment implements QRPayFreePwdContract.QrPaySecurityFactorView,
        QRPayFreePwdContract.QRPayFreePwdView, QRPayFreePwdContract.QRPayQueryPassFreeInfoView, SecurityVerity.VerifyCodeResultListener {

    private View mRootView;
    private ListView lvFreeCard;
    
    private View creditCardHeadView;
    private TextView tvCreditCardHead;

    private FreePassAdapter adapter;

    private QRPayFreePwdPresenter qrSecurityFactorPresenter;
    private QRPayFreePwdPresenter qrPayQueryPassFreeInfoPresenter;
    private QRPayFreePwdPresenter qrPayFreePwdPresenter;

    // 选择的安全因子
    private CombinListBean selectCombin;

    //账户信息
    private List<AccountBean> accountBeanList;
    
    private FreePassViewModel mClickItemModel;

    private boolean isDebitHaveAmount;//借记卡是否有限额数据
    private boolean isCreditHaveAmount;//信用卡是否有限额数据

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_qrpay_free_pwd, null);
        return mRootView;
    }


    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_qrpay_free_pwd);
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
    public void beforeInitView() {
        super.beforeInitView();
    }

    @Override
    public void initView() {
        lvFreeCard = (ListView) mRootView.findViewById(R.id.lv_free_card);
    }

    @Override
    public void initData() {
        qrSecurityFactorPresenter = new QRPayFreePwdPresenter((QRPayFreePwdContract.QrPaySecurityFactorView) this);
        qrPayQueryPassFreeInfoPresenter = new QRPayFreePwdPresenter((QRPayFreePwdContract.QRPayQueryPassFreeInfoView) this);
        qrPayFreePwdPresenter = new QRPayFreePwdPresenter((QRPayFreePwdContract.QRPayFreePwdView) this);

        isDebitHaveAmount = getArguments().getBoolean("isDebitHaveAmount");
        isCreditHaveAmount = getArguments().getBoolean("isCreditHaveAmount");

        accountBeanList = new ArrayList<>();
        //获取所有账户
        ArrayList<String> accountTypeList = new ArrayList<String>();
        if (isDebitHaveAmount){
            accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);//借记卡
            accountBeanList.addAll(QRPayMainFragment.getRelativeBankAccountList(accountTypeList));
        }
        if (isCreditHaveAmount) {
            accountTypeList.clear();
            accountTypeList.add(ApplicationConst.ACC_TYPE_ZHONGYIN);//中银系列信用卡
            accountBeanList.addAll(QRPayMainFragment.getRelativeBankAccountList(accountTypeList));
            accountTypeList.clear();
            accountTypeList.add(ApplicationConst.ACC_TYPE_GRE);//长城信用卡
            accountBeanList.addAll(QRPayMainFragment.getRelativeBankAccountList(accountTypeList));
        }
        //初始化RecycelView,并设置Adapter
        adapter = new FreePassAdapter(lvFreeCard, mContext);
        lvFreeCard.setAdapter(adapter);
        //设置账户数据
        adapter.setDatas(QrCodeModelUtil.generatePassFreeInfoListViewModels(accountBeanList));
        adapter.loadAmountInfo();


        showLoadingDialog();
        qrSecurityFactorPresenter.loadFreePwdSecurityFactor();
    }


    @Override
    public void setListener() {
        adapter.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(int position, FreePassViewModel item) {
                qrPayQueryPassFreeInfoPresenter.loadQRPayGetPassFreeInfo(item.getAccountBean());
            }

			@Override
			public void onClickCheckBox(int position, FreePassViewModel item, View childView) {
				
				mClickItemModel = item;
				adapter.updateItem(mClickItemModel);

                // 606
                if("1".equals(mClickItemModel.getFreeInfoViewModel().getPassFreeFlag())){
                    showLoadingDialog();
                    qrPayFreePwdPresenter.loadQRPayClosePassFreeService(mClickItemModel.getAccountBean().getAccountId());
                }else if("0".equals(mClickItemModel.getFreeInfoViewModel().getPassFreeFlag())){
                    //显示安全认证选择对话框
                    SecurityVerity.getInstance().selectSecurityType();
                }

                // X610
//				String accountType = mClickItemModel.getAccountBean().getAccountType();
//		        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
//		                ApplicationConst.ACC_TYPE_GRE.equals(accountType) ||
//		                ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(accountType)) {//信用卡
//		        	if("1".equals(mClickItemModel.getFreeInfoViewModel().getCreditCardFlag())){
//		        		showLoadingDialog();
//						qrPayFreePwdPresenter.loadQRPayClosePassFreeService(mClickItemModel.getAccountBean().getAccountId());
//					}else if("0".equals(mClickItemModel.getFreeInfoViewModel().getCreditCardFlag())){
//						//显示安全认证选择对话框
//		                SecurityVerity.getInstance().selectSecurityType();
//					}
//		        } else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)) {//借记卡
//		        	if("1".equals(mClickItemModel.getFreeInfoViewModel().getDebitCardFlag())){
//		        		showLoadingDialog();
//						qrPayFreePwdPresenter.loadQRPayClosePassFreeService(mClickItemModel.getAccountBean().getAccountId());
//					}else if("0".equals(mClickItemModel.getFreeInfoViewModel().getDebitCardFlag())){
//						//显示安全认证选择对话框
//		                SecurityVerity.getInstance().selectSecurityType();
//					}
//		        }

			}
        });

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SecurityVerity.getInstance(getActivity()).setSecurityVerifyListener(QRPayFreePwdFragment.this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * 封装开通小额免密服务 预交易 数据
     */
    private QRPayFreePwdViewModel buildOpenPassFreeServicePreViewModel() {
        QRPayFreePwdViewModel freePwdViewModel = new QRPayFreePwdViewModel();
        freePwdViewModel.set_combinId(selectCombin.getId());

        return freePwdViewModel;
    }


    /**
     * 封装开通小额免密服务 提交交易 数据
     */
    private QRPayFreePwdViewModel buildOpenPassFreeServiceViewModel(int factorId, String[] randomNums, String[] encryptPasswords) {
        QRPayFreePwdViewModel params = new QRPayFreePwdViewModel();
//        // 设置密码控件随机数
//        sipBoxPayPwd.setRandomKey_S(QRPayFreePwdPresenter.randomID);
//        sipBoxPayPwdAgain.setRandomKey_S(QRPayFreePwdPresenter.randomID);
//        params.setPassword(sipBoxPayPwd.getValue().getEncryptPassword());
//        params.setPassword_RC(sipBoxPayPwd.getValue().getEncryptRandomNum());
//        params.setPasswordConform(sipBoxPayPwdAgain.getValue().getEncryptPassword());
//        params.setPasswordConform_RC(sipBoxPayPwdAgain.getValue().getEncryptRandomNum());
        params.setActSeq(mClickItemModel.getAccountBean().getAccountId());
        params.setState(SecurityVerity.SECURITY_VERIFY_STATE);
        params.setActiv(SecurityVerity.getInstance().getCfcaVersion());

        switch (factorId) {
            case SecurityVerity.SECURITY_VERIFY_TOKEN:// 动态口令
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS:// 短信
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS_AND_TOKEN:// 动态口令+短信
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                params.setSmc(encryptPasswords[1]);
                params.setSmc_RC(randomNums[1]);
                break;
            case SecurityVerity.SECURITY_VERIFY_DEVICE:// 手机交易码+硬件绑定
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                DeviceInfoModel deviceInfoModel = DeviceInfoUtils.getDeviceInfo(mContext, QRPayFreePwdPresenter.randomID);
                params.setDeviceInfo(deviceInfoModel.getDeviceInfo());
                params.setDeviceInfo_RC(deviceInfoModel.getDeviceInfo_RC());
                break;
            case SecurityVerity.SECURITY_VERIFY_E_TOKEN:// 中银e盾
                params.set_signedData(randomNums[0]);
                break;
            default:
                break;
        }

        return params;
    }


    /*** 查询安全因子成功 */
    @Override
    public void loadFreePwdSecurityFactorSuccess(SecurityViewModel securityViewModel) {
        closeProgressDialog();
        // 传递安全因子给组件
        selectCombin = SecurityVerity.getInstance().
                getDefaultSecurityFactorId(new SecurityFactorModel(PublicUtils.copyOfSecurityCombin(securityViewModel)));

        //并发查询小额免密信息
        qrPayQueryPassFreeInfoPresenter.loadAllPassFreeInfo(accountBeanList);
    }

    /***
     * 查询安全因子失败
     */
    @Override
    public void loadFreePwdSecurityFactorFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /***
     * 开通小额免密服务预交易 成功
     */
    @Override
    public void loadQRPayOpenPassFreeServicePreSuccess(QRPayFreePwdViewModel freePwdViewModel) {
        closeProgressDialog();
        // 显示安全认证对话框
        EShieldVerify.getInstance(getActivity()).setmPlainData(freePwdViewModel.get_plainData());
        if (SecurityVerity.getInstance().confirmFactor(QrCodeModelUtil.copyToFactorListBean(freePwdViewModel.getFactorList()))) {
            SecurityVerity.getInstance().setConversationId(QRPayFreePwdPresenter.conversationID);
            SecurityVerity.getInstance().showSecurityDialog(QRPayFreePwdPresenter.randomID);
        }
    }

    /***
     * 开通小额免密服务预交易 失败
     */
    @Override
    public void loadQRPayOpenPassFreeServicePreFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /***
     * 开通小额免密提交交易 成功
     */
    @Override
    public void loadQRPayOpenPassFreeServiceSuccess() {
        closeProgressDialog();

        // 606
        mClickItemModel.getFreeInfoViewModel().setPassFreeFlag("1");
        adapter.updateItem(mClickItemModel);

        // X610
//        String accountType = mClickItemModel.getAccountBean().getAccountType();
//        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
//                ApplicationConst.ACC_TYPE_GRE.equals(accountType) ||
//                ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(accountType)) {//信用卡
//        	mClickItemModel.getFreeInfoViewModel().setCreditCardFlag("1");
//            adapter.updateItem(mClickItemModel);
//        } else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)) {//借记卡
//        	mClickItemModel.getFreeInfoViewModel().setDebitCardFlag("1");
//        	adapter.updateItem(mClickItemModel);
//        }
    }

    /***
     * 开通小额免密提交交易 失败
     */
    @Override
    public void loadQRPayOpenPassFreeServiceFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();

        // 606
        mClickItemModel.getFreeInfoViewModel().setPassFreeFlag("0");
        adapter.updateItem(mClickItemModel);

        // X610
//        String accountType = mClickItemModel.getAccountBean().getAccountType();
//        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
//                ApplicationConst.ACC_TYPE_GRE.equals(accountType) ||
//                ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(accountType)) {//信用卡
//        	mClickItemModel.getFreeInfoViewModel().setCreditCardFlag("0");
//            adapter.updateItem(mClickItemModel);
//        } else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)) {//借记卡
//        	mClickItemModel.getFreeInfoViewModel().setDebitCardFlag("0");
//        	adapter.updateItem(mClickItemModel);
//        }
    }

    /***
     * 关闭小额免密服务 成功
     */
    @Override
    public void loadQRPayClosePassFreeServiceSuccess() {
        closeProgressDialog();

        // 606
        mClickItemModel.getFreeInfoViewModel().setPassFreeFlag("0");
        adapter.updateItem(mClickItemModel);

        // X610
//        String accountType = mClickItemModel.getAccountBean().getAccountType();
//        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
//                ApplicationConst.ACC_TYPE_GRE.equals(accountType) ||
//                ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(accountType)) {//信用卡
//        	mClickItemModel.getFreeInfoViewModel().setCreditCardFlag("0");
//            adapter.updateItem(mClickItemModel);
//        } else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)) {//借记卡
//        	mClickItemModel.getFreeInfoViewModel().setDebitCardFlag("0");
//        	adapter.updateItem(mClickItemModel);
//        }
    }

    /***
     * 关闭小额免密服务 失败
     */
    @Override
    public void loadQRPayClosePassFreeServiceFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();

        // 606
        mClickItemModel.getFreeInfoViewModel().setPassFreeFlag("1");
        adapter.updateItem(mClickItemModel);

        // X610
//        String accountType = mClickItemModel.getAccountBean().getAccountType();
//        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
//                ApplicationConst.ACC_TYPE_GRE.equals(accountType) ||
//                ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(accountType)) {//信用卡
//        	mClickItemModel.getFreeInfoViewModel().setCreditCardFlag("1");
//            adapter.updateItem(mClickItemModel);
//        } else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)) {//借记卡
//        	mClickItemModel.getFreeInfoViewModel().setDebitCardFlag("1");
//        	adapter.updateItem(mClickItemModel);
//        }
    }


    /***
     * 查询小额免密信息成功
     */
    @Override
    public void loadQRPayGetPassFreeInfoSuccess(FreePassViewModel passViewModel) {
        if (passViewModel != null) {
            adapter.updateItem(passViewModel);
        }
    }

    /***
     * 查询小额免密信息失败
     */
    @Override
    public void loadQRPayGetPassFreeInfoFail(BiiResultErrorException biiResultErrorException) {

    }


    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {

    	selectCombin = bean;
    	
    	showLoadingDialog();
		qrPayFreePwdPresenter.loadQRPayOpenPassFreeServicePre(buildOpenPassFreeServicePreViewModel());
    	
//		String accountType = mClickItemModel.getAccountBean().getAccountType();
//        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
//                ApplicationConst.ACC_TYPE_GRE.equals(accountType) ||
//                ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(accountType)) {//信用卡
//        	if("1".equals(mClickItemModel.getFreeInfoViewModel().getCreditCardFlag())){
//        		showLoadingDialog();
//				qrPayFreePwdPresenter.loadQRPayClosePassFreeService(mClickItemModel.getAccountBean().getAccountId());
//			}else if("0".equals(mClickItemModel.getFreeInfoViewModel().getCreditCardFlag())){
//				showLoadingDialog();
//				qrPayFreePwdPresenter.loadQRPayOpenPassFreeServicePre(buildOpenPassFreeServicePreViewModel());
//			}
//        } else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)) {//借记卡
//        	if("1".equals(mClickItemModel.getFreeInfoViewModel().getDebitCardFlag())){
//        		showLoadingDialog();
//				qrPayFreePwdPresenter.loadQRPayClosePassFreeService(mClickItemModel.getAccountBean().getAccountId());
//			}else if("0".equals(mClickItemModel.getFreeInfoViewModel().getDebitCardFlag())){
//				showLoadingDialog();
//				qrPayFreePwdPresenter.loadQRPayOpenPassFreeServicePre(buildOpenPassFreeServicePreViewModel());
//			}
//        }
        
    	
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        showLoadingDialog();
        //开通小额免密服务提交交易
        qrPayFreePwdPresenter.loadQRPayOpenPassFreeService(buildOpenPassFreeServiceViewModel(Integer.valueOf(factorId), randomNums, encryptPasswords));
    }

    @Override
    public void onSignedReturn(String signRetData) {
        showLoadingDialog();
        String[] randomNums = {signRetData};
        String[] encryptPasswords = {};
        //开通小额免密服务提交交易
        qrPayFreePwdPresenter.loadQRPayOpenPassFreeService(buildOpenPassFreeServiceViewModel(SecurityVerity.SECURITY_VERIFY_E_TOKEN, randomNums, encryptPasswords));
    }
}
