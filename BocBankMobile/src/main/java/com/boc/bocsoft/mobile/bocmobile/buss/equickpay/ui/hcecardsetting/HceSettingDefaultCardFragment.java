package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardsetting;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.adapter.HceSettingDefaultCardAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.hcecardlist.HceCardListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util.HceConstants;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util.HceUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.framework.widget.listview.divider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangle on 2016/12/26.
 * 描述:设置默认支付卡列表界面
 */
public class HceSettingDefaultCardFragment extends BussFragment implements HceSettingDefaultCardAdapter.OnItemClickListener {

    private View mRootView;
    private RecyclerView mRecyclerView;
    private HceSettingDefaultCardAdapter mAdapter;

    public static HceSettingDefaultCardFragment newInstance() {
        return new HceSettingDefaultCardFragment();
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.boc_fragment_hce_setting_default_card, null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.hce_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(getItemDecoration());

        mAdapter = new HceSettingDefaultCardAdapter(getData());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private RecyclerView.ItemDecoration getItemDecoration() {
        return new HorizontalDividerItemDecoration
                .Builder(mContext)
                .colorResId(R.color.boc_common_bg_color)
                .size(PublicUtils.dip2px(mContext,10))
                .build();
    }

    @Override
    public void onItemClick(int position) {
        ToastUtils.show("position = " + position);
        // TODO: 2016/12/27 设置默认支付卡
        String cardBrand = null ;
        boolean isSuccess = setDefaultPayCard(cardBrand);
        if (isSuccess) {
            ToastUtils.show("设置成功");
        } else {
            ToastUtils.show("设置失败");
        }
    }

    // 设置默认支付卡
    private boolean setDefaultPayCard(String cardBrand) {
        String result =  HceUtil.setHceDefaultApp(mContext,HceUtil.getApplicationType(cardBrand));
        return HceConstants.HCE_SUCCEED.equals(result);
    }

    @Override
    protected String getTitleValue() {
        return "设置默认支付";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }


    public List<HceCardListQueryViewModel> getData() {
        List<HceCardListQueryViewModel> datas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HceCardListQueryViewModel hceCardListQueryViewModel = new HceCardListQueryViewModel();
            hceCardListQueryViewModel.setMasterCardNo("562654");
            hceCardListQueryViewModel.setSlaveCardNo("621111111111");
            hceCardListQueryViewModel.setCardType("01");
            datas.add(hceCardListQueryViewModel);
        }
        return datas;
    }

}
