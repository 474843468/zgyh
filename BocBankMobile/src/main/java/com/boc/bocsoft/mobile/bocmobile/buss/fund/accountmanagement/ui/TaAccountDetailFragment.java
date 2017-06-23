package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DeatilsBottomTableButtom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailRow;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountCancelReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountCancelResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.presenter.TaAccountDetailPresenter;

/**
 * 基金-账户管理-TA账户详情页
 * Created by lyf7084 on 2016/11/30.
 */
public class TaAccountDetailFragment extends MvpBussFragment<TaAccountDetailContract.Presenter>
        implements TaAccountDetailContract.View {

    private static final String TAG = "TaAccountDetailFragment";

    private View rootView;
    private DetailRow taDetail_fundRegName;    // 注册登记机构item
    private DetailRow taDetail_TaAccountNo;    // 基金TA账户item
    private DetailRow taDetail_accountStatus;    // 账户状态item
    private DetailRow taDetail_isPosition;    // 是否有持仓
    private DetailRow taDetail_isTrans;    // 是否有在途交易
    private DeatilsBottomTableButtom btn_taDetailCancel;    // Button销户
    private DeatilsBottomTableButtom btn_taDetailDisassociate;    // Button取消关联
    private int flag = 0;    // 上送参数：flag=0，销户；flag=1，取消关联
    private TaAccountModel.TaAccountBean mTaAccountBean;
    private TaAccountCancelReqModel model;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_ta_account_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        taDetail_fundRegName = (DetailRow) rootView.findViewById(R.id.taDetail_fundRegName);
        taDetail_TaAccountNo = (DetailRow) rootView.findViewById(R.id.taDetail_TaAccountNo);
        taDetail_accountStatus = (DetailRow) rootView.findViewById(R.id.taDetail_accountStatus);
        taDetail_isPosition = (DetailRow) rootView.findViewById(R.id.taDetail_isPosition);
        taDetail_isTrans = (DetailRow) rootView.findViewById(R.id.taDetail_isTrans);
        btn_taDetailCancel = (DeatilsBottomTableButtom) rootView.findViewById(R.id.btn_taDetailCancel);

        btn_taDetailCancel.setButtonText(getString(R.string.boc_fund_account_cancel), getResources().getColor(R.color.boc_text_color_red));
        btn_taDetailDisassociate = (DeatilsBottomTableButtom) rootView.findViewById(R.id.btn_taDetailDisassociate);
        btn_taDetailDisassociate.setButtonText(getString(R.string.boc_fund_account_disassociate), getResources().getColor(R.color.boc_text_color_red));
    }

    @Override
    public void initData() {
//        // Condition1 添加固定数据
//        taDetail_fundRegName.updateData(getResources().getString(R.string.boc_fund_reg_name), "名称");
////        taDetail_fundRegName.showDividerLine(View.VISIBLE);
//        taDetail_TaAccountNo.updateData(getResources().getString(R.string.boc_fund_ta_account), "622078115671");
//        taDetail_accountStatus.updateData(getResources().getString(R.string.boc_account_detail_status), "正常");
//        taDetail_isPosition.updateData(getResources().getString(R.string.boc_fund_is_position), "否");
//        taDetail_isTrans.updateData(getResources().getString(R.string.boc_fund_is_trans), "是");
//        btn_taDetailCancel.updateText(getString(R.string.boc_fund_account_cancel));
//        btn_taDetailDisassociate.updateText(getString(R.string.boc_fund_account_disassociate));

        // Condition2 由上级页面传数据
        if (getArguments() != null) {
            mTaAccountBean = (TaAccountModel.TaAccountBean) getArguments().getSerializable("TaAccountSelectedItem");
            //详细信息
            String fundRegName = mTaAccountBean.getFundRegName();
            String taAccountNo = mTaAccountBean.getTaAccountNo();
            String accountStatus = mTaAccountBean.getAccountStatus();
            String isPosition = mTaAccountBean.getIsPosition();
            String isTrans = mTaAccountBean.getIsTrans();
            taDetail_fundRegName.updateData(getResources().getString(R.string.boc_fund_reg_name), fundRegName);
            taDetail_TaAccountNo.updateData(getResources().getString(R.string.boc_fund_ta_account), taAccountNo);
            taDetail_accountStatus.updateData(getResources().getString(R.string.boc_account_detail_status),
                    getAccountStatusDesc(accountStatus));
            taDetail_isPosition.updateData(getResources().getString(R.string.boc_fund_is_position),
                    "Y".equals(isPosition) ? getString(R.string.boc_yes) : getString(R.string.boc_no));
            taDetail_isTrans.updateData(getResources().getString(R.string.boc_fund_is_trans),
                    "Y".equals(isTrans) ? getString(R.string.boc_yes) : getString(R.string.boc_no));
        }
    }

    /**
     * 获取账户状态描述
     *
     * @param statusCode
     * @return
     */
    private String getAccountStatusDesc(String statusCode) {
        if ("01".equals(statusCode)) {
            return "销户处理中";
        } else if ("02".equals(statusCode)) {
            return "取消处理中";
        } else if ("03".equals(statusCode)) {
            return "冻结";
        } else {
            return "正常";
        }
    }


    @Override
    protected TaAccountDetailContract.Presenter initPresenter() {
        return new TaAccountDetailPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setListener() {
        btn_taDetailCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog(false);
                flag = 1;

                TaAccountCancelReqModel model = new TaAccountCancelReqModel();
                model.setTransType(String.valueOf(flag));
                model.setTaAccountNo(mTaAccountBean.getTaAccountNo());
                model.setFundRegCode(mTaAccountBean.getFundRegCode());
                getPresenter().taAccountCancel(model);
            }
        });
        btn_taDetailDisassociate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog(false);
                flag = 0;
                TaAccountCancelReqModel model = new TaAccountCancelReqModel();
                model.setTransType(String.valueOf(flag));
                model.setTaAccountNo(mTaAccountBean.getTaAccountNo());
                model.setFundRegCode(mTaAccountBean.getFundRegCode());
                getPresenter().taAccountCancel(model);
            }
        });
    }

    @Override
    public void setPresenter(TaAccountDetailContract.Presenter presenter) {

    }

    @Override
    public void onTaAccountCancelSuccess(TaAccountCancelResModel result) {
        Log.i(TAG, "TA账户注销提交成功！");
        closeProgressDialog();

        if (1 == flag) {
            Toast.makeText(getContext(), R.string.boc_fund_account_cancel_submit, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), R.string.boc_fund_account_disassociate_submit, Toast.LENGTH_LONG).show();
        }

        //TODO:考虑通知前一界面重新申请044 刷新TA账户LISTVIEW
        titleLeftIconClick();
    }

    @Override
    public void onTaAccountCancelFail(BiiResultErrorException biiResultErrorException) {
        Log.i(TAG, "TA账户注销提交失败！");
        closeProgressDialog();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fund_details);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }
}