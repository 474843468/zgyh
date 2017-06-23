package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.mypurchase.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.mypurchase.model.XpadPsnVFGPositionInfoModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * 双向宝--我的持仓主页面
 * Created by zc on 2016/12/14
 */
public class MyPurchaseProductContract {

    public interface View extends BaseView<Presenter>{
        /**
         * 成功回调：
         * 查询当前交易账户成功
         */
        void psnXpadVfgGetBindAccountSuccess(VFGGetBindAccountViewModel vfgGetBindAccountViewModel);

        /**
         * 失败回调：
         * 查询当前交易账户失败
         */
        void psnXpadVfgGetBindAccountFail(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：
         * 查询当前持仓信息成功
         */
        void psnXpadVFGPositionInfoSuccess(XpadPsnVFGPositionInfoModel vfgPositionInfoModel);

        /**
         * 失败回调：
         * 查询当前持仓信息失败
         */
        void psnXpadVFGPositionInfoFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 接口请求
         * 查询当前交易账户
         */
        void psnVfgGetBindAccount(VFGGetBindAccountViewModel model);
        /**
         * 接口请求
         * 查询当前持仓信息
         */
        void psnVFGPositionInfo(XpadPsnVFGPositionInfoModel vfgPositionInfoModel);

    }

}
