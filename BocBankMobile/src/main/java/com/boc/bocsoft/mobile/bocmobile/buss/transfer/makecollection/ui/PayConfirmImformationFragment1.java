package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.CombinBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.adapter.PayerAccountAdapter2;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnBatchTransActCollectionSubmitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnBatchTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnTransActCollectionSubmitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.SavePayerViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter.PayConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Fragment：支付确认信息界面（我要收款）
 * Created by zhx on 2016/7/12
 * serviceId:转账汇款->主动收款->主动收款预交易	PB037
 */
public class PayConfirmImformationFragment1 extends BussFragment implements SecurityVerity.VerifyCodeResultListener, PayConfirmContact.View {
    // 会话ID
    public static String conversationId;

    /**
     * 当前选择的安全因子
     */
    private CombinBean curSelectedCombin;
    private PsnTransActCollectionVerifyViewModel psnTransActCollectionVerifyViewModel;
    private PsnTransActCollectionSubmitViewModel psnTransActCollectionSubmitViewModel;
    private PsnBatchTransActCollectionVerifyViewModel psnBatchTransActCollectionVerifyViewModel;

    private View mRootView;
    // 安全信息
    private DetailTableRowButton securityInfo;
    private PayConfirmPresenter payConfirmPresenter;
    private String random;
    private int type;
    private TextView tv_payer;
    private TextView tv_average_amount;
    private TextView tv_payee_actno;
    private TextView tv_remark;
    private ListView lv_payer_list;
    private LinearLayout ll_payer_container;
    private TextView tv_payer_mobile;
    private TextView tv_payee_mobile;
    private TextView tv_payer_name;
    private TextView tv_payer_channel;

    private ConfirmInfoView confirmInfoView;

    // 默认安全因子
    private String defaultCombinName;
    private String defaultCombinID;
    private List<FactorListBean> defaultFactorList;
    private List<FactorListBean> currentFactorList;
    private LinkedHashMap<String, String> datas = new LinkedHashMap<>();
    private boolean isSavePayer;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        confirmInfoView = new ConfirmInfoView(mContext);
        return confirmInfoView;
    }



    @Override
    public void initView() {


        //        securityInfo = (DetailTableRowButton) mRootView.findViewById(R.id.security_info);
        //        tv_payer = (TextView) mRootView.findViewById(R.id.tv_payer);
        //        tv_payer_mobile = (TextView) mRootView.findViewById(R.id.tv_payer_mobile);
        //        tv_payer_name = (TextView) mRootView.findViewById(R.id.tv_payer_name);
        //        tv_payer_channel = (TextView) mRootView.findViewById(R.id.tv_payer_channel);
        //        tv_payee_mobile = (TextView) mRootView.findViewById(R.id.tv_payee_mobile);
        //        tv_average_amount = (TextView) mRootView.findViewById(R.id.tv_average_amount);
        //        tv_payee_actno = (TextView) mRootView.findViewById(R.id.tv_payee_actno);
        //        tv_remark = (TextView) mRootView.findViewById(R.id.tv_remark);
        //        lv_payer_list = (ListView) mRootView.findViewById(R.id.lv_payer_list);
        //        ll_payer_container = (LinearLayout) mRootView.findViewById(R.id.ll_payer_container);
        //        btn_submit = (Button) mRootView.findViewById(R.id.btn_submit);


    }

    @Override
    public void initData() {
        payConfirmPresenter = new PayConfirmPresenter(this);
        type = getArguments().getInt("type", 0);
        defaultCombinID = getArguments().getString("DefaultCombinID");
        defaultCombinName = getArguments().getString("DefaultCombinName");
        isSavePayer = getArguments().getBoolean("isSavePayer");
        if (type == 0) { // 主动收款（单人）
            psnTransActCollectionVerifyViewModel = (PsnTransActCollectionVerifyViewModel) getArguments().getSerializable("psnTransActCollectionVerifyViewModel");
            currentFactorList = defaultFactorList = copyToFactorListBean();
//            payConfirmPresenter.collectionVerify(psnTransActCollectionVerifyViewModel);

            // 设置页面的数据
            //            tv_average_amount.setText(psnTransActCollectionVerifyViewModel.getNotifyPayeeAmount());
            //            tv_payee_actno.setText(psnTransActCollectionVerifyViewModel.getPayeeActno());
            //            tv_remark.setText(psnTransActCollectionVerifyViewModel.getRemark());
            //            tv_payer.setText(psnTransActCollectionVerifyViewModel.getPayerName());
            //            tv_payer_mobile.setText(psnTransActCollectionVerifyViewModel.getPayerMobile());
            //            tv_payee_mobile.setText(psnTransActCollectionVerifyViewModel.getPayeeMobile());
            //            tv_payer_name.setText(psnTransActCollectionVerifyViewModel.getPayerName());
            //            tv_payer_channel.setText("1".equals(psnTransActCollectionVerifyViewModel.getPayerChannel()) ? "网上银行用户" : "手机银行用户");
            //            lv_payer_list.setVisibility(View.GONE);

            //            // TODO 此处下面写法是否正确，有待确认
            //            securityInfo.addTextBtn("安全认证", psnTransActCollectionVerifyViewModel.get_combinName(), "更改", getResources().getColor(R.color.boc_main_button_color));

            confirmInfoView.setHeadValue("收款金额（人民币元）", psnTransActCollectionVerifyViewModel.getNotifyPayeeAmount());
            for (int i = 0; i < collectionName().length; i++) {
                if (!TextUtils.isEmpty(collectionValue()[i].trim())) {
                    datas.put(collectionName()[i], collectionValue()[i]);
                }
            }
            confirmInfoView.addData(datas);
            // 设置默认的安全因子名称
            confirmInfoView.updateSecurity(defaultCombinName);

        } else if (type == 1) { // 主动收款（多人）
            psnBatchTransActCollectionVerifyViewModel = (PsnBatchTransActCollectionVerifyViewModel) getArguments().getSerializable("psnBatchTransActCollectionVerifyViewModel");

            ////////////////////////////和结果页面基本一致(开始)///////////////////////////
            tv_average_amount.setText(psnBatchTransActCollectionVerifyViewModel.getNotifyPayeeAmount()); // 人均额
            tv_payee_actno.setText(NumberUtils.formatCardNumberStrong(psnBatchTransActCollectionVerifyViewModel.getPayeeActno())); // 收款帐号
            tv_remark.setText(psnBatchTransActCollectionVerifyViewModel.getRemark()); // 附言

            List<PsnBatchTransActCollectionVerifyViewModel.PayerEntity> payerList = psnBatchTransActCollectionVerifyViewModel.getPayerList();
            lv_payer_list.setAdapter(new PayerAccountAdapter2(mActivity, payerList));

            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) mContext;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            float density = metrics.density;

            int itemHeight = (int) activity.getResources().getDimension(R.dimen.boc_space_between_62px);
            LinearLayout.LayoutParams latyoutParams = (LinearLayout.LayoutParams) lv_payer_list.getLayoutParams();
            latyoutParams.height = itemHeight * payerList.size();
            lv_payer_list.setLayoutParams(latyoutParams);
            tv_payer.setVisibility(View.GONE);

            ////////////////////////////和结果页面基本一致(结束)///////////////////////////

            payConfirmPresenter.psnBatchTransActCollectionVerify(psnBatchTransActCollectionVerifyViewModel);
            // TODO 此处下面写法是否正确，有待确认
            securityInfo.addTextBtn("安全工具", psnBatchTransActCollectionVerifyViewModel.get_combinName(), "更改", getResources().getColor(R.color.boc_main_button_color));
        }

        SecurityVerity.getInstance(mActivity).setSecurityVerifyListener(this);
    }

    private List<FactorListBean> copyToFactorListBean() {
        List<FactorListBean> factorList = new ArrayList<FactorListBean>();
        for (int i = 0; i < psnTransActCollectionVerifyViewModel.getFactorList().size(); i++) {
            FactorListBean factorListBean = new FactorListBean();
            FactorListBean.FieldBean fieldBean = new FactorListBean.FieldBean();
            fieldBean.setName(psnTransActCollectionVerifyViewModel.getFactorList().get(i).getField().getName());
            fieldBean.setType(psnTransActCollectionVerifyViewModel.getFactorList().get(i).getField().getType());
            factorListBean.setField(fieldBean);
            factorList.add(factorListBean);
        }
        return factorList;
    }

    /**
     * 名称
     *
     * @return
     */
    private String[] collectionName() {
        String[] name = new String[5];
        name[0] = "付款人名称";
        name[1] = "付款人手机号";
        name[2] = "附言";
        name[3] = "收款账户";
        name[4] = "收款人手机号";
        return name;
    }

    /**
     * 数据
     *
     * @return
     */
    private String[] collectionValue() {
        String[] value = new String[5];
        value[0] = psnTransActCollectionVerifyViewModel.getPayerName();
        value[1] = NumberUtils.formatMobileNumber(psnTransActCollectionVerifyViewModel.getPayerMobile());
        value[2] = psnTransActCollectionVerifyViewModel.getRemark();
        value[3] = NumberUtils.formatCardNumber(psnTransActCollectionVerifyViewModel.getPayeeActno());
        value[4] = NumberUtils.formatMobileNumber(psnTransActCollectionVerifyViewModel.getPayeeMobile());
        return value;
    }

    @Override
    public void setListener() {
        //        securityInfo.setOnclick(new DetailTableRowButton.BtnCallback() {
        //            @Override
        //            public void onClickListener() {
        //                //获取安全因子
        //                SecurityVerity.getInstance().getDefaultSecurityFactorId(new SecurityFactorModel(payConfirmPresenter.getPsnGetSecurityFactorResult()));
        //                //显示安全认证选择对话框
        //                SecurityVerity.getInstance().selectSecurityType();
        //            }
        //        });
        //
        //        btn_submit.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //            }
        //        });

        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
                payConfirmPresenter.setCurrent_combinId(SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId());
                payConfirmPresenter.collectionVerify(psnTransActCollectionVerifyViewModel);
            }

            @Override
            public void onClickChange() { // 显示安全认证选择对话框
                SecurityVerity.getInstance().selectSecurityType();
            }
        });

    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "确认收款信息";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        // TODO

        CombinBean combinBean = new CombinBean();
        combinBean.setId(bean.getId());
        combinBean.setName(bean.getName());
        combinBean.setSafetyFactorList(bean.getSafetyFactorList());
        curSelectedCombin = combinBean;
        //        if (String.valueOf(SecurityVerity.SECURITY_VERIFY_DEVICE).equals(bean.getId())) {
        //            securityInfo.setTxtValue(getResources().getString(R.string.security_sms_name));
        //        } else {
        //            securityInfo.setTxtValue(bean.getName());
        //        }
        Log.e("ljljlj", "bean = " + bean);

        currentFactorList = copyToFactorListBean();

        // 设置更改的安全因子名称
        confirmInfoView.updateSecurity(bean.getName());
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        Log.e("ljljlj", "factorId = " + factorId);
        Log.e("ljljlj", "randomNums = " + Arrays.toString(randomNums));
        Log.e("ljljlj", "encryptPasswords = " + Arrays.toString(encryptPasswords));

        if (type == 0) { // 主动收款（单人）
            this.psnTransActCollectionSubmitViewModel = generateSubmitViewModel();
            if (curSelectedCombin != null) {
                payConfirmPresenter.collectionSubmit(this.psnTransActCollectionSubmitViewModel, randomNums, encryptPasswords, Integer.valueOf(curSelectedCombin.getId()), mActivity);
            } else {
                payConfirmPresenter.collectionSubmit(this.psnTransActCollectionSubmitViewModel, randomNums, encryptPasswords, Integer.valueOf(defaultCombinID), mActivity);
            }
        } else { // 主动收款（多人）
            PsnBatchTransActCollectionSubmitViewModel psnBatchTransActCollectionSubmitViewModel = generateBatchSubmitViewModel();
            payConfirmPresenter.psnBatchTransActCollectionSubmit(psnBatchTransActCollectionSubmitViewModel, randomNums, encryptPasswords, Integer.valueOf(curSelectedCombin.getId()), mActivity);
        }

    }

    @Override
    public void onSignedReturn(String signRetData) { // 用到中银E盾时才会调用这个方法
        String[] randomNums = {signRetData};
        String[] encryptPasswords = {};
        this.psnTransActCollectionSubmitViewModel = generateSubmitViewModel();
        if (curSelectedCombin != null) {
            payConfirmPresenter.collectionSubmit(this.psnTransActCollectionSubmitViewModel, randomNums, encryptPasswords, Integer.valueOf(curSelectedCombin.getId()), mActivity);
        } else {
            payConfirmPresenter.collectionSubmit(this.psnTransActCollectionSubmitViewModel, randomNums, encryptPasswords, Integer.valueOf(defaultCombinID), mActivity);
        }
    }

    // 生成主动收款提交的ViewModel（单人）
    private PsnTransActCollectionSubmitViewModel generateSubmitViewModel() {
        PsnTransActCollectionSubmitViewModel viewModel = new PsnTransActCollectionSubmitViewModel();

        // 所有的参数是对照ios一个个弄好的，conversationId和token是后面请求到的

        BeanConvertor.toBean(psnTransActCollectionVerifyViewModel, viewModel);

        return viewModel;
    }

    // 生成主动收款提交的ViewModel（多人）
    private PsnBatchTransActCollectionSubmitViewModel generateBatchSubmitViewModel() {
        PsnBatchTransActCollectionSubmitViewModel viewModel = new PsnBatchTransActCollectionSubmitViewModel();

        // 所有的参数是对照ios一个个弄好的，conversationId和token是后面请求到的

        BeanConvertor.toBean(psnBatchTransActCollectionVerifyViewModel, viewModel);

        return viewModel;
    }

    /**
     * // TODO 这个方法暂时保留，不会用到
     * 当安全因子返回时调用
     * securityFactorReturned() --- factor = 8
     */
    public void securityFactorReturned() {
        //        String factor = SecurityVerity.getInstance(getActivity()).getDefaultSecurityFactorId(model.getFactorModel()).getId();
        //        Log.e("ljljlj", "securityFactorReturned() --- factor = " + factor);
        //        securityInfo.addTextBtn("安全认证", factor, "更改", getResources().getColor(R.color.boc_main_button_color));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void collectionVerifySuccess(PsnTransActCollectionVerifyViewModel viewModel) {
        this.psnTransActCollectionVerifyViewModel = viewModel;
        // 获取随机数
        payConfirmPresenter.getRandom();

//        //
//        CombinBean combinBean = new CombinBean();
//        CombinListBean combinListBean = payConfirmPresenter.combinListBean;
//        combinBean.setId(combinListBean.getId());
//        combinBean.setName(combinListBean.getName());
//        combinBean.setSafetyFactorList(combinListBean.getSafetyFactorList());
//        curSelectedCombin = combinBean;

        EShieldVerify.getInstance(getActivity()).setmPlainData(psnTransActCollectionVerifyViewModel.get_plainData());
        // （此处参照刘卫东的写法）
        // 显示安全认证对话框
        if (SecurityVerity.getInstance().confirmFactor(viewModel.getFactorList())) {
            SecurityVerity.getInstance().setConversationId(payConfirmPresenter.conversationId);
        }

    }

    @Override
    public void collectionVerifyFailed(BiiResultErrorException biiResultErrorException) {
        Toast.makeText(mActivity, "主动收款预交易失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void collectionSubmitSuccess(PsnTransActCollectionSubmitViewModel viewModel) {
        // 保存上次操作账户
        BocCloudCenter.getInstance().updateLastAccountId(AccountType.ACC_TYPE_PAYEE, psnTransActCollectionVerifyViewModel.getToAccountId());

        if (isSavePayer) { // 如果用户勾选，那么执行保存的逻辑
            String payerCustId = viewModel.getPayerCustId();
            if (payerCustId == null) {
                SavePayerViewModel savePayerViewModel = new SavePayerViewModel();
                savePayerViewModel.setPayerMobile(psnTransActCollectionVerifyViewModel.getPayerMobile());
                savePayerViewModel.setPayerName(psnTransActCollectionVerifyViewModel.getPayerName());

                payConfirmPresenter.savePayer(savePayerViewModel);
            } else {
                goToOperateResultFragment1(0);
            }
        } else {
            goToOperateResultFragment1(0);
        }
    }

    @Override
    public void collectionSubmitFailed(BiiResultErrorException biiResultErrorException) {
    }

    @Override
    public void psnBatchTransActCollectionVerifySuccess(PsnBatchTransActCollectionVerifyViewModel viewModel) {
        this.psnBatchTransActCollectionVerifyViewModel = viewModel;
        // 获取随机数
        payConfirmPresenter.getRandom();

        CombinBean combinBean = new CombinBean();
        CombinListBean combinListBean = payConfirmPresenter.combinListBean;
        combinBean.setId(combinListBean.getId());
        combinBean.setName(combinListBean.getName());
        combinBean.setSafetyFactorList(combinListBean.getSafetyFactorList());
        curSelectedCombin = combinBean;
    }

    @Override
    public void psnBatchTransActCollectionVerifyFailed(BiiResultErrorException biiResultErrorException) {
        Toast.makeText(mActivity, "批量主动收款预交易失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void psnBatchTransActCollectionSubmitSuccess(PsnBatchTransActCollectionSubmitViewModel viewModel) {
        // 跳转到“操作结果”页面
        OperateResultFragment operateResultFragment = new OperateResultFragment();
        Bundle bundle = new Bundle();

        bundle.putInt("type", 1); // 0表示单人，1 表示多人
        bundle.putSerializable("psnBatchTransActCollectionVerifyViewModel", psnBatchTransActCollectionVerifyViewModel);

        operateResultFragment.setArguments(bundle);
        start(operateResultFragment);
    }

    @Override
    public void psnBatchTransActCollectionSubmitFailed(BiiResultErrorException biiResultErrorException) {
    }

    private void goToOperateResultFragment1(int isSuccess) {
        // 发送广播关闭“我要收款”界面
        //        Intent intent = new Intent(ACTION_NOTIFY);
        //        mActivity.sendBroadcast(intent);
        // 跳转到“操作结果”页面

        OperateResultFragment1 operateResultFragment = new OperateResultFragment1();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 0); // 0表示单人，1 表示多人
        bundle.putInt("isSuccess", isSuccess); // 0表示成功，1 表示失败
        psnTransActCollectionVerifyViewModel.setNotifyId(psnTransActCollectionSubmitViewModel.getNotifyId());
        bundle.putSerializable("psnTransActCollectionVerifyViewModel", psnTransActCollectionVerifyViewModel);
        operateResultFragment.setArguments(bundle);
        start(operateResultFragment);
    }

    @Override
    public void savePayerSuccess(SavePayerViewModel savePayerViewModel) {
        goToOperateResultFragment1(0);
    }

    @Override
    public void savePayerFail(BiiResultErrorException biiResultErrorException) {
        goToOperateResultFragment1(1);
    }

    @Override
    public void getRandomSuccess(String random) {
        this.random = random;

        // 显示安全认证对话框
        if (SecurityVerity.getInstance().confirmFactor(copyToFactorListBean())) { // 此处注意
            SecurityVerity.getInstance().setConversationId(payConfirmPresenter.conversationId);
            SecurityVerity.getInstance().showSecurityDialog(random);
        }
    }

    @Override
    public void getRandomFailed(BiiResultErrorException biiResultErrorException) {
    }

    @Override
    public void setPresenter(PayConfirmContact.Presenter presenter) {
    }
}