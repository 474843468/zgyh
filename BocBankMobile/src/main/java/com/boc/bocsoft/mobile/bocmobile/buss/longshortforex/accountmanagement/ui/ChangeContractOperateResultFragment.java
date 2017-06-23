package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailDetailQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGFilterDebitCardViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.LinkedHashMap;

/**
 * Fragment：双向宝-账户管理-变更资金账户操作结果页面
 * Created by zhx on 2016/12/14
 */
public class ChangeContractOperateResultFragment extends MvpBussFragment implements BaseResultView.HomeBackListener {
    private View mView;
    private BaseResultView mResultView;
    private VFGBailListQueryViewModel.BailItemEntity mBailItem;
    private VFGFilterDebitCardViewModel.DebitCardEntity debitCardEntity;
    private VFGBailDetailQueryViewModel mVFGBailDetailQueryViewModel;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mView = mInflater.inflate(R.layout.boc_fragment_creditcard_payadvance_result, null);
        return mView;
    }

    @Override
    public void initView() {
        mResultView = (BaseResultView) mView.findViewById(R.id.borv_result_view);
    }

    // 顶部的详情
    private LinkedHashMap<String,String> getTopDetailMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("新资金账户", NumberUtils.formatCardNumber(debitCardEntity.getAccountNumber()));
        map.put("原资金账户", NumberUtils.formatCardNumber(mBailItem.getAccountNumber()));
        return map;
    }

    // 详情
    private LinkedHashMap<String,String> getDetailMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String marginAccountNo = mBailItem.getMarginAccountNo();
        int index = marginAccountNo.indexOf("00000");
        if (index == 0) {
            String substring = marginAccountNo.substring(5, marginAccountNo.length());
            map.put("保证金账号", NumberUtils.formatCardNumber(substring));
        } else {
            map.put("保证金账号", NumberUtils.formatCardNumber(marginAccountNo));
        }
        map.put("结算币种", PublicCodeUtils.getCurrency(mActivity, mBailItem.getSettleCurrency()));
        return map;
    }

    @Override
    public void initData() {
        mBailItem = getArguments().getParcelable("mBailItem");
        debitCardEntity = getArguments().getParcelable("mDebitCardEntity");
        mVFGBailDetailQueryViewModel = getArguments().getParcelable("mVFGBailDetailQueryViewModel");

        // 设置头部
        mResultView.addStatus(ResultHead.Status.SUCCESS, "变更资金账户成功");
        mResultView.addTitle("变更签约账户成功，在进行双向宝交易前，请重新登记交易账户。");
        mResultView.updateDetail(getString(R.string.boc_crcd_myinstallment_see_details));
        mResultView.addTopDetail(getTopDetailMap());
        mResultView.addDetail(getDetailMap());
        mResultView.addNeedItem("变更交易账户", 1);
    }

    @Override
    public void setListener() {
        // 设置“您可能需要”条目的点击事件
        mResultView.setNeedListener(new ResultBottom.OnClickListener() {
            @Override
            public void onClick(int id) {
                Toast.makeText(mActivity, "点击了您可能需要", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置"返回首页"按钮的点击事件
        mResultView.setOnHomeBackClick(this);
    }

    @Override
    protected String getTitleValue() {
        return "操作结果";
    }

    @Override
    public boolean onBackPress() {
        popToAndReInit(BailAccountDetailFragment.class);
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(BailAccountDetailFragment.class);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onHomeBack() {
        ActivityManager.getAppManager().finishActivity();
    }
}
