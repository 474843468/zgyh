package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.adapter.OverseasNounAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.model.OverseasNounModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xwg on 16/11/1 08:47
 * 出国攻略内容界面 攻略基础类
 */
public abstract class BaseNounFragment extends BussFragment {
    private View mRootView;
    private ListView lvOverseasNoun;
    private List<OverseasNounModel> mNounModleList;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {

        mRootView = View.inflate(mContext, R.layout.boc_fragment_base_noun, null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        lvOverseasNoun=(ListView)mRootView.findViewById(R.id.lv_overseas_noun);
    }

    @Override
    public void initData() {
        super.initData();

        getNounModelList();

        OverseasNounAdapter adapter=new OverseasNounAdapter(mContext);
        adapter.setDatas(mNounModleList);
        lvOverseasNoun.setAdapter(adapter);
    }


    /**
    *  获取OverseasNounModel list
    */
    private void getNounModelList() {
        mNounModleList=new ArrayList<OverseasNounModel>();
        for (int i=0;i<getContentArr().length;i++){
            if (getTitleArr()!=null&&getTitleArr().length>0){
              mNounModleList.add( getOverseasNounModel(getTitleArr()[i],getContentArr()[i]));
            }else{
                mNounModleList.add( getOverseasNounModel("",getContentArr()[i]));
            }
        }
    }


    /**
     * 组装entity
    */
    private OverseasNounModel getOverseasNounModel(String title, String content) {
        OverseasNounModel nounModel=new OverseasNounModel();
        nounModel.setTitle(title);
        nounModel.setContent(content);
        return nounModel;
    }


    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    /**
     * 获取列表标题
    */
    public abstract String[] getTitleArr();
    /**
     * 获取列表内容
    */
    public abstract String[] getContentArr();


}
