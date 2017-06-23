package com.boc.bocsoft.mobile.bocmobile.buss.system.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.FessBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.FundBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.GoldBean;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 优选投资view
 * Created by lxw on 2016/8/2 0002.
 */
public class SelectedInvestView extends LinearLayout {

    private Context mContext;
    private View mRoot;

    private SelectedInvestCallback mCallBack;

    // 基金列表
    private ArrayList<FundBean> funds;
    // 结售汇列表
    private ArrayList<FessBean> fesses;
    // 贵金属列表
    private ArrayList<GoldBean> golds;

    // 基金
    private LinearLayout fundContainer;
    // 贵金属
    private LinearLayout goldsContainer;
    // 结购汇
    private LinearLayout fessContainer;

    // 基金实体与视图对应关系
    private HashMap<String, FundItemView> fundItemViewHashMap;
    // 贵金属实体与视图对应关系
    private HashMap<String, GoldItemView> goldItemViewHashMap;
    // 结购汇实体与视图对应关系
    private HashMap<String, FessItemView> fessItemViewHashMap;

    private LinearLayout contentContainer;

    private LinearLayout emptyView;

    private TextView tvEdit;

    public SelectedInvestView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public SelectedInvestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public SelectedInvestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        mRoot = View.inflate(mContext, R.layout.boc_view_main_selected_invest, null);
        contentContainer = (LinearLayout) mRoot.findViewById(R.id.llContentContainer);
        fundContainer = (LinearLayout) mRoot.findViewById(R.id.llFundContainer);
        goldsContainer = (LinearLayout) mRoot.findViewById(R.id.llGoldContainer);
        fessContainer = (LinearLayout) mRoot.findViewById(R.id.llFessContainer);
        emptyView = (LinearLayout) mRoot.findViewById(R.id.llEmpty);
        tvEdit = (TextView) mRoot.findViewById(R.id.tvEdit);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mRoot, params);
        initListener();
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        tvEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onEditing();
                }

            }
        });
    }

    /**
     * 设置回调
     *
     * @param callBack
     */
    public void setCallBack(SelectedInvestCallback callBack) {
        this.mCallBack = callBack;
    }

    /**
     * 更新基金列表
     *
     * @param funds  基金
     * @param golds  贵金属
     * @param fesses 结购汇
     */
    public void setData(ArrayList<FundBean> funds, ArrayList<GoldBean> golds, ArrayList<FessBean> fesses) {
        this.funds = funds;
        this.golds = golds;
        this.fesses = fesses;
        updateContentView();
    }

    /**
     * 设置是否可以编辑
     *
     * @param editable
     */
    public void setEditable(boolean editable) {
        if (editable) {
            tvEdit.setVisibility(View.VISIBLE);
        } else {
            tvEdit.setVisibility(View.GONE);
        }

    }

    /**
     * 更新内容视图
     */
    public void updateContentView() {

        // 没有自定义项目时
        if (PublicUtils.isEmpty(funds)
                && PublicUtils.isEmpty(fesses)
                && PublicUtils.isEmpty(golds)) {
            contentContainer.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            resetFundContentView(funds);
            resetGoldContentView(golds);
            resetFessContentView(fesses);
            contentContainer.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    /**
     * 更新基金视图
     *
     * @param funds
     */
    public void resetFundContentView(List<FundBean> funds) {
        fundContainer.removeAllViews();
        if (PublicUtils.isEmpty(funds)) {
            fundContainer.setVisibility(View.GONE);
            fundItemViewHashMap = null;

        }
        if (!PublicUtils.isEmpty(funds)) {
            fundItemViewHashMap = new HashMap<>();
            for (FundBean fund : funds) {
                FundItemView itemView = new FundItemView(mContext);

                fundItemViewHashMap.put(fund.getFundCode(), itemView);
                itemView.setCallBack(new BaseItemView.ReloadCallBack<FundBean>() {
                    @Override
                    public void onReload(FundBean fundBean) {
                        if (mCallBack != null) {
                            mCallBack.onFundReload(fundBean);
                        }
                    }
                });
                itemView.setData(fund);
                fundContainer.setVisibility(View.VISIBLE);
                fundContainer.addView(itemView);
            }

        }
    }

    /**
     * 更新贵金属视图
     *
     * @param golds
     */
    public void resetGoldContentView(ArrayList<GoldBean> golds) {
        goldsContainer.removeAllViews();
        if (PublicUtils.isEmpty(golds)) {
            goldsContainer.setVisibility(View.GONE);
            goldItemViewHashMap = null;
        }
        // 结售汇
        if (!PublicUtils.isEmpty(golds)) {
            goldItemViewHashMap = new HashMap<>();
            for (GoldBean gold : golds) {
                GoldItemView itemView = new GoldItemView(mContext);
                goldItemViewHashMap.put(gold.getSourceCurrencyCode() + "-" + gold.getTargetCurrencyCode(),itemView);
                itemView.setCallBack(new BaseItemView.ReloadCallBack<GoldBean>() {
                    @Override
                    public void onReload(GoldBean bean) {
                        if (mCallBack != null) {
                            mCallBack.onGoldReload(bean);
                        }
                    }
                });
                itemView.setData(gold);
                goldsContainer.setVisibility(View.VISIBLE);
                goldsContainer.addView(itemView);
            }
        }

    }

    /**
     * 更新结购汇
     *
     * @param fesses
     */
    public void resetFessContentView(ArrayList<FessBean> fesses) {
        fessContainer.removeAllViews();
        if (PublicUtils.isEmpty(fesses)) {
            fessContainer.setVisibility(View.GONE);
            fessItemViewHashMap = null;
        }

        // 结购汇不为空时
        if (!PublicUtils.isEmpty(fesses)) {

            fessItemViewHashMap = new HashMap<>();
            for (FessBean fess : fesses) {
                FessItemView itemView = new FessItemView(mContext);
                fessItemViewHashMap.put(fess.getCurCode(), itemView);
                itemView.setCallBack(new BaseItemView.ReloadCallBack<FessBean>() {
                    @Override
                    public void onReload(FessBean bean) {
                        if (mCallBack != null) {
                            mCallBack.onFessReload(bean);
                        }
                    }
                });
                itemView.setData(fess);
                fessContainer.setVisibility(View.VISIBLE);
                fessContainer.addView(itemView);
            }
        }
    }

    /**
     * 更新基金视图
     */
    public void updateFundContentView(List<FundBean> funds){
        for (FundBean fundBean : funds) {
            updateFundContentView(fundBean);
        }
    }

    /**
     * 更新基金视图
     * @param fund
     */
    public void updateFundContentView(FundBean fund){
        if (fundItemViewHashMap != null) {
            FundItemView itemView = fundItemViewHashMap.get(fund.getFundCode());
            itemView.setData(fund);
        } else {
            fundItemViewHashMap = new HashMap<>();
            // TODO
        }
    }

    /**
     * 更新基金视图
     */
    public void updateGoldContentView(List<GoldBean> golds){
        for (GoldBean goldBean : golds) {
            updateGoldContentView(goldBean);
        }
    }

    /**
     * 更新基金视图
     * @param gold
     */
    public void updateGoldContentView(GoldBean gold){
        if (goldItemViewHashMap != null) {
            GoldItemView itemView = goldItemViewHashMap.get(gold.getSourceCurrencyCode() + "-" + gold.getTargetCurrencyCode());
            itemView.setData(gold);
        } else {
            goldItemViewHashMap = new HashMap<>();
            // TODO
        }
    }

    /**
     * 更新基金视图
     */
    public void updateFessContentView(List<FessBean> fesses){
        for (FessBean fessBean : fesses) {
            updateFessContentView(fessBean);
        }
    }

    /**
     * 更新基金视图
     * @param fess
     */
    public void updateFessContentView(FessBean fess){
        if (fessItemViewHashMap != null) {
            FessItemView itemView = fessItemViewHashMap.get(fess.getCurCode());
            itemView.setData(fess);
        } else {
            fessItemViewHashMap = new HashMap<>();
            // TODO
        }
    }

    /**
     * 投资理财选择画面回调
     */
    public interface SelectedInvestCallback {

        /**
         * 编辑回调
         */
        public void onEditing();

        /**
         * 结汇购汇重新请求
         */
        public void onFessReload(FessBean id);

        /**
         * 贵金属重新请求
         */
        public void onGoldReload(GoldBean id);

        /**
         * 基金重新请求
         */
        public void onFundReload(FundBean id);


    }
}
