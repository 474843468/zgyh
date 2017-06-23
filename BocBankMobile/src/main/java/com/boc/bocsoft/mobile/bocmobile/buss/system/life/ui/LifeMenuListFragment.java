package com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.LifeTools;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.event.LifeItemClickEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.event.LifeMenuChangeEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeMenuModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.adapter.ChooseAdapter;
import java.util.List;

/**
 * 更多菜单 -
 */
public class LifeMenuListFragment extends BussFragment{

    private final int MAX = 7;//最多菜单数

    private View mRoot;
    private ListView lvList;
    private ChooseAdapter<LifeMenuModel> itemListAdapter;
    private TextView actionButton;

    private List<LifeMenuModel> datas;
    private List<LifeMenuModel> selectDatas;
    //private MoreMenusCallBack<LifeMenuModel> callBack;

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

        this.mTitleBarView.setTitle("更多");

        tipsView = mViewFinder.find(R.id.ll_select_count);
        tvAddeNum = mViewFinder.find(R.id.tv_added_num);
        tvRemainder = mViewFinder.find(R.id.tv_remainder);

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
                    actionButton.setTextColor(0xff000000);
                    tipsView.setVisibility(View.GONE);
                    // 获取新的排序
                    List<LifeMenuModel> newOrder = itemListAdapter.getNewOrder();
                        itemListAdapter.update(datas,newOrder);
                        BocEventBus.getInstance().post(new LifeMenuChangeEvent(newOrder));
                    ToastUtils.show("菜单定制成功");
                }

            }
        });
        actionButton.setTextColor(0xff000000);
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
        itemListAdapter = new ChooseAdapter<LifeMenuModel>() {
            @Override public void decorateView(ItemView textView) {
                int h = getResources().getDimensionPixelSize(R.dimen.boc_space_between_96px);
                textView.itemHeight(h);
            }

            @Override public void updateView(ItemView itemView, LifeMenuModel data, int pos) {
                itemView.itemName(data.getCatName()).itemIcon(LifeTools.getResIcon(data));
            }

        };
        lvList.setAdapter(itemListAdapter);
    }


    @Override
    public void setListener() {
        itemListAdapter.setChooseCallBack(new ChooseAdapter.ChooseCallBack<LifeMenuModel>() {
            @Override public void onItemClick(View v, int pos, LifeMenuModel data) {
                BocEventBus.getInstance().post(new LifeItemClickEvent(pos,data));
                getActivity().finish();
            }

            @Override public boolean onCheckChange(boolean oldCheck, int pos, LifeMenuModel data) {
                if(itemListAdapter.getCurrentSelectSize() >= MAX && !oldCheck){
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

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    private void updateTips(){
        int has = itemListAdapter.getCurrentSelectSize();
        int remainder = (MAX - has);
        tvAddeNum.setText(String.valueOf(has));
        tvRemainder.setText(String.valueOf(remainder));
    }

  /*  public void setMoreMenuCallBack(MoreMenusCallBack<LifeMenuModel> callBack){
        this.callBack = callBack;
    }

    public  interface  MoreMenusCallBack<LifeMenuModel>{
        void  onEditFinish(List<LifeMenuModel> chooseDatas);
        void  onItemClick(int pos, LifeMenuModel data);
    }*/
}
