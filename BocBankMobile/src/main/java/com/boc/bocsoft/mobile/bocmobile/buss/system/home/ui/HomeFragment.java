package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui;

import android.content.Context;
import android.graphics.Color;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.FessBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.FundBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.GoldBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.banner.ConvenientBanner;
import com.boc.bocsoft.mobile.bocmobile.base.widget.banner.holder.BannerViewHolderCreator;
import com.boc.bocsoft.mobile.bocmobile.base.widget.banner.listener.OnItemClickListener;
import com.boc.bocsoft.mobile.bocmobile.base.widget.gridview.DragGridBaseAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.gridview.DragGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.common.util.AdUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.util.InvestUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.system.common.adapter.MenuNavAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.common.view.SelectedInvestView;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.LoginedEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.event.MenuRefreshEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.AdvertisementModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.presenter.HomePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.view.BannerImageHolderView;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.view.HomeScrollView;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.LogoutEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 主页fragment
 * Created by lxw on 2016/5/23.
 */
public class HomeFragment extends BussFragment implements HomeContract.View{

    private final static String LOG_TAG = HomeFragment.class.getSimpleName();

    private float BANNER_WIDTH = 750;
    private float BANNER_HEIGHT = 328;
    protected HomeContract.Presenter mPresent;
    protected View rootView;
    protected DragGridView mainNav;
    private View mRoot;
    // 顶部广告栏控件
    private ConvenientBanner<AdvertisementModel> convenientBanner;
    private List<Item> menuItems;
    private HomeScrollView mScrollView;
    private MenuNavAdapter mMenuNavAdapter;

    private RelativeLayout headerContainer;
    private SelectedInvestView investContainer;
    private ImageView ivSearch;
    private ImageView ivLogo;
    private ImageView ivDbarCodes;

    private ArrayList<FundBean> funds;
    private ArrayList<GoldBean> golds;
    private ArrayList<FessBean> fesses;

    private Handler mHandler;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_home, null);
        return rootView;
    }

    @Override
    public void beforeInitView() {
        mHandler = new Handler();
        mPresent = new HomePresenter(this);
        BocEventBus.getInstance().getBusObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(homeBusSubscriber);
    }

    @Override
    public void initView() {
        convenientBanner = (ConvenientBanner) rootView.findViewById(R.id.convenientBanner);

        WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = (int)(width * (BANNER_HEIGHT/BANNER_WIDTH));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        convenientBanner.setLayoutParams(lp);
        mainNav = (DragGridView) rootView.findViewById(R.id.mainNav);

        mScrollView = mViewFinder.find(R.id.nsScroll);

        initMainNav();
        // 标题
        headerContainer = mViewFinder.find(R.id.headerContainer);
        // 精选投资
        investContainer = mViewFinder.find(R.id.investContainer);
        // 查询
        ivSearch = mViewFinder.find(R.id.ivSearch);
        ivLogo = mViewFinder.find(R.id.ivLogo);
        ivDbarCodes = mViewFinder.find(R.id.ivDbarCodes);

    }

    @Override
    public void initData() {

        // 获取广告
        mPresent.getAdsList();
        // 初始化优选投资列表
        mPresent.initInvestListView();

    }

    @Override
    public void setListener() {

        investContainer.setCallBack(new SelectedInvestView.SelectedInvestCallback() {

            @Override
            public void onEditing() {
                EditInvestModuleListViewFragment fragment = EditInvestModuleListViewFragment.newInstance(funds, golds, fesses);
                fragment.setOnChangedListener(onInvestListChangedListener);
                start(fragment);
            }

            @Override
            public void onFessReload(FessBean fessBean) {

                ArrayList<FessBean> fesses = new ArrayList<>();
                fesses.add(fessBean);
                mPresent.getFesses(fesses);
            }

            @Override
            public void onGoldReload(GoldBean goldBean) {
                ArrayList<GoldBean> golds = new ArrayList<>();
                golds.add(goldBean);
                mPresent.getGolds(golds);
            }

            @Override
            public void onFundReload(FundBean fundBean) {
                ArrayList<FundBean> funds = new ArrayList<>();
                funds.add(fundBean);
                mPresent.getFunds(funds);
            }
        });

        ivDbarCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到二维码扫描页面
                Intent intent = new Intent();
                intent.setClass(mActivity, BussActivity.class);
                intent.putExtra(BussActivity.MODULE_ID, ModuleCode.MODULE_QR_SCAN);
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition( R.anim.boc_infromright, R.anim.boc_outtoleft);
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //跳转到搜索产品界面
                ProductSearchFragment fragment = new ProductSearchFragment();
                start(fragment);

            }
        });
//        mScrollView.setOnScrollChangedListener(new HomeScrollView.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged(int l, int t, int oldl, int oldt) {
//
//                if (t > BANNER_HEIGHT){
//                    headerContainer.setBackgroundColor(0xe5ffffff);
//
//                } else {
//
//                    int tmp = (int)( 255 * 0.9*t/BANNER_HEIGHT );
//                    String colorString = "00ffffff";
//                    if (tmp < 20){
//                        colorString = "#" + "00ffffff";
//                        ivLogo.setVisibility(View.GONE);
//                        ivSearch.setImageResource(R.drawable.boc_image_search_white);
//                        ivDbarCodes.setImageResource(R.drawable.boc_image_dbarcodes_white);
//                        headerContainer.setBackgroundColor(Color.parseColor(colorString));
//                    } else {
//                        colorString = "#" + Integer.toString(tmp, 16) + "ffffff";
//                        ivSearch.setImageResource(R.drawable.boc_image_search_black);
//                        ivLogo.setVisibility(View.VISIBLE);
//                        ivDbarCodes.setImageResource(R.drawable.boc_image_dbarcodes_balck);
//                        headerContainer.setBackgroundColor(Color.parseColor(colorString));
//                    }
//
//                }
//            }
//        });

        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                AdvertisementModel adver = convenientBanner.getDatas().get(position);
                AdUtils.onAdClick(adver);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homeBusSubscriber.unsubscribe();
    }

    /**
     * 初始化主导航栏
     */
    private void initMainNav() {
        //设置一行可显示几个菜单选项

        //实例化菜单选项的Item
        mMenuNavAdapter = new MenuNavAdapter(mContext);
        mainNav.setAdapter(mMenuNavAdapter);
        mainNav.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = menuItems.get(position);
                String moduleId = item.getModuleId();
                if ("-1".equals(moduleId)) {
                    Bundle bundle = new Bundle();
                    ArrayList<Item> selectList = new ArrayList<>();
                    for (int i = 0; i < menuItems.size(); i++) {
                        // 剔除更多
                        if (!menuItems.get(i).getModuleId().equals("-1")) {
                            selectList.add(menuItems.get(i));
                        }
                    }
                    bundle.putSerializable("selectMenu", selectList);
                    Intent intent = new Intent();
                    intent.setClass(mContext, BussActivity.class);
                    intent.putExtra(BussActivity.MODULE_CLASS, ModuleListFragment.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    ModuleActivityDispatcher.dispatch(mActivity, moduleId);
                }
            }
        });

        mMenuNavAdapter.setDragInterface(new DragGridBaseAdapter.DragInterface() {

            @Override
            public void onDragEnd() {
                List<Item> datas = mMenuNavAdapter.getDatas();
                mPresent.updateMenuList(datas.subList(0, datas.size() - 1));
            }

            @Override
            public void onItemDelete(Object o) {
                List<Item> datas = mMenuNavAdapter.getDatas();
                mPresent.updateMenuList(datas.subList(0, datas.size() - 1));
            }
        });

        mPresent.getHomeModuleList();
    }

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     */
    private int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void updateAdsView(List<AdvertisementModel> adsList) {
        convenientBanner.setCanLoop(adsList != null && adsList.size() > 1);
        convenientBanner.startTurning(5000);
        convenientBanner.setPages(new BannerViewHolderCreator<BannerImageHolderView>() {

            @Override
            public BannerImageHolderView createHolder() {
                return new BannerImageHolderView();
            }
        }, adsList)
                .setPageIndicator(new int[]{
                        R.drawable.boc_view_banner_page_indicator,
                        R.drawable.boc_view_banner_page_indicator_focused
                })
                .setPointViewVisible(adsList != null && adsList.size() > 1);
    }

    @Override
    public void updateMainNav(List<Item> items) {
        Item item = new Item();
        item.setModuleId("-1");
        item.setTitle("更多");
        item.setIconId("boc_menu_home_more");
        items.add(item);
        menuItems = items;
        mMenuNavAdapter.setDatas(menuItems);
    }

    @Override
    public void updateFundView(Map<String, FundBean> fundBeanMap) {
        if (funds != null) {

            for (FundBean oldFundBean : funds) {
                FundBean newFundBean = fundBeanMap.get(oldFundBean.getFundCode());
                if (newFundBean != null) {
                    if(oldFundBean.getFundCode().equals(newFundBean.getFundCode())){
                        // 基金币种
                        oldFundBean.setCurrency(newFundBean.getCurrency());
                        oldFundBean.setFntype(newFundBean.getFntype());
                        oldFundBean.setFundIncomeRatio(newFundBean.getFundIncomeRatio());
                        oldFundBean.setFundIncomeUnit(newFundBean.getFundIncomeUnit());
                        oldFundBean.setOrderLowLimit(newFundBean.getOrderLowLimit());
                        oldFundBean.setApplyLowLimit(newFundBean.getApplyLowLimit());
                        oldFundBean.setDayIncomeRatio(newFundBean.getDayIncomeRatio());
                        oldFundBean.setNetPrice(newFundBean.getNetPrice());
                        oldFundBean.setChargeRate(newFundBean.getChargeRate());
                        oldFundBean.setEndDate(newFundBean.getEndDate());
                        oldFundBean.setIsBuy(newFundBean.getIsBuy());
                        oldFundBean.setIsInvt(newFundBean.getIsInvt());
                        oldFundBean.setSevenDayYield(newFundBean.getSevenDayYield());
                        oldFundBean.setRefreshState(newFundBean.getRefreshState());
                    }
                }

            }
        } else {
            funds = new ArrayList<>();
        }
        investContainer.updateFundContentView(funds);
    }

    @Override
    public void updateGoldView(Map<String, GoldBean> goldBeanMap) {
        if (golds != null) {

            for (GoldBean oldGoldBean : golds) {
                GoldBean newGoldBean = goldBeanMap.get(InvestUtils.getCurrencyPair(oldGoldBean.getSourceCurrencyCode(), oldGoldBean.getTargetCurrencyCode()));
                if (newGoldBean != null) {
                    oldGoldBean.setState(newGoldBean.getState());
                    oldGoldBean.setFlag(newGoldBean.getFlag());
                    oldGoldBean.setType(newGoldBean.getType());
                    oldGoldBean.setSellRate(newGoldBean.getSellRate());
                    oldGoldBean.setBuyRate(newGoldBean.getBuyRate());
                    oldGoldBean.setUpdateDate(newGoldBean.getUpdateDate());
                    oldGoldBean.setRefreshState(newGoldBean.getRefreshState());
                }
            }

        } else {
            golds = new ArrayList();
        }

        startDelayedUpdateGold(golds);
        investContainer.updateGoldContentView(golds);
    }

    @Override
    public void updateFessView(Map<String, FessBean> fessBeanMap) {
        if (fesses != null) {
            for (FessBean oldFessBean : fesses) {
                FessBean newFessBean = fessBeanMap.get(oldFessBean.getCurCode());
                if (newFessBean != null) {
                    oldFessBean.setCurCode(newFessBean.getCurCode());
                    oldFessBean.setUpdateDate(newFessBean.getUpdateDate());
                    oldFessBean.setBuyRate(newFessBean.getBuyRate());
                    oldFessBean.setSellRate(newFessBean.getSellRate());
                    oldFessBean.setBuyNoteRate(newFessBean.getBuyNoteRate());
                    oldFessBean.setSellNoteRate(newFessBean.getSellNoteRate());
                    oldFessBean.setUpdateDate(newFessBean.getUpdateDate());
                    oldFessBean.setRefreshState(newFessBean.getRefreshState());
                }
            }
        } else {
            fesses = new ArrayList();
        }
        // 开启刷新操作
        startDelayedUpdateGold(golds);
        investContainer.updateFessContentView(fesses);
    }

    @Override
    public void updateInvestListView(ArrayList<FundBean> funds, ArrayList<GoldBean> golds, ArrayList<FessBean> fesses) {
        this.funds = funds;
        this.golds = golds;
        this.fesses = fesses;
        investContainer.setData(funds, golds, fesses);

        //获取基金列表
        mPresent.getFunds(funds);
        //获取账户贵金属
        mPresent.getGolds(golds);
        //mPresent.startTimerUpdateGold(golds);

        //获取结购汇
        mPresent.getFesses(fesses);
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresent = presenter;
    }

    private OnChangedListener onInvestListChangedListener = new OnChangedListener(){

        @Override
        public void onChanged() {
            // 清理数据重新请求
            funds.clear();
            golds.clear();
            fesses.clear();
            stopDelayedUpdateGold();
            mPresent.initInvestListView();

        }
    };

    @Override
    public void onResume() {
        super.onResume();

        if(mHandler != null){
            startDelayedUpdateGold(golds);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        // 停止定时更新贵金属
        stopDelayedUpdateGold();
    }

    /**
     * 定时更新贵金属线程
     */
    Runnable updateGoldDataRunnable = new Runnable() {

        @Override
        public void run() {
            LogUtils.i(LOG_TAG, "开始执行定时刷新贵金属");
            mPresent.getGolds(golds);
            LogUtils.i(LOG_TAG, "执行结束定时刷新贵金属");
        }
    };

    /**
     * 开始定时更新贵金属数据
     */
    private void startDelayedUpdateGold(ArrayList<GoldBean> golds){
        try{
            if (golds != null && golds.size() > 0) {
                mHandler.postDelayed(updateGoldDataRunnable, 7000);
            } else {
                stopDelayedUpdateGold();
            }

        } catch(Exception ex){

        }
    }


    /**
     * 停止更新贵金属数据
     */
    private void stopDelayedUpdateGold(){
        mHandler.removeCallbacks(updateGoldDataRunnable);
    }

    /********************
     * EventBus  *
     *********************/

    private Subscriber<? super Object> homeBusSubscriber = new Subscriber<Object>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(Object event) {
            if (event instanceof MenuRefreshEvent) {
                mPresent.getHomeModuleList();
            } else if (event instanceof LoginedEvent) {
                mPresent.getAdsList();
            } else if (event instanceof LogoutEvent){
                mPresent.getAdsList();
            }
        }
    };


}
