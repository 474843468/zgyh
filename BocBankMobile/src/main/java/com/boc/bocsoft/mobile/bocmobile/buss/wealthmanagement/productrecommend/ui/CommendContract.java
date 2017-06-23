package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.model.RecommendModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.model.ProductModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.PsnXpadSignInitBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.ArrayList;

/**
 * 理财——商品推荐回调
 * Created by Wan mengxin on 2016/9/26.
 */
public class CommendContract {

    //界面回调
    public interface CommView extends BaseView<Presenter> {


        RecommendModel getModel();

        void psnOcrmProductQuerySuccess();

        void psnOcrmProductQueryFailed();

        void psnXpadProductDetailQuerySuccess(PsnXpadProductDetailQueryResModel model, PsnXpadAccountQueryResModel resModel, ArrayList<WealthAccountBean> accountList);

        void psnXpadProductDetailQueryFailed();

        void psnXpadProductBalanceQuerySuccess();

        void psnXpadProductBalanceQueryFailed();

        void psnXpadSignInitSuccess(PsnXpadSignInitBean result);

        void psnXpadSignInitFailed();
    }

    // 程序进程调用
    public interface Presenter extends BasePresenter {

        //指令交易产品查询  PsnOcrmProductQuery
        void psnOcrmProductQuery(RecommendModel model);

        //产品详情查询  PsnXpadProductDetailQuery
        void psnXpadProductDetailQuery(String code);

        //查询客户持仓信息  PsnXpadProductBalanceQuery
        void psnXpadProductBalanceQuery(String code);

        //周期性产品续约协议签约/签约初始化 PsnXpadSignInit
        void psnXpadSignInit(ProductModel model);
    }
}
