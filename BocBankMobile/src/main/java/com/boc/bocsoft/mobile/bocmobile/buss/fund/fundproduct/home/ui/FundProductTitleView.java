package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * 基金列表标题头，支持水平滑动
 * Created by liuzc on 2016/11/22.
 */
public class FundProductTitleView extends LinearLayout{
    public static final int TITLE_TYPE_ALL = 1; //标题类型：全部基金
    public static final int TITLE_TYPE_CURRENCY_WEALTH = 2; //标题类型：货币或者理财型
    public static final int TITLE_TYPE_SELF_SELECT = 3; //标题类型：自选型

    private Context mContext;

    private SyncHorizontalScrollView scrvAllType; //所有类型的基金的scrollview
    private LinearLayout llyCurrency; //货币型或者理财型基金的布局

    private List<SortItem> allTypeSortFieldList; //所有类型的基金的字段数据列表
    private List<LinearLayout> llyAllFieldsList = null; //所有类型的基金字段view列表

    private List<SortItem> currencySortFieldList = null; //货币类型基金的字段数据列表
    private List<LinearLayout> llyCurrencyFieldsList = null; //货币类型基金字段view列表

    private ITitleListener iListener = null;

    public FundProductTitleView(Context context) {
        this(context, null);
    }

    public FundProductTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FundProductTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setTitleClickListener(ITitleListener value){
        iListener = value;
    }

    /**
     * 初始化排序字段
     */
    private void initSortFields(){
        allTypeSortFieldList = new LinkedList<SortItem>();

        allTypeSortFieldList.add(new SortItem(mContext.getResources().getString(R.string.boc_fund_field_dwjz), "dwjz", "1", SORTTYPE.SORTTYPE_NONE));
        allTypeSortFieldList.add(new SortItem(mContext.getResources().getString(R.string.boc_fund_field_curpercentdiff), "curr_percent_diff", "3", SORTTYPE.SORTTYPE_NONE));
        allTypeSortFieldList.add(new SortItem(mContext.getResources().getString(R.string.boc_fund_field_changeofmonth), "change_of_month", "", SORTTYPE.SORTTYPE_NONE));
        allTypeSortFieldList.add(new SortItem(mContext.getResources().getString(R.string.boc_fund_field_changeofquarter), "change_of_quarter", "", SORTTYPE.SORTTYPE_NONE));
        allTypeSortFieldList.add(new SortItem(mContext.getResources().getString(R.string.boc_fund_field_changofhalfyear), "change_of_half_year", "", SORTTYPE.SORTTYPE_NONE));
        allTypeSortFieldList.add(new SortItem(mContext.getResources().getString(R.string.boc_fund_field_changeofyear), "change_of_year", "", SORTTYPE.SORTTYPE_NONE));
        allTypeSortFieldList.add(new SortItem(mContext.getResources().getString(R.string.boc_fund_field_changeofthisyear), "this_year_priceChange", "", SORTTYPE.SORTTYPE_NONE));

        currencySortFieldList = new LinkedList<SortItem>();
        currencySortFieldList.add(new SortItem(
                String.format("%s(%s)", mContext.getResources().getString(R.string.boc_fund_yield_of_wanfen),
                        mContext.getResources().getString(R.string.boc_fund_money_unit)),
                "tield_of_ten_thousand", "6", SORTTYPE.SORTTYPE_NONE));
        currencySortFieldList.add(new SortItem(
                mContext.getResources().getString(R.string.boc_fund_yield_of_week),
                "tield_of_week", "5", SORTTYPE.SORTTYPE_NONE));
    }

    private void initView(Context context){
        mContext = context;
        View.inflate(context, R.layout.boc_view_fund_list_title, this);

        scrvAllType = (SyncHorizontalScrollView)this.findViewById(R.id.shsrvListTitle);
        llyCurrency = (LinearLayout)this.findViewById(R.id.llyCurrency);

        initSortFields();
        updateAllTypeTitles();
        updateCurrencyTitles();
    }


    /**
     * 切换基金类型，更新标题
     * @param fundType: 基金类型
     */
    public void switchFundTypes(int fundType){
        if(fundType == TITLE_TYPE_CURRENCY_WEALTH){
            llyCurrency.setVisibility(View.VISIBLE);
            scrvAllType.setVisibility(View.GONE);
        } else if(fundType == TITLE_TYPE_SELF_SELECT){
            llyCurrency.setVisibility(View.GONE);
            scrvAllType.setVisibility(View.VISIBLE);
            setSortIconVisible(false);
        } else{
            llyCurrency.setVisibility(View.GONE);
            scrvAllType.setVisibility(View.VISIBLE);
            setSortIconVisible(true);
        }
    }

    /**
     * 设置排序图标是否可见（自选型不能排序）
     * @param isVisible：是否可见
     */
    private void setSortIconVisible(boolean isVisible){
        for(LinearLayout llyCurTitle : llyAllFieldsList){
            ImageView imvCurrent = (ImageView) llyCurTitle.findViewById(R.id.imvSort);
            if(isVisible){
                imvCurrent.setVisibility(View.VISIBLE);
            } else{
                imvCurrent.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 更新所有类型基金的标题view
     */
    private void updateAllTypeTitles(){
        //如果页面元素为空，则创建
        if(llyAllFieldsList == null){
            llyAllFieldsList = new LinkedList<LinearLayout>();
            LinearLayout llySortFields = (LinearLayout)this.findViewById(R.id.llySortFields);
            for(int i = 0; i < allTypeSortFieldList.size(); i ++){
                LinearLayout llySortItem = (LinearLayout)View.inflate(mContext,
                        R.layout.boc_view_fund_list_title_sort_field_item, null);
                final SortItem curSortItemData = allTypeSortFieldList.get(i);
                llySortItem.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        onClickItem(curSortItemData);
                    }
                });
                llyAllFieldsList.add(llySortItem);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ResUtils.dip2px(mContext, 100),
                        ResUtils.dip2px(mContext, 30));
                params.gravity = Gravity.CENTER;
                llySortFields.addView(llySortItem, params);
            }
        }

        //更新数据
        for(int i = 0; i < allTypeSortFieldList.size(); i ++){
            LinearLayout llySortItem = llyAllFieldsList.get(i);
            SortItem curSortItemData = allTypeSortFieldList.get(i);
            TextView tvFieldName = (TextView)llySortItem.findViewById(R.id.tvFieldName);
            ImageView imvSort = (ImageView)llySortItem.findViewById(R.id.imvSort);

            //更新排序文字
            tvFieldName.setText(curSortItemData.getFieldName());

            //更新排序图标
            SORTTYPE curSortType = curSortItemData.getSortType();
            if(curSortType == SORTTYPE.SORTTYPE_NONE){
                imvSort.setBackgroundResource(R.drawable.sort_def);
            }
            else if(curSortType == SORTTYPE.SORTTYPE_ASC){
                imvSort.setBackgroundResource(R.drawable.sort_asc);
            }
            else{
                imvSort.setBackgroundResource(R.drawable.sort_desc);
            }
        }
    }

    /**
     * 更新货币类型基金的标题view
     */
    private void updateCurrencyTitles(){
        //如果页面元素为空，则创建
        if(llyCurrencyFieldsList == null){
            llyCurrencyFieldsList = new LinkedList<LinearLayout>();
            for(int i = 0; i < currencySortFieldList.size(); i ++){
                LinearLayout llySortItem = (LinearLayout)View.inflate(mContext,
                        R.layout.boc_view_fund_list_title_sort_field_item, null);
                final SortItem curSortItemData = currencySortFieldList.get(i);
                llySortItem.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        onClickItem(curSortItemData);
                    }
                });
                llyCurrencyFieldsList.add(llySortItem);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER;
                params.weight = 1;
                llyCurrency.addView(llySortItem, params);
            }
        }

        //更新数据
        for(int i = 0; i < currencySortFieldList.size(); i ++){
            LinearLayout llySortItem = llyCurrencyFieldsList.get(i);
            SortItem curSortItemData = currencySortFieldList.get(i);
            TextView tvFieldName = (TextView)llySortItem.findViewById(R.id.tvFieldName);
            ImageView imvSort = (ImageView)llySortItem.findViewById(R.id.imvSort);

            //更新排序文字
            tvFieldName.setText(curSortItemData.getFieldName());

            //更新排序图标
            SORTTYPE curSortType = curSortItemData.getSortType();
            if(curSortType == SORTTYPE.SORTTYPE_NONE){
                imvSort.setBackgroundResource(R.drawable.sort_def);
            }
            else if(curSortType == SORTTYPE.SORTTYPE_ASC){
                imvSort.setBackgroundResource(R.drawable.sort_asc);
            }
            else{
                imvSort.setBackgroundResource(R.drawable.sort_desc);
            }
        }
    }

    private void onClickItem(SortItem item){
        SORTTYPE curSortType = item.getSortType();
        cleanSortFields();
        //排序方式按方式： 不排序-降序-升序-不排序
        if(curSortType == SORTTYPE.SORTTYPE_NONE){
            item.setSortType(SORTTYPE.SORTTYPE_DESC);
        }
        else if(curSortType == SORTTYPE.SORTTYPE_DESC){
            item.setSortType(SORTTYPE.SORTTYPE_ASC);
        }
        else{
            item.setSortType(SORTTYPE.SORTTYPE_NONE);
        }

        if(iListener != null){
            if(item.getSortType() == SORTTYPE.SORTTYPE_NONE){
                //无排序
                iListener.onClickItem("", "", "", "");
            }
            else{
                //把排序方式传出
                iListener.onClickItem(item.getWfssFiledCode(), getSortTypeDesc(item.getSortType(), true),
                    item.getBiFiledCode(), getSortTypeDesc(item.getSortType(), false));
            }
        }
        updateAllTypeTitles();
    }

    /**
     * 获取WFSS或者BI的排序方式描述，默认为WFSS的
     * @param sortType
     * @param isWFSS  是否为WFSS
     * @return
     */
    private String getSortTypeDesc(SORTTYPE sortType, boolean isWFSS){
        if(sortType == SORTTYPE.SORTTYPE_ASC){
            return  isWFSS ? "asc": "1";
        }
        else if(sortType == SORTTYPE.SORTTYPE_DESC){
            return isWFSS ? "desc": "2";
        }
        else{
            return "";
        }
    }

    /**
     * 清除所有排序
     */
    private void cleanSortFields(){
        if(allTypeSortFieldList != null){
            for(int i = 0; i < allTypeSortFieldList.size(); i ++){
                SortItem sortItem = allTypeSortFieldList.get(i);
                sortItem.setSortType(SORTTYPE.SORTTYPE_NONE);
            }
        }
    }

    class SortItem {
        private String fieldName; //排序字段名
        private String wfssFiledCode; //WFSS排序字段码
        private String biFiledCode; //bi排序字段码
        private SORTTYPE sortType; //排序方式

        public SortItem(String name, String code, String biCode, SORTTYPE type){
            fieldName = name;
            wfssFiledCode = code;
            biFiledCode = biCode;
            sortType = type;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getWfssFiledCode() {
            return wfssFiledCode;
        }

        public void setWfssFiledCode(String wfssFiledCode) {
            this.wfssFiledCode = wfssFiledCode;
        }

        public SORTTYPE getSortType() {
            return sortType;
        }

        public void setSortType(SORTTYPE sortType) {
            this.sortType = sortType;
        }

        public String getBiFiledCode() {
            return biFiledCode;
        }

        public void setBiFiledCode(String biFiledCode) {
            this.biFiledCode = biFiledCode;
        }
    }

    //排序方式
    enum SORTTYPE{
        SORTTYPE_ASC,
        SORTTYPE_DESC,
        SORTTYPE_NONE
    }

    public interface ITitleListener{
        /**
         * 点击排序字段
         * @param wfssSortField  四方排序字段
         * @param wfssSortType 四方排序方式
         * @param biSortField  BI排序字段
         * @param biSortType BI排序方式
         */
        void onClickItem(String wfssSortField, String wfssSortType, String biSortField, String biSortType);
    }
}
