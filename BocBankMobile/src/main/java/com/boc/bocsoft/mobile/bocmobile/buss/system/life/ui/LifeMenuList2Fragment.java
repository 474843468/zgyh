package com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.User;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.event.LifeAddCommUseEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.event.LifeGoToAddCommUsePageEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.event.LifeItemClickEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.event.LifeMenuChangeEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeMenuModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.presenter.LifePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.adapter.LifeMoreMenuAdapter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Action1;

/**
 * 更多菜单 -
 */
public class LifeMenuList2Fragment extends BussFragment implements LifeContract.MoreView{

    private final int MAX = 7;//最多菜单数

    private View mRoot;
    private ListView lvList;
    private TextView actionButton;

    private View tipsView;
    private TextView tvAddeNum;
    private TextView tvRemainder;
    private boolean isEditMode = false;//是否是编辑模式

    private LifeMoreMenuAdapter.LifeMoreNormalAdapter normalAdapter;
    private LifeMoreMenuAdapter.LifeMoreEditAdapter editAdapter;

    private LifePresenter lifePresenter;
    private RxLifecycleManager lifecycleManager;

    private boolean isFinishWhenPause = false;//onPause时 是否关闭页面

    protected View onCreateView(LayoutInflater inflater){
        mRoot = inflater.inflate(R.layout.boc_fragment_more_menu2, null);
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
                isEditMode = true;
                changeUI(isEditMode);
            }
        });
        actionButton.setTextColor(0xffff6666);
        actionButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.boc_text_size_small));
        actionButton.getPaint().setFakeBoldText(true);

    }

    @Override
    public void initData() {
        lifePresenter = new LifePresenter(null);
        lifePresenter.setMoreView(this);
        Bundle bundle = getArguments();

        initAdatper();
        //初始化为非编辑状态
        isEditMode = false;
        changeUI(isEditMode);
    }
    private void initAdatper(){

        normalAdapter = new LifeMoreMenuAdapter.LifeMoreNormalAdapter();
        //normalAdapter.updateDatas(datas);

        editAdapter = new LifeMoreMenuAdapter.LifeMoreEditAdapter();
        //editAdapter.updateDatas(datas);
    }

    @Override
    public void setListener() {
        //非编辑模式 点击跳转
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isEditMode)return;

                actionNormalItemClick(position,normalAdapter.getItem(position));
            }
        });

        //编辑模式 点击删除 添加
        editAdapter.setEditListener(new LifeMoreMenuAdapter.LifeMoreEditAdapter.EditListener() {
            @Override public void onItemDelClick(int pos, LifeMenuModel menuModel) {
                //ToastUtils.show("删除:"+menuModel.getCatName());
                lifePresenter.delCommonUse(menuModel);
            }

            @Override public void onAddClick() {
                List<LifeMenuModel> datas = editAdapter.getDatas();

                if(datas!=null && datas.size()< getMaxMenuCount()){
                    //ToastUtils.show("跳转连龙,添加常用");
                    BocEventBus.getInstance().post(new LifeGoToAddCommUsePageEvent());
                }else{
                    //常用缴费项目最多只能设置8个!
                    ToastUtils.show("您最多可定制"+getMaxMenuCount()+"个功能");
                }
            }
        });

        lifecycleManager = new RxLifecycleManager();
        BocEventBus.getInstance().getBusObservable().ofType(LifeAddCommUseEvent.class)
            .compose(lifecycleManager.<LifeAddCommUseEvent>bindToLifecycle())
            .subscribe(new Action1<LifeAddCommUseEvent>() {
                @Override public void call(LifeAddCommUseEvent lifeAddCommUseEvent) {
                    LogUtils.d("dding","生活,更多菜单接收到个人常用发生改变");
                    lifePresenter.loadCommonUseMenus();
                }
            });
    }

    private int notDislplayCount = 0;//不显示的菜单数
    private int getMaxMenuCount(){
        return  MAX - notDislplayCount;
    }
    /**
     * 切换编辑状态
     */
    private void changeUI(boolean isEdit) {
        List<LifeMenuModel> allMenuModels = lifePresenter.getAllMenuModels();
        List<LifeMenuModel> datas = lifePresenter.getAllDisplayMenuModels();
        if(allMenuModels == null){
            notDislplayCount = 0;
        }else{
            notDislplayCount = allMenuModels.size() - (datas==null?0:datas.size());
        }

        changeEditButton(isEdit);
        tipsView.setVisibility(isEdit?View.VISIBLE:View.GONE);

        if(isEdit){
            lvList.setPadding(lvList.getPaddingLeft(),
                getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px),
                lvList.getPaddingRight(), lvList.getPaddingBottom());
            List<LifeMenuModel> canEditList = getCanEditList(datas);
            editAdapter.updateDatas(canEditList);
            lvList.setAdapter(editAdapter);
            updateTips(canEditList.size());
        }else{
            lvList.setPadding(lvList.getPaddingLeft(),0,
                lvList.getPaddingRight(), lvList.getPaddingBottom());

            normalAdapter.updateDatas(datas);
            lvList.setAdapter(normalAdapter);
        }
    }

    /**
     * 改变编辑按钮状态
     * @param isEdit
     */
    private void changeEditButton(boolean isEdit) {
        if(isEdit){
            actionButton.setVisibility(View.INVISIBLE);
        }else{
            actionButton.setVisibility(View.VISIBLE);
        }
    }

    private void actionNormalItemClick(int pos,LifeMenuModel menuModel){
        if(pos == 0){
            pos = -1;//缴费记录
        }
        BocEventBus.getInstance().post(new LifeItemClickEvent(pos,menuModel));
        isFinishWhenPause = true;
        //getActivity().finish();
    }

    private List<LifeMenuModel> getCanEditList(List<LifeMenuModel> list){
        List<LifeMenuModel> resultList = new ArrayList<>();
        if(list == null)return resultList;

        User user = ApplicationContext.getInstance().getUser();
        if(user == null || StringUtils.isEmptyOrNull(user.getLoginName())){
            return resultList;
        }
        for(LifeMenuModel menuModel:list){
            if(user.getLoginName().equals(menuModel.getTypeId())){
                resultList.add(menuModel);
            }
            if(resultList.size()==getMaxMenuCount())break;
        }
        return resultList;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    private void updateTips(int has){
        int remainder = (getMaxMenuCount() - has);
        tvAddeNum.setText(String.valueOf(has));
        tvRemainder.setText(String.valueOf(remainder));
    }

    @Override public boolean onBack() {
        if(isEditMode){
            isEditMode = false;
            changeUI(isEditMode);
            return false;
        }
        LogUtils.d("dding","退出界面,数据回传");
        BocEventBus.getInstance().post(new LifeMenuChangeEvent(lifePresenter.getAllMenuModels()));
        return super.onBack();
    }

    @Override public void showLoading(String loading) {
        showLoadingDialog();
    }

    @Override public void endLoading() {
        closeProgressDialog();
    }

    @Override public void onItemDelSuccess(LifeMenuModel menuModel) {
        //编辑完成退出界面后修改内容统一入库
        changeUI(isEditMode);
    }

    @Override public void onLoadCommonUseEnd(boolean isSuccess, List<LifeMenuModel> list) {
        if(isSuccess){
            changeUI(isEditMode);
        }
    }

    @Override public void setPresenter(LifeContract.Presenter presenter) {

    }

    @Override public void onPause() {
        super.onPause();
        if(isFinishWhenPause){
            ActivityManager.getAppManager().finishActivity();
        }
    }

    @Override public void onDestroy() {
        super.onDestroy();
        lifecycleManager.onDestroy();
    }
}
