package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui;

/**
 * Created by liuzc on 2016/11/20.
 */
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class FundTypeSelectView extends LinearLayout{
    private Context mContext = null;
    private List<FundTypeItem> fundTypeList = null; //基金类型数据

    private LinearLayout llyFirstLine = null; //第一行
    private LinearLayout llySecondLine = null; //第二行
    private LinearLayout llyThirdLine = null; //第三行

    private ImageView imvArrow = null; //展开、收起箭头图标

    private int curSelItemIndex = 0; //当前选中元素的索引值

    private IFundTypeSelectListener iListener = null;

    public FundTypeSelectView(Context context) {
        this(context, null);
    }

    public FundTypeSelectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FundTypeSelectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    public void setFundTypeSelListener(IFundTypeSelectListener listener){
        iListener = listener;
    }

    /**
     * 初始化数据
     */
    private void initFundTypeListData(){
        fundTypeList = new LinkedList<FundTypeItem>();

        fundTypeList.add(new FundTypeItem(mContext.getResources().getString(R.string.boc_fund_type_all), "00", 1, true));
        fundTypeList.add(new FundTypeItem(mContext.getResources().getString(R.string.boc_fund_type_self_select), "10", 1, false));
        fundTypeList.add(new FundTypeItem(mContext.getResources().getString(R.string.boc_fund_type_stock), "07", 1, false));
        fundTypeList.add(new FundTypeItem(mContext.getResources().getString(R.string.boc_fund_type_bond), "08", 1, false));
        fundTypeList.add(new FundTypeItem(mContext.getResources().getString(R.string.boc_fund_type_currency), "06", 1, false));
        fundTypeList.add(new FundTypeItem(mContext.getResources().getString(R.string.boc_fund_type_mixed), "09", 2, false));
        fundTypeList.add(new FundTypeItem(mContext.getResources().getString(R.string.boc_fund_type_qdii), "02", 2, false));
        fundTypeList.add(new FundTypeItem(mContext.getResources().getString(R.string.boc_fund_type_principal_guaranteed), "04", 2, false));
        fundTypeList.add(new FundTypeItem(mContext.getResources().getString(R.string.boc_fund_type_exponential), "05", 2, false));
        fundTypeList.add(new FundTypeItem(mContext.getResources().getString(R.string.boc_fund_type_financial), "01", 2, false));
        fundTypeList.add(new FundTypeItem(mContext.getResources().getString(R.string.boc_fund_type_etf), "03", 3, false));
        fundTypeList.add(new FundTypeItem(mContext.getResources().getString(R.string.boc_fund_type_info_manage_plan), "11", 3, false));
        fundTypeList.add(new FundTypeItem(mContext.getResources().getString(R.string.boc_fund_type_trust_product), "12", 3, false));
        fundTypeList.add(new FundTypeItem(mContext.getResources().getString(R.string.boc_fund_type_other), "13",3, false));
    }

    /**
     * 初始化页面元素
     * @param context
     */
    private void initViews(Context context){
        mContext = context;
        initFundTypeListData();

        View view = View.inflate(context, R.layout.boc_view_fund_type_select, this);

        //由已有数据构造页面元素
        llyFirstLine = (LinearLayout)view.findViewById(R.id.llyFirstLine);
        llySecondLine = (LinearLayout)view.findViewById(R.id.llySecondLine);
        llyThirdLine = (LinearLayout)view.findViewById(R.id.llyThirdLine);

        for(int i = 0; i < fundTypeList.size(); i ++){
            FundTypeItem item = fundTypeList.get(i);
            LinearLayout llyDescLayout = llyFirstLine;

            if(item.getLineNumber() == 2){
                llyDescLayout = llySecondLine;
            }
            else if(item.getLineNumber() == 3){
                llyDescLayout = llyThirdLine;
            }
            addFundTypeItem(context, item, llyDescLayout, item.isbSelected());
        }

        //展开、收起箭头的造作
        LinearLayout llyArrow = (LinearLayout)view.findViewById(R.id.llyArrow);
        llyArrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickArrow();
            }
        });
        imvArrow = (ImageView)view.findViewById(R.id.imvArrowDown);
    }

    /**
     * 点击展开、收起箭头事件
     */
    private void onClickArrow(){
        if(isLayoutOpen()){
            //当前处于展开状态，应该收起
            imvArrow.setBackgroundResource(R.drawable.fund_type_arrow_down);
            llySecondLine.setVisibility(View.GONE);
            llyThirdLine.setVisibility(View.GONE);
        }
        else{
            imvArrow.setBackgroundResource(R.drawable.fund_type_arrow_up);
            llySecondLine.setVisibility(View.VISIBLE);
            llyThirdLine.setVisibility(View.VISIBLE);
        }

        updateViews();
    }

    /**
     * 根据选中状态，更新布局
     */
    private void updateViews(){
        llyFirstLine.removeAllViews();
        llySecondLine.removeAllViews();
        llyThirdLine.removeAllViews();

        //展开状态，选中的原色标红显示
        if(isLayoutOpen()){
            for(int i = 0; i < fundTypeList.size(); i ++){
                FundTypeItem item = fundTypeList.get(i);
                LinearLayout llyDescLayout = llyFirstLine;

                if(item.getLineNumber() == 2){
                    llyDescLayout = llySecondLine;
                }
                else if(item.getLineNumber() == 3){
                    llyDescLayout = llyThirdLine;
                }
                addFundTypeItem(mContext, item, llyDescLayout, item.isbSelected());
            }
        }
        //收起状态，如果选中的元素位于非第一行，则第一行的“货币型”显示成当前选中名称
        else{
            FundTypeItem selItem = fundTypeList.get(curSelItemIndex);
            if(selItem.getLineNumber() != 1){
                for(int i = 0; i < fundTypeList.size(); i ++){
                    FundTypeItem item = fundTypeList.get(i);
                    LinearLayout llyDescLayout = llyFirstLine;

                    if(item.getLineNumber() == 1){
                        if(!StringUtils.isEmptyOrNull(item.getName()) && item.getName().equals(mContext.getResources().getString(R.string.boc_fund_type_currency))){
                            addFundTypeItem(mContext, selItem, llyDescLayout, true);
                        }
                        else{
                            addFundTypeItem(mContext, item, llyDescLayout, item.isbSelected());
                        }
                    }
                }
            }
            else{
                for(int i = 0; i < fundTypeList.size(); i ++){
                    FundTypeItem item = fundTypeList.get(i);
                    LinearLayout llyDescLayout = llyFirstLine;

                    if(item.getLineNumber() == 1){
                        addFundTypeItem(mContext, item, llyDescLayout, item.isbSelected());
                    }
                }
            }
        }
    }

    /**
     *是否处于展开状态
     * @return
     */
    private boolean isLayoutOpen(){
        return (llySecondLine.getVisibility() == View.VISIBLE);
    }

    /**
     * 向布局中添加一个基金类型元素
     * @param context
     * @param item
     * @param llyParent
     * @param bSel
     */
    private void addFundTypeItem(Context context, final FundTypeItem item, LinearLayout llyParent, boolean bSel){
        LinearLayout addView = (LinearLayout)View.inflate(context, R.layout.boc_view_fund_type_select_item, null);
        addView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onSelectItem(item);
            }
        });

        TextView tvValue = (TextView)addView.findViewById(R.id.tvDesc);
        ImageView imvLine = (ImageView)addView.findViewById(R.id.imvLine);

        tvValue.setText(item.getName());

        if(bSel){
            tvValue.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            imvLine.setBackgroundColor(getResources().getColor(R.color.boc_text_color_red));
        }
        else{
            tvValue.setTextColor(context.getResources().getColor(R.color.boc_text_color_common_gray));
			imvLine.setBackgroundColor(context.getResources().getColor(R.color.boc_common_cell_color));
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
        params.weight = getFundTypeItemWeight(item.getName());

        llyParent.addView(addView, params);
    }

    /**
     * 选中元素处理逻辑
     * @param item
     */
    private void onSelectItem(FundTypeItem item){
        FundTypeItem selItem = fundTypeList.get(curSelItemIndex);
        if(item != selItem){
            imvArrow.setBackgroundResource(R.drawable.fund_type_arrow_down);
            llySecondLine.setVisibility(View.GONE);
            llyThirdLine.setVisibility(View.GONE);

            selItem.setbSelected(false);
            item.setbSelected(true);

            for(int i = 0; i < fundTypeList.size(); i ++){
                if(fundTypeList.get(i) == item){
                    curSelItemIndex = i;
                    break;
                }
            }

            updateViews();

            //点击监听回调
            if(iListener != null){
                iListener.onSelectItem(item.getCode());
            }
        }
    }

    /**
     * 获取基金类型元素在其所在行的比重
     * @return
     */
    private int getFundTypeItemWeight(String name){
        if(name != null && name.equals(mContext.getResources().getString(R.string.boc_fund_type_info_manage_plan))){
            return 2;
        }
        return 1;
    }

    class FundTypeItem{
        private String name = null; //名称
        private String code = null; //代码
        private int lineNumber = 1; //显示在第几行
        private boolean bSelected = false; //是否选中

        public FundTypeItem(String fName, String fCode, int fLineNumber, boolean isSelected){
            name = fName;
            code = fCode;
            lineNumber = fLineNumber;
            bSelected = isSelected;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }

        public boolean isbSelected() {
            return bSelected;
        }

        public void setbSelected(boolean bSelected) {
            this.bSelected = bSelected;
        }
    }

    //选择基金类型监听器
    public interface IFundTypeSelectListener{
        void onSelectItem(String fundTypeCode);
    }
}
