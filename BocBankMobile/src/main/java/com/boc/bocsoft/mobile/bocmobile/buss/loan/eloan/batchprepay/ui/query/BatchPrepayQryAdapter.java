package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.ui.query;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者：liuzc
 * 创建时间：2016/9/7
 * 描述：中银E贷-批量提前还款-列表选择页-listview的adapter
 */
public class BatchPrepayQryAdapter extends BaseListAdapter<BatchPrepayQryBean> {
    private boolean[] selectedStates;
    private int selectedCount;
    private boolean[] enabledStates;

    private String mSelectedCurrencyCode;
    private boolean fromSelectMax;//之前一次选择操作选够了最大选择数
    public static final int SELECT_COUNT_MAX = 5;//最大选择数
    private OnSelectListener mOnSelectListener;

    public BatchPrepayQryAdapter(Context context) {
        super(context);
    }

    @Override
    public void setDatas(List<BatchPrepayQryBean> datas) {
        super.setDatas(datas);

        if(getCount() < 1){
            return;
        }

        if(selectedStates == null || selectedStates.length < 1){
            selectedStates = new boolean[getCount()];
            enabledStates = new boolean[getCount()];
            Arrays.fill(enabledStates, true);
        }
        else{
            updateDataStatus(datas);
        }
    }

    /**
     * 更新选择、可用状态
     * @param datas
     */
    private void updateDataStatus(List<BatchPrepayQryBean> datas){
        //记录之前的选中状态
        int selStatesCount = selectedStates.length;
        for(int i = 0; i < datas.size(); i ++){
            if(i < selStatesCount){
                datas.get(i).setSelected(selectedStates[i]);
            }
            else{
                datas.get(i).setSelected(false);
            }
        }

        selectedStates = new boolean[getCount()];
        for(int i = 0; i < getCount(); i ++){
            selectedStates[i] = datas.get(i).isSelected();
        }

        //记录可用状态
        boolean[] tempEanabledStates = new boolean[getCount()];
        for(int i = 0; i < getCount(); i ++){
            int lastItemCount = enabledStates.length;
            boolean bNewItemEnabled = (getSelectedCount() < BatchPrepayQryAdapter.SELECT_COUNT_MAX);
            if(i < lastItemCount){
                tempEanabledStates[i] = enabledStates[i];
            }
            else{
                tempEanabledStates[i] = bNewItemEnabled;
            }
        }
        enabledStates = tempEanabledStates;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BatchPrepayQryBean mdoel = getItem(position);
        PsnLOANListEQueryResult.PsnLOANListEQueryBean item = mdoel.getBean();
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.boc_item_eloan_batch_prepay_qry, null);
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

//        convertView.setBackgroundColor(
//                mContext.getResources().getColor(R.color.boc_common_cell_color));

        viewHolder.btnCheckbox.setSelected(selectedStates[position]);
        String toDate = item.getLoanCycleMatDate() == null ? ""
                : item.getLoanCycleMatDate();
        viewHolder.tvTodate.setText(
                mContext.getString(R.string.boc_pledge_deposit_todate, toDate));
        viewHolder.tvPeriod.setText(String.format("%s%s",
                mContext.getResources().getString(R.string.boc_pledge_info_period),
                mContext.getResources().getString(R.string.boc_pledge_info_period_month, item.getLoanCycleLifeTerm())));
        viewHolder.tvCurrency.setText(mContext.getResources().getString(R.string.boc_eloan_prepay_outstanding_Principal));
        viewHolder.tvMoney.setText(
                MoneyUtils.transMoneyFormat(item.getRemainCapital(),item.getCurrencyCode()));

        //根据是否可提前还款设置复选款的可见性
        if(canPrepay(item)){
            viewHolder.btnCheckbox.setVisibility(View.VISIBLE);

            viewHolder.btnCheckbox.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    onCheck(position);
                    if(mOnSelectListener != null){
                        mOnSelectListener.onSelect(position, selectedStates[position]);
                    }
                }
            });
        }
        else{
            viewHolder.btnCheckbox.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    /**
     * item对应记录是否可提前还款
     * @param item
     * @return
     */
    private boolean canPrepay(PsnLOANListEQueryResult.PsnLOANListEQueryBean item){
        if(item == null){
            return false;
        }
        if(item.getTransFlag() != null && item.getTransFlag().equals("advance")
                && item.getOverDueStat() != null && item.getOverDueStat().equals("00")){
            return true;
        }
        return false;
    }

    public void setChecked(int position, boolean checked) {
        //禁用状态的条目，略过
        if(!enabledStates[position]){
            return;
        }
        //不可提前还款（无复选框）的条目，略过
        if(!canPrepay(getItem(position).getBean())){
            return;
        }
        selectedStates[position] = checked;
        getItem(position).setSelected(checked);
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
            enabledStates[i] = mSelectedCurrencyCode.equals(mList.get(i).getBean().getCurrencyCode());
        }
    }

    /**
     * 控制点击checkbox
     */
    public void onCheck(int position) {
        setChecked(position, !selectedStates[position]);
        if (getSelectedCount() == 1
                && mSelectedCurrencyCode == null) {//只能选择同币种进行还款。选择了一种币种后，相对应不同于选择的币种全部置灰
            setSelectedCurrencyCode(getItem(position).getBean().getCurrencyCode());
            if (mOnSelectListener != null) {
                mOnSelectListener.onFirstSelect();
            }
        } else if (getSelectedCount() == 0) {//取消了唯一一个选中，现在没有一个选中。置灰的恢复原始状态。
            setSelectedCurrencyCode(null);
            if (mOnSelectListener != null) {
                mOnSelectListener.onUnselectAll();
            }
        } else if (getSelectedCount()
                == SELECT_COUNT_MAX) {//同币种最多只能选择5条记录。进行还款当选择了5条同币种之后，剩下的全部置灰
            fromSelectMax = true;
            System.arraycopy(selectedStates, 0, enabledStates, 0, enabledStates.length);
        } else if (getSelectedCount() == SELECT_COUNT_MAX - 1
                && fromSelectMax) {//上一个操作选够了5个，现在少选一个，其他同币种不用置灰了
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

    public ArrayList<BatchPrepayQryBean> getSelectList() {
        ArrayList<BatchPrepayQryBean> result = new ArrayList<>();
        for (int i = 0; i < getCount(); i++)
            if (selectedStates[i]) {
                result.add(getItem(i));
            }
        return result;
    }

    /**
     * 计算选中条目的本息合计
     * @return
     */
    public BigDecimal getTotalSelectedCapital(){
        BigDecimal result = new BigDecimal(0);
        for(int i = 0; i < getCount(); i ++){
            BatchPrepayQryBean bean = getItem(i);
            if(bean.isSelected()){
                result = result.add(new BigDecimal(bean.getBean().getRemainCapital()));
            }
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
