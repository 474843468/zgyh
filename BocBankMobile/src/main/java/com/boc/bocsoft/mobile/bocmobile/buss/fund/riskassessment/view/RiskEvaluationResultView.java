package com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;

/**
 * Created by taoyongzhen on 2016/11/28.
 * 风险评估结果展示页面
 */

public class RiskEvaluationResultView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private View rootView;
    //风险的等级类型
    private TextView tvRiskType;
    //默认选择的风险等级
    private TextView tvLevel, tvDetail, tvRiskTitle;
    private ImageView ivLevel;
    private Button btnOk;
    private LinearLayout riskLevel;

    private int requestCode = -1;

    private String desc = "";
    private String levelIndex[];
    private String tvRiskTypeS[];
    private String tvRiskTitles[];
    private String descs[];
    private int riskDraws[];



    private RiskEvaluationListener riskEvaluationListener;

    public RiskEvaluationResultView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public RiskEvaluationResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public RiskEvaluationResultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.risk_assess_fragment, this);
        riskLevel = (LinearLayout) rootView.findViewById(R.id.tv_risk_level);
        tvLevel = (TextView) rootView.findViewById(R.id.tv_level);
        ivLevel = (ImageView) rootView.findViewById(R.id.iv_risk);
        tvRiskTitle = (TextView) rootView.findViewById(R.id.tv_risk_title);
        tvDetail = (TextView) rootView.findViewById(R.id.tv_detail);
        btnOk = (Button) rootView.findViewById(R.id.btn_ok);
        tvRiskType = (TextView) rootView.findViewById(R.id.tv_risk_type);
        tvLevel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        levelIndex = getResources().getStringArray(R.array.risk_evaluation_level);//"1","2"
        tvRiskTypeS = getResources().getStringArray(R.array.risk_evaluation_leve_type);
        tvRiskTitles = getResources().getStringArray(R.array.risk_evaluation_leve_title);//"1","2"
        descs = getResources().getStringArray(R.array.risk_evaluation_leve_des);//""
        TypedArray ar = getResources().obtainTypedArray(R.array.risk_evaluation_leve_draw);
        riskDraws = new int[ar.length()];
        for (int i=0;i<ar.length();i++){
            riskDraws[i] = ar.getResourceId(i,0);
        }
        ar.recycle();

    }

    public void initViewDate(String level){
        int index = getIndexByLevel(level);
        setView(index);
        setRiskLevel(index);
    }

    private void setRiskLevel(int index){
        tvRiskType.setText(tvRiskTypeS[index]);
        if (requestCode == DataUtils.RISK_ASSESS_ACCOUNT) {
            btnOk.setText(getString(R.string.boc_risk_assess_again));
            riskLevel.setVisibility(View.VISIBLE);
        } else if (requestCode == DataUtils.RISK_ASSESS_CHOICE) {
            riskLevel.setVisibility(View.INVISIBLE);
            tvLevel.setClickable(false);
            btnOk.setText(getString(R.string.boc_risk_assess_accomplish));
        } else {
            btnOk.setText(getString(R.string.boc_risk_assess_again));
            riskLevel.setVisibility(View.VISIBLE);
        }
    }



    private void setView(int index){
        tvRiskTitle.setText(tvRiskTitles[index]);
        if (index < riskDraws.length){
            ivLevel.setImageResource(riskDraws[index]);
        }
        if (index < descs.length){
            desc = descs[index];
        }
        getClickSpan();
    }

    /**
     * 对投资者的描述展开点击事件的处理
     */
    private void getClickSpan() {
        if (StringUtil.isNullOrEmpty(desc)){
            return;
        }
        if (desc.length() <= 70){
            tvDetail.setText(desc);
            return;
        }
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件
                getClickSpan2();
            }
        };
        String s = desc.substring(0, 70) + " " + "展开";
        SpannableStringBuilder style = new SpannableStringBuilder(s);
        style.setSpan(new RiskEvaluationClickable(l), s.length() - 2, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        style.setSpan(new RiskEvaluationClickable(l), s.length() - 2, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.boc_main_button_color))
                , s.length() - 2, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDetail.setText(style);
        tvDetail.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private String getString(int id){
        return mContext.getString(id);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.tv_level == id) {
            if (riskEvaluationListener != null){
                riskEvaluationListener.riskEvaluationSubmit();
            }
            return;

        }
        if (R.id.btn_ok == id) {
            if (riskEvaluationListener != null){
                riskEvaluationListener.riskEvaluationConfirm();
            }
            return;
        }

    }

    public void setRiskEvaluationListener(RiskEvaluationListener riskEvaluationListener) {
        this.riskEvaluationListener = riskEvaluationListener;
    }

    public interface RiskEvaluationListener{
        void riskEvaluationSubmit();//默认风险评估提交
        void riskEvaluationConfirm();//进行Fragment跳转

    }

    private int getIndexByLevel(String level){
        if (StringUtil.isNullOrEmpty(level)){
            return levelIndex.length -1;
        }
        for (int i=0;i<levelIndex.length;i++){
            if (level.equals(levelIndex[i])){
                return i;
            }
        }
        return levelIndex.length -1;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

}
