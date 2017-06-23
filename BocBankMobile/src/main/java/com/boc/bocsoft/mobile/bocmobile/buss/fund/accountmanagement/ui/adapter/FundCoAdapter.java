package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.FundRegCompanyModel;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * 基金-账户管理-选择注册登记机构List的适配器（含参数：首字母）
 * Created by lyf7084 on 2016/12/27.
 */
public class FundCoAdapter extends BaseAdapter {
    private Context mContext;

    private FundRegCompanyModel viewModel;

    public FundCoAdapter(Context context) {
        mContext = context;
    }

    public void setViewModel(FundRegCompanyModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public int getCount() {
        return ((viewModel == null || viewModel.getList() == null) ? 0 : viewModel.getList().size
                ());
    }

    @Override
    public Object getItem(int position) {
        if (viewModel == null || viewModel.getList() == null) {
            return null;
        }
        return viewModel.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 每条带首字母辨识的item-convertView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_fund_company,
                    null);

            viewHolder.llyFirstLetter = (LinearLayout) convertView.findViewById
                    (R.id.llyFirstLetter);
            ;
            viewHolder.tvFirstLetter = (TextView) convertView.findViewById(R.id.tvFirstLetter);
            viewHolder.tvValue = (TextView) convertView.findViewById(R.id.tvValue);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String companyName = viewModel.getList().get(position).getFundRegName(); //公司名称
        String firstLetters = viewModel.getList().get(position).getFundRegLetterTitle(); //公司名称对应的首字母

        if (StringUtils.isEmptyOrNull(firstLetters)) {
            //不显示字母
            viewHolder.llyFirstLetter.setVisibility(View.GONE);
        } else {
            //显示字母标题
            viewHolder.llyFirstLetter.setVisibility(View.VISIBLE);
            viewHolder.tvFirstLetter.setText(firstLetters);
        }

        viewHolder.tvValue.setText(companyName);

        return convertView;
    }

    class ViewHolder {
        LinearLayout llyFirstLetter;                                                 //名称布局
        TextView tvFirstLetter;                                                        //首字母view
        TextView tvValue;                                                               //内容
    }

}
