package com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem.ui;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.bocmobile.base.widget.PortfolioProductInfoView.PortfolioProductInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem.model.FundRedeemModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem.presenter.FundRedeemPresenter;
import com.hp.hpl.sparta.Text;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class FundRedeemFragment extends MvpBussFragment<FundRedeemContract.FundRedeemPresenter> implements FundRedeemContract.FundRedeemView,View.OnClickListener {

    private View rootView;
    private FundRedeemContract.FundRedeemPresenter mFundRedeemPresenter;
    private FundRedeemModel model;//赎回model
    private PortfolioProductInfoView tvFundName;
    private TextView tvAviliableBalance,tvFundValue,tvFundCompany,tvFundState;//基金名称
    private SelectGridView sgvFundRedeemType;//基金赎回方式
    private List<Content> sgvlFundRedeemTypeList;//基金赎回方式数据（普通、快速）
    private EditMoneyInputWidget emiwFundRedeemQuantity;//基金赎回份额
    private EditChoiceWidget ecwFundHugeRedeemType;//巨额赎回处理方式
    private TextView tvHugeRedeemHint;//巨额赎回温馨提示
    private SelectGridView sgvFundRedeemExecuteType;//基金赎回执行方式
    private RelativeLayout rlFundExecuteType;
    private List<Content> sgvlFundRedeemExecuteTypeList;//基金赎回执行方式数据（立即赎回、指定日期）
    private EditChoiceWidget etDate;//指定日期选择组件
    private LinearLayout llQuickHint;
    private TextView tvQuickHintTwo,tvQuickHintThree,tvQuickHintFour;//快速赎回温馨提示
    private Button btnNext;//下一步按钮
    private SelectStringListDialog sslDateDialog;//指定固定日期选择弹框

    private boolean isQuickFundSell;
    private CheckBox cbAgree,cbHKAgree;//协议勾选框
    private RelativeLayout rlAgreementHK;//香港协议View
    private SpannableString ssAgrement,ssHKAgreement ;//协议内容



    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fund_redeem, null);
        return rootView;
    }

    @Override
    public void initView() {
        /**
         * 头部详情
         * */
        tvFundName = (PortfolioProductInfoView) rootView.findViewById(R.id.tv_fundname);
        tvAviliableBalance =(TextView) rootView.findViewById(R.id.tv_aviliable_balance);
        tvFundValue =(TextView) rootView.findViewById(R.id.tv_fundvalue);
        tvFundCompany =(TextView) rootView.findViewById(R.id.tv_fundcompany);
        tvFundState =(TextView) rootView.findViewById(R.id.tv_funstate);
        //dcvFundDetailInfo = (DetailContentView) rootView.findViewById(R.id.dcv_fund_redeem_detailInfo);
        sgvFundRedeemType = (SelectGridView) rootView.findViewById(R.id.sgv_fund_redeem_type);
        emiwFundRedeemQuantity = (EditMoneyInputWidget) rootView.findViewById(R.id.emiw_fund_redeem_quantity);
        ecwFundHugeRedeemType = (EditChoiceWidget) rootView.findViewById(R.id.ecw_fund_hugeredeem_type);
        tvHugeRedeemHint = (TextView) rootView.findViewById(R.id.tv_fund_huge_hint);
        rlFundExecuteType = (RelativeLayout) rootView.findViewById(R.id.rl_fund_execute_type);
        sgvFundRedeemExecuteType = (SelectGridView) rootView.findViewById(R.id.sgv_fund_redeemexecute_type);
        etDate = (EditChoiceWidget) rootView.findViewById(R.id.et_date);
        btnNext = (Button) rootView.findViewById(R.id.btn_next);
        cbAgree = (CheckBox)  rootView.findViewById(R.id.cb_agree);
        cbHKAgree = (CheckBox) rootView.findViewById(R.id.cb_hk_agree);
        rlAgreementHK = (RelativeLayout) rootView.findViewById(R.id.rl_fund_agreement_hk);
        ssAgrement = (SpannableString) rootView.findViewById(R.id.ss_agreement);
        ssHKAgreement = (SpannableString) rootView.findViewById(R.id.ss_HKagreement);
        llQuickHint = (LinearLayout) rootView.findViewById(R.id.ll_quick_hint);
        tvQuickHintTwo = (TextView) rootView.findViewById(R.id.tv_QuickRedeem_hint_toptwo);
        tvQuickHintThree = (TextView) rootView.findViewById(R.id.tv_QuickRedeem_hint_topthree);
        tvQuickHintFour = (TextView) rootView.findViewById(R.id.tv_QuickRedeem_hint_topfour);
    }

    @Override
    public void initData() {
        model = new FundRedeemModel();
        model.setFntype("06");

        model.setFundName("中银薪钱包");
        model.setFundCode("163801");
        model.setTotalAvailableBalance("25000");
        model.setNetPrice("5.28");
        model.setFundCompanyName("国泰基金管理公司");
        model.setFundState("正常开放");

        model.setCurrency("014");
        model.setIsSale("Y");//是否允许赎回
        model.setIsQuickSale("N");//是否允许快速赎回
        model.setIsZisSale("1");//是否可指定赎回 0：不允许  1：允许指定任意日期赎回  2：允许指定固定日期赎回
        model.setIsHK("Y");//是否是香港基金

        List<String> dateList = new ArrayList<String>();
        dateList.add("2014/09/15");
        dateList.add("2014/010/15");
        dateList.add("2014/11/15");
        dateList.add("2014/12/15");
        model.setDealDatelist(dateList);


        //默认设置为普通赎回
        isQuickFundSell = false;
        initBaseInfo();//初始化基金信息
        initRedeemType();//初始化赎回方式：普通/快速
        initHugeRedeem();//初始化巨额赎回处理方式：顺延赎回/取消赎回
        initRedeemShare();//初始化赎回份额
        initAgreement();//初始化协议内容：普通
        initExecuteType();//初始化普通赎回执行方式：立即/指定

    }

    @Override
    protected FundRedeemContract.FundRedeemPresenter initPresenter() {
        return new FundRedeemPresenter(this);
    }

    @Override
    public void setListener() {
        btnNext.setOnClickListener(this);
        etDate.setOnClickListener(this);
        sgvFundRedeemType.setListener(new SelectGridView.ClickListener(){
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) { // 普通
                    isQuickFundSell = false;
                    ecwFundHugeRedeemType.setVisibility(View.VISIBLE);
                    tvHugeRedeemHint.setVisibility(View.VISIBLE);
                    rlFundExecuteType.setVisibility(View.VISIBLE);
                    llQuickHint.setVisibility(View.GONE);

                } else if (position == 1) { // 快速
                    isQuickFundSell = true;
                    ecwFundHugeRedeemType.setVisibility(View.GONE);
                    tvHugeRedeemHint.setVisibility(View.GONE);
                    rlFundExecuteType.setVisibility(View.GONE);
                    initQuickRedeemAgreement();
                    llQuickHint.setVisibility(View.VISIBLE);
                    getPresenter().quickSellQuotaQuery(model);
                }
            }
        });

        //设置巨额赎回处理方式
        ecwFundHugeRedeemType.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(sslDateDialog == null ){
                    sslDateDialog = new SelectStringListDialog(getContext());
                }
                List<String> hugeTypeList = new ArrayList<String>();
                hugeTypeList.add("顺延赎回");
                hugeTypeList.add("取消赎回");
                sslDateDialog.setHeaderTitleValue("巨额赎回处理方式");
                sslDateDialog.isShowHeaderTitle(true);
                sslDateDialog.isSetLineMargin(true);
                sslDateDialog.setListData(hugeTypeList);
                sslDateDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>(){
                    @Override
                    public void onSelect(int position,String model){
                        ecwFundHugeRedeemType.setChoiceTextContent(model);
                        sslDateDialog.dismiss();
                    }
                });
                if(!sslDateDialog.isShowing()){
                    sslDateDialog.show();
                }
            }
        });
        //设置执行方式{立即执行/指定日期执行} 点击监控
        sgvFundRedeemExecuteType.setListener(new SelectGridView.ClickListener(){
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) { // 立即
                    model.setExecuteType("0");
                    etDate.setVisibility(View.GONE);

                } else if (position == 1) { // 指定日期
                    model.setExecuteType("1");
                    if(model.getIsZisSale().equals("1")){
                        //指定任意日期
                        LocalDateTime date = ApplicationContext.getInstance().getCurrentSystemDate();
                        String startTime = date.format(DateFormatters.dateFormatter1);
                        etDate.setChoiceTextContent(startTime);
                        etDate.setVisibility(View.VISIBLE);
                    }else if(model.getIsZisSale().equals("2")){
                        //指定固定日期
                        queryFundCanDealDateQuery(model);
                        //getPresenter().queryFundCanDealDateQuery(model);

                    }


                }
            }
        });

        //设置“全部赎回”点击监控
        emiwFundRedeemQuantity.setRightTextViewOnClick(new EditMoneyInputWidget.RightTextClickListener(){
            //全部赎回
            @Override
            public void onRightClick(View v){
                emiwFundRedeemQuantity.setmContentMoneyEditText(model.getTotalAvailableBalance());
            }
        });

        //指定日期选择 点击监控
        etDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(model.getIsZisSale().equals("1")){
                    LocalDateTime date = ApplicationContext.getInstance().getCurrentSystemDate();
                    String startTime = date.format(DateFormatters.dateFormatter1);
                    AppiontAnyDate(LocalDate.parse(startTime,DateFormatters.dateFormatter1));
                }else if(model.getIsZisSale().equals("2")){
                    AppiontFixedDate();
                }
            }
        });
    }

    @Override
    public void onClick(View v){

        if(v == btnNext){
            if(!cbAgree.isChecked()){
                showErrorDialog("请阅读并同意用户协议");
                return;
            }
            showLoadingDialog();

            if("06".equals(model.getFntype())){
                //货币型基金
                start(new FundRedeemConfirmFragment(model,isQuickFundSell));
            }else{
                //非货币型基金
                getPresenter().queryFundCompanyDetail(model);
            }
        }
    }

    /**
     * 设置基金赎回方式 普通/快速
     * */
    private void initRedeemType(){
        if(model.getIsQuickSale().equals("Y")){
            //设置基金赎回类型  0 普通  1 快速
            sgvlFundRedeemTypeList = new ArrayList<Content>();
            Content mFundRedeemTypeContent1 = new Content();
            Content mFundRedeemTypeContent2 = new Content();
            mFundRedeemTypeContent1.setName("普通");
            mFundRedeemTypeContent1.setContentNameID("0");
            mFundRedeemTypeContent1.setSelected(true);
            sgvlFundRedeemTypeList.add(mFundRedeemTypeContent1);
            mFundRedeemTypeContent2.setName("快速");
            mFundRedeemTypeContent2.setContentNameID("1");
            mFundRedeemTypeContent2.setSelected(false);
            sgvlFundRedeemTypeList.add(mFundRedeemTypeContent2);
            sgvFundRedeemType.setData(sgvlFundRedeemTypeList);
        }else{
            //仅普通赎回
            sgvlFundRedeemTypeList = new ArrayList<Content>();
            Content mFundRedeemTypeContent1 = new Content();
            Content mFundRedeemTypeContent2 = new Content();
            mFundRedeemTypeContent1.setName("普通");
            mFundRedeemTypeContent1.setContentNameID("0");
            mFundRedeemTypeContent1.setSelected(true);
            sgvlFundRedeemTypeList.add(mFundRedeemTypeContent1);
            sgvFundRedeemType.setData(sgvlFundRedeemTypeList);
        }

    }

    /**
     * 初始化协议（普通/香港）
     * */
    private void initAgreement(){
        ssAgrement.setText("");
        String startTitle1 = getString(R.string.boc_purchase_product_agreement_start_title1);
        String content1 = "《中国银行股份有限公司理财产品客户权益须知》";
        String endTitle1 = getString(R.string.boc_purchase_product_agreement_end_title1);
        ssAgrement.setAppendContent(startTitle1, endTitle1, content1, new MClickableSpan.OnClickSpanListener() {
            @Override
            public void onClickSpan() {
                start(ContractFragment.newInstance("file:///android_asset/webviewcontent/wealthmanagement/portfoliopurchase/notice/notice.html"));
            }
        });

        String startTitle2 = getString(R.string.boc_purchase_product_agreement_start_title2);
        String content2 = getString(R.string.boc_purchase_product_agreement_content2);
        String endTitle2 = getString(R.string.boc_purchase_product_agreement_end_title2);
        ssAgrement.setAppendContent(startTitle2, endTitle2, content2, new MClickableSpan.OnClickSpanListener() {
            @Override
            public void onClickSpan() {
                start(ContractFragment.newInstance("http://srh.bankofchina.com/search/finprod/getProdPage.jsp?keyword="));
            }
        });

        //如果是香港基金
        if(model.getIsHK().equals("Y")){
            ssHKAgreement.setText("");
            String startTitleHk = "本人承诺本人不属于本基金";
            String contentHk = "《招募说明书》";
            String endTitleHk = "或其他相关法律文件中所述“美国人士”或其他限制人士，否则由此产生的一切风险和损失由本人自行承担。";
            ssHKAgreement.setAppendContent(startTitleHk, endTitleHk, contentHk, new MClickableSpan.OnClickSpanListener() {
                @Override
                public void onClickSpan() {
                    start(ContractFragment.newInstance("http://srh.bankofchina.com/search/finprod/getProdPage.jsp?keyword="));
                }
            });
        }else {
            rlAgreementHK.setVisibility(View.GONE);
        }

    }

    /**
     * 初始化基本信息
     * */
    private void initBaseInfo(){
        tvFundName.setText("美元","中银薪钱包","(000699)");
        tvAviliableBalance.setText("25000");
        tvFundValue.setText("5.28");
        tvFundCompany.setText("国泰基金管理公司");
        tvFundState.setText("正常开放");
    }

    /**
     * 初始化巨额赎回处理方式
     * */
    private void initHugeRedeem(){
        ecwFundHugeRedeemType.setChoiceTextContent("巨额赎回");
    }

    /**
     * 初始化快速赎回协议内容
     * */
    private void initQuickRedeemAgreement(){
        String str1 = "1.最少赎回20份，最低持有份额500\n2.每日：最多赎回500笔，累计可赎回50,000.00份。";
        String str2 = "3.今日：已赎回--笔，累计已赎回--份，还可赎回--份。";
        String str3 = "4.快速赎回当日到账，到日无收益；如遇赎回资金问题，请与《--》联系，基金公司客服电话为---";
        tvQuickHintTwo.setText(str1);
        tvQuickHintThree.setText(str2);
        tvQuickHintFour.setText(str3);
    }
    /**
     * 设置赎回份额
     * */
    private void initRedeemShare(){

        //设置基金赎回份额
        emiwFundRedeemQuantity.getContentMoneyEditText().setHint("请输入");
        emiwFundRedeemQuantity.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_black));
        emiwFundRedeemQuantity.setTitleTextBold(true);//加粗
        emiwFundRedeemQuantity.setEditWidgetTitle(getString(R.string.boc_position_redeem_shares_redemption));
        emiwFundRedeemQuantity.setRightTextViewVisibility(true);
        emiwFundRedeemQuantity.setRightTextColor(getResources().getColor(R.color.boc_text_color_red));
        emiwFundRedeemQuantity.setRightTextViewText("全部赎回");
        emiwFundRedeemQuantity.setRightTextSize(11);
        emiwFundRedeemQuantity.setMaxLeftNumber(12);
    }
    /**
     * 初始化执行方式 立即赎回 指定日期赎回
     * */
    private void initExecuteType(){
        //设置基金赎回执行方式类型  0：立即赎回  1：执行日期赎回
        if(model.getIsZisSale().equals("0")){
            sgvlFundRedeemExecuteTypeList = new ArrayList<Content>();
            Content mFundRedeemExecuteTypeContent1 = new Content();
            mFundRedeemExecuteTypeContent1.setName("立即赎回");
            mFundRedeemExecuteTypeContent1.setContentNameID("0");
            mFundRedeemExecuteTypeContent1.setSelected(true);
            sgvlFundRedeemExecuteTypeList.add(mFundRedeemExecuteTypeContent1);
            sgvFundRedeemExecuteType.setData(sgvlFundRedeemExecuteTypeList);
        }else{
            sgvlFundRedeemExecuteTypeList = new ArrayList<Content>();
            Content mFundRedeemExecuteTypeContent1 = new Content();
            mFundRedeemExecuteTypeContent1.setName("立即赎回");
            mFundRedeemExecuteTypeContent1.setContentNameID("0");
            mFundRedeemExecuteTypeContent1.setSelected(true);
            sgvlFundRedeemExecuteTypeList.add(mFundRedeemExecuteTypeContent1);
            Content mFundRedeemExecuteTypeContent2 = new Content();
            mFundRedeemExecuteTypeContent2.setName("指定日期赎回");
            mFundRedeemExecuteTypeContent2.setContentNameID("1");
            mFundRedeemExecuteTypeContent2.setSelected(false);
            sgvlFundRedeemExecuteTypeList.add(mFundRedeemExecuteTypeContent2);
            sgvFundRedeemExecuteType.setData(sgvlFundRedeemExecuteTypeList);
        }


    }

    /**
     * 指定固定日期选择
     * */
    private void AppiontFixedDate(){
        if(sslDateDialog == null ){
            sslDateDialog = new SelectStringListDialog(getContext());
        }
        List<String> dateList = new ArrayList<String>();
        for(int i=0;i<model.getDealDatelist().size();i++){
            dateList.add(model.getDealDatelist().get(i));
        }
        sslDateDialog.setHeaderTitleValue("选择固定日期");
        sslDateDialog.isShowHeaderTitle(true);
        sslDateDialog.isSetLineMargin(true);
        sslDateDialog.setListData(dateList);
        sslDateDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>(){
            @Override
            public void onSelect(int position,String model){
                etDate.setChoiceTextContent(model);
                sslDateDialog.dismiss();
            }
        });
        if(!sslDateDialog.isShowing()){
            sslDateDialog.show();
        }
    }

    /**
     * 指定任意日期选择-未来三月-系统弹框
     * */
    private void AppiontAnyDate(LocalDate currentDate){

        DateTimePicker.showDatePick(mContext,currentDate, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack(){

            @Override
            public  void onChoiceDateSet(String strChoiceTime, LocalDate choiceDate){
                if (ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().isAfter(choiceDate)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_before));
                    return;
                }
                if (PublicUtils.isCompareDateRange(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(),choiceDate , 3)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_year_change, PublicUtils.changeNumberToUpper(3 / 12)));
                    return;
                }
                etDate.setChoiceTextContent(strChoiceTime);
            }
        });
    }


    //查询可指定交易日期成功返回
    public void queryFundCanDealDateQuery(FundRedeemModel fundCanDealDateQueryModel){
        model = fundCanDealDateQueryModel;
        etDate.setChoiceTextContent(model.getDealDatelist().get(0));
        etDate.setVisibility(View.VISIBLE);

    }

    //基金快速赎回额度查询成功返回
    public void quickSellQuotaQuery(FundRedeemModel fundRedeemModel){
        this.model = fundRedeemModel;
        String str = "3.今日：最多赎回"+model.getDayQuickSellNum()+"笔，累计已赎回"
                +model.getDayCompleteShare()+"份，还可赎回"
                +(Integer.parseInt(model.getDayQuickSellNum())-Integer.parseInt(model.getDayCompleteShare()))
                +"份";
        tvQuickHintThree.setText(str);
        getPresenter().queryFundCompanyDetail(model);
    }

    //查询基金公司成功返回
    public void queryFundCompanyDetail(FundRedeemModel fundCompanyInfoQueryModel){
        closeProgressDialog();
        model = fundCompanyInfoQueryModel;
        String str = "4.快速赎回当日到账，到日无收益；如遇赎回资金问题，请与《"+model.getCompanyName()
                +"》联系，基金公司客服电话为"+model.getCompanyPhone();
        tvQuickHintFour.setText(str);
        //start(new FundRedeemConfirmFragment(model,isQuickFundSell));
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
    }

    @Override
    protected String getTitleValue() {
        return "赎回";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return super.isDisplayRightIcon();
    }

}