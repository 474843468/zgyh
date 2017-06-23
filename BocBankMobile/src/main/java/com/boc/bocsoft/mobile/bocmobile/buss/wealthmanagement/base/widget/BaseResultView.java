package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.boc.bocsoft.mobile.bocmobile.base.widget.buyprocedure.BuyProcedureWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.flowchart.BuyRedeemWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.adapter.LikeAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;

import java.util.List;

/**
 * @author wangyang
 *         2016/11/25 15:57
 *         理财结果页
 */
public class BaseResultView extends com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView{

    private BuyProcedureWidget buyProcedure;
    private BuyRedeemWidget redeemProcedure;

    private RecyclerView rvLike;
    private RecyclerView.LayoutManager layoutManager;
    private LikeAdapter adapter;


    public BaseResultView(Context context) {
        super(context);
    }

    public BaseResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addBuyProcedure(BuyProcedureWidget.CompleteStatus status, String[] dates, String[] contents){
        buyProcedure = new BuyProcedureWidget(getContext());
        buyProcedure.setDate(dates);
        buyProcedure.setText(contents);
        buyProcedure.setStatus(status);
        addHeadView(buyProcedure);
    }

    public void addBuyProcedureHint(String leftHint,String middleHint,String rightHint){
        buyProcedure.setLeftHint(leftHint);
        buyProcedure.setMiddHint(middleHint);
        buyProcedure.setRightHint(rightHint);
    }

    public void addRedeemProcedure(BuyRedeemWidget.CompleteRedeemStatus status,String[] titles, String[] values){
        redeemProcedure = new BuyRedeemWidget(getContext());
        redeemProcedure.setItemViewTitle(titles);
        redeemProcedure.setItemViewValue(values);
        redeemProcedure.setStatus(status);
        addHeadView(redeemProcedure);
    }

    public void addLikeView(List<WealthListBean> datas){
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
    }
}
