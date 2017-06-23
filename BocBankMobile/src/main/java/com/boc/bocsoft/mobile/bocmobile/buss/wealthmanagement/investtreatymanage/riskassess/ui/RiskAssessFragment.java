package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.ui;

import android.annotation.SuppressLint;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.model.AccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.presenter.Clickable;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.presenter.RiskAssessContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.presenter.RiskAssessPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui.PurchaseFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 风险评估
 * Created by guokai on 2016/9/17.
 */
@SuppressLint("ValidFragment")
public class RiskAssessFragment extends MvpBussFragment<RiskAssessPresenter> implements RiskAssessContract.RiskAssessView, View.OnClickListener {

    //风险的等级类型
    private TextView tvRiskType;
    //默认选择的风险等级
    private TextView tvLevel, tvDetail, tvRiskTitle;
    private ImageView ivLevel;
    private Button btnOk;

    private AccountModel model;
    private LinearLayout riskLevel;
    private String desc = "";
    private Class<? extends BussFragment> fromeClass;

    public static final int RISK_ASSESS_ACCOUNT = 1;//账户管理
    public static final int RISK_ASSESS_CHOICE = 2;//评估答题

    private int requestCode;

    public RiskAssessFragment(Class<? extends BussFragment> clazz, int requestCode) {
        this.fromeClass = clazz;
        this.requestCode = requestCode;
    }

    public RiskAssessFragment(Class<? extends BussFragment> clazz) {
        this.fromeClass = clazz;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.risk_assess_fragment, null);
    }

    @Override
    protected String getTitleValue() {
        if (requestCode == RISK_ASSESS_ACCOUNT) {
            return getString(R.string.boc_risk_assess_question);
        } else if (requestCode == RISK_ASSESS_CHOICE) {
            return getString(R.string.boc_risk_assess_question_result);
        }
        return getString(R.string.boc_risk_assess_question);
    }

    /**
     * 是否显示右侧标题按钮
     */
    protected boolean isDisplayRightIcon() {
        return false;
    }

    /**
     * 是否显示左侧标题按钮
     */
    protected boolean isDisplayLeftIcon() {
        return true;
    }

    /**
     * 红色主题titleBar：true ；
     * 白色主题titleBar：false ；
     */
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        if (fromeClass.isInstance(PurchaseFragment.class)){
            popTo(fromeClass,false);
        }else {
            popToAndReInit(fromeClass);
        }

    }

    @Override
    public boolean onBack() {
        if (fromeClass.isInstance(PurchaseFragment.class)){
            popTo(fromeClass,false);
        }else {
            popToAndReInit(fromeClass);
        }
        return false;
    }


    @Override
    public void initView() {
        tvRiskType = (TextView) mContentView.findViewById(R.id.tv_risk_type);
        riskLevel = (LinearLayout) mContentView.findViewById(R.id.tv_risk_level);
        tvLevel = (TextView) mContentView.findViewById(R.id.tv_level);
        ivLevel = (ImageView) mContentView.findViewById(R.id.iv_risk);
        tvRiskTitle = (TextView) mContentView.findViewById(R.id.tv_risk_title);
        tvDetail = (TextView) mContentView.findViewById(R.id.tv_detail);
        btnOk = (Button) mContentView.findViewById(R.id.btn_ok);
    }

    /**
     * 投资者不同类型描述的判断
     */
    private void setView(String level) {
        if ("1".equals(level)) {
            tvRiskTitle.setText(getString(R.string.boc_risk_assess_bao_shou));
            ivLevel.setBackgroundDrawable(getResources().getDrawable(R.drawable.boc_risk_assess_bao_shou));
            desc = getString(R.string.boc_risk_assess_bao_shou_xing);
        } else if ("2".equals(level)) {
            tvRiskTitle.setText(getString(R.string.boc_risk_assess_wen_jian));
            ivLevel.setBackgroundDrawable(getResources().getDrawable(R.drawable.boc_risk_assess_wen_jian));
            desc = getString(R.string.boc_risk_assess_wen_jian_xing);
        } else if ("3".equals(level)) {
            tvRiskTitle.setText(getString(R.string.boc_risk_assess_ping_heng));
            ivLevel.setBackgroundDrawable(getResources().getDrawable(R.drawable.boc_risk_assess_ping_heng));
            desc = getString(R.string.boc_risk_assess_ping_heng_xing);
        } else if ("4".equals(level)) {
            tvRiskTitle.setText(getString(R.string.boc_risk_assess_cheng_zhang));
            ivLevel.setBackgroundDrawable(getResources().getDrawable(R.drawable.boc_risk_assess_cheng_zhang));
            desc = getString(R.string.boc_risk_assess_cheng_zhang_xing);
        } else if ("5".equals(level)) {
            tvRiskTitle.setText(getString(R.string.boc_risk_assess_jin_qu));
            ivLevel.setBackgroundDrawable(getResources().getDrawable(R.drawable.boc_risk_assess_jin_qu));
            desc = getString(R.string.boc_risk_assess_jin_qu_xing);
        } else {
            tvRiskTitle.setText("");
        }
        getClickSpan();
    }

    /**
     * 对投资者的描述展开点击事件的处理
     */
    private void getClickSpan() {
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件
                getClickSpan2();
            }
        };
        String s = desc.substring(0, 70) + " " + "展开";
        SpannableStringBuilder style = new SpannableStringBuilder(s);
        style.setSpan(new Clickable(l), s.length() - 2, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.boc_main_button_color))
                , s.length() - 2, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDetail.setText(style);
        tvDetail.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 对投资者的描述收起点击事件的处理
     */
    private void getClickSpan2() {
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件
                getClickSpan();
            }
        };
        String s = desc + "收起";
        SpannableStringBuilder style = new SpannableStringBuilder(s);
        style.setSpan(new Clickable(l), s.length() - 2, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.boc_main_button_color))
                , s.length() - 2, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDetail.setText(style);
        tvDetail.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void initData() {
        if (requestCode == RISK_ASSESS_CHOICE) {
            boolean[] openWealth = WealthProductFragment.getInstance().isOpenWealth();
            openWealth[2] = true;
            WealthProductFragment.getInstance().setOpenWealth(openWealth);
        }
        showLoadingDialog();
        getPresenter().psnInvtEvaluationInit();

    }

    /**
     * 界面内容的显示
     */
    private void setRiskLevel(String level) {
        if ("1".equals(level)) {
            tvRiskType.setText(getString(R.string.boc_risk_assess_keep));
        } else if ("2".equals(level)) {
            tvRiskType.setText(getString(R.string.boc_risk_assess_steady));
        } else if ("3".equals(level)) {
            tvRiskType.setText(getString(R.string.boc_risk_assess_balance));
        } else if ("4".equals(level)) {
            tvRiskType.setText(getString(R.string.boc_risk_assess_grow));
        } else if ("5".equals(level)) {
            tvRiskType.setText(getString(R.string.boc_risk_assess_progress));
        } else {
            tvRiskType.setText(getString(R.string.boc_risk_assess_not));
        }

        if (requestCode == RISK_ASSESS_ACCOUNT) {
            btnOk.setText(getString(R.string.boc_risk_assess_again));
            riskLevel.setVisibility(View.VISIBLE);
        } else if (requestCode == RISK_ASSESS_CHOICE) {
            riskLevel.setVisibility(View.INVISIBLE);
            tvLevel.setClickable(false);
            btnOk.setText(getString(R.string.boc_risk_assess_accomplish));
        } else {
            btnOk.setText(getString(R.string.boc_risk_assess_again));
            riskLevel.setVisibility(View.VISIBLE);
        }
    }


    public AccountModel getModel() {
        return model;
    }

    @Override
    public void setListener() {
        tvLevel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //点击默认的风险等级按钮
        if (id == R.id.tv_level) {
            showLoadingDialog();
            getPresenter().psnInvtEvaluationResult();
            //点击button按钮
        } else if (id == R.id.btn_ok) {
            if (requestCode == RISK_ASSESS_ACCOUNT) {
                startWithPop(new RiskAssessChoiceFragment(fromeClass, RiskAssessChoiceFragment.RISK_ASSESS_ACCOUNT));
            } else if (requestCode == RISK_ASSESS_CHOICE) {
                popToAndReInit(fromeClass);
            } else {
                startWithPop(new RiskAssessChoiceFragment(fromeClass));
            }
        }
    }

    /**
     * 风险查询
     */
    @Override
    public void psnInvtEvaluationInit(String level) {
        closeProgressDialog();
        setRiskLevel(level);
        setView(level);
    }

    /**
     * 风险评估提交
     */
    @Override
    public void psnInvtEvaluationResult(String riskLevel) {
        closeProgressDialog();
        setRiskLevel(riskLevel);
        setView(riskLevel);
    }

    @Override
    protected RiskAssessPresenter initPresenter() {
        return new RiskAssessPresenter(this);
    }
}
