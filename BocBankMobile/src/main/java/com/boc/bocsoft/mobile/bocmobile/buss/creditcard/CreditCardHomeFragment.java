package com.boc.bocsoft.mobile.bocmobile.buss.creditcard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.ui.AttCardMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.ui.AutoCrcdPaymentMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.ui.BillInstallmentsFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.ui.CashInstallmentFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.ui.ConsumeInstallmentFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.ui.ConsumeQryFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.ui.CrcdHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.ui.InstallmentHistoryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cry7096 on 2016/11/21.
 * 信用卡功能跳转
 */
public class CreditCardHomeFragment extends BussFragment implements View.OnClickListener {
    private View rootView;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private TextView t4;
    private TextView t5;
    private TextView t6;
    private TextView t7;
    private TextView t8;
    private TextView t21;
    private List<AccountBean> accountList;
    private TextView t22;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView=mInflater.inflate(R.layout.boc_fragment_credit_card_home,null);
        t1 = (TextView)rootView.findViewById(R.id.fun1);
        t2 = (TextView)rootView.findViewById(R.id.fun2);
        t21 = (TextView)rootView.findViewById(R.id.fun21);
        t22 = (TextView)rootView.findViewById(R.id.fun22);
        t3 = (TextView)rootView.findViewById(R.id.fun3);
        t4 = (TextView)rootView.findViewById(R.id.fun4);
        t5 = (TextView)rootView.findViewById(R.id.fun5);
        t6 = (TextView)rootView.findViewById(R.id.fun6);
        t7 = (TextView)rootView.findViewById(R.id.fun7);
        t8 = (TextView)rootView.findViewById(R.id.fun8);
        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t21.setOnClickListener(this);
        t22.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        t5.setOnClickListener(this);
        t6.setOnClickListener(this);
        t7.setOnClickListener(this);
        t8.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();

    }

    @Override
    public void initData() {
        super.initData();
        filterAccountType();
    }

    /**
     * 过滤账户类型
     */
    private void filterAccountType() {
        ArrayList<String> accountTypeList = new ArrayList<String>();
        // 中银系列信用卡 103
        accountTypeList.add(ApplicationConst.ACC_TYPE_ZHONGYIN);
        //长城信用卡 104
        accountTypeList.add(ApplicationConst.ACC_TYPE_GRE);
        // 单外币信用卡 107
        accountTypeList.add(ApplicationConst.ACC_TYPE_SINGLEWAIBI);
        accountList = ApplicationContext.getInstance().getChinaBankAccountList(accountTypeList);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //信用卡首页
        if(id == R.id.fun1){
            CrcdHomeFragment overseasHomeFragment = new CrcdHomeFragment();
            start(overseasHomeFragment);
        }
        //立即还款
        else if(id == R.id.fun2){

//            RepaymentMainFragment repaymentMainFragment=new RepaymentMainFragment();
//            Bundle bundle=new Bundle();
//            bundle.putString(RepaymentMainFragment.CRCD_ACC_ID_KEY,accountList.get(0).getAccountId());
//            bundle.putString(RepaymentMainFragment.CRCD_ACC_NO_KEY,accountList.get(0).getAccountNumber());
//            repaymentMainFragment.setArguments(bundle);
//            start(repaymentMainFragment);

            ConsumeQryFragment billQueryYFragment=new ConsumeQryFragment();
            Bundle bundle=new Bundle();
            bundle.putParcelable(ConsumeQryFragment.ACCOUNT_BEAN,accountList.get(0));
            billQueryYFragment.setArguments(bundle);
            start(billQueryYFragment);
        }
        //立即还款
        else if(id == R.id.fun21){
            if (accountList.size()<2){
                ToastUtils.show("该账户下只有一张卡");
                return;
            }
//            RepaymentMainFragment repaymentMainFragment=new RepaymentMainFragment();
//            Bundle bundle=new Bundle();
//            bundle.putString(RepaymentMainFragment.CRCD_ACC_ID_KEY,accountList.get(1).getAccountId());
//            bundle.putString(RepaymentMainFragment.CRCD_ACC_NO_KEY,accountList.get(1).getAccountNumber());
//            repaymentMainFragment.setArguments(bundle);
//            start(repaymentMainFragment);
            ConsumeQryFragment billQueryYFragment=new ConsumeQryFragment();
            Bundle bundle=new Bundle();
            bundle.putParcelable(ConsumeQryFragment.ACCOUNT_BEAN,accountList.get(1));
            billQueryYFragment.setArguments(bundle);
            start(billQueryYFragment);
        }
        //立即还款
        else if(id == R.id.fun22){
            if (accountList.size()<3){
                ToastUtils.show("该账户下只有两张卡");
                return;
            }
//            RepaymentMainFragment repaymentMainFragment=new RepaymentMainFragment();
//            Bundle bundle=new Bundle();
//            bundle.putString(RepaymentMainFragment.CRCD_ACC_ID_KEY,accountList.get(2).getAccountId());
//            bundle.putString(RepaymentMainFragment.CRCD_ACC_NO_KEY,accountList.get(2).getAccountNumber());
//            repaymentMainFragment.setArguments(bundle);
//            start(repaymentMainFragment);
            ConsumeQryFragment billQueryYFragment=new ConsumeQryFragment();
            Bundle bundle=new Bundle();
            bundle.putParcelable(ConsumeQryFragment.ACCOUNT_BEAN,accountList.get(2));
            billQueryYFragment.setArguments(bundle);
            start(billQueryYFragment);
        }
        //分期记录
        else if(id == R.id.fun3){
            start(InstallmentHistoryFragment.newInstance(null));
        }
        //自动还款
        else if(id == R.id.fun4){
            start(new AutoCrcdPaymentMainFragment());
            //start(new AutoCrcdPaymentOpenedFragment());
        }
        //消费分期
        else if(id == R.id.fun5){
            start(new ConsumeInstallmentFragment());
//            showLoadingDialog();
//            ConSumeQryPresenter mcqpresenter = new ConSumeQryPresenter(this);
//            PsnCrcdDividedPayConsumeQryParams params = new PsnCrcdDividedPayConsumeQryParams();
//            params.setAccountId(125964206);
//            params.setCurrencyCode("001");
//            params.setCurrentIndex("0");
//            params.setPageSize("12");
//            mcqpresenter.crcdDividedPayConsumeQry( params);




        }
        //现金分期
        else if(id == R.id.fun6){
            CashInstallmentFragment cashInstallmentFragment = new CashInstallmentFragment();
            start(cashInstallmentFragment);
        }
        //附属卡管理
        else if(id == R.id.fun7){
            //start(new AttCardMessageSetupFragment());
            start(new AttCardMainFragment());
        }
        //账单分期
        else if(id == R.id.fun8){
           start(new BillInstallmentsFragment());
        }
    }

}
