package com.boc.bocsoft.mobile.bocmobile.buss.login.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.common.utils.ButtonClickLock;

/**
 * Created by feibin on 16/8/15.
 */
public class BindingDeviceResultFragment extends LoginBaseFragment implements View.OnClickListener {

    protected View rootView;
    protected Button btnConfirm;
    /**
     * 防重锁
     */
    private String clickOprLock = "click_more";
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_fragment_binding_device_result,
                null);

        return rootView;
    }

    @Override
    public void initView() {
        btnConfirm = (Button) rootView.findViewById(R.id.btn_confirm);
    }

    /**
     * 数据初始化
     */
    @Override
    public void initData() {
        super.initData();
        ButtonClickLock.getLock(clickOprLock).lockDuration = 1000;

    }

    @Override
    public void setListener() {
        btnConfirm.setOnClickListener(BindingDeviceResultFragment.this);
    }

    @Override
    public void onClick(View view) {

        //点击“进入手机银行”按钮
        if (view.getId() == R.id.btn_confirm) {
            // 防暴力点击下一步按钮
            if (!ButtonClickLock.isCanClick(clickOprLock)) {
                return;
            }
            showLoadingDialog(false);
            mLoginBasePresenter.queryGlobalMsgList();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButtonClickLock.removeLock(clickOprLock);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "操作结果";
    }

    @Override
    protected boolean isDisplayLeftIcon() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }
    @Override
    public boolean onBackPress() {
        return true;
    }
}
