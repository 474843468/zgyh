package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardsetting;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.hcecardlist.HceCardListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcesecuritysetting.HceSceneSelectFragment;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangle on 2016/12/26.
 * 描述:Hce更多界面
 */
public class HceMoreFragment extends BussFragment implements View.OnClickListener {

    public static String CARD_LIST_KEY = "hce_card_list";
    private View mRootView;
    private EditChoiceWidget mSettingDefaultCard;
    private EditChoiceWidget mSettingLockPay;
    private ArrayList<HceCardListQueryViewModel> mCardModelList;

    public static HceMoreFragment newInstance(List<HceCardListQueryViewModel> cardModelList){
        HceMoreFragment moreFragment = new HceMoreFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(CARD_LIST_KEY, (ArrayList) cardModelList);
        moreFragment.setArguments(bundle);
        return moreFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardModelList = getArguments().getParcelableArrayList(CARD_LIST_KEY);
        PublicUtils.checkNotNull(mCardModelList, "mCardModelList == null");
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.boc_fragment_hce_more, null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        mSettingDefaultCard = (EditChoiceWidget) mRootView.findViewById(R.id.setting_default_card);
        mSettingLockPay = (EditChoiceWidget) mRootView.findViewById(R.id.setting_lock_pay);
    }

    @Override
    public void setListener() {
        super.setListener();
        mSettingDefaultCard.setOnClickListener(this);
        mSettingLockPay.setOnClickListener(this);
    }

    @Override
    protected String getTitleValue() {
        return "更多";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.setting_default_card) {
            // 选择默认支付卡
            start(HceSettingDefaultCardFragment.newInstance());
        } else if (id == R.id.setting_lock_pay) {
            // 选择支付场景
            start(HceSceneSelectFragment.newInstance());
        } else if (id == R.id.hce_user_guide) {
            // 用户指南
            // TODO: 2017/1/4  
            ToastUtils.show("user guide");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCardModelList != null) {
            mCardModelList.clear();
            mCardModelList = null;
        }
    }
}
