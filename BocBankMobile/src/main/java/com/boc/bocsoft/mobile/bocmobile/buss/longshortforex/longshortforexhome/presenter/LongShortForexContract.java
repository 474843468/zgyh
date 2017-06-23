package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.presenter;


import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.psnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.psnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.wfssQueryMultipleQuotation.WFSSQueryMultipleQuotationReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.wfssQueryMultipleQuotation.WFSSQueryMultipleQuotationResModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querymultiplequotation.WFSSQueryMultipleQuotationParams;

import java.util.List;

/**
 * 双向宝首页
 * Created by yx on 2016/11/25.
 */

public class LongShortForexContract {
    /**
     * 接口调用
     */
    public interface LongShortForexHomePresenter extends BasePresenter {
        //-----------------------登录前调用接口---------------start--------

        /**
         * 4.18 018 PsnGetAllExchangeRatesOutlay 登录前贵金属、外汇、双向宝行情查询
         */
        void getPsnGetAllExchangeRatesOutlay(PsnGetAllExchangeRatesOutlayReqModel mParams);

        /**
         * WFSS-2.1 外汇、贵金属多笔行情查询
         * 概述
         * 查询外汇实盘、外汇虚盘、贵金属实盘、贵金属虚盘行情、详情基本信息。
         * 上送的URL
         * http://[ip]:[port]/mobileplatform/forex/queryMultipleQuotation
         */
        void getWFSSQueryMultipleQuotation(WFSSQueryMultipleQuotationReqModel mParams);
        //-----------------------登录前调用接口---------------end--------

        //-----------------------登录后调用接口---------------start--------


        //-----------------------登录后调用接口---------------end--------
    }

    /**
     * 接口响应处理
     */
    public interface LongShortForexHomeView extends BaseView<BasePresenter> {


        //-----------------------登录前调用接口---------------start--------
        /**
         * 成功回调： queryMultipleQuotation
         * WFSS-2.1 外汇、贵金属多笔行情查询-登录前-查询外汇实盘、外汇虚盘、贵金属实盘、贵金属虚盘行情、详情基本信息。
         */
        void obtainWfssQueryMultipleQuotationSuccess(WFSSQueryMultipleQuotationResModel viewListModel);

        /**
         * 失败回调： queryMultipleQuotation
         * WFSS-2.1 queryMultipleQuotation 外汇、贵金属多笔行情查询-登录前-查询外汇实盘、外汇虚盘、贵金属实盘、贵金属虚盘行情、详情基本信息。
         */
        void obtainWfssQueryMultipleQuotationFail(BiiResultErrorException biiResultErrorException);
        /**
         * 成功回调：
         * 4.18 018 PsnGetAllExchangeRatesOutlay 登录前贵金属、外汇、双向宝行情查询
         */
        void obtainPsnGetAllExchangeRatesOutlaySuccess(List<PsnGetAllExchangeRatesOutlayResModel> viewListModel);

        /**
         * 失败回调：
         * 4.18 018 PsnGetAllExchangeRatesOutlay 登录前贵金属、外汇、双向宝行情查询
         */
        void obtainPsnGetAllExchangeRatesOutlayFail(BiiResultErrorException biiResultErrorException);
        //-----------------------登录前调用接口---------------end--------


        //-----------------------登录后调用接口---------------start--------


        //-----------------------登录后调用接口---------------end--------


    }


}
