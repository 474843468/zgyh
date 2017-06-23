package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.deposit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PersonalTimeAccountBean;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者：XieDu
 * 创建时间：2016/8/11 9:44
 * 描述：
 */
public class PledgeReceiptAdapter extends BaseListAdapter<PersonalTimeAccountBean> {

    private boolean[] selectedStates;
    private int selectedCount;
    private boolean[] enabledStates;

    private String mSelectedCurrencyCode;
    private boolean fromSelectMax;//之前一次选择操作选够了最大选择数
    private final int SELECT_COUNT_MAX = 6;//最大选择数
    private OnSelectListener mOnSelectListener;

    public PledgeReceiptAdapter(Context context) {
        super(context);
    }

    @Override
    public void setDatas(List<PersonalTimeAccountBean> datas) {
        super.setDatas(datas);
        selectedCount=0;
        mSelectedCurrencyCode = null;
        selectedStates = new boolean[getCount()];
        enabledStates = new boolean[getCount()];
        Arrays.fill(enabledStates, true);
        fromSelectMax = false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PersonalTimeAccountBean item = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.boc_item_pledge_deposit_receipt, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (enabledStates[position]) {
            convertView.setBackgroundColor(
                    mContext.getResources().getColor(R.color.boc_common_cell_color));
        } else {
            convertView.setBackgroundColor(
                    mContext.getResources().getColor(R.color.boc_main_btn_bg_color));
        }
        viewHolder.btnCheckbox.setSelected(selectedStates[position]);
        String toDate = item.getInterestEndDate() == null ? ""
                : item.getInterestEndDate().format(DateFormatters.dateFormatter1);
        viewHolder.tvTodate.setText(
                String.format(mContext.getString(R.string.boc_pledge_deposit_todate), toDate));
        viewHolder.tvPeriod.setText(getCdPeriodString(item.getCdPeriod()));
        viewHolder.tvCurrency.setText(
                PublicCodeUtils.getCurrency(mContext, item.getCurrencyCode()));
        viewHolder.tvMoney.setText(
                MoneyUtils.transMoneyFormatNoLossAccuracy(item.getAvailableBalance(), item.getCurrencyCode()));
        return convertView;
    }

    /**
     * 存期
     * 1-1天通知存款
     * 7-7天通知存款
     * 01- 1个月
     * 03- 3个月
     * 06- 6个月
     * 12- 1年
     * 24- 2年
     * 36- 3年
     * 60- 5年
     * 72- 6年
     * 00- 不固定期限
     */
    public String getCdPeriodString(String cdPeriod) {
        if (StringUtils.isEmpty(cdPeriod)) {
            return "";
        }
        if ("00".equals(cdPeriod)) {
            return mContext.getString(R.string.boc_pledge_deposit_period_no_limit);
        }
        if ("1".equals(cdPeriod)) {
            return mContext.getString(R.string.boc_pledge_deposit_period_1day);
        }
        if ("7".equals(cdPeriod)) {
            return mContext.getString(R.string.boc_pledge_deposit_period_7day);
        }
        if ("3".equals(cdPeriod)) {
            return String.format(mContext.getString(R.string.boc_pledge_deposit_period_month),
                    cdPeriod);
        }
        if ("6".equals(cdPeriod)) {
            return String.format(mContext.getString(R.string.boc_pledge_deposit_period_month),
                    cdPeriod);
        }
        if (cdPeriod.length() == 1) {
            return "";
        }
        int periodNumber = Integer.parseInt(cdPeriod);
        if (periodNumber < 12) {
            return String.format(mContext.getString(R.string.boc_pledge_deposit_period_month),
                    periodNumber);
        } else {
            return String.format(mContext.getString(R.string.boc_pledge_deposit_period_year),
                    periodNumber / 12);
        }
    }

    public void setChecked(int position, boolean checked) {
        selectedStates[position] = checked;
        if (checked) {
            selectedCount++;
        } else {
            selectedCount--;
        }
    }

    public boolean getSelected(int position) {
        return selectedStates[position];
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public String getSelectedCurrencyCode() {
        return mSelectedCurrencyCode;
    }

    public void setSelectedCurrencyCode(final String selectedCurrencyCode) {
        mSelectedCurrencyCode = selectedCurrencyCode;
        if (mSelectedCurrencyCode == null) {
            Arrays.fill(enabledStates, true);
        } else {
            setEnableByCurrency();
        }
    }

    private void setEnableByCurrency() {
        for (int i = 0; i < mList.size(); i++) {
            enabledStates[i] = mSelectedCurrencyCode.equals(mList.get(i).getCurrencyCode());
        }
    }

    /**
     * 控制点击checkbox
     */
    public void onCheck(int position) {
        setChecked(position, !selectedStates[position]);
        if (getSelectedCount() == 1
                && mSelectedCurrencyCode == null) {//只能选择同币种存单进行贷款。选择了一种币种后，相对应不同于选择的币种全部置灰
            setSelectedCurrencyCode(getItem(position).getCurrencyCode());
            if (mOnSelectListener != null) {
                mOnSelectListener.onFirstSelect();
            }
        } else if (getSelectedCount() == 0) {//取消了唯一一个选中，现在没有一个选中。置灰的恢复原始状态。
            setSelectedCurrencyCode(null);
            if (mOnSelectListener != null) {
                mOnSelectListener.onUnselectAll();
            }
        } else if (getSelectedCount()
                == SELECT_COUNT_MAX) {//同币种最多只能选择6条存单。进行贷款当选择了6条同币种之后，剩下的存单全部置灰
            fromSelectMax = true;
            System.arraycopy(selectedStates, 0, enabledStates, 0, enabledStates.length);
        } else if (getSelectedCount() == SELECT_COUNT_MAX - 1
                && fromSelectMax) {//上一个操作选够了6个，现在少选一个，其他同币种存单不用置灰了
            fromSelectMax = false;
            setEnableByCurrency();
        }
        if (getSelectedCount() != 0 && mOnSelectListener != null) {
            mOnSelectListener.onSelect(position, selectedStates[position]);
        }
        notifyDataSetChanged();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return enabledStates[position];
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public ArrayList<PersonalTimeAccountBean> getSelectDepositReceiptList() {
        ArrayList<PersonalTimeAccountBean> result = new ArrayList<>();
        for (int i = 0; i < getCount(); i++)
            if (selectedStates[i]) {
                result.add(getItem(i));
            }
        return result;
    }

    private class ViewHolder {

        protected ImageView btnCheckbox;
        protected TextView tvTodate;
        protected TextView tvPeriod;
        protected TextView tvMoney;
        protected TextView tvCurrency;

        ViewHolder(View itemView) {
            btnCheckbox = (ImageView) itemView.findViewById(R.id.btn_checkbox);
            tvTodate = (TextView) itemView.findViewById(R.id.tv_todate);
            tvPeriod = (TextView) itemView.findViewById(R.id.tv_period);
            tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
            tvCurrency = (TextView) itemView.findViewById(R.id.tv_currency);
        }
    }

    public interface OnSelectListener {
        void onFirstSelect();

        void onUnselectAll();

        void onSelect(int position, boolean selectedState);
    }
}
