package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialog.MessageDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.model.OpenWealthModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.presenter.OpenWealthPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;

/**
 * 开通投资理财服务
 * Created by wangtong on 2016/10/20.
 * Modified by liuweidong on 2016/11/24
 */
@SuppressLint("ValidFragment")
public class OpenWealthManagerFragment extends MvpBussFragment<OpenWealthPresenter> implements OpenWealthManagerContact.OpenView,
        SecurityVerity.VerifyCodeResultListener {

    public static final String FROME_CLASS = "from_class";
    public static final String VIEW_MODEL = "view_model";
    private View rootView = null;
    private DetailTableRowButton btnSecurity;// 安全因子
    private CheckBox checkBox;
    private TextView userLimit;// 协议
    private Button btnOpen;// 开通按钮

    public OpenWealthModel mViewModel;
    private Class<? extends BussFragment> fromeClass;

    public OpenWealthManagerFragment(Class<? extends BussFragment> clazz) {
        fromeClass = clazz;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_open_wealth_manager, null);
        return rootView;
    }

    @Override
    public void initView() {
        btnSecurity = (DetailTableRowButton) rootView.findViewById(R.id.btn_security);
        checkBox = (CheckBox) rootView.findViewById(R.id.accept_rule);
        userLimit = (TextView) rootView.findViewById(R.id.user_limit);
        btnOpen = (Button) rootView.findViewById(R.id.btn_open);
    }

    @Override
    public void initData() {
        mViewModel = (OpenWealthModel) getArguments().getSerializable(VIEW_MODEL);
        initUserLimit();
        CombinListBean bean = SecurityVerity.getInstance(getActivity()).getDefaultSecurityFactorId(mViewModel.getFactorModel());
        btnSecurity.addTextBtn(getContext().getString(R.string.security_title), bean.getName(),
                getContext().getString(R.string.security_change_verify), getResources().getColor(R.color.boc_main_button_color));
        mViewModel.selectedFactorId = bean.getId();
    }

    @Override
    public void setListener() {
        SecurityVerity.getInstance(getActivity()).setSecurityVerifyListener(this);
        btnOpen.setOnClickListener(new View.OnClickListener() {// 开通
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    final MessageDialog dialog = new MessageDialog(getContext());
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setRightButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getPresenter().psnInvestmentManageOpenConfirm();
                            dialog.dismiss();
                        }
                    });
                    String hint = getString(R.string.boc_wealth_open_hint_2);
                    SpannableStringBuilder style = new SpannableStringBuilder(hint);
                    style.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    dialog.showDialog(getString(R.string.boc_wealth_open_hint_1), style);
                } else {
                    showErrorDialog(getString(R.string.boc_wealth_open_no_read));
                }
            }
        });

        btnSecurity.setOnclick(new DetailTableRowButton.BtnCallback() {
            @Override
            public void onClickListener() {
                SecurityVerity.getInstance().selectSecurityType();
            }
        });
    }

    @Override
    protected OpenWealthPresenter initPresenter() {
        return new OpenWealthPresenter(this);
    }

    /**
     * 初始化协议须知
     */
    private void initUserLimit() {
        String limitString = userLimit.getText().toString();
        SpannableStringBuilder style = new SpannableStringBuilder(limitString);
        userLimit.setMovementMethod(LinkMovementMethod.getInstance());
        style.setSpan(new MyClickableSpan(), 9, 29, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        userLimit.setMovementMethod(LinkMovementMethod.getInstance());
        userLimit.setText(style);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(fromeClass);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_wealth_open_title);
    }

    @Override
    public void psnInvestmentManageOpenConfirmReturned() {
        boolean isSecurity = SecurityVerity.getInstance().confirmFactor(mViewModel.preFactorList);
        if (isSecurity) {
            Activity activity = getActivity();
            EShieldVerify.getInstance(activity).setmPlainData(mViewModel.mPlainData);
            SecurityVerity.getInstance().showSecurityDialog(mViewModel.getRandomNum());
        } else {
            showErrorDialog("您的交易环境存在风险，请排除风险后重试!");
        }
    }

    @Override
    public boolean onBackPress() {
        popToAndReInit(fromeClass);
        return true;
    }

    @Override
    public void psnInvestmentManageOpenReturned() {
        boolean[] status = WealthProductFragment.getInstance().isOpenWealth();
        status[0] = true;
        WealthProductFragment.getInstance().setOpenWealth(status);
        popToAndReInit(fromeClass);
    }

    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        mViewModel.selectedFactorId = bean.getId();
        btnSecurity.addTextBtn(getContext().getString(R.string.security_title), bean.getName(),
                getContext().getString(R.string.security_change_verify), getResources().getColor(R.color.boc_main_button_color));
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        mViewModel.encryptPasswords = encryptPasswords;
        mViewModel.encryptRandomNums = randomNums;
        getPresenter().psnInvestmentManageOpen();
    }

    @Override
    public void onSignedReturn(String signRetData) {
        mViewModel.mSignData = signRetData;
        getPresenter().psnInvestmentManageOpen();
    }

    class MyClickableSpan extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
            start(ContractFragment.newInstance("file:///android_asset/webviewcontent/investmentprotocol/CustomerInvestmentProtocol.html", ApplicationContext.getInstance().getUser().getCustomerName()));
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
            ds.setColor(getResources().getColor(R.color.boc_text_color_red));
        }
    }

}
