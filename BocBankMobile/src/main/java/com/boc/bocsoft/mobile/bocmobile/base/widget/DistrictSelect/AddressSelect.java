package com.boc.bocsoft.mobile.bocmobile.base.widget.DistrictSelect;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.DistrictRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.presenter.ApplyPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.ui.ApplyContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.ui.DistrictSelectFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址选择组件
 * Created by xintong on 2016/6/4.
 */
public class AddressSelect extends FrameLayout implements ApplyContract.DistrictSelectView, OnItemClickListener {
    private static final String TAG = "AddressSelect";
    private Context context;
    // 带动画的选项卡组件
    private AnimTabsView mTabsView;
    // 地区选择页面列表
    private ListView listview;
    private ApplyPresenter applyPresenter;
    private DistrictSelectFragment fragment;

    // 所有标签适配器的集合
    private List<GetAddressAdater> adapterList;
    // 所有标签数据的集合
    private List<List<DistrictRes>> resultList;

    // 可添加Tabs的最大数量
    private int maxNum;
    // 当前选中标签
    private int curSelectedLabel;
    // 6位行政区划代码
    private String zoneCode;
    // 存储选中的省市区
    private StringBuffer zoneStringBuffer;
    // 标记用户是点击顶部选项卡进入的列表，还是依次选择进入的列表
    private boolean isClickLabel;
    private boolean isEmptyData = false;

    public AddressSelect(Context context, DistrictSelectFragment fragment) {
        super(context);
        this.context = context;
        this.fragment = fragment;
        initView();
        initData();
        setListener();
    }

    public AddressSelect(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddressSelect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
        initData();
        setListener();
    }

    /**
     * 初始化页面
     */
    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.boc_fragment_eloan_districtselect, this);
        mTabsView = (AnimTabsView) findViewById(R.id.tab);
        listview = (ListView) findViewById(R.id.listview);
    }

    private void initData() {
        applyPresenter = new ApplyPresenter(AddressSelect.this);

        adapterList = new ArrayList<GetAddressAdater>();
        resultList = new ArrayList<List<DistrictRes>>();
        zoneStringBuffer = new StringBuffer();
    }

    private void setListener() {
        listview.setOnItemClickListener(this);
        mTabsView.setOnTabsItemClickListener(new AnimTabsView.OnTabsItemClickListener() {// 标签的监听
            @Override
            public void changeData(int position) {
                curSelectedLabel = position;
                isClickLabel = true;
                if (adapterList.get(position) != null) {
                    // 设置ListView数据
                    listview.setAdapter(adapterList.get(position));
                }
            }
        });

        //选项卡条目变化监听
        mTabsView.setOnAnimTabsItemViewChangeListener(new AnimTabsView.IAnimTabsItemViewChangeListener() {
            @Override
            public void onChange(AnimTabsView tabsView, int oldPosition, int currentPosition) {
            }
        });
    }

    /**
     * 设置选项卡最大条目
     */
    public void setMaxTabsNum(int num) {
        this.maxNum = num;
    }

    /**
     * 设置接口结果数据
     *
     * @param resultInit
     */
    public void setInitResult(List<DistrictRes> resultInit) {
        resultList.add(resultInit);
    }

    public void setBackResultList(List<List<DistrictRes>> resultList) {
        this.resultList = resultList;
    }

    /**
     * 添加ListView的数据
     */
    public void addDataList(List<String> list) {
        GetAddressAdater adapter = new GetAddressAdater(context, list);
        adapterList.add(adapter);
    }

    /**
     * 添加选项卡
     */
    public void addInitLabel() {
        mTabsView.addItem("请选择");
        if (adapterList.get(0) != null) {
            listview.setAdapter(adapterList.get(0));
        }
    }

    /**
     * （返回地区调用）
     */
    public void setData() {
        for (int i = 0; i < adapterList.size(); i++) {
            if (i == adapterList.size() - 1) {
                mTabsView.addItem("请选择");
            } else {
                mTabsView.addItem(i + "");
            }
        }
        listview.setAdapter(adapterList.get(adapterList.size() - 1));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (curSelectedLabel == maxNum - 1  && adapterList.size()==3) {// 如果最后一个标签，直接返显
            adapterList.get(curSelectedLabel).setSelectPosition(position);
            adapterList.get(curSelectedLabel).notifyDataSetChanged();
            if (!fragment.getArguments().getBoolean("First")) {
                zoneStringBuffer = null;
                zoneStringBuffer = new StringBuffer();
            }
            for (int i = 0; i < maxNum; i++) {
                zoneStringBuffer.append(adapterList.get(i).getItem(adapterList.get(i).getSelectPosition()));
            }

            zoneCode = resultList.get(curSelectedLabel).get(position).getZoneCode();
            // 返显
            fragment.setDistrictInfo(zoneStringBuffer.toString(), zoneCode);
        } else {// 否则请求接口，接口返回数据为0时直接返显
            if (isEmptyData) {
                if (curSelectedLabel > 0) {
                    --curSelectedLabel;
                }
            }
            isEmptyData = false;
            dochange(position);
            zoneCode = resultList.get(curSelectedLabel).get(position).getZoneCode();
            // 省市区联动查询
            fragment.showLoadingDialog();
            applyPresenter.queryDistrict(zoneCode);
            curSelectedLabel++;
        }
    }

    /**
     * 选项卡选中项改变后，刷新数据
     *
     * @param position
     */
    private void dochange(int position) {
        if (adapterList.get(curSelectedLabel) != null) {
            if (isClickLabel) {
                zoneStringBuffer = null;
                zoneStringBuffer = new StringBuffer();
                mTabsView.removeItems(curSelectedLabel);
                isClickLabel = false;
                for (int i = curSelectedLabel + 1; i < resultList.size(); i++) {
                    resultList.remove(i);
                    i--;
                }
                for (int i = curSelectedLabel + 1; i < adapterList.size(); i++) {
                    adapterList.remove(i);
                    i--;
                }
            } else {
                mTabsView.removeItem(curSelectedLabel);
            }
            adapterList.get(curSelectedLabel).setSelectPosition(position);
            adapterList.get(curSelectedLabel).notifyDataSetChanged();
            mTabsView.addItem(adapterList.get(curSelectedLabel).getItem(position).toString());
        }
    }

    /**
     * 省市区联动查询成功
     *
     * @param result
     */
    @Override
    public void obtainDistrictSuccess(List<DistrictRes> result) {
        fragment.closeProgressDialog();
        if (result.size() == 0) {// 如果数据为0则返显回去
            isEmptyData = true;
            if (!fragment.getArguments().getBoolean("First")) {
                zoneStringBuffer = null;
                zoneStringBuffer = new StringBuffer();
            }
            for (int i = 0; i < curSelectedLabel; i++) {
                zoneStringBuffer.append(adapterList.get(i).getItem(adapterList.get(i).getSelectPosition()));
            }
            // 返显
            fragment.setDistrictInfo(zoneStringBuffer.toString(), zoneCode);
            return;
        }
        mTabsView.addItem("请选择");
        if (curSelectedLabel == 1) {
            this.resultList.add(result);
            List<String> cityList = new ArrayList<String>();
            for (int i = 0; i < result.size(); i++) {
                cityList.add(result.get(i).getCityName());
            }
            //添加城市列表
            addDataList(cityList);
        } else if (curSelectedLabel == 2) {
            this.resultList.add(result);
            List<String> areaList = new ArrayList<String>();
            for (int i = 0; i < result.size(); i++) {
                areaList.add(result.get(i).getAreaName());
            }
            //添加市列表
            addDataList(areaList);
        }

        listview.setAdapter(adapterList.get(curSelectedLabel));

    }

    /**
     * 省市区联动查询失败
     *
     * @param e
     */
    @Override
    public void obtainDistrictFail(ErrorException e) {
        fragment.closeProgressDialog();
        fragment.showErrorDialog(e.getErrorMessage());
    }

    @Override
    public void setPresenter(ApplyContract.Presenter presenter) {

    }

}
