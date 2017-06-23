package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.CombinBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.adapter.PayerAccountAdapter2;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnBatchTransActCollectionSubmitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnBatchTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnTransActCollectionSubmitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.SavePayerViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter.PayConfirmPresenter;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Fragment：支付确认信息界面（我要收款）
 * Created by zhx on 2016/7/12
 * serviceId:转账汇款->主动收款->主动收款预交易	PB037
 */
public class PayConfirmImformationFragment extends BussFragment implements SecurityVerity.VerifyCodeResultListener, PayConfirmContact.View {
    // 会话ID
    public static String conversationId;
    /**
     * 当前选择的安全因子
     */
    private CombinBean curSelectedCombin;
    private PsnTransActCollectionVerifyViewModel psnTransActCollectionVerifyViewModel;
    private PsnBatchTransActCollectionVerifyViewModel psnBatchTransActCollectionVerifyViewModel;

    private View mRootView;
    // 安全信息
    private DetailTableRowButton securityInfo;
    private PayConfirmPresenter payConfirmPresenter;
    private String random;
    private Button btn_submit;
    private int type;
    private TextView tv_payer;
    private TextView tv_average_amount;
    private TextView tv_total_amount;
    private TextView tv_payee_actno;
    private TextView tv_remark;
    private ListView lv_payer_list;
    private LinearLayout ll_payer_container;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_pay_confirm_imformation, null);
        return mRootView;
    }

    @Override
    public void initView() {
        securityInfo = (DetailTableRowButton) mRootView.findViewById(R.id.security_info);
        tv_payer = (TextView) mRootView.findViewById(R.id.tv_payer);
        tv_average_amount = (TextView) mRootView.findViewById(R.id.tv_average_amount);
        tv_total_amount = (TextView) mRootView.findViewById(R.id.tv_total_amount);
        tv_payee_actno = (TextView) mRootView.findViewById(R.id.tv_payee_actno);
        tv_remark = (TextView) mRootView.findViewById(R.id.tv_remark);
        lv_payer_list = (ListView) mRootView.findViewById(R.id.lv_payer_list);
        ll_payer_container = (LinearLayout) mRootView.findViewById(R.id.ll_payer_container);
        btn_submit = (Button) mRootView.findViewById(R.id.btn_submit);
    }

    @Override
    public void initData() {
        payConfirmPresenter = new PayConfirmPresenter(this);
        type = getArguments().getInt("type", 0);
        if (type == 0) { // 主动收款（单人）
            psnTransActCollectionVerifyViewModel = (PsnTransActCollectionVerifyViewModel) getArguments().getSerializable("psnTransActCollectionVerifyViewModel");
            payConfirmPresenter.collectionVerify(psnTransActCollectionVerifyViewModel);

            // 设置页面的数据
            tv_total_amount.setText(psnTransActCollectionVerifyViewModel.getNotifyPayeeAmount());
            tv_average_amount.setText(psnTransActCollectionVerifyViewModel.getNotifyPayeeAmount());
            tv_payee_actno.setText(psnTransActCollectionVerifyViewModel.getPayeeActno());
            tv_remark.setText(psnTransActCollectionVerifyViewModel.getRemark());
            tv_payer.setText(psnTransActCollectionVerifyViewModel.getPayerName());
            lv_payer_list.setVisibility(View.GONE);


            // TODO 此处下面写法是否正确，有待确认
            securityInfo.addTextBtn("安全认证", psnTransActCollectionVerifyViewModel.get_combinName(), "更改", getResources().getColor(R.color.boc_main_button_color));
        } else if (type == 1) { // 主动收款（多人）
            psnBatchTransActCollectionVerifyViewModel = (PsnBatchTransActCollectionVerifyViewModel) getArguments().getSerializable("psnBatchTransActCollectionVerifyViewModel");

            ////////////////////////////和结果页面基本一致(开始)///////////////////////////
            tv_total_amount.setText(psnBatchTransActCollectionVerifyViewModel.getTotalAmount()); // 总金额
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
            securityInfo.addTextBtn("安全认证", psnBatchTransActCollectionVerifyViewModel.get_combinName(), "更改", getResources().getColor(R.color.boc_main_button_color));
        }

        SecurityVerity.getInstance(mActivity).setSecurityVerifyListener(this);
    }

    @Override
    public void setListener() {
        securityInfo.setOnclick(new DetailTableRowButton.BtnCallback() {
            @Override
            public void onClickListener() {
                //获取安全因子
                SecurityVerity.getInstance().getDefaultSecurityFactorId(new SecurityFactorModel(payConfirmPresenter.getPsnGetSecurityFactorResult()));
                //显示安全认证选择对话框
                SecurityVerity.getInstance().selectSecurityType();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecurityVerity.getInstance().showSecurityDialog(random);
            }
        });
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "确认信息";
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
        if (String.valueOf(SecurityVerity.SECURITY_VERIFY_DEVICE).equals(bean.getId())) {
            securityInfo.setTxtValue(getResources().getString(R.string.security_sms_name));
        } else {
            securityInfo.setTxtValue(bean.getName());
        }
        Log.e("ljljlj", "bean = " + bean);
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        Log.e("ljljlj", "factorId = " + factorId);
        Log.e("ljljlj", "randomNums = " + Arrays.toString(randomNums));
        Log.e("ljljlj", "encryptPasswords = " + Arrays.toString(encryptPasswords));

        if (type == 0) { // 主动收款（单人）
            PsnTransActCollectionSubmitViewModel psnTransActCollectionSubmitViewModel = generateSubmitViewModel();
            payConfirmPresenter.collectionSubmit(psnTransActCollectionSubmitViewModel, randomNums, encryptPasswords, Integer.valueOf(curSelectedCombin.getId()), mActivity);
        } else { // 主动收款（多人）
            PsnBatchTransActCollectionSubmitViewModel psnBatchTransActCollectionSubmitViewModel = generateBatchSubmitViewModel();
            payConfirmPresenter.psnBatchTransActCollectionSubmit(psnBatchTransActCollectionSubmitViewModel, randomNums, encryptPasswords, Integer.valueOf(curSelectedCombin.getId()), mActivity);
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

    @Override
    public void onSignedReturn(String signRetData) { // 用到中银E盾时才会调用这个方法
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

        //
        CombinBean combinBean = new CombinBean();
        CombinListBean combinListBean = payConfirmPresenter.combinListBean;
        combinBean.setId(combinListBean.getId());
        combinBean.setName(combinListBean.getName());
        combinBean.setSafetyFactorList(combinListBean.getSafetyFactorList());
        curSelectedCombin = combinBean;


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
        // 跳转到“操作结果”页面
        OperateResultFragment operateResultFragment = new OperateResultFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("type", 0); // 0表示单人，1 表示多人
        bundle.putSerializable("psnTransActCollectionVerifyViewModel", psnTransActCollectionVerifyViewModel);

        operateResultFragment.setArguments(bundle);
        PayConfirmImformationFragment.this.pop();
        start(operateResultFragment);
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


        // （此处参照刘卫东的写法）
        // 显示安全认证对话框
        if (SecurityVerity.getInstance().confirmFactor(viewModel.getFactorList())) {
            SecurityVerity.getInstance().setConversationId(payConfirmPresenter.conversationId);
        }
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

    @Override
    public void savePayerSuccess(SavePayerViewModel savePayerViewModel) {

    }

    @Override
    public void savePayerFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void getRandomSuccess(String random) {
        this.random = random;
    }

    @Override
    public void getRandomFailed(BiiResultErrorException biiResultErrorException) {
    }

    @Override
    public void setPresenter(PayConfirmContact.Presenter presenter) {
    }
}