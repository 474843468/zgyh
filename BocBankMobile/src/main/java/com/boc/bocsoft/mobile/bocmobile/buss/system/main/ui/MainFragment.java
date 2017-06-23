package com.boc.bocsoft.mobile.bocmobile.buss.system.main.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.tab.MainTabView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.tab.TabIndicatorView;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong.event.BadgeChangeEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginBaseActivity;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginContext;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.HomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui.InvestFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.LifeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.ui.view.BottomTabIndicator;
import com.boc.bocsoft.mobile.bocmobile.buss.system.mine.ui.MineFragment;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import java.util.ArrayList;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 主页面Fragment
 * Created by lxw on 2016/8/1 0001.
 */
public class MainFragment extends BussFragment {
    private final static String TAG = MainFragment.class.getSimpleName();

    protected FrameLayout flContent;
    protected MainTabView tbMainTabHost;
    private View root;
    private BottomTabIndicator msgIndicatorView;
    private boolean hasMessageFragment = false;

    private boolean backToInitState = false;
    private int targetTab = -1;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        root = mInflater.inflate(R.layout.activity_main, null, false);
        return root;
    }

    @Override
    public void initView() {
        flContent = (FrameLayout) root.findViewById(R.id.fl_content);
        tbMainTabHost = (MainTabView) root.findViewById(R.id.tb_main_tab_host);
        initMainTabHost();
    }

    /**
     * 初始化主导航
     */
    private void initMainTabHost() {
        tbMainTabHost.setup(mContext, mActivity.getSupportFragmentManager(), R.id.fl_content);
        ArrayList<TabIndicatorView> tabs = new ArrayList();
        BottomTabIndicator concentIndicatorView = new BottomTabIndicator(mContext);
        concentIndicatorView.setTagId("concent").setIcon(mContext.getResources().getDrawable(R.drawable.boc_tab_icon_home))
                .setTitle("首页").setFragmentClass(HomeFragment.class);

        tabs.add(concentIndicatorView);

        BottomTabIndicator investIndicatorView = new BottomTabIndicator(mContext);
        investIndicatorView.setTagId("invest").setIcon(mContext.getResources().getDrawable(R.drawable.boc_tab_icon_invest))
                .setTitle("投资").setFragmentClass(InvestFragment.class);
        tabs.add(investIndicatorView);

        BottomTabIndicator lifeIndicatorView = new BottomTabIndicator(mContext);
        lifeIndicatorView.setTagId("life").setIcon(mContext.getResources().getDrawable(R.drawable.boc_tab_icon_life))
                .setTitle("生活").setFragmentClass(LifeFragment.class);
        tabs.add(lifeIndicatorView);

        Class<Fragment> infoFragment= null;
        try {
            infoFragment  = (Class<Fragment>) Class.forName("com.chinamworld.bocmbci.biz.infoserve.InfoServeMainFragment");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(infoFragment !=null){
            ApplicationContext.getInstance().setHasLianLong(true);
            msgIndicatorView = new BottomTabIndicator(mContext);
            msgIndicatorView.setTagId("message").setIcon(mContext.getResources().getDrawable(R.drawable.boc_tab_icon_goutong))
                .setTitle("沟通").setFragmentClass(infoFragment);
            tabs.add(msgIndicatorView);
            hasMessageFragment = true;
        }

        BottomTabIndicator personalIndicatorView = new BottomTabIndicator(mContext);
        personalIndicatorView.setTagId("personal").setIcon(mContext.getResources().getDrawable(R.drawable.boc_tab_icon_personal))
                .setTitle("我的").setFragmentClass(MineFragment.class);
        tabs.add(personalIndicatorView);
        tbMainTabHost.initTab(tabs);

        tbMainTabHost.setBeforeTabSelectionChanged(new MainTabView.OnBeforeTabSelectionChanged() {
            @Override public boolean onBeforeTabSelectionChanged(int tabIndex, boolean clicked) {

                if(hasMessageFragment && tabIndex == 3){
                    //msgIndicatorView.setBadgeNumber(false,0);
                }
                if(tabIndex == 2/*|| hasMessageFragment&&tabIndex == 3*/){
                    //return actionTabClickCheckLogin(tabIndex);
                }
                return false;
            }
        });

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override public void onResume() {
        super.onResume();
        LogUtils.d(TAG,"--mainFragment  onResume --->"+targetTab);
        if(targetTab>=0){
            setCurrentTab(targetTab);
            targetTab = -1;
        }
       /* if(nextRunable != null){
            nextRunable.run();
            nextRunable = null;
        }*/
        /*if (backToInitState) {
            toHomeFragemnt();
        }*/
    }

   /* private Runnable nextRunable;

    private boolean actionTabClickCheckLogin(final int pos) {
        boolean login = ApplicationContext.getInstance().isLogin();
        if (!login) {
            startToLogin(getActivity(), new LoginCallback() {
                @Override public void success() {
                    nextRunable = new Runnable() {
                        @Override public void run() {
                            LoginContext.instance.setCallback(null);
                            tbMainTabHost.setCurrentTab(pos);
                        }
                    };
                }
            });
            return true;
        }else{
            tbMainTabHost.setCurrentTab(pos);
        }

        return false;
    }*/

    /**
     * 跳转到登录页面
     */
    public void startToLogin(Activity activity,  LoginCallback callback){
        Intent intent = new Intent();
        intent.setClass(activity, LoginBaseActivity.class);
        if (callback != null) {
            LoginContext.instance.setCallback(callback);
        }

        activity.startActivity(intent);
    }

    public void setCurrentTab(int index){
        LogUtils.d(TAG,"--mainfragment setcurrentTab:"+index+"  "+tbMainTabHost +" isadd:"+isAdded()+" ");
        if(tbMainTabHost == null){
            targetTab = index;
        }else{
            if(isAdded() && isResumed()){
                tbMainTabHost.setCurrentTab(index);
            }else{
                targetTab = index;
            }
        }
    }

    private RxLifecycleManager lifecycleManager;
    @Override public void initData() {
        super.initData();
        lifecycleManager = new RxLifecycleManager();
        BocEventBus.getInstance().getBusObservable().ofType(BadgeChangeEvent.class)
            .compose(lifecycleManager.<BadgeChangeEvent>bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<BadgeChangeEvent>() {
                @Override public void call(BadgeChangeEvent badgeChangeEvent) {
                    if(msgIndicatorView == null)return;
                    LogUtils.d(TAG,"-------event:"+ (Looper.getMainLooper()==Looper.myLooper()));

                    msgIndicatorView.setBadgeNumber(badgeChangeEvent.isShow(),badgeChangeEvent.getCount());
                }
            });
    }

    @Override public void onDestroy() {
        super.onDestroy();
        lifecycleManager.onDestroy();
    }

    /*  public void toHomeFragemnt(){
        tbMainTabHost.setCurrentTab(0);
    }*/

    /**
     * 是否回初始状态
     * @param initState
     */
    public void setBackToInitState(boolean initState){
        this.backToInitState = initState;
    }
}
