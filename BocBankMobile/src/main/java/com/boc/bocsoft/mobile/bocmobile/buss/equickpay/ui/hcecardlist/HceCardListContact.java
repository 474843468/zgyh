package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardlist;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.hcecardlist.HceCardListQueryViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * Contact：* e 闪付-查询Hce卡列表
 * Created by gengjunying on 2016/11/29
 */
public class HceCardListContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * e 闪付-查询Hce卡列表
         */
        void HCEQuickPassListQuerySuccess(List<HceCardListQueryViewModel>  hceCardListQueryViewModelList);

        /**
         * 失败回调：
         * e 闪付-查询Hce卡列表
         */
        void HCEQuickPassListQueryFail(BiiResultErrorException biiResultErrorException);


        /**
         * 成功回调：
         * e 闪付-未登录查询Hce卡列表
         */
        void HCEQuickPassListNoLoginQuerySuccess(List<HceCardListQueryViewModel>  hceCardListQueryViewModelList);


        /**
         * 失败回调：
         * e 闪付-查询Hce卡列表
         */
        void HCEQuickPassListNoLoginQueryFail(BiiResultErrorException biiResultErrorException);


        /**
         * 成功回调：
         * e 闪付-注销
         */
        void PsnHCEQuickPassCancelSuccess();


        /**
         * 失败回调：
         * e 闪付-注销
         */
        void PsnHCEQuickPassCancelFail();



        /**
         * 成功回调：
         * HCE闪付卡LUK加载
         */
        void PsnHCEQuickPassLukLoadSuccess(List<String>  list);



        /**
         * 失败回调：
         * HCE闪付卡LUK加载
         */
        void PsnHCEQuickPassLukLoadFail(BiiResultErrorException biiResultErrorException);

    }

    public interface Presenter extends BasePresenter {
        /**
         * e 闪付-已登录查询Hce卡列表
         */
        void HCEQuickPassListQuery(String temp1);


        /**
         * e 闪付-未登录查询Hce卡列表
         */
        void HCEQuickPassListNoLoginQuery(String temp1, String temp2,String temp3);


        /**
         * e 闪付-注销
         */
        void PsnHCEQuickPassCancel(String temp1, String temp2, String temp3);


        /**
         *  HCE闪付卡LUK加载
         */
        void PsnHCEQuickPassLukLoad(String deviceNo, String slaveCardNo, String cardSeq, String keyNum);

        String getConversationId();

    }
}


