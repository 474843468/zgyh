package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.event.InvestMenuChangeEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.adapter.ChooseAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.adapter.ChooseAdapter.ChooseCallBack;
import java.util.List;

/**
 * 投资-全菜单列表
 * Created by dingeryue on 2016年09月05.
 */
public class InvestModuleListFragment extends BussFragment {
  private final int MAX = 7;//最多菜单数

  private View mRoot;
  private ListView lvList;
  private ChooseAdapter<Item> itemListAdapter;
  private TextView actionButton;

  private List<Item> datas;
  private List<Item> selectDatas;
  /*private MoreMenusCallBack<Item> callBack;*/

  private View tipsView;
  private TextView tvAddeNum;
  private TextView tvRemainder;


  protected View onCreateView(LayoutInflater inflater){
    mRoot = inflater.inflate(R.layout.boc_fragment_more_menu, null);
    return mRoot;
  }

  @Override
  public void beforeInitView() {

  }

  @Override
  public void initView() {
    lvList = (ListView) mRoot.findViewById(R.id.lv_list);

    tipsView = mViewFinder.find(R.id.ll_select_count);
    tvAddeNum = mViewFinder.find(R.id.tv_added_num);
    tvRemainder = mViewFinder.find(R.id.tv_remainder);

    this.mTitleBarView.setTitle("更多");

    actionButton = this.mTitleBarView.setRightButton("编辑", new View.OnClickListener(){

      @Override
      public void onClick(View v) {
        v.setSelected(!v.isSelected());
        updateTips();
        if(v.isSelected()){
          actionButton.setText("保存");
          itemListAdapter.setEdit(true);
          actionButton.setTextColor(0xffff6666);
          tipsView.setVisibility(View.VISIBLE);
        }else {
          actionButton.setText("编辑");
          itemListAdapter.setEdit(false);
          //actionButton.setTextColor(0xff000000);
          tipsView.setVisibility(View.GONE);
          // 获取新的排序
          List<Item> newOrder = itemListAdapter.getNewOrder();

          BocEventBus.getInstance().post(new InvestMenuChangeEvent(newOrder));

          ToastUtils.show("菜单定制成功");
        }

      }
    });

    actionButton.setTextColor(0xffff6666);

  }

  private void updateTips(){
    int has = itemListAdapter.getCurrentSelectSize();
    int remainder = (MAX - has);
    tvAddeNum.setText(String.valueOf(has));
    tvRemainder.setText(String.valueOf(remainder));
  }

  @Override
  public void initData() {
    Bundle bundle = getArguments();
    datas =  bundle.getParcelableArrayList("datas");
    selectDatas = bundle.getParcelableArrayList("selects");
    initAdatper();
    itemListAdapter.update(datas,selectDatas);
  }
  private void initAdatper(){
    itemListAdapter = new ChooseAdapter<Item>() {
      @Override public void decorateView(ItemView textView) {
        int h = getResources().getDimensionPixelSize(R.dimen.boc_space_between_104px);
        textView.itemHeight(h);
        textView.getPaint().setFakeBoldText(true);
      }

      @Override public void updateView(ItemView itemView, Item data, int pos) {
        itemView.itemName(data.getTitle()).itemIcon(getResByName(data.getIconId()));
      }

    };
    lvList.setAdapter(itemListAdapter);
  }


  @Override
  public void setListener() {
    itemListAdapter.setChooseCallBack(new ChooseCallBack<Item>() {
      @Override public void onItemClick(View v, int pos, Item data) {
        getActivity().finish();
        ModuleActivityDispatcher.dispatch(mActivity, data.getModuleId());
      }
      @Override public boolean onCheckChange(boolean oldCheck, int pos, Item data) {
        if(itemListAdapter.getCurrentSelectSize() >= 7 && !oldCheck){
          ToastUtils.show("您最多可定制"+MAX+"个功能");
          return false;
        }
        return true;
      }

      @Override public void onAfterCheckChange() {
        updateTips();
      }
    });
  }

  @Override protected void titleLeftIconClick() {
    super.titleLeftIconClick();
  }

  @Override
  protected boolean getTitleBarRed() {
    return false;
  }


 /* public void setMoreMenuCallBack(MoreMenusCallBack<Item> callBack){
    this.callBack = callBack;
  }*/

  private int getResByName(String name){
    Resources resources = ApplicationContext.getInstance().getResources();
    try{
      int ids = resources.getIdentifier(name,"drawable",ApplicationContext.getAppContext().getPackageName());
      return  ids>0?ids:R.drawable.icon;
    }catch (Exception e){
      return R.drawable.icon;
    }
  }

/*  public  interface  MoreMenusCallBack<Item>{
    void  onEditFinish(List<Item> chooseDatas);
  }*/
}
