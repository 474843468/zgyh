    package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.adapter;

    import android.content.Context;
    import android.text.TextUtils;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;

    import android.widget.Filter;
    import android.widget.Filterable;
    import android.widget.TextView;
    import com.boc.bocsoft.mobile.bocmobile.R;
    import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.TransRecentPayeeBean;
    import com.boc.bocsoft.mobile.common.utils.NumberUtils;
    import com.boc.bocsoft.mobile.common.utils.StringUtils;

    import java.util.ArrayList;
    import java.util.List;

    /**
     * 收款人列表适配器
     * Created by wangyuan on 2016-8-30
     */
    public class RecentPayeeListAdapter extends BaseAdapter implements Filterable {

    //    /\好好考虑下位置关系，过滤关系，到底是展示的是哪个，取值的是哪个list
        private Context context;
        private String filterWord;
        private List<TransRecentPayeeBean> recentPayeeList;//近期收款人
        private List<TransRecentPayeeBean> commenPayeeList;//常用收款人
        private List<TransRecentPayeeBean> payeeListToshow;//原始数据
    //    private List<TransRecentPayeeBean> recentPayeeListFiltered;//过滤后数据
        private PayeeFilterByNameFilter payeeFilter;
        public  RecentPayeeListAdapter(Context context, List<TransRecentPayeeBean> recentpayeeList,List<TransRecentPayeeBean> commpayeeList) {
            this.context = context;
            this.recentPayeeList = recentpayeeList;
            this.commenPayeeList=commpayeeList;
        }

    //    public RecentPayeeListAdapter( List<TransRecentPayeeBean> payeeList) {
    //        this.recentPayeeList = payeeList;
    //        this.payeeListToshow=payeeList;
    //    }

    //    public  List<TransRecentPayeeBean> getRecentPayeeList() {
    //        return payeeListToshow;
    //    }
    //    public  List<TransRecentPayeeBean> getFilteredRecentPayeeList() {
    //        return recentPayeeListFiltered;
    //    }
        public void setRecentPayeeList(List<TransRecentPayeeBean> payeeList){
            this.payeeListToshow =payeeList;
            notifyDataSetChanged();
        }

    //    public void setDatatoFilter(String type){
    //        if ("1".equals(type)){
    //            setRecentPayeeList(recentPayeeList);
    //        }
    //        if ("0".equals(type)){
    //            setRecentPayeeList(commenPayeeList);
    //        }
    //    }

        @Override
        public int getCount() {
            if (payeeListToshow != null && payeeListToshow.size() > 0) {
                return payeeListToshow.size();
            }else {
                return 0;
            }
        }
        @Override
        public TransRecentPayeeBean getItem(int position) {
            return payeeListToshow.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context,
                        R.layout.list_item_payee_recent, null);
            }
            ViewHolder holder = ViewHolder.getHolder(convertView);
            TransRecentPayeeBean payeeEntity = getItem(position); //
            holder.name.setText(payeeEntity.getAccountName()); // 收款人帐户名称
            holder.tv_account_number.setText(NumberUtils.formatCardNumberStrong(payeeEntity.getAccountNumber())); // 收款人银行帐号
            holder.tv_bank_name.setText(payeeEntity.getBankName()); // 收款人银行名称
            holder.view_separate_line.setVisibility(TextUtils.isEmpty(payeeEntity.getBankName()) ? View.GONE : View.VISIBLE);
            holder.tv_ding_xiang.setVisibility("1".equals(payeeEntity.getIsAppointed()) ? View.VISIBLE : View.GONE); // 设置“定向”的显示与否
            holder.tv_shi_shi.setVisibility("3".equals(payeeEntity.getBocFlag()) ? View.VISIBLE : View.GONE); // 设置“实时”的显示与否
            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (null==payeeFilter){
                payeeFilter=new PayeeFilterByNameFilter();
            }
            return payeeFilter;
        }

        static class ViewHolder {
            TextView name, tv_account_number, tv_bank_name, tv_ding_xiang, tv_shi_shi;
            View view_separate_line;

            public ViewHolder(View convertView) {
                name = (TextView) convertView.findViewById(R.id.name);
                tv_account_number = (TextView) convertView.findViewById(R.id.tv_account_number);
                tv_bank_name = (TextView) convertView.findViewById(R.id.tv_bank_name);
                tv_ding_xiang = (TextView) convertView.findViewById(R.id.tv_ding_xiang);
                tv_shi_shi = (TextView) convertView.findViewById(R.id.tv_shi_shi);
                view_separate_line = convertView.findViewById(R.id.view_separate_line); // 银行号码和银行名称中间的分割线
            }

            public static ViewHolder getHolder(View convertView) {
                ViewHolder viewHolder = (ViewHolder) convertView.getTag();
                if (viewHolder == null) {
                    viewHolder = new ViewHolder(convertView);
                    convertView.setTag(viewHolder);
                }
                return viewHolder;
            }
        }
        private class  PayeeFilterByNameFilter extends Filter{
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResult=new FilterResults();
                String prefixString=String.valueOf(constraint).toString();
                if (null == payeeListToshow) {
                    payeeListToshow = new ArrayList<>();
                }
           if (constraint.length()!=0) {
               payeeListToshow.clear();
               for (TransRecentPayeeBean onePayee : commenPayeeList) {
                   if (onePayee.getAccountName().contains(prefixString)) {
                       payeeListToshow.add(onePayee);
                       if (payeeListToshow.size() == 5) {
                           break;
                       }
                   }
               }
           }else{
               payeeListToshow.clear();
               payeeListToshow=recentPayeeList;
           }
                filterResult.values = payeeListToshow;
                filterResult.count = payeeListToshow.size();
                return filterResult;
        }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (constraint.length()!=0){
                    RecentPayeeListAdapter.this.setRecentPayeeList((ArrayList<TransRecentPayeeBean>)results.values);//??
                }else{
                    RecentPayeeListAdapter.this.setRecentPayeeList(recentPayeeList);//??
                }
    //            if (constraint!=null){
    ////                if (results!=null&&results.count>0){
    //                    RecentPayeeListAdapter.this.setRecentPayeeList((ArrayList<TransRecentPayeeBean>)results.values);//??
    //            }else{
    //                    setRecentPayeeList(recentPayeeList);
    //            }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                TransRecentPayeeBean onebean=(TransRecentPayeeBean)resultValue;
                return onebean.getAccountName();
            }
        }
    }