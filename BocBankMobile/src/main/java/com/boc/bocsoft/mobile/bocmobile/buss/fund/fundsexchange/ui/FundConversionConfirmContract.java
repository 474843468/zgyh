package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.ui;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.ui.BillInstallmentsContract;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.model.FundConversionConfirmModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by taoyongzhen on 2016/12/14.
 */

public class FundConversionConfirmContract {

    public interface ConfirmConversionView extends BaseView<Presenter> {
        //基金转换结果失败
        void confirmConversionFail(BiiResultErrorException e);

        //基金转换结果成功
        void confirmConversionSuccess(FundConversionConfirmModel model);

        //基金转换结果(挂单)失败
        void confirmNightConversionFail(BiiResultErrorException e);

        //基金转换结果(挂单)成功
        void confirmNightConversionSuccess(FundConversionConfirmModel model);

        //基金公司信息查询失败
        void queryFundCompanyInfoFail(BiiResultErrorException e);

        //基金公司信息查询成功
        void queryFundCompanyInfoSuccess(PsnFundCompanyInfoQueryResult result);

    }

    public interface Presenter extends BasePresenter {
        //基金转换结果
        void confirmConversion(FundConversionConfirmModel params);

        //基金转换结果(挂单)
        void confirmNightConversion(FundConversionConfirmModel params);

        //基金公司信息查询
        void queryFundCompanyInfo(String fundCode);

    }
}
