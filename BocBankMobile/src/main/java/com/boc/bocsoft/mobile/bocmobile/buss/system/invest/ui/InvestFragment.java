package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.banner.ConvenientBanner;
import com.boc.bocsoft.mobile.bocmobile.base.widget.banner.holder.BannerViewHolderCreator;
import com.boc.bocsoft.mobile.bocmobile.base.widget.banner.listener.OnItemClickListener;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.gridview.DragGridBaseAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.gridview.DragGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong.ModuleDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.util.AdUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.system.common.adapter.MenuNavAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.AdvertisementModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.view.BannerImageHolderView;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.InvestTools;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.event.InvestMenuChangeEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model.AssetDetailVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model.InvestItemVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.presenter.InvestPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui.view.MineAssetsCardView;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui.view.OptimalListView;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.LoginedEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.LogoutEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Action1;

/**
 * 投资主fragment
 * Created by dingeryue on 2016/5/25.
 */
public class InvestFragment extends BussFragment
    implements View.OnClickListener, InvestContract.View {

  private final int TURN_TIME = 1000 *5;

  private OptimalListView optimalListView;//优选投资根布局

  private View viewHeader;

  private MineAssetsCardView viewAssetInfo;//资产状况view
  //private PartialLoadView viewLoading;//资产loading

  private ConvenientBanner<AdvertisementModel> viewBanner;
  private DragGridView viewMenu;

  private DragGridBaseAdapter<Item> dragGridAdapter;

  private InvestContract.Presenter investPresenter;
  private  RxLifecycleManager lifecycleManager;

  @Override protected View onCreateView(LayoutInflater mInflater) {
    return mInflater.inflate(R.layout.boc_fragment_invest, null);
  }

  @Override public void beforeInitView() {

  }

  @Override public void initView() {

   changeHeader(false);

    viewHeader = mViewFinder.find(R.id.view_header);
    resertHeader();

    viewAssetInfo = mViewFinder.find(R.id.view_assetinfo);
    //viewLoading = mViewFinder.find(R.id.view_loading);

    viewBanner = mViewFinder.find(R.id.view_banner);
    viewMenu = mViewFinder.find(R.id.view_menu);

    optimalListView = mViewFinder.find(R.id.ll_optimal);

   /* if(ApplicationContext.getInstance().isLogin()){
      changUILogin();
    }else{
      changUINoLoin();
    }*/
  }

  @Override public void initData() {
    dragGridAdapter = new MenuNavAdapter(getActivity());
    viewMenu.setAdapter(dragGridAdapter);
    investPresenter = new InvestPresenter(this);

    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        investPresenter.uptateLoginState();
      }
    }, 1);
  }

  @Override public void setListener() {
    lifecycleManager = new RxLifecycleManager();

    //资产
    viewHeader.setOnClickListener(this);
    //viewLoading.setOnClickListener(this);

    //广告
    viewBanner.setOnItemClickListener(new OnItemClickListener() {
      @Override public void onItemClick(int position) {

        List<AdvertisementModel> datas = viewBanner.getDatas();
        if (datas == null || datas.size() - 1 < position) {
          return;
        }
        actionBannerItemClick(datas.get(position));
      }
    });

    //菜单点击
    viewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item = (Item) parent.getAdapter().getItem(position);
        if ("-1".equals(item.getModuleId())) {
          actionMoreClick();
        } else {
          actionMenuClick(item);
        }
      }
    });

    BocEventBus.getInstance().getBusObservable().compose(lifecycleManager.bindToLifecycle())
        .ofType(LogoutEvent.class)
        .subscribe(new Action1<LogoutEvent>() {
          @Override public void call(LogoutEvent logoutEvent) {
            changUINoLoin();
            hasLoginStateChange = true;
          }
        });

    BocEventBus.getInstance().getBusObservable().compose(lifecycleManager.bindToLifecycle())
        .ofType(LoginedEvent.class)
        .subscribe(new Action1<LoginedEvent>() {
          @Override public void call(LoginedEvent logoutEvent) {
            changUINoLoin();
            hasLoginStateChange = true;
          }
        });

    //菜单拖动
    dragGridAdapter.setDragInterface(new DragGridBaseAdapter.DragInterface<Item>() {
      @Override public void onDragEnd() {
        List<Item> datas = dragGridAdapter.getDatas();
        investPresenter.saveMenuSort(datas.subList(0, datas.size() - 1));
      }

      @Override public void onItemDelete(Item item) {
        List<Item> datas = dragGridAdapter.getDatas();
        investPresenter.saveMenuSort(datas.subList(0, datas.size() - 1));
      }
    });

    //优选投资部分
    optimalListView.setViewListener(new OptimalListView.OptimalListViewListener() {
      @Override public void onRefrensh() {
        //重新请求推荐接口
        investPresenter.loadInvestList();
      }

      @Override public void onItemRefrensh(int pos, InvestItemVo itemVo) {

        investPresenter.loadProductDetail(pos,itemVo);

      }

      @Override public void onItemClick(int pos, InvestItemVo itemVo) {
        if(InvestTools.isFund(itemVo)){
          ModuleDispatcher.gotoFincMudule(getActivity(),itemVo.getProductCode());
        }else if(InvestTools.isFinancing(itemVo)){
          //ModuleDispatcher.gotoInvestMudule(getActivity(),itemVo.getProductCode());

          DetailsRequestBean bean = new DetailsRequestBean();
          bean.setProdCode(itemVo.getProductCode());
          bean.setProdKind(itemVo.getProductNature());
          WealthDetailsFragment detailsFragment = new WealthDetailsFragment();

          Bundle bundle = new Bundle();
          bundle.putBoolean(WealthDetailsFragment.OTHER,true);
          bundle.putParcelable(WealthDetailsFragment.DETAILS_REQUEST,bean);
          detailsFragment.setArguments(bundle);

          Intent intent = new Intent();
          intent.setClass(getContext(),BussActivity.class);
          intent.putExtra(BussActivity.MODULE_CLASS,WealthDetailsFragment.class);
          intent.putExtras(bundle);
          startActivity(intent);

          //start(detailsFragment);

        }
      }
    });

    BocEventBus.getInstance().getBusObservable().ofType(InvestMenuChangeEvent.class)
        .compose(new RxLifecycleManager().<InvestMenuChangeEvent>bindToLifecycle())
        .subscribe(new Action1<InvestMenuChangeEvent>() {
          @Override public void call(InvestMenuChangeEvent event) {
            LogUtils.d("dding","----接收到事件改变:"+event);
            investPresenter.saveMenuSort(new ArrayList<>(new ArrayList<>(event.getNewOrder())));
            updateMenus(new ArrayList<>(event.getNewOrder()));
          }
        });

    viewAssetInfo.setViewClickListener(new MineAssetsCardView.ViewClickListener() {
      @Override public void onTipsClick() {
        actionTipsClicK();
      }
    });
  }

  private void actionTipsClicK(){
    ErrorDialog dialog = new ErrorDialog(getActivity());
    String msg = getResources().getString(R.string.invest_tips);
    dialog.setErrorData(msg);
    dialog.setBtnText("确定");
    dialog.show();
  }

  /**
   * 广告点击
   */
  private void actionBannerItemClick(AdvertisementModel ad) {
    if (ad == null) {
      return;
    }
    AdUtils.onAdClick(ad);
  }

  private void actionHeaderClick() {
    List<AssetDetailVo> detailVos = investPresenter.userAssetDetailList();
    if (detailVos == null || detailVos.size() == 0) {
      //return;
      detailVos = new ArrayList<>(0);
    }


    Bundle bundle = new Bundle();
    bundle.putParcelableArrayList("data", new ArrayList<Parcelable>(detailVos));
    //总资产
    bundle.putString("total",InvestTools.getTotalMoneyFormat(InvestTools.getTotalMoney(detailVos)));

    Intent intent = new Intent();
    intent.setClass(getContext(), BussActivity.class);
    intent.putExtra(BussActivity.MODULE_CLASS,InvestSummarizeFragment.class);
    intent.putExtras(bundle);
    startActivity(intent);
  }

  private void actionMoreClick() {
    Bundle bundle = new Bundle();
    ArrayList<Item> allMenuModels = investPresenter.getAllMenuModels();
    bundle.putParcelableArrayList("datas", allMenuModels);

    ArrayList<Parcelable> selectDatas = new ArrayList<Parcelable>(dragGridAdapter.getDatas());
    selectDatas.remove(selectDatas.size() - 1);//删除掉更多

    bundle.putParcelableArrayList("selects", selectDatas);
    //moreFragment.setArguments(bundle);

    Intent intent = new Intent();
    intent.setClass(getContext(), BussActivity.class);
    intent.putExtra(BussActivity.MODULE_CLASS,InvestModuleListFragment.class);
    intent.putExtras(bundle);
    startActivity(intent);

  }



  private void actionMenuClick(Item item){
    //actionHeaderClick();
   /* start(new TestBussFragment());
    if(true){
      return;
    }*/
    /*WealthManagementService service = new WealthManagementService();

    InvestPresenter presenter = (InvestPresenter) investPresenter;
    presenter.getOBSFinancing(new InvestItemVo()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<InvestItemVo>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable throwable) {

      }

      @Override public void onNext(InvestItemVo investItemVo) {

      }
    });

    if(true)return;*/
    ModuleActivityDispatcher.dispatch(mActivity, item.getModuleId());
  }

  protected boolean getTitleBarRed() {
    return false;
  }

  @Override protected String getTitleValue() {
    return "中银金融超市";
  }

  @Override protected boolean isHaveTitleBarView() {
    return true;
  }

  @Override protected boolean isDisplayLeftIcon() {
    return false;
  }

  @Override protected boolean isDisplayRightIcon() {
    return false;
  }

  @Override public void onClick(View v) {
    if(v == viewHeader){
      actionHeaderClick();
    }/*else if(v == viewLoading){
      viewLoading.setLoadStatus(PartialLoadView.LoadStatus.LOADING);
      viewLoading.setOnClickListener(null);
      investPresenter.loadMinAsset();
    }*/
  }

  @Override public void setPresenter(InvestContract.Presenter presenter) {

  }

  @Override public void changUILogin() {
    //viewAssetInfo.setVisibility(View.VISIBLE);
    //viewBanner.stopTurning();
    //viewBanner.setVisibility(View.INVISIBLE);
  }

  @Override public void changUINoLoin() {
   /* viewAssetInfo.setVisibility(View.VISIBLE);
    viewBanner.setVisibility(View.INVISIBLE);
    changeHeader(true);

    if(true){
      return;
    }*/

    viewAssetInfo.setVisibility(View.INVISIBLE);
    viewBanner.setVisibility(View.VISIBLE);
    viewBanner.startTurning(TURN_TIME);
  }


  @Override public void updateAdData(List<AdvertisementModel> adModelList) {

    viewBanner.setCanLoop(adModelList != null && adModelList.size() > 1);
    viewBanner.setPages(new BannerViewHolderCreator<BannerImageHolderView>() {
      @Override public BannerImageHolderView createHolder() {
        return new BannerImageHolderView();
      }
    }, adModelList)
        .setPageIndicator(new int[] {
            R.drawable.boc_view_banner_page_indicator,
            R.drawable.boc_view_banner_page_indicator_focused
        })
        .startTurning(TURN_TIME)
        .setPointViewVisible(adModelList != null && adModelList.size() > 1);

  }

  /**
   * @param items 数据不应该包含更多项
   */
  @Override public void updateMenus(List<Item> items) {
    if(items == null)items = new ArrayList<>();
    Item item = new Item();
    item.setModuleId("-1");
    item.setTitle("更多");
    item.setIconId("boc_menu_home_more");
    items.add(items.size(),item);
    dragGridAdapter.setDatas(items);
  }

  @Override public void setOptimalListViewLoadingState(boolean isVisiable,boolean isLoading) {
    optimalListView.setLoadingState(isVisiable,isLoading);
  }

  @Override public void updateProductData(List<InvestItemVo> investItemVos) {
    optimalListView.updateDatas(investItemVos);
  }

  @Override public void updateItemProduct(InvestItemVo vo, int pos) {
    optimalListView.updateItemData(vo,pos);
  }

  @Override public void onAssetLoadFail() {
    /*viewLoading.setLoadStatus(PartialLoadView.LoadStatus.REFRESH);
    viewLoading.setOnClickListener(this);
    viewLoading.setVisibility(View.VISIBLE);*/
    viewAssetInfo.setVisibility(View.INVISIBLE);
    viewBanner.setVisibility(View.VISIBLE);
    changeHeader(false);
  }

  @Override public void onAssetLoadSuccess() {


    BigDecimal totalMoney = InvestTools.getTotalMoney(investPresenter.userAssetDetailList());
    viewAssetInfo.setTotalAsset(InvestTools.getTotalMoneyFormat(totalMoney));

    //viewLoading.setLoadStatus(PartialLoadView.LoadStatus.SUCCESS);
    //viewLoading.setOnClickListener(null);
    //viewLoading.setVisibility(View.INVISIBLE);
    viewAssetInfo.setVisibility(View.VISIBLE);
    viewBanner.setVisibility(View.INVISIBLE);
    viewBanner.stopTurning();

    changeHeader(true);

  }

  private void changeHeader(boolean isRed){
    mTitleBarView.setBackgroundColor(0xffff4444);
    mTitleBarView.setTitleColor(0xffffffff);
  /*  if(isRed){
      mTitleBarView.setBackgroundColor(0xffff4444);
      mTitleBarView.setTitleColor(0xffffffff);
    }else{
      mTitleBarView.setBackgroundColor(Color.WHITE);
      mTitleBarView.setTitleColor(getResources().getColor(R.color.boc_text_color_dark_gray));
    }*/
  }

  private boolean hasLoginStateChange = false;

  @Override public void onResume() {
    super.onResume();
    if (hasLoginStateChange ) {
      //登录状态改变 - 刷新数据
      investPresenter.uptateLoginState();
      hasLoginStateChange = false;
    }
  }


  @Override public void onDestroy() {
    super.onDestroy();
    lifecycleManager.onDestroy();
  }

  private void resertHeader() {
    // UI w=759 height=248

    int ui_height = 472;
    int ui_width = 1080;

    int w = getResources().getDisplayMetrics().widthPixels;
    int h = ui_height * w / ui_width;

    ViewGroup.LayoutParams layoutParams = viewHeader.getLayoutParams();
    if (layoutParams == null) {
      layoutParams = new ViewGroup.LayoutParams(-1, h);
    } else {
      layoutParams.height = h;
    }
    viewHeader.setLayoutParams(layoutParams);
  }
}

