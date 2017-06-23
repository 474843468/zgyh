package com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ShareInfoFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.HomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.model.PhoneOperateModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.ui.TransferRecordFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransMoreFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 手机号转账结果页面
 * Created by wangtong on 2016/7/28.
 */
public class PhoneOperateFragment extends BussFragment {
    protected BaseOperationResultView paymentResult;
    protected ImageView btnBack;
    protected TextView fragmentTitle;
    private View rootView;
    private PhoneOperateModel model;
    private String isBindAccount;
    private boolean isChange;
    // 微信分享
    public static final int SHARE = 0;
    // 再汇一笔
    public static final int RETRANS = 1;
    // 转账记录
    public static final int TRANSRECORD = 2;
    private String[] key;
    private String[] value;
    String commisionNum;
    String payeeAccountNum;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_phone_operate, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        paymentResult = (BaseOperationResultView) rootView.findViewById(R.id.payment_result);
        btnBack = (ImageView) rootView.findViewById(R.id.btn_back);
        fragmentTitle = (TextView) rootView.findViewById(R.id.fragment_title);
    }

    @Override
    public void initData() {
        super.initData();
        fragmentTitle.setText("操作结果");
        model = getArguments().getParcelable("model");
        isBindAccount = model.getIsBundAccount();
        if ("0".equals(isBindAccount)) {
            isChange = false;
        } else {
            isChange = true;
        }
        updateViews();
        findFragment(TransMoreFragment.class).setRefresh(true);
    }


    private void updateViews() {
        if (isChange) {
            //转账成功
            paymentResult.updateHead(OperationResultHead.Status.SUCCESS,
                    MoneyUtils.transMoneyFormat(model.getTrfAmount(), model.getReminCurrency()) + "元交易成功");

            if ("0".equals(model.getCommisionCharge())) {
                commisionNum = "0.00";
                return;
            } else if (StringUtils.isEmpty(model.getCommisionCharge())) {
                commisionNum = "0.00";
            } else {
                commisionNum = MoneyUtils.transMoneyFormat(model.getCommisionCharge(), model.getReminCurrency());
            }
            payeeAccountNum = NumberUtils.formatCardNumber(model.getPayeeAccount());
            paymentResult.addHeadInfo("优惠后费用", commisionNum);
            paymentResult.addHeadInfo("交易序号", model.getTransNum() + "");

        } else {
            //短信转账成功
            paymentResult.updateHead(OperationResultHead.Status.SUCCESS, model.getTrfAmount() + "元提交成功");
            commisionNum = getResources().getString(R.string.boc_trans_without_account_notice);
            payeeAccountNum = getResources().getString(R.string.boc_trans_without_account_show);
            paymentResult.addHeadInfo("优惠后费用", commisionNum);
        }

        List<String> keyList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();

        keyList.add("收款人名称");
        valueList.add(model.getPayeeName());

        keyList.add("收款人手机号");
        valueList.add(NumberUtils.formatMobileNumber(model.getPayeeMobel()));

        if (!StringUtils.isEmpty(model.getTips())) {
            keyList.add("附言");
            valueList.add(model.getTips());
        }
        keyList.add("付款账户");
        valueList.add(NumberUtils.formatCardNumber(model.getFromAccoutnNum()));


        key = new String[keyList.size()];
        value = new String[valueList.size()];

        for (int i = 0; i < keyList.size(); i++) {
            key[i] = keyList.get(i);
        }

        for (int i = 0; i < valueList.size(); i++) {
            value[i] = valueList.get(i);
        }

        paymentResult.addDetailRow("收款人名称", model.getPayeeName());
        paymentResult.addDetailRow("收款人手机号", NumberUtils.formatMobileNumber(model.getPayeeMobel()));
        if (!StringUtils.isEmpty(model.getPayeeLbk())) {
            paymentResult.addDetailRow("所属地区", PublicCodeUtils.getTransferIbk(getContext(), model.getPayeeLbk()));
        }
        if (!StringUtils.isEmpty(model.getTips())) {
            paymentResult.addDetailRow("附言", model.getTips());
        }
        paymentResult.addDetailRow("付款账户", NumberUtils.formatCardNumber(model.getFromAccoutnNum()));
        paymentResult.addDetailRow("付款账户\n交易后余额", MoneyUtils.transMoneyFormat(model.getReminAmount(), model.getReminCurrency()));

        //添加分享字段
        paymentResult.addContentItem(getResources().getString(R.string.boc_common_notify), SHARE);
        paymentResult.addContentItem(getResources().getString(R.string.boc_common_again_retrans), RETRANS);
        paymentResult.addContentItem(getResources().getString(R.string.boc_common_trans_record), TRANSRECORD);

    }

    @Override
    public boolean onBack() {
        popToAndReInit(PhoneEditPageFragment.class);
        return false;
    }

    @Override
    public void setListener() {
        //点击底部按钮
        paymentResult.setgoHomeOnclick(new OperationResultBottom.HomeBtnCallback() {
            @Override
            public void onHomeBack() {
                ActivityManager.getAppManager().finishActivity();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popToAndReInit(PhoneEditPageFragment.class);
            }
        });

        paymentResult.setOnclick(new BaseOperationResultView.BtnCallback() {
            @Override
            public void onClickListener(View v) {
                int id = v.getId();
                String msg = "";
                if (id == SHARE) {
                    if (isChange) {
                        msg = "转账交易成功";
                        String[] moneyInfo = {"转账金额", model.getTrfAmount() + "元"};
                        ShareInfoFragment fragment = ShareInfoFragment.newInstance(msg, key, value, moneyInfo);
                        start(fragment);
                    } else {
                        String accName = StringUtils.isEmpty(model.getFromAccoutnName()) ? "" : model.getFromAccoutnName();
                        msg = accName + "已向您发起手机号转账，您尚未设置默认收款账户，请您登录中行网银或手机银行进行设置。";
                        String[] moneyInfo = {"转账金额", model.getTrfAmount() + "元"};
                        ShareInfoFragment fragment = ShareInfoFragment.newInstance(msg, key, value, moneyInfo);
                        fragment.setOther(true);//非转账不显示印章
                        start(fragment);
                    }

                } else if (id == RETRANS) {
                    popToAndReInit(PhoneEditPageFragment.class);
                } else if (id == TRANSRECORD) {
                    TransferRecordFragment transferRecordFragment = new TransferRecordFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransferRecordFragment.TRANS_FROM_KEY, TransferRecordFragment.TRANS_FROM_PHONE);
                    transferRecordFragment.setArguments(bundle);
                    start(transferRecordFragment);
                }

            }
        });

    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(PhoneEditPageFragment.class);
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }
}
