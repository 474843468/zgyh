package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.boc.bocsoft.mobile.bocmobile.buss.MenuFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.HomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.PaymentOperatorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.ui.PhoneEditPageFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.ui.TransferRecordFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransMoreFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.widget.listview.ListAdapterHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 收付款管理结果页面
 * Created by wangtong on 2016/6/30.
 */
public class PaymentOperateFragment extends BussFragment {

    protected BaseOperationResultView paymentResult;
    private View rootView;
    private PaymentOperatorModel model;
    private Button btnHome;
    private TextView fragmentTitle;
    private ImageView btn_back;
    private String[] key;
    private String[] value;
    // 微信分享
    public static final int SHARE = 0;
    // 转账记录
    public static final int PAYEEMANGER = 1;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_payment_operate, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        paymentResult = (BaseOperationResultView) rootView.findViewById(R.id.payment_result);
        fragmentTitle = (TextView) rootView.findViewById(R.id.fragment_title);
        btn_back = (ImageView) rootView.findViewById(R.id.btn_back);
        btnHome = (Button) rootView.findViewById(R.id.btn_back_home);

    }

    @Override
    public void initData() {
        super.initData();
        fragmentTitle.setText(getString(R.string.trans_pay_result));
        model = getArguments().getParcelable("model");
        updateViews();
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

        paymentResult.setOnclick(new BaseOperationResultView.BtnCallback() {
            @Override
            public void onClickListener(View v) {
                int id = v.getId();
                String msg = "付款成功！";
                if (id == SHARE) {
                    popTo(MenuFragment.class, false);
                    String[] moneyInfo = {"付款金额", model.getTransAmount() + "元"};
                    ShareInfoFragment fragment = ShareInfoFragment.newInstance(msg, key, value, moneyInfo);
                    start(fragment);
                } else if (id == PAYEEMANGER) {
                    popToAndReInit(OrderListFragment.class);
                }

            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getAppManager().finishActivity();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popToAndReInit(OrderListFragment.class);
            }
        });

    }

    @Override
    public boolean onBack() {
        popToAndReInit(OrderListFragment.class);
        return false;
    }

    private void updateViews() {
        paymentResult.addHeadInfo("优惠后费用", MoneyUtils.transMoneyFormat(model.getDiscountAmount(), model.getTrfCur()));
        paymentResult.addHeadInfo("交易序号", model.getTransferNum());
        //更新操作结果信息
        paymentResult.updateHead(OperationResultHead.Status.SUCCESS, model.getOperatorTitle());
        // 传入分享页面字段
        List<String> keyList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();

        keyList.add("收款人名称");
        valueList.add(model.getPayeeName());

        keyList.add("收款账号");
        valueList.add(model.getPayeeAccNum());
        keyList.add("收款人手机号");
        valueList.add(NumberUtils.formatMobileNumber(model.getPayeeMobel()));

        keyList.add("指令序号");
        valueList.add(model.getNotifyId());
        if (!StringUtils.isEmpty(model.getTips())) {
            keyList.add("附言");
            valueList.add(model.getTips());
        }
        keyList.add("付款人名称");
        valueList.add(model.getPayerName());
        keyList.add("付款账户");
        valueList.add(NumberUtils.formatCardNumber(model.getPayerAccount()));
        keyList.add("交易序号");
        valueList.add(model.getTransferNum());

        key = new String[keyList.size()];
        value = new String[valueList.size()];

        for (int i = 0; i < keyList.size(); i++) {
            key[i] = keyList.get(i);
        }

        for (int i = 0; i < valueList.size(); i++) {
            value[i] = valueList.get(i);
        }

        // 结果页面字段
        paymentResult.addDetailRow("收款人名称", model.getPayeeName());
        paymentResult.addDetailRow("收款人账号", model.getPayeeAccNum());
        paymentResult.addDetailRow("收款人手机号", NumberUtils.formatMobileNumber(model.getPayeeMobel()));
        paymentResult.addDetailRow("指令序号", model.getNotifyId());
        if (!StringUtils.isEmpty(model.getTips())) {
            paymentResult.addDetailRow("附言", model.getTips());
        }
        paymentResult.addDetailRow("付款账户", NumberUtils.formatCardNumber(model.getPayerAccount()));
        paymentResult.addDetailRow("付款账户\n交易后余额", MoneyUtils.transMoneyFormat(model.getRemainAmount(), model.getTrfCur()));

        paymentResult.addContentItem(getResources().getString(R.string.boc_common_notify), SHARE);
        paymentResult.addContentItem(getResources().getString(R.string.boc_common_pay_payee_manage), PAYEEMANGER);
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }
}
