package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.FessBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.FundBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.GoldBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.util.InvestUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.system.common.dao.AppStateDao;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.dao.HomeInvestDao;

import com.boc.bocsoft.mobile.bocmobile.buss.system.home.dao.InvestItemBO;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.InvestType;
import com.boc.bocsoft.mobile.framework.utils.PublicConst;
import com.boc.bocsoft.mobile.framework.utils.ViewFinder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 投诉模块编辑页面 boc_fragment_home_invest_select
 * Created by lxw on 2016/8/11 0011.
 */
public class EditInvestModuleListViewFragment extends BussFragment {

    private static final String PARAMS_FUND_LIST = "funds";
    private static final String PARAMS_GOLD_LIST = "golds";
    private static final String PARAMS_FESS_LIST = "fesses";
    private final static String APP_KEY_HOME_INVEST = "home_invest";
    private View mRoot;
    // 基金
    private LinearLayout fundsListView;
    // 贵金属
    private LinearLayout goldsListView;
    // 结购汇
    private LinearLayout fessesListView;


    private HomeInvestDao homeInvestDao;
    private AppStateDao appStateDao;

    private OnChangedListener onChangedListener;
    private boolean isChanged = false;

    private ArrayList<FundBean> funds;
    private ArrayList<GoldBean> golds;
    private ArrayList<FessBean> fesses;

    // 添加基金
    private TextView addFundAction;
    // 添加贵金属
    private TextView addGoldAction;
    // 添加结购汇
    private TextView addFessAction;
    private TextView tv_added_num;
    private TextView tv_remainder;

    protected View onCreateView(LayoutInflater inflater) {
        mRoot = inflater.inflate(R.layout.boc_fragment_home_invest_select, null);
        return mRoot;
    }

    public static EditInvestModuleListViewFragment newInstance(ArrayList<FundBean> funds, ArrayList<GoldBean> glods, ArrayList<FessBean> fesses) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PARAMS_FUND_LIST, funds);
        bundle.putParcelableArrayList(PARAMS_GOLD_LIST, glods);
        bundle.putParcelableArrayList(PARAMS_FESS_LIST, fesses);
        EditInvestModuleListViewFragment fragment = new EditInvestModuleListViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initView() {
        homeInvestDao = new HomeInvestDao();
        appStateDao = new AppStateDao();
        fundsListView = mViewFinder.find(R.id.lv_fund_list);
        goldsListView = mViewFinder.find(R.id.lv_gold_list);
        fessesListView = mViewFinder.find(R.id.lv_fess_list);

        addFundAction = mViewFinder.find(R.id.addFundAction);
        addGoldAction = mViewFinder.find(R.id.addGoldAction);
        addFessAction = mViewFinder.find(R.id.addFessAction);
        tv_added_num = mViewFinder.find(R.id.tv_added_num);
        tv_remainder = mViewFinder.find(R.id.tv_remainder);

        this.mTitleBarView.setTitle("定制");
        mTitleBarView.setRightImgBtnVisible(false);

    }

    private ArrayList<InvestItem> performFundList;//界面显示的基金集合
    private ArrayList<InvestItem> performGoldsList;//界面显示的集合
    private ArrayList<InvestItem> performFessesList;//界面显示的基金集合

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        funds = bundle.getParcelableArrayList(PARAMS_FUND_LIST);
        golds = bundle.getParcelableArrayList(PARAMS_GOLD_LIST);
        fesses = bundle.getParcelableArrayList(PARAMS_FESS_LIST);
        performFundList = createFundInvestItem(funds);
        performGoldsList = createGoldInvestItem(golds);
        performFessesList = createFessInvestItem(fesses);

        updateListData();
        setInvestNumInfo();
    }

    /**
     * 更新列表
     */
    private void updateListData(){
        fundsListView.removeAllViews();
        fessesListView.removeAllViews();
        goldsListView.removeAllViews();

        if (performFundList != null && performFundList.size() > 0){
            for (int i = 0; i < performFundList.size(); i++){
                addViewToLinearLayout(fundsListView, performFundList.get(i), i);
            }

        }

        if (performGoldsList != null && performGoldsList.size() > 0) {
            for (int i = 0; i < performGoldsList.size(); i++){
                addViewToLinearLayout(goldsListView, performGoldsList.get(i), i);
            }
        }

        if (performFessesList != null && performFessesList.size() > 0) {
            for (int i = 0; i < performFessesList.size(); i++){
                addViewToLinearLayout(fessesListView, performFessesList.get(i), i);
            }

        }

    }

    /**
     * 添加view到LinerLayout
     * @param ll
     * @param itemData
     */
    private void addViewToLinearLayout(final LinearLayout ll, final InvestItem itemData, final int postion){

        View item = View.inflate(mContext,
                R.layout.boc_item_home_edit_invest_item, null);
        ViewFinder finder = new ViewFinder(item);
        TextView tv = finder.find(R.id.tvTitle);
        ImageView iv = finder.find(R.id.ivIcon);
        tv.setText(itemData.getTitle());
        iv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                removeItem(itemData, postion);
                setInvestNumInfo();
                updateData();
            }
        });
        ll.addView(item);
    }

    /**
     *
     */
    private void removeItem(InvestItem investItem, int postion){
        if (investItem != null) {
            InvestType type =investItem.getType();
            if (type == InvestType.fund) {
                performFundList.remove(postion);
            } else if (type == InvestType.fess) {
                performFessesList.remove(postion);
            } else if (type == InvestType.gold) {
                performGoldsList.remove(postion);
            }
            updateListData();
        }
    }

    /**
     * 设置投资选择的数目提示
     */
    public void setInvestNumInfo() {
        tv_added_num.setText(getSelectedInvestNum() + "");
        tv_remainder.setText(ApplicationConst.MAX_INVEST_NUM - getSelectedInvestNum() + "");

    }

    private int getSelectedInvestNum() {
        return performFundList.size()
                + performGoldsList.size() + performFessesList.size();
    }

    private final int resultCodeGold = 1;
    private final int resultCodeFess = 2;
    private final int resultCodeFund = 3;

    private List<InvestItemBO> getSaveInvestList() {

        List<InvestItemBO> list = new ArrayList<>();
        list.addAll(getInvestList(performGoldsList));
        list.addAll(getInvestList(performFundList));
        list.addAll(getInvestList(performFessesList));
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setOrderId(i);
        }
        return list;
    }

    private List<InvestItemBO> getInvestList(List<InvestItem> investItems) {
        List<InvestItemBO> saveList = new ArrayList<>();
        if (investItems == null || investItems.size() == 0){
            return saveList;
        }
        List<InvestItem> list = investItems;
        for (InvestItem investItem : list) {
            InvestItemBO saveItem =
                    new InvestItemBO();
            saveItem.setInvestId(investItem.getSaveId());
            saveItem.setInvestName(investItem.getSaveName());
            saveItem.setType(investItem.getType());
            saveList.add(saveItem);
        }
        return saveList;
    }

    @Override
    public void setListener() {
        addFundAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (canAddInvest()) {
                    FundListViewFragment fundFragment = new FundListViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ApplicationConst.SELECTED_INVEST_FUND, performFundList);
                    fundFragment.setArguments(bundle);
                    startForResult(fundFragment, resultCodeFund);
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.boc_invest_cant_add, "5"), Toast.LENGTH_SHORT).show();
                }
            }
        });

        addGoldAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (canAddInvest()) {
                    GoldListViewFragment goldFragment = new GoldListViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ApplicationConst.SELECTED_INVEST_GOLD, performGoldsList);
                    goldFragment.setArguments(bundle);
                    startForResult(goldFragment, resultCodeGold);
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.boc_invest_cant_add, "5"), Toast.LENGTH_SHORT).show();
                }
            }
        });

        addFessAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (canAddInvest()) {
                    FessListViewFragment fessFragment = new FessListViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ApplicationConst.SELECTED_INVEST_FESS, performFessesList);
                    fessFragment.setArguments(bundle);
                    startForResult(fessFragment, resultCodeFess);
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.boc_invest_cant_add, "5"), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onBack() {

        if (isChanged) {
            if (onChangedListener != null) {
                onChangedListener.onChanged();
            }
        }
        return true;
    }

    @Override
    public boolean onBackPress(){
        if (isChanged) {
            if (onChangedListener != null) {
                onChangedListener.onChanged();
            }
        }

        return false;
    }

    /**
     * 添加基金数量是否达到上限
     */
    private boolean canAddInvest() {
        int investSize = performFundList.size()
                + performGoldsList.size() + performFessesList.size();
        if (investSize < ApplicationConst.MAX_INVEST_NUM) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    /**
     * 生成基金的list item
     *
     * @param funds
     * @return
     */
    private ArrayList<InvestItem> createFundInvestItem(List<FundBean> funds) {
        ArrayList<InvestItem> result = new ArrayList<>();
        if (funds != null) {
            for (final FundBean fund : funds) {
                InvestItem item = new FundInvestItem(fund);
                result.add(item);
            }
        }

        return result;
    }

    /**
     * 生成贵金属的list item
     *
     * @param golds
     * @return
     */
    private ArrayList<InvestItem> createGoldInvestItem(List<GoldBean> golds) {
        ArrayList<InvestItem> result = new ArrayList<>();
        if (golds != null) {
            for (final GoldBean gold : golds) {
                InvestItem item = new GoldInvestItem(gold);
                result.add(item);
            }
        }

        return result;
    }

    /**
     * 生成结汇购汇的list item
     *
     * @param fesses
     * @return
     */
    private ArrayList<InvestItem> createFessInvestItem(List<FessBean> fesses) {
        ArrayList<InvestItem> result = new ArrayList<>();
        if (fesses != null) {
            for (final FessBean fess : fesses) {
                InvestItem item = new FessInvestItem(fess);
                result.add(item);
            }
        }
        return result;
    }

     abstract class InvestItem<T>{
        public String title;
        T data;

        /**
         * 界面展示的数据
         */
        public abstract String getTitle();

        /**
         * 获取保存数据库的name
         */
        public abstract String getSaveName();

        /**
         * 获取保存数据库的id
         */
        public abstract String getSaveId();

        public abstract InvestType getType();

        public void setData(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode == Activity.RESULT_OK) {
            Parcelable parcelable = data.getParcelable(PublicConst.RESULT_DATA);
            if (requestCode == resultCodeGold) {
                performGoldData((GoldBean) parcelable);
            } else if (requestCode == resultCodeFess) {
                performFessData((FessBean) parcelable);
            } else if (requestCode == resultCodeFund) {
                performFundData((FundBean) parcelable);
            }
            setInvestNumInfo();
        }
        // 更新数据
        updateListData();
        updateData();
        super.onFragmentResult(requestCode, resultCode, data);
    }

    private void performGoldData(final GoldBean bean) {
        InvestItem item = new InvestItem() {
            @Override
            public String getTitle() {
                return PublicCodeUtils.getGoldCurrencyCode(mContext, bean.getSourceCurrencyCode())
                        + "/" + PublicCodeUtils.getGoldCurrencyCode(mContext, bean.getTargetCurrencyCode());
            }

            @Override
            public String getSaveName() {
                return PublicCodeUtils.getGoldCurrencyCode(mContext, bean.getSourceCurrencyCode())
                        + "/" + PublicCodeUtils.getGoldCurrencyCode(mContext, bean.getTargetCurrencyCode());
            }

            @Override
            public String getSaveId() {
                return bean.getSourceCurrencyCode()
                        + "-" + bean.getTargetCurrencyCode();
            }

            @Override
            public InvestType getType() {
                return InvestType.gold;
            }
        };

        performGoldsList.add(item);
        updateListData();
    }

    private void performFessData(final FessBean bean) {
        InvestItem item = new InvestItem() {
            @Override
            public String getTitle() {
                return PublicCodeUtils.getCurrencyWithLetter(mContext, bean.getCurCode());
            }

            @Override
            public String getSaveId() {
                return bean.getCurCode();
            }

            @Override
            public InvestType getType() {
                return InvestType.fess;
            }

            @Override
            public String getSaveName() {
                return PublicCodeUtils.getCurrencyWithLetter(mContext, bean.getCurCode());
            }
        };
        performFessesList.add(item);
        updateListData();
    }

    private void performFundData(final FundBean bean) {
        InvestItem item = new InvestItem() {
            @Override
            public String getTitle() {
                // return bean.getFundName() + "（" + bean.getFundCode() + "）";
                return bean.getFundName();
            }

            @Override
            public String getSaveName() {
                return bean.getFundName();
            }

            @Override
            public String getSaveId() {
                return bean.getFundCode();
            }

            @Override
            public InvestType getType() {
                return InvestType.fund;
            }
        };
        performFundList.add(item);
        updateListData();
    }

    public void setOnChangedListener(OnChangedListener onChangedListener) {
        this.onChangedListener = onChangedListener;
    }

    /**
     * 更新数据
     */
    public void updateData() {
        try {
            homeInvestDao.updateInvestList(getSaveInvestList());
            appStateDao.updateAppState(APP_KEY_HOME_INVEST);
            isChanged = true;

        } catch (Exception e) {
            showToast(getString(R.string.boc_order_fail));
            return;
        }
    }

    public class FundInvestItem extends InvestItem implements Parcelable{

        FundBean fund;

        protected FundInvestItem(FundBean fundBean){
            this.fund = fundBean;
        }

        protected FundInvestItem(Parcel in) {
            fund = in.readParcelable(FundBean.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(fund, flags);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public final Creator<FundInvestItem> CREATOR = new Creator<FundInvestItem>() {
            @Override
            public FundInvestItem createFromParcel(Parcel in) {
                return new FundInvestItem(in);
            }

            @Override
            public FundInvestItem[] newArray(int size) {
                return new FundInvestItem[size];
            }
        };

        @Override
        public String getTitle() {
            return fund.getFundName();
        }

        @Override
        public String getSaveName() {
            return fund.getFundName();
        }

        @Override
        public String getSaveId() {
            return fund.getFundCode();
        }

        @Override
        public InvestType getType() {
            return InvestType.fund;
        }
    }

    public  class GoldInvestItem extends InvestItem implements Parcelable{
        private GoldBean gold;
        GoldInvestItem(GoldBean gold){
            this.gold = gold;
        }

        protected GoldInvestItem(Parcel in) {
            gold = in.readParcelable(GoldBean.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(gold, flags);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public  final Creator<GoldInvestItem> CREATOR = new Creator<GoldInvestItem>() {
            @Override
            public GoldInvestItem createFromParcel(Parcel in) {
                return new GoldInvestItem(in);
            }

            @Override
            public GoldInvestItem[] newArray(int size) {
                return new GoldInvestItem[size];
            }
        };

        @Override
        public String getTitle() {
            return InvestUtils.getCurrencyPairName(mContext, gold.getSourceCurrencyCode(), gold.getTargetCurrencyCode());
        }

        @Override
        public String getSaveName() {
            return InvestUtils.getCurrencyPairName(mContext, gold.getSourceCurrencyCode(), gold.getTargetCurrencyCode());
        }

        @Override
        public String getSaveId() {
            return InvestUtils.getCurrencyPair(gold.getSourceCurrencyCode(), gold.getTargetCurrencyCode());
        }

        @Override
        public InvestType getType() {
            return InvestType.gold;
        }
    }

    public class FessInvestItem extends InvestItem implements Parcelable{

        FessBean fess;

        protected FessInvestItem(FessBean fessBean){
            this.fess = fessBean;
        }
        protected FessInvestItem(Parcel in) {
            fess = in.readParcelable(FessBean.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(fess, flags);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public final Creator<FessInvestItem> CREATOR = new Creator<FessInvestItem>() {
            @Override
            public FessInvestItem createFromParcel(Parcel in) {
                return new FessInvestItem(in);
            }

            @Override
            public FessInvestItem[] newArray(int size) {
                return new FessInvestItem[size];
            }
        };

        @Override
        public String getTitle() {
            return PublicCodeUtils.getCurrencyWithLetter(mContext, fess.getCurCode());
        }

        @Override
        public String getSaveId() {
            return fess.getCurCode();
        }

        @Override
        public InvestType getType() {
            return InvestType.fess;
        }

        @Override
        public String getSaveName() {
            return PublicCodeUtils.getCurrencyWithLetter(mContext, fess.getCurCode());
        }
    }
}
