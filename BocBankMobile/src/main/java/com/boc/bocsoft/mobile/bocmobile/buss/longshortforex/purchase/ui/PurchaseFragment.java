package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.ui;

import android.graphics.Typeface;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.PartialLoadView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.gallery.Gallery;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selectdialog.SelectDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui.MarginManagementFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.adapter.PriceAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.model.BailAccount;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.model.TabType;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.presenter.PurchaseContract;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.presenter.PurchasePresenter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

import static com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.model.TabType.DELETE;
import static com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.model.TransType.LIMIT_IMMEDIATELY;
import static com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.ui.PurchaseSelectDialog.SELECT_TYPE_CURRENCY;
import static com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.ui.PurchaseSelectDialog.SELECT_TYPE_TRANS;

/**
 * @author wangyang
 *         2016/12/15 17:20
 *         双向宝购买页面
 */
public class PurchaseFragment extends BaseAccountFragment<PurchasePresenter> implements PurchaseContract.PurchaseView, View.OnClickListener, RadioGroup.OnCheckedChangeListener, MClickableSpan.OnClickSpanListener, SelectDialog.OnItemSelectDialogClicked {

    /**
     * 更新时间,交易账户,文本提示
     */
    private SpannableString tvTime, tvAccount, tvHint, tvBalance, tvFirst, tvFirstTitle, tvSecondTitle;
    /**
     * 建仓,先开先平,制定平仓选项卡
     */
    private RadioGroup rgTab;
    /**
     * 下一步按钮
     */
    private Button btnNext;
    /**
     * 刷新牌价按钮
     */
    private PartialLoadView ivRefresh;
    /**
     * 牌价
     */
    private Gallery gyPrice;
    /**
     * 结算币种,指定平仓交易,交易方式,委托截止时间
     */
    private EditChoiceWidget etCurrency, etDelete, etType, etDate;
    /**
     * 交易金额,结算币种
     */
    private EditMoneyInputWidget etAmount, etDifferent;
    /**
     * 第一笔面板,第二笔面板
     */
    private ViewGroup llFirst, llSecond;
    /**
     * tab选项卡分割线
     */
    private View viewTab;
    /**
     * 第一笔分时图,第二笔分时图
     */
    private LineChart chartFirst, chartSecond;
    /**
     * 第一笔汇率输入框,第二笔汇率输入框
     */
    private PurchaseRate prFirst, prSecond;
    /**
     * 牌价Adapter
     */
    private PriceAdapter priceAdapter;
    /**
     * 选择框
     */
    private PurchaseSelectDialog selectDialog;
    /**
     * 结算账户列表
     */
    private List<BailAccount> accounts;
    /**
     * 结算币种列表
     */
    private List<String> currencyList;
    /**
     * 交易Model
     */
    private PurchaseModel model;

    public PurchaseFragment() {
        model = new PurchaseModel();
        model.setSourceCurrency("014");
        model.setTargetCurrency("001");
        model.setCardType("F");
        model.setTransType(LIMIT_IMMEDIATELY);
        model.setTabType(DELETE);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_purchase_title, getCurrency(model.getSourceCurrency()), getCurrency(model.getTargetCurrency()));
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_longshortforex_purchase, null);
    }

    @Override
    public void initView() {
        llFirst = (ViewGroup) mContentView.findViewById(R.id.ll_first);
        llSecond = (ViewGroup) mContentView.findViewById(R.id.ll_second);
        chartFirst = (LineChart) mContentView.findViewById(R.id.chart_first);
        chartSecond = (LineChart) mContentView.findViewById(R.id.chart_second);
        viewTab = mContentView.findViewById(R.id.view_tab);
        tvTime = (SpannableString) mContentView.findViewById(R.id.tv_time);
        tvAccount = (SpannableString) mContentView.findViewById(R.id.tv_account);
        tvHint = (SpannableString) mContentView.findViewById(R.id.tv_hint);
        tvBalance = (SpannableString) mContentView.findViewById(R.id.tv_balance);
        tvFirst = (SpannableString) mContentView.findViewById(R.id.tv_first_hint);
        tvFirstTitle = (SpannableString) mContentView.findViewById(R.id.tv_first);
        tvSecondTitle = (SpannableString) mContentView.findViewById(R.id.tv_second);
        ivRefresh = (PartialLoadView) mContentView.findViewById(R.id.iv_refresh);
        etCurrency = (EditChoiceWidget) mContentView.findViewById(R.id.et_currency);
        etDelete = (EditChoiceWidget) mContentView.findViewById(R.id.et_delete);
        etType = (EditChoiceWidget) mContentView.findViewById(R.id.et_type);
        etDate = (EditChoiceWidget) mContentView.findViewById(R.id.et_date);
        etAmount = (EditMoneyInputWidget) mContentView.findViewById(R.id.et_amount);
        etDifferent = (EditMoneyInputWidget) mContentView.findViewById(R.id.et_different);
        btnNext = (Button) mContentView.findViewById(R.id.btn_next);
        rgTab = (RadioGroup) mContentView.findViewById(R.id.rg_tab);
        gyPrice = (Gallery) mContentView.findViewById(R.id.gy_price);
        prFirst = (PurchaseRate) mContentView.findViewById(R.id.pr_first);
        prSecond = (PurchaseRate) mContentView.findViewById(R.id.pr_second);

        rgTab.setOnCheckedChangeListener(this);
        etCurrency.setBottomLineVisibility(true);
        etDelete.setBottomLineVisibility(true);
        etDate.setBottomLineVisibility(true);
        etAmount.setBottomLineVisibility(true);
        etDifferent.setBottomLineVisibility(true);
        ivRefresh.setPics(new int[]{R.drawable.boc_partial_loading_1, R.drawable.boc_partial_refresh_black});
        gyPrice.setSpacing(getResources().getDimensionPixelOffset(R.dimen.boc_space_between_80px));
        prFirst.setScrollView(mContentView);
        prSecond.setScrollView(mContentView);

        model.getTabType().setChecked(rgTab);

        //tvAccount.setText(getString(R.string.boc_purchase_account, NumberUtils.formatCardNumber(model.getBailAccount().getMarginAccountNo())));
    }

    /**
     * 根据TabType初始化UI,主要是tab选项卡
     */
    private void initViewByTabType() {
        switch (model.getTabType()) {
            case NONE:
                setVisibility(View.GONE, rgTab, viewTab);
                initCurrencyAndBalance();
                break;
            case CREATE:
                initCurrencyAndBalance();
                break;
            case FIRST_OPEN_FLAT:
                setVisibility(View.VISIBLE, prFirst, prSecond);
                break;
            case DELETE:
                setVisibility(View.VISIBLE, prFirst, prSecond, etDelete);
                break;
        }
        initViewByTransType();
    }

    private void resetView() {
        setVisibility(View.GONE, etDelete, etDate, etDifferent, tvBalance, prFirst, prSecond, llFirst, llSecond);
    }

    /**
     * 根据TransType初始化UI
     */
    private void initViewByTransType() {
        etType.setChoiceTextContent(model.getTransType().getTitle(mContext));
        switch (model.getTransType()) {
            case PRICE_IMMEDIATELY:
                btnNext.setText(getString(R.string.boc_purchase_deal));
                setVisibility(View.VISIBLE, llFirst);
                break;
            case LIMIT_IMMEDIATELY:
                setVisibility(View.VISIBLE, llFirst);
                break;
            case ENTRUST_PROFIT:
                setVisibility(View.VISIBLE, etDate, llFirst);
                break;
            case ENTRUST_STOP:
                setVisibility(View.VISIBLE, etDate, llFirst);
                break;
            case ENTRUST_CHOICE:
                setVisibility(View.VISIBLE, etDate, llFirst);
                break;
            case ENTRUST_ADD:
            case ENTRUST_SERIAL:
                setVisibility(View.VISIBLE, etDate, llFirst, llSecond, tvFirst, tvFirstTitle, tvSecondTitle);
                break;
            case ENTRUST_PURSUIT_STOP:
                setVisibility(View.VISIBLE, etDate, etDifferent);
                break;
        }
    }

    @Override
    public void setListener() {
        ivRefresh.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        etType.setOnClickListener(this);
        etCurrency.setOnClickListener(this);
        etDelete.setOnClickListener(this);
    }

    @Override
    protected PurchasePresenter initPresenter() {
        return new PurchasePresenter(this);
    }

    @Override
    public void initData() {
        initPrice();

        ivRefresh.setLoadStatus(PartialLoadView.LoadStatus.LOADING);
        showLoadingDialog();
        getPresenter().queryCurrencyAndSingleRate(model);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        System.out.println("onHiddenChanged===============");
        super.onHiddenChanged(hidden);
    }

    /**
     * 初始化头部刷新时间,牌价信息
     */
    private void initPrice() {
        if (model.getUpdateTime() != null)
            tvTime.setText(getString(R.string.boc_purchase_update_time, model.getUpdateTime().format(DateFormatters.timeFormatter)));

        if (priceAdapter == null) {
            priceAdapter = new PriceAdapter(getContext(), gyPrice);
            gyPrice.setAdapter(priceAdapter);
        }

        priceAdapter.setModel(model);
    }

    /**
     * 初始化账户信息(账号,结算币种,最大可参考交易额)
     */
    private void initCurrencyAndBalance() {
        String currency = getCurrency(model.getCurrency());
        etCurrency.setChoiceTextContent(currency);

        if (model.getTabType() != TabType.CREATE && model.getTabType() != TabType.NONE)
            return;

        if (accounts == null) {
            startPresenter();
            getPresenter().queryBailAccount();
            return;
        }

        BailAccount account = ModelUtil.generateBailAccount(model.getCurrency(), accounts);
        if (account == null)
            return;

        tvBalance.setVisibility(View.VISIBLE);
        String maxTradeMoney = MoneyUtils.transMoneyFormat(account.getMaxTradeAmount(), account.getCurrency());
        tvBalance.setContent(getString(R.string.boc_purchase_balance_hint), maxTradeMoney + " " + currency, R.color.boc_text_color_common_gray, new StyleSpan(Typeface.BOLD));
        tvBalance.setAppendContent(getString(R.string.boc_purchase_balance_hint_deposit), R.color.boc_main_button_color, this, new StyleSpan(Typeface.BOLD));
    }

    @Override
    public void onClick(View v) {
        if (v == ivRefresh) {
            ivRefresh.setLoadStatus(PartialLoadView.LoadStatus.LOADING);
            destroyBgTask();
            showLoadingDialog();
            getPresenter().querySingleRate(model);
            getPresenter().intervalQuerySingleRate(model);
        } else if (v == etType) {
            getSelectDialog().showDialog(model.getTabType().getTransList(mContext), SELECT_TYPE_TRANS);
        } else if (v == etCurrency) {
            if (model.isShowMaxTrade())
                getSelectDialog().showDialog(ModelUtil.generateCurrencyList(mContext, accounts, model), SELECT_TYPE_CURRENCY);
            else
                getSelectDialog().showDialog(currencyList, SELECT_TYPE_CURRENCY);
        }else if(v == etDelete){
            getPresenter().queryTransaction(model);
        }
    }

    public PurchaseSelectDialog getSelectDialog() {
        if (selectDialog == null) {
            selectDialog = new PurchaseSelectDialog(getContext());
            selectDialog.setListener(this);
        }
        return selectDialog;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        model.setTabType(model.getTabType().getTabType(group, checkedId));
        resetView();
        initViewByTabType();
    }

    @Override
    public void onListItemClicked(int index) {
        String item = getSelectDialog().getItem(index);
        switch (getSelectDialog().getType()) {
            case SELECT_TYPE_TRANS:
                model.setTransType(model.getTabType().getTransType(mContext, item));
                resetView();
                initViewByTransType();
                break;
            case SELECT_TYPE_CURRENCY:
                String currency = PublicCodeUtils.getCurrency(mContext, item);
                model.setCurrency(currency);
                if (StringUtils.isEmptyOrNull(currency))
                    MarginManagementFragment.newInstance(mActivity,this.getClass());
                else
                    initCurrencyAndBalance();
                break;
        }
    }

    @Override
    public void onClickSpan() {
        MarginManagementFragment.newInstance(mActivity,this.getClass(),model.getCurrency());
    }

    @Override
    public void querySingleRate(PurchaseModel model, boolean isCloseProgress) {
        if (isCloseProgress)
            closeProgressDialog();
        ivRefresh.setLoadStatus(PartialLoadView.LoadStatus.REFRESH);
        this.model = model;
        initPrice();
    }

    @Override
    public void queryCurrencyList(List<String> currencyList) {
        this.currencyList = currencyList;

        if (currencyList == null || currencyList.isEmpty())
            return;

        if (StringUtils.isEmptyOrNull(model.getCurrency()))
            model.setCurrency(getCurrency(currencyList.get(0)));
        initCurrencyAndBalance();
    }

    @Override
    public void queryBailAccount(List<BailAccount> accounts) {
        this.accounts = accounts;
        if (accounts == null || accounts.isEmpty())
            return;

        if (StringUtils.isEmptyOrNull(model.getCurrency()))
            model.setCurrency(accounts.get(0).getCurrency());
        initCurrencyAndBalance();
    }
}
