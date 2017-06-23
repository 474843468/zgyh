package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.model.XpadAccountModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;
import com.squareup.picasso.Picasso;
import static com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils.getCardPic;

/**
 * 理财——主页数据适配器
 * Created by Wan mengxin on 2016/9/17.
 */
public class MainAdapter extends BaseListAdapter<XpadAccountModel> {

    public MainAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.boc_fragment_list_account_item, null);
            holder = new ViewHolder();
            holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
            holder.tvaccountNum = (TextView) convertView.findViewById(R.id.tvaccountNum);
            holder.tvaccountName = (TextView) convertView.findViewById(R.id.tvaccountName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        XpadAccountModel entity = getItem(position);
        String accountType = entity.getAccountType();
        String accountName = PublicCodeUtils.changeaccounttypetoName(accountType);

        Picasso.with(mContext).load(getCardPic(AccountUtils.getCardType(accountType))).into(holder.ivPic);
        holder.tvaccountNum.setText(NumberUtils.formatCardNumberStrong(entity.getAccountNumber()));
        holder.tvaccountName.setText(accountName);
        return convertView;
    }

    static class ViewHolder {
        public ImageView ivPic;
        public TextView tvaccountNum;
        public TextView tvaccountName;
    }
}
