package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.index.QuickIndexBar;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.FundCompanyListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.adapter.FundCompanyAdapter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;
/**
 * 基金公司选择布局
 * Created by lzc4524 on 2016/12/15.
 */
public class FundCompanySelectView extends LinearLayout implements ListView.OnItemClickListener{
    private Context mContext = null;

    private TitleBarView titleView; // 标题
    protected ListView lsvCompanyList; //listview,显示基金公司列表
    protected QuickIndexBar quickIndexBar;  //列表的索引， A、B、C..

    private FundCompanyAdapter fundCompAdapater = null;

    private FundCompanyListViewModel viewModel = null; //基金公司model

    private IFundCompSelListener iListener = null; //事件监听接口

    public FundCompanySelectView(Context context) {
        this(context, null);
    }

    public FundCompanySelectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FundCompanySelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        mContext = context;
        View.inflate(context, R.layout.boc_view_fund_colist, this);

        titleView = (TitleBarView) this.findViewById(R.id.title_view);
        titleView.setStyle(R.style.titlebar_base_white);
        titleView.setTitle(R.string.boc_fund_sel_company);
        titleView.setRightImgBtnVisible(false);
        titleView.setLeftButtonOnClickLinster(new View.OnClickListener() {// 标题返回键
            @Override
            public void onClick(View v) {
                if(iListener != null){
                    iListener.onClickBack();
                }
            }
        });
        lsvCompanyList = (ListView) this.findViewById(R.id.lsvCompanyList);
        lsvCompanyList.setOnItemClickListener(this);
        quickIndexBar = (QuickIndexBar) this.findViewById(R.id.quickIndexBar);
        quickIndexBar.setOnTouchLetterListener(new QuickIndexBar.OnTouchLetterListener(){

            @Override
            public void onTouchLetter(String word) {
                scrollToWord(word);
            }

            @Override
            public void onTouchUp() {

            }
        });
    }

    /**
     * 更新数据，从而重绘页面
     * @param viewModel
     */
    public void updateViewModel(FundCompanyListViewModel viewModel) {
        if(fundCompAdapater == null){
            fundCompAdapater = new FundCompanyAdapter(getContext());
            lsvCompanyList.setAdapter(fundCompAdapater);
        }
        this.viewModel = viewModel;
        fundCompAdapater.setViewModel(viewModel);
        fundCompAdapater.notifyDataSetChanged();
    }

    private void scrollToWord(String word){
        int destIndex = 0; //目标listview的item的索引
        //选择的不是“全部”
        if(!word.equals(mContext.getResources().getString(R.string.boc_fund_trans_type_all))){
            for(int i = 0; i < viewModel.getList().size(); i ++){
                String curLetter = viewModel.getList().get(i).getFundCompanyLetterTitle();
                if(StringUtils.isEmptyOrNull(curLetter)){
                    continue;
                }
                if(curLetter.compareTo(word) < 0){
                    destIndex = i;
                }
                else if(curLetter.compareTo(word) == 0){
                    destIndex = i;
                    break;
                }
            }
        }
        lsvCompanyList.setSelection(destIndex);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(viewModel == null || viewModel.getList() == null || viewModel.getList().size() <= position){
            return;
        }

        FundCompanyListViewModel.ListBean bean = viewModel.getList().get(position);
        String companyName = bean.getFundCompanyName();

        if(!StringUtils.isEmptyOrNull(companyName) && iListener != null){
            iListener.onSelectCompany(companyName, bean.getFundCompanyCode());
        }
    }

    public void setiListener(IFundCompSelListener iListener) {
        this.iListener = iListener;
    }

    public interface IFundCompSelListener{
        void onClickBack(); //点击左上角返回
        void onSelectCompany(String companyName, String companyCode); //选择基金公司
    }
}
