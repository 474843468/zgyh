package com.boc.bocsoft.mobile.bocmobile.buss.system.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.FessBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.PartialLoadView;
import com.boc.bocsoft.mobile.bocmobile.buss.common.product.ProductDispatcher;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 频道页结购汇-item
 * Created by lxw on 2016/8/4 0004.
 */
public class FessItemView extends BaseItemView<FessBean> {

    private View mRoot;
    private Context mContext;

    private TextView tvBuyRate;
    private TextView tvBuyNoteRate;
    private TextView tvSellRate;
    private TextView tvName;
    private TextView tvUpdateDate;

    //重新请求
    private PartialLoadView iv_reload;

    public FessItemView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public FessItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public FessItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        mRoot = View.inflate(mContext, R.layout.boc_item_main_fess, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        tvBuyRate = (TextView) mRoot.findViewById(R.id.tvBuyRate);
        tvBuyNoteRate = (TextView) mRoot.findViewById(R.id.tvBuyNoteRate);
        tvSellRate = (TextView) mRoot.findViewById(R.id.tvSellRate);
        iv_reload = (PartialLoadView) mRoot.findViewById(R.id.iv_reload);
        tvName = (TextView)mRoot.findViewById(R.id.tv_name);
        tvUpdateDate = (TextView)mRoot.findViewById(R.id.tvUpdateDate);

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
                    Map<String, Object> params = new HashMap<String, Object>();
                    ProductDispatcher.dispatchSBRemitMudule(ActivityManager.getAppManager().currentActivity(), params);
                }
            }
        });

        this.addView(mRoot, params);
    }

    /**
     * 设置显示数据
     *
     * @param fess
     */
    @Override
    public void setData(FessBean fess) {
        super.setData(fess);
        if (fess != null) {
            boolean success = false;
            String refreshState = fess.getRefreshState();
            if (refreshState.equals("0")) {
                iv_reload.setLoadStatus(PartialLoadView.LoadStatus.LOADING);
            } else if (refreshState.equals("1")) {
                iv_reload.setLoadStatus(PartialLoadView.LoadStatus.SUCCESS);
                success = true;
            } else if (refreshState.equals("2")) {
                iv_reload.setLoadStatus(PartialLoadView.LoadStatus.REFRESH);
            }

            if (StringUtils.isEmptyOrNull(fess.getName())) {
                tvName.setText(PublicCodeUtils.getCurrencyWithLetter(mContext, fess.getCurCode()));
            } else {
                tvName.setText(fess.getName());
            }

            if (!success) {
                tvBuyRate.setText("--");
                tvBuyNoteRate.setText("--");
                tvSellRate.setText("--");
                tvUpdateDate.setText("--");
                return;
            }

            // 银行现汇买入价
            if (fess.getBuyRate() != null && (fess.getBuyRate().compareTo(new BigDecimal("0")) > 0)) {
                tvBuyRate.setText(fess.getBuyRate().toString());
            } else {
                tvBuyRate.setText("");
            }

            // 银行现汇卖出价
            if (fess.getBuyNoteRate() != null && (fess.getBuyNoteRate().compareTo(new BigDecimal("0")) > 0)) {
                tvBuyNoteRate.setText(fess.getBuyNoteRate().toString());
            } else {
                tvBuyNoteRate.setText("");
            }

            //
            if (fess.getSellRate() != null && (fess.getSellRate().compareTo(new BigDecimal("0")) > 0)) {
                tvSellRate.setText(fess.getSellRate().toString());
            } else {
                tvSellRate.setText("");
            }

            tvUpdateDate.setText(fess.getUpdateDate());
        }
    }
}
