package com.boc.bocsoft.mobile.bocmobile.buss.transfer.phonetransferquery.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowGroup;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * 手机号转账查询详情
 * <p/>
 * Created by liuweidong on 2016/6/23.
 */
public class PhoneTransferQueryDetailsFragment extends BussFragment {
    private View rootView;
    private DetailTableHead detailTableHead;
    private DetailContentView detailContentView;
    private DetailTableRowGroup detailTableRowGroup;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_account_transdetail_info, null);
        return rootView;
    }

    @Override
    public void initView() {
        detailTableHead = (DetailTableHead) rootView.findViewById(R.id.head_view);
        detailContentView = (DetailContentView) rootView.findViewById(R.id.body_view);
        detailTableRowGroup = (DetailTableRowGroup) rootView.findViewById(R.id.bottom_view);

        detailTableRowGroup.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        detailTableHead.updateData("转账金额（人民币元）", MoneyUtils.transMoneyFormat("123", "001"));
        detailTableHead.addDetail("转入账户", "张思 4637 ****** 3675");

        detailContentView.addDetailRow("指令流水号", "45653456");
        detailContentView.addDetailRow("转出账户", NumberUtils.formatCardNumber("3232232211234567"));
        detailContentView.addDetailRow("收款人姓名", "张三");
        detailContentView.addDetailRow("收款人电话", NumberUtils.formatMobileNumber("18711112222"));
        detailContentView.addDetailRow("初次提交日期", "2013/03/04");
        detailContentView.addDetailRow("处理日期", "2013/03/04");
        detailContentView.addDetailRow("状态", "交易成功");
        detailContentView.addDetailRow("失败原因", "");
        detailContentView.addDetailRow("备注", "转账");
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_phone_query_details_title);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }
}
