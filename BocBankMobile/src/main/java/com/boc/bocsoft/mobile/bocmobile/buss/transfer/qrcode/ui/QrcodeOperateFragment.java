package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ShareInfoFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.QrcodeOperateModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.ui.TransferRecordFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransMoreFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * 二维码转账结果页面
 * Created by wangtong on 2016/7/28.
 */
public class QrcodeOperateFragment extends BussFragment {
    protected BaseOperationResultView paymentResult;
    private View rootView;
    private QrcodeOperateModel model;
    protected TextView fragmentTitle;
    // 微信分享
    public static final int SHARE = 0;
    // 再汇一笔
    public static final int RETRANS = 1;
    // 转账记录
    public static final int TRANSRECORD = 2;
    private String[] key;
    private String[] value;
    private ImageView leftBack;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_phone_operate, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        paymentResult = (BaseOperationResultView) rootView.findViewById(R.id.payment_result);
        leftBack = (ImageView) rootView.findViewById(R.id.btn_back);
        fragmentTitle = (TextView) rootView.findViewById(R.id.fragment_title);
    }

    @Override
    public void initData() {
        super.initData();
        fragmentTitle.setText("操作结果");
        model = getArguments().getParcelable("model");
        updateViews();
        findFragment(TransMoreFragment.class).setRefresh(true);
    }

    @Override
    public boolean onBack() {
        popToAndReInit(QrcodeTransFragment.class);
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

        paymentResult.setOnclick(new BaseOperationResultView.BtnCallback() {
            @Override
            public void onClickListener(View v) {
                int id = v.getId();
                String msg = "转账交易成功";
                if (id == SHARE) {
                    String[] moneyInfo = {"转账金额", model.getTrfAmount() + "元"};
                    ShareInfoFragment fragment = ShareInfoFragment.newInstance(msg, key, value, moneyInfo);
                    start(fragment);
                } else if (id == RETRANS) {
                    startQrcodeScan();
                } else if (id == TRANSRECORD) {
                    TransferRecordFragment transferRecordFragment = new TransferRecordFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransferRecordFragment.TRANS_FROM_KEY, TransferRecordFragment.TRANS_FROM_QRCODE);
                    transferRecordFragment.setArguments(bundle);
                    start(transferRecordFragment);
                }
            }
        });

        leftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrcodeScan();
            }
        });
    }

    private void startQrcodeScan() {
        popTo(TransMoreFragment.class, false);
        start(new QrcodeScanFragment());
    }

    private void updateViews() {

        List<String> keyList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();

        keyList.add("交易日期");
        LocalDateTime currentSystemDate = ApplicationContext.getInstance().getCurrentSystemDate();
        valueList.add(currentSystemDate.getYear() + "/" + currentSystemDate.getMonthValue() + "/" + currentSystemDate.getDayOfMonth());

        keyList.add("收款人名称");
        valueList.add(model.getPayeeName());

        keyList.add("收款账号");
        valueList.add(NumberUtils.formatCardNumber2(model.getPayeeAccount()));

//        keyList.add("收款人手机号");
//        valueList.add(NumberUtils.formatMobileNumber(model.getPayeeMobel()));

        keyList.add("付款人名称");
        valueList.add(ApplicationContext.getInstance().getUser().getCustomerName());

        keyList.add("付款账号");
        valueList.add(NumberUtils.formatCardNumber(model.getFromAccountNum()));

        if (!StringUtils.isEmpty(model.getTips())) {
            keyList.add("附言");
            valueList.add(model.getTips());
        }

        keyList.add("交易序号");
        valueList.add(model.getTransactionId()+"");

        key = new String[keyList.size()];
        value = new String[valueList.size()];

        for (int i = 0; i < keyList.size(); i++) {
            key[i] = keyList.get(i);
        }

        for (int i = 0; i < valueList.size(); i++) {
            value[i] = valueList.get(i);
        }


        String commissionCharge = "";
        String charge = model.getFinalCommissionCharge();
        if ("0".equals(charge)) {
            commissionCharge = "0.00";
        } else if (StringUtils.isEmpty(charge)) {
            commissionCharge = "0.00";
        } else {
            commissionCharge = MoneyUtils.transMoneyFormat(model.getFinalCommissionCharge(), model.getRemainCurrency());
        }

        paymentResult.addHeadInfo("优惠后费用", commissionCharge);
        paymentResult.addHeadInfo("交易序号", model.getTransactionId() + "");

        //更新操作结果信息
        paymentResult.updateHead(OperationResultHead.Status.SUCCESS,
                MoneyUtils.transMoneyFormat(model.getTrfAmount(), model.getRemainCurrency()) + "元交易成功");
        //添加操作结果详情
        paymentResult.addDetailRow("收款人名称", model.getPayeeName());
        paymentResult.addDetailRow("收款账号", model.getPayeeAccount());
        String ibkNum = model.getFromIbkNum();
//        if (!StringUtils.isEmpty((ibkNum))) {
//            paymentResult.addDetailRow("所属地区", PublicCodeUtils.getTransferIbk(mContext, model.getFromIbkNum()));
//        }

        String mobile = model.getPayeeMobel();
        if (!StringUtils.isEmpty(mobile)) {
            paymentResult.addDetailRow("收款人手机号", NumberUtils.formatMobileNumber(model.getPayeeMobel()));
        }
        if (!StringUtils.isEmpty(model.getTips())) {
            paymentResult.addDetailRow("附言", model.getTips());
        }
        paymentResult.addDetailRow("付款账户", NumberUtils.formatCardNumber(model.getFromAccountNum()));
        paymentResult.addDetailRow("付款账户\n交易后余额",
                MoneyUtils.transMoneyFormat(model.getRemainAmount().toString(), model.getRemainCurrency()));

        //添加分享字段
        paymentResult.addContentItem(getResources().getString(R.string.boc_common_notify), SHARE);
        paymentResult.addContentItem(getResources().getString(R.string.boc_common_again_retrans), RETRANS);
        paymentResult.addContentItem(getResources().getString(R.string.boc_common_trans_record), TRANSRECORD);

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public boolean onBackPress() {
        startQrcodeScan();
        return true;
    }
}
