package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanCityBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanProvinceBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import java.util.List;

/**
 * Created by XieDu on 2016/7/19.
 */
public class LoanDistrictSelectContract {
    public interface View {
        void onLoadProvincesSuccess(List<OnLineLoanProvinceBean> provinces);
        void onLoadProvincesFail(BiiResultErrorException biiResultErrorException);

        void onLoadCitiesSuccess(String provinceCode, List<OnLineLoanCityBean> cities);
        void onLoadCitiesFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter{
        /**
         * 查询支持的省份列表
         */
        void getOnlineLoanProvinces();

        /**
         * 查询支持的省份列表
         */
        void getOnlineLoanCities(String provinceCode);
    }
}
