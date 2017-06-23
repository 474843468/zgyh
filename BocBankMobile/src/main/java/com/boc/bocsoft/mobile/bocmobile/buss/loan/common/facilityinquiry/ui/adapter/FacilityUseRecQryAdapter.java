package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAccountListAndDetailQuery.PsnLOANAccountListAndDetailQueryResult;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.FacilityInquiryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.model.FacilityUseRecordViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.ui.otherloanqrylistview.OtherLoanQryBean;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 额度使用记录列表适配器
 * <p/>
 * Created by liuzc on 2016/9/10.
 */
public class FacilityUseRecQryAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    private ClickListener mListener = null;

    //页面显示信息列表
    private List<PsnLOANAccountListAndDetailQueryResult.ListBean> list;

    private boolean bRepayOver = false; //是否已结清，默认为false

    public FacilityUseRecQryAdapter(Context context) {
        this.context = context;
        list = new ArrayList<PsnLOANAccountListAndDetailQueryResult.ListBean>();
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<PsnLOANAccountListAndDetailQueryResult.ListBean> list) {
        this.list = list;
    }

    public void setIsRepayOver(boolean value){
        bRepayOver = value;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.boc_item_facility_used_record, null);

            viewHolder.tvType = (TextView)convertView.findViewById(R.id.tv_type);;
            viewHolder.tvPerAndRate = (TextView)convertView.findViewById(R.id.tv_period_rate);
            viewHolder.tvAmount = (TextView)convertView.findViewById(R.id.tv_amount);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClick(position);
                }
            }
        });

        PsnLOANAccountListAndDetailQueryResult.ListBean bean = list.get(position);

        String loanType =
                PublicCodeUtils.getLoanTypeName(context, bean.getLoanType());

        String perAndRate = String.format("%s%s / %s%s",
                bean.getLoanPeriod(),
                context.getResources().getString(R.string.boc_period_unit),
                String.valueOf(bean.getLoanRate()),
                "%");

//        if(!bRepayOver){
//            perAndRate = String.format("%s%s%s / %s%s",
//                    context.getResources().getString(R.string.boc_loan_remain),
//                    bean.getRemainIssue(),
//                    context.getResources().getString(R.string.boc_period_unit),
//                    String.valueOf(bean.getLoanRate()),
//                    "%");
//        }

        BigDecimal loanAmount = bean.getLoanAmount();
        String currencyCode = bean.getCurrencyCode();
        String amount = (loanAmount == null || StringUtils.isEmpty(currencyCode)) ? "-"
                : MoneyUtils.transMoneyFormat(loanAmount, currencyCode);
        viewHolder.tvType.setText(loanType);
        viewHolder.tvPerAndRate.setText(perAndRate);
        viewHolder.tvAmount.setText(amount);
        return convertView;
    }

    public void setOnClickListener(ClickListener listener){
        mListener = listener;
    }

    public interface ClickListener {
        void onItemClick(int position);
    }

    class ViewHolder{
        protected TextView tvType; //贷款类型
        protected TextView tvPerAndRate; //期限、利率
        protected TextView tvAmount; //额度
    }
}
