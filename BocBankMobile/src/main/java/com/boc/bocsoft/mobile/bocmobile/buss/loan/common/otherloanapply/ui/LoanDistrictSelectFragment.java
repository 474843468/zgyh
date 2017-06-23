package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.DistrictSelect.AddressSelectView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.DistrictSelect.IAddress;
import com.boc.bocsoft.mobile.bocmobile.base.widget.DistrictSelect.IDistrictSelectView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanCityBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanProvinceBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.presenter.LoanDistrictSelectPresenter;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import java.util.HashMap;
import java.util.List;

/**
 * 其他贷款申请地区选择
 * Created by XieDu on 2016/7/19.
 */
public class LoanDistrictSelectFragment
        extends MvpBussFragment<LoanDistrictSelectContract.Presenter>
        implements LoanDistrictSelectContract.View, IDistrictSelectView {

    private AddressSelectView rootView;
    private List<OnLineLoanProvinceBean> provinces;
    private HashMap<String, List<OnLineLoanCityBean>> cityMap;
    /**
     * 页面跳转数据传递
     */
    public static final String SELECT_CITY = "select_city";

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        //初始化地址选择组件
        rootView = new AddressSelectView(getActivity(), this);
        //设置选项卡个数
        rootView.setMaxTabsNum(2);
        return rootView;
    }

    protected String getTitleValue() {
        return getString(R.string.boc_select_city);
    }

    @Override
    public void initData() {
        cityMap = new HashMap<>();
        rootView.initData();
    }

    /**
     * 是否显示标题栏
     */
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected LoanDistrictSelectContract.Presenter initPresenter() {
        return new LoanDistrictSelectPresenter(this);
    }

    @Override
    public void onLoadProvincesSuccess(List<OnLineLoanProvinceBean> provinces) {
        closeProgressDialog();
        if (!PublicUtils.isEmpty(provinces)) {
            this.provinces = provinces;
            rootView.obtainDistrictSuccess(provinces);
        } else {
            rootView.obtainDistrictFail(getString(R.string.boc_loan_other_no_apply_province));
        }
    }

    @Override
    public void onLoadProvincesFail(BiiResultErrorException biiResultErrorException) {
        rootView.obtainDistrictFail(getString(R.string.boc_loan_other_no_apply_province));
    }

    @Override
    public void onLoadCitiesSuccess(String provinceCode, List<OnLineLoanCityBean> cities) {
        closeProgressDialog();
        if (!PublicUtils.isEmpty(cities)) {
            cityMap.put(provinceCode, cities);
            rootView.obtainDistrictSuccess(cities);
        } else {
            rootView.obtainDistrictFail(getString(R.string.boc_loan_other_no_apply_city));
        }
    }

    @Override
    public void onLoadCitiesFail(BiiResultErrorException biiResultErrorException) {
        rootView.obtainDistrictFail(getString(R.string.boc_loan_other_no_apply_city));
    }

    @Override
    public void getFirstTabData() {
        showLoadingDialog();
        getPresenter().getOnlineLoanProvinces();
    }

    @Override
    public void getDistrictData(int tabPosition, String code) {
        if (tabPosition == 0) {
            List<OnLineLoanCityBean> cities = cityMap.get(code);
            if (PublicUtils.isEmpty(cities)) {
                showLoadingDialog();
                getPresenter().getOnlineLoanCities(code);
            } else {
                rootView.obtainDistrictSuccess(cities);
            }
        }
    }

    @Override
    public void setSelectDistrict(List<IAddress> list) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SELECT_CITY, (OnLineLoanCityBean) list.get(1));
        setFramgentResult(RESULT_OK, bundle);
        pop();
    }
}
