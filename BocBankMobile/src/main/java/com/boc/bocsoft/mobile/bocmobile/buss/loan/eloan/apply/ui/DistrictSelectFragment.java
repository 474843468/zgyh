package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.DistrictSelect.AddressSelect;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.DistrictRes;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址选择组件fragment
 * Created by xintong on 2016/6/3.
 * <p/>
 * Modified by liuweidong on 2016/8/25
 */
public class DistrictSelectFragment extends BussFragment {

    private static final String TAG = "DistrictSelectFragment";

    private AddressSelect mRootView;

    public static final int RequestCode = 333;
    public static final int ResultCode = 444;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = new AddressSelect(getActivity(), this);
        // 设置选项卡个数
        mRootView.setMaxTabsNum(3);
        return mRootView;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        if (getArguments().getBoolean("First")) {// 首次进入
            ArrayList<DistrictRes> zoneList = getArguments().getParcelableArrayList("ZoneList");
            List<String> provinceList = new ArrayList<String>();
            for (int i = 0; i < zoneList.size(); i++) {
                provinceList.add(zoneList.get(i).getPrivName());
            }
            //添加省列表
            mRootView.addDataList(provinceList);
            mRootView.setInitResult(zoneList);
            //初始化
            mRootView.addInitLabel();
        } else {
        }
    }

    @Override
    public void setListener() {
    }

    public void setDistrictInfo(String districtInfo, String zoneCode) {
        Bundle bundle = new Bundle();
        bundle.putString("districtInfo", districtInfo);
        bundle.putString("zoneCode", zoneCode);
        setFramgentResult(ResultCode, bundle);
        pop();
    }

    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_apply_selectDistrict);
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
    public void onDestroy() {
        super.onDestroy();

    }
}
