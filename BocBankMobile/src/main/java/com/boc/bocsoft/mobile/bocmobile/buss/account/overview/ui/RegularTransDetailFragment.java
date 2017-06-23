package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MapUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.BaseDetailView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DeatilsBottomTableButtom;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model.TermlyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.AccountTypeUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong.ModuleDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyang
 *         16/8/13 18:40
 *         定期一本通交易详情详情
 */
@SuppressLint("ValidFragment")
public class RegularTransDetailFragment extends BaseOverviewFragment implements BaseDetailView.BtnCallback, View.OnClickListener {

    /**
     * 详情组件
     */
    private BaseDetailView detailView;

    private DeatilsBottomTableButtom btnBottom;

    private TermlyViewModel termlyViewModel;

    private AccountBean accountBean;

    public RegularTransDetailFragment(AccountBean accountBean, TermlyViewModel termlyViewModel) {
        this.accountBean = accountBean;
        this.termlyViewModel = termlyViewModel;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_overview_detail_regular_info_title);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_overview_regular_trans_detail, null);
    }

    @Override
    public void initView() {
        //添加头部筛选按钮
        detailView = (BaseDetailView) mContentView.findViewById(R.id.bdv_detail);
        btnBottom = (DeatilsBottomTableButtom) mContentView.findViewById(R.id.btn_bottom);
    }

    @Override
    public void initData() {
        //设置详情信息
        String currency = PublicCodeUtils.getCurrency(mContext, termlyViewModel.getCurrencyCode());
        String availableBalance = MoneyUtils.transMoneyFormat(termlyViewModel.getAvailableBalance(), termlyViewModel.getCurrencyCode());

        String cashRemit = AccountUtils.getCashRemit(termlyViewModel.getCashRemit());
        if (StringUtils.isEmptyOrNull(cashRemit))
            detailView.updateHeadData(getString(R.string.boc_overview_detail_regular_info_available) + "(" + currency + ")", availableBalance);
        else
            detailView.updateHeadData(getString(R.string.boc_overview_detail_regular_info_available) + "(" + currency + "/" + cashRemit + ")", availableBalance);

        //设置利率
        String interestRate = BigDecimal.valueOf(Double.parseDouble(termlyViewModel.getInterestRate())).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        String content;
        if (termlyViewModel.getType().equals(AccountTypeUtil.REGULAR_TYPE_TZCK))
            content = PublicCodeUtils.getCdPeriodDay(mContext, termlyViewModel.getCdPeriod()) + "/" + interestRate + "%";
        else
            content = PublicCodeUtils.getCdPeriod(mContext, termlyViewModel.getCdPeriod()) + "/" + interestRate + "%";
        if (content.indexOf("/") == 0)
            content = content.substring(1);
        detailView.updateHeadDetail(getString(R.string.boc_overview_detail_regular_info_rate), content);

        //设置存款种类
        detailView.addDetailRow(getString(R.string.boc_overview_detail_regular_info_category), PublicCodeUtils.getDepositReceiptType(mContext, termlyViewModel.getType()));

        //设置开户日期
        LocalDate openDate = termlyViewModel.getOpenDate();
        if (openDate != null)
            detailView.addDetailRow(getString(R.string.boc_overview_detail_regular_info_open_date), termlyViewModel.getOpenDate().format(
                    DateFormatters.dateFormatter1));
        else
            detailView.addDetailRow(getString(R.string.boc_overview_detail_regular_info_open_date), "");

        //设置计息起始日
        LocalDate startsDate = termlyViewModel.getInterestStartsDate();
        LocalDate endDate = termlyViewModel.getInterestEndDate();
        if (startsDate != null && endDate != null)
            detailView.addDetailRow(getString(R.string.boc_overview_detail_regular_info_start_end_date), termlyViewModel.getInterestStartsDate().format(DateFormatters.dateFormatter1) + "~" + termlyViewModel.getInterestEndDate().format(DateFormatters.dateFormatter1));
        else if (startsDate != null)
            detailView.addDetailRow(getString(R.string.boc_overview_detail_regular_info_start_end_date), termlyViewModel.getInterestStartsDate().format(DateFormatters.dateFormatter1) + "~");
        else if (endDate != null)
            detailView.addDetailRow(getString(R.string.boc_overview_detail_regular_info_start_end_date), "~" + termlyViewModel.getInterestEndDate().format(DateFormatters.dateFormatter1));

        //设置转存标志
        if (StringUtils.isEmptyOrNull(termlyViewModel.getConvertType()))
            detailView.addDetailRowWithNullShow(getString(R.string.boc_overview_detail_regular_info_convert_type), "");
        else {
            if (termlyViewModel.isAutoConvertType())
                detailView.addDetailRow(getString(R.string.boc_overview_detail_regular_info_convert_type), getString(R.string.boc_overview_detail_regular_info_convert_type_auto));
            else
                detailView.addDetailRow(getString(R.string.boc_overview_detail_regular_info_convert_type), getString(R.string.boc_overview_detail_regular_info_convert_type_no_auto));

        }
        //设置存折册号,存单号
        detailView.addDetailRow(getString(R.string.boc_overview_detail_regular_info_volume_number), termlyViewModel.getVolumeNumber());
        detailView.addDetailRow(getString(R.string.boc_overview_detail_regular_info_cd_number), termlyViewModel.getCdNumber());

        //设置状态
        String accountStatus = PublicCodeUtils.getFacilityChildStatusCode(mContext, termlyViewModel.getStatus());
        detailView.addDetailRowNotLine(getString(R.string.boc_overview_detail_regular_info_status), accountStatus);

        //销户不显示底部按钮
        if (TermlyViewModel.STATUS_CANCEL.equals(termlyViewModel.getStatus()))
            return;

        //设置底部按钮
        String type = termlyViewModel.getType();
        if (type.equals(AccountTypeUtil.REGULAR_TYPE_TZCK)) {
            detailView.updateBottonBtn(getString(R.string.boc_overview_detail_regular_info_button_notice), getResources().getColor(R.color.boc_main_button_color));
            btnBottom.setVisibility(View.VISIBLE);
            btnBottom.setButtonText(getString(R.string.boc_overview_detail_regular_info_button_draw), getResources().getColor(R.color.boc_main_button_color));
        } else {
            detailView.updateBottonBtn(getString(R.string.boc_overview_detail_regular_info_button_draw), getResources().getColor(R.color.boc_main_button_color));
        }
    }

    @Override
    public void setListener() {
        detailView.setOnclick(this);
        btnBottom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        goAtmWithDrawal(true);
    }

    @Override
    public void onClickListener() {
        if (termlyViewModel.getType().equals(AccountTypeUtil.REGULAR_TYPE_TZCK))
            ModuleActivityDispatcher.dispatch(mActivity, ModuleCode.MODEUL_DEPTSTORAGECASH_5);
        else
            goAtmWithDrawal(false);
    }

    /**
     * @param isTZCK
     */
    private void goAtmWithDrawal(boolean isTZCK) {
        Map<String, Object> map = new HashMap<>();
        map.put("accountId", termlyViewModel.getAccountId());
        map.put("status", termlyViewModel.getStatus());
        map.put("type", termlyViewModel.getType());
        map.put("convertType", termlyViewModel.getConvertType());
        map.put("currencyCode", termlyViewModel.getCurrencyCode());
        map.put("cashRemit", termlyViewModel.getCashRemit());
        map.put("volumeNumber", termlyViewModel.getVolumeNumber());
        map.put("cdPeriod", termlyViewModel.getCdPeriod());
        map.put("cdNumber", termlyViewModel.getCdNumber());
        map.put("interestRate", termlyViewModel.getInterestRate());
        map.put("pingNo", termlyViewModel.getPingNo());
        map.put("appointStatus", termlyViewModel.getAppointStatus());
        if (termlyViewModel.getBookBalance() != null)
            map.put("bookBalance", termlyViewModel.getBookBalance().toString());
        if (termlyViewModel.getAvailableBalance() != null)
            map.put("availableBalance", termlyViewModel.getAvailableBalance().toString());
        if (termlyViewModel.getMonthBalance() != null)
            map.put("monthBalance", termlyViewModel.getMonthBalance().toString());
        if (termlyViewModel.getOpenDate() != null)
            map.put("openDate", termlyViewModel.getOpenDate().format(DateFormatters.dateFormatter1));
        if (termlyViewModel.getInterestEndDate() != null)
            map.put("interestEndDate", termlyViewModel.getInterestEndDate().format(DateFormatters.dateFormatter1));
        if (termlyViewModel.getInterestStartsDate() != null)
            map.put("interestStartsDate", termlyViewModel.getInterestStartsDate().format(DateFormatters.dateFormatter1));
        if (termlyViewModel.getSettlementDate() != null)
            map.put("settlementDate", termlyViewModel.getSettlementDate().format(DateFormatters.dateFormatter1));

        ModuleDispatcher.gotoWithdrawDeposits(mActivity, map, MapUtils.clzzField2Map(accountBean), isTZCK ? "1" : "0");
    }
}
