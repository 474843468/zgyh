package com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.WebUrl;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.WebViewActivity;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.banner.ConvenientBanner;
import com.boc.bocsoft.mobile.bocmobile.base.widget.banner.holder.BannerViewHolderCreator;
import com.boc.bocsoft.mobile.bocmobile.base.widget.banner.listener.OnItemClickListener;
import com.boc.bocsoft.mobile.bocmobile.base.widget.gridview.DragGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.coin.CommemorativeCoinActivity;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.common.util.AdUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginBaseActivity;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.AdvertisementModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.view.BannerImageHolderView;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.LifeTools;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.event.LifeAddCommUseEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.event.LifeGoToAddCommUsePageEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.event.LifeItemClickEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.event.LifeMenuChangeEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeMenuModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.presenter.LifePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.adapter.LifeMenuAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.view.LifeTopView;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.LoginedEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.LogoutEvent;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import java.util.List;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 生活主fragment
 * Created by Administrator on 2016/5/24.
 */
public class LifeFragment extends BussFragment implements LifeContract.View,LifeContract.MoreView,View.OnClickListener,
    CityChooseFragment.OnLocationChooseListener {

  /**
   * 轮播时间
   */
  private final int TRUN_TIME = 1000 * 5;

  private View mRoot;

  private LifeTopView topBarView;//顶部导航
  private ConvenientBanner<AdvertisementModel> bannerView;//广告view
  private DragGridView menuDragView;//菜单view

  private LifeContract.Presenter lifePresenter;

  private View[] clickItemViews;

  private LifeMenuAdapter menuAdapter;
  private  RxLifecycleManager lifecycleManager;

  String[] names = { "精彩特惠", "惠聚天下", "排队预约", "纪念币预约" };
  String[] infos = { "优惠尽在掌握", "活动源源不断", "减省排队时间", "" };
  int[] icons = {
      R.drawable.icon_life_jingcaitehui, R.drawable.icon_life_youhuishanghu,
      R.drawable.icon_life_paiduiyuyue, R.drawable.icon_life_jinianbi
  };

  private Runnable mNextLoginAction;

  @Override protected View onCreateView(LayoutInflater mInflater) {
    mRoot = mInflater.inflate(R.layout.boc_fragment_life, null);
    return mRoot;
  }

  @Override public void beforeInitView() {

  }

  @Override public void initView() {
    topBarView = mViewFinder.find(R.id.view_topbar);
    bannerView = mViewFinder.find(R.id.view_banner);
    menuDragView = mViewFinder.find(R.id.view_menu);

    clickItemViews = new View[] {
        mViewFinder.find(R.id.view_item_1), mViewFinder.find(R.id.view_item_2),
        mViewFinder.find(R.id.view_item_3), mViewFinder.find(R.id.view_item_4)
    };

    resertHeader();


    int index = 0;
    for (View view : clickItemViews) {
      TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
      TextView tv_info = (TextView) view.findViewById(R.id.tv_info);
      ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
      tv_name.setText(names[index]);
      tv_info.setText(infos[index]);
      iv_icon.setImageResource(icons[index]);
      view.setOnClickListener(this);
      view.setTag(Integer.valueOf(index));
      index++;
    }
  }

  @Override public void initData() {
    topBarView.setTitle("生活");
    topBarView.setLeftLoading();

    LifePresenter lifePresenter = new LifePresenter(this);
    lifePresenter.setMoreView(this);
    setPresenter(lifePresenter);

    menuAdapter = new LifeMenuAdapter(mContext);
    menuDragView.setAdapter(menuAdapter);

    menuDragView.setCanDrag(false);
    //初始化数据
    this.lifePresenter.initData();
  }

  @Override public void setListener() {

    topBarView.leftClick(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if(!lifePresenter.isLogin()){
          startLoginActivityForViewClick(v);
        }else{
          actionChooseLocationClick();
        }
      }
    });
    bannerView.setOnItemClickListener(new OnItemClickListener() {
      @Override public void onItemClick(int position) {

        List<AdvertisementModel> datas = bannerView.getDatas();
        if (datas == null || datas.size() - 1 < position) {
          return;
        }
        actionBannerItemClick(datas.get(position));
      }
    });
    menuDragView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!lifePresenter.isLogin()){//未登录 先跳转登录
          startLoginActivityForMenuClick(position,menuAdapter.getCount(),menuAdapter.getItem(position));
          return;
        }
        if (position == menuAdapter.getCount() - 1) {//更多
          actionMenuMoreClick();
        } else {
          actionMenuClick(position, menuAdapter.getItem(position));
        }
      }
    });
 /*   menuAdapter.setDragInterface(new DragGridBaseAdapter.DragInterface<LifeMenuModel>() {
      @Override public void onDragEnd() {
        lifePresenter.saveMenuSort(menuAdapter.getDatas());
      }

      @Override public void onItemDelete(LifeMenuModel lifeMenuModel) {
        lifePresenter.saveMenuSort(menuAdapter.getDatas());
      }
    });*/

    lifecycleManager = new RxLifecycleManager();

    //更多菜单中改变了
    BocEventBus.getInstance()
        .getBusObservable()
        .compose(lifecycleManager.bindToLifecycle())
        .ofType(LifeMenuChangeEvent.class)
        .subscribe(new Action1<LifeMenuChangeEvent>() {
          @Override public void call(LifeMenuChangeEvent event) {

            addAddCommUseEvent();

            List<LifeMenuModel> menuModels = new LifeTools().getHomeMenus(lifePresenter.getAllMenuModels());

            //更新DB
            lifePresenter.saveMenuSort(menuModels);

            updateMenu(menuModels);

          }
        });

    BocEventBus.getInstance()
        .getBusObservable()
        .compose(lifecycleManager.bindToLifecycle())
        .ofType(LifeItemClickEvent.class)
        .subscribe(new Action1<LifeItemClickEvent>() {
          @Override public void call(LifeItemClickEvent event) {
            addAddCommUseEvent();
            actionMenuClick(-1,event.getData());
          }
        });

    BocEventBus.getInstance().getBusObservable().compose(lifecycleManager.bindToLifecycle())
        .ofType(LogoutEvent.class)
        .subscribe(new Action1<LogoutEvent>() {
          @Override public void call(LogoutEvent logoutEvent) {
            hasLoginStateChange = true;
          }
        });

    BocEventBus.getInstance().getBusObservable().compose(lifecycleManager.bindToLifecycle())
        .ofType(LoginedEvent.class)
        .subscribe(new Action1<LoginedEvent>() {
          @Override public void call(LoginedEvent logoutEvent) {
            hasLoginStateChange = true;
          }
        });

    //接收到新增个人常用
    addAddCommUseEvent();
    /*BocEventBus.getInstance()
        .getBusObservable()
        .compose(lifecycleManager.bindToLifecycle())
        .ofType(LifeAddCommUseEvent.class)
        .subscribe(new Action1<LifeAddCommUseEvent>() {
          @Override public void call(LifeAddCommUseEvent event) {
            actionMenuClick(-1,event.getData());
          }
        });*/

    BocEventBus.getInstance()
        .getBusObservable()
        .compose(lifecycleManager.bindToLifecycle())
        .ofType(LifeGoToAddCommUsePageEvent.class)
        .subscribe(new Action1<LifeGoToAddCommUsePageEvent>() {
          @Override public void call(LifeGoToAddCommUsePageEvent lifeGoToAddCommUsePageEvent) {
            LifeVo.CityVo cityVo = lifePresenter.currentLocation();
            LifeTools.goToAddCommService(getActivity(), lifePresenter.getMapParamsCommonUse(), lifePresenter.getMapParamsCity(),
                cityVo.getProvinceVo().getShortName(),
                cityVo.getCode(),cityVo.getProvinceVo().getName(), cityVo.getName(),
                lifePresenter.getConversitionId());
          }
        });
  }

  private Subscription subscribe;
  private void addAddCommUseEvent(){
    removeAddCommUseEvent();
    LogUtils.d("dding","---添加  addAddCommUseEvent --- ");
    subscribe = BocEventBus.getInstance()
        .getBusObservable()
        .compose(lifecycleManager.bindToLifecycle())
        .ofType(LifeAddCommUseEvent.class)
        .subscribe(new Action1<LifeAddCommUseEvent>() {
          @Override public void call(LifeAddCommUseEvent lifeAddCommUseEvent) {
            LogUtils.d("dding","----接收到 添加个人常用成功 event LifeFragment");
            lifePresenter.loadCommonUseMenus();
          }
        });
  }

  private void removeAddCommUseEvent(){
    LogUtils.d("dding","移除  removeAddCommUseEvent");
    if(subscribe != null && !subscribe.isUnsubscribed()){
      subscribe.unsubscribe();
      subscribe = null;
    }
  }


  @Override public void onClick(View v) {
    Integer index = (Integer) v.getTag();
    if (index != null) {

      switch (index){
        case 0:
          String path = WebUrl.LIFE_JINGCAITEHUI;
          gotoWeb(path,names[0]);
          break;
        case 1://汇聚天下
          gotoWeb(WebUrl.LIFE_HUIJUTIANXIA,names[1]);
          break;
        case 2:
          LifeTools.gotoBranchOrder(getActivity());
          break;
        case 3://纪念币预约
          Intent intent  = new Intent(getContext(), CommemorativeCoinActivity.class);
          getActivity().startActivity(intent);
          break;
      }
    }

  }


  private void gotoWeb(String url,String title){

    Intent intent = new Intent();
    intent.putExtra(WebViewActivity.URL, url);
    intent.putExtra(WebViewActivity.TITLE, title);
    intent.setClass(getActivity(), WebViewActivity.class);
    getActivity().overridePendingTransition( R.anim.boc_infromright, R.anim.boc_outtoleft);
    getActivity().startActivity(intent);

  }

  @Override public void showLocationChoosePage() {
    final LocationChooseFragment fragment = new LocationChooseFragment();
    fragment.setOnLocationChooseListener(LifeFragment.this);
    start(fragment);
  }

  private void startLoginActivityForViewClick(final View view){
    Intent intent = new Intent();
    intent.setClass(getContext(),LoginBaseActivity.class);

    nextMenuAction = null;//一次只能触发一个nextaction

    mNextLoginAction = new Runnable() {
      @Override public void run() {
        //成功
        view.performClick();
      }
    };

  /*  LoginContext.instance.setCallback(new LoginCallback() {
      @Override public void success() {

      }
    });*/
    startActivity(intent);
  }

  private Runnable nextMenuAction ;

  private void startLoginActivityForMenuClick(int pos,int total ,final LifeMenuModel lifeMenuModel) {
    final boolean isMore = (pos == total-1);

    Intent intent = new Intent();
    intent.setClass(getContext(),LoginBaseActivity.class);

/*
    LoginContext.instance.setCallback(new LoginCallback() {
      @Override public void success() {
        //lifePresenter.initData();
      }
    });*/
    startActivity(intent);

    mNextLoginAction = null;
    nextMenuAction = new NextMenuClickAction(isMore,lifeMenuModel);
  }

  private void actionChooseLocationClick() {
    showLocationChoosePage();
  }

  /**
   * 广告点击
   */
  private void actionBannerItemClick(AdvertisementModel ad) {
    AdUtils.onAdClick(ad);
  }

  /**
   * 菜单更多点击 - 跳转缴费列表
   */
  private void actionMenuMoreClick() {

    Bundle bundle = new Bundle();
 /*   ArrayList<LifeMenuModel> allMenuModels = lifePresenter.getAllMenuModels();
    bundle.putParcelableArrayList("datas", allMenuModels);
    ArrayList<Parcelable> selectDatas = new ArrayList<Parcelable>(menuAdapter.getDatas());
    selectDatas.remove(selectDatas.size() - 1);
    bundle.putParcelableArrayList("selects", selectDatas);*/

    Intent intent = new Intent();
    intent.setClass(getContext(), BussActivity.class);
    intent.putExtra(BussActivity.MODULE_CLASS,LifeMenuList2Fragment.class);
    intent.putExtras(bundle);
    startActivity(intent);

    removeAddCommUseEvent();
  }

  /**
   * 菜单点击
   */
  private void actionMenuClick(int position, LifeMenuModel item) {
    if(item == null){
      //缴费记录
      LifeTools.gotoRecordService(getActivity());
      return;
    }

    if (LifeMenuModel.TYPE_CITY.equals(item.getTypeId())) {
      //城市 - 收费菜单
      LifeTools.gotoAllPayment(getActivity(), lifePresenter.getMapParamsCommonUse(), item.getCatId(),
          item.getCatName(), item.getIsAvalid(),item.getPrvcDispName(), item.getCityDispNo(),item.getCityDispName(),
          lifePresenter.getMenuNameById(item.getMenuId()), item.getPrvcShortName());
    } else if (LifeMenuModel.TYPE_COUNTRY.equals(item.getTypeId())) {
      LifeVo.CityVo cityVo = lifePresenter.currentLocation();
      String shortName = cityVo.getProvinceVo().getShortName();
      //国家
      LifeTools.gotoCountry(getActivity(),item.getMenuId(),shortName);
    } else if(LifeTools.isRecordMenu(item)){
      //跳转缴费记录
      LifeTools.gotoRecordService(getActivity());
    }

     else {
      //用户常用
     LifeTools.gotoComonService(getActivity(), item.getPrvcShortName(),item.getFlowFileId(), item.getCatName(),
          lifePresenter.getMenuNameById(item.getMenuId()), item.getMerchantName(),
          lifePresenter.getConversitionId());
    }
  }

  @Override protected boolean isHaveTitleBarView() {
    return false;
  }

  @Override public void updateBanner(List<AdvertisementModel> adModelList) {
    // 更新广告位
    bannerView.setCanLoop(adModelList != null && adModelList.size() > 1);
    bannerView.setPages(new BannerViewHolderCreator<BannerImageHolderView>() {
      @Override public BannerImageHolderView createHolder() {
        return new BannerImageHolderView();
      }
    }, adModelList)
        .setPageIndicator(new int[] {
            R.drawable.boc_view_banner_page_indicator,
            R.drawable.boc_view_banner_page_indicator_focused
        }).startTurning(TRUN_TIME)
        .setPointViewVisible(adModelList != null && adModelList.size() > 1);

  }

  @Override public void updateMenu(List<LifeMenuModel> menuModels) {
    menuAdapter.endEidt();
    menuAdapter.setDatas(menuModels);
  }

  @Override public void endLoadMenus(boolean isSuccess) {
    final  Runnable runnable = nextMenuAction;
    nextMenuAction = null;
    if(!isSuccess)return;

    if(runnable != null){
      runnable.run();
    }
  }

  @Override public void updateCoinStatus(String info, boolean isOpen) {
    //TODO 更新纪念币
    TextView tv = (TextView) clickItemViews[3].findViewById(R.id.tv_info);
    tv.setText(isOpen?"火爆预约中...":"敬请期待");
  }

  @Override public void endLocation(LifeVo.CityVo vo) {
    topBarView.endLeftLoading(vo.getName() + " ");
  }

  //private Boolean oldLoginState;
  private boolean hasLoginStateChange = false;

  @Override public void onResume() {
    super.onResume();

    if(lifePresenter.isLogin()){
      updateMenu(new LifeTools().getHomeMenus(lifePresenter.getAllMenuModels()));
    }

    LogUtils.d("dding","---lifeFragment resume----");
    addAddCommUseEvent();
    if(lifePresenter.isLogin()){

      if(mNextLoginAction!=null){
        mNextLoginAction.run();
        mNextLoginAction = null;
      }else if(nextMenuAction != null){
        //点击菜单请求的登录 , 这个时候还不能执行 ,必须等菜单数据加载完成才能执行
      }

    }else{
      mNextLoginAction = null;
      nextMenuAction = null;
    }

    if(hasLoginStateChange){
      //登录状态改变 - 刷新数据
      lifePresenter.initData();
      hasLoginStateChange = false;
    }

    /*if (oldLoginState != null && oldLoginState != lifePresenter.isLogin()) {
      //登录状态改变 - 刷新数据
      lifePresenter.initData();
      //menuDragView.setCanDrag(lifePresenter.isLogin());
    }*/
  }

  @Override public void onStop() {
    //oldLoginState = lifePresenter.isLogin();
    super.onStop();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    lifecycleManager.onDestroy();
    lifePresenter.unsubscribe();
  }

  @Override public void setPresenter(LifeContract.Presenter presenter) {
    this.lifePresenter = presenter;
  }

  public void showLoading(String msg) {
    //showLoadingDialog(msg);
    showLoadingDialog();
  }

  @Override public void endLoading() {
    closeProgressDialog();
  }

  @Override public void onItemDelSuccess(LifeMenuModel menuModel) {

  }

  @Override public void onLoadCommonUseEnd(boolean isSuccess, List<LifeMenuModel> list) {
    if(isSuccess){
      updateMenu(new LifeTools().getHomeMenus(lifePresenter.getAllMenuModels()));
    }

  }

  @Override public void OnChoose(LifeVo.CityVo cityVo) {
    lifePresenter.loadDataByLocation(cityVo);
    endLocation(cityVo);
  }

  @Override public void onCancel() {
   /* if (lifePresenter.currentLocation() == null) {
      OnChoose(new LifeTools().getDefauleCity());
    }*/
  }

  private void resertHeader() {
    //UI w=750 h = 226
    int ui_height = 472;
    int ui_width = 1080;

    int w = getResources().getDisplayMetrics().widthPixels;
    int h = ui_height * w / ui_width;

    ViewGroup.LayoutParams layoutParams = bannerView.getLayoutParams();
    if (layoutParams == null) {
      layoutParams = new ViewGroup.LayoutParams(-1, h);
    } else {
      layoutParams.height = h;
    }
    bannerView.setLayoutParams(layoutParams);
  }

  private class NextMenuClickAction implements Runnable{

    private boolean isMore=false;
    private LifeMenuModel lifeMenuModel;
    NextMenuClickAction(boolean ismore,LifeMenuModel menu){
      this.isMore = ismore;
      this.lifeMenuModel = menu;
    }

    @Override public void run() {
      if(isMore){
        actionMenuMoreClick();
      }else{
        // 跳转页面
        if(lifePresenter.getAllMenuModels() == null)return;
        int index = lifePresenter.getAllMenuModels().indexOf(lifeMenuModel);
        if(index<0){
          return;
        }
        LifeMenuModel fetch = lifePresenter.getAllMenuModels().get(index);
        actionMenuClick(-1,fetch);
      }
    }
  }

  @Override public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
    LogUtils.d("dding","-----life fragment onHiddenChanged :"+hidden);
    if(!hidden){
      addAddCommUseEvent();
    }else{
      //removeAddCommUseEvent();
    }
  }
}
