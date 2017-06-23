package com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.draw.ui;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.ui.ContractContentFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.AccoutFragment;

/**
 * Created by qiangchen on 2016/8/13.
 */
public class ReDrawFragment extends BussFragment implements View.OnClickListener {

    //页面根视图
    private View mRoot;
    //可用额度
    private TextView availableCredit;
    //提款金额
    private EditMoneyInputWidget drawAmount;
    //收款账户
    private EditChoiceWidget receiptAccount;
    //合同
    private ImageButton checkbox;
    private TextView agreement;
    //下一步按钮
    private Button next;
    private static String CONTRACT;
    private MClickableSpan clickableSpanFir;
    //SpannableString对象
    private SpannableString spannableStringFir;
    private ContractContentFragment contractContentFragment;
    private String conversationId;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot=mInflater.inflate(R.layout.fragment_eloan_draw,null);
        return  mRoot;
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
        return getString(R.string.boc_eloan_draw_pagename);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void initView() {
        //组件初始化
        availableCredit = (TextView) mRoot.findViewById(R.id.availableCredit);
        drawAmount = (EditMoneyInputWidget) mRoot.findViewById(R.id.drawAmount);
        receiptAccount = (EditChoiceWidget) mRoot.findViewById(R.id.receiptAccount);
        checkbox = (ImageButton) mRoot.findViewById(R.id.checkbox);
        agreement = (TextView) mRoot.findViewById(R.id.agreement);
        next = (Button) mRoot.findViewById(R.id.next);

        //初始化合同展示页面
        contractContentFragment = new ContractContentFragment();
    }

    @Override
    public void initData() {
        //第一次提款，页面初始值设置
//        availableCredit.setText("可用额度：人民币元"+ MoneyUtils.transMoneyFormat(mEloanDrawModel.getAvailableAvl(),"001"));

        drawAmount.getContentMoneyEditText().setHint(getString(R.string.boc_eloan_draw_input));
        receiptAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));

        CONTRACT = "《个人循环贷款合同》";
        //为组件添加数据
        spannableStringFir = new SpannableString(CONTRACT);
        clickableSpanFir = new MClickableSpan(CONTRACT, getContext());
        clickableSpanFir.setColor(Color.BLUE);
        spannableStringFir.setSpan(clickableSpanFir, 0,
                CONTRACT.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        agreement.setText("本人已仔细阅读并理解");
        agreement.append(spannableStringFir);
        agreement.append(",完全同意和接受协议全部条款和内容，愿意履行和承担该协议书中约定的权利和义务。");
        agreement.setMovementMethod(LinkMovementMethod.getInstance());
        agreement.setLongClickable(false);
    }

    @Override
    public void setListener() {
        checkbox.setOnClickListener(this);

        receiptAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccoutFragment accoutFragment = new AccoutFragment();
                accoutFragment.setAccountType(AccoutFragment.AccountType.SELECT_COLLECTIONACCOUNT);
                accoutFragment.setConversationId(conversationId);
                startForResult(accoutFragment, AccoutFragment.RequestCode);
                      }
            });

//        clickableSpanFir.setListener(new MClickableSpan.OnClickSpanListener() {
//        @Override
//        public void onClick() {
//            start(contractContentFragment);
//            }
//        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                judge();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
