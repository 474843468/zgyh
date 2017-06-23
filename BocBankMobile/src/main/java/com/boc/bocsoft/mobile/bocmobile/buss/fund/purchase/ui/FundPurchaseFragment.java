package com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.ui;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyruler.ExactBoundaryMoneyRulerWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyruler.MoneyRulerWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.model.FundbuyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.presenter.FundPurchasePresenter;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FundPurchaseFragment extends MvpBussFragment<FundPurchaseContract.FundPurchasePresenter> implements FundPurchaseContract.FundPurchaseView,ExactBoundaryMoneyRulerWidget.MoneyRulerScrollerListener , View.OnClickListener{


    private FundPurchaseContract.FundPurchasePresenter mFundPurchasePresenter;
    /**
     * 页面View
     * */
    private View rootView;
    private TextView tvFundname,tvFundcode;// TextView 基金名称、基金代码
    private TextView tvFundcompany;//TextView 基金公司
    private EditChoiceWidget moneyAccount;// 资金账户
    private ExactBoundaryMoneyRulerWidget moneyRuler;//购买金额标尺
    private EditMoneyInputWidget etAmount;//金额输入框
    private EditChoiceWidget etDate;//指定日期
    private SpannableString ssBalance;//可用余额
    private SelectGridView sgExecutetype;//执行方式
    private CheckBox cbAgree,cbHkAgree;//协议勾选框
    private RelativeLayout rlAgreementHk;//香港协议View
    private SpannableString ssAgrement,ssHkAgreement;//中行协议 香港协议
    private SelectStringListDialog sslDateDialog;//指定固定日期选择弹框
    private Button btnNext;

    private  ArrayList<Content> sgExecutetypeList;  //执行方式选择数据 立即 指定
    private FundbuyModel fundbuyModel;
    private Long strMoneyLowValue;//滑动标尺下限值

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fund_purchase, null);
        return rootView;
    }

    @Override
    public void initView() {
        tvFundname = (TextView) rootView.findViewById(R.id.tv_fundname);
        tvFundcompany = (TextView) rootView.findViewById(R.id.tv_fundcompany) ;
        moneyRuler = (ExactBoundaryMoneyRulerWidget) rootView.findViewById(R.id.ruler_fundmoneyamount);
        sgExecutetype = (SelectGridView) rootView.findViewById(R.id.viewSelect);
        ssAgrement = (SpannableString) rootView.findViewById(R.id.ss_agreement);
        ssHkAgreement = (SpannableString) rootView.findViewById(R.id.ss_hk_agreement) ;
        etDate = (EditChoiceWidget) rootView.findViewById(R.id.et_date);
        btnNext = (Button) rootView.findViewById(R.id.btn_next);
        ssBalance = (SpannableString) rootView.findViewById(R.id.ss_balance);
        cbAgree = (CheckBox) rootView.findViewById(R.id.cb_agree);
        cbHkAgree = (CheckBox) rootView.findViewById(R.id.cb_hk_agree);
        rlAgreementHk = (RelativeLayout)rootView.findViewById(R.id.rl_fund_agreement_hk);
        etDate = (EditChoiceWidget) rootView.findViewById(R.id.et_date);
    }

    @Override
    public void initData() {

        fundbuyModel = new FundbuyModel();
        /**
         * 挡板数据1 基金详情
         * */
        fundbuyModel.setFundName("中银中高等级债券");//基金名称
        fundbuyModel.setCompanyName("中银基金管理公司");//基金公司名称
        fundbuyModel.setFundCode("163816");//基金代码
        fundbuyModel.setFntype("02");//基金产品类型
        fundbuyModel.setFeeType("1");//收费方式
        fundbuyModel.setRisklv("1");//基金风险等级
        fundbuyModel.setSign("1");//是否需要签约电子合同
        fundbuyModel.setCurrency("014");//基金币种类型
        fundbuyModel.setCashFlag("TRN");//基金外币钞汇标识
        fundbuyModel.setIsHK("Y");//是否是香港基金
        fundbuyModel.setIsBuy("Y");//是否可认购
        fundbuyModel.setOrderLowLimit("500");//个人认购最低金额（首次）
        fundbuyModel.setBuyAddLowLmt("100");//追加认购最低金额
        fundbuyModel.setIsInvt("N");//是否可申购
        fundbuyModel.setApplyLowLimit("1100");//个人申购最低金额

        List<String> dateList = new ArrayList<String>();
        dateList.add("2014/09/15");
        dateList.add("2014/010/15");
        dateList.add("2014/11/15");
        dateList.add("2014/12/15");
        fundbuyModel.setDealDatelist(dateList);//指定固定日期

        if(fundbuyModel.getIsInvt().equals("Y")){
            fundbuyModel.setIsZisInvt("2");//是否可指定申购 0：不允许 1：允许指定任意日期申购 2：指定固定日期
        }else if(fundbuyModel.getIsBuy().equals("Y")){
            fundbuyModel.setIsZisTby("2");//是否可指定认购 0：不允许 1：允许指定任意日期认购 2：指定固定日期
        }

        /**
         * 挡板数据2 账户详情
         * */
        fundbuyModel.setAccountId("125927308");//资金账户ID
        /**
         * 挡板数据3 查询可指定交易日期
         * */
        fundbuyModel.setAppointFlag("0");//指定交易日期标识  0：购买 1：赎回
        /**
         * 挡板数据4 I41 024&025 基金买入&挂单接口
         * */
        fundbuyModel.setExecuteType("0");//执行方式
        fundbuyModel.setAssignedDate("");//指定日期
        /**
         * 挡板数据5 I00 001查询投资交易账号绑定信息
         * */
        fundbuyModel.setInvtType("12");//投资交易类型
        fundbuyModel.setInvestAccount("181818");//投资交易账户
        /**
         * 挡板数据6 I41 002 查询账户风险评估等级
         * */
        fundbuyModel.setEvaluated(true);//是否做过风险评估
        /**
         * 初始化界面数据
         * */
        initExecuteTypeView();//初始化执行类型
        initAgreement();//初始化协议
        initMoneyInput();//初始化金额输入标尺
        initBaseInfo();//初始化基本信息
        getPresenter().queryAccountDetail(fundbuyModel);
    }

    @Override
    protected FundPurchaseContract.FundPurchasePresenter initPresenter() {
        return new FundPurchasePresenter(this);
    }

    @Override
    public void setListener() {
        btnNext.setOnClickListener(this);
        etDate.setOnClickListener(this);
        moneyRuler.setOnMoneyRulerScrollerListener(this);
        sgExecutetype.setListener(new SelectGridView.ClickListener(){
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    etDate.setVisibility(View.GONE);
                }else {
                    showLoadingDialog();
                    if(fundbuyModel.getIsBuy().equals("Y")&&fundbuyModel.getIsZisTby().equals("2")){
                        //认购-固定日期
                        etDate.setChoiceTextContent(fundbuyModel.getDealDatelist().get(0));
                        etDate.setVisibility(View.VISIBLE);
                        //getPresenter().queryFundCanDealDateQuery(fundbuyModel);
                    }else if(fundbuyModel.getIsBuy().equals("Y")&&fundbuyModel.getIsZisTby().equals("1")){
                        //认购-任意日期
                        LocalDateTime date = ApplicationContext.getInstance().getCurrentSystemDate();
                        String startTime = date.format(DateFormatters.dateFormatter1);
                        etDate.setChoiceTextContent(startTime);
                        etDate.setVisibility(View.VISIBLE);
                    }else if(fundbuyModel.getIsInvt().equals("Y")&&fundbuyModel.getIsZisInvt().equals("2")){
                        //申购-固定日期
                        etDate.setChoiceTextContent(fundbuyModel.getDealDatelist().get(0));
                        etDate.setVisibility(View.VISIBLE);
                        //getPresenter().queryFundCanDealDateQuery(fundbuyModel);
                    }else if(fundbuyModel.getIsInvt().equals("Y")&&fundbuyModel.getIsZisInvt().equals("1")){
                        //申购-任意日期
                        LocalDateTime date = ApplicationContext.getInstance().getCurrentSystemDate();
                        String startTime = date.format(DateFormatters.dateFormatter1);
                        etDate.setChoiceTextContent(startTime);
                        etDate.setVisibility(View.VISIBLE);
                    }
                }
            }

        });
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fundbuyModel.getIsBuy().equals("Y")&&fundbuyModel.getIsZisTby().equals("2")){
                    //认购-固定日期
                    AppiontFixedDate();
                }else if(fundbuyModel.getIsBuy().equals("Y")&&fundbuyModel.getIsZisTby().equals("1")){
                    //认购-任意日期
                    LocalDateTime date = ApplicationContext.getInstance().getCurrentSystemDate();
                    String startTime = date.format(DateFormatters.dateFormatter1);
                    AppiontAnyDate(LocalDate.parse(startTime,DateFormatters.dateFormatter1));

                }else if(fundbuyModel.getIsInvt().equals("Y")&&fundbuyModel.getIsZisInvt().equals("2")){
                    //申购-固定日期
                    AppiontFixedDate();
                }else if(fundbuyModel.getIsInvt().equals("Y")&&fundbuyModel.getIsZisInvt().equals("1")){
                    //申购-任意日期
                    LocalDateTime date = ApplicationContext.getInstance().getCurrentSystemDate();
                    String startTime = date.format(DateFormatters.dateFormatter1);
                    AppiontAnyDate(LocalDate.parse(startTime,DateFormatters.dateFormatter1));

                }

            }
        });
    }

    /**
     * 指定任意日期选择-未来三月-系统弹框
     * */
    private void AppiontAnyDate(LocalDate currentDate){

        DateTimePicker.showDatePick(mContext,currentDate,DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack(){

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

    /**
     * 指定固定日期选择
     * */
    private void AppiontFixedDate(){
        if(sslDateDialog == null ){
            sslDateDialog = new SelectStringListDialog(getContext());
        }
        List<String> dateList = new ArrayList<String>();
        for(int i=0;i<fundbuyModel.getDealDatelist().size();i++){
            dateList.add(fundbuyModel.getDealDatelist().get(i));
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

    @Override
    public void onClick(View v){
        if(v == btnNext){
            if(!cbAgree.isChecked()){
                showErrorDialog("请阅读并同意用户协议");
                return;
            }

            showLoadingDialog();

            getPresenter().queryFundCompanyDetail(fundbuyModel);
        }
    }



    @Override
    protected String getTitleValue() {
        return "购买";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return super.isDisplayRightIcon();
    }

    /**
     * 初始化基本信息
     * */
    private void initBaseInfo(){
        tvFundname.setText(fundbuyModel.getFundName()+"("+fundbuyModel.getFundCode()+")");
        tvFundcompany.setText(fundbuyModel.getCompanyName());
    }

    /**
     * 初始化金额输入控件
     */
    private void initMoneyInput() {
        if(fundbuyModel.getIsInvt().equals("Y")){
            //如果是申购
            strMoneyLowValue = Long.parseLong(fundbuyModel.getApplyLowLimit());
            moneyRuler.initMoneyRuler(strMoneyLowValue,1000,fundbuyModel.getCurrency());
            moneyRuler.setInitMoney(strMoneyLowValue.toString());

        }else  if(fundbuyModel.getIsBuy().equals("Y")){
            //如果是认购
            //先判断首次认购下限和追加认购下限,取min{首次，追加}
            if(Long.parseLong(fundbuyModel.getOrderLowLimit())>Long.parseLong(fundbuyModel.getBuyAddLowLmt())){
                strMoneyLowValue = Long.parseLong(fundbuyModel.getBuyAddLowLmt());
                moneyRuler.setInitMoney(fundbuyModel.getOrderLowLimit());
            }else {
                strMoneyLowValue = Long.parseLong(fundbuyModel.getOrderLowLimit());
                moneyRuler.setInitMoney(fundbuyModel.getOrderLowLimit());
            }
            moneyRuler.initMoneyRuler(strMoneyLowValue,1000,fundbuyModel.getCurrency());

        }
        moneyRuler.setMoneyLabel("买入金额（元）");
        moneyRuler.setMinTip(getString(R.string.boc_purchase_product_amount_hint_min));
        moneyRuler.setMaxTip(getString(R.string.boc_purchase_product_amount_hint_max));
    }
    /**
     * 初始化余额
     * */
    private void initBalance(FundbuyModel fundbuyModel){
        String currentCode ="";
        String currencyType = "";
        String remitType = "";
        BigDecimal balanceAccount ;
        int indexAccount = 0;
        //获取与基金币种类型一致的账户index
        if(fundbuyModel.getCurrency().equals(ApplicationConst.CURRENCY_CNY)){
            //如果是人民币
            currentCode = "001";
            for(int i =0;i<fundbuyModel.getAccountDetaiList().size();i++){
                if(fundbuyModel.getAccountDetaiList().get(i).getCurrencyCode().equals("001")){
                    Log.d("张辰雨++++", "人民币种:001 "+i);
                    balanceAccount =fundbuyModel.getAccountDetaiList().get(i).getAvailableBalance();
                }
            }
        }else{
            //非人民币情况下，判断外币币种和钞汇标识
            for(int i=0;i<fundbuyModel.getAccountDetaiList().size();i++){

                //先匹配币种
                if(fundbuyModel.getAccountDetaiList().get(i).getCurrencyCode().equals(fundbuyModel.getCurrency())){
                    //匹配钞汇
                    if(fundbuyModel.getCashFlag().equals("CAS")
                            &&fundbuyModel.getAccountDetaiList().get(i).getCashRemit().equals("01")){
                        indexAccount = i;
                        currentCode = fundbuyModel.getAccountDetaiList().get(i).getCurrencyCode();
                    }else  if(fundbuyModel.getCashFlag().equals("TRN")
                            &&fundbuyModel.getAccountDetaiList().get(i).getCashRemit().equals("02")){
                        indexAccount = i;
                        currentCode = fundbuyModel.getAccountDetaiList().get(i).getCurrencyCode();
                    }
                }
            }

        }

        //翻译币种&钞汇标识
        if(currentCode.equals("001")) {
            currencyType = "人民币元";
            remitType = "";
        }else {
            //判断外币种类
            if(currentCode.equals("014")){
                currencyType = "美元";
            }else if(currentCode.equals("027")){
                currencyType = "日元";
            }
            //判断钞汇种类
            if(fundbuyModel.getAccountDetaiList().get(indexAccount).getCashRemit().equals("01")){
                remitType +="钞";
            }else{
                remitType +="汇";
            }

        }
        moneyRuler.setMoneyLabel("买入金额"+"("+currencyType+"/"+remitType+")");
        balanceAccount = fundbuyModel.getAccountDetaiList().get(indexAccount).getAvailableBalance();
        ssBalance.setContent("可用余额: ",currencyType+"/"+remitType+" "+balanceAccount,R.color.boc_text_color_money_count);
    }

    /**
     * 初始化购买执行方式view
     */
    private void initExecuteTypeView(){

        if(fundbuyModel.getIsBuy().equals("Y")){
            //认购
            if(fundbuyModel.getIsZisTby().equals("0")){
                String[] singleSelectData = {"立即执行"};
                sgExecutetypeList = new ArrayList<Content>();
                // 初始化执行方式单选组件数据
                for (int i = 0; i < singleSelectData.length; i++) {
                    Content item = new Content();
                    item.setName(singleSelectData[i]);
                    if (i ==0){
                        item.setSelected(true);// 默认勾选最后一个选项
                    }
                    sgExecutetypeList.add(item);
                }
                sgExecutetype.setData(sgExecutetypeList);
            }else {
                String[] singleSelectData = {"立即执行", "指定执行日期"};
                sgExecutetypeList = new ArrayList<Content>();
                // 初始化执行方式单选组件数据
                for (int i = 0; i < singleSelectData.length; i++) {
                    Content item = new Content();
                    item.setName(singleSelectData[i]);
                    if (i ==0){
                        item.setSelected(true);// 默认勾选最后一个选项
                    }
                    sgExecutetypeList.add(item);
                }
                sgExecutetype.setData(sgExecutetypeList);
            }
        }else if(fundbuyModel.getIsInvt().equals("Y")){
            //申购
            if(fundbuyModel.getIsZisInvt().equals("0")){
                String[] singleSelectData = {"立即执行"};
                sgExecutetypeList = new ArrayList<Content>();
                // 初始化执行方式单选组件数据
                for (int i = 0; i < singleSelectData.length; i++) {
                    Content item = new Content();
                    item.setName(singleSelectData[i]);
                    if (i ==0){
                        item.setSelected(true);// 默认勾选最后一个选项
                    }
                    sgExecutetypeList.add(item);
                }
                sgExecutetype.setData(sgExecutetypeList);
            }else {
                String[] singleSelectData = {"立即执行", "指定执行日期"};
                sgExecutetypeList = new ArrayList<Content>();
                // 初始化执行方式单选组件数据
                for (int i = 0; i < singleSelectData.length; i++) {
                    Content item = new Content();
                    item.setName(singleSelectData[i]);
                    if (i ==0){
                        item.setSelected(true);// 默认勾选最后一个选项
                    }
                    sgExecutetypeList.add(item);
                }
                sgExecutetype.setData(sgExecutetypeList);
            }
        }
    }

    /**
     * 初始化协议
     * */
    private void initAgreement() {
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
        if(fundbuyModel.getIsHK().equals("Y")){

            ssHkAgreement.setText("");
            String startTitleHk = "本人承诺本人不属于本基金";
            String contentHk = "《招募说明书》";
            String endTitleHk = "或其他相关法律文件中所述“美国人士”或其他限制人士，否则由此产生的一切风险和损失由本人自行承担。";
            ssHkAgreement.setAppendContent(startTitleHk, endTitleHk, contentHk, new MClickableSpan.OnClickSpanListener() {
                @Override
                public void onClickSpan() {
                    start(ContractFragment.newInstance("http://srh.bankofchina.com/search/finprod/getProdPage.jsp?keyword="));
                }
            });
        }else {
            rlAgreementHk.setVisibility(View.GONE);
        }

    }


    //查询可指定交易日期返回
    public void queryFundCanDealDateQuery(FundbuyModel model){
        closeProgressDialog();
        fundbuyModel = model;
        etDate.setChoiceTextContent(fundbuyModel.getDealDatelist().get(0));
        etDate.setVisibility(View.VISIBLE);
    }

    //查询资金账户详情
    public void queryAccountDetail(FundbuyModel accountDetailQueryModel){
        fundbuyModel = accountDetailQueryModel;
        initBalance(fundbuyModel);
    }

    //查询基金公司详情返回
    public void queryFundCompanyDetail(FundbuyModel model){
        closeProgressDialog();
        fundbuyModel = model;
        start(new FundPurchaseConfirmFragment(fundbuyModel));
    }


    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public void onMoneyRulerScrollered(BigDecimal money) {
        fundbuyModel.setBuyAmount(String.valueOf(money));
    }
}