package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;
import com.squareup.picasso.Picasso;

import static com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils.getCardPic;

/**
 * Name: liukai
 * Time：2016/12/7 17:16.
 * Created by lk7066 on 2016/12/7.
 * It's used to 附属卡首页适配器
 */

public class AttCardAdapter extends BaseListAdapter<AttCardModel> {

    //list列表
    private ListView listView;
    //资源文件Id
    private int resourceId;
    private View rootView;
    //主卡账户卡号
    private TextView crcdAccount;
    //主卡别名，昵称
    private TextView crcdName;
    //附属卡卡面图片
    private ImageView attCardImg;
    //附属卡卡号
    private TextView attCardNumber;
    //附属卡户主名字
    private TextView attCardName;

    public AttCardAdapter(ListView listView, Context context) {
        super(context);
        this.listView = listView;
    }

    public AttCardAdapter(Context context, int lvResourceId){
        super(context);
        resourceId = lvResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AttCardModel viewModel = getItem(position);
        rootView = LayoutInflater.from(mContext).inflate(resourceId, null);

        crcdAccount = (TextView) rootView.findViewById(R.id.tv_attcard_crcd_account);
        crcdName = (TextView) rootView.findViewById(R.id.tv_attcard_crcd_name);
        attCardImg = (ImageView) rootView.findViewById(R.id.iv_attcard_pic);
        attCardNumber = (TextView) rootView.findViewById(R.id.tv_attcard_number);
        attCardName = (TextView) rootView.findViewById(R.id.tv_attcard_name);

        //getFlag获取到的标志位标识是否为第一张附属卡，如果是，那么显示主卡的信息，如果不是，隐藏主卡的信息
        if(1 == viewModel.getFlag()){

            crcdAccount.setVisibility(View.VISIBLE);
            crcdName.setVisibility(View.VISIBLE);

            if(viewModel.getMasterCrcdProductName() != null) {
                crcdAccount.setText(NumberUtils.formatCardNumber(viewModel.getMasterCrcdNum()));
                crcdName.setText(viewModel.getMasterCrcdProductName());
            } else {
                crcdAccount.setText(NumberUtils.formatCardNumber(viewModel.getMasterCrcdNum()));
            }

        } else {

            crcdAccount.setVisibility(View.GONE);
            crcdName.setVisibility(View.GONE);

        }

        //附属卡卡面图片，卡号，户主姓名设置
        Picasso.with(mContext).load(getCardPic(AccountUtils.getCardType(viewModel.getMasterCrcdType()))).into(attCardImg);
        attCardNumber.setText(NumberUtils.formatCardNumber(viewModel.getSubCrcdNum()));
        attCardName.setText(viewModel.getSubCrcdHolder());

        return rootView;

    }

}
