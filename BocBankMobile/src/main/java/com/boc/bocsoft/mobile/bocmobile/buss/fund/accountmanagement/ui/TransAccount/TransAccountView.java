package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.TransAccount;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnCancelFundAccount.PsnCancelFundAccountResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.container.MvpContainer;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ConfirmDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.ChangeCardModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.presenter.TransAccountPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.AccountManagementFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.FundChangeCardFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.TransAccountCancelResultFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.InvstBindingInfoViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.ArrayList;

/**
 * 账户管理首页-基金交易账户View
 * Created by lyf7084 on 2016/12/16.
 */
public class TransAccountView extends MvpContainer<TransAccountPresenter>
        implements TransAccountContract.View, BaseView<TransAccountPresenter> {

    private View rootView;
    private DetailTableRowButton fincAccount;    // 基金交易账户
    private DetailTableRowButton accountNo;    // 资金帐户
    private DetailRow qccBalanceA;     // 可用余额

    protected TransAccountCancelResultFragment mTransAccountCancelResultFragment;
    private InvstBindingInfoViewModel mBindingInfoModel = null;    // 基金账户信息

    // 是否是初次实现view
    private boolean isTransView = true;

    public TransAccountView(Context context) {
        this(context, null, 0);
    }

    public TransAccountView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransAccountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setBindingInfo(InvstBindingInfoViewModel mBindingInfoModel) {
        this.mBindingInfoModel = mBindingInfoModel;
    }

    @Override
    protected TransAccountPresenter initPresenter() {
        return new TransAccountPresenter(this);
    }

    /**
     * 初始化布局
     */
    @Override
    protected View createContentView() {
        rootView = inflate(getContext(), R.layout.boc_fragment_fund_trans_account, null);
        LogUtil.d("Liyifan------------> OnCreate TransAccount View");
        return rootView;
    }

    private void initView(Context context) {
        fincAccount = (DetailTableRowButton) rootView.findViewById(R.id.fincAccount);
        accountNo = (DetailTableRowButton) rootView.findViewById(R.id.accountNo);
        qccBalanceA = (DetailRow) rootView.findViewById(R.id.qccBalanceA);
    }

    @Override
    public void initData() {
        // TODO:账户格式化***显示前4位 后四位
        qccBalanceA.updateData(getResources().getString(R.string.boc_fund_available_balance), "人民币元10,000.00\n美元/钞10,000.00");
        // TODO:金额的处理以及 美元/钞 如何换行
        qccBalanceA.showDividerLine(View.VISIBLE);
        fincAccount.clearValueWeight(); // 按钮由靠右格式变为紧跟Value 后方

        fincAccount.addTextBtn(getResources().getString(R.string.boc_fund_trans_account), mBindingInfoModel.getInvestAccount(),
                getResources().getString(R.string.boc_fund_account_cancel), getResources().getColor(R.color.boc_text_color_red));
        accountNo.clearValueWeight();
        accountNo.addTextBtn(getResources().getString(R.string.boc_fund_accountNo),
                NumberUtils.formatCardNumber(mBindingInfoModel.getAccount()),
                getResources().getString(R.string.boc_fund_change), getResources().getColor(R.color.boc_main_button_color));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isTransView && isVisibleToUser) {
//            局部刷新
//            getPresenter().queryQccBalance();
        }
    }

    //    父类限定setListener为protected,错误使用public
    @Override
    protected void setListener() {
        super.setListener();

        fincAccount.setOnclick(new DetailTableRowButton.BtnCallback() {
            @Override
            public void onClickListener() {

                if (((AccountManagementFragment) mBussFragment).getTAList() == null) {
                    showLoadingDialog();
                    getPresenter().queryTaAccList();
                } else {
                    checkTAList();
                }
            }
        });
        accountNo.setOnclick(new DetailTableRowButton.BtnCallback() {
            @Override
            public void onClickListener() {
                showLoadingDialog();
                getPresenter().queryCardList();
            }
        });

    }

    public void checkTAList() {
        //  判断取回的list的size判断 显示两种Dialog
        if (((AccountManagementFragment) mBussFragment).getTAList().size() == 0) {
            final ConfirmDialog dialog = new ConfirmDialog(getContext());
            dialog.setMessage("是否确认销户？")
                    .setLeftButton("我再想想")
                    .setRightButton("销户")
                    .setButtonClickInterface(new ConfirmDialog.OnButtonClickInterface() {
                        @Override
                        public void onLeftClick(ConfirmDialog warnDialog) {
                            dialog.cancel();
                        }

                        @Override
                        public void onRightClick(ConfirmDialog warnDialog) {
                            showLoadingDialog();
                            getPresenter().transAccCancelSubmit();
                        }
                    });
            dialog.show();
        } else {
            final ConfirmDialog dialog = new ConfirmDialog(getContext());
            dialog.setMessage("您仍有关联基金TA账户，请在取消关联或销户后再进行基金交易账户销户操作。")
                    .setLeftButton("查看基金TA账户")
                    .setRightButton("我知道了")
                    .setButtonClickInterface(new ConfirmDialog.OnButtonClickInterface() {
                        @Override
                        public void onRightClick(ConfirmDialog warnDialog) {
                        }

                        @Override
                        public void onLeftClick(ConfirmDialog warnDialog) {
                            // TODO: Tab页面的切换如何处理
//                            mSwitchTabListener.onSwitch(1);
                            ((AccountManagementFragment) mBussFragment).getLytTab().setCurrentTab(1);
                        }
                    });
            dialog.show();
        }

    }

    @Override
    public void queryTaAccListSuccess(TaAccountModel result) {
        closeProgressDialog();
        ((AccountManagementFragment) mBussFragment).setTAList(result.getTaAccountList());
        checkTAList();
    }

//    private SwitchTabListener mSwitchTabListener;
//    public void setSwitchTabListener(SwitchTabListener mSwitchTabListener) {
//        this.mSwitchTabListener = mSwitchTabListener;
//    }
//    public interface  SwitchTabListener{
//        void onSwitch(int pos);
//    }


    @Override
    public void queryTaAccListFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        Toast.makeText(getContext(), "请求失败", Toast.LENGTH_LONG).show();
        //// TODO: 如何提示
    }

    @Override
    public void transAccCancelSubmitSuccess(PsnCancelFundAccountResult result) {
        closeProgressDialog();
        mBussFragment.start(mTransAccountCancelResultFragment);
    }

    @Override
    public void transAccCancelSubmitFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        LogUtil.d("当前交易账户 :" + fincAccount);
        Toast.makeText(getContext(), "请求失败", Toast.LENGTH_LONG).show();
        //// TODO: 如何提示
    }

    @Override
    public void obtainConversationSuccess(String conversationId) {

    }

    @Override
    public void obtainConversationFail(BiiResultErrorException biiResultErrorException) {

    }

    /**
     * 调用接口076成功 获取资金账户列表ChangCardModel.CardListBean
     * 并将List<CardListBean>数据填充至父类的AccountBean
     */
    @Override
    public void queryCardListSuccess(ChangeCardModel result) {
        closeProgressDialog();
        ArrayList<AccountBean> list = new ArrayList<AccountBean>();
        for (ChangeCardModel.CardListBean resBean : result.getCardList()) {
            AccountBean bean = new AccountBean();
            bean.setAccountNumber(resBean.getAccountNumber());
            bean.setNickName(resBean.getNickName());
            bean.setAccountType(resBean.getAccountType());
            bean.setAccountName(resBean.getAccountName());
            bean.setAccountId(resBean.getAccountId());
            list.add(bean);
        }
        // newInstanceWithData方法传递list 有本页面至FundChangeCardFragment
        FundChangeCardFragment fragment = FundChangeCardFragment.newInstanceWithData(list);
        mBussFragment.start(fragment);
    }

    @Override
    public void queryCardListFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        Toast.makeText(getContext(), "请求失败", Toast.LENGTH_LONG).show();
        //// TODO: 如何提示
    }

}
