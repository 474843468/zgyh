package com.boc.bocsoft.mobile.bocmobile.buss.system.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.GoldBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.PartialLoadView;
import com.boc.bocsoft.mobile.bocmobile.buss.common.product.ProductDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.util.InvestUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

/**
 * 频道页-贵金属item
 * Created by lxw on 2016/8/4 0004.
 */
public class GoldItemView extends BaseItemView<GoldBean> {

    private ViewGroup mRoot;
    private Context mContext;

    // 涨跌标识
    TextView tvFlag;
    // 卖出价格
    TextView tvSellRate;
    // 买入价
    TextView tvBuyRate;
    // 贵金属名称
    TextView tvName;
    // 贵金属名称
    TextView tvUpdateDate;

    TableLayout tableLayout;
    ImageView ivTrade;
    //重新请求
    PartialLoadView iv_reload;


    public GoldItemView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public GoldItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public GoldItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mRoot = (ViewGroup) View.inflate(mContext, R.layout.boc_item_main_glod, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams
                (LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        // 涨跌标识
        tvFlag = (TextView) mRoot.findViewById(R.id.tvFlag);
        // 卖出价格
        tvSellRate = (TextView) mRoot.findViewById(R.id.tvSellRate);
        // 买入价
        tvBuyRate = (TextView) mRoot.findViewById(R.id.tvBuyRate);
        iv_reload = (PartialLoadView) mRoot.findViewById(R.id.iv_reload);
        tvName = (TextView)mRoot.findViewById(R.id.tv_name);
        tvUpdateDate = (TextView)mRoot.findViewById(R.id.tvUpdateDate);
        tableLayout = (TableLayout) mRoot.findViewById(R.id.tlContainer);
        ivTrade = (ImageView) mRoot.findViewById(R.id.ivTrade);

        iv_reload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PartialLoadView.LoadStatus loadStatus = iv_reload.getLoadStatus();
                if (loadStatus == PartialLoadView.LoadStatus.REFRESH) {
                    iv_reload.setLoadStatus(PartialLoadView.LoadStatus.LOADING);
                    if (mCallback != null) {
                        mCallback.onReload(itemData);
                    }
                }
                if (mCallback != null) {
                    mCallback.onReload(itemData);
                }
            }
        });

        mRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(itemData.getRefreshState())){
                    ProductDispatcher.dispatchToGold(ActivityManager.getAppManager().currentActivity(),
                            NumberUtils.nvlBigDecimal(itemData.getBuyRate(), "--"),
                            NumberUtils.nvlBigDecimal(itemData.getSellRate(), "--"),
                            itemData.getSourceCurrencyCode(),
                            itemData.getTargetCurrencyCode());
                }
            }
        });
        this.addView(mRoot, params);

    }

    /**
     * 设置显示数据
     *
     * @param gold
     */
    @Override
    public void setData(GoldBean gold) {
        super.setData(gold);
        if (gold != null) {
            String refreshState = gold.getRefreshState();
            if (refreshState.equals("0")) {
                iv_reload.setLoadStatus(PartialLoadView.LoadStatus.LOADING);
            } else if (refreshState.equals("1")) {
                iv_reload.setLoadStatus(PartialLoadView.LoadStatus.SUCCESS);
            } else if (refreshState.equals("2")) {
                iv_reload.setLoadStatus(PartialLoadView.LoadStatus.REFRESH);
            }

            // 未变化
            if ("0".equals(gold.getFlag())) {
                tvFlag.setText("--");
                tvFlag.setVisibility(View.VISIBLE);
                ivTrade.setVisibility(View.GONE);
            }
            // 涨
            else if ("1".equals(gold.getFlag())){
                ivTrade.setImageResource(R.drawable.boc_home_gold_arrow_up);
                tvFlag.setVisibility(View.GONE);
                ivTrade.setVisibility(View.VISIBLE);
            }
            // 跌
            else if ("2".equals(gold.getFlag())){
                ivTrade.setImageResource(R.drawable.boc_home_gold_arrow_down);
                tvFlag.setVisibility(View.GONE);
                ivTrade.setVisibility(View.VISIBLE);
            } else {
                tvFlag.setText("--");
                tvFlag.setVisibility(View.VISIBLE);
                ivTrade.setVisibility(View.GONE);
            }

            tvUpdateDate.setText(gold.getUpdateDate());
            if (!StringUtils.isEmptyOrNull(gold.getName())) {
                tvName.setText(gold.getName());
            } else {
                tvName.setText(InvestUtils.getCurrencyPairName(mContext, gold.getSourceCurrencyCode(), gold.getTargetCurrencyCode()));
            }

            if (gold.getSellRate() != null) {
                tvSellRate.setText(gold.getSellRate().toString());
            }
            if (gold.getBuyRate() != null) {
                tvBuyRate.setText(gold.getBuyRate().toString());
            }
        }
    }
}
