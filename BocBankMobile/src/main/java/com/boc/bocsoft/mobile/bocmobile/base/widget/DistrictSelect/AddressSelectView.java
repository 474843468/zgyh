package com.boc.bocsoft.mobile.bocmobile.base.widget.DistrictSelect;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 地址选择组件
 * Created by xintong on 2016/6/4.
 * 修改者：谢端阳
 */
public class AddressSelectView extends FrameLayout {
    private static final String TAG = "AddressSelect";
    //带动画的选项卡组件
    private AnimTabsView mTabsView;
    //地区选择页面列表
    private ListView listview;
    //标记用户是点击顶部选项卡进入的列表，还是依次选择进入的列表
    private boolean clickTabEnter;
    //选中的选项卡条目
    private int selectTab;
    //适配器集合
    private List<AddressAdapter> adapterList = new ArrayList<>();
    //可添加Tabs的最大数量
    private int maxNum;
    //存储选中的省市区
    private List<IAddress> addressInfo = new ArrayList<>();
    //请求数据失败后错误提示,由于选择项是关联的,所以只需要保留一条错误信息
    private String noDataInfo = "";
    //无数据界面布局
    private LinearLayout llyNoData;
    //无数据提示语显示
    private TextView tvNoData;

    private IDistrictSelectView districtSelectView;

    private boolean init = true;

    public AddressSelectView(Context context, IDistrictSelectView districtSelectView) {
        super(context);
        this.districtSelectView = districtSelectView;
        initView();
    }

    public AddressSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AddressSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setDistrictSelectView(IDistrictSelectView districtSelectView) {
        this.districtSelectView = districtSelectView;
    }

    /**
     * 初始化区域选择页面
     */
    private void initView() {
        inflate(getContext(), R.layout.boc_fragment_eloan_districtselect, this);
        mTabsView = (AnimTabsView) findViewById(R.id.tab);
        llyNoData = (LinearLayout) findViewById(R.id.llyNodata);
        tvNoData = (TextView) findViewById(R.id.tvNoData);
        listview = (ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(listener);
        mTabsView.setOnTabsItemClickListener(new AnimTabsView.OnTabsItemClickListener() {
            @Override
            public void changeData(int position) {
                // TODO Auto-generated method stub
                Log.i("----------->", "position: " + position);
                selectTab = position;
                clickTabEnter = true;
                //有数据
                if (adapterList.get(position) != null && !PublicUtils.isEmpty(
                        adapterList.get(position).getDatas())) {
                    listview.setAdapter(adapterList.get(position));
                    showNoDataView(false);
                } else {
                    tvNoData.setText(noDataInfo);
                    showNoDataView(true);
                }
            }
        });

        //选项卡条目变化监听
        mTabsView.setOnAnimTabsItemViewChangeListener(
                new AnimTabsView.IAnimTabsItemViewChangeListener() {
                    @Override
                    public void onChange(AnimTabsView tabsView, int oldPosition,
                            int currentPosition) {
                    }
                });
    }

    /**
     * 设置选项卡最大条目
     */
    public void setMaxTabsNum(int num) {
        this.maxNum = num;
        for (int i = 0; i < num; i++) {
            AddressAdapter adapter = new AddressAdapter(getContext());
            adapterList.add(adapter);
        }
    }

    public void initData() {
        districtSelectView.getFirstTabData();
    }

    /**
     * 导入列表数据
     */
    public void addDataList(List<? extends IAddress> list) {
        adapterList.get(selectTab).setDatas(list);
    }

    /**
     * 选项卡选中项改变后，刷新数据
     */
    private void dochange(int arg2) {
        if (adapterList.get(selectTab) != null) {
            if (clickTabEnter) {
                addressInfo.clear();
                mTabsView.removeItems(selectTab);
                clickTabEnter = false;
                //移除selectTab之后的tab对应的数据
                for (int i = selectTab + 1; i < adapterList.size(); i++) {
                    adapterList.get(i).clearData();
                }
            } else {
                mTabsView.removeItem(selectTab);
            }
            adapterList.get(selectTab).setSelectPosition(arg2);
            adapterList.get(selectTab).notifyDataSetChanged();
            mTabsView.addItem(adapterList.get(selectTab).getItem(arg2).getName());
            mTabsView.addItem("请选择");
        }
    }

    //为列表条目添加点击事件
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub

            if (selectTab == maxNum - 1) {
                adapterList.get(selectTab).setSelectPosition(position);
                adapterList.get(selectTab).notifyDataSetChanged();
                for (int i = 0; i < maxNum; i++) {
                    addressInfo.add(
                            adapterList.get(i).getItem(adapterList.get(i).getSelectPosition()));
                }
                districtSelectView.setSelectDistrict(addressInfo);
            } else {
                dochange(position);
                districtSelectView.getDistrictData(selectTab,
                        adapterList.get(selectTab++).getItem(position).getCode());
            }
        }
    };

    public void obtainDistrictSuccess(List<? extends IAddress> result) {
        if (init) {
            mTabsView.addItem("请选择");
            init = false;
        }
        addDataList(result);
        listview.setAdapter(adapterList.get(selectTab));
    }

    public void obtainDistrictFail(String failInfo) {
        if (init) {
            mTabsView.addItem("请选择");
            init = false;
        }
        noDataInfo = failInfo;
        tvNoData.setText(noDataInfo);
        showNoDataView(true);
    }

    private void showNoDataView(boolean isShowNoData) {
        if (isShowNoData) {
            llyNoData.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        } else {
            listview.setVisibility(View.VISIBLE);
            llyNoData.setVisibility(View.GONE);
        }
    }
}
