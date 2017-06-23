package com.boc.bocsoft.mobile.bocmobile.buss.system.home.presenter;

import android.support.annotation.NonNull;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.searchallpro.SearchAllProParams;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.searchallpro.SearchAllProResult;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.ProductSearchContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListParams;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListResult;
import com.boc.bocsoft.mobile.cr.bus.product.service.CRProductService;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import com.boc.bocsoft.mobile.wfss.buss.common.model.WFSSSearchAllProParams;
import com.boc.bocsoft.mobile.wfss.buss.common.model.WFSSSearchAllProResult;
import com.boc.bocsoft.mobile.wfss.common.service.CommonService;

import java.util.List;

/**
 * 产品搜索
 * Created by gwluo on 2016/11/5.
 */

public class ProductSearchPresenter extends RxPresenter implements ProductSearchContract.Presenter {
    private ProductSearchContract.View mView;
    private CommonService service;
    private CRProductService productService;
    private WealthManagementService wealthService;// 理财Service

    public ProductSearchPresenter(ProductSearchContract.View view) {
        mView = view;
        service = new CommonService();
        productService = new CRProductService();
        wealthService = new WealthManagementService();
    }

    /**
     * 产品搜索
     *
     * @param params
     */
    @Override
    public void searchAllPro(SearchAllProParams params) {
        WFSSSearchAllProParams clientParams = new WFSSSearchAllProParams();
        clientParams.setKey(params.getKey());
        service.searchAllPro(clientParams).
                compose(this.<WFSSSearchAllProResult>bindToLifecycle()).
                compose(SchedulersCompat.<WFSSSearchAllProResult>applyIoSchedulers()).
                subscribe(new BIIBaseSubscriber<WFSSSearchAllProResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.searchProductFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(WFSSSearchAllProResult searchAllProResult) {
                        SearchAllProResult result = getSearchAllProResult(searchAllProResult);
                        mView.searchProductSucc(result);
                    }

                    @NonNull
                    private SearchAllProResult getSearchAllProResult(WFSSSearchAllProResult searchAllProResult) {
                        SearchAllProResult result = new SearchAllProResult();
                        List<WFSSSearchAllProResult.Fund> fundList = searchAllProResult.getFundList();
                        List<SearchAllProResult.Fund> fundListCopy = result.getFundList();
                        for (WFSSSearchAllProResult.Fund fund :
                                fundList) {
                            SearchAllProResult.Fund item = new SearchAllProResult().new Fund();
                            item.setFundBakCode(fund.getFundBakCode());
                            item.setFundId(fund.getFundId());
                            item.setFundName(fund.getFundName());
                            fundListCopy.add(item);
                        }

                        List<WFSSSearchAllProResult.Lc> lcList = searchAllProResult.getLcList();
                        List<SearchAllProResult.Lc> lcListCopy = result.getLcList();
                        for (WFSSSearchAllProResult.Lc lc :
                                lcList) {
                            SearchAllProResult.Lc item = new SearchAllProResult().new Lc();
                            item.setProId(lc.getProId());
                            item.setProName(lc.getProName());
                            item.setKind(lc.getKind());
                            lcListCopy.add(item);
                        }
                        return result;
                    }
                });
    }

    public void cRgetProductList() {
        CRgetProductListParams params = new CRgetProductListParams();
        productService.cRgetProductList(params).
                compose(this.<CRgetProductListResult>bindToLifecycle()).
                compose(SchedulersCompat.<CRgetProductListResult>applyIoSchedulers()).
                subscribe(new BIIBaseSubscriber<CRgetProductListResult>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.getProductListFail();
                    }

                    @Override
                    public void onNext(CRgetProductListResult result) {
                        mView.getProductListSucc(result);
                    }
                });
    }

    /**
     * 查询客户理财账户信息
     */
    @Override
    public void queryFinanceAccountInfo() {
        PsnXpadAccountQueryParams params = new PsnXpadAccountQueryParams();
        params.setQueryType("1");
        params.setXpadAccountSatus(WealthConst.YES_1);
        wealthService.psnXpadAccountQuery(params)
                .compose(this.<PsnXpadAccountQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadAccountQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadAccountQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.queryFinanceAccountInfoFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadAccountQueryResult result) {
                        mView.queryFinanceAccountInfoSuccess(result);
                    }
                });
    }
}
