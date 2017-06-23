package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.OverseasConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：lq7090
 * 创建时间：2016/11/3.
 * 用途：了解国家
 */
public class NounStudyKnowCountryFragment extends BussFragment {
    private View mRootView = null;

    GridView gv;
    TextView moreTv;
    String url = OverseasConst.OVERSEA_KNOW_ABOUT_COUNTRY;
    String countries[] ={"美国","加拿大","日本","澳大利亚","法国"};
    String countryurl[] = {"usa","canada","japan","australia","france"};

    private AdapterView.OnItemClickListener gvItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            WebURL(url+"#"+countryurl[position]);

        }
    };

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.fragment_nounstudybranch_layout, null);

        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        gv = (GridView) mRootView.findViewById(R.id.countrygv);
        gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        moreTv = (TextView) mRootView.findViewById(R.id.more);



        List<Map<String, String>> list = new ArrayList<>();
        HashMap<String, String> map = null;
        for (int i = 0; i < 5; i++) {
            map = new HashMap<>();
            map.put("productName", countries[i]);
            list.add(map);
        }
        String[] form = new String[]{"productName"};
        int[] to = new int[]{com.boc.bocsoft.mobile.bocmobile.R.id.tv_country_name};
        gv.setAdapter(new SimpleAdapter(mContext, list,
                R.layout.boc_item_country_branch, form, to));
    }
    /**
     * 是否显示标题栏，默认显示
     * 子类可以重写此方法，控制是否显示标题栏
     */
    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }


    @Override
    public void setListener() {
        super.setListener();

        gv.setOnItemClickListener(gvItemListener);
        moreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebURL(url);
            }
        });
    }

    /**
     * 跳转链接方法
     * @param url
     */

    public void WebURL(String url){
        ContractFragment contractFragment = new ContractFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("content", null);
        bundle.putBoolean("loadWithOverviewMode", true);
        contractFragment.setArguments(bundle);

        start(contractFragment);
    }
}
