package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
 * 用途：学成归途
 */
public class NounStudyBackFragment extends BussFragment {
    private View mRootView = null;
    ListView listView;
    String stss[] = {"回国前注意事项","毕业回国时所需的金融服务"};
    String ss[] = {"tips","service"};
    String url = OverseasConst.OVERSEA_STUDY_BACK;
    private AdapterView.OnItemClickListener gvItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            WebURL(url+"#"+ss[position]);
        }
    };

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.fragment_overseas_strategy_listview, null);
        return mRootView;
    }

    @Override
    public void initData() {
        super.initData();
        listView = (ListView) mRootView.findViewById(R.id.list_view);
        List<Map<String, String>> list = new ArrayList<>();
        HashMap<String, String> map = null;
        for (int i = 0; i < 2; i++) {
            map = new HashMap<>();
            map.put("listName", stss[i]);
            list.add(map);
        }

        String[] form = new String[]{"listName"};
        int[] to = new int[]{R.id.fruit_name};

        listView.setAdapter(new SimpleAdapter(mContext, list,
                R.layout.overseas_strategy_item, form, to));

    }

    @Override
    public void initView() {
        super.initView();

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
        listView.setOnItemClickListener(gvItemListener);

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
