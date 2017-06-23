package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.PartialLoadView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.waterwaveballview.WaterWaveBallView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanStatusListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.EloanQueryContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.adapter.RepayAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huixiaobo on 2016/6/20.
 * 提款页面
 */
public class EloanListView extends ListView {

    public EloanListView(Context context) {
        super(context);
    }

    public EloanListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EloanListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expMeasureSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
