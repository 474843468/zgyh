package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthProfitBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.presenter.WealthPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;

/**
 * 收益试算
 * Created by liuweidong on 2016/10/16.
 */
public class WealthProfitCalcFragment extends MvpBussFragment<WealthContract.Presenter> implements WealthContract.ProfitView, View.OnClickListener {
    public static final String TAG = "WealthProfitCalcFragment";

    private View rootView;
    private LinearLayout llParentStandard;
    private LinearLayout llParentExtra;
    private EditClearWidget edtRateStandard;// 预期年化收益率
    private EditClearWidget edtDateStandard;// 产品期限
    private EditClearWidget edtPeriodStandard;// 投资期数
    private EditMoneyInputWidget moneyStandard;// 投资金额
    private Button btnCalcStandard;
    /*业绩基准转低收益*/
    private EditClearWidget edtRateExtra;// 预期年化收益率
    private EditClearWidget edtDateExtra;// 产品期限
    private EditMoneyInputWidget moneyExtra;// 投资金额
    private EditClearWidget edtRateCommon;// 预期年化收益率
    private EditClearWidget edtDateCommon;// 产品期限
    private EditMoneyInputWidget moneyCommon;// 投资金额
    private Button btnCalcExtra;// 试算

    private RelativeLayout rlBottom;
    private TextView txtProfit;// 预期收益
    private TextView txtTimes;

    private WealthDetailsBean detailsBean;
    private WealthProfitBean profitBean;

    public WealthProfitCalcFragment(WealthDetailsBean detailsBean) {
        this.detailsBean = detailsBean;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_profit_calc, null);
        return rootView;
    }

    @Override
    public void initView() {
        llParentStandard = (LinearLayout) rootView.findViewById(R.id.ll_parent_profit_standard);
        llParentExtra = (LinearLayout) rootView.findViewById(R.id.ll_parent_profit_extra);
        /*标准试算*/
        edtRateStandard = (EditClearWidget) rootView.findViewById(R.id.edt_rate_standard);
        edtDateStandard = (EditClearWidget) rootView.findViewById(R.id.edt_date_standard);
        edtPeriodStandard = (EditClearWidget) rootView.findViewById(R.id.edt_period_standard);
        moneyStandard = (EditMoneyInputWidget) rootView.findViewById(R.id.edt_money_standard);
        btnCalcStandard = (Button) rootView.findViewById(R.id.btn_calc_standard);
        /*锁定份额与普通份额试算*/
        edtRateExtra = (EditClearWidget) rootView.findViewById(R.id.edt_rate_extra);
        edtDateExtra = (EditClearWidget) rootView.findViewById(R.id.edt_date_extra);
        moneyExtra = (EditMoneyInputWidget) rootView.findViewById(R.id.edt_money_extra);
        edtRateCommon = (EditClearWidget) rootView.findViewById(R.id.edt_rate_common);
        edtDateCommon = (EditClearWidget) rootView.findViewById(R.id.edt_date_common);
        moneyCommon = (EditMoneyInputWidget) rootView.findViewById(R.id.edt_money_common);
        btnCalcExtra = (Button) rootView.findViewById(R.id.btn_calc_extra);
        /*预期收益*/
        rlBottom = (RelativeLayout) rootView.findViewById(R.id.rl_bottom);
        txtProfit = (TextView) rootView.findViewById(R.id.txt_profit);
        txtTimes = (TextView) rootView.findViewById(R.id.txt_times);
        setWidgetAttribute();// 设置控件的属性
    }

    @Override
    public void initData() {
        isShowView();
    }

    @Override
    public void setListener() {
        btnCalcStandard.setOnClickListener(this);
        btnCalcExtra.setOnClickListener(this);
        edtRateStandard.setClearEditTextWatcherListener(new EditClearWidget.ClearEditTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!NumberUtils.checkNumberLength(s.toString(), 12, 2)) {
                    s.delete(s.toString().length() - 1, s.toString().length());
                }
            }
        });
        edtRateExtra.setClearEditTextWatcherListener(new EditClearWidget.ClearEditTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!NumberUtils.checkNumberLength(s.toString(), 12, 2)) {
                    s.delete(s.toString().length() - 1, s.toString().length());
                }
            }
        });
        edtRateCommon.setClearEditTextWatcherListener(new EditClearWidget.ClearEditTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!NumberUtils.checkNumberLength(s.toString(), 12, 2)) {
                    s.delete(s.toString().length() - 1, s.toString().length());
                }
            }
        });
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_wealth_profit_title);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        hideSoftInput();
        super.titleLeftIconClick();
    }

    @Override
    protected WealthContract.Presenter initPresenter() {
        return new WealthPresenter(this);
    }

    /**
     * 设置控件的属性
     */
    private void setWidgetAttribute() {
        /*预期年化收益率*/
        edtRateStandard.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        /*产品期限*/
        edtDateStandard.getContentEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        edtDateStandard.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        /*投资期数*/
        edtPeriodStandard.getContentEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        edtPeriodStandard.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        /*投资金额*/
        moneyStandard.getContentMoneyEditText().setMaxLeftNumber(12);
        moneyStandard.getContentMoneyEditText().setMaxRightNumber(2);

        edtRateExtra.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edtRateCommon.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edtDateExtra.getContentEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        edtDateExtra.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        edtDateCommon.getContentEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        edtDateCommon.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        moneyExtra.getContentMoneyEditText().setMaxLeftNumber(12);
        moneyExtra.getContentMoneyEditText().setMaxRightNumber(2);
        moneyCommon.getContentMoneyEditText().setMaxLeftNumber(12);
        moneyCommon.getContentMoneyEditText().setMaxRightNumber(2);
        /*投资金额组件设置*/
        moneyStandard.setTitleTextViewVisibility(false);
        moneyStandard.setMoneyEditTextHaveMarginLeft(false);
        moneyStandard.getContentMoneyEditText().setHint(getString(R.string.boc_wealth_profit_hint_money));
    }

    /**
     * 页面布局的显示
     */
    private void isShowView() {
//        if ("0".equals(detailsBean.getProductKind())) {// 结构性理财产品
        if (WealthConst.IS_LOCK_PERIOD_1.equals(detailsBean.getIsLockPeriod())) {// 业绩基准-锁定期转低收益
            Log.i(TAG, "--------------业绩基准-锁定期转低收益");
                /*产品期限与投资金额回显*/
            llParentExtra.setVisibility(View.VISIBLE);
            llParentStandard.setVisibility(View.GONE);// 隐藏标准的

            edtDateExtra.setEditWidgetContent(detailsBean.getProdTimeLimit());// 产品期限（最低持有）
            moneyExtra.setmContentMoneyEditText(detailsBean.getSubAmount());// 投资金额
            moneyCommon.setmContentMoneyEditText(detailsBean.getSubAmount());
        } else if (WealthConst.IS_LOCK_PERIOD_2.equals(detailsBean.getIsLockPeriod()) ||
                (WealthConst.PRODUCT_TYPE_3.equals(detailsBean.getProductType()) && "0".equals(detailsBean.getPeriodical()) && !WealthConst.IS_LOCK_PERIOD_3.equals(detailsBean.getIsLockPeriod()))) {// 业绩基准-锁定期后入账或固定期限类
            Log.i(TAG, "---------------业绩基准-锁定期后入账或固定期限类");
                /*默认试算*/
            llParentExtra.setVisibility(View.GONE);
            llParentStandard.setVisibility(View.VISIBLE);
            edtPeriodStandard.setVisibility(View.GONE);// 隐藏投资期数

            if (StringUtils.isEmptyOrNull(detailsBean.getRateDetail()) || Double.valueOf(detailsBean.getRateDetail()) == 0) {
                edtRateStandard.setEditWidgetContent(detailsBean.getYearlyRR());// 预期年化收益率回显
            }
            edtDateStandard.setEditWidgetContent(detailsBean.getProdTimeLimit());// 产品期限回显
            moneyStandard.setmContentMoneyEditText(detailsBean.getSubAmount());// 金额回显
            if (checkProfitData()) {
                showLoadingDialog();
                getPresenter().profitCalc(profitBean);
            }
        } else if (WealthConst.IS_LOCK_PERIOD_3.equals(detailsBean.getIsLockPeriod()) || "1".equals(detailsBean.getPeriodical())) {// 业绩基准-锁定期周期滚续或周期性产品
            Log.i(TAG, "--------------业绩基准-锁定期周期滚续");
                /*默认试算*/
                /*预期年化收益率与产品期限与投资金额回显*/
            llParentExtra.setVisibility(View.GONE);
            llParentStandard.setVisibility(View.VISIBLE);

            if (StringUtils.isEmptyOrNull(detailsBean.getRateDetail()) || Double.valueOf(detailsBean.getRateDetail()) == 0) {
                edtRateStandard.setEditWidgetContent(detailsBean.getYearlyRR());// 预期年化收益率回显
            }
            edtDateStandard.setEditWidgetContent(detailsBean.getProdTimeLimit());// 产品期限回显
            edtPeriodStandard.setEditWidgetContent("1");// 投资期数默认为1
            moneyStandard.setmContentMoneyEditText(detailsBean.getSubAmount());// 投资金额回显
            if (checkProfitData()) {
                showLoadingDialog();
                getPresenter().profitCalc(profitBean);
            }
        } else if (WealthConst.YES_1.equals(detailsBean.getProgressionflag())) {// 收益累进
            Log.i(TAG, "-------------------收益累进");
            llParentExtra.setVisibility(View.GONE);
            llParentStandard.setVisibility(View.VISIBLE);
            edtPeriodStandard.setVisibility(View.GONE);// 隐藏投资期数
            moneyStandard.setmContentMoneyEditText(detailsBean.getSubAmount());// 投资金额回显
        } else if (WealthConst.PRODUCT_DAY.equals(detailsBean.getProdCode())) {// 日积月累
            Log.i(TAG, "---------------日积月累");
            /*预期年化收益率与投资金额回显*/
            llParentExtra.setVisibility(View.GONE);
            llParentStandard.setVisibility(View.VISIBLE);
            edtPeriodStandard.setVisibility(View.GONE);// 隐藏投资期数

            if (StringUtils.isEmptyOrNull(detailsBean.getRateDetail()) || Double.valueOf(detailsBean.getRateDetail()) == 0) {
                edtRateStandard.setEditWidgetContent(detailsBean.getYearlyRR());// 预期年化收益率回显
            }
            moneyStandard.setmContentMoneyEditText(detailsBean.getSubAmount());// 金额回显
        } else {
            Log.i(TAG, "-----------------其它情况");
            llParentExtra.setVisibility(View.GONE);
            llParentStandard.setVisibility(View.VISIBLE);
            edtPeriodStandard.setVisibility(View.GONE);// 隐藏投资期数

            if (StringUtils.isEmptyOrNull(detailsBean.getRateDetail()) || Double.valueOf(detailsBean.getRateDetail()) == 0) {
                edtRateStandard.setEditWidgetContent(detailsBean.getYearlyRR());// 预期年化收益率回显
            }
            moneyStandard.setmContentMoneyEditText(detailsBean.getSubAmount());// 金额回显
        }
//        }
    }

    /**
     * 校验预期年化收益率
     *
     * @param editClear
     * @return
     */
    private boolean checkRate(EditClearWidget editClear) {
        /*预期年化收益率*/
        if (StringUtils.isEmptyOrNull(editClear.getEditWidgetContent())) {
            showErrorDialog(getString(R.string.boc_wealth_profit_rate_empty));
            return false;
        } else if (Double.valueOf(editClear.getEditWidgetContent()) == 0) {
            showErrorDialog(getString(R.string.boc_wealth_profit_rate_error));
            return false;
        } else {
            profitBean.setExyield(editClear.getEditWidgetContent());
            return true;
        }
    }

    /**
     * 校验产品期限
     *
     * @param editClear
     * @return
     */
    private boolean checkDate(EditClearWidget editClear) {
        /*产品期限*/
        if (StringUtils.isEmptyOrNull(editClear.getEditWidgetContent())) {
            showErrorDialog(getString(R.string.boc_wealth_profit_date_empty));
            return false;
        } else if (Double.valueOf(editClear.getEditWidgetContent()) == 0) {
            showErrorDialog(getString(R.string.boc_wealth_profit_date_error));
            return false;
        } else {
            profitBean.setDayTerm(editClear.getEditWidgetContent());
            return true;
        }
    }

    /**
     * 校验投资期数
     *
     * @param editClear
     * @return
     */
    private boolean checkPeriod(EditClearWidget editClear) {
        if (StringUtils.isEmptyOrNull(editClear.getEditWidgetContent())) {
            showErrorDialog(getString(R.string.boc_wealth_profit_period_empty));
            return false;
        } else if (Double.valueOf(editClear.getEditWidgetContent()) == 0) {
            showErrorDialog(getString(R.string.boc_wealth_profit_period_error));
            return false;
        } else {
            profitBean.setTotalPeriod(editClear.getEditWidgetContent());
            return true;
        }
    }

    /**
     * 校验投资金额
     *
     * @param editMoney
     * @return
     */
    private boolean checkMoney(EditMoneyInputWidget editMoney) {
        /*投资金额*/
        if (StringUtils.isEmptyOrNull(editMoney.getContentMoney())) {
            showErrorDialog(getString(R.string.boc_wealth_profit_money_empty));
            return false;
        } else if (Double.valueOf(editMoney.getContentMoney()) == 0) {
            showErrorDialog(getString(R.string.boc_wealth_profit_money_error));
            return false;
        } else {
            profitBean.setPuramt(editMoney.getContentMoney());
            return true;
        }
    }

    /**
     * 封装收益试算数据
     */
    private boolean checkProfitData() {
        profitBean = new WealthProfitBean();
        profitBean.setProId(detailsBean.getProdCode());// 产品代码
        if (WealthConst.IS_LOCK_PERIOD_1.equals(detailsBean.getIsLockPeriod())) {// 业绩基准-锁定期转低收益
            if (!checkRate(edtRateExtra)) {// 预期年化收益率
                return false;
            }
            if (!checkDate(edtDateExtra)) {// 产品期限
                return false;
            }
            if (!checkMoney(moneyExtra)) {// 投资金额
                return false;
            }
            profitBean.setCommonExyield(edtRateCommon.getEditWidgetContent());
            profitBean.setCommonDayTerm(edtDateCommon.getEditWidgetContent());
            profitBean.setCommonPurAmt(moneyCommon.getContentMoney());
        } else if (WealthConst.IS_LOCK_PERIOD_2.equals(detailsBean.getIsLockPeriod()) || WealthConst.IS_LOCK_PERIOD_3.equals(detailsBean.getIsLockPeriod()) || WealthConst.PRODUCT_TYPE_3.equals(detailsBean.getProductType())
                || WealthConst.PRODUCT_DAY.equals(detailsBean.getProdCode()) || WealthConst.YES_1.equals(detailsBean.getProgressionflag())) {
            // 业绩基准-锁定期后入账|固定期限类|日积月累|收益累进
            if (!checkRate(edtRateStandard)) {// 预期年化收益率
                return false;
            }
            if (!checkDate(edtDateStandard)) {// 产品期限
                return false;
            }
            if (WealthConst.IS_LOCK_PERIOD_3.equals(detailsBean.getIsLockPeriod()) || "1".equals(detailsBean.getPeriodical())) {
                if (!checkPeriod(edtPeriodStandard)) {// 投资期数
                    return false;
                }
            }
            if (!checkMoney(moneyStandard)) {// 投资金额
                return false;
            }
        } else {
            if (!checkRate(edtRateStandard)) {// 预期年化收益率
                return false;
            }
            if (!checkDate(edtDateStandard)) {// 产品期限
                return false;
            }
            if (!checkMoney(moneyStandard)) {// 投资金额
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        rlBottom.setVisibility(View.GONE);// 点击试算按钮即隐藏
        if (checkProfitData()) {
            showLoadingDialog();
            getPresenter().profitCalc(profitBean);
        }
    }

    /**
     * 收益试算失败
     */
    @Override
    public void profitCalcFail() {
        closeProgressDialog();
        rlBottom.setVisibility(View.GONE);
    }

    /**
     * 收益试算成功
     */
    @Override
    public void profitCalcSuccess(BigDecimal expprofit, String procur) {
        closeProgressDialog();
        rlBottom.setVisibility(View.VISIBLE);
        txtProfit.setText(MoneyUtils.transMoneyFormat(expprofit, procur));
        String rate = "";
        if (WealthConst.IS_LOCK_PERIOD_1.equals(detailsBean.getIsLockPeriod())) {// 业绩基准-锁定期转低收益
            rate = edtRateExtra.getEditWidgetContent();
        } else {
            rate = edtRateStandard.getEditWidgetContent();
        }
        BigDecimal bigDecimal = new BigDecimal(rate).divide(new BigDecimal("0.35"), 0, BigDecimal.ROUND_DOWN);
        txtTimes.setText(bigDecimal.toPlainString());
    }
}
