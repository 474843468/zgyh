package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.EditResultModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

/**
 * 操作结果页面
 * Created by wangtong on 2016/6/17.
 */
public class ResultFragment extends BussFragment {

    protected View rootView;
    //操作结果页面
    protected BaseOperationResultView operateResult;
    //数据模型
    private EditResultModel dataModel;

    protected TextView fragmentTitle;

    protected ImageView btnBack;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_edit_result, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        fragmentTitle = (TextView) rootView.findViewById(R.id.fragment_title);
        fragmentTitle.setText("操作结果");
        dataModel = getArguments().getParcelable(SmsNotifyEditFragment.KEY_MODEL);
        operateResult = (BaseOperationResultView) rootView.findViewById(R.id.operate_result);
        btnBack = (ImageView) rootView.findViewById(R.id.btn_back);
        operateResult.isShowBottomInfo(false);

        //更新操作结果信息
        operateResult.updateHead(OperationResultHead.Status.SUCCESS, dataModel.getHeadName());
        //添加操作结果详情
        operateResult.addDetailRow("开通账户", NumberUtils.formatCardNumber(dataModel.getSignAccount()));
        operateResult.addDetailRow("客户姓名", dataModel.getUserName());
        if (!TextUtils.isEmpty(dataModel.getPhoneNum())) {
            operateResult.addDetailRow("接收短信\n手机号码", NumberUtils.formatMobileNumber(dataModel.getPhoneNum()));
        }
        operateResult.addDetailRow("金额范围", dataModel.getRangeAmount());
        operateResult.addDetailRow("缴费账户", NumberUtils.formatCardNumber(dataModel.getFeeAccount()));
        if (!dataModel.getFeeStandard().equals("0")) {
            operateResult.addDetailRow("收费标准", getString(R.string.boc_account_fee_type, dataModel.getFeeStandard()));
        }
    }

    @Override
    public void setListener() {
        super.setListener();
        //点击底部按钮
        operateResult.setgoHomeOnclick(new OperationResultBottom.HomeBtnCallback() {
            @Override
            public void onHomeBack() {
                ActivityManager.getAppManager().finishActivity();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshMainFragment();
            }
        });
    }

    private void refreshMainFragment() {
        findFragment(AccSmsNotifyHomeFragment.class).refreshSmsList();
        popTo(AccSmsNotifyHomeFragment.class, false);
    }

    @Override
    public boolean onBack() {
        refreshMainFragment();
        return true;
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }
}
